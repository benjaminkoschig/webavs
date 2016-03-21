package globaz.hercule.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.controleEmployeur.CEEmployeurMasseSalarialeManager;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.mappingXmlml.CEXmlmlMappingEmployeurMasseSalariale;
import globaz.hercule.utils.CEExcelmlUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.webavs.common.CommonExcelmlContainer;

/**
 * @author hpe
 * @since Créé le 14 févr. 07
 */
public class CEEmployeurMasseSalarialeProcess extends BProcess implements FWViewBeanInterface {

    private static final long serialVersionUID = 2621299143567134262L;
    public final static String MODEL_NAME = "employeurMasseSalariale.xml";
    public final static String MODEL_REINJECTION_NAME = "employeurMasseSalarialeReinjection.xml";
    public final static String NUMERO_INFOROM = "0245CCE";

    private String forAnnee = "";
    private String fromNumAffilie = "";
    private String toNumAffilie = "";
    private String typeAdresse = "";

    public CEEmployeurMasseSalarialeProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws HerculeException, Exception {

        CEEmployeurMasseSalarialeManager manager = new CEEmployeurMasseSalarialeManager();

        initManager(manager);

        if (manager.size() >= 1) {
            return createDocument(manager);
        } else {
            getMemoryLog().logMessage(getSession().getLabel("LISTE_EMPLOYEUR_AUCUN_EMPLOYEUR"), FWMessage.INFORMATION,
                    this.getClass().getName());
        }

        return false;

    }

    @Override
    protected void _validate() throws Exception {

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

        if (JadeStringUtil.isEmpty(getForAnnee())) {
            getSession().addError(getSession().getLabel("LISTE_EMPLOYEUR_MASSE_SALARIALE_ERREUR_ANNEE_OBLIGATOIRE"));
        }

        /**
         * sécurité supplémentaire ------------------------ mais on ne devrait pas se retrouver avec une adresse email
         * vide en effet, si l'email n'est pas renseigné getEMailAddress() prend l'email du parent ou à défaut, celui du
         * user connecté
         * 
         */
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            getSession().addError(getSession().getLabel("LISTE_EMPLOYEUR_MASSE_SALARIALE_ERREUR_EMAIL_OBLIGATOIRE"));
            setSendCompletionMail(false);
            setSendMailOnError(false);
        }
    }

    private boolean createDocument(final CEEmployeurMasseSalarialeManager manager) throws HerculeException, Exception {
        setProgressScaleValue(manager.size());
        CommonExcelmlContainer container = CEXmlmlMappingEmployeurMasseSalariale.loadResults(manager, this);

        if (isAborted()) {
            return false;
        }

        String nomDoc = getSession().getLabel("LISTE_EMPLOYEUR_MASSE_SALARIALE_NOM_DOCUMENT");
        String docPath = CEExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                + CEEmployeurMasseSalarialeProcess.MODEL_NAME, nomDoc, container);

        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(CEApplication.DEFAULT_APPLICATION_HERCULE);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(CEEmployeurMasseSalarialeProcess.NUMERO_INFOROM);
        this.registerAttachedDocument(docInfo, docPath);

        return true;
    }

    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("EMAIL_OBJECT_EMPLOYEUR_MASSE_SALARIALE_ERROR");
        } else {
            return getSession().getLabel("EMAIL_OBJECT_EMPLOYEUR_MASSE_SALARIALE");
        }
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getFromNumAffilie() {
        return fromNumAffilie;
    }

    public String getToNumAffilie() {
        return toNumAffilie;
    }

    public String getTypeAdresse() {
        return typeAdresse;
    }

    private void initManager(final CEEmployeurMasseSalarialeManager manager) throws Exception {
        manager.setSession(getSession());
        manager.setForAnnee(getForAnnee());
        manager.setFromNumAffilie(getFromNumAffilie());
        manager.setToNumAffilie(getToNumAffilie());
        manager.find(BManager.SIZE_NOLIMIT);

    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setForAnnee(final String newForAnnee) {
        forAnnee = newForAnnee;
    }

    public void setFromNumAffilie(final String newFromNumAffilie) {
        fromNumAffilie = newFromNumAffilie;
    }

    public void setToNumAffilie(final String newToNumAffilie) {
        toNumAffilie = newToNumAffilie;
    }

    public void setTypeAdresse(final String typeAdresse) {
        this.typeAdresse = typeAdresse;
    }
}
