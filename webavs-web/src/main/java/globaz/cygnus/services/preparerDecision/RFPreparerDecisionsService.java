package globaz.cygnus.services.preparerDecision;

import globaz.corvus.utils.REPmtMensuel;
import globaz.cygnus.api.TypesDeSoins.IRFCodeTypesDeSoins;
import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.motifsRefus.IRFMotifsRefus;
import globaz.cygnus.api.paiement.IRFTypePaiement;
import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.demandes.RFTypesDemandeJointDossierJointTiers;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemande;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemandeManager;
import globaz.cygnus.db.process.RFPreparerDecisionDemandeLinkedQdPrincipale;
import globaz.cygnus.db.process.RFPreparerDecisionDemandeLinkedQdPrincipaleManager;
import globaz.cygnus.db.qds.RFQdAssureJointDossierJointTiers;
import globaz.cygnus.db.qds.RFQdAssureJointDossierJointTiersManager;
import globaz.cygnus.db.qds.RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande;
import globaz.cygnus.db.qds.RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager;
import globaz.cygnus.db.qds.RFQdPrincipale;
import globaz.cygnus.db.qds.RFQdPrincipaleJointDossier;
import globaz.cygnus.db.qds.RFQdPrincipaleJointDossierManager;
import globaz.cygnus.db.qds.RFQdPrincipaleManager;
import globaz.cygnus.services.RFSetEtatProcessService;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFUtils;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.pegasus.business.constantes.IPCDroits;

public class RFPreparerDecisionsService {

    private String dateDernierPaiementMensuelRente = "";
    private String dateSurDocument = "";
    private List<RFTypesDemandeJointDossierJointTiers> demandesATraiterList = null;
    private String idExecutionProcess = "";
    private String idGestionnaire = "";
    private transient FWMemoryLog memoryLog = null;
    private BISession session = null;
    private BITransaction transaction = null;

    public RFPreparerDecisionsService(String dateDernierPaiementMensuelRente, String dateSurDocument,
            List<RFTypesDemandeJointDossierJointTiers> demandesATraiterList, String idGestionnaire,
            String idExecutionProcess, FWMemoryLog memoryLog, BISession session, BITransaction transaction) {
        super();
        this.dateDernierPaiementMensuelRente = dateDernierPaiementMensuelRente;
        this.dateSurDocument = dateSurDocument;
        this.demandesATraiterList = demandesATraiterList;
        this.idGestionnaire = idGestionnaire;
        this.memoryLog = memoryLog;
        this.session = session;
        this.transaction = transaction;
        this.idExecutionProcess = idExecutionProcess;
    }

    /**
     * Ajoute une liste des gestionnaires ayant forcé le paiement dans le memory log
     * 
     * @param forcePaiementGestsIdsDemandes
     * @throws Exception
     */
    private void ajouterGestionnaireForcerPaiement(Map<String, Set<String>> forcePaiementGestsIdsDemandes)
            throws Exception {

        if (forcePaiementGestsIdsDemandes.size() > 0) {

            StringBuffer listeGestForPaiStB = new StringBuffer();
            listeGestForPaiStB.append(getSession().getLabel("PROCESS_PREPARER_DECISIONS_GEST_FORCE_PAIEMENT") + " \n ");

            for (String key : forcePaiementGestsIdsDemandes.keySet()) {
                listeGestForPaiStB.append(key + ": \n ");
                Set<String> demandes = forcePaiementGestsIdsDemandes.get(key);
                StringBuffer demandesStrBuffer = new StringBuffer();
                int incDem = 1;
                for (String idDemande : demandes) {
                    if (incDem != demandes.size()) {
                        demandesStrBuffer.append(idDemande + ",");
                    } else {
                        demandesStrBuffer.append(idDemande);
                    }
                    incDem++;
                }
                listeGestForPaiStB.append(demandesStrBuffer);
            }

            getMemoryLog().logMessage(listeGestForPaiStB.toString(), FWMessage.INFORMATION,
                    getSession().getLabel("PROCESS_PREPARER_DECISIONS"));

        }

    }

    private void ajouterQdAImputerCorrection(Map<String, RFImputationDemandesData> demandesAimputerMap,
            Map<String, RFImputationQdsData> qdsAimputerMap, Set<String> idQdPrincipaleSet, String idDepassementQd,
            BITransaction transaction) throws Exception {

        Map<String, String[]> qdsCorrectionDemande = new HashMap<String, String[]>();

        // Recherche les qds des demandes corrigés et la somme à re-allouer
        for (String demandeCouranteKey : demandesAimputerMap.keySet()) {

            RFImputationDemandesData demandeCourante = demandesAimputerMap.get(demandeCouranteKey);
            if (demandeCourante != null) {
                if (!JadeStringUtil.isBlankOrZero(demandeCourante.getIdDemandeParent())) {

                    JACalendar cal = new JACalendarGregorian();

                    RFDemande demandeParent = new RFDemande();
                    demandeParent.setSession(getSession());
                    demandeParent.setIdDemande(demandeCourante.getIdDemandeParent());
                    demandeParent.retrieve();

                    if (!demandeParent.isNew()) {

                        // Mémorisation de l'id Qd de la demande parent
                        demandeCourante.setIdQdPrincipaleParent(demandeParent.getIdQdPrincipale());
                        demandeCourante.setRestitutionCsSource(demandeParent.getCsSource());

                        if (demandeParent.getIsForcerPaiement()) {

                            RFAssMotifsRefusDemandeManager rfAssMotifsRefusMgr = new RFAssMotifsRefusDemandeManager();

                            rfAssMotifsRefusMgr.setSession(getSession());
                            rfAssMotifsRefusMgr.setForIdDemande(demandeParent.getIdDemande());
                            rfAssMotifsRefusMgr.changeManagerSize(0);
                            rfAssMotifsRefusMgr.find();

                            Iterator<RFAssMotifsRefusDemande> rfAssMotifsRefusItr = rfAssMotifsRefusMgr.iterator();

                            while (rfAssMotifsRefusItr.hasNext()) {

                                RFAssMotifsRefusDemande motifCourant = rfAssMotifsRefusItr.next();

                                if (motifCourant != null) {
                                    if (motifCourant.getIdMotifsRefus().equals(idDepassementQd)) {
                                        demandeCourante.setRestitutionMontantDepassementQdParent(motifCourant
                                                .getMntMotifsDeRefus().replace("'", ""));
                                    }
                                }
                            }
                        }

                        demandeCourante.setRestitutionIsForcerPaiementParent(demandeParent.getIsForcerPaiement());

                        JADate dateDernierPaiementJADate = new JADate(RFUtils.getDateDernierPaiementMensuelRente(
                                dateDernierPaiementMensuelRente, getSession()));
                        String dateFinTraitementParent = "";
                        if (JadeStringUtil.isBlankOrZero(demandeParent.getDateFinTraitement())) {
                            dateFinTraitementParent = "31.12.9999";
                        } else {
                            dateFinTraitementParent = demandeParent.getDateFinTraitement().replace("'", "");
                        }

                        // Remarque: les cas sont séparés pour faciliter d'éventuelles modifications
                        if (demandeParent.getIdSousTypeDeSoin().equals(IRFTypesDeSoins.st_2_REGIME_ALIMENTAIRE)) {

                            reallocationRegime(demandeCourante, demandeParent, dateFinTraitementParent,
                                    dateDernierPaiementJADate, qdsCorrectionDemande, transaction, cal);
                        } else {

                            if (demandeParent.getIdSousTypeDeSoin().equals(
                                    IRFTypesDeSoins.st_17_FRANCHISE_ET_QUOTEPARTS)
                                    && demandeParent.getIsPP()) {
                                reallocationFrqpPP(demandeCourante, demandeParent, dateFinTraitementParent,
                                        dateDernierPaiementJADate, qdsCorrectionDemande, transaction, cal);

                            } else {
                                BigDecimal montantARestituerQdBigDec = new BigDecimal(demandeParent.getMontantAPayer()
                                        .replace("'", ""));

                                String[] codesTypeDeSoinTab = RFUtils.getCodesTypeDeSoin(
                                        demandeParent.getIdSousTypeDeSoin(), getSession());

                                if (!RFUtils.isSousTypeDeSoinNonImputeSurGrandeQd(codesTypeDeSoinTab[0],
                                        codesTypeDeSoinTab[1])
                                        && !JadeStringUtil.isBlankOrZero(demandeParent.getIdQdPrincipale())) {

                                    ajoutQdsCorrectionDemandeMap(qdsCorrectionDemande,
                                            demandeParent.getIdQdPrincipale(), montantARestituerQdBigDec, Boolean.TRUE);

                                }

                                // BZ_9141 - I140120_019 :
                                // Récupération du montantParent pour déterminer le montant à restituer (même pour les
                                // types non imputable sur grande QD)
                                if (!JadeStringUtil.isBlankOrZero(demandeParent.getMontantAPayer())) {
                                    demandeCourante.setRestitutionMontantAPayeParent(demandeParent.getMontantAPayer()
                                            .replace("'", ""));
                                }

                                // Réallocation sur la petite Qd
                                if (!JadeStringUtil.isBlankOrZero(demandeParent.getIdQdAssure())) {
                                    ajoutQdsCorrectionDemandeMap(qdsCorrectionDemande, demandeParent.getIdQdAssure(),
                                            montantARestituerQdBigDec, Boolean.FALSE);
                                }
                            }
                        }

                    } else {
                        throw new Exception(
                                "RFPreparerDecisionsProcess.ajouterQdAImputeeCorrection(): demande parent introuvable");
                    }
                }
            } else {
                throw new Exception(
                        "RFPreparerDecisionsProcess.ajouterQdAImputeeCorrection(): Impossible de retrouver les demandes corrigées");
            }

        }

        if (qdsCorrectionDemande.size() > 0) {

            // On traite les qds principales
            int qdsCorrectionDemandeSize = 0;
            Set<String> idsQdPrincipaleSet = new HashSet<String>();
            for (String key : qdsCorrectionDemande.keySet()) {
                if (qdsCorrectionDemande.get(key)[1].equals(Boolean.TRUE.toString())) {

                    idsQdPrincipaleSet.add(key);
                    qdsCorrectionDemandeSize++;

                }
            }

            if (idsQdPrincipaleSet.size() > 0) {
                RFQdPrincipaleJointDossierManager rfQdPriForImpMgr = new RFQdPrincipaleJointDossierManager();
                rfQdPriForImpMgr.setSession(getSession());
                rfQdPriForImpMgr.setForCsTypeRelation(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
                rfQdPriForImpMgr.setForIdsQd(idsQdPrincipaleSet);
                rfQdPriForImpMgr.changeManagerSize(0);
                rfQdPriForImpMgr.find();

                ajouterQdsPrincipaleDansQdsAImputerMap(true, demandesAimputerMap, idQdPrincipaleSet, qdsAimputerMap,
                        rfQdPriForImpMgr, qdsCorrectionDemandeSize, qdsCorrectionDemande, transaction);
            }

            // puis on traite les qds assurés
            qdsCorrectionDemandeSize = 0;
            Set<String> idsQdAssureSet = new HashSet<String>();
            for (String key : qdsCorrectionDemande.keySet()) {
                if (qdsCorrectionDemande.get(key)[1].equals(Boolean.FALSE.toString())) {
                    idsQdAssureSet.add(key);
                    qdsCorrectionDemandeSize++;
                }
            }
            if (idsQdAssureSet.size() > 0) {
                RFQdAssureJointDossierJointTiersManager rfQdAssForImpMgr = new RFQdAssureJointDossierJointTiersManager();
                rfQdAssForImpMgr.setSession(getSession());
                rfQdAssForImpMgr.setForCsTypeRelation(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
                rfQdAssForImpMgr.setForIdsQd(idsQdAssureSet);
                rfQdAssForImpMgr.changeManagerSize(0);
                rfQdAssForImpMgr.find();

                ajouterQdsAssureDansQdsAImputerMap(demandesAimputerMap, qdsAimputerMap, rfQdAssForImpMgr,
                        qdsCorrectionDemandeSize, qdsCorrectionDemande, transaction);
            }

        }
    }

    /**
     * 
     * Ajout dans la map qdsAImputer les Qds dont on a forcé l'imputation
     * 
     * @param idQdsImputationForcee
     * @param qdsAimputerMap
     * @param transaction
     * @throws Exception
     */
    private void ajouterQdAImputerForcerImputation(Map<String, RFImputationDemandesData> demandesAimputerMap,
            Set<String> idQdsImputationForcee, Map<String, RFImputationQdsData> qdsAimputerMap,
            Set<String> idQdPrincipaleSet, BITransaction transaction) throws Exception {

        if (idQdsImputationForcee.size() > 0) {
            RFQdPrincipaleJointDossierManager rfQdPriForImpMgr = new RFQdPrincipaleJointDossierManager();
            rfQdPriForImpMgr.setSession(getSession());
            rfQdPriForImpMgr.setForCsTypeRelation(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
            rfQdPriForImpMgr.setForIdsQd(idQdsImputationForcee);
            rfQdPriForImpMgr.changeManagerSize(0);
            rfQdPriForImpMgr.find();

            ajouterQdsPrincipaleDansQdsAImputerMap(false, demandesAimputerMap, idQdPrincipaleSet, qdsAimputerMap,
                    rfQdPriForImpMgr, idQdsImputationForcee.size(), null, transaction);

        }
    }

    private void ajouterQdsAssureDansQdsAImputerMap(Map<String, RFImputationDemandesData> demandesAimputerMap,
            Map<String, RFImputationQdsData> qdsAimputerMap, RFQdAssureJointDossierJointTiersManager rfQdAssForImpMgr,
            int qdsATraiterMapSize, Map<String, String[]> qdsCorrectionDemande, BITransaction transaction)
            throws Exception {

        if (rfQdAssForImpMgr.size() == qdsATraiterMapSize) {
            Iterator<RFQdAssureJointDossierJointTiers> rfQdAssForImpItr = rfQdAssForImpMgr.iterator();

            while (rfQdAssForImpItr.hasNext()) {
                RFQdAssureJointDossierJointTiers rfQdAss = rfQdAssForImpItr.next();
                if (null != rfQdAss) {

                    if (!qdsAimputerMap.containsKey(rfQdAss.getIdQd())) {

                        RFImputationQdsData rfImpQdDat = new RFImputationQdsData();
                        rfImpQdDat.setIdQd(rfQdAss.getIdQd());
                        if (rfQdAss.getIsPlafonnee()) {

                            rfImpQdDat.setMntResiduel(new BigDecimal(RFUtils.getMntResiduel(
                                    rfQdAss.getLimiteAnnuelle(),
                                    RFUtils.getAugmentationQd(rfQdAss.getIdQd(), getSession()),
                                    RFUtils.getSoldeDeCharge(rfQdAss.getIdQd(), getSession()),
                                    rfQdAss.getMontantChargeRfm())));

                            // On réalloue l'argent du parent sur la Qd
                            rfImpQdDat.setMontantCorrectionBigDec(new BigDecimal(qdsCorrectionDemande.get(rfQdAss
                                    .getIdQd())[0]));
                        }
                        rfImpQdDat.setNew(false);
                        rfImpQdDat.setQdAssure(true);
                        rfImpQdDat.setPlafonnee(rfQdAss.getIsPlafonnee());
                        // rfImpQdDat.setMntPlafondPot("");
                        rfImpQdDat.setAnneeQd(rfQdAss.getAnneeQd());
                        rfImpQdDat.setIdPotQdAssure(rfQdAss.getIdPotSousTypeDeSoin());
                        rfImpQdDat.setAnneeQd(rfQdAss.getAnneeQd());
                        rfImpQdDat.setCsCodeTypeDeSoin(rfQdAss.getIdSousTypeDeSoin());
                        rfImpQdDat.setIdTiers(rfQdAss.getIdTiers());
                        String[] codesTypeDeSoin = RFUtils.getCodesTypeDeSoin(rfQdAss.getIdSousTypeDeSoin(),
                                getSession());
                        rfImpQdDat.setCodeTypeDeSoin(codesTypeDeSoin[0]);
                        rfImpQdDat.setCodeSousTypeDeSoin(codesTypeDeSoin[1]);
                        // rfImpQdDat.setIdQdPrincipale(rfQdAss.getIdQd);
                        rfImpQdDat.setDateDebutPetiteQd(rfQdAss.getDateDebut());
                        rfImpQdDat.setDateFinPetiteQd(rfQdAss.getDateFin());
                        rfImpQdDat.setMntResiduel(new BigDecimal(RFUtils.getMntResiduel(rfQdAss.getLimiteAnnuelle(),
                                RFUtils.getAugmentationQd(rfQdAss.getIdQdAssure(), getSession()),
                                RFUtils.getSoldeDeCharge(rfQdAss.getIdQdAssure(), getSession()),
                                rfQdAss.getMontantChargeRfm())));
                        rfImpQdDat.setPlafonnee(rfQdAss.getIsPlafonnee());

                        // On vérifie si il existe d'autres Qds principale pour la même année
                        // -> idTiers NULL
                        RFQdAssureJointDossierJointTiersManager rfQdAssJoiDosJoiTieMgr = RFUtils
                                .getRFQdAssureJointDossierJointTiersManager(getSession(), null,
                                        rfImpQdDat.getIdTiers(), rfImpQdDat.getCodeTypeDeSoin(),
                                        rfImpQdDat.getCodeSousTypeDeSoin(), "", rfImpQdDat.getAnneeQd(), "", "", "",
                                        true, rfImpQdDat.getIdQd(), "");

                        Iterator<RFQdAssureJointDossierJointTiers> rfQdAssJoiDosJoiTieItr = rfQdAssJoiDosJoiTieMgr
                                .iterator();

                        BigDecimal montantAutresQdBigDec = new BigDecimal(0);
                        // String idQdIntermediaire = "";

                        if (rfQdAssJoiDosJoiTieMgr.size() > 0) {

                            while (rfQdAssJoiDosJoiTieItr.hasNext()) {

                                RFQdAssureJointDossierJointTiers rfQdAssJoinDosJoiTie = rfQdAssJoiDosJoiTieItr.next();

                                if (rfQdAssJoinDosJoiTie != null) {

                                    montantAutresQdBigDec = montantAutresQdBigDec.add(new BigDecimal(
                                            rfQdAssJoinDosJoiTie.getMontantChargeRfm()));

                                    // idQdIntermediaire = rfQdAssJoinDosJoiTie.getIdQdAssure();

                                } else {
                                    throw new Exception(
                                            "RFPreparerDecisionsProcess.ajouterQdsAssureDansQdsAImputerMap: Impossible de retrouver les qds dont on forcé l'imputation");
                                }
                            }
                        }

                        rfImpQdDat.setMontantAutresQdBigDec(montantAutresQdBigDec);

                        qdsAimputerMap.put(rfImpQdDat.getIdQd(), rfImpQdDat);

                    } else {

                        RFImputationQdsData rfImpQdDat = qdsAimputerMap.get(rfQdAss.getIdQdAssure());

                        if (null != rfImpQdDat) {

                            // On réalloue l'argent du parent sur la Qd du parent
                            rfImpQdDat.setMontantCorrectionBigDec(new BigDecimal(qdsCorrectionDemande.get(rfQdAss
                                    .getIdQdAssure())[0]));

                        } else {
                            throw new Exception(
                                    "RFPreparerDecisionsProcess.ajouterQdsAssureDansQdsAImputerMap: Impossible de retrouver la Qd à corriger");
                        }
                    }

                } else {
                    throw new Exception(
                            "RFPreparerDecisionsProcess.ajouterQdsAssureDansQdsAImputerMap: Impossible de retrouver les qds à corriger");
                }
            }
        } else {
            throw new Exception(
                    "RFPreparerDecisionsProcess.ajouterQdsAssureDansQdsAImputerMap: Impossible de retrouver les qds à corriger");
        }
    }

    /**
     * 
     * Ajoute les Qds dont on a forcé l'imputation ou qui sont liées à la correction d'une demande dans la map des Qds à
     * imputer
     * 
     * @param isCorrection
     * @param isForcerImputation
     * @param idQdPrincipaleSet
     * @param qdsAimputerMap
     * @param rfQdPriJoiDosMgr
     * @param qdsATraiterMapSize
     * @param transaction
     * @throws Exception
     */
    private void ajouterQdsPrincipaleDansQdsAImputerMap(boolean isCorrection,
            Map<String, RFImputationDemandesData> demandesAimputerMap, Set<String> idQdPrincipaleSet,
            Map<String, RFImputationQdsData> qdsAimputerMap, RFQdPrincipaleJointDossierManager rfQdPriJoiDosMgr,
            int qdsATraiterMapSize, Map<String, String[]> qdsCorrectionDemande, BITransaction transaction)
            throws Exception {

        if (rfQdPriJoiDosMgr.size() == qdsATraiterMapSize) {
            Iterator<RFQdPrincipaleJointDossier> rfQdPriForImpItr = rfQdPriJoiDosMgr.iterator();

            while (rfQdPriForImpItr.hasNext()) {
                RFQdPrincipaleJointDossier rfQdPri = rfQdPriForImpItr.next();
                if (null != rfQdPri) {

                    if (!qdsAimputerMap.containsKey(rfQdPri.getIdQdPrincipale())) {

                        idQdPrincipaleSet.add(rfQdPri.getIdQdPrincipale());
                        RFImputationQdsData rfImpQdDat = new RFImputationQdsData();
                        rfImpQdDat.setIdQd(rfQdPri.getIdQdPrincipale());
                        if (rfQdPri.getIsPlafonnee()) {

                            rfImpQdDat.setMntResiduel(new BigDecimal(RFUtils.getMntResiduel(
                                    rfQdPri.getLimiteAnnuelle(),
                                    RFUtils.getAugmentationQd(rfQdPri.getIdQdPrincipale(), getSession()),
                                    RFUtils.getSoldeDeCharge(rfQdPri.getIdQdPrincipale(), getSession()),
                                    rfQdPri.getMontantChargeRfm())));

                            if (isCorrection) {
                                // On réalloue l'argent du parent sur la Qd
                                rfImpQdDat.setMontantCorrectionBigDec(new BigDecimal(qdsCorrectionDemande.get(rfQdPri
                                        .getIdQd())[0]));
                            }
                        }
                        rfImpQdDat.setNew(false);
                        rfImpQdDat.setPlafonnee(rfQdPri.getIsPlafonnee());
                        rfImpQdDat.setQdPrincipale(true);
                        rfImpQdDat.setSoldeExcedent(new BigDecimal(RFUtils.getSoldeExcedentDeRevenu(
                                rfQdPri.getIdQdPrincipale(), getSession())));
                        rfImpQdDat.setCsTypeBeneficiaire(rfQdPri.getCsTypeBeneficiaire());
                        rfImpQdDat.setCsGenrePcAccordee(rfQdPri.getCsGenrePCAccordee());
                        rfImpQdDat.setCsTypePcAccordee(rfQdPri.getCsTypePCAccordee());
                        rfImpQdDat.setRI(rfQdPri.getIsRI());
                        rfImpQdDat.setLAPRAMS(rfQdPri.getIsLAPRAMS());
                        rfImpQdDat.setRemboursementConjoint(rfQdPri.getRemboursementConjoint());
                        rfImpQdDat.setRemboursementRequerant(rfQdPri.getRemboursementRequerant());
                        rfImpQdDat.setCsDegreApi(rfQdPri.getCsDegreApi());

                        // On vérifie si il existe d'autres Qds principale pour la même année
                        RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager rfQdJointPerValJointDosJointTieJointDemMgr = new RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager();
                        rfQdJointPerValJointDosJointTieJointDemMgr.setSession(getSession());
                        rfQdJointPerValJointDosJointTieJointDemMgr.setForCsGenreQd(IRFQd.CS_GRANDE_QD);
                        rfQdJointPerValJointDosJointTieJointDemMgr.setForIdTiers(rfQdPri.getIdTiers());
                        rfQdJointPerValJointDosJointTieJointDemMgr.setForAnneeQd(rfQdPri.getAnneeQd());
                        rfQdJointPerValJointDosJointTieJointDemMgr.setComprisDansCalcul(true);
                        rfQdJointPerValJointDosJointTieJointDemMgr.changeManagerSize(0);
                        rfQdJointPerValJointDosJointTieJointDemMgr.find(transaction);

                        Iterator<RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande> rfQdJointPerValJointDosJointTieJointDemItr = rfQdJointPerValJointDosJointTieJointDemMgr
                                .iterator();

                        BigDecimal montantAutresQdBigDec = new BigDecimal(0);
                        String idQdIntermediaire = "";

                        if (rfQdJointPerValJointDosJointTieJointDemMgr.size() > 0) {

                            while (rfQdJointPerValJointDosJointTieJointDemItr.hasNext()) {

                                RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande rfQdJointPerValJointDosJointTieJointDem = rfQdJointPerValJointDosJointTieJointDemItr
                                        .next();

                                if (rfQdJointPerValJointDosJointTieJointDem != null) {

                                    if (!idQdIntermediaire.equals(rfQdJointPerValJointDosJointTieJointDem
                                            .getIdQdPrincipale())) {

                                        if (rfQdJointPerValJointDosJointTieJointDem.getCsTypeBeneficiaire().equals(
                                                rfImpQdDat.getCsTypeBeneficiaire())
                                                && rfQdJointPerValJointDosJointTieJointDem.getCsTypePCAccordee()
                                                        .equals(rfImpQdDat.getCsTypePcAccordee())
                                                && !rfQdJointPerValJointDosJointTieJointDem.getIdQdPrincipale().equals(
                                                        rfImpQdDat.getIdQd())) {

                                            montantAutresQdBigDec = montantAutresQdBigDec.add(new BigDecimal(
                                                    rfQdJointPerValJointDosJointTieJointDem.getMontantChargeRfm()));

                                        }

                                    }

                                    idQdIntermediaire = rfQdJointPerValJointDosJointTieJointDem.getIdQdPrincipale();

                                } else {
                                    throw new Exception(
                                            "RFPreparerDecisionsProcess.ajouterQdsDansQdsAImputerMap: Impossible de retrouver les qds dont on forcé l'imputation");
                                }
                            }
                        }

                        rfImpQdDat.setMontantAutresQdBigDec(montantAutresQdBigDec);

                        qdsAimputerMap.put(rfImpQdDat.getIdQd(), rfImpQdDat);

                    } else {

                        RFImputationQdsData rfImpQdDat = qdsAimputerMap.get(rfQdPri.getIdQdPrincipale());

                        if (null != rfImpQdDat) {
                            if (isCorrection) {
                                // On réalloue l'argent du parent sur la Qd
                                rfImpQdDat.setMontantCorrectionBigDec(new BigDecimal(qdsCorrectionDemande.get(rfQdPri
                                        .getIdQd())[0]));
                            }
                        } else {
                            throw new Exception(
                                    "RFPreparerDecisionsProcess.ajouterQdsDansQdsAImputerMap: Impossible de retrouver la Qd à ajouter");
                        }
                    }

                } else {
                    throw new Exception(
                            "RFPreparerDecisionsProcess.ajouterQdsDansQdsAImputerMap: Impossible de retrouver les qds dont on forcé l'imputation");
                }
            }
        } else {
            throw new Exception(
                    "RFPreparerDecisionsProcess.ajouterQdsDansQdsAImputerMap: Impossible de retrouver les qds dont on a forcé l'imputation");
        }
    }

    private void ajouterTypePcDemandesCorrigeeSansQd(Map<String, RFImputationDemandesData> demandesAimputerMap,
            Map<String, RFImputationQdsData> qdsAimputerMap) throws Exception {

        for (String demandeCouranteKey : demandesAimputerMap.keySet()) {

            RFImputationDemandesData demandeCourante = demandesAimputerMap.get(demandeCouranteKey);
            if (demandeCourante != null) {
                if (!JadeStringUtil.isBlankOrZero(demandeCourante.getIdDemandeParent())
                        && JadeStringUtil.isBlankOrZero(demandeCourante.getIdQdPrincipale())) {

                    if (qdsAimputerMap.containsKey(demandeCourante.getIdQdPrincipaleParent())) {

                        RFImputationQdsData qdParent = qdsAimputerMap.get(demandeCourante.getIdQdPrincipaleParent());

                        if (null != qdParent) {
                            demandeCourante.setTypePCParent(qdParent.getCsTypePcAccordee());
                        } else {
                            throw new Exception(
                                    "RFPreparerDecisionsProcess.ajouterTypePcDemandesCorrigeeSansQd: Impossible de retrouver les qds dont on a forcé l'imputation");
                        }

                    } else {

                        System.out
                                .println("RFPreparerDecisionsProcess.ajouterTypePcDemandesCorrigeeSansQd: Qd non initialisée");

                        RFQdPrincipale rfQdPrinc = new RFQdPrincipale();
                        rfQdPrinc.setSession(getSession());
                        rfQdPrinc.setIdQdPrincipale(demandeCourante.getIdQdPrincipaleParent());
                        rfQdPrinc.retrieve();

                        if (!rfQdPrinc.isNew()) {
                            demandeCourante.setTypePCParent(rfQdPrinc.getCsTypePCAccordee());
                        } else {
                            throw new Exception(
                                    "RFPreparerDecisionsProcess.ajouterTypePcDemandesCorrigeeSansQd: Impossible de retrouver les qds dont on a forcé l'imputation");
                        }
                    }

                }
            }
        }
    }

    private void ajoutQdsCorrectionDemandeMap(Map<String, String[]> qdsCorrectionDemande, String key, BigDecimal value,
            Boolean isQdPrincipale) throws Exception {

        if (qdsCorrectionDemande.containsKey(key)) {

            String[] mntAncienneCorrection = qdsCorrectionDemande.get(key);
            if (null != mntAncienneCorrection) {
                mntAncienneCorrection[0] = new BigDecimal(mntAncienneCorrection[0]).add(value).toString();
            } else {
                throw new Exception(
                        "RFPreparerDecisionsProcess.ajoutQdsCorrectionDemandeMap(): ancien montant de correction introuvable");
            }
        } else {
            qdsCorrectionDemande.put(key, new String[] { value.toString(), isQdPrincipale.toString() });
        }
    }

    public String getDateDernierPaiementMensuelRente() {
        return dateDernierPaiementMensuelRente;
    }

    private String getDateFinDeMois_JJMMAAAA(String date_MMAAAA) {

        String dateFinDeTraitementJJMMAAAA = "01." + date_MMAAAA;
        dateFinDeTraitementJJMMAAAA = JadeDateUtil.addMonths(dateFinDeTraitementJJMMAAAA, 1);
        dateFinDeTraitementJJMMAAAA = JadeDateUtil.addDays(dateFinDeTraitementJJMMAAAA, -1);

        return dateFinDeTraitementJJMMAAAA;
    }

    public String getDateSurDocument() {
        return dateSurDocument;
    }

    public String getIdExecutionProcess() {
        return idExecutionProcess;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public BISession getISession() {
        return session;
    }

    public BITransaction getITransaction() {
        return transaction;
    }

    public FWMemoryLog getMemoryLog() {
        return memoryLog;
    }

    public BSession getSession() {
        return (BSession) session;
    }

    public BTransaction getTransaction() {
        return (BTransaction) transaction;
    }

    private void logTempsProcess(long procTimeInMillisFin, long procTimeInMillisDeb) {
        long procTimeInMillisFinale = procTimeInMillisFin - procTimeInMillisDeb;
        System.out.print("Total process: ");
        System.out.println(procTimeInMillisFinale);
    }

    /**
     * 
     * Recherche l'id Qd principale pour toutes les demandes dans l'état enregistré pour un gestionnaire
     * 
     * @param demandesAimputerMap
     * @param idQdPrincipaleSet
     * @param idGestionnaire
     * @param transaction
     * @param session
     * @throws Exception
     */
    private void majIdsQdPrincipaleDemandesAImputer(Map<String, RFImputationDemandesData> demandesAimputerMap,
            Set<String> idQdPrincipaleSet, String idGestionnaire, BITransaction transaction, BISession session)
            throws Exception {
        // Maj des ids Qd principale des demandes à imputer
        RFPreparerDecisionDemandeLinkedQdPrincipaleManager rfPreDecDemLinQdPriMgr = new RFPreparerDecisionDemandeLinkedQdPrincipaleManager();
        rfPreDecDemLinQdPriMgr.setSession((BSession) session);

        if (RFPropertiesUtils.misesAJourQdsPrincipalesDemandesAImputer()) {
            rfPreDecDemLinQdPriMgr.setIdGestionnaire(idGestionnaire);
        }

        rfPreDecDemLinQdPriMgr.changeManagerSize(0);
        rfPreDecDemLinQdPriMgr.find(transaction);

        Iterator<RFPreparerDecisionDemandeLinkedQdPrincipale> rfPreDecDemNotLinQdPriItr = rfPreDecDemLinQdPriMgr
                .iterator();

        while (rfPreDecDemNotLinQdPriItr.hasNext()) {
            RFPreparerDecisionDemandeLinkedQdPrincipale rfPreDecDemLinQdPri = rfPreDecDemNotLinQdPriItr.next();

            if (null != rfPreDecDemLinQdPri) {
                RFImputationDemandesData demandeCourante = demandesAimputerMap.get(rfPreDecDemLinQdPri.getIdDemande());

                if (null != demandeCourante) {

                    if (rfPreDecDemLinQdPri.getCsTypeRelation().equals(IPCDroits.CS_ROLE_FAMILLE_ENFANT)) {
                        demandeCourante.setEnfant(true);
                    }

                    if (rfPreDecDemLinQdPri.getIsComprisDansCalcul()) {
                        if (!demandeCourante.getIsForcerImputation()) {
                            demandeCourante.setIdQdPrincipale(rfPreDecDemLinQdPri.getIdQd());
                        }
                        idQdPrincipaleSet.add(demandeCourante.getIdQdPrincipale());
                    } else {
                        demandeCourante.setIdQdPrincipale("");
                        demandeCourante.setEnfantExclu(true);
                    }
                }
            }
        }
    }

    public boolean preparerDecisions() throws Exception {

        /********************* DEBUG *************************************************/
        long procTimeInMillisDeb = System.currentTimeMillis();
        /*****************************************************************************/

        // Si le gestionnaire est vide, on utilise celui de la session
        if (JadeStringUtil.isBlankOrZero(getIdGestionnaire())) {
            setIdGestionnaire(getSession().getUserId());
        }

        if (!REPmtMensuel.isValidationDecisionAuthorise(getSession())) {
            throw new Exception(getSession().getLabel("ERREUR_RF_PAIEMENT_MENSUEL_EN_COURS"));
        }

        // On verouille l'insertion des demandes
        if (RFSetEtatProcessService.getEtatProcessPreparerDecision(getSession())) {
            throw new Exception(getSession().getLabel("PROCESS_PREPARER_DECISIONS_ERREUR_EN_EXECUTION"));
        } else {
            RFSetEtatProcessService.setEtatProcessPreparerDecision(true, getSession());
        }

        // Récupèration de l'id bd du motif de refus dépassement QD
        Map<String, String[]> idsMotifsDeRefus = RFUtils.getIdsMotifDeRefusSysteme(getSession(), getTransaction());
        String idDepassementQD = idsMotifsDeRefus.get(IRFMotifsRefus.ID_MAXIMUM_N_FRANC_PAR_ANNEE)[0];

        // Initilalisation de la map contenant les demandes à imputer
        Map<String, RFImputationDemandesData> demandesAimputerMap = new LinkedHashMap<String, RFImputationDemandesData>();
        // Initilalisation de la map contenant les Qds à imputer
        Map<String, RFImputationQdsData> qdsAimputerMap = new HashMap<String, RFImputationQdsData>();
        // Initilalisation de la map contenant les Qds dont on a forcé l'imputation
        Set<String> idQdsImputationForcee = new HashSet<String>();
        // Initilalisation de la map contenant les ids des Qd principales concernées par le calcul
        Set<String> idQdPrincipaleSet = new HashSet<String>();
        // Initialisation d'une Map<idGest,tableau de numéros de demandes> contenant les utilisateurs ayant forcé le
        // paiement
        Map<String, Set<String>> forcePaiementGestsIdsDemandes = new HashMap<String, Set<String>>();

        rechercherDemandesAImputer(demandesAimputerMap, idQdsImputationForcee, forcePaiementGestsIdsDemandes,
                getTransaction());

        majIdsQdPrincipaleDemandesAImputer(demandesAimputerMap, idQdPrincipaleSet, getIdGestionnaire(),
                getTransaction(), getSession());

        ajouterQdAImputerForcerImputation(demandesAimputerMap, idQdsImputationForcee, qdsAimputerMap,
                idQdPrincipaleSet, getTransaction());

        ajouterQdAImputerCorrection(demandesAimputerMap, qdsAimputerMap, idQdPrincipaleSet, idDepassementQD,
                getTransaction());

        // Si la demande courante n'a pas de Qd, on renseigne le champ "typePc" de la demande courante selon la
        // Qd de la demande parent -> choix de la rubrique comptable
        ajouterTypePcDemandesCorrigeeSansQd(demandesAimputerMap, qdsAimputerMap);

        if (RFPropertiesUtils.verifierSiCalculConcerneAutreGestionnaire()) {
            verifierSiCalculConcerneAutreGestionnaire(idQdPrincipaleSet);
        }

        // Création de la map contenant les décisions
        RFGenererDecisionsEntitesService rfGenererDecisionsEntitesService = new RFGenererDecisionsEntitesService(
                getIdGestionnaire(), demandesAimputerMap, getIdExecutionProcess(), getSession(), getTransaction());
        Object[] genererDecisionsEntiteObj = rfGenererDecisionsEntitesService.genererDecisionsEntite();

        HashMap<String, Set<String[]>> idQdIdsDossierMap = (HashMap<String, Set<String[]>>) genererDecisionsEntiteObj[1];
        Set<RFDecisionData> decisions = (Set<RFDecisionData>) genererDecisionsEntiteObj[0];

        // Imputation des demandes
        RFImputationDemandesService rfImputationDemandesService = new RFImputationDemandesService(getTransaction(),
                getSession(), demandesAimputerMap, decisions, qdsAimputerMap, getMemoryLog(), false);
        rfImputationDemandesService.imputerDemandes();

        // Persistance des maps des demandes, qds et décisions
        RFPersistanceImputationDemandesDecisionsService rfPersistanceImputationDemandesDecisionsService = new RFPersistanceImputationDemandesDecisionsService(
                getTransaction(), getSession(), idGestionnaire, demandesAimputerMap, qdsAimputerMap, decisions,
                idQdIdsDossierMap, false);
        rfPersistanceImputationDemandesDecisionsService.majImputation(true);

        // Persistance des données du paiements
        RFGenererPaiementService rfGenererPaiementService = new RFGenererPaiementService(getTransaction(),
                getSession(), idGestionnaire, decisions, demandesAimputerMap, qdsAimputerMap, idDepassementQD,
                idQdIdsDossierMap, false, "", !JadeStringUtil.isBlankOrZero(idExecutionProcess), getMemoryLog());
        rfGenererPaiementService.genererPaiement();

        ajouterGestionnaireForcerPaiement(forcePaiementGestsIdsDemandes);

        /********************* DEBUG *************************************************/
        long procTimeInMillisFin = System.currentTimeMillis();
        logTempsProcess(procTimeInMillisFin, procTimeInMillisDeb);
        /***************************************************************************/

        return true;

    }

    /**
     * 
     * Réalloue l'argent sur chaque Qds concernées par le régime
     * 
     * @param demandeCourante
     * @param demandeParent
     * @param dateFinTraitementParent
     * @param dateDernierPaiementJADate
     * @param qdsCorrectionDemande
     * @param transaction
     * @param cal
     * @throws Exception
     */
    private void reallocationFrqpPP(RFImputationDemandesData demandeCourante, RFDemande demandeParent,
            String dateFinTraitementParent, JADate dateDernierPaiementJADate,
            Map<String, String[]> qdsCorrectionDemande, BITransaction transaction, JACalendar cal) throws Exception {

        JADate dateDebutTraitementParentJADate = new JADate(demandeParent.getDateDebutTraitement().replace("'", ""));

        // ATTENTION ON REALLOUE SUR LES QDS L'ARGENT PRE-IMPUTE ET NON PAS PAYE !!
        BigDecimal montantARestituerQdBigDec = new BigDecimal(demandeParent.getMontantAPayer().replace("'", ""));
        ajoutQdsCorrectionDemandeMap(qdsCorrectionDemande, demandeParent.getIdQdPrincipale(),
                montantARestituerQdBigDec, Boolean.TRUE);

        getMemoryLog().logMessage(
                getSession().getLabel("PROCESS_PREPARER_DECISIONS_CORRECTION_FRQP_PP") + " "
                        + demandeCourante.getIdDemande() + " (" + demandeCourante.getNss() + ") "
                        + ": Ré-allocation de " + montantARestituerQdBigDec.toString() + " sur la grande Qd N° "
                        + demandeParent.getIdQdPrincipale() + " pour l'année "
                        + String.valueOf(dateDebutTraitementParentJADate.getYear()), FWMessage.INFORMATION,
                "RFPreparerDecisionsProcess.reallocationFrqpPP()");

        // Réallocation sur la petite Qd
        if (!JadeStringUtil.isBlankOrZero(demandeParent.getIdQdAssure())) {
            ajoutQdsCorrectionDemandeMap(qdsCorrectionDemande, demandeParent.getIdQdAssure(),
                    montantARestituerQdBigDec, Boolean.FALSE);
            getMemoryLog().logMessage(
                    getSession().getLabel("PROCESS_PREPARER_DECISIONS_CORRECTION_FRQP_PP") + " "
                            + demandeCourante.getIdDemande() + " (" + demandeCourante.getNss() + ") "
                            + ": Ré-allocation de " + montantARestituerQdBigDec.toString() + " sur la petite Qd N° "
                            + demandeParent.getIdQdAssure() + " pour l'année "
                            + String.valueOf(dateDebutTraitementParentJADate.getYear()), FWMessage.INFORMATION,
                    "RFPreparerDecisionsProcess.reallocationFrqpPP()");
        }

        demandeCourante.setRestitutionIdDecisionRFMAccordee(demandeParent.getIdDecision());

        BigDecimal montantPrestAccFrqpJanvierBigDec = new BigDecimal(
                RFUtils.MONTANT_PRESTATION_ACCORDEE_FRQP_1_ER_PAIEMENT);
        BigDecimal montantPrestAccFrqpFevrierMarsBigDec = new BigDecimal(
                RFUtils.MONTANT_PRESTATION_ACCORDEE_FRQP_2_EME_PAIEMENT);

        JADate janvierJADate = new JADate(1, 1, dateDebutTraitementParentJADate.getYear());
        JADate fevrierJADate = new JADate(1, 2, dateDebutTraitementParentJADate.getYear());
        JADate marsJADate = new JADate(1, 3, dateDebutTraitementParentJADate.getYear());

        // Paiement des rentes < janvier, rien n'est payé
        if (cal.compare(dateDernierPaiementJADate, janvierJADate) == JACalendar.COMPARE_FIRSTLOWER) {

            demandeCourante.setRestitutionMontantAPayeParent("");
        } else {// >= Janvier

            // Paiement des rentes < février, janvier payé
            if (cal.compare(dateDernierPaiementJADate, fevrierJADate) == JACalendar.COMPARE_FIRSTLOWER) {

                demandeCourante.setRestitutionMontantAPayeParent(montantPrestAccFrqpJanvierBigDec.toString());
            } else {// >= février

                // Paiement des rentes < mars, février payé
                if (cal.compare(dateDernierPaiementJADate, marsJADate) == JACalendar.COMPARE_FIRSTLOWER) {

                    demandeCourante.setRestitutionMontantAPayeParent(montantPrestAccFrqpJanvierBigDec.add(
                            montantPrestAccFrqpFevrierMarsBigDec).toString());
                } else {// >=Mars

                    demandeCourante.setRestitutionMontantAPayeParent(montantPrestAccFrqpJanvierBigDec
                            .add(montantPrestAccFrqpFevrierMarsBigDec).add(montantPrestAccFrqpFevrierMarsBigDec)
                            .toString());
                }
            }
        }

    }

    private void reallocationQdsRegime(RFDemande demandeParent, RFImputationDemandesData demandeCourante,
            int anneeCouranteInt, String dateDeDebutAnneeCourante, BigDecimal montantARestituerBigDec,
            Map<String, String[]> qdsCorrectionDemande, BITransaction transaction) throws Exception {

        // Recherche des Qds
        String idTiersDemandeParent = RFUtils.getIdTiersFromIdDossier(demandeParent.getIdDossier(), getSession());

        RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager rfQdJointPerValJointDosJointTieJointDemMgr = new RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager();
        rfQdJointPerValJointDosJointTieJointDemMgr.setSession(getSession());
        rfQdJointPerValJointDosJointTieJointDemMgr.setForCsGenreQd(IRFQd.CS_GRANDE_QD);
        rfQdJointPerValJointDosJointTieJointDemMgr.setForIdTiers(idTiersDemandeParent);
        rfQdJointPerValJointDosJointTieJointDemMgr.setForAnneeQd(String.valueOf(anneeCouranteInt));
        rfQdJointPerValJointDosJointTieJointDemMgr.setForDateDebutBetweenPeriode(dateDeDebutAnneeCourante);
        rfQdJointPerValJointDosJointTieJointDemMgr.changeManagerSize(0);
        rfQdJointPerValJointDosJointTieJointDemMgr.find(transaction);

        if (rfQdJointPerValJointDosJointTieJointDemMgr.size() > 0) {
            RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande rfQdJointPerValJointDosJointTieJointDem = (RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande) rfQdJointPerValJointDosJointTieJointDemMgr
                    .getFirstEntity();
            if (null != rfQdJointPerValJointDosJointTieJointDem) {
                ajoutQdsCorrectionDemandeMap(qdsCorrectionDemande,
                        rfQdJointPerValJointDosJointTieJointDem.getIdQdPrincipale(), montantARestituerBigDec,
                        Boolean.TRUE);
                getMemoryLog().logMessage(
                        getSession().getLabel("PROCESS_PREPARER_DECISIONS_CORRECTION_PLUSIEURS_ANNEES") + " "
                                + demandeCourante.getIdDemande() + " (" + demandeCourante.getNss() + ") "
                                + ": Ré-allocation de " + montantARestituerBigDec.toString() + " sur la grande Qd N° "
                                + rfQdJointPerValJointDosJointTieJointDem.getIdQdPrincipale() + " pour l'année "
                                + String.valueOf(anneeCouranteInt), FWMessage.INFORMATION,
                        "RFPreparerDecisionsProcess.reallocationQdsRegime()");
            } else {
                getMemoryLog().logMessage(
                        getSession().getLabel("PROCESS_PREPARER_DECISIONS_CORRECTION_PLUSIEURS_ANNEES") + " "
                                + demandeCourante.getIdDemande() + " (" + demandeCourante.getNss() + ") "
                                + ": Ré-allocation impossible (grande Qd null) pour l'année "
                                + String.valueOf(anneeCouranteInt), FWMessage.INFORMATION,
                        "RFPreparerDecisionsProcess.reallocationQdsRegime()");
            }
        } else {
            getMemoryLog().logMessage(
                    getSession().getLabel("PROCESS_PREPARER_DECISIONS_CORRECTION_PLUSIEURS_ANNEES") + " "
                            + demandeCourante.getIdDemande() + " (" + demandeCourante.getNss() + ") "
                            + ": Ré-allocation impossible (grande Qd introuvalble) pour l'année "
                            + String.valueOf(anneeCouranteInt), FWMessage.INFORMATION,
                    "RFPreparerDecisionsProcess.reallocationQdsRegime()");
        }

        String[] codesDemandeParent = RFUtils.getCodesTypeDeSoin(demandeParent.getIdSousTypeDeSoin(), getSession());

        RFQdAssureJointDossierJointTiersManager rfQdAssureJointPotTypeDeSoinMgr = RFUtils
                .getRFQdAssureJointDossierJointTiersManager(getSession(), null, idTiersDemandeParent,
                        codesDemandeParent[0], codesDemandeParent[1], "", String.valueOf(anneeCouranteInt), "", "",
                        dateDeDebutAnneeCourante, true, "", "");

        if (rfQdAssureJointPotTypeDeSoinMgr.size() > 0) {
            RFQdAssureJointDossierJointTiers rfQdAssureJointPotTypeDeSoin = (RFQdAssureJointDossierJointTiers) rfQdAssureJointPotTypeDeSoinMgr
                    .getFirstEntity();

            if (null != rfQdAssureJointPotTypeDeSoin) {

                ajoutQdsCorrectionDemandeMap(qdsCorrectionDemande, rfQdAssureJointPotTypeDeSoin.getIdQdAssure(),
                        montantARestituerBigDec, Boolean.FALSE);

                getMemoryLog().logMessage(
                        getSession().getLabel("PROCESS_PREPARER_DECISIONS_CORRECTION_PLUSIEURS_ANNEES") + " "
                                + demandeCourante.getIdDemande() + " (" + demandeCourante.getNss() + ") "
                                + ": Ré-allocation de " + montantARestituerBigDec.toString() + " sur la petite Qd N° "
                                + rfQdAssureJointPotTypeDeSoin.getIdQdAssure() + " pour l'année "
                                + String.valueOf(anneeCouranteInt), FWMessage.INFORMATION,
                        "RFPreparerDecisionsProcess.ajouterQdAImputerCorrection()");

            } else {
                getMemoryLog().logMessage(
                        getSession().getLabel("PROCESS_PREPARER_DECISIONS_CORRECTION_PLUSIEURS_ANNEES") + " "
                                + demandeCourante.getIdDemande() + " (" + demandeCourante.getNss() + ") "
                                + ": Ré-allocation impossible (petite Qd null) pour l'année "
                                + String.valueOf(anneeCouranteInt), FWMessage.INFORMATION,
                        "RFPreparerDecisionsProcess.ajouterQdAImputerCorrection()");
            }

        } else {
            getMemoryLog().logMessage(
                    getSession().getLabel("PROCESS_PREPARER_DECISIONS_CORRECTION_PLUSIEURS_ANNEES") + " "
                            + demandeCourante.getIdDemande() + " (" + demandeCourante.getNss() + ") "
                            + ": Ré-allocation impossible (petite Qd introuvalble) pour l'année "
                            + String.valueOf(anneeCouranteInt), FWMessage.INFORMATION,
                    "RFPreparerDecisionsProcess.ajouterQdAImputerCorrection()");
        }

    }

    /**
     * 
     * Réalloue l'argent sur chaque Qds concernées par le régime
     * 
     * @param demandeCourante
     * @param demandeParent
     * @param dateFinTraitementParent
     * @param dateDernierPaiementJADate
     * @param qdsCorrectionDemande
     * @param transaction
     * @param cal
     * @throws Exception
     */
    private void reallocationRegime(RFImputationDemandesData demandeCourante, RFDemande demandeParent,
            String dateFinTraitementParent, JADate dateDernierPaiementJADate,
            Map<String, String[]> qdsCorrectionDemande, BITransaction transaction, JACalendar cal) throws Exception {

        JADate dateFinTraitementParentJADate = new JADate(dateFinTraitementParent);
        JADate dateDebutTraitementParentJADate = new JADate(demandeParent.getDateDebutTraitement().replace("'", ""));
        // ATTENTION ON REALLOUE SUR LES QDS L'ARGENT PRE-IMPUTE ET NON PAS PAYE !!
        if (cal.compare(dateFinTraitementParentJADate, dateDernierPaiementJADate) == JACalendar.COMPARE_FIRSTUPPER) {
            // PAIEMENT ACTUEL -> FUTURE, rien n'a encore été payé, mais la Qd à déjà été imputée
            if (cal.compare(dateDebutTraitementParentJADate, dateDernierPaiementJADate) == JACalendar.COMPARE_FIRSTUPPER) {

                BigDecimal montantARestituerQdBigDec = new BigDecimal(demandeParent.getMontantAPayer().replace("'", ""));

                // Réallocation sur la grande Qd
                ajoutQdsCorrectionDemandeMap(qdsCorrectionDemande, demandeParent.getIdQdPrincipale(),
                        montantARestituerQdBigDec, Boolean.TRUE);

                getMemoryLog().logMessage(
                        getSession().getLabel("PROCESS_PREPARER_DECISIONS_CORRECTION_PLUSIEURS_ANNEES") + " "
                                + demandeCourante.getIdDemande() + " (" + demandeCourante.getNss() + ") "
                                + ": Ré-allocation de " + montantARestituerQdBigDec.toString()
                                + " sur la grande Qd N° " + demandeParent.getIdQdPrincipale() + " pour l'année "
                                + String.valueOf(dateDebutTraitementParentJADate.getYear()), FWMessage.INFORMATION,
                        "RFPreparerDecisionsProcess.ajouterQdAImputerCorrection()");

                demandeCourante.setRestitutionIdDecisionRFMAccordee(demandeParent.getIdDecision());

                // Réallocation sur la petite Qd
                if (!JadeStringUtil.isBlankOrZero(demandeParent.getIdQdAssure())) {
                    ajoutQdsCorrectionDemandeMap(qdsCorrectionDemande, demandeParent.getIdQdAssure(),
                            montantARestituerQdBigDec, Boolean.FALSE);
                    getMemoryLog().logMessage(
                            getSession().getLabel("PROCESS_PREPARER_DECISIONS_CORRECTION_PLUSIEURS_ANNEES") + " "
                                    + demandeCourante.getIdDemande() + " (" + demandeCourante.getNss() + ") "
                                    + ": Ré-allocation de " + montantARestituerQdBigDec.toString()
                                    + " sur la petite Qd N° " + demandeParent.getIdQdAssure() + " pour l'année "
                                    + String.valueOf(dateDebutTraitementParentJADate.getYear()), FWMessage.INFORMATION,
                            "RFPreparerDecisionsProcess.ajouterQdAImputerCorrection()");
                }

                // Réallocation du montant déjà payé, si paiement futur, rien n'a été payé
                demandeCourante.setRestitutionMontantAPayeParent("");

            } else {
                // PAIEMENT ACTUEL -> COURANT, une partie à déja été payée
                // Recherche des Qds concernées et réallocation de l'argent
                // Recherche du montant total à restituer
                int anneeCouranteInt = dateDebutTraitementParentJADate.getYear();
                BigDecimal montantARestituerTotalBigDec = new BigDecimal("0");
                while (anneeCouranteInt <= dateDernierPaiementJADate.getYear()) {
                    // si l'on se trouve dans l'année de la date de début de traitement,
                    if (anneeCouranteInt == dateDebutTraitementParentJADate.getYear()) {

                        // Calcul du montant à restituer (= montant payé)
                        String dateDeFinAnneeCourantMntARestituer = "";
                        if (dateDebutTraitementParentJADate.getYear() == dateDernierPaiementJADate.getYear()) {

                            dateDeFinAnneeCourantMntARestituer = (dateDernierPaiementJADate.getMonth() < 10 ? "0"
                                    + String.valueOf(dateDernierPaiementJADate.getMonth()) : String
                                    .valueOf(dateDernierPaiementJADate.getMonth()))
                                    + "."
                                    + dateDernierPaiementJADate.getYear();

                            dateDeFinAnneeCourantMntARestituer = getDateFinDeMois_JJMMAAAA(dateDeFinAnneeCourantMntARestituer);

                        } else {
                            dateDeFinAnneeCourantMntARestituer = "31.12." + String.valueOf(anneeCouranteInt);
                        }
                        BigDecimal nbDeMoisARestituerBigDec = new BigDecimal(JadeDateUtil.getNbMonthsBetween(
                                demandeParent.getDateDebutTraitement(), dateDeFinAnneeCourantMntARestituer));
                        BigDecimal montantARestituerBigDec = new BigDecimal(demandeParent.getMontantMensuel().replace(
                                "'", "")).multiply(nbDeMoisARestituerBigDec);

                        montantARestituerTotalBigDec = montantARestituerTotalBigDec.add(montantARestituerBigDec);

                        // Calcul du montant à ré-allouer sur les Qds
                        String dateDeFinAnneeCouranteQd = "";
                        if (dateDebutTraitementParentJADate.getYear() == dateFinTraitementParentJADate.getYear()) {
                            dateDeFinAnneeCouranteQd = (dateFinTraitementParentJADate.getMonth() < 10 ? "0"
                                    + String.valueOf(dateFinTraitementParentJADate.getMonth()) : String
                                    .valueOf(dateFinTraitementParentJADate.getMonth()))
                                    + "."
                                    + dateFinTraitementParentJADate.getYear();

                            dateDeFinAnneeCouranteQd = getDateFinDeMois_JJMMAAAA(dateDeFinAnneeCouranteQd);

                        } else {
                            dateDeFinAnneeCouranteQd = "31.12." + String.valueOf(anneeCouranteInt);
                        }
                        BigDecimal nbDeMoisQdBigDec = new BigDecimal(JadeDateUtil.getNbMonthsBetween(
                                demandeParent.getDateDebutTraitement(), dateDeFinAnneeCouranteQd));
                        BigDecimal montantARestituerQdBigDec = new BigDecimal(demandeParent.getMontantMensuel()
                                .replace("'", "")).multiply(nbDeMoisQdBigDec);
                        String dateDeDebutAnneeCouranteQd = "01."
                                + (dateDebutTraitementParentJADate.getMonth() < 10 ? "0"
                                        + String.valueOf(dateDebutTraitementParentJADate.getMonth()) : String
                                        .valueOf(dateDebutTraitementParentJADate.getMonth())) + "."
                                + String.valueOf(dateDebutTraitementParentJADate.getYear());

                        reallocationQdsRegime(demandeParent, demandeCourante, anneeCouranteInt,
                                dateDeDebutAnneeCouranteQd, montantARestituerQdBigDec, qdsCorrectionDemande,
                                transaction);

                    }// Cas où l'on se trouve dans l'année de la date de dernier paiement des rentes
                    else if (anneeCouranteInt == dateDernierPaiementJADate.getYear()) {

                        // Calcul du montant à restituer (= montant payé)
                        String dateDeFinAnneeCouranteARestituer = "";
                        dateDeFinAnneeCouranteARestituer = (dateDernierPaiementJADate.getMonth() < 10 ? "0"
                                + String.valueOf(dateDernierPaiementJADate.getMonth()) : String
                                .valueOf(dateDernierPaiementJADate.getMonth()))
                                + "."
                                + dateDernierPaiementJADate.getYear();

                        dateDeFinAnneeCouranteARestituer = getDateFinDeMois_JJMMAAAA(dateDeFinAnneeCouranteARestituer);

                        BigDecimal nbDeMoisMntARestituerBigDec = new BigDecimal(JadeDateUtil.getNbMonthsBetween(
                                "01.01." + String.valueOf(anneeCouranteInt), dateDeFinAnneeCouranteARestituer));
                        BigDecimal montantARestituerBigDec = new BigDecimal(demandeParent.getMontantMensuel().replace(
                                "'", "")).multiply(nbDeMoisMntARestituerBigDec);

                        montantARestituerTotalBigDec = montantARestituerTotalBigDec.add(montantARestituerBigDec);

                        // Calcul du montant à re-allouer sur les Qds
                        String dateDeFinAnneeCouranteQd = "";
                        if (dateFinTraitementParentJADate.getYear() == dateDernierPaiementJADate.getYear()) {
                            dateDeFinAnneeCouranteQd = (dateFinTraitementParentJADate.getMonth() < 10 ? "0"
                                    + String.valueOf(dateFinTraitementParentJADate.getMonth()) : String
                                    .valueOf(dateFinTraitementParentJADate.getMonth()))
                                    + "."
                                    + dateFinTraitementParentJADate.getYear();

                            dateDeFinAnneeCouranteQd = getDateFinDeMois_JJMMAAAA(dateDeFinAnneeCouranteQd);

                        } else {
                            dateDeFinAnneeCouranteQd = "31.12." + String.valueOf(anneeCouranteInt);
                        }
                        BigDecimal nbDeMoisQdBigDec = new BigDecimal(JadeDateUtil.getNbMonthsBetween(
                                "01.01." + String.valueOf(anneeCouranteInt), dateDeFinAnneeCouranteQd));
                        BigDecimal montantARestituerQdBigDec = new BigDecimal(demandeParent.getMontantMensuel()
                                .replace("'", "")).multiply(nbDeMoisQdBigDec);

                        // ré-allocation sur Qd
                        reallocationQdsRegime(demandeParent, demandeCourante, anneeCouranteInt,
                                "01.01." + String.valueOf(anneeCouranteInt), montantARestituerQdBigDec,
                                qdsCorrectionDemande, transaction);

                    } else {
                        // Le régime a duré toute l'année courante autant pour la restitution du montant payé que la
                        // réallocation sur la Qd
                        BigDecimal montantARestituerBigDec = new BigDecimal(demandeParent.getMontantMensuel().replace(
                                "'", "")).multiply(new BigDecimal("12"));

                        montantARestituerTotalBigDec = montantARestituerTotalBigDec.add(montantARestituerBigDec);

                        reallocationQdsRegime(demandeParent, demandeCourante, anneeCouranteInt,
                                "01.01." + String.valueOf(anneeCouranteInt), montantARestituerBigDec,
                                qdsCorrectionDemande, transaction);
                    }

                    anneeCouranteInt++;
                }

                demandeCourante.setRestitutionIdDecisionRFMAccordee(demandeParent.getIdDecision());
                demandeCourante.setRestitutionMontantAPayeParent(montantARestituerTotalBigDec.toString());

            }
        } else {
            // PAIEMENT RETROACTIF, tout
            int anneeCouranteInt = dateDebutTraitementParentJADate.getYear();
            BigDecimal montantARestituerTotalBigDec = new BigDecimal("0");
            while (anneeCouranteInt <= dateFinTraitementParentJADate.getYear()) {
                // Lorsque l'on se trouve dans l'année de la date de début de traitement,
                // la somme pré-allouée sur la qd dépend du nombre de mois entre la date de déb
                // et de fin (si pas de date de fin-> fin année)
                if (anneeCouranteInt == dateDebutTraitementParentJADate.getYear()) {

                    String dateDeFinAnneeCourante = "";
                    if (dateDebutTraitementParentJADate.getYear() == dateFinTraitementParentJADate.getYear()) {
                        dateDeFinAnneeCourante = (dateFinTraitementParentJADate.getMonth() < 10 ? "0"
                                + String.valueOf(dateFinTraitementParentJADate.getMonth()) : String
                                .valueOf(dateFinTraitementParentJADate.getMonth()))
                                + "."
                                + dateFinTraitementParentJADate.getYear();

                        dateDeFinAnneeCourante = getDateFinDeMois_JJMMAAAA(dateDeFinAnneeCourante);

                    } else {
                        dateDeFinAnneeCourante = "31.12." + String.valueOf(anneeCouranteInt);
                    }

                    String dateDeDebutAnneeCourante = "01."
                            + (dateDebutTraitementParentJADate.getMonth() < 10 ? "0"
                                    + String.valueOf(dateDebutTraitementParentJADate.getMonth()) : String
                                    .valueOf(dateDebutTraitementParentJADate.getMonth())) + "."
                            + dateDebutTraitementParentJADate.getYear();

                    BigDecimal nbDeMoisBigDec = new BigDecimal(JadeDateUtil.getNbMonthsBetween(
                            demandeParent.getDateDebutTraitement(), dateDeFinAnneeCourante));

                    BigDecimal montantARestituerBigDec = new BigDecimal(demandeParent.getMontantMensuel().replace("'",
                            "")).multiply(nbDeMoisBigDec);

                    montantARestituerTotalBigDec = montantARestituerTotalBigDec.add(montantARestituerBigDec);

                    reallocationQdsRegime(demandeParent, demandeCourante, anneeCouranteInt, dateDeDebutAnneeCourante,
                            montantARestituerBigDec, qdsCorrectionDemande, transaction);

                }// Cas où l'on se trouve dans l'année de la date de fin traitement
                else if (anneeCouranteInt == dateFinTraitementParentJADate.getYear()) {

                    String dateDeFinAnneeCourante = dateFinTraitementParentJADate.getMonth() + "."
                            + dateFinTraitementParentJADate.getYear();

                    dateDeFinAnneeCourante = getDateFinDeMois_JJMMAAAA(dateDeFinAnneeCourante);

                    BigDecimal nbDeMoisBigDec = new BigDecimal(JadeDateUtil.getNbMonthsBetween(
                            "01.01." + String.valueOf(anneeCouranteInt), dateDeFinAnneeCourante));

                    BigDecimal montantARestituerBigDec = new BigDecimal(demandeParent.getMontantMensuel().replace("'",
                            "")).multiply(nbDeMoisBigDec);

                    montantARestituerTotalBigDec = montantARestituerTotalBigDec.add(montantARestituerBigDec);

                    // L'id Qd correspond à la demande, car l'adaptation annuelle l'aura mis à jour
                    reallocationQdsRegime(demandeParent, demandeCourante, anneeCouranteInt,
                            "01.01." + String.valueOf(anneeCouranteInt), montantARestituerBigDec, qdsCorrectionDemande,
                            transaction);

                } else {
                    // Le régime a duré toute l'année courante
                    BigDecimal montantARestituerBigDec = new BigDecimal(demandeParent.getMontantMensuel().replace("'",
                            "")).multiply(new BigDecimal("12"));

                    montantARestituerTotalBigDec = montantARestituerTotalBigDec.add(montantARestituerBigDec);

                    reallocationQdsRegime(demandeParent, demandeCourante, anneeCouranteInt,
                            "01.01." + String.valueOf(anneeCouranteInt), montantARestituerBigDec, qdsCorrectionDemande,
                            transaction);
                }

                anneeCouranteInt++;
            }

            demandeCourante.setRestitutionIdDecisionRFMAccordee(demandeParent.getIdDecision());
            demandeCourante.setRestitutionMontantAPayeParent(montantARestituerTotalBigDec.toString());

        }

    }

    /**
     * Recherche des demandes à imputer
     * 
     * @param demandesAimputerMap
     * @param transaction
     * @throws Exception
     */
    private void rechercherDemandesAImputer(Map<String, RFImputationDemandesData> demandesAimputerMap,
            Set<String> idQdsImputationForcee, Map<String, Set<String>> forcePaiementGestsIdsDemandes,
            BITransaction transaction) throws Exception {

        JACalendar cal = new JACalendarGregorian();

        Iterator<RFTypesDemandeJointDossierJointTiers> demandesATraiterItr = demandesATraiterList.iterator();

        while (demandesATraiterItr.hasNext()) {

            RFTypesDemandeJointDossierJointTiers rfTypDemJointDosJointTie = demandesATraiterItr.next();

            if (null != rfTypDemJointDosJointTie) {

                RFImputationDemandesData demandeCourante = new RFImputationDemandesData();

                demandeCourante.setIdDemande(rfTypDemJointDosJointTie.getIdDemande());
                demandeCourante.setIdDemandeParent(rfTypDemJointDosJointTie.getIdDemandeParent());
                demandeCourante.setIdGestionnaire(rfTypDemJointDosJointTie.getIdGestionnaire());
                demandeCourante.setCodeTypeDeSoin(rfTypDemJointDosJointTie.getCodeTypeDeSoin());
                demandeCourante.setCodeSousTypeDeSoin(rfTypDemJointDosJointTie.getCodeSousTypeDeSoin());
                demandeCourante.setCsSousTypeDeSoin(rfTypDemJointDosJointTie.getIdSousTypeDeSoin());
                demandeCourante.setIsRetro(rfTypDemJointDosJointTie.getIsRetro());
                // On initialise le montant accepté de la demande à traiter par le montant a payer, ainsi on ne se
                // préocupe plus des motifs de refus utilisateurs :-)
                demandeCourante.setMontantAccepte(rfTypDemJointDosJointTie.getMontantAPayer().replace("'", ""));
                demandeCourante.setMontantAPayerInitial(rfTypDemJointDosJointTie.getMontantAPayer().replace("'", ""));
                demandeCourante.setDateReception(rfTypDemJointDosJointTie.getDateReception());
                demandeCourante.setDateFacture(rfTypDemJointDosJointTie.getDateFacture());
                demandeCourante.setDateDeces(rfTypDemJointDosJointTie.getDateDeces());
                demandeCourante.setIdTiers(rfTypDemJointDosJointTie.getIdTiers());
                demandeCourante.setNss(rfTypDemJointDosJointTie.getNss());
                demandeCourante.setIdDossier(rfTypDemJointDosJointTie.getIdDossier());
                demandeCourante.setDateDebutTraitement(rfTypDemJointDosJointTie.getDateDebutTraitement());
                demandeCourante.setDateFinTraitement(rfTypDemJointDosJointTie.getDateFinTraitement());
                demandeCourante.setIdFournisseur(rfTypDemJointDosJointTie.getIdFournisseur());
                demandeCourante.setIdAdressePaiement(rfTypDemJointDosJointTie.getIdAdressePaiement());
                demandeCourante.setCsEtat(rfTypDemJointDosJointTie.getCsEtat());
                demandeCourante.setMontantFacture44(rfTypDemJointDosJointTie.getMontantFacture44().replace("'", ""));
                // demandeCourante.setMontantFacture(rfTypDemJointDosJointTie.getMontantFacture().replace("'", ""));
                demandeCourante.setMontantVerseOAI(rfTypDemJointDosJointTie.getMontantVerseOAI().replace("'", ""));
                demandeCourante.setDateDemande(rfTypDemJointDosJointTie.getDateDemande());
                demandeCourante.setMontantMensuel(rfTypDemJointDosJointTie.getMontantMensuel());
                demandeCourante.setCsSource(rfTypDemJointDosJointTie.getCsSource());

                if (!JadeStringUtil.isBlankOrZero(rfTypDemJointDosJointTie.getIdQdPrincipale())) {
                    demandeCourante.setIdQdPrincipale(rfTypDemJointDosJointTie.getIdQdPrincipale());
                    demandeCourante.setIsForcerImputation(true);
                    idQdsImputationForcee.add(demandeCourante.getIdQdPrincipale());
                }

                demandeCourante.setForcerPaiement(rfTypDemJointDosJointTie.getIsForcerPaiement().booleanValue());

                if (rfTypDemJointDosJointTie.getIsForcerPaiement().booleanValue()) {
                    if (forcePaiementGestsIdsDemandes.containsKey(demandeCourante.getIdGestionnaire())) {
                        Set<String> idsDemande = forcePaiementGestsIdsDemandes.get(demandeCourante.getIdGestionnaire());
                        idsDemande.add(demandeCourante.getIdDemande());
                    } else {
                        Set<String> idsDemande = new HashSet<String>();
                        idsDemande.add(demandeCourante.getIdDemande());
                        forcePaiementGestsIdsDemandes.put(demandeCourante.getIdGestionnaire(), idsDemande);
                    }
                }

                demandeCourante.setPP(rfTypDemJointDosJointTie.getIsPP().booleanValue());

                JADate dateDeDebutDeTraitement = new JADate(demandeCourante.getDateDebutTraitement());
                JADate dateDeFinDeTraitement = new JADate(demandeCourante.getDateFinTraitement());

                // Recherche si la demande concerne un paiement mensuel
                if (IRFCodeTypesDeSoins.TYPE_2_REGIME_ALIMENTAIRE.equals(demandeCourante.getCodeTypeDeSoin())) {

                    if (dateDeDebutDeTraitement.getYear() != dateDeFinDeTraitement.getYear()) {
                        dateDeFinDeTraitement = new JADate("01.12." + dateDeDebutDeTraitement.getYear());
                    }

                    demandeCourante.setPaiementMensuel(true);

                    // Recherche du type de paiement
                    demandeCourante.setDateDernierPaiement_mm_yyyy(RFUtils.getDateDernierPaiementMensuelRente(
                            dateDernierPaiementMensuelRente, getSession()));
                    if (((cal.compare(dateDeFinDeTraitement,
                            new JADate(demandeCourante.getDateDernierPaiement_mm_yyyy())) == JACalendar.COMPARE_FIRSTUPPER))) {

                        if (((cal.compare(dateDeDebutDeTraitement,
                                new JADate(demandeCourante.getDateDernierPaiement_mm_yyyy())) == JACalendar.COMPARE_FIRSTUPPER))) {
                            demandeCourante.setTypeDePaiment(IRFTypePaiement.PAIEMENT_FUTURE);
                        } else {
                            demandeCourante.setTypeDePaiment(IRFTypePaiement.PAIEMENT_COURANT);
                        }
                    } else {
                        demandeCourante.setTypeDePaiment(IRFTypePaiement.PAIEMENT_RETROACTIF);
                    }
                } else {

                    if (IRFCodeTypesDeSoins.TYPE_17_FRANCHISE_ET_QUOTEPARTS.equals(demandeCourante.getCodeTypeDeSoin())) {

                        if (demandeCourante.isPP()) {
                            throw new Exception(
                                    "RFPreparerDecisionsProcess.rechercherDemandesAImputer(): Demande frqp PP créée manuellement");
                        } else {
                            demandeCourante.setDateDernierPaiement_mm_yyyy(RFUtils.getDateDernierPaiementMensuelRente(
                                    dateDernierPaiementMensuelRente, getSession()));

                            demandeCourante.setTypeDePaiment(IRFTypePaiement.PAIEMENT_RETROACTIF);
                        }

                    } else {
                        demandeCourante.setTypeDePaiment(IRFTypePaiement.PAIEMENT_RETROACTIF);
                    }
                }

                demandesAimputerMap.put(demandeCourante.getIdDemande(), demandeCourante);
            }
        }
    }

    public void setDateDernierPaiementMensuelRente(String dateDernierPaiementMensuelRente) {
        this.dateDernierPaiementMensuelRente = dateDernierPaiementMensuelRente;
    }

    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
    }

    public void setIdExecutionProcess(String idExecutionProcess) {
        this.idExecutionProcess = idExecutionProcess;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setMemoryLog(FWMemoryLog memoryLog) {
        this.memoryLog = memoryLog;
    }

    public void setSession(BISession session) {
        this.session = session;
    }

    public void setTransaction(BITransaction transaction) {
        this.transaction = transaction;
    }

    /**
     * Vérifie si les Qds principales impactées par le calcul ne concerne pas le calcul d'un autre gestionnaire
     * 
     * 
     * @param idQdPrincipaleSet
     * @throws Exception
     */
    private void verifierSiCalculConcerneAutreGestionnaire(Set<String> idQdPrincipaleSet) throws Exception {

        if (idQdPrincipaleSet.size() > 0) {

            RFQdPrincipaleManager rfQdPriMgr = new RFQdPrincipaleManager();
            rfQdPriMgr.setSession(getSession());
            rfQdPriMgr.setForNotIdGesModSolExcPreDec(idGestionnaire);
            rfQdPriMgr.setForIdsQd(idQdPrincipaleSet);
            rfQdPriMgr.changeManagerSize(0);
            rfQdPriMgr.find();

            if (rfQdPriMgr.size() > 0) {

                StringBuffer gestionnaires = new StringBuffer();
                Iterator<RFQdPrincipale> rfQdPriItr = rfQdPriMgr.iterator();
                List<String> gestList = new ArrayList<String>();

                while (rfQdPriItr.hasNext()) {
                    RFQdPrincipale qdCourante = rfQdPriItr.next();
                    if (qdCourante != null) {

                        // Get Gestionnaire
                        String idGestMod = qdCourante.getIdGesModSoldeExcedentAugmentationQdPreDec();
                        if (!gestList.contains(idGestMod)) {
                            // On ajoute une virgule si le gestionnaire ajouté n'est pas le premier
                            if (gestionnaires.length() != 0) {
                                gestionnaires.append(", ");
                            }

                            gestList.add(idGestMod);
                            gestionnaires.append(PRGestionnaireHelper.getNomGestionnaire(idGestMod));
                        }

                    } else {
                        throw new Exception(getSession().getLabel("ERREUR_PREPARER_DECISIONS_QD_INTROUVABLE"));
                    }
                }

                throw new Exception(getSession().getLabel("ERREUR_PREPARER_DECISIONS_CALCULS_AUTRES_GESTIONNAIRES")
                        + " " + gestionnaires.toString());
            }
        }
    }
}
