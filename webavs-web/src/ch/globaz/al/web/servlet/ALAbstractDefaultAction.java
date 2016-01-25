package ch.globaz.al.web.servlet;

import globaz.al.vb.allocataire.ALAllocataireViewBean;
import globaz.al.vb.dossier.ALDossierMainViewBean;
import globaz.al.vb.droit.ALDroitViewBean;
import globaz.al.vb.echeances.ALEcheancesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Default Servlet Action mère de toutes les action servlets de l'application WEB@AF
 * 
 * @author VYJ
 */
public class ALAbstractDefaultAction extends FWDefaultServletAction {

    /**
     * @param servlet
     * 
     */
    public ALAbstractDefaultAction(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        if (viewBean instanceof ALEcheancesViewBean) {
            destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossier.chercher";

        } else {
            destination = super._getDestAjouterSucces(session, request, response, viewBean);
        }
        return destination;
    }

    /**
     * Appelé quand on revient sur la page après avec sélectionner un tiers
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChoisir(javax .servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChoisir(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        beforeAfficher(session, request, response, (FWViewBeanInterface) session.getAttribute("viewBean"));
        super.actionChoisir(session, request, response, mainDispatcher);
    }

    /**
     * gestion de toutes les actions custom de l'application webAF, définit notamment les droits par défaut nécessaires
     * à ces actions.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax .servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        // Définition de l'action custom standard pour l'application WEB@AF
        // Attention, si appel de custom action, on passe le paramètre "id" au
        // lieu de "selectedId"
        String destination = null;
        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            // Défini les droits par défaut nécessaire sur cette entité pour
            // lancer cette action custom
            action.setRight(FWSecureConstants.UPDATE);

            /*
             * recupération du bean depuis la sesison
             */
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, dispatcher.getPrefix());
            /*
             * set des properietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = dispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBean", viewBean);

            /*
             * choix de la destination _valid=fail : revient en mode edition
             */

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                destination = _getDestExecuterSucces(session, request, response, viewBean);
            } else {
                destination = _getDestExecuterEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        goSendRedirect(destination, request, response);
    }

    /**
     * Comportement identique à beforeAfficher (pour chargement de la liste des pays quand on réaffiche par ex.)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionReAfficher(javax .servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionReAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        beforeAfficher(session, request, response, (FWViewBeanInterface) session.getAttribute("viewBean"));
        super.actionReAfficher(session, request, response, mainDispatcher);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#addParametersFrom(javax.servlet.http.HttpServletRequest,
     *      java.lang.String)
     */
    @Override
    protected String addParametersFrom(HttpServletRequest request, String url) {

        String result = url;

        // Liste des paramètres à prendre en compte ( pour le moment aucun...)
        Map includeParams = new HashMap();
        // includeParams.put("1", "_method");

        Enumeration enumeration = request.getParameterNames();
        char separator = result.indexOf('?') == -1 ? '?' : '&';
        // pour chaque paramètre de requête, on regarde si ils sont dans les
        // paramètres
        // à prendre en compte

        while (enumeration.hasMoreElements()) {
            String paramName = (String) enumeration.nextElement();
            boolean add = false;
            Iterator i = includeParams.entrySet().iterator();

            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();

                // si on doit recupérer le paramètre et que pas déjà dans url
                // (donc défini dans getDest....)
                // on l'ajoute
                if (paramName.equals(me.getValue()) && (result.indexOf(me.getValue().toString()) == -1)) {
                    add = true;
                    break;
                }

            }

            if (add) {
                result += separator + paramName + "=" + URLEncoder.encode(request.getParameter(paramName));
                separator = '&';
            }
        }

        return result;
    }

    /**
     * Traitement des paramètres pour le menu options avec plusieurs afficher (selectedId / idDossier, ...)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax .servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof ALAllocataireViewBean) {
            if (JadeStringUtil.isEmpty(((ALAllocataireViewBean) viewBean).getAllocataireComplexModel()
                    .getAllocataireModel().getId())
                    && JadeStringUtil.isEmpty(request.getParameter("selectedId"))) {

                ((ALAllocataireViewBean) viewBean).setId(request.getParameter("idAllocataire"));

            }
        }

        if (viewBean instanceof ALDossierMainViewBean) {
            if (JadeStringUtil.isEmpty(((ALDossierMainViewBean) viewBean).getId())
                    && JadeStringUtil.isEmpty(request.getParameter("selectedId"))) {

                if (JadeStringUtil.isEmpty(request.getParameter("idDossier"))) {
                    ((ALDossierMainViewBean) viewBean).setId("");
                } else {
                    ((ALDossierMainViewBean) viewBean).setId(request.getParameter("idDossier"));
                }

            }
        }

        if (viewBean instanceof ALDroitViewBean) {
            if (JadeStringUtil.isEmpty(((ALDroitViewBean) viewBean).getId())
                    && JadeStringUtil.isEmpty(request.getParameter("selectedId"))) {

                if (JadeStringUtil.isEmpty(request.getParameter("idDroit"))) {
                    ((ALDroitViewBean) viewBean).setId("");
                } else {
                    ((ALDroitViewBean) viewBean).setId(request.getParameter("idDroit"));
                }

            }
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

}
