<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.globall.db.*,globaz.helios.db.comptes.*,globaz.helios.db.interfaces.*, globaz.helios.translation.*, globaz.globall.util.*, globaz.framework.util.*" %>
<html><META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE></TITLE>

<body>
<form>
<select size="5" style="width:15cm">
<%
	String like = globaz.helios.parser.CGAutoComplete.getLikeFormatted(request.getParameter("like"), Boolean.valueOf(request.getParameter("isMandatAVS")));

	BSession bsession = (BSession)CodeSystem.getSession(session);
	CGPlanComptableManager manager = new CGPlanComptableManager();
	manager.setSession(bsession);
	manager.setBeginWithIdExterne(like);
	manager.setReqCritere(CodeSystem.CS_TRI_NUMERO_COMPTE);
	manager.setForIdMandat(request.getParameter("idMandat"));
	manager.find();

	if (manager.size() != 0) {
		String lastIdCompte = "";

		for (int i=0 ; i<manager.size() ; i++) {
			CGPlanComptableViewBean entity = (CGPlanComptableViewBean) manager.getEntity(i);
			if (!entity.isEstVerrouille().booleanValue() && !entity.getIdCompte().equals(lastIdCompte)) {
				lastIdCompte = entity.getIdCompte();
			%>
			<option idCompte="<%=entity.getIdCompte()%>" value="<%=entity.getIdExterne()%>" compteLibelle="<%=entity.getLibelle()%>"><%=entity.getIdExterne()%> - <%=entity.getLibelle()%></option>
			<%
			}
	 	}
 	}
%>
</select>
</form>
</body>
</html>