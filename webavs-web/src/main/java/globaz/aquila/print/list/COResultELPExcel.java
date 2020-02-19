package globaz.aquila.print.list;

import ch.globaz.utils.Pair;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import ch.globaz.vulpecula.external.api.poi.StyleFactory;
import globaz.aquila.print.list.elp.*;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import javax.xml.datatype.XMLGregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Crée le protocol retour pour l'importation eLP
 * 
 * @author EBKO
 */
public class COResultELPExcel extends AbstractListExcel {

    public static final String NUMERO_REFERENCE_INFOROM = "0333GCO";
    public static final String DATE_FORMAT = "MM.dd.yyyy";

    private static final Integer[] HEADER_RGB = {91, 155, 213};
    private static final Integer[] BACK_LIGNE_IMPAIRE_RGB = {221, 235, 247};
    private static final Integer[] BORD_LIGNE_RGB = {155, 194, 230};

    private COProtocoleELP protocole;
    private HSSFCellStyle styleHeader = null;
    private HSSFCellStyle styleRightOdd = null;
    private HSSFCellStyle styleRightEven = null;
    private HSSFCellStyle styleLeftOdd = null;
    private HSSFCellStyle styleLeftEven = null;

    // créé la feuille xls
    public COResultELPExcel(BSession session, COProtocoleELP protocole) throws COELPException {
        super(session, session.getLabel("ELP_TITLE_CONCAT"), session.getLabel("ELP_LIST_TITLE") + JACalendar.todayJJsMMsAAAA());
        this.protocole = protocole;
        initStyle();
        populateSheet();
    }

    @Override
    public void createContent() {

    }

    @Override
    public String getNumeroInforom() {
        return COResultELPExcel.NUMERO_REFERENCE_INFOROM;
    }

    /**
     * Création des onglets
     */
    private void initOnglet1() {
        // Titres des colonnes
        ArrayList<String> colTitles = new ArrayList<>();
        colTitles.add(getSession().getLabel("ELP_REFERENCE_FICHIER"));
        colTitles.add(getSession().getLabel("ELP_TYPE_MESSAGE"));
        colTitles.add(getSession().getLabel("ELP_SC_DATE_NOTIFICATION"));
        colTitles.add(getSession().getLabel("ELP_SC_DATE_RECEPTION"));
        colTitles.add(getSession().getLabel("ELP_SC_NO_POURSUITE"));
        colTitles.add(getSession().getLabel("ELP_SC_OPPOSITION"));
        colTitles.add(getSession().getLabel("ELP_REMARQUE"));
        colTitles.add(getSession().getLabel("ELP_MOTIF"));

        createSheet(getSession().getLabel("ELP_ONGLET_CDP_NON_TRAITE"));

        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(COResultELPExcel.NUMERO_REFERENCE_INFOROM);

    }

    private void initOnglet2() {
        // Titres des colonnes
        ArrayList<String> colTitles = new ArrayList<>();
        colTitles.add(getSession().getLabel("ELP_REFERENCE_FICHIER"));
        colTitles.add(getSession().getLabel("ELP_TYPE_MESSAGE"));
        colTitles.add(getSession().getLabel("ELP_SP_TYPE_SAISIE"));
        colTitles.add(getSession().getLabel("ELP_SP_DATE_EXECUTION"));
        colTitles.add(getSession().getLabel("ELP_SP_DATE_RECEPTION"));
        colTitles.add(getSession().getLabel("ELP_REMARQUE"));
        colTitles.add(getSession().getLabel("ELP_MOTIF"));

        createSheet(getSession().getLabel("ELP_ONGLET_PV_NON_TRAITE"));

        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(COResultELPExcel.NUMERO_REFERENCE_INFOROM);
    }

    private void initOnglet3() {
        // Titres des colonnes
        ArrayList<String> colTitles = new ArrayList<>();
        colTitles.add(getSession().getLabel("ELP_REFERENCE_FICHIER"));
        colTitles.add(getSession().getLabel("ELP_TYPE_MESSAGE"));
        colTitles.add(getSession().getLabel("ELP_RC_NO_ADB"));
        colTitles.add(getSession().getLabel("ELP_RC_DATE_ETABLISSEMENT"));
        colTitles.add(getSession().getLabel("ELP_RC_DATE_RECEPTION"));
        colTitles.add(getSession().getLabel("ELP_REMARQUE"));
        colTitles.add(getSession().getLabel("ELP_MOTIF"));

        createSheet(getSession().getLabel("ELP_ONGLET_ADB_NON_TRAITE"));

        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(COResultELPExcel.NUMERO_REFERENCE_INFOROM);
    }

    private void initOnglet4() {
        // Titres des colonnes
        ArrayList<String> colTitles = new ArrayList<>();
        colTitles.add(getSession().getLabel("ELP_REFERENCE_FICHIER"));
        colTitles.add(getSession().getLabel("ELP_TYPE_MESSAGE"));
        colTitles.add(getSession().getLabel("ELP_MOTIF"));

        createSheet(getSession().getLabel("ELP_ONGLET_MESSAGES_INCOHERENTS"));

        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(COResultELPExcel.NUMERO_REFERENCE_INFOROM);
    }

    private void initOnglet5() {
        // Titres des colonnes
        ArrayList<String> colTitles = new ArrayList<>();
        colTitles.add(getSession().getLabel("ELP_REFERENCE_FICHIER"));
        colTitles.add(getSession().getLabel("ELP_TYPE_MESSAGE"));
        colTitles.add(getSession().getLabel("ELP_REMARQUE"));

        createSheet(getSession().getLabel("ELP_ONGLET_MESSAGES_TRAITES"));

        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(COResultELPExcel.NUMERO_REFERENCE_INFOROM);
    }

    public void populateSheet() throws COELPException {
        populateSheet1();
        populateSheet2();
        populateSheet3();
        populateSheet4();
        populateSheet5();
    }

    /**
     * initialisation des feuilles xls
     */
    public HSSFSheet populateSheet1() {
        initOnglet1();
        for(COResultScELP result : protocole.getListCDPnonTraite()){
            createRow();
            this.createCell(result.getFichier(), getStyleLeft());
            this.createCell(result.getTypemessage().getValue(), getStyleLeft());
            this.createCell(getDate(result.getDateNotification()), getStyleRight());
            this.createCell(result.getDateReception(), getStyleRight());
            this.createCell(result.getNoPoursuite(), getStyleLeft());
            this.createCell(getDate(result.getOpposition()), getStyleRight());
            this.createCell(result.getRemarque(), getStyleLeft());
            this.createCell(result.getMotif(getSession()), getStyleLeft());
        }
        formatSheet();
        return currentSheet;
    }

    public HSSFSheet populateSheet2() throws COELPException {
        initOnglet2();
        for(COResultSpELP result : protocole.getListPVnonTraite()){
            createRow();
            this.createCell(result.getFichier(), getStyleLeft());
            this.createCell(result.getTypemessage().getValue(), getStyleLeft());
            this.createCell(result.getTypeDeSaisie(getSession()), getStyleLeft());
            this.createCell(getDate(result.getDateExecution()), getStyleRight());
            this.createCell(result.getDateReception(), getStyleRight());
            this.createCell(result.getRemarque(), getStyleLeft());
            this.createCell(result.getMotif(getSession()), getStyleLeft());
        }
        formatSheet();
        return currentSheet;
    }

    public HSSFSheet populateSheet3() {
        initOnglet3();
        for(COResultRcELP result : protocole.getListADBnonTraite()){
            createRow();
            this.createCell(result.getFichier(), getStyleLeft());
            this.createCell(result.getTypemessage().getValue(), getStyleLeft());
            this.createCell(result.getNoAbd(), getStyleRight());
            this.createCell(getDate(result.getDateEtablissement()), getStyleRight());
            this.createCell(result.getDateReception(), getStyleRight());
            this.createCell(result.getRemarque(), getStyleLeft());
            this.createCell(result.getMotif(getSession()), getStyleLeft());
        }
        formatSheet();
        return currentSheet;
    }

    public HSSFSheet populateSheet4() {
        initOnglet4();
        for(COAbstractResultELP result : protocole.getListMsgIncoherent()){
            createRow();
            this.createCell(result.getFichier(), getStyleLeft());
            this.createCell(result.getTypemessage().getValue(), getStyleLeft());
            this.createCell(result.getMotif(getSession()), getStyleLeft());
        }
        formatSheet();
        return currentSheet;
    }

    public HSSFSheet populateSheet5() {
        initOnglet5();
        for(COAbstractResultELP result : protocole.getListMsgTraite()){
            createRow();
            this.createCell(result.getFichier(), getStyleLeft());
            this.createCell(result.getTypemessage().getValue(), getStyleLeft());
            this.createCell(result.getRemarque(), getStyleLeft());
        }
        formatSheet();
        return currentSheet;
    }

    private void setColumnWidth(HSSFSheet sheet, Integer... values){
        int numCol=0;
        for(int value: values){
            currentSheet.setColumnWidth((short) numCol++,(short) value);
        }
    }

    private void initStyle() {
        setColor(getWorkbook(), HSSFColor.ROYAL_BLUE.index, HEADER_RGB);
        setColor(getWorkbook(), HSSFColor.SKY_BLUE.index, BACK_LIGNE_IMPAIRE_RGB);
        setColor(getWorkbook(), HSSFColor.PALE_BLUE.index, BORD_LIGNE_RGB);

        StyleFactory styleFactory = new StyleFactory(getWorkbook());
        HSSFFont fontHeader = getWorkbook().createFont();
        fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        fontHeader.setColor(HSSFColor.WHITE.index);
        styleHeader = getWorkbook().createCellStyle();
        styleHeader.setFont(fontHeader);
        styleHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleHeader.setBorderBottom(HSSFCellStyle.BORDER_NONE);
        styleHeader.setBorderLeft(HSSFCellStyle.BORDER_NONE);
        styleHeader.setBorderRight(HSSFCellStyle.BORDER_NONE);
        styleHeader.setBorderTop(HSSFCellStyle.BORDER_NONE); // BORDER_THIN);
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

    HSSFCellStyle getStyleLeft() {
        return isPair() ? styleLeftEven : styleLeftOdd;
    }

    HSSFCellStyle getStyleRight() {
        return isPair() ? styleRightEven : styleRightOdd;
    }

    private boolean isPair() {
        return getCurrentRow().getRowNum() % 2 == 0;
    }

    private String getDate(XMLGregorianCalendar calendar) {
        return calendar == null ? "" : new SimpleDateFormat(DATE_FORMAT).format(calendar.toGregorianCalendar().getTime());
    }

    protected HSSFSheet initTitleRow(List<String> col_titles) {
        // create Title Row
        setCurrentRow(currentSheet.createRow(currentSheet.getPhysicalNumberOfRows()));

        IntStream.range(0, col_titles.size()).forEach(i -> {
            HSSFCell c = getCurrentRow().createCell((short)i);
            c.setCellValue(col_titles.get(i));
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

    public HSSFColor setColor(HSSFWorkbook workbook, int index, Integer[] colors){
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

}
