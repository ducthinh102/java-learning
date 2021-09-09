

/**
 * Service for History
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientmain.prefix + 'historyService', function($http, $rootScope) {
	
	// Create.
	this.create = function(history) {
		var serverUrl = clientmain.serverUrl + '/history/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: history
				}
		return $http(request);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientmain.serverUrl + '/history/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientmain.serverUrl + '/history/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, history) {
		var serverUrl = clientmain.serverUrl + '/history/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: history
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, history) {
		var serverUrl = clientmain.serverUrl + '/history/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: history
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientmain.serverUrl + '/history/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientmain.serverUrl + '/history/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientmain.serverUrl + '/history/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(historyId) {
		var serverUrl = clientmain.serverUrl + '/history/getById/' + historyId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientmain.serverUrl + '/history/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
