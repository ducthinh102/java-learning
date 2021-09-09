/**
 * Route for Comment
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientwh.prefix + 'comment', {
		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/comment',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientwh.contextPath + '/js/service/commentService.js', clientwh.contextPath + '/js/controller/commentController.js'], function () {
                    $ocLazyLoad.inject(clientwh.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientwh.contextPath + '/view/comment_list.html',
				controller: clientwh.prefix + 'commentController'
			}
		}
	});
	
});
