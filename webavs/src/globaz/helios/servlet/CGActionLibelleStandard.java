package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.helios.db.comptes.CGLibelleStandardViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la gestion des libellés standards.
 * 
 * @author DDA
 * 
 */
public class CGActionLibelleStandard extends FWDefaultServletAction {

    public CGActionLibelleStandard(FWServlet servlet) {
        super(servlet);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWViewBeanInterface) La table des secteurs AVS est composée de deux clé primaires
     *      (idMandat + idSecteurAvs) --> mise à jours de l'id Mandat
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        super.beforeAfficher(session, request, response, viewBean);

        CGLibelleStandardViewBean libelle = (CGLibelleStandardViewBean) viewBean;
        libelle.setIdMandat(request.getParameter("idMandat"));
        libelle.setIdLibelleStandard(request.getParameter("selectedId"));

        return libelle;
    }
}
