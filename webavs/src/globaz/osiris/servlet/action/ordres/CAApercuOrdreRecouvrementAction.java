package globaz.osiris.servlet.action.ordres;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrementManagerListViewBean;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrementViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour l'affichage des ordres de recouvrements dans l'ordre groupé.
 * 
 * @author DDA
 */
public class CAApercuOrdreRecouvrementAction extends CADefaultServletAction {

    public CAApercuOrdreRecouvrementAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_de.jsp";

        try {
            CAOperationOrdreRecouvrementViewBean element = getOrdreRecouvrementDetail(session, request, response,
                    mainDispatcher);

            setSessionAttribute(session, VB_ELEMENT, element);

            if (element.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _myDestination = getRelativeURL(request, session) + "_de.jsp?_valid=fail";
            }

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
            CAOperationOrdreRecouvrementViewBean element = getOrdreRecouvrement(request, mainDispatcher, false);
            CAOperationOrdreRecouvrementManagerListViewBean manager = (CAOperationOrdreRecouvrementManagerListViewBean) super
                    .getListViewBean(request, "comptes", "OperationOrdreRecouvrement", true);

            manager.setISession(mainDispatcher.getSession());

            JSPUtils.setBeanProperties(request, manager);
            manager.setForIdOrdreGroupe(element.getIdOrdreGroupe());

            manager = (CAOperationOrdreRecouvrementManagerListViewBean) beforeLister(session, request, response,
                    manager);
            manager = (CAOperationOrdreRecouvrementManagerListViewBean) mainDispatcher.dispatch(manager,
                    FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, VBL_ELEMENT, manager);

        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);

    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionModifier(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getActionFullURL() + ".chercher";

        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute(VB_ELEMENT);

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, VB_ELEMENT, viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = getRelativeURL(request, session) + "_de.jsp";
            } else {
                destination = getActionFullURL();
                destination = destination.substring(0, destination.indexOf("Recouvrement")) + ".chercher";
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    protected String destinationAfterModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) {
        return getActionFullURL().substring(0, getActionFullURL().indexOf("Recouvrement"))
                + ".chercher&forIdOrdreGroupe=" + super.getId(request, "forIdOrdreGroupe");
    }

    public CAOperationOrdreRecouvrementViewBean getOrdreRecouvrement(HttpServletRequest request,
            FWDispatcher mainDispatcher, boolean isNew) throws ServletException, Exception {
        CAOperationOrdreRecouvrementViewBean element = (CAOperationOrdreRecouvrementViewBean) super.getViewBean(
                request, "comptes", "operationOrdreRecouvrement", isNew);
        element.setSession((BSession) mainDispatcher.getSession());

        if ((!JadeStringUtil.isBlank(getId(request, "forIdOrdreGroupe")))
                && (!JadeStringUtil.isNull(super.getId(request, "forIdOrdreGroupe")))) {
            element.setIdOrdreGroupe(super.getId(request, "forIdOrdreGroupe"));
        }

        return element;
    }

    public CAOperationOrdreRecouvrementViewBean getOrdreRecouvrementDetail(HttpSession session,
            HttpServletRequest request, HttpServletResponse response, FWDispatcher mainDispatcher)
            throws ServletException, Exception {
        FWViewBeanInterface element = super.getViewBean(request, "comptes", "OperationOrdreRecouvrement", true);
        ((CAOperationOrdreRecouvrementViewBean) element).setSession((BSession) mainDispatcher.getSession());

        if ((!JadeStringUtil.isBlank(getId(request, "forIdOrdreGroupe")))
                && (!JadeStringUtil.isNull(super.getId(request, "forIdOrdreGroupe")))) {
            ((CAOperationOrdreRecouvrementViewBean) element).setIdOperation(super.getId(request, "forIdOrdreGroupe"));

            element = beforeAfficher(session, request, response, element);
            element = mainDispatcher.dispatch(element, FWAction.newInstance(request.getParameter("userAction")));
        }

        return ((CAOperationOrdreRecouvrementViewBean) element);
    }
}