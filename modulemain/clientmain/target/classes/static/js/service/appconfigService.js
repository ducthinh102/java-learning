

/**
 * Service for Appconfig
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientmain.prefix + 'appconfigService', function($http, $rootScope) {
	
	// Create.
	this.create = function(appconfig) {
		var serverUrl = clientmain.serverUrl + '/appconfig/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: appconfig
				}
		return $http(request);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientmain.serverUrl + '/appconfig/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientmain.serverUrl + '/appconfig/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, appconfig) {
		var serverUrl = clientmain.serverUrl + '/appconfig/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: appconfig
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, appconfig) {
		var serverUrl = clientmain.serverUrl + '/appconfig/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: appconfig
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientmain.serverUrl + '/appconfig/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientmain.serverUrl + '/appconfig/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientmain.serverUrl + '/appconfig/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(appconfigId) {
		var serverUrl = clientmain.serverUrl + '/appconfig/getById/' + appconfigId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientmain.serverUrl + '/appconfig/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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

});

});
