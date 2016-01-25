/**
 *
 * Idem document ready
 * 
 */
$(document).ready(function() {

	if( $('#revenuFullComplex\\.simpleRevenu\\.typeRevenu').val()==ID_TYPE_REVENU_SOURCIER){
		_revenuPrisEnCompte = getFields('revenuPrisEnCompte');
		_totalRevenuImposable = getFields('totalRevenuImposable');
		_cotisationAvsAiApg = getFields("cotisationAvsAiApg");
		_cotisationAc = getFields("cotisationAc");
		_cotisationAcSupplementaires = getFields("cotisationAcSupplementaires");
		_primesAANP = getFields("primesAANP");
		_primesLPP = getFields("primesLPP");
		_deductionAssurances = getFields("deductionAssurances");
		_deductionAssurancesEnfant = getFields("deductionAssurancesEnfant");
		_deductionAssurancesJeunes = getFields("deductionAssurancesJeunes");
		_deductionEnfants = getFields("deductionEnfants");
		_deductionFraisObtention = getFields("deductionFraisObtention");
		_deductionDoubleGain = getFields("deductionDoubleGain");
		
		collectDatasSourcierSimulation();
	}
	

	// Réaction sur année taxation
	// ----------------------------------------------------------------------
	$('#anneeTaxation').change(function (e){
		// mise à jour anneerefvisu
		$('#anneeRefVisu').val($('#anneeTaxation').val());
		if($('#anneeTaxation').val().length>3){
			if( $('#revenuFullComplex\\.simpleRevenu\\.typeRevenu').val()==ID_TYPE_REVENU_SOURCIER){
					collectDatasSourcier();
			}
		}
	});
	// Réaction sur type revenu (sourcier-standard)
	// ----------------------------------------------------------------------
	$('#revenuFullComplex\\.simpleRevenu\\.typeRevenu').change(function(){
		if( $('#revenuFullComplex\\.simpleRevenu\\.typeRevenu').val()==ID_TYPE_REVENU_SOURCIER){
			if($('#anneeTaxation').val().length>3){
				collectDatasSourcier();
			}
		}
	});
	// Réaction sur etat civil
	// ----------------------------------------------------------------------
	$('#revenuFullComplex\\.simpleRevenu\\.etatCivil').change(function(){
		if( $('#revenuFullComplex\\.simpleRevenu\\.typeRevenu').val()==ID_TYPE_REVENU_SOURCIER){
			if($('#anneeTaxation').val().length>3){
				collectDatasSourcier();
			}
		}
	});
	// Réaction sur le nombre d'enfants
	// ----------------------------------------------------------------------
	$('#nbEnfants').change(function (e){	
		if( $('#revenuFullComplex\\.simpleRevenu\\.typeRevenu').val()==ID_TYPE_REVENU_SOURCIER){
			if($('#anneeTaxation').val().length>3 && $('#nbEnfants').val().length>0){
				collectDatasSourcier();
			}
		} else if( $('#revenuFullComplex\\.simpleRevenu\\.typeRevenu').val()==ID_TYPE_REVENU_CONTRIBUABLE){
			if($('#anneeTaxation').val().length>3 && $('#nbEnfants').val().length>0){					
				collectDatasStandard();
			}
		}
	});
	$('#nbEnfantSuspens').change(function (e){	
		if( $('#revenuFullComplex\\.simpleRevenu\\.typeRevenu').val()==ID_TYPE_REVENU_SOURCIER){
			if($('#anneeTaxation').val().length>3 && $('#nbEnfantSuspens').val().length>0){
				collectDatasSourcier();
			}
		} else if( $('#revenuFullComplex\\.simpleRevenu\\.typeRevenu').val()==ID_TYPE_REVENU_CONTRIBUABLE){
			if($('#anneeTaxation').val().length>3 && $('#nbEnfantSuspens').val().length>0){					
				collectDatasStandard();
			}
		}
	});

	// Réaction sur le nombre d'enfants, on set le champs à 0 si vide et on recalcule (changement de cellule)
	// ----------------------------------------------------------------------
	$('#nbEnfants').change(function (e){	
		if ($('#nbEnfants').val().length==0) {
			$('#nbEnfants').val("0");
			$('#nbEnfants').trigger("keyup");
		}		
	});
	$('#nbEnfantSuspens').change(function (e){	
		if ($('#nbEnfantSuspens').val().length==0) {
			$('#nbEnfantSuspens').val("0");
			$('#nbEnfantSuspens').trigger("keyup");
		}		
	});
	// Réaction sur le montant du revenu annuel epoux (changement de cellule?)
	// ----------------------------------------------------------------------
	$('#revenuEpouxAnnuel').change(function (e){
		if( $('#revenuFullComplex\\.simpleRevenu\\.typeRevenu').val()==ID_TYPE_REVENU_SOURCIER){
			if($('#anneeTaxation').val().length>3){
				collectDatasSourcier();
			}
		}
	});
	// Réaction sur le montant du revenu mensuel epoux (changement de cellule?)
	// ----------------------------------------------------------------------
	$('#revenuEpouxMensuel').change(function (e){
		if( $('#revenuFullComplex\\.simpleRevenu\\.typeRevenu').val()==ID_TYPE_REVENU_SOURCIER){
			if($('#anneeTaxation').val().length>3){
				collectDatasSourcier();
			}
		}
	});
	// Réaction sur le montant du revenu annuel epouse (changement de cellule?)
	// ----------------------------------------------------------------------
	$('#revenuEpouseAnnuel').change(function (e){
		if( $('#revenuFullComplex\\.simpleRevenu\\.typeRevenu').val()==ID_TYPE_REVENU_SOURCIER){
			if($('#anneeTaxation').val().length>3){
				collectDatasSourcier();
			}
		}
	});
	// Réaction sur le montant du revenu mensuel epouse (changement de cellule?)
	// ----------------------------------------------------------------------
	$('#revenuEpouseMensuel').change(function (e){
		if( $('#revenuFullComplex\\.simpleRevenu\\.typeRevenu').val()==ID_TYPE_REVENU_SOURCIER){
			if($('#anneeTaxation').val().length>3){
				collectDatasSourcier();
			}
		}
	});
	// Réaction sur le nombre de mois
	// ----------------------------------------------------------------------
	$('#nombreMois').change(function (e){
		if( $('#revenuFullComplex\\.simpleRevenu\\.typeRevenu').val()==ID_TYPE_REVENU_SOURCIER){
			if($('#anneeTaxation').val().length>3){
				collectDatasSourcier();
			}
		}
	});
});

/**
 * 
 * collecte les données de la page actuelle pour calculer les déductions sourcier 
 * 
 */
function collectDatasSourcierSimulation() {
	differenceDBCalcul = false;

	var a_params = new Array(
			"anneeTaxation:"+$("#anneeTaxation").val(),
			"idContribuable:"+$("#idContribuable").val(),
			"etatCivil:"+$('#revenuFullComplex\\.simpleRevenu\\.etatCivil').val(),
			"nbEnfants:"+$('#nbEnfants').val(),
			"revenuAnnuelEpoux:"+$('#revenuEpouxAnnuel').val(),
			"revenuAnnuelEpouse:"+$('#revenuEpouseAnnuel').val(),
			"revenuMensuelEpoux:"+$('#revenuEpouxMensuel').val(),
			"revenuMensuelEpouse:"+$('#revenuEpouseMensuel').val(),
			"nbMois:"+$('#nombreMois').val()
	);
	
	for(i=0;i<a_params.length;i++) {
		a_params[i] = a_params[i].replace(/\'/g,"");
	}		
	

	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.models.revenu.RevenuCalculService',
		serviceMethodName:'calculDeductionSourcier',
		parametres:'['+a_params+']',
		callBack: afterSimu
	}

	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}

/**
 * 
 * collecte les données de la page actuelle pour calculer les déductions sourcier 
 * 
 */
function collectDatasSourcier() {
	differenceDBCalcul = false;
	$("#imgDiffCalcDb").css("display","none");

	var a_params = new Array(
			"anneeTaxation:"+$("#anneeTaxation").val(),
			"idContribuable:"+$("#idContribuable").val(),
			"etatCivil:"+$('#revenuFullComplex\\.simpleRevenu\\.etatCivil').val(),
			"nbEnfants:"+$('#nbEnfants').val(),
			"revenuAnnuelEpoux:"+$('#revenuEpouxAnnuel').val(),
			"revenuAnnuelEpouse:"+$('#revenuEpouseAnnuel').val(),
			"revenuMensuelEpoux:"+$('#revenuEpouxMensuel').val(),
			"revenuMensuelEpouse:"+$('#revenuEpouseMensuel').val(),
			"nbMois:"+$('#nombreMois').val()
	);
	
	for(i=0;i<a_params.length;i++) {
		a_params[i] = a_params[i].replace(/\'/g,"");
	}		
	

	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.models.revenu.RevenuCalculService',
		serviceMethodName:'calculDeductionSourcier',
		parametres:'['+a_params+']',
		callBack: refreshAllFields
	}

	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}

/**
 * 
 * collecte les données de la page actuelle pour calculer les déductions sourcier 
 * 
 */
function collectDatasStandard() {
	var a_params = new Array(
			"anneeTaxation:"+$("#anneeTaxation").val(),
			"idContribuable:"+$("#idContribuable").val(),				
			"nbEnfants:"+$('#nbEnfants').val(),
			"nbEnfantsSuspens:"+$('#nbEnfantSuspens').val(),
			"revenuImposable:"+$("#revenuImposable").val(),			
			"revenuTaux:"+$("#revenuTaux").val(),
			"persChargeEnf:"+$("#persChargeEnf").val()
	);
	
	for(i=0;i<a_params.length;i++) {
		a_params[i] = a_params[i].replace(/\'/g,"");
	}		
	

	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.models.revenu.RevenuCalculService',
		serviceMethodName:'calculDeductionStandard',
		parametres:'['+a_params+']',
		callBack: refreshAllFieldsStandard
	}

	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}

/**
 * Refresh all fields for sourcier
 * 
 * @param data
 */
function afterSimu(data){
	if (compareAmountTolerance(data.simpleRevenuSourcier.revenuPrisEnCompte, _revenuPrisEnCompte, 1)) {
		fillFields('revenuPrisEnCompte',data.simpleRevenuSourcier.revenuPrisEnCompte, _revenuPrisEnCompte);
	} else {
		fillFields('revenuPrisEnCompte',data.simpleRevenuSourcier.revenuPrisEnCompte);
	}
	
	if (compareAmountTolerance(data.simpleRevenuSourcier.revenuImposable, _totalRevenuImposable, 1)) {
		fillFields('totalRevenuImposable',data.simpleRevenuSourcier.revenuImposable, _totalRevenuImposable);
		$("#imgDiffCalcDb").css("display","inline");
	} else {
		fillFields('totalRevenuImposable',data.simpleRevenuSourcier.revenuImposable);
	}
		
	if (compareAmountTolerance(data.simpleRevenuSourcier.cotisationAvsAiApg, _cotisationAvsAiApg, 1)) {
		fillFields("cotisationAvsAiApg", data.simpleRevenuSourcier.cotisationAvsAiApg, _cotisationAvsAiApg);
	} else {
		fillFields("cotisationAvsAiApg", data.simpleRevenuSourcier.cotisationAvsAiApg);
	}
	
	if (compareAmountTolerance(data.simpleRevenuSourcier.cotisationAc, _cotisationAc, 1)) {
		fillFields("cotisationAc", data.simpleRevenuSourcier.cotisationAc, _cotisationAc);
	} else {
		fillFields("cotisationAc", data.simpleRevenuSourcier.cotisationAc);
	}
	
	if (compareAmountTolerance(data.simpleRevenuSourcier.cotisationAcSupplementaires, _cotisationAcSupplementaires, 1)) {
		fillFields("cotisationAcSupplementaires", data.simpleRevenuSourcier.cotisationAcSupplementaires, _cotisationAcSupplementaires);
	} else {
		fillFields("cotisationAcSupplementaires", data.simpleRevenuSourcier.cotisationAcSupplementaires);
	}
	
	if (compareAmountTolerance(data.simpleRevenuSourcier.primesAANP, _primesAANP, 1)) {
		fillFields("primesAANP", data.simpleRevenuSourcier.primesAANP, _primesAANP);
	} else {
		fillFields("primesAANP", data.simpleRevenuSourcier.primesAANP);
	}
		
	if (compareAmountTolerance(data.simpleRevenuSourcier.primesLPP, _primesLPP, 1)) {
		fillFields("primesLPP", data.simpleRevenuSourcier.primesLPP, _primesLPP);
	} else {
		fillFields("primesLPP", data.simpleRevenuSourcier.primesLPP);
	}

	if (compareAmountTolerance(data.simpleRevenuSourcier.deductionAssurances, _deductionAssurances, 1)) {
		fillFields("deductionAssurances", data.simpleRevenuSourcier.deductionAssurances, _deductionAssurances);
	} else {
		fillFields("deductionAssurances", data.simpleRevenuSourcier.deductionAssurances);
	}	
	
	if (compareAmountTolerance(data.simpleRevenuSourcier.deductionAssurancesEnfant, _deductionAssurancesEnfant, 1)) {
		fillFields("deductionAssurancesEnfant", data.simpleRevenuSourcier.deductionAssurancesEnfant, _deductionAssurancesEnfant);
	} else {
		fillFields("deductionAssurancesEnfant", data.simpleRevenuSourcier.deductionAssurancesEnfant);
	}
	
	if (compareAmountTolerance(data.simpleRevenuSourcier.deductionAssurancesJeunes, _deductionAssurancesJeunes, 1)) {
		fillFields("deductionAssurancesJeunes", data.simpleRevenuSourcier.deductionAssurancesJeunes, _deductionAssurancesJeunes);
	} else {
		fillFields("deductionAssurancesJeunes", data.simpleRevenuSourcier.deductionAssurancesJeunes);
	}
	
	if (compareAmountTolerance(data.simpleRevenuSourcier.deductionEnfants, _deductionEnfants, 1)) {
		fillFields("deductionEnfants", data.simpleRevenuSourcier.deductionEnfants, _deductionEnfants);
	} else {
		fillFields("deductionEnfants", data.simpleRevenuSourcier.deductionEnfants);
	}
	
	if (compareAmountTolerance(data.simpleRevenuSourcier.deductionFraisObtention, _deductionFraisObtention, 1)) {
		fillFields("deductionFraisObtention", data.simpleRevenuSourcier.deductionFraisObtention, _deductionFraisObtention);
	} else {
		fillFields("deductionFraisObtention", data.simpleRevenuSourcier.deductionFraisObtention);
	}
	
	if (compareAmountTolerance(data.simpleRevenuSourcier.deductionDoubleGain, _deductionDoubleGain, 1)) {
		fillFields("deductionDoubleGain", data.simpleRevenuSourcier.deductionDoubleGain, _deductionDoubleGain);	
	} else {
		fillFields("deductionDoubleGain", data.simpleRevenuSourcier.deductionDoubleGain);
	}
}

/**
 * Refresh all fields for sourcier
 * 
 * @param data
 */
function refreshAllFields(data){
	fillFields('revenuPrisEnCompte',data.simpleRevenuSourcier.revenuPrisEnCompte);
	fillFields('totalRevenuImposable',data.simpleRevenuSourcier.revenuImposable);
	fillFields("cotisationAvsAiApg", data.simpleRevenuSourcier.cotisationAvsAiApg);
	fillFields("cotisationAc", data.simpleRevenuSourcier.cotisationAc);
	fillFields("cotisationAcSupplementaires", data.simpleRevenuSourcier.cotisationAcSupplementaires);
	fillFields("primesAANP", data.simpleRevenuSourcier.primesAANP);
	fillFields("primesLPP", data.simpleRevenuSourcier.primesLPP);
	fillFields("deductionAssurances", data.simpleRevenuSourcier.deductionAssurances);
	fillFields("deductionAssurancesEnfant", data.simpleRevenuSourcier.deductionAssurancesEnfant);
	fillFields("deductionAssurancesJeunes", data.simpleRevenuSourcier.deductionAssurancesJeunes);
	fillFields("deductionEnfants", data.simpleRevenuSourcier.deductionEnfants);
	fillFields("deductionFraisObtention", data.simpleRevenuSourcier.deductionFraisObtention);
	fillFields("deductionDoubleGain", data.simpleRevenuSourcier.deductionDoubleGain);
}

/**
 * Compare 2 nombre <br>
 * 
 * @param nb1
 * @param nb2
 * @param tolerance
 * @returns false si les nombre sont égaux avec une tolérance de +/- [tolerance]
 */
function compareAmountTolerance(nb1, nb2, tolerance) {
	if (tolerance == undefined || isNaN(tolerance)) {
		tolerance = 0;
	}
	
	if (nb1 == undefined || isNaN(nb1)) {
		nb1 = 0;
	}
	if (nb2 == undefined || isNaN(nb2)) {
		nb2 = 0;
	}
	
	var difference = parseInt(nb1) - parseInt(nb2);
	var tolerance_neg = parseInt(tolerance) * -1;
//	alert(nb1+" / "+nb2+" / "+difference);
	if (difference <= tolerance && difference >= tolerance_neg) {		
		return false;
	}
	return true;
}
/**
 * Refresh all fields for standard
 * 
 * @param data
 */
function refreshAllFieldsStandard(data){
	$("#persChargeEnf").val(formatNumber(data.simpleRevenuContribuable.persChargeEnf,true));
	
	if (data.simpleRevenuContribuable.revenuImposable != undefined) {
		$("#revenuImposable").val(formatNumber(data.simpleRevenuContribuable.revenuImposable,true));
	}
	if (data.simpleRevenuContribuable.revenuTaux != undefined) {
		$("#revenuTaux").val(formatNumber(data.simpleRevenuContribuable.revenuTaux,true));
	}
}

/**
 * Utilitaire, renseigne les champs visibles
 * et les champs hidden
 * 
 * @param s_fieldName
 * @param s_value
 */
function fillFields(s_fieldName, s_value) {
	fillFields(s_fieldName, s_value, null);
}

/**
 * Utilitaire, renseigne les champs visibles
 * et les champs hidden
 * 
 * @param s_fieldName
 * @param s_value
 * @param s_title
 */
function fillFields(s_fieldName, s_value, s_title) {
	$("#"+s_fieldName).html(formatNumber(s_value,true));
	if (s_title != null) {
		$("#"+s_fieldName).css('color','red');
		$("#"+s_fieldName).attr('title','Valeur en base de donnée : '+s_title);
	} else {
		$("#"+s_fieldName).css('color','');
	}
	$("#hidden_"+s_fieldName).val(formatNumber(s_value));
}

function getFields(s_fieldName) {
	return $("#"+s_fieldName).html().replace(/\'/g,"");
}

/**
 * Formattage des numériques ?
 * 
 * @param nStr
 * @param wantBlankIfDecimalZero
 * @returns
 */
function formatNumber(nStr, wantBlankIfDecimalZero)
{
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + '\'' + '$2');
	}

	if (wantBlankIfDecimalZero) {
		return x1 + ".00";
	} else {
		return x1 + x2;
	}
}




	
