/*
 * FHA
 */
/**
 * 
 * @return
 */
function BienImmobilierServantHabitationPrincipalePart(container) {
	// variables
	var that = this;

	this.ACTION_AJAX = ACTION_AJAX_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE;
	this.mainContainer = container;
	this.table = this.mainContainer.find(".areaDFDataTable");
	this.detail = this.mainContainer.find(".areaDFDetail");
	this.titleContainer = this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass = "areaDFModified";
	this.membreId = null;

	// functions
	this.afterRetrieve = function($data) {
		this.detail.find('.typePropriete').val(
				$data.find('csTypePropriete').text()).end().find('.part').val($data.find('part').text()).end()
				.find('.typeBien').val($data.find('csTypeBien').text()).end()
				.find('.isConstructionMoinsDixAns').attr('checked', $data.find('isConstructionMoinsDixAns').text()=='true').end()
				.find('.isConstructionPlusVingtAns').attr('checked', $data.find('isConstructionPlusVingtAns').text()=='true').end()
				.find('.selecteurCommune').val($data.find('nomCommune').text()).end()
				.find('.autres').val(
				$data.find('autres').text()).end().find('.commune').val(
				$data.find('commune').text()).end().find('.selecteurCommune')
				.val($data.find('nomCommune').text()).end().find(
						'.numeroFeuillet').val(
						$data.find('numeroFeuillet').text()).end().find(
						'.nombrePersonnes').val(
						$data.find('nombrePersonne').text()).end().find(
						'.valeurLocative').val(
						$data.find('valeurLocative').text()).change().end()
				.find('.valeurFiscale').val($data.find('valeurFiscale').text())
				.change().end().find('.dette').val(
						$data.find('detteHypothecaire').text()).change().end()
				.find('.interets')
				.val($data.find('interetHypothecaire').text()).change().end()
				.find('.noHypotheque').val($data.find('numeroHypothecaire').text())
				.change().end().find('.compagnie').val(
						$data.find('compagnie').text()).change().end().find(
						'.selecteurCompagnie').val(
						$data.find('nomcompagnie').text()).change().end().find(
						'.loyersEncaisses').val(
						$data.find('loyersEncaisses').text()).change().end()
				.find('.sousLocation').val($data.find('sousLocation').text())
				.change().end().find('.dessaisissementFortune').attr('checked',
						$data.find('DF').text() == 'true').end().find(
						'.dessaisissementRevenus').attr('checked',
						$data.find('DR').text() == 'true').end().find(
						'[name=dateDebut]').val($data.find('dateDebut').text())
				.end().find('[name=dateFin]').val($data.find('dateFin').text())
				.end();
			this.detail.find('.lblCompagnie').text($data.find('nomCompagnie').text());
		//requirements(this.detail);
		this.displayAutre();
		this.displayPart(false);
		//this.supperAfterRetrieve($data);
	};

	this.getParametres = function($data) {
		return {
			'bienImmobilierServantHabitationPrincipale.simpleBienImmobilierServantHabitationPrincipale.csTypePropriete' : this.detail
					.find('.typePropriete').val(),
			'part' : this.detail.find('.part').val(),
			
			'bienImmobilierServantHabitationPrincipale.simpleBienImmobilierServantHabitationPrincipale.csTypeBien' : this.detail
					.find('.typeBien').val(),
					
			'bienImmobilierServantHabitationPrincipale.simpleBienImmobilierServantHabitationPrincipale.isConstructionMoinsDixAns' : this.detail
					.find('.isConstructionMoinsDixAns').prop('checked'),

			'bienImmobilierServantHabitationPrincipale.simpleBienImmobilierServantHabitationPrincipale.isConstructionPlusVingtAns' : this.detail
					.find('.isConstructionPlusVingtAns').prop('checked'),
					
			'bienImmobilierServantHabitationPrincipale.simpleBienImmobilierServantHabitationPrincipale.autresTypeBien' : this.detail
					.find('.autres').val(),
					
			'bienImmobilierServantHabitationPrincipale.simpleBienImmobilierServantHabitationPrincipale.idCommuneDuBien' : this.detail
					.find('.commune').val(),
			'bienImmobilierServantHabitationPrincipale.simpleBienImmobilierServantHabitationPrincipale.noFeuillet' : this.detail
					.find('.numeroFeuillet').val(),
			'bienImmobilierServantHabitationPrincipale.simpleBienImmobilierServantHabitationPrincipale.nombrePersonnes' : this.detail
					.find('.nombrePersonnes').val(),
			'bienImmobilierServantHabitationPrincipale.simpleBienImmobilierServantHabitationPrincipale.montantValeurLocative' : this.detail
					.find('.valeurLocative').val(),
			'bienImmobilierServantHabitationPrincipale.simpleBienImmobilierServantHabitationPrincipale.montantValeurFiscale' : this.detail
					.find('.valeurFiscale').val(),
			'bienImmobilierServantHabitationPrincipale.simpleBienImmobilierServantHabitationPrincipale.montantDetteHypothecaire' : this.detail
					.find('.dette').val(),
			'bienImmobilierServantHabitationPrincipale.simpleBienImmobilierServantHabitationPrincipale.montantInteretHypothecaire' : this.detail
					.find('.interets').val(),
			'bienImmobilierServantHabitationPrincipale.simpleBienImmobilierServantHabitationPrincipale.noHypotheque' : this.detail
					.find('.noHypotheque').val(),
			'bienImmobilierServantHabitationPrincipale.simpleBienImmobilierServantHabitationPrincipale.nomCompagnie' : this.detail
					.find('.compagnie').val(),
			'bienImmobilierServantHabitationPrincipale.simpleBienImmobilierServantHabitationPrincipale.montantLoyesEncaisses' : this.detail
					.find('.loyersEncaisses').val(),
			'bienImmobilierServantHabitationPrincipale.simpleBienImmobilierServantHabitationPrincipale.montantSousLocation' : this.detail
					.find('.sousLocation').val(),
			'bienImmobilierServantHabitationPrincipale.simpleDonneeFinanciereHeader.isDessaisissementFortune' : this.detail
					.find('.dessaisissementFortune').prop('checked'),
			'bienImmobilierServantHabitationPrincipale.simpleDonneeFinanciereHeader.isDessaisissementRevenu' : this.detail
					.find('.dessaisissementRevenus').prop('checked'),
			'bienImmobilierServantHabitationPrincipale.simpleDonneeFinanciereHeader.dateDebut' : this.detail
					.find('[name=dateDebut]').val(),
			'bienImmobilierServantHabitationPrincipale.simpleDonneeFinanciereHeader.dateFin' : this.detail
					.find('[name=dateFin]').val(),
			'doAddPeriode' : this.doAddPeriode,
			'idDroitMembreFamille' : this.membreId
		};
	};

	this.clearFields = function() {
		this.detail
				.find(
						'.typePropriete,.dette,.typeBien,.autres,.commune,.selecteurCommune,.compagnie,.selecteurCompagnie,.part,.numeroFeuillet,.nombrePersonnes,.valeurLocative,.valeurFiscale,.detteHypothecaire,.interets,.numeroHypothecaire,.sousLocation,.noHypotheque,.loyersEncaisses,[name=dateDebut],[name=dateFin]')
				.val('').end().find(
						'.dessaisissementFortune,.dessaisissementRevenu').attr(
						'checked', false);
	};

	this.getParentViewBean = function() {
		return droit;
	};

	this.setParentViewBean = function(newViewBean) {
		droit = newViewBean;
	};

	this.formatTableTd=function($elementTable){
		$elementTable.find('td:eq(2),td:eq(17)').attr('noWrap', 'noWrap').end().
				   find('td:eq(2)').css('width','65px').end().
				   find('td:eq(17)').css('text-align', 'left');
	};
	
	this.displayAutre = function (){
		var value = (this.detail.find('.typeBien').val() );
		if (value == "64023005") {
			this.detail.find('.cacherAutres').show();
		} else {
			this.detail.find('.cacherAutres').hide();
		}
	}
	
	this.displayPart = function (deflaut){
		var value = (this.detail.find('.typePropriete').val());
		if (value == "64009004") {
			this.detail.find('.part').attr("readonly", true);
			this.detail.find('.part').css("color", "red");
			if (deflaut) {
				this.detail.find('.part').val("1/1");
			}
		} else {
			this.detail.find('.part').attr("readonly", false);
			this.detail.find('.part').css("color", "black");
		}
	}
	
	this.addEvent= function () {
	    var $that = this;
		this.displayAutre();
		this.displayPart(true);
		this.detail.find('.typeBien').change( function() {
			$that.displayAutre();
		});
		this.detail.find('.typePropriete').change( function() {
			$that.displayPart(false);
		});
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

BienImmobilierServantHabitationPrincipalePart.prototype = DonneeFinancierePart.prototype;

function requirements(detail){
	 detail.find('.typePropriete').change(function() {
			var value=(detail.find(this).attr("value"));		
			
			if(value=="64009004") 
			{ 				
				detail.find('.part').attr("readonly",true);		
				detail.find('.part').css("color","red");
				detail.find('.part').val("1/1");
			}	
			else{ 				
				detail.find('.part').attr("readonly",false);
				detail.find('.part').css("color","black");
			}	
					
	});	
	 
	 
	 if(detail.find('.typePropriete').val()=="64009004"){
		 detail.find('.part').attr("readonly",true);	
		 detail.find('.part').css("color","red");
	 }
	 else{	 
		 detail.find('.part').attr("readonly",false);
		 detail.find('.part').css("color","black");
	 }
	  	 
}
$( function() {
	$('.areaMembre').each( function() {
		var $that = $(this);
		var zone = new BienImmobilierServantHabitationPrincipalePart($that);
		this.zone = zone;
		zone.membreId = $(this).attr('idMembre');
		zone.addEvent();
		$(this).find('.btnAjaxUpdate').click( function() {
			zone.startEdition();
			$that.find('.btnAjaxValidateNouvellePeriode').show();
		}).end().find('.btnAjaxCancel').click( function() {
			zone.stopEdition();
		}).end().find('.btnAjaxValidate').click( function() {
			zone.validateEdition();
		}).end().find('.btnAjaxDelete').click( function() {
			zone.ajaxDeleteEntity(zone.selectedEntityId);
		}).end().find('.btnAjaxAdd').click( function() {
			zone.stopEdition();
			zone.startEdition();
			$('.part').val("1/1");
			requirements($that.find(".areaDFDetail"));
		}).end().find('.btnAjaxValidateNouvellePeriode').click( function() {
			zone.doAddPeriode = true;
			zone.validateEdition();
		}).end();

	});
});



