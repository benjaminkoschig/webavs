package globaz.hercule.process.controleEmployeur;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.hercule.application.CEApplication;
import globaz.hercule.mappingXmlml.CEXmlmlMappingDsNonRemise;
import globaz.hercule.utils.CEExcelmlUtils;
import globaz.hercule.utils.CEUtils;
import globaz.hercule.utils.HerculeContainer;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.leo.constantes.ILEConstantes;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.pyxis.db.tiers.TIRole;

/**
 * Processus d'impression des DS non remises.
 */
public class CEDsNonRemiseProcess extends BProcess {

    private static final long serialVersionUID = -7605539066350475760L;
    public final static String MODEL_NAME = "dsNonRemise.xml";
    public final static String MODEL_REINJECTION_NAME = "dsNonRemiseReinjection.xml";
    public final static String NUMERO_INFOROM = "0249CCE";

    private String annee;
    private boolean aucunControle = false;
    private String typeAdresse = "";

    /**
     * Constructeur de CEDsNonRemiseProcess
     */
    public CEDsNonRemiseProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {

        boolean status = true;

        try {

            // Création des critères de provenance
            LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                    CEApplication.DEFAULT_APPLICATION_HERCULE);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, getAnnee());

            //
            LUJournalListViewBean manager = new LUJournalListViewBean();
            manager.setISession(getISession());
            manager.setProvenance(provenanceCriteres);
            manager.setForCsTypeCodeSysteme(ILEConstantes.CS_DEF_FORMULE_GROUPE);
            manager.setForValeurCodeSysteme(ILEConstantes.CS_DS_ST_COMTROLE_EMPLOYEUR);
            manager.setForDateReception("0");
            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            if (manager.size() > 0) {
                setProgressScaleValue(manager.size());
                HerculeContainer container = CEXmlmlMappingDsNonRemise.loadResults(manager, this);

                if (isAborted()) {
                    return false;
                }

                // On génère le doc
                String nomDoc = getSession().getLabel("LISTE_DS_NON_REMISE_NOM_DOCUMENT");
                String docPath = CEExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                        + CEDsNonRemiseProcess.MODEL_NAME, nomDoc, container);

                // Publication du document
                JadePublishDocumentInfo docInfo = createDocumentInfo();
                docInfo.setApplicationDomain(CEApplication.DEFAULT_APPLICATION_HERCULE);
                docInfo.setDocumentTitle(nomDoc);
                docInfo.setPublishDocument(true);
                docInfo.setArchiveDocument(false);
                docInfo.setDocumentTypeNumber(CEDsNonRemiseProcess.NUMERO_INFOROM);
                this.registerAttachedDocument(docInfo, docPath);

            } else {
                setAucunControle(true);
            }

        } catch (Exception e) {
            this._addError(getTransaction(), getSession().getLabel("EXECUTION_LISTE_DS_NON_REMISE_ERROR"));

            String messageInformation = "Annee : " + getAnnee() + "\n";
            messageInformation += CEUtils.stack2string(e);

            CEUtils.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

            status = false;
        }

        return status;
    }

    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getAnnee())) {
            getSession().addError(getSession().getLabel("ANNEE_OBLIGATOIRE"));
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    public String getAnnee() {
        return annee;
    }

    @Override
    protected String getEMailObject() {
        if (isAucunControle()) {
            return getSession().getLabel("LISTE_DS_NON_REMISE_AUCUN");
        }
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("LISTE_DS_NON_REMISE_ERROR");
        } else {
            return getSession().getLabel("LISTE_DS_NON_REMISE");
        }
    }

    public String getTypeAdresse() {
        return typeAdresse;
    }

    public boolean isAucunControle() {
        return aucunControle;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setAnnee(final String annee) {
        this.annee = annee;
    }

    public void setAucunControle(final boolean aucunControle) {
        this.aucunControle = aucunControle;
    }

    public void setTypeAdresse(final String typeAdresse) {
        this.typeAdresse = typeAdresse;
    }
}
