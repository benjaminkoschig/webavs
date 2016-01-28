/*
 * Créé le 20 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.ij.vb.prestations.IJCotisationViewBean;
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
public class IJCotisationAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String VERS_ECRAN_DE = "_de.jsp?";
    private static final String VERS_ECRAN_RC = "_rc.jsp";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APAssuranceAction.
     * 
     * @param servlet
     */
    public IJCotisationAction(FWServlet servlet) {
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
        saveNewViewBean(session, viewBean);

        // return getRelativeURL(request, session) + VERS_ECRAN_DE + VALID_NEW;
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        saveNewViewBean(session, viewBean);

        // return getRelativeURL(request, session) + VERS_ECRAN_DE + VALID_NEW;
        return super._getDestModifierSucces(session, request, response, viewBean);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        saveNewViewBean(session, viewBean);

        // return getRelativeURL(request, session) + VERS_ECRAN_DE + VALID_NEW +
        // "&" + METHOD_ADD;
        return super._getDestSupprimerSucces(session, request, response, viewBean);
    }

    /**
     * Dispatch dans le helper pour charger les infos qui doivent s'afficher dans la page rc.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        IJCotisationViewBean viewBean = new IJCotisationViewBean();

        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            viewBean.setMessage(((BSession) mainDispatcher.getSession()).getLabel("DONNEE_REQUETE_ERR"));
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        mainDispatcher.dispatch(viewBean, getAction());
        saveViewBean(viewBean, request);

        forward(getRelativeURL(request, session) + VERS_ECRAN_RC, request, response);
    }

    private void saveNewViewBean(HttpSession session, FWViewBeanInterface viewBean) {

        saveViewBean(viewBean, session);

    }
}
