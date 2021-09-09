

/**
 * Service for Requestdetail
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'requestdetailService', function($http, $rootScope) {
	
	// Create.
	this.create = function(requestdetail) {
		var serverUrl = clientwh.serverUrl + '/requestdetail/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: requestdetail
				}
		return $http(request);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientwh.serverUrl + '/requestdetail/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientwh.serverUrl + '/requestdetail/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, requestdetail) {
		var serverUrl = clientwh.serverUrl + '/requestdetail/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: requestdetail
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, requestdetail) {
		var serverUrl = clientwh.serverUrl + '/requestdetail/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: requestdetail
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/requestdetail/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/requestdetail/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientwh.serverUrl + '/requestdetail/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(requestdetailId) {
		var serverUrl = clientwh.serverUrl + '/requestdetail/getById/' + requestdetailId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/requestdetail/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
	this.listWithCriteriasByIdrequestAndPage = function(idrequest, criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/requestdetail/listWithCriteriasByIdrequestAndPage/' + idrequest + '?page=' + pageNo + '&size=' + pageSize;
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
	this.listWithCriteriasByIdrequestAndPage = function(idrequest, criterias, pageNo, pageSize) {
		var serverUrl = clientwh.serverUrl + '/requestdetail/listWithCriteriasByIdrequestAndPage/' + idrequest + '?page=' + pageNo + '&size=' + pageSize;
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: criterias
				}
		return $http(request);
	}
	
	// Get All by Id.
	this.getAllById = function(requestdetailId) {
		var serverUrl = clientwh.serverUrl + '/requestdetail/getAllById/' + requestdetailId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Create.
	this.createWithSelectDetail = function(requestdetail) {
		var serverUrl = clientwh.serverUrl + '/requestdetail/createWithSelectDetail';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: requestdetail
				}
		return $http(request);
	}

	// Update quantity.
	this.requestUpdateQuantity = function(requestdetail) {
		var serverUrl = clientwh.serverUrl + '/requestdetail/updateQuantity/';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: requestdetail
				}
		return $http(request);
	}
	// Get by Id For view.
	this.getByIdForView = function(requestId) {
		var serverUrl = clientwh.serverUrl + '/requestdetail/getByIdForView/' + requestId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
});

});
