/**
 * Route for History
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientmain.prefix + 'history', {
		parent: clientmain.prefix + 'main',
		url: clientmain.contextPath + '/history',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientmain.contextPath + '/js/service/historyService.js', clientmain.contextPath + '/js/controller/historyController.js'], function () {
                    $ocLazyLoad.inject(clientmain.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientmain.contextPath + '/view/history_list.html',
				controller: clientmain.prefix + 'historyController'
			}
		}
	});
	
});
