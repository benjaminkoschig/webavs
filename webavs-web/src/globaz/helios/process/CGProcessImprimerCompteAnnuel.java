package globaz.helios.process;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.application.CGApplication;
import globaz.helios.db.avs.CGSecteurAVS;
import globaz.helios.db.avs.CGSecteurAVSManager;
import globaz.helios.db.comptes.CGBilanListViewBean;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGComptePertesProfitsListViewBean;
import globaz.helios.db.comptes.CGExerciceComptableViewBean;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPeriodeComptableManager;
import globaz.helios.parser.CGBilanLine;
import globaz.helios.parser.CGBilanParser;
import globaz.helios.parser.CGComptePertesProfitsLine;
import globaz.helios.parser.CGComptePertesProfitsParser;
import globaz.helios.process.consolidation.utils.CGImprimerConsolidationUtils;
import globaz.helios.process.consolidation.utils.CGProcessConsolidationUtil;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

public class CGProcessImprimerCompteAnnuel extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final int INDEX_COLUMN_IDEXTERNE = 0;

    private static final int INDEX_COLUMN_LIBELLE = 1;
    private static final int INDEX_COLUMN_SOLDE_A = 2;
    private static final int INDEX_COLUMN_SOLDE_B = 3;
    private static final String NUMERO_REFERENCE_INFOROM = "0057GCF";

    private static final String OUTPUT_FILE_NAME = "compte_annuel";
    private static final String OUTPUT_FILE_WORK_DIR = "work";

    private String idExerciceComptable;

    /**
     * Constructor for CGProcessImprimerCompteAnnuelConsolide.
     */
    public CGProcessImprimerCompteAnnuel() throws Exception {
        this(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }

    /**
     * Constructor for CGProcessImprimerCompteAnnuelConsolide.
     * 
     * @param parent
     */
    public CGProcessImprimerCompteAnnuel(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CGProcessImprimerCompteAnnuelConsolide.
     * 
     * @param session
     */
    public CGProcessImprimerCompteAnnuel(BSession session) throws Exception {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
        // Do nothing.
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        CGExerciceComptableViewBean exerciceComptable = CGProcessConsolidationUtil.getExerciceComptable(getSession(),
                getTransaction(), getIdExerciceComptable());

        HSSFWorkbook wb = new HSSFWorkbook();

        createSheetForBilan(wb, exerciceComptable);
        createSheetForPertesProfits(wb, exerciceComptable, CGCompte.CS_COMPTE_EXPLOITATION);
        createSheetForPertesProfits(wb, exerciceComptable, CGCompte.CS_COMPTE_ADMINISTRATION);

        String outputFileName = Jade.getInstance().getHomeDir() + CGProcessImprimerCompteAnnuel.OUTPUT_FILE_WORK_DIR
                + "/" + CGProcessImprimerCompteAnnuel.OUTPUT_FILE_NAME + System.currentTimeMillis() + ".xls";
        CGImprimerConsolidationUtils.createFile(wb, outputFileName);

        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(CGProcessImprimerCompteAnnuel.NUMERO_REFERENCE_INFOROM);

        this.registerAttachedDocument(docInfo, outputFileName);

        return true;
    }

    /**
     * Ajout d'une ligne blanche dans le contenu du tableau.
     * 
     * @param wb
     * @param sheet
     * @param countLine
     */
    private void addBlankRow(HSSFWorkbook wb, HSSFSheet sheet, int countLine) {
        HSSFRow row = sheet.createRow((short) countLine);

        HSSFCellStyle styleColumnLeft = wb.createCellStyle();
        styleColumnLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);

        HSSFCellStyle styleColumnRight = wb.createCellStyle();
        styleColumnRight.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);

        HSSFCell cell = row.createCell((short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_IDEXTERNE);
        cell.setCellValue("");
        cell.setCellStyle(styleColumnLeft);

        cell = row.createCell((short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_SOLDE_A);
        cell.setCellValue("");
        cell.setCellStyle(styleColumnLeft);

        cell = row.createCell((short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_SOLDE_B);
        cell.setCellValue("");
        cell.setCellStyle(styleColumnRight);
    }

    /**
     * Ajout du contenu des comptes du bilan. Création du tableau.
     * 
     * @param wb
     * @param sheet
     * @param exerciceComptable
     * @throws Exception
     */
    private void addContentToBilan(HSSFWorkbook wb, HSSFSheet sheet, CGExerciceComptableViewBean exerciceComptable)
            throws Exception {
        CGBilanListViewBean listManager = new CGBilanListViewBean();
        listManager.setSession(getSession());

        listManager.setForSoldeOpen(true);
        listManager.setForIdExerciceComptable(getIdExerciceComptable());
        listManager.setReqComptabilite(CodeSystem.CS_DEFINITIF);
        listManager.setInclurePeriodesPrec(new Boolean(true));
        listManager.setGroupIdCompteOfas(true);
        listManager.setReqDomaine(CGCompte.CS_COMPTE_BILAN);

        listManager.setBeginWithIdExterneListIn(CGProcessConsolidationUtil.getSecteurExportable());

        listManager.setReqForListPeriodesComptable(getLastIdPeriode());

        listManager.find(BManager.SIZE_NOLIMIT);

        ArrayList lines = CGBilanParser.getLinesToPrint(listManager, exerciceComptable, true);
        String lastIdSecteurAVS = "";
        int countLine = 0;

        addRowContentLibelleColumns(wb, sheet, countLine,
                CodeSystem.getLibelle(getSession(), CGCompte.CS_COMPTE_BILAN),
                getSession().getLabel("IMPRESSION_RELEVEAVS_ACTIF"),
                getSession().getLabel("IMPRESSION_RELEVEAVS_PASSIF"));
        countLine++;

        FWCurrency totalActifs = new FWCurrency();
        FWCurrency totalPassifs = new FWCurrency();

        for (int i = 0; i < lines.size(); i++) {
            CGBilanLine line = (CGBilanLine) lines.get(i);

            if (line != null) {
                if ((!JadeStringUtil.isBlank(line.getIdExterne()))
                        && (!line.getIdExterne().substring(0, line.getIdExterne().indexOf("."))
                                .equals(lastIdSecteurAVS))) {
                    lastIdSecteurAVS = addRowContentSecteurAVS(wb, sheet, countLine, exerciceComptable,
                            line.getIdExterne());
                    countLine++;
                }

                addRowContent(wb, sheet, countLine, line.getIdExterne(), line.getLibelle(), line.getSoldeActif(),
                        line.getSoldePassif());

                if (!JadeStringUtil.isBlank(line.getIdExterne())) {
                    if (!JadeStringUtil.isBlankOrZero(line.getSoldeActif())) {
                        FWCurrency soldeActif = new FWCurrency(line.getSoldeActif());
                        if (soldeActif.isPositive()) {
                            totalActifs.add(JANumberFormatter.deQuote(soldeActif.toString()));
                        } else {
                            FWCurrency soldeActifAbs = new FWCurrency(soldeActif.toString());
                            soldeActifAbs.abs();
                            totalPassifs.add(JANumberFormatter.deQuote(soldeActifAbs.toString()));
                        }
                    }
                    if (!JadeStringUtil.isBlankOrZero(line.getSoldePassif())) {
                        FWCurrency soldePassif = new FWCurrency(line.getSoldePassif());
                        if (soldePassif.isPositive()) {
                            totalPassifs.add(JANumberFormatter.deQuote(soldePassif.toString()));
                        } else {
                            FWCurrency soldePassifAbs = new FWCurrency(soldePassif.toString());
                            soldePassifAbs.abs();
                            totalActifs.add(JANumberFormatter.deQuote(soldePassifAbs.toString()));
                        }
                    }
                }

                if (JadeStringUtil.isBlank(line.getIdExterne())
                        && (line.getLibelle().equals(getSession().getLabel("PARSER_RESULTAT"))
                                || line.getLibelle().equals(getSession().getLabel("PARSER_BENEFICE")) || needBlankForTotauxBilan(line))) {
                    countLine++;
                    addBlankRow(wb, sheet, countLine);
                }

                countLine++;
            }
        }

        addRowContentTotal(wb, sheet, countLine, totalActifs.toString(), totalPassifs.toString());
    }

    /**
     * Ajout du contenu des comptes de pertes et profits. Création du tableau.
     * 
     * @param wb
     * @param sheet
     * @param exerciceComptable
     * @param domaine
     * @throws Exception
     */
    private void addContentToPertesProfits(HSSFWorkbook wb, HSSFSheet sheet,
            CGExerciceComptableViewBean exerciceComptable, String domaine) throws Exception {
        CGComptePertesProfitsListViewBean listManager = new CGComptePertesProfitsListViewBean();
        listManager.setSession(getSession());

        listManager.setForSoldeOpen(true);
        listManager.setForIdExerciceComptable(getIdExerciceComptable());
        listManager.setReqComptabilite(CodeSystem.CS_DEFINITIF);
        listManager.setInclurePeriodesPrec(new Boolean(true));
        listManager.setGroupIdCompteOfas(true);
        listManager.setReqDomaine(domaine);

        listManager.setBeginWithIdExterneListIn(CGProcessConsolidationUtil.getSecteurExportable());

        listManager.setReqForListPeriodesComptable(getLastIdPeriode());

        listManager.find(BManager.SIZE_NOLIMIT);

        ArrayList lines = CGComptePertesProfitsParser.getLinesToPrint(listManager, exerciceComptable);
        String lastIdSecteurAVS = "";
        int countLine = 0;

        addRowContentLibelleColumns(wb, sheet, countLine, CodeSystem.getLibelle(getSession(), domaine), getSession()
                .getLabel("IMPRESSION_RELEVEAVS_CHARGE"), getSession().getLabel("IMPRESSION_RELEVEAVS_PRODUIT"));
        countLine++;

        FWCurrency totalCharges = new FWCurrency();
        FWCurrency totalProduits = new FWCurrency();

        for (int i = 0; i < lines.size(); i++) {
            CGComptePertesProfitsLine line = (CGComptePertesProfitsLine) lines.get(i);

            if (line != null) {
                if ((!JadeStringUtil.isBlank(line.getIdExterne()))
                        && (!line.getIdExterne().substring(0, line.getIdExterne().indexOf("."))
                                .equals(lastIdSecteurAVS))) {
                    lastIdSecteurAVS = addRowContentSecteurAVS(wb, sheet, countLine, exerciceComptable,
                            line.getIdExterne());
                    countLine++;
                }

                addRowContent(wb, sheet, countLine, line.getIdExterne(), line.getLibelle(), line.getSoldeCharges(),
                        line.getSoldeProduits());

                if (!JadeStringUtil.isBlank(line.getIdExterne())) {
                    totalCharges.add(JANumberFormatter.deQuote(line.getSoldeCharges()));
                    totalProduits.add(JANumberFormatter.deQuote(line.getSoldeProduits()));
                }

                if (JadeStringUtil.isBlank(line.getIdExterne())
                        && (line.getLibelle().equals(getSession().getLabel("PARSER_RESULTAT")) || needBlankForTotauxPertesEtProtifs(line))) {
                    countLine++;
                    addBlankRow(wb, sheet, countLine);
                }

                countLine++;
            }
        }

        addRowContentTotal(wb, sheet, countLine, totalCharges.toString(), totalProduits.toString());
    }

    /**
     * Ajout d'un header et d'un footer au document excel.
     * 
     * @param sheet
     * @param exerciceComptable
     */
    private void addHeaderFooter(HSSFSheet sheet, CGExerciceComptableViewBean exerciceComptable) {
        HSSFHeader header = sheet.getHeader();
        header.setLeft(exerciceComptable.getMandat().getLibelle());

        header.setCenter(getSession().getLabel("CONSOLIDATION_COMPTE_ANNUEL") + " "
                + exerciceComptable.getDateDebut().substring(exerciceComptable.getDateDebut().lastIndexOf(".") + 1));
        header.setRight(getSession().getLabel("DATE") + " : " + JACalendar.todayJJsMMsAAAA());

        HSSFFooter footer = sheet.getFooter();
        footer.setRight(getSession().getLabel("PAGE") + " : " + HSSFFooter.page() + "/" + HSSFFooter.numPages());
        footer.setLeft(getSession().getLabel("REFERENCE") + " : "
                + CGProcessImprimerCompteAnnuel.NUMERO_REFERENCE_INFOROM);
    }

    /**
     * Ajout d'un ligne de contenu au tableau contenant idExterne, libelle, solde (actifs ou charges) et solde (passifs
     * ou produits).
     * 
     * @param wb
     * @param sheet
     * @param countLine
     * @param idExterne
     * @param libelle
     * @param soldeA
     * @param soldeB
     */
    private void addRowContent(HSSFWorkbook wb, HSSFSheet sheet, int countLine, String idExterne, String libelle,
            String soldeA, String soldeB) {
        HSSFRow row = sheet.createRow((short) countLine);

        HSSFCellStyle styleColumnLeft = wb.createCellStyle();
        styleColumnLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);

        HSSFCellStyle styleColumnSoldeB = wb.createCellStyle();
        styleColumnSoldeB.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
        styleColumnSoldeB.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnSoldeB.setBorderRight(HSSFCellStyle.BORDER_THIN);

        HSSFCellStyle styleColumnSoldeA = wb.createCellStyle();
        styleColumnSoldeA.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
        styleColumnSoldeA.setBorderLeft(HSSFCellStyle.BORDER_THIN);

        HSSFCell cell = row.createCell((short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_IDEXTERNE);
        cell.setCellValue(idExterne);
        cell.setCellStyle(styleColumnLeft);

        cell = row.createCell((short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_LIBELLE);
        cell.setCellValue(libelle);

        FWCurrency bdA = new FWCurrency(soldeA);
        FWCurrency bdB = new FWCurrency(soldeB);
        if (bdA.isNegative() || bdB.isNegative()) {
            bdB.abs();
            soldeA = bdB.toString();
            bdA.abs();
            soldeB = bdA.toString();
        }

        cell = row.createCell((short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_SOLDE_A);
        if (!JadeStringUtil.isBlankOrZero(soldeA)) {
            cell.setCellValue(Double.parseDouble(JANumberFormatter.deQuote(soldeA)));
        } else {
            cell.setCellValue("");
        }
        cell.setCellStyle(styleColumnSoldeA);

        cell = row.createCell((short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_SOLDE_B);
        if (!JadeStringUtil.isBlankOrZero(soldeB)) {
            cell.setCellValue(Double.parseDouble(JANumberFormatter.deQuote(soldeB)));
        } else {
            cell.setCellValue("");
        }
        cell.setCellStyle(styleColumnSoldeB);
    }

    /**
     * Ajout de la ligne contenant les libellés des colonnes.
     * 
     * @param wb
     * @param sheet
     * @param countLine
     * @param libelle
     * @param libelleSoldeA
     * @param libelleSoldeB
     */
    private void addRowContentLibelleColumns(HSSFWorkbook wb, HSSFSheet sheet, int countLine, String libelle,
            String libelleSoldeA, String libelleSoldeB) {
        HSSFRow row = sheet.createRow((short) countLine);

        HSSFCellStyle styleColumnLeft = wb.createCellStyle();
        styleColumnLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        HSSFCellStyle styleColumn = wb.createCellStyle();
        styleColumn.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleColumn.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        HSSFCellStyle styleColumnRight = wb.createCellStyle();
        styleColumnRight.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnRight.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleColumnRight.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        sheet.addMergedRegion(new Region(countLine, (short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_IDEXTERNE,
                countLine, (short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_LIBELLE));
        HSSFCell cell = row.createCell((short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_IDEXTERNE);
        cell.setCellValue(libelle);
        cell.setCellStyle(styleColumnLeft);

        cell = row.createCell((short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_LIBELLE);
        cell.setCellValue("");
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_SOLDE_A);
        cell.setCellValue(libelleSoldeA);
        cell.setCellStyle(styleColumnRight);

        cell = row.createCell((short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_SOLDE_B);
        cell.setCellValue(libelleSoldeB);
        cell.setCellStyle(styleColumnRight);
    }

    /**
     * Ajout de la ligne de renseignement sur le secteur AVS. (Exp. : 100 Disponibilités)
     * 
     * @param wb
     * @param sheet
     * @param countLine
     * @param exerciceComptable
     * @param idExterne
     * @return
     * @throws Exception
     */
    private String addRowContentSecteurAVS(HSSFWorkbook wb, HSSFSheet sheet, int countLine,
            CGExerciceComptableViewBean exerciceComptable, String idExterne) throws Exception {
        HSSFRow row = sheet.createRow((short) countLine);

        CGSecteurAVS secteurAVS = getSecteurAVS(exerciceComptable, idExterne);

        HSSFCellStyle styleColumnLeft = wb.createCellStyle();
        styleColumnLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);

        HSSFCellStyle styleColumnRight = wb.createCellStyle();
        styleColumnRight.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);

        HSSFCell cell = row.createCell((short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_IDEXTERNE);
        cell.setCellValue(secteurAVS.getIdSecteurAVS().substring(0, 3));
        cell.setCellStyle(styleColumnLeft);

        cell = row.createCell((short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_LIBELLE);
        cell.setCellValue(secteurAVS.getLibelle());

        cell = row.createCell((short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_SOLDE_A);
        cell.setCellValue("");
        cell.setCellStyle(styleColumnLeft);

        cell = row.createCell((short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_SOLDE_B);
        cell.setCellValue("");
        cell.setCellStyle(styleColumnRight);

        return secteurAVS.getIdSecteurAVS().substring(0, 3);

    }

    /**
     * Ajout de la ligne contenant les totaux du tableau.
     * 
     * @param wb
     * @param sheet
     * @param countLine
     * @param soldeA
     * @param soldeB
     */
    private void addRowContentTotal(HSSFWorkbook wb, HSSFSheet sheet, int countLine, String soldeA, String soldeB) {
        HSSFRow row = sheet.createRow((short) countLine);

        HSSFCellStyle styleColumnLeft = wb.createCellStyle();
        styleColumnLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);

        HSSFCellStyle styleColumn = wb.createCellStyle();
        styleColumn.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleColumn.setBorderTop(HSSFCellStyle.BORDER_THIN);

        HSSFCellStyle styleColumnSoldeA = wb.createCellStyle();
        styleColumnSoldeA.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
        styleColumnSoldeA.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnSoldeA.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleColumnSoldeA.setBorderTop(HSSFCellStyle.BORDER_THIN);

        HSSFCellStyle styleColumnSoldeB = wb.createCellStyle();
        styleColumnSoldeB.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
        styleColumnSoldeB.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnSoldeB.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnSoldeB.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleColumnSoldeB.setBorderTop(HSSFCellStyle.BORDER_THIN);

        HSSFCell cell = row.createCell((short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_IDEXTERNE);
        cell.setCellValue("");
        cell.setCellStyle(styleColumnLeft);

        cell = row.createCell((short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_LIBELLE);
        cell.setCellValue(getSession().getLabel("PARSER_TOTAUX"));
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_SOLDE_A);
        cell.setCellValue(Double.parseDouble(soldeA));
        cell.setCellStyle(styleColumnSoldeA);

        cell = row.createCell((short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_SOLDE_B);
        cell.setCellValue(Double.parseDouble(soldeB));
        cell.setCellStyle(styleColumnSoldeB);
    }

    /**
     * Créer la feuille excel contenant les comptes du bilan.
     * 
     * @param wb
     * @param exerciceComptable
     * @throws Exception
     */
    private void createSheetForBilan(HSSFWorkbook wb, CGExerciceComptableViewBean exerciceComptable) throws Exception {
        HSSFSheet sheet = wb.createSheet(CodeSystem.getLibelle(getSession(), CGCompte.CS_COMPTE_BILAN));
        addHeaderFooter(sheet, exerciceComptable);

        HSSFPrintSetup psa = sheet.getPrintSetup();
        psa.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);

        sheet.setColumnWidth((short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_LIBELLE, (short) (45 * 256));
        CGImprimerConsolidationUtils.setContentColumnWidth(sheet, CGProcessImprimerCompteAnnuel.INDEX_COLUMN_SOLDE_A,
                CGProcessImprimerCompteAnnuel.INDEX_COLUMN_SOLDE_B);

        addContentToBilan(wb, sheet, exerciceComptable);
    }

    /**
     * Créer la feuille excel pour les comptes de pertes et profits du domaine passé en paramètre.
     * 
     * @param wb
     * @param exerciceComptable
     * @param domaine
     * @throws Exception
     */
    private void createSheetForPertesProfits(HSSFWorkbook wb, CGExerciceComptableViewBean exerciceComptable,
            String domaine) throws Exception {
        HSSFSheet sheet = wb.createSheet(CodeSystem.getLibelle(getSession(), domaine));
        addHeaderFooter(sheet, exerciceComptable);

        HSSFPrintSetup psa = sheet.getPrintSetup();
        psa.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);

        sheet.setColumnWidth((short) CGProcessImprimerCompteAnnuel.INDEX_COLUMN_LIBELLE, (short) (45 * 256));
        CGImprimerConsolidationUtils.setContentColumnWidth(sheet, CGProcessImprimerCompteAnnuel.INDEX_COLUMN_SOLDE_A,
                CGProcessImprimerCompteAnnuel.INDEX_COLUMN_SOLDE_B);

        addContentToPertesProfits(wb, sheet, exerciceComptable, domaine);
    }

    /**
     * Renvois le sujet de l'email.
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
            return getSession().getLabel("CONSOLIDATION_IMPR_COMPTE_ANNUEL_ERROR");
        } else {
            return getSession().getLabel("CONSOLIDATION_IMPR_COMPTE_ANNUEL_OK");
        }
    }

    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * Retrouve la dernière période dont le code != 99.
     * 
     * @return Throw error si aucune période résolue.
     * @throws Exception
     */
    private String getLastIdPeriode() throws Exception {
        CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
        manager.setSession(getSession());

        manager.setForIdExerciceComptable(getIdExerciceComptable());
        manager.setOrderBy(CGPeriodeComptableManager.TRI_DATE_FIN_CODE_DESC);

        manager.find(BManager.SIZE_NOLIMIT);

        if (manager.isEmpty()) {
            throw new Exception(getSession().getLabel("AUCUNE_PERIODE_RESOLUE"));
        }

        for (int i = 0; i < manager.size(); i++) {
            CGPeriodeComptable periode = (CGPeriodeComptable) manager.get(i);

            if (!periode.getCode().equals(CGPeriodeComptable.CS_CODE_CLOTURE)) {
                return periode.getIdPeriodeComptable();
            }
        }

        throw new Exception(getSession().getLabel("AUCUNE_PERIODE_RESOLUE"));
    }

    /**
     * Retourne le secteur avs en fonction de l'idexterne d'un compte.
     * 
     * @param exerciceComptable
     * @param idExterneSrc
     * @return
     * @throws Exception
     */
    private CGSecteurAVS getSecteurAVS(CGExerciceComptableViewBean exerciceComptable, String idExterneSrc)
            throws Exception {
        CGSecteurAVSManager manager = new CGSecteurAVSManager();
        manager.setSession(getSession());

        manager.setForIdMandat(exerciceComptable.getIdMandat());
        manager.setFromSecteur(idExterneSrc.substring(0, idExterneSrc.indexOf(".")) + "0");

        manager.find();

        return (CGSecteurAVS) manager.getFirstEntity();
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * True si le libelle de l'entité est "totaux" et si les montants sont identiques (=> aucune ligne de résultat).
     * 
     * @param line
     * @return
     */
    private boolean needBlankForTotauxBilan(CGBilanLine line) {
        return line.getLibelle().equals(getSession().getLabel("PARSER_TOTAUX"))
                && (new FWCurrency(line.getSoldeActif()).compareTo(new FWCurrency(line.getSoldePassif())) == 0);
    }

    /**
     * True si le libelle de l'entité est "totaux" et si les montants sont identiques (=> aucune ligne de résultat).
     * 
     * @param line
     * @return
     */
    private boolean needBlankForTotauxPertesEtProtifs(CGComptePertesProfitsLine line) {
        return line.getLibelle().equals(getSession().getLabel("PARSER_TOTAUX"))
                && (new FWCurrency(line.getSoldeCharges()).compareTo(new FWCurrency(line.getSoldeProduits())) == 0);
    }

    public void setIdExerciceComptable(String idExerciceComptable) {
        this.idExerciceComptable = idExerciceComptable;
    }

}
