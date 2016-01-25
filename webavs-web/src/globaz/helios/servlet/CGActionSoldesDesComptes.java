package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWListViewBeanActionFactory;
import globaz.globall.http.JSPUtils;
import globaz.helios.db.comptes.CGSoldesDesComptesListViewBean;
import globaz.helios.tools.CGSessionDataContainerHelper;
import globaz.jade.client.util.JadeStringUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour lister le solde des comptes
 * 
 * @author DDA
 * 
 */
public class CGActionSoldesDesComptes extends CGDefaultServletAction {

    public CGActionSoldesDesComptes(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = getRelativeURL(request, session) + "_rcListe.jsp";

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            FWViewBeanInterface viewBean = FWListViewBeanActionFactory.newInstance(action, mainDispatcher.getPrefix());

            JSPUtils.setBeanProperties(request, viewBean);

            if ((JadeStringUtil.isBlank(request.getParameter("reqMontant")))
                    && (!JadeStringUtil.isBlank(request.getParameter("reqMontantOfas")))) {
                ((CGSoldesDesComptesListViewBean) viewBean).setReqMontant(request.getParameter("reqMontantOfas"));
            }

            if ((JadeStringUtil.isBlank(request.getParameter("reqCritere")))
                    && (!JadeStringUtil.isBlank(request.getParameter("reqCritereOfas")))) {
                ((CGSoldesDesComptesListViewBean) viewBean).setReqCritere(request.getParameter("reqCritereOfas"));
            }

            viewBean = beforeLister(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, action);

            request.setAttribute(VIEWBEAN, viewBean);

            setSessionAttribute(session, "listViewBean", viewBean);

            destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#beforeLister(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String idPeriodeComptable = ((CGSoldesDesComptesListViewBean) viewBean).getReqPeriodeComptable();

        CGSessionDataContainerHelper sessionDataContainer = new CGSessionDataContainerHelper();
        sessionDataContainer.setData(session, CGSessionDataContainerHelper.KEY_LAST_SELECTED_ID_PERIODE,
                idPeriodeComptable.trim());

        String idTypeCompta = ((CGSoldesDesComptesListViewBean) viewBean).getReqComptabilite();
        sessionDataContainer.setData(session, CGSessionDataContainerHelper.KEY_LAST_SELECTED_ID_TYPE_COMPTA,
                idTypeCompta.trim());

        return viewBean;
    }

}
