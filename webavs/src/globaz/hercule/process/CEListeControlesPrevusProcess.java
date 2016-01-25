package globaz.hercule.process;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.controleEmployeur.CEControlesPrevusManager;
import globaz.hercule.mappingXmlml.CEXmlmlMappingControlesPrevus;
import globaz.hercule.utils.CEExcelmlUtils;
import globaz.hercule.utils.CEUtils;
import globaz.hercule.utils.HerculeContainer;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;

/**
 * @author SCO
 * @since 11 oct. 2010
 */
public class CEListeControlesPrevusProcess extends BProcess {

    private static final long serialVersionUID = 1964648904202679918L;
    public final static String MODEL_NAME = "controlesPrevus.xml";
    public final static String MODEL_REINJECTION_NAME = "controlesPrevusReinjection.xml";
    public final static String NOM_DOCUMENT = "LISTE_CONTROLE_PREVUS_NOM_DOCUMENT";
    public final static String NUMERO_INFOROM = "0253CCE";

    private String annee;
    private boolean aucunControle = false;
    private String typeAdresse;

    /**
     * Constructeur de CEListeControlePrevusProcess
     */
    public CEListeControlesPrevusProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        boolean status = true;

        try {

            // Création du manager
            CEControlesPrevusManager manager = new CEControlesPrevusManager();
            manager.setSession(getSession());
            manager.setForAnnee(getAnnee());

            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            setProgressScaleValue(manager.size());

            // Création du document
            // On remplit le container par les données de la base
            if (manager.size() > 0) {
                HerculeContainer container = CEXmlmlMappingControlesPrevus.loadResults(manager, this);

                if (isAborted()) {
                    return false;
                }

                // On génère le doc
                String nomDoc = getSession().getLabel(CEListeControlesPrevusProcess.NOM_DOCUMENT);
                String docPath = CEExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                        + CEListeControlesPrevusProcess.MODEL_NAME, nomDoc, container);

                // Publication du document
                JadePublishDocumentInfo docInfo = createDocumentInfo();
                docInfo.setApplicationDomain(CEApplication.DEFAULT_APPLICATION_HERCULE);
                docInfo.setDocumentTitle(nomDoc);
                docInfo.setPublishDocument(true);
                docInfo.setArchiveDocument(false);
                docInfo.setDocumentTypeNumber(CEListeControlesPrevusProcess.NUMERO_INFOROM);
                this.registerAttachedDocument(docInfo, docPath);
            } else {
                setAucunControle(true);
            }

        } catch (Exception e) {

            this._addError(getTransaction(), getSession().getLabel("EXECUTION_LISTE_CONTROLES_PREVUS"));

            String messageInformation = "Annee de la liste : " + getAnnee() + "\n";
            messageInformation += CEUtils.stack2string(e);

            CEUtils.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

            status = false;
        }

        return status;
    }

    @Override
    protected void _validate() throws Exception {

        if (JadeStringUtil.isEmpty(getAnnee())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_ANNEE"));
        }

        if (!CEUtils.validateYear(getAnnee())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_ANNEE_ERREUR"));
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
            return getSession().getLabel("EMAIL_OBJECT_CONTROLES_PREVUS_AUCUN");
        }
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("EMAIL_OBJECT_CONTROLES_PREVUS_ERROR");
        } else {
            return getSession().getLabel("EMAIL_OBJECT_CONTROLES_PREVUS");
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
