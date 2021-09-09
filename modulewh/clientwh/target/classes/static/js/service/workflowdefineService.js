

/**
 * Service for Workflow Define
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'workflowdefineService', function($http, $rootScope) {
	
	//////////////////////////////////////
	// Workflow Define list
	/////////////////////////////////////

	// List for page and filter
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/workflowdefine/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
		if(typeof(sorts) !== 'undefined' && sorts.length > 0) {
			angular.forEach(sorts, function(sort) {
				serverUrl += '&' + sort;
			});
		}
		else
			serverUrl += '&sort=updatedate,desc';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: criterias
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(workflowdefineId, workflowdefineVersion) {
		var serverUrl = clientwh.serverUrl + '/workflowdefine/delete/' + workflowdefineId + '/' + workflowdefineVersion;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get all
	this.getAllWorkflows = function(id) {
		var serverUrl = clientwh.serverUrl + '/workflow/listAll';
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}

	//////////////////////////////////////
	// Workflow Define Form
	/////////////////////////////////////

	// Get by Id
	this.getById = function(id) {
		var serverUrl = clientwh.serverUrl + '/workflowdefine/getById/' + id;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}

	// Update a material
	this.update = function(id, item) {
		var serverUrl = clientwh.serverUrl + '/workflowdefine/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: item
				}
		return $http(request);
	}
	
	// Create.
	this.create = function(workflowdefine) {
		var serverUrl = clientwh.serverUrl + '/workflowdefine/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: workflowdefine
				}
		return $http(request);
	}
	
	// Check step is duplicated
	this.isStepExisted = function(idworkflow, step) {
		var serverUrl = clientwh.serverUrl + '/workflowdefine/isStepExisted/' + idworkflow + '/' + step;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
});

});
