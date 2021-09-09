/**
 * Route for Materialbaseline
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientwh.prefix + 'materialbaseline', {
		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/materialbaseline',
		params: {
		    'reftype': null, 
		    'formcopy': null, 
		    'formdetailcopy': null
		  },
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([
                	clientwh.contextPath + '/js/service/userService.js',
                	clientwh.contextPath + '/js/service/quotationService.js',
                	clientwh.contextPath + '/js/service/quotationdetailService.js',
                	clientwh.contextPath + '/js/service/requestService.js',
                	clientwh.contextPath + '/js/service/requestdetailService.js',
                	clientwh.contextPath + '/js/service/materialimportdetailService.js',
                	clientwh.contextPath + '/js/service/materialimportService.js',
                	clientwh.contextPath + '/js/service/materialexportdetailService.js',
                	clientwh.contextPath + '/js/service/materialexportService.js',
                	clientwh.contextPath + '/js/service/purchasedetailService.js',
                	clientwh.contextPath + '/js/service/purchaseService.js',
                	clientwh.contextPath + '/js/service/materialformService.js',
                	clientwh.contextPath + '/js/service/materialformdetailService.js',
                	clientwh.contextPath + '/js/controller/selectfordetailController.js',
                	
                	clientwh.contextPath + '/js/service/materialstoreService.js',
                	clientwh.contextPath + '/js/service/commentService.js', clientwh.contextPath + '/js/controller/commentController.js',
                	clientwh.contextPath + '/js/service/storeService.js',
                	
                	// workflowexecute module
                	clientwh.contextPath + '/js/service/workflowexecuteService.js',
                	clientwh.contextPath + '/js/controller/workflowexecuteController.js',
                	
                	clientwh.contextPath + '/js/service/materialService.js',
                	clientwh.contextPath + '/js/service/supplierService.js',
                	clientwh.contextPath + '/js/service/materialbaselinedetailService.js', clientwh.contextPath + '/js/controller/materialbaselinedetailController.js',
                	clientwh.contextPath + '/js/service/materialbaselineService.js', clientwh.contextPath + '/js/controller/materialbaselineController.js'], function () {
                    $ocLazyLoad.inject(clientwh.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl: clientwh.contextPath + '/view/materialbaseline_list.html',
				controller: clientwh.prefix + 'materialbaselineController'
			}
		}
	});
	
});
