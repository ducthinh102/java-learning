
/**
 * Controller for Attachment
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientmain.prefix + 'attachmentController', ['$scope', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', clientmain.prefix + 'attachmentService',
		function($scope, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, attachmentService) {
		if(typeof(clientmain.translate.attachment) === 'undefined' || clientmain.translate.attachment.indexOf($translate.use()) < 0) {
			if(typeof(clientmain.translate.attachment) === 'undefined') {
				clientmain.translate.attachment = '';
			}
			clientmain.translate.attachment += $translate.use() + ';';
			$translatePartialLoader.addPart(clientmain.contextPath + '/js/common/message/attachment');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientmain_attachment_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    $translate.onReady().then(function() {
	    	$scope.title = $translate.instant('clientmain_attachment_title');
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
		
		$scope.attachment = {};
		
		// Init for list.
		$scope.initList = function() {
			$scope.listWithCriteriasByPage(1);
		}
		
		// Init for form.
		$scope.initForm = function(id) {
			$scope.createNew();
			$scope.attachment.id = id;
			if($scope.attachment.id > -1) {
				$scope.getById($scope.attachment.id);
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
	        var htmlUrlTemplate = clientmain.contextPath + '/view/attachment_form.html';
	        clientmain.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }

	    // Create new.
		$scope.createNew = function() {
			$scope.attachment = { id: -1 };
		}
		
		// Reset validate.
		$scope.resetValidate = function() {
			// idref.
		    $scope.frmAttachment.idref.$setPristine();
			$scope.frmAttachment.idref.$setUntouched();
			// reftype.
		    $scope.frmAttachment.reftype.$setPristine();
			$scope.frmAttachment.reftype.$setUntouched();
			// filename.
		    $scope.frmAttachment.filename.$setPristine();
			$scope.frmAttachment.filename.$setUntouched();
			// filesize.
		    $scope.frmAttachment.filesize.$setPristine();
			$scope.frmAttachment.filesize.$setUntouched();
			// mimetype.
		    $scope.frmAttachment.mimetype.$setPristine();
			$scope.frmAttachment.mimetype.$setUntouched();
			// description.
		    $scope.frmAttachment.description.$setPristine();
			$scope.frmAttachment.description.$setUntouched();
			// filepath.
		    $scope.frmAttachment.filepath.$setPristine();
			$scope.frmAttachment.filepath.$setUntouched();
			// filetype.
		    $scope.frmAttachment.filetype.$setPristine();
			$scope.frmAttachment.filetype.$setUntouched();
			// note.
		    $scope.frmAttachment.note.$setPristine();
			$scope.frmAttachment.note.$setUntouched();

		    // form.
			$scope.frmAttachment.$setPristine();
			$scope.frmAttachment.$setUntouched();
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
			if($scope.frmAttachment.$invalid) {
				$scope.frmAttachment.$dirty = true;
				$scope.frmDirty = true;
				return;
			}
			$scope.showMessage($translate.instant('clientmain_home_saving'), 'alert-success', false);
			var result;
			if($scope.attachment.id > -1) {
				result = attachmentService.updateWithLock($scope.attachment.id, $scope.attachment);
			} else {
				result = attachmentService.create($scope.attachment);
			}
			result
			// success.
			.then(function(response) {
				if(response.status === httpStatus.code.OK) {
					if($scope.attachment.id > -1) {
						$scope.attachment.version = response.data;
					} else {
						$scope.attachment.id = response.data;
						$scope.attachment.version = 1;
					}
					$scope.showMessage($translate.instant('clientmain_home_saved'), 'alert-success', true);
					$scope.listWithCriteriasByPage(1);
				} else {
					if(response.data.code == clientmain.serverCode.EXISTSCOPE) {
						$scope.frmAttachment.scope.$invalid = true;
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
				attachmentService.updateForDeleteWithLock(id, version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessage('Deleted!', 'alert-success', true);
						$scope.listWithCriteriasByPage(1);
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
				attachmentService.updateForDeleteWithLock($scope.attachment.id, $scope.attachment.version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.showMessage('Deleted!', 'alert-success', true);
						$scope.createNew();
						$scope.resetValidate();
						$scope.listWithCriteriasByPage(1);
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
			attachmentService.getById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.attachment = data;
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
			attachmentService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					$scope.attachments = [];
					$scope.page.totalElements = 0;
					if(response.data.content && response.data.content.length > 0) {
						var result = angular.fromJson(response.data.content);
						$scope.attachments = result;
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
				// idref.
		    	//result.push({ key: 'idref', operation: 'like', value: $scope.search.content, logic: 'or' });
				// reftype.
		    	//result.push({ key: 'reftype', operation: 'like', value: $scope.search.content, logic: 'or' });
				// filename.
		    	//result.push({ key: 'filename', operation: 'like', value: $scope.search.content, logic: 'or' });
				// filesize.
		    	//result.push({ key: 'filesize', operation: 'like', value: $scope.search.content, logic: 'or' });
				// mimetype.
		    	//result.push({ key: 'mimetype', operation: 'like', value: $scope.search.content, logic: 'or' });
				// description.
		    	//result.push({ key: 'description', operation: 'like', value: $scope.search.content, logic: 'or' });
				// filepath.
		    	//result.push({ key: 'filepath', operation: 'like', value: $scope.search.content, logic: 'or' });
				// filetype.
		    	//result.push({ key: 'filetype', operation: 'like', value: $scope.search.content, logic: 'or' });
				// note.
		    	//result.push({ key: 'note', operation: 'like', value: $scope.search.content, logic: 'or' });
		    }
		    // return.
		    return result;
		}
			
		// Show message.
		$scope.showMessage = function(message, cssName, autoHide) {
			$scope.attachmentAlertMessage = message;
			$('#attachmentAlertMessage').removeClass('alert-danger');
			$('#attachmentAlertMessage').removeClass('alert-success');
			$('#attachmentAlertMessage').addClass(cssName);
			$('#attachmentAlertMessage').slideDown(500, function() {
				if(autoHide) {
					$window.setTimeout(function() {
						$('#attachmentAlertMessage').slideUp(500, function() {
							$('#attachmentAlertMessage').removeClass(cssName);
		            	});
					}, 1000);
				}
			});
		}
	
	}]);

});
