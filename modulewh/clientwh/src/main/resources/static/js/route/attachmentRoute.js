/**
 * Route for Attachment
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientwh.prefix + 'attachment', {
		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/attachment',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientwh.contextPath + '/js/service/attachmentService.js',
                	clientwh.contextPath + '/js/service/commentService.js', 
                	clientwh.contextPath + '/js/controller/commentController.js',
                	clientwh.contextPath + '/js/controller/attachmentController.js'], function () {
                    $ocLazyLoad.inject(clientwh.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientwh.contextPath + '/view/attachment_list.html',
				controller: clientwh.prefix + 'attachmentController'
			}
		}
	});
	
});
