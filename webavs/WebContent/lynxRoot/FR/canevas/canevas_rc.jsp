<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "GLX0037";
	rememberSearchCriterias = true;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.lynx.parser.LXSelectBlockParser"%>
<%@page import="globaz.lynx.application.LXApplication"%>
<%@page import="globaz.lynx.db.canevas.LXCanevasOperation"%>
<%@page import="globaz.lynx.db.canevas.LXCanevasViewBean"%>

<ct:menuChange displayId="menu" menuId="LX-MenuPrincipal"/>
<ct:menuChange displayId="options" menuId="LX-OnlyDetail" showTab="menu"/>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
usrAction = "<%=LXApplication.DEFAULT_APPLICATION_LYNX.toLowerCase()%>.canevas.canevas.lister";
bFind = true;

<%

actionNew += "&forceNew=true";
actionNew += "&csTypeOperation=";


btnNewLabel = "BVR Orange";

String actionNewCanevasBvrRouge = actionNew+globaz.lynx.db.operation.LXOperation.CS_TYPE_FACTURE_BVR_ROUGE;
String actionNewCanevasVirement = actionNew+globaz.lynx.db.operation.LXOperation.CS_TYPE_FACTURE_VIREMENT;
String actionNewCanevasLsv = actionNew+globaz.lynx.db.operation.LXOperation.CS_TYPE_FACTURE_LSV;
String actionNewCanevasCaisse = actionNew+globaz.lynx.db.operation.LXOperation.CS_TYPE_FACTURE_CAISSE;

actionNew += globaz.lynx.db.operation.LXOperation.CS_TYPE_FACTURE_BVR_ORANGE;
%>

function updateSociete(tag) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("forIdSociete").value = element.idSociete;
		document.getElementById("libelleSociete").value = element.libelleSociete;
	}
}

function onSocieteFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" La société débitrice n'existe pas.");
	}
}

top.document.title = "Recherche des canevas - " + top.location.href;
// stop hiding -->
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Recherche des canevas<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	</tbody>
</table>
<script language="JavaScript">
	element = document.getElementById("subtable");
  	element.style.height = "10px";
  	element.style.width = "100%";
</script>
<table cellspacing="0" cellpadding="0" style="height: 100px; width: 100%">
	<tbody>
        <TR>
         	<TD>Soci&eacute;t&eacute; d&eacute;bitrice</TD>
            <TD colspan="2">
            	<%-- Obligation de declarer un viewBean null pour l'include qui suit--%>
				<% LXCanevasViewBean viewBean = null; %>
				<%@ include file="/lynxRoot/include/societe.jsp" %>
			</TD>
			<TD colspan="2">&nbsp;</TD>
		</TR>
		<TR> 
			<TD height="11" colspan="5"> <hr size="3" width="100%"/></TD>
		</TR>
		<TR>
           	<TD>Fournisseur</TD>
           	<TD >
           		<INPUT type="text" name="likeIdFournisseur" size="25" maxlength="25" tabindex="1"/>
           	</TD>
           	<TD>&nbsp;</TD>
           	<TD>No TVA</TD>
           	<TD>
           		<INPUT type="text" name="numTVA" size="25" maxlength="25" tabindex="1"/>
           	</TD>
		</TR>
		<TR>
           	<TD>No Canevas Interne</TD>
           	<TD>
           		<INPUT type="text" name="likeIdExterne" size="21" maxlength="20" tabindex="1"/>
           	</TD>
           	<TD>&nbsp;</TD>
           	<TD>No Canevas Fournisseur</TD>
           	<TD>
           		<INPUT type="text" name="likeReferenceExterne" size="41" maxlength="40" tabindex="1"/>
           	</TD>
		</TR>			
				
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>

<%-- tpl:put name="zoneButtons" --%>	
<ct:ifhasright element="lynx.canevas.canevas.afficher" crud="u">
	<INPUT type="button" name="btnNewCanevasBvrRouge" value="BVR Rouge" onClick="document.location.href='<%=actionNewCanevasBvrRouge%>'" tabindex="1"/>
	<INPUT type="button" name="btnNewCanevasVirement" value="Virement" onClick="document.location.href='<%=actionNewCanevasVirement%>'" tabindex="1"/>
	<INPUT type="button" name="btnNewCanevasLSV" value=" LSV " onClick="document.location.href='<%=actionNewCanevasLsv%>'" tabindex="1"/>
	<INPUT type="button" name="btnNewCanevasCaisse" value="Caisse" onClick="document.location.href='<%=actionNewCanevasCaisse%>'" tabindex="1"/>
</ct:ifhasright>	
<%-- /tpl:put --%>

<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>