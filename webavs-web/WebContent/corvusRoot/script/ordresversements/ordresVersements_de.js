
//fonctions de bases à redéfinir

function add () {
}

function upd() {
}

function validate() {
}

function cancel() {
}

function del() {
}

function init () {
}

var getParametresSupplementaires;
var afterExecute;

function OrdreVersement(container, 
						$montantPrestationsDues, 
						$montantPrestationsDejaVersees, 
						$montantInteretsMoratoires, 
						$montantCreanciers, 
						$montantSolde,
						$banniereModificationInterdite,
						$banniereRetenuePresenteMaisModificationInterdite,
						n_idOrdreVersement,
						n_idRenteVerseeATort) {

	this.ACTION_AJAX = "corvus.ordresversements.ordresVersementsAjax";
	this.mainContainer = container;
	this.table = this.mainContainer.find(".areaTable");
	this.detail = this.mainContainer.find(".areaDetail");
	this.$montantPrestationsDues = $montantPrestationsDues;
	this.$montantPrestationsDejaVersees = $montantPrestationsDejaVersees; 
	this.$montantInteretsMoratoires = $montantInteretsMoratoires;
	this.$montantCreanciers = $montantCreanciers;
	this.$montantSolde = $montantSolde;
	this.$banniereModificationInterdite = $banniereModificationInterdite;
	this.$banniereRetenuePresenteMaisModificationInterdite = $banniereRetenuePresenteMaisModificationInterdite;

	this.afterRetrieve = function (data) {
		var that = this;

		this.clearDetail();
		var $contenu = $(data.find('contenu').toXML());
		notationManager.addNotationOnFragment($contenu);
		this.detail.empty();
		this.detail.append($contenu);
		$contenu.find('.bouton').button();
		this.showDetail();

		var $provenance = this.detail.find('#provenance');

		if ($provenance.val() === 'CID') {
			var $spanMontantDisponible = $('#montantDisponible');
			var $inputMontantCompense = $('#montantCompense');
			var n_montantDisponible = 0.0;
			var n_soldeDecisionDeficitaire = $('#soldeDecisionDeficitaire').val();

			if ($('#idCompensationInterDecision').val()) {
				// si modification, le montant disponible est celui du solde de la décision déjà choisie (impossible de changer cette décision)
				n_montantDisponible = globazNotation.utilsFormatter.formatStringToAmout($spanMontantDisponible.text());

				// formattage du montant disponible (pour avoir les apostrophes si plus grand que mille)
				$spanMontantDisponible.text(n_montantDisponible);
			} else {
				this.selectedEntityId = 0;
				// si ajout, gestion dynamique du montant disponible en fonction de la décision qui sera choisie dans la liste
				$('#choixDecision').change(function () {
					var $this = $(this);
					n_montantDisponible = $this.find('option:selected').attr('montantDisponible');
					$spanMontantDisponible.text(globazNotation.utilsFormatter.formatStringToAmout(n_montantDisponible));
					$inputMontantCompense.val(n_montantDisponible).change();
				});
			}

			$inputMontantCompense.change(function () {
				that.validerCID(n_montantDisponible, n_soldeDecisionDeficitaire, $(this));
			}).keydown(function (event) {
				if (event.which == 13) {
					that.validerCID(n_montantDisponible, n_soldeDecisionDeficitaire, $inputMontantCompense);
					event.preventDefault();
					that.validateEdition();
				}
			});
			setTimeout(function () {
				$inputMontantCompense.focus().select();
			}, 200);
		} else if ($provenance.val() === 'OV') {
			var $checkboxIsCompense = $('#isCompense');
			var $montantCompense = $('#montantCompense');
			var n_montantDette = $('#montantDette').val();
			var $spanMontantDette = $('#spanMontantDette');

			$spanMontantDette.text(globazNotation.utilsFormatter.formatStringToAmout(n_montantDette));
			$checkboxIsCompense.change(function () {
				var $this = $(this);
				if ($this.is(':checked')) {
					$montantCompense.prop('disabled', false);
					$montantCompense.val($('#montantDette').val());
					$montantCompense.focus().select();
				} else {
					$montantCompense.val('0.00');
					$montantCompense.prop('disabled', true);
				}
			}).keydown(function (event) {
				if (event.which == 13) {
					event.preventDefault();
					that.validateEdition();
				}
			});
			$montantCompense.prop('disabled', !$checkboxIsCompense.is(':checked'));
			$montantCompense.change(function () {
				that.validerOV($montantCompense, n_montantDette);
			}).keydown(function (event) {
				if (event.which == 13) {
					that.validerOV($montantCompense, n_montantDette);
					event.preventDefault();
					that.validateEdition();
				}
			});
			setTimeout(function () {
				$montantCompense.focus().select();
			}, 200);
		} else if ($provenance.val() === 'Restitution') {

			this.selectedEntityId = $('#idSoldePourRestitution').val();

			var $spanMontantARecouvrer = $('#spanMontantARecouvrer');
			var n_montantARecouvrer = $('#montantARecouvrer').val();

			var $spanMontantPrestationAccordee = $('#spanMontantPrestationAccordee');
			var n_montantPrestationAccordee = $('#montantPrestationAccordee').val();

			$spanMontantARecouvrer.text(globazNotation.utilsFormatter.formatStringToAmout(n_montantARecouvrer));
			$spanMontantPrestationAccordee.text(globazNotation.utilsFormatter.formatStringToAmout(n_montantPrestationAccordee));

			var $montantRetenueMensuelle = $('#montantRetenueMensuelle');

			var n_codeSystemeRetenueMensuelle = $('#codeSystemeRetenueMensuelle').val();
			var n_codeSystemeRestitution = $('#codeSystemeRestitution').val();
			var $elementsPourRetenue = $('.retenueMensuelle');
			var $elementsPourRestitution = $('.restitution');

			function afficerLesBonsElements ($selecteurCodeSysteme, b_firstTime) {
				var n_codeSystemeChoisi = $selecteurCodeSysteme.val();
				if (n_codeSystemeRestitution === n_codeSystemeChoisi) {
					$elementsPourRestitution.show();
					$elementsPourRetenue.hide();
					$montantRetenueMensuelle.val(0.0);
				} else if (n_codeSystemeRetenueMensuelle === n_codeSystemeChoisi) {
					$elementsPourRetenue.show();
					$elementsPourRestitution.hide();
					if (!b_firstTime) {
						$montantRetenueMensuelle.val(n_montantPrestationAccordee);
					}
				} else {
					$elementsPourRestitution.hide();
					$elementsPourRetenue.hide();
					$montantRetenueMensuelle.val(0.0);
				}
			}

			var $selecteurTypeSoldePourRestitution = $('#csTypeSoldePourRestitution');
			$selecteurTypeSoldePourRestitution.change(function () {
				afficerLesBonsElements($(this));
				that.validerRestitution(n_montantARecouvrer, n_montantPrestationAccordee, $montantRetenueMensuelle);
			});
			afficerLesBonsElements($selecteurTypeSoldePourRestitution, true);

			$montantRetenueMensuelle.change(function () {
				that.validerRestitution(n_montantARecouvrer, n_montantPrestationAccordee, $montantRetenueMensuelle);
			}).keydown(function (event) {
				if (event.which == 13) {
					that.validerRestitution(n_montantARecouvrer, n_montantPrestationAccordee, $montantRetenueMensuelle);
					event.preventDefault();
					that.validateEdition();
				}
			});
			setTimeout(function () {
				$montantRetenueMensuelle.focus().select();
			}, 200);
		}
	};

	this.validerOV = function ($montantCompense, n_montantDette) {
		if ($montantCompense.val() == '') {
			$montantCompense.val(0.0);
		}
		if ($montantCompense.val() * 1.0 > n_montantDette * 1.0) {
			$montantCompense.val(n_montantDette);
		}
	};

	this.validerCID = function (n_montantDisponible, n_soldeDecisionDeficitaire, $inputMontantCompense) {
		var n_montantFormatte = globazNotation.utilsFormatter.amountTofloat($inputMontantCompense.val());
		if ((1.0 * n_montantFormatte) > (1.0 * n_soldeDecisionDeficitaire)) {
			$inputMontantCompense.val(n_soldeDecisionDeficitaire).change();
		}
		if ((1.0 * n_montantFormatte) > (1.0 * globazNotation.utilsFormatter.amountTofloat(n_montantDisponible))) {
			$inputMontantCompense.val(n_montantDisponible).change();
		}
	};

	this.validerRestitution = function (n_montantARecouvrer, n_montantPrestationAccordee, $montantRetenueMensuelle) {
		var n_montantModifie = globazNotation.utilsFormatter.amountTofloat($montantRetenueMensuelle.val());
		var n_plafond = 0;

		if ((1.0 * n_montantARecouvrer) > (1.0 * n_montantPrestationAccordee)) {
			n_plafond = n_montantPrestationAccordee;
		} else {
			n_plafond = n_montantARecouvrer;
		}

		if (n_montantModifie) {
			if ((1.0 * n_montantModifie) > n_plafond) {
				$montantRetenueMensuelle.val(n_plafond);
			} else if ((1.0 * n_montantModifie) < 0) {
				$montantRetenueMensuelle.val(n_plafond);
			}
		}
	};

	this.getParametresForFind = function () {
		return {
			"searchModel.forIdPrestation": $("#forIdPrestation").val(),
			"idDecision": $('#idDecision').val()
		};
	};

	this.getParametres = function () {
		var o_map = {
			'idEntity': $('#idOrdreVersement').val(),
			'idOrdreVersement': $('#idOrdreVersement').val(),
			'montantCompense': $('#montantCompense').val(),
			'provenance': $('#provenance').val(),
			'compense': $('#isCompense').is(':checked'),
			'idDecisionPonctionne': $('#choixDecision').val(),
			'idDecisionDeficitaire': $('#idDecisionDeficitaire').val(),
			'idCompensationInterDecision': $('#idCompensationInterDecision').val(),
			'idSoldePourRestitution': $('#idSoldePourRestitution').val(),
			'idDecision': $('#idDecision').val(),
			'csTypeSoldePourRestitution': $('#csTypeSoldePourRestitution').val(),
			'montantRetenueMensuelle': $('#montantRetenueMensuelle').val()
		};
		return $.extend(o_map, this.getParametresForFind());
	};

	this.afterStopEdition = function () {
		$('#zoneEditionOrdreVersement').empty().hide();
	};

	this.onUpdateAjaxComplete = function (data) {

		if (this.hasError(data)) {
			ajaxUtils.displayError(data);
			ajaxUtils.afterAjaxComplete(this.mainContainer);
			return;
		}

		$('#zoneEditionOrdreVersement').empty().hide();

		this.ajaxFind();
		this.sortTable();
	};

	this.clearFields = function () {
	};

	this.getParentViewBean = function () {
	};

	this.setParentViewBean = function (newViewBean) {
	};

	this.formatTable = function () {
		this.clearDetail();
	};

	// initialization
	this.init(
		function () {
			this.addTableEvent();
			this.ajaxFind();
			this.sortTable();
		}
	);

	this.clearDetail = function () {
		this.detail.contents().remove();
	};

	this.superOnFindAjaxComplete = this.onFindAjaxComplete;

	this.onFindAjaxComplete = function (data, n_idEntity) {
		this.superOnFindAjaxComplete(data, n_idEntity);

		var $tree = $(data);
		if (this.hasError(data)) {
			return;
		}

		var $contenu = $tree.find('contenu');

		this.$montantPrestationsDues.text($.trim($contenu.find('montantPrestationsDues').text()));
		this.$montantPrestationsDejaVersees.text($.trim($contenu.find('montantPrestationsDejaVersees').text()));
		this.$montantInteretsMoratoires.text($.trim($contenu.find('montantInteretsMoratoires').text()));
		this.$montantCreanciers.text($.trim($contenu.find('montantCreanciers').text()));
		this.$montantSolde.text($.trim($contenu.find('montantSolde').text()));

		if ($contenu.find('modificationPossible').text().replace(/\s+/g, '') == 'true') {
			$banniereModificationInterdite.hide();
			$('#boutonCID').button();
			$('#boutonRestitution').button();
			if ($contenu.find('retenuePresenteMaisModificationInterdite').text().replace(/\s+/g, '') == 'true') {
				$banniereRetenuePresenteMaisModificationInterdite.show();
			} else {
				$banniereRetenuePresenteMaisModificationInterdite.hide();
			}
		} else {
			$banniereModificationInterdite.show();
			$banniereRetenuePresenteMaisModificationInterdite.hide();
		}
	};
}

var ordreVersement = {
	o_zone: null,

	init: function (o_zone) {
		this.o_zone = o_zone;
	}
};

// héritage par prototype
OrdreVersement.prototype = AbstractScalableAJAXTableZone;

$(document).ready(function () {
	var $this = $(this);

	var $banniereModificationInterdite = $('#banniereModificationInterdite');
	$banniereModificationInterdite.hide();

	var $banniereRetenuePresenteMaisModificationInterdite = $('#banniereModificationRestitutionInterdite');
	$banniereRetenuePresenteMaisModificationInterdite.hide();

	var $montantPrestationsDues = $('#montantPrestationsDues');
	var $montantPrestationsDejaVersees = $('#montantPrestationsDejaVersees'); 
	var $montantInteretsMoratoires = $('#montantInteretsMoratoires');
	var $montantCreanciers = $('#montantCreanciers');
	var $montantSolde = $('#montantSolde');

	var $zoneAjaxOrdreVersement = $('#zoneAjaxOrdreVersement');
	var zone = new OrdreVersement(	$zoneAjaxOrdreVersement, 
									$montantPrestationsDues, 
									$montantPrestationsDejaVersees, 
									$montantInteretsMoratoires, 
									$montantCreanciers,
									$montantSolde,
									$banniereModificationInterdite,
									$banniereRetenuePresenteMaisModificationInterdite);

	$this.on('click', '.btnAjaxUpdate', function () {
		zone.startEdition();
	}).on('click', '.btnAjaxCancel', function () {
		zone.stopEdition();
	}).on('click', '.btnAjaxValidate', function () {
		if (notationManager.validateAndDisplayError()) {
			zone.validateEdition();
		}
	}).on('click', '.btnAjaxDelete', function () {
		zone.ajaxDeleteEntity();
	}).on('click', '#boutonCID', function () {
		$.ajax({
			data: {
				'userAction': 'corvus.ordresversements.ordresVersementsAjax.creerCIDAJAX',
				'idDecisionDeficitaire': $('#idDecision').val(),
				'soldeDecisionDeficitaire': $('#montantTotalSolde').val()
			},
			url: globazGlobal.corvusPath,
			success: function (data) {
				if (ajaxUtils.hasError(data)) {
					zone.displayError(data);
				} else {
					zone.onLoadAjaxData(data);
				}
			},
			type: 'GET'
		});
	}).on('click', '#boutonRestitution', function () {
		$.ajax({
			data: {
				'userAction': 'corvus.ordresversements.ordresVersementsAjax.gererRestitutionAJAX',
				'idDecision': $('#idDecision').val()
			},
			url: globazGlobal.corvusPath,
			success: function (data) {
				if (ajaxUtils.hasError(data)) {
					zone.displayError(data);
				} else {
					zone.onLoadAjaxData(data);
				}
			},
			type: 'GET'
		});
	});

	ordreVersement.init(zone);
});

ajaxUtils.displayError = function (data) {
	var $data = null,
	$error = null,
	$json = null,
		s_error = null,
		s_errorJson = null;

	if (!$.isPlainObject(data)) {
		$data = $(data);
		
		$error = $data.find('error');

		if ($error.attr('errorPage')) {
			$json = $data.find('json');
			s_errorJson = $json.find('exception').text();
			if (s_errorJson) {
				data = $.parseJSON(s_errorJson);
			}
		} else {
			s_error = $error.text();
		s_errorJson = $data.find('errorJson').text();
		if (s_errorJson) {
			data = $.parseJSON(s_errorJson);
		}
	}
	}

	if ($.isPlainObject(data)) {
		// on n'est dans le cas d'une exception
		if (data.errorBean) {
			s_error = ajaxUtils.renderDialogExceptionError(data.errorBean);
		} else if (data.messages && data.exceptions && data.stack) {
			s_error = ajaxUtils.renderDialogExceptionError(data);
		} else if (data.messages) {
			var logsRender = {};
			logsRender.logs = ajaxUtils.getLogsFromLevel(data.messages, 3);
			logsRender.clazz = 'error';

			logsRender.logs = ajaxUtils.uniqueMessage(logsRender.logs, 'message');

			s_error = ajaxUtils.renderLogs(logsRender);
		}
	}

	if (!s_error || !s_error.length) {
		s_error = data;
		if (data && $.isPlainObject(data)) {
			s_error = data.error;
			if ($.trim(s_error).length === 0 && data.viewBean) {
				s_error = data.viewBean.message;
			}
		}
	}

	if (s_error) {
		globazNotation.utils.consoleError(s_error);
	}
	$('.mainContainerAjax').find('.loading_horizonzal').remove();
};
