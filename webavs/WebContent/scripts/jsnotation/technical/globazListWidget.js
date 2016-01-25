/**
 * globazListWidget
 */

/**
 * @author DMA Le nom de la fonction doit �tre en minuscule!!!
 * 
 */
globazNotation.listwidget = {

	author: 'DMA',
	forTagHtml: 'utils',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Permet de faire un apple en ajax � un search modele comme pour l'autocompletion(widget) et retourne une liste",

	descriptionOptions: {
		serviceClassName: {
			desc: "nom de la class(chemin package) qui contient la fonction � appler",
			param: "String"
		},
		serviceMethodName: {
			desc: "Nom de la methode � appeler",
			param: "true, false(default)"
		},
		modelReturnVariables: {
			desc: "d�finit les variables de retour que l'on veut s�l�ctionner",
			param: "String"
		},
		cstCriterias: {
			desc: "Crit�re de recherche avec valeur directe : <br /> Exemple: forNom=toto forPrenom=tit",
			param: "String"
		},
		callBack: {
			desc: "fonction javaScript � executer apr�s le retour des donn�es <br />" + "Le retour des donn�es et dans le format XML. En fait on serilise l'objet de retour<br />" + "Exemple: maFonctionDeCallBack(data){$data('tier').find('nom')....}",
			param: "function"
		},
		cursor: {
			desc: "D�finit le nombre de retour dans la liste",
			param: "Nombre default(1000)"
		}
	},

	/**
	 * Parm�tre de le l'objet qui vont �tre pour son initilisation Cet �l�ment est obligatoire, Si pas d'option le cr��er mais vide (options:{})
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