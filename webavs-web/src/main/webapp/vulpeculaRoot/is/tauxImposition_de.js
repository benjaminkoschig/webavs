var tauxImpositionSearch;

$(function() {
	$('.areaSearch select').change(function() {
		tauxImpositionSearch.ajaxFind();
	});
	
	defaultTableAjax.init({
		s_actionAjax: globazGlobal.ACTION_AJAX,
		s_spy: "currentEntity",

		init: function () {
			tauxImpositionSearch = this;
			this.capage(5, [10, 20, 30, 50, 100]);
			this.addSearch();
		}
	});
});