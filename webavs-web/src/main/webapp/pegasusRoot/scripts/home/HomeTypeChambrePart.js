/**
 * @author DMA
 */

//fonction d'initialisation de la page lorsque JQuery est prêt
$(function () {
	defaultTableAjax.init({
		s_actionAjax: globazGlobal.ACTION_AJAX_HOME_TYPE_CHAMBRE,
		
		getParametresForFind: function () {
			return {"searchModel.forIdHome": globazGlobal.currentIdHome};
		},
		
		getParametres : function () {
			return {"typeChambre.simpleTypeChambre.idHome": globazGlobal.currentIdHome};
		},
		
		afterRetrieve: function (data) {
			$("#typeChambre\\.simpleTypeChambre\\.idTiersParticularite").val("");
			this.defaultLoadData(data, "#", false);
			$("#idTierParticulier").val(data.typeChambre.personneEtendue.tiers.designation1 + " " + data.typeChambre.personneEtendue.tiers.designation2);
		}
	});
});