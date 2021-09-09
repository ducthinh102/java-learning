
package com.redsun.server.wh.authorize;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.wh.controller.RequestdetailRestController;
import com.redsun.server.wh.model.Requestdetail;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.RequestdetailRepository;

@Component("requestdetailAuthorize")
public class RequestdetailAuthorize {

	public static final Logger logger = LoggerFactory.getLogger(RequestdetailRestController.class);

	@Autowired
	PermissionAuthorize permissionAuthorize;
	
	@Autowired
	RequestdetailRepository requestdetailRepository;

	public boolean canCreate(Requestdetail requestdetail) throws JsonParseException, JsonMappingException, IOException {
		return true;
	}

	public boolean canRead(Integer id) throws JsonParseException, JsonMappingException, IOException {
		return true;
	}
	
	public boolean canUpdate(Requestdetail requestdetail) throws JsonParseException, JsonMappingException, IOException {
		return true;
	}

	public boolean canUpdateId(Integer id) throws JsonParseException, JsonMappingException, IOException {
		return true;
	}

	public boolean canUpdateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		return true;
	}

	public boolean canDelete(Integer id) throws JsonParseException, JsonMappingException, IOException {
		return true;
	}

	public boolean canList(List<SearchCriteria> searchCriterias) throws JsonParseException, JsonMappingException, IOException {
		return true;
	}

}
