/*
 * Créé le 13 févr. 07
 */
package globaz.naos.itext.controleEmployeur;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.controleEmployeur.AFControlesAttribues;
import globaz.naos.db.controleEmployeur.AFControlesAttribuesManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.CommonProperties;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author hpe
 * 
 */
public class AFListeControlesAttribuesExcel {

    private String annee = new String();
    private String annee_1 = new String();
    private String annee_2 = new String();
    private String annee_3 = new String();
    private String annee_4 = new String();
    private String annee_5 = new String();

    private String date_impression = new String();
    private String genreControle = new String();
    private BProcess processAppelant = null;
    private BSession session;
    private HSSFSheet sheet1;
    private String user = new String();
    private String visaReviseur = new String();
    private HSSFWorkbook wb;

    // créé la feuille xls
    public AFListeControlesAttribuesExcel(BSession session) {
        this.session = session;
        wb = new HSSFWorkbook();

        setDate_impression(JACalendar.today().getDay() + "." + JACalendar.today().getMonth() + "."
                + JACalendar.today().getYear());
    }

    /**
     * @return
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * @return
     */
    public String getAnnee_1() {

        int Annee = Integer.parseInt(annee);
        annee_1 = String.valueOf(Annee - 1);

        return annee_1;
    }

    /**
     * @return
     */
    public String getAnnee_2() {

        int Annee = Integer.parseInt(annee);
        annee_2 = String.valueOf(Annee - 2);

        return annee_2;
    }

    /**
     * @return
     */
    public String getAnnee_3() {

        int Annee = Integer.parseInt(annee);
        annee_3 = String.valueOf(Annee - 3);

        return annee_3;
    }

    /**
     * @return
     */
    public String getAnnee_4() {

        int Annee = Integer.parseInt(annee);
        annee_4 = String.valueOf(Annee - 4);

        return annee_4;
    }

    /**
     * @return
     */
    public String getAnnee_5() {

        int Annee = Integer.parseInt(annee);
        annee_5 = String.valueOf(Annee - 5);

        return annee_5;
    }

    /**
     * @return
     */
    public String getDate_impression() {
        return date_impression;
    }

    /**
     * @return
     */
    public String getGenreControle() {
        return genreControle;
    }

    /*
     * méthode pour gérer le fichier en sortie
     */
    public String getOutputFile() {
        try {
            File f = File.createTempFile(getSession().getLabel("NAOS_FICHIER_CONTROLES_ATTRIBUES") + " - "
                    + getDate_impression() + " - ", ".xls");
            f.deleteOnExit();
            FileOutputStream out = new FileOutputStream(f);
            wb.write(out);
            out.close();
            return f.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return "";
        }
    }

    /**
     * @return
     */
    public BProcess getProcessAppelant() {
        return processAppelant;
    }

    /**
     * @return
     */
    public BSession getSession() {
        return session;
    }

    /**
     * @return
     */
    public String getUser() {
        return user;
    }

    /**
     * @return
     */
    public String getVisaReviseur() {
        return visaReviseur;
    }

    /**
     * initialisation de la feuille xls
     */
    public HSSFSheet populateSheetListe(AFControlesAttribuesManager manager, BTransaction transaction) throws Exception {
        BStatement statement = null;
        AFControlesAttribues controlesAttr = null;

        // style
        HSSFCellStyle styleAlignLeft = setStyleAlignLeft();
        HSSFCellStyle styleAlignLeftWrap = setStyleAlignLeftWrap();
        HSSFCellStyle styleAlignCenter = setStyleAlignCenter();
        HSSFCellStyle styleAlignRight = setStyleAlignRight();

        // création de la page
        setSheetListe();

        statement = manager.cursorOpen(transaction);

        int debutTableauDonnes = 10;

        // parcours du manager et remplissage des cell
        while (((controlesAttr = (AFControlesAttribues) manager.cursorReadNext(statement)) != null)
                && (!controlesAttr.isNew())) {
            if (controlesAttr != null) {
                HSSFRow row = sheet1.createRow(debutTableauDonnes++);
                HSSFCellStyle myStyle = wb.createCellStyle();
                HSSFCell cell;

                // Reprise des données de l'affilié pour remplissage des
                // cellules
                AFAffiliation affilie = new AFAffiliation();
                affilie.setId(controlesAttr.getIdAffilie());
                affilie.setIdTiers(controlesAttr.getIdTiers());
                affilie.setSession(getSession());
                affilie.retrieve();
                // Reprise des données du tiers
                TITiersViewBean tiers = affilie.getTiers();

                // N° Affilié + nom
                cell = row.createCell((short) 0);
                cell.setCellValue(affilie.getAffilieNumero() + " - " + tiers.getNom());
                myStyle = styleAlignLeftWrap;
                cell.setCellStyle(myStyle);

                // Localité
                cell = row.createCell((short) 1);
                cell.setCellValue(tiers.getLocalite());
                myStyle = styleAlignLeftWrap;
                cell.setCellStyle(myStyle);

                // Période affiliation
                cell = row.createCell((short) 2);
                cell.setCellValue(affilie.getDateDebut() + " - " + affilie.getDateFin());
                myStyle = styleAlignLeftWrap;
                cell.setCellStyle(myStyle);

                // Temps effectué
                cell = row.createCell((short) 3);
                cell.setCellValue(controlesAttr.getTempsJour());
                myStyle = styleAlignRight;
                cell.setCellStyle(myStyle);

                // CI année -1 (nombre)
                cell = row.createCell((short) 4);
                cell.setCellValue(controlesAttr.getNbInscCI());
                myStyle = styleAlignRight;
                cell.setCellStyle(myStyle);

                // Dernier contrôles
                cell = row.createCell((short) 5);
                cell.setCellValue(controlesAttr.getDatePrecControle());
                myStyle = styleAlignLeftWrap;
                cell.setCellStyle(myStyle);

                // Commentaires
                cell = row.createCell((short) 6);
                cell.setCellValue(" ");
                myStyle = styleAlignLeftWrap;
                cell.setCellStyle(myStyle);

                // Masse sur 5 ans -1
                cell = row.createCell((short) 7);
                cell.setCellValue(controlesAttr.getMontantMasse_1());
                myStyle = styleAlignRight;
                cell.setCellStyle(myStyle);

                // Masse sur 5 ans -2
                cell = row.createCell((short) 8);
                cell.setCellValue(controlesAttr.getMontantMasse_2());
                myStyle = styleAlignRight;
                cell.setCellStyle(myStyle);

                // Masse sur 5 ans -3
                cell = row.createCell((short) 9);
                cell.setCellValue(controlesAttr.getMontantMasse_3());
                myStyle = styleAlignRight;
                cell.setCellStyle(myStyle);

                // Masse sur 5 ans -4
                cell = row.createCell((short) 10);
                cell.setCellValue(controlesAttr.getMontantMasse_4());
                myStyle = styleAlignRight;
                cell.setCellStyle(myStyle);

                // Masse sur 5 ans -5
                cell = row.createCell((short) 11);
                cell.setCellValue(controlesAttr.getMontantMasse_5());
                myStyle = styleAlignRight;
                cell.setCellStyle(myStyle);

                // Réviseur
                cell = row.createCell((short) 12);
                cell.setCellValue(controlesAttr.getVisaReviseur());
                myStyle = styleAlignLeftWrap;
                cell.setCellStyle(myStyle);

                processAppelant.incProgressCounter();

            }
        }
        return sheet1;
    }

    /**
     * @param string
     */
    public void setAnnee(String string) {
        annee = string;
    }

    /**
     * @param string
     */
    public void setDate_impression(String string) {
        date_impression = string;
    }

    /**
     * défini une police Arial gras
     * 
     * @param wb
     * @return
     */
    private HSSFFont setFontBold(HSSFWorkbook wb) {
        HSSFFont fontBold = wb.createFont();
        fontBold.setFontName(HSSFFont.FONT_ARIAL);
        fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        return fontBold;
    }

    /**
     * @param string
     */
    public void setGenreControle(String string) {
        genreControle = string;
    }

    /**
     * @param process
     */
    public void setProcessAppelant(BProcess process) {
        processAppelant = process;
    }

    /**
     * @param session
     */
    public void setSession(BSession session) {
        this.session = session;
    }

    /**
     * Création de la page
     * 
     * @return
     */
    private void setSheetListe() {
        sheet1 = wb.createSheet(getSession().getLabel("NAOS_FICHIER_CONTROLES_ATTRIBUES"));
        sheet1 = setTitleRowListe(wb, sheet1);

        // marges (unité en pouces)
        sheet1.setMargin(HSSFSheet.LeftMargin, 0);
        sheet1.setMargin(HSSFSheet.RightMargin, 0);
        sheet1.setMargin(HSSFSheet.TopMargin, 0.6);
        sheet1.setMargin(HSSFSheet.BottomMargin, 0.6);

        HSSFPrintSetup ps = sheet1.getPrintSetup();

        // format
        ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);

        // orientation
        ps.setLandscape(true);

        // marges en-tête/pied de page
        ps.setHeaderMargin(0);
        ps.setFooterMargin(0);

        // en-tête
        HSSFHeader header = sheet1.getHeader();

        // pied de page
        HSSFFooter footer = sheet1.getFooter();
        footer.setRight("Page " + HSSFFooter.page() + "/" + HSSFFooter.numPages());
        footer.setLeft(this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.') + 1) + " - "
                + getDate_impression() + " - " + getSession().getUserName());

        // définition de la taille des cell
        sheet1.setColumnWidth((short) 0, (short) 10000);
        sheet1.setColumnWidth((short) 1, (short) 6500);
        sheet1.setColumnWidth((short) 2, (short) 6500);
        sheet1.setColumnWidth((short) 3, (short) 6500);
        sheet1.setColumnWidth((short) 4, (short) 6500);
        sheet1.setColumnWidth((short) 5, (short) 6500);
        sheet1.setColumnWidth((short) 6, (short) 6500);
        sheet1.setColumnWidth((short) 7, (short) 6500);
        sheet1.setColumnWidth((short) 8, (short) 6500);
        sheet1.setColumnWidth((short) 9, (short) 6500);
        sheet1.setColumnWidth((short) 10, (short) 6500);
        sheet1.setColumnWidth((short) 11, (short) 6500);
        sheet1.setColumnWidth((short) 12, (short) 6500);
        sheet1.setColumnWidth((short) 13, (short) 6500);
        sheet1.setColumnWidth((short) 14, (short) 6500);
        sheet1.setColumnWidth((short) 15, (short) 6500);

    }

    /**
     * alignement au centre
     * 
     * @return
     */
    private HSSFCellStyle setStyleAlignCenter() {
        HSSFCellStyle styleAlignCenter = wb.createCellStyle();
        styleAlignCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleAlignCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleAlignCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleAlignCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleAlignCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        return styleAlignCenter;
    }

    /**
     * alignement à gauche avec
     * 
     * @return
     */
    private HSSFCellStyle setStyleAlignLeft() {
        HSSFCellStyle styleAlignLeft = wb.createCellStyle();
        styleAlignLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleAlignLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleAlignLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleAlignLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleAlignLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        return styleAlignLeft;
    }

    /**
     * alignement à gauche avec WrapText
     * 
     * @return
     */
    private HSSFCellStyle setStyleAlignLeftWrap() {
        HSSFCellStyle styleAlignLeftWrap = wb.createCellStyle();
        styleAlignLeftWrap.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleAlignLeftWrap.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleAlignLeftWrap.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleAlignLeftWrap.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleAlignLeftWrap.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        styleAlignLeftWrap.setWrapText(true);
        return styleAlignLeftWrap;
    }

    /**
     * alignement à droite
     * 
     * @return
     */
    private HSSFCellStyle setStyleAlignRight() {
        HSSFCellStyle styleAlignRight = wb.createCellStyle();
        styleAlignRight.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleAlignRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleAlignRight.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleAlignRight.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleAlignRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        return styleAlignRight;
    }

    /**
     * défini le style des titres des colonnes
     * 
     * @param wb
     * @param fontBold
     * @return
     */
    private HSSFCellStyle setStyleTitreCol(HSSFWorkbook wb, HSSFFont fontBold) {
        HSSFCellStyle styleTitreCol = wb.createCellStyle();
        styleTitreCol.setFont(fontBold);
        styleTitreCol.setWrapText(true);
        styleTitreCol.setFont(fontBold);
        styleTitreCol.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleTitreCol.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        styleTitreCol.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        styleTitreCol.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
        styleTitreCol.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        return styleTitreCol;
    }

    /**
     * défini le style des titres des critères de sélection
     * 
     * @param wb
     * @param fontBold
     * @return
     */
    private HSSFCellStyle setStyleTitreCritere(HSSFWorkbook wb, HSSFFont fontBold) {
        HSSFCellStyle styleTitreCritere = wb.createCellStyle();
        styleTitreCritere.setFont(fontBold);
        styleTitreCritere.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        return styleTitreCritere;
    }

    /**
     * méthode pour la création du style de la feuille" (entêtes, des bordures...)
     */
    private HSSFSheet setTitleRowListe(HSSFWorkbook wb, HSSFSheet sheet) {
        int compteurCritere = 0;

        final String numAffilieEtNom = getSession().getLabel("NAOS_COLONNE_NOAFFILIENOM");
        final String localite = getSession().getLabel("NAOS_COLONNE_LOCALITE");
        final String periodeAffiliation = getSession().getLabel("NAOS_COLONNE_PERIODEAFF");
        final String tempsEffectue = getSession().getLabel("NAOS_COLONNE_TEMPS");
        final String ciAnnee = getSession().getLabel("NAOS_COLONNE_NBCI") + " " + getAnnee_1();
        final String dernierControle = getSession().getLabel("NAOS_COLONNE_DERCONTROLE");
        final String commentaires = getSession().getLabel("NAOS_COLONNE_COMMENTAIRES");
        final String masseMontant1 = getSession().getLabel("NAOS_COLONNE_MASSE") + " " + getAnnee_1();
        final String masseMontant2 = getSession().getLabel("NAOS_COLONNE_MASSE") + " " + getAnnee_2();
        final String masseMontant3 = getSession().getLabel("NAOS_COLONNE_MASSE") + " " + getAnnee_3();
        final String masseMontant4 = getSession().getLabel("NAOS_COLONNE_MASSE") + " " + getAnnee_4();
        final String masseMontant5 = getSession().getLabel("NAOS_COLONNE_MASSE") + " " + getAnnee_5();
        final String reviseur = getSession().getLabel("NAOS_COLONNE_REVISEUR");

        final String[] COL_TITLES = { numAffilieEtNom, localite, periodeAffiliation, tempsEffectue, ciAnnee,
                dernierControle, commentaires, masseMontant1, masseMontant2, masseMontant3, masseMontant4,
                masseMontant5, reviseur };

        HSSFRow row = null;
        HSSFCell c;

        // création du style pour le titre de la page
        HSSFFont font2 = wb.createFont();
        font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font2.setFontHeight((short) 300);
        font2.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font2);

        row = sheet.createRow(1);
        c = row.createCell((short) 0);
        c.setCellValue(getSession().getLabel("NAOS_EMAIL_OBJECT_CONTROLES_ATTRIBUES"));
        c.setCellStyle(style2);
        // critères de sélection
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font3.setFontHeight((short) 200);
        style3.setFont(font3);
        // row = sheet.createRow(3);
        row = sheet.createRow(4);
        // création de l'entête
        try {
            row = sheet.createRow(3);
            c = row.createCell((short) 0);
            c.setCellValue("");
            c.setCellStyle(style3);
            c = row.createCell((short) 0);
            c.setCellValue(session.getLabel("DATE_IMPRESSION"));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(getDate_impression());
            c.setCellStyle(style3);
            row = sheet.createRow(4);
            c = row.createCell((short) 0);
            c.setCellValue("");
            c.setCellStyle(style3);
            c = row.createCell((short) 0);
            c.setCellValue(getSession().getLabel("NAOS_CONTROLES_NOCAISSE"));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(getSession().getApplication().getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE));
            c.setCellStyle(style3);
            row = sheet.createRow(5);
            c = row.createCell((short) 0);
            c.setCellValue("");
            c.setCellStyle(style3);
            c = row.createCell((short) 0);
            c.setCellValue(getSession().getLabel("NAOS_CONTROLES_TYPE"));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(getSession().getCodeLibelle(getGenreControle()));
            c.setCellStyle(style3);
            row = sheet.createRow(6);
            c = row.createCell((short) 0);
            c.setCellValue("");
            c.setCellStyle(style3);
            c = row.createCell((short) 0);
            c.setCellValue(getSession().getLabel("NAOS_CONTROLES_ANNEE"));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(getAnnee());
            c.setCellStyle(style3);

        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }

        // let's use a nifty font for the title
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);

        row = sheet.createRow(9);

        HSSFFont fontBold = setFontBold(wb);

        HSSFCellStyle styleTitreCol = setStyleTitreCol(wb, fontBold);

        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(COL_TITLES[i]);
            c.setCellStyle(styleTitreCol);
        }
        return sheet;
    }

    /**
     * @param string
     */
    public void setUser(String string) {
        user = string;
    }

    /**
     * @param string
     */
    public void setVisaReviseur(String string) {
        visaReviseur = string;
    }

}
