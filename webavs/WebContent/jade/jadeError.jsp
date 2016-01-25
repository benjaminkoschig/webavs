<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Content-Style-Type" content="text/css"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Cache-Control" content="no-cache" /> 
<link rel="stylesheet" type="text/css" href="../theme/master.css"/>
<title>Application Error</title>
</head>
<body>
<h1>Application Error</h1>
<%
	globaz.jade.servlet.http.JadeHttpErrorViewBean vb = (globaz.jade.servlet.http.JadeHttpErrorViewBean)globaz.jade.web.JadeWebUtil.getViewBean(request);
	if (vb != null) {
%>
	<table>
		<tr>
			<td valign="top" style="width: 120px"><b>Error id</b>:</td>
			<td valign="top"><%=vb.getId()%></td>
		</tr>
		<tr>
			<td valign="top" style="width: 120px"><b>Error date</b>:</td>
			<td valign="top"><%=vb.getDate()%></td>
		</tr>
		<tr>
			<td valign="top" style="width: 120px"><b>HTTP request</b>:</td>
			<td valign="top"><a href="<%=vb.getRequestUrl()%>"><%=vb.getRequestUrl()%></a></td>
		</tr>
		<tr>
			<td valign="top" style="width: 120px"><b>Error message</b>:</td>
			<td valign="top" style="color: red"><%=vb.getMessage()%></td>
		</tr>
		<tr>
			<td valign="top" style="width: 120px"><b>Error class</b>:</td>
			<td valign="top"><%=vb.getClassName()%></td>
		</tr>
		<tr>
			<td valign="top" style="width: 120px"><b>Stack trace</b>:</td>
			<td><textarea readonly="readonly" rows="40" cols="120"><%=vb.getStackTrace()%></textarea></td>
		</tr>
	</table>
<%
	} else {
%>
<p>No information on the error</p>
<%
	}
%>
</body>
</html>