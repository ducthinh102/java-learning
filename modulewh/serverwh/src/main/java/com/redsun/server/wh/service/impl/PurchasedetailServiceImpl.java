
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
import com.redsun.server.wh.model.History;
import com.redsun.server.wh.model.Purchasedetail;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.PurchaseRepository;
import com.redsun.server.wh.repository.PurchasedetailRepository;
import com.redsun.server.wh.repository.specification.PurchasedetailSpecification;
import com.redsun.server.wh.repository.specification.PurchasedetailSpecificationsBuilder;
import com.redsun.server.wh.service.PurchasedetailService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("purchasedetail")
@Transactional
public class PurchasedetailServiceImpl implements PurchasedetailService {
	
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private PurchasedetailRepository purchasedetailRepository;
	
	@Autowired
	private PurchaseRepository purchaseRepository;
	
	@Autowired
	private PurchasedetailSpecificationsBuilder purchasedetailSpecificationsBuilder;

	@Override
	public Purchasedetail save(Purchasedetail purchasedetail) {
		return purchasedetailRepository.save(purchasedetail);
	}

	@Override
	public Purchasedetail create(Purchasedetail purchasedetail) throws JsonParseException, JsonMappingException, IOException {
		if (purchasedetailRepository.checkExitsMaterialInPurchaseForCreate(purchasedetail.getIdpurchase(), purchasedetail.getIdmaterial())) {
			throw new ServerException(Constant.SERVERCODE_EXITMATERIAL);
		}
		else {
			// Get iduser.
			Integer iduser = SecurityUtil.getIdUser();
			// Current date;
			Date currentDate = new Date();
			purchasedetail.setStatus(Constant.SERVERDB_STATUS_NEW);
			purchasedetail.setIdcreate(iduser);
			purchasedetail.setCreatedate(currentDate);
			purchasedetail.setIdowner(iduser);
			purchasedetail.setIdupdate(iduser);
			purchasedetail.setUpdatedate(currentDate);
			purchasedetail.setVersion(1);
			Purchasedetail result = purchasedetailRepository.save(purchasedetail);
			// Update totalAmount.
			Integer idpurchase = purchasedetail.getIdpurchase();
			double totalAmount = purchasedetailRepository.sumAmount(idpurchase);
			purchaseRepository.updateTotalAmount(idpurchase, totalAmount);
			return result;
		}
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = purchasedetailRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = purchasedetailRepository.updateUnlock(id);
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
		Purchasedetail purchasedetail = (Purchasedetail) params.get("purchasedetail");
		// Get from DB.
		Purchasedetail purchasedetailDb = purchasedetailRepository.findOne(id);
		if(purchasedetailDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if (purchasedetail != null && purchasedetailRepository.checkExitsMaterialInPurchase(id, purchasedetail.getIdpurchase(), purchasedetail.getIdmaterial())) { // check exist material name.
			throw new ServerException(Constant.SERVERCODE_EXITMATERIAL);
		} else if(purchasedetailDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (purchasedetailDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		/*} else if (purchasedetail != null && purchasedetailRepository.checkPriceDiffCreate(purchasedetail.getIdmaterial(), purchasedetail.getPrice())) { // price difference.
			throw new ServerException(Constant.SERVERCODE_DIFFPRICE);*/
		/*} else if (purchasedetail != null && purchasedetailRepository.checkExitsMaterial(id, purchasedetail.getIdmaterial())) { // check material exit																// difference.
			throw new ServerException(Constant.SERVERCODE_EXITMATERIAL);*/
		} else {
			// Keep history data.
			String historyStr = purchasedetailDb.toString();
			// Increase version.
			purchasedetailDb.setVersion(purchasedetailDb.getVersion() + 1);
			// return.
			result.put("purchasedetailDb", purchasedetailDb);
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
		History history = new History(id, "purchasedetail", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}
	
	@Override
	public Purchasedetail update(Integer id, Purchasedetail purchasedetail) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", purchasedetail.getVersion());
		params.put("purchasedetail", purchasedetail);
		Map<String, Object> resultPre = updatePre(params);
		
		// Update data.
		Purchasedetail purchasedetailDb = (Purchasedetail) resultPre.get("purchasedetailDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(purchasedetail, purchasedetailDb, ignoreProperties);
		Date currentDate = new Date();
		if (purchasedetailDb.getStatus() == Constant.SERVERDB_STATUS_NEW) {
			purchasedetailDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		}
		purchasedetailDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		purchasedetailDb.setIdupdate(iduser);
		purchasedetailDb.setUpdatedate(currentDate);
		purchasedetailDb.setIdowner(iduser);
		// Save.
		purchasedetailDb = purchasedetailRepository.save(purchasedetailDb);
		// Update totalAmount.
		Integer idpurchase = purchasedetail.getIdpurchase();
		double totalAmount = purchasedetailRepository.sumAmount(idpurchase);
		purchaseRepository.updateTotalAmount(idpurchase, totalAmount);
				
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return purchasedetailDb;
	}

	@Override
	public Purchasedetail updateWithLock(Integer id, Purchasedetail purchasedetail) throws JsonParseException, JsonMappingException, IOException {
		Purchasedetail result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		/*if (purchasedetailRepository.checkPriceDiffCreate(purchasedetail.getIdmaterial(), purchasedetail.getPrice())) {
			throw new ServerException(Constant.SERVERCODE_DIFFPRICE);
		}
		Purchasedetail purchasedetaillDb = purchasedetailRepository.findOne(id);
		purchasedetail.setQuantity(purchasedetaillDb.getQuantity() + purchasedetail.getQuantity());
		purchasedetail.setAmount(purchasedetail.getQuantity() * purchasedetail.getPrice());*/
		result = update(id, purchasedetail);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}
	
	@Override
	public Purchasedetail updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Purchasedetail purchasedetailDb = (Purchasedetail) resultPre.get("purchasedetailDb");
		Date currentDate = new Date();
		purchasedetailDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		purchasedetailDb.setIddelete(iduser);
		purchasedetailDb.setDeletedate(currentDate);
		// Save.
		purchasedetailDb = purchasedetailRepository.save(purchasedetailDb);
		// Update totalAmount.
		Integer idpurchase = purchasedetailDb.getIdpurchase();
		double totalAmount = purchasedetailRepository.sumAmount(idpurchase);
		purchaseRepository.updateTotalAmount(idpurchase, totalAmount);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return purchasedetailDb;
	}

	@Override
	public Purchasedetail updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		Purchasedetail result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public void delete(Purchasedetail purchasedetail) {
		purchasedetailRepository.delete(purchasedetail);
	}

	@Override
	public void deleteById(Integer id) {
		purchasedetailRepository.delete(id);
	}

	@Override
	public Purchasedetail getById(Integer id) {
		return purchasedetailRepository.findOne(id);
	}

	@Override
	public List<Purchasedetail> listAll() {
		return purchasedetailRepository.findAll();
	}

	@Override
	public long countAll() {
		return purchasedetailRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return purchasedetailRepository.exists(id);
	}
	
	public List<Purchasedetail> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Purchasedetail> purchasedetailSpecification = new PurchasedetailSpecification(searchCriteria);
		// Where status != delete.
		Specification<Purchasedetail> notDeleteSpec = new PurchasedetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		purchasedetailSpecification = Specifications.where(purchasedetailSpecification).and(notDeleteSpec);
		// Execute.
        List<Purchasedetail> result = purchasedetailRepository.findAll(purchasedetailSpecification);
        return result;
	}
	
	public List<Purchasedetail> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Purchasedetail> purchasedetailSpecification = purchasedetailSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Purchasedetail> notDeleteSpec = new PurchasedetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		purchasedetailSpecification = Specifications.where(purchasedetailSpecification).and(notDeleteSpec);
		// Execute.
        List<Purchasedetail> result = purchasedetailRepository.findAll(purchasedetailSpecification);
        return result;
	}
	
	public Page<Purchasedetail> listAllByPage(Pageable pageable) {
		return purchasedetailRepository.findAll(pageable);
	}
	
	public Page<Purchasedetail> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Purchasedetail> purchasedetailSpecification = new PurchasedetailSpecification(searchCriteria);
		// Where status != delete.
		Specification<Purchasedetail> notDeleteSpec = new PurchasedetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		purchasedetailSpecification = Specifications.where(purchasedetailSpecification).and(notDeleteSpec);
		// Execute.
		Page<Purchasedetail> result = purchasedetailRepository.findAll(purchasedetailSpecification, pageable);
        return result;
	}
	
	public Page<Purchasedetail> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Purchasedetail> purchasedetailSpecification = purchasedetailSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Purchasedetail> notDeleteSpec = new PurchasedetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		purchasedetailSpecification = Specifications.where(purchasedetailSpecification).and(notDeleteSpec);
		// Execute.
		Page<Purchasedetail> result = purchasedetailRepository.findAll(purchasedetailSpecification, pageable);
        return result;
	}
	
	public Page<Map<String, Object>> listWithCriteriasByIdpurchaseAndPage(Integer idpurchase, List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Purchasedetail> purchaseSpecification = purchasedetailSpecificationsBuilder.build(searchCriterias);
		// Where idpurchase.
		Specification<Purchasedetail> idpurchaseSpec = new PurchasedetailSpecification(new SearchCriteria("idpurchase", "=", idpurchase));
		// Where status != delete.
		Specification<Purchasedetail> notDeleteSpec = new PurchasedetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		purchaseSpecification = Specifications.where(purchaseSpecification).and(idpurchaseSpec).and(notDeleteSpec);
		// Execute.
		Page<Purchasedetail> purchasedetails = purchasedetailRepository.findAll(purchaseSpecification, pageable);
		Page<Map<String, Object>> result = purchasedetails.map(this::convertToMap);
        return result;
	}

	private Map<String, Object> convertToMap(final Purchasedetail purchasedetail) {
	    final Map<String, Object> result = new HashMap<String, Object>();
	    result.put("id", purchasedetail.getId());
	    result.put("quantity", purchasedetail.getQuantity());
	    result.put("amount", purchasedetail.getAmount());
	    result.put("price", purchasedetail.getPrice());
	    result.put("unitname", purchasedetail.getMaterial().getUnit().getName());
	    result.put("code", purchasedetail.getMaterial().getCode());
	    result.put("materialcode", purchasedetail.getMaterialcode());
	    result.put("idpurchase", purchasedetail.getIdpurchase());
	    result.put("idmaterial", purchasedetail.getIdmaterial());
	    result.put("materialname", purchasedetail.getMaterial().getName());
	    result.put("materialthumbnail", purchasedetail.getMaterial().getThumbnail());
	    result.put("version", purchasedetail.getVersion());
	    return result;
	}
	
	
	@Override
	public Double sumAmount(Integer idpurchase) {
		Double result = purchasedetailRepository.sumAmount(idpurchase);
		return result;
	}

	@Override
	public List<Map<String, Object>> getAllById(Integer id) {
		return purchasedetailRepository.getAllById(id);
	}

	@Override
	public Boolean isExistMaterial(Integer id, Integer idmaterial) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePrice(Integer id, Double price) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		purchasedetailRepository.updatePriceById(id, price,iduser);
		// Update totalAmount.
		Purchasedetail purchasedetail = purchasedetailRepository.getById(id);
		Integer idpurchase = purchasedetail.getIdpurchase();
		double totalAmount = purchasedetailRepository.sumAmount(idpurchase);
		purchaseRepository.updateTotalAmount(idpurchase, totalAmount);
	}

	@Override
	public void updateQuantity(Integer id, Integer quantity) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		purchasedetailRepository.updateQuantityById(id, quantity,iduser);
		// Update totalAmount.
		Purchasedetail purchasedetail = purchasedetailRepository.getById(id);
		Integer idpurchase = purchasedetail.getIdpurchase();
		double totalAmount = purchasedetailRepository.sumAmount(idpurchase);
		purchaseRepository.updateTotalAmount(idpurchase, totalAmount);
	}
	
	/*@Override
	public Map<String, Object> quantityMaterial(Integer idmaterial) {
		return purchasedetailRepository.quantityMaterial(idmaterial);
	}*/
	
}


