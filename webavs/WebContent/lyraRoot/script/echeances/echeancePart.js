
var getParametresSupplementaires;
var afterExecute;

function Echeance(container) {
	this.ACTION_AJAX = "lyra.echeances.echeanceAjax"; //ACTION_AJAX_EXECUTION
	this.mainContainer = container;
	this.table = this.mainContainer.find(".areaTable");
	this.detail = this.mainContainer.find(".areaDetail");
	this.n_idEntity = null;
	this.$triParOrdre = $('#triParOrdre');

	this.afterRetrieve = function (data) {
		var that = this;

		this.clearDetail();
		var $contenu = $(data.find('contenu').toXML());

		notationManager.addNotationOnFragment($contenu);

		this.detail.append($contenu);
		
		$('#imageProcessExecute').hide();

		var $boutonExecuterProcess = $('#executerProcess');
		if($boutonExecuterProcess.length) {
			$boutonExecuterProcess.button();
			$boutonExecuterProcess.one('click', function () {
				$boutonExecuterProcess.button('option', 'disabled', true);
	
				var o_dataProcess = {
					'processPath': $('#processPath').val(),
					'adresseEmail': $('#adresseEmail').val(),
					'moisTraitement': $('#moisTraitement').val()
				};
	
				if ($.type(getParametresSupplementaires) === 'function') {
					var parametresSupplementaires = getParametresSupplementaires();
					o_dataProcess[$('#processPath').val()] = ajaxUtils.jsonToString(parametresSupplementaires);
				}
	
				o_dataProcess = ajaxUtils.addNoCache(o_dataProcess);
				ajaxUtils.ajaxExecut('lyra.process.process', o_dataProcess, function () {
					that.processExecuted();
				});
	
				if ($.type(afterExecute) === 'function') {
					afterExecute();
				}
			});
		}
	};

	this.getParametresForFind = function () {
		return {
			"searchModel.forCsDomaineApplicatif": $("#forDomaineApplicatif").val()
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
		this.clearDetail();
		$('.echeance').each(function () {
			var $this = $(this);
			var s_idEcheance = $this.find('.idEcheance').val();
			$this.attr('idEntity', s_idEcheance);
		});
		if (this.$triParOrdre.hasClass('sorted-asc')) {
			this.$triParOrdre.click();
		}
		this.$triParOrdre.click();
	};

	// initialization
	this.init(
		function () {	
			this.addTableEvent();
			this.ajaxFind();
			this.sortTable();
			this.$triParOrdre.click();
		}
	);

	this.clearDetail = function () {
		this.detail.contents().remove();
	};

	this.processExecuted = function () {
		$('#imageProcessExecute').fadeIn();
		
		globazNotation.utilsInput.disableInputs($('#adresseEmail,#moisTraitement'), false);
	};

	this.processError = function () {
		$('#executerProcess').button();
	};
}

var declencheursRecherche = {
	$boutonRechercher: null,
	$elementsDeRecherche: null,
	o_zone: null,

	init: function (o_zone) {
		this.o_zone = o_zone;
		this.$elementsDeRecherche = $(".elementDeRecherche");
		this.$boutonRechercher = $("#forDomaineApplicatif");
		this.addEvent();
	},

	addEvent: function () {
		var that = this;
		this.$boutonRechercher.change(function () {
			that.o_zone.ajaxFind();
		});

		this.$elementsDeRecherche.keyup(function (event) {
			event.keyCode = event.keyCode ? event.keyCode : event.which;
			if (event.keyCode === 13) {
				that.o_zone.ajaxFind();
			}
		});
	}
};

// héritage par prototype
Echeance.prototype = AbstractScalableAJAXTableZone;

$(document).ready(function () {
	var $this = $('#zoneAjaxEcheance');

	var zone = new Echeance($this);
	$this.data("zone", zone);
	
	$this.find('.btnAjaxUpdate').click(function () {
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
});