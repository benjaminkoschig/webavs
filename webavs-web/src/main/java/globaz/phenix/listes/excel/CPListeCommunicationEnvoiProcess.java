package globaz.phenix.listes.excel;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichageManager;
import globaz.webavs.common.CommonExcelmlContainer;
import java.util.ArrayList;
import java.util.List;

/**
 * Liste des communications fiscales en envois.
 * 
 * @author dcl
 * 
 */
public class CPListeCommunicationEnvoiProcess extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // Constantes
    public static final String MODEL_NAME = "communicationEnvoi.xml";

    public static final String NUMERO_INFOROM = "";
    private String annee = "";
    private String canton = "";
    private List<String> communicationEnErreur = new ArrayList<String>();
    private String genre = "";
    CPCommunicationFiscaleAffichageManager manager;

    /**
     * COnstructeur par defaut.
     */
    public CPListeCommunicationEnvoiProcess() {
        super();
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
        // Nothing to do
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {

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

    private boolean createDocument(CPCommunicationFiscaleAffichageManager manager) throws Exception {
        setProgressScaleValue(manager.size());
        CommonExcelmlContainer container = CPXmlmlMappingCommunicationEnvoiProcess.loadResults(manager, this);

        if (isAborted()) {
            return false;
        }

        String nomDoc = getSession().getLabel("TITRE_LISTE_COMMUNICATION_ENVOI");
        String docPath = CPExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                + CPListeCommunicationEnvoiProcess.MODEL_NAME, nomDoc, container);

        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(CPApplication.DEFAULT_APPLICATION_PHENIX);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(CPListeCommunicationEnvoiProcess.NUMERO_INFOROM);
        this.registerAttachedDocument(docInfo, docPath);

        return true;
    }

    public String getAnnee() {
        return annee;
    }

    public String getCanton() {
        return canton;
    }

    public List<String> getCommunicationEnErreur() {
        return communicationEnErreur;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("TITRE_LISTE_COMMUNICATION_ENVOI") + " : "
                + JadeCodesSystemsUtil.getCodeLibelle(getSession(), getCanton());
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

    @Override
    public String getSubjectDetail() {
        StringBuilder builder = new StringBuilder();
        String cantonString = JadeCodesSystemsUtil.getCodeLibelle(getSession(), getCanton());
        builder.append(getSession().getLabel("LISTE_COMM_FISC_CANTON"));
        builder.append(cantonString);
        builder.append("\n");
        for (Object s : getMemoryLog().getMessagesToVector()) {
            builder.append(((FWMessage) s).getMessageText());
            builder.append("\n");
        }
        return builder.toString();
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

    public void setCommunicationEnErreur(List<String> communicationEnErreur) {
        this.communicationEnErreur = communicationEnErreur;
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
