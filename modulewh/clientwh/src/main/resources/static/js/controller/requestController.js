
/**
 * Controller for Request
 **/

define(['require', 'angular'], function (require, angular) {



		app.aController(clientwh.prefix + 'requestController', ['$scope','$stateParams','$mdBottomSheet','moment','$mdToast', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader','$q', clientwh.prefix + 'requestService',clientwh.prefix + 'userService',clientwh.prefix + 'materialService',clientwh.prefix + 'storeService',clientwh.prefix + 'workflowexecuteService', clientwh.prefix + 'requestdetailService', // workflowexecute module

		function($scope,$stateParams,$mdBottomSheet, moment, $mdToast, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, $q, requestService, userService, materialService,storeService,workflowexecuteService,requestdetailService) { // workflowexecute module
		if(typeof(clientwh.translate.request) === 'undefined' || clientwh.translate.request.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.request) === 'undefined') {
				clientwh.translate.request = '';
			}
			clientwh.translate.request += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/request');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_request_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    $translate.onReady().then(function() {
	    	$scope.title = $translate.instant('clientwh_request_title');
	    	$translate.refresh();
	    });
	    
	    // requestdetailView.
	    $scope.requestdetailView = clientwh.contextPath + "/view/requestdetail_list.html";

	    //store id
	    $scope.storeIdRequest = function(id){
		    	$rootScope.idQuotation = null;
		    	$rootScope.idMaterialimport = null;
		    	$rootScope.idMaterialexport = null;
		    	$rootScope.idPurchase = null;
		    	$rootScope.idRequest = id;
		    	$state.go(clientwh.prefix + 'selectfordetail');
	    }
	    
	    // Search.
	    $scope.search = {};
	    
		$scope.request = {id: -1, scope: $scope.scope};
	    
	    // commnetView.
	    $scope.commentOnlyView = clientwh.contextPath + "/view/comment_view.html";
	    
		
	    // comment.
		$scope.commentScope = {
			view: clientwh.contextPath + '/view/comment_list.html',
			idref: $scope.request.id,
			viewdetail: clientwh.contextPath + '/view/comment_view.html',
			reftype: 'request'
		}
		
		// requestdetail.
		$scope.requestdetailScope = {
			view: clientwh.contextPath + '/view/requestdetail_list.html',
			viewdetail: clientwh.contextPath + '/view/requestdetail_view.html',
			idref: $scope.request.id,
			reftype: 'request'
		}
		
		// Attachment.
		$scope.attachmentScope = {
			view: clientwh.contextPath + '/view/attachment_list.html',
			viewdetail: clientwh.contextPath + '/view/attachment_view.html',
			idref: $scope.request.id,
			reftype: 'request'
		}
	    
		// Paging.
		$scope.page = {
			pageSize: 9,
			totalElements: 0,
			currentPage: 0
		}
		
		
		
		// scope param.
		$scope.scope = undefined;
		if(typeof($state.params) !== 'undefined'){
			$scope.scope = $state.params.scope;
		}
		// Promise list for select.
		var listAllSelectPromise;
		
		// Init for list.
		$scope.initList = function () {
			if (typeof (listAllSelectPromise) === 'undefined') {
				var listAllSelectDefered = $q.defer();
				listAllSelectPromise = listAllSelectDefered.promise;
				listAllSelectDefered.resolve([]);
			}
			listAllSelectPromise.then(
				//Success
				function (response) {
					// Get permission.
					userService.definePermissionByTarget($scope, 'request').then(
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
				//Error
				function (response) { }
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
					$scope.request.id = id;
					if($scope.request.id > -1) {

						$scope.commentScope.idref = $scope.request.id;
						$scope.getById($scope.request.id);
					}
					
					// comment.
					$scope.commentScope.idref = $scope.request.id;
					
					// requestdetail.
					$scope.requestdetailScope.idref = $scope.request.id;
					
					// attachment.
					$scope.attachmentScope.idref = $scope.request.id;
					
					$scope.frmDirty = false;
				},
				// Error.
				function(response) {
					
				}
			);
		}
		
		// Show a create screen.
		$scope.showCreate = function() {
			$scope.initForm(-1);
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.showDialog();
			$scope.ctlentityfieldStore.selectedItem = null;
			$scope.ctlentityfieldWriter.selectedItem = null;
			$scope.ctlentityfieldReceiver.selectedItem = null;
			$scope.ctlentityfieldResponsible.selectedItem = null;
			$scope.ctlentityfieldResponsible.searchText = null;
			$scope.ctlentityfieldStore.searchText = null;
			$scope.ctlentityfieldWriter.searchText = null;
			$scope.ctlentityfieldReceiver.searchText = null;
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
						controller  : 'clientwhrequestController',
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
						controller  : 'clientwhrequestController',
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
		
		// Show detail 
		$scope.showDetail = function(id){
			$scope.idrequest = id;
			$scope.getByIdForView(id);
			$scope.request.id = id;
			$scope.commentScope.idref = $scope.request.id;
			if($scope.request.id > -1) {
				$scope.getById($scope.request.id);
			}
			// requestsub.
			$scope.requestdetailScope.idref = $scope.request.id;
			// Attachment.
			$scope.attachmentScope.idref = $scope.request.id;
			$scope.showDialogDetail();
		}
		//Show detail store
		$scope.showDialogDetail = function () {
			var htmlUrlTemplate = clientwh.contextPath + '/view/request_view.html';
			clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function (evt) {
				console.log('closed');
			}, function (evt) {
				console.log('not closed');
			});
		}
		
		
		// Show a form screen.
		$scope.showForm = function(id) {
			$scope.initForm(id);
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.showDialog();
		}
		
		 $scope.showCopyTo = function(id) {
	    	    $scope.getById(id);
	    	    $scope.getAllByIdRequest(id);
	    	    $mdBottomSheet.show({
	    	      templateUrl: clientwh.contextPath + '/view/copyTo_form.html',
	    	      controller  : 'clientwhrequestController'
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
	    
	    // Show edit view.
	    $scope.showDialog = function () {
	    	$mdToast.hide();
	        var htmlUrlTemplate = clientwh.contextPath + '/view/request_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }

	    // Create new.
		$scope.createNew = function() {
			$scope.request = { id: -1 , scope: $scope.scope };
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
		}
		
		// Clear filter Store.
		$scope.clearFilterStore = function() {
			$scope.search.store = "";
			$scope.listWithCriteriasByPage(1);
		}
		
		// Reset validate.
		$scope.resetValidate = function() {
			$scope.ctlentityfieldStore.searchText = undefined;
			$scope.ctlentityfieldStore.selectedItem = undefined;
			$scope.ctlentityfieldWriter.searchText = undefined;
			$scope.ctlentityfieldWriter.selectedItem = undefined;
			$scope.ctlentityfieldReceiver.searchText = undefined;
			$scope.ctlentityfieldReceiver.selectedItem = undefined;
			$scope.ctlentityfieldResponsible.searchText = undefined;
			$scope.ctlentityfieldResponsible.selectedItem = undefined;
			// idstore.
		    $scope.frmRequest.idstore.$setPristine();
			$scope.frmRequest.idstore.$setUntouched();
			// idwriter.
		    $scope.frmRequest.idwriter.$setPristine();
			$scope.frmRequest.idwriter.$setUntouched();
			// idreceiver.
		    $scope.frmRequest.idreceiver.$setPristine();
			$scope.frmRequest.idreceiver.$setUntouched();
			// idresponsible.
		    $scope.frmRequest.idresponsible.$setPristine();
			$scope.frmRequest.idresponsible.$setUntouched();
			// code.
		    $scope.frmRequest.code.$setPristine();
			$scope.frmRequest.code.$setUntouched();
			
			// times.
		    $scope.frmRequest.times.$setPristine();
			$scope.frmRequest.times.$setUntouched();
			
			$scope.resetVadidateDatetimepicker();
			
			// requestdate.
		    $scope.frmRequest.requestdate.$setPristine();
			$scope.frmRequest.requestdate.$setUntouched();
			
			// receivedate.
		    $scope.frmRequest.receivedate.$setPristine();
			$scope.frmRequest.receivedate.$setUntouched();
			


		    // form.
			$scope.frmRequest.$setPristine();
			$scope.frmRequest.$setUntouched();
			
			$scope.ctlentityfieldStore.selectedItem = null;
			$scope.ctlentityfieldWriter.selectedItem = null;
			$scope.ctlentityfieldReceiver.selectedItem = null;
			$scope.ctlentityfieldResponsible.selectedItem = null;
			
			// frmDirty.
			$scope.frmDirty = false;
			$scope.frmRequest.idstore.$invalid = false;
			$scope.frmRequest.idwriter.$invalid = false;
			$scope.frmRequest.idreceiver.$invalid = false;
			$scope.frmRequest.idresponsible.$invalid = false;
			$scope.frmRequest.requestdate.$invalid = false;
			$scope.frmRequest.receivedate.$invalid = false;
			$scope.frmRequest.code.$invalid = false;
			$scope.frmRequest.times.$invalid = false;
			
			$scope.resetValidateDuplicate(0);
			
		}
		
		// Create on form.
		$scope.createOnForm = function() {
			$scope.createNew();
			$scope.resetValidate();
		}
		$scope.clearForm = function(){
			$scope.request = { id: -1 };
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.resetValidate();
		}
		//////////////////SHOW	////////////////////
		// Show all combo box.
		$scope.listAllForSelect = function () {
			var listAllSelectDeferred = $q.defer();
			// Init data for select.
			$scope.stores = [];
			$scope.writers = [];
			$scope.receivers = [];
			$scope.reponsibles = [];
			// Call service
			var listStore = storeService.listAllForSelect();
			var listWriter = userService.listAllForSelect();
			// Response.
			$q.all([listStore,listWriter]).then(
				// Successes.
				function (responses) {
					$scope.stores = responses[0].data;
					$scope.writers = responses[1].data;
					$scope.receivers = responses[1].data;
					$scope.reponsibles = responses[1].data;
					$scope.ctlentityfieldStore.items = $scope.stores;
					$scope.ctlentityfieldWriter.items = $scope.writers;
					$scope.ctlentityfieldReceiver.items = $scope.receivers;
					$scope.ctlentityfieldResponsible.items = $scope.reponsibles;
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
			for (var i = 0; i < $scope.ctlentityfieldStore.items.length; i++) {
				if ($scope.request.idstore == $scope.ctlentityfieldStore.items[i].value) {
					$scope.ctlentityfieldStore.selectedItem = {};
					$scope.ctlentityfieldStore.selectedItem = $scope.ctlentityfieldStore.items[i];
				}
			}
			for (var i = 0; i < $scope.ctlentityfieldWriter.items.length; i++) {
				if ($scope.request.idwriter == $scope.ctlentityfieldWriter.items[i].id) {
					$scope.ctlentityfieldWriter.selectedItem = {};
					$scope.ctlentityfieldWriter.selectedItem = $scope.ctlentityfieldWriter.items[i];
				}
			}
			for (var i = 0; i < $scope.ctlentityfieldReceiver.items.length; i++) {
				if ($scope.request.idreceiver == $scope.ctlentityfieldReceiver.items[i].id) {
					$scope.ctlentityfieldReceiver.selectedItem = {};
					$scope.ctlentityfieldReceiver.selectedItem = $scope.ctlentityfieldReceiver.items[i];
				}
			}
			for (var i = 0; i < $scope.ctlentityfieldResponsible.items.length; i++) {
				if ($scope.request.idresponsible == $scope.ctlentityfieldResponsible.items[i].id) {
					$scope.ctlentityfieldResponsible.selectedItem = {};
					$scope.ctlentityfieldResponsible.selectedItem = $scope.ctlentityfieldResponsible.items[i];
				}
			}
		}
		
		$scope.resetInvalidStore = function(){
			$scope.frmRequest.idstore.$invalid = true;
		}
		
		$scope.resetInvalidWriter = function(){
			$scope.frmRequest.idwriter.$invalid = true;
		}
		
		$scope.resetInvalidReceiver = function(){
			$scope.frmRequest.idreceiver.$invalid = true;
		}
		
		$scope.resetInvalidResponsible = function(){
			$scope.frmRequest.idresponsible.$invalid = true;
		}
		
		$scope.resetInvalidRequestDate = function(){
			$scope.frmRequest.requestdate.$invalid = true;
		}
		$scope.resetInvalidReceiveDate = function(){
			$scope.frmRequest.receivedate.$invalid = true;
		}
		// Save.
		$scope.save = function() {
			if($scope.ctlentityfieldStore.selectedItem==null){
				$scope.frmRequest.idstore.$invalid = true;
				$scope.frmRequest.idstore.$touched = true;
			}
			if($scope.ctlentityfieldWriter.selectedItem==null){
				$scope.frmRequest.idwriter.$invalid = true;
				$scope.frmRequest.idwriter.$touched = true;
			}
			if($scope.ctlentityfieldReceiver.selectedItem==null){
				$scope.frmRequest.idreceiver.$invalid = true;
				$scope.frmRequest.idreceiver.$touched = true;
			}
			if($scope.ctlentityfieldResponsible.selectedItem==null){
				$scope.frmRequest.idresponsible.$invalid = true;
				$scope.frmRequest.idresponsible.$touched = true;
			}
			if ($scope.request.requestdate==null) {
				$scope.frmRequest.requestdate.$invalid = true;
				$scope.frmRequest.requestdate.$touched = true;
			}
			if ($scope.request.receivedate==null) {
				$scope.frmRequest.receivedate.$invalid = true;
				$scope.frmRequest.receivedate.$touched = true;
			}
			if ($scope.request.code==null) {
				$scope.frmRequest.code.$invalid = true;
				$scope.frmRequest.code.$touched = true;
			}
			if($scope.frmRequest.$invalid||
					$scope.frmRequest.requestdate.$invalid||
					$scope.frmRequest.receivedate.$invalid||
					$scope.frmRequest.code.$invalid) {
				$scope.frmRequest.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			$scope.request.scope = $scope.scope;
			// Ignore time
			$scope.request.requestdate = clientmain.getDateIgnoreTime($scope.request.requestdate)
			$scope.request.receivedate = clientmain.getDateIgnoreTime($scope.request.receivedate)
			$scope.showMessageOnToast($translate.instant('clientwh_home_saving'));
			$scope.subtractTwoDate();
			if ($scope.subtractTwoDate()==false) {
				$scope.frmRequest.requestdate.$invalid = true;
				$scope.frmRequest.requestdate.$error.compare = true;
				$scope.frmRequest.requestdate.$valid = false;
				$scope.frmRequest.requestdate.$touched = true;
				
				$scope.frmRequest.receivedate.$invalid = true;
				$scope.frmRequest.receivedate.$valid = false;
				$scope.frmRequest.receivedate.$error.compare = true;
				$scope.frmRequest.receivedate.$touched = true;
				$scope.showMessageOnToast($translate.instant('clientwh_quotation_date'));
				return;
			}	
			
			var result;			
			$scope.request.name = $scope.request.name.trim();
			if($scope.request.description != null){
				$scope.request.description = $scope.request.description.trim();
				}
			if($scope.request.id > -1) {
				result = requestService.updateWithLock($scope.request.id, $scope.request);
			} else {
				result = requestService.create($scope.request);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.request.id > -1) {
						$scope.request.version = response.data;
					} else {
						$scope.request.id = response.data;
						$scope.attachmentScope.idref = $scope.request.id;
						$scope.request.version = 1;
					}
					$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
					// comment.
					$scope.commentScope.idref = $scope.request.id;
					$scope.listWithCriteriasByPage(1);
				} else {
					if(response.data.code == clientwh.serverCode.EXISTCODE) {
						$scope.frmRequest.code.$error.duplicate = true;
						$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
					} else if(response.data.code == clientwh.serverCode.VERSIONDIFFERENCE) {
						$scope.showMessageOnToast($translate.instant('clientwh_servercode_' + response.data.code));
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
		$scope.resetValidateDuplicate = function(number){
	    	switch (number) {
	    	case 1:
				if($scope.frmRequest.code.$error.duplicate){
					delete $scope.frmRequest.code.$error.duplicate;
					$scope.frmRequest.code.$invalid = false;
					$scope.frmRequest.code.$valid = true;
				}
				break;
			default:
				if($scope.frmRequest.code.$error.duplicate){
					delete $scope.frmRequest.code.$error.duplicate;
					$scope.frmRequest.code.$invalid = false;
					$scope.frmRequest.code.$valid = true;
				}
			}
		}
		
		$scope.resetVadidateDatetimepicker = function(){
			if ($scope.frmRequest.requestdate.$error.compare) {
				delete $scope.frmRequest.requestdate.$error.compare;
				$scope.frmRequest.requestdate.$invalid = false;
				$scope.frmRequest.requestdate.$valid = true;
			}
			if ($scope.frmRequest.receivedate.$error.compare) {
				delete $scope.frmRequest.receivedate.$error.compare;
				$scope.frmRequest.receivedate.$invalid = false;
				$scope.frmRequest.receivedate.$valid = true;
			}
		}
		// Delete.
		$scope.delete = function(id, version) {
				requestService.updateForDeleteWithLock(id, version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToastList($translate.instant('clientwh_home_deleted'));
						$scope.listWithCriteriasByPage(1);
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
				requestService.updateForDeleteWithLock($scope.request.id, $scope.request.version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessageOnToast($translate.instant('clientwh_home_deleted'));
						$scope.createNew();
						$scope.resetValidate();
						$scope.listWithCriteriasByPage(1);
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
			requestService.getById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.request = data;
					$scope.request.requestdate = new Date($scope.request.requestdate);
					$scope.request.receivedate = new Date($scope.request.receivedate);
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
		
		// Get by Id for view.
		$scope.getByIdForView = function(id) {
			requestService.getByIdForView(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.requestView = data;
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
			//requestService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// workflowexecute module
			workflowexecuteService.listWithCriteriasByPage($state, $scope.workflowTab.id, $scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.requests = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.requests = result;
						for (var i = 0; i < $scope.requests.length; i++) {
							if ($scope.requests[i].requestdate!=null) {
								$scope.requests[i].requestdate = new Date($scope.requests[i].requestdate).toLocaleDateString();
							}
							if ($scope.requests[i].receivedate!=null) {
								$scope.requests[i].receivedate = new Date($scope.requests[i].receivedate).toLocaleDateString();
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
		
		// Clear filter.
		$scope.clearFilter = function() {
			$scope.search = {};
			$scope.listWithCriteriasByPage(1);
		}
		// Clear filter search.
		$scope.clearFilterSearch = function() {
			$scope.search.key = "";
			$scope.listWithCriteriasByPage(1);
		}
		/*Extend functions*/
		
		// Sort by.
		$scope.sortBy = function(keyName){
			$scope.sortKey = keyName;
			$scope.reverse = !$scope.reverse;
			// Reload data.
			$scope.listWithCriteriasByPage($scope.page.currentPage);
		}
		
		$scope.clearSortBy = function(){
			$scope.sortKey = '';
			$scope.sortName = null;
			// Reload data.
			$scope.listWithCriteriasByPage($scope.page.currentPage);
		}
		
		$scope.sortByName = clientwh.sortByNameRequest;
		
		
		
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
		    result.push({ key: 'scope', operation: 'like', value: $scope.scope, logic: 'and' });
		    if(typeof($scope.search.key) !== 'undefined' && $scope.search.key !== ''&&typeof($scope.search.store) !== 'undefined' && $scope.search.store !== ''&&$scope.search.store!=null){
		    	result.push({ key: 'idstore', operation: '=', value: $scope.search.store, logic: 'and' },{ key: 'code', operation: 'like', value: $scope.search.key, logic: 'or' });
		    }
		    else{
		    	// system
			    if(typeof($scope.search.store) !== 'undefined' && $scope.search.store !== ''&&$scope.search.store!=null){
			    	result.push({ key: 'idstore', operation: '=', value: $scope.search.store, logic: 'or' },);
			    }
			   
			    // username.
			    if(typeof($scope.search.key) !== 'undefined' && $scope.search.key !== '' && $scope.scope == $state.params.scope){
			    	result.push({ key: 'code', operation: 'like', value: $scope.search.key, logic: 'or' },
			    				{ key: 'name', operation: 'like', value: $scope.search.key, logic: 'or' },
				    			{ key: 'writer.username', operation: 'like', value: $scope.search.key, logic: 'or' },
				    			{ key: 'receiver.username', operation: 'like', value: $scope.search.key, logic: 'or' },
				    			{ key: 'responsible.username', operation: 'like', value: $scope.search.key, logic: 'or' },
			    				{ key: 'store.name', operation: 'like', value: $scope.search.key, logic: 'or' });
			    }
		    }
		    // return.
		    return result;
		}
		
		////////////////////////////////////////
		// Auto complete: idstore
		////////////////////////////////////////
		$scope.ctlentityfieldStore = {};
		$scope.ctlentityfieldStore.isCallServer = false;
		$scope.ctlentityfieldStore.isDisabled = false;

		// New.
		$scope.ctlentityfieldStore.newState = function (item) {
			alert("Sorry! You'll need to create a Constitution for " + item + " first!");
		}
		// Search in array.
		$scope.ctlentityfieldStore.querySearch = function (query) {
			var results = query ? $scope.ctlentityfieldStore.items.filter($scope.ctlentityfieldStore.createFilterFor(query)) : $scope.ctlentityfieldStore.items,
				deferred;

			if ($scope.ctlentityfieldStore.isCallServer) {
				deferred = $q.defer();
				$timeout(function () { deferred.resolve(results); }, Math.random() * 1000, false);
				return deferred.promise;
			} else {
				return results;
			}
		}
		// Filter.
		$scope.ctlentityfieldStore.createFilterFor = function (query) {
			var lowercaseQuery = angular.lowercase(query);
			return function filterFn(item) {
				return (angular.lowercase(item.display).indexOf(lowercaseQuery) >= 0);
			};
		}
		// Text change.
		$scope.ctlentityfieldStore.searchTextChange = function (text) {
			$log.info('Text changed to ' + text);
		}
		// Item change.
		$scope.ctlentityfieldStore.selectedItemChange = function (item) {
			if (typeof(frmRequest) === 'undefined' || frmRequest.idstore === undefined) {
				return;
			}
			$scope.request.idstore = undefined;
			frmRequest.idstore.$invalid = true;
			if (item) {
				$scope.request.idstore = item.value;
				frmRequest.idstore.$invalid = false;
			}
		}
		
		////////////////////////////////////////
		// Auto complete: idwriter
		////////////////////////////////////////
		$scope.ctlentityfieldWriter = {};
		$scope.ctlentityfieldWriter.isCallServer = false;
		$scope.ctlentityfieldWriter.isDisabled = false;

		// New.
		$scope.ctlentityfieldWriter.newState = function (item) {
			alert("Sorry! You'll need to create a Constitution for " + item + " first!");
		}
		// Search in array.
		$scope.ctlentityfieldWriter.querySearch = function (query) {
			var results = query ? $scope.ctlentityfieldWriter.items.filter($scope.ctlentityfieldWriter.createFilterFor(query)) : $scope.ctlentityfieldWriter.items,
				deferred;

			if ($scope.ctlentityfieldWriter.isCallServer) {
				deferred = $q.defer();
				$timeout(function () { deferred.resolve(results); }, Math.random() * 1000, false);
				return deferred.promise;
			} else {
				return results;
			}
		}
		// Filter.
		$scope.ctlentityfieldWriter.createFilterFor = function (query) {
			var lowercaseQuery = angular.lowercase(query);
			return function filterFn(item) {
				return (angular.lowercase(item.display).indexOf(lowercaseQuery) >= 0);
			};
		}
		// Text change.
		$scope.ctlentityfieldWriter.searchTextChange = function (text) {
			$log.info('Text changed to ' + text);
		}
		// Item change.
		$scope.ctlentityfieldWriter.selectedItemChange = function (item) {
			if (typeof(frmRequest) === 'undefined' || frmRequest.idwriter === undefined) {
				return;
			}
			$scope.request.idwriter = undefined;
			frmRequest.idwriter.$invalid = true;
			if (item) {
				$scope.request.idwriter = item.id;
				frmRequest.idwriter.$invalid = false;
			}
		}
		
		////////////////////////////////////////
		// Auto complete: idreceiver
		////////////////////////////////////////
		$scope.ctlentityfieldReceiver = {};
		$scope.ctlentityfieldReceiver.isCallServer = false;
		$scope.ctlentityfieldReceiver.isDisabled = false;

		// New.
		$scope.ctlentityfieldReceiver.newState = function (item) {
			alert("Sorry! You'll need to create a Constitution for " + item + " first!");
		}
		// Search in array.
		$scope.ctlentityfieldReceiver.querySearch = function (query) {
			var results = query ? $scope.ctlentityfieldReceiver.items.filter($scope.ctlentityfieldReceiver.createFilterFor(query)) : $scope.ctlentityfieldReceiver.items,
				deferred;

			if ($scope.ctlentityfieldReceiver.isCallServer) {
				deferred = $q.defer();
				$timeout(function () { deferred.resolve(results); }, Math.random() * 1000, false);
				return deferred.promise;
			} else {
				return results;
			}
		}
		// Filter.
		$scope.ctlentityfieldReceiver.createFilterFor = function (query) {
			var lowercaseQuery = angular.lowercase(query);
			return function filterFn(item) {
				return (angular.lowercase(item.display).indexOf(lowercaseQuery) >= 0);
			};
		}
		// Text change.
		$scope.ctlentityfieldReceiver.searchTextChange = function (text) {
			$log.info('Text changed to ' + text);
		}
		// Item change.
		$scope.ctlentityfieldReceiver.selectedItemChange = function (item) {
			if (typeof(frmRequest) === 'undefined' || frmRequest.idreceiver === undefined) {
				return;
			}
			$scope.request.idreceiver = undefined;
			frmRequest.idreceiver.$invalid = true;
			if (item) {
				$scope.request.idreceiver = item.id;
				frmRequest.idreceiver.$invalid = false;
			}
		}
		
		////////////////////////////////////////
		// Auto complete: idresponsible
		////////////////////////////////////////
		$scope.ctlentityfieldResponsible = {};
		$scope.ctlentityfieldResponsible.isCallServer = false;
		$scope.ctlentityfieldResponsible.isDisabled = false;

		// New.
		$scope.ctlentityfieldResponsible.newState = function (item) {
			alert("Sorry! You'll need to create a Constitution for " + item + " first!");
		}
		// Search in array.
		$scope.ctlentityfieldResponsible.querySearch = function (query) {
			var results = query ? $scope.ctlentityfieldResponsible.items.filter($scope.ctlentityfieldResponsible.createFilterFor(query)) : $scope.ctlentityfieldResponsible.items,
				deferred;

			if ($scope.ctlentityfieldResponsible.isCallServer) {
				deferred = $q.defer();
				$timeout(function () { deferred.resolve(results); }, Math.random() * 1000, false);
				return deferred.promise;
			} else {
				return results;
			}
		}
		// Filter.
		$scope.ctlentityfieldResponsible.createFilterFor = function (query) {
			var lowercaseQuery = angular.lowercase(query);
			return function filterFn(item) {
				return (angular.lowercase(item.display).indexOf(lowercaseQuery) >= 0);
			};
		}
		// Text change.
		$scope.ctlentityfieldResponsible.searchTextChange = function (text) {
			$log.info('Text changed to ' + text);
		}
		// Item change.
		$scope.ctlentityfieldResponsible.selectedItemChange = function (item) {
			if (typeof(frmRequest) === 'undefined' || frmRequest.idresponsible === undefined) {
				return;
			}
			$scope.request.idresponsible = undefined;
			frmRequest.idresponsible.$invalid = true;
			if (item) {
				$scope.request.idresponsible = item.id;
				frmRequest.idresponsible.$invalid = false;
			}
		}
	
		$scope.getAllByIdRequest = function(idrequest){
    		requestdetailService.getAllById(idrequest).then(function(response){
    			$scope.requestdetails = response.data;
    		},function(response){
    			
    		})
    	}
    	
		// Subtract 2 dates.
		$scope.subtractTwoDate = function() {
			var startdate = moment($scope.request.requestdate, 'dd/MM/yyyy');
			var enddate = moment($scope.request.receivedate, 'dd/MM/yyyy');
			var duration = enddate.diff(startdate, 'days');
			//alert(duration);
			if (duration <= 0) {
				return false;
			}
			else {
				return true;
			}
		}
		
    	$scope.copyTo = function(item){
    		var reftype = 'request'+ $scope.request.scope;
    		if (item=='materialformprematerial') {
    			$state.go(clientwh.prefix + 'materialform', {scope:'prematerial',reftype: reftype, formcopy: $scope.request, formdetailcopy: $scope.requestdetails});
			}
    		else if (item=='materialformtechmaterial') {
    			$state.go(clientwh.prefix + 'materialform', {scope:'techmaterial',reftype: reftype, formcopy: $scope.request, formdetailcopy: $scope.requestdetails});
			}
    		else if(item=='requestnormal'){
    			$state.go(clientwh.prefix + 'request', {scope:'normal',reftype: reftype, formcopy: $scope.request, formdetailcopy: $scope.requestdetails});
    		}
    		else if(item=='requestextra'){
    			$state.go(clientwh.prefix + 'request', {scope:'extra',reftype: reftype, formcopy: $scope.request, formdetailcopy: $scope.requestdetails});
    		}
    		else{
    			$state.go(clientwh.prefix + item, {reftype: reftype, formcopy: $scope.request, formdetailcopy: $scope.requestdetails});
    		}
	    }
    	
    	$scope.formcopy = {};
		$scope.formdetailcopy = {};
	    if($stateParams.formcopy&& typeof $scope.workflowTab !== "undefined"&&$scope.workflowTab.id==="created"){
		    $scope.formcopy = $stateParams.formcopy;
		    $scope.formdetailcopy = $stateParams.formdetailcopy;
		    
		    $scope.request.idref = $stateParams.formcopy.id;
		    $scope.request.reftype = $stateParams.reftype;
		    
		    $scope.request.id = -1;
		    $scope.request.idstore = $stateParams.formcopy.idstore;
		    $scope.request.idwriter = $stateParams.formcopy.idwriter;
		    $scope.request.idreceiver = $stateParams.formcopy.idreceiver;
		    $scope.request.idresponsible = $stateParams.formcopy.idresponsible;
		    $scope.request.code = $stateParams.formcopy.code;
		    $scope.request.name = $stateParams.formcopy.name;
		    $scope.request.receiverphonenumber = $stateParams.formcopy.receiverphonenumber; 
		    $scope.request.writerphonenumber = $stateParams.formcopy.writerphonenumber;
		    $scope.request.address = $stateParams.formcopy.address;
		    $scope.request.times = $stateParams.formcopy.times;
		    $scope.request.requestdate = $stateParams.formcopy.requestdate;
		    $scope.request.receivedate = $stateParams.formcopy.receivedate;
		    $scope.request.note = $stateParams.formcopy.note;
		   
		    
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
