<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %> 
<HEAD>
<TITLE></TITLE>
<META http-equiv="Content-Type" content="text/html;">
<META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<SCRIPT>
top.document.title="Web@Prestations - <ct:FWLabel key="TITLE_RF"/>";
</SCRIPT>
<%@page import="globaz.jade.common.Jade"%>
<style type="text/css">
* {
	background-color: <%=Jade.getInstance().getWebappBackgroundColor()%>;
	color: <%=Jade.getInstance().getWebappTextColor()%>;
}
</style>
</HEAD>
<BODY>
<H2 align="center"><ct:FWLabel key="TITLE_RF"/></H2>
</BODY>
</HTML>