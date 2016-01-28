package globaz.apg.servlet;

import globaz.apg.vb.process.APGenererDecisionAMATViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
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
public class APGenererDecisionAMATAction extends APDefaultProcessAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APGenererDecisionAMATAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public APGenererDecisionAMATAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
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
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        /*
         * si on aexecute le process, c'est que des prestations ont ete calculees pour le droit, hors le boolean qui
         * indique cela vient d'etre ecrase par JSPUtils. Donc on remet sa valeur
         */
        ((APGenererDecisionAMATViewBean) viewBean).setCalcule(true);

        return super._getDestExecuterSucces(session, request, response, viewBean);
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
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());

            // on lui donne les parametres en requete au cas ou.
            JSPUtils.setBeanProperties(request, viewBean);
            ((APGenererDecisionAMATViewBean) viewBean).setIdDroit(request.getParameter("selectedId"));

            // Par défaut, on génère une décision et non une communication
            if (viewBean instanceof APGenererDecisionAMATViewBean) {
                ((APGenererDecisionAMATViewBean) viewBean).setDecision(true);
            }
            saveViewBean(viewBean, session);
            mainDispatcher.dispatch(viewBean, getAction());
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());

        try {

            JSPUtils.setBeanProperties(request, viewBean);

            saveViewBean(viewBean, session);

        } catch (Exception e) {
            e.printStackTrace();
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        super.actionExecuter(session, request, response, mainDispatcher);
    }

}
