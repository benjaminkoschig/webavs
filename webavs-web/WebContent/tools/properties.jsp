<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@page import="java.util.Properties"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Enumeration"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ page 
language="java"
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
<title>Properties</title>
</head>
<body>
<h3>Submit me!</h3>
<form action="" method="get"><input type="text" name="inputGet" value="getéàéà"/><input type="submit" value="getMe"> Résultat: <%=request.getParameter("inputGet") %></form>
<form action="" method="post"><input type="text" name="inputPost" value="postéàéà"/><input type="submit" value="postMe"> Résultat: <%=request.getParameter("inputPost") %></form>
<h3>Req/Resp</h3>
<ul>
	<li>request.getCharacterEncoding(): <%=request.getCharacterEncoding() %></li>
	<li>request.getContentType(): <%=request.getContentType() %></li>
	<li>request.getQueryString(): <%=request.getQueryString() %></li>
	<li>request.getLocale(): <%=request.getLocale() %></li>
	<li>response.getCharacterEncoding(): <%=response.getCharacterEncoding() %></li>
	<li>response.getContentType(): <%=response.getContentType() %></li>
	<li>response.getLocale(): <%=response.getLocale() %></li>
</ul>
<h3>Request headers</h3>
<ul>
<%
	Enumeration enu = request.getHeaderNames();
	while(enu.hasMoreElements()) {
		String currentHead = (String)enu.nextElement();%>
		<li><%=currentHead %> = <%=request.getHeader(currentHead) %></li>
	<%}
%>
</ul>
<h3>System.properties</h3>
<ul>
<%
	Properties props = System.getProperties();
	Set keys = props.keySet();
	for (Iterator iter = keys.iterator(); iter.hasNext();) {
		String keyStr = (String)iter.next();%>
		<li><%="[" + keyStr + "] = [" + props.getProperty(keyStr) + "]"%></li>
	<%}
%>
</ul>
</body>
</html>
