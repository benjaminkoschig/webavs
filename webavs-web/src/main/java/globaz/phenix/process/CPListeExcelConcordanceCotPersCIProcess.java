package globaz.phenix.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.naos.translation.CodeSystem;
import globaz.phenix.db.principale.CPDecisionForCompareCIManager;
import globaz.phenix.listes.excel.CPExcelmlUtils;
import globaz.phenix.listes.excel.CPXmlmlMappingListeConcordanceCICotPers;
import globaz.webavs.common.CommonExcelmlContainer;

public class CPListeExcelConcordanceCotPersCIProcess extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Constantes
    public final static String MODEL_NAME = "listeConcordanceCotPersCI.xml";
    public final static String MODEL_REINJECTION_NAME = "listeConcordanceCotPersCIReinjection.xml";
    public final static String NUMERO_INFOROM = "0120CCP";

    private String forAnnee = "";
    private String fromDiffAdmise = "";

    private String fromNoAffilie = "";

    private String tillNoAffilie = "";

    private String toDiffAdmise = "";

    // Constructeur
    public CPListeExcelConcordanceCotPersCIProcess() {
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

        CPDecisionForCompareCIManager manager = new CPDecisionForCompareCIManager();

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
            getSession().addError(getSession().getLabel("LISTE_CONCORDANCE_COTPERD_CI_ERREUR_EMAIL_OBLIGATOIRE"));
            setSendCompletionMail(false);
            setSendMailOnError(false);
        }
    }

    private boolean createDocument(CPDecisionForCompareCIManager manager) throws Exception, Exception {
        setProgressScaleValue(manager.size());
        JadePublishDocumentInfo docInfo = createDocumentInfo();

        CommonExcelmlContainer container = CPXmlmlMappingListeConcordanceCICotPers.loadResults(manager, this, docInfo);

        if (isAborted()) {
            return false;
        }
        String nomDoc = (getSession().getLabel("LISTE_CONCORDANCE_COTPERD_CI") + " " + getForAnnee());

        String docPath = "";
        if (CodeSystem.LANGUE_ALLEMAND.equals(getSession().getIdLangueISO())) {
            docPath = CPExcelmlUtils.createDocumentExcel("DE" + "/"
                    + CPListeExcelConcordanceCotPersCIProcess.MODEL_NAME, nomDoc, container);
        } else {
            docPath = CPExcelmlUtils.createDocumentExcel("FR" + "/"
                    + CPListeExcelConcordanceCotPersCIProcess.MODEL_NAME, nomDoc, container);
        }
        // Publication du document

        docInfo.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(CPListeExcelConcordanceCotPersCIProcess.NUMERO_INFOROM);
        this.registerAttachedDocument(docInfo, docPath);

        return true;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */

    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("SUJET_EMAIL_LISTE_CI_CP_PASOK");
        } else {
            return getSession().getLabel("SUJET_EMAIL_LISTE_CI_CP_OK");
        }
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getFromDiffAdmise() {
        return fromDiffAdmise;
    }

    public String getFromNoAffilie() {
        return fromNoAffilie;
    }

    public String getTillNoAffilie() {
        return tillNoAffilie;
    }

    public String getToDiffAdmise() {
        return toDiffAdmise;
    }

    private void initManager(CPDecisionForCompareCIManager manager) throws Exception {
        manager.setSession(getSession());
        manager.setForAnneeCI(getForAnnee());
        manager.setForAnneeDecision(getForAnnee());
        manager.setFromNoAffilie(getFromNoAffilie());
        manager.setTillNoAffilie(getTillNoAffilie());
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

    public void setFromDiffAdmise(String fromDiffAdmise) {
        this.fromDiffAdmise = fromDiffAdmise;
    }

    public void setFromNoAffilie(String fromNoAffilie) {
        this.fromNoAffilie = fromNoAffilie;
    }

    public void setTillNoAffilie(String tillNoAffilie) {
        this.tillNoAffilie = tillNoAffilie;
    }

    public void setToDiffAdmise(String toDiffAdmise) {
        this.toDiffAdmise = toDiffAdmise;
    }
}
