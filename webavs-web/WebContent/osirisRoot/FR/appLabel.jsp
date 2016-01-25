<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ page import="globaz.globall.util.JAUtil"%>
<%@ page import="globaz.globall.db.BSession"%>
<HEAD>
<TITLE></TITLE>
<META http-equiv="Content-Type" content="text/html;">
<META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<% //Les labels ont été modifiés pour garder le titre "facturation" pour les opération exécutée sur le "Compta Aux" depuis facturation 
	// p. ex lors de l'affichage des intérêts moratoires d'un passage de facturation
	boolean domCA = true;
	BSession bSession = (BSession) session.getAttribute("objSession");
	if (bSession.getAttribute("musca_interet_moratoire") != null)
	{
		domCA = false;
	}%>
<SCRIPT>
top.document.title="<%=domCA?"web@AVS - Comptabilité auxiliaire":"Web@AVS - Facturation"%>";
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
<H2 align="center"><%=domCA?"Comptabilité auxiliaire":"Facturation"%></H2>
</BODY>
</HTML>