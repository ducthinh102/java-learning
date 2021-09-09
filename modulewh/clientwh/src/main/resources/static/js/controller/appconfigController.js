
/**
 * Controller for Appconfig
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'appconfigController', ['$scope', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', clientwh.prefix + 'appconfigService', clientwh.prefix + 'userService',
		function($scope, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, appconfigService, userService) {
		if(typeof(clientwh.translate.appconfig) === 'undefined' || clientwh.translate.appconfig.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.appconfig) === 'undefined') {
				clientwh.translate.appconfig = '';
			}
			clientwh.translate.appconfig += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/appconfig');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_appconfig_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    $translate.onReady().then(function() {
	    	$scope.title = $translate.instant('clientwh_appconfig_title');
	    	$translate.refresh();
	    });
	    
	    // Get permission.
		$scope.permission = {};
		$scope.isPermisCreate = false;
		$scope.isPermisUpdate = false;
		$scope.isPermisDelete = false;
		userService.getPermissionByTarget('appconfig').then(
				// success.
				function(response) {
					if(response.data) {
						$scope.permission = response.data;
						$scope.isPermisCreate = ($scope.permission.admins.length == 0 || $scope.permission.admins.indexOf($scope.permission.id) > -1) || ($scope.permission.creates.length == 0 || $scope.permission.creates.indexOf($scope.permission.id) > -1);
						$scope.isPermisUpdate = ($scope.permission.admins.length == 0 || $scope.permission.admins.indexOf($scope.permission.id) > -1) || ($scope.permission.updates.length == 0 || $scope.permission.updates.indexOf($scope.permission.id) > -1);
						$scope.isPermisDelete = ($scope.permission.admins.length == 0 || $scope.permission.admins.indexOf($scope.permission.id) > -1) || ($scope.permission.deletes.length == 0 || $scope.permission.deletes.indexOf($scope.permission.id) > -1)
					}
				},
				// error.
				function(reponse) {
					$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
				}
		);
	    // Search.
	    $scope.search = {};
	    
		// Paging.
		$scope.page = {
			pageSize: 3,
			totalElements: 0,
			currentPage: 0
		}
		
		$scope.appconfig = {};
		
		// Init for list.
		$scope.initList = function() {
			$scope.listWithCriteriasByPage(1);
		}
		
		// Init for form.
		$scope.initForm = function(id) {
			$scope.createNew();
			$scope.appconfig.id = id;
			if($scope.appconfig.id > -1) {
				$scope.getById($scope.appconfig.id);
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
	        var htmlUrlTemplate = clientwh.contextPath + '/view/appconfig_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }

	    // Create new.
		$scope.createNew = function() {
			$scope.appconfig = { id: -1 };
		}
		
		// Reset validate.
		$scope.resetValidate = function() {
			// scope.
			$scope.frmAppconfig.scope.$touched = false;
			$scope.frmAppconfig.scope.$dirty = false;
			// content.
			$scope.frmAppconfig.content.$touched = false;
			$scope.frmAppconfig.content.$dirty = false;
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
			if($scope.frmAppconfig.$invalid) {
				$scope.frmAppconfig.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			$scope.showMessage('Saving!', 'alert-success', false);
			
			// TODO
			$scope.appconfig.content = JSON.stringify({ host:'smtp.gmail.com', port: 465, protocol: 'smtp', username: 'redsunatvn@gmail.com', password: 'yLc/+k4xje6HDSm4XrDPcvbG1lQO4x0z' });
			
			var result;
			if($scope.appconfig.id > -1) {
				result = appconfigService.updateWithLock($scope.appconfig.id, $scope.appconfig);
			} else {
				result = appconfigService.create($scope.appconfig);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.appconfig.id > -1) {
						$scope.appconfig.version = response.data;
					} else {
						$scope.appconfig.id = response.data;
						$scope.appconfig.version = 1;
					}
					$scope.showMessage($translate.instant('clientwh_home_saved'), 'alert-success', true);
					$scope.listWithCriteriasByPage(1);
				} else {
					if(response.data.code == clientwh.serverCode.EXISTSCOPE) {
						$scope.frmAppconfig.scope.$invalid = true;
						$scope.showMessage($translate.instant('clientwh_servercode_' + response.data.code), 'alert-danger', false);
					} else if(response.data.code == clientwh.serverCode.VERSIONDIFFERENCE) {
						$scope.showMessage($translate.instant('clientwh_servercode_' + response.data.code), 'alert-danger', false);
					} else {
						$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
					}
				}
			},
			// error.
			function(response) {
				$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
			});
		}
		
		// Delete.
		$scope.delete = function(id, version) {
			if($window.confirm('Are you sure to delete?')) {
				appconfigService.updateForDeleteWithLock(id, version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessage('Deleted!', 'alert-success', true);
						$scope.listWithCriteriasByPage(1);
					} else {
						$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
					}
				},
				// error.
				function(response) {
					$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
				});
			}
		}
		
		// Delete with create.
		$scope.deleteOnForm = function() {
			if($window.confirm('Are you sure to delete?')) {
				appconfigService.updateForDeleteWithLock($scope.appconfig.id, $scope.appconfig.version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessage('Deleted!', 'alert-success', true);
						$scope.createNew();
						$scope.resetValidate();
						$scope.listWithCriteriasByPage(1);
					} else {
						$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
					}
				},
				// error.
				function(response) {
					$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
				});
			}
		}
		
		// Get by Id.
		$scope.getById = function(id) {
			appconfigService.getById(id)
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.appconfig = data;
				} else {
					$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
				}
			},
			// error.
			function(response) {
				$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
			});
		}
		
		// List for page and filter.
		$scope.listWithCriteriasByPage = function(pageNo) {
			$scope.page.currentPage = pageNo;
			appconfigService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort())
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.appconfigs = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.appconfigs = result;
						if(result.length > 0) {
							$scope.page.totalElements = response.data.totalElements;
						}
					}
				} else {
					$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
				}
			},
			// error.
			function(response) {
				$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
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
		    // scope, content.
		    if(typeof($scope.search.content) !== 'undefined' && $scope.search.content !== ''){
		    	result.push({ key: 'scope', operation: 'like', value: $scope.search.content, logic: 'or' });
		    	result.push({ key: 'content', operation: 'like', value: $scope.search.content, logic: 'or' });
		    }
		    // return.
		    return result;
		}
			
		// Show message.
		$scope.showMessage = function(message, cssName, autoHide) {
			$scope.alertMessage = message;
			$('#alertMessage').removeClass('alert-danger');
			$('#alertMessage').removeClass('alert-success');
			$('#alertMessage').addClass(cssName);
			$('#alertMessage').slideDown(500, function() {
				if(autoHide) {
					$window.setTimeout(function() {
						$('#alertMessage').slideUp(500, function() {
							$('#alertMessage').removeClass(cssName);
		            	});
					}, 1000);
				}
			});
		}
	
	}]);

});
