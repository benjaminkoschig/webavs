/**
 * globazListWidget
 */

/**
 * @author DMA Le nom de la fonction doit être en minuscule!!!
 * 
 */
globazNotation.listwidget = {

	author: 'DMA',
	forTagHtml: 'utils',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Permet de faire un apple en ajax à un search modele comme pour l'autocompletion(widget) et retourne une liste",

	descriptionOptions: {
		serviceClassName: {
			desc: "nom de la class(chemin package) qui contient la fonction à appler",
			param: "String"
		},
		serviceMethodName: {
			desc: "Nom de la methode à appeler",
			param: "true, false(default)"
		},
		modelReturnVariables: {
			desc: "définit les variables de retour que l'on veut séléctionner",
			param: "String"
		},
		cstCriterias: {
			desc: "Critère de recherche avec valeur directe : <br /> Exemple: forNom=toto forPrenom=tit",
			param: "String"
		},
		callBack: {
			desc: "fonction javaScript à executer après le retour des données <br />" + "Le retour des données et dans le format XML. En fait on serilise l'objet de retour<br />" + "Exemple: maFonctionDeCallBack(data){$data('tier').find('nom')....}",
			param: "function"
		},
		cursor: {
			desc: "Définit le nombre de retour dans la liste",
			param: "Nombre default(1000)"
		}
	},

	/**
	 * Parmétre de le l'objet qui vont être pour son initilisation Cet élément est obligatoire, Si pas d'option le crééer mais vide (options:{})
	 */
	options: {
		serviceClassName: '',
		serviceMethodName: '',
		cstCriterias: '',
		callBack: null,
		cursor: 1000,
		modelReturnVariables: ''
	},

	/**
	 * Cette fonction est obligatoire car c'est elle qui va initiliser l'objet
	 */
	init: function ($elementToPutObject) {

	},

	getParamterForAjax: function (parmetre, callBack) {
		var that = this;
		var parametre = {
			userAction: "widget.action.jade.lister",
			lineFormat: "#{}",
			criterias: "",
			searchText: "",
			serviceClassName: that.options.serviceClassName,
			serviceMethodName: that.options.serviceMethodName,
			cursor: that.options.cursor,
			cstCriterias: that.options.cstCriterias,
			modelReturnVariables: that.options.modelReturnVariables
		};
		return parametre;
	},

	applyCallBack: function (data) {
		this.options.callBack(data);
	},

	lister: function () {
		var that = this;
		$.ajax({
			dataType: "xml",
			data: that.getParamterForAjax(),
			success: function (data) {
				that.applyCallBack(data);
			},
			type: "GET"
		});
	}

};