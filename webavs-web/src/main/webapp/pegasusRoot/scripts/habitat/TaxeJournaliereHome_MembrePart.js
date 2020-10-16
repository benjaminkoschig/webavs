function TaxeJournaliereHome (container) {
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
		this.detail.find('[name=dateEcheance]')	.val($data.find('dateEcheance').text()).end()
				.find('.idAssureurMaladie').val($data.find('idAssureurMaladie').text()).change().end()
				.find('.idHome').val(idHome).change().end()
				.find('.isParticipationLCA').attr('checked', $data.find('isParticipationLCA').text() == 'true').end()
				.find('.isDeplafonner').attr('checked',$data.find('isDeplafonner').text()=='true').end()
				.find('.montantJournalierLCA').val($data.find('montantJournalierLCA').text()).change().end()
				.find('.primeAPayer').val($data.find('primeAPayer').text()).change().end()
				.find('.montantFraisLongueDuree').val($data.find('montantFraisLongueDuree').text()).change().end()
				.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
				.find('[name=dateFin]').val($data.find('dateFin').text()).end()
				.find('[name=dateEntreeHome]').val($data.find('dateEntreeHome').text()).end()
				.find('.libelleHome').val($data.find('libelleHome').text()).change().end()
				.find('.dessaisissementRevenu').attr('checked', $data.find('DR').text() == 'true').end().find('.csDestinationSortie').val($data.find('csDestinationSortie').text()).end()
			    .find('.isVersementDirect').attr('checked', $data.find('isVersementDirect').text() == 'true').end()
				.find('.libelleAssureurMaladie').val($data.find('libelleAssureurMaladie').text()).change().end()
				.find('[name=idTypeChambre]').val(this.idTypeChambre).attr('disabled', 'disabled').end()
				.find('.toHomeLink').attr('href', 'pegasus?userAction=pegasus.home.home.afficher&selectedId=' + idHome).end()
				.find('.detailPrixChambres').attr('data-id-home',idHome).end()
				.find('.detailPrixChambres').attr('data-g-desc-home',$data.find('libelleHome').text()).end()
				.find('.detailPrixChambres').attr('data-id-chambre',this.idTypeChambre).end()
				.find('.detailPrixChambres').attr('data-dateDebut',$data.find('dateDebut').text()).end()
				.find('.detailPrixChambres').attr('data-dateFin',$data.find('dateFin').text()); 	
		
		
	
		
		// champ.
		this.idEntity = idEntity;
	};

	this.afterStartEdition = function () {
		var jourAppoint = $('[name=dateEntreeHome]');
		if (jourAppoint && !jourAppoint.val()) {
			jourAppoint.prop( "disabled", true );
		}
	};

	this.getParametres = function ($data) {
		return {
			'taxeJournaliereHome.simpleTaxeJournaliereHome.idTypeChambre': this.detail.find('[name=idTypeChambre]').val(),
			'taxeJournaliereHome.simpleTaxeJournaliereHome.dateEcheance': this.detail.find('[name=dateEcheance]').val(),
			'taxeJournaliereHome.simpleTaxeJournaliereHome.idAssureurMaladie': this.detail.find('.idAssureurMaladie').val(),
			'taxeJournaliereHome.simpleTaxeJournaliereHome.idHome': this.detail.find('.idHome').val(),
			'taxeJournaliereHome.simpleTaxeJournaliereHome.isParticipationLCA': this.detail.find('.isParticipationLCA').prop('checked'),
			'taxeJournaliereHome.simpleTaxeJournaliereHome.montantJournalierLCA': this.detail.find('.montantJournalierLCA').val(),
			'taxeJournaliereHome.simpleTaxeJournaliereHome.primeAPayer': this.detail.find('.primeAPayer').val(),
			'taxeJournaliereHome.simpleTaxeJournaliereHome.csDestinationSortie': this.detail.find('.csDestinationSortie').val(),
			'taxeJournaliereHome.simpleDonneeFinanciereHeader.isDessaisissementRevenu': this.detail.find('.dessaisissementRevenu').prop('checked'),
			'taxeJournaliereHome.simpleDonneeFinanciereHeader.dateDebut': this.detail.find('[name=dateDebut]').val(),
			'taxeJournaliereHome.simpleDonneeFinanciereHeader.dateFin': this.detail.find('[name=dateFin]').val(),
			'taxeJournaliereHome.simpleTaxeJournaliereHome.dateEntreeHome': this.detail.find('[name=dateEntreeHome]').val(),
			'taxeJournaliereHome.simpleTaxeJournaliereHome.isDeplafonner' : this.detail.find('.isDeplafonner').prop('checked'),
			'taxeJournaliereHome.simpleTaxeJournaliereHome.montantFraisLongueDuree' : this.detail.find('.montantFraisLongueDuree').val(),
			'taxeJournaliereHome.simpleTaxeJournaliereHome.isVersementDirect' : this.detail.find('.isVersementDirect').is(':checked'),
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
		$elementTable.find('td:eq(3),td:eq(4)').css('text-align', 'right').end().find('td:eq(1),td:eq(2),td:eq(5),td:eq(7),td:eq(8)').css('text-align', 'left').end();
	};

	this.afficheDetail = function () {
		var elementToDisplay = '.lca';
		if (this.detail.find('.isParticipationLCA').prop('checked')) {
			this.detail.find(elementToDisplay).show();
		} else {
			this.detail.find(elementToDisplay).hide();
		}
	};

	this.addEnvent = function () {
		var that = this;
		this.afficheDetail();
		this.detail.find('.isParticipationLCA').click(function () {
			that.afficheDetail();
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
}

$(function () {
	TaxeJournaliereHome.prototype = DonneeFinancierePart.prototype;
	// jsManager.addAfter(function () {
	// $(".prendreEnCompteDansCalcul").notationBubble({wantMarker:false,id:"bubleTemplateCalcule",position:"left"});
	// })

	$('.areaMembre').each(function () {
		var $that = $(this);
		var zone = new TaxeJournaliereHome($that);
		this.zone = zone;
		zone.membreId = $(this).attr('idMembre');
		
		
		
		
		$that.find('.btnAjaxUpdate').click(function () {
			zone.startEdition();
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
		}).end()
		.find('.[name=csTypeLoyer]').change(function () {
			zone.showOrHideDetail();
		}).each(function () {
			zone.showOrHideDetail();
		}).end()
		.find('.btnAjaxValidateNouvellePeriode').click(function () {
			zone.doAddPeriode = true;
			zone.validateEdition();
		}).end()
		.find('.listTypechambre').change(function () {
			
			var idTypeChambre = $('.listTypechambre :selected').val();
			var dateDebut = $('[name=dateDebut]').val();
			var dateFin = $('[name=dateFin]').val();
			
			$that.find('.detailPrixChambres').attr('data-id-chambre',idTypeChambre);
			$that.find('.detailPrixChambres').attr('data-dateDebut',dateDebut);
			$that.find('.detailPrixChambres').attr('data-dateFin',dateFin);
			
		}).end();
		zone.addTypeChambre();


	});
});

/**
 * Fonction qui test si la date d'entrée effective est bien dans le mois précédent la date de début
 */
var validDate = {
	valid: function () {
		var moisEntreeHome = ($('[name=dateEntreeHome]').val().split('.', 2))[1];
		var moisDateDebut = ($('[name=dateDebut]').val().split('.', 1))[0];

		if (moisDateDebut - 1 == moisEntreeHome) {
			return true;
		} else {
			$('.dialog-dateentree').dialog({
				resizable: false,
				autoOpen: false,
				modal: false,
				width: 400,
				height: 100,
				buttons: {
					'ok': function () {
						$(this).dialog('close');
					}
				}
			});
			$('.dialog-dateentree').dialog('open');
		}
	}
};
