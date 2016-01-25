<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.globall.db.*,globaz.osiris.db.comptes.*, globaz.musca.db.facturation.*, globaz.musca.translation.*, globaz.globall.util.*, globaz.framework.util.*" %>
<html><META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE></TITLE>

<body>
<form>
<select size="5" style="width:15cm">
<%	
	BSession bsession = (BSession)CodeSystem.getSession(session);
	CARubriqueManager manager = new CARubriqueManager();
	manager.setSession(bsession);
	manager.setBeginWithIdExterne(request.getParameter("like"));
	//manager.setForIdExterne(request.getParameter("idRubrique"));
	manager.find();

	if (manager.size() != 0)
	for (int i=0 ; i<manager.size() ; i++) {
		CARubrique entity = (CARubrique) manager.getEntity(i);
%>
		<option idRubrique="<%=entity.getIdRubrique()%>" value="<%=entity.getIdExterne()%>" libelle="<%=entity.getDescription()%>"><%=entity.getIdExterne()%>-<%=entity.getDescription()%></option>
<%		
 	}
%>
</select>
</form>
</body>
</html>