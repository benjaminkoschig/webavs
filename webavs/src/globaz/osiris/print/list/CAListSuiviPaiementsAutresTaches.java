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
import globaz.globall.util.JACalendarGregorianStandard;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.suivipaiements.CASuiviPaiementsAutresTaches;
import globaz.osiris.db.suivipaiements.CASuiviPaiementsAutresTachesManager;
import globaz.osiris.db.suivipaiements.CASuiviPaiementsAutresTachesSumMontant;
import globaz.osiris.db.suivipaiements.CASuiviPaiementsAutresTachesSumMontantManager;

public class CAListSuiviPaiementsAutresTaches extends FWIAbstractManagerDocumentList {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String LABEL_PERIODE_NON_RENSEIGNEE = "PERIODE_NON_RENSEIGNEE";
    private static final String LABEL_SUIVI_PAIEMENTS_AUTRES_TACHES = "SUIVI_PAIEMENTS_AUTRES_TACHES";

    private static final String NUMERO_REFERENCE_INFOROM = "0144GCA";

    private int countAddRows = 0;
    private String forIdCompteCourant = new String();

    private String forMonthYear = new String();
    private String fromDateValeur = new String();

    private boolean printFirstPageInfos = true;
    private FWCurrency soldeMontantsPositifs = new FWCurrency();
    private FWCurrency soldeMouvementsInitial = new FWCurrency();
    private FWCurrency sumMontantTotal = new FWCurrency();

    private FWCurrency sumVentileTotal = new FWCurrency();
    private String untilDateValeur = new String();

    public CAListSuiviPaiementsAutresTaches() throws Exception {
        // session, prefix, Compagnie, Titre, manager, application
        super(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS), "CA", "GLOBAZ",
                "Liste suivi des paiements pour autres tâches", new CASuiviPaiementsAutresTachesManager(),
                CAApplication.DEFAULT_APPLICATION_OSIRIS);
    }

    public CAListSuiviPaiementsAutresTaches(BSession session) {
        // session, prefix, Compagnie, Titre, manager, application
        super(session, "CA", "GLOBAZ", session
                .getLabel(CAListSuiviPaiementsAutresTaches.LABEL_SUIVI_PAIEMENTS_AUTRES_TACHES),
                new CASuiviPaiementsAutresTachesManager(), CAApplication.DEFAULT_APPLICATION_OSIRIS);
    }

    /**
     * @see FWIAbstractManagerDocumentList
     */
    @Override
    public void _beforeExecuteReport() {
        CASuiviPaiementsAutresTachesManager manager = (CASuiviPaiementsAutresTachesManager) _getManager();
        manager.setSession(getSession());

        manager.setFromDateValeur(JACalendar.format(getFromDateValeur(), JACalendar.FORMAT_YYYYMMDD));
        manager.setUntilDateValeur(JACalendar.format(getUntilDateValeur(), JACalendar.FORMAT_YYYYMMDD));

        manager.setForIdCompteCourant(getForIdCompteCourant());

        _setDocumentTitle(getSession().getLabel(CAListSuiviPaiementsAutresTaches.LABEL_SUIVI_PAIEMENTS_AUTRES_TACHES));
        getDocumentInfo().setTemplateName("");
        // Référence Inforom
        getDocumentInfo().setDocumentTypeNumber(CAListSuiviPaiementsAutresTaches.NUMERO_REFERENCE_INFOROM);
        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));

        initSoldeMouvementsInitial();
    }

    /**
     * @see FWIAbstractManagerDocumentList
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

        if ((JadeStringUtil.isBlank(getFromDateValeur())) || (JadeStringUtil.isBlank(getUntilDateValeur()))) {
            this._addError(getSession().getLabel(CAListSuiviPaiementsAutresTaches.LABEL_PERIODE_NON_RENSEIGNEE));
        }
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

        CACompteCourant compteCourant = new CACompteCourant();
        compteCourant.setSession(getSession());
        compteCourant.setIdCompteCourant(getForIdCompteCourant());

        try {
            compteCourant.retrieve(getTransaction());

            this._addLine(getFontCell(), getSession().getLabel("COMPTECOURANT") + " : " + compteCourant.getIdExterne()
                    + " - " + compteCourant.getRubrique().getDescription(), null, null, null, null);
        } catch (Exception e) {

        }

        if (!JadeStringUtil.isBlank(getFromDateValeur()) && !JadeStringUtil.isBlank(getUntilDateValeur())) {
            this._addLine(getFontCell(), getSession().getLabel("PERIODE") + " : " + getFromDateValeur() + " - "
                    + getUntilDateValeur(), null, null, null, null);
        }

        this._addLine(getFontCell(), getSession().getLabel("COTISATIONS_ENCAISSEES") + " : " + soldeMouvementsInitial,
                null, null, null, null);

        this._addLine(getFontCell(), "", null, null, null, null);
    }

    /**
     * @see FWIAbstractManagerDocumentList
     */
    @Override
    protected void addRow(BEntity entity) throws FWIException {
        if (printFirstPageInfos) {
            addFirstPageInfos();
            printFirstPageInfos = false;
        }

        CASuiviPaiementsAutresTaches suivi = (CASuiviPaiementsAutresTaches) entity;
        FWCurrency montant = new FWCurrency(suivi.getSumMontant());

        if (montant.isPositive()) {
            _addCell(suivi.getRubriqueDescription());
            _addCell(JANumberFormatter.format(suivi.getSumMontant()));

            FWCurrency tmp = new FWCurrency((montant.doubleValue() * soldeMouvementsInitial.doubleValue())
                    / soldeMontantsPositifs.doubleValue());

            FWCurrency ventile = new FWCurrency(
                    JANumberFormatter.round(tmp.toString(), 0.05, 2, JANumberFormatter.NEAR));

            if ((_getManager().size() == countAddRows + 1)
                    && ((ventile.doubleValue() + sumVentileTotal.doubleValue()) != soldeMouvementsInitial.doubleValue())) {
                ventile = soldeMouvementsInitial;
                ventile.sub(sumVentileTotal);
            }

            _addCell(JANumberFormatter.format(ventile.toString()));

            sumMontantTotal.add(suivi.getSumMontant());
            sumVentileTotal.add(ventile);
        }
        countAddRows++;
    }

    /**
     * @return Returns the forIdCompteCourant.
     */
    public String getForIdCompteCourant() {
        return forIdCompteCourant;
    }

    /**
     * @return Returns the forMonthYear.
     */
    public String getForMonthYear() {
        return forMonthYear;
    }

    /**
     * @return Returns the fromDateValeur.
     */
    public String getFromDateValeur() {
        return fromDateValeur;
    }

    /**
     * @return Returns the untilDateValeur.
     */
    public String getUntilDateValeur() {
        return untilDateValeur;
    }

    /**
     * @see FWIAbstractManagerDocumentList
     */
    @Override
    protected void initializeTable() {
        this._addColumnLeft(getSession().getLabel("RUBRIQUE"));
        this._addColumnRight(getSession().getLabel("MONTANTBASE"));
        this._addColumnRight(getSession().getLabel("MONTANT_VENTILE"));

        initMontantsPositifs();
    }

    /**
     * Calcul des montants positifs trouvés.
     */
    private void initMontantsPositifs() {
        CASuiviPaiementsAutresTachesManager manager = (CASuiviPaiementsAutresTachesManager) _getManager();

        for (int i = 0; i < manager.getSize(); i++) {
            CASuiviPaiementsAutresTaches suivi = (CASuiviPaiementsAutresTaches) manager.get(i);

            FWCurrency test = new FWCurrency(suivi.getSumMontant());
            if (test.isPositive()) {
                soldeMontantsPositifs.add(suivi.getSumMontant());
            }
        }
    }

    /**
     * Calcul le solde des mouvements initials (voire mandat 045, point A et A').
     */
    private void initSoldeMouvementsInitial() {
        CASuiviPaiementsAutresTachesSumMontantManager manager = new CASuiviPaiementsAutresTachesSumMontantManager();
        manager.setSession(getSession());

        manager.setFromDateValeur(JACalendar.format(getFromDateValeur(), JACalendar.FORMAT_YYYYMMDD));
        manager.setUntilDateValeur(JACalendar.format(getUntilDateValeur(), JACalendar.FORMAT_YYYYMMDD));

        manager.setForIdCompteCourant(getForIdCompteCourant());

        try {
            manager.find(getTransaction());
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return;
        }

        if (!manager.isEmpty()) {
            soldeMouvementsInitial = new FWCurrency(
                    ((CASuiviPaiementsAutresTachesSumMontant) manager.getFirstEntity()).getSumMontant());
            soldeMouvementsInitial.abs();
        } else {
            soldeMouvementsInitial = new FWCurrency();
        }
    }

    /**
     * @see FWIAbstractManagerDocumentList
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * @param forIdCompteCourant
     *            The forIdCompteCourant to set.
     */
    public void setForIdCompteCourant(String forIdCompteCourant) {
        this.forIdCompteCourant = forIdCompteCourant;
    }

    /**
     * @param forMonthYear
     *            The forMonthYear to set.
     */
    public void setForMonthYear(String forMonthYear) {
        this.forMonthYear = forMonthYear;

        if (!JadeStringUtil.isBlank(forMonthYear)) {
            // Extrait les valeurs de début et de fin du mois.
            try {
                JADate tmp = new JADate("01." + forMonthYear);
                setFromDateValeur(tmp.toStr("."));

                JACalendar myCalendar = new JACalendarGregorianStandard();
                tmp = myCalendar.addDays(tmp, myCalendar.daysInMonth(tmp.getMonth(), tmp.getYear()) - 1);
                setUntilDateValeur(tmp.toStr("."));
            } catch (JAException e) {
                // do nothting here
            }
        }
    }

    /**
     * @param fromDateValeur
     *            The fromDateValeur to set.
     */
    public void setFromDateValeur(String fromDateValeur) {
        this.fromDateValeur = fromDateValeur;
    }

    /**
     * @param untilDateValeur
     *            The untilDateValeur to set.
     */
    public void setUntilDateValeur(String untilDateValeur) {
        this.untilDateValeur = untilDateValeur;
    }

    /**
     * @see FWIAbstractManagerDocumentList
     */
    @Override
    protected void summary() throws FWIException {
        _addCell(getSession().getLabel("TOTAL"));
        _addCell(JANumberFormatter.format(sumMontantTotal.toString()));
        _addCell(JANumberFormatter.format(sumVentileTotal.toString()));
    }

}
