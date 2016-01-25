$(document).ready(function () {
	var $moisTraitement = $("#moisTraitement");
	var $tdMoisTraitement = $('#tdMoisTraitement');

	$tdMoisTraitement.children().hide();

	var $spanMoisTraitement = $('<span>');
	var $strongMoisTraitement = $('<strong>');
	$strongMoisTraitement.text($moisTraitement.val());
	$spanMoisTraitement.append($strongMoisTraitement);
	$tdMoisTraitement.append($spanMoisTraitement);

	var $divExecuterProcess = $('#divExecuterProcess');
	var $msgValidationInterdites = $('#msgValidationInterdites');

	if(globazGlobal.isValidationDecisionAutorise === false){
		$divExecuterProcess.hide();
		$msgValidationInterdites.show();
	} else {
		$msgValidationInterdites.hide();
	}
});
