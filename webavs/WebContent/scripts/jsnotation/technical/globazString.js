/**
 * @author DMA Le nom de la fonction doit �tre en minuscule!!!
 * 
 */
globazNotation.string = {

	author: 'DMA',
	forTagHtml: 'input,textarea',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Cette objet permet de g�rer les champs inputs qui contiendrons du text <br />",

	descriptionOptions: {
		mandatory: {
			desc: "Test si �l�ment est obligatoir",
			param: "true, false(default)"
		},
		addSymboleMandatory: {
			desc: "Ajoute le symoble *. Si le param�tre mandatory est � true ce param�tre n'est pas utilis�",
			param: "true|false(default)"
		},
		sizeMax: {
			desc: "D�finit le nombre max de carat�res autoris� pour le champ input",
			param: "Un nombre. Aucune valeur par d�faut"
		},
		autoFit: {
			desc: "D�finit si on veut adapter la taille du champ en fonction de sizMax ",
			param: "true|false(default)"
		},
		firstCaseUpper: {
			desc: "Met le permier caract�res en majuscule",
			param: "true|false(default)"
		},
		isPasteOverload : {
			desc : "V�rifie si la valeur coll�e n'est pas plus haute que l'attribut sizeMax, si c'est le cas un message d'erreur est envoy� et la valeur coll�e est supprim�e",
			param : "true|false(default)"
		}		
	},

	/**
	 * Ce param�tre est facultatif Ce param�tre permet des lancer des fonctions sur diff�rent types d'�venements Liste des Evenements : boutons sandard de l'application. Les
	 * �v�nements ce lance sur le clique du bouton btnCancel btnAdd btnValidate btnUpdate btnDelete AJAX: tous ces fonctions ce lance � la fin de l'a fonction dans ajax
	 * ajaxShowDetailRefresh ajaxLoadData ajaxShowDetail ajaxStopEdition ajaxValidateEditon ajaxUpdateComplete
	 */
	bindEvent: {
		ajaxShowDetailRefresh: function (event) {
			this.utils.input.testColor(this.options, this.$elementToPutObject);
		}
	},

	/**
	 * Param�tre de le l'objet qui vont �tre pour son initialisation Cet �l�ment est obligatoire, Si pas d'option le cr�er mais vide (options:{})
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
