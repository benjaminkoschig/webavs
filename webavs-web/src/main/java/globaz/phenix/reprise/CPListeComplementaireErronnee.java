package globaz.phenix.reprise;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliation;
import globaz.phenix.db.principale.CPDecisionAffiliationManager;
import globaz.phenix.db.principale.CPDecisionManager;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Vector;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Insérez la description du type ici. Date de création : (17.06.2003 14:38:38)
 * 
 * @author: jpa
 */
public class CPListeComplementaireErronnee {
    private String forAnneeDecision = "";
    private CPDecisionAffiliationManager manager = null;
    private Vector messages = new Vector();
    private Boolean miseAjourDecision = Boolean.FALSE;
    private BProcess processAppelant = null;
    private BSession session = null;
    private HSSFSheet sheet;
    private HSSFWorkbook wb;

    /**
     * Commentaire relatif au constructeur TentDocument.
     */
    public CPListeComplementaireErronnee() {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet("COMP1");
    }

    /**
     * Commentaire relatif au constructeur TentDocument.
     */
    public CPListeComplementaireErronnee(BSession session, String forAnnee) {
        this.session = session;
        wb = new HSSFWorkbook();
        setForAnneeDecision(forAnnee);
        sheet = wb.createSheet(forAnnee);
        sheet = setTitleRow(wb, sheet);
    }

    /**
     * Returns the fromAnneeDecision.
     * 
     * @return String
     */
    public String getForAnneeDecision() {
        return forAnneeDecision;
    }

    public CPDecisionAffiliationManager getManager() {
        return manager;
    }

    public Boolean getMiseAjourDecision() {
        return miseAjourDecision;
    }

    public String getOutputFile() {
        try {
            File f = File.createTempFile("ListeComplementaire" + JACalendar.today().toStrAMJ() + "_", ".xls");
            // f.deleteOnExit();
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

    public HSSFSheet populateSheet(CPDecisionAffiliationManager manager, BTransaction transaction) throws Exception {

        BStatement statement = null;
        CPDecisionAffiliation decision = null;
        // decision.setSession(transaction.getSession());
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        processAppelant.setProgressScaleValue(manager.getCount(transaction));

        BTransaction transactionLecture = (BTransaction) getSession().newTransaction();
        sheet.setColumnWidth((short) 0, (short) 3500);
        sheet.setColumnWidth((short) 1, (short) 2000);
        sheet.setColumnWidth((short) 2, (short) 5000);
        sheet.setColumnWidth((short) 3, (short) 5000);
        int nbRow = 11;
        boolean aTraiter = false;
        boolean stopRecherche = false;
        try {
            transactionLecture.openTransaction();
            statement = manager.cursorOpen(transactionLecture);

            while (((decision = (CPDecisionAffiliation) manager.cursorReadNext(statement)) != null)
                    && (!decision.isNew())) {
                try {
                    aTraiter = false;
                    stopRecherche = false;
                    // Test s'il n'y a pas une autre décision active pour la
                    // même année
                    // Si même id affiliation => complementaire=true sinon
                    // complementaire = false
                    CPDecisionManager mng1 = new CPDecisionManager();
                    mng1.setForIdTiers(decision.getIdTiers());
                    mng1.setForExceptIdDecision(decision.getIdDecision());
                    mng1.setSession(getSession());
                    mng1.setForIsActive(Boolean.TRUE);
                    mng1.setForAnneeDecision(decision.getAnneeDecision());
                    mng1.find();
                    for (int i = 0; (i < mng1.getSize()) && !stopRecherche; i++) {
                        CPDecision deci = (CPDecision) mng1.getEntity(i);
                        if (!deci.getIdAffiliation().equalsIgnoreCase(decision.getIdAffiliation())) {
                            aTraiter = true;
                        }
                        if (deci.getIdAffiliation().equalsIgnoreCase(decision.getIdAffiliation())) {
                            aTraiter = false;
                            stopRecherche = true;
                        }
                    }
                    // Impression du cas à modifier
                    if (aTraiter) {
                        HSSFRow row = sheet.createRow(nbRow);
                        nbRow++;
                        int colNum = 0;
                        // numéro affilié
                        HSSFCell cell = row.createCell((short) colNum++);
                        cell.setCellStyle(style);
                        cell.setCellValue(new HSSFRichTextString(decision.getNumAffilie()));
                        // Année
                        cell = row.createCell((short) colNum++);
                        cell.setCellStyle(style);
                        cell.setCellValue(new HSSFRichTextString(decision.getAnneeDecision()));
                        // Période décision
                        cell = row.createCell((short) colNum++);
                        cell.setCellStyle(style);
                        cell.setCellValue(new HSSFRichTextString(decision.getDebutDecision() + " - "
                                + decision.getFinDecision()));
                        // Genre
                        cell = row.createCell((short) colNum++);
                        cell.setCellStyle(style);
                        cell.setCellValue(new HSSFRichTextString(globaz.phenix.translation.CodeSystem.getLibelleCourt(
                                getSession(), decision.getGenreAffilie())));
                        if (getMiseAjourDecision().equals(Boolean.TRUE)) {
                            try {
                                CPDecision decis = new CPDecision();
                                decis.setIdDecision(decision.getIdDecision());
                                decis.setSession(getSession());
                                decis.retrieve();
                                decis.setComplementaire(Boolean.FALSE);
                                decis.update(transaction);
                                if (getSession().hasErrors()) {
                                    processAppelant.getMemoryLog().logMessage(transaction.getErrors().toString(),
                                            FWMessage.ERREUR, this.getClass().getName());
                                    transaction.rollback();
                                    transaction.clearErrorBuffer();
                                }
                                if (transaction.hasErrors()) {
                                    processAppelant.getMemoryLog().logMessage(transaction.getErrors().toString(),
                                            FWMessage.ERREUR, this.getClass().getName());
                                    transaction.rollback();
                                    transaction.clearErrorBuffer();
                                } else {
                                    transaction.commit();
                                }
                            } catch (Exception e) {
                                processAppelant.getMemoryLog().logMessage(
                                        decision.getNumAffilie() + " - " + decision.getAnneeDecision() + " - "
                                                + decision.getIdDecision(), FWMessage.INFORMATION,
                                        this.getClass().getName());
                                processAppelant.getTransaction().rollback();
                            } finally {
                                for (Iterator iter = processAppelant.getMemoryLog().getMessagesToVector()
                                        .iterator(); iter.hasNext();) {
                                    messages.add(iter.next());
                                }
                                processAppelant.getMemoryLog().clear();
                            }
                        }
                    }
                    processAppelant.incProgressCounter();
                } catch (Exception e) {
                    processAppelant.getMemoryLog().logMessage(
                            decision.getNumAffilie() + " - " + decision.getAnneeDecision() + " - "
                                    + decision.getIdDecision(), FWMessage.INFORMATION, this.getClass().getName());
                    processAppelant.getTransaction().rollback();
                }
            }
        } catch (Exception e) {
            processAppelant.getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        } finally {
            try {
                try {
                    manager.cursorClose(statement);
                } finally {
                    if (transactionLecture != null) {
                        transactionLecture.closeTransaction();
                    }
                    statement = null;
                }
            } catch (Exception e) {
                processAppelant.getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL,
                        this.getClass().getName());
            }
        }
        // Remettre les erreurs des process dans le log
        for (Iterator iter = messages.iterator(); iter.hasNext();) {
            processAppelant.getMemoryLog().getMessagesToVector().add(iter.next());
        }
        manager.cursorClose(statement);
        statement = null;
        HSSFFooter footer = sheet.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Correction");
        return sheet;
    }

    /**
     * Sets the fromAnneeDecision.
     * 
     * @param fromAnneeDecision
     *            The fromAnneeDecision to set
     */
    public void setForAnneeDecision(String fromAnneeDecision) {
        forAnneeDecision = fromAnneeDecision;
    }

    public void setManager(CPDecisionAffiliationManager manager) {
        this.manager = manager;
    }

    public void setMiseAjourDecision(Boolean miseAjourDecision) {
        this.miseAjourDecision = miseAjourDecision;
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

    /**
     * @param wb
     * @param sheet
     * @param row
     * @return
     */
    private HSSFSheet setTitleRow(HSSFWorkbook wb, HSSFSheet sheet) {

        final String[] COL_TITLES = { session.getLabel("NUM_AFFILIE"), session.getLabel("ANNEE"),
                session.getLabel("PERIODE"), session.getLabel("GENRE_DECISION") };

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
        // row = sheet.createRow(1);
        // row = sheet.createRow(2);
        c = row.createCell((short) 5);
        c.setCellValue(new HSSFRichTextString(JACalendar.todayJJsMMsAAAA()));
        c.setCellStyle(style2);
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        c = row.createCell((short) 1);
        c.setCellValue(new HSSFRichTextString("Liste décision complémentaire erronée"));
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
        int i = 4;
        // création de l'entête
        try {
            // Année de début
            if (getForAnneeDecision().length() > 0) {
                row = sheet.createRow(i++);
                c = row.createCell((short) 0);
                c.setCellValue(new HSSFRichTextString(""));
                c.setCellStyle(style3);
                c = row.createCell((short) 1);
                c.setCellValue(new HSSFRichTextString(session.getLabel("ANNEE")));
                c.setCellStyle(style3);
                c = row.createCell((short) 3);
                c.setCellValue(new HSSFRichTextString(getForAnneeDecision()));
                c.setCellStyle(style3);
            }
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
        row = sheet.createRow(10);
        for (int j = 0; j < COL_TITLES.length; j++) {
            c = row.createCell((short) j);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[j]));
            c.setCellStyle(style);
        }
        return sheet;
    }
}
