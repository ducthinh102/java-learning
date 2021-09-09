

/**
 * Service for Supplier
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'supplierService', function($http, $rootScope) {
	
	// Create.
	this.create = function(supplier) {
		var serverUrl = clientwh.serverUrl + '/supplier/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: supplier
				}
		return $http(request);
	}
	
	// Update lock.
	this.updateLock = function(id) {
		var serverUrl = clientwh.serverUrl + '/supplier/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id, supplier) {
		var serverUrl = clientwh.serverUrl + '/supplier/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, supplier) {
		var serverUrl = clientwh.serverUrl + '/supplier/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: supplier
				}
		return $http(request);
	}
	
	// Update with lock.
	this.updateWithLock = function(id, supplier) {
		var serverUrl = clientwh.serverUrl + '/supplier/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: supplier
				}
		return $http(request);
	}
	
	
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/supplier/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(supplierId) {
		var serverUrl = clientwh.serverUrl + '/supplier/delete/' + supplierId;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(supplierId) {
		var serverUrl = clientwh.serverUrl + '/supplier/getById/' + supplierId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/supplier/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
	
	// Get list scope for combox
	this.getListScope = function(){
		var serverUrl =  clientwh.serverUrl + '/supplier/listAll';
		var request = {
				method: 'GET',
				url: serverUrl
			}
		return $http(request);
	}
	
	// List all user for select.
	this.listAllForSelect = function(){
		var serverUrl = clientwh.serverUrl + '/supplier/listAllForSelect';
		var request = {
				method: 'GET',
				url: serverUrl
		}
		return $http(request);
	}

});

});
