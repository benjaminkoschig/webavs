<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="ch.globaz.pegasus.business.services.PegasusServiceLocator"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul"%>
<%@page import="ch.globaz.pegasus.business.constantes.decision.DecisionTypes"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="ch.globaz.pegasus.business.models.decision.DecisionApresCalcul"%>	
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pegasus.vb.demande.PCImprimerBillagViewBean"%>
<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.pegasus.utils.BusinessExceptionHandler"%>

<%
	//Les labels de cette page commence par le préfix "JSP_PC_DECALCUL_D"
	idEcran="PPC0098";
	
	//viewbean
	PCImprimerBillagViewBean viewBean = (PCImprimerBillagViewBean) session.getAttribute("viewBean");
	
	//PopUp
	autoShowErrorPopup = true;
	//Bouton delete
	boolean viewBeanIsNew= false;
	bButtonCancel = false;
	bButtonValidate = false;
	bButtonDelete = false;
	
	String idErrorMsg=(String)request.getAttribute("errorMsg");
	String decisionErrorMsg = "";
	
	if(!JadeStringUtil.isEmpty(idErrorMsg)){
		decisionErrorMsg=BusinessExceptionHandler.getErrorMessage(idErrorMsg,null);
	}
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<script type="text/javascript" src="<%=rootPath %>/scripts/demande/imprimerBillag.js"></script>
<link rel="stylesheet" type="text/css" media="screen" href="<%=rootPath%>/css/decision/imprimerDecisions.css">
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">
var actionMethod;
var userAction = "<%=IPCActions.ACTION_DECISION_AC_IMPRIMER_BILLAG %>";
function add() {}
function upd() {}

/*
 * Initialistaion
 */
function init() {
	initEvents();
}
function cancel() {}  
function validate() {}    
function del() {}

var validInputs = function () {
	

	if( ($("#dateDoc").val() === '') || ($("#dateDebut").val() === '') || ($("#mailGest").val() === '')){
		alert('<%= objSession.getLabel("PEGASUS_JSP_PPC0098_CHAMPS_OBLIGATOIRE")%>');
		return false;
	}else{
		return true;
	}
}

$(function () {
	
	var $gestionnaires_liste = $('#gestionnaire');
	$("#gestionnaire option[value='']").remove();
	
	
	if($gestionnaires_liste.val() === ''){
		$("#gestionnaire").val($("#gestionnaire option:first").val());
	}
	
})

</script>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PC_IMPRBILLAG_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsbillagimpression">
	<ct:menuSetAllParams key="idDossier" value="<%= viewBean.getIdDossier() %>"/>
</ct:menuChange>
<tr>
	<td><ct:FWLabel key="JSP_PC_CREANCIE_ACCORDEE_D_REQUERANT" /> </td>
	<td> <%=viewBean.getRequerant()%></td>
</tr>
</tr>
	<tr><td>&nbsp;</td></tr>
<tr>
	<td><ct:FWLabel key="JSP_PC_IMPRBILLAG_DEMANDE_NO" /> </td>
	<td><%=viewBean.getIdDemandePc()%></td>
</tr>
<tr><td>&nbsp;</td></tr>
<!--  date document -->
<tr>			
	<td><ct:FWLabel key="JSP_PC_IMPRDECAL_DATEDOC"/></td>
	<td><input type="text" id="dateDoc" name="dateDoc" data-g-calendar="mandatory:true " value="<%= viewBean.getDateNow() %>"/></td>
</tr>
<!--  date droit federal document -->
<tr>			
	<td><ct:FWLabel key="JSP_PC_IMPRDECAL_DATE_DROIT_PC"/></td>
	<td><input type="text" id="dateDebut" name="dateDebut" data-g-calendar="mandatory:true,type:month " value="<%= viewBean.getDemande().getSimpleDemande().getDateDebut() %>"/></td>
</tr>

<tr>
	<td><ct:FWLabel key="JSP_PC_IMPRDECAL_GESTIONNAIRE"/></td>
	<td>
		
		<ct:FWListSelectTag name="gestionnaire" data="<%=PCGestionnaireHelper.getResponsableData(objSession)%>" 
					notation="data-g-select='mandatory:true'" defaut="<%=objSession.getUserId()%>" />
	</td>
</tr>
	<tr><td>&nbsp;</td></tr>


<tr>
	<td><ct:FWLabel key="JSP_PC_IMPRDECAL_EMAIL"/></td>
	<td><input type="text" id="mailGest" name="mailGest" data-g-string="mandatory:true" class="clearable" value="<%= viewBean.getMailGestionnaire(objSession) %>" />	</td>
</tr>

<tr><td>&nbsp;</td>
	<tr>
	<td colspan="3" align="center" id="thPrintDec">
		<input type="button" id="printDec" name="printDec" value="<ct:FWLabel key="JSP_PC_IMPRBILLAG_PRINT_SELECT" />"/>
		<input type="hidden" name="idDemandePc" value="<%=viewBean.getIdDemandePc()%>"/>
		<input type="hidden" name="idDossier" value="<%= viewBean.getIdDossier() %>"/>
		<input type="hidden" name="nss" value="<%= viewBean.getNSS()%>"/>
		<input type="hidden" name="idTiers" value="<%=viewBean.getIdTiers() %>" />
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
