var decisionCapSearch;

function manageDisplay(){
	
	$(".hiddenIfNoAffilie").hide();
	
	if($("#idAffilie").val()){
		$(".hiddenIfNoAffilie").show();
	}
	
	$(".alwaysHidden").hide();
}

$(function () {
	defaultTableAjax.init({
		s_actionAjax: "auriga.decisioncap.decisionCapSearchAjax",
		
		getParametresForFind: function () {
			
			var s_afficherDecisionsSupprimmesValue = '';
			
			if ($("#afficherDecisionsSupprimmes").is(':checked')) {
				s_afficherDecisionsSupprimmesValue = $("#afficherDecisionsSupprimmes").val() ;
			}
			
			var map = { "afficherDecisionsSupprimmes":s_afficherDecisionsSupprimmesValue,
						"searchModel.forIdAffiliation":$("#idAffilie").val()
						};
			return map;
		},
		
		init: function () {	
			decisionCapSearch = this;
			this.list();
			//this.list(5, [5,10,20,50]);
			
			$("#afficherDecisionsSupprimmes").click(function(){
				decisionCapSearch.ajaxFind();
			});
		}
	});
	
	manageDisplay();
});



