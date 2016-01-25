package globaz.osiris.print.list;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.cumulcotisations.CACumulCotisationsParAnnee;
import globaz.osiris.db.cumulcotisations.CACumulCotisationsParAnneeManager;

/**
 * @author dda
 */
public class CAListCumulCotisationsParAnnee extends FWIAbstractManagerDocumentList {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String NUMERO_REFERENCE_INFOROM = "0202GCA";

    private String fromDateValeur;
    private String fromIdExterne;
    private boolean printFirstPageInfos = true;
    private FWCurrency sumMasseTotal = new FWCurrency();
    private FWCurrency sumMontantTotal = new FWCurrency();
    private String toDateValeur;
    private String toIdExterne;
    private String typeImpression = "pdf";

    public CAListCumulCotisationsParAnnee() throws Exception {
        // session, prefix, Compagnie, Titre, manager, application
        super(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS), "CA", "GLOBAZ",
                "Cumul des cotisations par année", new CACumulCotisationsParAnneeManager(),
                CAApplication.DEFAULT_APPLICATION_OSIRIS);
    }

    public CAListCumulCotisationsParAnnee(BSession session) {
        // session, prefix, Compagnie, Titre, manager, application
        super(session, "CA", "GLOBAZ", "Cumul des cotisations par année", new CACumulCotisationsParAnneeManager(),
                CAApplication.DEFAULT_APPLICATION_OSIRIS);
    }

    /**
     * @see globaz.framework.printing.FWAbstractDocumentList#_beforeExecuteReport()
     */
    @Override
    public void _beforeExecuteReport() {
        CACumulCotisationsParAnneeManager manager = (CACumulCotisationsParAnneeManager) _getManager();
        manager.setSession(getSession());

        manager.setFromIdExterne(getFromIdExterne());
        manager.setToIdExterne(getToIdExterne());

        manager.setFromDateValeur(JACalendar.format(getFromDateValeur(), JACalendar.FORMAT_YYYYMMDD));
        manager.setToDateValeur(JACalendar.format(getToDateValeur(), JACalendar.FORMAT_YYYYMMDD));

        _setDocumentTitle(getSession().getLabel("LIST_CUMUL_COTISATIONS_PAR_ANNEE"));
        getDocumentInfo().setTemplateName("");
        // Numéro de référence Inforom.
        getDocumentInfo().setDocumentTypeNumber(CAListCumulCotisationsParAnnee.NUMERO_REFERENCE_INFOROM);
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

    /**
     * Ajoute les informations de header sur la première page.
     * 
     * @throws FWIException
     */
    private void addFirstPageInfos() throws FWIException {
        if (!_getReport().isOpen()) {
            _getReport().open();
        }

        if (!JadeStringUtil.isBlank(getFromDateValeur()) && !JadeStringUtil.isBlank(getToDateValeur())) {
            this._addLine(getFontCell(), getSession().getLabel("DATEVALEUR") + " : " + getFromDateValeur() + " - "
                    + getToDateValeur(), null, null, null, null);
        }

        if (!JadeStringUtil.isBlank(getFromIdExterne()) && !JadeStringUtil.isBlank(getToIdExterne())) {
            this._addLine(getFontCell(), getSession().getLabel("RUBRIQUE") + " : " + getFromIdExterne() + " - "
                    + getToIdExterne(), null, null, null, null);
        }

        this._addLine(getFontCell(), "", null, null, null, null);
    }

    /**
     * @see globaz.framework.printing.FWAbstractManagerDocumentList#addRow(globaz.globall.db.BEntity)
     */
    @Override
    protected void addRow(BEntity entity) throws FWIException {
        if (printFirstPageInfos) {
            addFirstPageInfos();
            printFirstPageInfos = false;
        }

        CACumulCotisationsParAnnee cumulCotisations = (CACumulCotisationsParAnnee) entity;

        _addCell(cumulCotisations.getIdExterne());
        _addCell(cumulCotisations.getAnneeCotisation());

        _addCell(JANumberFormatter.format(cumulCotisations.getSumMontant(), 0, 2, JANumberFormatter.NEAR));
        _addCell(JANumberFormatter.format(cumulCotisations.getSumMasse(), 0, 2, JANumberFormatter.NEAR));

        sumMontantTotal.add(cumulCotisations.getSumMontant());
        sumMasseTotal.add(cumulCotisations.getSumMasse());
    }

    public String getFromDateValeur() {
        return fromDateValeur;
    }

    public String getFromIdExterne() {
        return fromIdExterne;
    }

    public String getToDateValeur() {
        return toDateValeur;
    }

    public String getToIdExterne() {
        return toIdExterne;
    }

    public String getTypeImpression() {
        return typeImpression;
    }

    /**
     * @see globaz.framework.printing.FWAbstractManagerDocumentList#initializeTable()
     */
    @Override
    protected void initializeTable() {
        this._addColumnLeft(getSession().getLabel("RUBRIQUE"));
        this._addColumnLeft(getSession().getLabel("ANNEE_COTISATION"));
        this._addColumnRight(getSession().getLabel("MONTANT"));
        this._addColumnRight(getSession().getLabel("MASSE"));
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setFromDateValeur(String fromDateValeur) {
        this.fromDateValeur = fromDateValeur;
    }

    public void setFromIdExterne(String fromIdExterne) {
        this.fromIdExterne = fromIdExterne;
    }

    public void setToDateValeur(String toDateValeur) {
        this.toDateValeur = toDateValeur;
    }

    public void setToIdExterne(String toIdExterne) {
        this.toIdExterne = toIdExterne;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }

    /**
     * @see globaz.framework.printing.FWAbstractManagerDocumentList#summary()
     */
    @Override
    protected void summary() throws FWIException {
        _addCell(getSession().getLabel("TOTAL"));
        _addCell("");
        _addCell(JANumberFormatter.format(sumMontantTotal.toString(), 0, 2, JANumberFormatter.NEAR));
        _addCell(JANumberFormatter.format(sumMasseTotal.toString(), 0, 2, JANumberFormatter.NEAR));
    }

}
