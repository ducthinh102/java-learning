
/**
 * Controller for Brand
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'brandController', ['$scope', '$mdToast', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader',clientwh.prefix + 'catalogService', clientwh.prefix + 'userService', 'params',
		function($scope, $mdToast, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, catalogService, userService, params) {
		
		if(typeof(clientwh.translate.brand) === 'undefined' || clientwh.translate.brand.indexOf($translate.use()) < 0) {
			console.log(clientwh.translate.brand);
			if(typeof(clientwh.translate.brand) === 'undefined') {
				clientwh.translate.brand = '';
			}
			clientwh.translate.brand += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/brand');
			$translate.refresh();
		}

		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
			console.log('clientwh_brand_title');
			$scope.title = $translate.instant('clientwh_brand_title');
		});

	// Unregister
	$scope.$on('$destroy', function () {
		unRegister();
	});		

	$translate.onReady().then(function() {
		console.log('catalog onReady');
		$scope.title = $translate.instant('clientwh_brand_title');
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
	
	$scope.showBtnClose = params.showBtnClose;
	
	// Close dialoglist.
	$scope.closeDialogList = function(){
		$mdToast.hide();
		$mdDialog.hide({id: $scope.catalog.id});
	}
	
	$scope.catalog = {};
	    
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
						controller  : 'clientwhbrandController',
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
		$scope.cofirmDeleteToastList = function(id,version){
			$mdToast.show(
					{  	templateUrl : clientwh.contextPath + '/view/toast.html',
						hideDelay:5000,
						controller  : 'clientwhbrandController',
						locals : {params: {showBtnClose:false}},
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
			userService.definePermissionByTarget($scope, 'brand').then(
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
			$scope.catalog.id = id;
			if($scope.catalog.id > -1) {
				$scope.getById($scope.catalog.id);
			}
			$scope.frmDirty = false;
		}
		
		//Show form detail
		$scope.showDetail = function(id){
			$scope.getById(id);
			$scope.showDialogDetail();
		}
		
		//Show dialog view
		$scope.showDialogDetail = function () {
	        var htmlUrlTemplate = clientwh.contextPath + '/view/brand_view.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
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
	    	$mdToast.hide();
	    	var htmlUrlTemplate = clientwh.contextPath + '/view/brand_form.html';
	    	clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	    		console.log('closed');
	    	}, function(evt) {
	    		console.log('not closed');
	    	});
	    }


	    // Create new.
		$scope.create = function() {
			$scope.catalog = { id: -1 };
			$scope.resetValidate();
		}
		
		$scope.resetValidate = function(){
			$scope.resetValidateDuplicate(0);
			
			// code.
			$scope.frmCatalog.code.$setPristine();
			$scope.frmCatalog.code.$setUntouched();
			$scope.frmCatalog.code.$modelValue = null;
			// name.
			$scope.frmCatalog.name.$setPristine();
			$scope.frmCatalog.name.$setUntouched();
			$scope.frmCatalog.name.$modelValue = null;
			
			$scope.frmCatalog.note.$setPristine();
			$scope.frmCatalog.note.$setUntouched();
			$scope.frmCatalog.note.$modelValue = null;
			
			$scope.frmCatalog.$setPristine();
			$scope.frmCatalog.$setUntouched();
			
			$scope.frmDirty = false;
		}
		
		// Create new.
	    $scope.createNew = function() {
	    	$scope.catalog = { id: -1 };
	    }
	    
	  //Reset validate duplicate
	    $scope.resetValidateDuplicate = function(number){
	    	switch (number) {
			case 1:
				if($scope.frmCatalog.code.$error.duplicate){
					delete $scope.frmCatalog.code.$error.duplicate;
					$scope.frmCatalog.code.$invalid = false;
					$scope.frmCatalog.code.$valid = true;
				}
				break;
			case 2:
				if($scope.frmCatalog.name.$error.duplicate){
					delete $scope.frmCatalog.name.$error.duplicate;
					$scope.frmCatalog.name.$invalid = false;
					$scope.frmCatalog.name.$valid = true;
				}
				break;
			default:
				if($scope.frmCatalog.code.$error.duplicate){
					delete $scope.frmCatalog.code.$error.duplicate;
					$scope.frmCatalog.code.$invalid = false;
					$scope.frmCatalog.code.$valid = true;
				}
				if($scope.frmCatalog.name.$error.duplicate){
					delete $scope.frmCatalog.name.$error.duplicate;
					$scope.frmCatalog.name.$invalid = false;
					$scope.frmCatalog.name.$valid = true;
				}
				break;
			}
	    }
		// Save.
		$scope.save = function () {
			if ($scope.frmCatalog.name.$invalid||$scope.frmCatalog.code.$invalid||$scope.frmCatalog.note.$invalid) {
				$scope.frmCatalog.$dirty = true;
				$scope.frmDirty = true;
				return;
			}

			$scope.showMessageOnToast($translate.instant('clientwh_home_saving'));
			var result;
			$scope.catalog.scope = 'brand';
			$scope.catalog.code = $scope.catalog.code.trim();
			$scope.catalog.name = $scope.catalog.name.trim();
			if ($scope.catalog.note!=null) {
				$scope.catalog.note = $scope.catalog.note.trim();
			}
			if ($scope.catalog.id > -1) {
				result = catalogService.updateWithLock($scope.catalog.id, $scope.catalog);
			} 
			
			else {
				result = catalogService.create($scope.catalog);
			}

			result
			// success.
			.then(function (response) {

				if (response.status === httpStatus.code.OK) {
					if($scope.catalog.id > -1) {
						$scope.catalog.version = response.data;
					} else {
						$scope.catalog.id = response.data;
						$scope.catalog.version = 1;
					}
					$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
					$scope.listWithCriteriasByPage(1);
				} 
				else {
					if(response.data.code==clientwh.serverCode.EXISTCODE){
					$scope.frmCatalog.code.$error.duplicate = true;
					};
					if(response.data.code==clientwh.serverCode.EXISTNAME){
						$scope.frmCatalog.name.$error.duplicate = true;
					};
					if(response.data.code==clientwh.serverCode.EXISTALL){
						$scope.frmCatalog.code.$error.duplicate = true;
						$scope.frmCatalog.name.$error.duplicate = true;
						};
					$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
				}
			},

			// error.
				function (response) {
					$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
				});
		}
		
		// Delete.
		$scope.delete = function(id,version){
				catalogService.updateForDeleteWithLock(id,version)
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
				catalogService.updateForDeleteWithLock($scope.catalog.id, $scope.catalog.version)
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
			catalogService.getById(id,$scope.scope)
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if (response.data!=null) {
						var data = angular.fromJson(response.data);
						$scope.catalog = data;
					}
				} else {
					$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
				}
			},
			// error.
			function(response) {
				$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
			});
		}
		$scope.scope = 'brand';
		// List for page and filter.
		$scope.listWithCriteriasByPage = function(pageNo) {
			$scope.page.currentPage = pageNo;
			catalogService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort(),$scope.scope)
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if (response.data!=null) {
						var result = angular.fromJson(response.data.content);
						$scope.catalogs = result;
						$scope.page.totalElements = 0;
						if(result.length > 0) {
							$scope.page.totalElements = response.data.totalElements;
						}
					}
					else{
						$scope.catalogs = [];
						$scope.page.totalElements = 0;
					}
					
				} else {
					$scope.showMessageOnToastList($translate.instant('clientwh_home_fail'));
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

		/*Extend functions*/
		
		// Sort by.
		$scope.sortBy = function(keyName){
			$scope.sortKey = keyName;
			$scope.reverse = !$scope.reverse;
			// Reload data.
			$scope.listWithCriteriasByPage($scope.page.currentPage);
		}
		
		$scope.clearSortBy = function(){
			$scope.sortKey = '';
			$scope.sortName = null;
			// Reload data.
			$scope.listWithCriteriasByPage($scope.page.currentPage);
		}
		
		$scope.sortByName = clientwh.sortByNameCatalog;
		
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
		    // name.
		    if(typeof($scope.search.key) !== 'undefined' && $scope.search.key !== ''){
		    	result.push({ key: 'name', operation: 'like', value: $scope.search.key, logic: 'or' },{ key: 'code', operation: 'like', value: $scope.search.key, logic: 'or' });
		    }
		    // return.
		    return result;
		}
	}]);
	
});
