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
public class PFGroupeDepensesReconnuesHandler {

    private ArrayList<PFLignePlanCalculHandler> lignes;
    private OutputCalcul outputCalcul;

    public PFGroupeDepensesReconnuesHandler(OutputCalcul outputCalcul) {
        this.outputCalcul = outputCalcul;
        lignes = new ArrayList<PFLignePlanCalculHandler>(0);

        genererLignes();
    }

    /**
     * Permet de générer les lignes
     */
    private void genererLignes() {
        printLoyers();
        printFraisImmeubles();
        printFraisObtentionRevenu();

        printDepensesReconnues();
    }

    /**
     * Retourne les lignes du bloc Fortune pour les données passées au constructeur du handler
     * 
     * @return the lignes
     */
    public ArrayList<PFLignePlanCalculHandler> getLignes() {
        return lignes;
    }

    private void printDepensesReconnues() {
        lignes.add(new PFLignePlanCalculHandler());

        PFLignePlanCalculHandler ligne = new PFLignePlanCalculHandler();
        ligne.setCssClass("total");
        ligne.setLibelle("JSP_PF_PLANCALCUL_TOTAL_DEPENSES");
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.DEPENSES_RECONNUES), false, true);
        lignes.add(ligne);
    }

    private void printFraisImmeubles() {
        PFLignePlanCalculHandler ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_INTERETS_HYPO_ET_FI");
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_FRAIS_ENTRETIENS");
        ligne.setValCol1(outputCalcul.getDonneeString(OutputData.DEPENSES_FRAIS_ENTRETIENS_IMMEUBLE));
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.DEPENSES_FRAIS_ENTRETIENS_IMMEUBLE_MODIF));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_INTERETS_HYPO");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.DEPENSES_INTERETS_HYPOTHECAIRES), true, false);
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_TOTAL_FRAIS_IMMEUBLE");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.DEPENSES_FRAIS_IMMEUBLE));
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.DEPENSES_FRAIS_IMMEUBLE_MODIF));
        lignes.add(ligne);

    }

    private void printFraisObtentionRevenu() {

        // Pour le requérant

        PFLignePlanCalculHandler ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_FRAIS_OBTENTION_REVENU_REQUERANT");
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_FRAIS_REPAS");
        ligne.setValCol1(outputCalcul.getDonneeString(OutputData.DEPENSES_FRAIS_REPAS_REQUERANT));
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.DEPENSES_FRAIS_REPAS_REQUERANT_MODIF));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_FRAIS_TRANSPORT");
        ligne.setValCol1(outputCalcul.getDonneeString(OutputData.DEPENSES_FRAIS_TRANSPORT_REQUERANT));
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.DEPENSES_FRAIS_TRANSPORT_REQUERANT_MODIF_TAXATEUR));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_FRAIS_VETEMENTS");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.DEPENSES_FRAIS_VETEMENTS_REQUERANT), true, false);
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.DEPENSES_FRAIS_OBTENTION_REVENU_REQUERANT));
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.DEPENSES_FRAIS_OBTENTION_REVENU_REQUERANT_MODIF));
        lignes.add(ligne);

        // Pour le conjoint

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_FRAIS_OBTENTION_REVENU_CONJOINT");
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_FRAIS_REPAS");
        ligne.setValCol1(outputCalcul.getDonneeString(OutputData.DEPENSES_FRAIS_REPAS_CONJOINT));
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.DEPENSES_FRAIS_REPAS_CONJOINT_MODIF));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_FRAIS_TRANSPORT");
        ligne.setValCol1(outputCalcul.getDonneeString(OutputData.DEPENSES_FRAIS_TRANSPORT_CONJOINT));
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.DEPENSES_FRAIS_TRANSPORT_CONJOINT_MODIF_TAXATEUR));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("&nbsp;-&nbsp;JSP_PF_PLANCALCUL_FRAIS_VETEMENTS");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.DEPENSES_FRAIS_VETEMENTS_CONJOINT), true, false);
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.DEPENSES_FRAIS_OBTENTION_REVENU_CONJOINT));
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.DEPENSES_FRAIS_OBTENTION_REVENU_CONJOINT_MODIF));
        lignes.add(ligne);

        // Pour les enfants

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_FRAIS_OBTENTION_REVENU_ENFANTS");
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.DEPENSES_FRAIS_OBTENTION_REVENU_ENFANTS));
        lignes.add(ligne);

    }

    private void printLoyers() {
        PFLignePlanCalculHandler ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_BESOINS_VITAUX");
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.DEPENSES_BESOINS_VITAUX));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_LOYER_ANNUEL");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.DEPENSES_LOYER_ANNUEL));
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.DEPENSES_LOYER_ANNUEL_MODIF));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_CHARGES_FORFAITAIRES");
        ligne.setValCol2(outputCalcul.getDonneeString(OutputData.DEPENSES_CHARGES_ANNUELLES));
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.DEPENSES_CHARGES_ANNUELLES_MODIF));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_COTISATIONS_AVS");
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.DEPENSES_COTISATION_NON_ACTIF));
        lignes.add(ligne);

        ligne = new PFLignePlanCalculHandler();
        ligne.setLibelle("JSP_PF_PLANCALCUL_PENSION_AL_PAYEE");
        ligne.setValCol3(outputCalcul.getDonneeString(OutputData.DEPENSES_PENSION_ALIMENTAIRE_VERSEE));
        lignes.add(ligne);

    }

}
