package globaz.naos.process.taxeCo2;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.naos.db.taxeCo2.AFListeExcelTaxeCo2Manager;
import globaz.naos.listes.excel.taxeCo2.AFXmlmlMappingRadieTaxeCo2;
import globaz.naos.listes.excel.util.AFExcelmlUtils;
import globaz.webavs.common.CommonExcelmlContainer;

public class AFListeExcelRadieTaxeCo2Process extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Constantes
    public final static String MODEL_NAME = "listeRadieTaxeCo2.xml";
    public final static String MODEL_REINJECTION_NAME = "listeRadieTaxeCo2Reinjection.xml";
    public final static String NUMERO_INFOROM = "0229CAF";

    private String forAnnee = "";

    // Constructeur
    public AFListeExcelRadieTaxeCo2Process() {
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

    // M?thode
    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

        /**
         * s?curit? suppl?mentaire ------------------------ mais on ne devrait pas se retrouver avec une adresse email
         * vide en effet, si l'email n'est pas renseign? getEMailAddress() prend l'email du parent ou ? d?faut, celui du
         * user connect?
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
        CommonExcelmlContainer container = AFXmlmlMappingRadieTaxeCo2.loadResults(manager, this);

        if (isAborted()) {
            return false;
        }
        String nomDoc = (getSession().getLabel("LISTE_RADIE_TAXE_CO2") + " " + getForAnnee());

        String docPath = "";
        if ("DE".equalsIgnoreCase(getSession().getIdLangueISO())) {
            docPath = AFExcelmlUtils.createDocumentExcel("DE" + "/" + AFListeExcelRadieTaxeCo2Process.MODEL_NAME,
                    nomDoc, container);
        } else {
            docPath = AFExcelmlUtils.createDocumentExcel("FR" + "/" + AFListeExcelRadieTaxeCo2Process.MODEL_NAME,
                    nomDoc, container);
        }
        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(AFListeExcelRadieTaxeCo2Process.NUMERO_INFOROM);
        this.registerAttachedDocument(docInfo, docPath);

        return true;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("EMAIL_LISTE_RADIE_TAXE_CO2_ERROR");
        } else {
            return (getSession().getLabel("EMAIL_LISTE_RADIE_TAXE_CO2"));
        }
    }

    public String getForAnnee() {
        return forAnnee;
    }

    private void initManager(AFListeExcelTaxeCo2Manager manager) throws Exception {
        manager.setSession(getSession());
        manager.setIsRadie(new Boolean(true));
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
