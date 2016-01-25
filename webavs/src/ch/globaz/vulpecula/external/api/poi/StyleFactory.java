package ch.globaz.vulpecula.external.api.poi;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class StyleFactory {
    private HSSFWorkbook wb;

    /** Format : "#,##0.00" */
    private static final short FORMAT_MONTANT = (short) 4;
    private static final String FORMAT_POURCENT = "0.00%";
    private static final String FORMAT_NOMBRE_1D = "0.0";

    private static short NO_COLOR = -1;

    public enum FieldType {
        STANDARD,
        MONTANT,
        POURCENT,
        NOMBRE_1D
    }

    public enum Alignement {
        LEFT,
        CENTER,
        RIGHT
    }

    public enum VerticalAlignement {
        UNDEFINED,
        TOP,
        CENTER,
        BOTTOM
    }

    public enum Border {
        NONE,
        THIN,
        MEDIUM
    }

    public StyleFactory(HSSFWorkbook wb) {
        this.wb = wb;
    }

    public HSSFCellStyle createStyle(final int fontHeight) {
        return createStyle(10, false, Alignement.LEFT);
    }

    public HSSFCellStyle createStyle(final int fontHeight, final Border border) {
        return createStyle(fontHeight, Alignement.LEFT, border);
    }

    public HSSFCellStyle createStyle(final int fontHeight, final Alignement alignement, final Border border) {
        return createStyle(fontHeight, false, false, alignement, NO_COLOR, border);
    }

    public HSSFCellStyle createStyle(final int fontHeight, final boolean italic, final Alignement alignement) {
        return createStyle(fontHeight, italic, alignement, NO_COLOR);
    }

    public HSSFCellStyle createStyle(final int fontHeight, final boolean italic, final Alignement alignement,
            final int color) {
        return createStyle(fontHeight, italic, false, alignement, color);
    }

    public HSSFCellStyle createStyle(final int fontHeight, final boolean italic, final boolean bold,
            final Alignement alignement, final int color) {
        return createStyle(fontHeight, italic, bold, alignement, color, Border.NONE);
    }

    public HSSFCellStyle createStyle(final int fontHeight, final boolean italic, final boolean bold,
            final Alignement alignement, final int color, final Border border) {
        return createStyle(fontHeight, italic, bold, alignement, VerticalAlignement.UNDEFINED, color, border,
                FieldType.STANDARD);
    }

    public HSSFCellStyle createStyle(short fontHeight, boolean italic, boolean bold, Alignement alignement,
            Border border) {
        return createStyle(fontHeight, italic, bold, alignement, NO_COLOR, border);
    }

    public HSSFCellStyle createStyle(short fontHeight, boolean italic, boolean bold, Alignement alignement,
            VerticalAlignement verticalAlignement, Border border) {
        return createStyle(fontHeight, italic, bold, alignement, verticalAlignement, NO_COLOR, border,
                FieldType.STANDARD);
    }

    public HSSFCellStyle createStyle(short fontHeight, Alignement alignement, VerticalAlignement verticalAlignement,
            Border border) {
        return createStyle(fontHeight, false, false, alignement, verticalAlignement, NO_COLOR, border,
                FieldType.STANDARD);
    }

    public HSSFCellStyle createStyle(short fontHeight, boolean italic, boolean bold, Alignement alignement,
            Border border, FieldType type) {
        return createStyle(fontHeight, italic, bold, alignement, VerticalAlignement.UNDEFINED, NO_COLOR, border, type);
    }

    public HSSFCellStyle createStyle(short fontHeight, boolean italic, boolean bold, Alignement alignement, int color,
            Border border, FieldType type) {
        return createStyle(fontHeight, italic, bold, alignement, VerticalAlignement.UNDEFINED, color, border, type);
    }

    public HSSFCellStyle createStyle(final int fontHeight, final boolean italic, final boolean bold,
            final Alignement alignement, final VerticalAlignement verticalAlignement, final int color,
            final Border border, FieldType type) {
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        setAlignement(style, alignement);
        setVerticalAlignement(style, verticalAlignement);
        setColor(style, color);
        setFont(style, fontHeight, italic, bold);
        setBorder(style, border);
        setFormat(style, type);
        return style;
    }

    private void setFormat(HSSFCellStyle style, FieldType type) {
        switch (type) {
            case MONTANT:
                style.setDataFormat(FORMAT_MONTANT);
                break;
            case POURCENT:
                style.setDataFormat(wb.createDataFormat().getFormat(FORMAT_POURCENT));
            case NOMBRE_1D:
                style.setDataFormat(wb.createDataFormat().getFormat(FORMAT_NOMBRE_1D));
            default:
                break;
        }
    }

    private void setBorder(HSSFCellStyle style, Border border) {
        switch (border) {
            case THIN:
                initListBorderThin(style);
                break;
            case MEDIUM:
                initListBorderMedium(style);
                break;
            default:
                break;
        }
    }

    private void setFont(HSSFCellStyle style, int fontHeight, boolean italic, boolean bold) {

        if (bold) {
            HSSFFont font = getFontBold(fontHeight);
            font.setItalic(italic);
            style.setFont(getFontBold(fontHeight));
        } else {
            HSSFFont font = wb.createFont();
            font.setItalic(italic);
            font.setFontHeightInPoints((short) fontHeight);
            style.setFont(font);
        }
    }

    private void setColor(HSSFCellStyle style, int color) {
        if (color != NO_COLOR) {
            style.setFillBackgroundColor((short) color);
            style.setFillForegroundColor((short) color);
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        }
    }

    private void setAlignement(HSSFCellStyle style, Alignement alignement) {
        switch (alignement) {
            case LEFT:
                style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                break;
            case RIGHT:
                style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                break;
            case CENTER:
                style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                break;
            default:
                style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                break;
        }
    }

    private void setVerticalAlignement(HSSFCellStyle style, VerticalAlignement verticalAlignement) {
        switch (verticalAlignement) {
            case TOP:
                style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
                break;
            case CENTER:
                style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                break;
            case BOTTOM:
                style.setVerticalAlignment(HSSFCellStyle.VERTICAL_BOTTOM);
                break;
            default:
                break;
        }
    }

    /**
     * défini une police gras
     * 
     * @param wb
     * @return la font gras
     */
    protected HSSFFont getFontBold(int fontHeight) {
        HSSFFont fontBold = wb.createFont();
        fontBold.setFontHeightInPoints((short) fontHeight);
        fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        return fontBold;
    }

    /**
     * Initialise les bordures THIN pour une liste.
     */
    private void initListBorderThin(HSSFCellStyle style) {
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
    }

    /**
     * Initialise les bordures MEDIUM pour une liste.
     */
    private void initListBorderMedium(HSSFCellStyle style) {
        style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
        style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
    }
}
