
/**
 * Controller for Materialform
 **/
define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'materialformController', ['moment', '$q', '$scope', '$stateParams','$mdBottomSheet', '$mdToast', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', clientwh.prefix + 'materialformService', clientwh.prefix + 'userService',clientwh.prefix + 'workflowexecuteService', clientwh.prefix + 'materialformdetailService', // workflowexecute module
		function(moment, $q, $scope, $stateParams, $mdBottomSheet, $mdToast, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, materialformService, userService, workflowexecuteService, materialformdetailService) { // workflowexecute module
		if(typeof(clientwh.translate.materialform) === 'undefined' || clientwh.translate.materialform.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.materialform) === 'undefined') {
				clientwh.translate.materialform = '';
			}
			clientwh.translate.materialform += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/materialform');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_materialform_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
			$scope.closeToast();
		    unRegister();
		});		
		
	    $translate.onReady().then(function() {
	    		$scope.title = $translate.instant('clientwh_materialform_title');
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
		
		$scope.formatDate = function(dtime) {
			return moment(dtime).format('L')
		}
		
		$scope.newdate = function() {
			$scope.materialform.formdate = new Date();
		}
		
		// scope param.
		$scope.scope = undefined;
		if(typeof($state.params) !== 'undefined'){
			$scope.scope = $state.params.scope;
		}
		
		$scope.materialform = { id: -1, scope: $scope.scope };
	    
	    // materialformdetailView.
	    $scope.materialformdetailView = clientwh.contextPath + "/view/materialformdetail_list.html";
	    
	    // materialformdetailListView.
	    $scope.materialformdetailOnlyView = clientwh.contextPath + "/view/materialformdetail_list_view.html";
	    
	    // comment.
		$scope.commentScope = {
			onlyview: clientwh.contextPath + "/view/comment_view.html",
			view: clientwh.contextPath + '/view/comment_list.html',
			idref: $scope.materialform.id,
			reftype: $scope.scope
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
						controller  : 'clientwhmaterialformController',
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
						controller  : 'clientwhmaterialformController',
						position: 'right'}).then(function(response) {
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
		$scope.initList = function () {
			if (typeof (listAllSelectPromise) === 'undefined') {
				var listAllSelectDefered = $q.defer();
				listAllSelectPromise = listAllSelectDefered.promise;
				listAllSelectDefered.resolve([]);
			}
			listAllSelectPromise.then(
				// Success
				function (response) {
					// Get permission.
					userService.definePermissionByTarget($scope, 'materialform').then(
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
				// Error
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
					$scope.materialform.id = id;
					if($scope.materialform.id > -1) {
						$scope.commentScope.idref = $scope.materialform.id;
						$scope.getById($scope.materialform.id);
					}
					$scope.frmDirty = false;
				},
				// Error
				function(response) {
					
				}
			);
		}
		
		// Show a create screen.
		$scope.showCreate = function() {
			$scope.initForm(-1);
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.showFormDialog();
		}
		
		// Show detail 
		$scope.showView = function(id) {
			$scope.materialform.id = id;
			if($scope.materialform.id > -1) {
				$scope.commentScope.idref = $scope.materialform.id;
				$scope.getById($scope.materialform.id);
			}
			$scope.showViewDialog();
		}
		
		//Show detail materialform
		$scope.showViewDialog = function () {
			var htmlUrlTemplate = clientwh.contextPath + '/view/materialform_view.html';
			clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function (evt) {
				console.log('closed');
			}, function (evt) {
				console.log('not closed');
			});
		}
		
		$scope.showCopyTo = function(id) {
    	    $scope.getById(id);
    	    $scope.getAllByMaterialform(id);
    	    $mdBottomSheet.show({
    	      templateUrl: clientwh.contextPath + '/view/copyTo_form.html',
    	      controller  : 'clientwhmaterialformController'
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
    
		// Show a form screen.
		$scope.showForm = function(id) {
			$scope.initForm(id);
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.showFormDialog();
		}

	    // Show edit view.
	    $scope.showFormDialog = function () {
	        var htmlUrlTemplate = clientwh.contextPath + '/view/materialform_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }

	    // Create new.
		$scope.createNew = function() {
			$scope.materialform = { id: -1, scope: $scope.scope };
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
		}
		
		// Create on form.
		$scope.createOnForm = function() {
			// Clear all field
			$scope.materialform = { id: -1, scope: $scope.scope };
			$scope.formcopy = {};
			$scope.formdetailcopy = {};
			$scope.resetValidate();
		}
			
		// Reset validate.
		$scope.resetValidate = function() {
			
			// code.
		    $scope.frmMaterialform.code.$setPristine();
			$scope.frmMaterialform.code.$setUntouched();
			// form.
			$scope.frmMaterialform.$setPristine();
			$scope.frmMaterialform.$setUntouched();
			
			// frmDirty.
			$scope.frmDirty = false;
		}
		
		// Save.
		$scope.save = function() {
			if ($scope.materialform.formdate==null) {
				$scope.frmMaterialform.formdate.$invalid = true;
			}
			if ($scope.materialform.code==null) {
				$scope.frmMaterialform.code.$invalid = true;
			}
			if($scope.frmMaterialform.$invalid|| $scope.frmMaterialform.formdate.$invalid|| $scope.frmMaterialform.code.$invalid) {
				$scope.frmMaterialform.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			$scope.showMessageOnToast($translate.instant('clientwh_home_saving'));
			// Ignore time
			$scope.materialform.formdate = clientmain.getDateIgnoreTime(new Date($scope.materialform.formdate));
			// Ignore time
			$scope.materialform.formdate = clientmain.getDateIgnoreTime(new Date($scope.materialform.formdate));
			$scope.materialform.code = $scope.materialform.code.trim();
			$scope.materialform.name = $scope.materialform.name.trim();
			var result;
			if($scope.materialform.id > -1) {
				result = materialformService.updateWithLock($scope.materialform.id, $scope.materialform);
			} else {
				result = materialformService.create($scope.materialform);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.materialform.id > -1) {
						$scope.materialform.version = response.data;
					} else {
						$scope.materialform.id = response.data;
						$scope.materialform.version = 1;
					}
					// comment.
					$scope.commentScope.idref = $scope.materialform.id;
					
					$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
					$scope.listWithCriteriasByPage(1);
				} else {
					if(response.data.code == clientwh.serverCode.EXISTCODE) {
						$scope.frmMaterialform.code.$error.duplicate = true;
						$scope.showMessageOnToast($translate.instant('clientwh_materialform_existcode'));
					}
					else if(response.data.code == clientwh.serverCode.VERSIONDIFFERENCE) {
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
	    $scope.resetValidateDuplicate = function(){
	    	delete $scope.frmMaterialform.code.$error.duplicate;
	    	$scope.frmMaterialform.code.$invalid = false;
			$scope.frmMaterialform.code.$valid = true;
	    }
		
		// Delete.
		$scope.delete = function(id, version) {
			materialformService.updateForDeleteWithLock(id, version)
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.showMessageOnToastList($translate.instant('clientwh_home_deleted'));
					$scope.listWithCriteriasByPage(1);
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
			materialformService.updateForDeleteWithLock($scope.materialform.id, $scope.materialform.version)
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
			materialformService.getById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.materialform = data;
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
			//materialformService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// workflowexecute module
			workflowexecuteService.listWithCriteriasByPage($state, $scope.workflowTab.id, $scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.materialforms = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.materialforms = result;
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
		
		// Clear filter search.
		$scope.clearFilterSearch = function() {
			$scope.search.key = "";
			$scope.listWithCriteriasByPage(1);
		}
		
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
		
		$scope.sortByName = clientwh.sortByNameMaterialform;
			
		// Get sort object.
		$scope.getSort = function() {
			var result = [];
			// name.
		    if(typeof($scope.sortKey) !== 'undefined' && $scope.sortKey !== '') {
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
			// scope.
		 if(result.push({ key: 'scope', operation: '=', value: $scope.scope, logic: 'and' })) {
		    if(typeof($scope.search.key) !== 'undefined' && $scope.search.key !== ''){
		    	result.push({ key: 'scope', operation: '=', value: $scope.scope, logic: 'and' }, { key: '', operation: 'and', value: '', logic: 'and' }, { key: 'code', operation: 'like', value: $scope.search.key, logic: 'or' }, { key: 'name', operation: 'like', value: $scope.search.key, logic: 'or' });
		    }};
		    // return.
		    return result;
		}
		
		$scope.getAllByMaterialform = function(idmaterialform){
    		materialformdetailService.getAllById(idmaterialform).then(function(response){
    			$scope.materialformdetails = response.data;
    		},function(response){
    			
    		})
    	}
    	  
    	$scope.copyTo = function(item){
    		var reftype = 'material'+ $scope.materialform.scope;
    		if (item=='materialformprematerial') {
    			$state.go(clientwh.prefix + 'materialform', {scope:'prematerial',reftype: reftype, formcopy: $scope.materialform, formdetailcopy: $scope.materialformdetails});
			}
    		else if (item=='materialformtechmaterial') {
    			$state.go(clientwh.prefix + 'materialform', {scope:'techmaterial',reftype: reftype, formcopy: $scope.materialform, formdetailcopy: $scope.materialformdetails});
			}
    		else if(item=='requestnormal'){
    			$state.go(clientwh.prefix + 'request', {scope:'normal',reftype: reftype, formcopy: $scope.materialform, formdetailcopy: $scope.materialformdetails});
    		}
    		else if(item=='requestextra'){
    			$state.go(clientwh.prefix + 'request', {scope:'extra',reftype: reftype, formcopy: $scope.materialform, formdetailcopy: $scope.materialformdetails});
    		}
    		else{
    			$state.go(clientwh.prefix + item, {reftype: reftype, formcopy: $scope.materialform, formdetailcopy: $scope.materialformdetails});
    		}
	    }
		
		
		$scope.formcopy = {};
		$scope.formdetailcopy = {};
	    if($stateParams.formcopy&& typeof $scope.workflowTab !== "undefined"&&$scope.workflowTab.id==="created"){
		    $scope.formcopy = $stateParams.formcopy;
		    $scope.formdetailcopy = $stateParams.formdetailcopy;
		    
		    $scope.materialform.idref = $stateParams.formcopy.id;
		    $scope.materialform.reftype = $stateParams.reftype;
		    
		    $scope.materialform.id = -1;
		    $scope.materialform.code = $stateParams.formcopy.code;
		    $scope.materialform.name = $stateParams.formcopy.name;
	    	$scope.materialform.totalamount = $stateParams.formcopy.totalamount;
	    	$scope.materialform.formdate = $stateParams.formcopy.formdate;
	    	$scope.materialform.note = $stateParams.formcopy.note;
    	 if (typeof (listAllSelectPromise) === 'undefined') {
				var listAllSelectDefered = $q.defer();
				listAllSelectPromise = listAllSelectDefered.promise;
				listAllSelectDefered.resolve([]);
			}
		   
		    listAllSelectPromise.then(
		 	//Success
		 	function (response) {
		 		//$scope.itemSelected();
		 		$scope.showFormDialog();
		 	},
		 	//Error
		 	function (response) { }
		 	);
	    }
	}]);

});
