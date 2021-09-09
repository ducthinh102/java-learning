
package com.redsun.server.wh.authorize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redsun.server.wh.common.Role;
import com.redsun.server.wh.controller.PermissionRestController;
import com.redsun.server.wh.model.Permission;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.PermissionService;
import com.redsun.server.wh.util.SecurityUtil;

@Component("permissionAuthorize")
public class PermissionAuthorize {

	public static final Logger logger = LoggerFactory.getLogger(PermissionRestController.class);

	private Map<String, Object> permissionRepo;
	public void reloadPermissionRepo() throws JsonParseException, JsonMappingException, IOException {
		permissionRepo = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		List<Permission> permissions = permissionService.listAll();
		for (Permission permission : permissions) {
			Map<String, Object> rules = objectMapper.readValue(permission.getRules(), new TypeReference<Map<String, Object>>(){});
			permissionRepo.put(permission.getTarget(), rules);
		}
	}
	public Map<String, Object> instance() throws JsonParseException, JsonMappingException, IOException {
		//TODO
		reloadPermissionRepo();
		
		if(null == permissionRepo) {
			reloadPermissionRepo();
		}
		// return.
		return permissionRepo;
	}
	
	private void AddRulesIntoPermission(Map<String, Object> permission, Map<String, Object> rules, Integer userId, boolean isAllow) {
		// Declare variables
		List<Integer> ids;
		Object rule;
		// admin.
		rule = rules.get("admins");
		ids = (ArrayList<Integer>) rule;
		if(ids.size() == 0) {
			permission.put("admin", true && isAllow);
		} else if(!ids.contains(0) && ids.contains(userId)){
			permission.put("admin", isAllow);
		}
		// create.
		rule = rules.get("creates");
		if(rule != null){
			ids = (ArrayList<Integer>) rule;
			if(ids.size() == 0) {
				permission.put("create", true && isAllow);
			} else if(!ids.contains(0) && ids.contains(userId)){
				permission.put("create", isAllow);
			}
		}
		// read.
		rule = rules.get("reads");
		ids = (ArrayList<Integer>) rule;
		if(ids.size() == 0) {
			permission.put("read", true && isAllow);
		} else if(!ids.contains(0) && ids.contains(userId)){
			permission.put("read", isAllow);
		}
		// update.
		rule = rules.get("updates");
		ids = (ArrayList<Integer>) rule;
		if(ids.size() == 0) {
			permission.put("update", true && isAllow);
		} else if(!ids.contains(0) && ids.contains(userId)){
			permission.put("update", isAllow);
		}
		// delete.
		rule = rules.get("deletes");
		ids = (ArrayList<Integer>) rule;
		if(ids.size() == 0) {
			permission.put("delete", true && isAllow);
		} else if(!ids.contains(0) && ids.contains(userId)){
			permission.put("delete", isAllow);
		}
	}
	
	public Map<String, Object> mergePermission(Map<String, Object> denyPermission, Map<String, Object> allowPermission){
		Map<String, Object> permission = new HashMap<String, Object>();
		permission.putAll(denyPermission); // clone denyPermission.
		Object rule;
		// admin
		rule = denyPermission.get("admin");
		if(rule == null){
			rule = allowPermission.get("admin");
			if(rule != null){
				permission.put("admin", rule);
			}
		}
		// create
		rule = denyPermission.get("create");
		if(rule == null){
			rule = allowPermission.get("create");
			if(rule != null){
				permission.put("create", rule);
			}
		}
		// read
		rule = denyPermission.get("read");
		if(rule == null){
			rule = allowPermission.get("read");
			if(rule != null){
				permission.put("read", rule);
			}
		}
		// update
		rule = denyPermission.get("update");
		if(rule == null){
			rule = allowPermission.get("update");
			if(rule != null){
				permission.put("update", rule);
			}
		}
		// delete
		rule = denyPermission.get("delete");
		if(rule == null){
			rule = allowPermission.get("delete");
			if(rule != null){
				permission.put("delete", rule);
			}
		}
		return permission;
	}
	
	public Map<String, Object> getPermissionByIdUserAndTarget(Integer idUser, String key) throws JsonParseException, JsonMappingException, IOException {
		// Permission.
		Map<String, Object> result = new HashMap<String, Object>();
		// Check role.
		List<Integer> idroles = SecurityUtil.listIdRoles();
		if(idroles.contains(Role.ADMIN.getId())){
			result.put("admin", true);
			return result;
		}
		// Check user.
		Map<String, Object> denyPermission = new HashMap<String, Object>();
		Map<String, Object> allowPermission = new HashMap<String, Object>();
		// Check rules.
		Map<String, Object> rules = (Map<String, Object>) instance().get(key);
		// deny rules.
		Map<String, Object> denyRules = (Map<String, Object>) rules.get("deny");
		AddRulesIntoPermission(denyPermission, denyRules, idUser, false);
		// allow rules.
		Map<String, Object> allowRules = (Map<String, Object>) rules.get("allow");
		AddRulesIntoPermission(allowPermission, allowRules, idUser, true);
		// Merge permission.
		result = mergePermission(denyPermission, allowPermission);
		// Declare variables
		//boolean isAllow = false;
		List<Integer> ids;
		Object denyRule= null;
		Object allowRule= null;
		// adminoncreate.
		denyRule = denyRules.get("adminoncreates");
		ids = (ArrayList<Integer>) denyRule;
		if(ids.size() == 0) {
			result.put("adminoncreate", false);
		} else if(!ids.contains(0)){
			result.put("adminoncreate", !ids.contains(idUser));
		} else {
			allowRule = allowRules.get("adminoncreates");
			ids = (ArrayList<Integer>) allowRule;
			if(ids.size() == 0) {
				result.put("adminoncreate", true);
			} else if(!ids.contains(0)){
				result.put("adminoncreate", ids.contains(idUser));
			}
		}
		// adminonowner.
		denyRule = denyRules.get("adminonowners");
		ids = (ArrayList<Integer>) denyRule;
		if(ids.size() == 0) {
			result.put("adminonowner", false);
		} else if(!ids.contains(0)){
			result.put("adminonowner", !ids.contains(idUser));
		} else {
			allowRule = allowRules.get("adminonowners");
			ids = (ArrayList<Integer>) allowRule;
			if(ids.size() == 0) {
				result.put("adminonowner", true);
			} else if(!ids.contains(0)){
				result.put("adminonowner", ids.contains(idUser));
			}
		}

		// createbyids.
		boolean denyAll = false;
		boolean allowAll = false;
		List<Map<String, Object>> permissions = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> denyPermissions = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> allowPermissions = new ArrayList<Map<String, Object>>();
		denyRule = denyRules.get("createbyids");
		allowRule = allowRules.get("createbyids");
		if(null != denyRule) {
			List<Map<String, Object>> createbyids = (ArrayList<Map<String, Object>>) denyRule;
			for (Map<String, Object> createbyid1 : createbyids) {
				Map<String, Object> permission1 = new HashMap<String, Object>();
				AddRulesIntoPermission(permission1, createbyid1, idUser, false);
				List<Integer> createbyidsData = (List<Integer>) createbyid1.get("createbyids");
				if(createbyidsData.size() == 0){
					denyPermissions = new ArrayList<Map<String, Object>>();
					permission1.put("createbyid", -1);//all
					denyPermissions.add(permission1);
					denyAll = true;
					//break;
				} else if(createbyidsData.contains(0)){
					continue;
				}
				for (Integer createbyid : createbyidsData) {
					Map<String, Object> permission2 = new HashMap<String, Object>();
					permission2.putAll(permission1);// clone permission1.
					permission2.put("createbyid", createbyid);
					denyPermissions.add(permission2);
				}
			}
		}
		if(null != allowRule) {
			List<Map<String, Object>> createbyids = (ArrayList<Map<String, Object>>) allowRule;
			for (Map<String, Object> createbyid1 : createbyids) {
				Map<String, Object> permission1 = new HashMap<String, Object>();
				AddRulesIntoPermission(permission1, createbyid1, idUser, true);
				List<Integer> createbyidsData = (List<Integer>) createbyid1.get("createbyids");
				if(createbyidsData.size() == 0){
					allowPermissions = new ArrayList<Map<String, Object>>();
					permission1.put("createbyid", -1);//all
					allowPermissions.add(permission1);
					allowAll = true;
					//break;
				} else if(createbyidsData.contains(0)){
					continue;
				}
				for (Integer createbyid : createbyidsData) {
					Map<String, Object> permission2 = new HashMap<String, Object>();
					permission2.putAll(permission1);// clone permission1.
					permission2.put("createbyid", createbyid);
					allowPermissions.add(permission2);
				}
			}
		}
		// Merger allow and deny.
		List<Integer> founds = new ArrayList<Integer>();
		for (Map<String, Object> allowPer : allowPermissions) {
			Integer createbyidAllow = (Integer) allowPer.get("createbyid");
			boolean found = false;
			for (Map<String, Object> denyPer : denyPermissions) {
				Integer createbyidDeny = (Integer) denyPer.get("createbyid");
				if(createbyidDeny == createbyidAllow){
					// Merge permission.
					denyPer = mergePermission(denyPer, allowPer);
					permissions.add(denyPer);// add deny permission.
					founds.add(createbyidAllow);
					found = true;
					break;
				}
			}
			if(!found){
				permissions.add(allowPer);// add allow permission.
			}
		}
		if(denyPermissions.size() > founds.size()){
			for (Map<String, Object> denyPer : denyPermissions) {
				Integer createbyidDeny = (Integer) denyPer.get("createbyid");
				if(!founds.contains(createbyidDeny)){
					permissions.add(denyPer);// add deny permission.
				}
			}
		}
		result.put("createbyids", permissions);
		// ownerbyids.
		denyAll = false;
		allowAll = false;
		permissions = new ArrayList<Map<String, Object>>();
		denyPermissions = new ArrayList<Map<String, Object>>();
		allowPermissions = new ArrayList<Map<String, Object>>();
		denyRule = denyRules.get("ownerbyids");
		allowRule = allowRules.get("ownerbyids");
		if(null != denyRule) {
			List<Map<String, Object>> ownerbyids = (ArrayList<Map<String, Object>>) denyRule;
			for (Map<String, Object> ownerbyid1 : ownerbyids) {
				Map<String, Object> permission1 = new HashMap<String, Object>();
				AddRulesIntoPermission(permission1, ownerbyid1, idUser, false);
				List<Integer> ownerbyidsData = (List<Integer>) ownerbyid1.get("ownerbyids");
				if(ownerbyidsData.size() == 0){
					denyPermissions = new ArrayList<Map<String, Object>>();
					permission1.put("ownerbyid", -1);//all
					denyPermissions.add(permission1);
					denyAll = true;
					//break;
				} else if(ownerbyidsData.contains(0)){
					continue;
				}
				for (Integer ownerbyid : ownerbyidsData) {
					Map<String, Object> permission2 = new HashMap<String, Object>();
					permission2.putAll(permission1);// clone permission1.
					permission2.put("ownerbyid", ownerbyid);
					denyPermissions.add(permission2);
				}
			}
		}
		if(null != allowRule) {
			List<Map<String, Object>> ownerbyids = (ArrayList<Map<String, Object>>) allowRule;
			for (Map<String, Object> ownerbyid1 : ownerbyids) {
				Map<String, Object> permission1 = new HashMap<String, Object>();
				AddRulesIntoPermission(permission1, ownerbyid1, idUser, true);
				List<Integer> ownerbyidsData = (List<Integer>) ownerbyid1.get("ownerbyids");
				if(ownerbyidsData.size() == 0){
					allowPermissions = new ArrayList<Map<String, Object>>();
					permission1.put("ownerbyid", -1);//all
					allowPermissions.add(permission1);
					allowAll = true;
					//break;
				} else if(ownerbyidsData.contains(0)){
					continue;
				}
				for (Integer ownerbyid : ownerbyidsData) {
					Map<String, Object> permission2 = new HashMap<String, Object>();
					permission2.putAll(permission1);// clone permission1.
					permission2.put("ownerbyid", ownerbyid);
					allowPermissions.add(permission2);
				}
			}
		}
		// Merger allow and deny.
		founds = new ArrayList<Integer>();
		for (Map<String, Object> allowPer : allowPermissions) {
			Integer ownerbyidAllow = (Integer) allowPer.get("ownerbyid");
			boolean found = false;
			for (Map<String, Object> denyPer : denyPermissions) {
				Integer ownerbyidDeny = (Integer) denyPer.get("ownerbyid");
				if(ownerbyidDeny == ownerbyidAllow){
					// Merge permission.
					denyPer = mergePermission(denyPer, allowPer);
					permissions.add(denyPer);// add deny permission.
					founds.add(ownerbyidAllow);
					found = true;
					break;
				}
			}
			if(!found){
				permissions.add(allowPer);// add allow permission.
			}
		}
		if(denyPermissions.size() > founds.size()){
			for (Map<String, Object> denyPer : denyPermissions) {
				Integer ownerbyidDeny = (Integer) denyPer.get("ownerbyid");
				if(!founds.contains(ownerbyidDeny)){
					permissions.add(denyPer);// add deny permission.
				}
			}
		}
		result.put("ownerbyids", permissions);
		
		return result;
	}
	
	public Map<String, Object> getPermissionByIdUser(Integer idUser) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> permissionRepo = instance();
		Set<String> keys = permissionRepo.keySet();
		for (String key : keys) {
			Map<String, Object> permission = getPermissionByIdUserAndTarget(idUser, key);
			// Add permission to result;
			result.put(key, permission);
		}
		// return.
		return result;
	}
	
	
	private boolean isPermis(Map<String, Object> rules, Integer userId) {
		Object rule;
		// admin.
		rule = rules.get("admin");
		if(rule != null && (boolean)rule) {
			return true;
		}
		// create.
		rule = rules.get("create");
		if(rule != null && (boolean)rule) {
			return true;
		}
		// read.
		rule = rules.get("read");
		if(rule != null && (boolean)rule) {
			return true;
		}
		// update.
		rule = rules.get("update");
		if(rule != null && (boolean)rule) {
			return true;
		}
		// delete.
		rule = rules.get("delete");
		if(rule != null && (boolean)rule) {
			return true;
		}
		// return.
		return false;
	}

	
	public Map<String, Object> getPermissionForUserMenu(Integer idUser) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		boolean isPer = false;
		// Check object.
		Map<String, Object> permissionRepo = instance();
		Set<String> keys = permissionRepo.keySet();
		// Check role.
		List<Integer> idroles = SecurityUtil.listIdRoles();
		if(idroles.contains(Role.ADMIN.getId())){
			isPer = true;
		}
		if(isPer){
			for (String key : keys) {
				result.put(key, isPer);
			}
			// return.
			return result;
		}
		// Check user.
		for (String key : keys) {
			Map<String, Object> rules = getPermissionByIdUserAndTarget(idUser, key);
			// Add permission to result;
			isPer = isPermis(rules, idUser);
			if(isPer) {
				result.put(key, isPer);
				continue;
			}
			Object rule;
			// adminoncreate.
			rule = rules.get("adminoncreate");
			if(rule != null && (boolean)rule) {
				result.put(key, true);
				continue;
			}
			// adminonowner.
			rule = rules.get("adminonowner");
			if(rule != null && (boolean)rule) {
				result.put(key, true);
				continue;
			}

			List<Map<String, Object>> dataRules = null;
			boolean found = false;
			// createbyids.
			dataRules = (List<Map<String, Object>>) rules.get("createbyids");
			for (Map<String, Object> dataRule : dataRules) {
				isPer = isPermis(dataRule, idUser);
				if(isPer) {
					result.put(key, isPer);
					found = true;
					break;
				}
			}
			if(found){
				continue;
			}
			// ownerbyids.
			dataRules = (List<Map<String, Object>>) rules.get("ownerbyids");
			found = false;
			for (Map<String, Object> dataRule : dataRules) {
				isPer = isPermis(dataRule, idUser);
				if(isPer) {
					result.put(key, isPer);
					found = true;
					break;
				}
			}
			if(found){
				continue;
			}
		}
		// return.
		return result;
	}
	
	public boolean isCreate(String target, Integer iduser) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> permission = (Map<String, Object>) this.getPermissionByIdUser(iduser).get(target);
		Object actionPer;
		// admin.
		actionPer = permission.get("admin");
		if(actionPer != null && (boolean)actionPer) {
			return true;
		}
		// create.
		actionPer = permission.get("create");
		if(actionPer != null && (boolean)actionPer) {
			return true;
		}
		return false;
	}
	
	public boolean isAction(Map<String, Object> permission, Integer iduser, Integer idcreate, Integer idowner, String action, boolean isAllow) throws JsonParseException, JsonMappingException, IOException {
		Object actionPer;
		// admin.
		actionPer = permission.get("admin");
		if(actionPer != null && ((boolean)actionPer == isAllow)) {
			return true;
		}
		// action.
		actionPer = permission.get(action);
		if(actionPer != null && ((boolean)actionPer == isAllow)) {
			return true;
		}
		Object dataPer;
		// adminoncreate.
		dataPer = permission.get("adminoncreate");
		if(dataPer != null && ((boolean)dataPer == isAllow) && iduser == idcreate) {
			return true;
		}
		// adminonowner.
		dataPer = permission.get("adminonowner");
		if(dataPer != null && ((boolean)dataPer == isAllow) && iduser == idowner) {
			return true;
		}
		// createbyids.
		List<Map<String, Object>> createbyids = (ArrayList<Map<String, Object>>) permission.get("createbyids");
		for (Map<String, Object> createbyid : createbyids) {
			// admin.
			dataPer = createbyid.get("admin");
			Integer id = (Integer) createbyid.get("createbyid");
			if(dataPer != null && ((boolean)dataPer == isAllow) && id == idcreate) {
				return true;
			}
			// action.
			dataPer = createbyid.get(action);
			id = (Integer) createbyid.get("createbyid");
			if(dataPer != null && ((boolean)dataPer == isAllow) && id == idcreate) {
				return true;
			}
		}// for
		// ownerbyids.
		List<Map<String, Object>> ownerbyids = (ArrayList<Map<String, Object>>) permission.get("ownerbyids");
		for (Map<String, Object> ownerbyid : ownerbyids) {
			// admin.
			dataPer = ownerbyid.get("admin");
			Integer id = (Integer) ownerbyid.get("ownerbyid");
			if(dataPer != null && ((boolean)dataPer == isAllow) && id == idowner) {
				return true;
			}
			// action.
			dataPer = ownerbyid.get(action);
			id = (Integer) ownerbyid.get("ownerbyid");
			if(dataPer != null && ((boolean)dataPer == isAllow) && id == idowner) {
				return true;
			}
		}// for
		// return.
		return false;
	}
	
	public boolean isRead(String target, Integer iduser, Integer idcreate, Integer idowner) throws JsonParseException, JsonMappingException, IOException {
		// Check role.
		List<Integer> idroles = SecurityUtil.listIdRoles();
		if(idroles.contains(Role.ADMIN.getId())){
			return true;
		}
		// Check user.
		Map<String, Object> permission = (Map<String, Object>) this.getPermissionByIdUserAndTarget(iduser, target);
		boolean deny = isAction(permission, iduser, idcreate, idowner, "read", false);
		if(deny){
			return false;
		}
		boolean allow = isAction(permission, iduser, idcreate, idowner, "read", true);
		// return.
		return allow;
	}
	
	public boolean isUpdate(String target, Integer iduser, Integer idcreate, Integer idowner) throws JsonParseException, JsonMappingException, IOException {
		// Check role.
		List<Integer> idroles = SecurityUtil.listIdRoles();
		if(idroles.contains(Role.ADMIN.getId())){
			return true;
		}
		// Check user.
		Map<String, Object> permission = (Map<String, Object>) this.getPermissionByIdUserAndTarget(iduser, target);
		boolean deny = isAction(permission, iduser, idcreate, idowner, "update", false);
		if(deny){
			return false;
		}
		boolean allow = isAction(permission, iduser, idcreate, idowner, "update", true);
		// return.
		return allow;
	}
	
	public boolean isDelete(String target, Integer iduser, Integer idcreate, Integer idowner) throws JsonParseException, JsonMappingException, IOException {
		// Check role.
		List<Integer> idroles = SecurityUtil.listIdRoles();
		if(idroles.contains(Role.ADMIN.getId())){
			return true;
		}
		// Check user.
		Map<String, Object> permission = (Map<String, Object>) this.getPermissionByIdUserAndTarget(iduser, target);
		boolean deny = isAction(permission, iduser, idcreate, idowner, "delete", false);
		if(deny){
			return false;
		}
		boolean allow = isAction(permission, iduser, idcreate, idowner, "delete", true);
		// return.
		return allow;
	}
	
	public boolean isList(List<SearchCriteria> searchCriterias, String target) throws JsonParseException, JsonMappingException, IOException {
		// Check role.
		List<Integer> idroles = SecurityUtil.listIdRoles();
		if(idroles.contains(Role.ADMIN.getId())){
			return true;
		}
		// Check user.
		Integer iduser = SecurityUtil.getIdUser();
		Map<String, Object> list = getForList(target, iduser);
		Object admin = list.get("admin");
		Object read = list.get("read");
		ArrayList<Integer> idcreates = (ArrayList<Integer>) list.get("idcreates");
		ArrayList<Integer> idowners = (ArrayList<Integer>) list.get("idowners");
		List<SearchCriteria> criterias = new ArrayList<SearchCriteria>();
		boolean isPer = false;
		// admin deny.
		if(admin != null) {
			isPer = (boolean)admin;
			if(!isPer){
				return false;
			}
		}
		// read deny.
		if(read != null) {
			isPer = (boolean)read;
			if(!isPer){
				return false;
			}
		}
		if(isPer){
			SearchCriteria criteria = new SearchCriteria("id", ">", -1, "or");
			criterias.add(criteria);
		} else {
			SearchCriteria criteria = new SearchCriteria("id", "<", -1, "or");
			criterias.add(criteria);
		}
		// idcreates
		if(idcreates != null && idcreates.size() > 0 && !idcreates.contains(-1)) {
			SearchCriteria criteria = new SearchCriteria("idcreate", "in", idcreates, "or");
			criterias.add(criteria);
		}
		// idowners
		if(idowners != null && idowners.size() > 0 && !idowners.contains(-1)) {
			SearchCriteria criteria = new SearchCriteria("idowner", "in", idowners, "or");
			criterias.add(criteria);
		}
		
		if(criterias.size() > 0) {
			if(searchCriterias == null) {
				searchCriterias = new ArrayList<SearchCriteria>();
			}
			if(searchCriterias.size() > 0) {
				SearchCriteria criteria = new SearchCriteria("and");
				searchCriterias.add(criteria);
			}
			searchCriterias.addAll(criterias);
		}

		return true;
	}
	
	// TODO.
	public Map<String, Object> getForList(Map<String, Object> permission, Integer iduser, boolean isAllow) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Integer> idcreates = new ArrayList<Integer>();
		List<Integer> idowners = new ArrayList<Integer>();
		boolean found = false;
		Object actionPer;
		// admin.
		actionPer = permission.get("admin");
		if(actionPer != null && ((boolean)actionPer == isAllow)) {
			found = true;
		} else {
			// read.
			actionPer = permission.get("read");
			if(actionPer != null && ((boolean)actionPer == isAllow)) {
				found = true;
			}
		}
		if(found){
			result.put("read", true);
			return result;
		}
		Object dataPer;
		// createbyids.
		List<Map<String, Object>> createbyids = (ArrayList<Map<String, Object>>) permission.get("createbyids");
		for (Map<String, Object> createbyid : createbyids) {
			found = false;
			// admin.
			dataPer = createbyid.get("admin");
			if(dataPer != null && ((boolean)dataPer == isAllow)) {
				found = true;
			} else {
				// read.
				dataPer = permission.get("read");
				if(dataPer != null && ((boolean)dataPer == isAllow)) {
					found = true;
				}
			}
			if(found){
				idcreates.add((Integer) createbyid.get("createbyid"));
			}
		}// for
		// ownerbyids.
		List<Map<String, Object>> ownerbyids = (ArrayList<Map<String, Object>>) permission.get("ownerbyids");
		for (Map<String, Object> ownerbyid : ownerbyids) {
			found = false;
			// admin.
			dataPer = ownerbyid.get("admin");
			if(dataPer != null && ((boolean)dataPer == isAllow)) {
				found = true;
			} else {
				// read.
				dataPer = permission.get("read");
				if(dataPer != null && ((boolean)dataPer == isAllow)) {
					found = true;
				}
			}
			if(found){
				idowners.add((Integer) ownerbyid.get("ownerbyid"));
			}
		}// for
		// adminoncreate.
		dataPer = permission.get("adminoncreate");
		if(dataPer != null && ((boolean)dataPer == isAllow)) {
			idcreates.add(iduser);
		}
		if(idcreates.size() > 0){
			result.put("idcreates", idcreates);
		}
		// adminonowner.
		dataPer = permission.get("adminonowner");
		if(dataPer != null && ((boolean)dataPer == isAllow)) {
			idowners.add(iduser);
		}
		if(idowners.size() > 0){
			result.put("idowners", idowners);
		}
		// return.
		return result;
	}

	public Map<String, Object> getForList(String target, Integer iduser) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> permission = (Map<String, Object>) this.getPermissionByIdUserAndTarget(iduser, target);
		Map<String, Object> result = new HashMap<String, Object>();
		List<Integer> idcreatesAllow = new ArrayList<Integer>();
		List<Integer> idownersAllow = new ArrayList<Integer>();
		List<Integer> idcreatesDeny = new ArrayList<Integer>();
		List<Integer> idownersDeny = new ArrayList<Integer>();
		Object actionPer;
		// admin.
		actionPer = permission.get("admin");
		if(actionPer != null) {
			result.put("admin", true);
		} else {
			// read.
			actionPer = permission.get("read");
			if(actionPer != null) {
				result.put("read", true);
			}
		}
		Object dataPer;
		// createbyids.
		List<Map<String, Object>> createbyids = (ArrayList<Map<String, Object>>) permission.get("createbyids");
		for (Map<String, Object> createbyid : createbyids) {
			// admin.
			dataPer = createbyid.get("admin");
			if(dataPer != null) {
				Integer id = (Integer) createbyid.get("createbyid");
				if((boolean)dataPer){
					idcreatesAllow.add(id);
				} else {
					// Deny all.
					if(id == -1){
						result.clear();
						result.put("admin", false);
						// return.
						return result;
					}
					idcreatesDeny.add(id);
				}
			}
			// read.
			dataPer = createbyid.get("read");
			if(dataPer != null) {
				Integer id = (Integer) createbyid.get("createbyid");
				if((boolean)dataPer){
					idcreatesAllow.add(id);
				} else {
					// Deny all.
					if(id == -1){
						result.clear();
						result.put("admin", false);
						// return.
						return result;
					}
					idcreatesDeny.add(id);
				}
			}
		}// for
		// ownerbyids.
		List<Map<String, Object>> ownerbyids = (ArrayList<Map<String, Object>>) permission.get("ownerbyids");
		for (Map<String, Object> ownerbyid : ownerbyids) {
			// admin.
			dataPer = ownerbyid.get("admin");
			if(dataPer != null) {
				Integer id = (Integer) ownerbyid.get("ownerbyid");
				if((boolean)dataPer){
					idownersAllow.add(id);
				} else {
					// Deny all.
					if(id == -1){
						result.clear();
						result.put("admin", false);
						// return.
						return result;
					}
					idownersDeny.add(id);
				}
			}
			// read.
			dataPer = ownerbyid.get("read");
			if(dataPer != null) {
				Integer id = (Integer) ownerbyid.get("ownerbyid");
				if((boolean)dataPer){
					idownersAllow.add(id);
				} else {
					// Deny all.
					if(id == -1){
						result.clear();
						result.put("admin", false);
						// return.
						return result;
					}
					idownersDeny.add(id);
				}
			}
		}// for
		boolean found;
		// adminoncreate.
		dataPer = permission.get("adminoncreate");
		if(dataPer != null) {
			if((boolean)dataPer){
				idcreatesAllow.add(iduser);
			} else {
				idcreatesDeny.add(iduser);
			}
		}
		// Merge allow and deny.
		List<Integer> idcreates = new ArrayList<Integer>();
		for (Integer idallow : idcreatesAllow) {
			if(idallow == -1 && idcreatesDeny.size() == 0){
				idcreates.add(idallow);
				break;
			}
			found = false;
			for (Integer iddeny : idcreatesDeny) {
				if(idallow == iddeny){
					found = true;
				}
			}
			if(!found){
				idcreates.add(idallow);
			}
		}
		if(idcreates.size() > 0){
			result.put("idcreates", idcreates);
		}
		// adminonowner.
		dataPer = permission.get("adminonowner");
		if(dataPer != null) {
			if((boolean)dataPer){
				idownersAllow.add(iduser);
			} else {
				idownersAllow.add(iduser);
			}
		}
		// Merge allow and deny.
		List<Integer> idowners = new ArrayList<Integer>();
		for (Integer idallow : idownersAllow) {
			if(idallow == -1 && idownersDeny.size() == 0){
				idowners.add(idallow);
				break;
			}
			found = false;
			for (Integer iddeny : idownersDeny) {
				if(idallow == iddeny){
					found = true;
				}
			}
			if(!found){
				idowners.add(idallow);
			}
		}
		if(idowners.size() > 0){
			result.put("idowners", idowners);
		}
		// return.
		return result;
	}
	
	
	@Autowired
	private PermissionService permissionService;

	public boolean canCreate(Permission permission) {
		return true;
	}

	public boolean canRead(Integer id) {
		return true;
	}

	public boolean canRead(List<SearchCriteria> searchCriterias) {
		return true;
	}

	public boolean canUpdate(Permission permission) {
		return true;
	}

	public boolean canDelete(Integer id) {
		return true;
	}

}
