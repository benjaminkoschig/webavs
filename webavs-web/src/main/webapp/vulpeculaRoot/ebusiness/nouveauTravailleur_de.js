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
		
		init : function() {
			travailleurSearch = this;
			this.list(20, [ 10, 20, 30, 50, 100 ]);
			this.addSearch();	
		}
	});
	

		jsManager.addAfter(function (){
			checkClickedLine();
		}, "Initialisation des evenements")

});

function checkClickedLine(){
	$(".areaTable tbody").on("click", "tr", function(){
		var hasModif = $(this).attr("hasModification") === "true";
		if(hasModif){
			window.top.fr_main.window.location="vulpecula?userAction=vulpecula.ebusiness.modificationTravailleurDetail.afficher&selectedId="+$(this).attr('idEntity');
		}else{
			window.top.fr_main.window.location="vulpecula?userAction=vulpecula.ebusiness.nouveauTravailleurDetail.afficher&selectedId="+$(this).attr('idEntity');
		}
	})
}

