/*
 * Created on 13-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.naos.db.masse.AFAnnonceSalairesViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité Cotisation.
 * 
 * @author sau
 */
public class AFActionAnnonceSalaires extends AFDefaultActionChercher {

    public final static String ACTION_RELOAD = "reload";

    /**
     * Constructeur d'AFActionCotisation.
     * 
     * @param servlet
     */
    public AFActionAnnonceSalaires(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Traitement des actions non standard.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        getAction().setRight(FWSecureConstants.READ);

        if (ACTION_RELOAD.equals(getAction().getActionPart())) {
            // reafraichir les menus deroulants de la page (en particulier le
            // menu des plans de caisse)
            AFAnnonceSalairesViewBean viewBean = (AFAnnonceSalairesViewBean) session.getAttribute("viewBean");

            try {
                JSPUtils.setBeanProperties(request, viewBean);
            } catch (Exception e) {
                viewBean.setMessage(e.getMessage());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

            servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp")
                    .forward(request, response);
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

}
