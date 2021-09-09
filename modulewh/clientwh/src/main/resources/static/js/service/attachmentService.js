
/**
 * Service for Attachment
 **/

define(['require', 'angular'], function (require, angular) {
app.aService(clientwh.prefix + 'attachmentService', function($http, $rootScope, $location) {
	
	// Create.
	this.create = function(attachment) {
		var serverUrl = clientwh.serverUrl + '/attachment/create';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: attachment
				}
		return $http(request);
	}
	
	// Create With File.
	this.createWithFile = function(attachment, file) {
		var formData = new FormData();
        formData.append('attachment', JSON.stringify(attachment));
        formData.append('file', file);
        var rootUrl= $location.protocol() + '://' + $location.host() + ':' + $location.port();
		var serverUrl = rootUrl + '/attachment/createWithFile';
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: formData,
				 headers: {
		                'Content-Type': undefined
		            }
				}
		return $http(request);
	}
	
	// Update Lock.
	this.updateLock = function(id) {
		var serverUrl = clientwh.serverUrl + '/attachment/updateLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update Unlock.
	this.updateUnlock = function(id) {
		var serverUrl = clientwh.serverUrl + '/attachment/updateUnlock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update.
	this.update = function(id, attachment) {
		var serverUrl = clientwh.serverUrl + '/attachment/update/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: attachment
				}
		return $http(request);
	}
	
	// Update With Lock.
	this.updateWithLock = function(id, attachment) {
		var serverUrl = clientwh.serverUrl + '/attachment/updateWithLock/' + id;
		var request = {
				 method: 'PUT',
				 url: serverUrl,
				 data: attachment
				}
		return $http(request);
	}
	
	// Update With File.
	this.updateWithFile = function(id, attachment, file) {
		var formData = new FormData();
        formData.append('attachment', JSON.stringify(attachment));
        formData.append('file', file);
		var rootUrl= $location.protocol() + '://' + $location.host() + ':' + $location.port();
		var serverUrl = rootUrl + '/attachment/updateWithFile/' + id;
		var request = {
				 method: 'POST',
				 url: serverUrl,
				 data: formData,
				 headers: {
		                'Content-Type': undefined
		            }
				}
		return $http(request);
	}
	
	// Update With File And Lock.
	this.updateWithFileAndLock = function(id, attachment, file) {
		var formData = new FormData();
        formData.append('attachment', JSON.stringify(attachment));
        formData.append('file', file);
        var rootUrl= $location.protocol() + '://' + $location.host() + ':' + $location.port();
		var serverUrl = rootUrl + '/attachment/updateWithFileAndLock/' + id;
		var request = {
				 method: 'POST',
				 url: serverUrl,
		         transformRequest: angular.identity,
				 headers: {
			                'Content-Type': undefined
			            },
				 data: formData
				}
		return $http(request);
	}
	
	// Update For Delete.
	this.updateForDelete = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/attachment/updateForDelete/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Update For Delete With Lock.
	this.updateForDeleteWithLock = function(id, version) {
		var serverUrl = clientwh.serverUrl + '/attachment/updateForDeleteWithLock/' + id + '/' + version;
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Delete.
	this.delete = function(id) {
		var serverUrl = clientwh.serverUrl + '/attachment/delete/' + id;
		var request = {
				 method: 'DELETE',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// Get by Id.
	this.getById = function(attachmentId) {
		var serverUrl = clientwh.serverUrl + '/attachment/getById/' + attachmentId;
		var request = {
				 method: 'GET',
				 url: serverUrl
				}
		return $http(request);
	}
	
	// List for page and filter.
	this.listWithCriteriasByPage = function(criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/attachment/listWithCriteriasByPage?' + 'page=' + pageNo + '&size=' + pageSize;
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
	}
	
	// List for page and filter.
	this.listWithCriteriasByIdrefAndReftypeAndPage = function(idref, reftype, criterias, pageNo, pageSize, sorts) {
		var serverUrl = clientwh.serverUrl + '/attachment/listWithCriteriasByIdrefAndReftypeAndPage/' + idref + '/' + reftype + '?page=' + pageNo + '&size=' + pageSize;
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
	}
	
	
	
	// Upload file.
	this.uploadFile = function(filePath, file) {
		var formData = new FormData();
        formData.append('filePath', filePath);
        formData.append('file', file);
        var rootUrl= $location.protocol() + '://' + $location.host() + ':' + $location.port();
		var serverUrl = rootUrl + '/files/uploadFile';
		var request = {
				 method: 'POST',
				 url: serverUrl,
		         transformRequest: angular.identity,
				 headers: {
			                'Content-Type': undefined
			            },
				 data: formData
				}
		return $http(request);
	}
	
	// Upload files.
	this.uploadFiles = function(filePath, files) {
		var formData = new FormData();
        formData.append('filePath', filePath);
        formData.append('files', files);
        var rootUrl= $location.protocol() + '://' + $location.host() + ':' + $location.port();
		var serverUrl = rootUrl + '/files/uploadFiles';
		var request = {
				 method: 'POST',
				 url: serverUrl,
		         transformRequest: angular.identity,
				 headers: {
			                'Content-Type': undefined
			            },
				 data: formData
				}
		return $http(request);
	}
	
	// Download file.
	this.downloadFile = function(fileFullName) {
		var rootUrl= $location.protocol() + '://' + $location.host() + ':' + $location.port();
		var serverUrl = clientwh.serverUrl + '/files/downloadFile?fileFullName=' + encodeURIComponent(fileFullName);
		return $http.get(serverUrl, { responseType: 'arraybuffer' });
	}
	
	// Update for delete file.
	this.updateForDeleteFile = function(fileFullName) {
		var serverUrl = clientwh.serverUrl + '/files/updateForDeleteFile?fileFullName=' + encodeURIComponent(fileFullName);
		var request = {
				 method: 'PUT',
				 url: serverUrl
				}
		return $http(request);
	}

});

});
