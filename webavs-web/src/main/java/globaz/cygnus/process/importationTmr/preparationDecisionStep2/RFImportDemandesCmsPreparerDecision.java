package globaz.cygnus.process.importationTmr.preparationDecisionStep2;

import ch.globaz.common.util.errordriller.ErrorDriller;
import ch.globaz.common.util.errordriller.ErrorDriller.DrilledError;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepAfterable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.jade.process.utils.JadeProcessCommonUtils;
import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.db.demandes.RFTypesDemandeJointDossierJointTiers;
import globaz.cygnus.db.demandes.RFTypesDemandeJointDossierJointTiersManager;
import globaz.cygnus.exceptions.RFImportationTmrException;
import globaz.cygnus.mappingXmlml.IRFImportationTmrListeColumns;
import globaz.cygnus.mappingXmlml.RFXmlmlMappingLogImportationTmr;
import globaz.cygnus.process.importationTmr.RFTmrException;
import globaz.cygnus.services.RFSetEtatProcessService;
import globaz.cygnus.services.preparerDecision.RFAnnulerPreparationDecisionService;
import globaz.cygnus.services.preparerDecision.RFPreparerDecisionsService;
import globaz.cygnus.utils.RFExcelmlUtils;
import globaz.cygnus.utils.RFLogToDB;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.utils.RFXmlmlContainer;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.smtp.JadeSmtpClient;

import java.text.MessageFormat;
import java.util.*;

public class RFImportDemandesCmsPreparerDecision implements JadeProcessStepInterface, JadeProcessStepBeforable,
        JadeProcessStepAfterable {

    private FWMemoryLog memoryLog = new FWMemoryLog();
    private RFLogToDB tmrDebugLogger;

    @Override
    public void after(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        try {
            envoyerMail(map, generateDocumentLog(map, buildLogFromMemoryLog(map)));
            if (tmrDebugLogger != null) {// are we sure 'before' always complete?...
                tmrDebugLogger.logInfoToDB("ending Tmr step", "Tmr - step 2 - after");
            }
        } catch (Exception e) {
            JadeProcessCommonUtils.addError(e);
        }
    }

    @Override
    public void before(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        BSession sess = BSessionUtil.getSessionFromThreadContext();
        ErrorDriller ed = new ErrorDriller().add(sess).lookInThreadContext();
        List<DrilledError> errors = ed.drill();
        if (errors.size() > 0) {
            // oups, des méchantes erreurs... on les log et on fait tout péter, pour éviter des données incohérentes
            // plus loin
            String message = MessageFormat.format(sess.getLabel("RF_IMPORT_TMR_PROCESS_ERRORS_DRILLED"),
                    errors.size());
            JadeLogger.error(this, message);
            for (DrilledError e : errors) {
                JadeLogger.error(this, e.toString());
            }
            throw new RFTmrException("TMR Tmr 2. " + message, null);
        }

        BITransaction transactionPreparerDecProcess = null;
        try {

            tmrDebugLogger = new RFLogToDB(BSessionUtil.getSessionFromThreadContext());
            tmrDebugLogger.logInfoToDB("starting Tmr step", "TMR - step 2 - before");
            transactionPreparerDecProcess = BSessionUtil.getSessionFromThreadContext().newTransaction();
            transactionPreparerDecProcess.openTransaction();

            // Annulation du calcul
            RFAnnulerPreparationDecisionService.annulerPreparationDecision(BSessionUtil.getSessionFromThreadContext()
                    .getUserId(), step.getIdExecutionProcess(), null, BSessionUtil.getSessionFromThreadContext(),
                    (BTransaction) transactionPreparerDecProcess);

            RFPreparerDecisionsService rfPreDecSer = new RFPreparerDecisionsService(null, JACalendar.todayJJsMMsAAAA(),
                    retrieveDemandesATraiter(step.getIdExecutionProcess()), BSessionUtil.getSessionFromThreadContext()
                            .getUserId(), step.getIdExecutionProcess(), memoryLog,
                    BSessionUtil.getSessionFromThreadContext(), transactionPreparerDecProcess);

            rfPreDecSer.preparerDecisions();

        } catch (Exception e) {
            JadeThread.logError("RFImportDemandesCmsPreparerDecision.before()", e.toString());
            memoryLog.logMessage(e.getMessage(), FWMessage.ERREUR,
                    BSessionUtil.getSessionFromThreadContext().getLabel("PROCESS_PREPARER_DECISIONS"));

            if (transactionPreparerDecProcess != null) {
                transactionPreparerDecProcess.setRollbackOnly();
            }

            throw new RFImportationTmrException(e.getMessage());

        } finally {
            if (transactionPreparerDecProcess != null) {
                try {
                    if (transactionPreparerDecProcess.hasErrors() || transactionPreparerDecProcess.isRollbackOnly()) {
                        transactionPreparerDecProcess.rollback();
                    } else {
                        transactionPreparerDecProcess.commit();
                    }
                } catch (Exception e) {
                    throw new RFImportationTmrException(e.getMessage());
                } finally {
                    terminerProcessPreparerDecision(memoryLog, transactionPreparerDecProcess);
                }
            }
        }
    }

    private List<String[]> buildLogFromMemoryLog(Map<Enum<?>, String> map) {

        List<String[]> logsList = new ArrayList<String[]>();

        for (Object msg : memoryLog.getMessagesToVector()) {
            FWMessage fwMsg = (FWMessage) msg;

            int typeMessage;
            if (fwMsg.getCsTypeMessage().equals(FWMessage.ERREUR)) {
                typeMessage = JadeBusinessMessageLevels.ERROR;
            } else {
                typeMessage = JadeBusinessMessageLevels.WARN;
            }

            RFUtils.ajouterLogImportationsTmr(typeMessage, "", "", fwMsg.getMessageText(),  logsList);
        }

        return logsList;
    }

    private void envoyerMail(Map<Enum<?>, String> map, String docPath) throws Exception {
        JadeSmtpClient.getInstance()
                .sendMail(
                        getEmail(map),
                        BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_TMR_PROCESS_NAME"),
                        !JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR) ? BSessionUtil
                                .getSessionFromThreadContext().getLabel(
                                        "RF_IMPORT_TMR_PROCESS_PREPARATION_DECISION_SUCCESS") : BSessionUtil
                                .getSessionFromThreadContext().getLabel(
                                        "RF_IMPORT_TMR_PROCESS_PREPARATION_DECISION_FAILED"),
                        new String[] { docPath });
    }

    private String generateDocumentLog(Map<Enum<?>, String> map, List<String[]> logsList) throws Exception {

        RFXmlmlContainer container = RFXmlmlMappingLogImportationTmr.loadResults(logsList, BSessionUtil
                .getSessionFromThreadContext().getUserId());

        String nomDoc = BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_TMR_NOM_LOG_ETAPE_DEUX");
        String docPath = RFExcelmlUtils.createDocumentExcel(BSessionUtil.getSessionFromThreadContext().getIdLangueISO()
                .toUpperCase()
                + "/" + IRFImportationTmrListeColumns.MODEL_NAME, nomDoc, container);

        return docPath;
    }

    private String getEmail(Map<Enum<?>, String> map) {
        return /* JadeStringUtil.isBlankOrZero(map.get(RFProcessImportationTmrEnum.EMAIL)) ? */BSessionUtil
                .getSessionFromThreadContext().getUserEMail() /* : map.get(RFProcessImportationTmrEnum.EMAIL) */;
    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new RFImportDemandesCmsPreparerDecisionHandler();
    }

    private List<RFTypesDemandeJointDossierJointTiers> retrieveDemandesATraiter(String idExecutionProcess)
            throws Exception {

        RFTypesDemandeJointDossierJointTiersManager rfTypDemJointDosJointTieMgr = new RFTypesDemandeJointDossierJointTiersManager();
        rfTypDemJointDosJointTieMgr.setSession(BSessionUtil.getSessionFromThreadContext());
        rfTypDemJointDosJointTieMgr.setForCsEtatDemande(IRFDemande.ENREGISTRE);
        rfTypDemJointDosJointTieMgr.setForIdExecutionProcess(idExecutionProcess);

        Set<String> csSousTypesToIgnore = new HashSet<String>();
        csSousTypesToIgnore.add(IRFTypesDeSoins.st_19_DEVIS_DENTAIRE);
        rfTypDemJointDosJointTieMgr.setCssSousTypeDeSoinToIgnore(csSousTypesToIgnore);
        rfTypDemJointDosJointTieMgr.changeManagerSize(0);
        rfTypDemJointDosJointTieMgr.find(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction());

        Iterator<RFTypesDemandeJointDossierJointTiers> rfTypDemJointDosJointTieItr = rfTypDemJointDosJointTieMgr
                .iterator();

        List<RFTypesDemandeJointDossierJointTiers> rfTypDemJointDosJointTieList = new LinkedList<RFTypesDemandeJointDossierJointTiers>();

        while (rfTypDemJointDosJointTieItr.hasNext()) {

            RFTypesDemandeJointDossierJointTiers rfTypDemJoiDosJoiTie = rfTypDemJointDosJointTieItr.next();

            if (rfTypDemJoiDosJoiTie != null) {
                rfTypDemJointDosJointTieList.add(rfTypDemJoiDosJoiTie);
            } else {
                throw new Exception("Demande null [RFPreparerDecisionsProcess.retrieveDemandesATraiter()]");
            }
        }

        return rfTypDemJointDosJointTieList;
    }

    private void terminerProcessPreparerDecision(FWMemoryLog memoryLog, BITransaction transactionPreparerDecProcess) {
        try {
            if (transactionPreparerDecProcess != null) {
                transactionPreparerDecProcess.closeTransaction();
            }
            JadeThread.logClear();
            RFSetEtatProcessService.setEtatProcessPreparerDecision(false, BSessionUtil.getSessionFromThreadContext());
        } catch (Exception e) {
            memoryLog.logMessage(e.getMessage(), FWMessage.ERREUR,
                    BSessionUtil.getSessionFromThreadContext().getLabel("PROCESS_PREPARER_DECISIONS"));
        }
    }
}
