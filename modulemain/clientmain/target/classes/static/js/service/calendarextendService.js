

/**
 * Service for Calendarextend
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientmain.prefix + 'calendarextendService', function($http, $rootScope) {
	
	// Create.
	this.create = function(calendarextend) {
		var serverUrl = clientmain.serverUrl + '/calendarextend/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: calendarextend
				}
		return $http(request);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientmain.serverUrl + '/calendarextend/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientmain.serverUrl + '/calendarextend/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, calendarextend) {
		var serverUrl = clientmain.serverUrl + '/calendarextend/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: calendarextend
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, calendarextend) {
		var serverUrl = clientmain.serverUrl + '/calendarextend/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: calendarextend
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientmain.serverUrl + '/calendarextend/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientmain.serverUrl + '/calendarextend/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientmain.serverUrl + '/calendarextend/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(calendarextendId) {
		var serverUrl = clientmain.serverUrl + '/calendarextend/getById/' + calendarextendId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientmain.serverUrl + '/calendarextend/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
	
	// List for page and filter.
	this.listWithCriteriasByIdcalendarAndPage = function(idcalendar, criterias, pageNo, pageSize) {
		var serverUrl = clientmain.serverUrl + '/calendarextend/listWithCriteriasByIdcalendarAndPage/' + idcalendar + '?page=' + pageNo + '&size=' + pageSize;
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: criterias
				}
		return $http(request);
	}

});

});
