<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="globaz.jade.common.Jade"%><HTML>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<HEAD>
<TITLE></TITLE>
<META http-equiv="Content-Type" content="text/html;">
<META name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<style type="text/css">
body {
	background-color: <%=Jade.getInstance().getWebappBackgroundColor()%>;
}
</style>
</HEAD>
<BODY bottommargin=0>
<center><IMG src="<%=request.getContextPath()%>/images/<%=globaz.jade.common.Jade.getInstance().getClientLogoFile()%>" alt="Logo client"></center>
</BODY>
</HTML>