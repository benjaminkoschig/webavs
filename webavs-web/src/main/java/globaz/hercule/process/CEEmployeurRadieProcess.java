package globaz.hercule.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.controleEmployeur.CEEmployeurRadieManager;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.mappingXmlml.CEXmlmlMappingEmployeurRadie;
import globaz.hercule.utils.CEExcelmlUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.webavs.common.CommonExcelmlContainer;

public class CEEmployeurRadieProcess extends BProcess implements FWViewBeanInterface {

    private static final long serialVersionUID = 8747866621634540432L;
    public final static String MODEL_NAME = "employeurRadie.xml";
    public final static String MODEL_REINJECTION_NAME = "employeurRadieReinjection.xml";
    public final static String NUMERO_INFOROM = "0248CCE";

    private String forMotifRadiation = "";
    private String fromDateRadiation = "";
    private String fromMasseSalariale = "";
    private String toDateRadiation = "";
    private String toMasseSalariale = "";
    private String typeAdresse = "";

    public CEEmployeurRadieProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws HerculeException, Exception {

        CEEmployeurRadieManager manager = new CEEmployeurRadieManager();

        initManager(manager);

        if (manager.size() >= 1) {
            return createDocument(manager);
        }

        return false;

    }

    @Override
    protected void _validate() throws Exception {

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

        if (JadeStringUtil.isEmpty(getFromDateRadiation()) || JadeStringUtil.isEmpty(getToDateRadiation())
                || (new JADate(getFromDateRadiation()).getYear() != new JADate(getToDateRadiation()).getYear())) {
            getSession().addError(getSession().getLabel("LISTE_EMPLOYEUR_RADIE_ERREUR_DATE_RADIATION"));
        }

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

    private boolean createDocument(final CEEmployeurRadieManager manager) throws HerculeException, Exception {
        setProgressScaleValue(manager.size());
        CommonExcelmlContainer container = CEXmlmlMappingEmployeurRadie.loadResults(manager, this);

        if (isAborted()) {
            return false;
        }

        String nomDoc = getSession().getLabel("LISTE_EMPLOYEUR_RADIE_NOM_DOCUMENT");
        String docPath = CEExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                + CEEmployeurRadieProcess.MODEL_NAME, nomDoc, container);

        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(CEApplication.DEFAULT_APPLICATION_HERCULE);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(CEEmployeurRadieProcess.NUMERO_INFOROM);
        this.registerAttachedDocument(docInfo, docPath);

        return true;
    }

    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("EMAIL_OBJECT_EMPLOYEUR_RADIE_ERROR");
        } else {
            return getSession().getLabel("EMAIL_OBJECT_EMPLOYEUR_RADIE");
        }
    }

    public String getForMotifRadiation() {
        return forMotifRadiation;
    }

    public String getFromDateRadiation() {
        return fromDateRadiation;
    }

    public String getFromMasseSalariale() {
        return fromMasseSalariale;
    }

    public String getToDateRadiation() {
        return toDateRadiation;
    }

    public String getToMasseSalariale() {
        return toMasseSalariale;
    }

    public String getTypeAdresse() {
        return typeAdresse;
    }

    private void initManager(final CEEmployeurRadieManager manager) throws Exception {
        manager.setSession(getSession());
        manager.setFromDateRadiation(getFromDateRadiation());
        manager.setToDateRadiation(getToDateRadiation());
        manager.setFromMasseSalariale(getFromMasseSalariale());
        manager.setToMasseSalariale(getToMasseSalariale());
        manager.setForMotifRadiation(getForMotifRadiation());
        manager.find(BManager.SIZE_NOLIMIT);
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setForMotifRadiation(final String forMotifRadiation) {
        this.forMotifRadiation = forMotifRadiation;
    }

    public void setFromDateRadiation(final String newFromDateRadiation) {
        fromDateRadiation = newFromDateRadiation;
    }

    public void setFromMasseSalariale(final String newFromMasseSalariale) {
        fromMasseSalariale = newFromMasseSalariale;
    }

    public void setToDateRadiation(final String newToDateRadiation) {
        toDateRadiation = newToDateRadiation;
    }

    public void setToMasseSalariale(final String newToMasseSalariale) {
        toMasseSalariale = newToMasseSalariale;
    }

    public void setTypeAdresse(final String typeAdresse) {
        this.typeAdresse = typeAdresse;
    }
}
