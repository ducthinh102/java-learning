/**
 * Route for Materialexportdetail
 **/

define(['require'], function (require) {
	app.aStateProvider
	.state(clientwh.prefix + 'materialexportdetail', {
		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/materialexportdetail',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientwh.contextPath + '/js/service/materialexportdetailService.js', clientwh.contextPath + '/js/controller/materialexportdetailController.js'], function () {
                    $ocLazyLoad.inject(clientwh.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientwh.contextPath + '/view/materialexportdetail_list.html',
				controller: clientwh.prefix + 'materialexportdetailController'
			}
		}
	});
	
});
