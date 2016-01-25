/*
 * ECO
 */

/**
 * 
 * @return
 */
function CompteBancaireCCPPart(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX=ACTION_AJAX_COMPTE_BANCAIRE_CCP;
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
					.find('.typeCompte').val($data.find('csTypeCompte').text()).end()
					.find('.iban').val($data.find('iban').text()).end()
					.find('.montant').val($data.find('montant').text()).change().end()
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
			'compteBancaireCCP.simpleCompteBancaireCCP.csTypePropriete':this.detail.find('.typePropriete').val(),
			'part':this.detail.find('.part').val(),
			'compteBancaireCCP.simpleCompteBancaireCCP.csTypeCompte':this.detail.find('.typeCompte').val(),
			'compteBancaireCCP.simpleCompteBancaireCCP.iban':this.detail.find('.iban').val(),
			'compteBancaireCCP.simpleCompteBancaireCCP.montant':this.detail.find('.montant').val(),
			'compteBancaireCCP.simpleCompteBancaireCCP.isSansInteret':this.detail.find('.interet').prop('checked'),
			'compteBancaireCCP.simpleCompteBancaireCCP.montantInteret':this.detail.find('.montantInteret').val(),
			'compteBancaireCCP.simpleCompteBancaireCCP.montantFraisBancaire':this.detail.find('.montantFrais').val(),			
			'compteBancaireCCP.simpleDonneeFinanciereHeader.isDessaisissementFortune':this.detail.find('.dessaisissementFortune').prop('checked'),
			'compteBancaireCCP.simpleDonneeFinanciereHeader.isDessaisissementRevenu':this.detail.find('.dessaisissementRevenu').prop('checked'),
			'compteBancaireCCP.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'compteBancaireCCP.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};	
	
	this.clearFields=function(){
		 this.detail.find('.typePropriete,.typeCompte,.part,.iban,.montant,.montantInteret,.montantFrais,[name=dateDebut],[name=dateFin]').val('').end()
					.find('.dessaisissementFortune,.dessaisissementRevenu,.interet').attr('checked',false);		
		
	};
	
	this.getParentViewBean=function(){
		return droit;	
	};
	
	this.setParentViewBean=function(newViewBean){
		droit=newViewBean;
	}

	this.formatTableTd=function($elementTable){
		$elementTable.find('td:eq(3)').css('text-align','right').end()
		                              .find("td:eq(5)").css('text-align','center');
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

CompteBancaireCCPPart.prototype=DonneeFinancierePart.prototype;

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
		var zone=new CompteBancaireCCPPart($(this));
		this.zone=zone;
		zone.membreId=$(this).attr('idMembre');
				
		requirements($that.find(".areaDFDetail"));
		
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