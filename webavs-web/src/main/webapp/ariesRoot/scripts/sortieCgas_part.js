$(function () {
	defaultTableAjax.init({
		s_actionAjax: "aries.sortiecgas.sortieCgasAjax",
		b_validateNotation: true,
		
		getParametresForFind: function () {
			var map = {};
			$("#searchZone").find(":input").each(function (){
				map[this.id] = this.value;
			});
			return map;
		},
		
		init: function () {	
			this.capage(20, [50, 100]);
			this.addSearch();
		}
	});
	
	$("#complexSortieCgas\\.decisionCgas\\.idDecisionRectifiee").change(function(){
		if($(this).val()!=null && $(this).val().length!=0 && $(this).val()!=0){
			//afficher les champs pour décision réctificative
			$(".rectif").show();
		}else{
			//cacher les champs pour décision réctificative
			$(".rectif").hide();
		}
	});
});