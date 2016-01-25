package globaz.naos.process;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.db.affiliation.access.AFAffiliationNonProvisoires;
import globaz.naos.db.affiliation.access.AFAffiliationNonProvisoiresManager;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author ado
 * 
 * 
 * 
 *         22 avr. 04
 * 
 */
public class AFAffiliationNonProvisoiresProcessViewBean extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Numéro du document
    private static final String DOC_NO = "0115CAF";
    private static final String TITRE_DOC = "Affiliations non provisoires sans cotisation";

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
            String localPath = populateSheet();
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setDocumentType(AFAffiliationNonProvisoiresProcessViewBean.DOC_NO);
            docInfo.setDocumentTypeNumber("");
            this.registerAttachedDocument(docInfo, localPath);

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
            return getSession().getLabel("IMPRESSION_AFF_NON_PROV_ERREUR");
        } else {
            return getSession().getLabel("IMPRESSION_AFF_NON_PROV_OK");
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
        HSSFSheet sheet = wb.createSheet(getSession().getLabel("AFF_NON_PROV"));
        sheet.setDefaultColumnWidth((short) 20);

        HSSFCell c0;
        HSSFRow row0 = sheet.createRow((short) 0);
        c0 = row0.createCell((short) 0);
        c0.setCellValue(new HSSFRichTextString(AFAffiliationNonProvisoiresProcessViewBean.TITRE_DOC));

        HSSFRow row1 = sheet.createRow((short) 1);
        HSSFCell c = row1.createCell((short) 0);
        try {
            BSession sessionPhenix = (BSession) GlobazServer.getCurrentSystem().getApplication("PHENIX").newSession();
            c.setCellValue(new HSSFRichTextString(sessionPhenix.getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase())));
        } catch (Exception e) {
            c.setCellValue(new HSSFRichTextString(""));
        }

        setTitleRow(wb, sheet);

        AFAffiliationNonProvisoiresManager afProv = new AFAffiliationNonProvisoiresManager();
        afProv.setSession(getSession());
        afProv.find(getTransaction(), BManager.SIZE_NOLIMIT);
        setProgressScaleValue(afProv.getCount(getTransaction()));
        for (int i = 1; i < afProv.size(); i++) {
            AFAffiliationNonProvisoires entity = (AFAffiliationNonProvisoires) afProv.getEntity(i);
            HSSFRow dataRow = sheet.createRow(sheet.getPhysicalNumberOfRows());
            dataRow.createCell((short) 0).setCellValue(new HSSFRichTextString(entity.getNumAffilie()));
            dataRow.createCell((short) 1).setCellValue(new HSSFRichTextString(entity.getTiers().getNumAvsActuel()));
            dataRow.createCell((short) 2).setCellValue(new HSSFRichTextString(entity.getTiers().getNom()));
            incProgressCounter();
        }
        HSSFFooter footer = sheet.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: "
                + AFAffiliationNonProvisoiresProcessViewBean.DOC_NO);

        File f = File.createTempFile(getSession().getLabel("AFF_NON_PROV").replace(" ", "_").toUpperCase() + "_",
                "_.xls");

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
        HSSFRow row3 = sheet.createRow((short) 3);
        // Create title cells

        HSSFCell c = row3.createCell((short) 0);
        c.setCellStyle(style);
        c.setCellValue(new HSSFRichTextString(getSession().getLabel("NUM_AFFILIE")));

        HSSFCell c1 = row3.createCell((short) 1);
        c1.setCellStyle(style);
        c1.setCellValue(new HSSFRichTextString(getSession().getLabel("NUM_AVS")));

        HSSFCell c2 = row3.createCell((short) 2);
        c2.setCellStyle(style);
        c2.setCellValue(new HSSFRichTextString(getSession().getLabel("NOM_PRENOM")));

        return sheet;
    }

}
