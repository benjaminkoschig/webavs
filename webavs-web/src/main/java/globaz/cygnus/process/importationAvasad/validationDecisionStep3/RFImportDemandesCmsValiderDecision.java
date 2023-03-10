package globaz.cygnus.process.importationAvasad.validationDecisionStep3;

import globaz.cygnus.application.RFApplication;
import globaz.cygnus.exceptions.RFBusinessException;
import globaz.cygnus.mappingXmlml.IRFImportationAvasadListeColumns;
import globaz.cygnus.mappingXmlml.RFXmlmlMappingLogImportationAvasad;
import globaz.cygnus.process.RFImportDemandesCmsData;
import globaz.cygnus.process.RFValiderDecisionAvasadProcess;
import globaz.cygnus.process.importationAvasad.RFAVASADException;
import globaz.cygnus.process.importationAvasad.RFProcessImportationAvasadEnum;
import globaz.cygnus.services.avasad.RFGenererRecapDemandesAvasadService;
import globaz.cygnus.services.avasad.RFPreparerDemandesAvasadService;
import globaz.cygnus.utils.RFExcelmlUtils;
import globaz.cygnus.utils.RFLogToDB;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.utils.RFXmlmlContainer;
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
import ch.globaz.common.util.errordriller.ErrorDriller;
import ch.globaz.common.util.errordriller.ErrorDriller.DrilledError;
import ch.globaz.jade.process.business.JadeProcessServiceLocator;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.exceptions.ProprieteException;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepAfterable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;

public class RFImportDemandesCmsValiderDecision implements JadeProcessStepInterface, JadeProcessStepAfterable,
        JadeProcessStepBeforable {

    private List<RFImportDemandesCmsData> entitesList = Collections
            .synchronizedList(new ArrayList<RFImportDemandesCmsData>());// modification DMA
    private Boolean errorInDocument = false;
    private Boolean haveAdresseCms = false;
    private FWMemoryLog memoryLog = new FWMemoryLog();
    private RFLogToDB avasadDebugLogger;

    @Override
    public void before(JadeProcessStep arg0, Map<Enum<?>, String> arg1) throws JadeApplicationException,
            JadePersistenceException {
        BSession sess = BSessionUtil.getSessionFromThreadContext();
        ErrorDriller ed = new ErrorDriller().add(sess).lookInThreadContext();
        List<DrilledError> errors = ed.drill();
        if (errors.size() > 0) {
            // oups, des m?chantes erreurs... on les log et on fait tout p?ter, pour ?viter des donn?es incoh?rentes
            // plus loin
            String message = MessageFormat.format(sess.getLabel("RF_IMPORT_AVASAD_PROCESS_ERRORS_DRILLED"),
                    errors.size());
            JadeLogger.error(this, message);
            for (DrilledError e : errors) {
                JadeLogger.error(this, e.toString());
            }
            throw new RFAVASADException("AVASAD step 3. " + message, null);
        }
        avasadDebugLogger = new RFLogToDB(BSessionUtil.getSessionFromThreadContext());
        avasadDebugLogger.logInfoToDB("starting AVASAD step", "AVASAD - step 3 - before");
    }

    @Override
    public void after(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {

        try {

            String numAf = getNumAf(entitesList, step.getIdExecutionProcess());

            // On s'assure que le CMS soit pr?sent avant le lancement du process de g?n?ration du document.
            findAdressCms(numAf);

            // Si aucune erreur, on lance la g?n?ration du document.
            if (!hasEntityError() && haveAdresseCms) {
                BISession session = PRSession.connectSession(BSessionUtil.getSessionFromThreadContext(),
                        RFApplication.DEFAULT_APPLICATION_CYGNUS);

                demarrerValiderDecisionProcess(step);

                genererRecapitulatifImportAvasad((BSession) session, getEmail(map), step);

                // Si une erreur est remont? dans la g?n?ration du document, on la remonte par la session.
                errorInDocument = session.hasErrors();
            }

            // Si aucune erreur lors de la gen?ration du document, on envois un mail de fin de traitement
            if (!errorInDocument) {
                List<String[]> logsList = buildLogFromMemoryLog();
                envoyerMail(map, generateDocumentLog(map, logsList), logsList);
            }
            if (avasadDebugLogger != null) {// are we sure 'before' always complete?...
                avasadDebugLogger.logInfoToDB("ending AVASAD step", "AVASAD - step 3 - after");
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

            RFUtils.ajouterLogImportationsAvasad(typeMessage, "", "", fwMsg.getMessageText(), false, logsList);
        }

        return logsList;
    }

    private void demarrerValiderDecisionProcess(JadeProcessStep step) throws Exception {

        RFValiderDecisionAvasadProcess rfValDecAvaPro = new RFValiderDecisionAvasadProcess();
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
                BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_AVASAD_PROCESS_NAME"),
                (!RFUtils.hasErrorsLogList(logsList) && !hasEntityError()) ? BSessionUtil.getSessionFromThreadContext()
                        .getLabel("RF_IMPORT_AVASAD_PROCESS_VALIDATION_DECISION_SUCCESS") : BSessionUtil
                        .getSessionFromThreadContext().getLabel("RF_IMPORT_AVASAD_PROCESS_VALIDATION_DECISION_FAILED"),
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

        RFXmlmlContainer container = RFXmlmlMappingLogImportationAvasad.loadResults(logsList, BSessionUtil
                .getSessionFromThreadContext().getUserId());

        String nomDoc = BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_AVASAD_NOM_LOG_ETAPE_TROIS");
        String docPath = RFExcelmlUtils.createDocumentExcel(BSessionUtil.getSessionFromThreadContext().getIdLangueISO()
                .toUpperCase()
                + "/" + IRFImportationAvasadListeColumns.MODEL_NAME, nomDoc, container);

        return docPath;
    }

    /**
     * Methode pour lancer la pr?paration des demandes et lancer la g?n?ration du document de r?capitulation AVASAD
     * 
     * @param session
     * @param emailAdress
     * @param step
     * @throws Exception
     */
    private void genererRecapitulatifImportAvasad(BSession session, String emailAdress, JadeProcessStep step)
            throws Exception {

        // Appel du service g?n?rant la structure de donn?e
        RFPreparerDemandesAvasadService rfPreparerDemandesAvasadService = new RFPreparerDemandesAvasadService();
        Map<String, ArrayList<RFImportDemandesCmsData>> regroupementDemandesParCodeTraitementMap = rfPreparerDemandesAvasadService
                .regroupementDemandesParCodeTraitement(session, entitesList);

        String numAf = getNumAf(entitesList, step.getIdExecutionProcess());

        // Appel du service g?n?rant les d?cisions RFM et le r?capitulatif AVASAD
        RFGenererRecapDemandesAvasadService rfGenererRecapDemandesAvasadService = new RFGenererRecapDemandesAvasadService();
        rfGenererRecapDemandesAvasadService.generateDocument(emailAdress, false, memoryLog,
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
     * Methode pour r?cup?rer l'idCms de la premi?re ligne de la liste
     * 
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws ProprieteException
     */
    private String getNumAf(List<RFImportDemandesCmsData> entitesList, String idExecutionProcess)
            throws JadePersistenceException {

        try {
            return JadeProcessServiceLocator.getPropertiesService().findAllProperties(idExecutionProcess)
                    .get(RFProcessImportationAvasadEnum.NUMERO_AF);
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
