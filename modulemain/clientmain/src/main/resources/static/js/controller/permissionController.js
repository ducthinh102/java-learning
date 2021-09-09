
/**
 * Controller for Permission
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientmain.prefix + 'permissionController', ['$scope', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', clientmain.prefix + 'permissionService',
		function($scope, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, permissionService) {
		if(typeof(clientmain.translate.permission) === 'undefined' || clientmain.translate.permission.indexOf($translate.use()) < 0) {
			if(typeof(clientmain.translate.permission) === 'undefined') {
				clientmain.translate.permission = '';
			}
			clientmain.translate.permission += $translate.use() + ';';
			$translatePartialLoader.addPart(clientmain.contextPath + '/js/common/message/permission');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientmain_permission_title');
		    if(!$rootScope.menuActiveTitle){
		    	$rootScope.menuActiveTitle = $translate.instant('clientmain_permission_title');
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
		
		$scope.permission = {};
		
		// Init for list.
		$scope.initList = function() {
			$scope.listWithCriteriasByPage(1);
		}
		
		// Init for form.
		$scope.initForm = function(id) {
			$scope.createNew();
			$scope.permission.id = id;
			if($scope.permission.id > -1) {
				$scope.getById($scope.permission.id);
			}
			$scope.frmDirty = false;
		}
		
		// Show a create screen.
		$scope.showCreate = function() {
			$scope.initForm(-1);
			$scope.showDialog();
		}
		
		// Show a form screen.
		$scope.showForm = function(id) {
			$scope.initForm(id);
			$scope.showDialog();
		}

	    // Show edit view.
	    $scope.showDialog = function () {
	        var htmlUrlTemplate = clientmain.contextPath + '/view/permission_form.html';
	        clientmain.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }

	    // Create new.
		$scope.createNew = function() {
			$scope.permission = { id: -1 };
		}
		
		// Reset validate.
		$scope.resetValidate = function() {
			// target.
		    $scope.frmPermission.target.$setPristine();
			$scope.frmPermission.target.$setUntouched();
			// rules.
		    $scope.frmPermission.rules.$setPristine();
			$scope.frmPermission.rules.$setUntouched();

		    // form.
			$scope.frmPermission.$setPristine();
			$scope.frmPermission.$setUntouched();
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
			if($scope.frmPermission.$invalid) {
				$scope.frmPermission.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			$scope.showMessage($translate.instant('clientmain_home_saving'), 'alert-success', false);
			var result;
			if($scope.permission.id > -1) {
				result = permissionService.updateWithLock($scope.permission.id, $scope.permission);
			} else {
				result = permissionService.create($scope.permission);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.permission.id > -1) {
						$scope.permission.version = response.data;
					} else {
						$scope.permission.id = response.data;
						$scope.permission.version = 1;
					}
					$scope.showMessage($translate.instant('clientmain_home_saved'), 'alert-success', true);
					$scope.listWithCriteriasByPage(1);
				} else {
					if(response.data.code == clientmain.serverCode.EXISTSCOPE) {
						$scope.frmPermission.scope.$invalid = true;
						$scope.showMessage($translate.instant('clientmain_servercode_' + response.data.code), 'alert-danger', false);
					} else if(response.data.code == clientmain.serverCode.VERSIONDIFFERENCE) {
						$scope.showMessage($translate.instant('clientmain_servercode_' + response.data.code), 'alert-danger', false);
					} else {
						$scope.showMessage($translate.instant('clientmain_home_fail'), 'alert-danger', true);
					}
				}
			},
			// error.
			function(response) {
				$scope.showMessage($translate.instant('clientmain_home_error'), 'alert-danger', true);
			});
		}
		
		// Delete.
		$scope.delete = function(id, version) {
			if($window.confirm('Are you sure to delete?')) {
				permissionService.updateForDeleteWithLock(id, version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessage('Deleted!', 'alert-success', true);
						$scope.listWithCriteriasByPage(1);
					} else {
						$scope.showMessage($translate.instant('clientmain_home_fail'), 'alert-danger', true);
					}
				},
				// error.
				function(response) {
					$scope.showMessage($translate.instant('clientmain_home_error'), 'alert-danger', true);
				});
			}
		}
		
		// Delete with create.
		$scope.deleteOnForm = function() {
			if($window.confirm('Are you sure to delete?')) {
				permissionService.updateForDeleteWithLock($scope.permission.id, $scope.permission.version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessage('Deleted!', 'alert-success', true);
						$scope.createNew();
						$scope.resetValidate();
						$scope.listWithCriteriasByPage(1);
					} else {
						$scope.showMessage($translate.instant('clientmain_home_fail'), 'alert-danger', true);
					}
				},
				// error.
				function(response) {
					$scope.showMessage($translate.instant('clientmain_home_error'), 'alert-danger', true);
				});
			}
		}
		
		// Get by Id.
		$scope.getById = function(id) {
			permissionService.getById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.permission = data;
				} else {
					$scope.showMessage($translate.instant('clientmain_home_fail'), 'alert-danger', true);
				}
			},
			// error.
			function(response) {
				$scope.showMessage($translate.instant('clientmain_home_error'), 'alert-danger', true);
			});
		}
		
		// List for page and filter.
		$scope.listWithCriteriasByPage = function(pageNo) {
			$scope.page.currentPage = pageNo;
			permissionService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.permissions = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.permissions = result;
						if(result.length > 0) {
							$scope.page.totalElements = response.data.totalElements;
						}
					}
				} else {
					$scope.showMessage($translate.instant('clientmain_home_fail'), 'alert-danger', true);
				}
			},
			// error.
			function(response) {
				$scope.showMessage($translate.instant('clientmain_home_error'), 'alert-danger', true);
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
		}
		
		// Get sort object.
		$scope.getSort = function() {
			var result = [];
			// name.
		    if(typeof($scope.sortKey) !== 'undefined' && $scope.sortKey !== ''){
		    	result.push('sort=' + $scope.sortKey + ',' + $scope.reverse);
		    }
			// return.
			return result;
		}
		
		// Get search object.
		$scope.getSearch = function() {
		    var result = [];
		    if(typeof($scope.search.content) !== 'undefined' && $scope.search.content !== ''){
				// target.
		    	//result.push({ key: 'target', operation: 'like', value: $scope.search.content, logic: 'or' });
				// rules.
		    	//result.push({ key: 'rules', operation: 'like', value: $scope.search.content, logic: 'or' });
		    }
		    // return.
		    return result;
		}
			
		// Show message.
		$scope.showMessage = function(message, cssName, autoHide) {
			$scope.permissionAlertMessage = message;
			$('#permissionAlertMessage').removeClass('alert-danger');
			$('#permissionAlertMessage').removeClass('alert-success');
			$('#permissionAlertMessage').addClass(cssName);
			$('#permissionAlertMessage').slideDown(500, function() {
				if(autoHide) {
					$window.setTimeout(function() {
						$('#permissionAlertMessage').slideUp(500, function() {
							$('#permissionAlertMessage').removeClass(cssName);
		            	});
					}, 1000);
				}
			});
		}
	
	}]);

});
