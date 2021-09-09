
/**
 * Controller for Calendarextend
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientmain.prefix + 'calendarextendController', ['$scope', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', clientmain.prefix + 'calendarextendService',
		function($scope, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, calendarextendService) {
		if(typeof(clientmain.translate.calendarextend) === 'undefined' || clientmain.translate.calendarextend.indexOf($translate.use()) < 0) {
			if(typeof(clientmain.translate.calendarextend) === 'undefined') {
				clientmain.translate.calendarextend = '';
			}
			clientmain.translate.calendarextend += $translate.use() + ';';
			$translatePartialLoader.addPart(clientmain.contextPath + '/js/common/message/calendarextend');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientmain_calendarextend_title');
		    if(!$rootScope.menuActiveTitle){
		    	$rootScope.menuActiveTitle = $translate.instant('clientmain_calendarextend_title');
		    }
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    $translate.onReady().then(function() {
	    	$translate.refresh();
	    });
		
	    // Search.
	    $scope.search = {};
	    
		// Paging.
		$scope.page = {
			pageSize: 3,
			totalElements: 0,
			currentPage: 0
		}
		
		$scope.calendarextend = { id: -1, idcalendar: $scope.idcalendar, iswork: false };
		
		// Init for list.
		$rootScope.idcalendarextendController = $scope.$id;
		$scope.initList = function() {
			// watch idcalendar.
			if($rootScope.watchidcalendar) {
				$rootScope.watchidcalendar();
			}
			$rootScope.watchidcalendar = $scope.$watch (
				function(scope) {
					return scope.$parent ? scope.$parent.calendar.id : null;
				},
				function(newValue, oldValue) {
					if($scope.$parent.calendar) {
						$scope.calendarextends = [];
						$scope.page.totalElements = 0;
						$scope.idcalendar = $scope.$parent.calendar.id;
						if($scope.idcalendar > -1 && $rootScope.idcalendarextendController == $scope.$id) {
							$scope.listWithCriteriasByIdcalendarAndPage($scope.idcalendar, 1);
						}
					}
				}
			);
		}
		
		// Init for form.
		$scope.initForm = function(id) {
			// set idcalendar.
			$scope.idcalendar = $scope.$parent ? $scope.$parent.calendar.id : -1;
			$scope.createNew();
			$scope.calendarextend.id = id;
			if($scope.calendarextend.id > -1) {
				$scope.getById($scope.calendarextend.id);
			}
			$scope.frmDirty = false;
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
	        var htmlUrlTemplate = clientmain.contextPath + '/view/calendarextend_form.html';
	        clientmain.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }

	    // Create new.
		$scope.createNew = function() {
			$scope.calendarextend = { id: -1, idcalendar: $scope.idcalendar, iswork: false };
		}
		
		// Reset validate.
		$scope.resetValidate = function() {
			// idcalendar.
		    $scope.frmCalendarextend.idcalendar.$setPristine();
			$scope.frmCalendarextend.idcalendar.$setUntouched();
			// code.
		    $scope.frmCalendarextend.code.$setPristine();
			$scope.frmCalendarextend.code.$setUntouched();
			// name.
		    $scope.frmCalendarextend.name.$setPristine();
			$scope.frmCalendarextend.name.$setUntouched();
			// iswork.
		    $scope.frmCalendarextend.iswork.$setPristine();
			$scope.frmCalendarextend.iswork.$setUntouched();
			// calendardate.
		    $scope.frmCalendarextend.calendardate.$setPristine();
			$scope.frmCalendarextend.calendardate.$setUntouched();
			// day.
		    $scope.frmCalendarextend.day.$setPristine();
			$scope.frmCalendarextend.day.$setUntouched();
			// week.
		    $scope.frmCalendarextend.week.$setPristine();
			$scope.frmCalendarextend.week.$setUntouched();
			// month.
		    $scope.frmCalendarextend.month.$setPristine();
			$scope.frmCalendarextend.month.$setUntouched();
			// year.
		    $scope.frmCalendarextend.year.$setPristine();
			$scope.frmCalendarextend.year.$setUntouched();
			// note.
		    $scope.frmCalendarextend.note.$setPristine();
			$scope.frmCalendarextend.note.$setUntouched();

		    // form.
			$scope.frmCalendarextend.$setPristine();
			$scope.frmCalendarextend.$setUntouched();
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
			if ($scope.calendarextend.calendardate==null) {
				$scope.frmCalendarextend.calendardate.$invalid = true;
				$scope.frmCalendarextend.calendardate.$touched = true;
			}
			if($scope.frmCalendarextend.$invalid) {
				$scope.frmCalendarextend.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			$scope.showMessage($translate.instant('clientmain_home_saving'), 'alert-success', false);
			var result;
			if($scope.calendarextend.id > -1) {
				result = calendarextendService.updateWithLock($scope.calendarextend.id, $scope.calendarextend);
			} else {
				result = calendarextendService.create($scope.calendarextend);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.calendarextend.id > -1) {
						$scope.calendarextend.version = response.data;
					} else {
						$scope.calendarextend.id = response.data;
						$scope.calendarextend.version = 1;
					}
					$scope.showMessage($translate.instant('clientmain_home_saved'), 'alert-success', true);
					$scope.listWithCriteriasByIdcalendarAndPage($scope.idcalendar, 1);
				} else {
					if(response.data.code == clientmain.serverCode.EXISTSCOPE) {
						$scope.frmCalendarextend.scope.$invalid = true;
						$scope.showMessage($translate.instant('clientmain_servercode_' + response.data.code), 'alert-danger', false);
					} else if(response.data.code == clientmain.serverCode.VERSIONDIFFERENCE) {
						$scope.showMessage($translate.instant('clientmain_servercode_' + response.data.code), 'alert-danger', false);
					} else {
						$scope.showMessage($translate.instant('clientmain_home_fail'), 'alert-danger', true);
					}
				}
			},
			// error.
			function(response) {
				$scope.showMessage($translate.instant('clientmain_home_error'), 'alert-danger', true);
			});
		}
		
		// Delete.
		$scope.delete = function(id, version) {
			if($window.confirm('Are you sure to delete?')) {
				calendarextendService.updateForDeleteWithLock(id, version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessage('Deleted!', 'alert-success', true);
						$scope.listWithCriteriasByIdcalendarAndPage($scope.idcalendar, 1);
					} else {
						$scope.showMessage($translate.instant('clientmain_home_fail'), 'alert-danger', true);
					}
				},
				// error.
				function(response) {
					$scope.showMessage($translate.instant('clientmain_home_error'), 'alert-danger', true);
				});
			}
		}
		
		// Delete with create.
		$scope.deleteOnForm = function() {
			if($window.confirm('Are you sure to delete?')) {
				calendarextendService.updateForDeleteWithLock($scope.calendarextend.id, $scope.calendarextend.version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessage('Deleted!', 'alert-success', true);
						$scope.createNew();
						$scope.resetValidate();
						$scope.listWithCriteriasByIdcalendarAndPage($scope.idcalendar, 1);
					} else {
						$scope.showMessage($translate.instant('clientmain_home_fail'), 'alert-danger', true);
					}
				},
				// error.
				function(response) {
					$scope.showMessage($translate.instant('clientmain_home_error'), 'alert-danger', true);
				});
			}
		}
		
		// Get by Id.
		$scope.getById = function(id) {
			calendarextendService.getById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.calendarextend = data;

					$scope.calendarextend.calendardate = new Date($scope.calendarextend.calendardate);
				} else {
					$scope.showMessage($translate.instant('clientmain_home_fail'), 'alert-danger', true);
				}
			},
			// error.
			function(response) {
				$scope.showMessage($translate.instant('clientmain_home_error'), 'alert-danger', true);
			});
		}
		
		// List for page and filter.
		$scope.listWithCriteriasByPage = function(pageNo) {
			$scope.page.currentPage = pageNo;
			calendarextendService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.calendarextends = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.calendarextends = result;
						if(result.length > 0) {
							$scope.page.totalElements = response.data.totalElements;
						}
					}
				} else {
					$scope.showMessage($translate.instant('clientmain_home_fail'), 'alert-danger', true);
				}
			},
			// error.
			function(response) {
				$scope.showMessage($translate.instant('clientmain_home_error'), 'alert-danger', true);
			});
		}
		
		// List for page and filter.
		$scope.listWithCriteriasByIdcalendarAndPage = function(idcalendar, pageNo) {
			$scope.page.currentPage = pageNo;
			calendarextendService.listWithCriteriasByIdcalendarAndPage(idcalendar, $scope.getSearch(), pageNo - 1, $scope.page.pageSize).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.calendarextends = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.calendarextends = result;
						if(result.length > 0) {
							$scope.page.totalElements = response.data.totalElements;
						}
					}
				} else {
					$scope.showMessage($translate.instant('clientmain_home_fail'), 'alert-danger', true);
				}
			},
			// error.
			function(response) {
				$scope.showMessage($translate.instant('clientmain_home_error'), 'alert-danger', true);
			});
		}
		
		// Clear filter.
		$scope.clearFilter = function() {
			$scope.search = {};
		}

		/*Extend functions*/
		
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
				// idcalendar.
		    	//result.push({ key: 'idcalendar', operation: 'like', value: $scope.search.content, logic: 'or' });
				// code.
		    	//result.push({ key: 'code', operation: 'like', value: $scope.search.content, logic: 'or' });
				// name.
		    	//result.push({ key: 'name', operation: 'like', value: $scope.search.content, logic: 'or' });
				// iswork.
		    	//result.push({ key: 'iswork', operation: 'like', value: $scope.search.content, logic: 'or' });
				// calendardate.
		    	//result.push({ key: 'calendardate', operation: 'like', value: $scope.search.content, logic: 'or' });
				// day.
		    	//result.push({ key: 'day', operation: 'like', value: $scope.search.content, logic: 'or' });
				// week.
		    	//result.push({ key: 'week', operation: 'like', value: $scope.search.content, logic: 'or' });
				// month.
		    	//result.push({ key: 'month', operation: 'like', value: $scope.search.content, logic: 'or' });
				// year.
		    	//result.push({ key: 'year', operation: 'like', value: $scope.search.content, logic: 'or' });
				// note.
		    	//result.push({ key: 'note', operation: 'like', value: $scope.search.content, logic: 'or' });
		    }
		    // return.
		    return result;
		}
			
		// Show message.
		$scope.showMessage = function(message, cssName, autoHide) {
			$scope.calendarextendAlertMessage = message;
			$('#calendarextendAlertMessage').removeClass('alert-danger');
			$('#calendarextendAlertMessage').removeClass('alert-success');
			$('#calendarextendAlertMessage').addClass(cssName);
			$('#calendarextendAlertMessage').slideDown(500, function() {
				if(autoHide) {
					$window.setTimeout(function() {
						$('#calendarextendAlertMessage').slideUp(500, function() {
							$('#calendarextendAlertMessage').removeClass(cssName);
		            	});
					}, 1000);
				}
			});
		}
	
	}]);

});
