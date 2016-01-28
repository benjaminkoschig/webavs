/**
 *
 * Idem document ready
 * 
 */
$(document).ready(function() {
	//gestion du select selection processus au départ (modifiable que si récap par défaut pas attachée)
	if($('input[name="numProcessus"]').val()!='0'){
		$('#numProcessus').attr("readonly", true); 
	}
	
	$( "#noFactureValue" ).blur(function() {
		//alert('numFacture');
		
		if(checkIfValueInSelect(this.value,"noFactureSelection")){
			$("#noFactureSelection").val(this.value);
			updateProcessusAccordingNumFacture();
		}
		else{
			$("#numProcessus").val("0");
		}
	});
	
	// Réaction sur le changement de n° facture
	// ----------------------------------------------------------------------
	$('#noFactureSelection').change(function (e){	
		//alert('change detection');
		$( "#noFactureValue").val(this.value);		
		updateProcessusAccordingNumFacture();
		
	});
	
	
});
/**
 * met à jour le processus lié selon le numéro de récap sélectionné dans la liste
 */
function updateProcessusAccordingNumFacture(){
	
	var processusLieIdField = $("#noFactureSelection option:selected").attr("al_process");
	//alert('updateProcessusAccordingNumFacture 2:'+processusLieIdField);
	//alert('updateProcessusAccordingNumFacture 3:'+$("#"+processusLieIdField).val());
	
	if($("#"+processusLieIdField).val()!='0')
		displayLinkedProcessus($("#"+processusLieIdField).val());
	else
		$("#numProcessus").val($("#"+processusLieIdField).val()); 
	
}
/**
 * Vérifie si une value existe dans un select
 * @param value
 * @param idSelect
 * @returns {Boolean}
 */
function checkIfValueInSelect(value, idSelect){
	
	var isIn = false;
	//alert('check if '+value+' idSelect');
	$('#'+idSelect+' option').each(function(i, option){
		if (value == $(option).val())
			isIn = true;			
		});

	//alert('check :'+isIn);
	return isIn;
	
}

function displayLinkedProcessus(idProcessus){
	$("#numProcessus").val(idProcessus); 
	$('#numProcessus').attr("readonly", true); 
	
}