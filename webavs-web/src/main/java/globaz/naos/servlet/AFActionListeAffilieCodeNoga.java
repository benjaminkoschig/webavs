/**
 * 
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.naos.db.noga.AFListeAffiliesCodeNogaViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author est
 * 
 */
public class AFActionListeAffilieCodeNoga extends FWDefaultServletAction {

    public AFActionListeAffilieCodeNoga(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        if ("genererListe".equals(getAction().getActionPart())) {
            // actionGenererListe(session, request, response, dispatcher);
        }
    }

    @Override
    public void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String _destination = "";

        try {
            String action = request.getParameter("userAction");
            FWAction newAction = FWAction.newInstance(action);

            AFListeAffiliesCodeNogaViewBean viewBean = (AFListeAffiliesCodeNogaViewBean) session
                    .getAttribute("viewBean");
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = (AFListeAffiliesCodeNogaViewBean) dispatcher.dispatch(viewBean, newAction);
            request.setAttribute("viewBean", viewBean);

            /*
             * choix de la destination
             */
            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                _destination = _getDestExecuterSucces(session, request, response, viewBean);
            } else {
                _destination = _getDestExecuterEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        goSendRedirect(_destination, request, response);
    }

}
