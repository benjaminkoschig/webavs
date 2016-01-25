var travailleurSearch;
$(function() {
	var changedForced = false;
	$(".areaSearch :input").change(function() {
		if(!changedForced) {
			travailleurSearch.ajaxFind();
		}
		else {
			changedForced = false;
		}
	});
	$(".areaSearch :input").keydown(function(e) {
		changedForced = false;
		if(e.which == 13) {
			changedForced = true;
			travailleurSearch.ajaxFind();
		}
	});
	
	defaultTableAjaxList.init({
		s_actionAjax : globazGlobal.ACTION_AJAX,
		userActionDetail : "vulpecula?userAction=vulpecula.postetravailvueglobale.travailleurvueglobale.afficher&selectedId=",
		
		init : function() {
			travailleurSearch = this;
			this.capage(20, [ 10, 20, 30, 50, 100 ]);
			this.addSearch();
		},
		b_changeStack : true
	});
});
