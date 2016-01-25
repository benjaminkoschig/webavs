/**
 * 
 */
package globaz.perseus.utils.plancalcul;

import java.util.ArrayList;
import ch.globaz.perseus.business.calcul.OutputCalcul;
import ch.globaz.perseus.business.calcul.OutputData;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;

/**
 * @author DDE
 * 
 */
public class PFGroupePrestationHandler {

    private ArrayList<PFLignePlanCalculHandler> lignes;
    private OutputCalcul outputCalcul;
    private PCFAccordee pcfAccordee;

    public PFGroupePrestationHandler(PCFAccordee pcfAccordee) {
        this.pcfAccordee = pcfAccordee;
        outputCalcul = pcfAccordee.getCalcul();
        lignes = new ArrayList<PFLignePlanCalculHandler>(0);

        genererLignes();
    }

    /**
     * Permet de générer les lignes
     */
    private void genererLignes() {

        printPrestationComplementaireFamille();
    }

    /**
     * Retourne les lignes du bloc Fortune pour les données passées au constructeur du handler
     * 
     * @return the lignes
     */
    public ArrayList<PFLignePlanCalculHandler> getLignes() {
        return lignes;
    }

    private void printPrestationComplementaireFamille() {
        PFLignePlanCalculHandler ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_TOTAL_DEPENSES");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.DEPENSES_RECONNUES));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_TOTAL_REVENUS");
        ligne.setValCol2("-" + outputCalcul.getDonneeString(OutputData.REVENUS_DETERMINANT), true, false);
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_PRESTATION_ANNUELLE");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.PRESTATION_ANNUELLE));
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.PRESTATION_ANNUELLE_MODIF));
        lignes.add(ligne);

        lignes.add(new PFLignePlanCalculHandler());

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_EXCEDANT_REVENU");
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.EXCEDANT_REVENU));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setCssClass("total");
        ligne.setLibelle("JSP_PF_PLANCALCUL_PRESTATION_MENSUELLE");
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.PRESTATION_MENSUELLE), false, true);
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_MESURE_ENCOURAGEMENT");
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.MESURE_ENCOURAGEMENT), false, false);
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_MESURE_CHARGES_LOYER");
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.MESURE_CHARGES_LOYER), false, false);
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_MESURE_COACHING");
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.MESURE_COACHING), false, false);
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setCssClass("total");
        ligne.setLibelle("JSP_PF_PLANCALCUL_PRESTATION_MENSUELLE_TOTALE");
        ligne.setValCol3(pcfAccordee.getSimplePCFAccordee().getMontant(), false, true);
        lignes.add(ligne);
    }

}
