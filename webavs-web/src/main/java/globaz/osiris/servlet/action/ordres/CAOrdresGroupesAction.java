package globaz.osiris.servlet.action.ordres;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.ordres.CAOrdreGroupeManagerListViewBean;
import globaz.osiris.db.ordres.CAOrdreGroupeViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour l'affichage des ordres groupés.
 * 
 * @author DDA
 */
public class CAOrdresGroupesAction extends CADefaultServletAction {

    public CAOrdresGroupesAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWViewBeanInterface element = getOrdreGroupe(request, false);

            element = beforeAfficher(session, request, response, element);
            element = mainDispatcher.dispatch(element, FWAction.newInstance(request.getParameter("userAction")));

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
            CAOrdreGroupeViewBean element = getOrdreGroupe(request, true);
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
            CAOrdreGroupeManagerListViewBean viewBean = (CAOrdreGroupeManagerListViewBean) JSPUtils.useBean(request,
                    "manager", "globaz.osiris.db.ordres.CAOrdreGroupeManagerListViewBean", "request", true);
            viewBean.setISession(mainDispatcher.getSession());

            JSPUtils.setBeanProperties(request, viewBean);

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            viewBean = (CAOrdreGroupeManagerListViewBean) beforeLister(session, request, response, viewBean);
            viewBean = (CAOrdreGroupeManagerListViewBean) mainDispatcher.dispatch(viewBean, action);

            setSessionAttribute(session, VBL_ELEMENT, viewBean);

            _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);

    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String action = request.getParameter("userAction");
        String id = request.getParameter("selectedId");

        CAOrdreGroupeViewBean viewBean = new CAOrdreGroupeViewBean();
        viewBean.setId(id);
        if ((action != null) && (action.indexOf("osiris.ordres.ordresGroupes.valider") > -1)) {
            dispatcher.dispatch(viewBean, FWAction.newInstance(request.getParameter("userAction")));
            servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_rc.jsp")
                    .forward(request, response);
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

    /**
     * @see globaz.osiris.servlet.action.CADefaultServletAction#destinationAfterAjouter(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    protected String destinationAfterAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) {
        CAOrdreGroupeViewBean viewBean = (CAOrdreGroupeViewBean) session.getAttribute(VBL_ELEMENT);
        return getActionFullURL() + ".afficher&selectedId=" + viewBean.getIdOrdreGroupe();
    }

    public CAOrdreGroupeViewBean getOrdreGroupe(HttpServletRequest request, boolean isNew) throws ServletException {
        CAOrdreGroupeViewBean element = (CAOrdreGroupeViewBean) globaz.globall.http.JSPUtils.useBean(request,
                "element", "globaz.osiris.db.ordres.CAOrdreGroupeViewBean", "session", isNew);

        element.setUcEtat(null);

        if ((!JadeStringUtil.isBlank(getId(request, "forIdOrdreGroupe")))
                && (!JadeStringUtil.isNull(super.getId(request, "forIdOrdreGroupe")))) {
            element.setIdOrdreGroupe(super.getId(request, "forIdOrdreGroupe"));
        }

        return element;
    }

}