package globaz.hercule.process;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.controleEmployeur.CEControlesEffectuesManager;
import globaz.hercule.mappingXmlml.CEXmlmlMappingControlesEffectues;
import globaz.hercule.utils.CEExcelmlUtils;
import globaz.hercule.utils.CEUtils;
import globaz.hercule.utils.HerculeContainer;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;

/**
 * @author SCO
 * @since 2 déc. 2010
 */
public class CEListeControlesEffectuesProcess extends BProcess {

    private static final long serialVersionUID = 9215317510233839081L;
    public final static String MODEL_NAME = "controlesEffectues.xml";
    public final static String NOM_DOCUMENT = "LISTE_CONTROLE_REALISE_NOM_DOCUMENT";
    public final static String NUMERO_INFOROM = "0281CCE";

    private String annee;
    private boolean aucunControle = false;
    private String fromDateImpression;
    private String toDateImpression;
    private String typeAdresse;
    private String visaReviseur;

    /**
     * Constructeur de CEListeControlesEffectuesProcess
     */
    public CEListeControlesEffectuesProcess() {
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
            CEControlesEffectuesManager manager = new CEControlesEffectuesManager();
            manager.setSession(getSession());
            manager.setFromDateImpression(getFromDateImpression());
            manager.setToDateImpression(getToDateImpression());
            manager.setForAnnee(getAnnee());
            manager.setForVisaReviseur(getVisaReviseur());

            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            setProgressScaleValue(manager.size());

            // Création du document
            // On remplit le container par les données de la base
            if (manager.size() > 0) {
                HerculeContainer container = CEXmlmlMappingControlesEffectues.loadResults(manager, this);

                if (isAborted()) {
                    return false;
                }

                // On génère le doc
                String nomDoc = getSession().getLabel(CEListeControlesEffectuesProcess.NOM_DOCUMENT);
                String docPath = CEExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                        + CEListeControlesEffectuesProcess.MODEL_NAME, nomDoc, container);

                // Publication du document
                JadePublishDocumentInfo docInfo = createDocumentInfo();
                docInfo.setApplicationDomain(CEApplication.DEFAULT_APPLICATION_HERCULE);
                docInfo.setDocumentTitle(nomDoc);
                docInfo.setPublishDocument(true);
                docInfo.setArchiveDocument(false);
                docInfo.setDocumentTypeNumber(CEListeControlesEffectuesProcess.NUMERO_INFOROM);
                this.registerAttachedDocument(docInfo, docPath);

            } else {
                setAucunControle(true);
            }

        } catch (Exception e) {

            this._addError(getTransaction(), getSession().getLabel("EXECUTION_LISTE_CONTROLES_REALISE"));

            String messageInformation = "From date d'impression : " + getFromDateImpression() + "\n";
            messageInformation += "To date d'impression : " + getToDateImpression() + "\n";
            messageInformation += "Annee : " + getAnnee() + "\n";
            messageInformation += "Visa reviseur : " + getVisaReviseur() + "\n";
            messageInformation += "Type adresse : " + getTypeAdresse() + "\n";
            messageInformation += CEUtils.stack2string(e);

            CEUtils.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

            status = false;
        }

        return status;
    }

    @Override
    protected void _validate() throws Exception {

        if (JadeStringUtil.isBlankOrZero(getFromDateImpression())
                || JadeStringUtil.isBlankOrZero(getToDateImpression())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_BORNE_DATE_IMPRESSION"));
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
            return getSession().getLabel("EMAIL_OBJECT_CONTROLES_REALISE_AUCUN");
        }
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("EMAIL_OBJECT_CONTROLES_REALISE_ERROR");
        } else {
            return getSession().getLabel("EMAIL_OBJECT_CONTROLES_REALISE") + getAnnee();
        }
    }

    public String getFromDateImpression() {
        return fromDateImpression;
    }

    public String getToDateImpression() {
        return toDateImpression;
    }

    public String getTypeAdresse() {
        return typeAdresse;
    }

    public String getVisaReviseur() {
        return visaReviseur;
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

    public void setFromDateImpression(final String fromDateImpression) {
        this.fromDateImpression = fromDateImpression;
    }

    public void setToDateImpression(final String toDateImpression) {
        this.toDateImpression = toDateImpression;
    }

    public void setTypeAdresse(final String typeAdresse) {
        this.typeAdresse = typeAdresse;
    }

    public void setVisaReviseur(final String visaReviseur) {
        this.visaReviseur = visaReviseur;
    }
}
