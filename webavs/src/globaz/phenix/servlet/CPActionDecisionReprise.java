package globaz.phenix.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Pour gérer les actions impressions notamment la redirection après une impression par lot Date de création :
 * (06.06.2003 09:47:53)
 * 
 * @author: ado
 */
public class CPActionDecisionReprise extends globaz.framework.controller.FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CPActionImprimerLot.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public CPActionDecisionReprise(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    // pour test -oca
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = "";
        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());
            request.setAttribute("viewBean", viewBean);
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail";
            } else {
                // destination = getActionBack();
                // destination = getRelativeURL(request, session) + "_de.jsp";
                destination = "/" + getAction().getApplicationPart() + "?userAction="
                        + getAction().getApplicationPart() + "." + getAction().getPackagePart()
                        + ".decision.reprise&processStarted=yes";
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }
}
