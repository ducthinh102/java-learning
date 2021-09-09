/**
 * Route for Material Confirm
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientwh.prefix + 'materialconfirm', {
		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/materialconfirm',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientwh.contextPath + '/js/service/materialconfirmService.js', clientwh.contextPath + '/js/controller/materialconfirmController.js'], function () {
                    $ocLazyLoad.inject(clientwh.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientwh.contextPath + '/view/materialconfirm_list.html',
				controller: clientwh.prefix + 'materialconfirmController'
			}
		}
	});
	
});
