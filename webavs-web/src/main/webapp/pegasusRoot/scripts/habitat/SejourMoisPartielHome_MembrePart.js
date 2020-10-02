function SejourMoisPartielHome (container) {
	// variables
	var that = this;
	this.ACTION_AJAX = ACTION_AJAX_DONNEE_FINANCIERE;
	this.mainContainer = container;
	this.table = this.mainContainer.find(".areaDFDataTable");
	this.detail = this.mainContainer.find(".areaDFDetail");
	this.titleContainer = this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass = "areaDFModified";
	this.membreId = null;

	// functions
	this.afterRetrieve = function ($data, idEntity) {
		this.detail.find('.prixJournalier').val($data.find('prixJournalier').text()).change().end()
				.find('.fraisNourriture').val($data.find('fraisNourriture').text()).change().end()
				.find('.nbJours').val($data.find('nbJours').text()).change().end()
				.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
				.find('[name=dateFin]').val($data.find('dateFin').text()).end();
		
		// champ.
		this.idEntity = idEntity;
		this.isModifFraisGardeCheck();
	};

	this.getParametres = function ($data) {
		return {
			'sejourMoisPartielHome.simpleSejourMoisPartielHome.prixJournalier': this.detail.find('.prixJournalier').val(),
			'sejourMoisPartielHome.simpleSejourMoisPartielHome.fraisNourriture': this.detail.find('.fraisNourriture').val(),
			'sejourMoisPartielHome.simpleSejourMoisPartielHome.nbJours': this.detail.find('.nbJours').val(),
			'sejourMoisPartielHome.simpleDonneeFinanciereHeader.dateDebut': this.detail.find('[name=dateDebut]').val(),
			'sejourMoisPartielHome.simpleDonneeFinanciereHeader.dateFin': this.detail.find('[name=dateFin]').val(),
			'doAddPeriode': this.doAddPeriode,
			'idDroitMembreFamille': this.membreId
		};
	};

	this.clearFields = function () {
		this.detail.clearInputForm();
		
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

	this.modifFraisGarde = function () {
		var fraisNourriture = this.detail.find('.fraisNourriture');
		if (this.detail.find('.isFraisNourriture').prop('checked')) {
			fraisNourriture.prop( "disabled", false );
		} else {
			fraisNourriture.prop( "disabled", true );
			fraisNourriture.val(VAL_FRAIS_NOURRITURE);
		}
	};

	this.isModifFraisGarde = function () {
		var fraisNourriture = this.detail.find('.fraisNourriture');
		var isFraisNourriture = this.detail.find('.isFraisNourriture');
		if(fraisNourriture.val() ==VAL_FRAIS_NOURRITURE) {
			fraisNourriture.prop( "disabled", true );
		} else {
			fraisNourriture.prop( "disabled", false );
		}
	}

	this.isModifFraisGardeCheck = function () {
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
			that.modifFraisGarde();
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
	SejourMoisPartielHome.prototype = DonneeFinancierePart.prototype;

	$('.areaMembre').each(function () {
		var $that = $(this);
		var zone = new SejourMoisPartielHome($that);
		this.zone = zone;
		zone.membreId = $(this).attr('idMembre');
		zone.addEvent();
		$that.find('.btnAjaxUpdate').click(function () {
			zone.startEdition();
			zone.isModifFraisGarde();
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
			zone.isModifFraisGardeCheck();
			zone.isModifFraisGarde();
		}).end()
		.find('.btnAjaxValidateNouvellePeriode').click(function () {
			zone.doAddPeriode = true;
			zone.validateEdition();
		}).end()
		;

	});
});
