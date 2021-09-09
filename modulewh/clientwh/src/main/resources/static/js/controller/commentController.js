
/**
 * Controller for Comment
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'commentController', ['moment', '$scope', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', 'store', clientwh.prefix + 'commentService',
		function(moment, $scope, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, store, commentService) {
		if(typeof(clientwh.translate.comment) === 'undefined' || clientwh.translate.comment.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.comment) === 'undefined') {
				clientwh.translate.comment = '';
			}
			clientwh.translate.comment += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/comment');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_comment_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    $translate.onReady().then(function() {
	    	$scope.title = $translate.instant('clientwh_comment_title');
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
		
		$scope.formatDate = function(dtime) {
			return moment(dtime).format('LLLL')
		}
		
		$scope.comment = {};
		$scope.input = {};
		
		// Init for list.
		$rootScope.idcommentController = $scope.$id;
		$scope.initList = function() {
			// watch parent.
			$scope.$watch(
					function(scope) {
						return scope.$parent ? scope.$parent.commentScope.idref : null;
					},
					function(newValue, oldValue) {
						if($scope.$parent.commentScope) {
							$scope.comments = [];
							$scope.page.totalElements = 0;
							$scope.idwriter = $scope.$parent.commentScope.idwriter;
							$scope.idref = $scope.$parent.commentScope.idref;
							$scope.reftype = $scope.$parent.commentScope.reftype;
							if($scope.idref > -1 && $rootScope.idcommentController == $scope.$id) {
								$scope.listWithCriteriasByIdrefAndReftypeAndPage($scope.idref, $scope.reftype, 1);
								$scope.comment = { id: -1, idref: $scope.idref, reftype: $scope.reftype };
							}
						}
					}
			);
		}
		
		// Init for form.
		$scope.initForm = function(id) {
			$scope.createNew();
			$scope.comment.id = id;
			if($scope.comment.id > -1) {
				$scope.getById($scope.comment.id);
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
			$scope.input = {};
			//$scope.showDialog();
		}

	    // Show edit view.
	    $scope.showDialog = function () {
	        var htmlUrlTemplate = clientwh.contextPath + '/view/comment_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }
	    
	    // Create new.
		$scope.createNew = function() {
			$scope.comment = { id: -1, idref: $scope.idref, reftype: $scope.reftype };
			$scope.input = {};
		}
		
		// Reset validate.
		$scope.resetValidate = function() {
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
			$scope.showMessage($translate.instant('clientwh_home_saving'), 'alert-success', false);
			var result;
			if($scope.comment.id > -1) {
				result = commentService.updateWithLock($scope.comment.id, $scope.comment);
			} else {
				$scope.comment.content = $scope.input.comment;
				result = commentService.create($scope.comment);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.comment.id > -1) {
						$scope.comment.version = response.data;
					} else {
						$scope.comment.id = response.data;
						$scope.comment.version = 1;
					}
					$scope.createOnForm();
					$scope.showMessage($translate.instant('clientwh_home_saved'), 'alert-success', true);
					$scope.listWithCriteriasByIdrefAndReftypeAndPage($scope.idref, $scope.reftype, 1);
					
				} else {
					if(response.data.code == clientwh.serverCode.EXISTSCOPE) {
						$scope.frmComment.scope.$invalid = true;
						$scope.showMessage($translate.instant('clientwh_servercode_' + response.data.code), 'alert-danger', false);
					} else if(response.data.code == clientwh.serverCode.VERSIONDIFFERENCE) {
						$scope.showMessage($translate.instant('clientwh_servercode_' + response.data.code), 'alert-danger', false);
					} else {
						$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
					}
				}
			},
			// error.
			function(response) {
				$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
			});
		}
		
		// Delete.
		$scope.delete = function(id, version) {
			if($window.confirm('Are you sure to delete?')) {
				commentService.updateForDeleteWithLock(id, version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessage('Deleted!', 'alert-success', true);
						$scope.listWithCriteriasByIdrefAndReftypeAndPage($scope.idref, $scope.reftype, 1);
					} else {
						$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
					}
				},
				// error.
				function(response) {
					$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
				});
			}
		}
		
		// Delete with create.
		$scope.deleteOnForm = function() {
			if($window.confirm('Are you sure to delete?')) {
				commentService.updateForDeleteWithLock($scope.comment.id, $scope.comment.version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessage('Deleted!', 'alert-success', true);
						$scope.createNew();
						$scope.resetValidate();
						$scope.listWithCriteriasByIdrefAndReftypeAndPage($scope.idref, $scope.reftype, 1);
					} else {
						$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
					}
				},
				// error.
				function(response) {
					$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
				});
			}
		}
		
		// Get by Id.
		$scope.getById = function(id) {
			commentService.getById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.comment = data;
				} else {
					$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
				}
			},
			// error.
			function(response) {
				$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
			});
		}
		
		// List for page and filter.
		$scope.listWithCriteriasByPage = function(pageNo) {
			$scope.page.currentPage = pageNo;
			commentService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.comments = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.comments = result;
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
		
		// List for page and filter.
		$scope.listWithCriteriasByIdrefAndReftypeAndPage = function(idref, reftype, pageNo) {
			$scope.page.currentPage = pageNo;
			commentService.listWithCriteriasByIdrefAndReftypeAndPage(idref, reftype, $scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.comments = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.comments = result;
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
		}

		// Clear filter search.
		$scope.clearFilterSearch = function() {
			$scope.search.all = "";
			$scope.listWithCriteriasByIdrefAndReftypeAndPage($scope.idref, $scope.reftype, 1);
		}
		
		$scope.clearSortBy = function(){
			$scope.sortKeyComment = '';
			$scope.sortByArr = {};
			// Reload data.
			$scope.listWithCriteriasByIdrefAndReftypeAndPage($scope.idref,$scope.reftype, $scope.page.currentPage);
		}
		
		$scope.sortByName = clientwh.sortByNameComment;
		
		$scope.sortByArr = {};
		
		// Sort by.
		$scope.sortBy = function(keyName){
			$scope.sortKeyComment = keyName;
			$scope.reverse = !$scope.reverse;
			// Reload data.
			$scope.listWithCriteriasByIdrefAndReftypeAndPage($scope.idref,$scope.reftype, $scope.page.currentPage);
		}
		
		// Get sort object.
		$scope.getSort = function() {
			var result = [];
			result.push('sort=updatedate,desc');
			// name.
		    if(typeof($scope.sortKeyComment) !== 'undefined' && $scope.sortKeyComment !== ''){
		    	result.push('sort= ' + $scope.sortKeyComment + ',' + $scope.reverse);
		    }
			// return.
			return result;
		}
		
		// Get search object.
		$scope.getSearch = function() {
		    var result = [];
		    if(typeof($scope.search.all) !== 'undefined' && $scope.search.all !== ''){
				// username.
		    	//result.push({ key: 'username', operation: 'like', value: $scope.search.all, logic: 'or' });
				// idref.
		    	//result.push({ key: 'idref', operation: 'like', value: $scope.search.content, logic: 'or' });
				// reftype.
		    	//result.push({ key: 'reftype', operation: 'like', value: $scope.search.content, logic: 'or' });
				// content.
		    	result.push({ key: 'content', operation: 'like', value: $scope.search.all, logic: 'or' });
		    }
		    // return.
		    return result;
		}
			
		// Show message.
		$scope.showMessage = function(message, cssName, autoHide) {
			$scope.alertMessage1 = message;
			$('#alertMessage1').removeClass('alert-danger');
			$('#alertMessage1').removeClass('alert-success');
			$('#alertMessage1').addClass(cssName);
			$('#alertMessage1').slideDown(500, function() {
				if(autoHide) {
					$window.setTimeout(function() {
						$('#alertMessage1').slideUp(500, function() {
							$('#alertMessage1').removeClass(cssName);
		            	});
					}, 1000);
				}
			});
		}
	
		
	}]);

});
