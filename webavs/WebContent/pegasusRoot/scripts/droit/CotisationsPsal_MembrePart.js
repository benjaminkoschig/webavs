/*
 * FHA
 */
/**
 * 
 * @return
 */
function CotisationsPsalPart(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX=ACTION_AJAX_COTISATIONS_PSAL;
	this.mainContainer=container;
	this.table=this.mainContainer.find(".areaDFDataTable");
	this.detail=this.mainContainer.find(".areaDFDetail");
	this.titleContainer=this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass="areaDFModified";
	this.membreId=null;
	
	// functions	
	this.afterRetrieve=function($data){
		 this.detail.find('.montantCotisation').val($data.find('montantCotisation').text()).end()
					.find('.caisse').val($data.find('idCaisseAF').text()).end()
					.find('.selecteurCaisse').val($data.find('nomCaisse').text()).end()		
					.find('[name=dateEcheance]').val($data.find('dateEcheance').text()).end()					
					.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
					.find('[name=dateFin]').val($data.find('dateFin').text()).end();
		this.addSpy($data);
	};
	
	this.getParametres=function($data){
		return {
			'cotisationsPsal.simpleCotisationsPsal.montantCotisationsAnnuelles':this.detail.find('.montantCotisation').val(),
			'cotisationsPsal.simpleCotisationsPsal.idCaisseCompensation':this.detail.find('.caisse').val(),			
			'cotisationsPsal.simpleCotisationsPsal.dateEcheance':this.detail.find('[name=dateEcheance]').val(),			
			'cotisationsPsal.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'cotisationsPsal.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};	
	
	this.formatTableTd=function($elementTable){
		$elementTable.find('.montantCotisation,.caisse,.selecteurCaisse,[name=dateEcheance],[name=dateDebut],[name=dateFin]').val('').end();		
		
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

CotisationsPsalPart.prototype=DonneeFinancierePart.prototype;


$(function(){	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new CotisationsPsalPart($(this));
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