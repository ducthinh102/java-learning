

/**
 * Service for Material
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'materialsubService', function($http, $rootScope) {
	
	// Create.
	this.create = function(material) {
		var serverUrl = clientwh.serverUrl + '/material/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: material
				}
		return $http(request);
	}
	
	// Update lock.
	this.updateLock = function(id) {
		var serverUrl = clientwh.serverUrl + '/material/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id, material) {
		var serverUrl = clientwh.serverUrl + '/material/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, material) {
		var serverUrl = clientwh.serverUrl + '/material/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: material
				}
		return $http(request);
	}
	
	// Update with lock.
	this.updateWithLock = function(id, material) {
		var serverUrl = clientwh.serverUrl + '/material/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: material
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/material/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/material/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(materialId) {
		var serverUrl = clientwh.serverUrl + '/material/delete/' + materialId;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(materialId) {
		var serverUrl = clientwh.serverUrl + '/material/getById/' + materialId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/material/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
	
	// List for page and filter.
	this.listWithCriteriasByIdrefAndReftypeAndPage = function(idref, reftype, criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/material/listWithCriteriasByIdrefAndReftypeAndPage/' + idref + '/' + reftype + '?page=' + pageNo + '&size=' + pageSize;
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
	
	//Load material for materialimport
	this.loadallMaterial = function() {
		var serverUrl = clientwh.serverUrl + '/material/listAll/';
		var request = {
				 method: 'GET',
				 url: serverUrl
		}
		return $http(request);
	}
	
	// Get by Id.
	this.getByIdForViewSub = function(materialId) {
		var serverUrl = clientwh.serverUrl + '/material/getByIdForViewSub/' + materialId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
});

});
