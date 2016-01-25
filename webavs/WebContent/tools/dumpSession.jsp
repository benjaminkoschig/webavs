<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ page 
language="java"
contentType="text/html; charset=ISO-8859-1"
%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta name="GENERATOR" content="IBM WebSphere Studio" />
<style>
td {
	border-top: 1px solid black;
	padding: 2px;
}

table {
	border: 0px;
	font-size: medium;
}
</style>
<title>dumpSession.jsp</title>
</head>
<body>
<h3>Objets en session</h3>
<table>
	<tr>
		<th>Nom en session (key)</th>
		<th>Nom de la classe</th>
		<th>Serializable</th>
	</tr>
<%
java.util.Enumeration attribs = session.getAttributeNames();
int totalSize = 0;
int totalObjs = 0;
int totalSerial = 0;
int totalNonSerial = 0;

while(attribs.hasMoreElements()) {
	totalObjs++;
	String elementName = (String)attribs.nextElement();
	Object element = session.getAttribute(elementName);
	Class elClass = element.getClass();
	boolean isSerializable = true;
	long serialSize = 0;
	String nonSerialDetail = "ok";
	try {
		serialSize = globaz.jade.client.util.JadeUtil.testSerializable(element);
		totalSize += serialSize;
		totalSerial ++;
	} catch (Exception e) {
		isSerializable = false;
		totalNonSerial ++;
		nonSerialDetail = e.toString();
	}
	String serializableStr = null;
	if (isSerializable) {
		serializableStr = "<span style='color: green'>serializable (" + serialSize + " bytes)</span>";
	} else {
		serializableStr = "<span style='color: red; font-weight: bold'>not serializable</span>";
	}
%>
	<tr>
		<td><%=elementName%></td>
		<td><%=elClass.getName()%></td>
		<td title="<%=nonSerialDetail%>"><%=serializableStr%></td>
	</tr>
<%
}
%>
	<tr style="font-weight: bold; font-size: large">
		<td>Total</td>
		<td><%=totalObjs%> obj (<span style='color: green'><%=totalSerial%></span> / <span style='color: red'><%=totalNonSerial%></span>)</td>
		<td><%=totalSize%> bytes</td>
	</tr>
</table>
</body>
</html>
