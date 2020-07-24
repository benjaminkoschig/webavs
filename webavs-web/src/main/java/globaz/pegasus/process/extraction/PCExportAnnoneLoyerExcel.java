package globaz.pegasus.process.extraction;

import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import globaz.aquila.print.list.elp.COELPException;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Crée le protocol retour pour l'exportation des loyers RPC
 * 
 * @author EBKO
 */
public class PCExportAnnoneLoyerExcel extends AbstractListExcel {

    public static final String NUMERO_REFERENCE_INFOROM = "";

    public static final String TITLE = "EXPORT LOYER";

    private static final Integer[] HEADER_RGB = {91, 155, 213};
    private static final Integer[] BACK_LIGNE_IMPAIRE_RGB = {221, 235, 247};
    private static final Integer[] BORD_LIGNE_RGB = {155, 194, 230};
    public static final String COL_NAP1 = "nap1";
    public static final String COL_CSAK_T = "csak_t";
    public static final String COL_MAMIM = "mamim";
    public static final String COL_MAMI = "mami";
    public static final String COL_CSWO = "cswo";
    public static final String COL_CSBE = "csbe";
    public static final String COL_CSKI_T = "cski_t";
    public static final String COL_MAMI_X = "mami_x";
    public static final String COL_MAMIP_X = "mamip_x";


    private List<PCExportAnnonceLoyerDonne> donnees;
    private HSSFCellStyle styleHeader = null;
    private HSSFCellStyle styleRightOdd = null;
    private HSSFCellStyle styleRightEven = null;
    private HSSFCellStyle styleLeftOdd = null;
    private HSSFCellStyle styleLeftEven = null;

    // créé la feuille xls
    public PCExportAnnoneLoyerExcel(BSession session, List<PCExportAnnonceLoyerDonne> donnees) throws COELPException {
        super(session, TITLE, TITLE + JACalendar.todayJJsMMsAAAA());
        this.donnees = donnees;
        initStyle();
        populateSheet();
    }

    @Override
    public void createContent() {
        // fait dans populateSheet avec un COELPException
    }

    @Override
    public String getNumeroInforom() {
        return PCExportAnnoneLoyerExcel.NUMERO_REFERENCE_INFOROM;
    }

    private void initStyle() {
        setColor(getWorkbook(), HSSFColor.ROYAL_BLUE.index, HEADER_RGB);
        setColor(getWorkbook(), HSSFColor.SKY_BLUE.index, BACK_LIGNE_IMPAIRE_RGB);
        setColor(getWorkbook(), HSSFColor.PALE_BLUE.index, BORD_LIGNE_RGB);

        HSSFFont fontHeader = getWorkbook().createFont();
        fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        fontHeader.setColor(HSSFColor.WHITE.index);
        styleHeader = getWorkbook().createCellStyle();
        styleHeader.setFont(fontHeader);
        styleHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleHeader.setBorderBottom(HSSFCellStyle.BORDER_NONE);
        styleHeader.setBorderLeft(HSSFCellStyle.BORDER_NONE);
        styleHeader.setBorderRight(HSSFCellStyle.BORDER_NONE);
        styleHeader.setBorderTop(HSSFCellStyle.BORDER_NONE);
        styleHeader.setWrapText(true);
        styleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styleHeader.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);

        styleRightOdd = createsStyleLigne(HSSFCellStyle.ALIGN_RIGHT);
        styleRightEven = createsStyleLigne(HSSFCellStyle.ALIGN_RIGHT);
        styleRightEven.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styleRightEven.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        styleLeftOdd = createsStyleLigne(HSSFCellStyle.ALIGN_LEFT);
        styleLeftEven = createsStyleLigne(HSSFCellStyle.ALIGN_LEFT);
        styleLeftEven.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styleLeftEven.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
    }

    private HSSFColor setColor(HSSFWorkbook workbook, int index, Integer[] colors){
        Byte r = colors[0].byteValue();
        Byte g = colors[1].byteValue();
        Byte b = colors[2].byteValue();
        HSSFPalette palette = workbook.getCustomPalette();
        HSSFColor hssfColor = null;
        hssfColor= palette.findColor(r, g, b);
        if (hssfColor == null ){
            palette.setColorAtIndex((short) index, r, g, b);
            hssfColor = palette.getColor((short) index);
        }
        return hssfColor;
    }

    private HSSFCellStyle createsStyleLigne(Short alignement) {
        HSSFCellStyle style = getWorkbook().createCellStyle();
        style.setBottomBorderColor(HSSFColor.PALE_BLUE.index);
        style.setTopBorderColor(HSSFColor.PALE_BLUE.index);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(alignement);
        style.setWrapText(true);
        return style;
    }

    /**
     * initialisation de feuille xls
     */
    private HSSFSheet populateSheet() {
        initOnglet();
        for(PCExportAnnonceLoyerDonne donnee : donnees){
            createRow();
            this.createCell(donnee.getNap1(), getStyleLeft());
            this.createCell(donnee.getCsak_t(), getStyleLeft());
            this.createCell(donnee.getMamim(), getStyleLeft());
            this.createCell(donnee.getMami(), getStyleLeft());
            this.createCell(donnee.getCswo(), getStyleLeft());
            this.createCell(donnee.getCsbe(), getStyleLeft());
            this.createCell(donnee.getCski_t(), getStyleLeft());
            this.createCell(donnee.getMami_x(), getStyleLeft());
            this.createCell(donnee.getMamip_x(), getStyleLeft());
        }
        formatSheet();
        return currentSheet;
    }

    /**
     * Création onglet
     */
    private void initOnglet() {
        // Titres des colonnes
        ArrayList<String> colTitles = new ArrayList<>();
        colTitles.add(COL_NAP1);
        colTitles.add(COL_CSAK_T);
        colTitles.add(COL_MAMIM);
        colTitles.add(COL_MAMI);
        colTitles.add(COL_CSWO);
        colTitles.add(COL_CSBE);
        colTitles.add(COL_CSKI_T);
        colTitles.add(COL_MAMI_X);
        colTitles.add(COL_MAMIP_X);

        createSheet(TITLE);

        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(PCExportAnnoneLoyerExcel.NUMERO_REFERENCE_INFOROM);

    }

    HSSFCellStyle getStyleLeft() {
        return isPair() ? styleLeftEven : styleLeftOdd;
    }

    HSSFCellStyle getStyleRight() {
        return isPair() ? styleRightEven : styleRightOdd;
    }

    private boolean isPair() {
        return getCurrentRow().getRowNum() % 2 == 0;
    }

    protected HSSFSheet initTitleRow(List<String> colTitles) {
        // create Title Row
        setCurrentRow(currentSheet.createRow(currentSheet.getPhysicalNumberOfRows()));

        IntStream.range(0, colTitles.size()).forEach(i -> {
            HSSFCell c = getCurrentRow().createCell((short)i);
            c.setCellValue(colTitles.get(i));
            c.setCellStyle(styleHeader);
        });
        return currentSheet;
    }

    public void formatSheet() {
        autoSizeColumns();
        currentSheet.setFitToPage(true);
        currentSheet.setAutobreaks(true);
        currentSheet.getPrintSetup().setFitWidth((short) 1);
        currentSheet.getPrintSetup().setFitHeight((short) 0);
    }

    private void autoSizeColumns() {
        for(int i = 0; i <= currentSheet.getRow(currentSheet.getFirstRowNum()).getLastCellNum(); i++) {
            currentSheet.autoSizeColumn((short)i);
        }
    }


}
