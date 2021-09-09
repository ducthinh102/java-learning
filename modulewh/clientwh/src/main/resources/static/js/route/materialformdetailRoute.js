/**
 * Route for Materialformdetail
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientwh.prefix + 'materialformdetail', {
		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/materialformdetail',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([
                	clientwh.contextPath + '/js/service/materialService.js',
                	clientwh.contextPath + '/js/service/materialformdetailService.js', 
                	clientwh.contextPath + '/js/controller/materialformdetailController.js'
                	], function () {
                    $ocLazyLoad.inject(clientwh.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientwh.contextPath + '/view/materialformdetail_list.html',
				controller: clientwh.prefix + 'materialformdetailController'
			}
		}
	});
	
});
