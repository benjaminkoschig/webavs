package globaz.phenix.listes.excel;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.hercule.exception.HerculeException;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichageManager;

/**
 * Impression des cas dont le canton est vide ou étranger
 * 
 * 
 */
public class CPListeCommunicationEnvoiCantonAZeroProcess extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Constantes
    public final static String MODEL_NAME = "communicationEnvoiCantonAZero.xml";
    public final static String NUMERO_INFOROM = "";
    private String annee = "";
    private String canton = "";
    private String genre = "";

    CPCommunicationFiscaleAffichageManager manager;

    // Constructeur
    public CPListeCommunicationEnvoiCantonAZeroProcess() {
        super();
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws HerculeException, Exception {
        manager.find();
        if (manager.size() >= 1) {
            return createDocument(manager);
        }

        return false;

    }

    // Méthode
    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    private boolean createDocument(CPCommunicationFiscaleAffichageManager manager) throws HerculeException, Exception {
        // manager.find(BManager.SIZE_NOLIMIT);
        setProgressScaleValue(manager.size());
        PhenixContainer container = CPXmlmlMappingCommunicationEnvoiCantonAZeroProcess.loadResults(manager, this);

        if (isAborted()) {
            return false;
        }

        String nomDoc = getSession().getLabel("TITRE_LISTE_COMMUNICATION_ENVOI_CANTON_ZERO");
        String docPath = CPExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                + CPListeCommunicationEnvoiCantonAZeroProcess.MODEL_NAME, nomDoc, container);

        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(CPApplication.DEFAULT_APPLICATION_PHENIX);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(CPListeCommunicationEnvoiCantonAZeroProcess.NUMERO_INFOROM);
        this.registerAttachedDocument(docInfo, docPath);

        return true;
    }

    public String getAnnee() {
        return annee;
    }

    public String getCanton() {
        return canton;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("TITRE_LISTE_COMMUNICATION_ENVOI_CANTON_ZERO");
        } else {
            return getSession().getLabel("TITRE_LISTE_COMMUNICATION_ENVOI_CANTON_ZERO");
        }
    }

    public String getGenre() {
        return genre;
    }

    /**
     * getter
     */
    public CPCommunicationFiscaleAffichageManager getManager() {
        return manager;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * setter
     */
    public void setManager(CPCommunicationFiscaleAffichageManager manager) {
        this.manager = manager;
    }
}
