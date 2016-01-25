var cotisationSearch;

$(function () {
	$(".btnAjaxValidate").click(function (event) {
		if(!validate()){
			event.stopImmediatePropagation();
		}
	});
	
	$(".areaSearch :input").change(function() {
			cotisationSearch.ajaxFind();
	});
	
	defaultTableAjax.init({
		s_actionAjax: globazGlobal.ACTION_AJAX,
		s_spy: "currentEntity.parametreCotisationAssociationSimpleModel",

		init: function () {
			cotisationSearch = this;
			this.capage(5, [10, 20, 30, 50, 100]);
			this.addSearch();
		},

		clearFields: function () {
			this.defaultClearFields();
			$('.spyFormatted').html('');
		},
		
		afterRetrieve : function (data) {
			this.defaultLoadData(data, '#');
			$('#currentEntity\\.parametreCotisationAssociationSimpleModel\\.idCotisationAssociationProfessionnelle').val(data.currentEntity.parametreCotisationAssociationSimpleModel.idCotisationAssociationProfessionnelle);
			$('#nomCotisation').val(data.currentEntity.cotisationAssociationProfessionnelleSimpleModel.libelle + ' | ' + data.currentEntity.administrationComplexModel.tiers.designation1 + " " + data.currentEntity.administrationComplexModel.tiers.designation1);
		}
	});
});

function validate() {
	return true;
}