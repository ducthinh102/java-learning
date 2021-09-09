

/**
 * Service for Permission
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientmain.prefix + 'permissionService', function($http, $rootScope) {
	
	// Create.
	this.create = function(permission) {
		var serverUrl = clientmain.serverUrl + '/permission/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: permission
				}
		return $http(request);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientmain.serverUrl + '/permission/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientmain.serverUrl + '/permission/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, permission) {
		var serverUrl = clientmain.serverUrl + '/permission/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: permission
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, permission) {
		var serverUrl = clientmain.serverUrl + '/permission/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: permission
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientmain.serverUrl + '/permission/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientmain.serverUrl + '/permission/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientmain.serverUrl + '/permission/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(permissionId) {
		var serverUrl = clientmain.serverUrl + '/permission/getById/' + permissionId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientmain.serverUrl + '/permission/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
