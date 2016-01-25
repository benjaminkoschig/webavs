<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@page import="globaz.framework.utils.urls.FWUrlStackLog"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ page 
language="java"
contentType="text/html; charset=ISO-8859-1"
%>
<%
{
	String userAction = request.getParameter("userAction");
	if ("framework.urlstack.clear".equals(userAction)) {
		FWUrlStackLog.getInstance().clear();
	} else if ("framework.urlstack.logon".equals(userAction)) {
		FWUrlStackLog.getInstance().activate();
	} else if ("framework.urlstack.logoff".equals(userAction)) {
		FWUrlStackLog.getInstance().deactivate();
	}
}
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
	font-size: small;
}
</style>
<title>urlStack.jsp</title>
</head>
<body>
<h3>Pile d'URL's</h3>
<table>
	<tr>
		<th>Position</th>
		<th>userAction</th>
		<th>Full URL</th>
	</tr>
<%
globaz.framework.utils.urls.FWUrlsStack stack = (globaz.framework.utils.urls.FWUrlsStack)session.getAttribute(globaz.framework.servlets.FWServlet.URL_STACK);
for(int i = stack.size(); i > 0; ) {
	globaz.framework.utils.urls.FWUrl url = stack.peekAt(--i);
	globaz.framework.utils.params.FWParamString userAction = url.getParam("userAction");
	String userActionValue = null;
	if (userAction != null) {
		userActionValue = (String)userAction.getValue();
	}
%>
	<tr>
		<td><%=i%></td>
		<td title=""><%=userActionValue%></td>
		<td><%=url.toString()%></td>
	</tr>
<%
}
%>
	<tr style="font-weight: bold; font-size: medium">
		<td>Total</td>
		<td><%=stack.size()%> urls </td>
		<td>&nbsp;</td>
	</tr>
</table>
<hr/>
<h3>Règles en place</h3>
<table>
	<tr>
		<th>Nom</th>
		<th>Priorité</th>
	</tr>
<%
for(int i = stack.countRules(); i > 0; ) {
	globaz.framework.utils.rules.FWRule rule = stack.getRule(--i);
%>
	<tr>
		<td><%=rule.getClass().getName()%></td>
		<td title="">&nbsp;</td>
	</tr>
<%
}
%>
	<tr style="font-weight: bold; font-size: medium">
		<td>Total</td>
		<td><%=stack.countRules()%> règles </td>
	</tr>
</table>
<h3>Règles en place(2)</h3>
<table>
	<tr>
		<th>Nom</th>
		<th>Priorité</th>
		<th>Extra infos</th>
	</tr>
<%
java.util.List newRules = stack.getNewRules();
System.out.println(newRules);
for(int i = 0; i < newRules.size(); i++) {
	globaz.framework.utils.urls.rules.FWUrlsStackRule rule = (globaz.framework.utils.urls.rules.FWUrlsStackRule)newRules.get(i);
%>
	<tr>
		<td><%=rule.getClass().getName()%></td>
		<td title=""><%=rule.getPriority()%></td>
		<td><%=rule.toString()%></td>
	</tr>
<%
}
%>
	<tr style="font-weight: bold; font-size: medium">
		<td>Total</td>
		<td><%=newRules.size()%> règles </td>
		<td>&nbsp;</td>
	</tr>
</table>
<h3>Logging</h3>
<%
FWUrlStackLog log = FWUrlStackLog.getInstance();
%>
<p>Logging is now <%=log.isActive()? "on" : "off" %>. 
<%if (log.isActive()) { %>
	<a href="urlStack.jsp?userAction=framework.urlstack.logoff">Deactivate it!</a>
<%} else { %>
	<a href="urlStack.jsp?userAction=framework.urlstack.logon">Activate it!</a>
<% } %>
</p>
Actions:
<ol> 
<% for (int i = 0; i < log.getActions().size(); i++) {%>
	<li><%=(String)(log.getActions().get(i)) %></li>	
<% } %>
</ol>
<p>Total: <%=log.getActions().size()%> actions logged. <a href="urlStack.jsp?userAction=framework.urlstack.clear">Clear it!</a></p>
</body>
</html>
