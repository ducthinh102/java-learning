/**
 * Route for Catalog
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
		.state(clientwh.prefix + 'category', {

			parent: clientwh.prefix + 'main',
			url: clientwh.contextPath + '/category',
			resolve: {

				loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
					var deferred = $q.defer();
					require([clientwh.contextPath + '/js/service/catalogService.js', clientwh.contextPath + '/js/controller/categoryController.js'], function () {
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

					templateUrl: clientwh.contextPath + '/view/category_list.html',
					controller: clientwh.prefix + 'categoryController'

				}
			}
		});

});
