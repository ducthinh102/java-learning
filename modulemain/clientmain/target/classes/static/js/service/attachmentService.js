

/**
 * Service for Attachment
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientmain.prefix + 'attachmentService', function($http, $rootScope) {
	
	// Create.
	this.create = function(attachment) {
		var serverUrl = clientmain.serverUrl + '/attachment/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: attachment
				}
		return $http(request);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientmain.serverUrl + '/attachment/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientmain.serverUrl + '/attachment/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, attachment) {
		var serverUrl = clientmain.serverUrl + '/attachment/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: attachment
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, attachment) {
		var serverUrl = clientmain.serverUrl + '/attachment/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: attachment
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientmain.serverUrl + '/attachment/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientmain.serverUrl + '/attachment/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientmain.serverUrl + '/attachment/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(attachmentId) {
		var serverUrl = clientmain.serverUrl + '/attachment/getById/' + attachmentId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientmain.serverUrl + '/attachment/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
