/*
 * FHA
 */
/**
 * 
 * @return
 */
function BienImmobilierNonHabitablePart(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX=ACTION_AJAX_BIEN_IMMOBILIER_NON_HABITABLE;
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
			.find('.typeBien').val($data.find('csTypeBien').text()).end()
			.find('.autres').val($data.find('autres').text()).end()
			.find('.valeurVenale').val($data.find('valeurVenale').text()).end()
			.find('.commune').val($data.find('commune').text()).end()
			.find('.selecteurCommune').val($data.find('nomCommune').text()).end()			
			.find('.numeroFeuillet').val($data.find('numeroFeuillet').text()).end()			
			.find('.dette').val($data.find('detteHypothecaire').text()).change().end()
			.find('.interets').val($data.find('interetHypothecaire').text()).change().end()
			.find('.numero').val($data.find('numeroHypothecaire').text()).change().end()
			.find('.compagnie').val($data.find('compagnie').text()).change().end()
			.find('.selecteurCompagnie').val($data.find('nomCompagnie').text()).change().end()
			.find('.rendements').val($data.find('rendements').text()).change().end()
			.find('.dessaisissementFortune').attr('checked',$data.find('DF').text()=='true').end()
			.find('.dessaisissementRevenus').attr('checked',$data.find('DR').text()=='true').end()
			.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
			.find('[name=dateFin]').val($data.find('dateFin').text()).end()
			.find('.idPays').val($data.find('idPays').text()).change().end()
			.find('.widgetPays').val($data.find('pays').text()).change().end()
		 	.find('.designationAutreType').val($data.find('designationAutreType').text()).end()
			.find('.selecteurCommune').val($data.find('nomCommune').text()).end();
		 this.detail.find('.lblCompagnie').text($data.find('nomCompagnie').text());
			
		 requirements(this.detail);
		// this.supperAfterRetrieve($data);
		 
	};
	
	this.getParametres=function($data){
		return {
			'bienImmobilierNonHabitable.simpleBienImmobilierNonHabitable.csTypePropriete':this.detail.find('.typePropriete').val(),
			'part':this.detail.find('.part').val(),
			'bienImmobilierNonHabitable.simpleBienImmobilierNonHabitable.csTypeBien':this.detail.find('.typeBien').val(),
			'bienImmobilierNonHabitable.simpleBienImmobilierNonHabitable.autresTypeBien':this.detail.find('.autres').val(),
			'bienImmobilierNonHabitable.simpleBienImmobilierNonHabitable.idPays':this.detail.find('.idPays').val(),
			'bienImmobilierNonHabitable.simpleBienImmobilierNonHabitable.valeurVenale':this.detail.find('.valeurVenale').val(),
			'bienImmobilierNonHabitable.simpleBienImmobilierNonHabitable.idCommuneDuBien':this.detail.find('.commune').val(),
			'bienImmobilierNonHabitable.simpleBienImmobilierNonHabitable.noFeuillet':this.detail.find('.numeroFeuillet').val(),
			'bienImmobilierNonHabitable.simpleBienImmobilierNonHabitable.montantDetteHypothecaire':this.detail.find('.dette').val(),
			'bienImmobilierNonHabitable.simpleBienImmobilierNonHabitable.montantInteretHypothecaire':this.detail.find('.interets').val(),
			'bienImmobilierNonHabitable.simpleBienImmobilierNonHabitable.noHypotheque':this.detail.find('.numero').val(),
			'bienImmobilierNonHabitable.simpleBienImmobilierNonHabitable.nomCompagnie':this.detail.find('.compagnie').val(),
			'bienImmobilierNonHabitable.simpleBienImmobilierNonHabitable.montantRendement':this.detail.find('.rendements').val(),			
			'bienImmobilierNonHabitable.simpleBienImmobilierNonHabitable.designationAutreType':this.detail.find('.designationAutreType').val(),
			'bienImmobilierNonHabitable.simpleDonneeFinanciereHeader.isDessaisissementFortune':this.detail.find('.dessaisissementFortune').prop('checked'),
			'bienImmobilierNonHabitable.simpleDonneeFinanciereHeader.isDessaisissementRevenu':this.detail.find('.dessaisissementRevenus').prop('checked'),
			'bienImmobilierNonHabitable.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'bienImmobilierNonHabitable.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};	
	
	this.clearFields=function(){
		 this.detail.find('.typePropriete,.typeBien,.autres,.commune,.selecteurCommune,.compagnie,.selecteurCompagnie,.part,.numeroFeuillet,.valeurVenale,.rendements,.dette,.interets,.numero,.designationAutreType,[name=dateDebut],[name=dateFin]').val('').end()
					.find('.dessaisissementFortune,.dessaisissementRevenu').attr('checked',false);		
		$('.typeBien')[0].selectedIndex = 0;
		$('.autreToHiden').hide();
		 
	};
	
	this.getParentViewBean=function(){
		return droit;	
	};
	
	this.setParentViewBean=function(newViewBean){
		droit=newViewBean;
	};
	
	this.formatTableTd=function($elementTable){
		$elementTable.find('.autres').css('width','270px');
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

BienImmobilierNonHabitablePart.prototype=DonneeFinancierePart.prototype;

function requirements(detail){
	 detail.find('.typePropriete').change(function() {
			var value=(detail.find(this).attr("value"));		
			
			if(value=="64009004") 
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
		
		var zone=new BienImmobilierNonHabitablePart($(this));
		this.zone=zone;
		zone.membreId=$(this).attr('idMembre');
		
		requirements($that.find(".areaDFDetail"));
		
		$(this).find('.btnAjaxUpdate').click(function(){
				zone.startEdition();
				$that.find('.btnAjaxValidateNouvellePeriode').show();
				
		}).end()
		.find('.btnAjaxCancel').click(function(){
			zone.stopEdition();
			$('.autreToHiden').hide();
			
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