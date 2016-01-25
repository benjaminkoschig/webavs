package globaz.corvus.servlet;

import globaz.corvus.api.lots.IRELot;
import globaz.corvus.vb.process.REValiderLotViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author SCR
 * 
 */
public class REValiderLotAction extends REDefaultProcessAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public REValiderLotAction(FWServlet servlet) {
        super(servlet);
    }

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

        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, mainDispatcher.getPrefix());
            viewBean.setISession(mainDispatcher.getSession());

            Enumeration enu = request.getParameterNames();
            while (enu.hasMoreElements()) {
                String key = (String) enu.nextElement();
                System.out.println(key + " = " + request.getParameter(key));
            }
            // on lui donne les parametres en requete.
            JSPUtils.setBeanProperties(request, viewBean);
            session.setAttribute(FWServlet.VIEWBEAN, viewBean);

            if (!IRELot.CS_TYP_LOT_DECISION.equals(((REValiderLotViewBean) viewBean).getCsTypeLot())) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(((BSession) mainDispatcher.getSession())
                        .getLabel("ERREUR_LOT_TYPE_DECISION_VALIDE"));
            }

        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }
}
