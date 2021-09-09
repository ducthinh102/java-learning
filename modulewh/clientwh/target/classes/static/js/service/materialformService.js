

/**
 * Service for Materialform
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'materialformService', function($http, $rootScope) {
	
	// Create.
	this.create = function(materialform) {
		var serverUrl = clientwh.serverUrl + '/materialform/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: materialform
				}
		return $http(request);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialform/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialform/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, materialform) {
		var serverUrl = clientwh.serverUrl + '/materialform/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: materialform
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, materialform) {
		var serverUrl = clientwh.serverUrl + '/materialform/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: materialform
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/materialform/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/materialform/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientwh.serverUrl + '/materialform/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(materialformId) {
		var serverUrl = clientwh.serverUrl + '/materialform/getById/' + materialformId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/materialform/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
	this.listAllForSelectByScope = function(scope){
		var serverUrl = clientwh.serverUrl + '/materialform/listAllForSelectByScope/'+scope;
		var request = {
				method: 'GET',
				url: serverUrl
		}
		return $http(request);
	}
	
});

});
