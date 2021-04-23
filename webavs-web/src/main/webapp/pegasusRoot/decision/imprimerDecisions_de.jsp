<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="ch.globaz.pegasus.businessimpl.utils.PCGedUtils"%>
<%@page import="ch.globaz.pegasus.business.services.PegasusServiceLocator"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul"%>
<%@page import="ch.globaz.pegasus.business.constantes.decision.DecisionTypes"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="ch.globaz.pegasus.business.models.decision.DecisionApresCalcul"%>	
<%@page import="globaz.pegasus.utils.BusinessExceptionHandler"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pegasus.vb.decision.PCImprimerDecisionsViewBean"%>
<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%
	//Les labels de cette page commence par le préfix "JSP_PC_DECALCUL_D"
	idEcran="PPC0089";
	//Labels divers
	String btnAddAnnexes = objSession.getLabel("JSP_PC_DECALCUL_BTNADDANNEXE");//bouton annexes
	String preValidLibelle = objSession.getLabel("JSP_PC_DECALCUL_D_BTN_PREVALIDER");//bouton preValid
	
	//Message erreur si saisie annexe vide
	String alertEmptyString = objSession.getLabel("JSP_PC_DECALCUL_ERRORMSG_ANNEXE");
	
	//viewbean
	PCImprimerDecisionsViewBean viewBean = (PCImprimerDecisionsViewBean) session.getAttribute("viewBean");
	
	String idErrorMsg=(String)request.getAttribute("errorMsg");
	String decisionErrorMsg = "";
	
	if(!JadeStringUtil.isEmpty(idErrorMsg)){
		decisionErrorMsg=BusinessExceptionHandler.getErrorMessage(idErrorMsg,null);
	}
	//PopUp
	autoShowErrorPopup = true;
	//Bouton delete
	boolean viewBeanIsNew= false;
	bButtonCancel = false;
	bButtonValidate = false;
	bButtonDelete = false;
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<script type="text/javascript" src="<%=rootPath %>/scripts/decision/imprimerDecisions.js"></script>
<link rel="stylesheet" type="text/css" media="screen" href="<%=rootPath%>/css/decision/imprimerDecisions.css">
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">
var actionMethod;
var userAction = "<%=IPCActions.ACTION_DECISION_AC_IMPRIMER %>";
function add() {}
function upd() {}
/*
 * Validation de la decision, retourn le booleen et l'url
 */
function validateDecision(){}
/*
 * Initialistaion
 */
function init() {
	//initEvents();
}
function cancel() {}  
function validate() {}    
function del() {}

$(function () {
	var msgError="<%=decisionErrorMsg%>";
	if(msgError!=""){
		globazNotation.utils.consoleError(msgError);
	}
})
</script>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PC_IMPRDECAL_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsdecimpression">
	 
	<ct:menuSetAllParams key="idVersionDroit" value="<%=viewBean.getIdVersionDroit()%>"/>
	<ct:menuSetAllParams key="idDroit" value="<%=viewBean.getIdDroit()%>"/>
	<ct:menuSetAllParams key="noVersion" value="<%=viewBean.getNoVersion()%>"/>
	<ct:menuSetAllParams key="idDemandePc" value="<%=viewBean.getIdDemandePc()%>"/>
	<ct:menuSetAllParams key="idDecision" value="<%=viewBean.getIdDecision() %>"/>
	
</ct:menuChange>
<tr>
	<td><ct:FWLabel key="JSP_PC_CREANCIE_ACCORDEE_D_REQUERANT" /> </td>
	<td> <%=viewBean.getRequerant()%></td>
</tr>
<tr>
	<td><ct:FWLabel key="JSP_PC_VALIDATION_DECISIONS_D_ID_DROIT_NO_VERSION" /> </td>
	<td><%=viewBean.getIdDroit()%> / <%=viewBean.getNoVersion()%></td>
</tr>


<tr>
	<td><ct:FWLabel key="JSP_PC_IMPRDECAL_PREF"/></td>
	<td><ct:FWListSelectTag data="<%=PCGestionnaireHelper.getResponsableData(objSession)%>" defaut="<%=viewBean.getPersonnePrep() %>" name="persref" notation="data-g-select='mandatory:true'"/></td>		
</tr>

<tr>
	<td><ct:FWLabel key="JSP_PC_IMPRDECAL_EMAIL"/></td>
	<td><input type="text" name="mailGest" class="clearable" value="<%= viewBean.getMailGestionnaire(objSession) %>" />	</td>
</tr>

<!--  Gestion envoi en ged -->
	<% if(viewBean.isDecisionsForGed()) {
		%>
			<tr>
				<td><ct:FWLabel key="JSP_PC_IMPRDECAL_GED_CHECKBOX_LABEL" /></td>
				<td><input type="checkbox"  name="toGed" <%=viewBean.getToGed()? "checked" : ""%>/></td>
			</tr>
		<%
	}%>
<tr><td>&nbsp;</td></tr>

		<td colspan="3">
			

		<table id="decisionsTable" >
			<thead>
				<tr>
					<th><input id="headerCheckBox" type="checkbox" data-g-mastercheckbox=" "/></th>
					<th><ct:FWLabel key="JSP_PC_IMPRDECAL_NO_DECISION" /></th>
					<th><ct:FWLabel key="JSP_PC_IMPRDECAL_DATE_DECSISION" /></th>
					<th><ct:FWLabel key="JSP_PC_IMPRDECAL_PERIODE_DECSISION" /></th>
					<th><ct:FWLabel key="JSP_PC_IMPRDECAL_BENEFICIAIRE" /></th> 
					<th><ct:FWLabel key="JSP_PC_IMPRDECAL_D_ETAT" /></th>
				    <th><ct:FWLabel key="JSP_PC_IMPRDECAL_D_TYPE" /></th>
					
				<tr>
			</thead>
			<tbody>
				<%for(JadeAbstractModel model:viewBean.getListDecision()){
									DecisionApresCalcul decision = (DecisionApresCalcul)model; 
			%>   
			<tr id="<%=decision.getSimpleDecisionApresCalcul().getId() %>">
			   	<%if(viewBean.getIdDecision().trim().toString().equals(decision.getDecisionHeader().getSimpleDecisionHeader().getIdDecisionHeader().trim().toString())){%>
				   <td class="thChkPrint"><input class="chkPrint" type="checkbox" checked="true"/></td>
			 	<%  }else{%>
				  <td class="thChkPrint"><input class="chkPrint" type="checkbox" /></td>
				<%  } %> 
			   	<td><%=decision.getDecisionHeader().getSimpleDecisionHeader().getNoDecision()%></td>
			   	<td><%=decision.getDecisionHeader().getSimpleDecisionHeader().getDateDecision()%></td>
			   	<td data-g-periodformatter=" "><%=decision.getDecisionHeader().getSimpleDecisionHeader().getDateDebutDecision() +" - "+decision.getDecisionHeader().getSimpleDecisionHeader().getDateFinDecision() %></td>
			   	<td><%=decision.getDecisionHeader().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel()+" "+decision.getDecisionHeader().getPersonneEtendue().getTiers().getDesignation1()+" "+decision.getDecisionHeader().getPersonneEtendue().getTiers().getDesignation2() %></td>
			   	<td><%=objSession.getCodeLibelle(decision.getDecisionHeader().getSimpleDecisionHeader().getCsEtatDecision())%></td>
			   	<td><%=objSession.getCodeLibelle(decision.getDecisionHeader().getSimpleDecisionHeader().getCsTypeDecision())%><input type="hidden" name="decisionsId" value=""/></td>
				
			</tr>
		<% } %>
			</tbody>
		</table>
		</td>
		
	</tr>
	
	
	<tr>
	<td colspan="3" align="center" id="thPrintDec">
		<input type="button" id="printDec" name="printDec" value="<ct:FWLabel key="JSP_PC_IMPRDECAL_PRINT_SELECT" />"/>
		<input type="hidden" id="typeDecision" name="typeDecision" value="<%= DecisionTypes.DECISION_APRES_CALCUL %>"/>
		<input type="hidden" name="idVersionDroit" value="<%=viewBean.getIdVersionDroit()%>"/>
		<input type="hidden" name="idDroit" value="<%=viewBean.getIdDroit()%>"/>
		<input type="hidden" name="noVersion" value="<%=viewBean.getNoVersion()%>"/>
		<input type="hidden" name="idDemandePc" value="<%=viewBean.getIdDemandePc()%>"/>
		<input type="hidden" name="idDecision" value="<%=viewBean.getIdDecision() %>"/>
	</td>
</tr>
<tr id="infoProcess">
<td colspan=3><ct:FWLabel key="JSP_PC_IMPRDECAL_INFOPROCESSMSG" /></td>
</tr>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>

<%@ include file="/theme/detail/footer.jspf" %>
	
<%-- /tpl:insert --%>
