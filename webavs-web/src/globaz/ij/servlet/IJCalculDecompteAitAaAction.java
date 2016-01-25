/*
 * Créé le 25 oct. 07
 */
package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.ij.vb.basesindemnisation.IJBaseIndemnisationAitAaViewBean;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class IJCalculDecompteAitAaAction extends PRDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJSaisiePrononceAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public IJCalculDecompteAitAaAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Calcul des prestations Aa
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public String calculerAa(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        IJBaseIndemnisationAitAaViewBean biViewBean = new IJBaseIndemnisationAitAaViewBean();
        biViewBean.setSession((BSession) mainDispatcher.getSession());
        biViewBean.setIdBaseIndemisation(request.getParameter("forNoBaseIndemnisation"));
        try {
            biViewBean.retrieve();
        } catch (Exception e) {
            return ERROR_PAGE;
        }

        mainDispatcher.dispatch(biViewBean, getAction());

        return getUserActionURL(request, IIJActions.ACTION_PRESTATION_JOINT_LOT_PRONONCE, FWAction.ACTION_CHERCHER);
    }

    /**
     * Calcul des prestations Ait
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public String calculerAit(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        IJBaseIndemnisationAitAaViewBean biViewBean = new IJBaseIndemnisationAitAaViewBean();
        biViewBean.setSession((BSession) mainDispatcher.getSession());
        biViewBean.setIdBaseIndemisation(request.getParameter("forNoBaseIndemnisation"));
        try {
            biViewBean.retrieve();
        } catch (Exception e) {
            return ERROR_PAGE;
        }

        mainDispatcher.dispatch(biViewBean, getAction());

        return getUserActionURL(request, IIJActions.ACTION_PRESTATION_JOINT_LOT_PRONONCE, FWAction.ACTION_CHERCHER);
    }

}
