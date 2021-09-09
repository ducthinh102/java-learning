/**
 * Route for Calendar
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientmain.prefix + 'calendar', {
		parent: clientmain.prefix + 'main',
		url: clientmain.contextPath + '/calendar',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([
                	clientmain.contextPath + '/js/service/calendarextendService.js', clientmain.contextPath + '/js/controller/calendarextendController.js',
                	clientmain.contextPath + '/js/service/calendarService.js', clientmain.contextPath + '/js/controller/calendarController.js'], function () {
                    $ocLazyLoad.inject(clientmain.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientmain.contextPath + '/view/calendar_list.html',
				controller: clientmain.prefix + 'calendarController'
			}
		}
	});
	
});
