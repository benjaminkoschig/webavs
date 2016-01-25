package globaz.osiris.servlet.action.message;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.contentieux.CAEvenementContentieux;
import globaz.osiris.db.contentieux.CAEvenementContentieuxViewBean;
import globaz.osiris.db.utils.CAElementJournalisationManagerListViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import globaz.osiris.servlet.action.contentieux.CAOperationContentieuxAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour l'affichage de l'ancien contentieux.
 * 
 * @author DDA
 */
public class CAApercuJournalisationAction extends CADefaultServletAction {
    public CAApercuJournalisationAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_rc.jsp";

        try {
            CAEvenementContentieuxViewBean element = getEvenementContentieux(request);
            element.setSession((BSession) mainDispatcher.getSession());

            setSessionAttribute(session, VB_ELEMENT, element);

            _myDestination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);

    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";

        try {
            FWViewBeanInterface element = getEvenementContentieux(request);

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            action.changeActionPart(FWAction.ACTION_AFFICHER);

            element = beforeAfficher(session, request, response, element);
            element = mainDispatcher.dispatch(element, action);

            FWViewBeanInterface manager = getElementJournalisationManager((CAEvenementContentieuxViewBean) element,
                    request);

            manager = beforeLister(session, request, response, manager);
            manager = mainDispatcher.dispatch(manager, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, VBL_ELEMENT, manager);

            _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);
    }

    public CAElementJournalisationManagerListViewBean getElementJournalisationManager(
            CAEvenementContentieuxViewBean element, HttpServletRequest request) throws Exception {
        CAElementJournalisationManagerListViewBean manager = (CAElementJournalisationManagerListViewBean) JSPUtils
                .useBean(request, "manager", "globaz.osiris.db.utils.CAElementJournalisationManagerListViewBean",
                        "request");

        JSPUtils.setBeanProperties(request, manager);

        if (!JadeStringUtil.isDecimalEmpty(element.getIdPosteJournalisation())) {
            manager.setForIdPosteJournalisation(element.getIdPosteJournalisation());
        } else {
            manager.setForIdPosteJournalisation("0");
        }

        return manager;
    }

    public CAEvenementContentieuxViewBean getEvenementContentieux(HttpServletRequest request) throws ServletException {
        CAEvenementContentieuxViewBean element = (new CAOperationContentieuxAction((FWServlet) servlet))
                .getEvenementContentieux(request);
        element.setAlternateKey(CAEvenementContentieux.AK_IDSECPARAM);
        if ((!JadeStringUtil.isBlank(getId(request, "idParametreEtape")))
                && (!JadeStringUtil.isNull(request.getParameter("idParametreEtape")))) {
            element.setIdParametreEtape(request.getParameter("idParametreEtape"));
        }

        if ((!JadeStringUtil.isBlank(getId(request, "idSection")))
                && (!JadeStringUtil.isNull(request.getParameter("idSection")))) {
            element.setIdSection(request.getParameter("idSection"));
        }

        return element;
    }

}