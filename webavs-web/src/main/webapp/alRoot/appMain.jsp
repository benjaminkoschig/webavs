<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<HEAD>
<TITLE></TITLE>
<META http-equiv="Content-Type" content="text/html;">
<META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
</HEAD>
<body bgcolor="#FFFFFF" text="#000000">
<table width="100%" height="100%" border="0">  
  <tr valign="bottom">
    <td height="10%" align="center">&nbsp;</td>
  </tr>
  <%
 
	String url ="al?userAction=al.dossier.dossier.chercher";
	
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	
	//si l'utilisateur n'a pas accès à l'application webAF mais spécifiquement aux annonces délégués, on le redirige sur l'écran des annonces délégué
	if(!objSession.hasRight("al", globaz.framework.secure.FWSecureConstants.READ) && objSession.hasRight("al.annoncesRafam.annonceRafamED", globaz.framework.secure.FWSecureConstants.READ)){
		url = "al?userAction=al.rafam.annonceRafamED.chercher";
	}
	%>
 
</table>
	<script>	
	    location.replace("<%=request.getContextPath()%>/<%=url%>");
	</script>
</body>
</HTML>