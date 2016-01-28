/**
 * @author DMA
 */

notationManager.b_stop = true;
// notationManager.showErrors=false;

$(function () {
	jQuery.fn.liveUpdate = function (list) {
		var rows = null;
		var cache;
		list = jQuery(list);
		
		function filter() {
			var term = jQuery.trim(jQuery(this).val().toLowerCase()), scores = [];

			if (!term) {
				rows.show().addClass('keynav withoutfocus');
			} else {
				rows.hide().removeClass('keynav withfocus withoutfocus');

				cache.each(function (i) {
					if (this.indexOf(term) > -1) {
						jQuery(rows[i]).show().addClass('keynav withoutfocus');
					}
				});
			}
		}

		if (list.length) {
			rows = list.children('li');
			cache = rows.map(function () {
				return jQuery(this).text().toLowerCase();
			});

			this.keyup(filter).keyup();
		}

		return this;

	};

	notationDocumentation.createDocumentation();

	// jsManager.addBefore(function(){notationDocumentation.createDocumentation()},"Create the documentation");
	// jsManager.addBefore(function(){SyntaxHighlighter.all()},"SyntaxHighlighter.all()");
	// notationManager.start();
	// Obliger d'utiliser un setTimeout car la fonction SyntaxHighlighter.all() est executer de manière asyncrone
	setTimeout(function () {
		notationManager.start();
	}, 800);
});