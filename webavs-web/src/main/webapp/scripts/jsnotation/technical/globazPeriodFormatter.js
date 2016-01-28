globazNotation.periodformatter = {

	author: 'SCE',
	forTagHtml: 'td,th',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Cet objet formatte les cellules de tableau contenant une période.<br />" + 
	"<strong>Peut être utilisé de 2 manière:</strong><ul>" + 
	"<li>1 Sur un th->Ceci indique que le formatage doit s'effectuer sur la colonne du tableau </li>" + 
	"<li>2 Sur un td->Ceci formatera seulement la cellule qui contient la notation</li></ul>",

	descriptionOptions: {},
	/**
	* Parmétre de le l'objet qui vont être pour son initilisation Cet élément est obligatoire, Si pas d'option le crééer mais vide (options:{})
	*/
	options: {},

	/**
	* Cette fonction est obligatoire car c'est elle qui va initiliser l'objet
	*/
	// $elementToPutObjetc: null,
	bindEvent: {
		ajaxUpdateComplete: function () {
			this.addFormat();
		}
	},

	init: function ($elementToPutObject) {
		this.addFormat();
	},

	addFormat: function () {
		this.$elementToPutObject.css('width', '215px');
		this.$elementToPutObject.attr('noWrap', 'noWrap');
		this.utils.formatter.applyFunctionOnCellsOfTable(this.$elementToPutObject, function () {
			this.attr('noWrap', 'noWrap');
			// this.addClass('formatPeriod');
			this.css('text-align', 'left');
			this.css({
				'width': '215px',
				'text-align': 'left'
			});
			// this.css('text-align','left');
		});
	}
};