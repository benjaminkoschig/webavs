/*
 * Globaz SA.
 */
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
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.webavs.common.CommonExcelmlContainer;

/**
 * @author jpa
 * @revision SCO 9 déc. 2010
 */
public class CEControles5PourCentProcess extends BProcess {

    private static final long serialVersionUID = 399667172700392967L;
    public static final String MODEL_NAME = "pourcent.xml";
    public static final String NUM_INFOROM = "0242CCE";

    private String annee;
    private boolean aucunControle;
    private String genreControle;
    private String typeAdresse;

    public CEControles5PourCentProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
        // Nothing
    }

    @Override
    protected boolean _executeProcess() {

        boolean status = true;
        int nombreControlesExtraordinaires = 0;
        int nombreControles5PourCent = 0;

        try {

            // Recherche du nombre de contrôle à effectuer
            int nombre5PourCent = CEUtils.getNombre5PourCent(getTransaction(), getAnnee(), getSession());
            int nombreControleNCC = CEUtils.getNombreControleNCCCat0Et1(getTransaction(), getAnnee(), getSession());

            int nombreAControler = nombre5PourCent - nombreControleNCC;

            // Récupération des controles extraordinaires
            CEControlesExtraOrdinairesEffectuesManager mngExOr = new CEControlesExtraOrdinairesEffectuesManager();
            mngExOr.setSession(getSession());
            mngExOr.setForAnnee(getAnnee());
            mngExOr.find(getTransaction(), BManager.SIZE_NOLIMIT);

            nombreControlesExtraordinaires = mngExOr.getCount();

            // Récupération des controles 5%
            CEControles5PourCentManager manager5PourCent = new CEControles5PourCentManager();
            manager5PourCent.setSession(getSession());
            manager5PourCent.setForAnnee(getAnnee());
            manager5PourCent.find(getTransaction(), BManager.SIZE_NOLIMIT);

            nombreControles5PourCent = manager5PourCent.getCount();

            // Si on a au moins un controle, on crée le document
            if ((nombreControlesExtraordinaires != 0) || (nombreControles5PourCent != 0)) {
                setProgressScaleValue((long) nombreControles5PourCent + nombreControlesExtraordinaires);
                CommonExcelmlContainer container = CEXmlmlMappingControles5Pourcent.loadResults(manager5PourCent,
                        mngExOr, nombreAControler, nombreControles5PourCent, nombreControlesExtraordinaires,
                        nombreControleNCC, this);

                if (isAborted()) {
                    return false;
                }

                // On génère le doc
                String nomDoc = getSession().getLabel("LISTE_CONTROLE_5_POURCENT");
                String nomModel = getSession().getIdLangueISO().toUpperCase() + "/"
                        + CEControles5PourCentProcess.MODEL_NAME;
                String docPath = CEExcelmlUtils.createDocumentExcel(nomModel, nomDoc, container);

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
