/**
 *
 */
package globaz.corvus.excel;

import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.JadeCodingUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Classe générique pour toutes les listes Excel
 * 
 * @author BSC
 * 
 */
public abstract class REAbstractListExcel {
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
    protected static final double LEFT_MARGIN = 0.4;
    /** Séparatuer entre le N° de page et le nb de page */
    private static final String PAGE_SEPARATOR = "/";
    protected static final double RIGHT_MARGIN = 0.4;
    /** Longueur maximum pour le nom de la feuille */
    protected static final int SHEET_MAX_LENGTH = 31;
    protected static final double TOP_MARGIN = 0.6;

    private int colNum = 0; // Détermine la colonne en cours

    private HSSFCell currentCell;
    private HSSFRow currentRow;
    /** Feuille Excel courante */
    protected HSSFSheet currentSheet;

    private String dateImpression = "";
    private String documentTitle = "";
    private String filenameRoot = "";
    private BSession session;
    private HSSFCellStyle styleCenter = null;

    private HSSFCellStyle styleCreance = null;
    private HSSFCellStyle styleDate = null;
    private HSSFCellStyle styleLeft = null;
    private HSSFCellStyle styleLeftBorderTop = null;
    private HSSFCellStyle styleListTitleCenter = null;
    private HSSFCellStyle styleListTitleLeft = null;
    private HSSFCellStyle styleListTitleRight = null;
    private HSSFCellStyle styleMontant = null;
    private HSSFCellStyle styleNombre;
    // Les styles doivent être globaux pour éviter des problèmes avec Excel
    private HSSFCellStyle styleNeutre = null;
    private HSSFCellStyle styleRight = null;
    private HSSFCellStyle styleTitreCreance = null;
    private HSSFCellStyle styleTotal = null;
    //Pandemie Style
    private HSSFCellStyle styleBold;
    private HSSFCellStyle styleRightMontant;
    private HSSFCellStyle styleRightBorderTopMontant;
    private HSSFCellStyle styleRightBold;
    private HSSFCellStyle styleCenterNoBorder;
    private HSSFCellStyle styleCenterBorderTop;
    private HSSFCellStyle styleLeftCenterVert;
    private HSSFCellStyle styleRightMontantVert;
    private String fileName="";

    /** Workbook, classeur d'Excel */
    private HSSFWorkbook wb;

    /**
     * @param session
     * @param filenameRoot
     *            : nom du fichier
     * @param documentTitle
     */
    public REAbstractListExcel(BSession session, String filenameRoot, String documentTitle) {
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
     * Crée le pied de page
     * 
     * @param title
     * @return le pied de page
     */
    protected HSSFFooter createFooter() {
        // pied de page
        HSSFFooter footer = currentSheet.getFooter();
        footer.setRight(getSession().getLabel("PAGE") + HSSFFooter.page() + PAGE_SEPARATOR + HSSFFooter.numPages());
        footer.setLeft(getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1) + " - "
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
        JadePublishDocumentInfo docInfo = new JadePublishDocumentInfo();
        header.setLeft(FWIImportProperties.getInstance().getProperty(docInfo,
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        header.setRight(getDocumentTitle());
        return header;
    }

    /**
     * Crée une feuille Excel
     * 
     * @param la
     *            ligne courante
     */
    protected HSSFRow createRow() {
        colNum = 0;
        return currentRow = currentSheet.createRow(currentSheet.getPhysicalNumberOfRows());
    }

    /**
     * Crée une feuille Excel
     * 
     * @param title
     * @return la feuille Excel courante
     */
    protected HSSFSheet createSheet(String title) {
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
        fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        return fontBold;
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

    public String getOutputFile(String path) {
        try {
            File f = new File(path +(filenameRoot == null ? DEFAULT_FILE_NAME : filenameRoot)
                    + JACalendar.today().toString() + "_"+JadeUUIDGenerator.createStringUUID() +FILE_TYPE);
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
    public String getOutputFile(String path,String numCaisse) {
        try {
            JADate date = JACalendar.today();
            File f = new File(path +(filenameRoot == null ? DEFAULT_FILE_NAME : filenameRoot)
                    +"_"+numCaisse+"_"+date.getDay()+"."+String.format("%02d", date.getMonth())+"."+date.getYear()+FILE_TYPE);
            f.deleteOnExit();
            FileOutputStream out = new FileOutputStream(f);
            wb.write(out);
            out.close();
            fileName = f.getName();
            return f.getAbsolutePath();
        } catch (Exception e) {
            JadeCodingUtil.catchException(this, "getOutputFile", e);
            return "";
        }
    }


    public String getFileNameWithoutExtention(){
        return fileName.substring(0,fileName.length()-4);
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
            styleTitreCreance = wb.createCellStyle();
            styleTitreCreance.setFont(font);
            styleTitreCreance.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        }
        return styleTitreCreance;
    }

    /**
     * Date : droite; encadré(thin); Wrap; format
     * 
     * @return le style liste pour une date
     */
    protected HSSFCellStyle getStyleDate() {
        if (styleDate == null) {
            styleDate = wb.createCellStyle();

            HSSFDataFormat dataFormat = getWorkbook().createDataFormat();
            short indexformat = dataFormat.getFormat("dd.mm.yyyy");

            styleDate.setDataFormat(indexformat);

            styleDate.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

            styleDate.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            styleDate.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleDate.setBorderRight(HSSFCellStyle.BORDER_THIN);
            styleDate.setBorderTop(HSSFCellStyle.BORDER_THIN);

            styleDate.setWrapText(true); // La largeur de la cellule s'adapte au
            // contenu
        }
        return styleDate;
    }

    /**
     * Date : droite; encadré(thin); Wrap; format
     * 
     * @return le style liste pour une date
     */
    protected HSSFCellStyle getStyleDateMMAAAA() {
        if (styleDate == null) {
            styleDate = wb.createCellStyle();

            HSSFDataFormat dataFormat = getWorkbook().createDataFormat();
            short indexformat = dataFormat.getFormat("mm.yyyy");

            styleDate.setDataFormat(indexformat);

            styleDate.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

            styleDate.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            styleDate.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleDate.setBorderRight(HSSFCellStyle.BORDER_THIN);
            styleDate.setBorderTop(HSSFCellStyle.BORDER_THIN);

            styleDate.setWrapText(true); // La largeur de la cellule s'adapte au
            // contenu
        }
        return styleDate;
    }

    /**
     * centré; encadré(thin); wrap
     * 
     * @return le style liste centré
     */
    protected HSSFCellStyle getStyleListCenter() {
        if (styleCenter == null) {
            styleCenter = wb.createCellStyle();
            styleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            styleCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            styleCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
            styleCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);

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
            styleLeft = wb.createCellStyle();
            styleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);

            styleLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            styleLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
            styleLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);

            styleLeft.setWrapText(true); // La largeur de la cellule s'adapte au
            // contenu
        }
        return styleLeft;
    }

    protected HSSFCellStyle getStyleLeftBorderTop() {
        if (styleLeftBorderTop == null) {
            styleLeftBorderTop = wb.createCellStyle();
            styleLeftBorderTop.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            styleLeftBorderTop.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        }
        return styleLeftBorderTop;
    }

    /**
     * gauche; encadré(thin); Wrap
     * 
     * @return le style liste aligné à gauche et GRAS
     */
    protected HSSFCellStyle getStyleListLeftBold() {
        if (styleLeft == null) {
            styleLeft = wb.createCellStyle();
            styleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            styleLeft.setFont(getFontBold());
            styleLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            styleLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
            styleLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);

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
            styleRight = wb.createCellStyle();
            styleRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

            styleRight.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            styleRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleRight.setBorderRight(HSSFCellStyle.BORDER_THIN);
            styleRight.setBorderTop(HSSFCellStyle.BORDER_THIN);

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

            styleListTitleCenter.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
            styleListTitleCenter.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
            styleListTitleCenter.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
            styleListTitleCenter.setBorderTop(HSSFCellStyle.BORDER_MEDIUM); // BORDER_THIN);

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

            styleListTitleLeft.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
            styleListTitleLeft.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
            styleListTitleLeft.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
            styleListTitleLeft.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);

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

            styleListTitleRight.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
            styleListTitleRight.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
            styleListTitleRight.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
            styleListTitleRight.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);

            styleListTitleRight.setWrapText(true); // La largeur de la cellule
            // s'adapte au contenu
        }
        return styleListTitleRight;
    }

    /**
     * Montant : droite; encadré(thin); Wrap; format #,##0.00
     * 
     * @return le style liste pour un montant
     */
    protected HSSFCellStyle getStyleMontant() {
        if (styleMontant == null) {
            styleMontant = wb.createCellStyle();
            // setDataFormat(wb.createDataFormat().getFormat("#,##0.00")) ==
            // setDataFormat((short) 4)
            styleMontant.setDataFormat(FORMAT_MONTANT);
            styleMontant.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

            styleMontant.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            styleMontant.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleMontant.setBorderRight(HSSFCellStyle.BORDER_THIN);
            styleMontant.setBorderTop(HSSFCellStyle.BORDER_THIN);

            styleMontant.setWrapText(true); // La largeur de la cellule s'adapte
            // au contenu
        }
        return styleMontant;
    }

    /**
     * Nombre : droite; encadré(thin); Wrap; format #,##0.00
     * 
     * @return le style liste pour un nombre
     */
    protected HSSFCellStyle getStyleNombre() {
        if (styleNombre == null) {
            styleNombre = wb.createCellStyle();
            styleNombre.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

            styleNombre.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            styleNombre.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleNombre.setBorderRight(HSSFCellStyle.BORDER_THIN);
            styleNombre.setBorderTop(HSSFCellStyle.BORDER_THIN);

            styleNombre.setWrapText(true); // La largeur de la cellule s'adapte
            // au contenu
        }
        return styleNombre;
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
            styleTotal.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
            styleTotal.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
            styleTotal.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
            styleTotal.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);

            styleTotal.setWrapText(true); // La cellule s'adapte au contenu
        }
        return styleTotal;
    }
    /**
     * Pandemie cell style
     */

    protected HSSFCellStyle getStyleBold() {
        if (styleBold == null) {
            styleBold = wb.createCellStyle();
            styleBold.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            styleBold.setFont(getFontBold());
        }
        return styleBold;
    }

    protected HSSFCellStyle getStyleRightMontant() {
        if (styleRightMontant == null) {
            styleRightMontant = wb.createCellStyle();
            styleRightMontant.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            styleRightMontant.setDataFormat(FORMAT_MONTANT);
        }
        return styleRightMontant;
    }

    protected HSSFCellStyle getStyleRightBold() {
        if (styleRightBold == null) {
            styleRightBold = wb.createCellStyle();
            styleRightBold.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            styleRightBold.setFont(getFontBold());
        }
        return styleRightBold;
    }
    protected HSSFCellStyle getStyleCenterNoBorder() {
        if (styleCenterNoBorder == null) {
            styleCenterNoBorder = wb.createCellStyle();
            styleCenterNoBorder = getWorkbook().createCellStyle();
            styleCenterNoBorder.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        }
        return styleCenterNoBorder;
    }
    protected HSSFCellStyle getStyleCenterBorderTop() {
        if (styleCenterBorderTop == null) {
            styleCenterBorderTop = wb.createCellStyle();
            styleCenterBorderTop.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            styleCenterBorderTop.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        }
        return styleCenterBorderTop;
    }

    protected HSSFCellStyle getStyleRightMontantBorderTop () {
        if (styleRightBorderTopMontant == null) {
            styleRightBorderTopMontant = wb.createCellStyle();
            styleRightBorderTopMontant.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            styleRightBorderTopMontant.setDataFormat(FORMAT_MONTANT);
            styleRightBorderTopMontant.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        }
        return styleRightBorderTopMontant;
    }

    protected HSSFCellStyle getStyleLeftCenterVert () {
        if (styleLeftCenterVert == null) {
            styleLeftCenterVert = wb.createCellStyle();
            styleLeftCenterVert.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        }
        return styleLeftCenterVert;
    }
    protected HSSFCellStyle getStyleRightMontantVert () {
        if (styleRightMontantVert== null) {
            styleRightMontantVert = wb.createCellStyle();
            styleRightMontantVert.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            styleRightMontantVert.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            styleRightMontantVert.setDataFormat(FORMAT_MONTANT);
        }
        return styleRightMontantVert;
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
        // marges en-tête/pied de page
        ps.setHeaderMargin(0);
        ps.setFooterMargin(0);

        return ps;
    }

    /**
     * Methode setTitleRow. Permet de définir l'entête des colonnes
     * 
     * @author: sel Créé le : 05.06.2007
     * @param currentSheet
     * @param col_titles
     * @return la feuille Excel courante
     */
    protected HSSFSheet initTitleRow(ArrayList col_titles) {
        HSSFRow row = null;
        HSSFCell c;
        HSSFCellStyle styleCenter = getStyleListTitleCenter();
        // styleCenter.setFillPattern(HSSFCellStyle.FINE_DOTS ); //points en
        // trame de fond

        // create Title Row
        createRow(); // Ligne vide
        row = currentSheet.createRow(currentSheet.getPhysicalNumberOfRows());
        int i = 0;
        for (i = 0; i < col_titles.size(); i++) {
            // set cell value
            c = row.createCell((short) i);
            c.setCellValue((String) col_titles.get(i));
            c.setCellStyle(styleCenter);
        }

        return currentSheet;
    }
    protected void createEmptyCell(int number) {
        createEmptyCell(number, "");
    }
    private void createEmptyCell(int number, String value) {
        if (styleNeutre == null) {
            styleNeutre = wb.createCellStyle();
        }
        createEmptyCell(number, value, styleNeutre);
    }
    private void createEmptyCell(int number, String value, HSSFCellStyle style) {

        for (int i = 0; i < number; i++) {
            createCell(value, style);
        }
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

    protected int getColNum(){
        return colNum;
    }
    protected JadeThreadContext initThreadContext(BSession session) {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles;
        try {
            roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                    .findAllIdRoleForIdUser(session.getUserId());
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);

        return context;
    }

}
