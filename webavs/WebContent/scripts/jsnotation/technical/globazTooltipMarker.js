globazNotation.tooltip = {

	author: 'SCE',
	forTagHtml: 'td,span,div',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description : "Cet objet ajoute un indicateur visuel et un tooltip (coin tronqué vert) sur le champ contenant une infobulle",

	descriptionOptions: {
		libelle: {
			desc: "Libelle de l'infobulle",
			param: "Chaine de caractere à afficher"
		},
		marker: {
			desc: "Affichage de l'indicateur visuel",
			param: "true(defaut)|false"
		},
		pright: {
			desc: "Définis la propriété ccs padding-right, de manière a ce que le texte ne superpose pas l'image du coin vert",
			param: "true(defaut)|false"
		}
	},

	/**
	 * Paramètre de le l'objet qui vont être utilisés pour son initialisation
	 * Cet élément est obligatoire, si aucune option le créer vide (options:{})
	 */
	options: {
		libelle: '',
		marker: true,
		pright: true
	},

	/**
	 * Cette fonction est obligatoire car c'est elle qui va initiliser l'objet
	 */
	init: function ($elementToPutObjetc) {

		$elementToPutObjetc.attr('title', this.options.libelle);

		//Si marqueur, affichage de l'indicateur visuel
		if (this.options.marker) {
			//application des styles css
			$elementToPutObjetc.css({
				'background-image': 'url('+this.s_contextUrl+'/images/tooltipCorner.png)',
				'background-repeat': 'no-repeat',
				'background-position': 'right bottom'
			});
		}

		//Si padding
		if(this.options.pright){
			$elementToPutObjetc.css('padding-right','8px');
		}
	}
};
