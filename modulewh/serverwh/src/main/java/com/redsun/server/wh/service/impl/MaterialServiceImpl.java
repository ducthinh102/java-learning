

package com.redsun.server.wh.service.impl;

import java.io.IOException;
import java.util.ArrayList;
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
import com.redsun.server.wh.common.Role;
import com.redsun.server.wh.controller.common.ServerException;
import com.redsun.server.wh.model.History;
import com.redsun.server.wh.model.Material;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.MaterialRepository;
import com.redsun.server.wh.repository.specification.MaterialSpecification;
import com.redsun.server.wh.repository.specification.MaterialSpecificationsBuilder;
import com.redsun.server.wh.service.MaterialService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("material")
@Transactional(rollbackFor = Exception.class)
public class MaterialServiceImpl implements MaterialService {
	
	public static Object syncDuplicate = new Object();
	
	@Autowired
	private HistoryRepository historyRepository;
	
	@Autowired
	private MaterialRepository materialRepository;
	
	@Autowired
	private MaterialSpecificationsBuilder materialSpecificationsBuilder;

	@Override
	public Material save(Material material) {
		return materialRepository.save(material);
	}

	@Override
	public Material create(Material material)  throws JsonParseException, JsonMappingException, IOException {
			if (materialRepository.isExistCode(material.getId(), material.getCode()) &&  
				materialRepository.isExistName(material.getId(), material.getName())) {
				throw new ServerException(Constant.SERVERCODE_EXISTALL);
			}
			else {
				if (materialRepository.isExistName(material.getId(), material.getName())) {
					throw new ServerException(Constant.SERVERCODE_EXISTNAME);
				}
				else if (materialRepository.isExistCode(material.getId(), material.getCode())) {
					throw new ServerException(Constant.SERVERCODE_EXISTCODE);
				}
				else {
					// Get iduser.
					Integer iduser = SecurityUtil.getIdUser();
					// Current date;
					Date currentDate = new Date();
					material.setIdcreate(iduser);
					material.setQuantity(0);
					material.setCreatedate(currentDate);
					material.setIdowner(iduser);
					if(material.getIdref() == null) {
						// Check role.
						List<Integer> idroles = SecurityUtil.listIdRoles();
						if(idroles.contains(Role.MATERIAL_CODE.getId())){
							material.setIdref(0);
						} else {
							material.setIdref(-1);
						}
						material.setReftype("materialcode");
					}
					material.setVersion(1);
					material.setStatus(1);
					return materialRepository.save(material);
				}
		}
	}
	
	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = materialRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = materialRepository.updateUnlock(id);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;
	}
	
	@Override
	public Material updateWithLock(Integer id, Material material) throws JsonParseException, JsonMappingException, IOException  {
		Material result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, material);
		// Unlock of update.
		updateUnlock(id);
		return result;		
	}
	
	private Map<String, Object> updatePre(Map<String, Object> params) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		// Get value from params.
		Integer iduser = (Integer) params.get("iduser");
		Integer id = (Integer) params.get("id");
		Material material = (Material) params.get("material");
		// Get from DB.
		Material materialDb = materialRepository.findOne(id);
		if(materialDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if(materialDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (materialDb.getVersion() != material.getVersion()) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else if (materialRepository.isExistName(id, material.getName())) { // exist name.
			throw new ServerException(Constant.SERVERCODE_EXISTNAME);
		} else if (materialRepository.isExistCode(id, material.getCode())) { // exist code.
			throw new ServerException(Constant.SERVERCODE_EXISTCODE);
		}
		else {
			// Ignore properties.
			String[] ignoreProperties = new String[] { "idref", "reftype", "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate" };
			// Copy data.
			BeanUtils.copyProperties(material, materialDb, ignoreProperties);
			// Keep history data.
			String historyStr = materialDb.toString();
			// Increase version.
			materialDb.setVersion(materialDb.getVersion() + 1);
			// return.
			result.put("materialDb", materialDb);
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
		History history = new History(id, "material", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}

	@Override
	public Material update(Integer id, Material material) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("material", material);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Material materialDb = (Material) resultPre.get("materialDb");
		Date currentDate = new Date();
		materialDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		materialDb.setIdupdate(iduser);
		materialDb.setUpdatedate(currentDate);
		materialDb.setIdowner(iduser);
		// Save.
		materialDb = materialRepository.save(materialDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialDb;
	}
	
	@Override
	public void delete(Material material) {
		materialRepository.delete(material);
	}

	@Override
	public void deleteById(Integer id) {
		Material material = getById(id);
		material.setStatus(3);
		material.setDeletedate(new Date());
		materialRepository.save(material);		
	}

	@Override
	public Material getById(Integer id) {
		return materialRepository.findOne(id);
	}
	
	@Override
	public Material getAndLockById(Integer id, Integer userid) {
//		Material material = materialRepository.findOne(id);
//		material.setIdlock(userid);
//		material.setLockdate(new Date());
//		materialRepository.save(material);
//		materialRepository.updateLock(userid, id, 5);
//		return materialRepository.findOne(id);
		
		return materialRepository.findOne(id);
	}

	@Override
	public List<Material> listAll() {
		return materialRepository.findAll();
	}

	@Override
	public long countAll() {
		return materialRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return materialRepository.exists(id);
	}
	
	public List<Material> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Material> materialSpecification = new MaterialSpecification(searchCriteria);
        List<Material> result = materialRepository.findAll(materialSpecification);
        return result;
	}
	
	public List<Material> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Material> materialSpecification = materialSpecificationsBuilder.build(searchCriterias);
        List<Material> result = materialRepository.findAll(materialSpecification);
        return result;
	}
	
	public Page<Material> listAllByPage(Pageable pageable) {
		return materialRepository.findAll(pageable);
	}
	
	public Page<Material> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Material> materialSpecification = new MaterialSpecification(searchCriteria);
		// Where status != delete.
		Specification<Material> notDeleteSpec = new MaterialSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		// idref = 0
		Specification<Material> idrefSpec = new MaterialSpecification(new SearchCriteria("idref", "=", 0));
		// materialSpecification and notDeleteSpec and idrefSpec.
		materialSpecification = Specifications.where(materialSpecification).and(notDeleteSpec).and(idrefSpec);
		// Execute.
		Page<Material> result = materialRepository.findAll(materialSpecification, pageable);
        return result;
	}
	
	public Page<Material> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Material> materialSpecification = materialSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Material> notDeleteSpec = new MaterialSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		// materialSpecification and notDeleteSpec and idrefSpec.
		materialSpecification = Specifications.where(materialSpecification).and(notDeleteSpec);
		// Execute.
		Page<Material> result = materialRepository.findAll(materialSpecification, pageable);
        return result;
	}
	
	public Page<Map<String, Object>> listWithCriteriasByIdrefAndReftypeAndPage(Integer idref, String reftype, List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Material> materialSpecification = materialSpecificationsBuilder.build(searchCriterias);
		// Where idref.
		Specification<Material> idrefSpec = new MaterialSpecification(new SearchCriteria("idref", "=", idref));
		// Where reftype.
		Specification<Material> reftypeSpec = new MaterialSpecification(new SearchCriteria("reftype", "=", reftype));
		// Where status != delete.
		Specification<Material> notDeleteSpec = new MaterialSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialSpecification = Specifications.where(materialSpecification).and(idrefSpec).and(reftypeSpec).and(notDeleteSpec);
		// Execute.
		Page<Material> materials = materialRepository.findAll(materialSpecification, pageable);
		Page<Map<String, Object>> result = materials.map(this::convertToMap);
        return result;
	}

	private Map<String, Object> convertToMap(final Material material) {
	    final Map<String, Object> result = new HashMap<String, Object>();
	    result.put("id", material.getId());
	    result.put("idref", material.getIdref());
	    result.put("reftype", material.getReftype());
	    result.put("code", material.getCode());
	    result.put("name", material.getName());
	    result.put("materialcode", material.getMaterialcode());
	    result.put("quantity", material.getQuantity());
	    result.put("thumbnail", material.getThumbnail());
	    result.put("description", material.getDescription());
	    result.put("parentcode", material.getParent().getCode());
	    result.put("parentname", material.getParent().getName());
	    result.put("version", material.getVersion());
	    return result;
	}	
	
	@Override
	public Page<Material> listUnconfirmedWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		
		// Search unconfirmed item specification
		SearchCriteria searchUnconfirmedMaterialCriteria = new SearchCriteria("idref", "=", -1);
		List<SearchCriteria> searchUnconfirmedMaterialCriterias = new ArrayList<SearchCriteria>();
		searchUnconfirmedMaterialCriterias.add(searchUnconfirmedMaterialCriteria);
		Specification<Material> unconfirmedMaterialSpecification = materialSpecificationsBuilder.build(searchUnconfirmedMaterialCriterias);
		
		// Other search specification
		Specification<Material> materialSpecification = materialSpecificationsBuilder.build(searchCriterias);
		
		// And 2 specifications
		materialSpecification = Specifications.where(materialSpecification).and(unconfirmedMaterialSpecification);
		Page<Material> result = materialRepository.findAll(materialSpecification, pageable);
        return result;
	}

	@Override
	public List<Material> updateUnconfirmedItems(List<Integer> ids) throws JsonParseException, JsonMappingException, IOException {
		List<Material> updatedItems = new ArrayList<Material>();
		for (Integer id : ids) {
			Material newMaterialDB = materialRepository.findOne(id);
			
			// New material history
			History history = new History();
			history.setIdcreate(SecurityUtil.getIdUser());
			history.setIdupdate(SecurityUtil.getIdUser());
			history.setCreatedate(new Date());
			history.setUpdatedate(new Date());
			history.setVersion(1);
			history.setIdref(id);
			history.setReftype("material");
			history.setContent(newMaterialDB.toString());
			
			if(newMaterialDB != null && !newMaterialDB.getName().isEmpty() && !newMaterialDB.getCode().isEmpty()) {
				newMaterialDB.setIdref(0);
				if( !(newMaterialDB.getQuantity()>-1) )
					newMaterialDB.setQuantity(0);
				this.updateWithLock(newMaterialDB.getId(), newMaterialDB);
				
				// Save history
				historyRepository.save(history);
				
				updatedItems.add(newMaterialDB);
			}
			else
				return null;
		}
		return updatedItems;
	}
	
	@Override
	public Material getConfirmedItemByCode(String code) {
        List<Material> materials = materialRepository.findConfirmedItemsByCode(code);
        if(materials.size()>0)
        		return materials.get(0);
        return null;
	}

	@Override
	public List<Material> findConfirmedItemsByName(String name) {
		List<Material> materials = materialRepository.findConfirmedItemsByName(name);
		return materials;
	}
	
	@Override
	public List<Map<String,Object>> loadAllMaterialConfirmed() {
		List<Map<String,Object>> materials = materialRepository.loadAllMaterialConfirmed();
		return materials;
	}
	
	@Override
	public List<Map<String,Object>> loadAllFromBaselineByIdstore(Integer idstore) {
		List<Map<String,Object>> materials = materialRepository.loadAllFromBaselineByIdstore(idstore);
		return materials;
	}

	@Override
	public Material updateUnconfirmedItem(Material newMaterial) throws JsonParseException, JsonMappingException, IOException {
		
		// Check new material properties
		if( !(newMaterial.getId()>-1) || newMaterial.getName().isEmpty() || newMaterial.getCode().isEmpty())
			return null;
		
		// Check new material existed in DB
		Material newMaterialDB = materialRepository.findOne(newMaterial.getId());
		if(newMaterialDB == null)
			return null;
		
		// New material history
		History historyNewMaterialDB = new History();
		historyNewMaterialDB.setIdcreate(SecurityUtil.getIdUser());
		historyNewMaterialDB.setIdupdate(SecurityUtil.getIdUser());
		historyNewMaterialDB.setCreatedate(new Date());
		historyNewMaterialDB.setUpdatedate(new Date());
		historyNewMaterialDB.setVersion(1);
		historyNewMaterialDB.setIdref(newMaterial.getId());
		historyNewMaterialDB.setReftype("material");
		historyNewMaterialDB.setContent(newMaterialDB.toString());

		// Set new material quantity to 0 if not set before
		if( !(newMaterial.getQuantity()>-1))
			newMaterial.setQuantity(0);
		
		// Find old confirmed materials by code
        List<Material> oldMaterials = materialRepository.findConfirmedItemsByCode(newMaterial.getCode());
        
        // Exist 1 old confirmed material with this code
        if(oldMaterials.size()==1) {
        	
        		Material oldMaterial = oldMaterials.get(0);
        		
        		// Old material history
    			History historyOldMaterial = new History();
    			historyOldMaterial.setIdcreate(SecurityUtil.getIdUser());
    			historyOldMaterial.setIdupdate(SecurityUtil.getIdUser());
    			historyOldMaterial.setCreatedate(new Date());
    			historyOldMaterial.setUpdatedate(new Date());
    			historyOldMaterial.setVersion(1);
    			historyOldMaterial.setIdref(oldMaterial.getId());
    			historyOldMaterial.setReftype("materialcode");
    			historyOldMaterial.setContent(oldMaterial.toString());
        			
			// Update new material reference, code, category
        		newMaterial.setIdref(oldMaterial.getId());
        		newMaterial.setCode(oldMaterial.getCode());
        		newMaterial.setIdcategory(oldMaterial.getIdcategory());
        		newMaterial.setIdsystem(oldMaterial.getIdsystem());
        		newMaterial.setIdtype(oldMaterial.getIdtype());
        		newMaterial.setIdbrand(oldMaterial.getIdbrand());
        		newMaterial.setIdspec(oldMaterial.getIdspec());
        		newMaterial.setIdorigin(oldMaterial.getIdorigin());
        		newMaterial.setIdunit(oldMaterial.getIdunit());
        		
        		
        		String newMaterialThumbnail = newMaterial.getThumbnail();
        		String oldMaterialThumbnail = oldMaterial.getThumbnail();
        		if(oldMaterialThumbnail!=null && !oldMaterialThumbnail.isEmpty()) {
        			// Update new material thumbnail
        			newMaterial.setThumbnail(oldMaterialThumbnail);
        		}
        		else if(newMaterialThumbnail!=null && !newMaterialThumbnail.isEmpty()) {
        			// Update old material thumbnail
        			oldMaterial.setThumbnail(newMaterialThumbnail);
        		}
        		
        		// Update old material quantity
        		oldMaterial.setQuantity(oldMaterial.getQuantity() + newMaterial.getQuantity());
        		
    			// Update DB
        		newMaterial.setIdupdate(SecurityUtil.getIdUser());
        		newMaterial.setUpdatedate(new Date());
        		newMaterial.setReftype("materialcode");
        		if(newMaterial.getVersion() != null)
        			newMaterial.setVersion(newMaterial.getVersion() + 1);
        		materialRepository.save(newMaterial);
    			
        		this.updateWithLock(oldMaterial.getId(), oldMaterial);
        		
        		// Save history
			historyRepository.save(historyNewMaterialDB);
			historyRepository.save(historyOldMaterial);
        		
        		return newMaterial;
        }
        
        // Does not exist confirmed material with this code
        else if(oldMaterials.size()==0) {
        	
        		// Update new material
        		newMaterial.setIdref(0);
        		newMaterial.setIdupdate(SecurityUtil.getIdUser());
        		newMaterial.setUpdatedate(new Date());
        		newMaterial.setReftype(null);
        		newMaterial.setVersion(1);
        		
        		// Update DB
        		materialRepository.save(newMaterial);
        		
        		// Save history for new material
    			historyRepository.save(historyNewMaterialDB);
        		
        		return newMaterial;
        }
        
        return null;
	}

	@Override
	public List<Map<String, Object>> listAllForSelect() {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Material> materials = materialRepository.listAllForSelect();
		for (Material material : materials) {
			Map<String, Object> item = convertToMapGetAll(material);
			result.add(item);
		}
		return result;
	}
	
	private Map<String, Object> convertToMapGetAll(Material material) {
	    Map<String, Object> result = new HashMap<String, Object>();
	    result.put("value", material.getId());
	    result.put("display", material.getName());
	    result.put("code", material.getCode());
	    result.put("thumbnail", material.getThumbnail());
	    if (material.getIdunit()!=null) {
	    	 result.put("idunit", material.getIdunit());
	    	 result.put("unitName", material.getUnit().getName());
		}
	    else {
	    	result.put("idunit", material.getIdunit());
	    	result.put("unitName", "");
	    }
	   
	    return result;
	}
	
	@Override
	public Material updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Material material = materialRepository.findOne(id);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		params.put("material", material);
		
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Material materialDb = (Material) resultPre.get("materialDb");
		Date currentDate = new Date();
		materialDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		materialDb.setIddelete(iduser);
		materialDb.setDeletedate(currentDate);
		// Save.
		materialDb = materialRepository.save(materialDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialDb;
	}

	@Override
	public Material updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		Material result = null;
		// Lock to update.
		updateLock(id);
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}
	
	@Override
	public Map<String, Object> getByIdForView(Integer id) {
		Material material = materialRepository.getByIdForView(id);
		Map<String, Object> result = convertToMapForView(material);
		return result;
	}
	
	@Override
	public Map<String, Object> getByIdForViewSub(Integer id) {
		Material material = materialRepository.getByIdForView(id);
		Map<String, Object> result = convertToMapForViewSub(material);
		return result;
	}
	
	private Map<String, Object> convertToMapForViewSub(Material material) {
	    Map<String, Object> result = new HashMap<String, Object>();
	    result.put("id", material.getId());	    
	    result.put("code", material.getCode());
	    result.put("name", material.getName());
	    result.put("note", material.getNote());
	    result.put("description", material.getDescription());
	    result.put("typename", material.getType().getName());
	    result.put("specname", material.getSpec().getName());
	    result.put("brandname", material.getBrand().getName());
	    result.put("unitname", material.getUnit().getName());
	    return result;
	}

	private Map<String, Object> convertToMapForView(Material material) {
	    Map<String, Object> result = new HashMap<String, Object>();
	    result.put("id", material.getId());	    
	    result.put("code", material.getCode());
	    result.put("name", material.getName());
	    result.put("note", material.getNote());
	    result.put("description", material.getDescription());
	    result.put("typename", material.getType().getName());
	    result.put("specname", material.getSpec().getName());
	    result.put("brandname", material.getBrand().getName());
	    result.put("systemname", material.getSystem().getName());
	    result.put("unitname", material.getUnit().getName());
	    result.put("categoryname", material.getCategory().getName());
	    result.put("originname", material.getOrigin().getName());
	    return result;
	}
	
}
