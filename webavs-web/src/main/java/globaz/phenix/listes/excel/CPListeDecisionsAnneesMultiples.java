/**
 * class CPImpressionAnneeEnDouble écrit le 19/01/05 par JPA
 * 
 * class créant un document .xls des décisions avec mise en compte
 * 
 * @author JPA
 */
package globaz.phenix.listes.excel;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.phenix.db.principale.CPDecisionAnneeMultiple;
import globaz.phenix.db.principale.CPDecisionAnneeMultipleManager;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/*
 * @param ForidPassage idPassage utilisé pour ajouter l'idpassage et le libelle dans le sujet de l'e-mail
 * 
 * @param langue langue de l'ut afin d'afficher les entêtes .xls dans la langue correspondante
 */
public class CPListeDecisionsAnneesMultiples {
    private String date_impression = "";
    private String idPassage;
    private BProcess processAppelant = null;
    private BSession session;
    private HSSFSheet sheet1;
    private String user = "";
    private HSSFWorkbook wb;

    public CPListeDecisionsAnneesMultiples() {
        wb = new HSSFWorkbook();
        sheet1 = wb.createSheet("");
    }

    // créé la feuille xls
    public CPListeDecisionsAnneesMultiples(BSession session, String idPassageJSP) {
        setIdPassage(idPassageJSP);
        this.session = session;
        wb = new HSSFWorkbook();
        setDate_impression(JACalendar.today().getDay() + "." + JACalendar.today().getMonth() + "."
                + JACalendar.today().getYear());
        setUser(getSession().getUserFullName());
    }

    public String getDate_impression() {
        return date_impression;
    }

    public String getIdPassage() {
        return idPassage;
    }

    /*
     * méthode pour gérer le fichier en sortie
     */
    public String getOutputFile() {
        try {
            // récup du viewBean pour avoir accès au idpassage et libellepassage
            // pour le sujet de l'e-mail
            String title = getSession().getLabel("LIST_MULTIPLES_JOB") + "-";
            if (getIdPassage().length() != 0) {
                title += getIdPassage();
            } else {
                title += getSession().getLabel("TOUS");
                title += "-";
            }
            File f = File.createTempFile(title, ".xls");
            f.deleteOnExit();
            FileOutputStream out = new FileOutputStream(f);
            wb.write(out);
            out.close();
            return f.getAbsolutePath();
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return "";
        }
    }

    public BProcess getProcessAppelant() {
        return processAppelant;
    }

    public BSession getSession() {
        return session;
    }

    public String getUser() {
        return user;
    }

    /*
     * initialisation de la feuille xls
     */
    // création de la liste décisions avec mise en compte
    public HSSFSheet populateSheet(CPDecisionAnneeMultipleManager manager, BTransaction transaction, String passage)
            throws Exception {
        setIdPassage(passage);
        BStatement statement = null;
        CPDecisionAnneeMultiple decisionsMec = null;
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount());
        /*
         * définition du style et mise en place du titre ,des entêtes, des bordures...
         */
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        sheet1 = wb.createSheet(getSession().getLabel("PAGE") + " 1");
        sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet1.getPrintSetup().setLandscape(true);
        sheet1 = setTitleRow(wb, sheet1);
        // définition de la taille des cell
        sheet1.setColumnWidth((short) 0, (short) 2600);
        sheet1.setColumnWidth((short) 1, (short) 8000);
        sheet1.setColumnWidth((short) 2, (short) 4500);
        sheet1.setColumnWidth((short) 3, (short) 2600);
        sheet1.setColumnWidth((short) 4, (short) 5000);
        sheet1.setColumnWidth((short) 5, (short) 6500);
        sheet1.setColumnWidth((short) 6, (short) 2200);
        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((decisionsMec = (CPDecisionAnneeMultiple) manager.cursorReadNext(statement)) != null)
                && (!decisionsMec.isNew())) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows());
            int colNum = 0;
            // numéro affilié
            HSSFCell cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getNumAffilie()));
            cell.setCellStyle(style);
            // nom
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getNom()));
            cell.setCellStyle(style);
            // numéro AVS
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getNumAvs()));
            cell.setCellStyle(style);
            // année
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getAnnee()));
            cell.setCellStyle(style);
            // type de décision
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getTypeDecision()));
            cell.setCellStyle(style);
            // période
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getDateDeb() + " - " + decisionsMec.getDateFin()));
            cell.setCellStyle(style);
            // idPassage
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getEbipas()));
            cell.setCellStyle(style);
            processAppelant.incProgressCounter();
        }
        HSSFFooter footer = sheet1.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0084CCP");
        return sheet1;
    }

    public void setDate_impression(String string) {
        date_impression = string;
    }

    public void setIdPassage(String string) {
        idPassage = string;
    }

    public void setProcessAppelant(BProcess processAppelant) {
        this.processAppelant = processAppelant;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    /*
     * méthode pour la création du style de la feuille pour la liste des décisions avec mise en compte entêtes, des
     * bordures...
     */
    private HSSFSheet setTitleRow(HSSFWorkbook wb, HSSFSheet sheet) {
        final String nom = session.getLabel("NOM_PRENOM");
        final String num = session.getLabel("NUM_AFFILIE");
        final String navs = session.getLabel("NUM_AVS");
        final String type = session.getLabel("TYPE_DECISION");
        final String année = session.getLabel("ANNEE");
        final String période = session.getLabel("PERIODE");
        final String ebipas = session.getLabel("PASSAGE");

        final String[] COL_TITLES = { num, nom, navs, année, type, période, ebipas };
        HSSFRow row = null;
        HSSFCell c;
        // création du style pour le titre de la page
        HSSFFont font2 = wb.createFont();
        font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font2.setFontHeight((short) 200);
        font2.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font2);
        row = sheet.createRow(0);
        c = row.createCell((short) 0);
        try {
            c.setCellValue(new HSSFRichTextString(session.getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase())));
        } catch (Exception e) {
            c.setCellValue(new HSSFRichTextString(""));
        }
        c.setCellStyle(style2);
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        c = row.createCell((short) 2);
        c.setCellValue(new HSSFRichTextString(session.getLabel("LIST_MULTIPLES")));
        c.setCellStyle(style2);
        // critères de sélection
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font3.setFontHeight((short) 200);
        style3.setFont(font3);
        row = sheet.createRow(3);
        row = sheet.createRow(4);
        // création de l'entête
        try {
            row = sheet.createRow(5);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(session.getLabel("DATE_IMPRESSION")));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getDate_impression()));
            c.setCellStyle(style3);
            row = sheet.createRow(6);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(session.getLabel("USER")));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getUser()));
            c.setCellStyle(style3);
            row = sheet.createRow(7);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(session.getLabel("PASSAGE")));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            if (getIdPassage().length() != 0) {
                c.setCellValue(new HSSFRichTextString(getIdPassage()));
            } else {
                c.setCellValue(new HSSFRichTextString(getSession().getLabel("TOUS")));
            }

            c.setCellStyle(style3);
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        // let's use a nifty font for the title
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        // ??? style.setFillBackgroundColor((short) 150);
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        // create Title Row
        row = sheet.createRow(8);
        row = sheet.createRow(9);
        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
            c.setCellStyle(style);
        }
        return sheet;
    }

    public void setUser(String string) {
        user = string;
    }

}