package ch.globaz.perseus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.perseus.vb.pcfaccordee.PFCalculViewBean;
import globaz.perseus.vb.pcfaccordee.PFPcfAccordeeViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Controller des actions du package "perseus.dossier"
 * 
 * @author vyj
 */
public class PFPCFAccordeeAction extends PFAbstractDefaultServletAction {

    /**
     * @param aServlet
     */
    public PFPCFAccordeeAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PFPcfAccordeeViewBean) {
            return getActionFullURL() + ".chercher";
        }

        return super._getDestExecuterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PFPcfAccordeeViewBean) {
            PFPcfAccordeeViewBean vb = (PFPcfAccordeeViewBean) viewBean;
            return getActionFullURL() + ".afficher&selectedId=" + vb.getPcfAccordee().getId();
        }

        return super._getDestModifierSucces(session, request, response, viewBean);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        if ("calculer".equals(getAction().getActionPart())) {
            try {
                // FWViewBeanInterface viewBean = this.loadViewBean(session);
                String action = request.getParameter("userAction");
                FWAction _action = FWAction.newInstance(action);
                FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(_action, dispatcher.getPrefix());

                if (viewBean != null) {
                    JSPUtils.setBeanProperties(request, viewBean);
                }
                // request.setAttribute(FWServlet.VIEWBEAN, viewBean);
                session.setAttribute(FWServlet.VIEWBEAN, viewBean);
                dispatcher.dispatch(viewBean, getAction());
                PFCalculViewBean vb = (PFCalculViewBean) viewBean;

                if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
                    // A manager le message dans le viewbean
                    goSendRedirect(FWDefaultServletAction.ERROR_PAGE, request, response);
                } else {
                    goSendRedirect("perseus?userAction=perseus.pcfaccordee.pcfAccordee.afficher&selectedId="
                            + vb.getPcfAccordee().getId(), request, response);
                }

            } catch (Exception e) {
                // A manager le message dans le viewbean
                e.printStackTrace();
                goSendRedirect(FWDefaultServletAction.ERROR_PAGE, request, response);
            }
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }

    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PFCalculViewBean) {
            PFCalculViewBean vb = (PFCalculViewBean) viewBean;
            vb.setIdDemande(request.getParameter("idDemande"));
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

}
