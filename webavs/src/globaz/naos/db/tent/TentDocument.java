package globaz.naos.db.tent;

import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class TentDocument {

    private final static String[] COL_TITLES = { "N°affilié", "N°AVS", "Nom", "Prénom", "Rue", "Code Postal", "Lieu",
            "Code canton", "Allemand", "Français", "Italien", "Sexe", "User" };

    // Numéro du document
    private static final String DOC_NO = "0113CAF";
    private static final String TITRE_DOC = "Nouvelles affiliations personnelles";
    private HSSFSheet sheet;
    private HSSFWorkbook wb;

    /**
     * Commentaire relatif au constructeur RentierDocument.
     */
    public TentDocument() {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet("TENT");
    }

    /**
     * Commentaire relatif au constructeur RentierDocument.
     * 
     * @param sheetTitle
     */
    public TentDocument(String sheetTitle) {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet(sheetTitle);
        sheet.setDefaultColumnWidth((short) 10);
        sheet = setTitleRow(wb, sheet);
    }

    /**
     * Renvoie le path absolu du fichier.
     * 
     * @return
     */
    public String getOutputFile() {
        try {
            // File f = new File("tent_" + JACalendar.today().toStrAMJ() + "_" +
            // Math.round((Math.random() * 10000) % 10000) + ".xls");
            File f = File.createTempFile("tent_" + JACalendar.today().toStrAMJ() + "_", ".xls");
            f.deleteOnExit();
            FileOutputStream out = new FileOutputStream(f);
            wb.write(out);
            out.close();
            return f.getAbsolutePath();
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return "";
        }
    }

    /**
     * Rempli le sheet Excel.
     * 
     * @param manager
     * @return
     */
    public HSSFSheet populateSheet(AFAffiliationManager manager) {
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        for (int i = 0; i < manager.size(); i++) {
            // Balayer sur affilié
            AFAffiliation affiliation = (AFAffiliation) manager.getEntity(i);
            // TIHistoriqueTiers hTiers = (TIHistoriqueTiers)
            // manager.getEntity(i);
            // ///////////
            // TITiersViewBean personne = new TITiersViewBean();
            // personne.setSession(manager.getSession());
            // personne.setIdTiers((affiliation.getIdTiers()));
            try {

                TITiersViewBean personne = affiliation.getTiers();
                personne.retrieve();

                //
                // if (personne.getTypeTiers().equals(personne.CS_AFFILIE) ||
                // personne.getTypeTiers().equals(personne.CS_ASSUREAFFILIE)) {
                // non actif
                if ((affiliation.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_NON_ACTIF))
                        || (affiliation.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_PROVIS))) {
                    java.util.Hashtable data;
                    try {
                        TIAvoirAdresse avoirAdresse;
                        TIAdresseDataSource dataSource = new TIAdresseDataSource();
                        avoirAdresse = personne.findAvoirAdresse(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                                IConstantes.CS_APPLICATION_DEFAUT, new JADate().toStr("."));
                        dataSource.load(avoirAdresse);
                        data = dataSource.getData();
                    } catch (Exception e) {
                        JadeLogger.error(this, e);
                        data = new java.util.Hashtable();
                    }

                    // /////////
                    HSSFRow row = sheet.createRow(sheet.getPhysicalNumberOfRows());
                    int colNum = 0;
                    // numéro affilié
                    HSSFCell cell = row.createCell((short) colNum++);
                    // cell.setCellValue(personne.getNumAffilieActuel());
                    cell.setCellValue(affiliation.getAffilieNumero());
                    // num AVS
                    cell = row.createCell((short) colNum++);
                    cell.setCellValue(personne.getNumAvsActuel());
                    // name
                    cell = row.createCell((short) colNum++);
                    cell.setCellValue(personne.getDesignation1());
                    // vorname
                    cell = row.createCell((short) colNum++);
                    cell.setCellValue(personne.getDesignation2());
                    // strasse
                    cell = row.createCell((short) colNum++);
                    try {
                        cell.setCellValue(personne.getRue());
                    } catch (Exception e) {
                        cell.setCellValue("X");
                    }
                    // code postal
                    cell = row.createCell((short) colNum++);
                    cell.setCellValue((String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA));
                    // ville
                    cell = row.createCell((short) colNum++);
                    cell.setCellValue((String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE));
                    // code canton + canton
                    cell = row.createCell((short) colNum++);
                    try {
                        cell.setCellValue(Integer.parseInt((String) data
                                .get(TIAbstractAdresseDataSource.ADRESSE_VAR_CANTON_CODE_OFAS))
                                + " "
                                + (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_CANTON));
                    } catch (Exception e) {
                        cell.setCellValue("X");
                    }
                    // Allemand
                    cell = row.createCell((short) colNum++);
                    if (personne.getLangue().equals(TITiers.CS_ALLEMAND)) {
                        cell.setCellValue(true);
                    } else {
                        cell.setCellValue(false);
                    }
                    // Français
                    cell = row.createCell((short) colNum++);
                    if (personne.getLangue().equals(TITiers.CS_FRANCAIS)) {
                        cell.setCellValue(true);
                    } else {
                        cell.setCellValue(false);
                    }
                    // Italien
                    cell = row.createCell((short) colNum++);
                    if (personne.getLangue().equals(TITiers.CS_ITALIEN)) {
                        cell.setCellValue(true);
                    } else {
                        cell.setCellValue(false);
                    }
                    // sexe
                    cell = row.createCell((short) colNum++);
                    if (personne.getSexe().equals(TITiersViewBean.CS_HOMME)) {
                        cell.setCellValue("m");
                    } else if (personne.getSexe().equals(TITiersViewBean.CS_FEMME)) {
                        cell.setCellValue("f");
                    }
                    cell = row.createCell((short) colNum++);
                    cell.setCellValue(personne.getSpy().getUser());
                }

            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        sheet.createRow(sheet.getPhysicalNumberOfRows());
        HSSFRow row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(TentDocument.DOC_NO);
        /*
         * try { wb.write(new FileOutputStream("c:/temp/tent.xls")); } catch (Exception e) { e.printStackTrace(); }
         */
        return sheet;
    }

    /**
     * Definis le titre de la ligne.
     * 
     * @param wb
     * @param sheet
     * @return
     */
    private HSSFSheet setTitleRow(HSSFWorkbook wb, HSSFSheet sheet) {
        // let's use a nifty font for the title
        HSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        HSSFCellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        // ??? style.setFillBackgroundColor((short) 150);
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);

        // create TitleDoc Row
        HSSFRow row0 = sheet.createRow(0);
        HSSFCell c0 = row0.createCell((short) 0);
        c0.setCellValue(TentDocument.TITRE_DOC);

        // create Title CompanyName Row
        HSSFCell c1;
        HSSFRow row1 = sheet.createRow((short) 1);
        c1 = row1.createCell((short) 0);
        try {
            BSession sessionPhenix = (BSession) GlobazServer.getCurrentSystem().getApplication("PHENIX").newSession();
            c1.setCellValue(sessionPhenix.getApplication().getProperty(
                    "COMPANYNAME_" + sessionPhenix.getIdLangueISO().toUpperCase()));
        } catch (Exception e) {
            c1.setCellValue("");
        }

        // create col_titles
        HSSFRow row2 = sheet.createRow(2);// ligne vide
        HSSFRow row3 = sheet.createRow(3);
        for (int i = 0; i < TentDocument.COL_TITLES.length; i++) {
            // set cell value
            HSSFCell c = row3.createCell((short) i);
            c.setCellValue(TentDocument.COL_TITLES[i]);
            c.setCellStyle(style);
        }
        return sheet;
    }
}
