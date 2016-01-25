/**
 * @author DMA Ce fichier permet de paramétrer les notations.
 */

jQuery.i18n.properties({
	name: 'notation',
	path: globazNotation.utils.getContextUrl() + '/scripts/jsnotation/resources/',
	mode: 'map',
	language: globazNotation.utils.getLangue().toLowerCase(),
	callback: function () {
		notationManager.b_i18nIsLoaded = true;
	}
});

var inputRadio = {

	$eleRadios: null,

	init: function () {
		var that = this;
		this.$eleRadios = $('input:radio');
		this.bindEvent();
		this.$eleRadios.each(function () {
			var $element = $(this);
			that.wrapLabel($element);
		});
	},

	getLabel: function ($element) {
		var id = $element.attr('id');
		return $("[for=" + id + "]");
	},

	wrapLabel: function ($element) {
		var $label = this.getLabel($element);
		if ($element.is(":checked")) {
			$label.wrap("<strong>");
		}
	},

	unWrapLabel: function ($element) {
		var s_filtre = "[name=" + $element.attr('name') + "]", that = this;

		this.$eleRadios.filter(s_filtre).each(function () {
			var $this = $(this), $label = that.getLabel($this);
			if ($label.parent().is("strong")) {
				$label.unwrap("<strong>");
			}
		});
	},

	addStrong: function ($element) {
		this.unWrapLabel($element);
		this.wrapLabel($element);
	},

	bindEvent: function () {
		var that = this;
		this.$eleRadios.change(function () {
			var $element = $(this);
			that.addStrong($element);
		});
	}
};

$(function () {
	notationManager.n_wantedLogLevel = globazNotation.utils.logging.LEVEL_INFO;
	notationManager.b_debug = false;
    notationManager.init();
	setTimeout(function () {
		notationManager.initFocusColorBehavior();
	}, 100);
});