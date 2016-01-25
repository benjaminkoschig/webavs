/*
 * ECO
 */

/**
 * 
 * @return
 */
function AutreFortuneMobilierePart(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX=ACTION_AJAX_DONNEE_FINANCIERE;
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
					.find('.montant').val($data.find('montant').text()).change().end()
					.find('.csTypeFortune').val($data.find('csTypeFortune').text()).end()
					.find('.dessaisissementFortune').attr('checked',$data.find('DF').text()=='true').end()
					.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
					.find('[name=dateFin]').val($data.find('dateFin').text()).end()
					.find('.autres').val($data.find('autres').text()).end();
		 this.addSpy($data);
	};
	
	this.getParametres=function($data){
		return {
			'autreFortuneMobiliere.simpleAutreFortuneMobiliere.csTypePropriete':this.detail.find('.typePropriete').val(),
			'part':this.detail.find('.part').val(),
			'autreFortuneMobiliere.simpleAutreFortuneMobiliere.montant':this.detail.find('.montant').val(),
			'autreFortuneMobiliere.simpleAutreFortuneMobiliere.csTypeFortune':this.detail.find('.csTypeFortune').val(),
			'autreFortuneMobiliere.simpleDonneeFinanciereHeader.isDessaisissementFortune':this.detail.find('.dessaisissementFortune').prop('checked'),
			'autreFortuneMobiliere.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'autreFortuneMobiliere.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'autreFortuneMobiliere.simpleAutreFortuneMobiliere.autre':this.detail.find('.autres').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};	
	
	this.clearFields=function(){
		 this.detail.find('.typePropriete,.csTypeFortune,.part,.designation,.montant,[name=dateDebut],[name=dateFin]').val('').end()
					.find('.dessaisissementFortune,.dessaisissementRevenu').attr('checked',false);		
		
	};
	
	this.getParentViewBean=function(){
		return droit;	
	};
	
	this.setParentViewBean=function(newViewBean){
		droit=newViewBean;
	}

	this.formatTableTd=function($elementTable){
		$elementTable.find('td:eq(3)').css('text-align','right').end();
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

AutreFortuneMobilierePart.prototype=DonneeFinancierePart.prototype;


$(function(){	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new AutreFortuneMobilierePart($(this));
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
		}).end()
		.find('.btnAjaxValidateNouvellePeriode').click(function(){
			zone.doAddPeriode=true;
			zone.validateEdition();
		}).end();
		
	});
});