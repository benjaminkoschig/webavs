/**
 * @author DMA
 */
function AutreRentePart(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX = ACTION_AJAX_DONNEE_FINANCIERE;
	this.mainContainer = container;
	this.table = this.mainContainer.find(".areaDFDataTable");
	this.detail = this.mainContainer.find(".areaDFDetail");
	this.titleContainer = this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass = "areaDFModified";
	this.membreId=null; 
	
	// functions	
	this.afterRetrieve=function($data){
		 this.detail.find('.csGenre').val($data.find('csGenre').text()).end()
		    		.find('.csTypePc').val($data.find('csTypePc').text()).end()
					.find('.autreRente').val($data.find('autreGenre').text()).end()
					.find('.montant').val($data.find('montant').text()).change().end() 
					.find('.csType').val($data.find('csType').text()).change().end() 
					.find('.csMonnaie').val($data.find('monnaie').text()).change().end() 
					.find('.idPays').val($data.find('idPays').text()).change().end() 
					.find('.widgetPays').val($data.find('pays').text()).change().end() 
					.find('.fournisseurPrestation').val($data.find('fournisseurPrestation').text()).end()
					.find('[name=dateEcheance]').val($data.find('dateEcheance').text()).end()
					.find('.dessaisissementRevenu').attr('checked',$data.find('DR').text()=='true').end()
					.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
					.find('[name=dateFin]').val($data.find('dateFin').text()).end();
		 this.addSpy($data);
	};
	
	this.getParametres=function($data){
		return {
			'autreRente.simpleAutreRente.csGenre':this.detail.find('.csGenre').val(),
			'autreRente.simpleAutreRente.idPays':this.detail.find('.idPays').val(),
			'autreRente.simpleAutreRente.autreGenre':this.detail.find('.autreRente').val(),
			'autreRente.simpleAutreRente.csType':this.detail.find('.csType').val(),
			'autreRente.simpleAutreRente.montant':this.detail.find('.montant').val(),
			'autreRente.simpleAutreRente.csMonnaie':this.detail.find('.csMonnaie').val(),
			'autreRente.simpleAutreRente.fournisseurPrestation':this.detail.find('.fournisseurPrestation').val(),
			'autreRente.simpleAutreRente.dateEcheance':this.detail.find('[name=dateEcheance]').val(),
			'autreRente.simpleDonneeFinanciereHeader.isDessaisissementRevenu':this.detail.find('.dessaisissementRevenu').prop('checked'),
			'autreRente.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'autreRente.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};	
	
	this.clearFields=function(){
		/* this.detail.find('.fournisseurPestation,[name=csGenre],.autreRente,.montant,.typeRente,csGenre,#csMonnaie,.dateEchancefournisseurPestation,[name=dateEchance],[name=dateDebut],[name=dateFin]').val('').end()
					.find('.dessaisissementRevenu').attr('checked',false);		*/
		
		this.detail.clearInputForm();
		
	};
	
	this.getParentViewBean=function(){
		return droit;	
	};
	
	this.setParentViewBean=function(newViewBean){
		droit=newViewBean;
	}

	this.formatTableTd=function($elementTable){
		$elementTable.find('td:eq(3)').css('text-align','right');
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

AutreRentePart.prototype=DonneeFinancierePart.prototype;


$(function(){	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new AutreRentePart($(this));
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
		}).end()
		.find('.btnAjaxValidateNouvellePeriode').click(function(){
			zone.doAddPeriode=true;
			zone.validateEdition();
		}).end();
	});
});