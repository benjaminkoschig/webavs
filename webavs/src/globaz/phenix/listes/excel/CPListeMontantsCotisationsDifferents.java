/**
 * class CPImpressionCotisationDifferente écrit le 19/01/05 par JPA
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
import globaz.globall.util.JADate;
import globaz.phenix.db.principale.CPCotisationDifferente;
import globaz.phenix.db.principale.CPCotisationDifferenteManager;
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
public class CPListeMontantsCotisationsDifferents {
    private String date_impression = "";
    private String idPassage;
    private BProcess processAppelant = null;
    private BSession session;
    private HSSFSheet sheet1;
    private String user = "";
    private HSSFWorkbook wb;

    public CPListeMontantsCotisationsDifferents() {
        wb = new HSSFWorkbook();
        sheet1 = wb.createSheet("");

    }

    // créé la feuille xls
    public CPListeMontantsCotisationsDifferents(BSession session) {
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
            // viewBean.execute();
            File f = File.createTempFile(getSession().getLabel("DIFFERENCEAFCP") + " - " + JACalendar.todayJJsMMsAAAA()
                    + "  ", ".xls");
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
    public HSSFSheet populateSheet(CPCotisationDifferenteManager manager, BTransaction transaction) throws Exception {
        BStatement statement = null;
        CPCotisationDifferente decisionsMec = null;
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount(transaction));
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
        // dfinition de la taille des cell
        sheet1.setColumnWidth((short) 0, (short) 2800);
        sheet1.setColumnWidth((short) 1, (short) 3800);
        sheet1.setColumnWidth((short) 2, (short) 3800);
        sheet1.setColumnWidth((short) 3, (short) 3800);
        sheet1.setColumnWidth((short) 4, (short) 3800);
        sheet1.setColumnWidth((short) 5, (short) 3800);
        sheet1.setColumnWidth((short) 6, (short) 3500);
        sheet1.setColumnWidth((short) 7, (short) 3500);
        sheet1.setColumnWidth((short) 8, (short) 3800);
        sheet1.setColumnWidth((short) 9, (short) 3500);
        sheet1.setColumnWidth((short) 10, (short) 3800);
        sheet1.setColumnWidth((short) 11, (short) 3800);
        sheet1.setColumnWidth((short) 12, (short) 4100);
        sheet1.setColumnWidth((short) 13, (short) 3200);
        sheet1.setColumnWidth((short) 14, (short) 3800);
        sheet1.setColumnWidth((short) 15, (short) 2800);
        sheet1.setColumnWidth((short) 16, (short) 2300);
        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((decisionsMec = (CPCotisationDifferente) manager.cursorReadNext(statement)) != null)
                && (!decisionsMec.isNew())) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows());
            int colNum = 0;
            // numéro affilié
            HSSFCell cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getNumAff()));
            cell.setCellStyle(style);
            // date debut af
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getDateDebutAffiliation()));
            cell.setCellStyle(style);
            // date fin af
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getDateFinAffiliation()));
            cell.setCellStyle(style);
            // datedebutcp
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getDateDebutDecision()));
            cell.setCellStyle(style);
            // datefincp
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getDateFinDecision()));
            cell.setCellStyle(style);
            // date décision
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getDateInformation()));
            cell.setCellStyle(style);
            // type d'assurance
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getLibelleAssurance()));
            cell.setCellStyle(style);
            // montantannuel cp
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getMontantAnnuelCP()));
            cell.setCellStyle(style);
            // montant annuel af
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getMontantAnnuelAF()));
            cell.setCellStyle(style);
            // montant semestriel cp
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getMontantSemestrielCP()));
            cell.setCellStyle(style);
            // montant semestriel af
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getMontantSemestrielAF()));
            cell.setCellStyle(style);
            // montant trimestriel cp
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getMontantTrimestrielCP()));
            cell.setCellStyle(style);
            // montant trimestriel af
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getMontantTrimestrielAF()));
            cell.setCellStyle(style);
            // montant mensuel cp
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getMontantMensuelCP()));
            cell.setCellStyle(style);
            // montant mensuel af
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getMontantMensuelAF()));
            cell.setCellStyle(style);

            // année affiliation
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getAnneeDecisionAF()));
            cell.setCellStyle(style);
            // num passage
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(decisionsMec.getNumPassage()));
            cell.setCellStyle(style);
            processAppelant.incProgressCounter();
        }
        HSSFFooter footer = sheet1.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0083CCP");
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
        final String naff = session.getLabel("NUM_AFFILIE");
        final String dateinf = session.getLabel("DATE_INFO");
        final String idPassage = session.getLabel("NUM_PASSAGE");
        final String datedeb = session.getLabel("DATE_DEBUT");
        final String datefin = session.getLabel("DATE_FIN");
        final String anneeAF = session.getLabel("ANNEE_AFF");
        final String montantCP = session.getLabel("MONTANTCP");
        final String montantAF = session.getLabel("MONTANTAF");
        final String montAnnuelCP = session.getLabel("MONTANT_ANNUEL_CP");
        final String montAnnuelAF = session.getLabel("MONTANT_ANNUEL_AF");
        final String montSemestrielCP = session.getLabel("MONTANT_SEMESTRIEL_CP");
        final String montSemestrielAF = session.getLabel("MONTANT_SEMESTRIEL_AF");
        final String montMensuelCP = session.getLabel("MONTANT_MENSUEL_CP");
        final String montMensuelAF = session.getLabel("MONTANT_MENSUEL_AF");
        final String datedebCP = session.getLabel("DATE_DEBUTCP");
        final String datefinCP = session.getLabel("DATE_FINCP");
        final String typeAssurance = session.getLabel("TYPE_ASSURANCE");
        JADate date = JACalendar.today();
        String Trimestre = "";
        int annee;
        int mois;
        annee = date.getYear();
        mois = date.getMonth();
        if ((mois >= 1) && (mois <= 3)) {
            Trimestre = ("31.03." + annee);
        }
        if ((mois >= 4) && (mois <= 6)) {
            Trimestre = ("30.06." + annee);
        }
        if ((mois >= 7) && (mois <= 9)) {
            Trimestre = ("30.09." + annee);
        }
        if ((mois >= 10) && (mois <= 12)) {
            Trimestre = ("31.12." + annee);
        }
        final String[] COL_TITLES = { naff, datedeb, datefin, datedebCP, datefinCP, dateinf, typeAssurance,
                montAnnuelCP, montAnnuelAF, montSemestrielCP, montSemestrielAF, montantCP, montantAF, montMensuelCP,
                montMensuelAF, anneeAF, idPassage };
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
        c.setCellValue(new HSSFRichTextString(session.getLabel("LISTCOTISATIONDIFFERENTE")));
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
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(session.getLabel("TRIMESTRE")));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(Trimestre));
            c.setCellStyle(style3);
            row = sheet.createRow(7);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(session.getLabel("USER")));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getUser()));
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