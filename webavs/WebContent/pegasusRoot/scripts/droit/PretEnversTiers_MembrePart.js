/*
 * ECO
 */

/**
 * 
 * @return
 */
function PretEnversTiersPart(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX=ACTION_AJAX_PRET_ENVERS_TIERS;
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
					.find('.typePret').val($data.find('typePret').text()).end()
					.find('.montant').val($data.find('montant').text()).change().end()
					.find('.beneficiaire').val($data.find('beneficiaire').text()).end()
					.find('[name=dateEcheance]').val($data.find('dateEcheance').text()).end()
					.find('.interet').attr('checked',$data.find('interet').text()=='true').end()
					.find('.montantInteret').val($data.find('montantInteret').text()).change().end()
					.find('.dessaisissementFortune').attr('checked',$data.find('DF').text()=='true').end()
					.find('.dessaisissementRevenu').attr('checked',$data.find('DR').text()=='true').end()
					.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
					.find('[name=dateFin]').val($data.find('dateFin').text()).end();
		this.addSpy($data);
	};
	
	this.getParametres=function($data){
		return {
			'pretEnversTiers.simplePretEnversTiers.csTypePropriete':this.detail.find('.typePropriete').val(),
			'part':this.detail.find('.part').val(),
			'pretEnversTiers.simplePretEnversTiers.typePret':this.detail.find('.typePret').val(),
			'pretEnversTiers.simplePretEnversTiers.montantPret':this.detail.find('.montant').val(),
			'pretEnversTiers.simplePretEnversTiers.nomPrenomBeneficiaire':this.detail.find('.beneficiaire').val(),
			'pretEnversTiers.simplePretEnversTiers.dateEcheance':this.detail.find('[name=dateEcheance]').val(),
			'pretEnversTiers.simplePretEnversTiers.isSansInteret':this.detail.find('.interet').prop('checked'),
			'pretEnversTiers.simplePretEnversTiers.montantInteret':this.detail.find('.montantInteret').val(),
			'pretEnversTiers.simpleDonneeFinanciereHeader.isDessaisissementFortune':this.detail.find('.dessaisissementFortune').prop('checked'),
			'pretEnversTiers.simpleDonneeFinanciereHeader.isDessaisissementRevenu':this.detail.find('.dessaisissementRevenu').prop('checked'),
			'pretEnversTiers.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'pretEnversTiers.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};	
	
	this.clearFields=function(){
		 this.detail.find('.typePropriete,.part,.typePret,.montant,.beneficiaire,[name=dateEcheance],[name=dateDebut],[name=dateFin],.montantInteret').val('').end()
					.find('.dessaisissementFortune,.dessaisissementRevenu,.interet').attr('checked',false);		
		
	};
	
	this.getParentViewBean=function(){
		return droit;	
	};
	
	this.setParentViewBean=function(newViewBean){
		droit=newViewBean;
	};
	
	this.formatTableTd=function($elementTable){
		$elementTable.find('td:eq(4)').css('text-align','right').end();
	};
	
	// initialization
	this.init(
	    function(){	
			this.stopEdition();
			this.onAddTableEvent();
			this.colorTableRows(false);
		}
	);
}

PretEnversTiersPart.prototype=DonneeFinancierePart.prototype;


$(function(){	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new PretEnversTiersPart($(this));
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