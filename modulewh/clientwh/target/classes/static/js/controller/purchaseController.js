
/**
 * Controller for Purchase
 */

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'purchaseController', ['moment', '$q', '$scope','$stateParams','$mdBottomSheet', '$mdToast', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', clientwh.prefix + 'purchaseService', clientwh.prefix + 'userService', clientwh.prefix + 'materialService', clientwh.prefix + 'storeService', clientwh.prefix + 'supplierService', clientwh.prefix + 'userService', clientwh.prefix + 'workflowexecuteService', clientwh.prefix + 'purchasedetailService',// workflowexecute module
		function(moment, $q, $scope, $stateParams ,$mdBottomSheet, $mdToast, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, purchaseService, userService, materialService, storeService, supplierService, userService, workflowexecuteService, purchasedetailService) { // workflowexecute module
		if(typeof(clientwh.translate.purchase) === 'undefined' || clientwh.translate.purchase.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.purchase) === 'undefined') {
				clientwh.translate.purchase = '';
			}
			clientwh.translate.purchase += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/purchase');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_purchase_title');
		});
		
		// Unregister
		$scope.$on('$destroy', function () {
			$scope.closeToast();
		    unRegister();
		});	
		
	    $translate.onReady().then(function() {
	    	$scope.title = $translate.instant('clientwh_purchase_title');
	    	$translate.refresh();
	    });
	    
	    // store id
	    $scope.storeIdPurchase = function(id){
	    	$rootScope.idQuotation = null;
	    	$rootScope.idMaterialimport = null;
	    	$rootScope.idMaterialexport = null;
	    	$rootScope.idPurchase = id;
	    	$rootScope.idRequest = null;
	    	$state.go(clientwh.prefix + 'selectfordetail');
	    }
	    
	    // purchasedetailEditView.
	    $scope.purchasedetailView = clientwh.contextPath + "/view/purchasedetail_list.html";
	    
	    // purchasedetailEditView.
	    $scope.purchasedetailOnlyview = clientwh.contextPath + "/view/purchasedetail_view_list.html";

	    // Search.
	    $scope.search = {};
	    
		// Paging.
		$scope.page = {
			pageSize: 9,
			totalElements: 0,
			currentPage: 0
		}
		
		// Set current date
		$scope.newdate = function () {
			$scope.purchase.deliverydate = new Date();
		}
		
		$scope.noSunday = function (date) {
			var day = date.getDay();
			return day === 1 || day === 2 || day === 3 || day === 4 || day === 5 || day === 6;
		}
		
		$scope.formatDate = function(dtime) {
			return moment(dtime).format('L')
		}
		
		$scope.purchase = { id: -1 };
		
		// comment.
		$scope.commentScope = {
			view: clientwh.contextPath + '/view/comment_list.html',
			onlyview: clientwh.contextPath + '/view/comment_view.html',
			idref: $scope.purchase.id,
			reftype: 'purchase'
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
						controller  : 'clientwhpurchaseController',
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
						controller  : 'clientwhpurchaseController',
						position: 'right'}).then(function(response) {
							if (response) {
								  $scope.delete(id,version);
							}
						});
		}
		
		$scope.closeFormDialog = function(){
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
				// Success
				function (response) {
					// Get permission.
					userService.definePermissionByTarget($scope, 'purchase').then(
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
				// Error
				function (response) { }
			);
		}
		
		// Init for form.
		$scope.initForm = function(id) {
			if(typeof(listAllSelectPromise) === 'undefined') {
				var listAllSelectDefered = $q.defer();
				listAllSelectPromise = listAllSelectDefered.promise;
				listAllSelectDefered.resolve([]);
			}
			listAllSelectPromise.then(
				// Success.
				function(response) {
					$scope.createNew();
					$scope.purchase.id = id;
					if($scope.purchase.id > -1) {
						$scope.commentScope.idref = $scope.purchase.id;
						$scope.getById($scope.purchase.id);
					}
					
					$scope.frmDirty = false;
				},
				// Error
				function(response) {
					
				}
			);
		}
			
		// Show a create screen.
		$scope.showCreate = function() {
			$scope.initForm(-1);
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.showFormDialog();
			
			$scope.ctlentityfieldStore.selectedItem = null;
			$scope.ctlentityfieldContact.selectedItem = null;
			$scope.ctlentityfieldReceiver.selectedItem = null;
			$scope.ctlentityfieldSupplier.selectedItem = null;
			
			$scope.ctlentityfieldSupplier.searchText = null;
			$scope.ctlentityfieldStore.searchText = null;
			$scope.ctlentityfieldContact.searchText = null;
			$scope.ctlentityfieldReceiver.searchText = null;
		}
		
		// Show detail
		$scope.showView = function(id) {
			$scope.purchase.id = id;
			if($scope.purchase.id > -1) {
				$scope.commentScope.idref = $scope.purchase.id;
				$scope.getById($scope.purchase.id);
				$scope.formcopy = {};
				$scope.formdetailcopy = {};
			}
			$scope.showViewDialog();
		}
		
		// Show detail purchase
		$scope.showViewDialog = function () {
			var htmlUrlTemplate = clientwh.contextPath + '/view/purchase_view.html';
			clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function (evt) {
				console.log('closed');
			}, function (evt) {
				console.log('not closed');
			});
		}
		
		
		
		// Show a form screen.
		$scope.showForm = function(id) {
			$scope.initForm(id);
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.showFormDialog();
			
			$scope.ctlentityfieldStore.selectedItem = null;
			$scope.ctlentityfieldContact.selectedItem = null;
			$scope.ctlentityfieldReceiver.selectedItem = null;
			$scope.ctlentityfieldSupplier.selectedItem = null;
			
			$scope.ctlentityfieldSupplier.searchText = null;
			$scope.ctlentityfieldStore.searchText = null;
			$scope.ctlentityfieldContact.searchText = null;
			$scope.ctlentityfieldReceiver.searchText = null;
		}

	    // Show edit view.
	    $scope.showFormDialog = function () {
	    	$mdToast.hide();
	        var htmlUrlTemplate = clientwh.contextPath + '/view/purchase_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }

	    $scope.showCopyTo = function(id) {
    	    $scope.getById(id);
    	    $scope.getAllByPurchase(id);
    	    $mdBottomSheet.show({
    	      templateUrl: clientwh.contextPath + '/view/copyTo_form.html',
    	      controller  : 'clientwhpurchaseController'
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
		$scope.createOnForm = function() {
			// Clear all field
			$scope.purchase = { id: -1 };
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.newdate();
			$scope.resetValidate();
		}
		
		// Reset validate.
		$scope.resetValidate = function() {
			
			$scope.ctlentityfieldStore.searchText = undefined;
			$scope.ctlentityfieldStore.selectedItem = undefined;
			$scope.ctlentityfieldContact.searchText = undefined;
			$scope.ctlentityfieldContact.selectedItem = undefined;
			$scope.ctlentityfieldReceiver.searchText = undefined;
			$scope.ctlentityfieldReceiver.selectedItem = undefined;
			$scope.ctlentityfieldSupplier.searchText = undefined;
			$scope.ctlentityfieldSupplier.selectedItem = undefined;
			// idreceiver.
		    $scope.frmPurchase.idreceiver.$setPristine();
			$scope.frmPurchase.idreceiver.$setUntouched();
			// idcontact.
		    $scope.frmPurchase.idcontact.$setPristine();
			$scope.frmPurchase.idcontact.$setUntouched();
			// idsupplier.
		    $scope.frmPurchase.idsupplier.$setPristine();
			$scope.frmPurchase.idsupplier.$setUntouched();
			// idstore.
		    $scope.frmPurchase.idstore.$setPristine();
			$scope.frmPurchase.idstore.$setUntouched();
			// deliverydate.
		    $scope.frmPurchase.deliverydate.$setPristine();
			$scope.frmPurchase.deliverydate.$setUntouched();
			// code.
		    $scope.frmPurchase.code.$setPristine();
			$scope.frmPurchase.code.$setUntouched();
	
		    // form.
			$scope.frmPurchase.$setPristine();
			$scope.frmPurchase.$setUntouched();
			
			$scope.frmPurchase.idreceiver.$invalid = false;
			$scope.frmPurchase.idcontact.$invalid = false;
			$scope.frmPurchase.idsupplier.$invalid = false;
			$scope.frmPurchase.idstore.$invalid = false;
			
			$scope.ctlentityfieldStore.searchText = null;
			$scope.ctlentityfieldContact.searchText = null;
			$scope.ctlentityfieldReceiver.searchText = null;
			$scope.ctlentityfieldSupplier.searchText = null;
			
			$scope.ctlentityfieldStore.selectedItem = null;
			$scope.ctlentityfieldContact.selectedItem = null;
			$scope.ctlentityfieldReceiver.selectedItem = null;
			$scope.ctlentityfieldSupplier.selectedItem = null;
			$scope.frmDirty = false;
			
			$scope.resetValidateDuplicate();
		}
			
		// Create new.
	    $scope.createNew = function() {
	    	$scope.purchase = { id: -1 };
	    	$scope.formcopy = {};
			$scope.formdetailcopy = {};
	    	$scope.ctlentityfieldStore.searchText = null;
			$scope.ctlentityfieldContact.searchText = null;
			$scope.ctlentityfieldReceiver.searchText = null;
			$scope.ctlentityfieldSupplier.searchText = null;
			
			$scope.ctlentityfieldStore.selectedItem = null;
			$scope.ctlentityfieldContact.selectedItem = null;
			$scope.ctlentityfieldReceiver.selectedItem = null;
			$scope.ctlentityfieldSupplier.selectedItem = null;
	    }
		    
	    // Reset validate duplicate
	    $scope.resetValidateDuplicate = function() {
	    	delete $scope.frmPurchase.code.$error.duplicate;
	    	if ($scope.frmPurchase.code.$error.required) {
	    		$scope.frmPurchase.code.$invalid = true;
		    	$scope.frmPurchase.code.$valid = false;
			}
	    	else {
	    		$scope.frmPurchase.code.$invalid = false;
		    	$scope.frmPurchase.code.$valid = true;
	    	}
	    }
			
		// ///////////////////////////////////
		// CALL
		$scope.listAllForSelect = function () {
			var listAllSelectDeferred = $q.defer();
			// Init data for select.
			$scope.stores = [];
			$scope.contacts = [];
			$scope.receivers = [];
			$scope.suppliers = [];
			// Call service
			var listStore = storeService.loadallStore();
			var listContact = userService.listAllForSelect();
			var listSupplier = supplierService.listAllForSelect();
			// Response.
			$q.all([listStore, listContact, listSupplier]).then(
				// Successes.
				function (responses) {
					$scope.stores = responses[0].data;
					$scope.contacts = responses[1].data;
					$scope.receivers = responses[1].data;
					$scope.suppliers = responses[2].data;
					
					$scope.ctlentityfieldStore.items = $scope.loadItemsData($scope.stores);
					$scope.ctlentityfieldContact.items = $scope.contacts
					$scope.ctlentityfieldReceiver.items = $scope.receivers
					$scope.ctlentityfieldSupplier.items = $scope.suppliers
					
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
				if ($scope.purchase.idstore == $scope.ctlentityfieldStore.items[i].value) {
					$scope.ctlentityfieldStore.selectedItem = {};
					$scope.ctlentityfieldStore.selectedItem = $scope.ctlentityfieldStore.items[i];
				}
			}
			for (var i = 0; i < $scope.ctlentityfieldContact.items.length; i++) {
				if ($scope.purchase.idcontact == $scope.ctlentityfieldContact.items[i].id) {
					$scope.ctlentityfieldContact.selectedItem = {};
					$scope.ctlentityfieldContact.selectedItem = $scope.ctlentityfieldContact.items[i];
				}
			}
			for (var i = 0; i < $scope.ctlentityfieldReceiver.items.length; i++) {
				if ($scope.purchase.idreceiver == $scope.ctlentityfieldReceiver.items[i].id) {
					$scope.ctlentityfieldReceiver.selectedItem = {};
					$scope.ctlentityfieldReceiver.selectedItem = $scope.ctlentityfieldReceiver.items[i];
				}
			}
			for (var i = 0; i < $scope.ctlentityfieldSupplier.items.length; i++) {
				if ($scope.purchase.idsupplier == $scope.ctlentityfieldSupplier.items[i].id) {
					$scope.ctlentityfieldSupplier.selectedItem = {};
					$scope.ctlentityfieldSupplier.selectedItem = $scope.ctlentityfieldSupplier.items[i];
				}
			}
		}
		
		// totalamount (vat included)
		$scope.total = 0;
		 $scope.CalculateSum = function () {
		   $scope.total += ( $scope.purchase.totalamount *  $scope.purchase.vat);
		 }
		$scope.ResetTotalAmt = function () {
		   $scope.total = 0;
		}
		
		// Save.
		$scope.save = function() {
			if($scope.ctlentityfieldStore.selectedItem==null){
				$scope.frmPurchase.idstore.$invalid = true;
				$scope.frmPurchase.idstore.$touched = true;
			}
			if($scope.ctlentityfieldContact.selectedItem==null){
				$scope.frmPurchase.idcontact.$invalid = true;
				$scope.frmPurchase.idcontact.$touched = true;
			}
			if($scope.ctlentityfieldReceiver.selectedItem==null){
				$scope.frmPurchase.idreceiver.$invalid = true;
				$scope.frmPurchase.idreceiver.$touched = true;
			}
			if($scope.ctlentityfieldSupplier.selectedItem==null){
				$scope.frmPurchase.idsupplier.$invalid = true;
				$scope.frmPurchase.idsupplier.$touched = true;
			}
			if ($scope.purchase.deliverydate==null) {
				$scope.frmPurchase.deliverydate.$invalid = true;
				$scope.frmPurchase.deliverydate.$touched = true;
			}
			if ($scope.purchase.code==null) {
				$scope.frmPurchase.code.$invalid = true;
			}
			if($scope.frmPurchase.$invalid||$scope.frmPurchase.idstore.$invalid||$scope.frmPurchase.deliverydate.$invalid||$scope.frmPurchase.code.$invalid) {
				$scope.frmPurchase.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			$scope.showMessageOnToast($translate.instant('clientwh_home_saving'));
			
			// Ignore time
			/*$scope.purchase.deliverydate = clientmain.getDateIgnoreTime($scope.purchase.deliverydate);*/
			
			//Trim header & tailer whitespace 
			$scope.purchase.code = $scope.purchase.code.trim();
			$scope.purchase.name = $scope.purchase.name.trim();
			if ($scope.purchase.note != null) {
				$scope.purchase.note = $scope.purchase.note.trim();
			}
			
			var result;
			if($scope.purchase.id > -1) {
				result = purchaseService.updateWithLock($scope.purchase.id, $scope.purchase);
			} else {
				result = purchaseService.create($scope.purchase);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.purchase.id > -1) {
						$scope.purchase.version = response.data;
					} else {
						$scope.purchase.id = response.data;
						$scope.purchase.version = 1;
					}
					$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
					// comment.
					$scope.commentScope.idref = $scope.purchase.id;
					$scope.listWithCriteriasByPage(1);
				} 
				else {
					if(response.data.code==clientwh.serverCode.EXISTCODE) {
						$scope.frmPurchase.code.$error.duplicate=true;
						$scope.showMessageOnToast($translate.instant('clientwh_purchase_existcode'));
					} else if(response.data.code == clientwh.serverCode.VERSIONDIFFERENCE) {
						$scope.showMessageOnToast($translate.instant('clientwh_servercode_' + response.data.code));
					} else {
						$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
					}
				}
			},
			// error.
			function(response) {
				$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
			});
		}
			
		// //////////////////////////////////////
		// Auto complete: loadItemsData
		// //////////////////////////////////////
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
			
		// Delete.
		$scope.delete = function(id, version) {
			purchaseService.updateForDeleteWithLock(id, version)
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
			purchaseService.updateForDeleteWithLock($scope.purchase.id, $scope.purchase.version)
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
			purchaseService.getById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.purchase = data;
					$scope.purchase.deliverydate = new Date($scope.purchase.deliverydate);
					$scope.itemSelected();
				} else {
					$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
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
			// purchaseService.listWithCriteriasByPage($scope.getSearch(),
			// pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// workflowexecute module
			workflowexecuteService.listWithCriteriasByPage($state, $scope.workflowTab.id, $scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.purchases = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.purchases = result;
						for (var i = 0; i < $scope.purchases.length; i++) {
							if ($scope.purchases[i].deliverydate!=null) {
								$scope.purchases[i].deliverydate = new Date($scope.purchases[i].deliverydate).toLocaleDateString();
							}
						}
						if(result.length > 0) {
							$scope.page.totalElements = response.data.totalElements;
						}
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
		
		// Clear filter Store.
		$scope.clearFilterStore = function() {
			$scope.search.store = "";
			$scope.listWithCriteriasByPage(1);
		}
		
		// Clear filter search.
		$scope.clearFilterSearch = function() {
			$scope.search.key = "";
			$scope.listWithCriteriasByPage(1);
		}
	
		/* Extend functions */
		
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
		
		$scope.sortByName = clientwh.sortByNamePurchase;
			
		// Get sort object.
		$scope.getSort = function() {
			var result = [];
			// name.
		    if(typeof($scope.sortKey) !== 'undefined' && $scope.sortKey !== '') {
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
		    	result.push({ key: 'idstore', operation: '=', value: $scope.search.store, logic: 'and' }, { key: '', operation: 'and', value: '', logic: 'and' }, { key: 'code', operation: 'like', value: $scope.search.key, logic: 'or' }, { key: 'name', operation: 'like', value: $scope.search.key, logic: 'or' });
		    }
		    else {
		    	// system
			    if(typeof($scope.search.store) !== 'undefined' && $scope.search.store !== ''&&$scope.search.store!=null){
			    	result.push({ key: 'idstore', operation: '=', value: $scope.search.store, logic: 'or' },);
			    }
			    if(typeof($scope.search.key) !== 'undefined' && $scope.search.key !== ''){
			    	result.push({ key: 'code', operation: 'like', value: $scope.search.key, logic: 'or' },{ key: 'name', operation: 'like', value: $scope.search.key, logic: 'or' });
			    }
			    if(typeof($scope.search.key) !== 'undefined' && $scope.search.key !== '' && !isNaN($scope.search.key)) {
		    		result.push({ key: 'totalamount', operation: '=', value: $scope.search.key, logic: 'or' });
				}
		    }
		    // return.
		    return result;
		}
			
		// //////////////////////////////////////
		// Auto complete: idstore
		// //////////////////////////////////////
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
			if(typeof($scope.frmPurchase) === 'undefined' || typeof($scope.frmPurchase.idstore) === 'undefined') {
				return;
			}
			$scope.purchase.idstore = undefined;
			$scope.frmPurchase.idstore.$invalid = true;
			if (item) {
				$scope.purchase.idstore = item.value;
				$scope.frmPurchase.idstore.$invalid = false;
			}
		}
		
		// //////////////////////////////////////
		// Auto complete: idcontact
		// //////////////////////////////////////
		$scope.ctlentityfieldContact = {};
		$scope.ctlentityfieldContact.isCallServer = false;
		$scope.ctlentityfieldContact.isDisabled = false;
	
		// New.
		$scope.ctlentityfieldContact.newState = function (item) {
			alert("Sorry! You'll need to create a Constitution for " + item + " first!");
		}
		// Search in array.
		$scope.ctlentityfieldContact.querySearch = function (query) {
			var results = query ? $scope.ctlentityfieldContact.items.filter($scope.ctlentityfieldContact.createFilterFor(query)) : $scope.ctlentityfieldContact.items,
				deferred;
	
			if ($scope.ctlentityfieldContact.isCallServer) {
				deferred = $q.defer();
				$timeout(function () { deferred.resolve(results); }, Math.random() * 1000, false);
				return deferred.promise;
			} else {
				return results;
			}
		}
		// Filter.
		$scope.ctlentityfieldContact.createFilterFor = function (query) {
			var lowercaseQuery = angular.lowercase(query);
			return function filterFn(item) {
				return (angular.lowercase(item.display).indexOf(lowercaseQuery) >= 0);
			};
		}
		// Text change.
		$scope.ctlentityfieldContact.searchTextChange = function (text) {
			$log.info('Text changed to ' + text);
		}
		// Item change.
		$scope.ctlentityfieldContact.selectedItemChange = function (item) {
			if(typeof($scope.frmPurchase) === 'undefined' || typeof($scope.frmPurchase.idcontact) === 'undefined') {
				return;
			}
			$scope.purchase.idcontact = undefined;
			$scope.frmPurchase.idcontact.$invalid = true;
			if (item) {
				$scope.purchase.idcontact = item.id;
				$scope.frmPurchase.idcontact.$invalid = false;
			}
		}
		
		// //////////////////////////////////////
		// Auto complete: idreceiver
		// //////////////////////////////////////
		$scope.ctlentityfieldReceiver = {};
		$scope.ctlentityfieldReceiver.isCallServer = false;
		$scope.ctlentityfieldReceiver.isDisabled = false;
	
		// New.
		$scope.ctlentityfieldReceiver.newState = function (item) {
			alert("Sorry! You'll need to create a Constitution for " + item + " first!");
		}
		// Search in array.
		$scope.ctlentityfieldReceiver.querySearch = function (query) {
			var results = query ? $scope.ctlentityfieldReceiver.items.filter($scope.ctlentityfieldReceiver.createFilterFor(query)) : $scope.ctlentityfieldReceiver.items,
				deferred;
	
			if ($scope.ctlentityfieldReceiver.isCallServer) {
				deferred = $q.defer();
				$timeout(function () { deferred.resolve(results); }, Math.random() * 1000, false);
				return deferred.promise;
			} else {
				return results;
			}
		}
		// Filter.
		$scope.ctlentityfieldReceiver.createFilterFor = function (query) {
			var lowercaseQuery = angular.lowercase(query);
			return function filterFn(item) {
				return (angular.lowercase(item.display).indexOf(lowercaseQuery) >= 0);
			};
		}
		// Text change.
		$scope.ctlentityfieldReceiver.searchTextChange = function (text) {
			$log.info('Text changed to ' + text);
		}
		// Item change.
		$scope.ctlentityfieldReceiver.selectedItemChange = function (item) {
			if(typeof($scope.frmPurchase) === 'undefined' || typeof($scope.frmPurchase.idreceiver) === 'undefined') {
				return;
			}
			$scope.purchase.idreceiver = undefined;
			$scope.frmPurchase.idreceiver.$invalid = true;
			if (item) {
				$scope.purchase.idreceiver = item.id;
				$scope.frmPurchase.idreceiver.$invalid = false;
			}
		}
		
		// //////////////////////////////////////
		// Auto complete: idsupplier
		// //////////////////////////////////////
		$scope.ctlentityfieldSupplier = {};
		$scope.ctlentityfieldSupplier.isCallServer = false;
		$scope.ctlentityfieldSupplier.isDisabled = false;
	
		// New.
		$scope.ctlentityfieldSupplier.newState = function (item) {
			alert("Sorry! You'll need to create a Constitution for " + item + " first!");
		}
		// Search in array.
		$scope.ctlentityfieldSupplier.querySearch = function (query) {
			var results = query ? $scope.ctlentityfieldSupplier.items.filter($scope.ctlentityfieldSupplier.createFilterFor(query)) : $scope.ctlentityfieldSupplier.items,
				deferred;
	
			if ($scope.ctlentityfieldSupplier.isCallServer) {
				deferred = $q.defer();
				$timeout(function () { deferred.resolve(results); }, Math.random() * 1000, false);
				return deferred.promise;
			} else {
				return results;
			}
		}
		// Filter.
		$scope.ctlentityfieldSupplier.createFilterFor = function (query) {
			var lowercaseQuery = angular.lowercase(query);
			return function filterFn(item) {
				return (angular.lowercase(item.display).indexOf(lowercaseQuery) >= 0);
			};
		}
		// Text change.
		$scope.ctlentityfieldSupplier.searchTextChange = function (text) {
			$log.info('Text changed to ' + text);
		}
		// Item change.
		$scope.ctlentityfieldSupplier.selectedItemChange = function (item) {
			if(typeof($scope.frmPurchase) === 'undefined' || typeof($scope.frmPurchase.idsupplier) === 'undefined') {
				return;
			}
			$scope.purchase.idsupplier = undefined;
			$scope.frmPurchase.idsupplier.$invalid = true;
			if (item) {
				$scope.purchase.idsupplier = item.id;
				$scope.frmPurchase.idsupplier.$invalid = false;
			}
		}
		
		$scope.getAllByPurchase = function(idpurchase){
			purchasedetailService.getAllById(idpurchase).then(function(response){
				$scope.purchasedetails = response.data;
			},function(response){
				
			})
		}
	
		$scope.copyTo = function(item){
			if (item=='materialformprematerial') {
    			$state.go(clientwh.prefix + 'materialform', {scope:'prematerial',reftype: 'purchase', formcopy: $scope.purchase, formdetailcopy: $scope.purchasedetails});
			}
    		else if (item=='materialformtechmaterial') {
    			$state.go(clientwh.prefix + 'materialform', {scope:'techmaterial',reftype: 'purchase', formcopy: $scope.purchase, formdetailcopy: $scope.purchasedetails});
			}
    		else if(item=='requestnormal'){
    			$state.go(clientwh.prefix + 'request', {scope:'normal',reftype: 'purchase', formcopy: $scope.purchase, formdetailcopy: $scope.purchasedetails});
    		}
    		else if(item=='requestextra'){
    			$state.go(clientwh.prefix + 'request', {scope:'extra',reftype: 'purchase', formcopy: $scope.purchase, formdetailcopy: $scope.purchasedetails});
    		}
    		else {
    			$state.go(clientwh.prefix + item, {reftype: 'purchase', formcopy: $scope.purchase, formdetailcopy: $scope.purchasedetails});
    		}
	    }
	    	
		$scope.formcopy = {};
		$scope.formdetailcopy = {};
	    if($stateParams.formcopy&& typeof $scope.workflowTab !== "undefined"&&$scope.workflowTab.id==="created"){
		    $scope.formcopy = $stateParams.formcopy;
		    $scope.formdetailcopy = $stateParams.formdetailcopy;
		    
		    $scope.purchase.idref = $stateParams.formcopy.id;
		    $scope.purchase.reftype = $stateParams.reftype;
		    
		    $scope.purchase.id = -1;
		    $scope.purchase.idreceiver = $stateParams.formcopy.idreceiver;
		    $scope.purchase.idcontact = $stateParams.formcopy.idcontact;
		    $scope.purchase.idsupplier = $stateParams.formcopy.idsupplier;
		    $scope.purchase.code = $stateParams.formcopy.code;
		    $scope.purchase.name = $stateParams.formcopy.name;
		    $scope.purchase.idstore = $stateParams.formcopy.idstore; 
		    $scope.purchase.contactphonenumber = $stateParams.formcopy.contactphonenumber;
		    $scope.purchase.contactfaxnumber = $stateParams.formcopy.contactfaxnumber;
		    $scope.purchase.deliveryaddress = $stateParams.formcopy.deliveryaddress;
		    $scope.purchase.deliverydate = $stateParams.formcopy.deliverydate;
		    $scope.purchase.vat = $stateParams.formcopy.vat;
		    $scope.purchase.totalamount = $stateParams.formcopy.totalamount;
		    $scope.purchase.note = $stateParams.formcopy.note;
		    
		    if (typeof (listAllSelectPromise) === 'undefined') {
				var listAllSelectDefered = $q.defer();
				listAllSelectPromise = listAllSelectDefered.promise;
				listAllSelectDefered.resolve([]);
			}
		   
		    listAllSelectPromise.then(
		 	//Success
		 	function (response) {
		 		$scope.itemSelected();
		 		 $scope.showFormDialog();
		 	},
		 	//Error
		 	function (response) { }
		 	);
	    }
    	
	}]);
	
	 
});







