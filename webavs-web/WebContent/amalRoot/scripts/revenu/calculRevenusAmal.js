/*
 * Scripts pour la gestion des revenus
 * 
 * 2 modes de fonctionnement :
 * 
 * 1 - Création
 * 		a) Appel des taxations disponibles pour l'année historique introduite
 * 		b) Mise à jour de la liste déroulante
 * 		c) Mise à jour des champs taxations en fonction du choix de la liste déroulante
 * 		d) Mise à jour du calcul du revenu déterminant en fonction du choix de la liste déroulante
 *  
 * 2 - Modification (sélection d'une autre taxation)
 * 		a) Mise à jour des champs de taxations en fonction du choix de la liste déroulante
 * 		b) Mise à jour des champs de taxations en fonction du choix de la liste déroulante
 * 		c) Mise à jour du calcul du revenu déterminant en fonction du choix de la liste déroulante 
 *
 * 
 * Pour le point 1-a, appel du service DetailFamilleService.getListTaxations
 * Pour le point 1-c, 2-b, appel du service RevenuService.readRevenuFullComplex
 * Pour le point 1-d, 2-c, appel du service RevenuCalculService.calculRDet 
 * 
 */




/*
 * Scripts pour les calculs de revenus
 * CBU, 03.2011
 */

// identique document.ready
$(function() {
	
	// Détection de changement dans les input des div affichées
	// mise à jour d'une bordure orange pour dire les inputs changées
	$("#idConteneurImpotsSource :input, #idConteneurDeclarationImpots :input, #idConteneurGenericRevenu :input").change(function() {
		var s = jQuery($(this));
		var isSelect=false;
		if (s.is('select')) {
			isSelect=true;
		}
		if (!isSelect) {
			bef = this.defaultValue.replace(/'/g,'');
			aft = this.value.replace(/'/g,'');	
			if (bef != aft) {
				$(this).addClass('inputOldValue');
				$(this).attr('title','Ancienne valeur : '+this.defaultValue);
			} else {
				$(this).removeClass('inputOldValue');
				$(this).removeAttr('title');
			}
		}
	});
	
	// Réaction pour un changement sur la combo box de taxations
	$("#taxationLiee").change(function() {
		collectDatas();
	});

	// Réaction sur la saisie d'une année Historique
	$('#anneeHistorique').keyup(function (e){
		if($('#anneeHistorique').val().length>3){
			getListTaxations();
		}
	});
	
});

/**
 * Utilitaire : Affiche le tab correct en fonction du type revenu
 */
function showTabTypeRevenu(selected) {
	//si sourcier
	if (selected == ID_TYPE_REVENU_SOURCIER) {
		$("#ongletStandard").hide();
		$("#ongletStandard").css("");
		$("#ongletSourciers").show();
	} else {
		$("#ongletStandard").show();
		$("#ongletSourciers").hide();
	}
}


/**
 * Adapte le lien sur la taxation donnée
 */
function adaptLinkTaxation(){
	var s_contribuableId = $('#idContribuable').val();
	var s_revenuId = $('#taxationLiee').val();

	var s_newValue = '';
	s_newValue += s_servletContext;
	s_newValue += "/amal?userAction=amal.revenu.revenu.afficher&selectedId=";
	s_newValue += s_revenuId;
	s_newValue += "&contribuableId=";
	s_newValue += s_contribuableId;
	s_newValue += "&selectedTab=0";
	
	
	$('#taxationLieeURL').removeProp('href');
	$('#taxationLieeURL').prop('href',s_newValue);
	$('#taxationLieeImg').prop('title','Taxation '+$('#taxationLiee option[value='+s_revenuId+']').text());
}
/**
 * 
 * collecte les données de la page actuelle pour recalculer 
 * le revenu déterminant
 * 
 */
function collectDatas() {
	// Set the revenu id in all case
	$("#idRevenu").val($("#taxationLiee").val());

	var a_params = new Array(
			"taxationID:"+$("#taxationLiee").val(),
			$("#anneeHistorique").attr("id")+":"+$("#anneeHistorique").val()
	);
	
	for(i=0;i<a_params.length;i++) {
		a_params[i] = a_params[i].replace(/\'/g,"");
	}		
	

	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.models.revenu.RevenuCalculService',
		serviceMethodName:'calculRDet',
		parametres:'['+a_params+']',
		callBack: refreshAllFields
	}

	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}

/**
 * Refresh all fields
 * 
 * @param data
 */
function refreshAllFields(data){
	adaptLinkTaxation();
	refreshZoneCalcul(data);
	refreshZoneGenerique(data);
	if("42002601"==data.revenuFullComplex.simpleRevenu.typeRevenu){
		$("#ongletStandard").hide();
		$('#idConteneurDeclarationImpots').hide();
		$("#ongletSourciers").show();
		$('#idConteneurImpotsSource').show();
		refreshZoneSourcier(data);
	}else{
		$("#ongletSourciers").hide();
		$('#idConteneurImpotsSource').hide();
		$("#ongletStandard").show();
		$('#idConteneurDeclarationImpots').show();
		refreshZoneContribuable(data);
	}
}

/**
 * Fonction de callback, appelé en réponse au service
 * calculRDet
 * 
 * Réaffiche le revenu déterminant (calcul)
 * 
 * @param data
 */
function refreshZoneCalcul(data) {
	// SOURCIER
	fillFields("revenuPrisEnCompteRDET", data.revenuFullComplex.simpleRevenuSourcier.revenuPrisEnCompte);
	fillFields("totalRevenuImposableRDET", data.revenuFullComplex.simpleRevenuSourcier.revenuImposable);
	fillFields("cotisationAvsAiApgRDET", data.revenuFullComplex.simpleRevenuSourcier.cotisationAvsAiApg);
	fillFields("cotisationAcRDET", data.revenuFullComplex.simpleRevenuSourcier.cotisationAc);
	fillFields("cotisationAcSupplementairesRDET", data.revenuFullComplex.simpleRevenuSourcier.cotisationAcSupplementaires);
	fillFields("primesAANPRDET", data.revenuFullComplex.simpleRevenuSourcier.primesAANP);
	fillFields("primesLPPRDET", data.revenuFullComplex.simpleRevenuSourcier.primesLPP);
	fillFields("deductionAssurancesRDET", data.revenuFullComplex.simpleRevenuSourcier.deductionAssurances);
	fillFields("deductionAssurancesEnfantRDET", data.revenuFullComplex.simpleRevenuSourcier.deductionAssurancesEnfant);
	fillFields("deductionAssurancesJeunesRDET", data.revenuFullComplex.simpleRevenuSourcier.deductionAssurancesJeunes);
	fillFields("deductionEnfantsRDET", data.revenuFullComplex.simpleRevenuSourcier.deductionEnfants);
	fillFields("deductionFraisObtentionRDET", data.revenuFullComplex.simpleRevenuSourcier.deductionFraisObtention);
	fillFields("deductionDoubleGainRDET", data.revenuFullComplex.simpleRevenuSourcier.deductionDoubleGain);
	// STANDARD
	fillFields("partRendementImmobExedantIntPassifsCalcul", data.simpleRevenuDeterminant.partRendementImmobExedantIntPassifsCalcul);
	fillFields("perteLiquidationCalcul", data.simpleRevenuDeterminant.perteLiquidationCalcul);
	fillFields("perteReporteeExercicesCommerciauxCalcul", data.simpleRevenuDeterminant.perteReporteeExercicesCommerciauxCalcul);
	fillFields("perteExercicesCommerciauxCalcul", data.simpleRevenuDeterminant.perteExercicesCommerciauxCalcul);
	fillFields("interetsPassifsCalcul", data.simpleRevenuDeterminant.interetsPassifsCalcul);
	fillFields("excedentDepensesSuccNonPartageesCalcul", data.simpleRevenuDeterminant.excedentDepensesSuccNonPartageesCalcul);
	fillFields("excedentDepensesPropImmoCalcul", data.simpleRevenuDeterminant.excedentDepensesPropImmoCalcul);
	fillFields("rendementFortuneImmoCalcul", data.simpleRevenuDeterminant.rendementFortuneImmoCalcul);
	fillFields("revenuImposableCalcul", data.simpleRevenuDeterminant.revenuImposableCalcul);
	fillFields("deductionCouplesMaries", data.simpleRevenuDeterminant.deductionCouplesMaries);
	// COMMUN
	fillFields("fortuneImposableCalcul", data.simpleRevenuDeterminant.fortuneImposableCalcul);
	fillFields("fortuneImposablePercentCalcul", data.simpleRevenuDeterminant.fortuneImposablePercentCalcul);
	fillFields("deductionSelonNbreEnfantCalcul", data.simpleRevenuDeterminant.deductionSelonNbreEnfantCalcul);
	fillFields("deductionContribAvecEnfantChargeCalcul", data.simpleRevenuDeterminant.deductionContribAvecEnfantChargeCalcul);
	fillFields("deductionContribNonCelibSansEnfantChargeCalcul", data.simpleRevenuDeterminant.deductionContribNonCelibSansEnfantChargeCalcul);
	fillFields("revenuDeterminantCalcul", data.simpleRevenuDeterminant.revenuDeterminantCalcul);	
	var sommeEnfants = parseFloat(data.revenuFullComplex.simpleRevenu.nbEnfants)+(parseFloat(data.revenuFullComplex.simpleRevenu.nbEnfantSuspens)/2);
	$("#nbEnfantsCalcul").html(sommeEnfants);
}

/**
 * Refresh de la zone revenu générique
 * 
 * @param data
 */
function refreshZoneGenerique(data){
	var typeRevenu = data.revenuFullComplex.simpleRevenu.typeRevenu;
	$('#revenuHistoriqueComplex\\.revenuFullComplex\\.simpleRevenu\\.typeRevenu option[value='+typeRevenu+']').prop('selected','selected');
	var codeSuspendu = data.revenuFullComplex.simpleRevenu.codeSuspendu;
	$('#codeSuspendu option[value='+codeSuspendu+']').prop('selected','selected');
	var typeTaxation = data.revenuFullComplex.simpleRevenu.typeTaxation;
	$('#revenuHistoriqueComplex\\.revenuFullComplex\\.simpleRevenu\\.typeTaxation option[value='+typeTaxation+']').prop('selected','selected');
	$('#noLotAvisTaxation').val(data.revenuFullComplex.simpleRevenu.noLotAvisTaxation);
	var etatCivil = data.revenuFullComplex.simpleRevenu.etatCivil;
	$('#revenuHistoriqueComplex\\.revenuFullComplex\\.simpleRevenu\\.etatCivil option[value='+etatCivil+']').prop('selected','selected');
	$('#nbJours').val(data.revenuFullComplex.simpleRevenu.nbJours);
	$('#nbEnfants').val(data.revenuFullComplex.simpleRevenu.nbEnfants);
	$('#nbEnfantSuspens').val(data.revenuFullComplex.simpleRevenu.nbEnfantSuspens);
	var codeProfession = data.revenuFullComplex.simpleRevenu.profession;
	$('#revenuHistoriqueComplex\\.revenuFullComplex\\.simpleRevenu\\.profession option[value='+codeProfession+']').prop('selected','selected');
	$('#revDetUnique').val(data.revenuFullComplex.simpleRevenu.revDetUnique);
	var revDetUniqueOuiNon = data.revenuFullComplex.simpleRevenu.revDetUniqueOuiNon;
	$('#revDetUniqueOuiNon option[value='+revDetUniqueOuiNon+']').prop('selected','selected');
	var codeActif = data.revenuFullComplex.simpleRevenu.codeActif;
	$('#codeActif option[value='+codeActif+']').prop('selected','selected');
	$('#dateAvisTaxation').val(data.revenuFullComplex.simpleRevenu.dateAvisTaxation);
	$('#dateTraitement').val(data.revenuFullComplex.simpleRevenu.dateTraitement);
	$('#anneeTaxation').val(data.revenuFullComplex.simpleRevenu.anneeTaxation);
	$('#dateSaisie').val(data.revenuFullComplex.simpleRevenu.dateSaisie);
}

/**
 * Cache les champs de la zone générique
 */
function hideFieldsZoneGenerique(){
	$('#revenuHistoriqueComplex\\.revenuFullComplex\\.simpleRevenu\\.typeRevenu').hide();
	$('#codeSuspendu').hide();
	$('#revenuHistoriqueComplex\\.revenuFullComplex\\.simpleRevenu\\.typeTaxation').hide();
	$('#noLotAvisTaxation').hide();
	$('#revenuHistoriqueComplex\\.revenuFullComplex\\.simpleRevenu\\.etatCivil').hide();
	$('#nbJours').hide();
	$('#nbEnfants').hide();
	$('#nbEnfantSuspens').hide();
	$('#revenuHistoriqueComplex\\.revenuFullComplex\\.simpleRevenu\\.profession').hide();
	$('#revDetUnique').hide();
	$('#revDetUniqueOuiNon').hide();
	$('#codeActif').hide();
	$('#dateAvisTaxation').hide();
	$('#dateTraitement').hide();
	$('#anneeTaxation').hide();
	$('#dateSaisie').hide();
}

/**
 * Affiche les champs de la zone générique
 */
function showFieldsZoneGenerique(){
	$('#revenuHistoriqueComplex\\.revenuFullComplex\\.simpleRevenu\\.typeRevenu').show();
	$('#codeSuspendu').show();
	$('#revenuHistoriqueComplex\\.revenuFullComplex\\.simpleRevenu\\.typeTaxation').show();
	$('#noLotAvisTaxation').show();
	$('#revenuHistoriqueComplex\\.revenuFullComplex\\.simpleRevenu\\.etatCivil').show();
	$('#nbJours').show();
	$('#nbEnfants').show();
	$('#nbEnfantSuspens').show();
	$('#revenuHistoriqueComplex\\.revenuFullComplex\\.simpleRevenu\\.profession').show();
	$('#revDetUnique').show();
	$('#revDetUniqueOuiNon').show();
	$('#codeActif').show();
	$('#dateAvisTaxation').show();
	$('#dateTraitement').show();
	$('#anneeTaxation').show();
	$('#dateSaisie').show();
}

/**
 * Refresh de la zone revenu contribuable
 * 
 * @param data
 */
function refreshZoneContribuable(data){
	$('#allocationFamiliale').val(data.revenuFullComplex.simpleRevenuContribuable.allocationFamiliale);
	$('#perteActIndep').val(data.revenuFullComplex.simpleRevenuContribuable.perteActIndep);
	$('#revenuNetEmploi').val(data.revenuFullComplex.simpleRevenuContribuable.revenuNetEmploi);
	$('#perteActAgricole').val(data.revenuFullComplex.simpleRevenuContribuable.perteActAgricole);
	$('#revenuNetEpouse').val(data.revenuFullComplex.simpleRevenuContribuable.revenuNetEpouse);
	$('#perteSociete').val(data.revenuFullComplex.simpleRevenuContribuable.perteSociete);
	$('#indemniteImposable').val(data.revenuFullComplex.simpleRevenuContribuable.indemniteImposable);
	$('#perteActAccInd').val(data.revenuFullComplex.simpleRevenuContribuable.perteActAccInd);
	$('#rendFortImmobPrive').val(data.revenuFullComplex.simpleRevenuContribuable.rendFortImmobPrive);
	$('#excDepSuccNp').val(data.revenuFullComplex.simpleRevenuContribuable.excDepSuccNp);
	$('#rendFortImmobComm').val(data.revenuFullComplex.simpleRevenuContribuable.rendFortImmobComm);
	$('#persChargeEnf').val(data.revenuFullComplex.simpleRevenuContribuable.persChargeEnf);
	$('#totalRevenusNets').val(data.revenuFullComplex.simpleRevenuContribuable.totalRevenusNets);
	$('#deducAppEtu').val(data.revenuFullComplex.simpleRevenuContribuable.deducAppEtu);
	$('#deducCouplesMaries').val(data.revenuFullComplex.simpleRevenuContribuable.deductionCouplesMaries);
	$('#interetsPassifsPrive').val(data.revenuFullComplex.simpleRevenuContribuable.interetsPassifsPrive);
	$('#revenuImposable').val(data.revenuFullComplex.simpleRevenuContribuable.revenuImposable);
	$('#interetsPassifsComm').val(data.revenuFullComplex.simpleRevenuContribuable.interetsPassifsComm);
	$('#revenuTaux').val(data.revenuFullComplex.simpleRevenuContribuable.revenuTaux);
	$('#excedDepPropImmoPriv').val(data.revenuFullComplex.simpleRevenuContribuable.excedDepPropImmoPriv);
	$('#fortuneImposable').val(data.revenuFullComplex.simpleRevenuContribuable.fortuneImposable);
	$('#excedDepPropImmoComm').val(data.revenuFullComplex.simpleRevenuContribuable.excedDepPropImmoComm);
	$('#fortuneTaux').val(data.revenuFullComplex.simpleRevenuContribuable.fortuneTaux);
	$('#perteLiquidation').val(data.revenuFullComplex.simpleRevenuContribuable.perteLiquidation);
	$('#perteCommercial').val(data.revenuFullComplex.simpleRevenuContribuable.perteCommercial);
	// HIDE FIELD RDET sourcier and show standrad
	$('#revenuPrisEnCompteRDET_LINE').hide();
	$('#totalRevenuImposableRDET_LINE').hide();
	$('#cotisationAvsAiApgRDET_LINE').hide();
	$('#cotisationAcRDET_LINE').hide();
	$('#cotisationAcSupplRDET_LINE').hide();
	$('#primesAANPRDET_LINE').hide();
	$('#primesLPPRDET_LINE').hide();
	$('#deductionAssurancesRDET_LINE').hide();
	$('#deductionAssurancesEnfantRDET_LINE').hide();
	$('#deductionAssurancesJeunesRDET_LINE').hide();
	$('#deductionEnfantsRDET_LINE').hide();
	$('#deductionFraisObtentionRDET_LINE').hide();
	$('#deductionDoubleGainRDET_LINE').hide();
	// STANDARD
	$('#partRendementImmobExedantIntPassifsCalcul_LINE').show();
	$('#perteLiquidationCalcul_LINE').show();
	$('#perteReporteeExercicesCommerciauxCalcul_LINE').show();
	$('#perteExercicesCommerciauxCalcul_LINE').show();
	$('#interetsPassifsCalcul_LINE').show();
	$('#excedentDepensesSuccNonPartageesCalcul_LINE').show();
	$('#excedentDepensesPropImmoCalcul_LINE').show();
	$('#rendementFortuneImmoCalcul_LINE').show();
	$('#revenuImposableCalcul_LINE').show();
}
/**
 * Cache the zone contribuable standard 
 */
function hideFieldsZoneContribuable(){
	$('#allocationFamiliale').hide();
	$('#perteActIndep').hide();
	$('#revenuNetEmploi').hide();
	$('#perteActAgricole').hide();
	$('#revenuNetEpouse').hide();
	$('#perteSociete').hide();
	$('#indemniteImposable').hide();
	$('#perteActAccInd').hide();
	$('#rendFortImmobPrive').hide();
	$('#excDepSuccNp').hide();
	$('#rendFortImmobComm').hide();
	$('#persChargeEnf').hide();
	$('#totalRevenusNets').hide();
	$('#deducAppEtu').hide();
	$('#interetsPassifsPrive').hide();
	$('#revenuImposable').hide();
	$('#interetsPassifsComm').hide();
	$('#revenuTaux').hide();
	$('#excedDepPropImmoPriv').hide();
	$('#fortuneImposable').hide();
	$('#excedDepPropImmoComm').hide();
	$('#fortuneTaux').hide();
}
/**
 * Affiche the zone contribuable standard 
 */
function showFieldsZoneContribuable(){
	$('#allocationFamiliale').show();
	$('#perteActIndep').show();
	$('#revenuNetEmploi').show();
	$('#perteActAgricole').show();
	$('#revenuNetEpouse').show();
	$('#perteSociete').show();
	$('#indemniteImposable').show();
	$('#perteActAccInd').show();
	$('#rendFortImmobPrive').show();
	$('#excDepSuccNp').show();
	$('#rendFortImmobComm').show();
	$('#persChargeEnf').show();
	$('#totalRevenusNets').show();
	$('#deducAppEtu').show();
	$('#interetsPassifsPrive').show();
	$('#revenuImposable').show();
	$('#interetsPassifsComm').show();
	$('#revenuTaux').show();
	$('#excedDepPropImmoPriv').show();
	$('#fortuneImposable').show();
	$('#excedDepPropImmoComm').show();
	$('#fortuneTaux').show();
}

/**
 * Refresh de la zone revenu sourcier
 * 
 * @param data
 */
function refreshZoneSourcier(data){
	//ZONE DATA SOURCIER
	$('#anneeRefVisu').val(data.revenuFullComplex.simpleRevenu.anneeTaxation);
	$('#revenuEpouxAnnuel').val(data.revenuFullComplex.simpleRevenuSourcier.revenuEpouxAnnuel);
	$('#revenuEpouxMensuel').val(data.revenuFullComplex.simpleRevenuSourcier.revenuEpouxMensuel);
	$('#revenuEpouseAnnuel').val(data.revenuFullComplex.simpleRevenuSourcier.revenuEpouseAnnuel);
	$('#revenuEpouseMensuel').val(data.revenuFullComplex.simpleRevenuSourcier.revenuEpouseMensuel);
	$('#nombreMois').val(data.revenuFullComplex.simpleRevenuSourcier.nombreMois);

	//ZONE CALCUL SOURCIER
	fillFields("revenuPrisEnCompte", data.revenuFullComplex.simpleRevenuSourcier.revenuPrisEnCompte);
	fillFields("totalRevenuImposable", data.revenuFullComplex.simpleRevenuSourcier.revenuImposable);
	fillFields("cotisationAvsAiApg", data.revenuFullComplex.simpleRevenuSourcier.cotisationAvsAiApg);
	fillFields("cotisationAc", data.revenuFullComplex.simpleRevenuSourcier.cotisationAc);
	fillFields("cotisationAcSupplementaires", data.revenuFullComplex.simpleRevenuSourcier.cotisationAcSupplementaires);
	fillFields("primesAANP", data.revenuFullComplex.simpleRevenuSourcier.primesAANP);
	fillFields("primesLPP", data.revenuFullComplex.simpleRevenuSourcier.primesLPP);
	fillFields("deductionAssurances", data.revenuFullComplex.simpleRevenuSourcier.deductionAssurances);
	fillFields("deductionAssurancesEnfant", data.revenuFullComplex.simpleRevenuSourcier.deductionAssurancesEnfant);
	fillFields("deductionAssurancesJeunes", data.revenuFullComplex.simpleRevenuSourcier.deductionAssurancesJeunes);
	fillFields("deductionEnfants", data.revenuFullComplex.simpleRevenuSourcier.deductionEnfants);
	fillFields("deductionFraisObtention", data.revenuFullComplex.simpleRevenuSourcier.deductionFraisObtention);
	fillFields("deductionDoubleGain", data.revenuFullComplex.simpleRevenuSourcier.deductionDoubleGain);

	// HIDE FIELD RDET STANDARD AND SHOW SOURCIER
	// STANDARD
	$('#partRendementImmobExedantIntPassifsCalcul_LINE').hide();
	$('#perteLiquidationCalcul_LINE').hide();
	$('#perteReporteeExercicesCommerciauxCalcul_LINE').hide();
	$('#perteExercicesCommerciauxCalcul_LINE').hide();
	$('#interetsPassifsCalcul_LINE').hide();
	$('#excedentDepensesSuccNonPartageesCalcul_LINE').hide();
	$('#excedentDepensesPropImmoCalcul_LINE').hide();
	$('#rendementFortuneImmoCalcul_LINE').hide();
	$('#revenuImposableCalcul_LINE').hide();
	// Sourcier
	$('#revenuPrisEnCompteRDET_LINE').show();
	$('#totalRevenuImposableRDET_LINE').show();
	$('#cotisationAvsAiApgRDET_LINE').show();
	$('#cotisationAcRDET_LINE').show();
	$('#cotisationAcSupplRDET_LINE').show();
	$('#primesAANPRDET_LINE').show();
	$('#primesLPPRDET_LINE').show();
	$('#deductionAssurancesRDET_LINE').show();
	$('#deductionAssurancesEnfantRDET_LINE').show();
	$('#deductionAssurancesJeunesRDET_LINE').show();
	$('#deductionEnfantsRDET_LINE').show();
	$('#deductionFraisObtentionRDET_LINE').show();
	$('#deductionDoubleGainRDET_LINE').show();
}

/**
 * Cache the zone sourcier
 */
function hideFieldsZoneSourcier(){
	//ZONE DATA SOURCIER
	$('#anneeRefVisu').hide();
	$('#revenuEpouxAnnuel').hide();
	$('#revenuEpouxMensuel').hide();
	$('#revenuEpouseAnnuel').hide();
	$('#revenuEpouseMensuel').hide();
	$('#nombreMois').hide();

	$('#revenuPrisEnCompte').hide();
	$('#totalRevenuImposable').hide();
	
	$("#cotisationAvsAiApg").hide();
	$("#cotisationAc").hide();
	$("#cotisationAcSupplementaires").hide();;
	$("#primesAANP").hide();
	$("#primesLPP").hide();
	$("#deductionAssurances").hide();
	$("#deductionAssurancesEnfant").hide();
	$("#deductionAssurancesJeunes").hide();
	$("#deductionEnfants").hide();
	$("#deductionFraisObtention").hide();
	$("#deductionDoubleGain").hide();
}

/**
 * Affiche the zone sourcier
 */
function showFieldsZoneSourcier(){
	//ZONE DATA SOURCIER
	$('#anneeRefVisu').show();
	$('#revenuEpouxAnnuel').show();
	$('#revenuEpouxMensuel').show();
	$('#revenuEpouseAnnuel').show();
	$('#revenuEpouseMensuel').show();
	$('#nombreMois').show();

	$('#revenuPrisEnCompte').show();
	$('#totalRevenuImposable').show();
	
	$("#cotisationAvsAiApg").show();
	$("#cotisationAc").show();
	$("#cotisationAcSupplementaires").show();;
	$("#primesAANP").show();
	$("#primesLPP").show();
	$("#deductionAssurances").show();
	$("#deductionAssurancesEnfant").show();
	$("#deductionAssurancesJeunes").show();
	$("#deductionEnfants").show();
	$("#deductionFraisObtention").show();
	$("#deductionDoubleGain").show();
}

/**
 * Utilitaire, renseigne les champs visibles
 * et les champs hidden
 * 
 * @param s_fieldName
 * @param s_value
 */
function fillFields(s_fieldName, s_value) {
	$("#"+s_fieldName).html(formatNumber(s_value,true));
	//$("#hidden_"+s_fieldName).val(formatNumber(s_value));
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

/**
* 
* collecte les données de la page actuelle ré-afficher la liste des taxations découvertes
* 
*/
function getListTaxations() {
	var a_params = new Array(
			"idContribuable:"+$("#idContribuable").val(),
			$("#anneeHistorique").attr("id")+":"+$("#anneeHistorique").val()
	);
	
	for(i=0;i<a_params.length;i++) {
		a_params[i] = a_params[i].replace(/\'/g,"");
	}		

	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.models.detailfamille.DetailFamilleService',
		serviceMethodName:'getListTaxationsAjax',
		parametres: '['+a_params+']',
		callBack: refreshTaxationList
	}

	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}

/**
* Refresh all fields
* 
* @param data
*/
function refreshTaxationList(data){
	// Clear the list
	$('#taxationLiee').hide();
	$('#taxationLiee option').remove();

	// Save the first index
	var s_Value0 = '';
	// Read the data and update the taxation list
	for(var i_taxation=0;i_taxation<data.length;i_taxation++){
		var currentTaxation = data[i_taxation];
		var s_Value = ''+currentTaxation.idRevenu;
		var s_Item = ''+currentTaxation.anneeTaxation;
		if(currentTaxation.typeRevenu=='42002600'){
			s_Item += ' [C] ';
			s_Item += ' Revenu imposable : ';
			s_Item += currentTaxation.revenuImposable;
		}else{
			s_Item += ' [S] ';
			s_Item += ' Revenu imposable : ';
			s_Item += currentTaxation.revenuImposableSourcier;
		}
		// perform the list
		$('#taxationLiee').append($('<option></option>').val(s_Value).html(s_Item));
		if(i_taxation==0){
			s_Value0=s_Value;
		}
	}
	
	// Select the first item on the list
	if(s_Value0.length>0){
		$('#taxationLiee option[value='+s_Value0+']').prop('selected','selected');
		$('#taxationLiee').show();
		$('#taxationLieeImg').show();
		collectDatas();
		showFieldsZoneGenerique();
		showFieldsZoneContribuable();
		showFieldsZoneSourcier();
	}else{
		// HIDE EVERYTHING
		$('#taxationLiee').show();
		$('#taxationLieeImg').hide();
		hideFieldsZoneGenerique();
		hideFieldsZoneContribuable();
		hideFieldsZoneSourcier();
	}

	
}
