package ch.globaz.pegasus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BProcess;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pegasus.vb.demanderenseignement.PCDemandeRenseignementViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PCDemandeRenseignementServletAction extends PCAbstractServletAction {

    private static final String BACK_TO_DEMANDE_LIST = "/pegasus?userAction=pegasus.demande.demande.chercher";

    /**
     * Constructeur
     * 
     * @param aServlet
     */
    public PCDemandeRenseignementServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        // String parametersStr = "&idVersionDroit=" + request.getParameter("idVersionDroit") + "&idDroit="
        // + request.getParameter("idDroit") + "&noVersion=" + request.getParameter("noVersion") + "&idDecision="
        // + request.getParameter("idDecision");

        String idDossier = request.getParameter("idDossier");

        return PCDemandeRenseignementServletAction.BACK_TO_DEMANDE_LIST.concat("&idDossier=").concat(idDossier);
    }

    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String destination = "";

        String action = request.getParameter("userAction");
        FWAction newAction = FWAction.newInstance(action);

        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
        String copies = "";

        if (viewBean instanceof PCDemandeRenseignementViewBean) {
            copies = request.getParameter("copieSelect");
        }

        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            if (BProcess.class.isAssignableFrom(viewBean.getClass())) {
                BProcess process = (BProcess) viewBean;
                process.setControleTransaction(true);
                process.setSendCompletionMail(true);
            }

            viewBean = mainDispatcher.dispatch(viewBean, newAction);
            request.setAttribute("viewBean", viewBean);

            /*
             * choix de la destination
             */
            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                destination = _getDestExecuterSucces(session, request, response, viewBean);
            } else {
                destination = _getDestExecuterEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PCDemandeRenseignementViewBean) {
            PCDemandeRenseignementViewBean vb = (PCDemandeRenseignementViewBean) viewBean;
            vb.setIdDemandePc(JadeStringUtil.toNotNullString(request.getParameter("idDemandePc")));
            vb.setIdDossier(JadeStringUtil.toNotNullString(request.getParameter("idDossier")));
            vb.setIdGestionnaire(JadeStringUtil.toNotNullString(request.getParameter("idGestionnaire")));
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

}
