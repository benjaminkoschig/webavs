<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "GLX0031";
	rememberSearchCriterias = true;
	bButtonNew = false;
	
	String idSociete = request.getParameter("forIdSociete");
	String libelleSociete = "";
	if(!JadeStringUtil.isBlankOrZero(idSociete)) {
		libelleSociete = LXSocieteDebitriceUtil.getLibelle(objSession, idSociete);
	}
	
	String idFournisseur = request.getParameter("forIdFournisseur");
	String libelleFournisseur = "";
	if(!JadeStringUtil.isBlankOrZero(idFournisseur)) {
		libelleFournisseur = LXFournisseurUtil.getLibelleNomComplet(objSession, idFournisseur);
	}
	
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.lynx.db.recherche.LXRechercheGeneraleViewBean"%>
<%@page import="globaz.lynx.db.recherche.LXRechercheGenerale"%>
<%@page import="globaz.lynx.db.section.LXSection"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.lynx.utils.LXSocieteDebitriceUtil"%>
<%@page import="globaz.lynx.utils.LXFournisseurUtil"%>
<%@page import="globaz.lynx.utils.LXConstants"%>
<%@page import="globaz.lynx.db.recherche.LXRechercheGeneraleManager"%>

<ct:menuChange displayId="menu" menuId="LX-MenuPrincipal"/>
<ct:menuChange displayId="options" menuId="LX-OnlyDetail" showTab="menu"/>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
usrAction = "<%=globaz.lynx.application.LXApplication.DEFAULT_APPLICATION_LYNX.toLowerCase()%>.recherche.rechercheGenerale.lister";
bFind = true;

function updateSociete(tag) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("forIdSociete").value = element.idSociete;
		document.getElementById("libelleSociete").value = element.libelleSociete;
	}
}

function updateFournisseur(tag) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("forIdFournisseur").value = element.idFournisseur;
		document.getElementById("libelleFournisseur").value = element.libelleFournisseur;
	}
}

function onSocieteFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" Die Schuldnerfirma existiert nicht.");
	}
}

function onFounisseurFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" Der Lieferant existiert nicht.");
	}
}

top.document.title = "Suche der Rechnungen und Gutschriften - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Suche der Rechnungen und Gutschriften<%-- /tpl:put --%>
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
		<TD width="128">Schuldnerfirma</TD>
		<TD>   
			<input type="hidden" name="forIdSociete" value="<%= request.getParameter("forIdSociete") == null ? "" : request.getParameter("forIdSociete") %>"/>
	   		<% 	
	   			String jspLocationSociete = servletContext + "/lynxRoot/autocomplete/societe_select.jsp";
	   			int autoCompleteStart = globaz.helios.parser.CGAutoComplete.getAutoCompleteAutoStart();
	   			
	   			String tmp = request.getParameter("idExterneSociete") == null ? "" : request.getParameter("idExterneSociete");
	   		%>
			<ct:FWPopupList name="idExterneSociete" onFailure="onSocieteFailure(window.event);" onChange="updateSociete(tag)"  validateOnChange="true" params="" value="<%=tmp%>" className="libelle" jspName="<%=jspLocationSociete%>" minNbrDigit="1" autoNbrDigit="3" forceSelection="true" tabindex="1"/>
			&nbsp;
			<INPUT type="text" name="libelleSociete" value="<%= libelleSociete %>" size="45" maxlength="40" readonly="readonly" class="libelleLongDisabled"/>
		</TD>
		<TD cospan="3">&nbsp;</TD>	
	</TR>	
	<TR>
		<TD width="128">Lieferant</TD>
		<TD>   	
	   		<input type="hidden" name="forIdFournisseur" value="<%=request.getParameter("forIdFournisseur") == null ? "" : request.getParameter("forIdFournisseur") %>"/>
	   		<% 	
	   			String jspFournisseurLocation = servletContext + "/lynxRoot/autocomplete/fournisseur_select.jsp";
	   			tmp = request.getParameter("idExterne") == null ? "" : request.getParameter("idExterne");
	   		%>
			<ct:FWPopupList name="idExterneFournisseur" onFailure="onFounisseurFailure(window.event);" onChange="updateFournisseur(tag)"  validateOnChange="true" params="" value="<%=tmp%>" className="libelle" jspName="<%=jspFournisseurLocation%>" minNbrDigit="1" autoNbrDigit="<%=autoCompleteStart%>" forceSelection="true" tabindex="1"/>
			&nbsp;
			<INPUT type="text" name="libelleFournisseur" value="<%= libelleFournisseur %>" size="45" maxlength="40" readonly="readonly" class="libelleLongDisabled"/>
		</TD>
		<TD cospan="3">&nbsp;</TD>	
	</TR>		
	<TR> 
		<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
	</TR>
	<TR>
       	<TD>Von </TD>
       	<TD><ct:FWCalendarTag name="forDebutDateCreation" doClientValidation="CALENDAR" value="" tabindex="1"/></TD>
       	<TD>Bis</TD>
       	<TD><ct:FWCalendarTag name="forFinDateCreation" doClientValidation="CALENDAR" value="" tabindex="1"/></TD>
       	<TD>&nbsp;</TD>	
	</TR>		
	<TR>
       	<TD>Minimaler Saldo</TD>
       	<TD><INPUT type="text" name="forMontantMini" size="10" maxlength="40" tabindex="1"></TD>
		<TD>Höchstsaldo</TD>
		<TD><INPUT type="text" name="forMontantMaxi" size="10" maxlength="40" tabindex="1"></TD>
      	<TD>&nbsp;</TD>	
	</TR>
	<TR>
       	<TD>Interne-Nr.</TD>
       	<TD><INPUT type="text" name="likeIdExterne" size="20" maxlength="20" tabindex="1"></TD>
		<TD>Typ</TD>
		<TD><ct:FWCodeSelectTag name="forCsTypeSection" defaut="" codeType="LXTYPESECT" wantBlank="true" tabindex="1"/></TD>
     	<TD>&nbsp;</TD>	
	</TR>	
	<TR>
		<TD>Auswahl</TD>
		<TD>
			<SELECT id="forSelection" name="forSelection" tabindex="1">
				<OPTION value=""></OPTION>
				<OPTION value="<%= LXConstants.SELECTION_OUVERT %>">Offen</OPTION>
				<OPTION value="<%= LXConstants.SELECTION_SOLDE %>">Saldiert</OPTION>
			</SELECT>
		</TD>
       	<TD>Lieferant Rechnung-Nr.</TD>
       	<TD><INPUT type="text" name="likeIdReferenceExterne" size="20" maxlength="20" tabindex="1"></TD>
      	<TD>&nbsp;</TD>	
	</TR>
	<TR>
     	<TD>Status</TD>	
		<TD>
			<SELECT id="forEtat" name="forEtat" tabindex="1">
				<OPTION value="<%=LXConstants.ETAT_DEFINITIF %>">Definitiv</OPTION>
				<OPTION value="<%=LXConstants.ETAT_PROVISOIRE %>">Provisorisch</OPTION>
			</SELECT>
		</TD>
 		<TD>Sortierung</TD>
		<TD>
			<SELECT id="forTri" name="forTri" tabindex="1">
				<OPTION value="<%=LXRechercheGeneraleManager.ORDER_BY_FOURNISSEUR%>">Lieferant</OPTION>
				<OPTION value="<%=LXRechercheGeneraleManager.ORDER_BY_DATESECTION%>">Datum</OPTION>
				<OPTION value="<%=LXRechercheGeneraleManager.ORDER_BY_IDEXTERNE%>">Interne-Nr.</OPTION>
				<OPTION value="<%=LXRechercheGeneraleManager.ORDER_BY_BASE%>">Basis</OPTION>
				<OPTION value="<%=LXRechercheGeneraleManager.ORDER_BY_MOUVEMENT%>">Bewegungen</OPTION>
				<OPTION value="<%=LXRechercheGeneraleManager.ORDER_BY_SOLDE%>">Saldo</OPTION>
				<OPTION value="<%=LXRechercheGeneraleManager.ORDER_BY_IDREFERENCEEXTERNE%>">Lieferant Rechnung-Nr.</OPTION>
			</SELECT>
		</TD>
      	<TD>&nbsp;</TD>	
	</TR>
		
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>