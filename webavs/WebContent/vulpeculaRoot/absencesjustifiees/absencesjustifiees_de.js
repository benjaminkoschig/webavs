jsManager.executeAfter = function () {
	globazGlobal.calculAjoutSuppressionCotisations();
	$("#idPosteTravail").change(function () {
		globazGlobal.calculAjoutSuppressionCotisations();
	});
	if(globazGlobal.isNouveau) {
		globazGlobal.cotisations.init();
		globazGlobal.genrePrestations.init();
		globazGlobal.calcul.init();
		globazGlobal.decompteSalaire.init();
	} else {
		globazGlobal.calcul.calculTotalUnites();
		globazGlobal.genrePrestations.init();
		globazGlobal.loadNumeroCompte();
	}
};

function add () {
	document.forms[0].elements('userAction').value="vulpecula.absencesjustifiees.absencesjustifiees.afficher&_method=_add";
}

function upd() {
}

function validate() {
	var state = globazGlobal.validation.validate();
	 if (document.forms[0].elements('_method').value == "add") {
	     document.forms[0].elements('userAction').value="vulpecula.absencesjustifiees.absencesjustifiees.ajouter";
	 } else {
	     document.forms[0].elements('userAction').value="vulpecula.absencesjustifiees.absencesjustifiees.modifier";
	 }
	 return state;
}

function cancel() {
}

function del() {
	var message = jQuery.i18n.prop("ajax.deleteMessage");
	if(confirm(message)) {
		document.forms[0].elements('userAction').value="vulpecula.absencesjustifiees.absencesjustifiees.supprimer";
		document.forms[0].submit();
	}
}

function init(){
}

globazGlobal.calcul = (function () {
	var $nombreDeJours = null;
	var $salaireHoraire = null;	
	var $totalUnites = null;
	var $montantBrut = null;
	
	function calcul() {
		var nombreDeJours = getNombreDeJours();
		var nombreHeuresParJour = getNombreHeuresParJour();
		var salaireHoraire = getSalaireHoraire();
		
		var totalUnites = (nombreDeJours * nombreHeuresParJour).toFixed(2);
		var montantBrut = (totalUnites * salaireHoraire).toFixed(2); 
		
		$totalUnites.val(totalUnites);
		$montantBrut.val(globazNotation.utilsFormatter.formatStringToAmout(roundToFiveCentimes(montantBrut,2,true)));
		
		globazGlobal.cotisations.calcul();
	};
	
	function calculMontantBrut() {
		globazGlobal.cotisations.calcul();
	}
	
	function calculTotalUnites() {
		var $totalUnites = $('#totalUnites');
		var totalUnites = (getNombreDeJours() * getNombreHeuresParJour()).toFixed(2);
		
		$totalUnites.val(totalUnites);
		globazGlobal.cotisations.calcul();
	}
	
	function getNombreDeJours() {
		var $nombreDeJours = $('#nombreDeJours');
		return $nombreDeJours.val();
	}
	
	function getNombreHeuresParJour() {
		var $nombreHeuresParJour = $('#nombreHeuresParJour');
		return $nombreHeuresParJour.val();		
	}
	
	function getSalaireHoraire() {
		var $salaireHoraire = $('#salaireHoraire');
		return $salaireHoraire.val();		
	}
	
	return {
		init: function() {
			var that = this;
			$nombreDeJours = $('#nombreDeJours');
			$salaireHoraire = $('#salaireHoraire');	
			$totalUnites = $('#totalUnites');
			$montantBrut = $('#montantBrut');
			
			this.calculMontants();
			
			$nombreDeJours.change(function(){
				that.calculMontants();
			});
			
			$salaireHoraire.change(function() {
				that.calculMontants();
			});			
		},
		calculMontants : function() {
			calcul();
			calculMontantBrut();	
		},
		calculTotalUnites : calculTotalUnites
	};
})();

globazGlobal.genrePrestations = (function() {
	var $parente = null;
	var $genrePrestations = null;
	var $nbJours = null;
	
	function checkPrestationsValue() {
		if(globazGlobal.csDeuil==$genrePrestations.val()) {
			$parente.show();
		} else {
			$parente.hide();
		}			
	};
	
	function findNombreJourPourPrestationAJ() {
		var options = {
				serviceClassName:globazGlobal.prestationsViewService,
				serviceMethodName:'findNombreJourPourPrestationAJ',
				parametres:globazGlobal.getPosteTravail() + ',' + globazGlobal.$getDateDebutAbsence().val() + ',' + $genrePrestations.val() + ',' + $parente.val(),
				callBack:function (data) {
					$nbJours.val(globazNotation.utilsFormatter.formatStringToAmout(data,2,true));
					globazGlobal.calcul.calculMontants();
				}
		};
		vulpeculaUtils.lancementService(options);			
	}
	
	function tryToFindNombreJour() {
		if(globazGlobal.isDebutAbsenceSet()) {
			findNombreJourPourPrestationAJ();
		}
	}
	
	return {
		init : function() {
			$genrePrestations = $('#genrePrestations');
			$parente = $('#parente');
			$nbJours = $('#nombreDeJours');
			$debutAbsence = globazGlobal.$getDateDebutAbsence();
			
			$genrePrestations.change(function () {
				checkPrestationsValue();
				tryToFindNombreJour();
			});
			
			$parente.change(function () {
				tryToFindNombreJour();
			});
			
			$debutAbsence.change(function () {
				tryToFindNombreJour();
			});

			checkPrestationsValue();
		}
	};
})();

globazGlobal.notifyError = function(message) {
	$('#informations').noty({
		type: 'error',
		timeout: 3000,
		maxVisible : 3,
		timeout : false,
		text: message});
};

globazGlobal.isDebutAbsenceSet = function() {
	var anneeDebutAbsence = globazGlobal.$getDateDebutAbsence().val();
	return anneeDebutAbsence.length > 0;		
};

globazGlobal.decompteSalaire = (function(){
	var $debutAbsence = null;
	
	function isPeriodeContenueDansPoste(idPosteTravail, dateDebut, dateFin) {
		var dateDebutTime = getDateFromFormat(dateDebut,"dd.MM.yyyy");
		var dateFinTime = getDateFromFormat(dateFin,"dd.MM.yyyy");
		if(dateDebutTime==0 || dateFinTime==0) {
			return;
		}
		
		var options = {
				serviceClassName:globazGlobal.prestationsViewService,
				serviceMethodName:'isValidForAJ',
				parametres:idPosteTravail + ',' + dateDebut + ',' + dateFin,
				callBack:function (data) {
					var $inputs = $("input:not('#debutAbsence,#finAbsence'),select:not('#idPosteTravail')"); 
					$.noty.closeAll();
					if(!data.valid) {
						var messages = "";
						for(var i=0; i<data.messages.length;i++) {
							if(i!=0) {
								messages += "<br />";
							}
							messages += data.messages[i];
						}
						$inputs.attr('disabled',true);
						globazGlobal.notifyError(messages);
					} else {
						$inputs.attr('disabled',false);
						$("#beneficiaire").focus();
					}
				}
		};
		
		if(dateFinTime>=dateDebutTime)
			vulpeculaUtils.lancementService(options);	
	}
	
	function findTauxForAJ() {
		var options = {
				serviceClassName:globazGlobal.prestationsViewService,
				serviceMethodName:'findTauxForAJ',
				parametres:globazGlobal.$getDateDebutAbsence().val(),
				callBack:function (data) {
					var tauxAVS = data[0];
					var tauxAC = data[1];
					$('#spanTauxAVS').text(tauxAVS);
					$('#spanTauxAC').text(tauxAC);
					$('input[name="tauxAVS"]').val(tauxAVS);
					$('input[name="tauxAC"]').val(tauxAC);
				}
		};
		vulpeculaUtils.lancementService(options);				
	}
	
	function findDecomptePrecedent(idPosteTravail, dateFin) {
		$('#innerWrapper').overlay();
		var options = {
				serviceClassName:globazGlobal.decompteSalaireViewService,
				serviceMethodName:'findPrecedentComptabilise',
				parametres:idPosteTravail + ',' + dateFin,
				callBack:function (data) {
					var $salaireHoraire = globazGlobal.$getSalaireHoraire();
					if(data.salaireHoraire) {
						$salaireHoraire.val(globazNotation.utilsFormatter.formatStringToAmout(data.salaireHoraire,2, true));
					}
					else {
						$salaireHoraire.val(globazNotation.utilsFormatter.formatStringToAmout(roundToFiveCentimes(0),2, true));
					}
					
					globazGlobal.calcul.calculMontants();
					$('#innerWrapper').removeOverlay();
				}
		};
		vulpeculaUtils.lancementService(options);			
	}
	
	function findHeuresTravailSemaine(annee,anneeFin,idPosteTravail) {
		var options = {
				serviceClassName:globazGlobal.prestationsViewService,
				serviceMethodName:'getHeuresTravailJour',
				parametres:idPosteTravail+','+$debutAbsence.val(),
				callBack:function (data) {
					globazGlobal.$getNombreHeuresParJour().val(data);
				}
		};
		vulpeculaUtils.lancementService(options);
	}
	
	function reloadDecompteSalaireIfAnneeDebutSet() {
		$debutAbsence = $('#debutAbsence');
		$finAbsence = $('#finAbsence');
		$posteTravail = $('#idPosteTravail');
		
		var idPosteTravail = $posteTravail.val();
		var anneeDebutAbsence = getYearFromString($debutAbsence.val());
		var anneeFinAbsence = getYearFromString($finAbsence.val());
		if(anneeDebutAbsence !== null) {
			findDecomptePrecedent(idPosteTravail, $debutAbsence.val());
			findHeuresTravailSemaine(anneeDebutAbsence, anneeFinAbsence, idPosteTravail);
			findTauxForAJ(idPosteTravail, $debutAbsence.val());
			globazGlobal.cotisations.calcul();
		}		
	}
	
	return {
		init : function() {			
			globazGlobal.loadNumeroCompte();
			
			$debutAbsence = $('#debutAbsence');
			$finAbsence = $('#finAbsence');
			$posteTravail = $('#idPosteTravail');
			
			$debutAbsence.change(function () {
				reloadDecompteSalaireIfAnneeDebutSet();
				
			});
			
			$debutAbsence.add($finAbsence).change(function () {
				isPeriodeContenueDansPoste(globazGlobal.getPosteTravail(), $debutAbsence.val(), $finAbsence.val());
			});
			
			reloadDecompteSalaireIfAnneeDebutSet();
			globazGlobal.$getPosteTravail().change(function() {
				globazGlobal.loadNumeroCompte();
				reloadDecompteSalaireIfAnneeDebutSet();
				isPeriodeContenueDansPoste(globazGlobal.getPosteTravail(), $debutAbsence.val(), $finAbsence.val());
			});
		}
	};
})();

globazGlobal.loadNumeroCompte = function() {
	var options = {
			serviceClassName:globazGlobal.prestationsViewService,
			serviceMethodName:'getNumeroCompteForPoste',
			parametres: globazGlobal.getPosteTravail() + ',' + $('#beneficiaire').val() + ',',
			callBack:function (data) {
				$('#adressePaiementEmployeur').text(data);
			}
	};
	vulpeculaUtils.lancementService(options);
};

globazGlobal.calculAjoutSuppressionCotisations = function() {
	var options = {
			serviceClassName:globazGlobal.prestationsViewService,
			serviceMethodName:'mustSubstractCotisationsForAJ',
			parametres:globazGlobal.getPosteTravail() + ',',
			callBack:function (data) {
				globazGlobal.mustSubstractCotisations = data;
				globazGlobal.cotisations.calcul();
			}
	};
	vulpeculaUtils.lancementService(options);
};

globazGlobal.cotisations = (function() {
		function calculCotisation() {
			var $cotisations = $('.cotisation');
			$cotisations.each(function () {
				var $this = $(this);
				var $tauxAssurance = $this.find('.tauxAssurance');
				var $montantCotisation = $this.find('.montantCotisation');
				
				var tauxAssurance = $tauxAssurance.val() / 100;
				var montantBrut = getMontantBrut();
					
				var montantCotisation = globazNotation.utilsFormatter.formatStringToAmout(roundToFiveCentimes(tauxAssurance * montantBrut));
				$montantCotisation.val(montantCotisation);
			});	
		};
		
		function calculMontantAVerser() {
			var $montantAVerser = $('#montantVerse');
			if(globazGlobal.isNouveau) {
				$montantAVerser.val(globazNotation.utilsFormatter.formatStringToAmout(roundToFiveCentimes(getSommeCotisations())));
			} else {
				if (globazGlobal.mustSubstractCotisations) {
					$(".ajoutSuppressionCotisations").text("-");
				} else {
					$(".ajoutSuppressionCotisations").text("+");
				}
			}
		};
		
		function getMontantBrut() {
			return globazNotation.utilsFormatter.amountTofloat($('#montantBrut').val());
		}
		
		function getSommeCotisations() {
			var sum = 0;
			var $montantCotisations = $('.montantCotisation');
			$montantCotisations.each(function () {
				var montant = globazNotation.utilsFormatter.amountTofloat($(this).val());
				sum += montant;
			});
			
			if (globazGlobal.mustSubstractCotisations) {
				$(".ajoutSuppressionCotisations").text("-");
				sum = getMontantBrut() - sum;
			} else {
				$(".ajoutSuppressionCotisations").text("+");
				sum = getMontantBrut() + sum;
			}
			
			return sum;			
		}
	
		return {
			init : function() {
				$('#montantBrut').change(function() {
					calculCotisation();
					calculMontantAVerser();
				});
			},
			calcul : function() {
				calculCotisation();
				calculMontantAVerser();
			}
		};
	}());

globazGlobal.getPosteTravail = function() {
	var idPosteTravail = 0;
	if(globazGlobal.isNouveau) {
		idPosteTravail = globazGlobal.$getPosteTravail().val();
	} else {
		idPosteTravail = globazGlobal.idPosteTravail;
	}
	return idPosteTravail;
};

globazGlobal.$getPosteTravail = function() {
	return $('#idPosteTravail');
};

globazGlobal.$getSalaireHoraire = function() {
	return $('#salaireHoraire');
};

globazGlobal.$getNombreHeuresParJour = function() {
	return $('#nombreHeuresParJour');
};

globazGlobal.$getDateDebutAbsence = function() {
	return $('#debutAbsence');
};

globazGlobal.validation = (function()  {
	function checkDates() {
		var mainForm = $('form[name="mainForm"]').serializeFormJSON();
		
		var dateDebutAbsence = getDateFromFormat(mainForm.debutAbsence, 'dd.MM.yyyy');
		var dateFinAbsence = getDateFromFormat(mainForm.finAbsence, 'dd.MM.yyyy');
		
		if(dateDebutAbsence==0 || dateFinAbsence==0) {
			showErrorDialog(globazGlobal.messagePeriodeNonVide);
			return false;
		}
		if(dateFinAbsence<dateDebutAbsence) {
			showErrorDialog(globazGlobal.messagePeriodeDebutPlusGrandePeriodeFin);
			return false;
		}
		return true;
	}
	
	function validate() {
		return checkDates();
	}
	
	return {
		init : init,
		validate : validate
	};
})();
	