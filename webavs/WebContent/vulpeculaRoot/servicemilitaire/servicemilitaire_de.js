jsManager.executeAfter = function() {
		if (globazGlobal.isNouveau) {
			globazGlobal.bindEvents();
			globazGlobal.services.launch();
			globazGlobal.calcul.init();
			globazGlobal.decompteSalaire.init();
		} else {
			globazGlobal.calcul.calculExistant();
		}
};

// déclaration des événements
globazGlobal.bindEvents = function() {
	if (globazGlobal.isNouveau) {
		globazGlobal.$getPosteTravail().change(function() {
			globazGlobal.loadNumeroCompte();
			globazGlobal.decompteSalaire.reloadDecompte();
			globazGlobal.services.launch();
		});

		globazGlobal.$getNombreDeJours().change(function() {
			$.noty.closeAll();
			$that = $(this);
			if ($that.val() < 0) {
				$($that.val("0"));
			}
			globazGlobal.validation.verifierNombresJourSelonPrestation();
			globazGlobal.calcul.calculMontants();
		});

		globazGlobal.$getSalaireHoraire().change(function() {
			globazGlobal.calcul.calculMontants();
		});

		globazGlobal.$getTauxCouvertureAPG().change(function() {
			$that = $(this);
			if ($that.val() < 0) {
				$($that.val("0.000"));
			}
			if ($that.val() > 100) {
				$($that.val("100.000"));
			}
			globazGlobal.calcul.calculMontants();
		});

		globazGlobal.$getMontantVersementAPG().change(function() {
			globazGlobal.calcul.calculMontants();
		});

		globazGlobal.$getMontantCompensation().change(function() {
			globazGlobal.calcul.calculMontants();
		});

		globazGlobal.$getMontantBrut().change(function() {
			globazGlobal.cotisations.calcul();
		});

		globazGlobal.$getBeneficiaire().change(function() {
			globazGlobal.loadNumeroCompte();
			globazGlobal.cotisations.calcul();
		});

		globazGlobal.$getGenrePrestation().change(function() {
			globazGlobal.genrePrestations.getConfigurationPrestation(
					globazGlobal.getPosteTravail(), $(this).val(),
					globazGlobal.$getDebutAbsence().val());
		});
	}
};

globazGlobal.loadNumeroCompte = function() {
	var options = {
			serviceClassName:globazGlobal.prestationsViewService,
			serviceMethodName:'getNumeroCompteForPoste',
			parametres: globazGlobal.getPosteTravail() + ',' + globazGlobal.$getBeneficiaire().val() + ',',
			callBack:function (data) {
				$('#adressePaiementEmployeur').text(data);
			}
	};
	vulpeculaUtils.lancementService(options);
};

function add() {
	document.forms[0].elements('userAction').value = "vulpecula.servicemilitaire.servicemilitaire.afficher&_method=_add";
}

function upd() {
}

function validate() {
	function retrieveTaux() {
		var taux = [];

		if (!globazGlobal.isBeneficiaireTravailleur()) {
			$('.cotisation').each(function() {
				var $this = $(this);

				var idAssurance = $this.attr('data-idAssurance');
				var tauxAssurance = $this.find('.tauxAssurance').val();
				var tauxServiceMilitaire = {
				    idAssurance : idAssurance,
					tauxAssurance : tauxAssurance
				};
				taux.push(tauxServiceMilitaire);
			});
		}

		document.forms[0].elements('taux').value = JSON.stringify(taux);
	}

	var state = globazGlobal.validation.validate();
	if (document.forms[0].elements('_method').value == "add") {
		document.forms[0].elements('userAction').value = "vulpecula.servicemilitaire.servicemilitaire.ajouter";
	} else {
		document.forms[0].elements('userAction').value = "vulpecula.servicemilitaire.servicemilitaire.modifier";
	}

	retrieveTaux();

	return state;
}

function cancel() {
}

function del() {
	var message = jQuery.i18n.prop("ajax.deleteMessage");
	if (confirm(message)) {
		document.forms[0].elements('userAction').value = "vulpecula.servicemilitaire.servicemilitaire.supprimer";
		document.forms[0].submit();
	}
}

function init() {
	globazGlobal.loadNumeroCompte();
}

globazGlobal.calcul = (function() {
	var $totalUnites = null;
	var $baseSalaire = null;

	function calcul() {
		var nombreDeJours = globazNotation.utilsFormatter
				.amountTofloat(getNombreDeJours());
		var nombreHeuresParJour = getNombreHeuresParJour();
		var salaireHoraire = globazNotation.utilsFormatter
				.amountTofloat(getSalaireHoraire());

		var totalUnites = nombreDeJours * nombreHeuresParJour;
		var baseSalaire = totalUnites * salaireHoraire;
		$totalUnites.val(globazNotation.utilsFormatter.formatStringToAmout(
				totalUnites, 2, true));
		$baseSalaire.val(globazNotation.utilsFormatter.formatStringToAmout(
				roundToFiveCentimes(baseSalaire), 2, true));

		if (!isNaN(parseFloat(salaireHoraire))) {
			calculMontantCP();
			calculMontantGratification();
			calculTotalSalaire(totalUnites);
			calculMontantCouvertureAPG();
		}
	}

	function calculMontantBrut() {
		var $montantCouvertureAPG = globazNotation.utilsFormatter
				.amountTofloat(globazGlobal.$getMontantCouvertureAPG().val());
		var $montantVersementAPG = globazNotation.utilsFormatter
				.amountTofloat(globazGlobal.$getMontantVersementAPG().val());
		var $montantCompensation = globazNotation.utilsFormatter
				.amountTofloat(globazGlobal.$getMontantCompensation().val());
		var montantBrut = $montantCouvertureAPG - $montantVersementAPG
				- $montantCompensation;

		if (montantBrut < 0) {
			montantBrut = 0;
		}
		
		montantBrut = roundToFiveCentimes(montantBrut);
		globazGlobal.$getMontantBrut().val(
				globazNotation.utilsFormatter.formatStringToAmout(montantBrut,
						2, true));
	}

	function calculMontantAVerser() {
		globazGlobal.cotisations.calcul();
	}

	function calculExistant() {
		var $totalUnites = $('#totalUnites');
		var totalUnites = (getNombreDeJours() * getNombreHeuresParJour())
				.toFixed(2);

		$totalUnites.val(totalUnites);

		calculMontantCP();
		calculMontantGratification();
		calculTotalSalaire(totalUnites);
		calculMontantCouvertureAPG();
		globazGlobal.cotisations.calcul(true);
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

	function calculMontantCP() {
		var salaireHoraire = globazNotation.utilsFormatter
				.amountTofloat(getSalaireHoraire());
		var salaireHoraireXtauxCP = (salaireHoraire * globazGlobal.$getTauxCP()
				.val()) / 100;
		globazGlobal.$getMontantCP().val(
				globazNotation.utilsFormatter.formatStringToAmout(
						salaireHoraireXtauxCP, 2, true));
	}

	function calculMontantGratification() {
		var $salaireHoraire = globazNotation.utilsFormatter
				.amountTofloat(getSalaireHoraire());
		var $montantCP = globazNotation.utilsFormatter
				.amountTofloat(globazGlobal.$getMontantCP().val());

		var $salaireHorairePlusCPXtauxCP = (($salaireHoraire + $montantCP) * globazGlobal
				.$getTauxGratification().val()) / 100;
		globazGlobal.$getMontantGratification().val(
				globazNotation.utilsFormatter.formatStringToAmout(
						$salaireHorairePlusCPXtauxCP, 2,
						true));
	}

	function calculTotalSalaire(totalUnite) {
		var $salaireHoraire = globazNotation.utilsFormatter
				.amountTofloat(getSalaireHoraire());
		var $montantGratification = globazNotation.utilsFormatter
				.amountTofloat(globazGlobal.$getMontantGratification().val());

		var $salaireHorairePlusGrat = $salaireHoraire + $montantGratification;
		var $totalSalaire = roundToFiveCentimes($salaireHorairePlusGrat	* totalUnite);
		globazGlobal.$getTotalSalaire().val(
				globazNotation.utilsFormatter.formatStringToAmout(
						$totalSalaire, 2, true));
	}

	function calculMontantCouvertureAPG() {
		var $totalSalaire = globazNotation.utilsFormatter
				.amountTofloat(globazGlobal.$getTotalSalaire().val());
		var $tauxCouvertureAPG = globazNotation.utilsFormatter
				.amountTofloat(globazGlobal.$getTauxCouvertureAPG().val());
		var $montantCouvertureAPG = $totalSalaire * ($tauxCouvertureAPG / 100);
		globazGlobal.$getMontantCouvertureAPG().val(
				globazNotation.utilsFormatter.formatStringToAmout(
						roundToFiveCentimes($montantCouvertureAPG), 2, true));
	}

	return {
		init : function() {
			$nombreDeJours = $('#nombreDeJours');
			$salaireHoraire = $('#salaireHoraire');
			$totalUnites = $('#totalUnites');
			$montantBrut = $('#montantBrut');
			$baseSalaire = $('#baseSalaire');
			$tauxCouvertureAPG = $("#tauxCouvertureAPG");

			this.calculMontants();
		},
		calculMontants : function() {
			calcul();
			calculMontantBrut();
			calculMontantAVerser();
		},
		calculExistant : calculExistant
	};
})();

globazGlobal.genrePrestations = (function() {
	function getConfigurationPrestation(idPosteTravail, valeurPrestation,
			dateAbsenceDebut) {
		var debutAbsence = getYearFromString(globazGlobal.$getDebutAbsence()
				.val());

		if (debutAbsence != null) {
			var options = {
				serviceClassName : globazGlobal.prestationsViewService,
				serviceMethodName : 'findConfigurationPourPrestationSM',
				parametres : idPosteTravail + "," + valeurPrestation + ","
						+ dateAbsenceDebut,
				callBack : function(data) {
					if (data != undefined) {
						setConfiguration(data);
					}
					globazGlobal.calcul.calculMontants();
					globazGlobal.validation.verifierNombresJourSelonPrestation();
				}
			};
			vulpeculaUtils.lancementService(options);
		}
	}

	function setConfiguration(data) {
		globazGlobal.$getNbJoursMinimum().val(data.minJours);
		globazGlobal.$getNbJoursMaximum().val(data.maxJours);
		globazGlobal.$getTauxCP().val(data.tauxCP.toFixed(3));
		globazGlobal.$getTauxGratification().val(data.gratification.toFixed(3));
		globazGlobal.$getTauxCouvertureAPG().val(data.couvertureAPG.toFixed(3));
	}

	return {
		getConfigurationPrestation : getConfigurationPrestation
	};
})();

globazGlobal.decompteSalaire = (function() {
	var $debutAbsence = null;

	function reloadDecompteSalaireIfAnneeDebutSet() {
		var idPosteTravail = globazGlobal.$getPosteTravail().val();
		var anneeDebutAbsence = getYearFromString(globazGlobal.$getDebutAbsence().val());

		if (anneeDebutAbsence !== null) {
			// Lancement en cascade de findDecomptePrecedent qui lancera
			// findHeuresTravailSemaine, qui lancera findTauxCP, qui lancera
			// findTauxGratification,
			// qui lancera findTauxCouvertureAPG qui lancera la calcul final
			findDecomptePrecedent(idPosteTravail, $debutAbsence.val());
			globazGlobal.cotisations.calcul();
		}
	}

	function findDecomptePrecedent(idPosteTravail, dateFin) {
		$('#innerWrapper').overlay();
		var options = {
			serviceClassName : globazGlobal.decompteSalaireViewService,
			serviceMethodName : 'findPrecedentComptabilise',
			parametres : idPosteTravail + ',' + dateFin,
			callBack : function(data) {
				var $salaireHoraire = globazGlobal.$getSalaireHoraire();
				if (data.salaireHoraire) {
					$salaireHoraire.val(globazNotation.utilsFormatter
							.formatStringToAmout(data.salaireHoraire, 2, true));
				} else {
					$salaireHoraire.val(globazNotation.utilsFormatter
							.formatStringToAmout(0, 2, true));
				}

				var idPosteTravail = globazGlobal.$getPosteTravail().val();
				var anneeDebutAbsence = getYearFromString(globazGlobal
						.$getDebutAbsence().val());
				var anneeFinAbsence = getYearFromString(globazGlobal
						.$getFinAbsence().val());
				findHeuresTravailSemaine(anneeDebutAbsence, anneeFinAbsence,
						idPosteTravail);

				$('#innerWrapper').removeOverlay();
			}
		};
		vulpeculaUtils.lancementService(options);
	}
	
	function findAssurancesInfos(date) {
		var options = {
			serviceClassName : globazGlobal.prestationsViewService,
			serviceMethodName : 'findAssurancesInfosForSM',
			parametres : date,
			callBack : function(assurancesInfos) {
				var $beforeCotisations = $('#beforeCotisations'); 
				$beforeCotisations.nextUntil('#montantAVerser').remove();
				var assurancesHTML = $(createAssurancesInfos(assurancesInfos));
				$beforeCotisations.after(assurancesHTML);
				notationManager.addNotationOnFragmentWithoutEvents(assurancesHTML);
			}
		};
		vulpeculaUtils.lancementService(options);		
	}
	
	function createAssurancesInfos(assurancesInfos) {
		var assurancesHTML = null;
		for(var i=0;i<assurancesInfos.length;i++) {
			var assuranceInfo = assurancesInfos[i];
			assurancesHTML += 
			'<tr class="cotisation" data-idAssurance="' + assuranceInfo.idAssurance + '">' +
			'<td class="border_left indentLeft" colspan="5">' +
				'<input class="tauxAssurance" value="' + assuranceInfo.taux + '" type="hidden" />' +
				assuranceInfo.libelleAssurance + '&nbsp;' + assuranceInfo.taux + ' %' +
			'</td>' +
			'<td style="text-align: right;">' +
				'<input class="montantCotisation smallextended readOnly"  type="text" data-g-amount="" readonly="readonly" disabled="disabled" tabindex="-1"/>' +
			'</td>' +
			'<td></td>' +
			'<td class="border_right"></td>' +
			'</tr>';
		}
		return assurancesHTML;
	}

	function findHeuresTravailSemaine(annee, anneeFin, idPosteTravail) {
		var options = {
			serviceClassName : globazGlobal.prestationsViewService,
			serviceMethodName : 'getHeuresTravailJour',
			parametres : idPosteTravail + ',' + $debutAbsence.val(),
			callBack : function(data) {
				globazGlobal.$getNombreHeuresParJour().val(
						parseFloat(data).toFixed(2));

				$genrePrestations = globazGlobal.$getGenrePrestation();
				var idPosteTravail = globazGlobal.$getPosteTravail().val();
				var dateAbsenceDebut = globazGlobal.$getDebutAbsence().val();
				globazGlobal.genrePrestations.getConfigurationPrestation(
						idPosteTravail, $genrePrestations.val(),
						dateAbsenceDebut);
			}
		};
		vulpeculaUtils.lancementService(options);
	}

	function findNombreJoursOuvrablesEntreDeuxDates(dateDebut, dateFin) {
		var options = {
			serviceClassName : globazGlobal.dateViewService,
			serviceMethodName : 'nombreJoursOuvrablesEntreDeuxDates',
			parametres : dateDebut + "," + dateFin,
			callBack : function(data) {
				globazGlobal.$getNombreDeJours().val(data.toFixed(2));
				globazGlobal.validation.verifierNombresJourSelonPrestation();
				globazGlobal.calcul.calculMontants();
			}
		};
		vulpeculaUtils.lancementService(options);
	}

	function calculNombreDeJours(date1, date2) {
		if (date1.length > 0 && date2.length > 0) {
			var dateDebutTime = getDateFromFormat(date1, "dd.MM.yyyy");
			var dateFinTime = getDateFromFormat(date2, "dd.MM.yyyy");
			if(dateFinTime>=dateDebutTime)
				findNombreJoursOuvrablesEntreDeuxDates(date1, date2);
		}
	}

	function isPeriodeContenueDansPoste(idPosteTravail, dateDebut, dateFin) {
		var options = {
			serviceClassName : globazGlobal.prestationsViewService,
			serviceMethodName : 'isValidForSM',
			parametres : idPosteTravail + ',' + dateDebut + ',' + dateFin,
			callBack : function(data) {
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
					$inputs.attr('disabled', false);
					calculNombreDeJours(dateDebut, dateFin);
				}
			}
		};

		vulpeculaUtils.lancementService(options);
	}
	
	function isPeriodeValid(dateDebut, dateFin) {
		var dateDebutTime = getDateFromFormat(dateDebut, "dd.MM.yyyy");
		var dateFinTime = getDateFromFormat(dateFin, "dd.MM.yyyy");
		if (dateDebutTime == 0 || dateFinTime == 0) {
			return false;
		}		
		
		if (dateFinTime < dateDebutTime) {
			return false;
		}
		return true;
	}

	return {
		init : function() {
			$debutAbsence = $('#debutAbsence');
			$finAbsence = $('#finAbsence');
			
			$finAbsence.add($debutAbsence).change(
					function() {
						var debutAbsence = $debutAbsence.val();
						var finAbsence = $finAbsence.val();
						
						$.noty.closeAll();
						
						if(isPeriodeValid(debutAbsence, finAbsence)) {
							isPeriodeContenueDansPoste(globazGlobal.getPosteTravail(), debutAbsence, finAbsence);
						}
				});

			$debutAbsence.change(function() {
				var debutAbsence = $debutAbsence.val();
				if(debutAbsence.length!==0) {
					reloadDecompteSalaireIfAnneeDebutSet();
					findAssurancesInfos(debutAbsence);
				}
			});

			reloadDecompteSalaireIfAnneeDebutSet();
		},
		findTauxCouvertureAPG : function(idPosteTravail) {
			findTauxCouvertureAPG(idPosteTravail);
		},
		reloadDecompte : reloadDecompteSalaireIfAnneeDebutSet
	};
})();

globazGlobal.isBeneficiaireTravailleur = function() {
	return $('#beneficiaire').val() == globazGlobal.beneficiaireTravailleur;
};

globazGlobal.cotisations = (function() {
	function removeLineCotisations() {
		$('.cotisation td').css('text-decoration', 'none');
	}

	function lineCotisations() {
		$('.cotisation td').css('text-decoration', 'line-through');
	}

	function calcul(bPasCalculMontantAVerser) {
		var $cotisations = $('.cotisation');
		$cotisations.each(function() {
			var $this = $(this);
			var $tauxAssurance = $this.find('.tauxAssurance');

			var tauxAssurance = $tauxAssurance.val() / 100;
			var montantBrut = getMontantBrut();

			var montantCotisation = globazNotation.utilsFormatter
					.formatStringToAmout(0, 2, true);
			if (!globazGlobal.isBeneficiaireTravailleur()) {
				removeLineCotisations();
				montantCotisation = tauxAssurance * montantBrut;
			} else {
				lineCotisations();
			}
			var $montantCotisation = $this.find('.montantCotisation');
			$montantCotisation.val(globazNotation.utilsFormatter.formatStringToAmout(roundToFiveCentimes(montantCotisation)));
		});
		
		if(!bPasCalculMontantAVerser) {
			var $montantAVerserVal = getSommeCotisations();
			globazGlobal
					.$getMontantAVerser()
					.val(globazNotation.utilsFormatter.formatStringToAmout(roundToFiveCentimes($montantAVerserVal)));
		}
	}

	function getMontantBrut() {
		return globazNotation.utilsFormatter.amountTofloat($('#montantBrut').val());
	}

	function getSommeCotisations() {
		var sum = 0;
		var $montantCotisations = $('.montantCotisation');
		$montantCotisations.each(function() {
			var montant = globazNotation.utilsFormatter.amountTofloat($(this).val());
			sum += montant;
		});
		sum += getMontantBrut();
		return sum;
	}

	return {
		calcul : function(bPasCalculMontantAVerser) {
			calcul(bPasCalculMontantAVerser);
		}
	};
}());

globazGlobal.services = (function() {
	function hasCaisseMetier(idPosteTravail) {
		var buttons = $('input');

		function disableAllButons() {
			buttons.attr('disabled', 'disabled');
		}
		;
		function enabledAllButons() {
			buttons.removeAttr('disabled');
		}
		function resetFieldsCalcul() {
			globazGlobal.$getMontantVersementAPG().val("0.00");
			globazGlobal.$getMontantCompensation().val("0.00");
			globazGlobal.$getTauxCouvertureAPG().val("0.000");
			globazGlobal.$getMontantBrut().val("0.00");
		}

		var options = {
			serviceClassName : globazGlobal.prestationsViewService,
			serviceMethodName : 'hasCaisseMetier',
			parametres : idPosteTravail,
			callBack : function(data) {
				if (!data) {
					globazGlobal
							.notifyError(globazGlobal.messageAucuneCaisseMetier);
					resetFieldsCalcul();
					disableAllButons();
				} else {
					enabledAllButons();
				}
			}
		};
		vulpeculaUtils.lancementService(options);
	}

	function cleanMessages() {
		$('#informations').empty();
	}

	return {
		launch : function() {
			cleanMessages();
			hasCaisseMetier(globazGlobal.getPosteTravail());
		}
	};

})();

globazGlobal.validation = (function() {

	function verifierNombresJourSelonPrestation() {
		var nombreJoursMin =  globazGlobal.$getNbJoursMinimum().val();
		var nombreJoursMax = globazNotation.utilsFormatter.amountTofloat(globazGlobal.$getNbJoursMaximum().val());
		var nombreJours = globazNotation.utilsFormatter.amountTofloat(globazGlobal.$getNombreDeJours().val());

		if (nombreJoursMin == 0 && nombreJoursMax == 0) {
			return true;
		}

		if (nombreJours != 0) {
			if (nombreJours > nombreJoursMax) {
				globazGlobal
						.notifyWarning(globazGlobal.messageNombreJourMaximum);
				return false;
			}

			if (nombreJours < nombreJoursMin) {
				globazGlobal
						.notifyWarning(globazGlobal.messageNombreJourMinimum);
				return false;
			}
		}
	}

	function checkDates() {
		var mainForm = $('form[name="mainForm"]').serializeFormJSON();

		var dateDebutAbsence = getDateFromFormat(mainForm.debutAbsence,
				'dd.MM.yyyy');
		var dateFinAbsence = getDateFromFormat(mainForm.finAbsence,
				'dd.MM.yyyy');

		if (dateDebutAbsence == 0) {
			showErrorDialog(globazGlobal.messagePeriodeNonVide);
			return false;
		}
		if (dateFinAbsence == 0) {
			showErrorDialog(globazGlobal.messagePeriodeFinNonSaisie);
			return false;
		}
		if (dateFinAbsence < dateDebutAbsence) {
			showErrorDialog(globazGlobal.messagePeriodeDebutPlusGrandePeriodeFin);
			return false;
		}
		return true;
	}
	
	function checkVersementAPG() {
		var mainForm = $('form[name="mainForm"]').serializeFormJSON();
		var versementAPG = mainForm.versementAPG;
		if (versementAPG.length == 0) {
			showErrorDialog(globazGlobal.messageVersementAPG);
			return false;
		}
		return true;
	}

	function validate() {
		return checkDates() && checkVersementAPG();
	}

	return {
		validate : validate,
		verifierNombresJourSelonPrestation : verifierNombresJourSelonPrestation
	};
})();

// GESTION DES ERREURS
globazGlobal.notifyError = function(message) {
	$('#informations').noty({
		type : 'error',
		timeout : 3000,
		maxVisible : 2,
		timeout : false,
		text : message
	});
};

// GESTION DES WARNINGS
globazGlobal.notifyWarning = function(message) {
	$('#informations').noty({
		type : 'warning',
		timeout : 3000,
		maxVisible : 2,
		// timeout : false,
		text : message
	});
};

globazGlobal.$getSalaireHoraire = function() {
	return $('#salaireHoraire');
};

globazGlobal.$getNombreHeuresParJour = function() {
	return $('#nombreHeuresParJour');
};

globazGlobal.$getNombreDeJours = function() {
	return $('#nombreDeJours');
};

globazGlobal.$getNbJoursMinimum = function() {
	return $('#nombreDeJourMinimum');
};

globazGlobal.$getNbJoursMaximum = function() {
	return $('#nombreDeJourMaximum');
};

globazGlobal.$getTauxCP = function() {
	return $('#tauxCP');
};

globazGlobal.$getTauxGratification = function() {
	return $('#tauxGratification');
};

globazGlobal.$getTauxCouvertureAPG = function() {
	return $('#tauxCouvertureAPG');
};

globazGlobal.$getMontantCouvertureAPG = function() {
	return $('#montantCouvertureAPG');
};

globazGlobal.$getGenrePrestation = function() {
	return $('#genrePrestations');
};

globazGlobal.$getMontantCP = function() {
	return $('#montantCongesPayes');
};

globazGlobal.$getMontantGratification = function() {
	return $('#montantGratification');
};

globazGlobal.$getTotalSalaire = function() {
	return $('#totalSalaire');
};

globazGlobal.$getMontantVersementAPG = function() {
	return $('#versementAPG');
};

globazGlobal.$getMontantCompensation = function() {
	return $('#compensationAPG');
};

globazGlobal.$getMontantBrut = function() {
	return $('#montantBrut');
};

globazGlobal.$getDebutAbsence = function() {
	return $('#debutAbsence');
};

globazGlobal.$getFinAbsence = function() {
	return $('#finAbsence');
};

globazGlobal.$getMontantAVerser = function() {
	return $('#montantVerse');
};

globazGlobal.$getBeneficiaire = function() {
	return $('#beneficiaire');
};

globazGlobal.getPosteTravail = function() {
	var idPosteTravail = 0;
	if (globazGlobal.isNouveau) {
		idPosteTravail = globazGlobal.$getPosteTravail().val();
	} else {
		idPosteTravail = globazGlobal.idPosteTravail;
	}
	return idPosteTravail;
};

globazGlobal.$getPosteTravail = function() {
	return $('#idPosteTravail');
};