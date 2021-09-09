
clientwh.createFile = function(filename, filetype) {
    if (filetype == "js") { //if filename is a external JavaScript file
        var fileref = document.createElement('script');
        fileref.setAttribute("type","text/javascript");
        fileref.setAttribute("src", filename);
    }
    else if (filetype == "css"){ //if filename is an external CSS file
        var fileref = document.createElement("link");
        fileref.setAttribute("rel", "stylesheet");
        fileref.setAttribute("type", "text/css");
        fileref.setAttribute("href", filename);
    }
    return fileref
}
 
clientwh.loadFile = function(filename, filetype) {
	var targetelement = (filetype == "js") ? "script" : (filetype == "css") ? "link" : "none"; //determine element type to create nodelist using
	var allsuspects = document.getElementsByTagName(targetelement);
	var element = clientwh.createFile(filename, filetype);
	allsuspects[0].parentNode.append(element);
}

clientwh.replaceFile = function(oldfilename, newfilename, filetype) {
    var targetelement = (filetype == "js") ? "script" : (filetype == "css") ? "link" : "none"; //determine element type to create nodelist using
    var targetattr = (filetype == "js") ? "src" : (filetype == "css") ? "href" : "none"; //determine corresponding attribute to test for
    var allsuspects=document.getElementsByTagName(targetelement);
    for (var i = allsuspects.length; i >= 0; i--) { //search backwards within nodelist for matching elements to remove
        if (allsuspects[i] && allsuspects[i].getAttribute(targetattr) != null 
        		&& allsuspects[i].getAttribute(targetattr).indexOf(oldfilename) != -1) {
            var newelement = clientwh.createFile(newfilename, filetype);
            allsuspects[i].parentNode.replaceChild(newelement, allsuspects[i]);
        }
    }
}
 
//aReplacefile("oldscript.js", "newscript.js", "js") //Replace all occurences of "oldscript.js" with "newscript.js"
//aReplacefile("oldstyle.css", "newstyle", "css") //Replace all occurences "oldstyle.css" with "newstyle.css"

clientwh.removeFile = function(filename, filetype) {
    var targetelement = (filetype == "js") ? "script" : (filetype == "css") ? "link" : "none"; //determine element type to create nodelist from
    var targetattr = (filetype == "js") ? "src" : (filetype == "css") ? "href" : "none"; //determine corresponding attribute to test for
    var allsuspects = document.getElementsByTagName(targetelement);
    for (var i = allsuspects.length; i >= 0; i--) { //search backwards within nodelist for matching elements to remove
    if (allsuspects[i] && allsuspects[i].getAttribute(targetattr) != null 
    		&& allsuspects[i].getAttribute(targetattr).indexOf(filename) != -1)
        allsuspects[i].parentNode.removeChild(allsuspects[i]); //remove element by calling parentNode.removeChild()
    }
}


clientwh.loadSkin = function(skinName) {
	if(clientwh.currentSkin === '' || skinName === clientwh.prefix + 'admin') {
		clientwh.currentSkin = skinName;
		// Load js & css.
		clientwh.loadFile('https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css', 'css');
		clientwh.loadFile('http://code.ionicframework.com/ionicons/2.0.0/css/ionicons.min.css', 'css');
		clientwh.loadFile(clientwh.contextPath + '/lib/bootstrap/css/bootstrap.css', 'css');
		clientwh.loadFile(clientwh.contextPath + '/skin/admin/css/admin.min.css', 'css');
		clientwh.loadFile(clientwh.contextPath + '/skin/admin/css/_all-skins.min.css', 'css');
		clientwh.loadFile(clientwh.contextPath + '/skin/admin/css/skin-blue.css', 'css');
		clientwh.loadFile(clientwh.contextPath + '/skin/admin/js/admin.js', 'js');
	}
}

clientwh.removeSkin = function(skinName) {
	if(skinName === clientwh.prefix + 'admin') {
		clientwh.currentSkin = skinName;
		// Load js & css.
		clientwh.removeFile('https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css', 'css');
		clientwh.removeFile('http://code.ionicframework.com/ionicons/2.0.0/css/ionicons.min.css', 'css');
		clientwh.removeFile(clientwh.contextPath + '/lib/bootstrap/css/bootstrap.css', 'css');
		clientwh.removeFile(clientwh.contextPath + '/skin/admin/css/admin.min.css', 'css');
		clientwh.removeFile(clientwh.contextPath + '/skin/admin/css/_all-skins.min.css', 'css');
		clientwh.removeFile(clientwh.contextPath + '/skin/admin/css/skin-blue.css', 'css');
		clientwh.removeFile(clientwh.contextPath + '/skin/admin/js/admin.js', 'js');
	}
}


clientwh.showToast = function ($mdToast, htmlUrlTemplate, position, controller) {
    return $mdDialog.show({
        clickOutsideToClose: false,
        scope: $scope,
        preserveScope: true,
        templateUrl: htmlUrlTemplate,
        parent: angular.element(document.body),
        fullscreen: true,
        multiple: true,
        controller: function DialogController($scope, $mdDialog) {
			if(!$scope.closeDialog) {
				$scope.closeDialog = function () {
					$mdDialog.hide();
				}
			}
        },
        locals: {
        	params: params
        }
    });
}

clientwh.showDialog = function ($scope, $mdDialog, htmlUrlTemplate, params) {
    return $mdDialog.show({
        clickOutsideToClose: false,
        scope: $scope,
        preserveScope: true,
        templateUrl: htmlUrlTemplate,
        parent: angular.element(document.body),
        fullscreen: true,
        multiple: true,
        controller: function DialogController($scope, $mdDialog) {
			if(!$scope.closeDialog) {
				$scope.closeDialog = function () {
					$mdDialog.hide();
				}
			}
        },
        locals: {
        	params: params
        }
    });
}

clientwh.showDialogWithControllerName = function (controllerName, aliasName, $mdDialog, htmlUrlTemplate, params) {
    return $mdDialog.show({
        clickOutsideToClose: false,
        preserveScope: true,
        templateUrl: htmlUrlTemplate,
        parent: angular.element(document.body),
        fullscreen: true,
        multiple: true,
        controllerAs: aliasName,
        bindToController: true,
        controller: controllerName,
        locals: {
        	params: params
        }
    });
}

clientwh.transferList = function (pFromList, pToList, pIndex) {
    if (pIndex >= 0) {
        pToList.push(pFromList[pIndex]);
        pFromList.splice(pIndex, 1);
    } else {
        for (var i = 0; i < pFromList.length; i++) {
            pToList.push(pFromList[i]);
        }
        pFromList.length = 0;
    }
};

clientwh.Contains = function (pActualVal, pExpectedVal) {
    if (pActualVal === pExpectedVal)
        return true;
    if (!pActualVal)
        return false;
    if (!pExpectedVal)
        return true;
    return pActualVal.toString().toLowerCase().indexOf(pExpectedVal.toString().trim().toLowerCase()) !== -1;
}

clientwh.ContainInArray = function (pObjects, pObject, pPropertyName) {
    for (var i = 0; i < pObjects.length; i++) {
        if (pObjects[i][pPropertyName] == pObject[pPropertyName]) {
            return true;
        }
    }
    return false;
}

clientwh.ContaintByProperty = function (pObjects, pPropertyName, pValue) {
    if (pObjects) {
        for (var i = 0; i < pObjects.length; i++) {
            if (pObjects[i][pPropertyName] === pValue) {
                return true;
            }
        }
    }
    return false;
}

clientwh.GetByProperty = function (pObjects, pPropertyName, pPropertyValue) {
    for (var i = 0; i < pObjects.length; i++) {
        if (pObjects[i][pPropertyName] === pPropertyValue) {
            return pObjects[i];
        }
    }
    return undefined;
}

clientwh.IndexOfByProperty = function (pObjects, pPropertyName, pPropertyValue) {
    for (var i = 0; i < pObjects.length; i++) {
        if (pObjects[i][pPropertyName] === pPropertyValue) {
            return i;
        }
    }
    return -1;
}

clientwh.showPleaseWait = function(text) {
    var modalLoading = '<div class="modal" id="pleaseWaitDialog" data-backdrop="static" data-keyboard="false role="dialog">\
        <div class="modal-dialog">\
            <div class="modal-content">\
                <div class="modal-header">\
                    <h4 class="modal-title">'+text+'</h4>\
                </div>\
                <div class="modal-body">\
                    <div class="progress">\
                      <div class="progress-bar progress-bar-success progress-bar-striped active" role="progressbar"\
                      aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width:100%; height: 40px">\
                      </div>\
                    </div>\
                </div>\
            </div>\
        </div>\
    </div>';
    $(document.body).append(modalLoading);
    $("#pleaseWaitDialog").modal("show");
}

clientwh.hidePleaseWait = function() {
    $("#pleaseWaitDialog").modal("hide");
}