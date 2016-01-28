package globaz.naos.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.access.AFAffiliationProvisoires;
import globaz.naos.db.affiliation.access.AFAffiliationProvisoiresManager;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class AFAffiliationProvisoiresProcessViewBean extends BProcess implements FWViewBeanInterface {

    private static final long serialVersionUID = -5510254510772089569L;
    private static final String DOC_NO = "0114CAF";
    private static final String TITRE_DOC = "Affiliations provisoires avec des cotisations";

    @Override
    protected void _executeCleanUp() {
    }

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

    @Override
    protected void _validate() throws Exception {
        setSendCompletionMail(true);
        setSendMailOnError(true);
        setControleTransaction(true);
        if ("".equals(getEMailAddress())) {
            this._addError(null, getSession().getLabel("IMPRESSION_NO_EMAIL"));
        }
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("IMPRESSION_AFF_PROV_ERREUR");
        } else {
            return getSession().getLabel("IMPRESSION_AFF_PROV_OK");
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    private String populateSheet() throws Exception {
        // workbook
        HSSFWorkbook wb = new HSSFWorkbook();
        // new sheet
        HSSFSheet sheet = wb.createSheet(getSession().getLabel("AFF_PROV"));
        sheet.setDefaultColumnWidth((short) 20);
        // create TitleDoc Row
        HSSFRow row0 = sheet.createRow(0);
        HSSFCell c0 = row0.createCell((short) 0);
        c0.setCellValue(AFAffiliationProvisoiresProcessViewBean.TITRE_DOC);

        HSSFCell c;
        HSSFRow row1 = sheet.createRow((short) 1);
        c = row1.createCell((short) 0);
        try {
            BSession sessionPhenix = (BSession) GlobazServer.getCurrentSystem().getApplication("PHENIX").newSession();
            c.setCellValue(sessionPhenix.getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase()));
        } catch (Exception e) {
            c.setCellValue("");
        }

        setTitleRow(wb, sheet);

        AFAffiliationProvisoiresManager afProv = new AFAffiliationProvisoiresManager();
        afProv.setSession(getSession());
        afProv.find(getTransaction(), BManager.SIZE_NOLIMIT);
        for (int i = 1; i < afProv.size(); i++) {
            AFAffiliationProvisoires entity = (AFAffiliationProvisoires) afProv.getEntity(i);
            HSSFRow dataRow = sheet.createRow(sheet.getPhysicalNumberOfRows());
            dataRow.createCell((short) 0).setCellValue(entity.getAffilieNumero());
            dataRow.createCell((short) 1).setCellValue(entity.getTiers().getNumAvsActuel());
            dataRow.createCell((short) 2).setCellValue(entity.getTiers().getNom());
        }
        sheet.createRow(sheet.getPhysicalNumberOfRows());
        HSSFRow rowLast = sheet.createRow(sheet.getPhysicalNumberOfRows());
        HSSFCell cell = rowLast.createCell((short) 0);
        cell.setCellValue(AFAffiliationProvisoiresProcessViewBean.DOC_NO);

        File f = File.createTempFile("workbook_", "_.xls");

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
        c1.setCellValue(getSession().getLabel("NUM_AVS"));

        HSSFCell c2 = row3.createCell((short) 2);
        c2.setCellStyle(style);
        c2.setCellValue(getSession().getLabel("NOM_PRENOM"));

        return sheet;
    }

}
