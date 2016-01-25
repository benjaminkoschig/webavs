package globaz.osiris.servlet.action.services;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.services.CARechercheMontantSectionManagerListViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la recherche des montants dans les sections.
 * 
 * @author DDA
 */
public class CARechercheMontantSectionAction extends CADefaultServletAction {
    public static final String AFFICHER_SPECIAL = "afficherSpecial";
    public static final String JOURNAL_OPERATION_CUSTOM = "journalOperationCustom";

    public static final String RECHERCHE_MONTANT_SECTION = "rechercheMontantSection";

    public CARechercheMontantSectionAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String myDestination = getRelativeURL(request, session) + "_rc.jsp";
        try {
            CARechercheMontantSectionManagerListViewBean manager = new CARechercheMontantSectionManagerListViewBean();
            manager.setSession((BSession) mainDispatcher.getSession());

            setSessionAttribute(session, VBL_ELEMENT, manager);

        } catch (Exception e) {
            myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        String actionSuite = getActionSuite(request);
        try {
            FWViewBeanInterface manager = null;

            if ((manager = getManager(request, actionSuite, true)) == null) {
                throw new JAException("Action non valid : " + actionSuite);
            }

            manager.setISession(mainDispatcher.getSession());

            JSPUtils.setBeanProperties(request, manager);

            manager = beforeLister(session, request, response, manager);
            manager = mainDispatcher.dispatch(manager, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, VBL_ELEMENT, manager);

            myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            myDestination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
    }

    private CARechercheMontantSectionManagerListViewBean getManager(HttpServletRequest request, String actionSuite,
            boolean isNew) throws ServletException {
        CARechercheMontantSectionManagerListViewBean manager = new CARechercheMontantSectionManagerListViewBean();

        if (!JadeStringUtil.isNull(request.getParameter("forMontantABS"))) {
            manager.setForSoldeABS(request.getParameter("forMontantABS"));
        }

        if (!JadeStringUtil.isNull(request.getParameter("forIdExterneRole"))) {
            manager.setForIdExterneRole(request.getParameter("forIdExterneRole"));
        }

        return manager;
    }

    protected CARechercheMontantSectionManagerListViewBean getRechercheMontantManagerListViewBean(
            HttpServletRequest request) throws ServletException, InvocationTargetException, IllegalAccessException {
        CARechercheMontantSectionManagerListViewBean manager = (CARechercheMontantSectionManagerListViewBean) JSPUtils
                .useBean(request, "manager", "globaz.osiris.services.CARechercheMontantManagerListViewBean", "request");
        JSPUtils.setBeanProperties(request, manager);

        return manager;
    }

}