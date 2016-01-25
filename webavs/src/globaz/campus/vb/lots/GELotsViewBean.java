package globaz.campus.vb.lots;

import globaz.campus.db.annonces.GEAnnonces;
import globaz.campus.db.annonces.GEAnnoncesManager;
import globaz.campus.db.lots.GELots;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;

public class GELotsViewBean extends GELots implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de GELotsViewBean.
     */
    public GELotsViewBean() {
        super();
    }

    public String getEcoleDescription(String idTiersEcole) {
        String description = "";
        try {
            TIAdministrationViewBean tiersAdmin = new TIAdministrationViewBean();
            tiersAdmin.setSession(getSession());
            tiersAdmin.setIdTiersAdministration(idTiersEcole);
            tiersAdmin.retrieve();
            if (tiersAdmin != null && !tiersAdmin.isNew()) {
                description = tiersAdmin.getCodeAdministration() + "\n" + tiersAdmin.getDesignation1() + " "
                        + tiersAdmin.getDesignation2() + "\n" + tiersAdmin.getDesignation3() + " "
                        + tiersAdmin.getDesignation4();
            }
        } catch (Exception e) {
            return "";
        }
        return description;
    }

    public String getNbAnnonces() throws Exception {
        int nombreAnnonces = 0;
        GEAnnoncesManager annonceMng = new GEAnnoncesManager();
        annonceMng.setSession(getSession());
        annonceMng.setForIdLot(getIdLot());
        annonceMng.setForAnnoncesUniquement(new Boolean(true));
        nombreAnnonces = annonceMng.getCount();
        return nombreAnnonces + "";
    }

    public String getNbATraiter() throws Exception {
        int nombreAnnonces = 0;
        GEAnnoncesManager annonceMng = new GEAnnoncesManager();
        annonceMng.setSession(getSession());
        annonceMng.setForIdLot(getIdLot());
        annonceMng.setForCsEtatAnnonce(GEAnnonces.CS_ETAT_A_TRAITER);
        nombreAnnonces = annonceMng.getCount();
        return nombreAnnonces + "";
    }

    public String getNbErreurs() throws Exception {
        int nombreAnnonces = 0;
        GEAnnoncesManager annonceMng = new GEAnnoncesManager();
        annonceMng.setSession(getSession());
        annonceMng.setForIdLot(getIdLot());
        annonceMng.setForCsEtatAnnonce(GEAnnonces.CS_ETAT_ERREUR);
        nombreAnnonces = annonceMng.getCount();
        return nombreAnnonces + "";
    }

    public String getNbImputations() throws Exception {
        int nombreImputations = 0;
        GEAnnoncesManager imputationMng = new GEAnnoncesManager();
        imputationMng.setSession(getSession());
        imputationMng.setForIdLot(getIdLot());
        imputationMng.setForImputationsUniquement(new Boolean(true));
        nombreImputations = imputationMng.getCount();
        return nombreImputations + "";
    }
}
