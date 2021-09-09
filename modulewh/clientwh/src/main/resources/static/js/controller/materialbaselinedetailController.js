
/**
 * Controller for Materialbaselinedetail
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'materialbaselinedetailController', ['$q', '$mdToast', 'moment', '$scope', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', clientwh.prefix + 'materialService', clientwh.prefix + 'supplierService', clientwh.prefix + 'materialbaselinedetailService',
		function($q, $mdToast, moment, $scope, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, materialService, supplierService, materialbaselinedetailService) {
		if(typeof(clientwh.translate.materialbaselinedetail) === 'undefined' || clientwh.translate.materialbaselinedetail.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.materialbaselinedetail) === 'undefined') {
				clientwh.translate.materialbaselinedetail = '';
			}
			clientwh.translate.materialbaselinedetail += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/materialbaselinedetail');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_materialbaselinedetail_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    $translate.onReady().then(function() {
	    	$scope.title = $translate.instant('clientwh_materialbaselinedetail_title');
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
		
		// Set current date
		$scope.newstartdate = function () {
			$scope.materialbaselinedetail.startdate = new Date();
		}
		
		$scope.newenddate = function () {
			$scope.materialbaselinedetail.enddate = new Date();
		}
		
		
		$scope.noSunday = function (date) {
			var day = date.getDay();
			return day === 1 || day === 2 || day === 3 || day === 4 || day === 5 || day === 6;
		}
		
		$scope.formatDate = function(dtime) {
			return moment(dtime).format('LL');
		}
		
		$scope.materialbaselinedetail = {};
		
		$scope.materialbaseline = undefined;
		$scope.idmaterialbaseline = undefined;
		
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
						controller  : 'clientwhmaterialbaselinedetailController',
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
						controller  : 'clientwhmaterialbaselinedetailController',
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
		$rootScope.idmaterialbaselinedetailController = $scope.$id;
		$scope.initList = function() {
			if(typeof(listAllSelectPromise) === 'undefined') {
				var listAllSelectDefered = $q.defer();
				listAllSelectPromise = listAllSelectDefered.promise;
				listAllSelectDefered.resolve([]);
			}
			listAllSelectPromise.then(
				//Success.
				function(response) {
					// watch idmaterialbaseline.
					$scope.$watch(
							function(scope) {
								return scope.$parent ? scope.$parent.materialbaseline.id : null;
							},
							function(newValue, oldValue) {
								if(newValue > -1 && oldValue==newValue && $scope.$parent.formdetailcopy.length!=0 ){
									$scope.materialbaselinedetailsCopy = $scope.$parent.formdetailcopy
									$scope.page.totalElements = $scope.materialbaselinedetailsCopy.length;
									$scope.idmaterialbaseline = newValue; 
									$scope.materialbaselinedetails = [];
									for(var i = 0; i < $scope.page.totalElements; i++){
										var materialbaselinedetail = {idmaterialbaseline: newValue, idmaterial: $scope.materialbaselinedetailsCopy[i].idmaterial, note: $scope.materialbaselinedetailsCopy[i].note }
										if (typeof $scope.materialbaselinedetailsCopy[i].idsupplier !== "undefined") {
											materialbaselinedetail.idsupplier = $scope.materialbaselinedetailsCopy[i].idsupplier;
										}
										else {materialbaselinedetail.idsupplier = null;}
										
										if (typeof $scope.materialbaselinedetailsCopy[i].idunit !== "undefined") {
											materialbaselinedetail.idunit = $scope.materialbaselinedetailsCopy[i].idunit;
										}
										else {materialbaselinedetail.idunit = null;}
										
										if (typeof $scope.materialbaselinedetailsCopy[i].price !== "undefined") {
											materialbaselinedetail.price = $scope.materialbaselinedetailsCopy[i].price;
										}
										else {materialbaselinedetail.price = 0;}
										
										if (typeof $scope.materialbaselinedetailsCopy[i].quantity !== "undefined") {
											materialbaselinedetail.quantity = $scope.materialbaselinedetailsCopy[i].quantity;
										}
										else {materialbaselinedetail.quantity = 0;}
										
										if (typeof $scope.materialbaselinedetailsCopy[i].amount !== "undefined") {
											materialbaselinedetail.amount = $scope.materialbaselinedetailsCopy[i].amount;
										}
										else {materialbaselinedetail.amount = 0;}
										
										if (typeof $scope.materialbaselinedetailsCopy[i].startdate !== "undefined") {
											materialbaselinedetail.startdate = $scope.materialbaselinedetailsCopy[i].startdate;
										}
										else {materialbaselinedetail.startdate = null;}
										
										if (typeof $scope.materialbaselinedetailsCopy[i].enddate !== "undefined") {
											materialbaselinedetail.enddate = $scope.materialbaselinedetailsCopy[i].enddate;
										}
										else {materialbaselinedetail.enddate = null;}
										
										$scope.materialbaselinedetails.push(materialbaselinedetail);
									}
									materialbaselinedetailService.createWithSelectDetail(JSON.stringify($scope.materialbaselinedetails)).then(function(response){
										if($scope.idmaterialbaseline > -1 && $rootScope.idmaterialbaselinedetailController == $scope.$id) {
											$scope.listWithCriteriasByIdmaterialbaselineAndPage($scope.idmaterialbaseline, 1);
											$scope.$parent.formdetailcopy = []
										}
									},function(response){
										
									})
								} else {
									if($scope.$parent.materialbaseline) {
										$scope.materialbaselinedetails = [];
										$scope.page.totalElements = 0;
										$scope.idmaterialbaseline = $scope.$parent.materialbaseline.id;
										if($scope.idmaterialbaseline > -1 && $rootScope.idmaterialbaselinedetailController == $scope.$id) {
											$scope.listWithCriteriasByIdmaterialbaselineAndPage($scope.idmaterialbaseline, 1);
										}
									}
								}
								
							}
					);
				},
				//Error.
				function(response) {
					
				}
			);
		}
		
		// Init for form.
		$scope.initForm = function(id) {
			// set idmaterialbaseline.
			$scope.idmaterialbaseline = $scope.$parent ? $scope.$parent.materialbaseline.id : -1;
			//create new
			$scope.createNew();
			$scope.materialbaselinedetail.id = id;
			if($scope.materialbaselinedetail.id > -1) {
				$scope.getById($scope.materialbaselinedetail.id);
			}
			$scope.frmDirty = false;
		}
		
		// Show detail 
		$scope.showDetail = function(id) {
			$scope.materialbaselinedetail.id = id;
			if($scope.materialbaselinedetail.id > -1) {
				$scope.getById($scope.materialbaselinedetail.id);
			}
			$scope.showDialogDetail();
		}
		
		//Show detail materialbaseline
		$scope.showDialogDetail = function () {
			var htmlUrlTemplate = clientwh.contextPath + '/view/materialbaselinedetail_view.html';
			clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function (evt) {
				console.log('closed');
			}, function (evt) {
				console.log('not closed');
			});
		}
		
		// Show a create screen.
		$scope.showCreate = function() {
			$scope.initForm(-1);
			$scope.showDialog();
			$scope.ctlentityfieldMaterial.selectedItem = null;
			$scope.ctlentityfieldMaterial.searchText = null;
			$scope.ctlentityfieldSupplier.selectedItem = null;
			$scope.ctlentityfieldSupplier.searchText = null;
			$scope.unitName = null;
		}
		
		// Show a form screen.
		$scope.showForm = function(id) {
			$scope.initForm(id);
			$scope.showDialog();
		}

		//Show Dialog with controller.
		$scope.showDialogWithController = function() {
			var htmlUrlTemplate = clientwh.contextPath + '/view/selectfordetail_form.html';
			var params = {name:'materialbaseline',id:$scope.idmaterialbaseline}
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
			$scope.materialbaselineSelected = [];
			for (var i = 0; i < materials.length; i++) {
				var materialbaseline = {};
				materialbaseline.id = -1;
				materialbaseline.idmaterialbaseline = $scope.idmaterialbaseline;
				materialbaseline.idmaterial = materials[i].idmaterial;
				materialbaseline.note = materials[i].note;
				if (typeof materials[i].price !== "undefined") {
					materialbaseline.price = materials[i].price;
				}
				else {materialbaseline.price = null;}
				if (typeof materials[i].idunit !== "undefined") {
					materialbaseline.idunit = materials[i].idunit;
				}
				else {materialbaseline.idunit = null;}
				
				if (typeof materials[i].quantity !== "undefined") {
					materialbaseline.quantity = materials[i].quantity;
				}
				else {materialbaseline.quantity = 0;}
				
				if (typeof materials[i].amount !== "undefined") {
					materialbaseline.amount = materials[i].amount;
				}
				else {materialbaseline.quantity = 0;}
				
				if (typeof materials[i].startdate !== "undefined") {
					materialbaseline.startdate = materials[i].startdate;
				}
				else {materialbaseline.startdate = null;}
				if (typeof materials[i].enddate !== "undefined") {
					materialbaseline.enddate = materials[i].enddate;
				}
				else {materialbaseline.enddate = null;}
				$scope.materialbaselineSelected.push(materialbaseline);
			}
			// Create With Select Detail
			materialbaselinedetailService.createWithSelectDetail(JSON.stringify($scope.materialbaselineSelected))
			.then(function(response){
				$scope.listWithCriteriasByIdmaterialbaselineAndPage($scope.idmaterialbaseline, 1);
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
	    	$scope.listMaterialsByIdPrice($scope.idmaterialbaseline);
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
	    	$scope.listMaterialsByIdQuantity($scope.idmaterialbaseline);
	        var htmlUrlTemplate = clientwh.contextPath + '/view/inputquantity_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }
	   
	    // List materials by id for input price
	    $scope.listMaterialsByIdPrice = function(idmaterialbaseline){
	    	materialbaselinedetailService.getAllById(idmaterialbaseline).then(function(response){
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
	    $scope.listMaterialsByIdQuantity = function(idmaterialbaseline){
	    	materialbaselinedetailService.getAllById(idmaterialbaseline).then(function(response){
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
	    
	    $scope.materialbaselineUpdatePrice = [];
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
	    		$scope.materialbaselineUpdatePrice = $scope.materialdetaillist
		    	delete $scope.materialbaselineUpdatePrice.materialname
		    	// Create With Select Detail
		    	materialbaselinedetailService.materialbaselineUpdatePrice(JSON.stringify($scope.materialbaselineUpdatePrice))
				.then(function(response){
					if(response.status === httpStatus.code.OK) {
						$scope.listMaterialsByIdPrice($scope.idmaterialbaseline);
						$scope.listWithCriteriasByIdmaterialbaselineAndPage($scope.idmaterialbaseline, 1);
						$scope.showMessageOnToast($translate.instant('clientwh_home_update_price_successfully'))
					}	
				},function(response){
					$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
				});
				$scope.materialbaselineUpdatePrice = [];
	    	}
	    }
	    
	    $scope.materialbaselineUpdateQuantity = [];
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
	    		$scope.materialbaselineUpdateQuantity = $scope.materialdetaillistQuantity
		    	delete $scope.materialbaselineUpdateQuantity.materialname
		    	// Create With Select Detail
		    	materialbaselinedetailService.materialbaselineUpdateQuantity(JSON.stringify($scope.materialbaselineUpdateQuantity))
				.then(function(response){
					if(response.status === httpStatus.code.OK) {
						$scope.listMaterialsByIdQuantity($scope.idmaterialbaseline);
						$scope.listWithCriteriasByIdmaterialbaselineAndPage($scope.idmaterialbaseline, 1);
						$scope.showMessageOnToast($translate.instant('clientwh_home_update_quantity_successfully'))
					}	
				},function(response){
					$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
				});
				$scope.materialbaselineUpdateQuantity = [];
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
	        var htmlUrlTemplate = clientwh.contextPath + '/view/materialbaselinedetail_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }

	    // Create new.
		$scope.createNew = function() {
			$scope.materialbaselinedetail = { id: -1, idmaterialbaseline: $scope.idmaterialbaseline };
			$scope.ctlentityfieldMaterial.searchText = null;
			$scope.ctlentityfieldMaterial.selectedItem = null;
			$scope.ctlentityfieldSupplier.searchText = null;
			$scope.ctlentityfieldSupplier.selectedItem = null;
			$scope.unitName = null;
		}
		
		// material exist
		$scope.resetMaterialexist = function(){
			delete $scope.frmMaterialbaselinedetail.idmaterial.$error.duplicate;
			$scope.frmMaterialbaselinedetail.idmaterial.$valid = true;
			$scope.frmMaterialbaselinedetail.idmaterial.$invalid = false;
		}
		
		// Reset validate.
		$scope.resetValidate = function() {
			// idmaterial
			$scope.frmMaterialbaselinedetail.idmaterial.$setPristine();
			$scope.frmMaterialbaselinedetail.idmaterial.$setUntouched();
			$scope.ctlentityfieldMaterial.searchText = undefined;
			$scope.ctlentityfieldMaterial.selectedItem = undefined;
			//idsupplier
			$scope.frmMaterialbaselinedetail.idsupplier.$setPristine();
			$scope.frmMaterialbaselinedetail.idsupplier.$setUntouched();
			$scope.ctlentityfieldSupplier.searchText = undefined;
			$scope.ctlentityfieldSupplier.selectedItem = undefined;
			//price
			$scope.frmMaterialbaselinedetail.price.$setPristine();
			$scope.frmMaterialbaselinedetail.price.$setUntouched();
			//quantity
			$scope.frmMaterialbaselinedetail.quantity.$setPristine();
			$scope.frmMaterialbaselinedetail.quantity.$setUntouched();
/*			$scope.frmMaterialbaselinedetail.startdate.$invalid = false;
			$scope.frmMaterialbaselinedetail.startdate.$touched = false;
			$scope.frmMaterialbaselinedetail.enddate.$invalid = false;
			$scope.frmMaterialbaselinedetail.enddate.$touched = false;*/
			
			 // form.
			$scope.frmMaterialbaselinedetail.$setPristine();
			$scope.frmMaterialbaselinedetail.$setUntouched();
			$scope.frmDirty = false;
			
			$scope.ctlentityfieldMaterial.selectedItem = null;
			$scope.frmMaterialbaselinedetail.idmaterial.$invalid = false;
			$scope.ctlentityfieldSupplier.selectedItem = null;
			$scope.frmMaterialbaselinedetail.idsupplier.$invalid = false;
		}
		
		// Create on form.
		$scope.createOnForm = function() {
			$scope.materialbaselinedetail = { id: -1, idmaterialbaseline: $scope.idmaterialbaseline };
			$scope.newdate();
			// Clear all field
			$scope.resetValidate();
			$scope.resetMaterialexist();
		}
		
		
		//////////////////SHOW	////////////////////
		// Show all combo box.
		$scope.listAllForSelect = function () {
			var listAllSelectDeferred = $q.defer();
			// Init data for select.
			$scope.materials = [];
			$scope.suppliers = [];
			// Call service
			var listMaterial = materialService.listAllForSelect();
			var listSupplier = supplierService.listAllForSelect();
			// Response.
			$q.all([listMaterial, listSupplier]).then(
				// Successes.
				function (responses) {
					$scope.materials = responses[0].data;
					$scope.suppliers = responses[1].data;
					
					$scope.ctlentityfieldMaterial.items = $scope.materials
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
			for (var i = 0; i < $scope.ctlentityfieldMaterial.items.length; i++) {
				if ($scope.materialbaselinedetail.idmaterial == $scope.ctlentityfieldMaterial.items[i].value) {
					$scope.ctlentityfieldMaterial.selectedItem = {};
					$scope.ctlentityfieldMaterial.selectedItem = $scope.ctlentityfieldMaterial.items[i];
				}
			}
			for (var i = 0; i < $scope.ctlentityfieldSupplier.items.length; i++) {
				if ($scope.materialbaselinedetail.idsupplier == $scope.ctlentityfieldSupplier.items[i].id) {
					$scope.ctlentityfieldSupplier.selectedItem = {};
					$scope.ctlentityfieldSupplier.selectedItem = $scope.ctlentityfieldSupplier.items[i];
				}
			}
		}
		
		$scope.resetInvalidMaterial = function(){
			$scope.frmMaterialbaselinedetail.idmaterial.$invalid = true;
		}
	
		$scope.resetInvalidSupplier = function(){
			$scope.frmMaterialbaselinedetail.idsupplier.$invalid = true;
		}
		
		// Save.
		$scope.save = function() {
			if($scope.ctlentityfieldMaterial.selectedItem==null){
				$scope.frmMaterialbaselinedetail.idmaterial.$invalid = true;
				$scope.frmMaterialbaselinedetail.idmaterial.$touched = true;
			}
			if($scope.ctlentityfieldSupplier.selectedItem==null){
				$scope.frmMaterialbaselinedetail.idsupplier.$invalid = true;
				$scope.frmMaterialbaselinedetail.idsupplier.$touched = true;
			}
			if($scope.materialbaselinedetail.startdate==null){
				$scope.frmMaterialbaselinedetail.startdate.$invalid = true;
				$scope.frmMaterialbaselinedetail.startdate.$touched = true;
			}
			if($scope.materialbaselinedetail.enddate==null){
				$scope.frmMaterialbaselinedetail.enddate.$invalid = true;
				$scope.frmMaterialbaselinedetail.enddate.$touched = true;
			}
			if($scope.frmMaterialbaselinedetail.$invalid) {
				$scope.frmMaterialbaselinedetail.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			$scope.showMessageOnToast($translate.instant('clientwh_home_saving'));
			$scope.subtractTwoDate();
			if ($scope.subtractTwoDate()==false) {
				$scope.frmMaterialbaselinedetail.startdate.$invalid = true;
				$scope.frmMaterialbaselinedetail.enddate.$invalid = true;
				$scope.showMessageOnToast($translate.instant('clientwh_materialbaselinedetail_date'));
				return;
			}
			var result;
			if($scope.materialbaselinedetail.id > -1) {
				result = materialbaselinedetailService.updateWithLock($scope.materialbaselinedetail.id, $scope.materialbaselinedetail);
			} else {
				result = materialbaselinedetailService.create($scope.materialbaselinedetail);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.materialbaselinedetail.id > -1) {
						$scope.materialbaselinedetail.version = response.data;
					} else {
						$scope.materialbaselinedetail.id = response.data;
						$scope.materialbaselinedetail.version = 1;
					}
					$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
					$scope.listWithCriteriasByIdmaterialbaselineAndPage($scope.idmaterialbaseline, 1);
					//sum totalamount
					$scope.Sumtotalamount();
				} else {
					if(response.data.code == clientwh.serverCode.EXISTSCOPE) {
						$scope.frmMaterialbaselinedetail.scope.$invalid = true;
						$scope.showMessageOnToast($translate.instant('clientwh_servercode_' + response.data.code));
					} else if(response.data.code == clientwh.serverCode.VERSIONDIFFERENCE) {
						$scope.showMessageOnToast($translate.instant('clientwh_servercode_' + response.data.code));
					} else if(response.data.code == clientwh.serverCode.EXITMATERIAL) {
						$scope.frmMaterialbaselinedetail.idmaterial.$invalid = true;
						$scope.showMessageOnToast($translate.instant('clientwh_materialbaselinedetail_exit_material'));
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
		
		//Reset validate duplicate
	    $scope.resetValidateDuplicate = function(){
	    	delete $scope.frmMaterialbaselinedetail.code.$error.duplicate;
	    }
	    
		// Delete.
		$scope.delete = function(id, version) {
				materialbaselinedetailService.updateForDeleteWithLock(id, version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToast($translate.instant('clientwh_home_deleted'));
						$scope.listWithCriteriasByIdmaterialbaselineAndPage($scope.idmaterialbaseline, 1);
						//sum totalamount
						$scope.Sumtotalamount();
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
				materialbaselinedetailService.updateForDeleteWithLock($scope.materialbaselinedetail.id, $scope.materialbaselinedetail.version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToast($translate.instant('clientwh_home_deleted'));
						$scope.createNew();
						$scope.resetValidate();
						$scope.listWithCriteriasByIdmaterialbaselineAndPage($scope.idmaterialbaseline, 1);
						//sum totalamount
						$scope.Sumtotalamount();
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
			materialbaselinedetailService.getById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.materialbaselinedetail = data;
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
			materialbaselinedetailService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.materialbaselinedetails = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.materialbaselinedetails = result;
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
		$scope.listWithCriteriasByIdmaterialbaselineAndPage = function(idmaterialbaseline, pageNo) {
			$scope.page.currentPage = pageNo;
			materialbaselinedetailService.listWithCriteriasByIdmaterialbaselineAndPage(idmaterialbaseline, $scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.materialbaselinedetails = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.materialbaselinedetails = result;
						if(result.length > 0) {
							$scope.page.totalElements = response.data.totalElements;
						}
					}
					//sum totalamount
					$scope.Sumtotalamount();
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
			$scope.listWithCriteriasByIdmaterialbaselineAndPage($scope.idmaterialbaseline, 1);
		}

		/*Extend functions*/
		
		// Sort by.
		$scope.sortBy = function(keyName){
			$scope.sortKey = keyName;
			$scope.reverse = !$scope.reverse;
			// Reload data.
			$scope.listWithCriteriasByIdmaterialbaselineAndPage($scope.idmaterialbaseline,$scope.page.currentPage);
		}
		
		$scope.clearSortBy = function(){
			$scope.sortKey = '';
			$scope.sortName = null;
			// Reload data.
			$scope.listWithCriteriasByIdmaterialbaselineAndPage($scope.idmaterialbaseline,$scope.page.currentPage);
		}
		
		$scope.sortByName = clientwh.sortByNameMaterialBaselineDetail;
		
		
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
		    if(typeof($scope.search.content) !== 'undefined' && $scope.search.content !== ''){
				// name.
		    	result.push({ key: 'material.name', operation: 'like', value: $scope.search.content, logic: 'or' });
				// code.
		    	//result.push({ key: 'code', operation: 'like', value: $scope.search.content, logic: 'or' });
				// idmaterial.
		    	//result.push({ key: 'idmaterial', operation: 'like', value: $scope.search.content, logic: 'or' });
				// idunit.
		    	//result.push({ key: 'idunit', operation: 'like', value: $scope.search.content, logic: 'or' });
				// price.
		    	//result.push({ key: 'price', operation: 'like', value: $scope.search.content, logic: 'or' });
				// quantity.
		    	//result.push({ key: 'quantity', operation: '=', value: $scope.search.content, logic: 'or' });
				// amount.
		    	//result.push({ key: 'amount', operation: 'like', value: $scope.search.content, logic: 'or' });
				// startdate.
		    	//result.push({ key: 'startdate', operation: 'like', value: $scope.search.content, logic: 'or' });
				// enddate.
		    	//result.push({ key: 'enddate', operation: 'like', value: $scope.search.content, logic: 'or' });
				// note.
		    	//result.push({ key: 'note', operation: 'like', value: $scope.search.content, logic: 'or' });
		    }
		    // return.
		    return result;
		}
		
		// Amount = Quantity * Price
		$scope.materialbaselinedetail.amount = 0;
		 $scope.CalculateSum = function () {
		   $scope.materialbaselinedetail.amount += ( $scope.materialbaselinedetail.price *  $scope.materialbaselinedetail.quantity);
		 } 
		$scope.ResetTotalAmt = function () {
		   $scope.materialbaselinedetail.amount = 0;
		}
		
		// sum amount.
		$scope.Sumtotalamount = function() {
			materialbaselinedetailService.sumAmount($scope.$parent.materialbaseline.id).then(
			// success.
			function(response) {
				$scope.$parent.materialbaseline.totalamount = response.data;
				$scope.$parent.listWithCriteriasByPage(1);
			},
			// error.
			function(response) {
				$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
			});
		}
		
		////////////////////////////////////////
		// Auto complete: idmaterial
		////////////////////////////////////////
		$scope.ctlentityfieldMaterial = {};
		$scope.ctlentityfieldMaterial.isCallServer = false;
		$scope.ctlentityfieldMaterial.isDisabled = false;

		// New.
		$scope.ctlentityfieldMaterial.newState = function (item) {
			alert("Sorry! You'll need to create a Constitution for " + item + " first!");
		}
		// Search in array.
		$scope.ctlentityfieldMaterial.querySearch = function (query) {
			var results = query ? $scope.ctlentityfieldMaterial.items.filter($scope.ctlentityfieldMaterial.createFilterFor(query)) : $scope.ctlentityfieldMaterial.items,
				deferred;

			if ($scope.ctlentityfieldMaterial.isCallServer) {
				deferred = $q.defer();
				$timeout(function () { deferred.resolve(results); }, Math.random() * 1000, false);
				return deferred.promise;
			} else {
				return results;
			}
		}
		// Filter.
		$scope.ctlentityfieldMaterial.createFilterFor = function (query) {
			var lowercaseQuery = angular.lowercase(query);
			return function filterFn(item) {
				return (angular.lowercase(item.display).indexOf(lowercaseQuery) >= 0);
			};
		}
		// Text change.
		$scope.ctlentityfieldMaterial.searchTextChange = function (text) {
			$log.info('Text changed to ' + text);
		}
		// Item change.
		$scope.ctlentityfieldMaterial.selectedItemChange = function (item) {
			if ($scope.frmMaterialbaselinedetail.idmaterial === undefined) {
				return;
			}
			$scope.materialbaselinedetail.idmaterial = undefined;
			$scope.frmMaterialbaselinedetail.idmaterial.$invalid = true;
			if (item) {
				$scope.resetMaterialexist();
				$scope.materialbaselinedetail.idmaterial = item.value;
				$scope.frmMaterialbaselinedetail.idmaterial.$invalid = false;
				$scope.unitName = item.unitName;
				$scope.materialbaselinedetail.idunit = item.value;
				$scope.frmMaterialbaselinedetail.idunit.$invalid = false;
			}
	    	else {
	    		$scope.materialbaselinedetail.idunit = null;
	    		$scope.unitName = null;
	    	}
		}
		$scope.unitName;
		
		////////////////////////////////////////
		// Auto complete: idsupplier
		////////////////////////////////////////
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
			if ($scope.frmMaterialbaselinedetail.idsupplier === undefined) {
				return;
			}
			$scope.materialbaselinedetail.idsupplier = undefined;
			$scope.frmMaterialbaselinedetail.idsupplier.$invalid = true;
			if (item) {
				$scope.materialbaselinedetail.idsupplier = item.id;
				$scope.frmMaterialbaselinedetail.idsupplier.$invalid = false;
			}
		}
	
		// Subtract 2 dates.
		$scope.subtractTwoDate = function() {
			var startdate = moment($scope.materialbaselinedetail.startdate, 'dd/MM/yyyy');
			var enddate = moment($scope.materialbaselinedetail.enddate, 'dd/MM/yyyy');
			var duration = enddate.diff(startdate, 'days');
			//alert(duration);
			if (duration <= 0) {
				return false;
			}
			else {
				return true;
			}
		}
	
	}]);

});
