/**
 * Route for Requestdetail
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientwh.prefix + 'requestdetail', {
		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/requestdetail',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientwh.contextPath + '/js/service/materialService.js',
                	clientwh.contextPath + '/js/service/commentService.js', 
                	clientwh.contextPath + '/js/controller/commentController.js',
                	clientwh.contextPath + '/js/service/attachmentService.js',
                	clientwh.contextPath + '/js/controller/attachmentController.js',
                	clientwh.contextPath + '/js/service/requestdetailService.js', clientwh.contextPath + '/js/controller/requestdetailController.js'], function () {
                    $ocLazyLoad.inject(clientwh.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientwh.contextPath + '/view/requestdetail_list.html',
				controller: clientwh.prefix + 'requestdetailController'
			}
		}
	});
	
});
