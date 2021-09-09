

/**
 * Service for Material Confirm
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'materialconfirmService', function($http, $rootScope) {
	
	//////////////////////////////////////
	// Material list
	/////////////////////////////////////

	// List for page and filter
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/materialconfirm/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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

	// Update list of unconfirmed materials
	this.updateUnconfirmedItems = function(ids) {
		var serverUrl = clientwh.serverUrl + '/materialconfirm/updateUnconfirmedItems/';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: ids
				}
		return $http(request);
	}
	
	
	//////////////////////////////////////
	// Confirm a material form
	/////////////////////////////////////

	// Get by Id
	this.getById = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialconfirm/getById/' + id;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get and lock item by Id
	this.getAndLockById = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialconfirm/getAndLockById/' + id;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}

	// Get a confirmed material by code
	this.getConfirmedItemByCode = function(code) {
		var serverUrl = clientwh.serverUrl + '/materialconfirm/getConfirmedItemByCode/';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: code
				}
		return $http(request);
	}

	// List confirmed materials by name
	this.search = function(name) {
		var serverUrl = clientwh.serverUrl + '/materialconfirm/listConfirmedItemsByName/';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: name
				}
		return $http(request);
	}

	// Update a material
	this.update = function(id, item) {
		var serverUrl = clientwh.serverUrl + '/materialconfirm/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: item
				}
		return $http(request);
	}
	
});

});
