/**
 * 
 */
package ch.globaz.perseus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.perseus.vb.facture.PFValidationViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author CEL
 * 
 */
public class PFFactureAction extends PFAbstractDefaultServletAction {

    public PFFactureAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        // Définition de l'action custom standard pour l'application WEB@AF
        // Attention, si appel de custom action, on passe le paramètre "id" au
        // lieu de "selectedId"
        String destination = null;
        boolean isAjax = false;
        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            /*
             * recupération du bean depuis la sesison
             */
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, dispatcher.getPrefix());
            /*
             * set des properietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = dispatcher.dispatch(viewBean, action);

            if (viewBean instanceof PFValidationViewBean) {
                request.setAttribute(FWServlet.VIEWBEAN, viewBean);
                isAjax = true;
            } else {
                session.setAttribute("viewBean", viewBean);
                /*
                 * choix de la destination _valid=fail : revient en mode edition
                 */
                boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
                if (goesToSuccessDest) {
                    destination = _getDestExecuterSucces(session, request, response, viewBean);
                } else {
                    destination = _getDestExecuterEchec(session, request, response, viewBean);
                }
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        if (isAjax) {
            destination = "/jade/ajax/templateAjax_afficherAJAX.jsp";
            servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
        } else {
            /*
             * redirection vers la destination
             */
            goSendRedirect(destination, request, response);
        }
    }
}
