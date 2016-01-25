/*
 * ECO
 */

/**
 * 
 * @return
 */
function BetailPart(container){
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
					.find('.designation').val($data.find('designation').text()).end()
					.find('.dessaisissementFortune').attr('checked',$data.find('DF').text()=='true').end()
					.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
					.find('[name=dateFin]').val($data.find('dateFin').text()).end();
		this.addSpy($data);
	};
	
	this.getParametres=function($data){
		return {
			'betail.simpleBetail.csTypePropriete':this.detail.find('.typePropriete').val(),
			'part':this.detail.find('.part').val(),
			'betail.simpleBetail.montant':this.detail.find('.montant').val(),
			'betail.simpleBetail.designation':this.detail.find('.designation').val(),
			'betail.simpleDonneeFinanciereHeader.isDessaisissementFortune':this.detail.find('.dessaisissementFortune').prop('checked'),
			'betail.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'betail.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};	
	
	this.clearFields=function(){
		 this.detail.find('.typePropriete,.part,.designation,.montant,[name=dateDebut],[name=dateFin]').val('').end()
					.find('.dessaisissementFortune').attr('checked',false);		
		
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

BetailPart.prototype=DonneeFinancierePart.prototype;


$(function(){	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new BetailPart($(this));
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