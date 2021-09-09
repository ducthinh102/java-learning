
clientmain.createFile = function(filename, filetype) {
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
 
clientmain.loadFile = function(filename, filetype) {
	var targetelement = (filetype == "js") ? "script" : (filetype == "css") ? "link" : "none"; //determine element type to create nodelist using
	var allsuspects = document.getElementsByTagName(targetelement);
	var element = clientmain.createFile(filename, filetype);
	allsuspects[0].parentNode.append(element);
}

clientmain.replaceFile = function(oldfilename, newfilename, filetype) {
    var targetelement = (filetype == "js") ? "script" : (filetype == "css") ? "link" : "none"; //determine element type to create nodelist using
    var targetattr = (filetype == "js") ? "src" : (filetype == "css") ? "href" : "none"; //determine corresponding attribute to test for
    var allsuspects=document.getElementsByTagName(targetelement);
    for (var i = allsuspects.length; i >= 0; i--) { //search backwards within nodelist for matching elements to remove
        if (allsuspects[i] && allsuspects[i].getAttribute(targetattr) != null 
        		&& allsuspects[i].getAttribute(targetattr).indexOf(oldfilename) != -1) {
            var newelement = clientmain.createFile(newfilename, filetype);
            allsuspects[i].parentNode.replaceChild(newelement, allsuspects[i]);
        }
    }
}
 
//aReplacefile("oldscript.js", "newscript.js", "js") //Replace all occurences of "oldscript.js" with "newscript.js"
//aReplacefile("oldstyle.css", "newstyle", "css") //Replace all occurences "oldstyle.css" with "newstyle.css"

clientmain.removeFile = function(filename, filetype) {
    var targetelement = (filetype == "js") ? "script" : (filetype == "css") ? "link" : "none"; //determine element type to create nodelist from
    var targetattr = (filetype == "js") ? "src" : (filetype == "css") ? "href" : "none"; //determine corresponding attribute to test for
    var allsuspects = document.getElementsByTagName(targetelement);
    for (var i = allsuspects.length; i >= 0; i--) { //search backwards within nodelist for matching elements to remove
    if (allsuspects[i] && allsuspects[i].getAttribute(targetattr) != null 
    		&& allsuspects[i].getAttribute(targetattr).indexOf(filename) != -1)
        allsuspects[i].parentNode.removeChild(allsuspects[i]); //remove element by calling parentNode.removeChild()
    }
}


clientmain.loadSkin = function(skinName) {
	if(clientmain.currentSkin === '' || skinName === clientmain.prefix + 'admin') {
		clientmain.currentSkin = skinName;
		// Load js & css.
		clientmain.loadFile('https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css', 'css');
		clientmain.loadFile('http://code.ionicframework.com/ionicons/2.0.0/css/ionicons.min.css', 'css');
		clientmain.loadFile(clientmain.contextPath + '/lib/bootstrap/css/bootstrap.css', 'css');
		clientmain.loadFile(clientmain.contextPath + '/skin/admin/css/admin.min.css', 'css');
		clientmain.loadFile(clientmain.contextPath + '/skin/admin/css/_all-skins.min.css', 'css');
		clientmain.loadFile(clientmain.contextPath + '/skin/admin/css/skin-blue.css', 'css');
		clientmain.loadFile(clientmain.contextPath + '/skin/admin/js/admin.js', 'js');
	}
}

clientmain.removeSkin = function(skinName) {
	if(skinName === clientmain.prefix + 'admin') {
		clientmain.currentSkin = skinName;
		// Load js & css.
		clientmain.removeFile('https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css', 'css');
		clientmain.removeFile('http://code.ionicframework.com/ionicons/2.0.0/css/ionicons.min.css', 'css');
		clientmain.removeFile(clientmain.contextPath + '/lib/bootstrap/css/bootstrap.css', 'css');
		clientmain.removeFile(clientmain.contextPath + '/skin/admin/css/admin.min.css', 'css');
		clientmain.removeFile(clientmain.contextPath + '/skin/admin/css/_all-skins.min.css', 'css');
		clientmain.removeFile(clientmain.contextPath + '/skin/admin/css/skin-blue.css', 'css');
		clientmain.removeFile(clientmain.contextPath + '/skin/admin/js/admin.js', 'js');
	}
}


clientmain.showDialog = function (scope, mdDialog, htmlUrlTemplate, params) {
    return mdDialog.show({
        clickOutsideToClose: false,
        scope: scope,
        preserveScope: true,
        templateUrl: htmlUrlTemplate,
        parent: angular.element(document.body),
        fullscreen: true,
        multiple: true,
		escapeToClose: false,
        controller: function DialogController($scope, $mdDialog) {
			if(!$scope.closeDialog) {
				$scope.closeDialog = function () {
					$mdDialog.hide();
				}
			}
        },
        bindToController: true,
        locals: {
        	params: params
        }
    });
}

clientmain.showDialogWithControllerName = function (controllerName, aliasName, mdDialog, htmlUrlTemplate, params) {
    return mdDialog.show({
        clickOutsideToClose: false,
        preserveScope: true,
        templateUrl: htmlUrlTemplate,
        parent: angular.element(document.body),
        fullscreen: true,
        multiple: true,
		escapeToClose: false,
        controllerAs: aliasName,
        controller: controllerName,
        bindToController: true,
        locals: {
        	params: params
        }
    });
}

clientmain.showDialogConfirm = function (mdDialog, htmlUrlTemplate, title, message) {
	var confirm = mdDialog.confirm({
		templateUrl: htmlUrlTemplate,
		controller: function($scope, $mdDialog) {
			$scope.title = title;
			$scope.message = message;
			
			$scope.ok = function() {
				$mdDialog.hide(true);
			};

			$scope.cancel = function() {
				$mdDialog.hide(false);
			};
		},
		multiple: true,
		escapeToClose: false,
		parent: angular.element(document.body)
	});
    return mdDialog.show(confirm);
}

clientmain.showDialogAlert = function (mdDialog, htmlUrlTemplate, title, message) {
	var confirm = mdDialog.alert({
		templateUrl: htmlUrlTemplate,
        controller: function($scope) {
        	$scope.title = title;
			$scope.message = message;
        },
		multiple: true,
		escapeToClose: false,
		parent: angular.element(document.body)
	});

    return mdDialog.show(confirm);
}

clientmain.transferList = function (pFromList, pToList, pIndex) {
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

clientmain.Contains = function (pActualVal, pExpectedVal) {
    if (pActualVal === pExpectedVal)
        return true;
    if (!pActualVal)
        return false;
    if (!pExpectedVal)
        return true;
    return pActualVal.toString().toLowerCase().indexOf(pExpectedVal.toString().trim().toLowerCase()) !== -1;
}

clientmain.ContainInArray = function (pObjects, pObject, pPropertyName) {
    for (var i = 0; i < pObjects.length; i++) {
        if (pObjects[i][pPropertyName] == pObject[pPropertyName]) {
            return true;
        }
    }
    return false;
}

clientmain.ContaintByProperty = function (pObjects, pPropertyName, pValue) {
    if (pObjects) {
        for (var i = 0; i < pObjects.length; i++) {
            if (pObjects[i][pPropertyName] === pValue) {
                return true;
            }
        }
    }
    return false;
}

clientmain.GetByProperty = function (pObjects, pPropertyName, pPropertyValue) {
    for (var i = 0; i < pObjects.length; i++) {
        if (pObjects[i][pPropertyName] === pPropertyValue) {
            return pObjects[i];
        }
    }
    return undefined;
}

clientmain.IndexOfByProperty = function (pObjects, pPropertyName, pPropertyValue) {
    for (var i = 0; i < pObjects.length; i++) {
        if (pObjects[i][pPropertyName] === pPropertyValue) {
            return i;
        }
    }
    return -1;
}


////////////////////////////////////////
////////////////////////////////////////
// Date and time:
////////////////////////////////////////
////////////////////////////////////////

clientmain.getDateIgnoreTime = function(date){
	return new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate()));
}
