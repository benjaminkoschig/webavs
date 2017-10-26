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

    public static final String ACTION_ANNONCE_COMMUNICATION_OCC_GENERER = "pegasus.annonce.genererCommunicationOCC";
    public static final String ACTION_ANNONCE_LAPRAMS_GENERER = "pegasus.annonce.genererAnnonceLaprams";
    public static final String ACTION_AVANCE_DETAIL = "pegasus.avance.afficher";
    public static final String ACTION_AVANCE_DETAIL_DELETE = "pegasus.avance.supprimer";
    public static final String ACTION_AVANCE_DETAIL_EXECUTE = "pegasus.avance.executerAvances.executer";
    public static final String ACTION_AVANCE_DETAIL_NEW = "pegasus.avance.ajouter";
    public static final String ACTION_AVANCE_DETAIL_UPDATE = "pegasus.avance.modifier";
    public static final String ACTION_AVANCE_LISTE_AVANCES = "pegasus.avance.listeAvances.afficher";
    public static final String ACTION_AVANCE_LISTER_AJAX = "pegasus.avance.listeAvancesAjax";
    public static final String ACTION_CALCUL_DROIT = "pegasus.droit.calculDroit";
    public static final String ACTION_CREANCE_ACCORDEE = "pegasus.creancier.creanceAccordee";
    public static final String ACTION_CREANCIER = "pegasus.creancier.creancier";
    public static final String ACTION_CREANCIER_AJAX = "pegasus.creancier.creancierAjax";
    public static final String ACTION_BLOCAGE_DEBLOCAGE = "pegasus.blocage.deblocage";
    public static final String ACTION_BLOCAGE_DEBLOCAGE_AJAX = "pegasus.blocage.deblocageAjax";
    public static final String ACTION_BLOCAGE_ENTENTE = "pegasus.blocage.enteteBlocage";
    public static final String ACTION_BLOCAGE_ENTENTE_AJAX = "pegasus.blocage.enteteBlocageAjax";

    public static final String ACTION_CREANCIER_LISTE_CREANCES = "pegasus.creancier.creanceAccordee";
    public static final String ACTION_DECISION = "pegasus.decision.decision";
    public static final String ACTION_DECISION_AC_IMPRIMER = "pegasus.decision.imprimerDecisionsProcess.executer";
    public static final String ACTION_BILLAG_IMPRIMER = "pegasus.demande.imprimerBillagProcess.executer";
    public static final String ACTION_DECISION_AC_IMPRIMER_BILLAG = "pegasus.demande.imprimerBillagProcess.executer";
    public static final String ACTION_DECISION_AC_PREPARATION = "pegasus.decision.prepDecisionApresCalcul.preparer";
    public static final String ACTION_DECISION_AC_PREVALIDATION = "pegasus.decision.decisionApresCalcul.prevalider";
    public static final String ACTION_DECISION_AC_VALIDATION = "pegasus.decision.validationDecisions.valider";
    public static final String ACTION_DECISION_APRES_CALCUL_DETAIL = "pegasus.decision.decisionApresCalcul.detail";
    public static final String ACTION_DECISION_DETAIL_AP_CALCUL = "pegasus.decision.decisionApresCalcul";

    public static final String ACTION_DECISION_DETAIL_REFUS = "pegasus.decision.decisionRefus";

    public static final String ACTION_DECISION_DETAIL_SUPPRESSION = "pegasus.decision.decisionSuppression";
    public static final String ACTION_DECISION_DEVALIDER = "pegasus.decision.devalidationDecision.devalider";
    public static final String ACTION_DECISION_REF_PREPARATION = "pegasus.decision.prepDecisionRefus.preparer";
    public static final String ACTION_DECISION_REF_PREVALIDATION = "pegasus.decision.decisionRefus.prevalider";
    public static final String ACTION_DECISION_REF_VALIDATION = "pegasus.decision.decisionRefus.valider";
    public static final String ACTION_DECISION_REFUS_DETAIL = "pegasus.decision.decisionRefus.detail";
    public static final String ACTION_DECISION_SUP_IMPRIMER = "pegasus.decision.decisionSuppression.imprimer";
    public static final String ACTION_DECISION_SUP_PREPARATION = "pegasus.decision.prepDecisionSuppression.preparer";

    public static final String ACTION_DECISION_SUP_PREVALIDATION = "pegasus.decision.decisionSuppression.prevalider";

    public static final String ACTION_DECISION_SUP_VALIDATION = "pegasus.decision.decisionSuppression.valider";
    public static final String ACTION_DECISION_SUPPRESSION_DETAIL = "pegasus.decision.decisionSuppression.detail";
    public static final String ACTION_DECISION_VALIDER_TOUT = "pegasus.decision.validationDecisions";
    public static final String ACTION_DEMANDE = "pegasus.demande.demande";
    public static final String ACTION_DEMANDE_DETAIL = "pegasus.demande.demandeDetail";
    public static final String ACTION_DEMANDE_GENERER_LISTE_REVISIONS = "pegasus.demande.genererListeRevisions";
    public static final String ACTION_DEMANDE_LISTE_DEMANDES = "pegasus.demande.listDemandesPC";
    public static final String ACTION_DEMANDE_RENSEIGNEMENT = "pegasus.demanderenseignement.demandeRenseignement";
    public static final String ACTION_DEMANDE_TRANSFERT_DOSSIER_TRANSFERT = "pegasus.transfertdossier.demandeTransfertDossier";
    public static final String ACTION_DEMANDE_TRANSFERT_RENTE_TRANSFERT = "pegasus.transfertdossier.demandeTransfertRente";
    public static final String ACTION_DETTE_COMPTAT_COMPENSE = "pegasus.dettecomptatcompense.detteComptatCompense";
    public static final String ACTION_DETTE_COMPTAT_COMPENSE_AJAX = "pegasus.dettecomptatcompense.detteComptatCompenseAjax";
    public static final String ACTION_DOSSIER = "pegasus.dossier.dossier";
    public static final String ACTION_DOWNLOAD = "pegasus.downloadDocument.downloadDocument";
    public static final String ACTION_DROIT = "pegasus.droit.droit";
    public static final String ACTION_DROIT_ACTIVITE_LUCRATIVE_DEPENDANTE = "pegasus.revenusdepenses.revenuActiviteLucrativeDependante";
    public static final String ACTION_DROIT_ACTIVITE_LUCRATIVE_DEPENDANTE_AJAX = "pegasus.revenusdepenses.revenuActiviteLucrativeDependanteAjax";
    public static final String ACTION_DROIT_ACTIVITE_LUCRATIVE_INDEPENDANTE = "pegasus.revenusdepenses.revenuActiviteLucrativeIndependante";
    public static final String ACTION_DROIT_ACTIVITE_LUCRATIVE_INDEPENDANTE_AJAX = "pegasus.revenusdepenses.revenuActiviteLucrativeIndependanteAjax";
    public static final String ACTION_DROIT_ALLOCATION_IMPOTENT_AVS_AI = "pegasus.renteijapi.allocationImpotent";
    public static final String ACTION_DROIT_ALLOCATION_IMPOTENT_AVS_AI_AJAX = "pegasus.renteijapi.allocationImpotentAjax";
    public static final String ACTION_DROIT_ALLOCATIONS_FAMILIALES = "pegasus.revenusdepenses.allocationsFamiliales";
    public static final String ACTION_DROIT_ALLOCATIONS_FAMILIALES_AJAX = "pegasus.revenusdepenses.allocationsFamilialesAjax";
    public static final String ACTION_DROIT_ASSURANCE_RENTE_VIAGERE = "pegasus.fortuneparticuliere.assuranceRenteViagere";
    public static final String ACTION_DROIT_ASSURANCE_RENTE_VIAGERE_AJAX = "pegasus.fortuneparticuliere.assuranceRenteViagereAjax";
    public static final String ACTION_DROIT_ASSURANCE_VIE = "pegasus.fortuneusuelle.assuranceVie";
    public static final String ACTION_DROIT_ASSURANCE_VIE_AJAX = "pegasus.fortuneusuelle.assuranceVieAjax";
    public static final String ACTION_DROIT_AUTRE_FORTUNE_MOBILIERE = "pegasus.fortuneparticuliere.autreFortuneMobiliere";
    public static final String ACTION_DROIT_AUTRE_FORTUNE_MOBILIERE_AJAX = "pegasus.fortuneparticuliere.autreFortuneMobiliereAjax";
    public static final String ACTION_DROIT_AUTRES_API = "pegasus.renteijapi.autreApi";
    public static final String ACTION_DROIT_AUTRES_API_AJAX = "pegasus.renteijapi.autreApiAjax";
    public static final String ACTION_DROIT_AUTRES_DETTES_PROUVEES = "pegasus.fortuneusuelle.autresDettesProuvees";
    public static final String ACTION_DROIT_AUTRES_DETTES_PROUVEES_AJAX = "pegasus.fortuneusuelle.autresDettesProuveesAjax";
    public static final String ACTION_DROIT_AUTRES_RENTES = "pegasus.renteijapi.autreRente";
    public static final String ACTION_DROIT_AUTRES_RENTES_AJAX = "pegasus.renteijapi.autreRenteAjax";
    public static final String ACTION_DROIT_AUTRES_REVENUS = "pegasus.revenusdepenses.autresRevenus";
    public static final String ACTION_DROIT_AUTRES_REVENUS_AJAX = "pegasus.revenusdepenses.autresRevenusAjax";
    public static final String ACTION_DROIT_AVS_AI = "pegasus.renteijapi.renteAvsAi";
    public static final String ACTION_DROIT_AVS_AI_AJAX = "pegasus.renteijapi.renteAvsAiAjax";
    public static final String ACTION_DROIT_BETAIL = "pegasus.fortuneparticuliere.betail";
    public static final String ACTION_DROIT_BETAIL_AJAX = "pegasus.fortuneparticuliere.betailAjax";
    public static final String ACTION_DROIT_BIEN_IMMOBILIER_NH = "pegasus.fortuneusuelle.bienImmobilierNonHabitable";
    public static final String ACTION_DROIT_BIEN_IMMOBILIER_NH_AJAX = "pegasus.fortuneusuelle.bienImmobilierNonHabitableAjax";
    public static final String ACTION_DROIT_BIEN_IMMOBILIER_NSPHP = "pegasus.fortuneusuelle.bienImmobilierHabitationNonPrincipale";
    public static final String ACTION_DROIT_BIEN_IMMOBILIER_NSPHP_AJAX = "pegasus.fortuneusuelle.bienImmobilierHabitationNonPrincipaleAjax";
    public static final String ACTION_DROIT_BIEN_IMMOBILIER_SHP = "pegasus.fortuneusuelle.bienImmobilierServantHabitationPrincipale";
    public static final String ACTION_DROIT_BIEN_IMMOBILIER_SHP_AJAX = "pegasus.fortuneusuelle.bienImmobilierServantHabitationPrincipaleAjax";
    public static final String ACTION_DROIT_CALCULER = "pegasus.droit.calculDroit";
    public static final String ACTION_DROIT_CAPITAL_LPP = "pegasus.fortuneusuelle.capitalLPP";
    public static final String ACTION_DROIT_CAPITAL_LPP_AJAX = "pegasus.fortuneusuelle.capitalLPPAjax";
    public static final String ACTION_DROIT_COMPTE_BANCAIRE_CCP = "pegasus.fortuneusuelle.compteBancaireCCP";
    public static final String ACTION_DROIT_COMPTE_BANCAIRE_CCP_AJAX = "pegasus.fortuneusuelle.compteBancaireCCPAjax";
    public static final String ACTION_DROIT_CONTRAT_ENTRETIEN_VIAGER = "pegasus.revenusdepenses.contratEntretienViager";
    public static final String ACTION_DROIT_CONTRAT_ENTRETIEN_VIAGER_AJAX = "pegasus.revenusdepenses.contratEntretienViagerAjax";
    public static final String ACTION_DROIT_CORRIGER = "pegasus.droit.corrigerDroit";
    public static final String ACTION_DROIT_COTISATIONS_PSAL = "pegasus.revenusdepenses.cotisationsPsal";
    public static final String ACTION_DROIT_COTISATIONS_PSAL_AJAX = "pegasus.revenusdepenses.cotisationsPsalAjax";
    public static final String ACTION_DROIT_DESSAISISSEMENT_FORTUNE = "pegasus.dessaisissement.dessaisissementFortune";
    public static final String ACTION_DROIT_DESSAISISSEMENT_FORTUNE_AJAX = "pegasus.dessaisissement.dessaisissementFortuneAjax";
    public static final String ACTION_DROIT_DESSAISISSEMENT_REVENU = "pegasus.dessaisissement.dessaisissementRevenu";
    public static final String ACTION_DROIT_DESSAISISSEMENT_REVENU_AJAX = "pegasus.dessaisissement.dessaisissementRevenuAjax";
    public static final String ACTION_DROIT_DONNEES_PERSONNELLES = "pegasus.droit.saisieDonneesPersonnelles";
    public static final String ACTION_DROIT_DONNEES_PERSONNELLES_AJAX = "pegasus.droit.saisieDonneesPersonnellesAjax";
    public static final String ACTION_DROIT_IJAI = "pegasus.renteijapi.indemniteJournaliereAi";
    public static final String ACTION_DROIT_IJAI_AJAX = "pegasus.renteijapi.indemniteJournaliereAiAjax";
    public static final String ACTION_DROIT_INDEMNITES_JOURNALIERES_APG = "pegasus.renteijapi.ijApg";
    public static final String ACTION_DROIT_INDEMNITES_JOURNALIERES_APG_AJAX = "pegasus.renteijapi.ijApgAjax";
    public static final String ACTION_DROIT_LOYER = "pegasus.habitat.loyer";
    public static final String ACTION_DROIT_LOYER_AJAX = "pegasus.habitat.loyerAjax";
    public static final String ACTION_DROIT_MARCHANDISES_STOCK = "pegasus.fortuneparticuliere.marchandisesStock";
    public static final String ACTION_DROIT_MARCHANDISES_STOCK_AJAX = "pegasus.fortuneparticuliere.marchandisesStockAjax";
    public static final String ACTION_DROIT_NUMERAIRE = "pegasus.fortuneparticuliere.numeraire";
    public static final String ACTION_DROIT_NUMERAIRE_AJAX = "pegasus.fortuneparticuliere.numeraireAjax";
    public static final String ACTION_DROIT_PENSION_ALIMENTAIRE = "pegasus.revenusdepenses.pensionAlimentaire";
    public static final String ACTION_DROIT_PENSION_ALIMENTAIRE_AJAX = "pegasus.revenusdepenses.pensionAlimentaireAjax";
    public static final String ACTION_DROIT_PRET_ENVERS_TIERS = "pegasus.fortuneparticuliere.pretEnversTiers";
    public static final String ACTION_DROIT_PRET_ENVERS_TIERS_AJAX = "pegasus.fortuneparticuliere.pretEnversTiersAjax";
    public static final String ACTION_DROIT_REVENU_HYPOTHETIQUE = "pegasus.revenusdepenses.revenuHypothetique";
    public static final String ACTION_DROIT_REVENU_HYPOTHETIQUE_AJAX = "pegasus.revenusdepenses.revenuHypothetiqueAjax";
    public static final String ACTION_DROIT_TAXE_JOURNALIERE_HOME = "pegasus.habitat.taxeJournaliereHome";
    public static final String ACTION_DROIT_TAXE_JOURNALIERE_HOME_AJAX = "pegasus.habitat.taxeJournaliereHomeAjax";
    public static final String ACTION_DROIT_TITRE = "pegasus.fortuneusuelle.titre";
    public static final String ACTION_DROIT_TITRE_AJAX = "pegasus.fortuneusuelle.titreAjax";
    public static final String ACTION_DROIT_VEHICULE = "pegasus.fortuneparticuliere.vehicule";
    public static final String ACTION_DROIT_VEHICULE_AJAX = "pegasus.fortuneparticuliere.vehiculeAjax";
    public static final String ACTION_FRATRIE = "pegasus.demande.fratrie";
    public static final String ACTION_HOME = "pegasus.home.home";
    public static final String ACTION_HOME_AJAX = "pegasus.home.homeAjax";
    public static final String ACTION_HOME_PERIODE_AJAX = "pegasus.home.periodeAjax";
    public static final String ACTION_HOME_TYPE_CHAMBRE_AJAX = "pegasus.home.typeChambreAjax";
    public static final String ACTION_LOT_COMPTABILISER = "pegasus.lot.comptabiliser";
    public static final String ACTION_LOT_ORDRE_VERSEMENT = "pegasus.lot.ordreVersement";
    public static final String ACTION_LOT_ORDRE_VERSEMENT_AJAX = "pegasus.lot.ordreVersementAjax";
    public static final String ACTION_LOT_ORDRE_VERSEMENT_GENERER_LISTE = "pegasus.lot.genererListeOrdresVersement";
    public static final String ACTION_PARAMETRES_CONVERSION_RENTE = "pegasus.parametre.conversionRente";
    public static final String ACTION_PARAMETRES_FORFAIT_PRIME_ASSURANCE_MALADIE = "pegasus.parametre.forfaitPrimesAssuranceMaladie";
    public static final String ACTION_PARAMETRES_MONNAIES_ETRANGERES = "pegasus.monnaieetrangere.monnaieEtrangere";
    public static final String ACTION_PARAMETRES_VARIABLE_METIER = "pegasus.variablemetier.variableMetier";
    public static final String ACTION_PARAMETRES_ZONE_FORFAIT = "pegasus.parametre.zoneForfaits";
    public static final String ACTION_PARAMETRES_ZONE_LOCALITE = "pegasus.parametre.zoneLocalite";
    public static final String ACTION_PCACCORDEE = "pegasus.pcaccordee.pcAccordee";
    public static final String ACTION_PCACCORDEE_DETAIL = "pegasus.pcaccordee.pcAccordeeDetail";
    public static final String ACTION_PCACCORDEE_PLANCALCUL = "pegasus.pcaccordee.planCalcul";
    public static final String ACTION_PCACCORDEES_LIST = "pegasus.pcaccordee.pcAccordee";
    public static final String ACTION_PRIX_CHAMBRE = "pegasus.home.prixChambre";
    public static final String ACTION_PRIX_CHAMBRE_AJAX = "pegasus.home.prixChambreAjax";
    public static final String ACTION_PROCESS_ADAPTATION_PC = "pegasus.process.adaptationPC";
    public static final String ACTION_REVENUS_DEPENSES = "pegasus.revenusDepenses";
    public static final String ACTION_SITUATION_FAMILIALE = "pegasus.demandes.situationFamiliale";
    public static final String ACTION_TRAITMENT_DE_MASSE_DEFINITION_PARAMETRE_AJAX = "pegasus.traitementdemasse.definitionParametreAjax";
    public static final String ACTION_TRAITMENT_DE_MASSE_DEFINITION_TRAITEMENT_AJAX = "pegasus.traitementdemasse.definitionTraitementAjax";
    public static final String ACTION_TRAITMENT_DE_MASSE_LISTE_ACTION_AJAX = "pegasus.traitementdemasse.listeActionAjax";
    public static final String ACTION_TYPE_CHAMBRE = "pegasus.home.typeChambre";
    public static final String ACTION_WIDGET_CHERCHE_TIERS = "widget.pyxis.chercheTiers";

    public static final String ACTION_RPC_ANNONCE = "pegasus.rpc.annonces";
    public static final String ACTION_RPC_ANNONCE_AJAX = "pegasus.rpc.annoncesAjax";
    public static final String ACTION_RPC_DETAIL_ANNONCE_AJAX = "pegasus.rpc.detailAnnonceAjax";

    public static final String PARAM_ACTION_DEBLOCAGE_DEVALIDER_LIBERATION = "devalider";
    public static final String PARAM_ACTION_DEBLOCAGE_VALIDER_LIBERATION = "liberer";

}
