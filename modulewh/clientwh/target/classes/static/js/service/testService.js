

/**
 * Service for Test
 **/

define(['require', 'angular'], function (require, angular) {
app.aService('testService', function($http, $rootScope) {
    
	// Upload attachments.
	this.uploadAttachments = function(attachments, id) {
        var filePath = "test";
		var request = {
            method: 'POST',
            url: clientwh.serverUrl + "/files/uploadAttachments" + "?filePath=" + encodeURIComponent(filePath) + "&id=" + id,
            data: attachments,
            headers: {
                'Content-Type': undefined
            }
        };
        return $http(request);
    }
	
	// Download attachments.
	this.downloadAttachments = function(id, fileName) {
		var filePath = "test";
		var url = $rootScope.makeGetURL('/files/downloadAttachments?filePath=' + encodeURIComponent(filePath) + "&id=" + id + "&fileName=" + encodeURIComponent(fileName));
		return $http.get(url, { responseType: 'arraybuffer' });
	}

});

});
