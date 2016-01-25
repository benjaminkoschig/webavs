/*
 * ECO
 */

/**
 * 
 * @return
 */
function AutresDettesProuveesPart(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX=ACTION_AJAX_AUTRES_DETTES_PROUVEES;
	this.mainContainer=container;
	this.table=this.mainContainer.find(".areaDFDataTable");
	this.detail=this.mainContainer.find(".areaDFDetail");
	this.titleContainer=this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass="areaDFModified";
	this.membreId=null;
	
	// functions	
	this.afterRetrieve=function($data){
		 this.detail.find('.montantDette').val($data.find('montantDette').text()).end()					
					.find('.creancier').val($data.find('creancier').text()).end()
					.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
					.find('[name=dateFin]').val($data.find('dateFin').text()).end();
		 this.addSpy($data);
	};
	
	this.getParametres=function($data){				
		
		return {
			'autresDettesProuvees.simpleAutresDettesProuvees.montantDette':this.detail.find('.montantDette').val(),
			'autresDettesProuvees.simpleAutresDettesProuvees.nomPrenomCreancier':this.detail.find('.creancier').val(),
			'autresDettesProuvees.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'autresDettesProuvees.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};	
	
	this.clearFields=function(){
		 this.detail.find('.montantDette,.creancier,[name=dateDebut],[name=dateFin]').val('').end();		
		
	};
	
	this.getParentViewBean=function(){
		return droit;	
	};
	
	this.setParentViewBean=function(newViewBean){
		droit=newViewBean;
	}

	this.formatTableTd=function($elementTable){
		
	}
	
	// initialization
	this.init(
	    function(){	
			this.stopEdition();
			this.onAddTableEvent();
			this.colorTableRows(false);
		}
	);

}

AutresDettesProuveesPart.prototype=DonneeFinancierePart.prototype;


$(function(){	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new AutresDettesProuveesPart($(this));
		this.zone=zone;
		zone.membreId=$(this).attr('idMembre');
				
		$that.find('.btnAjaxUpdate').click(function(){
				zone.startEdition();
				$that.find('.btnAjaxValidateNouvellePeriode').show();
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
		.find('.btnAjaxValidateNouvellePeriode').click(function(){
			zone.doAddPeriode=true;
			zone.validateEdition();
		}).end();
		
	});
	
});