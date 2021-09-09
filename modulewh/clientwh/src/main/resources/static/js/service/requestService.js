

/**
 * Service for Request
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'requestService', function($http, $rootScope) {
	
	// Create.
	this.create = function(request) {
		var serverUrl = clientwh.serverUrl + '/request/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: request
				}
		return $http(request);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientwh.serverUrl + '/request/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientwh.serverUrl + '/request/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, request) {
		var serverUrl = clientwh.serverUrl + '/request/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: request
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, request) {
		var serverUrl = clientwh.serverUrl + '/request/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: request
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/request/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/request/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientwh.serverUrl + '/request/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(requestId) {
		var serverUrl = clientwh.serverUrl + '/request/getById/' + requestId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/request/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
		if(typeof(sorts) !== 'undefined' && sorts.length > 0) {
			angular.forEach(sorts, function(sort) {
				serverUrl += '&' + sort;
			});
		}
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: criterias
				}
		return $http(request);
	}
	
	// List all request for select.
	this.listAllForSelect = function(){
		var serverUrl = clientwh.serverUrl + '/request/listAllForSelect';
		var request = {
				method: 'GET',
				url: serverUrl
		}
		return $http(request);
	}
	
	// Get by Id For view.
	this.getByIdForView = function(requestId) {
		var serverUrl = clientwh.serverUrl + '/request/getByIdForView/' + requestId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
});

});
