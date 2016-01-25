/*
 * Created on 17-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliationViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entit� ParticulariteAffiliation.
 * 
 * @author sau
 */
public class AFActionParticulariteAffiliation extends AFDefaultActionChercher {

    /**
     * Constructeur d'AFActionParticulariteAffiliation.
     * 
     * @param servlet
     */
    public AFActionParticulariteAffiliation(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Action utilis�e pour la recherche.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        AFParticulariteAffiliationViewBean vBean = new AFParticulariteAffiliationViewBean();
        vBean.setAffiliationId(request.getParameter("affiliationId"));
        // TODO actionChercher =>
        // vBean.setSelection(request.getParameter("selectedId3")) a controller
        // !
        // vBean.setSelection(request.getParameter("selectedId3"));

        actionChercher(vBean, session, request, response, mainDispatcher);
    }

    /**
     * Effectue des traitements avant la saisie d'une nouvelle entit�.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeNouveau(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AFParticulariteAffiliationViewBean vBean = (AFParticulariteAffiliationViewBean) viewBean;
        vBean.setAffiliationId(request.getParameter("affiliationId"));

        return vBean;
    }
}
