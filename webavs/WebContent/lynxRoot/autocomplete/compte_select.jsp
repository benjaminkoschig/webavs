<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<html>
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE></TITLE>

<body>
<form>
<%=globaz.lynx.parser.LXAutoComplete.getComptes((globaz.globall.db.BSession) globaz.helios.translation.CodeSystem.getSession(session),request.getParameter("like"),request.getParameter("idMandat"),request.getParameter("forDate"))%>
</form>
</body>
</html>