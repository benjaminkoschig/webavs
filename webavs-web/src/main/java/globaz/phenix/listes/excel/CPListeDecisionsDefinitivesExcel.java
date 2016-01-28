package globaz.phenix.listes.excel;

import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JATime;
import globaz.jade.common.Jade;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * @author: sco
 */
public class CPListeDecisionsDefinitivesExcel {

    public static final String ACTIF = "ACTIF";
    public static final String ANNEE = "ANNEE";
    public static final String COTISATION = "COTISATION";
    public static final String EN_TETE_AFFILIES = "EN_TETE_AFFILIES";
    public static final String EN_TETE_ANNEE = "EN_TETE_ANNEE";
    public static final String EN_TETE_GENRE_DECISION = "EN_TETE_GENRE_DECISION";
    public static final String EN_TETE_PERIODE = "EN_TETE_PERIODE";
    public static final String EN_TETE_TYPE_DECISION = "EN_TETE_TYPE_DECISION";
    public static final String EN_TETE_UNIQUEMENT_ACTIVE = "EN_TETE_UNIQUEMENT_ACTIVE";
    public static final String GENRE = "GENRE";
    public static final String NOM = "NOM";

    public static final String NUM_INFOROM = "0270CCP";
    public static final String NUMERO = "NUMERO";
    public static final String PERIODE = "PERIODE";
    public static final String REVENU = "REVENU";
    public static final String REVENU_CI = "REVENU_CI";

    public static final String TYPE_DECISION = "TYPE_DECISION";

    private Collection<Map<String, String>> listeDecision;
    private Map<String, String> mapEntete;
    private BSession session = null;
    private HSSFSheet sheet;
    private HSSFWorkbook wb;

    /**
     * Commentaire relatif au constructeur TentDocument.
     */
    public CPListeDecisionsDefinitivesExcel() {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet("TENT");
        sheet.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet.getPrintSetup().setLandscape(true);
        // marges en-tête/pied de page
        sheet.getPrintSetup().setHeaderMargin(0);
        sheet.getPrintSetup().setFooterMargin(0);
    }

    /**
     * Commentaire relatif au constructeur TentDocument.
     */
    public CPListeDecisionsDefinitivesExcel(BSession session) {
        this.session = session;
        wb = new HSSFWorkbook();
        sheet = wb.createSheet(getSession().getLabel("PAGE") + " 1");
        sheet.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet.getPrintSetup().setLandscape(true);
        // marges en-tête/pied de page
        sheet.getPrintSetup().setHeaderMargin(0);
        sheet.getPrintSetup().setFooterMargin(0);
        // this.sheet = this.setTitleRow(this.wb, this.sheet);
    }

    public void createDocExcel() throws Exception {

        if (mapEntete == null) {
            throw new Exception("Unabled to create doc excel, information for entete is null or empty");
        }

        if (listeDecision == null) {
            throw new Exception("Unabled to create doc excel, information for  is null or empty");
        }

        // Création de l'entete
        createTitleRow();

        // Rempli le document
        populateSheet();
    }

    /**
     * @param wb
     * @param sheet
     * @param row
     * @return
     */
    private void createTitleRow() {
        final String nom = getSession().getLabel("NOM_PRENOM");
        final String num = getSession().getLabel("NUM_AFFILIE");
        final String annee = getSession().getLabel("ANNEE");
        final String periodeDecision = getSession().getLabel("PERIODE");
        final String typeDecision = getSession().getLabel("TYPE_DECISION");
        final String genre = getSession().getLabel("GENRE_AFFILIE");
        final String fortuneRevenuDet = getSession().getLabel("FORTUNE_DET");
        final String revenuCi = getSession().getLabel("REVENU_CI");
        final String cotiAnnuelle = getSession().getLabel("COTISATION_ANNUELLE");

        final String[] COL_TITLES = { num, nom, annee, periodeDecision, typeDecision, genre, fortuneRevenuDet,
                revenuCi, cotiAnnuelle };

        HSSFRow row = null;
        HSSFCell c;
        // Déclaration des styles pour les cellules
        HSSFCellStyle style1 = wb.createCellStyle();
        HSSFFont font1 = wb.createFont();
        font1.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font1.setFontHeight((short) 200);
        style1.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style1.setFont(font1);

        HSSFFont font2 = wb.createFont();
        font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font2.setFontHeight((short) 200);
        font2.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font2);

        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font3.setFontHeight((short) 200);
        style3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style3.setFont(font3);

        HSSFCellStyle style2Inac = wb.createCellStyle();
        style2Inac.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2Inac.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2Inac.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2Inac.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2Inac.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

        // Nom de la caisse
        row = sheet.createRow(0);
        c = row.createCell((short) 0);
        try {
            c.setCellValue(new HSSFRichTextString(session.getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase())));
        } catch (Exception e) {
            c.setCellValue(new HSSFRichTextString(""));
        }
        c.setCellStyle(style2);
        c = row.createCell((short) 8);
        c.setCellStyle(style1);
        c.setCellValue(new HSSFRichTextString(JACalendar.todayJJsMMsAAAA() + " - "
                + new JATime(JACalendar.now()).toStr(":")));
        row = sheet.createRow(1);

        // Nom du document
        row = sheet.createRow(2);
        c = row.createCell((short) 2);
        c.setCellStyle(style2);
        c.setCellValue(new HSSFRichTextString(session.getLabel("TITRE_LISTE_DECISION_DEFINITIVE")));

        row = sheet.createRow(3);

        // Pédiode
        row = sheet.createRow(4);
        c = row.createCell((short) 1);
        c.setCellStyle(style2);
        c.setCellValue(new HSSFRichTextString(session.getLabel("PERIODE_FACTURATION")));
        c = row.createCell((short) 2);
        c.setCellStyle(style3);
        c.setCellValue(new HSSFRichTextString(getMapEntete().get(CPListeDecisionsDefinitivesExcel.EN_TETE_PERIODE)));

        // Liste des affilies
        row = sheet.createRow(5);
        c = row.createCell((short) 1);
        c.setCellStyle(style2);
        c.setCellValue(new HSSFRichTextString(session.getLabel("NUM_AFFILIE")));
        c = row.createCell((short) 2);
        c.setCellStyle(style3);
        c.setCellValue(new HSSFRichTextString(getMapEntete().get(CPListeDecisionsDefinitivesExcel.EN_TETE_AFFILIES)));

        // Type de décision
        row = sheet.createRow(6);
        c = row.createCell((short) 1);
        c.setCellStyle(style2);
        c.setCellValue(new HSSFRichTextString(session.getLabel("TYPE_DECISION")));
        c = row.createCell((short) 2);
        c.setCellStyle(style3);
        c.setCellValue(new HSSFRichTextString(getMapEntete()
                .get(CPListeDecisionsDefinitivesExcel.EN_TETE_TYPE_DECISION)));

        // Genre de décision
        row = sheet.createRow(7);
        c = row.createCell((short) 1);
        c.setCellStyle(style2);
        c.setCellValue(new HSSFRichTextString(getSession().getLabel("GENRE_DECISION")));
        c = row.createCell((short) 2);
        c.setCellStyle(style3);
        c.setCellValue(new HSSFRichTextString(getMapEntete().get(
                CPListeDecisionsDefinitivesExcel.EN_TETE_GENRE_DECISION)));

        // Actif ou non
        row = sheet.createRow(8);
        c = row.createCell((short) 1);
        c.setCellStyle(style2);
        c.setCellValue(new HSSFRichTextString(getSession().getLabel("UNIQUEMENT_DECISIONS_ACTIVES")));
        c = row.createCell((short) 2);
        c.setCellStyle(style3);
        c.setCellValue(new HSSFRichTextString(getMapEntete().get(
                CPListeDecisionsDefinitivesExcel.EN_TETE_UNIQUEMENT_ACTIVE)));

        // Annee
        row = sheet.createRow(9);
        c = row.createCell((short) 1);
        c.setCellStyle(style2);
        c.setCellValue(new HSSFRichTextString(getSession().getLabel("ANNEE")));
        c = row.createCell((short) 2);
        c.setCellStyle(style3);
        c.setCellValue(new HSSFRichTextString(getMapEntete().get(CPListeDecisionsDefinitivesExcel.EN_TETE_ANNEE)));

        row = sheet.createRow(10);
        row = sheet.createRow(11);

        // Entete du tableau
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        // create Title Row
        row = sheet.createRow(11);
        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
            c.setCellStyle(style);
        }
    }

    public Collection<Map<String, String>> getListeDecision() {
        return listeDecision;
    }

    public Map<String, String> getMapEntete() {
        return mapEntete;
    }

    public String getOutputFile(String nomDoc) throws Exception {

        String filePath = Jade.getInstance().getPersistenceDir() + nomDoc + ".xls";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            wb.write(fos);
            fos.flush();
        } catch (Exception e) {
            throw new Exception("Technical Exception, unable to save the document at the emplacement : " + filePath, e);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        return filePath;
    }

    /**
     * Returns the session.
     * 
     * @return BSession
     */
    public BSession getSession() {
        return session;
    }

    private void populateSheet() throws Exception {

        HSSFFont fontInactive = wb.createFont();
        fontInactive.setColor(HSSFColor.GREY_50_PERCENT.index);

        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        HSSFCellStyle styleInac = wb.createCellStyle();
        styleInac.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleInac.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleInac.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleInac.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleInac.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleInac.setFont(fontInactive);

        HSSFCellStyle style1 = wb.createCellStyle();
        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style1.setAlignment(HSSFCellStyle.ALIGN_LEFT);

        HSSFCellStyle style1Inac = wb.createCellStyle();
        style1Inac.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style1Inac.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style1Inac.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style1Inac.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style1Inac.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style1Inac.setFont(fontInactive);

        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style2.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));

        HSSFCellStyle style2Inac = wb.createCellStyle();
        style2Inac.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2Inac.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2Inac.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2Inac.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2Inac.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style2Inac.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
        style2Inac.setFont(fontInactive);

        sheet.setColumnWidth((short) 0, (short) 4200);
        sheet.setColumnWidth((short) 1, (short) 8000);
        sheet.setColumnWidth((short) 2, (short) 2000);
        sheet.setColumnWidth((short) 3, (short) 7000);
        sheet.setColumnWidth((short) 4, (short) 4000);
        sheet.setColumnWidth((short) 5, (short) 1800);
        sheet.setColumnWidth((short) 6, (short) 3800);
        sheet.setColumnWidth((short) 7, (short) 3400);
        sheet.setColumnWidth((short) 8, (short) 3400);
        sheet.setColumnWidth((short) 9, (short) 3400);
        sheet.setColumnWidth((short) 10, (short) 4000);

        Object[] obj_listeDecision = listeDecision.toArray();

        for (Object obj_decision : obj_listeDecision) {

            Map<String, String> mapDecision = (Map<String, String>) obj_decision;

            HSSFRow row = sheet.createRow(sheet.getPhysicalNumberOfRows());
            int colNum = 0;

            // Si la decision est active
            if (Boolean.parseBoolean(mapDecision.get(CPListeDecisionsDefinitivesExcel.ACTIF))) {

                // numéro affilié
                HSSFCell cell = row.createCell((short) colNum++);
                cell.setCellStyle(style);
                cell.setCellValue(new HSSFRichTextString(mapDecision.get(CPListeDecisionsDefinitivesExcel.NUMERO)));

                // Nom Prenom
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style1);
                cell.setCellValue(new HSSFRichTextString(mapDecision.get(CPListeDecisionsDefinitivesExcel.NOM)));

                // Année
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style);
                cell.setCellValue(new HSSFRichTextString(mapDecision.get(CPListeDecisionsDefinitivesExcel.ANNEE)));

                // Période
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style1);
                cell.setCellValue(new HSSFRichTextString(mapDecision.get(CPListeDecisionsDefinitivesExcel.PERIODE)));

                // Type de décision
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style1);
                cell.setCellValue(new HSSFRichTextString(mapDecision
                        .get(CPListeDecisionsDefinitivesExcel.TYPE_DECISION)));

                // Genre
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style1);
                cell.setCellValue(new HSSFRichTextString(mapDecision.get(CPListeDecisionsDefinitivesExcel.GENRE)));

                // Fortune / revenu
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style2);
                cell.setCellValue(new HSSFRichTextString(mapDecision.get(CPListeDecisionsDefinitivesExcel.REVENU)));

                // Revenu CI
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style2);
                cell.setCellValue(new HSSFRichTextString(mapDecision.get(CPListeDecisionsDefinitivesExcel.REVENU_CI)));

                // Cotisation
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style2);
                cell.setCellValue(new HSSFRichTextString(mapDecision.get(CPListeDecisionsDefinitivesExcel.COTISATION)));

            } else {

                // numéro affilié
                HSSFCell cell = row.createCell((short) colNum++);
                cell.setCellStyle(styleInac);
                cell.setCellValue(new HSSFRichTextString(mapDecision.get(CPListeDecisionsDefinitivesExcel.NUMERO)));

                // Nom Prenom
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style1Inac);
                cell.setCellValue(new HSSFRichTextString(mapDecision.get(CPListeDecisionsDefinitivesExcel.NOM)));

                // Année
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(styleInac);
                cell.setCellValue(new HSSFRichTextString(mapDecision.get(CPListeDecisionsDefinitivesExcel.ANNEE)));

                // Période
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style1Inac);
                cell.setCellValue(new HSSFRichTextString(mapDecision.get(CPListeDecisionsDefinitivesExcel.PERIODE)));

                // Type de décision
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style1Inac);
                cell.setCellValue(new HSSFRichTextString(mapDecision
                        .get(CPListeDecisionsDefinitivesExcel.TYPE_DECISION)));

                // Genre
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style1Inac);
                cell.setCellValue(new HSSFRichTextString(mapDecision.get(CPListeDecisionsDefinitivesExcel.GENRE)));

                // Fortune / revenu
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style2Inac);
                cell.setCellValue(new HSSFRichTextString(mapDecision.get(CPListeDecisionsDefinitivesExcel.REVENU)));

                // Revenu CI
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style2Inac);
                cell.setCellValue(new HSSFRichTextString(mapDecision.get(CPListeDecisionsDefinitivesExcel.REVENU_CI)));

                // Cotisation
                cell = row.createCell((short) colNum++);
                cell.setCellStyle(style2Inac);
                cell.setCellValue(new HSSFRichTextString(mapDecision.get(CPListeDecisionsDefinitivesExcel.COTISATION)));
            }
        }

        HSSFFooter footer = sheet.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: "
                + CPListeDecisionsDefinitivesExcel.NUM_INFOROM);
    }

    public void setListeDecision(Collection<Map<String, String>> listeDecision) {
        this.listeDecision = listeDecision;
    }

    public void setMapEntete(Map<String, String> mapEntete) {
        this.mapEntete = mapEntete;
    }

    /**
     * Sets the session.
     * 
     * @param session
     *            The session to set
     */
    public void setSession(BSession session) {
        this.session = session;
    }
}
