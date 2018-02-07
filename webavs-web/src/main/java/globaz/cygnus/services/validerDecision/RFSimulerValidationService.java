package globaz.cygnus.services.validerDecision;

import globaz.corvus.api.lots.IRELot;
import globaz.cygnus.application.RFApplication;
import globaz.cygnus.db.paiement.RFSimulerValidationDecision;
import globaz.cygnus.db.paiement.RFSimulerValidationDecisionManager;
import globaz.cygnus.exceptions.RFXmlmlException;
import globaz.cygnus.mappingXmlml.IRFListeControleValiderDecisionListeColumns;
import globaz.cygnus.mappingXmlml.RFXmlmlMappingLogListeControleValiderDecision;
import globaz.cygnus.utils.RFExcelmlUtils;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFXmlmlContainer;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.pegasus.business.constantes.IPCDroits;

/**
 * 
 * @author jje
 */
public class RFSimulerValidationService {

    private static String dateEcheanceDernierPaiement = "";
    private static JadePublishDocumentInfo docInfo = null;
    private static String docPath = "";
    private static String eMailAdresse = "";
    private static String idGestionnaire = "";
    private static Boolean isPourValidation = Boolean.FALSE;
    private static Boolean isSimulation = Boolean.FALSE;
    private static FWMemoryLog memoryLog = null;
    private static BISession session = null;
    private static RFSimulerValidationDecisionManager simulationManager = null;
    private static BITransaction transaction = null;

    private static void createDocument(List<RFSimulerValidationDecision> validationDecisionList, String idLot,
            String idTiersFondationSas) throws RFXmlmlException, Exception {

        RFXmlmlContainer container = null;
        BSession session = (BSession) RFSimulerValidationService.getSession();
        boolean wantCommunePolitique = CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue();

        // chargement de l'ensemble du RFXmlmlcontainer
        container = RFXmlmlMappingLogListeControleValiderDecision.loadResults(validationDecisionList, session,
                RFSimulerValidationService.isSimulation, idLot, RFSimulerValidationService.dateEcheanceDernierPaiement,
                idTiersFondationSas, wantCommunePolitique);

        // Définition du nom du document
        String nomDoc = "";
        if (RFSimulerValidationService.isSimulation) {
            nomDoc = session.getLabel("PROCESS_SIMULATION_DECOMPTE_VALIDER_DECISION");
        } else {
            nomDoc = session.getLabel("PROCESS_DECOMPTE_VALIDER_DECISION");
        }

        // sélection du modèle à utiliser (avec ou sans les communes politiques)
        String modelName;
        if (wantCommunePolitique) {
            modelName = session.getIdLangueISO().toUpperCase() + "/"
                    + IRFListeControleValiderDecisionListeColumns.MODEL_NAME_COMMUNE_POLITIQUE;
        } else {
            modelName = session.getIdLangueISO().toUpperCase() + "/"
                    + IRFListeControleValiderDecisionListeColumns.MODEL_NAME;
        }

        // création du document
        RFSimulerValidationService.docPath = RFExcelmlUtils.createDocumentExcel(modelName, nomDoc, container);

        // Publication du document
        RFSimulerValidationService.setDocInfo(nomDoc);
    }

    public static String getDateEcheanceDernierPaiement() {
        return RFSimulerValidationService.dateEcheanceDernierPaiement;
    }

    public static JadePublishDocumentInfo getDocInfo() {
        return RFSimulerValidationService.docInfo;
    }

    public static String getDocPath() {
        return RFSimulerValidationService.docPath;
    }

    public static String geteMailAdresse() {
        return RFSimulerValidationService.eMailAdresse;
    }

    public static String getIdGestionnaire() {
        return RFSimulerValidationService.idGestionnaire;
    }

    public static Boolean getIsPourValidation() {
        return RFSimulerValidationService.isPourValidation;
    }

    public static Boolean getIsSimulation() {
        return RFSimulerValidationService.isSimulation;
    }

    public static FWMemoryLog getMemoryLog() {
        return RFSimulerValidationService.memoryLog;
    }

    public static BISession getSession() {
        return RFSimulerValidationService.session;
    }

    public static BITransaction getTransaction() {
        return RFSimulerValidationService.transaction;
    }

    private static void initSimulationManager(String idGestionnaire, String idLot) throws Exception {

        RFSimulerValidationService.simulationManager = new RFSimulerValidationDecisionManager();
        RFSimulerValidationService.simulationManager.setSession((BSession) RFSimulerValidationService.getSession());

        HashSet<String> setForCsEtatLot = new HashSet<String>();
        setForCsEtatLot.add(IRELot.CS_ETAT_LOT_OUVERT);
        setForCsEtatLot.add(IRELot.CS_ETAT_LOT_ERREUR);

        RFSimulerValidationService.simulationManager.setForCsEtatsLot(setForCsEtatLot);
        RFSimulerValidationService.simulationManager.setForCsLotOwner(IRELot.CS_LOT_OWNER_RFM);
        RFSimulerValidationService.simulationManager.setForCsTypeRelation(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);

        if (JadeStringUtil.isBlankOrZero(idLot)) {
            if (RFPropertiesUtils.utiliserGestionnaireViewBean()) {
                RFSimulerValidationService.simulationManager.setForIdGestionnaireDecision(idGestionnaire);
            }
        } else {
            RFSimulerValidationService.simulationManager.setForIdLot(idLot);
        }

        RFSimulerValidationService.simulationManager.changeManagerSize(0);
        RFSimulerValidationService.simulationManager.find();

    }

    public static JadePublishDocumentInfo lancerSimulationValidation(String eMailAdresse, String idGestionnaire,
            Boolean isSimulation, JadePublishDocumentInfo docInfo, String dateEcheancePaiement, BISession session,
            BITransaction transaction, Boolean isPourValidation, String idTiersFondationSas, String idLot)
            throws Exception {

        RFSimulerValidationService.seteMailAdresse(eMailAdresse);
        RFSimulerValidationService.setIdGestionnaire(idGestionnaire);
        RFSimulerValidationService.setIsSimulation(isSimulation);
        RFSimulerValidationService.setSession(session);
        RFSimulerValidationService.setTransaction(transaction);
        RFSimulerValidationService.setDocInfoCreation(docInfo);
        RFSimulerValidationService.setDateEcheanceDernierPaiement(dateEcheancePaiement);
        RFSimulerValidationService.setIsPourValidation(isPourValidation);
        RFSimulerValidationService.docPath = "";
        RFSimulerValidationService.simulationManager = null;
        RFSimulerValidationService.setMemoryLog(new FWMemoryLog());
        RFSimulerValidationService.getMemoryLog().setSession((BSession) RFSimulerValidationService.getSession());

        // Traitement des nouvelles adaptations
        if (JadeStringUtil.isBlankOrZero(RFSimulerValidationService.getIdGestionnaire())) {
            RFSimulerValidationService.setIdGestionnaire(RFSimulerValidationService.getSession().getUserId());
        }

        // appel du manager pour avoir la liste des ovs à valider : décisions non validées
        RFSimulerValidationService.initSimulationManager(idGestionnaire, idLot);

        if (RFSimulerValidationService.simulationManager.size() > 0) {
            String idLotFirstEntity = RFSimulerValidationService.simulationManager.getSize() > 0 ? ((RFSimulerValidationDecision) RFSimulerValidationService.simulationManager
                    .getFirstEntity()).getIdLot() : "";

            List<RFSimulerValidationDecision> validationDecisionList = RFSimulerValidationService.simulationManager
                    .getContainerAsList();
            if (RFPropertiesUtils.miseEnGedDesDecisionsAZero()) {
                validationDecisionList = filtreNotNull(validationDecisionList);
            }

            RFSimulerValidationService.createDocument(validationDecisionList, idLotFirstEntity, idTiersFondationSas);
        } else {
            RFSimulerValidationService.memoryLog.logMessage(((BSession) RFSimulerValidationService.getSession())
                    .getLabel("PROCESS_SIMULATION_VALIDER_DECISION_PAS_DE_PRESTATIONS"), FWMessage.ERREUR,
                    "RFSimulerValidationService.lancerSimulationValidation()");
        }

        return RFSimulerValidationService.docInfo;

    }

    private static List<RFSimulerValidationDecision> filtreNotNull(List<RFSimulerValidationDecision> decisionArray) {
        List<RFSimulerValidationDecision> tmpArray = new ArrayList<RFSimulerValidationDecision>();
        for (RFSimulerValidationDecision decision : decisionArray) {
            if (!Montant.valueOf(decision.getMontantPrestation()).isZero()) {
                tmpArray.add(decision);
            }
        }
        return tmpArray;
    }

    public static void setDateEcheanceDernierPaiement(String dateEcheanceDernierPaiement) {
        RFSimulerValidationService.dateEcheanceDernierPaiement = dateEcheanceDernierPaiement;
    }

    private static void setDocInfo(String nomDoc) {
        RFSimulerValidationService.docInfo.setApplicationDomain(RFApplication.DEFAULT_APPLICATION_CYGNUS);
        RFSimulerValidationService.docInfo.setPublishDocument(false);
        RFSimulerValidationService.docInfo.setArchiveDocument(false);
        RFSimulerValidationService.docInfo.setOwnerEmail(RFSimulerValidationService.geteMailAdresse());
        RFSimulerValidationService.docInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO,
                RFSimulerValidationService.geteMailAdresse());
        RFSimulerValidationService.docInfo.setOwnerEmail(RFSimulerValidationService.geteMailAdresse());
        RFSimulerValidationService.docInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO,
                RFSimulerValidationService.geteMailAdresse());
        RFSimulerValidationService.docInfo.setArchiveDocument(false);
        RFSimulerValidationService.docInfo.setPublishDocument(true);
    }

    public static void setDocInfoCreation(JadePublishDocumentInfo docInfo) {
        RFSimulerValidationService.docInfo = docInfo;
    }

    private static void seteMailAdresse(String eMailAdresse) {
        RFSimulerValidationService.eMailAdresse = eMailAdresse;
    }

    private static void setIdGestionnaire(String idGestionnaire) {
        RFSimulerValidationService.idGestionnaire = idGestionnaire;
    }

    public static void setIsPourValidation(Boolean isPourValidation) {
        RFSimulerValidationService.isPourValidation = isPourValidation;
    }

    private static void setIsSimulation(Boolean isSimulation) {
        RFSimulerValidationService.isSimulation = isSimulation;
    }

    public static void setMemoryLog(FWMemoryLog memoryLog) {
        RFSimulerValidationService.memoryLog = memoryLog;
    }

    private static void setSession(BISession session) {
        RFSimulerValidationService.session = session;
    }

    private static void setTransaction(BITransaction transaction) {
        RFSimulerValidationService.transaction = transaction;
    }

}
