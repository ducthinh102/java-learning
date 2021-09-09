/**
 * Route for Materialimportdetail
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientwh.prefix + 'materialimportdetail', {
		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/materialimportdetail',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientwh.contextPath + '/js/service/materialimportdetailService.js', clientwh.contextPath + '/js/controller/materialimportdetailController.js'], function () {
                    $ocLazyLoad.inject(clientwh.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientwh.contextPath + '/view/materialimportdetail_list.html',
				controller: clientwh.prefix + 'materialimportdetailController'
			}
		}
	});
	
});
