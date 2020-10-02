/*
 * FHA
 */


/**
 * 
 * @return
 */
function FraisGardePart(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX=ACTION_AJAX_FRAIS_GARDE;
	this.mainContainer=container;
	this.table=this.mainContainer.find(".areaDFDataTable");
	this.detail=this.mainContainer.find(".areaDFDetail");
	this.titleContainer=this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass="areaDFModified";
	this.membreId=null;
	// functions	
	this.afterRetrieve=function($data){
		 this.detail.find('.libelle').val($data.find('libelle').text()).end()
					.find('.montant').val($data.find('montant').text()).end()
					.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
					.find('[name=dateFin]').val($data.find('dateFin').text()).end();
		this.addSpy($data);
	};
	
	this.getParametres=function($data){
		return {
			'fraisGarde.simpleFraisGarde.libelle':this.detail.find('.libelle').val(),
			'fraisGarde.simpleFraisGarde.montant':this.detail.find('.montant').val(),
			'fraisGarde.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'fraisGarde.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};	
	
	this.clearFields=function(){
		 this.detail.find('.libelle,.montant,[name=dateDebut],[name=dateFin]').val('').end();
	};
	
	this.formatTableTd=function($elementTable){
		$elementTable.find('td:eq(1)').css('text-align','left').end();
	}
	
	this.getParentViewBean=function(){
		return droit;	
	};
	
	this.setParentViewBean=function(newViewBean){
		droit=newViewBean;
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

FraisGardePart.prototype=DonneeFinancierePart.prototype;


$(function(){	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new FraisGardePart($(this));
		this.zone=zone;
		zone.membreId=$(this).attr('idMembre');
				
		$(this).find('.btnAjaxUpdate').click(function(){
				zone.startEdition();
				$that.find('.btnAjaxValidateNouvellePeriode').show();
		}).end()
		.find('.btnAjaxCancel').click(function(){
			zone.stopEdition();
		}).end()
		.find('.btnAjaxValidate').click(function(){
			zone.validateEditionV2();
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
			zone.validateEditionV2();
		}).end();	
		
	});

});