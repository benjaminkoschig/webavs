/**
 * 
 */
package globaz.perseus.utils.plancalcul;

import java.util.ArrayList;
import ch.globaz.perseus.business.calcul.OutputCalcul;
import ch.globaz.perseus.business.calcul.OutputData;

/**
 * @author DDE
 * 
 */
public class PFGroupeRevenuHandler {

    private ArrayList<PFLignePlanCalculHandler> lignes;
    private OutputCalcul outputCalcul;

    public PFGroupeRevenuHandler(OutputCalcul outputCalcul) {
        this.outputCalcul = outputCalcul;
        lignes = new ArrayList<PFLignePlanCalculHandler>(0);

        genererLignes();
    }

    /**
     * Permet de générer les lignes
     */
    private void genererLignes() {
        printImputationFortune();
        printRevenuActiviteLucrative();
        printDeductionFranchise();
        printIndemnitesJournalieres();
        printRevenuHypothetique();
        printRevenusEnfants();
        printPrestationsRecues();
        printRendementFortuneMobiliere();
        printRendementFortuneImmobiliere();
        printAutresRevenus();

        printRevenuDeterminant();
    }

    /**
     * Retourne les lignes du bloc Fortune pour les données passées au constructeur du handler
     * 
     * @return the lignes
     */
    public ArrayList<PFLignePlanCalculHandler> getLignes() {
        return lignes;
    }

    private void printAutresRevenus() {
        PFLignePlanCalculHandler ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_AUTRES_REVENUS");
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_AIDES_LOGEMENT");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_AIDES_LOGEMENT));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_SOUS_LOCATION");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_SOUS_LOCATION));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_VALEUR_USUFRUIT");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_VALEUR_USUFRUIT));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_DROIT_HABITATION");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_DROIT_HABITATION));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_AUTRS_CREANCES");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_AUTRES_CREANCES));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_SUCCESSION_NON_PARTAGEE");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_SUCCESSION_NON_PARTAGEE), true, false);
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.REVENUS_AUTRES_REVENUS), true, false);
        lignes.add(ligne);

    }

    private void printDeductionFranchise() {
        PFLignePlanCalculHandler ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_DEDUCTION_FRANCHISE");
        ligne.setValCol2("-" + outputCalcul.getDonneeString(OutputData.REVENUS_DEDUCTION_FRANCHISE), true, false);
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.REVENUS_ACTIVITE_MODIF));
        lignes.add(ligne);
    }

    private void printImputationFortune() {
        PFLignePlanCalculHandler ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_IMPUTATION_FORTUNE");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.FORTUNE_NETTE));
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.REVENUS_FORTUNE_NETTE_MODIF));
        lignes.add(ligne);

    }

    private void printIndemnitesJournalieres() {
        PFLignePlanCalculHandler ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_INDEM_JOURN_ASS");
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_IJA_MALADIE");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_INDEMNITE_JOURNALIERE_MALADIE));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_IJA_ACCIDENTS");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_INDEMNITE_JOURNALIERE_ACCIDENTS));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_IJA_CHOMAGE");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_INDEMNITE_JOURNALIERE_CHOMAGE));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_IJA_APG");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_INDEMNITE_JOURNALIERE_APG));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_IJA_MILITAIRE");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_INDEMNITE_JOURNALIERE_MILITAIRE));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_IJA_AI");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_INDEMNITE_JOURNALIERE_AI), true, false);
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.REVENUS_INDEMNITES_JOURNALIERES));
        lignes.add(ligne);

    }

    private void printPrestationsRecues() {
        PFLignePlanCalculHandler ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_PRESTATIONS_RECUEES");
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_PENSION_ALIMENTAIRE");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_PENSION_ALIMENTAIRE));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_ALLOCATIONS_FAMILIALES");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_ALLOCATIONS_FAMILIALES));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_ALLOCATIONS_CANT_MAT");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_ALLOCATION_CANTONALE_MATERNITE));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_ALLOCATIONS_AMINH");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_ALLOCATIONS_AMINH));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_BROUSES_ETUDES");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_AIDE_FORMATION));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_CONTRAT_VIAGER");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_CONTRAT_ENTRETIENS_VIAGER));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_RENTES");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_TOTAL_RENTES));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_AUTRES_RENTES");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_AUTRES_RENTES), true, false);
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.REVENUS_PRESTATIONS_RECUEES));
        lignes.add(ligne);

    }

    private void printRendementFortuneImmobiliere() {
        PFLignePlanCalculHandler ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_RENDEMENT_FORTUNE_IMMO");
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_LOYERS_FERMAGES");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_LOYERS_ET_FERMAGES));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_VALEUR_LOCATIVE");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_VALEUR_LOCATIVE_PROPRE_IMMEUBLE), true, false);
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.REVENUS_RENDEMENT_FORTUNE_IMMOBILIERE));
        lignes.add(ligne);

    }

    private void printRendementFortuneMobiliere() {
        PFLignePlanCalculHandler ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_RENDEMENT_FORTUNE_MOBILIERE");
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_INTERETS_FORTUNE");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_INTERET_FORTUNE));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_INTERETS_FORTUNE_DESSAISIE");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_INTERET_FORTUNE_DESSAISIE), true, false);
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.REVENUS_RENDEMENT_FORTUNE_MOBILIERE));
        lignes.add(ligne);

    }

    private void printRevenuActiviteLucrative() {
        PFLignePlanCalculHandler ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_REVNENU_ACTIVITE_LUCRATIVE_REQUERANT");
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_SALAIRE_NET");
        ligne.setValCol1(outputCalcul.getDonneeString(OutputData.REVENUS_SALAIRE_NET_REQUERANT));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_SALAIRE_NATURE");
        ligne.setValCol1(outputCalcul.getDonneeString(OutputData.REVENUS_SALAIRE_NATURE_REQUERANT));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_REVENU_INDEPENDANT");
        ligne.setValCol1(outputCalcul.getDonneeString(OutputData.REVENUS_INDEPENDANT_REQUERANT));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_REVNENU_ACTIVITE_LUCRATIVE_CONJOINT");
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_SALAIRE_NET");
        ligne.setValCol1(outputCalcul.getDonneeString(OutputData.REVENUS_SALAIRE_NET_CONJOINT));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_SALAIRE_NATURE");
        ligne.setValCol1(outputCalcul.getDonneeString(OutputData.REVENUS_SALAIRE_NATURE_CONJOINT));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_REVENU_INDEPENDANT");
        ligne.setValCol1(outputCalcul.getDonneeString(OutputData.REVENUS_INDEPENDANT_CONJOINT), true, false);
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_ACTIVITE));
        lignes.add(ligne);

    }

    private void printRevenuDeterminant() {
        // Ligne vide
        lignes.add(new PFLignePlanCalculHandler());

        PFLignePlanCalculHandler ligne = new PFLignePlanCalculHandler();
        ligne.setCssClass("total");
        ligne.setLibelle("JSP_PF_PLANCALCUL_TOTAL_REVENUS");
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.REVENUS_DETERMINANT), false, true);
        lignes.add(ligne);
    }

    private void printRevenuHypothetique() {
        PFLignePlanCalculHandler ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_REVENU_HYPOTHETIQUE");
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_REVENU_HYPO_PRIS");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.REVENUS_HYPOTHETIQUE));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_DEDUC_REVENU_ACT_LUC");
        ligne.setValCol2("-" + outputCalcul.getDonneeString(OutputData.REVENUS_ACTIVITE));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_DEDUC_ALL_MATERNITE");
        ligne.setValCol2("-" + outputCalcul.getDonneeString(OutputData.REVENUS_ALLOCATION_CANTONALE_MATERNITE));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_DEDUC_AIJ");
        ligne.setValCol2("-" + outputCalcul.getDonneeString(OutputData.REVENUS_INDEMNITES_JOURNALIERES), true, false);
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.REVENUS_HYPOTHETIQUE_MODIF));
        lignes.add(ligne);
    }

    private void printRevenusEnfants() {
        PFLignePlanCalculHandler ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_REVENUS_ENFANTS");
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.REVENUS_ENFANTS));
        lignes.add(ligne);

    }

}
