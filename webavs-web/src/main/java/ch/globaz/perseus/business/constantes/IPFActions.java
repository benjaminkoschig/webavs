/*
 * Créé le 22 juillet 2009
 */

package ch.globaz.perseus.business.constantes;

/**
 * @author bsc
 * 
 *         Définition des constantes pour les actions
 */

public interface IPFActions {

    public static final String ACTION_CALCUL_DROIT = "perseus.droit.calculDroit";
    public static final String ACTION_CREANCE_ACCORDEE = "perseus.creancier.creanceAccordee";
    public static final String ACTION_CREANCIER = "perseus.creancier.creancier";
    public static final String ACTION_DECISION = "perseus.decision.decision";
    public static final String ACTION_DECISION_DETAIL_AP_CALCUL = "perseus.decision.decisionApresCalcul";
    public static final String ACTION_DECISION_DETAIL_REFUS = "perseus.decision.decisionRefus";
    public static final String ACTION_DECISION_DETAIL_SUPPRESSION = "perseus.decision.decisionSuppression";
    public static final String ACTION_DEMANDE = "perseus.demande.demande";
    public static final String ACTION_DEMANDE_PC_AVS_AI = "perseus.demande.demandePCAVSAIOuvertureQD";
    public static final String ACTION_DOSSIER = "perseus.dossier.dossier";
    public static final String ACTION_DOSSIER_INFORMATION_FACTURE = "perseus.informationfacture.informationFacture";
    public static final String ACTION_DROIT = "perseus.droit.droit";
    public static final String ACTION_DROIT_ACTIVITE_LUCRATIVE_DEPENDANTE = "perseus.revenusdepenses.revenuActiviteLucrativeDependante";
    public static final String ACTION_DROIT_ACTIVITE_LUCRATIVE_DEPENDANTE_AJAX = "perseus.revenusdepenses.revenuActiviteLucrativeDependanteAjax";
    public static final String ACTION_DROIT_ACTIVITE_LUCRATIVE_INDEPENDANTE = "perseus.revenusdepenses.revenuActiviteLucrativeIndependante";
    public static final String ACTION_DROIT_ACTIVITE_LUCRATIVE_INDEPENDANTE_AJAX = "perseus.revenusdepenses.revenuActiviteLucrativeIndependanteAjax";
    public static final String ACTION_DROIT_ALLOCATION_IMPOTENT_AVS_AI = "perseus.renteijapi.allocationImpotent";
    public static final String ACTION_DROIT_ALLOCATION_IMPOTENT_AVS_AI_AJAX = "perseus.renteijapi.allocationImpotentAjax";
    public static final String ACTION_DROIT_ALLOCATIONS_FAMILIALES = "perseus.revenusdepenses.allocationsFamiliales";
    public static final String ACTION_DROIT_ALLOCATIONS_FAMILIALES_AJAX = "perseus.revenusdepenses.allocationsFamilialesAjax";
    public static final String ACTION_DROIT_ASSURANCE_RENTE_VIAGERE = "perseus.fortuneparticuliere.assuranceRenteViagere";
    public static final String ACTION_DROIT_ASSURANCE_RENTE_VIAGERE_AJAX = "perseus.fortuneparticuliere.assuranceRenteViagereAjax";
    public static final String ACTION_DROIT_ASSURANCE_VIE = "perseus.fortuneusuelle.assuranceVie";
    public static final String ACTION_DROIT_ASSURANCE_VIE_AJAX = "perseus.fortuneusuelle.assuranceVieAjax";

    public static final String ACTION_DROIT_AUTRE_FORTUNE_MOBILIERE = "perseus.fortuneparticuliere.autreFortuneMobiliere";

    public static final String ACTION_DROIT_AUTRE_FORTUNE_MOBILIERE_AJAX = "perseus.fortuneparticuliere.autreFortuneMobiliereAjax";
    public static final String ACTION_DROIT_AUTRES_API = "perseus.renteijapi.autreApi";

    public static final String ACTION_DROIT_AUTRES_API_AJAX = "perseus.renteijapi.autreApiAjax";
    public static final String ACTION_DROIT_AUTRES_DETTES_PROUVEES = "perseus.fortuneusuelle.autresDettesProuvees";
    public static final String ACTION_DROIT_AUTRES_DETTES_PROUVEES_AJAX = "perseus.fortuneusuelle.autresDettesProuveesAjax";
    public static final String ACTION_DROIT_AUTRES_RENTES = "perseus.renteijapi.autreRente";
    public static final String ACTION_DROIT_AUTRES_RENTES_AJAX = "perseus.renteijapi.autreRenteAjax";
    public static final String ACTION_DROIT_AUTRES_REVENUS = "perseus.revenusdepenses.autresRevenus";
    public static final String ACTION_DROIT_AUTRES_REVENUS_AJAX = "perseus.revenusdepenses.autresRevenusAjax";
    public static final String ACTION_DROIT_AVS_AI = "perseus.renteijapi.renteAvsAi";
    public static final String ACTION_DROIT_AVS_AI_AJAX = "perseus.renteijapi.renteAvsAiAjax";
    public static final String ACTION_DROIT_BETAIL = "perseus.fortuneparticuliere.betail";
    public static final String ACTION_DROIT_BETAIL_AJAX = "perseus.fortuneparticuliere.betailAjax";
    public static final String ACTION_DROIT_BIEN_IMMOBILIER_NH = "perseus.fortuneusuelle.bienImmobilierNonHabitable";
    public static final String ACTION_DROIT_BIEN_IMMOBILIER_NH_AJAX = "perseus.fortuneusuelle.bienImmobilierNonHabitableAjax";
    public static final String ACTION_DROIT_BIEN_IMMOBILIER_NSPHP = "perseus.fortuneusuelle.bienImmobilierHabitationNonPrincipale";
    public static final String ACTION_DROIT_BIEN_IMMOBILIER_NSPHP_AJAX = "perseus.fortuneusuelle.bienImmobilierHabitationNonPrincipaleAjax";
    public static final String ACTION_DROIT_BIEN_IMMOBILIER_SHP = "perseus.fortuneusuelle.bienImmobilierServantHabitationPrincipale";
    public static final String ACTION_DROIT_BIEN_IMMOBILIER_SHP_AJAX = "perseus.fortuneusuelle.bienImmobilierServantHabitationPrincipaleAjax";
    public static final String ACTION_DROIT_CAPITAL_LPP = "perseus.fortuneusuelle.capitalLPP";
    public static final String ACTION_DROIT_CAPITAL_LPP_AJAX = "perseus.fortuneusuelle.capitalLPPAjax";
    public static final String ACTION_DROIT_COMPTE_BANCAIRE_CCP = "perseus.fortuneusuelle.compteBancaireCCP";

    public static final String ACTION_DROIT_COMPTE_BANCAIRE_CCP_AJAX = "perseus.fortuneusuelle.compteBancaireCCPAjax";
    public static final String ACTION_DROIT_CONTRAT_ENTRETIEN_VIAGER = "perseus.revenusdepenses.contratEntretienViager";

    public static final String ACTION_DROIT_CONTRAT_ENTRETIEN_VIAGER_AJAX = "perseus.revenusdepenses.contratEntretienViagerAjax";
    public static final String ACTION_DROIT_COTISATIONS_PSAL = "perseus.revenusdepenses.cotisationsPsal";

    public static final String ACTION_DROIT_COTISATIONS_PSAL_AJAX = "perseus.revenusdepenses.cotisationsPsalAjax";
    public static final String ACTION_DROIT_DESSAISISSEMENT_FORTUNE = "perseus.dessaisissement.dessaisissementFortune";
    public static final String ACTION_DROIT_DESSAISISSEMENT_FORTUNE_AJAX = "perseus.dessaisissement.dessaisissementFortuneAjax";
    public static final String ACTION_DROIT_DESSAISISSEMENT_REVENU = "perseus.dessaisissement.dessaisissementRevenu";
    public static final String ACTION_DROIT_DESSAISISSEMENT_REVENU_AJAX = "perseus.dessaisissement.dessaisissementRevenuAjax";

    public static final String ACTION_DROIT_DONNEES_PERSONNELLES = "perseus.droit.saisieDonneesPersonnelles";
    public static final String ACTION_DROIT_DONNEES_PERSONNELLES_AJAX = "perseus.droit.saisieDonneesPersonnellesAjax";

    public static final String ACTION_DROIT_IJAI = "perseus.renteijapi.indemniteJournaliereAi";
    public static final String ACTION_DROIT_IJAI_AJAX = "perseus.renteijapi.indemniteJournaliereAiAjax";
    public static final String ACTION_DROIT_INDEMNITES_JOURNALIERES_APG = "perseus.renteijapi.ijApg";
    public static final String ACTION_DROIT_INDEMNITES_JOURNALIERES_APG_AJAX = "perseus.renteijapi.ijApgAjax";
    public static final String ACTION_DROIT_LOYER = "perseus.habitat.loyer";
    public static final String ACTION_DROIT_LOYER_AJAX = "perseus.habitat.loyerAjax";
    public static final String ACTION_DROIT_MARCHANDISES_STOCK = "perseus.fortuneparticuliere.marchandisesStock";
    public static final String ACTION_DROIT_MARCHANDISES_STOCK_AJAX = "perseus.fortuneparticuliere.marchandisesStockAjax";
    public static final String ACTION_DROIT_NUMERAIRE = "perseus.fortuneparticuliere.numeraire";
    public static final String ACTION_DROIT_NUMERAIRE_AJAX = "perseus.fortuneparticuliere.numeraireAjax";
    public static final String ACTION_DROIT_PENSION_ALIMENTAIRE = "perseus.revenusdepenses.pensionAlimentaire";
    public static final String ACTION_DROIT_PENSION_ALIMENTAIRE_AJAX = "perseus.revenusdepenses.pensionAlimentaireAjax";

    public static final String ACTION_DROIT_PRET_ENVERS_TIERS = "perseus.fortuneparticuliere.pretEnversTiers";
    public static final String ACTION_DROIT_PRET_ENVERS_TIERS_AJAX = "perseus.fortuneparticuliere.pretEnversTiersAjax";

    public static final String ACTION_DROIT_REVENU_HYPOTHETIQUE = "perseus.revenusdepenses.revenuHypothetique";
    public static final String ACTION_DROIT_REVENU_HYPOTHETIQUE_AJAX = "perseus.revenusdepenses.revenuHypothetiqueAjax";
    public static final String ACTION_DROIT_TAXE_JOURNALIERE_HOME = "perseus.habitat.taxeJournaliereHome";
    public static final String ACTION_DROIT_TAXE_JOURNALIERE_HOME_AJAX = "perseus.habitat.taxeJournaliereHomeAjax";
    public static final String ACTION_DROIT_TITRE = "perseus.fortuneusuelle.titre";

    public static final String ACTION_DROIT_TITRE_AJAX = "perseus.fortuneusuelle.titreAjax";
    public static final String ACTION_DROIT_VEHICULE = "perseus.fortuneparticuliere.vehicule";

    public static final String ACTION_DROIT_VEHICULE_AJAX = "perseus.fortuneparticuliere.vehiculeAjax";

    public static final String ACTION_VALIDATION_FACTURE = "perseus.qd.validationFacture";

    public static final String ACTION_FACTURE = "perseus.qd.detailfacture";
    public static final String ACTION_PAIEMENTS = "perseus.paiements.validationDecision";
    public static final String ACTION_PCFACCORDEE = "perseus.pcfaccordee.calcul";

    public static final String ACTION_ECHEANCELIBRE = "perseus.echeance.echeanceLibre";

    public static final String ACTION_HOME = "perseus.home.home";
    public static final String ACTION_HOME_AJAX = "perseus.home.homeAjax";

    public static final String ACTION_HOME_PERIODE_AJAX = "perseus.home.periodeAjax";
    public static final String ACTION_HOME_TYPE_CHAMBRE_AJAX = "perseus.home.typeChambreAjax";

    // Création du document de décision
    public static final String ACTION_IMPRIMER_DOC = "perseus.decision.decisionProcess.executer";
    public static final String ACTION_IMPRIMER_REFUS_FACTURE = "perseus.decision.decisionRefusFactureDocProcess.executer";

    public static final String ACTION_PARAMETRES_CONVERSION_RENTE = "perseus.parametre.conversionRente";
    public static final String ACTION_PARAMETRES_FORFAIT_PRIME_ASSURANCE_MALADIE = "perseus.parametre.forfaitPrimesAssuranceMaladie";
    public static final String ACTION_PARAMETRES_MONNAIES_ETRANGERES = "perseus.monnaieetrangere.monnaieEtrangere";
    public static final String ACTION_PARAMETRES_PERIODE_IMPOT_SOURCE = "perseus.impotsource.periodeImpotSource";
    public static final String ACTION_PARAMETRES_VARIABLE_METIER = "perseus.variablemetier.variableMetier";

    public static final String ACTION_PARAMETRES_ZONE_FORFAIT = "perseus.parametre.zoneForfaits";
    public static final String ACTION_PARAMETRES_ZONE_LOCALITE = "perseus.parametre.zoneLocalite";
    public static final String ACTION_PCACCORDEE = "perseus.pcaccordee.pcAccordee";

    public static final String ACTION_PCACCORDEE_DETAIL = "perseus.pcaccordee.pcAccordeeDetail";

    public static final String ACTION_PCACCORDEES_LIST = "perseus.pcaccordee.pcAccordee";
    public static final String ACTION_PRIX_CHAMBRE = "perseus.home.prixChambre";
    public static final String ACTION_REVENUS_DEPENSES = "perseus.revenusDepenses";

    public static final String ACTION_SITUATION_FAMILIALE = "perseus.demandes.situationFamiliale";

    public static final String ACTION_TYPE_CHAMBRE = "perseus.home.typeChambre";
    public static final String ACTION_WIDGET_CHERCHE_TIERS = "widget.pyxis.chercheTiers";

}