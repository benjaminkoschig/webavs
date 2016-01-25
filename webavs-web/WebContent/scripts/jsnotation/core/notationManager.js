/**
 * @author DMA
 * 
 * Cette objet permet des gérer le notation qui sont définit dans le html
 */

var NotationManager = function () {
};

NotationManager.prototype = {
	$html: null,
	//elementsWhoHasNotation: [],
	hasError: false, // indique Si il y a eu des erreurs
	showErrors: true, // permet de définir si il faut afficher les messages d'erreurs
	//$elementsNotation: {},
	elementsInError: [], // Tableau qui contitent les éléments en erreurs
	lienForTest: 'jade/notation/demo.jsp',
	lienForDocumentation: 'jade/notation/doc.jsp',
	o_objEnTraitement: null,
	s_nameObjEnTraitement: '',
	b_stop: false, // permet de ne pas executer le moteur
	b_showInfos: false, // permet de définir si il faut afficher les temps d'executions des notations
	b_debug: false, // mode débug
	s_contextUrl: null,
	s_pathImg: null,
	s_typePage: null, // indique le type de page html (AJAX,..)
	b_i18nIsLoaded: false, // inque si l'internationalisation a été chargé.
	b_started: false,
	mapEmentsTriggers: [], // Contient les éléments qui on executé le moteur par un evenement
	o_infosMoteur: {},
	n_wantedLogLevel: -1,
	b_isFocusInit: false,
	//$inputColoredWhenFocus: null,
	//inputColoredWhenFocusInHandler: null,
	//inputColoredWhenFocusOutHandler: null,

	init: function () {
		this.$html = $('html');
		this.b_showInfos = this.toBoolean($('[showInfos]:first').attr('showInfos'));
		this.s_contextUrl = globazNotation.utils.getContextUrl();
		this.s_pathImg = this.s_contextUrl + "/scripts/jsnotation/imgs/";
		this.s_typePage = $('[name=TypePage]').attr('content');
		jsManager.executeBefore();
		this.startByEvent();
		if (this.b_showInfos) {
			this.displayElelementsWhoHasNotation();
		}
	},

	existFonction: function (f_fonction) {
		return ((typeof f_fonction) === "function");
	},

	specific: function () {
	},

	hasSpecificFonction: function (s_fonctionName) {
		if (this.s_typePage !== 'SANS' || this.s_typePage != undefined) {
			if (this.specific[s_fonctionName]) {
				return true;
			}
		}
		return false;
	},

	startByEvent: function () {
		var that = this;
		var start = (new Date()).getTime();
		if (this.hasSpecificFonction("startByEvent")) {
			this.specific.startByEvent.call(this);
		} else if (this.s_typePage === "JADE_FW_DE") {
			this.$html.bind(eventConstant.JADE_FW_ACTION_DONE, function () {
				that.o_infosMoteur.t_timeJsAutre = (new Date()).getTime() - start;
				that.startNotation();
				that.b_started = true;
			});
			setTimeout(function () {
				if (that.b_started === false) {
					that.startNotation();
					that.b_started = true;
				}
			}, 200);
		} else {
			if (globazNotation.utils.isEmpty(this.s_typePage)) {
				this.s_typePage = "SANS";
			}
			that.startNotation();
		}
	},

	exitFFConsole: function () {
		return (typeof console !== "undefined");
	},

	startNotation: function () {
		var b_init = false;

		while (!b_init) {
			if (notationManager.b_i18nIsLoaded) {
				b_init = true;
				if (!this.b_stop) {
					var start = (new Date()).getTime();
					$.createAllFunctionForNotationOnJquery();
					this.$html.triggerHandler(eventConstant.NOTATION_MANAGER_START);
					try {
						if (this.exitFFConsole()) {
							console.log("Start Notation");
						}
						this.addToAllElementJs();
						this.o_infosMoteur.t_timeMoteur = (new Date()).getTime() - start;
						jsManager.executeAfter();

						this.displayErrors();
					} catch (e) {
						this.putError(this.messageJsException(e));
						if (this.exitFFConsole()) {
							console.log(e);
						}
						this.o_infosMoteur.t_timeMoteur = (new Date()).getTime() - start;
						this.displayErrors();
						throw e;
					}
					this.b_started = true;
					this.$html.triggerHandler(eventConstant.NOTATION_MANAGER_DONE);
				}
			}
		}
		this.startLogging();
	},

	startLogging: function () {
		var that = this;
		this.$html.bind(eventConstant.NEW_LOG, function (event, o_logObject) {
			globazNotation.utils.logging.log(o_logObject, that.n_wantedLogLevel);
		});
	},

	start: function () {
		this.b_stop = false;
		this.init();
	},

	toBoolean: function (value) {
		if (value === "true") {
			value = true;
		} else if (value === "false") {
			value = false;
		} else {
			value = null;
		}
		return value;
	},

	toNumber: function (value) {
		var number = parseInt(value, 10);
		if (!isNaN(value)) {
			if (number == value) {
				return number;
			}
		}
		return null;
	},

	toFunction: function (value) {
		if (/function\s*[(][\f\r\n\t\v\0\s\S\w\W\d\D]*[)]*\s*[{][\f\r\n\t\v\0\s\S\w\W\d\D\]*[}]/.test((value))) {
			value = eval('[' + (value) + ']')[0];
			return value;
		}
		return null;
	},

	toArray: function (value) {
		var t = [];
		if (/(^\[.*\]$)/.test((value))) {
			t = eval(value);
			return t;
		} else {
			return null;
		}
	},

	toObject: function (value) {
		value = value.replace(/[\r]*[\n]*[\t]*/g, '');
		if (/(^\{.*\}$)/.test((value))) {
			return eval('(' + value + ')');
		} else {
			return null;
		}
	},
	
	

	string_to_js_variable: function (value) {
		var js = null, t_objetLiteral;

		if (value.indexOf(".") > 0) {
			t_objetLiteral = value.split('.');
			js = window[t_objetLiteral[0]];
			if (js && t_objetLiteral.length === 2 && typeof js[t_objetLiteral[1]] === "function") {
				js = js[t_objetLiteral[1]];
			} else {
				return null;
			}
		} else {
			js = window[value];
			if (typeof js != "undefined") {
				// Ducktyping pour IE, si le nom donné correspond à un ID
				// IE donnera l'élement HTML...
				if ($.type(js) === "object"){
					if (js.nodeName !== undefined && js.id !== undefined && js.id === value ) {
						return null;
					}	
				}
			}
			if (globazNotation.utils.isEmpty(js)) {
				js = null;
			}
		}
		return js;
	},
	isAjaxPage: function () {
		return this.s_typePage === 'AJAX';
	},

	convertSrting: function (s_value, b_hasDoubleVerticalBar) {
		var value, message;
		value = this.toBoolean(s_value);
		if (value === null) {
			value = this.toNumber(s_value);
		}
		if (value === null) {
			try {
				value = this.toArray(s_value);
			} catch (e) {
				message = "<strong>Erreur de conversion de tableau avec la valeur <i class='ui-state-error-text'>" + s_value + "</i></strong>";
				this.putError(message);
			}
		}
		if (value === null) {
			try {
				value = this.toObject(s_value);
			} catch (e1) {
				message = "<strong>Erreur de conversion d'objet avec la valeur <i class='ui-state-error-text'>" + s_value + "</i></strong>";
				this.putError(message);
			}
		}
		if (value === null) {
			value = this.toFunction(s_value);
		}

		if (value === null && !b_hasDoubleVerticalBar) {
			value = this.string_to_js_variable(s_value);
		}

		if (value === null) {
			value = s_value;
		}
		return value;
	},

	/**
	 * Test la validité syntaxique des parmétres définint dans l'attribut de la notation
	 * 
	 * @param {Object}
	 *            s_string
	 */
	hasManyComma: function (s_string) {
		var exec = /([\w\s]*:[^\¦|:]+[^\w:\u0020])/g, b_return = false, tab = s_string.match(exec);
		if (tab !== null) {
			if (s_string.indexOf('¦') < 0) {
				for (var j = 0; j < tab.length; j++) {
					var s = $.trim(tab[j]);
					s = (s.slice(0, s.length - 1));
					if (s.countChar(',') > 0) {
						b_return = true;
					}
				}
			}
		}
		return b_return;
	},

	/**
	 * Test si la notation peut être appliqué sur l'élément DOM
	 * 
	 * @param {Object}
	 *            elementAccess
	 * @param {Object}
	 *            s_nameElementToPutNoation
	 */
	isObjectApllyToElement: function (elementAccess, s_nameElementToPutNoation) {
		var s_elementAccess = elementAccess.toLowerCase(), t_access = s_elementAccess.split(','), all = (s_elementAccess.indexOf('*') >= 0) ? true : false;
		for (var i = 0; i < t_access.length; i++) {
			var ele = $.trim(t_access[i]);
			if (ele.indexOf('!') >= 0) {
				if (ele.indexOf(s_nameElementToPutNoation) >= 0) {
					return false;
				}
			} else if (s_nameElementToPutNoation === ele) {
				return true;
			}
		}
		return all;
	},

	/**
	 * Crée un objet contenant les parmétres définit dans l'attribut de l'objet
	 * 
	 * @param {Object}
	 *            o_attribut
	 */
	createOptions: function (o_attribut) {
		var s_param = "", t_param = [], o_param = {}, t_sousParm = [], s_valueParam = "", pars_attribut;

		s_param = o_attribut.nodeValue;
		if (!globazNotation.utils.isEmpty(s_param)) {
			pars_attribut = /([\w\s]*:([^,¦]*)(\¦[^\¦]*\¦|))/g;

			t_param = s_param.match(pars_attribut);
			if (!globazNotation.utils.isEmpty(t_param)) {
				if (!this.hasManyComma(s_param)) {
					for (var j = 0; j < t_param.length; j++) {
						var b_hasDoubleVerticalBar = false;
						if (t_param[j].indexOf('¦') > 0) {
							t_sousParm = t_param[j].split(':¦');
							s_valueParam = t_sousParm[1].replace('¦', "").replace('¦', '');
							b_hasDoubleVerticalBar = true;
						} else {
							t_sousParm = t_param[j].split(':');
							if (t_sousParm.length > 1) {
								s_valueParam = t_sousParm[1];
							}
						}
						s_valueParam = ($.trim(s_valueParam));
						s_valueParam = this.convertSrting(s_valueParam, b_hasDoubleVerticalBar);
						o_param[$.trim(t_sousParm[0])] = s_valueParam;
					}
				} else {
					var message = "<div>" + "<strong>" + "Erreur d' écriture dans cet élément " + "<i class='ui-state-error-text'>" + this.o_objEnTraitement.nodeName + "</i>" + "</strong><br />" + "Il faut utilisé <strong>'¦'</strong> pour définr toute options qui n'est pas du string" + "</div>";
					this.putError(message);
				}
			}
		}
		return o_param;
	},

	/**
	 * Initialise l'objet notaion
	 * 
	 * @param {Object}
	 *            objNotation
	 * @param {Object}
	 *            o_attribut
	 * @param {Object}
	 *            $elementToPutObject
	 */
	initObjNotation: function (objNotation, s_notationName, $elementToPutObject) {
		var s_elementAccess = $.trim(objNotation.forTagHtml.toLowerCase());
		var s_nameElement = $.trim($elementToPutObject[0].nodeName.toString().toLowerCase());
		
		if (this.isObjectApllyToElement(s_elementAccess, s_nameElement)) {
			objNotation.init($elementToPutObject);
		} else {
			var message =	"<strong>" + "La notation " + 
								"<i class='ui-state-error-text'>" + s_notationName + "</i>" + 
									" ne peut pas être utilisé sur l'element " + 
								"<i class='ui-state-error-text'>" + s_nameElement + "</i> html." + 
							"</strong>" + 
							"<br />On peut l'utiliser dans: " + s_elementAccess;

			this.putError(message);
			objNotation = null;
		}
		return objNotation;
	},

	/**
	 * Test que les parmétres(options) sont valides
	 * 
	 * @param {Object}
	 *            defaultOption
	 * @param {Object}
	 *            optionFinded
	 */
	isOptionValidFormat: function (defaultOption, optionFinded) {
		var s_typeOf = typeof defaultOption;
		if (!globazNotation.utils.isEmpty(optionFinded) && defaultOption != null) {
			if (typeof optionFinded !== s_typeOf && !(s_typeOf == "string" && typeof optionFinded == "number") && s_typeOf != "object") {
				return false;
			} else {
				if (s_typeOf === 'object') {
					if ($.isArray(defaultOption)) {
						if (!$.isArray(optionFinded)) {
							return false;
						}
					}
					if ($.isPlainObject(defaultOption)) {
						if (!$.isPlainObject(optionFinded)) {
							return false;
						}
					}
				}
			}
		}
		return true;
	},

	/**
	 * Parsse la chaine qui doit etre une expresion JS boolean et retourn le resultat
	 * 
	 * @param {String}
	 *            value:
	 */
	execStructureConditionnelle: function (value) {
		var condition = null;
		var val;
		if (value.indexOf('$(this)')) {
			condition = value.replace('$(this)', 'that.$ele');
		} else {
			condition = value.replace('this', 'that.o_objEnTraitement');
		}
		try {
			if (typeof value === 'string' && value !== null) {
				val = eval('(' + condition + ')');
			} else {
				val = value;
			}
		} catch (e) {
			val = value;
			this.putError("<strong>Cette chaine<i class='ui-state-error-text'> " + value + " </i> n'est pas une structure conditionnnel</strong>");
		}
		return val;
	},

	isOptionMandatory: function (objNotation, s_optionCourant) {
		var specOption = objNotation.descriptionOptions[s_optionCourant];
		if (specOption !== undefined) {
			if (typeof specOption.mandatory !== 'undefined') {
				if (specOption.mandatory === true) {
					return true;
				}
			}
		}
		return false;
	},

	optionMandatoryIsOk: function (objNotationDefautl, optionsFinded, s_opt) {
		if (this.isOptionMandatory(objNotationDefautl, s_opt) && globazNotation.utils.isEmpty(optionsFinded[s_opt])) {
			return false;
		}
		return true;
	},

	/**
	 * Verifi que le parmétre définti dans l'attribut de l élément, soit correct et fusion les paramétres par défaut de l'objet notation.
	 * 
	 * @param {Object}
	 *            obj
	 * @param {Object}
	 *            options
	 */
	verifAndModifOptions: function (obj, options) {
		var optionsOK = true;
		var message = "";
		var defaultOptions = Object.create(options);
		for (var opt in defaultOptions) {
			if (opt in obj.options) {
				if (!this.optionMandatoryIsOk(obj, defaultOptions, opt)) {
					this.putFormattedError("Option mandatory", opt, "Cette option est obligatoire");
				}
				if (optionsOK !== false) {
					optionsOK = true;
				}
				if (typeof obj.options[opt] === 'boolean') {
					if (typeof options[opt] === 'string') {
						options.stringConditionnel = options[opt];
						options[opt] = this.execStructureConditionnelle(options[opt]);
					}
				}
				if (!this.isOptionValidFormat(obj.options[opt], options[opt])) {
					message = "<strong>Erreur dans le paramétre <i class='ui-state-error-text'>" + opt + "</i></strong>. Type du param: " + typeof obj.options[opt] + " trouvé -> " + typeof options[opt] + " VALEUR:" + options[opt];
					this.putError(message);
				}
			} else {
				message = "<div><strong>L'option <i class='ui-state-error-text'>" + opt + "</i>  existe pas. </strong></div>";
				this.putError(message);
				optionsOK = false;
			}
			opt = null;
		}
		return options;
	},

	/**
	 * Ajoute les différents paramétres définit dans l'attribut(notation syntaxe data-g-nomNotation) de l'élément
	 * 
	 * @param {Object}
	 *            o_objNoation
	 * @param {Object}
	 *            o_attribut
	 * @param {Object}
	 *            $elementToPutObject
	 */
	addOptionsToObjectNotation: function (o_objNoation, o_attribut) {
		var o_options = this.createOptions(o_attribut);
		return this.addDefaultOptionsToObjectNotation(o_objNoation, o_options);
	},
	
	addDefaultOptionsToObjectNotation: function(o_objNoation, o_options){
		var empty = {};
		var defaults = o_objNoation.options;
		var settings;
		options = this.verifAndModifOptions(o_objNoation, o_options);

		if (!$.isEmptyObject(options)) {
			settings = $.extend(true, empty, defaults, o_options);
			o_objNoation.options = settings;
		}
		return o_objNoation;
	},
	
	/**
	 * Retourn un nouvelle objet noation vierge. 
	 * Inqique aussi au moteur de la notations le nom de la notation qui est en cour de traitement
	 * 
	 * @param {Object}
	 *            o_attribut : attribut de type DOM ou se trouve la noation.
	 */
	createObjNotation: function (o_attribut) {
		var s_toUse = this.getAttNameforObj(o_attribut);
		this.s_nameObjEnTraitement = s_toUse;
		var o_toUse = globazNotation[s_toUse];
		var o_obj = this.newNotation(o_toUse);
		return o_obj;
	},
	
	 /**
	 * Creer un nouvell objet notation en passant une notation 
	 * Permet d'avoir une nouvelle instence de la notation
	 * @param o_toUse
	 * @returns o_obj0
	 */
	newNotation: function (o_notation) {
		var o_obj = Object.create($.extend({}, o_notation));
		if (o_notation.vars) {
			o_obj.vars = $.extend(true, {}, o_notation.vars);
		}
		return o_obj;
	},

	/**
	 * Permet d'ajouter des message d'erreur qui seront formaté
	 * 
	 * @param {Object}
	 *            s_typeError
	 * @param {Object}
	 *            s_elementInError
	 * @param {Object}
	 *            s_messageHtml
	 */
	putFormattedError: function (s_typeError, s_elementInError, s_messageHtml) {
		var s_message = "<div>" + "<strong>" + s_typeError + ": <i class='ui-state-error-text'>" + s_elementInError + "</i>" + "</strong><br />" + s_messageHtml + "</div>";
		this.putError(s_message);
	},

	/**
	 * @param {Object}
	 *            s_message
	 */
	addError: function (s_message) {
		this.putError.call(notationManager, s_message);
	},

	/**
	 * Convertit une chaine de caractère en objet. 
	 * Selon la convontion de la notation Cette fonction est appeler depuis un objet notation via sa référence. 
	 * Elle est en sorte hérité par l'objet.Elle ne doit pas être utilisé dans la moteur(notationManager)
	 * Créer un objet contentant les options passé en paramètre.
	 * 
	 * @param {Object}
	 *            s_options
	 */
	createOptionsForObj: function (s_options) {
		var o_att = {
			nodeValue: s_options
		}, o_options = null;

		if (!globazNotation.utils.isEmpty(s_options)) {
			o_options = notationManager.createOptions(o_att);
		}
		return o_options;
	},

	i18n: function (s_key, s_nameNotation) {
		return jQuery.i18n.prop("notation." +s_nameNotation + "." + s_key);
	},
	
	i18nFunction:  function (s_nameNotation) {
		var  that = this, f ;
		f = function (s_key) {
				return that.i18n(s_key, s_nameNotation);
		};
		return f ;
	},

	/**
	 * Ajoute dynamiquement les fonctions et les attributs aux objets de la notation. On fait une sort d'heritage
	 * 
	 * @param {Object}
	 *            o_objNoation (element(attriubt)dom )
	 * @param {Object}
	 *            $elementToPutObject
	 */
	addAttributAndMethodeToObjNotation: function (o_objNotation, $elementToPutObject) {
		var that = this;
		o_objNotation.s_contextUrl = this.s_contextUrl;
		o_objNotation.getImage = function (s_imgName) {
			return this.getImage.call(that, s_imgName);
		};

		o_objNotation.$elementToPutObject = $elementToPutObject;

		o_objNotation.utils = globazNotation.utils;
		o_objNotation.utils.input = globazNotation.utilsInput;
		o_objNotation.utils.formatter = globazNotation.utilsFormatter;
		o_objNotation.utils.date = globazNotation.utilsDate;
		o_objNotation.putError = this.addError;
		o_objNotation.putFormattedError = this.putFormattedError;
		o_objNotation.createParams = this.createOptionsForObj;
		o_objNotation.i18n = this.i18nFunction(notationManager.s_nameObjEnTraitement);
		return o_objNotation;
	},

	/**
	 * Initialise l'objet de la notation à utiliser Ajoute tout les fonctionalités à l'objet 
	 * notation passé en paramétre et lui applique les paramétres
	 */
	callOject: function (o_attribut, $elementToPutObject, $container) {
		var o_objNoation;
		
		o_objNoation = this.createObjNotation(o_attribut);
		o_objNoation = this.addOptionsToObjectNotation(o_objNoation, o_attribut);
		o_objNoation = this.addAttributAndMethodeToObjNotation(o_objNoation, $elementToPutObject);
		o_objNoation = this.initObjNotation(o_objNoation,  this.getAttNameforObj(o_attribut), $elementToPutObject);
		this.bindEventsToObject($elementToPutObject, o_objNoation, $container);

		return o_objNoation;
	},
	
	callObjectByJs: function (s_notation, o_options, $elementToPutObject, $container) {
		var o_notation = this.newNotation(globazNotation[s_notation]);
		o_notation = this.addDefaultOptionsToObjectNotation(o_notation, o_options);
		o_notation = this.addAttributAndMethodeToObjNotation(o_notation, $elementToPutObject);
	
		o_notation = this.initObjNotation(o_notation, s_notation, $elementToPutObject);

		this.bindEventsToObject($elementToPutObject, o_notation, $container);
	
		return o_notation;
	},
	
	/**
	 * Prépare l'objet notation qui doit être appliqué sur l'élément DOM
	 * 
	 * @param {Object}
	 *            o_attribut
	 * @param {Object}
	 *            $elementToPutObject
	 */
	callObjectToUseOnJS: function (s_notation, o_options, o_element) {
		var o_objNoation = null;
		var $element = (o_element instanceof jQuery)?o_element:$(o_element);
	
		if (this.hasSpecificFonction("callObjectToUseOnJS")) {
			o_objNoation = this.specific.callObjectToUseOnJS.call(this, s_notation, o_options, $element);
		} else {
			o_objNoation = this.callObjectByJs(s_notation, o_options, $element);
		}
		return o_objNoation;
	},
	

	/**
	 * Prépare l'objet notation qui doit être appliqué sur l'élément DOM
	 * 
	 * @param {Object}
	 *            o_attribut
	 * @param {Object}
	 *            $elementToPutObject
	 */
	callObjToUse: function (o_attribut, $elementToPutObject) {
		var o_objNoation = null;
		if (this.hasSpecificFonction("callObjToUse")) {
			o_objNoation = this.specific.callObjToUse.call(this, o_attribut, $elementToPutObject);
		} else {
			o_objNoation = this.callOject(o_attribut, $elementToPutObject);
		}
		return o_objNoation;
	},

	/**
	 * Lie les objets sur des events pour les exectuer plus tard
	 * 
	 */
	callObjOnEvent: function (o_attribut, $elementToPutObject, $container, s_eventToBind) {
		var that = this;
		$container.one(s_eventToBind, function () {
			that.callOject(o_attribut, $elementToPutObject, $container);
		});
	},

	/**
	 * retourn le nom de la noation définit dans l'syntaxe de la notation de l'élément
	 * 
	 * @param {Object}
	 *            o_attribute
	 */
	getAttNameforObj: function (o_attribute) {
		return o_attribute.nodeName.split('-g-')[1];
	},

	/**
	 * Créer le message d'erreur si il y une exception JS
	 * 
	 * @param {Object}
	 *            e
	 */
	messageJsException: function (e) {
		var lineNumber = 0;
		var source = "";
		if (e.lineNumber !== undefined) { // FF
			lineNumber = e.lineNumber;
			source = 'Source : ' + e.toSource();
		} else if (e.number !== undefined) { // IE
			lineNumber = e.number;
		}
		var message = "<div>" + "<strong>" + "Erreur indéfinit: <i class='ui-state-error-text'>" + e + " line: " + lineNumber + "</i>" + "</strong><br />" + source + "</div>";
		return message;
	},

	/**
	 * Recherche un élément DOM qui se trouve les plus près du sélécteur donnée.
	 * 
	 * @param {Object}
	 *            $elementToPutObject
	 * @param {Object}
	 *            selector
	 */
	findClosestElement: function ($elementToPutObject, selector, $container) {
		return $container.find(selector);
	},

	/**
	 * 
	 * @param {Object}
	 *            $elementTrigger
	 * @param {Object}
	 *            o_obj
	 * @param {Object}
	 *            s_nameId
	 * @param {Object}
	 *            s_nameObjEvent
	 * @param {Object}
	 *            s_event
	 */
	bindEventAndSearchElementToBind: function ($elementToPutObject, o_obj, s_nameId, s_nameObjEvent, s_event, $container) {
		if (o_obj !== null && typeof o_obj.bindEvent !== 'undefined') {
			var $ele = this.findClosestElement($elementToPutObject, s_nameId, $container);
			if ($ele.length) {
				this.bindEventWithObject(o_obj, $ele, s_nameObjEvent, s_event);
			}
		}
	},

	/**
	 * Lie un evenement à un objet
	 * 
	 * @param {Object}
	 *            o_obj
	 * @param {Object}
	 *            $elemtToBind
	 * @param {Object}
	 *            s_nameObjEvent
	 * @param {Object}
	 *            s_event
	 */
    bindEventWithObject: function (o_obj, $elemtToBind, s_nameObjEvent, s_event) {
        if (this.existFonction(o_obj.bindEvent[s_nameObjEvent])) {
           $elemtToBind.bind(s_event, {obj: o_obj, nameObjEvent: s_nameObjEvent}, function (event) {
              if($.contains(document.documentElement, event.data.obj.$elementToPutObject.get(0))){
            	  event.data.obj.bindEvent[event.data.nameObjEvent].call(event.data.obj, event);
              }
           });
        }
  	},


	/**
	 * lie les différents évenement lié au boutton a la notations passée en paramétre
	 * 
	 * @param {Object}
	 *            $elementToPutObject: element ou se trouve la noation
	 * @param {Object}
	 *            o_obj: objet notation
	 */
	bindEventButton: function ($elementToPutObject, o_obj, $container) {
		var s_event = 'click';

		if (this.hasSpecificFonction("bindEventButton")) {
			this.specific.bindEventButton.call(this, $elementToPutObject, o_obj, $container);
		} else if ($('#btnCtrlJade').length) {
			// bind pour les bouttons normeaux
			this.bindEventWithObject(o_obj, $("#btnCan"), "btnCancel", s_event);
			this.bindEventWithObject(o_obj, $("#btnNew"), "btnAdd", s_event);
			this.bindEventWithObject(o_obj, $("#btnVal"), "btnValidate", s_event);
			this.bindEventWithObject(o_obj, $("#btnUpd"), "btnUpdate", s_event);
			this.bindEventWithObject(o_obj, $("#btnDel"), "btnDelete", s_event);
		}
	},

	/**
	 * Lie les différents type évenements à la notation passée en paramétre
	 * 
	 * @param {Object}
	 *            $elementToPutObject: element ou se trouve la noation
	 * @param {Object}
	 *            o_obj: objet notation
	 */
	bindEventsToObject: function ($elementToPutObject, o_obj, $container) {
		if (o_obj !== null && typeof o_obj.bindEvent !== 'undefined') {
			this.bindEventButton($elementToPutObject, o_obj, $container);
			if (this.hasSpecificFonction("bindSpecialEvent")) {
				this.specific.bindSpecialEvent.call(this, $elementToPutObject, o_obj, $container);
			}
		}
	},

	/**
	 * Ajout le comportement d'une ou plusieurs notations définit sur l'élément html
	 * 
	 * @param {Object}
	 *            element: noeud DOM content le syntaxt de la notation
	 */
	addObjToElement: function (element, $elementTrigger) {
		var $elementTrigger1 = $elementTrigger || null;
		var that = this;
		var s_attName = "";
		var length = element.attributes.length;
		var attributes = element.attributes;
		var obj = null;
		
		for (var i = 0; i < length; i++) {
			s_attName = this.getAttNameforObj(attributes[i]);
			if (s_attName !== undefined && attributes[i].specified) {
				this.o_objEnTraitement = element;
				if (s_attName.toLowerCase() in globazNotation) {
					var strat = (new Date()).getTime();
					var $element = $(element);
					var s_nameNotation = s_attName.toLowerCase();
					if ($element.data("notation_" + s_nameNotation) == undefined) { // Hack ajout de se test pour IE. Permet de ne pas avoir les élémens a doubles.
						obj = that.callObjToUse(attributes[i], $element);
						$element.data("notation_" + s_nameNotation, obj);
						$element.data("notation_technical_" + s_nameNotation, ({
							$elementTrigger: $elementTrigger1,
							s_nameObj: s_nameNotation,
							t_time: (new Date()).getTime() - strat,
							id: s_nameNotation + "_" + globazNotation.utils.generateUniqueNumber()
						}));
					}
				} else {
					var message = "<div>" + "<strong>" + "La notation <i class='ui-state-error-text'>" + s_attName + "</i> est inexistant." + "</strong><br />" + "Soit le nom est mal orthographier ou le javascript n'a pas été ajoutée" + "</div>";
					this.putError(message);
				}
				this.o_objEnTraitement = null;
			}
		}
		element = null;
		this.o_objEnTraitement = null;
		return obj;
	},

	/**
	 * Créer une chaine de caractères contentant le nom des notations existantes. Permet de créer un filtre pour jquery puissse rechercher que les elements html qui contients une
	 * notation. Utilisé principalement pour IE pour que la recherche soit plus rapide.
	 */
	createNameObjForFilter: function () {
		var names = '';
		var virgule = '';
		for (var notation in globazNotation) {
			if (notation.indexOf('utils') === -1) {
				names += virgule + '[data-g-' + notation + ']';
				virgule = ',';
			}
		}
		return names;
	},

	process: function (start) {
		var n_size = this.$elementsNotation.size();
		var n_index = 0 + start;
		var i;
		var that = this;

		while (n_index < n_size && n_index < start + 20) {
			this.addObjToElement(this.$elementsNotation.get(n_index));
			n_index++;
		}

		if (i < n_size) {
			setTimeout(function () {
				that.fors(n_index);
			}, 5);
		} 
	},

	addNotationOnFragment: function ($fragment) {
		jsManager.executeBefore();
		this.findAndInitNotationIn($fragment);
		
		this.$html.triggerHandler(eventConstant.NOTATION_FRAGMENT_DONE);
		jsManager.executeAfter();
		this.displayErrors();
	},
	
	addNotationOnFragmentWithoutEvents: function ($fragment) {
		this.findAndInitNotationIn($fragment);
		
		this.$html.triggerHandler(eventConstant.NOTATION_FRAGMENT_DONE);
		this.displayErrors();
	},


	findElementWhoHasNotation: function ($scope) {
		var t_notationsTrieesParDegree = [],
		    t_elements = [];

		for (var keyNotation in globazNotation) {
			if (!globazNotation[keyNotation].forTagHtml || globazNotation[keyNotation].forTagHtml === 'Utils') {
				continue;
			}
	
			var o_notation = {
				name: keyNotation
			};
			
			if (globazNotation[keyNotation].degreePrioritee) {
				o_notation.prioritee = globazNotation[keyNotation].degreePrioritee;
			} else {
				o_notation.prioritee = 0;
			}
			
			t_notationsTrieesParDegree.push(o_notation);
		}
		
		t_notationsTrieesParDegree.sort(function (a, b) {
			return b.prioritee - a.prioritee;
		});
	
	
		for (var i = 0; i < t_notationsTrieesParDegree.length; i++) {
			var o_wrapperInfoNotation = t_notationsTrieesParDegree[i];
			$scope.find('[data-g-' + o_wrapperInfoNotation.name + ']').each(function (index, element) {
				t_elements.push(element);
			});

			if ($scope.is('[data-g-' + o_wrapperInfoNotation.name + ']')) {
				t_elements.push($scope.get(0));
			}
		}
		

		return t_elements;
	},
	
	findAndInitNotationIn: function ($scope) {
		var start = (new Date()).getTime();
		var elements = this.findElementWhoHasNotation($scope);
		this.o_infosMoteur.t_timeInitJquery = (new Date()).getTime() - start;
		for (var i = 0; i < elements.length; i++) {
			this.addObjToElement(elements[i]);
		}
	},

	/**
	 * Recherche tout les objets DOM qui contient la syntaxe de la notations. Fonction principale qui ajoute tout les comportements des notations sur les elements DOM
	 */
	addToAllElementJs: function () {
		this.findAndInitNotationIn($(document));
	},

	/**
	 * Permet d'afficher la console (element qui s'affiche de puis le haut). Même console que pour les erreur mais avec un style différent
	 * 
	 * @param {Object}
	 *            s_html: String html que l'on veut afficher dans la console.
	 */
	consoleInfo: function (s_html) {
		globazNotation.utils.console(s_html, 'Infos', 'infos', '90%');
	},

	/**
	 * Raccourcie pour l'utilisation de puis fireBug. Affiche une statistique sur les temps des éléments qui contient une notation
	 */
	showInfos: function () {
		this.displayAllElelementsWhoHasNotation();
	},

	displayAllElelementsWhoHasNotation: function () {
		var s_html = "", that = this, $body = $('body');
		s_html = this.displayElelementsWhoHasNotation();
		s_html = s_html + this.displayElelementsWhoHasNotationOnEvent();
		this.consoleInfo(s_html);
		$('.notation')
				.find('tr')
				.click(function () {
					var obj = {},$ele;
					var element = this;
					that.iterOnElementsWhoHasNotation(function ($element,notation,notationTechnical) {
						if (notationTechnical.id == element.id) {
							obj = notationTechnical;
							$ele  = $element;
						}
					});
	
					$body
							.append("<span class='tooltip' style='position:absolute;top:" + ($ele.position().top - 13) + ";left:" + $ele.position().left + "'>" + obj.s_nameObj + "</span>");
					$ele.css('border', '3px dotted green');
				});
	},

	displayElelementsWhoHasNotationOnEvent: function () {
		var n_length = this.mapEmentsTriggers.length;
		var s_html = "";
		for (var i = 0; i < n_length; i++) {
			s_html += this.displayElelementsWhoHasNotation(this.mapEmentsTriggers[i]);
		}
		return s_html;
	},

	ti: function () {
		var $body = $('body');

		this.iterOnElementsWhoHasNotation(function ($element,notation,notationTechnical) {
				$body
						.append("<span class='tooltip' style='position:absolute;top:" + ($element.position().top - 13) + ";left:" + $element.position().left + "'>" + notationTechnical.s_nameObj + "</span>");
				$element.css('border', '3px dotted green');
		});
	},

	/**
	 * Affiche une statistique sur les temps des éléments qui contient une notation
	 */
	displayElelementsWhoHasNotation: function (o_elementTrigger) {
		var i = 0;
		var n_compteur = 0;
		var t_timeObj = 0;
		var $elementTrigger = null;
		var o_elementTrigger1 = o_elementTrigger || null;
		var s_html = "";

		var s_titre = "Element Html";
		if (o_elementTrigger1 !== null) {
			s_titre = "Elements trigger " + o_elementTrigger1.$elementTrigger[0].nodeName + " class:" + o_elementTrigger1.$elementTrigger.attr("class") + " id: " + o_elementTrigger1.$elementTrigger
					.attr("id");
		}
		s_html = "<table class='notation'>" + 
					"<thead>" + 
						"<tr>" + 
							"<th>" + s_titre + "</th>" + 
							"<th>Obj notation</th>" + 
							"<th>T(ms)</th>" + 
						"</tr>" + 
					"</thead>" + 
				"<tbody>";
		
		this.iterOnElementsWhoHasNotation(function ($ele,notation,notationTechnical) {
			$elementTrigger = notationTechnical.$elementTrigger;
			if ((o_elementTrigger1 === null) || o_elementTrigger1.$elementTrigger === $elementTrigger) {
				if ((o_elementTrigger1 === null && $elementTrigger === null) || ($elementTrigger !== null && o_elementTrigger1 !== null)) {
					n_compteur++;
					var odd = n_compteur % 2, c;
					if (odd === 1) {
						c = 'odd';
					} else {
						c = 'even';
					}
					t_timeObj = t_timeObj + notationTechnical.t_time;
					
					s_html += "<tr class='" + c + "' id='" + notationTechnical.id + "'>" + "<td>" + $ele.wrap('<span />').parent().encHTML() + "</td>" + "<td>" +notationTechnical.s_nameObj + "</td>" + "<td>" + notationTechnical.t_time + "</td>" + "</tr>";
				}
			}
		});
		
		if (o_elementTrigger1 === null) {
			o_elementTrigger1 = this.o_infosMoteur;
		}
		var total = "<tr>" + 
						"<td colspan=\"3\">" + "&nbsp;" + "</td>" + 
					"</tr>" + 
					"<tr style=\"font-weight: bold;\">" + 
						"<td>" + "Init Jquery elements" + "</td>" + 
						"<td>" + "&nbsp;" + "</td>" + 
						"<td>" + o_elementTrigger1.t_timeInitJquery + "</td>" + 
					"</tr>" + 
					"<tr style=\"font-weight: bold;\">" + 
						"<td>" + "Moteur notation" + "</td>" + 
						"<td>" + "&nbsp;" + "</td>" + 
						"<td>" + (o_elementTrigger1.t_timeMoteur - t_timeObj - o_elementTrigger1.t_timeInitJquery) + "</td>" + 
					"</tr>" + "<tr style=\"font-weight: bold;\">" + 
						"<td>" + "Element Notation" + "</td>" + 
						"<td>" + "&nbsp;" + "</td>" + 
						"<td>" + t_timeObj + "</td>" + 
					"</tr>" + 
					"<tr style=\"font-weight: bold;\">" + 
						"<td>" + "Total" + "</td>" + 
						"<td>" + n_compteur + "</td>" + 
						"<td>" + o_elementTrigger1.t_timeMoteur + "</td>" + 
					"</tr>" + "<tr style=\"font-weight: bold;\">" + 
						"<td>" + "Temps js pour type page: " + this.s_typePage + "</td>" + 
						"<td>" + "&nbsp;" + "</td>" + 
						"<td>" + o_elementTrigger1.t_timeJsAutre + "</td>" + 
					"</tr>";

		s_html += total + "</tbody></table>";

		return s_html;
	},

	/**
	 * Permet d'ajouter des messages d'erreurs liée aux notation. Les messages d'erreurs seront affiché avec la console.
	 * 
	 * @param {Object}
	 *            message
	 */
	putError: function (message) {
		var msg = message;
		if (this.o_objEnTraitement !== null && this.o_objEnTraitement !== undefined) {
			msg += '<div class="errorElement"><span class="errorElementText">Erreur dans cet élément: </span>';
			if (this.o_objEnTraitement) {
				msg += '<pre>' + $(this.o_objEnTraitement).wrap('<span />').parent().encHTML() + '</pre>';
			}
			msg += '</div>';
		}
		this.elementsInError.push({
			message: msg,
			ele: this.o_objEnTraitement
		});
		this.hasError = true;
	},

	timeHtml: function () {
		var html = "<div>Time: " + this.o_infosMoteur.t_timeMoteur + "milliseconds</div>";
		return html;
	},

	displayAllErrors: function () {
		var html = "<a href=" + this.lienForTest + ">Il est possible de tester les notations ici</a><br />" + "<a href=" + this.lienForDocumentation + ">Documentation pour le notations</a>";
		if (this.hasError) {
			var $ele;
			for (var el in this.elementsInError) {
				html += "<p>" + this.elementsInError[el].message + "</p>";
				$ele = $(this.elementsInError[el].ele);
				$ele.addClass('notationInError');
			}
			html += this.timeHtml();
			globazNotation.utils.consoleError(html, 'Error in notation');
		}
	},

	displayErrors: function () {
		var that = this;
		if (this.showErrors) {
			setTimeout(function () {
				that.displayAllErrors();
			}, 500);
		}
	},

	getImage: function (s_imgName) {
		return this.s_pathImg + s_imgName;
	},

	validateAndDisplayError: function () {
		globazNotation.utils.logging.clearCache();
		var b_isValid = true;
		if (!this.validate()) {
			var s_errorMessage = "";
			$.each(globazNotation.utils.logging.o_cache, function () {
				if (this.n_level > globazNotation.utils.logging.LEVEL_INFO && s_errorMessage.indexOf(this.s_message) == -1) {
					s_errorMessage += this.s_message + '<br/>';
				}
			});
			if (s_errorMessage != '') {
				globazNotation.utils.consoleError(s_errorMessage);
				globazNotation.utils.logging.clearCache();
				b_isValid = false;
			}
		}
		this.$html.trigger(eventConstant.FORM_VALIDATION, {
			b_isFormValid: b_isValid
		});
		return b_isValid;
	},

	validate: function () {
		var b_isFormValid = true;
		this.iterOnElementsWhoHasNotation(function ($element,notation,notationTechnical) {
			if ($.isFunction(notation.validate)) {
				b_isFormValid &= notation.validate();
			}
		});
		return b_isFormValid;
	},
	
	
	iterOnElementsWhoHasNotation: function(callBack, $scope) {
		var elements = this.findElementWhoHasNotation(($scope)?$scope:$(document));
		var s_attName = "",
		    length,
		    attributes,
		    s_nameNotation,
		    element;
		
		for (var i = 0; i < elements.length; i++) {
			element = elements[i];
			length = element.attributes.length;
			attributes = element.attributes;
			for (var j = 0; j < length; j++) {
				s_attName = this.getAttNameforObj(attributes[j]);
				if (s_attName !== undefined && attributes[j].specified){
					s_nameNotation = s_attName.toLowerCase();
					if (s_nameNotation in globazNotation) {
						var $element = $(element);
						var notation = $element.data("notation_" + s_nameNotation);
						var notationTechnical = $element.data("notation_technical_" + s_nameNotation);
						callBack($element,notation,notationTechnical);
					} 
				}
			}
		}
	},

	initFocusColorBehavior: function () {
		if (!this.b_isFocusInit) {
			this.$html.on('focus','input:not(:button,:submit,:radio),textarea', function () {
				var $this = $(this);
				if ($this.not(':hidden,:disabled')) {
					if (!this.readOnly) {
						$this.addClass('hasFocus');
					}
				}
			});
			
			this.$html.on('focusout','input:not(:button,:submit,:radio),textarea', function () {
				$(this).removeClass('hasFocus');
			});
			this.b_isFocusInit = true;
		}
	}
};



/*


$(window).unload(function () {
	function purge(d) {
		var a = d.attributes, i, l, n;
		if (a) {
			l = a.length;
			for (i = 0; i < l; i += 1) {
				n = a[i].name;
				if (typeof d[n] === 'function') {
					d[n] = null;
				}
			}
		}
		a = d.childNodes;
		if (a) {
			l = a.length;
			for (i = 0; i < l; i += 1) {
				purge(d.childNodes[i]);
			}
		}
	}
	
	for (var i = 0; i < notationManager.elementsWhoHasNotation.length; i++) {
		for (var key in notationManager.elementsWhoHasNotation[i]) {
			notationManager.elementsWhoHasNotation[i][key] = null;
		} 
		notationManager.elementsWhoHasNotation[i] = null;
	}
	//purge($("html").get(0));
});

*/

notationManager = new NotationManager();