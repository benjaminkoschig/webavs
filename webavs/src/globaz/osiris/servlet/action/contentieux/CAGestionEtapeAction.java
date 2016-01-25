package globaz.osiris.servlet.action.contentieux;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.osiris.db.contentieux.CAParametreEtapeManagerListViewBean;
import globaz.osiris.db.contentieux.CAParametreEtapeViewBean;
import globaz.osiris.db.contentieux.CASequenceContentieuxViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la gestion des étapes du contentieux.
 */
public class CAGestionEtapeAction extends CADefaultServletAction {

    /**
     * Constructor for CAGestionEtape.
     * 
     * @param servlet
     */
    public CAGestionEtapeAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_de.jsp";
        try {
            CAParametreEtapeViewBean element = (new CAOperationContentieuxAction((FWServlet) servlet)
                    .getParametreEtape(request));
            element.setSession((BSession) mainDispatcher.getSession());
            element.retrieve();
            element = (CAParametreEtapeViewBean) mainDispatcher.dispatch(element, super.getAction());
            setSessionAttribute(session, VB_ELEMENT, element);
            if (element.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _myDestination = getRelativeURL(request, session) + "_de.jsp?_valid=fail";
            } else {
                _myDestination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_rc.jsp";
        try {
            CASequenceContentieuxViewBean element = getSequenceContentieux(request);
            element.setSession((BSession) mainDispatcher.getSession());
            element.retrieve();

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
            CASequenceContentieuxViewBean element = getSequenceContentieux(request);
            CAParametreEtapeManagerListViewBean manager = (new CAOperationContentieuxAction((FWServlet) servlet))
                    .getParametreEtapeManager(request);
            manager.setForIdSequenceContentieux(element.getId());
            manager.setISession(mainDispatcher.getSession());
            mainDispatcher.dispatch(manager, getAction());
            JSPUtils.setBeanProperties(request, manager);
            setSessionAttribute(session, VBL_ELEMENT, manager);
            _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);

    }

    private CASequenceContentieuxViewBean getSequenceContentieux(HttpServletRequest request) throws ServletException {
        CASequenceContentieuxViewBean element = (new CASequenceContentieuxAction((FWServlet) servlet))
                .getSequenceContentieux(request);
        return element;
    }

}
