<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.globall.db.*,globaz.osiris.db.comptes.*, globaz.globall.util.*, globaz.framework.util.*" %>
<html><META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE></TITLE>

<body>
<form>
<select size="5" style="width:12cm">
<jsp:useBean id="objSession" class="globaz.globall.db.BSession" scope="session"></jsp:useBean>
<%
CACompteCourantManager manager = new CACompteCourantManager();
manager.setSession(objSession);

	manager.setBeginWithIdExterne(request.getParameter("like"));

	manager.changeManagerSize(0);
	manager.find();

	if (manager.size() != 0)
	for (int i=0 ; i<manager.size() ; i++) {
		CACompteCourant entity = (CACompteCourant) manager.getEntity(i);
%>
	<option idCompteCourant="<%=entity.getIdCompteCourant()%>" idExterneCompteCourantEcran="<%=entity.getIdExterne()%>" CCEcran="<%=entity.getRubrique().getDescription()%>" value="<%=entity.getIdExterne()%>"><%=entity.getIdExterne()+" - "+entity.getRubrique().getDescription()%></option>
<%
 	}
%>
</select>
</form>
</body>
</html>
