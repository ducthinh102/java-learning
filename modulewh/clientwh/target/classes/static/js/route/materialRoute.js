/**
 * Route for Material
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientwh.prefix + 'material', {
		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/material',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([
                	clientwh.contextPath + '/js/service/attachmentService.js',
                	clientwh.contextPath + '/js/controller/attachmentController.js',
                	
                	clientwh.contextPath + '/js/service/commentService.js', 
                	clientwh.contextPath + '/js/controller/commentController.js',
                	
                	
                	
                	clientwh.contextPath + '/js/service/materialsubService.js',
                	clientwh.contextPath + '/js/controller/materialsubController.js',
                	
                	clientwh.contextPath + '/js/service/materialstoreService.js',
                	clientwh.contextPath + '/js/controller/materialstoreController.js',
                	
                	clientwh.contextPath + '/js/service/catalogService.js',
                	clientwh.contextPath + '/js/controller/systemController.js',
                	clientwh.contextPath + '/js/controller/typeController.js',
                	clientwh.contextPath + '/js/controller/brandController.js',
                	clientwh.contextPath + '/js/controller/specController.js',
                	clientwh.contextPath + '/js/controller/originController.js',
                	clientwh.contextPath + '/js/controller/categoryController.js',
                	clientwh.contextPath + '/js/controller/unitController.js',
                	
                	clientwh.contextPath + '/js/service/materialService.js',                	
                	clientwh.contextPath + '/js/controller/materialController.js'], function () {
                    $ocLazyLoad.inject(clientwh.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientwh.contextPath + '/view/material_list.html',
				controller: clientwh.prefix + 'materialController'
			}
		}
	});
	
});
