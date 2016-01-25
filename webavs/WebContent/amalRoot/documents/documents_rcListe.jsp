<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.amal.vb.documents.AMDocumentsListViewBean"%>
<%@page import="globaz.amal.vb.documents.AMDocumentsViewBean"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme"%>
<%@page import="ch.globaz.amal.business.constantes.IAMActions"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Arrays"%>
<%@page import="globaz.globall.parameters.FWParametersCode"%>
<%@page import="java.util.Iterator"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>

<%
	AMDocumentsListViewBean viewBean=(AMDocumentsListViewBean)request.getAttribute("viewBean");
	size=viewBean.getSize();
	String currentTypeJob =viewBean.getSearchModel().getForTypeJob(); 
	//detailLink = "amal?userAction=amal.contribuable.contribuable.afficher&selectedId=";
	//menuName = "amal-menuprincipal";	 
	String deleteJobAction = IAMActions.ACTION_DOCUMENTS+".deleteJob";
	boolean hasRightDelete = objSession.hasRight(deleteJobAction, FWSecureConstants.REMOVE);
	boolean hasRightUpdate = objSession.hasRight(deleteJobAction, FWSecureConstants.UPDATE);
	boolean hasRightNew = objSession.hasRight(deleteJobAction, FWSecureConstants.ADD);
%>


<%-- tpl:insert attribute="zoneScripts" --%>
<script type="text/javascript">

//---------------------------------------------------------------------------------------------------
//Object javascript pour enregistrer les codes systèmes AMMODELES
//---------------------------------------------------------------------------------------------------
function CSDocument(_cu,_libelleShort,_libelle) {
	this.cu = _cu;
	this.libelleShort = _libelleShort;
	this.libelle = _libelle;
}
//nom à problèmes (multiple prénom ou nom)
var allCSDocument = new Array;
<%
for (Iterator it = viewBean.getListeDocuments().iterator(); it.hasNext();) {
	FWParametersCode code = (FWParametersCode) it.next();
	String codeSysteme = code.getId(); // Code système
	String codeUtilisateur = code.getCodeUtilisateur(objSession.getIdLangue()).getCodeUtilisateur(); // CU (1 - )
	String codeLibelleShort = code.getLibelle().trim(); // Libelle Short
	String codeLibelle = code.getCodeUtilisateur(objSession.getIdLangue()).getLibelle().trim(); // Libelle Long
%>
allCSDocument['<%=codeSysteme%>']=new CSDocument('<%=codeUtilisateur%>','<%=codeLibelleShort%>','<%=codeLibelle%>');
<%
}
%>
//---------------------------------------------------------------------------------------------------
//Fin initialisation Object javascript pour enregistrer les codes systèmes AMMODELES
//---------------------------------------------------------------------------------------------------
var s_csTypeJob = <%=currentTypeJob%>;


/**
 * Initialisation et fonctions particulières de gestion du tableau de bord
 */
$(document).ready(function() {
	// ajout des champs nécessaires dans le formulaires
	addFormFields();
	
	// par défaut, disabled toutes les checkbox
	$('table :checkbox').each(function(index){
		var idCheckBox = $(this).attr('id');
		toggleOpacityElement(idCheckBox,0.2,1.0);
	});
	// enregistrement de l'événement click sur les images expand
	$('.imgExpand').click(function(){
		var s_idImgExpand = $(this).attr('id');
		var s_idJob=s_idImgExpand.split("_")[1];
		var $tdParent = $('#'+s_idImgExpand).parent();
		var $rowParent = $tdParent.parent();
		var s_idRow = $rowParent.attr('id');
		// Get the id of the row, if null proceed
		if(s_idRow == null || s_idRow == 'undefined'){
			getDocumentsList(s_idJob);
		}else{
			toggleChildrenLines(s_idJob);
		}
	});
	// enregistrement de l'événement click sur les images calendar
	$('.imgCalendar').click(function(){
		var s_idImgCalendar = $(this).attr('id');
		var s_idJob=s_idImgCalendar.split("_")[1];
		manageCalendarChange(s_idJob);
		
	});
	// enregistrement de l'événement click sur les checkbox
	$('input:checkbox').click(function(){
		var s_idCheckBox = this.id;
		var s_idJob = s_idCheckBox.split("_")[1];
		var s_idCheckBoxPrincipale = "checkboxPrincipale_"+s_idJob+"_";
		if(s_idCheckBox.length>=s_idCheckBoxPrincipale.length){
			if(s_idCheckBox.substring(0,s_idCheckBoxPrincipale.length)==s_idCheckBoxPrincipale){
				toggleChildrenElements(s_idJob);
			}
		}
	});
	// enregistrement des actions sur les combobox
	$('table').on('change','select',function(event){
		var s_idCombo = this.id;
		manageComboBoxEvent(s_idCombo);
	});
	// enregistrement des actions sur les boutons
	$('table').on('click','button',function(event){
		var s_idButton = this.id;
		if(s_idButton.indexOf('Imprimer')>=0){
			managePrintButtonEvent(s_idButton);
		}else if(s_idButton.indexOf('Supprimer')>=0){
			manageDeleteButtonEvent(s_idButton);
		}
	});
	
	// ---------------------------------------------------------------------
	// Initialisation du dialogue de sélection des templates
	// ---------------------------------------------------------------------
	$( '.ui-dialog-titlebar-close' ).remove();
	$('#dialogDateSelection').dialog(
			{
				autoOpen:false,
				buttons:[	{
								text:"Appliquer",
								click: function() {
													callDateGeneration();
													}
							},
							{
								text:"Annuler",
								click: function() {$(this).dialog("close");}
							}
						],
				modal:true,
				closeOnEscape:true,
				draggable : false,
				resizable : false,
				width : 320,
				height : 280
			}
	);
	$('#dialogInProgress').dialog(
			{
				dialogClass: 'alert',
				autoOpen:false,
				modal:true,
				closeOnEscape:true,
				draggable : false,
				resizable : false,
				width : 320,
				height : 240
			}
	);
	


});

/**
 * Ajout des champs nécessaires au formulaire courant
 */
function addFormFields(){
	var s_textToInsert = '';
	if(parent.document.forms[0].elements('selectedJob')==null){
		s_textToInsert +='<input type="hidden" name="selectedJob" value="">';
	}
	if(parent.document.forms[0].elements('selectedStatus')==null){
		s_textToInsert +='<input type="hidden" name="selectedStatus" value="">';
	}
	if(parent.document.forms[0].elements('newStatus')==null){
		s_textToInsert +='<input type="hidden" name="newStatus" value="">';
	}
	if(parent.document.forms[0].elements('expandedJob')==null){
		s_textToInsert +='<input type="hidden" name="expandedJob" value="">';
	}
	if(parent.document.forms[0].elements('userAction')==null){
		s_textToInsert +='<input type="hidden" name="userAction" value="">';
	}
	if(parent.document.forms[0].elements('newStatus')==null){
		s_textToInsert +='<input type="hidden" name="newDate" value="">';
	}
	if(s_textToInsert.length>0){
		parent.$('form').append(s_textToInsert);
	}
}

/**
 * Gestion du click sur le calendrier (changement de date de job)
 */
function manageCalendarChange(currentIdJob){
	// display modal dialog
    parent.document.forms[0].elements('selectedJob').value = currentIdJob;
	$('#dialogDateSelection').dialog('open');
}

/**
 * Génération de la nouvelle date
 */
function callDateGeneration(){
	var s_actionDate = '<%=IAMActions.ACTION_DOCUMENTS+".newJobDate"%>';
	var s_newDate = $('#newDateInput').val();
    $('#dialogDateSelection').dialog('close');
    $('#dialogInProgress').dialog('open');
    parent.document.forms[0].elements('userAction').value = s_actionDate;
    parent.document.forms[0].elements('newDate').value = s_newDate;
	parent.document.forms[0].onsubmit = '';
	parent.document.forms[0].target = '';
	parent.document.forms[0].submit();
}
/**
 * Gestion des événements sur les combo box de status
 */
function manageComboBoxEvent(currentIdCombo){

	var s_actionStatus = '<%=IAMActions.ACTION_DOCUMENTS+".changeJobStatus"%>';
	var s_idJob = currentIdCombo.split("_")[1];
	var s_idComboBoxPrincipale="comboboxPrincipale_"+s_idJob+"_";
	var s_idComboBoxEnfant="comboboxEnfant_"+s_idJob+"_";
   	var s_selectedValue=$('#'+currentIdCombo).val();
	
	if(currentIdCombo.substring(0,s_idComboBoxEnfant.length)==s_idComboBoxEnfant){
    	var s_idStatus = currentIdCombo.split("_")[2];
	    if (window.confirm('Voulez-vous changer le status de ce document ?')){
			// Proceed with status change			
		    parent.document.forms[0].elements('userAction').value = s_actionStatus;
		    parent.document.forms[0].elements('expandedJob').value = s_idJob;
		    parent.document.forms[0].elements('selectedJob').value = s_idJob;
		    parent.document.forms[0].elements('selectedStatus').value = s_idStatus;
		    parent.document.forms[0].elements('newStatus').value = s_selectedValue;
			parent.document.forms[0].onsubmit = '';
			parent.document.forms[0].target = '';
			parent.document.forms[0].submit();
	    }else{
	    	// New Status à 0, ré-initialise la page
			// Proceed with status change			
		    parent.document.forms[0].elements('userAction').value = s_actionStatus;
		    parent.document.forms[0].elements('expandedJob').value = s_idJob;
		    parent.document.forms[0].elements('selectedJob').value = s_idJob;
		    parent.document.forms[0].elements('selectedStatus').value = s_idStatus;
		    parent.document.forms[0].elements('newStatus').value = '';
			parent.document.forms[0].onsubmit = '';
			parent.document.forms[0].target = '';
			parent.document.forms[0].submit();
	    }
	}else if(currentIdCombo.substring(0,s_idComboBoxPrincipale.length)==s_idComboBoxPrincipale){

		var s_idRelatedCheckBox='checkboxPrincipale_'+s_idJob+'_';
		if(s_csTypeJob == <%=IAMCodeSysteme.AMJobType.JOBPROCESS.getValue()%> || $('#'+s_idRelatedCheckBox).prop('checked')){
			if (window.confirm('Voulez-vous changer le status du job '+s_idJob+' ?')){
				// Proceed with status change			
			    parent.document.forms[0].elements('userAction').value = s_actionStatus;
			    parent.document.forms[0].elements('selectedJob').value = s_idJob;
			    parent.document.forms[0].elements('newStatus').value = s_selectedValue;
				parent.document.forms[0].onsubmit = '';
				parent.document.forms[0].target = '';
				parent.document.forms[0].submit();
		    }else{
		    	// New Status à 0, ré-initialise la page
				// Proceed with status change			
			    parent.document.forms[0].elements('userAction').value = s_actionStatus;
			    parent.document.forms[0].elements('selectedJob').value = s_idJob;
			    parent.document.forms[0].elements('newStatus').value = '';
				parent.document.forms[0].onsubmit = '';
				parent.document.forms[0].target = '';
				parent.document.forms[0].submit();
		    }
		}else{
			var s_allStatusId = '';
			var i_nbLines = 0;
			// check the children status (checked) and get the ids
			$('input:checkbox').each(function (iIndex) {
				var s_currentCheckBoxId=this.id;
				var s_checkBoxEnfant = 'checkboxEnfant_'+s_idJob+'_';
				if(s_currentCheckBoxId.substring(0,s_checkBoxEnfant.length)==s_checkBoxEnfant){
					i_nbLines++;
					if($('#'+s_currentCheckBoxId).prop('checked')){
						var s_statusId = s_currentCheckBoxId.split('_')[2];
						if(s_allStatusId.length>0){
							s_allStatusId+=',';	
						}
						s_allStatusId+=s_statusId;
					}
				}
			});
			// We got the status, we can proceed with status change
			if(s_allStatusId.length>0){
				// Job entier
				if(i_nbLines == s_allStatusId.split(',').length){
					if (window.confirm('Voulez-vous changer le status du job '+s_idJob+' ?')){
						// Proceed with status change			
					    parent.document.forms[0].elements('userAction').value = s_actionStatus;
					    parent.document.forms[0].elements('selectedJob').value = s_idJob;
					    parent.document.forms[0].elements('newStatus').value = s_selectedValue;
						parent.document.forms[0].onsubmit = '';
						parent.document.forms[0].target = '';
						parent.document.forms[0].submit();
				    }else{
				    	// New Status à 0, ré-initialise la page
						// Proceed with status change			
					    parent.document.forms[0].elements('userAction').value = s_actionStatus;
					    parent.document.forms[0].elements('selectedJob').value = s_idJob;
					    parent.document.forms[0].elements('newStatus').value = '';
						parent.document.forms[0].onsubmit = '';
						parent.document.forms[0].target = '';
						parent.document.forms[0].submit();
				    }
				}else{
					// Sélection d'éléments
					if (window.confirm('Voulez-vous changer le status des éléments sélectionnés ?')){
						// Proceed with status change			
					    parent.document.forms[0].elements('userAction').value = s_actionStatus;
					    parent.document.forms[0].elements('selectedJob').value = s_idJob;
					    parent.document.forms[0].elements('selectedStatus').value = s_allStatusId;
					    parent.document.forms[0].elements('newStatus').value = s_selectedValue;
						parent.document.forms[0].onsubmit = '';
						parent.document.forms[0].target = '';
						parent.document.forms[0].submit();
				    }else{
				    	// New Status à 0, ré-initialise la page
						// Proceed with status change			
					    parent.document.forms[0].elements('userAction').value = s_actionStatus;
					    parent.document.forms[0].elements('selectedJob').value = s_idJob;
					    parent.document.forms[0].elements('selectedStatus').value = s_allStatusId;
					    parent.document.forms[0].elements('newStatus').value = '';
						parent.document.forms[0].onsubmit = '';
						parent.document.forms[0].target = '';
						parent.document.forms[0].submit();
				    }
				}
			}else{
				alert('Pas d\'élément sélectionné, aucune action ne sera générée');
			}
		}
	}	
}

/**
 * Gestion des événements sur les boutons imprimer
 */
function managePrintButtonEvent(currentIdButton){

	var s_actionImprimer = '<%=IAMActions.ACTION_DOCUMENTS+".launchPrintProcess"%>';
	var s_idButtonImprimerPrincipale = 'buttonImprimerPrincipale_';
	var s_idButtonImprimerEnfant = 'buttonImprimerEnfant_';

	if(currentIdButton.substring(0,s_idButtonImprimerEnfant.length)==s_idButtonImprimerEnfant){

	    if (window.confirm('Voulez-vous imprimer ce document ?')){
			// Proceed with deletion			
			var s_idJob=currentIdButton.split('_')[1];
			var s_idStatus=currentIdButton.split('_')[2];
		    parent.document.forms[0].elements('userAction').value = s_actionImprimer;
		    parent.document.forms[0].elements('expandedJob').value = s_idJob;
		    parent.document.forms[0].elements('selectedJob').value = s_idJob;
		    parent.document.forms[0].elements('selectedStatus').value = s_idStatus;
			parent.document.forms[0].onsubmit = '';
			parent.document.forms[0].target = '';
			parent.document.forms[0].submit();
	    }
	}else if(currentIdButton.substring(0,s_idButtonImprimerPrincipale.length)==s_idButtonImprimerPrincipale){

		var s_idJob=currentIdButton.split('_')[1];

		// Get the status of the main checkbox
		// if checked, print the whole job
		// if not, print the selected childrens
		var s_idRelatedCheckBox='checkboxPrincipale_'+s_idJob+'_';
		if(s_csTypeJob == <%=IAMCodeSysteme.AMJobType.JOBPROCESS.getValue()%> || $('#'+s_idRelatedCheckBox).prop('checked')){
		    if (window.confirm('Voulez-vous imprimer l\'ensemble du job '+s_idJob+' ?')){
				// Proceed with the print process			
			    parent.document.forms[0].elements('userAction').value = s_actionImprimer;
			    parent.document.forms[0].elements('selectedJob').value = s_idJob;
				parent.document.forms[0].onsubmit = '';
				parent.document.forms[0].target = '';
				parent.document.forms[0].submit();
		    }
		}else{
			var s_allStatusId = '';
			var i_nbLines = 0;
			// check the children status (checked) and get the ids
			$('input:checkbox').each(function (iIndex) {
				var s_currentCheckBoxId=this.id;
				var s_checkBoxEnfant = 'checkboxEnfant_'+s_idJob+'_';
				if(s_currentCheckBoxId.substring(0,s_checkBoxEnfant.length)==s_checkBoxEnfant){
					i_nbLines++;
					if($('#'+s_currentCheckBoxId).prop('checked')){
						var s_statusId = s_currentCheckBoxId.split('_')[2];
						if(s_allStatusId.length>0){
							s_allStatusId+=',';	
						}
						s_allStatusId+=s_statusId;
					}
				}
			});
			// We got the status, we can proceed with print process
			if(s_allStatusId.length>0){
				// Job entier
				if(i_nbLines == s_allStatusId.split(',').length){
				    if (window.confirm('Voulez-vous imprimer l\'ensemble du job '+s_idJob+' ?')){
						// Proceed with the print process
					    parent.document.forms[0].elements('userAction').value = s_actionImprimer;
					    parent.document.forms[0].elements('selectedJob').value = s_idJob;
						parent.document.forms[0].onsubmit = '';
						parent.document.forms[0].target = '';
						parent.document.forms[0].submit();
				    }
				}else{
					// Sélection d'éléments
				    if (window.confirm('Voulez-vous imprimer les éléments sélectionnés ?')){
						// Proceed with deletion			
					    parent.document.forms[0].elements('userAction').value = s_actionImprimer;
					    parent.document.forms[0].elements('selectedJob').value = s_idJob;
					    parent.document.forms[0].elements('selectedStatus').value = s_allStatusId;
						parent.document.forms[0].onsubmit = '';
						parent.document.forms[0].target = '';
						parent.document.forms[0].submit();
				    }
				}
			}else{
				alert('Pas d\'élément sélectionné, aucune action ne sera générée');
			}
		}
	}
		
}
/**
 * Gestion des événements sur les boutons supprimer
 */
function manageDeleteButtonEvent(currentIdButton){

	var s_actionSupprimer = '<%=IAMActions.ACTION_DOCUMENTS+".deleteJob"%>';
	var s_idButtonSupprimerPrincipale = 'buttonSupprimerPrincipale_';
	var s_idButtonSupprimerEnfant = 'buttonSupprimerEnfant_';
	
	if(currentIdButton.substring(0,s_idButtonSupprimerEnfant.length)==s_idButtonSupprimerEnfant){
	    if (window.confirm('Voulez-vous réellement supprimer ce document ?')){
			// Proceed with deletion			
			var s_idJob=currentIdButton.split('_')[1];
			var s_idStatus=currentIdButton.split('_')[2];
		    parent.document.forms[0].elements('userAction').value = s_actionSupprimer;
		    parent.document.forms[0].elements('expandedJob').value = s_idJob;
		    parent.document.forms[0].elements('selectedJob').value = s_idJob;
		    parent.document.forms[0].elements('selectedStatus').value = s_idStatus;
			parent.document.forms[0].onsubmit = '';
			parent.document.forms[0].target = '';
			parent.document.forms[0].submit();
	    }
	}else if(currentIdButton.substring(0,s_idButtonSupprimerPrincipale.length)==s_idButtonSupprimerPrincipale){

		var s_idJob=currentIdButton.split('_')[1];

		// Get the status of the main checkbox
		// if checked, delete the job
		// if not, delete the childrens
		var s_idRelatedCheckBox='checkboxPrincipale_'+s_idJob+'_';
		if(s_csTypeJob == <%=IAMCodeSysteme.AMJobType.JOBPROCESS.getValue()%> || $('#'+s_idRelatedCheckBox).prop('checked')){
		    if (window.confirm('Voulez-vous réellement supprimer l\'ensemble du job '+s_idJob+' ?')){
				// Proceed with deletion			
			    parent.document.forms[0].elements('userAction').value = s_actionSupprimer;
			    parent.document.forms[0].elements('selectedJob').value = s_idJob;
				parent.document.forms[0].onsubmit = '';
				parent.document.forms[0].target = '';
				parent.document.forms[0].submit();
		    }
		}else{
			var s_allStatusId = "";
			var i_nbLines = 0;
			// check the children status (checked) and get the ids
			$('input:checkbox').each(function (iIndex) {
				var s_currentCheckBoxId=this.id;
				var s_checkBoxEnfant = "checkboxEnfant_"+s_idJob+"_";
				if(s_currentCheckBoxId.substring(0,s_checkBoxEnfant.length)==s_checkBoxEnfant){
					i_nbLines++;
					if($('#'+s_currentCheckBoxId).prop('checked')){
						var s_statusId = s_currentCheckBoxId.split('_')[2];
						if(s_allStatusId.length>0){
							s_allStatusId+=',';	
						}
						s_allStatusId+=s_statusId;
					}
				}
			});
			// We got the status, we can proceed with the deletion
			if(s_allStatusId.length>0){
				// Job entier
				if(i_nbLines == s_allStatusId.split(',').length){
				    if (window.confirm('Voulez-vous réellement supprimer l\'ensemble du job '+s_idJob+' ?')){
						// Proceed with deletion			
					    parent.document.forms[0].elements('userAction').value = s_actionSupprimer;
					    parent.document.forms[0].elements('selectedJob').value = s_idJob;
						parent.document.forms[0].onsubmit = '';
						parent.document.forms[0].target = '';
						parent.document.forms[0].submit();
				    }
				}else{
					// Sélection d'éléments
				    if (window.confirm('Voulez-vous réellement supprimer les éléments sélectionnés ?')){
						// Proceed with deletion			
					    parent.document.forms[0].elements('userAction').value = s_actionSupprimer;
					    parent.document.forms[0].elements('selectedJob').value = s_idJob;
					    parent.document.forms[0].elements('selectedStatus').value = s_allStatusId;
						parent.document.forms[0].onsubmit = '';
						parent.document.forms[0].target = '';
						parent.document.forms[0].submit();
				    }
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
function toggleChildrenLines(currentIdJob){
	
	// variables de travail
	var s_idImgExpand = 'imgExpand_'+currentIdJob;
	var s_idComboBoxPrincipale = 'comboboxPrincipale_'+currentIdJob+'_';
	var s_ComboBoxIsDisabled = ''+$('#'+s_idComboBoxPrincipale).prop('disabled');
	var s_srcImgExpand = $('#'+s_idImgExpand).attr('src');
	var i_nbLignesEnfants = 0;
	
	// TOGGLE THE CHILDREN
	$('table tr').each(function (iIndex) {
		var s_idLigne=this.id;
		if(s_idLigne!=null && s_idLigne.length>0){
			var s_idLigneChidrenStart = 'ligneEnfant_';
			// check if we find the ligneEnfant and work on it
			if(s_idLigne.length>=s_idLigneChidrenStart.length){
				if(s_idLigne.substring(0,s_idLigneChidrenStart.length)==s_idLigneChidrenStart){
					var s_idJobLine = s_idLigne.split('_')[1];
					if(s_idJobLine == currentIdJob){
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
		// check checkbox principale et tous les enfants
		if(i_nbLignesEnfants>1 && s_ComboBoxIsDisabled=='false'){
			$('input:checkbox').each(function (iIndex) {
				var s_idCheckBox=this.id;
				var s_idCheckBoxStart='checkboxEnfant_'+currentIdJob+'_';
				if(s_idCheckBox.length>=s_idCheckBoxStart.length){
					if(s_idCheckBox.substring(0,s_idCheckBoxStart.length)==s_idCheckBoxStart){
						$('#'+s_idCheckBox).prop('checked',true);
					}
				}
			});
			if($('#checkboxPrincipale_'+currentIdJob+'_').prop('checked')==false){
				toggleChildrenElements(currentIdJob);
			}
			$('#checkboxPrincipale_'+currentIdJob+'_').prop('checked',true);
		}
	
	}else{
		$('#'+s_idImgExpand).attr('src','<%=request.getContextPath()%>/images/icon-collapse.gif');
	}
	// TOGGLE DE LA CHECKBOX PRINCIPALE
	if(i_nbLignesEnfants>1  && s_ComboBoxIsDisabled=='false'){
		toggleOpacityElement('checkboxPrincipale_'+currentIdJob+'_',0.2,1.0);
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
		var s_idCheckBoxStart='checkboxEnfant_'+currentIdJob+'_';
		if(s_idCheckBox.length>=s_idCheckBoxStart.length){
			if(s_idCheckBox.substring(0,s_idCheckBoxStart.length)==s_idCheckBoxStart){
				toggleOpacityElement(s_idCheckBox,0.2,1.0);
				if($('#checkboxPrincipale_'+currentIdJob+'_').prop('checked')==true){
					$('#'+s_idCheckBox).prop('checked',true);
				}
				var s_idStatus = s_idCheckBox.split("_")[2];
				a_idChildrens[i_incrementChildren++] = s_idStatus;
			}
		}
	});
	// Préparation des ids des éléments
	var s_idComboBoxEnfant = 'comboboxEnfant_'+currentIdJob+'_';
	var s_idComboBoxPrincipale = 'comboboxPrincipale_'+currentIdJob+'_';
	var s_ComboBoxIsDisabled = ''+$('#'+s_idComboBoxPrincipale).prop('disabled');

	var s_idButtonImprimerEnfant = 'buttonImprimerEnfant_'+currentIdJob+'_';
	var s_idButtonImprimerPrincipale = 'buttonImprimerPrincipale_'+currentIdJob+'_';
	var s_ButtonImprimerIsDisabled = ''+$('#'+s_idButtonImprimerPrincipale).prop('disabled');
	
	var s_idButtonSupprimerEnfant = 'buttonSupprimerEnfant_'+currentIdJob+'_';
	var s_idButtonSupprimerPrincipale = 'buttonSupprimerPrincipale_'+currentIdJob+'_';
	var s_ButtonSupprimerIsDisabled = ''+$('#'+s_idButtonSupprimerPrincipale).prop('disabled');
	// Parcours de tous les enfants
	for(iCurrentId=0;iCurrentId<a_idChildrens.length;iCurrentId++){
		var b_Sent = false;
		var s_currentIdCombo = s_idComboBoxEnfant+a_idChildrens[iCurrentId]+'_';
		var s_currentIdImprimer = s_idButtonImprimerEnfant+a_idChildrens[iCurrentId]+'_';
		var s_currentIdSupprimer = s_idButtonSupprimerEnfant+a_idChildrens[iCurrentId]+'_';
		// 2) combobox
		if(s_ComboBoxIsDisabled=='false'){
			var s_valeurCombo = $('#'+s_currentIdCombo).val();
			if(s_valeurCombo==null || s_valeurCombo=='undefined'){
				b_Sent=true;	
			}else if(s_valeurCombo=='42002403'){
				b_Sent=true;
			}else{
				toggleOpacityElement(s_currentIdCombo,0.2,1.0);
			}
		}
		if(!b_Sent){
			// 3) bouton imprimer
			if(s_ButtonImprimerIsDisabled=='false'){
				toggleOpacityElement(s_currentIdImprimer,0.2,1.0);
			}
			// 4) bouton supprimer
			if(s_ButtonSupprimerIsDisabled=='false'){
				toggleOpacityElement(s_currentIdSupprimer,0.2,1.0);
			}
		}else{
			var s_currentIdCheckBox='checkboxEnfant_'+currentIdJob+'_'+a_idChildrens[iCurrentId]+'_';
			toggleOpacityElement(s_currentIdCheckBox,0.2,1.0);
		}
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

//------------------------------------------------------------
// AJAX get list documents
//------------------------------------------------------------
function getDocumentsList(currentIdJob) {
	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.models.controleurEnvoi.ControleurEnvoiService',
		serviceMethodName:'getListComplexControleurEnvoiDetail',
		parametres:currentIdJob,
		callBack: callbackGetDocumentsList
	}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}

//------------------------------------------------------------
//AJAX get list documents callback, create lines
//------------------------------------------------------------
function callbackGetDocumentsList(data) {

	var s_textToInsert = '';
	var s_idJob = '';
	var s_idImgExpand = '';
	var $tdParent = null;
	var $rowParent = null;
	var s_classParent = '';
	var a_idElementEnfants = new Array();
	var i_idIncrement = 0;
	var s_lastNoGroupe = '';
	var s_nextNoGroupe = '';
	for(iElement=0; iElement< data.length; iElement++){
		// Récupération de la ligne mère
		if(s_textToInsert.length==0){
			s_idJob = data[iElement].idJob;
			s_idImgExpand = 'imgExpand_'+s_idJob;
			$tdParent = $('#'+s_idImgExpand).parent();
			$rowParent = $tdParent.parent();
			s_classParent= $rowParent.next().attr('class');
			if(s_classParent != null && s_classParent.length>0){
				if(s_classParent=='row'){
					s_classParent='rowOdd';
				}else{
					s_classParent='row';
				}
			}else{
				s_classParent= $rowParent.prev().attr('class');
				if(s_classParent != null && s_classParent.length>0){
					if(s_classParent=='row'){
						s_classParent='rowOdd';
					}else{
						s_classParent='row';
					}
				}else{
					s_classParent='row';
				}
			}
			var i_indexRow = $rowParent.index();
		}
		// identification de la ligne enfant
		s_textToInsert += '<tr class='+s_classParent;
		s_textToInsert += ' id="ligneEnfant_'+s_idJob+'_'+data[iElement].idStatus+'_"';
		s_textToInsert += ' onMouseOver="jscss(\'swap\', this, \''+s_classParent+'\', \'rowHighligthed\')"';
		s_textToInsert += ' onMouseOut="jscss(\'swap\', this, \'rowHighligthed\', \''+s_classParent+'\')"';
		s_textToInsert += '>';

			// colonnes de la ligne
			var s_char = '';
			var s_border0 = 'border-bottom:solid 1px #226194;';
			var s_border1 = 'border-right:none;';
			var s_border2 = 'border-right:none;';
			var s_border3 = '';
			if(iElement<data.length-1){
				s_nextNoGroupe = data[iElement+1].noGroupe;
			}else{
				s_nextNoGroupe = '0';
			}
			if(data[iElement].noGroupe !='0'){
				if(s_nextNoGroupe!=data[iElement].noGroupe && s_lastNoGroupe!=data[iElement].noGroupe){
					// cas 0, groupe unique...
					//s_char='+';
					var s_border1 = 'border-left:solid 1px silver;border-bottom:solid 1px silver;border-right:none;';
					var s_border2 = 'border-bottom:solid 1px silver;border-right:none;';
				}else if(s_nextNoGroupe==data[iElement].noGroupe){
					// cas 1, x éléments dans le groupe
					if(s_lastNoGroupe!=data[iElement].noGroupe){
						//s_char='&lceil;';
						s_border1='border-left:solid 1px silver;border-bottom:solid 1px silver;border-right:none;';
						s_border2='border-right:none';
					}else{
						//s_char='|';
						s_border1='border-right:none;';
						s_border2='border-right:none';
						s_border3='border-left:solid 1px silver;';
					}
				}else if(s_nextNoGroupe!=data[iElement].noGroupe){
					// cas 2, dernier élément du groupe
					//s_char='&lfloor;';
					s_border1='border-bottom:solid 1px silver;border-right:none;';
					s_border2='border-bottom:solid 1px silver;border-right:none;';
					s_border3='border-left:solid 1px silver;';
				}
			}else{
				s_border1='border-left:solid 1px silver;border-bottom:solid 1px silver;border-right:none;';
				s_border2='border-bottom:solid 1px silver;border-right:none;';
			}
			// dernière ligne
			if(iElement==data.length-1){
				s_border1 = s_border1+s_border0;
				s_border2 = s_border2+s_border0;
				s_border3 = s_border3+s_border0;
				s_border0 +="border-right:none;";
			}else{
				s_border0 = 'border-right:none;';
			}

			// colonne vide
			// ------------------------------------------------------------------
			s_textToInsert += '<td class="mtd" style="'+s_border0+'">&nbsp;</td>';

			// Check box + document
			// ------------------------------------------------------------------
			s_textToInsert += '<td class="mtd" style="'+s_border1+'">';
			s_textToInsert += '<input id="checkboxEnfant_'+s_idJob+'_'+data[iElement].idStatus+'_" type="checkbox" checked="yes">'; 
			s_textToInsert += '&nbsp;';
			if(data[iElement].noGroupe=='0'){
				s_textToInsert+= allCSDocument[data[iElement].numModele].libelleShort;
			}else if(s_lastNoGroupe!=data[iElement].noGroupe){
				s_textToInsert+= allCSDocument[data[iElement].numModele].libelleShort;
			}
			s_textToInsert += '</td>';
			a_idElementEnfants[i_idIncrement++]='checkboxEnfant_'+s_idJob+'_'+data[iElement].idStatus+'_';
			
			// Nom Prenom
			// ------------------------------------------------------------------
			s_textToInsert += '<td class="mtd" style="'+s_border3+s_border2+'">';
			s_textToInsert += s_char+'&nbsp;'+data[iElement].nomPrenom;// + ' - ' +data[iElement].noGroupe;
			s_textToInsert += '</td>';
			
			s_lastNoGroupe = data[iElement].noGroupe;
			
			
			// Select ou loading
			// ------------------------------------------------------------------
			s_textToInsert += '<td class="mtd" align="center" style="'+s_border2+'">';
			
				// option
				var s_idComboBoxPrincipale = '#comboboxPrincipale_'+s_idJob+'_';
				var i_optionCount = 0;
				var s_textOption = 	'<select id="comboboxEnfant_'+s_idJob+'_'+data[iElement].idStatus+'_" style="font-size: 10px;width:180px">';
				$('#comboboxPrincipale_'+s_idJob+'_ option').each(function(index){
					i_optionCount++;
					s_textOption += '<option value="'+$(this).val()+'"';
					if($(this).val() == data[iElement].statusEnvoi){
						s_textOption += ' selected="selected"';
					}
					s_textOption +='>'+$(this).text();
					s_textOption +='</option>';
				});
				s_textOption += '</select>';
				if(i_optionCount>0){
					// options-select
					s_textToInsert += s_textOption;
					a_idElementEnfants[i_idIncrement++]='comboboxEnfant_'+s_idJob+'_'+data[iElement].idStatus+'_';
				}else{
					// loading gif
					s_textToInsert += '<img src="<%=request.getContextPath()%>/images/amal/loading.gif"';
					s_textToInsert += ' title="Traitement en cours"';
					s_textToInsert += ' border="0"';
					s_textToInsert += ' width="220px"';
					s_textToInsert += ' height="14px"';
					s_textToInsert += '>';
				}
			
			s_textToInsert += '</td>';
			
			// Bouton imprimer
			// ------------------------------------------------------------------
			s_textToInsert += '<td class="mtd" align="center" style="'+s_border2+'">';
				s_textToInsert += '<button id="buttonImprimerEnfant_'+s_idJob+'_'+data[iElement].idStatus+'_" type="button" style="font-size: 10px;">';
					s_textToInsert += '<img src="<%=request.getContextPath()%>/images/amal/printer1.png"';
					s_textToInsert += ' title="Imprimer"';
					s_textToInsert += ' border="0"';
					s_textToInsert += ' width="10px"';
					s_textToInsert += ' height="10px"';
					s_textToInsert += ' >';
				s_textToInsert += 'Imprimer';			 
				s_textToInsert += '</button>';
			s_textToInsert += '</td>';
			a_idElementEnfants[i_idIncrement++]='buttonImprimerEnfant_'+s_idJob+'_'+data[iElement].idStatus+'_';

			// Bouton supprimer
			// ------------------------------------------------------------------
			s_textToInsert += '<td class="mtd" align="center" style="'+s_border2+'">';
				s_textToInsert += '<button id="buttonSupprimerEnfant_'+s_idJob+'_'+data[iElement].idStatus+'_" type="button" style="font-size: 10px;">';
					s_textToInsert += '<img src="<%=request.getContextPath()%>/images/amal/edit_remove.png"';
					s_textToInsert += ' title="Supprimer"';
					s_textToInsert += ' border="0"';
					s_textToInsert += ' width="10px"';
					s_textToInsert += ' height="10px"';
					s_textToInsert += '	>';
				s_textToInsert += ' Supprimer';
				s_textToInsert += '</button>';
			s_textToInsert += '</td>';
			a_idElementEnfants[i_idIncrement++]='buttonSupprimerEnfant_'+s_idJob+'_'+data[iElement].idStatus+'_';
			
		// fin ligne
		s_textToInsert += '</tr>';
	}
	// Set id ligne principale
	$rowParent.attr('id','lignePrincipale_'+s_idJob+'_');
	$rowParent.after(s_textToInsert);
	// Adapt icon
	$('#'+s_idImgExpand).attr('src','<%=request.getContextPath()%>/images/icon-collapse.gif');
	// toggle opacity elements checkbox, combobox, boutons enfants
	for(iElement = 0; iElement<a_idElementEnfants.length;iElement++){
		toggleOpacityElement(a_idElementEnfants[iElement],0.2,1.0);
	}
	// Activation de la checkbox parente
	var s_idComboBoxPrincipale = 'comboboxPrincipale_'+s_idJob+'_';
	var s_ComboBoxIsDisabled = ''+$('#'+s_idComboBoxPrincipale).prop('disabled');
	if(data.length>1 && s_ComboBoxIsDisabled=='false'){
		toggleOpacityElement('checkboxPrincipale_'+s_idJob+'_',0.2,1.0);
	}
}

</script>

<%-- /tpl:insert --%>

<div id="dialogDateSelection" title="Nouvelle date">
	<table id="tableDate" style="widht=100%">
		<tr id="ligneIntroduction">
			<td colspan="3">&nbsp;</td>
		</tr>
		<tr id="ligneChangement">
			<td></td>
			<td>Nouvelle date :</td>
			<td>
				<input id="newDateInput" type="text"
						data-g-calendar="mandatory:false"/>
			<td>
		</tr>
		<tr>
			<td colspan="3">&nbsp;</td>
		</tr>
	</table>
</div>

<div id="dialogInProgress" title="Opération en cours">
	<table id="tableInProgress" style="widht=100%">
		<tr>
			<td colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="3">Merci de patienter</td>
		</tr>
		<tr>
			<td colspan="3">&nbsp;</td>
		</tr>
	</table>
</div>


<%@ include file="/theme/list/javascripts.jspf" %>	   	    

<%-- tpl:insert attribute="zoneHeaders" --%>

	<%if(!viewBean.getSearchModel().getForTypeJob().equals(IAMCodeSysteme.AMJobType.JOBPROCESS.getValue())){%>
	<TH>&nbsp;</TH>   		
	<%} %>
	<TH colspan="2">Description</TH>   		
	<TH>Status</TH>   		
	<TH>Impression</TH>   		
	<TH>Suppression</TH>
<%-- /tpl:insert --%> 

<%@ include file="/theme/list/tableHeader.jspf" %>

<%-- tpl:insert attribute="zoneCondition" --%>
<%
	AMDocumentsViewBean line = (AMDocumentsViewBean)viewBean.getEntity(i);
	//String detailUrl = "parent.location.href='" + detailLink + line.getContribuableRcListe().getId()+"'";
%>
<%-- /tpl:insert --%>

<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
		<!-- BOUTON EXPAND -->
		<%
			// check si le job de type process
			if(!line.getCurrentJob().getTypeJob().equals(IAMCodeSysteme.AMJobType.JOBPROCESS.getValue())){
		%>
		<TD class="mtd" style="width:6px;border-bottom:solid 1px #226194;border-right:none">
			<img id="imgExpand_<%=line.getCurrentJob().getId()%>"
				class="imgExpand" 
				src="<%=request.getContextPath()%>/images/icon-expand.gif" 
				title="Détails Job <%=line.getCurrentJob().getId()%>" 
				border="0"
				onMouseOver="this.style.cursor='hand';"
				onMouseOut="this.style.cursor='pointer';"
				width="12px"
				height="12px"
				>
		</TD>
		<%}%>
		<!-- CHECKBOX ET DESCRIPTION -->
		<TD class="mtd" colspan="2" align="left" style="border-bottom:solid 1px #226194;border-right:none" style="font-weight:bold;height:42px">
		<%
			// check si le job de type process
			if(!line.getCurrentJob().getTypeJob().equals(IAMCodeSysteme.AMJobType.JOBPROCESS.getValue())){
		%>
			<input id="checkboxPrincipale_<%=line.getCurrentJob().getId()%>_" type="checkbox" checked="yes" />
		<%}%>
			&nbsp;
			<img src="<%=request.getContextPath()%>/images/amal/iconExcel.png"
				onClick="javascript:alert('Not yet implemented')" 
				title="Exporter la liste" 
				border="0"
				onMouseOver="this.style.cursor='hand';"
				onMouseOut="this.style.cursor='pointer';"
				width="12px"
				height="12px"
				>
		<%
			// check si le job de type process
			if(line.getCurrentJob().getTypeJob().equals(IAMCodeSysteme.AMJobType.JOBPROCESS.getValue())
					&&
					!line.getCurrentJob().getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.SENT.getValue())){
		%>
			<img id="imgCalendar_<%=line.getCurrentJob().getId()%>"
				class="imgCalendar" 
				src="<%=request.getContextPath()%>/images/calendar.gif" 
				title="Editer la date <%=line.getCurrentJob().getId()%>" 
				border="0"
				onMouseOver="this.style.cursor='hand';"
				onMouseOut="this.style.cursor='pointer';"
				width="12px"
				height="12px"
				>
		<%}%>
			&nbsp;
			
			<%
			String subTypeJob = "";
			for (Iterator it = viewBean.getListeDocuments().iterator(); it.hasNext();) {
				FWParametersCode code = (FWParametersCode) it.next();
				String codeSysteme = code.getId(); // Code système
				if(line.getCurrentJob().getSubTypeJob().equals(codeSysteme)){
					String codeUtilisateur = code.getCodeUtilisateur(objSession.getIdLangue()).getCodeUtilisateur(); // CU (1 - )
					String codeLibelleShort = code.getLibelle().trim(); // Libelle Short
					String codeLibelle = code.getCodeUtilisateur(objSession.getIdLangue()).getLibelle().trim(); // Libelle Long
					subTypeJob = codeLibelleShort+" - ";
					break;
				}
			}
			%>
			
			<%=line.getCurrentJob().getDateJob()+" - "+subTypeJob+line.getCurrentJob().getDescriptionJob() + " - "+line.getCurrentJob().getUserJob() +" ["+line.getNbDocumentsForCurrentJob()+"]"%>
		</TD>
		<!-- STATUS -->
		<TD class="mtd" align="center" style="border-bottom:solid 1px #226194;border-right:none">
		<%
			// check si le job est in progress
			if(line.getCurrentJob().getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue())){
			// Affiche la bare waiting
		%>
			<img src="<%=request.getContextPath()%>/images/amal/loading.gif" 
				title="Traitement en cours" 
				border="0"
				width="220px"
				height="18px"
				>
		<%}else{ %>
			<%
			if(line.getCurrentJob().getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.SENT.getValue()) || !hasRightUpdate){
			%>
				<select id="comboboxPrincipale_<%=line.getCurrentJob().getId()%>_" disabled="disabled" style="width:220px">
			<%}else{ %>
				<select id="comboboxPrincipale_<%=line.getCurrentJob().getId()%>_" style="width:220px">
			<%} %>
			<%
				// Génération de la combobox
				HashMap<String, String> myOptions = null;
				String csValue="";
				String csLibelle="";
				String csSelected=line.getCurrentJob().getStatusEnvoi();
				myOptions = line.getStatusListJob(line.getCurrentJob());
				Object[] myKeys=myOptions.keySet().toArray();
				Arrays.sort(myKeys);
				for(int iIndex=0; iIndex<myKeys.length;iIndex++){
					csValue = (String) myKeys[iIndex];
					csLibelle = (String) myOptions.get(myKeys[iIndex]);
			%>
				<option value="<%=csValue%>" <%=(csSelected.equals(csValue)?"selected=\"selected\"":"")%>><%=csLibelle%></option>
			<%
				}// fin for option
			%>
		<%} // fin else%>
		
		</TD>
		<!-- BOUTON IMPRIMER -->
		<TD class="mtd" align="center" style="border-bottom:solid 1px #226194;border-right:none">
		<%
		if( ((line.getCurrentJob().getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.AUTOGENERATED.getValue())
				||
				line.getCurrentJob().getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.MANUALGENERATED.getValue())
				||
				line.getCurrentJob().getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.ERROR.getValue())
				||
				line.getCurrentJob().getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.PRINTED.getValue())
				)
				&&
				!line.getCurrentJob().getTypeJob().equals(IAMCodeSysteme.AMJobType.JOBMANUALEDITED.getValue())
				)  && hasRightNew)
		{
		%>
			<button id="buttonImprimerPrincipale_<%=line.getCurrentJob().getId()%>_" type="button" style="width:110px;height:24px">
		<%}else{%>
			<button id="buttonImprimerPrincipale_<%=line.getCurrentJob().getId()%>_" type="button" disabled="disabled" style="width:110px;height:24px">
		<%}%>
				<img src="<%=request.getContextPath()%>/images/amal/printer1.png" 
					title="Imprimer" 
					border="0"
					width="14px"
					height="14px"
					>
				<b>Imprimer</b>
			</button>
		</TD>
		<!-- BOUTON SUPPRIMER -->
			<TD class="mtd" align="center" style="border-bottom:solid 1px #226194;border-right:none">
				<%
					// Activation du bouton supprimer pour les travaux
					// qui n'ont pas encore été envoyé ou en cours
					if(line.getCurrentJob().getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.SENT.getValue())
							||
							line.getCurrentJob().getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue()) || !hasRightDelete){
				%>
					<button id="buttonSupprimerPrincipale_<%=line.getCurrentJob().getId()%>_" disabled="disabled" type="button" style="width:110px;height:24px">
				<%}else{%>
					<button id="buttonSupprimerPrincipale_<%=line.getCurrentJob().getId()%>_" type="button" style="width:110px;height:24px">
				<%} %>
						<img src="<%=request.getContextPath()%>/images/amal/edit_remove.png" 
							title="Supprimer" 
							border="0"
							width="12px"
							height="12px"
							>
						<b>Supprimer</b>
					</button>			
			</TD>
		<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>