/*
 * Scripts pour les écrans paramètres Prime Moyenne
 * DHI, 05.2011
 */

/**
 * 
 * @return
 */
function PrimeMoyennePart(container){	
	
	// variables
	var that=this;
	this.ACTION_AJAX=ACTION_AJAX_PRIME_MOYENNE;
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
			'simplePrimeMoyenne.anneeSubside':this.detail.find('#anneeSubside').val(),
			'simplePrimeMoyenne.montantPrimeAdulte':this.detail.find('#montantPrimeAdulte').val(),
			'simplePrimeMoyenne.montantPrimeFormation':this.detail.find('#montantPrimeFormation').val(),
			'simplePrimeMoyenne.montantPrimeEnfant':this.detail.find('#montantPrimeEnfant').val()
		};
		$.extend(o_map,this.getParametresForFind());
		return o_map;
	};	
	
	this.getParametresForFind=function(){
		return {			
			'listePrimeMoyenneAjaxListViewBean.simplePrimeMoyenneSearch.forAnneeSubside':$('#s_annee').val()
		};
	};
	
	this.clearFields=function(){
		 this.detail.find('#anneeSubside, #montantPrimeAdulte, #montantPrimeFormation, #montantPrimeEnfant').val('').end();		
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

PrimeMoyennePart.prototype=AbstractScalableAJAXTableZone;

//jsManager.add(
$(function(){	
//	$("#s_anneeSubside").val(new Date().getFullYear());
	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new PrimeMoyennePart($that);
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
		.find('#s_annee').keypress(function(e){			
			if(e.which === 13){
				zone.ajaxFind();
			}
		}).end()
		.find('.bt_search').click(function(){			
			zone.ajaxFind();
		}).end()
		.find('.bt_clear').click(function(){			
			$('#s_annee').val('').end();
			oldContent = "";
			zone.ajaxFind();
		}).end()
	});
});