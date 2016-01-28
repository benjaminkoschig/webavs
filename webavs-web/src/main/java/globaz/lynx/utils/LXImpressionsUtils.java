package globaz.lynx.utils;

import globaz.globall.db.BSession;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

public class LXImpressionsUtils {

    /**
     * Ajout d'un header et d'un footer au document excel.
     * 
     * @param sheet
     * @param session
     * @param hautGauche
     * @param hautCentre
     * @param hautDroite
     * @param basGauche
     * @param basCentre
     * @throws Exception
     */
    public static void addHeaderFooter(HSSFSheet sheet, BSession session, String hautGauche, String hautCentre,
            String hautDroite, String basGauche, String basCentre) throws Exception {
        HSSFHeader header = sheet.getHeader();
        header.setLeft(hautGauche);
        header.setCenter(hautCentre);
        header.setRight(hautDroite);

        HSSFFooter footer = sheet.getFooter();
        footer.setRight(session.getLabel("IMP_PAGE") + " : " + HSSFFooter.page() + "/" + HSSFFooter.numPages());
        footer.setCenter(basCentre);
        footer.setLeft(session.getLabel("IMP_REFERENCE") + " : " + basGauche);
    }

    /**
     * Permet la création simple d'une ligne avec 2 cellules
     * 
     * @param wb
     * @param sheet
     * @param countLine
     * @param libelle
     * @param value
     */
    public static void addRowContentColumns(HSSFWorkbook wb, HSSFSheet sheet, int countLine, String libelle,
            String value) {
        HSSFRow row = sheet.createRow((short) countLine);

        HSSFCell cell = row.createCell((short) LXConstants.INDEX_COLUMN_LIBELLE);
        cell.setCellValue(libelle + "  " + value);
    }

    public static void addRowContentColumns(HSSFWorkbook wb, HSSFSheet sheet, int countLine, String libelle,
            String value1, double value2, double value3, double value4, boolean alterneCouleur) {
        HSSFRow row = sheet.createRow((short) countLine);

        HSSFCellStyle styleColumnAlignRight = wb.createCellStyle();
        styleColumnAlignRight.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        if (alterneCouleur && LXConstants.USE_COLOR) {
            styleColumnAlignRight.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumnAlignRight.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
        }

        HSSFCellStyle styleColumnAlignRightNumeric = wb.createCellStyle();
        styleColumnAlignRightNumeric.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignRightNumeric.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignRightNumeric.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        styleColumnAlignRightNumeric.setDataFormat((short) 4);
        if (alterneCouleur && LXConstants.USE_COLOR) {
            styleColumnAlignRightNumeric.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumnAlignRightNumeric.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
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
        cell.setCellStyle(styleColumnAlignRight);

        if (value1 != null) {
            cell = row.createCell((short) i++);
            cell.setCellValue(value1);
            cell.setCellStyle(styleColumnAlignLeft);
        }

        cell = row.createCell((short) i++);
        cell.setCellValue(value2);
        cell.setCellStyle(styleColumnAlignRightNumeric);

        cell = row.createCell((short) i++);
        cell.setCellValue(value3);
        cell.setCellStyle(styleColumnAlignRightNumeric);

        cell = row.createCell((short) i++);
        cell.setCellValue(value4);
        cell.setCellStyle(styleColumnAlignRightNumeric);

    }

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
     * @param alterneCouleur
     */
    public static void addRowContentColumns(HSSFWorkbook wb, HSSFSheet sheet, int countLine, String libelle,
            String value1, String value2, String value3, String value4, String value5, String value6,
            boolean alterneCouleur) {
        HSSFRow row = sheet.createRow((short) countLine);

        HSSFCellStyle styleColumnAlignRight = wb.createCellStyle();
        styleColumnAlignRight.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignRight.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignRight.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        if (alterneCouleur && LXConstants.USE_COLOR) {
            styleColumnAlignRight.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumnAlignRight.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
        }

        HSSFCellStyle styleColumnAlignLeft = wb.createCellStyle();
        styleColumnAlignLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        if (alterneCouleur && LXConstants.USE_COLOR) {
            styleColumnAlignLeft.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumnAlignLeft.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
        }

        HSSFCellStyle styleColumnAlignCenter = wb.createCellStyle();
        styleColumnAlignCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleColumnAlignCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        if (alterneCouleur && LXConstants.USE_COLOR) {
            styleColumnAlignCenter.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleColumnAlignCenter.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
        }

        int i = 0;

        HSSFCell cell = row.createCell((short) i++);
        cell.setCellValue(libelle);
        cell.setCellStyle(styleColumnAlignLeft);

        if (value1 != null) {
            cell = row.createCell((short) i++);
            cell.setCellValue(value1);
            cell.setCellStyle(styleColumnAlignCenter);
        }

        if (value2 != null) {
            cell = row.createCell((short) i++);
            cell.setCellValue(value2);
            cell.setCellStyle(styleColumnAlignCenter);
        }

        if (value3 != null) {
            cell = row.createCell((short) i++);
            cell.setCellValue(value3);
            cell.setCellStyle(styleColumnAlignLeft);
        }

        if (value4 != null) {
            cell = row.createCell((short) i++);
            cell.setCellValue(value4);
            cell.setCellStyle(styleColumnAlignCenter);
        }
        if (value5 != null) {
            cell = row.createCell((short) i++);
            cell.setCellValue(value5);
            cell.setCellStyle(styleColumnAlignCenter);
        }

        cell = row.createCell((short) i++);
        cell.setCellValue(value6);
        cell.setCellStyle(styleColumnAlignRight);
    }

    /**
     * @param wb
     * @param sheet
     * @param countLine
     * @param libelle
     * @param libelleCol1
     * @param libelleCol2
     * @param libelleCol3
     */
    public static void addRowContentLibelleColumns(HSSFWorkbook wb, HSSFSheet sheet, int countLine, String libelle,
            String libelleCol1, String libelleCol2, String libelleCol3) {
        HSSFRow row = sheet.createRow((short) countLine);

        HSSFFont font = wb.createFont();
        font.setColor(new HSSFColor.WHITE().getIndex());

        HSSFCellStyle styleColumnLeft = wb.createCellStyle();
        styleColumnLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setAlignment(HSSFCellStyle.ALIGN_CENTER);

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
        cell.setCellValue("");
        cell.setCellStyle(styleColumnLeft);

        sheet.addMergedRegion(new Region(countLine, (short) 0, countLine, (short) (i - 1)));

        if (libelleCol1 != null) {
            cell = row.createCell((short) i++);
            cell.setCellValue(libelleCol1);
            cell.setCellStyle(styleColumn);
        }

        if (libelleCol2 != null) {
            cell = row.createCell((short) i++);
            cell.setCellValue(libelleCol2);
            cell.setCellStyle(styleColumn);
        }

        if (libelleCol3 != null) {
            cell = row.createCell((short) i++);
            cell.setCellValue(libelleCol3);
            cell.setCellStyle(styleColumn);
        }

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
     * @param align
     */
    public static void addRowContentLibelleColumns(HSSFWorkbook wb, HSSFSheet sheet, int countLine, String libelle,
            String libelleCol1, String libelleCol2, String libelleCol3, String libelleCol4, String libelleCol5,
            String libelleCol6) {
        HSSFRow row = sheet.createRow((short) countLine);

        HSSFFont font = wb.createFont();
        font.setColor(new HSSFColor.WHITE().getIndex());

        HSSFCellStyle styleColumnLeft = wb.createCellStyle();
        styleColumnLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleColumnLeft.setAlignment(HSSFCellStyle.ALIGN_CENTER);

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
        cell.setCellStyle(styleColumnLeft);

        if (libelleCol1 != null) {
            cell = row.createCell((short) i++);
            cell.setCellValue(libelleCol1);
            cell.setCellStyle(styleColumn);
        }

        if (libelleCol2 != null) {
            cell = row.createCell((short) i++);
            cell.setCellValue(libelleCol2);
            cell.setCellStyle(styleColumn);
        }

        if (libelleCol3 != null) {
            cell = row.createCell((short) i++);
            cell.setCellValue(libelleCol3);
            cell.setCellStyle(styleColumn);
        }

        if (libelleCol4 != null) {
            cell = row.createCell((short) i++);
            cell.setCellValue(libelleCol4);
            cell.setCellStyle(styleColumn);
        }

        if (libelleCol5 != null) {
            cell = row.createCell((short) i++);
            cell.setCellValue(libelleCol5);
            cell.setCellStyle(styleColumn);
        }

        cell = row.createCell((short) i++);
        cell.setCellValue(libelleCol6);
        cell.setCellStyle(styleColumnRight);

    }

    /**
     * Permet d'ajouter une ligne vide
     * 
     * @param sheet
     * @param countLine
     */
    public static void addRowVoid(HSSFSheet sheet, int countLine) {
        HSSFRow row = sheet.createRow((short) countLine);
        row.createCell((short) LXConstants.INDEX_COLUMN_SOLDE_A);
    }

    /**
     * Initialise la page en A4 paysage
     * 
     * @param landScape
     *            si <code>true</code> mode paysage, sinon mode portrait
     * @return HSSFPrintSetup
     */
    public static HSSFPrintSetup initPage(HSSFSheet sheet, boolean landScape) {
        HSSFPrintSetup ps = sheet.getPrintSetup();
        // format
        ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        ps.setLandscape(landScape);
        // marges en-tête/pied de page
        ps.setHeaderMargin(0);
        ps.setFooterMargin(0);

        return ps;
    }

    /**
     * @param sheet
     * @param numColumn
     * @param valeur
     */
    public static void setContentColumnWidth(HSSFSheet sheet, int numColumn, int valeur) {
        sheet.setColumnWidth((short) numColumn, (short) (valeur * 256));
    }

    /**
     * Permet de définir la taille des colonnes<br>
     * <br>
     * ex de valeur : "1'000'000'000.00"
     * 
     * @param sheet
     * @param fromColum
     * @param toColumn
     * @param valeur
     */
    public static void setContentColumnWidth(HSSFSheet sheet, int fromColum, int toColumn, String valeur) {
        for (int i = fromColum; i <= toColumn; i++) {
            sheet.setColumnWidth((short) i, (short) (valeur.length() * 256));
        }
    }

    /**
     * Constructeur
     */
    protected LXImpressionsUtils() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }

}
