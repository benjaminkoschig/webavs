package globaz.prestation.controler.rentes;

import globaz.hera.db.famille.SFMembreFamille;
import globaz.prestation.controler.IPRDataController;

/**
 * 
 * @author SCR
 * 
 *         EtatCivil controler.
 * 
 *         Données : - EtatCivil des 2 conjoints avant modification. - Les conjoints sous forme de membre de famille,
 *         après modification.
 * 
 */
public class PREtatCivilDataController implements IPRDataController {

    private String csEtatCivilMF1AvantMAJ = "";
    private String csEtatCivilMF2AvantMAJ = "";

    private SFMembreFamille mf1 = null;
    private SFMembreFamille mf2 = null;

    public String getCsEtatCivilMF1AvantMAJ() {
        return csEtatCivilMF1AvantMAJ;
    }

    public String getCsEtatCivilMF2AvantMAJ() {
        return csEtatCivilMF2AvantMAJ;
    }

    public SFMembreFamille getMf1() {
        return mf1;
    }

    public SFMembreFamille getMf2() {
        return mf2;
    }

    public void setCsEtatCivilMF1AvantMAJ(String csEtatCivilMF1AvantMAJ) {
        this.csEtatCivilMF1AvantMAJ = csEtatCivilMF1AvantMAJ;
    }

    public void setCsEtatCivilMF2AvantMAJ(String csEtatCivilMF2AvantMAJ) {
        this.csEtatCivilMF2AvantMAJ = csEtatCivilMF2AvantMAJ;
    }

    public void setMf1(SFMembreFamille mf1) {
        this.mf1 = mf1;
    }

    public void setMf2(SFMembreFamille mf2) {
        this.mf2 = mf2;
    }

}
