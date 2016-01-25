<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" isErrorPage="true" %>
<%@ page import="globaz.framework.bean.*" %>
<HEAD>
<TITLE></TITLE>
<META http-equiv="Content-Type" content="text/html;">
<META name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
</HEAD>
<BODY>
<HR>
<H2>L'application a rencontré une erreur</H2>
<HR>
<P><B>Veuillez imprimer le message ci-dessous et le transmettre à Globaz: </B><P>
<dl>
  <dt><b>Date et heure</b></dt>
<dd><%=java.text.DateFormat.getDateInstance().format(new java.util.Date())%></dd>
  <dt><b>Server</b></dt>
<dd><%=request.getServerName()%></dd>
  <dt><b>Message</b></dt>
<%  
FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
if (viewBean == null) {
	viewBean = (FWViewBeanInterface) request.getAttribute("viewBean");
}
if (viewBean != null) {%>
<dd><%=viewBean.getMessage()%></dd>
  <dt><b>ViewBean</b></dt> 
<dd><%=viewBean.getClass().getName()%></dd>
<%} else { %>
<dd>aucun (pas d'objet "viewBean" en session ou en request)</dd>
<%}%>
<%
 if (exception != null) {%>
  <dt><b>Exception</b></dt>
<dd><PRE><%=exception.getMessage()%></PRE></dd>
  <dt><b>StackTrace</b></dt>
<dd><PRE><%exception.printStackTrace(new java.io.PrintStream(response.getOutputStream()));%></PRE></dd>
<%}%>


</dl>
</BODY>
</HTML>