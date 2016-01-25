package globaz.naos.process;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TITiers;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * <H1>Description :</H1> processus qui met en "inactif" le tiers dont l'affiliation est de genre "Non soumis" et dont
 * la date de début d'affiliation est inférieure ou égale à la date saisie dans l'écran de lancement
 * 
 * @author BJO
 * 
 */
public class AFEpuCasNonSoumisProcess extends BProcess {

    private static final String DOC_NO = "CAF3015";
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private String date;
    private ArrayList dateAffiliationTiers;
    private ArrayList nomTiersDesactive;
    private ArrayList numAffiliation;

    private String simulation;
    private String titreDoc = "Epuration des cas non-soumis (Aucun non-soumis trouvé)";

    public AFEpuCasNonSoumisProcess() {
        super();
    }

    /**
     * @param parent
     */
    public AFEpuCasNonSoumisProcess(BProcess parent) {
        super(parent);
    }

    /**
     * @param session
     */
    public AFEpuCasNonSoumisProcess(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        boolean success = true;
        nomTiersDesactive = new ArrayList();
        dateAffiliationTiers = new ArrayList();
        numAffiliation = new ArrayList();

        try {
            // Création du manager
            AFAffiliationManager manager = new AFAffiliationManager();
            manager.setSession(getSession());
            manager.setForDateDebutAffLowerOrEqualTo(getDate());
            manager.setForTypeAffiliation(new String[] { CodeSystem.TYPE_AFFILI_NON_SOUMIS });

            manager.find(BManager.SIZE_NOLIMIT);
            for (int i = 0; i < manager.size(); i++) {
                AFAffiliation affiliation = (AFAffiliation) manager.getEntity(i);

                TITiers tiers = new TITiers();
                tiers.setIdTiers(affiliation.getIdTiers());
                tiers.setSession(getSession());
                tiers.retrieve();

                if (!tiers.getInactif().booleanValue()) {

                    numAffiliation.add(affiliation.getAffilieNumero());
                    nomTiersDesactive.add(affiliation.getTiersNom());
                    dateAffiliationTiers.add(affiliation.getDateDebut());

                    // désactivation des tier(s que si le mode s)imulation n'est pas
                    // coché!!!
                    if ((simulation != null) && getSimulation().equals("coche")) {
                        titreDoc = "Epuration des cas non-soumis (simulation)";
                    } else {
                        titreDoc = "Epuration des cas non-soumis";
                        tiers.setInactif(new Boolean(true));
                        tiers.wantCallValidate(false);
                        tiers.wantCallMethodAfter(false);
                        tiers.update(getTransaction());
                        tiers.wantCallValidate(true);
                        tiers.wantCallMethodAfter(true);
                    }
                }

            }
        } catch (Exception e) {
            success = false;
            abort();
            e.printStackTrace();
        }

        // Création du fichier Excel et attachement au mail
        try {
            this.registerAttachedDocument(populateSheet());
        } catch (Exception e) {
            success = false;
            JadeLogger.error(this, e);
            this._addError(e.getMessage());
        }

        return success;
    }

    public String getDate() {
        return date;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("EPURATION_CAS_NON_SOUMIS_ERREUR");
        } else {
            return getSession().getLabel("EPURATION_CAS_NON_SOUMIS_OK");
        }
    }

    public String getSimulation() {
        return simulation;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * <H1>Description :</H1> Méthode permettant de créer un fichier excel et de le remplire
     * 
     * @return String : chemin du fichier
     * @throws Exception
     */
    private String populateSheet() throws Exception {
        // workbook
        HSSFWorkbook wb = new HSSFWorkbook();
        // new sheet
        HSSFSheet sheet = wb.createSheet("Epuration des cas non-soumis");
        sheet.setDefaultColumnWidth((short) 23);
        // create TitleDoc Row
        HSSFRow row0 = sheet.createRow(0);
        HSSFCell c0 = row0.createCell((short) 0);
        c0.setCellValue(titreDoc);

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

        // création des entetes des colonnes
        setTitleRow(wb, sheet);

        HSSFCellStyle style = wb.createCellStyle();
        style.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // rempli le fichier
        for (int i = 0; i < nomTiersDesactive.size(); i++) {
            HSSFRow dataRow = sheet.createRow(sheet.getPhysicalNumberOfRows());
            HSSFCell c1 = dataRow.createCell((short) 0);
            c1.setCellValue(numAffiliation.get(i).toString());
            HSSFCell c2 = dataRow.createCell((short) 1);
            c2.setCellValue(nomTiersDesactive.get(i).toString());
            HSSFCell c3 = dataRow.createCell((short) 2);
            c3.setCellValue(dateAffiliationTiers.get(i).toString());

            if (i % 2 == 0) {
                c1.setCellStyle(style);
                c2.setCellStyle(style);
                c3.setCellStyle(style);
            }
        }

        sheet.createRow(sheet.getPhysicalNumberOfRows());
        HSSFRow rowLast = sheet.createRow(sheet.getPhysicalNumberOfRows());
        HSSFCell cell = rowLast.createCell((short) 0);
        cell.setCellValue(AFEpuCasNonSoumisProcess.DOC_NO);

        File f = File.createTempFile("workbook_", "_.xls");

        FileOutputStream fileOut = new FileOutputStream(f);
        wb.write(fileOut);
        fileOut.close();

        return f.getAbsolutePath();
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSimulation(String simulation) {
        this.simulation = simulation;
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
        HSSFRow row2 = sheet.createRow(2);// insertion d'une ligne vide
        HSSFRow row3 = sheet.createRow((short) 3);
        // Create title cells

        HSSFCell c = row3.createCell((short) 0);
        c.setCellStyle(style);
        c.setCellValue("Numéro d'affiliation");

        HSSFCell c1 = row3.createCell((short) 1);
        c1.setCellStyle(style);
        c1.setCellValue("Tiers désactivés");

        HSSFCell c2 = row3.createCell((short) 2);
        c2.setCellStyle(style);
        c2.setCellValue("Début de l'affiliation");

        return sheet;
    }

}
