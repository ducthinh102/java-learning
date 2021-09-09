

/**
 * Service for Quotation
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'quotationService', function($http, $rootScope) {
	
	// Create.
	this.create = function(quotation) {
		var serverUrl = clientwh.serverUrl + '/quotation/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: quotation
				}
		return $http(request);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientwh.serverUrl + '/quotation/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientwh.serverUrl + '/quotation/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, quotation) {
		var serverUrl = clientwh.serverUrl + '/quotation/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: quotation
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, quotation) {
		var serverUrl = clientwh.serverUrl + '/quotation/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: quotation
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/quotation/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/quotation/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientwh.serverUrl + '/quotation/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(quotationId) {
		var serverUrl = clientwh.serverUrl + '/quotation/getById/' + quotationId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/quotation/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
	
	// List all quotation for select.
	this.listAllForSelect = function(){
		var serverUrl = clientwh.serverUrl + '/quotation/listAllForSelect';
		var request = {
				method: 'GET',
				url: serverUrl
		}
		return $http(request);
	}
	
	// Get by Id for view.
	this.getByIdForView = function(quotationId) {
		var serverUrl = clientwh.serverUrl + '/quotation/getByIdForView/' + quotationId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Send mail.
	this.saveCsvFileAndSendMailByIdquotation = function(idquotation) {
		var serverUrl = clientwh.serverUrl + '/quotation/saveCsvFileAndSendMailByIdquotation/' + idquotation;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}

});

});
