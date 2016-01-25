/*
 * Créé le 2 août 07
 */

package globaz.corvus.servlet;

import globaz.corvus.vb.interetsmoratoires.REPreparationInteretMoratoireViewBean;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
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
public class REPreparationInteretsMoratoiresAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String VERS_ECRAN_DE_ADD = "_de.jsp?" + PRDefaultAction.METHOD_ADD;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public REPreparationInteretsMoratoiresAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // ----------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        REPreparationInteretMoratoireViewBean viewBean = new REPreparationInteretMoratoireViewBean();
        viewBean.setISession(mainDispatcher.getSession());

        // recuperation des parametres
        String idDemandeRente = request.getParameter("noDemandeRente");
        String idTiersRequerant = request.getParameter("idTierRequerant");
        String dateDecision = request.getParameter("dateDecision");
        String decisionDepuis = request.getParameter("decisionDepuis");
        String testRetenue = request.getParameter("testRetenue");

        viewBean.setIdDemandeRente(idDemandeRente);
        viewBean.setIdTierRequerant(idTiersRequerant);
        if (!JadeStringUtil.isNull(dateDecision)) {
            viewBean.setDateDecision(dateDecision);
        }

        if (!JadeStringUtil.isNull(decisionDepuis)) {
            viewBean.setDecisionDepuis(decisionDepuis);
        }

        if (!JadeStringUtil.isNull(testRetenue)) {
            viewBean.setTestRetenue(testRetenue);
        }

        mainDispatcher.dispatch(viewBean, getAction());
        this.saveViewBean(viewBean, session);

        String destination = getRelativeURL(request, session) + REPreparationInteretsMoratoiresAction.VERS_ECRAN_DE_ADD;
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

}
