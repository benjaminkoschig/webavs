/**
 * @author DMA

 * 
 */


if(!variableGlobal){
	var variableGlobal = {};
}
if(!variableGlobal.uniqueId){
	variableGlobal.uniqueId = 1;	
}
jQuery.uniqueId = function () {	
	return ++variableGlobal.uniqueId; 
},

/*
 * Permet de sérialiser un objet xml
 */
jQuery.fn.toXML = function () {
	var out = '';
	if (this.length > 0) {
		if (typeof XMLSerializer == 'function' || typeof XMLSerializer == 'object') {
			var xs = new XMLSerializer();
			// XMLSerializer.serializeToString(oDOMNode)
			try {
				this.each(function () {
					out += xs.serializeToString(this);
				});
			} catch (e) {
				// hack pour IE 9
				this.each(function () {
					out += this.xml;
				})
			}
		} else if (this[0].xml !== undefined) {
			this.each(function () {
				out += this.xml;
			})
		} else {
			// TODO: Manually serialize DOM here,
			// for browsers that support neither
			// of two methods above.
		}
	}
	return out;
};

// src: http://www.ryancoughlin.com/2009/01/22/jquery-timeout-function/
jQuery.fn.idle = function (time) {
	var i = $(this);
	i.queue(function () {
		setTimeout(function () {
			i.dequeue();
		}, time);
	});
};

jQuery.fn.executeNotation = function () {
	var t_start = (new Date()).getTime(), t_initJquery = null, that = this, $elements = this.find(notationManager.createNameObjForFilter());
	t_initJquery = (new Date()).getTime() - t_start;

	$elements.each(function (index, element) {
		notationManager.addObjToElement(element, that);
	});

	notationManager.mapEmentsTriggers.push({
		$elementTrigger: that,
		t_timeInitJquery: t_initJquery,
		t_timeMoteur: (new Date()).getTime() - t_start,
		t_timeJsAutre: '0'
	});
};

jQuery.callObjectNotationByJquery = function (s_notation, o_options, $element) {
	if (!o_options) {
		o_options = {}; 
	}
	//$("#firstInteger")
	return notationManager.callObjectToUseOnJS(s_notation, o_options, $element);
};


jQuery.createAllFunctionForNotationOnJquery = function () {
	// On fait ce test pour ie. pour eviter l'affichage de l'erreur si l'on revient sur la page
	if (!globazNotation.globalVariables.isFunctionForNotationOnJquery) {
		for (var s_notation in globazNotation) {
			(function (s_notation) {
				var s_nameNotation = "notation" + globazNotation.utils.firstUpperCase(s_notation);
				if (typeof jQuery.fn[s_nameNotation] !== "undefined") {
					globazNotation.utils.consoleError("Conflit  de nom pour la notation '" + s_nameNotation + "' avec jquery");
					throw "Conflit de nom pour la notation '" + s_nameNotation + "' avec jquery";
				}
				jQuery.fn[s_nameNotation] = function (o_options, o_element) {
					var notation = $.callObjectNotationByJquery(s_notation, o_options, this);
					return notation;
			    };
			})(s_notation);
		}
	}
	globazNotation.globalVariables.isFunctionForNotationOnJquery = true;
};

jQuery.fn.removeOverlay = function (options) {
	$('#'+this.data("overlay_uniqueID")).fadeOut("200").remove();
	this.data("overlay_uniqueID",null);
};

jQuery.fn.overlay = function (options) {
	if (this.length) {
		var n_width = 0, 
			n_height = 0, 
			that = this, 
			id = null,
			elementToAppend, 
			$elementOverlay, 
			m_css;
	
		if (options && options.id) {
			id = options.id;
		} else {
			id= "uidOverlay_"+jQuery.uniqueId();
		}
		this.data("overlay_uniqueID",id);
		
		if (options && options.b_relatif && id == null) {
			$elementOverlay = this.find(".ajaxOverlay");
		} else if (id == null) {
			$elementOverlay = $(".ajaxOverlay");
		} else {
			$elementOverlay = $("#" + id);
		}
	
		if ($elementOverlay && $elementOverlay.length) {
			$elementOverlay.fadeOut("200").remove();
		} 
		
		if (this.length > 1) {
			this.each(function () {
				if (options && options.b_width) {
					n_width += $(this).outerWidth();
				} else {
					n_width = that.outerWidth();
				}
				if (options && options.b_height) {
					n_height += $(this).outerHeight();
				} else {
					n_height = that.outerHeight();
				}
			});
		} else {
			n_width = this.outerWidth();
			n_height = this.outerHeight();
		}
		
		m_css = {
				width: n_width + "px", 
				height: n_height + "px",
				opacity: "0.2"
		};
		
		if (options && options.b_relatif) {
			
			if (this.css("position") !== "relative") {
				this.css("position","relative");
			}
			m_css.left = 0;
			m_css.top = 0;
			elementToAppend = this;
		} else {
			m_css.left =  this.offset().left +"px";
			m_css.top = this.offset().top +"px";
			//style = "width:" + n_width + "px; " + "height:" + n_height + "px; " + "left:" + this.offset().left + "px;" + "top:" + this.offset().top + "px;" + "opacity:0.2;filter:Alpha(Opacity=20)",
			elementToAppend = "body";
		}
		
		var $divOverlay = $("<div/>", {
			css: m_css,
			"class": "ajaxOverlay",
			id: id
		});
		
		$divOverlay.appendTo(elementToAppend);

		if (options && options.zIndex) {
			$divOverlay.css({"z-index": options.zIndex});
		}
		
	}
	return this;
};
/*
 * Vide les champs input d'un formulaire
 * 
 */

jQuery.fn.clearInput = function () {
	if (this.length) {
		switch (this[0].type) {
		case 'password':
			this.val('');
			break;
		case 'select-multiple':
			this.val('');
			break;
		case 'select-one':
			this.val('');
			break;
		case 'text':
			this.val('');
			break;
		case 'textarea':
			this.val('');
			break;
		case 'hidden':
			this.val('');
			break;
		case 'checkbox':
			this.checked = false;
			break;
		case 'radio':
			this.checked = false;
		}
	}
	return this;
};

jQuery.fn.clearInputs = function () {
	this.each(function () {
		$(this).clearInput();
	});
	return this;
};

jQuery.fn.clearInputForm = function () {
	this.find(':input').not(':button, :submit, :reset').each(function () {
		$(this).clearInput();
	});
	return this;
};

/*
 * Retourne le texte que l'utilisateur a séléctioné dans le navigateur;
 */
jQuery.fn.getSelection = function () {
	var txt = '';
	if (window.getSelection) {
		txt = window.getSelection();
	} else if (document.getSelection) {
		txt = document.getSelection();
	} else if (document.selection) {
		txt = document.selection.createRange().text;
	}
	return txt;
};

/**
 * Plugin retournant un objet parmi une selection, et des critères de comparaison EX: $(selecteur).getObjectByValue('max','width'), retourne l'objet ayant la propriètè css width la
 * plus grande Pour l'instant, uniquement les propriétés width, et height, en min et max SCE
 */
jQuery.fn.getObjectByValue = function (method) {

	// propriété css prise en compte
	var t_cssAllowed = {
		height: 'height',
		width: 'width'
	};

	// methodes
	var o_methods = {
		// Fonction recherche max
		max: function (s_cssToCompare) {
			return checkIfCssIsAllowed(this, s_cssToCompare, true);
		},
		min: function (cssToCompare) {
			return checkIfCssIsAllowed(this, s_cssToCompare, false);
		}
	};

	// Applique la comparaison si la propriété css est prise en compte
	// Retourne l'objet passé en param dans le cas contraire
	var checkIfCssIsAllowed = function ($_objects, cssToCompare, isMax) {
		if (t_cssAllowed[cssToCompare]) {
			return returnCompareObject($_objects, isMax, t_cssAllowed[cssToCompare]);
		} else {
			return this;
		}
	};

	// retourne l'objet sortant de la comparaison en fonction des critères définis
	var returnCompareObject = function ($_objects, isMax, comparator) {
		var $_return;
		n_toCompare = 0;
		if (!isMax) {
			n_toCompare = Number.MAX_VALUE;
		}

		$_objects.each(function () {
			var value = parseInt($(this).css(comparator).split("px")[0]);
			if (isMax) {
				if (value > n_toCompare) {
					n_toCompare = value;
					$_return = $(this);
				}
			} else {
				if (value < n_toCompare) {
					n_toCompare = value;
					$_return = $(this);
				}
			}
		});
		return $_return;
	};

	if (o_methods[method]) {

		return o_methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
	} else {
		$.error('Method ' + method + ' does not exist on the plugin getObjectByValue');
	}
};
// Begin jquery.attrNameBegins.js // In case included inline.

// jQuery Plug-in "attrNameBegins"
// Version: 1.0 (2010-01-01)
// Examples and documentation at: http://www.webmanwalking.org/projects/jquery/jquery.attrNameBegins/
// Copyright (c) 2009-2010 by WebManWalking
// Dually licensed under the MIT and GPL licenses:
// http://www.opensource.org/licenses/mit-license.php
// http://www.gnu.org/licenses/gpl.html

// ATTENTION
// DMA Cette fonction A été un changé pour être plus lisible et un test a été ajouté
$.extend($.expr[":"], {
	attrNameBegins: function (pObj, pIdxInObjArray, pProps, pObjArray) {

		var sString = pProps[3].toLowerCase();
		var sStrLen = sString.length;
		if (pObj.attributes) {
			for ( var j = 0; j < pObj.attributes.length; j++) {
				if (pObj.attributes[j].specified) { // TEST en PLUS POUR IE :(
					sAttrName = pObj.attributes[j].nodeName.toLowerCase();
					if ((sAttrName.length > sStrLen) && (sAttrName.substring(0, sStrLen) == sString)) {
						return true;
					}
				}
			}
		}

		return false;
	}
});

/*
 * Encode le html Retour une string content le code html de l'element
 */
jQuery.fn.encHTML = function () {
	var rhtml = "";
	this.each(function () {
		var me = $(this);
		var html = me.html();
		rhtml += html.replace(/\&#9;/g, '').replace(/\&#10;/g, '').replace(/\&#13;/g, '\r\n') // For IE :(
		.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/\u0020\u0020\u0020/g, '').replace(/[\t]/g, '');
	});
	return rhtml;
};

/*
 * $.fn.serializeToJson = function() { var o = {}; var a = this.serializeArray(); var s =""; $.each(a, function() { if (o[this.name]) { if (!o[this.name].push) { o[this.name] =
 * [o[this.name]]; } o[this.name].push(this.value || ''); } else { o[this.name] = this.value || ''; } });
 * 
 * $.each(a, function() { s+=this.name+":"+this.value+","; });
 * 
 * return "{["+s.slice(0,s.length-1)+"]}"; };
 */

/**
 * Retourne les paramétres de l'url
 */
$.extend({
	getUrlVars: function () {
		var vars = [], hash;
		var url = window.location.href;
		var hashes = url.slice(url.indexOf('?') + 1).split('&');
		for ( var i = 0; i < hashes.length; i++) {
			hash = hashes[i].split('=');
			vars.push(hash[0]);
			vars[hash[0]] = unescape(hash[1]);
		}
		return vars;
	},
	getUrlVar: function (name) {
		return $.getUrlVars()[name];
	}
});

/**
 * jQuery Cookie plugin
 * 
 * Copyright (c) 2010 Klaus Hartl (stilbuero.de) Dual licensed under the MIT and GPL licenses: http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html Create a cookie with the given key and value and other optional parameters.
 * 
 * @example $.cookie('the_cookie', 'the_value');
 * @desc Set the value of a cookie.
 * @example $.cookie('the_cookie', 'the_value', { expires: 7, path: '/', domain: 'jquery.com', secure: true });
 * @desc Create a cookie with all available options.
 * @example $.cookie('the_cookie', 'the_value');
 * @desc Create a session cookie.
 * @example $.cookie('the_cookie', null);
 * @desc Delete a cookie by passing null as value. Keep in mind that you have to use the same path and domain used when the cookie was set.
 * 
 * @param String
 *            key The key of the cookie.
 * @param String
 *            value The value of the cookie.
 * @param Object
 *            options An object literal containing key/value pairs to provide optional cookie attributes.
 * @option Number|Date expires Either an integer specifying the expiration date from now on in days or a Date object. If a negative value is specified (e.g. a date in the past),
 *         the cookie will be deleted. If set to null or omitted, the cookie will be a session cookie and will not be retained when the the browser exits.
 * @option String path The value of the path atribute of the cookie (default: path of page that created the cookie).
 * @option String domain The value of the domain attribute of the cookie (default: domain of page that created the cookie).
 * @option Boolean secure If true, the secure attribute of the cookie will be set and the cookie transmission will require a secure protocol (like HTTPS).
 * @type undefined
 * 
 * @name $.cookie
 * @cat Plugins/Cookie
 * @author Klaus Hartl/klaus.hartl@stilbuero.de
 */

/**
 * Get the value of a cookie with the given key.
 * 
 * @example $.cookie('the_cookie');
 * @desc Get the value of a cookie.
 * 
 * @param String
 *            key The key of the cookie.
 * @return The value of the cookie.
 * @type String
 * 
 * @name $.cookie
 * @cat Plugins/Cookie
 * @author Klaus Hartl/klaus.hartl@stilbuero.de
 */
jQuery.cookie = function (key, value, options) {

	// key and at least value given, set cookie...
	if (arguments.length > 1 && String(value) !== "[object Object]") {
		options = jQuery.extend({}, options);

		if (value === null || value === undefined) {
			options.expires = -1;
		}

		if (typeof options.expires === 'number') {
			var days = options.expires, t = options.expires = new Date();
			t.setDate(t.getDate() + days);
		}

		value = String(value);

		return (document.cookie = [encodeURIComponent(key), '=', options.raw ? value : encodeURIComponent(value),
				options.expires ? '; expires=' + options.expires.toUTCString() : '', // use expires attribute, max-age is not supported by IE
				options.path ? '; path=' + options.path : '', options.domain ? '; domain=' + options.domain : '', options.secure ? '; secure' : ''].join(''));
	}

	// key and possibly options given, get cookie...
	options = value || {};
	var result, decode = options.raw ? function (s) {
		return s;
	} : decodeURIComponent;
	return (result = new RegExp('(?:^|; )' + encodeURIComponent(key) + '=([^;]*)').exec(document.cookie)) ? decode(result[1]) : null;
};

jQuery.fn.highlightRowOnClick = function (f_callback) {
	this.click(function(){
		var $this = $(this);
		
		if(!$this.prop('disabled')){
			$this.toggleClass('highlight_selected');
			if($.isFunction(f_callback)){
				f_callback($this);
			}
			$('html').triggerHandler(eventConstant.AJAX_HIGHLIGHTED);
		}
	});
	return this;
};

jQuery.fn.focusNextInputField = function() {
	return this.each(function() {
		var fields = $(this).parents('form:eq(0),body').find(':input,a');
		var index = fields.index( this );
		var done = false;
		while ( index > -1 && ( index + 1 ) < fields.length && !done) {
			index = index + 1;
			if(fields.eq(index).attr('type')!='hidden' && !fields.eq(index).attr('readonly') && !fields.eq(index).attr('disabled') && fields.eq(index).is(':visible') ){
				fields.eq(index).focus().select();
				done=true;
			}
		}
		return false;
	});
};

/**
 * jQuery blink plugin
 * 
 * Permet de faire clignoter un element html.
 * 
 * @example $("selector").blink({delay:200});
 * @desc Fait clignoter l'élément toutes les 200ms;
 * 
 * @option delay: Determine le temps de clignotement
 * @name $.fn.blink
 * @cat Plugins/blink
 * @author dma
 * @return The jquery element
 * @type Jquery
 */
jQuery.fn.blink = function(options) {
	var defaults = { delay:500 },
	    options = $.extend(defaults, options);
	
	return this.each(function() {
        var $obj = $(this), timerid;
        if ($obj.attr("timerid") > 0) return;
        $obj.data("isInitStatVisible",$obj.is(":visible"));
        timerid = setInterval(function(){
            if($obj.is(":visible"))   {
               $obj.hide();
            }
            else {
               $obj.show();
            }
        }, options.delay);
        $obj.attr("timerid", timerid);
	});
};
/**
 * jQuery unblink plugin
 * 
 * Permet d'arrête de faire clignoter une élément
 * 
 * @example $("selector").unblink();
 * @desc Arrête de faire clignoter l'élément
 * 
 * @name $.fn.unblink
 * @return The jquery element
 * @type Jquery
 */
jQuery.fn.unblink = function(options) {
    return this.each(function()  {
        var $obj = $(this);
        if ($obj.attr("timerid") > 0)  {
            clearInterval($obj.attr("timerid"));
            $obj.attr("timerid", 0);
            $obj.data("isInitStatVisible")? $obj.show():$obj.hide();
       }
   });
};

/*	

	jQuery pub/sub plugin by Peter Higgins (dante@dojotoolkit.org)

	Loosely based on Dojo publish/subscribe API, limited in scope. Rewritten blindly.

	Original is (c) Dojo Foundation 2004-2009. Released under either AFL or new BSD, see:
	http://dojofoundation.org/license for more information.

*/	

;(function(d){

	// the topic/subscription hash
	var cache = {};

	d.publish = function(/* String */topic, /* Array? */args){
		// summary: 
		//		Publish some data on a named topic.
		// topic: String
		//		The channel to publish on
		// args: Array?
		//		The data to publish. Each array item is converted into an ordered
		//		arguments on the subscribed functions. 
		//
		// example:
		//		Publish stuff on '/some/topic'. Anything subscribed will be called
		//		with a function signature like: function(a,b,c){ ... }
		//
		//	|		$.publish("/some/topic", ["a","b","c"]);
		d.each(cache[topic], function(){
			this.apply(d, args || []);
		});
	};

	d.subscribe = function(/* String */topic, /* Function */callback){
		// summary:
		//		Register a callback on a named topic.
		// topic: String
		//		The channel to subscribe to
		// callback: Function
		//		The handler event. Anytime something is $.publish'ed on a 
		//		subscribed channel, the callback will be called with the
		//		published array as ordered arguments.
		//
		// returns: Array
		//		A handle which can be used to unsubscribe this particular subscription.
		//	
		// example:
		//	|	$.subscribe("/some/topic", function(a, b, c){ /* handle data */ });
		//
		if(!cache[topic]){
			cache[topic] = [];
		}
		cache[topic].push(callback);
		return [topic, callback]; // Array
	};

	d.unsubscribe = function(/* Array */handle){
		// summary:
		//		Disconnect a subscribed function for a topic.
		// handle: Array
		//		The return value from a $.subscribe call.
		// example:
		//	|	var handle = $.subscribe("/something", function(){});
		//	|	$.unsubscribe(handle);
		
		var t = handle[0];
		cache[t] && d.each(cache[t], function(idx){
			if(this == handle[1]){
				cache[t].splice(idx, 1);
			}
		});
	};

})(jQuery);



/**
 * Permet de créer un nouveau "objet" de type json ??? Cette fonction et ajouter a l'objet Object.
 * 
 * @param {Object}
 *            o
 */
if (typeof Object.create !== 'function') {
	Object.create = function (o) {
		var F = function () {
		};
		F.prototype = o;
		return new F();
	};
}

Function.prototype.method = function (name, func) {
	if (!this.prototype[name]) {
		this.prototype[name] = func;
	}
	return this;
};

/**
 * Ajoute la fonction countChar a l'objet string
 * 
 * @param {s_string}
 *            String
 */
String.method('countChar', function (s_string) {
	var tab = this.split(s_string);
	return tab.length - 1;
});
