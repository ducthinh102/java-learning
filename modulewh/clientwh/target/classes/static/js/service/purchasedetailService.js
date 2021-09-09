
/**
 * Service for Purchasedetail
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'purchasedetailService', function($http, $rootScope) {
	
	// Create.
	this.create = function(purchasedetail) {
		var serverUrl = clientwh.serverUrl + '/purchasedetail/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: purchasedetail
				}
		return $http(request);
	}
	
	
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientwh.serverUrl + '/purchasedetail/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientwh.serverUrl + '/purchasedetail/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, purchasedetail) {
		var serverUrl = clientwh.serverUrl + '/purchasedetail/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: purchasedetail
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, purchasedetail) {
		var serverUrl = clientwh.serverUrl + '/purchasedetail/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: purchasedetail
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/purchasedetail/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/purchasedetail/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientwh.serverUrl + '/purchasedetail/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(purchasedetailId) {
		var serverUrl = clientwh.serverUrl + '/purchasedetail/getById/' + purchasedetailId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/purchasedetail/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
	this.listWithCriteriasByIdpurchaseAndPage = function(idpurchase, criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/purchasedetail/listWithCriteriasByIdpurchaseAndPage/' + idpurchase + '?page=' + pageNo + '&size=' + pageSize;
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
	
	
	// List of sumAmount for select.
	this.sumAmount = function(idpurchase) {
		var serverUrl = clientwh.serverUrl + '/purchasedetail/sumAmount/' + idpurchase;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get All by Id.
	this.getAllById = function(purchasedetailId) {
		var serverUrl = clientwh.serverUrl + '/purchasedetail/getAllById/' + purchasedetailId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}

	// Get quantity by material.
	this.getQuantityByMaterial = function (idmaterial) {
		var serverUrl = clientwh.serverUrl + '/purchasedetail/getQuantityByMaterial/' + idmaterial;
		var request = {
			method: 'GET',
			url: serverUrl
		}
		return $http(request);
	}
	
	// Create.
	this.createWithSelectDetail = function(purchasedetail) {
		var serverUrl = clientwh.serverUrl + '/purchasedetail/createWithSelectDetail';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: purchasedetail
				}
		return $http(request);
	}
	
	// Update price.
	this.purchaseUpdatePrice = function(purchasedetail) {
		var serverUrl = clientwh.serverUrl + '/purchasedetail/updatePrice/';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: purchasedetail
				}
		return $http(request);
	}
	
	// Update quantity.
	this.purchaseUpdateQuantity = function(purchasedetail) {
		var serverUrl = clientwh.serverUrl + '/purchasedetail/updateQuantity/';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: purchasedetail
				}
		return $http(request);
	}
});

});
