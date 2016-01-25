package globaz.corvus.servlet;

/**
 * Author scr
 */
import globaz.corvus.vb.process.REConcordanceCentraleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BProcess;
import globaz.prestation.servlet.PRDefaultAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class REConcordanceCentraleAction extends PRDefaultAction {

    public REConcordanceCentraleAction(FWServlet servlet) {
        super(servlet);
    }

    // Surcharge de l'action afficher, la user action start with
    // concordanceCentrale.corvus , ceci pour
    // que personne excepté si maj dans le module admin n'aient accès à cette
    // fonction.

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String destination = "";
        try {

            REConcordanceCentraleViewBean vb = new REConcordanceCentraleViewBean();
            vb.setISession(mainDispatcher.getSession());

            String action = request.getParameter("userAction");

            if (action.startsWith("concordanceCentrale_")) {
                action = action.substring(20, action.length());
            } else {
                throw new Exception("Unsupported action error");
            }

            FWAction _act = FWAction.newInstance(action);
            /*
             * initialisation du viewBean
             */

            /*
             * appelle beforeAfficher, puis le Dispatcher, puis met le bean en session
             */
            FWViewBeanInterface viewBean = mainDispatcher.dispatch(vb, _act);
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);
            /*
             * choix destination
             */

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = ERROR_PAGE;
            } else {
                destination = getRelativeURL(request, session) + "_de.jsp";
                if (destination.startsWith("/concordanceCentrale_")) {
                    destination = destination.substring(21, destination.length());
                    destination = "/" + destination;
                } else if (destination.startsWith("concordanceCentrale_")) {
                    destination = destination.substring(20, destination.length());
                }

            }

            /*
             * redirection vers la destination
             */
            servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

        } catch (Exception e) {
            destination = ERROR_PAGE;
            servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

        }
    }

    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String destination = "";
        try {

            String action = request.getParameter("userAction");
            if (action.startsWith("concordanceCentrale_")) {
                action = action.substring(20, action.length());
            } else {
                throw new Exception("Unsupported action error");
            }

            FWAction _action = FWAction.newInstance(action);

            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            if (BProcess.class.isAssignableFrom(viewBean.getClass())) {
                BProcess process = (BProcess) viewBean;
                process.setControleTransaction(true);
                process.setSendCompletionMail(true);
            }
            viewBean = mainDispatcher.dispatch(viewBean, _action);
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
            destination = ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        goSendRedirect(destination, request, response);

    }

}
