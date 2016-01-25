
  
  var req1;
  var autoCompleteElem;
  function autoComplete(elem,query) {
	autoCompleteElem = elem;
	if (event.keyCode== 40) { // curs DOWN
		if (window.XMLHttpRequest) req1 = new XMLHttpRequest();
			else if (window.ActiveXObject) req1 = new ActiveXObject("Microsoft.XMLHTTP");
			else return; // fall on our sword
		req1.open('GET', query,false);
		req1.onreadystatechange = handlerAutoComplete;
		req1.send(null);
	}
  }
  function handlerAutoComplete() {
	if (req1.readyState != 4) { 
		return;
	}
	if ((req1.status == 0)||(req1.status == 200)) {
		var result = req1.responseText;
		document.getElementById('zoneAutoComplete').style.display="block";
		document.getElementById('zoneAutoComplete').style.left=findPosX(autoCompleteElem);
		document.getElementById('zoneAutoComplete').style.top=findPosY(autoCompleteElem)
			+autoCompleteElem.offsetHeight;
		document.getElementById('zoneAutoComplete').style.width = autoCompleteElem.offsetWidth;
		document.getElementById('zoneAutoComplete').innerHTML=result;
		
		try {
		document.getElementById('autoCompleteList').focus();
		document.getElementById('autoCompleteList').selectedIndex =0;
		} catch (e) {}
		
	} else {
		alert(req1.status);
	}
  }
  function autoCompleteAction(elem) {
  	try {
  	document.getElementById('zoneAutoComplete').style.display="none";
  	autoCompleteElem.focus();
  	
  	if (event.keyCode== 13) { // ESC
			autoCompleteElem.value = elem.options[elem.selectedIndex].value;
  	}
  	} catch (e) {}
  }
  
  function findPosX(obj) {
	var curleft = 0;
	if (obj.offsetParent)
	{
	while (obj.offsetParent)
	{
	curleft += obj.offsetLeft
	obj = obj.offsetParent;
	}
	}
	else if (obj.x)
	curleft += obj.x;
	return curleft;
  }

  function findPosY(obj) {
	var curtop = 0;
	if (obj.offsetParent)
	{
	while (obj.offsetParent)
	{
	curtop += obj.offsetTop
	obj = obj.offsetParent;
	}
	}
	else if (obj.y)
	curtop += obj.y;
	return curtop;
}




