
/**
 * Controller for Materialimportdetail
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'materialimportdetailController', ['$scope', '$mdToast', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', '$q', 'tmhDynamicLocale', clientwh.prefix + 'materialimportdetailService', clientwh.prefix + 'materialService', clientwh.prefix + 'catalogService',
		function($scope, $mdToast, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, $q, tmhDynamicLocale, materialimportdetailService, materialService, catalogService) {
		if(typeof(clientwh.translate.materialimportdetail) === 'undefined' || clientwh.translate.materialimportdetail.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.materialimportdetail) === 'undefined') {
				clientwh.translate.materialimportdetail = '';
			}
			clientwh.translate.materialimportdetail += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/materialimportdetail');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_materialimportdetail_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    $translate.onReady().then(function() {
	    	$scope.title = $translate.instant('clientwh_materialimportdetail_title');
	    	tmhDynamicLocale.set($translate.use());
	    	$translate.refresh();
	    });
		
	    // Search.
	    $scope.search = {};
	    
		// Paging.
		$scope.page = {
			pageSize: 2,
			totalElements: 0,
			currentPage: 0
		}
		
		$scope.materialimportdetail = {};
		
		$scope.materialimport = undefined;
	    $scope.idmaterialimport = undefined;
	    
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
						controller  : 'clientwhmaterialimportdetailController',
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
						controller  : 'clientwhmaterialimportdetailController',
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
	    
	    // Promise list for select.
		var listAllSelectPromise;
		
		// Init for list.
		$rootScope.idmaterialimportdetailController = $scope.$id;
		$scope.initList = function() {
			if(typeof(listAllSelectPromise) === 'undefined') {
				var listAllSelectDefered = $q.defer();
				listAllSelectPromise = listAllSelectDefered.promise;
				listAllSelectDefered.resolve([]);
			}
			listAllSelectPromise.then(
				// Success.
				function(response) {
					// watch idmaterialimport.
					if($rootScope.watchidmaterialimport) {
						$rootScope.watchidmaterialimport();
					}
					$rootScope.watchidmaterialimport = $scope.$watch(
							function(scope) {
								return scope.$parent ? scope.$parent.materialimport.id : null;
							},
							function(newValue, oldValue) {
								if($scope.$parent.materialimport) {
									if(newValue > -1 && oldValue==newValue && $scope.$parent.formdetailcopy.length!=0 ){
										$scope.materialimportdetailsCopy = $scope.$parent.formdetailcopy
										$scope.page.totalElements = $scope.materialimportdetailsCopy.length;
										$scope.idmaterialimport = newValue; 
										$scope.idstore = $scope.$parent.materialimport.idstore;
										$scope.materialimportdetails = [];
										for(var i = 0; i < $scope.page.totalElements; i++){
											var materialimportdetail = {idmaterialimport: newValue, idmaterial: $scope.materialimportdetailsCopy[i].idmaterial, note: $scope.materialimportdetailsCopy[i].note }
											if (typeof $scope.materialimportdetailsCopy[i].price !== "undefined") {
												materialimportdetail.price = $scope.materialimportdetailsCopy[i].price;
											}
											else {materialimportdetail.price = null;}
											if (typeof $scope.materialimportdetailsCopy[i].idunit !== "undefined") {
												materialimportdetail.idunit = $scope.materialimportdetailsCopy[i].idunit;
											}
											else {materialimportdetail.idunit = null;}
											
											if (typeof $scope.materialimportdetailsCopy[i].quantity !== "undefined") {
												materialimportdetail.quantity = $scope.materialimportdetailsCopy[i].quantity;
											}
											else {materialimportdetail.quantity = 0;}
											
											if (typeof $scope.materialimportdetailsCopy[i].amount !== "undefined") {
												materialimportdetail.amount = $scope.materialimportdetailsCopy[i].amount;
											}
											else {materialimportdetail.amount = 0;}
											$scope.materialimportdetails.push(materialimportdetail);
										}
										materialimportdetailService.createWithSelectDetail(JSON.stringify($scope.materialimportdetails), $scope.idstore).then(function(response){
											if($scope.idmaterialimport > -1 && $rootScope.idmaterialimportdetailController == $scope.$id) {
												$scope.listWithCriteriasByIdmaterialimportAndPage($scope.idmaterialimport, 1);
												$scope.$parent.formdetailcopy = []
											}
										},function(response){
											
										})
									} else {
										$scope.materialimportdetails = [];
										$scope.page.totalElements = 0;
										$scope.idstore = $scope.$parent.materialimport.idstore;
										$scope.idmaterialimport = $scope.$parent.materialimport.id;
										if($scope.idmaterialimport > -1 && $rootScope.idmaterialimportdetailController == $scope.$id) {
											$scope.listWithCriteriasByIdmaterialimportAndPage($scope.idmaterialimport, 1);
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
		
		// Call service: list all for select.
		$scope.listAllForSelect = function() {
			var listAllSelectDeferred = $q.defer();
			// Call service.
			var listMaterialByScopeForSelectDeferred = materialService.listAllForSelect();
			var listUnitByScopeForSelectDeferred = catalogService.getListScope('unit');
			// Response.
			$q.all([listMaterialByScopeForSelectDeferred,listUnitByScopeForSelectDeferred]).then(
				// Successes.
				function(responses) {
					$scope.ctlentityfieldMaterial.items = responses[0].data;
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
		listAllSelectPromise = $scope.listAllForSelect();
		
		// Init for form.
		$scope.initForm = function(id) {
			// set idmaterialimport.
			$scope.idmaterialimport = $scope.$parent ? $scope.$parent.materialimport.id : -1;
			// create new.
			$scope.createNew();
			$scope.materialimportdetail.id = id;
			if($scope.materialimportdetail.id > -1) {
				$scope.getById($scope.materialimportdetail.id);
			}
			$scope.frmDirty = false;
		}
		
		// Amount = Quantity * Price
		$scope.materialimportdetail.amount = 0;
		$scope.calculateAmount = function () {
		   $scope.materialimportdetail.amount += ( $scope.materialimportdetail.price *  $scope.materialimportdetail.quantity);
		}
		
		$scope.defaultAmount = function () {
			 $scope.materialimportdetail.amount = 0;
		}
		
		// Show a create screen.
		$scope.showCreate = function() {
			$scope.initForm(-1);
			$scope.showDialog();
			$scope.ctlentityfieldMaterial.selectedItem = null;
			$scope.unitName = null;
		}
		
		//Show form detail
		$scope.showDetail = function(id){
			$scope.getByIdForView(id);
			$scope.showDialogDetail();
		}
		
		//Show dialog view
		$scope.showDialogDetail = function () {
	        var htmlUrlTemplate = clientwh.contextPath + '/view/materialimportdetail_formView.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }
		
		// Show a form screen.
		$scope.showForm = function(id) {
			$scope.initForm(id);
			$scope.showDialog();
		}
		//Show Dialog with controller.
		$scope.showDialogWithController = function() {
			var htmlUrlTemplate = clientwh.contextPath + '/view/selectfordetail_form.html';
			var params = {name:'materialimport',id:$scope.idmaterialimport}
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
			$scope.materialimportSelected = [];
			for (var i = 0; i < materials.length; i++) {
				var materialimport = {};
				materialimport.id = -1;
				materialimport.idmaterialimport = $scope.idmaterialimport;
				materialimport.idmaterial = materials[i].idmaterial;
				materialimport.note = materials[i].note;
				if (typeof materials[i].price !== "undefined") {
					materialimport.price = materials[i].price;
				}
				else {materialimport.price = null;}
				if (typeof materials[i].idunit !== "undefined") {
					materialimport.idunit = materials[i].idunit;
				}
				else {materialimport.idunit = null;}
				
				if (typeof materials[i].quantity !== "undefined") {
					materialimport.quantity = materials[i].quantity;
				}
				else {materialimport.quantity = 0;}
				
				if (typeof materials[i].amount !== "undefined") {
					materialimport.amount = materials[i].amount;
				}
				else {materialimport.quantity = 0;}
				
				$scope.materialimportSelected.push(materialimport);
			}
			// Create With Select Detail
			materialimportdetailService.createWithSelectDetail(JSON.stringify($scope.materialimportSelected), $scope.idstore)
			.then(function(response){
				$scope.listWithCriteriasByIdmaterialimportAndPage($scope.idmaterialimport, 1);
			},function(response){
				
			});
		}
		
		$scope.inputPrice = {};
		$scope.indexItemPrice = 0;
		$scope.inputQuantity = {};
		$scope.indexItemQuantity = 0;
		// Show input price view.
	    $scope.showInputPriceFormDialog = function () {
	    	$scope.indexItemPrice = 0;
	    	$scope.listMaterialsByIdPrice($scope.idmaterialimport);
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
	    	$scope.listMaterialsByIdQuantity($scope.idmaterialimport);
	        var htmlUrlTemplate = clientwh.contextPath + '/view/inputquantity_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
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
	    
	    // List materials by id for input price
	    $scope.listMaterialsByIdPrice = function(idmaterialimport){
	    	materialimportdetailService.getAllById(idmaterialimport).then(function(response){
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
	    $scope.listMaterialsByIdQuantity = function(idmaterialimport){
	    	materialimportdetailService.getAllById(idmaterialimport).then(function(response){
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
	    
	    $scope.materialimportUpdatePrice = [];
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
	    		$scope.materialimportUpdatePrice = $scope.materialdetaillist
		    	delete $scope.materialimportUpdatePrice.materialname
		    	// Create With Select Detail
		    	materialimportdetailService.materialimportUpdatePrice(JSON.stringify($scope.materialimportUpdatePrice))
				.then(function(response){
					if(response.status === httpStatus.code.OK) {
						$scope.listMaterialsByIdPrice($scope.idmaterialimport);
						$scope.listWithCriteriasByIdmaterialimportAndPage($scope.idmaterialimport, 1);
						$scope.showMessageOnToast($translate.instant('clientwh_home_update_price_successfully'))
					}	
				},function(response){
					$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
				});
				$scope.materialimportUpdatePrice = [];
	    	}
	    }
	    
	    $scope.materialimportUpdateQuantity = [];
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
	    		$scope.materialimportUpdateQuantity = $scope.materialdetaillistQuantity
		    	delete $scope.materialimportUpdateQuantity.materialname
		    	// Create With Select Detail
		    	materialimportdetailService.materialimportUpdateQuantity(JSON.stringify($scope.materialimportUpdateQuantity),$scope.idstore)
				.then(function(response){
					if(response.status === httpStatus.code.OK) {
						$scope.listMaterialsByIdQuantity($scope.idmaterialimport);
						$scope.listWithCriteriasByIdmaterialimportAndPage($scope.idmaterialimport, 1);
						$scope.showMessageOnToast($translate.instant('clientwh_home_update_quantity_successfully'))
					}	
				},function(response){
					$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
				});
				$scope.materialimportUpdateQuantity = [];
	    	}
	    }
	    
	    //Materialform Reset Price
	    $scope.materialformResetPrice = function(){
	    	for (var i = 0; i < $scope.materialdetaillist.length; i++) {
	    		$scope.materialdetaillist[i].price = null
	    		$scope.inputPrice.price = null;
			}
	    	$scope.indexItemPrice = 0;
	    	$scope.idselectedPrice = [$scope.materialdetaillist[$scope.indexItemPrice].id]
	    	$scope.inputPrice.materialname = $scope.materialdetaillist[$scope.indexItemPrice].materialname
	    }
	    
	    //Materialform Reset Quantity
	    $scope.materialformResetQuantity = function(){
	    	for (var i = 0; i < $scope.materialdetaillistQuantity.length; i++) {
	    		$scope.materialdetaillistQuantity[i].quantity = null
	    		$scope.inputQuantity.quantity = null;
			}
	    	$scope.indexItemQuantity = 0;
	    	$scope.idselectedQuantity = [$scope.materialdetaillistQuantity[$scope.indexItemQuantity].id]
	    	$scope.inputQuantity.materialname = $scope.materialdetaillistQuantity[$scope.indexItemQuantity].materialname
	    }
		
	    // Show edit view.
	    $scope.showDialog = function () {
	        var htmlUrlTemplate = clientwh.contextPath + '/view/materialimportdetail_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }

	    // Create new.
		$scope.createNew = function() {
			$scope.materialimportdetail = { id: -1, idmaterialimport: $scope.idmaterialimport };
			$scope.unitName = null;
		}
		
		// Reset validate.
		$scope.resetValidate = function() {
			$scope.unitName = null;
			
			$scope.ctlentityfieldMaterial.searchText = undefined;
			$scope.ctlentityfieldMaterial.selectedItem = undefined;
			
			// idmaterial.
		    $scope.frmMaterialimportdetail.idmaterial.$setPristine();
			$scope.frmMaterialimportdetail.idmaterial.$setUntouched();
			
			// price.
		    $scope.frmMaterialimportdetail.price.$setPristine();
			$scope.frmMaterialimportdetail.price.$setUntouched();
			// quantity.
		    $scope.frmMaterialimportdetail.quantity.$setPristine();
			$scope.frmMaterialimportdetail.quantity.$setUntouched();
			
			//note
			$scope.frmMaterialimportdetail.note.$setPristine();
			$scope.frmMaterialimportdetail.note.$setUntouched();
			$scope.frmMaterialimportdetail.note.$modelValue = null;
		    
			// form.
			$scope.frmMaterialimportdetail.$setPristine();
			$scope.frmMaterialimportdetail.$setUntouched();
			// frmDirty.
			$scope.frmDirty = false;
			
			$scope.ctlentityfieldMaterial.selectedItem = null;
			
		}
						
		// Create on form.
		$scope.createOnForm = function() {
			$scope.resetAutocomplete();
			$scope.createNew();
			$scope.resetValidate();
		}
		
		// Reset autocomplete
		$scope.resetAutocomplete = function(){
			delete $scope.frmMaterialimportdetail.idmaterial.$error.exitsmaterial;
			$scope.frmMaterialimportdetail.idmaterial.$valid = true;
			$scope.frmMaterialimportdetail.idmaterial.$invalid = false;
		}
		
		// Save.
		$scope.save = function() {
			if($scope.ctlentityfieldMaterial.selectedItem==null){
				$scope.frmMaterialimportdetail.idmaterial.$invalid = true;
				$scope.frmMaterialimportdetail.idmaterial.$touched = true;
			}
			if($scope.frmMaterialimportdetail.$invalid||$scope.frmMaterialimportdetail.idmaterial.$invalid||$scope.frmMaterialimportdetail.quantity.$invalid||$scope.frmMaterialimportdetail.price.$invalid||$scope.frmMaterialimportdetail.note.$invalid) {
				$scope.frmMaterialimportdetail.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			$scope.showMessageOnToast($translate.instant('clientwh_home_saving'));
			if ($scope.materialimportdetail.note!=null) {
				$scope.materialimportdetail.note = $scope.materialimportdetail.note.trim();
			}
			var result;
			if($scope.materialimportdetail.id > -1) {
				result = materialimportdetailService.updateWithLock($scope.materialimportdetail.id, $scope.materialimportdetail, $scope.idstore);
			} else {
				result = materialimportdetailService.create($scope.materialimportdetail, $scope.idstore);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.materialimportdetail.id > -1) {
						$scope.materialimportdetail.version = response.data;
					} else {
						$scope.materialimportdetail.id = response.data;
						$scope.materialimportdetail.version = 1;
					}
					$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
					$scope.listWithCriteriasByIdmaterialimportAndPage($scope.idmaterialimport, 1);
				} else {
					if(response.data.code == clientwh.serverCode.DIFFPRICE) {
						$scope.frmMaterialimportdetail.price.$error.diffprice = true;
						$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
					} else if(response.data.code == clientwh.serverCode.VERSIONDIFFERENCE) {
						$scope.showMessageOnToast($translate.instant('clientwh_servercode_' + response.data.code));
					} 
					else if(response.data.code == clientwh.serverCode.OUTOFSTOCK) {
						$scope.showMessageOnToast($translate.instant('clientwh_materialimportdetail_outofstock'));
					} else if(response.data.code == clientwh.serverCode.EXITMATERIAL) {
						$scope.frmMaterialimportdetail.idmaterial.$error.exitsmaterial = true;
						$scope.frmMaterialimportdetail.idmaterial.$valid = false;
						$scope.frmMaterialimportdetail.idmaterial.$invalid = true;
						$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
					}  else {
						$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
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
				materialimportdetailService.updateForDeleteWithLock(id, version,$scope.idstore)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToast($translate.instant('clientwh_home_deleted'));
						$scope.listWithCriteriasByIdmaterialimportAndPage($scope.idmaterialimport, 1);
					} else {
						$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
					}
				},
				// error.
				function(response) {
					$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
				});
		}
		
		// Delete with create.
		$scope.deleteOnForm = function() {
				materialimportdetailService.updateForDeleteWithLock($scope.materialimportdetail.id, $scope.materialimportdetail.version, $scope.idstore)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToast($translate.instant('clientwh_home_deleted'));
						$scope.createNew();
						$scope.resetValidate();
						$scope.listWithCriteriasByIdmaterialimportAndPage($scope.idmaterialimport, 1);
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
			materialimportdetailService.getById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.materialimportdetail = data;
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
		
		// Get by Id.
		$scope.getByIdForView = function(id) {
			materialimportdetailService.getByIdForView(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.materialimportDetailView = data;
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
		$scope.getVerisonById = function(id) {
			materialimportdetailService.getVersionById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.materialimportdetail.version = data;
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
			materialimportdetailService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.materialimportdetails = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.materialimportdetails = result;
						if(result.length > 0) {
							$scope.page.totalElements = response.data.totalElements;
						}
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
		
		// List for page and filter.
		$scope.listWithCriteriasByIdmaterialimportAndPage = function(idmaterialimport, pageNo) {
			$scope.page.currentPage = pageNo;
			materialimportdetailService.listWithCriteriasByIdmaterialimportAndPage(idmaterialimport, $scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.materialimportdetails = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.materialimportdetails = result;
						if(result.length > 0) {
							$scope.page.totalElements = response.data.totalElements;
						}
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
		
		// Clear filter.
		$scope.clearFilter = function() {
			$scope.search = {};
		}
		
		// Clear filter search.
		$scope.clearFilterSearch = function() {
			$scope.search.content = "";
			$scope.listWithCriteriasByIdmaterialimportAndPage($scope.idmaterialimport,1);
		}
		
		$scope.clearSortBy = function(){
			$scope.sortKeyDetail = '';
			$scope.sortByArr = {};
			// Reload data.
			$scope.listWithCriteriasByIdmaterialimportAndPage($scope.idmaterialimport,$scope.page.currentPage);
		}
		
		$scope.sortByName = clientwh.sortByNameMaterialImportDetail;
		
		$scope.sortByArr = {}
		// Sort by.
		$scope.sortBy = function(keyName){
			$scope.sortKeyDetail = keyName;
			$scope.reverse = !$scope.reverse;
			// Reload data.
			$scope.listWithCriteriasByIdmaterialimportAndPage($scope.idmaterialimport,$scope.page.currentPage);
		}
		
		// Get sort object.
		$scope.getSort = function() {
			var result = [];
			// name.
			if(typeof($scope.sortKeyDetail) !== 'undefined' && $scope.sortKeyDetail !== ''){
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
		    	// material name.
		    	result.push({ key: 'material.name', operation: 'like', value: $scope.search.content, logic: 'or' });
				// idmaterialimport.
		    	//result.push({ key: 'idmaterialimport', operation: 'like', value: $scope.search.content, logic: 'or' });
				// idmaterial.
		    	//result.push({ key: 'idmaterial', operation: 'like', value: $scope.search.content, logic: 'or' });
				// idunit.
		    	//result.push({ key: 'idunit', operation: 'like', value: $scope.search.content, logic: 'or' });
				// price.
		    	//result.push({ key: 'price', operation: 'like', value: $scope.search.content, logic: 'or' });
				// quantity.
		    	if (parseInt($scope.search.content)) {
		    		result.push({ key: 'quantity', operation: '=', value: $scope.search.content, logic: 'or' });
				}
				// amount.
		    	//result.push({ key: 'amount', operation: 'like', value: $scope.search.content, logic: 'or' });
				// note.
		    	//result.push({ key: 'note', operation: 'like', value: $scope.search.content, logic: 'or' });
		    }
		    // return.
		    return result;
		}
		
		$scope.itemSelected = function(){
			for (var i = 0; i < $scope.ctlentityfieldMaterial.items.length; i++) {
				if($scope.materialimportdetail.idmaterial==$scope.ctlentityfieldMaterial.items[i].value){
					$scope.ctlentityfieldMaterial.selectedItem = {};
					$scope.ctlentityfieldMaterial.selectedItem = $scope.ctlentityfieldMaterial.items[i];
					$scope.unitName = $scope.ctlentityfieldMaterial.items[i].unitName;
					break;
				}
			}
		}
    	
	    
	    ////////////////////////////////////////
		// Auto complete: idMaterial
		////////////////////////////////////////
		$scope.ctlentityfieldMaterial= {};		
		$scope.ctlentityfieldMaterial.isCallServer = false;
	    $scope.ctlentityfieldMaterial.isDisabled    = false;	    
	    
	    // New.
	    $scope.ctlentityfieldMaterial.newState = function(item) {
	      alert("Sorry! You'll need to create a Constitution for " + item + " first!");
	    }
	    // Search in array.
	    $scope.ctlentityfieldMaterial.querySearch = function(query) {
	      var results = query ? $scope.ctlentityfieldMaterial.items.filter( $scope.ctlentityfieldMaterial.createFilterFor(query) ) : $scope.ctlentityfieldMaterial.items,
	          deferred;
	      
	      if ($scope.ctlentityfieldMaterial.isCallServer) {
	        deferred = $q.defer();
	        $timeout(function () { deferred.resolve( results ); }, Math.random() * 1000, false);
	        return deferred.promise;
	      } else {
	        return results;
	      }
	    }
	    // Filter.
	    $scope.ctlentityfieldMaterial.createFilterFor = function(query) {
			 var lowercaseQuery = angular.lowercase(query);
			
			 return function filterFn(item) {
				 return (angular.lowercase(item.display).indexOf(lowercaseQuery) >= 0);
			 };
	    }
	    // Text change.
	    $scope.ctlentityfieldMaterial.searchTextChange = function(text) {
	    	$log.info('Text changed to ' + text);
	    }
	    // Item change.
	    $scope.ctlentityfieldMaterial.selectedItemChange = function(item) {
	    	
	    	if(typeof(frmMaterialimportdetail) === 'undefined' || frmMaterialimportdetail.idmaterial === undefined) {
	    		return;
	    	}
	    	$scope.materialimportdetail.idmaterial = undefined;
	    	$scope.frmMaterialimportdetail.idmaterial.$invalid = true;
	    	if(item) {
	    		$scope.resetAutocomplete();
	    		$scope.materialimportdetail.idmaterial = item.value;
	    		$scope.materialimportdetail.idunit = item.idunit;
	    		$scope.unitName = item.unitName;
	    		$scope.frmMaterialimportdetail.idmaterial.$invalid = false;
	    	}
	    	else {
	    		$scope.materialimportdetail.idunit = null;
	    		$scope.unitName = null;
			}
	    }
	    $scope.unitName;
	
	}]);

});
