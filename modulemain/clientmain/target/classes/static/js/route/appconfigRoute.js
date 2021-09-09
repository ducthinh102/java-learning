/**
 * Route for Appconfig
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientmain.prefix + 'appconfig', {
		parent: clientmain.prefix + 'main',
		url: clientmain.contextPath + '/appconfig',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientmain.contextPath + '/js/service/appconfigService.js', clientmain.contextPath + '/js/controller/appconfigController.js'], function () {
                    $ocLazyLoad.inject(clientmain.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientmain.contextPath + '/view/appconfig_list.html',
				controller: clientmain.prefix + 'appconfigController'
			}
		}
	});
	
});
