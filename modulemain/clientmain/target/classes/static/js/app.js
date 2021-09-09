
var clientmain = {
		//authenUrl: 'https://localhost:1111',
		name: 'app',
		contextPath: '/clientmain',
		prefix: 'clientmain',
		modules: {
			clientmain: {
				contextPath: '/clientmain/',
				isuse: true
			},
			clientbuilding: {
				contextPath: '/clientbuilding/',
				isuse: true
			},
			clientwh: {
				contextPath: '/clientwh/',
				isuse: true
			},
			clienthr: {
				contextPath: '/clienthr/',
				isuse: true
			}
		},
		skin: {
			currentSkin: ''
		},
		translate: {}
}


var gDateFormat = 'DD/MM/YYYY';
var gLanguage = 'vi';

// import require shim.
var app;
define(['require', 'angular', 'bootstrap', 'dirPagination'
	], function (require, angular) {
	
	app = angular.module(clientmain.name, ['ui.bootstrap', 'ui.router', 'ngAnimate', 'ngMaterial', 'ngMessages', 'oc.lazyLoad', 'pascalprecht.translate', 'ngCookies', 'ngResource', 'angular-storage', 'angular-jwt', 'LocalStorageModule', 'angularMoment', 'ngSanitize',
		'angularUtils.directives.dirPagination', 'purplefox.numeric', 'tmh.dynamicLocale'
		]);
	// init.
	app.init = function() {
		angular.bootstrap(document, [clientmain.name]);
	}
	
	app.config(['$httpProvider', '$locationProvider', '$controllerProvider', '$provide', '$compileProvider', '$stateProvider', '$urlRouterProvider', '$translateProvider', '$translatePartialLoaderProvider', '$mdDateLocaleProvider', '$mdToastProvider', '$mdThemingProvider', 'moment',
		function($httpProvider, $locationProvider, $controllerProvider, $provide, $compileProvider, $stateProvider, $urlRouterProvider, $translateProvider, $translatePartialLoaderProvider, $mdDateLocaleProvider, $mdToastProvider, $mdThemingProvider, moment) {
		
		$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
		$httpProvider.defaults.headers.common['Accept'] = 'application/json';
		$httpProvider.defaults.headers["delete"] = {'Content-Type': 'application/json;charset=utf-8'};
		$httpProvider.defaults.headers.common['ConId'] = '0';

		//app.registerController = $controllerProvider.register;
		app.aProvide = $provide;
		
	    // Provider-based controller.
	    app.aController = function (name, constructor) {
	        $controllerProvider.register(name, constructor);
	        return (this);
	    };
	    // Provider-based service.
	    app.aService = function (name, constructor) {
	        $provide.service(name, constructor);
	        return (this);
	    };
	    // Provider-based factory.
	    app.aFactory = function (name, factory) {
	        $provide.factory(name, factory);
	        return (this);
	    };
	    // Provider-based value.
	    app.aValue = function (name, value) {
	        $provide.value(name, value);
	        return (this);
	    };
	    // Provider-based directive.
	    app.aDirective = function (name, factory) {
	        $compileProvider.directive(name, factory);
	        return (this);
	    };
	    // Provider-based state.
	    app.aStateProvider = $stateProvider;

	    // Provider-based urlRoute.
	    app.aUrlRouterProvider = $urlRouterProvider;
	    
	    app.register = {
	        controller: $controllerProvider.register,
	        service: $provide.service,
	        factory: $provide.factory,
	        value: $provide.value,
	        directive: $compileProvider.directive,
	        factory: $provide.factory,
	        urlRouterProvider: $urlRouterProvider
	        //filter: $filterProvider.register,
	        //animation: $animationProvider.register
	    };

		// $mdDateLocaleProvider
		app.mdDateLocaleProvider = $mdDateLocaleProvider;
	    
		// $translateProvider.
	    $translateProvider.useLoader('$translatePartialLoader', {
            urlTemplate: '{part}_{lang}.json'
        });
        $translateProvider.preferredLanguage(gLanguage);
        $translateProvider.fallbackLanguage(gLanguage);
        //$translateProvider.forceAsyncReload(true);
        //$translateProvider.useLocalStorage();
        //$translatePartialLoaderProvider.addPart('home');
        //$translateProvider.useLoaderCache(true);
        // Enable escaping of HTML          
        $translateProvider.useSanitizeValueStrategy('sanitizeParameters');//escape, sanitizeParameters
        $translateProvider.synchronizeLocale = true;
        // tell angular-translate to use your custom handler
        
	    require(['moment_' + gLanguage], function(){
	        // Change moment language.
	        moment.locale(gLanguage);
	        var localeData = moment.localeData();
	        $mdDateLocaleProvider.months      = localeData._months;
	        $mdDateLocaleProvider.shortMonths = moment.monthsShort();
	        $mdDateLocaleProvider.days        = localeData._weekdays;
	        $mdDateLocaleProvider.shortDays   = localeData._weekdaysMin;
	        $mdDateLocaleProvider.firstDayOfWeek = localeData._week.dow;
	        // parseDate
	        $mdDateLocaleProvider.parseDate = function(dateString) {
	          var m = moment(dateString, 'L', true);
	          return m.isValid() ? m.toDate() : new Date(NaN);
	        };
	        // formatDate
	        $mdDateLocaleProvider.formatDate = function(date) {
	          var m = moment(date);
	          return m.isValid() ? m.format('L') : '';
	        };
	    });
	    
	    // toastMessage.
	    $mdToastProvider.addPreset('toastMessage', {
	    	methods: ['textContent', 'action', 'templateUrl'],
	    	options: function() {
	    		return {
	    			textContent: '...',
	    			templateUrl: clientmain.contextPath + '/view/toast_message.html',
	    			controller: function($scope, $mdToast) {
	    				$scope.textContent = $scope.toast.textContent;
	    				$scope.action = $scope.toast.action;
	    				$scope.closeToast = function(){
	    					$mdToast.hide();
	    				}
	    			},
	    			bindToController: true,
	    			controllerAs: 'toast',
	    			position: 'top right',
	    			hideDelay: 3000
	    		};
	    	}
	    });
	    
	    // material theme.
	    $mdThemingProvider.theme('default').primaryPalette('indigo').accentPalette('pink').warnPalette('red');//.backgroundPalette('blue-grey');
        
	    // Interecepts every request sent from client application and stores token to Authorization header
        $httpProvider.interceptors.push('AuthenticationHttpInterceptor');
	    
	    // Default route.
	    $urlRouterProvider.otherwise(clientmain.contextPath + '/home');
        
        // Define route.
        app.aStateProvider
    	.state(clientmain.prefix + 'layout', {
    		abstract: true,
    		//url: '',
    		resolve: {
    			loadRequire: ['$ocLazyLoad', '$q', function($ocLazyLoad, $q) {
    				var deferred = $q.defer();
    				require([
    					clientmain.contextPath + '/js/util/commonUtil.js',
    					clientmain.contextPath + '/js/common/serverCode.js',
    					clientmain.contextPath + '/js/common/httpStatus.js',
    					clientmain.contextPath + '/js/common/constant.js',
    					clientmain.contextPath + '/js/util/validation_tooltip/directive.js',
    					clientmain.contextPath + '/js/util/upload-files-directive.js',
    					clientmain.contextPath + '/js/util/file-style-directive.js',
    					clientmain.contextPath + '/js/util/dualmultiselect-directive.js',
    					clientmain.contextPath + '/lib/jcubic-jquery.splitter/js/jquery.splitter-0.14.0.js'
    					], function() {
    						clientmain.loadFile(clientmain.contextPath + '/js/util/validation_tooltip/validation-tooltip.css', 'css');
    						clientmain.loadFile(clientmain.contextPath + '/css/layout.css', 'css');
    						deferred.resolve();
    				});
    				return deferred.promise;
    			}]
    		},
    		views: {
    			'layout': {
    				templateUrl: clientmain.contextPath + '/layout/layout.html'
    			}
    		}
    	})
    	.state(clientmain.prefix + 'main', {
    		abstract: true,
    		parent: clientmain.prefix + 'layout',
    		//url: '',
    		resolve: {
                loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                    var deferred = $q.defer();
                    require([
                    	clientmain.contextPath + '/js/controller/headerController.js',
                    	clientmain.contextPath + '/js/controller/leftController.js',
                    	clientmain.contextPath + '/js/controller/footerController.js'
                    	], function () {
                        $ocLazyLoad.inject(clientmain.name);
                        deferred.resolve();
                    });
                    
                    return deferred.promise;
                }]
            },
    		views: {
    			'header': {
    				templateUrl: clientmain.contextPath + '/layout/header.html',
    				controller: clientmain.prefix + 'headerController'
    			},
    			'left': {
    				templateUrl: clientmain.contextPath + '/layout/left.html',
    				controller: clientmain.prefix + 'leftController'
    			},
    			'content': {
    				templateUrl: clientmain.contextPath + '/layout/content.html'
    			},
    			'footer': {
    				templateUrl: clientmain.contextPath + '/layout/footer.html',
    				controller: clientmain.prefix + 'footerController'
    			}
    		}
    	})
    	.state(clientmain.prefix + 'home', {
    		parent: clientmain.prefix + 'main',
    		url: clientmain.contextPath + '/home',
    		resolve: {
                loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                    var deferred = $q.defer();
                    require([clientmain.contextPath + '/js/controller/homeController.js'], function () {
                    	require([clientmain.contextPath + '/js/route.js'], function () {
    	                    $ocLazyLoad.inject(clientmain.name);
    	                    deferred.resolve();
    	                });
                    });
                    
                    return deferred.promise;
                }]
            },
    		views: {
    			'container': {
    				templateUrl: clientmain.contextPath + '/view/home.html',
    				controller: clientmain.prefix + 'homeController'
    			}
    		}
    	});
        
	}]);
	
	app.service('AuthenticationHttpInterceptor', ['$rootScope', '$q', 'store',function($rootScope, $q, store) {
        this.request = function(config) {
    	    if(store.get('access_token')) {
                    config.headers.Authorization = 'Bearer ' + store.get('access_token');
                    var userinfo = store.get(clientmain.USER_INFO);
                    config.headers.userinfo = JSON.stringify(userinfo);
                    if(!$rootScope.loggedIn){
                        $rootScope.loggedIn = true;
                        // Get user info from store.
                        $rootScope.userdisplayname = store.get('userdisplayname');
            			$rootScope.userthumbnail = store.get('userthumbnail');
                    }
                    // Setting notify for module.
                    if(!$rootScope.modules && userinfo.modules){
                        $rootScope.modules = {};
                        // servermain.
                        var servermain = clientmain.GetByProperty(userinfo.modules, 'name', 'servermain');
                        if(servermain){
                        	$rootScope.modules.servermain = {iduser: servermain.iduser};
                        }
                        // serverbuilding.
                        var serverbuilding = clientmain.GetByProperty(userinfo.modules, 'name', 'serverbuilding');
                        if(serverbuilding){
                        	$rootScope.modules.serverbuilding = {iduser: serverbuilding.iduser};
                        }
                        // serverwh.
                        var serverwh = clientmain.GetByProperty(userinfo.modules, 'name', 'serverwh');
                        if(serverwh){
                        	$rootScope.modules.serverwh = {iduser: serverwh.iduser};
                        }
                        // serverhr.
                        var serverhr = clientmain.GetByProperty(userinfo.modules, 'name', 'serverhr');
                        if(serverhr){
                        	$rootScope.modules.serverhr = {iduser: serverhr.iduser};
                        }
            			// Connect notify.
            			$rootScope.notifyConnect();
                    }
                }
                return config;
            };
        this.responseError = function(error) {
        		if (error.status === 401 || error.status === 403) {
        			store.remove('access_token');
        			store.remove(clientmain.USER_INFO);
                    // Remove user info from store.
                    store.remove('userdisplayname');
        			store.remove('userthumbnail');
        			$rootScope.userdisplayname = undefined;
        			$rootScope.userthumbnail = undefined;
        			$rootScope.loggedIn = false;
        			// Show form login.
        			$rootScope.showLoginForm();
        			console.log(error);
        		}
        		return error;
        	}
        }]);
	
	app.run(['$rootScope', '$translate','$state', '$http', '$location', 'moment', '$mdToast', 'store', function ($rootScope, $translate, $state, $http, $location, moment, $mdToast, store) {
		console.log('app.run');
		$rootScope.constants = {};
		
		// Set server url.
		clientmain.serverUrl = $location.protocol() + '://' + $location.host() + ':' + $location.port() + '/servermain';
		
		// Set auth url.
		clientmain.baseUrl = $location.protocol() + '://' + $location.host() + ':' + $location.port();
		clientmain.authenUrl = $location.protocol() + '://' + $location.host() + ':' + $location.port();
		
		$rootScope.clientmainsidebarclick = function() {
			$('.main-sidebar').toggleClass('collapse');
			$('.main-content').toggleClass('expanse');
			$('.vsplitter').toggleClass('collapse');
			//$('.left_panel').toggleClass('collapse');
			$('.right_panel').toggleClass('collapse');
		};
		
		$rootScope.closeNav = function() {
		    document.getElementById("moduleNav").style.width = "0%";
		}
		
		$rootScope.openNav = function() {
		    document.getElementById("moduleNav").style.width = "100%";
		}
		$rootScope.showMessageOnToast = function(message){
			$mdToast.show(
					{ template: '<md-toast class="md-toast">' + message + '</md-toast>',
						hideDelay:3000,
						position: 'right'})
		}
		
		// Notify.
		$rootScope.notifyConnect = function() {
			var httpHeaders = {};
			var SockJS = require('sockjs');
			$rootScope.socket = new SockJS(clientmain.baseUrl + '/socketregistry?access_token=' + store.get('access_token'));
			$rootScope.stompClient = Stomp.over($rootScope.socket);
			$rootScope.stompClient.connect(httpHeaders, function(frame) {
				$rootScope.stompClient.subscribe('/topic/notify', function(response){
					var notify = JSON.parse(response.body);
					var count = 0;
					// servermain.
					if(notify.module == 'servermain' && notify.status == 1 && notify.idreceiver == $rootScope.modules.servermain.iduser){
						$rootScope.notifies.clientmain.count += 1;
						$rootScope.notifies.clientmain.content.push(notify);
						count++;
					}
					// serverbuilding.
					if(notify.module == 'serverbuilding' && notify.status == 1 && notify.idreceiver == $rootScope.modules.serverbuilding.iduser){
						$rootScope.notifies.clientbuilding.count += 1;
						$rootScope.notifies.clientbuilding.content.push(notify);
						count++;
					}
					// serverwh.
					if(notify.module == 'serverwh' && notify.status == 1 && notify.idreceiver == $rootScope.modules.serverwh.iduser){
						$rootScope.notifies.clientwh.count += 1;
						$rootScope.notifies.clientwh.content.push(notify);
						count++;
					}
					// serverhr.
					else if(notify.module == 'serverhr' && notify.status == 1 && notify.idreceiver == $rootScope.modules.serverhr.iduser){
						$rootScope.notifies.clienthr.count += 1;
						$rootScope.notifies.clienthr.content.push(notify);
						count++;
					}
					$rootScope.notifies.totalCount += count;
					$rootScope.$apply();
				});
			});
		}
		// Send.
		$rootScope.notifySend = function() {
			var httpHeaders = {};
			$rootScope.stompClient.send('/app/notify', httpHeaders, JSON.stringify({id: 1, name: '111'}));
		}
		$rootScope.notifyDisconnect = function() {
			$rootScope.stompClient.disconnect();
		}
		
		// Login
		$rootScope.showLoginForm = function() {
			$rootScope.redirectState = $state.current;
			$state.go(clientmain.prefix + 'login');
		}
		// Logout	
		$rootScope.logout = function () {
	        // Remove token on backend side
	        $http.get(clientmain.authenUrl + '/oauth/revoke-token').then(
	        	// success.
	        	function(response) {
	        		store.remove('access_token');
	        		store.remove(clientmain.USER_INFO);
                    // Remove user info from store.
                    store.remove('userdisplayname');
        			store.remove('userthumbnail');
        			$rootScope.userdisplayname = undefined;
        			$rootScope.userthumbnail = undefined;
	        		$rootScope.persistencePermission = null;
	        		$rootScope.loggedIn = false;
	    	        $state.go(clientmain.prefix + 'home');
	    	        // Disconnect notify.
	    	        $rootScope.notifyDisconnect();
	    	        // Clear notify.
					$rootScope.notifies = { totalCount: 0, clientwh: {cout: 0, content: []}, clienthr: {cout: 0, content: []} };
					$rootScope.showMessageOnToast($translate.instant('clientmain_home_successfully'));
	        	},
	        	// error.
	        	function(response) {
	        		
	        	}
	        );
		}

		$rootScope.clientmainloaded = true;
		// goto module.
		$rootScope.gotoModule = function(moduleName) {
			// show loader.
			$('.loaderContain').show();
			
			if(moduleName === 'clientmain') {
				$state.go(clientmain.prefix + 'home');
			} 
			else if(moduleName === 'clientbuilding') {
				require([clientmain.modules.clientbuilding.contextPath + 'js/clientbuildingModule.js'], function() {
					// crm has loaded.
					$rootScope.clientbuildingloaded = true;
					$state.go(clientbuilding.prefix + 'home');
				}, function(evt) {
					console.log('Require file faild.');
				});
			}
			else if(moduleName === 'clientwh') {
				require([clientmain.modules.clientwh.contextPath + 'js/clientwhModule.js'], function() {
					// crm has loaded.
					$rootScope.clientwhloaded = true;
					$state.go(clientwh.prefix + 'home');
				}, function(evt) {
					console.log('Require file faild.');
				});
			}
			else if(moduleName === 'clienthr') {
				require([clientmain.modules.clienthr.contextPath + 'js/clienthrModule.js'], function() {
					// crm has loaded.
					$rootScope.clienthrloaded = true;
					$state.go(clienthr.prefix + 'home');
				}, function(evt) {
					console.log('Require file faild.');
				});
			}
		}

	}]);
	
	// return for require.
	return app;
	
});
