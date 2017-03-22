<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="ch.horizon.jaspe.util.JACalendarGregorian"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page import="globaz.globall.util.JADate"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="ch.globaz.amal.business.models.caissemaladie.CaisseMaladie"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Collection"%>
<%@page import="ch.globaz.pyxis.business.model.AdministrationComplexModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="ch.globaz.amal.business.models.caissemaladie.CaisseMaladieGroupeRCListe"%>
<%@page import="ch.globaz.amal.business.models.caissemaladie.CaisseMaladieGroupeRCListeSearch"%>
<%@page import="globaz.amal.utils.AMCaisseMaladieHelper"%>
<%@page import="globaz.amal.vb.caissemaladie.AMCaissemaladieViewBean"%>
<%@page import="globaz.amal.vb.caissemaladie.AMCaissemaladieListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme"%>
<%@page import="ch.globaz.amal.business.constantes.IAMActions"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Arrays"%>
<%@page import="globaz.globall.parameters.FWParametersCode"%>
<%@page import="java.util.Iterator"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>

<%
	AMCaissemaladieListViewBean viewBean=(AMCaissemaladieListViewBean)request.getAttribute("viewBean");
	List<String> idTiersCMInProgress = viewBean.getIdTiersCMInProgress();
	size=viewBean.getSize();
	
	String SEDEXRPAction = IAMActions.ACTION_CAISSEMALADIE+".launchSEDEXRPProcess";
	String SEDEXCOAction = IAMActions.ACTION_CAISSEMALADIE+".launchSEDEXCOProcess";
// 	String annonceAction = IAMActions.ACTION_CAISSEMALADIE+".launchAnnonceProcess";
	String simulationRPAction = IAMActions.ACTION_CAISSEMALADIE+".launchSimulationProcess";
	String simulationCOAction = IAMActions.ACTION_CAISSEMALADIE+".launchSimulationCOProcess";
	
	boolean hasRightOnSEDEX = objSession.hasRight(SEDEXRPAction, FWSecureConstants.ADD);
	boolean hasRightOnSEDEXCO = objSession.hasRight(SEDEXCOAction, FWSecureConstants.ADD);
// 	boolean hasRightOnAnnonce = objSession.hasRight(annonceAction, FWSecureConstants.ADD);
	boolean hasRightOnSimulationRP = objSession.hasRight(simulationRPAction, FWSecureConstants.ADD);
	boolean hasRightOnSimulationCO = objSession.hasRight(simulationCOAction, FWSecureConstants.ADD);
%>

<%-- tpl:insert attribute="zoneScripts" --%>
<script type="text/javascript">
var s_currentButtonId = "";
var s_typeJob = "";
var s_descriptionCaisse = "";
/**
 * Initialisation et fonctions particulières de gestion du tableau de bord
 */
$(document).ready(function() {	
	// ajout des champs nécessaires dans le formulaires
	addFormFields();
	$(".trSedexRP").hide();
	// par défaut, disabled toutes les checkbox
	$('table :checkbox').each(function(index){
		var idCheckBox = $(this).attr('id');
		toggleOpacityElement(idCheckBox,0.2,1.0);
	});
	
	$('.rowCaisses :button').each(function(index){
		var idCheckBox = $(this).attr('id');
		toggleOpacityElement(idCheckBox,0.2,1.0);
	});
	
	$('.rowCaisses td').click(function(){
		var b_input = $(this).children().is(":input");
		if (!b_input) {
			var s_idCaisseImg = $(this).parent().attr('id');	
			var s_idCaisse=s_idCaisseImg.split("_")[2];
			manageDetailButtonEvent(s_idCaisse);
		}		
	});
	
	$('.cellGroupe').click(function(event){	
		var s_idImgExpand = $(this).parent().find(".imgExpand").attr("id");		
		var s_idGrp=s_idImgExpand.split("_")[1];
		toggleChildrenLines(s_idGrp);
	});
	
	// enregistrement de l'événement click sur les images expand
	$('.imgExpand').click(function(){
		var s_idImgExpand = $(this).attr('id');		
		var s_idGrp=s_idImgExpand.split("_")[1];
		toggleChildrenLines(s_idGrp);
	});
	// enregistrement de l'événement click sur les checkbox
	$('input:checkbox').click(function(){		
		var s_idCheckBox = this.id;
		var s_idJob = s_idCheckBox.split("_")[1];
		var s_idCheckBoxGroupe = "checkboxGroupe_"+s_idJob+"_";		
		if(s_idCheckBox.length>=s_idCheckBoxGroupe.length){
			if(s_idCheckBox.substring(0,s_idCheckBoxGroupe.length)==s_idCheckBoxGroupe){
				toggleChildrenElements(s_idJob);
			}
		}
	});
	// enregistrement des actions sur les boutons
	$('table').on('click','button',function(event){
		//Reset des input
		setDisabledElement("typeSelectionYearAll", false);
		$("#typeSelectionYearAll").click();
		
		s_descriptionCaisse = $(this).parent().parent().find("td:nth-child(3)").text();
		$(".descriptionCaisse").html(s_descriptionCaisse);
		var s_idButton = this.id;
		if(s_idButton.indexOf('SimulationRP')>=0){
			manageSimulationRPButtonEvent(s_idButton, "S");
		} else if(s_idButton.indexOf('SEDEXRP')>=0){
			manageSEDEXRPButtonEvent(s_idButton, "R");
		} else if(s_idButton.indexOf('SEDEXCO')>=0){
			manageSEDEXCOButtonEvent(s_idButton, "C");
		} else if(s_idButton.indexOf('SimulationCO')>=0){
			manageSimulationCOButtonEvent(s_idButton, "SC");
		} 
// 		else if(s_idButton.indexOf('Annoncer')>=0){			
// 			if (confirm("Cosama désactivé, continuer ?")) {
// 				manageAnnoncerButtonEvent(s_idButton, "A");				
// 			}
// 		}  
	});
	
	<% if (size == 1) { %>
		$(".imgExpand").click();	
	<% } %>
	$("#dialogSimuAnnonce").dialog({
		closeOnEscape: true, 
		height: 300, 
		width: 520,
		minHeight: 300, 
		minWidth: 520,
		resizable:false,
		autoOpen: false,
		modal:true,
		dialogClass: "dialogSimuAnnonceClass",
		position:['center','top'],
		buttons : {
			"Ok" : function() {
				if (s_typeJob == "S") {
					b_returnResult = processSimulationRP(s_currentButtonId);
				} else if (s_typeJob == "R") {
					b_returnResult = processSEDEXRP(s_currentButtonId);
				} else if (s_typeJob == "A") {
					//b_returnResult = processAnnoncer(s_currentButtonId);
				} else {
					alert("Erreur, job inconnu");
				}				
				if (b_returnResult) {
					$(this).dialog("close");
				}
			},
			"Annuler": function() {$(this).dialog("close");}			
		}, 
		open: function() { 
			$(this).show();
			if (s_typeJob == "S") {
				$("#dialogSimuAnnonce").dialog('option', 'title', 'Simulation RP');
			} else if (s_typeJob == "R") {
				$("#dialogSimuAnnonce").dialog('option', 'title', 'SEDEX RP');
			} else if (s_typeJob == "A") {
				//$("#dialogSimuAnnonce").dialog('option', 'title', 'Nouvelle annonce');
			}
		}
	});
	
	$("#dialogSimuAnnonceCO").dialog({
		closeOnEscape: true, 
		height: 300, 
		width: 520,
		minHeight: 300, 
		minWidth: 520,
		resizable:false,
		autoOpen: false,
		modal:true,
		dialogClass: "dialogSimuAnnonceClass",
		position:['center','top'],
		buttons : {
			"Ok" : function() {
				if (s_typeJob == "C") {
					b_returnResult = processSEDEXCO(s_currentButtonId);
				} else if (s_typeJob == "SC") {
					b_returnResult = processSimulationCO(s_currentButtonId);				
				} else {
					alert("Erreur, job inconnu");
				}				
				if (b_returnResult) {
					$(this).dialog("close");
				}
			},
			"Annuler": function() {$(this).dialog("close");}			
		}, 
		open: function() { 
			$(this).show();
			if (s_typeJob == "C") {
				$("#dialogSimuAnnonceCO").dialog('option', 'title', 'SEDEX CO');
			} else if (s_typeJob == "SC") {
				$("#dialogSimuAnnonceCO").dialog('option', 'title', 'Simulation CO');
			}
		}
	});
	
	$("#dialogSimuAnnonce :radio").click(function() {
		if ($(this).attr("id")=="typeSelectionYearOne" || $(this).attr("id")=="typeSelectionYearAll") {
			if ($(this).val() == "one") {
				setDisabledElement("anneeAnnonceSimulation", false);
				$("#anneeAnnonceSimulation").focus();
			} else {
				$("#anneeAnnonceSimulation").val("");
				setDisabledElement("anneeAnnonceSimulation", true);
			}
		} else {
			if ($(this).attr("id")=="msgSelectionDecreeStop") {
				setDisabledElement("typeSelectionYearOne", false);
				setDisabledElement("typeSelectionYearAll", false);
			} else if ($(this).attr("id")=="msgSelectionInsuranceQuery") {
				setDisabledElement("typeSelectionYearOne", true);
				setDisabledElement("typeSelectionYearAll", false);
				$("#typeSelectionYearAll").click();
				$("#anneeAnnonceSimulation").val("");
			} else {
				setDisabledElement("typeSelectionYearOne", false);
				setDisabledElement("typeSelectionYearAll", true);
				$("#anneeAnnonceSimulation").val("");
				setDisabledElement("anneeAnnonceSimulation", true);
				$("#typeSelectionYearOne").click();
			}
		}
	});
	
	$("#dialogSimuAnnonceCO :radio").click(function() {
		if ($(this).attr("id")=="msgSelectionListOfGuaranteedAssumptions") {
			//
		}
	});
	
	$("#msgSelectionNone").click(function() {
		if ($(this).attr("checked")) {
			$(".msgTypeSelectionSedexRP").attr("disabled",false);
		} else {
			$(".msgTypeSelectionSedexRP").attr("disabled",true);
		}
	});
});

/**
 * Ajout des champs nécessaires au formulaire courant
 */
function addFormFields(){
	var s_textToInsert = '';
	if(parent.document.forms[0].elements('selectedGroupe')==null){
		s_textToInsert +='<input type="hidden" name="selectedGroupe" value="">';
	}
	if(parent.document.forms[0].elements('selectedCaisse')==null){
		s_textToInsert +='<input type="hidden" name="selectedCaisse" value="">';
	}
	if(parent.document.forms[0].elements('userAction')==null){
		s_textToInsert +='<input type="hidden" name="userAction" value="">';
	}
	if(parent.document.forms[0].elements('selectedId')==null){
		s_textToInsert +='<input type="hidden" name="selectedId" value="">';
	}
	if(parent.document.forms[0].elements('selectedAnnee')==null){
		s_textToInsert +='<input type="hidden" name="selectedAnnee" value="">';
	}
	if(parent.document.forms[0].elements('selectedTypeMessage')==null){
		s_textToInsert +='<input type="hidden" name="selectedTypeMessage" value="">';
	}
	if(parent.document.forms[0].elements('simulationSedex')==null){
		s_textToInsert +='<input type="hidden" name="simulationSedex" value="">';
	}	
		
	if(s_textToInsert.length>0){
		parent.$('form').append(s_textToInsert);
	}
}

function manageDetailButtonEvent(idCaisse){
	var s_actionAfficher = '<%=IAMActions.ACTION_CAISSEMALADIE+".afficher"%>';
	
    parent.document.forms[0].elements('userAction').value = s_actionAfficher;
    parent.document.forms[0].elements('selectedId').value = idCaisse;
	parent.document.forms[0].onsubmit = '';
	parent.document.forms[0].target = '';
	parent.document.forms[0].submit();
}

/**
 * Gestion des événements sur les boutons SEDEX RP
 */
function manageSEDEXRPButtonEvent(currentIdButton, type){
	s_currentButtonId = currentIdButton;
	s_typeJob = type;
	$("#dialogSimuAnnonce").dialog( "option", "height", 450 );
	$("#dialogSimuAnnonce").dialog( "option", "minHeight", 450 );
	$("#msgSelectionNone").attr("checked", true);
	$(".trSedexRP").show();
	$(".msgTypeSelectionSedexRP").attr("disabled",false);
	$("#msgSelectionNone").hide();
	$("#msgSelectionDecreeStop").click();
	$("#dialogSimuAnnonce").dialog('open');	
}

/**
 * Gestion des événements sur les boutons SimulationRP
 */
function manageSimulationRPButtonEvent(currentIdButton, type){
	s_currentButtonId = currentIdButton;
	s_typeJob = type;
	$("#dialogSimuAnnonce").dialog( "option", "height", 450 );
	$("#dialogSimuAnnonce").dialog( "option", "minHeight", 450 );
	$(".trSedexRP").show();
	//Affichage de la case à cocher pour simuler les message
	$("#msgSelectionNone").show();
	setOpacityElement("msgSelectionNone",1.0);
	setDisabledElement("msgSelectionNone", false);
	$("#msgSelectionNone").attr("checked", false);
	$(".msgTypeSelectionSedexRP").attr("disabled",true);
	//Fin affichage de la case à cocher pour simuler les message
	
	$("#dialogSimuAnnonce").dialog('open');	
}

/**
 * Gestion des événements sur les boutons SEDEX CO
 */
function manageSEDEXCOButtonEvent(currentIdButton, type){	
	s_currentButtonId = currentIdButton;
	s_typeJob = type;
	$("#dialogSimuAnnonceCO").dialog( "option", "height", 250 );
	$("#dialogSimuAnnonceCO").dialog( "option", "minHeight", 250 );
	$(".msgTypeSelectionSedexCO").attr("disabled",false);
	$("#msgSelectionListOfGuaranteedAssumptions").click();
	
	$("#dialogSimuAnnonceCO").dialog('open');	
}

/**
 * Gestion des événements sur les boutons Simulation CO
 */
function manageSimulationCOButtonEvent(currentIdButton, type){
	s_currentButtonId = currentIdButton;
	s_typeJob = type;
	$("#dialogSimuAnnonceCO").dialog( "option", "height", 250 );
	$("#dialogSimuAnnonceCO").dialog( "option", "minHeight", 250 );
	$(".msgTypeSelectionSedexCO").attr("disabled",false);
	$("#msgSelectionListOfGuaranteedAssumptions").click();
	
	
	$("#dialogSimuAnnonceCO").dialog('open');	
}

function processSEDEXRP(currentIdButton) {
	var s_actionSEDEXRP = '<%=SEDEXRPAction%>';
	var s_idButtonSEDEXRPPrincipale = 'buttonSEDEXRPGroupe_';
	var s_idButtonSEDEXRPEnfant = 'buttonSEDEXRPEnfant_';
	var s_anneeAnnonceSEDEXRP = $("#anneeAnnonceSimulation").val();
	var s_typeMessageSEDEXRP = $(".msgTypeSelectionSedexRP:checked").val();
	
	if ($("#typeSelectionYearOne").attr("checked") && (s_anneeAnnonceSEDEXRP.length != 4 || !$.isNumeric(s_anneeAnnonceSEDEXRP))) {
		alert("Année incorrecte");
		return false;
	}

	if(currentIdButton.substring(0,s_idButtonSEDEXRPEnfant.length)==s_idButtonSEDEXRPEnfant){
		// Proceed with deletion			
		var s_idGroupe=currentIdButton.split('_')[1];
		var s_idTiersCaisse=currentIdButton.split('_')[2];
	    parent.document.forms[0].elements('userAction').value = s_actionSEDEXRP;
	    parent.document.forms[0].elements('selectedCaisse').value = s_idTiersCaisse;
	    parent.document.forms[0].elements('selectedAnnee').value = s_anneeAnnonceSEDEXRP;
	    parent.document.forms[0].elements('selectedTypeMessage').value = s_typeMessageSEDEXRP;
		parent.document.forms[0].onsubmit = '';
		parent.document.forms[0].target = '';
		parent.document.forms[0].submit();
	} else if(currentIdButton.substring(0,s_idButtonSEDEXRPPrincipale.length)==s_idButtonSEDEXRPPrincipale){
		var s_idGroupe=currentIdButton.split('_')[1];
		// Get the status of the main checkbox
		// if checked, print the whole job
		// if not, print the selected childrens
		var s_idRelatedCheckBox='checkboxGroupe_'+s_idGroupe+'_';
	
		if($('#'+s_idRelatedCheckBox).prop('checked') && s_idGroupe!= '0000' && s_idGroupe !='XXXX'){
			    parent.document.forms[0].elements('userAction').value = s_actionSEDEXRP;
			    parent.document.forms[0].elements('selectedGroupe').value = s_idGroupe;
			    parent.document.forms[0].elements('selectedAnnee').value = s_anneeAnnonceSEDEXRP;
			    parent.document.forms[0].elements('selectedTypeMessage').value = s_typeMessageSEDEXRP;
				parent.document.forms[0].onsubmit = '';
				parent.document.forms[0].target = '';
				parent.document.forms[0].submit();
		} else {
			var s_allCaissesId = '';
			var i_nbLines = 0;
			// check the children status (checked) and get the ids
			$('input:checkbox').each(function (iIndex) {
				var s_currentCheckBoxId=this.id;
				var s_s_checkBoxCaisse = 'checkboxCaisse_'+s_idGroupe+'_';
				if(s_currentCheckBoxId.substring(0,s_s_checkBoxCaisse.length)==s_s_checkBoxCaisse){
					i_nbLines++;
					if($('#'+s_currentCheckBoxId).prop('checked')){
						var s_caisseId = s_currentCheckBoxId.split('_')[2];
						if(s_allCaissesId.length>0){
							s_allCaissesId+=';';	
						}
						s_allCaissesId+=s_caisseId;
					}
				}
			});
			if(s_allCaissesId.length>0){
				if(i_nbLines == s_allCaissesId.split(';').length && s_idGroupe!= '0000' && s_idGroupe !='XXXX'){
					    parent.document.forms[0].elements('userAction').value = s_actionSEDEXRP;
					    parent.document.forms[0].elements('selectedGroupe').value = s_idGroupe;
					    parent.document.forms[0].elements('selectedAnnee').value = s_anneeAnnonceSEDEXRP;
					    parent.document.forms[0].elements('selectedTypeMessage').value = s_typeMessageSEDEXRP;
						parent.document.forms[0].onsubmit = '';
						parent.document.forms[0].target = '';
						parent.document.forms[0].submit();
				}else{
					// Sélection d'éléments
					    parent.document.forms[0].elements('userAction').value = s_actionSEDEXRP;
					    parent.document.forms[0].elements('selectedCaisse').value = s_allCaissesId;
					    parent.document.forms[0].elements('selectedAnnee').value = s_anneeAnnonceSEDEXRP;
					    parent.document.forms[0].elements('selectedTypeMessage').value = s_typeMessageSEDEXRP;
						parent.document.forms[0].onsubmit = '';
						parent.document.forms[0].target = '';
						parent.document.forms[0].submit();
				}
			}else{
				alert('Pas d\'élément sélectionné, aucune action ne sera générée');
			}
		}
	}
}

function processSEDEXCO(currentIdButton) {
	var s_actionSEDEXCO = '<%=SEDEXCOAction%>';
	var s_idButtonSEDEXCOPrincipale = 'buttonSEDEXCOGroupe_';
	var s_idButtonSEDEXCOEnfant = 'buttonSEDEXCOEnfant_';
	var s_typeMessageSEDEXCO = $(".msgTypeSelectionSEDEXCO:checked").val();
	
	if(currentIdButton.substring(0,s_idButtonSEDEXCOEnfant.length)==s_idButtonSEDEXCOEnfant){
		// Proceed with deletion			
		var s_idGroupe=currentIdButton.split('_')[1];
		var s_idTiersCaisse=currentIdButton.split('_')[2];
	    parent.document.forms[0].elements('userAction').value = s_actionSEDEXCO;
	    parent.document.forms[0].elements('selectedCaisse').value = s_idTiersCaisse;
	    parent.document.forms[0].elements('selectedTypeMessage').value = s_typeMessageSEDEXCO;
		parent.document.forms[0].onsubmit = '';
		parent.document.forms[0].target = '';
		parent.document.forms[0].submit();
	} else if(currentIdButton.substring(0,s_idButtonSEDEXCOPrincipale.length)==s_idButtonSEDEXCOPrincipale){
		var s_idGroupe=currentIdButton.split('_')[1];
		// Get the status of the main checkbox
		// if checked, print the whole job
		// if not, print the selected childrens
		var s_idRelatedCheckBox='checkboxGroupe_'+s_idGroupe+'_';
	
		if($('#'+s_idRelatedCheckBox).prop('checked') && s_idGroupe!= '0000' && s_idGroupe !='XXXX'){
			    parent.document.forms[0].elements('userAction').value = s_actionSEDEXCO;
			    parent.document.forms[0].elements('selectedGroupe').value = s_idGroupe;
			    parent.document.forms[0].elements('selectedTypeMessage').value = s_typeMessageSEDEXCO;
				parent.document.forms[0].onsubmit = '';
				parent.document.forms[0].target = '';
				parent.document.forms[0].submit();
		} else {
			var s_allCaissesId = '';
			var i_nbLines = 0;
			// check the children status (checked) and get the ids
			$('input:checkbox').each(function (iIndex) {
				var s_currentCheckBoxId=this.id;
				var s_s_checkBoxCaisse = 'checkboxCaisse_'+s_idGroupe+'_';
				if(s_currentCheckBoxId.substring(0,s_s_checkBoxCaisse.length)==s_s_checkBoxCaisse){
					i_nbLines++;
					if($('#'+s_currentCheckBoxId).prop('checked')){
						var s_caisseId = s_currentCheckBoxId.split('_')[2];
						if(s_allCaissesId.length>0){
							s_allCaissesId+=';';	
						}
						s_allCaissesId+=s_caisseId;
					}
				}
			});
			if(s_allCaissesId.length>0){
				if(i_nbLines == s_allCaissesId.split(';').length && s_idGroupe!= '0000' && s_idGroupe !='XXXX'){
					    parent.document.forms[0].elements('userAction').value = s_actionSEDEXCO;
					    parent.document.forms[0].elements('selectedGroupe').value = s_idGroupe;
					    parent.document.forms[0].elements('selectedTypeMessage').value = s_typeMessageSEDEXCO;
						parent.document.forms[0].onsubmit = '';
						parent.document.forms[0].target = '';
						parent.document.forms[0].submit();
				}else{
					// Sélection d'éléments
					    parent.document.forms[0].elements('userAction').value = s_actionSEDEXCO;
					    parent.document.forms[0].elements('selectedCaisse').value = s_allCaissesId;
					    parent.document.forms[0].elements('selectedTypeMessage').value = s_typeMessageSEDEXCO;
						parent.document.forms[0].onsubmit = '';
						parent.document.forms[0].target = '';
						parent.document.forms[0].submit();
				}
			}else{
				alert('Pas d\'élément sélectionné, aucune action ne sera générée');
			}
		}
	}
}

/**
 * Gestion des événements sur les boutons Annoncer
 */
// function manageAnnoncerButtonEvent(currentIdButton, type){
// 	s_currentButtonId = currentIdButton;
// 	s_typeJob = type;
// 	$("#dialogSimuAnnonce").dialog( "option", "height", 300 );
// 	$("#dialogSimuAnnonce").dialog( "option", "minHeight", 300 );
// 	$(".trSedexRP").hide();
// 	$("#msgSelectionNone").hide();
// 	$("#dialogSimuAnnonce").dialog('open');	
// }

// function processAnnoncer(currentIdButton) {
// 	var s_idButtonAnnoncerPrincipale = 'buttonAnnoncerGroupe_';
// 	var s_idButtonAnnoncerEnfant = 'buttonAnnoncerEnfant_';
// 	var s_anneeAnnonceSimulation = $("#anneeAnnonceSimulation").val();
	
// 	if ($("#typeSelectionYearOne").attr("checked") && (s_anneeAnnonceSimulation.length != 4 || !$.isNumeric(s_anneeAnnonceSimulation))) {
// 		alert("Année incorrecte");
// 		return false;
// 	}

// 	if(currentIdButton.substring(0,s_idButtonAnnoncerEnfant.length)==s_idButtonAnnoncerEnfant){		
// 		var s_idGroupe=currentIdButton.split('_')[1];
// 		var s_idTiersCaisse=currentIdButton.split('_')[2];
// 		parent.document.forms[0].elements('userAction').value = s_actionAnnoncer;
// 		parent.document.forms[0].elements('selectedCaisse').value = s_idTiersCaisse;
// 		parent.document.forms[0].elements('selectedAnnee').value = s_anneeAnnonceSimulation;
// 		parent.document.forms[0].onsubmit = '';
// 		parent.document.forms[0].target = '';
// 		parent.document.forms[0].submit();
// 	} else if(currentIdButton.substring(0,s_idButtonAnnoncerPrincipale.length)==s_idButtonAnnoncerPrincipale){

// 		var s_idGroupe=currentIdButton.split('_')[1];
		
// 		var s_idRelatedCheckBox='checkboxGroupe_'+s_idGroupe+'_';

// 		if($('#'+s_idRelatedCheckBox).prop('checked') && s_idGroupe!= '0000' && s_idGroupe !='XXXX'){
// 		    parent.document.forms[0].elements('userAction').value = s_actionAnnoncer;
// 		    parent.document.forms[0].elements('selectedGroupe').value = s_idGroupe;
// 		    parent.document.forms[0].elements('selectedAnnee').value = s_anneeAnnonceSimulation;
// 			parent.document.forms[0].onsubmit = '';
// 			parent.document.forms[0].target = '';
// 			parent.document.forms[0].submit();
// 		} else {
// 			var s_allCaissesId = '';
// 			var i_nbLines = 0;
// 			$('input:checkbox').each(function (iIndex) {
// 				var s_currentCheckBoxId=this.id;
// 				var s_s_checkBoxCaisse = 'checkboxCaisse_'+s_idGroupe+'_';
// 				if(s_currentCheckBoxId.substring(0,s_s_checkBoxCaisse.length)==s_s_checkBoxCaisse){
// 					i_nbLines++;
// 					if($('#'+s_currentCheckBoxId).prop('checked')){
// 						var s_caisseId = s_currentCheckBoxId.split('_')[2];
// 						if(s_allCaissesId.length>0){
// 							s_allCaissesId+=';';	
// 						}
// 						s_allCaissesId+=s_caisseId;
// 					}
// 				}
// 			});
// 			if(s_allCaissesId.length>0){
// 				if(i_nbLines == s_allCaissesId.split(';').length && s_idGroupe!= '0000' && s_idGroupe !='XXXX'){
// 				    parent.document.forms[0].elements('userAction').value = s_actionAnnoncer;
// 				    parent.document.forms[0].elements('selectedGroupe').value = s_idGroupe;
// 				    parent.document.forms[0].elements('selectedAnnee').value = s_anneeAnnonceSimulation;
// 					parent.document.forms[0].onsubmit = '';
// 					parent.document.forms[0].target = '';
// 					parent.document.forms[0].submit();
// 				}else{
// 					// Sélection d'éléments
// 				    parent.document.forms[0].elements('userAction').value = s_actionAnnoncer;
// 				    parent.document.forms[0].elements('selectedCaisse').value = s_allCaissesId;
// 				    parent.document.forms[0].elements('selectedAnnee').value = s_anneeAnnonceSimulation;
// 					parent.document.forms[0].onsubmit = '';
// 					parent.document.forms[0].target = '';
// 					parent.document.forms[0].submit();
// 				}
// 			}else{
// 				alert('Pas d\'élément sélectionné, aucune action ne sera générée');
// 			}

// 		}
// 	}
// }

function processSimulationRP(currentIdButton) {
	var s_actionSimuler = '<%=simulationRPAction%>';
	var s_idButtonSimulerPrincipale = 'buttonSimulationRPGroupe_';
	var s_idButtonSimulerEnfant = 'buttonSimulationRPEnfant_';
	var s_anneeAnnonceSimulation = $("#anneeAnnonceSimulation").val();
	var b_simulerSedex = $("#msgSelectionNone").attr("checked");
	var s_typeMessageSEDEXRP = $(".msgTypeSelectionSedexRP:checked").val();

	if ($("#typeSelectionYearOne").attr("checked") && (s_anneeAnnonceSimulation.length != 4 || !$.isNumeric(s_anneeAnnonceSimulation))) {
		alert("Année incorrecte");
		return false;
	}
	
	if(currentIdButton.substring(0,s_idButtonSimulerEnfant.length)==s_idButtonSimulerEnfant){
		// Proceed with deletion			
		var s_idGroupe=currentIdButton.split('_')[1];
		var s_idTiersCaisse=currentIdButton.split('_')[2];
	    parent.document.forms[0].elements('userAction').value = s_actionSimuler;
	    parent.document.forms[0].elements('selectedCaisse').value = s_idTiersCaisse;
	    parent.document.forms[0].elements('selectedAnnee').value = s_anneeAnnonceSimulation;
	    parent.document.forms[0].elements('simulationSedex').value = b_simulerSedex;
	    parent.document.forms[0].elements('selectedTypeMessage').value = s_typeMessageSEDEXRP;
		parent.document.forms[0].onsubmit = '';
		parent.document.forms[0].target = '';
		parent.document.forms[0].submit();
	} else if(currentIdButton.substring(0,s_idButtonSimulerPrincipale.length)==s_idButtonSimulerPrincipale){
		var s_idGroupe=currentIdButton.split('_')[1];
		// Get the status of the main checkbox
		// if checked, print the whole job
		// if not, print the selected childrens
		var s_idRelatedCheckBox='checkboxGroupe_'+s_idGroupe+'_';
	
		if($('#'+s_idRelatedCheckBox).prop('checked') && s_idGroupe!= '0000' && s_idGroupe !='XXXX'){
			    parent.document.forms[0].elements('userAction').value = s_actionSimuler;
			    parent.document.forms[0].elements('selectedGroupe').value = s_idGroupe;
			    parent.document.forms[0].elements('selectedAnnee').value = s_anneeAnnonceSimulation;
			    parent.document.forms[0].elements('simulationSedex').value = b_simulerSedex;
			    parent.document.forms[0].elements('selectedTypeMessage').value = s_typeMessageSEDEXRP;
				parent.document.forms[0].onsubmit = '';
				parent.document.forms[0].target = '';
				parent.document.forms[0].submit();
		} else {
			var s_allCaissesId = '';
			var i_nbLines = 0;
			// check the children status (checked) and get the ids
			$('input:checkbox').each(function (iIndex) {
				var s_currentCheckBoxId=this.id;
				var s_s_checkBoxCaisse = 'checkboxCaisse_'+s_idGroupe+'_';
				if(s_currentCheckBoxId.substring(0,s_s_checkBoxCaisse.length)==s_s_checkBoxCaisse){
					i_nbLines++;
					if($('#'+s_currentCheckBoxId).prop('checked')){
						var s_caisseId = s_currentCheckBoxId.split('_')[2];
						if(s_allCaissesId.length>0){
							s_allCaissesId+=';';	
						}
						s_allCaissesId+=s_caisseId;
					}
				}
			});
			if(s_allCaissesId.length>0){
				if(i_nbLines == s_allCaissesId.split(';').length && s_idGroupe!= '0000' && s_idGroupe !='XXXX'){
					    parent.document.forms[0].elements('userAction').value = s_actionSimuler;
					    parent.document.forms[0].elements('selectedGroupe').value = s_idGroupe;
					    parent.document.forms[0].elements('selectedAnnee').value = s_anneeAnnonceSimulation;
					    parent.document.forms[0].elements('simulationSedex').value = b_simulerSedex;
					    parent.document.forms[0].elements('selectedTypeMessage').value = s_typeMessageSEDEXRP;
						parent.document.forms[0].onsubmit = '';
						parent.document.forms[0].target = '';
						parent.document.forms[0].submit();
				}else{
					// Sélection d'éléments
					    parent.document.forms[0].elements('userAction').value = s_actionSimuler;
					    parent.document.forms[0].elements('selectedCaisse').value = s_allCaissesId;
					    parent.document.forms[0].elements('selectedAnnee').value = s_anneeAnnonceSimulation;
					    parent.document.forms[0].elements('simulationSedex').value = b_simulerSedex;
					    parent.document.forms[0].elements('selectedTypeMessage').value = s_typeMessageSEDEXRP;
						parent.document.forms[0].onsubmit = '';
						parent.document.forms[0].target = '';
						parent.document.forms[0].submit();
				}
			}else{
				alert('Pas d\'élément sélectionné, aucune action ne sera générée');
			}
		}
	}
}

function processSimulationCO(currentIdButton) {
	var s_actionSimuler = '<%=simulationCOAction%>';
	var s_idButtonSimulerPrincipale = 'buttonSimulationCOGroupe_';
	var s_idButtonSimulerEnfant = 'buttonSimulationCOEnfant_';

	if(currentIdButton.substring(0,s_idButtonSimulerEnfant.length)==s_idButtonSimulerEnfant){
		// Proceed with deletion			
		var s_idGroupe=currentIdButton.split('_')[1];
		var s_idTiersCaisse=currentIdButton.split('_')[2];
	    parent.document.forms[0].elements('userAction').value = s_actionSimuler;
	    parent.document.forms[0].elements('selectedCaisse').value = s_idTiersCaisse;
		parent.document.forms[0].onsubmit = '';
		parent.document.forms[0].target = '';
		parent.document.forms[0].submit();
	} else if(currentIdButton.substring(0,s_idButtonSimulerPrincipale.length)==s_idButtonSimulerPrincipale){
		var s_idGroupe=currentIdButton.split('_')[1];
		// Get the status of the main checkbox
		// if checked, print the whole job
		// if not, print the selected childrens
		var s_idRelatedCheckBox='checkboxGroupe_'+s_idGroupe+'_';
	
		if($('#'+s_idRelatedCheckBox).prop('checked') && s_idGroupe!= '0000' && s_idGroupe !='XXXX'){
			    parent.document.forms[0].elements('userAction').value = s_actionSimuler;
			    parent.document.forms[0].elements('selectedGroupe').value = s_idGroupe;
				parent.document.forms[0].onsubmit = '';
				parent.document.forms[0].target = '';
				parent.document.forms[0].submit();
		} else {
			var s_allCaissesId = '';
			var i_nbLines = 0;
			// check the children status (checked) and get the ids
			$('input:checkbox').each(function (iIndex) {
				var s_currentCheckBoxId=this.id;
				var s_s_checkBoxCaisse = 'checkboxCaisse_'+s_idGroupe+'_';
				if(s_currentCheckBoxId.substring(0,s_s_checkBoxCaisse.length)==s_s_checkBoxCaisse){
					i_nbLines++;
					if($('#'+s_currentCheckBoxId).prop('checked')){
						var s_caisseId = s_currentCheckBoxId.split('_')[2];
						if(s_allCaissesId.length>0){
							s_allCaissesId+=';';	
						}
						s_allCaissesId+=s_caisseId;
					}
				}
			});
			if(s_allCaissesId.length>0){
				if(i_nbLines == s_allCaissesId.split(';').length && s_idGroupe!= '0000' && s_idGroupe !='XXXX'){
					    parent.document.forms[0].elements('userAction').value = s_actionSimuler;
					    parent.document.forms[0].elements('selectedGroupe').value = s_idGroupe;
						parent.document.forms[0].onsubmit = '';
						parent.document.forms[0].target = '';
						parent.document.forms[0].submit();
				}else{
					// Sélection d'éléments
					    parent.document.forms[0].elements('userAction').value = s_actionSimuler;
					    parent.document.forms[0].elements('selectedCaisse').value = s_allCaissesId;
						parent.document.forms[0].onsubmit = '';
						parent.document.forms[0].target = '';
						parent.document.forms[0].submit();
				}
			}else{
				alert('Pas d\'élément sélectionné, aucune action ne sera générée');
			}
		}
	}
}
/**
 * Toggle hide/show children lines
 */
function toggleChildrenLines(s_idRow){	
	// variables de travail
	var s_idImgExpand = 'imgExpand_'+s_idRow;
	var s_srcImgExpand = $('#'+s_idImgExpand).attr('src');
	var i_nbLignesEnfants = 0;
	
	// TOGGLE THE CHILDREN
	$('table tr').each(function (iIndex) {
		var s_idLigne=this.id;		
		if(s_idLigne!=null && s_idLigne.length>0){
			var s_idLigneChidrenStart = 'assurance_';
			// check if we find the ligneEnfant and work on it
			if(s_idLigne.length>=s_idLigneChidrenStart.length){
				if(s_idLigne.substring(0,s_idLigneChidrenStart.length)==s_idLigneChidrenStart){
					var s_idJobLine = s_idLigne.split('_')[1];					
					if(s_idJobLine == s_idRow){						
						$('#'+s_idLigne).toggle();
						i_nbLignesEnfants++;
					}
				}
			}
		}
	});	
	// TOGGLE THE IMAGE	
	if(s_srcImgExpand=='<%=request.getContextPath()%>/images/icon-collapse.gif'){
		$('#'+s_idImgExpand).attr('src','<%=request.getContextPath()%>/images/icon-expand.gif');
	}else{
		$('#'+s_idImgExpand).attr('src','<%=request.getContextPath()%>/images/icon-collapse.gif');
	}
	
	// TOGGLE DE LA CHECKBOX PRINCIPALE
	if(i_nbLignesEnfants>1){
		toggleOpacityElement('checkboxGroupe_'+s_idRow+'_',0.2,1.0);
	}
}

/**
 * Toggle all interactiv elements for a specific job
 */
function toggleChildrenElements(currentIdJob){
	// Preparation des tableaux pour enregistrer les id des enfants
	var a_idChildrens= new Array();
	var i_incrementChildren = 0;

	// 1) Checkbox et récupération des id enfants
	$('input:checkbox').each(function (iIndex) {
		var s_idCheckBox=this.id;
		var s_idCheckBoxStart='checkboxCaisse_'+currentIdJob+'_';
		if(s_idCheckBox.length>=s_idCheckBoxStart.length){
			if(s_idCheckBox.substring(0,s_idCheckBoxStart.length)==s_idCheckBoxStart){
				toggleOpacityElement(s_idCheckBox,0.2,1.0);
				if($('#checkboxGroupe_'+currentIdJob+'_').prop('checked')==true){
					$('#'+s_idCheckBox).prop('checked',true);
				} else {
					$('#'+s_idCheckBox).prop('checked',false);
				}  
				var s_idStatus = s_idCheckBox.split("_")[2];
				a_idChildrens[i_incrementChildren++] = s_idStatus;
			}
		}
	});
	var s_idButtonSimulationRPEnfant = 'buttonSimulationRPEnfant_'+currentIdJob+'_';
	var s_idButtonSimulationRPGroupe = 'buttonSimulationRPGroupe_'+currentIdJob+'_';
	var s_ButtonSimulationRPIsDisabled = ''+$('#'+s_idButtonSimulationRPGroupe).prop('disabled');
	
// 	var s_idButtonAnnoncerEnfant = 'buttonAnnoncerEnfant_'+currentIdJob+'_';
// 	var s_idButtonAnnoncerGroupe = 'buttonAnnoncerGroupe_'+currentIdJob+'_';
// 	var s_ButtonAnnoncerIsDisabled = ''+$('#'+s_idButtonAnnoncerGroupe).prop('disabled');
	
	var s_idButtonSEDEXRPEnfant = 'buttonSEDEXRPEnfant_'+currentIdJob+'_';
	var s_idButtonSEDEXRPGroupe = 'buttonSEDEXRPGroupe_'+currentIdJob+'_';
	var s_ButtonSEDEXRPIsDisabled = ''+$('#'+s_idButtonSEDEXRPGroupe).prop('disabled');
	
	var s_idButtonSEDEXCOEnfant = 'buttonSEDEXCOEnfant_'+currentIdJob+'_';
	var s_idButtonSEDEXCOGroupe = 'buttonSEDEXCOGroupe_'+currentIdJob+'_';
	var s_ButtonSEDEXCOIsDisabled = ''+$('#'+s_idButtonSEDEXCOGroupe).prop('disabled');
	
	var s_idButtonSimulationCOEnfant = 'buttonSimulationCOEnfant_'+currentIdJob+'_';
	var s_idButtonSimulationCOGroupe = 'buttonSimulationCOGroupe_'+currentIdJob+'_';
	var s_ButtonSimulationCOIsDisabled = ''+$('#'+s_idButtonSimulationCOGroupe).prop('disabled');
	
	// Parcours de tous les enfants
	for(iCurrentId=0;iCurrentId<a_idChildrens.length;iCurrentId++){
		var b_Sent = false;
		var s_currentIdSimuler = s_idButtonSimulationRPEnfant+a_idChildrens[iCurrentId]+'_';
// 		var s_currentIdAnnoncer = s_idButtonAnnoncerEnfant+a_idChildrens[iCurrentId]+'_';
		var s_currentIdSEDEXRP = s_idButtonSEDEXRPEnfant+a_idChildrens[iCurrentId]+'_';
		var s_currentIdSEDEXCO = s_idButtonSEDEXCOEnfant+a_idChildrens[iCurrentId]+'_';
		var s_currentIdSimulerCO = s_idButtonSimulationCOEnfant+a_idChildrens[iCurrentId]+'_';
			toggleOpacityElement(s_currentIdSimuler,0.2,1.0);
// 			toggleOpacityElement(s_currentIdAnnoncer,0.2,1.0);
			toggleOpacityElement(s_currentIdSEDEXRP,0.2,1.0);
			toggleOpacityElement(s_currentIdSEDEXCO,0.2,1.0);
			toggleOpacityElement(s_currentIdSimulerCO,0.2,1.0);
	}
	
}

/**
 * Toggle the visibility of an graphic element
 */
function toggleOpacityElement(idElement, opacityHidden, opacityShown){
	if($('#'+idElement).css('opacity') <= opacityHidden) {
		setOpacityElement(idElement,opacityShown);
		setDisabledElement(idElement, false);
	}else{
		setOpacityElement(idElement, opacityHidden);
		setDisabledElement(idElement, true);
	}		
}

/**
 * set the opacity of a specific element
 */
function setOpacityElement(idElement, myOpacity){
	$('#'+idElement).css({opacity:myOpacity});
}

/**
 * set the disabled attribute of a specific element
 */
function setDisabledElement(idElement, toDisabled){
	if(toDisabled){
		$('#'+idElement).prop('disabled','disabled');
	}else{
		$('#'+idElement).removeProp('disabled');
	}
}

</script>
<style type="text/css">
	.dialogSimuAnnonceClass {
		font-size: 0.9em;
		color:red;
	}
</style>
<%-- /tpl:insert --%>

<%@ include file="/theme/list/javascripts.jspf" %>	   	    

<%-- tpl:insert attribute="zoneHeaders" --%>

	<TH colspan="2">&nbsp;</TH>   		
	<TH colspan="3" style="width:400px">Description</TH>
	<TH>SEDEX RP</TH>   		
	<TH>Simulation RP</TH>   		
	<TH>SEDEX CO</TH>
	<TH>Simulation CO</TH>
<%-- /tpl:insert --%> 

<%
boolean showAllCaisses = true;
if (!JadeStringUtil.isEmpty(request.getParameter("searchModel.forIdTiersGroupe")) 
		|| !JadeStringUtil.isBlankOrZero(request.getParameter("searchModel.forNumCaisse"))
		|| !JadeStringUtil.isBlankOrZero(request.getParameter("searchModel.likeNomCaisse"))) {
	showAllCaisses = false;
}

boolean orderByNumero = false;
if ("orderRCListeNumCaisse".equals(request.getParameter("searchModel.orderBy"))) {
	orderByNumero = true;
}
boolean oneResultShow = false;
if (viewBean.getSize()==1) {
	oneResultShow = true;
}
TreeMap<String, ArrayList<CaisseMaladie>> mapGroupesAssureurs = new TreeMap<String, ArrayList<CaisseMaladie>>();
ArrayList<String> listAssureursNumero = new ArrayList<String>();
ArrayList<String> listAssureursNom = new ArrayList<String>();
ArrayList<CaisseMaladie> listAllAssureurs = new ArrayList<CaisseMaladie>();
HashMap mapGroupes = AMCaisseMaladieHelper.getGroupesCM();

for (int ind =0;ind<viewBean.getSize();ind++) {
	AMCaissemaladieViewBean line = (AMCaissemaladieViewBean)viewBean.getEntity(ind);
	if (!mapGroupes.containsKey(line.getCaisseMaladie().getIdTiersCaisse())) {
		listAllAssureurs.add(line.getCaisseMaladie());	
	}
	ArrayList<CaisseMaladie> arrayAssurances = new ArrayList<CaisseMaladie>();
	String numGroupe = "0000";
	String nomGroupe = "Sans groupe";
	if (!JadeStringUtil.isBlankOrZero(line.getCaisseMaladie().getNumGroupe())) {
		numGroupe = JadeStringUtil.fillWithZeroes(line.getCaisseMaladie().getNumGroupe(),5);
		nomGroupe = line.getCaisseMaladie().getNomGroupe();
	}
	
	if (!mapGroupes.containsKey(line.getCaisseMaladie().getIdTiersCaisse())) {
		if (!mapGroupesAssureurs.containsKey(numGroupe)) {
			arrayAssurances.add(line.getCaisseMaladie());
			mapGroupesAssureurs.put(numGroupe, arrayAssurances);
		} else {
			ArrayList arrayTmp = mapGroupesAssureurs.get(numGroupe);
			arrayTmp.add(line.getCaisseMaladie());
			mapGroupesAssureurs.put(numGroupe, arrayTmp);
		}
	}
}
AMCaissemaladieViewBean line = (AMCaissemaladieViewBean)viewBean.getEntity(1);
size = mapGroupesAssureurs.keySet().size();
%>

<%// include file="/theme/list/tableHeader.jspf" %>
	    </TR>
<%	String rowStyle = "row";
	int i = 0;	
	for (Iterator<String> it = mapGroupesAssureurs.keySet().iterator(); it.hasNext();) {	    	
		String key = it.next();
	    ArrayList arrayAssu = mapGroupesAssureurs.get(key);
		boolean condition = (i % 2 == 0);
		%>
		<%-- tpl:insert attribute="zoneCondition" --%>
		<%-- /tpl:insert --%>
		<%// include file="/theme/list/lineStyle.jspf" 
		if (condition) {
			rowStyle = "row";
		} else {
			rowStyle = "rowOdd";
		}
		%>		
		<%-- tpl:insert attribute="zoneList" --%>
		<%if (i == 0 && showAllCaisses)  {
			int sizeTotal = listAllAssureurs.size();
		%>
			<!-- TR TOUTES LES ASSURANCES -->
		<tr id="assurances_XXXX" class="<%=rowStyle%> rowGroupes" onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'rowHighligthed')" onMouseOut="jscss('swap', this, 'rowHighligthed', '<%=rowStyle%>')">
			<TD class="mtd" style="width:6px;border-bottom:solid 1px #226194;border-right:none">
				<img id="imgExpand_XXXX"
					class="imgExpand" 
					src="<%=request.getContextPath()%>/images/icon-expand.gif" 
					title="" 
					border="0"
					onMouseOver="this.style.cursor='hand';"
					onMouseOut="this.style.cursor='pointer';"
					width="12px"
					height="12px"
					>
			</TD>
			<TD class="mtd" style="width:6px;border-bottom:solid 1px #226194;border-right:none;">
				<input type="checkbox" checked="checked" id="checkboxGroupe_XXXX_"/>
			</TD>	
			<!--DESCRIPTION -->
			<TD class="mtd cellGroupe" colspan="3" align="left" style="border-bottom:solid 1px #226194;border-right:none" style="font-weight:bold;height:42px">
				Toutes	(<%=sizeTotal%> caisses)		
			</TD>
			<%
			boolean inProgressAllAssureurs = false;
			for (CaisseMaladie cm : listAllAssureurs) {
				if(idTiersCMInProgress.contains(cm.getIdTiersCaisse())){
					inProgressAllAssureurs = true;
					break;
				}
			}
			if(!inProgressAllAssureurs){
			%>
			<!-- BOUTON SEDEX RP -->
			<TD class="mtd" align="center" style="border-bottom:solid 1px #226194;border-right:none">
				<button id="buttonSEDEXRPGroupe_XXXX_" class="buttonSEDEXRP" type="button" style="width:110px;height:24px" <% if (!hasRightOnSEDEX) { %>disabled="disabled"<% } %>>	
					<b>SEDEX RP</b>
				</button>
			</TD>								
			<!-- BOUTON SIMULATION -->
			<TD class="mtd" align="center" style="border-bottom:solid 1px #226194;border-right:none">
				<button id="buttonSimulationRPGroupe_XXXX_" class="buttonSimulationRP" type="button" style="width:110px;height:24px" <% if (!hasRightOnSimulationRP) { %>disabled="disabled"<% } %>>	
					<b>Simulation RP</b>
				</button>
			</TD>
			<!-- BOUTON SEDEX CO -->
			<TD class="mtd" align="center" style="border-bottom:solid 1px #226194;border-right:none">
				<button id="buttonSEDEXCOGroupe_XXXX_" class="buttonSEDEXCO" type="button" style="width:110px;height:24px" <% if (!hasRightOnSEDEX) { %>disabled="disabled"<% } %>>	
					<b>SEDEX CO</b>
				</button>
			</TD>								
			<!-- BOUTON SIMULATION CO-->
			<TD class="mtd" align="center" style="border-bottom:solid 1px #226194;border-right:none">
				<button id="buttonSimulationCOGroupe_XXXX_" class="buttonSimulationCO" type="button" style="width:110px;height:24px" <% if (!hasRightOnSimulationRP) { %>disabled="disabled"<% } %>>	
					<b>Simulation CO</b>
				</button>
			</TD>
			<!-- BOUTON ANNONCER -->
<!-- 			<TD class="mtd" align="center" style="border-bottom:solid 1px #226194;border-right:none"> -->
<%-- 				<button id="buttonAnnoncerGroupe_XXXX_" class="buttonAnnoncer" type="button" style="width:110px;height:24px" <% if (!hasRightOnAnnonce) { %>disabled="disabled"<% } %>>					 --%>
<!-- 					<b>Annonce</b> -->
<!-- 				</button>	 -->
<!-- 			</TD> -->
			<%}else{%>
			<TD align="center" colspan="4" style="border-bottom:solid 1px #226194;border-right:none">
				<img src="<%=request.getContextPath()%>/images/amal/loading.gif" 
					title="Traitement en cours" 
					border="0"
					width="220px"
					height="18px"
					>
			</TD>
			<%}%>
			<!-- FIN TR TOUTES LES ASSURANCES -->
		</TR>
				<!-- DEBUT TR CAISSES (TOUTES)-->
				<% 										
					if (orderByNumero) {
						Collections.sort(listAllAssureurs,CaisseMaladie.CaisseMaladieComparator.getComparator(CaisseMaladie.CaisseMaladieComparator.ID_SORT));	
					} else {
						Collections.sort(listAllAssureurs,CaisseMaladie.CaisseMaladieComparator.getComparator(CaisseMaladie.CaisseMaladieComparator.NAME_SORT));
					}
				
					for (CaisseMaladie cm : listAllAssureurs) {
						String numCaisse = cm.getNumCaisse();
						String nomCaisse = cm.getNomCaisse();
						%>
						<tr id="assurance_XXXX_<%=cm.getIdTiersCaisse()%>" style="display: none;" class="<%=rowStyle%> rowCaisses" onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'rowHighligthed')" onMouseOut="jscss('swap', this, 'rowHighligthed', '<%=rowStyle%>')">
							<TD align="center"></TD>
							<TD style="padding: 3px 10px">
								<input type="checkbox" checked="checked" id="checkboxCaisse_XXXX_<%=cm.getIdTiersCaisse()%>"/>
							</TD>
							<% 
							//Si il y a une date de fin à la caisse,  on différencie la ligne
							String classInactive = "padding-left: 10px";
					   		if ("1".equals(cm.getInactif())) { 
					   			classInactive = "padding-left: 10px;color:red";
					   		}					
					   		%>
							<TD style="<%=classInactive%>" colspan="3"><%=!orderByNumero?nomCaisse+" - "+numCaisse:numCaisse+" - "+nomCaisse%></TD>
							<%
							if(!idTiersCMInProgress.contains(cm.getIdTiersCaisse())){
							%>
							<TD align="center">
								<button id="buttonSEDEXRPEnfant_XXXX_<%=cm.getIdTiersCaisse()%>_" class="buttonSEDEXRP" type="button" style="font-size: 10px" <% if (!hasRightOnSEDEX) { %>disabled="disabled"<% } %>>	
									<b>SEDEX RP</b>
								</button>
							</TD>							
							<TD align="center">
								<button id="buttonSimulationRPEnfant_XXXX_<%=cm.getIdTiersCaisse()%>_" class="buttonSimulationRP" type="button" style="font-size: 10px" <% if (!hasRightOnSimulationRP) { %>disabled="disabled"<% } %>>	
									<b>Simulation RP</b>
								</button>
							</TD>
							<TD align="center">
								<button id="buttonSEDEXCOEnfant_XXXX_<%=cm.getIdTiersCaisse()%>_" class="buttonSEDEXCO" type="button" style="font-size: 10px" <% if (!hasRightOnSEDEX) { %>disabled="disabled"<% } %>>	
									<b>SEDEX CO</b>
								</button>
							</TD>
							<TD align="center">
								<button id="buttonSimulationCOEnfant_XXXX_<%=cm.getIdTiersCaisse()%>_" class="buttonSimulationCO" type="button" style="font-size: 10px" <% if (!hasRightOnSimulationRP) { %>disabled="disabled"<% } %>>	
									<b>Simulation CO</b>
								</button>
							</TD>
<!-- 							<TD align="center"> -->
<%-- 								<button id="buttonAnnoncerEnfant_XXXX_<%=cm.getIdTiersCaisse()%>_" class="buttonAnnoncer" type="button" style="font-size: 10px" <% if (!hasRightOnAnnonce) { %>disabled="disabled"<% } %>>	 --%>
<!-- 									<b>Annoncer</b> -->
<!-- 								</button> -->
<!-- 							</TD> -->
							<%}else{%>
							<TD align="center" colspan="4">
								<img src="<%=request.getContextPath()%>/images/amal/loading.gif" 
									title="Traitement en cours" 
									border="0"
									width="220px"
									height="18px"
									>
							</TD>
							<%}%>
 						</TR>
				<% } %>
				<!-- FIN TR CAISSES (TOUTES)-->			
			<%	
			//Simulation d'une nouvelle ligne
			condition = condition?false:true;
			i++;
			%>
		<% //include file="/theme/list/lineStyle.jspf" %>
		<%}%>
		<% 
		if (key.equals("0000")) {
				ArrayList<CaisseMaladie> arrayCaisseSansGroupe = mapGroupesAssureurs.get("0000");
				int sizeSansGroupe = arrayCaisseSansGroupe.size();
				
				if (condition) {
					rowStyle = "row";
				} else {
					rowStyle = "rowOdd";
				}
				%>
				<tr id="assurances_0000" class="<%=rowStyle%> rowGroupes" onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'rowHighligthed')" onMouseOut="jscss('swap', this, 'rowHighligthed', '<%=rowStyle%>')">
					<!-- TR SANS GROUPE DEFINI -->
					<TD class="mtd" style="width:6px;border-bottom:solid 1px #226194;border-right:none">
						<img id="imgExpand_0000"
							class="imgExpand" 
							src="<%=request.getContextPath()%>/images/icon-expand.gif" 
							title="" 
							border="0"
							onMouseOver="this.style.cursor='hand';"
							onMouseOut="this.style.cursor='pointer';"
							width="12px"
							height="12px"
							>
					</TD>
					<TD class="mtd" style="width:6px;border-bottom:solid 1px #226194;border-right:none;">
						<input type="checkbox" checked="checked" id="checkboxGroupe_0000_"/>
					</TD>	
					<!--DESCRIPTION -->
					<TD class="mtd cellGroupe" colspan="3" align="left" style="border-bottom:solid 1px #226194;border-right:none" style="font-weight:bold;height:42px">
						Sans groupe	(<%=sizeSansGroupe%> caisses)		
					</TD>
					<%
					boolean inProgressSansGroupe = false;
					for (CaisseMaladie cm : arrayCaisseSansGroupe) {
						if(idTiersCMInProgress.contains(cm.getIdTiersCaisse())){
							inProgressSansGroupe = true;
							break;
						}
					}
					if(!inProgressSansGroupe){
					%>
					<!-- BOUTON SEDEX RP -->
					<TD class="mtd" align="center" style="border-bottom:solid 1px #226194;border-right:none">
						<button id="buttonSEDEXRPGroupe_0000_" class="buttonSEDEXRP" type="button" style="width:110px;height:24px" <% if (!hasRightOnSEDEX) { %>disabled="disabled"<% } %>>				
							<b>SEDEX RP</b>
						</button>
					</TD>								
					<!-- BOUTON SIMULATION -->
					<TD class="mtd" align="center" style="border-bottom:solid 1px #226194;border-right:none">
						<button id="buttonSimulationRPGroupe_0000_" class="buttonSimulationRP" type="button" style="width:110px;height:24px" <% if (!hasRightOnSimulationRP) { %>disabled="disabled"<% } %>>				
							<b>Simulation RP</b>
						</button>
					</TD>
					<!-- BOUTON SEDEX CO -->
					<TD class="mtd" align="center" style="border-bottom:solid 1px #226194;border-right:none">
						<button id="buttonSEDEXCOGroupe_0000_" class="buttonSEDEXCO" type="button" style="width:110px;height:24px" <% if (!hasRightOnSEDEX) { %>disabled="disabled"<% } %>>				
							<b>SEDEX CO</b>
						</button>
					</TD>								
					<!-- BOUTON SIMULATION CO -->
					<TD class="mtd" align="center" style="border-bottom:solid 1px #226194;border-right:none">
						<button id="buttonSimulationCOGroupe_0000_" class="buttonSimulationCO" type="button" style="width:110px;height:24px" <% if (!hasRightOnSimulationRP) { %>disabled="disabled"<% } %>>				
							<b>Simulation CO</b>
						</button>
					</TD>
<!-- 					BOUTON ANNONCER -->
<!-- 					<TD class="mtd" align="center" style="border-bottom:solid 1px #226194;border-right:none"> -->
<%-- 							<button id="buttonAnnoncerGroupe_0000_" class="buttonAnnoncer" type="button" style="width:110px;height:24px" <% if (!hasRightOnAnnonce) { %>disabled="disabled"<% } %>> --%>
<!-- 								<b>Annonce</b> -->
<!-- 							</button>			 -->
<!-- 					</TD> -->
					<%}else{%>
					<TD align="center" colspan="4" style="border-bottom:solid 1px #226194;border-right:none">
						<img src="<%=request.getContextPath()%>/images/amal/loading.gif" 
							title="Traitement en cours" 
							border="0"
							width="220px"
							height="18px"
							>
					</TD>
					<%}%>
					<!-- FIN TR SANS GROUPE DEFINI -->
				</TR>
				<!-- DEBUT TR CAISSES (SANS GROUPE)-->
				<% 									
				if (orderByNumero) {
					Collections.sort(arrayCaisseSansGroupe,CaisseMaladie.CaisseMaladieComparator.getComparator(CaisseMaladie.CaisseMaladieComparator.ID_SORT));	 
				} else {
					Collections.sort(arrayCaisseSansGroupe,CaisseMaladie.CaisseMaladieComparator.getComparator(CaisseMaladie.CaisseMaladieComparator.NAME_SORT));	
				}				
				
				for (CaisseMaladie cm : arrayCaisseSansGroupe) {
					String numCaisse = cm.getNumCaisse();
					String nomCaisse = cm.getNomCaisse();
					
					if (condition) {
						rowStyle = "row";
					} else {
						rowStyle = "rowOdd";
					}
				%>
				<tr id="assurance_0000_<%=cm.getIdTiersCaisse()%>" style="display: none;" class="<%=rowStyle%> rowCaisses" onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'rowHighligthed')" onMouseOut="jscss('swap', this, 'rowHighligthed', '<%=rowStyle%>')">
					<TD align="center"></TD>
					<TD style="padding: 3px 10px">
						<input type="checkbox" checked="checked" id="checkboxCaisse_0000_<%=cm.getIdTiersCaisse()%>"/>
					</TD>					
					<% 
					//Si il y a une date de fin à la caisse,  on différencie la ligne
					String classInactive = "padding-left: 10px";
			   		if ("1".equals(cm.getInactif())) { 
			   			classInactive = "padding-left: 10px;color:red";
			   		}					
			   		%>
					<TD style="<%=classInactive%>" colspan="3"><%=!orderByNumero?nomCaisse+" - "+numCaisse:numCaisse+" - "+nomCaisse%></TD>
					<%
					if(!idTiersCMInProgress.contains(cm.getIdTiersCaisse())){
					%>		
					<TD align="center">
						<button id="buttonSEDEXRPEnfant_0000_<%=cm.getIdTiersCaisse()%>_" class="buttonSEDEXRP" type="button" style="font-size: 10px" <% if (!hasRightOnSEDEX) { %>disabled="disabled"<% } %>>	
							<b>SEDEX RP</b>
						</button>
					</TD>							
					<TD align="center">
						<button id="buttonSimulationRPEnfant_0000_<%=cm.getIdTiersCaisse()%>_" class="buttonSimulationRP" type="button" style="font-size: 10px" <% if (!hasRightOnSimulationRP) { %>disabled="disabled"<% } %>>	
							<b>Simulation RP</b>
						</button>
					</TD>
					<TD align="center">
						<button id="buttonSEDEXCOEnfant_0000_<%=cm.getIdTiersCaisse()%>_" class="buttonSEDEXCO" type="button" style="font-size: 10px" <% if (!hasRightOnSEDEX) { %>disabled="disabled"<% } %>>	
							<b>SEDEX CO</b>
						</button>
					</TD>							
					<TD align="center">
						<button id="buttonSimulationCOEnfant_0000_<%=cm.getIdTiersCaisse()%>_" class="buttonSimulationCO" type="button" style="font-size: 10px" <% if (!hasRightOnSimulationRP) { %>disabled="disabled"<% } %>>	
							<b>Simulation CO</b>
						</button>
					</TD>
<!-- 					<TD align="center"> -->
<%-- 						<button id="buttonAnnoncerEnfant_0000_<%=cm.getIdTiersCaisse()%>_" class="buttonAnnoncer" type="button" style="font-size: 10px" <% if (!hasRightOnAnnonce) { %>disabled="disabled"<% } %>>	 --%>
<!-- 							<b>Annoncer</b> -->
<!-- 						</button>					 -->
<!-- 					</TD> -->
					<%}else{%>
					<TD align="center" colspan="4">
						<img src="<%=request.getContextPath()%>/images/amal/loading.gif" 
							title="Traitement en cours" 
							border="0"
							width="220px"
							height="18px"
							>
					</TD>
					<%}%>
				</TR>								
				<%				
				} //FIN for (String assurance : arrayCaisseSansGroupe)
				%>
				<!-- FIN TR CAISSES (SANS GROUPE)-->
				<%
		} else { 
			ArrayList<CaisseMaladie> arrayCaissesGroupes = mapGroupesAssureurs.get(key);
			int sizeGroupe = arrayCaissesGroupes.size();	
			String nomGroupe = arrayCaissesGroupes.get(0).getNomGroupe();
			String numGroupe = arrayCaissesGroupes.get(0).getNumGroupe();
			%>
			<tr id="assurances_<%=arrayCaissesGroupes.get(0).getIdTiersGroupe()%>" class="<%=rowStyle%> rowGroupes" onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'rowHighligthed')" onMouseOut="jscss('swap', this, 'rowHighligthed', '<%=rowStyle%>')">
			<!-- TR ASSURANCES PAR GROUPE -->
			<!-- BOUTON EXPAND -->
			<TD class="mtd" style="width:6px;border-bottom:solid 1px #226194;border-right:none">
				<img id="imgExpand_<%=arrayCaissesGroupes.get(0).getIdTiersGroupe()%>"
					class="imgExpand" 
					src="<%=request.getContextPath()%>/images/icon-expand.gif" 
					title="" 
					border="0"
					onMouseOver="this.style.cursor='hand';"
					onMouseOut="this.style.cursor='pointer';"
					width="12px"
					height="12px"
					>
			</TD>
			<!--DESCRIPTION -->
			<TD class="mtd" style="width:6px;border-bottom:solid 1px #226194;border-right:none;">
				<input type="checkbox" checked="checked" id="checkboxGroupe_<%=arrayCaissesGroupes.get(0).getIdTiersGroupe()%>_"/>
			</TD>
			<TD class="mtd cellGroupe" colspan="3" align="left" style="border-bottom:solid 1px #226194;border-right:none" style="font-weight:bold;height:42px">
				<%=numGroupe%> - <%=nomGroupe %> (<%=sizeGroupe%> caisses)			
			</TD>
			<%
			boolean inProgress = false;
			for (CaisseMaladie cm : arrayCaissesGroupes) {
				if(idTiersCMInProgress.contains(cm.getIdTiersCaisse())){
					inProgress = true;
					break;
				}
			}
			if(!inProgress){
			%>
			<!-- BOUTON SEDEX RP -->
			<TD class="mtd" align="center" style="border-bottom:solid 1px #226194;border-right:none">
				<button id="buttonSEDEXRPGroupe_<%=arrayCaissesGroupes.get(0).getIdTiersGroupe()%>_" class="buttonSEDEXRP" type="button" style="width:110px;height:24px" <% if (!hasRightOnSEDEX) { %>disabled="disabled"<% } %>>
					<b>SEDEX RP</b>
				</button>
			</TD>								
			<!-- BOUTON SIMULATION -->
			<TD class="mtd" align="center" style="border-bottom:solid 1px #226194;border-right:none">
				<button id="buttonSimulationRPGroupe_<%=arrayCaissesGroupes.get(0).getIdTiersGroupe()%>_" class="buttonSimulationRP" type="button" style="width:110px;height:24px" <% if (!hasRightOnSimulationRP) { %>disabled="disabled"<% } %>>
					<b>Simulation RP</b>
				</button>
			</TD>
			<!-- BOUTON SEDEX CO -->
			<TD class="mtd" align="center" style="border-bottom:solid 1px #226194;border-right:none">
				<button id="buttonSEDEXCOGroupe_<%=arrayCaissesGroupes.get(0).getIdTiersGroupe()%>_" class="buttonSEDEXCO" type="button" style="width:110px;height:24px" <% if (!hasRightOnSEDEX) { %>disabled="disabled"<% } %>>
					<b>SEDEX CO</b>
				</button>
			</TD>								
			<!-- BOUTON SIMULATION CO -->
			<TD class="mtd" align="center" style="border-bottom:solid 1px #226194;border-right:none">
				<button id="buttonSimulationCOGroupe_<%=arrayCaissesGroupes.get(0).getIdTiersGroupe()%>_" class="buttonSimulationCO" type="button" style="width:110px;height:24px" <% if (!hasRightOnSimulationRP) { %>disabled="disabled"<% } %>>
					<b>Simulation CO</b>
				</button>
			</TD>
<!-- 			<!-- BOUTON ANNONCER -->
<!-- 			<TD class="mtd" align="center" style="border-bottom:solid 1px #226194;border-right:none"> -->
<%-- 					<button id="buttonAnnoncerGroupe_<%=arrayCaissesGroupes.get(0).getIdTiersGroupe()%>_" class="buttonAnnoncer" type="button" style="width:110px;height:24px" <% if (!hasRightOnAnnonce) { %>disabled="disabled"<% } %>> --%>
<!-- 						<b>Annonce</b> -->
<!-- 					</button>			 -->
<!-- 			</TD> -->
			<%}else{%>
			<TD align="center" colspan="4" style="border-bottom:solid 1px #226194;border-right:none">
				<img src="<%=request.getContextPath()%>/images/amal/loading.gif" 
					title="Traitement en cours" 
					border="0"
					width="220px"
					height="18px"
					>
			</TD>
			<%}%>
			</TR>
			<!-- FIN TR ASSURANCES PAR GROUPE -->
			<!-- DEBUT TR CAISSES (GROUPE)-->
				<% 			
				if (orderByNumero) {
					Collections.sort(arrayCaissesGroupes,CaisseMaladie.CaisseMaladieComparator.getComparator(CaisseMaladie.CaisseMaladieComparator.ID_SORT));	 
				} else {
					Collections.sort(arrayCaissesGroupes,CaisseMaladie.CaisseMaladieComparator.getComparator(CaisseMaladie.CaisseMaladieComparator.NAME_SORT));	
				}				
				
				for (CaisseMaladie cm : arrayCaissesGroupes) {
					String numCaisse = cm.getNumCaisse();
					String nomCaisse = cm.getNomCaisse();					
					if (condition) {
						rowStyle = "row";
					} else {
						rowStyle = "rowOdd";
					}
				%>
				<tr id="assurance_<%=cm.getIdTiersGroupe()%>_<%=cm.getIdTiersCaisse()%>" style="display: none;" class="<%=rowStyle%> rowCaisses" onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'rowHighligthed')" onMouseOut="jscss('swap', this, 'rowHighligthed', '<%=rowStyle%>')">
					<TD align="center"></TD>
					<TD style="padding: 3px 10px">
						<input type="checkbox" checked="checked" id="checkboxCaisse_<%=cm.getIdTiersGroupe()%>_<%=cm.getIdTiersCaisse()%>"/>
					</TD>					
					<% 
					//Si il y a une date de fin à la caisse,  on différencie la ligne
					String classInactive = "padding-left: 10px";
			   		if ("1".equals(cm.getInactif())) { 
			   			classInactive = "padding-left: 10px;color:red";
			   		}
			   		String dateFin = "";
			   		boolean isDateFinGroupe = false;
			   		if (!JadeStringUtil.isBlankOrZero(cm.getDateFinCaisse())) { 
			   			classInactive = "padding-left: 10px;";
			   			JADate d_dateFin = new JADate(cm.getDateFinCaisse().substring(6,8)+"."+cm.getDateFinCaisse().substring(4,6)+"."+cm.getDateFinCaisse().substring(0,4));
						dateFin = "(départ : "+d_dateFin.getDay()+"."+d_dateFin.getMonth()+"."+d_dateFin.getYear()+")";						
						isDateFinGroupe = true;
			   		}
			   		%>
			   		<TD style="<%=classInactive%>" colspan="3"><%=!orderByNumero?nomCaisse+" - "+numCaisse:numCaisse+" - "+nomCaisse%>&nbsp;<%=isDateFinGroupe?dateFin:""%></TD>					
					<%
					if(!idTiersCMInProgress.contains(cm.getIdTiersCaisse())){
					%>	
					<TD align="center">
						<button id="buttonSEDEXRPEnfant_<%=cm.getIdTiersGroupe()%>_<%=cm.getIdTiersCaisse()%>_" class="buttonSEDEXRP" type="button" style="font-size: 10px" <% if (!hasRightOnSEDEX) { %>disabled="disabled"<% } %>>	
							<b>SEDEX RP</b>
						</button>
					</TD>							
					<TD align="center">
						<button id="buttonSimulationRPEnfant_<%=cm.getIdTiersGroupe()%>_<%=cm.getIdTiersCaisse()%>_" class="buttonSimulationRP" type="button" style="font-size: 10px" <% if (!hasRightOnSimulationRP) { %>disabled="disabled"<% } %>>	
							<b>Simulation RP</b>
						</button>
					</TD>
					<TD align="center">
						<button id="buttonSEDEXCOEnfant_<%=cm.getIdTiersGroupe()%>_<%=cm.getIdTiersCaisse()%>_" class="buttonSEDEXCO" type="button" style="font-size: 10px" <% if (!hasRightOnSEDEX) { %>disabled="disabled"<% } %>>	
							<b>SEDEX CO</b>
						</button>
					</TD>							
					<TD align="center">
						<button id="buttonSimulationCOEnfant_<%=cm.getIdTiersGroupe()%>_<%=cm.getIdTiersCaisse()%>_" class="buttonSimulationCO" type="button" style="font-size: 10px" <% if (!hasRightOnSimulationRP) { %>disabled="disabled"<% } %>>	
							<b>Simulation CO</b>
						</button>
					</TD>
<!-- 					<TD align="center"> -->
<%-- 						<button id="buttonAnnoncerEnfant_<%=cm.getIdTiersGroupe()%>_<%=cm.getIdTiersCaisse()%>_" class="buttonAnnoncer" type="button" style="font-size: 10px" <% if (!hasRightOnAnnonce) { %>disabled="disabled"<% } %>>	 --%>
<!-- 							<b>Annoncer</b> -->
<!-- 						</button>					 -->
<!-- 					</TD> -->
					<%}else{%>
					<TD align="center" colspan="4">
						<img src="<%=request.getContextPath()%>/images/amal/loading.gif" 
							title="Traitement en cours" 
							border="0"
							width="220px"
							height="18px"
							>
					</TD>
					<%}%>
				</TR>
				<!-- FIN TR CAISSES (GROUPE)-->
				<%				
				} //FIN for (String assurance : arrayCaisseSansGroupe) {
	 	} %>
		<%-- /tpl:insert --%>
	 <% i++; %>
	 <!-- FIN DE LA BOUCLE PRINCIPALE for (Iterator<String> it = mapGroupes.keySet().iterator(); it.hasNext();)   -->
	 <!--  "}" dans l'include ci-dessous -->
	     </tr>
<% } %>
	<%// include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<div id="dialogSimuAnnonce" title="Nouvelle simulation / annonce" style="display: none;">
	<table>
		<tr>
			<td width="180px">Groupe  / Caisse</td>
			<td class="descriptionCaisse" style="font-weight: bold;">				
			</td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td width="100px">Année</td>
			<td>
				<input type="radio" id="typeSelectionYearAll" name="typeSelectionYear" checked="checked" value="all"/>
				Toutes
			</td>
		</tr>
		<tr>
			<td></td>
			<td class="trSelectionYear">
				<input type="radio" id="typeSelectionYearOne" name="typeSelectionYear" value="one"/>
				Sélection 
				<input id="anneeAnnonceSimulation" type="text" size="4" maxlength="4" disabled="disabled" />
			</td>
		</tr>
		<tr class="trSedexRP">
			<td width="100px">Messages <input type="checkbox" id="msgSelectionNone" name="msgSelectionNone" value="yes"/></td>
			<td>
				<input type="radio" id="msgSelectionDecreeStop" class="msgTypeSelectionSedexRP" name="msgTypeSelectionSedexRP" checked="checked" value="5201"/>
				Nouvelles décisions / Interruptions
			</td>
		</tr>
		<tr class="trSedexRP">
			<td></td>
			<td>
				<input type="radio" id="msgSelectionInsuranceQuery" class="msgTypeSelectionSedexRP" name="msgTypeSelectionSedexRP" value="5202"/>
				Demande du rapport d'assurance
			</td>
		</tr>
		<tr class="trSedexRP">
			<td></td>
			<td>
				<input type="radio" id="msgSelectionDecreeInventory" class="msgTypeSelectionSedexRP" name="msgTypeSelectionSedexRP" value="5203"/>
				Etat des décisions
			</td>
		</tr>		
	</table>
</div>
<div id="dialogSimuAnnonceCO" title="Nouvelle simulation CO/ annonce CO" style="display: none;">
	<table>
		<tr>
			<td width="180px">Groupe  / Caisse</td>
			<td class="descriptionCaisse" style="font-weight: bold;">				
			</td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td width="100px">Messages</td>
			<td>
				<input type="radio" id="msgSelectionListOfGuaranteedAssumptions" class="msgTypeSelectionSedexCO" name="msgTypeSelectionSedexCO" value="5222"/>
				Personnes ne devant pas être poursuivies
			</td>
		</tr>			
	</table>
</div>