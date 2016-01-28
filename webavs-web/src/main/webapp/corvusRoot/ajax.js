// ajax.js

// newFunction

var req1;
var reqCount = 0;
var reqComleteAndOkHandler;

/*
 * query - the query to execute
 * handlerWhenEverythingIsOk - a function to call, with a param (response text from server)
 */

function doQuery(query, handlerWhenEverythingIsOk) {
	reqComleteAndOkHandler = handlerWhenEverythingIsOk;
	if (window.XMLHttpRequest) {
		req1 = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		req1 = new ActiveXObject("Microsoft.XMLHTTP");
	} else {
		return; // fall on our sword
	}
	req1.open('GET', query,true);
	req1.onreadystatechange = handlerAutoComplete;
	req1.send(null);
}

function handlerAutoComplete() {
	if (req1.readyState != 4) {
		return;
	}
	if ((req1.status == 0)||(req1.status == 200)) {
		var result = req1.responseText;
		reqComleteAndOkHandler(result);
	} else {
		alert(req1.status);
	}
}
