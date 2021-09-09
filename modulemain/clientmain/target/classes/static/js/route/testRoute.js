
/**
 * Route for test
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientmain.prefix + 'test', {
		parent: clientmain.prefix + 'main',
		url: clientmain.contextPath + '/test',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientmain.contextPath + '/js/service/testService.js', clientmain.contextPath + '/js/controller/testController.js'], function () {
                    $ocLazyLoad.inject(clientmain.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientmain.contextPath + '/view/test_list.html',
				controller: clientmain.prefix + 'testController'
			}
		}
	});
	
});
