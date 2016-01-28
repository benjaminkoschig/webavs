globazNotation.cellformatter = {

	author: 'DMA',
	forTagHtml: 'td,th',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Cet objet formatte les cellules de tableau en fonction de la class css donn�e en options.<br />" + 
					"<strong>Peut �tre utilis� de 2 mani�re:</strong><ul>" + 
						"<li>1 Sur un th->Ceci indique que le formatage doit s'effectuer sur la colonne du tableau </li>" + 
						"<li>2 Sur un td->Ceci formatera seulement la cellule qui contient la notation</li></ul>",

	descriptionOptions: {
		css: {
			desc: "Class css qui vas �tre appliqu� � chaque cellue",
			param: "calss css"
		}
	},
	bindEvent: {
		ajaxUpdateComplete: function () {
			this.addCss();
		}
	},
	/**
	 * Parm�tre de le l'objet qui vont �tre pour son initilisation Cet �l�ment est obligatoire, Si pas d'option le cr��er mais vide (options:{})
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