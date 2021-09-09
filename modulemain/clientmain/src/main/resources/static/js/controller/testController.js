
define(['require', 'angular'], function (require, angular) {

	app.aController(clientmain.prefix + 'testController', function($scope, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, $element, moment, testService) {
		
		
		////////////////////////////////////////
	    // Full screen.
		////////////////////////////////////////
	    $scope.isFormScreenFull = false;
	    $scope.mdDialogResizeClass = 'md-dialog-small'; 
	    $scope.imgSmallFullClass = 'glyphicon-resize-full';
		$scope.resizeFormScreen = function(event) {
			$scope.isFormScreenFull = !$scope.isFormScreenFull;
			if($scope.isFormScreenFull) {
				// Remove class.
				angular.element(document.querySelector('md-dialog')).removeClass('md-dialog-small');
				angular.element(event.target).removeClass('glyphicon-resize-full');
				// Add class.
				angular.element(document.querySelector('md-dialog')).addClass('md-dialog-full');
				angular.element(event.target).addClass('glyphicon-resize-small');
			} else {
				// Remove class.
				angular.element(document.querySelector('md-dialog')).removeClass('md-dialog-full');
				angular.element(event.target).removeClass('glyphicon-resize-small');
				// Add class.
				angular.element(document.querySelector('md-dialog')).addClass('md-dialog-small');
				angular.element(event.target).addClass('glyphicon-resize-full');
			}
		}
		
		
		////////////////////////////////////////
		// showDialog.
		////////////////////////////////////////
	    $scope.showDialog = function (dialog) {
	        var htmlUrlTemplate = clientmain.contextPath + '/view/test_list.html';
	        clientmain.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
	    }
		
		////////////////////////////////////////
	    // Show Dialog with controller.
		////////////////////////////////////////
		$scope.showDialogWithController = function() {
			var htmlUrlTemplate = clientmain.contextPath + '/view/client_list.html';
			clientmain.showDialogWithControllerName(clientmain.prefix + 'clientController', 'clientController', $mdDialog, htmlUrlTemplate).then(function(evt) {
	        	console.log('closed');
	        }, function(evt) {
	        	console.log('not closed');
	        });
		}
		
		////////////////////////////////////////
	    // Close dialog.
		////////////////////////////////////////
		$scope.closeDialog = function() {
			$mdDialog.hide();
		}
		
		////////////////////////////////////////
		// Auto complete: entityfield.
		////////////////////////////////////////
		$scope.ctlentityfield = {};
		$scope.ctlentityfield.items = [{value: 'a', display: 'a'}, {value: 'aa', display: 'aa'}, {value: 'b', display: 'b'}, {value: 'bb', display: 'bb'}];
		$scope.ctlentityfield.isCallServer = false;
	    $scope.ctlentityfield.isDisabled    = false;
	    // New.
	    $scope.ctlentityfield.newState = function(item) {
	      alert("Sorry! You'll need to create a Constitution for " + item + " first!");
	    }
	    // Search in array.
	    $scope.ctlentityfield.querySearch = function(query) {
	      var results = query ? $scope.ctlentityfield.items.filter( $scope.ctlentityfield.createFilterFor(query) ) : $scope.ctlentityfield.items,
	          deferred;
	      
	      if ($scope.ctlentityfield.isCallServer) {
	        deferred = $q.defer();
	        $timeout(function () { deferred.resolve( results ); }, Math.random() * 1000, false);
	        return deferred.promise;
	      } else {
	        return results;
	      }
	    }
	    // Filter.
	    $scope.ctlentityfield.createFilterFor = function(query) {
			 var lowercaseQuery = angular.lowercase(query);
			
			 return function filterFn(item) {
				 return (angular.lowercase(item.display).indexOf(lowercaseQuery) >= 0);
			 };
	    }
	    // Text change.
	    $scope.ctlentityfield.searchTextChange = function(text) {
	    	$log.info('Text changed to ' + text);
	    }
	    // Item change.
	    $scope.ctlentityfield.selectedItemChange = function(item) {
	    	if(frmEntity.entityfield === undefined) {
	    		return;
	    	}
	    	$scope.entity.entityfield = undefined;
	    	frmEntity.entityfield.$invalid = true;
	    	if(item) {
	    		$scope.entity.entityfield = item.id;
	    		frmEntity.entityfield.$invalid = false;
	    	}
	    }
	    // Watch $scope.entity.entityfield
	    $scope.$watch('entity.entityfield', function(newVal, oldVal, scope) {
	    	$scope.ctlentityfield.selectedItem = undefined;
	    	if(newVal === undefined) {
	    		return;
	    	}
	    	var length = $scope.ctlentityfield.items.length;
	    	for(var i = 0; i < length; i++) {
	    		var item = $scope.ctlentityfield.items[i];
	    		if(item.id === newVal) {
	    			$scope.ctlentityfield.selectedItem = item;
	    			break;
	    		}
	    	}
	    });

	    
		////////////////////////////////////////
	    // Multiple select.
		////////////////////////////////////////
	    $scope.entityfields = [{id: 1, name: 'a'}, {id: 2, name: 'aa'}, {id: 3, name: 'b'}, {id: 4, name: 'bb'}];
	    $scope.searchentityfield;
	    $scope.clearSearchentityfield = function() {
		    $scope.searchentityfield = '';
	    };
		$scope.searchentityfieldKeyDown = function(event) {
			event.stopPropagation();
		}
/*		
		$element.find('input').on('keydown', function(ev) {
			ev.stopPropagation();
		});
*/	    
	    
		////////////////////////////////////////
	    // Date picker popup.
		////////////////////////////////////////
        $scope.dateFormat = "dd/MM/yyyy";
	    $scope.startDate = new Date();
	    $scope.endDate = new Date();
        $scope.status = {};
        // Set min and max date.
        var durationNumber = 10;
    	$scope.maxDate = new Date();
    	$scope.minDate = new Date();
    	$scope.maxDate.setFullYear($scope.maxDate.getFullYear() + durationNumber);
    	$scope.minDate.setFullYear($scope.minDate.getFullYear() - durationNumber);
        // Disable weekend selection
    	$scope.disabled = function (date, mode) {
    		return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
        }
    	// Set date options.
    	$scope.dateOptions = {
	        formatYear: 'yyyy',
	        startingDay: 1
    	};
    	

    	
    	////////////////////////////////////////
	    // Angular ckeditor.
		////////////////////////////////////////
    	CKEDITOR.config.extraPlugins = "base64image";
    	$scope.options = {
    		htmlEncodeOutput: false,
    	    allowedContent: true,
    	    entities: false,
    	    enterMode: CKEDITOR.ENTER_BR,
    		shiftEnterMode: CKEDITOR.ENTER_P,
    		autoParagraph: false
    	};
    	
    	
    	////////////////////////////////////////
	    // gantt chart.
		////////////////////////////////////////
   	
    	$scope.factoryTask = function() {
    		return {
    			id: 111,
    			name: 'New task',
    			color: '#AA8833'
    		}
    	}
    	
    	// Setting for gantt.
    	$scope.ganttOptions = {
    			readOnly: false,
    			movable: {
    				allowRowSwitching: function(task, targetRow) {
    					return true;
    				}
    			}
    	}
    	
    	// Data.
    	$scope.data = 
    		[
            	{name: 'Milestones', height: '3em', sortable: false, classes: 'gantt-row-milestone', color: '#45607D', tasks: [
                    // Dates can be specified as string, timestamp or javascript date object. The data attribute can be used to attach a custom object
                    {name: 'Kickoff', color: '#93C47D', from: '2013-10-07T09:00:00', to: '2013-10-07T10:00:00', data: 'Can contain any custom data or object'},
                    {name: 'Concept approval', color: '#93C47D', from: new Date(2013, 9, 18, 18, 0, 0), to: new Date(2013, 9, 18, 18, 0, 0), est: new Date(2013, 9, 16, 7, 0, 0), lct: new Date(2013, 9, 19, 0, 0, 0)},
                    {name: 'Development finished', color: '#93C47D', from: new Date(2013, 10, 15, 18, 0, 0), to: new Date(2013, 10, 15, 18, 0, 0)},
                    {name: 'Shop is running', color: '#93C47D', from: new Date(2013, 10, 22, 12, 0, 0), to: new Date(2013, 10, 22, 12, 0, 0)},
                    {name: 'Go-live', color: '#93C47D', from: new Date(2013, 10, 29, 16, 0, 0), to: new Date(2013, 10, 29, 16, 0, 0)}
                    ], data: 'Can contain any custom data or object'},
                {name: 'Status meetings', tasks: [
                    {name: 'Demo #1', color: '#9FC5F8', from: new Date(2013, 9, 25, 15, 0, 0), to: new Date(2013, 9, 25, 18, 30, 0)},
                    {name: 'Demo #2', color: '#9FC5F8', from: new Date(2013, 10, 1, 15, 0, 0), to: new Date(2013, 10, 1, 18, 0, 0)},
                    {name: 'Demo #3', color: '#9FC5F8', from: new Date(2013, 10, 8, 15, 0, 0), to: new Date(2013, 10, 8, 18, 0, 0)},
                    {name: 'Demo #4', color: '#9FC5F8', from: new Date(2013, 10, 15, 15, 0, 0), to: new Date(2013, 10, 15, 18, 0, 0)},
                    {name: 'Demo #5', color: '#9FC5F8', from: new Date(2013, 10, 24, 9, 0, 0), to: new Date(2013, 10, 24, 10, 0, 0)}
                ]},
                {name: 'Kickoff', movable: {allowResizing: false}, tasks: [
                    {name: 'Day 1', color: '#9FC5F8', from: new Date(2013, 9, 7, 9, 0, 0), to: new Date(2013, 9, 7, 17, 0, 0),
                        progress: {percent: 100, color: '#3C8CF8'}, movable: false},
                    {name: 'Day 2', color: '#9FC5F8', from: new Date(2013, 9, 8, 9, 0, 0), to: new Date(2013, 9, 8, 17, 0, 0),
                        progress: {percent: 100, color: '#3C8CF8'}},
                    {name: 'Day 3', color: '#9FC5F8', from: new Date(2013, 9, 9, 8, 30, 0), to: new Date(2013, 9, 9, 12, 0, 0),
                        progress: {percent: 100, color: '#3C8CF8'}}
                ]},
                {name: 'Create concept', tasks: [
                    {name: 'Create concept', content: '<i class="fa fa-cog" ng-click="scope.handleTaskIconClick(task.model)"></i> {{task.model.name}}', color: '#F1C232', from: new Date(2013, 9, 10, 8, 0, 0), to: new Date(2013, 9, 16, 18, 0, 0), est: new Date(2013, 9, 8, 8, 0, 0), lct: new Date(2013, 9, 18, 20, 0, 0),
                        progress: 100}
                ]},
                {name: 'Finalize concept', tasks: [
                    {name: 'Finalize concept', color: '#F1C232', from: new Date(2013, 9, 17, 8, 0, 0), to: new Date(2013, 9, 18, 18, 0, 0),
                        progress: 100}
                ]},
                {name: 'Development', children: ['Sprint 1', 'Sprint 2', 'Sprint 3', 'Sprint 4'], content: '<i class="fa fa-file-code-o" ng-click="scope.handleRowIconClick(row.model)"></i> {{row.model.name}}'},
                {name: 'Sprint 1', tooltips: false, tasks: [
                    {id: 'idTask1', name: 'Product list view', color: '#F1C232', from: new Date(2013, 9, 21, 8, 0, 0), to: new Date(2013, 9, 25, 15, 0, 0),
                        progress: 25}
                ]},
                {name: 'Sprint 2', tasks: [
                    {name: 'Order basket', color: '#F1C232', from: new Date(2013, 9, 28, 8, 0, 0), to: new Date(2013, 10, 1, 15, 0, 0),
                    	dependencies: [{from: 'idTask1'}, {to: 'idTask3'}]
                    }
                ]},
                {name: 'Sprint 3', tasks: [
                    {id: 'idTask3', name: 'Checkout', color: '#F1C232', from: new Date(2013, 10, 4, 8, 0, 0), to: new Date(2013, 10, 8, 15, 0, 0)}
                ]},
                {name: 'Sprint 4', tasks: [
                    {name: 'Login & Signup & Admin Views', color: '#F1C232', from: new Date(2013, 10, 11, 8, 0, 0), to: new Date(2013, 10, 15, 15, 0, 0)}
                ]},
                {name: 'Hosting'},
                {name: 'Setup', tasks: [
                    {name: 'HW', color: '#F1C232', from: new Date(2013, 10, 18, 8, 0, 0), to: new Date(2013, 10, 18, 12, 0, 0)}
                ]},
                {name: 'Config', tasks: [
                    {name: 'SW / DNS/ Backups', color: '#F1C232', from: new Date(2013, 10, 18, 12, 0, 0), to: new Date(2013, 10, 21, 18, 0, 0)}
                ]},
                {name: 'Server', parent: 'Hosting', children: ['Setup', 'Config']},
                {name: 'Deployment', parent: 'Hosting', tasks: [
                    {name: 'Depl. & Final testing', color: '#F1C232', from: new Date(2013, 10, 21, 8, 0, 0), to: new Date(2013, 10, 22, 12, 0, 0), 'classes': 'gantt-task-deployment'}
                ]},
                {name: 'Workshop', tasks: [
                    {name: 'On-side education', color: '#F1C232', from: new Date(2013, 10, 24, 9, 0, 0), to: new Date(2013, 10, 25, 15, 0, 0)}
                ]},
                {name: 'Content', tasks: [
                    {name: 'Supervise content creation', color: '#F1C232', from: new Date(2013, 10, 26, 9, 0, 0), to: new Date(2013, 10, 29, 16, 0, 0)}
                ]},
                {name: 'Documentation', tasks: [
                    {name: 'Technical/User documentation', color: '#F1C232', from: new Date(2013, 10, 26, 8, 0, 0), to: new Date(2013, 10, 28, 18, 0, 0)}
                ]}
            ];


    	////////////////////////////////////////
    	// Attachments.
    	////////////////////////////////////////
    	// Add attachments.
    	
    	$scope.test = { id: 1 };
    	$scope.filenames = [];
    	$scope.files = new FormData();
		$scope.test.attachments = [];
    	
    	$scope.addAttachments = function($files) {
    		angular.forEach($files, function(file, idx) {
    			if($.inArray(file.name, $scope.filenames) < 0) {
    				$scope.filenames.push(file.name);
    				$scope.files.append("files", file);
    				var attachment = { filename: file.name, fileSize: file.size, mimeType: file.type };
    				$scope.test.attachments.push(attachment);
    			}
    		});
    		$scope.$apply();
    	}
    	// Clear value of file element.
    	$scope.fileClear = function (event) {
    		$(event.target).val(null);
    	};
    	// Delete attachments.
    	$scope.deleteAttachments = function(filename) {
    		if(!$window.confirm('Are you sure to delete?')) {
    			return;
    		}
    		$scope.filenames = $.grep($scope.filenames, function (pValue) {
                                    return pValue != filename;
                                });
    		
    		var files = $scope.files.getAll('files');
    		var fileLength = files.length;
    		for(var idx = 0; idx < fileLength; idx++) {
    			if(files[idx].name == filename) {
    				files.splice(idx, 1);
    				fileLength = -1;
    			}
    		}
    		$scope.files = new FormData();
    		angular.forEach(files, function(file, idx) {
    			$scope.files.append("files", file);
    		});

    		$scope.test.attachments = $.grep($scope.test.attachments, function (attachment) {
                                    return attachment.filename != filename;
                                });
    	}
        // Upload attachments.
        $scope.uploadAttachments = function (attachments, testId) {
    		testService.uploadAttachments(attachments, testId).then(
	    		// Success.
	    		function(response) {
	    		},
	    		// Error.
	    		function(response) {
	    			$window.alert('Upload error!');
	    		}
	    	);
        }
    	// Download attachments.
    	$scope.downloadAttachments = function(id, filename) {
    		testService.downloadAttachments(id, filename).then(
    		// success.
    		function(response, status, headers, config) {
    			var file = new Blob([response], {type: 'application/*'});
    			var fileURL = (window.URL || window.webkitURL).createObjectURL(file);
    			var downloadLink = angular.element('<a></a>');
    			downloadLink.attr('href', fileURL);
                downloadLink.attr('download', filename);
    			downloadLink[0].click();
    		},
    		// error.
    		function(response, status, headers, config) {
    			$window.alert('Upload error!');
    		});
    	}
    	

    	
	});
	
});
