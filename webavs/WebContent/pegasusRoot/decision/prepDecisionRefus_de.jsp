<%@page import="globaz.pegasus.vb.decision.PCPrepDecisionRefusViewBean"%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>

<%@ include file="/theme/process/header.jspf" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDecision"%>
<%@page import="ch.globaz.pyxis.business.model.TiersSimpleModel"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneSimpleModel"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.prestation.interfaces.tiers.PRTiersHelper"%>
<%@page import="globaz.globall.parameters.FWParametersCodeManager" %>
<%@page import="globaz.globall.vb.BJadePersistentObjectViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>

<%@page import="java.util.ArrayList" %>
<%@page import="globaz.jade.admin.user.service.JadeUserService"%>
<%@page import="globaz.jade.admin.JadeAdminServiceLocatorProvider"%>
<%@ page import="globaz.jade.json.MultiSelectHandler" %>
<%
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

	//LES PREFIXES DE CETTE PAGES COMMENCENT PAR JSP_PC_PREP_DECISION_REFUS_DE
	PCPrepDecisionRefusViewBean viewBean = (PCPrepDecisionRefusViewBean) session.getAttribute("viewBean"); 



	selectedIdValue=viewBean.getId();

	boolean viewBeanIsNew="add".equals(request.getParameter("_method"));

	//bButtonDelete = false;
	idEcran="PPC0085";
	autoShowErrorPopup = false;
	MultiSelectHandler handler = new MultiSelectHandler();
	String titleErroxBox = objSession.getLabel("JSP_GLOBAL_ERROR_BOX_TITLE");//titre box erreur

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<%-- ********************************** HACK CACHER LE LIEN AFFICHER LES ERREURS ********************** --%>
<% 
vBeanHasErrors = false;
%>
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/decision/preparation.css"/>

<%-- tpl:insert attribute="zoneScripts" --%>
<!--  Pour gestion de la liste a deux niveau -->
<script type="text/javascript" src="<%=servletContext%>/pegasusRoot/scripts/decision/decisionListHandlerPrep.js"></script>
<script type="text/javascript">
//Tableau taille code systeme motif

var ACTION_DECISION="<%=IPCActions.ACTION_DECISION%>";
var ACTION_DECISION_PREP = "<%=IPCActions.ACTION_DECISION_REF_PREPARATION%>";
var actionMethod;
var userAction;
var $champsLibreMotif;
var $champMotif;
var $valChampLibreMotif;
var motifRenonciation = "<%= viewBean.getCsCodeRenonciation() %>";

function add() {}
function upd() {}
$(function(){
	
	$champsLibreMotif = $('#champLibreMotif').hide();
	$champMotif = $('#motifListe');
	$valChampLibreMotif = $('#valChampLibreMotif');
	
	$champMotif.change(function () {
		var csMotif = $('#motifListe :selected').attr('value');
		
		if(csMotif === motifRenonciation){	
			$champsLibreMotif.show();
		}else{
			$champsLibreMotif.hide();
			$valChampLibreMotif.html('');
		}
	});
	
	actionMethod=$('[name=_method]',document.forms[0]).val();
	userAction=$('[name=userAction]',document.forms[0])[0];
	//gestion erreurs
	pegasusErrorsUtils.dealErrors(<%= request.getParameter("decisionErrorMsg") %>,"<%= titleErroxBox %>");
	
	
});



function cancel() {
	userAction.value=ACTION_DECISION+".chercher";
}  

function validate() {
	
	state = true;
	userAction.value=ACTION_DECISION_PREP;
	return state;
}  




/*var multiSelectData = [
          				{"value":"64043001","libelle":"ok1"},
          				{"value":"64043003","libelle":"ok2"},
          				{"value":"64043004","libelle":"ok3"},
          				{"value":"64043005","libelle":"ok4","childrens":[
          					{"value":"64044001","libelle":"Domici en Sdepuis - de 10 ans, + 5 ans, paysmais rente supérieure au minimum de l\u0027échelle 44"},{"value":"64044002","libelle":"Doisse depuis - de 5 ans, pays conventionné mais rente inférieure au minimum de l\u0027 échelle 44"},{"value":"64044003","libelle":"Domicilié en Suisse depuis - de 10 ans, pays non-conventionné"},{"value":"64044004","libelle":"Pas de rente, pays non-conventionné"}
          				    ],"childrensLibelle":"Sousliste2"},
          				{"value":"64043006","libelle":"ok5"},
          				{"value":"64043007","libelle":"ok6"},
          				{"value":"64043009","libelle":"ok7"},
          				{"value":"64043012","libelle":"ok8","childrens":[
          					{"value":"64044001","libelle":"Domicilié en Suisse depuis - de 10 ans, + 5 ans, pays conventionné mais rente supérieure au minimum de l\u0027échelle 44"},{"value":"64044002","libelle":"Domicilié en Suisse depuis - de 5 ans, pays conventionné mais rente inférieure au minimum de l\u0027 échelle 44"},{"value":"64044003","libelle":"Domicilié en Suisse depuis - de 10 ans, pays non-conventionné"},{"value":"64044004","libelle":"Pas de rente, pays non-conventionné"}
          					],"childrensLibelle":"Sousliste"
          				}
          				]
          				;*/
var multiSelectData = <%= handler.createMotifsWithSousMotifs(viewBean.CS_MOTIF) %>;
var initValue = {"masterValue":"64043005","childValue":"64044002"};


</script>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PC_PREP_DECISION_REFUS_DE_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>  
						<td> 	
			<table width="90%">
				<tr>
					<td>
						<span id="lblRequerantR">
							<ct:FWLabel key="JSP_PC_PREP_DECISION_REFUS_DE_REQU"/>
						</span>
					</td>
					<td colspan="3">
						<span id="requerant">
							<%=viewBean.getRequerantInfos(viewBean.getPersonneEtendue(),viewBean.getTiers()) %>
						</span>
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>
						<span id="lblDemandeNo">
							<ct:FWLabel key="JSP_PC_PREP_DECISION_REFUS_DE_DEMANDENO"/>
						</span>
					</td>
					<td>
						<span id="valDemandeNo">
							<%= viewBean.getDemande().getSimpleDemande().getId() %>
						</span>
					</td>
				</tr>
				</table>
				<div id="decisionDuR">
					<span id="lblDecisionDuR">
						<ct:FWLabel key="JSP_PC_PREP_DECISION_REFUS_DE_DECDU"/>
					</span>
					<span id="valDecisionDuR">
						<input type="text" data-g-calendar="mandatory:true" name="decisionRefus.decisionHeader.simpleDecisionHeader.dateDecision" value=""/>
					</span>
				</div>
				
				<div id="dateRefus">
					<span id="lblDateRefus">
						<ct:FWLabel key="JSP_PC_PREP_DECISION_REFUS_DE_DATEREFUS"/>
					</span>
					<span id="valDateRefus">
						<input type="text" data-g-calendar="mandatory:true" name="decisionRefus.simpleDecisionRefus.dateRefus" value=""/>
					</span>
				</div>
				
<!--				<div id="selectM">-->
<!--					<span id="lblSelectM">-->
<!--						<ct:FWLabel key="JSP_PC_PREP_DECISION_REFUS_DE_MOTIF"/>-->
<!--					</span>-->
<!--					<span id="selSelectM">-->
<!--						<ct:select styleClass="motif" id="selectMotif" name="decisionRefus.simpleDecisionRefus.csMotif" wantBlank="true">-->
<!--								<ct:optionsCodesSystems csFamille="PCMOTIFDER">-->
<!--								</ct:optionsCodesSystems>-->
<!--						</ct:select>-->
<!--					</span>-->
<!--				</div>-->
<!--				<div class="selectSm">-->
<!--					<span id="lblSelectSm">-->
<!--						<ct:FWLabel key="JSP_PC_PREP_DECISION_REFUS_DE_SOUSMOTIF"/>-->
<!--					</span>-->
<!--					<span>-->
<!--						<select id="selectSousMotif" name="decisionRefus.simpleDecisionRefus.csSousMotif" value=""></select>-->
<!--					</span>-->
<!--				</div>-->
					<div id="zoneInfosRefusSupp">
						
						<label id="motifsLabel"><ct:FWLabel key="JSP_PC_PREP_DECISION_REFUS_DE_MOTIF"/>
						</label>
						<select id="motifListe" name="decisionRefus.simpleDecisionRefus.csMotif" data-g-multiselect=" dataList:multiSelectData,initValues :initValue,subLevelId:sousMotifListe,subLevelLibelle:sous-motifs"></select>
						<label id="sousMotifsLabel"></label>
						<select id="sousMotifListe" name="decisionRefus.simpleDecisionRefus.csSousMotif"></select>
					</div>
					<div id="champLibreMotif">
						<span id="lblChampLibreMotif">
								<ct:FWLabel key="JSP_PC_PREP_DECISION_REFUS_DE_CHAMP_LIBRE_MOTIF"/>
							</span>
							<span id="spanChampLibreMotif">
								<textarea id="valChampLibreMotif" data-g-string="sizeMax:255" name="decisionRefus.simpleDecisionRefus.champLibreMotif" maxlength="255" ><%=viewBean.getDecisionRefus().getSimpleDecisionRefus().getChampLibreMotif()%></textarea>
							</span>
					</div>
				
				<input type="hidden" name="idDemandePc" value="<%=viewBean.getIdDemande()%>" />
					</td>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>