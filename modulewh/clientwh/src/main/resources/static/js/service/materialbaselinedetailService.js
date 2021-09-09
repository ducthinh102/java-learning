

/**
 * Service for Materialbaselinedetail
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'materialbaselinedetailService', function($http, $rootScope) {
	
	// Create.
	this.create = function(materialbaselinedetail) {
		var serverUrl = clientwh.serverUrl + '/materialbaselinedetail/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: materialbaselinedetail
				}
		return $http(request);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialbaselinedetail/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialbaselinedetail/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, materialbaselinedetail) {
		var serverUrl = clientwh.serverUrl + '/materialbaselinedetail/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: materialbaselinedetail
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, materialbaselinedetail) {
		var serverUrl = clientwh.serverUrl + '/materialbaselinedetail/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: materialbaselinedetail
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/materialbaselinedetail/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/materialbaselinedetail/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialbaselinedetail/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(materialbaselinedetailId) {
		var serverUrl = clientwh.serverUrl + '/materialbaselinedetail/getById/' + materialbaselinedetailId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/materialbaselinedetail/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
	this.listWithCriteriasByIdmaterialbaselineAndPage = function(idmaterialbaseline, criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/materialbaselinedetail/listWithCriteriasByIdmaterialbaselineAndPage/' + idmaterialbaseline + '?page=' + pageNo + '&size=' + pageSize;
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
	
	// List all material idref = 0 for select.
	this.listAllForSelect = function(){
		var serverUrl = clientwh.serverUrl + '/material/listAllForSelect';
		var request = {
				method: 'GET',
				url: serverUrl
		}
		return $http(request);
	}
	
	// List all supplier for select.
	this.listAllForSelect = function(){
		var serverUrl = clientwh.serverUrl + '/supplier/listAllForSelect';
		var request = {
				method: 'GET',
				url: serverUrl
		}
		return $http(request);
	}
	
	// List of amount for select.
	this.sumAmount = function(idmaterialbaseline) {
		var serverUrl = clientwh.serverUrl + '/materialbaselinedetail/sumAmount/' + idmaterialbaseline;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}

	// Get All by Id.
	this.getAllById = function(materialbaselineId) {
		var serverUrl = clientwh.serverUrl + '/materialbaselinedetail/getAllById/' + materialbaselineId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Create.
	this.createWithSelectDetail = function(materialbaselinedetail) {
		var serverUrl = clientwh.serverUrl + '/materialbaselinedetail/createWithSelectDetail/';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: materialbaselinedetail
				}
		return $http(request);
	}
	
	// Update price.
	this.materialbaselineUpdatePrice = function(materialbaselinedetail) {
		var serverUrl = clientwh.serverUrl + '/materialbaselinedetail/updatePrice/';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: materialbaselinedetail
				}
		return $http(request);
	}
	
	// Update quantity.
	this.materialbaselineUpdateQuantity = function(materialbaselinedetail) {
		var serverUrl = clientwh.serverUrl + '/materialbaselinedetail/updateQuantity/';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: materialbaselinedetail
				}
		return $http(request);
	}
});

});
