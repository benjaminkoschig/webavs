package globaz.ij.servlet;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.ij.vb.controleAbsences.IJPrononceAjaxViewBean;
import globaz.prestation.servlet.PRHybridAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class IJPrononceAjaxAction extends PRHybridAction {

    public IJPrononceAjaxAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionModifierAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        FWAJAXViewBeanInterface viewBean = new IJPrononceAjaxViewBean();
        try {

            String action = request.getParameter("userAction");
            FWAction newAction = FWAction.newInstance(action);

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // _action.changeActionPart(FWAction.ACTION_MODIFIER_AJAX);
            viewBean.setGetListe(true);
            /*
             * beforeUpdate, call du dispatcher puis mis en session
             */
            viewBean = (FWAJAXViewBeanInterface) beforeModifier(session, request, response, viewBean);
            viewBean = (FWAJAXViewBeanInterface) mainDispatcher.dispatch(viewBean, newAction);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            /*
             * choix de la destination _valid=fail : revient en mode edition
             */

            redirectAJAXModificationDestination(session, request, response, mainDispatcher, viewBean);

        } catch (Exception e) {
            // this.excetpionAjax(e, request);
        }
        // super.actionModifierAJAX(session, request, response, mainDispatcher);
    }
}
