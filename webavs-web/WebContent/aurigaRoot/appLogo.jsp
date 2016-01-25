<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<HEAD>
<TITLE></TITLE>
<META http-equiv="Content-Type" content="text/html;">
<META name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
</HEAD>
<BODY>
<table align="left" cellpadding="0" cellspacing="0">
<tr><td>
<IMG src="<%=request.getContextPath()%>/images/<%=globaz.jade.common.Jade.getInstance().getClientLogoFile()%>">
</td></tr>
<tr><td align="center">
		<%
		try {
			globaz.jade.jdbc.JadeJdbcDatasourceDirect dsDirect = (globaz.jade.jdbc.JadeJdbcDatasourceDirect) globaz.jade.jdbc.JadeJdbcDriver.getInstance().getDatasource(globaz.jade.common.Jade.getInstance().getDefaultJdbcUrl().substring(10));
			out.write(dsDirect.getUrl());
		} catch (Exception e) {
		}
		%>
</td></tr>
</table>
</BODY>
</HTML>