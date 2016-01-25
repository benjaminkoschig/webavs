package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.ij.vb.basesindemnisation.IJFormulaireIndemnisationViewBean;
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
public class IJFormulaireIndemnisationAction extends PRDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJFormulaireIndemnisationAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public IJFormulaireIndemnisationAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getUserActionURL(request, IIJActions.ACTION_FORMULAIRE_INDEMNISATION, FWAction.ACTION_CHERCHER
                + "&selectedId=" + ((IJFormulaireIndemnisationViewBean) viewBean).getIdIndemnisation());
    }

    /**
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getUserActionURL(request, IIJActions.ACTION_FORMULAIRE_INDEMNISATION, FWAction.ACTION_CHERCHER
                + "&selectedId=" + ((IJFormulaireIndemnisationViewBean) viewBean).getIdIndemnisation());
    }

    /**
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getUserActionURL(request, IIJActions.ACTION_FORMULAIRE_INDEMNISATION, FWAction.ACTION_CHERCHER
                + "&selectedId=" + ((IJFormulaireIndemnisationViewBean) viewBean).getIdIndemnisation());
    }

    /**
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
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        IJFormulaireIndemnisationViewBean viewBean = new IJFormulaireIndemnisationViewBean();

        viewBean.setIdIndemnisation(getSelectedId(request));
        saveViewBean(viewBean, request);
        mainDispatcher.dispatch(viewBean, getAction());
        super.actionChercher(session, request, response, mainDispatcher);
    }

    /**
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((IJFormulaireIndemnisationViewBean) viewBean).setIdIndemnisation(request.getParameter("idIndemnisation"));

        return super.beforeNouveau(session, request, response, viewBean);
    }
}
