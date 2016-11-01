package ch.globaz.orion.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.orion.vb.swissdec.EBPucsValidationDetailViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.orion.business.constantes.EBProperties;

public class EBActionValidationSwissDec extends FWDefaultServletAction {

    public EBActionValidationSwissDec(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        if (getAction().getActionPart().equals("accepter") || getAction().getActionPart().equals("refuser")) {
            _actionAccepterRefuser(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("annulerRefus")) {
            _actionAnnulerRefus(session, request, response, dispatcher);
        }
    }

    private void _actionAnnulerRefus(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        String destination = "";

        FWUrlsStack urlStack = (FWUrlsStack) session.getAttribute("urlStack");
        urlStack.pop();
        urlStack.pop();

        try {
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), dispatcher.getPrefix());
            EBPucsValidationDetailViewBean vb = (EBPucsValidationDetailViewBean) viewBean;

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            viewBean = dispatcher.dispatch(viewBean, getAction());
            session.setAttribute("viewBean", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                destination = "/" + getAction().getApplicationPart()
                        + "?userAction=orion.swissdec.pucsValidationDetail.afficher&id=" + vb.getCurrentId()
                        + "&selectedIds" + vb.getSelectedIds();
            }
        } catch (Exception e) {
            e.printStackTrace();
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);
    }

    private void _actionAccepterRefuser(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        String destination = "";

        FWUrlsStack urlStack = (FWUrlsStack) session.getAttribute("urlStack");
        urlStack.pop();

        try {
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), dispatcher.getPrefix());

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            viewBean = dispatcher.dispatch(viewBean, getAction());
            session.setAttribute("viewBean", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                EBPucsValidationDetailViewBean vb = (EBPucsValidationDetailViewBean) viewBean;
                destination = getDestinationAccordingToValidationType(vb);
            }
        } catch (Exception e) {
            e.printStackTrace();
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);
    }

    private void _actionAccepter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        String destination = "";

        try {
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), dispatcher.getPrefix());

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            viewBean = dispatcher.dispatch(viewBean, getAction());
            session.setAttribute("viewBean", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {

                EBPucsValidationDetailViewBean vb = (EBPucsValidationDetailViewBean) viewBean;

                destination = getDestinationAccordingToValidationType(vb);
                if (vb.wantNext()) {
                    FWUrlsStack urlStack = (FWUrlsStack) session.getAttribute("urlStack");
                    urlStack.pop();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        goSendRedirect(destination, request, response);
    }

    private String getDestinationAccordingToValidationType(EBPucsValidationDetailViewBean vb) {
        if (vb.wantNext()) {
            return "/" + getAction().getApplicationPart()
                    + "?userAction=orion.swissdec.pucsValidationDetail.afficher&id=" + vb.getNextId();

        } else {
            return "/" + getAction().getApplicationPart() + "?userAction=orion.pucs.pucsFile.chercher";
        }
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return viewBean;
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        try {
            if (EBProperties.VALIDATION_SWISSDEC.getValue().equals("active")) {
                super.actionAfficher(session, request, response, mainDispatcher);
            } else {
                goSendRedirect("/enConstruction.jsp", request, response);
            }
        } catch (PropertiesException e) {
            servlet.getServletContext().getRequestDispatcher(FWDefaultServletAction.ERROR_PAGE)
                    .forward(request, response);
        }

    }

}
