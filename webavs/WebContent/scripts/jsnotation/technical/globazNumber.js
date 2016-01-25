/**
 * @author DMA
 * 
 */
globazNotation.integer = {

	author: 'DMA',
	forTagHtml: 'input',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Cette objet indique que le champ input contiendra un entier.<br /> " + "On peut d�finir le nombre max de carat�res",

	descriptionOptions: {
		sizeMax: {
			desc: "D�finit le nombre max de carat�res autoris� pour le champ input",
			param: "Un nombre. Aucune valeur par d�faut"
		},
		mandatory: {
			desc: "D�fini si l'�l�ment est obligatoire",
			param: "true|false(default)"
		},
		addSymboleMandatory: {
			desc: "Ajoute le symoble '*'. Si le param�tre mandatory est � true, ce param�tre n'est pas utilis�",
			param: "true(par d�fault)|false"
		},
		autoFit: {
			desc: "D�finit si on veut adapter la taille du champ en fonction de sizMax ",
			param: "true|false(default)"
		}
	},

	/**
	 * Param�tre de le l'objet qui vont �tre pour son initialisation Cet �l�ment est obligatoire. S'il n'y a pas d'option, le cr�er vide (options:{})
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
