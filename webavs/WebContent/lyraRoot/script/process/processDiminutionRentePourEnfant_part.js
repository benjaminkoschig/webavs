
var $selectionnerTout;
var $deselectionnerTout;
var $boutonExecuterProcess;
var $zoneAjaxRentePourEnfant;
var b_isDansMoisActuel = true;
var $messageHorsMoisActuel;

function checkBoutonExecuter(b_horsMoisActuel) {
	if ($.type(b_horsMoisActuel) === 'boolean') {
		b_isDansMoisActuel = !b_horsMoisActuel;
	}

	if (b_isDansMoisActuel) {
		if ($boutonExecuterProcess.is(':not(:visible)')) {
			$boutonExecuterProcess.show();
		}
		$boutonExecuterProcess.button('option', 'disabled', ($('.checkEnfant:checked').length === 0));
		$messageHorsMoisActuel.hide();
	} else {
		$boutonExecuterProcess.hide();
		$messageHorsMoisActuel.show();
	}
}

function ProcessDiminutionRentePourEnfant(mainContainerAjax) {
	this.ACTION_AJAX = "lyra.echeances.rentePourEnfantAjax"; //ACTION_AJAX_EXECUTION
	this.mainContainer = mainContainerAjax;
	this.table = this.mainContainer.find(".areaTable");
	this.detail = this.mainContainer.find(".areaDetail");
	this.n_idEntity = null;

	this.afterRetrieve = function (data) {
	};

	this.getParametresForFind = function () {
		return {
			"moisTraitement": $("#moisTraitement").val()
		};
	};

	this.getParametres = function () {
		var that  = this;
		var o_map = {
			'simpleEntite.isManual': that.isManual,
			'idExecutionProcess:': that.idExecuteProcess,
			'keyProcess': S_NAME_PROCESS,
			'idEntity': that.n_idEntity
		};
		return $.extend(o_map, this.getParametresForFind());
	};

	this.clearFields = function () {
	};

	this.getParentViewBean = function () {
	};

	this.setParentViewBean = function (newViewBean) {
	};

	this.formatTable = function () {
		var $divBoutonExecuter = $('#divExecuterProcess');
		var $divBoutonsToutesAucunes = $('#divBoutonsTableauAjax');
		$divBoutonExecuter.children().detach().appendTo($divBoutonsToutesAucunes).css({
			'position': 'relative',
			'left': '30%'
		});
		$messageHorsMoisActuel.appendTo($divBoutonsToutesAucunes);
		checkBoutonExecuter();
	};

	this.init(function () {
		this.ajaxFind();
		this.sortTable();
	});
}

var declencheursRecherche = {
	$moisActuel: null,
	$moisTraitement: null,
	o_zone: null,

	init: function (o_zone) {
		this.o_zone = o_zone;
		this.$moisActuel = $('#moisActuel');
		this.$moisTraitement = $("#moisTraitement");
		this.addEvent();
	},

	addEvent: function () {
		var that = this;
		this.$moisTraitement.change(function () {
			if (that.$moisTraitement.val() !== '') {
				that.o_zone.ajaxFind();
				if (that.$moisActuel.val() !== '' && that.$moisActuel.val() !== that.$moisTraitement.val()) {
					checkBoutonExecuter(true);
				} else {
					checkBoutonExecuter(false);
				}
			}
		});
	}
};

function getParametresSupplementaires() {
	var o_params = {};

	var s_idRenteADiminuer = '';
	
	$('.checkEnfant:checked').each(function () {
		var $this = $(this);
		s_idRenteADiminuer += $this.attr('idRenteAccordee') + ',';
	});
	
	o_params.idRentesADiminuer = s_idRenteADiminuer.substring(0, s_idRenteADiminuer.lastIndexOf(','));
	
	return o_params;
}

function afterExecute() {
	$zoneAjaxRentePourEnfant.undelegate('diminutionRentePourEnfant');
	$selectionnerTout.button('option', 'disabled', true);
	$deselectionnerTout.button('option', 'disabled', true);
	
	$('.checkEnfant').prop('disabled', true);
}

//héritage par prototype
ProcessDiminutionRentePourEnfant.prototype = AbstractScalableAJAXTableZone;

setTimeout(function () {
	$zoneAjaxRentePourEnfant = $('#zoneAjaxRentePourEnfant');
	$boutonExecuterProcess = $('#executerProcess');
	$boutonExecuterProcess.hide();
	
	$messageHorsMoisActuel = $('#messageHorsMoisActuel');

	var zone = new ProcessDiminutionRentePourEnfant($zoneAjaxRentePourEnfant);
	$zoneAjaxRentePourEnfant.data("zone", zone);
	
	$zoneAjaxRentePourEnfant.find('.btnAjaxUpdate').click(function () {
		zone.startEdition();
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
	}).end();
	
	declencheursRecherche.init(zone);
	
	$selectionnerTout = $('#selectionnerTout');
	$selectionnerTout.button();

	$deselectionnerTout = $('#deselectionnerTout');
	$deselectionnerTout.button();
	
	$zoneAjaxRentePourEnfant.delegate('#selectionnerTout', 'click.diminutionRentePourEnfant', function () {
		$('.rente').addClass('highlight_selected');
		$('.checkEnfant').prop('checked', true);
		checkBoutonExecuter();
	});

	$zoneAjaxRentePourEnfant.delegate('#deselectionnerTout', 'click.diminutionRentePourEnfant', function () {
		$('.rente').removeClass('highlight_selected');
		$('.checkEnfant').removeProp('checked');
		checkBoutonExecuter();
	});

	$zoneAjaxRentePourEnfant.delegate('.rente', 'click.diminutionRentePourEnfant', function (event) {
		var $uneLigne = $(this);
		$uneLigne.toggleClass('highlight_selected');

		var $checkbox = $uneLigne.find('.checkEnfant');
		if (event.target !== $checkbox.get(0)) {
			if ($checkbox.is(':checked')) {
				$checkbox.removeProp('checked');
			} else {
				$checkbox.prop('checked', true);
			}
		}
		checkBoutonExecuter();
	});

	var $divExecuterProcess = $('#divExecuterProcess');	var $msgValidationInterdites = $('#msgValidationInterdites');
		
	if(!globazGlobal.isValidationDecisionAutorise){
			$divExecuterProcess.remove();
			$msgValidationInterdites.show();
	}
}, 100);

