

/**
 * Service for Materialformdetail
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'materialformdetailService', function($http, $rootScope) {
	
	// Create.
	this.create = function(materialformdetail) {
		var serverUrl = clientwh.serverUrl + '/materialformdetail/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: materialformdetail
				}
		return $http(request);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialformdetail/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialformdetail/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, materialformdetail) {
		var serverUrl = clientwh.serverUrl + '/materialformdetail/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: materialformdetail
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, materialformdetail) {
		var serverUrl = clientwh.serverUrl + '/materialformdetail/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: materialformdetail
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/materialformdetail/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/materialformdetail/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialformdetail/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(materialformdetailId) {
		var serverUrl = clientwh.serverUrl + '/materialformdetail/getById/' + materialformdetailId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/materialformdetail/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
	this.listWithCriteriasByIdmaterialformAndPage = function(idmaterialform, criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/materialformdetail/listWithCriteriasByIdmaterialformAndPage/' + idmaterialform + '?page=' + pageNo + '&size=' + pageSize;
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

	// Get All by Id.
	this.getAllById = function(materialformdetailId) {
		var serverUrl = clientwh.serverUrl + '/materialformdetail/getAllById/' + materialformdetailId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	
	// Create.
	this.createWithSelectDetail = function(materialformdetail) {
		var serverUrl = clientwh.serverUrl + '/materialformdetail/createWithSelectDetail/';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: materialformdetail
				}
		return $http(request);
	}
	
	// List of sumAmount for select.
	this.sumAmount = function(idmaterialform) {
		var serverUrl = clientwh.serverUrl + '/materialformdetail/sumAmount/' + idmaterialform;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update price.
	this.materialformUpdatePrice = function(materialformdetail) {
		var serverUrl = clientwh.serverUrl + '/materialformdetail/updatePrice/';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: materialformdetail
				}
		return $http(request);
	}
	
	// Update quantity.
	this.materialformUpdateQuantity = function(materialformdetail) {
		var serverUrl = clientwh.serverUrl + '/materialformdetail/updateQuantity/';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: materialformdetail
				}
		return $http(request);
	}
	
	// Update Reference.
	this.updateRef = function(id, materialformdetail) {
		var serverUrl = clientwh.serverUrl + '/materialformdetail/updateRef/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: materialformdetail
				}
		return $http(request);
	}
	
	// Update Reference With Lock.
	this.updateRefWithLock = function(id, materialformdetail) {
		var serverUrl = clientwh.serverUrl + '/materialformdetail/updateRefWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: materialformdetail
				}
		return $http(request);
	}
	
});

});
