package globaz.aquila.print.journal;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.api.ICOEtape;
import globaz.aquila.application.COApplication;
import globaz.aquila.db.access.journal.COElementJournalBatch;
import globaz.aquila.db.access.journal.COElementJournalBatchManager;
import globaz.aquila.db.access.poursuite.COExtraitCompteManager;
import globaz.aquila.db.rdp.CORequisitionPoursuiteUtil;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.ts.opge.COTSOPGEExecutor;
import globaz.aquila.ts.opge.file.COOPGEFileCommonPart;
import globaz.aquila.ts.opge.utils.COTSOPGEUtils;
import globaz.aquila.ts.utils.COTSTiersUtils;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.docinfo.CADocumentInfoHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.dynamique.FWIDocumentTable;
import globaz.framework.printing.itext.dynamique.FWITableModel;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.db.comptes.extrait.CAExtraitCompte;
import globaz.osiris.external.IntRole;
import globaz.osiris.external.IntTiers;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.db.adressecourrier.TIPays;
import java.math.BigDecimal;
import java.util.StringTokenizer;

public class COTSOPGEImprimerRequisitionPoursuiteJournal extends FWIAbstractManagerDocumentList {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String LABEL_RDP_TSGE_CODE = "RDP_TSGE_CODE";
    private static final String LABEL_RDP_TSGE_MONTANT = "RDP_TSGE_MONTANT";
    private static final String LABEL_RDP_TSGE_NUMERO = "RDP_TSGE_NUMERO";
    private static final String LABEL_RDP_TSGE_TEXTE_LIBRE = "RDP_TSGE_TEXTE_LIBRE";
    private static final String LABEL_RDP_TSGE_TITLE = "RDP_TSGE_TITLE";
    private static final String NUM_REF_INFOROM = "0224GCO";

    private String ecrituresFromDate = "0";

    private COElementJournalBatch element = null;
    private String forIdJournal;

    private String forNotIdJournalCreance = "0";

    /**
     * 
     * @throws Exception
     */
    public COTSOPGEImprimerRequisitionPoursuiteJournal() throws Exception {
        super(new BSession(ICOApplication.DEFAULT_APPLICATION_AQUILA), COApplication.APPLICATION_AQUILA_PREFIX,
                "GLOBAZ", "PresentationFichierPourOP", new COElementJournalBatchManager(),
                ICOApplication.DEFAULT_APPLICATION_AQUILA);
    }

    /**
     * @param session
     */
    public COTSOPGEImprimerRequisitionPoursuiteJournal(BSession session) {
        super(session, COApplication.APPLICATION_AQUILA_PREFIX, "GLOBAZ", session
                .getLabel(COTSOPGEImprimerRequisitionPoursuiteJournal.LABEL_RDP_TSGE_TITLE),
                new COElementJournalBatchManager(), ICOApplication.DEFAULT_APPLICATION_AQUILA);
    }

    /**
     * @see globaz.framework.printing.FWAbstractDocumentList#_beforeExecuteReport()
     */
    @Override
    public void _beforeExecuteReport() {
        COElementJournalBatchManager manager = (COElementJournalBatchManager) _getManager();
        manager.setSession(getSession());
        manager.setForIdJournal(getForIdJournal());

        super.getDocumentInfo().setDocumentTypeNumber(COTSOPGEImprimerRequisitionPoursuiteJournal.NUM_REF_INFOROM);

        _setDocumentTitle(getSession().getLabel(COTSOPGEImprimerRequisitionPoursuiteJournal.LABEL_RDP_TSGE_TITLE));
        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        _setOrientationPortrait();
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
     * Ajout les informations du débiteur (zone 10).
     * 
     * @param element
     * @throws Exception
     */
    private void addDebiteur(COElementJournalBatch element) throws Exception {
        this._addLine(getFontHeaderPage(), null, null, null, null, null);

        FWIDocumentTable tableDebiteur = new FWIDocumentTable();

        String debiteur = element.getContentieux().getCompteAnnexe().getIdExterneRole();
        if (element.getContentieux().getCompteAnnexe().getIdRole().equals(IntRole.ROLE_AFFILIE_PARITAIRE)) {
            debiteur += " " + getSession().getLabel("RDP_COTISATIONS_PARITAIRES");
        } else if (element.getContentieux().getCompteAnnexe().getIdRole().equals(IntRole.ROLE_AFFILIE_PERSONNEL)) {
            debiteur += " " + getSession().getLabel("RDP_COTISATIONS_PERSONNELLES");
        }

        tableDebiteur.addColumn(getSession().getLabel("RDP_TSGE_DEBITEUR") + " " + debiteur, FWITableModel.LEFT);
        tableDebiteur.endTableDefinition();

        TIAdresseDataSource adresseDataSource = COServiceLocator.getTiersService().getAdresseDataSource(getSession(),
                element.getContentieux().getCompteAnnexe().getTiers(),
                element.getContentieux().getCompteAnnexe().getIdExterneRole(), JACalendar.today().toString());

        tableDebiteur.addCell(COTSTiersUtils.getNomOuRaisonSociale(getSession(), element.getContentieux(),
                adresseDataSource).toUpperCase());
        tableDebiteur.addRow();

        if (!JadeStringUtil.isBlank(COTSTiersUtils.getSuiteNomOuRaisonSociale(getSession(), element.getContentieux(),
                adresseDataSource))) {
            tableDebiteur.addCell(COTSTiersUtils.getSuiteNomOuRaisonSociale(getSession(), element.getContentieux(),
                    adresseDataSource).toUpperCase());
            tableDebiteur.addRow();
        }

        tableDebiteur.addCell(COTSTiersUtils.convertSpecialChars(
                adresseDataSource.rue + " " + adresseDataSource.numeroRue).toUpperCase());
        tableDebiteur.addRow();

        tableDebiteur.addCell(COTSTiersUtils.convertSpecialChars(
                adresseDataSource.localiteNpa + " " + adresseDataSource.localiteNom).toUpperCase());
        tableDebiteur.addRow();

        _addTable(tableDebiteur);
    }

    /**
     * Ajoute les informations de header de la page.
     * 
     * @param element
     * @throws FWIException
     */
    private void addPageInfos(COElementJournalBatch element) throws Exception {
        if (!_getReport().isOpen()) {
            _getReport().open();
        }

        addDebiteur(element);

        addTableCreances(element);

        addTableImputations(element);

        addTableTextes(element);

        addTableRemarques(element);

        _addPageBreak();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList#addRow(globaz.globall.db.BEntity)
     **/
    @Override
    protected void addRow(BEntity entity) throws FWIException {
        COElementJournalBatch element = (COElementJournalBatch) entity;
        setElement(element);

        try {
            addPageInfos(element);
        } catch (Exception e) {
            JadeLogger.warn(this, e);
        }
    }

    /**
     * Ajoute les créances sans intérêts.
     * 
     * @param element
     * @param tableCreance
     * @throws Exception
     * @throws FWIException
     */
    private void addRowsCreancesSansInterets(COElementJournalBatch element, FWIDocumentTable tableCreance, int countRows)
            throws Exception, FWIException {

        COExtraitCompteManager extraitCompteManager = COTSOPGEUtils.getExtraitCompteManager(getSession(),
                element.getContentieux(), forNotIdJournalCreance, ecrituresFromDate);

        extraitCompteManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        for (int i = 0; i < extraitCompteManager.size(); ++i) {
            CAExtraitCompte extraitCompte = (CAExtraitCompte) extraitCompteManager.getEntity(i);

            FWCurrency montant = new FWCurrency(extraitCompte.getMontant());

            if (montant.isPositive()
                    && !montant.isZero()
                    && !COTSOPGEUtils.isRubriqueSoumise(getSession(), getTransaction(), extraitCompte.getIdRubrique())
                    && !CORequisitionPoursuiteUtil.isLineBlocked(getTransaction(),
                            ICOEtape.CS_REQUISITION_DE_POURSUITE_ENVOYEE, extraitCompte.getIdRubrique())) {
                tableCreance.addCell("" + countRows);
                tableCreance.addCell(COTSTiersUtils.convertSpecialChars(
                        COTSOPGEUtils.getDescriptionExtraitCompte(getSession(), extraitCompte,
                                element.getIdTransition())).toUpperCase());
                tableCreance.addCell(extraitCompte.getMontant());
                tableCreance.addCell("");

                tableCreance.addRow();

                countRows++;
            }
        }
    }

    /**
     * Ajoute les remarques plus le privilege legal.
     * 
     * @param element
     * @param tableTextes
     * @throws Exception
     * @throws FWIException
     */
    private void addRowsRemarqueDebiteur(COElementJournalBatch element, FWIDocumentTable tableTextes) throws Exception,
            FWIException {
        int countRows = 0;

        countRows++;

        tableTextes.addCell("" + countRows);
        tableTextes.addCell(COOPGEFileCommonPart.TYPE_STRUCTURE_TEXTE_CREANCE);
        tableTextes.addCell(getSession().getLabel("RDP_LIBELLE_PRIVILEGE_LEGAL_REQUIS"));
        tableTextes.addRow();

        if (!JadeStringUtil.isIntegerEmpty(element.getIdRemarque())) {
            StringTokenizer st = new StringTokenizer(element.getRemarque().getTexte(), "\r\n");

            while (st.hasMoreTokens()) {
                String tmp = COTSTiersUtils.convertSpecialChars(st.nextToken());

                countRows++;

                tableTextes.addCell("" + countRows);
                tableTextes.addCell(COOPGEFileCommonPart.TYPE_STRUCTURE_TEXTE_CREANCE);
                tableTextes.addCell(tmp.toUpperCase());

                tableTextes.addRow();
            }
        }
    }

    /**
     * Ajoute la zone de texte manuel ou automatique.
     * 
     * @param element
     * @param adresseDataSource
     * @throws Exception
     * @throws FWIException
     */
    private void addRowsStructureDebiteur(COElementJournalBatch element, TIAdresseDataSource adresseDataSource)
            throws Exception, FWIException {
        FWIDocumentTable tableTextes = getTableTextesDeclaration();

        int countRows = 0;

        if (!IntTiers.OSIRIS_PERSONNE_MORALE.equals(element.getContentieux().getCompteAnnexe().getTiers()
                .getTypeTiers())) {
            countRows++;
            addRowStructureDebiteur(
                    element,
                    tableTextes,
                    countRows,
                    COServiceLocator.getTiersService().getFormulePolitesse(getSession(),
                            element.getContentieux().getCompteAnnexe().getTiers(), getSession().getIdLangueISO()));
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne1)) {
            countRows++;
            addRowStructureDebiteur(element, tableTextes, countRows, adresseDataSource.fullLigne1);
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne2)) {
            countRows++;
            addRowStructureDebiteur(element, tableTextes, countRows, adresseDataSource.fullLigne2);
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne3)) {
            countRows++;
            addRowStructureDebiteur(element, tableTextes, countRows, adresseDataSource.fullLigne3);
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne4)) {
            countRows++;
            addRowStructureDebiteur(element, tableTextes, countRows, adresseDataSource.fullLigne4);
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.rue) || !JadeStringUtil.isBlank(adresseDataSource.numeroRue)) {
            countRows++;
            addRowStructureDebiteur(element, tableTextes, countRows, adresseDataSource.rue + " "
                    + adresseDataSource.numeroRue);
        }

        countRows++;
        addRowStructureDebiteur(element, tableTextes, countRows, adresseDataSource.localiteNpa + " "
                + adresseDataSource.localiteNom);

        _addTable(tableTextes);
    }

    /**
     * Ajoute le texte pour un tiers étranger.
     * 
     * @param element
     * @param adresseDataSourceDomicile
     * @param adresseDataSourceDebiteur
     * @throws Exception
     * @throws FWIException
     */
    private void addRowsStructureDebiteurEtranger(COElementJournalBatch element,
            TIAdresseDataSource adresseDataSourceDomicile, TIAdresseDataSource adresseDataSourceDebiteur)
            throws Exception, FWIException {
        FWIDocumentTable tableTextes = getTableTextesDeclaration();

        int countRows = 0;

        String codePolitesse = "";
        if (!IntTiers.OSIRIS_PERSONNE_MORALE.equals(element.getContentieux().getCompteAnnexe().getTiers()
                .getTypeTiers())) {
            codePolitesse = COServiceLocator.getTiersService().getFormulePolitesse(getSession(),
                    element.getContentieux().getCompteAnnexe().getTiers(), getSession().getIdLangueISO())
                    + " ";
        }

        countRows++;
        addRowStructureDebiteur(element, tableTextes, countRows,
                codePolitesse + COTSTiersUtils.getLigneCumule(adresseDataSourceDomicile, " "));

        if (!JadeStringUtil.isBlank(adresseDataSourceDomicile.rue)
                || !JadeStringUtil.isBlank(adresseDataSourceDomicile.numeroRue)) {
            countRows++;

            String tmp = adresseDataSourceDomicile.rue + " " + adresseDataSourceDomicile.numeroRue + "," + " "
                    + COTSTiersUtils.limitNpaEtranger(adresseDataSourceDomicile.localiteNpa) + " "
                    + adresseDataSourceDomicile.localiteNom + "," + " " + adresseDataSourceDomicile.paysNom;
            addRowStructureDebiteur(element, tableTextes, countRows, tmp);
        }

        countRows++;
        addRowStructureDebiteur(element, tableTextes, countRows,
                getSession().getLabel(COTSOPGEExecutor.LABEL_DEBITEUR_ETR_ART_50));

        countRows++;
        addRowStructureDebiteur(element, tableTextes, countRows,
                getSession().getLabel(COTSOPGEExecutor.LABEL_DEBITEUR_ETR_NOTIFICATION));

        if (!JadeStringUtil.isBlank(adresseDataSourceDebiteur.rue)
                || !JadeStringUtil.isBlank(adresseDataSourceDebiteur.numeroRue)) {
            countRows++;
            addRowStructureDebiteur(element, tableTextes, countRows, adresseDataSourceDebiteur.rue + " "
                    + adresseDataSourceDebiteur.numeroRue);
        }

        countRows++;
        addRowStructureDebiteur(element, tableTextes, countRows, adresseDataSourceDebiteur.localiteNpa + " "
                + adresseDataSourceDebiteur.localiteNom);

        _addTable(tableTextes);
    }

    /**
     * Ajout une ligne de zone 11.
     * 
     * @param element
     * @param tableTextes
     * @param countRows
     * @param line
     * @throws Exception
     * @throws FWIException
     */
    private void addRowStructureDebiteur(COElementJournalBatch element, FWIDocumentTable tableTextes, int countRows,
            String line) throws Exception, FWIException {
        tableTextes.addCell("" + countRows);
        tableTextes.addCell(COOPGEFileCommonPart.TYPE_STRUCTURE_TEXTE_DEBITEUR);
        tableTextes.addCell(COTSTiersUtils.convertSpecialChars(line.toUpperCase()));

        tableTextes.addRow();
    }

    /**
     * Ajout la table des créances (avec et sans intérêts).
     * 
     * @param element
     * @throws FWIException
     * @throws Exception
     */
    private void addTableCreances(COElementJournalBatch element) throws FWIException, Exception {
        this._addLine(getFontHeaderPage(), null, null, null, null, null);
        this._addLine(getFontColumn(), getSession().getLabel("RDP_TSGE_CREANCES") + " :", null, null, null, null);

        this._addLine(getFontCell(), getSession().getLabel("IMPRIMER_JOURNAL_NUM_SECTION") + " : "
                + element.getContentieux().getSection().getIdExterne(), null, null, null, null);

        if (!JadeStringUtil.isBlank(element.getContentieux().getSection().getNumeroPoursuite())) {
            this._addLine(getFontCell(), getSession().getLabel("AQUILA_NUM_POURSUITE") + " : "
                    + element.getContentieux().getSection().getNumeroPoursuite(), null, null, null, null);
        }

        FWIDocumentTable tableCreance = new FWIDocumentTable();
        tableCreance.addColumn(
                getSession().getLabel(COTSOPGEImprimerRequisitionPoursuiteJournal.LABEL_RDP_TSGE_NUMERO),
                FWITableModel.LEFT, 1);
        tableCreance.addColumn(getSession().getLabel("RDP_TSGE_TITRE_CREANCE"), FWITableModel.LEFT, 5);
        tableCreance.addColumn(getSession()
                .getLabel(COTSOPGEImprimerRequisitionPoursuiteJournal.LABEL_RDP_TSGE_MONTANT), FWITableModel.RIGHT, 1);
        tableCreance.addColumn(getSession().getLabel("RDP_TSGE_DATE_INTERETS"), FWITableModel.RIGHT, 1);
        tableCreance.endTableDefinition();

        String[] soldeInitial = CORequisitionPoursuiteUtil.getSoldeSectionInitial(getSession(), element
                .getContentieux().getIdSection());
        forNotIdJournalCreance = soldeInitial[1];
        ecrituresFromDate = soldeInitial[2];
        String montant = soldeInitial[0];
        String montantSoumis = CORequisitionPoursuiteUtil.getMontantCreanceSoumis(getTransaction(),
                element.getContentieux());
        String montantNonSoumis = (new BigDecimal(montant)).subtract(new BigDecimal(montantSoumis)).toString();

        int countRows = 1;
        if (!JadeStringUtil.isBlankOrZero(montantSoumis)) {
            tableCreance.addCell("" + countRows++);
            tableCreance
                    .addCell(COTSTiersUtils.convertSpecialChars(
                            CORequisitionPoursuiteUtil.getLibelleCreance(getSession(), element.getContentieux()))
                            .toUpperCase());
            tableCreance.addCell(montantSoumis);
            tableCreance.addCell(CORequisitionPoursuiteUtil.getDateDebutInteretsTardifs(getSession(), getTransaction(),
                    element.getContentieux()));
            tableCreance.addRow();
        }

        if (!JadeStringUtil.isBlankOrZero(montantNonSoumis)) {
            tableCreance.addCell("" + countRows++);
            tableCreance
                    .addCell(COTSTiersUtils.convertSpecialChars(
                            CORequisitionPoursuiteUtil.getLibelleCreance(getSession(), element.getContentieux()))
                            .toUpperCase());
            tableCreance.addCell(montantNonSoumis);
            tableCreance.addCell("");
            tableCreance.addRow();
        }

        addRowsCreancesSansInterets(element, tableCreance, countRows);

        _addTable(tableCreance);
    }

    /**
     * Ajoute la table d'imputations.
     * 
     * @param element
     * @throws FWIException
     * @throws Exception
     */
    private void addTableImputations(COElementJournalBatch element) throws FWIException, Exception {
        COExtraitCompteManager extraitCompteManager = COTSOPGEUtils.getExtraitCompteManager(getSession(),
                element.getContentieux(), forNotIdJournalCreance, ecrituresFromDate);

        extraitCompteManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        if (!extraitCompteManager.isEmpty()) {
            FWIDocumentTable tableImputations = null;

            int countRows = 1;
            for (int i = 0; i < extraitCompteManager.size(); ++i) {
                CAExtraitCompte extraitCompte = (CAExtraitCompte) extraitCompteManager.getEntity(i);

                FWCurrency montant = new FWCurrency(extraitCompte.getMontant());

                if (montant.isNegative() && !montant.isZero()
                        && JadeStringUtil.isIntegerEmpty(extraitCompte.getProvenancePmt())) {
                    montant.abs();

                    if (tableImputations == null) {
                        this._addLine(getFontHeaderPage(), null, null, null, null, null);
                        this._addLine(getFontColumn(), getSession().getLabel("RDP_TSGE_IMPUTATIONS") + " :", null,
                                null, null, null);

                        tableImputations = new FWIDocumentTable();
                        tableImputations.addColumn(
                                getSession()
                                        .getLabel(COTSOPGEImprimerRequisitionPoursuiteJournal.LABEL_RDP_TSGE_NUMERO),
                                FWITableModel.LEFT, 1);
                        tableImputations.addColumn(
                                getSession().getLabel(
                                        COTSOPGEImprimerRequisitionPoursuiteJournal.LABEL_RDP_TSGE_MONTANT),
                                FWITableModel.LEFT, 1);
                        tableImputations.addColumn(getSession().getLabel("RDP_TSGE_DATE_IMPUTATION"),
                                FWITableModel.LEFT, 5);
                        tableImputations.endTableDefinition();
                    }

                    tableImputations.addCell("" + countRows);
                    tableImputations.addCell(montant.toString());
                    tableImputations.addCell(extraitCompte.getDate());

                    tableImputations.addRow();

                    countRows++;
                }
            }

            if (tableImputations != null) {
                _addTable(tableImputations);
            }
        }
    }

    /**
     * Ajoute la table de remarque (zone 71).
     * 
     * @param element
     * @throws FWIException
     * @throws Exception
     */
    private void addTableRemarques(COElementJournalBatch element) throws FWIException, Exception {
        this._addLine(getFontHeaderPage(), null, null, null, null, null);
        this._addLine(getFontColumn(), getSession().getLabel("RDP_TSGE_REMARQUES") + " :", null, null, null, null);

        FWIDocumentTable tableTextes = new FWIDocumentTable();
        tableTextes.addColumn(getSession().getLabel(COTSOPGEImprimerRequisitionPoursuiteJournal.LABEL_RDP_TSGE_NUMERO),
                FWITableModel.LEFT, 1);
        tableTextes.addColumn(getSession().getLabel(COTSOPGEImprimerRequisitionPoursuiteJournal.LABEL_RDP_TSGE_CODE),
                FWITableModel.LEFT, 1);
        tableTextes.addColumn(
                getSession().getLabel(COTSOPGEImprimerRequisitionPoursuiteJournal.LABEL_RDP_TSGE_TEXTE_LIBRE),
                FWITableModel.LEFT, 5);
        tableTextes.endTableDefinition();

        addRowsRemarqueDebiteur(element, tableTextes);

        _addTable(tableTextes);
    }

    /**
     * Ajout le texte pour le tiers (zone 11). <br/>
     * 1. Complément de requisition dans les tiers <br/>
     * 2. Si non présent => Automatique pour élément étrangé ou tronqué.
     * 
     * @param element
     * @throws FWIException
     * @throws Exception
     */
    private void addTableTextes(COElementJournalBatch element) throws FWIException, Exception {
        TIAdresseDataSource adresseDataSourceComplementPoursuite = COServiceLocator.getTiersService()
                .getAdresseDataSourceComplementRDP(getSession(), element.getContentieux().getCompteAnnexe().getTiers(),
                        element.getContentieux().getCompteAnnexe().getIdExterneRole(), JACalendar.today().toString());
        if (adresseDataSourceComplementPoursuite != null) {
            addRowsStructureDebiteur(element, adresseDataSourceComplementPoursuite);
        } else {
            TIAdresseDataSource adresseDataSourceDomicile = COServiceLocator.getTiersService()
                    .getAdresseDataSourceDomicileStandard(getSession(),
                            element.getContentieux().getCompteAnnexe().getTiers(),
                            element.getContentieux().getCompteAnnexe().getIdExterneRole(),
                            JACalendar.today().toString());
            TIAdresseDataSource adresseDataSourceDebiteur = COServiceLocator.getTiersService().getAdresseDataSource(
                    getSession(), element.getContentieux().getCompteAnnexe().getTiers(),
                    element.getContentieux().getCompteAnnexe().getIdExterneRole(), JACalendar.today().toString());

            if ((adresseDataSourceDomicile != null) && !TIPays.CODE_ISO_CH.equals(adresseDataSourceDomicile.paysIso)) {
                addRowsStructureDebiteurEtranger(element, adresseDataSourceDomicile, adresseDataSourceDebiteur);
            } else if (COTSTiersUtils.needTexteDuDebiteurAutomatique(getSession(), element.getContentieux(),
                    adresseDataSourceDebiteur)) {
                addRowsStructureDebiteur(element, adresseDataSourceDebiteur);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList#afterAdd(globaz.globall.db.BEntity)
     */
    @Override
    protected void afterAdd(BEntity entity) throws FWIException {
        COApplication app = null;
        IFormatData affilieFormater = null;
        try {
            app = (COApplication) GlobazSystem.getApplication(ICOApplication.DEFAULT_APPLICATION_AQUILA);
            affilieFormater = app.getAffileFormater();
        } catch (Exception e1) {
            getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("ERROR_UNFORMATING_NUM_AFFILIE"));
        }
        try {
            // On rempli le documentInfo de l'enfant avec les infos du document
            if (getDocumentInfo() == null) {
                return;
            }
            JadePublishDocumentInfo childDocumentInfo = getDocumentInfo().createCopy();
            // HACK sel : le createCopy() copie également les children se qui génère un OutOfMemory.
            childDocumentInfo.setChildren(null);
            getDocumentInfo().addChild(childDocumentInfo);
            childDocumentInfo.setNumberOfPages(1);
            childDocumentInfo.setPublishDocument(true);
            childDocumentInfo.setArchiveDocument(true);
            TIDocumentInfoHelper.fill(
                    childDocumentInfo,
                    getElement().getContentieux().getSection().getCompteAnnexe().getIdTiers(),
                    getSession(),
                    getElement().getContentieux().getSection().getCompteAnnexe().getIdRole(),
                    getElement().getContentieux().getSection().getCompteAnnexe().getIdExterneRole(),
                    affilieFormater.unformat(getElement().getContentieux().getSection().getCompteAnnexe()
                            .getIdExterneRole()));
            CADocumentInfoHelper.fill(childDocumentInfo, getElement().getContentieux().getSection());
            childDocumentInfo.setDocumentProperty("babel.type.id", "CTX");
            childDocumentInfo.setDocumentProperty("aquila.contentieux.idContentieux", getElement().getContentieux()
                    .getIdContentieux());
            childDocumentInfo.setDocumentProperty("annee", getAnneeFromContentieux());
        } catch (Exception e) {
            JadeLogger.warn(this, e);
        }
    }

    /**
     * permet de trouver l'année pour l'idExterne d'une section (utilisé par ex. par la ligne technique à la FER)
     */
    protected String getAnneeFromContentieux() {
        String annee = "";
        if ((getElement().getContentieux() != null) && (getElement().getContentieux().getSection() != null)) {
            if (getElement().getContentieux().getSection().getIdExterne().length() >= 4) {
                annee = getElement().getContentieux().getSection().getIdExterne().substring(0, 4);
            }
        }
        return annee;
    }

    /**
     * @return the element
     */
    public COElementJournalBatch getElement() {
        return element;
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    /**
     * Return la déclaration de la table de textes.
     * 
     * @return
     * @throws FWIException
     */
    private FWIDocumentTable getTableTextesDeclaration() throws FWIException {
        this._addLine(getFontHeaderPage(), null, null, null, null, null);
        this._addLine(getFontColumn(), getSession().getLabel("RDP_TSGE_TEXTES") + " :", null, null, null, null);

        FWIDocumentTable tableTextes = new FWIDocumentTable();
        tableTextes.addColumn(getSession().getLabel(COTSOPGEImprimerRequisitionPoursuiteJournal.LABEL_RDP_TSGE_NUMERO),
                FWITableModel.LEFT, 1);
        tableTextes.addColumn(getSession().getLabel(COTSOPGEImprimerRequisitionPoursuiteJournal.LABEL_RDP_TSGE_CODE),
                FWITableModel.LEFT, 1);
        tableTextes.addColumn(
                getSession().getLabel(COTSOPGEImprimerRequisitionPoursuiteJournal.LABEL_RDP_TSGE_TEXTE_LIBRE),
                FWITableModel.LEFT, 5);
        tableTextes.endTableDefinition();
        return tableTextes;
    }

    @Override
    protected void initializeTable() {
        // Declaration not used
        this._addColumnCenter("");
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * @param element the element to set
     */
    public void setElement(COElementJournalBatch element) {
        this.element = element;
    }

    public void setForIdJournal(String forIdJournal) {
        this.forIdJournal = forIdJournal;
    }

}
