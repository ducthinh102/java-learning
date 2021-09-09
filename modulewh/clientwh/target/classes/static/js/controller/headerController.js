
define(['require', 'angular'], function (require, angular) {

	app.aController(clientwh.prefix + 'headerController', function($rootScope, $scope, $translate, $translatePartialLoader, $mdDateLocale, moment) {
		if(typeof(clientwh.translate.header) === 'undefined' || clientwh.translate.header.indexOf($translate.use()) < 0) {
			if(typeof(clientwh.translate.header) === 'undefined') {
				clientwh.translate.header = '';
			}
			clientwh.translate.header += $translate.use() + ';';
			$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/header');
		}
		
		var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
		    $scope.title = $translate.instant('clientwh_header_title');
		});
		// Unregister
		$scope.$on('$destroy', function () {
		    unRegister();
		});		
		$translate.onReady().then(function() {
	    });

		$scope.goto = function(state, params) {
			delete $rootScope.menuActiveTitle;
			$state.go(clientwh.prefix + state, params);
		}

		$scope.changeLanguage = function(language) {
			gLanguage = language;
			$translate.use(gLanguage);
			require(['moment_' + gLanguage], function(){
				$translate.use(gLanguage);
				$translate.refresh();
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
