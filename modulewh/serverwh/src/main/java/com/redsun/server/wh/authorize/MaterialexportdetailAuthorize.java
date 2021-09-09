
package com.redsun.server.wh.authorize;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.wh.model.Materialexportdetail;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.MaterialexportdetailRepository;


@Component("materialexportdetailAuthorize")
public class MaterialexportdetailAuthorize {


	@Autowired
	PermissionAuthorize permissionAuthorize;
	
	@Autowired
	MaterialexportdetailRepository materialexportdetailRepository;

	public boolean canCreate(Materialexportdetail materialexportdetail) throws JsonParseException, JsonMappingException, IOException {
		return true;
	}

	public boolean canRead(Integer id) throws JsonParseException, JsonMappingException, IOException {
		return true;
	}
	
	public boolean canUpdate(Materialexportdetail materialexportdetail) throws JsonParseException, JsonMappingException, IOException {
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
