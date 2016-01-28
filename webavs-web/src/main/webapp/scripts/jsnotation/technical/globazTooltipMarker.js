globazNotation.tooltip = {

	author: 'SCE',
	forTagHtml: 'td,span,div',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description : "Cet objet ajoute un indicateur visuel et un tooltip (coin tronqu� vert) sur le champ contenant une infobulle",

	descriptionOptions: {
		libelle: {
			desc: "Libelle de l'infobulle",
			param: "Chaine de caractere � afficher"
		},
		marker: {
			desc: "Affichage de l'indicateur visuel",
			param: "true(defaut)|false"
		},
		pright: {
			desc: "D�finis la propri�t� ccs padding-right, de mani�re a ce que le texte ne superpose pas l'image du coin vert",
			param: "true(defaut)|false"
		}
	},

	/**
	 * Param�tre de le l'objet qui vont �tre utilis�s pour son initialisation
	 * Cet �l�ment est obligatoire, si aucune option le cr�er vide (options:{})
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
