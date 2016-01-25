/*
 * Scripts pour les écrans paramètres subsideAnnée
 * CBU, 03.2011
 */

/**
 * 
 * @return
 */
function SubsideAnneePart(container){	
	
	// variables
	var that=this;
	this.ACTION_AJAX=ACTION_AJAX_SUBSIDE_ANNEE;
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
			'simpleSubsideAnnee.anneeSubside':this.detail.find('#anneeSubside').val(),
			'simpleSubsideAnnee.limiteRevenu':this.detail.find('#limiteRevenu').val(),
			'simpleSubsideAnnee.subsideAdulte':this.detail.find('#subsideAdulte').val(),
			'simpleSubsideAnnee.subsideAdo':this.detail.find('#subsideAdo').val(),
			'simpleSubsideAnnee.subsideEnfant':this.detail.find('#subsideEnfant').val(),
			'simpleSubsideAnnee.subsideSalarie1618':this.detail.find('#subsideSalarie1618').val(),
			'simpleSubsideAnnee.subsideSalarie1925':this.detail.find('#subsideSalarie1925').val()	
		};
		$.extend(o_map,this.getParametresForFind());
		return o_map;
	};	
	
	this.getParametresForFind=function(){

		if ($('#s_limiteRevenu').val().length>0) {
			if ($("#s_operateur").val() == 'forLimiteRevenu') {
				return {			
					'listeSubsideanneeAjaxListViewBean.simpleSubsideAnneeSearch.forAnneeSubside':$('#s_anneeSubside').val(),
					'listeSubsideanneeAjaxListViewBean.simpleSubsideAnneeSearch.forLimiteRevenu':$('#s_limiteRevenu').val()
				};
			} else if ($("#s_operateur").val() == 'forLimiteRevenuLOE') {
				return {			
					'listeSubsideanneeAjaxListViewBean.simpleSubsideAnneeSearch.forAnneeSubside':$('#s_anneeSubside').val(),
					'listeSubsideanneeAjaxListViewBean.simpleSubsideAnneeSearch.forLimiteRevenuLOE':$('#s_limiteRevenu').val()
				};
			} else if ($("#s_operateur").val() == 'forLimiteRevenuGOE') {
				return {			
					'listeSubsideanneeAjaxListViewBean.simpleSubsideAnneeSearch.forAnneeSubside':$('#s_anneeSubside').val(),
					'listeSubsideanneeAjaxListViewBean.simpleSubsideAnneeSearch.forLimiteRevenuGOE':$('#s_limiteRevenu').val()
				};
			}
		} else {
			return {			
				'listeSubsideanneeAjaxListViewBean.simpleSubsideAnneeSearch.forAnneeSubside':$('#s_anneeSubside').val()
			};
		}
	};
	
	this.clearFields=function(){
		 this.detail.find('#anneeSubside, #limiteRevenu, #subsideAdulte, #subsideAdo, #subsideEnfant, #subsideSalarie1618, #subsideSalarie1925').val('').end()		
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

SubsideAnneePart.prototype=AbstractScalableAJAXTableZone;

//jsManager.add(
$(function(){	
//	$("#s_anneeSubside").val(new Date().getFullYear());
	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new SubsideAnneePart($that);
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
		.find('#s_anneeSubside').keyup(function(){
			if ($(this).val().length==4 && oldContent != $(this).val()) {
				zone.ajaxFind();
				oldContent = $("#s_anneeSubside").val();
			}			
		}).end()
		.find('#s_anneeSubside, #s_limiteRevenu, #s_operateur').keypress(function(e){			
			if(e.which === 13){
				zone.ajaxFind();
			}
		}).end()
		.find('.bt_search').click(function(){			
			zone.ajaxFind();
		}).end()
		.find('.bt_clear').click(function(){			
			$('#s_anneeSubside').val('').end();
			$('#s_limiteRevenu').val('').end();
			oldContent = "";
		}).end()
	});
});