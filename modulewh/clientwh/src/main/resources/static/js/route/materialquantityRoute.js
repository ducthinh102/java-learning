/**
 * Route for Materialquantity
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientwh.prefix + 'materialquantity', {
		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/materialquantity',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientwh.contextPath + '/js/service/materialquantityService.js', clientwh.contextPath + '/js/controller/materialquantityController.js'], function () {
                    $ocLazyLoad.inject(clientwh.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientwh.contextPath + '/view/materialquantity_list.html',
				controller: clientwh.prefix + 'materialquantityController'
			}
		}
	});
	
});
