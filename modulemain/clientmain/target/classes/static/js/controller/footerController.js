
define(['require', 'angular'], function (require, angular) {

	app.aController(clientmain.prefix + 'footerController', function($rootScope, $scope, $translate, $translatePartialLoader) {
		if(typeof(clientmain.translate.footer) === 'undefined' || clientmain.translate.footer.indexOf($translate.use()) < 0) {
			console.log(clientmain.translate.footer);
			if(typeof(clientmain.translate.footer) === 'undefined') {
				clientmain.translate.footer = '';
			}
			clientmain.translate.footer += $translate.use() + ';';
			$translatePartialLoader.addPart(clientmain.contextPath + '/js/common/message/footer');
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
	    	console.log('clientmain_footer_title');
		    $scope.title = $translate.instant('clientmain_footer_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
		$translate.onReady().then(function() {
	    	console.log('footer onReady');
	    	$scope.title = $translate.instant('clientmain_footer_title');
	    });
		// goto.
		$scope.goto = function(state, params) {
			$state.go(clientmain.prefix + state, params);
		}
	});
	
});
