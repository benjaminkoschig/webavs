$(function() {
	var tableAjax = this;
	
	$('.areaSearch :input').change(function () {
		tableAjax.ajaxFind();
	});
	
	
	defaultTableAjaxList.init({
		s_selector: "#",
		s_actionAjax: globazGlobal.ACTION_AJAX,
		s_spy: "currentEntity.cotisationAssociationProfessionnelleSimpleModel",
		userActionDetail : "vulpecula?userAction=vulpecula.registre.detailParamCotiAP.afficher&idCotisationAP=",
		init : function() {
			tableAjax = this;
			this.capage(5, [10, 20, 30, 50, 100]);
			this.addSearch();
		},
		b_changeStack:true,
		afterRetrieve : function(data) {
			this.defaultLoadData(data, '#');
			$("#libelleAssociation").val(data.currentEntity.administrationComplexModel.admin.codeAdministration + " - " + data.currentEntity.administrationComplexModel.tiers.designation1+" "+data.currentEntity.administrationComplexModel.tiers.designation2);
			
		}
	});
});