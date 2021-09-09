

/**
 * Service for Userroles
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientmain.prefix + 'userrolesService', function($http, $rootScope) {
	
	// Save.
	this.saveWithUsername = function(username, idroles) {
		var serverUrl = clientmain.authenUrl + '/userroles/saveWithUsername/' + username;
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: {idroles: idroles}
				}
		return $http(request);
	}
	
	// Create.
	this.create = function(userroles) {
		var serverUrl = clientmain.authenUrl + '/userroles/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: userroles
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientmain.authenUrl + '/userroles/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(userrolesId) {
		var serverUrl = clientmain.authenUrl + '/userroles/getById/' + userrolesId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List all roles.
	this.listAllRoles = function() {
		var serverUrl = clientmain.authenUrl + '/userroles/listAllRoles';
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List idroles by username.
	this.listIdRolesByUsername = function(username) {
		var serverUrl = clientmain.authenUrl + '/userroles/listIdRolesByUsername/' + username;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}

});

});
