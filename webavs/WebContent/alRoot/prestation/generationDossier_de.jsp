<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseSearchModel"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="ch.globaz.al.business.constantes.*"%>
<%@page import="globaz.al.vb.prestation.ALGenerationDossierViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page isELIgnored="false"%>

<%@page import="ch.globaz.al.business.services.models.prestation.RecapitulatifEntrepriseModelService"%>
<%@page import="ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel"%>
<%@page import="ch.globaz.al.business.models.prestation.DetailPrestationComplexModel"%>
<%@page import="ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel"%>
<%@page import="globaz.jade.client.util.JadeNumericUtil"%>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	ALGenerationDossierViewBean viewBean = (ALGenerationDossierViewBean) session.getAttribute("viewBean"); 
	selectedIdValue=viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("GENERER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	
	idEcran="AL0015";

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/dossier/generation.js"/></script>
<script type="text/javascript">
var PARAM_WARN_RETRO_BEFORE = <%=viewBean.getParamWarnRetroBefore()%>;
</script>
<%-- tpl:insert attribute="zoneScripts" --%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.helios.translation.CodeSystem"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/util_webaf.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/ajax_webaf.js"></script>
<script type="text/javascript" language="Javascript">

function add() {

    document.forms[0].elements('userAction').value="al.prestation.generationDossier.ajouter";
}
function upd() {
    document.forms[0].elements('userAction').value="al.prestation.generationDossier.modifier";
}
function validate() {
    state = validateFields();

    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.prestation.generationDossier.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.prestation.generationDossier.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	
	if(methodElement.value == ADD) {
		document.forms[0].elements('selectedId').value='<%=JavascriptEncoder.getInstance().encode(viewBean.getDossierComplexModel().getId()) %>';
		document.forms[0].elements('userAction').value="al.dossier.dossierMain.afficher";
	} else {
		document.forms[0].elements('selectedId').value='<%=JavascriptEncoder.getInstance().encode(viewBean.getDossierComplexModel().getId()) %>';
		document.forms[0].elements('userAction').value="al.dossier.dossierMain.afficher";
	}
}

function del() {
	var msgDelete = '<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="al.prestation.prestation.supprimer";
        document.forms[0].submit();
    }
}

function init(){
	initDebugManager();
	initMoreInfosElements('genAdvancedOptions','Content',1);	
}

function postInit(){
	
}

//définir cette méthode si traitement après remplissage ajax du formulaire
function callbackFillInputAjax(){
} 
 

$(function(){ 

	$( "#entetePrestationModel_periodeDe" ).blur(function() {
		
		if(this.value.length>3){
			var year= this.value.substr(this.value.length-4,4);
			var month = this.value.substr(0,2);
			
			var period=year+month;
			
			//on a un paramètre période (yyyy.MM)
			if(PARAM_WARN_RETRO_BEFORE>99999){
				if(parseInt(period)<PARAM_WARN_RETRO_BEFORE){
					showWarnPopup();
				}
				//on a un paramètre année (yyyy)	
			}else{
				if(parseInt(year)<PARAM_WARN_RETRO_BEFORE){
					showWarnPopup();
				}
			}	
		}
		
		
	});
	
	
	
	$( "#popupAL0015" ).hide();
	// run the currently selected effect
	function showWarnPopup() {
		//faire une alerte avec message avertissement
		
		alert(	$( "#popupAL0015" ).text());
	
	};

	//callback function to bring a hidden box back
	function callback() {
		
	
		
	setTimeout(function() {
		$( "#popupAL0015:visible" ).removeAttr( "style" ).fadeOut();	
		}, 2000 );
	};
	
	
});



</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%>		
			<ct:FWLabel key="AL0015_TITRE"/> (<ct:FWLabel key="AL0004_TITRE"/> <%=viewBean.getDossierComplexModel().getId() %>- 
			<%=viewBean.getDossierComplexModel().getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1() %>
			<%=viewBean.getDossierComplexModel().getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2()%>)
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<tr>
	<td>
		<div id="popupAL0015" class="popup_warn">
			<ct:FWLabel key="AL0015_POPUP_WARN_RETRO"/><br/><%=viewBean.getFormattedParamWarnRetroBefore()%>
			
		</div>
	<td>
</tr>
<tr><td>
		
	<%-- tpl:insert attribute="zoneMain" --%>
    	<table id="AL0015periodeZone" class="zone">
			<tr>
				<td class="label subtitle" colspan="2"><ct:FWLabel key="AL0015_TITRE_PERIODE"/></td>
			</tr>
			<tr>
			<td>
				<table style="width:70%">
					<tr>
						<td><ct:FWLabel key="AL0015_PERIODE_TRAITEMENT"/></td>
						<td>
							<input id="periodeTraitement" name="periodeTraitement" tabindex="1" 
							value="<%=viewBean.getPeriodeTraitement()%>" type="text" data-g-calendar="type:month">
						</td>		
					</tr>
			
					<tr>
						<td><ct:FWLabel key="AL0015_NUM_FACTURE"/></td> 
						<td>						
							<ct:inputText name="noFacture" id="noFactureValue" tabindex="2"/>
						</td>		
					</tr>
			
					<tr>
						<td><ct:FWLabel key="AL0015_GENERATION_PRESTATION"/></td>
						<td>			
							<input id="entetePrestationModel_periodeDe" name="entetePrestationModel.periodeDe" tabindex="3" 
							value="<%=viewBean.getEntetePrestationModel().getPeriodeDe()%>" type="text" data-g-calendar="type:month">
						
							<input id="entetePrestationModel_periodeA" name="entetePrestationModel.periodeA" tabindex="4" 
							value="<%=viewBean.getEntetePrestationModel().getPeriodeA()%>" type="text" data-g-calendar="type:month">
					
						</td>
				
					</tr>
				</table>
			</td>
			<td>
				<table style="width:50%">
					<tr>
						<td><ct:FWLabel key="AL0015_NUM_FACTURE_DISPO"/></td>
						<td>
					<%if(viewBean.getSearchRecapsExistantesAffilie().getSize()>0){ %>
					
						<select size="5" name="noFactureSelect" id="noFactureSelection">
	                    <%for (int i=0;i<viewBean.getSearchRecapsExistantesAffilie().getSize();i++){ 
	                   		RecapitulatifEntrepriseModel currentRecap = (RecapitulatifEntrepriseModel)viewBean.getSearchRecapsExistantesAffilie().getSearchResults()[i];
	                    	%>
	                    	<option al_process="noFactureSelection-<%=i%>" value="<%=currentRecap.getNumeroFacture()%>"><%=currentRecap.getNumeroFacture()%></option>
	                    
	                    <%} %>
	                   
	            		<select>
	            		<%for (int i=0;i<viewBean.getSearchRecapsExistantesAffilie().getSize();i++){ 
	                   		RecapitulatifEntrepriseModel currentRecap = (RecapitulatifEntrepriseModel)viewBean.getSearchRecapsExistantesAffilie().getSearchResults()[i];
	                    	%>
	                    	<input type="hidden" value="<%=currentRecap.getIdProcessusPeriodique()%>" id="noFactureSelection-<%=i%>"/>
	                    <%} %>
					<%} %>
						
						</td>
					</tr>
				</table>
				
			</td>
			</tr>
	
    	</table>
    	
    	<table id="AL0015optionsZone" class="zone">
			<tr>
				<td class="label subtitle" colspan="2"><ct:FWLabel key="AL0015_GENERATION_OPTIONS"/></td>
			</tr>
			<%if(!viewBean.getDroitComplexModel().isNew()) { %>
				<tr><td>
				<em><ct:FWLabel key="AL0015_GENERATION_DROIT"/></em>&nbsp;
				<%=viewBean.getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1() + " " + 
				viewBean.getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2() + " (" + objSession.getCode(viewBean.getDroitComplexModel().getDroitModel().getTypeDroit()) + ")"%>
				</td></tr>
			<% }%>
			<tr>
				
				<td><ct:FWLabel key="AL0015_GENERATION_OPTION_TYPE"/></td>
				<td>
			       	<ct:select tabindex="5" name="entetePrestationModel.bonification" defaultValue="<%=ALCSPrestation.BONI_INDIRECT %>">
	                    <ct:optionsCodesSystems csFamille="ALBONITYPE">
	                    	<%if(!JadeNumericUtil.isEmptyOrZero(viewBean.getDossierComplexModel().getDossierModel().getIdTiersBeneficiaire())){ %>
	                    		<ct:excludeCode code="<%=ALCSPrestation.BONI_INDIRECT %>"/>
	                    	<%} %>
	                    	<%if(JadeNumericUtil.isEmptyOrZero(viewBean.getDossierComplexModel().getDossierModel().getIdTiersBeneficiaire())){ %>
	                    		<ct:excludeCode code="<%=ALCSPrestation.BONI_DIRECT %>"/>
	                    		<ct:excludeCode code="<%=ALCSPrestation.BONI_RESTITUTION %>"/>
	                    	<%} %>
	                    </ct:optionsCodesSystems>   			
	                </ct:select>
	            </td>
	            <td><ct:FWLabel key="AL0015_GENERATION_OPTION_PROCESSUS"/></td>
	            <td>
	            	<select size="5" name="numProcessus" id="numProcessus">
	            		<%
	            	
	            		for(int i=0;i<viewBean.getProcessusSelectableList().size();i++){
	            			ProcessusPeriodiqueModel currentProcessus = (ProcessusPeriodiqueModel)viewBean.getProcessusSelectableList().get(i);
	            		%>
	            			<option <%=viewBean.getNumProcessus().equals(currentProcessus.getIdProcessusPeriodique())?"selected='selected' ":""%> <%=currentProcessus.getIsPartiel()?"":"class='main'"%> value='<%=currentProcessus.getIsPartiel()?currentProcessus.getId():"0"%>'><%=viewBean.getDescriptionProcessusSelectable(i)%></option>
	            		<%
	            			}
	            		%>
	            		<option <%=viewBean.getNumProcessus().equals("0")?"selected='selected' ":""%> value='0'><ct:FWLabel key="AL0017_RECAP_DETACHER_PROCESSUS"/></option>
					</select>
	            </td>
				
			</tr>
			<tr id="genAdvancedOptions0">
				<td colspan="2"><a href="#" class="moreLink"><ct:FWLabel key="AL0015_GENERATION_OPTIONS_MORE"/></a></td>
			</tr>
			<tr id="genAdvancedOptions0Content" <%=ALCSDossier.UNITE_CALCUL_MOIS.equals(viewBean.getDossierComplexModel().getDossierModel().getUniteCalcul())?"style='display:none;'":"style='display:block;'" %>>	
				<td colspan="2"> 
					<table>
						<tr>
							<td><ct:FWLabel key="AL0015_GENERATION_OPTION_MONTANT"/></td>
							<td><ct:inputText tabindex="6" name="entetePrestationModel.montantTotal" styleClass="small"/></td>
						</tr>
						<tr>
							<td><ct:FWLabel key="AL0015_GENERATION_OPTION_NB_UNITE"/>
							<%=ALCSDossier.UNITE_CALCUL_HEURE.equals(viewBean.getDossierComplexModel().getDossierModel().getUniteCalcul())?objSession.getLabel("AL0015_GENERATION_OPTION_UNITE_HEURE"):(ALCSDossier.UNITE_CALCUL_JOUR.equals(viewBean.getDossierComplexModel().getDossierModel().getUniteCalcul())
									?objSession.getLabel("AL0015_GENERATION_OPTION_UNITE_JOUR"):objSession.getLabel("AL0015_GENERATION_OPTION_UNITE_MOIS"))%>

							</td>
							<td>
							<c:choose>
								<c:when test="${viewBean.uniteHeure}">
									<input tabindex="7" name="entetePrestationModel.nombreUnite" class="small" value="0" />
								</c:when>
								<c:otherwise>
									<ct:inputText tabindex="7" name="entetePrestationModel.nombreUnite" styleClass="small" defaultValue='<%=ALCSDossier.UNITE_CALCUL_MOIS.equals(viewBean.getDossierComplexModel().getDossierModel().getUniteCalcul())?"1":"0" %>'/>
								</c:otherwise>
							</c:choose>
							</td>
							<!-- <td><ct:FWLabel key="AL0015_GENERATION_OPTION_TAUX"/></td>
							<td><input tabindex="8" type="text" value="TODO"/></td>-->
						</tr>
					</table>
				</td>
			</tr>
    	</table>
	<%-- /tpl:insert --%>			
</td></tr>											
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%if(!JadeNumericUtil.isEmptyOrZero(viewBean.getEntetePrestationModel().getIdRecap())){ %>
<ct:menuChange displayId="options" menuId="generationDossier-detail" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="selectedId" checkAdd="no" value="<%=viewBean.getEntetePrestationModel().getIdRecap()%>"  />
	<ct:menuSetAllParams key="idDossier" checkAdd="no" value="<%=viewBean.getDossierComplexModel().getId()%>"  />		
</ct:menuChange>
<%}else{ %>
<ct:menuChange displayId="options" menuId="default-detail" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="idDossier" checkAdd="no" value="<%=viewBean.getDossierComplexModel().getId()%>"  />		
</ct:menuChange>
<%} %>

<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
