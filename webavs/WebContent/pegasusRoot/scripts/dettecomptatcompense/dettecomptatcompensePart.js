/**
 * @author DMA
 */

//fonction d'initialisation de la page lorsque JQuery est prêt
$(function () {
	$('#decompte').ajaxSuccess(function(event, xml, option) {
		if(option.data && option.data.indexOf(globazGlobal.ACTION_AJAX+".modifierAJAX") >=0) {
			var s_json = 	$(xml.responseXML).find("infoJson").text();
			var json = $.parseJSON(s_json);
			ajaxUtils.defaultLoadData(json, $(this), "#");
		}
	});
	
	defaultTableAjax.init({
		s_actionAjax: globazGlobal.ACTION_AJAX,
		b_hasButtonNew: false,	
		
		init: function () {			//Permet de définir le comportement de la zone ajax
			this.capage();	// Définit le comportement d'une capage
		},
		
		getParametresForFind: function () {
			return {"searchModel.forIdVersionDroit": globazGlobal.idVersionDroit,
				"searchModel.forIdDroit": globazGlobal.idDroit
			};
		},
		
		getParametres : function () {
			return {"idVersionDroit": globazGlobal.idVersionDroit,
					"idDroit": globazGlobal.idDroit,
					"currentEntity.montant" : $("currentEntity\\.montant").val()
				};
		},
		
		afterRetrieve: function (data) {
			this.defaultLoadData(data, "#",false);
		},
		
		clearFields: function (){
			$('#currentEntity\\.montant').text("");
			$('#descriptionSection').text("");
			$('#isCompense').prop("checked", false);
			this.detail.clearInputForm();
		}
	});
});