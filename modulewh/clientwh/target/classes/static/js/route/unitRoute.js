/**
 * Route for Unit
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
		.state(clientwh.prefix + 'unit', {

			parent: clientwh.prefix + 'main',
			url: clientwh.contextPath + '/unit',
			resolve: {

				loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
					var deferred = $q.defer();
					require([clientwh.contextPath + '/js/service/catalogService.js', clientwh.contextPath + '/js/controller/unitController.js'], function () {
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

					templateUrl: clientwh.contextPath + '/view/unit_list.html',
					controller: clientwh.prefix + 'unitController'

				}
			}
		});

});
