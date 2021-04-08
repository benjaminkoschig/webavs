<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.jade.common.Jade"%>
<%@ page import="globaz.apg.menu.MenuPrestation" %>
<%@ page import="globaz.prestation.tools.PRSessionDataContainerHelper" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<META http-equiv="Content-Type" content="text/html;">
<META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css" >
<style type="text/css">
* {
	background-color: <%=Jade.getInstance().getWebappBackgroundColor()%>;
	color: <%=Jade.getInstance().getWebappTextColor()%>;
}
</style>
</HEAD>
<BODY>
<%
    MenuPrestation menuPrestation = MenuPrestation.of(session);
%>
<H2 align="center"><ct:FWLabel key="<%=menuPrestation.getTitre()%>" /></H2>
</BODY>
<SCRIPT>
    top.document.title="Web@Prestations - <ct:FWLabel key="<%=menuPrestation.getTitre()%>"/>";
</SCRIPT>
</HTML>