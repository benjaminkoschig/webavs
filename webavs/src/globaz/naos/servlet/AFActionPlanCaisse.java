/*
 * Created on 18-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.naos.db.planCaisse.AFPlanCaisseViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité PlanCaisse.
 * 
 * @author sau
 */
public class AFActionPlanCaisse extends AFDefaultActionChercher {

    /**
     * Constructeur d'AFActionPlanCaisse.
     * 
     * @param servlet
     */
    public AFActionPlanCaisse(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Action utilisée pour la recherche.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        AFPlanCaisseViewBean vBean = new AFPlanCaisseViewBean();
        actionChercher(vBean, session, request, response, mainDispatcher);
    }
}