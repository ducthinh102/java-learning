
package com.redsun.server.main.authorize;

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
import com.redsun.server.main.controller.PermissionRestController;
import com.redsun.server.main.model.Permission;
import com.redsun.server.main.model.common.SearchCriteria;
import com.redsun.server.main.service.PermissionService;

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
		if(null == permissionRepo) {
			reloadPermissionRepo();
		}
		// return.
		return permissionRepo;
	}
	
	private void AddRulesIntoPermission(Map<String, Object> permission, Map<String, Object> rules, Integer userId) {
		Object rule;
		// admin.
		rule = rules.get("admins");
		if(null != rule) {
			List<Integer> ids = (ArrayList<Integer>) rule;
			if(ids.size() == 0 || ids.contains(userId)) {
				permission.put("admin", true);
			}
		}
		// create.
		rule = rules.get("creates");
		if(null != rule) {
			List<Integer> ids = (ArrayList<Integer>) rule;
			if(ids.size() == 0 || ids.contains(userId)) {
				permission.put("create", true);
			}
		}
		// read.
		rule = rules.get("reads");
		if(null != rule) {
			List<Integer> ids = (ArrayList<Integer>) rule;
			if(ids.size() == 0 || ids.contains(userId)) {
				permission.put("read", true);
			}
		}
		// update.
		rule = rules.get("updates");
		if(null != rule) {
			List<Integer> ids = (ArrayList<Integer>) rule;
			if(ids.size() == 0 || ids.contains(userId)) {
				permission.put("update", true);
			}
		}
		// delete.
		rule = rules.get("deletes");
		if(null != rule) {
			List<Integer> ids = (ArrayList<Integer>) rule;
			if(ids.size() == 0 || ids.contains(userId)) {
				permission.put("delete", true);
			}
		}
	}
	
	public Map<String, Object> getPermissionByIdUser(Integer idUser) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		// Check object.
		Map<String, Object> permissionRepo = instance();
		Set<String> keys = permissionRepo.keySet();
		for (String key : keys) {
			// Permission.
			Map<String, Object> permission = new HashMap<String, Object>();
			// Check rules.
			Map<String, Object> rules = (Map<String, Object>) permissionRepo.get(key);
			
			AddRulesIntoPermission(permission, rules, idUser);
			
			Object rule= null;
			// adminoncreate.
			rule = rules.get("adminoncreates");
			if(null != rule) {
				List<Integer> ids = (ArrayList<Integer>) rule;
				if(ids.size() == 0 || ids.contains(idUser)) {
					permission.put("adminoncreate", true);
				}
			}
			// adminonowner.
			rule = rules.get("adminonowners");
			if(null != rule) {
				List<Integer> ids = (ArrayList<Integer>) rule;
				if(ids.size() == 0 || ids.contains(idUser)) {
					permission.put("adminonowner", true);
				}
			}

			// createbyids.
			rule = rules.get("createbyids");
			if(null != rule) {
				List<Map<String, Object>> createbyids = (ArrayList<Map<String, Object>>) rule;
				List<Map<String, Object>> permissions1 = new ArrayList<Map<String, Object>>();
				for (Map<String, Object> createbyid1 : createbyids) {
					Map<String, Object> permission1 = new HashMap<String, Object>();
					AddRulesIntoPermission(permission1, createbyid1, idUser);
					if(permission1.size() > 0) {
						permission1.put("createbyids", createbyid1.get("createbyids"));
						permissions1.add(permission1);
					}
				}
				if(permissions1.size() > 0){
					permission.put("createbyids", permissions1);
				}
			}
			// ownerbyids.
			rule = rules.get("ownerbyids");
			if(null != rule) {
				List<Map<String, Object>> ownerbyids = (ArrayList<Map<String, Object>>) rule;
				List<Map<String, Object>> permissions1 = new ArrayList<Map<String, Object>>();
				for (Map<String, Object> ownerbyid1 : ownerbyids) {
					Map<String, Object> permission1 = new HashMap<String, Object>();
					AddRulesIntoPermission(permission1, ownerbyid1, idUser);
					if(permission1.size() > 0) {
						permission1.put("ownerbyids", ownerbyid1.get("ownerbyids"));
						permissions1.add(permission1);
					}
				}
				if(permissions1.size() > 0){
					permission.put("ownerbyids", permissions1);
				}
			}
			
			// Add permission to result;
			result.put(key, permission);
		}
		// return.
		return result;
	}
	
	
	private boolean isPermis(Map<String, Object> rules, Integer userId) {
		Object rule;
		// admin.
		rule = rules.get("admins");
		if(null != rule) {
			List<Integer> ids = (ArrayList<Integer>) rule;
			if(ids.size() == 0 || ids.contains(userId)) {
				return true;
			}
		}
		// create.
		rule = rules.get("creates");
		if(null != rule) {
			List<Integer> ids = (ArrayList<Integer>) rule;
			if(ids.size() == 0 || ids.contains(userId)) {
				return true;
			}
		}
		// read.
		rule = rules.get("reads");
		if(null != rule) {
			List<Integer> ids = (ArrayList<Integer>) rule;
			if(ids.size() == 0 || ids.contains(userId)) {
				return true;
			}
		}
		// update.
		rule = rules.get("updates");
		if(null != rule) {
			List<Integer> ids = (ArrayList<Integer>) rule;
			if(ids.size() == 0 || ids.contains(userId)) {
				return true;
			}
		}
		// delete.
		rule = rules.get("deletes");
		if(null != rule) {
			List<Integer> ids = (ArrayList<Integer>) rule;
			if(ids.size() == 0 || ids.contains(userId)) {
				return true;
			}
		}
		return false;
	}

	
	public Map<String, Object> getPermissionForUserMenu(Integer idUser) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		// Check object.
		Map<String, Object> permissionRepo = instance();
		Set<String> keys = permissionRepo.keySet();
		for (String key : keys) {
			// Check rules.
			Map<String, Object> rules = (Map<String, Object>) permissionRepo.get(key);
			// Add permission to result;
			boolean isPer = isPermis(rules, idUser);
			if(isPer) {
				result.put(key, isPer);
				continue;
			}
			Object rule= null;
			// adminoncreate.
			rule = rules.get("adminoncreates");
			if(null != rule) {
				List<Integer> ids = (ArrayList<Integer>) rule;
				if(ids.size() == 0 || ids.contains(idUser)) {
					result.put(key, isPer);
					continue;
				}
			}
			// adminonowner.
			rule = rules.get("adminonowners");
			if(null != rule) {
				List<Integer> ids = (ArrayList<Integer>) rule;
				if(ids.size() == 0 || ids.contains(idUser)) {
					result.put(key, isPer);
					continue;
				}
			}

			// createbyids.
			rule = rules.get("createbyids");
			if(null != rule) {
				List<Map<String, Object>> createbyids = (ArrayList<Map<String, Object>>) rule;
				Map<String, Object> permission1 = null;
				for (Map<String, Object> createbyids1 : createbyids) {
					isPer = isPermis(createbyids1, idUser);
					if(isPer) {
						result.put(key, isPer);
						continue;
					}
				}
			}
			// ownerbyids.
			rule = rules.get("ownerbyids");
			if(null != rule) {
				List<Map<String, Object>> ownerbyids = (ArrayList<Map<String, Object>>) rule;
				Map<String, Object> permission1 = null;
				for (Map<String, Object> ownerbyids1 : ownerbyids) {
					isPer = isPermis(ownerbyids1, idUser);
					if(isPer) {
						result.put(key, isPer);
						continue;
					}
				}
			}
		}
		// return.
		return result;
	}
	
	public boolean isCreate(String target, Integer iduser) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> permission = (Map<String, Object>) this.getPermissionByIdUser(iduser).get(target);
		Object actionPer = null;
		// admin.
		actionPer = permission.get("admin");
		if(actionPer == null) {
			// create.
			actionPer = permission.get("create");
		}
		return actionPer != null;
	}
	
	public boolean isRead(String target, Integer iduser, Integer idcreate, Integer idowner) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> permission = (Map<String, Object>) this.getPermissionByIdUser(iduser).get(target);
		// admin.
		Object actionPer = permission.get("admin");
		if(actionPer == null) {
			// read.
			actionPer = permission.get("read");
		}
		if(actionPer != null){
			return true;
		}
		Object dataPer = null;
		// adminoncreate.
		dataPer = permission.get("adminoncreate");
		if(dataPer == null || iduser != idcreate) {
			// adminonowner.
			dataPer = permission.get("adminonowner");
			if(dataPer == null || iduser != idowner) {
				// createbyids.
				dataPer = permission.get("createbyids");
				if(dataPer != null) {
					List<Map<String, Object>> createbyids = (ArrayList<Map<String, Object>>) dataPer;
					for (Map<String, Object> createbyid : createbyids) {
						dataPer = createbyid.get("createbyids");
						if(dataPer != null) {
							List<Integer> rules = (ArrayList<Integer>) dataPer;
							if(rules.indexOf(idcreate) > -1) {
								// admin.
								dataPer = createbyid.get("admin");
								if(dataPer == null) {
									// read.
									dataPer = createbyid.get("read");
									if(dataPer != null) {
										break;
									}
								} else {
									break;
								}
							} else {
								dataPer = null;
							}
						}
					}// for
				}
				if(dataPer == null) {
					// ownerbyids.
					dataPer = permission.get("ownerbyids");
					if(dataPer != null) {
						List<Map<String, Object>> ownerbyids = (ArrayList<Map<String, Object>>) dataPer;
						for (Map<String, Object> ownerbyid : ownerbyids) {
							dataPer = ownerbyid.get("ownerbyids");
							if(dataPer != null) {
								List<Integer> rules = (ArrayList<Integer>) dataPer;
								if(rules.indexOf(idcreate) > -1) {
									// admin.
									dataPer = ownerbyid.get("admin");
									if(dataPer == null) {
										// read.
										dataPer = ownerbyid.get("read");
										if(dataPer != null) {
											break;
										}
									} else {
										break;
									}
								} else {
									dataPer = null;
								}
							}
						}// for
					}
				}// if
			}
		}
		return dataPer != null;
	}
	
	public boolean isUpdate(String target, Integer iduser, Integer idcreate, Integer idowner) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> permission = (Map<String, Object>) this.getPermissionByIdUser(iduser).get(target);
		// admin.
		Object actionPer = permission.get("admin");
		if(actionPer == null) {
			// update.
			actionPer = permission.get("update");
		}
		if(actionPer != null){
			return true;
		}
		Object dataPer = null;
		// adminoncreate.
		dataPer = permission.get("adminoncreate");
		if(dataPer == null || iduser != idcreate) {
			// adminonowner.
			dataPer = permission.get("adminonowner");
			if(dataPer == null || iduser != idowner) {
				// createbyids.
				dataPer = permission.get("createbyids");
				if(dataPer != null) {
					List<Map<String, Object>> createbyids = (ArrayList<Map<String, Object>>) dataPer;
					for (Map<String, Object> createbyid : createbyids) {
						dataPer = createbyid.get("createbyids");
						if(dataPer != null) {
							List<Integer> rules = (ArrayList<Integer>) dataPer;
							if(rules.indexOf(idcreate) > -1) {
								// admin.
								dataPer = createbyid.get("admin");
								if(dataPer == null) {
									// update.
									dataPer = createbyid.get("update");
									if(dataPer != null) {
										break;
									}
								} else {
									break;
								}
							} else {
								dataPer = null;
							}
						}
					}// for
				}
				if(dataPer == null) {
					// ownerbyids.
					dataPer = permission.get("ownerbyids");
					if(dataPer != null) {
						List<Map<String, Object>> ownerbyids = (ArrayList<Map<String, Object>>) dataPer;
						for (Map<String, Object> ownerbyid : ownerbyids) {
							dataPer = ownerbyid.get("ownerbyids");
							if(dataPer != null) {
								List<Integer> rules = (ArrayList<Integer>) dataPer;
								if(rules.indexOf(idcreate) > -1) {
									// admin.
									dataPer = ownerbyid.get("admin");
									if(dataPer == null) {
										// update.
										dataPer = ownerbyid.get("update");
										if(dataPer != null) {
											break;
										}
									} else {
										break;
									}
								} else {
									dataPer = null;
								}
							}
						}// for
					}
				}// if
			}
		}
		return dataPer != null;
	}
	
	public boolean isDelete(String target, Integer iduser, Integer idcreate, Integer idowner) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> permission = (Map<String, Object>) this.getPermissionByIdUser(iduser).get(target);
		// admin.
		Object actionPer = permission.get("admin");
		if(actionPer == null) {
			// delete.
			actionPer = permission.get("delete");
		}
		if(actionPer != null){
			return true;
		}
		Object dataPer = null;
		// adminoncreate.
		dataPer = permission.get("adminoncreate");
		if(dataPer == null || iduser != idcreate) {
			// adminonowner.
			dataPer = permission.get("adminonowner");
			if(dataPer == null || iduser != idowner) {
				// createbyids.
				dataPer = permission.get("createbyids");
				if(dataPer != null) {
					List<Map<String, Object>> createbyids = (ArrayList<Map<String, Object>>) dataPer;
					for (Map<String, Object> createbyid : createbyids) {
						dataPer = createbyid.get("createbyids");
						if(dataPer != null) {
							List<Integer> rules = (ArrayList<Integer>) dataPer;
							if(rules.indexOf(idcreate) > -1) {
								// admin.
								dataPer = createbyid.get("admin");
								if(dataPer == null) {
									// delete.
									dataPer = createbyid.get("delete");
									if(dataPer != null) {
										break;
									}
								} else {
									break;
								}
							} else {
								dataPer = null;
							}
						}
					}// for
				}
				if(dataPer == null) {
					// ownerbyids.
					dataPer = permission.get("ownerbyids");
					if(dataPer != null) {
						List<Map<String, Object>> ownerbyids = (ArrayList<Map<String, Object>>) dataPer;
						for (Map<String, Object> ownerbyid : ownerbyids) {
							dataPer = ownerbyid.get("ownerbyids");
							if(dataPer != null) {
								List<Integer> rules = (ArrayList<Integer>) dataPer;
								if(rules.indexOf(idcreate) > -1) {
									// admin.
									dataPer = ownerbyid.get("admin");
									if(dataPer == null) {
										// delete.
										dataPer = ownerbyid.get("delete");
										if(dataPer != null) {
											break;
										}
									} else {
										break;
									}
								} else {
									dataPer = null;
								}
							}
						}// for
					}
				}// if
			}
		}
		return dataPer != null;
	}
	
	// Return {"idcreates":[], "idowners":[]}.
	public Map<String, Object> getForList(String target, Integer iduser) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Integer> idcreates = new ArrayList<Integer>();;
		List<Integer> idowners = new ArrayList<Integer>();;
		
		Map<String, Object> permission = (Map<String, Object>) this.getPermissionByIdUser(iduser).get(target);
		Object permis = null;
		// admin.
		boolean admin = false;
		// read.
		boolean read = false;
		permis = permission.get("admin");
		if(permis == null) {
			permis = permission.get("read");
			if(permis == null) {
				// createbyids.
				permis = permission.get("createbyids");
				if(permis != null) {
					List<Map<String, Object>> createbyids = (ArrayList<Map<String, Object>>) permis;
					for (Map<String, Object> createbyid : createbyids) {
						permis = createbyid.get("createbyids");
						if(permis != null) {
							ArrayList<Integer> rules = (ArrayList<Integer>) permis;
							// amdin.
							permis = createbyid.get("admin");
							if(permis != null) {
								if(rules.size() > 0) {
									idcreates.addAll(rules);
								} else {
									admin = true;
								}
							} else {
								// read.
								permis = createbyid.get("read");
								if(permis != null) {
									if(rules.size() > 0) {
										idcreates.addAll(rules);
									} else {
										read = true;
									}
								}
							}
						}
					}// for
				}
				// ownerbyids.
				permis = permission.get("ownerbyids");
				if(permis != null) {
					List<Map<String, Object>> ownerbyids = (ArrayList<Map<String, Object>>) permis;
					for (Map<String, Object> ownerbyid : ownerbyids) {
						permis = ownerbyid.get("ownerbyids");
						if(permis != null) {
							ArrayList<Integer> rules = (ArrayList<Integer>) permis;
							// amdin.
							permis = ownerbyid.get("admin");
							if(permis != null) {
								if(rules.size() > 0) {
									idcreates.addAll(rules);
								} else {
									admin = true;
								}
							} else {
								// read.
								permis = ownerbyid.get("read");
								if(permis != null) {
									if(rules.size() > 0) {
										idcreates.addAll(rules);
									} else {
										read = true;
									}
								}
							}
						}
					}// for
				}
			} else {
				read = true;
			}
		} else {
			admin = true;
		}
		// admin.
		result.put("admin", admin);
		// read.
		result.put("read", read);
		// adminoncreate.
		permis = permission.get("adminoncreate");
		if(permis != null) {
			idcreates.add(iduser);
		}
		if(idcreates.size() > 0){
			result.put("idcreates", idcreates);
		}
		// adminonowner.
		permis = permission.get("adminonowner");
		if(permis != null) {
			idowners.add(iduser);
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
