/*
 * Créé le 15 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWController;
import globaz.globall.api.BISession;
import javax.servlet.http.HttpSession;

/**
 * @author mmu
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPReglePlausibiliteViewBean extends CPReglePlausibilite implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public void getReglePlausibilite(String idPlausibilite, HttpSession session) {
        FWController controller = (FWController) session.getAttribute("objController");
        BISession bSession = controller.getSession();
        setISession(bSession);
        setIdPlausibilite(idPlausibilite);
        try {
            retrieve();
        } catch (Exception e) {
        }
    }
}
