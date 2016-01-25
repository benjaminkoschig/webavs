<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Console</title>

<script>
  var req
  function send(cmd) {
		if (window.XMLHttpRequest) req = new XMLHttpRequest();
			else if (window.ActiveXObject) req = new ActiveXObject("Microsoft.XMLHTTP");
			else return; // fall on our sword
		req.open('GET', '<%=request.getContextPath()%>/console?'+cmd,false);
		req.onreadystatechange = responseCallBack;
		req.send(null);
	
  }
  function responseCallBack() {
	if (req.readyState != 4) { 
		return;
	}
	if ((req.status == 0)||(req.status == 200)) {
		
		var result = eval('('+req.responseText+')'); // req.responseText;
		
		if (typeof result["message"] != "undefined") { 
			document.getElementById('message').innerHTML=result["message"];
		}
		if (typeof result["console"] != "undefined") { 
			document.getElementById('console').innerHTML=result["console"];
		}
		if (typeof result["sql"] != "undefined") { 
			document.getElementById('sql').innerHTML=result["sql"];
		}
	} else {
		alert(req.status);
	}
  }
  
 
 var clockID = 0;

function UpdateClock() {
   if(clockID) {
      clearTimeout(clockID);
      clockID  = 0;
   }
   send("update")
   clockID = setTimeout("UpdateClock()", 1000);
}
function startUpdate() {
   clockID = setTimeout("UpdateClock()", 500);
}
function killUpdate() {
   if(clockID) {
      clearTimeout(clockID);
      clockID  = 0;
   }
}

//-->

</script>
 
</head>
<body  onunload="killUpdate()">
<div style="background-color:#b3c4db;  padding : 0.2cm 0.2cm 0.2cm 0.2cm;">
	<input value="Start" type="button" onclick="javascript:startUpdate();send('start')">
	<input value="Stop" type="button" onclick="javascript:killUpdate();send('stop')">
	<input value="Update" type="button" onclick="javascript:send('start');send('update');">
	<input value="Clear" type="button" onclick="javascript:send('clear')">
	<SPAN id="message">&nbsp;</SPAN>
</div>
<div id="console" style="border:solid 1px silver; font-family:courier; padding : 0.2cm 0.2cm 0.2cm 0.2cm;"></div>
<div id="sql" style="background-color:#F0F0F0;border:solid 1px silver; font-family:courier; padding : 0.2cm 0.2cm 0.2cm 0.2cm;"></div>
</body>
</html>