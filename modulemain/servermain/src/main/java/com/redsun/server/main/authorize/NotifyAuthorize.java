
package com.redsun.server.main.authorize;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.main.controller.NotifyRestController;
import com.redsun.server.main.model.Notify;
import com.redsun.server.main.model.common.SearchCriteria;
import com.redsun.server.main.repository.NotifyRepository;

@Component("notifyAuthorize")
public class NotifyAuthorize {

	public static final Logger logger = LoggerFactory.getLogger(NotifyRestController.class);

	@Autowired
	PermissionAuthorize permissionAuthorize;
	
	@Autowired
	NotifyRepository notifyRepository;

	public boolean canCreate(Notify notify) throws JsonParseException, JsonMappingException, IOException {
		return true;
	}

	public boolean canRead(Integer id) throws JsonParseException, JsonMappingException, IOException {
		return true;
	}
	
	public boolean canUpdate(Notify notify) throws JsonParseException, JsonMappingException, IOException {
		return true;
	}

	public boolean canUpdateId(Integer id) throws JsonParseException, JsonMappingException, IOException {
		return true;
	}

	public boolean canUpdateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		return canUpdateId(id);
	}

	public boolean canDelete(Integer id) throws JsonParseException, JsonMappingException, IOException {
		return true;
	}

	public boolean canList(List<SearchCriteria> searchCriterias) throws JsonParseException, JsonMappingException, IOException {
		return true;
	}

}
