/**
 * Controller for Catalog
 */

define(['require', 'angular'], function (require, angular) {

	app.aController(clientwh.prefix + 'warehouseController', ['$scope', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader',
		
		function($scope, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, warehouseService) {
			
			if(typeof(clientwh.translate.warehouse) === 'undefined' || clientwh.translate.warehouse.indexOf($translate.use()) < 0) {
				console.log(clientwh.translate.warehouse);
				if(typeof(clientwh.translate.warehouse) === 'undefined') {
					clientwh.translate.warehouse = '';
				}
				clientwh.translate.warehouse += $translate.use() + ';';
				$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/warehouse');
				$translate.refresh();
			}

			var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
				console.log('clientwh_warehouse_title');
				$scope.title = $translate.instant('clientwh_warehouse_title');
			});

		// Unregister
		$scope.$on('$destroy', function () {
			unRegister();
		});		

		$translate.onReady().then(function() {
			console.log('warehouse onReady');
			$scope.title = $translate.instant('clientwh_warehouse_title');
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
		
		$scope.warehouse = {};

		$scope.show = function() {
			alert($scope.title);
		}

		$scope.goto = function(state, params) {
			$state.go(clientwh.prefix + state, params);
		}

		$scope.changeLanguage = function(language) {
			$translate.refresh();
			$translate.use(language);
			$translate.refresh();
			$translate.use(language);
			$translate.refresh();
		} 
		
		// Init for list.
		$scope.initList = function() {
			$scope.listWithCriteriasByPage(1);
		}
		
		// Init for form.
		$scope.initForm = function(id) {
			//$scope.getProject();
			$scope.createNew();
			$scope.warehouse.id = id;
			if($scope.warehouse.id > -1) {
				$scope.getById($scope.warehouse.id);
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
	    	var htmlUrlTemplate = clientwh.contextPath + '/view/warehouse_form.html';
	    	clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	    		console.log('closed');
	    	}, function(evt) {
	    		console.log('not closed');
	    	});
	    }

	    // Create new.
	    $scope.createNew = function() {
	    	$scope.warehouse = { id: -1 };
	    }

		// Save.
		$scope.save = function () {

			if ($scope.frmWarehouse.$invalid) {
				$scope.frmWarehouse.$dirty = true;
				$scope.frmDirty = true;
				return;
			}

			$scope.showMessage('Saving!', 'alert-success', false);
			var result;
			if ($scope.warehouse.id > -1) {
				result = warehouseService.update($scope.warehouse.id, $scope.warehouse);
			} 
			
			else {
				result = warehouseService.create($scope.warehouse);
			}

			result
				// success.
				.then(function (response) {

					if (response.status === httpStatus.code.Created || response.status === httpStatus.code.OK) {
						
						var data = angular.fromJson(response.data);
						$scope.warehouse.id = data.id;
						$scope.showMessage($translate.instant('clientwh_home_saved'), 'alert-success', true);
						$scope.listWithCriteriasByPage(1);
						
					} 
					
					else {
						$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
					}
				},

				// error.
				function (response) {
					$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
				});
		}
		
		// Delete.
		$scope.delete = function(id){
			if($window.confirm('Are you sure to delete?')) {
				warehouseService.delete(id)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.NoContent) {
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
		
		// Get by Id.
		$scope.getById = function(id) {
			warehouseService.getById(id)
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.warehouse = data;
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
			warehouseService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort())
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					var result = angular.fromJson(response.data.content);
					$scope.warehouses = result;
					$scope.page.totalElements = 0;
					if(result.length > 0) {
						$scope.page.totalElements = response.data.totalElements;
					}
				} else if(response.status === httpStatus.code.NoContent) {
					$scope.warehouses = [];
					$scope.page.totalElements = 0;
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

		/* Extend functions */
		
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
		    // object.
		    if(typeof($scope.search.object) !== 'undefined' && $scope.search.object !== ''){
		    	result.push({ key: 'object', operation: 'like', value: $scope.search.object, logic: 'or' });
		    }
		    // rules.
		    if(typeof($scope.search.rules) !== 'undefined' && $scope.search.rules !== ''){
		    	result.push({ key: 'rules', operation: 'like', value: $scope.search.rules, logic: 'or' });
		    }
		    // return.
		    return result;
		}

		// Show message.
		$scope.showMessage = function(message, cssName, autoHide) {
			$scope.alertMessage = message;
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
		
		// Get project
		$scope.getProject = function(){
			
			projectService.loadProject()
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.projects = data;
				} else {
					$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
				}
			},
			// error.
			function(response) {
				$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
			});
		}
	}]);
});
