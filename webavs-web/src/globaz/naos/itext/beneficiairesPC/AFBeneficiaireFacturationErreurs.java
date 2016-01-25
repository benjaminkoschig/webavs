/**
 * class CPImpressionCotisationDifferente écrit le 19/01/05 par JPA
 * 
 * class créant un document .xls des décisions avec mise en compte
 * 
 * @author JPA
 */
package globaz.naos.itext.beneficiairesPC;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.beneficiairepc.AFQuittance;
import globaz.naos.db.beneficiairepc.AFQuittanceManager;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/*
 * @param ForidPassage idPassage utilisé pour ajouter l'idpassage et le libelle dans le sujet de l'e-mail @param langue
 * langue de l'ut afin d'afficher les entêtes .xls dans la langue correspondante
 */
public class AFBeneficiaireFacturationErreurs {
    private String idPassage;
    private BProcess processAppelant = null;
    private BSession session;
    private HSSFSheet sheet1;
    private HSSFWorkbook wb;

    public AFBeneficiaireFacturationErreurs() {
        wb = new HSSFWorkbook();
        sheet1 = wb.createSheet("");

    }

    // créé la feuille xls
    public AFBeneficiaireFacturationErreurs(BSession session) {
        this.session = session;
        wb = new HSSFWorkbook();
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
            File f = File.createTempFile("FacturationPCG" + " - " + JACalendar.todayJJsMMsAAAA() + "  ", ".xls");
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
    public HSSFSheet populateSheet(AFQuittanceManager manager, BTransaction transaction) throws Exception {
        BStatement statement = null;
        AFQuittance quittance = null;
        // Pour information: indique le nombre d'annonces à charger
        // processAppelant.setProgressScaleValue(manager.getCount());
        /*
         * définition du style et mise en place du titre ,des entêtes, des bordures...
         */
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        sheet1 = wb.createSheet("test");
        sheet1 = setTitleRowMiseCompte(wb, sheet1, manager);
        // dfinition de la taille des cell
        sheet1.setDefaultColumnWidth((short) 30);
        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((quittance = (AFQuittance) manager.cursorReadNext(statement)) != null) && (!quittance.isNew())) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            if (quittance != null) {
                if (!JadeStringUtil.isEmpty(quittance.getMessageErreur())) {
                    HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows());
                    int colNum = 0;
                    // numAffilié
                    HSSFCell cell = row.createCell((short) colNum++);
                    cell.setCellValue(new HSSFRichTextString(quittance.getNumAffilieBeneficiaire(getSession())));
                    cell.setCellStyle(style);
                    // Nom, Prénom
                    cell = row.createCell((short) colNum++);
                    cell.setCellValue(new HSSFRichTextString(quittance.getNomPrenomBeneficiaire()));
                    cell.setCellStyle(style);

                    // numAvsAideMenage
                    /*
                     * cell = row.createCell((short) colNum++); cell.setCellValue(quittance.getNumAvsAideMenage());
                     * cell.setCellStyle(style); // periodeDebut cell = row.createCell((short) colNum++);
                     * cell.setCellValue(quittance.getPeriodeDebut()); cell.setCellStyle(style); // periodeFin cell =
                     * row.createCell((short) colNum++); cell.setCellValue(quittance.getPeriodeFin());
                     * cell.setCellStyle(style); // Nombre heures cell = row.createCell((short) colNum++);
                     * cell.setCellValue(quittance.getNombreHeures()); cell.setCellStyle(style); // Prix heure cell =
                     * row.createCell((short) colNum++); cell.setCellValue(quittance.getPrixHeure());
                     * cell.setCellStyle(style); // Total cell = row.createCell((short) colNum++);
                     * cell.setCellValue(quittance.getTotalVerse()); cell.setCellStyle(style);
                     */
                    // Log
                    cell = row.createCell((short) colNum++);
                    cell.setCellValue(new HSSFRichTextString(quittance.getMessageErreur()));
                    cell.setCellStyle(style);
                }
            }
        }
        return sheet1;
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
    private HSSFSheet setTitleRowMiseCompte(HSSFWorkbook wb, HSSFSheet sheet, AFQuittanceManager manager) {
        sheet1.setColumnWidth((short) 0, (short) 3000);
        sheet1.setColumnWidth((short) 1, (short) 8000);
        sheet1.setColumnWidth((short) 2, (short) 30000);
        final String numAffilie = "Numéro Affilié";
        // final String numAvsAideMenage = "NSS Aide Ménage";
        // final String periodeDeb = "Période Début";
        // final String periodeFin = "Période Fin";
        // final String Nhe = "Nbre heures";
        // final String Phe = "Prix heure";
        // final String total = "Total";
        final String nomPrenom = "Nom, Prénom";
        final String log = "Erreur";
        final String titre = "Liste des erreurs : Facturation quittances PCG";

        final String[] COL_TITLES = { numAffilie, nomPrenom, /*
                                                              * numAvsAideMenage, periodeDeb, periodeFin, Nhe, Phe,
                                                              * total,
                                                              */log };
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
        c.setCellValue(new HSSFRichTextString(titre));
        c.setCellStyle(style2);
        row = sheet.createRow(3);
        row = sheet.createRow(4);
        // let's use a nifty font for the title
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
            c.setCellStyle(style);
        }
        return sheet;
    }
}