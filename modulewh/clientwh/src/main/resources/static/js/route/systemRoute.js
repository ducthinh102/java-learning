/**
 * Route for Catalog
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
		.state(clientwh.prefix + 'system', {

			parent: clientwh.prefix + 'main',
			url: clientwh.contextPath + '/system',
			resolve: {

				loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
					var deferred = $q.defer();
					require([clientwh.contextPath + '/js/service/catalogService.js', clientwh.contextPath + '/js/controller/systemController.js'], function () {
						$ocLazyLoad.inject(clientwh.name);
						deferred.resolve();
					});

					return deferred.promise;

				}],
				params: function(){
					return {showBtnClose:false};
				}
			},
			views: {

				'container': {

					templateUrl: clientwh.contextPath + '/view/system_list.html',
					controller: clientwh.prefix + 'systemController'

				}
			}
		});

});
