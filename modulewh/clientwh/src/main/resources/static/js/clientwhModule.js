
var clientwh = {
		clientId: 1,
		name: 'clientwh',
		contextPath: '/clientwh',
		prefix: 'clientwh',
		//serverUrl: 'https://localhost:1111/serverwh',
		currentSkin: '',
		translate: {}
};


var clientwhModule;
define(['require', 'angular', 'bootstrap', 
	'angular-ui-tree', 
	'moment', 'angular-native-dragdrop', 'angular-moment', 'angular-gantt', 'angular-gantt-plugins', 'moment-range', 'jsplumb',
	'bootstrap-filestyle',
	'tinycolor2', 'angularjs-color-picker',
	'dirPagination', 'ckeditor', 'angular-ckeditor'
	], function (require, angular) {

	clientwhModule = angular.module(clientwh.name, ['ngResource', 'angular-storage',
		'angularUtils.directives.dirPagination', 'ckeditor',
		'ui.tree',
		// gantt chart.
		'gantt', 'gantt.tree', 'gantt.groups', 'gantt.dependencies', 'gantt.movable', 'gantt.drawtask',
		// color picker.
		'color.picker'
		]);
    
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
    					clientwh.contextPath + '/js/common/serverCode.js',
    					clientwh.contextPath + '/js/common/constant.js',
    					//clientwh.contextPath + '/js/common/httpStatus.js',
    					//clientwh.contextPath + '/js/util/validation_tooltip/directive.js',
    					//clientwh.contextPath + '/js/util/upload-files-directive.js',
    					//clientwh.contextPath + '/js/util/file-style-directive.js'
    					], function() {
    						//clientwh.loadFile(clientwh.contextPath + '/js/util/validation_tooltip/validation-tooltip.css', 'css');
    						//clientwh.loadFile(clientwh.contextPath + '/css/layout.css', 'css');
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
    
    
	// module run.
	clientwhModule.run(['$rootScope', '$location', function ($rootScope, $location) {
		console.log('clientwhModule.run');
		
		// Set server url.
		clientwh.serverUrl = $location.protocol() + '://' + $location.host() + ':' + $location.port() + '/serverwh';
		
		$rootScope.clientwhsidebarclick = function() {
			$('.main-sidebar').toggleClass('collapse');
			$('.main-content').toggleClass('expanse');
		};

		// Get current login user id
		var currentUserId = 0;
		var serverwhStore = $rootScope.modules.serverwh;
		if(serverwhStore)
			currentUserId = serverwhStore.iduser;
		
		// workflowexecute module
		$rootScope.constants = {
			workflowStatuses: clientwh.workflowStatuses,
			workflowTabs: clientwh.tabs,
			workflowexecuteView: clientwh.workflowexecuteView,
			currentUserId: currentUserId
		};

	}]);
	
	// return for require.
	return clientwhModule;
	
	
});