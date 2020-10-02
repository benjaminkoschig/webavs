

function SubsideAssuranceMaladiePart(container){
	// variables
	this.ACTION_AJAX=ACTION_AJAX;
	this.mainContainer=container;
	this.table=this.mainContainer.find(".areaDFDataTable");
	this.detail=this.mainContainer.find(".areaDFDetail");
	this.titleContainer=this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass="areaDFModified";
	this.membreId=null;

	// functions
	
	this.afterRetrieve=function($data){
		this.detail
				.find('.montant').val($data.find('montant').text()).end()
				.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
				.find('[name=dateFin]').val($data.find('dateFin').text()).end();
		this.addSpy($data);
	};
			
	this.getParametres=function(){
		return {
			"subsideAssuranceMaladie.simpleSubsideAssuranceMaladie.montant":this.detail.find('.montant').val(),
			'subsideAssuranceMaladie.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'subsideAssuranceMaladie.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};

	this.clearFields=function(){
		this.detail.find('.montant,[name=dateDebut],[name=dateFin]').val('').end();
	};

	this.formatTableTd=function($elementTable){
		$elementTable.find('td:eq(1)').css('text-align','left').end();
	}

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

SubsideAssuranceMaladiePart.prototype=DonneeFinancierePart.prototype;


$(function(){
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new SubsideAssuranceMaladiePart($(this));
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
		}).end()
			.find('.btnAjaxValidateNouvellePeriode').click(function(){
			zone.doAddPeriode=true;
			zone.validateEdition();
		}).end();

	});
});
