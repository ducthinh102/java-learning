/**
 * Route for Notify
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientwh.prefix + 'notify', {
		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/notify',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientwh.contextPath + '/js/service/notifyService.js', clientwh.contextPath + '/js/controller/notifyController.js'], function () {
                    $ocLazyLoad.inject(clientwh.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientwh.contextPath + '/view/notify_list.html',
				controller: clientwh.prefix + 'notifyController'
			}
		}
	});
	
});
