var startPage = {
	$boutonBack: null,
	$boutonDemo: null,
	$boutonDoc: null,
	$boutonQuirks: null,
	$boutonClone: null,
	$boutonJsHint: null,
	$boutonQUnit: null,
	$body: null,
	n_bodyWidth: null,
	$divIntro: null,
	o_iframes: {},

	init: function () {
		this.$boutonHome = $('#home');
		this.$boutonDemo = $('#demo');
		this.$boutonDoc = $('#doc');
		this.$boutonQuirks = $('#demoQuirks');
		this.$boutonClone = $('#testClone');
		this.$boutonJsHint = $('#jsHint');
		this.$boutonQUnit = $('#qunit');
		this.$body = $('body');
		this.$divIntro = $('#intro');
		
		var $boutons = $('.bouton').button();
		this.$boutonHome.button({
			icons: {
				primary:'ui-icon-home'
			},
            text: false
		});
		
		var that = this;
		
		$(document).bind("reload",function () {
			console.log('reload',this);
		});
		$(document).bind("load",function () {
			console.log('load',this);
		});
		$boutons.click(function (e) {
			var $this = $(this);
			var $div = that.createAndReturnDivWithIframe($this);
			e.preventDefault();
			that.slide($div);
			e.preventDefault();
		});
		this.$boutonHome.click(function (e) {
			that.reverseSlide();
		});
		this.n_bodyWidth = this.$body.outerWidth();
	},

	slide: function ($div) {
		var $iframe = this.o_iframes['iframe'+$div.attr("id").replace('div','')];
		this.$divIntro.effect('drop', 'slow', function () {
			$div.append($iframe);
			//$div.get(0).appendChild($iframe.get(0))
			$iframe.show();
			$div.effect('slide', {'direction': 'right'}, 'slow');
			
		});
	},

	reverseSlide: function () {
		var that = this;
		var $divToHide = $('iFrame:visible').parent('div');
		var $iframe = $divToHide.find("iframe").detach();
		this.o_iframes[$iframe.attr('id')] = $iframe;
		$divToHide.effect('drop', {'direction': 'right'}, 'slow', function () {
			//that.$body.append($iframe)
			$iframe.hide();
			that.$divIntro.effect('slide', {'direction': 'left'}, 'slow');
		});
	},

	createAndReturnDivWithIframe: function ($element) {
		var s_idDivFrame = 'div_' + $element.attr('id');
		var $divIframe = $('#' + s_idDivFrame);
	
		
		if ($divIframe.length === 0) {
	
			var $iframe = $("<iframe>", {
				name: "iframe_" + $element.attr('id'),
				id: "iframe_" + $element.attr('id'),
				src: $element.attr('href'),
				css: {
					'width': "100%"
//					'width': (this.$body.width() - 20) + "px",
//					'height': ($(document).height() - 120) + 'px'
				},
				scrolling: "yes"
			});
	
			$divIframe = $('<div>', {
				'id': s_idDivFrame,
				'class': 'slidable content withBorder',
				css: {
					'width': (this.$body.outerWidth()) + "px",
					'height': ($(document).height() - 100) + 'px'
				}

			}).hide();
			
			var $bar = $('<div>', {
				'class': "navPage"
			});
			$butonReSize = $("<span>");
			$butonReSize.button({
				icons: {
					primary:'ui-icon-newwin'
				},
	            text: false
			});
			$bar.append($butonReSize);
			$divIframe.append($bar);
			this.reSize($butonReSize);
			//this.$body.append($iframe);
			$divIframe.appendTo(this.$body);
			//$iframe = $iframe.detach();
			this.o_iframes[$iframe.attr('id')] = $iframe;
		}
		return $divIframe;
	},
	
	reSize: function ($elementButton) {	
		var that = this;
		$elementButton.toggle(function () {
			var $divPage = $elementButton.closest(".slidable");
			$divPage.css({position: "absolute"});
			$divPage.animate({
				  top:0,
				  left:0,
				  'width': "100%",
				  'height':"100%"
				 });
		},function () {
			var $divPage = $elementButton.closest(".slidable");
			$divPage.css({position: "static"});
			$divPage.animate({ 
						  'width': (that.n_bodyWidth) + "px",
						  'height':($(document).height() - 100) + 'px'
						 });
		});
	}
};

$(document).ready(function () {
	startPage.init();
});