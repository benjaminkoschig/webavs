/**
 * @author DMA Le nom de la fonction doit être en minuscule!!!
 * 
 */
globazNotation.string = {

	author: 'DMA',
	forTagHtml: 'input,textarea',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Cette objet permet de gérer les champs inputs qui contiendrons du text <br />",

	descriptionOptions: {
		mandatory: {
			desc: "Test si élément est obligatoir",
			param: "true, false(default)"
		},
		addSymboleMandatory: {
			desc: "Ajoute le symoble *. Si le paramétre mandatory est à true ce paramétre n'est pas utilisé",
			param: "true|false(default)"
		},
		sizeMax: {
			desc: "Définit le nombre max de caratères autorisé pour le champ input",
			param: "Un nombre. Aucune valeur par défaut"
		},
		autoFit: {
			desc: "Définit si on veut adapter la taille du champ en fonction de sizMax ",
			param: "true|false(default)"
		},
		firstCaseUpper: {
			desc: "Met le permier caractères en majuscule",
			param: "true|false(default)"
		},
		isPasteOverload : {
			desc : "Vérifie si la valeur collée n'est pas plus haute que l'attribut sizeMax, si c'est le cas un message d'erreur est envoyé et la valeur collée est supprimée",
			param : "true|false(default)"
		}		
	},

	/**
	 * Ce paramètre est facultatif Ce paramètre permet des lancer des fonctions sur différent types d'évenements Liste des Evenements : boutons sandard de l'application. Les
	 * événements ce lance sur le clique du bouton btnCancel btnAdd btnValidate btnUpdate btnDelete AJAX: tous ces fonctions ce lance à la fin de l'a fonction dans ajax
	 * ajaxShowDetailRefresh ajaxLoadData ajaxShowDetail ajaxStopEdition ajaxValidateEditon ajaxUpdateComplete
	 */
	bindEvent: {
		ajaxShowDetailRefresh: function (event) {
			this.utils.input.testColor(this.options, this.$elementToPutObject);
		}
	},

	/**
	 * Paramètre de le l'objet qui vont être pour son initialisation Cet élément est obligatoire, Si pas d'option le créer mais vide (options:{})
	 */
	options: {
		mandatory: false,
		addSymboleMandatory: true,
		sizeMax: null,
		autoFit: false,
		firstCaseUpper: false,
		isPasteOverload : false
	},

	/**
	 * Cette fonction est obligatoire car c'est elle qui va initialiser l'objet
	 */
	init: function ($elementToPutObject) {
		this.utils.input.addAllPropertyFromUtil(this.options, $elementToPutObject);
		if (this.options.firstCaseUpper) {
			this.addEventFirstCharInUpperCase();
		}
		if (this.options.isPasteOverload && this.options.sizeMax != null) {
			this.addEventIsPasteOverload(this.options.sizeMax);
		}
	},

	addEventIsPasteOverload : function(maxSize) {
		var that = this;
		this.$elementToPutObject.keyup(function() {
			if (that.utils.input.isSizeMax(this, maxSize + 1)) {
			   alert(that.i18n("errormessage") + "\n" 
					   + that.i18n("errormessagelenghttried") + " " + this.value.length + "\n"
					   + that.i18n("errormessagelength") + " " + maxSize);
			   return this.value = "";
			}
		});
	},
	
	addEventFirstCharInUpperCase: function () {
		var that = this;
		this.$elementToPutObject.keyup(function () {
			this.value = that.utils.firstUpperCase(this.value);
		});
	},

	validate: function () {
		if (this.utils.input.validate(this.options, this.$elementToPutObject) != '') {
			return false;
		}
		return true;
	}
};
