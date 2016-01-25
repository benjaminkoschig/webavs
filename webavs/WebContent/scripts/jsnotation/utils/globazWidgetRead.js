/**
 * @author DMA Le nom de la fonction doit être en minuscule!!!
 * 
 */
globazNotation.readwidget = {

	author: 'DMA',
	forTagHtml: 'utils',
	type: globazNotation.typesNotation.UTILITIES,

	description: "Permet de faire un apple en ajax à une fonction ou un search modele comme pour l'autocompletion(widget)",

	descriptionOptions: {
		serviceClassName: {
			desc: "nom de la class(chemin package) qui contient la fonction à appler",
			param: "String"
		},
		serviceMethodName: {
			desc: "Nom de la methode à appleter",
			param: "true, false(default)"
		},
		parametres: {
			desc: "Parametre de la méthode (serviceMethodName) que l'on veut appeler",
			param: "string or boolean"
		},
		criterias: {
			desc: "critere de recheche qui doivent etre utiliser si on utilise un searchModele." + "On utilise le 'paramtres' pour setter les vauleurs Exemple: forNom,forPrenom",
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
		async: {
			desc:  "paramètre qui définit si l'appel ajax est traité de manière asynchrone ou synchrone. Valeur par défaut: true (asynchrone)",
			param: "true (default), false"
		}
		
	},

	/**
	 * Parmétre de le l'objet qui vont être pour son initilisation Cet élément est obligatoire, 
	 * Si pas d'option le crééer mais vide (options:{})
	 */
	options: {
		serviceClassName: '',
		serviceMethodName: '',
		parametres: '',
		criterias: '',
		cstCriterias: '',
		callBack: null,
		errorCallBack: null,
		wantInitThreadContext: false,
		forceParametres :false,
		notification:null,
		async: true
	},
	
	url: '',
	/**
	 * Cette fonction est obligatoire car c'est elle qui va initiliser l'objet
	 */
	init: function () {
		
	},

	getParamterForAjax: function (parmetre, callBack) {
		var that = this;
		var parametre = {
			userAction: "widget.action.jade.afficher",
			serviceClassName: that.options.serviceClassName,
			serviceMethodName: that.options.serviceMethodName,
			initThreadContext: that.options.wantInitThreadContext,
			cursor: 5,
			criterias: '',
			cstCriterias: '',
			modelReturnVariables: '',
			searchText: that.options.parametres,
			parametres: that.options.parametres,
			forceParametres : that.options.forceParametres,
			noCache: globazNotation.utilsDate.toDayInStringJadeFormate() + (new Date()).getMilliseconds()
		};

		return parametre;
	},

	applyCallBack: function (data) {
		var that = this;
		if(ajaxUtils.hasError(data)){
			this.applyErrorCallBack(data);
		} else { 
			this.options.callBack(data.viewBean.returnInfosService);
			ajaxUtils.displayLogsIfExsite(data);
		}
	},
	
	applyErrorCallBack: function (jqXHR, textStatus, errorThrown) {
		if (typeof this.options.errorCallBack === "function" && this.options.errorCallBack !== null) {
			this.options.errorCallBack(jqXHR, textStatus, errorThrown);
		} else {
			ajaxUtils.displayError(jqXHR);
			//globazNotation.utils.console(jqXHR.responseText, "ERROR");
		}
	},

	read: function () {
		var that = this;
		$.ajax({
			url: globazNotation.utils.getFromAction(),
			async: that.options.async,
			dataType: "json",
			contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
			data: that.getParamterForAjax(),
			success: function (data) {
				if(data) {
					that.applyCallBack(data);
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				that.applyErrorCallBack(jqXHR, textStatus, errorThrown);
			},
			type: "GET"
		});
	},
	
	readSync: function () {
		var that = this;
		$.ajax({
			url: globazNotation.utils.getFromAction(),
			dataType: "json",
			contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
			data: that.getParamterForAjax(),
			async: false,
			success: function (data) {
				if(data) {
					that.applyCallBack(data);
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				that.applyErrorCallBack(jqXHR, textStatus, errorThrown);
			},
			type: "GET"
		});
	}
};