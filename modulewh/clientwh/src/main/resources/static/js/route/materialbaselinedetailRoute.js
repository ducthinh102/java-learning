/**
 * Route for Materialbaselinedetail
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientwh.prefix + 'materialbaselinedetail', {
		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/materialbaselinedetail',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientwh.contextPath + '/js/service/materialbaselinedetailService.js', clientwh.contextPath + '/js/controller/materialbaselinedetailController.js'], function () {
                    $ocLazyLoad.inject(clientwh.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientwh.contextPath + '/view/materialbaselinedetail_list.html',
				controller: clientwh.prefix + 'materialbaselinedetailController'
			}
		}
	});
	
});
