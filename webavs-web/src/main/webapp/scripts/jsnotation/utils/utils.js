globazNotation.utils = {

	author: 'DMA',
	forTagHtml: 'Utils',
	type: globazNotation.typesNotation.UTILITIES,

	_s_console: null,
	_s_context: null,// utilisÃ© pour mettre en cache
	_s_langue: null,
	_s_formAction: null,
	_n_cpt: 0,
	_t_functionOnCloseConsole: [],

	/**
	* Fonction qui génère un nombre unique basée sur une valeur max
	*/
	generateUniqueNumber: function () {
		this._n_cpt++;
		return this._n_cpt;
	},

	console1: function (s_html, s_title, s_class, b_overflow) {
		this.console(s_html, s_title, s_class, undefined, b_overflow);
	},

	console: function (html, s_title, s_class, n_width, b_overflow, o_buttons) {
		var $html, that = this;

		if(html instanceof jQuery) {
			$html = html;
		    html.wrap('<div class="globaz_utils_console">');
		} else {
			$html = $('<div class="globaz_utils_console">' + html + '</div>');
		}
		
		if (typeof o_buttons === "undefined")  {
			o_buttons = {};
		}
	
		$html.dialog({
			position: 'top',
			title: s_title,
			width: n_width || 750,
			show: "blind",
			hide: "blind",
			closeOnEscape: true,
			dialogClass: s_class,
			buttons: o_buttons
		});
		if (b_overflow) {
			$html.dialog("widget").find(".ui-dialog-content").css("overflow", "hidden");
		}

		var length = that._t_functionOnCloseConsole.length;
		$html.bind(eventConstant.UTILS_CONSOLE_CLOSE, function (event, ui) {
			for (var i = 0; i < length; i++) {
				that.callFunctionForCloseConsole(that._t_functionOnCloseConsole[i].f, that._t_functionOnCloseConsole[i].o, event, ui);
			}
		});
	},

	callFunctionForCloseConsole: function (f_function, o_context, event, ui) {
		if (typeof o_context === "undefined" || o_context === null) {
			f_function(event, ui);
		} else {
			if ($.isPlainObject(o_context)) {
				f_function.call(o_context, event, ui);
			}
		}
	},

	eventCloseConsole: function (f_function, o_context) {
		if (!$.isFunction(f_function)) {
			this.consoleError("The given parameter for the function onCloseConsole is not a function", "Not function !!!!!!!");
		}
		this._t_functionOnCloseConsole.push({
			f: f_function,
			o: o_context
		});
	},

	/**
	* Retourne le context(chemin) de l'application. 
	* Pas besoin d'utilser cette fonction dans un objet de la notation, car chaque objet a une variable(s_contextUrl) qui contient
	* cette infos.
	*/
	getContextUrl: function () {
		if (this._s_context === null) {
			this._s_context = $('[name=Context_URL]').attr('content');
		}
		return this._s_context;
	},

	getLangue: function () {
		if (this._s_langue === null) {
			this._s_langue = $('[name=User-Lang]').attr('content');
		}
		return this._s_langue;
	},

	getFromAction: function () {
		if (this._s_formAction === null) {
			this._s_formAction = $('[name=formAction]').attr('content');
		}
		return this._s_formAction;
	},

	isTextHtml: function (s_value) {
		if ($.type(s_value) !== "string") {
			return false;
		}
		return $.trim(s_value).indexOf('<') === 0;
	},

	isString: function (value) {
		var retour = false;
		if (typeof value === 'string') {
			retour = true;
		}
		return retour;
	},

	firstUpperCase: function (s_value) {
		return s_value.substr(0, 1).toUpperCase() + s_value.substr(1, s_value.length);
	},

	consoleError: function (s_html, title) {
		var titre = title || 'Error';
		if ($.type(s_html) === "string") {
			if (!this.isTextHtml(s_html)) {
				s_html = s_html.replace(/[\r\n]/g, '<br />');
			}
			this.console(s_html, titre, 'ui-state-error');
		} else if(s_html instanceof jQuery) {
			this.console(s_html, titre, 'ui-state-error');
		}
	},

	consoleInfo: function (s_html, title, b_overflow) {
		var titre = title || 'Infos', overflow = b_overflow || false;

		if (!this.isTextHtml(s_html)) {
			s_html = s_html.replace(/[\r\n]/g, '<br />');
		}
		this.console1(s_html, titre, 'ui-state-infos', overflow);
	},
	
	consoleWarn: function (s_html, title, b_overflow) {
		var titre = title || 'Infos', overflow = b_overflow || false;

		if (!this.isTextHtml(s_html)) {
			s_html = s_html.replace(/[\r\n]/g, '<br />');
		}
		
		this.console1(s_html, titre, 'ui-state-highlight', overflow);
	},

    dialogWarn: function (s_html, o_buttons, parent) {
	    var frame= window
	    if(parent){
            frame = parent
        }
        var $html = frame.$('<div class="globaz_utils_console">' + s_html + '</div>');
        $html.dialog({
            minHeight:300,
            position: 'center',
            title: jQuery.i18n.prop('notation.avertissement.message'),
            show: "blind",
            hide: "blind",
            closeOnEscape: true,
            dialogClass: 'ui-state-highlight',
            buttons: o_buttons
        })
        if(o_buttons) {
            $html.dialog("widget").find(".ui-dialog-buttonpane").css("background", "#fff0");
            $html.dialog("widget").find(".ui-dialog-buttonpane").css("background-color", "#fff0");
            $html.dialog("widget").find(".ui-dialog-buttonpane").css("background-image", "none");
        }
    },

	// fonction pour afficher les attribut de l'objet utils pour les tests
	displaysObject: function (obj) {
		var s = "<p>tes</p><p>";
		for (var i in obj) {
			if (typeof obj[i] === 'object') {
				for (var j in obj[i]) {
					if (j === 'nodeName') {
						s += obj[i] + "<br />";
						s += obj[i].nodeName + " " + obj[i].nodeValue + "<br />";
					}
				}
			}
		}
		s += "</p>";
		return s;
	},

	isEmpty: function (value) {
		if (this.isString(value)) {
			value = $.trim(value);
		}
		if (value !== null && value !== undefined && value !== '' && value !== 'undefined' && value !== 'null') {
			return false;
		} else {
			return true;
		}
	},

	isJQueryObject: function ($objectToTest) {
		return $objectToTest instanceof jQuery;
	},

	i18nWithPlainKey: function (s_key) {
		return jQuery.i18n.prop(s_key);
	},

	getDefaultHtmlTagHeight: function ($elementHtml) {
		if (this.isJQueryObject($elementHtml) && $elementHtml.length) {
			var $element = $elementHtml;
			var $body = $('body');
			$body.append($element);
			$element.show();
			var n_height = $element.outerHeight();
			$element.hide();
			$element.remove();
			return n_height;
		} else {
			return 0;
		}
	},

	getDefaultInputHeight: function () {
		return this.getDefaultHtmlTagHeight($('<input/>'));
	},

	validateFormOnSubmit: function ($form) {
		$form.submit(function () {
			return notationManager.validateAndDisplayError();
		});
	}
};

var $$c = function (s_string) {
	globazNotation.utils.console(s_string);
};
 
var $$t = function (s_string) {
	globazNotation.utils._s_console += s_string + "<br/>";
};

$(function () {
	$('html').bind(eventConstant.NOTATION_MANAGER_DONE, function () {
		if (globazNotation.utils._s_console !== null) {
			globazNotation.utils.console(notationManager._s_console);
		}
	});
});



/**
 * Permet d'appliquer un template à un objet JSON
 * 
 * L'écriture du template ce fait avec {{variable}}
 * 	EX: html -> <div> {{nom}} {{prenom}} </div>
 * 		js  ->  globazNotation.template.compile(json, s_template);
 * 
 * Il existe 2 fonction:
 * 						compile-> retourne du string
 * 						complie$-> Retourn un objet jquery
 * 
 * Il est possible de lier des zones dans un template afin de le mettre à jour par ajax.
 * IL suffit de définir dans le template qu'elle sera la zone a lier en utilisant l'attribut data-bind et de lui donner un nom.
 * 	EX: En html ->  data-bind="ajaxLignes"
 * 		En js -> globazNotation.template.update(JSON, "ajaxLignes");
 * 
 * Pour appliquer une class paire a une liste il faut utiliser la propriété @odd et définir la class que l'on veut appliquer
 * 	EX: <tr id="{{idNote}}" class="{{@odd odd}}" >'
 * 
 * Pour appliquer un boucle:
 * 	Ex: tempalte -> {{@each lignes}}<tr><td>{{nom}}</td><tr>{{@each lignes}}
 * 		JSON -> {lignes: [{nom: 'tata'}, {nom:'toto'}]}
 * 
 * @name globazNotation.template
 * @cat Utils/template
 * @author dma
 */
globazNotation.template = {
	map: {},
	s_mainCompileTemlate: null,
	
	replaceData: function (html, key, data) {
		return html.replace(new RegExp("\\{\\{" + key + "\\}\\}", 'g'), data);
	},
	
	applyListTemplate: function (o_data, s_template) {
		var html = "", o_donne, s_templateOdd;
		var reg = /\{\{@odd\s*(\w*)\}\}/g;
		var matches = s_template.match(/\{\{@odd\s*(\w*)\}\}/);
		s_templateOdd = s_template;
		if (matches && matches.length) {
			s_templateOdd = s_template.replace(new RegExp(matches[0], 'g'), matches[1]);
			s_template = s_template.replace(new RegExp(matches[0], 'g'), "");
		}
		//TODO ne renvoyer une chaine vide si pas de match
		if ($.isArray(o_data)) {
			for (var i = 0; i < o_data.length; i++) {
				if (i % 2) {
					html += this.compile(o_data[i], s_templateOdd);
				} else {
					html += this.compile(o_data[i], s_template);
				}
			}
		}
		return html;
	},
	
	findTemplateEach: function (s_key, s_template) {
		var regex = new RegExp("<[^<.]* \\{\\#each[\\s]*" + s_key + "\\}[^>.]*>");
		var matches = s_template.match(regex); 
		var s_templateEach = "";
		var s_tag; 
		if (!matches) {
			regex = new RegExp("\\{\\{\\@each[\\s]*" + s_key + "\\}\\}(\\s*.*){\\{\\/@each[\\s]*" + s_key + "\\}\\}");
			matches = s_template.match(regex);
			if(matches){
				s_templateEach = s_template.match(regex)[1];
				this.s_mainCompileTemlate = this.s_mainCompileTemlate.replace(matches[0], "{##each" + s_key + "}");
			}
		} //else {
//			s_tag = this.replaceChar(matches[0]);
//			s_templateEach = this.findInTemplate(s_mainTemplate, s_tag);
//			s_templateEach = s_templateEach.replace(new RegExp("[\\s]*\\{\\{#each[\\s]*" + key + "\\}[\\s]*")," "); 
//			return s_templateEach ;
		//}
		
		return s_templateEach;
	},
	
	findTagBind: function (s_template, s_key) {
		var regex = new RegExp("<[^<.]* [\\s]*data-bind[\\s]*=[\\s]*[\"|']ajaxLignes[\"|'][\\s]*[^>.]*>");
		var matches = s_template.match(regex);
		return matches[0];
	},
	
	findNameTag: function (s_tag) {
		var regex = /<\s*(\S*)/; 
		var matches = s_tag.match(regex);
		return matches[1];// on séléctionnne la 1ere capture;
	},
	
	findInTemplate : function (s_template, s_tag) {
		var s_tagName = this.findNameTag(s_tag);
		var regex = new RegExp(s_tag + "[\\s\\S]*<\/" + s_tagName + ">");
		var s_ligne = s_template.match(regex)[0];
		return s_ligne;
	},
	
	findBindTempalte: function (s_template, s_key) {
		var s_tag = this.findTagBind(s_template, s_key);
		return this.findInTemplate(s_template, s_tag);
	},
	
	compile: function (o_data, s_template) {
		var	s_ligneTemplate,
		s_lignes,
		o_donne,
		s_liste,
		html = s_template;

		for (var key in o_data) {
			var r = new RegExp("\\{\\{@each\\s*"+key+"\\s*\\w*\\}\\}");
			if ($.isArray(o_data[key]) && r.test(s_template)) {
			
				this.s_mainCompileTemlate = html;
				s_ligneTemplate = this.findTemplateEach(key, s_template);
				s_lignes = this.applyListTemplate(o_data[key], s_ligneTemplate);
				html = this.s_mainCompileTemlate;
				html = html.replace("{##each" + key + "}", s_lignes); 
			} else {
				html = this.replaceData(html, key, o_data[key]);
			}
		}
		return html;
	},
	
	compile$: function (o_data, s_template) {
		this.s_mainCompileTemlate = s_template;
		var $html = $(this.compile(o_data, s_template)), that = this;
		$html.find("[data-bind]").each(function () {
			var $this = $(this);			
			that.map[$this.attr("data-bind")] = {$element: $this, s_template: s_template};
		});
		this.bind(o_data, s_template, $html);
		return $html;
	},
	
	odd: function () {
		
	},
	
	bind: function (o_data, s_tempate, $html) {
		
	},
	
	update: function (o_data, s_name) {
		var template = this.findBindTempalte(this.map[s_name].s_template, s_name);
		var s_html = this.compile(o_data, template);
		var $shtml = $(s_html);
		
		this.map[s_name].$element.replaceWith($shtml);
		this.map[s_name].$element = $shtml;
	},
	
	applyTemplate: function (o_data, s_template) {	
		var html = this.compile(o_data, s_template);
		return html;
	}
};
