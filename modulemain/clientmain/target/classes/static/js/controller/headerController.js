
define(['require', 'angular'], function (require, angular) {

	app.aController(clientmain.prefix + 'headerController', function($rootScope, $scope, $state, $translate, $translatePartialLoader, $mdDateLocale, moment) {
		if(typeof(clientmain.translate.header) === 'undefined' || clientmain.translate.header.indexOf($translate.use()) < 0) {
			if(typeof(clientmain.translate.header) === 'undefined') {
				clientmain.translate.header = '';
			}
			clientmain.translate.header += $translate.use() + ';';
			$translatePartialLoader.addPart(clientmain.contextPath + '/js/common/message/header');
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientmain_header_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
		$translate.onReady().then(function() {
	    });
		
/*
	    //I wrapped your refresh in the eval async function
	    $scope.$evalAsync(function() {
	      $translate.refresh();
	    });

	    $scope.dotranslate = function() {
	      $translate.refresh();
	    };
*/		//$scope.title = $translate.instant('clientmain_header_title');
	    
		
		$scope.goto = function(state, params) {
			delete $rootScope.menuActiveTitle;
			$state.go(clientmain.prefix + state, params);
		}

		$scope.changeLanguage = function(language) {
			gLanguage = language;
			require(['moment_' + gLanguage], function(){
				$translate.use(gLanguage);
				//$translate.refresh();
				// Change moment language.
			    moment.locale(gLanguage);
			    var localeData = moment.localeData();
			    $mdDateLocale.months      = localeData._months;
			    $mdDateLocale.shortMonths = moment.monthsShort();
			    $mdDateLocale.days        = localeData._weekdays;
			    $mdDateLocale.shortDays   = localeData._weekdaysMin;
			    $mdDateLocale.firstDayOfWeek = localeData._week.dow;
			});
		}
		
		
		
	});	
});
