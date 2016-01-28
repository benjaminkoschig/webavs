/*
 * Cr�� le 18 juil. 07
 */
package globaz.corvus.helpers.process;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.REDecisionsManager;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.demandes.REDemandeRenteJointPrestationAccordee;
import globaz.corvus.db.demandes.REDemandeRenteJointPrestationAccordeeManager;
import globaz.corvus.db.ordresversements.RECompensationInterDecisions;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.prestations.REPrestationsManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.process.REImprimerDecisionProcess;
import globaz.corvus.utils.decisions.REDecisionsUtil;
import globaz.corvus.vb.decisions.REDecisionsListViewBean;
import globaz.corvus.vb.decisions.REDecisionsViewBean;
import globaz.corvus.vb.decisions.REPreValiderDecisionViewBean;
import globaz.corvus.vb.process.REValiderDecisionsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.utils.SFFamilleUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author SCR
 */
public class REValiderDecisionsHelper extends PRAbstractHelper {
    /**
     * 20.09.2012 LGA Utilis� pour contr�ler l'�tat avant et apr�s traitement de la demande
     */
    private String etatDemandeAvantraitement;

    /**
     * 20.09.2012 LGA Utilis� pour contr�ler l'�tat avant et apr�s traitement des d�cisions qui d�coulent de la demande
     */
    private HashMap<String, String> mapDecisionSousSurveillance;

    public REValiderDecisionsHelper() {
        etatDemandeAvantraitement = null;
        mapDecisionSousSurveillance = new HashMap<String, String>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        // Recherche la d�cision en fonction de son id
        String idDecision = ((REValiderDecisionsViewBean) viewBean).getIdDecision();
        REDecisionEntity decision = new REDecisionEntity();
        decision.setSession((BSession) session);
        decision.setIdDecision(idDecision);
        decision.retrieve();

        if (decision.isNew()) {
            throw new Exception("Unable to retrieve Decision - idDecision = " + idDecision);
        }

        String idDemandeRente = decision.getIdDemandeRente();
        REDemandeRenteJointDemande dem = new REDemandeRenteJointDemande();
        dem.setSession((BSession) session);
        dem.setIdDemandeRente(idDemandeRente);
        dem.retrieve();

        if (dem.isNew()) {
            throw new Exception("Unable to retrieve Demande - idDecision/idDemande = " + idDecision + " / "
                    + idDemandeRente);
        }

        ((REValiderDecisionsViewBean) viewBean).setRequerantInfo(dem.getNoAVS() + " - " + dem.getNom() + " "
                + dem.getPrenom() + " (" + dem.getDateNaissance() + " / " + session.getCodeLibelle(dem.getCsSexe())
                + ")");

        ((REValiderDecisionsViewBean) viewBean).setIdTiersRequerant(dem.getIdTiersRequerant());
        ((REValiderDecisionsViewBean) viewBean).setIdDemandeRente(dem.getIdDemandeRente());

        // Regrouper dans le viewBean toutes les d�cisions de la demande de
        // rentes pour affichage dans l'�cran de validation
        REDecisionsListViewBean mgr = new REDecisionsListViewBean();
        mgr.setSession((BSession) session);
        mgr.setForIdDemandeRente(dem.getIdDemandeRente());
        mgr.find(BManager.SIZE_NOLIMIT);

        // Pour chacune des d�cisions de la demande de rente, il faut r�cup�rer
        // les d�cisions li�es par CID.

        // N�cessaire de clear� la liste car dans le cas de la pr�-validation
        // d'une d�cision,
        // Lors du r�-affichage de l'�cran, les d�cisions sont toujours dans la
        // liste, et l'�tat
        // n'est donc pas mise � jour � l'�cran, car pas eu de rechargement.
        ((REValiderDecisionsViewBean) viewBean).getDecisionsList().clear();

        for (int i = 0; i < mgr.size(); i++) {

            REDecisionsViewBean dvb = (REDecisionsViewBean) mgr.getEntity(i);

            if (!((REValiderDecisionsViewBean) viewBean).getDecisionsList().contains(dvb)) {
                ((REValiderDecisionsViewBean) viewBean).addDecision(dvb);
            }

            // R�cup�ration de la prestation accord�e pour retrouver l'id du Lot
            REPrestationsManager prestationsManager = new REPrestationsManager();
            prestationsManager.setSession((BSession) session);
            prestationsManager.setForIdDecision(dvb.getIdDecision());
            prestationsManager.find();
            REPrestations prestation = (REPrestations) prestationsManager.getFirstEntity();
            if (prestation != null) {
                dvb.setIdLot(prestation.getIdLot());
            }

            REPrestationsManager mgr2 = new REPrestationsManager();
            mgr2.setSession((BSession) session);
            mgr2.setForIdDecision(dvb.getIdDecision());
            mgr2.find();
            if (mgr2.isEmpty()) {
                throw new Exception("Aucune prestation trouv�e pour la d�cision no : " + dvb.getIdDecision());
            }
            REPrestations prst = (REPrestations) mgr2.getFirstEntity();
            REOrdresVersements[] ovs = prst.getOrdresVersement(null);
            for (int j = 0; j < ovs.length; j++) {
                REOrdresVersements ov = ovs[j];
                if (IREOrdresVersements.CS_TYPE_DETTE.equals(ov.getCsType())) {

                    if ((ov.getIsCompensationInterDecision() != null)
                            && ov.getIsCompensationInterDecision().booleanValue()) {
                        // Recherche de la decision de base
                        RECompensationInterDecisions cid = new RECompensationInterDecisions();
                        cid.setSession((BSession) session);
                        cid.setIdOVCompensation(ov.getIdOrdreVersement());
                        cid.setAlternateKey(RECompensationInterDecisions.ALTERNATE_KEY_ID_OV_COMPENSATION);
                        cid.retrieve();
                        PRAssert.notIsNew(cid, null);

                        REOrdresVersements ovSrc = new REOrdresVersements();
                        ovSrc.setSession((BSession) session);
                        ovSrc.setIdOrdreVersement(cid.getIdOrdreVersement());
                        ovSrc.retrieve();
                        PRAssert.notIsNew(ovSrc, null);

                        REPrestations prstSrc = new REPrestations();
                        prstSrc.setSession((BSession) session);
                        prstSrc.setIdPrestation(ovSrc.getIdPrestation());
                        prstSrc.retrieve();
                        PRAssert.notIsNew(prstSrc, null);

                        REDecisionsViewBean dvbSrc = new REDecisionsViewBean();
                        dvbSrc.setSession((BSession) session);
                        dvbSrc.setIdDecision(prstSrc.getIdDecision());
                        dvbSrc.retrieve();

                        if (!((REValiderDecisionsViewBean) viewBean).getDecisionsList().contains(dvbSrc)) {
                            ((REValiderDecisionsViewBean) viewBean).addDecision(dvbSrc);
                        }
                    } else {
                        // Recherche de la CID
                        RECompensationInterDecisions cid = new RECompensationInterDecisions();
                        cid.setSession((BSession) session);
                        cid.setIdOrdreVersement(ov.getIdOrdreVersement());
                        cid.setAlternateKey(RECompensationInterDecisions.ALTERNATE_KEY_ID_OV);
                        cid.retrieve();
                        // Une CID a �t� trouv�e...
                        if (!cid.isNew()) {

                            REOrdresVersements ovDeLaCID = new REOrdresVersements();
                            ovDeLaCID.setSession((BSession) session);
                            ovDeLaCID.setIdOrdreVersement(cid.getIdOVCompensation());
                            ovDeLaCID.retrieve();
                            PRAssert.notIsNew(ovDeLaCID, "86001 : Incoh�rance dans les donn�es");

                            REPrestations prstDeLaCID = new REPrestations();
                            prstDeLaCID.setSession((BSession) session);
                            prstDeLaCID.setIdPrestation(ovDeLaCID.getIdPrestation());
                            prstDeLaCID.retrieve();
                            PRAssert.notIsNew(prstDeLaCID, "86002 : Incoh�rance dans les donn�es");

                            REDecisionsViewBean dvbCID = new REDecisionsViewBean();
                            dvbCID.setSession((BSession) session);
                            dvbCID.setIdDecision(prstDeLaCID.getIdDecision());
                            dvbCID.retrieve();

                            // // TODO HERE
                            // // R�cup�ration de la prestation accord�e pour retrouver l'id du Lot
                            // REPrestationsManager prestationsManager = new REPrestationsManager();
                            // prestationsManager.setSession((BSession) session);
                            // prestationsManager.setForIdDecision(idDecision);
                            // prestationsManager.find();
                            // REPrestations prestation = (REPrestations) prestationsManager.getFirstEntity();
                            // dvbCID.setIdLot(prestation.getIdLot());

                            if (!((REValiderDecisionsViewBean) viewBean).getDecisionsList().contains(dvbCID)) {
                                ((REValiderDecisionsViewBean) viewBean).addDecision(dvbCID);
                            }
                        }
                    }
                }
            }
        }
        // ((REValiderDecisionsViewBean)viewBean).setDecisionsList(mgr.getContainer());
    }

    public FWViewBeanInterface actionValiderDirect(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        BITransaction transaction = null;
        try {
            transaction = (session).newTransaction();
            transaction.openTransaction();

            REPreValiderDecisionViewBean vb = (REPreValiderDecisionViewBean) viewBean;

            REDecisionsViewBean decisionVb = new REDecisionsViewBean();
            decisionVb.setIdDecision(vb.getIdDecision());
            decisionVb.setSession(session);
            decisionVb.retrieve();

            if (!decisionVb.isDateDecisionInferieureMoisPaiement().booleanValue()) {
                transaction.addErrors(session.getLabel("JSP_VAL_D_TEXTE_1"));
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

            // Si d�cision en attente, faire le traitement de pr�validation
            if (vb.getDecision().getCsEtat().equals(IREDecision.CS_ETAT_ATTENTE)) {

                REDecisionsUtil du = new REDecisionsUtil();
                du.enregistrerModifs(vb, action, session, transaction);

                // Mise � jour de la d�cision
                REDecisionEntity decision = new REDecisionEntity();
                decision.setIdDecision(vb.getIdDecision());
                decision.setSession(session);
                decision.retrieve(transaction);

                if (decision.isNew()) {
                    throw new Exception("D�cision introuvale");
                }

                decision.setCsEtat(IREDecision.CS_ETAT_PREVALIDE);
                decision.update(transaction);

                // Lancer l'impression de la d�cision
                REImprimerDecisionProcess imprimerDecision = new REImprimerDecisionProcess();
                imprimerDecision.setSession(session);
                imprimerDecision.setIdDecision(decision.getIdDecision());
                imprimerDecision.setIdDemandeRente(decision.getIdDemandeRente());
                imprimerDecision.setDateDocument(decision.getDatePreparation());
                imprimerDecision.setEMailAddress(decision.getAdresseEMail());

                BProcessLauncher.start(imprimerDecision, false);

                vb.setDocumentsPreview(null);

            }

            // Faire le traitement de validation
            REValiderDecisionsViewBean validerVb = new REValiderDecisionsViewBean();
            validerVb.setIdDemandeRente(vb.getIdDemandeRente());
            validerVb.setIdDecision(vb.getIdDecision());
            validerVb.setIdTiersRequerant(vb.getIdTiersRequerant());

            tttValider(session, validerVb, (BTransaction) transaction);

            return vb;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }

            viewBean.setMessage("Error " + e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            return viewBean;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        if (transaction.hasErrors()) {
                            viewBean.setMessage(transaction.getErrors().toString());
                            viewBean.setMsgType(FWViewBeanInterface.ERROR);
                        }
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

    }

    /**
     * Valide que le changement d'�tat de la demande respect la s�quence pr�vue
     * 
     * @return True si l'�volution de l'�tat de la demande est correct
     */
    private boolean checkDecisionStateEvolution(String previousState, String afterState) {
        if ((previousState == null) || (afterState == null)) {
            return false;
        }
        if (previousState.equals(afterState)) {
            return true;
        }
        if (previousState.equals(IREDecision.CS_ETAT_PREVALIDE) && (!afterState.equals(IREDecision.CS_ETAT_VALIDE))) {
            return false;
        }
        return true;
    }

    /**
     * Valide que le changement d'�tat de la d�cision respect la s�quence pr�vue
     * 
     * @return True si l'�volution de l'�tat de la d�cision est correct
     */
    private boolean checkDemandeStateEvolution(String previousState, String afterState) {
        if ((previousState == null) || (afterState == null)) {
            return false;
        }
        if (previousState.equals(afterState)) {
            return true;
        }
        if (previousState.equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE)
                && !((afterState.equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE)) || (afterState
                        .equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE)))) {
            return false;
        }
        if (previousState.equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE)
                && (!afterState.equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE))) {
            return false;
        }
        return true;
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    /**
     * (non javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    public FWViewBeanInterface executerTTT(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        REValiderDecisionsViewBean vb = (REValiderDecisionsViewBean) viewBean;

        try {

            String idDecision = vb.getDecisionATraiter();

            if (null != vb.getTypeTTT()) {

                if (vb.getTypeTTT().equals("prevalider")) {
                    // Traitement de pr�validation d'une seule d�cision

                    // Mise � jour de la d�cision
                    REDecisionEntity decision = new REDecisionEntity();
                    decision.setIdDecision(idDecision);
                    decision.setSession(session);
                    decision.retrieve();

                    if (decision.isNew()) {
                        throw new Exception("D�cision introuvable");
                    }

                    decision.setCsEtat(IREDecision.CS_ETAT_PREVALIDE);
                    decision.update();

                    // Lancer l'impression de la d�cision
                    REImprimerDecisionProcess imprimerDecision = new REImprimerDecisionProcess();
                    imprimerDecision.setSession(session);
                    imprimerDecision.setIdDecision(decision.getIdDecision());
                    imprimerDecision.setIdDemandeRente(decision.getIdDemandeRente());
                    imprimerDecision.setDateDocument(decision.getDatePreparation());
                    imprimerDecision.setEMailAddress(decision.getAdresseEMail());

                    BProcessLauncher.start(imprimerDecision, false);

                    vb.setDocumentsPreview(null);

                } else if (vb.getTypeTTT().equals("valider")) {
                    throw new Exception("Error# 54688, should never get here !!!");

                } else if (vb.getTypeTTT().equals("afficher")) {

                    throw new Exception("Error# 54678, should never get here !!!");

                } else if (vb.getTypeTTT().equals("toutValider")) {
                    // Valider toutes les d�cisions de la demande
                    tttValider(session, vb, null);

                } else {
                    throw new Exception("Functionality not implemented. Code error = 875589874");
                }

            } else {
                throw new Exception("Functionality not implemented. Code error = 8754574");
            }

        } catch (Exception e) {
            vb.setMsgType(FWViewBeanInterface.ERROR);
            vb.setMessage(e.toString());
        }

        return vb;

    }

    /**
     * R�initialise les champs de classe utilis� pour le contr�le de validation des demandes et des d�cisions
     */
    private void initControlFields() {
        etatDemandeAvantraitement = null;
        mapDecisionSousSurveillance.clear();
    }

    /**
     * Enregistre l'�tat de la demande et des d�cisions qui en d�coulent avant ex�cution du traitement dans le but de
     * contr�ler l'�tat de la demande apr�s traitement et s'assurer qu'on est coh�rent
     * 
     * @param idDemande
     *            Id de la demande
     * @param session
     *            La session � utiliser
     * @throws Exception
     *             Ben ouais �a peut arriver si on ne peut retrouver la d�cision selon son id en bd
     */
    private void storeEtatDemandeEtDecisionsAvantTraitement(String idDemande, BISession session,
            BITransaction transaction) throws Exception {
        REDemandeRente demande = new REDemandeRente();
        demande.setIdDemandeRente(idDemande);
        demande.setSession((BSession) session);
        demande.retrieve(transaction);
        etatDemandeAvantraitement = demande.getCsEtat();

        REDecisionsManager decisionManager = new REDecisionsManager();
        decisionManager.setForIdDemandeRente(demande.getIdDemandeRente());
        decisionManager.setSession((BSession) session);
        decisionManager.find(transaction);
        for (Object d : decisionManager.getContainer()) {
            REDecisionEntity decision = (REDecisionEntity) d;
            mapDecisionSousSurveillance.put(decision.getIdDecision(), decision.getCsEtat());
        }
    }

    /**
     * M�thode utilitaire 'stateless' pour la validation des d�cision. Cette m�thode peut �tre appel�e dans une boucle,
     * elle garanti qu'� chaque appel le travail sera constant et ind�pendant de l'�tat de l'instance
     * REValiderDecisionsHelper utilis� pour ces appels. Cette m�thode n'est volontairement plus static car elle
     * r�f�rence des champs de classe ATTENTION : la transation pass�e en argument sera close � la fin du traitement de
     * la d�cision !!
     * 
     * @param session
     *            La session utilis�e
     * @param vb
     *            Le viewbean
     * @param transaction
     *            La transaction � utiliser. Si aucune transaction n'est fournie, une nouvelle instance sera cr�er
     *            depuis la session
     * @throws Exception
     */
    public void tttValider(BSession session, REValiderDecisionsViewBean vb, BTransaction transaction) throws Exception {

        // On r�initialise les champs de classe utilis� pour contr�ler l'�volution des �tats de la demande et des
        // d�cisions
        initControlFields();

        // Si on re�oit une transaction en argument, on l'utilise
        BTransaction transactionExterne = null;
        try {
            if (null == transaction) {
                transactionExterne = new BTransaction(session);
                transactionExterne.openTransaction();
            } else {
                if (!transaction.isOpened()) {
                    throw new Exception(
                            "The provided transaction is not open !! Can't do the job with a closed transaction");
                } else {
                    transactionExterne = transaction;
                }
            }

            // On stock l'�tat de la demande avant traitement
            storeEtatDemandeEtDecisionsAvantTraitement(vb.getIdDemandeRente(), session, transactionExterne);

            // Retrieve de toutes les d�cisions de la demande
            REDecisionsListViewBean mgr = new REDecisionsListViewBean();
            mgr.setSession(session);
            mgr.setForIdDemandeRente(vb.getIdDemandeRente());
            mgr.find(transactionExterne, BManager.SIZE_NOLIMIT);

            // 1�re passe, on stock toute les d�cisions par demande de rente !!!!
            // N�cessaire pour mettre � jours la demande de rente 1 seul et
            // unique fois, lors de traitement de la derni�re
            // decision de la demande concern�e.
            Map<String, List<REDecisionsViewBean>> mapDecisionsParDemandeRente = new HashMap<String, List<REDecisionsViewBean>>();

            for (Iterator<REDecisionsViewBean> iterator = mgr.iterator(); iterator.hasNext();) {
                REDecisionsViewBean decision = iterator.next();

                if (mapDecisionsParDemandeRente.containsKey(decision.getIdDemandeRente())) {
                    List<REDecisionsViewBean> decisions = mapDecisionsParDemandeRente.get(decision.getIdDemandeRente());
                    if (!decisions.contains(decision)) {
                        decisions.add(decision);
                    }
                } else {
                    List<REDecisionsViewBean> decisions = new ArrayList<REDecisionsViewBean>();
                    decisions.add(decision);
                    mapDecisionsParDemandeRente.put(decision.getIdDemandeRente(), decisions);
                }

                // Pour chacune des d�cisions de la demande de rente, il faut
                // r�cup�rer les d�cisions li�es par CID.
                REPrestationsManager mgr2 = new REPrestationsManager();
                mgr2.setSession(session);
                mgr2.setForIdDecision(decision.getIdDecision());
                mgr2.find();
                if (mgr2.isEmpty()) {
                    throw new Exception("Aucune prestation trouv�e pour la d�cision no : " + decision.getIdDecision());
                }
                REPrestations prst = (REPrestations) mgr2.getFirstEntity();
                REOrdresVersements[] ovs = prst.getOrdresVersement(null);
                for (int j = 0; j < ovs.length; j++) {
                    REOrdresVersements ov = ovs[j];
                    if (IREOrdresVersements.CS_TYPE_DETTE.equals(ov.getCsType())) {

                        if ((ov.getIsCompensationInterDecision() != null)
                                && ov.getIsCompensationInterDecision().booleanValue()) {

                            // Recherche de la decision de base
                            RECompensationInterDecisions cid = new RECompensationInterDecisions();
                            cid.setSession(session);
                            cid.setIdOVCompensation(ov.getIdOrdreVersement());
                            cid.setAlternateKey(RECompensationInterDecisions.ALTERNATE_KEY_ID_OV_COMPENSATION);
                            cid.retrieve();
                            PRAssert.notIsNew(cid, null);

                            REOrdresVersements ovSrc = new REOrdresVersements();
                            ovSrc.setSession(session);
                            ovSrc.setIdOrdreVersement(cid.getIdOrdreVersement());
                            ovSrc.retrieve();
                            PRAssert.notIsNew(ovSrc, null);

                            REPrestations prstSrc = new REPrestations();
                            prstSrc.setSession(session);
                            prstSrc.setIdPrestation(ovSrc.getIdPrestation());
                            prstSrc.retrieve();
                            PRAssert.notIsNew(prstSrc, null);

                            REDecisionsViewBean dvbSrc = new REDecisionsViewBean();
                            dvbSrc.setSession(session);
                            dvbSrc.setIdDecision(prstSrc.getIdDecision());
                            dvbSrc.retrieve();

                            if (mapDecisionsParDemandeRente.containsKey(dvbSrc.getIdDemandeRente())) {
                                List<REDecisionsViewBean> decisions = mapDecisionsParDemandeRente.get(dvbSrc
                                        .getIdDemandeRente());
                                if (!decisions.contains(dvbSrc)) {
                                    decisions.add(dvbSrc);
                                }
                            } else {
                                List<REDecisionsViewBean> decisions = new ArrayList<REDecisionsViewBean>();
                                decisions.add(dvbSrc);
                                mapDecisionsParDemandeRente.put(dvbSrc.getIdDemandeRente(), decisions);
                            }

                        } else {
                            // Recherche de la CID
                            RECompensationInterDecisions cid = new RECompensationInterDecisions();
                            cid.setSession(session);
                            cid.setIdOrdreVersement(ov.getIdOrdreVersement());
                            cid.setAlternateKey(RECompensationInterDecisions.ALTERNATE_KEY_ID_OV);
                            cid.retrieve();
                            if (!cid.isNew()) {

                                REOrdresVersements ovDeLaCID = new REOrdresVersements();
                                ovDeLaCID.setSession(session);
                                ovDeLaCID.setIdOrdreVersement(cid.getIdOVCompensation());
                                ovDeLaCID.retrieve();
                                PRAssert.notIsNew(
                                        ovDeLaCID,
                                        "85001 : Incoh�rance dans les donn�es pour la d�cision no : "
                                                + decision.getIdDecision());

                                REPrestations prstDeLaCID = new REPrestations();
                                prstDeLaCID.setSession(session);
                                prstDeLaCID.setIdPrestation(ovDeLaCID.getIdPrestation());
                                prstDeLaCID.retrieve();
                                PRAssert.notIsNew(
                                        prstDeLaCID,
                                        "85002 : Incoh�rance dans les donn�es pour la d�cision no : "
                                                + decision.getIdDecision());

                                REDecisionsViewBean dvbCID = new REDecisionsViewBean();
                                dvbCID.setSession(session);
                                dvbCID.setIdDecision(prstDeLaCID.getIdDecision());
                                dvbCID.retrieve();
                                PRAssert.notIsNew(dvbCID, "85003 : Incoh�rance dans les donn�es pour la d�cision no : "
                                        + decision.getIdDecision());

                                if (mapDecisionsParDemandeRente.containsKey(dvbCID.getIdDemandeRente())) {
                                    List<REDecisionsViewBean> decisions = mapDecisionsParDemandeRente.get(dvbCID
                                            .getIdDemandeRente());
                                    if (!decisions.contains(dvbCID)) {
                                        decisions.add(dvbCID);
                                    }
                                } else {
                                    List<REDecisionsViewBean> decisions = new ArrayList<REDecisionsViewBean>();
                                    decisions.add(dvbCID);
                                    mapDecisionsParDemandeRente.put(dvbCID.getIdDemandeRente(), decisions);
                                }
                            }
                        }
                    }
                }
            }

            // 2�me passe, on lance la validation de chacune des d�cisions
            // A la fin, on met a jours l'�tat des demandes de rentes.

            Set<String> keys = mapDecisionsParDemandeRente.keySet();

            REValiderDecisionVO validationDecisionVO = new REValiderDecisionVO();
            for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
                String key = iterator.next();
                List<REDecisionsViewBean> decisions = mapDecisionsParDemandeRente.get(key);

                for (Iterator<REDecisionsViewBean> iterator2 = decisions.iterator(); iterator2.hasNext();) {

                    REDecisionsViewBean decision = iterator2.next();

                    // Lancement du traitement...
                    if (decision.getCsEtat().equals(IREDecision.CS_ETAT_PREVALIDE)) {
                        REValiderDecisionHandler handler = new REValiderDecisionHandler(session);
                        handler.setIdDecision(decision.getIdDecision());
                        handler.setIdDemandeRente(decision.getIdDemandeRente());
                        validationDecisionVO.add(handler.doTraitement(transactionExterne));
                    }

                    // BZ 5432
                    REDemandeRente demandeRente = new REDemandeRente();
                    demandeRente.setIdDemandeRente(decision.getIdDemandeRente());
                    demandeRente.setSession(session);
                    demandeRente.retrieve();

                    if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE.equals(demandeRente.getCsTypeDemandeRente())) {
                        PRTiersWrapper conjointActuel = SFFamilleUtils.getConjointActuel(session,
                                ISFSituationFamiliale.CS_DOMAINE_RENTES, vb.getIdTiersRequerant());
                        if (conjointActuel != null) {
                            REDemandeRenteJointPrestationAccordeeManager manager = new REDemandeRenteJointPrestationAccordeeManager();
                            manager.setSession(session);
                            manager.setForIdTiersBeneficiaire(conjointActuel
                                    .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                            manager.setForCsTypeDemande(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE);

                            // est-ce que le conjoint a une rente accord�e de type vieillesse en �tat calcul�
                            manager.setForCsEtatPrestationIn(IREPrestationAccordee.CS_ETAT_CALCULE);
                            manager.find();

                            // si c'est le cas, on ne va pas plus loin
                            if (manager.isEmpty()) {

                                manager.clear();

                                // s'il n'y a pas de rente vieillesse � l'�tat calcul�,
                                // est-ce que le conjoint a une rente vieillesse valid�e, ou courant valid�e
                                manager.setForCsEtatPrestationIn(IREPrestationAccordee.CS_ETAT_VALIDE + ","
                                        + IREPrestationAccordee.CS_ETAT_PARTIEL);
                                manager.find();

                                if (!manager.isEmpty()) {
                                    // si c'est le cas, on parcours les rentes
                                    for (Iterator<REDemandeRenteJointPrestationAccordee> rentesAccordees = manager
                                            .iterator(); rentesAccordees.hasNext();) {
                                        REDemandeRenteJointPrestationAccordee demandeRenteJointPrestationAccordee = rentesAccordees
                                                .next();

                                        // si une rente est en cours (donc pas de date de fin) et a une date d'�ch�ance,
                                        // on va retirer cette date d'�ch�ance
                                        if (JadeStringUtil.isBlank(demandeRenteJointPrestationAccordee
                                                .getDateFinDroit())
                                                && !JadeStringUtil.isBlank(demandeRenteJointPrestationAccordee
                                                        .getDateEcheanceRenteAccordee())) {

                                            RERenteAccordee renteAccordee = new RERenteAccordee();
                                            renteAccordee.setSession(session);
                                            renteAccordee.setId(demandeRenteJointPrestationAccordee
                                                    .getIdRenteAccordee());
                                            renteAccordee.retrieve();

                                            renteAccordee.setDateEcheance("");
                                            renteAccordee.update();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Pour terminer, on met a jours les demandes de rentes impact�es
            // La mise � jours se fait en fin de traitement pour �viter des
            // inter-blocages entre les maj �ventuelle d'une m�me demande.
            Map<String, String> mapIdDemandeDateFin = validationDecisionVO.getMapDemandesPourDiminution();
            Map<String, String> mapIdDemandeCsEtat = validationDecisionVO.getMapDemandesPourMAJEtat();

            if (mapIdDemandeCsEtat != null) {
                for (String idDemande : mapIdDemandeCsEtat.keySet()) {
                    REDemandeRente dem = new REDemandeRente();
                    dem.setSession(session);
                    dem.setIdDemandeRente(idDemande);
                    dem.retrieve(transactionExterne);
                    if (mapIdDemandeDateFin.containsKey(idDemande)) {
                        dem.setDateFin(mapIdDemandeDateFin.get(idDemande));
                    }
                    dem.setCsEtat(mapIdDemandeCsEtat.get(idDemande));
                    dem.update(transactionExterne);
                }
            }

            // On traite les demande dans la liste 2 non encore trait�e
            if (mapIdDemandeDateFin != null) {
                for (String idDemande : mapIdDemandeDateFin.keySet()) {
                    if (!mapIdDemandeCsEtat.containsKey(idDemande)) {
                        REDemandeRente dem = new REDemandeRente();
                        dem.setSession(session);
                        dem.setIdDemandeRente(idDemande);
                        dem.retrieve(transactionExterne);
                        dem.setDateFin(mapIdDemandeDateFin.get(idDemande));
                        dem.update(transactionExterne);
                    }
                }
            }

            boolean etatIncoherent = false;

            // Voil� on � fait joujou avec notre demande et nos d�cisions. Maintenant on vas contr�ler que tout cela
            // soit bien correct
            // Donc :
            // On va d�j� contr�ler l'�tat de notre demande
            REDemandeRente demandeNew = new REDemandeRente();
            demandeNew.setSession(session);
            demandeNew.setIdDemandeRente(vb.getIdDemandeRente());
            demandeNew.retrieve(transactionExterne);
            if (!checkDemandeStateEvolution(etatDemandeAvantraitement, demandeNew.getCsEtat())) {
                etatIncoherent = true;
                vb.setMsgType(FWViewBeanInterface.ERROR);
                vb.setMessage("Attention : l'�tat de la demande [" + demandeNew.getIdDemandeRente()
                        + "] est incoh�rent. Etat pr�c�dent [" + etatDemandeAvantraitement + "] - etat actuel ["
                        + demandeNew.getCsEtat() + "]");
            }

            REDecisionsManager redm = new REDecisionsManager();
            redm.setSession(session);
            redm.setForIdDemandeRente(demandeNew.getIdDemandeRente());
            redm.find(transactionExterne);
            for (Object d : redm.getContainer()) {
                REDecisionEntity decision = (REDecisionEntity) d;

                String etatPrecedent = mapDecisionSousSurveillance.get(decision.getIdDecision());
                if (!checkDecisionStateEvolution(etatPrecedent, decision.getCsEtat())) {
                    etatIncoherent = true;
                    vb.setMsgType(FWViewBeanInterface.ERROR);
                    vb.setMessage("Attention : l'�tat de la decision [" + decision.getIdDecision()
                            + "] est incoh�rent. Etat pr�c�dent [" + etatPrecedent + "] - etat actuel ["
                            + decision.getCsEtat() + "]");
                }
            }

            if (!etatIncoherent) {
                transactionExterne.commit();
            }

        } catch (Exception e) {
            if (transactionExterne != null) {
                transactionExterne.rollback();
                vb.setMsgType(FWViewBeanInterface.ERROR);
                vb.setMessage(e.toString());
            }
        } finally {
            vb.setDocumentsPreview(null);
            if (transactionExterne != null) {
                transactionExterne.closeTransaction();
            }
        }

    }

}
