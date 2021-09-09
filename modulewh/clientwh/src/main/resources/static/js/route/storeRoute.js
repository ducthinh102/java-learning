
/**
 * Route for Store
 **/

define(['require'], function (require) {
	app.aStateProvider.state(clientwh.prefix + 'store', {

		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/store',
		resolve: {
			loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
				var deferred = $q.defer();
				require([
					
						clientwh.contextPath + '/js/service/materialstoreService.js', 
						clientwh.contextPath + '/js/service/storeService.js', 
						clientwh.contextPath + '/js/controller/storeController.js',
						clientwh.contextPath + '/js/controller/materialstoreController.js'
						], function () {
					$ocLazyLoad.inject(clientwh.name);
					deferred.resolve();
				});
				return deferred.promise;
			}]
		},

		views: {
			'container': {
				templateUrl: clientwh.contextPath + '/view/store_list.html',
				controller: clientwh.prefix + 'storeController'
			}
		}
	});
});