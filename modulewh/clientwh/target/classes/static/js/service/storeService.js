
/**
 * Service for Store
 **/

 define(['require', 'angular'], function (require, angular) {
 	app.aService(clientwh.prefix + 'storeService', function ($http, $rootScope) {

		// Create.
		this.create = function (store) {
			var serverUrl = clientwh.serverUrl + '/store/create';
			var request = {
				method: 'POST',
				url: serverUrl,
				data: store
			}
			return $http(request);
		}

		// Update Lock.
		this.updateLock = function(id) {
			var serverUrl = clientwh.serverUrl + '/store/updateLock/' + id;
			var request = {
				method: 'PUT',
				url: serverUrl
			}
			return $http(request);
		}
		
		// Update Unlock.
		this.updateUnlock = function(id, store) {
			var serverUrl = clientwh.serverUrl + '/store/updateUnlock/' + id;
			var request = {
				method: 'PUT',
				url: serverUrl
			}
			return $http(request);
		}
		
		// Update With Lock.
		this.updateWithLock = function(id, store) {
			var serverUrl = clientwh.serverUrl + '/store/updateWithLock/' + id;
			var request = {
				method: 'PUT',
				url: serverUrl,
				data: store
			}
			return $http(request);
		}
		
		// Update For Delete.
		this.updateForDelete = function(id, version) {
			var serverUrl = clientwh.serverUrl + '/store/updateForDelete/' + id + "/" + version;
			var request = {
				method: 'PUT',
				url: serverUrl
			}
			return $http(request);
		}
		
		// Update For Delete With Lock.
		this.updateForDeleteWithLock = function(id, version) {
			var serverUrl = clientwh.serverUrl + '/store/updateForDeleteWithLock/' + id + "/" + version;
			var request = {
				method: 'PUT',
				url: serverUrl
			}
			return $http(request);
		}
		
		// Update.
		this.update = function (id, store) {
			var serverUrl = clientwh.serverUrl + '/store/update/' + id;
			var request = {
				method: 'PUT',
				url: serverUrl,
				data: store
			}
			return $http(request);
		}

		// Delete.
		this.delete = function (id) {
			var serverUrl = clientwh.serverUrl + '/store/delete/' + id;
			var request = {
				method: 'DELETE',
				url: serverUrl
			}
			return $http(request);
		}

		// Get by Id.
		this.getById = function (id) {
			var serverUrl = clientwh.serverUrl + '/store/getById/' + id;
			var request = {
				method: 'GET',
				url: serverUrl
			}
			return $http(request);
		}

		// List for page and filter.
		this.listWithCriteriasByPage = function (criterias, pageNo, pageSize, sorts) {
			var serverUrl = clientwh.serverUrl + '/store/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
		
		//Load all store for combo box
		this.loadallStore = function(){
			var serverUrl =  clientwh.serverUrl + '/store/listAll';
			var request = {
					method: 'GET',
					url: serverUrl
			}
			return $http(request);
		}
		
		// List all user for select.
		this.listAllForSelect = function(){
			var serverUrl = clientwh.serverUrl + '/store/listAllForSelect';
			var request = {
					method: 'GET',
					url: serverUrl
			}
			return $http(request);
		}
	});
 });
