package globaz.osiris.servlet.action.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CAReferenceRubriqueManagerListViewBean;
import globaz.osiris.db.comptes.CAReferenceRubriqueViewBean;
import globaz.osiris.db.comptes.CARubriqueViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour les opérations de référence rubrique.
 * 
 * @author DDA
 */
public class CAReferenceRubriqueAction extends CADefaultServletAction {

    /**
     * Constructor for CAReferenceRubrique.
     * 
     * @param servlet
     */
    public CAReferenceRubriqueAction(FWServlet servlet) {
        super(servlet);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";
        try {
            FWViewBeanInterface element;
            if (!JadeStringUtil.isBlank(request.getParameter("_method"))
                    && (request.getParameter("_method").equals("add"))) {
                element = new CAReferenceRubriqueViewBean();
            } else {
                element = getReferenceRubrique(request);
            }

            element = beforeAfficher(session, request, response, element);
            element = mainDispatcher.dispatch(element, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, VB_ELEMENT, element);

            if (element.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail";
            } else {
                destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        CARubriqueViewBean vBean = new CARubriqueViewBean();
        String selectedId = request.getParameter("idRubrique");
        vBean.setIdRubrique(selectedId);

        String destination = "";

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            action.changeActionPart(FWAction.ACTION_AFFICHER);

            vBean = (CARubriqueViewBean) mainDispatcher.dispatch(vBean, action);

            setSessionAttribute(session, "viewBeanRC", vBean);
            destination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionLister(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_rcListe.jsp";

        try {
            FWViewBeanInterface manager = getReferenceRubriqueManager(request);

            manager = beforeAfficher(session, request, response, manager);
            manager = mainDispatcher.dispatch(manager, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, VBL_ELEMENT, manager);

            destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    protected CAReferenceRubriqueViewBean getReferenceRubrique(HttpServletRequest request) throws ServletException {
        CAReferenceRubriqueViewBean element = (CAReferenceRubriqueViewBean) JSPUtils.useBean(request, "element",
                "globaz.osiris.db.comptes.CAReferenceRubriqueViewBean", "session");
        if ((!JadeStringUtil.isBlank(getId(request, "refRubrique")))
                && (!JadeStringUtil.isNull(getId(request, "refRubrique")))) {
            element.setIdRefRubrique(getId(request, "refRubrique"));
        } else {
            element = (CAReferenceRubriqueViewBean) JSPUtils.useBean(request, "element",
                    "globaz.osiris.db.comptes.CAReferenceRubriqueViewBean", "session", true);
        }

        return element;
    }

    protected CAReferenceRubriqueManagerListViewBean getReferenceRubriqueManager(HttpServletRequest request)
            throws ServletException, InvocationTargetException, IllegalAccessException {
        CAReferenceRubriqueManagerListViewBean manager = (CAReferenceRubriqueManagerListViewBean) JSPUtils.useBean(
                request, "manager", "globaz.osiris.db.comptes.CAReferenceRubriqueManagerListViewBean", "request");

        try {
            JSPUtils.setBeanProperties(request, manager);
        } catch (Exception e) {
            throw new ServletException(e);
        }

        return manager;
    }
}
