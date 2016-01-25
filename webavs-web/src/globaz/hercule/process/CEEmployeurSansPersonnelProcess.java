package globaz.hercule.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.controleEmployeur.CEEmployeurSansPersonnelManager;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.mappingXmlml.CEXmlmlMappingEmployeurSansPersonnel;
import globaz.hercule.utils.CEExcelmlUtils;
import globaz.hercule.utils.HerculeContainer;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;

public class CEEmployeurSansPersonnelProcess extends BProcess implements FWViewBeanInterface {

    private static final long serialVersionUID = -6553125380860689292L;
    public final static String MODEL_NAME = "employeurSansPersonnel.xml";
    public final static String MODEL_REINJECTION_NAME = "employeurSansPersonnelReinjection.xml";
    public final static String NUMERO_INFOROM = "0247CCE";

    private String forAnnee = "";
    private String typeAdresse = "";

    public CEEmployeurSansPersonnelProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws HerculeException, Exception {

        CEEmployeurSansPersonnelManager manager = new CEEmployeurSansPersonnelManager();

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

        if (JadeStringUtil.isEmpty(getForAnnee())) {
            getSession().addError(getSession().getLabel("LISTE_EMPLOYEUR_SANS_PERSONNEL_ERREUR_ANNEE_OBLIGATOIRE"));
        }

        /**
         * sécurité supplémentaire ------------------------ mais on ne devrait pas se retrouver avec une adresse email
         * vide en effet, si l'email n'est pas renseigné getEMailAddress() prend l'email du parent ou à défaut, celui du
         * user connecté
         * 
         */
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            getSession().addError(getSession().getLabel("LISTE_EMPLOYEUR_SANS_PERSONNEL_ERREUR_EMAIL_OBLIGATOIRE"));
            setSendCompletionMail(false);
            setSendMailOnError(false);
        }
    }

    private boolean createDocument(final CEEmployeurSansPersonnelManager manager) throws HerculeException, Exception {
        setProgressScaleValue(manager.size());
        HerculeContainer container = CEXmlmlMappingEmployeurSansPersonnel.loadResults(manager, this);

        if (isAborted()) {
            return false;
        }

        String nomDoc = getSession().getLabel("LISTE_EMPLOYEUR_SANS_PERSONNEL_NOM_DOCUMENT");
        String docPath = CEExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                + CEEmployeurSansPersonnelProcess.MODEL_NAME, nomDoc, container);

        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(CEApplication.DEFAULT_APPLICATION_HERCULE);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(CEEmployeurSansPersonnelProcess.NUMERO_INFOROM);
        this.registerAttachedDocument(docInfo, docPath);

        return true;
    }

    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("EMAIL_OBJECT_EMPLOYEUR_SANS_PERSONNEL_ERROR");
        } else {
            return getSession().getLabel("EMAIL_OBJECT_EMPLOYEUR_SANS_PERSONNEL");
        }
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getTypeAdresse() {
        return typeAdresse;
    }

    private void initManager(final CEEmployeurSansPersonnelManager manager) throws Exception {
        manager.setSession(getSession());
        manager.setForAnnee(getForAnnee());
        manager.find(BManager.SIZE_NOLIMIT);
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setForAnnee(final String newForAnnee) {
        forAnnee = newForAnnee;
    }

    public void setTypeAdresse(final String typeAdresse) {
        this.typeAdresse = typeAdresse;
    }
}
