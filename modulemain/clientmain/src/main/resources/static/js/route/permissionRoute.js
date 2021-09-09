/**
 * Route for Permission
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientmain.prefix + 'permission', {
		parent: clientmain.prefix + 'main',
		url: clientmain.contextPath + '/permission',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientmain.contextPath + '/js/service/permissionService.js', clientmain.contextPath + '/js/controller/permissionController.js'], function () {
                    $ocLazyLoad.inject(clientmain.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientmain.contextPath + '/view/permission_list.html',
				controller: clientmain.prefix + 'permissionController'
			}
		}
	});
	
});
