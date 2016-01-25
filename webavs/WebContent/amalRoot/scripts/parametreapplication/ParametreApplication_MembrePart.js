/*
 * Scripts pour les écrans paramètres Parametre Application
 * DHI
 */

/**
 * 
 * @return
 */
function ParametreApplicationPart(container){	
	
	// variables
	var that=this;
	this.ACTION_AJAX=ACTION_AJAX_PARAMETRE_APPLICATION;
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
			'simpleParametreApplication.csTypeParametre':this.detail.find('#csTypeParametre').val(),
			'simpleParametreApplication.csGroupeParametre':this.detail.find('#csGroupeParametre').val(),
			'simpleParametreApplication.valeurParametre':this.detail.find('#valeurParametre').val()
		};
		$.extend(o_map,this.getParametresForFind());
		return o_map;
	};	
	
	this.getParametresForFind=function(){
		return {			
			'listeParametreApplicationAjaxListViewBean.simpleParametreApplicationSearch.forCsTypeParametre':$('#s_type').val(),
			'listeParametreApplicationAjaxListViewBean.simpleParametreApplicationSearch.forCsGroupeParametre':$('#s_groupe').val()
		};
	};
	
	this.clearFields=function(){
		 this.detail.find('#typeParametre, #groupeParametre, #valeurParametre').val('').end();		
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

ParametreApplicationPart.prototype=AbstractScalableAJAXTableZone;

$(function(){	
	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new ParametreApplicationPart($that);
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
		.find('#s_type').change(function(){			
			zone.ajaxFind();
		}).end()		
		.find('#s_type').keypress(function(e){			
			if(e.which === 13){
				zone.ajaxFind();
			}
		}).end()
		.find('#s_groupe').change(function(){			
			zone.ajaxFind();
		}).end()
		.find('#s_groupe').keypress(function(e){			
			if(e.which === 13){
				zone.ajaxFind();
			}
		}).end()
		.find('.bt_search').click(function(){			
			zone.ajaxFind();
		}).end()
		.find('.bt_clear').click(function(){			
			$('#s_type').val('').end();
			$('#s_groupe').val('').end();
			oldContent = "";
			zone.ajaxFind();
		}).end()
	});
});