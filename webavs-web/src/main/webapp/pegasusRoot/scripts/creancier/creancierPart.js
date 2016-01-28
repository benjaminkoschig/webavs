/**
 * @author DMA
 */

var creancier = {
	b_temporise: true,
	init: function () {
		this.addEvent();
		$('html').bind(eventConstant.NOTATION_MANAGER_DONE, function () {
			if ($.trim($('#idTiers').val()) == '') {
				$('.adresse').html('');
			}
		});
	},
	
	addEvent: function () {
		var that = this;
		$('#idTiers').change(function () {
			if ($.trim(this.value) != '' && that.b_temporise) {
				that.b_temporise = false;
				that.readAdresse(this.value);
				setTimeout(function () {
					that.b_temporise = true;
				}, 200);
			} 
		});
	},

	displayAdresse: function (data) {
		
		if(data && data.adresseFormate) {
			var html = data.adresseFormate.replace(/[\r\n]/g, '<br />');
			$('.adresse').html(html);
		} else {
			$('.adresse').html(aucuneAdresseDePaiement);
		}
	},

	readAdresse: function (idTiers) {
		var that = this;
		var options = {
			serviceClassName: 'ch.globaz.pyxis.business.service.AdresseService',
			serviceMethodName: 'getAdressePaiementTiers',
			parametres: idTiers + ",true," + CS_DOMAINE_APPLICATION_RENTE + "," + globazNotation.utilsDate.toDayFromated() + ",0",
			callBack: that.displayAdresse
		}

		globazNotation.readwidget.options = options;
		globazNotation.readwidget.read();
	}

}

function Creancier (container) {
	var that = this;

	this.ACTION_AJAX = ACTION_AJAX_CREANCIER;
	this.mainContainer = container;
	this.table = this.mainContainer.find(".areaTabel");
	this.detail = this.mainContainer.find(".areaDetail");
	this.idDemande = idDemande;
	this.objetSpy = "creancier.simpleCreancier";

	// functions
	this.afterRetrieve = function ($data) {
		creancier.b_temporise = false;
		//multiWidgets 
		this.defaultLoadData($data, "#", true);
		$(".adresse").append($data.adresse);
		$("#span_value_multiwidgets_"+$(".globazMultiWidgets").attr("uniqueid")).text($data.creancier.simpleTiers.designation1+" "+$data.creancier.simpleTiers.designation2);
	};

	this.getParametresForFind = function () {
		return {
			"searchModel.forIdDemande": this.idDemande
		};
	};

	this.getParametres = function () {
		var o_map = {
			'creancier.simpleCreancier.csTypeCreance': this.detail.find('#csTypeCreance').val(),
			'creancier.simpleCreancier.idAffilieAdressePaiment': this.detail.find('#idAffilieAdressePaiment').val(),
			'creancier.simpleCreancier.idDemande': idDemande,
			'creancier.simpleCreancier.idDomaineApplicatif': this.detail.find('#idDomaineApplicatif').val(),
			'creancier.simpleCreancier.idTiers': this.detail.find('#idTiers').val(),
			'creancier.simpleCreancier.idTiersAdressePaiement': this.detail.find('#idTiersAdressePaiement').val(),
			'creancier.simpleCreancier.idTiersRegroupement': this.detail.find('#idTiersRegroupement').val(),
			'creancier.simpleCreancier.montant': this.detail.find('#montant').val(),
			'creancier.simpleCreancier.referencePaiement': this.detail.find('#referencePaiement').val()
		};
		return $.extend(o_map, this.getParametresForFind());
	};

	this.clearFields = function () {
		this.detail.clearInputForm();
		this.detail.find('.adresse').html('');
		this.detail.find('#adressePaiement').attr('defaultvalue', '');
		this.detail.find('.adresse').children().remove();
		$("#span_value_multiwidgets_"+$(".globazMultiWidgets").attr("uniqueid")).text("");
	};

	this.getParentViewBean = function () {};

	this.setParentViewBean = function (newViewBean) {};

	this.formatTableTd = function ($elementTable) {	};

	// initialization
	this.init(function () {
		this.capage();
		this.mainContainer.bind(eventConstant.AJAX_LOAD_DATA, function () {
			creancier.b_temporise = true;
		});
	});
}

// fonction d'initialisation de la page lorsque JQuery est prêt
$(function () {
	creancier.init();
	Creancier.prototype = AbstractScalableAJAXTableZone;
	$('.area').each(function () {
		var $that = $(this);
		var zone = new Creancier($that);
		this.zone = zone;
		$that.find('.btnAjaxUpdate').click(function () {
			zone.startEdition();
		}).end().find('.btnAjaxCancel').click(function () {
			zone.stopEdition();
		}).end().find('.btnAjaxValidate').click(function () {
			zone.validateEdition();
		}).end().find('.btnAjaxDelete').click(function () {
			zone.ajaxDeleteEntity(zone.selectedEntityId);
		}).end().find('.btnAjaxAdd').click(function () {
			zone.stopEdition();
			zone.startEdition();
		});
		// zone.ajaxFind();
	});
});