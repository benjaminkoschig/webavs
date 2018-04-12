package ch.globaz.orion.servlet;

import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.log.JadeLogger;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.orion.service.EBRecapAfService;

public class EBRecapAfAction extends EBAbstractServletAction {

    public EBRecapAfAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        try {
            if ("valider".equals(getAction().getActionPart())) {
                EBRecapAfService.validerLigneRecapAf(session, request);
            } else if ("validerRadier".equals(getAction().getActionPart())) {
                // Processus de validation de la demande
                EBRecapAfService.validerRadierLigneRecapAf(session, request);
            }
        } catch (Exception e) {
            JadeLogger.error(e, e.getMessage());
            request.setAttribute("exception", e);
            servlet.getServletContext().getRequestDispatcher(FWDefaultServletAction.ERROR_AJAX_PAGE)
                    .forward(request, response);
        }
    }
}
