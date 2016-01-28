package globaz.osiris.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.osiris.db.recaprubriques.CARecapRubriquesExcelManager;
import globaz.osiris.print.list.CAListRecapRubriquesExcel;

/**
 * @author sch
 */
public class CAProcessListRecapParRubriquesExcel extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String filtreAnnee;
    private String forIdCategorie;

    private String forIdGenreCompte;
    private String forSelectionRole;
    private String fromDateValeur;
    private String fromIdExterne;
    private String fromIdExterneRole;
    private FWCurrency sumMasseTotal = new FWCurrency();
    private FWCurrency sumMontantTotal = new FWCurrency();
    private String toDateValeur;
    private String toIdExterneRole;

    public CAProcessListRecapParRubriquesExcel() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            CARecapRubriquesExcelManager manager = new CARecapRubriquesExcelManager();
            manager.setSession(getSession());

            manager.setFromIdExterne(getFromIdExterne());

            manager.setFromIdExterneRole(getFromIdExterneRole());
            manager.setToIdExterneRole(getToIdExterneRole());

            manager.setFromDateValeur(JACalendar.format(getFromDateValeur(), JACalendar.FORMAT_YYYYMMDD));
            manager.setToDateValeur(JACalendar.format(getToDateValeur(), JACalendar.FORMAT_YYYYMMDD));

            manager.setForSelectionRole(getForSelectionRole());

            manager.setForIdGenreCompte(getForIdGenreCompte());
            manager.setForIdCategorie(getForIdCategorie());

            manager.setFiltreAnnee(getFiltreAnnee());
            manager.find();

            if (isAborted()) {
                return false;
            }

            setProgressScaleValue(manager.size());
            // Création du document
            CAListRecapRubriquesExcel excelDoc = new CAListRecapRubriquesExcel(getSession(), this);
            excelDoc.setRole(getForSelectionRole());
            excelDoc.setGenre(getForIdGenreCompte());
            excelDoc.setCategorie(getForIdCategorie());
            excelDoc.setFromIdExterneRole(getFromIdExterneRole());
            excelDoc.setToIdExterneRole(getToIdExterneRole());
            excelDoc.setDateValeurDebut(getFromDateValeur());
            excelDoc.setDateValeurFin(getToDateValeur());
            excelDoc.setIdExterne(getFromIdExterne());
            excelDoc.setDocumentInfo(createDocumentInfo());
            excelDoc.populateSheetListe(manager, getTransaction());

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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("LISTE_RECAP_PARRUBRIQUE_EXCEL_SUJETMAIL_KO");
        } else {
            return getSession().getLabel("LISTE_RECAP_PARRUBRIQUE_EXCEL_SUJETMAIL_OK");
        }
    }

    public String getFiltreAnnee() {
        return filtreAnnee;
    }

    public String getForIdCategorie() {
        return forIdCategorie;
    }

    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    public String getForSelectionRole() {
        return forSelectionRole;
    }

    public String getFromDateValeur() {
        return fromDateValeur;
    }

    public String getFromIdExterne() {
        return fromIdExterne;
    }

    public String getFromIdExterneRole() {
        return fromIdExterneRole;
    }

    public FWCurrency getSumMasseTotal() {
        return sumMasseTotal;
    }

    public FWCurrency getSumMontantTotal() {
        return sumMontantTotal;
    }

    public String getToDateValeur() {
        return toDateValeur;
    }

    public String getToIdExterneRole() {
        return toIdExterneRole;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setFiltreAnnee(String filtreAnnee) {
        this.filtreAnnee = filtreAnnee;
    }

    public void setForIdCategorie(String forIdCategorie) {
        this.forIdCategorie = forIdCategorie;
    }

    public void setForIdGenreCompte(String forIdGenreCompte) {
        this.forIdGenreCompte = forIdGenreCompte;
    }

    public void setForSelectionRole(String forSelectionRole) {
        this.forSelectionRole = forSelectionRole;
    }

    public void setFromDateValeur(String fromDateValeur) {
        this.fromDateValeur = fromDateValeur;
    }

    public void setFromIdExterne(String fromIdExterne) {
        this.fromIdExterne = fromIdExterne;
    }

    public void setFromIdExterneRole(String fromIdExterneRole) {
        this.fromIdExterneRole = fromIdExterneRole;
    }

    public void setSumMasseTotal(FWCurrency sumMasseTotal) {
        this.sumMasseTotal = sumMasseTotal;
    }

    public void setSumMontantTotal(FWCurrency sumMontantTotal) {
        this.sumMontantTotal = sumMontantTotal;
    }

    public void setToDateValeur(String toDateValeur) {
        this.toDateValeur = toDateValeur;
    }

    public void setToIdExterneRole(String toIdExterneRole) {
        this.toIdExterneRole = toIdExterneRole;
    }

}
