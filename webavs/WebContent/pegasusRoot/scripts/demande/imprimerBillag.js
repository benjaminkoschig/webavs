
$(function (){
	//Initialisation des variables jquery
	$submitPrintButton = $('#printDec');
	$form = $("[name=mainForm]");
});

//initialisation des évenements
var initEvents = function () {
	//on cache la zone d'information du process
	$('#infoProcess').hide();
	
	//Clic imprimer selection
	$submitPrintButton.click(function () {
		
		if(validInputs()){
			$(this).hide();
			$('#infoProcess').show();
			setTimeout(submitForm, 2500);
		}
		
	});
	
};


var submitForm = function () {
	$("[name=userAction]").val(userAction);
	$form.submit();
};
