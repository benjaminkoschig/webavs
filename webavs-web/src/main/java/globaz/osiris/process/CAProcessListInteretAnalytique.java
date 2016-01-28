package globaz.osiris.process;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.db.interet.analytique.CAInteretAnalytique;
import globaz.osiris.db.interet.analytique.CAInteretAnalytiqueManager;
import globaz.osiris.db.interet.analytique.CAInteretManuelAnalytiqueManager;
import globaz.osiris.db.interet.analytique.CAInteretTardifEt25PourCentAnalytiqueManager;
import globaz.osiris.print.list.CAListInteretAnalytique;
import java.util.HashMap;

public class CAProcessListInteretAnalytique extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String TYPE_MANUEL = "manuel";
    private static final String TYPE_TARDIF_AND_25_POURCENT = "tardif_et_25";

    private String forDateDebut;
    private String forDateFin;

    @Override
    protected void _executeCleanUp() {
        // Not used
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            CAListInteretAnalytique excelDoc = new CAListInteretAnalytique(getSession(), this);
            excelDoc.setForDateDebut(forDateDebut);
            excelDoc.setForDateFin(forDateFin);
            excelDoc.setDocumentInfo(createDocumentInfo());
            excelDoc.initListe();

            populateSheet(excelDoc, CAProcessListInteretAnalytique.TYPE_MANUEL);

            if (isAborted()) {
                return false;
            }

            populateSheet(excelDoc, CAProcessListInteretAnalytique.TYPE_TARDIF_AND_25_POURCENT);

            if (isAborted()) {
                return false;
            }

            if (!isAborted()) {
                this.registerAttachedDocument(excelDoc.getDocumentInfo(), excelDoc.getOutputFile());
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return false;
        }
    }

    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("IM_ANALYTIQUE_SUJETMAIL_KO");
        } else {
            return getSession().getLabel("IM_ANALYTIQUE_SUJETMAIL_OK");
        }
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    /**
     * Return le manager de recherche des intérêts analytiques.
     * 
     * @param type
     * @return
     */
    private CAInteretAnalytiqueManager getManagerInteretAnalytique(String type) {
        CAInteretAnalytiqueManager manager = null;

        if (CAProcessListInteretAnalytique.TYPE_TARDIF_AND_25_POURCENT.equals(type)) {
            manager = new CAInteretTardifEt25PourCentAnalytiqueManager();
        } else if (CAProcessListInteretAnalytique.TYPE_MANUEL.equals(type)) {
            manager = new CAInteretManuelAnalytiqueManager();
        }

        manager.setSession(getSession());

        manager.setForDateDebut(getForDateDebut());
        manager.setForDateFin(getForDateFin());

        return manager;
    }

    /**
     * Return la liste des montants soumis des sections analysées.
     * 
     * @param type
     * @return
     * @throws Exception
     */
    private HashMap getMontantSoumisInteretAnalytique(String type) throws Exception {
        HashMap montantSoumis = new HashMap();
        CAInteretAnalytiqueManager managerSoumis = getManagerInteretAnalytique(type);
        managerSoumis.setSumMontantSoumis(true);

        managerSoumis.find(getTransaction(), BManager.SIZE_NOLIMIT);

        for (int i = 0; i < managerSoumis.size(); i++) {
            CAInteretAnalytique interetAnalytique = (CAInteretAnalytique) managerSoumis.get(i);

            montantSoumis.put(interetAnalytique.getIdSection(), interetAnalytique.getMontant());
        }
        return montantSoumis;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Execute le manager et rempli le tableau avec les informations d'intérêts analytiques trouvées.
     * 
     * @param excelDoc
     * @param type
     * @throws Exception
     */
    private void populateSheet(CAListInteretAnalytique excelDoc, String type) throws Exception {
        HashMap montantSoumis = getMontantSoumisInteretAnalytique(type);

        CAInteretAnalytiqueManager manager = getManagerInteretAnalytique(type);

        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        if (isAborted()) {
            return;
        }

        setProgressScaleValue(manager.size());

        excelDoc.populateSheetListe(manager, montantSoumis, getTransaction());
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

}
