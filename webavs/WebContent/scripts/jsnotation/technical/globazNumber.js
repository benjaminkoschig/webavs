/**
 * @author DMA
 * 
 */
globazNotation.integer = {

	author: 'DMA',
	forTagHtml: 'input',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Cette objet indique que le champ input contiendra un entier.<br /> " + "On peut définir le nombre max de caratères",

	descriptionOptions: {
		sizeMax: {
			desc: "Définit le nombre max de caratères autorisé pour le champ input",
			param: "Un nombre. Aucune valeur par défaut"
		},
		mandatory: {
			desc: "Défini si l'élément est obligatoire",
			param: "true|false(default)"
		},
		addSymboleMandatory: {
			desc: "Ajoute le symoble '*'. Si le paramètre mandatory est à true, ce paramètre n'est pas utilisé",
			param: "true(par défault)|false"
		},
		autoFit: {
			desc: "Définit si on veut adapter la taille du champ en fonction de sizMax ",
			param: "true|false(default)"
		}
	},

	/**
	 * Paramètre de le l'objet qui vont être pour son initialisation Cet élément est obligatoire. S'il n'y a pas d'option, le créer vide (options:{})
	 */
	options: {
		sizeMax: null,
		mandatory: false,
		addSymboleMandatory: true,
		autoFit: false
	},

	bindEvent: {
		ajaxShowDetailRefresh: function (event) {
			this.utils.input.testColor(this.options, this.$elementToPutObject);
		}
	},

	/**
	 * Cette fonction est obligatoire car c'est elle qui va initialiser l'objet
	 */
	init: function ($elementToPutObject) {
		var that = this;
		this.utils.input.addAllPropertyFromUtil(this.options, $elementToPutObject);
		this.utils.input.addEventIsInteger($elementToPutObject);
		$elementToPutObject.addClass('numberNotation');
	},

	validate: function () {
		if (this.utils.input.validate(this.options, this.$elementToPutObject) != '') {
			return false;
		}
		return true;
	}
};
