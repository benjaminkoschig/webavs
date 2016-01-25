package globaz.hercule.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.controleEmployeur.CEControlesAttribuesManager;
import globaz.hercule.mappingXmlml.CEXmlmlMappingControlesAAttribuer;
import globaz.hercule.utils.CEExcelmlUtils;
import globaz.hercule.utils.CEUtils;
import globaz.hercule.utils.HerculeContainer;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;

/**
 * @author hpe
 * @since Créé le 14 févr. 07
 * @revision SCO 24 nov. 2010
 */
public class CEListeControlesAttribuesProcess extends BProcess implements FWViewBeanInterface {

    private static final long serialVersionUID = -8116910535335812789L;
    public final static String MODEL_NAME = "attribution.xml";
    public final static String NUMERO_INFOROM = "0243CCE";

    private Boolean aEffectuer = new Boolean(false);
    private String annee = new String();
    private boolean aucunControle = false;
    private Boolean dejaEffectuer = new Boolean(false);
    private String genreControle = new String();
    private String selectionGroupe = new String();
    private Boolean tousLesControles = new Boolean(false);
    private String typeAdresse = "";
    private String visaReviseur = new String();

    public CEListeControlesAttribuesProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {

        boolean status = true;

        try {

            // Création du manager
            CEControlesAttribuesManager manager = new CEControlesAttribuesManager();
            manager.setSession(getSession());

            manager.setForAnnee(getAnnee());
            manager.setForGenreControle(getGenreControle());
            manager.setForVisaReviseur(getVisaReviseur());
            manager.setAEffectuer(getAEffectuer());
            manager.setDejaEffectuer(getDejaEffectuer());
            manager.setTousLesControles(getTousLesControles());

            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            // Création du document
            // On remplit le container par les données de la base
            if (manager.size() > 0) {
                setProgressScaleValue(manager.size());

                HerculeContainer container = CEXmlmlMappingControlesAAttribuer.loadResults(manager, this);

                if (isAborted()) {
                    return false;
                }

                // On génère le doc
                String nomDoc = getSession().getLabel("LISTE_CONTROLE_ATTRIBUES_NOM_DOCUMENT");
                String docPath = CEExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                        + CEListeControlesAttribuesProcess.MODEL_NAME, nomDoc, container);

                // Publication du document
                JadePublishDocumentInfo docInfo = createDocumentInfo();
                docInfo.setApplicationDomain(CEApplication.DEFAULT_APPLICATION_HERCULE);
                docInfo.setDocumentTitle(nomDoc);
                docInfo.setPublishDocument(true);
                docInfo.setArchiveDocument(false);
                docInfo.setDocumentTypeNumber(CEListeControlesAttribuesProcess.NUMERO_INFOROM);
                this.registerAttachedDocument(docInfo, docPath);

            } else {
                setAucunControle(true);
            }

        } catch (Exception e) {

            this._addError(getTransaction(), getSession().getLabel("EXECUTION_CONTROLES_ATTRIBUES_ERREUR"));

            String messageInformation = "Annee de rattrapage : " + getAnnee() + "\n";
            messageInformation += "GenreControle : " + getGenreControle() + "\n";
            messageInformation += "VisaReviseur : " + getVisaReviseur() + "\n";
            messageInformation += "AEffectuer : " + getAEffectuer() + "\n";
            messageInformation += "DejaEffectuer : " + getDejaEffectuer() + "\n";
            messageInformation += CEUtils.stack2string(e);

            CEUtils.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

            status = false;
        }

        return status;
    }

    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getAnnee())) {
            this._addError(getTransaction(), getSession().getLabel("LISTE_CONTROLE_ATTRIBUES_ERREUR_ANNEE_OBLIGATOIRE"));
        }
        if (getAnnee().length() != 4) {
            this._addError(getTransaction(), getSession().getLabel("LISTE_CONTROLE_ATTRIBUES_ERREUR_ANNEE_FORMAT"));
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    public Boolean getaEffectuer() {
        return aEffectuer;
    }

    public Boolean getAEffectuer() {
        return aEffectuer;
    }

    public String getAnnee() {
        return annee;
    }

    public Boolean getDejaEffectuer() {
        return dejaEffectuer;
    }

    @Override
    protected String getEMailObject() {
        if (isAucunControle()) {
            return getSession().getLabel("EMAIL_OBJECT_CONTROLES_ATTRIBUES_AUCUN");
        }
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("EMAIL_OBJECT_CONTROLES_ATTRIBUES_ERROR");
        } else {
            return getSession().getLabel("EMAIL_OBJECT_CONTROLES_ATTRIBUES");
        }
    }

    public String getGenreControle() {
        return genreControle;
    }

    public String getSelectionGroupe() {
        return selectionGroupe;
    }

    public Boolean getTousLesControles() {
        return tousLesControles;
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

    public void setaEffectuer(final Boolean aEffectuer) {
        this.aEffectuer = aEffectuer;
    }

    public void setAEffectuer(final Boolean effectuer) {
        aEffectuer = effectuer;
    }

    public void setAnnee(final String string) {
        annee = string;
    }

    public void setAucunControle(final boolean aucunControle) {
        this.aucunControle = aucunControle;
    }

    public void setDejaEffectuer(final Boolean dejaEffectuer) {
        this.dejaEffectuer = dejaEffectuer;
    }

    public void setGenreControle(final String string) {
        genreControle = string;
    }

    public void setSelectionGroupe(final String string) {
        selectionGroupe = string;
    }

    public void setTousLesControles(final Boolean tousLesControles) {
        this.tousLesControles = tousLesControles;
    }

    public void setTypeAdresse(final String typeAdresse) {
        this.typeAdresse = typeAdresse;
    }

    public void setVisaReviseur(final String string) {
        visaReviseur = string;
    }
}
