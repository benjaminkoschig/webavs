/*
 * Créé le 22 juillet 2009
 */

package ch.globaz.pegasus.business.constantes;

/**
 * @author bsc
 * 
 *         Définition des constantes pour les actions
 */

public interface IPCActions {

    String ACTION_ANNONCE_COMMUNICATION_OCC_GENERER = "pegasus.annonce.genererCommunicationOCC";
    String ACTION_ANNONCE_LAPRAMS_GENERER = "pegasus.annonce.genererAnnonceLaprams";
    String ACTION_AVANCE_DETAIL = "pegasus.avance.afficher";
    String ACTION_AVANCE_DETAIL_DELETE = "pegasus.avance.supprimer";
    String ACTION_AVANCE_DETAIL_EXECUTE = "pegasus.avance.executerAvances.executer";
    String ACTION_AVANCE_DETAIL_NEW = "pegasus.avance.ajouter";
    String ACTION_AVANCE_DETAIL_UPDATE = "pegasus.avance.modifier";
    String ACTION_AVANCE_LISTE_AVANCES = "pegasus.avance.listeAvances.afficher";
    String ACTION_AVANCE_LISTER_AJAX = "pegasus.avance.listeAvancesAjax";
    String ACTION_CALCUL_DROIT = "pegasus.droit.calculDroit";
    String ACTION_CREANCE_ACCORDEE = "pegasus.creancier.creanceAccordee";
    String ACTION_CREANCIER = "pegasus.creancier.creancier";
    String ACTION_CREANCIER_AJAX = "pegasus.creancier.creancierAjax";
    String ACTION_BLOCAGE_DEBLOCAGE = "pegasus.blocage.deblocage";
    String ACTION_BLOCAGE_DEBLOCAGE_AJAX = "pegasus.blocage.deblocageAjax";
    String ACTION_BLOCAGE_ENTENTE = "pegasus.blocage.enteteBlocage";
    String ACTION_BLOCAGE_ENTENTE_AJAX = "pegasus.blocage.enteteBlocageAjax";

    String ACTION_CREANCIER_LISTE_CREANCES = "pegasus.creancier.creanceAccordee";
    String ACTION_DECISION = "pegasus.decision.decision";
    String ACTION_DECISION_AC_IMPRIMER = "pegasus.decision.imprimerDecisionsProcess.executer";
    String ACTION_BILLAG_IMPRIMER = "pegasus.demande.imprimerBillagProcess.executer";
    String ACTION_DECISION_AC_IMPRIMER_BILLAG = "pegasus.demande.imprimerBillagProcess.executer";
    String ACTION_DECISION_AC_PREPARATION = "pegasus.decision.prepDecisionApresCalcul.preparer";
    String ACTION_DECISION_AC_PREVALIDATION = "pegasus.decision.decisionApresCalcul.prevalider";
    String ACTION_DECISION_AC_VALIDATION = "pegasus.decision.validationDecisions.valider";
    String ACTION_DECISION_APRES_CALCUL_DETAIL = "pegasus.decision.decisionApresCalcul.detail";
    String ACTION_DECISION_DETAIL_AP_CALCUL = "pegasus.decision.decisionApresCalcul";

    String ACTION_DECISION_DETAIL_REFUS = "pegasus.decision.decisionRefus";

    String ACTION_DECISION_DETAIL_SUPPRESSION = "pegasus.decision.decisionSuppression";
    String ACTION_DECISION_DEVALIDER = "pegasus.decision.devalidationDecision.devalider";
    String ACTION_DECISION_REF_PREPARATION = "pegasus.decision.prepDecisionRefus.preparer";
    String ACTION_DECISION_REF_PREVALIDATION = "pegasus.decision.decisionRefus.prevalider";
    String ACTION_DECISION_REF_VALIDATION = "pegasus.decision.decisionRefus.valider";
    String ACTION_DECISION_REFUS_DETAIL = "pegasus.decision.decisionRefus.detail";
    String ACTION_DECISION_SUP_IMPRIMER = "pegasus.decision.decisionSuppression.imprimer";
    String ACTION_DECISION_SUP_PREPARATION = "pegasus.decision.prepDecisionSuppression.preparer";

    String ACTION_DECISION_SUP_PREVALIDATION = "pegasus.decision.decisionSuppression.prevalider";

    String ACTION_DECISION_SUP_VALIDATION = "pegasus.decision.decisionSuppression.valider";
    String ACTION_DECISION_SUPPRESSION_DETAIL = "pegasus.decision.decisionSuppression.detail";
    String ACTION_DECISION_VALIDER_TOUT = "pegasus.decision.validationDecisions";
    String ACTION_DEMANDE = "pegasus.demande.demande";
    String ACTION_DEMANDE_DETAIL = "pegasus.demande.demandeDetail";
    String ACTION_DEMANDE_GENERER_LISTE_REVISIONS = "pegasus.demande.genererListeRevisions";
    String ACTION_DEMANDE_LISTE_DEMANDES = "pegasus.demande.listDemandesPC";
    String ACTION_DEMANDE_RENSEIGNEMENT = "pegasus.demanderenseignement.demandeRenseignement";
    String ACTION_DEMANDE_TRANSFERT_DOSSIER_TRANSFERT = "pegasus.transfertdossier.demandeTransfertDossier";
    String ACTION_DEMANDE_TRANSFERT_RENTE_TRANSFERT = "pegasus.transfertdossier.demandeTransfertRente";
    String ACTION_DETTE_COMPTAT_COMPENSE = "pegasus.dettecomptatcompense.detteComptatCompense";
    String ACTION_DETTE_COMPTAT_COMPENSE_AJAX = "pegasus.dettecomptatcompense.detteComptatCompenseAjax";
    String ACTION_DOSSIER = "pegasus.dossier.dossier";
    String ACTION_DOWNLOAD = "pegasus.downloadDocument.downloadDocument";
    String ACTION_DROIT = "pegasus.droit.droit";
    String ACTION_DROIT_ACTIVITE_LUCRATIVE_DEPENDANTE = "pegasus.revenusdepenses.revenuActiviteLucrativeDependante";
    String ACTION_DROIT_ACTIVITE_LUCRATIVE_DEPENDANTE_AJAX = "pegasus.revenusdepenses.revenuActiviteLucrativeDependanteAjax";
    String ACTION_DROIT_ACTIVITE_LUCRATIVE_INDEPENDANTE = "pegasus.revenusdepenses.revenuActiviteLucrativeIndependante";
    String ACTION_DROIT_ACTIVITE_LUCRATIVE_INDEPENDANTE_AJAX = "pegasus.revenusdepenses.revenuActiviteLucrativeIndependanteAjax";
    String ACTION_DROIT_ALLOCATION_IMPOTENT_AVS_AI = "pegasus.renteijapi.allocationImpotent";
    String ACTION_DROIT_ALLOCATION_IMPOTENT_AVS_AI_AJAX = "pegasus.renteijapi.allocationImpotentAjax";
    String ACTION_DROIT_ALLOCATIONS_FAMILIALES = "pegasus.revenusdepenses.allocationsFamiliales";
    String ACTION_DROIT_ALLOCATIONS_FAMILIALES_AJAX = "pegasus.revenusdepenses.allocationsFamilialesAjax";
    String ACTION_DROIT_ASSURANCE_MALADIE = "pegasus.assurancemaladie.primeAssuranceMaladie";
    String ACTION_DROIT_ASSURANCE_MALADIE_AJAX = "pegasus.assurancemaladie.primeAssuranceMaladieAjax";
    String ACTION_DROIT_ASSURANCE_RENTE_VIAGERE = "pegasus.fortuneparticuliere.assuranceRenteViagere";
    String ACTION_DROIT_ASSURANCE_RENTE_VIAGERE_AJAX = "pegasus.fortuneparticuliere.assuranceRenteViagereAjax";
    String ACTION_DROIT_ASSURANCE_VIE = "pegasus.fortuneusuelle.assuranceVie";
    String ACTION_DROIT_ASSURANCE_VIE_AJAX = "pegasus.fortuneusuelle.assuranceVieAjax";
    String ACTION_DROIT_AUTRE_FORTUNE_MOBILIERE = "pegasus.fortuneparticuliere.autreFortuneMobiliere";
    String ACTION_DROIT_AUTRE_FORTUNE_MOBILIERE_AJAX = "pegasus.fortuneparticuliere.autreFortuneMobiliereAjax";
    String ACTION_DROIT_AUTRES_API = "pegasus.renteijapi.autreApi";
    String ACTION_DROIT_AUTRES_API_AJAX = "pegasus.renteijapi.autreApiAjax";
    String ACTION_DROIT_AUTRES_DETTES_PROUVEES = "pegasus.fortuneusuelle.autresDettesProuvees";
    String ACTION_DROIT_AUTRES_DETTES_PROUVEES_AJAX = "pegasus.fortuneusuelle.autresDettesProuveesAjax";
    String ACTION_DROIT_AUTRES_RENTES = "pegasus.renteijapi.autreRente";
    String ACTION_DROIT_AUTRES_RENTES_AJAX = "pegasus.renteijapi.autreRenteAjax";
    String ACTION_DROIT_AUTRES_REVENUS = "pegasus.revenusdepenses.autresRevenus";
    String ACTION_DROIT_AUTRES_REVENUS_AJAX = "pegasus.revenusdepenses.autresRevenusAjax";
    String ACTION_DROIT_AVS_AI = "pegasus.renteijapi.renteAvsAi";
    String ACTION_DROIT_AVS_AI_AJAX = "pegasus.renteijapi.renteAvsAiAjax";
    String ACTION_DROIT_BETAIL = "pegasus.fortuneparticuliere.betail";
    String ACTION_DROIT_BETAIL_AJAX = "pegasus.fortuneparticuliere.betailAjax";
    String ACTION_DROIT_BIEN_IMMOBILIER_NH = "pegasus.fortuneusuelle.bienImmobilierNonHabitable";
    String ACTION_DROIT_BIEN_IMMOBILIER_NH_AJAX = "pegasus.fortuneusuelle.bienImmobilierNonHabitableAjax";
    String ACTION_DROIT_BIEN_IMMOBILIER_NSPHP = "pegasus.fortuneusuelle.bienImmobilierHabitationNonPrincipale";
    String ACTION_DROIT_BIEN_IMMOBILIER_NSPHP_AJAX = "pegasus.fortuneusuelle.bienImmobilierHabitationNonPrincipaleAjax";
    String ACTION_DROIT_BIEN_IMMOBILIER_SHP = "pegasus.fortuneusuelle.bienImmobilierServantHabitationPrincipale";
    String ACTION_DROIT_BIEN_IMMOBILIER_SHP_AJAX = "pegasus.fortuneusuelle.bienImmobilierServantHabitationPrincipaleAjax";
    String ACTION_DROIT_CALCULER = "pegasus.droit.calculDroit";
    String ACTION_DROIT_CAPITAL_LPP = "pegasus.fortuneusuelle.capitalLPP";
    String ACTION_DROIT_CAPITAL_LPP_AJAX = "pegasus.fortuneusuelle.capitalLPPAjax";
    String ACTION_DROIT_COMPTE_BANCAIRE_CCP = "pegasus.fortuneusuelle.compteBancaireCCP";
    String ACTION_DROIT_COMPTE_BANCAIRE_CCP_AJAX = "pegasus.fortuneusuelle.compteBancaireCCPAjax";
    String ACTION_DROIT_CONTRAT_ENTRETIEN_VIAGER = "pegasus.revenusdepenses.contratEntretienViager";
    String ACTION_DROIT_CONTRAT_ENTRETIEN_VIAGER_AJAX = "pegasus.revenusdepenses.contratEntretienViagerAjax";
    String ACTION_DROIT_CORRIGER = "pegasus.droit.corrigerDroit";
    String ACTION_DROIT_COTISATIONS_PSAL = "pegasus.revenusdepenses.cotisationsPsal";
    String ACTION_DROIT_COTISATIONS_PSAL_AJAX = "pegasus.revenusdepenses.cotisationsPsalAjax";
    String ACTION_DROIT_DESSAISISSEMENT_FORTUNE = "pegasus.dessaisissement.dessaisissementFortune";
    String ACTION_DROIT_DESSAISISSEMENT_FORTUNE_AJAX = "pegasus.dessaisissement.dessaisissementFortuneAjax";
    String ACTION_DROIT_DESSAISISSEMENT_REVENU = "pegasus.dessaisissement.dessaisissementRevenu";
    String ACTION_DROIT_DESSAISISSEMENT_REVENU_AJAX = "pegasus.dessaisissement.dessaisissementRevenuAjax";
    String ACTION_DROIT_DONNEES_PERSONNELLES = "pegasus.droit.saisieDonneesPersonnelles";
    String ACTION_DROIT_DONNEES_PERSONNELLES_AJAX = "pegasus.droit.saisieDonneesPersonnellesAjax";
    String ACTION_DROIT_IJAI = "pegasus.renteijapi.indemniteJournaliereAi";
    String ACTION_DROIT_IJAI_AJAX = "pegasus.renteijapi.indemniteJournaliereAiAjax";
    String ACTION_DROIT_INDEMNITES_JOURNALIERES_APG = "pegasus.renteijapi.ijApg";
    String ACTION_DROIT_INDEMNITES_JOURNALIERES_APG_AJAX = "pegasus.renteijapi.ijApgAjax";
    String ACTION_DROIT_LOYER = "pegasus.habitat.loyer";
    String ACTION_DROIT_LOYER_AJAX = "pegasus.habitat.loyerAjax";
    String ACTION_DROIT_MARCHANDISES_STOCK = "pegasus.fortuneparticuliere.marchandisesStock";
    String ACTION_DROIT_MARCHANDISES_STOCK_AJAX = "pegasus.fortuneparticuliere.marchandisesStockAjax";
    String ACTION_DROIT_NUMERAIRE = "pegasus.fortuneparticuliere.numeraire";
    String ACTION_DROIT_NUMERAIRE_AJAX = "pegasus.fortuneparticuliere.numeraireAjax";
    String ACTION_DROIT_PENSION_ALIMENTAIRE = "pegasus.revenusdepenses.pensionAlimentaire";
    String ACTION_DROIT_PENSION_ALIMENTAIRE_AJAX = "pegasus.revenusdepenses.pensionAlimentaireAjax";
    String ACTION_DROIT_PRET_ENVERS_TIERS = "pegasus.fortuneparticuliere.pretEnversTiers";
    String ACTION_DROIT_PRET_ENVERS_TIERS_AJAX = "pegasus.fortuneparticuliere.pretEnversTiersAjax";
    String ACTION_DROIT_REVENU_HYPOTHETIQUE = "pegasus.revenusdepenses.revenuHypothetique";
    String ACTION_DROIT_REVENU_HYPOTHETIQUE_AJAX = "pegasus.revenusdepenses.revenuHypothetiqueAjax";
    String ACTION_DROIT_SUBSIDE_RETRO_ACCORDE = "pegasus.assurancemaladie.subsideAssuranceMaladie";
    String ACTION_DROIT_SUBSIDE_RETRO_ACCORDE_AJAX = "pegasus.assurancemaladie.subsideAssuranceMaladieAjax";
    String ACTION_DROIT_TAXE_JOURNALIERE_HOME = "pegasus.habitat.taxeJournaliereHome";
    String ACTION_DROIT_TAXE_JOURNALIERE_HOME_AJAX = "pegasus.habitat.taxeJournaliereHomeAjax";
    String ACTION_DROIT_SEJOUR_MOIS_PARTIEL_HOME = "pegasus.habitat.sejourMoisPartielHome";
    String ACTION_DROIT_SEJOUR_MOIS_PARTIEL_HOME_AJAX = "pegasus.habitat.sejourMoisPartielHomeAjax";
    String ACTION_DROIT_TITRE = "pegasus.fortuneusuelle.titre";
    String ACTION_DROIT_TITRE_AJAX = "pegasus.fortuneusuelle.titreAjax";
    String ACTION_DROIT_VEHICULE = "pegasus.fortuneparticuliere.vehicule";
    String ACTION_DROIT_VEHICULE_AJAX = "pegasus.fortuneparticuliere.vehiculeAjax";
    String ACTION_FRATRIE = "pegasus.demande.fratrie";
    String ACTION_HOME = "pegasus.home.home";
    String ACTION_HOME_AJAX = "pegasus.home.homeAjax";
    String ACTION_HOME_PERIODE_AJAX = "pegasus.home.periodeAjax";
    String ACTION_HOME_TYPE_CHAMBRE_AJAX = "pegasus.home.typeChambreAjax";
    String ACTION_LOT_COMPTABILISER = "pegasus.lot.comptabiliser";
    String ACTION_LOT_ORDRE_VERSEMENT = "pegasus.lot.ordreVersement";
    String ACTION_LOT_ORDRE_VERSEMENT_AJAX = "pegasus.lot.ordreVersementAjax";
    String ACTION_LOT_ORDRE_VERSEMENT_GENERER_LISTE = "pegasus.lot.genererListeOrdresVersement";
    String ACTION_PARAMETRES_CONVERSION_RENTE = "pegasus.parametre.conversionRente";
    String ACTION_PARAMETRES_FORFAIT_PRIME_ASSURANCE_MALADIE = "pegasus.parametre.forfaitPrimesAssuranceMaladie";
    String ACTION_PARAMETRES_MONNAIES_ETRANGERES = "pegasus.monnaieetrangere.monnaieEtrangere";
    String ACTION_PARAMETRES_VARIABLE_METIER = "pegasus.variablemetier.variableMetier";
    String ACTION_PARAMETRES_ZONE_FORFAIT = "pegasus.parametre.zoneForfaits";
    String ACTION_PARAMETRES_ZONE_LOCALITE = "pegasus.parametre.zoneLocalite";
    String ACTION_PCACCORDEE = "pegasus.pcaccordee.pcAccordee";
    String ACTION_PCACCORDEE_DETAIL = "pegasus.pcaccordee.pcAccordeeDetail";
    String ACTION_PCACCORDEE_PLANCALCUL = "pegasus.pcaccordee.planCalcul";
    String ACTION_PCACCORDEES_LIST = "pegasus.pcaccordee.pcAccordee";
    String ACTION_PRIX_CHAMBRE = "pegasus.home.prixChambre";
    String ACTION_PRIX_CHAMBRE_AJAX = "pegasus.home.prixChambreAjax";
    String ACTION_PROCESS_ADAPTATION_PC = "pegasus.process.adaptationPC";
    String ACTION_REVENUS_DEPENSES = "pegasus.revenusDepenses";
    String ACTION_SITUATION_FAMILIALE = "pegasus.demandes.situationFamiliale";
    String ACTION_TRAITMENT_DE_MASSE_DEFINITION_PARAMETRE_AJAX = "pegasus.traitementdemasse.definitionParametreAjax";
    String ACTION_TRAITMENT_DE_MASSE_DEFINITION_TRAITEMENT_AJAX = "pegasus.traitementdemasse.definitionTraitementAjax";
    String ACTION_TRAITMENT_DE_MASSE_LISTE_ACTION_AJAX = "pegasus.traitementdemasse.listeActionAjax";
    String ACTION_TYPE_CHAMBRE = "pegasus.home.typeChambre";
    String ACTION_WIDGET_CHERCHE_TIERS = "widget.pyxis.chercheTiers";

    String ACTION_ECHEANCE_LISTE_ENFANTS_11_ANS = "pegasus.demande.genererListeRevisions";

    String ACTION_RPC_ANNONCE = "pegasus.rpc.annonces";
    String ACTION_RPC_ANNONCE_AJAX = "pegasus.rpc.annoncesAjax";
    String ACTION_RPC_DETAIL_ANNONCE_AJAX = "pegasus.rpc.detailAnnonceAjax";

    String PARAM_ACTION_DEBLOCAGE_DEVALIDER_LIBERATION = "devalider";
    String PARAM_ACTION_DEBLOCAGE_VALIDER_LIBERATION = "liberer";
    String ACTION_PARAMETRES_ZONE_FORFAIT_LOYER = "pegasus.parametre.zoneForfaitsLoyer";
    String ACTION_PARAMETRES_ZONE_LOCALITE_LOYER = "pegasus.parametre.zoneLocaliteLoyer";
    String ACTION_PARAMETRES_FORFAIT_LOYER = "pegasus.parametre.forfaitLoyer";

    String ACTION_DROIT_FRAIS_GARDE = "pegasus.revenusdepenses.fraisGarde" ;
    String ACTION_DROIT_FRAIS_GARDE_AJAX = "pegasus.revenusdepenses.fraisGardeAjax" ;

    String ACTION_RESTITUTION_PC = "pegasus.restitution.restitution" ;
}
