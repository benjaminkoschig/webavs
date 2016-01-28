/*
 * Créé le 8 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.BITransaction;
import globaz.globall.client.AJParams;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFApercuEnfant;
import globaz.hera.db.famille.SFApercuEnfantManager;
import globaz.hera.db.famille.SFApercuRelationConjoint;
import globaz.hera.db.famille.SFApercuRequerant;
import globaz.hera.db.famille.SFConjoint;
import globaz.hera.db.famille.SFConjointManager;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFPeriode;
import globaz.hera.db.famille.SFRelationConjoint;
import globaz.hera.db.famille.SFRelationFamilialeRequerant;
import globaz.hera.db.famille.SFRequerant;
import globaz.hera.db.famille.SFRequerantDTO;
import globaz.hera.enums.TypeDeDetenteur;
import globaz.hera.external.ISFUrlEncode;
import globaz.hera.helpers.famille.SFPeriodeHelper;
import globaz.hera.helpers.famille.SFRequerantHelper;
import globaz.hera.interfaces.tiers.SFTiersHelper;
import globaz.hera.interfaces.tiers.SFTiersWrapper;
import globaz.hera.tools.SFSessionDataContainerHelper;
import globaz.hera.tools.nss.SFUtil;
import globaz.hera.vb.famille.SFApercuRelationConjointViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.controler.IPRImpactController;
import globaz.prestation.controler.PRImpactControllerFactory;
import globaz.prestation.controler.rentes.PREtatCivilDataController;
import java.io.IOException;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author mmu
 * 
 *         <p>
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 *         </p>
 */
public class SFApercuRelationConjointAction extends SFDefaultAction {

    private static final String VERS_ECRAN_DE_ADD = "_de.jsp?" + SFDefaultAction.METHOD_ADD;
    private static final String VERS_ECRAN_DE_UPD = "_de.jsp?" + SFDefaultAction.METHOD_UPD;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    /**
     * Crée une nouvelle instance de la classe SFApercuRelationConjointAction.
     * 
     * @param servlet
     */
    public SFApercuRelationConjointAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return "/hera?userAction=" + ISFActions.ACTION_VUE_GLOBALE + ".afficherFamilleRequerant";
    }

    @Override
    protected String _getDestEchec(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".reAfficher";
    }

    // Surcharge de la méthode pour retourner dans la vue globale.
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return "/hera?userAction=" + ISFActions.ACTION_VUE_GLOBALE + ".afficherFamilleRequerant";
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return "/hera?userAction=" + ISFActions.ACTION_VUE_GLOBALE + ".afficherFamilleRequerant";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        FWViewBeanInterface viewBean = this.loadViewBean(session);

        if (isRetourDepuisPyxis(viewBean)) {
            // on revient depuis pyxis on se contente de forwarder car le bon
            // viewBean est deja en session
            ((SFApercuRelationConjointViewBean) viewBean).setRetourDepuisPyxis(false); // pour
            // la
            // prochaine
            // fois

            if (((SFApercuRelationConjointViewBean) viewBean).isNew()) {
                forward(getRelativeURL(request, session) + SFApercuRelationConjointAction.VERS_ECRAN_DE_ADD, request,
                        response);
            } else {
                forward(getRelativeURL(request, session) + SFApercuRelationConjointAction.VERS_ECRAN_DE_UPD, request,
                        response);
            }
        } else {
            // on ne revient pas depuis pyxis, comportement par defaut
            super.actionAfficher(session, request, response, mainDispatcher);

        }
    }

    /**
     * DOCUMENT ME!
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
    protected void actionAjouterConjointInconnu(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        BSession bSession = (BSession) mainDispatcher.getSession();
        BITransaction transaction = null;

        try {
            transaction = bSession.newTransaction();
            // On connait l'id membre famille du con joint inconnu il suffit
            // alors de rajouter la relation avec le requerant
            SFConjoint conjoint = null;
            // On regarde d'abord que le requerant et le membre de la famille ne
            // sont pas déjà conjoints

            SFRequerantHelper rh = new SFRequerantHelper();
            SFRequerantDTO requerant = rh.getRequerantDTO(session);
            SFConjointManager conjointManager = new SFConjointManager();
            conjointManager.setSession(bSession);
            conjointManager.setForIdsConjoints(ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU,
                    requerant.getIdMembreFamille());
            try {
                conjointManager.find(transaction);
            } catch (Exception e) {
                JadeLogger.warn("SFApercuRelationConjointAction : ajouterRelation : conjointInconnu", e);
            }
            if (conjointManager.isEmpty()) {
                // ajouter le conjoint
                conjoint = new SFConjoint();
                conjoint.setSession(bSession);
                conjoint.setIdConjoint1(requerant.getIdMembreFamille());
                conjoint.setIdConjoint2(ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU);
                conjoint.add(transaction);
                rh.throwExceptionIfError((BTransaction) transaction, conjoint);
            } else {
                conjoint = (SFConjoint) conjointManager.getFirstEntity();
            }
            // On a le conjoint il suffit alors de rajouter la relation entre
            // conjoint
            SFRelationConjoint relationConjoint = new SFRelationConjoint();
            relationConjoint.setSession(bSession);
            relationConjoint.setIdConjoints(conjoint.getIdConjoints());
            relationConjoint.setTypeRelation(ISFSituationFamiliale.CS_REL_CONJ_ENFANT_COMMUN);
            relationConjoint.setAlternateKey(SFRelationConjoint.ALT_KEY_CONJ_TYPE);
            relationConjoint.retrieve(transaction);
            if (relationConjoint.isNew()) {
                relationConjoint.add(transaction);
                rh.throwExceptionIfError((BTransaction) transaction, relationConjoint);
            }
            // On ajoute le conjoint inconnu à la famille du requérant si il
            // n'en fait pas déjà partie
            SFRelationFamilialeRequerant familleRequerant = new SFRelationFamilialeRequerant();
            familleRequerant.setSession(bSession);
            familleRequerant.setIdMembreFamille(ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU);
            familleRequerant.setIdRequerant(requerant.getIdRequerant());
            familleRequerant.setAlternateKey(SFRelationFamilialeRequerant.ALTERNATE_KEY_MEMBRE_REQ);
            familleRequerant.retrieve(transaction);
            if (familleRequerant.isNew()) {
                familleRequerant.add(transaction);
                rh.throwExceptionIfError((BTransaction) transaction, familleRequerant);
            }
            transaction.commit();
        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (Exception e1) {
            }
            JadeLogger.warn("SFApercuRelationConjointAction : ajouterFamilleRequerant", e);
        } finally {
            try {
                transaction.closeTransaction();
            } catch (Exception e1) {
            }
            transaction = null;
        }
        // on redirige
        String _destination = _getDestAjouterSucces(session, request, response, null);
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Cette méthode est appelée quand une relation est crée
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     */
    protected void actionAjouterRelation(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        SFRequerantHelper rh = new SFRequerantHelper();

        StringBuffer sb = new StringBuffer();
        BSession bSession = (BSession) dispatcher.getSession();
        BITransaction transaction = null;
        String _destination = "";
        SFApercuRelationConjointViewBean viewbean = (SFApercuRelationConjointViewBean) session.getAttribute("viewBean");
        String numAvs = rh.parseRequest(request, "nss");
        String nom = rh.parseRequest(request, "nom");
        String prenom = rh.parseRequest(request, "prenom");
        String sexe = rh.parseRequest(request, "csSexe");
        String dateNaissance = rh.parseRequest(request, "dateNaissance");
        String dateDeces = rh.parseRequest(request, "dateDeces");
        String nationalite = rh.parseRequest(request, "csNationalite");
        String cantonDomicile = rh.parseRequest(request, "csCantonDomicile");
        String relation = request.getParameter("typeRelation");
        String debutRelation = request.getParameter("dateDebut");
        String finRelation = request.getParameter("dateFin");
        String idConjoints = request.getParameter("idConjoints");
        String motif = request.getParameter("motif");
        String codePays = rh.parseRequest(request, "pays", null);

        try {

            transaction = bSession.newTransaction();

            String idAssure = rh.parseRequest(request, "idAssure", null);
            String provenance = rh.parseRequest(request, "provenance", null);
            String idTiers = null;
            if (SFUtil.PROVENANCE_TIERS.equals(provenance)) {
                idTiers = idAssure;
            }
            SFRequerantDTO requerant = rh.getRequerantDTO(session);
            SFMembreFamille membre = rh.ajouterMembre(idTiers, numAvs, nom, prenom, sexe, dateNaissance, dateDeces,
                    nationalite, cantonDomicile, requerant.getIdDomaineApplication(), codePays, bSession, transaction);
            if (FWViewBeanInterface.ERROR.equals(membre.getMsgType())) {
                throw new Exception(membre.getMessage());
            }

            // Vérifie que le membre est valide
            rh.throwExceptionIfError((BTransaction) transaction, membre);
            try {
                membre.retrieve(transaction);
            } catch (Exception e1) {
                throw new Exception(bSession.getLabel("ERROR_ADD_MEMBRE"));
            }
            if ((membre == null) || membre.isNew()) {
                throw new Exception(bSession.getLabel("ERROR_ADD_MEMBRE"));
            }

            // Voilà le membre est soit crée, soit retrouvé, il suffit alors
            // d'ajouter la relation
            SFConjoint conjoint = null;
            SFConjointManager conjointManager = null;
            if ((membre != null) && !membre.isNew()) {
                // On regarde d'abord que le requerant et le membre de la
                // famille ne sont pas déjà conjoints
                conjointManager = new SFConjointManager();
                conjointManager.setSession(bSession);
                conjointManager.setForIdsConjoints(membre.getIdMembreFamille(), requerant.getIdMembreFamille());
                conjointManager.find(transaction);
                if (conjointManager.isEmpty()) {
                    // ajouter le conjoint
                    conjoint = new SFConjoint();
                    conjoint.setSession(bSession);
                    conjoint.setIdConjoint1(requerant.getIdMembreFamille());
                    conjoint.setIdConjoint2(membre.getIdMembreFamille());
                    conjoint.add(transaction);
                    rh.throwExceptionIfError((BTransaction) transaction, conjoint);
                } else {
                    conjoint = (SFConjoint) conjointManager.getFirstEntity();
                }
                idConjoints = conjoint.getIdConjoints();
            } else {
                throw new Exception(bSession.getLabel("ERROR_ADD_MEMBRE"));
            }

            // Récupération de l'état civil de chacun des MF :
            SFMembreFamille mf1 = new SFMembreFamille();
            mf1.setSession(bSession);
            mf1.setIdMembreFamille(conjoint.getIdConjoint1());
            mf1.retrieve(transaction);
            String csEtatCivil1 = mf1.getEtatCivil(JACalendar.todayJJsMMsAAAA());

            SFMembreFamille mf2 = new SFMembreFamille();
            mf2.setSession(bSession);
            mf2.setIdMembreFamille(conjoint.getIdConjoint2());
            mf2.retrieve(transaction);
            String csEtatCivil2 = mf2.getEtatCivil(JACalendar.todayJJsMMsAAAA());

            // On a le conjoint il suffit alors de rajouter la relation entre
            // conjoint
            SFRelationConjoint relationConjoint = new SFRelationConjoint();
            relationConjoint.setSession(bSession);
            relationConjoint.setIdConjoints(idConjoints);
            relationConjoint.setTypeRelation(relation);
            relationConjoint.setDateDebut(debutRelation);
            relationConjoint.setDateFin(finRelation);
            relationConjoint.add(transaction);
            rh.throwExceptionIfError((BTransaction) transaction, relationConjoint);

            // On ajoute le conjoint à la famille du requérant
            rh.updateRelationRequerant(requerant.getIdRequerant(), membre.getIdMembreFamille(), true, bSession,
                    transaction);
            // Si le membre conjoint est égallement un requérant, on lui ajoute
            // le membre à sa famille
            rh.updateRelationMembreRequerant(membre.getIdMembreFamille(), requerant.getIdMembreFamille(), true,
                    bSession, transaction);

            // Si on a une relation de type divorce et si les conjoints ont des
            // enfants...il faut rajouter 2 périodes BTE aux parents
            ajoutePeriodeBTE(bSession, transaction, relation, debutRelation, finRelation, conjoint, relationConjoint);

            // Si on a une date de fin et un motif, on peu creer la relation
            // suivante
            if (!JadeStringUtil.isEmpty(finRelation) && !JadeStringUtil.isIntegerEmpty(motif)) {

                // on cherche la date de debut de la relation suivante
                JACalendar cal = new JACalendarGregorian();
                JADate dateFinRelationPrec = new JADate(finRelation);
                String debutRelationSuivante = cal.addDays(dateFinRelationPrec, 1).toStr(".");

                // on ajoute la prochaine
                SFRelationConjoint relationSuivante = new SFRelationConjoint();
                relationSuivante.setSession(bSession);
                relationSuivante.setIdConjoints(idConjoints);
                relationSuivante.setTypeRelation(motif);
                relationSuivante.setDateDebut(debutRelationSuivante);
                relationSuivante.setDateFin("");
                relationSuivante.add(transaction);
                rh.throwExceptionIfError((BTransaction) transaction, relationSuivante);

                // Si on a une relation de type divorce et si les conjoints ont
                // des enfants...il faut rajouter 2 périodes BTE aux parents
                ajoutePeriodeBTE(bSession, transaction, relation, debutRelationSuivante, "", conjoint, relationSuivante);
            }

            PREtatCivilDataController dc = new PREtatCivilDataController();
            dc.setCsEtatCivilMF1AvantMAJ(csEtatCivil1);
            dc.setCsEtatCivilMF2AvantMAJ(csEtatCivil2);
            dc.setMf1(mf1);
            dc.setMf2(mf2);

            sb.append(PRImpactControllerFactory.getControler(IPRImpactController.DOMAINE_RENTE_CONTROLER).control(
                    bSession, (BTransaction) transaction, dc));

            transaction.commit();
            _destination = _getDestAjouterSucces(session, request, response, null);
        } catch (Exception e) {
            // JadeLogger.warn("SFApercuRelationConjointAction : ajouterRelation ",
            // e);
            try {
                transaction.rollback();
            } catch (Exception e1) {
            }
            try {
                viewbean.setMsgType(FWViewBeanInterface.ERROR);
                viewbean.setMessage(e.getMessage());
                SFMembreFamille membre = new SFMembreFamille();
                JSPUtils.setBeanProperties(request, membre);
                viewbean.setMembreFamilleConjoint(membre);
                viewbean.setTypeRelation(relation);
                viewbean.setDateDebut(debutRelation);
                viewbean.setDateFin(finRelation);
                session.setAttribute(FWServlet.VIEWBEAN, viewbean);
            } catch (Exception e1) {
            }
            _destination = _getDestEchec(session, request, response, viewbean);
        } finally {

            if (!FWViewBeanInterface.ERROR.equals(viewbean.getMsgType())) {
                if ((sb != null) && (sb.length() > 0)) {
                    viewbean.setMsgType(FWViewBeanInterface.WARNING);
                    viewbean.setMessage(sb.toString());
                }
            }

            try {
                if (transaction != null) {
                    transaction.closeTransaction();
                }
            } catch (Exception e1) {
                transaction = null;
            }

        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        FWViewBeanInterface viewBean = this.loadViewBean(session);
        SFApercuRelationConjointViewBean sfViewBean;

        if (isRetourDepuisPyxis(viewBean)) {
            ((SFApercuRelationConjointViewBean) viewBean).setRetourDepuisPyxis(false);
            // on revient de pyxis, on recupere le viewBean en session pour
            // afficher les donnees dans la page rc.
            sfViewBean = (SFApercuRelationConjointViewBean) viewBean;

            // mainDispatcher.dispatch(sfViewBean, getAction());

            this.saveViewBean(sfViewBean, request);
        }

        super.actionChercher(session, request, response, mainDispatcher);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param dispatcher
     *            DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String actionPart = getAction().getActionPart();
        if ("ajouterConjointInconnu".equals(actionPart)) {
            actionAjouterConjointInconnu(session, request, response, dispatcher);
        } else if ("ajouterRelation".equals(actionPart)) {
            actionAjouterRelation(session, request, response, dispatcher);
        } else if ("nouvelleRelation".equals(actionPart)) {
            actionNouvelleRelation(session, request, response, dispatcher);
        } else if ("entrerApplication".equals(actionPart)) {
            actionEntrerApplication(session, request, response, dispatcher);
        } else {
            // on a demandé un page qui n'existe pas
            servlet.getServletContext().getRequestDispatcher(FWDefaultServletAction.UNDER_CONSTRUCTION_PAGE)
                    .forward(request, response);
        }
    }

    /**
     * Entrée dans HERA depuis une autre application 2 parametres doivent être donnés par la requete: idTiers, urlFrom
     * urlFrom est l'url de retour dans l'application de base et doit être encodée comme ceci:
     * globaz.hera.external.ISFUrlEncode.encodeUrl("mon URL de retour");
     * 
     * 
     * 
     */
    protected void actionEntrerApplication(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        String idTiers = request.getParameter("idTiers");
        String urlFrom = request.getParameter("urlFrom");
        String csDomaine = request.getParameter("csDomaine");
        if (csDomaine == null) {
            csDomaine = ISFSituationFamiliale.CS_DOMAINE_STANDARD;
        }

        String destination = "";

        // ajoute l'url en session pour le mettre sur la pile
        String allParams = AJParams.getQueryString(request);
        String fullUrl = FWServlet.HTTP + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + request.getServletPath() + FWServlet.QUESTION + allParams;
        session.setAttribute("fullUrl", fullUrl);
        SFRequerantHelper rh = new SFRequerantHelper();
        BSession bSession = (BSession) mainDispatcher.getSession();
        BTransaction transaction = null;
        try {
            transaction = (BTransaction) bSession.newTransaction();

            // Vérifie que le Tiers soit bien dans les tiers
            if (JadeStringUtil.isEmpty(idTiers)) {
                throw new Exception(bSession.getLabel("ERROR_TIERS_NON_TROUVE"));
            }
            SFTiersWrapper tWrapper = SFTiersHelper.getTiersParId(bSession, idTiers);
            if (tWrapper == null) {
                throw new Exception(bSession.getLabel("ERROR_TIERS_NON_TROUVE"));
            }

            // Règles......
            // 1) Si requerant existe pour le domaine concerné, on le récupère.
            // 2) Si requerant n'existe pas dans IJ, mais dans STD, on le
            // récupère.
            // 3) Si requérant inexistant dans IJ et STD, mais membre existe
            // dans STD on crée le requérant dans standard
            // 4) Si requérant inexistant dans IJ et STD, mais membre existe
            // dans IJ -- on créé requérant dans STD
            // 5) Si requérant inexistant dans IJ et STD, mais membre existe
            // dans IJ et STD -- on créé requérant dans STD
            // 6) Si requérant inexistant dans IJ et STD, et membre inexistant
            // dans IJ et STD, on créé requérant et membre dans standard
            // TODO faire meme traitement dans
            // SFApercuRelationConjointAction.java !!!

            // Contrôle de l'existence du requérant ...
            SFApercuRequerant requerant = new SFApercuRequerant();
            requerant.setISession(bSession);
            requerant.setAlternateKey(SFApercuRequerant.ALT_KEY_IDTIERS);
            requerant.setIdTiers(idTiers);
            String idDomaine = csDomaine;
            if (idDomaine == null) {
                idDomaine = rh.getDomaine(session);
            }

            requerant.setIdDomaineApplication(idDomaine);
            requerant.retrieve(transaction);

            boolean isRequerantFoundDomaineExt = false;
            boolean isRequerantFoundDomaineStd = false;
            boolean isMFFoundDomaineStd = false;

            if (!requerant.isNew()) {
                isRequerantFoundDomaineExt = true;
            } else {
                // Cas particulier.... dans le cas du calcul prévisionnel, si
                // rien trouvé dans le domaine prévisionnel,
                // on tente la récupération du membre dans le domaine des
                // rentes.
                if (ISFSituationFamiliale.CS_DOMAINE_CALCUL_PREVISIONNEL.equals(idDomaine)) {
                    requerant.setIdDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_RENTES);
                    requerant.retrieve(transaction);
                    if (!requerant.isNew()) {
                        isRequerantFoundDomaineExt = true;
                    } else {
                        // On tente de récupérer le requerant pour le domaine
                        // standard !!!
                        requerant.setIdDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
                        requerant.retrieve(transaction);
                        if (!requerant.isNew()) {
                            isRequerantFoundDomaineStd = true;
                        }
                    }
                } else {
                    // On tente de récupérer le requerant pour le domaine
                    // standard !!!
                    requerant.setIdDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
                    requerant.retrieve(transaction);
                    if (!requerant.isNew()) {
                        isRequerantFoundDomaineStd = true;
                    }
                }
            }

            // ////////////////////////////////////////////////////////////////////////////////
            // Cas #1,2 le requérant a été trouvé --> on le stock en session.
            // ////////////////////////////////////////////////////////////////////////////////
            if (isRequerantFoundDomaineExt || isRequerantFoundDomaineStd) {
                // Instancie le DT du nouveau requerant
                SFRequerantDTO reqDTO = new SFRequerantDTO(requerant);
                // Le place dans la session
                SFSessionDataContainerHelper.setData(session, SFSessionDataContainerHelper.KEY_REQUERANT_DTO, reqDTO);
            }

            // //////////////////////////////////////////////////////////////////////////////
            // Aucun requérant trouvé -> on le crée dans le domaine standard.
            // //////////////////////////////////////////////////////////////////////////////
            else {

                SFMembreFamille membre = new SFMembreFamille();
                membre.setSession(bSession);
                membre.setIdTiers(idTiers);
                membre.setCsDomaineApplication(csDomaine);
                membre.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
                membre.retrieve(transaction);
                if (membre.isNew()) {
                    // On tente de récupérer le membre de famille pour le
                    // domaine standard !!!
                    membre.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
                    membre.retrieve(transaction);
                    if (!membre.isNew()) {
                        isMFFoundDomaineStd = true;
                    }
                } else {
                    if (ISFSituationFamiliale.CS_DOMAINE_STANDARD.equals(membre.getCsDomaineApplication())) {
                        isMFFoundDomaineStd = true;
                    }
                }

                // On créé le membre de famille pour le domaine standard si
                // inexistant.
                String idMembreFamille = null;
                if (isMFFoundDomaineStd) {
                    idMembreFamille = membre.getIdMembreFamille();
                }
                // On créé le membre de famille pour le domaine standard
                else {
                    // Crée le membre
                    membre = new SFMembreFamille();
                    membre.setSession(bSession);
                    membre.setIdTiers(idTiers);
                    membre.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
                    membre.add(transaction);
                    rh.throwExceptionIfError(transaction, membre);
                    idMembreFamille = membre.getIdMembreFamille();
                }

                // On crée le requérant
                SFRequerant req = new SFRequerant();
                req.setISession(bSession);
                req.setIdDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
                req.setIdMembreFamille(idMembreFamille);
                req.add(transaction);
                rh.throwExceptionIfError(transaction, req);

                // On met le requerant en sesion
                requerant = new SFApercuRequerant();
                requerant.setISession(bSession);
                requerant.setIdRequerant(req.getIdRequerant());
                requerant.retrieve(transaction);
                // met le requerant en session
                // Instancie le DT du nouveau requerant
                SFRequerantDTO reqDTO = new SFRequerantDTO(requerant);
                // Le place dans la session
                SFSessionDataContainerHelper.setData(session, SFSessionDataContainerHelper.KEY_REQUERANT_DTO, reqDTO);

                // On ajoute dans la table des relation au requerant, tous les
                // enfants et les conjoints du nouveau requerant
                SFConjointManager conjMgr = new SFConjointManager();
                conjMgr.setSession(bSession);
                conjMgr.setForIdConjoint(idMembreFamille);
                conjMgr.find(transaction);
                // recherche les conjoints du nouveau requerant
                for (Iterator it = conjMgr.iterator(); it.hasNext();) {
                    SFConjoint conjoint = (SFConjoint) it.next();
                    String idConj = conjoint.getIdMembreFamilleConjoint(idMembreFamille);
                    SFRelationFamilialeRequerant relConj = new SFRelationFamilialeRequerant();
                    relConj.setSession(bSession);
                    relConj.setIdMembreFamille(idConj);
                    relConj.setIdRequerant(req.getIdRequerant());
                    relConj.retrieve(transaction);
                    if (relConj.isNew()) {
                        relConj.add(transaction);
                        rh.throwExceptionIfError(transaction, relConj);
                    }

                    // rajoute les enfants des conjoints
                    for (Iterator itEnfants = conjoint.getEnfants(transaction); itEnfants.hasNext();) {
                        SFApercuEnfant enfant = (SFApercuEnfant) itEnfants.next();
                        SFRelationFamilialeRequerant relEnf = new SFRelationFamilialeRequerant();
                        relEnf.setSession(bSession);
                        relEnf.setIdRequerant(req.getIdRequerant());
                        relEnf.setIdMembreFamille(enfant.getIdMembreFamille());
                        relEnf.retrieve(transaction);
                        if (relEnf.isNew()) {
                            relEnf.add(transaction);
                            rh.throwExceptionIfError(transaction, relEnf);
                        }
                    }
                }
            }

            // met l'adresse de retour en session
            String urlDecode = ISFUrlEncode.decodeUrl(urlFrom);
            session.setAttribute(SFSessionDataContainerHelper.KEY_VALEUR_RETOUR, urlDecode);
            destination = "/hera?userAction=" + ISFActions.ACTION_VUE_GLOBALE + ".afficherFamilleRequerant";

        } catch (Exception e) {
            // les erreurs sont renvoyées à l'utilisateur
            try {
                transaction.rollback();
            } catch (Exception e1) {
            }
            destination = "/hera?userAction=" + ISFActions.ACTION_VUE_GLOBALE + ".afficherFamilleRequerant&message="
                    + e.getMessage();
            // JadeLogger.warn("SFApercuRelationConjointAction.actionEntrerApplication()",
            // e);
        } finally {
            try {
                transaction.closeTransaction();
            } catch (Exception e1) {
            }
            transaction = null;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String destination = "";
        try {

            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            /*
             * recupération du bean depuis la sesison
             */
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            /*
             * set des properietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeUpdate, call du dispatcher puis mis en session
             */
            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.setAttribute("viewBean", viewBean);

            /*
             * choix de la destination _valid=fail : revient en mode edition
             */

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                destination = _getDestModifierSucces(session, request, response, viewBean);
            } else {
                destination = _getDestModifierEchec(session, request, response, viewBean);
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
     * Cette méthode est appelée quand une relation est crée à partir d'une relation précédante
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws ServletException
     * @throws IOException
     */
    protected void actionNouvelleRelation(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws ServletException, IOException {
        BSession bSession = (BSession) dispatcher.getSession();
        BITransaction transaction = null;
        String _destination = "";
        SFApercuRelationConjointViewBean viewbean = (SFApercuRelationConjointViewBean) session.getAttribute("viewBean");
        StringBuffer sb = new StringBuffer();
        SFRequerantHelper rh = new SFRequerantHelper();
        try {

            transaction = bSession.newTransaction();

            String relation = rh.parseRequest(request, "typeRelation");
            String debutRelation = rh.parseRequest(request, "dateDebut");
            String finRelation = rh.parseRequest(request, "dateFin");
            String idConjoints = request.getParameter("idConjoints");

            // Recherche les conjoints pour un test de consistance et pour
            // calculer les périodes BTE
            SFConjoint conjoint = new SFConjoint();
            conjoint.setSession(bSession);
            conjoint.setIdConjoints(idConjoints);
            conjoint.retrieve(transaction);

            // Récupération de l'état civil de chacun des MF :
            SFMembreFamille mf1 = new SFMembreFamille();
            mf1.setSession(bSession);
            mf1.setIdMembreFamille(conjoint.getIdConjoint1());
            mf1.retrieve(transaction);
            String csEtatCivil1 = mf1.getEtatCivil(JACalendar.todayJJsMMsAAAA());

            SFMembreFamille mf2 = new SFMembreFamille();
            mf2.setSession(bSession);
            mf2.setIdMembreFamille(conjoint.getIdConjoint2());
            mf2.retrieve(transaction);
            String csEtatCivil2 = mf2.getEtatCivil(JACalendar.todayJJsMMsAAAA());

            if (conjoint.isNew()) {
                throw new Exception(bSession.getLabel("VALIDATE_ID_CONJOINTS"));
            }

            // On a le conjoint il suffit alors de rajouter la relation entre
            // conjoint
            SFRelationConjoint relationConjoint = new SFRelationConjoint();
            relationConjoint.setSession(bSession);
            relationConjoint.setIdConjoints(idConjoints);
            relationConjoint.setTypeRelation(relation);
            relationConjoint.setDateDebut(debutRelation);
            relationConjoint.setDateFin(finRelation);
            relationConjoint.add(transaction);
            rh.throwExceptionIfError((BTransaction) transaction, relationConjoint);

            PREtatCivilDataController dc = new PREtatCivilDataController();
            dc.setCsEtatCivilMF1AvantMAJ(csEtatCivil1);
            dc.setCsEtatCivilMF2AvantMAJ(csEtatCivil2);
            dc.setMf1(mf1);
            dc.setMf2(mf2);

            sb.append(PRImpactControllerFactory.getControler(IPRImpactController.DOMAINE_RENTE_CONTROLER).control(
                    bSession, (BTransaction) transaction, dc));

            // ajoute les périodes BTE s'il s'agit d'un divorce
            ajoutePeriodeBTE(bSession, transaction, relation, debutRelation, finRelation, conjoint, relationConjoint);
            transaction.commit();
            _destination = _getDestAjouterSucces(session, request, response, viewbean);

        } catch (Exception e) {
            // JadeLogger.warn("SFApercuRelationConjointAction : ajouterRelation ",
            // e);
            try {
                transaction.rollback();
            } catch (Exception e1) {
            }
            try {
                viewbean.setMsgType(FWViewBeanInterface.ERROR);
                viewbean.setMessage(e.getMessage());
                SFMembreFamille membre = new SFMembreFamille();
                JSPUtils.setBeanProperties(request, membre);
                viewbean.setMembreFamilleMembre(membre);
                session.setAttribute(FWServlet.VIEWBEAN, viewbean);
            } catch (Exception e1) {
            }

            _destination = _getDestEchec(session, request, response, viewbean) + "&mode=isNewRelation";
        } finally {
            try {
                if (transaction != null) {
                    transaction.closeTransaction();
                }
            } catch (Exception e1) {
                transaction = null;
            }

            if (!FWViewBeanInterface.ERROR.equals(viewbean.getMsgType())) {
                if ((sb != null) && (sb.length() > 0)) {
                    viewbean.setMsgType(FWViewBeanInterface.WARNING);
                    viewbean.setMessage(sb.toString());
                }
            }
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    /**
     * DOCUMENT ME!
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
    protected void actionRechercherSituation(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        super.actionChercher(session, request, response, mainDispatcher);
    }

    // ajoute les périodes BTE si la relation est un divorce
    private void ajoutePeriodeBTE(BSession bSession, BITransaction transaction, String typeRelation,
            String debutRelation, String finRelation, SFConjoint conjoint, SFRelationConjoint relationConjoint)
            throws Exception {

        // Si on a une relation de type divorce et si les conjoints ont des
        // enfants...il faut rajouter 2 périodes BTE aux parents
        if (typeRelation.equals(ISFSituationFamiliale.CS_REL_CONJ_DIVORCE)) {
            SFApercuEnfantManager enfantManager = new SFApercuEnfantManager();
            enfantManager.setSession(bSession);
            enfantManager.setForIdConjoint(relationConjoint.getIdConjoints());

            enfantManager.find(transaction);
            for (Iterator itEnf = enfantManager.iterator(); itEnf.hasNext();) {
                SFApercuEnfant enfant = (SFApercuEnfant) itEnf.next();
                // si le divorce se termine avant la naissance de l'enfant ou le
                // déces avant le début du divorce, pas de BTE
                if (BSessionUtil.compareDateFirstGreaterOrEqual(bSession, finRelation, enfant.getDateNaissance())
                        || (BSessionUtil.compareDateFirstGreater(bSession, debutRelation, enfant.getDateDeces()) && !JAUtil
                                .isDateEmpty(enfant.getDateDeces()))) {
                    continue;
                }

                SFPeriode periode = new SFPeriode();
                periode.setSession(bSession);
                periode.setIdMembreFamille(enfant.getIdMembreFamille());
                periode.setType(ISFSituationFamiliale.CS_TYPE_PERIODE_GARDE_BTE);
                periode.setDateDebut(SFPeriodeHelper.getDebutPeriode(bSession, debutRelation, enfant.getDateNaissance()));
                periode.setDateFin(SFPeriodeHelper.getFinPeriode(bSession, finRelation, enfant.getDateDeces()));

                if (!ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(conjoint.getIdConjoint1())) {
                    periode.setIdDetenteurBTE(conjoint.getIdConjoint1());
                    periode.setCsTypeDeDetenteur(TypeDeDetenteur.FAMILLE.getCodeSystemAsString());
                    periode.add(transaction);
                    SFRequerantHelper rh = new SFRequerantHelper();
                    rh.throwExceptionIfError((BTransaction) transaction, periode);
                }
                if (!ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(conjoint.getIdConjoint2())) {
                    periode.setIdDetenteurBTE(conjoint.getIdConjoint2());
                    periode.setCsTypeDeDetenteur(TypeDeDetenteur.FAMILLE.getCodeSystemAsString());
                    periode.add(transaction);
                    SFRequerantHelper rh = new SFRequerantHelper();
                    rh.throwExceptionIfError((BTransaction) transaction, periode);
                }
            }

        }
    }

    /**
     * DOCUMENT ME!
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
        if (viewBean instanceof SFApercuRelationConjoint) {
            SFApercuRelationConjoint relation = (SFApercuRelationConjoint) viewBean;
            String idRelation = request.getParameter("idRelationConjoint");
            if (!JadeStringUtil.isEmpty(idRelation)) {
                relation.setIdRelationConjoint(idRelation);
            }
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

    /**
     * inspecte le viewBean et retourne vrai si celui-ci indique que l'on revient de pyxis.
     * 
     * @param viewBean
     * 
     * @return
     */
    private boolean isRetourDepuisPyxis(FWViewBeanInterface viewBean) {
        return ((viewBean != null) && (viewBean instanceof SFApercuRelationConjointViewBean) && ((SFApercuRelationConjointViewBean) viewBean)
                .isRetourDepuisPyxis());
    }

}