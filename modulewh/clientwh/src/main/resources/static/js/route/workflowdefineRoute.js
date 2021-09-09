/**
 * Route for Workflow Define
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientwh.prefix + 'workflowdefine', {
		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/workflowdefine',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientwh.contextPath + '/js/service/workflowdefineService.js', clientwh.contextPath + '/js/controller/workflowdefineController.js'], function () {
                    $ocLazyLoad.inject(clientwh.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientwh.contextPath + '/view/workflowdefine_list.html',
				controller: clientwh.prefix + 'workflowdefineController'
			}
		}
	});
	
});
