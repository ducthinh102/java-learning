/**
 * Route for Permission
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientwh.prefix + 'permissionDeny', {
		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/permissionDeny',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientwh.contextPath + '/js/service/userService.js',clientwh.contextPath + '/js/service/permissionService.js', clientwh.contextPath + '/js/controller/permissionDenyController.js'], function () {
                    $ocLazyLoad.inject(clientwh.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientwh.contextPath + '/view/permissionDeny_form.html',
				controller: clientwh.prefix + 'permissionDenyController'
			}
		}
	});
	
});
