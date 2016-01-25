package globaz.campus.vb.etudiants;

import globaz.campus.db.etudiants.GEEtudiants;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWController;
import globaz.globall.db.BSession;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TITiersViewBean;
import javax.servlet.http.HttpSession;

public class GEEtudiantsAjoutViewBean extends GEEtudiants implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static String getEcoleDescription(String idTiersEcole, HttpSession session) {
        String description = "";
        try {
            BSession bSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
            TIAdministrationViewBean tiersAdmin = new TIAdministrationViewBean();
            tiersAdmin.setSession(bSession);
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

    public static String getEtudiantDescription(String idTiersEtudiants, HttpSession session) {
        String description = "";
        try {
            BSession bSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
            TITiersViewBean tiers = new TITiersViewBean();
            tiers.setSession(bSession);
            tiers.setIdTiers(idTiersEtudiants);
            tiers.retrieve();
            if (tiers != null && !tiers.isNew()) {
                description = tiers.getNumAvsActuel() + "\n" + tiers.getDesignation1() + " " + tiers.getDesignation2()
                        + "\n" + tiers.getDesignation3() + " " + tiers.getDesignation4();
            }
        } catch (Exception e) {
            return "";
        }
        return description;
    }

}
