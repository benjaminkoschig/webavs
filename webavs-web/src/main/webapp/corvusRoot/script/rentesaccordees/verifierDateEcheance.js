function verifyDateEcheanceValidity(){
	
	var groupLevel = $('#groupLevelRequerant').val();
	//si le grouplevel est 5, on fait le contrôle de l'âge du tiers 
	if(groupLevel == 5){
		var dateEcheance = $('#dateEcheance').val();
		var dateNaissanceRequerant = $('#dateNaissanceTiers').val();
		
		var moisNaissance = dateNaissanceRequerant.substring(3,5);
		//si le mois de naissance n'est pas fourni, on le remplace par 06 par défaut.
		if(moisNaissance == "00"){
			moisNaissance = "06";
		}
		var anneeNaissance = dateNaissanceRequerant.substring(6,10);
		var annee25ans = parseInt(anneeNaissance) + 25;
		var moisDateEcheance = dateEcheance.substring(0,2);
		var anneeDateEcheance = dateEcheance.substring(3,7);
		var placeWarning = false;
		
		if(anneeDateEcheance > annee25ans){
			placeWarning = true;
		}
        
		if((anneeDateEcheance == annee25ans) && (moisDateEcheance > moisNaissance)){
			placeWarning = true;
		}
		
		if(placeWarning){
			var warningToPrint = $('#warningAge25ans').val().replace("{0}", moisNaissance + "." + annee25ans);
			$('#dateEcheance').val(moisNaissance + "." + annee25ans);
			globazNotation.utils.consoleWarn(warningToPrint,jQuery.i18n.prop('notation.avertissement.message'),true);
		}
		
	}
	
}
