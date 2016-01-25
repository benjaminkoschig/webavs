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
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.comptes.CARole;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.listfsfp.CANominativeFsfp;
import globaz.osiris.db.listfsfp.CANominativeFsfpManager;
import globaz.osiris.exceptions.CATechnicalException;
import globaz.osiris.translation.CACodeSystem;
import globaz.pyxis.db.tiers.TITiers;

/**
 * @author BJO <h1>description:</h1> Génère une liste(pdf) nominative des cas FSFP en poursuite ou irrécouvrable pour
 *         une date donnée. Sur cette liste figurent le numéro d’affilié, le nom, l’adresse de domicile, le numéro de
 *         facture, le libellé de la facture, le solde ouvert en poursuite ou irrécouvrable.
 * 
 */
/**
 * @author BJO
 * 
 */
public class CAListNominativeFsfp extends FWIAbstractManagerDocumentList {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String LABEL_DATE_NON_RENSEIGNEE = "DATE_NON_RENSEIGNEE";
    private static final String LABEL_TITRE_LIST_NOMINATIVE_FSFP = "LIST_NOMINATIVE_FSFP";

    private static final String NUMERO_REFERENCE_INFOROM = "0194GCA";
    private int countAddRows = 0;

    private String forIdCompteCourant = new String();
    private String forSelectionRole = new String();
    private String fromDateValeur = new String();
    private boolean isBloque = false;
    private boolean listeResumee = false;

    private String numAffilePrecedent = null;
    private boolean printFirstPageInfos = true;

    private boolean seulPoursuiteIrrecouvrable = false;
    private FWCurrency sommeMontant;
    private final FWCurrency sommeMontantTotal = new FWCurrency();

    public CAListNominativeFsfp() throws Exception {
        // session, prefix, Compagnie, Titre, manager, application
        super(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS), "CA", "GLOBAZ", "Liste nominative autre tâche",
                new CANominativeFsfpManager(), CAApplication.DEFAULT_APPLICATION_OSIRIS);
    }

    public CAListNominativeFsfp(BSession session) {
        // session, prefix, Compagnie, Titre, manager, application
        super(session, "CA", "GLOBAZ", session.getLabel(CAListNominativeFsfp.LABEL_TITRE_LIST_NOMINATIVE_FSFP),
                new CANominativeFsfpManager(), CAApplication.DEFAULT_APPLICATION_OSIRIS);
    }

    /**
     * @see FWIAbstractManagerDocumentList
     */
    @Override
    public void _beforeExecuteReport() {
        CANominativeFsfpManager manager = (CANominativeFsfpManager) _getManager();
        manager.setSession(getSession());

        manager.setFromDateValeur(JACalendar.format(getFromDateValeur(), JACalendar.FORMAT_YYYYMMDD));
        manager.setForIdCompteCourant(getForIdCompteCourant());
        manager.setForSelectionRole(getForSelectionRole());

        _setDocumentTitle(getSession().getLabel(CAListNominativeFsfp.LABEL_TITRE_LIST_NOMINATIVE_FSFP));
        getDocumentInfo().setTemplateName("");
        // Référence Inforom
        getDocumentInfo().setDocumentTypeNumber(CAListNominativeFsfp.NUMERO_REFERENCE_INFOROM);
        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));

    }

    /**
     * @see FWIAbstractManagerDocumentList
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

        if ((JadeStringUtil.isBlank(getFromDateValeur()))) {
            this._addError(getSession().getLabel(CAListNominativeFsfp.LABEL_DATE_NON_RENSEIGNEE));
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

        if (!JadeStringUtil.isBlank(getFromDateValeur())) {
            this._addLine(getFontCell(), getSession().getLabel("DATE") + " : " + getFromDateValeur(), null, null, null,
                    null);
        }

        if (!JadeStringUtil.isBlank(getForSelectionRole())) {
            if (getForSelectionRole().indexOf(',') != -1) {
                this._addLine(getFontCell(), getSession().getLabel("ROLE") + " : Tous", null, null, null, null);
            } else {
                CARole role = new CARole();
                role.setIdRole(getForSelectionRole());
                role.setSession(getSession());
                try {
                    role.retrieve();
                    this._addLine(getFontCell(), getSession().getLabel("ROLE") + " : " + role.getDescription(), null,
                            null, null, null);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        if (getSeulPoursuiteIrrecouvrable()) {
            this._addLine(getFontCell(), getSession().getLabel("LIST_SEULEMENT_POURSUITE_IRRECOUVRABLE"), null, null,
                    null, null);
        }

        // ligne vide
        this._addLine(getFontCell(), "", null, null, null, null);
    }

    /**
     * @see FWIAbstractManagerDocumentList
     */
    @Override
    protected void addRow(BEntity entity) throws FWIException {
        boolean isPoursuiteOuIrrecouvrable = false;
        isBloque = false;

        if (printFirstPageInfos) {
            addFirstPageInfos();
            printFirstPageInfos = false;
        }

        CANominativeFsfp nominativeFsfp = (CANominativeFsfp) entity;

        // récupération du compteAnnexe
        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setIdCompteAnnexe(nominativeFsfp.getIdCompteAnnexe());
        compteAnnexe.setSession(getSession());
        try {
            compteAnnexe.retrieve();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // récupération de la section
        CASection section = new CASection();
        section.setIdSection(nominativeFsfp.getIdSection());
        section.setSession(getSession());
        try {
            section.retrieve();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // variable d'état permettant de savoir si on est en présence d'un cas
            // de poursuite ou d'irrécouvrable
            if (compteAnnexe.isMotifExistant(CACodeSystem.CS_IRRECOUVRABLE)
                    || section.hasMotifContentieux(CACodeSystem.CS_IRRECOUVRABLE)
                    || section.isSectionAuxPoursuites(true)) {
                isPoursuiteOuIrrecouvrable = true;
            } else if (compteAnnexe.isCompteBloque(JACalendar.todayJJsMMsAAAA())
                    && !compteAnnexe.isMotifExistant(CACodeSystem.CS_RENTIER)) {
                isPoursuiteOuIrrecouvrable = true;
                isBloque = true;
            }
        } catch (CATechnicalException ex) {
            throw new FWIException("Error while calling [hasMotifContentieux]", ex);
        }

        // checkbox coché, on liste que les cas aux poursuites et irrécouvrables
        if (seulPoursuiteIrrecouvrable) {
            if (isPoursuiteOuIrrecouvrable) {
                if (getListeResumee()) {
                    if ((numAffilePrecedent == null) || !numAffilePrecedent.equals(compteAnnexe.getIdExterneRole())) {
                        ajouterLigne(nominativeFsfp, compteAnnexe, section);
                    }
                } else {
                    ajouterLigne(nominativeFsfp, compteAnnexe, section);
                }
            }
        }
        // checkbox pas coché, on liste tout
        else {
            if (getListeResumee()) {
                if ((numAffilePrecedent == null) || !numAffilePrecedent.equals(compteAnnexe.getIdExterneRole())) {
                    ajouterLigne(nominativeFsfp, compteAnnexe, section);
                }
            } else {
                ajouterLigne(nominativeFsfp, compteAnnexe, section);
            }
        }
    }

    private void ajouterLigne(CANominativeFsfp nominativeFsfp, CACompteAnnexe compteAnnexe, CASection section)
            throws FWIException {
        // num affilié et nom (compteannexe)
        _addCell(compteAnnexe.getIdExterneRole());
        _addCell(compteAnnexe.getDescription());
        numAffilePrecedent = compteAnnexe.getIdExterneRole();

        // addresse de domicile (tiers)
        TITiers tiers = new TITiers();
        tiers.setIdTiers(nominativeFsfp.getIdTiers());
        tiers.setSession(getSession());
        try {
            tiers.retrieve();
            _addCell(tiers.getAdresseAsString(getDocumentInfo()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!getListeResumee()) {
            // numéro de facture (se.idExterne)
            _addCell(nominativeFsfp.getIdExterne());
            // libellé de la facture
            _addCell(section.getDescription());
            // le solde ouvert (
            sommeMontant = new FWCurrency(nominativeFsfp.getSommeMontant());
            _addCell(JANumberFormatter.format(sommeMontant.toString()));
            // contentieux
            try {
                if (section.hasMotifContentieux(CACodeSystem.CS_IRRECOUVRABLE)
                        || compteAnnexe.isMotifExistant(CACodeSystem.CS_IRRECOUVRABLE)) {
                    _addCell(getSession().getLabel("IM_IRRECOUVRABLES"));
                } else if (section.isSectionAuxPoursuites(true)) {
                    _addCell(getSession().getLabel("IM_ENPOURSUITE"));
                } else if (isBloque) {
                    _addCell(getSession().getLabel("IM_BLOQUE"));
                } else {
                    _addCell("");
                }
            } catch (CATechnicalException ex) {
                throw new FWIException("Error while calling [hasMotifContentieux]", ex);
            }

            sommeMontantTotal.add(sommeMontant);
        } else {
            _addCell("");
            _addCell("");
            _addCell("");
            _addCell("");
        }

        countAddRows++;
    }

    /**
     * @return Returns the forIdCompteCourant.
     */
    public String getForIdCompteCourant() {
        return forIdCompteCourant;
    }

    public String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * @return Returns the fromDateValeur.
     */
    public String getFromDateValeur() {
        return fromDateValeur;
    }

    public boolean getListeResumee() {
        return listeResumee;
    }

    public boolean getSeulPoursuiteIrrecouvrable() {
        return seulPoursuiteIrrecouvrable;
    }

    /**
     * @see FWIAbstractManagerDocumentList
     */
    @Override
    protected void initializeTable() {
        if (getListeResumee()) {
            this._addColumnLeft(getSession().getLabel("AFFILIE"), 30);
            this._addColumnLeft(getSession().getLabel("NOM"), 70);
            this._addColumnLeft(getSession().getLabel("ADRESSE_DOMICILE"), 70);
            this._addColumnLeft("", 30);
            this._addColumnLeft("", 70);
            this._addColumnLeft("", 30);
            this._addColumnLeft("", 20);
        } else {
            this._addColumnLeft(getSession().getLabel("AFFILIE"), 30);
            this._addColumnLeft(getSession().getLabel("NOM"), 70);
            this._addColumnLeft(getSession().getLabel("ADRESSE_DOMICILE"), 70);
            this._addColumnLeft(getSession().getLabel("FACTURE"), 30);
            this._addColumnLeft(getSession().getLabel("LIBELLE_FACTURE"), 70);
            this._addColumnRight(getSession().getLabel("SOLDE_OUVERT"), 30);
            this._addColumnRight(getSession().getLabel("CONTENTIEUX"), 20);
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

    public void setForSelectionRole(String forRole) {
        forSelectionRole = forRole;
    }

    /**
     * @param fromDateValeur
     *            The fromDateValeur to set.
     */
    public void setFromDateValeur(String fromDateValeur) {
        this.fromDateValeur = fromDateValeur;
    }

    public void setListeResumee(boolean listeResumee) {
        this.listeResumee = listeResumee;
    }

    public void setSeulPoursuiteIrrecouvrable(boolean seulPoursuiteIrrecouvrable) {
        this.seulPoursuiteIrrecouvrable = seulPoursuiteIrrecouvrable;
    }

    /**
     * @see FWIAbstractManagerDocumentList
     */
    @Override
    protected void summary() throws FWIException {
        if (!getListeResumee()) {
            _addCell("");
            _addCell("");
            _addCell("");
            _addCell("");
            _addCell(getSession().getLabel("TOTAL"));
            _addCell(JANumberFormatter.format(sommeMontantTotal.toString()));
        }
    }

}
