package ch.globaz.vulpecula.process.comptabilite;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.osiris.db.recaprubriques.CARecapRubriquesExcelManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.external.BProcessWithContext;

public class ListeRecapParRubriqueProcess extends BProcessWithContext {

    private static final long serialVersionUID = 4786967713929583014L;
    private String filtreAnnee;
    private String forIdRole;
    private String forIdGenre;
    private String forIdCategorie;
    private String fromDateValeur;
    private String toDateValeur;
    private String fromIdExterne;
    private String toIdExterne;
    private List<String> fromIdExternes = new ArrayList<String>();
    private FWCurrency sumMasseTotal = new FWCurrency();
    private FWCurrency sumMontantTotal = new FWCurrency();

    @Override
    protected boolean _executeProcess() {

        CARecapRubriquesExcelManager manager = new CARecapRubriquesExcelManager();
        manager.setSession(getSession());
        manager.setFromIdExterneRole(getFromIdExterne());
        manager.setToIdExterneRole(getToIdExterne());
        manager.setFromDateValeur(JACalendar.format(getFromDateValeur(), JACalendar.FORMAT_YYYYMMDD));
        manager.setToDateValeur(JACalendar.format(getToDateValeur(), JACalendar.FORMAT_YYYYMMDD));
        manager.setForSelectionRole(getForIdRole());
        manager.setForIdGenreCompte(getForIdGenre());
        manager.setForIdCategorie(getForIdCategorie());
        manager.setFiltreAnnee(getFiltreAnnee());

        if (isAborted()) {
            return false;
        }

        try {
            print(manager);
        } catch (IOException e) {
            getTransaction().addErrors(e.getMessage());
        }

        setProgressScaleValue(manager.size());

        return true;
    }

    private void print(CARecapRubriquesExcelManager manager) throws IOException {
        ListRecapParRubriqueExcel liste = new ListRecapParRubriqueExcel(getSession(),
                DocumentConstants.LISTES_RECAP_PAR_RUBRIQUE, DocumentConstants.LISTES_REVISION_RECAP_DOC_NAME,
                fromIdExternes);
        liste.setManager(manager);

        liste.create();

        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), liste.getOutputFile());
    }

    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    public String getFiltreAnnee() {
        return filtreAnnee;
    }

    public void setFiltreAnnee(String filtreAnnee) {
        this.filtreAnnee = filtreAnnee;
    }

    public String getFromDateValeur() {
        return fromDateValeur;
    }

    public void setFromDateValeur(String fromDateValeur) {
        this.fromDateValeur = fromDateValeur;
    }

    public String getToDateValeur() {
        return toDateValeur;
    }

    public void setToDateValeur(String toDateValeur) {
        this.toDateValeur = toDateValeur;
    }

    public FWCurrency getSumMasseTotal() {
        return sumMasseTotal;
    }

    public void setSumMasseTotal(FWCurrency sumMasseTotal) {
        this.sumMasseTotal = sumMasseTotal;
    }

    public FWCurrency getSumMontantTotal() {
        return sumMontantTotal;
    }

    public void setSumMontantTotal(FWCurrency sumMontantTotal) {
        this.sumMontantTotal = sumMontantTotal;
    }

    public String getForIdRole() {
        return forIdRole;
    }

    public void setForIdRole(String forIdRole) {
        this.forIdRole = forIdRole;
    }

    public String getForIdGenre() {
        return forIdGenre;
    }

    public void setForIdGenre(String forIdGenre) {
        this.forIdGenre = forIdGenre;
    }

    public String getForIdCategorie() {
        return forIdCategorie;
    }

    public void setForIdCategorie(String forIdCategorie) {
        this.forIdCategorie = forIdCategorie;
    }

    public String getFromIdExterne() {
        return fromIdExterne;
    }

    public void setFromIdExterne(String fromIdExterne) {
        this.fromIdExterne = fromIdExterne;
    }

    public String getToIdExterne() {
        return toIdExterne;
    }

    public void setToIdExterne(String toIdExterne) {
        this.toIdExterne = toIdExterne;
    }

    public List<String> getFromIdExternes() {
        return fromIdExternes;
    }

    public void setFromIdExternes(List<String> fromIdExternes) {
        this.fromIdExternes = fromIdExternes;
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_RECAP_PAR_RUBRIQUE;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

}
