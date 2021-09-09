
package com.redsun.server.main.authorize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.main.controller.CommentRestController;
import com.redsun.server.main.model.Comment;
import com.redsun.server.main.model.common.SearchCriteria;
import com.redsun.server.main.repository.CommentRepository;
import com.redsun.server.main.util.SecurityUtil;

@Component("commentAuthorize")
public class CommentAuthorize {

	public static final Logger logger = LoggerFactory.getLogger(CommentRestController.class);

	@Autowired
	PermissionAuthorize permissionAuthorize;
	
	@Autowired
	CommentRepository commentRepository;

	public boolean canCreate(Comment comment) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		Integer iduser = SecurityUtil.getIdUser();
		result = permissionAuthorize.isCreate("comment", iduser);

		return result;
	}

	public boolean canRead(Integer id) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		Integer iduser = SecurityUtil.getIdUser();
		Map<String, Object> authorizeProperties = commentRepository.getAuthorizePropertiesById(id).get(0);
		Integer idcreate = (Integer)authorizeProperties.get("idcreate");
		Integer idowner = (Integer)authorizeProperties.get("idowner");
		result = permissionAuthorize.isRead("comment", iduser, idcreate, idowner);

		return result;
	}
	
	public boolean canUpdate(Comment comment) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		Integer iduser = SecurityUtil.getIdUser();
		Map<String, Object> authorizeProperties = commentRepository.getAuthorizePropertiesById(comment.getId()).get(0);
		Integer idcreate = (Integer)authorizeProperties.get("idcreate");
		Integer idowner = (Integer)authorizeProperties.get("idowner");
		result = permissionAuthorize.isUpdate("comment", iduser, idcreate, idowner);

		return result;
	}

	public boolean canUpdateId(Integer id) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		Integer iduser = SecurityUtil.getIdUser();
		Map<String, Object> authorizeProperties = commentRepository.getAuthorizePropertiesById(id).get(0);
		Integer idcreate = (Integer)authorizeProperties.get("idcreate");
		Integer idowner = (Integer)authorizeProperties.get("idowner");
		result = permissionAuthorize.isUpdate("comment", iduser, idcreate, idowner);

		return result;
	}

	public boolean canUpdateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		return canUpdateId(id);
	}

	public boolean canDelete(Integer id) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		Integer iduser = SecurityUtil.getIdUser();
		Map<String, Object> authorizeProperties = commentRepository.getAuthorizePropertiesById(id).get(0);
		Integer idcreate = (Integer)authorizeProperties.get("idcreate");
		Integer idowner = (Integer)authorizeProperties.get("idowner");
		result = permissionAuthorize.isDelete("comment", iduser, idcreate, idowner);

		return result;
	}

	public boolean canList(List<SearchCriteria> searchCriterias) throws JsonParseException, JsonMappingException, IOException {
		Integer iduser = SecurityUtil.getIdUser();
		
		Map<String, Object> list = permissionAuthorize.getForList("comment", iduser);
		boolean admin = (boolean) list.get("admin");
		boolean read = (boolean) list.get("read");
		ArrayList<Integer> idcreates = (ArrayList<Integer>) list.get("idcreates");
		ArrayList<Integer> idowners = (ArrayList<Integer>) list.get("idowners");
		// Denied
		if(!admin && !read && idcreates == null && idowners == null) {
			return false;
		}
		// admin or read.
		if(admin || read) {
			return true;
		}
		
		List<SearchCriteria> criterias = new ArrayList<SearchCriteria>();
		// idcreates
		if(idcreates != null && idcreates.size() > 0) {
			SearchCriteria criteria = new SearchCriteria("idcreate", "in", idcreates);
			criterias.add(criteria);
		}
		// idowners
		if(idowners != null && idowners.size() > 0) {
			SearchCriteria criteria = new SearchCriteria("idowner", "in", idowners);
			criterias.add(criteria);
		}
		
		if(searchCriterias == null) {
			searchCriterias = new ArrayList<SearchCriteria>();
		}
		if(searchCriterias.size() > 0) {
			SearchCriteria criteria = new SearchCriteria("", "and", "");
			searchCriterias.add(criteria);
		}
		if(criterias.size() > 0) {
			searchCriterias.addAll(criterias);
		}

		return true;
	}

}
