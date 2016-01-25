/**
 * @author DMA
 */
function IjApgPart(container){
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
		 this.detail.find('[name=csGenrePrestation]').val($data.find('csGenrePrestation').text()).end()
					.find('.autreGenrePresation').val($data.find('autreGenrePresation').text()).end()
					.find('.montantIj').val($data.find('montant').text()).change().end() 
					.find('.nomFournisseur').val($data.find('nomFournisseurPrestation').text()).end()
					.find('.fournisseurPrestation').val($data.find('fournisseurPrestation').text()).end()
					.find('.dessaisissementRevenu').attr('checked',$data.find('DR').text()=='true').end()
					.find('.montantBrutAC').val($data.find('montantBrutAC').text()).end()
					.find('.nbJours').val($data.find('nbJours').text()).end()
					.find('.tauxAVS').val($data.find('tauxAVS').text()).end()
					.find('.tauxAA').val($data.find('tauxAA').text()).end()
					.find('.cotisationLPPMens').val($data.find('cotisationLPPMens').text()).end()
					.find('.gainIntAnnuel').val($data.find('gainIntAnnuel').text()).end()
					.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
					.find('[name=dateFin]').val($data.find('dateFin').text()).end();
		//this.showOrHideDetail();
		 this.addSpy($data);
  };
	
  
	this.getParametres=function($data){
		return {
			'ijApg.simpleIjApg.csGenrePrestation':this.detail.find('[name=csGenrePrestation]').val(),
			'ijApg.simpleIjApg.autreGenrePresation':this.detail.find('.autreGenrePresation').val(),
			'ijApg.simpleIjApg.montant':this.detail.find('.montantIj').val(),
			'ijApg.simpleIjApg.idFournisseurPrestation':this.detail.find('.fournisseurPrestation').val(),
			'ijApg.simpleDonneeFinanciereHeader.isDessaisissementRevenu':this.detail.find('.dessaisissementRevenu').prop('checked'),
			'ijApg.simpleIjApg.montantBrutAC':this.detail.find('.montantBrutAC').val(),
			'ijApg.simpleIjApg.nbJours':this.detail.find('.nbJours').val(),
			'ijApg.simpleIjApg.tauxAVS':this.detail.find('.tauxAVS').val(),
			'ijApg.simpleIjApg.tauxAA':this.detail.find('.tauxAA').val(),
			'ijApg.simpleIjApg.cotisationLPPMens':this.detail.find('.cotisationLPPMens').val(),
			'ijApg.simpleIjApg.gainIntAnnuel':this.detail.find('.gainIntAnnuel').val(),
			'ijApg.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'ijApg.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};	
	
	this.clearFields=function(){
		 this.detail.clearInputForm();				
	};
	
	this.getParentViewBean=function(){
		return droit;	
	};
	
	this.setParentViewBean=function(newViewBean){
		droit=newViewBean;
	}

	this.formatTableTd=function($elementTable){
		$elementTable.find('td:eq(2)').css('text-align','right').end();
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

IjApgPart.prototype=DonneeFinancierePart.prototype;


$(function(){	
	//var start = (new Date()).getTime();
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new IjApgPart($that);
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
		}).end()
		//Gestion liste choix type IJ, raz du champ
		.find('[name=csGenrePrestation]').change(function () {
			if($('.ijChomage').is(':visible')){
					$('.montantIj').val('');
				}else{
					$('.montantBrutAc').val('');
				}
		}).end()
		//Hack style champ cotisations longueur
		.find('.cotisationLPPMens').css('width','4.2cm').end();
		
	});
});