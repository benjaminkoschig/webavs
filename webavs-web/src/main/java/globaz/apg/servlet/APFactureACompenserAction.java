/*
 * Créé le 29 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.servlet;

import globaz.apg.vb.lots.APFactureACompenserViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APFactureACompenserAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String VERS_ECRAN_DE = "_de.jsp";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APFactureACompenserAction.
     * 
     * @param servlet
     */
    public APFactureACompenserAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // return getRelativeURL(request, session) + VERS_ECRAN_DE + "?" +
        // METHOD_ADD + "&" + VALID_NEW;

        if ((getAction() != null) && (getAction().isWellFormed())) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + IAPActions.ACTION_COMPENSATIONS_LOT + "."
                    + FWAction.ACTION_CHERCHER;
        }
        return "";

    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // return getRelativeURL(request, session) + VERS_ECRAN_DE + "?" +
        // VALID_NEW;

        if ((getAction() != null) && (getAction().isWellFormed())) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + IAPActions.ACTION_COMPENSATIONS_LOT + "."
                    + FWAction.ACTION_CHERCHER;
        }
        return "";

    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface fwViewBean) {
        APFactureACompenserViewBean viewBean = new APFactureACompenserViewBean();

        saveViewBean(viewBean, session);

        // return getRelativeURL(request, session) + VERS_ECRAN_DE + "?" +
        // METHOD_ADD + "&" + VALID_NEW;

        if ((getAction() != null) && (getAction().isWellFormed())) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + IAPActions.ACTION_COMPENSATIONS_LOT + "."
                    + FWAction.ACTION_CHERCHER;
        }
        return "";

    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = ERROR_PAGE;

        APFactureACompenserViewBean viewBean = new APFactureACompenserViewBean();

        if (METHOD_ADD_VALUE.equalsIgnoreCase(getMethod(request))) {
            viewBean.setIdCompensationParente(getSelectedId(request));

            if (actionSetSession(viewBean, session, request, mainDispatcher)) {
                destination = getRelativeURL(request, session) + VERS_ECRAN_DE;
                viewBean.setIdTiers(request.getParameter("idTiers"));
            }
        } else {
            viewBean.setIdFactureACompenser(getSelectedId(request));
            viewBean = (APFactureACompenserViewBean) mainDispatcher.dispatch(viewBean, getAction());
            destination = getRelativeURL(request, session) + VERS_ECRAN_DE + "?" + DESACTIVE_VALIDATION;
        }

        viewBean.setIdLot(request.getParameter("idLot"));

        saveViewBean(viewBean, session);

        forward(destination, request, response);
    }

    /**
     * DOCUMENT ME!
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
    public String actionAfficherEcranDE(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        String retValue = getRelativeURL(request, session) + VERS_ECRAN_DE + "?";
        viewBean = new APFactureACompenserViewBean();
        viewBean.setISession(mainDispatcher.getSession());

        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

        retValue += METHOD_ADD;
        saveViewBean(viewBean, session);

        return retValue;
    }
}
