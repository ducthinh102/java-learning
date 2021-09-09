/**
 * Route for Supplier
 **/

// import require shim.
define(['require'], function (require) {
	app.aStateProvider
	.state(clientwh.prefix + 'supplier', {
		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/supplier',
		resolve: {
            loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                var deferred = $q.defer();
                require([clientwh.contextPath + '/js/service/supplierService.js', clientwh.contextPath + '/js/controller/supplierController.js'], function () {
                    $ocLazyLoad.inject(clientwh.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }],
	
			params: function(){
				return {showBtnClose:false};
			}
        },
		views: {
			'container': {
				templateUrl: clientwh.contextPath + '/view/supplier_list.html',
				controller: clientwh.prefix + 'supplierController'
			}
		}
	});
	
});
