package globaz.apg.servlet;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.vb.droits.APDroitAPGDTO;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class APBreakRuleAction extends PRDefaultAction {
    /**
     * Crée une nouvelle instance de la classe PRDefaultAction.
     *
     * @param servlet
     */
    public APBreakRuleAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                  FWDispatcher mainDispatcher) throws ServletException, IOException {
        String idDroit = request.getParameter("forIdDroit");

        // on cherche le droit
        APDroitLAPG droit = new APDroitLAPG();
        droit.setSession((BSession) mainDispatcher.getSession());
        droit.setIdDroit(idDroit);
        try {
            droit.retrieve();
        } catch (Exception e) {
           // on ne met pas à jour le dto.
        }

        // on met à jour le dto pour le back
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, new APDroitAPGDTO(droit));

        forward(getRelativeURL(request, session) + "_rc.jsp", request, response);
    }

}
