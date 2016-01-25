globazNotation.cellformatter = {

	author: 'DMA',
	forTagHtml: 'td,th',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Cet objet formatte les cellules de tableau en fonction de la class css donnée en options.<br />" + 
					"<strong>Peut être utilisé de 2 manière:</strong><ul>" + 
						"<li>1 Sur un th->Ceci indique que le formatage doit s'effectuer sur la colonne du tableau </li>" + 
						"<li>2 Sur un td->Ceci formatera seulement la cellule qui contient la notation</li></ul>",

	descriptionOptions: {
		css: {
			desc: "Class css qui vas être appliqué à chaque cellue",
			param: "calss css"
		}
	},
	bindEvent: {
		ajaxUpdateComplete: function () {
			this.addCss();
		}
	},
	/**
	 * Parmétre de le l'objet qui vont être pour son initilisation Cet élément est obligatoire, Si pas d'option le crééer mais vide (options:{})
	 */
	options: {
		css: ""
	},

	$elementToPutObjetc: null,

	/**
	 * Cette fonction est obligatoire car c'est elle qui va initiliser l'objet
	 */
	init: function ($elementToPutObjetc) {
		this.$elementToPutObjetc = $elementToPutObjetc;
		this.addCss();
	},

	addCss: function () {
		var that = this;
		this.utils.formatter.applyFunctionOnCellsOfTable(this.$elementToPutObjetc, function () {
			var css = that.options.css;
			this.addClass(css);
		}, true);
	}
};