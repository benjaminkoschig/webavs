package globaz.campus.vb.etudiants;

import globaz.campus.db.etudiants.GEEtudiantsTiers;
import globaz.campus.db.etudiants.GEEtudiantsTiersManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWController;
import globaz.globall.db.BSession;
import java.util.Vector;
import javax.servlet.http.HttpSession;

public class GEEtudiantsViewBean extends GEEtudiantsTiers implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final Vector getIdsEtNomsEcole(HttpSession session) {
        Vector ecole = null;
        BSession bSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
        try {
            ecole = GEEtudiantsTiersManager.getIdsEtNomsEcole(bSession);
        } catch (Exception e) {
            ecole.add("Aucune école existante dans les tiers");
        }
        return ecole;
    }
}
