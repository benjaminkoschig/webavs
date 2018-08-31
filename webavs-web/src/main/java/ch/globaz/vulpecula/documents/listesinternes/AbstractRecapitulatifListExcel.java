package ch.globaz.vulpecula.documents.listesinternes;

import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.HSSFColor;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import globaz.globall.db.BSession;

abstract class AbstractRecapitulatifListExcel extends AbstractListExcel {
    protected static final String NUMERIC = "###,###,###,##0.00";

    protected final HSSFDataFormat format = getWorkbook().createDataFormat();

    protected AbstractRecapitulatifListExcel(AbstractListExcel listExcel) {
        super(listExcel);
    }

    protected AbstractRecapitulatifListExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
        HSSFPalette palette = getWorkbook().getCustomPalette();
        palette.setColorAtIndex(HSSFColor.LIGHT_BLUE.index, (byte) 243, (byte) 0xFF, (byte) 0xFF);
    }

    protected HSSFCellStyle styleTitle() {
        HSSFFont font = getWorkbook().createFont();
        font.setFontHeight((short) 500);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setUnderline((byte) 2);

        HSSFCellStyle style = getWorkbook().createCellStyle();
        style.setWrapText(true);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        style.setFont(font);
        style.setWrapText(true);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setBorderBottom((short) 1);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setBorderBottom((short) 1);
        style.setBorderTop((short) 1);
        style.setBorderLeft((short) 1);
        style.setBorderRight((short) 1);
        return style;
    }

    protected HSSFCellStyle styleHeader() {
        HSSFFont font = getWorkbook().createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        HSSFCellStyle style = getWorkbook().createCellStyle();
        style.setWrapText(true);
        style.setFont(font);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setBorderTop((short) 1);
        style.setBorderBottom((short) 1);
        style.setBorderLeft((short) 1);
        style.setBorderRight((short) 1);
        return style;
    }

    protected HSSFCellStyle styleIdExterne() {
        HSSFFont font = getWorkbook().createFont();
        font.setFontHeightInPoints((short) 8);

        HSSFCellStyle style = getWorkbook().createCellStyle();
        style.setWrapText(true);
        style.setFont(font);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setBorderBottom((short) 1);
        style.setBorderLeft((short) 1);
        style.setBorderRight((short) 1);
        return style;
    }

    protected HSSFCellStyle styleCot() {
        HSSFFont font = getWorkbook().createFont();
        font.setFontHeightInPoints((short) 9);

        HSSFCellStyle style = getWorkbook().createCellStyle();
        style.setDataFormat(format.getFormat(NUMERIC));
        style.setWrapText(true);
        style.setFont(font);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setBorderBottom((short) 1);
        style.setBorderTop((short) 1);
        style.setBorderLeft((short) 1);
        style.setBorderRight((short) 1);
        return style;
    }

    protected HSSFCellStyle styleMS() {
        HSSFFont font = getWorkbook().createFont();
        font.setFontHeightInPoints((short) 9);
        font.setItalic(true);
        font.setColor(HSSFColor.GREY_40_PERCENT.index);

        HSSFCellStyle style = getWorkbook().createCellStyle();
        style.setDataFormat(format.getFormat(NUMERIC));
        style.setWrapText(true);
        style.setFont(font);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setBorderBottom((short) 1);
        style.setBorderTop(HSSFCellStyle.BORDER_DOTTED);
        style.setBorderLeft((short) 1);
        style.setBorderRight((short) 1);
        return style;
    }

    protected HSSFCellStyle styleMontantCot() {
        HSSFFont font = getWorkbook().createFont();
        font.setFontHeightInPoints((short) 11);

        HSSFCellStyle style = getWorkbook().createCellStyle();
        style.setDataFormat(format.getFormat(NUMERIC));
        style.setWrapText(true);
        style.setFont(font);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style.setBorderTop((short) 1);
        // style.setBorderBottom(HSSFCellStyle.BORDER_DOTTED);
        style.setBorderLeft((short) 1);
        style.setBorderRight((short) 1);
        return style;
    }

    protected HSSFCellStyle styleMontantMs() {
        HSSFFont font = getWorkbook().createFont();
        font.setFontHeightInPoints((short) 11);
        font.setItalic(true);
        font.setColor(HSSFColor.GREY_40_PERCENT.index);

        HSSFCellStyle style = getWorkbook().createCellStyle();
        style.setDataFormat(format.getFormat(NUMERIC));
        style.setWrapText(true);
        style.setFont(font);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style.setBorderBottom((short) 1);
        // style.setBorderTop((short) 1);
        style.setBorderLeft((short) 1);
        style.setBorderRight((short) 1);
        return style;
    }

    protected void autosizeAllColumns(final HSSFSheet hssfSheet) {
        if (hssfSheet.getPhysicalNumberOfRows() > 0) {
            final HSSFRow row = hssfSheet.getRow(0);

            final Iterator<HSSFCell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                final HSSFCell cell = cellIterator.next();
                final int columnIndex = cell.getCellNum();
                hssfSheet.autoSizeColumn((short) columnIndex);
            }
        }
    }

    /**
     * takes in a 0-based base-10 column and returns a ALPHA-26 representation
     */
    // copied from class CellReference, which is private in our current version of POI!
    protected String convertNumToColString(int col) {
        String retval = null;
        int mod = col % 26;
        int div = col / 26;
        char small = (char) (mod + 65);
        char big = (char) (div + 64);

        if (div == 0) {
            retval = "" + small;
        } else {
            retval = "" + big + "" + small;
        }

        return retval;
    }

    /** Construit une chaîne de caractères pouvant être utilisée dans une formule excel, avec une list de */
    protected String joinRowsForCols(int colNum, Integer... rowsList) {
        StringBuilder sb = new StringBuilder(rowsList.length * 3);

        for (int i = 0; i < rowsList.length; i++) {
            sb.append(convertNumToColString(colNum)).append(rowsList[i] + 1);
            if (i == rowsList.length - 1) {
                break;
            }
            sb.append(",");
        }

        return sb.toString();
    }
}
