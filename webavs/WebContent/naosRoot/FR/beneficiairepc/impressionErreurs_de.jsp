<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<!-- Creer l'enregistrement si il n'existe pas -->
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran = "CAF2017";
	AFQuittanceViewBean viewBean =(AFQuittanceViewBean) session.getAttribute("viewBean");
	bButtonCancel = false;
%>
<%
String MSG_PROCESS_OK = "The process successfully started.";
if ("FR".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "La tâche a démarré avec succès.";
} else if ("DE".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "Prozess erfolgreich gestartet.";
}
%>
<%@page import="globaz.naos.db.beneficiairepc.AFQuittanceViewBean"%>
<%@page import="globaz.globall.util.JAUtil"%>
<%@page import="java.util.Vector"%>
<%@page import="globaz.naos.db.beneficiairepc.AFImpressionQuittanceViewBean"%>
<SCRIPT language="JavaScript">

</SCRIPT> 
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>

<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/swap.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript">

function add() {
}

function upd() {
}


function validate() {
	document.forms[0].elements('userAction').value="naos.beneficiairepc.impressionErreurs.imprimerErreurs";
	return true;
}

function cancel() {
}

function del() {
}

function init(){
	document.forms[0].elements('_method').value ="upd";
	<!-- document.forms[0].elements('dateEvaluation').onkeyup = new Function("","nouveauCasChanged()"); -->
}

function postInit(){
	document.forms[0].elements("dateEvaluation").tabIndex= "-1";
}

function updateNumAffilie(tag){
	if(tag.select && tag.select.selectedIndex != -1){
		document.getElementById('nomBenef').value =  tag.select[tag.select.selectedIndex].nom;
	}	
}

function trouverAdresse(){
		 parent.fr_main.location.href ="<%=(servletContext + mainServletPath)%>?userAction=naos.beneficiairepc.impression.afficher&numAffilie=" + document.forms[0].elements('numAffilie').value;
}

function updateForm(tag){
	field = document.forms[0].elements('nomPrenom');
	if (tag.select) {
		nom = tag.select[tag.select.selectedIndex].nom;
		field.value = nom;
		if(field.readOnly==false) {
			field.readOnly = true;
			field.className = 'disabled';
			field.tabIndex = -1;						
		}
		document.dernieresEcritures.location.href='<%=request.getContextPath()%>/pavoRoot/lastentries.jsp?compteIndividuelId='+tag.select[tag.select.selectedIndex].idci;
	}
}

function resetNom() {
	field = document.forms[0].elements('nomPrenom');
	field.value = '';
	field.readOnly = false;
	field.className = 'libelle';
	field.tabIndex = 0;
}

function casExistantChanged() {
	if(document.forms[0].elements("casExistant").checked){
		document.forms[0].elements("heures").className = 'disabled';
		document.forms[0].elements("heures").disabled = true;
		document.forms[0].elements("dateEvaluation").className = 'disabled';
		document.forms[0].elements("dateEvaluation").disabled = true;
		document.forms[0].elements("nbreQuittances").className = 'disabled';
		document.forms[0].elements("nbreQuittances").disabled = true;
		document.forms[0].elements("dateEvaluation").value = "";
	}else{
		document.forms[0].elements("heures").className = 'enabled';
		document.forms[0].elements("heures").disabled = false;
		document.forms[0].elements("dateEvaluation").className = 'enabled';
		document.forms[0].elements("dateEvaluation").disabled = false;
		document.forms[0].elements("nbreQuittances").className = 'enabled';
		document.forms[0].elements("nbreQuittances").disabled = false;
	}	
}

function nombreChanged(){
	if(document.forms[0].elements("nbreQuittances").value.length > 0){
		document.forms[0].elements("heures").className = 'disabled';
		document.forms[0].elements("heures").disabled = true;
		document.forms[0].elements("dateEvaluation").className = 'disabled';
		document.forms[0].elements("dateEvaluation").disabled = true;
		document.forms[0].elements("casExistant").disabled = true;
		document.forms[0].elements("dateEvaluation").value = "";
	}else{
		document.forms[0].elements("heures").className = 'enabled';
		document.forms[0].elements("heures").disabled = false;
		document.forms[0].elements("dateEvaluation").className = 'enabled';
		document.forms[0].elements("dateEvaluation").disabled = false;
		document.forms[0].elements("casExistant").disabled = false;
	}
}

function nouveauCasChanged(){
	if((document.forms[0].elements("heures").value.length > 0) || (document.forms[0].elements("dateEvaluation").value.length > 0)){
		document.forms[0].elements("nbreQuittances").className = 'disabled';
		document.forms[0].elements("nbreQuittances").disabled = true;
		document.forms[0].elements("casExistant").disabled = true;
	}else{
		document.forms[0].elements("nbreQuittances").className = 'enabled';
		document.forms[0].elements("nbreQuittances").disabled = false;
		document.forms[0].elements("casExistant").disabled = false;
	}
}


</SCRIPT> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
				Impression de la liste d'erreurs de la facturation
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
<TR>
							<TD> <strong>Journal des quittances</strong></TD>
			</TR>
			<TR>
				<TD>&nbsp;</TD>
			</TR>
			<TR>
				<TD><ct:FWListSelectTag name="idJournal" data="<%=viewBean.getListeJournauxPourImpression()%>" defaut=""/></TD>
			</TR>
			<TR>
				<TD>&nbsp;</TD>
			</TR>
<TR> 
            <TD>Adresse E-Mail</TD>
            <TD> 
              <input name='adresseEmail' class='libelleLong' value='<%=viewBean.getAdresseEmail()%>'>
            </TD>
</TR>
		<% 
		 if (viewBean.getIsProcessRunning().equals("TRUE")) { 
		 %>
		 <TR class="title">
			<TD colspan="4" style="color:white; text-align:center">
			<SPAN style="color:palegreen">&gt;</SPAN> <%=MSG_PROCESS_OK%> <SPAN style="color:palegreen">&lt;</SPAN>
			</TD>
		</TR>
		<% 
		}
		%>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>