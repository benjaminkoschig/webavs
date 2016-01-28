/*
 * Créé le 17 nov. 06
 */
package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
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
 * @author bsc
 */
public class IJValiderDecisionAction extends PRDefaultAction {

    /** */
    protected static final String USER_ACTION = "userAction";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJGenererDecomptesAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public IJValiderDecisionAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Redéfinie pour permettre d'afficher une erreur à l'affichage de la page si le lot n'est pas compensé
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
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = getRelativeURL(request, session) + "_de.jsp?";

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, mainDispatcher.getPrefix());
            viewBean.setISession(mainDispatcher.getSession());

            System.out.println("idDecision = " + request.getParameter("idDecision"));

            // on lui donne les parametres en requete au cas ou.
            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = mainDispatcher.dispatch(viewBean, action);
            session.setAttribute(FWServlet.VIEWBEAN, viewBean);

            // On mémorise dans la session le Num. AVS
            /*
             * IJNSSDTO dto = new IJNSSDTO(); dto.setNSS(
             * ((IJGenererDecisionViewBean)viewBean).getNoAVSTiersPrincipal());
             * PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
             */

        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        destination = destination + METHOD_ADD;

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }
}
