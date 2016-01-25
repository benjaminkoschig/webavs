<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@page import="globaz.jade.common.Jade"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page 
language="java"
contentType="text/html; charset=ISO-8859-1"
%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Content-Style-Type" content="text/css"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Cache-Control" content="no-cache" /> 
<title>selfClose.jsp</title>
<script type="text/javascript">
var time = <%=Jade.getInstance().getExecClientCommandSeconds()%>;
var timeStep = 1000;

function selfClose() {
	self.close();
}

function decrAndWait() {
	time -= 1;
	if (time < 1) {
		selfClose();
		return;
	}
	setCloseLinkText();
	setTimeout(decrAndWait, timeStep);
}

function setCloseLinkText() {
	var obj = document.getElementById("closeLink");
	obj.innerHTML="OK (" + time + ")";
	
}
</script>

<%
String command = (String)request.getAttribute("requestedCommand");
%>
</head>
<body onload="decrAndWait()">
<h3><ct:FWLabel key="FW_GED_EXECUTING"/></h3>
<p><%=command%></p>
<script type="text/vbscript">
Dim objShell
Set objShell = CreateObject("WScript.shell")
objShell.run "<%=command%>"
</script>
<p>
<a id="closeLink" href="javascript: selfClose()"></a>
</p>
</body>
</html>
