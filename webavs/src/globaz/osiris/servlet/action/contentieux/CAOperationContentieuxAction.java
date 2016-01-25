package globaz.osiris.servlet.action.contentieux;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CASectionViewBean;
import globaz.osiris.db.contentieux.CAEvenementContentieux;
import globaz.osiris.db.contentieux.CAEvenementContentieuxViewBean;
import globaz.osiris.db.contentieux.CAParametreEtapeManagerListViewBean;
import globaz.osiris.db.contentieux.CAParametreEtapeViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour l'execution manuelle d'une étape de contentieux.
 */
public class CAOperationContentieuxAction extends CADefaultServletAction {

    /**
     * Constructor for CAContentieux.
     * 
     * @param servlet
     */
    public CAOperationContentieuxAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_de.jsp";
        BSession objSession = (BSession) mainDispatcher.getSession();
        try {
            CAEvenementContentieuxViewBean element = getEvenementContentieux(request);
            element.setAlternateKey(CAEvenementContentieux.AK_IDSECPARAM);
            element.setRemarque(null);

            if (!JadeStringUtil.isNull(request.getParameter("selectedId"))) {
                element.setIdParametreEtape(request.getParameter("selectedId"));
            }
            if (!JadeStringUtil.isNull(request.getParameter("sectionId"))) {
                element.setIdSection(request.getParameter("sectionId"));
            }

            element.setSession(objSession);
            element.setEstModifie(Boolean.TRUE);
            element.retrieve();

            element = (CAEvenementContentieuxViewBean) mainDispatcher.dispatch(element, super.getAction());
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

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_rc.jsp";
        try {
            CASectionViewBean element = new CASectionViewBean();
            element.setIdSection(getId(request, "idSection"));
            element.setSession((BSession) mainDispatcher.getSession());
            element.retrieve();
            setSessionAttribute(session, VB_ELEMENT, element);

            destination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e) {
            destination = ERROR_PAGE;
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
        String _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        try {
            // Element
            CASectionViewBean element = getSection(request, mainDispatcher);
            element.setSession((BSession) mainDispatcher.getSession());
            element.retrieve();

            // Manager
            CAParametreEtapeManagerListViewBean manager = getParametreEtapeManager(request);
            manager.setSection(element);
            manager.setForIdSequenceContentieux(element.getIdSequenceContentieux());
            manager.setISession(mainDispatcher.getSession());
            mainDispatcher.dispatch(manager, getAction());
            JSPUtils.setBeanProperties(request, manager);
            setSessionAttribute(session, VBL_ELEMENT, manager);
            setSessionAttribute(session, VB_ELEMENT, element);
            _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);

    }

    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        CAEvenementContentieuxViewBean element = (CAEvenementContentieuxViewBean) viewBean;

        element.setModifie("Oui");

        return element;
    }

    public CAEvenementContentieuxViewBean getEvenementContentieux(HttpServletRequest request) throws ServletException {
        CAEvenementContentieuxViewBean element = (CAEvenementContentieuxViewBean) JSPUtils.useBean(request, "element",
                "globaz.osiris.db.contentieux.CAEvenementContentieuxViewBean", "session");
        return element;
    }

    protected CAParametreEtapeViewBean getParametreEtape(HttpServletRequest request) throws ServletException {
        CAParametreEtapeViewBean element = (CAParametreEtapeViewBean) JSPUtils.useBean(request, "manager",
                "globaz.osiris.db.contentieux.CAParametreEtapeViewBean", "request");
        if ((!JadeStringUtil.isBlank(getId(request, "forIdParametreEtape")))
                && (!JadeStringUtil.isNull(super.getId(request, "forIdParametreEtape")))) {
            element.setIdParametreEtape(super.getId(request, "forIdParametreEtape"));
        }
        return element;
    }

    protected CAParametreEtapeManagerListViewBean getParametreEtapeManager(HttpServletRequest request)
            throws ServletException {
        CAParametreEtapeManagerListViewBean manager = (CAParametreEtapeManagerListViewBean) JSPUtils.useBean(request,
                "manager", "globaz.osiris.db.contentieux.CAParametreEtapeManagerListViewBean", "request");
        return manager;
    }

    protected CASectionViewBean getSection(HttpServletRequest request, FWDispatcher mainDispatcher)
            throws ServletException, Exception {
        CASectionViewBean element = (CASectionViewBean) JSPUtils.useBean(request, "element",
                "globaz.osiris.db.comptes.CASectionViewBean", "session");
        if ((!JadeStringUtil.isBlank(getId(request, "idSection")))
                && (!JadeStringUtil.isNull(super.getId(request, "idSection")))) {
            element.setIdSection(super.getId(request, "idSection"));
        }

        return element;
    }

}
