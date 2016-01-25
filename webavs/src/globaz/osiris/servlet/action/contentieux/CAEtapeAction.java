package globaz.osiris.servlet.action.contentieux;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.contentieux.CAEtapeManagerListViewBean;
import globaz.osiris.db.contentieux.CAEtapeViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour les étapes du contentieux.
 */
public class CAEtapeAction extends CADefaultServletAction {
    /**
     * Constructor for CAEtape.
     * 
     * @param servlet
     */
    public CAEtapeAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_de.jsp";
        try {
            CAEtapeViewBean element;
            if (!JadeStringUtil.isBlank(request.getParameter("_method"))
                    && (request.getParameter("_method").equals("add"))) {
                element = new CAEtapeViewBean();
            } else {
                element = getEtape(request);
            }

            element.setSession((BSession) mainDispatcher.getSession());
            element.retrieve();
            if (element.isNew()) {
                element.setSession((BSession) mainDispatcher.getSession());
                JSPUtils.setBeanProperties(request, element);
                element.retrieve();
            }

            element = (CAEtapeViewBean) mainDispatcher.dispatch(element, super.getAction());
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
            CAEtapeViewBean element = getEtape(request);
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
            CAEtapeViewBean element = getEtape(request);
            CAEtapeManagerListViewBean manager = getEtapeManager(request);
            if (!JadeStringUtil.isNull(super.getId(request, "forIdEtape"))) {
                manager.setFromIdEtape(super.getId(request, "forIdEtape"));
            } else {
                manager.setFromIdEtape(element.getIdEtape());
            }
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

    private CAEtapeViewBean getEtape(HttpServletRequest request) throws ServletException {
        CAEtapeViewBean element = (CAEtapeViewBean) JSPUtils.useBean(request, "element",
                "globaz.osiris.db.contentieux.CAEtapeViewBean", "session");
        if ((!JadeStringUtil.isBlank(getId(request, "forIdEtape")))
                && (!JadeStringUtil.isNull(super.getId(request, "forIdEtape")))) {
            element.setIdEtape(super.getId(request, "forIdEtape"));
        } else {
            element = (CAEtapeViewBean) JSPUtils.useBean(request, "element",
                    "globaz.osiris.db.contentieux.CAEtapeViewBean", "session", true);
        }
        return element;
    }

    private CAEtapeManagerListViewBean getEtapeManager(HttpServletRequest request) throws ServletException {
        CAEtapeManagerListViewBean manager = (CAEtapeManagerListViewBean) JSPUtils.useBean(request, "manager",
                "globaz.osiris.db.contentieux.CAEtapeManagerListViewBean", "request");
        return manager;
    }
}
