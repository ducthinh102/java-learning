/**
 * Route for Calendarextend
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientmain.prefix + 'calendarextend', {
		parent: clientmain.prefix + 'main',
		url: clientmain.contextPath + '/calendarextend',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientmain.contextPath + '/js/service/calendarextendService.js', clientmain.contextPath + '/js/controller/calendarextendController.js'], function () {
                    $ocLazyLoad.inject(clientmain.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientmain.contextPath + '/view/calendarextend_list.html',
				controller: clientmain.prefix + 'calendarextendController'
			}
		}
	});
	
});
