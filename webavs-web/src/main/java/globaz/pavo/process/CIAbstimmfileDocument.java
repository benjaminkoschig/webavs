package globaz.pavo.process;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.pavo.util.CIUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

/**
 */
public class CIAbstimmfileDocument {

    /**
     * @author sel
     * 
     *         Opérateurs possibles sur le montant de la différence.
     * 
     */
    public enum Operator {
        different("<>"),
        egal("="),
        plusGrandEgal(">="),
        plusPetitEgal("<=");

        protected String operateur = "";

        /**
         * @param val
         */
        Operator(String val) {
            operateur = val;
        }

        /**
         * @return the operateur
         */
        public String getOperateur() {
            return operateur;
        }
    }

    private Boolean allAffilie = true;
    private String anneeDebut = "";
    private String anneeFin = "";
    private JadePublishDocumentInfo documentInfo = null;
    private String montant = "";
    private CIAbstimmfileDocument.Operator montantOperator;
    private String numeroFrom = "";
    private String numeroTo = "";
    private BProcess process = null;
    private BSession session = null;
    private HSSFSheet sheet;
    private HSSFWorkbook wb;

    /**
     * Constructor for CIAbstimmfileDocument.
     */
    public CIAbstimmfileDocument() {
        super();
    }

    /**
     * @param sheetTitle
     * @param anneeD
     * @param anneeF
     * @param sessionM
     */
    public CIAbstimmfileDocument(String sheetTitle, String anneeD, String anneeF, BSession sessionM) {
        documentInfo = new JadePublishDocumentInfo();
        setSession(sessionM);
        wb = new HSSFWorkbook();
        sheet = wb.createSheet(sheetTitle);
        HSSFPrintSetup psa = sheet.getPrintSetup();
        psa.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        createHeader(sheet);
        anneeDebut = anneeD;
        anneeFin = anneeF;
    }

    /**
     * Vérifie que les éléments soient valides pour être traités
     * 
     * @param elementARemplir
     * @return true si les conditions sont adéquates
     */
    private boolean checkElementARemplir(ArrayList elementARemplir) {
        // On affiche pas si tout est égal à 0
        if ("0.00".equals(elementARemplir.get(1)) && "0.00".equals(elementARemplir.get(2))
                && "0.00".equals(elementARemplir.get(3))) {
            return false;
        }

        if (!JadeStringUtil.isBlank(getMontant())) {
            // Inforom279
            FWCurrency difference = new FWCurrency((String) elementARemplir.get(3));
            difference.abs();
            FWCurrency montant = new FWCurrency(getMontant());
            montant.abs();

            switch (getMontantOperator()) {
                case egal:
                    if (difference.compareTo(montant) == 0) {
                        return true;
                    }
                    break;
                case different:
                    if (difference.compareTo(montant) != 0) {
                        return true;
                    }
                    break;
                case plusGrandEgal:
                    if (difference.compareTo(montant) >= 0) {
                        return true;
                    }
                    break;
                case plusPetitEgal:
                    if (difference.compareTo(montant) <= 0) {
                        return true;
                    }
                    break;
            }
            return false;
        }

        return true;
    }

    /**
     * Crée l'entête
     * 
     * @param title
     * @return l'entête
     */
    protected HSSFHeader createHeader(HSSFSheet sheetHead) {
        // en-tête
        getDocumentInfo().setTemplateName("");
        // getDocumentInfo().setDocumentType("xls");
        HSSFHeader header = sheetHead.getHeader();
        header.setLeft(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        return header;
    }

    /**
     * @param critereNom
     * @param critereValue
     */
    private void creatRowCritere(String critereNom, String critereValue, HSSFCellStyle style) {
        int colNum = 0;
        HSSFRow row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        HSSFCell cell = row.createCell((short) colNum++);
        cell.setCellStyle(style);
        cell.setCellValue(critereNom);
        cell = row.createCell((short) colNum++);
        cell.setCellStyle(style);
        cell.setCellValue(critereValue);
    }

    /**
     * @param element
     * @param employeur
     * @param nom
     */
    private void fillElements(ArrayList element, String employeur, String nom) {
        int colNum = 0;
        HSSFCellStyle style = wb.createCellStyle();
        HSSFRow row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        HSSFCell cell = row.createCell((short) colNum++);
        cell.setCellValue(employeur);
        cell = row.createCell((short) colNum++);
        cell.setCellValue(nom);
        cell = row.createCell((short) colNum++);
        cell.setCellValue((String) element.get(0));

        HSSFCellStyle styleMontant = wb.createCellStyle();
        // setDataFormat(wb.createDataFormat().getFormat("#,##0.00"))
        styleMontant.setDataFormat((short) 4);
        styleMontant.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        styleMontant.setWrapText(true); // La largeur de la cellule s'adapte

        cell = row.createCell((short) colNum++);
        cell.setCellStyle(styleMontant);
        cell.setCellValue(transformMontant((String) element.get(1)));

        cell = row.createCell((short) colNum++);
        cell.setCellStyle(styleMontant);
        cell.setCellValue(transformMontant((String) element.get(2)));

        cell = row.createCell((short) colNum++);
        cell.setCellStyle(styleMontant);
        cell.setCellValue(transformMontant((String) element.get(3)));
    }

    /**
     * @return the allAffilie
     */
    public Boolean getAllAffilie() {
        return allAffilie;
    }

    public JadePublishDocumentInfo getDocumentInfo() {
        return documentInfo;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return the montantOperator
     */
    public CIAbstimmfileDocument.Operator getMontantOperator() {
        return montantOperator;
    }

    /**
     * @return the numeroFrom
     */
    public String getNumeroFrom() {
        return numeroFrom;
    }

    /**
     * @return the numeroTo
     */
    public String getNumeroTo() {
        return numeroTo;
    }

    /**
     * @return
     */
    public String getOutputFile() {
        try {
            // File f = new File("tent_" + JACalendar.today().toStrAMJ() + "_" +
            // Math.round((Math.random() * 10000) % 10000) + ".xls");
            File f = File.createTempFile("Abstimmfile" + JACalendar.today().toStrAMJ() + "_", ".xls");
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
    public BProcess getProcess() {
        return process;
    }

    /**
     * @return
     */
    public BSession getSession() {
        return session;
    }

    /**
     * @param wb
     * @param font
     * @return
     */
    private HSSFCellStyle getStyleHeadColumn(HSSFWorkbook wb, HSSFFont font) {
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        return style;
    }

    /**
     * @param session
     * @return
     */
    public HSSFSheet populateSheet(BSession session) {

        sheet = setTitleRow(wb, sheet);

        AFAffiliationManager tiMgr = new AFAffiliationManager();
        tiMgr.setSession(session);
        tiMgr.setForTypeFacturation(AFAffiliationManager.PARITAIRE);
        tiMgr.forIsTraitement(false);
        tiMgr.setOrderBy(AFAffiliationManager.ORDER_AFFILIENUMERO);

        if (!getAllAffilie()) {
            tiMgr.setFromAffilieNumero(getNumeroFrom());
            tiMgr.setToAffilieNumero(getNumeroTo());
        }

        int sizeAff = 0;
        try {
            sizeAff = tiMgr.getCount();
        } catch (Exception e1) {
            JadeLogger.info(this, e1);
        }
        process.setProgressScaleValue(sizeAff);
        BITransaction transaction = null;
        BStatement statement = null;
        try {
            transaction = session.newTransaction();
            transaction.openTransaction();
            int nbAffilieTraite = 0;
            statement = tiMgr.cursorOpen((BTransaction) transaction);
            AFAffiliation aff = new AFAffiliation();
            while (((aff = (AFAffiliation) tiMgr.cursorReadNext(statement)) != null) && !process.isAborted()) {

                String numeroAffilie = aff.getAffilieNumero();
                ArrayList result = CIUtil.getMasseSalarialePaAnnee(session, numeroAffilie, anneeDebut, anneeFin);
                for (int i = 0; i < result.size(); i++) {
                    ArrayList elementARemplir = new ArrayList();
                    elementARemplir = (ArrayList) result.get(i);

                    if (checkElementARemplir(elementARemplir)) {
                        fillElements(elementARemplir, numeroAffilie, aff.getTiersNom());
                        nbAffilieTraite++;
                        process.setProgressCounter(nbAffilieTraite);
                    }
                }
            }

            HSSFFooter footer = sheet.getFooter();
            footer.setRight("Ref : 0212CCI");
            // Ajout de la référence INFOROM
            HSSFCell cell = null;
            HSSFRow row = null;
            row = sheet.createRow(sheet.getPhysicalNumberOfRows() + 1);
            cell = row.createCell((short) 1);
            cell.setCellValue("Réf. : 0212CCI");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    try {
                        tiMgr.cursorClose(statement);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        statement.closeStatement();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                if (transaction != null) {
                    try {
                        transaction.closeTransaction();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sheet;

    }

    /**
     * Affiche les filtres selectionnés
     */
    private void printCritere() {
        HSSFFont font = wb.createFont();
        font.setItalic(true);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style.setFont(font);

        if (!JadeStringUtil.isBlank(anneeDebut)) {
            creatRowCritere(getSession().getLabel("ABSTIMMFILE_CRITERE_ANNEEDEBUT"), anneeDebut, style);
        }
        if (!JadeStringUtil.isBlank(anneeFin)) {
            creatRowCritere(getSession().getLabel("ABSTIMMFILE_CRITERE_ANNEEFIN"), anneeFin, style);
        }
        if (allAffilie) {
            creatRowCritere(getSession().getLabel("ABSTIMMFILE_CRITERE_ALLAFFILIE"), allAffilie.toString(), style);
        }
        if (!JadeStringUtil.isBlank(numeroFrom)) {
            creatRowCritere(getSession().getLabel("ABSTIMMFILE_CRITERE_DUNUMERO"), numeroFrom, style);
        }
        if (!JadeStringUtil.isBlank(numeroTo)) {
            creatRowCritere(getSession().getLabel("ABSTIMMFILE_CRITERE_AUNUMERO"), numeroTo, style);
        }
        if (!JadeStringUtil.isBlank(montant)) {
            creatRowCritere(getSession().getLabel("ABSTIMMFILE_CRITERE_DIFFERENCE"), montantOperator.getOperateur()
                    + montant, style);
        }

        creatRowCritere("", "", style);
    }

    /**
     * @param wb
     * @param sheet
     * @param font
     */
    private void printTitle(HSSFWorkbook wb, HSSFSheet sheet, HSSFFont font) {
        // Ajoute une ligne vide
        sheet.createRow(sheet.getPhysicalNumberOfRows());

        // Fusionne les colonnes
        Region region = new Region();
        region.setRowFrom(sheet.getPhysicalNumberOfRows());
        region.setRowTo(sheet.getPhysicalNumberOfRows());
        region.setColumnFrom((short) 0);
        region.setColumnTo((short) 5);
        sheet.addMergedRegion(region);

        // Style pour le titre
        HSSFCellStyle styleListTitleCenter = wb.createCellStyle();
        styleListTitleCenter.setFont(font);
        styleListTitleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // Crée le titre
        HSSFRow rowTitle = sheet.createRow(sheet.getPhysicalNumberOfRows());
        rowTitle.setHeight((short) 400);
        HSSFCell cell = rowTitle.createCell((short) 0);
        cell.setCellStyle(styleListTitleCenter);
        cell.setCellValue(getSession().getLabel("ABSTIMMFILE_TITLE"));

        // Ajoute une ligne vide
        sheet.createRow(sheet.getPhysicalNumberOfRows());
    }

    /**
     * @param allAffilie
     *            the allAffilie to set
     */
    public void setAllAffilie(Boolean allAffilie) {
        this.allAffilie = allAffilie;
    }

    public void setDocumentInfo(JadePublishDocumentInfo documentInfo) {
        this.documentInfo = documentInfo;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * @param montantOperator
     *            the montantOperator to set
     */
    public void setMontantOperator(CIAbstimmfileDocument.Operator montantOperator) {
        this.montantOperator = montantOperator;
    }

    /**
     * @param numeroFrom
     *            the numeroFrom to set
     */
    public void setNumeroFrom(String numeroFrom) {
        this.numeroFrom = numeroFrom;
    }

    /**
     * @param numeroTo
     *            the numeroTo to set
     */
    public void setNumeroTo(String numeroTo) {
        this.numeroTo = numeroTo;
    }

    /**
     * @param process
     */
    public void setProcess(BProcess process) {
        this.process = process;
    }

    /**
     * @param session
     */
    public void setSession(BSession session) {
        this.session = session;
    }

    /**
     * @param wb
     * @param sheet
     * @return
     */
    private HSSFSheet setTitleRow(HSSFWorkbook wb, HSSFSheet sheet) {
        HSSFRow row = null;

        try {
            HSSFFont font = wb.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL); // arial font
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold

            HSSFCellStyle style = getStyleHeadColumn(wb, font);
            String[] COL_TITLES = { session.getLabel("IMPRESSION_JOURNAL_NUMERO_AFFILIE"),
                    session.getLabel("MSG_DOSSIER_EMAIL_NOM"), session.getLabel("IMPRESSION_JOURNAL_COL3"),
                    session.getLabel("MSG_MENU_COMPTE"), session.getLabel("ABSTI_HB"),
                    session.getLabel("ABSTI_HB_DIFF")

            };

            printTitle(wb, sheet, font);
            printCritere();

            sheet.setColumnWidth((short) 0, (short) 4200);
            sheet.setColumnWidth((short) 1, (short) 10000);
            sheet.setColumnWidth((short) 2, (short) 2500);
            sheet.setColumnWidth((short) 3, (short) 4000);
            sheet.setColumnWidth((short) 4, (short) 4000);
            sheet.setColumnWidth((short) 5, (short) 4000);

            // create Title Row
            row = sheet.createRow(this.sheet.getPhysicalNumberOfRows());
            for (int i = 0; i < COL_TITLES.length; i++) {
                // set cell value
                HSSFCell c = row.createCell((short) i);
                c.setCellValue(COL_TITLES[i]);
                c.setCellStyle(style);
            }

        } catch (Exception e) {
        }
        return sheet;

    }

    /**
     * @param element
     * @return
     */
    private double transformMontant(String montant) {
        return JadeStringUtil.parseDouble(JANumberFormatter.deQuote(montant), 0);
    }

}
