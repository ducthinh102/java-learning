

/**
 * Service for Quotationdetail
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'quotationdetailService', function($http, $rootScope) {
	
	// Create.
	this.create = function(quotationdetail) {
		var serverUrl = clientwh.serverUrl + '/quotationdetail/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: quotationdetail
				}
		return $http(request);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientwh.serverUrl + '/quotationdetail/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientwh.serverUrl + '/quotationdetail/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, quotationdetail) {
		var serverUrl = clientwh.serverUrl + '/quotationdetail/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: quotationdetail
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, quotationdetail) {
		var serverUrl = clientwh.serverUrl + '/quotationdetail/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: quotationdetail
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/quotationdetail/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/quotationdetail/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientwh.serverUrl + '/quotationdetail/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(quotationdetailId) {
		var serverUrl = clientwh.serverUrl + '/quotationdetail/getById/' + quotationdetailId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/quotationdetail/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
	this.listWithCriteriasByIdquotationAndPage = function(idquotation, criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/quotationdetail/listWithCriteriasByIdquotationAndPage/' + idquotation + '?page=' + pageNo + '&size=' + pageSize;
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
	this.getAllById = function(quotationdetailId) {
		var serverUrl = clientwh.serverUrl + '/quotationdetail/getAllById/' + quotationdetailId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}

	// Create.
	this.createWithSelectDetail = function(quotationdetail) {
		var serverUrl = clientwh.serverUrl + '/quotationdetail/createWithSelectDetail';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: quotationdetail
				}
		return $http(request);
	}
	
	// Get by Id for view.
	this.getByIdForView = function(quotationdetail) {
		var serverUrl = clientwh.serverUrl + '/quotationdetail/getByIdForView/' + quotationdetail;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update price.
	this.quotationUpdatePrice = function(quotationdetail) {
		var serverUrl = clientwh.serverUrl + '/quotationdetail/updatePrice/';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: quotationdetail
				}
		return $http(request);
	}
	
});

});
