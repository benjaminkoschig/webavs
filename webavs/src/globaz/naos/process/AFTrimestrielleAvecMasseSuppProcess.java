package globaz.naos.process;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JADate;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.masse.AFMasseAffilieParitaire;
import globaz.naos.db.masse.AFMasseAffilieParitaireManager;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author SEL <br>
 *         Date : 18 févr. 08
 */
public class AFTrimestrielleAvecMasseSuppProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String DOC_NO = "0193CAF";
    private static final String FILE_FORMAT = ".xls";
    private static final String FILE_NAME = "workbook_";
    private static final String TITRE_DOC = "Affiliés trimestriels avec masse supérieure à 200'000";

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        try {
            this.registerAttachedDocument(populateSheet());
        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(e.getMessage());
        }
        return true;
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        setSendCompletionMail(true);
        setSendMailOnError(true);
        setControleTransaction(true);
        if ("".equals(getEMailAddress())) {
            this._addError(null, getSession().getLabel("IMPRESSION_NO_EMAIL"));
        }
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("IMPRESSION_MASSE_SUPP_ERREUR");
        } else {
            return getSession().getLabel("IMPRESSION_MASSE_SUPP_OK");
        }
    }

    /**
     * Renvoie la Job Queue à utiliser pour soumettre le process.
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Method populateSheet.
     * 
     * @return String
     */
    private String populateSheet() throws Exception {
        // workbook
        HSSFWorkbook wb = new HSSFWorkbook();
        // new sheet
        HSSFSheet sheet = wb.createSheet(getSession().getLabel("AFF_MASSE_SUPP"));
        sheet.setDefaultColumnWidth((short) 20);

        HSSFCell c0;
        HSSFRow row0 = sheet.createRow((short) 0);
        c0 = row0.createCell((short) 0);
        c0.setCellValue(AFTrimestrielleAvecMasseSuppProcess.TITRE_DOC);

        HSSFCell c;
        HSSFRow row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        c = row.createCell((short) 0);
        try {
            BSession sessionPhenix = (BSession) GlobazServer.getCurrentSystem().getApplication("PHENIX").newSession();
            c.setCellValue(sessionPhenix.getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase()));
        } catch (Exception e) {
            c.setCellValue("");
        }

        // Create title row
        setTitleRow(wb, sheet);

        AFMasseAffilieParitaireManager afProv = new AFMasseAffilieParitaireManager();
        afProv.setSession(getSession());
        afProv.find(getTransaction(), BManager.SIZE_NOLIMIT);
        for (int i = 0; i < afProv.size(); i++) {
            AFMasseAffilieParitaire entity = (AFMasseAffilieParitaire) afProv.getEntity(i);
            HSSFRow dataRow = sheet.createRow(sheet.getPhysicalNumberOfRows());
            dataRow.createCell((short) 0).setCellValue(entity.getNumAffilie());
            dataRow.createCell((short) 1).setCellValue(entity.getTiers().getNom());
            dataRow.createCell((short) 2).setCellValue(new JADate(new BigDecimal(entity.getDateDebut())).toStr("."));
            dataRow.createCell((short) 3).setCellValue(new FWCurrency(entity.getMasseAnnuelle()).toStringFormat());
        }

        sheet.createRow(sheet.getPhysicalNumberOfRows());
        HSSFRow rowRef = sheet.createRow(sheet.getPhysicalNumberOfRows());
        HSSFCell cell = rowRef.createCell((short) 0);
        cell.setCellValue(AFTrimestrielleAvecMasseSuppProcess.DOC_NO);

        File f = File.createTempFile(AFTrimestrielleAvecMasseSuppProcess.FILE_NAME,
                AFTrimestrielleAvecMasseSuppProcess.FILE_FORMAT);

        FileOutputStream fileOut = new FileOutputStream(f);
        wb.write(fileOut);
        fileOut.close();

        return f.getAbsolutePath();
    }

    private HSSFSheet setTitleRow(HSSFWorkbook wb, HSSFSheet sheet) {
        // let's use a nifty font for the title
        HSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        HSSFCellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);

        // Create title row
        HSSFRow row2 = sheet.createRow(2);// ligne vide
        HSSFRow row3 = sheet.createRow((short) 3);
        // Create title cells

        HSSFCell c = row3.createCell((short) 0);
        c.setCellStyle(style);
        c.setCellValue(getSession().getLabel("NUM_AFFILIE"));

        HSSFCell c1 = row3.createCell((short) 1);
        c1.setCellStyle(style);
        c1.setCellValue(getSession().getLabel("AFF_DESIGNATION"));

        HSSFCell c2 = row3.createCell((short) 2);
        c2.setCellStyle(style);
        c2.setCellValue(getSession().getLabel("AFF_DATE_DEBUT"));

        HSSFCell c3 = row3.createCell((short) 3);
        c3.setCellStyle(style);
        c3.setCellValue(getSession().getLabel("AFF_MASSE_ANNUELLE"));

        return sheet;
    }
}
