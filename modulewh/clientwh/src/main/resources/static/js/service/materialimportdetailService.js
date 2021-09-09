

/**
 * Service for Materialimportdetail
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'materialimportdetailService', function($http, $rootScope) {
	
	// Create.
	this.create = function(materialimportdetail,idstore) {
		var serverUrl = clientwh.serverUrl + '/materialimportdetail/create/'+idstore;
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: materialimportdetail
				}
		return $http(request);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialimportdetail/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialimportdetail/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, materialimportdetail) {
		var serverUrl = clientwh.serverUrl + '/materialimportdetail/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: materialimportdetail
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, materialimportdetail, idstore) {
		var serverUrl = clientwh.serverUrl + '/materialimportdetail/updateWithLock/' + id + '/'+idstore;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: materialimportdetail
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/materialimportdetail/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version, store) {
		var serverUrl = clientwh.serverUrl + '/materialimportdetail/updateForDeleteWithLock/' + id + '/' + version+'/'+store;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialimportdetail/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(materialimportdetailId) {
		var serverUrl = clientwh.serverUrl + '/materialimportdetail/getById/' + materialimportdetailId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getVersionById = function(materialimportdetailId) {
		var serverUrl = clientwh.serverUrl + '/materialimportdetail/getVerisonById/' + materialimportdetailId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/materialimportdetail/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
	this.listWithCriteriasByIdmaterialimportAndPage = function(idmaterialimport, criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/materialimportdetail/listWithCriteriasByIdmaterialimportAndPage/' + idmaterialimport + '?page=' + pageNo + '&size=' + pageSize;
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
	// Get quantity by material.
	this.getQuantityByMaterial = function (idmaterialimport,idmaterial) {
		var serverUrl = clientwh.serverUrl + '/materialimportdetail/getQuantityByMaterial/' + idmaterialimport+'/'+idmaterial;
		var request = {
			method: 'GET',
			url: serverUrl
		}
		return $http(request);
	}
			
	// Update With Lock.
	this.updateWithLockForImport = function(id, materialimportdetail) {
		var serverUrl = clientwh.serverUrl + '/materialimportdetail/updateWithLockForImport/' + id;
		var request = {
			method: 'PUT',
			url: serverUrl,
			data: materialimportdetail
		}
		return $http(request);
	}
	
	// Get All by Id.
	this.getAllById = function(materialimportdetailId) {
		var serverUrl = clientwh.serverUrl + '/materialimportdetail/getAllById/' + materialimportdetailId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Create.
	this.createWithSelectDetail = function(materialimportdetail,idstore) {
		var serverUrl = clientwh.serverUrl + '/materialimportdetail/createWithSelectDetail/'+idstore;
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: materialimportdetail
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getByIdForView = function(materialimportdetail) {
		var serverUrl = clientwh.serverUrl + '/materialimportdetail/getByIdForView/' + materialimportdetail;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update price.
	this.materialimportUpdatePrice = function(materialimportdetail) {
		var serverUrl = clientwh.serverUrl + '/materialimportdetail/updatePrice/';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: materialimportdetail
				}
		return $http(request);
	}
	
	// Update quantity.
	this.materialimportUpdateQuantity = function(materialimportdetail,idstore) {
		var serverUrl = clientwh.serverUrl + '/materialimportdetail/updateQuantity/'+idstore;
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: materialimportdetail
				}
		return $http(request);
	}
});

});
