package globaz.osiris.servlet.action.message;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.framework.util.FWLog;
import globaz.framework.util.FWMessageManager;
import globaz.globall.http.JSPUtils;
import globaz.osiris.db.comptes.CAJournalViewBean;
import globaz.osiris.db.ordres.CAOrdreGroupeViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour l'affichage des messages dans les journaux.
 * 
 * @author DDA
 */
public class CAApercuMessageAction extends CADefaultServletAction {

    public CAApercuMessageAction(FWServlet servlet) {
        super(servlet);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String myDestination = getRelativeURL(request, session) + "_rc.jsp";

        try {
            FWAction action = FWAction.newInstance("osiris.comptes.apercuJournal.afficher");

            FWViewBeanInterface element = new CAJournalViewBean();
            ((CAJournalViewBean) element).setIdJournal(request.getParameter("id"));

            element = beforeAfficher(session, request, response, element);
            element = mainDispatcher.dispatch(element, action);

            if (!((CAJournalViewBean) element).isNew()) {
                FWLog log = new FWLog();
                log.setIdLog(((CAJournalViewBean) element).getIdLog());

                action = FWAction.newInstance(request.getParameter("userAction"));
                action.changeActionPart(FWAction.ACTION_AFFICHER);

                element = beforeAfficher(session, request, response, log);
                element = mainDispatcher.dispatch(log, action);

                setSessionAttribute(session, VB_ELEMENT, log);

                myDestination = getRelativeURL(request, session) + "_rc.jsp";
            } else {
                myDestination = ERROR_PAGE;
            }
        } catch (Exception e) {
            myDestination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);

    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_rc.jsp";

        try {
            FWAction action = FWAction.newInstance("osiris.ordres.ordresGroupes.afficher");

            FWViewBeanInterface element = new CAOrdreGroupeViewBean();
            ((CAOrdreGroupeViewBean) element).setIdOrdreGroupe(request.getParameter("id"));

            element = beforeAfficher(session, request, response, element);
            element = dispatcher.dispatch(element, action);

            if (!((CAOrdreGroupeViewBean) element).isNew()) {
                FWLog log = new FWLog();
                log.setIdLog(((CAOrdreGroupeViewBean) element).getIdLog());

                action = FWAction.newInstance(request.getParameter("userAction"));
                action.changeActionPart(FWAction.ACTION_AFFICHER);

                element = beforeAfficher(session, request, response, log);
                element = dispatcher.dispatch(log, action);

                setSessionAttribute(session, VB_ELEMENT, log);

                _myDestination = getRelativeURL(request, session) + "_rc.jsp";
            } else {
                _myDestination = ERROR_PAGE;
            }
        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionLister(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";

        try {
            FWViewBeanInterface element = getLog(request);

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            action.changeActionPart(FWAction.ACTION_AFFICHER);

            element = beforeAfficher(session, request, response, element);
            element = mainDispatcher.dispatch(element, action);

            FWViewBeanInterface manager = (FWMessageManager) globaz.globall.http.JSPUtils.useBean(request, "manager",
                    "globaz.framework.util.FWMessageManager", "request");
            JSPUtils.setBeanProperties(request, manager);
            ((FWMessageManager) manager).setForIdLog(((FWLog) element).getIdLog());

            manager = beforeAfficher(session, request, response, manager);
            manager = mainDispatcher.dispatch(manager, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, VBL_ELEMENT, manager);

            _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);
    }

    private FWLog getLog(HttpServletRequest request) throws ServletException {
        FWLog element = (FWLog) JSPUtils.useBean(request, "element", "globaz.framework.util.FWLog", "session", true);
        element.setIdLog(super.getId(request, "forIdLog"));

        return element;
    }

}