
/**
 * Controller for Quotationdetail
 */

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'quotationdetailController', ['$scope', '$mdToast', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', '$q', 'moment', clientwh.prefix + 'quotationdetailService', clientwh.prefix + 'quotationService', clientwh.prefix + 'catalogService', clientwh.prefix + 'materialService',
		function($scope, $mdToast, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, $q, moment, quotationdetailService, quotationService, catalogService, materialService) {
		if(typeof(clientwh.translate.quotationdetail) === 'undefined' || clientwh.translate.quotationdetail.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.quotationdetail) === 'undefined') {
				clientwh.translate.quotationdetail = '';
			}
			clientwh.translate.quotationdetail += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/quotationdetail');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_quotationdetail_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    $translate.onReady().then(function() {
	    	$scope.title = $translate.instant('clientwh_quotationdetail_title');
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
		
		$scope.quotation = undefined;
	    $scope.idquotation = undefined;
		$scope.quotationdetail = {};
		
		//Cofirm Delete Toast List
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
						controller  : 'clientwhquotationdetailController',
						position: 'right'}).then(function(response){
							if (response) {
								  $scope.delete(id,version);
							}
						});
		}
		
		$scope.showMessageOnToastList = function(message){
			$mdToast.show(
					{ template: '<md-toast class="md-toast">' + message + '</md-toast>',
						hideDelay:3000,
						position: 'right'})
		}
		
		//Cofirm Delete Toast Form
		$scope.cofirmDeleteToastForm = function(){
			$mdToast.show(
					{  	templateUrl : clientwh.contextPath + '/view/toast.html',
						hideDelay:5000,
						controller  : 'clientwhquotationdetailController',
						position: 'top right'}).then(function(response){
							if (response) {
								$scope.deleteOnForm();
							}
						});
		}
		
		
		$scope.showMessageOnToast = function(message){
			$mdToast.show(
					{ template: '<md-toast class="md-toast">' + message + '</md-toast>',
						hideDelay:3000,
						position: 'top right'})
		}
		
		
		// Promise list for select.
		var listAllSelectPromise;
		
		// Init for list.
		$rootScope.idquotationdetailController = $scope.$id;
		$scope.initList = function() {
			
			// Promise
			if(typeof(listAllSelectPromise) === 'undefined') {
				var listAllSelectDefered = $q.defer();
				listAllSelectPromise = listAllSelectDefered.promise;
				listAllSelectDefered.resolve([]);
			}
			listAllSelectPromise.then(
					// Success.
					function(response) {
						// watch idquotation.
						$scope.$watch(
								function(scope) {
									return scope.$parent ? scope.$parent.quotation.id : null;
								},
								function(newValue, oldValue) {
									if($scope.$parent.quotation) {
										if(newValue > -1 && oldValue==newValue && $scope.$parent.formdetailcopy.length!=0 ){
											$scope.quotationdetailsCopy = $scope.$parent.formdetailcopy
											$scope.page.totalElements = $scope.quotationdetailsCopy.length;
											$scope.idquotation = newValue; 
											$scope.quotationdetails = [];
											for(var i = 0; i < $scope.page.totalElements; i++){
												var quotationdetail = {idquotation: newValue, idmaterial: $scope.quotationdetailsCopy[i].idmaterial, note: $scope.quotationdetailsCopy[i].note }
												if (typeof $scope.quotationdetailsCopy[i].price !== "undefined") {
													quotationdetail.price = $scope.quotationdetailsCopy[i].price;
												}
												else {quotationdetail.price = null;}
												if (typeof $scope.quotationdetailsCopy[i].idunit !== "undefined") {
													quotationdetail.idunit = $scope.quotationdetailsCopy[i].idunit;
												}
												else {quotationdetail.idunit = null;}
												
												if (typeof $scope.quotationdetailsCopy[i].materialcode !== "undefined") {
													quotationdetail.materialcode = $scope.quotationdetailsCopy[i].materialcode;
												}
												else {quotationdetail.materialcode = null;}
												$scope.quotationdetails.push(quotationdetail);
											}
											quotationdetailService.createWithSelectDetail(JSON.stringify($scope.quotationdetails)).then(function(response){
												if($scope.idquotation > -1 && $rootScope.idquotationdetailController == $scope.$id) {
													$scope.listWithCriteriasByIdquotationAndPage($scope.idquotation, 1);
													$scope.$parent.formdetailcopy = []
												}
											},function(response){
												
											})
										} else {
											$scope.quotationdetails = [];
											$scope.page.totalElements = 0;
											$scope.idquotation = $scope.$parent.quotation.id;
											if($scope.idquotation > -1 && $rootScope.idquotationdetailController == $scope.$id) {
												$scope.listWithCriteriasByIdquotationAndPage($scope.idquotation, 1);
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
		
		// Init for form.
		$scope.initForm = function(id) {
			// set idquotation.
			$scope.idquotation = $scope.$parent ? $scope.$parent.quotation.id : -1;
			$scope.parseIdUnit();
			
			// promise
			
			$scope.createNew();
			$scope.quotationdetail.id = id;
			if($scope.quotationdetail.id > -1) {
				$scope.getById($scope.quotationdetail.id);
			}
			//$scope.parseIdUnit();
			$scope.frmDirty = false;
		}
		
		
		
		// Call service: list all for select.
		$scope.listAllForSelect = function() {
			var listAllSelectDeferred = $q.defer();
			// Init data for select.
			$scope.materials = [];
			$scope.units = [];
			// Call service.
			var listMaterial = materialService.listAllForSelect();
			var listUnit = catalogService.getListScope('unit');
			// Response.
			$q.all([listMaterial,listUnit]).then(
				// Successes.
				function(responses) {
					$scope.ctlentityfieldMaterial.items = responses[0].data;					
					$scope.units = responses[1].data;
										
		
					
					
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
		
		
		$scope.itemSelected = function(){
			for (var i = 0; i < $scope.ctlentityfieldMaterial.items.length; i++) {
				if($scope.quotationdetail.idmaterial==$scope.ctlentityfieldMaterial.items[i].value){
					$scope.ctlentityfieldMaterial.selectedItem = {};
					$scope.ctlentityfieldMaterial.selectedItem = $scope.ctlentityfieldMaterial.items[i];
				}
			}
		}
		
		//Show Dialog with controller.
		$scope.showDialogWithController = function() {
			var htmlUrlTemplate = clientwh.contextPath + '/view/selectfordetail_form.html';
			var params = {name:'quotation',id:$scope.idquotation}
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
			$scope.quotationdetailSelected = [];
			for (var i = 0; i < materials.length; i++) {
				var quotation = {};
				quotation.id = -1;
				quotation.idquotation = $scope.idquotation;
				quotation.idmaterial = materials[i].idmaterial;
				quotation.note = materials[i].note;
				if (typeof materials[i].price !== "undefined") {
					quotation.price = materials[i].price;
				}
				else {quotation.price = null;}
				if (typeof materials[i].idunit !== "undefined") {
					quotation.idunit = materials[i].idunit;
				}
				else {quotation.idunit = null;}
				if (typeof materials[i].materialcode !== "undefined") {
					quotation.materialcode = materials[i].materialcode;
				}
				else {quotation.materialcode = null;}
				
				$scope.quotationdetailSelected.push(quotation);
			}
			// Create With Select Detail
			quotationdetailService.createWithSelectDetail(JSON.stringify($scope.quotationdetailSelected))
			.then(function(response){
				$scope.listWithCriteriasByIdquotationAndPage($scope.idquotation, 1);
			},function(response){
				
			});
		}
		
		$scope.inputPrice = {};
		$scope.indexItemPrice = 0;
		// Show input price view.
	    $scope.showInputPriceFormDialog = function () {
	    	$scope.indexItemPrice = 0;
	    	$scope.listMaterialsByIdPrice($scope.idquotation);
	        var htmlUrlTemplate = clientwh.contextPath + '/view/inputprice_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }
	    
	    // List materials by id for input price
	    $scope.listMaterialsByIdPrice = function(idquotation){
	    	quotationdetailService.getAllById(idquotation).then(function(response){
	    		$scope.materialdetaillist = response.data;
	    		$scope.indexItemPrice = 0;
	    		$scope.idselectedPrice = [$scope.materialdetaillist[$scope.indexItemPrice].id];
	    		$scope.inputPrice.materialname = $scope.materialdetaillist[$scope.indexItemPrice].materialname
	    		$scope.inputPrice.price = $scope.materialdetaillist[$scope.indexItemPrice].price
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
	   
	    //Check null item when input price
	    $scope.checkNulItemPrice = function(arrInputPrice){
	    	for (var i = 0; i < arrInputPrice.length; i++) {
				if (arrInputPrice[i].price===null) {
					return i
				} 
			}
	    	return -1;
	    }
	    
	    $scope.quotationUpdatePrice = [];
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
	    		$scope.quotationUpdatePrice = $scope.materialdetaillist
		    	delete $scope.quotationUpdatePrice.materialname
		    	// Create With Select Detail
		    	quotationdetailService.quotationUpdatePrice(JSON.stringify($scope.quotationUpdatePrice))
				.then(function(response){
					if(response.status === httpStatus.code.OK) {
						$scope.listMaterialsByIdPrice($scope.idquotation);
						$scope.listWithCriteriasByIdquotationAndPage($scope.idquotation, 1);
						$scope.showMessageOnToast($translate.instant('clientwh_home_update_price_successfully'))
					}	
				},function(response){
					$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
				});
				$scope.quotationUpdatePrice = [];
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
	    
		// //////////////////////////////////////
		// Auto complete: loadItemsData
		// //////////////////////////////////////
    	$scope.loadItemsData = function(dataResponse){
	    	var items = [];
	    	for (var i = 0; i < dataResponse.length; i++) {
	    		var item = {value: '', display: '',code:'',unit:''};
	    		item.value = dataResponse[i].id;
	    		item.display = dataResponse[i].name;
	    		item.code = dataResponse[i].code;
	    		item.unit = dataResponse[i].idunit;
				items.push(item);
			}
	    	return items;
	    }
    	$scope.unitName;
    	$scope.parseIdUnit = function(){
    		$scope.unitName = null;
    		for (var i = 0; i < $scope.units.length; i++) {
    				if ($scope.quotationdetail.idunit==$scope.units[i].id) {
    					$scope.unitName = $scope.units[i].name;
    					break;
    				}
//    				else {
//    					$scope.unitName = null;
//					}
				
			}
    	}
    	
    	 // //////////////////////////////////////
		// Auto complete: idMaterial
		// //////////////////////////////////////
		$scope.ctlentityfieldMaterial = {};		
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
	    	if(frmQuotationdetail.idmaterial === undefined) {
	    		return;
	    	}
	    	$scope.quotationdetail.idmaterial = undefined;
	    	frmQuotationdetail.idmaterial.$invalid = true;
	    	if(item) {
	    		$scope.quotationdetail.idmaterial = item.value;
	    		$scope.quotationdetail.idunit = item.idunit;
	    		$scope.quotationdetail.materialcode = item.code;
	    		$scope.unitName = item.unitName;
	    		frmQuotationdetail.idmaterial.$invalid = false;
	    	}
	    	else{
	    		$scope.quotationdetail.idunit = null;
	    		$scope.unitName = null;
	    	}
	    }
	    
	    $scope.unitName;
	    $scope.displayNameUnit = function(){
	    	for (var i = 0; i < $scope.listUnit.length; i++) {
				if ($scope.quotationdetail.idunit==$scope.listUnit[i].id) {
					 $scope.unitName = $scope.listUnit[i].name;
				}
				else
					{
					 $scope.unitName = null;
					}
			}
	    }
	    
	    // //////////////////////////////////////
		// Auto complete: idUnit
		// //////////////////////////////////////
		$scope.ctlentityfieldUnit = {};		
		$scope.ctlentityfieldUnit.isCallServer = false;
	    $scope.ctlentityfieldUnit.isDisabled    = false;	    
	    
	    // New.
	    $scope.ctlentityfieldUnit.newState = function(item) {
	      alert("Sorry! You'll need to create a Constitution for " + item + " first!");
	    }
	    // Search in array.
	    $scope.ctlentityfieldUnit.querySearch = function(query) {
	      var results = query ? $scope.ctlentityfieldUnit.items.filter( $scope.ctlentityfieldUnit.createFilterFor(query) ) : $scope.ctlentityfieldUnit.items,
	          deferred;
	      
	      if ($scope.ctlentityfieldUnit.isCallServer) {
	        deferred = $q.defer();
	        $timeout(function () { deferred.resolve( results ); }, Math.random() * 1000, false);
	        return deferred.promise;
	      } else {
	        return results;
	      }
	    }
	    // Filter.
	    $scope.ctlentityfieldUnit.createFilterFor = function(query) {
			 var lowercaseQuery = angular.lowercase(query);
			
			 return function filterFn(item) {
				 return (angular.lowercase(item.display).indexOf(lowercaseQuery) >= 0);
			 };
	    }
	    // Text change.
	    $scope.ctlentityfieldUnit.searchTextChange = function(text) {
	    	$log.info('Text changed to ' + text);
	    }
	    // Item change.
	    $scope.ctlentityfieldUnit.selectedItemChange = function(item) {
	    	if(frmQuotationdetail.idunit === undefined) {
	    		return;
	    	}
	    	$scope.quotationdetail.idunit = undefined;
	    	frmQuotationdetail.idunit.$invalid = true;
	    	if(item) {
	    		$scope.quotationdetail.idunit = item.value;
	    		frmQuotationdetail.idunit.$invalid = false;
	    	}
	    }
	    
	    
	    
		
		
		// Show a create screen.
		$scope.showCreate = function() {
			$scope.initForm(-1);
			$scope.showDialog();
			$scope.ctlentityfieldMaterial.selectedItem=null;
			$scope.unitName = null;
			
		}
		
		//Show form detail for view
		$scope.showDetail = function(id){
			$scope.getByIdForView(id);
			$scope.showDialogDetail();
		}
		
		//Show dialog view
		$scope.showDialogDetail = function () {
	        var htmlUrlTemplate = clientwh.contextPath + '/view/quotationdetail_formView.html';
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
			$scope.ctlentityfieldMaterial.selectedItem=null;
		}

	    // Show edit view.
	    $scope.showDialog = function () {
	        var htmlUrlTemplate = clientwh.contextPath + '/view/quotationdetail_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }

	    // Create new.
		$scope.createNew = function() {
			$scope.quotationdetail = { id: -1, idquotation: $scope.idquotation };
			$scope.ctlentityfieldMaterial.selectedItem = null;
			
		}
		
		// Reset validate.
		$scope.resetValidate = function() {
			// idquotation.
		    $scope.frmQuotationdetail.idquotation.$setPristine();
			$scope.frmQuotationdetail.idquotation.$setUntouched();
			// idmaterial.
		    $scope.frmQuotationdetail.idmaterial.$setPristine();
			$scope.frmQuotationdetail.idmaterial.$setUntouched();
			// idunit.
		    $scope.frmQuotationdetail.idunit.$setPristine();
			$scope.frmQuotationdetail.idunit.$setUntouched();
			// materialcode.
		    $scope.frmQuotationdetail.materialcode.$setPristine();
			$scope.frmQuotationdetail.materialcode.$setUntouched();
			// price.
		    $scope.frmQuotationdetail.price.$setPristine();
			$scope.frmQuotationdetail.price.$setUntouched();
			// note.
		    $scope.frmQuotationdetail.note.$setPristine();
			$scope.frmQuotationdetail.note.$setUntouched();

		    // form.
			$scope.frmQuotationdetail.$setPristine();
			$scope.frmQuotationdetail.$setUntouched();
			// frmDirty.
			$scope.frmDirty = false;
		}
		
		// Create on form.
		$scope.createOnForm = function() {
			$scope.createNew();
			$scope.resetValidate();
		}
		
		
		// Save.
		$scope.save = function() {
			if($scope.frmQuotationdetail.$invalid) {
				$scope.frmQuotationdetail.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			
			$scope.showMessageOnToast($translate.instant('clientwh_home_saving'));
			var result;
			if($scope.quotationdetail.id > -1) {
				result = quotationdetailService.updateWithLock($scope.quotationdetail.id, $scope.quotationdetail);
			} else {
				result = quotationdetailService.create($scope.quotationdetail);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.quotationdetail.id > -1) {
						$scope.quotationdetail.version = response.data;
					} else {
						$scope.quotationdetail.id = response.data;
						$scope.quotationdetail.version = 1;
					}
					$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
					$scope.listWithCriteriasByIdquotationAndPage($scope.idquotation, 1);
				} else {
					if (response.data.code == clientwh.serverCode.EXITMATERIAL) {
						$scope.frmQuotationdetail.idmaterial.$invalid = true;
						$scope.showMessageOnToast($translate.instant('clientwh_quotationdetail_exist'));
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
		
		// Delete.
		$scope.delete = function(id, version) {
			
				quotationdetailService.updateForDeleteWithLock(id, version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToastList($translate.instant('clientwh_home_deleted'));
						$scope.listWithCriteriasByIdquotationAndPage($scope.idquotation, 1);
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
			
				quotationdetailService.updateForDeleteWithLock($scope.quotationdetail.id, $scope.quotationdetail.version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToast($translate.instant('clientwh_home_deleted'));
						$scope.createNew();
						$scope.resetValidate();
						$scope.listWithCriteriasByIdquotationAndPage($scope.idquotation, 1);
					} else {
						$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
					}
				},
				// error.
				function(response) {
					$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
				});
			
		}
		
		
		// Get by Id for view.
		$scope.getByIdForView = function(id) {
			quotationdetailService.getByIdForView(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.quotationDetailView = data;
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
			quotationdetailService.getById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.quotationdetail = data;
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
		
		// List for page and filter.
		$scope.listWithCriteriasByPage = function(pageNo) {
			$scope.page.currentPage = pageNo;
			quotationdetailService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.quotationdetails = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.quotationdetails = result;
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
		
		// List for page and filter.
		$scope.listWithCriteriasByIdquotationAndPage = function(idquotation, pageNo) {
			$scope.page.currentPage = pageNo;
			quotationdetailService.listWithCriteriasByIdquotationAndPage(idquotation, $scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.quotationdetails = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.quotationdetails = result;
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
			$scope.listWithCriteriasByIdquotationAndPage($scope.idquotation, 1)
		}

		/* Extend functions */
		
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
		    	result.push('sort=' + $scope.sortKey + ',' + $scope.reverse);
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
				// idquotation.
		    	// result.push({ key: 'idquotation', operation: 'like', value:
				// $scope.search.content, logic: 'or' });
				// idmaterial.
		    	// result.push({ key: 'idmaterial', operation: 'like', value:
				// $scope.search.content, logic: 'or' });
				// idunit.
		    	// result.push({ key: 'idunit', operation: 'like', value:
				// $scope.search.content, logic: 'or' });
				// materialcode.
		    	 result.push({ key: 'materialcode', operation: 'like', value:
				 $scope.search.content, logic: 'or' });
				// price.
		    	// result.push({ key: 'price', operation: 'like', value:
				// $scope.search.content, logic: 'or' });
				// note.
		    	// result.push({ key: 'note', operation: 'like', value:
				// $scope.search.content, logic: 'or' });
		    }
		    // return.
		    return result;
		}
			
		// Show message.
		$scope.showMessage = function(message, cssName, autoHide) {
			$scope.quotationdetailArlertMessage = message;
			$('#quotationdetailArlertMessage').removeClass('alert-danger');
			$('#quotationdetailArlertMessage').removeClass('alert-success');
			$('#quotationdetailArlertMessage').addClass(cssName);
			$('#quotationdetailArlertMessage').slideDown(500, function() {
				if(autoHide) {
					$window.setTimeout(function() {
						$('#quotationdetailArlertMessage').slideUp(500, function() {
							$('#quotationdetailArlertMessage').removeClass(cssName);
		            	});
					}, 1000);
				}
			});
		}
	
	}]);

});
