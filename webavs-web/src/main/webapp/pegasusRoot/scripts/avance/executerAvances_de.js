


var dealGui = function () {
	
	//Si retour process
	if(fromProcess){
		//on cache le gif
		$('#processRunningGif').hide();
		
		//process ok
		if(processOk){
			//on affiche l'état ok
			$('#processRunningLbl').html(lblProcessOk);
			$('#lancer').hide();
		}
		//process ko
		else{
			//on affiche l'état ko
			$('#processRunningLbl').html(lblProcessKo);
			$('#lancer').button();
		}
		//on affiche la zone process state
		$('#processRunning').show();
	}
	//Sinon traitement standard
	else{
		//bouton process
		$('#lancer').button();
		$('#processRunning').hide();
		$('.dateValeurFields').hide();
		$('#zone_og').show();
	}
	
	
	
};
/**
 * On set les champs nécessaire en read only
 */
var dealReadOnlyFields = function () {
	//date valeur
	$('#valDateValeur').attr('disabled','disabled');
};

/**
 *  */
var dealInputEvents = function () {
	//date valeur seulement ave liste
	$('[name=csTypeAvance]').change(function () {
		var selectedValue = $('[name=csTypeAvance] option:selected').attr('value');
		if(selectedValue==="52854002"){
			$('.dateValeurFields').hide();
			$('#zone_og').show();
		}else{
			$('.dateValeurFields').show();
			$('#zone_og').hide();
		}
	});
	
	//ok, lancement process
	$('#lancer').click(function () {
		if(validate()){
			action(COMMIT);
			$('#lancer').hide();
			$('#processRunning').show();
			
		}
	})
	
}