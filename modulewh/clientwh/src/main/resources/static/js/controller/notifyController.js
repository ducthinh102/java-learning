
/**
 * Controller for Notify
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'notifyController', ['$scope', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', clientwh.prefix + 'notifyService',
		function($scope, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, notifyService) {
		if(typeof(clientwh.translate.notify) === 'undefined' || clientwh.translate.notify.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.notify) === 'undefined') {
				clientwh.translate.notify = '';
			}
			clientwh.translate.notify += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/notify');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_notify_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    $translate.onReady().then(function() {
	    	$scope.title = $translate.instant('clientwh_notify_title');
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
		
		$scope.notify = {};
		
		// Init for list.
		$scope.initList = function() {
			$scope.listWithCriteriasByPage(1);
		}
		
		// Init for form.
		$scope.initForm = function(id) {
			$scope.createNew();
			$scope.notify.id = id;
			if($scope.notify.id > -1) {
				$scope.getById($scope.notify.id);
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
	        var htmlUrlTemplate = clientwh.contextPath + '/view/notify_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }

	    // Create new.
		$scope.createNew = function() {
			$scope.notify = { id: -1 };
		}
		
		// Reset validate.
		$scope.resetValidate = function() {
			// idsender.
		    $scope.frmNotify.idsender.$setPristine();
			$scope.frmNotify.idsender.$setUntouched();
			// idreceiver.
		    $scope.frmNotify.idreceiver.$setPristine();
			$scope.frmNotify.idreceiver.$setUntouched();
			// idref.
		    $scope.frmNotify.idref.$setPristine();
			$scope.frmNotify.idref.$setUntouched();
			// reftype.
		    $scope.frmNotify.reftype.$setPristine();
			$scope.frmNotify.reftype.$setUntouched();
			// content.
		    $scope.frmNotify.content.$setPristine();
			$scope.frmNotify.content.$setUntouched();
			// method.
		    $scope.frmNotify.method.$setPristine();
			$scope.frmNotify.method.$setUntouched();
			// priority.
		    $scope.frmNotify.priority.$setPristine();
			$scope.frmNotify.priority.$setUntouched();
			// isactive.
		    $scope.frmNotify.isactive.$setPristine();
			$scope.frmNotify.isactive.$setUntouched();

		    // form.
			$scope.frmNotify.$setPristine();
			$scope.frmNotify.$setUntouched();
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
			if($scope.frmNotify.$invalid) {
				$scope.frmNotify.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			$scope.showMessage($translate.instant('clientwh_home_saving'), 'alert-success', false);
			var result;
			if($scope.notify.id > -1) {
				result = notifyService.updateWithLock($scope.notify.id, $scope.notify);
			} else {
				result = notifyService.create($scope.notify);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.notify.id > -1) {
						$scope.notify.version = response.data;
					} else {
						$scope.notify.id = response.data;
						$scope.notify.version = 1;
					}
					$scope.showMessage($translate.instant('clientwh_home_saved'), 'alert-success', true);
					$scope.listWithCriteriasByPage(1);
				} else {
					if(response.data.code == clientwh.serverCode.EXISTSCOPE) {
						$scope.frmNotify.scope.$invalid = true;
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
				notifyService.updateForDeleteWithLock(id, version)
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
				notifyService.updateForDeleteWithLock($scope.notify.id, $scope.notify.version)
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
			notifyService.getById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.notify = data;
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
			notifyService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.notifys = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.notifys = result;
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
		    if(typeof($scope.search.content) !== 'undefined' && $scope.search.content !== ''){
				// idsender.
		    	//result.push({ key: 'idsender', operation: 'like', value: $scope.search.content, logic: 'or' });
				// idreceiver.
		    	//result.push({ key: 'idreceiver', operation: 'like', value: $scope.search.content, logic: 'or' });
				// idref.
		    	//result.push({ key: 'idref', operation: 'like', value: $scope.search.content, logic: 'or' });
				// reftype.
		    	//result.push({ key: 'reftype', operation: 'like', value: $scope.search.content, logic: 'or' });
				// content.
		    	//result.push({ key: 'content', operation: 'like', value: $scope.search.content, logic: 'or' });
				// method.
		    	//result.push({ key: 'method', operation: 'like', value: $scope.search.content, logic: 'or' });
				// priority.
		    	//result.push({ key: 'priority', operation: 'like', value: $scope.search.content, logic: 'or' });
				// isactive.
		    	//result.push({ key: 'isactive', operation: 'like', value: $scope.search.content, logic: 'or' });
		    }
		    // return.
		    return result;
		}
			
		// Show message.
		$scope.showMessage = function(message, cssName, autoHide) {
			$scope.notifyAlertMessage = message;
			$('#notifyAlertMessage').removeClass('alert-danger');
			$('#notifyAlertMessage').removeClass('alert-success');
			$('#notifyAlertMessage').addClass(cssName);
			$('#notifyAlertMessage').slideDown(500, function() {
				if(autoHide) {
					$window.setTimeout(function() {
						$('#notifyAlertMessage').slideUp(500, function() {
							$('#notifyAlertMessage').removeClass(cssName);
		            	});
					}, 1000);
				}
			});
		}
	
	}]);

});
