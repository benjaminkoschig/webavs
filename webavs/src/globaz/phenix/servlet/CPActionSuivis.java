package globaz.phenix.servlet;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.phenix.vb.suivis.CPRevBilanViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CPActionSuivis extends FWDefaultServletAction {

    public CPActionSuivis(FWServlet servlet) {
        super(servlet);
    }

    private BSession _getSession(HttpSession session) {
        return (BSession) session.getAttribute(FWServlet.OBJ_SESSION);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = "";

        CPRevBilanViewBean vBean = new CPRevBilanViewBean();

        String affiliationId = request.getParameter("affiliationId");
        vBean.setAffiliationId(affiliationId);
        vBean.setEMailAddress(_getSession(session).getUserEMail());
        AFAffiliation aff = new AFAffiliation();
        aff.setSession(_getSession(session));
        aff.setAffiliationId(affiliationId);
        try {
            aff.retrieve();
            vBean.setDateDebut(aff.getDateDebut());
            vBean.setDateFin(aff.getDateFin());

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            action.changeActionPart(FWAction.ACTION_AFFICHER);
            vBean = (CPRevBilanViewBean) mainDispatcher.dispatch(vBean, action);
            session.setAttribute("viewBean", vBean);
            destination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            throw new ServletException(e);
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

}
