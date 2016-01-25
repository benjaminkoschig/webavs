package globaz.naos.process.taxeCo2;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.naos.db.taxeCo2.AFListeExcelTaxeCo2Manager;
import globaz.naos.listes.excel.taxeCo2.AFXmlmlMappingTaxeCo2;
import globaz.naos.listes.excel.util.AFExcelmlUtils;
import globaz.naos.listes.excel.util.NaosContainer;

public class AFListeExcelTaxeCo2Process extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Constantes
    public final static String MODEL_NAME = "listeTaxeCo2.xml";
    public final static String MODEL_REINJECTION_NAME = "listeTaxeCo2Reinjection.xml";
    public final static String NUMERO_INFOROM = "0274CAF";

    private String forAnnee = "";

    // Constructeur
    public AFListeExcelTaxeCo2Process() {
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
    protected boolean _executeProcess() throws Exception, Exception {

        AFListeExcelTaxeCo2Manager manager = new AFListeExcelTaxeCo2Manager();

        initManager(manager);

        if (manager.size() >= 1) {
            return createDocument(manager);
        } else {
            setSendCompletionMail(false);
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

        /**
         * sécurité supplémentaire ------------------------ mais on ne devrait pas se retrouver avec une adresse email
         * vide en effet, si l'email n'est pas renseigné getEMailAddress() prend l'email du parent ou à défaut, celui du
         * user connecté
         * 
         */
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            getSession().addError(getSession().getLabel("LISTE_EMPLOYEUR_RADIE_ERREUR_EMAIL_OBLIGATOIRE"));
            setSendCompletionMail(false);
            setSendMailOnError(false);
        }
    }

    private boolean createDocument(AFListeExcelTaxeCo2Manager manager) throws Exception, Exception {
        setProgressScaleValue(manager.size());
        NaosContainer container = AFXmlmlMappingTaxeCo2.loadResults(manager, this);

        if (isAborted()) {
            return false;
        }
        String nomDoc = (getSession().getLabel("LISTE_TAXE_CO2") + " " + getForAnnee());

        String docPath = "";
        if ("DE".equalsIgnoreCase(getSession().getIdLangueISO())) {
            docPath = AFExcelmlUtils.createDocumentExcel("DE" + "/" + AFListeExcelTaxeCo2Process.MODEL_NAME, nomDoc,
                    container);
        } else {
            docPath = AFExcelmlUtils.createDocumentExcel("FR" + "/" + AFListeExcelTaxeCo2Process.MODEL_NAME, nomDoc,
                    container);
        }
        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(AFListeExcelTaxeCo2Process.NUMERO_INFOROM);
        this.registerAttachedDocument(docInfo, docPath);

        return true;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("EMAIL_LISTE_TAXE_CO2_ERROR");
        } else {
            return (getSession().getLabel("EMAIL_LISTE_TAXE_CO2"));
        }
    }

    public String getForAnnee() {
        return forAnnee;
    }

    private void initManager(AFListeExcelTaxeCo2Manager manager) throws Exception {
        manager.setSession(getSession());
        manager.setForAnnee(getForAnnee());
        manager.find(BManager.SIZE_NOLIMIT);
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }
}
