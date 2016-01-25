var KingTour = {};

var BUBBLE_TYPE_HOVER = "hover",
	BUBBLE_TYPE_CLICK = "click",
	POSITION_TOP = "top",
	POSITION_LEFT = "left",
	POSITION_RIGHT = "right",
	POSITION_BOTTOM = "bottom";

globazNotation.bubble = {

	author: "DMA,PBA",

	forTagHtml: "span,input,td,tr,img",
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Permet d'afficher une bulle d'aide sur différents élement HTML<br/><br/>Le contenu de la bulle peut être défini par du texte directement dans la déclaration de la notation (champ \"text\") ou par un identifiant d'un élement HTML déjà construit dans la page (champ \"id\").",

	descriptionOptions: {
		text: {
			desc: "Le texte contenu de la bulle d'aide.<br/>Ignoré si \"id\" est défini.",
			param: "¦string¦"
		},
		id: {
			desc: "l'ID d'un élement HTML qui sera contenu dans la bulle d'aide",
			param: "un ID"
		},
		title: {
			desc: "Titre de la bulle, optionnel",
			param: "¦string¦"
		},
		type: {
			desc: "Le type de bulle d'aide",
			param: "click: ouvre la bulle lors d'un click sur le bouton d'aide, <br/>hover(par défaut): ouvre la bulle lorsque la souris passe au dessus de l'élement"
		},
		wantMarker: {
			desc: "Indique si l'on veut l'image avec le point d'interrogation ou si on ne veut pas d'image",
			param: "boolean(defaut true)"
		},
		position: {
			desc: "La position de la bulle par rapport à l'élement",
			param: "top(par défaut), left, right, bottom ou le couplet de 3 lettres correspondant à la position voulue (voir la fonction _drawArrow)"
		}
	},

	options: {
		text: null,
		id: null,
		title: "",
		type: BUBBLE_TYPE_HOVER,
		position: POSITION_TOP,
		wantMarker: true,
		ajax:false,
		padding: 15
	},

	vars: {
		n_padding: 15,
		pos: null,
		$buble: null,
		$questionMarker: null,
		coordsTarget: {}
	},

	init: function () {
		if(!this.options.ajax){
			var s_template = this.createTemplate();
			
			var s_innerHTML;
			if (this.options.text !== '') {
				s_innerHTML = this.options.text;
			} else {
				s_innerHTML = 'missing text';
			}
	
			if (this.options.id) {
				this.vars.$buble = $(s_template.replace('{innerHTML}', '<div id="globazBubbleWrappingDiv"></div>'));
				var $divEnglobante = this.vars.$buble.find('#globazBubbleWrappingDiv');
				$divEnglobante.append($('#' + this.options.id));
			} else {
				this.vars.$buble = $(s_template.replace('{innerHTML}', s_innerHTML));
			}
	
			if (this.options.wantMarker) {
				this.vars.$questionMarker = $('<span>', {
					'class': 'ui-state-default ui-corner-all globazBubbleQuestionIcon'
				}).append($('<span>', {
					'class': 'ui-icon ui-icon-help'
				}));
				
				this.vars.$questionMarker.appendTo(this.$elementToPutObject);
	
				if (this.options.type !== BUBBLE_TYPE_HOVER) {
					globazNotation.utilsInput.emulateJqueryButton(this.vars.$questionMarker);
				}
			}
	
			$("body").append(this.vars.$buble);
	
			this.vars.$buble.hide();
	
			this.bindEvents();
		}
	},

	createTemplate: function () {
		var b_wantHeader = this.options.title !== '' || this.options.type !== BUBBLE_TYPE_HOVER;
		
		var s_template =	'<div class="bubbleDialog" {styleBubbleDialog}>' +
								'{divBubbleDialogHead}' +
								'<div class="bubbleDialogBody">' +
									'{innerHTML}' +
								'<\/div>' +
								'<div class="arrow">' +
									'<div class="arrow-inner"><\/div>' +
								'<\/div>' +
							'<\/div>';

		var s_styleBubbleDialog = '';
		if (!$.support.boxModel) {
			s_styleBubbleDialog = 'style="width:350px;"';
		}
		s_template = s_template.replace('{styleBubbleDialog}', s_styleBubbleDialog);

		var s_divHead = '';
		if (b_wantHeader) {
			s_divHead += '<div class="bubbleDialogHead">';
		}
		if (this.options.title !== '') {
			s_divHead += this.options.title;
		} else if (b_wantHeader) {
			s_divHead += '&nbsp;';
		}
		if (this.options.type !== BUBBLE_TYPE_HOVER) {
			s_divHead += '<span class="bubbleClose" href="#" {styleBubbleClose}>x<\/span>';
			
			var s_styleBubbleClose = '';
			if (!$.support.boxModel) {
				s_styleBubbleClose = 'style="position:relative;top:-21px;left:-5px;"';
			}
			s_divHead = s_divHead.replace('{styleBubbleClose}', s_styleBubbleClose);
		}
		if (b_wantHeader) {
			s_divHead += '<\/div>';
		}
		s_template = s_template.replace('{divBubbleDialogHead}', s_divHead);
		return s_template;
	},

	open: function () {
		if (this.vars.$buble.not(':visible')) {
			
			$('.bubbleDialog').hide();
			
			if (this.options.wantMarker) {
				this.coordsTarget = KingTour.Expose.draw(this.vars.$questionMarker.get(0), this.options.padding);
				this.attachToExpose(this.getPosition(), this.vars.$buble, this.vars.$questionMarker.get(0));
			} else {
				this.coordsTarget = KingTour.Expose.draw(this.$elementToPutObject.get(0), this.options.padding);
				this.attachToExpose(this.getPosition(), this.vars.$buble, this.$elementToPutObject.get(0));
			}

			if (this.options.highLight) {
				KingTour.Expose.highLight();
			}

			this.vars.$buble.fadeIn();
		}
	},

	getPosition: function () {
		switch (this.options.position) {
		case POSITION_TOP:
			return "tcr";
		case POSITION_LEFT:
			return "ltm";
		case POSITION_RIGHT:
			return "rtm";
		case POSITION_BOTTOM:
			return "bcr";
		}
		return this.options.position;
	},

	close: function () {
		if (this.vars.$buble.is(':visible')) {
			this.vars.$buble.fadeOut(400);
			if (this.options.highLight) {
				$('.kCover').remove();
			}
		}
	},

	bindEvents: function () {
		var that = this;
		
		if (this.options.type === BUBBLE_TYPE_CLICK) {
			var $closeCross = this.vars.$buble.find('.bubbleClose');

			if (this.options.wantMarker) {
				this.vars.$questionMarker.click(function () {
					that.open();
				});
			} else {
				this.$elementToPutObject.click(function () {
					that.open();
				});
			}
			this.vars.$buble.click(function (event) {
				if (event.target !== $closeCross.get(0)) {
					return false;
				}
			});
			$closeCross.click(function () {
				that.close();
			});
			$(document).click(function (event) {
				if (event.target !== that.$elementToPutObject.get(0) && !$.contains(that.$elementToPutObject.get(0), event.target)) {
					that.close();
				}
			});
		} else {
			this.$elementToPutObject.hover(function () {
				that.open();
			}, function () {
				that.close();
			});
		}
	},

	/**
	 * calculate coordinates for the help elements
	 * */
	calcCoords: function () {
		// use the first element jQuery finds
		var o_offset =  this.$elementToPutObject.offset(),
			coords = {};
		coords.t = o_offset.top - this.vars._padding;
		coords.l = o_offset.left - this.vars._padding;
		coords.w = this.$elementToPutObject.width() + this.vars._padding * 2;
		coords.h = this.$elementToPutObject.height() + this.vars._padding * 2;
		coords.b = o_offset.top  + this.$elementToPutObject.height()  + this.vars._padding;
		coords.r = o_offset.left + this.$elementToPutObject.width()   + this.vars._padding;
		return coords;
	},

	attachToExpose: function (pos, $element) {
		this.vars._pos = pos;
		var dWidth  = this.vars.$buble.width(),
			dHeight = this.vars.$buble.height(),
			coords  = this.coordsTarget,
			dialogPos = this.vars._pos.charAt(0),
			arrowPos = this.vars._pos.charAt(1),
			dialogAlign = this.vars._pos.charAt(2),
			dialogTop  = 0,
			dialogLeft = 0,
			// the arrow size is actually defined in css, but needed in here too for math
			arrowSize = 16,
			//If the dialog has round borders the arrow cannot sit on the edges of the
			//dialog and must be moved by the border size, also definied in css
			borderRadius = 10,
			center = 0;
		// Positioning of the dialog, around the exposed area
		switch (dialogPos) {
		case 't':
			//set css top taking dialog height and arrow height into account (move up by hight+arrow Size)
			dialogTop  = coords.t - arrowSize - dHeight;
			break;
		case 'b':
			dialogTop  = coords.b + arrowSize;
			break;
		case 'l':
			dialogLeft = coords.l - arrowSize - dWidth;
			break;
		case 'r':
			dialogLeft = coords.r + arrowSize;
			break;
		}
		// switch through Arrow position(second el) and with it the alignment of the dialog
		switch (arrowPos) {
		case 't': // top rt lt
			dialogTop  = coords.t - borderRadius; //rtb ltb
			if (dialogAlign === 't') {
				//ltt, rtt
				dialogTop = coords.t - dHeight + arrowSize * 2 + borderRadius;
			}
			if (dialogAlign === 'm') { //ltm rtm
				dialogTop  = coords.t - dHeight / 2 + arrowSize;
			}
			break;
		case 'm': // middle rm lm
			center = coords.t + coords.h / 2;
			dialogTop = center - dHeight / 2;
			if (dialogAlign === 't') {
				//rmt, lmt
				dialogTop  = center - dHeight + arrowSize + borderRadius;
			}
			if (dialogAlign === 'b') { //rmb, lmb
				dialogTop  = center - arrowSize - borderRadius;
			}
			break;
		case 'b': //rb lb bottom
			dialogTop  = coords.b - dHeight + borderRadius; //rbt lbt
			if (dialogAlign === 'b') { //rbb, lbb,
				dialogTop = coords.b - arrowSize * 2 - borderRadius;
			}
			if (dialogAlign === 'm') { //rbm, lbm
				dialogTop  = coords.b - dHeight / 2 - arrowSize;
			}
			break;
		case 'l': // left tlr tlc tll blr bll blc
			dialogLeft = coords.l - borderRadius;// tlr, blr
			if (dialogAlign === 'l') { //tll, bll
				dialogLeft = coords.l - dWidth + arrowSize * 2 + borderRadius;
			}
			if (dialogAlign === 'c') { // tlc, blc
				dialogLeft = coords.l - dWidth / 2 + arrowSize;
			}
			break;
		case 'c': // center tcl, tcr, bcl , bcr
			center = coords.l + coords.w / 2; //left val of expose - half exposed width
			dialogLeft = center - dWidth / 2; //tcc, bcc
			if (dialogAlign === 'l') { //tcl bcl
				dialogLeft = center - dWidth + arrowSize + borderRadius;
			}
			if (dialogAlign === 'r') { //tcr bcr
				dialogLeft = center - arrowSize - borderRadius;
				if (dialogLeft > $(document).width() / 2) {
					// si la position de la bulle est dans la moitié droite de l'écran, on passe en mode **l afin d'éviter de sortir de l'écran
					dialogLeft = center - dWidth + arrowSize + borderRadius;
					this.vars._pos = this.vars._pos.substring(0, 2) + 'l';
				}
			}
			dialogTop = dialogTop + 13;
			if (!$.support.boxModel) {
				dialogLeft = dialogLeft - 4;
			}
			break;
		case 'r': //right trl trc trr brl brr brc
			dialogLeft = coords.r - dWidth + borderRadius; // trl
			if (dialogAlign === 'r') { //trr brr
				dialogLeft = coords.r - arrowSize * 2 - borderRadius;
			}
			if (dialogAlign === 'c') { //trc brc
				dialogLeft = coords.r - dWidth / 2 - arrowSize;
			}
			break;
		}
		//set css for the arrow
		this._drawArrow(arrowSize, borderRadius, dHeight, dWidth, this.vars._pos);
		//set top/left positions for the dialog
		this.vars.$buble.css({
			'position': 'absolute',
			'top': dialogTop + 'px',
			'left': dialogLeft + 'px',
			'right': 'auto',
			'bottom': 'auto'
		});
	},

	/**
	 * @param arrowSize Integer size of the arrow, actually set in css
	 * @param borderRadius Integer the dialog's order radius,
	 * If the dialog has round borders the arrow cannot sit on the edges of the
	 * dialog and is moved by the border radius
	 * @param dHeight Integer dialog height
	 * @param dWidth Integer dialog width
	 * @param pos String position tripple => ltm
	 */
	_drawArrow: function (arrowSize, borderRadius, dHeight, dWidth, pos) {
		//arrow div, also responsible for outer border
		var arrow = jQuery('.arrow'),
			arrow_inner = jQuery('.arrow-inner'), //inner arrow, holds arrow bg-color
			base_css = {}, //hash applied to .css inline styles for arrow/inner divs
			dialogPos = pos.charAt(0), // fist char in position def => t b l r
			dialogAlign = pos.charAt(2); // Third char in position def => m b t / l c r
		//kick inline styles & classes, re-add base classes
		arrow.removeAttr('style').removeClass().addClass('arrow');
		arrow_inner.removeAttr('style').removeClass().addClass('arrow-inner');

		switch (dialogPos) {
			//dialog on top / bottom
		case 't':
		case 'b':
			if (dialogPos === 't') {
				arrow.addClass('arrow-down');
				arrow_inner.addClass('arrow-inner-down');
			} else { // bottom
				arrow.addClass('arrow-up');
				arrow_inner.addClass('arrow-inner-up');
			}
			// alignment under the bubble border-radius + right/center/right
			switch (dialogAlign) {
			case 'l': // left tl t
				base_css.right = arrowSize - borderRadius + 'px';
				break;
			case 'c': // center
				base_css.left = dWidth / 2 - arrowSize + 'px';
				break;
			case 'r': // right
				base_css.left = borderRadius + 'px';
				break;
			}
			break;
		// right, left aligned dialog
		case 'r':
		case 'l':
			if (dialogPos === 'r') {
				arrow.addClass('arrow-lft'); //arrow pointing left <
				arrow_inner.addClass('arrow-inner-lft');
			} else { // left >
				arrow.addClass('arrow-rgt');
				arrow_inner.addClass('arrow-inner-rgt');
			}
			// alignment above the bubble border-radius + right/center/right
			switch (dialogAlign) {
			case 't': // top align dialog
				base_css.bottom =  borderRadius + 'px';
				break;
			case 'm': // middle
				base_css.top = (dHeight / 2 - arrowSize) + 'px';
				break;
			case 'b': // bottom
				base_css.top = borderRadius + 'px';
				break;
			}
			break;
		}
		arrow.css(base_css);
	}
};

KingTour.Expose = (function () {
	var _element  = null,  // jquery selector MUST return array of dom elements from which the first el is taken
		_padding  = 0,
		_coords   = {};

	/**
	 * calculate coordinates for the help elements
	 */
	function _calcCoords() {
		var $di = jQuery(_element).eq(0),
			o_offset = $di.offset(),
			coords = {};
		coords.t = o_offset.top - _padding;
		coords.l = o_offset.left - _padding;

		coords.w = $di.width()   + _padding * 2;
		coords.h = $di.height()  + _padding * 2;
		coords.b = o_offset.top  + $di.height()  + _padding;
		coords.r = o_offset.left + $di.width()   + _padding;

		return coords;
	}

	function _createCover(s_cover) {
		var $cover = jQuery('#' + s_cover);
		if (0 === $cover.length) {
			return jQuery('<div id="' + s_cover + '" class="kCover" style="position:absolute"></div>').appendTo("body");
		} else {
			return $cover;
		}
	}

	function _drawTop() {
		var cover = _createCover("kCoverTop");
		cover.css({
			'position' : 'absolute',
			'top' : '0px',
			'height' : Math.max(0, _coords.t) + 'px'
		});
	}

	function _drawBottom() {
		var cover = _createCover("kCoverBottom"),
			top = Math.max(0, _coords.b),
			height = document.documentElement.scrollHeight - top;
		cover.css({
			'position' : 'absolute',
			'top' : top + 'px',
			'height' : height + 'px'
		});
	}

	function _drawLeft() {
		var cover = _createCover("kCoverLeft");
		cover.css({
			'position' : 'absolute',
			'top' : _coords.t + 'px',
			'width' : Math.max(0, _coords.l) + 'px',
			'height' : _coords.h + 'px'
		});
	}

	function _drawRight() {
		var cover = _createCover("kCoverRight");
		cover.css({
			'position' : 'absolute',
			'top' : _coords.t + 'px',
			'left' : _coords.r + 'px',
			'height' : _coords.h + 'px'
		});
	}

	function _drawHighlight() {
		var cover;
		
		if (0 === jQuery('#kExposeCover').length) {
			cover = jQuery('<div id="kExposeCover"></div>').appendTo("body");
		} else {
			cover = jQuery('#kExposeCover');
		}

		cover.css({
			'position' : 'absolute',
			'top' : _coords.t + 'px',
			'left' : _coords.l + 'px',
			'height' : _coords.h + 'px',
			'width' : _coords.w + 'px'
		});
	}

	function _drawCover() {
		if (KingTour.mouseNav) { // add previous navigation if right mouse is clicked
			document.body.oncontextmenu = function () {
				KingTour.Dialog.prev();
				return false;
			};
		}
		_drawTop();
		_drawBottom();
		_drawLeft();
		_drawRight();
	}

	// KingTour.Expose public class functions
	return {
		draw: function (element, padding) {
			_element  = element;
			_padding  = padding;
			_coords   = _calcCoords();
			return _coords;
		},

		highLight: function () {
			_drawCover();
		},

		refresh: function () {
			_coords = _calcCoords();
		},

		getCoords: function () {
			return _coords;
		}
	};
})();