
define(['require', 'angular', clientmain.contextPath + '/js/service/homeService.js'], function (require, angular) {

	app.aController(clientmain.prefix + 'homeController', ['$q', '$rootScope', '$scope', '$state', '$translate', '$translatePartialLoader', '$cookies', clientmain.prefix + 'homeService', function($q, $rootScope, $scope, $state, $translate, $translatePartialLoader, $cookies, homeService) {
		if(typeof(clientmain.translate.home) === 'undefined' || clientmain.translate.home.indexOf($translate.use()) < 0) {
			console.log(clientmain.translate.home);
			if(typeof(clientmain.translate.home) === 'undefined') {
				clientmain.translate.home = '';
			}
			clientmain.translate.home += $translate.use() + ';';
			$translatePartialLoader.addPart(clientmain.contextPath + '/js/common/message/home');
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
	    	console.log('clientmain_header_title');
		    $scope.title = $translate.instant('clientmain_header_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
		$translate.onReady().then(function() {
	    	console.log('header onReady');
	    	$scope.title = $translate.instant('clientmain_header_title');
	    	$translate.refresh();
	    });
	    // goto.
		$scope.goto = function(state, params) {
			$state.go(clientmain.prefix + state, params);
		}
		
		$scope.show = function() {
			alert('/clientmain/homeController');
			$state.go(clientmain.prefix + 'test');
		}

		// The ngView content is reloaded.
		$scope.$on('$viewContentLoaded', function() {
			// hide loader.
			$('.loaderContain').hide();
/*			
			// Load skin.
			if(clientmain.currentSkin === '') {
				clientmain.removeSkin(clientmain.prefix + 'admin');
				clientmain.loadSkin(clientmain.prefix + 'admin');
				clientmain.removeFile(clientmain.contextPath + '/skin/admin/js/admin.js', 'js');
				clientmain.loadFile(clientmain.contextPath + '/skin/admin/js/admin.js', 'js');
			}
*/			
		});
		console.log('/clientmain/homeController');

		if($rootScope.getNotifies === undefined) {
			$rootScope.getNotifies = function() {
				$rootScope.notifies = { totalCount: 0, clientmain: {cout: 0, content: []}, clientbuilding: {cout: 0, content: []}, clientwh: {cout: 0, content: []}, clienthr: {cout: 0, content: []} };
				// servermain
	            var servermainDeferred = $q.defer()
	            if(clientmain.modules.clientmain.isuse) {
	                servermainDeferred = homeService.listWithCriterasByIdreceiverAndPage(clientmain.baseUrl + '/servermain/notify', [], 0, 100, []);
	            } else {
	            	servermainDeferred.resolve();
	            }
	            // serverbuilding
	            var serverbuildingDeferred = $q.defer()
	            if(clientmain.modules.clientbuilding.isuse) {
	                serverbuildingDeferred = homeService.listWithCriterasByIdreceiverAndPage(clientmain.baseUrl + '/serverbuilding/notify', [], 0, 100, []);
	            } else {
	            	serverbuildingDeferred.resolve();
	            }
	            // serverwh
	            var serverwhDeferred = $q.defer()
	            if(clientmain.modules.clientwh.isuse) {
	                serverwhDeferred = homeService.listWithCriterasByIdreceiverAndPage(clientmain.baseUrl + '/serverwh/notify', [], 0, 100, []);
	            } else {
	            	serverwhDeferred.resolve();
	            }
	            // serverhr
	            var serverhrDeferred = $q.defer()
	            if(clientmain.modules.clienthr.isuse) {
	                serverhrDeferred = homeService.listWithCriterasByIdreceiverAndPage(clientmain.baseUrl + '/serverhr/notify', [], 0, 100, []);
	            } else {
	            	serverhrDeferred.resolve();
	            }
	            // Responses.
	            $q.all([servermainDeferred, serverbuildingDeferred, serverwhDeferred, serverhrDeferred]).then(
	            		// success.
	            		function(responses){
	            			// servermain.
	            			if(clientmain.modules.clientmain.isuse && responses[0].status == httpStatus.code.OK) {
	            				$rootScope.notifies.clientmain.count = responses[0].data.totalElements;
								$rootScope.notifies.clientmain.content.push(responses[0].data.content);
								$rootScope.notifies.totalCount += $rootScope.notifies.clientmain.count;
	            			}
	            			// serverbuilding.
	            			if(clientmain.modules.clientbuilding.isuse && responses[1].status == httpStatus.code.OK) {
	            				$rootScope.notifies.clientbuilding.count = responses[1].data.totalElements;
								$rootScope.notifies.clientbuilding.content.push(responses[1].data.content);
								$rootScope.notifies.totalCount += $rootScope.notifies.clientbuilding.count;
	            			}
	            			// serverwh.
	            			if(clientmain.modules.clientwh.isuse && responses[2].status == httpStatus.code.OK) {
	            				$rootScope.notifies.clientwh.count = responses[2].data.totalElements;
								$rootScope.notifies.clientwh.content.push(responses[2].data.content);
								$rootScope.notifies.totalCount += $rootScope.notifies.clientwh.count;
	            			}
							// serverhr.
	            			if(clientmain.modules.clienthr.isuse && responses[3].status == httpStatus.code.OK) {
	            				$rootScope.notifies.clienthr.count = responses[3].data.totalElements;
								$rootScope.notifies.clienthr.content.push(responses[3].data.content);
								$rootScope.notifies.totalCount += $rootScope.notifies.clienthr.count;
	            			}
	            		},
	            		// error.
	            		function(responses){
	            			
	            		}
	            );
			}
            
			// Get notify.
			if($rootScope.loggedIn){
	            $rootScope.getNotifies();
			}
		}


	}]);
	
});
