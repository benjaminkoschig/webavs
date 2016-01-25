package globaz.osiris.servlet.action.contentieux;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.contentieux.CASequenceContentieuxManagerListViewBean;
import globaz.osiris.db.contentieux.CASequenceContentieuxViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour les séquences contentieux.
 */
public class CASequenceContentieuxAction extends CADefaultServletAction {
    /**
     * Constructor for CASequenceContentieux.
     * 
     * @param servlet
     */
    public CASequenceContentieuxAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_de.jsp";

        try {

            CASequenceContentieuxViewBean element;
            if (!JadeStringUtil.isBlank(request.getParameter("_method"))
                    && (request.getParameter("_method").equals("add"))) {
                element = new CASequenceContentieuxViewBean();
            } else {
                element = getSequenceContentieux(request);
            }

            element.setSession((BSession) mainDispatcher.getSession());
            element.retrieve();
            if (element.isNew()) {
                element.setSession((BSession) mainDispatcher.getSession());
                JSPUtils.setBeanProperties(request, element);
                element.retrieve();
            }

            element = (CASequenceContentieuxViewBean) mainDispatcher.dispatch(element, super.getAction());
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
            CASequenceContentieuxManagerListViewBean manager = getSequenceContentieuxManager(request);
            manager.setFromIdSequenceContentieux(element.getIdSequenceContentieux());
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

    protected CASequenceContentieuxViewBean getSequenceContentieux(HttpServletRequest request) throws ServletException {
        CASequenceContentieuxViewBean element = (CASequenceContentieuxViewBean) JSPUtils.useBean(request, "element",
                "globaz.osiris.db.contentieux.CASequenceContentieuxViewBean", "session");
        if ((!JadeStringUtil.isBlank(getId(request, "forIdSequenceContentieux")))
                && (!JadeStringUtil.isNull(super.getId(request, "fromIdSequenceContentieux")))) {
            element.setIdSequenceContentieux(super.getId(request, "fromIdSequenceContentieux"));
        } else {
            element = (CASequenceContentieuxViewBean) JSPUtils.useBean(request, "element",
                    "globaz.osiris.db.contentieux.CASequenceContentieuxViewBean", "session", true);
        }
        return element;
    }

    private CASequenceContentieuxManagerListViewBean getSequenceContentieuxManager(HttpServletRequest request)
            throws ServletException {
        CASequenceContentieuxManagerListViewBean manager = (CASequenceContentieuxManagerListViewBean) globaz.globall.http.JSPUtils
                .useBean(request, "manager", "globaz.osiris.db.contentieux.CASequenceContentieuxManagerListViewBean",
                        "request");
        return manager;
    }

}
