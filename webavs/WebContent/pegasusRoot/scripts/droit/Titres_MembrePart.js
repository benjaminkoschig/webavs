/*
 * ECO
 */

/**
 * 
 * @return
 */
function TitrePart(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX=ACTION_AJAX_TITRE;
	this.mainContainer=container;
	this.table=this.mainContainer.find(".areaDFDataTable");
	this.detail=this.mainContainer.find(".areaDFDetail");
	this.titleContainer=this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass="areaDFModified";
	this.membreId=null;
	
	// functions	
	this.afterRetrieve=function($data){
		 this.detail.find('.typePropriete').val($data.find('csTypePropriete').text()).end()
					.find('.part').val($data.find('part').text()).end()
					.find('.genre').val($data.find('csGenre').text()).end()
					.find('.autres').val($data.find('autres').text()).end()
					.find('.designation').val($data.find('designation').text()).end()
					.find('.valeur').val($data.find('valeur').text()).end()
					.find('.montant').val($data.find('montant').text()).change().end()
					.find('.sansRendemeent').attr('checked',$data.find('sansRendement').text()=='true').end()
					.find('.rendement').val($data.find('rendement').text()).change().end()
					.find('.droitGarde').val($data.find('droitGarde').text()).change().end()					
					.find('.dessaisissementFortune').attr('checked',$data.find('DF').text()=='true').end()
					.find('.dessaisissementRevenu').attr('checked',$data.find('DR').text()=='true').end()
					.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
					.find('[name=dateFin]').val($data.find('dateFin').text()).end();
		 
		 requirements(this.detail);
		 this.addSpy($data); 
		 
	};
	
	this.getParametres=function($data){								
		
		return {
			'titre.simpleTitre.csTypePropriete':this.detail.find('.typePropriete').val(),
			'part':this.detail.find('.part').val(),
			'titre.simpleTitre.csGenreTitre':this.detail.find('.genre').val(),
			'titre.simpleTitre.autreGenreTitre':this.detail.find('.autres').val(),
			'titre.simpleTitre.designationTitre':this.detail.find('.designation').val(),
			'titre.simpleTitre.numeroValeur':this.detail.find('.valeur').val(),
			'titre.simpleTitre.montantTitre':this.detail.find('.montant').val(),
			'titre.simpleTitre.isSansRendement':this.detail.find('.sansRendement').prop('checked'),
			'titre.simpleTitre.rendementTitre':this.detail.find('.rendement').val(),
			'titre.simpleTitre.droitDeGarde':this.detail.find('.droitGarde').val(),			
			'titre.simpleDonneeFinanciereHeader.isDessaisissementFortune':this.detail.find('.dessaisissementFortune').prop('checked'),
			'titre.simpleDonneeFinanciereHeader.isDessaisissementRevenu':this.detail.find('.dessaisissementRevenu').prop('checked'),
			'titre.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'titre.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};	
	
	this.clearFields=function(){		
		 this.detail.find('.typePropriete,.genre,.part,.designation,.montant,.autres,.droitGarde,.rendement,.valeur,[name=dateDebut],[name=dateFin]').val('').end()
					.find('.dessaisissementFortune,.dessaisissementRevenu,.sansRendement').attr('checked',false);		
		
	};
	
	this.getParentViewBean=function(){
		return droit;	
	};
	
	this.setParentViewBean=function(newViewBean){
		droit=newViewBean;
	}

	this.formatTableTd=function($elementTable){
		$elementTable.find('td:eq(3),td:eq(4)').css('text-align','left').end();
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

TitrePart.prototype=DonneeFinancierePart.prototype;

function requirements(detail){	
	 detail.find('.typePropriete').change(function() {
			//var value=(detail.find(detail).attr("value"));					
			if(detail.find('.typePropriete').val()=="64009004") 
			{ 				
				detail.find('.part').attr("readonly",true);		
				detail.find('.part').css("color","red");
				detail.find('.part').val("1/1");	
			}	
			else{ 				
				detail.find('.part').attr("readonly",false);
				detail.find('.part').css("color","black");
			}	
					
	});	
	  
	 if(detail.find('.typePropriete').val()=="64009004"){
		 
		 detail.find('.part').attr("readonly",true);	
		 detail.find('.part').css("color","red");
	 }
	 else{	 
		 detail.find('.part').attr("readonly",false);
		 detail.find('.part').css("color","black");
	 }
}


$(function(){	
	$('.areaMembre').each(function(){
		var $that=$(this);		
		
		requirements($that.find(".areaDFDetail"));
		
		var zone=new TitrePart($(this));
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
				$('.part').val("1/1");		
				requirements($that.find(".areaDFDetail"));
		}).end()
		.find('.btnAjaxValidateNouvellePeriode').click(function(){
			zone.doAddPeriode=true;
			zone.validateEdition();
		}).end();		
	});		
	
});