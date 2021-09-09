
define(['require', 'angular'], function (require, angular) {

	app.aController(clientwh.prefix + 'footerController', function($rootScope, $scope, $translate, $translatePartialLoader) {
		if(typeof(clientwh.translate.footer) === 'undefined' || clientwh.translate.footer.indexOf($translate.use()) < 0) {
			console.log(clientwh.translate.footer);
			if(typeof(clientwh.translate.footer) === 'undefined') {
				clientwh.translate.footer = '';
			}
			clientwh.translate.footer += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/footer');
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
	    	console.log('clientwh_footer_title');
		    $scope.title = $translate.instant('clientwh_footer_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
		$translate.onReady().then(function() {
	    	console.log('footer onReady');
	    	$scope.title = $translate.instant('clientwh_footer_title');
	    });
		// goto.
		$scope.goto = function(state, params) {
			$state.go(clientwh.prefix + state, params);
		}
	});
	
});
