/*
 * Créé le 12 sept. 05
 */
package globaz.apg.servlet;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.vb.droits.APDroitAPGDTO;
import globaz.apg.vb.droits.APInfoComplViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APInfoComplAction extends PRDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJPrononceAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public APInfoComplAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc).
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getUserActionURL(request, IAPActions.ACTION_DROIT_LAPG + "." + FWAction.ACTION_CHERCHER);
    }

    /**
     * (non-Javadoc).
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getUserActionURL(request, IAPActions.ACTION_DROIT_LAPG + "." + FWAction.ACTION_CHERCHER);
    }

    /**
     * (non-Javadoc).
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getUserActionURL(request, IAPActions.ACTION_DROIT_LAPG + "." + FWAction.ACTION_CHERCHER);
    }

    /**
     * redefinition pour intercepter les retours depuis pyxis et faire en sorte que le cadre de detail s'affiche avec
     * les donnes de la situation professionnelle dont l'id de l'employeur vient d'etre modifie et pas une ecran vide.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWViewBeanInterface viewBean = loadViewBean(session);
        String typeInfoCompl = request.getParameter("typeInfoCompl");
        if (!JadeStringUtil.isIntegerEmpty(typeInfoCompl)) {
            ((APInfoComplViewBean) viewBean).setTypeInfoCompl(typeInfoCompl);
        }

        if (((APInfoComplViewBean) viewBean).isNew()) {
            ((APInfoComplViewBean) viewBean).setDestination(getRelativeURL(request, session) + "_de.jsp?_method=add");
            forward(getRelativeURL(request, session) + "_de.jsp?_method=add", request, response);
        } else {
            ((APInfoComplViewBean) viewBean).setDestination(getRelativeURL(request, session) + "_de.jsp?");
            forward(getRelativeURL(request, session) + "_de.jsp?", request, response);
        }
    }

    /**
     * Initializetion de l'ecran de saisie des informations complémentaires
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param vb
     * @return
     * @throws Exception
     */
    public void actionAfficherInfoCompl(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        try {
            String idDroit = request.getParameter("selectedId");

            // on cherche le droit
            APDroitLAPG droit = new APDroitLAPG();
            droit.setSession((BSession) mainDispatcher.getSession());
            droit.setIdDroit(idDroit);
            droit.retrieve();

            // on met à jour le dto pour le back
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, new APDroitAPGDTO(droit));

            // creation ou chargement de l'info. compl.
            APInfoComplViewBean infoCompl = new APInfoComplViewBean();
            infoCompl.setSession((BSession) mainDispatcher.getSession());
            infoCompl.setNoAVS(request.getParameter("noAVS"));
            infoCompl.setIdPrononce(idDroit);
            infoCompl.setGenreService(droit.getGenreService());

            String param = "&selectedId=";

            // si deja une info. compl. on set selectedId avec l'id de l'info.
            // compl.
            if (!JadeStringUtil.isIntegerEmpty(droit.getIdInfoCompl())) {
                infoCompl.setIdInfoCompl(droit.getIdInfoCompl());
                infoCompl.retrieve();

                param += droit.getIdInfoCompl();

                PRTiersWrapper tiersCaisse = PRTiersHelper.getAdministrationParId(mainDispatcher.getSession(),
                        infoCompl.getIdTiersCaisse());
                if (tiersCaisse != null) {
                    infoCompl.setNomCaisse(tiersCaisse.getProperty(PRTiersWrapper.PROPERTY_NOM));
                }

            } else {
                // si pas d'info. compl. on set le parametre method a
                // add ouvrir la jsp en mode creation
                param += "&_method=add";
            }

            // on sauve le viewBean en session
            session.removeAttribute(FWServlet.VIEWBEAN);
            session.setAttribute(FWServlet.VIEWBEAN, infoCompl);

            goSendRedirect(
                    getUserActionURL(request, IAPActions.ACTION_INFO_COMPL + "." + FWAction.ACTION_AFFICHER + param),
                    request, response);
        } catch (Exception e) {
            goSendRedirect(ERROR_PAGE, request, response);
        }
    }

}
