/**
 * Route for Appconfig
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientwh.prefix + 'appconfig', {
		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/appconfig',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientwh.contextPath + '/js/service/appconfigService.js', clientwh.contextPath + '/js/controller/appconfigController.js'], function () {
                    $ocLazyLoad.inject(clientwh.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientwh.contextPath + '/view/appconfig_list.html',
				controller: clientwh.prefix + 'appconfigController'
			}
		}
	});
	
});
