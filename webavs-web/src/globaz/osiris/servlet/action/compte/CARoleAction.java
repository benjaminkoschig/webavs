package globaz.osiris.servlet.action.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CARoleManagerListViewBean;
import globaz.osiris.db.comptes.CARoleViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour lister les rôles.
 * 
 * @author DDA
 */
public class CARoleAction extends CADefaultServletAction {

    /**
     * Constructor for CARole.
     * 
     * @param servlet
     */
    public CARoleAction(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(HttpSession , HttpServletRequest,
     * HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_de.jsp";
        try {
            FWViewBeanInterface element;

            if (!JadeStringUtil.isBlank(request.getParameter("_method"))
                    && (request.getParameter("_method").equals("add"))) {
                element = new CARoleViewBean();
            } else {
                element = getRole(request);
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
            CARoleViewBean element = getRole(request);
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
            FWViewBeanInterface manager = getRoleManager(request);

            manager = beforeLister(session, request, response, manager);
            manager = mainDispatcher.dispatch(manager, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, VBL_ELEMENT, manager);

            _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);

    }

    protected CARoleViewBean getRole(HttpServletRequest request) throws ServletException {
        CARoleViewBean element = (CARoleViewBean) globaz.globall.http.JSPUtils.useBean(request, "element",
                "globaz.osiris.db.comptes.CARoleViewBean", "session");

        if ((!JadeStringUtil.isBlank(getId(request, "idRole"))) && (!JadeStringUtil.isNull(getId(request, "idRole")))) {
            element.setIdRole(getId(request, "idRole"));
        } else {
            element = (CARoleViewBean) globaz.globall.http.JSPUtils.useBean(request, "element",
                    "globaz.osiris.db.comptes.CARoleViewBean", "session", true);
        }

        return element;
    }

    protected CARoleManagerListViewBean getRoleManager(HttpServletRequest request) throws Exception {
        CARoleManagerListViewBean manager = (CARoleManagerListViewBean) globaz.globall.http.JSPUtils.useBean(request,
                "manager", "globaz.osiris.db.comptes.CARoleManagerListViewBean", "request");

        JSPUtils.setBeanProperties(request, manager);

        if (!JadeStringUtil.isNull(getId(request, "fromIdRole"))) {
            manager.setFromIdRole(getId(request, "fromIdRole"));
        }

        return manager;
    }

}
