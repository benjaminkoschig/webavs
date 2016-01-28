/*
 * ECO
 */
/**
 * 
 * @return
 */
function AssuranceRenteViagerePart(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX=ACTION_AJAX_ASSURANCE_RENTE_VIAGERE;
	this.mainContainer=container;
	this.table=this.mainContainer.find(".areaDFDataTable");
	this.detail=this.mainContainer.find(".areaDFDetail");
	this.titleContainer=this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass="areaDFModified";
	this.membreId=null;
	
	// functions	
	this.afterRetrieve=function($data){
		 this.detail.find('.montantRachat').val($data.find('montantRachat').text()).change().end()
					.find('.noPolice').val($data.find('noPolice').text()).end()
					.find('.compagnie').val($data.find('idCompagnie').text()).end()
					.find('.selecteurCompagnie').val($data.find('nomCompagnie').text()).end()
					.find('.renteMontant').val($data.find('renteMontant').text()).change().end()
					.find('.renteExcedent').val($data.find('renteExcedent').text()).change().end()
					.find('.dessaisissementFortune').attr('checked',$data.find('DF').text()=='true').end()
					.find('.dessaisissementRevenu').attr('checked',$data.find('DR').text()=='true').end()
					.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
					.find('[name=dateFin]').val($data.find('dateFin').text()).end();
		 this.addSpy($data);
	};
	
	this.getParametres=function($data){
		return {
			'assuranceRenteViagere.simpleAssuranceRenteViagere.montantValeurRachat':this.detail.find('.montantRachat').val(),
			'assuranceRenteViagere.simpleAssuranceRenteViagere.numeroPolice':this.detail.find('.noPolice').val(),
			'assuranceRenteViagere.simpleAssuranceRenteViagere.idCompagnie':this.detail.find('.compagnie').val(),
			'assuranceRenteViagere.simpleAssuranceRenteViagere.montantRenteViagere':this.detail.find('.renteMontant').val(),
			'assuranceRenteViagere.simpleAssuranceRenteViagere.excedentRenteViagere':this.detail.find('.renteExcedent').val(),
			'assuranceRenteViagere.simpleDonneeFinanciereHeader.isDessaisissementFortune':this.detail.find('.dessaisissementFortune').prop('checked'),
			'assuranceRenteViagere.simpleDonneeFinanciereHeader.isDessaisissementRevenu':this.detail.find('.dessaisissementRevenu').prop('checked'),
			'assuranceRenteViagere.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'assuranceRenteViagere.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};	
	
	this.clearFields=function(){
		 this.detail.find('.montantRachat,.noPolice,.compagnie,.renteMontant,.renteExcedent,[name=dateDebut],[name=dateFin]').val('').end()
					.find('.dessaisissementFortune,.dessaisissementRevenu').attr('checked',false);		
		
	};
	
	this.getParentViewBean=function(){
		return droit;	
	};
	
	this.setParentViewBean=function(newViewBean){
		droit=newViewBean;
	}
	
	this.formatTableTd=function($elementTable){
		$elementTable.find('td:eq(1),td:eq(4),td:eq(5)').css('text-align','right').end();
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

AssuranceRenteViagerePart.prototype=DonneeFinancierePart.prototype;


$(function(){	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new AssuranceRenteViagerePart($(this));
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