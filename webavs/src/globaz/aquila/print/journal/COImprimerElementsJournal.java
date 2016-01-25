package globaz.aquila.print.journal;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.application.COApplication;
import globaz.aquila.db.access.journal.COElementJournalBatch;
import globaz.aquila.db.access.journal.COElementJournalBatchManager;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;

public class COImprimerElementsJournal extends FWIAbstractManagerDocumentList {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String LABEL_IMPRIMER_JOURNAL_AFFILIE = "IMPRIMER_JOURNAL_AFFILIE";
    private static final String LABEL_IMPRIMER_JOURNAL_ETAT = "IMPRIMER_JOURNAL_ETAT";
    private static final String LABEL_IMPRIMER_JOURNAL_MESSAGES = "IMPRIMER_JOURNAL_MESSAGES";
    private static final String LABEL_IMPRIMER_JOURNAL_NUM_AFFILIE = "IMPRIMER_JOURNAL_NUM_AFFILIE";
    // private static final String LABEL_IMPRIMER_JOURNAL_ADRESSE =
    // "IMPRIMER_JOURNAL_ADRESSE";
    // private static final String LABEL_IMPRIMER_JOURNAL_ADRESSE_COMPLEMENTAIRE
    // = "IMPRIMER_JOURNAL_ADRESSE_COMPLEMENTAIRE";
    private static final String LABEL_IMPRIMER_JOURNAL_NUM_SECTION = "IMPRIMER_JOURNAL_NUM_SECTION";
    private static final String LABEL_IMPRIMER_JOURNAL_TITRE = "IMPRIMER_JOURNAL_TITRE";
    private static final String NUM_REF_INFOROM = "0165GCA";

    private String forIdJournal;

    public COImprimerElementsJournal() throws Exception {
        super(new BSession(ICOApplication.DEFAULT_APPLICATION_AQUILA), COApplication.APPLICATION_AQUILA_PREFIX,
                "GLOBAZ", "ListeElementsJournal", new COElementJournalBatchManager(),
                ICOApplication.DEFAULT_APPLICATION_AQUILA);
    }

    public COImprimerElementsJournal(BSession session) {
        super(session, COApplication.APPLICATION_AQUILA_PREFIX, "GLOBAZ", session
                .getLabel(COImprimerElementsJournal.LABEL_IMPRIMER_JOURNAL_TITRE), new COElementJournalBatchManager(),
                ICOApplication.DEFAULT_APPLICATION_AQUILA);
    }

    /**
     * @see globaz.framework.printing.FWAbstractDocumentList#_beforeExecuteReport()
     */
    @Override
    public void _beforeExecuteReport() {
        COElementJournalBatchManager manager = (COElementJournalBatchManager) _getManager();
        manager.setSession(getSession());
        manager.setForIdJournal(getForIdJournal());

        super.getDocumentInfo().setDocumentTypeNumber(COImprimerElementsJournal.NUM_REF_INFOROM);

        _setDocumentTitle(getSession().getLabel(COImprimerElementsJournal.LABEL_IMPRIMER_JOURNAL_TITRE));
        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    @Override
    protected void addRow(BEntity entity) throws FWIException {
        COElementJournalBatch element = (COElementJournalBatch) entity;
        _addCell(element.getContentieux().getCompteAnnexe().getIdExterneRole());
        _addCell(element.getContentieux().getCompteAnnexe().getDescription().toUpperCase());
        _addCell(element.getContentieux().getSection().getIdExterne());
        _addCell(element.getEtatLibelle());
        _addCell(element.getErrorMessages());
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    @Override
    protected void initializeTable() {
        this._addColumnLeft(getSession().getLabel(COImprimerElementsJournal.LABEL_IMPRIMER_JOURNAL_NUM_AFFILIE), 7);
        this._addColumnLeft(getSession().getLabel(COImprimerElementsJournal.LABEL_IMPRIMER_JOURNAL_AFFILIE), 27);
        this._addColumnLeft(getSession().getLabel(COImprimerElementsJournal.LABEL_IMPRIMER_JOURNAL_NUM_SECTION), 9);
        this._addColumnLeft(getSession().getLabel(COImprimerElementsJournal.LABEL_IMPRIMER_JOURNAL_ETAT), 7);
        this._addColumnLeft(getSession().getLabel(COImprimerElementsJournal.LABEL_IMPRIMER_JOURNAL_MESSAGES), 50);
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setForIdJournal(String forIdJournal) {
        this.forIdJournal = forIdJournal;
    }

}
