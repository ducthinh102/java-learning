

/**
 * Service for User
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'userService', function($q, $http, $rootScope, store) {
	
	// Create with login.
	this.createWithLogin = function(user) {
		var serverUrl = clientwh.serverUrl + '/user/createWithLogin';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 params: {token: store.get('access_token')},
				 data: user
				}
		return $http(request);
	}
	
	// Get user info.
	this.getUserInfoByUsername = function(username) {
		var serverUrl = clientwh.serverUrl + '/user/getUserInfoByUsername/' + username;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Create.
	this.create = function(user) {
		var serverUrl = clientwh.serverUrl + '/user/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: user
				}
		return $http(request);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientwh.serverUrl + '/user/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientwh.serverUrl + '/user/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updatePasswordWithLock = function(id, user) {
		var serverUrl = clientwh.serverUrl + '/user/updatePasswordWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 params: {token: store.get('access_token')},
				 data: { id: user.id, username: user.username, password: user.password }
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(userId, user) {
		var serverUrl = clientwh.serverUrl + '/user/update/' + userId;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: user
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, user) {
		var serverUrl = clientwh.serverUrl + '/user/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 params: {token: store.get('access_token')},
				 data: user
				}
		return $http(request);
	}
	
	// Update With Event.
	this.updateWithEvent = function(id, user) {
		var serverUrl = clientwh.serverUrl + '/user/updateWithEvent/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: user
				}
		return $http(request);
	}
	
	// Update With Lock And Event.
	this.updateWithLockAndEvent = function(id, user) {
		var serverUrl = clientwh.serverUrl + '/user/updateWithLockAndEvent/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: user
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/user/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/user/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(userId) {
		var serverUrl = clientwh.serverUrl + '/user/delete/' + userId;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(userId) {
		var serverUrl = clientwh.serverUrl + '/user/getById/' + userId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/user/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
		var serverUrl = clientwh.serverUrl + '/user/listAll';
		var request = {
				method: 'GET',
				url: serverUrl
		}
		return $http(request);
	}
	
	// Get permission for user menu.
	this.getPermissionForUserMenu = function() {
		var serverUrl = clientwh.serverUrl + '/user/getPermissionForUserMenu';
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get permission by iduser.
	this.getPermissionByIdUser = function() {
		var serverUrl = clientwh.serverUrl + '/user/getPermissionByIdUser';
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get permission by target.
	this.getPermissionByTarget = function(target) {
		var serverUrl = clientwh.serverUrl + '/user/getPermissionByTarget/' + target;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	this.definePermissionByTarget = function(scopeModel, target) {
		var defer = $q.defer();
		// Get permission.
		this.getPermissionByTarget(target).then(
				// success.
				function(response) {
					if(response.data) {
						scopeModel.permission = response.data;
						// Define Create method.
						scopeModel.isPermisCreate = function() {
							return scopeModel.permission.admin || scopeModel.permission.create;
						}
						// Define Action method.
						scopeModel.isAction = function(idcreate, idowner, action, isAllow) {
							// admin.
							if((typeof(scopeModel.permission.admin) != 'undefined') && (scopeModel.permission.admin == isAllow)){
								return true;
							}
							// read.
							if((typeof(scopeModel.permission[action]) != 'undefined') && (scopeModel.permission[action] == isAllow)){
								return true;
							}
							// adminoncreate.
							if((typeof(scopeModel.permission.adminoncreate) != 'undefined') && (scopeModel.permission.adminoncreate == isAllow) && scopeModel.permission.id == idcreate){
								return true;
							}
							// adminonowner.
							if((typeof(scopeModel.permission.adminonowner) != 'undefined') && (scopeModel.permission.adminonowner == isAllow) && scopeModel.permission.id == idowner){
								return true;
							}
							// createbyids.
							if((typeof(scopeModel.permission.createbyids) != 'undefined')){
								var createbyidsSize = scopeModel.permission.createbyids.length;
								for(var i = 0; i < createbyidsSize; i++) {
									var createby = scopeModel.permission.createbyids[i];
									// admin.
									if((typeof(createby.admin) != 'undefined') && (createby.admin == isAllow) && (createby.createbyid == -1 || createby.createbyid == idcreate)){
										return true;
									}
									// read.
									if((typeof(createby[action]) != 'undefined') && (createby[action] == isAllow) && (createby.createbyid == -1 || createby.createbyid == idcreate)){
										return true;
									}
								}
							}
							// ownerbyids.
							if((typeof(scopeModel.permission.ownerbyids) != 'undefined')){
								var ownerbyidsSize = scopeModel.permission.ownerbyids.length;
								for(var i = 0; i < ownerbyidsSize; i++) {
									var ownerby = scopeModel.permission.ownerbyids[i];
									// admin.
									if((typeof(ownerby.admin) != 'undefined') && (ownerby.admin == isAllow) && (ownerby.ownerbyid == -1 || ownerby.ownerbyid == idowner)){
										return true;
									}
									// read.
									if((typeof(ownerby[action]) != 'undefined') && (ownerby[action] == isAllow) && (ownerby.ownerbyid == -1 || ownerby.ownerbyid == idowner)){
										return true;
									}
								}
							}
							// return.
							return false;
						}
						// Define Read method.
						scopeModel.isPermisRead = function(idcreate, idowner) {
							var deny = scopeModel.isAction(idcreate, idowner, 'read', false);
							if(deny){
								return false;
							}
							var allow = scopeModel.isAction(idcreate, idowner, 'read', true);
							// return.
							return allow;
						}
						// Define Update method.
						scopeModel.isPermisUpdate = function(idcreate, idowner) {
							var deny = scopeModel.isAction(idcreate, idowner, 'update', false);
							if(deny){
								return false;
							}
							var allow = scopeModel.isAction(idcreate, idowner, 'update', true);
							// return.
							return allow;
						}
						// Define Delete.
						scopeModel.isPermisDelete = function(idcreate, idowner) {
							var deny = scopeModel.isAction(idcreate, idowner, 'delete', false);
							if(deny){
								return false;
							}
							var allow = scopeModel.isAction(idcreate, idowner, 'delete', true);
							// return.
							return allow;
						}
					}
					// defer resolve.
					defer.resolve(response);
				},
				// error.
				function(reponse) {
					// defer error.
					defer.error(response);
				}
		);
		// return defer.
		return defer.promise;
	}
	
	// List all user for select.
	this.listAllForSelect = function(){
		var serverUrl = clientwh.serverUrl + '/user/listAllForSelect';
		var request = {
				method: 'GET',
				url: serverUrl
		}
		return $http(request);
	}


});

});
