/**
 * @author DDE
 */

function Creancier (container){
	// variables
	var that=this;
	
	this.ACTION_AJAX = ACTION_AJAX_CREANCIER;
	this.mainContainer = container;
	this.table = this.mainContainer.find(".areaTabel");
	this.detail = this.mainContainer.find(".areaDetail");
	this.idRentePont = idRentePont;
	//this.titleContainer = this.mainContainer.find(".areaTitre");
	
	// functions	
	this.afterRetrieve=function(data){
	//	creancierRentePont.b_temporise = false;
		this.defaultLoadData(data, "#",true);
		if (data.adressePaiement != null) {
			this.detail.find('.adresse').html(data.adressePaiement.replace(/[\r\n]/g,'<br />'));
		}
	};
	
	this.getParametresForFind = function () {
		return {"searchModel.forIdRentePont":this.idRentePont};
	}
	
	this.getParametres=function(){
		var o_map = {
			'creancierRentePont.simpleCreancierRentePont.csTypeCreance': this.detail.find('#csTypeCreance').val(),
			'creancierRentePont.simpleCreancierRentePont.idRentePont': idRentePont,
			'creancierRentePont.simpleCreancierRentePont.idDomaineApplicatif': this.detail.find('#idDomaineApplicatif').val(),
			'creancierRentePont.simpleCreancierRentePont.idTiers': this.detail.find('#idTiers').val(),
			'creancierRentePont.simpleCreancierRentePont.montantRevendique': this.detail.find('#montantRevendique').val(), 
			'creancierRentePont.simpleCreancierRentePont.montantAccorde': this.detail.find('#montantAccorde').val(), 
			'creancierRentePont.simpleCreancierRentePont.referencePaiement': this.detail.find('#referencePaiement').val()
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
			$("html").bind(eventConstant.NOTATION_FRAGMENT_DONE,function(){
				retroTotal = globazNotation.utilsFormatter.amountTofloat($('#retroTotal').val());
				totalReparti = globazNotation.utilsFormatter.amountTofloat($('#cellTotalReparti').text());
				retroDispo = globazNotation.utilsFormatter.formatStringToAmout(retroTotal - totalReparti);
				$('#retroDisponible').val(retroDispo);
			});
	    	this.capage();
		}
	);
}


//fonction d'initialisation de la page lorsque JQuery est prêt
$(function(){
	Creancier.prototype = AbstractScalableAJAXTableZone;
	$('.area').each(function(){
		var $that = $(this);
		var zone = new Creancier($that);
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