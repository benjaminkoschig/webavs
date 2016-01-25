<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.globall.db.*,globaz.helios.db.comptes.*,globaz.helios.db.interfaces.*, globaz.helios.translation.*, globaz.globall.util.*, globaz.framework.util.*" %>
<html><META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE></TITLE>

<body>
<form>
<select size="5" style="width:8cm">
<%	
	
	String like = request.getParameter("like");
	
	if (like.charAt(0) == '*') {
		like = like.substring(1);
	}
		BSession bsession = (BSession)CodeSystem.getSession(session);
		CGLibelleStandardListViewBean manager = new CGLibelleStandardListViewBean();
		manager.setSession(bsession);
		manager.setForIdMandat(request.getParameter("idMandat"));
		manager.setBeginWithIdLibelleStandard(like);
		manager.find();
		String langue = request.getParameter("langue");
		if (manager.size() != 0)
		for (int i=0 ; i<manager.size() ; i++) {						
			CGLibelleStandardViewBean entity = (CGLibelleStandardViewBean) manager.getEntity(i);
			String libelle = null;
			if ("DE".equalsIgnoreCase(langue))
				libelle = entity.getLibelleDE();
			else if ("IT".equalsIgnoreCase(langue))
				libelle = entity.getLibelleIT();
			else
				libelle = entity.getLibelleFR();

				
			if (libelle==null || libelle.trim().length()==0)
				libelle =entity.getLibelleFR();
			if (libelle==null || libelle.trim().length()==0)
				libelle =entity.getLibelleDE();
			if (libelle==null || libelle.trim().length()==0)
				libelle =entity.getLibelleIT();
				
			
%>
	<option value="<%=libelle%>"><%=libelle%></option>
<%
	
 	} 
 %>
</select>
</form>
</body>
</html>