/**
 * @author DDE
 */

function Retenues (container){
	// variables
	var that=this;
	
	this.ACTION_AJAX = ACTION_AJAX_RETENUE;
	this.mainContainer = container;
	this.table = this.mainContainer.find(".areaTabel");
	this.detail = this.mainContainer.find(".areaDetail");
	this.idPcfAccordee = ID_PCFACCORDEE;
	//this.titleContainer = this.mainContainer.find(".areaTitre");
	
	// functions	
	this.afterRetrieve=function(data){
	//	creancier.b_temporise = false;
		this.defaultLoadData(data, "#",true);
		this.detail.find('#cleSection').val(data.simpleRetenue.idCompteAnnexe+","+data.simpleRetenue.idExterneSection+","+data.simpleRetenue.idTypeSection);
		if (data.adressePaiement != null) {
			this.detail.find('.adresse').html(data.adressePaiement.replace(/[\r\n]/g,'<br />'));
		}
	};
	
	this.getParametresForFind = function () {
		return {"searchModel.forIdPcfAccordee":this.idPcfAccordee};
	}
	
	this.getParametres=function(){
		var cleSection = this.detail.find('#cleSection').val().split(",");
		var idCompteAnnexe = cleSection[0];
		var idExterneSection = cleSection[1];
		var idTypeSection = cleSection[2];
		var o_map = {
			'simpleRetenue.csTypeRetenue' : this.detail.find('#csTypeRetenue').val(),
			'simpleRetenue.dateDebutRetenue' : this.detail.find('#dateDebutRetenue').val(),
			'simpleRetenue.dateFinRetenue' : this.detail.find('#dateFinRetenue').val(),
			'simpleRetenue.idDomaineApplicatif' : this.detail.find('#idDomaineApplicatif').val(),
			'simpleRetenue.idExterne' : this.detail.find('#idExterne').val(),
			'simpleRetenue.idParentRetenue' : this.detail.find('#idParentRetenue').val(),
			'simpleRetenue.idPcfAccordee' : this.idPcfAccordee,
			'simpleRetenue.idExterneSection' : idExterneSection,
			'simpleRetenue.idTypeSection' : idTypeSection,
			'simpleRetenue.idCompteAnnexe' : idCompteAnnexe,
			'simpleRetenue.idTiersAdressePmt' : this.detail.find('#idTiersAdressePmt').val(),
			'simpleRetenue.montantDejaRetenu' : this.detail.find('#montantDejaRetenu').val(),
			'simpleRetenue.montantRetenuMensuel' : this.detail.find('#montantRetenuMensuel').val(),
			'simpleRetenue.montantTotalARetenir' : this.detail.find('#montantTotalARetenir').val(),
			'simpleRetenue.noFacture' : this.detail.find('#noFacture').val(),
			'simpleRetenue.tauxImposition' : this.detail.find('#tauxImposition').val()
			
		};
		return $.extend(o_map,this.getParametresForFind());
	};	
	
	this.clearFields = function(){
		this.detail.clearInputForm();
		this.detail.find('.adresse').html('');
		this.detail.find('#adressePaiementValue').attr('defaultvalue','');
		this.detail.find('.adresse').children().remove();
	}
	
	this.getParentViewBean=function(){
		//return droit;	
	};
	
	this.setParentViewBean=function(newViewBean){
		//droit=newViewBean;
	}
	
	this.formatTableTd=function($elementTable){
		//$elementTable.find('td:eq(3),td:eq(4),td:eq(8),td:eq(9)').css('text-align','right').end();
	} 
	
	// initialization
	this.init(
	    function(){
			this.capage();
		}
	);
}


//fonction d'initialisation de la page lorsque JQuery est prêt
$(function(){
	Retenues.prototype = AbstractScalableAJAXTableZone;
	$('.area').each(function(){
		var $that = $(this);
		var zone = new Retenues($that);
		this.zone = zone;
		$that.find('.btnAjaxUpdate').click(function(){
				zone.startEdition();
		}).end()
		.find('.btnAjaxCancel').click(function(){
			zone.stopEdition();
		}).end()
		.find('.btnAjaxValidate').click(function(){
			zone.validateEdition();
		}).end()
		.find('.btnAjaxDelete').click(function(){
				zone.ajaxDeleteEntity(zone.selectedEntityId);
		}).end()
		.find('.btnAjaxAdd').click(function(){
				zone.stopEdition();
				zone.startEdition();
		});
		//zone.ajaxFind();
	});
});