package globaz.corvus.process;

import globaz.corvus.db.annonces.REAnnoncesGroupePrestationsManager;
import globaz.corvus.excel.REListeExcelAnnonces;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;

public class REGenererListeExcelAnnoncesProcess extends BProcess {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String mois = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public REGenererListeExcelAnnoncesProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            // selection des annonces
            REAnnoncesGroupePrestationsManager manager = new REAnnoncesGroupePrestationsManager();
            manager.setSession(getSession());
            manager.setForMoisRapport(getMois());

            // Création du document
            REListeExcelAnnonces excelDoc = new REListeExcelAnnonces(getSession());
            excelDoc.setMois(getMois());
            excelDoc.populateSheetListe(manager, getTransaction());

            // attachement du fichier de sortie au mail
            registerAttachedDocument(excelDoc.getOutputFile());

            return true;

        } catch (Exception e) {
            _addError(getTransaction(), e.getMessage());
            e.getMessage();
            return false;
        }
    }

    @Override
    protected void _validate() throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("EMAIL_OBJECT_GENERATION_LISTE_EXCEL_ANNONCES_ERREUR");
        } else {
            return getSession().getLabel("EMAIL_OBJECT_GENERATION_LISTE_EXCEL_ANNONCES_SUCCES");
        }
    }

    public String getMois() {
        return mois;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }
}
