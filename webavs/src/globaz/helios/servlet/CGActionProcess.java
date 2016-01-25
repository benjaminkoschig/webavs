package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BProcess;
import globaz.globall.http.JSPUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la gestion des processus.
 * 
 * @author DDA
 * 
 */
public class CGActionProcess extends CGDefaultServletAction {

    public CGActionProcess(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String myDestination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, mainDispatcher.getPrefix());
            viewBean.setISession(mainDispatcher.getSession());

            JSPUtils.setBeanProperties(request, viewBean);

            setSessionAttribute(session, VIEWBEAN, viewBean);

            myDestination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            myDestination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
    }

    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination;

        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute(VIEWBEAN);
            JSPUtils.setBeanProperties(request, viewBean);

            ((BProcess) viewBean).setControleTransaction(true);
            ((BProcess) viewBean).setSendCompletionMail(true);
            ((BProcess) viewBean).setSendMailOnError(true);

            viewBean = mainDispatcher.dispatch(viewBean, FWAction.newInstance(request.getParameter("userAction")));

            request.setAttribute(VIEWBEAN, viewBean);

            if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = _getDestExecuterSucces(session, request, response, viewBean);
            } else {
                destination = _getDestExecuterEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);
    }
}
