

/**
 * Service for Materialexport
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'materialexportService', function($http, $rootScope) {
	
	// Create.
	this.create = function(materialexport) {
		var serverUrl = clientwh.serverUrl + '/materialexport/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: materialexport
				}
		return $http(request);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialexport/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialexport/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, materialexport) {
		var serverUrl = clientwh.serverUrl + '/materialexport/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: materialexport
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, materialexport) {
		var serverUrl = clientwh.serverUrl + '/materialexport/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: materialexport
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/materialexport/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/materialexport/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialexport/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(materialexportId) {
		var serverUrl = clientwh.serverUrl + '/materialexport/getById/' + materialexportId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/materialexport/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
		var serverUrl = clientwh.serverUrl + '/materialexport/listAllForSelect';
		var request = {
				method: 'GET',
				url: serverUrl
		}
		return $http(request);
	}

	// Get by Id.
	this.getByIdForView = function(materialexportId) {
		var serverUrl = clientwh.serverUrl + '/materialexport/getByIdForView/' + materialexportId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
});

});
