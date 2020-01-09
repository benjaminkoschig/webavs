package globaz.cygnus.process.importationSecutel.validationDecisionStep3;

import ch.globaz.common.util.errordriller.ErrorDriller;
import ch.globaz.common.util.errordriller.ErrorDriller.DrilledError;
import ch.globaz.jade.process.business.JadeProcessServiceLocator;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.exceptions.ProprieteException;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepAfterable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import globaz.cygnus.application.RFApplication;
import globaz.cygnus.exceptions.RFBusinessException;
import globaz.cygnus.mappingXmlml.IRFImportationSecutelListeColumns;
import globaz.cygnus.mappingXmlml.RFXmlmlMappingLogImportationSecutel;
import globaz.cygnus.process.RFImportDemandesCmsData;
import globaz.cygnus.process.RFValiderDecisionSecutelProcess;
import globaz.cygnus.process.importationSecutel.RFProcessImportationSecutelEnum;
import globaz.cygnus.process.importationSecutel.RFSecutelException;
import globaz.cygnus.services.secutel.RFGenererRecapDemandesSecutelService;
import globaz.cygnus.services.secutel.RFPreparerDemandesSecutelService;
import globaz.cygnus.utils.*;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRSession;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RFImportDemandesCmsValiderDecision implements JadeProcessStepInterface, JadeProcessStepAfterable,
        JadeProcessStepBeforable {

    private List<RFImportDemandesCmsData> entitesList = Collections
            .synchronizedList(new ArrayList<RFImportDemandesCmsData>());// modification DMA
    private Boolean errorInDocument = false;
    private Boolean haveAdresseCms = false;
    private FWMemoryLog memoryLog = new FWMemoryLog();
    private RFLogToDB secutelDebugLogger;

    @Override
    public void before(JadeProcessStep arg0, Map<Enum<?>, String> arg1) throws JadeApplicationException,
            JadePersistenceException {
        BSession sess = BSessionUtil.getSessionFromThreadContext();
        ErrorDriller ed = new ErrorDriller().add(sess).lookInThreadContext();
        List<DrilledError> errors = ed.drill();
        if (errors.size() > 0) {
            // oups, des méchantes erreurs... on les log et on fait tout péter, pour éviter des données incohérentes
            // plus loin
            String message = MessageFormat.format(sess.getLabel("RF_IMPORT_SECUTEL_PROCESS_ERRORS_DRILLED"),
                    errors.size());
            JadeLogger.error(this, message);
            for (DrilledError e : errors) {
                JadeLogger.error(this, e.toString());
            }
            throw new RFSecutelException("Secutel step 3. " + message, null);
        }
        secutelDebugLogger = new RFLogToDB(BSessionUtil.getSessionFromThreadContext());
        secutelDebugLogger.logInfoToDB("starting Secutel step", "Secutel - step 3 - before");
    }

    @Override
    public void after(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {

        try {

            String numAf = getNumAf(entitesList, step.getIdExecutionProcess());

            // On s'assure que le CMS soit présent avant le lancement du process de génération du document.
            findAdressCms(numAf);

            // Si aucune erreur, on lance la génération du document.
            if (!hasEntityError() && haveAdresseCms) {
                BISession session = PRSession.connectSession(BSessionUtil.getSessionFromThreadContext(),
                        RFApplication.DEFAULT_APPLICATION_CYGNUS);

                demarrerValiderDecisionProcess(step);

                genererRecapitulatifImportSecutel((BSession) session, getEmail(map), step);

                // Si une erreur est remonté dans la génération du document, on la remonte par la session.
                errorInDocument = session.hasErrors();
            }

            // Si aucune erreur lors de la genération du document, on envois un mail de fin de traitement
            if (!errorInDocument) {
                List<String[]> logsList = buildLogFromMemoryLog();
                envoyerMail(map, generateDocumentLog(map, logsList), logsList);
            }
            if (secutelDebugLogger != null) {// are we sure 'before' always complete?...
                secutelDebugLogger.logInfoToDB("ending Secutel step", "Secutel - step 3 - after");
            }
        } catch (Exception e) {
            JadeThread.logError("RFImportDemandesCmsValiderDecision.after()", e.toString());
            memoryLog.logMessage(e.getMessage(), FWMessage.ERREUR, "RFImportDemandesCmsValiderDecision.after()");
        }
    }

    private List<String[]> buildLogFromMemoryLog() {

        List<String[]> logsList = new ArrayList<String[]>();

        for (Object msg : memoryLog.getMessagesToVector()) {
            FWMessage fwMsg = (FWMessage) msg;

            int typeMessage;
            if ((fwMsg.getCsTypeMessage().equals(FWMessage.ERREUR))
                    || (fwMsg.getTypeMessage().equals(FWMessage.ERREUR))) {
                typeMessage = JadeBusinessMessageLevels.ERROR;
            } else {
                typeMessage = JadeBusinessMessageLevels.WARN;
            }

            RFUtils.ajouterLogImportationsSecutel(typeMessage, "", "", fwMsg.getMessageText(), logsList);
        }

        return logsList;
    }

    private void demarrerValiderDecisionProcess(JadeProcessStep step) throws Exception {

        RFValiderDecisionSecutelProcess rfValDecAvaPro = new RFValiderDecisionSecutelProcess();
        rfValDecAvaPro.setSession(BSessionUtil.getSessionFromThreadContext());
        rfValDecAvaPro.setEmailAdress(BSessionUtil.getSessionFromThreadContext().getUserEMail());
        rfValDecAvaPro.setIdExecutionProcess(step.getIdExecutionProcess());
        rfValDecAvaPro.setIdGestionnaire(BSessionUtil.getSessionFromThreadContext().getUserId());
        rfValDecAvaPro.setMemoryLog(memoryLog);
        rfValDecAvaPro.run();
        FWMemoryLog memLog = rfValDecAvaPro.getMemoryLog();
        if (memLog.hasErrors()) {
            throw new RFBusinessException(memLog.getMessagesInString());
        }
    }

    private void envoyerMail(Map<Enum<?>, String> map, String docPath, List<String[]> logsList) throws Exception {

        JadeSmtpClient.getInstance().sendMail(
                getEmail(map),
                BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_SECUTEL_PROCESS_NAME"),
                (!RFUtils.hasErrorsLogList(logsList) && !hasEntityError()) ? BSessionUtil.getSessionFromThreadContext()
                        .getLabel("RF_IMPORT_SECUTEL_PROCESS_VALIDATION_DECISION_SUCCESS") : BSessionUtil
                        .getSessionFromThreadContext().getLabel("RF_IMPORT_SECUTEL_PROCESS_VALIDATION_DECISION_FAILED"),
                new String[] { docPath });
    }

    private void findAdressCms(String numCms) throws Exception {
        try {

            String csGenreAdministration = RFPropertiesUtils.getCsGenreAdminstrationCMS();

            if (JadeStringUtil.isBlankOrZero(csGenreAdministration)) {
                memoryLog.logMessage("Can't find properties 'cygnus.cs.genre.administration.cms' in database",
                        FWMessage.ERREUR, "RFImportDemandesCmsValiderDecision.findAdressCms(String numCms)");
            }

            PRTiersWrapper[] tiersW = PRTiersHelper.getAdministrationActiveForGenreAndCode(
                    BSessionUtil.getSessionFromThreadContext(), csGenreAdministration, numCms);

            if (tiersW == null) {

                JadeThread.logError("RFImportDemandesCmsValiderDecision.checkAdressCms()",
                        "Can't find adress CMS with this number " + numCms);

                memoryLog.logMessage("Can't find adress CMS with this number", FWMessage.ERREUR,
                        "RFImportDemandesCmsValiderDecision.findAdressCms(String numCms)");

                haveAdresseCms = false;

            } else {
                haveAdresseCms = true;
            }

        } catch (Exception e) {
            throw new Exception(e + "RFImportDemandesCmsValiderDecision.findAdressCms(String numCms)");
        }

    }

    private String generateDocumentLog(Map<Enum<?>, String> map, List<String[]> logsList) throws Exception {

        RFXmlmlContainer container = RFXmlmlMappingLogImportationSecutel.loadResults(logsList, BSessionUtil
                .getSessionFromThreadContext().getUserId());

        String nomDoc = BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_SECUTEL_NOM_LOG_ETAPE_TROIS");
        String docPath = RFExcelmlUtils.createDocumentExcel(BSessionUtil.getSessionFromThreadContext().getIdLangueISO()
                .toUpperCase()
                + "/" + IRFImportationSecutelListeColumns.MODEL_NAME, nomDoc, container);

        return docPath;
    }

    /**
     * Methode pour lancer la préparation des demandes et lancer la génération du document de récapitulation Secutel
     * 
     * @param session
     * @param emailAdress
     * @param step
     * @throws Exception
     */
    private void genererRecapitulatifImportSecutel(BSession session, String emailAdress, JadeProcessStep step)
            throws Exception {

        // Appel du service générant la structure de donnée
        RFPreparerDemandesSecutelService rfPreparerDemandesSecutelService = new RFPreparerDemandesSecutelService();
        Map<String, ArrayList<RFImportDemandesCmsData>> regroupementDemandesParCodeTraitementMap = rfPreparerDemandesSecutelService
                .regroupementDemandesParCodeTraitement(session, entitesList);

        String numAf = getNumAf(entitesList, step.getIdExecutionProcess());

        // Appel du service générant les décisions RFM et le récapitulatif Secutel
        RFGenererRecapDemandesSecutelService rfGenererRecapDemandesSecutelService = new RFGenererRecapDemandesSecutelService();
        rfGenererRecapDemandesSecutelService.generateDocument(emailAdress, false, memoryLog,
                regroupementDemandesParCodeTraitementMap, session, step, numAf);
    }

    private String getEmail(Map<Enum<?>, String> map) {
        return BSessionUtil.getSessionFromThreadContext().getUserEMail();

    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new RFImportDemandesCmsValiderDecisionHandler(entitesList);
    }

    /**
     * Methode pour récupérer l'idCms de la première ligne de la liste
     * 
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws ProprieteException
     */
    private String getNumAf(List<RFImportDemandesCmsData> entitesList, String idExecutionProcess)
            throws JadePersistenceException {

        try {
            return JadeProcessServiceLocator.getPropertiesService().findAllProperties(idExecutionProcess)
                    .get(RFProcessImportationSecutelEnum.NUMERO_AF);
        } catch (Exception e) {
            throw new JadePersistenceException(e);
        }
    }

    private boolean hasEntityError() {
        boolean hasEntityError = false;
        for (RFImportDemandesCmsData entiteCourante : entitesList) {
            if (entiteCourante.hasErrorsEtape3()) {
                hasEntityError = true;
                break;
            }
        }
        return hasEntityError;
    }
}
