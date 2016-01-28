<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<!-- Creer l'enregistrement si il n'existe pas -->
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran = "CAF2009";
	AFImpressionQuittanceViewBean viewBean =(AFImpressionQuittanceViewBean) session.getAttribute("viewBean");
	bButtonCancel = false;
	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	int autoDigiAff = globaz.naos.util.AFUtil.getAutoDigitAff(session);
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

function updateNumAffilie(tag){
	if(tag.select && tag.select.selectedIndex != -1){
		document.getElementById('numAffilie').value =  tag.select[tag.select.selectedIndex].value;
		parent.fr_main.location.href ="<%=(servletContext + mainServletPath)%>?userAction=naos.beneficiairepc.quittance.modifier&numAffilie=" + document.getElementById('numAffilie').value;
	}	
}

function add() {
}

function upd() {
}


function validate() {
	document.forms[0].elements('userAction').value="naos.beneficiairepc.impression.imprimerQuittance";
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
				Impression d'une quittance pré-remplie
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
<TR>
									<td>N° affili&eacute;</td>         
									<td>
											<ct:FWPopupList 
											name="numAffilie" 
											value="<%=viewBean.getNumAffilie()%>" 
											className="libelle" 
											jspName="<%=jspLocation%>" 
											autoNbrDigit="<%=autoDigiAff%>" 
											size="25"
											onChange="updateNumAffilie(tag);"
											minNbrDigit="6"/>
										<!--IMG
											src="<%=servletContext%>/images/down.gif"
											alt="presser sur la touche 'flèche bas' pour effectuer une recherche"
											title="presser sur la touche 'flèche bas' pour effectuer une recherche"
											onclick="if (document.forms[0].elements('numAffilie').value != '') numAffiliePopupTag.validate();"-->		
										<%
											Object[] tiersMethodsName= new Object[]{
												new String[]{"setNumAffilie","getNumAffilieActuel"},
												new String[]{"setIdTiers","getIdTiers"},};
											Object[] tiersParams = new Object[]{
												new String[]{"selection","_pos"},};
											String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/beneficiairepc/impression_de.jsp";
										%>
										<ct:FWSelectorTag 
											name="tiersSelector" 
											methods="<%=tiersMethodsName%>"
											providerApplication ="pyxis"
											providerPrefix="TI"
											providerAction ="pyxis.tiers.tiers.chercher"
											providerActionParams ="<%=tiersParams%>"
											redirectUrl="<%=redirectUrl%>"/>
										<input type="hidden" value="<%=viewBean.getIdTiers()%>" name="idTiers">
									</td>
									<td>
								<input type="text" name="nomBenef" disabled="disabled" readonly="readonly" value="<%=viewBean.getNomBenefJSP()%>"></input>
							</td>
</TR>
<!--  
<TR>
	<TD>Adresse du bénéficiaire</TD>
	<TD><input type="textarea" name="adresseBeneficiaire" value=""> </TD>
</TR>
-->
<TR>
	<TD>&nbsp;</TD>
</TR>
<TR>
	<TD>&nbsp;</TD>
</TR>
<TR>
	<TD>Cas existant</TD>
	<TD><input tabindex="-1" type="checkbox" name="casExistant" onclick="casExistantChanged();"></TD>
</TR>
<TR>
	<TD>&nbsp;</TD>
</TR>
<TR>
	<TD>&nbsp;</TD>
</TR>
<TR>
	<TD>Nouveau cas</TD>
	<TD>Heures</TD>
	<TD><input  tabindex="-1"type="texte" name="heures" onkeyup="nouveauCasChanged();"></TD>
</TR>
<TR>
	<TD>&nbsp;</TD>
	<TD>Evaluation du</TD>
	<TD><ct:FWCalendarTag name="dateEvaluation" value=""/></TD>
</TR>
<SCRIPT language="javascript">	    				        	
	document.forms[0].elements("dateEvaluation").onkeyup= new Function ("","return nouveauCasChanged();");
</SCRIPT>
<TR>
	<TD>&nbsp;</TD>
</TR>
<TR>
	<TD>&nbsp;</TD>
</TR>
<TR>
	<TD>Quittances à retourner, nombre</TD>
	<TD><input type="texte" tabindex="-1" name="nbreQuittances" onkeyup="nombreChanged();"></TD>
</TR>
<TR>
	<TD>&nbsp;</TD>
</TR>
<TR>
	<TD>&nbsp;</TD>
</TR>
<TR> 
            <TD height="50%" width="212">Adresse E-Mail</TD>
            <TD width="50%"> 
              <input name='adresseEmail' class='libelleLong' value='<%=viewBean.getAdresseEmail()%>'>
            </TD>
</TR>
<TR> 
            <TD height="50%" width="212">Envoi quittances par</TD>
            <TD width="50%"> 
              <input name='user' class='libelleLong' value='<%=viewBean.getUser()%>'>
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