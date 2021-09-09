/**
 * Route for Band
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
		.state(clientwh.prefix + 'brand', {

			parent: clientwh.prefix + 'main',
			url: clientwh.contextPath + '/brand',
			resolve: {

				loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
					var deferred = $q.defer();
					require([clientwh.contextPath + '/js/service/catalogService.js', clientwh.contextPath + '/js/controller/brandController.js'], function () {
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

					templateUrl: clientwh.contextPath + '/view/brand_list.html',
					controller: clientwh.prefix + 'brandController'

				}
			}
		});

});
