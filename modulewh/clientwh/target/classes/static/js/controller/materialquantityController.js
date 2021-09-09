
/**
 * Controller for Materialquantity
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'materialquantityController', ['$scope', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', clientwh.prefix + 'materialquantityService',
		function($scope, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, materialquantityService) {
		if(typeof(clientwh.translate.materialquantity) === 'undefined' || clientwh.translate.materialquantity.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.materialquantity) === 'undefined') {
				clientwh.translate.materialquantity = '';
			}
			clientwh.translate.materialquantity += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/materialquantity');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_materialquantity_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    $translate.onReady().then(function() {
	    	$scope.title = $translate.instant('clientwh_materialquantity_title');
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
		
		$scope.materialquantity = {};
		
		// Init for list.
		$scope.initList = function() {
			$scope.listWithCriteriasByPage(1);
		}
		
		// Init for form.
		$scope.initForm = function(id) {
			$scope.createNew();
			$scope.materialquantity.id = id;
			if($scope.materialquantity.id > -1) {
				$scope.getById($scope.materialquantity.id);
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
	        var htmlUrlTemplate = clientwh.contextPath + '/view/materialquantity_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }

	    // Create new.
		$scope.createNew = function() {
			$scope.materialquantity = { id: -1 };
		}
		
		// Reset validate.
		$scope.resetValidate = function() {
			// idref.
		    $scope.frmMaterialquantity.idref.$setPristine();
			$scope.frmMaterialquantity.idref.$setUntouched();
			// reftype.
		    $scope.frmMaterialquantity.reftype.$setPristine();
			$scope.frmMaterialquantity.reftype.$setUntouched();
			// scope.
		    $scope.frmMaterialquantity.scope.$setPristine();
			$scope.frmMaterialquantity.scope.$setUntouched();
			// quantity.
		    $scope.frmMaterialquantity.quantity.$setPristine();
			$scope.frmMaterialquantity.quantity.$setUntouched();
			// note.
		    $scope.frmMaterialquantity.note.$setPristine();
			$scope.frmMaterialquantity.note.$setUntouched();
			// version.
		    $scope.frmMaterialquantity.version.$setPristine();
			$scope.frmMaterialquantity.version.$setUntouched();

		    // form.
			$scope.frmMaterialquantity.$setPristine();
			$scope.frmMaterialquantity.$setUntouched();
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
			if($scope.frmMaterialquantity.$invalid) {
				$scope.frmMaterialquantity.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			$scope.showMessage('Saving!', 'alert-success', false);
			var result;
			if($scope.materialquantity.id > -1) {
				result = materialquantityService.updateWithLock($scope.materialquantity.id, $scope.materialquantity);
			} else {
				result = materialquantityService.create($scope.materialquantity);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.materialquantity.id > -1) {
						$scope.materialquantity.version = response.data;
					} else {
						$scope.materialquantity.id = response.data;
						$scope.materialquantity.version = 1;
					}
					$scope.showMessage($translate.instant('clientwh_home_saved'), 'alert-success', true);
					$scope.listWithCriteriasByPage(1);
				} else {
					if(response.data.code == clientwh.serverCode.EXISTSCOPE) {
						$scope.frmMaterialquantity.scope.$invalid = true;
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
				materialquantityService.updateForDeleteWithLock(id, version)
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
				materialquantityService.updateForDeleteWithLock($scope.materialquantity.id, $scope.materialquantity.version)
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
			materialquantityService.getById(id)
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.materialquantity = data;
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
			materialquantityService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort())
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.materialquantitys = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.materialquantitys = result;
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
				// idref.
		    	//result.push({ key: 'idref', operation: 'like', value: $scope.search.content, logic: 'or' });
				// reftype.
		    	//result.push({ key: 'reftype', operation: 'like', value: $scope.search.content, logic: 'or' });
				// scope.
		    	//result.push({ key: 'scope', operation: 'like', value: $scope.search.content, logic: 'or' });
				// quantity.
		    	//result.push({ key: 'quantity', operation: 'like', value: $scope.search.content, logic: 'or' });
				// note.
		    	//result.push({ key: 'note', operation: 'like', value: $scope.search.content, logic: 'or' });
				// version.
		    	//result.push({ key: 'version', operation: 'like', value: $scope.search.content, logic: 'or' });
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
