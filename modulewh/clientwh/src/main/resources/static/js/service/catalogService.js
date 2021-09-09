/**
 * Service for Catalog
 **/

define(['require', 'angular'], function (require, angular) {
	app.aService(clientwh.prefix + 'catalogService', function ($http, $rootScope) {

		// Create.
		this.create = function (catalog) {
			var serverUrl = clientwh.serverUrl + '/catalog/create';
			var request = {
				method: 'POST',
				url: serverUrl,
				data: catalog
			}
			return $http(request);
		}

		// Update lock.
		this.updateLock = function(id) {
			var serverUrl = clientwh.serverUrl + '/catalog/update/' + id;
			var request = {
					 method: 'PUT',
					 url: serverUrl
					}
			return $http(request);
		}
		
		// Update Unlock.
		this.updateUnlock = function(id, catalog) {
			var serverUrl = clientwh.serverUrl + '/catalog/updateUnlock/' + id;
			var request = {
					 method: 'PUT',
					 url: serverUrl
					}
			return $http(request);
		}
		
		// Update.
		this.update = function(id, catalog) {
			var serverUrl = clientwh.serverUrl + '/catalog/updateLock/' + id;
			var request = {
					 method: 'PUT',
					 url: serverUrl,
					 data: catalog
					}
			return $http(request);
		}
		
		// Update with lock.
		this.updateWithLock = function(id, catalog) {
			var serverUrl = clientwh.serverUrl + '/catalog/updateWithLock/' + id;
			var request = {
					 method: 'PUT',
					 url: serverUrl,
					 data: catalog
					}
			return $http(request);
		}
		// Update For Delete.
		this.updateForDelete = function(id, version) {
			var serverUrl = clientwh.serverUrl + '/catalog/updateForDelete/' + id + '/' + version;
			var request = {
					 method: 'PUT',
					 url: serverUrl
					}
			return $http(request);
		}
		
		// Update For Delete With Lock.
		this.updateForDeleteWithLock = function(id, version) {
			var serverUrl = clientwh.serverUrl + '/catalog/updateForDeleteWithLock/' + id + '/' + version;
			var request = {
					 method: 'PUT',
					 url: serverUrl
					}
			return $http(request);
		}
		// Delete.
		this.delete = function (catalogId) {
			var serverUrl = clientwh.serverUrl + '/catalog/delete/' + catalogId;
			var request = {
				method: 'DELETE',
				url: serverUrl
			}
			return $http(request);
		}

		// Get by Id.
		this.getById = function (catalogId,scope) {
			var serverUrl = clientwh.serverUrl + '/catalog/getById/' + catalogId+'/'+scope;
			var request = {
				method: 'GET',
				url: serverUrl
			}
			return $http(request);
		}

		// List for page and filter.
		this.listWithCriteriasByPage = function (criterias, pageNo, pageSize, sorts,scope) {
			var serverUrl = clientwh.serverUrl + '/catalog/listWithCriteriasByPage/'+scope+'?' + 'page=' + pageNo + '&size=' + pageSize;
			if (typeof (sorts) !== 'undefined' && sorts.length > 0) {
				angular.forEach(sorts, function (sort) {
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
		
		// Get list scope for combox
		this.getListScope = function(scope){
			
			var serverUrl =  clientwh.serverUrl + '/catalog/getList/'+scope;
			var request = {
					method: 'GET',
					url: serverUrl
				}
			return $http(request);
			
		}
		
		// List all user for select.
		this.listAllForSelectByScope = function(scope){
			var serverUrl = clientwh.serverUrl + '/catalog/listAllForSelect/'+scope;
			var request = {
					method: 'GET',
					url: serverUrl
			}
			return $http(request);
		}
		
	});
});
