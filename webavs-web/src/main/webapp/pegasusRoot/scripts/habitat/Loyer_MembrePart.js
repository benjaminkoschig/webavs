
function Loyer(container){
	// variables
	var that=this;
	
	this.ACTION_AJAX = ACTION_AJAX_DONNEE_FINANCIERE;
	this.mainContainer = container;
	this.table = this.mainContainer.find(".areaDFDataTable");
	this.detail = this.mainContainer.find(".areaDFDetail");
	this.titleContainer = this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass = "areaDFModified";
	this.membreId=null;
	var b_isMainChangeExecuted = false;
	var $listeCsMotifChangement = this.mainContainer.find('[name=csMotifChangementLoyer]');
	var $_csMotifBail = $listeCsMotifChangement.find('option[value='+CS_MOTIF_CHANGEMENT_BAIL+']');
	var $_csMotifValLoc = $listeCsMotifChangement.find('option[value='+CS_MOTIF_CHANGEMENT_VALEUR_LOCATIVE+']');

	// functions	
	this.afterRetrieve=function($data){
		 this.detail.find('[name=csTypeLoyer]').val($data.find('csTypeLoyer').text()).end()
					.find('.nbPersonnes').val($data.find('nbPersonnes').text()).end()
					.find('.montantLoyerNet').val($data.find('montantLoyerNet').text()).end()
					.find('.montantLoyerAnulle').val($data.find('montantLoyerNet').text()).change().end()
					.find('.montantCharges').val($data.find('montantCharges').text()).change().end()
					.find('.idBailleurRegie').val($data.find('idBailleurRegie').text()).change().end()
					.find('.isFauteuilRoulant').attr('checked',$data.find('isFauteuilRoulant').text()=='true').end()
					.find('[name=csMotifChangementLoyer]').val($data.find('csMotifChangementLoyer').text()).change().end()
					.find('.revenuSousLocation').val($data.find('revenuSousLocation').text()).change().end()
					.find('.fraisPlacementEnfant').val($data.find('fraisPlacementEnfant').text()).change().end()
					.find('.nomBailleurRegie').val($data.find('nomBailleurRegie').text()).change().end()
					.find('.isTenueMenage').attr('checked',$data.find('isTenueMenage').text()=='true').end()
					.find('.pensionNonReconnue').val($data.find('pensionNonReconnue').text()).change().end()
					.find('.taxeJournalierePensionNonReconnue').val($data.find('taxeJournalierePensionNonReconnue').text()).change().end()
					.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
					.find('[name=dateFin]').val($data.find('dateFin').text()).end()
					.find('.selecteurCommune').val($data.find('nomCommune').text()).end()
					.find('.commune').val($data.find('commune').text()).end()
					.find('.textLibre').val($data.find('textLibre').text()).end()
		 			.find('[name=csDeplafonnementAppartementPartage]').val($data.find('csDeplafonnementAppartementPartage').text()).change().end();
		 this.detail.find('.lblCompagnie').text($data.find('nomBailleurRegie').text())
			 .find('[name=csTypeLoyer]').val($data.find('csTypeLoyer').text()).end();
		//this.showOrHideDetail();
		this.checkIfAppartementProtegeAlreadySet($data.find('csDeplafonnementAppartementPartage').text());
		this.calculSurAn();
		b_isMainChangeExecuted = false;
	};
	
	 
	
	this.getParametres=function($data){
		var csTypeLoyer = this.detail.find('[name=csTypeLoyer]');
		var montantCharges = this.detail.find('.montantCharges');
		var csDeplafonnement = this.detail.find('[name=csDeplafonnementAppartementPartage]').val();
		(CS_LOYER_NET_AVEC_CHARGE != csTypeLoyer.val())?montantCharges.val(''):null;
		if(CS_VALEUR_LOCATIVE_CHEZ_PROPRIETAIRE == csTypeLoyer.val()) {
			this.detail.find('.montantLoyerNet').val('');
			this.detail.find('.taxeJournalierePensionNonReconnue').val('');
		} else if(CS_PENSION_NON_RECONNUE == csTypeLoyer.val()) {
			this.detail.find('.montantLoyerNet').val('');
			this.detail.find('.montantLoyerAnulle').val('');	
		} else {
			this.detail.find('.taxeJournalierePensionNonReconnue').val('');
			this.detail.find('.montantLoyerAnulle').val('');	
		}
		
		if(!this.detail.find(".isAppartementProtege").prop("checked")) {
			csDeplafonnement = '';
			this.detail.find(".isAppartementProtege").prop('checked', false);
		}
		else
		{
			this.detail.find(".isAppartementProtege").prop('checked', true);
		}
		
		return {
			'loyer.simpleLoyer.csTypeLoyer': this.detail.find('[name=csTypeLoyer]').val(),
			'loyer.simpleLoyer.nbPersonnes' : this.detail.find('.nbPersonnes').val(),
			'loyer.simpleLoyer.montantLoyerNet': (CS_VALEUR_LOCATIVE_CHEZ_PROPRIETAIRE!= csTypeLoyer.val())?this.detail.find('.montantLoyerNet').val():this.detail.find('.montantLoyerAnulle').val(),
			'loyer.simpleLoyer.montantCharges': this.detail.find('.montantCharges').val(),
			'loyer.simpleLoyer.idBailleurRegie'  : this.detail.find('.idBailleurRegie').val(),
			'loyer.simpleLoyer.isFauteuilRoulant' : this.detail.find('.isFauteuilRoulant').prop('checked'),
			'loyer.simpleLoyer.csMotifChangementLoyer'  : this.detail.find('[name=csMotifChangementLoyer]').val(),
			'loyer.simpleLoyer.revenuSousLocation'  : this.detail.find('.revenuSousLocation').val(),
			'loyer.simpleLoyer.fraisPlacementEnfant'  : this.detail.find('.fraisPlacementEnfant').val(),
			'loyer.simpleLoyer.isTenueMenage'  : this.detail.find('.isTenueMenage').prop('checked'),
			'loyer.simpleLoyer.pensionNonReconnue'  : this.detail.find('.pensionNonReconnue').val(),
			'loyer.simpleLoyer.taxeJournalierePensionNonReconnue'  : this.detail.find('.taxeJournalierePensionNonReconnue').val(),
			'loyer.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'loyer.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'loyer.simpleLoyer.csDeplafonnementAppartementPartage': csDeplafonnement,
			'loyer.simpleLoyer.idLocalite': this.detail.find('.commune').val(),
			'loyer.simpleLoyer.textLibre': this.detail.find('.textLibre').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
		
	};	
	
	this.clearFields = function(){
		this.detail.find('.totalAnne').text(" ");
		this.detail.clearInputForm();
	};
	
	this.getParentViewBean=function(){
		return droit;	
	};
	
	this.setParentViewBean=function(newViewBean){
		droit=newViewBean;
	};
	
	this.formatTableTd=function($elementTable){
		$elementTable.find('td:eq(3),td:eq(4),td:eq(8),td:eq(9)').css('text-align','right').end();
	};
	
	this.addCsTypeChangeEvent = function () {
		var that = this;
		
		that.detail.find('[name=csTypeLoyer]').change(function () {
			//that.detail.find('[name=csTypeLoyer]').val('0');
			
			if(b_isMainChangeExecuted){
								that.detail.find('.montantCharges').val(0);
				that.clearValueTotalLoyerFields();
				that.dealMapOptionsMotifChangement($(this).val(),null);
				
			}else{
			
				b_isMainChangeExecuted = true;
				that.dealMapOptionsMotifChangement($(this).val(),$listeCsMotifChangement.val());
			}
		});
		
		
	};
	
	// Gestion de la liste des motifs de changements en fonction du type de loyer
	this.dealMapOptionsMotifChangement = function (s_csTypeLoyer,s_motifChangement) {
				//Sy type loyer val loca cher prop, on cache le bail
		if(s_csTypeLoyer == CS_VALEUR_LOCATIVE_CHEZ_PROPRIETAIRE){
			var valToSelect = CS_MOTIF_CHANGEMENT_VALEUR_LOCATIVE;
			if(s_motifChangement!==null){
				valToSelect = s_motifChangement;
			}
			$listeCsMotifChangement.find('option[value='+CS_MOTIF_CHANGEMENT_BAIL+']').detach();
			$listeCsMotifChangement.append($_csMotifValLoc);
			$listeCsMotifChangement.find('option[value='+valToSelect+']').attr('selected','selected');
		}else{
			var valToSelect = CS_MOTIF_CHANGEMENT_BAIL;
			if(s_motifChangement!==null){
				valToSelect = s_motifChangement;
			}
			$listeCsMotifChangement.find('option[value='+CS_MOTIF_CHANGEMENT_VALEUR_LOCATIVE+']').detach();
			$listeCsMotifChangement.append($_csMotifBail);
			$listeCsMotifChangement.find('option[value='+valToSelect+']').attr('selected','selected');
		}
		
	};
	
	this.checkIfAppartementProtegeAlreadySet = function(val){
		if(val == '0' || val == null)
		{
			this.detail.find(".isAppartementProtege").prop('checked', false);
			this.detail.find('.nbPieces').hide();

		}
		else
		{
			this.detail.find(".isAppartementProtege").prop('checked', true);
			this.detail.find('.nbPieces').show();
		}
	}
	
	this.calculSurAn = function () {
		
		var csTypeLoyer = parseInt(this.detail.find('[name=csTypeLoyer]').val());
		var montantNet = parseFloat(this.detail.find('.montantLoyerNet').val().replace("'",""));  /*+ this.table.find('.montantLoyerNet').val()*/;
		var charge = parseFloat(this.detail.find('.montantCharges').val().replace("'",""));
		var total = 0;
		var appendToTotal = "";
	
		
		
		if  (!isNaN(charge) && csTypeLoyer!==CS_VALEUR_LOCATIVE_CHEZ_PROPRIETAIRE) {
			//charges forfaitaires
			if(csTypeLoyer===CS_LOYER_NET_AVEC_CHARGE_FORFAITAIRES){
				total = (charge + montantNet ) * 12;
				appendToTotal = " "+labelChargesFofaitaire;
			}else{
				total = (charge + montantNet ) * 12;
			}
			
		}else if(csTypeLoyer===CS_VALEUR_LOCATIVE_CHEZ_PROPRIETAIRE){
			total = montantNet;
		}
		else{
			total = (montantNet)*12;
		}
		total = globazNotation.utilsFormatter.formatStringToAmout(total,2,false);
		this.detail.find('.totalAnne').text(total + appendToTotal);
	};
	
	this.clearValueTotalLoyerFields = function () {
		this.detail.find('.totalAnne').text(" ");
		this.detail.find('.montantLoyerNet').val(" ");
	};
	
	//changement dans la liste des motifs en fonction des types de loyer
	this.updateMotifChangementList = function () {
		
	};
	this.addEnvent = function(){
		var that = this;
		this.detail.find('.montantLoyerNet,.montantCharges').keyup(function(){
			that.calculSurAn();
		});
		
	};
	
	
	this.checkIsAppartementProtege= function () {
	    if(this.detail.find(".isAppartementProtege").prop("checked")) {
	    	this.detail.find('.nbPieces').show();
	    } else{
	    	this.detail.find('.nbPieces').hide();
    	}
	};
	
	this.showOrHideDetail = function(){
		var value = this.detail.find('[name=csTypeLoyer]').val();
		var valeurLocative = this.detail.find('.valeurLocative');
		var charge = this.detail.find('.charge');
		var loyer = this.detail.find('.loyer');
		if(CS_VALEUR_LOCATIVE_CHEZ_PROPRIETAIRE == value){		
			valeurLocative.show();
			loyer.hide(); 
		}else{
			loyer.show();
			valeurLocative.hide();
		}
		(CS_LOYER_NET_AVEC_CHARGE == value)?charge.show():charge.hide();
	};
	//initialization
	this.init(
		    function(){	
				this.stopEdition();
				this.onAddTableEvent();
				this.colorTableRows(false);
				this.addCsTypeChangeEvent();
				this.detail.find(".isAppartementProtege").change(function() {
					that.checkIsAppartementProtege();
				});
			}
		);
}




$(function(){	
Loyer.prototype=DonneeFinancierePart.prototype;
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new Loyer($that);
		this.zone=zone;
		zone.membreId=$(this).attr('idMembre');
		zone.addEnvent();
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
		.find('[name=csTypeLoyer]').change(function(){
			
		}).each(function (){
			//zone.showOrHideDetail();
		}).end()
		.find('.btnAjaxValidateNouvellePeriode').click(function(){
			zone.doAddPeriode=true;
			zone.validateEdition();
		}).end();
	});
});