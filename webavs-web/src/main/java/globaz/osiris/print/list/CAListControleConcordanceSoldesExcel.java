package globaz.osiris.print.list;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.controleConcordanceSoldes.CAControleConcordanceSoldeCASEExcel;
import globaz.osiris.db.controleConcordanceSoldes.CAControleConcordanceSoldeCASEExcelManager;
import globaz.osiris.db.controleConcordanceSoldes.CAControleConcordanceSoldeSEOPExcel;
import globaz.osiris.db.controleConcordanceSoldes.CAControleConcordanceSoldeSEOPExcelManager;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * Crée la liste des dossiers par étape
 * 
 * @author sch
 */
public class CAListControleConcordanceSoldesExcel extends CAAbstractListExcel {

    private static final String NUMERO_REFERENCE_INFOROM = "0301GCA";
    private BProcess process = null;

    // créé la feuille xls
    public CAListControleConcordanceSoldesExcel(BSession session, BProcess process) {
        super(session, session.getLabel("LISTE_CONTROLE_CONCORDANCE_SOLDE_EXCEL"), session
                .getLabel("LISTE_CONTROLE_CONCORDANCE_SOLDE_EXCEL"));
        this.process = process;
    }

    @Override
    public String getNumeroInforom() {
        return CAListControleConcordanceSoldesExcel.NUMERO_REFERENCE_INFOROM;
    }

    /**
     * @return the process
     */
    public BProcess getProcess() {
        return process;
    }

    /**
     * Création de la page
     * 
     * @return
     */
    private void initListeCompteAnnexeSections() {
        // Titres des colonnes
        ArrayList colTitles = new ArrayList();
        colTitles.add(new ParamTitle(getSession().getLabel("ROLE"), getStyleListTitleLeft()));
        colTitles.add(new ParamTitle(getSession().getLabel("GCA2034_IDEXTERNEROLE"), getStyleListTitleLeft()));
        colTitles.add(new ParamTitle(getSession().getLabel("GCA2034_SOLDE_COMPTE_ANNEXE"), getStyleListTitleRight()));
        colTitles.add(new ParamTitle(getSession().getLabel("GCA2034_CUMUL_SECTIONS"), getStyleListTitleRight()));

        createSheet(getSession().getLabel("GCA2034_COMPTEANNEXES_SECTIONS"));

        // this.initCritere();
        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(CAListControleConcordanceSoldesExcel.NUMERO_REFERENCE_INFOROM);

        // définition de la taille des cell
        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, (short) 6000); // Role
        currentSheet.setColumnWidth((short) numCol++, (short) 6000); // IdExterneRole
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_MONTANT); // Solde compte
                                                                                                 // annexe
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_MONTANT); // cumul solde
                                                                                                 // sections
    }

    private void initListeSectionOperations() {
        // Titres des colonnes
        ArrayList colTitles = new ArrayList();
        colTitles.add(new ParamTitle(getSession().getLabel("ROLE"), getStyleListTitleLeft()));
        colTitles.add(new ParamTitle(getSession().getLabel("GCA2034_IDCOMPTEANNEXE"), getStyleListTitleRight()));
        colTitles.add(new ParamTitle(getSession().getLabel("GCA2034_IDEXTERNEROLE"), getStyleListTitleRight()));
        colTitles.add(new ParamTitle(getSession().getLabel("GCA2034_IDSECTION"), getStyleListTitleRight()));
        colTitles.add(new ParamTitle(getSession().getLabel("GCA2034_IDTYPESECTION"), getStyleListTitleRight()));
        colTitles.add(new ParamTitle(getSession().getLabel("GCA2034_IDEXTERNE"), getStyleListTitleLeft()));
        colTitles.add(new ParamTitle(getSession().getLabel("GCA2034_SOLDE_SECTION"), getStyleListTitleRight()));
        colTitles.add(new ParamTitle(getSession().getLabel("GCA2034_CUMUL_OPERATIONS"), getStyleListTitleRight()));

        createSheet(getSession().getLabel("GCA2034_SECTIONS_OPERATIONS"));

        // this.initCritere();
        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(CAListControleConcordanceSoldesExcel.NUMERO_REFERENCE_INFOROM);

        // définition de la taille des cell
        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, (short) 6000); // Role
        currentSheet.setColumnWidth((short) numCol++, (short) 6000); // IdCompteAnnexe
        currentSheet.setColumnWidth((short) numCol++, (short) 6000); // IdExterneRole
        currentSheet.setColumnWidth((short) numCol++, (short) 6000); // IdSection
        currentSheet.setColumnWidth((short) numCol++, (short) 6000); // IdTypeSection
        currentSheet.setColumnWidth((short) numCol++, (short) 6000); // IdExterne
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_MONTANT); // Solde compte
                                                                                                 // annexe
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_MONTANT); // cumul solde
                                                                                                 // sections
    }

    /**
     * initialisation de la feuille xls pour comptes annexes <--> sections
     */
    public HSSFSheet populateSheetListe(CAControleConcordanceSoldeCASEExcelManager managerCompteannexeSections,
            BTransaction transaction) throws Exception {
        BStatement statement = managerCompteannexeSections.cursorOpen(transaction);
        CAControleConcordanceSoldeCASEExcel concordanceSoldeCompteAnnexeSection = null;

        // création de la page
        initListeCompteAnnexeSections();
        while (((concordanceSoldeCompteAnnexeSection = (CAControleConcordanceSoldeCASEExcel) managerCompteannexeSections
                .cursorReadNext(statement)) != null)
                && (!concordanceSoldeCompteAnnexeSection.isNew())
                && !getProcess().isAborted()) {
            // parcours du manager et remplissage des cell
            if (concordanceSoldeCompteAnnexeSection != null) {
                // Recherche du compte annexe pour obtenir le motif de blocage
                // CACompteAnnexe compte = (CACompteAnnexe)
                // recap.getCompteAnnexe();

                createRow();
                this.createCell(concordanceSoldeCompteAnnexeSection.getRoleDescription(),
                        getStyleListVerticalAlignTopLeft()); // Role
                this.createCell(concordanceSoldeCompteAnnexeSection.getIdExterneRole(),
                        getStyleListVerticalAlignTopLeft()); // Compte annexe
                this.createCell(
                        JadeStringUtil.parseDouble(concordanceSoldeCompteAnnexeSection.getSoldeCompteAnnexe(), 0),
                        false); // Solde compte annexe
                this.createCell(
                        JadeStringUtil.parseDouble(concordanceSoldeCompteAnnexeSection.getSoldeCumulSections(), 0),
                        false); // Cumul soldes sections
            }
            getProcess().incProgressCounter();
        }
        return currentSheet;
    }

    /**
     * initialisation de la feuille xls pour sections <--> operations
     */
    public HSSFSheet populateSheetListe(CAControleConcordanceSoldeSEOPExcelManager managerSectionOperations,
            BTransaction transaction) throws Exception {
        BStatement statement = managerSectionOperations.cursorOpen(transaction);
        CAControleConcordanceSoldeSEOPExcel concordanceSoldeSectionOperations = null;

        // création de la page
        initListeSectionOperations();
        while (((concordanceSoldeSectionOperations = (CAControleConcordanceSoldeSEOPExcel) managerSectionOperations
                .cursorReadNext(statement)) != null)
                && (!concordanceSoldeSectionOperations.isNew())
                && !getProcess().isAborted()) {
            // parcours du manager et remplissage des cell
            if (concordanceSoldeSectionOperations != null) {
                // Recherche du compte annexe pour obtenir le motif de blocage
                // CACompteAnnexe compte = (CACompteAnnexe)
                // recap.getCompteAnnexe();

                createRow();
                this.createCell(concordanceSoldeSectionOperations.getRoleDescription(),
                        getStyleListVerticalAlignTopLeft()); // Role
                this.createCell(concordanceSoldeSectionOperations.getIdCompteAnnexe(),
                        getStyleListVerticalAlignTopRight()); // idCompteAnnexe
                this.createCell(concordanceSoldeSectionOperations.getIdExterneRole(),
                        getStyleListVerticalAlignTopLeft()); // idExterneRole
                this.createCell(concordanceSoldeSectionOperations.getIdSection(), getStyleListVerticalAlignTopRight()); // idSection
                this.createCell(concordanceSoldeSectionOperations.getIdTypeSection(),
                        getStyleListVerticalAlignTopRight()); // type section
                this.createCell(concordanceSoldeSectionOperations.getIdExterne(), getStyleListVerticalAlignTopLeft()); // idExterne
                this.createCell(JadeStringUtil.parseDouble(concordanceSoldeSectionOperations.getSoldeSection(), 0),
                        false); // Solde
                                // compte
                                // annexe
                this.createCell(
                        JadeStringUtil.parseDouble(concordanceSoldeSectionOperations.getSoldeCumulOperations(), 0),
                        false); // Cumul
                                // soldes
                                // sections
            }
            getProcess().incProgressCounter();
        }
        return currentSheet;
    }

    /**
     * @param process
     *            the process to set
     */
    public void setProcess(BProcess process) {
        this.process = process;
    }

}
