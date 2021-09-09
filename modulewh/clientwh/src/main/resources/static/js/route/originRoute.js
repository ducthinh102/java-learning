/**
 * Route for Origin
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
		.state(clientwh.prefix + 'origin', {

			parent: clientwh.prefix + 'main',
			url: clientwh.contextPath + '/origin',
			resolve: {

				loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
					var deferred = $q.defer();
					require([clientwh.contextPath + '/js/service/catalogService.js', clientwh.contextPath + '/js/controller/originController.js'], function () {
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

					templateUrl: clientwh.contextPath + '/view/origin_list.html',
					controller: clientwh.prefix + 'originController'

				}
			}
		});

});
