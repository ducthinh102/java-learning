
/**
 * Controller for Workflowexecute
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'workflowexecuteController', ['$scope', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader','$q', 'store', clientwh.prefix + 'workflowexecuteService',
		function($scope, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, $q, store, workflowexecuteService) {
		if(typeof(clientwh.translate.workflowexecute) === 'undefined' || clientwh.translate.workflowexecute.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.workflowexecute) === 'undefined') {
				clientwh.translate.workflowexecute = '';
			}
			clientwh.translate.workflowexecute += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/workflowexecute');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_workflowexecute_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
			// Exit view-only mode
			document.querySelector('body').classList.remove('view-only');
		    unRegister();
		});
		
	    $translate.onReady().then(function() {
	    		$scope.title = $translate.instant('clientwh_workflowexecute_title');
	    		$translate.refresh();
	    });
	    
	    // Show/hide button variables
	    $scope.showSendButton = $scope.showApproveButton
	    = $scope.showCancelButton = $scope.showGetBackButton
		= $scope.showSendBackButton = showNoteAttach
		= $scope.showAssign
	    = false;
	    
		// Get idWorkflow from $state
		var idWorkflow = workflowexecuteService.getIdWorkflowFromState($state);
		if(!idWorkflow)
			return;
		
		// Get workflow variable name from $state
		var workflowVariableName = workflowexecuteService.getWorkflowVariableNameFromState($state);
		if(!workflowVariableName)
			return;
		
		// Get current login user id
		var currentUserId;
		var userinfoStore = store.get(clientwh.USER_INFO);
		if(userinfoStore.modules) {
			var serverwhStore = userinfoStore.modules.find(i => i.name == 'serverwh');
			if(serverwhStore)
				currentUserId = serverwhStore.iduser;
		}

		// Get workflow item id from parent scope
		$scope.$watch('$parent.' + workflowVariableName + '.id', function () {
			if(!( ($scope.$parent[workflowVariableName].id > -1) && ($scope.$parent[workflowVariableName].id != $scope.workflowIdref) ))
				return;
			$scope.workflowIdref = $scope.$parent[workflowVariableName].id;
			
			$scope.checkButtonsVisibility();
		});
		
		// Switch form to view-only mode
		if($scope.$parent.workflowTab.id == 'deleted')
			document.querySelector('body').classList.add('view-only');
		else
			document.querySelector('body').classList.remove('view-only');
		
		// Execute workflow
		$scope.executeCommand = function(command) {
			var result = workflowexecuteService.executeCommand(command, idWorkflow, $scope.workflowIdref)
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if(command == $scope.constants.workflowStatuses.RECEIVE) {
						$scope.checkButtonsVisibility();
						$scope.$parent.listWithCriteriasByPage(1);
						console.log('Item status is updated to RECEIVED.');
					}
					else {
						console.log($translate.instant('clientwh_workflowexecute_success'));
						$scope.$parent.listWithCriteriasByPage(1);
						alert($translate.instant('clientwh_workflowexecute_success'));
						$scope.$parent.closeDialog();
					}
				} else {
					if(command == $scope.constants.workflowStatuses.RECEIVE)
						console.log($translate.instant('clientwh_workflowexecute_receive_error'));
					else
						alert($translate.instant('clientwh_workflowexecute_fail'));
				}
			},
			// error.
			function(response) {
				if(command == $scope.constants.workflowStatuses.RECEIVE)
					console.log($translate.instant('clientwh_workflowexecute_receive_error'));
				else
					alert($translate.instant('clientwh_workflowexecute_error'));
			});
		}

		// Assign workflow
		$scope.assign = function() {
			var result = workflowexecuteService.assign($scope.selectedAssignee.id, idWorkflow, $scope.workflowIdref)
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.$parent.listWithCriteriasByPage(1);
					alert($translate.instant('clientwh_workflowexecute_success'));
					$scope.$parent.closeDialog();
				} else {
					alert($translate.instant('clientwh_workflowexecute_fail'));
				}
			},
			// error.
			function(response) {
				alert($translate.instant('clientwh_workflowexecute_error'));
			});
		}
		
		// Check buttons visibility
		$scope.checkButtonsVisibility = function() {

			// Assinged tab
			if($scope.$parent.workflowTab.id == 'assigned') {
				$scope.showApproveButton = true;
			    $scope.showCancelButton = true;
				$scope.showSendBackButton = true;
				return;
			}
			// Deleted tab
			else if($scope.$parent.workflowTab.id == 'deleted')
				return;
			
			// Check buttons visibility
			workflowexecuteService.checkButtonVisibility(idWorkflow, $scope.workflowIdref)
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK && response.data) {
					var result = angular.fromJson(response.data);
					if(result) {
						$scope.showSendButton = result.send;
						$scope.showSendBackButton = result.sendback;
						$scope.showGetBackButton = result.getback;
						if(result.receive)
							$scope.executeCommand($scope.constants.workflowStatuses.RECEIVE);
						if(result.assigneeUsers && result.assigneeUsers.length > 0) {
							$scope.showAssign = true;
							$scope.assigneeList = result.assigneeUsers;
						}
					}
					else
						console.log('Cannot check buttons visibility !');
					//$scope.showNoteAttach = ($scope.showSendButton || $scope.showSendBackButton || $scope.showGetBackButton);
				}
			},
			// error.
			function(response) {
				console.log('Check has Send rules failed !');
			});
		}
		
	}]);

});
