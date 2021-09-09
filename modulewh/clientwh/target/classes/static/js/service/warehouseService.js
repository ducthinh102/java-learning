/**
 * Service for Warehouse
 **/

define(['require', 'angular'], function (require, angular) {
	app.aService(clientwh.prefix + 'warehouseService', function ($http, $rootScope) {

		// Create.
		this.create = function (warehouse) {
			var serverUrl = clientwh.serverUrl + '/warehouse/create';
			var request = {
				method: 'POST',
				url: serverUrl,
				data: warehouse
			}
			return $http(request);
		}

		// Update.
		this.update = function (warehouseId, warehouse) {
			var serverUrl = clientwh.serverUrl + '/warehouse/update/' + warehouseId;
			var request = {
				method: 'PUT',
				url: serverUrl,
				data: warehouse
			}
			return $http(request);
		}

		// Delete.
		this.delete = function (warehouseId) {
			var serverUrl = clientwh.serverUrl + '/warehouse/delete/' + warehouseId;
			var request = {
				method: 'DELETE',
				url: serverUrl
			}
			return $http(request);
		}

		// Get by Id.
		this.getById = function (warehouseId) {
			var serverUrl = clientwh.serverUrl + '/warehouse/getById/' + warehouseId;
			var request = {
				method: 'GET',
				url: serverUrl
			}
			return $http(request);
		}

		// List for page and filter.
		this.listWithCriteriasByPage = function (criterias, pageNo, pageSize, sorts) {
		
			var serverUrl = clientwh.serverUrl + '/warehouse/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
		
		//Load warehouse
		
		this.loadWarehouse = function(){
			
			var serverUrl =  clientwh.serverUrl + '/warehouse/listAll/';
			var request = {
					method: 'GET',
					url: serverUrl
				}
			return $http(request);		
		}
	});
});
