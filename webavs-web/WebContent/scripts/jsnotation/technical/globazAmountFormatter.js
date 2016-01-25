globazNotation.amountformatter = {

	author: 'DMA',
	forTagHtml: 'td,th,span',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Cet objet formatte les cellules de tableau contenant un montant.<br />" + 
					"<strong>Peut être utilisé de 3 manière:</strong><ul>" + 
					"<li>1 Sur un th->Ceci indique que le formatage doit s'effectuer sur la colonne du tableau </li>" + 
					"<li>2 Sur un td->Ceci formatera seulement la cellule qui contient la notation</li>" +
					"<li>Sur un span->Ceci formatera seulement le span qui contient la notation</li></ul>" + 
					"Si la valeur n'est pas un montant, aucun formattage ne sera appliqué!",

	descriptionOptions: {
		blankAsZero: {
			desc: "Définit si il faut afficher les valeur null par 0.00",
			param: "true(default), false"
		}

	},

	/**
	 * Parmétre de le l'objet qui vont être pour son initilisation Cet élément est obligatoire, Si pas d'option le crééer mais vide (options:{})
	 */
	options: {
		blankAsZero: true
	},

	bindEvent: {
		ajaxUpdateComplete: function () {
			this.applyFormat();
		},
		ajaxLoadData: function () {
			this.applyFormat();
		}
	},

	/**
	 * Cette fonction est obligatoire car c'est elle qui va initiliser l'objet
	 */
	init: function ($elementToPutObject) {
		this.applyFormat();
	},

	applyFormat: function () {
		var that = this;
		if(that.$elementToPutObject.get(0).nodeName === "SPAN") {
			var s_formatted = that.utils.formatter.formatStringToAmout(that.$elementToPutObject.text(), 2, that.options.blankAsZero);
			that.$elementToPutObject.text(s_formatted);
			that.$elementToPutObject.addClass('montant');
		} else {
			this.utils.formatter.applyFunctionOnCellsOfTable(that.$elementToPutObject, function () {
				if (!isNaN(this.text())) {
					var s_formatted = that.utils.formatter.formatStringToAmout(this.text(), 2, that.options.blankAsZero);
					this.text(s_formatted);
					this.addClass('montant');
				}
			});
		}
	},

	checkIfIsNumber: function (number) {
		return (number == parseFloat(number));
	}

};