/*
 * Created on 14-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.masse.AFMasseModifierViewBean;
import globaz.naos.translation.CodeSystem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité Masse (pour la modification).
 * 
 * @author sau
 */
public class AFActionMasseModifier extends FWDefaultServletAction {

    /**
     * Constructeur d'AFActionMasseModifier.
     * 
     * @param servlet
     */
    public AFActionMasseModifier(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Effectue les traitements pour en afficher les détails.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            AFMasseModifierViewBean viewBean = (AFMasseModifierViewBean) FWViewBeanActionFactory.newInstance(action,
                    mainDispatcher.getPrefix());

            BSession bSession = (BSession) CodeSystem.getSession(session);

            viewBean = (AFMasseModifierViewBean) beforeAfficher(session, request, response, viewBean);
            viewBean.setSession(bSession);
            viewBean.setAffiliationId(request.getParameter("affiliationId"));
            viewBean.retrieveCotisation();

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Effectue les modifications.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionModifier(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";

        AFMasseModifierViewBean viewBean = (AFMasseModifierViewBean) session.getAttribute("viewBean");
        String forAction = request.getParameter("forAction");

        try {
            viewBean.updateMasse(request);
            if (viewBean.getSession().hasErrors()) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(viewBean.getSession().getErrors().toString());
            }
            session.setAttribute("viewBean", viewBean);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
            _destination = _getDestEchec(session, request, response, viewBean);
        } else {
            if ("updateMasse".equals(forAction)) {
                _destination = "/"
                        + getAction().getApplicationPart()
                        + "?userAction=naos.affiliation.autreDossier.afficher&_valid=fail&_method=upd&forAction=updateMasse";
            } else {
                // _destination = "/" + getAction().getApplicationPart() +
                // "?userAction=naos.affiliation.affiliation.afficher";
                _destination = "/" + getAction().getApplicationPart()
                        + "?userAction=naos.cotisation.cotisation.chercher&affiliationId="
                        + viewBean.getAffiliationId();
            }

        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    };

    /**
     * Effectue des traitements avant recupération de l'entité dans la DB, pour l'afficher.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AFMasseModifierViewBean vBean = (AFMasseModifierViewBean) viewBean;
        vBean.setAffiliationId(request.getParameter("affiliationId"));

        return vBean;
    }

}
