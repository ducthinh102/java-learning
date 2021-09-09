/**
 * Route for Attachment
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientmain.prefix + 'attachment', {
		parent: clientmain.prefix + 'main',
		url: clientmain.contextPath + '/attachment',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientmain.contextPath + '/js/service/attachmentService.js', clientmain.contextPath + '/js/controller/attachmentController.js'], function () {
                    $ocLazyLoad.inject(clientmain.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientmain.contextPath + '/view/attachment_list.html',
				controller: clientmain.prefix + 'attachmentController'
			}
		}
	});
	
});
