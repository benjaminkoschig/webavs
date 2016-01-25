/**
 * class CPImpressionCotisationDifferente écrit le 19/01/05 par JPA
 * 
 * class créant un document .xls des décisions avec mise en compte
 * 
 * @author JPA
 */
package globaz.phenix.listes.excel;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.principale.CPCotisationDifferente;
import globaz.phenix.db.principale.CPCotisationDifferenteManager;
import globaz.phenix.translation.CodeSystem;
import java.io.File;
import java.io.FileOutputStream;
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

/*
 * @param ForidPassage idPassage utilisé pour ajouter l'idpassage et le libelle dans le sujet de l'e-mail
 * 
 * @param langue langue de l'ut afin d'afficher les entêtes .xls dans la langue correspondante
 */
public class CPListeAcompteCotisationAnnuellleDifferente {
    private String dateImpression = "";
    private String idPassage;
    private BProcess processAppelant = null;
    private BSession session;
    private HSSFSheet sheet1;
    private String user = "";
    private HSSFWorkbook wb;

    public CPListeAcompteCotisationAnnuellleDifferente() {
        wb = new HSSFWorkbook();
        sheet1 = wb.createSheet("");

    }

    // créé la feuille xls
    public CPListeAcompteCotisationAnnuellleDifferente(BSession session) {
        this.session = session;
        wb = new HSSFWorkbook();
        setDateImpression(JACalendar.today().getDay() + "." + JACalendar.today().getMonth() + "."
                + JACalendar.today().getYear());
        setUser(getSession().getUserFullName());
    }

    public String getDateImpression() {
        return dateImpression;
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
            File f = File.createTempFile(
                    getSession().getLabel("LISTACOMPTEAFFILIATIONDIFFERENTE") + " - " + JACalendar.todayJJsMMsAAAA()
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
        HSSFCellStyle style1 = wb.createCellStyle();
        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style1.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style1.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        sheet1 = wb.createSheet(getSession().getLabel("PAGE") + " 1");
        // format
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
        sheet1.setColumnWidth((short) 6, (short) 3800);
        sheet1.setColumnWidth((short) 7, (short) 3500);
        sheet1.setColumnWidth((short) 8, (short) 3500);
        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((decisionsMec = (CPCotisationDifferente) manager.cursorReadNext(statement)) != null)
                && (!decisionsMec.isNew())) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            if (decisionsMec != null) {
                HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows());
                int colNum = 0;
                // numéro affilié
                HSSFCell cell = row.createCell((short) colNum++);
                cell.setCellValue(new HSSFRichTextString(decisionsMec.getNumAff()));
                cell.setCellStyle(style);
                // datedebutcp
                cell = row.createCell((short) colNum++);
                cell.setCellValue(new HSSFRichTextString(decisionsMec.getDateDebutDecision()));
                cell.setCellStyle(style);
                // datefincp
                cell = row.createCell((short) colNum++);
                cell.setCellValue(new HSSFRichTextString(decisionsMec.getDateFinDecision()));
                cell.setCellStyle(style);
                // genre
                cell = row.createCell((short) colNum++);
                cell.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(),
                        decisionsMec.getGenreDecision())));
                cell.setCellStyle(style2);
                // type d'assurance
                cell = row.createCell((short) colNum++);
                cell.setCellValue(new HSSFRichTextString(decisionsMec.getLibelleAssurance()));
                cell.setCellStyle(style2);
                // montantannuel cp
                cell = row.createCell((short) colNum++);
                cell.setCellValue(Double.parseDouble(decisionsMec.getMontantAnnuelCP()));
                cell.setCellStyle(style1);
                // montant annuel af
                cell = row.createCell((short) colNum++);
                cell.setCellValue(Double.parseDouble(decisionsMec.getMontantAnnuelAF()));
                cell.setCellStyle(style1);
                // Ecart
                float montantCp = 0;
                float montantAf = 0;
                float ecart = 0;
                if (!JadeStringUtil.isEmpty(decisionsMec.getMontantAnnuelCP())) {
                    montantCp = Float.parseFloat(JANumberFormatter.deQuote(decisionsMec.getMontantAnnuelCP()));
                }
                if (!JadeStringUtil.isEmpty(decisionsMec.getMontantAnnuelAF())) {
                    montantAf = Float.parseFloat(JANumberFormatter.deQuote(decisionsMec.getMontantAnnuelAF()));
                }
                ecart = new FWCurrency(montantCp - montantAf).floatValue();
                cell = row.createCell((short) colNum++);
                cell.setCellValue(Double.parseDouble(Float.toString(ecart)));
                cell.setCellStyle(style1);
            }
            processAppelant.incProgressCounter();
        }
        HSSFFooter footer = sheet1.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0083CCP");
        return sheet1;
    }

    public void setDateImpression(String string) {
        dateImpression = string;
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
        final String montAnnuelCP = session.getLabel("MONTANT_ANNUEL_CP");
        final String montAnnuelAF = session.getLabel("MONTANT_ANNUEL_AF");
        final String datedebCP = session.getLabel("DATE_DEBUTCP");
        final String datefinCP = session.getLabel("DATE_FINCP");
        final String typeAssurance = session.getLabel("TYPE_ASSURANCE");
        final String genreAffilie = session.getLabel("GENRE_AFFILIE");
        final String ecart = session.getLabel("ITEXT_DECISION_DIFFERENCE");
        final String[] COL_TITLES = { naff, datedebCP, datefinCP, genreAffilie, typeAssurance, montAnnuelCP,
                montAnnuelAF, ecart };
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
        c.setCellValue(new HSSFRichTextString(session.getLabel("LISTACOMPTECOTISATIONANNUELLEDIFFERENTE")));
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
            c.setCellValue(new HSSFRichTextString(getDateImpression()));
            c.setCellStyle(style3);
            row = sheet.createRow(6);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(session.getLabel("NUM_PASSAGE")));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getIdPassage()));
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