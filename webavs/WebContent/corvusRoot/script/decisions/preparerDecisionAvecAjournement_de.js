var $checkBoxEditionDuDocument,
	$elementsAffichesSiEditionDuDocument,
	$boutonOk,
	$imageOk;

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

function init() {
}

function afficherOuMasquerSelonCheckbox() {
	if ($checkBoxEditionDuDocument.is(':checked')) {
		$elementsAffichesSiEditionDuDocument.show();
	} else {
		$elementsAffichesSiEditionDuDocument.hide();
	}
}

$(document).ready(function () {
	$checkBoxEditionDuDocument = $('#editionDuDocument');
	$elementsAffichesSiEditionDuDocument = $('.afficherSiEditionDuDocument');

	afficherOuMasquerSelonCheckbox();
	$checkBoxEditionDuDocument.change(afficherOuMasquerSelonCheckbox);

	$imageOk = $('#imageOK').hide();

	$boutonOk = $('#boutonOk').button();
	$boutonOk.click(function () {
		$boutonOk.button({'disabled': true});

		var o_dataProcess = {
			'adresseEmailGestionnaire': $('#adresseEmailGestionnaire').val(),
			'idDemandeRente': $('#idDemandeRente').val(),
			'editionDuDocument': $('#editionDuDocument').is(':checked'),
			'dateSurLeDocument': $('#dateSurLeDocument').val(),
			'miseEnGed': $('#miseEnGed').is(':checked')
		};

		ajaxUtils.ajaxExecut('corvus.decisions.preparerDecisionAvecAjournement', ajaxUtils.addNoCache(o_dataProcess), function () {
			// callback quand tout s'est bien passé
			$imageOk.fadeIn();
		}, function () {
			// callback en cas d'erreur
			$boutonOk.button({'disabled': false});
		});
	});
});

$('html').bind(eventConstant.JADE_FW_ACTION_DONE, function () {
	readOnly(false);
});
