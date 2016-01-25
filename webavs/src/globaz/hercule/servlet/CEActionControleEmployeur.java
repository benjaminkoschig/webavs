package globaz.hercule.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWSmartUrl;
import globaz.framework.utils.urls.FWUrl;
import globaz.globall.db.BSession;
import globaz.hercule.db.controleEmployeur.CEControleEmployeurViewBean;
import globaz.hercule.db.controleEmployeur.CEImprimerControleViewBean;
import globaz.hercule.db.controleEmployeur.CELettreProchainControleViewBean;
import globaz.jade.log.JadeLogger;
import globaz.musca.translation.CodeSystem;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité Control Employeur.
 * 
 * @author sau
 * @since Created on 13-Jan-05
 */
public class CEActionControleEmployeur extends FWDefaultServletAction {

    /**
     * Constructeur d'AFActionControleEmployeur.
     * 
     * @param servlet
     */
    public CEActionControleEmployeur(FWServlet servlet) {
        super(servlet);
    }

    private void _actionImprimerControle(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {

            /*
             * Creation dynamique de notre viewBean
             */
            CEImprimerControleViewBean viewBean = new CEImprimerControleViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Id du passage passé en paramètre
            String selectedId = request.getParameter("selectedId");
            viewBean.setControleId(selectedId);
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURLwithoutClassPart(request, session) + "imprimerControle_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    private void _actionLettreProchainControle(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {

            /*
             * Creation dynamique de notre viewBean
             */
            CELettreProchainControleViewBean viewBean = new CELettreProchainControleViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Id du passage passé en paramètre
            String selectedId = request.getParameter("selectedId");
            viewBean.setIdControle(selectedId);
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURLwithoutClassPart(request, session) + "lettreProchainControle_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        CEControleEmployeurViewBean bean = (CEControleEmployeurViewBean) viewBean;
        return getAction().getApplicationPart()
                + "?userAction=hercule.controleEmployeur.listeControleEmployeur.chercher&likeNumAffilie="
                + bean.getNumAffilie();
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        CEControleEmployeurViewBean bean = (CEControleEmployeurViewBean) viewBean;
        return getAction().getApplicationPart()
                + "?userAction=hercule.controleEmployeur.listeControleEmployeur.chercher&likeNumAffilie="
                + bean.getNumAffilie();
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getAction().getApplicationPart()
                + "?userAction=hercule.controleEmployeur.listeControleEmployeur.chercher";
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {

        if (getAction().getActionPart().equals("imprimerControle")) {
            // chercher avec chargement des données nécessaire
            _actionImprimerControle(session, request, response, dispatcher);
        }
        if (getAction().getActionPart().equals("lettreProchainControle")) {
            _actionLettreProchainControle(session, request, response, dispatcher);
        }
    }

    @Override
    protected String addParametersFrom(HttpServletRequest request, String url) throws UnsupportedEncodingException {
        FWSmartUrl sUrl = new FWSmartUrl(new FWUrl(request));
        if ("hercule.controleEmployeur.controleEmployeur.modifier".equals(sUrl.getUserAction())) {
            return url;
        } else {
            return super.addParametersFrom(request, url);
        }
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

        CEControleEmployeurViewBean vBean = (CEControleEmployeurViewBean) viewBean;
        vBean.setAffiliationId(request.getParameter("affiliationId"));
        vBean.setDateDebutControle(request.getParameter("dateDebutControle"));

        return vBean;
    }
}