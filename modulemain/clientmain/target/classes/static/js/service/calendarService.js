

/**
 * Service for Calendar
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientmain.prefix + 'calendarService', function($http, $rootScope) {
	
	// Create.
	this.create = function(calendar) {
		var serverUrl = clientmain.serverUrl + '/calendar/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: calendar
				}
		return $http(request);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientmain.serverUrl + '/calendar/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientmain.serverUrl + '/calendar/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, calendar) {
		var serverUrl = clientmain.serverUrl + '/calendar/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: calendar
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, calendar) {
		var serverUrl = clientmain.serverUrl + '/calendar/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: calendar
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientmain.serverUrl + '/calendar/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientmain.serverUrl + '/calendar/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientmain.serverUrl + '/calendar/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(calendarId) {
		var serverUrl = clientmain.serverUrl + '/calendar/getById/' + calendarId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List all user for select.
	this.listForSelect = function(){
		var serverUrl = clientmain.serverUrl + '/calendar/listForSelect';
		var request = {
				method: 'GET',
				url: serverUrl
		}
		return $http(request);
	}
	
	// Get by Id.
	this.getWithCalendarextendById = function(calendarId) {
		var serverUrl = clientmain.serverUrl + '/calendar/getWithCalendarextendById/' + calendarId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientmain.serverUrl + '/calendar/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
