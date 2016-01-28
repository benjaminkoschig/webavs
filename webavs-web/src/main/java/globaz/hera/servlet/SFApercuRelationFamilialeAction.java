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
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFApercuEnfant;
import globaz.hera.db.famille.SFApercuRelationFamilialeRequerantManager;
import globaz.hera.db.famille.SFApercuRequerant;
import globaz.hera.db.famille.SFConjoint;
import globaz.hera.db.famille.SFConjointManager;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFMembreFamilleManager;
import globaz.hera.db.famille.SFRelationConjoint;
import globaz.hera.db.famille.SFRelationConjointManager;
import globaz.hera.db.famille.SFRelationFamilialeRequerant;
import globaz.hera.db.famille.SFRequerant;
import globaz.hera.db.famille.SFRequerantDTO;
import globaz.hera.external.ISFUrlEncode;
import globaz.hera.helpers.famille.SFRequerantHelper;
import globaz.hera.interfaces.tiers.SFTiersHelper;
import globaz.hera.interfaces.tiers.SFTiersWrapper;
import globaz.hera.tools.SFSessionDataContainerHelper;
import globaz.hera.tools.nss.SFUtil;
import globaz.hera.vb.famille.SFApercuRelationFamilialeRequerantListViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.tools.PRAssert;
import java.io.IOException;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * DOCUMENT ME!
 * 
 * @author jpa
 * 
 *         <p>
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 *         </p>
 */
public class SFApercuRelationFamilialeAction extends SFDefaultAction {

    /**
     * Crée une nouvelle instance de la classe SFApercuRelationFamilialeAction.
     * 
     * @param servlet
     */
    public SFApercuRelationFamilialeAction(FWServlet servlet) {
        super(servlet);
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
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return "/hera?userAction=" + ISFActions.ACTION_VUE_GLOBALE + ".afficherFamilleRequerant";
    }

    @Override
    protected String _getDestEchec(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        return "/hera?userAction=" + ISFActions.ACTION_VUE_GLOBALE + ".afficherFamilleRequerant&message="
                + viewBean.getMessage();
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

    /**
     * Ajoute le membre de la famille comme conjoint du requerant avec une relation indéfinie
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     */
    protected void actionAjouterConjointRequerant(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws ServletException, IOException {
        BITransaction transaction = null;

        String destination = _getDestAjouterSucces(session, request, response, null);
        SFRequerantHelper rh = new SFRequerantHelper();
        try {
            String idMembreFamille = request.getParameter("idMembreFamille");
            SFRequerantDTO reqDTO = rh.getRequerantDTO(session);
            // Si aucun requerant en session, on ne fait rien
            if (reqDTO == null) {
                throw new Exception();
            }

            BSession bSession = (BSession) dispatcher.getSession();
            transaction = bSession.newTransaction();

            /*
             * 0. Contrôle que le membre de famille à ajouter est du même domaine que le requérant
             */
            SFMembreFamille mf = new SFMembreFamille();
            mf.setSession(bSession);
            mf.setIdMembreFamille(idMembreFamille);
            mf.retrieve(transaction);
            PRAssert.notIsNew(mf, bSession.getLabel("ERROR_MEMBRE_FAMILLE_NON_TROUVE") + idMembreFamille);

            if (!mf.getCsDomaineApplication().equals(reqDTO.getIdDomaineApplication())) {
                throw new Exception(bSession.getLabel("ERROR_MEMBRE_MEME_DOMAINE"));
            }

            /* 1. Ajoute une relation au requerant */
            rh.updateRelationRequerant(reqDTO.getIdRequerant(), idMembreFamille, true, bSession, transaction);
            rh.updateRelationMembreRequerant(idMembreFamille, reqDTO.getIdMembreFamille(), true, bSession, transaction);

            /* 2. Crée les conjoints */

            // On regarde d'abord que le requerant et le membre de la famille ne
            // sont pas déjà conjoints
            String idMemFamReq = reqDTO.getIdMembreFamille(); // idMembreFamille
            // du requerant
            SFConjointManager conjointManager = new SFConjointManager();
            conjointManager.setSession(bSession);
            conjointManager.setForIdsConjoints(idMembreFamille, idMemFamReq);
            try {
                conjointManager.find(transaction);
            } catch (Exception e) {
                JadeLogger.warn("SFApercuRelationFamilialeAction : ajouterRelationConjoint : findError ", e);
            }
            SFConjoint conjoint = new SFConjoint();
            if (conjointManager.isEmpty()) {
                // ajouter le conjoint
                conjoint.setSession(bSession);
                conjoint.setIdConjoint1(idMemFamReq);
                conjoint.setIdConjoint2(idMembreFamille);
                conjoint.add(transaction);
                rh.throwExceptionIfError((BTransaction) transaction, conjoint);
            } else {
                conjoint = (SFConjoint) conjointManager.getFirstEntity();
            }

            /* 3. Ajoute la relation de type indéfinie entre les conjoints */
            SFRelationConjoint relation = new SFRelationConjoint();
            relation.setSession(bSession);
            relation.setIdConjoints(conjoint.getIdConjoints());
            relation.setTypeRelation(ISFSituationFamiliale.CS_REL_CONJ_RELATION_INDEFINIE);
            relation.add(transaction);
            rh.throwExceptionIfError((BTransaction) transaction, relation);
            transaction.commit();
        } catch (Exception e) {
            destination = "/hera?userAction=" + ISFActions.ACTION_RELATION_FAMILIALE_REQUERANT + '.'
                    + FWAction.ACTION_CHERCHER + "&message=" + e.toString();
            try {
                transaction.rollback();
            } catch (Exception e2) {
            }
            // JadeLogger.warn("SFApercuRelationConjointAction : ajouterConjointRequerant ",
            // e);
        } finally {
            try {
                transaction.closeTransaction();
            } catch (Exception e2) {
            }
            transaction = null;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * Crée un membre de famille, devient requérant et mis en session
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     */

    protected void actionAjouterRequerant(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination = "";
        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
        BSession bSession = (BSession) mainDispatcher.getSession();
        BITransaction transaction = null;
        SFRequerantHelper rh = new SFRequerantHelper();
        try {
            transaction = bSession.newTransaction();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            String numAvs = request.getParameter("nss");
            // Si aucun n° avs est renseigné on renvoie une erreur
            if (JadeStringUtil.isEmpty(numAvs)) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(bSession.getLabel("ERROR_REQUERANT_NO_AVS"));
                throw new Exception();
                // servlet.getServletContext().getRequestDispatcher(_destination).forward(request,
                // response);
                // return ;
            }

            String nom = rh.parseRequest(request, "nom", "");
            String prenom = rh.parseRequest(request, "prenom", "");
            String sexe = rh.parseRequest(request, "csSexe", "");
            String dateNaissance = rh.parseRequest(request, "dateNaissance", "");
            String dateDeces = rh.parseRequest(request, "dateDeces", "");
            String nationalite = rh.parseRequest(request, "csNationalite", "");
            String cantonDomicile = rh.parseRequest(request, "csCantonDomicile", "");
            String idAssure = rh.parseRequest(request, "idAssure", null);
            String provenance = rh.parseRequest(request, "provenance", null);
            String csDomaine = rh.parseRequest(request, "csDomaine", null);
            String codePays = rh.parseRequest(request, "pays", null);
            String idTiers = null;
            if (SFUtil.PROVENANCE_TIERS.equals(provenance)) {
                idTiers = idAssure;
            }
            SFMembreFamilleManager mgr = null;
            if (idTiers != null) {
                // On contrôle que le requérant n'a pas déjà été créé
                mgr = new SFMembreFamilleManager();
                mgr.setSession(bSession);
                mgr.setForIdTiers(idTiers);
                mgr.setForCsDomaineApplication(csDomaine);
                mgr.find(transaction, 1);

                // Le requérant existe déjà dans les membres de la famille
                SFMembreFamille membreRequerant = null;
                if ((mgr != null) && (mgr.size() != 0)) {
                    membreRequerant = (SFMembreFamille) mgr.getEntity(0);
                    selectAndOrAddRequerant(session, bSession, transaction, membreRequerant.getIdMembreFamille(),
                            csDomaine, csDomaine);
                }
                // Le requérant n'existe pas encore en tant que membre de la
                // famille -> on le créé
                else {
                    SFMembreFamille membre = rh.ajouterMembre(idTiers, numAvs, nom, prenom, sexe, dateNaissance,
                            dateDeces, nationalite, cantonDomicile, csDomaine, codePays, bSession, transaction);
                    if (FWViewBeanInterface.ERROR.equals(membre.getMsgType())) {
                        throw new Exception(membre.getMessage());
                    }
                    try {
                        membre.retrieve(transaction);
                    } catch (Exception e) {
                        throw new Exception(bSession.getLabel("ERROR_ADD_MEMBRE") + ": " + e.getMessage());
                    }
                    // On s'assure que le membre ait bien été créé
                    if (!membre.isNew()) {
                        // On crée le requérant et le sélectionné
                        if (JadeStringUtil.isBlankOrZero(csDomaine)) {
                            csDomaine = rh.getDomaine(session);
                        }

                        SFRequerant requerant = addRequerant(bSession, (BTransaction) transaction, csDomaine,
                                membre.getIdMembreFamille());
                        rh.setRequerantToDTO(session, requerant);
                    } else {
                        throw new Exception(bSession.getLabel("ERROR_ADD_MEMBRE"));
                    }
                }
            }
            // idTiers non renseigné
            else {
                SFMembreFamille membre = rh.ajouterMembre(null, numAvs, nom, prenom, sexe, dateNaissance, dateDeces,
                        nationalite, cantonDomicile, csDomaine, codePays, bSession, transaction);

                if (FWViewBeanInterface.ERROR.equals(membre.getMsgType())) {
                    throw new Exception(membre.getMessage());
                }
                try {
                    membre.retrieve(transaction);
                } catch (Exception e) {
                    throw new Exception(bSession.getLabel("ERROR_ADD_MEMBRE") + ": " + e.getMessage());
                }
                // On s'assure que le membre ait bien été créé
                if (!membre.isNew()) {
                    // On cré le requérant
                    if (JadeStringUtil.isBlankOrZero(csDomaine)) {
                        csDomaine = rh.getDomaine(session);
                    }

                    SFRequerant requerant = addRequerant(bSession, (BTransaction) transaction, csDomaine,
                            membre.getIdMembreFamille());
                    rh.setRequerantToDTO(session, requerant);
                } else {
                    throw new Exception(bSession.getLabel("ERROR_ADD_MEMBRE"));
                }
            }

            transaction.commit();
            _destination = _getDestAjouterSucces(session, request, response, viewBean);
        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (Exception e1) {
            }
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            if (JadeStringUtil.isEmpty(viewBean.getMessage())) {
                viewBean.setMessage(bSession.getLabel("ERROR_AJOUTER_REQUERANT") + " :" + e.getMessage());
            }
            _destination = _getDestEchec(session, request, response, viewBean);
        } finally {
            try {
                transaction.closeTransaction();
            } catch (Exception e1) {
            }
            transaction = null;
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void actionChangerDomaineMF(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = new String();
        BSession bSession = (BSession) mainDispatcher.getSession();
        BITransaction transaction = null;
        try {
            transaction = bSession.newTransaction();
            String idTiers = request.getParameter("selectedId");
            String csDomaine = request.getParameter("csDomaine");
            if (csDomaine == null) {
                csDomaine = ISFSituationFamiliale.CS_DOMAINE_ID_STANDARD;
            }

            if (JadeStringUtil.isIntegerEmpty(idTiers)) {
                throw new Exception(bSession.getLabel("ERROR_ID_TIERS_NON_RENSEIGNE"));
            }

            SFMembreFamille membre = new SFMembreFamille();
            membre.setSession(bSession);
            membre.setIdTiers(idTiers);
            membre.setCsDomaineApplication(csDomaine);
            membre.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
            membre.retrieve(transaction);

            // Le requérant existe déjà dans les membres de la famille

            if (!membre.isNew()) {
                destination = "/hera?userAction=" + ISFActions.ACTION_VUE_GLOBALE
                        + ".afficherFamilleMembre&idMembreFamille=" + membre.getIdMembreFamille();
            } else {
                destination = "/hera?userAction=" + ISFActions.ACTION_VUE_GLOBALE
                        + ".afficherFamilleMembre&message=Requérant not trouvé pour ce domaine";
            }

            transaction.commit();
        } catch (Exception e) {
            // les erreurs sont renvoyées à l'utilisateur
            try {
                transaction.rollback();
            } catch (Exception e1) {
            }
            destination = "/hera?userAction=" + ISFActions.ACTION_APERCU_RELATION_CONJOINT + '.'
                    + FWAction.ACTION_CHERCHER + "&message=" + e.getMessage();
            // JadeLogger.warn("SFApercuRelationFamilialeAction.actionChangerDomaineRequerant()",
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

    /**
     * Crée le réquerant s'il n'existe pas, puis le place en session
     */
    protected void actionChangerDomaineRequerant(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = new String();
        BSession bSession = (BSession) mainDispatcher.getSession();
        BITransaction transaction = null;
        try {

            transaction = bSession.newTransaction();
            String idTiers = request.getParameter("selectedId");
            String csDomaine = request.getParameter("csDomaine");
            if (csDomaine == null) {
                csDomaine = ISFSituationFamiliale.CS_DOMAINE_ID_STANDARD;
            }

            if (JadeStringUtil.isIntegerEmpty(idTiers)) {
                throw new Exception(bSession.getLabel("ERROR_ID_TIERS_NON_RENSEIGNE"));
            }

            SFMembreFamille membre = new SFMembreFamille();
            membre.setSession(bSession);
            membre.setIdTiers(idTiers);
            membre.setCsDomaineApplication(csDomaine);
            membre.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
            membre.retrieve(transaction);

            // Le requérant existe déjà dans les membres de la famille

            if (!membre.isNew()) {
                retrieveRequerant(session, bSession, transaction, membre.getIdMembreFamille(), csDomaine);

                destination = "/hera?userAction=" + ISFActions.ACTION_VUE_GLOBALE + ".afficherFamilleRequerant";
            } else {
                destination = "/hera?userAction=" + ISFActions.ACTION_VUE_GLOBALE
                        + ".afficherFamilleRequerant&message=Requérant not trouvé pour ce domaine";
            }

            transaction.commit();
        } catch (Exception e) {
            // les erreurs sont renvoyées à l'utilisateur
            try {
                transaction.rollback();
            } catch (Exception e1) {
            }
            destination = "/hera?userAction=" + ISFActions.ACTION_APERCU_RELATION_CONJOINT + '.'
                    + FWAction.ACTION_CHERCHER + "&message=" + e.getMessage();
            // JadeLogger.warn("SFApercuRelationFamilialeAction.actionChangerDomaineRequerant()",
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
        if ("ajouterRequerant".equals(actionPart)) {
            actionAjouterRequerant(session, request, response, dispatcher);
        } else if ("ajouterConjointRequerant".equals(actionPart)) {
            actionAjouterConjointRequerant(session, request, response, dispatcher);
        } else if ("selectionnerRequerant".equals(actionPart)) {
            actionSelectionnerRequerant(session, request, response, dispatcher);
        } else if ("modifierMembre".equals(actionPart)) {
            actionModifierMembre(session, request, response, dispatcher);
        } else if ("modifierConjointInconnu".equals(actionPart)) {
            actionModifierConjointInconnu(session, request, response, dispatcher);
        } else if ("quitterApplication".equals(actionPart)) {
            actionQuitterApplication(session, request, response, dispatcher);
        } else if ("entrerApplication".equals(actionPart)) {
            actionEntrerApplication(session, request, response, dispatcher);
        } else if ("changerDomaineRequerant".equals(actionPart)) {
            actionChangerDomaineRequerant(session, request, response, dispatcher);
        } else if ("changerDomaineMF".equals(actionPart)) {
            actionChangerDomaineMF(session, request, response, dispatcher);

        } else {
            // on a demandé une page qui n'existe pas
            servlet.getServletContext().getRequestDispatcher(FWDefaultServletAction.UNDER_CONSTRUCTION_PAGE)
                    .forward(request, response);
        }
    }

    /**
     * Entrée dans HERA depuis une autre application 3 parametres doivent être donnés par la requete: idTiers, urlFrom,
     * csDomaine urlFrom est l'url de retour dans l'application de base et doit être encodée comme ceci:
     * globaz.hera.external.ISFUrlEncode.encodeUrl("mon URL de retour");
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
        SFRequerantHelper rh = new SFRequerantHelper();
        String destination = "";

        // ajoute l'url en session pour le mettre sur la pile
        String allParams = AJParams.getQueryString(request);
        String fullUrl = FWServlet.HTTP + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + request.getServletPath() + FWServlet.QUESTION + allParams;
        session.setAttribute("fullUrl", fullUrl);

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
            // destination = "/hera?userAction=" +
            // ISFActions.ACTION_APERCU_RELATION_CONJOINT + '.' +
            // FWAction.ACTION_CHERCHER;
            destination = "/hera?userAction=" + ISFActions.ACTION_VUE_GLOBALE + ".afficherFamilleRequerant";

        } catch (Exception e) {
            // les erreurs sont renvoyées à l'utilisateur
            try {
                transaction.rollback();
            } catch (Exception e1) {
            }
            // destination = "/hera?userAction=" +
            // ISFActions.ACTION_APERCU_RELATION_CONJOINT + '.' +
            // FWAction.ACTION_CHERCHER + "&message=" + e.getMessage();
            destination = "/hera?userAction=" + ISFActions.ACTION_VUE_GLOBALE + ".afficherFamilleRequerant"
                    + "&message=" + e.getMessage();
            // JadeLogger.warn("SFApercuRelationFamilialeAction.actionEntrerApplication()",
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionLister(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {
            /*
             * creation automatique du listviewBean
             */
            FWViewBeanInterface viewBean = new SFApercuRelationFamilialeRequerantListViewBean();

            /*
             * set automatique des properietes du listViewBean depuis la requete
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeLister() , puis appelle du dispatcher, puis le bean est mis en request
             */
            viewBean = beforeLister(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());
            request.setAttribute("viewBean", viewBean);

            // pour bt [...] et pagination
            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", viewBean);

            /*
             * destination : remarque : si erreur, on va quand meme sur la liste avec le bean vide en erreur
             */
            _destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void actionModifierConjointInconnu(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {

        SFRequerantHelper rh = new SFRequerantHelper();
        StringBuffer sb = new StringBuffer();
        BSession bSession = (BSession) mainDispatcher.getSession();
        BITransaction transaction = null;
        String _destination = "";
        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
        String numAvs = rh.parseRequest(request, "nss");
        String nom = rh.parseRequest(request, "nom");
        String prenom = rh.parseRequest(request, "prenom");
        String sexe = rh.parseRequest(request, "csSexe");
        String dateNaissance = rh.parseRequest(request, "dateNaissance");
        String dateDeces = rh.parseRequest(request, "dateDeces");
        String nationalite = rh.parseRequest(request, "csNationalite");
        String cantonDomicile = rh.parseRequest(request, "csCantonDomicile");
        String codePays = rh.parseRequest(request, "pays", null);
        String relation = request.getParameter("typeRelation");
        String debutRelation = request.getParameter("dateDebut");
        String finRelation = request.getParameter("dateFin");
        String idConjoints = request.getParameter("idConjoints");
        String motif = request.getParameter("motif");

        try {
            String idMembreFamille = request.getParameter("idMembreFamille");
            if (!ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(idMembreFamille)) {
                throw new Exception(
                        "Action Error, le membre de famille n'est pas le conjoint inconnu; modification refusée. idMF = "
                                + idMembreFamille);
            }

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

            // Voilà le membre est créé, il suffit alors de mettre à jours la
            // table des conjoint de sorte
            // à remplacer le conjoint inconnu par le nouveau conjoint.

            SFConjoint conjoint = null;
            SFConjointManager conjointManager = null;

            // On regarde d'abord que le requerant et le membre de la famille ne
            // sont pas déjà conjoints
            conjointManager = new SFConjointManager();
            conjointManager.setSession(bSession);
            conjointManager.setForIdsConjoints(ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU,
                    requerant.getIdMembreFamille());
            conjointManager.find(transaction);

            if (conjointManager.isEmpty()) {
                throw new Exception("Error, Conjoint cannot be empty. idMFRequerant = "
                        + requerant.getIdMembreFamille());
            } else {
                conjoint = (SFConjoint) conjointManager.getFirstEntity();
                if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(conjoint.getIdConjoint1())) {

                    conjoint.setIdConjoint1(membre.getIdMembreFamille());
                } else if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(conjoint.getIdConjoint2())) {
                    conjoint.setIdConjoint2(membre.getIdMembreFamille());
                } else {
                    throw new Exception("Error, Conjoint Inconnu non référencé. idConjoints = "
                            + conjoint.getIdConjoints());
                }
                conjoint.update(transaction);
            }
            idConjoints = conjoint.getIdConjoints();

            // On ajoute le conjoint à la famille du requérant
            rh.updateRelationRequerant(requerant.getIdRequerant(), membre.getIdMembreFamille(), true, bSession,
                    transaction);
            // Si le membre conjoint est égallement un requérant, on lui ajoute
            // le membre à sa famille
            rh.updateRelationMembreRequerant(membre.getIdMembreFamille(), requerant.getIdMembreFamille(), true,
                    bSession, transaction);

            // On supprime le conjoint inconnu de la famille du requérant
            // (SFREFARE)
            SFRelationFamilialeRequerant familleRequerant = new SFRelationFamilialeRequerant();
            familleRequerant.setSession(bSession);
            familleRequerant.setIdMembreFamille(ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU);
            familleRequerant.setIdRequerant(requerant.getIdRequerant());
            familleRequerant.setAlternateKey(SFRelationFamilialeRequerant.ALTERNATE_KEY_MEMBRE_REQ);
            familleRequerant.retrieve(transaction);
            if (!familleRequerant.isNew()) {
                familleRequerant.delete(transaction);
            }

            // On contrôle la cohérence de la/des relation de conjoints.
            // Si le nouveau conjoints est déjà marié, il faut s'assurer qu'il
            // n'est pas polygame !
            SFRelationConjointManager mgr = new SFRelationConjointManager();
            mgr.setSession(bSession);
            mgr.setForIdDesConjoints(conjoint.getIdConjoints());
            mgr.find(transaction);
            for (int i = 0; i < mgr.size(); i++) {
                SFRelationConjoint rel = (SFRelationConjoint) mgr.getEntity(i);
                BStatement stmt = new BStatement((BTransaction) transaction);
                rel.validate(stmt);
            }

            if (transaction.hasErrors()) {
                throw new Exception(transaction.getErrors().toString());
            }

            transaction.commit();
            _destination = _getDestAjouterSucces(session, request, response, null);
        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (Exception e1) {
            }
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            if (JadeStringUtil.isEmpty(viewBean.getMessage())) {
                viewBean.setMessage(bSession.getLabel("ERROR_MODIFICATION_MEMBRE") + ": " + e.getMessage());
            }
            _destination = _getDestEchec(session, request, response, viewBean);
        } finally {
            try {
                transaction.closeTransaction();
            } catch (Exception e1) {
            }
            transaction = null;
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void actionModifierMembre(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination = "";
        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
        BSession bSession = (BSession) mainDispatcher.getSession();
        BITransaction transaction = null;
        SFRequerantHelper rh = new SFRequerantHelper();
        try {
            transaction = bSession.newTransaction();

            String idAssure = rh.parseRequest(request, "idAssure", null);

            SFRequerantDTO requerant = rh.getRequerantDTO(session);
            String csDomaine = requerant.getIdDomaineApplication();

            if ("null".equals(idAssure)) {
                idAssure = null;
            }

            if (isIdAssureDejaPresentDansMembreFamille(idAssure, csDomaine, bSession, transaction)) {
                throw new Exception(bSession.getLabel("ERROR_TIERS_EXISTE_MEMBREFAMILLE"));
            }

            String provenance = rh.parseRequest(request, "provenance", null);
            String idMembreFamille = rh.parseRequest(request, "idMembreFamille", null);
            String idTiers = rh.parseRequest(request, "idTiers", null);
            String hasAdresseDomicile = rh.parseRequest(request, "hasAdresseDomicile", null);

            String numAvs = request.getParameter("nss");
            String nom = rh.parseRequest(request, "nom", "");
            String prenom = rh.parseRequest(request, "prenom", "");
            String sexe = rh.parseRequest(request, "csSexe", "");
            String dateNaissance = rh.parseRequest(request, "dateNaissance", "");
            String dateDeces = rh.parseRequest(request, "dateDeces", "");
            String nationalite = rh.parseRequest(request, "csNationalite", "");
            String cantonDomicile = rh.parseRequest(request, "csCantonDomicile", "");

            // On modifie le membre Si ...
            SFMembreFamille membre = new SFMembreFamille();
            membre.setSession(bSession);
            membre.setIdMembreFamille(idMembreFamille);
            membre.retrieve(transaction);
            if (membre.isNew()) {
                throw new Exception(bSession.getLabel("ERROR_MEMBRE_NOT_FOUND"));
            }
            rh.modifierMembre(rh.getRequerantDTO(session), false, membre, idAssure, provenance, numAvs, nom, prenom,
                    sexe, dateNaissance, dateDeces, nationalite, null, cantonDomicile, idTiers, hasAdresseDomicile,
                    bSession, transaction);

            // Si le membre est référencé dans les tiers, on contrôle la
            // validité de ses relations avec le requérant
            // Exemple : Requérant 756.1234.5678.90
            // Membre famille NSS = VIDE; Jean Dupont [ Marié ]
            //
            // Si la modification de Jean Dupont porte sur le NSS, et que le
            // nouveau NSS référence un membre de famille
            // déjà existant, il y a donc lieu de contrôler qu'il n'y ait pas de
            // chevauchement de période avec ce 'nouveau' membre de famille.
            if (!JadeStringUtil.isBlankOrZero(membre.getIdTiers())) {
                SFRequerantDTO reqDTO = rh.getRequerantDTO(session);
                // Si aucun requerant en session, on ne fait rien
                if (reqDTO == null) {
                    throw new Exception("Action impossible, aucun requérant sélectionné");
                }
                SFConjointManager cMgr = new SFConjointManager();
                cMgr.setSession(bSession);
                cMgr.setForIdsConjoints(membre.getIdMembreFamille(), reqDTO.getIdMembreFamille());
                if (!cMgr.isEmpty()) {
                    SFConjoint cj = (SFConjoint) cMgr.getFirstEntity();
                    // On contrôle la cohérence de la/des relation de conjoints.
                    // Si le nouveau conjoints est déjà marié, il faut s'assurer
                    // qu'il n'est pas polygame !
                    SFRelationConjointManager mgr = new SFRelationConjointManager();
                    mgr.setSession(bSession);
                    mgr.setForIdDesConjoints(cj.getIdConjoints());
                    mgr.find(transaction);
                    for (int i = 0; i < mgr.size(); i++) {
                        SFRelationConjoint rel = (SFRelationConjoint) mgr.getEntity(i);
                        BStatement stmt = new BStatement((BTransaction) transaction);
                        rel.validate(stmt);
                    }
                    if (transaction.hasErrors()) {
                        throw new Exception(transaction.getErrors().toString());
                    }
                }
            }
            transaction.commit();
            _destination = _getDestModifierSucces(session, request, response, viewBean);
        }

        catch (Exception e) {
            try {
                transaction.rollback();
            } catch (Exception e1) {
            }
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            if (JadeStringUtil.isEmpty(viewBean.getMessage())) {
                viewBean.setMessage(bSession.getLabel("ERROR_MODIFICATION_MEMBRE") + ": " + e.getMessage());
            }
            _destination = _getDestEchec(session, request, response, viewBean);
        } finally {
            try {
                transaction.closeTransaction();
            } catch (Exception e1) {
            }
            transaction = null;
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private boolean isIdAssureDejaPresentDansMembreFamille(String idAssure, String csDomaineApplication,
            BSession bSession, BITransaction transaction) throws Exception {
        SFMembreFamille membre = new SFMembreFamille();
        membre.setSession(bSession);
        membre.setIdTiers(idAssure);
        membre.setCsDomaineApplication(csDomaineApplication);
        membre.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
        membre.retrieve(transaction);

        if (JadeStringUtil.isBlankOrZero(membre.getIdMembreFamille())) {
            return false;
        }

        return true;
    }

    /**
     * Appele lorsque l'on souhaite retourner vers l'application appelante
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     */
    protected void actionQuitterApplication(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws ServletException, IOException {
        // Enlève l'url de la session et le requérant DTO
        String destination = (String) session.getAttribute(SFSessionDataContainerHelper.KEY_VALEUR_RETOUR);
        session.removeAttribute(SFSessionDataContainerHelper.KEY_VALEUR_RETOUR);
        session.removeAttribute(SFSessionDataContainerHelper.KEY_REQUERANT_DTO);

        goSendRedirect(destination, request, response);
        // servlet.getServletContext().getRequestDispatcher(destination).forward(request,
        // response);
    }

    /**
     * Crée le réquerant s'il n'existe pas, puis le place en session
     */
    protected void actionSelectionnerRequerant(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = new String();
        BSession bSession = (BSession) mainDispatcher.getSession();
        BITransaction transaction = null;
        try {

            transaction = bSession.newTransaction();
            String idMembreFamille = request.getParameter("selectedId");

            if (JadeStringUtil.isIntegerEmpty(idMembreFamille)) {
                throw new Exception(bSession.getLabel("ERROR_ID_MEMBRE_FAMILLE_NON_RENSEIGNE"));
            }

            // interdiction de sélectionner le conjoint inconnu
            if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(idMembreFamille)) {
                throw new Exception(bSession.getLabel("JSP_MEMBRE_FAMILLE_CONJOINT_INCONNU_NO_SELECT"));
            }
            // On recherche le membre de famille pour voir s'il a un numéro avs
            SFMembreFamille membre = new SFMembreFamille();
            membre.setSession(bSession);
            membre.setIdMembreFamille(idMembreFamille);
            membre.retrieve(transaction);
            // si l'on arrive pas à retrouver le membre, on renvoie un message
            // d'erreur
            if (membre.isNew()) {
                throw new Exception(bSession.getLabel("JSP_MEMBRE_FAMILLE_NO_MEMBRE"));
            }
            // si le numéro avs n'est pas renseigné, on renvoie un message
            // d'erreur
            if (JadeStringUtil.isIntegerEmpty(membre.getNss())) {
                throw new Exception(bSession.getLabel("JSP_MEMBRE_FAMILLE_NO_NSS"));
            }

            try {
                retrieveRequerant(session, bSession, transaction, idMembreFamille, membre.getCsDomaineApplication());
            } catch (Exception e) {
                // bz-5193
                // Requérant not found... on va le créer
                selectAndOrAddRequerant(session, bSession, transaction, idMembreFamille,
                        ISFSituationFamiliale.CS_DOMAINE_STANDARD, ISFSituationFamiliale.CS_DOMAINE_STANDARD);
            }

            // destination = "/hera?userAction=" +
            // ISFActions.ACTION_APERCU_RELATION_CONJOINT+ '.' +
            // FWAction.ACTION_CHERCHER;
            destination = "/hera?userAction=" + ISFActions.ACTION_VUE_GLOBALE + ".afficherFamilleRequerant";

            transaction.commit();
        } catch (Exception e) {
            // les erreurs sont renvoyées à l'utilisateur
            try {
                transaction.rollback();
            } catch (Exception e1) {
            }
            // destination = "/hera?userAction=" +
            // ISFActions.ACTION_APERCU_RELATION_CONJOINT + '.' +
            // FWAction.ACTION_CHERCHER + "&message=" + e.getMessage();
            destination = "/hera?userAction=" + ISFActions.ACTION_VUE_GLOBALE + ".afficherFamilleRequerant&message="
                    + e.getMessage();

            // JadeLogger.warn("SFApercuRelationFamilialeAction.ActionSelectionnerRequerant()",
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

    /*
     * Méthode interne
     */
    private SFRequerant addRequerant(BSession bSession, BTransaction transaction, String idDomaine,
            String idMembreFamille) throws Exception {

        // On cré le requérant
        SFRequerant requerant = new SFRequerant();
        requerant.setSession(bSession);
        requerant.setAlternateKey(SFRequerant.ALT_KEY_DOMAINE_MEMBRE);
        requerant.setIdDomaineApplication(idDomaine);
        requerant.setIdMembreFamille(idMembreFamille);
        requerant.retrieve(transaction);
        if (requerant.isNew()) {
            requerant.add(transaction);
            SFRequerantHelper rh = new SFRequerantHelper();
            rh.throwExceptionIfError(transaction, requerant);
        }
        return requerant;
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
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        SFApercuRelationFamilialeRequerantManager relation = (SFApercuRelationFamilialeRequerantManager) viewBean;

        SFRequerantHelper rh = new SFRequerantHelper();
        if (relation.getWantFamilleRequerant().booleanValue()) {
            SFRequerantDTO reqDTO = rh.getRequerantDTO(session);
            reqDTO.setWantFamille(true);

            if (reqDTO != null) {
                String idRequerant = reqDTO.getIdRequerant();
                relation.setForIdRequerant(idRequerant);
            }
        } else {
            SFRequerantDTO reqDTO = rh.getRequerantDTO(session);

            if (reqDTO == null) {
                reqDTO = new SFRequerantDTO();
                reqDTO.setWantFamille(false);
            } else {
                reqDTO.setWantFamille(false);
            }

        }

        return viewBean;
    }

    // Méthode interne pour sélectionner un requerant
    protected void retrieveRequerant(HttpSession session, BSession bSession, BITransaction transaction,
            String idMembreFamille, String csDomaineForSelection) throws Exception {

        SFRequerantHelper rh = new SFRequerantHelper();
        SFApercuRequerant requerant = new SFApercuRequerant();
        requerant.setISession(bSession);
        requerant.setAlternateKey(SFApercuRequerant.ALT_KEY_IDMEMBRE);
        requerant.setIdMembreFamille(idMembreFamille);
        String idDomaine = csDomaineForSelection;
        if (idDomaine == null) {
            idDomaine = rh.getDomaine(session);
        }

        requerant.setIdDomaineApplication(idDomaine);
        requerant.retrieve(transaction);

        // Si le requérant n'a pas été trouvé, on essaie de la récupéré avec le
        // domaine standard.
        if (requerant.isNew() && !ISFSituationFamiliale.CS_DOMAINE_STANDARD.equals(idDomaine)) {
            requerant.setIdDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
            requerant.retrieve(transaction);
        }

        if (requerant.isNew()) {
            throw new Exception(bSession.getLabel("ERROR_REQUERANT_INEXISTANT"));
        } else {
            // Instancie le DT du nouveau requerant
            SFRequerantDTO reqDTO = new SFRequerantDTO(requerant);
            // Le place dans la session
            SFSessionDataContainerHelper.setData(session, SFSessionDataContainerHelper.KEY_REQUERANT_DTO, reqDTO);
        }
    }

    // Méthode interne pour sélectionner un requerant
    protected void selectAndOrAddRequerant(HttpSession session, BSession bSession, BITransaction transaction,
            String idMembreFamille, String csDomaineForSelection, String csDomaineForCreation) throws Exception {

        SFRequerantHelper rh = new SFRequerantHelper();
        SFApercuRequerant requerant = new SFApercuRequerant();
        requerant.setISession(bSession);
        requerant.setAlternateKey(SFApercuRequerant.ALT_KEY_IDMEMBRE);
        requerant.setIdMembreFamille(idMembreFamille);
        String idDomaine = csDomaineForSelection;
        if (idDomaine == null) {
            idDomaine = rh.getDomaine(session);
        }

        requerant.setIdDomaineApplication(idDomaine);
        requerant.retrieve(transaction);

        // Si le requérant n'a pas été trouvé, on essaie de la récupéré avec le
        // domaine standard.
        if (requerant.isNew() && !ISFSituationFamiliale.CS_DOMAINE_STANDARD.equals(idDomaine)) {
            requerant.setIdDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
            requerant.retrieve(transaction);
        }

        // Si le membre n'existe pas encore dans la table de requerant,
        if (requerant.isNew()) {
            // On crée le requérant
            SFRequerant req = new SFRequerant();
            req.setISession(bSession);
            // Par défaut, le membre de famille est toujours créé pour
            // le domaine standard.
            req.setIdDomaineApplication(csDomaineForCreation);
            // req.setIdDomaineApplication(idDomaine);

            req.setIdMembreFamille(idMembreFamille);
            req.add(transaction);
            rh.throwExceptionIfError((BTransaction) transaction, req);

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
                    rh.throwExceptionIfError((BTransaction) transaction, relConj);
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
                        rh.throwExceptionIfError((BTransaction) transaction, relEnf);
                    }
                }

            }

            requerant = new SFApercuRequerant();
            requerant.setISession(bSession);
            requerant.setIdRequerant(req.getIdRequerant());
            requerant.retrieve(transaction);

            // Instancie le DT du nouveau requerant
            SFRequerantDTO reqDTO = new SFRequerantDTO(requerant);
            // Le place dans la session
            SFSessionDataContainerHelper.setData(session, SFSessionDataContainerHelper.KEY_REQUERANT_DTO, reqDTO);

        } else {
            // Instancie le DT du nouveau requerant
            SFRequerantDTO reqDTO = new SFRequerantDTO(requerant);
            // Le place dans la session
            SFSessionDataContainerHelper.setData(session, SFSessionDataContainerHelper.KEY_REQUERANT_DTO, reqDTO);
        }
    }
}
