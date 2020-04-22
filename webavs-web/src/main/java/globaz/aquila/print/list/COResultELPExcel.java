package globaz.aquila.print.list;

import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import globaz.aquila.print.list.elp.*;
import globaz.aquila.process.elp.*;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

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

    private static final Integer[] HEADER_RGB = {91, 155, 213};
    private static final Integer[] BACK_LIGNE_IMPAIRE_RGB = {221, 235, 247};
    private static final Integer[] BORD_LIGNE_RGB = {155, 194, 230};
    public static final String ELP_REFERENCE_FICHIER = "ELP_REFERENCE_FICHIER";
    public static final String ELP_TYPE_MESSAGE = "ELP_TYPE_MESSAGE";
    public static final String ELP_SC_DATE_NOTIFICATION = "ELP_SC_DATE_NOTIFICATION";
    public static final String ELP_SC_DATE_RECEPTION = "ELP_SC_DATE_RECEPTION";
    public static final String ELP_SC_NO_POURSUITE = "ELP_SC_NO_POURSUITE";
    public static final String ELP_SC_OPPOSITION = "ELP_SC_OPPOSITION";
    public static final String ELP_REMARQUE = "ELP_REMARQUE";
    public static final String ELP_MOTIF = "ELP_MOTIF";
    public static final String ELP_ONGLET_CDP_NON_TRAITE = "ELP_ONGLET_CDP_NON_TRAITE";
    public static final String ELP_SP_TYPE_SAISIE = "ELP_SP_TYPE_SAISIE";
    public static final String ELP_SP_DATE_EXECUTION = "ELP_SP_DATE_EXECUTION";
    public static final String ELP_SP_DATE_RECEPTION = "ELP_SP_DATE_RECEPTION";
    public static final String ELP_ONGLET_PV_NON_TRAITE = "ELP_ONGLET_PV_NON_TRAITE";
    public static final String ELP_RC_NO_ADB = "ELP_RC_NO_ADB";
    public static final String ELP_RC_DATE_ETABLISSEMENT = "ELP_RC_DATE_ETABLISSEMENT";
    public static final String ELP_RC_DATE_RECEPTION = "ELP_RC_DATE_RECEPTION";
    public static final String ELP_ONGLET_ADB_NON_TRAITE = "ELP_ONGLET_ADB_NON_TRAITE";
    public static final String ELP_ONGLET_MESSAGES_INCOHERENTS = "ELP_ONGLET_MESSAGES_INCOHERENTS";
    public static final String ELP_ONGLET_MESSAGES_TRAITES = "ELP_ONGLET_MESSAGES_TRAITES";

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
        // fait dans populateSheet avec un COELPException
    }

    @Override
    public String getNumeroInforom() {
        return COResultELPExcel.NUMERO_REFERENCE_INFOROM;
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

    private void populateSheet() throws COELPException {
        populateSheet1();
        populateSheet2();
        populateSheet3();
        populateSheet4();
        populateSheet5();
    }

    /**
     * initialisation des feuilles xls
     */
    private HSSFSheet populateSheet1() {
        initOnglet1();
        for(COScElpDto result : protocole.getListCDPnonTraite()){
            createRow();
            this.createCell(result.getFichier(), getStyleLeft());
            this.createCell(result.getTypemessage().getValue(), getStyleLeft());
            this.createCell(result.getDateNotification(), getStyleRight());
            this.createCell(result.getDateReception(), getStyleRight());
            this.createCell(result.getNoPoursuite(), getStyleLeft());
            this.createCell(result.getOpposition(), getStyleRight());
            this.createCell(result.getRemarque(), getStyleLeft());
            this.createCell(result.getMotif(getSession()), getStyleLeft());
        }
        formatSheet();
        return currentSheet;
    }

    /**
     * Création des onglets
     */
    private void initOnglet1() {
        // Titres des colonnes
        ArrayList<String> colTitles = new ArrayList<>();
        colTitles.add(getSession().getLabel(ELP_REFERENCE_FICHIER));
        colTitles.add(getSession().getLabel(ELP_TYPE_MESSAGE));
        colTitles.add(getSession().getLabel(ELP_SC_DATE_NOTIFICATION));
        colTitles.add(getSession().getLabel(ELP_SC_DATE_RECEPTION));
        colTitles.add(getSession().getLabel(ELP_SC_NO_POURSUITE));
        colTitles.add(getSession().getLabel(ELP_SC_OPPOSITION));
        colTitles.add(getSession().getLabel(ELP_REMARQUE));
        colTitles.add(getSession().getLabel(ELP_MOTIF));

        createSheet(getSession().getLabel(ELP_ONGLET_CDP_NON_TRAITE));

        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(COResultELPExcel.NUMERO_REFERENCE_INFOROM);

    }

    private HSSFSheet populateSheet2() throws COELPException {
        initOnglet2();
        for(COSpElpDto result : protocole.getListPVnonTraite()){
            createRow();
            this.createCell(result.getFichier(), getStyleLeft());
            this.createCell(result.getTypemessage().getValue(), getStyleLeft());
            this.createCell(result.getTypeSaisieLabel(), getStyleLeft());
            this.createCell(result.getDateExecution(), getStyleRight());
            this.createCell(result.getDateReception(), getStyleRight());
            this.createCell(result.getRemarque(), getStyleLeft());
            this.createCell(result.getMotif(getSession()), getStyleLeft());
        }
        formatSheet();
        return currentSheet;
    }

    private void initOnglet2() {
        // Titres des colonnes
        ArrayList<String> colTitles = new ArrayList<>();
        colTitles.add(getSession().getLabel(ELP_REFERENCE_FICHIER));
        colTitles.add(getSession().getLabel(ELP_TYPE_MESSAGE));
        colTitles.add(getSession().getLabel(ELP_SP_TYPE_SAISIE));
        colTitles.add(getSession().getLabel(ELP_SP_DATE_EXECUTION));
        colTitles.add(getSession().getLabel(ELP_SP_DATE_RECEPTION));
        colTitles.add(getSession().getLabel(ELP_REMARQUE));
        colTitles.add(getSession().getLabel(ELP_MOTIF));

        createSheet(getSession().getLabel(ELP_ONGLET_PV_NON_TRAITE));

        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(COResultELPExcel.NUMERO_REFERENCE_INFOROM);
    }

    private HSSFSheet populateSheet3() {
        initOnglet3();
        for(CORcElpDto result : protocole.getListADBnonTraite()){
            createRow();
            this.createCell(result.getFichier(), getStyleLeft());
            this.createCell(result.getTypemessage().getValue(), getStyleLeft());
            this.createCell(result.getNoAbd(), getStyleRight());
            this.createCell(result.getDateEtablissement(), getStyleRight());
            this.createCell(result.getDateReception(), getStyleRight());
            this.createCell(result.getRemarque(), getStyleLeft());
            this.createCell(result.getMotif(getSession()), getStyleLeft());
        }
        formatSheet();
        return currentSheet;
    }

    private void initOnglet3() {
        // Titres des colonnes
        ArrayList<String> colTitles = new ArrayList<>();
        colTitles.add(getSession().getLabel(ELP_REFERENCE_FICHIER));
        colTitles.add(getSession().getLabel(ELP_TYPE_MESSAGE));
        colTitles.add(getSession().getLabel(ELP_RC_NO_ADB));
        colTitles.add(getSession().getLabel(ELP_RC_DATE_ETABLISSEMENT));
        colTitles.add(getSession().getLabel(ELP_RC_DATE_RECEPTION));
        colTitles.add(getSession().getLabel(ELP_REMARQUE));
        colTitles.add(getSession().getLabel(ELP_MOTIF));

        createSheet(getSession().getLabel(ELP_ONGLET_ADB_NON_TRAITE));

        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(COResultELPExcel.NUMERO_REFERENCE_INFOROM);
    }

    private HSSFSheet populateSheet4() {
        initOnglet4();
        for(COAbstractELP result : protocole.getListMsgIncoherent()){
            createRow();
            this.createCell(result.getFichier(), getStyleLeft());
            this.createCell(result.getTypemessage().getValue(), getStyleLeft());
            this.createCell(result.getMotif(getSession()), getStyleLeft());
        }
        formatSheet();
        return currentSheet;
    }

    private void initOnglet4() {
        // Titres des colonnes
        ArrayList<String> colTitles = new ArrayList<>();
        colTitles.add(getSession().getLabel(ELP_REFERENCE_FICHIER));
        colTitles.add(getSession().getLabel(ELP_TYPE_MESSAGE));
        colTitles.add(getSession().getLabel(ELP_MOTIF));

        createSheet(getSession().getLabel(ELP_ONGLET_MESSAGES_INCOHERENTS));

        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(COResultELPExcel.NUMERO_REFERENCE_INFOROM);
    }

    private HSSFSheet populateSheet5() {
        initOnglet5();
        for(COAbstractELP result : protocole.getListMsgTraite()){
            createRow();
            this.createCell(result.getFichier(), getStyleLeft());
            this.createCell(result.getTypemessage().getValue(), getStyleLeft());
            this.createCell(result.getRemarque(), getStyleLeft());
        }
        formatSheet();
        return currentSheet;
    }

    private void initOnglet5() {
        // Titres des colonnes
        ArrayList<String> colTitles = new ArrayList<>();
        colTitles.add(getSession().getLabel(ELP_REFERENCE_FICHIER));
        colTitles.add(getSession().getLabel(ELP_TYPE_MESSAGE));
        colTitles.add(getSession().getLabel(ELP_REMARQUE));

        createSheet(getSession().getLabel(ELP_ONGLET_MESSAGES_TRAITES));

        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(COResultELPExcel.NUMERO_REFERENCE_INFOROM);
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
