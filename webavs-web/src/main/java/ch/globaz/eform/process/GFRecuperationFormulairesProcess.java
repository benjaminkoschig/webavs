package ch.globaz.eform.process;


import ch.globaz.simpleoutputlist.exception.TechnicalException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GFRecuperationFormulairesProcess extends BProcess {

    protected static final String BACKUP_FOLDER = "/backup/";
    protected static final String ERRORS_FOLDER = "/errors/";
    protected static final String XML_EXTENSION = ".xml";
    protected static final String ZIP_EXTENSION = ".zip";

    protected String backupFolder;
    protected String errorsFolder;
    protected String storageFolder;
    protected String demandeFolder;

    protected BSession bsession;

    @Override
    protected void _executeCleanUp() {
        // cleanup done previously in the process
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            LOG.info("Lancement du process d'importation des formulaires P14.");
            initBsession();
            this.setSendCompletionMail(true);
            this.setSendMailOnError(true);
            importFiles();
        } catch (Exception e) {
            setReturnCode(-1);
            throw new TechnicalException("Erreur dans le process d'importation.", e);
        } finally {
            closeBsession();
        }

        try {

        } catch (Exception e1) {
            setReturnCode(-1);
            throw new TechnicalException("Problème à l'envoi du mail", e1);
        }
        LOG.info("Fin du process d'importation.");
        return true;
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Initialisation de la session
     *
     * @throws Exception : lance une exception si un problème intervient lors de l'initialisation du contexte
     */
    private void initBsession() throws Exception {
        bsession = getSession();
        BSessionUtil.initContext(bsession, this);
    }

    /**
     *  Fermeture de la session
     */
    private void closeBsession() {
        BSessionUtil.stopUsingContext(this);
    }

    private void importFiles(){

    }

    private void importFile(String nomFichier){

    }

    private String getXmlFilePathFromZip(String nomFichier){
        return null;
    }

    private String getMessageFromFile(String destPath) {
        return null;
    }
}
