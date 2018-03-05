var showWarn = false;
var fieldsForMotifDecesHandler = {
		$csMotif: null,
		$dateSuppressionTr: null,
		$dateDecisionTr: null,
		$montantRestitutionTr: null,
		$dateSuppression: null,
		$dateDecision: null,
		$montantRestitution: null,
		
		init:  function () {
			this.$csMotif = $('#csMotif');
			this.$dateSuppressionTr = $('#dateSuppressionTr');
			this.$dateDecisionTr = $('#dateDecisionTr');
			this.$montantRestitutionTr = $('#montantRestitutionTr');
			this.$dateSuppression = $('#dateSuppression');
			this.$dateDecision = $('#dateDecision');
			this.$montantRestitution = $('#montantRestitution');
			this.bindEvents();
			this.showHide();
			this.calculerMontantRestitutionAjax(this.$dateSuppression.val());
		}, 
		
		bindEvents: function (){
			var that = this;
			this.$csMotif.change(function () {
				that.showHide();
			});
			$('#dateSuppression').change(function () {
				  that.calculerMontantRestitutionAjax(this.value);
			});
		},
			
		showHide: function (){
			if( this.$csMotif.val() == globazGlobal.csMotifDeces ){
				this.$dateSuppressionTr.show();
				this.$dateDecisionTr.show();
				this.$montantRestitutionTr.show();
				showWarn = true;
			}else{
				this.$dateSuppressionTr.hide();
				this.$dateDecisionTr.hide();
				this.$montantRestitutionTr.hide();
				this.$dateSuppression.val("");
				this.$dateDecision.val("");
				this.$montantRestitution.val("");
				showWarn = false;
			}
		},
		
		calculerMontantRestitutionAjax: function (valeurDateSuppression){
			var that = this;
			if(valeurDateSuppression.length){
				var o_options, ajax;
				o_options = {
					serviceClassName: globazGlobal.pcAccordeeServiceClassName,
					serviceMethodName: "calculerMontantRestitution",
					parametres: globazGlobal.idVersionDroit+","+globazGlobal.numVersionDroit+","+valeurDateSuppression,
					criterias: '',
					cstCriterias: '',
					callBack: function (data) {
						that.$montantRestitution.val(data).change();
					},
					errorCallBack: null
				};
				ajax = Object.create($.extend(true, {}, globazNotation.readwidget));
				ajax.options = o_options;
				ajax.read();
			  }
		}
};


$(document).ready(function(){
	  fieldsForMotifDecesHandler.init();

	  $('#btnVal').off("click");
	  $('#btnVal').removeAttr('onclick');
	  $('#btnVal').click(function(e) { 
		  if(showWarn) {
			  showConfirmDialog();
		  } else {
			  validateCorrigerDroit();
		  }
	  });
	  
  });

function validateCorrigerDroit(){
	  if(validate()) {
		  if($('#csMotif').val() == globazGlobal.csMotifDeces && parseFloat($('#montantRestitution').val()) > 0) {
			  showConfirmDialogForCreateLot();
		  } else {
			 action(COMMIT);
		  }
	  }
}