/*
 * Created on 18-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.naos.db.nombreAssures.AFNombreAssuresViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité NombreAssures.
 * 
 * @author sau
 */
public class AFActionNombreAssures extends AFDefaultActionChercher {

    /**
     * Constructeur d'AFActionNombreAssures.
     * 
     * @param servlet
     */
    public AFActionNombreAssures(FWServlet servlet) {
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

        AFNombreAssuresViewBean vBean = new AFNombreAssuresViewBean();
        vBean.setAffiliationId(request.getParameter("affiliationId"));

        actionChercher(vBean, session, request, response, mainDispatcher);
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

        AFNombreAssuresViewBean vBean = (AFNombreAssuresViewBean) viewBean;
        vBean.setAffiliationId(request.getParameter("affiliationId"));

        return vBean;
    }
}
