
/**
 * Controller for User
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'userController', ['$scope', '$mdToast', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', 'store', clientwh.prefix + 'userService',
		function($scope, $mdToast, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, store, userService) {
		if(typeof(clientwh.translate.user) === 'undefined' || clientwh.translate.user.indexOf($translate.use()) < 0) {
			console.log(clientwh.translate.user);
			if(typeof(clientwh.translate.user) === 'undefined') {
				clientwh.translate.user = '';
			}
			clientwh.translate.user += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/user');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
	    	console.log('clientwh_user_title');
		    $scope.title = $translate.instant('clientwh_user_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    $translate.onReady().then(function() {
	    	console.log('user onReady');
	    	$scope.title = $translate.instant('clientwh_user_title');
	    	$translate.refresh();
	    });

	    // Search.
	    $scope.search = {};
	    
		// Paging.
		$scope.page = {
			pageSize: 9,
			totalElements: 0,
			currentPage: 0
		}
		
		$scope.user = {};
		$scope.isUpdatePassword = false;
		
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
					{  	templateUrl : clientwh.contextPath + '/view/toast.html',
						hideDelay:5000,
						controller  : 'clientwhuserController',
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
						controller  : 'clientwhuserController',
						position: 'right'}).then(function(response){
							if (response) {
								  $scope.delete(id,version);
							}
						});
		}
		
		$scope.closeDialog = function(){
			$mdToast.hide();
			$mdDialog.hide();
		}
		
		// Init for list.
		$scope.initList = function() {
			// Get permission.
			userService.definePermissionByTarget($scope, 'user').then(
				// success.
				function(response) {
					// List data.
					$scope.listWithCriteriasByPage(1);
				},
				// error.
				function(reponse) {
					$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
				}
			);
		}
		
		// Init for form.
		$scope.initForm = function(id) {
			$scope.createNew();
			$scope.user.id = id;
			if($scope.user.id > -1) {
				$scope.getById($scope.user.id);
			}
			$scope.frmDirty = false;
		}
		
		// Show a create screen.
		$scope.showCreate = function() {
			$scope.initForm(-1);
			$scope.isUpdatePassword = false;
			$scope.showDialog();
		}
		
		// Show a form screen.
		$scope.showForm = function(id, isUpdatePassword) {
			$scope.isUpdatePassword = isUpdatePassword;
			$scope.initForm(id);
			$scope.showDialog();
		}

	    // Show edit view.
	    $scope.showDialog = function () {
	    	$mdToast.hide();
	        var htmlUrlTemplate = clientwh.contextPath + '/view/user_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }

	    // Create new.
		$scope.createNew = function() {
    		// Clear thumbnail.
    		if($scope.frmUser && $scope.frmUser.thumbnailImage) {
    			$scope.frmUser.thumbnailImage.val(null);
    		}
			
			$scope.user = { id: -1, enabled: true };
		}
		
		// Reset validate.
		$scope.resetValidate = function() {
			document.getElementById("Inputemail").value = null;  
			// username.
			$scope.frmUser.username.$setPristine();
			$scope.frmUser.username.$setUntouched();
			// password.
			$scope.frmUser.password.$setPristine();
			$scope.frmUser.password.$setUntouched();
			// displayname.
			$scope.frmUser.displayname.$setPristine();
			$scope.frmUser.displayname.$setUntouched();
			// email.
			$scope.frmUser.email.$setPristine();
			$scope.frmUser.email.$setUntouched();
			// enabled.
			$scope.frmUser.enabled.$setPristine();
			$scope.frmUser.enabled.$setUntouched();
			// firstname.
			$scope.frmUser.firstname.$setPristine();
			$scope.frmUser.firstname.$setUntouched();
			// lastname.
			$scope.frmUser.lastname.$setPristine();
			$scope.frmUser.lastname.$setUntouched();
			// frmDirty.
			$scope.frmDirty = false;
		}
		
		// Create on form.
		$scope.createOnForm = function() {
			$scope.createNew();
			$scope.resetValidate();
		}
		
		// Save.
		$scope.save = function() {
			if($scope.frmUser.$invalid) {
				$scope.frmUser.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			$scope.showMessageOnToast($translate.instant('clientwh_home_saving'));
			var result;
			if($scope.user.id > -1) {
				if($scope.isUpdatePassword) {
					result = userService.updatePasswordWithLock($scope.user.id, $scope.user);
				} else {
					result = userService.updateWithLock($scope.user.id, $scope.user);
				}
			} else {
				result = userService.createWithLogin($scope.user);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.user.id > -1) {
						$scope.user.version = response.data;
					} else {
						$scope.user.id = response.data;
						$scope.user.version = 1;
					}
					$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
					$scope.listWithCriteriasByPage(1);
				} else {
					if(!response.data) {
						$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
					}
					else if(response.data.code == clientwh.serverCode.EXISTSCOPE) {
						$scope.frmUser.scope.$invalid = true;
						$scope.showMessageOnToast($translate.instant('clientwh_servercode_' + response.data.code));
					} else if(response.data.code == clientwh.serverCode.VERSIONDIFFERENCE) {
						$scope.showMessageOnToast($translate.instant('clientwh_servercode_' + response.data.code));
					} else {
						$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
					}
				}
			},
			// error.
			function(response) {
				$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
			});
		}
		
		// Delete.
		$scope.delete = function(id){
				userService.delete(id)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToastList($translate.instant('clientwh_home_deleted'));
						$scope.listWithCriteriasByPage(1);
					} else {
						$scope.showMessageOnToastList($translate.instant('clientwh_home_fail'));
					}
				},
				// error.
				function(response) {
					$scope.showMessageOnToastList($translate.instant('clientwh_home_error'));
				});
		}
		
		// Delete with create.
		$scope.deleteOnForm = function() {
				userService.updateForDeleteWithLock($scope.user.id, $scope.user.version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToast($translate.instant('clientwh_home_deleted'));
						$scope.createNew();
						$scope.resetValidate();
						$scope.listWithCriteriasByPage(1);
					} else {
						$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
					}
				},
				// error.
				function(response) {
					$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
				});
		}
		
		// Get by Id.
		$scope.getById = function(id) {
			userService.getById(id)
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.user = data;
					$scope.user.password = "password";
				} else {
					$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
				}
			},
			// error.
			function(response) {
				$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
			});
		}
		
		// List for page and filter.
		$scope.listWithCriteriasByPage = function(pageNo) {
			$scope.page.currentPage = pageNo;
			userService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort())
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.users = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.users = result;
						if(result.length > 0) {
							$scope.page.totalElements = response.data.totalElements;
						}
					}
				} else {
					$scope.showMessageOnToastList($translate.instant('clientwh_home_fail'));
				}
			},
			// error.
			function(response) {
				$scope.showMessageOnToastList($translate.instant('clientwh_home_error'));
			});
		}
		
		// Clear filter.
		$scope.clearFilter = function() {
			$scope.search = {};
			$scope.listWithCriteriasByPage(1);
		}

		/*Extend functions*/
		
		// Sort by.
		$scope.sortBy = function(keyName){
			$scope.sortKey = keyName;
			$scope.reverse = !$scope.reverse;
			// Reload data.
			$scope.listWithCriteriasByPage($scope.page.currentPage);
		}
		
		// Get sort object.
		$scope.getSort = function() {
			var result = [];
			// name.
		    if(typeof($scope.sortKey) !== 'undefined' && $scope.sortKey !== ''){
		    	var order = 'desc';
		    	if($scope.reverse) {
		    		order = 'asc';
		    	}
		    	result.push('sort=' + $scope.sortKey + ',' + order);
		    }
			// return.
			return result;
		}
		
		// Get search object.
		$scope.getSearch = function() {
			var result = [];
		    // scope, content.
		    if(typeof($scope.search.content) !== 'undefined' && $scope.search.content !== ''){
		    	result.push({ key: 'username', operation: 'like', value: $scope.search.content, logic: 'or' });
		    	result.push({ key: 'email', operation: 'like', value: $scope.search.content, logic: 'or' });
		    	result.push({ key: 'email', operation: 'like', value: $scope.search.content, logic: 'or' });
		    	result.push({ key: 'firstname', operation: 'like', value: $scope.search.content, logic: 'or' });
		    	result.push({ key: 'lastname', operation: 'like', value: $scope.search.content, logic: 'or' });
		    }
		    // return.
		    return result;
		}
		
    	
    	////////////////////////////////////////
    	// ThumbnailImage.
    	////////////////////////////////////////

    	// Clear thumbnailImage.
    	$scope.thumbnailImageFileClear = function(event) {
    		$(event.target).val(null);
    	}
    	
    	// Add thumbnailImage.
    	$scope.addThumbnailImage = function(files) {
    		var fileReader = new FileReader();
    		if(files.length < 1) {
    			return;
    		}
    		fileReader.addEventListener("load", function () {
    		    $scope.user.thumbnail = fileReader.result;
    		    $scope.$apply();
    		}, false);
    		fileReader.readAsDataURL(files[0]);
    	}
	
	}]);

});
