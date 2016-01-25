/*
 * ECO
 */
/**
 * 
 * @return
 */
function PensionAlimentairePart(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX=ACTION_AJAX_PENSION_ALIMENTAIRE;
	this.mainContainer=container;
	this.table=this.mainContainer.find(".areaDFDataTable");
	this.detail=this.mainContainer.find(".areaDFDetail");
	this.titleContainer=this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass="areaDFModified";
	this.membreId=null;	
	
	// functions	
	this.afterRetrieve=function($data){
		
		var a=$data.find('pension').text()=='64031001';
		this.detail.find('[name=pension]').filter("[value='"+$data.find('pension').text()+"']").prop("checked",true);
			//this.detail.find('[name=pension]').prop("checked",$data.find('pension').text());
		
		 this.detail.find('.montantPension').val($data.find('montantPension').text()).end()
					.find('[name="motif"]').val($data.find('motif').text()).end()					
					.find('.idTiers').val($data.find('idTiers').text()).end()
					.find('.selecteurTiers').val($data.find('nomTiers').text()).end()
					.find('[name="lien"]').val($data.find('lien').text()).end()
					.find('.autres').val($data.find('autres').text()).end()
					.find('.deductionEnfant').attr('checked',$data.find('DRE').text()=='true').end()
					.find('[name="dateEcheance"]').val($data.find('dateEcheance').text()).end()
					.find('.dessaisissementRevenu').attr('checked',$data.find('DR').text()=='true').end()
					.find('[name="dateDebut"]').val($data.find('dateDebut').text()).end()
					.find('[name="dateFin"]').val($data.find('dateFin').text()).end()
		 			.find('[name="montantRenteEnfant"]').val($data.find('montantRenteEnfant').text()).end();
		 requirements(this.detail);
		 this.addSpy($data);
	};
	
	this.getParametres=function($data){
		return {
			'pensionAlimentaire.simplePensionAlimentaire.csTypePension':(this.detail.find('[name="pension"]')[0].checked ? "64031001" : "64031002"),
			'pensionAlimentaire.simplePensionAlimentaire.montantPensionAlimentaire':this.detail.find('.montantPension').val(),
			'pensionAlimentaire.simplePensionAlimentaire.csMotif':this.detail.find('[name="motif"]').val(),			
			'pensionAlimentaire.simplePensionAlimentaire.idTiers':this.detail.find('.idTiers').val(),
			'pensionAlimentaire.simplePensionAlimentaire.csLienAvecRequerantPC':this.detail.find('[name="lien"]').val(),
			'pensionAlimentaire.simplePensionAlimentaire.autreLienAvecRequerantPC':this.detail.find('.autres').val(),
			'pensionAlimentaire.simplePensionAlimentaire.isDeductionRenteEnfant':this.detail.find('.deductionEnfant').prop('checked'),
			'pensionAlimentaire.simplePensionAlimentaire.dateEcheance':this.detail.find('[name="dateEcheance"]').val(),
			'pensionAlimentaire.simpleDonneeFinanciereHeader.isDessaisissementRevenu':this.detail.find('.dessaisissementRevenu').prop('checked'),
			'pensionAlimentaire.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name="dateDebut"]').val(),
			'pensionAlimentaire.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name="dateFin"]').val(),
			'pensionAlimentaire.simplePensionAlimentaire.montantRenteEnfant':this.detail.find('[name="montantRenteEnfant"]').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};	
	
	this.clearFields=function(){
		 this.detail.find('[name=motif],.montantPension,.selecteurTiers,[name=lien],.autres,[name=dateEcheance],[name=dateDebut],[name=dateFin]').val('').end()
					.find('.dessaisissementRevenu','.deductionEnfant','[name=pension]').attr('checked',false);		
		
	};
	
	this.formatTableTd=function($elementTable){
		$elementTable.find('td:eq(9)').attr('noWrap','noWrap').css('text-align','left').end();
	};
	
	this.getParentViewBean=function(){
		return droit;	
	};
	
	this.setParentViewBean=function(newViewBean){
		droit=newViewBean;
	}
	this.start = function(){
		
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

PensionAlimentairePart.prototype=DonneeFinancierePart.prototype;


function requirements(detail){
	
}

$(function(){	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new PensionAlimentairePart($(this));
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
			$that.find(".areaDFDetail").find('[name=pension]').eq(0).attr('checked',true).change();
				zone.stopEdition();
				zone.startEdition();			
				requirements($that.find(".areaDFDetail"));
		}).end()		
		.find('.btnAjaxValidateNouvellePeriode').click(function(){
			zone.doAddPeriode=true;
			zone.validateEdition();
		}).end();	
		
	});
	
	
});