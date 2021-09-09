
/**
 * Service for Material Store
 **/

 define(['require', 'angular'], function (require, angular) {
 	app.aService(clientwh.prefix + 'materialstoreService', function ($http, $rootScope) {

		// Create.
		this.create = function (materialstore) {
			var serverUrl = clientwh.serverUrl + '/materialstore/create';
			var request = {
				method: 'POST',
				url: serverUrl,
				data: materialstore
			}
			return $http(request);
		}

		// Update Lock.
		this.updateLock = function(id) {
			var serverUrl = clientwh.serverUrl + '/materialstore/updateLock/' + id;
			var request = {
				method: 'PUT',
				url: serverUrl
			}
			return $http(request);
		}
		
		// Update Unlock.
		this.updateUnlock = function(id, materialstore) {
			var serverUrl = clientwh.serverUrl + '/materialstore/updateUnlock/' + id;
			var request = {
				method: 'PUT',
				url: serverUrl
			}
			return $http(request);
		}
		
		// Update With Lock.
		this.updateWithLock = function(id, materialstore) {
			var serverUrl = clientwh.serverUrl + '/materialstore/updateWithLock/' + id;
			var request = {
				method: 'PUT',
				url: serverUrl,
				data: materialstore
			}
			return $http(request);
		}
		
		// Update For Delete.
		this.updateForDelete = function(id) {
			var serverUrl = clientwh.serverUrl + '/materialstore/updateForDelete/' + id;
			var request = {
				method: 'PUT',
				url: serverUrl
			}
			return $http(request);
		}
		
		// Update For Delete With Lock.
		this.updateForDeleteWithLock = function(id) {
			var serverUrl = clientwh.serverUrl + '/materialstore/updateForDeleteWithLock/' + id;
			var request = {
				method: 'PUT',
				url: serverUrl
			}
			return $http(request);
		}
		
		// Update.
		this.update = function (id, materialstore) {
			var serverUrl = clientwh.serverUrl + '/materialstore/update/' + id;
			var request = {
				method: 'PUT',
				url: serverUrl,
				data: materialstore
			}
			return $http(request);
		}

		// Delete.
		this.delete = function (id) {
			var serverUrl = clientwh.serverUrl + '/materialstore/delete/' + id;
			var request = {
				method: 'DELETE',
				url: serverUrl
			}
			return $http(request);
		}

		// Get by Id.
		this.getById = function (id) {
			var serverUrl = clientwh.serverUrl + '/materialstore/getById/' + id;
			var request = {
				method: 'GET',
				url: serverUrl
			}
			return $http(request);
		}

		// List for page and filter.
		this.listWithCriteriasByPage = function (criterias, pageNo, pageSize, sorts) {
			var serverUrl = clientwh.serverUrl + '/materialstore/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
		// List for page and filter.
		this.listWithCriteriasByIdStoreAndPage = function(idstore, criterias, pageNo, pageSize) {
			var serverUrl = clientwh.serverUrl + '/materialstore/listWithCriteriasByIdStoreimportAndPage/' + idstore + '?page=' + pageNo + '&size=' + pageSize;
			var request = {
					 method: 'POST',
					 url: serverUrl,
					 data: criterias
					}
			return $http(request);
		}
		// List for page and filter.
		this.listWithCriteriasByIdMaterialAndPage = function(idmaterial, criterias, pageNo, pageSize) {
			var serverUrl = clientwh.serverUrl + '/materialstore/listWithCriteriasByIdMaterialimportAndPage/' + idmaterial + '?page=' + pageNo + '&size=' + pageSize;
			var request = {
					 method: 'POST',
					 url: serverUrl,
					 data: criterias
					}
			return $http(request);
		}
		
		// Update With Lock.
		this.checkQuantityMaterial = function(idstore, materialstore) {
			var serverUrl = clientwh.serverUrl + '/materialstore/checkQuantityMaterial/' + idstore;
			var request = {
				method: 'POST',
				url: serverUrl,
				data: materialstore
			}
			return $http(request);
		}
	});
 });
