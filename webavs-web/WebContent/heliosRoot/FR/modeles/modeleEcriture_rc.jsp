<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %> 
<%@ page import="globaz.helios.db.interfaces.*,globaz.helios.db.modeles.*" %>
<%
idEcran = "GCF4017";

String tous = "Tous";
if (languePage.equalsIgnoreCase("de")) {
	tous = "Alle";
}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CG-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CG-OnlyDetail"/>

<%
	globaz.framework.menu.FWMenuBlackBox bb = (globaz.framework.menu.FWMenuBlackBox) session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_USER_MENU);
	bb.setNodeOpen(true, "parameters", "CG-MenuPrincipal");
%>

<SCRIPT>
usrAction = "helios.modeles.modeleEcriture.lister";
bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Aperçu des modèles d'écritures
		  <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		
		<tr>
            <TD>A partir de ...&nbsp;&nbsp;</TD>
            <TD><input name='fromNumero' class="libelle" size="5" maxlength="5" value=""></TD>
		</tr>

		<tr>
            <TD>Libelle&nbsp;&nbsp;</TD>
            <TD><input name='forLibelle' class="libelleLong" value=""></TD>
		</tr>


                        <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>