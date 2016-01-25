/*
 * Créé le 28 sept. 05
 */
package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.fweb.taglib.FWSelectorTag;
import globaz.globall.http.JSPUtils;
import globaz.ij.vb.prononces.IJSituationProfessionnelleViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
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
public class IJSituationProfessionnelleAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String VERS_ECRAN_DE_ADD = "_de.jsp?" + METHOD_ADD;
    private static final String VERS_ECRAN_DE_NEW = "_de.jsp?" + VALID_NEW;
    private static final String VERS_ECRAN_DE_UPD = "_de.jsp?" + METHOD_UPD;
    private static final String VERS_ECRAN_RC = "_rc.jsp";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJSituationProfessionnelleAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public IJSituationProfessionnelleAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * par défaut le framework redirige vers une action .chercher après un ajout. Nous préférons que la page soit
     * directement redirigée vers la page _de.jsp.
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
        saveNewViewBean(session, request);

        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        saveNewViewBean(session, request);

        return super._getDestModifierSucces(session, request, response, viewBean);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        saveNewViewBean(session, request);

        return super._getDestSupprimerSucces(session, request, response, viewBean);
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

        if (isRetourDepuisPyxis(viewBean)) {
            // on revient depuis pyxis on se contente de forwarder car le bon
            // viewBean est deja en session
            ((IJSituationProfessionnelleViewBean) viewBean).setRetourDepuisPyxis(false);

            ((IJSituationProfessionnelleViewBean) viewBean).setRetourDesTiers(true);

            if (((IJSituationProfessionnelleViewBean) viewBean).isNew()) {
                forward(getRelativeURL(request, session) + VERS_ECRAN_DE_ADD, request, response);
            } else {
                forward(getRelativeURL(request, session) + VERS_ECRAN_DE_UPD, request, response);
            }
        } else if (viewBean instanceof IJSituationProfessionnelleViewBean
                && ((IJSituationProfessionnelleViewBean) viewBean).isActionRechercherAffilie()) {
            ((IJSituationProfessionnelleViewBean) viewBean).setActionRechercherAffilie(false);

            if (((IJSituationProfessionnelleViewBean) viewBean).isNew()) {
                forward(getRelativeURL(request, session) + VERS_ECRAN_DE_ADD, request, response);
            } else {
                forward(getRelativeURL(request, session) + VERS_ECRAN_DE_UPD, request, response);
            }
        } else {
            // on ne revient pas depuis pyxis, comportement par defaut
            super.actionAfficher(session, request, response, mainDispatcher);
        }
    }

    /**
     * redefinition pour charger les informations qui doivent s'afficher dans l'ecran rc.
     * 
     * <p>
     * cette methode inspecte la session pour savoir si l'on revient depuis pyxis. Si c'est le cas, l'ancien viewBean
     * est conserve dans la session et une propriete du viewBean est renseignee avec une action qui permettra de
     * rafficher le bon ecran de detail pour la repartition de paiement.
     * </p>
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
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWViewBeanInterface viewBean = loadViewBean(session);
        IJSituationProfessionnelleViewBean spViewBean;

        if (isRetourDepuisPyxis(viewBean)) {
            /*
             * on revient depuis pyxis
             */
            spViewBean = (IJSituationProfessionnelleViewBean) viewBean;
            ((IJSituationProfessionnelleViewBean) viewBean).setRetourDesTiers(true);
        } else {
            /*
             * on affiche cette page par un chemin habituel, dans ce cas tout est normal, mise a part que l'on met un
             * viewBean dans la session qui contient les donnees qui doivent s'afficher dans le cadre rc de la ca page.
             */
            spViewBean = new IJSituationProfessionnelleViewBean();

            try {
                JSPUtils.setBeanProperties(request, spViewBean);
            } catch (Exception e) {
                spViewBean.setMessage(e.getMessage());
                spViewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        // on appelle le helper qui va charger les infos concernante les
        // employeurs
        recupererInfosPrononce(request, spViewBean);
        mainDispatcher.dispatch(spViewBean, getAction());

        /*
         * on sauve de toutes facons le viewBean dans la requete meme s'il est deja en session car c'est la que la page
         * rc va le rechercher.
         */
        saveViewBean(spViewBean, request);
        forward(getRelativeURL(request, session) + VERS_ECRAN_RC, request, response);
    }

    /**
     * ecrase une des valeurs sauvee dans la session par FWSelectorTag de telle sorte que l'on sache exactement quelle
     * action sera executee lorsque l'on revient de pyxis et avec quels parametres.
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
     * 
     * @see FWSelectorTag
     */
    @Override
    protected void actionSelectionner(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // recuperer les id et genre de service du droit pour le retour depuis
        // pyxis
        IJSituationProfessionnelleViewBean spViewBean = (IJSituationProfessionnelleViewBean) loadViewBean(session);
        StringBuffer queryString = new StringBuffer();

        queryString.append(USER_ACTION);
        queryString.append("=");
        queryString.append(IIJActions.ACTION_SITUATION_PROFESSIONNELLE);
        queryString.append(".");
        queryString.append(FWAction.ACTION_CHERCHER);
        queryString.append("&noAVS=");
        queryString.append(spViewBean.getNoAVS());
        queryString.append("&prenomNom=");
        queryString.append(spViewBean.getPrenomNom());
        queryString.append("&dateDebutPrononce=");
        queryString.append(spViewBean.getDateDebutPrononce());
        queryString.append("&idPrononce=");
        queryString.append(spViewBean.getIdPrononce());
        queryString.append("&forIdPrononce=");
        queryString.append(spViewBean.getIdPrononce());
        queryString.append("&csTypeIJ=");
        queryString.append(spViewBean.getCsTypeIJ());
        queryString.append("&detailRequerant=");
        queryString.append(request.getParameter("detailRequerant"));

        // HACK: on remplace une des valeurs sauvee en session par FWSelectorTag
        session.setAttribute(FWDefaultServletAction.ATTRIBUT_SELECTOR_CUSTOMERURL, queryString.toString());

        // comportement par defaut
        super.actionSelectionner(session, request, response, mainDispatcher);
    }

    /**
     * renseigne le dto dans le viewBean (necessaire pour savoir si le droit est modfiable).
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
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        recupererInfosPrononce(request, (IJSituationProfessionnelleViewBean) viewBean);

        return viewBean;
    }

    /**
     * inspecte le viewBean et retourne vrai si celui-ci indique que l'on revient de pyxis.
     * 
     * @param viewBean
     * 
     * @return
     */
    private boolean isRetourDepuisPyxis(FWViewBeanInterface viewBean) {
        return ((viewBean != null) && (viewBean instanceof IJSituationProfessionnelleViewBean) && ((IJSituationProfessionnelleViewBean) viewBean)
                .isRetourDepuisPyxis());
    }

    /**
     * DOCUMENT ME ! --> hpe
     * 
     * @see FWSelectorTag
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
    public void rechercherAffilie(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        IJSituationProfessionnelleViewBean spViewBean = null;
        try {
            JSPUtils.setBeanProperties(request, viewBean);

            spViewBean = (IJSituationProfessionnelleViewBean) viewBean;

            // on appelle le helper qui va charger les infos concernante les
            // employeurs

            mainDispatcher.dispatch(spViewBean, getAction());
            // setDTO(session, spViewBean);
            spViewBean.setActionRechercherAffilie(true);
            /*
             * on sauve de toutes facons le viewBean dans la requete meme s'il est deja en session car c'est la que la
             * page rc va le rechercher.
             */
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        } finally {
            saveViewBean(spViewBean, session);
            saveViewBean(spViewBean, request);

            forward(getRelativeURL(request, session) + VERS_ECRAN_RC, request, response);
        }
    }

    /**
     * renseigne le viewBean avec les infos du prononce qui trainent de requete en requete.
     * 
     * @param request
     * @param viewBean
     */
    private void recupererInfosPrononce(HttpServletRequest request, IJSituationProfessionnelleViewBean viewBean) {
        viewBean.setNoAVS(request.getParameter("noAVS"));
        viewBean.setPrenomNom(request.getParameter("prenomNom"));
        viewBean.setDateDebutPrononce(request.getParameter("dateDebutPrononce"));

        if (JadeStringUtil.isIntegerEmpty(viewBean.getIdPrononce())) {
            if (JadeStringUtil.isEmpty(request.getParameter("forIdPrononce"))) {
                viewBean.setIdPrononce(request.getParameter("idPrononce"));
            } else {
                viewBean.setIdPrononce(request.getParameter("forIdPrononce"));
            }
        }

        viewBean.setCsTypeIJ(request.getParameter("csTypeIJ"));
    }

    /**
     * remplace le viewBean actuellement en session par un nouveau.
     * 
     * <p>
     * cette action est parfois necessaire pour eviter par exemple qu'un enregistrement soit efface deux fois.
     * </p>
     * 
     * @param session
     * @param request
     */
    private void saveNewViewBean(HttpSession session, HttpServletRequest request) {
        IJSituationProfessionnelleViewBean viewBean = new IJSituationProfessionnelleViewBean();

        viewBean.setNoAVS(request.getParameter("noAVS"));
        viewBean.setPrenomNom(request.getParameter("prenomNom"));
        viewBean.setDateDebutPrononce(request.getParameter("dateDebutPrononce"));
        viewBean.setIdPrononce(request.getParameter("idPrononce"));
        viewBean.setCsTypeIJ(request.getParameter("csTypeIJ"));

        saveViewBean(viewBean, session);
    }
}
