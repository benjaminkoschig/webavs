package globaz.cygnus.process.adaptationAnnuelle.frqppp;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.lots.IRELot;
import globaz.corvus.dao.IREValidationLevel;
import globaz.corvus.dao.REInfoCompta;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.cygnus.api.TypesDeSoins.IRFCodeTypesDeSoins;
import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.api.paiement.IRFPrestations;
import globaz.cygnus.api.paiement.IRFTypePaiement;
import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.demandes.RFDemandeFrq17Fra18;
import globaz.cygnus.db.demandes.RFPrDemandeJointDossier;
import globaz.cygnus.db.motifsDeRefus.RFMotifsDeRefus;
import globaz.cygnus.db.motifsDeRefus.RFMotifsDeRefusManager;
import globaz.cygnus.db.paiement.RFLotManager;
import globaz.cygnus.db.paiement.RFPrestationAccordee;
import globaz.cygnus.db.paiement.RFPrestationAccordeeJointREPrestationAccordee;
import globaz.cygnus.db.paiement.RFPrestationAccordeeJointREPrestationAccordeeManager;
import globaz.cygnus.db.qds.RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande;
import globaz.cygnus.db.qds.RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager;
import globaz.cygnus.services.adaptationJournaliere.RFAdaptationJournaliereContext;
import globaz.cygnus.services.preparerDecision.RFDecisionData;
import globaz.cygnus.services.preparerDecision.RFGenererDecisionsEntitesService;
import globaz.cygnus.services.preparerDecision.RFImputationDemandesData;
import globaz.cygnus.services.preparerDecision.RFImputationDemandesService;
import globaz.cygnus.services.preparerDecision.RFImputationQdsData;
import globaz.cygnus.services.preparerDecision.RFPersistanceImputationDemandesDecisionsService;
import globaz.cygnus.utils.RFUtils;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRDateFormater;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.decision.DecisionPcVO;
import com.google.gson.Gson;

public class RFProcessAdaptationAnnuelleFrqpppHandlerEntity implements JadeProcessEntityInterface,
        JadeProcessEntityNeedProperties {

    private final String IDDEMANDEVIRTUELLE = "IDDEMANDEVIRTUELLE";
    private List<String[]> logsList = null;
    DecisionPcVO pcVOs = null;
    private Map<Enum<?>, String> properties = null;

    public RFProcessAdaptationAnnuelleFrqpppHandlerEntity(List<String[]> logsList) {
        this.logsList = logsList;
    }

    private void creationDemandeNonCalculeEnBd(Map<String, RFImputationDemandesData> demandesAimputerMap,
            RFAdaptationJournaliereContext contextCourant) throws Exception {

        String idDemande = "";

        try {

            RFImputationDemandesData demData = (RFImputationDemandesData) demandesAimputerMap.values().toArray()[0];

            RFDemande rfDemande = new RFDemande();
            rfDemande.setSession(BSessionUtil.getSessionFromThreadContext());
            rfDemande.setDateDebutTraitement(demData.getDateDebutTraitement());
            rfDemande.setDateFacture(demData.getDateFacture());
            rfDemande.setDateFinTraitement(demData.getDateFinTraitement());
            rfDemande.setDateReception(demData.getDateDebutTraitement());
            rfDemande.setIdAdressePaiement(demData.getIdTiers());
            rfDemande.setIdDossier(RFUtils.getDossierJointPrDemande(demData.getIdTiers(),
                    BSessionUtil.getSessionFromThreadContext()).getIdDossier());
            rfDemande.setIdFournisseur("");
            rfDemande.setIdGestionnaire(BSessionUtil.getSessionFromThreadContext().getUserId());
            rfDemande.setIdSousTypeDeSoin(demData.getCsSousTypeDeSoin());
            rfDemande.setIsForcerPaiement(false);
            rfDemande.setIsContratDeTravail(false);
            rfDemande.setIsRetro(demData.getIsRetro());
            rfDemande.setIsPP(true);
            rfDemande.setMontantAPayer(demData.getMontantAPayerInitial());
            rfDemande.setMontantFacture(demData.getMontantAPayerInitial());
            rfDemande.setNumeroFacture("");
            rfDemande.setMontantMensuel("");
            rfDemande.setCsEtat(IRFDemande.ENREGISTRE);
            rfDemande.setCsSource(IRFDemande.ADAPTATION);
            rfDemande.setCsStatut(IRFDemande.ACCEPTE);

            rfDemande.add(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction());

            idDemande = rfDemande.getIdDemande();
            demData.setIdDemande(idDemande);

            RFDemandeFrq17Fra18 rfDemandeFrq17Fra18 = new RFDemandeFrq17Fra18();
            rfDemandeFrq17Fra18.setSession(BSessionUtil.getSessionFromThreadContext());
            rfDemandeFrq17Fra18.setCsGenreDeSoin(IRFDemande.AUTRES);
            rfDemandeFrq17Fra18.setDateDecompte(demData.getDateFacture());
            rfDemandeFrq17Fra18.setIdDemande1718(rfDemande.getIdDemande());
            rfDemandeFrq17Fra18.setMontantDecompte(demData.getMontantAPayerInitial());
            rfDemandeFrq17Fra18.setNumeroDecompte("");

            rfDemandeFrq17Fra18.add(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction());

        } catch (Exception e) {
            if (BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction() != null) {
                BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction().setRollbackOnly();
            }
            throw e;
        } finally {
            // TODO: Vérifier si pas d'effet de bord
            if (BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction() != null) {
                try {
                    if (BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction().hasErrors()
                            || BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction()
                                    .isRollbackOnly()) {

                        BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction().rollback();

                        RFUtils.ajouterLogAdaptation(
                                FWViewBeanInterface.ERROR,
                                contextCourant.getIdAdaptationJournaliere(),
                                contextCourant.getIdTiersBeneficiaire(),
                                contextCourant.getNssTiersBeneficiaire(),
                                contextCourant.getIdDecisionPc(),
                                contextCourant.getNumeroDecisionPc(),
                                "RFProcessAdaptationAnuelleRegimeHandlerEntity.creationDemandeNonCalculeEnBd(): impossible de créer la demande",
                                getLogsList());
                    } else {
                        BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction().commit();

                        RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING,
                                contextCourant.getIdAdaptationJournaliere(), contextCourant.getIdTiersBeneficiaire(),
                                contextCourant.getNssTiersBeneficiaire(), contextCourant.getIdDecisionPc(),
                                contextCourant.getNumeroDecisionPc(), "Création de la demande frqp pp N° " + idDemande,
                                getLogsList());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

    }

    /**
     * Recherche du lot ouvert ou création si celui-ci n'existe pas. CCJU : 1 lot ouvert par gestionnaire, CCVD : 1 lot
     * ouvert pour tous En fonction de la valeur de hasDecisionPonctuelle et hasDecisionMensuelle :
     * 
     * @return String
     * @throws Exception
     */
    private String creationLot(String typeLot) throws Exception {

        RFLotManager lotManager = RFUtils.retrieveLotOuvert(BSessionUtil.getSessionFromThreadContext(), typeLot);

        // si pas de lot RFM, on le créé
        if (lotManager.size() == 0) {

            RELot lotRente = RFUtils.addRELot(typeLot, BSessionUtil.getSessionFromThreadContext(), BSessionUtil
                    .getSessionFromThreadContext().getCurrentThreadTransaction());

            RFUtils.addRFLot(lotRente, "", BSessionUtil.getSessionFromThreadContext(), BSessionUtil
                    .getSessionFromThreadContext().getCurrentThreadTransaction());
            return lotRente.getIdLot();

        } else {
            if (lotManager.size() == 1) {
                return ((RELot) lotManager.getFirstEntity()).getIdLot();

            } else {
                throw new Exception(
                        "RFProcessAdaptationAnnuelleFrqpppHandlerEntity.creationLot():Plusieurs lots trouvées (RFGenererPaiementService::creationLot)");
            }
        }
    }

    private void creationRfmAccordeesFrqpPP(RFImputationQdsData qdPrincipale, String montantMensuel,
            String dateDebut_mmaaaa, String dateFin_mmaaaa, String dateAugmentation, String dateDiminution,
            String idTiersBeneficiaire, String idDecision, String idParentAdaptation,
            RFAdaptationJournaliereContext contextCourant) throws Exception {

        // Créer un enregistrement dans REINCOM
        REInformationsComptabilite ic = new REInformationsComptabilite();
        ic.setSession(BSessionUtil.getSessionFromThreadContext());
        // Recherche de l'adresse de paiement
        ic.setAdressePaiement(PRTiersHelper.getAdressePaiementData(BSessionUtil.getSessionFromThreadContext(),
                BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction(), idTiersBeneficiaire,
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA()));

        ic.add(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction());

        REPrestationsAccordees rePrestationAccordee = new REPrestationsAccordees();
        rePrestationAccordee.setSession(BSessionUtil.getSessionFromThreadContext());
        rePrestationAccordee.setMontantPrestation(montantMensuel);
        rePrestationAccordee.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);

        rePrestationAccordee.setDateDebutDroit(dateDebut_mmaaaa);
        rePrestationAccordee.setDateFinDroit(dateFin_mmaaaa);

        boolean isSousTypesGenrePrestationActif = CommonProperties.SOUS_TYPE_GENRE_PRESTATION_ACTIF.getBooleanValue();

        if (qdPrincipale.getCsTypePcAccordee().equals(IPCPCAccordee.CS_TYPE_PC_INVALIDITE)) {
            rePrestationAccordee.setCodePrestation("250");

            if (isSousTypesGenrePrestationActif) {
                rePrestationAccordee.setSousTypeGenrePrestation("215");
            }

        } else if (qdPrincipale.getCsTypePcAccordee().equals(IPCPCAccordee.CS_TYPE_PC_SURVIVANT)) {
            rePrestationAccordee.setCodePrestation("213");

            if (isSousTypesGenrePrestationActif) {
                rePrestationAccordee.setSousTypeGenrePrestation("201");
            }

        } else if (qdPrincipale.getCsTypePcAccordee().equals(IPCPCAccordee.CS_TYPE_PC_VIELLESSE)) {
            rePrestationAccordee.setCodePrestation("210");

            if (isSousTypesGenrePrestationActif) {
                rePrestationAccordee.setSousTypeGenrePrestation("201");
            }

        } else {
            throw new Exception(
                    "RFProcessAdaptationAnnuelleFrqpppHandlerEntity.creationRfmAccordeesFrqpPP: Impossible de détérminer le type PC de la prestation accordée");
        }

        rePrestationAccordee.setCsGenre(IREPrestationAccordee.CS_GENRE_RFM);
        rePrestationAccordee.setIdTiersBeneficiaire(idTiersBeneficiaire);
        rePrestationAccordee.setIdInfoCompta(ic.getIdInfoCompta());

        rePrestationAccordee.add(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction());

        RFPrestationAccordee prestAcc = new RFPrestationAccordee();
        prestAcc.setSession(BSessionUtil.getSessionFromThreadContext());
        prestAcc.setIdDecision(idDecision);
        prestAcc.setCsTypeRFA(qdPrincipale.getCsTypePcAccordee());
        prestAcc.setIdRFMAccordee(rePrestationAccordee.getIdPrestationAccordee());
        prestAcc.setIdAdressePaiement(idTiersBeneficiaire);
        prestAcc.setIdParentAdaptation(idParentAdaptation);

        prestAcc.setIsAdaptation(true);

        prestAcc.setDateDiminution(dateDiminution);
        prestAcc.setDateAugmentation(dateAugmentation);

        prestAcc.setCs_source(IRFPrestations.CS_SOURCE_RFACCORDEES_FRQP);
        prestAcc.setIdLot(creationLot(IRELot.CS_TYP_LOT_MENSUEL));

        prestAcc.add(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction());

        REInformationsComptabilite ic2 = new REInformationsComptabilite();
        ic2.setIdInfoCompta(rePrestationAccordee.getIdInfoCompta());
        ic2.setSession(BSessionUtil.getSessionFromThreadContext());
        ic2.retrieve(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction());

        if (JadeStringUtil.isBlankOrZero(ic2.getIdCompteAnnexe())) {
            REInfoCompta.initCompteAnnexe_noCommit(BSessionUtil.getSessionFromThreadContext(), BSessionUtil
                    .getSessionFromThreadContext().getCurrentThreadTransaction(), rePrestationAccordee
                    .getIdTiersBeneficiaire(), ic2, IREValidationLevel.VALIDATION_LEVEL_ALL);
        }

        RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING, contextCourant.getIdAdaptationJournaliere(),
                contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(),
                "Création de la prestation accordée N° " + prestAcc.getIdRFMAccordee(), getLogsList());

    }

    private String diminuerPrestationAccordee(RFAdaptationJournaliereContext contextCourant) throws Exception {

        String idParentAdaptation = "";

        // Recherche si le bénéficiaire possède une RFAccordée
        RFPrestationAccordeeJointREPrestationAccordeeManager rfPreAccJoiRePreAccMgr = new RFPrestationAccordeeJointREPrestationAccordeeManager();
        rfPreAccJoiRePreAccMgr.setSession(BSessionUtil.getSessionFromThreadContext());
        rfPreAccJoiRePreAccMgr.setForIdTiersBeneficiaire(pcVOs.getIdTiersBeneficiaire());
        rfPreAccJoiRePreAccMgr.setForEnCoursAtMois("01." + String.valueOf(JACalendar.today().getYear() + 1));
        rfPreAccJoiRePreAccMgr.setForCsSourceRfmAccordee(new String[] { IRFPrestations.CS_SOURCE_RFACCORDEES_FRQP });
        rfPreAccJoiRePreAccMgr.changeManagerSize(0);
        rfPreAccJoiRePreAccMgr.find();

        Iterator<RFPrestationAccordeeJointREPrestationAccordee> rfPreAccJoiRePreAccItr = rfPreAccJoiRePreAccMgr
                .iterator();

        String dateDiminution = "12." + String.valueOf(JACalendar.today().getYear());

        while (rfPreAccJoiRePreAccItr.hasNext()) {

            RFPrestationAccordeeJointREPrestationAccordee prestationCourante = rfPreAccJoiRePreAccItr.next();

            idParentAdaptation = prestationCourante.getIdRFMAccordee();

            RFPrestationAccordee rfPrestationAcc = new RFPrestationAccordee();
            rfPrestationAcc.setSession(BSessionUtil.getSessionFromThreadContext());
            rfPrestationAcc.setIdRFMAccordee(prestationCourante.getIdRFMAccordee());

            rfPrestationAcc.retrieve();

            if (!rfPrestationAcc.isNew()) {

                rfPrestationAcc.setDateDiminution("01." + dateDiminution);
                rfPrestationAcc.setIsAdaptation(true);
                rfPrestationAcc.update(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction());

                REPrestationsAccordees rePrestationAccordee = new REPrestationsAccordees();
                rePrestationAccordee.setSession(BSessionUtil.getSessionFromThreadContext());
                rePrestationAccordee.setIdPrestationAccordee(prestationCourante.getIdRFMAccordee());

                rePrestationAccordee.retrieve();

                if (!rePrestationAccordee.isNew()) {

                    rePrestationAccordee.setDateFinDroit(dateDiminution);
                    rePrestationAccordee.update(BSessionUtil.getSessionFromThreadContext()
                            .getCurrentThreadTransaction());

                    RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING,
                            contextCourant.getIdAdaptationJournaliere(), contextCourant.getIdTiersBeneficiaire(),
                            contextCourant.getNssTiersBeneficiaire(), contextCourant.getIdDecisionPc(),
                            contextCourant.getNumeroDecisionPc(), "Diminution de la la prestation accordée N°"
                                    + prestationCourante.getIdRFMAccordee(), getLogsList());

                } else {
                    RFUtils.ajouterLogAdaptation(
                            FWViewBeanInterface.ERROR,
                            contextCourant.getIdAdaptationJournaliere(),
                            contextCourant.getIdTiersBeneficiaire(),
                            contextCourant.getNssTiersBeneficiaire(),
                            contextCourant.getIdDecisionPc(),
                            contextCourant.getNumeroDecisionPc(),
                            "Impossible de retrouver la prestation accordée RE N°"
                                    + prestationCourante.getIdRFMAccordee(), getLogsList());

                    throw new Exception(
                            "RFProcessAdaptationAnnuelleFrqpppHandlerEntity.diminuerPrestationAccordee(): impossible de retrouver la demande, mgr.size != 0");
                }
            } else {

                RFUtils.ajouterLogAdaptation(
                        FWViewBeanInterface.ERROR,
                        contextCourant.getIdAdaptationJournaliere(),
                        contextCourant.getIdTiersBeneficiaire(),
                        contextCourant.getNssTiersBeneficiaire(),
                        contextCourant.getIdDecisionPc(),
                        contextCourant.getNumeroDecisionPc(),
                        "Impossible de retrouver la prestation accordée RFM N°" + prestationCourante.getIdRFMAccordee(),
                        getLogsList());

                throw new Exception(
                        "RFProcessAdaptationAnnuelleFrqpppHandlerEntity.diminuerPrestationAccordee(): impossible de retrouver la demande, mgr.size != 0");

            }

        }

        return idParentAdaptation;
    }

    private String getDescriptionMotifDeRefus(RFImputationDemandesData demande) throws Exception {

        Set<String> idsMotifRefusSystemSet = new HashSet<String>();
        StringBuffer descStrBuf = new StringBuffer();

        for (String[] motifCourant : demande.getMotifsDeRefus()) {
            idsMotifRefusSystemSet.add(motifCourant[0]);
        }

        RFMotifsDeRefusManager rfMotifRefusMgr = new RFMotifsDeRefusManager();
        rfMotifRefusMgr.setSession(BSessionUtil.getSessionFromThreadContext());
        rfMotifRefusMgr.setForIdsMotifRefus(idsMotifRefusSystemSet);

        rfMotifRefusMgr.find();

        Iterator<RFMotifsDeRefus> rfMotifRefusItr = rfMotifRefusMgr.iterator();
        int i = 0;
        while (rfMotifRefusItr.hasNext()) {
            RFMotifsDeRefus motifCourant = rfMotifRefusItr.next();
            if (i == 0) {
                descStrBuf.append(motifCourant.getDescriptionFR());
            } else {
                descStrBuf.append(", " + motifCourant.getDescriptionFR());
            }
            i++;
        }

        return descStrBuf.toString();

    }

    public List<String[]> getLogsList() {
        return logsList;
    }

    private RFImputationDemandesData initDemandeACreer(String idTiersBeneficiaire, String anneeAdaptationStr,
            RFAdaptationJournaliereContext contextCourant) throws Exception {

        RFImputationDemandesData demandeCourante = new RFImputationDemandesData();

        demandeCourante.setIdDemande(IDDEMANDEVIRTUELLE);
        demandeCourante.setIdDemandeParent("");
        demandeCourante.setIdGestionnaire(BSessionUtil.getSessionFromThreadContext().getUserId());
        demandeCourante.setCodeTypeDeSoin(IRFCodeTypesDeSoins.TYPE_17_FRANCHISE_ET_QUOTEPARTS);
        demandeCourante.setCodeSousTypeDeSoin(IRFCodeTypesDeSoins.SOUS_TYPE_17_1_FRANCHISE_ET_QUOTEPARTS);
        demandeCourante.setCsSousTypeDeSoin(IRFTypesDeSoins.st_17_FRANCHISE_ET_QUOTEPARTS);

        demandeCourante.setDateReception("01.01." + anneeAdaptationStr);
        demandeCourante.setDateFacture("01.01." + anneeAdaptationStr);
        demandeCourante.setIdTiers(idTiersBeneficiaire);

        RFPrDemandeJointDossier dossier = RFUtils.getDossierJointPrDemande(idTiersBeneficiaire,
                BSessionUtil.getSessionFromThreadContext());
        if (dossier == null) {
            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, contextCourant.getIdAdaptationJournaliere(),
                    contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                    contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(),
                    "Impossible de trouver le dossier", getLogsList());
            StringBuilder sb = new StringBuilder();
            sb.append("Impossible de trouver le dossier pour le NSS : ");
            sb.append(contextCourant.getNssTiersBeneficiaire());
            sb.append(", ");
            sb.append("idTiers bénéficiaire : ");
            sb.append(contextCourant.getIdTiersBeneficiaire());
            sb.append(", ");
            sb.append("idDecisionPC : ");
            sb.append(contextCourant.getIdDecisionPc());
            sb.append(", ");
            sb.append("numeroDecisionPC : ");
            sb.append(contextCourant.getNumeroDecisionPc());
            throw new Exception(sb.toString());
        }
        demandeCourante.setIdDossier(dossier.getIdDossier());

        demandeCourante.setDateDebutTraitement("01.01." + anneeAdaptationStr);
        demandeCourante.setDateFinTraitement("31.12." + anneeAdaptationStr);

        demandeCourante.setMontantAccepte(RFUtils.MONTANT_INITIAL_FRQP_PP);
        demandeCourante.setMontantAPayerInitial(RFUtils.MONTANT_INITIAL_FRQP_PP);

        demandeCourante.setIdFournisseur("");
        demandeCourante.setIdAdressePaiement(idTiersBeneficiaire);
        demandeCourante.setCsEtat(IRFDemande.ENREGISTRE);
        demandeCourante.setMontantFacture44("");
        // demandeCourante.setMontantFacture("");
        demandeCourante.setMontantVerseOAI("");
        demandeCourante.setDateDemande("01.01." + anneeAdaptationStr);
        demandeCourante.setCsSource(IRFDemande.SYSTEME);
        demandeCourante.setForcerPaiement(false);
        demandeCourante.setPP(false);

        // Recherche si la demande concerne un paiement mensuel
        demandeCourante.setPaiementMensuel(true);
        // Recherche du type de paiement
        demandeCourante.setTypeDePaiment(IRFTypePaiement.PAIEMENT_FUTURE);

        return demandeCourante;
    }

    private void majIdsQdPrincipaleDemandeCourante(Map<String, RFImputationDemandesData> demandesAImputerMap,
            RFAdaptationJournaliereContext contextCourant) throws Exception {

        RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager rfQdJointPerValJointDosJointTieJointDemMgr = new RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager();
        rfQdJointPerValJointDosJointTieJointDemMgr.setSession(BSessionUtil.getSessionFromThreadContext());
        rfQdJointPerValJointDosJointTieJointDemMgr.setForCsGenreQd(IRFQd.CS_GRANDE_QD);
        rfQdJointPerValJointDosJointTieJointDemMgr.setForNotCsEtatQd(IRFQd.CS_ETAT_QD_CLOTURE);
        rfQdJointPerValJointDosJointTieJointDemMgr.setForIdTiers(pcVOs.getIdTiersBeneficiaire());

        RFImputationDemandesData demandeCourante = (RFImputationDemandesData) demandesAImputerMap.values().toArray()[0];

        if (JadeStringUtil.isBlankOrZero(demandeCourante.getDateDebutTraitement())) {

            rfQdJointPerValJointDosJointTieJointDemMgr.setForAnneeQd(PRDateFormater
                    .convertDate_JJxMMxAAAA_to_AAAA(demandeCourante.getDateFacture()));

            rfQdJointPerValJointDosJointTieJointDemMgr.setForDateDebutBetweenPeriode(demandeCourante.getDateFacture());
        } else {

            if (demandeCourante.getDateDebutTraitement().length() == 7) {
                demandeCourante.setDateDebutTraitement("01." + demandeCourante.getDateDebutTraitement());
            }

            rfQdJointPerValJointDosJointTieJointDemMgr.setForAnneeQd(PRDateFormater
                    .convertDate_JJxMMxAAAA_to_AAAA(demandeCourante.getDateDebutTraitement()));

            rfQdJointPerValJointDosJointTieJointDemMgr.setForDateDebutBetweenPeriode(demandeCourante
                    .getDateDebutTraitement());
        }

        rfQdJointPerValJointDosJointTieJointDemMgr.changeManagerSize(0);
        rfQdJointPerValJointDosJointTieJointDemMgr.find(BSessionUtil.getSessionFromThreadContext()
                .getCurrentThreadTransaction());

        if (rfQdJointPerValJointDosJointTieJointDemMgr.size() == 0) {
            demandeCourante.setIdQdPrincipale("");
        } else if (rfQdJointPerValJointDosJointTieJointDemMgr.size() == 1) {
            demandeCourante
                    .setIdQdPrincipale(((RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande) rfQdJointPerValJointDosJointTieJointDemMgr
                            .getFirstEntity()).getIdQdPrincipale());
        } else {
            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, contextCourant.getIdAdaptationJournaliere(),
                    contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                    contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(),
                    "Plusieurs grandes Qds trouvées pour la même date", getLogsList());

            throw new Exception("RFProcessAdaptationAnuelleRegimeHandlerEntity.majIdsQdPrincipaleDemandeCourante(): ");
        }

    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {

        RFAdaptationJournaliereContext contextCourant = null;
        try {
            String anneeAdaptationStr = String.valueOf(JACalendar.today().getYear() + 1);

            contextCourant = RFUtils.initContextAdaptation(pcVOs, BSessionUtil.getSessionFromThreadContext()
                    .getUserId());

            FWMemoryLog memoryLog = new FWMemoryLog();

            String dateDonneeFinanciere = "01." + anneeAdaptationStr;

            // Diminution de la RFMAccordee existante
            String idParentAdaptation = diminuerPrestationAccordee(contextCourant);

            int nbChambresMedicaliseesInt = PegasusServiceLocator.getHomeService().countChambreMedicalisee(
                    pcVOs.getIdTiersBeneficiaire(), dateDonneeFinanciere, pcVOs.getIdVersionDroitApc());

            // Recherche si le bénéficiaire est en home médicalisé
            if (nbChambresMedicaliseesInt > 0) {

                // On contrôle si les conditions du bénéficiaire n'ont pas changées

                // Initilalisation de la map contenant les demandes à imputer
                Map<String, RFImputationDemandesData> demandesAimputerMap = new LinkedHashMap<String, RFImputationDemandesData>();
                // Initilalisation de la map contenant les Qds à imputer
                Map<String, RFImputationQdsData> qdsAimputerMap = new HashMap<String, RFImputationQdsData>();

                RFImputationDemandesData demandeCourante = null;

                demandeCourante = initDemandeACreer(pcVOs.getIdTiersBeneficiaire(), anneeAdaptationStr, contextCourant);

                demandesAimputerMap.put(demandeCourante.getIdDemande(), demandeCourante);

                // Création de la map contenant la décision
                RFGenererDecisionsEntitesService rfGenererDecisionsEntitesService = new RFGenererDecisionsEntitesService(
                        BSessionUtil.getSessionFromThreadContext().getUserId(), demandesAimputerMap, "",
                        BSessionUtil.getSessionFromThreadContext(), BSessionUtil.getSessionFromThreadContext()
                                .getCurrentThreadTransaction());

                Object[] genererDecisionsEntiteObj = rfGenererDecisionsEntitesService.genererDecisionsEntite();

                HashMap<String, Set<String[]>> idQdIdsDossierMap = (HashMap<String, Set<String[]>>) genererDecisionsEntiteObj[1];
                Set<RFDecisionData> decisions = (Set<RFDecisionData>) genererDecisionsEntiteObj[0];

                majIdsQdPrincipaleDemandeCourante(demandesAimputerMap, contextCourant);

                // Calcul
                RFImputationDemandesService rfImputationDemandesService = new RFImputationDemandesService(BSessionUtil
                        .getSessionFromThreadContext().getCurrentThreadTransaction(),
                        BSessionUtil.getSessionFromThreadContext(), demandesAimputerMap, decisions, qdsAimputerMap,
                        memoryLog, true);
                rfImputationDemandesService.imputerDemandes();

                // Si la demande est refusée
                if (JadeStringUtil.isBlankOrZero(((RFDecisionData) decisions.toArray()[0]).getMontantTotalAPayer())) {

                    // On log le refus
                    RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING,
                            contextCourant.getIdAdaptationJournaliere(), contextCourant.getIdTiersBeneficiaire(),
                            contextCourant.getNssTiersBeneficiaire(), contextCourant.getIdDecisionPc(),
                            contextCourant.getNumeroDecisionPc(), "Frqp pp refusée: "
                                    + getDescriptionMotifDeRefus((RFImputationDemandesData) demandesAimputerMap
                                            .values().toArray()[0]), getLogsList());

                }// Si la demande est acceptée ou partiellement acceptée
                else {

                    RFImputationDemandesData demCourante = (RFImputationDemandesData) demandesAimputerMap.values()
                            .toArray()[0];

                    // Si la demande calculé est complétement accepté, on impute sur les Qds et on créé la demande
                    if (demCourante.getMontantAPayerInitial().equals(demCourante.getMontantAccepte())) {

                        // Création de la demande en Bd dans l'état enregistré (permet de ne pas modifier
                        // RFPersistanceImputationDemandesDecisionsService)
                        creationDemandeNonCalculeEnBd(demandesAimputerMap, contextCourant);

                        // Imputation sur les Qds, création de la décision
                        RFPersistanceImputationDemandesDecisionsService rfPersistanceImputationDemandesDecisionsService = new RFPersistanceImputationDemandesDecisionsService(
                                BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction(),
                                BSessionUtil.getSessionFromThreadContext(), BSessionUtil.getSessionFromThreadContext()
                                        .getUserId(), demandesAimputerMap, qdsAimputerMap, decisions,
                                idQdIdsDossierMap, true);

                        String idDecision = rfPersistanceImputationDemandesDecisionsService.majImputation(true);

                        RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING,
                                contextCourant.getIdAdaptationJournaliere(), contextCourant.getIdTiersBeneficiaire(),
                                contextCourant.getNssTiersBeneficiaire(), contextCourant.getIdDecisionPc(),
                                contextCourant.getNumeroDecisionPc(), "Création de la décision N° " + idDecision,
                                getLogsList());

                        // Recherche de la grande Qd
                        RFImputationQdsData qdPrincipaleData = new RFImputationQdsData();
                        for (RFImputationQdsData qdCourante : qdsAimputerMap.values()) {
                            if (qdCourante.isQdPrincipale()) {
                                qdPrincipaleData = qdCourante;
                            }
                        }

                        // Création des RFAccordées
                        creationRfmAccordeesFrqpPP(qdPrincipaleData,
                                RFUtils.MONTANT_PRESTATION_ACCORDEE_FRQP_1_ER_PAIEMENT, "01." + anneeAdaptationStr,
                                "01." + anneeAdaptationStr, "01.01." + anneeAdaptationStr, "01.01."
                                        + anneeAdaptationStr, contextCourant.getIdTiersBeneficiaire(), idDecision,
                                idParentAdaptation, contextCourant);

                        creationRfmAccordeesFrqpPP(qdPrincipaleData,
                                RFUtils.MONTANT_PRESTATION_ACCORDEE_FRQP_2_EME_PAIEMENT, "02." + anneeAdaptationStr,
                                "03." + anneeAdaptationStr, "01.02." + anneeAdaptationStr, "01.03."
                                        + anneeAdaptationStr, contextCourant.getIdTiersBeneficiaire(), idDecision, "",
                                contextCourant);

                    }// Sinon on ne fait rien et on log l'acceptation partiel
                    else {

                        RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING,
                                contextCourant.getIdAdaptationJournaliere(), contextCourant.getIdTiersBeneficiaire(),
                                contextCourant.getNssTiersBeneficiaire(), contextCourant.getIdDecisionPc(),
                                contextCourant.getNumeroDecisionPc(),
                                "Frqp pp partiellement accepté (pas de prestation accordée modifiée ou créée): "
                                        + getDescriptionMotifDeRefus((RFImputationDemandesData) demandesAimputerMap
                                                .values().toArray()[0]), getLogsList());
                    }
                }

            } else {
                if (!JadeStringUtil.isBlankOrZero(idParentAdaptation)) {
                    RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING,
                            contextCourant.getIdAdaptationJournaliere(), contextCourant.getIdTiersBeneficiaire(),
                            contextCourant.getNssTiersBeneficiaire(), contextCourant.getIdDecisionPc(),
                            contextCourant.getNumeroDecisionPc(), "Chambre non médicalisée", getLogsList());
                }
            }
        }
        /* On log les Exception typées afin d'avoir une trace des cas en erreur dans la liste */
        catch (NullPointerException e) {
            if (contextCourant != null) {
                RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, contextCourant.getIdAdaptationJournaliere(),
                        contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                        contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(),
                        e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage(), getLogsList());
            }
            throw new JadePersistenceException(e.toString(), e);
        }
        /*
         * On ne log pas les Exception non typées car elle sont lancées manuellement par nous donc elles devraient déjà
         * être loggués
         */
        catch (Exception e) {
            throw new JadePersistenceException(e.toString(), e);
        }
    }

    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        Gson gson = new Gson();
        pcVOs = gson.fromJson(entity.getValue1(), DecisionPcVO.class);
    }

    public void setLogsList(List<String[]> logsList) {
        this.logsList = logsList;
    }

    @Override
    public void setProperties(Map<Enum<?>, String> map) {
        properties = map;
    }

}
