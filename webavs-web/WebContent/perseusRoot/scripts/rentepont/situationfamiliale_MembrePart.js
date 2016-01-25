

function EnfantFamillePart(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX = "perseus.rentepont.enfantAjax";
	this.mainContainer=container;
	this.table=this.mainContainer.find(".areaDFDataTable");
	this.detail=this.mainContainer.find(".areaDFDetail");
	this.titleContainer=this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass="areaDFModified";
	
	this.membreId=null;

	// functions
	
	this.afterRetrieve=function($data){
		var xmlData=this.getXMLData($data, 'tiers');
		this.detail.find('#resultAutocompeteEnfant').children().remove();
		this.detail.find('#resultAutocompeteEnfant').contents().remove();
		this.detail.find('#resultAutocompeteEnfant').append(xmlData);
		
		this.detail.find('[name=csFormation]').val($data.find('formation').text()).end()
				.find('#idTiersEnfant').val($data.find('idTiersEnfant').text()).end()
				.find('[name=csGarde]').val($data.find('garde').text()).end()
				.find('[name=csSource]').val($data.find('source').text()).end()
		this.detail.find('#widgetTiersEnfant').hide();
	};
	
	this.formatTable=function() {
	}
	
	this.getParametres=function(){
		return {
			"enfantFamille.simpleEnfantFamille.csFormation":this.detail.find('[name=csFormation]').val(),
			"enfantFamille.simpleEnfantFamille.csGarde":this.detail.find('[name=csGarde]').val(),
			"enfantFamille.simpleEnfantFamille.csSource":this.detail.find('[name=csSource]').val(),
			"enfantFamille.enfant.membreFamille.simpleMembreFamille.idTiers":this.detail.find('#idTiersEnfant').val(),
			"enfantFamille.simpleEnfantFamille.idSituationFamiliale":PAGE_ID_SITUATION_FAMILIALE,
			"idSituationFamiliale":PAGE_ID_SITUATION_FAMILIALE,
			"idRentePont":PAGE_ID_RENTEPONT
		};
	};
				
	this.clearFields=function(){
		this.detail.find('#widgetTiersEnfant,#resultAutocompeteEnfant,#idTiersEnfant,#csFormation,#csGarde,#csSource').val('');
		this.detail.find('#resultAutocompeteEnfant').html('');
	};	
	
	this.getParentViewBean=function(){
		return situationFamiliale;
	};
	
	this.setParentViewBean=function(newViewBean){
		situationFamiliale=newViewBean;
	};	
	
	this.init(
		    function(){	
		    	// cache les boutons ajax
		    	this.stopEdition();
		    	// configure la table
		    	this.colorTableRows();
		    	this.addTableEvent();
		    	//this.detail.show();
		    	//this.getBtnContainer().show();
		    }
		);
	
};

// periodesPart extends AbstractSimpleAJAXTableZone
EnfantFamillePart.prototype=AbstractScalableAJAXTableZone;


function ConjointFamillePart(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX = "perseus.rentepont.conjointAjax";
	this.mainContainer=container;
	this.detail=this.mainContainer.find(".areaDFDetail");
	this.modifiedZoneClass="areaDFModified";
	this.selectedEntityId = PAGE_ID_SITUATION_FAMILIALE;
	this.membreId=null;
	this.currentViewBean = conjointAjaxViewBean

	// functions
	
	this.onUpdate=function($data){
		var idTiersConjoint = $('#idTiers').val();
		//Si il y'a un conjoint défini récupérer l'id conjoint
		if (idTiersConjoint != "") {
			var options = {
				serviceClassName:'ch.globaz.perseus.business.services.models.dossier.DossierService',
				serviceMethodName:'hasDossier',
				parametres:idTiersConjoint,
				callBack:function(hasDossier){
					if (hasDossier) {
						globazNotation.utils.consoleInfo("<div style='height:120px;'><br/>"+AVERTISSEMENT_DOSSIER_CONJOINT + "</div>", AVERTISSEMENT_DOSSIER_CONJOINT_TITRE, true);
					}
				}
			}
			
			globazNotation.readwidget.options = options;
			globazNotation.readwidget.read();
		} 
	};

	this.getParametres=function(){
		return {
			"conjoint.membreFamille.personneEtendue.personneEtendue.numAvsActuel":this.mainContainer.find("#nss").val(),
			"idTiersConjoint":this.mainContainer.find("#idTiers").val(),
			"idRentePont":this.mainContainer.find("#idRentePont").val(),
			"situationFamiliale.simpleSituationFamiliale.csTypeConjoint":this.mainContainer.find('[name="situationFamiliale.simpleSituationFamiliale.csTypeConjoint"]').val(),
			"situationFamiliale.simpleSituationFamiliale.csNiveauFormationRequerant":this.mainContainer.find('[name="situationFamiliale.simpleSituationFamiliale.csNiveauFormationRequerant"]').val(),
			"situationFamiliale.simpleSituationFamiliale.csNiveauFormationConjoint":this.mainContainer.find('[name="situationFamiliale.simpleSituationFamiliale.csNiveauFormationConjoint"]').val()
		};
	};
				
	this.clearFields=function(){

	};	
	
	this.getParentViewBean=function(){
		//return situationFamilialeViewBean;
	};
	
	this.setParentViewBean=function(newViewBean){
		//situationFamilialeViewBean = newViewBean;
	};	
	
	this.init(
		    function(){	
		    	this.stopEdition();
		    }
		);
	
};

// fonction d'initialisation de la page lorsque JQuery est prêt
$(function(){
	
	var $areaMembre = $('.areaMembre');
	var enfantFamillePart= new EnfantFamillePart($('.areaMembre'));

	$areaMembre.find('.btnAjaxUpdate').click(function(){
		enfantFamillePart.startEdition();
	});
	$areaMembre.find('.btnAjaxCancel').click(function(){
		enfantFamillePart.stopEdition();
	});
	$areaMembre.find('.btnAjaxValidate').click(function(){
		enfantFamillePart.validateEdition();
	});
	$areaMembre.find('.btnAjaxDelete').click(function(){
		enfantFamillePart.ajaxDeleteEntity(enfantFamillePart.selectedEntityId);
	});
	$areaMembre.find('.btnAjaxAdd').click(function(){
		enfantFamillePart.stopEdition();
		enfantFamillePart.startEdition();
		$('#widgetTiersEnfant').show();
	});
	 
	ConjointFamillePart.prototype = AbstractSimpleAJAXDetailZone;
	
	var $conjointContainer = $('#ConjointContainer');
	var conjointFamillePart = new ConjointFamillePart($conjointContainer);
	var $supprimerConjoint = $('#linkSupprimerConjoint');
	var b_disabled = true;
	var s_linkDFconj = "";
	
	$supprimerConjoint.button({ disabled: true });
	
	$supprimerConjoint.click(function(){
		if(!b_disabled){
			$('#resultAutocompete').children().remove();
			$('#nss').val("");
			$('#idTiers').val("");
			$('#widgetTiers').val("");
			$('#widgetTiers').hide();
			$('[name="situationFamiliale.simpleSituationFamiliale.csTypeConjoint"]').val('');
			$('#resultAutocompete').children().remove();
			b_disabled = true;
			$supprimerConjoint.button("option", "disabled", true);
			conjointFamillePart.ajaxUpdateEntity();
		}
	});

	$conjointContainer.find('.btnAjaxUpdate').click(function(){
		b_disabled = false;
		$supprimerConjoint.button("option", "disabled", false);
		$('#widgetTiers').show();
		$('#linkDFconj').hide();
		conjointFamillePart.startEdition();
		conjointFamillePart.ajaxLoadEntity();
	});
	$conjointContainer.find('.btnAjaxCancel').click(function(){
		b_disabled = true;
		$supprimerConjoint.button("option", "disabled", true);
		conjointFamillePart.stopEdition();
		$('#widgetTiers').hide();
	});
	$conjointContainer.find('.btnAjaxValidate').click(function(){
		b_disabled = true;
		$supprimerConjoint.button("option", "disabled", true);
		conjointFamillePart.ajaxUpdateEntity();
		$('#widgetTiers').hide();
	});
//	$conjointContainer.find('.btnAjaxDelete').click(function(){
//		$('#nss').val("");
//		$('#idTiers').val("");
//		$('#resultAutocompete').children().remove();
//		conjointFamillePart.ajaxUpdateEntity();
//	});
	$conjointContainer.find('.btnAjaxAdd').click(function(){
		conjointFamillePart.stopEdition();
		conjointFamillePart.startEdition();
	});
	
});
