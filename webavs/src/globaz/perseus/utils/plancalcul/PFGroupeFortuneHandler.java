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
public class PFGroupeFortuneHandler {

    private ArrayList<PFLignePlanCalculHandler> lignes;
    private OutputCalcul outputCalcul;

    public PFGroupeFortuneHandler(OutputCalcul outputCalcul) {
        this.outputCalcul = outputCalcul;
        lignes = new ArrayList<PFLignePlanCalculHandler>(0);

        genererLignes();
    }

    /**
     * Permet de générer les lignes
     */
    private void genererLignes() {
        printFortuneMobiliere();
        printFortuneImmobiliere();
        printFortuneEnfants();
        printDettes();
        printFortuneNette();
    }

    /**
     * Retourne les lignes du bloc Fortune pour les données passées au constructeur du handler
     * 
     * @return the lignes
     */
    public ArrayList<PFLignePlanCalculHandler> getLignes() {
        return lignes;
    }

    private void printDettes() {
        PFLignePlanCalculHandler ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_DETTES_HYPOTHECAIRES");
        ligne.setValCol3("-" + outputCalcul.getDonneeString(OutputData.DETTE_HYPOTHECAIRES));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_AUTRES_DETTES");
        ligne.setValCol3("-" + outputCalcul.getDonneeString(OutputData.DETTE_AUTRES_DETTES));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_DEDUCTION_LEGALE");
        ligne.setValCol3("-" + outputCalcul.getDonneeString(OutputData.DETTE_DEDUCTION_LEGALE), true, false);
        lignes.add(ligne);

        // Ligne vide
        lignes.add(new PFLignePlanCalculHandler());

    }

    private void printFortuneEnfants() {
        PFLignePlanCalculHandler ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_FORTUNE_ENFANTS");
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.FORTUNE_ENFANTS));
        lignes.add(ligne);

        // Ligne vide
        lignes.add(new PFLignePlanCalculHandler());
    }

    private void printFortuneImmobiliere() {
        PFLignePlanCalculHandler ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_FORTUNE_IMMOBILIERE");
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_BIEN_IMMOBILIER");
        ligne.setValCol1(outputCalcul.getDonneeString(OutputData.FORTUNE_IMMEUBLE_HABITE));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;&nbsp;&nbsp;-&nbsp;JSP_PF_PLANCALCUL_DEDUC_FORFAITAIRE");
        ligne.setValCol1("-" + outputCalcul.getDonneeString(OutputData.FORTUNE_IMMEUBLE_HABITE_DEDUC), true, false);
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.FORTUNE_IMMEUBLE_HABITE_MODIF));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_IMMEUBLES_ETRANGERS");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.FORTUNE_BIENS_ETRANGERS));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_AUTRES_IMMEUBLES");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.FORTUNE_AUTRES_IMMEUBLES), true, false);
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.FORTUNE_IMMOBILIERE));
        lignes.add(ligne);

    }

    private void printFortuneMobiliere() {
        PFLignePlanCalculHandler ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_FORTUNE_MOBILIERE");
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_LIQUIDITE");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.FORTUNE_LIQUIDITE));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_VALEUR_RACHAT_ASS");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.FORTUNE_RACHAT_ASSURANCE_VIE));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_HOIRE");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.FORTUNE_HOIRIE));
        lignes.add(ligne);

        // Cession du requérant
        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_CESSION_BIENS_REQUERANT");
        ligne.setValCol1(outputCalcul.getDonneeString(OutputData.FORTUNE_CESSION_REQUERANT));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;&nbsp;&nbsp;-&nbsp;JSP_PF_PLANCALCUL_DEDUC_CESSION");
        ligne.setValCol1("-" + outputCalcul.getDonneeString(OutputData.FORTUNE_CESSION_DEDUC_REQUERANT), true, false);
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.FORTUNE_CESSION_MODIF_REQUERANT));
        lignes.add(ligne);

        // Cession du conjoint
        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_CESSION_BIENS_CONJOINT");
        ligne.setValCol1(outputCalcul.getDonneeString(OutputData.FORTUNE_CESSION_CONJOINT));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;&nbsp;&nbsp;-&nbsp;JSP_PF_PLANCALCUL_DEDUC_CESSION");
        ligne.setValCol1("-" + outputCalcul.getDonneeString(OutputData.FORTUNE_CESSION_DEDUC_CONJOINT), true, false);
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.FORTUNE_CESSION_MODIF_CONJOINT));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_AUTRES_BIENS");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.FORTUNE_AUTRE_BIEN), true, false);
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.FORTUNE_MOBILIERE));
        lignes.add(ligne);
    }

    private void printFortuneNette() {
        PFLignePlanCalculHandler ligne = new PFLignePlanCalculHandler();
        ligne.setCssClass("total");
        ligne.setLibelle("JSP_PF_PLANCALCUL_TOTAL_FORTUNE");
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.FORTUNE_NETTE), false, true);
        lignes.add(ligne);
    }

}
