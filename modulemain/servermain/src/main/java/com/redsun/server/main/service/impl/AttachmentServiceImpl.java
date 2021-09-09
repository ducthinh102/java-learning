

package com.redsun.server.main.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.main.common.Constant;
import com.redsun.server.main.controller.common.ServerException;
import com.redsun.server.main.model.Attachment;
import com.redsun.server.main.model.History;
import com.redsun.server.main.model.common.SearchCriteria;
import com.redsun.server.main.repository.AttachmentRepository;
import com.redsun.server.main.repository.HistoryRepository;
import com.redsun.server.main.repository.specification.AttachmentSpecification;
import com.redsun.server.main.repository.specification.AttachmentSpecificationsBuilder;
import com.redsun.server.main.service.AttachmentService;
import com.redsun.server.main.util.SecurityUtil;

@Service("attachment")
@Transactional
public class AttachmentServiceImpl implements AttachmentService {
	
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private AttachmentRepository attachmentRepository;
	
	@Autowired
	private AttachmentSpecificationsBuilder attachmentSpecificationsBuilder;

	@Override
	public Attachment save(Attachment attachment) {
		return attachmentRepository.save(attachment);
	}

	@Override
	public Attachment create(Attachment attachment) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Current date;
		Date currentDate = new Date();
		attachment.setStatus(Constant.SERVERDB_STATUS_NEW);
		attachment.setIdcreate(iduser);
		attachment.setCreatedate(currentDate);
		attachment.setIdowner(iduser);
		attachment.setIdupdate(iduser);
		attachment.setUpdatedate(currentDate);
		attachment.setVersion(1);
		return attachmentRepository.save(attachment);
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = attachmentRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = attachmentRepository.updateUnlock(id);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;
	}
	
	private Map<String, Object> updatePre(Map<String, Object> params) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		// Get value from params.
		Integer iduser = (Integer) params.get("iduser");
		Integer id = (Integer) params.get("id");
		Integer version = (Integer) params.get("version");
		Attachment attachment = (Attachment) params.get("attachment");
		// Get from DB.
		Attachment attachmentDb = attachmentRepository.findOne(id);
		if(attachmentDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if(attachmentDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (attachmentDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else {
			// Keep history data.
			String historyStr = attachmentDb.toString();
			// Increase version.
			attachmentDb.setVersion(attachmentDb.getVersion() + 1);
			// return.
			result.put("attachmentDb", attachmentDb);
			result.put("historyStr", historyStr);
			return result;
		}
	}

	private Map<String, Object> updatePost(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		// Get value from params.
		Integer id = (Integer) params.get("id");
		String historyStr = (String) params.get("historyStr");
		// Save history.
		History history = new History(id, "attachment", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}
	
	@Override
	public Attachment update(Integer id, Attachment attachment) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", attachment.getVersion());
		params.put("attachment", attachment);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Attachment attachmentDb = (Attachment) resultPre.get("attachmentDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(attachment, attachmentDb, ignoreProperties);
		Date currentDate = new Date();
		attachmentDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		attachmentDb.setIdupdate(iduser);
		attachmentDb.setUpdatedate(currentDate);
		attachmentDb.setIdowner(iduser);
		// Save.
		attachmentDb = attachmentRepository.save(attachmentDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return attachmentDb;
	}

	@Override
	public Attachment updateWithLock(Integer id, Attachment attachment) throws JsonParseException, JsonMappingException, IOException {
		Attachment result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, attachment);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}
	
	@Override
	public Attachment updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Attachment attachmentDb = (Attachment) resultPre.get("attachmentDb");
		Date currentDate = new Date();
		attachmentDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		attachmentDb.setIddelete(iduser);
		attachmentDb.setDeletedate(currentDate);
		// Save.
		attachmentDb = attachmentRepository.save(attachmentDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return attachmentDb;
	}

	@Override
	public Attachment updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		Attachment result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}


	@Override
	public void delete(Attachment attachment) {
		attachmentRepository.delete(attachment);
	}

	@Override
	public void deleteById(Integer id) {
		attachmentRepository.delete(id);
	}

	@Override
	public Attachment getById(Integer id) {
		return attachmentRepository.findOne(id);
	}

	@Override
	public List<Attachment> listAll() {
		return attachmentRepository.findAll();
	}

	@Override
	public long countAll() {
		return attachmentRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return attachmentRepository.exists(id);
	}
	
	public List<Attachment> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Attachment> attachmentSpecification = new AttachmentSpecification(searchCriteria);
		// Where status != delete.
		Specification<Attachment> notDeleteSpec = new AttachmentSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		attachmentSpecification = Specifications.where(attachmentSpecification).and(notDeleteSpec);
		// Execute.
        List<Attachment> result = attachmentRepository.findAll(attachmentSpecification);
        return result;
	}
	
	public List<Attachment> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Attachment> attachmentSpecification = attachmentSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Attachment> notDeleteSpec = new AttachmentSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		attachmentSpecification = Specifications.where(attachmentSpecification).and(notDeleteSpec);
		// Execute.
        List<Attachment> result = attachmentRepository.findAll(attachmentSpecification);
        return result;
	}
	
	public Page<Attachment> listAllByPage(Pageable pageable) {
		return attachmentRepository.findAll(pageable);
	}
	
	public Page<Attachment> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Attachment> attachmentSpecification = new AttachmentSpecification(searchCriteria);
		// Where status != delete.
		Specification<Attachment> notDeleteSpec = new AttachmentSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		attachmentSpecification = Specifications.where(attachmentSpecification).and(notDeleteSpec);
		// Execute.
		Page<Attachment> result = attachmentRepository.findAll(attachmentSpecification, pageable);
        return result;
	}
	
	public Page<Attachment> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Attachment> attachmentSpecification = attachmentSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Attachment> notDeleteSpec = new AttachmentSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		attachmentSpecification = Specifications.where(attachmentSpecification).and(notDeleteSpec);
		// Execute.
		Page<Attachment> result = attachmentRepository.findAll(attachmentSpecification, pageable);
        return result;
	}

}
