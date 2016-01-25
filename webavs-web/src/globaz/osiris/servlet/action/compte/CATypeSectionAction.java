package globaz.osiris.servlet.action.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CATypeSectionManagerListViewBean;
import globaz.osiris.db.comptes.CATypeSectionViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour lister les types de sections.
 * 
 * @author DDA
 */
public class CATypeSectionAction extends CADefaultServletAction {

    /**
     * Constructor for CATypeSection.
     * 
     * @param servlet
     */
    public CATypeSectionAction(FWServlet servlet) {
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
                element = new CATypeSectionViewBean();
            } else {
                element = getTypeSection(request);
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
            CATypeSectionManagerListViewBean manager = getTypeSectionManager(request);
            setSessionAttribute(session, VBL_ELEMENT, manager);

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
            FWViewBeanInterface manager = getTypeSectionManager(request);

            manager = beforeLister(session, request, response, manager);
            manager = mainDispatcher.dispatch(manager, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, VBL_ELEMENT, manager);

            _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);

    }

    protected CATypeSectionViewBean getTypeSection(HttpServletRequest request) throws ServletException {
        CATypeSectionViewBean element = (CATypeSectionViewBean) JSPUtils.useBean(request, "element",
                "globaz.osiris.db.comptes.CATypeSectionViewBean", "session");

        if ((!JadeStringUtil.isBlank(getId(request, "idTypeSection")))
                && (!JadeStringUtil.isNull(super.getId(request, "idTypeSection")))) {
            element.setIdTypeSection(super.getId(request, "idTypeSection"));
        } else {
            element = (CATypeSectionViewBean) JSPUtils.useBean(request, "element",
                    "globaz.osiris.db.comptes.CATypeSectionViewBean", "session", true);
        }

        return element;
    }

    protected CATypeSectionManagerListViewBean getTypeSectionManager(HttpServletRequest request) throws Exception {
        CATypeSectionManagerListViewBean manager = (CATypeSectionManagerListViewBean) JSPUtils.useBean(request,
                "manager", "globaz.osiris.db.comptes.CATypeSectionManagerListViewBean", "request");

        JSPUtils.setBeanProperties(request, manager);

        if (!JadeStringUtil.isNull(super.getId(request, "fromIdTypeSection"))) {
            manager.setFromIdTypeSection(super.getId(request, "fromIdTypeSection"));
        }

        return manager;
    }

}
