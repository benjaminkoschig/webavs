/*
 * Created on 05-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.controller.FWActionHandlerFactoryMapSupport;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.widget.FWJadeWidgetServletAction;
import java.util.HashMap;
import java.util.Map;

/**
 * Class pour le mapping des actions avec les classes de traitement correspondantes.
 * 
 * @author sau
 */
public class AFMainServletAction extends FWActionHandlerFactoryMapSupport {

    private static Map<String, Class<?>> hActionsTable = new HashMap<String, Class<?>>();

    static {
        AFMainServletAction.hActionsTable.put("naos.adhesion.adhesion", AFActionAdhesion.class);
        AFMainServletAction.hActionsTable.put("naos.affiliation.autreDossier", AFActionAutreDossier.class);
        AFMainServletAction.hActionsTable.put("naos.affiliation.ancienNumero", AFActionAncienNumero.class);
        AFMainServletAction.hActionsTable.put("naos.affiliation.affiliation", AFActionAffiliation.class);
        AFMainServletAction.hActionsTable.put("naos.affiliation.affiliationProvisoires",
                AFActionAffiliationProvisoires.class);
        AFMainServletAction.hActionsTable.put("naos.affiliation.affiliationNonProvisoires",
                AFActionAffiliationProvisoires.class);
        AFMainServletAction.hActionsTable.put("naos.annonceAffilie.annonceAffilie", AFActionAnnonceAffilie.class);
        AFMainServletAction.hActionsTable.put("naos.assurance.assurance", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.avisMutation.avisMutation", AFActionAvisMutation.class);
        AFMainServletAction.hActionsTable.put("naos.controleEmployeur.controleEmployeur",
                AFActionControleEmployeur.class);
        AFMainServletAction.hActionsTable.put("naos.controleEmployeur.reviseur", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.controleEmployeur.saisieRapideReviseur",
                FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.controleEmployeur.imprimerControle", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.controleEmployeur.imprimerlettrelibre",
                FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.controleEmployeur.lettreProchainControle",
                AFActionControleEmployeur.class);
        // hActionsTable.put("naos.controleEmployeur.statOFASControle",AFActionControleEmployeur.class);
        AFMainServletAction.hActionsTable.put("naos.controleEmployeur.controlesAttribues",
                AFActionControlesAttribues.class);
        AFMainServletAction.hActionsTable.put("naos.controleEmployeur.controlesAEffectuer",
                AFActionControlesAEffectuer.class);
        AFMainServletAction.hActionsTable.put("naos.controleEmployeur.attributionPts", AFActionAttributionPts.class);
        AFMainServletAction.hActionsTable.put("naos.cotisation.cotisation", AFActionCotisation.class);
        AFMainServletAction.hActionsTable.put("naos.couverture.couverture", AFActionCouverture.class);
        AFMainServletAction.hActionsTable.put("naos.fact.fact", AFActionFact.class);
        AFMainServletAction.hActionsTable.put("naos.fact.factAffilie", AFActionFactAffilie.class);
        AFMainServletAction.hActionsTable.put("naos.fact.factCotisation", AFActionFactCotisation.class);
        AFMainServletAction.hActionsTable.put("naos.journalisation.journal", AFActionJournalisation.class);
        AFMainServletAction.hActionsTable.put("naos.lienAffiliation.lienAffiliation", AFActionLienAffiliation.class);
        AFMainServletAction.hActionsTable.put("naos.lienAffiliation.affilieLienAffiliation",
                AFActionLienAffiliation.class);
        AFMainServletAction.hActionsTable.put("naos.masse.masse", AFActionMasse.class);
        AFMainServletAction.hActionsTable.put("naos.masse.cotisationMasse", AFActionMasse.class);
        AFMainServletAction.hActionsTable.put("naos.masse.masseModifier", AFActionMasseModifier.class);
        AFMainServletAction.hActionsTable.put("naos.nombreAssures.nombreAssures", AFActionNombreAssures.class);
        AFMainServletAction.hActionsTable.put("naos.particulariteAffiliation.particulariteAffiliation",
                AFActionParticulariteAffiliation.class);
        AFMainServletAction.hActionsTable.put("naos.parametreAssurance.parametreAssurance",
                AFActionParametreAssurance.class);
        AFMainServletAction.hActionsTable.put("naos.plan.plan", AFActionPlan.class);
        AFMainServletAction.hActionsTable.put("naos.planAffiliation.planAffiliation", AFActionPlanAffiliation.class);
        AFMainServletAction.hActionsTable.put("naos.planCaisse.planCaisse", AFActionPlanCaisse.class);
        AFMainServletAction.hActionsTable.put("naos.releve.apercuReleve", AFActionApercuReleve.class);
        AFMainServletAction.hActionsTable.put("naos.releve.gestionReleve", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.rentier.export", AFActionRentier.class);
        AFMainServletAction.hActionsTable.put("naos.suiviAssurance.suiviAssurance", AFActionSuiviAssurance.class);
        AFMainServletAction.hActionsTable.put("naos.suiviCaisseAffiliation.suiviCaisseAffiliation",
                AFActionSuiviCaisseAffiliation.class);
        AFMainServletAction.hActionsTable.put("naos.tauxAssurance.tauxAssurance", AFActionTauxAssurance.class);
        AFMainServletAction.hActionsTable.put("naos.tent.export", AFActionTent.class);
        AFMainServletAction.hActionsTable.put("naos.wizard.wizard", AFActionWizard.class);
        AFMainServletAction.hActionsTable.put("naos.masse.annonceSalaires", AFDefaultActionChercher.class);
        AFMainServletAction.hActionsTable.put("naos.masse.confirmationSalaires", AFDefaultActionChercher.class);
        AFMainServletAction.hActionsTable.put("naos.acompte.bouclementAcompteImprimer", AFDefaultActionChercher.class);
        AFMainServletAction.hActionsTable.put("naos.acompte.decisionAcompteImprimer", AFDefaultActionChercher.class);
        AFMainServletAction.hActionsTable.put("naos.acompte.previsionAcompteImprimer", AFDefaultActionChercher.class);
        AFMainServletAction.hActionsTable.put("naos.calculRetroactif.calculRetroactif", AFActionCalculRetroactif.class);
        AFMainServletAction.hActionsTable.put("naos.affiliation.affiliationNAIndSansCI", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.affiliation.attestationAffiliation", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.affiliation.soldesAffiliesRadies", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.affiliation.ficheCartotheque", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.affiliation.bordereauMutation", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.affiliation.lettreBienvenue", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.annonceAffilie.impressionMutation", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.beneficiairepc.quittance", AFActionBeneficiairepc.class);
        AFMainServletAction.hActionsTable.put("naos.beneficiairepc.journalQuittance", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.beneficiairepc.impression", AFActionImpressionQuittances.class);
        AFMainServletAction.hActionsTable.put("naos.beneficiairepc.journalQuittances", AFActionBeneficiairepc.class);
        AFMainServletAction.hActionsTable.put("naos.beneficiairepc.impressionErreurs",
                AFActionImpressionQuittancesErreurs.class);
        AFMainServletAction.hActionsTable.put("naos.beneficiairepc.processGenererQuittances",
                FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.beneficiairepc.processComptabiliserCiQuittances",
                FWDefaultServletAction.class);

        AFMainServletAction.hActionsTable.put("naos.tauxAssurance.tauxMoyen", AFActionTauxMoyen.class);
        AFMainServletAction.hActionsTable.put("naos.affiliation.statistiquesOfas", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.masse.trimestrielleAvecMasseSupp", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.affiliation.epuCasNonSoumis", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.decisionCotisations.decisionCotisations",
                AFDefaultActionChercher.class);
        AFMainServletAction.hActionsTable.put("naos.listeDeces.listeDeces", AFDefaultActionChercher.class);
        AFMainServletAction.hActionsTable.put("naos.listeAgenceCommunale.listeAgenceCommunale",
                FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.listeAgenceCommunale.listeAgenceCommunale",
                FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.taxeCo2.figerTaxeCo2", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.taxeCo2.taxeCo2", AFActionTaxeCo2.class);
        AFMainServletAction.hActionsTable.put("naos.taxeCo2.listeRadieTaxeCo2", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.taxeCo2.listeExcelRadieTaxeCo2", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.taxeCo2.listeExcelTaxeCo2", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.taxeCo2.lettreTaxeCo2", AFActionTaxeCo2.class);
        AFMainServletAction.hActionsTable.put("naos.taxeCo2.masseTaxeCo2", AFActionTaxeCo2.class);
        AFMainServletAction.hActionsTable.put("naos.taxeCo2.reinjectionListeExcel", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.taxeCo2.reloadAnnee", AFActionTaxeCo2.class);

        // hActionsTable.put("naos.statOfas.statOFASControle",
        // AFActionStatOfas.class);
        AFMainServletAction.hActionsTable.put("naos.statOfas.statOFASControle", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.statOfas.statistiquesOfas", FWDefaultServletAction.class);

        // InfoRom210
        AFMainServletAction.hActionsTable.put("naos.affiliation.listeAffiliationModeFacturation",
                FWDefaultServletAction.class);

        // InfoRom211
        AFMainServletAction.hActionsTable
                .put("naos.affiliation.listeNouvelleAffiliation", FWDefaultServletAction.class);

        // Inforom270
        AFMainServletAction.hActionsTable.put("naos.attestation.attestationPersonnelle", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.process.attestationPersonnelle", FWDefaultServletAction.class);

        // InfoRom301
        AFMainServletAction.hActionsTable.put("naos.affiliation.listeAffilieFraisGestionReduit",
                FWDefaultServletAction.class);

        // InfoRom300
        AFMainServletAction.hActionsTable
                .put("naos.attestation.attestationChaSocProcess", FWDefaultServletAction.class);

        // Controle LPP annuel
        AFMainServletAction.hActionsTable.put("naos.controleLpp.controleLppAnnuel", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.process.controleLppAnnuel", FWDefaultServletAction.class);
        // Inforom 432
        AFMainServletAction.hActionsTable.put("naos.acompte.comparaisonAcompteMasse", FWDefaultServletAction.class);

        AFMainServletAction.hActionsTable.put("naos.affiliation.listeNbrAssuresByAffiliation",
                FWDefaultServletAction.class);

        AFMainServletAction.hActionsTable.put("widget.action.jade", FWJadeWidgetServletAction.class);

        // InfoRomD0050(IDE)
        AFMainServletAction.hActionsTable.put("naos.ide.ideSearch", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.ide.ideAnnonce", AFActionIdeAnnonce.class);
        AFMainServletAction.hActionsTable.put("naos.ide.ideTraitementAnnonce", FWDefaultServletAction.class);
        AFMainServletAction.hActionsTable.put("naos.ide.ideSyncRegistre", FWDefaultServletAction.class);
    }

    /**
     * Constructeur d'AFMainServletAction.
     */
    public AFMainServletAction() {
        super();
    }

    /**
     * Retourne la table de mapping des actions.
     * 
     * @see globaz.framework.controller.FWActionHandlerFactoryMapSupport#getMap()
     */
    @Override
    public Map<String, Class<?>> getMap() {
        return AFMainServletAction.hActionsTable;
    };

}
