/*
 * Créé le 14 févr. 07
 */
package globaz.naos.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.naos.db.controleEmployeur.AFControlesAttribuesManager;
import globaz.naos.itext.controleEmployeur.AFListeControlesAttribuesExcel;

/**
 * @author hpe
 * 
 */

public class AFListeControlesAttribuesProcess extends BProcess implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = new String();
    private String genreControle = new String();
    private String selectionGroupe = new String();
    private String visaReviseur = new String();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public AFListeControlesAttribuesProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {

            // Création du manager
            AFControlesAttribuesManager manager = new AFControlesAttribuesManager();
            manager.setSession(getSession());

            manager.setForAnnee(getAnnee());
            manager.setForGenreControle(getGenreControle());
            manager.setForVisaReviseur(getVisaReviseur());

            manager.find(BManager.SIZE_NOLIMIT);

            // Création du document
            AFListeControlesAttribuesExcel excelDoc = new AFListeControlesAttribuesExcel(getSession());

            excelDoc.setAnnee(getAnnee());
            excelDoc.setGenreControle(getGenreControle());
            excelDoc.setVisaReviseur(getVisaReviseur());

            excelDoc.setProcessAppelant(this);
            excelDoc.populateSheetListe(manager, getTransaction());

            this.registerAttachedDocument(excelDoc.getOutputFile());

            return true;
        } catch (Exception e) {
            abort();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    /**
     * @return
     */
    public String getAnnee() {
        return annee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("NAOS_EMAIL_OBJECT_CONTROLES_ATTRIBUES_ERROR");
        } else {
            return getSession().getLabel("NAOS_EMAIL_OBJECT_CONTROLES_ATTRIBUES");
        }
    }

    /**
     * @return
     */
    public String getGenreControle() {
        return genreControle;
    }

    /**
     * @return
     */
    public String getSelectionGroupe() {
        return selectionGroupe;
    }

    /**
     * @return
     */
    public String getVisaReviseur() {
        return visaReviseur;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * @param string
     */
    public void setAnnee(String string) {
        annee = string;
    }

    /**
     * @param string
     */
    public void setGenreControle(String string) {
        genreControle = string;
    }

    /**
     * @param string
     */
    public void setSelectionGroupe(String string) {
        selectionGroupe = string;
    }

    /**
     * @param string
     */
    public void setVisaReviseur(String string) {
        visaReviseur = string;
    }

}
