package globaz.naos.servlet;

import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.naos.db.fact.AFFactViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entit? Facturation (depuis une Affiliation).
 * 
 * @author sau
 */
public class AFActionFactAffilie extends AFDefaultActionChercher {

    /**
     * Constructeur d'AFActionFactAffilie.
     * 
     * @param servlet
     */
    public AFActionFactAffilie(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Action utilis?e pour la recherche.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        AFFactViewBean vBean = new AFFactViewBean();
        vBean.setAffiliationId(request.getParameter("affiliationId"));

        actionChercher(vBean, session, request, response, mainDispatcher);
    }
}
