package globaz.phenix.listes.excel;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JATime;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAPassage;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.phenix.db.principale.CPCotisation;
import globaz.phenix.db.principale.CPDecisionAffiliationTiers;
import globaz.phenix.db.principale.CPDecisionAffiliationTiersManager;
import globaz.phenix.db.principale.CPDonneesCalcul;
import globaz.phenix.util.CPProperties;
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
import org.apache.poi.hssf.util.HSSFColor;

/**
 * @author: ado
 */
public class CPListeDecisions {
    private CPDecisionAffiliationTiersManager manager = null;
    private FAPassage myPassage;
    private BProcess processAppelant = null;
    private BSession session = null;
    private HSSFSheet sheet;
    private HSSFWorkbook wb;

    public CPListeDecisions() {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet("TENT");
        sheet.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet.getPrintSetup().setLandscape(true);

        // marges en-tête/pied de page
        sheet.getPrintSetup().setHeaderMargin(0);
        sheet.getPrintSetup().setFooterMargin(0);
    }

    public CPListeDecisions(BSession session, String idPassage, CPDecisionAffiliationTiersManager manager1) {
        manager = manager1;
        this.session = session;
        wb = new HSSFWorkbook();
        sheet = wb.createSheet(getSession().getLabel("PAGE") + " 1");
        try {
            myPassage = new FAPassage();
            myPassage.setSession(getSession());
            myPassage.setIdPassage(idPassage);
            myPassage.retrieve();
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
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
            if (JadeStringUtil.isBlankOrZero(manager.getFromDateFacturation())) {
                f = File.createTempFile(getSession().getLabel("LISTDECISION") + "_" + JACalendar.today().toStrAMJ()
                        + "_", ".xls");
            } else {
                f = File.createTempFile(getSession().getLabel("LISTDECISIONPERIODE"), ".xls");
            }
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

    public HSSFSheet populateSheet(CPDecisionAffiliationTiersManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = null;
        CPDecisionAffiliationTiers decisionAffiliationTiers = null;
        processAppelant.setProgressScaleValue(manager.getCount(transaction));
        statement = manager.cursorOpen(transaction);

        HSSFFont fontInactive = wb.createFont();
        fontInactive.setColor(HSSFColor.GREY_50_PERCENT.index);
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFCellStyle styleInac = wb.createCellStyle();
        styleInac.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleInac.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleInac.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleInac.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleInac.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleInac.setFont(fontInactive);
        HSSFCellStyle style1 = wb.createCellStyle();
        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        HSSFCellStyle style1Inac = wb.createCellStyle();
        style1Inac.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style1Inac.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style1Inac.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style1Inac.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style1Inac.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style1Inac.setFont(fontInactive);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style2.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
        HSSFCellStyle style2Inac = wb.createCellStyle();
        style2Inac.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2Inac.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2Inac.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2Inac.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2Inac.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style2Inac.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
        style2Inac.setFont(fontInactive);

        if (!JadeStringUtil.isEmpty(this.manager.getFromDateFacturation())) {
            sheet.setColumnWidth((short) 0, (short) 4200);
            sheet.setColumnWidth((short) 1, (short) 4200);
            sheet.setColumnWidth((short) 2, (short) 8000);
            sheet.setColumnWidth((short) 3, (short) 2000);
            sheet.setColumnWidth((short) 4, (short) 3000);
            sheet.setColumnWidth((short) 5, (short) 4000);
            sheet.setColumnWidth((short) 6, (short) 1800);
            sheet.setColumnWidth((short) 7, (short) 3800);
            sheet.setColumnWidth((short) 8, (short) 3400);
            sheet.setColumnWidth((short) 9, (short) 3400);
            sheet.setColumnWidth((short) 10, (short) 3400);
            sheet.setColumnWidth((short) 11, (short) 4000);
            sheet.setColumnWidth((short) 12, (short) 2000);
        } else {
            sheet.setColumnWidth((short) 0, (short) 4200);
            sheet.setColumnWidth((short) 1, (short) 8000);
            sheet.setColumnWidth((short) 2, (short) 2000);
            sheet.setColumnWidth((short) 3, (short) 3000);
            sheet.setColumnWidth((short) 4, (short) 4000);
            sheet.setColumnWidth((short) 5, (short) 1800);
            sheet.setColumnWidth((short) 6, (short) 3800);
            sheet.setColumnWidth((short) 7, (short) 3400);
            sheet.setColumnWidth((short) 8, (short) 3400);
            sheet.setColumnWidth((short) 9, (short) 3400);
            sheet.setColumnWidth((short) 10, (short) 4000);

        }
        while (((decisionAffiliationTiers = (CPDecisionAffiliationTiers) manager.cursorReadNext(statement)) != null)
                && (!decisionAffiliationTiers.isNew())) {

            // /////////
            HSSFRow row = sheet.createRow(sheet.getPhysicalNumberOfRows() + 1);
            int colNum = 0;
            // numéro affilié
            HSSFCell cell = row.createCell((short) colNum++);
            if (decisionAffiliationTiers.getActive().equals(Boolean.TRUE)) {
                cell.setCellStyle(style);
            } else {
                cell.setCellStyle(styleInac);
            }
            cell.setCellValue(new HSSFRichTextString(decisionAffiliationTiers.getNumAffilie()));
            // NSS pour liste comptabilisée
            if (!JadeStringUtil.isEmpty(this.manager.getFromDateFacturation())) {
                cell = row.createCell((short) colNum++);
                if (decisionAffiliationTiers.getActive().equals(Boolean.TRUE)) {
                    cell.setCellStyle(style1);
                } else {
                    cell.setCellStyle(style1Inac);
                }
                cell.setCellValue(new HSSFRichTextString(decisionAffiliationTiers.getNumAvsActuel()));
            }

            // prénom et nom
            cell = row.createCell((short) colNum++);
            if (decisionAffiliationTiers.getActive().equals(Boolean.TRUE)) {
                cell.setCellStyle(style1);
            } else {
                cell.setCellStyle(style1Inac);
            }
            cell.setCellValue(new HSSFRichTextString(decisionAffiliationTiers.getDesignation1() + " "
                    + decisionAffiliationTiers.getDesignation2()));
            // Année
            cell = row.createCell((short) colNum++);
            if (decisionAffiliationTiers.getActive().equals(Boolean.TRUE)) {
                cell.setCellStyle(style);
            } else {
                cell.setCellStyle(styleInac);
            }
            cell.setCellValue(new HSSFRichTextString(decisionAffiliationTiers.getAnneeDecision()));
            // Période
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            if (decisionAffiliationTiers.getActive().equals(Boolean.TRUE)) {
                cell.setCellStyle(style);
            } else {
                cell.setCellStyle(styleInac);
            }
            String debutDec = "";
            if (decisionAffiliationTiers.getDebutDecision().length() > 5) {
                debutDec = decisionAffiliationTiers.getDebutDecision().substring(0, 5);
            }
            String finDec = "";
            if (decisionAffiliationTiers.getFinDecision().length() > 5) {
                finDec = decisionAffiliationTiers.getFinDecision().substring(0, 5);
            }
            cell.setCellValue(new HSSFRichTextString(debutDec + " - " + finDec));
            // Type de décision
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style1);
            if (decisionAffiliationTiers.getActive().equals(Boolean.TRUE)) {
                cell.setCellStyle(style1);
            } else {
                cell.setCellStyle(style1Inac);
            }
            cell.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(),
                    decisionAffiliationTiers.getTypeDecision())));
            // Genre
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style1);
            if (decisionAffiliationTiers.getActive().equals(Boolean.TRUE)) {
                cell.setCellStyle(style1);
            } else {
                cell.setCellStyle(style1Inac);
            }
            cell.setCellValue(new HSSFRichTextString(CodeSystem.getCode(getSession(),
                    decisionAffiliationTiers.getGenreAffilie())));
            // Fortune ou revenu déterminant
            String montant = "";
            CPDonneesCalcul donnee = new CPDonneesCalcul();
            donnee.setSession(getSession());
            if (decisionAffiliationTiers.isNonActif()) {
                montant = donnee
                        .getMontant(decisionAffiliationTiers.getIdDecision(), CPDonneesCalcul.CS_FORTUNE_TOTALE);
            } else {
                montant = donnee.getMontant(decisionAffiliationTiers.getIdDecision(), CPDonneesCalcul.CS_REV_NET);
            }
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            if (decisionAffiliationTiers.getActive().equals(Boolean.TRUE)) {
                cell.setCellStyle(style2);
            } else {
                cell.setCellStyle(style2Inac);
            }
            cell.setCellValue(new HSSFRichTextString(montant));
            // Revenu Ci
            montant = donnee.getMontant(decisionAffiliationTiers.getIdDecision(), CPDonneesCalcul.CS_REV_CI);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            if (decisionAffiliationTiers.getActive().equals(Boolean.TRUE)) {
                cell.setCellStyle(style2);
            } else {
                cell.setCellStyle(style2Inac);
            }
            cell.setCellValue(new HSSFRichTextString(montant));
            // Cotisation annuelle
            CPCotisation coti = new CPCotisation();
            coti.setSession(getSession());
            coti.setAlternateKey(2);
            coti.setIdDecision(decisionAffiliationTiers.getIdDecision());
            coti.setGenreCotisation("812001");
            coti.retrieve();
            if (coti.isNew()) {
                montant = "";
            } else {
                montant = coti.getMontantAnnuel();
            }
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            if (decisionAffiliationTiers.getActive().equals(Boolean.TRUE)) {
                cell.setCellStyle(style2);
            } else {
                cell.setCellStyle(style2Inac);
            }
            cell.setCellValue(new HSSFRichTextString(montant));
            // N° de caisse AF pour liste comptabilisée
            if (!JadeStringUtil.isEmpty(this.manager.getFromDateFacturation())) {
                cell = row.createCell((short) colNum++);
                if (decisionAffiliationTiers.getActive().equals(Boolean.TRUE)) {
                    cell.setCellStyle(style1);
                } else {
                    cell.setCellStyle(style1Inac);
                }
                AFSuiviCaisseAffiliation caisse = AFAffiliationUtil.getCaisseExterne(
                        AFAffiliationUtil.getAffiliation(decisionAffiliationTiers.getIdAffiliation(), getSession()),
                        decisionAffiliationTiers.getDebutDecision(),
                        CPProperties.LISTE_DECISIONS_COMPTABILISEES_TYPE_SUIVI_AF.getValue());
                if (caisse != null) {
                    if (caisse.getAdministration() != null) {
                        cell.setCellValue(new HSSFRichTextString(caisse.getAdministration().getCodeAdministration()));
                    }
                }
            }
            processAppelant.incProgressCounter();
        }
        manager.cursorClose(statement);
        statement = null;
        HSSFFooter footer = sheet.getFooter();
        if (JadeStringUtil.isEmpty(manager.getFromDateFacturation())) {
            // Liste décision
            footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8)
                    + "Ref: 0081CCP");
        } else {
            // Décision pour une période donnée
            footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8)
                    + "Ref: 0267CCP");
        }
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

    private HSSFSheet setTitleRow(HSSFWorkbook wb, HSSFSheet sheet) {
        final String nom = getSession().getLabel("NOM_PRENOM");
        final String num = getSession().getLabel("NUM_AFFILIE");
        final String nss = getSession().getLabel("NUM_AVS");
        final String annee = getSession().getLabel("ANNEE");
        final String periodeDecision = getSession().getLabel("PERIODE");
        final String typeDecision = getSession().getLabel("TYPE_DECISION");
        final String genre = getSession().getLabel("GENRE_AFFILIE");
        final String fortuneRevenuDet = getSession().getLabel("FORTUNE_DET");
        final String revenuCi = getSession().getLabel("REVENU_CI");
        final String cotiAnnuelle = getSession().getLabel("COTISATION_ANNUELLE");
        final String caisseAf = getSession().getLabel("NUM_CAISSE_AF");
        // final String collaborateur = getSession().getLabel("COLLABORATEUR");
        final String[] COL_TITLES = { num, nom, annee, periodeDecision, typeDecision, genre, fortuneRevenuDet,
                revenuCi, cotiAnnuelle };
        final String[] COL_TITLES1 = { num, nss, nom, annee, periodeDecision, typeDecision, genre, fortuneRevenuDet,
                revenuCi, cotiAnnuelle, caisseAf };

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

        HSSFCellStyle style2Inac = this.wb.createCellStyle();
        style2Inac.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2Inac.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2Inac.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2Inac.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2Inac.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

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
        if (JadeStringUtil.isBlankOrZero(manager.getFromDateFacturation())) {
            c.setCellValue(new HSSFRichTextString(session.getLabel("LISTDECISION")));
        } else {
            c.setCellValue(new HSSFRichTextString(session.getLabel("LISTDECISIONPERIODE")));
        }

        row = sheet.createRow(3);
        // création de l'entête
        try {
            row = sheet.createRow(4);
            if (JadeStringUtil.isBlankOrZero(manager.getFromDateFacturation())) { // Décisions d'un passage
                c = row.createCell((short) 0);
                c.setCellStyle(style2);
                c.setCellValue(new HSSFRichTextString(session.getLabel("PASSAGE")));
                c = row.createCell((short) 1);
                c.setCellStyle(style3);
                c.setCellValue(new HSSFRichTextString(myPassage.getIdPassage() + "   " + myPassage.getLibelle()));
            } else { // Décision pour une période donnée
                c = row.createCell((short) 0);
                c.setCellValue(new HSSFRichTextString(session.getLabel("PERIODE")));
                c.setCellStyle(style2);
                c = row.createCell((short) 1);
                c.setCellStyle(style3);
                c.setCellValue(new HSSFRichTextString(manager.getFromDateFacturation() + " - "
                        + manager.getToDateFacturation()));
            }
            row = sheet.createRow(5);
            if (!JadeStringUtil.isBlankOrZero(manager.getFromDateFacturation())) {
                // Décisions pour une période donnée
                c = row.createCell((short) 0);
                c.setCellStyle(style2);
                c.setCellValue(new HSSFRichTextString(session.getLabel("LISTE_AFF_DEB")));
                c = row.createCell((short) 1);
                c.setCellStyle(style3);
                c.setCellValue(new HSSFRichTextString(manager.getFromAffilie()));
                c = row.createCell((short) 3);
                c.setCellStyle(style2);
                c.setCellValue(new HSSFRichTextString(session.getLabel("LISTE_AFF_FIN")));
                c = row.createCell((short) 5);
                c.setCellStyle(style3);
                c.setCellValue(new HSSFRichTextString(manager.getToNumAffilie()));
            }
            //
            row = sheet.createRow(6);
            c = row.createCell((short) 0);
            c.setCellStyle(style2);
            c.setCellValue(new HSSFRichTextString(session.getLabel("TYPE_DECISION")));
            c = row.createCell((short) 1);
            c.setCellStyle(style3);
            if ((manager.getForTypeDecision() != null) && !JadeStringUtil.isEmpty(manager.getForTypeDecision())) {
                c.setCellValue(new HSSFRichTextString(globaz.phenix.translation.CodeSystem.getLibelle(getSession(),
                        manager.getForTypeDecision())));
            }
            c = row.createCell((short) 3);
            c.setCellStyle(style2);
            c.setCellValue(new HSSFRichTextString(getSession().getLabel("GENRE_DECISION")));
            c = row.createCell((short) 5);
            c.setCellStyle(style3);
            String lib = "";
            if ((manager.getForGenreAffilie() != null) && !JadeStringUtil.isEmpty(manager.getForGenreAffilie())) {
                lib = globaz.phenix.translation.CodeSystem.getLibelle(getSession(), manager.getForGenreAffilie());
            }
            c.setCellValue(new HSSFRichTextString(lib));

            //
            row = sheet.createRow(7);
            c = row.createCell((short) 0);
            c.setCellStyle(style2);
            c.setCellValue(new HSSFRichTextString(getSession().getLabel("ANNEE")));
            c = row.createCell((short) 1);
            c.setCellStyle(style3);
            c.setCellValue(new HSSFRichTextString(manager.getForAnneeDecision()));
            if (!JadeStringUtil.isBlankOrZero(manager.getFromDateFacturation())) { // Décisions pour une période
                String texte = "";
                if (manager.getIsActive().equals(Boolean.TRUE)) {
                    texte = getSession().getLabel("OUI");
                } else {
                    texte = getSession().getLabel("NON");
                }
                c = row.createCell((short) 3);
                c.setCellStyle(style2);
                c.setCellValue(new HSSFRichTextString(getSession().getLabel("ACTIVE")));
                c = row.createCell((short) 5);
                c.setCellStyle(style3);
                c.setCellValue(new HSSFRichTextString(texte));
            }
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
        row = sheet.createRow(9);
        int lengthCol = COL_TITLES.length;
        if (!JadeStringUtil.isEmpty(manager.getFromDateFacturation())) {
            lengthCol = COL_TITLES1.length;
        }
        for (int i = 0; i < lengthCol; i++) {
            c = row.createCell((short) i);
            if (!JadeStringUtil.isEmpty(manager.getFromDateFacturation())) {
                c.setCellValue(new HSSFRichTextString(COL_TITLES1[i]));
            } else {
                c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
            }
            c.setCellStyle(style);
        }
        return sheet;
    }
}
