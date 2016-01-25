package globaz.osiris.servlet.action.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CARubriqueManager;
import globaz.osiris.db.comptes.CARubriqueManagerListViewBean;
import globaz.osiris.db.comptes.CARubriqueViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour lister les rubriques.
 * 
 * @author DDA
 */
public class CARubriqueAction extends CADefaultServletAction {

    /**
     * Constructor for CARubrique.
     * 
     * @param servlet
     */
    public CARubriqueAction(FWServlet servlet) {
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
                element = new CARubriqueViewBean();
            } else {
                element = getRubrique(request);
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
        String actionSuite = getActionSuite(request);
        try {
            if (actionSuite.equals("rubrique")) {
                CARubriqueManagerListViewBean manager = getRubriqueManager(request, mainDispatcher);
                JSPUtils.setBeanProperties(request, manager);
                setSessionAttribute(session, VBL_ELEMENT, manager);
            } else if (actionSuite.equals("rechercheRubrique")) {
                CARubriqueViewBean element = getRubrique(request);
                setSessionAttribute(session, VB_ELEMENT, element);
            }

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
            FWViewBeanInterface manager = getRubriqueManager(request, mainDispatcher);

            manager = beforeLister(session, request, response, manager);
            manager = mainDispatcher.dispatch(manager, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, VBL_ELEMENT, manager);

            _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);
    }

    protected CARubriqueViewBean getRubrique(HttpServletRequest request) throws ServletException {
        CARubriqueViewBean element = (CARubriqueViewBean) JSPUtils.useBean(request, "element",
                "globaz.osiris.db.comptes.CARubriqueViewBean", "session");
        if ((!JadeStringUtil.isBlank(getId(request, "rubriqueId")))
                && (!JadeStringUtil.isNull(getId(request, "rubriqueId")))) {
            element.setIdRubrique(getId(request, "rubriqueId"));
        } else {
            element = (CARubriqueViewBean) JSPUtils.useBean(request, "element",
                    "globaz.osiris.db.comptes.CARubriqueViewBean", "session", true);
        }

        return element;
    }

    protected CARubriqueManagerListViewBean getRubriqueManager(HttpServletRequest request, FWDispatcher mainDispatcher)
            throws ServletException, InvocationTargetException, IllegalAccessException {
        CARubriqueManagerListViewBean manager = (CARubriqueManagerListViewBean) JSPUtils.useBean(request, "manager",
                "globaz.osiris.db.comptes.CARubriqueManagerListViewBean", "request");

        JSPUtils.setBeanProperties(request, manager);

        manager.setOrderBy(CARubriqueManager.ORDER_IDEXTERNE);
        manager.setSession((BSession) mainDispatcher.getSession());

        if (JadeStringUtil.isNull(request.getParameter("forNatureRubrique"))) {
            manager.setForNatureRubrique("");
        }

        if (!JadeStringUtil.isNull(request.getParameter("fromDescription"))) {
            manager.setFromDescription(request.getParameter("fromDescription"));
        }

        if (!JadeStringUtil.isNull(request.getParameter("fromNumero"))) {
            manager.setFromNumero(request.getParameter("fromNumero"));
        }

        if (!JadeStringUtil.isEmpty(manager.getFromDescription())) {
            manager.setOrderBy(CARubriqueManager.ORDER_DESCRIPTION);
        } else {
            manager.setOrderBy(CARubriqueManager.ORDER_IDEXTERNE);
        }

        return manager;
    }

}
