/*
 * FHA
 */


/**
 * 
 * @return
 */
function RevenuHypothetiquePart(container){
	
	// variables
	var that=this;
	
	this.ACTION_AJAX=ACTION_AJAX_REVENU_HYPOTHETIQUE;
	this.mainContainer=container;
	this.table=this.mainContainer.find(".areaDFDataTable");
	this.detail=this.mainContainer.find(".areaDFDetail");
	this.titleContainer=this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass="areaDFModified";
	this.membreId=null;
	
	// functions	
	this.afterRetrieve=function($data){								
		
		 this.detail.find('.revenuNet').val($data.find('revenuNet').text()).end()
					.find('.revenuBrut').val($data.find('revenuBrut').text()).end()
					.find('.deductionsSociales').val($data.find('deductionSociales').text()).end()
					.find('.deductionLPP').val($data.find('deductionLPP').text()).end()
					.find('.fraisDeGarde').val($data.find('fraisDeGarde').text()).end()
					.find('[name=motif]').val($data.find('csMotif').text()).end()
					.find('.autres').val($data.find('autres').text()).end()
					.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
					.find('[name=dateFin]').val($data.find('dateFin').text()).end();
		 this.setValueRadio('.autreBrutOuNet', $data.find('autreBrutOuNet').text());
		 this.addSpy($data);
	};
	
	this.setValueRadio = function(s_idendifiant,s_value){
		var $radios = this.detail.find('.autreBrutOuNet');
		if (s_value!=""){
			$radios.filter('[value='+s_value+']').attr('checked',true);
		}
	};
	
	this.getParametres=function($data){		
		return {
			'revenuHypothetique.simpleRevenuHypothetique.montantRevenuHypothetiqueNet':this.detail.find('.revenuNet').val(),			
			'revenuHypothetique.simpleRevenuHypothetique.montantRevenuHypothetiqueBrut':this.detail.find('.revenuBrut').val(),
			'revenuHypothetique.simpleRevenuHypothetique.deductionsSociales':this.detail.find('.deductionsSociales').val(),
			'revenuHypothetique.simpleRevenuHypothetique.deductionLPP':this.detail.find('.deductionLPP').val(),
			'revenuHypothetique.simpleRevenuHypothetique.fraisDeGarde':this.detail.find('.fraisDeGarde').val(),
			'revenuHypothetique.simpleRevenuHypothetique.csMotif':this.detail.find('[name=motif]').val(),
			'revenuHypothetique.simpleRevenuHypothetique.autreMotif':this.detail.find('.autres').val(),
			'autreBrutOuNet':this.detail.find('.autreBrutOuNet:checked').val(),
			'revenuHypothetique.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'revenuHypothetique.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};	
	
	this.clearFields=function(){
		// this.detail.find('.revenuNet,.revenuBrut,.deductionsSociales,.deductionLPP,.fraisDeGarde,[name=motif],[name=dateDebut],.autres,[name=dateFin]').val('').end();
		this.detail.clearInputForm();	
	};
	
	// Formatatage de la table
	this.formatTableTd=function($elementTable){
		
	};
	
	this.getParentViewBean=function(){
		return droit;	
	};
	
	this.setParentViewBean=function(newViewBean){
		droit=newViewBean;
	};
	
	this.radioAutr = function (){
		var $radios = this.detail.find('.autreBrutOuNet'),
			$cacherBrut =  this.detail.find('.cacherBrut'),
			$cacherNet =  this.detail.find('.cacherNet');
		$radios.change(function(){
			if($radios.filter(':checked').val()=='BRUT'){
				$cacherBrut.show();
				$cacherNet.hide();
				$cacherNet.clearInputForm();
			}else{
				$cacherNet.show();
				$cacherBrut.hide();
				$cacherBrut.clearInputForm();
			}
		})
	}
	
	
	// initialization
	this.init(
		    function(){	
				this.stopEdition();
				this.onAddTableEvent();
				this.colorTableRows(false);
				this.radioAutr();
			}
		);
	
	/*
	 * data-g-commutator="context:$(this).parents('.areaDFDetail'),
										 					   master:context.find('.autreBrutOuNet'),
										 		               condition:context.find('.autreBrutOuNet:checked').val()!='BRUT',
										 		               actionTrue:¦show(context.find('.cacherBrut'))¦,
										 		               actionFalse:¦hide(context.find('.cacherNet'))¦"*/

}

RevenuHypothetiquePart.prototype=DonneeFinancierePart.prototype;

$(function(){	

	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new RevenuHypothetiquePart($(this));
		this.zone=zone;
		zone.membreId=$(this).attr('idMembre');					
		
		$that.find(".areaDFDetail").find('.motif').change(function() {
		});		
		
		$(this).find('.btnAjaxUpdate').click(function(){
				zone.startEdition();
				$that.find('.btnAjaxValidateNouvellePeriode').show();
		}).end()
		.find('.btnAjaxCancel').click(function(){
			zone.stopEdition();
		}).end()
		.find('.btnAjaxValidate').click(function(){
			zone.validateEditionV2();
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
			zone.validateEditionV2();
		}).end();	
	});
	
});