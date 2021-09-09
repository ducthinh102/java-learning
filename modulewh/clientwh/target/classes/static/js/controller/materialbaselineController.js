
/**
 * Controller for Materialbaseline
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'materialbaselineController', ['$scope', 'moment', '$stateParams','$mdBottomSheet', '$mdToast', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', '$q', clientwh.prefix + 'materialbaselineService', clientwh.prefix + 'userService', clientwh.prefix + 'storeService', clientwh.prefix + 'workflowexecuteService', clientwh.prefix + 'materialbaselinedetailService',// workflowexecute module
		function($scope, moment, $stateParams, $mdBottomSheet, $mdToast, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, $q, materialbaselineService, userService, storeService, workflowexecuteService,materialbaselinedetailService) { // workflowexecute module
		if(typeof(clientwh.translate.materialbaseline) === 'undefined' || clientwh.translate.materialbaseline.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.materialbaseline) === 'undefined') {
				clientwh.translate.materialbaseline = '';
			}
			clientwh.translate.materialbaseline += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/materialbaseline');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_materialbaseline_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
			$scope.closeToast();
		    unRegister();
		});	
		
	    $translate.onReady().then(function() {
	    		$scope.title = $translate.instant('clientwh_materialbaseline_title');
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
		$scope.newdate = function () {
			$scope.materialbaseline.baselinedate = new Date();
		}
		
		$scope.noSunday = function (date) {
			var day = date.getDay();
			return day === 1 || day === 2 || day === 3 || day === 4 || day === 5 || day === 6;
		}
		
		$scope.formatDate = function(dtime) {
			return moment(dtime).format('LL');
		}
		
		$scope.materialbaseline = {};
	    
	    // materialbaselinedetailView.
	    $scope.materialbaselinedetailView = clientwh.contextPath + "/view/materialbaselinedetail_list.html";
	    
	    // materialbaselinedetaillistView.
	    $scope.materialbaselinedetaillistView = clientwh.contextPath + "/view/materialbaselinedetaillist_view.html";
	    
	    // comment.
		$scope.commentScope = {
			view: clientwh.contextPath + '/view/comment_list.html',
			onlyview: clientwh.contextPath + '/view/comment_view.html',
			idref: $scope.materialbaseline.id,
			reftype: 'materialbaseline'
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
						controller  : 'clientwhmaterialbaselineController',
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
						controller  : 'clientwhmaterialbaselineController',
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
					userService.definePermissionByTarget($scope, 'materialbaseline').then(
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
			$scope.createNew();
			$scope.materialbaseline.id = id;
			if($scope.materialbaseline.id > -1) {
				$scope.getById($scope.materialbaseline.id);
			}
			
			// comment.
			$scope.commentScope.idref = $scope.materialbaseline.id;
			
			$scope.frmDirty = false;
		}
		
		// Show detail 
		$scope.showDetail = function(id) {
			$scope.materialbaseline.id = id;
			if($scope.materialbaseline.id > -1) {
				//comment.
				$scope.commentScope.idref = $scope.materialbaseline.id;
				$scope.getById($scope.materialbaseline.id);
			}
			$scope.showDialogDetail();
		}
		
		//Show detail materialbaseline
		$scope.showDialogDetail = function () {
			var htmlUrlTemplate = clientwh.contextPath + '/view/materialbaseline_view.html';
			clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function (evt) {
				console.log('closed');
			}, function (evt) {
				console.log('not closed');
			});
		}
	    
		$scope.showCopyTo = function(id) {
    	    $scope.getById(id);
    	    $scope.getAllByMaterialbaseline(id);
    	    $mdBottomSheet.show({
    	      templateUrl: clientwh.contextPath + '/view/copyTo_form.html',
    	      controller  : 'clientwhmaterialbaselineController'
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
    
		// Show a create screen.
		$scope.showCreate = function() {
			$scope.initForm(-1);
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.showDialog();
			
			$scope.ctlentityfieldStore.selectedItem = null;
			$scope.ctlentityfieldStore.searchText = null;
		}
		
		// Show a form screen.
		$scope.showForm = function(id) {
			$scope.initForm(id);
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.showDialog();
			
			$scope.ctlentityfieldStore.selectedItem = null;
			$scope.ctlentityfieldStore.searchText = null;
		}

	    // Show edit view.
	    $scope.showDialog = function () {
	        var htmlUrlTemplate = clientwh.contextPath + '/view/materialbaseline_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }
	    

	    // Create new.
		$scope.createNew = function() {
			$scope.materialbaseline = { id: -1 };
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			
	    	$scope.ctlentityfieldStore.searchText = null;
	    	
			$scope.ctlentityfieldStore.selectedItem = null;
		}
		
		// Clear filter Store.
		$scope.clearFilterStore = function() {
			$scope.search.store = "";
			$scope.listWithCriteriasByPage(1);
		}
		
		// Reset validate duplicate
	    $scope.resetValidateDuplicate = function() {
	    	delete $scope.frmMaterialbaseline.code.$error.duplicate;
			$scope.frmMaterialbaseline.code.$invalid = false;
			$scope.frmMaterialbaseline.code.$valid = true;
	    }
	    
		// Reset validate.
		$scope.resetValidate = function() {
			
			$scope.ctlentityfieldStore.searchText = undefined;
			$scope.ctlentityfieldStore.selectedItem = undefined;
			
			// idstore.
		    $scope.frmMaterialbaseline.idstore.$setPristine();
			$scope.frmMaterialbaseline.idstore.$setUntouched();
			// code.
		    $scope.frmMaterialbaseline.code.$setPristine();
			$scope.frmMaterialbaseline.code.$setUntouched();
			// name.
		    $scope.frmMaterialbaseline.name.$setPristine();
			$scope.frmMaterialbaseline.name.$setUntouched();
			// scope.
		    $scope.frmMaterialbaseline.scope.$setPristine();
			$scope.frmMaterialbaseline.scope.$setUntouched();
			// totalamount.
		    $scope.frmMaterialbaseline.totalamount.$setPristine();
			$scope.frmMaterialbaseline.totalamount.$setUntouched();
			// baselinedate.
		    $scope.frmMaterialbaseline.baselinedate.$setPristine();
			$scope.frmMaterialbaseline.baselinedate.$setUntouched();
			// note.
		    $scope.frmMaterialbaseline.note.$setPristine();
			$scope.frmMaterialbaseline.note.$setUntouched();

		    // form.
			$scope.frmMaterialbaseline.$setPristine();
			$scope.frmMaterialbaseline.$setUntouched();
			// frmDirty.
			$scope.frmDirty = false;
			
			$scope.frmMaterialbaseline.idstore.$invalid = false;
			$scope.frmMaterialbaseline.code.$invalid = false;
			
			$scope.ctlentityfieldStore.searchText = null;
			
			$scope.ctlentityfieldStore.selectedItem = null;
		}
		
		// Create on form.
		$scope.createOnForm = function() {
			// Clear all field
			$scope.materialbaseline = { id: -1 };
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.resetValidate();
			$scope.newdate();
			
		}
		$scope.clearForm = function(){
			$scope.materialbaseline = { id: -1 };
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
			// Call service
			var listStore = storeService.listAllForSelect();
			// Response.
			$q.all([listStore]).then(
				// Successes.
				function (responses) {
					$scope.stores = responses[0].data;
					$scope.ctlentityfieldStore.items = $scope.stores;
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
				if ($scope.materialbaseline.idstore == $scope.ctlentityfieldStore.items[i].value) {
					$scope.ctlentityfieldStore.selectedItem = {};
					$scope.ctlentityfieldStore.selectedItem = $scope.ctlentityfieldStore.items[i];
				}
			}
		}
		
		$scope.resetInvalidStore = function(){
			$scope.frmMaterialbaseline.idstore.$invalid = true;
		}
		
		// Trim Whitespace
		$scope.trimwhitespace = function () {
			$scope.materialbaseline.code = $scope.materialbaseline.code.trim();
			$scope.materialbaseline.name = $scope.materialbaseline.name.trim();
		}
		
		// Save.
		$scope.save = function() {
			if($scope.ctlentityfieldStore.selectedItem==null){
				$scope.frmMaterialbaseline.idstore.$invalid = true;
				$scope.frmMaterialbaseline.idstore.$touched = true;
			}
			if($scope.materialbaseline.baselinedate==null){
				$scope.frmMaterialbaseline.baselinedate.$invalid = true;
				$scope.frmMaterialbaseline.baselinedate.$touched = true;
			}
			if ($scope.materialbaseline.code==null) {
				$scope.frmMaterialbaseline.code.$invalid = true;
				$scope.frmMaterialbaseline.code.$touched = true;
			}
			if($scope.frmMaterialbaseline.$invalid) {
				$scope.frmMaterialbaseline.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			// Trim Whitespace
			$scope.trimwhitespace();
			
			$scope.showMessageOnToast($translate.instant('clientwh_home_saving'));
			var result;
			if($scope.materialbaseline.id > -1) {
				result = materialbaselineService.updateWithLock($scope.materialbaseline.id, $scope.materialbaseline);
			} else {
				result = materialbaselineService.create($scope.materialbaseline);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.materialbaseline.id > -1) {
						$scope.materialbaseline.version = response.data;
					} else {
						$scope.materialbaseline.id = response.data;
						$scope.materialbaseline.version = 1;
					}

					// comment.
					$scope.commentScope.idref = $scope.materialbaseline.id;
					
					$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
					$scope.listWithCriteriasByPage(1);
				} else {
					if(response.data.code == clientwh.serverCode.EXISTCODE) {
						$scope.frmMaterialbaseline.code.$error.duplicate = true;
						$scope.showMessageOnToast($translate.instant('clientwh_materialbaseline_exit_code'));
/*					if(response.data.code == clientwh.serverCode.EXISTSCOPE) {
						$scope.frmMaterialbaseline.scope.$invalid = true;
						$scope.showMessage($translate.instant('clientwh_servercode_' + response.data.code), 'alert-danger', false);*/
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
		
		////////////////////////////////////////
		// Auto complete: loadItemsData
		////////////////////////////////////////
		$scope.loadItemsData = function (dataResponse) {
			var items = [];
			for (var i = 0; i < dataResponse.length; i++) {
				var item = { value: '', display: '', code: '' };
				item.value = dataResponse[i].id;
				item.display = dataResponse[i].name;
				item.code = dataResponse[i].code;
				items.push(item);
			}
			return items;
		}
		
		//Reset validate duplicate
	    $scope.resetValidateDuplicate = function(){
	    	delete $scope.frmMaterialbaseline.code.$error.duplicate;
	    }
	    
		// Delete.
		$scope.delete = function(id, version) {
				materialbaselineService.updateForDeleteWithLock(id, version)
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
				materialbaselineService.updateForDeleteWithLock($scope.materialbaseline.id, $scope.materialbaseline.version)
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
			materialbaselineService.getById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.materialbaseline = data;
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
		
		/*// Get by Id For View.
		$scope.getByIdForView = function(id) {
			materialimportService.getByIdForView(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.materialimportView = data;
				} else {
					$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
				}
			},
			// error.
			function(response) {
				$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
			});
		}*/
		
		// List for page and filter.
		$scope.listWithCriteriasByPage = function(pageNo) {
			$scope.page.currentPage = pageNo;
			//materialbaselineService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// workflowexecute module
			workflowexecuteService.listWithCriteriasByPage($state, $scope.workflowTab.id, $scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.materialbaselines = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.materialbaselines = result;
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
		
		$scope.sortByName = clientwh.sortByNameMaterialBaseline;
		
		
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
		    if(typeof($scope.search.key) !== 'undefined' && $scope.search.key !== ''&&typeof($scope.search.store) !== 'undefined' && $scope.search.store !== ''&&$scope.search.store!=null){
		    	result.push({ key: 'idstore', operation: '=', value: $scope.search.store, logic: 'and' }, { key: '', operation: 'and', value: '', logic: 'and' }, { key: 'code', operation: 'like', value: $scope.search.key, logic: 'or' }, { key: 'name', operation: 'like', value: $scope.search.key, logic: 'or' });
		    }
		    else {
		    	// system
			    if(typeof($scope.search.store) !== 'undefined' && $scope.search.store !== ''&&$scope.search.store!=null){
			    	result.push({ key: 'idstore', operation: '=', value: $scope.search.store, logic: 'or' },);
			    }
			    if(typeof($scope.search.key) !== 'undefined' && $scope.search.key !== ''){
			    	result.push({ key: 'code', operation: 'like', value: $scope.search.key, logic: 'or' },{ key: 'name', operation: 'like', value: $scope.search.key, logic: 'or' });
			    }
			    if(typeof($scope.search.key) !== 'undefined' && $scope.search.key !== '' && !isNaN($scope.search.key)) {
		    		result.push({ key: 'totalamount', operation: '=', value: $scope.search.key, logic: 'or' });
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
			if (typeof($scope.frmMaterialbaseline) === 'undefined' || typeof($scope.frmMaterialbaseline.idstore) === 'undefined') {
				return;
			}
			$scope.materialbaseline.idstore = undefined;
			$scope.frmMaterialbaseline.idstore.$invalid = true;
			if (item) {
				$scope.materialbaseline.idstore = item.value;
				$scope.frmMaterialbaseline.idstore.$invalid = false;
			}
		}
			
		// Show message.
		$scope.showMessage = function(message, cssName, autoHide) {
			$scope.materialbaselineAlertMessage = message;
			$('#materialbaselineAlertMessage').removeClass('alert-danger');
			$('#materialbaselineAlertMessage').removeClass('alert-success');
			$('#materialbaselineAlertMessage').addClass(cssName);
			$('#materialbaselineAlertMessage').slideDown(500, function() {
				if(autoHide) {
					$window.setTimeout(function() {
						$('#materialbaselineAlertMessage').slideUp(500, function() {
							$('#materialbaselineAlertMessage').removeClass(cssName);
		            	});
					}, 1000);
				}
			});
		}
		
		$scope.getAllByMaterialbaseline = function(idmaterialbaseline){
    		materialbaselinedetailService.getAllById(idmaterialbaseline).then(function(response){
    			$scope.materialbaselinedetails = response.data;
    		},function(response){
    			
    		})
    	}
 		
    	$scope.copyTo = function(item){
    		if (item=='materialformprematerial') {
    			$state.go(clientwh.prefix + 'materialform', {scope:'prematerial',reftype: 'materialbaseline', formcopy: $scope.materialbaseline, formdetailcopy: $scope.materialbaselinedetails});
			}
    		else if (item=='materialformtechmaterial') {
    			$state.go(clientwh.prefix + 'materialform', {scope:'techmaterial',reftype: 'materialbaseline', formcopy: $scope.materialbaseline, formdetailcopy: $scope.materialbaselinedetails});
			}
    		else if(item=='requestnormal'){
    			$state.go(clientwh.prefix + 'request', {scope:'normal',reftype: 'materialbaseline', formcopy: $scope.materialbaseline, formdetailcopy: $scope.materialbaselinedetails});
    		}
    		else if(item=='requestextra'){
    			$state.go(clientwh.prefix + 'request', {scope:'extra',reftype: 'materialbaseline', formcopy: $scope.materialbaseline, formdetailcopy: $scope.materialbaselinedetails});
    		}
    		else{
    			$state.go(clientwh.prefix + item, {reftype: 'materialbaseline', formcopy: $scope.materialbaseline, formdetailcopy: $scope.materialbaselinedetails});
    		}
	    }
    	
		$scope.formcopy = {};
		$scope.formdetailcopy = {};
	    if($stateParams.formcopy&& typeof $scope.workflowTab !== "undefined"&&$scope.workflowTab.id==="created"){
		    $scope.formcopy = $stateParams.formcopy;
		    $scope.formdetailcopy = $stateParams.formdetailcopy;
		    
		    $scope.materialbaseline.idref = $stateParams.formcopy.id;
		    $scope.materialbaseline.reftype = $stateParams.reftype;
		    
		    $scope.materialbaseline.id = -1;
		    $scope.materialbaseline.code = $stateParams.formcopy.code;
		    $scope.materialbaseline.name = $stateParams.formcopy.name;
		    $scope.materialbaseline.scope = $stateParams.formcopy.scope;
	    	$scope.materialbaseline.totalamount = $stateParams.formcopy.totalamount;
	    	$scope.materialbaseline.baselinedate = $stateParams.formcopy.baselinedate;
	    	$scope.materialbaseline.note = $stateParams.formcopy.note;
	    	$scope.materialbaseline.idstore = $stateParams.formcopy.idstore;
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
