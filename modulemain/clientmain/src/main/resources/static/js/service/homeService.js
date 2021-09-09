

/**
 * Service for home
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientmain.prefix + 'homeService', function($http, $rootScope, store) {


	// Get notify by server url.
	this.listWithCriterasByIdreceiverAndPage = function(serverUrl, criterias, pageNo, pageSize, sorts) {
		var serverUrl = serverUrl + '/listWithCriterasByIdreceiverAndPage?' + 'page=' + pageNo + '&size=' + pageSize;
		if(typeof(sorts) !== 'undefined' && sorts.length > 0) {
			angular.forEach(sorts, function(sort) {
				serverUrl += '&' + sort;
			});
		}
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: criterias
				}
		return $http(request);
	};


});

});
