<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.al.vb.droit.ALDroitViewBean"%>
<%@page import="ch.globaz.pyxis.business.model.PaysSearchSimpleModel" %>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/detail/header.jspf" %>

<%@page import="ch.globaz.pyxis.business.service.AdministrationService"%>
<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%-- tpl:insert attribute="zoneInit" --%>
<%
    ALDroitViewBean viewBean = (ALDroitViewBean) session.getAttribute("viewBean"); 
	selectedIdValue = viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	
	boolean hasNewRight = objSession.hasRight(userActionNew, FWSecureConstants.ADD);
	//userActionValue = "al.droit.droit.modifier";
	idEcran="AL0003";
	bButtonNew = hasNewRight;
	actionNew = actionNew.concat("&idDossier=");	
	try{
		if(!JadeStringUtil.isEmpty(request.getParameter("idDossier")))
			actionNew = actionNew.concat(request.getParameter("idDossier"));
		else
			actionNew = actionNew.concat(viewBean.getDroitComplexModel().getDroitModel().getIdDossier());
	}catch(Exception e){
		System.out.println("droit_de.jsp#Problème pour ajouter le paramètre idDossier dans l'actionNew ");
		
	}

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript">
 
  var LANGUAGES ="<ct:FWLabel key='AL0004_BENEFICAIRE_W_BENEF'/>";
  var CS_PAIEMENT_TIERS = "<%=ALCSDossier.PAIEMENT_TIERS%>"; 
  var CS_PAIEMENT_DIRECT = "<%=ALCSDossier.PAIEMENT_DIRECT%>"; 
  var CS_PAIEMENT_INDIRECT = "<%=ALCSDossier.PAIEMENT_INDIRECT%>";   
  var ID_TIERS_ALLOC = "<%=viewBean.getDossierComplexModel().getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire()%>";

</script>
<%-- tpl:insert attribute="zoneScripts" --%>

<%@page import="ch.globaz.al.business.constantes.*"%>
<%@page import="globaz.globall.util.JAUtil"%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@page import="ch.globaz.pyxis.business.model.PaysSimpleModel"%>
<%@page import="globaz.pyxis.db.alternate.TIPersonneAvsAdresseViewBean"%>
<%@page import="globaz.jade.client.util.JadeNumericUtil"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/util_webaf.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/ajax_webaf.js"></script>
<script type="text/javascript">

//variables nécessaires à la recherche ajax et variant selon la page...
searchAjaxInputId = "searchNssCriteria";
autoSuggestContainerId = "autoSuggestContainer";
prefixModel = "droitComplexModel.enfantComplexModel";

function add() {
    document.forms[0].elements('userAction').value="al.droit.droit.ajouter";
}
function upd() {
	for (key in document.getElementsByTagName('a'))
  	{
		if(document.getElementsByTagName('a')[key].className=='removeLink')
			document.getElementsByTagName('a')[key].style.display='inline';
  	}
	
    document.forms[0].elements('userAction').value="al.droit.droit.modifier";

  	//Curseur par défaut sur le champ NSS ou titre si nss non éditable
	if(document.getElementById('partialforNumAvs')!=null)
		document.getElementById('partialforNumAvs').focus();
	else if(document.getElementById(prefixModel+'.personneEtendueComplexModel.tiers.designation1')){
		document.getElementById(prefixModel+'.personneEtendueComplexModel.tiers.designation1').focus();
	}
    
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.droit.droit.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.droit.droit.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('selectedId').value='<%=JavascriptEncoder.getInstance().encode(viewBean.getDroitComplexModel().getDroitModel().getIdDossier()) %>';
		document.forms[0].elements('userAction').value="al.dossier.dossierMain.afficher";
	} else {
        document.forms[0].elements('userAction').value="al.droit.droit.afficher";
	}
}

function del() {
	var msgDelete = '<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="al.droit.droit.supprimer";
        document.forms[0].submit();
    }
}

function init(){
	initDebugManager();
	
	$('#modePaiementDroit').change(function(){	
		
		if(this.value != CS_PAIEMENT_TIERS){	
			$('#idTiersBeneficiaire').val('0');
		}
		
		//si CS_PAIEMENT_TIERS, on ne fait rien car rempli par widget
	
	});
	
	
	if(document.forms[0].elements('_method').value!='add'){
		$("#AL0003tiersZone input").each(function(index) {
			$(this).attr("disabled", true);
			$(this).attr("readonly", true);
			});
	
		$("#AL0003tiersZone select").each(function(index) {
			$(this).attr("disabled", true);
			$(this).attr("readonly", true);
			});
	}

	//test sur le paramètre selectedIndex de la requête, si défini, alors ca signifie qu'on revient de la sélection du tiers
	isFromSelectionTiers = '<%=JavascriptEncoder.getInstance().encode(request.getParameter("selectedIndex")!=null?request.getParameter("selectedIndex"):"")%>';
	initNssInputComponent('<%=JavascriptEncoder.getInstance().encode(idEcran)%>','forNumAvs');

	//affichage dépend du type de droit
	document.getElementById('droitComplexModel.droitModel.typeDroit').onchange = function(){
		accordDisplayingZoneForm();
	};

	if(isFromSelectionTiers!=''){	
		synchroTiersAjax();
	}
	//pas besoin d'afficher le bouton, on appelle le onclick par code quand on est
	//met mode de paiement => tiers beneficiaire
	if(document.forms[0].elements('benefSelector')!= null)
		document.forms[0].elements('benefSelector').style.display="none";
}

function postInit() {
	// Message d'avertissement
	var idMsgWarning = '<%=request.getParameter("idMessageDroitPC")%>';
	if (idMsgWarning != null && idMsgWarning != 'null') {
		window.alert('<%=viewBean.getSession().getLabel(request.getParameter("idMessageDroitPC")).replace("\'", "\\\'") %>');
	}
}

//définir cette méthode si traitement après remplissage ajax du formulaire
function callbackFillInputAjax(){
	mlog('callbackfillInputAjax');

	var enfantSync = false;
	var tiersSync = false;
	var idEnfant = '';
	if(response.search.results.length==1){
	
		for(var i=0;i<response.search.results[0].properties.length;i++){
			if(response.search.results[0].properties[i].name==prefixModel+'.enfantModel.idEnfant'){
				idEnfant = response.search.results[0].properties[i].value;
				enfantSync=true;
				tiersSync=true;
				break;
			}
			
		}
	}

	if(!tiersSync && document.getElementById(prefixModel+'.personneEtendueComplexModel.tiers.idTiers').value!='')
		tiersSync=true;
	mlog('tiersSync fill form');		
	accordStatutLogoWithResponse(tiersSync);

}

function accordDisplayingZoneForm(){
		var capableExercer = $("input[name='capableExercer']").is(':checked');
	  if(window.event.srcElement.name=='droitComplexModel.droitModel.typeDroit'){
	
	      switch(window.event.srcElement.value){
	        case '<%=JavascriptEncoder.getInstance().encode(ALCSDroit.TYPE_ENF)%>'  : 
	        			if(capableExercer){
	        				$(".attestationZone").hide();	
	        			}else{
	        				$(".attestationZone").show();	
	        			}
	        			if(document.getElementById('AL0003naissanceZone'))
    						document.getElementById('AL0003naissanceZone').style.display='block'; 
						if(document.getElementById('AL0003enfantZone'))
                     		document.getElementById('AL0003enfantZone').style.display='block';
						if(document.getElementById('AL0003tiersZone')) 
	                      	document.getElementById('AL0003tiersZone').style.display='block'; 
  
	                      break;
	        case '<%=JavascriptEncoder.getInstance().encode(ALCSDroit.TYPE_FORM)%>' : 
	        			$(".attestationZone").show();
	        			if(document.getElementById('AL0003naissanceZone'))
    						document.getElementById('AL0003naissanceZone').style.display='none';
		        		if(document.getElementById('AL0003enfantZone'))
	                      	document.getElementById('AL0003enfantZone').style.display='block'; 
	                    if(document.getElementById('AL0003tiersZone'))
	                      	document.getElementById('AL0003tiersZone').style.display='block'; 
	                   break;
	        case '<%=JavascriptEncoder.getInstance().encode(ALCSDroit.TYPE_MEN)%>'  : 
	        	 	 	$(".attestationZone").hide();		
	        			if(document.getElementById('AL0003naissanceZone'))
    						document.getElementById('AL0003naissanceZone').style.display='none';
	        			if(document.getElementById('AL0003enfantZone'))
	                      	document.getElementById('AL0003enfantZone').style.display='none';
	                    if(document.getElementById('AL0003tiersZone'))
	                      	document.getElementById('AL0003tiersZone').style.display='none';
	                    resetZonePartForm('AL0003enfantZone'); 
	                    resetZonePartForm('AL0003naissanceZone'); 
	                    resetZonePartForm('AL0003tiersZone'); 
	                    break;
	                    
	      }
	  
	  }
	
}

//gère la valeur de la propriété idTiersBeneficiaire selon le choix fait
function updatePaiementType(){
	
	var typeValue = window.event.srcElement.value;
	var typeDirect = <%=JavascriptEncoder.getInstance().encode(ALCSDossier.PAIEMENT_DIRECT) %>;
	var typeIndirect = <%=JavascriptEncoder.getInstance().encode(ALCSDossier.PAIEMENT_INDIRECT)%>;
	var typeTiersBenef = <%=JavascriptEncoder.getInstance().encode(ALCSDossier.PAIEMENT_TIERS)%>;

	if(typeValue == typeDirect)
		document.getElementsByName("droitComplexModel.droitModel.idTiersBeneficiaire")[0].value =
			document.getElementsByName("dossierComplexModel.allocataireComplexModel.allocataireModel.idTiersAllocataire")[0].value;
	if(typeValue == typeIndirect)
		document.getElementsByName("droitComplexModel.droitModel.idTiersBeneficiaire")[0].value = "0";
	if(typeValue == typeTiersBenef){
	
		document.forms[0].elements('benefSelector').onclick();
	}

}

function updateInputsMontantForce(input) {
	
	if(input == document.forms[0].elements['montantDroit'][0]) {
		
		document.getElementById('droitComplexModel.droitModel.montantForce').value='';
		document.getElementById("droitComplexModel.droitModel.force").value = "false";
		
	} else if(input == document.forms[0].elements['montantDroit'][1]) {
		
		document.getElementById("droitComplexModel.droitModel.force").value = "true";
		
		if(document.getElementById('droitComplexModel.droitModel.montantForce').value == '') {
			document.getElementById('droitComplexModel.droitModel.montantForce').value = "0.00";
		}
	
	} else if(input == document.forms[0].elements['droitComplexModel.droitModel.montantForce']) {
		
		if((event.keyCode >= 48 && event.keyCode <= 57) || (event.keyCode >= 96 && event.keyCode <= 105) || event.keyCode == 110 || event.keyCode == 190 || event.keyCode == 46) {
		
			document.getElementById("droitComplexModel.droitModel.force").value = "true";
			document.forms[0].elements['montantDroit'][1].checked='true';
		}
	}
}


$(document).ready(function(){
	//Gestion de l'affichage zone attestation
	var typeDroitForm = <%=JavascriptEncoder.getInstance().encode(ALCSDroit.TYPE_FORM) %>;
	var capableExercer = $("input[name='capableExercer']").is(':checked');
	var valueTypeDroit = null;
	
	if($("select[name='droitComplexModel.droitModel.typeDroit']").length>0){
		valueTypeDroit = $("select[name='droitComplexModel.droitModel.typeDroit']").val();
	}
	else if($("input[name='droitComplexModel.droitModel.typeDroit']").length>0){
		valueTypeDroit = $("input[name='droitComplexModel.droitModel.typeDroit']").val();
	}
	
	if(valueTypeDroit!=typeDroitForm && capableExercer){
		$(".attestationZone").hide();
	}
	
	//gestion concordance echéance calculée attestation
	$("#finDroitForcee").change(function() {
	    document.getElementById('droitComplexModel.droitModel.imprimerEcheance').value = 'true';
		document.getElementsByName('imprimerEcheance')[0].checked = 'true';
 		var newValue = $(this).val();
	    var attestationDateValue = $("#dateAttestationEtude").val();
	    if(newValue.length>0 && attestationDateValue.length>0){
	    	//si la date d'échéance forcée > attestation => attestation = échéance forcée
	    	if(compareGlobazDates(attestationDateValue,newValue)==1){
	    	
	    		$("#dateAttestationEtude").val(newValue);
	    	}	
	    }
	   
	});
	
	var jsonWarn = '<%=viewBean.getAddWarnings()%>';	
	if(jsonWarn.length > 0) {
	
		var initializationPopup = jQuery.parseJSON(jsonWarn);
		var servletContxt = '<%=servletContext%>';
		//ajout du servletContext devant l'url configuré
		initializationPopup.options.url = servletContxt.concat(initializationPopup.options.url);
		$('#popup-container').data('al-popup',initializationPopup);
		$('#popup-container').trigger('newWarnMsg');
		
	}

});

</script>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%>
			<%=(viewBean.getDroitComplexModel().getDroitModel().isNew())?objSession.getLabel("AL0003_TITRE_NEW"):objSession.getLabel("AL0003_TITRE")+viewBean.getDroitComplexModel().getId()+"&nbsp;"+objSession.getCodeLibelle(viewBean.getDroitComplexModel().getDroitModel().getTypeDroit())%>		
			(<ct:FWLabel key="AL0004_TITRE"/><%=viewBean.getDossierComplexModel().getId() %> - <%=viewBean.getDossierComplexModel().getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1() %>
			<%=viewBean.getDossierComplexModel().getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2()%>)
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
			<tr><td>
			<%-- tpl:insert attribute="zoneMain" --%>
 					<div id="popup-container" data-al-popup=''></div>
			       <table class="zone" id="AL0003typeZone">
			       <tr> 
			       		<td class="label"><ct:FWLabel key="AL0003_DROIT_TYPE"/></td> 
			       		<td>
			       		<%if(viewBean.getDroitComplexModel().getDroitModel().isNew()){%>
			       		<ct:select tabindex="1" name="droitComplexModel.droitModel.typeDroit" defaultValue="<%=ALCSDroit.TYPE_ENF%>" >
	                    	<ct:optionsCodesSystems csFamille="ALPRETYPE">
	                    		<ct:excludeCode code="<%=ALCSDroit.TYPE_ACCE%>"/>
	                    		<ct:excludeCode code="<%=ALCSDroit.TYPE_ENF_LJA%>"/>
	                    		<ct:excludeCode code="<%=ALCSDroit.TYPE_FNB%>"/>
	                    		<ct:excludeCode code="<%=ALCSDroit.TYPE_MEN_LFA%>"/>
	                    		<ct:excludeCode code="<%=ALCSDroit.TYPE_MEN_LJA%>"/>
	                    		<ct:excludeCode code="<%=ALCSDroit.TYPE_NAIS%>"/>	
	                    	</ct:optionsCodesSystems>         			
	                    </ct:select>
	                    <%} else{%>
	                    
			       		<input type="text" name="typeDroit" value="<%=objSession.getCodeLibelle(viewBean.getDroitComplexModel().getDroitModel().getTypeDroit())%>" class="readonly normal" disabled="disabled"/>
			       		<ct:inputHidden name="droitComplexModel.droitModel.typeDroit"/>
			       		<%}%>
	                    </td>
	               </tr>  
			       		
          		   </table>		
                   <hr/>
			      
			       <%if(!ALCSDroit.TYPE_MEN.equals(viewBean.getDroitComplexModel().getDroitModel().getTypeDroit())){%>
			       <table class="zone" id="AL0003tiersZone">                
		                <tr>
		               		<td class="subtitle" colspan="4">
		                  		
		                  		<%if(viewBean.getDroitComplexModel().getDroitModel().isNew()){ %>
                					<ct:FWLabel key="AL0003_TITRE_TIERS"/>
                					<div id="idTiers"></div>
                					<div><a class="syncLink" href="#" onclick="synchroTiersAjax();" title="<%=objSession.getLabel("LINK_SYNC_TIERS_DESC")%>">&nbsp;</a></div>
                					<div id="statutSynchroTiers"></div>
                				<%}else{ %>
                					<ct:FWLabel key="AL0003_TITRE_TIERS"/>
                					<div id="idTiers"> (<a href="<%=servletContext+"/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId="+viewBean.getDroitComplexModel().getEnfantComplexModel().getEnfantModel().getIdTiersEnfant()%>"><%=viewBean.getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getId()%></a>)</div>
                					<div id="statutSynchroTiers"><img src="images/dialog-ok-apply.png" alt="Synchronisation réussie" width="16" height="16"/></div>
                					
                				<%}%>
		                  		
                			</td>
                		</tr>		               
		                <tr>               
		              
		                 
		                    <!--  Champs nécessaire pour écriture dans les tiers (création / update) -->
		                    <ct:inputHidden name="droitComplexModel.enfantComplexModel.personneEtendueComplexModel.tiers.idTiers"/>
		                    <ct:inputHidden name="droitComplexModel.enfantComplexModel.personneEtendueComplexModel.personne.idTiers"/>
		                    <ct:inputHidden name="droitComplexModel.enfantComplexModel.personneEtendueComplexModel.personneEtendue.idTiers"/>
		                    <ct:inputHidden name="droitComplexModel.enfantComplexModel.personneEtendueComplexModel.tiers.spy"/>
		                    <ct:inputHidden name="droitComplexModel.enfantComplexModel.personneEtendueComplexModel.personne.spy"/>
		                    <ct:inputHidden name="droitComplexModel.enfantComplexModel.personneEtendueComplexModel.personneEtendue.spy"/>
		                    <ct:inputHidden name="droitComplexModel.enfantComplexModel.personneEtendueComplexModel.tiers.new"/>
		                    <ct:inputHidden name="droitComplexModel.enfantComplexModel.personneEtendueComplexModel.personne.new"/>
		                    <ct:inputHidden name="droitComplexModel.enfantComplexModel.personneEtendueComplexModel.personneEtendue.new"/>
		                    <!--  Champs nécessaire pour écriture dans les enfants AF (ctrl isNew, création / update) -->
		                    <ct:inputHidden name="droitComplexModel.enfantComplexModel.enfantModel.idEnfant"/>
		                    <ct:inputHidden name="droitComplexModel.enfantComplexModel.enfantModel.spy"/>
		                    <ct:inputHidden name="droitComplexModel.enfantComplexModel.enfantModel.new"/>
		                    <!-- nécessaire pour updater enfant existant récupéré -->
		                    <ct:inputHidden name="droitComplexModel.enfantComplexModel.enfantModel.idTiersEnfant"/>
		                    
		                    <td class="label"><ct:FWLabel key="AL0003_TIERS_NSS"/></td>
		                    <td>                
		                    <%
		                    Object[] tiersMethodsName = new Object[]{
									/*
								
									
									new String[]{"droitComplexModel.enfantComplexModel.personneEtendueComplexModel.personne.idTiers","idTiers"},
									new String[]{"droitComplexModel.enfantComplexModel.personneEtendueComplexModel.personneEtendue.idTiers","idTiers"},
									*/
									new String[]{"droitComplexModel.enfantComplexModel.personneEtendueComplexModel.tiers.idTiers","idTiers"},
									new String[]{"droitComplexModel.enfantComplexModel.personneEtendueComplexModel.personneEtendue.numAvsActuel","getNumAvsActuel"},
									/*
									new String[]{"droitComplexModel.enfantComplexModel.personneEtendueComplexModel.personne.sexe","getSexe"},	
									new String[]{"droitComplexModel.enfantComplexModel.personneEtendueComplexModel.tiers.designation1","getDesignation1_tiers"},
									new String[]{"droitComplexModel.enfantComplexModel.personneEtendueComplexModel.tiers.designation2","getDesignation2_tiers"},
									new String[]{"droitComplexModel.enfantComplexModel.personneEtendueComplexModel.personne.dateNaissance","getDateNaissance"},
									new String[]{"droitComplexModel.enfantComplexModel.personneEtendueComplexModel.tiers.idPays","getIdPaysTiers"},
									new String[]{"droitComplexModel.enfantComplexModel.personneEtendueComplexModel.tiers.spy","spy"},
									new String[]{"droitComplexModel.enfantComplexModel.personneEtendueComplexModel.personne.spy","spy"},
									new String[]{"droitComplexModel.enfantComplexModel.personneEtendueComplexModel.personneEtendue.spy","spy"}
									*/
				
									
							};
		                    
		                    if(viewBean.getDroitComplexModel().getDroitModel().isNew()){ %>
                    			<nss:nssPopup tabindex="2" avsMinNbrDigit="2" nssMinNbrDigit="2" name="forNumAvs" newnss="true" />
                    			<ct:inputHidden name="droitComplexModel.enfantComplexModel.personneEtendueComplexModel.personneEtendue.numAvsActuel"/>
                    			<ct:FWSelectorTag name="tiersSelector"
								methods="<%=tiersMethodsName%>"
								providerApplication="pyxis" 
								providerPrefix="TI"
								providerAction="pyxis.tiers.tiers.chercher"
							/>
                    	
                    		<%}else{ %>
                    			<ct:inputText name="droitComplexModel.enfantComplexModel.personneEtendueComplexModel.personneEtendue.numAvsActuel" 
                    			styleClass="nss readOnly" readonly="readonly" />
                    		<%}%>
		                 	<div id="autoSuggestContainer" class="suggestList"></div>
		                 	
		                    </td>
		                    <td class="label"><ct:FWLabel key="AL0003_TIERS_SEXE"/></td>
		                    <td>
		                    	<ct:select tabindex="5" name="droitComplexModel.enfantComplexModel.personneEtendueComplexModel.personne.sexe"  wantBlank="true">
	                    			<ct:optionsCodesSystems csFamille="PYSEXE"></ct:optionsCodesSystems>	
	                    		</ct:select>    
		                    </td>
		                </tr>
		                <tr>
		                    <td class="label"><ct:FWLabel key="AL0003_TIERS_NOM"/></td>
		                    <td>
		                    	<ct:inputText tabindex="3" name="droitComplexModel.enfantComplexModel.personneEtendueComplexModel.tiers.designation1" styleClass="normal"/>
		                    </td>
		                    <td class="label"><ct:FWLabel key="AL0003_TIERS_NAISSANCE"/></td>
		                    <td>
		                    	<%
		                    	
		                    	String dateNaissance = "";
		                    		try{
		                    			dateNaissance = viewBean.getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne().getDateNaissance();
		                    		}catch(Exception e){
		                    			e.printStackTrace();
		                    		}
		                    	%>
		                    	<ct:FWCalendarTag name="dateNaissance" tabindex="6" 
									value="<%=dateNaissance%>" 
									doClientValidation="CALENDAR"/>
								<ct:inputHidden name="droitComplexModel.enfantComplexModel.personneEtendueComplexModel.personne.dateNaissance" id="dateNaissanceValue"/>
								<script language="JavaScript">
									document.getElementsByName('dateNaissance')[0].onblur=function(){
										fieldFormat(document.getElementsByName('dateNaissance')[0],'CALENDAR');document.getElementById('dateNaissanceValue').value = this.value;
										
										};
									
									function theTmpReturnFunctiondateNaissance(y,m,d) { 
										if (window.CalendarPopup_targetInput!=null) {
											var d = new Date(y,m-1,d,0,0,0);
											window.CalendarPopup_targetInput.value = formatDate(d,window.CalendarPopup_dateFormat);
											document.getElementById('dateNaissanceValue').value = document.getElementsByName('dateNaissance')[0].value;
											
										}else {
											alert('Use setReturnFunction() to define which function will get the clicked results!'); 
										}
									}
									cal_dateNaissance.setReturnFunction('theTmpReturnFunctiondateNaissance');
							</script>
							</td>
		                </tr>
		                <tr>
		                    <td class="label"><ct:FWLabel key="AL0003_TIERS_PRENOM"/></td>
		                    <td><ct:inputText tabindex="4" name="droitComplexModel.enfantComplexModel.personneEtendueComplexModel.tiers.designation2" styleClass="normal"/></td>
		                    <td class="label"><ct:FWLabel key="AL0003_TIERS_NATIONALITE"/></td>
		                    <td>
		                    	<%
		                			PaysSearchSimpleModel paysSearchModel = (PaysSearchSimpleModel) request.getAttribute("list_pays");    
					            %>
								<ct:select tabindex="7" name="droitComplexModel.enfantComplexModel.personneEtendueComplexModel.tiers.idPays" defaultValue="<%=viewBean.getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getIdPays()%>">
								  	<%for(int i=0; i<paysSearchModel.getSize(); i++){ 
								  		PaysSimpleModel pays = (PaysSimpleModel) paysSearchModel.getSearchResults()[i];%>
								  		<ct:option value="<%=pays.getIdPays()%>" label="<%=pays.getLibelleFr()%>"></ct:option>
								  	<%} %>
					  			</ct:select>	
		                    </td>              
		         		</tr>
		         		     
	         	</table>
 		
	         	<table class="zone" id="AL0003enfantZone">
	                <tr>
	                	<td class="subtitle" colspan="4">
	                	<ct:FWLabel key="AL0003_TITRE_ENFANT"/>
	                	<div id="statutWarningEnfant">
	                	<%= (viewBean.getEnfantAlreadyActif()>1)?"<img src='images/dialog-warning.png' alt='"+objSession.getLabel("AL0003_ENFANT_WARNING_ACTIF")+"' width='16' height='16'/>":""%>
	                	</div>
	                	</td>
	                </tr>
	                <tr>
	                    <td class="label"><ct:FWLabel key="AL0003_ENFANT_PAYS"/></td>
	                    <td>       	
		                    <ct:select tabindex="8" name="droitComplexModel.enfantComplexModel.enfantModel.idPaysResidence" defaultValue="<%=viewBean.getDroitComplexModel().getEnfantComplexModel().getEnfantModel().getIdPaysResidence()%>">
								 <%for(int i=0; i<paysSearchModel.getSize(); i++){ 
								  	PaysSimpleModel pays = (PaysSimpleModel) paysSearchModel.getSearchResults()[i];%>
								  	<ct:option value="<%=pays.getIdPays()%>" label="<%=pays.getLibelleFr()%>"></ct:option>
								  <%} %>
					  		</ct:select>	
		                  </td>
	                    <td class="label"><ct:FWLabel key="AL0003_ENFANT_STATUT"/></td>
	                    <td>
	                    	<ct:select tabindex="10" name="droitComplexModel.droitModel.statutFamilial">
	                    		<ct:optionsCodesSystems csFamille="ALRAFAMSTF"></ct:optionsCodesSystems>	
	                    	</ct:select>                                   
						</td>
	                </tr>
	                <tr>
	                    <td class="label"><ct:FWLabel key="AL0003_ENFANT_CANTON"/></td>
	                    <td>
	                       <ct:select tabindex="9" name="droitComplexModel.enfantComplexModel.enfantModel.cantonResidence" wantBlank="true">
	                    		<ct:optionsCodesSystems csFamille="ALCANTON"></ct:optionsCodesSystems>	
	                    	</ct:select>    
	                    </td>
	                    <td class="label"></td>
	                    <td>
	                    <ct:inputHidden name="droitComplexModel.enfantComplexModel.enfantModel.capableExercer"/>
	                    <% if(viewBean.getDroitComplexModel().getEnfantComplexModel().getEnfantModel().getCapableExercer().booleanValue()){ %> 
	                    	<input type="checkbox" tabindex="11" checked="checked" name="capableExercer"
	                    		onclick="reportCheckboxInModel('droitComplexModel.enfantComplexModel.enfantModel');"/>
	                    <%}else{%>
	                    	<input type="checkbox" name="capableExercer" tabindex="11"          		
	                    		   onclick="reportCheckboxInModel('droitComplexModel.enfantComplexModel.enfantModel');"/>
	                    <%}%>

	                 		<ct:FWLabel key="AL0003_ENFANT_CAPABLE"/>
	                    </td>
	                </tr>
	          </table>
	          <%}%>
	          <table class="tab3Col zone" id="AL0003droitZone">
	                <tr>
	                	<td class="subtitle" colspan="6"><ct:FWLabel key="AL0003_DROIT_TITRE"/></td>
	                </tr>
	                <tr>
	                    <td class="labelSmall"><ct:FWLabel key="AL0003_DROIT_DEBUT"/></td>
	                    <td>
							<%if(!viewBean.getDroitComplexModel().getDroitModel().isNew()){ %>
							
							<input id=debutDroit name="droitComplexModel.droitModel.debutDroit" tabindex="12" 
							  value="<%=viewBean.getDroitComplexModel().getDroitModel().getDebutDroit()%>" type="text" data-g-calendar=" ">
							<%}else{ %>
								<ct:inputText readonly="readonly" disabled="disabled" name="droitComplexModel.droitModel.debutDroit" styleClass="readonly date" />
							<%} %>
	                    </td>
	                    <td class="label"><ct:FWLabel key="AL0003_DROIT_ECHEANCE"/></td>
	                    <td class="dateCol">
							
							<input id=finDroitForcee name="droitComplexModel.droitModel.finDroitForcee" tabindex="14" 
							  value="<%=viewBean.getDroitComplexModel().getDroitModel().getFinDroitForcee()%>" type="text" data-g-calendar=" ">
	
	                    </td>
	                    <td class="label"><ct:FWLabel key="AL0003_DROIT_MOTIFFIN"/></td>
	                    <td>
	                        <ct:select tabindex="16" name="droitComplexModel.droitModel.motifFin" defaultValue="<%=ALCSDroit.MOTIF_FIN_ECH%>">
	                    		<ct:optionsCodesSystems csFamille="ALDROMOFIN"></ct:optionsCodesSystems>	
	                    	</ct:select>  
	                    </td>       
	                </tr>
	     
	                <tr>    	
	                    <td class="label"><span class="attestationZone"><ct:FWLabel key="AL0003_DROIT_DATE_ATTESTATION"/></span></td>
	                    <td><span class="attestationZone"><input id="dateAttestationEtude"  name="droitComplexModel.droitModel.dateAttestationEtude" tabindex="17" 
							  value="<%=viewBean.getDroitComplexModel().getDroitModel().getDateAttestationEtude()%>" type="text" data-g-calendar=" "></span></td>
	                   
	                    <td class="label"><ct:FWLabel key="AL0003_DROIT_ECHEANCE_CAL"/></td>
	                    <td class="dateCol">
							<ct:inputText name="echeanceCalculee" styleClass="date readonly" disabled="disabled" readonly="readonly"/>
						</td>
	                    <td class="label"></td>
	                    <td>  		
	                 		<ct:inputHidden name="droitComplexModel.droitModel.imprimerEcheance"/>
	                 		<% if(viewBean.getDroitComplexModel().getDroitModel().getImprimerEcheance().booleanValue()){ %> 
	                    	<input type="checkbox" checked="checked" name="imprimerEcheance" tabindex="18"
	                    		onclick="reportCheckboxInModel('droitComplexModel.droitModel');"   		
	                    	/>
	                    	
	                    	<%}else{%>
	                    	<input type="checkbox" tabindex="18" name="imprimerEcheance" onclick="reportCheckboxInModel('droitComplexModel.droitModel');"/>
	                    	<%}%>
	                 		<ct:FWLabel key="AL0003_DROIT_ECHEANCE_ANN"/>             		
	                 	</td>  
	                </tr>
	                <tr>
	                    <td class="labelSmall"></td>
	                    <td></td>
	                    <td class="label"></td>
	                    <td class="dateCol">
	                    </td>
	                    <td class="label">
	                    <% if(viewBean.isFnbActive()){ %> 
	                    	<ct:inputHidden name="droitComplexModel.droitModel.supplementFnb"/>
			                    <% if(viewBean.getDroitComplexModel().getDroitModel().getSupplementFnb()){ %> 
			                    	<input type="checkbox" checked="checked" name="supplementFnb"
			                    		onclick="reportCheckboxInModel('droitComplexModel.droitModel');"/>
			                    <%}else{%>
			                    	<input type="checkbox" name="supplementFnb"          		
			                    		   onclick="reportCheckboxInModel('droitComplexModel.droitModel');"/>
			                    <%}%>
	
	                  		<ct:FWLabel key="AL0003_ANNONCER_FNB"/></td>
	                  	 <%}%>
	                    
	                    </td>
	                    <td >	               
	                    	<ct:inputText name="montantCalcule" styleClass="montant readonly" readonly="readonly" defaultValue='<%=request.getParameter("montantCalcule")%>'/>
	                    	<input name="droitComplexModel.droitModel.force" type="hidden" value="<%if(viewBean.getDroitComplexModel().getDroitModel().getForce()!= null && viewBean.getDroitComplexModel().getDroitModel().getForce()){ %>true<% } else { %>false<% } %>">
	                    	<input tabindex="19" class="radio" type="radio" name="montantDroit"  checked="checked" 
	                    		onclick="updateInputsMontantForce(this)"/>
	                    	<ct:FWLabel key="AL0003_DROIT_MONTANT_CAL"/>
	                    </td>           
	                </tr>
	            
	                <tr>
	                    <td class="labelSmall"><ct:FWLabel key="AL0003_DROIT_ETAT"/></td>
	                    <td>
	                    	<ct:select tabindex="13" name="droitComplexModel.droitModel.etatDroit" defaultValue="<%=ALCSDroit.ETAT_A%>">
	                    		<ct:optionsCodesSystems csFamille="ALDROETATS"></ct:optionsCodesSystems>	
	                    	</ct:select>  

	                    </td>
	                    <td class="label">&nbsp;</td>
	                    <td class="dateCol">&nbsp;</td>
	                    <td class="label">
	                   	<% if(viewBean.isCaisseHorlogere()){ %>
	                    	<ct:inputHidden name="droitComplexModel.droitModel.supplementActif"/>
			                    <% if(viewBean.getDroitComplexModel().getDroitModel().getSupplementActif()){ %> 
			                    	<input type="checkbox" checked="checked" name="supplementActif"
			                    		onclick="reportCheckboxInModel('droitComplexModel.droitModel');"/>
			                    <%}else{%>
			                    	<input type="checkbox" name="supplementActif"          		
			                    		   onclick="reportCheckboxInModel('droitComplexModel.droitModel');"/>
			                    <%}%>
	
	                  		<ct:FWLabel key="AL0003_GENERER_SUPPLEMENT"/></td>
	                  	 <%}%>
	                    <td>              
		                    <ct:inputText 	name="droitComplexModel.droitModel.montantForce" styleClass="montant" 
		                    				onkeyup="updateInputsMontantForce(this);"
		                    				tabindex="20"/>
		                    <%if(viewBean.getDroitComplexModel().getDroitModel().getForce()!=null && viewBean.getDroitComplexModel().getDroitModel().getForce().booleanValue()){ %>
	                    		<input id="radioForce" name="montantDroit" class="radio" type="radio" onclick="updateInputsMontantForce(this)" checked="checked" />
	                    	<%}else{ %>
	                    		<input id="radioForce" name="montantDroit" class="radio" type="radio" onclick="updateInputsMontantForce(this)" />
	                    	<%} %>
	                    	<ct:FWLabel key="AL0003_DROIT_MONTANT_FORCE"/>               
	                    </td>    
	                </tr>	               
	                <tr><td colspan="6"></td></tr>
	          </table>
	                 
	          <table class="zone" id="AL0003naissanceZone" <%=ALCSDroit.TYPE_ENF.equals(viewBean.getDroitComplexModel().getDroitModel().getTypeDroit())?"style='display:block'":"style='display:none'" %>> 
	                <tr>
	                	<td class="subtitle" colspan="4"><ct:FWLabel key="AL0003_TITRE_NAISSANCE"/></td>
	                </tr> 
	               
	                <tr>
	                    <td class="label"><ct:FWLabel key="AL0003_NAISSANCE_ALLOC"/></td>
	                    <td>
	                    	<ct:select tabindex="21" name="droitComplexModel.enfantComplexModel.enfantModel.typeAllocationNaissance" defaultValue="<%=viewBean.getDroitComplexModel().getEnfantComplexModel().getEnfantModel().getTypeAllocationNaissance()%>">
	                    		<ct:optionsCodesSystems csFamille="ALDRONAITY"></ct:optionsCodesSystems>	
	                    	</ct:select> 
	                    </td>
	                    <td class="label"><ct:FWLabel key="AL0003_NAISSANCE_MONTANT_FORCE"/></td>
	                    <td>
		                    <ct:inputText tabindex="22" name="droitComplexModel.enfantComplexModel.enfantModel.montantAllocationNaissanceFixe" styleClass="montant" defaultValue="<%=viewBean.getDroitComplexModel().getEnfantComplexModel().getEnfantModel().getMontantAllocationNaissanceFixe() %>"/>
	                    </td>
	                    <td colspan="2">
	                    
	                    	 <ct:inputHidden name="droitComplexModel.enfantComplexModel.enfantModel.allocationNaissanceVersee"/>
			                    <% if(viewBean.getDroitComplexModel().getEnfantComplexModel().getEnfantModel().getAllocationNaissanceVersee().booleanValue()){ %> 
			                    	<input type="checkbox" tabindex="23" checked="checked" name="allocationNaissanceVersee"
			                    		onclick="reportCheckboxInModel('droitComplexModel.enfantComplexModel.enfantModel');"/>
			                    <%}else{%>
			                    	<input type="checkbox" name="allocationNaissanceVersee" tabindex="23"          		
			                    		   onclick="reportCheckboxInModel('droitComplexModel.enfantComplexModel.enfantModel');"/>
			                    <%}%>
	
	                  		<ct:FWLabel key="AL0003_NAISSANCE_VERSEE"/>
	                		
	                	</td> 
	                </tr>
	          </table>
	  		
	          <table class="zone" id="AL0003complZone">
	                
	                <tr>
	                    <td class="label"><ct:FWLabel key="AL0003_DROIT_TAUX"/></td>
	                    <td>
		                    <ct:inputText tabindex="24" name="droitComplexModel.droitModel.tauxVersement" styleClass="small" defaultValue="100.00"/>%
	                    </td>
	                    <td class="label"></td>
	                    <td></td>
	                    
	                </tr>
	                <tr>
	                    <td class="label"><ct:FWLabel key="AL0003_DROIT_TARIF_FORCE"/></td>
	                    <td>
	                        <ct:select tabindex="25" name="droitComplexModel.droitModel.tarifForce" wantBlank="true">
	                    		<ct:optionsCodesSystems csFamille="ALTARCAT"></ct:optionsCodesSystems>	
	                    	</ct:select> 
	                    </td>
	                    <td class="label"><ct:FWLabel key="AL0003_DROIT_MOTIF_REDUC"/></td>
	                    <td>
	                    	<ct:select tabindex="26" name="droitComplexModel.droitModel.motifReduction" defaultValue="<%=ALCSDroit.MOTIF_REDUC_COMP %>">
	                    		<ct:optionsCodesSystems csFamille="ALDROMORED"></ct:optionsCodesSystems>	
	                    	</ct:select> 
	                    </td>
	                     
	                </tr>
	                <tr>
	                    <td class="label"><ct:FWLabel key="AL0003_DROIT_VERSEMENT"/></td>
	                    <td class="mediumSelect">
	                		<ct:inputHidden name="droitComplexModel.droitModel.idTiersBeneficiaire" id="idTiersBeneficiaire"/>
	                			<div data-g-commutator="master:$('#modePaiementDroit'),
			                        condition:($('#modePaiementDroit').val()==CS_PAIEMENT_TIERS),
			                        actionTrue:¦show('#selectionBeneficiaire')¦,
			                        actionFalse:¦hide('#selectionBeneficiaire')¦"/>
	                		
	                		<%-- Utilisé uniquement dans la méthode js updatePaiementType --%>
	                		<ct:inputHidden name="dossierComplexModel.allocataireComplexModel.allocataireModel.idTiersAllocataire"/>
	                		 
	                		 
	                		<select tabindex="13" id="modePaiementDroit" name="modePaiementDroit">
	                		<%if (ALCSDossier.PAIEMENT_INDIRECT.equals(viewBean.getPaiementMode())){ %>
                				<option value="<%=ALCSDossier.PAIEMENT_INDIRECT %>" <%=viewBean.getPaiementMode().equals(ALCSDossier.PAIEMENT_INDIRECT)?"selected='selected'":"" %>><%=objSession.getCodeLibelle(ALCSDossier.PAIEMENT_INDIRECT) %></option>
                			<%}%>	
                			<%if (ALCSDossier.PAIEMENT_DIRECT.equals(viewBean.getPaiementMode()) || ALCSDossier.PAIEMENT_TIERS.equals(viewBean.getPaiementMode())){ %>
                				<option value="<%=ALCSDossier.PAIEMENT_DIRECT %>" <%=viewBean.getPaiementMode().equals(ALCSDossier.PAIEMENT_DIRECT)?"selected='selected'":"" %>><%=objSession.getCodeLibelle(ALCSDossier.PAIEMENT_DIRECT) %></option>
                				
                				<option value="<%=ALCSDossier.PAIEMENT_TIERS%>" <%=viewBean.getPaiementMode().equals(ALCSDossier.PAIEMENT_TIERS)?"selected='selected'":"" %>><%=objSession.getCodeLibelle(ALCSDossier.PAIEMENT_TIERS) %></option>	
                			<%}%>	
                			</select> 
                			<%if(viewBean.isPaiementFromDossier()) {%>
                				<img src="images/folder_user.png" height="16" width="16" alt="<%=objSession.getLabel("AL0003_IMG_BENEF_DOSSIER")%>"/>
                	        <%} %>      		
	                		
							
	                	</td>
	                
	           				
	           				<%if (viewBean.getAfficheAttesAlloc()){ %>
	                	
	                	    <td  class="label"  > 
	                            <ct:FWLabel key="AL0003_DROIT_ECHEANCE_ATTEST_ALLOC"/>  </td>
	                        <td >
	                     		
	                 		<ct:inputHidden  name="droitComplexModel.droitModel.attestationAlloc"/>
	                 		<% if(viewBean.getDroitComplexModel().getDroitModel().getAttestationAlloc().booleanValue()){ %> 
	                    	<input type="checkbox" checked="checked" name="attestationAlloc" tabindex="26"
	                    		onclick="reportCheckboxInModel('droitComplexModel.droitModel');"   		
	                    	/>
	                    	
	                    	<%}else{%>
	                    	<input type="checkbox" tabindex="27" name="attestationAlloc" onclick="reportCheckboxInModel('droitComplexModel.droitModel');"/>
	                    	<%}%>
	                 	           		
	                 	</td> 
	                 	<%}%>
	         
	                    <td class="label"></td>
	                    <td></td>    
	                </tr>
	                <tr>
	                	<td></td>
	                	<td>
	                	<fieldset id="selectionBeneficiaire">	
					<div data-g-multiwidgets="languages:LANGUAGES"  style="float:left;">	

						<ct:widget id='widgetTiers' name='widgetTiers' 
					          defaultValue="<%=viewBean.getInfosBeneficiaire() %>"
					           styleClass="widgetTiers">
						<ct:widgetService methodName="find" className="<%=PersonneEtendueService.class.getName()%>">
							<ct:widgetCriteria criteria="forDesignation1Like" label="AL0004_BENEFICIAIRE_W_NOM"/>								
							<ct:widgetCriteria criteria="forDesignation2Like" label="AL0004_BENEFICIAIRE_W_PRENOM"/>
							<ct:widgetCriteria criteria="forNumeroAvsActuel" label="AL0004_BENEFICIAIRE_W_AVS"/>									
							<ct:widgetCriteria criteria="forDateNaissance" label="AL0004_BENEFICIAIRE_W_NAISS"/>																						
							<ct:widgetLineFormatter format="#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}"/>
							<ct:widgetJSReturnFunction>
								<script type="text/javascript">
									function(element){
										$('#idTiersBeneficiaire').val($(element).attr('tiers.id'));	
										this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
									}
								</script>										
							</ct:widgetJSReturnFunction>
							</ct:widgetService>
						</ct:widget>
						<ct:widget id='widgetAdmin' name='widgetAdmin' 
					            styleClass="widgetAdmin"  
					            defaultValue="<%=viewBean.getInfosBeneficiaire() %>">
						<ct:widgetService methodName="find" className="<%=AdministrationService.class.getName()%>">										
							<ct:widgetCriteria criteria="forCodeAdministrationLike" label="AL0004_BENEFICIAIRE_W_CODE_ADMIN"/>																
								<ct:widgetCriteria criteria="forDesignation1Like" label="AL0004_BENEFICIAIRE_W_DESIGNATION_ADMIN"/>								
							<ct:widgetLineFormatter format="#{tiers.designation2} #{tiers.designation1}, #{tiers.idTiers} "/>
							<ct:widgetJSReturnFunction>
								<script type="text/javascript">
									function(element){
										$('#idTiersBeneficiaire').val($(element).attr('tiers.id'));
										this.value=$(element).attr('tiers.designation2')+' '+$(element).attr('tiers.designation1');
									}
								</script>										
							</ct:widgetJSReturnFunction>
						</ct:widgetService>
						</ct:widget>
					
					</div> 
					<div id="idTiers"> (<a href="<%=servletContext+"/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId="+viewBean.getDroitComplexModel().getDroitModel().getIdTiersBeneficiaire()%>"><%=viewBean.getDroitComplexModel().getDroitModel().getIdTiersBeneficiaire()%></a>)</div>
				 </fieldset>
	                	</td>
	                	 <td class="label"></td>
	                    <td></td> 
	                </tr>
	          </table>
	               
	       
			<%-- /tpl:insert --%>
			</td></tr>								
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="menuWEBAF"/>


<%
String menuOptionsId = "";
if(!viewBean.getDroitComplexModel().getDroitModel().isNew()&& ALCSDroit.TYPE_ENF.equals(viewBean.getDroitComplexModel().getDroitModel().getTypeDroit())){
%>
<ct:menuChange displayId="options" menuId="droit-detail-enfant" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="id" checkAdd="no" value="<%=viewBean.getDroitComplexModel().getId()%>"  />
	<ct:menuSetAllParams key="idDossier" checkAdd="no" value="<%=viewBean.getDroitComplexModel().getDroitModel().getIdDossier()%>"  />
	<ct:menuSetAllParams checkAdd="no" key="searchModel.forIdDroit" value="<%=viewBean.getDroitComplexModel().getId()%>" />	
</ct:menuChange>

<% 
}else{
%>	
<ct:menuChange displayId="options" menuId="droit-detail" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="id" checkAdd="no" value="<%=viewBean.getDroitComplexModel().getId()%>"  />
	<ct:menuSetAllParams key="idDossier" checkAdd="no" value="<%=viewBean.getDroitComplexModel().getDroitModel().getIdDossier()%>"  />
	<ct:menuSetAllParams checkAdd="no" key="searchModel.forIdDroit" value="<%=viewBean.getDroitComplexModel().getId()%>" />	
</ct:menuChange>
<%
}
%>

<%-- tpl:insert attribute="zoneEndPage" --%>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/simple-popup.js"></script>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
