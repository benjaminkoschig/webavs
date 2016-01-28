/*
 * Créé le 10 novembre 2010
 */
package globaz.cygnus.services.preparerDecision;

import globaz.cygnus.api.decisions.IRFDecisions;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.decisions.RFAssDossierDecision;
import globaz.cygnus.db.decisions.RFCopieDecision;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.demandes.RFPrDemandeJointDossier;
import globaz.cygnus.db.dossiers.RFDossierJointDecisionJointTiers;
import globaz.cygnus.db.dossiers.RFDossierJointDecisionJointTiersManager;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemande;
import globaz.cygnus.db.qds.RFAssQdDossier;
import globaz.cygnus.db.qds.RFAssQdDossierJointDossierJointTiers;
import globaz.cygnus.db.qds.RFAssQdDossierJointDossierJointTiersManager;
import globaz.cygnus.db.qds.RFQd;
import globaz.cygnus.db.qds.RFQdAssure;
import globaz.cygnus.db.qds.RFQdAssureJointDossierJointTiersManager;
import globaz.cygnus.db.qds.RFQdAugmentation;
import globaz.cygnus.db.qds.RFQdAugmentationManager;
import globaz.cygnus.db.qds.RFQdPrincipale;
import globaz.cygnus.db.qds.RFQdSoldeExcedentDeRevenu;
import globaz.cygnus.db.qds.RFQdSoldeExcedentDeRevenuManager;
import globaz.cygnus.utils.RFUtils;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import ch.globaz.pegasus.business.constantes.IPCDroits;

/**
 * Enregistre les résultats métiers (Qds, demandes et décisions) de la préparation des décisions en base de donnée
 * 
 * @author JJE
 */
public class RFPersistanceImputationDemandesDecisionsService {

    private String dateDernierPaiementMensuelRente = "";
    private Set<RFDecisionData> decisions = null;

    private Map<String, RFImputationDemandesData> demandesAimputerMap = null;
    private String idGestionnaireProcess = "";
    private HashMap<String, Set<String[]>> idQdIdsDossierMap = null;
    private boolean isAdaptationAnnuelle = false;
    private Map<String, RFImputationQdsData> qdsAimputerMap = null;
    private BSession session = null;

    private BITransaction transaction = null;

    public RFPersistanceImputationDemandesDecisionsService(BITransaction transaction, BSession session,
            String idGestionnaire, Map<String, RFImputationDemandesData> demandesAImputer,
            Map<String, RFImputationQdsData> qdsAimputerMap, Set<RFDecisionData> decisions,
            HashMap<String, Set<String[]>> idQdIdsDossierMap, boolean isAdaptationAnnuelle) {
        super();
        this.transaction = transaction;
        this.session = session;
        idGestionnaireProcess = idGestionnaire;
        demandesAimputerMap = demandesAImputer;
        this.qdsAimputerMap = qdsAimputerMap;
        this.decisions = decisions;
        dateDernierPaiementMensuelRente = RFUtils.getDateDernierPaiementMensuelRente("", session);
        this.isAdaptationAnnuelle = isAdaptationAnnuelle;
        this.idQdIdsDossierMap = idQdIdsDossierMap;
    }

    /**
     * Ajout de la réallocation dans le champ historisé augmentation de Qd
     * 
     * @param idFamModifCompteur
     * @param qdCourante
     * @return
     * @throws Exception
     */
    private String augmentationQdCorrection(String idFamModifCompteur, RFImputationQdsData qdCourante) throws Exception {

        int famModifCompteur = 0;
        if (JadeStringUtil.isBlankOrZero(idFamModifCompteur)) {
            // calcule le nouvel id unique de famille de modification
            RFQdAugmentationManager mgr = new RFQdAugmentationManager();
            mgr.setSession(session);
            mgr.setForIdFamilleMax(true);
            mgr.changeManagerSize(0);
            mgr.find(transaction);

            if (!mgr.isEmpty()) {
                RFQdAugmentation aug = (RFQdAugmentation) mgr.getFirstEntity();
                if (null != aug) {
                    famModifCompteur = JadeStringUtil.parseInt(aug.getIdFamilleModification(), 0) + 1;
                } else {
                    famModifCompteur = 1;
                }
            } else {
                famModifCompteur = 1;
            }
            if (!isAdaptationAnnuelle) {
                idFamModifCompteur = new Integer(famModifCompteur).toString();
            }
        }

        System.out.println("idQd ajout augmentation (correction): " + qdCourante.getIdQd());

        RFQdAugmentation rfAug = new RFQdAugmentation();
        rfAug.setSession(session);
        rfAug.setVisaGestionnaire(idGestionnaireProcess);
        rfAug.setRemarque(session.getLabel("PROCESS_PREPARER_DECISIONS_AJOUT_AUGMENTATION_REMARQUE"));
        rfAug.setConcerne(session.getLabel("PROCESS_PREPARER_DECISIONS_AJOUT_AUGMENTATION_CONCERNE"));
        rfAug.setMontantAugmentationQd(qdCourante.getMontantCorrectionBigDec().toString());
        rfAug.setIdFamilleModification(Integer.toString(famModifCompteur));
        rfAug.setIdQd(qdCourante.getIdQd());
        rfAug.setDateModification(JadeDateUtil.getDMYDate(new Date()));
        rfAug.setTypeModification(IRFQd.CS_AJOUT);
        rfAug.setIdAugmentationQdModifiePar("");

        rfAug.add(transaction);

        return idFamModifCompteur;

    }

    private String getIdRequerant(RFDecisionData decision) {
        Set<String[]> dossiers = idQdIdsDossierMap.get(decision.getIdQdPrincipale());
        String[] dossier = null;
        for (Iterator<String[]> i = dossiers.iterator(); i.hasNext();) {

            dossier = i.next();
            if (null != dossier[2]) {
                if (IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(dossier[2])) {
                    return dossier[1];
                }
            }
        }
        return null;
    }

    /**
     * 
     * Lance la pérsistance des décisions,demandes et des Qds
     * 
     * @param isCreationDecision
     * @return
     * @throws Exception
     */
    public String majImputation(boolean isCreationDecision) throws Exception {

        majImputationQds();

        if (isCreationDecision) {
            return majImputationDecisions();
        } else {
            return "";
        }

    }

    /**
     * 
     * Lance la persistance des décisions et des demandes
     * 
     * @param idQdIdsDossierMap
     * @return
     * @throws Exception
     */
    private String majImputationDecisions() throws Exception {

        Iterator<RFDecisionData> decisionsIter = decisions.iterator();
        String idDecision = "";

        // Parcours des décisions à persister
        while (decisionsIter.hasNext()) {

            RFDecisionData decisionCourante = decisionsIter.next();

            if (null != decisionCourante) {

                RFDecision rfDec = new RFDecision();
                rfDec.setSession(session);

                rfDec.setAnneeQD(decisionCourante.getAnneeQD());
                rfDec.setDatePreparation(JACalendar.todayJJsMMsAAAA());
                if (!isAdaptationAnnuelle) {
                    rfDec.setEtatDecision(IRFDecisions.NON_VALIDE);
                } else {
                    rfDec.setEtatDecision(IRFDecisions.VALIDE);
                    rfDec.setDateValidation(JACalendar.todayJJsMMsAAAA());
                    rfDec.setDateSurDocument(JACalendar.todayJJsMMsAAAA());
                    rfDec.setIdValidePar(idGestionnaireProcess);
                }

                String idAdresseDomicile = "";

                for (String keyIdDossier : decisionCourante.getIdsDossier().keySet()) {

                    String[] idDossier = decisionCourante.getIdsDossier().get(keyIdDossier);

                    RFDossierJointDecisionJointTiersManager rfDosJoiDecJoiTieMgr = new RFDossierJointDecisionJointTiersManager();
                    rfDosJoiDecJoiTieMgr.setSession(session);
                    rfDosJoiDecJoiTieMgr.setForIdDossiers(idDossier[0]);
                    rfDosJoiDecJoiTieMgr.changeManagerSize(0);
                    rfDosJoiDecJoiTieMgr.find(transaction);

                    if (rfDosJoiDecJoiTieMgr.size() == 1) {
                        RFDossierJointDecisionJointTiers rfDosJoiDecJoiTier = (RFDossierJointDecisionJointTiers) rfDosJoiDecJoiTieMgr
                                .getFirstEntity();

                        if (null != rfDosJoiDecJoiTier) {
                            if (rfDosJoiDecJoiTier.getCsSexe().equals(PRACORConst.CS_HOMME)) {
                                idAdresseDomicile = rfDosJoiDecJoiTier.getIdTiers();
                            } else {
                                if (JadeStringUtil.isBlankOrZero(idAdresseDomicile)) {
                                    idAdresseDomicile = rfDosJoiDecJoiTier.getIdTiers();
                                }
                            }
                        }
                    }
                }

                rfDec.setIdAdresseDomicile(idAdresseDomicile);
                rfDec.setIdAdressePaiement(decisionCourante.getIdAdressePaiement());
                rfDec.setIdGestionnaire(decisionCourante.getIdGestionnaire());
                rfDec.setIdPreparePar(decisionCourante.getIdPreparePar());
                rfDec.setNumeroDecision(decisionCourante.getNumeroDecision());
                rfDec.setIdQdPrincipale(decisionCourante.getIdQdPrincipale());
                rfDec.setMontantDepassementQd(decisionCourante.getDepassementQd());
                rfDec.setMontantExcedentDeRecette(decisionCourante.getExcedentDeRevenus());
                rfDec.setTexteRemarque("");
                rfDec.setGenreDecision(IRFDecisions.DECISION);
                rfDec.setTypePaiement(decisionCourante.getTypeDePaiment());

                rfDec.setMontantDepassementQd(decisionCourante.getDepassementQd());
                rfDec.setMontantARembourserParLeDsas(decisionCourante.getMontantARembourserDsas());
                rfDec.setMontantExcedentDeRecette(decisionCourante.getExcedentDeRevenus());
                rfDec.setMontantTotalRFM(decisionCourante.getMontantTotalAPayer());

                rfDec.setDateDernierPaiement(dateDernierPaiementMensuelRente);

                rfDec.setMontantCourantPartieFuture(decisionCourante.getMontantMensuel());
                rfDec.setMontantCourantPartieRetroactive(decisionCourante.getMontantCourantPartieRetro());

                rfDec.setDateDebutRetro(decisionCourante.getDateDebutRetro());

                if (!JadeStringUtil.isBlankOrZero(decisionCourante.getDateFinRetro())) {
                    rfDec.setDateFinRetro(decisionCourante.getDateFinRetro());
                }

                if (decisionCourante.isPaiementMensuel()) {
                    rfDec.setBordereauAccompagnement(Boolean.FALSE);
                } else {
                    rfDec.setBordereauAccompagnement(decisionCourante.getBordereauAccompagnement());
                }

                if (!JadeStringUtil.isBlankOrZero(decisionCourante.getIdExecutionProcess())) {
                    rfDec.setIdExecutionProcess(decisionCourante.getIdExecutionProcess());
                }

                rfDec.add(transaction);

                idDecision = rfDec.getIdDecision();

                decisionCourante.setIdDecision(rfDec.getIdDecision());

                for (String keyIdDossier : decisionCourante.getIdsDossier().keySet()) {

                    String[] idDossier = decisionCourante.getIdsDossier().get(keyIdDossier);

                    RFAssDossierDecision rfAssDosDec = new RFAssDossierDecision();
                    rfAssDosDec.setSession(session);
                    rfAssDosDec.setIdDecision(rfDec.getIdDecision());
                    rfAssDosDec.setIdDossier(idDossier[0]);

                    rfAssDosDec.add(transaction);
                }

                String idRequerant = "";
                if (!JadeStringUtil.isBlankOrZero(decisionCourante.getIdQdPrincipale())) {
                    idRequerant = getIdRequerant(decisionCourante);
                } else {
                    if (decisionCourante.getIdsDossier().size() == 1) {
                        for (String keyIdDossier : decisionCourante.getIdsDossier().keySet()) {
                            String[] idDossier = decisionCourante.getIdsDossier().get(keyIdDossier);
                            idRequerant = idDossier[1];
                        }
                    }
                }

                if (JadeStringUtil.isBlankOrZero(idRequerant)) {
                    throw new Exception(
                            "RFPersistanceImputationDemandesDecisionsService.: Impossible de retrouver le bénéficiaire de la décision N° "
                                    + decisionCourante.getIdDecision());
                } else {
                    decisionCourante.setIdRequerant(idRequerant);
                }

                // Si l'idAdresssePaiement est différente de l'idTiers, on effectue le traitement
                if (!decisionCourante.getIdAdressePaiement().equals(decisionCourante.getIdRequerant())) {

                    boolean hasCopie = true;

                    // Parcour l'ensemble des ids tiers présents
                    for (String keyDossierCourant : decisionCourante.getIdsDossier().keySet()) {

                        String[] dossierCourant = decisionCourante.getIdsDossier().get(keyDossierCourant);

                        // Si l'un des idTiers est égal à l'idAdressePaiement on ne génère pas de copie
                        if (dossierCourant[1].equals(decisionCourante.getIdAdressePaiement())) {
                            hasCopie = false;
                        }
                    }

                    // Ajout d'une copie pour l'adresse de paiement ne correspondant pas aux membres de la famille
                    if (hasCopie) {
                        RFCopieDecision rfCopDec = new RFCopieDecision();
                        rfCopDec.setSession(session);
                        rfCopDec.setIdDecision(decisionCourante.getIdDecision());
                        rfCopDec.setIdTiers(decisionCourante.getIdAdressePaiement());
                        rfCopDec.setHasPageGarde(true);
                        rfCopDec.setHasVersement(true);
                        rfCopDec.setHasDecompte(true);
                        rfCopDec.setHasRemarques(true);
                        rfCopDec.setHasMoyensDroit(true);
                        rfCopDec.setHasSignature(true);
                        rfCopDec.setHasAnnexes(true);
                        rfCopDec.setHasCopies(true);
                        rfCopDec.add(transaction);
                    }
                }

                // MàJ des demandes
                for (String key : decisionCourante.getIdDemandes()) {
                    RFImputationDemandesData demandeCourante = demandesAimputerMap.get(key);

                    if (null != demandeCourante) {

                        RFDemande rfDemande = new RFDemande();
                        rfDemande.setSession(session);
                        rfDemande.setIdDemande(demandeCourante.getIdDemande());
                        rfDemande.retrieve();

                        if (!rfDemande.isNew()) {
                            if (!isAdaptationAnnuelle) {
                                rfDemande.setCsEtat(IRFDemande.CALCULE);
                            } else {
                                rfDemande.setCsEtat(IRFDemande.PAYE);
                            }
                            rfDemande.setCsStatut(demandeCourante.getStatutDemande());
                            rfDemande.setDateImputation(JACalendar.todayJJsMMsAAAA());
                            rfDemande.setIdDecision(rfDec.getIdDecision());
                            rfDemande.setIdQdPrincipale(demandeCourante.getIdQdPrincipale());
                            rfDemande.setIdQdAssure(demandeCourante.getIdQdAssure());
                            rfDemande.setMontantAPayer(demandeCourante.getMontantAccepte());
                            rfDemande.setIsForcerPaiement(demandeCourante.isForcerPaiement());
                            rfDemande.setMontantMensuel(demandeCourante.getMontantMensuel());

                            rfDemande.update(transaction);

                            // Persistance des motifs de refus
                            for (String[] motifCourrant : demandeCourante.getMotifsDeRefus()) {

                                RFAssMotifsRefusDemande rfAssMotRefDem = new RFAssMotifsRefusDemande();
                                rfAssMotRefDem.setSession(session);
                                rfAssMotRefDem.setIdDemande(demandeCourante.getIdDemande());
                                rfAssMotRefDem.setIdMotifsRefus(motifCourrant[0]);
                                rfAssMotRefDem.setMntMotifsDeRefus(motifCourrant[1]);

                                rfAssMotRefDem.add(transaction);

                            }

                        } else {
                            throw new Exception("RFPersistanceImputationDemandesDecisionsService demande introuvable");
                        }
                    } else {
                        throw new Exception("RFPersistanceImputationDemandesDecisionsService demande introuvable");
                    }
                }
            } else {
                throw new Exception("RFPersistanceImputationDemandesDecisionsService demande introuvable");
            }
        }

        return idDecision;
    }

    /**
     * Lance la persistance des Qds
     * 
     * @throws Exception
     */
    private void majImputationQds() throws Exception {

        for (String keyQd : qdsAimputerMap.keySet()) {

            RFImputationQdsData qdCourante = qdsAimputerMap.get(keyQd);

            if (qdCourante != null) {

                if (qdCourante.isQdPrincipale()) {

                    RFQdPrincipale rfQdPri = new RFQdPrincipale();
                    rfQdPri.setSession(session);
                    rfQdPri.setIdQdPrincipale(qdCourante.getIdQd());

                    rfQdPri.retrieve();

                    if (!rfQdPri.isNew()) {

                        if (qdCourante.isHasSoldeExcedentModifie()) {

                            int famModifCompteur = 0;
                            if (JadeStringUtil.isBlankOrZero(rfQdPri.getIdFamModSoldeExcedentPreDec())) {
                                // calcule le nouvel id unique de famille de modification
                                RFQdSoldeExcedentDeRevenuManager mgr = new RFQdSoldeExcedentDeRevenuManager();
                                mgr.setSession(session);
                                mgr.setForIdFamilleMax(true);
                                mgr.changeManagerSize(0);
                                mgr.find(transaction);

                                if (!mgr.isEmpty()) {
                                    RFQdSoldeExcedentDeRevenu sc = (RFQdSoldeExcedentDeRevenu) mgr.getFirstEntity();
                                    if (null != sc) {
                                        famModifCompteur = JadeStringUtil.parseInt(sc.getIdFamilleModification(), 0) + 1;
                                    } else {
                                        famModifCompteur = 1;
                                    }
                                } else {
                                    famModifCompteur = 1;
                                }

                                if (!isAdaptationAnnuelle) {
                                    rfQdPri.setIdFamModSoldeExcedentPreDec(new Integer(famModifCompteur).toString());
                                }

                            } else {
                                famModifCompteur = new Integer(rfQdPri.getIdFamModSoldeExcedentPreDec()).intValue();
                            }

                            System.out.println("idQd ajout solde excedent de charge: " + rfQdPri.getIdQdPrincipale());

                            BigDecimal ajoutSoldeExcedent = qdCourante.getSoldeExcedent().add(
                                    new BigDecimal(RFUtils.getSoldeExcedentDeRevenu(rfQdPri.getIdQdPrincipale(),
                                            session)).negate());

                            RFQdSoldeExcedentDeRevenu rfsoEx = new RFQdSoldeExcedentDeRevenu();
                            rfsoEx.setSession(session);
                            // rfsoEx.setVisaGestionnaire(qdCourante.idGestionnaire);
                            rfsoEx.setVisaGestionnaire(idGestionnaireProcess);
                            rfsoEx.setRemarque("");
                            rfsoEx.setConcerne(session
                                    .getLabel("PROCESS_PREPARER_DECISIONS_AJOUT_SOLDE_EXCEDENT_CONCERNE"));
                            rfsoEx.setMontantSoldeExcedent(ajoutSoldeExcedent.toString());

                            rfsoEx.setIdFamilleModification(Integer.toString(famModifCompteur));
                            rfsoEx.setIdQd(qdCourante.getIdQd());
                            rfsoEx.setDateModification(JadeDateUtil.getDMYDate(new Date()));
                            rfsoEx.setTypeModification(IRFQd.CS_AJOUT);
                            rfsoEx.setIdSoldeExcedentModifie("");

                            rfsoEx.add(transaction);
                        }

                        // Gestion de la somme ré-allouée si correction
                        if (qdCourante.getMontantCorrectionBigDec().compareTo(new BigDecimal("0")) == 1) {
                            rfQdPri.setIdFamModAugmentationDeQd(augmentationQdCorrection(
                                    rfQdPri.getIdFamModAugmentationDeQd(), qdCourante));
                        }

                        if (!isAdaptationAnnuelle) {
                            rfQdPri.setIdGesModSoldeExcedentAugmentationQdPreDec(idGestionnaireProcess);
                        }
                        rfQdPri.update(transaction);

                        RFQd rfQd = new RFQd();
                        rfQd.setSession(session);
                        rfQd.setIdQd(qdCourante.getIdQd());
                        rfQd.retrieve();

                        if (!rfQd.isNew()) {
                            boolean update = false;
                            if (!isAdaptationAnnuelle) {
                                if (JadeStringUtil.isBlankOrZero(rfQd.getMontantInitialChargeRfm())) {
                                    if (JadeStringUtil.isBlankOrZero(rfQd.getMontantChargeRfm())
                                            || rfQd.getMontantChargeRfm().equals("0.00")
                                            || rfQd.getMontantChargeRfm().equals("0.0")) {
                                        rfQd.setMontantInitialChargeRfm(RFUtils.montantInitialChargeRfmZero);
                                    } else {
                                        rfQd.setMontantInitialChargeRfm(rfQd.getMontantChargeRfm());
                                    }
                                    update = true;
                                }
                            }

                            if (qdCourante.getChargeRfm().floatValue() > 0) {
                                BigDecimal chargeRfmBigDec = new BigDecimal(JadeStringUtil.isBlank(rfQd
                                        .getMontantChargeRfm()) ? "0" : rfQdPri.getMontantChargeRfm()).add(qdCourante
                                        .getChargeRfm());

                                rfQd.setMontantChargeRfm(chargeRfmBigDec.toString());
                                update = true;
                            }

                            if (update) {
                                rfQd.update(transaction);
                            }
                        } else {
                            throw new Exception(
                                    "RFPersistanceImputationDemandesDecisionsService Qd de base introuvable");
                        }
                    } else {
                        throw new Exception("RFPersistanceImputationDemandesDecisionsService grande QD introuvable");
                    }

                } else if (qdCourante.isQdAssure()) {

                    RFQd rfQdAss = new RFQd();
                    rfQdAss.setSession(session);
                    rfQdAss.setIdQd(qdCourante.getIdQd());
                    rfQdAss.retrieve();

                    if (!rfQdAss.isNew()) {

                        boolean update = false;
                        if (!isAdaptationAnnuelle) {
                            if (JadeStringUtil.isBlankOrZero(rfQdAss.getMontantInitialChargeRfm())) {
                                if (JadeStringUtil.isBlankOrZero(rfQdAss.getMontantChargeRfm())
                                        || rfQdAss.getMontantChargeRfm().equals("0.00")
                                        || rfQdAss.getMontantChargeRfm().equals("0.0")) {
                                    rfQdAss.setMontantInitialChargeRfm(RFUtils.montantInitialChargeRfmZero);
                                } else {
                                    rfQdAss.setMontantInitialChargeRfm(rfQdAss.getMontantChargeRfm());
                                }
                                update = true;
                            }
                        }

                        if (qdCourante.getChargeRfm().floatValue() > 0) {

                            BigDecimal chargeRfmBigDec = new BigDecimal(JadeStringUtil.isBlank(rfQdAss
                                    .getMontantChargeRfm()) ? "0" : rfQdAss.getMontantChargeRfm()).add(qdCourante
                                    .getChargeRfm());

                            rfQdAss.setMontantChargeRfm(chargeRfmBigDec.toString());
                            update = true;

                        }

                        if (update) {
                            rfQdAss.update(transaction);
                        }

                        // Réallocation en cas de corrrection par l'augmentation de la Qd
                        if (qdCourante.getMontantCorrectionBigDec().compareTo(new BigDecimal("0")) == 1) {

                            RFQdAssure rfQdAssure = new RFQdAssure();
                            rfQdAssure.setSession(session);
                            rfQdAssure.setIdQdAssure(qdCourante.getIdQd());
                            rfQdAssure.retrieve();

                            if (!rfQdAssure.isNew()) {

                                rfQdAssure.setIdFamilleAugmentation(augmentationQdCorrection(
                                        rfQdAssure.getIdFamilleAugmentation(), qdCourante));
                                rfQdAssure.setIdGesModSoldeExcedentAugmentationQdPreDec(idGestionnaireProcess);

                                rfQdAssure.update(transaction);

                            } else {
                                throw new Exception(
                                        "RFPersistanceImputationDemandesDecisionsService petite QD introuvable");
                            }
                        }
                    } else {
                        throw new Exception("RFPersistanceImputationDemandesDecisionsService petite QD introuvable");
                    }

                } else if (qdCourante.isQdVirtuelle()) {

                    Set<String> idsTiersMembresFamillePrisDansCalcul = new HashSet<String>();
                    Set<String[]> idsTiersMembresFamille = new HashSet<String[]>();

                    // Recherche des membres de la grande Qd
                    RFAssQdDossierJointDossierJointTiersManager rfAssQdDossierJointDossierJointTiersMgr = RFUtils
                            .getMembresFamilleGrandeQd(session, qdCourante.getIdQdPrincipale());

                    if (rfAssQdDossierJointDossierJointTiersMgr.size() > 0) {

                        Iterator<RFAssQdDossierJointDossierJointTiers> rfAssQdDossierJointDossierJointTiersIter = rfAssQdDossierJointDossierJointTiersMgr
                                .iterator();

                        while (rfAssQdDossierJointDossierJointTiersIter.hasNext()) {

                            RFAssQdDossierJointDossierJointTiers rfAssQdDossierJointDossierJointTiers = rfAssQdDossierJointDossierJointTiersIter
                                    .next();

                            if (rfAssQdDossierJointDossierJointTiers != null) {
                                if (rfAssQdDossierJointDossierJointTiers.getIsComprisDansCalcul()) {
                                    idsTiersMembresFamillePrisDansCalcul.add(rfAssQdDossierJointDossierJointTiers
                                            .getIdTiers());
                                }

                                idsTiersMembresFamille.add(new String[] {
                                        rfAssQdDossierJointDossierJointTiers.getIdTiers(),
                                        rfAssQdDossierJointDossierJointTiers.getTypeRelation(),
                                        rfAssQdDossierJointDossierJointTiers.getIsComprisDansCalcul() ? Boolean.TRUE
                                                .toString() : Boolean.FALSE.toString() });

                            } else {
                                throw new Exception(
                                        "RFPersistanceImputationDemandesDecisionsService impossible de remonter les membres de la Qd principale");
                            }
                        }

                    } else {
                        throw new Exception(
                                "RFPersistanceImputationDemandesDecisionsService impossible de remonter les membres de la Qd principale");
                    }

                    // Création de la petite Qd
                    RFQd rfQd = new RFQd();
                    rfQd.setSession(session);
                    rfQd.setAnneeQd(qdCourante.getAnneeQd());

                    RFQdAssureJointDossierJointTiersManager rfQdAssJointDosJointTieMgr = RFUtils
                            .getRFQdAssureJointDossierJointTiersManager(session, idsTiersMembresFamillePrisDansCalcul,
                                    null, qdCourante.getCodeTypeDeSoin(), qdCourante.getCodeSousTypeDeSoin(),
                                    IRFQd.CS_ETAT_QD_OUVERT, "", "", "", "", false, "", "");

                    if (rfQdAssJointDosJointTieMgr.size() > 0) {
                        rfQd.setCsEtat(IRFQd.CS_ETAT_QD_FERME);
                    } else {
                        rfQd.setCsEtat(IRFQd.CS_ETAT_QD_OUVERT);
                    }
                    rfQd.setCsGenreQd(IRFQd.CS_PETITE_QD);

                    if (!isAdaptationAnnuelle) {
                        rfQd.setCsSource(IRFQd.CS_SOURCE_QD_SYSTEME);
                    } else {
                        rfQd.setCsSource(IRFQd.CS_SOURCE_QD_ADAPTATION);
                    }

                    // rfQd.setCsTypeQd(IRFQd.CS_STANDARD);
                    rfQd.setDateCreation(JACalendar.todayJJsMMsAAAA());
                    rfQd.setIdGestionnaire(idGestionnaireProcess);
                    rfQd.setIsPlafonnee(true);
                    rfQd.setLimiteAnnuelle(qdCourante.getMntPlafondPot());
                    rfQd.setMontantChargeRfm(qdCourante.getChargeRfm().toString());

                    if (!isAdaptationAnnuelle) {
                        if (!JadeStringUtil.isBlankOrZero(rfQd.getMontantInitialChargeRfm())) {
                            rfQd.setMontantInitialChargeRfm(rfQd.getMontantChargeRfm());
                        } else {
                            rfQd.setMontantInitialChargeRfm(RFUtils.montantInitialChargeRfmZero);
                        }
                    }

                    rfQd.add(transaction);

                    // Maj de l'id Qd des demandes
                    for (String key : demandesAimputerMap.keySet()) {
                        RFImputationDemandesData demandeCourante = demandesAimputerMap.get(key);
                        if (demandeCourante.getIdQdVirtuelle().equals(qdCourante.getIdQd())) {
                            demandeCourante.setIdQdAssure(rfQd.getIdQd());
                        }
                    }
                    qdCourante.setIdQd(rfQd.getIdQd());

                    RFQdAssure rfQdAss = new RFQdAssure();
                    rfQdAss.setSession(session);
                    rfQdAss.setDateDebut(qdCourante.getDateDebutPetiteQd());
                    rfQdAss.setDateFin(qdCourante.getDateFinPetiteQd());

                    rfQdAss.setIdPotSousTypeDeSoin(qdCourante.getIdPotQdAssure());
                    rfQdAss.setIdQdAssure(rfQd.getIdQd());
                    rfQdAss.setIdSousTypeDeSoin(qdCourante.getCsCodeTypeDeSoin());

                    rfQdAss.add(transaction);

                    if (RFUtils.isSousTypeDeSoinCodeConcernePlusieursPersonnes(qdCourante.getCodeTypeDeSoin(),
                            qdCourante.getCodeSousTypeDeSoin())) {

                        for (String[] membreFamilleCourant : idsTiersMembresFamille) {

                            RFAssQdDossier rfAssQdDossierMembreQdPrincipale = new RFAssQdDossier();
                            rfAssQdDossierMembreQdPrincipale.setSession(session);

                            rfAssQdDossierMembreQdPrincipale.setIdQd(rfQd.getIdQd());

                            RFPrDemandeJointDossier rfDosJoiPrDem = RFUtils.getDossierJointPrDemande(
                                    membreFamilleCourant[0], session);

                            if (null != rfDosJoiPrDem) {
                                rfAssQdDossierMembreQdPrincipale.setIdDossier(rfDosJoiPrDem.getIdDossier());
                            } else {
                                throw new Exception(
                                        "RFPersistanceImputationDemandesDecisionsService impossible de remonter les membres de la Qd principale");
                            }

                            rfAssQdDossierMembreQdPrincipale.setTypeRelation(RFUtils
                                    .getCsTypeRelationPc(membreFamilleCourant[1]));

                            rfAssQdDossierMembreQdPrincipale.setIsComprisDansCalcul(Boolean.TRUE.toString().equals(
                                    membreFamilleCourant[2]));

                            rfAssQdDossierMembreQdPrincipale.add(transaction);
                        }

                    } else {
                        // Création de l'association entre le/les dossiers et la Qd assuré
                        RFAssQdDossier rfAssQdDossier = new RFAssQdDossier();
                        rfAssQdDossier.setSession(session);

                        rfAssQdDossier.setIdQd(rfQd.getIdQd());
                        rfAssQdDossier.setIdDossier(qdCourante.getIdDossier());
                        rfAssQdDossier.setTypeRelation(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
                        rfAssQdDossier.setIsComprisDansCalcul(Boolean.TRUE);

                        rfAssQdDossier.add(transaction);
                    }
                }
            } else {
                throw new Exception("RFPersistanceImputationDemandesDecisionsService QD introuvable");
            }
        }
    }
}
