<!--hide this script from non-javascript-enabled browsers

function getWebAppName() {
  var pairs = top.document.location.pathname.split('/');
 

	
 var appName = "";

  for (var i=0; i<pairs.length - 1; i++) {
    appName += pairs[i] + '/';
  }

  return appName;
}

function getParams(d) {
  var idx = d.location.href.indexOf('?');
  var params = new Array();
  
  if (idx != -1) {
    var pairs = d.location.href.substring(idx+1, d.location.href.length).split('&');
    for (var i=0; i<pairs.length; i++) {
      nameVal = pairs[i].split('=');
      params[nameVal[0]] = nameVal[1];
    }
  }
  return params;
}

params = getParams(top.document);
paramsLocal = getParams(this.document);

function getParam(name) {
  if (unescape(params[name]) == 'undefined')
    return "";
  else
    return unescape(params[name]);
}

function getParamLocal(name) {
  if (unescape(paramsLocal[name]) == 'undefined')
    return "";
  else
    return unescape(paramsLocal[name]);
}

// stop hiding -->
