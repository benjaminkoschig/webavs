package globaz.phenix.reprise;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliationTiers;
import globaz.phenix.db.principale.CPDecisionAffiliationTiersManager;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Process de génaration d'une liste excel pour les concordances entre Cot.Pers. et CI Date de création : (17.06.2007
 * 08:34:14)
 * 
 * @author: jpa
 */
public class CPProcessListeNumAvsIncomplet extends BProcess {
    public static void main(String[] args) {
        CPProcessListeNumAvsIncomplet process = null;
        String user = "globazf";
        String pwd = "globazf";
        String email = "hna@globaz.ch";
        try {
            System.out.println("User : " + user);
            System.out.println("Password : " + pwd);
            System.out.println("Email : " + email);
            BSession session = (BSession) GlobazSystem.getApplication("PHENIX").newSession(user, pwd);
            System.out.println("Reprise started...");
            session.connect(user, pwd);
            process = new CPProcessListeNumAvsIncomplet();
            process.setSession(session);
            process.setEMailAddress(email);
            process.executeProcess();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            System.out.println("La liste des décisions avec n° avs incomplet est terminée.");
        }
        System.exit(0);
    }

    private HSSFSheet sheet;
    private HSSFWorkbook wb;

    /**
     * Commentaire relatif au constructeur TIExportProcess.
     */
    public CPProcessListeNumAvsIncomplet() {
        super();
    }

    /**
     * Commentaire relatif au constructeur TIExportProcess.
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessListeNumAvsIncomplet(BProcess parent) {
        super(parent);
    }

    /**
     * @param session
     */
    public CPProcessListeNumAvsIncomplet(BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        try {
            CPDecisionAffiliationTiersManager manager = new CPDecisionAffiliationTiersManager();
            manager.setSession(getSession());
            manager.setIsActive(Boolean.TRUE);
            manager.setForInTypeDecision(CPDecision.CS_PROVISOIRE + ", " + CPDecision.CS_ACOMPTE + ", "
                    + CPDecision.CS_CORRECTION);
            manager.orderByNumAffilie();
            manager.orderByAnnee();
            manager.orderByIdDecision();
            manager.changeManagerSize(0);

            wb = new HSSFWorkbook();
            sheet = wb.createSheet("CCVD");
            sheet = setTitleRow(wb, sheet);

            populateSheet(manager, getTransaction());
            sheet.toString();

            if (!getTransaction().hasErrors()) {
                JadePublishDocumentInfo docInfo = createDocumentInfo();
                docInfo.setDocumentType("0XXXCCP");
                docInfo.setDocumentTypeNumber("");
                this.registerAttachedDocument(docInfo, getOutputFile());
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return false;
        }
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires) On va compter le nombre d'inscriptions
     */
    @Override
    protected void _validate() throws Exception {
        // Contrôle du mail
        if (JadeStringUtil.isBlank(getEMailAddress())) {
            getSession().addError(getSession().getLabel("CP_MSG_0145"));
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        if (getSession().hasErrors()) {
            abort();
        }
    }

    /**
     * Return le sujet de l'email Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return "Pb avec la liste des numéros avs incomplet pour les décisions provisoires actives";
        } else {
            return "Liste des numéros avs incomplet pour les décisions provisoires actives";
        }
    }

    public String getOutputFile() {
        try {
            File f = File.createTempFile("Liste des numéros avs incomplet", ".xls");
            // f.deleteOnExit();
            FileOutputStream out = new FileOutputStream(f);
            wb.write(out);
            out.close();
            System.out.println(f.getAbsolutePath());
            return f.getAbsolutePath();
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return "";
        }
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public HSSFSheet populateSheet(CPDecisionAffiliationTiersManager manager, BTransaction transaction)
            throws Exception {

        BStatement statement = null;
        CPDecisionAffiliationTiers decision = null;
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
        style2.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
        setProgressScaleValue(manager.getCount(transaction));
        statement = manager.cursorOpen(transaction);
        sheet.setColumnWidth((short) 0, (short) 3500);
        sheet.setColumnWidth((short) 1, (short) 3500);
        sheet.setColumnWidth((short) 2, (short) 8000);
        sheet.setColumnWidth((short) 3, (short) 3500);
        int nbRow = 5;

        String dateNss = "";
        try {
            dateNss = ((CPApplication) getSession().getApplication()).getDateNNSS();
        } catch (Exception e) {
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0115"));
        }

        try {
            while (((decision = (CPDecisionAffiliationTiers) manager.cursorReadNext(statement)) != null)
                    && (!decision.isNew())) {
                boolean casALister = false;
                // Recherche numéro avs
                String numAvs = "";
                if (BSessionUtil.compareDateFirstLower(getSession(), decision.getFinDecision(), dateNss)) {
                    TIHistoriqueAvs hist = new TIHistoriqueAvs();
                    hist.setSession(getSession());
                    try {
                        numAvs = hist.findPrevKnownNumAvs(decision.getIdTiers(), decision.getFinDecision());
                        if (JadeStringUtil.isEmpty(numAvs)) {
                            numAvs = hist.findNextKnownNumAvs(decision.getIdTiers(), decision.getDebutDecision());
                        }
                    } catch (Exception e) {
                        numAvs = "";
                    }
                }
                // Si aucun n° trouvé dans historique ou NNSS => prendre l'actuel n° avs
                if (JadeStringUtil.isEmpty(numAvs)) {
                    numAvs = decision.getNumAvsActuel();
                }
                if (numAvs.length() < 14) {
                    casALister = true;
                }
                if (casALister) {
                    HSSFRow row = sheet.createRow(nbRow);
                    nbRow++;
                    int colNum = 0;
                    // 0 - numéro affilié
                    HSSFCell cell = row.createCell((short) colNum++);
                    cell.setCellStyle(style);
                    cell.setCellValue(new HSSFRichTextString(decision.getNumAffilie()));
                    // 1 - Numéro AVS
                    cell = row.createCell((short) colNum++);
                    cell.setCellStyle(style2);
                    cell.setCellValue(new HSSFRichTextString(numAvs));
                    // 2 - Numéro AVS
                    cell = row.createCell((short) colNum++);
                    cell.setCellStyle(style2);
                    cell.setCellValue(new HSSFRichTextString(decision.getDesignation1() + " "
                            + decision.getDesignation2()));
                    // 3 - Année de décision
                    cell = row.createCell((short) colNum++);
                    cell.setCellStyle(style2);
                    cell.setCellValue(new HSSFRichTextString(decision.getAnneeDecision()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            manager.cursorClose(statement);
        }
        statement = null;
        return sheet;
    }

    /**
     * @param wb
     * @param sheet
     * @param row
     * @return
     */
    private HSSFSheet setTitleRow(HSSFWorkbook wb, HSSFSheet sheet) {

        final String[] COL_TITLES = { getSession().getLabel("NUM_AFFILIE"), "N° AVS incomplet", "Nom", "Année" };

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
            c.setCellValue(new HSSFRichTextString(getSession().getApplication()
                    .getProperty("COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase())));
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
        c.setCellValue(new HSSFRichTextString("Liste des décision provisoires actives avec n° avs incomplet"));
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
        for (int j = 0; j < COL_TITLES.length; j++) {
            c = row.createCell((short) j);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[j]));
            c.setCellStyle(style);
        }
        return sheet;
    }
}
