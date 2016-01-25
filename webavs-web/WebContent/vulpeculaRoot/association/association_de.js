//fonctions de bases à redéfinir

function add () {
}

function upd() {
}

function validate() {
	if(notationManager.validateAndDisplayError()) {
		globazGlobal.associations.save();
	}
}

function cancel() {
     document.forms[0].elements('userAction').value="back";
     document.forms[0].elements('tab').value="associations";
}

function del() {
}

function init(){
}

function postInit() {
	$('.associationHeader').attr('disabled','disabled');
}

$(function () {
	globazGlobal.associations.bindEvents();
});

globazGlobal.associations = (function() {
	function bindEvents() {
		var $associations = $('#associations');
		
		//Lors du clic sur le bouton d'ajout d'une association, on va copier le template présent
		//dans le page JSP.
		$('#addAssociation').click(function () {
			var nouvelleAssociation = $($('#nouvelleAssociation').html());
			$associations.append(nouvelleAssociation);
		});
		
		$associations.on('click','.deleteAssociation', function() {
			$(this).closest('.association').remove();;
		});
		
		// Lorsque l'on supprime une cotisation, on vérifie que ce ne soit pas la dernière.
		// Dans le cas où c'est la dernière, on réactive la sélection d'une association et du genre de cotisation.
		$associations.on('click','.deleteCotisation', function() {
			var $cotisationMembre = $(this).closest('.cotisationMembre');
			var $association = $cotisationMembre.closest('.association');
			
			$cotisationMembre.remove();
			
			var nbCotisations = $association.find('.cotisationMembre').length;
			if(nbCotisations==0) {
				$association.find('.associationHeader').removeAttr('disabled');
			}			
		});
		
		// Lorsque l'on clic sur le bouton OK, on va rechercher toutes les cotisations pour cette assurance
		// et ce genr de cotisations. Une entrée est ensuite créée pour chacune d'elle
		$associations.on('click','.btnSearchCotisations', function() {
			var $association = $(this).closest('.association');
			createAllCotisations($association);
		});
		
		$associations.on('click','.addCotisation',function() {
			$association = $(this).closest('.association');
			addCotisation($association);
		});
		
		$associations.on('change','.periodeDebut', function() {
			var $this = $(this);
			var periodeDebut = $this.val();
			var $periodesDebuts = $(this).closest('.association').find('.periodeDebut');
			setValueToInputs($periodesDebuts, periodeDebut);
		});
		
		$associations.on('change','.periodeFin', function() {
			var $this = $(this);
			var periodeFin = $this.val();
			var $periodesFin = $(this).closest('.association').find('.periodeFin');
			setValueToInputs($periodesFin, periodeFin);
		});
	}
	
	function setValueToInputs($inputs, value) {
		if(value.length==0) {
			return;
		}
		for(var i=0;i<$inputs.length;i++) {
			var $input = $($inputs[i]);
			if(isEmpty($input)) {
				$input.val(value);
				$input.removeClass('isValueKo');
			}
		}		
	}
	
	function isEmpty(element) {
		if(element.val().length===0) {
			return true;
		}
		return false;
	}
	
	function createAllCotisations(association) {
		var genre = association.find('.genre').val();
		var idAssociation = association.find('.associationProfessionnelle').val();	
		findCotisationsAssociationsProfessionnelles(idAssociation, genre, function(cotisations) {
			for(var i=0;i<cotisations.length;i++) {
				creerNouvelleCotisation(association, genre, cotisations, i);
			}
		});
	}
	
	function addCotisation(association) {
		var genre = association.find('.genre').val();
		var idAssociation = association.find('.associationProfessionnelle').val();
		var $cotisations = $(association.find('.cotisationMembre'));
		var nbCotisations = $cotisations.length;
		
		//Si il existe déjà une cotisation, on reprend son taux de réduction de facture par défaut.
		if(nbCotisations > 0) {
			var $lastCotisation = $($cotisations.get(nbCotisations-1));
			reductionFacture = $lastCotisation.find('.reductionFacture').val();
		}
		
		findCotisationsAssociationsProfessionnelles(idAssociation, genre, function(cotisations) {
			if(cotisations.length>0) {
				creerNouvelleCotisation(association, genre, cotisations);
			} else {
				alert('Pas de cotisation disponible');
			}
		});
	}
	
	function creerNouvelleCotisation(association, genre, cotisations) {
		creerNouvelleCotisation(association, genre, cotisations);
	}
	
	function creerNouvelleCotisation(association, genre, cotisations, selectedId) {
		var reductionFacture = globazGlobal.reductionFactureDefaut;
		var $cotisations = association.find('.cotisations');
		var nouvelleCotisation = '<tr class="cotisationMembre">';
		nouvelleCotisation += '<td><button class="deleteCotisation"><img src="images/edit-delete.png" /></button></td>';
		nouvelleCotisation += '<td><select style="width:100%" class="idCotisation">';
		var nbCotisations = cotisations.length;
		for(var i=0;i<nbCotisations;i++) {
			var cotisation = cotisations[i];
			if(selectedId==i) {
				nouvelleCotisation += '<option selected="selected" value="'+cotisation.id+'">'+cotisation.libelle+'</option>';
			} else {
				nouvelleCotisation += '<option value="'+cotisation.id+'">'+cotisation.libelle+'</option>';
			}
		}
		nouvelleCotisation += '</select></td>';
		nouvelleCotisation += '<td><input class="periodeDebut" data-g-calendar="mandatory:true" /></td>';
		nouvelleCotisation += '<td><input class="periodeFin" data-g-calendar="" /></td>';
		nouvelleCotisation += '<td><input class="masseSalariale" type="text" data-g-amount=" " value="100.00" /></td>';
		nouvelleCotisation += '<td><input class="reductionFacture" type="text" data-g-amount=" " value="'+reductionFacture+'" /></td>';
		nouvelleCotisation += '</tr>';
		
		var $nouvelleCotisation = $(nouvelleCotisation); 
		notationManager.addNotationOnFragment($nouvelleCotisation);
		if(genre==globazGlobal.csNonTaxe) {
			var $masseSalariale = $nouvelleCotisation.find('.masseSalariale');
			$masseSalariale.attr('disabled','disabled');
			$masseSalariale.removeClass('isValueKo');
			$masseSalariale.val(globazNotation.utilsFormatter.formatStringToAmout(0));
		}
		$cotisations.append($nouvelleCotisation);
		
		//Désactivation des champs relatifs à l'association
		association.find('.associationHeader').attr('disabled','disabled');			
	}	
	
	function findCotisationsAssociationsProfessionnelles(idAssociation, genre, callback) {
		var options = {
				serviceClassName:globazGlobal.associationViewService,
				serviceMethodName:'findCotisationsAssociationsProfessionnelles',
				parametres:idAssociation + ',' + genre,
				callBack:callback
		};
		vulpeculaUtils.lancementService(options);	
	}

	function save() {
		var options = {
				serviceClassName:globazGlobal.associationViewService,
				serviceMethodName:'create',
				parametres:retrieve(),
				callBack:function (data) {
					if(data) {
						window.location.href = "vulpecula?userAction=back&tab=associations";
					}
				}
		};
		vulpeculaUtils.lancementService(options);		
	}

	function retrieve() {
		var associations = [];
		var result = {
				associations : associations,
				idEmployeur : globazGlobal.idEmployeur};
		
		var $associations = $('.association');
		var nbAssociations = $associations.length;
		for(var i=0;i<nbAssociations;i++) {
			var association = {};
			
			$association = $($associations[i]);
			association.associationProfessionnelle = $association.find('.associationProfessionnelle').val();
			association.genre = $association.find('.genre').val();
			association.cotisations = [];
			
			var $cotisations = $association.find('.cotisationMembre');
			var nbCotisations = $cotisations.length;
			for(var j=0;j<nbCotisations;j++) {
				cotisation = {};
				$cotisation = $($cotisations[j]);
				cotisation.id = $cotisation.find('.idCotisation').val();
				cotisation.periodeDebut = $cotisation.find('.periodeDebut').val();
				cotisation.periodeFin = $cotisation.find('.periodeFin').val();
				cotisation.masseSalariale = $cotisation.find('.masseSalariale').val();
				cotisation.reductionFacture = $cotisation.find('.reductionFacture').val();
				association.cotisations.push(cotisation);
			}
			associations.push(association);
		}
		return JSON.stringify(result);
	}

	return {
		bindEvents : bindEvents,
		save : save
	};
})();
