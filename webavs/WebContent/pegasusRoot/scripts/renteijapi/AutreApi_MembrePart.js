/*
 * ECO
 */

/**
 * 
 * @return
 */
function AutreApi(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX = ACTION_DROIT_AUTRES_API_AJAX;
	this.mainContainer = container;
	this.table = this.mainContainer.find(".areaDFDataTable");
	this.detail = this.mainContainer.find(".areaDFDetail");
	this.titleContainer = this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass = "areaDFModified";
	this.membreId=null;
	
	// functions	
	this.afterRetrieve=function($data){
		 this.detail.find('[name=csDegre]').val($data.find('csDegre').text()).end()
					.find('[name=csTypeApi]').val($data.find('csType').text()).end()
					.find('[name=csDegre]').val($data.find('csGenre').text()).end()
					.find('.montant').val($data.find('montant').text()).change().end()
					.find('.autreTypeApi').val($data.find('autre').text()).change().end()
					.find('.dessaisissementRevenu').attr('checked',$data.find('DR').text()=='true').end()
					.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
					.find('[name=dateFin]').val($data.find('dateFin').text()).end();
		this.showOrHideDetail();
		this.addSpy($data);
	};
	
	this.getParametres=function($data){
		return {
			'autreApi.simpleAutreApi.csDegre': this.detail.find('[name=csDegre]').val(),
			'autreApi.simpleAutreApi.csType' : this.detail.find('[name=csTypeApi]').val(),
			'autreApi.simpleAutreApi.csGenre': this.detail.find('[name=csGenre]').val(),
			'autreApi.simpleAutreApi.montant': this.detail.find('.montant').val(),
			'autreApi.simpleAutreApi.autre'  : this.detail.find('.autreTypeApi').val(),
			'autreApi.simpleDonneeFinanciereHeader.isDessaisissementRevenu':this.detail.find('.dessaisissementRevenu').prop('checked'),
			'autreApi.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'autreApi.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};	
	
	this.clearFields=function(){
		 this.detail.find('.csDegre,.csType,.csGenre,.montant,.autreTypeApi,[name=dateDebut],[name=dateFin]').val('').end()
					.find('.dessaisissementRevenu').attr('checked',false);		
		
	};
	
	this.getParentViewBean=function(){
		return droit;	
	};
	
	this.setParentViewBean=function(newViewBean){
		droit=newViewBean;
	};
	
	this.formatTableTd=function($elementTable){
		$elementTable.find('td:eq(1)').css('text-align','right').end();
	};
	
	// initialization
	this.init(
	    function(){	
			this.stopEdition();
			this.onAddTableEvent();
			this.colorTableRows(false);
		}
	);
	
	this.showOrHideDetail = function(){
		var value =this.detail.find('[name=csTypeApi]').val();
		var autreToHidden = this.detail.find('.autreToHidden');
			(CS_TYPE_AUTRE == value)?autreToHidden.show():autreToHidden.hide();
	}
}

AutreApi.prototype=DonneeFinancierePart.prototype;


$(function(){	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new AutreApi($that);
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
		.find('.[name=csTypeApi]').change(function(){
			zone.showOrHideDetail();
		}).each(function (){
			zone.showOrHideDetail();
		}).end()
		.find('.btnAjaxValidateNouvellePeriode').click(function(){
			zone.doAddPeriode=true;
			zone.validateEdition();
		}).end();
	});
});