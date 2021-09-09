
/**
 * Controller for Attachment
 **/

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'attachmentController', ['$scope', '$state','$mdToast', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', clientwh.prefix + 'attachmentService',
		function($scope, $state, $mdToast,$rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, attachmentService) {
		if(typeof(clientwh.translate.attachment) === 'undefined' || clientwh.translate.attachment.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.attachment) === 'undefined') {
				clientwh.translate.attachment = '';
			}
			clientwh.translate.attachment += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/attachment');
			$translate.refresh();
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_attachment_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
	    $translate.onReady().then(function() {
	    	$scope.title = $translate.instant('clientwh_attachment_title');
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
		
		$scope.attachment = {};
		
		// Init for list.
		$rootScope.idattachmentController = $scope.$id;
		$scope.initList = function() {
			// watch parent.
			if($rootScope.watchidmaterial) {
				$rootScope.watchidmaterial();
			}
			$rootScope.watchidmaterial = $scope.$watch(
					function(scope) {
						return scope.$parent ? scope.$parent.attachmentScope.idref : null;
					},
					function(newValue, oldValue) {
						if($scope.$parent.attachmentScope) {
							$scope.attachments = [];
							$scope.page.totalElements = 0;
							$scope.idref = $scope.$parent.attachmentScope.idref;
							$scope.reftype = $scope.$parent.attachmentScope.reftype;
							if($scope.idref > -1 && $rootScope.idattachmentController == $scope.$id) {
								$scope.listWithCriteriasByIdrefAndReftypeAndPage($scope.idref, $scope.reftype, 1);
							}
						}
					}
			);
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
		$scope.showForm = function(attachment) {
			$scope.initForm(attachment.id);
			$scope.showDialog();
		}

	    // Show edit view.
	    $scope.showDialog = function () {
	    	$mdToast.hide();
	        var htmlUrlTemplate = clientwh.contextPath + '/view/attachment_form.html';
	        clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }

	    // Create new.
		$scope.createNew = function() {
			$scope.attachment = { id: -1, idref: $scope.idref, reftype: $scope.reftype };
		}
		
		// Reset validate.
		$scope.resetValidate = function() {
			// filename.
		    $scope.frmAttachment.filename.$setPristine();
			$scope.frmAttachment.filename.$setUntouched();
			// description.
		    $scope.frmAttachment.description.$setPristine();
			$scope.frmAttachment.description.$setUntouched();

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
			$scope.showMessageOnToast($translate.instant('clientwh_home_saving'));
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
					// Upload file.
					attachmentService.uploadFile(encodeURIComponent($scope.reftype + '\\' + $scope.idref + '\\'), $scope.file).then(
							// success.
							function(response1){
								if(response1.status === httpStatus.code.OK) {
									$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
									$scope.listWithCriteriasByIdrefAndReftypeAndPage($scope.idref, $scope.reftype, 1);
								}
								else if(response1.status === -1) {
									$scope.showMessageOnToast($translate.instant('clientwh_attachment_error_filesizelimited'));
								}
								else {
									$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
								}
							},
							// error.
							function(response1){
								$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
							}
					);
				} else {
					if(!response.data){
						$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
					}
					else if(response.data.code == clientwh.serverCode.EXISTFILENAME) {
						$scope.getById($scope.attachment.id);
						//$scope.frmAttachment.filename.$invalid = true;
						$scope.showMessageOnToast($translate.instant('clientwh_servercode_' + response.data.code));
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
		
		// Delete.
		$scope.delete = function(attachment) {
				attachmentService.updateForDeleteWithLock(attachment.id, attachment.version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						// Update for delete file.
						attachmentService.updateForDeleteFile(encodeURIComponent(attachment.filepath + attachment.filename)).then(
								// success.
								function(response1){
									$scope.showMessageOnToastList($translate.instant('clientwh_home_deleted'));
									$scope.listWithCriteriasByIdrefAndReftypeAndPage($scope.idref, $scope.reftype, 1);
								},
								// error.
								function(response1){
									$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
								}
						);
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
				attachmentService.updateForDeleteWithLock($scope.attachment.id, $scope.attachment.version)
				// success.
				.then(function(response) {
					if(response.status === httpStatus.code.OK) {
						// Update for delete file.
						attachmentService.updateForDeleteFile(encodeURIComponent($scope.attachment.filepath + $scope.attachment.filename)).then(
								// success.
								function(response1){
									$scope.showMessageOnToast($translate.instant('clientwh_home_deleted'));
									$scope.createNew();
									$scope.resetValidate();
									$scope.listWithCriteriasByIdrefAndReftypeAndPage($scope.idref, $scope.reftype, 1);
								},
								// error.
								function(response1){
									$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
								}
						);
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
			attachmentService.getById(id).then(
			// success.
			function(response) {
				if(response.status === httpStatus.code.OK) {
					var data = angular.fromJson(response.data);
					$scope.attachment = data;
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
					$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
				}
			},
			// error.
			function(response) {
				$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
			});
		}
		
		// List for page and filter.
		$scope.listWithCriteriasByIdrefAndReftypeAndPage = function(idref, reftype, pageNo) {
			$scope.page.currentPage = pageNo;
			attachmentService.listWithCriteriasByIdrefAndReftypeAndPage(idref, reftype, $scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
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
					$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
				}
			},
			// error.
			function(response) {
				$scope.showMessageOnToast($translate.instant('clientwh_home_error') );
			});
		}
		
		// Clear filter.
		$scope.clearFilter = function() {
			$scope.search = {};
		}
		
		// Clear filter search.
		$scope.clearFilterSearch = function() {
			$scope.search.content = "";
			$scope.listWithCriteriasByIdrefAndReftypeAndPage($scope.idref, $scope.reftype,1);
		}
		
		$scope.clearSortBy = function(){
			$scope.sortKeyAttachment = '';
			$scope.sortByArr = {};
			// Reload data.
			$scope.listWithCriteriasByIdrefAndReftypeAndPage($scope.idref,$scope.reftype,$scope.page.currentPage);
		}
		
		$scope.sortByName = clientwh.sortByNameAttachment;
		
		$scope.sortByArr = {}
		// Sort by.
		$scope.sortBy = function(keyName){
			$scope.sortKeyAttachment = keyName;
			$scope.reverse = !$scope.reverse;
			// Reload data.
			$scope.listWithCriteriasByIdrefAndReftypeAndPage($scope.idref,$scope.reftype,$scope.page.currentPage);
		}
		
		// Get sort object.
		$scope.getSort = function() {
			var result = [];
			// name.
			if(typeof($scope.sortKeyAttachment) !== 'undefined' && $scope.sortKeyAttachment !== ''){
				var order = 'desc';
		    	if($scope.reverse) {
		    		order = 'asc';
		    	}
		    	result.push('sort=' + $scope.sortKeyAttachment + ',' + order);
			}
			// return.
			return result;
		}
		
		// Get search object.
		$scope.getSearch = function() {
		    var result = [];
		    if(typeof($scope.search.content) !== 'undefined' && $scope.search.content !== ''){
		    	result.push({ key: 'description', operation: 'like', value: $scope.search.content, logic: 'or' },
	    				{ key: 'filename', operation: 'like', value: $scope.search.content, logic: 'or' });
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
						controller  : 'clientwhattachmentController',
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
		
		$scope.cofirmDeleteToastList = function(attachment){
			$mdToast.show(
					{  	templateUrl : clientwh.contextPath + '/view/toast.html',
						hideDelay:5000,
						controller  : 'clientwhattachmentController',
						position: 'right'}).then(function(response){
							if (response) {
								  $scope.delete(attachment);
							}
						});
		}
		
		$scope.closeDialog = function(){
			$mdToast.hide();
			$mdDialog.hide();
		}
	    
	    ////////////////////////////////////////
    	// Attachments.
    	////////////////////////////////////////
    	// Add attachments.
		$scope.file;
    	
    	$scope.addAttachments = function($files) {
    		if($files && $files.length > 0){
    			$scope.file = $files[0];
    			$scope.attachment.filename = $scope.file.name;
    			$scope.attachment.filesize = $scope.file.size;
    			$scope.attachment.filetype = $scope.file.type;
    			$scope.attachment.mimetype = $scope.file.type;
    		}
    		$scope.$apply();
    	}
    	
    	// Clear value of file element.
    	$scope.fileClear = function (event) {
    		$(event.target).val(null);
    	};
    	
    	// Download file.
    	$scope.downloadFile = function(attachment) {
    		attachmentService.downloadFile(attachment.filepath + attachment.filename).then(
    		// success.
    		function(response, status, headers, config) {
    			var file = new Blob([response.data], {type: 'application/*'});
    			var fileURL = (window.URL || window.webkitURL).createObjectURL(file);
    			var downloadLink = angular.element('<a></a>');
    			downloadLink.attr('href', fileURL);
                downloadLink.attr('download', attachment.filename);
    			downloadLink[0].click();
    		},
    		// error.
    		function(response, status, headers, config) {
    			$window.alert('Upload error!');
    		});
    	}
    	
	}]);

});
