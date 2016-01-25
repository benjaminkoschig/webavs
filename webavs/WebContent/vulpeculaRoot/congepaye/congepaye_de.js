$(function () {
	$("#cotisations").hide();
	$("#rowMontantBrut").hide();
	if (globazGlobal.isNouveau) {
		globazGlobal.bindEvents();
		
		if (globazGlobal.wantCotisations) {
			globazGlobal.rechercheEtAfficheCotisations();
		} else {
			globazGlobal.cacherCotisations();
		}
	} else {
		if (globazGlobal.wantCotisations) {
			globazGlobal.rechercheEtAfficheCotisations();
		} else {
			globazGlobal.cacherCotisations();
		}
		globazGlobal.calculMontantsPourRetrieve();
	}
});	

globazGlobal.services = $.extend(globazGlobal.services, (function() {
	
	function disableAllButtons() {
		var $inputs = $("input:not('#anneeDebut,#anneeFin'),select:not('#idPosteTravail')");
		$inputs.attr('disabled','disabled');
	};
	function enabledAllButtons() {
		var $inputs = $("input:not('#anneeDebut,#anneeFin'),select:not('#idPosteTravail')");
		$inputs.removeAttr('disabled');
	}
	
	function hasCaisseMetier(idPosteTravail) {
		var options = {
				serviceClassName:globazGlobal.prestationsViewService,
				serviceMethodName:'hasCaisseMetier',
				parametres:idPosteTravail,
				callBack:function (data) {
					if(!data) {
						globazGlobal.notifyError(globazGlobal.messageAucuneCaisseMetier);
						return;
					}
					isPeriodeContenueDansPoste(globazGlobal.getIdPosteTravail(), globazGlobal.getAnneeDebut(), globazGlobal.getAnneeFin());
				}
		};
		vulpeculaUtils.lancementService(options);	
	};
	
	function isPeriodeContenueDansPoste(idPosteTravail, anneeDebut, anneeFin) {
		var options = {
				serviceClassName:globazGlobal.prestationsViewService,
				serviceMethodName:'isPeriodeContenueDansPosteForCP',
				parametres:idPosteTravail + ',' + anneeDebut + ',' + anneeFin,
				callBack:function (data) {
					if(!data.value) {
						disableAllButtons();
						globazGlobal.notifyError(data.message);
					} else {
						enabledAllButtons();
						searchCompteurs(globazGlobal.getIdPosteTravail());
						hasLignesPourPeriode(globazGlobal.getIdPosteTravail(), globazGlobal.getAnneeDebut(), globazGlobal.getAnneeFin());
					}
				}
		};
		vulpeculaUtils.lancementService(options);	
	}
	
	function hasLignesPourPeriode(idPosteTravail, anneeDebut, anneeFin) {
		var options = {
				serviceClassName:globazGlobal.prestationsViewService,
				serviceMethodName:'hasLignesPourPeriode',
				parametres:idPosteTravail+','+anneeDebut+','+anneeFin,
				callBack:function (data) {
					if(data) {
						globazGlobal.notifyWarning(globazGlobal.messageLignePeriodeSaisie);
					}
				}
		};
		vulpeculaUtils.lancementService(options);	
	};
	
	function searchCompteurs(idPosteTravail) {
		var options = {
				serviceClassName:globazGlobal.prestationsViewService,
				serviceMethodName:'hasCompteursActifs',
				parametres:$("#idPosteTravail").val(),
				callBack:function (data) {
					if(!data) {
						globazGlobal.notifyWarning(globazGlobal.messageAucunCompteur);
					}
				}
		};
		vulpeculaUtils.lancementService(options);	
	};
	
	function calculAllFields(){
		globazGlobal.nouveauPosteWantCotisations();
	};
	
	function isServiceCanBeCalled() {
		var anneeSize = 4;
		var anneeDebut = $("#anneeDebut").val();
		var anneeFin = $("#anneeFin").val();
		
		return anneeDebut.length == anneeSize && anneeFin.length == anneeSize && anneeDebut<=anneeFin;
	};
	
	function cleanMessages() {
		$.noty.closeAll();
	}
	
	function beforeLaunch() {
		cleanMessages();
		if(!isServiceCanBeCalled()) {
			return false;
		}
		calculAllFields();
		return true;
	}
	
	return {
		launchForPeriodeChange : function() {
			if(beforeLaunch())
				isPeriodeContenueDansPoste(globazGlobal.getIdPosteTravail(), globazGlobal.getAnneeDebut(), globazGlobal.getAnneeFin());
		},
		launchForPosteChange : function() {
			if(beforeLaunch())
				hasCaisseMetier(globazGlobal.getIdPosteTravail());		
		}
	};
	
})());

globazGlobal.getIdPosteTravail = function() {
	return $('#idPosteTravail').val();
};

globazGlobal.getAnneeDebut = function() {
	return $("#anneeDebut").val();
};

globazGlobal.getAnneeFin = function() {
	return $("#anneeFin").val();
};

globazGlobal.notifyError = function(message) {
	$('#informations').noty({
		type: 'error',
		timeout: 3000,
		maxVisible : 3,
		timeout : false,
		text: message});
};

globazGlobal.notifyWarning = function(message) {
	$('#informations').noty({
		type: 'warning',
		timeout: 3000,
		maxVisible : 3,
		timeout : false,
		text: message});
};

globazGlobal.cacherCotisations = function(){
	$('#ajoutSuppressionCotisations').hide();
	$('#montantCotisations').hide();
};

//Regarde si le nouveau poste tient compte des cotisations
globazGlobal.nouveauPosteWantCotisations = function() {
	var options = {
			serviceClassName:globazGlobal.prestationsViewService,
			serviceMethodName:'tenirCompteDesCotisations',
			parametres:$("#idPosteTravail").val(),
			callBack:function (data) {
				globazGlobal.wantCotisations = data;
				
				if (globazGlobal.wantCotisations) {
					globazGlobal.rechercheEtAfficheCotisations();
					$("#rowMontantBrut").show();
					$("#cotisations").show();
				} else {
					$("#rowMontantBrut").hide();
					$("#cotisations").hide();
				}
				globazGlobal.findCompteur( $("#idPosteTravail").val(), $("#anneeDebut").val(),$("#anneeFin").val());
			}
	};
	vulpeculaUtils.lancementService(options);
};

//Recherche du montant restant dans les compteurs pour alimenter le champ salaire déclaré
globazGlobal.findCompteur = function(idPosteTravail, anneeDebut, anneeFin) {
	var options = {
			serviceClassName:globazGlobal.compteurService,
			serviceMethodName:'getMontantTotalForPoste',
			parametres:idPosteTravail + ',' + anneeDebut + ',' + anneeFin,
			callBack:function (data) {
				var idPosteTravail = $("#idPosteTravail").val();
				$("#salaireDeclare").val(globazNotation.utilsFormatter.formatStringToAmout(data, 2, true));
				globazGlobal.services.findTaux(idPosteTravail, function (data) {
					var tauxCP = parseFloat(data).toFixed(2);
					$("#tauxCP").val(tauxCP);
					globazGlobal.calculMontants();
				});
			}
	};
	if(($("#anneeDebut").val().length==4)&&($("#anneeFin").val().length==4)){
		vulpeculaUtils.lancementService(options);
	}
};

//calcul des montants
globazGlobal.calculMontants = function() {
	function calculMontantCotisations(montantBrut) {
		var montantCotisation = 0.0;
		var data = globazGlobal.listeCotisations;
		if(!data) {
			return montantCotisation;
		}
		
		for(var i=0;i<data.length;i++) {
			var montant = parseFloat(montantBrut) * (parseFloat(data[i]['taux'])/100) ;
			montantCotisation += roundToFiveCentimes(montant);
		}
		return montantCotisation;
	}
	
	var salaireNonDeclare = $("#salaireNonDeclare").val();
	if(salaireNonDeclare.length==0){
		salaireNonDeclare = 0;
	}
	var totalSalaire = globazNotation.utilsFormatter.amountTofloat($("#salaireDeclare").val()) + globazNotation.utilsFormatter.amountTofloat(salaireNonDeclare);
	$("#totalSalaire").val(globazNotation.utilsFormatter.formatStringToAmout(totalSalaire.toFixed(2)));

	var montantBrut = parseFloat(totalSalaire) * parseFloat($("#tauxCP").val()) / 100;
	montantBrut = roundToFiveCentimes(montantBrut);
	
	var montantNet = 0;
	if (globazGlobal.wantCotisations) {
		$("#montantBrut").val(globazNotation.utilsFormatter.formatStringToAmout(montantBrut));
		var montantCotisations = calculMontantCotisations(montantBrut);
		montantCotisations = roundToFiveCentimes(montantCotisations);
		$("#montantCotisations").val(globazNotation.utilsFormatter.formatStringToAmout(montantCotisations));
		
		if(globazGlobal.mustSubstractCotisations){
			$("#ajoutSuppressionCotisations").text("-");
			montantNet = montantBrut - montantCotisations;
			montantNet = globazNotation.utilsFormatter.formatStringToAmout(montantNet);
		} else {
			$("#ajoutSuppressionCotisations").text("+");
			montantNet = roundToFiveCentimes(montantBrut + montantCotisations);
			montantNet = globazNotation.utilsFormatter.formatStringToAmout(montantNet);
		}
	} else {
		montantNet = globazNotation.utilsFormatter.formatStringToAmout(montantBrut, 2, true);
	}

	if(globazGlobal.isNouveau) {
		$("#montantNet").val(montantNet);
	}
};

//recherche des cotisations
globazGlobal.rechercheEtAfficheCotisations = function() {
	$("#cotisations").show();
	$("#rowMontantBrut").show();
	var idPosteTravail = $("#idPosteTravail").val();

	if (globazGlobal.isNouveau) {
		var options = {
				serviceClassName:globazGlobal.prestationsViewService,
				serviceMethodName:'getCotisations',
				parametres:idPosteTravail + ',' + $("#anneeDebut").val() + "," + $("#beneficiaire").val(),
				callBack:function (data) {
					globazGlobal.listeCotisations = data; 
					if (globazGlobal.isNouveau && globazGlobal.wantCotisations) {
						globazGlobal.remplirCotisations(data);
						globazGlobal.calculTotal();
					}
					globazGlobal.calculMontants();
				}
			};
	} else {
		var options = {
			serviceClassName:globazGlobal.prestationsViewService,
			serviceMethodName:'getCotisationsForAffichage',
			parametres:idPosteTravail + ',' + $("#anneeDebut").val() + "," + $("#beneficiaire").val() + "," + globazGlobal.idCongepaye,
			callBack:function (data) {
				globazGlobal.listeCotisations = data; 
				if (globazGlobal.isNouveau && globazGlobal.wantCotisations) {
					globazGlobal.remplirCotisations(data);
					globazGlobal.calculTotal();
				}
				globazGlobal.calculMontants();
			}
		};
	}
	vulpeculaUtils.lancementService(options);
};

globazGlobal.calculTotal = function() {
	var total = 0.0;
	var tauxCotisations = $('.tauxCotisation');
	for(var i=0;i<tauxCotisations.length;i++) {
		$taux = $(tauxCotisations[i]);
		total += parseFloat($taux.text());
	}
	var totalFixed2 = total.toFixed(2);
	$('#tauxTotal').text(totalFixed2);
	$('#totalTaux').val(totalFixed2);
};

//remplissage de la table des cotisations
globazGlobal.remplirCotisations = function(data) {	
	$cotisationsTable = $('#cotisationsTable');
	$cotisationsTable.find('tr.cotisationsList').remove();
	var container = '';
	for(var i=0; i<data.length; i++){
		var cotisation = data[i];
		container += '<tr class="cotisationsList" id="cotisation'+i+'" name="cotisation'+i+'">';
		container += '<td>';
		container += cotisation.libelle;
		container += '</td><td class="tauxCotisation" style="text-align: right;">';
		container += cotisation.taux;
		container += '</td>';
		container += '</tr>';
	}
	$(container).insertBefore('#total');
};

globazGlobal.changeCompteursLink = function(idPosteTravail) {
	var $lienCompteurs = $('#lienCompteurs');
	var href = $lienCompteurs.attr('href');
	var newHref = href.replace(/(selectedId=)[0-9]*/,'$1'+idPosteTravail);
	$lienCompteurs.attr('href',newHref);
};

//déclaration des événements
globazGlobal.bindEvents = function() {
	if(globazGlobal.isNouveau){
		globazGlobal.calculAjoutSuppressionCotisations();
		globazGlobal.findCompteur( $("#idPosteTravail").val(), $("#anneeDebut").val(),$("#anneeFin").val());
		$("#anneeDebut").change(function () {
			if(isNaN($("#anneeDebut").val())){
				$("#anneeDebut").val("");
			}
			globazGlobal.services.launchForPeriodeChange();
		});
		$("#anneeFin").change(function () {
			if(isNaN($("#anneeFin").val())){
				$("#anneeFin").val("");
			}
			globazGlobal.services.launchForPeriodeChange();
		});
		$("#idPosteTravail").change(function () {
			var idPosteTravail = globazGlobal.getIdPosteTravail();
			globazGlobal.loadNumeroCompte();
			globazGlobal.calculAjoutSuppressionCotisations();
			$("#salaireNonDeclare").val("0.00");
			$("#dateSalaireNonDeclare").val("");
			globazGlobal.services.launchForPosteChange();
			globazGlobal.changeCompteursLink(idPosteTravail);
		});
		
		$("#salaireNonDeclare").change(function () {
			globazGlobal.calculMontants();
		});
		
		$("#beneficiaire").change(function () {
			globazGlobal.loadNumeroCompte();
			globazGlobal.calculAjoutSuppressionCotisations();
			if (globazGlobal.wantCotisations) {
				globazGlobal.rechercheEtAfficheCotisations();
			}
		});
	}
};

//Changement du bénéficiaire -> si électricien + mensuel et ben travailleur/autre maj mustSubstract
globazGlobal.calculAjoutSuppressionCotisations = function() {
	var options = {
			serviceClassName:globazGlobal.prestationsViewService,
			serviceMethodName:'mustSubstractCotisations',
			parametres:$("#idPosteTravail").val()+','+$("#beneficiaire").val(),
			callBack:function (data) {
				globazGlobal.mustSubstractCotisations = data;
				globazGlobal.findCompteur( $("#idPosteTravail").val(), $("#anneeDebut").val(),$("#anneeFin").val());
				globazGlobal.calculMontants();
			}
	};
	vulpeculaUtils.lancementService(options);
};

//Calculs des montants dans le cas d'un affichage
globazGlobal.calculMontantsPourRetrieve = function() {
	var options = {
			serviceClassName:globazGlobal.prestationsViewService,
			serviceMethodName:'mustSubstractCotisations',
			parametres:$("#idPosteTravail").val()+','+$("#beneficiaire").val(),
			callBack:function (data) {
				globazGlobal.mustSubstractCotisations = data;
				globazGlobal.calculMontants();
			}
	};
	vulpeculaUtils.lancementService(options);
	
};

globazGlobal.loadNumeroCompte = function() {
	var options = {
			serviceClassName:globazGlobal.prestationsViewService,
			serviceMethodName:'getNumeroCompteForPoste',
			parametres: globazGlobal.getIdPosteTravail() + ',' + $("#beneficiaire").val() + ',',
			callBack:function (data) {
				$('#adressePaiementEmployeur').text(data);
			}
	};
	vulpeculaUtils.lancementService(options);
};

//validation des champs
globazGlobal.validate = (function() {
	function checkDates() {
		var messages = [];
		var anneeDebut = $('#anneeDebut').val();
		var anneeFin = $('#anneeFin').val();
		
		if(anneeDebut.length == 0) {
			messages.push(globazGlobal.messageAnneeDebutNonVide);
		}
		
		if(anneeFin.length == 0) {
			messages.push(globazGlobal.messageAnneeFinNonVide);
		}
		
		if(anneeDebut > anneeFin) {
			messages.push(globazGlobal.messageAnneeFinPlusGrandAnneeDebut);
		}
		
		return messages;
	}
	
	function checkSalaireNonDeclare() {
		var messages = [];
		var salaireNonDeclare = globazNotation.utilsFormatter.amountTofloat($('#salaireNonDeclare').val());
		var dateSalaireNonDeclare = getDateFromFormat($('#dateSalaireNonDeclare').val(),'dd.MM.yyyy');
		
		if(salaireNonDeclare > 0 && dateSalaireNonDeclare == 0) {
			messages.push(globazGlobal.messageDateRequise);
		}
		return messages;
	}
	
	return function() {
		var messages = checkDates();
		messages = messages.concat(checkSalaireNonDeclare());
		
		if(messages.length==0) {
			return true;
		} else {
			showErrorDialog(messages);
			return false;
		}
	};
})();

function add () {
	document.forms[0].elements('userAction').value="vulpecula.congepaye.congepaye.afficher&_method=_add";
}

function upd() {
}

function validate() {
	if (globazGlobal.wantCotisations) {
		var listeCotisations = JSON.stringify(globazGlobal.listeCotisations);
		$("#listeCotisations").val(listeCotisations);
	}
	
	state = globazGlobal.validate();
	if (document.forms[0].elements('_method').value == "add") {
	     document.forms[0].elements('userAction').value="vulpecula.congepaye.congepaye.ajouter";
	 } else {
	     document.forms[0].elements('userAction').value="vulpecula.congepaye.congepaye.modifier";
	 }
	 return state;
}

function cancel() {
}

function del() {
	var message = jQuery.i18n.prop("ajax.deleteMessage");
	if(confirm(message)) {
		document.forms[0].elements('userAction').value="vulpecula.congepaye.congepaye.supprimer";
		document.forms[0].submit();
	}
}

function init(){
	globazGlobal.loadNumeroCompte();
}