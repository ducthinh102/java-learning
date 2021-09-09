
define(['require', 'angular'], function (require, angular) {

	app.aController(clientwh.prefix + 'leftController', function($rootScope, $scope, $state, $translate, $translatePartialLoader, $timeout) {
		if(typeof(clientwh.translate.left) === 'undefined' || clientwh.translate.left.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.left) === 'undefined') {
				clientwh.translate.left = '';
			}
			clientwh.translate.left += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/left');
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_left_title');
		    // menu label.
		    $scope.menu.home.label = $translate.instant('clientwh_home_title');
		    $scope.menu.usermanager.label = $translate.instant('clientwh_left_user_management');
		    $scope.menu.user.label = $translate.instant('clientwh_left_user');
		    $scope.menu.permission.label = $translate.instant('clientwh_left_permission');
		    $scope.menu.permissionAllow.label = $translate.instant('clientwh_left_permission_allow');
		    $scope.menu.permissionDeny.label = $translate.instant('clientwh_left_permission_deny');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});

		// goto.
		$scope.goto = function(state, params) {
			delete $rootScope.menuActiveTitle;
			$state.go(clientwh.prefix + state, params);
		}
		
		
		$scope.$on('$viewContentLoaded', function(event) {
			$scope.sidebarMenu = function (menu) {
				var animationSpeed = 300,
					subMenuSelector = '.sidebar-submenu';

				$(menu).on('click', 'li a', function (e) {
					var $this = $(this);
					var checkElement = $this.next();

					if (checkElement.is(subMenuSelector) && checkElement.is(':visible')) {
						checkElement.slideUp(animationSpeed, function () {
							checkElement.removeClass('menu-open');
						});
						checkElement.parent("li").removeClass("active");
					}

					//If the menu is not visible
					else if ((checkElement.is(subMenuSelector)) && (!checkElement.is(':visible'))) {
						//Get the parent menu
						var parent = $this.parents('ul').first();
						//Close all open menus within the parent
						var ul = parent.find('ul:visible').slideUp(animationSpeed);
						//Remove the menu-open class from the parent
						ul.removeClass('menu-open');
						//Get the parent li
						var parent_li = $this.parent("li");

						//Open the target menu and add the menu-open class
						checkElement.slideDown(animationSpeed, function () {
							//Add the class active to the parent li
							checkElement.addClass('menu-open');
							parent.find('li.active').removeClass('active');
							parent_li.addClass('active');
						});
					}
					//if this isn't a link, prevent the page from being redirected
					if (checkElement.is(subMenuSelector)) {
						e.preventDefault();
					}
				});
			}
			
			$scope.sidebarMenu($('.sidebar-menu'));
			
			// spliter.
			$('#widget').width('100%').height('100%').split({
				orientation: 'vertical',
				limit: 10,
				position: '20%',
				percent: true
			});
			
		});
		
		// menu
		$scope.menu = {
				home: {},
				usermanager: {},
				user: {},
				permission: {},
				permissionAllow: {},
				permissionDeny: {}
		};
		// menu search.
		$scope.menuSearch = function(text){
			angular.forEach($scope.menu, function(value, key){
				$scope.menu[key].hide = $scope.menu[key].label.toLowerCase().indexOf(text.toLowerCase()) < 0;
			});
		}
	});
	
});
