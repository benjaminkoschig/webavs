package globaz.osiris.servlet.action.ordres;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CAOperationOrdreVersementManagerListViewBean;
import globaz.osiris.db.comptes.CAOperationOrdreVersementViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour l'affichage des ordres de versements dans l'ordre groupé.
 * 
 * @author DDA
 */
public class CAApercuOrdreVersementAction extends CADefaultServletAction {

    public CAApercuOrdreVersementAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_de.jsp";

        try {
            boolean isNew = (!JadeStringUtil.isBlank(request.getParameter("_method")) && (request
                    .getParameter("_method").equals("add")));

            CAOperationOrdreVersementViewBean element = getOrdreVersementDetail(session, request, response,
                    mainDispatcher, isNew);

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
            CAOperationOrdreVersementViewBean element = getOrdreVersement(request, mainDispatcher, false);
            CAOperationOrdreVersementManagerListViewBean manager = (CAOperationOrdreVersementManagerListViewBean) super
                    .getListViewBean(request, "comptes", "OperationOrdreVersement", true);
            manager.setISession(mainDispatcher.getSession());

            manager.setForIdOrdreGroupe(element.getIdOrdreGroupe());

            if (!JadeStringUtil.isNull(request.getParameter("fromNomCache"))) {
                manager.setFromNomCache(request.getParameter("fromNomCache"));
            }
            if (!JadeStringUtil.isNull(request.getParameter("forMontant"))) {
                manager.setForMontant(request.getParameter("forMontant"));
            }
            if (!JadeStringUtil.isNull(request.getParameter("forNumTransaction"))) {
                manager.setForNumTransaction(request.getParameter("forNumTransaction"));
            }

            JSPUtils.setBeanProperties(request, manager);

            manager = (CAOperationOrdreVersementManagerListViewBean) beforeLister(session, request, response, manager);
            manager = (CAOperationOrdreVersementManagerListViewBean) mainDispatcher.dispatch(manager,
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
                destination = destination.substring(0, destination.indexOf("Versement")) + ".chercher";
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    public CAOperationOrdreVersementViewBean getOrdreVersement(HttpServletRequest request, FWDispatcher mainDispatcher,
            boolean isNew) throws ServletException, Exception {
        CAOperationOrdreVersementViewBean element = (CAOperationOrdreVersementViewBean) super.getViewBean(request,
                "comptes", "OperationOrdreVersement", isNew);
        element.setSession((BSession) mainDispatcher.getSession());

        if (JadeStringUtil.isIntegerEmpty(element.getIdOrdreGroupe())
                && !JadeStringUtil.isNull(super.getId(request, "forIdOrdreGroupe"))) {
            element.setIdOrdreGroupe(super.getId(request, "forIdOrdreGroupe"));
        }

        return element;
    }

    public CAOperationOrdreVersementViewBean getOrdreVersementDetail(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, boolean isNew) throws ServletException,
            Exception {
        FWViewBeanInterface element = super.getViewBean(request, "comptes", "OperationOrdreVersement", isNew);
        ((CAOperationOrdreVersementViewBean) element).setSession((BSession) mainDispatcher.getSession());

        if ((!JadeStringUtil.isBlank(getId(request, "forIdOrdreGroupe")))
                && (!JadeStringUtil.isNull(super.getId(request, "forIdOrdreGroupe")))) {
            ((CAOperationOrdreVersementViewBean) element).setIdOperation(super.getId(request, "forIdOrdreGroupe"));

            element = beforeAfficher(session, request, response, element);
            element = mainDispatcher.dispatch(element, FWAction.newInstance(request.getParameter("userAction")));
        }

        return ((CAOperationOrdreVersementViewBean) element);
    }
}