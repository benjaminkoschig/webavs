package globaz.osiris.servlet.action.interets;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.osiris.db.interets.CADetailInteretMoratoireListViewBean;
import globaz.osiris.db.interets.CADetailInteretMoratoireViewBean;
import globaz.osiris.db.interets.CAInteretMoratoireViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour l'affichage du détail des intérêts.
 * 
 * @author DDA
 */
public class CADetailInteretMoratoireAction extends CADefaultServletAction {

    public CADetailInteretMoratoireAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_rc.jsp";

        try {
            FWViewBeanInterface element = (CAInteretMoratoireViewBean) JSPUtils.useBean(request, "element",
                    "globaz.osiris.db.interets.CAInteretMoratoireViewBean", "session", true);
            ((CAInteretMoratoireViewBean) element).setIdInteretMoratoire(super.getId(request, "id"));
            ((CAInteretMoratoireViewBean) element).setDomaine(request.getParameter("domaine"));

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            action.changeActionPart(FWAction.ACTION_AFFICHER);

            element = beforeAfficher(session, request, response, element);
            element = mainDispatcher.dispatch(element, action);

            setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, element);

            _myDestination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e) {
            _myDestination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";

        try {
            FWViewBeanInterface manager = getDetailInteretMoratoireManager(request);

            manager = mainDispatcher.dispatch(manager, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, CADefaultServletAction.VBL_ELEMENT, manager);

            _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _myDestination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);
    }

    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((CADetailInteretMoratoireViewBean) viewBean).setManualModification(true);
        return viewBean;
    }

    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((CADetailInteretMoratoireViewBean) viewBean).setManualModification(true);
        return viewBean;
    }

    @Override
    protected FWViewBeanInterface beforeSupprimer(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((CADetailInteretMoratoireViewBean) viewBean).setManualModification(true);
        return viewBean;
    }

    protected CADetailInteretMoratoireListViewBean getDetailInteretMoratoireManager(HttpServletRequest request)
            throws ServletException {
        CADetailInteretMoratoireListViewBean manager = (CADetailInteretMoratoireListViewBean) JSPUtils.useBean(request,
                "manager", "globaz.osiris.db.interets.CADetailInteretMoratoireListViewBean", "request");

        try {
            JSPUtils.setBeanProperties(request, manager);
        } catch (Exception e) {
            throw new ServletException(e);
        }

        return manager;
    }

}
