
/**
 * Controller for Permission
 **/

define(['require', 'angular'], function (require, angular) {
    app.aController(clientwh.prefix + 'permissionAllowController', ['$scope', '$mdToast', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader','$q', clientwh.prefix + 'userService', clientwh.prefix + 'permissionService',
        function ($scope, $mdToast, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader,$q, userService, permissionService) {
            if (typeof (clientwh.translate.permission) === 'undefined' || clientwh.translate.permission.indexOf($translate.use()) < 0) {
                console.log(clientwh.translate.permission);
                if (typeof (clientwh.translate.permission) === 'undefined') {
                    clientwh.translate.permission = '';
                }
                clientwh.translate.permission += $translate.use() + ';';
                $translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/permission');
                $translate.refresh();
            }

            var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
                console.log('clientwh_permission_title');
                $scope.title = $translate.instant('clientwh_permission_title');
            });
            // Unregister
            $scope.$on('$destroy', function () {
                unRegister();
            });
            $translate.onReady().then(function () {
                console.log('permission onReady');
                $scope.title = $translate.instant('clientwh_permission_title');
                $translate.refresh();
            });
            
            $scope.ArrTarget = [
            	{value:'user', display:'User', active:true},
            	{value:'category', display:'Category'},
            	{value:'system', display:'System'},
            	{value:'type', display:'Type'},
            	{value:'spec', display:'Specification'},
            	{value:'unit', display:'Unit'},
            	{value:'brand', display:'Brand'},
            	{value:'origin', display:'Origin'},
            	{value:'material', display:'Material'},
            	{value:'store', display:'Store'},
            	{value:'materialimport', display:'Material Import'},
            	{value:'materialexport', display:'Material Export'},
            	{value:'supplier', display:'Supplier'},
            	{value:'quotation', display:'Quotation'},
            	{value:'request', display:'Request'},
            	{value:'purchase', display:'Purchase'}
            	];
            
            // Search.
            $scope.search = {};

            $scope.permission = {};
            $scope.rules = {};
            $scope.availableUser = [];
            $scope.selectedAdmins = [];
            $scope.selectedReads = [];
            $scope.selectedCreates = [];
            $scope.selectedUpdates = [];
            $scope.selectedDeletes = [];
            $scope.selectedAdOnCreate = [];
            $scope.selectedAdOnOwner = [];
            $scope.selectedCreatCData = [];
            $scope.selectedAdminsCData = [];
            $scope.selectedReadsCData = [];
            $scope.selectedCreatesCData = [];
            $scope.selectedUpdatesCData = [];
            $scope.selectedDeletesCData = [];
            $scope.selectedOwnersOData = [];
            $scope.selectedAdminsOData =[];
            $scope.selectedReadsOData = [];
            $scope.selectedCreatesOData = [];
            $scope.selectedUpdatesOData = [];
            $scope.selectedDeletesOData = [];
            
            $scope.tab = 0;
            
            var NUM_EMPTY = 0;
            var NUM_ALL = -1;
            
            var NUM_TAB_ADMIN = 0;
            var NUM_TAB_READ = 1;
            var NUM_TAB_CREATE = 2;
            var NUM_TAB_UPDATE = 3;
            var NUM_TAB_DELETE = 4;
            var NUM_TAB_ADMIN_ON_CREATE = 5;
            var NUM_TAB_ADMIN_ON_OWNER = 6;
            var NUM_TAB_CREATE_CREAT = 7;
            var NUM_TAB_CREATE_ADMIN = 8;
            var NUM_TAB_CREATE_READ = 9;
            var NUM_TAB_CREATE_UPDATE = 10;
            var NUM_TAB_CREATE_DELETE = 11;
            var NUM_TAB_OWNER_OWNER = 12;
            var NUM_TAB_OWNER_ADMIN = 13;
            var NUM_TAB_OWNER_READ = 14;
            var NUM_TAB_OWNER_UPDATE = 15;
            var NUM_TAB_OWNER_DELETE = 16;

            $scope.changeLanguage = function (language) {
                $translate.refresh();
                $translate.use(language);
                $translate.refresh();
                $translate.use(language);
                $translate.refresh();
            }
            
            
            $scope.showMessageOnToast = function(message){
    			$mdToast.show(
    					{ template: '<md-toast class="md-toast">' + message + '</md-toast>',
    						hideDelay:3000,
    						position: 'right'})
    		}
            
            // Promise list for select.
    		var listAllSelectPromise;
            // Init for list.
            $scope.initList = function () {
                if(typeof(listAllSelectPromise) === 'undefined') {
    				var listAllSelectDefered = $q.defer();
    				listAllSelectPromise = listAllSelectDefered.promise;
    				listAllSelectDefered.resolve([]);
    			}
    			listAllSelectPromise.then(
    				// Success.
    				function(response) {
    					  
    		                $scope.tab = NUM_TAB_ADMIN;
    		                $scope.changePrimaryTab(NUM_TAB_ADMIN);
    				},
    				// Error.
    				function(response) {
    					
    				}
    			);
            }
            
         // Call service: list all for select.
    		$scope.listAllForSelect = function() {
    			var listAllSelectDeferred = $q.defer();
    			// Init data for select.
    			$scope.availableUser = [];
    			$scope.listPermission = [];
    			// Call service.
    			var listUserDeferred = userService.getAllUser();
    			var listPermissionDeferred = permissionService.getAllPermission();
    			// Response.
    			$q.all([listUserDeferred,listPermissionDeferred]).then(
    				// Successes.
    				function(responses) {
    					$scope.permission.target = 'user'; //Target default
    					if(responses[0].data)
                		{
    						$scope.availableUser = responses[0].data; //Get user available
                		}
                    	$scope.availableUser.push({ "id": NUM_ALL, "username": "All" });
                    	if(responses[1].data)
                		{	
                    		$scope.listPermission =  responses[1].data; //Get list permission
                    		$scope.loadPermission($scope.permission.target);
                    		$scope.disableItemInAvailable($scope.availableUser,$scope.tab);
                		}	
    					// Resolve promise.
    					listAllSelectDeferred.resolve(responses);
    				},
    				// Errors.
    				function(responses) {
    					$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
    					// Reject promise.
    					listAllSelectDeferred.reject(responses);
    				}
    				
    			);
    			return listAllSelectDeferred.promise;
    		}
    		listAllSelectPromise = $scope.listAllForSelect();
            // Show edit view.
            $scope.showDialog = function () {
                var htmlUrlTemplate = clientwh.contextPath + '/view/permission_form.html';
                clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function (evt) {
                    console.log('closed');
                }, function (evt) {
                    console.log('not closed');
                });
            }

            // Save.
            $scope.save = function () {
                $scope.permission.id = -1;
                if ($scope.listPermission!=null) {
                	for (var i = 0; i < $scope.listPermission.length; i++) {
                        if ($scope.listPermission[i].target == $scope.permission.target) {
                            $scope.permission = $scope.listPermission[i];
                        }
                    }
				}
                // Add serial rules by target
                $scope.addRules();
                
                if ($scope.permission.rules!=null) {
                	 var tmp = JSON.parse( $scope.permission.rules);
                	 tmp.allow = $scope.rules;
                     $scope.permission.rules = JSON.stringify(tmp);
				}
                else {
                	var rule = {}
                	rule.allow = $scope.rules;
                	rule.deny = {};
                	$scope.permission.rules = JSON.stringify(rule);
                }
                $scope.showMessageOnToast($translate.instant('clientwh_home_saving'));
                var result;
                if ($scope.permission.id > -1) {
                    result = permissionService.updateWithLock($scope.permission.id, $scope.permission);
                } else {
                    result = permissionService.create($scope.permission);
                }
                result
                    // success.
                    .then(function (response) {
                        if (response.status === httpStatus.code.OK) {
                        	if($scope.permission.id > -1) {
        						$scope.permission.version = response.data;
        					} else {
        						$scope.permission.id = response.data;
        						$scope.permission.version = 1;
        					}
                            $scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
							// Get permission.
							$rootScope.getPermissionForUserMenu();
                            $scope.getAllPermission();
                        } else {
        					if(response.data.code == clientwh.serverCode.EXISTSCOPE) {
        						$scope.frmAppconfig.scope.$invalid = true;
        						$scope.showMessageOnToast($translate.instant('clientwh_servercode_' + response.data.code));
        					} else if(response.data.code == clientwh.serverCode.VERSIONDIFFERENCE) {
        						$scope.showMessageOnToast($translate.instant('clientwh_servercode_' + response.data.code));
        					} else {
        						$scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
        					}
        				}
                    },
                    // error.
                    function (response) {
                        $scope.showMessageOnToast($translate.instant('clientwh_home_error'));
                    });
            }

            // Get by Id.
            $scope.getById = function (id) {
                permissionService.getById(id)
                    // success.
                    .then(function (response) {
                        if (response.status === httpStatus.code.OK) {
                            var data = angular.fromJson(response.data);
                            $scope.permission = data;
                        } else {
                            $scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
                        }
                    },
                    // error.
                    function (response) {
                        $scope.showMessageOnToast($translate.instant('clientwh_home_error'));
                    });
            }

            // Clear filter.
            $scope.clearFilter = function () {
                $scope.search = {};
            }

            /*Extend functions*/

            // Sort by.
            $scope.sortBy = function (keyName) {
                $scope.sortKey = keyName;
                $scope.reverse = !$scope.reverse;
            }

            // Get sort target.
            $scope.getSort = function () {
                var result = [];
                // name.
                if (typeof ($scope.sortKey) !== 'undefined' && $scope.sortKey !== '') {
                    result.push('sort=' + $scope.sortKey + ',' + $scope.reverse);
                }
                // return.
                return result;
            }

            // Get search target.
            $scope.getSearch = function () {
                var result = [];
                // target.
                if (typeof ($scope.search.target) !== 'undefined' && $scope.search.target !== '') {
                    result.push({ key: 'target', operation: 'like', value: $scope.search.target, logic: 'or' });
                }
                // rules.
                if (typeof ($scope.search.rules) !== 'undefined' && $scope.search.rules !== '') {
                    result.push({ key: 'rules', operation: 'like', value: $scope.search.rules, logic: 'or' });
                }
                // return.
                return result;
            }

            // Get all permission.
            $scope.getAllPermission = function () {
                permissionService.getAllPermission()
                    // success.
                    .then(function (response) {
                        if (response.status === httpStatus.code.OK) {
                        	// Have data response
                        	if(response.data)
                        		{
                        		 var data = angular.fromJson(response.data);
                                 $scope.allPermission(4); //Clean all data on UI
                                 $scope.listPermission = data; 
                                 $scope.loadPermission($scope.permission.target); // Reload list by target
                                 $scope.disableItemInAvailable($scope.availableUser,$scope.tab);
                        		}
                        } else {
                            $scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
                        }
                    },
                    // error.
                    function (response) {
                        $scope.showMessageOnToast($translate.instant('clientwh_home_error'));
                    });
            }

            // Get all user.
            $scope.getAllUser = function () {
                userService.getAllUser()
                    // success.
                    .then(function (response) {
                        if (response.status === httpStatus.code.OK) {
                        	$scope.availableUser = [];
                        	// Have data response
                        	if(response.data)
                    		{
	                        	var data = angular.fromJson(response.data);
	                            $scope.availableUser = data;
                    		}
                        	// User for denied default in list user available
                        	$scope.availableUser.push({ "id": NUM_ALL, "username": "All" });
                        } else {
                            $scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
                        }
                    },
                    // error.
                    function (response) {
                        $scope.showMessageOnToast($translate.instant('clientwh_home_error'));
                    });
            }

            // Get user by id
            $scope.getById = function (id) {
                userService.getById(id)
                    // success.
                    .then(function (response) {
                        if (response.status === httpStatus.code.OK) {
                            var data = angular.fromJson(response.data);
                            $scope.userById = data;
                        } else {
                            $scope.showMessageOnToast($translate.instant('clientwh_home_fail'));
                        }
                    },
                    // error.
                    function (response) {
                        $scope.showMessageOnToast($translate.instant('clientwh_home_error'));
                    });
            }
            
            // Display user name by id
            $scope.displayUsernameById = function(id){
            	 for (var i = 0; i < $scope.availableUser.length; i++) {
                     if (id == $scope.availableUser[i].id) {
                     	return $scope.availableUser[i]
                     }
                 }
            }
            
            
            // Load permission by target
            $scope.loadPermission = function (target) {
            	 $scope.ruleCreate = [];
                 $scope.ruleOwner = [];
                 
                 //Condition for show button create creat
                 $scope.addOneCreat = false;
                 //Condition for show button create owner
                 $scope.addOneOwner = false;
                 
                for (var i = 0; i < $scope.listPermission.length; i++) {
                    if ($scope.listPermission[i].target == target) {
                    	// Parse to rules to json
                        var tmp = JSON.parse($scope.listPermission[i].rules).allow; 
                       
                        // Condition create by id
                        if (Object.keys(tmp.createbyids).length==1) {
                        	if (tmp.createbyids[0].createbyids[0]==NUM_EMPTY || tmp.createbyids[0].createbyids[0]==null) { // If creat was denied or empty 
                        		
                        		$scope.ruleCreate = [];
                        		// Implement per object in json
                        		var sCreate = [tmp.createbyids[0].createbyids, tmp.createbyids[0].admins, tmp.createbyids[0].reads, tmp.createbyids[0].updates, tmp.createbyids[0].deletes];
                        		// Array to store data
                                var selectedCreate = [$scope.selectedCreatCData, $scope.selectedAdminsCData, $scope.selectedReadsCData, $scope.selectedUpdatesCData, $scope.selectedDeletesCData];
                                // Loop for insert user to array create
                                for (var j = 0; j < sCreate.length; j++) {
                                	
                                    var arrId = sCreate[j];
                                    if (arrId.length==0) {
                                    	selectedCreate[j].push($scope.displayUsernameById(NUM_ALL));
        							}
                                    else {
                                    for (var k = 0; k < arrId.length; k++) {
                                    	if ($scope.checkDupes(arrId[k], selectedCreate[j])) {
                                    		  if (arrId[k]==NUM_EMPTY) {
                                    			  selectedCreate[j] = []
                           					}
                                               else {
                                            	   selectedCreate[j].push($scope.displayUsernameById(arrId[k]));
                                               }
                                        }
                                    }
                                    }
                                    
                                }
							}	
                        	else {
                        		 $scope.ruleCreate = tmp.createbyids;
                        	}
						} 
                        
                        else {
                        	 $scope.ruleCreate = tmp.createbyids;
                        }
                        
                        // Condition owner by id
                        if (Object.keys( tmp.ownerbyids).length==1) {
                        	if ( tmp.ownerbyids[0].ownerbyids[0]==NUM_EMPTY|| tmp.ownerbyids[0].ownerbyids[0]==null) { // If owner was denied or empty 
                        		
                        		$scope.ruleOwner = [];
                        		// Implement per object in json
                        		var sOwner = [tmp.ownerbyids[0].ownerbyids, tmp.ownerbyids[0].admins, tmp.ownerbyids[0].reads, tmp.ownerbyids[0].updates, tmp.ownerbyids[0].deletes];
                        		// Array to store data
                                var selectedOwner = [ $scope.selectedOwnersOData, $scope.selectedAdminsOData, $scope.selectedReadsOData, $scope.selectedUpdatesOData, $scope.selectedDeletesOData];
                                
                                // Loop for insert user to array Owner
                                for (var j = 0; j < sOwner.length; j++) {
                                    var arrId = sOwner[j];
                                    if (arrId.length==NUM_EMPTY) {
                                    	selectedOwner[j].push($scope.displayUsernameById(NUM_ALL));
        							}
                                    else {
                                    for (var k = 0; k < arrId.length; k++) {
                                    	if ($scope.checkDupes(arrId[k], selectedOwner[j])) {
                                    		if (arrId[k]==NUM_EMPTY) {
                                    			selectedOwner[j] = [];
        									}
                                    		else {
                                    			 selectedOwner[j].push($scope.displayUsernameById(arrId[k]));
                                    		}
                                           
                                    	}
                                    }
                                    }
                                }
							}
                        	else {
                        		$scope.ruleOwner = tmp.ownerbyids;
                        	}
						} 
                        
                        else {
							 $scope.ruleOwner = tmp.ownerbyids;
						}
                        
                        // Implement per object in json
                        var s = [tmp.admins, tmp.creates, tmp.reads, tmp.updates, tmp.deletes, tmp.adminoncreates, tmp.adminonowners];
                        // Array to store data
                        var selected = [$scope.selectedAdmins, $scope.selectedCreates, $scope.selectedReads,  $scope.selectedUpdates, $scope.selectedDeletes, $scope.selectedAdOnCreate, $scope.selectedAdOnOwner];
                        
                        // Loop for insert user to array
                        for (var j = 0; j < s.length; j++) {
                            var arrId = s[j];
                            if (arrId.length==0) {
                            	selected[j].push($scope.displayUsernameById(NUM_ALL));
							}
                            else {
                            	for (var k = 0; k < arrId.length; k++) {
                            		if (arrId[k]==NUM_EMPTY) {
                            			selected[j] = [];
									}
                            		else {
                            			selected[j].push($scope.displayUsernameById(arrId[k]));
                            		}
                                }
                            }
                        }
                        
                    }
                }
            }
            
            // Check user exit in array creat
            $scope.checUserExitInArrCreat = function(id,arr){
            	for (var i = 0; i < arr.length; i++) {
            		for (var j = 0; j < arr[i].createbyids.length; j++) {
            			if (id==arr[i].createbyids[j]) {
    						return true;
    					}
					}
				}
            	return false;
            }
            
            // Check user exit in array owner
            $scope.checUserExitInArrOwner = function(id,arr){
            	for (var i = 0; i < arr.length; i++) {
					if (id==arr[i].ownerbyids) {
						return true;
					}
				}
            	return false;
            }
            
            // Init for create and owner
            $scope.ruleCreate = [];
            $scope.ruleOwner = [];
            $scope.addOneCreat = false;
            $scope.addOneOwner = false;
            
            // Check condition for add user create
            $scope.checkConditionAddCreat = function(){
            	$scope.arrTmp = $scope.choseArr(NUM_TAB_CREATE_CREAT);
            	if ($scope.checkDenied($scope.choseArr(NUM_TAB_CREATE_CREAT))) {
            		 $scope.addOneCreat = false;
            		 return;
				}
            	$scope.addOneCreat = true;
            }
            
            // Check condition for add user owner
            $scope.checkConditionAddOwner = function(){
            	$scope.arrTmp = $scope.choseArr(NUM_TAB_OWNER_OWNER);
            	if ($scope.checkDenied($scope.choseArr(NUM_TAB_OWNER_OWNER))) {
            		$scope.addOneOwner = false;
            		 return;
				}
            	$scope.addOneOwner = true;
            }
            
            // Add rule for create
            $scope.addRuleCreat = function(){
            	$scope.ruleCreat = { "createbyids": [], "admins": [], "reads": [], "updates": [], "deletes": [] }
            	// id array for get data
            	var idSelected = ['#selectedCreatCData option', '#selectedAdminsCData option', '#selectedReadsCData option',  '#selectedUpdatesCData option', '#selectedDeletesCData option'];
            	// Array to store data
            	var s = [$scope.ruleCreat.createbyids, $scope.ruleCreat.admins, $scope.ruleCreat.reads, $scope.ruleCreat.updates, $scope.ruleCreat.deletes];
            	
            	if ($scope.forCreate.id > -1) {
            		$scope.ruleCreate.splice($scope.forCreate.id, 1 );
				}
            	
            	for (var i = 0; i < idSelected.length; i++) {
                     var optionValues = [];
                     $(idSelected[i]).each(function () {
                         optionValues.push($(this).val());
                     });
                     if (optionValues.length==0) {
                     	s[i].push(NUM_EMPTY);
 					}
                     else {
                    	 for (var j = 0; j < optionValues.length; j++) {
                             var id = parseInt(optionValues[j]);
                             if (id==NUM_ALL) {
                              	s[i] = []
          					}
                              else {
                              	s[i].push(id);
                              }
                         }
                     }
                 }
            	 var flag = false;
            	 for (var i = 0; i < $scope.ruleCreat.createbyids.length; i++) { // Check user exit in createids
            		 flag = $scope.checUserExitInArrCreat($scope.ruleCreat.createbyids[i],$scope.ruleCreate);
            		 if (flag) {
            			$scope.showMessageOnToast($translate.instant('clientwh_permission_duplicate'))
						break;
					}
				}
            	 if (flag==false&&$scope.ruleCreat.createbyids.length!=NUM_EMPTY) { // Check user denied in createids
            		 if ($scope.forCreate.id > -1) {
            			 $scope.ruleCreate.splice($scope.forCreate.id, 0, $scope.ruleCreat);
					}
            		 else {
            			 $scope.ruleCreate.push($scope.ruleCreat);
                		 $scope.forCreate.id = Object.keys($scope.ruleCreate).length-1;
            		 }
            		 $scope.showMessageOnToast($translate.instant('clientwh_home_saved'))
                 	 $scope.disableItemInAvailable($scope.availableUser,$scope.tab);
				}
            	
            }
            
            // Remove item user in list creat
            $scope.removeRowCreate = function ($index) {
            	$scope.cofirmDeleteCreateToastForm($index);
            }
            
            $scope.cofirmDeleteCreateToastForm = function(id){
    			$mdToast.show(
    					{  	templateUrl : clientwh.contextPath + '/view/toast.html',
    						hideDelay:5000,
    						controller  : 'clientwhpermissionAllowController',
    						position: 'right'}).then(function(response){
    							if (response) {
    								$scope.ruleCreate.splice(id, 1 );
    			                	$scope.allPermission(5);
    			                	$scope.disableItemInAvailable($scope.availableUser,$scope.tab);
    			                	$scope.showMessageOnToast($translate.instant('clientwh_home_deleted'))
								}
    						});
    		}
    		
            $scope.cofirmDeleteOwnerToastForm = function(id){
    			$mdToast.show(
    					{  	templateUrl : clientwh.contextPath + '/view/toast.html',
    						hideDelay:5000,
    						controller  : 'clientwhpermissionAllowController',
    						position: 'right'}).then(function(response){
    							if (response) {
    								$scope.ruleOwner.splice(id, 1 );
    			                	$scope.allPermission(6);
    			                	$scope.disableItemInAvailable($scope.availableUser,$scope.tab);
    			                	$scope.showMessageOnToast($translate.instant('clientwh_home_deleted'))
								}
    						});
    		}
            
    		$scope.OkToast = function() {
    	        $mdToast.hide(true);
    	    };
    		
    		$scope.closeToast = function() {
    	        $mdToast.hide(false);
    	      };
    	      
            // Remove item user in list owner
            $scope.removeRowOwner = function ($index) {
            	$scope.cofirmDeleteOwnerToastForm($index);
            }
            
            $scope.forCreate = {id:-1};
            
            // Load user for creat
            $scope.loadUserForCreat = function($index){
            	$scope.allPermission(5); // Clean data
            	$scope.forCreate.id = $index;
            	
            	var tmp = $scope.ruleCreate[$index];
            	
            	// Implement per object in json
            	var s = [tmp.createbyids, tmp.admins, tmp.reads, tmp.updates, tmp.deletes];
            	
            	// Array to store data
            	var selected = [$scope.selectedCreatCData, $scope.selectedAdminsCData, $scope.selectedReadsCData, $scope.selectedUpdatesCData, $scope.selectedDeletesCData];
            	
            	for (var j = 0; j < s.length; j++) {
                    var arrId = s[j];
                    if (arrId.length==0) {
                    	selected[j].push($scope.displayUsernameById(NUM_ALL));
					}
                    else {
                    	for (var k = 0; k < arrId.length; k++) {
                    		if (arrId[k]==NUM_EMPTY) {
                    			selected[j] = [];
							}
                    		else {
                    			selected[j].push($scope.displayUsernameById(arrId[k]));
                    		}
                        }
                    }
                }
            	$scope.checkConditionAddCreat();
            	$scope.disableItemInAvailable($scope.availableUser,$scope.tab);
            }
            
            $scope.forOwner = {id:-1};
            
            //Load user for owner
            $scope.loadUserForOwner = function($index){
            	$scope.allPermission(6); // Clean data
            	$scope.forOwner.id = $index;
            	
            	var tmp = $scope.ruleOwner[$index];
            	// Implement per object in json
            	var s = [tmp.ownerbyids, tmp.admins, tmp.reads, tmp.updates, tmp.deletes];
            	// Array to store data
            	var selected = [$scope.selectedOwnersOData, $scope.selectedAdminsOData, $scope.selectedReadsOData, $scope.selectedUpdatesOData, $scope.selectedDeletesOData];
            	
            	for (var j = 0; j < s.length; j++) {
                    var arrId = s[j];
                    if (arrId.length==0) {
                    	selected[j].push($scope.displayUsernameById(NUM_ALL));
					}
                    else {
                    	for (var k = 0; k < arrId.length; k++) {
                    		if (arrId[k]==NUM_EMPTY) {
                    			selected[j] = [];
							}
                    		else {
                    			selected[j].push($scope.displayUsernameById(arrId[k]));
                    		}
                        }
                    }
                }
            	$scope.checkConditionAddOwner();
            	$scope.disableItemInAvailable($scope.availableUser,$scope.tab);
            }
            
            // Add rule for owner
            $scope.addRuleOwner = function(){
            	$scope.ruleOwners = { "ownerbyids": [], "admins": [], "reads": [], "updates": [], "deletes": [] }
            	
            	// id array to get data
            	var idSelected = ['#selectedOwnersOData option', '#selectedAdminsOData option', '#selectedReadsOData option', '#selectedUpdatesOData option', '#selectedDeletesOData option'];
            	
            	// Array to store data
            	var s = [$scope.ruleOwners.ownerbyids, $scope.ruleOwners.admins, $scope.ruleOwners.reads, $scope.ruleOwners.updates, $scope.ruleOwners.deletes];
            	
            	if ($scope.forOwner.id > -1) {
            		$scope.ruleOwner.splice($scope.forOwner.id, 1 );
				} 
            	for (var i = 0; i < idSelected.length; i++) {
                     var optionValues = [];
                     $(idSelected[i]).each(function () {
                         optionValues.push($(this).val());
                     });
                     if (optionValues.length==0) {
                     	s[i].push(NUM_EMPTY);
 					}
                     else {
                     for (var j = 0; j < optionValues.length; j++) {
                         var id = parseInt(optionValues[j]);
                         if (id==NUM_ALL) {
                          	s[i] = []
      					}
                          else {
                          	s[i].push(id);
                          }
                     }
                     }
                 }
            	 var flag = false;
            	 for (var i = 0; i < $scope.ruleOwners.ownerbyids.length; i++) { // Check user exit in list owner
            		 flag = $scope.checUserExitInArrOwner($scope.ruleOwners.ownerbyids[i],$scope.ruleOwner);
            		 if (flag) {
            			$scope.showMessageOnToast($translate.instant('clientwh_permission_duplicate'))
						break;
					}
				}
            	
            	 if (flag==false&&$scope.ruleOwners.ownerbyids.length!=NUM_EMPTY) { // Check user denided
            		 if ($scope.forOwner.id > -1) {
            			 $scope.ruleOwner.splice($scope.forOwner.id, 0, $scope.ruleOwners);
					}
            		 else {
            			 $scope.ruleOwner.push($scope.ruleOwners);
                		 $scope.forOwner.id = Object.keys($scope.ruleOwner).length-1;
            		 }
            		$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
                 	$scope.disableItemInAvailable($scope.availableUser,$scope.tab);
				}
            	
            }
            
            // Add rules to permission
            $scope.addRules = function () {
                $scope.rules = { "admins": [], "creates": [], "reads": [], "updates": [], "deletes": [], "adminoncreates": [], "adminonowners": [], "createbyids": [], "ownerbyids": [] };
                // id array to get data
                var idSelected = ['#selectedAdmins option', '#selectedCreates option','#selectedReads option',  '#selectedUpdates option', '#selectedDeletes option', '#selectedAdOnCreate option', '#selectedAdOnOwner option'];
                // Array to store data
                var s = [$scope.rules.admins, $scope.rules.creates, $scope.rules.reads, $scope.rules.updates, $scope.rules.deletes, $scope.rules.adminoncreates, $scope.rules.adminonowners];
                if (Object.keys($scope.ruleCreate).length==0) {
                	
                	$scope.ruleCreat = { "createbyids": [], "admins": [],  "reads": [], "updates": [], "deletes": [] }
                	// id array to get data
                	var idSelectedCreate = ['#selectedCreatCData option', '#selectedAdminsCData option', '#selectedReadsCData option',  '#selectedUpdatesCData option', '#selectedDeletesCData option'];
                	// Array to store data
                	var sCreate = [$scope.ruleCreat.createbyids, $scope.ruleCreat.admins, $scope.ruleCreat.reads, $scope.ruleCreat.updates, $scope.ruleCreat.deletes];
                	
                	for (var i = 0; i < idSelectedCreate.length; i++) {
                        var optionValues = [];
                        $(idSelectedCreate[i]).each(function () {
                            optionValues.push($(this).val());
                        });
                        if (optionValues.length==0) {
                        	sCreate[i].push(NUM_EMPTY);
    					}
                        else {
                        	 for (var j = 0; j < optionValues.length; j++) {
                                 var id = parseInt(optionValues[j]);
                                 if (id==NUM_ALL) {
                                	 sCreate[i] = []
             					}
                                 else {
                                	 sCreate[i].push(id);
                                 }
                             }
                        }
                    }
                	$scope.ruleCreate.push($scope.ruleCreat);
				} 
                
                if (Object.keys($scope.ruleOwner).length==0) {
                	$scope.ruleOwners = { "ownerbyids": [], "admins": [], "reads": [], "updates": [], "deletes": [] }
                	// id array to get data
                	var idSelectedOwner = ['#selectedOwnersOData option', '#selectedAdminsOData option', '#selectedReadsOData option', '#selectedUpdatesOData option', '#selectedDeletesOData option'];
                	// Array to store data
                	var sOwner = [$scope.ruleOwners.ownerbyids, $scope.ruleOwners.admins, $scope.ruleOwners.reads, $scope.ruleOwners.updates, $scope.ruleOwners.deletes];
                	
                	for (var i = 0; i < idSelectedOwner.length; i++) {
                        var optionValues = [];
                        $(idSelectedOwner[i]).each(function () {
                            optionValues.push($(this).val());
                        });
                        if (optionValues.length==0) {
                        	sOwner[i].push(NUM_EMPTY);
    					}
                        else {
                        	 for (var j = 0; j < optionValues.length; j++) {
                                 var id = parseInt(optionValues[j]);
                                 if (id==NUM_ALL) {
                                	 sOwner[i] = []
             					}
                                 else {
                                	 sOwner[i].push(id);
                                 }
                             }
                        }
                    }
                	$scope.ruleOwner.push($scope.ruleOwners);
				}
                
                // Add rule to json createids
                $scope.rules.createbyids =  angular.copy($scope.ruleCreate);
                
                // Add rule to json ownerids
                $scope.rules.ownerbyids = angular.copy($scope.ruleOwner);
                
                // Add rules
                for (var i = 0; i < idSelected.length; i++) {
                    var optionValues = [];
                    $(idSelected[i]).each(function () {
                        optionValues.push($(this).val());
                    });
                    if (optionValues.length==0) {
                    	s[i].push(NUM_EMPTY);
					}
                    else {
                    	 for (var j = 0; j < optionValues.length; j++) {
                             var id = parseInt(optionValues[j]);
                             if (id==NUM_ALL) {
                             	s[i] = []
         					}
                             else {
                             	s[i].push(id);
                             }
                         }
                    }
                }
            }
            
            // Default denied permission for data
            $scope.defaultDenided = function(){
            	$scope.allPermission(2);
            	$scope.allPermission(3);
            }
            //Change primary tab
            $scope.changePrimaryTab = function(numTab){
            	if(numTab==NUM_TAB_ADMIN){
            		$('.nav-tabs a[href="#home"]').tab('show');
            		$scope.changeTab(NUM_TAB_ADMIN);
            	}
            	if(numTab==1){ //Tab admin
            		$scope.selectedIndexCreate = 0;
            		$('.nav-tabs a[href="#creatCData"]').tab('show');
            		$scope.changeTab(NUM_TAB_CREATE_CREAT);
            	}
            	if(numTab==2){ //Tab creat
            		$('.nav-tabs a[href="#creatCData"]').tab('show');
            		$scope.changeTab(NUM_TAB_CREATE_CREAT);
            	}
            	if(numTab==3){ // Tab owner
            		$('.nav-tabs a[href="#ownerOData"]').tab('show');
            		$scope.changeTab(NUM_TAB_OWNER_OWNER);
            	}
            }
            
            // Change tab number
            $scope.changeTab = function (numTab) {
                $scope.tab = numTab;
                $scope.disableItemInAvailable($scope.availableUser,numTab);
            }

            // Return array of tab by tab number
            $scope.choseArr = function (numTab) {
                var s = [$scope.selectedAdmins, $scope.selectedReads, $scope.selectedCreates, $scope.selectedUpdates, $scope.selectedDeletes, $scope.selectedAdOnCreate, $scope.selectedAdOnOwner, $scope.selectedCreatCData, $scope.selectedAdminsCData, $scope.selectedReadsCData, $scope.selectedUpdatesCData, $scope.selectedDeletesCData, $scope.selectedOwnersOData, $scope.selectedAdminsOData, $scope.selectedReadsOData, $scope.selectedUpdatesOData, $scope.selectedDeletesOData];
                return s[numTab];
            }

            // Return model by tab number
            $scope.choseModel = function (numTab) {
                var m = [$scope.adminPermission, $scope.readsPermission, $scope.createPermission, $scope.updatePermission, $scope.deletePermission, $scope.adOnCreate, $scope.adOnOwner, $scope.creatCData, $scope.adminCData, $scope.readsCData, $scope.updateCData, $scope.deleteCData, $scope.ownerOData, $scope.adminOData, $scope.readsOData, $scope.updateOData, $scope.deleteOData];
                return m[numTab];
            }
            
            // Disable item in available
            $scope.disableItemInAvailable = function(listUserAvailable, numTab){
            	var arrSelected = $scope.choseArr(numTab);
            	delete listUserAvailable.display;
            	delete listUserAvailable.group;
            	if (numTab==NUM_TAB_CREATE_CREAT) {
            		for (var i = 0; i < listUserAvailable.length; i++) {
            			listUserAvailable[i].display = false;
                		listUserAvailable[i].group = null;
    					for (var j = 0; j < arrSelected.length; j++) {
    						if (parseInt(listUserAvailable[i].id)==parseInt(arrSelected[j].id)) {
    							listUserAvailable[i].display = true;	
    						}
    						else if (parseInt(arrSelected[j].id)==NUM_ALL){
    							listUserAvailable[i].display = true;
    						}
    					}
    					for (var j = 0; j < $scope.ruleCreate.length; j++) {
							var creatTmp = $scope.ruleCreate[j].createbyids;
							for (var k = 0; k < creatTmp.length; k++) {
									if (parseInt(listUserAvailable[i].id)==parseInt(creatTmp[k])) {
		    							listUserAvailable[i].display = true;
		    							if (creatTmp[k]!=NUM_EMPTY) {
		    								listUserAvailable[i].group = ' (N'+j+')';
										}
		    						}
							}
						}
    				}
				}
            	else if(numTab==NUM_TAB_OWNER_OWNER){
            		for (var i = 0; i < listUserAvailable.length; i++) {
            			listUserAvailable[i].display = false;
                		listUserAvailable[i].group = null;
    					for (var j = 0; j < arrSelected.length; j++) {
    						if (parseInt(listUserAvailable[i].id)==parseInt(arrSelected[j].id)) {
    							listUserAvailable[i].display = true;	
    						}
    						else if (parseInt(arrSelected[j].id)==NUM_ALL){
    							listUserAvailable[i].display = true;
    						}
    					}
    					for (var j = 0; j < $scope.ruleOwner.length; j++) {
							var ownerTmp = $scope.ruleOwner[j].ownerbyids;
							for (var k = 0; k < ownerTmp.length; k++) {
									if (parseInt(listUserAvailable[i].id)==parseInt(ownerTmp[k])) {
		    							listUserAvailable[i].display = true;
		    							if (ownerTmp[k]!=NUM_EMPTY) {
		    								listUserAvailable[i].group = ' (N'+j+')';
										}
		    						}
							}
						}
    				}
            	}
            	else {
            		for (var i = 0; i < listUserAvailable.length; i++) {
                		listUserAvailable[i].display = false;
                		listUserAvailable[i].group = null;
    					for (var j = 0; j < arrSelected.length; j++) {
    						if (parseInt(listUserAvailable[i].id)==parseInt(arrSelected[j].id)) {
    							listUserAvailable[i].display = true;	
    						}
    						else if (parseInt(arrSelected[j].id)==NUM_ALL){
    							listUserAvailable[i].display = true;
    						}
    					}
    				}
            	}
            }
            
            // Move all item
            $scope.moveAll = function (from) {
                for (var i = 0; i < from.length; i++) {
                	if ($scope.tab==NUM_TAB_ADMIN&&from[i]!=NUM_EMPTY) { //When add user in tab Admin
                		for (var k = NUM_TAB_ADMIN; k <= NUM_TAB_ADMIN_ON_OWNER; k++) { // Loop add user in permission for action
                			if ($scope.checkDupes(from[i].id, $scope.choseArr(k)) && from[i].id != NUM_ALL && $scope.checkDenied($scope.choseArr(k)) == false) {
                                $scope.choseArr(k).push(from[i]);
                            }
                		}
                	}
                	if ($scope.tab==NUM_TAB_CREATE_ADMIN&&from[i]!=NUM_EMPTY) { //When add user in tab Admin creat
                		for (var k = NUM_TAB_CREATE_ADMIN; k <= NUM_TAB_CREATE_DELETE; k++) { // Loop add user in permission for data creat
                			if ($scope.checkDupes(from[i].id, $scope.choseArr(k)) && from[i].id != NUM_ALL && $scope.checkDenied($scope.choseArr(k)) == false) {
                                $scope.choseArr(k).push(from[i]);
                            }
                		}
                	}
                	if ($scope.tab==NUM_TAB_OWNER_ADMIN&&from[i]!=NUM_EMPTY) { //When add user in tab Admin creat
                		for (var k = NUM_TAB_OWNER_ADMIN; k <= NUM_TAB_OWNER_DELETE; k++) { // Loop add user in permission for data owner
                			if ($scope.checkDupes(from[i].id, $scope.choseArr(k)) && from[i].id != NUM_ALL && $scope.checkDenied($scope.choseArr(k)) == false) {
                                $scope.choseArr(k).push(from[i]);
                            }
                		}
                	}
                	else if ($scope.checkDupes(from[i].id, $scope.choseArr($scope.tab)) && from[i].id != NUM_ALL && $scope.checkDenied($scope.choseArr($scope.tab)) == false) {
                        $scope.choseArr($scope.tab).push(from[i]);
                    }
                }
                $scope.checkConditionAddCreat();
                $scope.checkConditionAddOwner();
                $scope.disableItemInAvailable($scope.availableUser,$scope.tab);
            }

            // Check have denied is exit in arr
            $scope.checkDenied = function (arr) {
                for (var i = 0; i < arr.length; i++) {
                    if (arr[i].id == NUM_ALL) {
                        return true;
                    }
                }
                return false;
            }
            
            // Check user admin is exit in arr
            $scope.checkAdmin = function(arrAdmin,arrUser,tab){
            	for (var i = 0; i < arrAdmin.length; i++) {
						if (arrAdmin[i].id==parseInt(arrUser)&&tab!=$scope.returnTabAdmin($scope.tab)&&parseInt(arrUser)!=NUM_ALL) {
							alert('User is admin');
							return true;
						}
				}
            	return false;
            }

            // Check duplicate
            $scope.checkDupes = function (item, arr) {
                for (var i = 0; i < arr.length; i++) {
                    if (arr[i].id == parseInt(item)) {
                        return false;
                    }
                }
                return true;
            }
            
            // Return tab admin 
            $scope.returnTabAdmin = function(tab){
            	var tabAdmin = NUM_TAB_ADMIN;
            	if (tab>=NUM_TAB_ADMIN&&tab<=NUM_TAB_ADMIN_ON_OWNER) {
            		tabAdmin = NUM_TAB_ADMIN;
				}
            	if (tab>=NUM_TAB_CREATE_ADMIN&&tab<=NUM_TAB_CREATE_DELETE) {
            		tabAdmin = NUM_TAB_CREATE_ADMIN;
				}
            	if (tab>=NUM_TAB_OWNER_ADMIN&&tab<=NUM_TAB_OWNER_DELETE) {
            		tabAdmin = NUM_TAB_OWNER_ADMIN;
				}
            	return tabAdmin;
            }
            
            // Remove item
            $scope.remove = function () {
                var item = $scope.choseModel($scope.tab);
                if (item != null) {
                    for (var i = 0; i < item.length; i++) {
                    	if ($scope.checkAdmin($scope.choseArr($scope.returnTabAdmin($scope.tab)),item[i],$scope.tab)==false) {
                    		 var id = parseInt(item[i]);
                             var index = $scope.choseArr($scope.tab).findIndex(x => x.id === id);
                             $scope.choseArr($scope.tab).splice(index, 1);
    					}
                    }
                }
                $scope.checkConditionAddCreat();
                $scope.checkConditionAddOwner();
                $scope.disableItemInAvailable($scope.availableUser,$scope.tab);
            }

            // RemoveAll
            $scope.removeAll = function () {
                $scope.choseArr($scope.tab).splice(0, $scope.choseArr($scope.tab).length);
                var arrUserAdmin = $scope.choseArr($scope.returnTabAdmin($scope.tab));
                for (var i = 0; i < arrUserAdmin.length; i++) {
                	if (arrUserAdmin[i].id!=NUM_ALL&&$scope.tab!=NUM_TAB_CREATE_ADMIN&&$scope.tab!=NUM_TAB_OWNER_ADMIN) {
                		$scope.choseArr($scope.tab).push(arrUserAdmin[i]);
					}
				}
                $scope.checkConditionAddCreat();
                $scope.checkConditionAddOwner();
                $scope.disableItemInAvailable($scope.availableUser,$scope.tab);
            }

            // move item to tab list
            $scope.moveItem = function (item, from) {
                if (item != null) {
                    for (var i = 0; i < item.length; i++) {
                        for (var j = 0; j < from.length; j++) {
                            if (item[i] == from[j].id) {
                            	if ($scope.tab==NUM_TAB_ADMIN&&item[i]!=NUM_ALL) {
                            		for (var k = NUM_TAB_ADMIN; k <= NUM_TAB_ADMIN_ON_OWNER; k++) {
                            			 if ($scope.checkDupes(item[i], $scope.choseArr(k)) && $scope.checkDenied($scope.choseArr(k)) == false) {
                                             if (item[i] == NUM_ALL) {
                                                 $scope.choseArr(k).splice(0, $scope.choseArr(k).length);
                                                 $scope.choseArr(k).push(from[j]);
                                             }
                                             else {
                                                 $scope.choseArr(k).push(from[j]);
                                             }
                                         }
                            			 if ($scope.checkDenied($scope.choseArr(k))&&$scope.tab==$scope.returnTabAdmin($scope.tab)) {
                            				 $scope.choseArr(k).splice(0, $scope.choseArr(k).length);
                            				 $scope.choseArr(k).push(from[j]);
										}
                                         else {
                                             console.log('User is exists');
                                         }
									}
								}
                            	if ($scope.tab==NUM_TAB_CREATE_ADMIN&&item[i]!=NUM_ALL) {
                            		for (var k = NUM_TAB_CREATE_ADMIN; k <= NUM_TAB_CREATE_DELETE; k++) {
                            			 if ($scope.checkDupes(item[i], $scope.choseArr(k)) && $scope.checkDenied($scope.choseArr(k)) == false) {
                                             if (item[i] == NUM_ALL) {
                                                 $scope.choseArr(k).splice(0, $scope.choseArr(k).length);
                                                 $scope.choseArr(k).push(from[j]);
                                             }
                                             else {
                                                 $scope.choseArr(k).push(from[j]);
                                             }
                                         }
                            			 if ($scope.checkDenied($scope.choseArr(k))&&$scope.tab==$scope.returnTabAdmin($scope.tab)) {
                            				 $scope.choseArr(k).splice(0, $scope.choseArr(k).length);
                            				 $scope.choseArr(k).push(from[j]);
										}
                                         else {
                                             console.log('User is exists');
                                         }
									}
								}
                            	if ($scope.tab==NUM_TAB_OWNER_ADMIN&&item[i]!=NUM_ALL) {
                            		for (var k = NUM_TAB_OWNER_ADMIN; k <= NUM_TAB_OWNER_DELETE; k++) {
                            			 if ($scope.checkDupes(item[i], $scope.choseArr(k)) && $scope.checkDenied($scope.choseArr(k)) == false) {
                                             if (item[i] == NUM_ALL) {
                                                 $scope.choseArr(k).splice(0, $scope.choseArr(k).length);
                                                 $scope.choseArr(k).push(from[j]);
                                             }
                                             else {
                                                 $scope.choseArr(k).push(from[j]);
                                             }
                                         }
                            			 if ($scope.checkDenied($scope.choseArr(k))&&$scope.tab==$scope.returnTabAdmin($scope.tab)) {
                            				 $scope.choseArr(k).splice(0, $scope.choseArr(k).length);
                            				 $scope.choseArr(k).push(from[j]);
										}
                                         else {
                                             console.log('User is exists');
                                         }
									}
								}
                            	else if ($scope.checkDupes(item[i], $scope.choseArr($scope.tab)) && $scope.checkDenied($scope.choseArr($scope.tab)) == false) {
                                    if (item[i] == NUM_ALL) {
                                        $scope.choseArr($scope.tab).splice(0, $scope.choseArr($scope.tab).length);
                                        $scope.choseArr($scope.tab).push(from[j]);
                                        if($scope.tab==NUM_TAB_CREATE_CREAT){
                                        	$scope.ruleCreate = [];
                                        }
                                        if($scope.tab==NUM_TAB_OWNER_OWNER){
                                        	$scope.ruleOwner = [];
                                        }
                                    }
                                    else {
                                        $scope.choseArr($scope.tab).push(from[j]);
                                    }
                                }
                                else {
                                    console.log('User is exists');
                                }
                            }
                        }
                    }
                }
                $scope.checkConditionAddCreat();
                $scope.checkConditionAddOwner();
                $scope.disableItemInAvailable($scope.availableUser,$scope.tab);
            };
            
            $scope.activeTarget = function(target){
            	for (var i = 0; i < $scope.ArrTarget.length; i++) {
					if ($scope.ArrTarget[i].value===target) {
						$scope.ArrTarget[i].active = true;
					}
					else {
						$scope.ArrTarget[i].active = false;
					}
				}
            }
            //Change target
            $scope.changeTarget = function (target) {
                $scope.permission.target = target;
                $scope.activeTarget(target);
                $scope.allPermission(4);
                $scope.loadPermission(target);
                $scope.disableItemInAvailable($scope.availableUser,$scope.tab);
                $scope.changePrimaryTab(NUM_TAB_ADMIN);
                $scope.selectedIndexPermissionForAction = 0;
            }

            // All permission
            $scope.allPermission = function (tabNumber) {
            	var s = [];
            	if(tabNumber==1){
            		s = [$scope.selectedAdmins, $scope.selectedReads, $scope.selectedCreates, $scope.selectedUpdates, $scope.selectedDeletes, $scope.selectedAdOnCreate, $scope.selectedAdOnOwner];
            	}
                if(tabNumber==2){
                	s = [$scope.selectedCreatCData, $scope.selectedAdminsCData, $scope.selectedReadsCData, $scope.selectedUpdatesCData, $scope.selectedDeletesCData];
                	$scope.forCreate.id = {id:-1};
                	$scope.ruleCreate = [];
                    $scope.checkConditionAddCreat();
                }
                if(tabNumber==3){
                	s = [$scope.selectedOwnersOData, $scope.selectedAdminsOData, $scope.selectedReadsOData, $scope.selectedUpdatesOData, $scope.selectedDeletesOData];
                	$scope.forOwner.id = {id:-1};
                	$scope.ruleOwner = [];
                    $scope.checkConditionAddOwner();
                }
                if(tabNumber==4){
                	s = [$scope.selectedAdmins, $scope.selectedReads, $scope.selectedCreates, $scope.selectedUpdates, $scope.selectedDeletes, $scope.selectedAdOnCreate, $scope.selectedAdOnOwner, $scope.selectedOwnersOData, $scope.selectedAdminsOData, $scope.selectedReadsOData, $scope.selectedUpdatesOData, $scope.selectedDeletesOData,$scope.selectedCreatCData, $scope.selectedAdminsCData, $scope.selectedReadsCData, $scope.selectedUpdatesCData, $scope.selectedDeletesCData];
                }
                if(tabNumber==5){
                	s = [$scope.selectedCreatCData, $scope.selectedAdminsCData, $scope.selectedReadsCData, $scope.selectedUpdatesCData, $scope.selectedDeletesCData];
                	$scope.forCreate.id = {id:-1};
                    $scope.checkConditionAddCreat();
                }
                if(tabNumber==6){
                	s = [$scope.selectedOwnersOData, $scope.selectedAdminsOData, $scope.selectedReadsOData, $scope.selectedUpdatesOData, $scope.selectedDeletesOData];
                	$scope.forOwner.id = {id:-1};
                    $scope.checkConditionAddOwner();
                }
                for (var i = 0; i < s.length; i++) {
                    s[i].splice(0, s[i].length);
                }
                $scope.checkConditionAddCreat();
                $scope.checkConditionAddOwner();
                $scope.disableItemInAvailable($scope.availableUser,$scope.tab);
            }

            // All denided
            $scope.allDenied = function (tabNumber) {
            	var s = [];
            	if(tabNumber==1){
            		s = [$scope.selectedAdmins, $scope.selectedReads, $scope.selectedCreates, $scope.selectedUpdates, $scope.selectedDeletes, $scope.selectedAdOnCreate, $scope.selectedAdOnOwner];
            	}
                if(tabNumber==2){
                	s = [$scope.selectedCreatCData, $scope.selectedAdminsCData, $scope.selectedReadsCData, $scope.selectedUpdatesCData, $scope.selectedDeletesCData];
                }
                if(tabNumber==3){
                	s = [$scope.selectedOwnersOData, $scope.selectedAdminsOData, $scope.selectedReadsOData, $scope.selectedUpdatesOData, $scope.selectedDeletesOData];
                }
                $scope.allPermission(tabNumber);
                for (var i = 0; i < s.length; i++) {
                    s[i].push({ "id": NUM_ALL, "username": "All" });
                }
                if($scope.tab==NUM_TAB_CREATE_CREAT){
                	$scope.ruleCreate = [];
                }
                if($scope.tab==NUM_TAB_OWNER_OWNER){
                	$scope.ruleOwner = [];
                }
                $scope.checkConditionAddCreat();
                $scope.checkConditionAddOwner();
                $scope.disableItemInAvailable($scope.availableUser,$scope.tab);
            }

            // All admin permission
            $scope.adminPer = function (item,tabNumber) {
            	var s = [];
            	if(tabNumber==1){
            		s = [$scope.selectedAdmins, $scope.selectedReads, $scope.selectedCreates, $scope.selectedUpdates, $scope.selectedDeletes, $scope.selectedAdOnCreate, $scope.selectedAdOnOwner];
            	}
                if(tabNumber==2){
                	s = [$scope.selectedCreatCData, $scope.selectedAdminsCData, $scope.selectedReadsCData, $scope.selectedUpdatesCData, $scope.selectedDeletesCData];
                }
                if(tabNumber==3){
                	s = [$scope.selectedOwnersOData, $scope.selectedAdminsOData, $scope.selectedReadsOData, $scope.selectedUpdatesOData, $scope.selectedDeletesOData];
                }
                if (item != null) {
                    for (var i = 0; i < s.length; i++) {
                        for (var j = 0; j < item.length; j++) {
                            for (var k = 0; k < $scope.availableUser.length; k++) {
                                if (item[j] == $scope.availableUser[k].id) {
                                    if ($scope.checkDupes(item[j], s[i]) && $scope.checkDenied(s[i]) == false) {
                                        if (item[j] != NUM_ALL) {
                                            s[i].push($scope.availableUser[k]);
                                        }
                                    }
                                    else {
                                        console.log('User is exists');
                                    }
                                }
                            }
                        }
                    }
                }
                $scope.checkConditionAddCreat();
                $scope.checkConditionAddOwner();
                $scope.disableItemInAvailable($scope.availableUser,$scope.tab);
            }
        }]);

});
