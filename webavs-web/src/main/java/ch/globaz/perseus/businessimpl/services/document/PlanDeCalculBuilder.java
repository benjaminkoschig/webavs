/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.document;

import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRStringUtils;
import ch.globaz.perseus.business.calcul.OutputCalcul;
import ch.globaz.perseus.business.calcul.OutputData;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.DecisionOO;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * @author MBO
 * 
 */
public class PlanDeCalculBuilder extends LettreEnTeteBuilder {
    private static final String CDT_DATE_DEBUT_DEMANDE = "{dateDebutDemande}";
    private static final String CDT_DATE_DECISION = "{dateDecision}";
    private static final String CDT_DATE_FIN_DEMANDE = "{dateFinDemande}";
    private static final String CDT_NOM_REQUERANT = "{nomRequerant}";
    private static final String CDT_NUM_NSS = "{numeroNSS}";

    private boolean isAfficherExcedentRevenu = false;
    private boolean isAfficherMontantPlafond = false;
    private boolean isMesureCoaching = false;
    private boolean isMesureEncouragement = false;

    public PlanDeCalculBuilder() {

    }

    protected DocumentData buildPlanDeCalcul(DocumentData data, String dateDocument, DecisionOO decisionOO,
            OutputCalcul calcul, Boolean isCopy) throws Exception {

        // Charger le tiers pour déterminer la langue et le titre
        PersonneEtendueComplexModel tiersPlanDeCalcul = TIBusinessServiceLocator.getPersonneEtendueService().read(
                decisionOO.getSimpleDecision().getIdTiersAdresseCourrier());

        // Chargement catalogue de textes pour la lettre en-tête
        getBabelContainer().RegisterCtx(IPFCatalogueTextes.CS_PLAN_DE_CALCUL);
        getBabelContainer().setCodeIsoLangue(getSession().getCode(tiersPlanDeCalcul.getTiers().getLangue()));
        getBabelContainer().load();

        // Insertion du titre Plan de calcul
        data.addData("TitrePlanDeCalcul", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 1, 1));

        // Insertion du label COPIE si copie
        if (isCopy == true) {
            data.addData("IsCopy", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 1, 2));
        }

        // Insertion du texte avec date de debut et date de fin
        if (!JadeStringUtil.isBlankOrZero(decisionOO.getDemande().getSimpleDemande().getDateFin())) {
            String datePeriode = PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 1, 3),
                    PlanDeCalculBuilder.CDT_DATE_DECISION, dateDocument);

            datePeriode = PRStringUtils.replaceString(datePeriode, PlanDeCalculBuilder.CDT_DATE_DEBUT_DEMANDE,
                    decisionOO.getDemande().getSimpleDemande().getDateDebut());

            data.addData(
                    "TextePeriode",
                    PRStringUtils.replaceString(datePeriode, PlanDeCalculBuilder.CDT_DATE_FIN_DEMANDE, decisionOO
                            .getDemande().getSimpleDemande().getDateFin()));
        } else {
            // Insertion du texte avec date de debut
            String datePeriode = PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 1, 2),
                    PlanDeCalculBuilder.CDT_DATE_DECISION, dateDocument);
            data.addData(
                    "TextePeriode",
                    PRStringUtils.replaceString(datePeriode, PlanDeCalculBuilder.CDT_DATE_DEBUT_DEMANDE, decisionOO
                            .getDemande().getSimpleDemande().getDateDebut()));
        }

        // Insertion de l'ayant droit avec NSS
        String numNss = decisionOO.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                .getPersonneEtendue().getNumAvsActuel();
        if (!JadeStringUtil.isEmpty(numNss)) {
            String ayantDroitNss = PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 1, 4),
                    PlanDeCalculBuilder.CDT_NUM_NSS, numNss);
            data.addData(
                    "NssAyantDroit",
                    PRStringUtils.replaceString(ayantDroitNss, PlanDeCalculBuilder.CDT_NOM_REQUERANT, decisionOO
                            .getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                            .getDesignation1()
                            + " "
                            + decisionOO.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                                    .getTiers().getDesignation2()));
        } else {
            throw new DecisionException("PlanDeCalculBuilder - Absence de l'ayant droit et de son numero NSS");
        }

        // Insertion de l'unitée monaitaire CHF
        data.addData("CHF", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 1, 5));

        // ###################################################################################
        // INSERTION DES DONNEES DANS LE PREMIER TABLEAU : FORTUNE
        // ###################################################################################

        // Insertion du titre du 1er tableau : Fortune
        data.addData("TitreFortune", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 2, 1));

        // Insertion du texte de la fortune mobilière
        data.addData("FortuneMobiliere", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 2, 2));
        // Insertion du montant
        data.addData("MontantMobiliere", convertCentimes(calcul.getDonneeString(OutputData.FORTUNE_MOBILIERE)));

        // Insertion du texte Fortune des enfants
        data.addData("FortuneEnfants", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 2, 3));
        // Insertion du montant
        data.addData("MontantEnfants", convertCentimes(calcul.getDonneeString(OutputData.FORTUNE_ENFANTS)));

        // Insertion du texte des immeubles habité par le requerant
        data.addData("ImmeubleHabite", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 2, 4));
        // Insertion du montant
        data.addData("MontantHabite", convertCentimes(calcul.getDonneeString(OutputData.FORTUNE_IMMEUBLE_HABITE)));

        // Insertion du texte de déduction légale d'immeuble
        data.addData("DeductionLegaleImmeuble", getBabelContainer()
                .getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 2, 5));
        // Insertion du montant
        data.addData("MontantLegal", convertCentimes(calcul.getDonneeString(OutputData.FORTUNE_IMMEUBLE_HABITE_DEDUC)));
        // Insertion du montant déduit
        data.addData("MontantDeduit", convertCentimes(calcul.getDonneeString(OutputData.FORTUNE_IMMEUBLE_HABITE_MODIF)));

        // Insertion du texte autre immeuble
        data.addData("AutreImmeuble", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 2, 6));
        // Insertion du montant
        Float montantAutreImmeuble = calcul.getDonnee(OutputData.FORTUNE_BIENS_ETRANGERS)
                + calcul.getDonnee(OutputData.FORTUNE_AUTRES_IMMEUBLES);
        data.addData("MontantAutreImmeuble", convertCentimes(montantAutreImmeuble.toString()));

        // Insertion du texte Dette Hypothecaire
        data.addData("DetteHypothecaire", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 2, 7));
        // Insertion du montant
        data.addData("MontantHypothecaire", convertCentimes(calcul.getDonneeString(OutputData.DETTE_HYPOTHECAIRES)));

        // Insertion du texte Autres Dettes
        data.addData("AutreDette", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 2, 8));
        // Insertion du montant
        data.addData("MontantAutreDette", convertCentimes(calcul.getDonneeString(OutputData.DETTE_AUTRES_DETTES)));

        // Insertion du texte Deduction légale
        data.addData("DeductionLegale", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 2, 9));
        // Insertion du montant
        data.addData("MontantDeductionLegale",
                convertCentimes(calcul.getDonneeString(OutputData.DETTE_DEDUCTION_LEGALE)));
        // Insertion du montant apres deduction
        data.addData("MontantPreTotalFortune", convertCentimes(calcul.getDonneeString(OutputData.FORTUNE_NETTE)));

        // Insertion du texte Total Fortune Nette
        data.addData("TotalFortuneNette", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 2, 10));
        // Insertion du montant
        data.addData("MontantTotalFortune", convertCentimes(calcul.getDonneeString(OutputData.FORTUNE_NETTE)));

        // ###################################################################################
        // INSERTION DES DONNEES DANS LE DEUXIEME TABLEAU : REVENUS
        // ###################################################################################

        // Insertion du titre du tableau
        data.addData("TitreRevenu", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 3, 1));

        // Insertion du texte Imputation fortune
        data.addData("ImputationFortune", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 3, 2));
        // Insertion du montant
        data.addData("MontantImputationFortune", convertCentimes(calcul.getDonneeString(OutputData.FORTUNE_NETTE)));
        // Insertion du montant modifié
        data.addData("MontantImputationFortuneDeduit",
                convertCentimes(calcul.getDonneeString(OutputData.REVENUS_FORTUNE_NETTE_MODIF)));

        // Insertion du texte Revenu d'une activiét lucrative
        data.addData("ActiviteLucrative", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 3, 3));
        // Insertion du montant
        data.addData("MontantBrutActiviteLucrative",
                convertCentimes(calcul.getDonneeString(OutputData.REVENUS_ACTIVITE)));
        // Insertion du montant modifié
        data.addData("MontantNetActiviteLucrative",
                convertCentimes(calcul.getDonneeString(OutputData.REVENUS_ACTIVITE_MODIF)));

        // Insertion du texte Revenus des enfants
        data.addData("RevenusEnfants", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 3, 4));
        // Insertion du montant
        data.addData("MontantRevenusEnfants", convertCentimes(calcul.getDonneeString(OutputData.REVENUS_ENFANTS)));

        // Insertion du texte Indemnité journalière d'assurances
        data.addData("IndemniteJournaliereAssurance",
                getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 3, 5));
        // Insertion du montant
        data.addData("MontantIndemniteAssurance",
                convertCentimes(calcul.getDonneeString(OutputData.REVENUS_INDEMNITES_JOURNALIERES)));

        // Insertion du texte Revenu Hypothetique
        data.addData("RevenuHypothetique", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 3, 6));
        // Insertion du montant
        data.addData("MontantHypothetique",
                convertCentimes(calcul.getDonneeString(OutputData.REVENUS_HYPOTHETIQUE_MODIF)));

        // Insertion du texte PensionsAllocations
        data.addData("PensionAllocation", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 3, 7));
        // Insertion du montant
        data.addData("MontantPensionAllocation",
                convertCentimes(calcul.getDonneeString(OutputData.REVENUS_PRESTATIONS_RECUEES)));

        // Insertion du texte Rendement de Fortune
        data.addData("RendementFortune", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 3, 8));
        // Insertion du texte Mobilier
        data.addData("Mobiliere", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 3, 9));
        // Insertion du montant
        data.addData("MontantRendementMobiliere",
                convertCentimes(calcul.getDonneeString(OutputData.REVENUS_RENDEMENT_FORTUNE_MOBILIERE)));
        // Insertion du texte Mobilier
        data.addData("Immobiliere", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 3, 10));
        // Insertion du montant
        data.addData("MontantRendementImmobiliere",
                convertCentimes(calcul.getDonneeString(OutputData.REVENUS_RENDEMENT_FORTUNE_IMMOBILIERE)));
        // Insertion du montant total du rendement
        Float montantTotalRendement = ((calcul.getDonnee(OutputData.REVENUS_RENDEMENT_FORTUNE_MOBILIERE)) + (calcul
                .getDonnee(OutputData.REVENUS_RENDEMENT_FORTUNE_IMMOBILIERE)));
        data.addData("MontantRendement", convertCentimes(montantTotalRendement.toString()));

        // Insertion du texte Autre revenus
        data.addData("AutreRevenu", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 3, 11));
        // Insertion du montant
        data.addData("MontantAutreRevenu", convertCentimes(calcul.getDonneeString(OutputData.REVENUS_AUTRES_REVENUS)));
        // Insertion du montant total revenu
        data.addData("MontantTotalRevenu", convertCentimes(calcul.getDonneeString(OutputData.REVENUS_DETERMINANT)));

        // Insertion du texte Montant Total Revenu
        data.addData("TotalRevenuDeterminant", getBabelContainer()
                .getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 3, 12));
        // Insertion du montant
        data.addData("MontantTotalRevenu", convertCentimes(calcul.getDonneeString(OutputData.REVENUS_DETERMINANT)));

        // ###################################################################################
        // INSERTION DES DONNEES DANS LE TROISIEME TABLEAU : DEPENSES
        // ###################################################################################

        // Insertion du titre
        data.addData("TitreDepense", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 4, 1));

        // Insertion du texte Couverture des besoins vitaux
        data.addData("CouvertureBesoins", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 4, 2));
        // Insertion du montant
        data.addData("MontantCouvertureBesoins",
                convertCentimes(calcul.getDonneeString(OutputData.DEPENSES_BESOINS_VITAUX)));

        // Insertion du texte Loyer annuel
        data.addData("LoyerAnnuel", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 4, 3));
        // Insertion du montant
        data.addData("MontantLoyerAnnuel",
                convertCentimes(calcul.getDonneeString(OutputData.DEPENSES_LOYER_ANNUEL_MODIF)));

        // Insertion du texte Charges annuelles
        data.addData("ChargesAnnuelles", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 4, 4));
        // Insertion du montant
        data.addData("MontantChargesAnnuel",
                convertCentimes(calcul.getDonneeString(OutputData.DEPENSES_CHARGES_ANNUELLES_MODIF)));

        // Insertion du texte Cotisation AVS/AI/APG
        data.addData("Cotisations", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 4, 5));
        // Insertion du montant
        data.addData("MontantCotisations",
                convertCentimes(calcul.getDonneeString(OutputData.DEPENSES_COTISATION_NON_ACTIF)));

        // Insertion du texte Frais d'entretien d'immeuble
        data.addData("FraisEntretienImmeuble", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 4, 7));
        // Insertion du montant
        data.addData("MontantEntretienImmeuble",
                convertCentimes(calcul.getDonneeString(OutputData.DEPENSES_FRAIS_ENTRETIENS_IMMEUBLE_MODIF)));

        // Insertion du texte Interet Hypothecaire
        data.addData("InteretHypothecaire", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 4, 6));
        // Insertion du montant
        data.addData("MontantInteretHypothecaire",
                convertCentimes(calcul.getDonneeString(OutputData.DEPENSES_INTERETS_HYPOTHECAIRES)));

        // Insertion du montant total ImmeubleInteret
        data.addData("MontantTotalImmeubleInteret",
                convertCentimes(calcul.getDonneeString(OutputData.DEPENSES_FRAIS_IMMEUBLE_MODIF)));

        // Insertion du texte Pension alimentaire
        data.addData("PensionAlimentaire", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 4, 8));
        // Insertion du montant
        data.addData("MontantPensionAlimentaire",
                convertCentimes(calcul.getDonneeString(OutputData.DEPENSES_PENSION_ALIMENTAIRE_VERSEE)));

        // Insertion du texte Frais d'obtention revenus
        data.addData("FraisRevenu", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 4, 9));

        // Insertion du texte requerant
        data.addData("Requerant", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 4, 10));
        // Insertion du montant
        data.addData("MontantFraisRequerant",
                convertCentimes(calcul.getDonneeString(OutputData.DEPENSES_FRAIS_OBTENTION_REVENU_REQUERANT_MODIF)));

        // Insertion du texte conjoint
        data.addData("Conjoint", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 4, 11));
        // Insertion du montant
        data.addData("MontantFraisConjoint",
                convertCentimes(calcul.getDonneeString(OutputData.DEPENSES_FRAIS_OBTENTION_REVENU_CONJOINT_MODIF)));

        // Insertion du texte enfant(s)
        data.addData("Enfant", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 4, 12));
        // Insertion montant
        data.addData("MontantFraisEnfant",
                convertCentimes(calcul.getDonneeString(OutputData.DEPENSES_FRAIS_OBTENTION_REVENU_ENFANTS)));

        // Insertion du montant modifié
        data.addData("MontantTotalDepense", convertCentimes(calcul.getDonneeString(OutputData.DEPENSES_RECONNUES)));

        // Insertion du texte Total Depenses Reconnues
        data.addData("TotalDepensesReconnues", getBabelContainer()
                .getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 4, 14));

        // ###################################################################################
        // INSERTION DES DONNEES DANS LE QUATRIEME TABLEAU : FINAL
        // ###################################################################################
        Float MontantDeduitAnnuel = calcul.getDonnee(OutputData.PRESTATION_ANNUELLE);
        Float TotalMontantAnnuel = calcul.getDonnee(OutputData.PRESTATION_ANNUELLE_MODIF);
        if (MontantDeduitAnnuel > TotalMontantAnnuel) {
            isAfficherMontantPlafond = true;
        }

        if ((Float.parseFloat(calcul.getDonneeString(OutputData.MESURE_CHARGES_LOYER)) != 0)
                || (Float.parseFloat(calcul.getDonneeString(OutputData.MESURE_ENCOURAGEMENT)) != 0)) {
            isMesureEncouragement = true;
        }

        if ((Float.parseFloat(calcul.getDonneeString(OutputData.MESURE_COACHING)) != 0)) {
            isMesureCoaching = true;
        }

        if ((0 != calcul.getDonnee(OutputData.EXCEDANT_REVENU)) && !isMesureEncouragement && !isMesureCoaching) {
            isAfficherExcedentRevenu = true;
        }

        // Insertion du titre Calcul
        data.addData("TitreCalcul", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 5, 1));

        if (isAfficherMontantPlafond) {
            // Insertion du commentaire du calcul
            data.addData("CommentaireCalcul", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 5, 2));
        }

        // Insertion du texte Montant annuel
        data.addData("MontantPCFAnnuel", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 5, 3));
        // Insertion du montant Depense
        data.addData("MontantAnnuelBrut", convertCentimes(calcul.getDonneeString(OutputData.DEPENSES_RECONNUES)));
        // Insertion du montant Revenus
        data.addData("DeductionMontantAnnuel", convertCentimes(calcul.getDonneeString(OutputData.REVENUS_DETERMINANT)));
        // Insertion du montant Final
        data.addData("MontantDeduitAnnuel", convertCentimes(calcul.getDonneeString(OutputData.PRESTATION_ANNUELLE)));
        if (isAfficherMontantPlafond) {
            // Insertion du texte Montant plafonné
            data.addData("MontantPlafonne", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 5, 4));
            // Insertion du CHF pour le montant plafond
            data.addData("CHFPlafond", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 1, 5));
            // Insertion du montant plafonné
            data.addData("TotalMontantAnnuel",
                    convertCentimes(calcul.getDonneeString(OutputData.PRESTATION_ANNUELLE_MODIF)));
        }

        if (isAfficherExcedentRevenu) {
            // Insertion de l'excedant de revenu
            data.addData("ExcedantRevenu", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 5, 5));
            // Insertion du CHF pour l'excedant de revenu
            data.addData("CHFExcedant", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 1, 5));
            // Insertion du montant
            data.addData("MontantExcedantRevenu", convertCentimes(calcul.getDonneeString(OutputData.EXCEDANT_REVENU)));
        }

        // Insertion du texte Mesure d'encouragement si pas à zéro
        Float additionMontantsMesure = Float.valueOf(0);
        if (!isMesureCoaching) {
            if (isMesureEncouragement) {
                // Cas excedent de revenu compensé par mesure d'encouragement donnant droit à une prestation ou restant
                // en
                // excedent de revenu
                if ((0 != calcul.getDonnee(OutputData.EXCEDANT_REVENU))) {
                    data.addData("MesureEncouragement",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 5, 9));
                    // Insertion du label CHFmesure
                    data.addData("CHFmesure", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 1, 5));
                    // Addition des deux montants de mesure d'encouragement
                    additionMontantsMesure = Float.parseFloat(calcul.getDonneeString(OutputData.MESURE_CHARGES_LOYER))
                            + Float.parseFloat(calcul.getDonneeString(OutputData.MESURE_ENCOURAGEMENT));
                    // Insertion du montant de mesure d'encouragement
                    data.addData("MontantMesureEncouragement",
                            convertCentimes(String.valueOf(additionMontantsMesure * 12)));
                } else {
                    data.addData("MesureEncouragement",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 5, 7));
                    // Cas prestation augmentée par mesure d'encouragement
                    // Insertion du label CHFmesure
                    data.addData("CHFmesure", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 1, 5));
                    // Addition des deux montants de mesure d'encouragement
                    additionMontantsMesure = Float.parseFloat(calcul.getDonneeString(OutputData.MESURE_CHARGES_LOYER))
                            + Float.parseFloat(calcul.getDonneeString(OutputData.MESURE_ENCOURAGEMENT));
                    // Insertion du montant de mesure d'encouragement
                    data.addData("MontantMesureEncouragement", convertCentimes(String.valueOf(additionMontantsMesure)));
                }

            }
        } else {
            // Insertion de la mesure de coaching à la place de la mesure d'encouragement
            data.addData("MesureEncouragement",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 5, 12));
            // Insertion du label CHFmesure
            data.addData("CHFmesure", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 1, 5));
            // Insertion du montant de mesure de coaching
            data.addData("MontantMesureEncouragement",
                    convertCentimes(calcul.getDonneeString(OutputData.MESURE_COACHING)));
        }

        // Insertion du texte Montant mensuel
        if ((0 != calcul.getDonnee(OutputData.EXCEDANT_REVENU))
                && !decisionOO.getPcfAccordee().getMontant().equals("0")) {
            data.addData("MontantPCFMensuel", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 5, 11));
            data.addData(
                    "MontantPcf",
                    convertCentimes(String.valueOf((additionMontantsMesure * 12)
                            - calcul.getDonnee(OutputData.EXCEDANT_REVENU))));
            // Insertion du texte Total
            data.addData("TexteTotal", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 5, 10));
            // Insertion du montant total
            data.addData("TotalPcfEtMesure", convertCentimes(decisionOO.getPcfAccordee().getMontant()));
        } else if ((0 != calcul.getDonnee(OutputData.EXCEDANT_REVENU)) && isMesureEncouragement
                && decisionOO.getPcfAccordee().getMontant().equals("0")) {
            data.addData("MontantPCFMensuel", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 5, 5));
            float montantNegatif = (additionMontantsMesure * 12) - calcul.getDonnee(OutputData.EXCEDANT_REVENU);

            data.addData("MontantPcf", convertCentimes(String.valueOf(montantNegatif * (-1))));
            // Insertion du texte Total
            data.addData("TexteTotal", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 5, 10));
            // Insertion du montant total
            data.addData("TotalPcfEtMesure", convertCentimes(decisionOO.getPcfAccordee().getMontant()));
        } else {

            if (decisionOO.getDemande().getSimpleDemande().getCasRigueur()) {
                data.addData("MontantPCFMensuel",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 5, 6) + " "
                                + getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 5, 13));
            } else {
                data.addData("MontantPCFMensuel",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 5, 6));
            }

            // Insertion du montant

            data.addData("MontantPcf", convertCentimes(calcul.getDonneeString(OutputData.PRESTATION_MENSUELLE)));
            // Insertion du texte Total
            data.addData("TexteTotal", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 5, 8));
            // Insertion du montant total
            data.addData("TotalPcfEtMesure", convertCentimes(decisionOO.getPcfAccordee().getMontant()));
        }

        // ###################################################################################

        // Insertion du 1er paragraphe en bas de page
        data.addData("ParagrapheUnPlanCalcul", getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 6, 1));
        // Insertion du 2eme paragraphe en bas de page, selon conditions
        if ((CSTypeDecision.REFUS_SANS_CALCUL.getCodeSystem()
                .equals(decisionOO.getSimpleDecision().getCsTypeDecision()))) {
            data.addData("ParagrapheDeuxPlanCalcul",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 6, 3));
        } else if (!(CSTypeDecision.OCTROI_PARTIEL.getCodeSystem().equals(decisionOO.getSimpleDecision()
                .getCsTypeDecision()))) {
            data.addData("ParagrapheDeuxPlanCalcul",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 6, 2));
        }
        // Insertion du titre Remarque
        data.addData("TitreRemarquePlanCalcul", getBabelContainer()
                .getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 6, 4));
        // Inseertion du texte Remarque
        data.addData("TexteRemarquePlanCalcul", getBabelContainer()
                .getTexte(IPFCatalogueTextes.CS_PLAN_DE_CALCUL, 6, 5));

        return data;
    }
}
