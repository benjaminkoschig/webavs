/*
 * FHA
 */

/**
 * 
 * @return
 */
function RevenuActiviteLucrativeDependantePart(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX=ACTION_AJAX_ACTIVITE_LUCRATIVE_DEPENDANTE;
	this.mainContainer=container;
	this.table=this.mainContainer.find(".areaDFDataTable");
	this.detail=this.mainContainer.find(".areaDFDetail");
	this.titleContainer=this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass="areaDFModified";
	this.membreId=null;
	
	// functions	
	this.afterRetrieve=function($data){
		
		//vide tout
		that.detail.find(".fraisObtentionId").next('.multiSelectOptions').find('INPUT').attr("checked",false);
		
		that.detail.find(".fraisObtentionId").html("<span style='width: 396px;'>Pas de sélection</span>");							
		
		$data.find('fraisObtention frais').each(function(){			
			id = $(this).text(); 		
			that.detail.find(".fraisObtentionId").next('.multiSelectOptions').find('INPUT[value='+id+']').attr("checked", true);
			//on a trouvé qqc il faut mettre l'état à sélectionné
			that.detail.find(".fraisObtentionId").html("<span style='width: 396px;'>Sélection</span>");
		});
		
		
		 this.detail.find('[name=genreRevenu]').val($data.find('csGenreRevenu').text()).end()
					.find('.revenuNature').val($data.find('typeRevenuNature').text()).end()
					.find('.employeur').val($data.find('idEmployeur').text()).end()
					.find('.montant').val($data.find('montant').text()).end()
					.find('.deductionsSociales').val($data.find('deductionSociale').text()).end()
					.find('.deductionsLpp').val($data.find('deductionsLpp').text()).end()																			
					.find('.autres').val($data.find('autreFraisObtentionRevenu').text()).end()
					.find('.montantFrais').val($data.find('montantFrais').text()).end()					
					.find('.dessaisissementRevenu').attr('checked',$data.find('DR').text()=='true').end()
					.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
					.find('[name=dateFin]').val($data.find('dateFin').text()).end()		 		
		 			.find('.nomAffilie').text($data.find('noAffilie').text()+" "+$data.find('nomEmployeur').text());
		 //this.detail.find('.nomAffilie').text($data.find('nomCompagnie').text());
		 // si autre est cochée on affiche une box en plus
			
			var selected="";
			that.detail.find(".fraisObtentionId").next('.multiSelectOptions').find('INPUT:checkbox:checked').not('.optGroup, .selectAll').each(function() {
				selected+=$(this).attr('value');
			});
 	
			if(selected != ""){
				that.detail.find('.montantFrais').show();
				that.detail.find('.montantFraisLine').show();
			}
			else{
				that.detail.find('.montantFrais').hide();
				that.detail.find('.montantFraisLine').hide();
			}
		 
			if(this.detail.find(".fraisObtentionId").next('.multiSelectOptions').find('INPUT[value=64035005]').attr("checked")){
				that.detail.find('.autres').show();
				that.detail.find('.autresLine').show();

			}
			else{
				that.detail.find('.autres').hide();
				that.detail.find('.autresLine').hide();
			}
			
			that.detail.find('.fraisObtentionId').next('.multiSelectOptions').click(function() {

				var value="";
				that.detail.find(".fraisObtentionId").next('.multiSelectOptions').find('INPUT:checkbox:checked').not('.optGroup, .selectAll').each(function() {
					value += $(this).attr('value');
				});			 	
				
				if(value!="") 
				{ 				
					that.detail.find('.montantFrais').show();
					that.detail.find('.montantFraisLine').show();
				}	
				else{ 				
					that.detail.find('.montantFrais').hide();
					that.detail.find('.montantFraisLine').hide();
				}	
				
				
				if(that.detail.find(".fraisObtentionId").next('.multiSelectOptions').find('INPUT[value=64035005]').attr("checked")){
					that.detail.find('.autres').show();
					that.detail.find('.autresLine').show();
				}
				else
				{
					that.detail.find('.autres').hide();
					that.detail.find('.autresLine').hide();
				}
			});		
			this.addSpy($data);
	
		 
	};
	
	this.getParametres=function($data){
        		
	 	//Récupération des éléments cochés
		var typeFrais = "";		
		
		
		this.detail.find(".fraisObtentionId").next('.multiSelectOptions').find('INPUT:checkbox:checked').not('.optGroup, .selectAll').each(function() {
			typeFrais += $(this).attr('value');
		});				
		
		return {
			'revenuActiviteLucrativeDependante.simpleRevenuActiviteLucrativeDependante.csGenreRevenu':this.detail.find('[name=genreRevenu]').val(),
			'revenuActiviteLucrativeDependante.simpleRevenuActiviteLucrativeDependante.typeRevenu':this.detail.find('.revenuNature').val(),
			'revenuActiviteLucrativeDependante.simpleRevenuActiviteLucrativeDependante.idEmployeur':this.detail.find('.employeur').val(),
			'revenuActiviteLucrativeDependante.simpleRevenuActiviteLucrativeDependante.montantActiviteLucrative':this.detail.find('.montant').val(),
			'revenuActiviteLucrativeDependante.simpleRevenuActiviteLucrativeDependante.deductionsSociales':this.detail.find('.deductionsSociales').val(),
			'revenuActiviteLucrativeDependante.simpleRevenuActiviteLucrativeDependante.deductionsLpp':this.detail.find('.deductionsLpp').val(),			
			'revenuActiviteLucrativeDependante.simpleRevenuActiviteLucrativeDependante.idAffiliation':this.detail.find('.idAffiliation').val(),
			'simpleTypeFraisObtentionRevenu.csFraisObtentionRevenu':typeFrais,			
			
			'revenuActiviteLucrativeDependante.simpleRevenuActiviteLucrativeDependante.autreFraisObtentionRevenu':this.detail.find('.autres').val(),
			'revenuActiviteLucrativeDependante.simpleRevenuActiviteLucrativeDependante.montantFrais':this.detail.find('.montantFrais').val(),						
			'revenuActiviteLucrativeDependante.simpleDonneeFinanciereHeader.isDessaisissementRevenu':this.detail.find('.dessaisissementRevenu').prop('checked'),
			'revenuActiviteLucrativeDependante.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'revenuActiviteLucrativeDependante.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};		
	
	this.clearFields=function(){
		 this.detail.find('[name=genreRevenu],.revenuNature,.selecteurEmployeur,.montant,.deductionsSociales,.deductionsLpp,.autres,.montantFrais,[name=dateDebut],[name=dateFin]').val('').end()
					.find('.dessaisissementRevenu').attr('checked',false);	
		
		 this.detail.find(".fraisObtentionId").next('.multiSelectOptions').find('INPUT').attr("checked",false);
		 
		
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

RevenuActiviteLucrativeDependantePart.prototype=DonneeFinancierePart.prototype;

function requirements(detail){
	var selected="";
	detail.find(".fraisObtentionId").next('.multiSelectOptions').find('INPUT:checkbox:checked').not('.optGroup, .selectAll').each(function() {
		selected+=detail.find(this).attr('value');
	});

	if(selected != ""){
		detail.find('.montantFrais').show();
		detail.find('.montantFraisLine').show();
	}
	else{
		detail.find('.montantFrais').hide();
		detail.find('.montantFraisLine').hide();
	}
 
	if(detail.find(".fraisObtentionId").next('.multiSelectOptions').find('INPUT[value=64035005]').attr("checked")){
		detail.find('.autres').show();
		detail.find('.autresLine').show();

	}
	else{
		detail.find('.autres').hide();
		detail.find('.autresLine').hide();
	}
	
	detail.find('.fraisObtentionId').next('.multiSelectOptions').click(function() {

		var value="";
		detail.find(".fraisObtentionId").next('.multiSelectOptions').find('INPUT:checkbox:checked').not('.optGroup, .selectAll').each(function() {
			value += detail.find(this).attr('value');
		});			 	
		
		if(value!="") 
		{ 				
			detail.find('.montantFrais').show();
			detail.find('.montantFraisLine').show();
		}	
		else{ 				
			detail.find('.montantFrais').hide();
			detail.find('.montantFraisLine').hide();
		}	
		
		
		if(detail.find(".fraisObtentionId").next('.multiSelectOptions').find('INPUT[value=64035005]').attr("checked")){
			detail.find('.autres').show();
			detail.find('.autresLine').show();
		}
		else
		{
			detail.find('.autres').hide();
			detail.find('.autresLine').hide();
		}
	});		
						
}


$(function(){	
	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new RevenuActiviteLucrativeDependantePart($(this));
		this.zone=zone;
		zone.membreId=$(this).attr('idMembre');
		
		requirements($that.find(".areaDFDetail"));
		
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
				$(".fraisObtentionId").html("<span style='width: 396px;'>Pas de sélection</span>");
				requirements($that.find(".areaDFDetail"));
		}).end()
		.find('.btnAjaxValidateNouvellePeriode').click(function(){
			zone.doAddPeriode=true;
			zone.validateEdition();
		}).end();	
		
		
	});
});