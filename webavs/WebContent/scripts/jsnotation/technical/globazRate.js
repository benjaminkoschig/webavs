/**
 * @author DMA Le nom de la fonction doit �tre en minuscule!!!
 * 
 */
globazNotation.rate = {

	author: 'DMA',
	forTagHtml: 'input',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Cette objet ajout la class taux au champ input et accepte seulement des numero",

	descriptionOptions: {
		mandatory: {
			desc: "Test si �l�ment est obligatoir",
			param: "true, false(default)"
		},
		addSymboleMandatory: {
			desc: "Ajoute le symoble *. Si le param�tre mandatory est � true ce param�tre n'est pas utilis�",
			param: "true(par d�fault)|false"
		},
		nbMaxDecimal: {
			desc: "Indique le nombre max de d�cimals",
			param: "Nombres|par d�fault 5"
		}
	},

	/**
	 * Parm�tre de le l'objet qui vont �tre pour son initilisation Cet �l�ment est obligatoire, Si pas d'option le cr��er mais vide (options:{})
	 */
	options: {
		mandatory: false,
		addSymboleMandatory: true,
		nbMaxDecimal: 5
	},

	/**
	 * Cette fonction est obligatoire car c'est elle qui va initiliser l'objet
	 */
	init: function ($elementToPutObject) {
		this.utils.input.addAllPropertyFromUtil(this.options, $elementToPutObject);
		this.addEventAndClassForRate($elementToPutObject);
	},

	addEventAndClassForRate: function ($obj) {
		var that = this;
		$obj.change(function () {
			validateFloatNumber(this, that.options.nbMaxDecimal);
		});
		$obj.addClass('numberNotation');
		this.utils.input.addEventIsRate($obj);
	}
};
