package globaz.lynx.servlet.journal;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.lynx.db.journal.LXJournalAnnulerViewBean;
import globaz.lynx.db.journal.LXJournalComptabiliserViewBean;
import globaz.lynx.db.journal.LXJournalImprimerViewBean;
import globaz.lynx.db.journal.LXJournalViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LXJournalAction extends FWDefaultServletAction {

    private static final String JSP_ANNULER_DE = "Annuler_de.jsp";

    private static final String JSP_COMPTABILISER_DE = "Comptabiliser_de.jsp";
    private static final String JSP_IMPRIMER_DE = "Imprimer_de.jsp";
    private static final String SELECTED_ID = "selectedId";

    public LXJournalAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".afficher&_method=&selectedId=" + ((LXJournalViewBean) viewBean).getIdJournal();
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    private void actionAnnuler(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = null;
        try {

            LXJournalAnnulerViewBean viewBean = new LXJournalAnnulerViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setIdJournal(request.getParameter(SELECTED_ID));
            viewBean.setSession((BSession) mainDispatcher.getSession());

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURL(request, session) + JSP_ANNULER_DE;
        } catch (Exception ex) {
            ex.printStackTrace();
            destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    private void actionComptabiliser(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = null;
        try {

            LXJournalComptabiliserViewBean viewBean = new LXJournalComptabiliserViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setIdJournal(request.getParameter(SELECTED_ID));
            viewBean.setSession((BSession) mainDispatcher.getSession());

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURL(request, session) + JSP_COMPTABILISER_DE;
        } catch (Exception ex) {
            ex.printStackTrace();
            destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    @Override
    protected void actionCustom(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {

        if ("imprimer".equals(getAction().getActionPart())) {
            actionImprimer(session, request, response, mainDispatcher);
        } else if ("comptabiliser".equals(getAction().getActionPart())) {
            actionComptabiliser(session, request, response, mainDispatcher);
        } else if ("annuler".equals(getAction().getActionPart())) {
            actionAnnuler(session, request, response, mainDispatcher);
        }
    }

    private void actionImprimer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = null;
        try {

            LXJournalImprimerViewBean viewBean = new LXJournalImprimerViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setIdJournal(request.getParameter(SELECTED_ID));
            viewBean.setSession((BSession) mainDispatcher.getSession());

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURL(request, session) + JSP_IMPRIMER_DE;
        } catch (Exception ex) {
            ex.printStackTrace();
            destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

}
