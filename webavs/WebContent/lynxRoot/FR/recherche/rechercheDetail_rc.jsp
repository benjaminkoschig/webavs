<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "GLX0030";
	rememberSearchCriterias = true;
	bButtonNew = false;

	LXRechercheGeneraleViewBean viewBean = (LXRechercheGeneraleViewBean) session.getAttribute("viewBean");
	
	String libelleFournisseur =  viewBean.getIdExterneFournisseur() + " - " + viewBean.getNomCompletFournisseur();
	String libelleSociete = viewBean.getSociete().getIdExterne() + " - " + viewBean.getNomSociete();
	
	String csTypeSection = viewBean.getCsTypeSection();
	String titre = "";
	String numeroInterne = "No Facture interne";
	
	if(LXSection.CS_TYPE_FACTURE.equals(csTypeSection)) {
		titre = "Détail d'une facture";
	}else {
		numeroInterne = "No note de crédit interne";
		titre = "Détail d'une note de crédit";
	}
	
	String solde = "";
	String mouvements = "";
	String base = "";
	
	if(LXConstants.ETAT_DEFINITIF.equals(request.getParameter("forEtat"))) {
		solde = viewBean.getSoldeFormatted();
		mouvements = viewBean.getMouvementFormatted();
		base = viewBean.getBaseFormatted();
	}else {
		solde = viewBean.getSoldeProvisoireFormatted();
		mouvements = viewBean.getMouvementProvisoireFormatted();
		base = viewBean.getBaseProvisoireFormatted();
	}
	
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.lynx.db.operation.LXOperationViewBean"%>
<%@page import="globaz.lynx.db.operation.LXOperation"%>
<%@page import="globaz.lynx.db.fournisseur.LXFournisseur"%>
<%@page import="globaz.lynx.utils.LXFournisseurUtil"%>
<%@page import="globaz.lynx.utils.LXSocieteDebitriceUtil"%>
<%@page import="globaz.lynx.db.section.LXSection"%>
<%@page import="globaz.lynx.db.recherche.LXRechercheGeneraleViewBean"%>
<%@page import="globaz.lynx.db.recherche.LXRechercheGenerale"%>
<%@page import="globaz.lynx.utils.LXConstants"%>

<ct:menuChange displayId="menu" menuId="LX-MenuPrincipal"/>
<ct:menuChange displayId="options" menuId="LX-OnlyDetail" showTab="menu"/>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
usrAction = "<%=globaz.lynx.application.LXApplication.DEFAULT_APPLICATION_LYNX.toLowerCase()%>.recherche.rechercheDetail.lister";
bFind = true;

top.document.title = "<%=titre%> - " + top.location.href;

function changeMontant() {

	var etat = document.getElementById('forEtat').options[document.getElementById('forEtat').selectedIndex].value;

	if('<%=LXConstants.ETAT_DEFINITIF%>' == etat) {
		
		document.getElementById('base').value = "<%=viewBean.getBaseFormatted()%>";	
		document.getElementById('mouvements').value = "<%=viewBean.getMouvementFormatted()%>";	
		document.getElementById('solde').value = "<%=viewBean.getSoldeFormatted()%>";	
	}else {
		document.getElementById('base').value = "<%=viewBean.getBaseProvisoireFormatted()%>";	
		document.getElementById('mouvements').value = "<%=viewBean.getMouvementProvisoireFormatted()%>";	
		document.getElementById('solde').value = "<%=viewBean.getSoldeProvisoireFormatted()%>";	

	}
}

// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><%=titre%><%-- /tpl:put --%>
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
	<input type="hidden" name="forIdSection" value="<%=request.getParameter("forIdSection")%>"/>
	<TR>
		<TD width="128">Soci&eacute;t&eacute; d&eacute;bitrice</TD>
		<TD><INPUT type="text" value="<%= libelleSociete %>" size="45" maxlength="40" readonly="readonly" class="libelleLongDisabled"></TD>
		<TD>Fournisseur</TD>
		<TD><INPUT type="text" value="<%= libelleFournisseur %>" size="45" maxlength="40" readonly="readonly" class="libelleLongDisabled"></TD>
		<TD>&nbsp;</TD>	
	</TR>
	<TR>
       	<TD>Base</TD>
       	<TD><INPUT type="text" name="base" value="<%= base %>" size="7" maxlength="40" readonly="readonly" class="libelleDisabled"></TD>
		<TD>Mouvements</TD>
		<TD><INPUT type="text" name="mouvements" value="<%= mouvements %>" size="7" maxlength="40" readonly="readonly" class="libelleDisabled"></TD>
      	<TD>&nbsp;</TD>	
	</TR>
	<TR>
       	<TD>Solde</TD>
       	<TD><INPUT type="text" name="solde" value="<%= solde %>" size="7" maxlength="40" readonly="readonly" class="libelleDisabled"></TD>
		<TD><%= numeroInterne %></TD>
		<TD><INPUT type="text" value="<%= viewBean.getIdExterne() %>" size="7" maxlength="40" readonly="readonly" class="libelleLongDisabled"></TD>
     	<TD>&nbsp;</TD>	
	</TR>	
	<TR> 
		<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
	</TR>
	<TR>
       	<TD>Depuis</TD>
       	<TD><ct:FWCalendarTag name="forDate" doClientValidation="CALENDAR" value="" tabindex="1"/></TD>
		<TD>Jusqu'a</TD>
		<TD><ct:FWCalendarTag name="forDateInferieure" doClientValidation="CALENDAR" value="" tabindex="1"/></TD>
      	<TD>&nbsp;</TD>	
	</TR>	
	<TR>
       	<TD>Depuis date d'&eacute;ch&eacute;ance</TD>
       	<TD><ct:FWCalendarTag name="forDateEcheance" doClientValidation="CALENDAR" value="" tabindex="1"/></TD>
		<TD>Jusqu'a date d'&eacute;ch&eacute;ance</TD>
		<TD><ct:FWCalendarTag name="forDateEcheanceInferieure" doClientValidation="CALENDAR" value="" tabindex="1"/></TD>
      	<TD>&nbsp;</TD>	
	</TR>	
	<TR>
       	<TD>Montant minimum</TD>
       	<TD><INPUT type="text" name="forMontantMini" size="10" maxlength="40" tabindex="1"/></TD>
       	<TD>Montant maximum</TD>
       	<TD><INPUT type="text" name="forMontantMaxi" size="10" maxlength="40" tabindex="1"/></TD>
      	<TD>&nbsp;</TD>	
	</TR>	
	<TR>
       	<TD>Libell&eacute;</TD>
       	<TD><INPUT type="text" name="likeLibelle" size="45" maxlength="40" tabindex="1"/></TD>
      	<TD>Etat</TD>	
		<TD>
			<SELECT id="forEtat" name="forEtat"onchange="javascript:changeMontant()" tabindex="1">
				<OPTION value="<%=LXConstants.ETAT_DEFINITIF %>" <%= LXConstants.ETAT_DEFINITIF.equals(request.getParameter("forEtat")) ? "selected=\'selected\'" : "" %> >Définitif</OPTION>
				<OPTION value="<%=LXConstants.ETAT_PROVISOIRE %>" <%= LXConstants.ETAT_PROVISOIRE.equals(request.getParameter("forEtat")) ? "selected=\'selected\'" : "" %>>Provisoire</OPTION>
			</SELECT>
		</TD>
      	<TD>&nbsp;</TD>	
	</TR>	
	<TR>
		<TD>Tri</TD>
		<TD>
			<SELECT id="forTri" name="forTri" tabindex="1">
				<OPTION value=""></OPTION>
				<OPTION value="<%=LXOperation.FIELD_DATEOPERATION %>">Date</OPTION>
				<OPTION value="<%=LXOperation.FIELD_DATEECHEANCE %>">Echéance</OPTION>
				<OPTION value="<%=LXOperation.FIELD_LIBELLE %>">Libellé</OPTION>
				<OPTION value="<%=LXOperation.FIELD_CSTYPEOPERATION %>">Type</OPTION>
				<OPTION value="<%=LXOperation.FIELD_MONTANT %>">Montant</OPTION>
			</SELECT>
		</TD>
      	<TD>&nbsp;</TD>	
      	<TD>&nbsp;</TD>	
      	<TD>&nbsp;</TD>	
	</TR>		
	
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>