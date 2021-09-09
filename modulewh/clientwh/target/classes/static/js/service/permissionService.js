

/**
 * Service for Permission
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'permissionService', function($http, $rootScope) {
	
	// Create.
	this.create = function(permission) {
		var serverUrl = clientwh.serverUrl + '/permission/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: permission
				}
		return $http(request);
	}
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientwh.serverUrl + '/permission/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientwh.serverUrl + '/permission/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	// Update.
	this.update = function(permissionId, permission) {
		var serverUrl = clientwh.serverUrl + '/permission/update/' + permissionId;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: permission
				}
		return $http(request);
	}
	// Update With Lock.
	this.updateWithLock = function(id, permission) {
		var serverUrl = clientwh.serverUrl + '/permission/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: permission
				}
		return $http(request);
	}
	// Delete.
	this.delete = function(permissionId) {
		var serverUrl = clientwh.serverUrl + '/permission/delete/' + permissionId;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(permissionId) {
		var serverUrl = clientwh.serverUrl + '/permission/getById/' + permissionId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/permission/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
	//Get all permission
	this.getAllPermission = function(){
		var serverUrl = clientwh.serverUrl + '/permission/listAll';
		var request = {
				method: 'GET',
				url: serverUrl
		}
		return $http(request);
	}

});

});
