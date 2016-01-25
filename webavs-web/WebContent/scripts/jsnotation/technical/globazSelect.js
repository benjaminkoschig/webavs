/**
 * @author DMA Le nom de la fonction doit �tre en minuscule!!!
 * 
 */
globazNotation.select = {

	author: 'DMA',
	forTagHtml: 'select',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Cette objet permet de g�rer les champs select <br />",

	descriptionOptions: {
		mandatory: {
			desc: "Test si �l�ment est obligatoir",
			param: "true, false(default)"
		},
		addSymboleMandatory: {
			desc: "Ajoute le symoble *. Si le param�tre mandatory est � true ce param�tre n'est pas utilis�",
			param: "true(par d�fault)|false"
		}
	},

	/**
	 * Parm�tre de le l'objet qui vont �tre pour son initilisation Cet �l�ment est obligatoire, Si pas d'option le cr��er mais vide (options:{})
	 */
	options: {
		mandatory: false,
		addSymboleMandatory: true
	},

	/**
	 * Cette fonction est obligatoire car c'est elle qui va initiliser l'objet
	 */
	init: function ($elementToPutObject) {
		this.utils.input.addAllPropertyFromUtil(this.options, $elementToPutObject);
	}

};