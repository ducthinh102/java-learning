/**
 * Route for User
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientwh.prefix + 'user', {
		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/user',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientwh.contextPath + '/js/service/userService.js', clientwh.contextPath + '/js/controller/userController.js'], function () {
                    $ocLazyLoad.inject(clientwh.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientwh.contextPath + '/view/user_list.html',
				controller: clientwh.prefix + 'userController'
			}
		}
	});
	
});
