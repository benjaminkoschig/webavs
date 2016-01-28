/*
 * FHA
 */

/**
 * 
 * @return
 */
function ContratEntretienViagerPart(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX=ACTION_AJAX_CONTRAT_ENTRETIEN_VIAGER;
	this.mainContainer=container;
	this.table=this.mainContainer.find(".areaDFDataTable");
	this.detail=this.mainContainer.find(".areaDFDetail");
	this.titleContainer=this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass="areaDFModified";
	this.membreId=null;
	
	// functions	
	this.afterRetrieve=function($data){
		
		//vide tout
		this.detail.find(".libelleId").next('.multiSelectOptions').find('INPUT').attr("checked",false);
		
		that.detail.find(".libelleId").html("<span style='width: 396px;'>Pas de sélection</span>");
		
		$data.find('csLibelle libelle').each(function(){
			id = $(this).text(); 
						
			that.detail.find(".libelleId").next('.multiSelectOptions').find('INPUT[value='+id+']').attr("checked", true);						

			that.detail.find(".libelleId").html("<span style='width: 396px;'>Sélection</span>");			
			
		});
		
		 this.detail.find('.montantContrat').val($data.find('montantContrat').text()).end()			
					.find('.dessaisissementRevenu').attr('checked',$data.find('DR').text()=='true').end()
					.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
					.find('[name=dateFin]').val($data.find('dateFin').text()).end();		 		

		 
		 // si autre est cochée on affiche une box en plus
			
			var selected="";
			this.detail.find(".libelleId").next('.multiSelectOptions').find('INPUT:checkbox:checked').not('.optGroup, .selectAll').each(function() {
				selected+=$(this).attr('value');
			}); 
			this.addSpy($data);
	
		 
	};
	
	this.getParametres=function($data){
        
	 	//Récupération des éléments cochés
		var typeLibelle = "";
		this.detail.find(".libelleId").next('.multiSelectOptions').find('INPUT:checkbox:checked').not('.optGroup, .selectAll').each(function() {
			typeLibelle += $(this).attr('value');
		});
 	
		
		return {			
			'contratEntretienViager.simpleContratEntretienViager.montantContrat':this.detail.find('.montantContrat').val(),			
			'simpleLibelleContratEntretienViager.csLibelleContratEntretienViager':typeLibelle,	
			'contratEntretienViager.simpleDonneeFinanciereHeader.isDessaisissementRevenu':this.detail.find('.dessaisissementRevenu').prop('checked'),
			'contratEntretienViager.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'contratEntretienViager.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};		
	
	this.clearFields=function(){
		 this.detail.find('.montantContrat,[name=dateDebut],[name=dateFin]').val('').end()
					.find('.dessaisissementRevenu').attr('checked',false);	
		
		 this.detail.find(".libelleId").next('.multiSelectOptions').find('INPUT').attr("checked",false);

		
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

ContratEntretienViagerPart.prototype=DonneeFinancierePart.prototype;

$(function(){	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new ContratEntretienViagerPart($(this));
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
				$(".libelleId").html("<span style='width: 396px;'>Pas de sélection</span>");
		}).end()
		.find('.btnAjaxValidateNouvellePeriode').click(function(){
			zone.doAddPeriode=true;
			zone.validateEdition();
		}).end();	
		
	});
	
	var selected="";
	$(".libelleId").next('.multiSelectOptions').find('INPUT:checkbox:checked').not('.optGroup, .selectAll').each(function() {
		selected+=$(this).attr('value');
	}); 	
});