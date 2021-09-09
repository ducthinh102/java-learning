

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
import com.redsun.server.wh.model.Purchase;
import com.redsun.server.wh.model.History;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.PurchaseRepository;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.specification.PurchaseSpecification;
import com.redsun.server.wh.repository.specification.PurchaseSpecificationsBuilder;
import com.redsun.server.wh.service.PurchaseService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("purchase")
@Transactional
public class PurchaseServiceImpl implements PurchaseService {
	
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private PurchaseRepository purchaseRepository;
	
	@Autowired
	private PurchaseSpecificationsBuilder purchaseSpecificationsBuilder;

	@Override
	public Purchase save(Purchase purchase) {
		return purchaseRepository.save(purchase);
	}

	@Override
	public Purchase create(Purchase purchase) throws JsonParseException, JsonMappingException, IOException {
		if (purchaseRepository.isExistCode(purchase.getId(), purchase.getCode())) {
			throw new ServerException(Constant.SERVERCODE_EXISTCODE);
		} else {
			// Get iduser.
			Integer iduser = SecurityUtil.getIdUser();
			// Current date;
			Date currentDate = new Date();
			purchase.setStatus(Constant.SERVERDB_STATUS_NEW);
			purchase.setTotalamount(0.0);
			purchase.setIdcreate(iduser);
			purchase.setCreatedate(currentDate);
			purchase.setIdowner(iduser);
			purchase.setIdupdate(iduser);
			purchase.setUpdatedate(currentDate);
			purchase.setVersion(1);
			return purchaseRepository.save(purchase);
		}
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = purchaseRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = purchaseRepository.updateUnlock(id);
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
		Purchase purchase = (Purchase) params.get("purchase");
		// Get from DB.
		Purchase purchaseDb = purchaseRepository.findOne(id);
		if(purchaseDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if(purchaseDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (purchaseDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else if (purchase!=null && purchaseRepository.isExistCode(id, purchase.getCode())) {
			throw new ServerException(Constant.SERVERCODE_EXISTCODE);
		}
		else {
			// Keep history data.
			String historyStr = purchaseDb.toString();
			// Increase version.
			purchaseDb.setVersion(purchaseDb.getVersion() + 1);
			// return.
			result.put("purchaseDb", purchaseDb);
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
		History history = new History(id, "purchase", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}
	
	@Override
	public Purchase update(Integer id, Purchase purchase) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", purchase.getVersion());
		params.put("purchase", purchase);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Purchase purchaseDb = (Purchase) resultPre.get("purchaseDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(purchase, purchaseDb, ignoreProperties);
		Date currentDate = new Date();
		if (purchaseDb.getStatus() == Constant.SERVERDB_STATUS_NEW) {
			purchaseDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		}
		purchaseDb.setIdupdate(iduser);
		purchaseDb.setUpdatedate(currentDate);
		purchaseDb.setIdowner(iduser);
		// Save.
		purchaseDb = purchaseRepository.save(purchaseDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return purchaseDb;
	}

	@Override
	public Purchase updateWithLock(Integer id, Purchase purchase) throws JsonParseException, JsonMappingException, IOException {
		Purchase result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, purchase);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public Purchase updateStatusAndOwnerWithLock(Integer id, Purchase purchase) throws JsonParseException, JsonMappingException, IOException {
		// Lock to update.
		updateLock(id);
		
		// Update.
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", purchase.getVersion());
		params.put("purchase", purchase);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Purchase purchaseDb = (Purchase) resultPre.get("purchaseDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(purchase, purchaseDb, ignoreProperties);
		Date currentDate = new Date();
		purchaseDb.setIdupdate(iduser);
		purchaseDb.setUpdatedate(currentDate);
		// Save.
		purchaseDb = purchaseRepository.save(purchaseDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		
		// Unlock of update.
		updateUnlock(id);
		return purchaseDb;
	}
	
	@Override
	public Purchase updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Purchase purchaseDb = (Purchase) resultPre.get("purchaseDb");
		Date currentDate = new Date();
		purchaseDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		purchaseDb.setIddelete(iduser);
		purchaseDb.setDeletedate(currentDate);
		// Save.
		purchaseDb = purchaseRepository.save(purchaseDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return purchaseDb;
	}

	@Override
	public Purchase updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		Integer iduser = SecurityUtil.getIdUser();
		Purchase result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		purchaseRepository.updateForDeleteByIdPurchase(iduser, id);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}


	@Override
	public void delete(Purchase purchase) {
		purchaseRepository.delete(purchase);
	}

	@Override
	public void deleteById(Integer id) {
		purchaseRepository.delete(id);
	}

	@Override
	public Purchase getById(Integer id) {
		return purchaseRepository.findOne(id);
	}

	@Override
	public List<Purchase> listAll() {
		return purchaseRepository.findAll();
	}

	@Override
	public long countAll() {
		return purchaseRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return purchaseRepository.exists(id);
	}
	
	public List<Purchase> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Purchase> purchaseSpecification = new PurchaseSpecification(searchCriteria);
		// Where status != delete.
		Specification<Purchase> notDeleteSpec = new PurchaseSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		purchaseSpecification = Specifications.where(purchaseSpecification).and(notDeleteSpec);
		// Execute.
        List<Purchase> result = purchaseRepository.findAll(purchaseSpecification);
        return result;
	}
	
	public List<Purchase> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Purchase> purchaseSpecification = purchaseSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Purchase> notDeleteSpec = new PurchaseSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		purchaseSpecification = Specifications.where(purchaseSpecification).and(notDeleteSpec);
		// Execute.
        List<Purchase> result = purchaseRepository.findAll(purchaseSpecification);
        return result;
	}
	
	public Page<Purchase> listAllByPage(Pageable pageable) {
		return purchaseRepository.findAll(pageable);
	}
	
	public Page<Purchase> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Purchase> purchaseSpecification = new PurchaseSpecification(searchCriteria);
		// Where status != delete.
		Specification<Purchase> notDeleteSpec = new PurchaseSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		purchaseSpecification = Specifications.where(purchaseSpecification).and(notDeleteSpec);
		// Execute.
		Page<Purchase> result = purchaseRepository.findAll(purchaseSpecification, pageable);
        return result;
	}
	
	public Page<Map<String, Object>> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Purchase> purchaseSpecification = purchaseSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		//Specification<Purchase> notDeleteSpec = new PurchaseSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		//purchaseSpecification = Specifications.where(purchaseSpecification).and(notDeleteSpec);
		// Execute.
		Page<Purchase> purchase = purchaseRepository.findAll(purchaseSpecification, pageable);
		Page<Map<String, Object>> result = purchase.map(this::convertToMap);
		
        return result;
	}
	

	@Override
	public List<Purchase> findByCode(String code) {
		return purchaseRepository.findByCode(code);
	}
	
	private Map<String, Object> convertToMap(final Purchase purchase) {
	    final Map<String, Object> result = new HashMap<String, Object>();
	    result.put("id", purchase.getId());
	    result.put("storename", purchase.getStore().getName());
	    result.put("contactname", purchase.getContact().getFirstname());
	    result.put("receivername", purchase.getReceiver().getFirstname());
	    result.put("suppliername", purchase.getSupplier().getName());
	    result.put("code", purchase.getCode());
	    result.put("name", purchase.getName());
	    result.put("contactphonenumber", purchase.getContactphonenumber());
	    result.put("contactfaxnumber", purchase.getContactfaxnumber());
	    result.put("deliveryaddress", purchase.getDeliveryaddress());
	    result.put("deliverydate", purchase.getDeliverydate());
	    result.put("vat", purchase.getVat());
	    result.put("note", purchase.getNote());
	    result.put("status", purchase.getStatus());
	    result.put("totalamount", purchase.getTotalamount());
	    result.put("version", purchase.getVersion());
	    return result;
	}

	@Override
	public List<Map<String, Object>> listAllForSelect() {
		// TODO Auto-generated method stub
		return purchaseRepository.listForSelect();
	}

}
