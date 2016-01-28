package globaz.aquila.print.journal;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.application.COApplication;
import globaz.aquila.db.access.journal.COElementJournalBatch;
import globaz.aquila.db.access.journal.COElementJournalBatchManager;
import globaz.aquila.db.access.poursuite.COExtraitCompteManager;
import globaz.aquila.db.rdp.CORequisitionPoursuiteUtil;
import globaz.aquila.ts.opge.utils.COTSOPGEUtils;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.extrait.CAExtraitCompte;

public class COImprimerRecapitulatifJournal extends FWIAbstractManagerDocumentList {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String LABEL_AQUILA_NUM_POURSUITE = "AQUILA_NUM_POURSUITE";
    private static final String LABEL_IMPRIMER_JOURNAL_NUM_AFFILIE = "IMPRIMER_JOURNAL_NUM_AFFILIE";
    private static final String LABEL_IMPRIMER_JOURNAL_NUM_SECTION = "IMPRIMER_JOURNAL_NUM_SECTION";
    private static final String LABEL_IMPRIMER_RDP_DIFFERENCE = "IMPRIMER_RDP_DIFFERENCE";
    private static final String LABEL_IMPRIMER_RDP_IMPUTATIONS = "IMPRIMER_RDP_IMPUTATIONS";
    private static final String LABEL_IMPRIMER_RDP_MONTANT_SOUMIS = "IMPRIMER_RDP_MONTANT_SOUMIS";
    private static final String LABEL_IMPRIMER_RDP_NOM_PRENOM = "IMPRIMER_RDP_NOM_PRENOM";
    private static final String LABEL_IMPRIMER_RDP_TITRE = "IMPRIMER_RDP_TITRE";
    private static final String NUM_REF_INFOROM = "0166GCA";

    private int count = 0;

    private String forIdJournal;
    private FWCurrency montantCreanceTotal = new FWCurrency();

    private FWCurrency montantImputationsTotal = new FWCurrency();

    public COImprimerRecapitulatifJournal() throws Exception {
        super(new BSession(ICOApplication.DEFAULT_APPLICATION_AQUILA), COApplication.APPLICATION_AQUILA_PREFIX,
                "GLOBAZ", "ListeRequisitionsPoursuiteEnvoyees", new COElementJournalBatchManager(),
                ICOApplication.DEFAULT_APPLICATION_AQUILA);
    }

    public COImprimerRecapitulatifJournal(BSession session) {
        super(session, COApplication.APPLICATION_AQUILA_PREFIX, "GLOBAZ", session
                .getLabel(COImprimerRecapitulatifJournal.LABEL_IMPRIMER_RDP_TITRE), new COElementJournalBatchManager(),
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

        super.getDocumentInfo().setDocumentTypeNumber(COImprimerRecapitulatifJournal.NUM_REF_INFOROM);

        _setDocumentTitle(getSession().getLabel(COImprimerRecapitulatifJournal.LABEL_IMPRIMER_RDP_TITRE));

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
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    @Override
    protected void addRow(BEntity entity) throws FWIException {
        COElementJournalBatch element = (COElementJournalBatch) entity;

        _addCell(element.getContentieux().getCompteAnnexe().getDescription().toUpperCase());
        _addCell(element.getContentieux().getCompteAnnexe().getIdExterneRole());
        _addCell(element.getContentieux().getSection().getIdExterne());
        _addCell(element.getContentieux().getSection().getNumeroPoursuite());
        try {
            String[] soldeInitial = CORequisitionPoursuiteUtil.getSoldeSectionInitial(getSession(), element
                    .getContentieux().getIdSection());
            String forNotIdJournalCreance = soldeInitial[1];
            String ecrituresFromDate = soldeInitial[2];

            FWCurrency montant = new FWCurrency(soldeInitial[0]);
            montant.add(COTSOPGEUtils.getMontantCumuleTaxe(getSession(), getTransaction(), element.getContentieux(),
                    forNotIdJournalCreance, ecrituresFromDate));

            montantCreanceTotal.add(montant);

            _addCell(JANumberFormatter.format(montant.toString(), 0, 2, JANumberFormatter.NEAR));

            FWCurrency montantImputations = getMontantImputations(element, forNotIdJournalCreance, ecrituresFromDate);
            _addCell(JANumberFormatter.format(montantImputations.toString(), 0, 2, JANumberFormatter.NEAR));
            montantImputationsTotal.add(montantImputations);

            montant.add(montantImputations);
            _addCell(JANumberFormatter.format(montant.toString(), 0, 2, JANumberFormatter.NEAR));

        } catch (Exception e) {
            _addCell("");
        }

        count++;
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    /**
     * Return le montant des imputations.
     * 
     * @param element
     * @param forNotIdJournalCreance
     * @param ecrituresFromDate
     * @return
     * @throws Exception
     */
    private FWCurrency getMontantImputations(COElementJournalBatch element, String forNotIdJournalCreance,
            String ecrituresFromDate) throws Exception {
        COExtraitCompteManager extraitCompteManager = COTSOPGEUtils.getExtraitCompteManager(getSession(),
                element.getContentieux(), forNotIdJournalCreance, ecrituresFromDate);

        extraitCompteManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        FWCurrency montantImputations = new FWCurrency();
        for (int i = 0; i < extraitCompteManager.size(); ++i) {
            CAExtraitCompte extraitCompte = (CAExtraitCompte) extraitCompteManager.getEntity(i);

            FWCurrency tmp = new FWCurrency(extraitCompte.getMontant());

            if (tmp.isNegative() && !tmp.isZero() && JadeStringUtil.isIntegerEmpty(extraitCompte.getProvenancePmt())) {
                montantImputations.add(tmp);
            }
        }
        return montantImputations;
    }

    @Override
    protected void initializeTable() {

        this._addColumnLeft(getSession().getLabel(COImprimerRecapitulatifJournal.LABEL_IMPRIMER_RDP_NOM_PRENOM), 25);
        this._addColumnLeft(getSession().getLabel(COImprimerRecapitulatifJournal.LABEL_IMPRIMER_JOURNAL_NUM_AFFILIE), 5);
        this._addColumnLeft(getSession().getLabel(COImprimerRecapitulatifJournal.LABEL_IMPRIMER_JOURNAL_NUM_SECTION), 5);
        this._addColumnLeft(getSession().getLabel(COImprimerRecapitulatifJournal.LABEL_AQUILA_NUM_POURSUITE), 5);
        this._addColumnRight(getSession().getLabel(COImprimerRecapitulatifJournal.LABEL_IMPRIMER_RDP_MONTANT_SOUMIS), 4);
        this._addColumnRight(getSession().getLabel(COImprimerRecapitulatifJournal.LABEL_IMPRIMER_RDP_IMPUTATIONS), 8);
        this._addColumnRight(getSession().getLabel(COImprimerRecapitulatifJournal.LABEL_IMPRIMER_RDP_DIFFERENCE), 4);

    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setForIdJournal(String forIdJournal) {
        this.forIdJournal = forIdJournal;
    }

    /**
     * @see globaz.framework.printing.FWAbstractManagerDocumentList#summary()
     */
    @Override
    protected void summary() throws FWIException {
        _addCell(getSession().getLabel("IMPRIMER_RDP_NB_POURSUITES") + " : " + count);
        _addCell("");
        _addCell("");
        _addCell(getSession().getLabel("TOTAL"));
        _addCell(JANumberFormatter.format(montantCreanceTotal.toString(), 0, 2, JANumberFormatter.NEAR));
        _addCell(JANumberFormatter.format(montantImputationsTotal.toString(), 0, 2, JANumberFormatter.NEAR));

        montantCreanceTotal.add(montantImputationsTotal);
        _addCell(JANumberFormatter.format(montantCreanceTotal.toString(), 0, 2, JANumberFormatter.NEAR));
    }

}
