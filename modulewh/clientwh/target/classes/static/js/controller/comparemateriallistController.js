
/**
 * Controller for Comparemateriallist
 **/

define(['require', 'angular'], function (require, angular) {
    app.aController(clientwh.prefix + 'comparemateriallistController', ['$scope', '$state', '$rootScope', '$mdDialog', '$mdToast', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader','$q', clientwh.prefix + 'userService', clientwh.prefix + 'quotationService', clientwh.prefix + 'quotationdetailService', clientwh.prefix + 'materialimportdetailService', clientwh.prefix + 'materialimportService', clientwh.prefix + 'materialexportdetailService', clientwh.prefix + 'materialexportService', clientwh.prefix + 'purchasedetailService', clientwh.prefix + 'purchaseService', clientwh.prefix + 'requestdetailService', clientwh.prefix + 'requestService',clientwh.prefix + 'materialformService',clientwh.prefix + 'materialformdetailService',clientwh.prefix + 'materialbaselineService',clientwh.prefix + 'materialbaselinedetailService',
        function ($scope, $state, $rootScope, $mdDialog, $mdToast, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, $q, userService, quotationService, quotationdetailService, materialimportdetailService, materialimportService, materialexportdetailService, materialexportService, purchasedetailService, purchaseService, requestdetailService, requestService, materialformService, materialformdetailService, materialbaselineService, materialbaselinedetailService) {
            if (typeof (clientwh.translate.comparemateriallist) === 'undefined' || clientwh.translate.comparemateriallist.indexOf($translate.use()) < 0) {
                console.log(clientwh.translate.comparemateriallist);
                if (typeof (clientwh.translate.comparemateriallist) === 'undefined') {
                    clientwh.translate.comparemateriallist = '';
                }
                clientwh.translate.comparemateriallist += $translate.use() + ';';
                $translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/comparemateriallist');
                $translate.refresh();
            }

            var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
                $scope.title = $translate.instant('clientwh_comparemateriallist_title');
                $scope.options.filterPlaceHolder = $translate.instant('clientmain_home_search');
                $scope.options.title = $translate.instant('clientwh_comparemateriallist_title');
                $scope.options.helpMessage = $translate.instant('clientwh_comparemateriallist_helpMessage');
                $scope.options.labelAll = $translate.instant('clientwh_comparemateriallist_labelAll');
                $scope.options.labelSelected = $translate.instant('clientwh_comparemateriallist_labelSelected');
                $scope.options.labelSelectAll = $translate.instant('clientwh_comparemateriallist_labelSelectAll');
                $scope.options.labelDeselectAll = $translate.instant('clientwh_comparemateriallist_labelDeselectAll');
                
                let sourceCombox = clientwh.GetByProperty($scope.sourceList, 'value', $scope.comparemateriallist.sourceCombox);
                if(sourceCombox){
    				$scope.options.labelAll = sourceCombox.display;
                }
                let targetCombox = clientwh.GetByProperty($scope.targetList, 'value', $scope.comparemateriallist.targetCombox);
                if(targetCombox){
    				$scope.options.labelSelected = targetCombox.display;
                }
            });
            // Unregister
            $scope.$on('$destroy', function () {
                unRegister();
            });
            $translate.onReady().then(function () {
                $translate.refresh();
            });

            var self = this;
			// dualmultiselect.
			$scope.options = {
				items: [],
				selectedItems: [],
				displayProperty: 'materialname',
				valueProperty: 'idmaterial'
			};
	        $scope.options.transferList = function (items, selectedItems, index, isSelected) {
	        	// mark item source.
	        	if(typeof(items[index].isSource) === 'undefined'){
	        		items[index].isSource = isSelected;
	        	}
	            if (index >= 0) {
	                selectedItems.push(items[index]);
	                items.splice(index, 1);
	            } else {
	                for (var i = 0; i < items.length; i++) {
	                    selectedItems.push(items[i]);
	                }
	                items.length = 0;
	            }
	        };
            
			// service: listAllForSelect
			$scope.formServices = {quotation: quotationService, materialimport: materialimportService, materialexport: materialexportService, purchase: purchaseService, request: requestService, materialformprematerial: materialformService, materialformtechmaterial: materialformService, materialbaseline: materialbaselineService };

			// service: getAllById
			$scope.formdetailServices = {quotation: quotationdetailService, materialimport: materialimportdetailService, materialexport: materialexportdetailService, purchase: purchasedetailService, request: requestdetailService, materialformprematerial: materialformdetailService, materialformtechmaterial: materialformdetailService, materialbaseline: materialbaselinedetailService };

            $scope.comparemateriallist = {};
            $scope.resultList = [];
			$scope.isCompare = false;
			$scope.isSave = false;
			
			// source.
			$scope.sourceList = [{value:'quotation',display:'Quotation'},{value:'materialimport',display:'Material import'},{value:'materialexport',display:'Material export'},{value:'purchase',display:'Purchase'},{value:'request',display:'Request'},{value:'materialformprematerial',display:'Material form prematerial'},{value:'materialformtechmaterial',display:'Material form techmaterial'},{value:'materialbaseline',display:'Material baseline'}];
			$scope.changeSourceCombox = function() {
                let sourceCombox = clientwh.GetByProperty($scope.sourceList, 'value', $scope.comparemateriallist.sourceCombox);
                if(sourceCombox){
    				$scope.options.labelAll = sourceCombox.display;
                }
				$scope.resultList = [];
            	$scope.sourceSelectcode.items = [];
            	$scope.sourceItems = [];
            	$scope.options.items = [];
				if($scope.comparemateriallist.sourceCombox) {
					var result;
					if($scope.comparemateriallist.sourceCombox == 'materialformprematerial'){
						result = $scope.formServices[$scope.comparemateriallist.sourceCombox].listAllForSelectByScope('prematerial');
					} else if ($scope.comparemateriallist.sourceCombox == 'materialformtechmaterial') {
						result = $scope.formServices[$scope.comparemateriallist.sourceCombox].listAllForSelectByScope('techmaterial');
					} else {
						result = $scope.formServices[$scope.comparemateriallist.sourceCombox].listAllForSelect();
					}
					result.then(
						// success.
						function(response) {
							$scope.sourceSelectcode.items = response.data;
							if(self.locals.params){
								$scope.sourceSelectcode.selectedItem = clientmain.GetByProperty($scope.sourceSelectcode.items, 'value', self.locals.params.id);
							}
						},
						// error.
						function(response) {
						}
					);
				}
			}
			
			// target.
			$scope.targetList = [{value:'quotation',display:'Quotation'},{value:'materialimport',display:'Material import'},{value:'materialexport',display:'Material export'},{value:'purchase',display:'Purchase'},{value:'request',display:'Request'},{value:'materialformprematerial',display:'Material form prematerial'},{value:'materialformtechmaterial',display:'Material form techmaterial'},{value:'materialbaseline',display:'Material baseline'}];
			$scope.changeTargetCombox = function() {
                let targetCombox = clientwh.GetByProperty($scope.targetList, 'value', $scope.comparemateriallist.targetCombox);
                if(targetCombox){
    				$scope.options.labelSelected = targetCombox.display;
                }
				$scope.resultList = [];
            	$scope.targetSelectcode.items = [];
            	$scope.targetItems = [];
            	$scope.options.selectedItems = [];
				if($scope.comparemateriallist.targetCombox) {
					var result;
					if($scope.comparemateriallist.targetCombox == 'materialformprematerial'){
						result = $scope.formServices[$scope.comparemateriallist.targetCombox].listAllForSelectByScope('prematerial');
					} else if ($scope.comparemateriallist.targetCombox == 'materialformtechmaterial') {
						result = $scope.formServices[$scope.comparemateriallist.targetCombox].listAllForSelectByScope('techmaterial');
					} else {
						result = $scope.formServices[$scope.comparemateriallist.targetCombox].listAllForSelect();
					}
					result.then(
						// success.
						function(response) {
							$scope.targetSelectcode.items = response.data;
						},
						// error.
						function(response) {
						}
					);
				}
			}
			
	    	// //////////////////////////////////////
			// Auto complete: source
			// ////////////////////////////////////// 
		    $scope.sourceItems = [];
			$scope.sourceSelectcode = {};
			$scope.sourceSelectcode.items = [];
			$scope.sourceSelectcode.isCallServer = false;
		    $scope.sourceSelectcode.isDisabled    = false;   
		    // Search in array.
		    $scope.sourceSelectcode.querySearch = function(query) {
		      var results = query ? $scope.sourceSelectcode.items.filter( $scope.sourceSelectcode.createFilterFor(query) ) : $scope.sourceSelectcode.items;
		      return results;
		    }
		    // Filter.
		    $scope.sourceSelectcode.createFilterFor = function(query) {
				 var lowercaseQuery = angular.lowercase(query);
				
				 return function filterFn(item) {
					 return (angular.lowercase(item.display).indexOf(lowercaseQuery) >= 0);
				 };
		    }
		    // Text change.
		    $scope.sourceSelectcode.searchTextChange = function(text) {
		    	$log.info('Text changed to ' + text);
		    }
		    // Item change.
		    $scope.sourceSelectcode.selectedItemChange = function(item) {
            	$scope.sourceItems = [];
            	$scope.options.items = [];
		    	if($scope.frmComparemateriallist.sellectcodesource === undefined) {
		    		return;
		    	}
		    	$scope.frmComparemateriallist.sellectcodesource.$invalid = true;
		    	if(item) {
		    		$scope.frmComparemateriallist.sellectcodesource.$invalid = false;
					
					$scope.options.items = [];
					var result = $scope.formdetailServices[$scope.comparemateriallist.sourceCombox].getAllById(item.value);
					result.then(
						// success.
						function(response) {
							$scope.sourceItems = response.data;
							$scope.options.items = response.data;
							//$scope.compareList();
							$scope.isCompare = true;
							$scope.isSave = false;
						},
						// error.
						function(response) {
						}
					);
		    	}
		    }
	    	
	    	// //////////////////////////////////////
			// Auto complete: target
			// ////////////////////////////////////// 
		    $scope.targetItems = [];
			$scope.targetSelectcode = {};
			$scope.targetSelectcode.items = [];
			$scope.targetSelectcode.isCallServer = false;
		    $scope.targetSelectcode.isDisabled = false;
		    // Search in array.
		    $scope.targetSelectcode.querySearch = function(query) {
		      var results = query ? $scope.targetSelectcode.items.filter( $scope.targetSelectcode.createFilterFor(query) ) : $scope.targetSelectcode.items;
		      return results;
		    }
		    // Filter.
		    $scope.targetSelectcode.createFilterFor = function(query) {
				 var lowercaseQuery = angular.lowercase(query);
				
				 return function filterFn(item) {
					 return (angular.lowercase(item.display).indexOf(lowercaseQuery) >= 0);
				 };
		    }
		    // Text change.
		    $scope.targetSelectcode.searchTextChange = function(text) {
		    	$log.info('Text changed to ' + text);
		    }
		    // Item change.
		    $scope.targetSelectcode.selectedItemChange = function(item) {
            	$scope.targetItems = [];
            	$scope.options.selectedItems = [];
		    	if($scope.frmComparemateriallist.sellectcodetarget === undefined) {
		    		return;
		    	}
		    	$scope.frmComparemateriallist.sellectcodetarget.$invalid = true;
		    	if(item) {
		    		$scope.frmComparemateriallist.sellectcodetarget.$invalid = false;
					var result = $scope.formdetailServices[$scope.comparemateriallist.targetCombox].getAllById(item.value);
					result.then(
						// success.
						function(response) {
							$scope.targetItems = response.data;
							$scope.options.selectedItems = response.data;
							//$scope.compareList();
							$scope.isCompare = true;
							$scope.isSave = false;
						},
						// error.
						function(response) {
						}
					);
		    	}
		    }

            
            // compare list.
            $scope.compareList = function() {
				$scope.isCompare = false;
            	if($scope.sourceItems.length === 0 || $scope.targetItems.length === 0){
            		return;
            	}
            	$scope.resultList = [];
            	$scope.options.items = [];
            	$scope.options.selectedItems = angular.copy($scope.targetItems);
            	// compare.
            	$scope.sourceItems.forEach(function(item){
            		var index = clientmain.IndexOfByProperty($scope.options.selectedItems, 'idmaterial', item.idmaterial);
            		if(index > -1) {
            			clientwh.transferList($scope.options.selectedItems, $scope.resultList, index)
            		} else {
            			$scope.options.items.push(item);
            		}
            	});
        		var itemsLength = $scope.options.items.length;
        		var selectedItemsLength = $scope.options.selectedItems.length;
        		$scope.isSave = itemsLength > 0 || selectedItemsLength > 0;
            }
            
            // save
            $scope.save = function() {
				$scope.isSave = false;
            	if(self.locals.params){
            		// compare list.
            		//$scope.compareList();
            		var itemsLength = $scope.options.items.length;
            		var selectedItemsLength = $scope.options.selectedItems.length;
            		if(itemsLength == 0 && selectedItemsLength == 0){
            			return;
            		}
            		var countItems = 0;
            		var countSelectedItems = 0;
            		$scope.showMessageOnToast($translate.instant('clientwh_home_saving'));
            		// insert material detail in source list.
            		$scope.options.items.forEach(function(item){
            			if(item.isSource === false){
            				countItems++;
                			$scope.formdetailServices[$scope.comparemateriallist.targetCombox].getById(item.id).then(
                				// success.
                				function(response){
                					// create.
                					if($scope.comparemateriallist.sourceCombox == 'materialformprematerial' || $scope.comparemateriallist.sourceCombox == 'materialformtechmaterial') {
                						response.data.idmaterialform = self.locals.params.id;
                					} else if ($scope.comparemateriallist.sourceCombox == 'quotation') {
                						response.data.idquotation = self.locals.params.id;
                					} else if ($scope.comparemateriallist.sourceCombox == 'purchase') {
                						response.data.idpurchase = self.locals.params.id;
                					} else  if ($scope.comparemateriallist.sourceCombox == 'request') {
                						response.data.idrequest = self.locals.params.id;
                					} else if ($scope.comparemateriallist.sourceCombox == 'materialbaseline') {
                						response.data.idmaterialbaseline = self.locals.params.id;
                					} else if ($scope.comparemateriallist.sourceCombox == 'materialimport') {
                						response.data.idmaterialimport = self.locals.params.id;
                					} else if ($scope.comparemateriallist.sourceCombox == 'materialexport') {
                						response.data.idmaterialexport = self.locals.params.id;
                					} else {
                                        $scope.showMessageOnToastList($translate.instant('clientwh_home_error'));
                                        return;
                					}
                					response.data.id = -1;
                        			$scope.formdetailServices[$scope.comparemateriallist.sourceCombox].create(response.data).then(
                        				// success.
                        				function(response1){
                        					countItems--;
                        					// push item to result list.
                        					$scope.resultList.push(item);
                        					$scope.sourceItems.push(item);
                                			// Clear selected items.
                                			if(countItems === 0 && countSelectedItems === 0){
                                				$scope.options.items = [];
                                				$scope.options.selectedItems = [];
                            					$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
                                			}
                        				},
                        				// error.
                        				function(response1){
                                            $scope.showMessageOnToastList($translate.instant('clientwh_home_error'));
                                            return;
                        				}
                            		);
                				},
                                // error.
                                function (response) {
                                    $scope.showMessageOnToastList($translate.instant('clientwh_home_error'));
                                    return;
                                }
                			);
            			}
            		});
            		// delete material detail in source list.
            		$scope.options.selectedItems.forEach(function(item){
            			if(item.isSource === true){
            				countSelectedItems++;
                			$scope.formdetailServices[$scope.comparemateriallist.sourceCombox].updateForDeleteWithLock(item.id, item.version).then(
                				// success.
                				function(response){
                					countSelectedItems--;
                					// remove item in target list.
                					let index = clientwh.IndexOfByProperty($scope.options.selectedItems, "id", item.id);
                					$scope.sourceItems.splice(index, 1);
                        			// Clear selected items.
                        			if(countItems === 0 && countSelectedItems === 0){
                        				$scope.options.items = [];
                        				$scope.options.selectedItems = [];
                    					$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
                        			}
                				},
                                // error.
                                function (response) {
                                    $scope.showMessageOnToastList($translate.instant('clientwh_home_error'));
                                    return;
                                }
                			);
            			}
            		});
        			if(countItems === 0 && countSelectedItems === 0){
    					$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
        			}
            	}
            }

            
            // Close dialog.
            $scope.closeDialog = function() {
            	var response = { result: true };
            	$mdDialog.hide(response);
            }
            
            /*Extend functions*/
    		
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
    						controller  : 'clientwhcategoryController',
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
    						controller  : 'clientwhcategoryController',
    						position: 'right'}).then(function(response){
    							if (response) {
    								  $scope.delete(id,version);
    							}
    						});
    		}
			
			// Check params.
    		$scope.isSave = false;
			if(self.locals.params) {
				if(self.locals.params.name == 'materialformprematerial' || self.locals.params.name == 'materialformtechmaterial'){
					$scope.isSave = true;
				}
				$scope.comparemateriallist.sourceCombox = self.locals.params.name;
				$scope.changeSourceCombox();
			}
			
	}]);

});
