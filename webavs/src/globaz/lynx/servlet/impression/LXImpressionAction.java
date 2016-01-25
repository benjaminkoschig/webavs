package globaz.lynx.servlet.impression;

import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.lynx.db.impression.LXImpressionBalanceAgeeViewBean;
import globaz.lynx.db.impression.LXImpressionBalanceViewBean;
import globaz.lynx.db.impression.LXImpressionGrandLivreViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LXImpressionAction extends FWDefaultServletAction {

    private static final String JSP_IMPRIMER_BALANCE_AGEE_DE = "BalanceAgee_de.jsp";
    private static final String JSP_IMPRIMER_BALANCE_DE = "Balance_de.jsp";
    private static final String JSP_IMPRIMER_GRANDLIVRE_DE = "GrandLivre_de.jsp";

    /**
     * Constructeur
     */
    public LXImpressionAction(FWServlet servlet) {
        super(servlet);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {

        if ("imprimerBalance".equals(getAction().getActionPart())) {
            actionImprimerBalance(session, request, response, mainDispatcher);
        } else if ("imprimerBalanceAgee".equals(getAction().getActionPart())) {
            actionImprimerBalanceAgee(session, request, response, mainDispatcher);
        } else if ("imprimerGrandLivre".equals(getAction().getActionPart())) {
            actionImprimerGrandLivre(session, request, response, mainDispatcher);
        }
    }

    /**
     * Action permettant l'affichage de l'écran d'impression d'une balance
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    private void actionImprimerBalance(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = null;
        try {

            LXImpressionBalanceViewBean viewBean = new LXImpressionBalanceViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setSession((BSession) mainDispatcher.getSession());

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURL(request, session) + JSP_IMPRIMER_BALANCE_DE;
        } catch (Exception ex) {
            ex.printStackTrace();
            destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    /**
     * Action permettant l'affichage de l'écran d'impression d'une balance agée
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    private void actionImprimerBalanceAgee(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = null;
        try {

            LXImpressionBalanceAgeeViewBean viewBean = new LXImpressionBalanceAgeeViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setSession((BSession) mainDispatcher.getSession());

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURL(request, session) + JSP_IMPRIMER_BALANCE_AGEE_DE;
        } catch (Exception ex) {
            ex.printStackTrace();
            destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    /**
     * Action permettant l'affichage de l'écran d'impression du grand livre
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    private void actionImprimerGrandLivre(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = null;
        try {

            LXImpressionGrandLivreViewBean viewBean = new LXImpressionGrandLivreViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setSession((BSession) mainDispatcher.getSession());

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURL(request, session) + JSP_IMPRIMER_GRANDLIVRE_DE;
        } catch (Exception ex) {
            ex.printStackTrace();
            destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }
}
