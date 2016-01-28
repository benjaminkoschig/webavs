globazNotation.utilsString = {
	author: 'ECO',
	forTagHtml: 'Utils',
	type: globazNotation.typesNotation.UTILITIES,

	description: "Regroupement de fonction utilitaire pour le traitement des strings",

	/*
	 * Fonction qui convertit une date format Globaz[jj.mm.aaaa] en un objet Date JS
	 */
	toProperCase: function (input) {
		if(input==null || (typeof input !="string")){
			return null;
		}

		return input.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
	}
};
