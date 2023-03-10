/**
 *
 */
package globaz.aquila.print.list;

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

/**
 * Classe g?n?rique pour toutes les listes Excel
 * 
 * @author SEL
 * 
 */
public abstract class COAbstractListExcel {
    protected static final double BOTTOM_MARGIN = 0.6;
    protected static final short COLUMN_WIDTH_COMPTEANNEXE = 18000;

    /** Largeur d'une colonne contenant une date */
    protected static final short COLUMN_WIDTH_DATE = 3500;
    /** Largeur d'une colonne contenant un montant */
    protected static final short COLUMN_WIDTH_MONTANT = 4500;
    protected static final short COLUMN_WIDTH_REMARQUE = 7000;
    protected static final short COLUMN_WIDTH_SECTION = 4000;
    private static final String DEFAULT_FILE_NAME = "Liste";
    private static final String FILE_TYPE = ".xls";
    /** Format : "#,##0.00" */
    protected static final short FORMAT_MONTANT = (short) 4;
    protected static final short FORMAT_NOMBRE = (short) 1;
    protected static final double LEFT_MARGIN = 0.4;
    /** Nombre maximum de ligne par feuille */
    private static final int NUMBER_MAX_ROW = 65535;
    /** S?paratuer entre le N? de page et le nb de page */
    private static final String PAGE_SEPARATOR = "/";
    protected static final double RIGHT_MARGIN = 0.4;
    /** Longueur maximum pour le nom de la feuille */
    protected static final int SHEET_MAX_LENGTH = 31;
    /**
     * Nom par d?faut d'une feuille cr??e automatiquement quand le nb de ligne d?passe NUMBER_MAX_ROW
     */
    private static final String SUITE = "Suite ";
    protected static final double TOP_MARGIN = 0.6;

    private int colNum = 0; // D?termine la colonne en cours

    /** D?termine le nb de page si le contenu d?passe NUMBER_MAX_ROW lignes. */
    private int cptPage = 0;
    private HSSFCell currentCell;
    private HSSFRow currentRow;

    /** Feuille Excel courante */
    protected HSSFSheet currentSheet;
    private String dateImpression = "";
    private JadePublishDocumentInfo documentInfo = null;
    private String documentTitle = "";
    private String filenameRoot = "";

    private BSession session;

    private HSSFCellStyle styleCenter = null;

    private HSSFCellStyle styleCreance = null;

    private HSSFCellStyle styleLeft = null;
    private HSSFCellStyle styleListTitleCenter = null;
    private HSSFCellStyle styleListTitleLeft = null;
    private HSSFCellStyle styleListTitleRight = null;
    private HSSFCellStyle styleMontant = null;
    private HSSFCellStyle styleLeftWithoutBorder = null;

    // Les styles doivent ?tre globaux pour ?viter des probl?mes avec Excel
    private HSSFCellStyle styleNeutre = null;
    private HSSFCellStyle styleRight = null;
    private HSSFCellStyle styleTitreCreance = null;
    private HSSFCellStyle styleTotal = null;

    private HSSFCellStyle styleNombreWithoutMontant = null;
    private HSSFCellStyle styleMontantWithoutBorder = null;

    /** Workbook, classeur d'Excel */
    private HSSFWorkbook wb;

    /**
     * @param session
     * @param filenameRoot
     *            : nom du fichier
     * @param documentTitle
     */
    public COAbstractListExcel(BSession session, String filenameRoot, String documentTitle) {
        setSession(session);
        setFilenameRoot(filenameRoot);
        setDocumentTitle(documentTitle);
        setDateImpression(JACalendar.today().getDay() + "." + JACalendar.today().getMonth() + "."
                + JACalendar.today().getYear());
        wb = new HSSFWorkbook();
        documentInfo = new JadePublishDocumentInfo();
    }

    /**
     * @param value
     *            boolean
     * @return la cellule courante avec un style neutre (non param?tr?)
     */
    protected HSSFCell createCell(boolean value) {
        return this.createCell(value, wb.createCellStyle());
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
     * Cellule format?e pour un montant
     * 
     * @param value
     *            double
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
     * @param value
     *            boolean
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
     * @return la cellule courante avec un style neutre (non param?tr?)
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
     * @return la cellule courante avec un style neutre (non param?tr?)
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
        currentCell.setCellStyle(style);
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
     * Cr?e le pied de page
     * 
     * @param title
     * @return le pied de page
     */
    protected HSSFFooter createFooter(String refInforom) {
        // pied de page
        HSSFFooter footer = currentSheet.getFooter();
        footer.setRight(getSession().getLabel("PAGE") + HSSFFooter.page() + COAbstractListExcel.PAGE_SEPARATOR
                + HSSFFooter.numPages());
        footer.setLeft(getSession().getLabel("CACPAGE") + " : " + refInforom + " - "
                + this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.') + 1) + " - "
                + getDateImpression() + " - " + getSession().getUserName());
        return footer;
    }

    /**
     * Cr?e l'ent?te
     * 
     * @param title
     * @return l'ent?te
     */
    protected HSSFHeader createHeader() {
        // en-t?te
        documentInfo.setTemplateName("");
        getDocumentInfo().setDocumentTypeNumber(getNumeroInforom());
        HSSFHeader header = currentSheet.getHeader();
        header.setLeft(FWIImportProperties.getInstance().getProperty(documentInfo,
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        header.setRight(getDocumentTitle());
        return header;
    }

    /**
     * Cr?e une feuille Excel
     * 
     * @param la
     *            ligne courante
     */
    protected HSSFRow createRow() {
        colNum = 0;

        if (currentSheet.getPhysicalNumberOfRows() >= COAbstractListExcel.NUMBER_MAX_ROW) {
            cptPage++;
            createSheet(COAbstractListExcel.SUITE + cptPage);
        }

        return currentRow = currentSheet.createRow(currentSheet.getPhysicalNumberOfRows());
    }

    /**
     * Cr?e une feuille Excel
     * 
     * @param title
     * @return la feuille Excel courante
     */
    protected HSSFSheet createSheet(String title) {
        if (title.length() > COAbstractListExcel.SHEET_MAX_LENGTH) {
            title = title.substring(0, COAbstractListExcel.SHEET_MAX_LENGTH);
        }
        currentSheet = wb.createSheet(title);

        // Marges (unit? en pouces)
        currentSheet.setMargin(HSSFSheet.LeftMargin, COAbstractListExcel.LEFT_MARGIN);
        currentSheet.setMargin(HSSFSheet.RightMargin, COAbstractListExcel.RIGHT_MARGIN);
        currentSheet.setMargin(HSSFSheet.TopMargin, COAbstractListExcel.TOP_MARGIN);
        currentSheet.setMargin(HSSFSheet.BottomMargin, COAbstractListExcel.BOTTOM_MARGIN);

        return currentSheet;
    }

    /**
     * @return the currentSheet
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
     * d?fini une police gras
     * 
     * @param wb
     * @return la font gras
     */
    protected HSSFFont getFontBold() {
        HSSFFont fontBold = wb.createFont();
        // fontBold.setFontName(HSSFFont.FONT_ARIAL);
        fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        return fontBold;
    }

    /**
     * @return le num?ro InfoRom du document
     */
    public abstract String getNumeroInforom();

    /**
     * G?n?re le fichier Excel <br />
     * m?thode pour g?rer le fichier en sortie <br />
     * Cr?? le : 17 ao?t 06
     * 
     * @return le chemin absolu du fichier
     */
    public String getOutputFile() {
        try {
            File f = File.createTempFile((filenameRoot == null ? COAbstractListExcel.DEFAULT_FILE_NAME : filenameRoot)
                    + JACalendar.today().toStrAMJ() + "_", COAbstractListExcel.FILE_TYPE);
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
     * @return the session
     */
    protected BSession getSession() {
        return session;
    }

    /**
     * gauche, italique
     * 
     * @return le style des crit?res des listes
     */
    protected HSSFCellStyle getStyleCritere() {
        if (styleCreance == null) {
            HSSFFont font = wb.createFont();
            font.setItalic(true);
            styleCreance = wb.createCellStyle();
            styleCreance.setFont(font);
            styleCreance.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        }
        return styleCreance;
    }

    /**
     * droite, italique
     * 
     * @return le style titre pour les crit?res des listes
     */
    protected HSSFCellStyle getStyleCritereTitle() {
        if (styleTitreCreance == null) {
            HSSFFont font = wb.createFont();
            font.setItalic(true);
            styleTitreCreance = wb.createCellStyle();
            styleTitreCreance.setFont(font);
            styleTitreCreance.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        }
        return styleTitreCreance;
    }

    /**
     * centr?; encadr?(thin); wrap
     * 
     * @return le style liste centr?
     */
    protected HSSFCellStyle getStyleListCenter() {
        if (styleCenter == null) {
            styleCenter = wb.createCellStyle();
            styleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            styleCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            styleCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
            styleCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);

            styleCenter.setWrapText(true); // La largeur de la cellule s'adapte au contenu
        }
        return styleCenter;
    }

    /**
     * gauche; encadr?(thin); Wrap
     * 
     * @return le style liste align? ? gauche
     */
    protected HSSFCellStyle getStyleListLeft() {
        if (styleLeft == null) {
            styleLeft = wb.createCellStyle();
            styleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);

            styleLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            styleLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
            styleLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);

            styleLeft.setWrapText(true); // La largeur de la cellule s'adapte au contenu
        }
        return styleLeft;
    }

    protected HSSFCellStyle getStyleLeftWihtoutBorder() {
        if (styleLeftWithoutBorder == null) {
            styleLeftWithoutBorder = wb.createCellStyle();
            styleLeftWithoutBorder.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            styleLeftWithoutBorder.setWrapText(true); // La largeur de la cellule s'adapte au contenu
        }
        return styleLeftWithoutBorder;
    }

    /**
     * droite; encadr?(thin); wrap
     * 
     * @return le style liste align? ? droite
     */
    protected HSSFCellStyle getStyleListRight() {
        if (styleRight == null) {
            styleRight = wb.createCellStyle();
            styleRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

            styleRight.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            styleRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleRight.setBorderRight(HSSFCellStyle.BORDER_THIN);
            styleRight.setBorderTop(HSSFCellStyle.BORDER_THIN);

            styleRight.setWrapText(true); // La largeur de la cellule s'adapte au contenu
        }
        return styleRight;
    }

    /**
     * centr?; gras; encadr?(medium); Wrap
     * 
     * @author: sel Cr?? le : 20 oct. 06
     * @param wb
     * @param font
     * @return le style pour les titres des listes
     */
    protected HSSFCellStyle getStyleListTitleCenter() {
        if (styleListTitleCenter == null) {
            // D?finition du style align? au centre
            styleListTitleCenter = wb.createCellStyle();
            styleListTitleCenter.setFont(getFontBold());
            styleListTitleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            styleListTitleCenter.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
            styleListTitleCenter.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
            styleListTitleCenter.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
            styleListTitleCenter.setBorderTop(HSSFCellStyle.BORDER_MEDIUM); // BORDER_THIN);

            styleListTitleCenter.setWrapText(true); // La largeur de la cellule s'adapte au contenu
        }
        return styleListTitleCenter;
    }

    /**
     * gauche; gras; encadr?(medium); Wrap
     * 
     * @return le style titre align? ? gauche
     */
    protected HSSFCellStyle getStyleListTitleLeft() {
        if (styleListTitleLeft == null) {
            styleListTitleLeft = wb.createCellStyle();
            styleListTitleLeft.setFont(getFontBold());
            styleListTitleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);

            styleListTitleLeft.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
            styleListTitleLeft.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
            styleListTitleLeft.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
            styleListTitleLeft.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);

            styleListTitleLeft.setWrapText(true); // La largeur de la cellule s'adapte au contenu
        }
        return styleListTitleLeft;
    }

    /**
     * gauche; gras; encadr?(medium); Wrap
     * 
     * @return le style titre align? ? droite
     */
    protected HSSFCellStyle getStyleListTitleRight() {
        if (styleListTitleRight == null) {
            styleListTitleRight = wb.createCellStyle();
            styleListTitleRight.setFont(getFontBold());
            styleListTitleRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

            styleListTitleRight.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
            styleListTitleRight.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
            styleListTitleRight.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
            styleListTitleRight.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);

            styleListTitleRight.setWrapText(true); // La largeur de la cellule s'adapte au contenu
        }
        return styleListTitleRight;
    }

    /**
     * Montant : droite; encadr?(thin); Wrap; format #,##0.00
     * 
     * @return le style liste pour un montant
     */
    protected HSSFCellStyle getStyleMontant() {
        if (styleMontant == null) {
            styleMontant = wb.createCellStyle();
            // setDataFormat(wb.createDataFormat().getFormat("#,##0.00")) == setDataFormat((short) 4)
            styleMontant.setDataFormat(COAbstractListExcel.FORMAT_MONTANT);
            styleMontant.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

            styleMontant.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            styleMontant.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleMontant.setBorderRight(HSSFCellStyle.BORDER_THIN);
            styleMontant.setBorderTop(HSSFCellStyle.BORDER_THIN);

            styleMontant.setWrapText(true); // La largeur de la cellule s'adapte au contenu
        }
        return styleMontant;
    }

    protected HSSFCellStyle getStyleMontantWithoutBorder() {
        if (styleMontantWithoutBorder == null) {
            styleMontantWithoutBorder = wb.createCellStyle();
            // setDataFormat(wb.createDataFormat().getFormat("#,##0.00")) == setDataFormat((short) 4)
            styleMontantWithoutBorder.setDataFormat(COAbstractListExcel.FORMAT_MONTANT);
            styleMontantWithoutBorder.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

            styleMontantWithoutBorder.setWrapText(true); // La largeur de la cellule s'adapte au contenu
        }
        return styleMontantWithoutBorder;
    }

    /**
     * Total : droite; gras; encadr?(medium); Wrap; format #,##0.00
     * 
     * @return le style liste pour un montant total
     */
    protected HSSFCellStyle getStyleMontantTotal() {
        if (styleTotal == null) {
            styleTotal = wb.createCellStyle();

            styleTotal.setDataFormat(COAbstractListExcel.FORMAT_MONTANT);
            styleTotal.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

            styleTotal.setFont(getFontBold());
            styleTotal.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
            styleTotal.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
            styleTotal.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
            styleTotal.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);

            styleTotal.setWrapText(true); // La cellule s'adapte au contenu
        }
        return styleTotal;
    }

    protected HSSFCellStyle getStyleNombreWithoutBorder() {
        if (styleNombreWithoutMontant == null) {
            styleNombreWithoutMontant = wb.createCellStyle();
            // setDataFormat(wb.createDataFormat().getFormat("#,##0.00")) == setDataFormat((short) 4)
            styleNombreWithoutMontant.setDataFormat(FORMAT_NOMBRE);
            styleNombreWithoutMontant.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

            styleNombreWithoutMontant.setWrapText(true); // La largeur de la cellule s'adapte au contenu
        }
        return styleNombreWithoutMontant;
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
     * @return
     */
    protected HSSFPrintSetup initPage(boolean landScape) {
        HSSFPrintSetup ps = currentSheet.getPrintSetup();
        // format
        ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        ps.setLandscape(landScape);
        // marges en-t?te/pied de page
        ps.setHeaderMargin(0);
        ps.setFooterMargin(0);

        return ps;
    }

    /**
     * Methode setTitleRow. Permet de d?finir l'ent?te des colonnes
     * 
     * @author: sel Cr?? le : 05.06.2007
     * @param currentSheet
     * @param col_titles
     * @return la feuille Excel courante
     */
    protected HSSFSheet initTitleRow(ArrayList<String> col_titles) {
        HSSFRow row = null;
        HSSFCell c;
        HSSFCellStyle styleCenter = getStyleListTitleCenter();
        // styleCenter.setFillPattern(HSSFCellStyle.FINE_DOTS ); //points en trame de fond

        // create Title Row
        createRow(); // Ligne vide
        row = currentSheet.createRow(currentSheet.getPhysicalNumberOfRows());
        int i = 0;
        for (i = 0; i < col_titles.size(); i++) {
            // set cell value
            c = row.createCell((short) i);
            c.setCellValue(col_titles.get(i));
            c.setCellStyle(styleCenter);
        }

        return currentSheet;
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
