package globaz.osiris.process.importoperations;

import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import java.util.List;

/**
 * @author MMO 10.04.2012
 **/

public class CAProcessImportOperationsLauncher extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String LOCAL_WORK_DIRECTORY = "osirisOperationsFiles";
    public static final int NB_MAX_CAR_FOR_LIBELLE_JOURNAL = 50;

    private String remoteDirectoryATraiter = "";
    private String remoteDirectoryTraiteKo = "";

    private String remoteDirectoryTraiteOk = "";

    // Constructeur
    public CAProcessImportOperationsLauncher() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
        // Nothing to do

    }

    @Override
    protected boolean _executeProcess() throws Exception {

        // envoie pas d'email
        setSendCompletionMail(false);

        // validation des inputs
        StringBuffer wrongInputBuffer = new StringBuffer();

        if (JadeStringUtil.isBlankOrZero(getEMailAddress())) {
            wrongInputBuffer.append("the eMailAddress is blank or zero / ");
        }

        if (JadeStringUtil.isBlankOrZero(getRemoteDirectoryATraiter())) {
            wrongInputBuffer.append("the remoteDirectoryATraiter is blank or zero / ");
        }

        if (JadeStringUtil.isBlankOrZero(getRemoteDirectoryTraiteKo())) {
            wrongInputBuffer.append("the remoteDirectoryTraiteKo is blank or zero / ");
        }

        if (JadeStringUtil.isBlankOrZero(getRemoteDirectoryTraiteOk())) {
            wrongInputBuffer.append("the remoteDirectoryTraiteOk is blank or zero / ");
        }

        if (wrongInputBuffer.length() >= 1) {
            throw new Exception(this.getClass().getName()
                    + " is unable to start operations import due to wrong inputs : " + wrongInputBuffer.toString());
        }

        String localWorkDirectory = Jade.getInstance().getPersistenceDir()
                + CAProcessImportOperationsLauncher.LOCAL_WORK_DIRECTORY;

        JadeFsFacade.createFolder(localWorkDirectory);

        // Vidage du répertoire de travail local (CGProcessImportEcritureLauncher.LOCAL_WORK_DIRECTORY)
        List<String> listOldLocalFile = JadeFsFacade.getFolderChildren(localWorkDirectory);
        for (String oldLocalFile : listOldLocalFile) {
            JadeFsFacade.delete(oldLocalFile);
        }

        // Copie des fichiers en local (dans le répertoire
        // persistence/CGProcessImportEcritureLauncher.LOCAL_WORK_DIRECTORY)
        List<String> listRemoteFileATraiterUri = JadeFsFacade.getFolderChildren(getRemoteDirectoryATraiter());

        for (String remoteFileATraiterUri : listRemoteFileATraiterUri) {
            String theFileName = JadeFilenameUtil.extractFilename(remoteFileATraiterUri);

            if (!(JadeFsFacade.isFolder(remoteFileATraiterUri) || JadeStringUtil.isEmpty(theFileName)
                    || ".".equals(theFileName) || "..".equals(theFileName))) {
                JadeFsFacade.copyFile(remoteFileATraiterUri, localWorkDirectory + "/" + theFileName);
            }
        }

        // Traitement de chaque fichier
        List<String> listLocalFileATraiterUri = JadeFsFacade.getFolderChildren(localWorkDirectory);

        for (String localFileATraiterUri : listLocalFileATraiterUri) {

            String libelleJournal = JadeFilenameUtil.extractFilenameRoot(localFileATraiterUri);
            if (!JadeStringUtil.isBlank(libelleJournal)
                    && (libelleJournal.length() > CAProcessImportOperationsLauncher.NB_MAX_CAR_FOR_LIBELLE_JOURNAL)) {
                libelleJournal = libelleJournal.substring(0,
                        CAProcessImportOperationsLauncher.NB_MAX_CAR_FOR_LIBELLE_JOURNAL);
            }

            CAProcessImportOperations processImportOperations = new CAProcessImportOperations();
            processImportOperations.setSession(getSession());
            processImportOperations.setIsBatch(new Boolean(true));
            processImportOperations.setEMailAddress(getEMailAddress());
            processImportOperations.setFileName(localFileATraiterUri);
            processImportOperations.setDateTraitement(JACalendar.todayJJsMMsAAAA());
            processImportOperations.setLibelleJournal(libelleJournal);
            processImportOperations.setRemoteDirectoryATraiter(remoteDirectoryATraiter);
            processImportOperations.setRemoteDirectoryTraiteKo(remoteDirectoryTraiteKo);
            processImportOperations.setRemoteDirectoryTraiteOk(remoteDirectoryTraiteOk);
            BProcessLauncher.start(processImportOperations);

        }

        return !(isAborted() || isOnError() || getSession().hasErrors());

    }

    @Override
    protected String getEMailObject() {
        // aucun email envoyé
        return null;
    }

    public String getRemoteDirectoryATraiter() {
        return remoteDirectoryATraiter;
    }

    public String getRemoteDirectoryTraiteKo() {
        return remoteDirectoryTraiteKo;
    }

    public String getRemoteDirectoryTraiteOk() {
        return remoteDirectoryTraiteOk;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setRemoteDirectoryATraiter(String remoteDirectoryATraiter) {
        this.remoteDirectoryATraiter = remoteDirectoryATraiter;
    }

    public void setRemoteDirectoryTraiteKo(String remoteDirectoryTraiteKo) {
        this.remoteDirectoryTraiteKo = remoteDirectoryTraiteKo;
    }

    public void setRemoteDirectoryTraiteOk(String remoteDirectoryTraiteOk) {
        this.remoteDirectoryTraiteOk = remoteDirectoryTraiteOk;
    }

}
