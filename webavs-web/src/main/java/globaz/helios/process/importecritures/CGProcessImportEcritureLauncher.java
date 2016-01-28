package globaz.helios.process.importecritures;

import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import java.util.List;

public class CGProcessImportEcritureLauncher extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int NB_MAX_CAR_FOR_LIBELLE_JOURNAL = 50;
    private String directoryFile = "";

    // Constructeur
    public CGProcessImportEcritureLauncher() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
        // Nothing to do

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        String localWorkDirectory = Jade.getInstance().getPersistenceDir() + "helios_import";
        JadeFsFacade.createFolder(localWorkDirectory);
        // Récupération des fichiers
        List<String> listRemoteFileATraiterUri = JadeFsFacade.getFolderChildren(getDirectoryFile());

        for (String localFileATraiterUri : listRemoteFileATraiterUri) {
            String theFileName = JadeFilenameUtil.extractFilename(localFileATraiterUri);
            // Utile pour ne pas prendre les sous-répertoires
            if (!(JadeFsFacade.isFolder(localFileATraiterUri) || JadeStringUtil.isEmpty(theFileName)
                    || ".".equals(theFileName) || "..".equals(theFileName))) {
                // envoie pas d'email
                setSendCompletionMail(false);
                // Copie du fichier en local (persistence)
                JadeFsFacade.copyFile(localFileATraiterUri, localWorkDirectory + "/" + theFileName);
                // Lancement du process d'import des écritures dans Hélios
                CGProcessImportEcritures importEcriture = new CGProcessImportEcritures();
                importEcriture.setSession(getSession());
                importEcriture.setTraitementSchedule(Boolean.TRUE);
                importEcriture.setEMailAddress(getEMailAddress());
                importEcriture.setFileName(theFileName);
                importEcriture.setDateTraitement(JACalendar.todayJJsMMsAAAA());
                importEcriture.setDirectoryFile(getDirectoryFile());
                BProcessLauncher.start(importEcriture);
            }
        }
        return !(isAborted() || isOnError() || getSession().hasErrors());
    }

    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isBlankOrZero(getEMailAddress())) {
            this._addError(getTransaction(), "the eMailAddress is blank or null.");
        }

        if (JadeStringUtil.isBlankOrZero(getDirectoryFile())) {
            this._addError(getTransaction(), "the fileDirectory is blank or null.");
        }
    }

    public String getDirectoryFile() {
        return directoryFile;
    }

    @Override
    protected String getEMailObject() {
        // aucun email envoyé
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setDirectoryFile(String directoryFile) {
        this.directoryFile = directoryFile;
    }
}
