package ch.globaz.perseus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.menu.FWMenuBlackBox;
import globaz.framework.servlets.FWServlet;
import globaz.perseus.vb.dossier.PFDossierViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PFPaiementsAction extends PFAbstractDefaultServletAction {

    public PFPaiementsAction(FWServlet aServlet) {
        super(aServlet);
    }

    // @Override
    // protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
    // HttpServletResponse response, FWViewBeanInterface viewBean) {
    //
    // return "/perseus";
    // }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        if ("activer".equals(getAction().getActionPart()) || "desactiver".equals(getAction().getActionPart())) {
            try {
                FWMenuBlackBox menuBB = (FWMenuBlackBox) session.getAttribute(FWServlet.OBJ_USER_MENU);
                menuBB.setCurrentMenu("perseus-menuprincipal", "menu");
                menuBB.setCurrentMenu("perseus-optionsempty", "options");
                menuBB.setNodeActive("desactiver".equals(getAction().getActionPart()), "PF-activerValidationDecision",
                        "perseus-menuprincipal");
                menuBB.setNodeActive("activer".equals(getAction().getActionPart()), "PF-desactiverValidationDecision",
                        "perseus-menuprincipal");

                // FWViewBeanInterface viewBean = this.loadViewBean(session);
                String action = request.getParameter("userAction");
                FWAction _action = FWAction.newInstance(action);
                FWViewBeanInterface viewBean = new PFDossierViewBean();

                // request.setAttribute(FWServlet.VIEWBEAN, viewBean);
                session.setAttribute(FWServlet.VIEWBEAN, viewBean);
                dispatcher.dispatch(viewBean, getAction());

                if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
                    // A manager le message dans le viewbean
                    goSendRedirect(FWDefaultServletAction.ERROR_PAGE, request, response);
                } else {
                    goSendRedirect("perseus", request, response);
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

}
