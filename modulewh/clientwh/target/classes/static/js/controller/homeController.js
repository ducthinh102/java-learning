
define(['require', 'angular'], function (require, angular) {

	app.aController(clientwh.prefix + 'homeController', ['$q', '$rootScope', '$scope', '$state', '$translate', '$translatePartialLoader', '$cookies', 'store', clientwh.prefix + 'userService',
			function($q, $rootScope, $scope, $state, $translate, $translatePartialLoader, $cookies, store, userService) {
		if(typeof(clientwh.translate.home) === 'undefined' || clientwh.translate.home.indexOf($translate.use()) < 0) {
			console.log(clientwh.translate.home);
			if(typeof(clientwh.translate.home) === 'undefined') {
				clientwh.translate.home = '';
			}
			clientwh.translate.home += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/home');
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/servercode');
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
	    	console.log('clientwh_header_title');
		    $scope.title = $translate.instant('clientwh_header_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
		$translate.onReady().then(function() {
	    	console.log('header onReady');
	    	$scope.title = $translate.instant('clientwh_header_title');
	    	$translate.refresh();
	    });
	    // goto.
		$scope.goto = function(state, params) {
			$state.go(clientwh.prefix + state, params);
		}
		
		$scope.show = function() {
			alert('/clientwh/homeController');
			$state.go(clientwh.prefix + 'test');
		}
		
		// Get permission for user menu.
		$rootScope.getPermissionForUserMenu = function() {
			userService.getPermissionForUserMenu().then(
					// Success.
					function(response) {
						$rootScope.permissions = response.data;
					},
					// Error.
					function(response) {
						$rootScope.permissions = {};
					}
			);
		}

		// The ngView content is reloaded.
		$scope.$on('$viewContentLoaded', function() {
			// hide loader.
			$('.loaderContain').hide();
/*			
			// Load skin.
			if(clientwh.currentSkin === '') {
				clientwh.removeSkin(clientwh.prefix + 'admin');
				clientwh.loadSkin(clientwh.prefix + 'admin');
				clientwh.removeFile(clientwh.contextPath + '/skin/admin/js/admin.js', 'js');
				clientwh.loadFile(clientwh.contextPath + '/skin/admin/js/admin.js', 'js');
			}
*/			
			var userinfo = store.get(clientwh.USER_INFO);
			var index = clientmain.IndexOfByProperty(userinfo.modules, 'name', 'serverwh');
			if(index < 0) {
				userService.getUserInfoByUsername(userinfo.username).then(
						// Success.
						function(response) {
							var userinfo = store.get(clientwh.USER_INFO);
							// Add serverwh module.
							var serverwh = { name: 'serverwh', iduser: response.data.iduser, info: response.data.info}
							userinfo.modules.push(serverwh);
							// Update userinfo.
							store.set(clientwh.USER_INFO, userinfo);
							// Get permission.
							$rootScope.getPermissionForUserMenu();
						},
						// Error.
						function(response) {
						}
				);
			}
			else {
				// Get permission.
				$rootScope.getPermissionForUserMenu();
			}
		});
		
		// Get server info.
		$rootScope.getServerInfo = function() {
			var userinfo = store.get(clientwh.USER_INFO);
			var serverwh = clientwh.GetByProperty(userinfo.modules, 'name', 'serverwh');
			return { iduser: serverwh.iduser, username: userinfo.username };
		}

	}]);
	
});
