package globaz.phenix.reprise;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliation;
import globaz.phenix.db.principale.CPDecisionAffiliationManager;
import globaz.phenix.db.principale.CPDecisionManager;
import globaz.phenix.db.principale.CPDonneesBase;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Insérez la description du type ici. Date de création : (17.06.2003 14:38:38)
 * 
 * @author: jpa
 */
public class CPListeAcompteRevenuAnnualise {
    private CPDecisionAffiliationManager manager = null;
    private BProcess processAppelant = null;
    private BSession session = null;
    private HSSFSheet sheet;
    private HSSFWorkbook wb;

    /**
     * Commentaire relatif au constructeur TentDocument.
     */
    public CPListeAcompteRevenuAnnualise() {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet("COMP1");
    }

    /**
     * Commentaire relatif au constructeur TentDocument.
     */
    public CPListeAcompteRevenuAnnualise(BSession session) {
        this.session = session;
        wb = new HSSFWorkbook();
        sheet = wb.createSheet("CCVD - 2011");
        sheet = setTitleRow(wb, sheet);
    }

    public CPDecisionAffiliationManager getManager() {
        return manager;
    }

    public String getOutputFile() {
        try {
            File f = File.createTempFile("Acompte avec revenu doublement annualisé", ".xls");
            // f.deleteOnExit();
            FileOutputStream out = new FileOutputStream(f);
            wb.write(out);
            out.close();
            return f.getAbsolutePath();
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return "";
        }
    }

    /**
     * Returns the processAppelant.
     * 
     * @return BProcess
     */
    public BProcess getProcessAppelant() {
        return processAppelant;
    }

    /**
     * Returns the session.
     * 
     * @return BSession
     */
    public BSession getSession() {
        return session;
    }

    public HSSFSheet populateSheet(CPDecisionAffiliationManager manager, BTransaction transaction) throws Exception {

        BStatement statement = null;
        CPDecisionAffiliation decision = null;
        // decision.setSession(transaction.getSession());
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style2.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
        processAppelant.setProgressScaleValue(manager.getCount(transaction));
        statement = manager.cursorOpen(transaction);
        sheet.setColumnWidth((short) 0, (short) 3500);
        sheet.setColumnWidth((short) 1, (short) 3000);
        sheet.setColumnWidth((short) 2, (short) 3000);
        sheet.setColumnWidth((short) 3, (short) 3500);
        sheet.setColumnWidth((short) 4, (short) 3000);
        sheet.setColumnWidth((short) 5, (short) 3000);
        sheet.setColumnWidth((short) 6, (short) 3500);
        int nbRow = 11;
        try {
            while (((decision = (CPDecisionAffiliation) manager.cursorReadNext(statement)) != null)
                    && (!decision.isNew())) {
                boolean casALister = false;
                // Recherche décision de base
                CPDecisionManager mng1 = new CPDecisionManager();
                mng1.setForIdTiers(decision.getIdTiers());
                mng1.setSession(getSession());
                mng1.setNotInTypeDecision(CPDecision.CS_ACOMPTE + ", " + CPDecision.CS_IMPUTATION);
                mng1.setForGenreAffilie(decision.getGenreAffilie());
                mng1.setToAnneeDecision("2010");
                mng1.orderByAnneeDecision();
                mng1.orderByIdDecision();
                mng1.find();
                CPDecision decBase = new CPDecision();
                CPDonneesBase doBase = null;
                boolean casSuivant = false;
                for (int i = 0; (i < mng1.size()) && !casSuivant; i++) {
                    decBase = (CPDecision) mng1.getEntity(i);
                    // if (BSessionUtil.compareDateFirstLower(this.getSession(), decBase.getDateFacturation(),
                    // "16.02.2011")) {
                    casSuivant = true;
                    // Test si période incomplète
                    int md = JACalendar.getMonth(decBase.getDebutDecision());
                    int mf = JACalendar.getMonth(decBase.getFinDecision());
                    if ((md != 1) || (mf != 12)) {
                        // Extraction donnée
                        doBase = new CPDonneesBase();
                        doBase.setSession(getSession());
                        doBase.setIdDecision(decBase.getIdDecision());
                        doBase.retrieve();
                        if ((doBase.getNbMoisExercice1().equalsIgnoreCase("12") && !JadeStringUtil.isBlankOrZero(doBase
                                .getRevenu1()))
                                || (doBase.getNbMoisRevenuAutre1().equalsIgnoreCase("12") && !JadeStringUtil
                                        .isBlankOrZero(doBase.getRevenuAutre1()))) {
                            casALister = true;
                        }
                    }
                    // }
                }
                if (casALister) {
                    // Recherche revenu acompte
                    CPDonneesBase dob = new CPDonneesBase();
                    dob.setSession(getSession());
                    dob.setIdDecision(decision.getIdDecision());
                    dob.retrieve();
                    HSSFRow row = sheet.createRow(nbRow);
                    nbRow++;
                    int colNum = 0;
                    // 0 - numéro affilié
                    HSSFCell cell = row.createCell((short) colNum++);
                    cell.setCellStyle(style);
                    cell.setCellValue(new HSSFRichTextString(decision.getNumAffilie()));
                    // 1 - Revenu 1 acompte
                    cell = row.createCell((short) colNum++);
                    cell.setCellStyle(style2);
                    cell.setCellValue(new HSSFRichTextString(dob.getRevenu1()));
                    // 2 - Revenu autre acompte
                    cell = row.createCell((short) colNum++);
                    cell.setCellStyle(style2);
                    cell.setCellValue(new HSSFRichTextString(dob.getRevenuAutre1()));
                    // 3 - periode acompte
                    String periode = JACalendar.getDay(decision.getDebutDecision()) + "."
                            + JACalendar.getMonth(decision.getDebutDecision()) + " - " + decision.getFinDecision();
                    cell = row.createCell((short) colNum++);
                    cell.setCellStyle(style);
                    cell.setCellValue(new HSSFRichTextString(periode));
                    // 4 - Revenu 1 décision
                    cell = row.createCell((short) colNum++);
                    cell.setCellStyle(style2);
                    cell.setCellValue(new HSSFRichTextString(doBase.getRevenu1()));
                    // 5 - Revenu autre decision
                    cell = row.createCell((short) colNum++);
                    cell.setCellStyle(style2);
                    cell.setCellValue(new HSSFRichTextString(doBase.getRevenuAutre1()));
                    // 6 - periode acompte
                    periode = JACalendar.getDay(decBase.getDebutDecision()) + "."
                            + JACalendar.getMonth(decBase.getDebutDecision()) + " - " + decBase.getFinDecision();
                    cell = row.createCell((short) colNum++);
                    cell.setCellStyle(style);
                    cell.setCellValue(new HSSFRichTextString(periode));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            manager.cursorClose(statement);
        }
        statement = null;
        return sheet;
    }

    public void setManager(CPDecisionAffiliationManager manager) {
        this.manager = manager;
    }

    /**
     * Sets the processAppelant.
     * 
     * @param processAppelant
     *            The processAppelant to set
     */
    public void setProcessAppelant(BProcess processAppelant) {
        this.processAppelant = processAppelant;
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

    /**
     * @param wb
     * @param sheet
     * @param row
     * @return
     */
    private HSSFSheet setTitleRow(HSSFWorkbook wb, HSSFSheet sheet) {

        final String[] COL_TITLES = { session.getLabel("NUM_AFFILIE"), "Revenu acompte", "Revenu autre acompte",
                "Période acompte", "Revenu décision", "Revenu autre décision", "Période décision" };

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
        // row = sheet.createRow(1);
        // row = sheet.createRow(2);
        c = row.createCell((short) 5);
        c.setCellValue(new HSSFRichTextString(JACalendar.todayJJsMMsAAAA()));
        c.setCellStyle(style2);
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        c = row.createCell((short) 1);
        c.setCellValue(new HSSFRichTextString("Acompte avec revenu doublement annualisé"));
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
        row = sheet.createRow(10);
        for (int j = 0; j < COL_TITLES.length; j++) {
            c = row.createCell((short) j);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[j]));
            c.setCellStyle(style);
        }
        return sheet;
    }
}
