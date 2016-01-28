/**
 * @author DMA
 */

$(function () {
	defaultTableAjax.init({
		s_actionAjax: globazGlobal.ACTION_AJAX,
		
		getParametresForFind: function () {
			var map = {
				"searchModel.forIdHome": globazGlobal.IdHome,
				"searchModel.forDateValable": $("#forDateValable").val() ,
				"searchModel.forIdTypeChambre": globazGlobal.IdTypeChambre
				
			};
			return map;
		},
		
		getParametres : function () {
			return {"prixChambre.simplePrixChambre.idHome": globazGlobal.currentIdHome};
		},
		
		init: function () {	
			this.capage(5, [10, 20, 30, 50, 100]);
			this.addSearch();
		}
	});
});
