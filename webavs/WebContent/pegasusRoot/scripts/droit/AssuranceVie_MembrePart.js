/*
 * FHA
 */
/**
 * 
 * @return
 */
function AssuranceViePart(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX=ACTION_AJAX_ASSURANCE_VIE;
	this.mainContainer=container;
	this.table=this.mainContainer.find(".areaDFDataTable");
	this.detail=this.mainContainer.find(".areaDFDetail");
	this.titleContainer=this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass="areaDFModified";
	this.membreId=null;
	
	// functions	
	this.afterRetrieve=function($data){
		this.detail.find('.valeurRachat').val($data.find('valeurRachat').text()).end()
					.find('.numeroPolice').val($data.find('numeroPolice').text()).end()
					.find('.compagnie').val($data.find('nomCompagnie').text()).end()
					.find('.lblCompagnie').text($data.find('nomNomCompagnie').text()).end()
					.find('[name=dateEcheance]').val($data.find('dateEcheance').text()).end()									
					.find('.dessaisissementRevenu').attr('checked',$data.find('DF').text()=='true').end()
					.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
					.find('[name=dateFin]').val($data.find('dateFin').text()).end();
		this.addSpy($data);
	};
	
	
	this.getParametres=function($data){
		return {
			'assuranceVie.simpleAssuranceVie.montantValeurRachat':this.detail.find('.valeurRachat').val(),
			'assuranceVie.simpleAssuranceVie.numeroPolice':this.detail.find('.numeroPolice').val(),
			'assuranceVie.simpleAssuranceVie.nomCompagnie':this.detail.find('.compagnie').val(),			
			'assuranceVie.simpleAssuranceVie.dateEcheance':this.detail.find('[name=dateEcheance]').val(),
			'assuranceVie.simpleDonneeFinanciereHeader.isDessaisissementFortune':this.detail.find('.dessaisissementRevenu').prop('checked'),
			'assuranceVie.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'assuranceVie.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};	
	
	this.clearFields=function(){
		 this.detail.find('.valeurRachat,.numeroPolice,.compagnie,.selecteurCompagnie,[name=dateEcheance],[name=dateDebut],[name=dateFin]').val('').end()
					.find('.dessaisissementRevenu').attr('checked',false);		
		
	};
	
	this.getParentViewBean=function(){
		return droit;	
	};
	
	this.setParentViewBean=function(newViewBean){
		droit=newViewBean;
	};

	this.formatTableTd=function($elementTable){
	};	
	
	// initialization
	this.init(
	    function(){	
			this.stopEdition();
			this.onAddTableEvent();
			this.colorTableRows(false);
		}
	);
}

AssuranceViePart.prototype=DonneeFinancierePart.prototype;


$(function(){	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new AssuranceViePart($(this));
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