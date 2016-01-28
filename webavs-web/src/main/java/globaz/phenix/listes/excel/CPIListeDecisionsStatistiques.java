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
import globaz.musca.db.facturation.FAPassage;
import globaz.phenix.db.principale.CPDecisionAffiliation;
import globaz.phenix.db.principale.CPDecisionAffiliationTiersManager;
import globaz.phenix.db.principale.CPEtatDecisionTiersManagerForList;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
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
public class CPIListeDecisionsStatistiques {
    private String idPassage;
    private FAPassage myPassage;
    private BProcess processAppelant = null;
    private BSession session;
    private HSSFSheet sheet1;
    private HSSFWorkbook wb;

    public CPIListeDecisionsStatistiques() {
        wb = new HSSFWorkbook();
        sheet1 = wb.createSheet("");

    }

    // créé la feuille xls
    public CPIListeDecisionsStatistiques(BSession session, String id) {
        this.session = session;
        wb = new HSSFWorkbook();
        setIdPassage(id);
        try {
            myPassage = new FAPassage();
            myPassage.setSession(getSession());
            myPassage.setIdPassage(id);
            myPassage.retrieve();
            setMyPassage(myPassage);
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
    }

    public String getIdPassage() {
        return idPassage;
    }

    public FAPassage getMyPassage() {
        return myPassage;
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
                    getSession().getLabel("LISTDECISIONSTAT") + " - " + JACalendar.todayJJsMMsAAAA() + "  ", ".xls");
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

    /*
     * initialisation de la feuille xls
     */
    // création de la liste décisions avec mise en compte
    public HSSFSheet populateSheet(CPEtatDecisionTiersManagerForList manager, BTransaction transaction)
            throws Exception {
        BStatement statement = null;
        CPDecisionAffiliation decisionsMec = null;
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
        // format
        sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet1.getPrintSetup().setLandscape(true);
        sheet1 = setTitleRowMiseCompte(wb, sheet1, manager);
        // dfinition de la taille des cell
        sheet1.setDefaultColumnWidth((short) 30);
        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((decisionsMec = (CPDecisionAffiliation) manager.cursorReadNext(statement)) != null)
                && (!decisionsMec.isNew())) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            if (decisionsMec != null) {
                HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows());
                int colNum = 0;
                // collaborateur
                HSSFCell cell = row.createCell((short) colNum++);
                cell.setCellValue(new HSSFRichTextString(decisionsMec.getCollaborateur()));
                cell.setCellStyle(style);
                // type décision
                cell = row.createCell((short) colNum++);
                cell.setCellValue(new HSSFRichTextString(globaz.phenix.translation.CodeSystem.getLibelle(getSession(),
                        decisionsMec.getTypeDecision())));
                cell.setCellStyle(style);
                // total
                cell = row.createCell((short) colNum++);
                cell.setCellValue(new HSSFRichTextString(decisionsMec.getTotal()));
                cell.setCellStyle(style);
            }
            processAppelant.incProgressCounter();
        }
        return sheet1;
    }

    public void setIdPassage(String string) {
        idPassage = string;
    }

    public void setMyPassage(FAPassage passage) {
        myPassage = passage;
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
    private HSSFSheet setTitleRowMiseCompte(HSSFWorkbook wb, HSSFSheet sheet, CPDecisionAffiliationTiersManager manager) {
        final String collaborateur = getSession().getLabel("COLLABORATEUR");
        final String typeDecision = getSession().getLabel("TYPE_DECISION");
        final String total = getSession().getLabel("TOTAL");

        final String[] COL_TITLES = { collaborateur, typeDecision, total };
        HSSFRow row = null;
        HSSFCell c;
        // création du style pour le titre de la page
        HSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font.setFontHeight((short) 500);
        font.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle style = wb.createCellStyle();

        HSSFCellStyle style2 = wb.createCellStyle();
        HSSFFont font2 = wb.createFont();
        font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font2.setFontHeight((short) 200);
        style2.setFont(font2);
        style.setFont(font);

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
        c = row.createCell((short) 1);
        c.setCellValue(new HSSFRichTextString(getSession().getLabel("LISTDECISIONSTAT")));
        c.setCellStyle(style2);
        row = sheet.createRow(3);
        row = sheet.createRow(4);
        // création de l'entête
        try {
            row = sheet.createRow(5);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style2);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(session.getLabel("PASSAGE")));
            c.setCellStyle(style2);
            c = row.createCell((short) 2);
            c.setCellValue(new HSSFRichTextString(myPassage.getIdPassage() + "   " + myPassage.getLibelle()));
            c.setCellStyle(style2);
            row = sheet.createRow(6);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(session.getLabel("TYPE_DECISION")));
            c.setCellStyle(style2);
            c = row.createCell((short) 2);
            c.setCellValue(new HSSFRichTextString(globaz.phenix.translation.CodeSystem.getLibelle(getSession(),
                    manager.getForTypeDecision())));
            c.setCellStyle(style2);
            row = sheet.createRow(7);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style2);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getLabel("GENRE_DECISION")));
            c.setCellStyle(style2);
            c = row.createCell((short) 2);
            c.setCellValue(new HSSFRichTextString(globaz.phenix.translation.CodeSystem.getLibelle(getSession(),
                    manager.getForGenreAffilie())));
            c.setCellStyle(style2);
            row = sheet.createRow(8);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style2);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getLabel("ANNEE")));
            c.setCellStyle(style2);
            c = row.createCell((short) 2);
            c.setCellValue(new HSSFRichTextString(manager.getForAnneeDecision()));
            c.setCellStyle(style2);

        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        // let's use a nifty font for the title
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        // create Title Row
        row = sheet.createRow(9);
        row = sheet.createRow(10);

        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
            c.setCellStyle(style);
        }
        return sheet;
    }

}