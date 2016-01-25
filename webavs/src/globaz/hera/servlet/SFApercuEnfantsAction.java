/*
 * Créé le 19 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.servlets.FWServlet;
import globaz.fweb.taglib.FWSelectorTag;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFApercuEnfant;
import globaz.hera.db.famille.SFConjoint;
import globaz.hera.db.famille.SFConjointManager;
import globaz.hera.db.famille.SFEnfant;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFPeriode;
import globaz.hera.db.famille.SFRelationConjoint;
import globaz.hera.db.famille.SFRelationConjointManager;
import globaz.hera.db.famille.SFRequerantDTO;
import globaz.hera.enums.TypeDeDetenteur;
import globaz.hera.helpers.famille.SFPeriodeHelper;
import globaz.hera.helpers.famille.SFRequerantHelper;
import globaz.hera.interfaces.tiers.SFTiersHelper;
import globaz.hera.interfaces.tiers.SFTiersWrapper;
import globaz.hera.tools.SFSessionDataContainerHelper;
import globaz.hera.tools.nss.SFUtil;
import globaz.hera.vb.famille.SFApercuEnfantsViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
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
public class SFApercuEnfantsAction extends SFDefaultAction {

    // private static final String VERS_ECRAN_RC = "_rc.jsp";
    private static final String VERS_ECRAN_DE_ADD = "_de.jsp?" + SFDefaultAction.METHOD_ADD;
    private static final String VERS_ECRAN_DE_UPD = "_de.jsp?" + SFDefaultAction.METHOD_UPD;

    // private static final String VERS_ECRAN_DE_NEW = "_de.jsp?" + VALID_NEW;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe SFApercuEnfantsAction.
     * 
     * @param servlet
     */
    public SFApercuEnfantsAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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
            ((SFApercuEnfantsViewBean) viewBean).setRetourDepuisPyxis(false); // pour
            // la
            // prochaine
            // fois

            if (((SFApercuEnfantsViewBean) viewBean).isNew()) {
                forward(getRelativeURL(request, session) + SFApercuEnfantsAction.VERS_ECRAN_DE_ADD, request, response);
            } else {
                forward(getRelativeURL(request, session) + SFApercuEnfantsAction.VERS_ECRAN_DE_UPD, request, response);
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
     * @param request
     * @param response
     * @param dispatcher
     */
    protected void actionAjouterEnfant(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        BSession bSession = (BSession) dispatcher.getSession();
        BITransaction transaction = null;
        String _destination = null;
        try {
            SFRequerantHelper rh = new SFRequerantHelper();
            transaction = bSession.newTransaction();

            String idConjoints = rh.parseRequest(request, "idConjoints", "");
            String dateAdoption = rh.parseRequest(request, "dateAdo", "");
            String numAvs = rh.parseRequest(request, "nss", "");
            String nom = rh.parseRequest(request, "nom", "");
            String prenom = rh.parseRequest(request, "prenom", "");
            String sexe = rh.parseRequest(request, "csSexe", "");
            String dateNaissance = rh.parseRequest(request, "dateNaissance", "");
            String dateDeces = rh.parseRequest(request, "dateDeces", "");
            String nationalite = rh.parseRequest(request, "csNationalite", "");
            String cantonDomicile = rh.parseRequest(request, "csCantonDomicile", "");
            String idAssure = rh.parseRequest(request, "idAssure", null);
            String provenance = rh.parseRequest(request, "provenance", null);
            String codePays = rh.parseRequest(request, "pays", null);
            String idTiers = null;
            if (SFUtil.PROVENANCE_TIERS.equals(provenance)) {
                idTiers = idAssure;
            }

            SFRequerantDTO requerant = (SFRequerantDTO) SFSessionDataContainerHelper.getData(session,
                    SFSessionDataContainerHelper.KEY_REQUERANT_DTO);
            SFMembreFamille membre = rh.ajouterMembre(idTiers, numAvs, nom, prenom, sexe, dateNaissance, dateDeces,
                    nationalite, cantonDomicile, requerant.getIdDomaineApplication(), codePays,
                    (BSession) dispatcher.getSession(), transaction);
            if (FWViewBeanInterface.ERROR.equals(membre.getMsgType())) {
                throw new Exception(membre.getMessage());
            }

            try {
                rh.throwExceptionIfError((BTransaction) transaction, membre);
                membre.retrieve(transaction);
            } catch (Exception e) {
                throw new Exception(bSession.getLabel("ERROR_ADD_MEMBRE") + ": " + e.getMessage());
            }
            if ((membre == null) || membre.isNew()) {
                throw new Exception(bSession.getLabel("ERROR_ADD_MEMBRE"));
            }

            // On ajoute l'enfant aux conjoints
            SFEnfant enfant = new SFEnfant();
            enfant.setSession(bSession);
            enfant.setIdConjoint(idConjoints);
            enfant.setIdMembreFamille(membre.getIdMembreFamille());
            if (dateAdoption != null) {
                enfant.setDateAdoption(dateAdoption);
            }
            enfant.setAlternateKey(SFEnfant.ALTERNATE_KEY_IDMEMBREFAMILLE);
            enfant.retrieve(transaction);
            if (!enfant.isNew()) {
                throw new Exception(bSession.getLabel("ERROR_ENFANT_EXISTANT"));
            }
            enfant.add(transaction);
            rh.throwExceptionIfError((BTransaction) transaction, enfant);

            SFApercuEnfant apEnfant = new SFApercuEnfant();
            apEnfant.setSession(bSession);
            apEnfant.setIdEnfant(enfant.getIdEnfant());
            apEnfant.setAlternateKey(SFApercuEnfant.ALT_KEY_IDENFANT);
            apEnfant.retrieve(transaction);

            // cherche les parents
            SFConjoint conjoint = enfant.getConjoints(transaction);
            String idMembreConjoint = conjoint.getIdMembreFamilleConjoint(requerant.getIdMembreFamille());

            // On ajoute les périodes BTE des relations de divorce des parents
            SFRelationConjointManager relMgr = new SFRelationConjointManager();
            relMgr.setSession(bSession);
            relMgr.setForIdDesConjoints(idConjoints);
            relMgr.setForTypeRelation(ISFSituationFamiliale.CS_REL_CONJ_DIVORCE);
            relMgr.setFromDateDebut(apEnfant.getDateNaissance());
            if (!JAUtil.isDateEmpty(apEnfant.getDateDeces())) {
                relMgr.setUntilDateDebut(apEnfant.getDateDeces());
            }
            relMgr.find(transaction);
            for (Iterator it = relMgr.iterator(); it.hasNext();) {
                SFRelationConjoint rel = (SFRelationConjoint) it.next();
                SFPeriode periode = new SFPeriode();
                periode.setSession(bSession);
                periode.setIdMembreFamille(enfant.getIdMembreFamille());
                periode.setType(ISFSituationFamiliale.CS_TYPE_PERIODE_GARDE_BTE);
                periode.setDateDebut(SFPeriodeHelper.getDebutPeriode(bSession, rel.getDateDebut(),
                        apEnfant.getDateNaissance()));
                periode.setDateFin(SFPeriodeHelper.getFinPeriode(bSession, rel.getDateFin(), apEnfant.getDateDeces()));
                if (!ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(conjoint.getIdConjoint1())) {
                    periode.setIdDetenteurBTE(conjoint.getIdConjoint1());
                    periode.setCsTypeDeDetenteur(TypeDeDetenteur.FAMILLE.getCodeSystemAsString());
                    periode.add(transaction);
                    rh.throwExceptionIfError((BTransaction) transaction, periode);
                }

                if (!ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(conjoint.getIdConjoint2())) {
                    periode.setIdDetenteurBTE(conjoint.getIdConjoint2());
                    periode.setCsTypeDeDetenteur(TypeDeDetenteur.FAMILLE.getCodeSystemAsString());
                    periode.add(transaction);
                    rh.throwExceptionIfError((BTransaction) transaction, periode);
                }
            }

            // On ajoute l'enfant à la famille du requérant
            rh.updateRelationRequerant(requerant.getIdRequerant(), membre.getIdMembreFamille(), true, bSession,
                    transaction);
            // Ajoute l'enfant à la famille de l'autre parent s'il est requérant
            rh.updateRelationMembreRequerant(idMembreConjoint, membre.getIdMembreFamille(), true, bSession, transaction);
            transaction.commit();
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e1) {
            try {
                transaction.rollback();
            } catch (Exception e2) {
            }
            // JadeLogger.warn("SFApercuRelationConjointAction : ajouterRelation()",
            // e1);
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(bSession.getLabel("ERROR_ADD_ENFANT") + ": " + e1.getMessage());
            try {
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            } catch (Exception e) {
            }
            ((SFApercuEnfantsViewBean) viewBean).setNss(request.getParameter("likeNSS"));
            _destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail";
        } finally {
            try {
                transaction.closeTransaction();
            } catch (Exception e2) {
            }
            transaction = null;
        }

        // on redirige
        // goSendRedirect(_destination, request, response);

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

        String _destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                new FWRequestActionAdapter().adapt(request), null);
        // On cherche les enfants sur la base de l'idConjoints et non de
        // l'idRelationConjoint
        // --> on recherche idConjoints
        String idRelationConjoint = request.getParameter("idRelationConjoint");
        BSession bSession = (BSession) mainDispatcher.getSession();
        SFRelationConjoint relation = new SFRelationConjoint();
        relation.setSession(bSession);
        relation.setIdRelationConjoint(idRelationConjoint);
        try {
            relation.retrieve();
            if (relation.isNew()) {
                throw new Exception();
            }
            String idConjoints = relation.getIdConjoints();
            if (isRetourDepuisPyxis(viewBean)) {
                _destination = getRelativeURL(request, session) + "_de.jsp?_method=add&idConjoints=" + idConjoints;
            } else {
                _destination = getRelativeURL(request, session) + "_rc.jsp?idConjoints=" + idConjoints;
            }

        } catch (Exception e) {
            if (isRetourDepuisPyxis(viewBean)) {
                _destination = getRelativeURL(request, session) + "_de.jsp?_method=add";
            } else {
                _destination = getRelativeURL(request, session) + "_rc.jsp";
            }
        }

        SFApercuEnfantsViewBean sfViewBean;

        if (isRetourDepuisPyxis(viewBean)) {
            ((SFApercuEnfantsViewBean) viewBean).setRetourDepuisPyxis(false);
            // on revient de pyxis, on recupere le viewBean en session pour
            // afficher les donnees dans la page rc.
            sfViewBean = (SFApercuEnfantsViewBean) viewBean;

            // mainDispatcher.dispatch(sfViewBean, getAction());

            this.saveViewBean(sfViewBean, request);
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

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

        if ("ajouterEnfant".equals(actionPart)) {
            actionAjouterEnfant(session, request, response, dispatcher);
        } else if ("supprimerEnfant".equals(actionPart)) {
            actionSupprimerEnfant(session, request, response, dispatcher);
        } else {
            // on a demandé un page qui n'existe pas
            servlet.getServletContext().getRequestDispatcher(FWDefaultServletAction.UNDER_CONSTRUCTION_PAGE)
                    .forward(request, response);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionModifier(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination = "";
        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
        BSession bSession = (BSession) mainDispatcher.getSession();
        BITransaction transaction = null;
        try {
            transaction = bSession.newTransaction();

            // 1. Mise-à-jour de l'enfant
            // Il faut setter la date d'adoption, car elle ne se trouve pas dans
            // la table des membres de famille
            SFRequerantHelper rh = new SFRequerantHelper();
            String idMembreFamille = rh.parseRequest(request, "idMembreFamille");
            String idEnfant = rh.parseRequest(request, "idEnfant");
            String dateAdoption = rh.parseRequest(request, "dateAdo");
            SFEnfant enfant = new SFEnfant();
            enfant.setSession(bSession);
            enfant.setIdEnfant(idEnfant);
            enfant.retrieve(transaction);
            enfant.setDateAdoption(dateAdoption);
            enfant.update(transaction);

            // 2. Mise-à-jour du membre
            SFMembreFamille membre = new SFMembreFamille();
            membre.setSession(bSession);
            membre.setIdMembreFamille(idMembreFamille);
            membre.retrieve(transaction);
            if (membre.isNew()) {
                throw new Exception(bSession.getLabel("ERROR_MEMBRE_NOT_FOUND"));
            }

            String idAssure = rh.parseRequest(request, "idAssure", null);
            if ("null".equals(idAssure)) {
                idAssure = null;
            }

            String provenance = rh.parseRequest(request, "provenance", null);

            String numAvs = request.getParameter("nss");
            String nom = rh.parseRequest(request, "nom", "");
            String prenom = rh.parseRequest(request, "prenom", "");
            String sexe = rh.parseRequest(request, "csSexe", "");
            String dateNaissance = rh.parseRequest(request, "dateNaissance", "");
            String dateDeces = rh.parseRequest(request, "dateDeces", "");
            String nationalite = rh.parseRequest(request, "csNationalite", "");
            String cantonDomicile = rh.parseRequest(request, "csCantonDomicile", "");
            String codePays = rh.parseRequest(request, "pays", null);

            if (!JadeStringUtil.isEmpty(numAvs)) {
                // Est-ce que le tiers existe dans les tiers ??
                SFTiersWrapper tiers = SFTiersHelper.getTiers(mainDispatcher.getSession(), numAvs);

                if (tiers != null) {
                    idAssure = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
                    provenance = SFUtil.PROVENANCE_TIERS;
                }
            }

            rh.modifierMembre(rh.getRequerantDTO(session), true, membre, idAssure, provenance, numAvs, nom, prenom,
                    sexe, dateNaissance, dateDeces, nationalite, codePays, cantonDomicile, "", "", bSession,
                    transaction);

            membre.retrieve(transaction);
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
            _destination = "/hera?userAction=" + ISFActions.ACTION_ENFANTS + '.' + FWAction.ACTION_CHERCHER;
        }

        catch (Exception e) {
            try {
                transaction.rollback();
            } catch (Exception e1) {
            }
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            if (JadeStringUtil.isEmpty(viewBean.getMessage())) {
                viewBean.setMessage(bSession.getLabel("ERROR_MODIFIER_ENFANT") + ": " + e.getMessage());
            }
            _destination = _getDestAjouterEchec(session, request, response, viewBean);
        } finally {
            try {
                transaction.closeTransaction();
            } catch (Exception e1) {
            }
            transaction = null;
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
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
        // SFApercuEnfantsViewBean viewBean = (SFApercuEnfantsViewBean)
        // loadViewBean(session);
        StringBuffer queryString = new StringBuffer();

        queryString.append(SFDefaultAction.USER_ACTION);
        queryString.append("=");
        queryString.append(ISFActions.ACTION_ENFANTS);
        queryString.append(".");
        queryString.append(FWAction.ACTION_CHERCHER);
        queryString.append("&idConjoints=");
        queryString.append(request.getParameter("idConjoints"));

        // HACK: on remplace une des valeurs sauvee en session par FWSelectorTag
        session.setAttribute(FWDefaultServletAction.ATTRIBUT_SELECTOR_CUSTOMERURL, queryString.toString());

        // comportement par defaut
        super.actionSelectionner(session, request, response, mainDispatcher);
    }

    /**
     * Supprime un enfant d'entre les conjoints
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     */
    protected void actionSupprimerEnfant(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String _destination;
        BSession bSession = (BSession) dispatcher.getSession();
        BITransaction transaction = null;
        SFRequerantHelper rh = new SFRequerantHelper();
        try {
            transaction = bSession.newTransaction();
            String idMembreFamille = rh.parseRequest(request, "idMembreFamille");
            String idEnfant = rh.parseRequest(request, "idEnfant");

            // Recherche le requerant
            SFRequerantDTO requerant = (SFRequerantDTO) SFSessionDataContainerHelper.getData(session,
                    SFSessionDataContainerHelper.KEY_REQUERANT_DTO);
            // String idRequerant = requerant.getIdRequerant();

            // On supprime l'enfant
            SFEnfant enfant = new SFEnfant();
            enfant.setSession(bSession);
            enfant.setIdEnfant(idEnfant);
            enfant.retrieve(transaction);
            // String idConjoints = enfant.getIdConjoint();
            enfant.delete(transaction); // ses périodes sont égallement
            // suppriméee

            // recherche les parents
            SFConjoint conjoint = enfant.getConjoints(transaction);
            String idMembreFamilleConjoint = conjoint.getIdMembreFamilleConjoint(requerant.getIdMembreFamille());

            // On supprime la relation de l'enfant avec le requerant
            rh.updateRelationRequerant(requerant.getIdRequerant(), idMembreFamille, false, bSession, transaction);
            rh.updateRelationMembreRequerant(idMembreFamilleConjoint, idMembreFamille, false, bSession, transaction);
            transaction.commit();

            // on redirige
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e1) {
            try {
                transaction.rollback();
            } catch (Exception e2) {
            }
            JadeLogger.warn("SFApercuEnfantAction.supprimerEnfant()", e1);
            _destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail";
        } finally {
            try {
                transaction.closeTransaction();
            } catch (Exception e2) {
                transaction = null;
            }
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    /**
     * inspecte le viewBean et retourne vrai si celui-ci indique que l'on revient de pyxis.
     * 
     * @param viewBean
     * 
     * @return
     */
    private boolean isRetourDepuisPyxis(FWViewBeanInterface viewBean) {
        return ((viewBean != null) && (viewBean instanceof SFApercuEnfantsViewBean) && ((SFApercuEnfantsViewBean) viewBean)
                .isRetourDepuisPyxis());
    }

}
