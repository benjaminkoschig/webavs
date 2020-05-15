/*
 * Créé le 23 mai 05
 */
package globaz.apg.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.db.droits.APEmployeur;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.apg.groupdoc.ccju.GroupdocPropagateUtil;
import globaz.apg.properties.APProperties;
import globaz.apg.util.TypePrestation;
import globaz.apg.vb.droits.APDroitAPGDTO;
import globaz.apg.vb.droits.APDroitDTO;
import globaz.apg.vb.droits.APSituationProfessionnelleViewBean;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.fweb.taglib.FWSelectorTag;
import globaz.globall.db.BManager;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;

/**
 * <H1>Description</H1>
 *
 * @author dvh
 */
public class APSituationProfessionnelleAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static FWAction ACTION_PRE_CHERCHER;
    private static final String VERS_ECRAN_DE_ADD = "_de.jsp?" + METHOD_ADD;
    private static final String VERS_ECRAN_DE_UPD = "_de.jsp?" + METHOD_UPD;

    private static final String VERS_ECRAN_RC = "_rc.jsp";

    static {
        ACTION_PRE_CHERCHER = FWAction.newInstance(IAPActions.ACTION_REPARTITION_PAIEMENTS + ".actionPreparerChercher");
        // ACTION_PRE_CHERCHER.setRight(FWSecureConstants.ADD);
    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APSituationProfessionnelleAction.
     *
     * @param servlet
     *            DOCUMENT ME!
     */
    public APSituationProfessionnelleAction(FWServlet servlet) {
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
        saveNewViewBean(session, (APSituationProfessionnelleViewBean) viewBean);

        return super._getDestAjouterSucces(session, request, response, viewBean);
        // return getRelativeURL(request, session) + VERS_ECRAN_DE_NEW + "&" +
        // METHOD_ADD
        // +"&userAction=apg.droits.situationProfessionnelle.chercher";
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        saveNewViewBean(session, (APSituationProfessionnelleViewBean) viewBean);

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
        saveNewViewBean(session, (APSituationProfessionnelleViewBean) viewBean);

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
            ((APSituationProfessionnelleViewBean) viewBean).setRetourDepuisPyxis(false);
            ((APSituationProfessionnelleViewBean) viewBean).setRetourDepuisAdresse(false);

            ((APSituationProfessionnelleViewBean) viewBean).setRetourDesTiers(true);

            if (((APSituationProfessionnelleViewBean) viewBean).isNew()) {
                forward(getRelativeURL(request, session) + VERS_ECRAN_DE_ADD, request, response);
            } else {
                forward(getRelativeURL(request, session) + VERS_ECRAN_DE_UPD, request, response);
            }
        } else if (viewBean instanceof APSituationProfessionnelleViewBean
                && ((APSituationProfessionnelleViewBean) viewBean).isActionRechercherAffilie()) {
            ((APSituationProfessionnelleViewBean) viewBean).setActionRechercherAffilie(false);
            ((APSituationProfessionnelleViewBean) viewBean).setIsRetourRechercheAffilie(true);

            if (((APSituationProfessionnelleViewBean) viewBean).isNew()) {
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
        APSituationProfessionnelleViewBean spViewBean;

        if (isRetourDepuisPyxis(viewBean)) {
            /*
             * on revient depuis pyxis
             */
            spViewBean = (APSituationProfessionnelleViewBean) viewBean;
            ((APSituationProfessionnelleViewBean) viewBean).setRetourDesTiers(true);
        } else {
            /*
             * on affiche cette page par un chemin habituel, dans ce cas tout est normal, mise a part que l'on met un
             * viewBean dans la session qui contient les donnees qui doivent s'afficher dans le cadre rc de la ca page.
             */
            spViewBean = new APSituationProfessionnelleViewBean();

            try {
                JSPUtils.setBeanProperties(request, spViewBean);
            } catch (Exception e) {
                spViewBean.setMessage(e.getMessage());
                spViewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        // on appelle le helper qui va charger les infos concernante les
        // employeurs
        mainDispatcher.dispatch(spViewBean, getAction());

        // saveNewViewBean(session, spViewBean);
        setDTO(session, spViewBean);

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
    @Override
    protected void actionSelectionner(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // recuperer les id et genre de service du droit pour le retour depuis
        // pyxis
        APSituationProfessionnelleViewBean spViewBean = (APSituationProfessionnelleViewBean) loadViewBean(session);
        StringBuffer queryString = new StringBuffer();

        queryString.append(USER_ACTION);
        queryString.append("=");
        queryString.append(IAPActions.ACTION_SITUATION_PROFESSIONNELLE);
        queryString.append(".");
        queryString.append(FWAction.ACTION_CHERCHER);
        queryString.append("&idDroitLAPGBackup=");
        queryString.append(spViewBean.getIdDroit());

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
        setDTO(session, (APSituationProfessionnelleViewBean) viewBean);

        return viewBean;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.framework.controller.IFWActionHandler#doAction(javax.servlet.http .HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWController)
     */
    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        // TODO Raccord de méthode auto-généré
        super.doAction(session, request, response, mainController);
    }

    /**
     * CUSTOM action.
     *
     * @param session
     *            the HTTP session
     * @param request
     *            the HTTP request
     * @param response
     *            the HTTP response
     * @param mainController
     *            the main controller
     * @param action
     *            the action
     * @exception Exception
     *                if an error occurred
     */
    public String ecranSuivant(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher, FWViewBeanInterface viewBean) throws Exception {

        String idDroit = request.getParameter(APAbstractDroitDTOAction.PARAM_ID_DROIT);
        try {
            APSituationProfessionnelleViewBean spViewBean = (APSituationProfessionnelleViewBean) viewBean;

            // Fait pour les anciens situations professionnelles, car on ne passe pas forcement par
            // l ecran pour remplir les nouveaux deux champs id domaine paiement et id tiers paiement.
            updateAdresseSituationProfessionnelle(idDroit, spViewBean);

            // check if propagation have to be called
            if (GroupdocPropagateUtil.shouldPropagate()) {
                APSituationProfessionnelleManager manager = new APSituationProfessionnelleManager();
                manager.setSession(spViewBean.getSession());
                manager.setForIdDroit(idDroit);
                if (manager.getCount() == 0) {
                    // on cree un dossier 000.0000-xxx.xx.xxx.xxx-A07 car c'est
                    // un étudiant (pas de situation professionnelle)
                    APDroitLAPG droit = new APDroitLAPG();
                    droit.setSession(spViewBean.getSession());
                    droit.setIdDroit(idDroit);
                    droit.retrieve();
                    if (!JadeStringUtil.isBlank(droit.getIdDemande())) {
                        PRDemande demande = droit.loadDemande();
                        if (!JadeStringUtil.isBlank(demande.getIdTiers())) {
                            PRTiersWrapper allocataire = demande.loadTiers();
                            if (allocataire != null) {
                                GroupdocPropagateUtil.propagateData(null, allocataire, droit.getDateReception());
                            } else {
                                if (GroupdocPropagateUtil.isVerbose()) {
                                    JadeLogger.warn(this, "Allocataire(" + demande.getIdTiers() + ") for Demande("
                                            + demande.getId() + ") not found");
                                }
                            }
                        } else {
                            if (GroupdocPropagateUtil.isVerbose()) {
                                JadeLogger.warn(this, "Demande(" + droit.getIdDemande() + ") for Droit(" + droit.getId()
                                        + ") not found");
                            }
                        }
                    } else {
                        if (GroupdocPropagateUtil.isVerbose()) {
                            JadeLogger.warn(this, "Droit(" + idDroit + ") not found");
                        }
                    }
                }
            }
            return "/apg?userAction=" + spViewBean.getDestEcranSuivant() + "&" + APAbstractDroitDTOAction.PARAM_ID_DROIT
                    + "=" + idDroit;
        } catch (ClassCastException e) {
            // fack pour le retour de la creation de la sit. fam. APG
            return "/apg?userAction=" + IAPActions.ACTION_ENFANT_APG + ".chercher&"
                    + APAbstractDroitDTOAction.PARAM_ID_DROIT + "=" + idDroit;
        }
    }

    private void updateAdresseSituationProfessionnelle(String idDroit, APSituationProfessionnelleViewBean spViewBean)
            throws Exception {

        final APSituationProfessionnelleManager managerSituation = new APSituationProfessionnelleManager();
        managerSituation.setSession(spViewBean.getSession());
        managerSituation.setForIdDroit(idDroit);
        managerSituation.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < managerSituation.getSize(); i++) {
            final APSituationProfessionnelle situation = (APSituationProfessionnelle) managerSituation.get(i);
            if (situation.getIsVersementEmployeur()
                    && (JadeStringUtil.isBlankOrZero(situation.getIdDomainePaiementEmployeur())
                            || JadeStringUtil.isBlankOrZero(situation.getIdTiersPaiementEmployeur()))) {

                final APEmployeur loadEmployeur = situation.loadEmployeur();
                final String idTiersPaiementEmployeur = loadEmployeur.getIdTiers();
                final String idDomainPaiementEmployeur;
                if (IPRDemande.CS_TYPE_MATERNITE.equals(spViewBean.getTypePrestation().toCodeSysteme())) {
                    idDomainPaiementEmployeur = IPRConstantesExternes.TIERS_CS_DOMAINE_MATERNITE;
                } else if (IPRDemande.CS_TYPE_PANDEMIE.equals(spViewBean.getTypePrestation().toCodeSysteme())) {
                    idDomainPaiementEmployeur = APProperties.DOMAINE_ADRESSE_APG_PANDEMIE.getValue();
                } else {
                    idDomainPaiementEmployeur = IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_APG;
                }

                // nous recherchons en cascade du domaine APG ou MATERNITE
                final TIAdressePaiementData detailTiers = PRTiersHelper.getAdressePaiementData(spViewBean.getSession(),
                        spViewBean.getSession().getCurrentThreadTransaction(), idTiersPaiementEmployeur,
                        idDomainPaiementEmployeur, loadEmployeur.getIdAffilie(), JACalendar.todayJJsMMsAAAA());

                if (detailTiers != null && !detailTiers.isNew()) {
                    situation.setIdDomainePaiementEmployeur(detailTiers.getIdApplication());
                    situation.setIdTiersPaiementEmployeur(detailTiers.getIdTiers());
                    situation.setSession(spViewBean.getSession());
                    situation.wantCallValidate(false);
                    situation.update();
                }
            }
        }
    }

    /**
     * inspecte le viewBean et retourne vrai si celui-ci indique que l'on revient de pyxis.
     *
     * @param viewBean
     *
     * @return
     */
    private boolean isRetourDepuisPyxis(FWViewBeanInterface viewBean) {
        return viewBean != null && (viewBean instanceof APSituationProfessionnelleViewBean)
                && (((APSituationProfessionnelleViewBean) viewBean).isRetourDepuisPyxis()
                        || ((APSituationProfessionnelleViewBean) viewBean).isRetourDepuisAdresse());
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

        APSituationProfessionnelleViewBean spViewBean = null;
        try {
            JSPUtils.setBeanProperties(request, viewBean);

            spViewBean = (APSituationProfessionnelleViewBean) viewBean;

            // on sauvegarde les date de debut et date de fin de la situation
            // prof.
            // car ces date vont etre effacees pas les dates de debut et de fin
            // d'affiliation
            String dateDebut = spViewBean.getDateDebut();
            String dateFin = spViewBean.getDateFin();

            // on appelle le helper qui va charger les infos concernante les
            // employeurs
            // getAction().setRight(FWSecureConstants.READ);
            mainDispatcher.dispatch(spViewBean, getAction());
            setDTO(session, spViewBean);
            spViewBean.setActionRechercherAffilie(true);

            // on permute les date sit. prof. et date affiliation, et on
            // restaure les dates sit. prof.
            spViewBean.setDateDebutAffiliation(spViewBean.getDateDebut());
            spViewBean.setDateFinAffiliation(spViewBean.getDateFin());
            spViewBean.setDateDebut(dateDebut);
            spViewBean.setDateFin(dateFin);

            spViewBean.setAdressePaiementEmployeur(null);

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
     * remplace le viewBean actuellement en session par un nouveau.
     *
     * <p>
     * cette action est parfois necessaire pour eviter par exemple qu'un enregistrement soit efface deux fois.
     * </p>
     *
     * @param session
     * @param oldViewBean
     */
    private void saveNewViewBean(HttpSession session, APSituationProfessionnelleViewBean oldViewBean) {
        APSituationProfessionnelleViewBean viewBean = new APSituationProfessionnelleViewBean();

        viewBean.setDroitDTO(oldViewBean.getDroitDTO());
        viewBean.setIdDroitLAPGBackup(oldViewBean.getIdDroitLAPGBackup());
        viewBean.setSession(oldViewBean.getSession());

        saveViewBean(viewBean, session);
    }

    /**
     * trouve un dto dans la session, en recree un si ce n'est pas le bon puis le renseigne dans le viewBean.
     *
     * <p>
     * note: cette methode doit etre appellee une fois qu'une session valide se trouve dans le viewBean.
     * </p>
     *
     * @param session
     * @param spViewBean
     */
    private void setDTO(HttpSession session, APSituationProfessionnelleViewBean spViewBean) {
        // le type de prestation
        spViewBean.setTypePrestation(TypePrestation.typePrestationInstanceForCS((String) PRSessionDataContainerHelper
                .getData(session, PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)));

        // le dto
        APDroitDTO dto = (APDroitDTO) PRSessionDataContainerHelper.getData(session,
                PRSessionDataContainerHelper.KEY_DROIT_DTO);

        if (!spViewBean.getIdDroitLAPGBackup().equals(dto.getIdDroit())) {
            try {
                APDroitLAPG droit;

                if (spViewBean.getTypePrestation().equals(TypePrestation.TYPE_APG)) {
                    droit = new APDroitAPG();
                } else {
                    droit = new APDroitMaternite();
                }

                droit.setIdDroit(spViewBean.getIdDroitLAPGBackup());
                droit.setSession(spViewBean.getSession());
                droit.retrieve();

                if (spViewBean.getTypePrestation().equals(TypePrestation.TYPE_APG)) {
                    dto = new APDroitAPGDTO(droit);
                } else {
                    dto = new APDroitDTO(droit);
                }

                PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, dto);
            } catch (Exception e) {
                spViewBean.setMessage(e.getMessage());
                spViewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        spViewBean.setDroitDTO(dto);
    }

}
