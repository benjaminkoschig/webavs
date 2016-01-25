/*
 * Created on 04-Mar-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.naos.db.parametreAssurance.AFParametreAssuranceViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité ParametreAssurance.
 * 
 * @author sau
 */
public class AFActionParametreAssurance extends AFDefaultActionChercher {

    /**
     * Constructeur d'AFActionParametreAssurance.
     * 
     * @param servlet
     */
    public AFActionParametreAssurance(FWServlet servlet) {
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

        String assuranceId = request.getParameter("assuranceId");
        String genre = request.getParameter("genre");

        AFParametreAssuranceViewBean viewBean = new AFParametreAssuranceViewBean();
        viewBean.setAssuranceId(assuranceId);
        viewBean.setGenre(genre);

        actionChercher(viewBean, session, request, response, mainDispatcher);
    }

    /**
     * Effectue des traitements avant la saisie d'une nouvelle entité.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeNouveau(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AFParametreAssuranceViewBean vBean = (AFParametreAssuranceViewBean) viewBean;
        vBean.setAssuranceId(request.getParameter("assuranceId"));

        return vBean;
    }
}
