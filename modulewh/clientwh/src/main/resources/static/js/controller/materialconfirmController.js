
/**
 * Controller for Material Confirm
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'materialconfirmController', ['$scope', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', clientwh.prefix + 'materialconfirmService',
		function($scope, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, materialconfirmService) {
		//////////////////////////////////////
		// Material list
		/////////////////////////////////////
		
		if(typeof(clientwh.translate.materialconfirm) === 'undefined' || clientwh.translate.materialconfirm.indexOf($translate.use()) < 0) {
			console.log(clientwh.translate.materialconfirm);
			if(typeof(clientwh.translate.materialconfirm) === 'undefined') {
				clientwh.translate.materialconfirm = '';
			}
			clientwh.translate.materialconfirm += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/materialconfirm');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
	    		console.log('clientwh_materialconfirm_title');
		    $scope.title = $translate.instant('clientwh_materialconfirm_title');
		});
		
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    
		$translate.onReady().then(function() {
		    	console.log('materialconfirm onReady');
		    	$scope.title = $translate.instant('clientwh_materialconfirm_title');
		    	$translate.refresh();
	    });
		
	    // Search
		$scope.search = {};
		$scope.lists = {i:[]};
	    
		// Paging
		$scope.page = {
			pageSize: 9,
			totalElements: 0,
			currentPage: 0
		}
		
		$scope.changeLanguage = function(language) {
			$translate.refresh();
			$translate.use(language);
			$translate.refresh();
			$translate.use(language);
			$translate.refresh();
		}
		
		// Init for list
		$scope.initList = function() {
			$scope.listWithCriteriasByPage(1);
		}

		// Clear filter.
		$scope.clearFilter = function() {
			$scope.search = {};
			$scope.initList();
		}

		// List for page and filter.
		$scope.listWithCriteriasByPage = function(pageNo) {
			$scope.page.currentPage = pageNo;
			materialconfirmService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort())
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					var result = angular.fromJson(response.data.content);
					$scope.materialconfirms = result;
					$scope.page.totalElements = 0;
					if(result.length > 0) {
						$scope.page.totalElements = response.data.totalElements;
						$scope.isAllSelected = false;
					}
				} else if(response.status === httpStatus.code.NoContent) {
					$scope.materialconfirms = [];
					$scope.page.totalElements = 0;
					$scope.isAllSelected = false;
				} else {
					$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
				}
			},
			// error.
			function(response) {
				$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
			});
		}
		
		// Check All toggle
		$scope.toggleAll = function () {
	        if ($scope.isAllSelected) {
	            $scope.isAllSelected = true;
	        } else {
	            $scope.isAllSelected = false;
	        }
	        angular.forEach($scope.materialconfirms, function (item) {
	            item.isSelected = $scope.isAllSelected;
	        });
		};
		
		// Check/Uncheck item
		$scope.itemCheckChange = function () {
			$scope.isAllSelected = true;
			angular.forEach($scope.materialconfirms, function (item) {
				if(!item.isSelected) {
					$scope.isAllSelected = false;
					return;
				}
			});
		};
	    
	    // Confirm selected materials
	    $scope.confirmSelectedItems = function() {
	    		
	    		// Get a list of selected materials
	    		var ids = [];
	    		for (var i = 0; i < $scope.materialconfirms.length; i++) {
		    		if ($scope.materialconfirms[i].isSelected){
		    			if(!$scope.materialconfirms[i].name){
		    				$mdDialog.show($mdDialog.alert({theme:'popup'})
							        .textContent($translate.instant('clientwh_materialconfirm_invalid_name'))
							        .ok('OK'));
		    				return;
		    			}
		    			else if(!$scope.materialconfirms[i].code) {
		    				$mdDialog.show($mdDialog.alert({theme:'popup'})
							        .textContent("'" + $scope.materialconfirms[i].name + "' " + $translate.instant('clientwh_materialconfirm_invalid_code'))
							        .ok('OK'));
		    				return;
		    			}
		    			ids.push($scope.materialconfirms[i].id);
		    		}
		    	}
		    	
	    		// Materials list has items
		    	if(ids.length) {
		    		$mdDialog.show($mdDialog.confirm({theme:'popup'})
					        .textContent($translate.instant('clientwh_materialconfirm_confirm_verify'))
					        .ok('OK')
					        .cancel('Cancel')
						   ).then(function() {
							   clientwh.showPleaseWait($translate.instant('clientwh_materialconfirm_confirming'));
							   var result;
					    			result = materialconfirmService.updateUnconfirmedItems(ids);
					    			result
									// success.
									.then(function(response) {
										clientwh.hidePleaseWait();
										if(response.status === httpStatus.code.OK) {
											var data = angular.fromJson(response.data);
											$scope.listWithCriteriasByPage(1);
										} else {
											$window.alert($translate.instant('clientwh_home_fail'));
										}
									},
									// error.
									function(response) {
										clientwh.hidePleaseWait();
										$window.alert($translate.instant('clientwh_home_error'));
									});
						   });
		    	}
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
		    	result.push('sort=' + $scope.sortKey + '&reverse=' + $scope.reverse);
		    }
			// return.
			return result;
		}
		
		// Get search object.
		$scope.getSearch = function() {
		    var result = [];
		    // id.
		    if(typeof($scope.search.id) !== 'undefined' && $scope.search.id !== ''){
		    	result.push({ key: 'id', operation: '=', value: $scope.search.id, logic: 'or' });
		    }
		    // name.
		    if(typeof($scope.search.name) !== 'undefined' && $scope.search.name !== ''){
		    	result.push({ key: 'name', operation: 'like', value: $scope.search.name, logic: 'or' });
		    }
		    // code.
		    if(typeof($scope.search.code) !== 'undefined' && $scope.search.code !== ''){
		    	result.push({ key: 'code', operation: 'like', value: $scope.search.code, logic: 'or' });
		    }
		    // return.
		    return result;
		}
		
	    
		//////////////////////////////////////
		// Confirm a material form
		/////////////////////////////////////

		$scope.materialconfirm = {};

		// Init for form
		$scope.initForm = function(id) {
			$scope.materialconfirm = {};
			$scope.materialconfirm.id = id;
			$scope.getAndLockById($scope.materialconfirm.id);
			$scope.frmDirty = false;
		}
		
		// Show a form screen.
		$scope.showForm = function(id) {
			$scope.initForm(id);
			$scope.showDialog();
		}

	    // Show edit view.
	    $scope.showDialog = function () {
	        var htmlUrlTemplate = clientwh.contextPath + '/view/materialconfirm_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        		console.log('closed');
	        }, function(evt) {
	        		console.log('not closed');
	        });
	    }

	    // Clear Search By Name input value
	    $scope.clearNameSearch = function() {
	    		$("#nameSearch").val('');
	    }
	    
	    // Search material by name
	    $scope.searchByName = function() {
		    	return materialconfirmService.search($scope.frmMaterialConfirm.nameSearch.searchText)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						var result = angular.fromJson(response.data);
						return result;
					} else {
						$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
						return [];
					}
				},
				// error.
				function(response) {
					$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
					return [];
				});
	    }
	    
	    // Selected material change
	    $scope.selectedItemChange = function(item) {
	    		if(item)
	    			$scope.materialconfirm.code = item.code;
	    }
		
		// Show message in top of form
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
		
		// Get material by Id
		$scope.getById = function(id) {
			materialconfirmService.getById(id)
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.materialconfirm = data;
				} else {
					$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
				}
			},
			// error.
			function(response) {
				$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
			});
		}
		
		// Get and lock item material by Id
		$scope.getAndLockById = function(id) {
			materialconfirmService.getAndLockById(id)
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.materialconfirm = data;
				} else {
					$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
				}
			},
			// error.
			function(response) {
				$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
			});
		}
		
		// Save a material
		$scope.save = function() {
			if($scope.frmMaterialConfirm.$invalid) {
				$scope.frmMaterialConfirm.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			
			// Check material with code is existed
			materialconfirmService.getConfirmedItemByCode($scope.materialconfirm.code)
			// check code success
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					var existedMaterial = angular.fromJson(response.data);
					$mdDialog.show($mdDialog.confirm({multiple: true, theme:'popup'})
					        .textContent("'" + $scope.materialconfirm.name + $translate.instant('clientwh_materialconfirm_reference_verify') + existedMaterial.name + "'")
					        .ok('OK')
					        .cancel('Cancel')
						   ).then(function() {
							   $scope.saveRequest();
						   });
					
				} else if(response.status === httpStatus.code.NoContent) {
					$mdDialog.show($mdDialog.confirm({multiple: true, theme:'popup'})
					        .textContent("'" + $scope.materialconfirm.name + $translate.instant('clientwh_materialconfirm_new_verify') + $scope.materialconfirm.code + "'")
					        .ok('OK')
					        .cancel('Cancel')
						   ).then(function() {
							   $scope.saveRequest();
						   });
				}
				else {
					$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
					return;
				}
			},
			// check code error
			function(response) {
				$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
			});
		}
		
		// Send save material request
		$scope.saveRequest = function() {
			$scope.showMessage('Saving!', 'alert-success', false);
			materialconfirmService.update($scope.materialconfirm.id, $scope.materialconfirm)
			// save material request success
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.listWithCriteriasByPage(1);
					$scope.closeDialog();
				} else {
					$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
				}
			},
			// save material request error
			function(response) {
				$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
			});
		}
		
	}]);

});
