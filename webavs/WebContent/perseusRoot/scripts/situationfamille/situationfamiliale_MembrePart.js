

function EnfantFamillePart(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX = "perseus.situationfamille.enfantAjax";
	this.mainContainer=container;
	this.table=this.mainContainer.find(".areaDFDataTable");
	this.detail=this.mainContainer.find(".areaDFDetail");
	this.titleContainer=this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass="areaDFModified";
	
	this.membreId=null;

	// functions
	
	this.openConfirmInsertDialog = function (msg) {
		$html = $('<div class="globaz_utils_console">' + msg + '</div>');
		
		
		
		$html.dialog({
			position: 'top',
			title: 'Information',
			width: 750,
			show: "blind",
			hide: "blind",
			modal: true,
		    resizable: false,
			closeOnEscape: true,
			buttons: [
			 {
				text:globazGloball.popup_cancel_button,
				click:function () {
					$( this ).dialog( "close" );
				}
			 },
			 {
				text:globazGloball.popup_ok_button,
				click : function () {
					that.validateEdition();
					that.afterValidation();
					$( this ).dialog( "close" );
				}
			}]
		});
		
		
		
	};
	
	this.afterRetrieve=function($data){
		
		var xmlData=this.getXMLData($data, 'tiers');
		
		this.detail.find('#resultAutocompeteEnfant').children().remove();
		this.detail.find('#resultAutocompeteEnfant').contents().remove();
		this.detail.find('#resultAutocompeteEnfant').append(xmlData);
		
		this.detail.find('[name=csFormation]').val($data.find('formation').text()).end()
				.find('#idTiersEnfant').val($data.find('idTiersEnfant').text()).end()
				.find('[name=csGarde]').val($data.find('garde').text()).end()
				.find('[name=csEtatCivil]').val($data.find('etatCivil').text()).end()
				.find('[name=csSource]').val($data.find('source').text()).end()
				.find('[name=spy]').val($data.find('spy').text()).end()
				.find('#idMembreFamille').val($data.find('idMembreFamille').text()).end()
				.find('#idEnfant').val($data.find('idEnfant').text()).end()
				.find('#idEnfantFamille').val($data.find('idEnfantFamille').text()).end()
				.find('[name=spyMbrFam]').val($data.find('spyMbrFam').text()).end();

		this.detail.find('#widgetTiersEnfant').hide();
		if ($data.find('ai').text() === "true") {
			this.detail.find('[name=isAI]').prop("checked", true);
		} else {
			this.detail.find('[name=isAI]').prop("checked", false);
		}
	};
	
	this.afterStopEdition=function() {
		var options = {
				serviceClassName:'ch.globaz.perseus.business.services.models.demande.DemandeService',
				serviceMethodName:'isCalculable',
				parametres:PAGE_ID_DEMANDE,
				callBack:function(calculable){
					if (calculable === 'true') {
						$('#linkDFreq').show();
						$('#divlinkDFconj').show();
					} else {
						$('#linkDFreq').hide();
						$('#divlinkDFconj').hide();
					}
				}
			};
		
		globazNotation.readwidget.options = options;
		globazNotation.readwidget.read();
	};
	
	this.getParametresForFind= function () {
		return {"enfantFamilleSearch.forIdSituationFamiliale": 99};
	};
	
	this.afterValidation=function() {
	};
		
	this.formatTable=function() {
		$.ajax({
			  url: "perseus?userAction=perseus.situationfamille.situationfamiliale.afficher&selectedId=" + PAGE_ID_SITUATION_FAMILIALE + "&idDemande=" + PAGE_ID_DEMANDE, 
			  context: document.body,
			  success: function(){
				  reloadMenuFrame(top.fr_menu, 'options');
			  }
		});
	};
	
	this.getParametres=function(){

		return {
			"enfantFamille.simpleEnfantFamille.csFormation":this.detail.find('[name=csFormation]').val(),
			"enfantFamille.simpleEnfantFamille.csGarde":this.detail.find('[name=csGarde]').val(),
			"enfantFamille.simpleEnfantFamille.csEtatCivil":this.detail.find('[name=csEtatCivil]').val(),
			"enfantFamille.simpleEnfantFamille.csSource":this.detail.find('[name=csSource]').val(),
			"enfantFamille.enfant.membreFamille.simpleMembreFamille.isAI":this.detail.find('[name=isAI]').prop("checked"),
			"enfantFamille.enfant.membreFamille.simpleMembreFamille.idTiers":this.detail.find('#idTiersEnfant').val(),
			"enfantFamille.simpleEnfantFamille.spy":this.detail.find('#spy').val(),
			"enfantFamille.enfant.membreFamille.simpleMembreFamille.spyMbrFam":this.detail.find('#spyMbrFam').val(),
			"enfantFamille.simpleEnfantFamille.idEnfant":this.detail.find('#idEnfant').val(),
			"enfantFamille.simpleEnfantFamille.idEnfantFamille":this.detail.find('#idEnfantFamille').val(),
			
			"enfantFamille.enfant.membreFamille.simpleMembreFamille.idMembreFamille":this.detail.find('#idMembreFamille').val(),
			"enfantFamille.simpleEnfantFamille.idSituationFamiliale":PAGE_ID_SITUATION_FAMILIALE,
			"idSituationFamiliale":PAGE_ID_SITUATION_FAMILIALE,
			"idDemande":PAGE_ID_DEMANDE
		};
	};
		
	this.getParametresAsString = function () {

		var obj = {};
		obj.simpleEnfantFamille = {};
		obj.simpleEnfantFamille.csFormation = this.detail.find('[name=csFormation]').val();
		obj.simpleEnfantFamille.csGarde = this.detail.find('[name=csGarde]').val();
		obj.simpleEnfantFamille.csEtatCivil = this.detail.find('[name=csEtatCivil]').val();
		obj.simpleEnfantFamille.csSource = this.detail.find('[name=csSource]').val();
		obj.simpleEnfantFamille.spy = this.detail.find('[name=spy]').val();
		obj.simpleEnfantFamille.idEnfant = this.detail.find('#idEnfant').val();
		obj.simpleEnfantFamille.idEnfantFamille = this.detail.find('#idEnfantFamille').val();
		
		obj.enfant = {};
		obj.enfant.membreFamille = {};
		obj.enfant.membreFamille.simpleMembreFamille = {};
		obj.enfant.membreFamille.simpleMembreFamille = {};
		obj.enfant.membreFamille.simpleMembreFamille.isAI = this.detail.find('[name=isAI]').prop("checked");
		obj.enfant.membreFamille.simpleMembreFamille.idTiers = this.detail.find('#idTiersEnfant').val();
		obj.enfant.membreFamille.simpleMembreFamille.spy = this.detail.find('[name=spyMbrFam]').val();
		obj.enfant.membreFamille.simpleMembreFamille.idMembreFamille = this.detail.find('#idMembreFamille').val();
		
		
		obj.simpleEnfant = {};
		obj.simpleEnfantFamille.idSituationFamiliale = PAGE_ID_SITUATION_FAMILIALE;
		
		return ajaxUtils.jsonToString(obj);
		
	};
	
	this.clearFields=function(){
		this.detail.find('#widgetTiersEnfant,#resultAutocompeteEnfant,#idTiersEnfant,#csFormation,#csGarde,#csEtatCivil,#csSource').val('');
		this.detail.find('[name=isAI]').prop("checked", false);
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
	
	this.ACTION_AJAX = "perseus.situationfamille.conjointAjax";
	this.mainContainer=container;
	this.detail=this.mainContainer.find(".areaDFDetail");
	this.modifiedZoneClass="areaDFModified";
	this.selectedEntityId = PAGE_ID_SITUATION_FAMILIALE;
	this.membreId=null;
	this.currentViewBean = conjointAjaxViewBean

	// functions
	
	this.onUpdate = function($data){
		var idTiersConjoint = $('#idTiers').val();
		var $divLinkDfConj = $('#divlinkDFconj');
		$divLinkDfConj.children().remove();
		//Si il y'a un conjoint défini récupérer l'id conjoint
		if (idTiersConjoint != "") {
			var options = {
				serviceClassName:'ch.globaz.perseus.business.services.models.situationfamille.ConjointService',
				serviceMethodName:'getIdConjoint',
				parametres:idTiersConjoint,
				callBack:function(idConjoint){
					$divLinkDfConj.children().remove();
					link = '<a id="linkDFconj" href="';
					link += URL_DONNEE_FINANCIERES;
					link += '&idConjoint=' + idConjoint + '">' + JSP_PF_FAMILLE_D_DONNEES_FINANCIERES + '</a>';
					$divLinkDfConj.append(link);
				}
			}
			
			var o_read = Object.create($.extend({},globazNotation.readwidget));
			o_read.options = options;
			o_read.read();
		} 
		//Si il y'a un conjoint défini récupérer l'id conjoint
		if (idTiersConjoint != "") {
			var options2 = {
				serviceClassName:'ch.globaz.perseus.business.services.models.dossier.DossierService',
				serviceMethodName:'hasDossier',
				parametres:idTiersConjoint,
				callBack:function(hasDossier){
					if (hasDossier) {
						globazNotation.utils.consoleInfo("<div style='height:120px;'><br/>"+AVERTISSEMENT_DOSSIER_CONJOINT + "</div>", AVERTISSEMENT_DOSSIER_CONJOINT_TITRE, true);
					}
				}
			}
			var o_read2 = Object.create($.extend({},globazNotation.readwidget));
			o_read2.options = options2;
			o_read2.read();
		} 
	};
	
	this.getParametres=function(){
		return {
			"conjoint.membreFamille.personneEtendue.personneEtendue.numAvsActuel":this.mainContainer.find("#nss").val(),
			"idTiersConjoint":this.mainContainer.find("#idTiers").val(),
			"idDemande":this.mainContainer.find("#idDemande").val(),
			"situationFamiliale.simpleSituationFamiliale.csTypeConjoint":this.mainContainer.find('[name="situationFamiliale.simpleSituationFamiliale.csTypeConjoint"]').val(),
			"situationFamiliale.simpleSituationFamiliale.csNiveauFormationRequerant":this.mainContainer.find('[name="situationFamiliale.simpleSituationFamiliale.csNiveauFormationRequerant"]').val(),
			"situationFamiliale.simpleSituationFamiliale.csEtatCivilRequerant":this.mainContainer.find('[name="situationFamiliale.simpleSituationFamiliale.csEtatCivilRequerant"]').val(),
			"situationFamiliale.simpleSituationFamiliale.csSituationActiviteRequerant":this.mainContainer.find('[name="situationFamiliale.simpleSituationFamiliale.csSituationActiviteRequerant"]').val(),
			"situationFamiliale.simpleSituationFamiliale.csEtatCivilConjoint":this.mainContainer.find('[name="situationFamiliale.simpleSituationFamiliale.csEtatCivilConjoint"]').val(),
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
		IS_UPDATE = true;
		enfantFamillePart.startEdition();
	});
	$areaMembre.find('.btnAjaxCancel').click(function(){
		IS_UPDATE = false;
		enfantFamillePart.stopEdition();
	});
	
	var getParametersForAjax = function (parmetre, callBack) {
	    var that = this;
	    var parametre;
	    
	    if(!IS_UPDATE){
	    	parametre = {
	    	        userAction: "widget.action.jade.afficher",
	    	        serviceClassName: "ch.globaz.perseus.business.services.models.situationfamille.EnfantFamilleService",
	    	        serviceMethodName: "checkForAjaxAdd",
	    	        initThreadContext: that.options.wantInitThreadContext,
	    	        cursor: 5,
	    	        parametres: enfantFamillePart.getParametresAsString(),
	    	        forceParametres : that.options.forceParametres,
	    	        noCache: globazNotation.utilsDate.toDayInStringJadeFormate() + (new Date()).getMilliseconds()
	    	    };
	    } else {
	    	parametre = {
	    	        userAction: "widget.action.jade.afficher",
	    	        serviceClassName: "ch.globaz.perseus.business.services.models.situationfamille.EnfantFamilleService",
	    	        serviceMethodName: "updateAjax",
	    	        initThreadContext: that.options.wantInitThreadContext,
	    	        cursor: 5,
	    	        parametres: enfantFamillePart.getParametresAsString(),
	    	        forceParametres : that.options.forceParametres,
	    	        noCache: globazNotation.utilsDate.toDayInStringJadeFormate() + (new Date()).getMilliseconds()
	    	    };
	    };
	    
	    return parametre;
	};
	
	
	
	$areaMembre.find('.btnAjaxValidate').click(function(){
		
		var that = this;
		
			$.ajax({
	            url: globazNotation.utils.getFromAction(),
	            async: true,
	            dataType: "json",
	            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
	            data: getParametersForAjax(),
	            success: function (data) {


	            	msgType = data.viewBean.returnInfosService.msgType;
	            	
	            	switch(msgType){
	            		
	            		case "WARN":
	            			enfantFamillePart.openConfirmInsertDialog(data.viewBean.returnInfosService.message);
	            			break;
	            		
	            		case "ERROR":
	            			globazNotation.utils.consoleError(data.messages[0].message);
	            			break;
	            		
	            		case "EXCEPTION":
	            			globazNotation.utils.consoleError(data.messages[0].message);
	            			break;
	            		
	            		case "OK":
	            			enfantFamillePart.validateEdition();
	            			enfantFamillePart.afterValidation();
	            			break;
	            	}
	                
	            	
	            	
	            },
	            error: function (jqXHR, textStatus, errorThrown) {
	            	enfantFamillePart.applyErrorCallBack(jqXHR, textStatus, errorThrown);
	            },
	            type: "GET"
	        });
		 
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
			//$('#linkDFconj').hide();
			conjointFamillePart.ajaxUpdateEntity();
		}
	});

	$conjointContainer.find('.btnAjaxUpdate').click(function(){
		b_disabled = false;
		$supprimerConjoint.button("option", "disabled", false);
		$('#widgetTiers').show();
		//$('#linkDFconj').hide();
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
