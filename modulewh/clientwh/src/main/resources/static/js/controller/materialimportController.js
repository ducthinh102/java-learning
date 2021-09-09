
/**
 * Controller for Materialimport
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'materialimportController', ['$scope', '$mdBottomSheet', '$mdToast', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader','$q', '$stateParams', clientwh.prefix + 'materialimportService',clientwh.prefix + 'storeService',clientwh.prefix + 'userService', clientwh.prefix + 'workflowexecuteService', clientwh.prefix + 'materialimportdetailService',// workflowexecute module
		function($scope, $mdBottomSheet, $mdToast, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, $q, $stateParams, materialimportService, storeService,userService, workflowexecuteService, materialimportdetailService) { // workflowexecute module
		if(typeof(clientwh.translate.materialimport) === 'undefined' || clientwh.translate.materialimport.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.materialimport) === 'undefined') {
				clientwh.translate.materialimport = '';
			}
			clientwh.translate.materialimport += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/materialimport');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_materialimport_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    $translate.onReady().then(function() {
	    	$scope.title = $translate.instant('clientwh_materialimport_title');
	    	$translate.refresh();
	    });
	    
	    //store id
	    $scope.storeIdMaterialimport = function(id){
		    	$rootScope.idMaterialimport = id;
		    	$rootScope.idMaterialexport = null;
		    	$rootScope.idQuotation = null;
		    	$rootScope.idPurchase = null;
		    	$rootScope.idRequest = null;
		    	$state.go(clientwh.prefix + 'selectfordetail');
	    }
	    
	    
	    // materialimportdetailView.
	    $scope.materialimportdetailView = clientwh.contextPath + "/view/materialimportdetail_list.html";
	    
	    // materialimportdetailForView.
	    $scope.materialimportdetailForView = clientwh.contextPath + "/view/materialimportdetail_view.html";
		
	    // Search.
	    $scope.search = {};
	    
		// Paging.
		$scope.page = {
			pageSize: 9,
			totalElements: 0,
			currentPage: 0
		}
		$scope.materialimport = { id: -1 };
		
		// commnetView.
	    $scope.commentOnlyView = clientwh.contextPath + "/view/comment_view.html";
	    
		
	    // comment.
		$scope.commentScope = {
			view: clientwh.contextPath + '/view/comment_list.html',
			idref: $scope.materialimport.id,
			reftype: 'materialimport'
		}
		
		
		// Promise list for select.
		var listAllSelectPromise;
	    
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
						controller  : 'clientwhmaterialimportController',
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
						controller  : 'clientwhmaterialimportController',
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
					userService.definePermissionByTarget($scope, 'materialimport').then(
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
			$scope.materialimport.id = id;
			if($scope.materialimport.id > -1) {
				$scope.commentScope.idref = $scope.materialimport.id;
				$scope.getById($scope.materialimport.id);
			}
			$scope.frmDirty = false;
		}
		
		// Show a create screen.
		$scope.showCreate = function() {
			$scope.initForm(-1);
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.showDialog();
			
			$scope.ctlentityfieldStore.selectedItem = null;
			$scope.ctlentityfieldStore.searchText = null;
			
			$scope.ctlentityfieldImporter.selectedItem = null;
			$scope.ctlentityfieldImporter.searchText = null;
		}
		
		// Show a form screen.
		$scope.showForm = function(id) {
			$scope.initForm(id);
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.showDialog();
			$scope.ctlentityfieldStore.selectedItem = null;
			$scope.ctlentityfieldImporter.selectedItem = null;
			
			$scope.ctlentityfieldStore.searchText = null;
			$scope.ctlentityfieldImporter.searchText = null;
		}
		
		//Show form detail
		$scope.showDetail = function(id){
			$scope.materialimport.id = id;
			$scope.getByIdForView(id);
			$scope.commentScope.idref = $scope.materialimport.id;
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.showDialogDetail();
		}
		
		//Show dialog view
		$scope.showDialogDetail = function () {
	        var htmlUrlTemplate = clientwh.contextPath + '/view/materialimport_view.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }
		
	    // Show edit view.
	    $scope.showDialog = function () {
	        var htmlUrlTemplate = clientwh.contextPath + '/view/materialimport_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }

	    $scope.showCopyTo = function(id) {
    	    $scope.getById(id);
    	    $scope.getAllByMaterialImport(id);
    	    $mdBottomSheet.show({
    	      templateUrl: clientwh.contextPath + '/view/copyTo_form.html',
    	      controller  : 'clientwhmaterialimportController'
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
	    // Create new.
		$scope.createNew = function() {
			$scope.materialimport = { id: -1 };
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.ctlentityfieldStore.selectedItem = null;
			$scope.ctlentityfieldImporter.selectedItem = null;
			
			$scope.ctlentityfieldStore.searchText = null;
			$scope.ctlentityfieldImporter.searchText = null;
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
			
			$scope.ctlentityfieldImporter.searchText = undefined;
			$scope.ctlentityfieldImporter.selectedItem = undefined;
			
			// idstore.
		    $scope.frmMaterialimport.idstore.$setPristine();
			$scope.frmMaterialimport.idstore.$setUntouched();
			
			// idstore.
		    $scope.frmMaterialimport.idimporter.$setPristine();
			$scope.frmMaterialimport.idimporter.$setUntouched();
			
			// code.
		    $scope.frmMaterialimport.code.$setPristine();
			$scope.frmMaterialimport.code.$setUntouched();
			$scope.frmMaterialimport.code.$modelValue = null;
			
			
			//note
			$scope.frmMaterialimport.note.$setPristine();
			$scope.frmMaterialimport.note.$setUntouched();
			$scope.frmMaterialimport.note.$modelValue = null;
			
			// importdate.
		    $scope.frmMaterialimport.importdate.$setPristine();
			$scope.frmMaterialimport.importdate.$setUntouched();
			
		    // form.
			$scope.frmMaterialimport.$setPristine();
			$scope.frmMaterialimport.$setUntouched();
			
			$scope.ctlentityfieldStore.selectedItem = null;
			$scope.ctlentityfieldImporter.selectedItem = null;
			
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
			$scope.materialimport = { id: -1 };
			$scope.resetValidate();
		}
		//////////////////SHOW	////////////////////
		// Show all combo box.
		$scope.listAllForSelect = function () {
			var listAllSelectDeferred = $q.defer();
			// Init data for select.
			$scope.stores = [];
			$scope.importers = [];
			// Call service
			var listStore = storeService.listAllForSelect();
			var listImporter = userService.listAllForSelect();
			// Response.
			$q.all([listStore,listImporter]).then(
				// Successes.
				function (responses) {
					$scope.stores = responses[0].data;
					$scope.ctlentityfieldImporter.items = responses[1].data;
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
				if ($scope.materialimport.idstore == $scope.ctlentityfieldStore.items[i].value) {
					$scope.ctlentityfieldStore.selectedItem = {};
					$scope.ctlentityfieldStore.selectedItem = $scope.ctlentityfieldStore.items[i];
					break
				}
			}
			for (var i = 0; i < $scope.ctlentityfieldImporter.items.length; i++) {
				if ($scope.materialimport.idimporter == $scope.ctlentityfieldImporter.items[i].id) {
					$scope.ctlentityfieldImporter.selectedItem = {};
					$scope.ctlentityfieldImporter.selectedItem = $scope.ctlentityfieldImporter.items[i];
					break
				}
			}
		}
		
		// Save.
		$scope.save = function() {
			if($scope.ctlentityfieldStore.selectedItem==null){
				$scope.frmMaterialimport.idstore.$invalid = true;
				$scope.frmMaterialimport.idstore.$touched = true;
			}
			if ($scope.materialimport.importdate==null) {
				$scope.frmMaterialimport.importdate.$invalid = true;
				$scope.frmMaterialimport.importdate.$touched = true;
			}
			if ($scope.ctlentityfieldImporter.selectedItem==null) {
				$scope.frmMaterialimport.idimporter.$invalid = true;
				$scope.frmMaterialimport.idimporter.$touched = true;
			}
			if($scope.frmMaterialimport.$invalid||$scope.frmMaterialimport.idimporter.$invalid||$scope.frmMaterialimport.idstore.$invalid||$scope.frmMaterialimport.importdate.$invalid||$scope.frmMaterialimport.code.$invalid||$scope.frmMaterialimport.note.$invalid) {
				$scope.frmMaterialimport.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			$scope.showMessageOnToast($translate.instant('clientwh_home_saving'));
			// Ignore time
			$scope.materialimport.importdate = clientmain.getDateIgnoreTime($scope.materialimport.importdate)
			if ($scope.materialimport.note!=null) {
				$scope.materialimport.note = $scope.materialimport.note.trim()
			}
			$scope.materialimport.code = $scope.materialimport.code.trim()
			var result;
			if($scope.materialimport.id > -1) {
				result = materialimportService.updateWithLock($scope.materialimport.id, $scope.materialimport);
			} else {
				result = materialimportService.create($scope.materialimport);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.materialimport.id > -1) {
						$scope.materialimport.version = response.data;
					} else {
						$scope.materialimport.id = response.data;
						$scope.materialimport.version = 1;
					}
					$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
					// comment.
					$scope.commentScope.idref = $scope.materialimport.id;
					$scope.listWithCriteriasByPage(1);
				} else {
					if(response.data.code == clientwh.serverCode.EXISTCODE) {
						$scope.frmMaterialimport.code.$error.duplicate = true;
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
	    	delete $scope.frmMaterialimport.code.$error.duplicate;
	    	if ($scope.frmMaterialimport.code.$error.required||$scope.frmMaterialimport.code.$error.pattern) {
	    		$scope.frmMaterialimport.code.$invalid = true;
		    	$scope.frmMaterialimport.code.$valid = false;
			}
	    	else {
	    		$scope.frmMaterialimport.code.$invalid = false;
		    	$scope.frmMaterialimport.code.$valid = true;
	    	}
	    }
		// Delete.
		$scope.delete = function(id, version) {
				materialimportService.updateForDeleteWithLock(id, version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToastList($translate.instant('clientwh_home_deleted'));
						$scope.listWithCriteriasByPage(1);
					} else {
						$scope.showMessageOnToastList($translate.instant('clientwh_home_error'));
					}
				},
				// error.
				function(response) {
					$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
				});
		}
		
		// Delete with create.
		$scope.deleteOnForm = function() {
				materialimportService.updateForDeleteWithLock($scope.materialimport.id, $scope.materialimport.version)
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
			materialimportService.getById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.materialimport = data;
					$scope.materialimport.importdate = new Date($scope.materialimport.importdate);
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
			materialimportService.getByIdForView(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.materialimportView = data;
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
			//materialimportService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// workflowexecute module
			workflowexecuteService.listWithCriteriasByPage($state, $scope.workflowTab.id, $scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.materialimports = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.materialimports = result;
						for (var i = 0; i < $scope.materialimports.length; i++) {
							if ($scope.materialimports[i].importdate!=null) {
								$scope.materialimports[i].importdate = new Date($scope.materialimports[i].importdate).toLocaleDateString();
							}
						}
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
		
		$scope.sortByName = clientwh.sortByNameMaterialImport;
		
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
			if (typeof(frmMaterialimport) === 'undefined' || frmMaterialimport.idstore === undefined) {
				return;
			}
			$scope.materialimport.idstore = undefined;
			$scope.frmMaterialimport.idstore.$invalid = true;
			if (item) {
				$scope.materialimport.idstore = item.value;
				$scope.frmMaterialimport.idstore.$invalid = false;
			}
		}
		
		////////////////////////////////////////
		// Auto complete: idimporter
		////////////////////////////////////////
		$scope.ctlentityfieldImporter = {};
		$scope.ctlentityfieldImporter.isCallServer = false;
		$scope.ctlentityfieldImporter.isDisabled = false;

		// New.
		$scope.ctlentityfieldImporter.newState = function (item) {
			alert("Sorry! You'll need to create a Constitution for " + item + " first!");
		}
		// Search in array.
		$scope.ctlentityfieldImporter.querySearch = function (query) {
			var results = query ? $scope.ctlentityfieldImporter.items.filter($scope.ctlentityfieldImporter.createFilterFor(query)) : $scope.ctlentityfieldImporter.items,
				deferred;

			if ($scope.ctlentityfieldImporter.isCallServer) {
				deferred = $q.defer();
				$timeout(function () { deferred.resolve(results); }, Math.random() * 1000, false);
				return deferred.promise;
			} else {
				return results;
			}
		}
		// Filter.
		$scope.ctlentityfieldImporter.createFilterFor = function (query) {
			var lowercaseQuery = angular.lowercase(query);
			return function filterFn(item) {
				return (angular.lowercase(item.display).indexOf(lowercaseQuery) >= 0);
			};
		}
		// Text change.
		$scope.ctlentityfieldImporter.searchTextChange = function (text) {
			$log.info('Text changed to ' + text);
		}
		// Item change.
		$scope.ctlentityfieldImporter.selectedItemChange = function (item) {
			if (typeof(frmMaterialimport) === 'undefined' || frmMaterialimport.idimporter === undefined) {
				return;
			}
			$scope.materialimport.idimporter = undefined;
			$scope.frmMaterialimport.idimporter.$invalid = true;
			if (item) {
				$scope.materialimport.idimporter = item.id;
				$scope.frmMaterialimport.idimporter.$invalid = false;
			}
		}
		
		$scope.getAllByMaterialImport = function(idmaterialimport){
    		materialimportdetailService.getAllById(idmaterialimport).then(function(response){
    			$scope.materialimportdetails = response.data;
    		},function(response){
    			
    		})
    	}
    	
    	$scope.copyTo = function(item){
    		if (item=='materialformprematerial') {
    			$state.go(clientwh.prefix + 'materialform', {scope:'prematerial',reftype: 'materialimport', formcopy: $scope.materialimport, formdetailcopy: $scope.materialimportdetails});
			}
    		else if (item=='materialformtechmaterial') {
    			$state.go(clientwh.prefix + 'materialform', {scope:'techmaterial',reftype: 'materialimport', formcopy: $scope.materialimport, formdetailcopy: $scope.materialimportdetails});
			}
    		else if(item=='requestnormal'){
    			$state.go(clientwh.prefix + 'request', {scope:'normal',reftype: 'materialimport', formcopy: $scope.materialimport, formdetailcopy: $scope.materialimportdetails});
    		}
    		else if(item=='requestextra'){
    			$state.go(clientwh.prefix + 'request', {scope:'extra',reftype: 'materialimport', formcopy: $scope.materialimport, formdetailcopy: $scope.materialimportdetails});
    		}
    		else{
    			$state.go(clientwh.prefix + item, {reftype: 'materialimport', formcopy: $scope.materialimport, formdetailcopy: $scope.materialimportdetails});
    		}
	    }
    	
		$scope.formcopy = {};
		$scope.formdetailcopy = {};
	    if($stateParams.formcopy&& typeof $scope.workflowTab !== "undefined"&&$scope.workflowTab.id==="created"){
		    $scope.formcopy = $stateParams.formcopy;
		    $scope.formdetailcopy = $stateParams.formdetailcopy;
		    
		    $scope.materialimport.idref = $stateParams.formcopy.id;
		    $scope.materialimport.reftype = $stateParams.reftype;
		    
		    $scope.materialimport.id = -1;
		    $scope.materialimport.code = $stateParams.formcopy.code;
		    $scope.materialimport.name = $stateParams.formcopy.name;
		    $scope.materialimport.idstore = $stateParams.formcopy.idstore;
	    	$scope.materialimport.idimporter = $stateParams.formcopy.idimporter;
	    	$scope.materialimport.importdate = $stateParams.formcopy.importdate;
	    	$scope.materialimport.note = $stateParams.formcopy.note;
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
