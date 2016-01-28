package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.annonceAffilie.AFAnnonceAffilieViewBean;
import globaz.naos.translation.CodeSystem;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité Annonce d'Affiliation.
 * 
 * @author sau
 */
public class AFActionAnnonceAffilie extends AFDefaultActionChercher {

    public final static String ACTION_CREE_AVIS_MUTATION = "creer";

    /**
     * Constructeur d'AFActionAnnonceAffilie.
     * 
     * @param servlet
     */
    public AFActionAnnonceAffilie(FWServlet servlet) {
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

        AFAnnonceAffilieViewBean vBean = new AFAnnonceAffilieViewBean();

        String affiliationId = "";
        if (!JadeStringUtil.isEmpty((String) session.getAttribute("affiliationPrincipale"))) {
            affiliationId = (String) session.getAttribute("affiliationPrincipale");
        }
        vBean.setAffiliationId(affiliationId);

        actionChercher(vBean, session, request, response, mainDispatcher);
    }

    /**
     * Action utilisée pour crée un Avis de mutation.
     * 
     * @param session
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void actionCreeAvisMutation(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws javax.servlet.ServletException, java.io.IOException {

        String _destination;

        AFAnnonceAffilieViewBean vBean = new AFAnnonceAffilieViewBean();

        // Tiers
        String idTiers = "";
        if (!JadeStringUtil.isEmpty((String) session.getAttribute("tiersPrincipale"))) {
            idTiers = (String) session.getAttribute("tiersPrincipale");
        } else if (!JadeStringUtil.isEmpty(request.getParameter("idTiers"))) {
            idTiers = request.getParameter("idTiers");
        }

        if (!JadeStringUtil.isEmpty(idTiers)) {
            try {
                BISession bSession = CodeSystem.getSession(session);
                vBean.setSession((BSession) bSession);

                vBean._creationAvisMutation();
                getAction().changeActionPart(FWAction.ACTION_AFFICHER);
            } catch (Exception e) {
                vBean.setMsgType(FWViewBeanInterface.ERROR);
                vBean.setMessage(e.getMessage());
            }
        }

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", vBean);

        if (vBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = ERROR_PAGE;
        } else if (JadeStringUtil.isEmpty(idTiers)) {
            // _destination =
            // "/"+getAction().getApplicationPart()+"?userAction=naos.affiliation.affilieSelect.chercher";
            _destination = "/" + getAction().getApplicationPart()
                    + "?userAction=naos.affiliation.autreDossier.afficher&_method=upd";
        } else {
            _destination = "/" + getAction().getApplicationPart()
                    + "?userAction=naos.affiliation.affiliation.chercher&idTiers=" + idTiers;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
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

        if (ACTION_CREE_AVIS_MUTATION.equals(getAction().getActionPart())) {
            actionCreeAvisMutation(session, request, response);
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

    /**
     * Effectue des traitements avant la recherche d'entités.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeLister(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        // AFAnnonceAffilieListViewBean vBean =
        // (AFAnnonceAffilieListViewBean)viewBean;
        // vBean.setForTraitement("1"); // TODO to be checked
        return super.beforeLister(session, request, response, viewBean);
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

        AFAnnonceAffilieViewBean vBean = (AFAnnonceAffilieViewBean) viewBean;
        vBean.setAffiliationId(request.getParameter("affiliationId"));

        return vBean;
    }
}
