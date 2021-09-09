/**
 * Route for User
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientmain.prefix + 'user', {
		parent: clientmain.prefix + 'main',
		url: clientmain.contextPath + '/user',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientmain.contextPath + '/js/service/userService.js', clientmain.contextPath + '/js/controller/userController.js'], function () {
                    $ocLazyLoad.inject(clientmain.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientmain.contextPath + '/view/user_list.html',
				controller: clientmain.prefix + 'userController'
			}
		}
	});
	
});
