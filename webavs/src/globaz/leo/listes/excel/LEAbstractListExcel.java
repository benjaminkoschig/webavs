package globaz.leo.listes.excel;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.common.JadeCodingUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
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

// styleCenter.setFillPattern(HSSFCellStyle.FINE_DOTS ); //points en trame de fond
// TODO sel : ajouter des _addError() pour éviter les NullPointerException quand on appelle createCell avant createRow
// par exemple.
/**
 * Classe générique pour toutes les listes Excel
 * 
 * @author SEL
 */
public abstract class LEAbstractListExcel {
    /**
     * Cette classe permet de saisir le titre et son style <br>
     * afin de pouvoir définir un style propre à chaque titre de colonne
     * 
     * @author SCH
     */
    public class ParamTitle {
        /** HSSFCellStyle : sytle du titre */
        HSSFCellStyle style = getStyleListTitleCenter();
        /** String : titre de la colonne */
        String title = "";

        /**
         * Définit le titer de la colonne avec un style par défaut (centré; gras; encadré(medium); Wrap)
         * 
         * @param String
         *            : titre de la colonne
         */
        public ParamTitle(String title) {
            this(title, getStyleListTitleCenter());
        }

        /**
         * Définit le titre de la colonne et le sytle du titre.
         * 
         * @param String
         *            : titre de la colonne
         * @param HSSFCellStyle
         *            : sytle du titre
         */
        public ParamTitle(String title, HSSFCellStyle style) {
            setTitle(title);
            setStyle(style);
        }

        /**
         * Style de la colonne
         * 
         * @return HSSFCellStyle style de la colonne
         */
        public HSSFCellStyle getStyle() {
            return style;
        }

        /**
         * Titre de la colonne
         * 
         * @return String retourne le titre
         */
        public String getTitle() {
            return title;
        }

        /**
         * Style de la colonne
         * 
         * @param HSSFCellStyle
         *            style
         */
        public void setStyle(HSSFCellStyle style) {
            this.style = style;
        }

        /**
         * Titre de la colonne
         * 
         * @param String
         *            title
         */
        public void setTitle(String title) {
            this.title = title;
        }
    }

    protected static final double BOTTOM_MARGIN = 0.6;

    // Les formats disponibles dans POI API :
    // "General"
    // "0"
    // "0.00"
    // "#,##0"
    // "#,##0.00"
    // "($#,##0_);($#,##0)"
    // "($#,##0_);[Red]($#,##0)"
    // "($#,##0.00);($#,##0.00)"
    // "($#,##0.00_);[Red]($#,##0.00)"
    // "0%"
    // "0.00%"
    // "0.00E+00"
    // "# ?/?"
    // "# ??/??"
    // "m/d/yy"
    // "d-mmm-yy"
    // "d-mmm"
    // "mmm-yy"
    // "h:mm AM/PM"
    // "h:mm:ss AM/PM"
    // "h:mm"
    // "h:mm:ss"
    // "m/d/yy h:mm"

    protected static final short COLUMN_WIDTH_10000 = 10000;
    protected static final short COLUMN_WIDTH_12000 = 12000;

    protected static final short COLUMN_WIDTH_3500 = 3500;
    protected static final short COLUMN_WIDTH_4000 = 4000;
    protected static final short COLUMN_WIDTH_4500 = 4500;
    protected static final short COLUMN_WIDTH_5500 = 5500;

    protected static final short COLUMN_WIDTH_6500 = 6500;
    protected static final short COLUMN_WIDTH_7500 = 7500;
    /** Nom par défaut de la liste */
    private static final String DEFAULT_FILE_NAME = "Liste";
    /** Extension du fichier */
    private static final String FILE_TYPE = ".xls";
    /** Format : "#,##0.00" */
    protected static final short FORMAT_MONTANT = (short) 4;
    private static final String FORMAT_POURCENT = "0.00%";
    protected static final double LEFT_MARGIN = 0.4;
    /** Nombre maximum de ligne par feuille */
    private static final int NUMBER_MAX_ROW = 65535;

    /** Séparateur entre le N° de page et le nb de page */
    private static final String PAGE_SEPARATOR = "/";
    protected static final double RIGHT_MARGIN = 0.4;
    /** Longueur maximum pour le nom de la feuille */
    protected static final int SHEET_MAX_LENGTH = 31;
    /**
     * Nom par défaut d'une feuille créée automatiquement quand le nb de ligne dépasse NUMBER_MAX_ROW
     */
    private static final String SUITE = "Suite ";

    protected static final double TOP_MARGIN = 0.6;

    /** True : affiche un border à la liste. */
    private boolean border = true;
    /** Détermine la colonne en cours */
    private int colNum = 0;
    /** Détermine le nb de page si le contenu dépasse NUMBER_MAX_ROW lignes. */
    private int cptPage = 0;

    /** Cellule courante */
    private HSSFCell currentCell;
    /** Ligne courante */
    private HSSFRow currentRow;
    /** Feuille Excel courante */
    protected HSSFSheet currentSheet;
    private String dateImpression = "";
    private String documentTitle = "";
    private String filenameRoot = "";
    /** taille de la police */
    private short fontHeight = 10;
    private BSession session;

    private HSSFCellStyle styleCenter = null;
    private HSSFCellStyle styleCreance = null;
    private HSSFCellStyle styleGris25Pourcent = null;
    private HSSFCellStyle styleLeft = null;
    private HSSFCellStyle styleListTitleCenter = null;
    private HSSFCellStyle styleListTitleLeft = null;
    private HSSFCellStyle styleListTitleRight = null;
    private HSSFCellStyle styleMontant = null;
    // Les styles doivent être globaux pour éviter des problèmes avec Excel
    // (Limite de style pour une feuille)
    private HSSFCellStyle styleNeutre = null;
    private HSSFCellStyle stylePourcent = null;
    private HSSFCellStyle styleRight = null;
    private HSSFCellStyle styleTitleVerticalTopCenter = null;
    private HSSFCellStyle styleTitreCreance = null;
    private HSSFCellStyle styleTotal = null;
    private HSSFCellStyle styleVerticalBottomCenter = null;
    private HSSFCellStyle styleVerticalBottomLeft = null;
    private HSSFCellStyle styleVerticalBottomRight = null;
    private HSSFCellStyle styleVerticalTopCenter = null;
    private HSSFCellStyle styleVerticalTopLeft = null;
    private HSSFCellStyle styleVerticalTopRight = null;

    /** Workbook, classeur Excel */
    private HSSFWorkbook wb;;

    /**
     * @param session
     * @param filenameRoot
     *            : nom du fichier
     * @param documentTitle
     */
    public LEAbstractListExcel(BSession session, String filenameRoot, String documentTitle) {
        setSession(session);
        setFilenameRoot(filenameRoot);
        setDocumentTitle(documentTitle);
        setDateImpression(JACalendar.today().getDay() + "." + JACalendar.today().getMonth() + "."
                + JACalendar.today().getYear());
        wb = new HSSFWorkbook();
    }

    /**
     * @param boolean
     * @return la cellule courante avec un style neutre (non paramétré)
     */
    protected HSSFCell createCell(boolean value) {
        return createCell(value, wb.createCellStyle());
    }

    /**
     * @param value
     * @param style
     * @return
     */
    protected HSSFCell createCell(boolean value, HSSFCellStyle style) {
        currentCell = currentRow.createCell((short) colNum++);
        currentCell.setCellValue(value);
        currentCell.setCellStyle(style);
        return currentCell;
    }

    /**
     * Cellule formatée pour un montant
     * 
     * @param double
     * @param total
     *            si <code>true</code> utilise le style <code>getStyleMontantTotal()</code> <br />
     *            si <code>false</code> utilise le style <code>getStyleMontant</code>
     * @return la cellule courante au style montant ou montant total
     */
    protected HSSFCell createCell(double value, boolean total) {
        currentCell = currentRow.createCell((short) colNum++);
        currentCell.setCellValue(value);
        if (total) {
            currentCell.setCellStyle(getStyleMontantTotal());
        } else {
            currentCell.setCellStyle(getStyleMontant());
        }
        return currentCell;
    }

    /**
     * @param boolean
     * @param style
     * @return la cellule courante
     */
    protected HSSFCell createCell(double value, HSSFCellStyle style) {
        currentCell = currentRow.createCell((short) colNum++);
        currentCell.setCellValue(value);
        currentCell.setCellStyle(style);
        return currentCell;
    }

    /**
     * @param value
     * @return la cellule courante avec un style neutre (non paramétré)
     */
    protected HSSFCell createCell(int value) {
        currentCell = currentRow.createCell((short) colNum++);
        currentCell.setCellValue(value);
        return currentCell;
    }

    /**
     * @param value
     * @param style
     * @return la cellule courante
     */
    protected HSSFCell createCell(int value, HSSFCellStyle style) {
        currentCell = currentRow.createCell((short) colNum++);
        currentCell.setCellValue(value);
        currentCell.setCellStyle(style);
        return currentCell;
    }

    /**
     * @param value
     * @return la cellule courante avec un style neutre (non paramétré)
     */
    protected HSSFCell createCell(String value) {
        if (styleNeutre == null) {
            styleNeutre = wb.createCellStyle();
        }
        return createCell(value, styleNeutre);
    }

    /**
     * @param String
     * @param style
     * @return la cellule courante
     */
    protected HSSFCell createCell(String value, HSSFCellStyle style) {
        currentCell = currentRow.createCell((short) colNum++);
        currentCell.setCellValue(value);
        if (style != null) {
            currentCell.setCellStyle(style);
        }
        return currentCell;
    }

    /**
     * Permet de saisir une fomule.
     * 
     * @param formule
     *            Ex : SUM(E1:E12)
     * @param style
     *            da la cellule
     * @return la cellule courante
     */
    protected HSSFCell createCellFormula(String formule, HSSFCellStyle style) {
        currentCell = currentRow.createCell((short) colNum++);
        currentCell.setCellFormula(formule);
        currentCell.setCellStyle(style);
        return currentCell;
    }

    /**
     * Crée le pied de page
     * 
     * @param title
     * @return le pied de page
     */
    protected HSSFFooter createFooter(String refInforom) {
        // pied de page
        HSSFFooter footer = currentSheet.getFooter();
        footer.setRight(getSession().getLabel("PAGE") + HSSFFooter.page() + PAGE_SEPARATOR + HSSFFooter.numPages());
        footer.setLeft(getSession().getLabel("CACPAGE") + " : " + refInforom + " - "
                + getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1) + " - "
                + getDateImpression() + " - " + getSession().getUserName());
        return footer;
    }

    /**
     * Crée l'entête
     * 
     * @param title
     * @return l'entête
     */
    protected HSSFHeader createHeader() {
        // en-tête
        HSSFHeader header = currentSheet.getHeader();
        header.setLeft(FWIImportProperties.getInstance().getProperty(new JadePublishDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        header.setRight(getDocumentTitle());
        return header;
    }

    /**
     * Créée une région fusionnée avec le style et la valeur passée en paramètre.
     * 
     * @param int colFrom
     * @param int colTo
     * @param int rowFrom
     * @param int rowTo
     * @param String
     *            value
     * @param HSSFCellStyle
     *            style
     * @return int index of this region.
     */
    protected int createMergedRegion(int colFrom, int colTo, int rowFrom, int rowTo, String value, HSSFCellStyle style) {
        return createMergedRegion(getRegion(colFrom, colTo, rowFrom, rowTo), createCell(value, style));
    }

    /**
     * Fusionne une région avec les paramètres de style de la cellule.
     * 
     * @param Region
     *            region
     * @param HSSFCell
     *            cell
     * @return int index of this region.
     */
    protected int createMergedRegion(Region region, HSSFCell cell) {
        int rowStart = region.getRowFrom();
        int rowEnd = region.getRowTo();
        int columnFrom = region.getColumnFrom();
        int columnTo = region.getColumnTo();

        for (int i = rowStart; i <= rowEnd; i++) {
            for (int j = columnFrom; j <= columnTo; j++) {
                getCell(currentSheet.getRow(i), j).setCellStyle(cell.getCellStyle());
            }
        }

        return getCurrentSheet().addMergedRegion(region);
    }

    /**
     * Crée une ligne.
     * 
     * @return le numéro de la ligne.
     */
    protected int createRow() {
        colNum = 0;
        if (currentSheet.getPhysicalNumberOfRows() >= NUMBER_MAX_ROW) {
            cptPage++;
            createSheet(SUITE + cptPage);
        }
        currentRow = currentSheet.createRow(currentSheet.getPhysicalNumberOfRows());

        return currentRow.getRowNum();
    }

    /**
     * Crée une feuille Excel
     * 
     * @param title
     * @return la feuille Excel courante
     */
    protected HSSFSheet createSheet(String title) {
        // Stoquer le titre dans une map et vérifier pour ne pas avoir deux
        // feuilles portant le meme nom.
        if (title.length() > SHEET_MAX_LENGTH) {
            title = title.substring(0, SHEET_MAX_LENGTH);
        }
        currentSheet = wb.createSheet(title);

        // Marges (unité en pouces)
        currentSheet.setMargin(HSSFSheet.LeftMargin, LEFT_MARGIN);
        currentSheet.setMargin(HSSFSheet.RightMargin, RIGHT_MARGIN);
        currentSheet.setMargin(HSSFSheet.TopMargin, TOP_MARGIN);
        currentSheet.setMargin(HSSFSheet.BottomMargin, BOTTOM_MARGIN);

        return currentSheet;
    }

    /**
     * Retourne la cellule selon la ligne et la colonne passées en paramètre.
     * 
     * @param HSSFRow
     *            row : ligne
     * @param int column : colonne
     * @return HSSFCell : cellule
     */
    public HSSFCell getCell(HSSFRow row, int column) {
        HSSFCell cell = row.getCell((short) column);
        if (cell == null) {
            cell = row.createCell((short) column);
        }
        return cell;
    }

    /**
     * Formate un numéro de colonne en son équivalent alpha (ex : AD)
     * 
     * @param int nbCol : le numéro de la colonne
     * @return retourne le nom de la colonne
     */
    public String getColAlpha(int nbCol) {
        char alpha = 'A';
        String colAplha = "";
        int mod = 0;
        int div = 0;

        if (nbCol > 25) {
            mod = (nbCol % 26) - 1;
            div = (nbCol / 26) - 1;
            alpha += div;
            colAplha += Character.toString(alpha);
            alpha = 'A';
            alpha += mod + 1;
            colAplha += Character.toString(alpha);
        } else {
            alpha += nbCol;
            colAplha = Character.toString(alpha);
        }
        return colAplha;
    }

    /**
     * @return the colNum
     */
    public int getColNum() {
        return colNum;
    }

    /**
     * @return the currentCell
     */
    public HSSFCell getCurrentCell() {
        return currentCell;
    }

    /**
     * @return the currentRow
     */
    public HSSFRow getCurrentRow() {
        return currentRow;
    }

    /**
     * Retourne la feuille Excel courante.
     * 
     * @return HSSFSheet the currentSheet
     */
    protected HSSFSheet getCurrentSheet() {
        return currentSheet;
    }

    /**
     * @return the dateImpression
     */
    protected String getDateImpression() {
        return dateImpression;
    }

    /**
     * @return the documentTitle
     */
    protected String getDocumentTitle() {
        return documentTitle;
    }

    /**
     * @return the filenameRoot
     */
    protected String getFilenameRoot() {
        return filenameRoot;
    }

    /**
     * défini une police gras
     * 
     * @param wb
     * @return la font gras
     */
    protected HSSFFont getFontBold() {
        HSSFFont fontBold = wb.createFont();
        // fontBold.setFontName(HSSFFont.FONT_ARIAL);
        fontBold.setFontHeightInPoints(getFontHeight());
        fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        return fontBold;
    }

    /**
     * @return
     */
    public short getFontHeight() {
        return fontHeight;
    }

    /**
     * Génère le fichier Excel <br />
     * méthode pour gérer le fichier en sortie <br />
     * Créé le : 17 août 06
     * 
     * @return le chemin absolu du fichier
     */
    public String getOutputFile() {
        try {
            File f = File.createTempFile((filenameRoot == null ? DEFAULT_FILE_NAME : filenameRoot)
                    + JACalendar.today().toStrAMJ() + "_", FILE_TYPE);
            f.deleteOnExit();
            FileOutputStream out = new FileOutputStream(f);
            wb.write(out);
            out.close();
            return f.getAbsolutePath();
        } catch (Exception e) {
            JadeCodingUtil.catchException(this, "getOutputFile", e);
            return "";
        }
    }

    /**
     * Retourne une région de cellules.
     * 
     * @param short colFrom
     * @param short colTo
     * @param short rowFrom
     * @param short rowTo
     * @return Region : une région de cellules.
     */
    protected Region getRegion(int colFrom, int colTo, int rowFrom, int rowTo) {
        Region region = new Region();
        region.setRowFrom(rowFrom);
        region.setRowTo(rowTo);
        region.setColumnFrom((short) colFrom);
        region.setColumnTo((short) colTo);
        return region;
    }

    /**
     * Retourne la ligne passée en paramètre de la feuille courante.
     * 
     * @param int rowNum : numéro de la ligne
     * @return retourne la ligne demandée
     */
    public HSSFRow getRow(int rowNum) {
        currentRow = getCurrentSheet().getRow(rowNum);
        if (currentRow == null) {
            currentRow = getCurrentSheet().createRow(rowNum);
        }
        return currentRow;
    }

    /**
     * @return the session
     */
    protected BSession getSession() {
        return session;
    }

    /**
     * gauche, italique
     * 
     * @return le style des critères des listes
     */
    protected HSSFCellStyle getStyleCritere() {
        if (styleCreance == null) {
            HSSFFont font = wb.createFont();
            font.setItalic(true);
            font.setFontHeightInPoints(getFontHeight());
            styleCreance = wb.createCellStyle();
            styleCreance.setFont(font);
            styleCreance.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        }
        return styleCreance;
    }

    /**
     * droite, italique
     * 
     * @return le style titre pour les critères des listes
     */
    protected HSSFCellStyle getStyleCritereTitle() {
        if (styleTitreCreance == null) {
            HSSFFont font = wb.createFont();
            font.setItalic(true);
            font.setFontHeightInPoints(getFontHeight());
            styleTitreCreance = wb.createCellStyle();
            styleTitreCreance.setFont(font);
            styleTitreCreance.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        }
        return styleTitreCreance;
    }

    /**
     * Grise la cellule à 25%
     * 
     * @return
     */
    protected HSSFCellStyle getStyleGris25Pourcent() {
        if (styleGris25Pourcent == null) {
            styleGris25Pourcent = wb.createCellStyle();
            styleGris25Pourcent.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
            styleGris25Pourcent.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            styleGris25Pourcent.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        }
        return styleGris25Pourcent;
    }

    /**
     * centré; encadré(thin); wrap
     * 
     * @return le style liste centré
     */
    protected HSSFCellStyle getStyleListCenter() {
        if (styleCenter == null) {
            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints(getFontHeight());
            styleCenter = wb.createCellStyle();
            styleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            styleCenter.setFont(font);
            initListBorderThin(styleCenter);
            styleCenter.setWrapText(true); // La largeur de la cellule s'adapte
            // au contenu
        }
        return styleCenter;
    }

    /**
     * gauche; encadré(thin); Wrap
     * 
     * @return le style liste aligné à gauche
     */
    protected HSSFCellStyle getStyleListLeft() {
        if (styleLeft == null) {
            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints(getFontHeight());
            styleLeft = wb.createCellStyle();
            styleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            styleLeft.setFont(font);
            initListBorderThin(styleLeft);
            styleLeft.setWrapText(true); // La largeur de la cellule s'adapte au
            // contenu
        }
        return styleLeft;
    }

    /**
     * droite; encadré(thin); wrap
     * 
     * @return le style liste aligné à droite
     */
    protected HSSFCellStyle getStyleListRight() {
        if (styleRight == null) {
            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints(getFontHeight());
            styleRight = wb.createCellStyle();
            styleRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            styleRight.setFont(font);
            initListBorderThin(styleRight);
            styleRight.setWrapText(true); // La largeur de la cellule s'adapte
            // au contenu
        }
        return styleRight;
    }

    /**
     * centré; gras; encadré(medium); Wrap
     * 
     * @author: sel Créé le : 20 oct. 06
     * @param wb
     * @param font
     * @return le style pour les titres des listes
     */
    protected HSSFCellStyle getStyleListTitleCenter() {
        if (styleListTitleCenter == null) {
            // Définition du style aligné au centre
            styleListTitleCenter = wb.createCellStyle();
            styleListTitleCenter.setFont(getFontBold());
            styleListTitleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            initListBorderMedium(styleListTitleCenter);
            styleListTitleCenter.setWrapText(true); // La largeur de la cellule
            // s'adapte au contenu
        }
        return styleListTitleCenter;
    }

    /**
     * gauche; gras; encadré(medium); Wrap
     * 
     * @return le style titre aligné à gauche
     */
    protected HSSFCellStyle getStyleListTitleLeft() {
        if (styleListTitleLeft == null) {
            styleListTitleLeft = wb.createCellStyle();
            styleListTitleLeft.setFont(getFontBold());
            styleListTitleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);

            initListBorderMedium(styleListTitleLeft);
            styleListTitleLeft.setWrapText(true); // La largeur de la cellule
            // s'adapte au contenu
        }
        return styleListTitleLeft;
    }

    /**
     * gauche; gras; encadré(medium); Wrap
     * 
     * @return le style titre aligné à droite
     */
    protected HSSFCellStyle getStyleListTitleRight() {
        if (styleListTitleRight == null) {
            styleListTitleRight = wb.createCellStyle();
            styleListTitleRight.setFont(getFontBold());
            styleListTitleRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

            initListBorderMedium(styleListTitleRight);
            styleListTitleRight.setWrapText(true); // La largeur de la cellule
            // s'adapte au contenu
        }
        return styleListTitleRight;
    }

    /**
     * centré; gras; top; encadré(medium); Wrap
     * 
     * @author: sel Créé le : 21.11.2007
     * @param wb
     * @param font
     * @return le style pour les titres des listes
     */
    protected HSSFCellStyle getStyleListTitleVerticalAlignTopCenter() {
        if (styleTitleVerticalTopCenter == null) {
            styleTitleVerticalTopCenter = wb.createCellStyle();
            styleTitleVerticalTopCenter.setFont(getFontBold());
            styleTitleVerticalTopCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            styleTitleVerticalTopCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            initListBorderMedium(styleTitleVerticalTopCenter);
            styleTitleVerticalTopCenter.setWrapText(true); // La largeur de la
            // cellule s'adapte
            // au contenu
        }
        return styleTitleVerticalTopCenter;
    }

    /**
     * centré; bottom; encadré(thin); Wrap
     * 
     * @author: sch Créé le : 19.11.2007
     * @return le style pour les titres des listes
     */
    protected HSSFCellStyle getStyleListVerticalAlignBottomCenter() {
        if (styleVerticalBottomCenter == null) {
            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints(getFontHeight());
            styleVerticalBottomCenter = wb.createCellStyle();
            styleVerticalBottomCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_BOTTOM);
            styleVerticalBottomCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            styleVerticalBottomCenter.setFont(font);
            initListBorderThin(styleVerticalBottomCenter);
            styleVerticalBottomCenter.setWrapText(true); // La largeur de la
            // cellule s'adapte
            // au contenu
        }
        return styleVerticalBottomCenter;
    }

    /**
     * gauche; bottom; encadré(thin); Wrap
     * 
     * @author: sch Créé le : 19.11.2007
     * @return le style pour les titres des listes
     */
    protected HSSFCellStyle getStyleListVerticalAlignBottomLeft() {
        if (styleVerticalBottomLeft == null) {
            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints(getFontHeight());
            styleVerticalBottomLeft = wb.createCellStyle();
            styleVerticalBottomLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_BOTTOM);
            styleVerticalBottomLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            styleVerticalBottomLeft.setFont(font);
            initListBorderThin(styleVerticalBottomLeft);
            styleVerticalBottomLeft.setWrapText(true); // La largeur de la
            // cellule s'adapte au
            // contenu
        }
        return styleVerticalBottomLeft;
    }

    /**
     * droite; top; encadré(thin); Wrap
     * 
     * @author: sch Créé le : 19.11.2007
     * @return le style pour les titres des listes
     */
    protected HSSFCellStyle getStyleListVerticalAlignBottomRight() {
        if (styleVerticalBottomRight == null) {
            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints(getFontHeight());
            styleVerticalBottomRight = wb.createCellStyle();
            styleVerticalBottomRight.setVerticalAlignment(HSSFCellStyle.VERTICAL_BOTTOM);
            styleVerticalBottomRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            styleVerticalBottomRight.setFont(font);
            initListBorderThin(styleVerticalBottomRight);
            styleVerticalBottomRight.setWrapText(true); // La largeur de la
            // cellule s'adapte au
            // contenu
        }
        return styleVerticalBottomRight;
    }

    /**
     * centré; top; encadré(thin); Wrap
     * 
     * @author: sch Créé le : 19.11.2007
     * @return le style pour les titres des listes
     */
    protected HSSFCellStyle getStyleListVerticalAlignTopCenter() {
        if (styleVerticalTopCenter == null) {
            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints(getFontHeight());
            styleVerticalTopCenter = wb.createCellStyle();
            styleVerticalTopCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
            styleVerticalTopCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            styleVerticalTopCenter.setFont(font);
            initListBorderThin(styleVerticalTopCenter);
            styleVerticalTopCenter.setWrapText(true); // La largeur de la
            // cellule s'adapte au
            // contenu
        }
        return styleVerticalTopCenter;
    }

    /**
     * gauche; top; encadré(thin); Wrap
     * 
     * @author: sch Créé le : 19.11.2007
     * @return le style pour les titres des listes
     */
    protected HSSFCellStyle getStyleListVerticalAlignTopLeft() {
        if (styleVerticalTopLeft == null) {
            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints(getFontHeight());
            styleVerticalTopLeft = wb.createCellStyle();
            styleVerticalTopLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
            styleVerticalTopLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            styleVerticalTopLeft.setFont(font);
            initListBorderThin(styleVerticalTopLeft);
            styleVerticalTopLeft.setWrapText(true); // La largeur de la cellule
            // s'adapte au contenu
        }
        return styleVerticalTopLeft;
    }

    /**
     * droite; top; encadré(thin); Wrap
     * 
     * @author: sch Créé le : 19.11.2007
     * @return le style pour les titres des listes
     */
    protected HSSFCellStyle getStyleListVerticalAlignTopRight() {
        if (styleVerticalTopRight == null) {
            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints(getFontHeight());
            styleVerticalTopRight = wb.createCellStyle();
            styleVerticalTopRight.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
            styleVerticalTopRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            styleVerticalTopRight.setFont(font);
            initListBorderThin(styleVerticalTopRight);
            styleVerticalTopRight.setWrapText(true); // La largeur de la cellule
            // s'adapte au contenu
        }
        return styleVerticalTopRight;
    }

    /**
     * Montant : droite; encadré(thin); Wrap; format #,##0.00
     * 
     * @return le style liste pour un montant
     */
    protected HSSFCellStyle getStyleMontant() {
        if (styleMontant == null) {
            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints(getFontHeight());
            styleMontant = wb.createCellStyle();
            // setDataFormat(wb.createDataFormat().getFormat("#,##0.00")) ==
            // setDataFormat((short) 4)
            styleMontant.setDataFormat(FORMAT_MONTANT);
            styleMontant.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            styleMontant.setFont(font);
            initListBorderThin(styleMontant);
            styleMontant.setWrapText(true); // La largeur de la cellule s'adapte
            // au contenu
        }
        return styleMontant;
    }

    /**
     * Total : droite; gras; encadré(medium); Wrap; format #,##0.00
     * 
     * @return le style liste pour un montant total
     */
    protected HSSFCellStyle getStyleMontantTotal() {
        if (styleTotal == null) {
            styleTotal = wb.createCellStyle();
            styleTotal.setDataFormat(FORMAT_MONTANT);
            styleTotal.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            styleTotal.setFont(getFontBold());
            initListBorderMedium(styleTotal);
            styleTotal.setWrapText(true); // La cellule s'adapte au contenu
        }
        return styleTotal;
    }

    /**
     * Pourcent : droite;encadré(medium); Wrap; format 0.00%
     * 
     * @return le style liste pour un pourcentage
     */
    protected HSSFCellStyle getStylePourcent() {
        if (stylePourcent == null) {
            stylePourcent = wb.createCellStyle();

            stylePourcent.setDataFormat(wb.createDataFormat().getFormat(FORMAT_POURCENT));
            stylePourcent.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints(getFontHeight());
            stylePourcent.setFont(font);

            initListBorderThin(stylePourcent);
            stylePourcent.setWrapText(true); // La largeur de la cellule
            // s'adapte au contenu
        }
        return stylePourcent;
    }

    /**
     * @return le classeur
     */
    protected HSSFWorkbook getWorkbook() {
        return wb;
    }

    /**
     * Initialise les bordures MEDIUM pour une liste.
     */
    private void initListBorderMedium(HSSFCellStyle style) {
        if (isBorder()) {
            style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
            style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
            style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
            style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        }
    }

    /**
     * Initialise les bordures THIN pour une liste.
     */
    private void initListBorderThin(HSSFCellStyle style) {
        if (isBorder()) {
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        }
    }

    /**
     * Initialise la page en A4 paysage
     * 
     * @param landScape
     *            si <code>true</code> mode paysage, sinon mode portrait
     * @return HSSFPrintSetup
     */
    protected HSSFPrintSetup initPage(boolean landScape) {
        HSSFPrintSetup ps = currentSheet.getPrintSetup();
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
     * Permet de définir l'entête des colonnes
     * 
     * @author: sel Créé le : 05.06.2007
     * @param currentSheet
     * @param ArrayList
     *            de <code>ParamTitle</code>
     * @return HSSFSheet la feuille Excel courante
     */
    protected HSSFSheet initTitleRow(ArrayList col_titles) {
        HSSFRow row = null;
        HSSFCell c;

        // create Title Row
        createRow(); // Ligne vide
        row = currentSheet.createRow(currentSheet.getPhysicalNumberOfRows());
        int i = 0;
        for (i = 0; i < col_titles.size(); i++) {
            // set cell value
            c = row.createCell((short) i);
            c.setCellValue(((ParamTitle) col_titles.get(i)).getTitle());
            c.setCellStyle(((ParamTitle) col_titles.get(i)).getStyle());
        }

        return currentSheet;
    }

    /**
     * @return the border
     */
    public boolean isBorder() {
        return border;
    }

    /**
     * @param border
     *            the border to set
     */
    public void setBorder(boolean border) {
        this.border = border;
    }

    /**
     * @param colNum
     *            the colNum to set
     */
    public void setColNum(int colNum) {
        this.colNum = colNum;
    }

    /**
     * Initialise la cellule courante avec le numéro de cellule passsé en paramètre.
     * 
     * @param short cellNum
     */
    public void setCurrentCell(short cellNum) {
        currentCell = currentRow.getCell(cellNum);
    }

    /**
     * @param currentRow
     *            the currentRow to set
     */
    public void setCurrentRow(HSSFRow currentRow) {
        this.currentRow = currentRow;
    }

    /**
     * Initialise la ligne courante avec le numéro de ligne passsé en paramètre.
     * 
     * @param int rowNum
     */
    public void setCurrentRow(int rowNum) {
        currentRow = currentSheet.getRow(rowNum);
    }

    /**
     * @param currentSheet
     *            the currentSheet to set
     */
    protected void setCurrentSheet(HSSFSheet sheet) {
        currentSheet = sheet;
    }

    /**
     * @param dateImpression
     *            the dateImpression to set
     */
    protected void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    /**
     * @param documentTitle
     *            the documentTitle to set
     */
    protected void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    /**
     * @param filenameRoot
     *            the filenameRoot to set
     */
    protected void setFilenameRoot(String filenameRoot) {
        this.filenameRoot = filenameRoot;
    }

    /**
     * @param fontHeight
     */
    public void setFontHeight(short fontHeight) {
        this.fontHeight = fontHeight;
    }

    /**
     * @param session
     *            the session to set
     */
    protected void setSession(BSession session) {
        this.session = session;
    }

    /**
     * @param wb
     *            the wb to set
     */
    protected void setWb(HSSFWorkbook wb) {
        this.wb = wb;
    }
}
