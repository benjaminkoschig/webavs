/**
 * Recheche si le Rc contient des champ de recherche avec des valeurs remplies
 * A l'exclusion, des champs hidden, button et submit
 * @returns etat des champs
 */
var isRCsSearchFielsdSet = function (){
	var b_fieldsSet = false;
	
	$(':input:not(:hidden):not(:button):not(:submit)').each(function (){
		if(this.value!==''&&this.value!=='0'&&this.value!==0){
			b_fieldsSet = true;
			
		}
	});
	return b_fieldsSet;
}