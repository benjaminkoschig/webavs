package ch.globaz.vulpecula.external.api.poi;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.common.Jade;
import globaz.jade.common.JadeCodingUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Observable;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.external.api.poi.StyleFactory.Alignement;
import ch.globaz.vulpecula.external.api.poi.StyleFactory.Border;
import ch.globaz.vulpecula.external.api.poi.StyleFactory.FieldType;
import ch.globaz.vulpecula.external.api.poi.StyleFactory.VerticalAlignement;

/**
 * Classe générique pour toutes les listes Excel
 */
public abstract class AbstractListExcel extends Observable {
    /**
     * Cette classe permet de saisir le titre et son style <br>
     * afin de pouvoir définir un style propre à chaque titre de colonne
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

    protected static final short COLUMN_WIDTH_1000 = 1000;
    protected static final short COLUMN_WIDTH_2000 = 2000;
    protected static final short COLUMN_WIDTH_3500 = 3500;
    protected static final short COLUMN_WIDTH_4500 = 4500;

    protected static final short COLUMN_WIDTH_5500 = 5500;
    protected static final short COLUMN_WIDTH_AFILIE = 5000;

    protected static final short COLUMN_WIDTH_ADRESS = 12000;

    /** Largeur d'une colonne contenant une date */
    protected static final short COLUMN_WIDTH_DATE = 3500;
    protected static final short COLUMN_WIDTH_DESCRIPTION = 12000;
    protected static final short COLUMN_WIDTH_NAME = 8000;
    /** Largeur d'une colonne contenant un montant */
    protected static final short COLUMN_WIDTH_MONTANT = 4000;
    protected static final short COLUMN_WIDTH_REMARQUE = 7000;
    /** Nom par défaut de la liste */
    private static final String DEFAULT_FILE_NAME = "Liste";
    /** Extension du fichier */
    private static final String FILE_TYPE = ".xls";

    protected static final double LEFT_MARGIN = 0.4;

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

    /** Formules **/
    public static final String OP_ADD = "+";
    public static final String OP_SUBSTRACT = "-";
    public static final String OP_MULTIPLY = "*";
    public static final String OP_DIVIDE = "/";
    public static final String OP_DIVIDE_BY_100 = "/100";
    public static final String FORMULA_SUM = "SUM";
    
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
    private JadePublishDocumentInfo documentInfo = null;
    private String documentTitle = "";
    private String filenameRoot = "";
    /** taille de la police */
    private short fontHeight = 10;
    private BSession session;

    private HSSFCellStyle styleGras = null;
    private HSSFCellStyle styleCenter = null;
    private HSSFCellStyle styleCritere = null;
    private HSSFCellStyle styleGris25Pourcent = null;
    private HSSFCellStyle styleGris25PourcentGras = null;
    private HSSFCellStyle styleListGris25PourcentGras = null;
    private HSSFCellStyle styleListMontantGris25PourcentGras = null;
    private HSSFCellStyle styleListMontant = null;
    private HSSFCellStyle styleListNombre1DGris25PourcentGras = null;
    private HSSFCellStyle styleLeft = null;
    private HSSFCellStyle styleListTitleCenter = null;
    private HSSFCellStyle styleListTitleLeft = null;
    private HSSFCellStyle styleListTitleRight = null;
    private HSSFCellStyle styleMontant = null;
    private HSSFCellStyle styleNombre1D = null;
    // Les styles doivent être globaux pour éviter des problèmes avec Excel
    // (Limite de style pour une feuille)
    private HSSFCellStyle styleNeutre = null;
    private HSSFCellStyle stylePourcent = null;
    private HSSFCellStyle styleRight = null;
    private HSSFCellStyle styleMontantRed = null;
    private HSSFCellStyle stylePourcentRed = null;
    private HSSFCellStyle styleRightRed = null;
    private HSSFCellStyle styleTitleVerticalTopCenter = null;
    private HSSFCellStyle styleTitreCritere = null;
    private HSSFCellStyle styleTotal = null;
    private HSSFCellStyle styleMontantBorderMediumTotal = null;
    private HSSFCellStyle styleVerticalBottomCenter = null;
    private HSSFCellStyle styleVerticalBottomLeft = null;
    private HSSFCellStyle styleVerticalBottomRight = null;
    private HSSFCellStyle styleVerticalTopCenter = null;
    private HSSFCellStyle styleVerticalTopLeft = null;
    private HSSFCellStyle styleVerticalTopRight = null;
    private HSSFCellStyle styleVerticalAlign = null;
    private HSSFCellStyle styleRightNone = null;
    private HSSFCellStyle styleRightNoneGreen = null;
    private HSSFCellStyle styleMontantNone = null;
    private HSSFCellStyle styleMontantNoneGreen = null;
    private HSSFCellStyle stylePourcentNone = null;
    private HSSFCellStyle styleListLeftNone = null;
    private HSSFCellStyle stylePourcentNoneGreen = null;
    private HSSFCellStyle styleMontantNoBorder = null;
    private HSSFCellStyle styleGris25PourcentGrasMontant = null;

    private HSSFCellStyle previousStyle = null;

    private boolean wantHeader = true;
    private boolean wantFooter = true;

    /** Workbook, classeur Excel */
    private HSSFWorkbook wb;

    /** Factory pour les styles */
    StyleFactory styleFactory;

    /**
     * @param session
     * @param filenameRoot
     *            : nom du fichier
     * @param documentTitle
     */
    public AbstractListExcel(BSession session, String filenameRoot, String documentTitle) {
        setSession(session);
        setFilenameRoot(filenameRoot);
        documentInfo = new JadePublishDocumentInfo();
        setDocumentTitle(documentTitle);
        setDateImpression(JACalendar.today().getDay() + "." + JACalendar.today().getMonth() + "."
                + JACalendar.today().getYear());
        wb = new HSSFWorkbook();
        styleFactory = new StyleFactory(wb);
    }

    /**
     * Créer une liste en fonction d'une existante
     * 
     * @param listExcel
     */
    public AbstractListExcel(AbstractListExcel listExcel) {
        setSession(listExcel.getSession());
        setFilenameRoot(listExcel.getFilenameRoot());
        documentInfo = listExcel.getDocumentInfo();
        setDocumentTitle(listExcel.getDocumentTitle());
        setDateImpression(JACalendar.today().getDay() + "." + JACalendar.today().getMonth() + "."
                + JACalendar.today().getYear());
        wb = listExcel.getWorkbook();
        styleFactory = new StyleFactory(wb);
    }

    public void create() {
        createContent();
        if (wantHeader) {
            createHeader();
        }
        if (wantFooter) {
            createFooter(getNumeroInforom());
        }
    }

    public abstract void createContent();

    /**
     * Création d'une cellule vide
     * Ces dernières utilisent le style qui a précédemment été utilisé
     * 
     */
    protected void createEmptyCell() {
        createEmptyCell(1);
    }

    /**
     * Création d'un certain nombre de cellules vides.
     * Ces dernières utilisent le style qui a précédemment été utilisé
     * 
     * @param number Nombre de cellules à créer
     */
    protected void createEmptyCell(int number) {
        createEmptyCell(number, "");
    }

    /**
     * Création d'un certain nombre de cellules avec un montant à 0.
     * Ces dernières utilisent le style qui a précédemment été utilisé
     * 
     * @param number Nombre de cellules à créer
     */
    protected void createEmptyCellForMontant(int number) {
        createEmptyCell(number, "0", getStyleMontant());
    }

    protected void createEmptyCellForMontant(int number, HSSFCellStyle style) {
        createEmptyCell(number, "0", style);
    }

    private void createEmptyCell(int number, String value) {
        createEmptyCell(number, value, null);
    }

    private void createEmptyCell(int number, String value, HSSFCellStyle style) {
        if (previousStyle == null) {
            style = styleNeutre;
        } else if (style == null) {
            style = previousStyle;
        }

        for (int i = 0; i < number; i++) {
            createCell(value, style);
        }
    }

    /**
     * @param boolean
     * @return la cellule courante avec un style neutre (non paramétré)
     */
    protected HSSFCell createCell(boolean value) {
        return this.createCell(value, wb.createCellStyle());
    }

    protected HSSFCell createCell(Montant montant) {
        return this.createCell(montant.doubleValue(), wb.createCellStyle());
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
        previousStyle = style;
        return currentCell;
    }

    protected HSSFCell createCell(Montant montant, HSSFCellStyle style) {
        currentCell = currentRow.createCell((short) colNum++);
        currentCell.setCellValue(montant.doubleValue());
        currentCell.setCellStyle(style);
        previousStyle = style;
        return currentCell;
    }

    protected HSSFCell createCell(Taux taux, HSSFCellStyle style) {
        currentCell = currentRow.createCell((short) colNum++);
        currentCell.setCellValue(taux.getBigDecimal().doubleValue());
        currentCell.setCellStyle(style);
        previousStyle = style;
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
        previousStyle = style;
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
        previousStyle = style;
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
        return this.createCell(value, styleNeutre);
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
        previousStyle = style;
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
        previousStyle = style;
        return currentCell;
    }

    protected void clearStyle() {
        previousStyle = null;
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
                + this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.') + 1) + " - "
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
        getDocumentInfo().setTemplateName("");
        // getDocumentInfo().setDocumentType("xls");
        getDocumentInfo().setDocumentTypeNumber(getNumeroInforom());
        HSSFHeader header = currentSheet.getHeader();
        header.setLeft(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
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
        setColNum((short) colFrom);
        int index = this.createMergedRegion(getRegion(colFrom, colTo, rowFrom, rowTo), this.createCell(value, style));
        setColNum((short) colTo + 1);
        return index;
    }

    protected int createMergedRegion(int colFrom, int colTo, int rowFrom, int rowTo, double value, HSSFCellStyle style) {
        setColNum((short) colFrom);
        int index = this.createMergedRegion(getRegion(colFrom, colTo, rowFrom, rowTo), this.createCell(value, style));
        setColNum((short) colTo + 1);
        return index;
    }

    protected int createMergedRegion(int size, String value) {
        return createMergedRegion(getColNum(), getColNum() + size - 1, getRowNum(), getRowNum(), value, previousStyle);
    }

    protected int createMergedRegion(int size, String value, HSSFCellStyle style) {
        return createMergedRegion(getColNum(), getColNum() + size - 1, getRowNum(), getRowNum(), value, style);
    }

    protected int createMergedRegion(int size, double value, HSSFCellStyle style) {
        return createMergedRegion(getColNum(), getColNum() + size - 1, getRowNum(), getRowNum(), value, style);
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
     * Création d'un certain nombre de lignes
     * 
     * @param number Nombre de lignes à créer
     * @return int représentant l'offset de la dernière ligne créer
     */
    protected int createRow(int number) {
        int lastOffset = 0;
        for (int i = 0; i < number; i++) {
            lastOffset = createRow();
        }
        return lastOffset;
    }

    /**
     * Crée une ligne.
     * 
     * @return le numéro de la ligne.
     */
    protected int createRow() {
        colNum = 0;
        if (currentSheet.getPhysicalNumberOfRows() >= RowRecord.MAX_ROW_NUMBER) {
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
        try {
            currentSheet = wb.createSheet(title);
        } catch (Exception e) {
            JadeLogger.error(e, e.getMessage());
        }

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
     * @return the colNum
     */
    public int getColNum() {
        return colNum;
    }

    public int getRowNum() {
        return currentRow.getRowNum();
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
     * Returns the document info.
     * 
     * @return the document info, <code>null</code> if the process is not running
     */
    public JadePublishDocumentInfo getDocumentInfo() {
        return documentInfo;
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
     * @return
     */
    public short getFontHeight() {
        return fontHeight;
    }

    /**
     * @return le numéro InfoRom du document
     */
    public abstract String getNumeroInforom();

    /**
     * Génère le fichier Excel <br />
     * méthode pour gérer le fichier en sortie <br />
     * Créé le : 17 août 06
     * 
     * @return le chemin absolu du fichier
     */
    public String getOutputFile() {
        try {
            String filePath = Jade.getInstance().getPersistenceDir()
                    + (filenameRoot == null ? DEFAULT_FILE_NAME : filenameRoot) + FILE_TYPE;
            File f = new File(filePath);
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
        if (isNull(styleCritere)) {
            styleCritere = styleFactory.createStyle(getFontHeight(), true, Alignement.LEFT);
        }
        return styleCritere;
    }

    protected HSSFCellStyle getStyleVerticalAlign() {
        if (isNull(styleVerticalAlign)) {
            styleVerticalAlign = getWorkbook().createCellStyle();
        }
        styleVerticalAlign.setVerticalAlignment(HSSFCellStyle.VERTICAL_JUSTIFY);
        return styleVerticalAlign;
    }

    /**
     * Pourcent : droite;encadré(medium); Wrap; format 0.00%
     * 
     * @return le style liste pour un pourcentage
     */
    protected HSSFCellStyle getStylePourcentRed() {
        if (isNull(stylePourcentRed)) {
            stylePourcentRed = styleFactory.createStyle(getFontHeight(), false, false, Alignement.RIGHT, Border.NONE,
                    FieldType.POURCENT);
        }
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.RED.index);
        stylePourcentRed.setFont(font);
        return stylePourcentRed;
    }

    /**
     * Montant : droite; encadré(thin); Wrap; format #,##0.00
     * 
     * @return le style liste pour un montant
     */
    protected HSSFCellStyle getStyleMontantRed() {
        if (isNull(styleMontantRed)) {
            styleMontantRed = styleFactory.createStyle(getFontHeight(), false, false, Alignement.RIGHT, Border.NONE,
                    FieldType.MONTANT);
        }
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.RED.index);
        styleMontantRed.setFont(font);
        return styleMontantRed;
    }

    /**
     * gauche, italique, rouge
     * 
     * @return le style des critères des listes
     */
    protected HSSFCellStyle getStyleCritereRed() {
        if (isNull(styleCritere)) {
            styleCritere = styleFactory.createStyle(getFontHeight(), true, Alignement.LEFT, HSSFColor.RED.index);
        }
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.RED.index);
        styleCritere.setFont(font);
        return styleCritere;
    }

    /**
     * droite; encadré(thin); wrap
     * 
     * @return le style liste aligné à droite
     */
    protected HSSFCellStyle getStyleListRightRed() {
        if (isNull(styleRightRed)) {
            styleRightRed = styleFactory.createStyle(getFontHeight(), Alignement.RIGHT, Border.NONE);
        }
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.RED.index);
        styleRightRed.setFont(font);
        return styleRightRed;
    }

    /**
     * droite, italique
     * 
     * @return le style titre pour les critères des listes
     */
    protected HSSFCellStyle getStyleCritereTitle() {
        if (isNull(styleTitreCritere)) {
            styleTitreCritere = styleFactory.createStyle(getFontHeight(), true, Alignement.RIGHT);
        }
        return styleTitreCritere;
    }

    /**
     * Grise la cellule à 25%
     * 
     * @return
     */
    protected HSSFCellStyle getStyleGris25Pourcent() {
        if (isNull(styleGris25Pourcent)) {
            styleGris25Pourcent = styleFactory.createStyle(getFontHeight(), false, Alignement.LEFT,
                    HSSFColor.GREY_25_PERCENT.index);
        }
        return styleGris25Pourcent;
    }

    /**
     * Grise la cellule à 25% et ajoute du gras
     */
    protected HSSFCellStyle getStyleGris25PourcentGras() {
        if (isNull(styleGris25PourcentGras)) {
            styleGris25PourcentGras = styleFactory.createStyle(getFontHeight(), false, true, Alignement.LEFT,
                    HSSFColor.GREY_25_PERCENT.index);
        }
        return styleGris25PourcentGras;
    }

    protected HSSFCellStyle getStyleGris25PourcentGrasMontant() {
        if (isNull(styleGris25PourcentGrasMontant)) {
            styleGris25PourcentGrasMontant = styleFactory.createStyle(getFontHeight(), false, true, Alignement.RIGHT,
                    HSSFColor.GREY_25_PERCENT.index, Border.NONE, FieldType.MONTANT);
        }

        return styleGris25PourcentGrasMontant;

    }

    /**
     * Grise la cellule à 25% et ajoute du gras pour un tableau
     */
    protected HSSFCellStyle getStyleListGris25PourcentGras() {
        if (isNull(styleListGris25PourcentGras)) {
            styleListGris25PourcentGras = styleFactory.createStyle(getFontHeight(), false, true, Alignement.LEFT,
                    HSSFColor.GREY_25_PERCENT.index, Border.THIN);
        }
        return styleListGris25PourcentGras;
    }

    /**
     * Grise la cellule à 25% et ajoute du gras pour un tableau
     */
    protected HSSFCellStyle getStyleListGris25Pourcent() {
        if (isNull(styleListGris25PourcentGras)) {
            styleListGris25PourcentGras = styleFactory.createStyle(getFontHeight(), false, false, Alignement.LEFT,
                    HSSFColor.GREY_25_PERCENT.index, Border.THIN);
        }
        return styleListGris25PourcentGras;
    }

    /**
     * Grise la cellule à 25% et ajoute du gras pour un tableau pour un montant
     */
    protected HSSFCellStyle getStyleListMontantGris25PourcentGras() {
        if (isNull(styleListMontantGris25PourcentGras)) {
            styleListMontantGris25PourcentGras = styleFactory.createStyle(getFontHeight(), false, true,
                    Alignement.RIGHT, HSSFColor.GREY_25_PERCENT.index, Border.THIN, FieldType.MONTANT);
        }
        return styleListMontantGris25PourcentGras;
    }

    /**
     * Grise la cellule à 25% et ajoute du gras pour un tableau pour un montant
     */
    protected HSSFCellStyle getStyleListMontant() {
        if (isNull(styleListMontant)) {
            styleListMontant = styleFactory.createStyle(getFontHeight(), false, true, Alignement.RIGHT, Border.THIN,
                    FieldType.MONTANT);
        }
        return styleListMontant;
    }

    /**
     * Grise la cellule à 25% et ajoute du gras pour un tableau pour un nombre à une décimale
     */
    protected HSSFCellStyle getStyleListNombreD1Gris25PourcentGras() {
        if (isNull(styleListNombre1DGris25PourcentGras)) {
            styleListNombre1DGris25PourcentGras = styleFactory.createStyle(getFontHeight(), false, true,
                    Alignement.RIGHT, HSSFColor.GREY_25_PERCENT.index, Border.THIN, FieldType.NOMBRE_1D);
        }
        return styleListNombre1DGris25PourcentGras;
    }

    /**
     * centré; encadré(thin); wrap
     * 
     * @return le style liste centré
     */
    protected HSSFCellStyle getStyleListCenter() {
        if (isNull(styleCenter)) {
            styleCenter = styleFactory.createStyle(getFontHeight(), Alignement.CENTER, Border.THIN);
        }
        return styleCenter;
    }

    /**
     * gauche; encadré(thin); Wrap
     * 
     * @return le style liste aligné à gauche
     */
    protected HSSFCellStyle getStyleListLeft() {
        if (isNull(styleLeft)) {
            styleLeft = styleFactory.createStyle(getFontHeight(), Border.THIN);
        }
        return styleLeft;
    }

    /**
     * gauche; encadré(thin); Wrap
     * 
     * @return le style liste aligné à gauche
     */
    protected HSSFCellStyle getStyleListLeftNone() {
        if (isNull(styleListLeftNone)) {
            styleListLeftNone = styleFactory.createStyle(getFontHeight(), Border.NONE);
        }
        return styleListLeftNone;
    }

    /**
     * droite; encadré(thin); wrap
     * 
     * @return le style liste aligné à droite
     */
    protected HSSFCellStyle getStyleListRight() {
        if (isNull(styleRight)) {
            styleRight = styleFactory.createStyle(getFontHeight(), Alignement.RIGHT, Border.THIN);
        }
        return styleRight;
    }

    /**
     * droite; wrap
     * 
     * @return le style liste aligné à droite
     */
    protected HSSFCellStyle getStyleListRightNone() {
        if (isNull(styleRightNone)) {
            styleRightNone = styleFactory.createStyle(getFontHeight(), Alignement.RIGHT, Border.NONE);
        }
        return styleRightNone;
    }

    /**
     * droite; encadré(thin); wrap
     * 
     * @return le style liste aligné à droite
     */
    protected HSSFCellStyle getStyleListRightNoneGreen() {
        if (isNull(styleRightNoneGreen)) {
            styleRightNoneGreen = styleFactory.createStyle(getFontHeight(), Alignement.RIGHT, Border.NONE);
        }
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.GREEN.index);
        styleRightNoneGreen.setFont(font);

        return styleRightNoneGreen;
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
        if (isNull(styleListTitleCenter)) {
            styleListTitleCenter = styleFactory.createStyle(getFontHeight(), false, true, Alignement.CENTER,
                    Border.MEDIUM);
        }
        return styleListTitleCenter;
    }

    /**
     * gauche; gras; encadré(medium); Wrap
     * 
     * @return le style titre aligné à gauche
     */
    protected HSSFCellStyle getStyleListTitleLeft() {
        if (isNull(styleListTitleLeft)) {
            styleListTitleLeft = styleFactory.createStyle(getFontHeight(), false, true, Alignement.LEFT, Border.MEDIUM);
        }
        return styleListTitleLeft;
    }

    /**
     * gauche; gras; encadré(medium); Wrap
     * 
     * @return le style titre aligné à droite
     */
    protected HSSFCellStyle getStyleListTitleRight() {
        if (isNull(styleListTitleRight)) {
            styleListTitleRight = styleFactory.createStyle(getFontHeight(), false, true, Alignement.RIGHT,
                    Border.MEDIUM);
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
        if (isNull(styleTitleVerticalTopCenter)) {
            styleTitleVerticalTopCenter = styleFactory.createStyle(getFontHeight(), false, true, Alignement.RIGHT,
                    VerticalAlignement.TOP, Border.THIN);
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
        if (isNull(styleVerticalBottomCenter)) {
            styleVerticalBottomCenter = styleFactory.createStyle(getFontHeight(), Alignement.CENTER,
                    VerticalAlignement.BOTTOM, Border.THIN);
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
            styleVerticalBottomLeft = styleFactory.createStyle(getFontHeight(), Alignement.LEFT,
                    VerticalAlignement.BOTTOM, Border.THIN);
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
        if (isNull(styleVerticalBottomRight)) {
            styleVerticalBottomRight = styleFactory.createStyle(getFontHeight(), Alignement.RIGHT,
                    VerticalAlignement.BOTTOM, Border.THIN);
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
        if (isNull(styleVerticalTopCenter)) {
            styleVerticalTopCenter = styleFactory.createStyle(getFontHeight(), Alignement.CENTER,
                    VerticalAlignement.TOP, Border.THIN);
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
        if (isNull(styleVerticalTopLeft)) {
            styleVerticalTopLeft = styleFactory.createStyle(getFontHeight(), Alignement.LEFT, VerticalAlignement.TOP,
                    Border.THIN);
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
        if (isNull(styleVerticalTopRight)) {
            styleVerticalTopRight = styleFactory.createStyle(getFontHeight(), Alignement.RIGHT, VerticalAlignement.TOP,
                    Border.THIN);
        }
        return styleVerticalTopRight;
    }

    /**
     * Montant : droite; encadré(thin); Wrap; format #,##0.00
     * 
     * @return le style liste pour un montant
     */
    protected HSSFCellStyle getStyleMontant() {
        if (isNull(styleMontant)) {
            styleMontant = styleFactory.createStyle(getFontHeight(), false, false, Alignement.RIGHT, Border.THIN,
                    FieldType.MONTANT);
        }
        return styleMontant;
    }

    /**
     * Montant : droite; encadré(thin); Wrap; format #,##0.00
     * 
     * @return le style liste pour un montant
     */
    protected HSSFCellStyle getStyleMontantNoBorder() {
        if (isNull(styleMontantNoBorder)) {
            styleMontantNoBorder = styleFactory.createStyle(getFontHeight(), false, false, Alignement.RIGHT,
                    Border.NONE, FieldType.MONTANT);
        }
        return styleMontantNoBorder;
    }

    /**
     * Montant : droite; encadré(thin); Wrap; format #,##0.00
     * 
     * @return le style liste pour un montant
     */
    protected HSSFCellStyle getStyleMontantNone() {
        if (isNull(styleMontantNone)) {
            styleMontantNone = styleFactory.createStyle(getFontHeight(), false, false, Alignement.RIGHT, Border.NONE,
                    FieldType.MONTANT);
        }
        return styleMontantNone;
    }

    /**
     * Montant : droite; encadré(thin); Wrap; format #,##0.00
     * 
     * @return le style liste pour un montant
     */
    protected HSSFCellStyle getStyleMontantNoneGreen() {
        if (isNull(styleMontantNoneGreen)) {
            styleMontantNoneGreen = styleFactory.createStyle(getFontHeight(), false, false, Alignement.RIGHT,
                    Border.NONE, FieldType.MONTANT);
        }
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.GREEN.index);
        styleMontantNoneGreen.setFont(font);

        return styleMontantNoneGreen;
    }

    /**
     * Nombre à une décimale : droite; encadré(thin); Wrap; format 0.0
     * 
     * @return le style liste pour un montant
     */
    protected HSSFCellStyle getStyleNombre1D() {
        if (isNull(styleNombre1D)) {
            styleNombre1D = styleFactory.createStyle(getFontHeight(), false, false, Alignement.RIGHT, Border.THIN,
                    FieldType.NOMBRE_1D);
        }
        return styleNombre1D;
    }

    /**
     * Total : droite; gras; encadré(thin); Wrap; format #,##0.00
     * 
     * @return le style liste pour un montant total
     */
    protected HSSFCellStyle getStyleMontantTotal() {
        if (isNull(styleTotal)) {
            styleTotal = styleFactory.createStyle(getFontHeight(), false, true, Alignement.RIGHT, Border.THIN,
                    FieldType.MONTANT);
        }
        return styleTotal;
    }

    /**
     * Total : droite; gras; encadré(medium); Wrap; format #,##0.00
     * 
     * @return le style liste pour un montant total
     */
    protected HSSFCellStyle getStyleMontantBorderMediumTotal() {
        if (isNull(styleMontantBorderMediumTotal)) {
            styleMontantBorderMediumTotal = styleFactory.createStyle(getFontHeight(), false, true, Alignement.RIGHT,
                    Border.MEDIUM, FieldType.MONTANT);
        }
        return styleMontantBorderMediumTotal;
    }

    /**
     * Pourcent : droite;encadré(medium); Wrap; format 0.00%
     * 
     * @return le style liste pour un pourcentage
     */
    protected HSSFCellStyle getStylePourcent() {
        if (isNull(stylePourcent)) {
            stylePourcent = styleFactory.createStyle(getFontHeight(), false, false, Alignement.RIGHT, Border.THIN,
                    FieldType.POURCENT);
        }
        return stylePourcent;
    }

    /**
     * Pourcent : droite;encadré(medium); Wrap; format 0.00%
     * 
     * @return le style liste pour un pourcentage
     */
    protected HSSFCellStyle getStylePourcentNone() {
        if (isNull(stylePourcentNone)) {
            stylePourcentNone = styleFactory.createStyle(getFontHeight(), false, false, Alignement.RIGHT, Border.NONE,
                    FieldType.POURCENT);
        }
        return stylePourcentNone;
    }

    /**
     * Pourcent : droite;encadré(medium); Wrap; format 0.00%
     * 
     * @return le style liste pour un pourcentage
     */
    protected HSSFCellStyle getStylePourcentNoneGreen() {
        if (isNull(stylePourcentNoneGreen)) {
            stylePourcentNoneGreen = styleFactory.createStyle(getFontHeight(), false, false, Alignement.RIGHT,
                    Border.NONE, FieldType.POURCENT);
        }
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.GREEN.index);
        stylePourcentNoneGreen.setFont(font);

        return stylePourcentNoneGreen;
    }

    protected HSSFCellStyle getStyleGras() {
        if (isNull(styleGras)) {
            styleGras = styleFactory.createStyle(getFontHeight(), false, true, Alignement.LEFT, Border.NONE,
                    FieldType.STANDARD);
        }
        return styleGras;
    }

    private boolean isNull(Object object) {
        return object == null;
    }

    /**
     * @return le classeur
     */
    protected HSSFWorkbook getWorkbook() {
        return wb;
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
    protected HSSFSheet initTitleRow(ArrayList<ParamTitle> col_titles) {
        HSSFRow row = null;
        HSSFCell c;

        // create Title Row
        createRow(); // Ligne vide
        row = currentSheet.createRow(currentSheet.getPhysicalNumberOfRows());
        int i = 0;
        for (i = 0; i < col_titles.size(); i++) {
            // set cell value
            c = row.createCell((short) i);
            c.setCellValue((col_titles.get(i)).getTitle());
            c.setCellStyle((col_titles.get(i)).getStyle());
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
        setColNum(cellNum);
        currentCell = currentRow.getCell(cellNum);
    }

    /**
     * Initialise la cellule courante avec le numéro de cellule passsé en paramètre.
     * 
     * @param short cellNum
     */
    public void setCurrentCell(int cellNum) {
        setColNum((short) cellNum);
        currentCell = currentRow.getCell((short) cellNum);
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
     * @param documentInfo
     *            the documentInfo to set
     */
    public void setDocumentInfo(JadePublishDocumentInfo documentInfo) {
        this.documentInfo = documentInfo;
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

    /**
     * Retourne le label correspondant à la chaîne associée.
     * 
     * @param label String représentant un label
     */
    protected String getLabel(String label) {
        return getSession().getLabel(label);
    }

    /**
     * Retourne le label correspondant au code système passé en paramètre.
     * 
     * @param codeSysteme String représentant un code système
     * @return String représentant une correspondance de code système
     */
    protected String getCodeLibelle(String codeSysteme) {
        return getSession().getCodeLibelle(codeSysteme);
    }

    /**
     * Retourne le label court correspondant au code système passé en paramètre.
     * 
     * @param codeSysteme String représentant un code système
     * @return String représentant une correspondance d'un code système
     */
    protected String getCode(String codeSysteme) {
        return getSession().getCode(codeSysteme);
    }

    /**
     * Définit si l'on souhaite ou non disposer d'un header sur le document. (défaut true)
     * 
     * @param wantHeader
     */
    public void setWantHeader(boolean wantHeader) {
        this.wantHeader = wantHeader;
    }

    /**
     * Définit si l'on souhaite ou non disposer d'un footer sur le document. (défaut true)
     * 
     * @param wantFooter
     */
    public void setWantFooter(boolean wantFooter) {
        this.wantFooter = wantFooter;
    }

    public CellReference getCurrentCellReference() {
        return new CellReference(currentRow.getRowNum(), colNum);
    }

    public CellReference getLeftCellReference() {
        return new CellReference(currentRow.getRowNum(), colNum - 1);
    }

    /**
     * Retourne la référence de la case actuelle (A1,A2,A3 etc..)
     * 
     * @return
     */
    public String getCurrentCellReferenceValue() {
        return getCurrentCellReference().toString();
    }

    /**
     * Retourne la référence de la case se situant à droite
     */
    public String getRightCellReferenceValue() {
        return new CellReference(currentRow.getRowNum(), colNum + 1).toString();
    }

    /**
     * Retourne la référence de la case se situant à gauche
     */
    public String getLeftCellReferenceValue() {
        return getLeftCellReference().toString();
    }

    /**
     * Retourne la référence de la case se situant en-dessus
     */
    public String getTopCellReferenceValue() {
        return getTopCellReferenceValue(1);
    }

    public CellReference getTopCellReference() {
        return getTopCellReference(1);
    }

    public CellReference getTopCellReference(int nb) {
        return new CellReference(currentRow.getRowNum() - nb, colNum);
    }

    public String getTopCellReferenceValue(CellReference cellReference, int nb) {
        CellReference copy = new CellReference(cellReference.getRow() - nb, cellReference.getCol());
        return copy.toString();
    }

    public String getTopCellReferenceValue(int nb) {
        CellReference copy = new CellReference(getCurrentCellReference().getRow() - nb, getCurrentCellReference()
                .getCol());
        return copy.toString();
    }

    public String getReferenceValueFromCurrentCell(int topRows, int leftRows) {
        CellReference copy = new CellReference(getCurrentCellReference().getRow() - topRows, getCurrentCellReference()
                .getCol() - leftRows);
        return copy.toString();
    }

    public String getTopCellsReferenceValue(CellReference cellReference, int nb) {
        CellReference copy = new CellReference(cellReference.getRow() - nb, cellReference.getCol());
        return cellReference.toString() + ":" + copy.toString();
    }

    public String getRangeBetween(String reference1, String reference2) {
        return reference1 + ":" + reference2;
    }
}
