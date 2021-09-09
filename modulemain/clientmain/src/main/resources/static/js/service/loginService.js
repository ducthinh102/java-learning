define(['require', 'angular'], function (require, angular) {
app.aService('loginService', ['$http', '$httpParamSerializer',
    				function ($http, $httpParamSerializer) {
		// login.
		this.login = function(uname, pwd) {
			var data = {
					grant_type:"password",
					username: uname,
					password: pwd,
					client_id: "acme"
					};
			var request = {
					method: 'POST',
					url: clientmain.authenUrl + '/oauth/token',
					headers: {
					"Authorization": "Basic " + btoa("acme:acmesecret"),
					"Content-type": "application/x-www-form-urlencoded; charset=utf-8"
					},
					data: $httpParamSerializer(data)
					};
			
			return $http(request);
		};
		
		// Get user info by user name.
		this.getUserInfoByUsername = function(username) {
			var request = {
					method: 'GET',
					url: clientmain.authenUrl + '/users/getUserInfoByUsername/' + username
			}
			return $http(request);
		};
		
		// Get user info by server url.
		this.getUserInfoByServerUrlAndUsername = function(serverUrl, username) {
			var request = {
					method: 'GET',
					url: serverUrl + '/user/getUserInfoByUsername/' + username
			}
			return $http(request);
		};
					
    }]);

});
