/*
 * FHA
 */
/**
 * 
 * @return
 */
function CapitalLPPPart(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX=ACTION_AJAX_CAPITAL_LPP;
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
			.find('.capitalLPP').val($data.find('capitalLPP').text()).end()
			.find('.numeroPolice').val($data.find('numeroPolice').text()).end()
			.find('.institution').val($data.find('institution').text()).end()
			.find('.selecteurInstitution').val($data.find('nomInstitution').text()).end()
			.find('[name=dateLiberation]').val($data.find('dateLiberation').text()).end()
			.find('.destinationLiberation').val($data.find('destinationLiberation').text()).end()			
			.find('.interet').attr('checked',$data.find('interet').text()=='true').end()
			.find('.montantInteret').val($data.find('montantInteret').text()).change().end()
			.find('.montantFrais').val($data.find('montantFrais').text()).change().end()					
			.find('.dessaisissementFortune').attr('checked',$data.find('DF').text()=='true').end()
			.find('.dessaisissementRevenu').attr('checked',$data.find('DR').text()=='true').end()
			.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
			.find('[name=dateFin]').val($data.find('dateFin').text()).end();
		 
		 requirements(this.detail);
		 this.addSpy($data);
	};
	
	this.getParametres=function($data){
		return {
			'capitalLPP.simpleCapitalLPP.csTypePropriete':this.detail.find('.typePropriete').val(),
			'part':this.detail.find('.part').val(),
			'capitalLPP.simpleCapitalLPP.montantCapitalLPP':this.detail.find('.capitalLPP').val(),
			'capitalLPP.simpleCapitalLPP.noPoliceNoCompte':this.detail.find('.numeroPolice').val(),
			'capitalLPP.simpleCapitalLPP.idInstitutionPrevoyance':this.detail.find('.institution').val(),
			'capitalLPP.simpleCapitalLPP.dateLiberation':this.detail.find('[name=dateLiberation]').val(),
			'capitalLPP.simpleCapitalLPP.destinationLiberation':this.detail.find('.destinationLiberation').val(),
			'capitalLPP.simpleCapitalLPP.isSansInteret':this.detail.find('.interet').prop('checked'),
			'capitalLPP.simpleCapitalLPP.montantInteret':this.detail.find('.montantInteret').val(),
			'capitalLPP.simpleCapitalLPP.montantFrais':this.detail.find('.montantFrais').val(),			
			'capitalLPP.simpleDonneeFinanciereHeader.isDessaisissementFortune':this.detail.find('.dessaisissementFortune').prop('checked'),
			'capitalLPP.simpleDonneeFinanciereHeader.isDessaisissementRevenu':this.detail.find('.dessaisissementRevenu').prop('checked'),
			'capitalLPP.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'capitalLPP.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};	
	
	this.clearFields=function(){
		 this.detail.find('.institution,.selecteurInstitution,.typePropriete,.capitalLPP,.part,.numeroPolice,.montant,.montantInteret,.montantFrais,[name=dateLiberation],[name=dateDebut],[name=dateFin]').val('').end()
					.find('.dessaisissementFortune,.dessaisissementRevenu,.interet').attr('checked',false);		
		
	};
	
	this.getParentViewBean=function(){
		return droit;	
	};
	
	this.setParentViewBean=function(newViewBean){
		droit=newViewBean;
	}
	
	this.formatTableTd=function($elementTable){		
	
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

CapitalLPPPart.prototype=DonneeFinancierePart.prototype;

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
		var zone=new CapitalLPPPart($(this));
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
				$('.part').val("1/1");
				requirements($that.find(".areaDFDetail"));
		}).end()
		.find('.btnAjaxValidateNouvellePeriode').click(function(){
			zone.doAddPeriode=true;
			zone.validateEdition();
		}).end();	
		
	});
		
});