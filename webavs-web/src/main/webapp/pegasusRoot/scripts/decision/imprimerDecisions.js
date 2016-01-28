var $headerChk, $submitPrintButton, $form, $decisionsTable;


var submitForm = function () {
	$("[name=userAction]").val(userAction);
	$form.submit();
};
// Set l'état des boutons radios
var setAllDecisionsState = function (state) {

	$decisionsTable.find('.chkPrint').each(function () {
		$(this).attr('checked', state);
		// Si décocher on desactive le bouton imprimer
		if (!state) {
			$submitPrintButton.hide();
		} else {
			$submitPrintButton.show();
		}
	});

};

var setDecisionsIdValuesForSubmitting = function () {
	// iteration sur les cases à cocher
	$decisionsTable.find('.chkPrint').each(function () {
		var $tr = $(this).closest('tr');
		// Si la case est cocher on set l'id dans le champ caché
		if ($(this).is(':checked')) {
			$('[name=decisionsId]', $tr).attr('value', $tr.attr('id'));
		} else {
			$('[name=decisionsId]', $tr).remove();
		}
	});
};

var isAllBoutonsUnchecked = function () {
	var allUnchecked = true;
	$decisionsTable.find('.chkPrint').each(function () {
		// Si la case est cocher on set l'id dans le champ caché
		if ($(this).is(':checked')) {
			allUnchecked = false;
		}
	});
	return allUnchecked;
};


// initialisation des évenements
var initEvents = function () {
	// on cache la zone d'information du process
	$('#infoProcess').hide();
	// boutons radio header tableau
	var headerChkState = false;
	// Clic checkbox header
	$headerChk.click(function () {
		setAllDecisionsState($headerChk.is(':checked'));
	});

	// Click sur les boutons radios en
	$decisionsTable.find('.chkPrint').each(function () {
		$(this).click(function () {
			// Si unchek, desactiver radio hedaer
			if (!($(this).is(':checked'))) {
				$headerChk.attr('checked', false);
			}

			if (isAllBoutonsUnchecked()) {
				$submitPrintButton.hide();
			} else {
				$submitPrintButton.show();
			}
		});
	});
	// Clic imprimer selection
	$submitPrintButton.click(function () {
		setDecisionsIdValuesForSubmitting();
		$(this).hide();
		$('#infoProcess').show();
		setTimeout(submitForm, 2500);
	});
};


$(function () {
	// Initialisation des variables jquery
	$headerChk = $('#headerCheckBox');
	$submitPrintButton = $('#printDec');
	$form = $("[name=mainForm]");
	$decisionsTable = $('#decisionsTable');
	initEvents();
});