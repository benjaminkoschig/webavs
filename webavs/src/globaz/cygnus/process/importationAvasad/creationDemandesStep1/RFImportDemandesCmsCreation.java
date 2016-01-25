package globaz.cygnus.process.importationAvasad.creationDemandesStep1;

import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.demandes.RFDemandeMai13;
import globaz.cygnus.db.demandes.RFDemandeManager;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemandeManager;
import globaz.cygnus.exceptions.RFXmlmlException;
import globaz.cygnus.mappingXmlml.IRFImportationAvasadListeColumns;
import globaz.cygnus.mappingXmlml.RFXmlmlMappingLogImportationAvasad;
import globaz.cygnus.process.importationAvasad.RFAVASADException;
import globaz.cygnus.process.importationAvasad.RFProcessImportationAvasadEnum;
import globaz.cygnus.utils.RFExcelmlUtils;
import globaz.cygnus.utils.RFLogToDB;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.utils.RFXmlmlContainer;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.common.util.errordriller.ErrorDriller;
import ch.globaz.common.util.errordriller.ErrorDriller.DrilledError;
import ch.globaz.jade.process.business.JadeProcessServiceLocator;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepAfterable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInfoCurrentStep;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInit;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.jade.process.businessimpl.models.JadeProcessExecut;
import ch.globaz.jade.process.utils.JadeProcessCommonUtils;

/**
 * 
 * @author jje
 * @author vch
 * 
 */
public class RFImportDemandesCmsCreation implements JadeProcessStepInterface, JadeProcessStepBeforable,
        JadeProcessStepAfterable, JadeProcessStepInfoCurrentStep, JadeProcessStepInit {

    private class ComparateurLogs implements Comparator<String[]> {
        @Override
        public int compare(String[] log1, String[] log2) {
            // tri desc
            if (log1[1].compareTo(log2[1]) > 0) {
                return 1;
            } else if (log1[1].compareTo(log2[1]) < 0) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    // public static final String CS_GENRE_ADMINISTRATION_CMS = "509008";// "509031" "509017" ;
    private String idExecutionProcess = "";
    private List<String> idsDemandeAReexecuter = new ArrayList<String>();
    private String idTiersAf = "";

    private RFLogToDB avasadDebugLogger;

    private List<String[]> logsList = new ArrayList<String[]>();

    @Override
    public void after(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        try {
            envoyerMail(map, generateDocumentLog(map));
            if (avasadDebugLogger != null) {// are we sure 'before' always complete?...
                avasadDebugLogger.logInfoToDB("ending AVASAD step", "AVASAD - step 1 - after");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            String message = MessageFormat.format(sess.getLabel("RF_IMPORT_AVASAD_PROCESS_ERRORS_DRILLED"),
                    errors.size());
            JadeLogger.error(this, message);
            for (DrilledError e : errors) {
                JadeLogger.error(this, e.toString());
            }
            throw new RFAVASADException("AVASAD step 1. " + message, null);
        }
        avasadDebugLogger = new RFLogToDB(BSessionUtil.getSessionFromThreadContext());
        avasadDebugLogger.logInfoToDB("starting AVASAD step", "AVASAD - step 1 - before");
        idTiersAf = getAdministrationAfParCode(JadeProcessServiceLocator.getPropertiesService()
                .findAllProperties(step.getIdExecutionProcess()).get(RFProcessImportationAvasadEnum.NUMERO_AF));
        avasadDebugLogger.logInfoToDB(
                String.format(
                        "Adresse de paiement pour importation AVASAD %s : %s",
                        JadeProcessServiceLocator.getPropertiesService()
                                .findAllProperties(step.getIdExecutionProcess())
                                .get(RFProcessImportationAvasadEnum.PATH_FILE_USER), idTiersAf),
                "AVASAD - step 1 - before");
        JadeLogger.info(this, String.format(
                "Adresse de paiement pour importation du fichier AVASAD %s : %s",
                JadeProcessServiceLocator.getPropertiesService().findAllProperties(step.getIdExecutionProcess())
                        .get(RFProcessImportationAvasadEnum.FILE_PATH_FOR_POPULATION), idTiersAf));
        idExecutionProcess = step.getIdExecutionProcess();
        supprimerDemandes(step.getIdExecutionProcess());
    }

    private void envoyerMail(Map<Enum<?>, String> map, String docPath) throws Exception {
        JadeSmtpClient.getInstance().sendMail(
                getEmail(map),
                BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_AVASAD_PROCESS_NAME"),
                /*
                 * !JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR) !RFUtils
                 * .hasErrorsLogList(this.logsList) ?
                 */
                BSessionUtil.getSessionFromThreadContext()
                        .getLabel("RF_IMPORT_AVASAD_PROCESS_CREATION_DEMANDE_SUCCESS") /*
                                                                                        * : BSessionUtil
                                                                                        * .getSessionFromThreadContext().
                                                                                        * getLabel(
                                                                                        * "RF_IMPORT_AVASAD_PROCESS_CREATION_DEMANDE_FAILED"
                                                                                        * )
                                                                                        */, new String[] { docPath });
    }

    private String generateDocumentLog(Map<Enum<?>, String> map) throws RFXmlmlException, Exception {
        trierLogParNumeroLigne();
        RFXmlmlContainer container = RFXmlmlMappingLogImportationAvasad.loadResults(logsList, BSessionUtil
                .getSessionFromThreadContext().getUserId());

        String nomDoc = BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_AVASAD_NOM_LOG_ETAPE_UNE");
        String docPath = RFExcelmlUtils.createDocumentExcel(BSessionUtil.getSessionFromThreadContext().getIdLangueISO()
                .toUpperCase()
                + "/" + IRFImportationAvasadListeColumns.MODEL_NAME, nomDoc, container);
        return docPath;
    }

    private String getAdministrationAfParCode(String code) throws JadePersistenceException {

        if (JadeStringUtil.isBlankOrZero(code)) {
            logErreurs("getAdministrationAfParCode()",
                    BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_AVASAD_NUMERO_AF_VIDE"), true);
            throw new IllegalArgumentException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "RF_IMPORT_AVASAD_NUMERO_AF_VIDE")
                    + " [getAdministrationAfParCode()]");
        }

        PRTiersWrapper[] administrationCms = null;
        try {
            administrationCms = PRTiersHelper.getAdministrationActiveForGenreAndCode(
                    BSessionUtil.getSessionFromThreadContext(), RFPropertiesUtils.getCsGenreAdminstrationCMS(), code);
        } catch (Exception e) {
            logErreurs("getAdministrationAfParCode()", e.getMessage(), false);
            throw new JadePersistenceException(e.getMessage());
        }

        if ((administrationCms == null) || (administrationCms.length == 0)) {
            logErreurs("getAdministrationAfParCode()",
                    BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_AVASAD_ADMINISTRATION_AF_INEXISANT")
                            + " n°:" + code, false);

            throw new JadePersistenceException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "RF_IMPORT_AVASAD_ADMINISTRATION_AF_INEXISANT")
                    + " n°:" + code + " [getAdministrationAfParCode()]");

        } else {
            String idTiers = administrationCms[0].getIdTiers();
            if (!JadeStringUtil.isBlankOrZero(idTiers)) {
                return idTiers;
            } else {
                logErreurs(
                        "getAdministrationAfParCode()",
                        BSessionUtil.getSessionFromThreadContext().getLabel(
                                "RF_IMPORT_AVASAD_ADMINISTRATION_AF_INEXISANT")
                                + " n°:" + code, false);

                throw new JadePersistenceException(BSessionUtil.getSessionFromThreadContext().getLabel(
                        "RF_IMPORT_AVASAD_ADMINISTRATION_AF_INEXISANT")
                        + " n°:" + code + " [getAdministrationAfParCode()]");
            }
        }

    }

    private String getEmail(Map<Enum<?>, String> map) {
        return /* JadeStringUtil.isBlankOrZero(map.get(RFProcessImportationAvasadEnum.EMAIL)) ? */BSessionUtil
                .getSessionFromThreadContext().getUserEMail()/* : map.get(RFProcessImportationAvasadEnum.EMAIL) */;
    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new RFImportDemandesCmsCreationHandler(logsList, idTiersAf, idExecutionProcess);
    }

    @Override
    public void init(JadeProcessExecut jadeProcessExecut) {

        idsDemandeAReexecuter = new ArrayList<String>();
        // Recherche de l'idDemande par entité depuis les properties du process
        Map<String, String> idsIdEntityIdDemande = new HashMap<String, String>();
        for (Entry<String, Map<Enum<?>, String>> entry : jadeProcessExecut.getPropertiesEntities().entrySet()) {
            idsIdEntityIdDemande.put(entry.getKey(), entry.getValue().get(RFProcessImportationAvasadEnum.ID_DEMANDE));
        }

        if (idsIdEntityIdDemande.size() > 0) {
            // Recherche des ids demandes à re-executés
            List<String> idsEntiteAReexecuter = new ArrayList<String>();
            if (jadeProcessExecut != null) {
                for (JadeAbstractModel model : jadeProcessExecut.getSimpleEntiteSearch().getSearchResults()) {
                    idsEntiteAReexecuter.add(model.getId());
                }
            }

            // Création de la liste des demandes à supprimer
            for (String idEntiteCourant : idsEntiteAReexecuter) {
                if (idsIdEntityIdDemande.containsKey(idEntiteCourant)) {
                    if (null != idsIdEntityIdDemande.get(idEntiteCourant)) {
                        idsDemandeAReexecuter.add(idsIdEntityIdDemande.get(idEntiteCourant));
                    }
                }
            }
        }

    }

    private void logErreurs(String source, String message, boolean isErreurImportation)
            throws JadeNoBusinessLogSessionError {
        JadeThread.logError(source, message);
        RFUtils.ajouterLogImportationsAvasad(JadeBusinessMessageLevels.ERROR, "", "", message, isErreurImportation,
                logsList);
    }

    @Override
    public void setInfosCurrentStep(JadeProcessExecut jadeInfo) {

    }

    private void supprimerDemandes(String idProcess) throws JadePersistenceException {
        BITransaction transaction = null;
        try {
            transaction = BSessionUtil.getSessionFromThreadContext().newTransaction();
            transaction.openTransaction();

            RFDemandeManager rfDemMgr = new RFDemandeManager();
            rfDemMgr.setSession(BSessionUtil.getSessionFromThreadContext());
            rfDemMgr.setForIdProcess(idProcess);
            if (idsDemandeAReexecuter.size() > 0) {
                rfDemMgr.setForIdsDemande(idsDemandeAReexecuter);
            }
            rfDemMgr.changeManagerSize(0);
            rfDemMgr.find();

            Iterator<RFDemande> rfDemItr = rfDemMgr.iterator();
            while (rfDemItr.hasNext()) {

                RFDemande rfDemCour = rfDemItr.next();
                if (rfDemCour != null) {
                    if (rfDemCour.getCsEtat().equals(IRFDemande.ENREGISTRE)) {

                        RFDemandeMai13 rfDemandeMai13 = new RFDemandeMai13();
                        rfDemandeMai13.setSession(BSessionUtil.getSessionFromThreadContext());
                        rfDemandeMai13.setIdDemandeMaintienDom13(rfDemCour.getIdDemande());

                        rfDemandeMai13.retrieve();
                        if (!rfDemandeMai13.isNew()) {
                            rfDemandeMai13.delete(transaction);

                            RFDemande rfDemande = new RFDemande();
                            rfDemande.setSession(BSessionUtil.getSessionFromThreadContext());
                            rfDemande.setIdDemande(rfDemCour.getIdDemande());

                            rfDemande.retrieve(transaction);

                            if (!rfDemande.isNew()) {
                                rfDemande.delete(transaction);
                            } else {
                                logErreurs("RFImportDemandesCmsCreation.supprimerDemandes()", "Demande introuvable",
                                        false);
                            }
                        } else {
                            logErreurs("RFImportDemandesCmsCreation.supprimerDemandes()", "Demande 13 introuvable",
                                    false);
                        }

                        // Suppression des motifs de refus
                        RFAssMotifsRefusDemandeManager rfAssMotifsRefusMgr = new RFAssMotifsRefusDemandeManager();
                        rfAssMotifsRefusMgr.setSession(BSessionUtil.getSessionFromThreadContext());
                        rfAssMotifsRefusMgr.changeManagerSize(0);
                        rfAssMotifsRefusMgr.setForIdDemande(rfDemandeMai13.getIdDemande());

                        rfAssMotifsRefusMgr.delete(transaction);

                    } else {
                        logErreurs("RFImportDemandesCmsCreation.supprimerDemandes()",
                                "Etat demande différent d'enregistré" + " id demande:" + rfDemCour.getIdDemande(),
                                false);
                    }
                } else {
                    logErreurs("RFImportDemandesCmsCreation.supprimerDemandes()", "Demande introuvable (null)", false);
                }
            }
        } catch (Exception e) {
            transaction.setRollbackOnly();
            throw new JadePersistenceException(e);
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                } finally {
                    try {
                        transaction.closeTransaction();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }

    private void trierLogParNumeroLigne() {
        Collections.sort(logsList, new ComparateurLogs());
    }

}
