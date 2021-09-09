
/**
 * Controller for Supplier
 */

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'supplierController', ['$scope', '$mdToast', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', clientwh.prefix + 'supplierService', clientwh.prefix + 'userService', 'params',
		function($scope, $mdToast, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, supplierService, userService, params) {
		if(typeof(clientwh.translate.supplier) === 'undefined' || clientwh.translate.supplier.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.supplier) === 'undefined') {
				clientwh.translate.supplier = '';
			}
			clientwh.translate.supplier += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/supplier');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_supplier_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    $translate.onReady().then(function() {
	    	$scope.title = $translate.instant('clientwh_supplier_title');
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
		
		$scope.supplier = {id:-1};
		
		
				
		// Init for list.
		$scope.initList = function() {
			// Get permission.
			userService.definePermissionByTarget($scope, 'supplier').then(
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
			$scope.supplier.id = id;
			if($scope.supplier.id > -1) {
				$scope.getById($scope.supplier.id);
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
		
		
		
		//Show form detail
		$scope.showDetail = function(id){
			$scope.getById(id);
			$scope.showDialogDetail();
		}
		
		//Show dialog view
		$scope.showDialogDetail = function () {
	        var htmlUrlTemplate = clientwh.contextPath + '/view/supplier_view.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }
		

	    // Show edit view.
	    $scope.showDialog = function () {
	        var htmlUrlTemplate = clientwh.contextPath + '/view/supplier_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }

	    // Create new.
		$scope.createNew = function() {
			$scope.supplier = { id: -1 };
		}
		
		// Reset validate.
		$scope.resetValidate = function() {
			// scope.
			$scope.frmSupplier.scope.$touched = false;
			$scope.frmSupplier.scope.$dirty = false;
			// content.
			$scope.frmSupplier.content.$touched = false;
			$scope.frmSupplier.content.$dirty = false;
			// frmDirty.
			$scope.frmDirty = false;
		}
		
		// Create on form.
		$scope.createOnForm = function() {
			$scope.createNew();
			$scope.resetValidate();
		}
		
		
		//Show Message Toast List
		$scope.showMessageOnToastList = function(message){
			$mdToast.show(
					{ template: '<md-toast class="md-toast">' + message + '</md-toast>',
						hideDelay:3000,
						position: 'right'})
		}
		
		//Show Message Toast
		$scope.showMessageOnToast = function(message){
			$mdToast.show(
					{ template: '<md-toast class="md-toast">' + message + '</md-toast>',
						hideDelay:3000,
						position: 'top right'})
		}
		
		
		//Delete Toast List
		$scope.cofirmDeleteToastList = function(id,version){
			$mdToast.show(
					{  	templateUrl : clientwh.contextPath + '/view/toast.html',
						hideDelay:5000,
						controller  : 'clientwhsupplierController',
						locals : {params: {showBtnClose:false}},
						position: 'right'}).then(function(response){
							if (response) {
								  $scope.delete(id,version);
							}
						});
		}
		
		//Delete Toast From
		$scope.cofirmDeleteToastForm = function(){
			$mdToast.show(
					{  	templateUrl : clientwh.contextPath + '/view/toast.html',
						hideDelay:5000,
						controller  : 'clientwhsupplierController',
						locals : {params: {showBtnClose:false}},
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
		
		
		
		// Close dialog.
		$scope.closeDialog = function(){
			$mdToast.hide();
			$mdDialog.hide({id: $scope.supplier.id});
			
		}
		
		
		$scope.showBtnClose = params.showBtnClose;
		
		// Close dialoglist.
		$scope.closeDialogList = function(){
			$mdToast.hide();
			$mdDialog.hide({id: $scope.supplier.id});
		}
		
		// Save.
		$scope.save = function() {
			if($scope.frmSupplier.name.$invalid||$scope.frmSupplier.code.$invalid||$scope.frmSupplier.note.$invalid||$scope.frmSupplier.email.$invalid) {
				$scope.frmSupplier.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			$scope.showMessageOnToast($translate.instant('clientwh_home_saving'));
			
			// TODO
			$scope.supplier.content = JSON.stringify({ host:'smtp.gmail.com', port: 465, protocol: 'smtp', username: 'redsunatvn@gmail.com', password: 'yLc/+k4xje6HDSm4XrDPcvbG1lQO4x0z' });
			
			var result;
			$scope.supplier.code = $scope.supplier.code.trim();
			$scope.supplier.name = $scope.supplier.name.trim();
			//$scope.supplier.note = $scope.supplier.note.trim();
			
			if ($scope.supplier.email != null) {
				$scope.supplier.email = $scope.supplier.email.trim();
			}
				
			if($scope.supplier.id > -1) {
				result = supplierService.updateWithLock($scope.supplier.id, $scope.supplier);
			} else {
				result = supplierService.create($scope.supplier);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.supplier.id > -1) {
						$scope.supplier.version = response.data;
					} else {
						$scope.supplier.id = response.data;
						$scope.supplier.version = 1;
					}
					$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
					//$rootScope.idSupplier = $scope.supplier.id;
					$scope.listWithCriteriasByPage(1);
				} else {
					
					if(response.data.code == clientwh.serverCode.EXISTALL) {
						$scope.frmSupplier.code.$invalid = true;
						$scope.frmSupplier.name.$invalid = true;
						$scope.showMessageOnToast($translate.instant('clientwh_supplier_exist'));
					} 
					
					else if(response.data.code == clientwh.serverCode.EXISTCODE) {
						$scope.frmSupplier.code.$invalid = true;
						$scope.showMessageOnToast($translate.instant('clientwh_supplier_name_code'));
					} 
					
					else if(response.data.code == clientwh.serverCode.EXISTNAME) {
						$scope.frmSupplier.name.$invalid = true;
						$scope.showMessageOnToast($translate.instant('clientwh_supplier_name_exist'));
					} 
					
					else if(response.data.code == clientwh.serverCode.VERSIONDIFFERENCE) {
						$scope.showMessageOnToast($translate.instant('clientwh_servercode_' + response.data.code));
					} else {
						$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
					}
				}
			},
			// error.
			function(response) {
				$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
			});
		}
		
		// Delete.
		$scope.delete = function(id, version) {
				supplierService.updateForDeleteWithLock(id, version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToastList($translate.instant('clientwh_home_deleted'));
						$scope.listWithCriteriasByPage(1);
					} else {
						$scope.showMessageOnToastList($translate.instant('clientwh_home_deleted'));
					}
				},
				// error.
				function(response) {
					$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
				});
			
		}
		
		// Delete with create.
		$scope.deleteOnForm = function() {
			
				supplierService.updateForDeleteWithLock($scope.supplier.id, $scope.supplier.version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToast($translate.instant('clientwh_home_deleted'));
						$scope.createNew();
						//$scope.resetValidate();
						$scope.listWithCriteriasByPage(1);
					} else {
						$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
					}
				},
				// error.
				function(response) {
					$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
				});
			
		}
		
		// Get by Id.
		$scope.getById = function(id) {
			supplierService.getById(id)
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.supplier = data;
				} else {
					$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
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
			supplierService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort())
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.suppliers = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.suppliers = result;
						if(result.length > 0) {
							$scope.page.totalElements = response.data.totalElements;
						}
					}
				} else {
					$scope.showMessageOnToastList($translate.instant('clientwh_home_error'));
				}
			},
			// error.
			function(response) {
				$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
			});
		}
		
		// Clear filter.
		$scope.clearFilter = function() {
			$scope.search = {};
			$scope.listWithCriteriasByPage(1);
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
		    // scope, content.
		    if(typeof($scope.search.content) !== 'undefined' && $scope.search.content !== ''){
		    	result.push({ key: 'name', operation: 'like', value: $scope.search.content, logic: 'or' });
		    	result.push({ key: 'code', operation: 'like', value: $scope.search.content, logic: 'or' });
		    	result.push({ key: 'address', operation: 'like', value: $scope.search.content, logic: 'or' });
		    	result.push({ key: 'fax', operation: 'like', value: $scope.search.content, logic: 'or' });
		    	result.push({ key: 'mobile', operation: 'like', value: $scope.search.content, logic: 'or' });
		    	result.push({ key: 'telephone', operation: 'like', value: $scope.search.content, logic: 'or' });
		    }
		    // return.
		    return result;
		}
			
		// Show message.
		$scope.showMessage = function(message, cssName, autoHide) {
			$scope.supplierAlertMessage = message;
			$('#supplierAlertMessage').removeClass('alert-danger');
			$('#supplierAlertMessage').removeClass('alert-success');
			$('#supplierAlertMessage').addClass(cssName);
			$('#supplierAlertMessage').slideDown(500, function() {
				if(autoHide) {
					$window.setTimeout(function() {
						$('#supplierAlertMessage').slideUp(500, function() {
							$('#supplierAlertMessage').removeClass(cssName);
		            	});
					}, 1000);
				}
			});
		}
	
	}]);

});
