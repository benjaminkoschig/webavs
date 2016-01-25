package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.helios.db.modeles.CGModeleEcritureViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la gestion des modèles d'écritures.
 * 
 * @author DDA
 * 
 */
public class CGActionModeleEcriture extends globaz.framework.controller.FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMouvementCompte.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public CGActionModeleEcriture(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String selectedId = request.getParameter("selectedId");
        ((CGModeleEcritureViewBean) viewBean).setIdModeleEcriture(selectedId);
        return viewBean;
    }

}
