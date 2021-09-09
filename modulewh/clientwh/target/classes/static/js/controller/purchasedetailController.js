
/**
 * Controller for Purchasedetail
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'purchasedetailController', ['$q','$scope', '$mdToast', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', 'tmhDynamicLocale', clientwh.prefix + 'purchasedetailService', clientwh.prefix + 'materialService', clientwh.prefix + 'catalogService',
		function($q, $scope, $mdToast, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, tmhDynamicLocale, purchasedetailService, materialService, catalogService) {
		if(typeof(clientwh.translate.purchasedetail) === 'undefined' || clientwh.translate.purchasedetail.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.purchasedetail) === 'undefined') {
				clientwh.translate.purchasedetail = '';
			}
			clientwh.translate.purchasedetail += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/purchasedetail');
			$translate.refresh();
		}
		    
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_purchasedetail_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    $translate.onReady().then(function() {
	    	$scope.title = $translate.instant('clientwh_purchasedetail_title');
	    	tmhDynamicLocale.set($translate.use());
	    	$translate.refresh();
	    });
		
	    // Search.
	    $scope.search = {};
	    
		// Paging.
		$scope.page = {
			pageSize: 4,
			totalElements: 0,
			currentPage: 0
		}
		
		$scope.purchasedetail = {};
		
		$scope.purchase = undefined;
		$scope.idpurchase = undefined;
		
		//Promise list for select
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
						controller  : 'clientwhpurchasedetailController',
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
						controller  : 'clientwhpurchasedetailController',
						position: 'right'}).then(function(response){
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
		$rootScope.idpurchasedetailController = $scope.$id;
		$scope.initList = function() {
			if(typeof(listAllSelectPromise) === 'undefined') {
				var listAllSelectDefered = $q.defer();
				listAllSelectPromise = listAllSelectDefered.promise;
				listAllSelectDefered.resolve([]);
			}
			listAllSelectPromise.then(
				// Success.
				function(response) {
					// watch idpurchase.
					if($rootScope.watchidpurchase) {
						$rootScope.watchidpurchase();
					}
					$rootScope.watchidpurchase = $scope.$watch(
						function(scope) {
							return scope.$parent ? scope.$parent.purchase.id : null;
						},
						function(newValue, oldValue) {
							if(newValue > -1 && oldValue==newValue && $scope.$parent.formdetailcopy.length!=0 ) {
								$scope.purchasedetailsCopy = $scope.$parent.formdetailcopy
								$scope.page.totalElements = $scope.purchasedetailsCopy.length;
								$scope.idpurchase = newValue; 
								$scope.purchasedetails = [];
								for(var i = 0; i < $scope.page.totalElements; i++){
									var purchasedetail = {idpurchase: newValue, idmaterial: $scope.purchasedetailsCopy[i].idmaterial, note: $scope.purchasedetailsCopy[i].note }
									if (typeof $scope.purchasedetailsCopy[i].idunit !== "undefined") {
										purchasedetail.idunit = $scope.purchasedetailsCopy[i].idunit;
									}
									else {purchasedetail.idunit = null;}
									if (typeof $scope.purchasedetailsCopy[i].materialcode !== "undefined") {
										purchasedetail.materialcode = $scope.purchasedetailsCopy[i].materialcode;
									}
									else {purchasedetail.materialcode = null;}
									
									if (typeof $scope.purchasedetailsCopy[i].price !== "undefined") {
										purchasedetail.price = $scope.purchasedetailsCopy[i].price;
									}
									else {purchasedetail.price = 0;}
									
									if (typeof $scope.purchasedetailsCopy[i].quantity !== "undefined") {
										purchasedetail.quantity = $scope.purchasedetailsCopy[i].quantity;
									}
									else {purchasedetail.quantity = 0;}
									
									if (typeof $scope.purchasedetailsCopy[i].amount !== "undefined") {
										purchasedetail.amount = $scope.purchasedetailsCopy[i].amount;
									}
									else {purchasedetail.amount = 0;}
									$scope.purchasedetails.push(purchasedetail);
								}
								purchasedetailService.createWithSelectDetail(JSON.stringify($scope.purchasedetails)).then(function(response){
									if($scope.idpurchase > -1 && $rootScope.idpurchasedetailController == $scope.$id) {
										$scope.listWithCriteriasByIdpurchaseAndPage($scope.idpurchase, 1);
										$scope.$parent.formdetailcopy = [];
										if($scope.$parent.$stateParams) { 
											$scope.$parent.$stateParams.formcopy = {};
											$scope.$parent.$stateParams.formdetailcopy = {};
										}
									}
								},function(response){
									
								})
							} else {
								if($scope.$parent.purchase) {
									$scope.purchasedetails = [];
									$scope.page.totalElements = 0;
									$scope.idpurchase = $scope.$parent.purchase.id;
									if($scope.idpurchase > -1 && $rootScope.idpurchasedetailController == $scope.$id) {
										$scope.listWithCriteriasByIdpurchaseAndPage($scope.idpurchase, 1);
									}
								}
							}
						}
					);
				},
				// Error.
				function(response) {
					
				}
			);
		}
		
		// //////////////////////////////////////
		// Call service:
		// //////////////////////////////////////
		$scope.listAllForSelect = function() {
			var listAllSelectDeferred = $q.defer();
			// init data for select.
			
			var listAllForSelectDeferred = materialService.listAllForSelect();
			var listUnitByScopeForSelectDeferred = catalogService.getListScope('unit');
			
			$q.all([listAllForSelectDeferred, listUnitByScopeForSelectDeferred]).then(
				// Successes.
				function(responses) {
					$scope.ctlidmaterial.items = responses[0].data;
					$scope.listUnit =  responses[1].data;

					// Resolve promise.
					listAllSelectDeferred.resolve(responses);
				},
				// Errors.
				function(responses) {
					$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
					// Reject promise.
					listAllSelectDeferred.reject(responses);
				}				
			);
			return listAllSelectDeferred.promise;
		}
		// Call and return a promise.
		listAllSelectPromise = $scope.listAllForSelect();
		
		// Init for form.
		$scope.initForm = function(id) {
			// set idpurchase.
			$scope.idpurchase = $scope.$parent ? $scope.$parent.purchase.id : -1;
			//create new
			$scope.createNew();
			$scope.purchasedetail.id = id;
			if($scope.purchasedetail.id > -1) {
				$scope.getById($scope.purchasedetail.id);
			}
			$scope.frmDirty = false;
		}
		
		// Show a create screen.
		$scope.showCreate = function() {
			$scope.initForm(-1);
			$scope.showFormDialog();
			$scope.ctlidmaterial.selectedItem = null;
			$scope.ctlidmaterial.searchText = null;
			$scope.unitName = null
		}
		
		// Show detail 
		$scope.showView = function(id) {
			$scope.purchasedetail.id = id;
			if($scope.purchasedetail.id > -1) {
				$scope.getById($scope.purchasedetail.id);
			}
			$scope.showViewDialog();
		}
		
		//Show Dialog with controller.
		$scope.showDialogWithController = function() {
			var htmlUrlTemplate = clientwh.contextPath + '/view/selectfordetail_form.html';
			var params = {name:'purchase',id:$scope.idpurchase}
			clientwh.showDialogWithControllerName(clientwh.prefix + 'selectfordetailController', 'selectfordetailController', $mdDialog, htmlUrlTemplate, params).then(function(evt) {
				if (evt) {
					$scope.materials = angular.copy(evt);
					if ($scope.materials.length!=0) {
						$scope.creatForSelectDetail($scope.materials);
					}
				}
				console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
		}
		
		$scope.creatForSelectDetail = function(materials){
			$scope.purchasedetailSelected = [];
			for (var i = 0; i < materials.length; i++) {
				var purchase = {};
				purchase.id = -1;
				purchase.idpurchase = $scope.idpurchase;
				purchase.idmaterial = materials[i].idmaterial;
				purchase.note = materials[i].note;
				if (typeof materials[i].price !== "undefined") {
					purchase.price = materials[i].price;
				}
				else {purchase.price = null;}
				if (typeof materials[i].idunit !== "undefined") {
					purchase.idunit = materials[i].idunit;
				}
				else {purchase.idunit = null;}
				
				if (typeof materials[i].materialcode !== "undefined") {
					purchase.materialcode = materials[i].materialcode;
				}
				else {purchase.materialcode = null;}
				
				if (typeof materials[i].quantity !== "undefined") {
					purchase.quantity = materials[i].quantity;
				}
				else {purchase.quantity = 0;}
				
				if (typeof materials[i].amount !== "undefined") {
					purchase.amount = materials[i].amount;
				}
				else {purchase.quantity = 0;}
				
				$scope.purchasedetailSelected.push(purchase);
			}
			// Create With Select Detail
			purchasedetailService.createWithSelectDetail(JSON.stringify($scope.purchasedetailSelected))
			.then(function(response){
				$scope.listWithCriteriasByIdpurchaseAndPage($scope.idpurchase, 1);
				$scope.$parent.listWithCriteriasByPage(1);
			},function(response){
				
			});
		}
		
		/////////////////////////////////
		////update quantity & price/////
		///////////////////////////////
		$scope.inputPrice = {};
		$scope.indexItemPrice = 0;
		$scope.inputQuantity = {};
		$scope.indexItemQuantity = 0;
		
		// Show input price view.
	    $scope.showInputPriceFormDialog = function () {
	    	$scope.indexItemPrice = 0;
	    	$scope.listMaterialsByIdPrice($scope.idpurchase);
	        var htmlUrlTemplate = clientwh.contextPath + '/view/inputprice_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }
	    
	    // Show input quantity view.
	    $scope.showInputQuantityFormDialog = function () {
	    	$scope.indexItemQuantity = 0;
	    	$scope.listMaterialsByIdQuantity($scope.idpurchase);
	        var htmlUrlTemplate = clientwh.contextPath + '/view/inputquantity_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }
	   
	    // List materials by id for input price
	    $scope.listMaterialsByIdPrice = function(idpurchase){
	    	purchasedetailService.getAllById(idpurchase).then(function(response){
	    		$scope.materialdetaillist = response.data;
	    		$scope.indexItemPrice = 0;
	    		$scope.idselectedPrice = [$scope.materialdetaillist[$scope.indexItemPrice].id];
	    		$scope.inputPrice.materialname = $scope.materialdetaillist[$scope.indexItemPrice].materialname
	    		$scope.inputPrice.price = $scope.materialdetaillist[$scope.indexItemPrice].price
	    	},function(response){
	    		$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
	    	})
	    }
	    
	    // List materials by id for input quantity
	    $scope.listMaterialsByIdQuantity = function(idpurchase){
	    	purchasedetailService.getAllById(idpurchase).then(function(response){
	    		$scope.materialdetaillistQuantity = response.data;
	    		$scope.indexItemQuantity = 0;
	    		$scope.idselectedQuantity = [$scope.materialdetaillistQuantity[$scope.indexItemQuantity].id];
	    		$scope.inputQuantity.materialname = $scope.materialdetaillistQuantity[$scope.indexItemQuantity].materialname
	    		$scope.inputQuantity.quantity = $scope.materialdetaillistQuantity[$scope.indexItemQuantity].quantity
	    	},function(response){
	    		$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
	    	})
	    }
	    
	    // Get price by index
	    $scope.getMaterialByIndex = function(index){
	    	$scope.indexItemPrice = index
	    	$scope.inputPrice.materialname = $scope.materialdetaillist[$scope.indexItemPrice].materialname
    		$scope.inputPrice.price = $scope.materialdetaillist[$scope.indexItemPrice].price
	    }
	    
	    // Get quantity by index 
	    $scope.getMaterialByIndexQuantity = function(index){
	    	$scope.indexItemQuantity = index
	    	$scope.inputQuantity.materialname = $scope.materialdetaillistQuantity[$scope.indexItemQuantity].materialname
    		$scope.inputQuantity.quantity = $scope.materialdetaillistQuantity[$scope.indexItemQuantity].quantity
	    }
	    
	    // Update price of material
	    $scope.updatePriceOfMaterial = function(){
    		$scope.materialdetaillist[$scope.indexItemPrice].price = $scope.inputPrice.price;
	    	if ($scope.indexItemPrice < $scope.materialdetaillist.length-1) {
	    		$scope.indexItemPrice++;
	    		$scope.idselectedPrice = [$scope.materialdetaillist[$scope.indexItemPrice].id]
		    	$scope.inputPrice.materialname = $scope.materialdetaillist[$scope.indexItemPrice].materialname
	    		$scope.inputPrice.price = null
			}
	    	else {
	    		$scope.showMessageOnToast($translate.instant('clientwh_out_of_range_price'))
	    	}
	    }
	   
	    // Update quantity of material
	    $scope.updateQuantityOfMaterial = function(){
    		$scope.materialdetaillistQuantity[$scope.indexItemQuantity].quantity = $scope.inputQuantity.quantity;
	    	if ($scope.indexItemQuantity < $scope.materialdetaillistQuantity.length-1) {
	    		$scope.indexItemQuantity++;
	    		$scope.idselectedQuantity = [$scope.materialdetaillistQuantity[$scope.indexItemQuantity].id]
		    	$scope.inputQuantity.materialname = $scope.materialdetaillistQuantity[$scope.indexItemQuantity].materialname
	    		$scope.inputQuantity.quantity = null
			}
	    	else {
	    		$scope.showMessageOnToast($translate.instant('clientwh_out_of_range_quantity'))
	    	}
	    }
	    
	    
	    //Check null item when input price
	    $scope.checkNulItemPrice = function(arrInputPrice){
	    	for (var i = 0; i < arrInputPrice.length; i++) {
				if (arrInputPrice[i].price===null) {
					return i
				} 
			}
	    	return -1;
	    }
	    
	  //Check null item when input quantity
	    $scope.checkNulItemQuantity = function(arrInputQuantity){
	    	for (var i = 0; i < arrInputQuantity.length; i++) {
				if (arrInputQuantity[i].quantity===null) {
					return i
				} 
			}
	    	return -1;
	    }
	    
	    
	    $scope.purchaseUpdatePrice = [];
	    $scope.idselectedPrice = [];
	    
	    // Save update quantity
	    $scope.saveAllInputPrice = function(){
	    	var flag = $scope.checkNulItemPrice($scope.materialdetaillist)
	    	if (flag!=-1) {
	    		$scope.showMessageOnToast($translate.instant('clientwh_home_exits_price_is_null'))
	    		$scope.indexItemPrice = flag;
	    		$scope.idselectedPrice = [$scope.materialdetaillist[$scope.indexItemPrice].id];
	    		$scope.inputPrice.materialname = $scope.materialdetaillist[$scope.indexItemPrice].materialname
	    		$scope.inputPrice.price = $scope.materialdetaillist[$scope.indexItemPrice].price
			}
	    	else {
	    		$scope.purchaseUpdatePrice = $scope.materialdetaillist
		    	delete $scope.purchaseUpdatePrice.materialname
		    	// Create With Select Detail
				purchasedetailService.purchaseUpdatePrice(JSON.stringify($scope.purchaseUpdatePrice))
				.then(function(response){
					if(response.status === httpStatus.code.OK) {
						$scope.listMaterialsByIdPrice($scope.idpurchase);
						$scope.Sumtotalamount();
						$scope.listWithCriteriasByIdpurchaseAndPage($scope.idpurchase, 1);
						$scope.showMessageOnToast($translate.instant('clientwh_home_update_price_successfully'));				}	
				},function(response){
					$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
				});
				$scope.purchaseUpdatePrice = [];
	    	}
	    }
	    
	    $scope.purchaseUpdateQuantity = [];
	    $scope.idselectedQuantity = [];
	    
	    // Save update quantity
	    $scope.saveAllInputQuantity = function(){
	    	var flag = $scope.checkNulItemQuantity( $scope.materialdetaillistQuantity);
	    	if (flag!=-1) {
	    		$scope.showMessageOnToast($translate.instant('clientwh_home_exits_quantity_is_null'))
	    		$scope.indexItemQuantity = flag;
	    		$scope.idselectedQuantity = [$scope.materialdetaillistQuantity[$scope.indexItemQuantity].id];
	    		$scope.inputQuantity.materialname = $scope.materialdetaillistQuantity[$scope.indexItemQuantity].materialname
	    		$scope.inputQuantity.quantity = $scope.materialdetaillistQuantity[$scope.indexItemQuantity].quantity
			}
	    	else {
	    		$scope.purchaseUpdateQuantity = $scope.materialdetaillistQuantity
		    	delete $scope.purchaseUpdateQuantity.materialname
		    	// Create With Select Detail
				purchasedetailService.purchaseUpdateQuantity(JSON.stringify($scope.purchaseUpdateQuantity))
				.then(function(response){
					if(response.status === httpStatus.code.OK) {
						$scope.listMaterialsByIdQuantity($scope.idpurchase);
						$scope.Sumtotalamount();
						$scope.showMessageOnToast($translate.instant('clientwh_home_update_quantity_successfully'));
						$scope.listWithCriteriasByIdpurchaseAndPage($scope.idpurchase, 1);
					}	
				},function(response){
					$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
				});
				$scope.purchaseUpdateQuantity = [];
	    	}
	    }
	    
	    // Materialform Reset Price
	    $scope.materialformResetPrice = function(){
	    	for (var i = 0; i < $scope.materialdetaillist.length; i++) {
	    		$scope.materialdetaillist[i].price = null
	    		$scope.inputPrice.price = null;
			}
	    	$scope.indexItemPrice = 0;
	    	$scope.idselectedPrice = [$scope.materialdetaillist[$scope.indexItemPrice].id]
	    	$scope.inputPrice.materialname = $scope.materialdetaillist[$scope.indexItemPrice].materialname
	    }
	    
	    // Materialform Reset Quantity
	    $scope.materialformResetQuantity = function(){
	    	for (var i = 0; i < $scope.materialdetaillistQuantity.length; i++) {
	    		$scope.materialdetaillistQuantity[i].quantity = null
	    		$scope.inputQuantity.quantity = null;
			}
	    	$scope.indexItemQuantity = 0;
	    	$scope.idselectedQuantity = [$scope.materialdetaillistQuantity[$scope.indexItemQuantity].id]
	    	$scope.inputQuantity.materialname = $scope.materialdetaillistQuantity[$scope.indexItemQuantity].materialname
	    }
		
		// Show detail purchase
		$scope.showViewDialog = function () {
			var htmlUrlTemplate = clientwh.contextPath + '/view/purchasedetail_view.html';
			clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function (evt) {
				console.log('closed');
			}, function (evt) {
				console.log('not closed');
			});
		}
		
		// Show a form screen.
		$scope.showForm = function(id) {
			$scope.initForm(id);
			$scope.showFormDialog();
			$scope.ctlidmaterial.selectedItem = null;
			$scope.ctlidmaterial.searchText = null;
		}

	    // Show edit view.
	    $scope.showFormDialog = function () {
	        var htmlUrlTemplate = clientwh.contextPath + '/view/purchasedetail_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }
	    
	 
	    // Create new.
	    $scope.createNew = function() {
			$scope.purchasedetail = { id: -1, idpurchase: $scope.idpurchase };
			$scope.ctlidmaterial.searchText = null;
			$scope.ctlidmaterial.selectedItem = null;
			$scope.unitName = null;
		}
	    
		// Create on form.
		$scope.createOnForm = function() {
			$scope.resetMaterialexist();
			// Clear all field
			$scope.purchasedetail = { id: -1, idpurchase: $scope.idpurchase };
			$scope.resetValidate();
		}
		
		$scope.resetValidate = function(){
			$scope.unitName = null;
			$scope.ctlidmaterial.searchText = undefined;
			$scope.ctlidmaterial.selectedItem = undefined;
			// idmaterial.
		    $scope.frmPurchasedetail.idmaterial.$setPristine();
			$scope.frmPurchasedetail.idmaterial.$setUntouched();
			// price.
		    $scope.frmPurchasedetail.price.$setPristine();
			$scope.frmPurchasedetail.price.$setUntouched();
			// quantity.
		    $scope.frmPurchasedetail.quantity.$setPristine();
			$scope.frmPurchasedetail.quantity.$setUntouched();
			
			 // form.
			$scope.frmPurchasedetail.$setPristine();
			$scope.frmPurchasedetail.$setUntouched();
			$scope.frmDirty = false;
			
			$scope.ctlidmaterial.selectedItem = null;
			$scope.ctlidmaterial.searchText = null;
			$scope.frmPurchasedetail.idmaterial.$invalid = false;
		}
		
		// material exist
		$scope.resetMaterialexist = function(){
			delete $scope.frmPurchasedetail.idmaterial.$error.exitsmaterial;
			$scope.frmPurchasedetail.idmaterial.$valid = true;
			$scope.frmPurchasedetail.idmaterial.$invalid = false;
		}
		
		// Amount = Quantity * Price
		$scope.purchasedetail.amount = 0;
		 $scope.CalculateSum = function () {
		   $scope.purchasedetail.amount += ( $scope.purchasedetail.price *  $scope.purchasedetail.quantity);
		 }
		$scope.ResetTotalAmt = function () {
		   $scope.purchasedetail.amount = 0;
		}
		
		// sum amount.
		$scope.Sumtotalamount = function() {
			purchasedetailService.sumAmount($scope.$parent.purchase.id).then(
			// success.
			function(response) {
				$scope.$parent.purchase.totalamount = response.data;
				$scope.$parent.listWithCriteriasByPage(1);
			},
			// error.
			function(response) {
				$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
			});
		}
		
		// Save.
		$scope.save = function() {
			if($scope.ctlidmaterial.selectedItem==null){
				$scope.frmPurchasedetail.idmaterial.$invalid = true;
				$scope.frmPurchasedetail.idmaterial.$touched = true;
			}
			if($scope.frmPurchasedetail.$invalid) {
				$scope.frmPurchasedetail.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			$scope.showMessageOnToast($translate.instant('clientwh_home_saving'));
			var result;
			if($scope.purchasedetail.id > -1) {
				result = purchasedetailService.updateWithLock($scope.purchasedetail.id, $scope.purchasedetail);
			} else {
				result = purchasedetailService.create($scope.purchasedetail);
				
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.purchasedetail.id > -1) {
						$scope.purchasedetail.version = response.data;
					} else {
						$scope.purchasedetail.id = response.data;
						$scope.purchasedetail.version = 1;
					}

					$scope.Sumtotalamount();

					$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
					$scope.listWithCriteriasByIdpurchaseAndPage($scope.idpurchase, 1);
				} else {
					if(response.data.code == clientwh.serverCode.EXISTSCOPE) {
						$scope.frmPurchasedetail.scope.$invalid = true;
						$scope.showMessageOnToast($translate.instant('clientwh_servercode_' + response.data.code));
					} else if(response.data.code == clientwh.serverCode.VERSIONDIFFERENCE) {
						$scope.showMessageOnToast($translate.instant('clientwh_servercode_' + response.data.code));
					} else if(response.data.code == clientwh.serverCode.EXITMATERIAL) {
						$scope.frmPurchasedetail.idmaterial.$error.exitsmaterial = true;
						$scope.frmPurchasedetail.idmaterial.$valid = false;
						$scope.frmPurchasedetail.idmaterial.$invalid = true;
						$scope.showMessageOnToast($translate.instant('clientwh_purchasedetail_exist_material'));
					}  else {
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
			purchasedetailService.updateForDeleteWithLock(id, version)
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.showMessageOnToastList($translate.instant('clientwh_home_deleted'));
					$scope.Sumtotalamount();
					$scope.listWithCriteriasByIdpurchaseAndPage($scope.idpurchase, 1);
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
			purchasedetailService.updateForDeleteWithLock($scope.purchasedetail.id, $scope.purchasedetail.version)
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.showMessageOnToast($translate.instant('clientwh_home_deleted'));
					$scope.createNew();
					$scope.resetValidate();
					$scope.Sumtotalamount();
					$scope.listWithCriteriasByIdpurchaseAndPage($scope.idpurchase, 1);
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
			purchasedetailService.getById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.purchasedetail = data;
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
			purchasedetailService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.purchasedetails = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.purchasedetails = result;
						for (var i = 0; i < $scope.purchase.length; i++) {
							if ($scope.purchases[i].deliverydate!=null) {
								$scope.purchases[i].deliverydate = new Date($scope.purchases[i].deliverydate).toLocalDateString();
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
		
		// List for page and filter.
		$scope.listWithCriteriasByIdpurchaseAndPage = function(idpurchase, pageNo) {
			$scope.page.currentPage = pageNo;
			purchasedetailService.listWithCriteriasByIdpurchaseAndPage(idpurchase, $scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.purchasedetails = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.purchasedetails = result;
						if(result.length > 0) {
							$scope.page.totalElements = response.data.totalElements;
						}
						$scope.Sumtotalamount();
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
		}
		
		// Clear filter search.
		$scope.clearFilterSearch = function() {
			$scope.search.content = "";
			$scope.listWithCriteriasByIdpurchaseAndPage($scope.idpurchase,1);
		}
		
		$scope.clearSortBy = function(){
			$scope.sortKeyDetail = '';
			$scope.sortByArr = {};
			// Reload data.
			$scope.listWithCriteriasByIdpurchaseAndPage($scope.idpurchase,$scope.page.currentPage);
		}
		
		$scope.sortByName = clientwh.sortByNamePurchaseDetail;
		
		$scope.sortByArr = {};
		
		// Sort by.
		$scope.sortBy = function(keyName){
			$scope.sortKeyDetail = keyName;
			$scope.reverse = !$scope.reverse;
			// Reload data.
			$scope.listWithCriteriasByIdpurchaseAndPage($scope.idpurchase, $scope.page.currentPage);
		}
	
		// Get sort object.
		$scope.getSort = function() {
			var result = [];
			// name.
		    if(typeof($scope.sortKeyDetail) !== 'undefined' && $scope.sortKeyDetail !== '') {
		    	var order = 'desc';
		    	if($scope.reverse) {
		    		order = 'asc';
		    	}
		    	result.push('sort=' + $scope.sortKeyDetail + ',' + order);
		    }
			// return.
			return result;
		}
		
		// Get search object.
		$scope.getSearch = function() {
		    var result = [];
		    if(typeof($scope.search.content) !== 'undefined' && $scope.search.content !== ''){
		    	result.push({ key: 'material.name', operation: 'like', value: $scope.search.content, logic: 'or' }, { key: '', operation: 'and', value: '', logic: 'or' }, { key: 'material.code', operation: 'like', value: $scope.search.content, logic: 'or' });
				// price.
		    	//result.push({ key: 'price', operation: 'like', value: $scope.search.content, logic: 'or' });
				// amount.
		    	//result.push({ key: 'amount', operation: 'like', value: $scope.search.content, logic: 'or' });
				// note.
		    	//result.push({ key: 'note', operation: 'like', value: $scope.search.content, logic: 'or' });
		    }
		    // return.
		    return result;
		}
		
	
		$scope.itemSelected = function(){
			for (var i = 0; i < $scope.ctlidmaterial.items.length; i++) {
				if($scope.purchasedetail.idmaterial==$scope.ctlidmaterial.items[i].value){
					$scope.ctlidmaterial.selectedItem = {};
					$scope.ctlidmaterial.selectedItem = $scope.ctlidmaterial.items[i];
					$scope.unitName = $scope.ctlidmaterial.items[i].unitName;
					break;
				}
			}
		}
			
		// Amount = Quantity * Price
		$scope.purchasedetail.amount = 0;
		 $scope.CalculateSum = function () {
		   $scope.purchasedetail.amount += ( $scope.purchasedetail.price *  $scope.purchasedetail.quantity);
		 }
		$scope.ResetTotalAmt = function () {
		   $scope.purchasedetail.amount = 0;
		}
		
        
        ////////////////////////////////////////
		// Auto complete: loadItemsData
		////////////////////////////////////////
    	/*$scope.loadItemsData = function(dataResponse){
	    	var items = [];
	    	for (var i = 0; i < dataResponse.length; i++) {
	    		var item = {value: '', display: '',code: '', mcode: '', unit: ''};
	    		item.value = dataResponse[i].id;
	    		item.display = dataResponse[i].name;
	    		item.code = dataResponse[i].code;
	    		item.mcode = dataResponse[i].materialcode;
	    		item.unit = dataResponse[i].idunit;
				items.push(item);
			}
	    	return items;
	    }    	*/
    	
		// //////////////////////////////////////
		// Auto complete: ctlidmaterial
		// //////////////////////////////////////
		$scope.ctlidmaterial = {};
		$scope.ctlidmaterial.isCallServer = false;
	    $scope.ctlidmaterial.isDisabled    = false;
	    // New.
	    $scope.ctlidmaterial.newState = function(item) {
	      alert("Sorry! You'll need to create a Constitution for " + item + " first!");
	    }
	    // Search in array.
	    $scope.ctlidmaterial.querySearch = function(query) {
	    	var results = query ? $scope.ctlidmaterial.items.filter( $scope.ctlidmaterial.createFilterFor(query) ) : $scope.ctlidmaterial.items, deferred;
	    	if ($scope.ctlidmaterial.isCallServer) {
		        deferred = $q.defer();
		        $timeout(function () { deferred.resolve( results ); }, Math.random() * 1000, false);
		        return deferred.promise;
		    } else {
		        return results;
		    }
	    }
	    // Filter.
	    $scope.ctlidmaterial.createFilterFor = function(query) {
	    	var lowercaseQuery = angular.lowercase(query);

	    	return function filterFn(item) {
	    		return (angular.lowercase(item.display).indexOf(lowercaseQuery) >= 0);
	    	};
	    }
	    // Text change.
	    $scope.ctlidmaterial.searchTextChange = function(text) {
	    	$log.info('Text changed to ' + text);
	    }
	    // Item change.
	    $scope.ctlidmaterial.selectedItemChange = function(item) {
	    	if(typeof($scope.frmPurchasedetail) === 'undefined' || typeof($scope.frmPurchasedetail.idmaterial) === 'undefined') {
	    		return;
	    	}
	    	$scope.purchasedetail.idmaterial = undefined;
	    	$scope.frmPurchasedetail.idmaterial.$invalid = true;
	    	if(item) {
	    		$scope.resetMaterialexist();
	    		$scope.purchasedetail.idmaterial = item.value;
	    		$scope.purchasedetail.idunit = item.idunit;
	    		$scope.unitName = item.unitName;
	    		$scope.frmPurchasedetail.idmaterial.$invalid = false;
	    	}
	    	else {
	    		$scope.purchasedetail.idunit = null;
	    		$scope.unitName = null;
	    	}
	    }
	    $scope.unitName;
	}]);

});
