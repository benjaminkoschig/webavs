package globaz.apg.servlet;

import ch.globaz.common.mail.CommonFilesUtils;
import ch.globaz.common.process.ProcessMailUtils;
import ch.globaz.common.properties.PropertiesException;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.helpers.droits.APDroitLAPGJointDemandeHelper;
import globaz.apg.properties.APProperties;
import globaz.apg.util.TypePrestation;
import globaz.apg.utils.APGUtils;
import globaz.apg.vb.droits.*;
import globaz.apg.vb.prestation.APPrestationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.prestation.ged.PRGedAffichageDossier;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSendException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette action est appellée par les écrans de recherche du droit. Il s'agit du premier écran qui est affiché lors de
 * l'entrée dans l'application.
 * <p>
 * Comme on a une structure hiérarchique des droits, il faut pouvoir distinguer s'il s'agit d'un droit mat ou d'un droit
 * apg. Pour ce faire, la plupart des actions de cette classe s'attendent à ce que le type de demande (code systeme apg
 * ou mat) soit transmis en parametre de la requete avec le nom 'typeDemande'.
 * </p>
 * 
 * @author vre
 */
public class APLAPGAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final Logger LOG = LoggerFactory.getLogger(APLAPGAction.class);

    private static final String MAIL_SUBJECT_ENVOI_ZIP = "AGP_PANDEMIE_MAIL_SUBJECT_ENVOI_ZIP";
    private static final String MAIL_CORPS_ENVOI_ZIP = "AGP_PANDEMIE_MAIL_CORPS_ENVOI_ZIP";
    private static final String MAIL_CORPS_ENVOI_ZIP_EMPTY = "AGP_PANDEMIE_MAIL_CORPS_ENVOI_ZIP_EMPTY";

    private static final String VERS_ECRAN_RECAPITULATIF_APG = "recapitulatifDroitAPG_de.jsp";
    private static final String VERS_ECRAN_RECAPITULATIF_MAT = "recapitulatifDroitMat_de.jsp";
    private static final String VERS_ECRAN_RECAPITULATIF_PAT = "recapitulatifDroitPat_de.jsp";
    private static final String VERS_ECRAN_RECAPITULATIF_PAN = "recapitulatifDroitPan_de.jsp";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APLAPGAction.
     * 
     * @param servlet
     */
    public APLAPGAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param session
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @throws JadeClassCastException
     * @throws ClassCastException
     * @throws NullPointerException
     * @throws JadeServiceActivatorException
     * @throws JadeServiceLocatorException
     */
    public void actionAfficherDossierGed(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException,
            JadeServiceLocatorException, JadeServiceActivatorException, NullPointerException, ClassCastException,
            JadeClassCastException {

        PRGedAffichageDossier.actionAfficherDossierGed(session, request, response, mainDispatcher, viewBean);

    }

    /**
     * Action custom permettant de rediriger correctement soit vers l'écran d'affichage du droit maternité, soit vers
     * les écrans du droit APG.
     * 
     * @param session        DOCUMENT ME!
     * @param request        DOCUMENT ME!
     * @param response       DOCUMENT ME!
     * @param mainDispatcher DOCUMENT ME!
     * @param viewBean       DOCUMENT ME!
     * @return la destination de forward
     * @throws ServletException DOCUMENT ME!
     * @throws IOException      DOCUMENT ME!
     */
    public String actionAfficherLAPG(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        String destination = FWDefaultServletAction.ERROR_PAGE;
        String genreService = request.getParameter("genreService");
        String selectedId = getSelectedId(request);

        genreService = assureQueGenreServiceEstLa(genreService, selectedId, session, mainDispatcher);
        if (APGUtils.isTypeAllocationPandemie(genreService)) {
            destination = this.getUserActionURL(request, IAPActions.ACTION_SAISIE_CARTE_PAN, FWAction.ACTION_AFFICHER
                    + "&" + getSelectedIdParam(request));
        } else if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(genreService)) {
            destination = this.getUserActionURL(request, IAPActions.ACTION_SAISIE_CARTE_AMAT, FWAction.ACTION_AFFICHER
                    + "&" + getSelectedIdParam(request));
        } else if (IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE.equals(genreService)) {
            destination = this.getUserActionURL(request, IAPActions.ACTION_SAISIE_CARTE_APAT, FWAction.ACTION_AFFICHER
                    + "&" + getSelectedIdParam(request));
        } else {
            destination = this.getUserActionURL(request, IAPActions.ACTION_SAISIE_CARTE_APG, FWAction.ACTION_AFFICHER
                    + "&" + getSelectedIdParam(request));
        }

        return destination;
    }

    /**
     * Redéfinition d'actionChercher permettant de créer un viewBean qui sera utilisé pour l'affichage de données dans
     * la page rc.
     * 
     * @param session        DOCUMENT ME!
     * @param request        DOCUMENT ME!
     * @param response       DOCUMENT ME!
     * @param mainDispatcher DOCUMENT ME!
     * @throws ServletException DOCUMENT ME!
     * @throws IOException      DOCUMENT ME!
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        APDroitLAPGJointDemandeViewBean viewBean = new APDroitLAPGJointDemandeViewBean();

        // Modification Inforom297

        // // Récupération des id passé par l'url
        String noAvs = request.getParameter("noAVS");
        String idTiers = request.getParameter("idTiers");
        if (!JadeStringUtil.isBlankOrZero(noAvs) && !JadeStringUtil.isBlankOrZero(idTiers)) {

            APDroitDTO apDroitDTO = null;
            String idTiersCurrentDTO = "";
            try {
                apDroitDTO = (APDroitDTO) PRSessionDataContainerHelper.getData(session,
                        PRSessionDataContainerHelper.KEY_DROIT_DTO);
                if (apDroitDTO == null) {
                    idTiersCurrentDTO = "";
                } else {
                    idTiersCurrentDTO = apDroitDTO.getIdTiers();
                }

                //

                if (!idTiersCurrentDTO.equals(idTiers)) {

                    apDroitDTO = new APDroitDTO();
                    // On stocke le no avs si présent
                    if (!JadeStringUtil.isBlankOrZero(noAvs)) {
                        apDroitDTO.setNoAVS(noAvs);
                        // on stocke l'idTiers si présent
                        if (!JadeStringUtil.isBlankOrZero(idTiers)) {
                            apDroitDTO.setIdTiers(idTiers);
                        }
                        // Enregistrement du DTO
                        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO,
                                apDroitDTO);
                    } else {
                        // Si on a pas de no avs, on supprime le dto du container pour ne pas le trainer alors qu'on est
                        // sur
                        // un
                        // autre tiers
                        PRSessionDataContainerHelper.removeData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO);
                    }
                }
            } catch (Exception ex) {
                JadeLogger.error(this, ex);
            }

            // Fin modification Inforom297

        }

        viewBean.setTypePrestation(getTypePrestation(session));
        deleguerActionChercher(viewBean, session, request, response, mainDispatcher);
    }

    /**
     * Action custom permettant de ridiriger correctement vers soit l'écran de récapitulatif droit mat soit vers l'écran
     * de recapitulatif droit apg.
     * 
     * @param session        DOCUMENT ME!
     * @param request        DOCUMENT ME!
     * @param response       DOCUMENT ME!
     * @param mainDispatcher DOCUMENT ME!
     * @param viewBean       DOCUMENT ME!
     * @return soit l'écran de récapitulatif droit mat soit l'écran de recapitulatif droit apg.
     * @throws ServletException DOCUMENT ME!
     * @throws IOException      DOCUMENT ME!
     */
    public String actionRecapitulatif(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        String genreService = request.getParameter("genreService");
        String selectedId = getSelectedId(request);

        // ACK : LGA avril 2013 : Si l'utilisateur est déjà allé sur l'écran de recap des APG
        // et qu'une erreur est apparue lors de la simulation du paiement, s'il fait back ou revient
        // sur l'écran de recap des APG, une popup d'erreur va s'afficher pour lui re-afficher l'erreur survenue
        // précédemment lors de la simulation du paiement....
        if (viewBean != null) {
            viewBean.setMsgType(null);
            viewBean.setMessage(null);
        }

        genreService = assureQueGenreServiceEstLa(genreService, selectedId, session, mainDispatcher);

        String destination = getRelativeURLwithoutClassPart(request, session);
        FWViewBeanInterface recViewBean = null;
        FWAction action = null;

        // Faut-il afficher le bouton 'Simuler paiement avec BPID'
        boolean afficherBouton = false;
        try {
            afficherBouton = APProperties.AFFICHER_BOUTON_SIMULER_PAIEMENT_AVEC_BPID.getBooleanValue();
        } catch (Exception exception) {
            /**
             * Pas grave si on catch l'exception car cette propriété est utilisée uniquement dans le cas de caisse qui
             * ont eu une reprise incomplète de données. Dans ce cas on affiche pas le bouton
             */
        }

        if (APGUtils.isTypeAllocationPandemie(genreService)) {
            destination += APLAPGAction.VERS_ECRAN_RECAPITULATIF_PAN;
            action = FWAction.newInstance(IAPActions.ACTION_RECAPITUALATIF_DROIT_PAN + "." + FWAction.ACTION_AFFICHER);
            recViewBean = new APRecapitulatifDroitPanViewBean();
            ((APRecapitulatifDroitPanViewBean) recViewBean).setIdDroit(selectedId);

            ((APRecapitulatifDroitPanViewBean) recViewBean).setAfficherBoutonSimulerPmtBPID(afficherBouton);
            // dispatch
            this.saveViewBean(recViewBean, session);
            // action.setRight(FWSecureConstants.ADD);
            mainDispatcher.dispatch(recViewBean, action);

            APDroitDTO dto = new APDroitDTO();
            dto.setDateDebutDroit(((APRecapitulatifDroitPanViewBean) recViewBean).getDateDebutDroit());
            dto.setGenreService(((APRecapitulatifDroitPanViewBean) recViewBean).getGenreService());
            dto.setIdDroit(selectedId);
            dto.setNoAVS(((APRecapitulatifDroitPanViewBean) recViewBean).getNoAVS());
            dto.setNomPrenom(((APRecapitulatifDroitPanViewBean) recViewBean).getNomPrenom());

            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, dto);
        } else if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(genreService)) {
            destination += APLAPGAction.VERS_ECRAN_RECAPITULATIF_MAT;
            action = FWAction.newInstance(IAPActions.ACTION_RECAPITUALATIF_DROIT_MAT + "." + FWAction.ACTION_AFFICHER);
            recViewBean = new APRecapitulatifDroitMatViewBean();
            ((APRecapitulatifDroitMatViewBean) recViewBean).setIdDroit(selectedId);

            ((APRecapitulatifDroitMatViewBean) recViewBean).setAfficherBoutonSimulerPmtBPID(afficherBouton);
            // dispatch
            this.saveViewBean(recViewBean, session);
            // action.setRight(FWSecureConstants.ADD);
            mainDispatcher.dispatch(recViewBean, action);

            APDroitDTO dto = new APDroitDTO();
            dto.setDateDebutDroit(((APRecapitulatifDroitMatViewBean) recViewBean).getDateDebutDroit());
            dto.setGenreService(((APRecapitulatifDroitMatViewBean) recViewBean).getGenreService());
            dto.setIdDroit(selectedId);
            dto.setNoAVS(((APRecapitulatifDroitMatViewBean) recViewBean).getNoAVS());
            dto.setNomPrenom(((APRecapitulatifDroitMatViewBean) recViewBean).getNomPrenom());

            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, dto);

        } else if (IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE.equals(genreService)) {
            destination += APLAPGAction.VERS_ECRAN_RECAPITULATIF_PAT;
            action = FWAction.newInstance(IAPActions.ACTION_RECAPITULATIF_DROIT_PAT + "." + FWAction.ACTION_AFFICHER);
            recViewBean = new APRecapitulatifDroitPatViewBean();
            ((APRecapitulatifDroitPatViewBean) recViewBean).setIdDroit(selectedId);

            ((APRecapitulatifDroitPatViewBean) recViewBean).setAfficherBoutonSimulerPmtBPID(afficherBouton);
            // dispatch
            this.saveViewBean(recViewBean, session);
            // action.setRight(FWSecureConstants.ADD);
            mainDispatcher.dispatch(recViewBean, action);

            APDroitDTO dto = new APDroitDTO();
            dto.setDateDebutDroit(((APRecapitulatifDroitPatViewBean) recViewBean).getDateDebutDroit());
            dto.setGenreService(((APRecapitulatifDroitPatViewBean) recViewBean).getGenreService());
            dto.setIdDroit(selectedId);
            dto.setNoAVS(((APRecapitulatifDroitPatViewBean) recViewBean).getNoAVS());
            dto.setNomPrenom(((APRecapitulatifDroitPatViewBean) recViewBean).getNomPrenom());

            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, dto);

        } else {
            destination += APLAPGAction.VERS_ECRAN_RECAPITULATIF_APG;
            action = FWAction.newInstance(IAPActions.ACTION_RECAPITUALATIF_DROIT_APG + "." + FWAction.ACTION_AFFICHER);
            recViewBean = new APRecapitulatifDroitAPGViewBean();
            ((APRecapitulatifDroitAPGViewBean) recViewBean).setIdDroit(selectedId);

            ((APRecapitulatifDroitAPGViewBean) recViewBean).setAfficherBoutonSimulerPmtBPID(afficherBouton);
            // dispatch
            this.saveViewBean(recViewBean, session);
            // action.setRight(FWSecureConstants.ADD);
            mainDispatcher.dispatch(recViewBean, action);

            APDroitDTO dto = new APDroitDTO();
            dto.setDateDebutDroit(((APRecapitulatifDroitAPGViewBean) recViewBean).getDateDebutDroit());
            dto.setGenreService(((APRecapitulatifDroitAPGViewBean) recViewBean).getGenreService());
            dto.setIdDroit(selectedId);
            dto.setNoAVS(((APRecapitulatifDroitAPGViewBean) recViewBean).getNoAVS());
            dto.setNomPrenom(((APRecapitulatifDroitAPGViewBean) recViewBean).getNomPrenom());

            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, dto);
        }

        // récupérer le message d'erreur pour le cas ou l'on vient du calcul
        if ((viewBean != null)
                && ((viewBean instanceof APPrestationViewBean) || (viewBean instanceof APDroitLAPGJointDemandeViewBean))
                && FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            recViewBean.setMsgType(viewBean.getMsgType());
            recViewBean.setMessage(viewBean.getMessage());
        }

        return destination;
    }

    private String assureQueGenreServiceEstLa(String genreService, String id, HttpSession session,
            FWDispatcher mainDispatcher) {
        // TODO virer quand on pourra passer des parametres dans la requête avec
        // le menu d'option
        // On s'assure que on a bien le genre de service. Si on ne l'a pas, on
        // essaye de le trouver dans le viewBean
        // en session. Si ce n'est pas possible, on va le rechercher dans la
        // base grace à l'id passé en requête
        String genreServiceARetourner = genreService;

        try {
            if (JadeStringUtil.isIntegerEmpty(genreServiceARetourner)) {
                FWViewBeanInterface viewBeanEnSession = this.loadViewBean(session);

                if ((viewBeanEnSession instanceof APDroitLAPGJointDemandeViewBean)
                        && ((APDroitLAPGJointDemandeViewBean) viewBeanEnSession).getIdDroit().equals(id)) {
                    genreServiceARetourner = ((APDroitLAPGJointDemandeViewBean) viewBeanEnSession).getGenreService();
                } else {
                    // on doit aller le chercher dans la base
                    APDroitLAPG droitLAPG = new APDroitLAPG();
                    droitLAPG.setSession((BSession) mainDispatcher.getSession());
                    droitLAPG.setIdDroit(id);
                    droitLAPG.retrieve();
                    genreServiceARetourner = droitLAPG.getGenreService();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return genreServiceARetourner;
    }

    /**
     * redéfinition de beforeLister permettant d'initialiser le listViewBean avec les données contenues dans la session.
     * 
     * @param session  DOCUMENT ME!
     * @param request  DOCUMENT ME!
     * @param response DOCUMENT ME!
     * @param viewBean DOCUMENT ME!
     * @return super.beforeLister
     * @see globaz.framework.controller.FWDefaultServletAction#beforeLister(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        APDroitLAPGJointDemandeListViewBean listViewBean = (APDroitLAPGJointDemandeListViewBean) viewBean;

        listViewBean.setTypePrestation(getTypePrestation(session));

        APDroitParametresRCDTO dto = new APDroitParametresRCDTO();
        dto.setResponsable(listViewBean.getForIdGestionnaire());
        dto.setEtatDemande(listViewBean.getForEtatDemande());
        dto.setEtatDroit(listViewBean.getForEtatDroit());
        dto.setGenreService(listViewBean.getForGenreServiceListDroit());
        dto.setOrderBy(listViewBean.getOrderBy());

        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_PARAMETRES_RC_DTO, dto);

        return super.beforeLister(session, request, response, viewBean);
    }

    /**
     * Action custom permettant de calculer des prestations rétroactive pour les ACM crée un nouveau droit enfant du
     * droit source en vue d'une correction.
     * 
     * @param session        DOCUMENT ME!
     * @param request        DOCUMENT ME!
     * @param response       DOCUMENT ME!
     * @param mainDispatcher DOCUMENT ME!
     * @param viewBean       DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws ServletException DOCUMENT ME!
     * @throws IOException      DOCUMENT ME!
     */
    public String calculerACM(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        // tout se fait dans le helper
        return deleguerAHelper(session, request, response, mainDispatcher, viewBean);
    }

    /**
     * Action custom permettant de calculer des prestations rétroactive pour le droits à la maternite cantonale crée un
     * nouveau droit enfant du droit source en vue d'une correction.
     * 
     * @param session        DOCUMENT ME!
     * @param request        DOCUMENT ME!
     * @param response       DOCUMENT ME!
     * @param mainDispatcher DOCUMENT ME!
     * @param viewBean       DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws ServletException DOCUMENT ME!
     * @throws IOException      DOCUMENT ME!
     */

    public String calculerDroitMaterniteCantonale(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {
        // tout se fait dans le helper
        return deleguerAHelper(session, request, response, mainDispatcher, viewBean);
    }

    /**
     * Action custom permettant de copier complètement un droit. Au contraire de corrigerDroit, cette méthode crée une
     * copie complète et autonome d'un droit.
     * 
     * @param session        DOCUMENT ME!
     * @param request        DOCUMENT ME!
     * @param response       DOCUMENT ME!
     * @param mainDispatcher DOCUMENT ME!
     * @param viewBean       DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws ServletException DOCUMENT ME!
     * @throws IOException      DOCUMENT ME!
     */
    public String copierDroit(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        String destination = FWDefaultServletAction.ERROR_PAGE;
        String genreService = request.getParameter("genreService");
        String selectedId = getSelectedId(request);

        genreService = assureQueGenreServiceEstLa(genreService, selectedId, session, mainDispatcher);
        if (APGUtils.isTypeAllocationPandemie(genreService)) {
            destination = this.getUserActionURL(request, IAPActions.ACTION_SAISIE_CARTE_PAN, FWAction.ACTION_AFFICHER
                    + "&" + getSelectedIdParam(request));
        } else if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(genreService)) {
            destination = this.getUserActionURL(request, IAPActions.ACTION_SAISIE_CARTE_AMAT, FWAction.ACTION_AFFICHER
                    + "&" + getSelectedIdParam(request));
        } else if (IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE.equals(genreService)) {
            destination = this.getUserActionURL(request, IAPActions.ACTION_SAISIE_CARTE_APAT, FWAction.ACTION_AFFICHER
                    + "&" + getSelectedIdParam(request));
        } else {
            destination = this.getUserActionURL(request, IAPActions.ACTION_SAISIE_CARTE_APG, FWAction.ACTION_AFFICHER
                    + "&" + getSelectedIdParam(request));
        }

        String temp = deleguerAHelper(session, request, response, mainDispatcher, viewBean);

        // mise à jour de l'id du droit selectionne
        destination = destination.substring(0, destination.length() - getSelectedId(request).length()) + temp;

        // tout se fait dans le helper
        return destination;
    }

    /**
     * Action custom permettant de copier un droit en vue d'une correction. Au contraire de copierDroit, cette méthode
     * crée un nouveau droit enfant du droit source en vue d'une correction.
     * 
     * @param session        DOCUMENT ME!
     * @param request        DOCUMENT ME!
     * @param response       DOCUMENT ME!
     * @param mainDispatcher DOCUMENT ME!
     * @param viewBean       DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws ServletException DOCUMENT ME!
     * @throws IOException      DOCUMENT ME!
     */
    public String corrigerDroit(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        clearEtatDroit(session);
        // tout se fait dans le helper
        return deleguerAHelper(session, request, response, mainDispatcher, viewBean);
    }

    /**
     * Méthode permettant de nettoyer l'état de droit dans la recherche des droits.
     * @param session
     */
    private void clearEtatDroit(HttpSession session) {
        // Dans le cadre d'une correction de droit, on réinitialise l'état du droit dans la recherche.
        APDroitParametresRCDTO dto = (APDroitParametresRCDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_DROIT_PARAMETRES_RC_DTO);
        dto.setEtatDroit("");
    }

    // delegue simplement au helper et retourne la page RC
    private String deleguerAHelper(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        APDroitLAPGJointDemandeViewBean dlViewBean = new APDroitLAPGJointDemandeViewBean();
        String retValue = null;

        try {
            FWAction action = FWAction.newInstance(getUserAction(request));

            // action.setRight(FWSecureConstants.ADD);
            initViewBean(dlViewBean, session, request, mainDispatcher);
            FWViewBeanInterface retour = null;
            retour = mainDispatcher.dispatch(dlViewBean, action);

            if (dlViewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                this.saveViewBean(dlViewBean, session);

                return this.getUserActionURL(request, "apg.droits.droitLAPGJointDemande.actionRecapitulatif");
            }

            APDroitAPGDTO dto = dlViewBean.getDto();
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, dto);

            // pour donner l'id du nouveau droit dans le cas d'une copie
            if ((retour != null) && (retour instanceof APDroitLAPGJointDemandeViewBean)
                    && APDroitLAPGJointDemandeHelper.COPIER_DROIT.equals(action.getActionPart())) {
                return ((APDroitLAPGJointDemandeViewBean) retour).getIdDroit();
            } else {
                return this.getUserActionURL(request, IAPActions.ACTION_DROIT_LAPG, FWAction.ACTION_CHERCHER);
            }
        } catch (Exception e) {
            // si le viewBean stocké dans la session n'est pas du type
            // APDroitAPGViewBean
            retValue = FWDefaultServletAction.ERROR_PAGE;
        }

        return retValue;
    }

    /**
     * retourne le type de prestation sélectionné pour cette action.
     * 
     * @param session DOCUMENT ME!
     * @return la valeur courante de l'attribut type prestation
     */
    protected TypePrestation getTypePrestation(HttpSession session) {
        String csTypePrestation = (String) PRSessionDataContainerHelper.getData(session,
                PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION);

        return TypePrestation.typePrestationInstanceForCS(csTypePrestation);
    }

    // initialise le viewBean au moyen des données de la requetes et de la
    // session
    private void initViewBean(APDroitLAPGJointDemandeViewBean viewBean, HttpSession session,
            HttpServletRequest request, FWDispatcher mainDispatcher) {
        if (JadeStringUtil.isIntegerEmpty(viewBean.getIdDroit())) {
            viewBean.setIdDroit(getSelectedId(request));
        }

        // genreService
        String genreService = request.getParameter("genreService");
        genreService = assureQueGenreServiceEstLa(genreService, viewBean.getIdDroit(), session, mainDispatcher);
        viewBean.setGenreService(genreService);
        viewBean.setTypePrestation(getTypePrestation(session));
    }

    /**
     * Action custom permettant de restituer les prestations versées dans le cadre d'un droit. Cette méthode annule
     * toutes les prestations d'un droit en créant une prestation d'un montant exactement opposé.
     * 
     * @param session        DOCUMENT ME!
     * @param request        DOCUMENT ME!
     * @param response       DOCUMENT ME!
     * @param mainDispatcher DOCUMENT ME!
     * @param viewBean       DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws ServletException DOCUMENT ME!
     * @throws IOException      DOCUMENT ME!
     */
    public String restituerDroit(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        // Dans le cadre d'une restitution de droit, on réinitialise l'état du droit dans la recherche.
        clearEtatDroit(session);
        return deleguerAHelper(session, request, response, mainDispatcher, viewBean);
    }

    /**
     * Action custom permettant de simuler le paiement d'un droit. Est utilisé pour récupéré des anciens cas de l'AS400
     * qui doivent être restitué. Pré requis : Saisir l'ancien cas dans le nouveau système.
     * 
     * @param session          DOCUMENT ME!
     * @param request          DOCUMENT ME!
     * @param response         DOCUMENT ME!
     * @param mainDispatcher   DOCUMENT ME!
     * @param originalViewBean DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws ServletException DOCUMENT ME!
     * @throws IOException      DOCUMENT ME!
     */
    public String simulerPaiementDroit(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface originalViewBean) throws ServletException, IOException {

        String destination = null;

        try {
            APDroitLAPGJointDemandeViewBean viewBean = new APDroitLAPGJointDemandeViewBean();

            // On peut venir depuis les APG ou la mat donc il faut contrôller le type du VB
            if (originalViewBean instanceof APRecapitulatifDroitAPGViewBean) {
                viewBean.setPidAnnonce(((APRecapitulatifDroitAPGViewBean) originalViewBean).getPidAnnonce());
            } else if (originalViewBean instanceof APRecapitulatifDroitMatViewBean) {
                viewBean.setPidAnnonce(((APRecapitulatifDroitMatViewBean) originalViewBean).getPidAnnonce());
            } else {
                throw new Exception("Le viewBean est de type inconnu. Impossible de continuer l'action");
            }
            viewBean.setISession(mainDispatcher.getSession());
            (viewBean).setIdDroit(request.getParameter("selectedId"));

            viewBean = (APDroitLAPGJointDemandeViewBean) mainDispatcher.dispatch(viewBean, getAction());
            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            if (goesToSuccessDest) {
                destination = _getDestModifierSucces(session, request, response, viewBean);
            } else {
                destination = FWDefaultServletAction.ERROR_PAGE;
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;

        }

        return destination;
    }

    public void actionEnvoyerMail(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                  FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        String numAvs = request.getParameter("noAVS");
        BSession bSession = (BSession) mainDispatcher.getSession();
        FWAction newAction = null;

        String method = request.getParameter("_method");

        String filesPathStorage = "";
        try {


            if (viewBean instanceof APDroitPanViewBean) {
                newAction = FWAction.newInstance(IAPActions.ACTION_DROIT_LAPG + ".actionAfficherLAPG");
            } else {
                newAction = FWAction.newInstance(IAPActions.ACTION_DROIT_LAPG + ".chercher");
            }

            filesPathStorage = APProperties.STORAGE_APG_PANDEMIE_FOLDER.getValue();

            filesPathStorage = CommonFilesUtils.readPathFiles(numAvs, filesPathStorage);

            sendEmail(bSession, numAvs, filesPathStorage);

            String destination = request.getServletPath() + "?" + PRDefaultAction.USER_ACTION + "=" + newAction;
            goSendRedirect(destination, request, response);
        } catch (PropertiesException e) {
            servlet.getServletContext().getRequestDispatcher("/errorPage.jsp").forward(request, response);
        }
    }

    private void sendEmail(BSession session, String numAVS, String filesPath) throws MailSendException, IOException {
        List<String> filesToJoin = CommonFilesUtils.selectFilesToJoin(filesPath, numAVS);
        StringBuilder corps = new StringBuilder();
        if (!filesToJoin.isEmpty()) {
            corps.append(session.getLabel(MAIL_CORPS_ENVOI_ZIP));
        } else {
            corps.append(session.getLabel(MAIL_CORPS_ENVOI_ZIP_EMPTY));
        }
        List<String> emailsAdresses = new ArrayList<>();
        emailsAdresses.add(getEMailAddress(session));
        try {
            ProcessMailUtils.sendMail(emailsAdresses, getEMailObject(session, numAVS), corps.toString(), filesToJoin);
        } catch (Exception e) {
            throw new MailSendException("Erreur à l'envoi de l'e-mail", e);
        }
    }

    public final String getEMailAddress(BSession session) {
        String eMailAddress = "";
        try {
            if (session != null) {
                eMailAddress = session.getUserEMail();
            }
            if (JadeStringUtil.isBlankOrZero(eMailAddress)) {
                eMailAddress = APProperties.EMAIL_APG_PANDEMIE.getValue();
            }
        } catch (PropertiesException e) {
            LOG.error("Erreur à la récupération de la propriété Adresse E-mail", e);
        }
        return eMailAddress;
    }

    protected String getEMailObject(BSession session, String numAVS) {
        return session.getLabel(MAIL_SUBJECT_ENVOI_ZIP) + ": " + numAVS;
    }

    public String actionAttenteReponse(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                     FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        clearEtatDroit(session);
        return deleguerAHelper(session, request, response, mainDispatcher, viewBean);
    }

    public String actionRefuser(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                       FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        clearEtatDroit(session);
        return deleguerAHelper(session, request, response, mainDispatcher, viewBean);
    }

}
