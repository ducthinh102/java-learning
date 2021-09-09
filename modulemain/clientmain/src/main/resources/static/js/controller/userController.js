
/**
 * Controller for User
 **/

define(['require', 'angular',
	clientmain.contextPath + '/js/controller/userrolesController.js'
	], function (require, angular) {
	app.aController(clientmain.prefix + 'userController', ['$scope', '$q', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', '$mdToast', 'store', clientmain.prefix + 'userService',
		function($scope, $q, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, $mdToast, store, userService) {
		if(typeof(clientmain.translate.user) === 'undefined' || clientmain.translate.user.indexOf($translate.use()) < 0) {
			if(typeof(clientmain.translate.user) === 'undefined') {
				clientmain.translate.user = '';
			}
			clientmain.translate.user += $translate.use() + ';';
			$translatePartialLoader.addPart(clientmain.contextPath + '/js/common/message/user');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientmain_user_title');
		    if(!$rootScope.menuActiveTitle){
		    	$rootScope.menuActiveTitle = $translate.instant('clientmain_user_title');
		    }
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    $translate.onReady().then(function() {
	    	$translate.refresh();
	    });
		
	    // Search.
	    $scope.search = {};
	    
		// Paging.
		$scope.page = {
			pageSize: 3,
			totalElements: 0,
			currentPage: 0
		}
		
		$scope.user = {};
		$scope.isUpdatePassword = false;
		
		// Init for list.
		$scope.initList = function() {
					$scope.listWithCriteriasByPage(1);
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
			$scope.showFormDialog();
		}
		
		// Show a form screen.
		$scope.showForm = function(id, isUpdatePassword) {
			$scope.isUpdatePassword = isUpdatePassword;
			$scope.initForm(id);
			$scope.showFormDialog();
		}

	    // Show edit view.
	    $scope.showFormDialog = function () {
	        var htmlUrlTemplate = clientmain.contextPath + '/view/user_form.html';
	        clientmain.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }
	    
	    // Show roles
	    $scope.showRoles = function(username) {
	        var htmlUrlTemplate = clientmain.contextPath + '/view/userroles_form.html';
	        clientmain.showDialogWithControllerName(clientmain.prefix + 'userrolesController', 'userrolesController', $mdDialog, htmlUrlTemplate, {username: username}).then(function(evt) {
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
			// username.
			$scope.frmUser.username.$touched = false;
			$scope.frmUser.username.$dirty = false;
			// password.
			$scope.frmUser.password.$touched = false;
			$scope.frmUser.password.$dirty = false;
			// displayname.
			$scope.frmUser.displayname.$touched = false;
			$scope.frmUser.displayname.$dirty = false;
			// email.
			$scope.frmUser.email.$touched = false;
			$scope.frmUser.email.$dirty = false;
			// enabled.
			$scope.frmUser.enabled.$touched = false;
			$scope.frmUser.enabled.$dirty = false;
			// firstname.
			$scope.frmUser.firstname.$touched = false;
			$scope.frmUser.firstname.$dirty = false;
			// lastname.
			$scope.frmUser.lastname.$touched = false;
			$scope.frmUser.lastname.$dirty = false;
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
			$scope.showMessageOnToast($translate.instant('clientmain_home_saving'));
			var result;
			if($scope.user.id > -1) {
				if($scope.isUpdatePassword) {
					result = userService.updatePassword($scope.user.username, $scope.user.password);
				} else {
					result = userService.update($scope.user.username, $scope.user);
				}
			} else {
				result = userService.create($scope.user);
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
					$scope.showMessageOnToast($translate.instant('clientmain_home_saved'));
					$scope.listWithCriteriasByPage(1);
				} else {
					if(!response.data) {
						$scope.showMessageOnToast($translate.instant('clientmain_home_fail'));
					}
					else if(response.data.code == clientmain.serverCode.EXISTSCOPE) {
						$scope.frmUser.scope.$invalid = true;
						$scope.showMessageOnToast($translate.instant('clientmain_servercode_' + response.data.code));
					} else if(response.data.code == clientmain.serverCode.VERSIONDIFFERENCE) {
						$scope.showMessageOnToast($translate.instant('clientmain_servercode_' + response.data.code));
					} else {
						$scope.showMessageOnToast($translate.instant('clientmain_home_fail'));
					}
				}
			},
			// error.
			function(response) {
				$scope.showMessageOnToast($translate.instant('clientmain_home_error'));
			});
		}
		
		// Delete.
		$scope.delete = function(user){
			if($window.confirm($translate.instant('clientmain_home_delete_message_confirm'))) {
				userService.updateForDelete(user.username, user.version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToast($translate.instant('clientmain_home_deleted'));
						$scope.listWithCriteriasByPage(1);
					} else {
						$scope.showMessageOnToast($translate.instant('clientmain_home_fail'));
					}
				},
				// error.
				function(response) {
					$scope.showMessageOnToast($translate.instant('clientmain_home_error'));
				});
			}
		}
		
		// Delete with create.
		$scope.deleteOnForm = function() {
			if($window.confirm('Are you sure to delete?')) {
				userService.updateForDelete($scope.user.username, $scope.user.version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToast($translate.instant('clientmain_home_deleted'));
						$scope.createNew();
						$scope.resetValidate();
						$scope.listWithCriteriasByPage(1);
					} else {
						$scope.showMessageOnToast($translate.instant('clientmain_home_fail'));
					}
				},
				// error.
				function(response) {
					$scope.showMessageOnToast($translate.instant('clientmain_home_error'));
				});
			}
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
					$scope.showMessageOnToast($translate.instant('clientmain_home_fail'));
				}
			},
			// error.
			function(response) {
				$scope.showMessageOnToast($translate.instant('clientmain_home_error'));
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
					$scope.showMessageOnToast($translate.instant('clientmain_home_fail'));
				}
			},
			// error.
			function(response) {
				$scope.showMessageOnToast($translate.instant('clientmain_home_error'));
			});
		}
		
		// Clear filter.
		$scope.clearFilter = function() {
			$scope.search = {};
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
		    	result.push({ key: 'displayname', operation: 'like', value: $scope.search.content, logic: 'or' });
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
