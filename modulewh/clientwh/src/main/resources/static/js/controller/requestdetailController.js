
/**
 * Controller for Requestdetail
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'requestdetailController', ['$scope','$mdToast', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', '$q', clientwh.prefix + 'requestdetailService', clientwh.prefix + 'materialService',
		function($scope,$mdToast, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, $q, requestdetailService, materialService) {
		if(typeof(clientwh.translate.requestdetail) === 'undefined' || clientwh.translate.requestdetail.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.requestdetail) === 'undefined') {
				clientwh.translate.requestdetail = '';
			}
			clientwh.translate.requestdetail += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/requestdetail');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_requestdetail_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    $translate.onReady().then(function() {
	    	$scope.title = $translate.instant('clientwh_requestdetail_title');
	    	$translate.refresh();
	    });
		
	    // Search.
	    $scope.search = {};
	    
		// Paging.
		$scope.page = {
			pageSize: 6,
			totalElements: 0,
			currentPage: 0
		}
		
		$scope.requestdetail = {};
		
		$scope.request = undefined;
	    $scope.idrequest = undefined;
	    
	    // commnetView.
	    $scope.commentOnlyView = clientwh.contextPath + "/view/comment_view.html";
	    
		
	    // comment.
		$scope.commentScope = {
			view: clientwh.contextPath + '/view/comment_list.html',
			idref: $scope.requestdetail.id,
			reftype: 'requestdetail'
		}
		
	    // Attachment.
		$scope.attachmentScope = {
			view: clientwh.contextPath + '/view/attachment_list.html',
			viewdetail: clientwh.contextPath + '/view/attachment_view.html',
			idref: $scope.requestdetail.id,
			reftype: 'requestdetail'
		}
	    
	    // Promise list for select.
		var listAllSelectPromise;
		
		// Init for list.
		$rootScope.idrequestdetailController = $scope.$id;
		$scope.initList = function() {
			if(typeof(listAllSelectPromise) === 'undefined') {
				var listAllSelectDefered = $q.defer();
				listAllSelectPromise = listAllSelectDefered.promise;
				listAllSelectDefered.resolve([]);
			}
			listAllSelectPromise.then(
				// Success.
				function(response) {
					// watch idrequest.
					if($rootScope.watchidrequest) {
						$rootScope.watchidrequest();
					}
					$rootScope.watchidrequest = $scope.$watch(
							function(scope) {
								return scope.$parent ? scope.$parent.requestdetailScope.idref : null;
							},
							function(newValue, oldValue) {
								if(newValue > -1 && oldValue==newValue && $scope.$parent.formdetailcopy.length !== undefined ){
									$scope.requestdetailsCopy = $scope.$parent.formdetailcopy
									$scope.page.totalElements = $scope.requestdetailsCopy.length;
									$scope.idstore = $scope.$parent.request.idstore;
									$scope.idrequest = newValue; 
									$scope.requestdetails = [];
									for(var i = 0; i < $scope.page.totalElements; i++){
										var requestdetail = {idrequest: newValue, idmaterial: $scope.requestdetailsCopy[i].idmaterial, note: $scope.requestdetailsCopy[i].note }
										if (typeof $scope.requestdetailsCopy[i].softquantity !== "undefined") {
											requestdetail.softquantity = $scope.requestdetailsCopy[i].softquantity;
										}
										else {requestdetail.softquantity = 0;}
										if (typeof $scope.requestdetailsCopy[i].quantity !== "undefined") {
											requestdetail.quantity = $scope.requestdetailsCopy[i].quantity;
										}
										else {requestdetail.quantity = 0;}
										
										if (typeof $scope.requestdetailsCopy[i].workitem !== "undefined") {
											requestdetail.workitem = $scope.requestdetailsCopy[i].workitem;
										}
										else {requestdetail.workitem = null;}
										
										if (typeof $scope.requestdetailsCopy[i].deliverydate !== "undefined") {
											requestdetail.deliverydate = $scope.requestdetailsCopy[i].deliverydate;
										}
										else {requestdetail.deliverydate = null;}
										
										if (typeof $scope.requestdetailsCopy[i].drawingname !== "undefined") {
											requestdetail.drawingname = $scope.requestdetailsCopy[i].drawingname;
										}
										else {requestdetail.drawingname = null;}
										if (typeof $scope.requestdetailsCopy[i].teamname !== "undefined") {
											requestdetail.teamname = $scope.requestdetailsCopy[i].teamname;
										}
										else {requestdetail.teamname = null;}
										$scope.requestdetails.push(requestdetail);
									}
									requestdetailService.createWithSelectDetail(JSON.stringify($scope.requestdetails)).then(function(response){
										if($scope.idrequest > -1 && $rootScope.idrequestdetailController == $scope.$id) {
											$scope.listWithCriteriasByIdrequestAndPage($scope.idrequest, 1);
											$scope.$parent.formdetailcopy = []
										}
									},function(response){
										
									})
								} else {
									if($scope.$parent.request) {
										$scope.requestdetails = [];
										$scope.page.totalElements = 0;
										$scope.attachments = [];
										$scope.idstore = $scope.$parent.request.idstore;
										$scope.idrequest = $scope.$parent.request.id;
										if($scope.idrequest > -1 && $rootScope.idrequestdetailController == $scope.$id) {
											$scope.listWithCriteriasByIdrequestAndPage($scope.idrequest, 1);
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

			// load material by idstore.
			var listMaterialByScopeForSelectDeferred = $q.defer();
			if($scope.idstore){
				listMaterialByScopeForSelectDeferred.resolve();
				//listMaterialByScopeForSelectDeferred = materialService.loadAllFromBaselineByIdstore($scope.idstore);
				} 
			else {
				listMaterialByScopeForSelectDeferred.resolve();
			}
				
			// Response.
			$q.all([listMaterialByScopeForSelectDeferred]).then(
				// Successes.
				function(responses) {
				
					$scope.ctlentityfieldMaterial.items  = responses[0].data;
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
			// set idrequest.
			$scope.idrequest = $scope.$parent ? $scope.$parent.request.id : -1;
			$scope.idstore = $scope.$parent ? $scope.$parent.request.idstore : -1;
			// create new.
			$scope.createNew();
			$scope.requestdetail.id = id;
			if($scope.requestdetail.id > -1) {
				$scope.commentScope.idref = $scope.requestdetail.id;
				$scope.getById($scope.requestdetail.id);
			}
			var result;
			// load material from baseline.
			if($scope.scope == "normal"){
				result = materialService.loadAllFromBaselineByIdstore($scope.idstore);
				}
			else{
				result = materialService.loadAllMaterialConfirmed();
			}
			result
			.then(
				// success.
				function(response){
					$scope.ctlentityfieldMaterial.items  = response.data;
					$scope.itemSelected();
				},
				// error.
				function(response){
					$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
				}
			);

			// comment.
			$scope.commentScope.idref = $scope.requestdetail.id;

			// attachment.
			$scope.attachmentScope.idref = $scope.requestdetail.id;
			
			$scope.frmDirty = false;
		}
		
		// Show detail 
		$scope.showDetailrequestdetail = function(id){
			$scope.idrequestdetail = id;
			$scope.getByIdForViewDetail(id);
			$scope.requestdetail.id = id;
			$scope.commentScope.idref = $scope.requestdetail.id;
			if($scope.requestdetail.id > -1) {
				$scope.getById($scope.requestdetail.id);
			}
			// Attachment.
			$scope.attachmentScope.idref = $scope.requestdetail.id;
			$scope.showDialogDetail();
		}
		//Show detail store
		$scope.showDialogDetail = function () {
			var htmlUrlTemplate = clientwh.contextPath + '/view/requestdetail_list_view.html';
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
		}
		
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
						controller  : 'clientwhrequestdetailController',
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
						controller  : 'clientwhrequestdetailController',
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
		
		// Show a form screen.
		$scope.showForm = function(id) {
			$scope.initForm(id);
			$scope.showDialog();
		}
		
		$scope.materials = []
	    
		//Show Dialog with controller.
		$scope.showDialogWithController = function() {
			var htmlUrlTemplate = clientwh.contextPath + '/view/selectfordetail_form.html';
			var params = {name:'request',id:$scope.idrequest}
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
			$scope.requestdetailSelected = [];
			for (var i = 0; i < materials.length; i++) {
				var requestdetail = {};
				requestdetail.id = -1;
				requestdetail.idrequest = $scope.idrequest;
				requestdetail.idmaterial = materials[i].idmaterial;
				requestdetail.note = materials[i].note;
				if (typeof materials[i].quantity !== "undefined") {
					requestdetail.quantity = materials[i].quantity;
				}
				else {
					requestdetail.quantity = 0;
				}
				if (typeof materials[i].softquantity !== "undefined") {
					requestdetail.softquantity = materials[i].softquantity;
				}
				else {
					requestdetail.softquantity = 0;
				}
				if (typeof materials[i].workitem !== "undefined") {
					requestdetail.workitem = materials[i].workitem;
				}
				else {
					requestdetail.workitem = null;
				}
				if (typeof materials[i].deliverydate !== "undefined") {
					requestdetail.deliverydate = materials[i].deliverydate;
				}
				else {
					requestdetail.deliverydate = null;
				}
				if (typeof materials[i].drawingname !== "undefined") {
					requestdetail.drawingname = materials[i].drawingname;
				}
				else {
					requestdetail.drawingname = null;
				}
				if (typeof materials[i].teamname !== "undefined") {
					requestdetail.teamname = materials[i].teamname;
				}
				else {
					requestdetail.teamname = null;
				}
				$scope.requestdetailSelected.push(requestdetail);
			}
			// Create With Select Detail
			requestdetailService.createWithSelectDetail(JSON.stringify($scope.requestdetailSelected))
			.then(function(response){
				$scope.listWithCriteriasByIdrequestAndPage($scope.idrequest, 1);
			},function(response){
				
			});
		}
		
		$scope.inputQuantity = {};
		$scope.indexItemQuantity = 0;
	    
	    // Show input quantity view.
	    $scope.showInputQuantityFormDialog = function () {
	    	$scope.indexItemQuantity = 0;
	    	$scope.listMaterialsByIdQuantity($scope.idrequest);
	        var htmlUrlTemplate = clientwh.contextPath + '/view/inputquantity_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }
	   
	    // List materials by id for input quantity
	    $scope.listMaterialsByIdQuantity = function(idrequest){
	    	requestdetailService.getAllById(idrequest).then(function(response){
	    		$scope.materialdetaillistQuantity = response.data;
	    		$scope.indexItemQuantity = 0;
	    		$scope.idselectedQuantity = [$scope.materialdetaillistQuantity[$scope.indexItemQuantity].id];
	    		$scope.inputQuantity.materialname = $scope.materialdetaillistQuantity[$scope.indexItemQuantity].materialname
	    		$scope.inputQuantity.quantity = $scope.materialdetaillistQuantity[$scope.indexItemQuantity].quantity
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
	    
	  //Check null item when input quantity
	    $scope.checkNulItemQuantity = function(arrInputQuantity){
	    	for (var i = 0; i < arrInputQuantity.length; i++) {
				if (arrInputQuantity[i].quantity===null) {
					return i
				} 
			}
	    	return -1;
	    }
	    
	    $scope.requestUpdateQuantity = [];
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
	    		$scope.requestUpdateQuantity = $scope.materialdetaillistQuantity
		    	delete $scope.requestUpdateQuantity.materialname
		    	// Create With Select Detail
		    	requestdetailService.requestUpdateQuantity(JSON.stringify($scope.requestUpdateQuantity))
				.then(function(response){
					if(response.status === httpStatus.code.OK) {
						$scope.listMaterialsByIdQuantity($scope.idrequest);
						$scope.listWithCriteriasByIdrequestAndPage($scope.idrequest, 1);
						$scope.showMessageOnToast($translate.instant('clientwh_home_update_quantity_successfully'))
					}	
				},function(response){
					$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
				});
				$scope.requestUpdateQuantity = [];
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
	    	$mdToast.hide();
	        var htmlUrlTemplate = clientwh.contextPath + '/view/requestdetail_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }

	    // Create new.
		$scope.createNew = function() {
			$scope.requestdetail = { id: -1, idrequest: $scope.idrequest };
		}
		
		// Reset validate.
		$scope.resetValidate = function() {
			
			$scope.ctlentityfieldMaterial.searchText = undefined;
			$scope.ctlentityfieldMaterial.selectedItem = undefined;
			
			
			// idmaterial.
		    $scope.frmRequestdetail.idmaterial.$setPristine();
			$scope.frmRequestdetail.idmaterial.$setUntouched();
			// softquantity.
		    $scope.frmRequestdetail.softquantity.$setPristine();
			$scope.frmRequestdetail.softquantity.$setUntouched();
			// quantity.
		    $scope.frmRequestdetail.quantity.$setPristine();
			$scope.frmRequestdetail.quantity.$setUntouched();
			// workitem.
		    $scope.frmRequestdetail.workitem.$setPristine();
			$scope.frmRequestdetail.workitem.$setUntouched();
			// deliverydate.
		    $scope.frmRequestdetail.deliverydate.$setPristine();
			$scope.frmRequestdetail.deliverydate.$setUntouched();
			// drawingname.
		    $scope.frmRequestdetail.drawingname.$setPristine();
			$scope.frmRequestdetail.drawingname.$setUntouched();
			// teamname.
		    $scope.frmRequestdetail.teamname.$setPristine();
			$scope.frmRequestdetail.teamname.$setUntouched();
			// note.
		    $scope.frmRequestdetail.note.$setPristine();
			$scope.frmRequestdetail.note.$setUntouched();

		    // form.
			$scope.frmRequestdetail.$setPristine();
			$scope.frmRequestdetail.$setUntouched();
			// frmDirty.
			$scope.frmDirty = false;
			
			$scope.ctlentityfieldMaterial.selectedItem = null;
			$scope.frmRequestdetail.quantity.$invalid = false;
			$scope.frmRequestdetail.softquantity.$invalid = false;
			$scope.frmRequestdetail.workitem.$invalid = false;
			$scope.frmRequestdetail.deliverydate.$invalid = false;
			$scope.frmRequestdetail.drawingname.$invalid = false;
			$scope.frmRequestdetail.teamname.$invalid = false;
			$scope.frmRequestdetail.note.$invalid = false;
			
			$scope.frmRequestdetail.idmaterial.$invalid = false;
		}
		
		$scope.resetInvalidMaterial = function(){
			$scope.frmRequestdetail.idmaterial.$invalid = true;
		}
		
		$scope.resetInvalidDeliveryDate = function(){
			$scope.frmRequestdetail.deliverydate.$invalid = true;
		}
		
		// Create on form.
		$scope.createOnForm = function() {
			$scope.createNew();
			$scope.resetValidate();
		}
		
		
		
		// Save.
		$scope.save = function() {
			if($scope.ctlentityfieldMaterial.selectedItem==null){
				$scope.frmRequestdetail.idmaterial.$invalid = true;
				$scope.frmRequestdetail.idmaterial.$touched = true;
			}
			if ($scope.requestdetail.deliverydate==null) {
				$scope.frmRequestdetail.deliverydate.$invalid = true;
				$scope.frmRequestdetail.deliverydate.$touched = true;
			}
			if(
					$scope.frmRequestdetail.$invalid||
					$scope.frmRequestdetail.idmaterial.$invalid||$scope.frmRequestdetail.deliverydate.$invalid) {
				$scope.frmRequestdetail.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			$scope.showMessageOnToast($translate.instant('clientwh_home_saving'), 'alert-success', false);
			var result;
			if($scope.requestdetail.id > -1) {
				result = requestdetailService.updateWithLock($scope.requestdetail.id, $scope.requestdetail);
			} else {
				result = requestdetailService.create($scope.requestdetail);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.requestdetail.id > -1) {
						$scope.requestdetail.version = response.data;
					} else {
						$scope.requestdetail.id = response.data;

						// Attachment.
						$scope.attachmentScope.idref = $scope.requestdetail.id;
						// comment.
						$scope.commentScope.idref = $scope.requestdetail.id;
						
						$scope.requestdetail.version = 1;
					}
					$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
					$scope.listWithCriteriasByIdrequestAndPage($scope.idrequest, 1);
				} else {
					if(response.data.code == clientwh.serverCode.EXISTSCOPE) {
						$scope.frmRequestdetail.scope.$invalid = true;
						$scope.showMessageOnToast($translate.instant('clientwh_servercode_' + response.data.code));
					} else if(response.data.code == clientwh.serverCode.VERSIONDIFFERENCE) {
						$scope.showMessageOnToast($translate.instant('clientwh_servercode_' + response.data.code));
					} else if(response.data.code == clientwh.serverCode.EXITMATERIAL) {
						$scope.showMessageOnToast($translate.instant('clientwh_requestdetail_exit_material'));
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
		
		// Delete.
		$scope.delete = function(id, version) {
				requestdetailService.updateForDeleteWithLock(id, version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToastList($translate.instant('clientwh_home_deleted'));
						$scope.listWithCriteriasByIdrequestAndPage($scope.idrequest, 1);
					} else {
						$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
						}
				},
				// error.
				function(response) {
					$scope.showMessage($translate.instant('clientwh_home_error'));
				});
		}
		
		// Delete with create.
		$scope.deleteOnForm = function() {
				requestdetailService.updateForDeleteWithLock($scope.requestdetail.id, $scope.requestdetail.version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToast('Deleted!', 'alert-success', true);
						$scope.createNew();
						$scope.resetValidate();
						$scope.listWithCriteriasByIdrequestAndPage($scope.idrequest, 1);
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
			requestdetailService.getById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.requestdetail = data;
					$scope.requestdetail.deliverydate = new Date($scope.requestdetail.deliverydate);
					if($scope.ctlentityfieldMaterial.items){
						$scope.itemSelected();
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
		
		// Get by Id for view.
		$scope.getByIdForViewDetail = function(id) {
			requestdetailService.getByIdForView(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.requestdetailView = data;
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
			requestdetailService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.requestdetails = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.requestdetails = result;
						for (var i = 0; i < $scope.requestdetails.length; i++) {
							if ($scope.requestdetails[i].deliverydate!=null) {
								$scope.requestdetails[i].deliverydate = new Date($scope.requestdetails[i].deliverydate).toLocaleDateString();
							}
						}
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
		$scope.listWithCriteriasByIdrequestAndPage = function(idrequest, pageNo) {
			$scope.page.currentPage = pageNo;
			requestdetailService.listWithCriteriasByIdrequestAndPage(idrequest, $scope.getSearch(), pageNo - 1, $scope.page.pageSize).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.requestdetails = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.requestdetails = result;
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
			$scope.listWithCriteriasByIdrequestAndPage($scope.idrequest,1);
		}
		/*Extend functions*/
		
		
		// Get search object.
		$scope.getSearch = function() {
			var result = [];
			// code.
		    if(typeof($scope.search.content) !== 'undefined' && $scope.search.content !== ''){
		    	result.push({ key: 'material.name', operation: 'like', value: $scope.search.content, logic: 'or' });
		    }
		    	if (typeof($scope.search.content)==='number') {
		    		result.push({ key: 'quantity', operation: '=', value: $scope.search.content, logic: 'or' });
				}		    
		    // return.
		    return result;
		}
		
		
		$scope.itemSelected = function(){
			for (var i = 0; i < $scope.ctlentityfieldMaterial.items.length; i++) {
				if($scope.requestdetail.idmaterial==$scope.ctlentityfieldMaterial.items[i].value){
					$scope.ctlentityfieldMaterial.selectedItem = {};
					$scope.ctlentityfieldMaterial.selectedItem = $scope.ctlentityfieldMaterial.items[i];
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
	    	if(typeof(frmRequestdetail) === 'undefined' || frmRequestdetail.idmaterial === undefined) {
	    		return;
	    	}
	    	$scope.requestdetail.idmaterial = undefined;
	    	frmRequestdetail.idmaterial.$invalid = true;
	    	if(item) {
	    		$scope.requestdetail.idmaterial = item.value;
	    		frmRequestdetail.idmaterial.$invalid = false;
	    	}
	    }
	
	}]);

});
