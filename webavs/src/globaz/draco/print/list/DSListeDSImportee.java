package globaz.draco.print.list;

import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JATime;
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

/**
 * Insérez la description du type ici. Date de création : (17.06.2003 14:38:38)
 * 
 * @author: ado
 */
public class DSListeDSImportee {
    private DSDeclarationListViewBean manager = null;
    private BProcess processAppelant = null;
    private BSession session = null;
    private HSSFSheet sheet;
    private HSSFWorkbook wb;

    /**
     * Commentaire relatif au constructeur TentDocument.
     */
    public DSListeDSImportee() {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet(DSDeclarationViewBean.getLabelPourProvenance(getSession(), manager.getForProvenance()));
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
    public DSListeDSImportee(BSession session, DSDeclarationListViewBean manager1) {
        manager = manager1;
        this.session = session;
        wb = new HSSFWorkbook();
        sheet = wb.createSheet(getSession().getLabel("PAGE") + " 1");
        // format
        sheet.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet.getPrintSetup().setLandscape(true);

        // marges en-tête/pied de page
        sheet.getPrintSetup().setHeaderMargin(0);
        sheet.getPrintSetup().setFooterMargin(0);
        sheet = setTitleRow(wb, sheet);

    }

    public String getOutputFile() {
        try {
            File f = null;
            f = File.createTempFile(getSession().getLabel("LISTE_DS_IMPORTEE1") + "_" + JACalendar.today().toStrAMJ()
                    + "_", ".xls");
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

    public HSSFSheet populateSheet(DSDeclarationListViewBean manager, BTransaction transaction) throws Exception {
        BStatement statement = null;
        DSDeclarationViewBean declarationDS = null;

        processAppelant.setProgressScaleValue(manager.getCount(transaction));
        statement = manager.cursorOpen(transaction);

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
        style1.setAlignment(HSSFCellStyle.ALIGN_LEFT);

        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style2.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));

        sheet.setColumnWidth((short) 0, (short) 4200);
        sheet.setColumnWidth((short) 1, (short) 8000);
        sheet.setColumnWidth((short) 2, (short) 2000);
        sheet.setColumnWidth((short) 3, (short) 3000);
        sheet.setColumnWidth((short) 4, (short) 4000);

        while (((declarationDS = (DSDeclarationViewBean) manager.cursorReadNext(statement)) != null)
                && (!declarationDS.isNew())) {
            HSSFRow row = sheet.createRow(sheet.getPhysicalNumberOfRows() + 1);
            int colNum = 0;
            HSSFCell cell = row.createCell((short) colNum++);
            // Num affilié
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(declarationDS.getNumeroAffilie()));
            // prénom et nom
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style1);
            cell.setCellValue(new HSSFRichTextString(declarationDS.getDesignation1()));
            // Année
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(declarationDS.getAnnee()));
            // Date référence
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(declarationDS.getDateRetourEff()));
            // Masse
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(declarationDS.getMasseSalTotal()));
            processAppelant.incProgressCounter();
        }
        manager.cursorClose(statement);
        statement = null;
        HSSFFooter footer = sheet.getFooter();
        // Décision pour une période donnée
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0311CDS");
        return sheet;
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
        final String num = getSession().getLabel("NUM_AFFILIE");
        final String nom = getSession().getLabel("NOM_PRENOM");
        final String annee = getSession().getLabel("ANNEE");
        final String dateReference = getSession().getLabel("DRACO_LISTE_DATE_REFERENCE");
        final String masse = getSession().getLabel("DRACO_LISTE_MASSE");
        // final String collaborateur = getSession().getLabel("COLLABORATEUR");
        final String[] COL_TITLES = { num, nom, annee, dateReference, masse };
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
        row = sheet.createRow(2);
        c = row.createCell((short) 2);
        c.setCellStyle(style2);
        c.setCellValue(new HSSFRichTextString(session.getLabel("LISTE_DS_IMPORTEE") + " "
                + DSDeclarationViewBean.getLabelPourProvenance(getSession(), manager.getForProvenance())));

        row = sheet.createRow(3);
        // création de l'entête
        try {
            row = sheet.createRow(4);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(session.getLabel("PERIODE")));
            c.setCellStyle(style2);
            c = row.createCell((short) 1);
            c.setCellStyle(style3);
            c.setCellValue(new HSSFRichTextString(manager.getForDateRetourEffGreaterOrEquals() + " - "
                    + manager.getForDateRetourEffLowerOrEquals()));

            //
            row = sheet.createRow(5);
            c = row.createCell((short) 0);
            c.setCellStyle(style2);
            c.setCellValue(new HSSFRichTextString(getSession().getLabel("ANNEE")));
            c = row.createCell((short) 1);
            c.setCellStyle(style3);
            c.setCellValue(new HSSFRichTextString(manager.getForAnnee()));

        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        // let's use a nifty font for the title
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        style.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        // create Title Row
        row = sheet.createRow(7);
        int lengthCol = COL_TITLES.length;
        for (int i = 0; i < lengthCol; i++) {
            c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
            c.setCellStyle(style);
        }
        return sheet;
    }
}
