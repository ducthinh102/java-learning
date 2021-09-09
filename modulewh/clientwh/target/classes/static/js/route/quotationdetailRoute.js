/**
 * Route for Quotationdetail
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientwh.prefix + 'quotationdetail', {
		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/quotationdetail',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientwh.contextPath + '/js/service/quotationdetailService.js',
                	clientwh.contextPath + '/js/service/catalogService.js',
                	clientwh.contextPath + '/js/controller/quotationdetailController.js'], function () {
                    $ocLazyLoad.inject(clientwh.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientwh.contextPath + '/view/quotationdetail_list.html',
				controller: clientwh.prefix + 'quotationdetailController'
			}
		}
	});
	
});
