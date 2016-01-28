package globaz.lynx.servlet.ordregroupe;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.lynx.db.ordregroupe.LXOrdreGroupe;
import globaz.lynx.db.ordregroupe.LXOrdreGroupeAnnulerViewBean;
import globaz.lynx.db.ordregroupe.LXOrdreGroupeExecutionViewBean;
import globaz.lynx.db.ordregroupe.LXOrdreGroupeImprimerViewBean;
import globaz.lynx.db.ordregroupe.LXOrdreGroupePreparationViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LXOrdreGroupeAction extends FWDefaultServletAction {

    private static final String JSP_ANNULER_DE = "Annuler_de.jsp";

    private static final String JSP_EXECUTION_DE = "Execution_de.jsp";
    private static final String JSP_IMPRIMER_DE = "Imprimer_de.jsp";
    private static final String JSP_PREPARER_DE = "Preparation_de.jsp";
    private static final String SELECTED_ID = "selectedId";

    public LXOrdreGroupeAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".afficher&_method=&selectedId=" + ((LXOrdreGroupe) viewBean).getIdOrdreGroupe();
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

            LXOrdreGroupeAnnulerViewBean viewBean = new LXOrdreGroupeAnnulerViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setIdOrdreGroupe(request.getParameter(SELECTED_ID));
            viewBean.setSession((BSession) mainDispatcher.getSession());

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURL(request, session) + JSP_ANNULER_DE;
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
        } else if ("preparation".equals(getAction().getActionPart())) {
            actionPreparation(session, request, response, mainDispatcher);
        } else if ("execution".equals(getAction().getActionPart())) {
            actionExecution(session, request, response, mainDispatcher);
        } else if ("annuler".equals(getAction().getActionPart())) {
            actionAnnuler(session, request, response, mainDispatcher);
        }
    }

    private void actionExecution(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = null;
        try {

            LXOrdreGroupeExecutionViewBean viewBean = new LXOrdreGroupeExecutionViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setIdOrdreGroupe(request.getParameter(SELECTED_ID));
            viewBean.setSession((BSession) mainDispatcher.getSession());

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURL(request, session) + JSP_EXECUTION_DE;
        } catch (Exception ex) {
            ex.printStackTrace();
            destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    private void actionImprimer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = null;
        try {

            LXOrdreGroupeImprimerViewBean viewBean = new LXOrdreGroupeImprimerViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setIdOrdreGroupe(request.getParameter(SELECTED_ID));
            viewBean.setSession((BSession) mainDispatcher.getSession());

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURL(request, session) + JSP_IMPRIMER_DE;
        } catch (Exception ex) {
            ex.printStackTrace();
            destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    private void actionPreparation(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = null;
        try {

            LXOrdreGroupePreparationViewBean viewBean = new LXOrdreGroupePreparationViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setIdOrdreGroupe(request.getParameter(SELECTED_ID));
            viewBean.setSession((BSession) mainDispatcher.getSession());

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURL(request, session) + JSP_PREPARER_DE;
        } catch (Exception ex) {
            ex.printStackTrace();
            destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

}
