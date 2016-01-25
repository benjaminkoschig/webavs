package globaz.osiris.servlet.action.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CATypeOperationManagerListViewBean;
import globaz.osiris.db.comptes.CATypeOperationViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour lister les opérations.
 * 
 * @author DDA
 */
public class CATypeOperationAction extends CADefaultServletAction {

    /**
     * Constructor for CATypeOperation.
     * 
     * @param servlet
     */
    public CATypeOperationAction(FWServlet servlet) {
        super(servlet);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWViewBeanInterface element;

            if (!JadeStringUtil.isBlank(request.getParameter("_method"))
                    && (request.getParameter("_method").equals("add"))) {
                element = new CATypeOperationViewBean();
            } else {
                element = getTypeOperation(request);
            }

            element = beforeAfficher(session, request, response, element);
            element = mainDispatcher.dispatch(element, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, VB_ELEMENT, element);

            _myDestination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);

    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_rc.jsp";

        try {
            CATypeOperationViewBean element = getTypeOperation(request);
            setSessionAttribute(session, VB_ELEMENT, element);

            _myDestination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);

    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionLister(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";

        try {
            FWViewBeanInterface manager = getTypeOperationManager(request);

            manager = beforeLister(session, request, response, manager);
            manager = mainDispatcher.dispatch(manager, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, VBL_ELEMENT, manager);

            _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);

    }

    protected CATypeOperationViewBean getTypeOperation(HttpServletRequest request) throws ServletException {
        CATypeOperationViewBean element = (CATypeOperationViewBean) JSPUtils.useBean(request, "element",
                "globaz.osiris.db.comptes.CATypeOperationViewBean", "session");

        if ((!JadeStringUtil.isBlank(getId(request, "idTypeOperation")))
                && (!JadeStringUtil.isNull(super.getId(request, "idTypeOperation")))) {
            element.setIdTypeOperation(super.getId(request, "idTypeOperation"));
        } else {
            element = (CATypeOperationViewBean) JSPUtils.useBean(request, "element",
                    "globaz.osiris.db.comptes.CATypeOperationViewBean", "session", true);
        }

        return element;
    }

    protected CATypeOperationManagerListViewBean getTypeOperationManager(HttpServletRequest request) throws Exception {
        CATypeOperationManagerListViewBean manager = (CATypeOperationManagerListViewBean) JSPUtils.useBean(request,
                "manager", "globaz.osiris.db.comptes.CATypeOperationManagerListViewBean", "request");

        JSPUtils.setBeanProperties(request, manager);

        if (!JadeStringUtil.isNull(super.getId(request, "fromTypeOperation"))) {
            manager.setFromIdTypeOperation(super.getId(request, "fromTypeOperation"));
        }

        return manager;
    }

}
