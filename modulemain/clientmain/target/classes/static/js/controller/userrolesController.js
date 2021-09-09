
/**
 * Controller for Userroles
 **/

define(['require', 'angular',
	clientmain.contextPath + '/js/service/userrolesService.js'
	], function (require, angular) {
	app.aController(clientmain.prefix + 'userrolesController', ['$scope', '$q', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', '$mdToast', clientmain.prefix + 'userrolesService', 'params',
		function($scope, $q, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, $mdToast, userrolesService, params) {
		if(typeof(clientmain.translate.userroles) === 'undefined' || clientmain.translate.userroles.indexOf($translate.use()) < 0) {
			if(typeof(clientmain.translate.userroles) === 'undefined') {
				clientmain.translate.userroles = '';
			}
			clientmain.translate.userroles += $translate.use() + ';';
			$translatePartialLoader.addPart(clientmain.contextPath + '/js/common/message/userroles');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientmain_userroles_title');
		    if(!$rootScope.menuActiveTitle){
		    	$rootScope.menuActiveTitle = $translate.instant('clientmain_userroles_title');
		    }
            $scope.options.filterPlaceHolder = $translate.instant('clientmain_home_search');
            $scope.options.title = $translate.instant('clientmain_userroles_title');
            $scope.options.helpMessage = $translate.instant('clientmain_userroles_helpMessage');
            $scope.options.labelAll = $translate.instant('clientmain_userroles_labelAll');
            $scope.options.labelSelected = $translate.instant('clientmain_userroles_labelSelected');
            $scope.options.labelSelectAll = $translate.instant('clientmain_userroles_labelSelectAll');
            $scope.options.labelDeselectAll = $translate.instant('clientmain_userroles_labelDeselectAll');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    $translate.onReady().then(function() {
	    	$translate.refresh();
	    });
		
		// dualmultiselect.
        $scope.options = {
            items: [],
            selectedItems: [],
            displayProperty: 'display',
            valueProperty: 'value'
        };
		
		$scope.userroles = [];
		$scope.username = params.username;
		
		// Call service.
		$scope.listAllForSelect = function() {
			var listAllSelectDeferred = $q.defer();
			$scope.options.items = [];
			$scope.options.selectedItems = [];
			// Call service.
			var listAllRolesDeferred = userrolesService.listAllRoles();
			var listIdRolesByUsernameDeferred = userrolesService.listIdRolesByUsername($scope.username);
			// Response.
			$q.all([listAllRolesDeferred, listIdRolesByUsernameDeferred]).then(
				// Successes.
				function(responses) {
					responses[0].data.forEach(function(item){
						item.display = $translate.instant('clientmain_userroles_' + item.display);
						if(responses[1].data.indexOf(item.value) > -1){
							$scope.options.selectedItems.push(item);
						} else {
							$scope.options.items.push(item);
						}
					});
					// Resolve promise.
					listAllSelectDeferred.resolve(responses);
				},
				// Errors.
				function(responses) {
					$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
					// Reject promise.
					listAllSelectDeferred.reject(responses);
				}
				
			);
			return listAllSelectDeferred.promise;
		}
		$scope.listAllForSelect();
		
		// Close dialog.
		$scope.closeDialog = function(){
			$mdDialog.hide($scope.userroles);
		}
		
		// Save.
		$scope.save = function() {
			var idroles = [];
			$scope.options.selectedItems.forEach(function(item){
				idroles.push(item.value);
			});
			userrolesService.saveWithUsername($scope.username, idroles).then(
				// success.
				function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToast($translate.instant('clientmain_home_saved'));
					} else {
						$scope.showMessageOnToast($translate.instant('clientmain_home_fail'));
					}
				},
				// error.
				function(response) {
					$scope.showMessageOnToast($translate.instant('clientmain_home_error'));
				}
			);// then.
		}

    	
		$scope.showMessageOnToast = function(message){
			$mdToast.show(
					{ template: '<md-toast class="md-toast">' + message + '</md-toast>',
						hideDelay:3000,
						position: 'top right'})
		}
		
		$scope.showMessageOnToastList = function(message){
			$mdToast.show(
					{ template: '<md-toast class="md-toast">' + message + '</md-toast>',
						hideDelay:3000,
						position: 'right'})
		}
		
		$scope.cofirmDeleteToastForm = function(){
			$mdToast.show(
					{  	templateUrl : clientmain.contextPath + '/view/toast.html',
						hideDelay:5000,
						controller  : 'clientwhcategoryController',
						position: 'top right'}).then(function(response){
							if (response) {
								$scope.deleteOnForm();
							}
						});
		}
		
		$scope.OkToast = function() {
	        $mdToast.hide(true);
	    };
		
		$scope.closeToast = function() {
	        $mdToast.hide(false);
	      };
	      
		$scope.cofirmDeleteToastList = function(id,version){
			$mdToast.show(
					{  	templateUrl : clientwh.contextPath + '/view/toast.html',
						hideDelay:5000,
						controller  : 'clientwhcategoryController',
						position: 'right'}).then(function(response){
							if (response) {
								  $scope.delete(id,version);
							}
						});
		}
	
	}]);

});
