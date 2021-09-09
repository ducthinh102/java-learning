
/**
 * Controller for Materialformdetail
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'materialformdetailController', ['$q', '$mdToast', '$stateParams','moment', '$scope', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', clientwh.prefix + 'materialformdetailService', clientwh.prefix + 'materialService',
		function($q, $mdToast, $stateParams, moment, $scope, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, materialformdetailService, materialService) {
		if(typeof(clientwh.translate.materialformdetail) === 'undefined' || clientwh.translate.materialformdetail.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.materialformdetail) === 'undefined') {
				clientwh.translate.materialformdetail = '';
			}
			clientwh.translate.materialformdetail += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/materialformdetail');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_materialformdetail_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    $translate.onReady().then(function() {
	    	$scope.title = $translate.instant('clientwh_materialformdetail_title');
	    	$translate.refresh();
	    });
		
	    // Search.
	    $scope.search = {};
	    
	    $scope.idmaterialform = undefined;
	    
		// Paging.
		$scope.page = {
			pageSize: 9,
			totalElements: 0,
			currentPage: 0
		}
		
		$scope.materialformdetail = {};
		
		$scope.newdate = function() {
			$scope.materialformdetail.startdate = new Date();
			$scope.materialformdetail.enddate = new Date();
		}
		
		$scope.defaultprice = function() {
			$scope.materialformdetail.price = 0;
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
						controller  : 'clientwhmaterialformdetailController',
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
						controller  : 'clientwhmaterialformdetailController',
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
		$rootScope.idmaterialformdetailController = $scope.$id;
		$scope.initList = function() {
			if(typeof(listAllSelectPromise) === 'undefined') {
				var listAllSelectDefered = $q.defer();
				listAllSelectPromise = listAllSelectDefered.promise;
				listAllSelectDefered.resolve([]);
			}
			listAllSelectPromise.then(
				// Success.
				function(response) {
					// watch idmaterialform.
					if($rootScope.watchidmaterialform) {
						$rootScope.watchidmaterialform();
					}
					$rootScope.watchidmaterialform = $scope.$watch (
						function(scope) {
							return scope.$parent && scope.$parent.materialform ? scope.$parent.materialform.id : null;
						},
						function(newValue, oldValue) {
							if(newValue > -1 && oldValue==newValue && $scope.$parent.formdetailcopy.length!=0 ){
								 var t = $stateParams.formcopy;
								$scope.materialformdetailsCopy = $scope.$parent.formdetailcopy
								$scope.page.totalElements = $scope.materialformdetailsCopy.length;
								$scope.idmaterialform = newValue; 
								$scope.materialformdetails = [];
								for(var i = 0; i < $scope.page.totalElements; i++){
									var materialformdetail = {idmaterialform: newValue, idmaterial: $scope.materialformdetailsCopy[i].idmaterial, note: $scope.materialformdetailsCopy[i].note }
									
									if (typeof $scope.materialformdetailsCopy[i].idunit !== "undefined") {
										materialformdetail.idunit = $scope.materialformdetailsCopy[i].idunit;
									}
									else {materialformdetail.idunit = null;}
									
									if (typeof $scope.materialformdetailsCopy[i].price !== "undefined") {
										materialformdetail.price = $scope.materialformdetailsCopy[i].price;
									}
									else {materialformdetail.price = 0;}
									
									if (typeof $scope.materialformdetailsCopy[i].quantity !== "undefined") {
										materialformdetail.quantity = $scope.materialformdetailsCopy[i].quantity;
									}
									else {materialformdetail.quantity = 0;}
									
									if (typeof $scope.materialformdetailsCopy[i].amount !== "undefined") {
										materialformdetail.amount = $scope.materialformdetailsCopy[i].amount;
									}
									else {materialformdetail.amount = 0;}
									
									if (typeof $scope.materialformdetailsCopy[i].startdate !== "undefined") {
										materialformdetail.startdate = $scope.materialformdetailsCopy[i].startdate;
									}
									else {materialformdetail.startdate = null;}
									
									if (typeof $scope.materialformdetailsCopy[i].enddate !== "undefined") {
										materialformdetail.enddate = $scope.materialformdetailsCopy[i].enddate;
									}
									else {materialformdetail.enddate = null;}
									
									$scope.materialformdetails.push(materialformdetail);
								}
								materialformdetailService.createWithSelectDetail(JSON.stringify($scope.materialformdetails)).then(function(response){
									if($scope.idmaterialform > -1 && $rootScope.idmaterialformdetailController == $scope.$id) {
										$scope.listWithCriteriasByIdmaterialformAndPage($scope.idmaterialform, 1);
										$scope.$parent.formdetailcopy = [];
										if($scope.$parent.$stateParams) { 
											$scope.$parent.$stateParams.formcopy = {};
											$scope.$parent.$stateParams.formdetailcopy = {};
										}
									}
								},function(response){
									
								})
							} else {
								if($scope.$parent.materialform) {
									$scope.materialformdetails = [];
									$scope.page.totalElements = 0;
									$scope.idmaterialform = $scope.$parent.materialform.id;
									if($scope.idmaterialform > -1 && $rootScope.idmaterialformdetailController == $scope.$id) {
										$scope.listWithCriteriasByIdmaterialformAndPage($scope.idmaterialform, 1);
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
			// Init data for select.
			$scope.listMaterial = [];
			// Call service.
			var listAllForSelectDeferred = materialService.listAllForSelect();
			// Response.
			$q.all([listAllForSelectDeferred]).then(
				// Successes.
				function(responses) {
				
					$scope.ctlidmaterial.items  = responses[0].data;
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
			// set idmaterialform.
			$scope.idmaterialform = $scope.$parent ? $scope.$parent.materialform.id : -1;
			$scope.createNew();
			$scope.materialformdetail.id = id;
			if($scope.materialformdetail.id > -1) {
				$scope.getById($scope.materialformdetail.id);
			}
			$scope.frmDirty = false;
		}
		
		// Show a create screen.
		$scope.showCreate = function() {
			$scope.initForm(-1);
			$scope.showFormDialog();
			$scope.ctlidmaterial.selectedItem = null;
		}
		
		// Show a form screen.
		$scope.showForm = function(id) {
			$scope.initForm(id);
			$scope.showFormDialog();
		}

		//Show Dialog with controller.
		$scope.showDialogWithController = function() {
			var params = {};
			if ($scope.$parent.materialform.scope=='prematerial') {
				params = {name:'materialformprematerial',id:$scope.idmaterialform}
			}
			else if($scope.$parent.materialform.scope=='techmaterial'){
				params = {name:'materialformtechmaterial',id:$scope.idmaterialform}
			}
			var htmlUrlTemplate = clientwh.contextPath + '/view/selectfordetail_form.html';
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

		//Show Dialog with controller for compare list of materials.
		$scope.showDialogWithControllerForCompare = function() {
			var params = {};
			if ($scope.$parent.materialform.scope=='prematerial') {
				params = {name:'materialformprematerial',id:$scope.idmaterialform}
			}
			else if($scope.$parent.materialform.scope=='techmaterial'){
				params = {name:'materialformtechmaterial',id:$scope.idmaterialform}
			}
			var htmlUrlTemplate = clientwh.contextPath + '/view/comparemateriallist_form.html';
			clientwh.showDialogWithControllerName(clientwh.prefix + 'comparemateriallistController', 'comparemateriallistController', $mdDialog, htmlUrlTemplate, params).then(function(response) {
				if(response.result){
					$scope.listWithCriteriasByIdmaterialformAndPage($scope.idmaterialform, 1);
				}
				console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
		}
		
		$scope.creatForSelectDetail = function(materials){
			$scope.materialformSelected = [];
			for (var i = 0; i < materials.length; i++) {
				var materialform = {};
				materialform.id = -1;
				materialform.idmaterialform = $scope.idmaterialform;
				materialform.idmaterial = materials[i].idmaterial;
				materialform.note = materials[i].note;
				if (typeof materials[i].price !== "undefined") {
					materialform.price = materials[i].price;
				}
				else {materialform.price = null;}
				if (typeof materials[i].idunit !== "undefined") {
					materialform.idunit = materials[i].idunit;
				}
				else {materialform.idunit = null;}
				
				if (typeof materials[i].quantity !== "undefined") {
					materialform.quantity = materials[i].quantity;
				}
				else {materialform.quantity = 0;}
				
				if (typeof materials[i].amount !== "undefined") {
					materialform.amount = materials[i].amount;
				}
				else {materialform.quantity = 0;}
				
				if (typeof materials[i].startdate !== "undefined") {
					materialform.startdate = materials[i].startdate;
				}
				else {materialform.startdate = null;}
				if (typeof materials[i].enddate !== "undefined") {
					materialform.enddate = materials[i].enddate;
				}
				else {materialform.enddate = null;}
				$scope.materialformSelected.push(materialform);
			}
			// Create With Select Detail
			materialformdetailService.createWithSelectDetail(JSON.stringify($scope.materialformSelected))
			.then(function(response){
				$scope.listWithCriteriasByIdmaterialformAndPage($scope.idmaterialform, 1);
			},function(response){
				$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
			});
		}
		
		$scope.inputPrice = {};
		$scope.indexItemPrice = 0;
		$scope.inputQuantity = {};
		$scope.indexItemQuantity = 0;
		// Show input price view.
	    $scope.showInputPriceFormDialog = function () {
	    	$scope.indexItemPrice = 0;
	    	$scope.listMaterialsByIdPrice($scope.idmaterialform);
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
	    	$scope.listMaterialsByIdQuantity($scope.idmaterialform);
	        var htmlUrlTemplate = clientwh.contextPath + '/view/inputquantity_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }
	   
	    // List materials by id for input price
	    $scope.listMaterialsByIdPrice = function(idmaterialform){
	    	materialformdetailService.getAllById(idmaterialform).then(function(response){
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
	    $scope.listMaterialsByIdQuantity = function(idmaterialform){
	    	materialformdetailService.getAllById(idmaterialform).then(function(response){
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
	    
	    $scope.materialformUpdatePrice = [];
	    $scope.idselectedPrice = [];
	    
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
	    
	    // Save update price
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
	    		$scope.materialformUpdatePrice = $scope.materialdetaillist
		    	delete $scope.materialformUpdatePrice.materialname
		    	// Create With Select Detail
				materialformdetailService.materialformUpdatePrice(JSON.stringify($scope.materialformUpdatePrice))
				.then(function(response){
					if(response.status === httpStatus.code.OK) {
						$scope.listMaterialsByIdPrice($scope.idmaterialform);
						$scope.listWithCriteriasByIdmaterialformAndPage($scope.idmaterialform, 1);
						$scope.$parent.listWithCriteriasByPage(1);
						$scope.showMessageOnToast($translate.instant('clientwh_home_update_price_successfully'))
					}	
				},function(response){
					$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
				});
				$scope.materialformUpdatePrice = [];
	    	}
	    }
	    
	    $scope.materialformUpdateQuantity = [];
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
	    		$scope.materialformUpdateQuantity = $scope.materialdetaillistQuantity
		    	delete $scope.materialformUpdateQuantity.materialname
		    	// Create With Select Detail
				materialformdetailService.materialformUpdateQuantity(JSON.stringify($scope.materialformUpdateQuantity))
				.then(function(response){
					if(response.status === httpStatus.code.OK) {
						$scope.listMaterialsByIdQuantity($scope.idmaterialform);
						$scope.$parent.listWithCriteriasByPage(1);
						$scope.listWithCriteriasByIdmaterialformAndPage($scope.idmaterialform, 1);
						$scope.showMessageOnToast($translate.instant('clientwh_home_update_quantity_successfully'))
					}	
				},function(response){
					$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
				});
				$scope.materialformUpdateQuantity = [];
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
	    $scope.showFormDialog = function () {
	        var htmlUrlTemplate = clientwh.contextPath + '/view/materialformdetail_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }

	    // Create new.
		$scope.createNew = function() {
			$scope.materialformdetail = { id: -1, idmaterialform: $scope.idmaterialform };
			$scope.ctlidmaterial.searchText = null;
			$scope.ctlidmaterial.selectedItem = null;
		}
		
		$scope.resetInvalidMaterial = function(){
			$scope.frmMaterialformdetail.idmaterial.$invalid = true;
		}
		
		/*$scope.resetInvalidStartdate = function(){
			$scope.frmMaterialformdetail.startdate.$invalid = true;
		}
		
		$scope.resetInvalidEnddate = function(){
			$scope.frmMaterialformdetail.enddate.$invalid = true;
		}*/
		
		// Create on form.
		$scope.createOnForm = function() {
			$scope.resetMaterialexist();
			// Clear all field
			$scope.materialformdetail = { id: -1, idmaterialform: $scope.idmaterialform };
			$scope.newdate();
			$scope.resetValidate();
		}
			
		$scope.resetValidate = function(){
			// idmaterial.
		    $scope.frmMaterialformdetail.idmaterial.$setPristine();
			$scope.frmMaterialformdetail.idmaterial.$setUntouched();
			// price.
		    $scope.frmMaterialformdetail.price.$setPristine();
			$scope.frmMaterialformdetail.price.$setUntouched();
			// quantity.
		    $scope.frmMaterialformdetail.quantity.$setPristine();
			$scope.frmMaterialformdetail.quantity.$setUntouched();
			/*// startdate.
		    $scope.frmMaterialformdetail.startdate.$setPristine();
			$scope.frmMaterialformdetail.startdate.$setUntouched();
			// enddate.
		    $scope.frmMaterialformdetail.enddate.$setPristine();
			$scope.frmMaterialformdetail.enddate.$setUntouched();*/

		    // form.
			$scope.frmMaterialformdetail.$setPristine();
			$scope.frmMaterialformdetail.$setUntouched();
			// frmDirty.
			$scope.frmDirty = false;
			
			$scope.frmMaterialformdetail.idmaterial.$invalid = false;
			
			$scope.ctlidmaterial.searchText = null;
			$scope.ctlidmaterial.selectedItem = null;
		}
		
		// material exist
		$scope.resetMaterialexist = function(){
			delete $scope.frmMaterialformdetail.idmaterial.$error.exitsmaterial;
			$scope.frmMaterialformdetail.idmaterial.$valid = true;
			$scope.frmMaterialformdetail.idmaterial.$invalid = false;
		}
		
		
		// Save.
		$scope.save = function() {
			if ($scope.materialformdetail.idmaterial==null) {
				$scope.frmMaterialformdetail.idmaterial.$invalid = true;
				$scope.frmMaterialformdetail.idmaterial.$touched = true;
			}
			/*if ($scope.materialformdetail.startdate==null) {
				$scope.frmMaterialformdetail.startdate.$invalid = true;
				$scope.frmMaterialformdetail.startdate.$touched = true;
			}
			
			if ($scope.materialformdetail.enddate==null) {
				$scope.frmMaterialformdetail.enddate.$invalid = true;
				$scope.frmMaterialformdetail.enddate.$touched = true;
			}*/
			if($scope.frmMaterialformdetail.$invalid || $scope.frmMaterialformdetail.startdate.$invalid || $scope.frmMaterialformdetail.enddate.$invalid) {
				$scope.frmMaterialformdetail.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			$scope.showMessageOnToast($translate.instant('clientwh_home_saving'));
			
			
			$scope.subtractTwoDate();
			if ($scope.subtractTwoDate()==false) {
				$scope.showMessageOnToast($translate.instant('clientwh_materialformdetail_date'));
				return;
			}
			
			var result;
			if($scope.materialformdetail.id > -1) {
				result = materialformdetailService.updateWithLock($scope.materialformdetail.id, $scope.materialformdetail);
			} else {
				result = materialformdetailService.create($scope.materialformdetail);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.materialformdetail.id > -1) {
						$scope.materialformdetail.version = response.data;
					} else {
						$scope.materialformdetail.id = response.data;
						$scope.materialformdetail.version = 1;
					}
					$scope.Sumtotalamount();
					$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
					$scope.listWithCriteriasByIdmaterialformAndPage($scope.idmaterialform, 1);
					$scope.$parent.listWithCriteriasByPage(1);
					// Ignore time
					$scope.materialformdetail.startdate = clientmain.getDateIgnoreTime($scope.materialformdetail.startdate);
					$scope.materialformdetail.enddate = clientmain.getDateIgnoreTime($scope.materialformdetail.enddate);
				} else {
					if(response.data.code == clientwh.serverCode.EXISTSCOPE) {
						$scope.frmMaterialformdetail.scope.$invalid = true;
						$scope.showMessageOnToast($translate.instant('clientwh_servercode_' + response.data.code));
					} else if(response.data.code == clientwh.serverCode.VERSIONDIFFERENCE) {
						$scope.showMessageOnToast($translate.instant('clientwh_servercode_' + response.data.code));
					} else if(response.data.code == clientwh.serverCode.EXITMATERIAL) {
						$scope.frmMaterialformdetail.idmaterial.$error.exitsmaterial = true;
						$scope.frmMaterialformdetail.idmaterial.$valid = false;
						$scope.frmMaterialformdetail.idmaterial.$invalid = true;
						$scope.showMessageOnToast($translate.instant('clientwh_materialformdetail_existmaterial'));
					}  
					else {
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
			materialformdetailService.updateForDeleteWithLock(id, version)
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.showMessageOnToastList($translate.instant('clientwh_home_deleted'));
					$scope.Sumtotalamount();
					$scope.listWithCriteriasByIdmaterialformAndPage($scope.idmaterialform, 1);
					$scope.$parent.listWithCriteriasByPage(1);
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
			materialformdetailService.updateForDeleteWithLock($scope.materialformdetail.id, $scope.materialformdetail.version)
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.showMessageOnToast($translate.instant('clientwh_home_deleted'));
					$scope.Sumtotalamount();
					$scope.createNew();
					$scope.resetValidate();
					$scope.listWithCriteriasByIdmaterialformAndPage($scope.idmaterialform, 1);
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
			materialformdetailService.getById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.materialformdetail = data;
					$scope.materialformdetail.startdate = new Date($scope.materialformdetail.startdate);
					$scope.materialformdetail.enddate = new Date($scope.materialformdetail.enddate);
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
			materialformdetailService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.materialformdetails = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.materialformdetails = result;
						
						for (var i = 0; i < $scope.materialformdetails.length; i++) {
							if ($scope.materialformdetails[i].startdate!=null) {
								$scope.materialformdetails[i].startdate = new Date($scope.materialformdetails[i].startdate).toLocaleDateString();
							}
						}
						
						for (var i = 0; i < $scope.materialformdetails.length; i++) {
							if ($scope.materialformdetails[i].enddate!=null) {
								$scope.materialformdetails[i].enddate = new Date($scope.materialformdetails[i].enddate).toLocaleDateString();
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
				$scope.showMessageOnToastList($translate.instant('clientwh_home_error'));
			});
		}
		
		// List for page and filter.
		$scope.listWithCriteriasByIdmaterialformAndPage = function(idmaterialform, pageNo) {
			$scope.page.currentPage = pageNo;
			materialformdetailService.listWithCriteriasByIdmaterialformAndPage(idmaterialform, $scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.materialformdetails = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.materialformdetails = result;
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
		
		// Clear filter search.
		$scope.clearFilterSearch = function() {
			$scope.search.content = "";
			$scope.listWithCriteriasByIdmaterialformAndPage($scope.idmaterialform, 1);
		}

		/*Extend functions*/
		
		$scope.clearSortBy = function(){
			$scope.sortKeyDetail = '';
			$scope.sortByArr = {};
			// Reload data.
			$scope.listWithCriteriasByIdmaterialformAndPage($scope.idmaterialform,$scope.page.currentPage);
		}
		
		$scope.sortByName = clientwh.sortByNamematerialformdetaildetail;
		
		$scope.sortByArr = {};
		
		// Sort by.
		$scope.sortBy = function(keyName){
			$scope.sortKeyDetail = keyName;
			$scope.reverse = !$scope.reverse;
			// Reload data.
			$scope.listWithCriteriasByIdmaterialformAndPage($scope.idmaterialform,$scope.page.currentPage);
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
		    	result.push({ key: 'material.name', operation: 'like', value: $scope.search.content, logic: 'or' },
		    				{ key: 'material.code', operation: 'like', value: $scope.search.content, logic: 'or' });
				
				// quantity.
		    	if (parseInt($scope.search.content)) {
		    		result.push({ key: 'quantity', operation: '=', value: $scope.search.content, logic: 'or' },
		    					{ key: 'price', operation: '=', value: $scope.search.content, logic: 'or' });
				}
		    }
		    // return.
		    return result;
		}
			
		// Amount = Quantity * Price
		$scope.materialformdetail.amount = 0;
		 $scope.CalculateSum = function () {
		   $scope.materialformdetail.amount += ( $scope.materialformdetail.price *  $scope.materialformdetail.quantity);
		 }
		$scope.ResetTotalAmt = function () {
		   $scope.materialformdetail.amount = 0;
		}
		
		// sum amount.
		$scope.Sumtotalamount = function() {
			materialformdetailService.sumAmount($scope.$parent.materialform.id).then(
			// success.
			function(response) {
				$scope.$parent.materialform.totalamount = response.data;
			},
			// error.
			function(response) {
				$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
			});
		}
		
		$scope.itemSelected = function(){
			for (var i = 0; i < $scope.ctlidmaterial.items.length; i++) {
				if($scope.materialformdetail.idmaterial==$scope.ctlidmaterial.items[i].value){
					$scope.ctlidmaterial.selectedItem = {};
					$scope.ctlidmaterial.selectedItem = $scope.ctlidmaterial.items[i];
					break;
				}
			}
		}
		
		////////////////////////////////////////
		// Auto complete: idMaterial
		////////////////////////////////////////
		$scope.ctlidmaterial= {};		
		$scope.ctlidmaterial.isCallServer = false;
	    $scope.ctlidmaterial.isDisabled    = false;	    
	    
	    // New.
	    $scope.ctlidmaterial.newState = function(item) {
	      alert("Sorry! You'll need to create a Constitution for " + item + " first!");
	    }
	    // Search in array.
	    $scope.ctlidmaterial.querySearch = function(query) {
	      var results = query ? $scope.ctlidmaterial.items.filter( $scope.ctlidmaterial.createFilterFor(query) ) : $scope.ctlidmaterial.items,
	          deferred;
	      
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
	    	if($scope.frmMaterialformdetail.idmaterial === undefined) {
	    		return;
	    	}
	    	$scope.materialformdetail.idmaterial = undefined;
	    	$scope.frmMaterialformdetail.idmaterial.$invalid = true;
	    	if(item) {
	    		$scope.resetMaterialexist();
	    		$scope.materialformdetail.idmaterial = item.value;
	    		$scope.materialformdetail.idunit = item.idunit;
	    		$scope.frmMaterialformdetail.idmaterial.$invalid = false;
	    	}
	    }
		
		// Show message.
		$scope.showMessage = function(message, cssName, autoHide) {
			$scope.materialformdetailAlertMessage = message;
			$('#materialformdetailAlertMessage').removeClass('alert-danger');
			$('#materialformdetailAlertMessage').removeClass('alert-success');
			$('#materialformdetailAlertMessage').addClass(cssName);
			$('#materialformdetailAlertMessage').slideDown(1000, function() {
				if(autoHide) {
					$window.setTimeout(function() {
						$('#materialformdetailAlertMessage').slideUp(1000, function() {
							$('#materialformdetailAlertMessage').removeClass(cssName);
		            	});
					}, 1000);
				}
			});
		}
	
		// Show detail 
		$scope.showView = function(id) {
			$scope.materialformdetail.id = id;
			if($scope.materialformdetail.id > -1) {
				$scope.getById($scope.materialformdetail.id);
			}
			$scope.showViewDialog();
		}
		
		//Show detail materialformdetail
		$scope.showViewDialog = function () {
			var htmlUrlTemplate = clientwh.contextPath + '/view/materialformdetail_view.html';
			clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function (evt) {
				console.log('closed');
			}, function (evt) {
				console.log('not closed');
			});
		}
		
		// Set date options.
    	$scope.dateOptions = {
	        formatYear: 'yyyy',
	        startingDay: 1
    	};
    	
	    
	    
	// Subtract 2 dates.
	$scope.subtractTwoDate = function() {
		var startdate = moment($scope.materialformdetail.startdate, 'dd/MM/yyyy');
		var enddate = moment($scope.materialformdetail.enddate, 'dd/MM/yyyy');
		var duration = enddate.diff(startdate, 'days');
		//alert(duration);
		if (duration < 0) {
			return false;
		}
		else {
			return true;
		}
	}
		
		
	}]);

});
