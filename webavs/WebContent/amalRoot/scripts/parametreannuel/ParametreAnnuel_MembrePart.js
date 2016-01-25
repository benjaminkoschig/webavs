/*
 * Scripts pour les écrans paramètres subsideAnnée
 * CBU, 03.2011
 */

/**
 * 
 * @return
 */
function ParametreAnnuelPart(container){	
	
	// variables
	var that=this;
	this.ACTION_AJAX=ACTION_AJAX_PARAMETRE_ANNUEL;
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
			'simpleParametreAnnuel.codeTypeParametre':this.detail.find('#codeTypeParametre').val(),
			'simpleParametreAnnuel.anneeParametre':this.detail.find('#anneeParametre').val(),
			'simpleParametreAnnuel.valeurParametre':this.detail.find('#valeurParametre').val(),
			'simpleParametreAnnuel.valeurParametreString':this.detail.find('#valeurParametreString').val()
		};
		$.extend(o_map,this.getParametresForFind());
		return o_map;
	};	
	
	this.getParametresForFind=function(){
		return {
			'listeParametreAnnuelAjaxListViewBean.simpleParametreAnnuelSearch.forAnneeParametre':$('#s_annee').val(),
			'listeParametreAnnuelAjaxListViewBean.simpleParametreAnnuelSearch.forCodeTypeParametre':$('#s_type').val()
		};
	};
	
	this.clearFields=function(){
		 this.detail.find('#codeTypeParametre, #anneeParametre, #valeurParametre, #valeurParametreString').val('').end();		
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

ParametreAnnuelPart.prototype=AbstractScalableAJAXTableZone;

//jsManager.add(
$(function(){	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new ParametreAnnuelPart($that);
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
		.find('#s_annee').keyup(function(){
			if ($(this).val().length==4 && oldContent != $(this).val()) {
				zone.ajaxFind();
				oldContent = $("#s_annee").val();	
			}			
		}).end()
		.find('#s_type').change(function(){			
			zone.ajaxFind();
		}).end()		
		.find('#s_type, #s_annee').keypress(function(e){			
			if(e.which === 13){
				zone.ajaxFind();
			}
		}).end()
		.find('.bt_search').click(function(){			
			zone.ajaxFind();
		}).end()
		.find('.bt_clear').click(function(){			
			$('#s_annee').val('').end();
			$('#s_type').val('').end();
			oldContent = "";
		}).end()
	});
});