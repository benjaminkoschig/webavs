package globaz.osiris.servlet.action.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CAJournalManagerListViewBean;
import globaz.osiris.db.comptes.CAJournalViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour les actions du journal.
 * 
 * @author DDA
 */
public class CAJournalAction extends CADefaultServletAction {

    /**
     * Constructor for CAJournalAction.
     * 
     * @param servlet
     */
    public CAJournalAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));
        String actionPath = getActionFullURL();
        if ("apercuJournal".equals(action.getPackagePart())) {
            actionPath += ".chercher";
        } else {
            actionPath += ".afficher";
        }
        return actionPath + "&selectedId=" + ((CAJournalViewBean) viewBean).getIdJournal();
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".afficher";
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String myDestination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWViewBeanInterface element = new CAJournalViewBean();
            if ((!JadeStringUtil.isBlank(getId(request, "idJournal")))
                    && (!JadeStringUtil.isNull(getId(request, "idJournal")))) {
                ((CAJournalViewBean) element).setIdJournal(getId(request, "idJournal"));
                ((CAJournalViewBean) element).setSession((BSession) mainDispatcher.getSession());

                element = beforeAfficher(session, request, response, element);
                element = mainDispatcher.dispatch(element, FWAction.newInstance(request.getParameter("userAction")));
            }

            setSessionAttribute(session, VB_ELEMENT, element);

            myDestination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String myDestination = getRelativeURL(request, session) + "_rc.jsp";

        try {
            CAJournalManagerListViewBean manager = getJournalManager(request);
            manager.setSession((BSession) mainDispatcher.getSession());

            setSessionAttribute(session, VBL_ELEMENT, manager);

            myDestination = getRelativeURL(request, session) + "_rc.jsp";

        } catch (Exception e) {
            myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionLister(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String myDestination = getRelativeURL(request, session) + "_rcListe.jsp";

        try {
            CAJournalManagerListViewBean manager = getJournalManager(request);
            manager.setISession(mainDispatcher.getSession());

            manager = (CAJournalManagerListViewBean) beforeLister(session, request, response, manager);
            manager = (CAJournalManagerListViewBean) mainDispatcher.dispatch(manager,
                    FWAction.newInstance(request.getParameter("userAction")));
            setSessionAttribute(session, VBL_ELEMENT, manager);

            myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
    }

    protected CAJournalManagerListViewBean getJournalManager(HttpServletRequest request) throws Exception {
        CAJournalManagerListViewBean manager = new CAJournalManagerListViewBean();

        if (!JadeStringUtil.isNull(super.getId(request, "id"))) {
            manager.setForIdJournal(super.getId(request, "id"));
        }

        JSPUtils.setBeanProperties(request, manager);

        return manager;
    }
}
