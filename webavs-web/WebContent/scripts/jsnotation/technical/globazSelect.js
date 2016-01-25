/**
 * @author DMA Le nom de la fonction doit être en minuscule!!!
 * 
 */
globazNotation.select = {

	author: 'DMA',
	forTagHtml: 'select',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Cette objet permet de gérer les champs select <br />",

	descriptionOptions: {
		mandatory: {
			desc: "Test si élément est obligatoir",
			param: "true, false(default)"
		},
		addSymboleMandatory: {
			desc: "Ajoute le symoble *. Si le paramétre mandatory est à true ce paramétre n'est pas utilisé",
			param: "true(par défault)|false"
		}
	},

	/**
	 * Parmétre de le l'objet qui vont être pour son initilisation Cet élément est obligatoire, Si pas d'option le crééer mais vide (options:{})
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