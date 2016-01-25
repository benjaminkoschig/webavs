package globaz.hercule.process.controleEmployeur;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.controleEmployeur.CEControles5PourCentManager;
import globaz.hercule.db.controleEmployeur.CEControlesExtraOrdinairesEffectuesManager;
import globaz.hercule.mappingXmlml.CEXmlmlMappingControles5Pourcent;
import globaz.hercule.utils.CEExcelmlUtils;
import globaz.hercule.utils.CEUtils;
import globaz.hercule.utils.HerculeContainer;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;

/**
 * @author jpa
 * @revision SCO 9 déc. 2010
 */
public class CEControles5PourCentProcess extends BProcess {

    private static final long serialVersionUID = 399667172700392967L;
    public final static String MODEL_NAME = "pourcent.xml";
    public static final String NUM_INFOROM = "0242CCE";

    private String annee = "";
    private boolean aucunControle = false;
    private String genreControle = "";
    private String typeAdresse = "";

    /**
     * Constructeur de CEControles5PourCentProcess
     */
    public CEControles5PourCentProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {

        boolean status = true;
        int nombreControlesExtraordinaires = 0;
        int nombreControles5PourCent = 0;

        try {
            int nombre5PourCent = CEUtils.getNombre5PourCent(getTransaction(), getAnnee(), getSession());

            // Récupération des controles extraordinaires
            CEControlesExtraOrdinairesEffectuesManager managerExtraOrdinaires = new CEControlesExtraOrdinairesEffectuesManager();
            managerExtraOrdinaires.setSession(getSession());
            managerExtraOrdinaires.setForAnnee(getAnnee());
            managerExtraOrdinaires.find(getTransaction(), BManager.SIZE_NOLIMIT);

            nombreControlesExtraordinaires = managerExtraOrdinaires.getCount();

            // Récupération des controles 5%
            CEControles5PourCentManager manager5PourCent = new CEControles5PourCentManager();
            manager5PourCent.setSession(getSession());
            manager5PourCent.setForAnnee(getAnnee());
            manager5PourCent.find(getTransaction(), BManager.SIZE_NOLIMIT);

            nombreControles5PourCent = manager5PourCent.getCount();

            // Si on a au moins un controle, on crée le document
            if ((nombreControlesExtraordinaires != 0) || (nombreControles5PourCent != 0)) {
                setProgressScaleValue(nombreControles5PourCent + nombreControlesExtraordinaires);
                HerculeContainer container = CEXmlmlMappingControles5Pourcent.loadResults(manager5PourCent,
                        managerExtraOrdinaires, nombre5PourCent, nombreControles5PourCent,
                        nombreControlesExtraordinaires, this);

                if (isAborted()) {
                    return false;
                }

                // On génère le doc
                String nomDoc = getSession().getLabel("LISTE_CONTROLE_5_POURCENT");
                String docPath = CEExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                        + CEControles5PourCentProcess.MODEL_NAME, nomDoc, container);

                // Publication du document
                JadePublishDocumentInfo docInfo = createDocumentInfo();
                docInfo.setApplicationDomain(CEApplication.DEFAULT_APPLICATION_HERCULE);
                docInfo.setDocumentTitle(nomDoc);
                docInfo.setDocumentTypeNumber(CEControles5PourCentProcess.NUM_INFOROM);
                docInfo.setPublishDocument(true);
                docInfo.setArchiveDocument(false);
                this.registerAttachedDocument(docInfo, docPath);
            } else {
                setAucunControle(true);
            }

        } catch (Exception e) {

            this._addError(getTransaction(), getSession().getLabel("EXECUTION_LISTE_CONTROLE_5_POURCENT"));

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
            return getSession().getLabel("LISTE_5_POURCENT_AUCUN");
        }
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("LISTE_5_POURCENT_ERROR");
        } else {
            return getSession().getLabel("LISTE_5_POURCENT");
        }
    }

    public String getGenreControle() {
        return genreControle;
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

    public void setGenreControle(final String string) {
        genreControle = string;
    }

    public void setTypeAdresse(final String typeAdresse) {
        this.typeAdresse = typeAdresse;
    }
}
