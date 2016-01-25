/**
 * @author DMA
 */

//fonction d'initialisation de la page lorsque JQuery est prêt
$(function () {
//	$('#decompte').ajaxSuccess(function(event, xml, option) {
//		if(option.data && option.data.indexOf(globazGlobal.ACTION_AJAX+".modifierAJAX") >=0) {
//			var s_json = 	$(xml.responseXML).find("infoJson").text();
//			var json = $.parseJSON(s_json);
//			ajaxUtils.defaultLoadData(json, $(this), "#");
//		}
//	});
//	
//	$("#isCompense").change(function () {
//		if(this.checked && !this.disabled) {
//			$("#currentEnitiy\\.montantModifie").prop("disabled",false);
//		} else {
//			$("#currentEnitiy\\.montantModifie").prop("disabled",true);
//		}
//	});
	defaultTableAjax.init({
		s_actionAjax: globazGlobal.ACTION_AJAX,
		b_hasButtonNew: false,	
		
		init: function () {			//Permet de définir le comportement de la zone ajax
			this.list(50,[10,20,50]);	// Définit le comportement d'une capage
			this.mainContainer.find('tbody').on('click', 'td', function (event) {
				var $this = $(this), $parent = $this.parent();
				window.location = location.href='pegasus?userAction='+globazGlobal.ACTION_DEBLOCAGE_DETAIL +'.afficher&selectedId='+$parent.attr('idEntity');
//				if (that.isEntitySelectable() &&  event.target == this && !$parent.is(".notSortable") && $parent.attr('idEntity')) {
//					that.displayLoadDetail($parent.attr('idEntity'));
//				}
			});	
		},
		
		getParametresForFind: function () {
			return {
				"searchModel.forIdDemande": globazGlobal.idDemande
			};
		}
//		
//		getParametres : function () {
//			return {"idVersionDroit": globazGlobal.idVersionDroit,
//					"idDroit": globazGlobal.idDroit,
//					"currentEntity.montant" : $("currentEntity\\.montant").val()
//				};
//		},
//		
//		afterRetrieve: function (data) {
//			this.defaultLoadData(data, "#",false);
//		}
	});
	
});