

/**
 * Service for Workflowexecute
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'workflowexecuteService', function($http, $rootScope) {
	
	// Create.
	this.create = function(workflowexecute) {
		var serverUrl = clientwh.serverUrl + '/workflowexecute/create';
		var workflowexecute = {
				 method: 'POST',
				 url: serverUrl,
				 data: workflowexecute
				}
		return $http(workflowexecute);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientwh.serverUrl + '/workflowexecute/updateLock/' + id;
		var workflowexecute = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(workflowexecute);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientwh.serverUrl + '/workflowexecute/updateUnlock/' + id;
		var workflowexecute = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(workflowexecute);
	}
	
	// Update.
	this.update = function(id, workflowexecute) {
		var serverUrl = clientwh.serverUrl + '/workflowexecute/update/' + id;
		var workflowexecute = {
				 method: 'PUT',
				 url: serverUrl,
				 data: workflowexecute
				}
		return $http(workflowexecute);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, workflowexecute) {
		var serverUrl = clientwh.serverUrl + '/workflowexecute/updateWithLock/' + id;
		var workflowexecute = {
				 method: 'PUT',
				 url: serverUrl,
				 data: workflowexecute
				}
		return $http(workflowexecute);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/workflowexecute/updateForDelete/' + id + '/' + version;
		var workflowexecute = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(workflowexecute);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/workflowexecute/updateForDeleteWithLock/' + id + '/' + version;
		var workflowexecute = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(workflowexecute);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientwh.serverUrl + '/workflowexecute/delete/' + id;
		var workflowexecute = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(workflowexecute);
	}
	
	// Get by Id.
	this.getById = function(workflowexecuteId) {
		var serverUrl = clientwh.serverUrl + '/workflowexecute/getById/' + workflowexecuteId;
		var workflowexecute = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(workflowexecute);
	}
	
	// List workflowexecutes for page and filter.
	this.listWorkflowexecuteWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/workflowexecute/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
		if(typeof(sorts) !== 'undefined' && sorts.length > 0) {
			angular.forEach(sorts, function(sort) {
				serverUrl += '&' + sort;
			});
		}
		var workflowexecute = {
				 method: 'POST',
				 url: serverUrl,
				 data: criterias
				}
		return $http(workflowexecute);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(state, idtab, criterias, pageNo, pageSize, sorts) {
		var idworkflow = this.getIdWorkflowFromState(state);
		var serverUrl = clientwh.serverUrl + '/workflowexecute/listWithCriteriasByIdworkflowAndIdtabAndPage?' + 'page=' + pageNo + '&size=' + pageSize;
		if(typeof(sorts) !== 'undefined' && sorts.length > 0) {
			angular.forEach(sorts, function(sort) {
				serverUrl += '&' + sort;
			});
		}
		else
			serverUrl += '&sort=updatedate,desc';
		var workflowexecute = {
				 method: 'POST',
				 url: serverUrl,
				 data: criterias,
				 params: {'idworkflow': idworkflow, 'idtab': idtab}
				}
		return $http(workflowexecute);
	}
	
	// Execute Command.
	this.executeCommand = function(command, idworkflow, idref) {
		var serverUrl = clientwh.serverUrl + '/workflowexecute/execute';
		var workflowexecute = {
				 method: 'PUT',
				 url: serverUrl,
				 params: {'command':command, 'idworkflow':idworkflow, 'idref':idref}
				}
		return $http(workflowexecute);
	}

	// Assign.
	this.assign = function(idassignee, idworkflow, idref) {
		var serverUrl = clientwh.serverUrl + '/workflowexecute/assign';
		var workflowexecute = {
				 method: 'PUT',
				 url: serverUrl,
				 params: {'idassignee':idassignee, 'idworkflow':idworkflow, 'idref':idref}
				}
		return $http(workflowexecute);
	}
	
	// Check buttons visibility.
	this.checkButtonVisibility = function(idworkflow, idref) {
		var serverUrl = clientwh.serverUrl + '/workflowexecute/checkButtonVisibility/' + idworkflow + '/' + idref;
		var workflowexecute = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(workflowexecute);
	}

	// Get current workflow variable name from $state
	this.getWorkflowVariableNameFromState = function(state) {
		var currentControllerName = state.current.name;
		var workflowVariableName = currentControllerName.slice('clientwh'.length);
		if(!workflowVariableName)
			console.log('Cannot get workflow variable name from $state!');
		return workflowVariableName;
	}
	
	// Get idworkflow from $state
	this.getIdWorkflowFromState = function(state) {
		
		// Get current workflow variable name
		var workflowVariableName = this.getWorkflowVariableNameFromState(state);
		if(!workflowVariableName)
			return;
		
		// Get refType
		var refType = workflowVariableName.toUpperCase();
		if(workflowVariableName == 'materialform') {
			if(state.params.scope == 'prematerial')
				refType = 'MATERIALPRE';
			else if(state.params.scope == 'techmaterial')
				refType = 'MATERIALTECH';
			else {
				console.log('Materialform scope is undefined!');
				return;
			}
		}
		else if(workflowVariableName == 'request') {
			if(state.params.scope == 'normal')
				refType = 'REQUEST';
			else if(state.params.scope == 'extra')
				refType = 'REQUESTEXTRA';
			else {
				console.log('Request scope is undefined!');
				return;
			}
		}
		
		// Return
		var result = clientwh.idWorkflows[refType];
		if(!result)
			console.log('Cannot get idWorkflow from $state');
		return result;
		
	}
	
});

});
