/**
 * @author DMA Le nom de la fonction doit �tre en minuscule!!!
 * 
 */
globazNotation.readwidget = {

	author: 'DMA',
	forTagHtml: 'utils',
	type: globazNotation.typesNotation.UTILITIES,

	description: "Permet de faire un apple en ajax � une fonction ou un search modele comme pour l'autocompletion(widget)",

	descriptionOptions: {
		serviceClassName: {
			desc: "nom de la class(chemin package) qui contient la fonction � appler",
			param: "String"
		},
		serviceMethodName: {
			desc: "Nom de la methode � appleter",
			param: "true, false(default)"
		},
		parametres: {
			desc: "Parametre de la m�thode (serviceMethodName) que l'on veut appeler",
			param: "string or boolean"
		},
		criterias: {
			desc: "critere de recheche qui doivent etre utiliser si on utilise un searchModele." + "On utilise le 'paramtres' pour setter les vauleurs Exemple: forNom,forPrenom",
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
		async: {
			desc:  "param�tre qui d�finit si l'appel ajax est trait� de mani�re asynchrone ou synchrone. Valeur par d�faut: true (asynchrone)",
			param: "true (default), false"
		}
		
	},

	/**
	 * Parm�tre de le l'objet qui vont �tre pour son initilisation Cet �l�ment est obligatoire, 
	 * Si pas d'option le cr��er mais vide (options:{})
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