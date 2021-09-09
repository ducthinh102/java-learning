
/**
 * Route.
 **/
// import require shim.
define(['require'], function (require) {
	app.aStateProvider.state(clientwh.prefix + 'test', {
		parent: clientwh.prefix + 'main',
		url: clientwh.contextPath + '/test',
		resolve: {
			loadRequire : [
					'$ocLazyLoad',
					'$q',
					function($ocLazyLoad, $q) {
                var deferred = $q.defer();
						require([
								clientwh.contextPath
										+ '/js/service/testService.js',
								clientwh.contextPath
										+ '/js/controller/testController.js' ],
								function() {
                    $ocLazyLoad.inject(clientwh.name);
                    deferred.resolve();
                });
                
                return deferred.promise;
            }]
        },
		views: {
			'container': {
				templateUrl : clientwh.contextPath + '/view/test_list.html',
				controller: clientwh.prefix + 'testController'
			}
		}
	});
	
	// Require dependences.
	require([ '/clientwh/js/route/appconfigRoute.js',
			'/clientwh/js/route/notifyRoute.js',
			'/clientwh/js/route/materialformRoute.js',
			'/clientwh/js/route/materialformdetailRoute.js',
			'/clientwh/js/route/materialquantityRoute.js',
			'/clientwh/js/route/permissionAllowRoute.js',
			'/clientwh/js/route/permissionDenyRoute.js',
			'/clientwh/js/route/userRoute.js',
			'/clientwh/js/route/unitRoute.js',
			'/clientwh/js/route/materialRoute.js',
			'/clientwh/js/route/materialsubRoute.js',
			'/clientwh/js/route/warehouseRoute.js',
			'/clientwh/js/route/materialconfirmRoute.js',
			'/clientwh/js/route/workflowdefineRoute.js',
			'/clientwh/js/route/supplierRoute.js',
			'/clientwh/js/route/specRoute.js',
			'/clientwh/js/route/brandRoute.js',
			'/clientwh/js/route/systemRoute.js',
			'/clientwh/js/route/categoryRoute.js',
			'/clientwh/js/route/typeRoute.js',
			'/clientwh/js/route/originRoute.js',
			'/clientwh/js/route/quotationRoute.js',
			'/clientwh/js/route/purchaseRoute.js',
			'/clientwh/js/route/purchasedetailRoute.js',
			'/clientwh/js/route/quotationdetailRoute.js',
			'/clientwh/js/route/quotationRoute.js',
			'/clientwh/js/route/materialbaselineRoute.js',
			'/clientwh/js/route/materialbaselinedetailRoute.js',
			'/clientwh/js/route/storeRoute.js',
			'/clientwh/js/route/materialimportRoute.js',
			'/clientwh/js/route/materialexportRoute.js',
			'/clientwh/js/route/quotationRoute.js',
			'/clientwh/js/route/requestRoute.js',
			'/clientwh/js/route/requestdetailRoute.js',
			'/clientwh/js/route/quotationRoute.js'

		], function () {
    });
});
