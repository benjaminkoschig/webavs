var entitySearch;
$(function() {
	$(".areaSearch :input").focusout(function(){
		entitySearch.ajaxFind();
	});
	var t_entity = defaultTableAjaxList.init({
		s_actionAjax : globazGlobal.ACTION_AJAX,
		userActionDetail : "module?userAction=USER_ACTION&selectedId=",
		
		init : function() {
			entitySearch = this;
			this.capage(5, [ 10, 20, 30, 50, 100 ]);
			this.addSearch();
		},
	});
});
