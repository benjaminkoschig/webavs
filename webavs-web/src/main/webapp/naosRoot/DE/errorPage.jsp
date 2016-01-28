<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ page import="globaz.framework.bean.*" %>
<HEAD>
<TITLE></TITLE>
<META http-equiv="Content-Type" content="text/html;">
<META name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
</HEAD>
<BODY style="background-color:#FFCCCC">
<H1>L'application a rencontré une erreur irrémédiable !</H1>
<%  
FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
if (viewBean == null) {
	viewBean = (FWViewBeanInterface) request.getAttribute("viewBean");
}
if (viewBean != null) {%>
message : <%=viewBean.getMessage()%>

<%} else { %>
message : aucun (pas d'objet "viewBean" en session ou en request)
<%}%>
</BODY>
</HTML>