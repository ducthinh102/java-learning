

package com.redsun.server.wh.service.impl;

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
import com.redsun.server.wh.common.Constant;
import com.redsun.server.wh.controller.common.ServerException;
import com.redsun.server.wh.model.Comment;
import com.redsun.server.wh.model.History;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.CommentRepository;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.specification.CommentSpecification;
import com.redsun.server.wh.repository.specification.CommentSpecificationsBuilder;
import com.redsun.server.wh.service.CommentService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("comment")
@Transactional
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private CommentSpecificationsBuilder commentSpecificationsBuilder;

	@Override
	public Comment save(Comment comment) {
		return commentRepository.save(comment);
	}

	@Override
	public Comment create(Comment comment) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Current date;
		Date currentDate = new Date();
		comment.setIdwriter(iduser);
		comment.setStatus(Constant.SERVERDB_STATUS_NEW);
		comment.setIdcreate(iduser);
		comment.setCreatedate(currentDate);
		comment.setIdowner(iduser);
		comment.setIdupdate(iduser);
		comment.setUpdatedate(currentDate);
		comment.setVersion(1);
		return commentRepository.save(comment);
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = commentRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = commentRepository.updateUnlock(id);
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
		//Comment comment = (Comment) params.get("comment");
		// Get from DB.
		Comment commentDb = commentRepository.findOne(id);
		if(commentDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if(commentDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (commentDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else {
			// Keep history data.
			String historyStr = commentDb.toString();
			// Increase version.
			commentDb.setVersion(commentDb.getVersion() + 1);
			// return.
			result.put("commentDb", commentDb);
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
		History history = new History(id, "comment", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}
	
	@Override
	public Comment update(Integer id, Comment comment) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", comment.getVersion());
		params.put("comment", comment);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Comment commentDb = (Comment) resultPre.get("commentDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(comment, commentDb, ignoreProperties);
		Date currentDate = new Date();
		commentDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		commentDb.setIdupdate(iduser);
		commentDb.setUpdatedate(currentDate);
		commentDb.setIdowner(iduser);
		// Save.
		commentDb = commentRepository.save(commentDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return commentDb;
	}

	@Override
	public Comment updateWithLock(Integer id, Comment comment) throws JsonParseException, JsonMappingException, IOException {
		Comment result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, comment);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}
	
	@Override
	public Comment updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Comment commentDb = (Comment) resultPre.get("commentDb");
		Date currentDate = new Date();
		commentDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		commentDb.setIddelete(iduser);
		commentDb.setDeletedate(currentDate);
		// Save.
		commentDb = commentRepository.save(commentDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return commentDb;
	}

	@Override
	public Comment updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		Comment result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}


	@Override
	public void delete(Comment comment) {
		commentRepository.delete(comment);
	}

	@Override
	public void deleteById(Integer id) {
		commentRepository.delete(id);
	}

	@Override
	public Comment getById(Integer id) {
		return commentRepository.findOne(id);
	}

	@Override
	public List<Comment> listAll() {
		return commentRepository.findAll();
	}

	@Override
	public long countAll() {
		return commentRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return commentRepository.exists(id);
	}
	
	public List<Comment> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Comment> commentSpecification = new CommentSpecification(searchCriteria);
		// Where status != delete.
		Specification<Comment> notDeleteSpec = new CommentSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		commentSpecification = Specifications.where(commentSpecification).and(notDeleteSpec);
		// Execute.
        List<Comment> result = commentRepository.findAll(commentSpecification);
        return result;
	}
	
	public List<Comment> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Comment> commentSpecification = commentSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Comment> notDeleteSpec = new CommentSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		commentSpecification = Specifications.where(commentSpecification).and(notDeleteSpec);
		// Execute.
        List<Comment> result = commentRepository.findAll(commentSpecification);
        return result;
	}
	
	public Page<Comment> listAllByPage(Pageable pageable) {
		return commentRepository.findAll(pageable);
	}
	
	public Page<Comment> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Comment> commentSpecification = new CommentSpecification(searchCriteria);
		// Where status != delete.
		Specification<Comment> notDeleteSpec = new CommentSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		commentSpecification = Specifications.where(commentSpecification).and(notDeleteSpec);
		// Execute.
		Page<Comment> result = commentRepository.findAll(commentSpecification, pageable);
        return result;
	}
	
	public Page<Comment> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Comment> commentSpecification = commentSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Comment> notDeleteSpec = new CommentSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		commentSpecification = Specifications.where(commentSpecification).and(notDeleteSpec);
		// Execute.
		Page<Comment> result = commentRepository.findAll(commentSpecification, pageable);
        return result;
	}
	
	public Page<Map<String, Object>> listWithCriteriasByIdrefAndReftypeAndPage(Integer idref, String reftype, List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Comment> commentSpecification = commentSpecificationsBuilder.build(searchCriterias);
		// Where idref.
		Specification<Comment> idrefSpec = new CommentSpecification(new SearchCriteria("idref", "=", idref));
		// Where reftype.
		Specification<Comment> reftypeSpec = new CommentSpecification(new SearchCriteria("reftype", "=", reftype));
		// Where status != delete.
		Specification<Comment> notDeleteSpec = new CommentSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		commentSpecification = Specifications.where(commentSpecification).and(idrefSpec).and(reftypeSpec).and(notDeleteSpec);
		// Execute.
		Page<Comment> comments = commentRepository.findAll(commentSpecification, pageable);
		Page<Map<String, Object>> result = comments.map(this::convertToMap);
        return result;
	}

	private Map<String, Object> convertToMap(final Comment comment) {
	    final Map<String, Object> result = new HashMap<String, Object>();
	    result.put("id", comment.getId());
	    result.put("idwriter", comment.getIdwriter());
	    result.put("idref", comment.getIdref());
	    result.put("reftype", comment.getReftype());
	    result.put("content", comment.getContent());
	    result.put("version", comment.getVersion());
	    result.put("username", comment.getWriter().getUsername());
	    result.put("thumbnail", comment.getWriter().getThumbnail());
	    result.put("updatedate", comment.getUpdatedate());
	    return result;
	}

}
