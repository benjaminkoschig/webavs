<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%
//String languePage = globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session);
//String servletContext = request.getContextPath();
%>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<TITLE>WebAVS - Accueil</TITLE>
<script>
var now = new Date();
var nowString = "y" + now.getYear() + "m" + now.getMonth() + "d" + now.getDay();
nowString += "h" + now.getHours() + "m" + now.getMinutes() + "s" + now.getSeconds();

if (self != top){
	top.location.href=window.location.href;
}
</script>
<body>
<script>
location.replace("<%=request.getContextPath()%>/pyxis?mode=vg&" + nowString);
</script>
</BODY>
</HTML>
