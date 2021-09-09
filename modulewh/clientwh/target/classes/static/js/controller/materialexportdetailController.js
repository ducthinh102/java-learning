
/**
 * Controller for Materialexportdetail
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'materialexportdetailController', ['$scope', '$mdToast', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', '$q', clientwh.prefix + 'materialexportdetailService', clientwh.prefix + 'materialService', clientwh.prefix + 'catalogService',clientwh.prefix + 'materialstoreService',
		function($scope, $mdToast, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, $q, materialexportdetailService, materialService, catalogService, materialstoreService) {
		if(typeof(clientwh.translate.materialexportdetail) === 'undefined' || clientwh.translate.materialexportdetail.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.materialexportdetail) === 'undefined') {
				clientwh.translate.materialexportdetail = '';
			}
			clientwh.translate.materialexportdetail += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/materialexportdetail');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_materialexportdetail_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    $translate.onReady().then(function() {
	    	$scope.title = $translate.instant('clientwh_materialexportdetail_title');
	    	$translate.refresh();
	    });
		
	    // Search.
	    $scope.search = {};
	    
		// Paging.
		$scope.page = {
			pageSize: 8,
			totalElements: 0,
			currentPage: 0
		}
		
		$scope.materialexportdetail = {};
		
		$scope.materialexport = undefined;
	    $scope.idmaterialexport = undefined;
	    
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
						controller  : 'clientwhmaterialexportdetailController',
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
						controller  : 'clientwhmaterialexportdetailController',
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
		$rootScope.idmaterialexportdetailController = $scope.$id;
		$scope.initList = function() {
			if(typeof(listAllSelectPromise) === 'undefined') {
				var listAllSelectDefered = $q.defer();
				listAllSelectPromise = listAllSelectDefered.promise;
				listAllSelectDefered.resolve([]);
			}
			listAllSelectPromise.then(
				// Success.
				function(response) {
					// watch idmaterialexport.
					if($rootScope.watchidmaterialexport) {
						$rootScope.watchidmaterialexport();
					}
					$rootScope.watchidmaterialexport = $scope.$watch(
							function(scope) {
								return scope.$parent ? scope.$parent.materialexport.id : null;
							},
							function(newValue, oldValue) {
								if($scope.$parent.materialexport) {
									if(newValue > -1 && oldValue==newValue && $scope.$parent.formdetailcopy.length!=0 ){
										$scope.materialexportdetailsCopy = $scope.$parent.formdetailcopy
										$scope.page.totalElements = $scope.materialexportdetailsCopy.length;
										$scope.idmaterialexport = newValue; 
										$scope.idstore = $scope.$parent.materialexport.idstore;
										$scope.materialexportdetails = [];
										
										for(var i = 0; i < $scope.page.totalElements; i++){
											var materialexportdetail = {idmaterialexport: newValue, idmaterial: $scope.materialexportdetailsCopy[i].idmaterial, note: $scope.materialexportdetailsCopy[i].note }
											if (typeof $scope.materialexportdetailsCopy[i].price !== "undefined") {
												materialexportdetail.price = $scope.materialexportdetailsCopy[i].price;
											}
											else {materialexportdetail.price = null;}
											if (typeof $scope.materialexportdetailsCopy[i].idunit !== "undefined") {
												materialexportdetail.idunit = $scope.materialexportdetailsCopy[i].idunit;
											}
											else {materialexportdetail.idunit = null;}
											
											if (typeof $scope.materialexportdetailsCopy[i].quantity !== "undefined") {
												materialexportdetail.quantity = $scope.materialexportdetailsCopy[i].quantity;
											}
											else {materialexportdetail.quantity = 0;}
											
											if (typeof $scope.materialexportdetailsCopy[i].amount !== "undefined") {
												materialexportdetail.amount = $scope.materialexportdetailsCopy[i].amount;
											}
											else {materialexportdetail.amount = 0;}
											$scope.materialexportdetails.push(materialexportdetail);
										}
										$scope.checkQuantityBeforeInputDetail($scope.materialexportdetails);
									} else {
										$scope.materialexportdetails = [];
										$scope.page.totalElements = 0;
										$scope.idstore = $scope.$parent.materialexport.idstore;
										$scope.idmaterialexport = $scope.$parent.materialexport.id;
										if($scope.idmaterialexport > -1 && $rootScope.idmaterialexportdetailController == $scope.$id) {
											$scope.listWithCriteriasByIdmaterialexportAndPage($scope.idmaterialexport, 1);
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
		
		$scope.checkQuantityBeforeInputDetail = function(details){
    		var quantityMaterials = [];
    		var intArr = details.length
    		for (var i = 0; i < intArr; i++) {
    			quantityMaterials.push({"idmaterial":details[i].idmaterial})
			}
    		if (quantityMaterials.length!=0) {
    			materialstoreService.checkQuantityMaterial($scope.idstore,JSON.stringify(quantityMaterials))
        		.then(function(response){
        			var materialstore = [];
        			materialstore =  response.data;
        			for (var i = 0; i < materialstore.length; i++) {
        				for (var j = 0; j < details.length; j++) {
        					if (parseInt(materialstore[i].idmaterial)==parseInt(details[j].idmaterial)) {
        						if (parseInt(materialstore[i].quantity)-parseInt(details[j].quantity)<0) {
        							details[j].quantity = 0;
    							}
        					}
    					}
    				}
        			var tmpDetail = [];
        			for (var i = 0; i < materialstore.length; i++) {
        				for (var j = 0; j < details.length; j++) {
        					if (parseInt(materialstore[i].idmaterial)==parseInt(details[j].idmaterial)) {
        						tmpDetail.push(details[j])
        					}
    					}
    				}
        			details = tmpDetail;
        			materialexportdetailService.createWithSelectDetail(JSON.stringify(details), $scope.idstore).then(function(response){
						if($scope.idmaterialexport > -1 && $rootScope.idmaterialexportdetailController == $scope.$id) {
							$scope.listWithCriteriasByIdmaterialexportAndPage($scope.idmaterialexport, 1);
							$scope.$parent.formdetailcopy = []
						}
        			},function(response){
						
					})
        		},function(response){
        			
        		})
			}
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
			// set idmaterialexport.
			$scope.idmaterialexport = $scope.$parent ? $scope.$parent.materialexport.id : -1;
			// create new.
			$scope.createNew();
			$scope.materialexportdetail.id = id;
			if($scope.materialexportdetail.id > -1) {
				$scope.getById($scope.materialexportdetail.id);
			}
			$scope.frmDirty = false;
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
	        var htmlUrlTemplate = clientwh.contextPath + '/view/materialexportdetail_formView.html';
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
			var params = {name:'materialexport',id:$scope.idmaterialexport,idstore:$scope.idstore}
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
			$scope.materialexportSelected = [];
			for (var i = 0; i < materials.length; i++) {
				var materialexport = {};
				materialexport.id = -1;
				materialexport.idmaterialexport = $scope.idmaterialexport;
				materialexport.idmaterial = materials[i].idmaterial;
				materialexport.note = materials[i].note;
				if (typeof materials[i].idunit !== "undefined") {
					materialexport.idunit = materials[i].idunit;
				}
				else {materialexport.idunit = null;}
				
				if (typeof materials[i].price !== "undefined") {
					materialexport.price = materials[i].price;
				}
				else {materialexport.price = 0;}
				
				if (typeof materials[i].amount !== "undefined") {
					materialexport.amount = materials[i].amount;
				}
				else {materialexport.amount = 0;}
				
				if (typeof materials[i].quantity !== "undefined") {
					materialexport.quantity = materials[i].quantity;
				}
				else {materialexport.quantity = 0;}
				
				$scope.materialexportSelected.push(materialexport);
			}
			// Create With Select Detail
			materialexportdetailService.createWithSelectDetail(JSON.stringify($scope.materialexportSelected), $scope.idstore)
			.then(function(response){
				$scope.listWithCriteriasByIdmaterialexportAndPage($scope.idmaterialexport, 1);
			},function(response){
				
			});
		}
		
		$scope.inputQuantity = {};
		$scope.indexItemQuantity = 0;
	    
	    // Show input quantity view.
	    $scope.showInputQuantityFormDialog = function () {
	    	$scope.indexItemQuantity = 0;
	    	$scope.listMaterialsByIdQuantity($scope.idmaterialexport);
	        var htmlUrlTemplate = clientwh.contextPath + '/view/inputquantity_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }
	   $scope.checkQuantityInput = function(idmaterial, quantity, quantityDB, arrQuantityMaterial){
		   for (var i = 0; i < arrQuantityMaterial.length; i++) {
			if (arrQuantityMaterial[i].idmaterial==idmaterial) {
				if (arrQuantityMaterial[i].quantity+quantityDB-quantity < 0) {
					return arrQuantityMaterial[i].quantity + quantityDB
				}
			}
		   }
		   return -1;
	   }
	   $scope.priceMaterials = []
	   $scope.DBQuantity = []
	    // List materials by id for input quantity
	    $scope.listMaterialsByIdQuantity = function(idmaterialexport){
	    	materialexportdetailService.getAllById(idmaterialexport).then(function(response){
	    		$scope.materialdetaillistQuantity = response.data;
	    		var priceMaterials = [];
	    		for (var i = 0; i < $scope.materialdetaillistQuantity.length; i++) {
	    			priceMaterials.push({"idmaterial":$scope.materialdetaillistQuantity[i].idmaterial})
	    			$scope.DBQuantity.push({"quantity":$scope.materialdetaillistQuantity[i].quantity})
				}
	    		materialstoreService.checkQuantityMaterial($scope.idstore,JSON.stringify(priceMaterials))
	    		.then(function(response){
	    			$scope.priceMaterials = response.data;
	    			$scope.indexItemQuantity = 0;
		    		$scope.idselectedQuantity = [$scope.materialdetaillistQuantity[$scope.indexItemQuantity].id];
		    		$scope.inputQuantity.materialname = $scope.materialdetaillistQuantity[$scope.indexItemQuantity].materialname
		    		$scope.inputQuantity.quantity = $scope.materialdetaillistQuantity[$scope.indexItemQuantity].quantity
		    		$scope.DBQuantity.quantity =  $scope.materialdetaillistQuantity[$scope.indexItemQuantity].quantity
	    		},function(response){
	    			$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
	    		})
	    	},function(response){
	    		$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
	    	})
	    }
	    
	    // Get quantity by index 
	    $scope.getMaterialByIndexQuantity = function(index){
	    	$scope.indexItemQuantity = index
	    	$scope.inputQuantity.materialname = $scope.materialdetaillistQuantity[$scope.indexItemQuantity].materialname
    		$scope.inputQuantity.quantity = $scope.materialdetaillistQuantity[$scope.indexItemQuantity].quantity
	    }
	    
	   
	    // Update quantity of material
	    $scope.updateQuantityOfMaterial = function(){
    		
	    	if ($scope.indexItemQuantity < $scope.materialdetaillistQuantity.length-1) {
	    		var quantity = $scope.checkQuantityInput($scope.materialdetaillistQuantity[$scope.indexItemQuantity].idmaterial, $scope.inputQuantity.quantity,$scope.DBQuantity[$scope.indexItemQuantity].quantity, $scope.priceMaterials)
	    		if (quantity==-1) {
	    			$scope.materialdetaillistQuantity[$scope.indexItemQuantity].quantity = $scope.inputQuantity.quantity;
	    			$scope.indexItemQuantity++;
		    		$scope.idselectedQuantity = [$scope.materialdetaillistQuantity[$scope.indexItemQuantity].id]
			    	$scope.inputQuantity.materialname = $scope.materialdetaillistQuantity[$scope.indexItemQuantity].materialname
		    		$scope.inputQuantity.quantity = null
				}
	    		else {
	    			$scope.showMessageOnToast($translate.instant('clientwh_home_range_of_out_quantity_in_store')+': '+ quantity)
	    		}
			}
	    	else {
	    		$scope.showMessageOnToast($translate.instant('clientwh_out_of_range_quantity'))
	    		var quantity = $scope.checkQuantityInput($scope.materialdetaillistQuantity[$scope.indexItemQuantity].idmaterial, $scope.inputQuantity.quantity,$scope.DBQuantity[$scope.indexItemQuantity].quantity, $scope.priceMaterials)
	    		if (quantity==-1) {
	    			$scope.materialdetaillistQuantity[$scope.indexItemQuantity].quantity = $scope.inputQuantity.quantity;
		    		$scope.idselectedQuantity = [$scope.materialdetaillistQuantity[$scope.indexItemQuantity].id]
			    	$scope.inputQuantity.materialname = $scope.materialdetaillistQuantity[$scope.indexItemQuantity].materialname
				}
	    		else {
	    			$scope.showMessageOnToast($translate.instant('clientwh_home_range_of_out_quantity_in_store')+': '+ quantity)
	    		}
	    	}
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
	    
	    $scope.materialexportUpdateQuantity = [];
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
	    		$scope.materialexportUpdateQuantity = $scope.materialdetaillistQuantity
		    	delete $scope.materialexportUpdateQuantity.materialname
		    	// Create With Select Detail
		    	materialexportdetailService.materialexportUpdateQuantity(JSON.stringify($scope.materialexportUpdateQuantity),$scope.idstore)
				.then(function(response){
					if(response.status === httpStatus.code.OK) {
						$scope.listMaterialsByIdQuantity($scope.idmaterialexport);
						$scope.listWithCriteriasByIdmaterialexportAndPage($scope.idmaterialexport, 1);
						$scope.showMessageOnToast($translate.instant('clientwh_home_update_quantity_successfully'))
					}	
				},function(response){
					$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
				});
				$scope.materialexportUpdateQuantity = [];
	    	}
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
	        var htmlUrlTemplate = clientwh.contextPath + '/view/materialexportdetail_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }

	    // Create new.
		$scope.createNew = function() {
			$scope.materialexportdetail = { id: -1, idmaterialexport: $scope.idmaterialexport };
			$scope.unitName = null;
		}
		
		// Reset validate.
		$scope.resetValidate = function() {
			$scope.unitName = null;
			
			$scope.ctlentityfieldMaterial.searchText = undefined;
			$scope.ctlentityfieldMaterial.selectedItem = undefined;
			
			// idmaterial.
		    $scope.frmMaterialexportdetail.idmaterial.$setPristine();
			$scope.frmMaterialexportdetail.idmaterial.$setUntouched();
			
			// quantity.
		    $scope.frmMaterialexportdetail.quantity.$setPristine();
			$scope.frmMaterialexportdetail.quantity.$setUntouched();
			
		    // form.
			$scope.frmMaterialexportdetail.$setPristine();
			$scope.frmMaterialexportdetail.$setUntouched();
			
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
			delete $scope.frmMaterialexportdetail.idmaterial.$error.exitsmaterial;
			$scope.frmMaterialexportdetail.idmaterial.$valid = true;
			$scope.frmMaterialexportdetail.idmaterial.$invalid = false;
		}
		
		// Save.
		$scope.save = function() {
			if($scope.ctlentityfieldMaterial.selectedItem==null){
				$scope.frmMaterialexportdetail.idmaterial.$invalid = true;
				$scope.frmMaterialexportdetail.idmaterial.$touched = true;
			}
			if($scope.frmMaterialexportdetail.$invalid
					||$scope.frmMaterialexportdetail.idmaterial.$invalid
					||$scope.frmMaterialexportdetail.quantity.$invalid
				) {
				$scope.frmMaterialexportdetail.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			$scope.showMessageOnToast($translate.instant('clientwh_home_saving'));
			var result;
			if($scope.materialexportdetail.id > -1) {
				result = materialexportdetailService.updateWithLock($scope.materialexportdetail.id, $scope.materialexportdetail, $scope.idstore);
			} else {
				result = materialexportdetailService.create($scope.materialexportdetail, $scope.idstore);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.materialexportdetail.id > -1) {
						$scope.materialexportdetail.version = response.data;
					} else {
						$scope.materialexportdetail.id = response.data;
						$scope.materialexportdetail.version = 1;
					}
					$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
					$scope.listWithCriteriasByIdmaterialexportAndPage($scope.idmaterialexport, 1);
				} else {
					if(response.data.code == clientwh.serverCode.VERSIONDIFFERENCE) {
						$scope.showMessageOnToast($translate.instant('clientwh_servercode_' + response.data.code));
					} 
					else if(response.data.code == clientwh.serverCode.OUTOFSTOCK) {
						$scope.showMessageOnToast($translate.instant('clientwh_materialexportdetail_outofstock'), 'alert-danger', true);
					} 
					else if(response.data.code == clientwh.serverCode.EXITMATERIAL) {
						$scope.frmMaterialexportdetail.idmaterial.$error.exitsmaterial = true;
						$scope.frmMaterialexportdetail.idmaterial.$valid = false;
						$scope.frmMaterialexportdetail.idmaterial.$invalid = true;
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
				materialexportdetailService.updateForDeleteWithLock(id, version,$scope.idstore)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToast($translate.instant('clientwh_home_deleted'));
						$scope.listWithCriteriasByIdmaterialexportAndPage($scope.idmaterialexport, 1);
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
				materialexportdetailService.updateForDeleteWithLock($scope.materialexportdetail.id, $scope.materialexportdetail.version, $scope.idstore)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToast($translate.instant('clientwh_home_deleted'));
						$scope.createNew();
						$scope.resetValidate();
						$scope.listWithCriteriasByIdmaterialexportAndPage($scope.idmaterialexport, 1);
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
			materialexportdetailService.getById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.materialexportdetail = data;
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
			materialexportdetailService.getByIdForView(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.materialexportDetailView = data;
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
			materialexportdetailService.getVersionById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.materialexportdetail.version = data;
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
			materialexportdetailService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.materialexportdetails = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.materialexportdetails = result;
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
		$scope.listWithCriteriasByIdmaterialexportAndPage = function(idmaterialexport, pageNo) {
			$scope.page.currentPage = pageNo;
			materialexportdetailService.listWithCriteriasByIdmaterialexportAndPage(idmaterialexport, $scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.materialexportdetails = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.materialexportdetails = result;
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
			$scope.listWithCriteriasByIdmaterialexportAndPage($scope.idmaterialexport,1);
		}
		/*Extend functions*/
		
		$scope.clearSortBy = function(){
			$scope.sortKeyDetail = '';
			$scope.sortByArr = {};
			// Reload data.
			$scope.listWithCriteriasByIdmaterialexportAndPage($scope.idmaterialexport,$scope.page.currentPage);
		}
		
		$scope.sortByName = clientwh.sortByNameMaterialExportDetail;
		
		$scope.sortByArr = {}
		// Sort by.
		$scope.sortBy = function(keyName){
			$scope.sortKeyDetail = keyName;
			$scope.reverse = !$scope.reverse;
			// Reload data.
			$scope.listWithCriteriasByIdmaterialexportAndPage($scope.idmaterialexport,$scope.page.currentPage);
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
		    	result.push({ key: 'material.name', operation: 'like', value: $scope.search.content, logic: 'or' });
				
				// quantity.
		    	if (parseInt($scope.search.content)) {
		    		result.push({ key: 'quantity', operation: '=', value: $scope.search.content, logic: 'or' });
				}
		    }
		    // return.
		    return result;
		}
		$scope.itemSelected = function(){
			for (var i = 0; i < $scope.ctlentityfieldMaterial.items.length; i++) {
				if($scope.materialexportdetail.idmaterial==$scope.ctlentityfieldMaterial.items[i].value){
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
	    	
	    	if(typeof(frmMaterialexportdetail) === 'undefined' || frmMaterialexportdetail.idmaterial === undefined) {
	    		return;
	    	}
	    	$scope.materialexportdetail.idmaterial = undefined;
	    	$scope.frmMaterialexportdetail.idmaterial.$invalid = true;
	    	if(item) {
	    		$scope.resetAutocomplete();
	    		$scope.materialexportdetail.idmaterial = item.value;
	    		$scope.materialexportdetail.idunit = item.idunit;
	    		$scope.unitName = item.unitName;
	    		$scope.frmMaterialexportdetail.idmaterial.$invalid = false;
	    	}
	    	else {
	    		$scope.materialexportdetail.idunit = null;
	    		$scope.unitName = null;
			}
	    }
	    $scope.unitName;
	
	}]);

});
