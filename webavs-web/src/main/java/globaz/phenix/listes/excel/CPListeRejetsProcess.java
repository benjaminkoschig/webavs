package globaz.phenix.listes.excel;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.hercule.exception.HerculeException;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPRejetsListViewBean;
import globaz.webavs.common.CommonExcelmlContainer;

public class CPListeRejetsProcess extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Constantes
    public final static String MODEL_NAME = "rejets.xml";
    public final static String NUMERO_INFOROM = "";

    CPRejetsListViewBean manager = null;

    // Constructeur
    public CPListeRejetsProcess() {
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
    protected boolean _executeProcess() throws Exception {
        manager.setSession(getSession());
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

    private boolean createDocument(CPRejetsListViewBean manager) throws HerculeException, Exception {
        manager.find(BManager.SIZE_NOLIMIT);
        setProgressScaleValue(manager.size());
        CommonExcelmlContainer container = CPXmlmlMappingRejetsProcess.loadResults(manager, this);

        if (isAborted()) {
            return false;
        }

        String nomDoc = getSession().getLabel("TITRE_LISTE_COMMUNICATION_REJETS");
        String docPath = CPExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                + CPListeRejetsProcess.MODEL_NAME, nomDoc, container);

        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(CPApplication.DEFAULT_APPLICATION_PHENIX);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setDocumentType("0292CCP");
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(CPListeRejetsProcess.NUMERO_INFOROM);
        this.registerAttachedDocument(docInfo, docPath);

        return true;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("TITRE_LISTE_COMMUNICATION_REJETS");
        } else {
            return getSession().getLabel("TITRE_LISTE_COMMUNICATION_REJETS");
        }
    }

    /**
     * getter
     */
    public CPRejetsListViewBean getManager() {
        return manager;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * setter
     */
    public void setManager(CPRejetsListViewBean manager) {
        this.manager = manager;
    }
}
