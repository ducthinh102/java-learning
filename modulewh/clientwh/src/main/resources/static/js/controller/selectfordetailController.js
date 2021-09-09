
/**
 * Controller for Selectfordetail
 **/

define(['require', 'angular'], function (require, angular) {
    app.aController(clientwh.prefix + 'selectfordetailController', ['$scope', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader','$q', 'params', clientwh.prefix + 'userService', clientwh.prefix + 'quotationService', clientwh.prefix + 'quotationdetailService', clientwh.prefix + 'materialimportdetailService', clientwh.prefix + 'materialimportService', clientwh.prefix + 'materialexportdetailService', clientwh.prefix + 'materialexportService', clientwh.prefix + 'purchasedetailService', clientwh.prefix + 'purchaseService', clientwh.prefix + 'requestdetailService', clientwh.prefix + 'requestService',clientwh.prefix + 'materialformService',clientwh.prefix + 'materialformdetailService',clientwh.prefix + 'materialbaselineService',clientwh.prefix + 'materialbaselinedetailService',clientwh.prefix + 'materialstoreService',
        function ($scope, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, $q, params ,userService, quotationService, quotationdetailService, materialimportdetailService, materialimportService, materialexportdetailService, materialexportService, purchasedetailService, purchaseService, requestdetailService, requestService, materialformService, materialformdetailService, materialbaselineService, materialbaselinedetailService, materialstoreService) {
            if (typeof (clientwh.translate.selectfordetail) === 'undefined' || clientwh.translate.selectfordetail.indexOf($translate.use()) < 0) {
                console.log(clientwh.translate.selectfordetail);
                if (typeof (clientwh.translate.selectfordetail) === 'undefined') {
                    clientwh.translate.selectfordetail = '';
                }
                clientwh.translate.selectfordetail += $translate.use() + ';';
                $translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/selectfordetail');
                $translate.refresh();
            }

            var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
                console.log('clientwh_selectfordetail_title');
                $scope.title = $translate.instant('clientwh_selectfordetail_title');
            });
            // Unregister
            $scope.$on('$destroy', function () {
                unRegister();
            });
            $translate.onReady().then(function () {
                console.log('selectfordetail onReady');
                $scope.title = $translate.instant('clientwh_selectfordetail_title');
                $translate.refresh();
            });
            
            $scope.targetList = [{value:0,display:'clientwh_left_supply_chain_quotation'},{value:1,display:'clientwh_left_material_import'},{value:2,display:'clientwh_left_material_export'},{value:3,display:'clientwh_left_supply_chain_material_Purchase'},{value:4,display:'clientwh_left_supply_chain_material_request'},{value:5,display:'clientwh_left_material_materialform_prematerial'},{value:6,display:'clientwh_left_material_materialform_techmaterial'},{value:7,display:'clientwh_left_supply_chain_material_baseline'}];
            $scope.changeTargetCombox = function(){
            	var numOfTarget = parseInt($scope.selectfordetail.targetCombox);
            	var nameTarget =$scope.returnNameOfTargetByNumber(numOfTarget);
            	$scope.changeTarget(nameTarget,numOfTarget);
            }
            
            $scope.returnNameOfTargetByNumber = function(number){
            	var s = ['quotation','materialimport','materialexport','purchase','request','materialformprematerial','materialformtechmaterial','materialbaseline'];
            	return s[number];
            }
            // Search.
            $scope.search = {};

            $scope.selectfordetail = {};
            $scope.show = function () {
                alert($scope.title);
            }
            $scope.goto = function (state) {
                $state.go(clientwh.prefix + state);
            }

            $scope.changeLanguage = function (language) {
                $translate.refresh();
                $translate.use(language);
                $translate.refresh();
                $translate.use(language);
                $translate.refresh();
            }
            
            // Promise list for select.
    		var listAllSelectPromise;
    		
            // Init for list.
            $scope.initList = function () {
                if(typeof(listAllSelectPromise) === 'undefined') {
    				var listAllSelectDefered = $q.defer();
    				listAllSelectPromise = listAllSelectDefered.promise;
    				listAllSelectDefered.resolve([]);
    			}
    			listAllSelectPromise.then(
    				// Success.
    				function(response) {
    					 $scope.getListMaterialByParam();
    				},
    				// Error.
    				function(response) {
    					
    				}
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
						$scope.selectfordetail.id = id;
						if($scope.selectfordetail.id > -1) {
							$scope.getById($scope.selectfordetail.id);
						}
						$scope.frmDirty = false;
						
					},
					// Error.
					function(response) {
						
					}
				);
			}
            
         //get list
			$scope.getidquotationdetail = function() {
				
			}
			
			$scope.quotation = [];
			$scope.purchase = [];
			$scope.request = [];
			$scope.materialimport = [];
			$scope.materialexport = [];
			$scope.materialformprematerial = [];
			$scope.materialformtechmaterial = [];
			$scope.materialbaseline = [];
			
         // Call service: list all for select.
    		$scope.listAllForSelect = function() {
    			var listAllSelectDeferred = $q.defer();
       			// Call service.
				var listQuotation = quotationService.listAllForSelect();
				var listMaterialimport = materialimportService.listAllForSelect();
				var listMaterialexport = materialexportService.listAllForSelect();
				var listPurchase = purchaseService.listAllForSelect();
				var listRequest = requestService.listAllForSelect();
				var listMaterialFormPrematerial = materialformService.listAllForSelectByScope('prematerial');
				var listMaterialFormTechmaterial = materialformService.listAllForSelectByScope('techmaterial');
				var listMaterialbaseline = materialbaselineService.listAllForSelect();
				// Response.
				$q.all([listQuotation, listMaterialimport, listMaterialexport, listPurchase, listRequest, listMaterialFormPrematerial, listMaterialFormTechmaterial, listMaterialbaseline]).then(
					// Successes.
					function(responses) {
						$scope.quotation = responses[0].data;
						$scope.materialimport = responses[1].data;
						$scope.materialexport = responses[2].data;
						$scope.purchase = responses[3].data;
						$scope.request = responses [4].data;
						$scope.materialformprematerial = responses [5].data;
						$scope.materialformtechmaterial = responses [6].data;
						$scope.materialbaseline = responses [7].data;
						// Resolve promise.
						listAllSelectDeferred.resolve(responses);
					},
					// Errors.
					function(responses) {
						$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
						// Reject promise.
						listAllSelectDeferred.reject(responses);
					}
					
				);
				return listAllSelectDeferred.promise;
    		}
    		
    		//Call and return a promise.
    		listAllSelectPromise = $scope.listAllForSelect();
    		
    		$scope.itemSelected = function(id){
				for (var i = 0; i < $scope.ctlentityfieldSelectcode.items.length; i++) {
					if(id==$scope.ctlentityfieldSelectcode.items[i].value){
						$scope.ctlentityfieldSelectcode.selectedItem = {};
						$scope.ctlentityfieldSelectcode.selectedItem = $scope.ctlentityfieldSelectcode.items[i];
					}
				}
			}
    		
    		
    		// //////////////////////////////////////
			// Auto complete: loadItemsData
			// //////////////////////////////////////
	    	$scope.loadItemsData = function(dataResponse){
		    	var items = [];
		    	for (var i = 0; i < dataResponse.length; i++) {
		    		var item = {value: '', display: '',code:''};
		    		item.value = dataResponse[i].id;
		    		item.display = dataResponse[i].name;
		    		item.code = dataResponse[i].code;
					items.push(item);
				}
		    	return items;
		    }
	    	
	    	 // //////////////////////////////////////
			// Auto complete: idQuotation
			// //////////////////////////////////////
			$scope.ctlentityfieldSelectcode = {};		
			$scope.ctlentityfieldSelectcode.isCallServer = false;
		    $scope.ctlentityfieldSelectcode.isDisabled    = false;	    
		    
		    // New.
		    $scope.ctlentityfieldSelectcode.newState = function(item) {
		      alert("Sorry! You'll need to create a Constitution for " + item + " first!");
		    }
		    // Search in array.
		    $scope.ctlentityfieldSelectcode.querySearch = function(query) {
		      var results = query ? $scope.ctlentityfieldSelectcode.items.filter( $scope.ctlentityfieldSelectcode.createFilterFor(query) ) : $scope.ctlentityfieldSelectcode.items,
		          deferred;
		      
		      if ($scope.ctlentityfieldSelectcode.isCallServer) {
		        deferred = $q.defer();
		        $timeout(function () { deferred.resolve( results ); }, Math.random() * 1000, false);
		        return deferred.promise;
		      } else {
		        return results;
		      }
		    }
		    
		    // Filter.
		    $scope.ctlentityfieldSelectcode.createFilterFor = function(query) {
				 var lowercaseQuery = angular.lowercase(query);
				
				 return function filterFn(item) {
					 return (angular.lowercase(item.display).indexOf(lowercaseQuery) >= 0);
				 };
		    }
		    // Text change.
		    $scope.ctlentityfieldSelectcode.searchTextChange = function(text) {
		    	$log.info('Text changed to ' + text);
		    }
		    // Item change.
		    $scope.ctlentityfieldSelectcode.selectedItemChange = function(item) {
		    	if($scope.frmSelectfordetail.sellectcode === undefined) {
		    		return;
		    	}
		    	$scope.frmSelectfordetail.sellectcode.$invalid = true;
		    	if(item) {
		    		$scope.frmSelectfordetail.sellectcode.$invalid = false;
		    		$scope.getByIdTarget($scope.numTarget,item.value)
		    	}
		    }
		    
    		
    		
    		
            // Show edit view.
            $scope.showDialog = function () {
                var htmlUrlTemplate = clientwh.contextPath + '/view/selectfordetail_form.html';
                clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function (evt) {
                    console.log('closed');
                }, function (evt) {
                    console.log('not closed');
                });
            }
            $scope.selectForData = function(){
            	$mdDialog.hide($scope.selected);
            }
            $scope.closeDialog = function(){
            	$mdDialog.hide();
            }
            // Get by Id.
            $scope.getById = function (id) {
                selectfordetailService.getById(id)
                    // success.
                    .then(function (response) {
                        if (response.status === httpStatus.code.OK) {
                            var data = angular.fromJson(response.data);
                            $scope.selectfordetail = data;
                        } else {
                            $scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
                        }
                    },
                    // error.
                    function (response) {
                        $scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
                    });
            }

            // Clear filter.
            $scope.clearFilter = function () {
                $scope.search = {};
            }

            /*Extend functions*/

            // Sort by.
            $scope.sortBy = function (keyName) {
                $scope.sortKey = keyName;
                $scope.reverse = !$scope.reverse;
            }

            // Get sort target.
            $scope.getSort = function () {
                var result = [];
                // name.
                if (typeof ($scope.sortKey) !== 'undefined' && $scope.sortKey !== '') {
                    result.push('sort=' + $scope.sortKey + ',' + $scope.reverse);
                }
                // return.
                return result;
            }

            // Get search target.
            $scope.getSearch = function () {
                var result = [];
                // target.
                if (typeof ($scope.search.target) !== 'undefined' && $scope.search.target !== '') {
                    result.push({ key: 'target', operation: 'like', value: $scope.search.target, logic: 'or' });
                }
                // rules.
                if (typeof ($scope.search.rules) !== 'undefined' && $scope.search.rules !== '') {
                    result.push({ key: 'rules', operation: 'like', value: $scope.search.rules, logic: 'or' });
                }
                // return.
                return result;
            }

            // Show message.
            $scope.showMessage = function (message, cssName, autoHide) {
                $scope.alertMessage = message;
                $('#alertMessage').addClass(cssName);
                $('#alertMessage').slideDown(500, function () {
                    if (autoHide) {
                        $window.setTimeout(function () {
                            $('#alertMessage').slideUp(500, function () {
                                $('#alertMessage').removeClass(cssName);
                            });
                        }, 1000);
                    }
                });
            }

            // Get all selectfordetail.
            $scope.getAllSelectfordetail = function () {
                selectfordetailService.getAllSelectfordetail()
                    // success.
                    .then(function (response) {
                        if (response.status === httpStatus.code.OK) {
                        	// Have data response
                        	if(response.data)
                        		{
                        		 var data = angular.fromJson(response.data);
                                 $scope.allSelectfordetail(4);
                                 $scope.listSelectfordetail = data;
                                 $scope.loadSelectfordetail($scope.selectfordetail.target);
                        		}
                        } else {
                            $scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
                        }
                    },
                    // error.
                    function (response) {
                        $scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
                    });
            }

           
            
            $scope.listDetail=[];
            // Return array of tab by tab number
            $scope.choseArrForSelect = function (numTab,id) {
                var s = [quotationdetailService.getAllById(id), materialimportdetailService.getAllById(id), materialexportdetailService.getAllById(id), purchasedetailService.getAllById(id), requestdetailService.getAllById(id), materialformdetailService.getAllById(id), materialformdetailService.getAllById(id), materialbaselinedetailService.getAllById(id)];
                return s[numTab];
            }
            
            $scope.displayMaterialByQuantity = function(lstMaterialById, lstMaterialStore){
            	for (var i = 0; i < lstMaterialById.length; i++) {
            		if (!lstMaterialById[i].display) {
            			for (var j = 0; j < lstMaterialStore.length; j++) {
							if (parseInt(lstMaterialById[i].idmaterial)==parseInt(lstMaterialStore[j].idmaterial)) {
								if (lstMaterialStore[j].quantity-lstMaterialById[i].quantity < 0) {
									lstMaterialById[i].display = true;
								}
							}
						}
					}	
				}
            }
            $scope.listMaterialStore = []
            // Get quotationdetail by id (quotationdetail la bien chung, chua doi ten)l
            $scope.getByIdTarget = function (numTarget,id) {
            	 $scope.choseArrForSelect(numTarget,id)
                    // success.
                    .then(function (response) {
                        if (response.status === httpStatus.code.OK) {
                            $scope.listMaterialById = response.data;
                            if (params.name=='materialexport') {
                            	var priceMaterials = [];
                	    		for (var i = 0; i < $scope.listMaterialById.length; i++) {
                	    			priceMaterials.push({"idmaterial":$scope.listMaterialById[i].idmaterial})
                				}
                                materialstoreService.checkQuantityMaterial(params.idstore, priceMaterials)
                                .then(function(response){
                                	$scope.listMaterialStore = response.data
                                	$scope.disableMaterialExits($scope.listMaterialById, $scope.materialsByParam);
                                	$scope.displayMaterialByQuantity($scope.listMaterialById,$scope.listMaterialStore)
                                },function(response){
                                	
                                })
							}
                            else{
                            	$scope.disableMaterialExits($scope.listMaterialById, $scope.materialsByParam);
							}
                        } else {
                            $scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
                        }
                    },
                    // error.
                    function (response) {
                        $scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
                    });
            }
            
            $scope.disableMaterialExits = function(listById, listByIdParam){
            	delete listById.display;
            	for (var i = 0; i < listById.length; i++) {
            		listById[i].display = false;
            		for (var j = 0; j < listByIdParam.length; j++) {
						if (parseInt(listById[i].idmaterial)==parseInt(listByIdParam[j].idmaterial)) {
							listById[i].display = true;
						}
					}
				}
            }
            
            $scope.getListMaterialByParam = function(){
            	var numTarget = $scope.returnNumArrByTarget(params.name);
            	$scope.getByIdParam(numTarget,params.id);
            }
            
            $scope.getByIdParam = function (numTarget,id) {
           	 $scope.choseArrForSelect(numTarget,id)
                   // success.
                   .then(function (response) {
                	   $scope.materialsByParam = []
                       if (response.status === httpStatus.code.OK) {
                    	   $scope.materialsByParam = response.data;
                       } else {
                           $scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
                       }
                   },
                   // error.
                   function (response) {
                       $scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
                   });
           }
            
            $scope.returnNumArrByTarget = function(target){
            	var s = ['quotation','materialimport','materialexport','purchase','request','materialformprematerial','materialformtechmaterial','materialbaseline'];
            	for (var i = 0; i < s.length; i++) {
					if (target==s[i]) {
						return i;
					}
				}
            }

            
 

            // Check duplicate
            $scope.checkDupes = function (item, arr) {
                for (var i = 0; i < arr.length; i++) {
                    if (arr[i].id == parseInt(item)) {
                        return true;
                    }
                }
                return false;
            }
            
            // Move All
            $scope.moveAll = function (from,to) {
            	var indexArr = [];
                for (var i = 0; i < from.length; i++) {
                	if (!from[i].display) {
                		to.push(from[i]);
                		indexArr.push(i);
                	}
				}
                for (var i = 0; i < indexArr.length; i++) {
                	from.splice(indexArr[i], indexArr.length );
				}
            }
            $scope.selected = [];
            // move item to tab list
            $scope.moveItem = function (item, from, to) {
                if (item != null) {
                    for (var i = 0; i < item.length; i++) {
                        for (var j = 0; j < from.length; j++) {
                        	 if (parseInt(item[i]) == from[j].id) {
                        		 if (!$scope.checkDupes(item[i],to)) {
                             		to.push(from[j]);
                             		from.splice(j, 1 );
     							}
                             	else {
                             		console.log("Exits");
                             	}
                        	 }
                        }
                    }
                }
            };

            // Return array of tab by tab number
            $scope.choseArr = function (numTab) {
                var s = [$scope.quotation, $scope.materialimport, $scope.materialexport, $scope.purchase, $scope.request, $scope.materialformprematerial, $scope.materialformtechmaterial, $scope.materialbaseline];
                return s[numTab]; 
            }
            
            $scope.filterList = function(target,numArr){
            	$scope.ctlentityfieldSelectcode.items = $scope.choseArr(numArr);
            	if (target==params.name) {
            		for (var i = 0; i < $scope.ctlentityfieldSelectcode.items.length; i++) {
						if ($scope.ctlentityfieldSelectcode.items[i].value==params.id) {
							$scope.ctlentityfieldSelectcode.items.splice(i,1);
							break;
						}
					}
				}
            }
            
            $scope.numTarget;
            //Change target
            $scope.changeTarget = function (target,numArr) {
            	$scope.numTarget = numArr;
                $scope.selectfordetail.target = target;
                $scope.filterList(target,numArr);
                $scope.ctlentityfieldSelectcode.selectedItem=null;
                $scope.frmSelectfordetail.sellectcode.$setPristine();
    			$scope.frmSelectfordetail.sellectcode.$setUntouched();
                $scope.selected = [];
                $scope.listMaterialById = null;
            }

            
        }]);

});
