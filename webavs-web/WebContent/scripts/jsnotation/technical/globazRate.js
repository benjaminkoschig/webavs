/**
 * @author DMA Le nom de la fonction doit être en minuscule!!!
 * 
 */
globazNotation.rate = {

	author: 'DMA',
	forTagHtml: 'input',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Cette objet ajout la class taux au champ input et accepte seulement des numero",

	descriptionOptions: {
		mandatory: {
			desc: "Test si élément est obligatoir",
			param: "true, false(default)"
		},
		addSymboleMandatory: {
			desc: "Ajoute le symoble *. Si le paramétre mandatory est à true ce paramétre n'est pas utilisé",
			param: "true(par défault)|false"
		},
		nbMaxDecimal: {
			desc: "Indique le nombre max de décimals",
			param: "Nombres|par défault 5"
		}
	},

	/**
	 * Parmétre de le l'objet qui vont être pour son initilisation Cet élément est obligatoire, Si pas d'option le crééer mais vide (options:{})
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
