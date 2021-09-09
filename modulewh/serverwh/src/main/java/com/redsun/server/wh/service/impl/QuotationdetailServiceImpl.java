
package com.redsun.server.wh.service.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

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
import com.redsun.server.wh.model.History;
import com.redsun.server.wh.model.Quotation;
import com.redsun.server.wh.model.Quotationdetail;
import com.redsun.server.wh.model.Supplier;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.QuotationRepository;
import com.redsun.server.wh.repository.QuotationdetailRepository;
import com.redsun.server.wh.repository.specification.QuotationdetailSpecification;
import com.redsun.server.wh.repository.specification.QuotationdetailSpecificationsBuilder;
import com.redsun.server.wh.service.QuotationdetailService;
import com.redsun.server.wh.util.CsvFileUtil;
import com.redsun.server.wh.util.MailHelper;
import com.redsun.server.wh.util.SecurityUtil;

@Service("quotationdetail")
@Transactional
public class QuotationdetailServiceImpl implements QuotationdetailService {

	@Autowired
	MailHelper mailHelper;

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private QuotationRepository quotationRepository;

	@Autowired
	private QuotationdetailRepository quotationdetailRepository;

	@Autowired
	private QuotationdetailSpecificationsBuilder quotationdetailSpecificationsBuilder;

	@Override
	public Quotationdetail save(Quotationdetail quotationdetail) {
		return quotationdetailRepository.save(quotationdetail);
	}

	@Override
	public Quotationdetail create(Quotationdetail quotationdetail)
			throws JsonParseException, JsonMappingException, IOException {

		if (quotationdetailRepository.isExistMaterial(quotationdetail.getId(), quotationdetail.getIdmaterial())) {
			throw new ServerException(Constant.SERVERCODE_EXITMATERIAL);
		} else {

			// Get iduser.
			Integer iduser = SecurityUtil.getIdUser();
			// Current date;
			Date currentDate = new Date();
			quotationdetail.setStatus(Constant.SERVERDB_STATUS_NEW);
			quotationdetail.setIdcreate(iduser);
			quotationdetail.setCreatedate(currentDate);
			quotationdetail.setIdowner(iduser);
			quotationdetail.setIdupdate(iduser);
			quotationdetail.setUpdatedate(currentDate);
			quotationdetail.setVersion(1);
			return quotationdetailRepository.save(quotationdetail);

		}

	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = quotationdetailRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if (result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;
	}

	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = quotationdetailRepository.updateUnlock(id);
		if (result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;
	}

	private Map<String, Object> updatePre(Map<String, Object> params)
			throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		// Get value from params.
		Integer iduser = (Integer) params.get("iduser");
		Integer id = (Integer) params.get("id");
		Integer version = (Integer) params.get("version");
		Quotationdetail quotationdetail = (Quotationdetail) params.get("quotationdetail");
		// Get from DB.
		Quotationdetail quotationdetailDb = quotationdetailRepository.findOne(id);
		if (quotationdetailDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if (quotationdetailDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (quotationdetailDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else if (quotationdetail != null && quotationdetailRepository.isExistMaterial(id, quotationdetail.getIdmaterial())) {
			throw new ServerException(Constant.SERVERCODE_EXITMATERIAL);
		} else {
			// Keep history data.
			String historyStr = quotationdetailDb.toString();
			// Increase version.
			quotationdetailDb.setVersion(quotationdetailDb.getVersion() + 1);
			// return.
			result.put("quotationdetailDb", quotationdetailDb);
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
		History history = new History(id, "quotationdetail", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}

	@Override
	public Quotationdetail update(Integer id, Quotationdetail quotationdetail)
			throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", quotationdetail.getVersion());
		params.put("quotationdetail", quotationdetail);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Quotationdetail quotationdetailDb = (Quotationdetail) resultPre.get("quotationdetailDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock",
				"createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(quotationdetail, quotationdetailDb, ignoreProperties);
		Date currentDate = new Date();
		quotationdetailDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		quotationdetailDb.setIdupdate(iduser);
		quotationdetailDb.setUpdatedate(currentDate);
		quotationdetailDb.setIdowner(iduser);
		// Save.
		quotationdetailDb = quotationdetailRepository.save(quotationdetailDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return quotationdetailDb;
	}

	@Override
	public Quotationdetail updateWithLock(Integer id, Quotationdetail quotationdetail)
			throws JsonParseException, JsonMappingException, IOException {
		Quotationdetail result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, quotationdetail);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public Quotationdetail updateForDelete(Integer id, Integer version)
			throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Quotationdetail quotationdetailDb = (Quotationdetail) resultPre.get("quotationdetailDb");
		Date currentDate = new Date();
		quotationdetailDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		quotationdetailDb.setIddelete(iduser);
		quotationdetailDb.setDeletedate(currentDate);
		// Save.
		quotationdetailDb = quotationdetailRepository.save(quotationdetailDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return quotationdetailDb;
	}

	@Override
	public Quotationdetail updateForDeleteWithLock(Integer id, Integer version)
			throws JsonParseException, JsonMappingException, IOException {
		Quotationdetail result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public void delete(Quotationdetail quotationdetail) {
		quotationdetailRepository.delete(quotationdetail);
	}

	@Override
	public void deleteById(Integer id) {
		quotationdetailRepository.delete(id);
	}

	@Override
	public Quotationdetail getById(Integer id) {
		return quotationdetailRepository.findOne(id);
	}

	@Override
	public List<Quotationdetail> listAll() {
		return quotationdetailRepository.findAll();
	}

	@Override
	public long countAll() {
		return quotationdetailRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return quotationdetailRepository.exists(id);
	}

	public List<Quotationdetail> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Quotationdetail> quotationdetailSpecification = new QuotationdetailSpecification(searchCriteria);
		// Where status != delete.
		Specification<Quotationdetail> notDeleteSpec = new QuotationdetailSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		quotationdetailSpecification = Specifications.where(quotationdetailSpecification).and(notDeleteSpec);
		// Execute.
		List<Quotationdetail> result = quotationdetailRepository.findAll(quotationdetailSpecification);
		return result;
	}

	public List<Quotationdetail> listWithCriteras(List<SearchCriteria> searchCriterias) {
		Specification<Quotationdetail> quotationdetailSpecification = quotationdetailSpecificationsBuilder
				.build(searchCriterias);
		// Where status != delete.
		Specification<Quotationdetail> notDeleteSpec = new QuotationdetailSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		quotationdetailSpecification = Specifications.where(quotationdetailSpecification).and(notDeleteSpec);
		// Execute.
		List<Quotationdetail> result = quotationdetailRepository.findAll(quotationdetailSpecification);
		return result;
	}

	public Page<Quotationdetail> listAllByPage(Pageable pageable) {
		return quotationdetailRepository.findAll(pageable);
	}

	public Page<Quotationdetail> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Quotationdetail> quotationdetailSpecification = new QuotationdetailSpecification(searchCriteria);
		// Where status != delete.
		Specification<Quotationdetail> notDeleteSpec = new QuotationdetailSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		quotationdetailSpecification = Specifications.where(quotationdetailSpecification).and(notDeleteSpec);
		// Execute.
		Page<Quotationdetail> result = quotationdetailRepository.findAll(quotationdetailSpecification, pageable);
		return result;
	}

	public Page<Quotationdetail> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Quotationdetail> quotationdetailSpecification = quotationdetailSpecificationsBuilder
				.build(searchCriterias);
		// Where status != delete.
		Specification<Quotationdetail> notDeleteSpec = new QuotationdetailSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		quotationdetailSpecification = Specifications.where(quotationdetailSpecification).and(notDeleteSpec);
		// Execute.
		Page<Quotationdetail> result = quotationdetailRepository.findAll(quotationdetailSpecification, pageable);
		return result;
	}

	public Page<Map<String, Object>> listWithCriteriasByIdquotationAndPage(Integer idquotation,
			List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Quotationdetail> quotationSpecification = quotationdetailSpecificationsBuilder
				.build(searchCriterias);
		// Where idquotation.
		Specification<Quotationdetail> idquotationSpec = new QuotationdetailSpecification(
				new SearchCriteria("idquotation", "=", idquotation));
		// Where status != delete.
		Specification<Quotationdetail> notDeleteSpec = new QuotationdetailSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		quotationSpecification = Specifications.where(quotationSpecification).and(idquotationSpec).and(notDeleteSpec);
		// Execute.
		Page<Quotationdetail> quotationdetails = quotationdetailRepository.findAll(quotationSpecification, pageable);
		Page<Map<String, Object>> result = quotationdetails.map(this::convertToMap);
		return result;
	}

	private Map<String, Object> convertToMap(final Quotationdetail quotationdetail) {
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", quotationdetail.getId());
		result.put("idquotation", quotationdetail.getIdquotation());
		result.put("idmaterial", quotationdetail.getIdmaterial());
		result.put("materialcode", quotationdetail.getMaterialcode());
		result.put("price", quotationdetail.getPrice());
		result.put("materialname", quotationdetail.getMaterial().getName());
		result.put("materialthumbnail", quotationdetail.getMaterial().getThumbnail());
		result.put("unitname", quotationdetail.getCatalog().getName());
		result.put("version", quotationdetail.getVersion());
		return result;
	}

	@Override
	public List<Map<String, Object>> getAllById(Integer id) {

		return quotationdetailRepository.getAllById(id);
	}

	@Override
	public Map<String, Object> getByIdForView(Integer id) {
		Quotationdetail quotationdetail = quotationdetailRepository.getByIdForView(id);
		Map<String, Object> result = convertToMapForView(quotationdetail);
		return result;
	}

	private Map<String, Object> convertToMapForView(Quotationdetail quotationdetail) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", quotationdetail.getId());
		result.put("materialname", quotationdetail.getMaterial().getName());
		result.put("price", quotationdetail.getPrice());
		result.put("materialcode", quotationdetail.getMaterial().getCode());
		result.put("unitname", quotationdetail.getCatalog().getName());
		// result.put("amount", quotationdetail.getAmount());
		result.put("note", quotationdetail.getNote());
		return result;
	}
	
	public void saveCsvFileByIdquotation(Integer idquotation, String filePath, String fileName) throws IOException{
		List<SearchCriteria> searchCriterias = new ArrayList<SearchCriteria>();
		// Where idquotation.
		searchCriterias.add(new SearchCriteria("idquotation", "=", idquotation, "and"));
		// Where status != delete.
		searchCriterias.add(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		// Build SearchCriterias.
		Specification<Quotationdetail> quotationdetailSpecification = quotationdetailSpecificationsBuilder.build(searchCriterias);
		// Execute.
		List<Quotationdetail> quotationdetails = quotationdetailRepository.findAll(quotationdetailSpecification);
		// Covert to map.
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		for (Quotationdetail quotationdetail : quotationdetails) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("materialcode", quotationdetail.getMaterial().getCode());
			map.put("materialname", quotationdetail.getMaterial().getName());
			map.put("price", quotationdetail.getPrice());
			map.put("unitname", quotationdetail.getCatalog().getName());
			
			maps.add(map);
		}
		
		File dir = new File(filePath);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(filePath + fileName);
        // Create a File and append if it already exists.
        Writer writer = new FileWriter(file, true);
        //Copy List of Map Object into CSV format at specified File location.
        CsvFileUtil.csvWriterList(maps, writer);
        //Read CSV format from specified File location and print on console.
        Reader reader = new FileReader(file);
        CsvFileUtil.csvReader(reader);
	}
	
	public void saveCsvFileAndSendMailByIdquotation(Integer idquotation, String filePath, String fileName) throws IOException, MessagingException{
		// Save csv file.
		saveCsvFileByIdquotation(idquotation, filePath, fileName);
		// Get email form Supplier.
		Quotation quotation = quotationRepository.findOne(idquotation);
		Supplier supplier = quotation.getSupplier();
		String toEmail = supplier.getEmail();
		// Send mail.
		List<String> attachments = new ArrayList<String>();
		attachments.add(filePath + fileName);
		mailHelper.sendWithAttachment(toEmail, "quotation", "Please input price for each material!", attachments);
	}

	@Override
	public void updatePrice(Integer id, Double price) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		quotationdetailRepository.updatePriceById(id, price,iduser);
	}

}
