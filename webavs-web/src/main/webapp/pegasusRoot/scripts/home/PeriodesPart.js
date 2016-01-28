
// namespace for all the scripts about the AJAX code for periodes
function PeriodesPart (container) {
	// variables
	var that = this;

	this.ACTION_AJAX = ACTION_AJAX_PERIODE;
	this.mainContainer = container;
	this.table = this.mainContainer.find(".areaPeriodeDataTable");
	this.detail = this.mainContainer.find(".areaPeriodeDetail");
	//this.modifiedZoneClass = "areaDFModified";
 
	this.membreId = null;

	// functions

	this.afterRetrieve = function ($data) {
		this.detail.find('[name=csService]').val($data.find('csEtatService').text()).end()
			.find('#isReconnu').attr('checked', $data.find('isReconnu').text() === "true").end()
			.find('#dateDebut').val($data.find('dateDebut').text()).end()
			.find('#dateFin').val($data.find('dateFin').text()).end();
	};
	
	this.getParametresForFind = function () {
		return {"searchModel.forIdHome": PAGE_ID_HOME};
	};

	this.getParametres = function () {
		return {
			"periodeServiceEtat.simplePeriodeServiceEtat.dateDebut": this.detail.find('#dateDebut').val(),
			"periodeServiceEtat.simplePeriodeServiceEtat.dateFin": this.detail.find('#dateFin').val(),
			"periodeServiceEtat.simplePeriodeServiceEtat.isReconnu": this.detail.find('#isReconnu').prop('checked'),
			"periodeServiceEtat.simplePeriodeServiceEtat.csServiceEtat": this.detail.find('[name=csService]').val(),
			"periodeServiceEtat.simplePeriodeServiceEtat.idHome": PAGE_ID_HOME
		};
	};

	this.clearFields = function () {
		this.detail.find('#dateFin,#dateDebut,#csService').val('');
		this.detail.find('#isReconnu').prop('checked', false);
	};

	this.getParentViewBean = function () {
		return home;
	};
	this.setParentViewBean = function (newViewBean) {
		home = newViewBean;
	};

	this.init(function () {
		this.addTableEvent();
		this.colorTableRows();
		//Pour afficher par défaut la zone détail
		this.detail.show();
		this.getBtnContainer().show();
		this.stopEdition();
		//this.createPagination(2, [5, 10, 15]);
		//this.sortTable();
	});
}

// periodesPart extends AbstractSimpleAJAXTableZone
PeriodesPart.prototype = AbstractScalableAJAXTableZone;

// fonction d'initialisation de la page lorsque JQuery est prêt
$(function () {
	var periodesPart = new PeriodesPart($('.areaPeriodes'));

	$('.areaPeriodes .btnAjaxUpdate').click(function () {
		periodesPart.startEdition();
	});
	$('.areaPeriodes .btnAjaxCancel').click(function () {
		periodesPart.stopEdition();
	});
	$('.areaPeriodes .btnAjaxValidate').click(function () {
		periodesPart.validateEdition();
	});
	$('.areaPeriodes .btnAjaxDelete').click(function () {
		periodesPart.ajaxDeleteEntity(periodesPart.selectedEntityId);
	});
	$('.areaPeriodes .btnAjaxAdd').click(function () {
		periodesPart.stopEdition();
		periodesPart.startEdition();
	});

});
