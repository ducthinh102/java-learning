
/**
 * Controller for Quotation
 */

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'quotationController', ['$scope', '$stateParams','$mdBottomSheet', '$mdToast', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', '$q', 'moment', clientwh.prefix + 'quotationdetailService', clientwh.prefix + 'supplierService', clientwh.prefix + 'quotationService', clientwh.prefix + 'userService', clientwh.prefix + 'workflowexecuteService', // workflowexecute module 
			function($scope, $stateParams, $mdBottomSheet, $mdToast, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, $q, moment, quotationdetailService, supplierService, quotationService, userService, workflowexecuteService) { // workflowexecute module
		if(typeof(clientwh.translate.quotation) === 'undefined' || clientwh.translate.quotation.indexOf($translate.use()) < 0) {
			console.log(clientwh.translate.quotation);
			if(typeof(clientwh.translate.quotation) === 'undefined') {
				clientwh.translate.quotation = '';
			}
			clientwh.translate.quotation += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/quotation');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
	    	console.log('clientwh_quotation_title');
		    $scope.title = $translate.instant('clientwh_quotation_title');
		});

		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    $translate.onReady().then(function() {
	    	$scope.title = $translate.instant('clientwh_quotation_title');
	    	$translate.refresh();
	    });
	    
	    //store id
	    $scope.storeIdQuotation = function(id){
	    	$rootScope.idQuotation = id;
	    	$rootScope.idMaterialimport = null;
	    	$rootScope.idMaterialexport = null;
	    	$rootScope.idPurchase = null;
	    	$rootScope.idRequest = null;
	    	$state.go(clientwh.prefix + 'selectfordetail');
	    }
	    
	    //go to
//	    $scope.goto = function() {
//			$state.go(clientwh.prefix + 'selectfordetail');
//		}
//	    
	    // quotationdetailView.
	    $scope.quotationdetailView = clientwh.contextPath + "/view/quotationdetail_list.html";
	    
	    // materialimportdetailForView.
	    $scope.quotationdetailForView = clientwh.contextPath + "/view/quotationdetail_view.html";
		
	    // Search.
	    $scope.search = {};
	    
		// Paging.
		$scope.page = {
			pageSize: 9,
			totalElements: 0,
			currentPage: 0
		}
		
		$scope.quotation = {};
		
		//DeleteToastList
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
						controller  : 'clientwhquotationController',
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
		
		 $scope.showCopyTo = function(id) {
	    	    $scope.getById(id);
	    	    $scope.getAllByQuotation(id);
	    	    $mdBottomSheet.show({
	    	      templateUrl: clientwh.contextPath + '/view/copyTo_form.html',
	    	      controller  : 'clientwhquotationController'
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
		//DeleteToastListFrom
		$scope.cofirmDeleteToastForm = function(){
			$mdToast.show(
					{  	templateUrl : clientwh.contextPath + '/view/toast.html',
						hideDelay:5000,
						controller  : 'clientwhquotationController',
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
		$scope.initList = function() {
			if(typeof(listAllSelectPromise) === 'undefined') {
				var listAllSelectDefered = $q.defer();
				listAllSelectPromise = listAllSelectDefered.promise;
				listAllSelectDefered.resolve([]);
			}
			listAllSelectPromise.then(
				// Success.
				function(response) {
					// Get permission.
					userService.definePermissionByTarget($scope, 'quotation').then(
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
							$scope.quotation.id = id;
							if($scope.quotation.id > -1) {
								$scope.getById($scope.quotation.id);
							}
							$scope.frmDirty = false;
							
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
					$scope.suppliers = [];
					// Call service.
					var listSupplier = supplierService.getListScope();
					// Response.
					$q.all([listSupplier]).then(
						// Successes.
						function(responses) {
							$scope.suppliers = responses[0].data;
							$scope.ctlentityfieldSupplier.items = $scope.loadItemsData($scope.suppliers);
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
				
				
				//Call supplier for select
				$scope.SupplierForSelect = function() {
					supplierService.getListScope().then(function(responses) {
						$scope.suppliers = responses.data;
						$scope.ctlentityfieldSupplier.items = $scope.loadItemsData($scope.suppliers);
						$scope.itemSelected();
					},function(responses) {
						
					})
				}
				
				// Call and return a promise.
				listAllSelectPromise = $scope.listAllForSelect();
				
				
				$scope.itemSelected = function(){
					for (var i = 0; i < $scope.ctlentityfieldSupplier.items.length; i++) {
						if($scope.quotation.idsupplier==$scope.ctlentityfieldSupplier.items[i].value){
							$scope.ctlentityfieldSupplier.selectedItem = {};
							$scope.ctlentityfieldSupplier.selectedItem = $scope.ctlentityfieldSupplier.items[i];
						}
					}
				}
				
//				$scope.resetInvalidSupplier = function(){
//					$scope.frmQuotation.idsupplier.$invalid = true;
//				}
				
				$scope.resetInvalidStartdate = function(){
					$scope.frmQuotation.startdate.$invalid = true;
				}
				
				$scope.resetInvalidEnddate = function(){
					$scope.frmQuotation.enddate.$invalid = true;
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
				// Auto complete: idSupplier
				// //////////////////////////////////////
				$scope.ctlentityfieldSupplier = {};		
				$scope.ctlentityfieldSupplier.isCallServer = false;
			    $scope.ctlentityfieldSupplier.isDisabled    = false;	    
			    
			    // New.
			    $scope.ctlentityfieldSupplier.newState = function(item) {
			      alert("Sorry! You'll need to create a Constitution for " + item + " first!");
			    }
			    // Search in array.
			    $scope.ctlentityfieldSupplier.querySearch = function(query) {
			      var results = query ? $scope.ctlentityfieldSupplier.items.filter( $scope.ctlentityfieldSupplier.createFilterFor(query) ) : $scope.ctlentityfieldSupplier.items,
			          deferred;
			      
			      if ($scope.ctlentityfieldSupplier.isCallServer) {
			        deferred = $q.defer();
			        $timeout(function () { deferred.resolve( results ); }, Math.random() * 1000, false);
			        return deferred.promise;
			      } else {
			        return results;
			      }
			    }
			    // Filter.
			    $scope.ctlentityfieldSupplier.createFilterFor = function(query) {
					 var lowercaseQuery = angular.lowercase(query);
					
					 return function filterFn(item) {
						 return (angular.lowercase(item.display).indexOf(lowercaseQuery) >= 0);
					 };
			    }
			    // Text change.
			    $scope.ctlentityfieldSupplier.searchTextChange = function(text) {
			    	$log.info('Text changed to ' + text);
			    }
			    // Item change.
			    $scope.ctlentityfieldSupplier.selectedItemChange = function(item) {
			    	if(typeof $scope.frmQuotation.idsupplier === 'undefined') {
			    		return;
			    	}
			    	$scope.quotation.idsupplier = undefined;
			    	$scope.frmQuotation.idsupplier.$invalid = true;
			    	if(item) {
			    		$scope.quotation.idsupplier = item.value;
			    		$scope.frmQuotation.idsupplier.$invalid = false;
			    	}
			    }
			    
			    
			  
		
		// Show a create screen.
		$scope.showCreate = function() {
			$scope.initForm(-1);
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.showDialog();
			$scope.ctlentityfieldSupplier.selectedItem = null;
		}
		
		// Show a form screen.
		$scope.showForm = function(id) {
			$scope.initForm(id);
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.showDialog();
			$scope.ctlentityfieldSupplier.selectedItem = null;
		}
		
		
		//Show form detail
		$scope.showDetail = function(id){
			$scope.quotation.id = id;
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.getByIdForView(id);
			$scope.showDialogDetail();
		}
		
		
		//Show dialog view
		$scope.showDialogDetail = function () {
	        var htmlUrlTemplate = clientwh.contextPath + '/view/quotation_view.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }

	    // Show edit view.
	    $scope.showDialog = function () {
	        var htmlUrlTemplate = clientwh.contextPath + '/view/quotation_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }
	    
	 // Show supplier view.
	    $scope.showDialog2 = function () {
	    	var param = {showBtnClose:true}
	        var htmlUrlTemplate = clientwh.contextPath + '/view/supplier_list.html';
	        clientwh.showDialogWithControllerName(clientwh.prefix + 'supplierController', 'supplierController', $mdDialog, htmlUrlTemplate,param).then(function(evt) {
	        	
	        	$scope.quotation.idsupplier = evt.id;
	        	$scope.SupplierForSelect();
	        	
	        	
	        	console.log('closed');
	        }, function(response) {
	        	console.log(response.id);
	        });
	    }
	    
	    
	 // Show a form to create supplier.
		$scope.showForm2 = function() {
			
			$scope.showDialog2();
			
			//$scope.initForm(-1);
			//$scope.ctlentityfieldSelectcode.selectedItem = id;
		}


	    // Create new.
		$scope.createNew = function() {
			$scope.quotation = { id: -1 };
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.ctlentityfieldSupplier.selectedItem = null;
		}
		
		// Reset validate.
		$scope.resetValidate = function() {
			// idsupplier.
		    $scope.frmQuotation.idsupplier.$setPristine();
			$scope.frmQuotation.idsupplier.$setUntouched();
			// code.
		    $scope.frmQuotation.code.$setPristine();
			$scope.frmQuotation.code.$setUntouched();
			// name.
//		    $scope.frmQuotation.name.$setPristine();
//			$scope.frmQuotation.name.$setUntouched();
			// startdate.
		    $scope.frmQuotation.startdate.$setPristine();
			$scope.frmQuotation.startdate.$setUntouched();
			// enddate.
		    $scope.frmQuotation.enddate.$setPristine();
			$scope.frmQuotation.enddate.$setUntouched();
			// note.
		    $scope.frmQuotation.note.$setPristine();
			$scope.frmQuotation.note.$setUntouched();

		    // form.
			$scope.frmQuotation.$setPristine();
			$scope.frmQuotation.$setUntouched();
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
			
			if($scope.ctlentityfieldSupplier.selectedItem==null){
				$scope.frmQuotation.idsupplier.$invalid = true;
			}
			
			if ($scope.quotation.startdate==null) {
				$scope.frmQuotation.startdate.$invalid = true;
			}
			
			if ($scope.quotation.enddate==null) {
				$scope.frmQuotation.enddate.$invalid = true;
			}
			
			if ($scope.quotation.code==null) {
				$scope.frmQuotation.code.$invalid = true;
			}
			
			if($scope.frmQuotation.$invalid||$scope.frmQuotation.idsupplier.$invalid||$scope.frmQuotation.startdate.$invalid||$scope.frmQuotation.enddate.$invalid||$scope.frmQuotation.code.$invalid) {
				$scope.frmQuotation.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			$scope.showMessageOnToast($translate.instant('clientwh_home_saving'));
			
			$scope.subtractTwoDate();
			
			if ($scope.subtractTwoDate()==false) {
				$scope.showMessageOnToast($translate.instant('clientwh_quotation_date'));
//				$scope.frmQuotation.scope.$invalid = true;
//				$scope.frmQuotation.$dirty = true;
//				$scope.frmDirty = true;
				return;
			}
			
			var result;
			if($scope.quotation.id > -1) {
				result = quotationService.updateWithLock($scope.quotation.id, $scope.quotation);
			} else {
				result = quotationService.create($scope.quotation);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.quotation.id > -1) {
						$scope.quotation.version = response.data;
					} else {
						$scope.quotation.id = response.data;
						$scope.quotation.version = 1;
					}
					$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
					$scope.listWithCriteriasByPage(1);
				} else {
					if(response.data.code == clientwh.serverCode.EXISTCODE) {
						//alert(1);
						$scope.frmQuotation.code.$invalid = true;
						$scope.showMessageOnToast($translate.instant('clientwh_quotation_exist'));
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
		
		// Send mail.
		$scope.sendMail = function(id) {
			
				quotationService.saveCsvFileAndSendMailByIdquotation(id)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToastList($translate.instant('clientwh_home_saved'));
					} else {
						$scope.showMessageOnToastList($translate.instant('clientwh_home_error'));
					}
				},
				// error.
				function(response) {
					$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
				});
			
		}
		
		// Delete.
		$scope.delete = function(id, version) {
			
				quotationService.updateForDeleteWithLock(id, version)
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
				quotationService.updateForDeleteWithLock($scope.quotation.id, $scope.quotation.version)
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
			quotationService.getById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.quotation = data;
					$scope.quotation.startdate = new Date($scope.quotation.startdate);
					$scope.quotation.enddate = new Date($scope.quotation.enddate);
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
		
		
		// Get by Id for view
		$scope.getByIdForView = function(id) {
			quotationService.getByIdForView(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.quotationView = data;
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
			//quotationService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// workflowexecute module
			workflowexecuteService.listWithCriteriasByPage($state, $scope.workflowTab.id, $scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.quotations = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.quotations = result;
						
						for (var i = 0; i < $scope.quotations.length; i++) {
							if ($scope.quotations[i].startdate!=null) {
								$scope.quotations[i].startdate = new Date($scope.quotations[i].startdate).toLocaleDateString();
							}
						}
						
						for (var i = 0; i < $scope.quotations.length; i++) {
							if ($scope.quotations[i].enddate!=null) {
								$scope.quotations[i].enddate = new Date($scope.quotations[i].enddate).toLocaleDateString();
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
			$scope.listWithCriteriasByPage(1);
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
				// idsupplier.
		    	// result.push({ key: 'supplier.name', operation: 'like', value: $scope.search.content, logic: 'or' });
				// code.
		    	 result.push({ key: 'code', operation: 'like', value:
				 $scope.search.content, logic: 'or' });
				// name.
		    	// result.push({ key: 'name', operation: 'like', value:
				// $scope.search.content, logic: 'or' });
				// startdate.
//		    	 result.push({ key: 'startdate', operation: 'like', value:
//				 $scope.search.content, logic: 'or' });
				// enddate.
//		    	 result.push({ key: 'enddate', operation: 'like', value:
//				 $scope.search.content, logic: 'or' });
				// note.
		    	// result.push({ key: 'note', operation: 'like', value:
				// $scope.search.content, logic: 'or' });
		    }
		    // return.
		    return result;
		}
			
		// Show message.
		$scope.showMessage = function(message, cssName, autoHide) {
			$scope.alertMessage = message;
			$('#alertMessage').removeClass('alert-danger');
			$('#alertMessage').removeClass('alert-success');
			$('#alertMessage').addClass(cssName);
			$('#alertMessage').slideDown(500, function() {
				if(autoHide) {
					$window.setTimeout(function() {
						$('#alertMessage').slideUp(500, function() {
							$('#alertMessage').removeClass(cssName);
		            	});
					}, 1000);
				}
			});
		}
		
		//date
//		  $scope.dateFormat = "dd/MM/yyyy";
//		    $scope.startdate = new Date();
//		    $scope.enddate = new Date();
//		
		
		 // Set date options.
	    	$scope.dateOptions = {
		        formatYear: 'yyyy',
		        startingDay: 1
	    	};
	    	
		    
		    
		// Subtract 2 dates.
    	$scope.subtractTwoDate = function() {
    		var startdate = moment($scope.quotation.startdate, 'dd/MM/yyyy');
    		var enddate = moment($scope.quotation.enddate, 'dd/MM/yyyy');
    		var duration = enddate.diff(startdate, 'days');
    		//alert(duration);
    		if (duration <= 0) {
				return false;
			}
    		else {
				return true;
			}
    	}
	
    	$scope.getAllByQuotation = function(idquotation){
    		quotationdetailService.getAllById(idquotation).then(function(response){
    			$scope.quotationdetails = response.data;
    		},function(response){
    			
    		})
    	}
    	
    	$scope.copyTo = function(item){
    		if (item=='materialformprematerial') {
    			$state.go(clientwh.prefix + 'materialform', {scope:'prematerial',reftype: 'quotation', formcopy: $scope.quotation, formdetailcopy: $scope.quotationdetails});
			}
    		else if (item=='materialformtechmaterial') {
    			$state.go(clientwh.prefix + 'materialform', {scope:'techmaterial',reftype: 'quotation', formcopy: $scope.quotation, formdetailcopy: $scope.quotationdetails});
			}
    		else if(item=='requestnormal'){
    			$state.go(clientwh.prefix + 'request', {scope:'normal',reftype: 'quotation', formcopy: $scope.quotation, formdetailcopy: $scope.quotationdetails});
    		}
    		else if(item=='requestextra'){
    			$state.go(clientwh.prefix + 'request', {scope:'extra',reftype: 'quotation', formcopy: $scope.quotation, formdetailcopy: $scope.quotationdetails});
    		}
    		else{
    			$state.go(clientwh.prefix + item, {reftype: 'quotation', formcopy: $scope.quotation, formdetailcopy: $scope.quotationdetails});
    		}
	    }
    	
    	$scope.formcopy = {};
		$scope.formdetailcopy = {};
	    if($stateParams.formcopy&& typeof $scope.workflowTab !== "undefined"&&$scope.workflowTab.id==="created"){
		    $scope.formcopy = $stateParams.formcopy;
		    $scope.formdetailcopy = $stateParams.formdetailcopy;
		    
		    $scope.quotation.idref = $stateParams.formcopy.id;
		    $scope.quotation.reftype = $stateParams.reftype;
		    
		    $scope.quotation.id = -1;
		    $scope.quotation.code = $stateParams.formcopy.code;
		    $scope.quotation.name = $stateParams.formcopy.name;
		    $scope.quotation.startdate = $stateParams.formcopy.startdate;
		    $scope.quotation.enddate = $stateParams.formcopy.enddate;
		    $scope.quotation.idsupplier = $stateParams.formcopy.idsupplier;
		    $scope.quotation.note = $stateParams.formcopy.note;
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
