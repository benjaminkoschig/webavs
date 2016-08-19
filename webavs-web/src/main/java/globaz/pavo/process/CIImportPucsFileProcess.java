package globaz.pavo.process;

import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pavo.application.CIApplication;
import globaz.webavs.common.CommonExcelmlContainer;
import globaz.webavs.common.CommonExcelmlUtils;
import java.io.IOException;
import java.util.List;

/**
 * @author MMO 10.11.2011
 **/

public class CIImportPucsFileProcess extends BProcess {

    private static final long serialVersionUID = -314714724478084902L;

    public static final String IMPORT_STATUT_KO = "ko";
    public static final String IMPORT_STATUT_OK = "ok";
    public static final String LOCAL_PUCS_FILE_DIRECTORY_NAME = "importedPucsFile";
    public static final String MODEL_XML_NAME = "RapportImportedPucsFileModele.xml";
    public static final String MODEL_XML_AF_SEULE_NAME = "RapportImportedSwissDecFileModeleAFSeule.xml";
    public static final String MODEL_XML_SWISSDEC_NAME = "RapportImportedSwissDecFileModele.xml";
    public static final String OUTPUT_EXCELML_NAME = "RapportImportedFile.xml";
    public static final String OUTPUT_EXCELML_AF_SEULE_NAME = "RapportImportedFileAFSeule.xml";

    private CommonExcelmlContainer containerRapportExcelmlImportedPucsFile = new CommonExcelmlContainer();
    private CommonExcelmlContainer containerRapportExcelmlImportedAFSeule = new CommonExcelmlContainer();
    private String importStatutAFile = CIImportPucsFileProcess.IMPORT_STATUT_OK;
    private String modeExecution = "";
    private String nombreFichierATraiter = "";
    private String processExecutionStatut = CIImportPucsFileProcess.IMPORT_STATUT_OK;
    private String remotePucsFileDirectory = "";
    private String remotePucsFileDirectoryTraiteKo = "";
    private String remotePucsFileDirectoryTraiteOk = "";
    private String typeFichierPucs = "";
    private String provenance = "";
    private boolean traitementAFSeul = false;

    // Constructeur
    public CIImportPucsFileProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
        // Nothing to do

    }

    @Override
    protected boolean _executeProcess() throws Exception {

        try {

            // validation des inputs
            StringBuffer wrongInputBuffer = new StringBuffer();

            if (JadeStringUtil.isBlankOrZero(getEMailAddress())) {
                wrongInputBuffer.append("the eMailAddress is blank or zero / ");
            }

            if (getContainerRapportExcelmlImportedPucsFile() == null) {
                wrongInputBuffer.append("the containerRapportExcelmlImportedPucsFile is null / ");
            }

            if (JadeStringUtil.isBlankOrZero(getRemotePucsFileDirectory())) {
                wrongInputBuffer.append("the remotePucsFileDirectory is blank or zero / ");
            }

            if (JadeStringUtil.isBlankOrZero(getRemotePucsFileDirectoryTraiteKo())) {
                wrongInputBuffer.append("the remotePucsFileDirectoryTraiteKo is blank or zero / ");
            }

            if (JadeStringUtil.isBlankOrZero(getRemotePucsFileDirectoryTraiteOk())) {
                wrongInputBuffer.append("the remotePucsFileDirectoryTraiteOk is blank or zero / ");
            }

            if (JadeStringUtil.isBlankOrZero(getTypeFichierPucs())) {
                wrongInputBuffer.append("the typeFichierPucs is blank or zero / ");
            }

            if (JadeStringUtil.isBlankOrZero(getProvenance())) {
                wrongInputBuffer.append("the provenance is blank or zero / ");
            }

            if (wrongInputBuffer.length() >= 1) {
                throw new Exception("unable to start pucs file import due to wrong inputs : "
                        + wrongInputBuffer.toString());
            }

            // 0 --> traiter tous les fichiers du répertoire
            int theNombreFichierATraiter = 0;
            if (JadeNumericUtil.isIntegerPositif(getNombreFichierATraiter())) {
                theNombreFichierATraiter = Integer.valueOf(getNombreFichierATraiter()).intValue();
            }

            // Copie des fichiers pucs en local (dans le répertoire persistence/LOCAL_PUCS_FILE_DIRECTORY_NAME)
            // puis suppression des fichiers copiés du répertoire distant
            String localPucsFileDirectory = Jade.getInstance().getPersistenceDir()
                    + CIImportPucsFileProcess.LOCAL_PUCS_FILE_DIRECTORY_NAME;

            // String localPucsFileDirectory = "C:/localPucsFile" + "/"
            // + CIImportPucsFileProcess.LOCAL_PUCS_FILE_DIRECTORY_NAME;

            JadeFsFacade.createFolder(localPucsFileDirectory);

            List<String> listRemotePucsFileUri = JadeFsFacade.getFolderChildren(getRemotePucsFileDirectory());

            int i = 0;
            for (String remotePucsFileUri : listRemotePucsFileUri) {
                if ((theNombreFichierATraiter != 0) && (i >= theNombreFichierATraiter)) {
                    break;
                }
                String theFileName = JadeFilenameUtil.extractFilename(remotePucsFileUri);

                if (!(JadeFsFacade.isFolder(remotePucsFileUri) || JadeStringUtil.isEmpty(theFileName)
                        || ".".equals(theFileName) || "..".equals(theFileName) || ".xml".equals(theFileName))) {
                    JadeFsFacade.copyFile(remotePucsFileUri, localPucsFileDirectory + "/" + theFileName);
                    JadeFsFacade.delete(remotePucsFileUri);
                    i++;
                }
            }

            // Traitement de chaque fichier pucs

            // le traitement se fait en synchrone (executeProcess)
            // ceci car le processus parent (this) doit créer un rapport excel récapitulant les fichiers pucs importés
            // et
            // placer ces derniers dans un répertoire différent en fonction de leur statut d'importation (ok / ko)

            // une erreur lors du traitement d'un fichier ne doit pas stopper le processus

            // CIDeclaration se charge du traitement d'un fichier pucs
            // récupération des donnnées qu'il contient
            // création de la DS
            // création du journal CI
            // calcul des masses de la DS

            // le mode synchrone ne gère pas l'envoi de mails
            // c'est pourquoi l'envoi est fait manuellement dans ce process

            // 2 variables processExecutionStatut et importStatutAFile
            // permettent de gérer l'exécution du process sans ajouter d'erreurs dans la transaction ou la session et
            // risquer de le bloquer
            List<String> listLocalPucsFileUri = JadeFsFacade.getFolderChildren(localPucsFileDirectory);

            String theRemotePucsFileDirectoryForTraite;
            for (String localPucsFileUri : listLocalPucsFileUri) {
                // vide le buffer d'erreur de la session (au cas où)
                getSession().getErrors();
                importStatutAFile = CIImportPucsFileProcess.IMPORT_STATUT_OK;
                theRemotePucsFileDirectoryForTraite = getRemotePucsFileDirectoryTraiteOk();
                try {
                    CIDeclaration theDeclarationCreator = new CIDeclaration();
                    theDeclarationCreator.setSession(getSession());
                    theDeclarationCreator.setEMailAddress(getEMailAddress());
                    theDeclarationCreator.setSimulation(getModeExecution());
                    theDeclarationCreator.setProvenance(getProvenance());
                    theDeclarationCreator.setType(getTypeFichierPucs());
                    theDeclarationCreator.setAccepteAnneeEnCours("true");
                    theDeclarationCreator.setAccepteEcrituresNegatives("true");
                    theDeclarationCreator.setAccepteLienDraco("true");
                    theDeclarationCreator.setIsBatch(new Boolean(true));
                    theDeclarationCreator.setFilename(localPucsFileUri);
                    theDeclarationCreator.setLauncherImportPucsFileProcess(this);
                    theDeclarationCreator.executeProcess();

                    if (!theDeclarationCreator.isPUCS4()) {
                        Object[] attachedDocumentArray = theDeclarationCreator.getAttachedDocuments().toArray();
                        String[] attachedDocumentLocationArray = null;
                        if (attachedDocumentArray != null) {
                            int nbElem = attachedDocumentArray.length;
                            attachedDocumentLocationArray = new String[nbElem];
                            for (int k = 0; k < nbElem; k++) {
                                attachedDocumentLocationArray[k] = ((JadePublishDocument) attachedDocumentArray[k])
                                        .getDocumentLocation();
                            }
                        }

                        JadeSmtpClient.getInstance().sendMail(theDeclarationCreator.getEMailAddress(),
                                theDeclarationCreator.getEMailObject(), theDeclarationCreator.getSubjectDetail(),
                                attachedDocumentLocationArray);

                    }

                    if (CIImportPucsFileProcess.IMPORT_STATUT_KO.equalsIgnoreCase(importStatutAFile)) {
                        processExecutionStatut = CIImportPucsFileProcess.IMPORT_STATUT_KO;
                        theRemotePucsFileDirectoryForTraite = getRemotePucsFileDirectoryTraiteKo();
                    }

                } catch (Exception e) {
                    importStatutAFile = CIImportPucsFileProcess.IMPORT_STATUT_KO;
                    processExecutionStatut = CIImportPucsFileProcess.IMPORT_STATUT_KO;
                    theRemotePucsFileDirectoryForTraite = getRemotePucsFileDirectoryTraiteKo();
                    getMemoryLog().logMessage(localPucsFileUri + " : " + e.toString(), FWMessage.INFORMATION,
                            this.getClass().getName());
                } finally {
                    try {
                        JadeFsFacade.copyFile(
                                localPucsFileUri,
                                theRemotePucsFileDirectoryForTraite + "/"
                                        + JadeFilenameUtil.extractFilenameRoot(localPucsFileUri) + "_"
                                        + JACalendar.today().toStrAMJ() + "."
                                        + JadeFilenameUtil.extractFilenameExtension(localPucsFileUri));
                        JadeFsFacade.delete(localPucsFileUri);
                    } catch (Exception e2) {
                        processExecutionStatut = CIImportPucsFileProcess.IMPORT_STATUT_KO;
                        getMemoryLog().logMessage(localPucsFileUri + " : " + e2.toString(), FWMessage.INFORMATION,
                                this.getClass().getName());
                    }
                }
            }

            // création du rapport Excelml (selon le modèle RapportImportedPucsFileModele.xml) récapitulant les fichiers
            // pucs importés
            creationRapportExcelPourDS();
            // création du rapport Excelml récapitulant les affiliés ayant les AF seuls
            creationRapportExcelmlPourAFSeul();
        } catch (Exception e) {
            processExecutionStatut = CIImportPucsFileProcess.IMPORT_STATUT_KO;
            getMemoryLog().logMessage(e.toString(), FWMessage.INFORMATION, this.getClass().getName());
            return false;
        }

        return !(isAborted() || isOnError() || getSession().hasErrors());

    }

    private void creationRapportExcelPourDS() throws Exception, IOException {
        if (containerRapportExcelmlImportedPucsFile.size() >= 1) {

            containerRapportExcelmlImportedPucsFile.put("P_BLANK_CELL", " ");

            String xmlModelPath = "";

            if (DSDeclarationViewBean.PROVENANCE_SWISSDEC.equalsIgnoreCase(getProvenance())) {
                containerRapportExcelmlImportedPucsFile.put(
                        "P_TITRE_DATE_VAL",
                        getSession().getLabel("INFOROM363_TITRE_RAPPORT_EXCELML_SWISSDEC") + " - "
                                + JACalendar.todayJJsMMsAAAA());
                xmlModelPath = Jade.getInstance().getExternalModelDir() + CIApplication.APPLICATION_PAVO_REP
                        + "/model/excelml/" + getSession().getIdLangueISO().toUpperCase() + "/"
                        + CIImportPucsFileProcess.MODEL_XML_SWISSDEC_NAME;
            } else {
                containerRapportExcelmlImportedPucsFile.put(
                        "P_TITRE_DATE_VAL",
                        getSession().getLabel("INFOROM363_TITRE_RAPPORT_EXCELML") + " - "
                                + JACalendar.todayJJsMMsAAAA());
                xmlModelPath = Jade.getInstance().getExternalModelDir() + CIApplication.APPLICATION_PAVO_REP
                        + "/model/excelml/" + getSession().getIdLangueISO().toUpperCase() + "/"
                        + CIImportPucsFileProcess.MODEL_XML_NAME;
            }

            String xlsDocPath = Jade.getInstance().getPersistenceDir()
                    + JadeFilenameUtil.addOrReplaceFilenameSuffixUID(CIImportPucsFileProcess.OUTPUT_EXCELML_NAME);

            xlsDocPath = CommonExcelmlUtils.createDocumentExcel(xmlModelPath, xlsDocPath,
                    containerRapportExcelmlImportedPucsFile);

            // Publication du document
            JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
            docInfoExcel.setApplicationDomain(CIApplication.DEFAULT_APPLICATION_PAVO);
            docInfoExcel.setDocumentTitle(CIImportPucsFileProcess.OUTPUT_EXCELML_NAME);
            docInfoExcel.setPublishDocument(true);
            docInfoExcel.setArchiveDocument(false);
            this.registerAttachedDocument(docInfoExcel, xlsDocPath);
        }
    }

    private void creationRapportExcelmlPourAFSeul() throws Exception, IOException {
        if (containerRapportExcelmlImportedAFSeule.size() >= 1) {

            containerRapportExcelmlImportedAFSeule.put(
                    "P_TITRE_DATE_VAL",
                    getSession().getLabel("INFOROM363_TITRE_RAPPORT_EXCELML_AF_SEULE") + " - "
                            + JACalendar.todayJJsMMsAAAA());
            containerRapportExcelmlImportedAFSeule.put("P_BLANK_CELL", " ");

            String xmlModelPath = Jade.getInstance().getExternalModelDir() + CIApplication.APPLICATION_PAVO_REP
                    + "/model/excelml/" + getSession().getIdLangueISO().toUpperCase() + "/"
                    + CIImportPucsFileProcess.MODEL_XML_AF_SEULE_NAME;

            String xlsDocPath = Jade.getInstance().getPersistenceDir()
                    + JadeFilenameUtil
                            .addOrReplaceFilenameSuffixUID(CIImportPucsFileProcess.OUTPUT_EXCELML_AF_SEULE_NAME);

            xlsDocPath = CommonExcelmlUtils.createDocumentExcel(xmlModelPath, xlsDocPath,
                    containerRapportExcelmlImportedAFSeule);

            // Publication du document
            JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
            docInfoExcel.setApplicationDomain(CIApplication.DEFAULT_APPLICATION_PAVO);
            docInfoExcel.setDocumentTitle(CIImportPucsFileProcess.OUTPUT_EXCELML_AF_SEULE_NAME);
            docInfoExcel.setPublishDocument(true);
            docInfoExcel.setArchiveDocument(false);
            this.registerAttachedDocument(docInfoExcel, xlsDocPath);
        }
    }

    public CommonExcelmlContainer getContainerRapportExcelmlImportedAFSeule() {
        return containerRapportExcelmlImportedAFSeule;
    }

    public CommonExcelmlContainer getContainerRapportExcelmlImportedPucsFile() {
        return containerRapportExcelmlImportedPucsFile;
    }

    @Override
    public String getEMailObject() {
        String emailObject = getSession().getLabel("INFOROM363_EMAIL_OBJECT_SUCCES");
        if (DSDeclarationViewBean.PROVENANCE_SWISSDEC.equalsIgnoreCase(getProvenance())) {
            if (isTraitementAFSeul()) {
                emailObject = getSession().getLabel("INFOROM363_SWISSDEC_AFSEULE_EMAIL_OBJECT_SUCCES");
            } else {
                emailObject = getSession().getLabel("INFOROM363_SWISSDEC_EMAIL_OBJECT_SUCCES");
            }
        }

        if (isAborted() || isOnError() || getSession().hasErrors()
                || CIImportPucsFileProcess.IMPORT_STATUT_KO.equalsIgnoreCase(processExecutionStatut)) {
            emailObject = getSession().getLabel("INFOROM363_EMAIL_OBJECT_ERREUR");
            if (DSDeclarationViewBean.PROVENANCE_SWISSDEC.equalsIgnoreCase(getProvenance())) {
                if (isTraitementAFSeul()) {
                    emailObject = getSession().getLabel("INFOROM363_SWISSDEC_AFSEULE_EMAIL_OBJECT_ERREUR");
                } else {
                    emailObject = getSession().getLabel("INFOROM363_SWISSDEC_EMAIL_OBJECT_ERREUR");
                }
            }
        }

        return emailObject;
    }

    public String getImportStatutAFile() {
        return importStatutAFile;
    }

    public String getModeExecution() {
        return modeExecution;
    }

    public String getNombreFichierATraiter() {
        return nombreFichierATraiter;
    }

    public String getProcessExecutionStatut() {
        return processExecutionStatut;
    }

    public String getProvenance() {
        return provenance;
    }

    public String getRemotePucsFileDirectory() {
        return remotePucsFileDirectory;
    }

    public String getRemotePucsFileDirectoryTraiteKo() {
        return remotePucsFileDirectoryTraiteKo;
    }

    public String getRemotePucsFileDirectoryTraiteOk() {
        return remotePucsFileDirectoryTraiteOk;
    }

    public String getTypeFichierPucs() {
        return typeFichierPucs;
    }

    public boolean isTraitementAFSeul() {
        return traitementAFSeul;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setTraitementAFSeul(boolean traitementAFSeul) {
        this.traitementAFSeul = traitementAFSeul;
    }

    public void setContainerRapportExcelmlImportedAFSeule(CommonExcelmlContainer containerRapportExcelmlImportedAFSeule) {
        this.containerRapportExcelmlImportedAFSeule = containerRapportExcelmlImportedAFSeule;
    }

    public void setContainerRapportExcelmlImportedPucsFile(
            CommonExcelmlContainer containerRapportExcelmlImportedPucsFile) {
        this.containerRapportExcelmlImportedPucsFile = containerRapportExcelmlImportedPucsFile;
    }

    public void setImportStatutAFile(String importStatutAFile) {
        this.importStatutAFile = importStatutAFile;
    }

    public void setModeExecution(String modeExecution) {
        this.modeExecution = modeExecution;
    }

    public void setNombreFichierATraiter(String nombreFichierATraiter) {
        this.nombreFichierATraiter = nombreFichierATraiter;
    }

    public void setProcessExecutionStatut(String processExecutionStatut) {
        this.processExecutionStatut = processExecutionStatut;
    }

    public void setRemotePucsFileDirectory(String remotePucsFileDirectory) {
        this.remotePucsFileDirectory = remotePucsFileDirectory;
    }

    public void setRemotePucsFileDirectoryTraiteKo(String remotePucsFileDirectoryTraiteKo) {
        this.remotePucsFileDirectoryTraiteKo = remotePucsFileDirectoryTraiteKo;
    }

    public void setRemotePucsFileDirectoryTraiteOk(String remotePucsFileDirectoryTraiteOk) {
        this.remotePucsFileDirectoryTraiteOk = remotePucsFileDirectoryTraiteOk;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public void setTypeFichierPucs(String typeFichierPucs) {
        this.typeFichierPucs = typeFichierPucs;
    }

}
