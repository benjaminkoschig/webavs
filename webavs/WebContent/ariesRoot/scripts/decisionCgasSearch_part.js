function manageDisplay(){
	
	$(".hiddenIfNoAffilie").hide();
	
	if($("#idAffilie").val()){
		$(".hiddenIfNoAffilie").show();
	}
	
	$(".alwaysHidden").hide();
}

$(function () {
	
	 
		
	 
	
	var the_t_zone = defaultTableAjax.init({
		s_actionAjax: "aries.decisioncgas.decisionCgasSearchAjax",
		
		getParametresForFind: function () {
			var s_afficherDecisionsSupprimmesValue = '';
			
			if ($("#afficherDecisionsSupprimmes").is(':checked')) {
				s_afficherDecisionsSupprimmesValue = $("#afficherDecisionsSupprimmes").val() ;
			}
			
			var map = {"afficherDecisionsSupprimmes":s_afficherDecisionsSupprimmesValue,
					   "searchModel.forIdAffiliation":$("#idAffilie").val()
					   };
			
			return map;
		},
		
		init: function () {	
			var that = this;
			
			this.ajaxFind();
			this.sortTable();
			
			$("#afficherDecisionsSupprimmes").click(function(){
				that.ajaxFind();
			}); 
		}
	});   

	globazGlobal.zoneDecisionCgasAjax = the_t_zone[0];
	
	globazGlobal.zoneDecisionCgasAjax.mainContainer.on(eventConstant.AJAX_FIND_COMPLETE, function () {
		setTimeout(function () {
			globazNotation.globalVariables.count();
		},1);
	});
	
	manageDisplay();
		
});

