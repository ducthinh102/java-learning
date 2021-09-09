

/**
 * Service for User
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientmain.prefix + 'userService', function($http, $rootScope, store) {
	
	// Get user info.
	this.getByUsername = function(username) {
		var serverUrl = clientmain.authenUrl + '/users/getByUsername/' + username;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Create.
	this.create = function(user) {
		var serverUrl = clientmain.authenUrl + '/users/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: user
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(userId, user) {
		var serverUrl = clientmain.authenUrl + '/users/update/' + userId;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: user
				}
		return $http(request);
	}
	
	// Update With Event.
	this.updateWithEvent = function(id, user) {
		var serverUrl = clientmain.authenUrl + '/users/updateWithEvent/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: user
				}
		return $http(request);
	}
	
	// Update Password
	this.updatePassword = function(username, password) {
		var serverUrl = clientmain.authenUrl + '/users/updatePassword/' + username;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: { password: password }
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(username, version) {
		var serverUrl = clientmain.authenUrl + '/users/updateForDelete/' + username + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(userId) {
		var serverUrl = clientmain.authenUrl + '/users/delete/' + userId;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(userId) {
		var serverUrl = clientmain.authenUrl + '/users/getById/' + userId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientmain.authenUrl + '/users/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
	
	this.getAllUser = function(){
		var serverUrl = clientmain.authenUrl + '/users/listAll';
		var request = {
				method: 'GET',
				url: serverUrl
		}
		return $http(request);
	}
	
	// Get permission for user menu.
	this.getPermissionForUserMenu = function() {
		var serverUrl = clientmain.authenUrl + '/users/getPermissionForUserMenu';
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get permission by iduser.
	this.getPermissionByIdUser = function() {
		var serverUrl = clientmain.authenUrl + '/users/getPermissionByIdUser';
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get permission by target.
	this.getPermissionByTarget = function(target) {
		var serverUrl = clientmain.authenUrl + '/users/getPermissionByTarget/' + target;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List all user for select.
	this.listAllForSelect = function(){
		var serverUrl = clientmain.authenUrl + '/users/listAllForSelect';
		var request = {
				method: 'GET',
				url: serverUrl
		}
		return $http(request);
	}


});

});
