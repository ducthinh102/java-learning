
/**
 * Route for test
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientwh.prefix + 'test', {
		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/test',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientwh.contextPath + '/js/service/testService.js', clientwh.contextPath + '/js/controller/testController.js'], function () {
                    $ocLazyLoad.inject(clientwh.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientwh.contextPath + '/view/test_list.html',
				controller: clientwh.prefix + 'testController'
			}
		}
	});
	
});
