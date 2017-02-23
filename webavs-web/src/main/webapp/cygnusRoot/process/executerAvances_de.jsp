<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	/**
		Mandat INFOROM 547 - 19.02.2013 - SCE
		Page d'exécution du traitement des avances pour le module RFM
		Cette page permet de lancer le traitement de paiement des avances unqiues, et de sortir la liste des avances (global pour WebAvs)
	*/
	idEcran="PRF0063";
	
	//Les labels de cette page commence par la préfix "JSP_AVANCE_RFM_E"
	userActionValue="cygnus.process.executerAvances.executer";
	
	RFExecuterAvancesViewBean viewBean = (RFExecuterAvancesViewBean)(session.getAttribute("viewBean"));
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.corvus.api.avances.IREAvances"%>

<SCRIPT>

// bloc de date de paiement 
var $blockDatePaiement;
//select du choix de type de traitement
var $selectTypeTraitement;
//bloc ordre groupé
var $blocOg;

//Fonction validate, avant lancement process
function validate () {
	//récupération du type d'avance, 
	var typeAvance = $('#selectTypeTraitement option:selected').val();
	//on set user action
	document.forms[0].elements('userAction').value = "cygnus.process.executerAvances.executer";
    var form=document.forms[0];
  //Si traitement liste, on check pas les paramètres mandatory, 
	if (typeAvance == <%=IREAvances.CS_TYPE_LISTES%>) {
		state = true;
	}else{
		//ok, si mandatory ok...
		state = notationManager.validateAndDisplayError();
	}
	
	
    return state;
}

function selectChangeISO20022(s) {
	changeAffichageISO20022(s[s.selectedIndex].id);
}

function changeAffichageISO20022(cs) {
	if(cs==258003){ // 258003 traitement ISO20022
		$('.classIso').css('display', 'block'); $('.classNonIso').css('display', 'none');
	} else {
		$('.classNonIso').css('display', 'block'); $('.classIso').css('display', 'none');
	} 
}
function selectChangePriority(s) {
	showPriorWarning(s[s.selectedIndex].value);
}
function showPriorWarning(val){
	if(val==1){
		$('.classPrioWarn').css('display', 'block');
	}else{
		$('.classPrioWarn').css('display', 'none');
	}
}

$(function () {
	
	//init variable
	$blockDatePaiement = $('#blockDatePaiement');
	$selectTypeTraitement = $('#selectTypeTraitement');
	$blocOg = $('#blocOg');
	
	//init affichage selon OE
	selectChangeISO20022(document.getElementById("idOrganeExecution"));
	selectChangePriority(document.getElementById("isoHighPriority"));
	
	//on cache le bloc date valeur, par défaut, car le type de traitement par defaut est paiement unique
	$blockDatePaiement.hide();
	
	//init on change select
	$selectTypeTraitement.change(function () {
		
		var typeAvance = $('#selectTypeTraitement option:selected').val();
		//accompte unique, on cacahe la date de valeur
		if (typeAvance == <%=IREAvances.CS_TYPE_ACOMPTES_UNIQUE%>) {
			$blockDatePaiement.hide();	
			$blocOg.show();
		}
		//liste on affiche
		else {
			$blockDatePaiement.show();
			$blocOg.hide();
		}	
	});
	
});
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>		
<%@page import="globaz.cygnus.vb.process.RFExecuterAvancesViewBean"%>
<%@page import="java.util.Vector"%>
<%@page import="globaz.corvus.utils.REPmtMensuel"%><ct:FWLabel key="JSP_AVANCE_RFM_E_TITLE"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD align="left">
								<TABLE width="60%">
								<TR>
								<TD><ct:FWLabel key="JSP_AVANCE_RFM_E_ADRESSE_EMAIL"/>&nbsp;
								</TD>
								<TD>
								    <INPUT type="text" name="eMailAddress" size="40" value="<%=eMailAddress!=null?eMailAddress:""%>">
								</TD>
								<TD>&nbsp;</TD>
								</TR>
								

								<TR>
								<TD><ct:FWLabel key="JSP_AVANCE_RFM_E_TYPE"/>&nbsp;
								</TD>
								<TD>
								    <ct:select id="selectTypeTraitement" name="csTypeAvance" defaultValue="<%=viewBean.getCsTypeAvance()%>" wantBlank="false">
										<ct:optionsCodesSystems csFamille="RETYPAVANC">	
											<ct:excludeCode code="52854001"/>											
										</ct:optionsCodesSystems>
									</ct:select>
								    
								</TD>
								<TD>&nbsp;</TD>
								</TR>
								
								
								<TBODY id="blockDatePaiement">								
								<TR>
									<TD><ct:FWLabel key="JSP_AVANCE_RFM_E_DATE_VALEUR"/>&nbsp;</TD>											
									<TD><INPUT type="text" class="disabled" name="mois" value="<%=REPmtMensuel.getDateDernierPmt(objSession)%>" readonly></TD>
									    <INPUT type="hidden" name="datePaiement" value="<%=REPmtMensuel.getDateDernierPmt(objSession)%>">
									</TD>
									<TD>&nbsp;</TD>
								</TR>
								</TBODY>	
								
								<tbody id="blocOg">
									<!--  zone ordre groupé -->
									<!--  ligne date échéance -->
									<tr><td>&nbsp;<td><td>&nbsp;<td></tr>
									<tr>
										<TD><ct:FWLabel key="JSP_AVANCE_RFM_E_DATE_ECHEANCE"/>&nbsp;</TD>
										<TD><input type="text" name="dateEcheance" data-g-calendar="mandatory:true"</TD>
									</tr>
									
									<!--  ligne choix organes execution -->
									<tr>
										<TD><ct:FWLabel key="JSP_AVANCE_RFM_E_ORGANE_EXECUTION"/>&nbsp;</TD>
										<TD>
<%-- 											<ct:FWListSelectTag	name="idOrganeExecution" data="<%=viewBean.getOrganesExecution()%>" defaut="" /> --%>
												<% 	Vector<String[]> _CsOrganeExecution = viewBean.getOrganesExecution();%> 
												<select id="idOrganeExecution" name="idOrganeExecution" onchange="selectChangeISO20022(this)">
								                <%for (int i=0; i < _CsOrganeExecution.size(); i++) {
													String[] _organeExecution = _CsOrganeExecution.get(i);%>
													
									                <option value="<%=_organeExecution[0]%>" id="<%=_organeExecution[2]%>"><%=_organeExecution[1]%></option>
									           	<%} %>
										</TD>
									</tr>
									
									<!--  ligne choix numéro OG -->
									<tr class="classNonIso">
										<TD><ct:FWLabel key="JSP_AVANCE_RFM_E_NO_OG"/>&nbsp;</TD>
										<TD><input type="text" style="width:25px;" name="noOg" data-g-integer="mandatory:true, sizeMax:2"></TD>
									</tr>
									<tr class="classIso">
										<TD><ct:FWLabel key="JSP_AVANCE_RFM_E_ISO_GEST"/>&nbsp;</TD>
										<TD><input type="text" name="isoGestionnaire" id="isoGestionnaire" value="<%=viewBean.getIsoGestionnaire()%>" data-g-string="mandatory:true" /></TD>
									</tr>
									<tr class="classIso">
										<TD><ct:FWLabel key="JSP_AVANCE_RFM_E_ISO_PRIO"/>&nbsp;</TD>
										<td>
											<select id="isoHighPriority" name="isoHighPriority" onchange="selectChangePriority(this)">
								                <OPTION selected value="0"><ct:FWLabel key="JSP_COMMOM_ISO_PRIO_NORMALE"/></OPTION>
								                <OPTION value="1"><ct:FWLabel key="JSP_COMMOM_ISO_PRIO_HAUTE"/></OPTION>
							              	</select><span class="classPrioWarn" style="color:red"><ct:FWLabel key="JSP_COMMOM_ISO_PRIO_WARNING"/></span>
										</td>
									</tr>
								</tbody>					
								</TABLE>
						</TD></TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>