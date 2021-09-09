
/**
 * Route for login
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientmain.prefix + 'login', {
		parent: clientmain.prefix + 'main',
		url: clientmain.contextPath + '/login',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientmain.contextPath + '/js/service/loginService.js', clientmain.contextPath + '/js/controller/loginController.js'], function () {
                    $ocLazyLoad.inject(clientmain.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientmain.contextPath + '/view/login_form.html',
				controller: clientmain.prefix + 'loginController'
			}
		}
	});
	
});
