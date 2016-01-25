package globaz.aquila.servlet;

import globaz.aquila.application.COApplication;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.poursuite.COEtapeSuivanteListViewBean;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.config.COConfigurationService;
import globaz.aquila.util.COActionUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <h1>Description</h1>
 * <p>
 * Action dédiée aux poursuites.
 * </p>
 * <p>
 * Gère toutes les actions relatives à l'affichage et à la liste des contentieux de n'importe quel type.
 * </p>
 * 
 * @author Pascal Lovy, 01-oct-2004
 */
public class COActionPoursuite extends CODefaultServletAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final COConfigurationService CONFIG_SERVICE = COServiceLocator.getConfigService();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Initialise l'action.
     * 
     * @param servlet
     *            Le servlet concerné par cette action
     */
    public COActionPoursuite(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Redirige éventuellement sur la page de confirmation de mutation.
     * <p>
     * La redirection est faite uniquement si une {@link COApplication#PROPERTY_DELAI_MUTATION_CONFIRMATION propriété}
     * de l'application est à vrai.
     * </p>
     * 
     * @param session
     * @param request
     * @param response
     * @param viewBean
     * @return
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        /*
         * on vient d'ajouter un contentieux. si on a modifié la prochaine date de déclenchement, on veut eventuellement
         * afficher l'écran d'envoi de la confirmation de mutation, sinon on redirige sur la page standard.
         */
        COContentieux contentieux = (COContentieux) viewBean;

        try {
            if (contentieux.isProchaineDateDeclenchementChangee()
                    && COActionPoursuite.CONFIG_SERVICE.getOption((BSession) viewBean.getISession(),
                            COConfigurationService.CONFIRMATION_DELAI_MUTATION).booleanValue()) {
                // il faut envoyer une confirmation, on redirige vers la page
                // d'envoi
                return getDestConfirmationMutation();
            }
        } catch (Exception e) {
            e.printStackTrace();

            return FWDefaultServletAction.ERROR_PAGE;
        }

        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    /**
     * Redirige éventuellement sur la page de confirmation de mutation.
     * <p>
     * La redirection est faite uniquement si une {@link COApplication#PROPERTY_DELAI_MUTATION_CONFIRMATION propriété}
     * de l'application est à vrai.
     * </p>
     * 
     * @param session
     * @param request
     * @param response
     * @param viewBean
     * @return
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        /*
         * on vient de modifier un contentieux. si on a modifié la prochaine date de déclenchement, on veut
         * eventuellement afficher l'écran d'envoi de la confirmation de mutation, sinon on redirige sur la page
         * standard.
         */
        COContentieux contentieux = (COContentieux) viewBean;

        try {
            if (contentieux.isProchaineDateDeclenchementChangee()
                    && COActionPoursuite.CONFIG_SERVICE.getOption((BSession) viewBean.getISession(),
                            COConfigurationService.CONFIRMATION_DELAI_MUTATION).booleanValue()) {
                // il faut envoyer une confirmation, on redirige vers la page
                // d'envoi
                return getDestConfirmationMutation();
            }
        } catch (Exception e) {
            e.printStackTrace();

            return FWDefaultServletAction.ERROR_PAGE;
        }

        return super._getDestModifierSucces(session, request, response, viewBean);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = "";
        BSession appSession = (BSession) mainDispatcher.getSession();

        try {
            COContentieux contentieux = chargerContentieux(request, session, appSession);
            FWAction action = COActionUtils.createAction(contentieux, mainDispatcher.getPrefix(), getAction()
                    .getActionPart());

            if (COContentieux.ID_CONTENTIEUX_BIDON.equals(contentieux.getIdContentieux())) {
                action.changeActionPart(FWAction.ACTION_NOUVEAU);
            }

            mainDispatcher.dispatch(contentieux, action);
            session.setAttribute("viewBean", contentieux);

            request.getSession().setAttribute(globaz.pyxis.summary.TIActionSummary.PYXIS_VG_IDTIERS_CTX,
                    contentieux.getCompteAnnexe().getIdTiers());

            // il ne devrait pas y avoir d'erreur ici mais on ne sait jamais
            if (((FWViewBeanInterface) contentieux).getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                // Détermine la destination en fonction du type du contentieux
                destination = getRelativeURLwithoutClassPart(request, session) + action.getClassPart() + "_de.jsp";
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * "Nettoye" les effets de bords de la méthode {@link JSPUtils#setBeanProperties(HttpServletRequest, Object)}.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeLister(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof COEtapeSuivanteListViewBean) {
            // le framework a renseigné les champs de type Boolean, on ne le
            // veut pas, on annule donc
            cleanBooleanDefaultValues(request, viewBean);
        }

        return viewBean;
    }

    private String getDestConfirmationMutation() {
        FWAction action = FWAction.newInstance("aquila.process.processMuterDelai.afficher");

        return "/" + action.getApplicationPart() + "?userAction=" + action.toString();
    }
}
