
var clientwh = {
		name: 'clientwh',
		contextPath: '/clientwh',
		prefix: 'clientwh',
		//serverUrl: 'https://localhost:1111/serverwh',
		currentSkin: '',
		translate: {}
}


var gDateFormat = 'DD/MM/YYYY';
var gLanguage = 'vi';

// import require shim.
var app;
define(['require', 'angular', 'bootstrap', 
	'angular-ui-tree', 
	'moment', 'angular-native-dragdrop', 'angular-moment', 'angular-gantt', 'angular-gantt-plugins', 'moment-range', 'jsplumb',
	'bootstrap-filestyle',
	'tinycolor2', 'angularjs-color-picker',
	'dirPagination', 'ckeditor', 'angular-ckeditor'
	], function (require, angular) {
	
	app = angular.module(clientwh.name, ['ui.bootstrap', 'ui.router', 'ngAnimate', 'ngMaterial', 'ngMessages', 'oc.lazyLoad', 'pascalprecht.translate', 'ngCookies', 'ngResource', 'angular-storage', 'LocalStorageModule', 'angularMoment',
		'angularUtils.directives.dirPagination', 'ckeditor',
		'ui.tree',
		// gantt chart.
		'gantt', 'gantt.tree', 'gantt.groups', 'gantt.dependencies', 'gantt.movable', 'gantt.drawtask',
		// color picker.
		'color.picker'
		]);
	// init.
	app.init = function() {
		angular.bootstrap(document, [clientwh.name]);
	}
	
	app.config(['$httpProvider', '$locationProvider', '$controllerProvider', '$provide', '$compileProvider', '$stateProvider', '$urlRouterProvider', '$translateProvider', '$translatePartialLoaderProvider', '$mdDateLocaleProvider', 'moment',
		function($httpProvider, $locationProvider, $controllerProvider, $provide, $compileProvider, $stateProvider, $urlRouterProvider, $translateProvider, $translatePartialLoaderProvider, $mdDateLocaleProvider, moment) {
		
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

	    require(['moment_' + gLanguage], function(){
	        //$translateProvider.forceAsyncReload(true);
	        //$translateProvider.useLocalStorage();
	        //$translatePartialLoaderProvider.addPart('home');
	        //$translateProvider.useLoaderCache(true);
	        // Enable escaping of HTML          
	        $translateProvider.useSanitizeValueStrategy('sanitizeParameters');//escape, sanitizeParameters
	        // tell angular-translate to use your custom handler
	        
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
	    
	    // Default route.
	    $urlRouterProvider.otherwise(clientwh.contextPath + '/home');
        
        // Define route.
        app.aStateProvider
    	.state(clientwh.prefix + 'layout', {
    		abstract: true,
    		//url: '',
    		resolve: {
    			loadRequire: ['$ocLazyLoad', '$q', function($ocLazyLoad, $q) {
    				var deferred = $q.defer();
    				require([
    					clientwh.contextPath + '/js/util/commonUtil.js',
    					clientwh.contextPath + '/js/common/constant.js',
    					clientwh.contextPath + '/js/common/httpStatus.js',
    					clientwh.contextPath + '/js/util/validation_tooltip/directive.js',
    					clientwh.contextPath + '/js/util/upload-files-directive.js',
    					clientwh.contextPath + '/js/util/file-style-directive.js'
    					], function() {
    						clientwh.loadFile(clientwh.contextPath + '/js/util/validation_tooltip/validation-tooltip.css', 'css');
    						clientwh.loadFile(clientwh.contextPath + '/css/layout.css', 'css');
    						clientwh.loadFile(clientwh.contextPath + '/css/layoutwh.css', 'css');
    						deferred.resolve();
    				});
    				return deferred.promise;
    			}]
    		},
    		views: {
    			'layout': {
    				templateUrl: clientwh.contextPath + '/layout/layout.html'
    			}
    		}
    	})
    	.state(clientwh.prefix + 'main', {
    		abstract: true,
    		parent: clientwh.prefix + 'layout',
    		//url: '',
    		resolve: {
                loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                    var deferred = $q.defer();
                    require([
                    	clientwh.contextPath + '/js/controller/headerController.js',
                    	clientwh.contextPath + '/js/controller/leftController.js',
                    	clientwh.contextPath + '/js/controller/footerController.js'
                    	], function () {
                        $ocLazyLoad.inject(clientwh.name);
                        deferred.resolve();
                    });
                    
                    return deferred.promise;
                }]
            },
    		views: {
    			'header': {
    				templateUrl: clientwh.contextPath + '/layout/header.html',
    				controller: clientwh.prefix + 'headerController'
    			},
    			'left': {
    				templateUrl: clientwh.contextPath + '/layout/left.html',
    				controller: clientwh.prefix + 'leftController'
    			},
    			'content': {
    				templateUrl: clientwh.contextPath + '/layout/content.html'
    			},
    			'footer': {
    				templateUrl: clientwh.contextPath + '/layout/footer.html',
    				controller: clientwh.prefix + 'footerController'
    			}
    		}
    	})
    	.state(clientwh.prefix + 'home', {
    		parent: clientwh.prefix + 'main',
    		url: clientwh.contextPath + '/home',
    		resolve: {
                loadRequire: ['$ocLazyLoad', '$q', function ($ocLazyLoad, $q) {
                    var deferred = $q.defer();
                    require([
                    	clientwh.contextPath + '/js/service/userService.js',
                    	clientwh.contextPath + '/js/controller/homeController.js'
                    	], function () {
                    	require([clientwh.contextPath + '/js/route.js'], function () {
    	                    $ocLazyLoad.inject(clientwh.name);
    	                    deferred.resolve();
    	                });
                    });
                    
                    return deferred.promise;
                }]
            },
    		views: {
    			'container': {
    				templateUrl: clientwh.contextPath + '/view/home.html',
    				controller: clientwh.prefix + 'homeController'
    			}
    		}
    	});
	}]);
	
	// app run.
	app.run(['$rootScope', '$location', 'moment', function ($rootScope, $location, moment) {
		console.log('app.run');
		$rootScope.constants = {};
		// Set server url.
		clientwh.serverUrl = 'http://localhost:4445/serverwh';
		
		$rootScope.clientwhsidebarclick = function() {
			$('.main-sidebar').toggleClass('collapse');
			$('.main-content').toggleClass('expanse');
		};

		$rootScope.closeNav = function() {
		    document.getElementById("moduleNav").style.width = "0%";
		}
		
	}]);
	
	// return for require.
	return app;
	
});