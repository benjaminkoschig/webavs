package globaz.osiris.print.list;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.recaprubriques.CARecapRubriques;
import globaz.osiris.db.recaprubriques.CARecapRubriquesManager;
import globaz.osiris.parser.CASelectBlockParser;
import globaz.osiris.translation.CACodeSystem;

/**
 * @author dda
 */
public class CAListRecapRubriques extends FWIAbstractManagerDocumentList {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String NUMERO_REFERENCE_INFOROM = "0145GCA";
    private String filtreAnnee;
    private String forIdCategorie;

    private String forIdGenreCompte;
    private String forSelectionRole;
    private String fromDateValeur;
    private String fromIdExterne;
    private String fromIdExterneRole;
    private boolean printFirstPageInfos = true;
    private FWCurrency sumMasseTotal = new FWCurrency();
    private FWCurrency sumMontantTotal = new FWCurrency();
    private String toDateValeur;
    private String toIdExterne;

    private String toIdExterneRole;

    public CAListRecapRubriques() throws Exception {
        // session, prefix, Compagnie, Titre, manager, application
        super(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS), "CA", "GLOBAZ",
                "Liste récapitulatives par rubriques", new CARecapRubriquesManager(),
                CAApplication.DEFAULT_APPLICATION_OSIRIS);
    }

    public CAListRecapRubriques(BSession session) {
        // session, prefix, Compagnie, Titre, manager, application
        super(session, "CA", "GLOBAZ", "Liste récapitulatives par rubriques", new CARecapRubriquesManager(),
                CAApplication.DEFAULT_APPLICATION_OSIRIS);
    }

    /**
     * @see globaz.framework.printing.FWAbstractDocumentList#_beforeExecuteReport()
     */
    @Override
    public void _beforeExecuteReport() {
        CARecapRubriquesManager manager = (CARecapRubriquesManager) _getManager();
        manager.setSession(getSession());

        manager.setFromIdExterne(getFromIdExterne());
        manager.setToIdExterne(getToIdExterne());

        manager.setFromIdExterneRole(getFromIdExterneRole());
        manager.setToIdExterneRole(getToIdExterneRole());

        manager.setFromDateValeur(JACalendar.format(getFromDateValeur(), JACalendar.FORMAT_YYYYMMDD));
        manager.setToDateValeur(JACalendar.format(getToDateValeur(), JACalendar.FORMAT_YYYYMMDD));

        manager.setForSelectionRole(getForSelectionRole());

        manager.setForIdGenreCompte(getForIdGenreCompte());
        manager.setForIdCategorie(getForIdCategorie());

        manager.setFiltreAnnee(getFiltreAnnee());

        _setDocumentTitle(getSession().getLabel("LIST_RECAP_PAR_RUBRIQUES"));
        getDocumentInfo().setTemplateName("");
        // Numéro de référence Inforom.
        getDocumentInfo().setDocumentTypeNumber(CAListRecapRubriques.NUMERO_REFERENCE_INFOROM);
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

        if ((getForSelectionRole() != null) && (getForSelectionRole().indexOf(',') == -1)) {
            try {
                this._addLine(
                        getFontCell(),
                        getSession().getLabel("ROLE") + " : "
                                + CACodeSystem.getLibelle(getSession(), getForSelectionRole()), null, null, null, null);
            } catch (Exception e) {
                // do nothing
            }
        } else {
            this._addLine(getFontCell(), getSession().getLabel("ROLE") + " : " + getSession().getLabel("TOUS"), null,
                    null, null, null);
        }

        if (!JadeStringUtil.isBlank(getForIdGenreCompte())) {
            if (getForIdGenreCompte().equals(CACompteAnnexe.GENRE_COMPTE_STANDARD)) {
                this._addLine(
                        getFontCell(),
                        getSession().getLabel("GENRE") + " : "
                                + getSession().getLabel(CASelectBlockParser.LABEL_COMPTE_AUXILIAIRE_STANDARD), null,
                        null, null, null);
            } else {
                try {
                    FWParametersSystemCodeManager manager = CACodeSystem.getGenreComptes(getSession());
                    for (int i = 0; i < manager.size(); i++) {
                        FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);
                        if (code.getIdCode().equals(getForIdGenreCompte())) {
                            this._addLine(getFontCell(), getSession().getLabel("GENRE") + " : "
                                    + code.getCurrentCodeUtilisateur().getLibelle(), null, null, null, null);
                        }
                    }
                } catch (Exception e) {
                    // do nothing
                }
            }
        }

        if (!JadeStringUtil.isBlank(getForIdCategorie())) {
            if (getForIdCategorie().equals(CACompteAnnexeManager.ALL_CATEGORIE)) {
                this._addLine(
                        getFontCell(),
                        getSession().getLabel("CATEGORIE") + " : "
                                + getSession().getLabel(CASelectBlockParser.LABEL_TOUS), null, null, null, null);
            } else if (getForIdCategorie().equals(CACompteAnnexe.CATEGORIE_COMPTE_STANDARD)) {
                this._addLine(
                        getFontCell(),
                        getSession().getLabel("CATEGORIE") + " : "
                                + getSession().getLabel(CASelectBlockParser.LABEL_COMPTE_ANNEXE_CATEGORIE_STANDARD),
                        null, null, null, null);
            } else {
                try {
                    FWParametersSystemCodeManager manager = CACodeSystem.getCategories(getSession());

                    for (int i = 0; i < manager.size(); i++) {
                        FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);
                        if (code.getIdCode().equals(getForIdCategorie())) {
                            this._addLine(getFontCell(), getSession().getLabel("CATEGORIE") + " : "
                                    + code.getCurrentCodeUtilisateur().getLibelle(), null, null, null, null);
                        }
                    }
                } catch (Exception e) {
                    // do nothing
                }
            }
        }

        if (!JadeStringUtil.isBlank(getFromDateValeur()) && !JadeStringUtil.isBlank(getToDateValeur())) {
            this._addLine(getFontCell(), getSession().getLabel("DATEVALEUR") + " : " + getFromDateValeur() + " - "
                    + getToDateValeur(), null, null, null, null);
        }

        if (!JadeStringUtil.isBlank(getFromIdExterne()) && !JadeStringUtil.isBlank(getToIdExterne())) {
            this._addLine(getFontCell(), getSession().getLabel("RUBRIQUE") + " : " + getFromIdExterne() + " - "
                    + getToIdExterne(), null, null, null, null);
        }

        // AJOUT
        if (!JadeStringUtil.isBlank(getFiltreAnnee())) {
            this._addLine(getFontCell(), getSession().getLabel("ANNEE") + " : " + getFiltreAnnee(), null, null, null,
                    null);
            // FIN AJOUT
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

        CARecapRubriques recap = (CARecapRubriques) entity;

        _addCell(recap.getRoleDescription() + " " + recap.getIdExterneRole());
        _addCell(recap.getNomTiers());

        _addCell(JANumberFormatter.format(recap.getSumMontant(), 0, 2, JANumberFormatter.NEAR));
        _addCell(JANumberFormatter.format(recap.getSumMasse(), 0, 2, JANumberFormatter.NEAR));

        sumMontantTotal.add(recap.getSumMontant());
        sumMasseTotal.add(recap.getSumMasse());
    }

    public String getFiltreAnnee() {
        return filtreAnnee;
    }

    /**
     * @return
     */
    public String getForIdCategorie() {
        return forIdCategorie;
    }

    /**
     * @return
     */
    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    /**
     * @return
     */
    public String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * @return
     */
    public String getFromDateValeur() {
        return fromDateValeur;
    }

    /**
     * @return
     */
    public String getFromIdExterne() {
        return fromIdExterne;
    }

    /**
     * @return
     */
    public String getFromIdExterneRole() {
        return fromIdExterneRole;
    }

    /**
     * @return
     */
    public FWCurrency getSumMasseTotal() {
        return sumMasseTotal;
    }

    /**
     * @return
     */
    public FWCurrency getSumMontantTotal() {
        return sumMontantTotal;
    }

    /**
     * @return
     */
    public String getToDateValeur() {
        return toDateValeur;
    }

    /**
     * @return
     */
    public String getToIdExterne() {
        return toIdExterne;
    }

    /**
     * @return
     */
    public String getToIdExterneRole() {
        return toIdExterneRole;
    }

    /**
     * @see globaz.framework.printing.FWAbstractManagerDocumentList#initializeTable()
     */
    @Override
    protected void initializeTable() {
        this._addColumnLeft(getSession().getLabel("COMPTEANNEXE"));
        this._addColumnLeft(getSession().getLabel("NOM"));
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

    public void setFiltreAnnee(String filtreAnnee) {
        this.filtreAnnee = filtreAnnee;
    }

    /**
     * @param string
     */
    public void setForIdCategorie(String string) {
        forIdCategorie = string;
    }

    /**
     * @param string
     */
    public void setForIdGenreCompte(String string) {
        forIdGenreCompte = string;
    }

    /**
     * @param string
     */
    public void setForSelectionRole(String string) {
        forSelectionRole = string;
    }

    /**
     * @param string
     */
    public void setFromDateValeur(String string) {
        fromDateValeur = string;
    }

    /**
     * @param string
     */
    public void setFromIdExterne(String string) {
        fromIdExterne = string;
    }

    /**
     * @param string
     */
    public void setFromIdExterneRole(String string) {
        fromIdExterneRole = string;
    }

    /**
     * @param currency
     */
    public void setSumMasseTotal(FWCurrency currency) {
        sumMasseTotal = currency;
    }

    /**
     * @param currency
     */
    public void setSumMontantTotal(FWCurrency currency) {
        sumMontantTotal = currency;
    }

    /**
     * @param string
     */
    public void setToDateValeur(String string) {
        toDateValeur = string;
    }

    /**
     * @param string
     */
    public void setToIdExterne(String string) {
        toIdExterne = string;
    }

    /**
     * @param string
     */
    public void setToIdExterneRole(String string) {
        toIdExterneRole = string;
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
