/*
 * ECO
 */
/**
 * 
 * @return
 */
function RevenuActiviteLucrativeIndependantePart(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX=ACTION_AJAX_ACTIVITE_LUCRATIVE_INDEPENDANTE;
	this.mainContainer=container;
	this.table=this.mainContainer.find(".areaDFDataTable");
	this.detail=this.mainContainer.find(".areaDFDetail");
	this.titleContainer=this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass="areaDFModified";
	this.membreId=null;	
	
	// functions	
	this.afterRetrieve=function($data){
		 this.detail.find('[name=determinationRevenu]').val($data.find('csDeterminationRevenu').text()).end()
					.find('[name=genreRevenu]').val($data.find('csGenreRevenu').text()).end()
					.find('.montant').val($data.find('montant').text()).end()
			 		.find('.fraisDeGarde').val($data.find('fraisDeGarde').text()).end()
					.find('.idTiersAffilie').val($data.find('idTiersAffilie').text()).end()
					.find('.caisse').val($data.find('idCaisseCompensation').text()).end()	
					.find('.selecteurCaisse').val($data.find('nomCaisse').text()).end()					
					.find('.dessaisissementRevenu').attr('checked',$data.find('DR').text()=='true').end()
					.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
					.find('[name=dateFin]').val($data.find('dateFin').text()).end()
					.find('.nomAffilie').text($data.find('noAffilie').text()+" "+$data.find('nomAffilie').text());
		this.addSpy($data);	
	};
	
	this.getParametres=function($data){
		return {
			'revenuActiviteLucrativeIndependante.simpleRevenuActiviteLucrativeIndependante.csDeterminationRevenu':this.detail.find('[name=determinationRevenu]').val(),
			'revenuActiviteLucrativeIndependante.simpleRevenuActiviteLucrativeIndependante.csGenreRevenu':this.detail.find('[name=genreRevenu]').val(),
			'revenuActiviteLucrativeIndependante.simpleRevenuActiviteLucrativeIndependante.montantRevenu':this.detail.find('.montant').val(),
			'revenuActiviteLucrativeIndependante.simpleRevenuActiviteLucrativeIndependante.fraisDeGarde':this.detail.find('.fraisDeGarde').val(),
			'revenuActiviteLucrativeIndependante.simpleRevenuActiviteLucrativeIndependante.idTiersAffilie':this.detail.find('.idTiersAffilie').val(),
			'revenuActiviteLucrativeIndependante.simpleRevenuActiviteLucrativeIndependante.idCaisseCompensation':this.detail.find('.caisse').val(),
			'revenuActiviteLucrativeIndependante.simpleDonneeFinanciereHeader.isDessaisissementRevenu':this.detail.find('.dessaisissementRevenu').prop('checked'),
			'revenuActiviteLucrativeIndependante.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'revenuActiviteLucrativeIndependante.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'revenuActiviteLucrativeIndependante.simpleRevenuActiviteLucrativeIndependante.idAffiliation':this.detail.find('.idAffiliation').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};	
	
	this.clearFields=function(){
		 this.detail.find('[name=determinationRevenu],[name=genreRevenu],.montant,.fraisDeGarde,.selecteurAffilie,.selecteurCaisse,[name=dateDebut],[name=dateFin]').val('').end()
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

RevenuActiviteLucrativeIndependantePart.prototype=DonneeFinancierePart.prototype;


$(function(){	
	$('.areaMembre').each(function(){
		var $that=$(this);
		
		var zone=new RevenuActiviteLucrativeIndependantePart($(this));
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
				$('[name=genreRevenu]').val("64038001");
				
		}).end()		
		.find('.btnAjaxValidateNouvellePeriode').click(function(){
			zone.doAddPeriode=true;
			zone.validateEditionV2();
		}).end();	
	});
});