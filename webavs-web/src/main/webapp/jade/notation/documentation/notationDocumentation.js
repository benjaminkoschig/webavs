/**
 * @author DMA
 */

var notationDocumentation = {

	map_menuTag: [],
	cache: null,
	rows: null,
	creatDocParam: function (obj) {
		var html = "<div class=options><h2>Liste des options possibles</h2>";
		if (obj.hasOwnProperty('description')) {
			var options = obj.descriptionOptions;
			for (var opt in options) {
				html += "<div class ='option'><h3>" + opt + "</h3><p>" + options[opt].desc + "</p>" + "<h4>Valeurs possible</h4>" + "<p>" + options[opt].param + "</p></div>";
			}
		} else {
			html = html + "Aucune description des options existes !!!!";
		}
		return html + "</div>";
	},

	hasDescription: function (obj) {
		return obj.hasOwnProperty('description');
	},

	getDesciption: function (obj) {
		var desc = "";
		if (this.hasDescription(obj)) {
			desc = obj.description;
		} else {
			desc = "Pas de descriptions";
		}
		return desc;
	},

	createHtmlForCode: function (s_code, brush) {
		return '<div class="exempleCode"><pre class="brush: ' + brush + '; toolbar: false">' + s_code + '</pre></div>';
	},

	addExemple: function (s_nameObjet) {
		var code = '', $ele = $('#' + s_nameObjet).detach(),
		// $ele = $('#'+s_nameObjet).clone().remove(),
		html = '<div class="formTableLess exemple"> <h2>Exemple </h2>';
		html += '<div>' + $ele.html() + '</div><br />';
		html += '<h4>Code</h4>';
		code = this.createHtmlForCode($ele.clone().html(), 'html');
		html += code;
		html += '</div> <br class="ui-helper-clearfix clear" />';
		return html;
	},

	getAuthor: function (objNotation) {
		var author = "none :(";
		if (objNotation.hasOwnProperty('author')) {
			author = objNotation.author;
		}
		return author;
	},

	createTitre: function (s_nameObjet, descCourt, objNotation) {
		var classUtils = "";
		var s_degree = (objNotation.degreePrioritee)?"<span class='degreePrioritee'> DegreePrioritee: "+objNotation.degreePrioritee+"</span>":"";
		if (objNotation.type === globazNotation.typesNotation.UTILITIES) {
			classUtils = "utilsTitre";
		}
		return "<h1 class='ui-corner-top " + classUtils + " '><span class='titreNotation'>" + s_nameObjet + 
				"</span> <span class='descCour'>" + descCourt + "...</span>" + s_degree + "<span class='author'>" + 
				this.getAuthor(objNotation) + "</span></h1>";
	},

	createDocObjct: function (s_nameObjet) {
		var html = "<li  class='ui-corner-bottom'>";
		var obj = globazNotation[s_nameObjet];
		var desc = this.getDesciption(obj);
		var descCourt = desc.split('<')[0].substring(0, 80);
		if (obj.type !== globazNotation.typesNotation.UTILITIES) {
			html += this.createTitre(s_nameObjet, descCourt, obj);
			html += "<div class='objetNotation'><p class='descriptionObj'>" + desc + "</p>";
			html += "<p><strong>Utilisable sur ces elements html:</strong> <span class='onElementHtml'>" + this.getForTagHtml(s_nameObjet) + "</span></p>";
			if (this.hasDescription(obj)) {
				html = html + this.creatDocParam(obj);
			}
			html += this.addExemple(s_nameObjet);
		} else {
			
			html += this.displayFunction(s_nameObjet, desc, descCourt);
		}
		return html + "</div></li>";

	},

	getForTagHtml: function (s_nameObjet) {
		var obj = globazNotation[s_nameObjet];
		var tags = "";
		if (obj.hasOwnProperty('forTagHtml')) {
			tags = obj.forTagHtml.toLowerCase();
		}
		return tags;
	},

	createMenuForTagElement: function (s_nameObjet) {
		var obj = globazNotation[s_nameObjet];
		var tags = this.getForTagHtml(s_nameObjet);

		if (tags !== "") {
			var arr = obj.forTagHtml.toLowerCase().split(',');
			for ( var i = 0; i < arr.length; i++) {
				this.map_menuTag[$.trim(arr[i])] = $.trim(arr[i]);
			}
		}
	},

	filtreForTag: function (term) {
		var that = this;
		if (!term || term === 'all') {
			this.rows.parents('li').show().addClass('keynav withoutfocus');
		} else {
			this.rows.parents('li').hide().removeClass('keynav withfocus withoutfocus');

			this.cache.each(function (i) {
				if (this.indexOf(term) > -1) {
					$(that.rows[i]).parents('li').show().addClass('keynav withoutfocus');
				}
			});
		}
	},

	createHtmlForFunction: function (s_nameFunction, o_objet,s_nameObjet) {
		var notIn = 'forTagHtml,description,descriptionOptions';
		var html = '';
		var o_function = o_objet[s_nameFunction];
		if (notIn.indexOf(s_nameFunction) === -1) {
			if (!globazNotation.utils.isEmpty(o_function) && o_function !== true && o_function !== false) {
				html = '<div >';
				html += '<h3>' + s_nameFunction + '</h3>';
				html += '<div class="comment">' + this.createDocForComment(s_nameObjet, s_nameFunction) + '</div>';
				html += '<div class="codeJs">' + this.createHtmlForCode(o_function.toString(), 'js;gutter: false') + '</div>';
				html += '</div>';
			}
		}
		return html;
	},

	createDocForComment: function (s_nameObjet, s_nameFunction) {
		var s_source ="";
		var t_functions;
		var s_nameFunctiontTrim;	
		if(this.o_scriptsLin[s_nameObjet] && this.o_scriptsLin[s_nameObjet].functions){
			t_functions = this.o_scriptsLin[s_nameObjet].functions;		
			for ( var i = 0; i < t_functions.length; i++) {
				s_nameFunctiontTrim = t_functions[i].name.replace(/\"/g,"");
				if(t_functions[i] &&  s_nameFunctiontTrim === s_nameFunction){
					s_source = t_functions[i].docComment;
					break;
				}
			}
		}
		s_source = s_source.replace(/\/\*/g,"");
		s_source = s_source.replace(/\*\//g,"");
		s_source = s_source.replace(/\*/g,"");
		return s_source;

	},
	
	displayFunction: function (s_nameObjet, desc, descCourt) {
		var obj = globazNotation[s_nameObjet];
		var html = this.createTitre(s_nameObjet, descCourt, obj);
		html += "<div class='objetNotation'> " + "<span style='display:none' class='onElementHtml'>" + 
				this.getForTagHtml(s_nameObjet) + "</span>" + "<p class='descriptionObj'>" + desc + "</p>";
		for (var f in obj) {
			html += this.createHtmlForFunction(f, obj,s_nameObjet);
		}
		return html + '</div>';
	},

	docJava: function () {
		var regex = /([\/][*][*][\]*[\w\s,.\/'"#%\(\)\\=|\{\}&$!\[\];\+\-\?\ˆ§<>¢ß¥£™©®ª×÷±²³¼½¾µ¿¶·¸º°¯§…¤‡¬¨‰ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØŒŠþÙÚÛÜÝŸàáâãäåæçèéêëìíîïðñòóôõöøœšÞùúûüýÿ°]*[*\/])([\s\w]*[ ]*:[ ]*[function ]*\([ ]*.*\))/g;
		var input = "your input string";
		var $srciptNotation = $('script[src*="jsnotation/utils.js"]');
		$srciptNotation.each(function () {
			$.ajax({
				url: this.src,
				dataType: 'text',
				success: function (data, textStatus) {
					var matches = data.match(regex);
				}
			});
		});

	},

	createDocumentation: function (o_scriptsLint) {
		var html = "<ul id='notation'>";
		var infoCouleur = "<div id='infosCouleur'><div style='background-color:#C1DCF7' class='infosCouleur' id='bojNotation' /><span>Objets notation</span>" + "<br /><div class='utilsTitre infosCouleur' id='utilsNotation' /><span>Utilitaires</span></div>";
		var menuTag = "<div id='menuTag'>Notation pour : <a href='#' id='all'> all</a> ";
		var version = "<div>Jquery:" + $.fn.jquery + "</div><div>UI:" + $.ui.version + "</div>";
		var tab = [];
		var that = this;
		this.o_scriptsLin = {};

		// this.docJava();
		// Suprime tout les cas tests qui sont en erreur
		$('.testInError').remove();

		for (var fonction in globazNotation) {
			for (var key in o_scriptsLint){
				if(key.indexOf(fonction+".js")>0){
					this.o_scriptsLin[fonction] = o_scriptsLint[key].data;
				}
			}
			tab.push(fonction);
		}

		tab.sort();

		for (var j = 0; j < tab.length; j++) {
			html = html + this.createDocObjct(tab[j]);
			this.createMenuForTagElement(tab[j]);
		}

		html = html + "</ul>";

		SyntaxHighlighter.all();

		for (var n in this.map_menuTag) {
			menuTag += " <a href='#' id=" + n + ">" + n + "</a> ";
		}
		html = version + infoCouleur + menuTag + "</div>" + html;

		var $html = $(html).find('.objetNotation').hide().end();

		var effet = '';

		// workaround pour ie :(
		if ($.support.noCloneEvent) {
			effet = 'blind';
		}
		$html.find('h1,.explication').toggle(function () {
			$(this).next().show(effet);
		}, function () {
			$(this).next().hide(effet);
		});

		$('.explication').toggle(function () {
			$(this).next().show('blind');
		}, function () {
			$(this).next().hide('blind');
		});

		// SyntaxHighlighter.defaults['html-script'] = true;

		$html.appendTo("#documentationNotation");
		$("#searchNotation").liveUpdate("#notation");

		this.rows = $html.find('.onElementHtml');
		this.cache = this.rows.map(function () {
			return $(this).text().toLowerCase();
		});

		// $html.find("#menuTag").find('a').button();
		var $menuTag = $("#menuTag").find('a');
		$menuTag.button();
		$menuTag.click(function (event) {
			event.preventDefault();
			that.filtreForTag($(this).attr('id'));
		});

		$("#documentationNotation").dialog({
			title: 'Documentation des notation',
			width: '90%',
			position: 'top',
			dialogClass: 'documentation'
		});

	}
};


var docSourceJavaScript = {
	init: function (s_source) {
		var regexExecuted;
		var optionRegex = {
			param:	/@param(.*\n*\w*)/g,
			returnValue: /@return(.*\n*\w*)/g
		};
		regexExecuted = this.execueRegex(s_source, optionRegex);
	},
	
	execueRegex: function (s_source, options) {
		var regexExecuted = {};
		for (var key in options) {
			var matches = s_source.match(options[key]); 
			for (var match in matches) { 
				
			} 
			regexExecuted[key] = matches; 
		}
		return regexExecuted;
	}
};