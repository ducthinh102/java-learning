
/**
 * Controller for Materialexport
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'materialexportController', ['$scope','$stateParams','$mdBottomSheet', '$mdToast', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader','$q', 
		clientwh.prefix + 'materialexportService',
		clientwh.prefix + 'storeService',
		clientwh.prefix + 'userService',
		clientwh.prefix + 'workflowexecuteService', // workflowexecute module
		clientwh.prefix + 'materialexportdetailService',
		function($scope, $stateParams, $mdBottomSheet, $mdToast, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, $q,
				materialexportService, 
				storeService,
				userService, 
				workflowexecuteService,
				materialexportdetailService
		) { // workflowexecute module
		
		if(typeof(clientwh.translate.materialexport) === 'undefined' || clientwh.translate.materialexport.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.materialexport) === 'undefined') {
				clientwh.translate.materialexport = '';
			}
			clientwh.translate.materialexport += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/materialexport');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_materialexport_title');
		});
		
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
		
	    $translate.onReady().then(function() {
	    	$scope.title = $translate.instant('clientwh_materialexport_title');
	    	$translate.refresh();
	    });
	    
	    //store id
	    $scope.storeIdMaterialexport = function(id){
		    	$rootScope.idMaterialexport = id;
		    	$rootScope.idMaterialexport = null;
		    	$rootScope.idQuotation = null;
		    	$rootScope.idPurchase = null;
		    	$rootScope.idRequest = null;
		    	$state.go(clientwh.prefix + 'selectfordetail');
	    }
	    
	    // materialexportdetailView.
	    $scope.materialexportdetailView = clientwh.contextPath + "/view/materialexportdetail_list.html";
	    
	    // materialexportdetailForView.
	    $scope.materialexportdetailForView = clientwh.contextPath + "/view/materialexportdetail_view.html";
		
	    // Search.
	    $scope.search = {};
	    
	    $scope.materialexport = { id: -1 };
	    
	    // commnetView.
	    $scope.commentOnlyView = clientwh.contextPath + "/view/comment_view.html";
	    
		
	    // comment.
		$scope.commentScope = {
			view: clientwh.contextPath + '/view/comment_list.html',
			idref: $scope.materialexport.id,
			reftype: 'materialexport'
		}
	    
		// Paging.
		$scope.page = {
			pageSize: 9,
			totalElements: 0,
			currentPage: 0
		}
		
		$scope.materialexport = {};
		// Promise list for select.
		var listAllSelectPromise;
	    
	    $scope.showMessageOnToast = function(message){
			$mdToast.show(
					{ template: '<md-toast class="md-toast">' + message + '</md-toast>',
						hideDelay:3000,
						position: 'top right'})
		}
		
	    $scope.cofirmDeleteToastForm = function(){
			$mdToast.show(
					{  	templateUrl : clientwh.contextPath + '/view/toast.html',
						hideDelay:5000,
						controller  : 'clientwhmaterialexportController',
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
						controller  : 'clientwhmaterialexportController',
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
	    
		$scope.showCopyTo = function(id) {
    	    $scope.getById(id);
    	    $scope.getAllByMaterialExport(id);
    	    $mdBottomSheet.show({
    	      templateUrl: clientwh.contextPath + '/view/copyTo_form.html',
    	      controller  : 'clientwhmaterialexportController'
    	    }).then(function(clickedItem) {
    	    	$scope.copyTo(clickedItem);
    	    }).catch(function(error) {
    	      // User clicked outside or hit escape
    	    });
    };
    
    $scope.listItemClick = function(target) {
        $mdBottomSheet.hide(target);
    };

    $scope.targetCopy = clientwh.targetCopy;
		// Init for list.
		$scope.initList = function () {
			if (typeof (listAllSelectPromise) === 'undefined') {
				var listAllSelectDefered = $q.defer();
				listAllSelectPromise = listAllSelectDefered.promise;
				listAllSelectDefered.resolve([]);
			}
			listAllSelectPromise.then(
				//Success
				function (response) {
					// Get permission.
					userService.definePermissionByTarget($scope, 'materialexport').then(
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
				},
				//Error
				function (response) { }
			);
		}
		
		// Init for form.
		$scope.initForm = function(id) {
			$scope.createNew();
			$scope.materialexport.id = id;
			if($scope.materialexport.id > -1) {
				$scope.commentScope.idref = $scope.materialexport.id;
				$scope.getById($scope.materialexport.id);
			}
			$scope.frmDirty = false;
		}
		
		// Show a create screen.
		$scope.showCreate = function() {
			$scope.initForm(-1);
			$scope.showDialog();
			
			$scope.ctlentityfieldStore.selectedItem = null;
			$scope.ctlentityfieldStore.searchText = null;
			
			$scope.ctlentityfieldExporter.selectedItem = null;
			$scope.ctlentityfieldExporter.searchText = null;
			
			$scope.formdetailcopy = {};
		}
		
		// Show a form screen.
		$scope.showForm = function(id) {
			$scope.initForm(id);
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.showDialog();
			$scope.ctlentityfieldStore.selectedItem = null;
			$scope.ctlentityfieldExporter.selectedItem = null;
			
			$scope.ctlentityfieldStore.searchText = null;
			$scope.ctlentityfieldExporter.searchText = null;
		}
		//Show form detail
		$scope.showDetail = function(id){
			$scope.materialexport.id = id;
			$scope.commentScope.idref = $scope.materialexport.id;
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.getByIdForView(id);
			$scope.showDialogDetail();
		}
		
		//Show dialog view
		$scope.showDialogDetail = function () {
	        var htmlUrlTemplate = clientwh.contextPath + '/view/materialexport_view.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }
		
	    // Show edit view.
	    $scope.showDialog = function () {
	        var htmlUrlTemplate = clientwh.contextPath + '/view/materialexport_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }

	    // Create new.
		$scope.createNew = function() {
			$scope.materialexport = { id: -1 };
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.ctlentityfieldStore.selectedItem = null;
			$scope.ctlentityfieldExporter.selectedItem = null;
			
			$scope.ctlentityfieldStore.searchText = null;
			$scope.ctlentityfieldExporter.searchText = null;
		}
		
		// Clear filter system.
		$scope.clearFilterStore = function() {
			$scope.search.store = "";
			$scope.listWithCriteriasByPage(1);
		}
		
		// Reset validate.
		$scope.resetValidate = function() {
			$scope.ctlentityfieldStore.searchText = undefined;
			$scope.ctlentityfieldStore.selectedItem = undefined;
			
			$scope.ctlentityfieldExporter.searchText = undefined;
			$scope.ctlentityfieldExporter.selectedItem = undefined;
			
			// idstore.
		    $scope.frmMaterialexport.idstore.$setPristine();
			$scope.frmMaterialexport.idstore.$setUntouched();
			
			// idstore.
		    $scope.frmMaterialexport.idexporter.$setPristine();
			$scope.frmMaterialexport.idexporter.$setUntouched();
			
			// code.
		    $scope.frmMaterialexport.code.$setPristine();
			$scope.frmMaterialexport.code.$setUntouched();
			
			// exportdate.
		    $scope.frmMaterialexport.exportdate.$setPristine();
			$scope.frmMaterialexport.exportdate.$setUntouched();
			
		    // form.
			$scope.frmMaterialexport.$setPristine();
			$scope.frmMaterialexport.$setUntouched();
			
			$scope.ctlentityfieldStore.selectedItem = null;
			$scope.ctlentityfieldExporter.selectedItem = null;
			
			// frmDirty.
			$scope.frmDirty = false;
			
			$scope.resetValidateDuplicate();
			
		}
		
		// Create on form.
		$scope.createOnForm = function() {
			$scope.createNew();
			$scope.resetValidate();
		}
		$scope.clearForm = function(){
			$scope.materialexport = { id: -1 };
			$scope.resetValidate();
		}
		//////////////////SHOW	////////////////////
		// Show all combo box.
		$scope.listAllForSelect = function () {
			var listAllSelectDeferred = $q.defer();
			// Init data for select.
			$scope.stores = [];
			$scope.exporters = [];
			// Call service
			var listStore = storeService.listAllForSelect();
			var listExporter = userService.listAllForSelect();
			// Response.
			$q.all([listStore,listExporter]).then(
				// Successes.
				function (responses) {
					$scope.stores = responses[0].data;
					$scope.ctlentityfieldExporter.items = responses[1].data;
					$scope.ctlentityfieldStore.items = responses[0].data;
					// Resolve promise.
					listAllSelectDeferred.resolve(responses);
				},
				// Errors.
				function (responses) {
					$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
					// Reject promise.
					listAllSelectDeferred.reject(responses);
				}
			);
			return listAllSelectDeferred.promise;
		}

		// Call and return a promise.
		listAllSelectPromise = $scope.listAllForSelect();
		
		$scope.itemSelected = function () {
			for (var i = 0; i < $scope.ctlentityfieldStore.items.length; i++) {
				if ($scope.materialexport.idstore == $scope.ctlentityfieldStore.items[i].value) {
					$scope.ctlentityfieldStore.selectedItem = {};
					$scope.ctlentityfieldStore.selectedItem = $scope.ctlentityfieldStore.items[i];
					break;
				}
			}
			for (var i = 0; i < $scope.ctlentityfieldExporter.items.length; i++) {
				if ($scope.materialexport.idexporter == $scope.ctlentityfieldExporter.items[i].id) {
					$scope.ctlentityfieldExporter.selectedItem = {};
					$scope.ctlentityfieldExporter.selectedItem = $scope.ctlentityfieldExporter.items[i];
					break;
				}
			}
		}
		
		// Save.
		$scope.save = function() {
			if($scope.ctlentityfieldStore.selectedItem==null){
				$scope.frmMaterialexport.idstore.$invalid = true;
				$scope.frmMaterialexport.idstore.$touched = true;
			}
			if ($scope.materialexport.exportdate==null) {
				$scope.frmMaterialexport.exportdate.$invalid = true;
				$scope.frmMaterialexport.exportdate.$touched = true;
			}
			if ($scope.ctlentityfieldExporter.selectedItem==null) {
				$scope.frmMaterialexport.idexporter.$invalid = true;
				$scope.frmMaterialexport.idexporter.$touched = true;
			}
			if($scope.frmMaterialexport.$invalid||$scope.frmMaterialexport.idexporter.$invalid||$scope.frmMaterialexport.idstore.$invalid||$scope.frmMaterialexport.exportdate.$invalid||$scope.frmMaterialexport.code.$invalid) {
				$scope.frmMaterialexport.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			$scope.showMessageOnToast($translate.instant('clientwh_home_saving'));
			var result;
			if($scope.materialexport.id > -1) {
				result = materialexportService.updateWithLock($scope.materialexport.id, $scope.materialexport);
			} else {
				result = materialexportService.create($scope.materialexport);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.materialexport.id > -1) {
						$scope.materialexport.version = response.data;
					} else {
						$scope.materialexport.id = response.data;
						$scope.materialexport.version = 1;
					}
					$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
					// comment.
					$scope.commentScope.idref = $scope.materialexport.id;
					$scope.listWithCriteriasByPage(1);
				} else {
					if(response.data.code == clientwh.serverCode.EXISTCODE) {
						$scope.frmMaterialexport.code.$error.duplicate = true;
						$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
					} else if(response.data.code == clientwh.serverCode.VERSIONDIFFERENCE) {
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
		
		////////////////////////////////////////
		// Auto complete: loadItemsData
		////////////////////////////////////////
		$scope.loadItemsData = function (dataResponse) {
			var items = [];
			for (var i = 0; i < dataResponse.length; i++) {
				var item = { value: '', display: '', code: '' };
				item.value = dataResponse[i].id;
				item.display = dataResponse[i].name;
				item.code = dataResponse[i].code;
				items.push(item);
			}
			return items;
		}
		
		//Reset validate duplicate
	    $scope.resetValidateDuplicate = function(){
	    	delete $scope.frmMaterialexport.code.$error.duplicate;
	    	$scope.frmMaterialexport.code.$invalid = false;
	    	$scope.frmMaterialexport.code.$valid = true;
	    }
		// Delete.
		$scope.delete = function(id, version) {
				materialexportService.updateForDeleteWithLock(id, version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToast($translate.instant('clientwh_home_deleted'));
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
		
		// Delete with create.
		$scope.deleteOnForm = function() {
				materialexportService.updateForDeleteWithLock($scope.materialexport.id, $scope.materialexport.version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToast($translate.instant('clientwh_home_deleted'));
						$scope.createNew();
						$scope.resetValidate();
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
			materialexportService.getById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.materialexport = data;
					$scope.materialexport.exportdate = new Date($scope.materialexport.exportdate);
					$scope.itemSelected();
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
		$scope.getByIdForView = function(id) {
			materialexportService.getByIdForView(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.materialexportView = data;
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
			//materialexportService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// workflowexecute module
			workflowexecuteService.listWithCriteriasByPage($state, $scope.workflowTab.id, $scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.materialexports = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.materialexports = result;
						for (var i = 0; i < $scope.materialexports.length; i++) {
							if ($scope.materialexports[i].exportdate!=null) {
								$scope.materialexports[i].exportdate = new Date($scope.materialexports[i].exportdate).toLocaleDateString();
							}
						}
						if(result.length > 0) {
							$scope.page.totalElements = response.data.totalElements;
						}
					}
				} else {
					$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
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
		}
		// Clear filter search.
		$scope.clearFilterSearch = function() {
			$scope.search.key = "";
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
		
		$scope.sortByName = clientwh.sortByNameMaterialExport;
		
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
		    if(typeof($scope.search.key) !== 'undefined' && $scope.search.key !== ''&&typeof($scope.search.store) !== 'undefined' && $scope.search.store !== ''&&$scope.search.store!=null){
		    	result.push({ key: 'idstore', operation: '=', value: $scope.search.store, logic: 'and' },{ key: 'code', operation: 'like', value: $scope.search.key, logic: 'or' });
		    }
		    else{
		    	// system
			    if(typeof($scope.search.store) !== 'undefined' && $scope.search.store !== ''&&$scope.search.store!=null){
			    	result.push({ key: 'idstore', operation: '=', value: $scope.search.store, logic: 'or' },);
			    }
			   
			    // username.
			    if(typeof($scope.search.key) !== 'undefined' && $scope.search.key !== ''){
			    	result.push({ key: 'code', operation: 'like', value: $scope.search.key, logic: 'or' },{ key: 'store.name', operation: 'like', value: $scope.search.key, logic: 'or' });
			    }
		    }
		    // return.
		    return result;
		}
		
		////////////////////////////////////////
		// Auto complete: idstore
		////////////////////////////////////////
		$scope.ctlentityfieldStore = {};
		$scope.ctlentityfieldStore.isCallServer = false;
		$scope.ctlentityfieldStore.isDisabled = false;

		// New.
		$scope.ctlentityfieldStore.newState = function (item) {
			alert("Sorry! You'll need to create a Constitution for " + item + " first!");
		}
		// Search in array.
		$scope.ctlentityfieldStore.querySearch = function (query) {
			var results = query ? $scope.ctlentityfieldStore.items.filter($scope.ctlentityfieldStore.createFilterFor(query)) : $scope.ctlentityfieldStore.items,
				deferred;

			if ($scope.ctlentityfieldStore.isCallServer) {
				deferred = $q.defer();
				$timeout(function () { deferred.resolve(results); }, Math.random() * 1000, false);
				return deferred.promise;
			} else {
				return results;
			}
		}
		// Filter.
		$scope.ctlentityfieldStore.createFilterFor = function (query) {
			var lowercaseQuery = angular.lowercase(query);
			return function filterFn(item) {
				return (angular.lowercase(item.display).indexOf(lowercaseQuery) >= 0);
			};
		}
		// Text change.
		$scope.ctlentityfieldStore.searchTextChange = function (text) {
			$log.info('Text changed to ' + text);
		}
		// Item change.
		$scope.ctlentityfieldStore.selectedItemChange = function (item) {
			if (typeof(frmMaterialexport) === 'undefined' || frmMaterialexport.idstore === undefined) {
				return;
			}
			$scope.materialexport.idstore = undefined;
			$scope.frmMaterialexport.idstore.$invalid = true;
			if (item) {
				$scope.materialexport.idstore = item.value;
				$scope.frmMaterialexport.idstore.$invalid = false;
			}
		}
		
		////////////////////////////////////////
		// Auto complete: idexporter
		////////////////////////////////////////
		$scope.ctlentityfieldExporter = {};
		$scope.ctlentityfieldExporter.isCallServer = false;
		$scope.ctlentityfieldExporter.isDisabled = false;

		// New.
		$scope.ctlentityfieldExporter.newState = function (item) {
			alert("Sorry! You'll need to create a Constitution for " + item + " first!");
		}
		// Search in array.
		$scope.ctlentityfieldExporter.querySearch = function (query) {
			var results = query ? $scope.ctlentityfieldExporter.items.filter($scope.ctlentityfieldExporter.createFilterFor(query)) : $scope.ctlentityfieldExporter.items,
				deferred;

			if ($scope.ctlentityfieldExporter.isCallServer) {
				deferred = $q.defer();
				$timeout(function () { deferred.resolve(results); }, Math.random() * 1000, false);
				return deferred.promise;
			} else {
				return results;
			}
		}
		// Filter.
		$scope.ctlentityfieldExporter.createFilterFor = function (query) {
			var lowercaseQuery = angular.lowercase(query);
			return function filterFn(item) {
				return (angular.lowercase(item.display).indexOf(lowercaseQuery) >= 0);
			};
		}
		// Text change.
		$scope.ctlentityfieldExporter.searchTextChange = function (text) {
			$log.info('Text changed to ' + text);
		}
		// Item change.
		$scope.ctlentityfieldExporter.selectedItemChange = function (item) {
			if (typeof(frmMaterialexport) === 'undefined' || frmMaterialexport.idexporter === undefined) {
				return;
			}
			$scope.materialexport.idexporter = undefined;
			$scope.frmMaterialexport.idexporter.$invalid = true;
			if (item) {
				$scope.materialexport.idexporter = item.id;
				$scope.frmMaterialexport.idexporter.$invalid = false;
			}
		}
	
		$scope.getAllByMaterialExport = function(idmaterialexport){
    		materialexportdetailService.getAllById(idmaterialexport).then(function(response){
    			$scope.materialexportdetails = response.data;
    		},function(response){
    			
    		})
    	}
    	
    	$scope.copyTo = function(item){
    		if (item=='materialformprematerial') {
    			$state.go(clientwh.prefix + 'materialform', {scope:'prematerial',reftype: 'materialexport', formcopy: $scope.materialexport, formdetailcopy: $scope.materialexportdetails});
			}
    		else if (item=='materialformtechmaterial') {
    			$state.go(clientwh.prefix + 'materialform', {scope:'techmaterial',reftype: 'materialexport', formcopy: $scope.materialexport, formdetailcopy: $scope.materialexportdetails});
			}
    		else if(item=='requestnormal'){
    			$state.go(clientwh.prefix + 'request', {scope:'normal',reftype: 'materialexport', formcopy: $scope.materialexport, formdetailcopy: $scope.materialexportdetails});
    		}
    		else if(item=='requestextra'){
    			$state.go(clientwh.prefix + 'request', {scope:'extra',reftype: 'materialexport', formcopy: $scope.materialexport, formdetailcopy: $scope.materialexportdetails});
    		}
    		else{
    			$state.go(clientwh.prefix + item, {reftype: 'materialexport', formcopy: $scope.materialexport, formdetailcopy: $scope.materialexportdetails});
    		}
	    }

    	
    	$scope.formcopy = {};
		$scope.formdetailcopy = {};
	    if($stateParams.formcopy&& typeof $scope.workflowTab !== "undefined"&&$scope.workflowTab.id==="created"){
		    $scope.formcopy = $stateParams.formcopy;
		    $scope.formdetailcopy = $stateParams.formdetailcopy;
		    
		    $scope.materialexport.idref = $stateParams.formcopy.id;
		    $scope.materialexport.reftype = $stateParams.reftype;
		    
		    $scope.materialexport.id = -1;
		    $scope.materialexport.code = $stateParams.formcopy.code;
		    $scope.materialexport.name = $stateParams.formcopy.name;
		    $scope.materialexport.idstore = $stateParams.formcopy.idstore;
	    	$scope.materialexport.idexporter = $stateParams.formcopy.idexporter;
	    	$scope.materialexport.exportdate = $stateParams.formcopy.exportdate;
		    $scope.materialexport.note = $stateParams.formcopy.note;
		    if (typeof (listAllSelectPromise) === 'undefined') {
				var listAllSelectDefered = $q.defer();
				listAllSelectPromise = listAllSelectDefered.promise;
				listAllSelectDefered.resolve([]);
			}
		    listAllSelectPromise.then(
		 	//Success
		 	function (response) {
		 		$scope.itemSelected();
		 		$scope.showDialog();
		 	},
		 	//Error
		 	function (response) { }
		    );
	    }

    	
	}]);

});
