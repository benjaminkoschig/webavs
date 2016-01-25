/*
 * FHA
 */
/**
 * 
 * @return
 */
function AllocationsFamilialesPart(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX=ACTION_AJAX_ALLOCATIONS_FAMILIALES;
	this.mainContainer=container;
	this.table=this.mainContainer.find(".areaDFDataTable");
	this.detail=this.mainContainer.find(".areaDFDetail");
	this.titleContainer=this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass="areaDFModified";
	this.membreId=null;
	
	// functions	
	this.afterRetrieve=function($data){
		 this.detail.find('.montantMensuel').val($data.find('montantMensuel').text()).end()
					.find('.caisse').val($data.find('idCaisseAF').text()).end()
					.find('.selecteurCaisse').val($data.find('nomCaisse').text()).end()		
					.find('[name=dateEcheance]').val($data.find('dateEcheance').text()).end()									
					.find('.dessaisissementRevenu').attr('checked',$data.find('DR').text()=='true').end()
					.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
					.find('[name=dateFin]').val($data.find('dateFin').text()).end();
		 this.addSpy($data);
	};
	
	this.getParametres=function($data){
		return {
			'allocationsFamiliales.simpleAllocationsFamiliales.montantMensuel':this.detail.find('.montantMensuel').val(),
			'allocationsFamiliales.simpleAllocationsFamiliales.idCaisseAf':this.detail.find('.caisse').val(),			
			'allocationsFamiliales.simpleAllocationsFamiliales.dateEcheance':this.detail.find('[name=dateEcheance]').val(),
			'allocationsFamiliales.simpleDonneeFinanciereHeader.isDessaisissementRevenu':this.detail.find('.dessaisissementRevenu').prop('checked'),
			'allocationsFamiliales.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'allocationsFamiliales.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};	
	
	this.clearFields=function(){
		 this.detail.find('.montantMensuel,.caisse,.selecteurCaisse,[name=dateEcheance],[name=dateDebut],[name=dateFin]').val('').end()
					.find('.dessaisissementRevenu').attr('checked',false);		
		
	};
	
	// Formatatage de la table
	this.formatTableTd=function($elementTable){	
	};
	
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

AllocationsFamilialesPart.prototype=DonneeFinancierePart.prototype;


$(function(){	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new AllocationsFamilialesPart($(this));
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