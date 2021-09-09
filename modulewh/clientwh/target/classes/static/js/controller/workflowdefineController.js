
/**
 * Controller for Workflow Define
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'workflowdefineController', ['$scope', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', clientwh.prefix + 'workflowdefineService', clientwh.prefix + 'userService',
		function($scope, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, workflowdefineService, userService) {
		if(typeof(clientwh.translate.workflowdefine) === 'undefined' || clientwh.translate.workflowdefine.indexOf($translate.use()) < 0) {
			console.log(clientwh.translate.workflowdefine);
			if(typeof(clientwh.translate.workflowdefine) === 'undefined') {
				clientwh.translate.workflowdefine = '';
			}
			clientwh.translate.workflowdefine += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/workflowdefine');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
	    		console.log('clientwh_workflowdefine_title');
		    $scope.title = $translate.instant('clientwh_workflowdefine_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});
		
	    $translate.onReady().then(function() {
	    		console.log('user onReady');
	    		$scope.title = $translate.instant('clientwh_workflowdefine_title');
	    		$translate.refresh();
	    });
		
		
	    ////////////////////////////////////////////////////////////////////////////////////
		// Workflow define list
		//////////////////////////////////////////////////////////////////////////////////
		
	    // Search.
	    $scope.search = {};
	    
		// Paging.
		$scope.page = {
			pageSize: 3,
			totalElements: 0,
			currentPage: 0
		}

		// Load all username for Select User control
		$scope.getAllWorkflows = function() {
			workflowdefineService.getAllWorkflows()
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.workflows = angular.fromJson(response.data);
				}
			},
			// error.
			function(response) {
			});
		}
		$scope.getAllWorkflows();
		
		$scope.workflowdefine = {};
	    
		$scope.show = function() {
			alert($scope.title);
		}
	
		$scope.changeLanguage = function(language) {
			$translate.refresh();
			$translate.use(language);
			$translate.refresh();
			$translate.use(language);
			$translate.refresh();
		} 
		
		// Init for list.
		$scope.initList = function() {
			// Get permission.
			userService.definePermissionByTarget($scope, 'workflowdefine').then(
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
		}
		
		// List for page and filter.
		$scope.listWithCriteriasByPage = function(pageNo) {
			$scope.page.currentPage = pageNo;
			workflowdefineService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort())
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK || response.status === httpStatus.code.NoContent) {
					$scope.workflowdefines = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.workflowdefines = result;
						if(result.length > 0) {
							$scope.page.totalElements = response.data.totalElements;
						}
					}
				} else {
					$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
				}
			},
			// error.
			function(response) {
				$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
			});
		}
		
		// Clear filter.
		$scope.clearFilter = function() {
			$scope.search = {};
			$scope.listWithCriteriasByPage(1);
		}

		
		////////////////////////////////////////////////////////////////////////////////////
		// Workflow define form
		//////////////////////////////////////////////////////////////////////////////////
		
		// Init for form.
		$scope.initForm = function(id) {
			$scope.createNew();
			$scope.getUsernames();
			$scope.selectedWorkflow = undefined;
			$scope.transmits = [];
			$scope.workflowdefine.id = id;
			if($scope.workflowdefine.id > -1) {
				$scope.getById($scope.workflowdefine.id);
			}
			$scope.frmDirty = false;
			$scope.oldIdWorkflow = undefined;
			$scope.oldStep = undefined;
		}
		
		// Show a create screen.
		$scope.showCreate = function() {
			$scope.initForm(-1);
			$scope.showDialog();
		}
		
		// Show a form screen.
		$scope.showForm = function(id) {
			$scope.initForm(id);
			$scope.showDialog();
		}

	    // Show edit view.
	    $scope.showDialog = function () {
	        var htmlUrlTemplate = clientwh.contextPath + '/view/workflowdefine_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        		console.log('closed');
	        }, function(evt) {
	        		console.log('not closed');
	        });
	    }

	    // Create new.
		$scope.createNew = function() {
			$scope.workflowdefine = { id: -1 };
		}

		// Reset validate.
		$scope.resetValidate = function() {
			$scope.initForm(-1);

			$scope.frmWorkflowDefine.workflowname.$setPristine();
			$scope.frmWorkflowDefine.workflowname.$setUntouched();
			$scope.frmWorkflowDefine.code.$setPristine();
			$scope.frmWorkflowDefine.code.$setUntouched();
			$scope.frmWorkflowDefine.name.$setPristine();
			$scope.frmWorkflowDefine.name.$setUntouched();
			$scope.frmWorkflowDefine.step.$setPristine();
			$scope.frmWorkflowDefine.step.$setUntouched();
			$scope.frmWorkflowDefine.duration.$setPristine();
			$scope.frmWorkflowDefine.duration.$setUntouched();

			$scope.frmWorkflowDefine.step.$$element.val("");
			$scope.frmWorkflowDefine.step.isInvalid = false;
		}
		
		// Create on form.
		$scope.createOnForm = function() {
			$scope.createNew();
			$scope.resetValidate();
		}

		// Delete on form.
		$scope.deleteOnForm = function() {
			$scope.delete($scope.workflowdefine.id, $scope.workflowdefine.version);
		}

		// Reset transmit validate.
		$scope.resetTransmitValidate = function() {
			$scope.initTransmitDialog(-1);

			$scope.frmWorkflowDefineTransmit.sender.$setPristine();
			$scope.frmWorkflowDefineTransmit.sender.$setUntouched();
			$scope.frmWorkflowDefineTransmit.receiver.$setPristine();
			$scope.frmWorkflowDefineTransmit.receiver.$setUntouched();
			$scope.frmWorkflowDefineTransmit.approver.$setPristine();
			$scope.frmWorkflowDefineTransmit.approver.$setUntouched();
		}

		// Create transmit on form.
		$scope.createTransmitOnForm = function() {
			$scope.resetTransmitValidate();
		}

		// Delete transmit on form.
		$scope.deleteTransmitOnForm = function() {
			$scope.deleteTransmit($scope.transmit.id);
			$scope.closeDialog();
		}
		
		// Save.
		$scope.save = function() {
			if($scope.frmWorkflowDefine.$invalid || $scope.frmWorkflowDefine.step.isInvalid) {
				$scope.frmWorkflowDefine.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			$scope.showMessage('Saving!', 'alert-success', false);
			var result;
			$scope.workflowdefine.idworkflow = $scope.selectedWorkflow;
			if($scope.workflowdefine.id > -1) {
				result = workflowdefineService.update($scope.workflowdefine.id, $scope.workflowdefine);
			} else {
				result = workflowdefineService.create($scope.workflowdefine);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.Created) {
					$scope.workflowdefine.id = response.data;
					$scope.workflowdefine.version = 1;
					$scope.showMessage($translate.instant('clientwh_home_saved'), 'alert-success', true);
					$scope.listWithCriteriasByPage(1);
				}
				else if(response.status === httpStatus.code.OK) {
					$scope.workflowdefine = response.data;
					$scope.showMessage($translate.instant('clientwh_home_saved'), 'alert-success', true);
					$scope.listWithCriteriasByPage(1);
				}else {
					$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
				}
			},
			// error.
			function(response) {
				$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
			});
		}
		
		// Delete.
		$scope.delete = function(id, version){
			if($window.confirm($translate.instant('clientwh_workflowdefine_delete_confirm'))) {
				workflowdefineService.delete(id, version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK || response.status === httpStatus.code.NoContent) {
						$scope.listWithCriteriasByPage(1);
						$scope.closeDialog();
						console.log($translate.instant('clientwh_workflowdefine_deleted'));
					} else {
						alert($translate.instant('clientwh_home_fail'));
					}
				},
				// error.
				function(response) {
					alert($translate.instant('clientwh_home_error'));
				});
			}
		} 
		
		// Get by Id.
		$scope.getById = function(id) {
			workflowdefineService.getById(id)
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					var dataWorkflow= angular.fromJson(response.data.workflow);
					var dataWorkflowdefine = angular.fromJson(response.data.workflowdefine);
					dataWorkflowdefine.workflowname = dataWorkflow.name;
					$scope.workflowdefine = dataWorkflowdefine;
					$scope.selectedWorkflow = String($scope.workflowdefine.idworkflow);
					$scope.oldIdWorkflow = $scope.selectedWorkflow;
					$scope.oldStep = dataWorkflowdefine.step;
					$scope.transmits = angular.fromJson(dataWorkflowdefine.transmit);
				} else {
					$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
				}
			},
			// error.
			function(response) {
				$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
			});
		}
		
		/*Extend functions*/
		
		// Sort by.
		$scope.sortBy = function(keyName){
			$scope.sortKey = keyName;
			$scope.reverse = !$scope.reverse;
			// Reload data.
			$scope.listWithCriteriasByPage($scope.page.currentPage);
		}
		
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
		    // workflow id.
		    if(typeof($scope.search.workflowId) !== 'undefined' && $scope.search.workflowId !== '' && $scope.search.workflowId !== null){
		    	result.push({ key: 'idworkflow', operation: '=', value: $scope.search.workflowId, logic: 'or' });
		    }
		    // code.
		    if(typeof($scope.search.code) !== 'undefined' && $scope.search.code !== ''){
		    	result.push({ key: 'code', operation: 'like', value: $scope.search.code, logic: 'or' });
		    }
		    // name.
		    if(typeof($scope.search.name) !== 'undefined' && $scope.search.name !== ''){
		    	result.push({ key: 'name', operation: 'like', value: $scope.search.name, logic: 'or' });
		    }
		    // step.
		    if(typeof($scope.search.step) !== 'undefined' && $scope.search.step !== '' && $scope.search.step !== null){
		    	result.push({ key: 'step', operation: '=', value: $scope.search.step, logic: 'or' });
		    }
		    // return.
		    return result;
		}
			
		// Show message.
		$scope.showMessage = function(message, cssName, autoHide) {
			$scope.alertMessage = message;
			$('[name="alertMessage"]').addClass(cssName);
			$('[name="alertMessage"]').slideDown(500, function() {
				if(autoHide) {
					$window.setTimeout(function() {
						$('[name="alertMessage"]').slideUp(500, function() {
							$('[name="alertMessage"]').removeClass(cssName);
		            	});
					}, 1000);
				}
			});
		}
		

		// Check step with same workflow is duplicated
		$scope.isStepExisted = function() {
			
			// Workflow or step is not selected
			if(!$scope.selectedWorkflow || !$scope.workflowdefine.step) {
				$scope.frmWorkflowDefine.step.isInvalid = false;
				return;
			}
			
			// Workflow and step do not change
			if( ($scope.selectedWorkflow == $scope.oldIdWorkflow) && ($scope.workflowdefine.step == $scope.oldStep) ) {
				$scope.frmWorkflowDefine.step.isInvalid = false;
				return;
			}
			
			workflowdefineService.isStepExisted($scope.selectedWorkflow, $scope.workflowdefine.step)
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.frmWorkflowDefine.step.isInvalid = angular.fromJson(response.data);
				} else {
					$scope.frmWorkflowDefine.step.isInvalid = true;
				}
			},
			// error.
			function(response) {
				$scope.frmWorkflowDefine.step.isInvalid = true;
				console.log('Check step is existed function has error!');
			});
			
		}
		
		
		////////////////////////////////////////////////////////////////////////////////////
		// Transmit list form
		//////////////////////////////////////////////////////////////////////////////////
		
		$scope.transmits = angular.fromJson($scope.workflowdefine.transmit);
		
		
		////////////////////////////////////////////////////////////////////////////////////
		// Transmit item form
		//////////////////////////////////////////////////////////////////////////////////
		
		$scope.searchTerm;
		$scope.clearSearchTerm = function() {
			$scope.searchTerm = '';
		};
		$scope.searchKeydown = function(ev) {
			ev.stopPropagation();
		}

		// Init transmit dialog.
		$scope.initTransmitDialog = function(id) {
			$scope.transmit = {};
			$scope.tempTransmits = JSON.parse(JSON.stringify($scope.transmits));
			if(id > -1) {
				$scope.transmit = $scope.tempTransmits[id];
			}
			$scope.transmit.id = id;
			$scope.frmTransmitDirty = false;
		}
		
		// Show transmit dialog
		$scope.showTransmitDialog = function() {
	        var htmlUrlTransmitTemplate = clientwh.contextPath + '/view/workflowdefine_transmit_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTransmitTemplate).then(function(evt) {
	        		console.log('closed');
	        }, function(evt) {
	        		console.log('not closed');
	        });
		}
		
		// Add a transmit
		$scope.addTransmit = function() {
			$scope.initTransmitDialog(-1);
			$scope.showTransmitDialog();
		}

		// Edit a transmit
		$scope.editTransmit = function(id) {
			$scope.initTransmitDialog(id);
			$scope.showTransmitDialog();
		}
		
		// Delete a transmit
		$scope.deleteTransmit = function(id) {
			if($window.confirm($translate.instant('clientwh_workflowdefine_delete_confirm'))) {
				$scope.transmits.splice(id, 1);
				var str = angular.toJson($scope.transmits);
				$scope.workflowdefine.transmit = str;
				
				$scope.showMessageTransmit('Deleting!', 'alert-success', false);
				workflowdefineService.update($scope.workflowdefine.id, $scope.workflowdefine)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.workflowdefine = response.data;
						$scope.showMessageTransmit($translate.instant('clientwh_home_saved'), 'alert-success', true);
						$scope.listWithCriteriasByPage(1);
					} else {
						$scope.showMessageTransmit($translate.instant('clientwh_home_fail'), 'alert-danger', true);
					}
				},
				// error.
				function(response) {
					$scope.showMessageTransmit($translate.instant('clientwh_home_error'), 'alert-danger', true);
				});
			};
		}
	
		// Save a transmit item
		$scope.saveTransmit = function() {
			if($scope.frmWorkflowDefineTransmit.$invalid) {
				$scope.frmWorkflowDefineTransmit.$dirty = true;
				$scope.frmTransmitDirty = true;
				return;
			}
			
			if(!$scope.transmits)
				$scope.transmits = [];
			var index = $scope.transmit.id;
			if (index === null || index === undefined || index < 0){
				index = $scope.transmits.length;
			}
			var tempTransmit = JSON.parse(JSON.stringify($scope.transmit));
			delete tempTransmit.id;
			$scope.transmits[index] = tempTransmit;
			var str = angular.toJson($scope.transmits);
			$scope.workflowdefine.transmit = str;
			$scope.showMessageTransmit('Saving!', 'alert-success', false);
			var result;
			if($scope.workflowdefine.id > -1) {
				result = workflowdefineService.update($scope.workflowdefine.id, $scope.workflowdefine);
			} else {
				if($scope.workflowdefine.hasOwnProperty('id')) {
					delete $scope.workflowdefine.id;
				}
				if(!$scope.workflowdefine.hasOwnProperty('idworkflow')) {
					$scope.workflowdefine.idworkflow=1; // quotation
				}
				result = workflowdefineService.create($scope.workflowdefine);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.Created) {
					$scope.workflowdefine.id = response.data;
					$scope.workflowdefine.version = 1;
					$scope.transmit.id = 0;
					$scope.showMessageTransmit($translate.instant('clientwh_home_saved'), 'alert-success', true);
					$scope.listWithCriteriasByPage(1);
				}
				else if(response.status === httpStatus.code.OK) {
					$scope.workflowdefine = response.data;
					if($scope.transmit.id < 0)
						$scope.transmit.id = 0;
					$scope.showMessageTransmit($translate.instant('clientwh_home_saved'), 'alert-success', true);
					$scope.listWithCriteriasByPage(1);
				} else {
					$scope.showMessageTransmit($translate.instant('clientwh_home_fail'), 'alert-danger', true);
				}
			},
			// error.
			function(response) {
				$scope.showMessageTransmit($translate.instant('clientwh_home_error'), 'alert-danger', true);
			});
		}
		
		// Load all username for Select User control
		$scope.getUsernames = function() {
			userService.listAllForSelect()
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.users = angular.fromJson(response.data);
				} else {
					$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
				}
			},
			// error.
			function(response) {
				$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
			});
		}
		
		// Show message transmit dialog.
		$scope.showMessageTransmit = function(message, cssName, autoHide) {
			$scope.alertMessage = message;
			$('[name="alertMessageTransmit"]').addClass(cssName);
			$('[name="alertMessageTransmit"]').slideDown(500, function() {
				if(autoHide) {
					$window.setTimeout(function() {
						$('[name="alertMessageTransmit"]').slideUp(500, function() {
							$('[name="alertMessageTransmit"]').removeClass(cssName);
		            	});
					}, 1000);
				}
			});
		}
		
		// Get username by user id
		$scope.getUserName = function(id) {
			if(!$scope.users)
				return;
			var item = $scope.users.find(i=>i.id==id);
			if(item)
				return item.display;
		}
		
		// Get workflow name by workflow id
		$scope.getWorkflownameByWorkflowid = function(id) {
			if(!$scope.workflows)
				return;
			var item = $scope.workflows.find(i=>i.id==id);
			if(item)
				return item.name;
		}
		
		// Remove selected user if it is existed in another user list
		$scope.checkItemExisted = function(listName) {
			switch(listName) {
			case 'sender':
				$scope.removeDuplicateItemInAnotherList($scope.transmit.sender, $scope.transmit.receiver, 'clientwh_workflowdefine_item_existed_in_receiver');
				break;
				
			case 'receiver':
				$scope.removeDuplicateItemInAnotherList($scope.transmit.receiver, $scope.transmit.sender, 'clientwh_workflowdefine_item_existed_in_sender');
				break;
				
			case 'approver':
				$scope.removeDuplicateItemInAnotherList($scope.transmit.approver, $scope.transmit.receiver, 'clientwh_workflowdefine_item_existed_in_receiver');
				break;
				
			default:
				break;
			}
		};
		
		// Remove item of current list which is existed in another list and show message
		$scope.removeDuplicateItemInAnotherList = function(currentList, comparedList, removeMessageId) {
			if(!currentList || !comparedList)
				return false;
			var result = false;
			currentList.some(function (item1, index1) {
				const index2 = comparedList.indexOf(item1);
		        if(index2 >= 0) {
		        		alert("["+$scope.getUserName(item1)+"] " + $translate.instant(removeMessageId) );
		        		currentList.splice(index1, 1);
		        		result = true;
		        }
		    });
			return result;
		}
		
	}]);

});
