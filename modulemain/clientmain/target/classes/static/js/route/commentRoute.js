/**
 * Route for Comment
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientmain.prefix + 'comment', {
		parent: clientmain.prefix + 'main',
		url: clientmain.contextPath + '/comment',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientmain.contextPath + '/js/service/commentService.js', clientmain.contextPath + '/js/controller/commentController.js'], function () {
                    $ocLazyLoad.inject(clientmain.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientmain.contextPath + '/view/comment_list.html',
				controller: clientmain.prefix + 'commentController'
			}
		}
	});
	
});
