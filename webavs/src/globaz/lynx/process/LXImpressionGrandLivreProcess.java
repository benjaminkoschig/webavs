package globaz.lynx.process;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.process.consolidation.utils.CGImprimerConsolidationUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.lynx.db.impression.LXImpressionGrandLivre;
import globaz.lynx.db.impression.LXImpressionGrandLivreManager;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitrice;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitriceManager;
import globaz.lynx.translation.LXCodeSystem;
import globaz.lynx.utils.LXConstants;
import globaz.lynx.utils.LXImpressionsUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class LXImpressionGrandLivreProcess extends LXImpressionProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param wb
     * @param sheet
     * @param countLine
     * @param libelle
     * @param value1
     * @param value2
     * @param value3
     * @param value4
     * @param value5
     * @param value6
     * @param value7
     * @param value8
     * @param value9
     * @param value10
     * @param value11
     * @param value12
     * @param value13
     * @param alterneCouleur
     */
    public static void addRowContentColumns(HSSFWorkbook wb, HSSFSheet sheet, int countLine, String libelle,
            String value1, String value2, String value3, String value4, String value5, String value6, String value7,
            String value8, String value9, String value10, String value11, String value12, String value13,
            boolean alterneCouleur) {
        HSSFRow row = sheet.createRow((short) countLine);

        HSSFCellStyle styleColumnAlignRight = wb.createCellStyle();
        styleColumnAlignRight.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        styleColumnAlignRight.setDataFormat((short) 4);
        if (alterneCouleur && LXConstants.USE_COLOR) {
            styleColumnAlignRight.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumnAlignRight.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
        }

        HSSFCellStyle styleColumnAlignLeft = wb.createCellStyle();
        styleColumnAlignLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        if (alterneCouleur && LXConstants.USE_COLOR) {
            styleColumnAlignLeft.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumnAlignLeft.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
        }

        HSSFCellStyle styleColumnAlignCenter = wb.createCellStyle();
        styleColumnAlignCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        if (alterneCouleur && LXConstants.USE_COLOR) {
            styleColumnAlignCenter.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumnAlignCenter.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
        }

        int i = 0;

        HSSFCell cell = row.createCell((short) i++);
        cell.setCellValue(libelle);
        cell.setCellStyle(styleColumnAlignCenter);

        cell = row.createCell((short) i++);
        cell.setCellValue(value1);
        cell.setCellStyle(styleColumnAlignLeft);

        cell = row.createCell((short) i++);
        cell.setCellValue(value2);
        cell.setCellStyle(styleColumnAlignLeft);

        cell = row.createCell((short) i++);
        cell.setCellValue(value3);
        cell.setCellStyle(styleColumnAlignCenter);

        cell = row.createCell((short) i++);
        cell.setCellValue(value4);
        cell.setCellStyle(styleColumnAlignCenter);

        cell = row.createCell((short) i++);
        cell.setCellValue(value5);
        cell.setCellStyle(styleColumnAlignCenter);

        cell = row.createCell((short) i++);
        cell.setCellValue(value6);
        cell.setCellStyle(styleColumnAlignCenter);

        cell = row.createCell((short) i++);
        if (JadeStringUtil.isBlankOrZero(value7)) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(JadeStringUtil.parseDouble(JANumberFormatter.deQuote(value7), 0));
        }
        cell.setCellStyle(styleColumnAlignRight);

        cell = row.createCell((short) i++);
        if (JadeStringUtil.isBlankOrZero(value8)) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(JadeStringUtil.parseDouble(JANumberFormatter.deQuote(value8), 0));
        }
        cell.setCellStyle(styleColumnAlignRight);

        cell = row.createCell((short) i++);
        if (JadeStringUtil.isBlankOrZero(value9)) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(JadeStringUtil.parseDouble(JANumberFormatter.deQuote(value9), 0));
        }
        cell.setCellStyle(styleColumnAlignRight);

        // Taux escompte
        cell = row.createCell((short) i++);
        if (JadeStringUtil.isBlankOrZero(value10)) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(JadeStringUtil.parseDouble(JANumberFormatter.deQuote(value10), 0));
        }
        cell.setCellStyle(styleColumnAlignRight);

        // Code tva
        cell = row.createCell((short) i++);
        cell.setCellValue(value11);
        cell.setCellStyle(styleColumnAlignRight);

        // Bloque
        cell = row.createCell((short) i++);
        cell.setCellValue(value12);
        cell.setCellStyle(styleColumnAlignCenter);

        // Motif
        cell = row.createCell((short) i++);
        cell.setCellValue(value13);
        cell.setCellStyle(styleColumnAlignLeft);

    }

    /**
     * @param wb
     * @param sheet
     * @param countLine
     * @param libelle
     * @param libelleCol1
     * @param libelleCol2
     * @param libelleCol3
     * @param libelleCol4
     * @param libelleCol5
     * @param libelleCol6
     * @param libelleCol7
     * @param libelleCol8
     * @param libelleCol9
     * @param colorBackground
     * @param colorFont
     */
    public static void addRowContentFooterLibelleColumns(HSSFWorkbook wb, HSSFSheet sheet, int countLine,
            String libelle, String libelleCol1, String libelleCol2, String libelleCol3, String libelleCol4,
            String libelleCol5, String libelleCol6, String libelleCol7, String libelleCol8, String libelleCol9,
            short colorBackground, short colorFont) {
        HSSFRow row = sheet.createRow((short) countLine);

        HSSFFont font = wb.createFont();
        font.setColor(colorFont);

        HSSFCellStyle styleColumnLeft = wb.createCellStyle();
        styleColumnLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

        if (LXConstants.USE_COLOR) {
            styleColumnLeft.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumnLeft.setFillForegroundColor(colorBackground);
            styleColumnLeft.setFont(font);
        }

        HSSFCellStyle styleColumn = wb.createCellStyle();
        styleColumn.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumn.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumn.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleColumn.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleColumn.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        if (LXConstants.USE_COLOR) {
            styleColumn.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumn.setFillForegroundColor(colorBackground);
            styleColumn.setFont(font);
        }

        HSSFCellStyle styleColumnRight = wb.createCellStyle();
        styleColumnRight.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnRight.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleColumnRight.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleColumnRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        styleColumnRight.setDataFormat((short) 4);

        if (LXConstants.USE_COLOR) {
            styleColumnRight.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumnRight.setFillForegroundColor(colorBackground);
            styleColumnRight.setFont(font);
        }

        int i = 0;

        HSSFCell cell = row.createCell((short) i++);
        cell.setCellValue(libelle);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol1);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol2);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol3);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol4);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol5);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol6);
        cell.setCellStyle(styleColumnRight);

        cell = row.createCell((short) i++);
        cell.setCellValue(JadeStringUtil.parseDouble(JANumberFormatter.deQuote(libelleCol7), 0));
        cell.setCellStyle(styleColumnRight);

        cell = row.createCell((short) i++);
        cell.setCellValue(JadeStringUtil.parseDouble(JANumberFormatter.deQuote(libelleCol8), 0));
        cell.setCellStyle(styleColumnRight);

        cell = row.createCell((short) i++);
        cell.setCellValue(JadeStringUtil.parseDouble(JANumberFormatter.deQuote(libelleCol9), 0));
        cell.setCellStyle(styleColumnRight);

        //
        cell = row.createCell((short) i++);
        cell.setCellValue("");
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue("");
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue("");
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue("");
        cell.setCellStyle(styleColumn);

    }

    /**
     * @param wb
     * @param sheet
     * @param countLine
     * @param libelle
     * @param libelleCol1
     * @param libelleCol2
     * @param libelleCol3
     * @param libelleCol4
     * @param libelleCol5
     * @param libelleCol6
     * @param libelleCol7
     * @param libelleCol8
     * @param libelleCol9
     * @param libelleCol10
     * @param libelleCol11
     * @param libelleCol12
     * @param libelleCol13
     */
    public static void addRowContentLibelleColumns(HSSFWorkbook wb, HSSFSheet sheet, int countLine, String libelle,
            String libelleCol1, String libelleCol2, String libelleCol3, String libelleCol4, String libelleCol5,
            String libelleCol6, String libelleCol7, String libelleCol8, String libelleCol9, String libelleCol10,
            String libelleCol11, String libelleCol12, String libelleCol13) {
        HSSFRow row = sheet.createRow((short) countLine);

        HSSFFont font = wb.createFont();
        font.setColor(new HSSFColor.WHITE().getIndex());

        HSSFCellStyle styleColumnLeft = wb.createCellStyle();
        styleColumnLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);

        if (LXConstants.USE_COLOR) {
            styleColumnLeft.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumnLeft.setFillForegroundColor(new HSSFColor.LIGHT_BLUE().getIndex());
            styleColumnLeft.setFont(font);
        }

        HSSFCellStyle styleColumn = wb.createCellStyle();
        styleColumn.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumn.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumn.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleColumn.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleColumn.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        if (LXConstants.USE_COLOR) {
            styleColumn.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumn.setFillForegroundColor(new HSSFColor.LIGHT_BLUE().getIndex());
            styleColumn.setFont(font);
        }

        HSSFCellStyle styleColumnRight = wb.createCellStyle();
        styleColumnRight.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnRight.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleColumnRight.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleColumnRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

        if (LXConstants.USE_COLOR) {
            styleColumnRight.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumnRight.setFillForegroundColor(new HSSFColor.LIGHT_BLUE().getIndex());
            styleColumnRight.setFont(font);
        }

        int i = 0;

        HSSFCell cell = row.createCell((short) i++);
        cell.setCellValue(libelle);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol1);
        cell.setCellStyle(styleColumnLeft);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol2);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol3);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol4);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol5);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol6);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol7);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol8);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol9);
        cell.setCellStyle(styleColumn);

        // escompte
        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol10);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol11);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol12);
        cell.setCellStyle(styleColumn);

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol13);
        cell.setCellStyle(styleColumn);

    }

    private Map<String, String> mapCsCodeTva = null;

    private Map<String, String> mapCsMotifBlocage = null;

    private Map<String, String> mapCsTypeOperation = null;

    @Override
    protected boolean _executeProcess() throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();

        // On peuple les map code systeme
        recuperationTypeOperation();
        recuperationMotifBlocage();
        recuperationCodeTva();

        if (!JadeStringUtil.isEmpty(getIdSociete())) {
            createSheetForGrandLivre(wb, getSociete());
        } else {

            // Recherche de toutes les sociétées
            LXSocieteDebitriceManager manager = new LXSocieteDebitriceManager();
            manager.setSession(getSession());
            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            for (int i = 0; i < manager.size(); i++) {
                LXSocieteDebitrice societeDeb = (LXSocieteDebitrice) manager.getEntity(i);
                createSheetForGrandLivre(wb, societeDeb);
            }

        }

        String outputFileName = Jade.getInstance().getHomeDir() + LXConstants.OUTPUT_FILE_WORK_DIR + "/"
                + LXConstants.OUTPUT_FILE_NAME_GRAND_LIVRE + "_" + LXConstants.NUMERO_REFERENCE_INFOROM_GRAND_LIVRE
                + ".xls";
        CGImprimerConsolidationUtils.createFile(wb, outputFileName);
        this.registerAttachedDocument(outputFileName);

        return false;
    }

    /**
     * @param wb
     * @param sheet
     * @param societeDeb
     * @throws Exception
     */
    private void addContentToGrandLivre(HSSFWorkbook wb, HSSFSheet sheet, LXSocieteDebitrice societeDeb)
            throws Exception {

        int countLine = 0;
        String idFournisseur = "";
        boolean couleurLigne = false;

        FWCurrency debit = new FWCurrency();
        FWCurrency credit = new FWCurrency();
        FWCurrency solde = new FWCurrency();

        // Infos générales
        countLine = printCritere(wb, sheet, societeDeb, countLine);

        LXImpressionGrandLivreManager manager = findImpressionGrandLivreManager(societeDeb);

        for (int i = 0; i < manager.size(); i++) {
            LXImpressionGrandLivre imp = (LXImpressionGrandLivre) manager.getEntity(i);
            if (LXOperation.CS_TYPE_NOTEDECREDIT_LIEE.equals(imp.getCsTypeOperation())) {
                // nothing
                // TODO exclure par la requete
                continue;
            }

            if (!idFournisseur.equals(imp.getIdFournisseur())) {

                if (!JadeStringUtil.isEmpty(idFournisseur)) {
                    LXImpressionGrandLivreProcess.addRowContentFooterLibelleColumns(wb, sheet, countLine++, "", "", "",
                            "", "", "", getSession().getLabel("IMP_TOTAL") + " :", debit.toStringFormat(),
                            credit.toStringFormat(), solde.toStringFormat(), new HSSFColor.LIGHT_BLUE().getIndex(),
                            new HSSFColor.WHITE().getIndex());

                    debit = new FWCurrency();
                    credit = new FWCurrency();
                    solde = new FWCurrency();
                } else {
                    idFournisseur = imp.getIdFournisseur();
                }

                idFournisseur = imp.getIdFournisseur();
                couleurLigne = false;
                // Création lignes vides
                LXImpressionsUtils.addRowVoid(sheet, countLine++);
                LXImpressionsUtils.addRowVoid(sheet, countLine++);

                LXImpressionGrandLivreProcess.addRowContentLibelleColumns(wb, sheet, countLine++, imp
                        .getIdExterneFournisseur(), imp.getNomCompletFournisseur(), "Libellé",
                        getSession().getLabel("IMP_NUMERO_INTERNE_SECTION"), getSession()
                                .getLabel("IMP_NUMERO_EXTERNE"), getSession().getLabel("IMP_DATE"), getSession()
                                .getLabel("IMP_ECHEANCE"), getSession().getLabel("IMP_DEBIT"),
                        getSession().getLabel("IMP_CREDIT"), getSession().getLabel("IMP_SOLDE"),
                        getSession().getLabel("IMP_ESCOMPTE"), getSession().getLabel("IMP_TVA"),
                        getSession().getLabel("IMP_BLOQUE"), getSession().getLabel("IMP_MOTIF_BLOCAGE"));
            }

            FWCurrency test = new FWCurrency();
            test.add(imp.getMontant());
            String estBloque = imp.getEstBloque().booleanValue() || imp.getFournisseurEstBloque() ? "X" : "";

            if (test.isNegative()) {
                test.negate();
                debit.add(test);
                solde.add(imp.getMontant());
                LXImpressionGrandLivreProcess.addRowContentColumns(wb, sheet, countLine++, "",
                        getLibelleTypeOperation(imp.getCsTypeOperation()), imp.getLibelle(), imp.getIdExterneSection(),
                        imp.getReferenceExterne(), imp.getDateOperation(), imp.getDateEcheance(), test.toString(), "",
                        solde.toString(), imp.getTauxEscompte(), getLibelleCodeTva(imp.getCsCodeTVA()), estBloque,
                        getLibelleMotifBlocage(imp.getCsMotifBlocage()), couleurLigne);
            } else if (LXOperation.CS_TYPE_NOTEDECREDIT_DEBASE.equals(imp.getCsTypeOperation())
                    || LXOperation.CS_TYPE_NOTEDECREDIT_ENCAISSEE.equals(imp.getCsTypeOperation())) {
                debit.add(imp.getMontant());
                test.negate();
                solde.add(test);
                LXImpressionGrandLivreProcess.addRowContentColumns(wb, sheet, countLine++, "",
                        getLibelleTypeOperation(imp.getCsTypeOperation()), imp.getLibelle(), imp.getIdExterneSection(),
                        imp.getReferenceExterne(), imp.getDateOperation(), imp.getDateEcheance(), imp.getMontant()
                                .toString(), "", solde.toString(), imp.getTauxEscompte(), getLibelleCodeTva(imp
                                .getCsCodeTVA()), estBloque, getLibelleMotifBlocage(imp.getCsMotifBlocage()),
                        couleurLigne);
            } else {
                credit.add(test);
                solde.add(imp.getMontant());
                LXImpressionGrandLivreProcess.addRowContentColumns(wb, sheet, countLine++, "",
                        getLibelleTypeOperation(imp.getCsTypeOperation()), imp.getLibelle(), imp.getIdExterneSection(),
                        imp.getReferenceExterne(), imp.getDateOperation(), imp.getDateEcheance(), "", test.toString(),
                        solde.toString(), imp.getTauxEscompte(), getLibelleCodeTva(imp.getCsCodeTVA()), estBloque,
                        getLibelleMotifBlocage(imp.getCsMotifBlocage()), couleurLigne);
            }

            couleurLigne = couleurLigne ? false : true;
        }

        // On termine le dernier tableau
        if (manager.size() > 0) {
            LXImpressionGrandLivreProcess.addRowContentFooterLibelleColumns(wb, sheet, countLine++, "", "", "", "", "",
                    "", getSession().getLabel("IMP_TOTAL") + " :", debit.toString(), credit.toString(),
                    solde.toString(), new HSSFColor.LIGHT_BLUE().getIndex(), new HSSFColor.WHITE().getIndex());

            LXImpressionsUtils.addRowVoid(sheet, countLine++);
            LXImpressionsUtils.addRowVoid(sheet, countLine++);
        }

    }

    /**
     * Créer la feuille excel contenant la balance.
     * 
     * @param wb
     * @throws Exception
     */
    private void createSheetForGrandLivre(HSSFWorkbook wb, LXSocieteDebitrice societeDeb) throws Exception {

        String libelleSheet = getSession().getLabel("IMP_GRAND_LIVRE") + " - " + societeDeb.getIdExterne() + " - "
                + societeDeb.getNom();
        HSSFSheet sheet = wb.createSheet(libelleSheet.length() > 31 ? libelleSheet.substring(0, 30) : libelleSheet);
        LXImpressionsUtils.initPage(sheet, true);

        LXImpressionsUtils.addHeaderFooter(
                sheet,
                getSession(),
                "",
                getSession().getLabel("IMP_GRAND_LIVRE"),
                "",
                LXConstants.NUMERO_REFERENCE_INFOROM_GRAND_LIVRE + " - LXImpressionGrandLivreProcess - "
                        + JACalendar.todayJJsMMsAAAA() + " - " + getSession().getUserFullName(), "");

        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_LIBELLE, 10);
        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_SOLDE_C,
                LXConstants.INDEX_COLUMN_SOLDE_O, "1'000'000'000");
        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_SOLDE_A, 20); //
        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_SOLDE_B, 30); // libelle
        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_SOLDE_K, 7); // TVA
        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_SOLDE_L, 7); // Bloque
        LXImpressionsUtils.setContentColumnWidth(sheet, LXConstants.INDEX_COLUMN_SOLDE_M, 25); // Motif
        // blocage

        addContentToGrandLivre(wb, sheet, societeDeb);
    }

    /**
     * @param societeDeb
     * @return
     * @throws Exception
     */
    private LXImpressionGrandLivreManager findImpressionGrandLivreManager(LXSocieteDebitrice societeDeb)
            throws Exception {
        LXImpressionGrandLivreManager manager = new LXImpressionGrandLivreManager();
        manager.setSession(getSession());
        manager.setForIdSociete(societeDeb.getIdSociete());
        manager.setForDateDebut(getDateDebut());
        manager.setForDateFin(getDateFin());
        manager.setForFournisseurNameBorneInf(getFournisseurBorneInf());
        manager.setForFournisseurNameBorneSup(getFournisseurBorneSup());
        manager.setForFournisseurIdBorneInf(getFournisseurIdBorneInf());
        manager.setForFournisseurIdBorneSup(getFournisseurIdBorneSup());
        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);
        return manager;
    }

    /**
     * Renvois le sujet de l'email.
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
            return getSession().getLabel("IMP_GENERATION_GRAND_LIVRE_ERROR");
        } else {
            return getSession().getLabel("IMP_GENERATION_GRAND_LIVRE_OK");
        }
    }

    public String getLibelleCodeTva(String _csCodeTva) {
        return getMapCsCodeTva().get(_csCodeTva);
    }

    public String getLibelleMotifBlocage(String _csMotifBlocage) {
        return getMapCsMotifBlocage().get(_csMotifBlocage);
    }

    public String getLibelleTypeOperation(String _csTypeOperation) {
        return getMapCsTypeOperation().get(_csTypeOperation);
    }

    public Map<String, String> getMapCsCodeTva() {
        return mapCsCodeTva;
    }

    public Map<String, String> getMapCsMotifBlocage() {
        return mapCsMotifBlocage;
    }

    public Map<String, String> getMapCsTypeOperation() {
        return mapCsTypeOperation;
    }

    /**
     * Infos générales
     * 
     * @param wb
     * @param sheet
     * @param societeDeb
     * @param countLine
     * @return
     */
    private int printCritere(HSSFWorkbook wb, HSSFSheet sheet, LXSocieteDebitrice societeDeb, int countLine) {
        LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_SOCIETEDEBITRICE")
                + " : ", societeDeb.getIdExterne() + " - " + societeDeb.getNom());

        if (!JadeStringUtil.isEmpty(getCsCategorie())) {
            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_CATEGORIE")
                    + " : ", getSession().getCodeLibelle(getCsCategorie()));
        }
        if (!JadeStringUtil.isIntegerEmpty(getDateDebut())) {
            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_DATE_DEPUIS")
                    + " : ", getDateDebut());
        }
        if (!JadeStringUtil.isIntegerEmpty(getDateFin())) {
            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_DATE_FIN")
                    + " : ", getDateFin());
        }
        if (!JadeStringUtil.isEmpty(getFournisseurBorneInf())) {
            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_FOUR_BORNE_INF")
                    + " : ", getFournisseurBorneInf());
        }
        if (!JadeStringUtil.isEmpty(getFournisseurBorneSup())) {
            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++, getSession().getLabel("IMP_FOUR_BORNE_SUP")
                    + " : ", getFournisseurBorneSup());
        }
        if (!JadeStringUtil.isEmpty(getFournisseurIdBorneInf())) {
            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++,
                    getSession().getLabel("IMP_FOUR_ID_BORNE_INF") + " : ", getFournisseurIdBorneInf());
        }
        if (!JadeStringUtil.isEmpty(getFournisseurIdBorneSup())) {
            LXImpressionsUtils.addRowContentColumns(wb, sheet, countLine++,
                    getSession().getLabel("IMP_FOUR_ID_BORNE_SUP") + " : ", getFournisseurIdBorneSup());
        }
        return countLine;
    }

    /**
     * @throws Exception
     */
    private void recuperationCodeTva() throws Exception {

        mapCsCodeTva = new HashMap<String, String>();

        FWParametersSystemCodeManager manager = LXCodeSystem.getCsCodeTva(getSession());

        for (int i = 0; i < manager.size(); i++) {
            FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);

            mapCsCodeTva.put(code.getIdCode(), code.getCurrentCodeUtilisateur().getLibelle());
        }
    }

    /**
     * @throws Exception
     */
    private void recuperationMotifBlocage() throws Exception {

        mapCsMotifBlocage = new HashMap<String, String>();

        FWParametersSystemCodeManager manager = LXCodeSystem.getCsMotifBlocage(getSession());

        for (int i = 0; i < manager.size(); i++) {
            FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);

            mapCsMotifBlocage.put(code.getIdCode(), code.getCurrentCodeUtilisateur().getLibelle());
        }
    }

    /**
     * @throws Exception
     */
    private void recuperationTypeOperation() throws Exception {

        mapCsTypeOperation = new HashMap<String, String>();

        FWParametersSystemCodeManager manager = LXCodeSystem.getCsTypeOperation(getSession());

        for (int i = 0; i < manager.size(); i++) {
            FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);

            mapCsTypeOperation.put(code.getIdCode(), code.getCurrentCodeUtilisateur().getLibelle());
        }
    }

    public void setMapCsCodeTva(Map<String, String> mapCsCodeTva) {
        this.mapCsCodeTva = mapCsCodeTva;
    }

    public void setMapCsMotifBlocage(Map<String, String> csMotifBlocage) {
        mapCsMotifBlocage = csMotifBlocage;
    }

    public void setMapCsTypeOperation(Map<String, String> csTypeOperation) {
        mapCsTypeOperation = csTypeOperation;
    }

}
