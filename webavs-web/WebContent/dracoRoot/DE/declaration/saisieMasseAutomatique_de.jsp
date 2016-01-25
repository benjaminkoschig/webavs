<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "CDS0007";%>
<%@page import="globaz.draco.translation.*"%>
<%@page import="globaz.draco.db.declaration.*"%>
<%
globaz.draco.db.declaration.DSSaisieMasseAutomatiqueViewBean viewBean = (globaz.draco.db.declaration.DSSaisieMasseAutomatiqueViewBean)session.getAttribute ("viewBean");
userActionValue = "draco.declaration.saisieMasseAutomatique.ajouter";
String jspLocation = servletContext + mainServletPath + "Root/barcode_select.jsp";
int autoDigitAff = globaz.draco.util.DSUtil.getLongueurReception(session);

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JACalendar"%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers-->

function add() {
}

function upd() {
}

function validate() {

	 document.forms[0].elements('userAction').value="draco.declaration.saisieMasseAutomatique.ajouter";
     return true;
}
function cancel() {
	document.forms[0].elements('userAction').value="draco.declaration.saisieMasseAutomatique.afficherApresAnnulation";
}
function del() {
	if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?"))
	{
		document.forms[0].elements('userAction').value="draco.declaration.saisieMasseAutomatique.supprimer";
		document.forms[0].submit();
	}
}
function init() {}

function updateInfoAffilie(tag) {
	if(tag.select && tag.select.selectedIndex != -1){
 		document.getElementById('descriptionTiers').value = tag.select[tag.select.selectedIndex].nom;
 		document.getElementById('affiliationId').value = tag.select[tag.select.selectedIndex].affiliationId;
 		document.getElementById('affilieDesEcran').value = tag.select[tag.select.selectedIndex].affilieDesEcran;
 		document.getElementById('affilieRadieEcran').value = tag.select[tag.select.selectedIndex].affilieRadieEcran;
 		document.getElementById('typeAffiliationEcran').value = tag.select[tag.select.selectedIndex].typeAffiliationEcran;
 	}
}
function resetInfoAffilie() {
 	document.getElementById('descriptionTiers').value = '';
 	document.getElementById('affiliationId').value = '';
 	document.getElementById('affilieDesEcran').value = '';
 	document.getElementById('affilieRadieEcran').value = '';
 	document.getElementById('typeAffiliationEcran').value = '';
}
function commitBarCode(tag){
	if(tag.select && tag.select.selectedIndex != -1){
	
		document.getElementById('btnVal').click();
	}
}

/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Eingabe des Empfangsdatums - Detail <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

<TR>
	
    <TD nowrap height="14" width="128">   Strichcode <input type="hidden" name="saisieEcran" value="true" tabindex="-1"></TD>
    <TD nowrap height="31" width="294">
   	<ct:FWPopupList name="barcode" jspName="<%=jspLocation%>" minNbrDigit="<%=autoDigitAff%>" autoNbrDigit="<%=autoDigitAff%>" onChange="commitBarCode(tag);" size="44" maxlength="44"/>
    </TD>
    <TD nowrap height="31" width="76"></TD>
    <TD nowrap height="31" width="350"></TD>
</TR>
<TR>
	<%
		String dateRetourEff = ""; 
		if(JadeStringUtil.isBlankOrZero(request.getParameter("dateRetourEff"))){
			dateRetourEff = JACalendar.todayJJsMMsAAAA();
		}else{
			dateRetourEff = request.getParameter("dateRetourEff");
		}
	%> 
    <TD nowrap height="30" width="128">Empfangsdatum</TD>
    <TD nowrap height="31" width="294"><ct:FWCalendarTag name="dateRetourEff" value="<%=dateRetourEff%>"/></TD>
</TR>
 <TR>
    <TD nowrap height="14" width="128">Lohnbescheinigungstyp</TD>
    <TD nowrap height="31" width="76" colspan="2"><INPUT name="typeDeclarationEcran" value="<%=CodeSystem.getLibelle(session,DSDeclarationViewBean.CS_PRINCIPALE)%>" class="disabled" readonly ></TD>
</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>