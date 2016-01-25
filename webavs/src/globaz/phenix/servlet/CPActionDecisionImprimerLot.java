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
public class CPActionDecisionImprimerLot extends globaz.framework.controller.FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CPActionImprimerLot.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public CPActionDecisionImprimerLot(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    // pour test -oca
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());
            request.setAttribute("viewBean", viewBean);
            _destination = "/phenix?userAction=phenix.principale.decision.imprimerLot";
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == false) {
                _destination = _destination + "&process=launched";
                response.sendRedirect(request.getContextPath() + _destination);
            } else {
                _destination = getRelativeURLwithoutClassPart(request, session) + "decisionImprimerLot_de.jsp";
                servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        // servlet.getServletContext().getRequestDispatcher(_destination).forward(request,
        // response);
        // goSendRedirect(_destination, request, response);
    }
}
