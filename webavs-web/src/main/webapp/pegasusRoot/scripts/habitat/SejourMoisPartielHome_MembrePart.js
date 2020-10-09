function SejourMoisPartielHome (container) {
	// variables
	var that = this;
	var idTypeChambre = null;
	this.ACTION_AJAX = ACTION_AJAX_DONNEE_FINANCIERE;
	this.ACTION_TYPE_CHAMBRE = ACTION_AJAX_TYPE_CHAMBRE;
	this.mainContainer = container;
	this.table = this.mainContainer.find(".areaDFDataTable");
	this.detail = this.mainContainer.find(".areaDFDetail");
	this.titleContainer = this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass = "areaDFModified";
	this.membreId = null;
	this.$dialogue = that.mainContainer.find(".dialog-confirm");

	// functions
	this.afterRetrieve = function ($data, idEntity) {
		this.idTypeChambre = $data.find('idTypeChambre').text();
		var idHome = $data.find('idHome').text();
		this.detail.find('.prixJournalier').val($data.find('prixJournalier').text()).change().end()
				.find('.fraisNourriture').val($data.find('fraisNourriture').text()).change().end()
				.find('.nbJours').val($data.find('nbJours').text()).change().end()
				.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
				.find('[name=dateFin]').val($data.find('dateFin').text()).end()
				.find('.idHome').val(idHome).change().end()
				.find('.libelleHome').val($data.find('libelleHome').text()).change().end()
				.find('.textLibre').val($data.find('textLibre').text()).change().end()
				.find('[name=idTypeChambre]').val(this.idTypeChambre).attr('disabled', 'disabled').end()
				.find('.isVersementDirect').attr('checked', $data.find('isVersementDirect').text() == 'true').end()
				.find('.detailPrixChambres').attr('data-id-home',idHome).end()
				.find('.detailPrixChambres').attr('data-g-desc-home',$data.find('libelleHome').text()).end()
				.find('.detailPrixChambres').attr('data-id-chambre',this.idTypeChambre).end()
				.find('.detailPrixChambres').attr('data-dateDebut',$data.find('dateDebut').text()).end()
				.find('.detailPrixChambres').attr('data-dateFin',$data.find('dateFin').text());

		// champ.
		this.idEntity = idEntity;
		this.isModifFraisNourritureCheck();
	};

	this.getParametres = function ($data) {
		return {
			'sejourMoisPartielHome.simpleSejourMoisPartielHome.prixJournalier': this.detail.find('.prixJournalier').val(),
			'sejourMoisPartielHome.simpleSejourMoisPartielHome.fraisNourriture': this.detail.find('.fraisNourriture').val(),
			'sejourMoisPartielHome.simpleSejourMoisPartielHome.nbJours': this.detail.find('.nbJours').val(),
			'sejourMoisPartielHome.simpleSejourMoisPartielHome.idHome': this.detail.find('.idHome').val(),
			'sejourMoisPartielHome.simpleSejourMoisPartielHome.idTypeChambre': this.detail.find('[name=idTypeChambre]').val(),
			'sejourMoisPartielHome.simpleSejourMoisPartielHome.nbJours': this.detail.find('.nbJours').val(),
			'sejourMoisPartielHome.simpleSejourMoisPartielHome.textLibre': this.detail.find('.textLibre').val(),
			'sejourMoisPartielHome.simpleDonneeFinanciereHeader.dateDebut': this.detail.find('[name=dateDebut]').val(),
			'sejourMoisPartielHome.simpleDonneeFinanciereHeader.dateFin': this.detail.find('[name=dateFin]').val(),
			'sejourMoisPartielHome.simpleSejourMoisPartielHome.isVersementDirect' : this.detail.find('.isVersementDirect').is(':checked'),
			'doAddPeriode': this.doAddPeriode,
			'idDroitMembreFamille': this.membreId
		};
	};

	this.clearFields = function () {
		this.detail.clearInputForm();
		this.detail.find('.listTypechambre').children().remove();
	};

	this.addTypeChambre = function () {
		var that = this;
		var idTier = that.mainContainer.attr('idTier');
		var idHome = this.value;
		var doOnce = true;
		this.detail.find('.idHome').bind(eventConstant.AJAX_CHANGE, function () {
			if ((this.value.length)) {
				doOnce = false;
				idHome = this.value;
				var nomHome = that.detail.find('.selecteurHome').val();
				// that.detail.find('.toHomeLink').attr('href','pegasus?userAction=pegasus.home.home.afficher&selectedId='+idHome);

				$listTypeChambre = that.detail.find('.listTypechambre');
				$listTypeChambre.empty();
				$listTypeChambre.append('<span>Loading...</span>');
				that.$dialogue.dialog('destroy');
				$.ajax({
					type: "GET",
					url: "pegasus",
					dataType: "xml",
					async: false,
					data: {
						userAction: that.ACTION_TYPE_CHAMBRE + ".listerAJAX",
						afficheTypeChambre: true,
						forIdHome: idHome,
						forIdTier: idTier
					},
					success: function (data) {
						$select = $(data).find('[name=idTypeChambre]');
						var $xml;
						($select).is('select') ? $xml = $select.toXML() : $xml = "<span class='listeVide'>" + $(data).find('messageListeVide').text() + "<span>";
						$listTypeChambre.children().remove().end().append($xml);

						that.detail.find('.typeChambre').val(that.idTypeChambre);


						var re = new RegExp("designationTypeChambre=$");


						if ($select.find('[particularite]').attr('particularite') != undefined) {

							that.$dialogue.dialog({
								resizable: false,
								autoOpen: false,
								modal: false,
								buttons: {
									'ok': function () {
										$(this).dialog('close');
									}
								}
							});
							$listTypeChambre.find('[name=idTypeChambre]').change(function () {
								that.$dialogue.dialog("open");
							});
						}

					}
				});
			}
		});
	};

	this.getParentViewBean = function () {
		return droit;
	};

	this.setParentViewBean = function (newViewBean) {
		droit = newViewBean;
	};

	this.formatTableTd = function ($elementTable) {
		$elementTable.find('td:eq(1),td:eq(2)').css('text-align', 'right').end();
	};

	this.modifFraisNourriture = function () {
		var fraisNourriture = this.detail.find('.fraisNourriture');
		if (this.detail.find('.isFraisNourriture').prop('checked')) {
			fraisNourriture.prop( "disabled", false );
		} else {
			fraisNourriture.prop( "disabled", true );
			fraisNourriture.val(VAL_FRAIS_NOURRITURE);
		}
	};

	this.isModifFraisNourriture = function () {
		var fraisNourriture = this.detail.find('.fraisNourriture');
		var isFraisNourriture = this.detail.find('.isFraisNourriture');
		if(fraisNourriture.val() ==VAL_FRAIS_NOURRITURE) {
			fraisNourriture.prop( "disabled", true );
		} else {
			fraisNourriture.prop( "disabled", false );
		}
	}

	this.isModifFraisNourritureCheck = function () {
		var fraisNourriture = this.detail.find('.fraisNourriture');
		var isFraisNourriture = this.detail.find('.isFraisNourriture');
		if(fraisNourriture.val() ==VAL_FRAIS_NOURRITURE) {
			isFraisNourriture.prop( "checked", false );
		} else {
			isFraisNourriture.prop( "checked", true );
		}
	}

	this.addEvent = function () {
		this.detail.find('.isFraisNourriture').click(function () {
			that.modifFraisNourriture();
		});
	};

	// initialization
	this.init(function () {
		this.stopEdition();
		this.onAddTableEvent();
		this.colorTableRows(false);
	});
	this.showOrHideDetail = function () {
	};

	this.updatePrixJournalier = function (){
		var dateDebut = this.detail.find('[name=dateDebut]').val();
		var idTypeChambre = this.detail.find('.listTypechambre :selected').val();
		var idHome = this.detail.find('.idHome').val();

		if(dateDebut && idTypeChambre) {
			var affichage_decompte_service_call_options = {
				serviceClassName: 'ch.globaz.pegasus.business.services.models.home.HomeService',
				serviceMethodName: 'getListePrixChambres',
				parametres: idHome+","+idTypeChambre+","+dateDebut+","+dateDebut,
				callBack: function (data){
					that.callback_prix_chambre(data);
				}
			}
			globazNotation.readwidget.options = affichage_decompte_service_call_options;
			globazNotation.readwidget.read();
		}
	};

	this.callback_prix_chambre = function (data) {
		var montant = data.montantsPeriode[0];
		if(montant != null) {
			this.detail.find('.prixJournalier').val(montant.prixChambre.montant);
		}
	};
}

$(function () {
	SejourMoisPartielHome.prototype = DonneeFinancierePart.prototype;

	$('.areaMembre').each(function () {
		var $that = $(this);
		var zone = new SejourMoisPartielHome($that);
		this.zone = zone;
		zone.membreId = $(this).attr('idMembre');
		zone.addEvent();
		$that.find('.btnAjaxUpdate').click(function () {
			zone.startEdition();
			zone.modifFraisNourriture();
			$that.find('.btnAjaxValidateNouvellePeriode').show();

		}).end()
		.find('.btnAjaxCancel').click(function () {
			zone.stopEdition();
		}).end()
		.find('.btnAjaxValidate').click(function () {
			zone.validateEdition();
		}).end()
		.find('.btnAjaxDelete').click(function () {
			zone.ajaxDeleteEntity(zone.selectedEntityId);
		}).end()
		.find('.btnAjaxAdd').click(function () {
			zone.stopEdition();
			zone.startEdition();
			$that.find('.fraisNourriture').val(VAL_FRAIS_NOURRITURE);
			zone.isModifFraisNourritureCheck();
			zone.isModifFraisNourriture();
		}).end()
		.find('.btnAjaxValidateNouvellePeriode').click(function () {
			zone.doAddPeriode = true;
			zone.validateEdition();
		}).end()
			.find('[name=dateDebut]').change(function () {
			zone.updatePrixJournalier();
		}).end()
			.find('.listTypechambre').change(function () {

				var idTypeChambre = $('.listTypechambre :selected').val();
				var dateDebut = $that.find('[name=dateDebut]').val();
				var dateFin = $('[name=dateFin]').val();

				$that.find('.detailPrixChambres').attr('data-id-chambre',idTypeChambre);
				$that.find('.detailPrixChambres').attr('data-dateDebut',dateDebut);
				$that.find('.detailPrixChambres').attr('data-dateFin',dateFin);
				zone.updatePrixJournalier();
			}).end();
		zone.addTypeChambre();

	});
});
