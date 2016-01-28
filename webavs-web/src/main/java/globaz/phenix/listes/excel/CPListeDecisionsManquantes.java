package globaz.phenix.listes.excel;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.cotisation.AFCotisationJAssuranceManager;
import globaz.naos.translation.CodeSystem;
import globaz.pavo.db.compte.CICompteIndividuelUtil;
import globaz.phenix.db.principale.CPDecisionListeDecisionsManquantesManager;
import globaz.phenix.db.principale.CPDecisionManager;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CPListeDecisionsManquantes {
    private String fromAnneeDecision = "";
    private BProcess processAppelant = null;
    private BSession session = null;
    private HSSFSheet sheet;
    private String toAnneeDecision = "";
    private HSSFWorkbook wb;

    public CPListeDecisionsManquantes() {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet("TENT");
        sheet.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet.getPrintSetup().setLandscape(true);
    }

    public CPListeDecisionsManquantes(BSession session, String fromAnnee, String toAnnee) {
        this.session = session;
        wb = new HSSFWorkbook();
        setFromAnneeDecision(fromAnnee);
        setToAnneeDecision(toAnnee);
        sheet = wb.createSheet(fromAnnee + " -" + toAnnee);
        sheet.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet.getPrintSetup().setLandscape(true);
        sheet = setTitleRow(wb, sheet);
    }

    /**
     * Returns the fromAnneeDecision.
     * 
     * @return String
     */
    public String getFromAnneeDecision() {
        return fromAnneeDecision;
    }

    public String getOutputFile() {
        try {
            File f = File.createTempFile(getSession().getLabel("LISTDECISIONMANQUANTES"), ".xls");
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

    /**
     * Returns the toAnneeDecision.
     * 
     * @return String
     */
    public String getToAnneeDecision() {
        return toAnneeDecision;
    }

    public HSSFSheet populateSheet(CPDecisionListeDecisionsManquantesManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = null;
        AFAffiliation affiliation = null;
        CICompteIndividuelUtil ci = new CICompteIndividuelUtil();
        ci.setSession(transaction.getSession());
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        processAppelant.setProgressScaleValue(manager.getCount(transaction));
        statement = manager.cursorOpen(transaction);
        long i = 0;
        sheet.setColumnWidth((short) 0, (short) 4000);
        sheet.setColumnWidth((short) 1, (short) 8000);
        sheet.setColumnWidth((short) 2, (short) 4000);
        sheet.setColumnWidth((short) 3, (short) 4000);
        sheet.setColumnWidth((short) 4, (short) 6000);
        sheet.setColumnWidth((short) 5, (short) 6000);
        sheet.setColumnWidth((short) 6, (short) 4000);
        while (((affiliation = (AFAffiliation) manager.cursorReadNext(statement)) != null) && (!affiliation.isNew())) {
            i++;
            CPDecisionManager decisionMng = new CPDecisionManager();
            decisionMng.setSession(getSession());
            decisionMng.setForIdAffiliation(affiliation.getAffiliationId());
            decisionMng.setForExceptEtatDecision(manager.getForExceptEtatDecision());
            decisionMng.orderByAnneeDecision();
            decisionMng.orderByDateDecision();
            decisionMng.orderByIdDecision();
            String anneesManquantes = "";
            int iFromAnnee = Integer.parseInt(fromAnneeDecision);
            int iToAnnee = Integer.parseInt(toAnneeDecision);
            // Si date début d'affiliation > à date de début => prendre année de
            // début de l'affiliation
            if (JACalendar.getYear(affiliation.getDateDebut()) > iFromAnnee) {
                iFromAnnee = JACalendar.getYear(affiliation.getDateDebut());
            }
            // Si date de fin d'affiliation > à date de fin => prendre année de
            // fin de l'affiliation
            if (!JAUtil.isDateEmpty(affiliation.getDateFin())
                    && (JACalendar.getYear(affiliation.getDateFin()) < iToAnnee)) {
                iToAnnee = JACalendar.getYear(affiliation.getDateFin());
            }
            for (int idx = iFromAnnee; idx <= iToAnnee; idx++) {
                // Si caisse externe => Ne pas traiter
                if (!AFAffiliationUtil.hasCaisseExterne(affiliation, idx + "", CodeSystem.GENRE_CAISSE_AVS)) {
                    // Test si l'affiliation a des assurances cot. pers. active
                    // (ex: pour ne pas prendre ceux qui n'appartienne pas à la
                    // caisse)
                    AFCotisationJAssuranceManager cotiManager = new AFCotisationJAssuranceManager();
                    cotiManager.setSession(getSession());
                    cotiManager.setForAffiliationId(affiliation.getAffiliationId());
                    cotiManager.setForAnneeActive(idx + "");
                    cotiManager.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
                    if (cotiManager.getCount() > 0) {
                        decisionMng.setForAnneeDecision(idx + "");
                        decisionMng.find(transaction);
                        // Si manager vide ou dernière décision = annulation =>
                        // cas manquant
                        if (decisionMng.size() == 0) {
                            // Recherche si CI dispense
                            if (!ci.existeDispense(transaction, affiliation.getAffilieNumero(), Integer.toString(idx))) {
                                anneesManquantes = anneesManquantes + " " + idx;
                            }
                        }
                    }
                }
            }
            if (!JadeStringUtil.isBlank(anneesManquantes)) {
                // /////////
                HSSFRow row = sheet.createRow(sheet.getPhysicalNumberOfRows() + 1);
                int colNum = 0;
                // numéro affilié
                HSSFCell cell = row.createCell((short) colNum++);
                cell.setCellValue(new HSSFRichTextString(affiliation.getAffilieNumero()));
                // prénom et nom
                cell = row.createCell((short) colNum++);
                cell.setCellValue(new HSSFRichTextString(affiliation.getTiers().getPrenomNom()));
                // Date début
                cell = row.createCell((short) colNum++);
                cell.setCellValue(new HSSFRichTextString(affiliation.getDateDebut()));
                // Date fin
                cell = row.createCell((short) colNum++);
                cell.setCellValue(new HSSFRichTextString(affiliation.getDateFin()));
                // Motif de fin
                cell = row.createCell((short) colNum++);
                cell.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), affiliation.getMotifFin())));
                // Genre
                cell = row.createCell((short) colNum++);
                cell.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(),
                        affiliation.getTypeAffiliation())));
                // années manquantes
                cell = row.createCell((short) colNum++);
                cell.setCellValue(new HSSFRichTextString(anneesManquantes));
            }
            processAppelant.incProgressCounter();
        }
        manager.cursorClose(statement);
        statement = null;
        HSSFFooter footer = sheet.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0086CCP");
        return sheet;
    }

    /**
     * Sets the fromAnneeDecision.
     * 
     * @param fromAnneeDecision
     *            The fromAnneeDecision to set
     */
    public void setFromAnneeDecision(String fromAnneeDecision) {
        this.fromAnneeDecision = fromAnneeDecision;
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
        final String debutAffiliation = getSession().getLabel("DATE_DEBUT");
        final String finAffiliation = getSession().getLabel("DATE_FIN");
        final String motifFinAffiliation = getSession().getLabel("SORTIE_MOTIF");
        final String genre = getSession().getLabel("GENRE_AFFILIE");
        final String anneesManquantes = getSession().getLabel("ANNEES_MANQUANTES");

        final String[] COL_TITLES = { num, nom, debutAffiliation, finAffiliation, motifFinAffiliation, genre,
                anneesManquantes };
        HSSFRow row = null;
        HSSFCell c;
        // création du style pour le titre de la page
        HSSFFont font2 = wb.createFont();
        font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font2.setFontHeight((short) 200);
        font2.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font2);
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
        c = row.createCell((short) 2);
        c.setCellValue(new HSSFRichTextString(session.getLabel("LISTDECISIONMANQUANTES")));
        c.setCellStyle(style2);
        // critères de sélection
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font3.setFontHeight((short) 200);
        style3.setFont(font3);
        row = sheet.createRow(3);
        row = sheet.createRow(4);
        // création de l'entête
        try {
            row = sheet.createRow(5);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(session.getLabel("DEBUT_SELECTION")));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getFromAnneeDecision()));
            c.setCellStyle(style3);
            row = sheet.createRow(6);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(session.getLabel("FIN_SELECTION")));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getToAnneeDecision()));
            c.setCellStyle(style3);
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        // let's use a nifty font for the title
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        // ??? style.setFillBackgroundColor((short) 150);
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        // create Title Row
        row = sheet.createRow(8);
        row = sheet.createRow(9);
        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
            c.setCellStyle(style);
        }
        return sheet;
    }

    /**
     * Sets the toAnneeDecision.
     * 
     * @param toAnneeDecision
     *            The toAnneeDecision to set
     */
    public void setToAnneeDecision(String toAnneeDecision) {
        this.toAnneeDecision = toAnneeDecision;
    }
}
