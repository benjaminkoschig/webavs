/*
 * Créé le 6 août 07
 */

package globaz.corvus.servlet;

import globaz.corvus.vb.interetsmoratoires.RECalculInteretMoratoireViewBean;
import globaz.corvus.vb.interetsmoratoires.REPreparationInteretMoratoireViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author BSC
 * 
 */

public class RECalculInteretMoratoireAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String VERS_ECRAN_RC = "_rc.jsp";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public RECalculInteretMoratoireAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // ----------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        RECalculInteretMoratoireViewBean viewBean = new RECalculInteretMoratoireViewBean();
        viewBean.setSession((BSession) mainDispatcher.getSession());
        viewBean.setIdDemandeRente(request.getParameter("idDemandeRente"));
        viewBean.setIdTiersDemandeRente(request.getParameter("idTiersDemandeRente"));
        viewBean.setDateDepotDemande(request.getParameter("dateDepotDemande"));
        viewBean.setDateDebutDroit(request.getParameter("dateDebutDroit"));
        viewBean.setDateDecision(request.getParameter("dateDecision"));

        request.setAttribute("viewBean", viewBean);

        super.actionChercher(session, request, response, mainDispatcher);
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @throws Exception
     */
    public String calculerInteretMoratoire(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        String destination = null;

        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            if (goesToSuccessDest) {
                destination = getRelativeURLwithoutClassPart(request, session) + "/calculInteretMoratoire"
                        + VERS_ECRAN_RC;
            } else {
                destination = _getDestAjouterEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        return destination;
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @throws Exception
     */
    public String genererDecisionSansInteretMoratoire(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        String destination = null;

        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            REPreparationInteretMoratoireViewBean pimViewVean = (REPreparationInteretMoratoireViewBean) viewBean;
            pimViewVean.cleanInteretsMoratoires();

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            if (goesToSuccessDest) {

                FWAction action = FWAction.newInstance(IREActions.ACTION_PREPARER_DECISION + ".genererDecision");
                destination = getUserActionURL(request, action.toString());

            } else {
                destination = ERROR_PAGE;
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        return destination;

    }

}
