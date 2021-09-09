

/**
 * Service for Materialexportdetail
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'materialexportdetailService', function($http, $rootScope) {
	
	// Create.
	this.create = function(materialexportdetail,idstore) {
		var serverUrl = clientwh.serverUrl + '/materialexportdetail/create/'+idstore;
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: materialexportdetail
				}
		return $http(request);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialexportdetail/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialexportdetail/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, materialexportdetail) {
		var serverUrl = clientwh.serverUrl + '/materialexportdetail/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: materialexportdetail
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, materialexportdetail, idstore) {
		var serverUrl = clientwh.serverUrl + '/materialexportdetail/updateWithLock/' + id + '/'+idstore;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: materialexportdetail
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/materialexportdetail/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version, store) {
		var serverUrl = clientwh.serverUrl + '/materialexportdetail/updateForDeleteWithLock/' + id + '/' + version+'/'+store;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialexportdetail/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(materialexportdetailId) {
		var serverUrl = clientwh.serverUrl + '/materialexportdetail/getById/' + materialexportdetailId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getVersionById = function(materialexportdetailId) {
		var serverUrl = clientwh.serverUrl + '/materialexportdetail/getVerisonById/' + materialexportdetailId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	

	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/materialexportdetail/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
	this.listWithCriteriasByIdmaterialexportAndPage = function(idmaterialexport, criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/materialexportdetail/listWithCriteriasByIdmaterialexportAndPage/' + idmaterialexport + '?page=' + pageNo + '&size=' + pageSize;
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
	this.getQuantityByMaterial = function (idmaterialexport,idmaterial) {
		var serverUrl = clientwh.serverUrl + '/materialexportdetail/getQuantityByMaterial/' + idmaterialexport+'/'+idmaterial;
		var request = {
			method: 'GET',
			url: serverUrl
		}
		return $http(request);
	}
			
	// Update With Lock.
	this.updateWithLockForExport = function(id, materialexportdetail) {
		var serverUrl = clientwh.serverUrl + '/materialexportdetail/updateWithLockForExport/' + id;
		var request = {
			method: 'PUT',
			url: serverUrl,
			data: materialexportdetail
		}
		return $http(request);
	}
	
	// Get All by Id.
	this.getAllById = function(materialexportdetailId) {
		var serverUrl = clientwh.serverUrl + '/materialexportdetail/getAllById/' + materialexportdetailId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Create.
	this.createWithSelectDetail = function(materialexportdetail,idstore) {
		var serverUrl = clientwh.serverUrl + '/materialexportdetail/createWithSelectDetail/'+idstore;
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: materialexportdetail
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getByIdForView = function(materialexportdetail) {
		var serverUrl = clientwh.serverUrl + '/materialexportdetail/getByIdForView/' + materialexportdetail;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update quantity.
	this.materialexportUpdateQuantity = function(materialexportdetail,idstore) {
		var serverUrl = clientwh.serverUrl + '/materialexportdetail/updateQuantity/'+idstore;
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: materialexportdetail
				}
		return $http(request);
	}
});

});
