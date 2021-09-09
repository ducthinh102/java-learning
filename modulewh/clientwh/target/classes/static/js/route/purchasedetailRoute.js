/**
 * Route for Purchasedetail
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientwh.prefix + 'purchasedetail', {
		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/purchasedetail',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([
                	clientwh.contextPath + '/js/service/materialService.js', clientwh.contextPath + '/js/service/purchasedetailService.js', clientwh.contextPath + '/js/controller/purchasedetailController.js'], function () {
                    $ocLazyLoad.inject(clientwh.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientwh.contextPath + '/view/purchasedetail_list.html',
				controller: clientwh.prefix + 'purchasedetailController'
			}
		}
	});
	
});
