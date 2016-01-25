/*
 * Scripts pour les écrans paramètres date année taxation
 * CBU, 03.2011
 */

/**
 * 
 * @return
 */
function ParametreModelPart(container){	
	
	// variables
	var that=this;
	this.ACTION_AJAX=ACTION_AJAX_PARAMETRE_MODEL;
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
			'parametreModelComplex.simpleParametreModel.anneeValiditeDebut':this.detail.find('#anneeValiditeDebut').val()
//			'parametreModelComplex.simpleParametreModel.dateSaisieFin':this.detail.find('#dateSaisieFin').val(),
//			'parametreModelComplex.simpleParametreModel.dateSaisieTaxation':this.detail.find('#dateSaisieTaxation').val()
		};
//		$.extend(o_map,this.getParametresForFind());
		return o_map;
	};	
	
	this.getParametresForFind=function(){
		return {
//			'listeDateSaisieTaxationAjaxListViewBean.simpleDateSaisieTaxationSearch.fromDateSaisieTaxationDebut':$('#s_dateDebut').val(),
//			'listeDateSaisieTaxationAjaxListViewBean.simpleDateSaisieTaxationSearch.untilDateSaisieTaxationFin':$('#s_dateFin').val(),
//			'listeDateSaisieTaxationAjaxListViewBean.simpleDateSaisieTaxationSearch.forDateSaisieTaxation':$('#s_dateTaxation').val()
		};
	};
	
	this.clearFields=function(){
//		 this.detail.find('#dateSaisieDebut, #dateSaisieFin, #dateSaisieTaxation').val('').end()		
	};
	
	this.getParentViewBean=function(){
		//return subsideannee;	
	};
	
	this.setParentViewBean=function(newViewBean){
		//subsideannee=newViewBean;
	} 	
	
	this.formatTableTd=function($elementTable){
		//Mettre un style !!
		//$elementTable.find('td:eq(1), td:eq(2), td:eq(3)').css('text-align','right').end();
	}
	
	
	
	// Formatatage de la table 
	this.formatTable=function(){
		//this.formatTableTd(this.$trInTbody);
	}
	
	// initialization
	this.init(
	    function(){			    	
//	    	this.stopEdition();			
			this.addTableEvent();
			this.colorTableRows();
			//Pour afficher par défaut la zone détail
			this.detail.show();
			//On charge le 1er élément			
			this.mainContainer.one(eventConstant.AJAX_UPDATE_COMPLETE,function() {
				that.ajaxLoadEntity(that.$trInTbody.filter(":first").attr("idEntity"));
			});	
//			this.createPagination(10,[10,20,30,50,100]);
			this.ajaxFind();
		}
	);		
}

ParametreModelPart.prototype=AbstractScalableAJAXTableZone;

//jsManager.add(
$(function(){	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new ParametreModelPart($that);
		this.zone=zone;
				
		$that.find('.btnAjaxUpdate').click(function(){
				zone.startEdition();
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
		}).end()
		.find('.btnAjaxRefresh').click(function(){
			zone.ajaxLister();
		}).end()
	});
});