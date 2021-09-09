/**
 * Route for Notify
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientmain.prefix + 'notify', {
		parent: clientmain.prefix + 'main',
		url: clientmain.contextPath + '/notify',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientmain.contextPath + '/js/service/notifyService.js', clientmain.contextPath + '/js/controller/notifyController.js'], function () {
                    $ocLazyLoad.inject(clientmain.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientmain.contextPath + '/view/notify_list.html',
				controller: clientmain.prefix + 'notifyController'
			}
		}
	});
	
});
