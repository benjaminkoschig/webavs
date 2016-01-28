package globaz.naos.db.rentier;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.db.planAffiliation.AFPlanAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Insérez la description du type ici. Date de création : (17.06.2003 14:38:38)
 * 
 * @author: ado
 */
public class RentierDocument {

    private final static String[] COL_TITLES = { "N°affilié", "N°AVS", "Nom", "Prénom", "Date naissance",
            "Agence communale" };

    private HSSFSheet sheet;
    private HSSFWorkbook wb;

    /**
     * Commentaire relatif au constructeur RentierDocument.
     */
    public RentierDocument() {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet("RENTIER");
    }

    /**
     * Commentaire relatif au constructeur RentierDocument.
     * 
     * @param sheetTitle
     */
    public RentierDocument(String sheetTitle) {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet(sheetTitle);
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
            File f = File.createTempFile("rentier_" + JACalendar.today().toStrAMJ() + "_", ".xls");
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
     * @param dateDebut
     * @param dateFin
     * @param getSession
     * @param memoryLog
     * @return
     */
    public HSSFSheet populateSheet(AFAffiliationManager manager, String dateDebut, String dateFin, BSession getSession,
            FWMemoryLog memoryLog, BTransaction transaction, boolean changementPeriodicite) {
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        for (int i = 0; i < manager.size(); i++) {
            // Balayer sur affilié
            AFAffiliation affiliation = (AFAffiliation) manager.getEntity(i);
            try {
                TITiersViewBean personne = affiliation.getTiers();
                personne.retrieve();
                if (!personne.isNew()) {
                    if (JadeStringUtil.isBlankOrZero(personne.getDateNaissance())) {
                        memoryLog.logMessage(
                                affiliation.getAffilieNumero() + " - " + getSession.getLabel("CP_SEDEX_MSG7"),
                                FWViewBeanInterface.ERROR, "");
                    }
                } else {
                    memoryLog.logMessage(affiliation.getAffilieNumero() + " - " + getSession.getLabel("791"),
                            FWViewBeanInterface.ERROR, "");
                }
                if (BSessionUtil.compareDateBetweenOrEqual(getSession, dateDebut, dateFin, personne.getDateRentier()
                        .toString())) {
                    if ((affiliation.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_NON_ACTIF) || affiliation
                            .getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_PROVIS))
                            && JadeStringUtil.isIntegerEmpty(affiliation.getDateFin())) {
                        AFPlanAffiliationManager planAffiliationManager = new AFPlanAffiliationManager();
                        planAffiliationManager.setForAffiliationId(affiliation.getAffiliationId());
                        planAffiliationManager.setSession(transaction.getSession());
                        planAffiliationManager.find(transaction);
                        // changement de la periodicite selon la valeur de la case à cocher
                        for (int j = 0; j < planAffiliationManager.size(); j++) {
                            AFPlanAffiliation planAffiliation = (AFPlanAffiliation) planAffiliationManager.getEntity(j);
                            if (!planAffiliation.isInactif().booleanValue()) {
                                // Mise à jour date de fin et motif dans l'affiliation
                                updateAffiliation(getSession, transaction, changementPeriodicite, affiliation, personne);
                                if (transaction.hasErrors()) {
                                    memoryLog.logMessage(affiliation.getAffilieNumero() + " - "
                                            + transaction.getErrors().toString(), FWViewBeanInterface.ERROR, "");
                                } else {
                                    // Mise à jour date de fin et motif dans les cotisations
                                    updateCotisation(getSession, transaction, changementPeriodicite, affiliation,
                                            personne, planAffiliation);
                                    if (transaction.hasErrors()) {
                                        memoryLog.logMessage(affiliation.getAffilieNumero() + " - "
                                                + transaction.getErrors().toString(), FWViewBeanInterface.ERROR, "");
                                    }
                                }
                            }
                        }
                        HSSFRow row = sheet.createRow(sheet.getPhysicalNumberOfRows());
                        int colNum = 0;
                        // numéro affilié
                        HSSFCell cell = row.createCell((short) colNum++);
                        cell.setCellValue(new HSSFRichTextString(affiliation.getAffilieNumero()));
                        // num AVS
                        cell = row.createCell((short) colNum++);
                        cell.setCellValue(new HSSFRichTextString(personne.getNumAvsActuel()));
                        // name
                        cell = row.createCell((short) colNum++);
                        cell.setCellValue(new HSSFRichTextString(personne.getDesignation1()));
                        // vorname
                        cell = row.createCell((short) colNum++);
                        cell.setCellValue(new HSSFRichTextString(personne.getDesignation2()));
                        // geburstDatum
                        cell = row.createCell((short) colNum++);
                        cell.setCellValue(new HSSFRichTextString(personne.getDateNaissance()));
                        // geburstDatum
                        cell = row.createCell((short) colNum++);
                        cell.setCellValue(new HSSFRichTextString(affiliation.getAgenceCom(
                                affiliation.getAffiliationId(), JACalendar.todayJJsMMsAAAA())));
                    }
                }
            } catch (Exception e) {
                memoryLog.logMessage("Problème de mise à jour pour l'affilié: " + affiliation.getAffilieNumero()
                        + " - " + e.toString(), FWViewBeanInterface.ERROR, "");
            }
        }
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
        HSSFRow row = null;
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
        // create Title Row
        row = sheet.createRow(0);
        for (int i = 0; i < RentierDocument.COL_TITLES.length; i++) {
            // set cell value
            HSSFCell c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(RentierDocument.COL_TITLES[i]));
            c.setCellStyle(style);
        }
        return sheet;
    }

    /**
     * Mise à jour de l'affilation avec informations pour radiation rentier
     * 
     * @param getSession
     * @param transaction
     * @param changementPeriodicite
     * @param affiliation
     * @param personne
     * @throws Exception
     */

    protected void updateAffiliation(BSession getSession, BTransaction transaction, boolean changementPeriodicite,
            AFAffiliation affiliation, TITiersViewBean personne) throws Exception {
        if ((changementPeriodicite == true) && !affiliation.getPeriodicite().equals(CodeSystem.PERIODICITE_ANNUELLE)) {
            affiliation.setPeriodicite(CodeSystem.PERIODICITE_MENSUELLE);
        }
        affiliation.setDateFin(personne.getDateRentier().toStr("."));
        affiliation.setMotifFin(CodeSystem.MOTIF_FIN_RETRAITE);
        affiliation.setSession(getSession);
        affiliation.update(transaction);
    }

    /**
     * Mise à jour des cotisation d'un affilié avec informations pour radiation rentier
     * 
     * @param getSession
     * @param transaction
     * @param changementPeriodicite
     * @param affiliation
     * @param personne
     * @throws Exception
     */
    protected void updateCotisation(BSession getSession, BTransaction transaction, boolean changementPeriodicite,
            AFAffiliation affiliation, TITiersViewBean personne, AFPlanAffiliation planAffiliation) throws Exception {
        AFCotisationManager cotisationManager = new AFCotisationManager();
        cotisationManager.setForPlanAffiliationId(planAffiliation.getPlanAffiliationId());
        cotisationManager.setDateFinGreaterEqual(personne.getDateRentier().toStr("."));
        cotisationManager.setSession(transaction.getSession());
        cotisationManager.find(transaction);
        for (int h = 0; h < cotisationManager.size(); h++) {
            AFCotisation cotisation = (AFCotisation) cotisationManager.getEntity(h);
            if (changementPeriodicite == true) {
                if (affiliation.getPeriodicite().equals(CodeSystem.PERIODICITE_ANNUELLE) == false) {
                    cotisation.setPeriodicite(CodeSystem.PERIODICITE_MENSUELLE);
                } else {
                    cotisation.setTraitementMoisAnnee("8370" + personne.getDateRentier().getMonth());
                }
            }
            cotisation.setDateFin(personne.getDateRentier().toStr("."));
            cotisation.setMotifFin(CodeSystem.MOTIF_FIN_RETRAITE);
            cotisation.setSession(getSession);
            cotisation.update();
        }
    }
}
