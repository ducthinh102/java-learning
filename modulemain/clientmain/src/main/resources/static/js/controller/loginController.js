define(['require', 'angular'], function (require, angular) {

	app.aController(clientmain.prefix + 'loginController', function($rootScope, $scope, $mdToast, $q, $state, $translate, $translatePartialLoader, store, loginService) {
		if(typeof(clientmain.translate.login) === 'undefined' || clientmain.translate.login.indexOf($translate.use()) < 0) {
			console.log(clientmain.translate.login);
			if(typeof(clientmain.translate.login) === 'undefined') {
				clientmain.translate.login = '';
			}
			clientmain.translate.login += $translate.use() + ';';
			$translatePartialLoader.addPart(clientmain.contextPath + '/js/common/message/login');
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
	    	console.log('clientmain_login_title');
		    $scope.title = $translate.instant('clientmain_login_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
		$translate.onReady().then(function() {
	    	console.log('login onReady');
	    	$scope.title = $translate.instant('clientmain_login_title');
	    	$translate.refresh();
	    });
		
		// The ngView content is reloaded.
		$scope.$on('$viewContentLoaded', function() {
			// Load layout.
		});
		
		$scope.showMessageOnToast = function(message){
			$mdToast.show(
					{ template: '<md-toast class="md-toast">' + message + '</md-toast>',
						hideDelay:3000,
						position: 'right'})
		}
		
        $scope.user = {
            username: null,
            password: null
        };
        
        $scope.login = function () {
            loginService.login($scope.user.username, $scope.user.password).then( //success(function (result, status, headers, config) {
            		// success.
            		function(response) {
            			if(response.status != httpStatus.code.OK) {
            				 $scope.showMessageOnToast($translate.instant('clientmain_login_invalid'));
            				return;
            			}
            			// Store token.
                        store.set('access_token', response.data.access_token);
                        // Store user info.
                        store.set(clientmain.USER_INFO, { username: $scope.user.username });
                        // Get user info.
                        var serverauthDeferred = loginService.getUserInfoByUsername($scope.user.username);
                        // servermain
                        var servermainDeferred = $q.defer()
                        if(clientmain.modules.clientmain.isuse) {
                            servermainDeferred = loginService.getUserInfoByServerUrlAndUsername(clientmain.baseUrl + '/servermain', $scope.user.username);
                        } else {
                        	servermainDeferred.resolve();
                        }
                        // serverbuilding
                        var serverbuildingDeferred = $q.defer()
                        if(clientmain.modules.clientbuilding.isuse) {
                            serverbuildingDeferred = loginService.getUserInfoByServerUrlAndUsername(clientmain.baseUrl + '/serverbuilding', $scope.user.username);
                        } else {
                        	serverbuildingDeferred.resolve();
                        }
                        // serverwh
                        var serverwhDeferred = $q.defer()
                        if(clientmain.modules.clientwh.isuse) {
                            serverwhDeferred = loginService.getUserInfoByServerUrlAndUsername(clientmain.baseUrl + '/serverwh', $scope.user.username);
                        } else {
                        	serverwhDeferred.resolve();
                        }
                        // serverhr
                        var serverhrDeferred = $q.defer()
                        if(clientmain.modules.clienthr.isuse) {
                            serverhrDeferred = loginService.getUserInfoByServerUrlAndUsername(clientmain.baseUrl + '/serverhr', $scope.user.username);
                        } else {
                        	serverhrDeferred.resolve();
                        }
                        // Responses.
                        $q.all([serverauthDeferred, servermainDeferred, serverbuildingDeferred, serverwhDeferred, serverhrDeferred]).then(
                        		// success.
                        		function(responses) {
                        			var userinfo = store.get(clientmain.USER_INFO);
                        			// serverauth.
                        			$rootScope.userdisplayname = responses[0].data.displayname;
                        			$rootScope.userthumbnail = responses[0].data.thumbnail;
                        			store.set('userdisplayname', $rootScope.userdisplayname);
                        			store.set('userthumbnail', $rootScope.userthumbnail);
                        			userinfo.info = responses[0].data.info;
                        			userinfo.modules = [];
        							// Add servermain module.
                        			if(clientmain.modules.clientmain.isuse) {
            							var servermain = { name: 'servermain', iduser: responses[1].data.iduser, info: responses[1].data.info}
            							userinfo.modules.push(servermain);
                        			}
        							// Add serverbuilding module.
                        			if(clientmain.modules.clientbuilding.isuse) {
            							var serverbuilding = { name: 'serverbuilding', iduser: responses[2].data.iduser, info: responses[2].data.info}
            							userinfo.modules.push(serverbuilding);
                        			}
        							// Add serverwh module.
                        			if(clientmain.modules.clientwh.isuse) {
            							var serverwh = { name: 'serverwh', iduser: responses[3].data.iduser, info: responses[3].data.info}
            							userinfo.modules.push(serverwh);
                        			}
        							// Add serverhr module.
                        			if(clientmain.modules.clienthr.isuse) {
            							var serverhr = { name: 'serverhr', iduser: responses[4].data.iduser, info: responses[4].data.info}
            							userinfo.modules.push(serverhr);
                        			}
        							// Update userinfo.
        							store.set(clientmain.USER_INFO, userinfo);

        	                        // Get notify.
        	                        $rootScope.getNotifies();
                        		},
                        		// error.
                        		function(responses) {
                        			// Remove user info from store.
                                    store.remove('userdisplayname');
                        			store.remove('userthumbnail');
                        			$rootScope.userdisplayname = undefined;
                        			$rootScope.userthumbnail = undefined;
                        		}
                        );
                        $scope.showMessageOnToast($translate.instant('clientmain_login_successfully'));
                        // Redirect to previous state.
                        $state.go($rootScope.redirectState);
            		},
            		// error.
            		function(response) {
            		}
            );
        };
				
		
	});	
});
