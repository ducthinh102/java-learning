

/**
 * Service for Materialimport
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'materialimportService', function($http, $rootScope) {
	
	// Create.
	this.create = function(materialimport) {
		var serverUrl = clientwh.serverUrl + '/materialimport/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: materialimport
				}
		return $http(request);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialimport/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialimport/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, materialimport) {
		var serverUrl = clientwh.serverUrl + '/materialimport/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: materialimport
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, materialimport) {
		var serverUrl = clientwh.serverUrl + '/materialimport/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: materialimport
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/materialimport/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/materialimport/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialimport/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(materialimportId) {
		var serverUrl = clientwh.serverUrl + '/materialimport/getById/' + materialimportId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/materialimport/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
	
	// List all user for select.
	this.listAllForSelect = function(){
		var serverUrl = clientwh.serverUrl + '/materialimport/listAllForSelect';
		var request = {
				method: 'GET',
				url: serverUrl
		}
		return $http(request);
	}

	// Get by Id.
	this.getByIdForView = function(materialimportId) {
		var serverUrl = clientwh.serverUrl + '/materialimport/getByIdForView/' + materialimportId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
});

});
