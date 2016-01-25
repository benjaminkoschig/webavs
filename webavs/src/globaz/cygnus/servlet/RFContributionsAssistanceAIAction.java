package globaz.cygnus.servlet;

import globaz.cygnus.vb.contributions.RFContributionsAssistanceAIDetailViewBean;
import globaz.cygnus.vb.contributions.RFContributionsAssistanceAIViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RFContributionsAssistanceAIAction extends RFDefaultAction {

    public RFContributionsAssistanceAIAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        RFContributionsAssistanceAIDetailViewBean viewBean = new RFContributionsAssistanceAIDetailViewBean();

        String userAction = request.getParameter("userAction");
        FWAction action = FWAction.newInstance(userAction);

        String idContributionAssistanceAI = request.getParameter("idContributionAssistanceAI");
        viewBean.setIdContributionAssistanceAI(idContributionAssistanceAI);

        String idDossierRFM = request.getParameter("idDossierRFM");
        viewBean.setIdDossierRFM(idDossierRFM);

        viewBean = (RFContributionsAssistanceAIDetailViewBean) mainDispatcher.dispatch(viewBean, action);

        session.setAttribute("detailViewBean", viewBean);

        forward(getRelativeURL(request, session) + "_de.jsp", request, response);
    }

    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        RFContributionsAssistanceAIDetailViewBean viewBean = (RFContributionsAssistanceAIDetailViewBean) session
                .getAttribute("detailViewBean");

        if (viewBean == null) {
            viewBean = new RFContributionsAssistanceAIDetailViewBean();
        }
        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        viewBean = (RFContributionsAssistanceAIDetailViewBean) mainDispatcher.dispatch(viewBean, getAction());

        String destination = null;
        boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
        if (goesToSuccessDest) {
            destination = _getDestAjouterSucces(session, request, response, viewBean);
        } else {
            destination = _getDestAjouterEchec(session, request, response, viewBean);
        }

        session.setAttribute("detailViewBean", viewBean);
        goSendRedirect(destination, request, response);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        RFContributionsAssistanceAIViewBean viewBean = new RFContributionsAssistanceAIViewBean();
        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            viewBean = (RFContributionsAssistanceAIViewBean) mainDispatcher.dispatch(viewBean, getAction());
        }

        this.saveViewBean(viewBean, session);
        forward(getRelativeURL(request, session) + "_rc.jsp", request, response);
    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        RFContributionsAssistanceAIDetailViewBean viewBean = (RFContributionsAssistanceAIDetailViewBean) session
                .getAttribute("detailViewBean");

        if (viewBean == null) {
            viewBean = new RFContributionsAssistanceAIDetailViewBean();
        }
        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception ex) {
            viewBean.setMessage(ex.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            viewBean = (RFContributionsAssistanceAIDetailViewBean) mainDispatcher.dispatch(viewBean, getAction());
        }

        String destination = null;
        boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
        if (goesToSuccessDest) {
            destination = _getDestModifierSucces(session, request, response, viewBean);
        } else {
            destination = _getDestModifierEchec(session, request, response, viewBean);
        }

        session.setAttribute("detailViewBean", viewBean);
        goSendRedirect(destination, request, response);
    }

    @Override
    protected void actionSupprimer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        RFContributionsAssistanceAIDetailViewBean viewBean = (RFContributionsAssistanceAIDetailViewBean) session
                .getAttribute("detailViewBean");

        if (viewBean == null) {
            viewBean = new RFContributionsAssistanceAIDetailViewBean();
        }
        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception ex) {
            viewBean.setMessage(ex.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            viewBean = (RFContributionsAssistanceAIDetailViewBean) beforeSupprimer(session, request, response, viewBean);
            viewBean = (RFContributionsAssistanceAIDetailViewBean) mainDispatcher.dispatch(viewBean, getAction());
        }

        String destination = null;
        boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
        if (goesToSuccessDest) {
            destination = _getDestModifierSucces(session, request, response, viewBean);
        } else {
            destination = _getDestModifierEchec(session, request, response, viewBean);
        }

        goSendRedirect(destination, request, response);
    }
}
