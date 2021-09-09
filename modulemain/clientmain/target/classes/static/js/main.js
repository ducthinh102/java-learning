
var requireConfig = {
	//urlArgs: "bust=" + (new Date()).getTime(),
	baseUrl: '/clientmain',
	waitSeconds: 200,
	paths: {
        // Jquery
        'jquery': 'lib/jquery/jquery.min',

        // angular
        'angular': 'lib/angular/angular.min',
        'angular_aria': 'lib/angular/angular-aria.min',
        'angular_route': 'lib/angular/angular-route.min',
        'angular_cookie': 'lib/angular/angular-cookies.min',
        'angular-resource': 'lib/angular-resource/angular-resource.min',
        'angular-jwt': 'lib/angular-jwt/angular-jwt.min',
        'angular-storage': 'lib/angular-storage/angular-storage.min',

        //'angular_translate': 'lib/angular-translate/AngularTranslate',
        //'angular_translate_cookie': 'lib/angular-translate-storage-cookie.min',

        'angular_translate': 'lib/i18n/angular-translate.min',
        'angular_locale_en': 'lib/i18n/angular-locale_en',
        'angular_locale_vi': 'lib/i18n/angular-locale_vi',
        'angular_animate': 'lib/angular/angular-animate.min',
        
        'messageformat': 'lib/i18n/messageformat',
        'angular-sanitize': 'lib/i18n/angular-sanitize',
        'angular-translate-interpolation-messageformat': 'lib/i18n/angular-translate-interpolation-messageformat',
        'angular-translate-storage-cookie': 'lib/i18n/angular-translate-storage-cookie',
        'angular-translate-storage-local': 'lib/i18n/angular-translate-storage-local',
        'angular-translate-loader-url': 'lib/i18n/angular-translate-loader-url',
        'angular-translate-loader-static-files': 'lib/i18n/angular-translate-loader-static-files',
        'angular-translate-handler-log': 'lib/i18n/angular-translate-handler-log',
        'angular-translate-loader-partial': 'lib/i18n/angular-translate-loader-partial',
        

        // angular local storage
        'angular-local-storage': 'lib/angular/angular-local-storage',
        
        'bootstrap': 'lib/bootstrap/js/bootstrap.min',
        // Angular ui
        'angular_ui_bootstrap': 'lib/angular-ui/ui-bootstrap-tpls.min',
        'angular-ui-router': 'lib/angular-ui-router/angular-ui-router.min',

        'angular_ocLazyLoad': 'lib/ocLazyLoad/ocLazyLoad',
        
		'angular-messages': 'lib/angular/angular-messages',
        'angular-material': 'lib/angular-material/angular-material.min',
        
        'angular-ui-tree': 'lib/angular-ui-tree/angular-ui-tree.min',
        
        // gantt chart
        'moment': 'lib/moment/moment',
        'moment_en': 'lib/moment/locale/en',
        'moment_vi': 'lib/moment/locale/vi',
        'angular-native-dragdrop': 'lib/angular-native-dragdrop/draganddrop',
        'angular-moment': 'lib/angular-moment/angular-moment',
        'angular-gantt': 'lib/angular-gantt/assets/angular-gantt',
        'angular-gantt-plugins': 'lib/angular-gantt/assets/angular-gantt-plugins',
        'moment-range': 'lib/moment-range/moment-range',
        'jsplumb': 'lib/jsplumb/js/jsplumb',
        
        'bootstrap-filestyle': 'lib/bootstrap-filestyle/bootstrap-filestyle.min',
        
        'tinycolor2': 'lib/tinycolor2/tinycolor-min',
        'angularjs-color-picker': 'lib/angularjs-color-picker/angularjs-color-picker.min',
        
        'domReady': 'lib/domReady',
        'dirPagination': 'lib/paging/dirPagination',
        'ckeditor': 'lib/angular-ckeditor/ckeditor/ckeditor',
        'angular-ckeditor': 'lib/angular-ckeditor/angular-ckeditor',
        
        'sockjs': 'lib/socketjs/sockjs.min',
        'stomp': 'lib/ng-stomp/stomp.min',
        'ng-stomp': 'lib/ng-stomp/ng-stomp.min',
        'ng-stomp.standalone': 'lib/ng-stomp/ng-stomp.standalone.min',
        'numeric-directive': 'lib/angular-numeric/numeric-directive',
        'tmhDynamicLocale': 'lib/angular-dynamic-locale/tmhDynamicLocale.min',
        
        'app': 'js/app'
	},
	// Dependencies
	shim: {
        'angular': {
            exports: 'angular'
        },
        'jquery': {
            exports: '$'
        },
        'bootstrap': {
        	deps: ['angular']
        },

        'angular_ocLazyLoad': {
            deps: ['angular']
        },
       
        'angular_route': {
            deps: ['angular']
        },
        'angular_cookie': {
            deps: ['angular']
        },
        'angular-resource': {
            deps: ['angular']
        },
        'angular-jwt': {
            deps: ['angular']
        },
        'angular-storage': {
            deps: ['angular']
        },
        'angular_ui_bootstrap': {
            deps: ['angular']
        },
        'angular-ui-router': {
            deps: ['angular']
        },
        'angular_translate': {
            deps: ['angular']
        },
        'angular_locale_en': {
            deps: ['angular']
        },
        'angular_locale_vi': {
            deps: ['angular']
        },
        
        'angular-sanitize': {
            deps: ['angular']
        },
        'angular-translate-interpolation-messageformat': {
            deps: ['angular']
        },
        'angular-translate-storage-cookie': {
            deps: ['angular']
        },
        'angular-translate-storage-local': {
            deps: ['angular']
        },
        'angular-translate-loader-url': {
            deps: ['angular']
        },
        'angular-translate-loader-static-files': {
            deps: ['angular']
        },
        'angular-translate-handler-log': {
            deps: ['angular']
        },
        'angular-translate-loader-partial': {
            deps: ['angular', 'angular_translate']
        },
        
        'angular_animate': {
        	deps: ['angular']
        },
        'angular_aria': {
        	deps: ['angular']
        },
		'angular-messages': {
			deps: ['angular', 'angular_animate', 'angular_aria']
		},
        'angular-material': {
        	deps: ['angular', 'angular_animate', 'angular_aria', 'angular-messages']
        },
        'angular-local-storage': {
        	deps: ['angular']
        },
        'angular-ui-tree': {
        	deps: ['angular']
        },
        // gantt chart
        'moment': {
        	deps: ['angular']
        },
        'moment_en': {
        	deps: ['moment']
        },
        'moment_vi': {
        	deps: ['moment']
        },
        'angular-native-dragdrop': {
        	deps: ['angular', 'moment']
        },
        'angular-moment': {
        	deps: ['angular', 'angular-native-dragdrop']
        },
        'angular-gantt': {
        	deps: ['angular', 'angular-moment']
        },
        'angular-gantt-plugins': {
        	deps: ['angular', 'angular-gantt']
        },
        'moment-range': {
        	deps: ['angular', 'angular-gantt-plugins']
        },
        'jsplumb': {
        	deps: ['angular', 'moment-range']
        },
        'bootstrap-filestyle': {
        	deps: ['angular_ui_bootstrap']
        },
        'tinycolor2': {
        	deps: []
        },
        'angularjs-color-picker': {
    	   deps: ['angular', 'bootstrap', 'tinycolor2']
        },
        'dirPagination': {
        	deps: ['angular']
        },
        'ckeditor': {
        	deps: ['angular']
        },
        'angular-ckeditor': {
        	deps: ['angular']
        },
        'sockjs': {
        	deps: ['angular']
        },
        'stomp': {
        	deps: ['sockjs']
        },
        'ng-stomp': {
        	deps: ['sockjs', 'stomp']
        },
        'ng-stomp.standalone': {
        	deps: ['angular']
        },
        'numeric-directive': {
        	deps: ['angular']
        },
        'tmhDynamicLocale': {
        	deps: ['angular']
        },
        
        'app': {
            deps:
                ['angular_route', 'jquery', 'bootstrap'
                , 'angular_cookie'
                //, 'angular_aria'
                //, 'angular_animate'
                , 'angular-resource'
                , 'angular-storage'
                , 'angular-jwt'
                , 'angular_ocLazyLoad'
                , 'angular_translate'
                , 'angular_locale_en'
                , 'angular_locale_vi'
                , 'messageformat'
                
                ,'angular-sanitize'
                //, 'angular-translate-interpolation-messageformat'
                //, 'angular-translate-storage-cookie'
                //, 'angular-translate-storage-local'
                //, 'angular-translate-loader-url'
                //, 'angular-translate-loader-static-files'
                //, 'angular-translate-handler-log'
                , 'angular-translate-loader-partial'
                
                , 'angular-local-storage'
                , 'angular_ui_bootstrap'
                , 'angular-ui-router'
                , 'angular-material'

                , 'angular-ui-tree',
                // gantt chart
                'moment',
                //'angular-native-dragdrop',
                'angular-moment',
                //'angular-gantt',
                //'angular-gantt-plugins',
                'moment-range',
                'jsplumb',
                'stomp',
                //'bootstrap-filestyle',
                //'angularjs-color-picker',
                'numeric-directive',
                'tmhDynamicLocale'
                ]
        }
        
	}
	// default load.
	//, deps:['app']

};

require.config(requireConfig);
/*
// At the start. Load indexController.js file.
require(['indexController'], function () {
    angular.bootstrap(document, ['app']);
});
*/

require(['app'], function (app) {
	app.init();
});
