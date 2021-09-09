

/**
 * Service for Materialbaseline
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'materialbaselineService', function($http, $rootScope) {
	
	// Create.
	this.create = function(materialbaseline) {
		var serverUrl = clientwh.serverUrl + '/materialbaseline/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: materialbaseline
				}
		return $http(request);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialbaseline/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialbaseline/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, materialbaseline) {
		var serverUrl = clientwh.serverUrl + '/materialbaseline/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: materialbaseline
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, materialbaseline) {
		var serverUrl = clientwh.serverUrl + '/materialbaseline/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: materialbaseline
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/materialbaseline/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/materialbaseline/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialbaseline/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(materialbaselineId) {
		var serverUrl = clientwh.serverUrl + '/materialbaseline/getById/' + materialbaselineId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/materialbaseline/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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

	// List all purchase for select.
	this.listAllForSelect = function(){
		var serverUrl = clientwh.serverUrl + '/materialbaseline/listAllForSelect';
		var request = {
				method: 'GET',
				url: serverUrl
		}
		return $http(request);
	}
	
});

});
