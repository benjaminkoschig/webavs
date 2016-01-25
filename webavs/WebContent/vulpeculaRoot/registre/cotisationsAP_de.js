$(function() {
	var tableAjax = this;
	
	$('#currentEntity\\.cotisationAssociationProfessionnelleSimpleModel\\.id').change(function (event){
			$('#redirectionParam').show();			
	});
	
	$( "#redirectionParam" ).click(function( event ) {
		  event.preventDefault();		  
		  window.location.href='vulpecula?userAction=vulpecula.registre.parametresCotisationsAssociations.afficher&forLibelle='+$('#currentEntity\\.cotisationAssociationProfessionnelleSimpleModel\\.libelle').val();
	});
	
	$('.areaSearch :input').change(function () {
		tableAjax.ajaxFind();
	});
	
	defaultTableAjax.init({
		s_actionAjax: globazGlobal.ACTION_AJAX,
		s_spy: "currentEntity.cotisationAssociationProfessionnelleSimpleModel",
		init : function() {
			tableAjax = this;
			this.capage(5, [10, 20, 30, 50, 100]);
			this.addSearch();
		},
		afterRetrieve : function(data) {
			this.defaultLoadData(data, '#');
			$("#libelleAssociation").val(data.currentEntity.administrationComplexModel.admin.codeAdministration + " - " + data.currentEntity.administrationComplexModel.tiers.designation1+" "+data.currentEntity.administrationComplexModel.tiers.designation2);
			
		}
	});
});