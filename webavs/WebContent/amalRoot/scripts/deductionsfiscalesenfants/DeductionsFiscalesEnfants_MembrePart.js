/*
 * Scripts pour les écrans paramètres subsideAnnée
 * CBU, 03.2011
 */

/**
 * 
 * @return
 */
function DeductionsFiscalesEnfantsPart(container){		
	// variables
	var that=this;
	this.ACTION_AJAX=ACTION_AJAX_DEDUCTIONS_FISCALES_ENFANTS;
	this.mainContainer=container;
	this.table=this.mainContainer.find(".areaDataTable");
	this.detail=this.mainContainer.find(".areaDetail");
	this.paramId = null;
	this.b_applyStyle = false;
	
	// functions	
	this.afterRetrieve=function($data){		
		this.defaultLoadData($data, "#");
	};
	
	this.getParametres=function($data){
		var o_map = {
			'simpleDeductionsFiscalesEnfants.anneeTaxation':this.detail.find('#anneeTaxation').val(),
			'simpleDeductionsFiscalesEnfants.nbEnfant':this.detail.find('#nbEnfant').val(),
			'simpleDeductionsFiscalesEnfants.montantDeductionParEnfant':this.detail.find('#montantDeductionParEnfant').val(),
			'simpleDeductionsFiscalesEnfants.montantDeductionTotal':this.detail.find('#montantDeductionTotal').val()
		};
		$.extend(o_map,this.getParametresForFind());
		return o_map;
	};	
	
	this.getParametresForFind=function(){
		return {
			'listeDeductionsFiscalesEnfantsAjaxListViewBean.simpleDeductionsFiscalesEnfantsSearch.forAnneeTaxation':$('#s_anneeTaxation').val()
		};
	};
	
	this.clearFields=function(){
		 this.detail.find('#anneeTaxation, #nbEnfant, #montantDeductionParEnfant, #montantDeductionTotal').val('').end();		
	};
	
	this.getParentViewBean=function(){
	};
	
	this.setParentViewBean=function(newViewBean){
	}; 	
	
	this.formatTableTd=function($elementTable){
	};
	
	
	
	// Formatatage de la table 
	this.formatTable=function(){
	};
	
	// initialization
	this.init(			
	    function(){	    	
	    	this.capage(10,[10,20,30,50,100]);
			this.mainContainer.one(eventConstant.AJAX_UPDATE_COMPLETE,function() {
				setTimeout(function (){
					that.displayLoadDetail(that.$trInTbody.filter(":first").attr("idEntity"));
				},250);
			});		
		}
	);		
}

DeductionsFiscalesEnfantsPart.prototype=AbstractScalableAJAXTableZone;

//jsManager.add(
$(function(){	
	$('.areaMembre').each(function(){
		var $that=$(this);		
		var zone=new DeductionsFiscalesEnfantsPart($that);
		this.zone=zone;
		var oldContent;
		
		$that.find('.btnAjaxUpdate').click(function(){
				zone.startEdition();
				upd();
		}).end()
		.find('.btnAjaxCancel').click(function(){
			zone.stopEdition();
		}).end()
		.find('.btnAjaxValidate').click(function(){
			zone.validateEdition();
		}).end()
		.find('.btnAjaxDelete').click(function(){
				zone.ajaxDeleteEntity(zone.selectedEntityId);
		}).end()
		.find('.btnAjaxAdd').click(function(){
				zone.stopEdition();
				zone.startEdition();
				add();
		}).end()
		.find('#s_anneeTaxation').keyup(function(){
			if ($(this).val().length==4 && oldContent != $(this).val()) {
				zone.ajaxFind();
				oldContent = $("#s_anneeTaxation").val();	
			}			
		}).end()
		.find('#s_type').change(function(){			
			zone.ajaxFind();
		}).end()		
		.find('#s_anneeTaxation').keypress(function(e){			
			if(e.which === 13){
				zone.ajaxFind();
			}
		}).end()
		.find('.bt_search').click(function(){			
			zone.ajaxFind();
		}).end()
		.find('.bt_clear').click(function(){			
			$('#s_anneeTaxation').val('').end();
			oldContent = "";
		}).end()
		.find('#nbEnfant, #montantDeductionParEnfant').blur(function() {
			if ($("#montantDeductionTotal").val().length==0) {
				var nbEnf = $("#nbEnfant").val();
				var mtDedEnf = $("#montantDeductionParEnfant").val();
				var mtTotal = nbEnf.replace(/'/g,'') * mtDedEnf.replace(/'/g,'');			
				$("#montantDeductionTotal").val(mtTotal);
			}
		}).end()
	});
});