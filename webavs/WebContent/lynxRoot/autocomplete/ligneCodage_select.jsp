<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<html>
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE></TITLE>

<%
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>

<body>
<form>
<%=globaz.lynx.parser.LXAutoComplete.getDecoupageLigneCodage(objSession, request.getParameter("like"), request.getParameter("idSociete"))%>
</form>
</body>
</html>