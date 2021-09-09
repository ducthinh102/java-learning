/**
 * Route for Ware house
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider.state(clientwh.prefix + 'warehouse', {

		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/warehouse',
		resolve: {

			loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {

				var deferred = $q.defer();
				require([ clientwh.contextPath + '/js/service/warehouseService.js', clientwh.contextPath + '/js/controller/warehouseController.js'], function () {
					$ocLazyLoad.inject(clientwh.name);
					deferred.resolve();

				});

				return deferred.promise;

			}]
		},

		views: {
			'container': {

				templateUrl: clientwh.contextPath + '/view/warehouse_list.html',
				controller: clientwh.prefix + 'warehouseController'

			}
		}
	});

});
