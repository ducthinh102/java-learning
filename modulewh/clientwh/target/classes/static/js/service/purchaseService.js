

/**
 * Service for Purchase
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'purchaseService', function($http, $rootScope) {
	
	// Create.
	this.create = function(purchase) {
		var serverUrl = clientwh.serverUrl + '/purchase/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: purchase
				}
		return $http(request);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientwh.serverUrl + '/purchase/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientwh.serverUrl + '/purchase/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, purchase) {
		var serverUrl = clientwh.serverUrl + '/purchase/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: purchase
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, purchase) {
		var serverUrl = clientwh.serverUrl + '/purchase/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: purchase
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/purchase/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/purchase/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientwh.serverUrl + '/purchase/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(purchaseId) {
		var serverUrl = clientwh.serverUrl + '/purchase/getById/' + purchaseId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/purchase/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
	
	// List of supplier for select.
	this.listSupplierForSelect = function() {
		var serverUrl = clientwh.serverUrl + '/supplier/listSupplierForSelect';
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}

	// List of receiver for select.
	this.listReceiverForSelect = function() {
		var serverUrl = clientwh.serverUrl + '/receiver/listReceiverForSelect';
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List all purchase for select.
	this.listAllForSelect = function(){
		var serverUrl = clientwh.serverUrl + '/purchase/listAllForSelect';
		var request = {
				method: 'GET',
				url: serverUrl
		}
		return $http(request);
	}
	
});

});
