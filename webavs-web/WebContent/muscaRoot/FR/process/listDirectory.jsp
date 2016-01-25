<!-- Sample JSP file -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<HEAD>
<jsp:useBean id="objSession" class="globaz.globall.db.BSession" scope="session"></jsp:useBean>
<%
	String sApp = new String();
	if (!request.getServerName().equals("localhost"))
		sApp = "/";

	String cd = new String("");
	if (request.getParameter("dir") != null)
		cd = request.getParameter("dir");
	if (!cd.endsWith("/"))
		cd = cd + "/";
	File realPath = realPath = new File(application.getRealPath(cd));
%>
<META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE>
</TITLE>
</HEAD>

<BODY BGCOLOR="#FFFFFF">
Contenu du répertoire <%= cd %><p>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr><td>Nom</td><td>Taille</td><td>Modification</td></tr>
<tr><td colspan="3"><hr></td></tr>
<%
	File[] files = realPath.listFiles();
	if (files != null) {
		for (int i = 0; i < files.length; i++ ) {
%>
<tr>
<td><a href="<%=sApp+cd+files[i].getName()%>">
<%= files[i].getName() %></a></td>
<td><%= files[i].length() %></td>
<td><i><%= new Date(files[i].lastModified()) %></i></td>
</tr>
<% } } %>
</table>
</BODY>
</HTML>
