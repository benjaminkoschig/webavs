package globaz.pavo.process;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.common.Jade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIRassemblementOuverture;
import globaz.pavo.db.compte.CIRassemblementOuvertureManager;
import globaz.pavo.db.splitting.CIDossierSplitting;
import globaz.pavo.db.splitting.CIDossierSplittingManager;
import globaz.pavo.print.list.CIExtendeEcritureManager;
import globaz.pyxis.util.TISQL;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import ch.horizon.jaspe.util.JADate;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CIStatDocument {
    private final static String[] COL_TITLES = { "Beschreibung", "Ziffer", "Anzahl" };
    private final static String[] COL_TITLES_FR = { "Description", "Code", "Nombre"

    };
    private final static String[] COL_TITLES2 = { "Beitragsjahr", "Statistikdaten", "SZ", "Anzahl Buchungen",
            "Beitrag in Fr.", "Beitrag mit Rp." };
    private final static String[] COL_TITLES2_FR = { "Année de cotisation", "Données statistiques", "Genre",
            "Nombre d'inscriptions", "Montant", "Montant avec cts" };
    protected final static String MSG_EMAIL_SUBJECT_ERROR = "8002";
    protected final static String MSG_EMAIL_SUBJECT_OK = "8001";
    private final static String NUM_INFOROM = "0160CCI";
    private String debutAnnee = "";
    private JadePublishDocumentInfo documentInfo = null;
    private String finAnnee = "";
    private BProcess process = null;
    private String rentner = "Rentner-AHV Lohnsumme";
    /**
     * Constructor for CIStatDocument.
     */
    private BSession session = null;
    private HSSFSheet sheet;
    private HSSFSheet sheet2;
    private HSSFSheet sheet3;
    private String sz = "Schlüsselzahl";
    private String sz0 = "Schlüsselzahl(00/04)";
    private String sz2 = "Schlüsselzahl(02)";
    private String sz3 = "Schlüsselzahl(03)";
    private BigDecimal totalMontant06 = new BigDecimal("0");
    private BigDecimal totalMontant07 = new BigDecimal("0");
    private BigDecimal totalMontant0700 = new BigDecimal("0");
    private BigDecimal totalMontant0702 = new BigDecimal("0");
    private BigDecimal totalMontant0703 = new BigDecimal("0");
    private BigDecimal totalMontant16 = new BigDecimal("0");
    private BigDecimal totalMontant17 = new BigDecimal("0");
    private BigDecimal totalMontant1700 = new BigDecimal("0");
    private BigDecimal totalMontant1702 = new BigDecimal("0");
    private BigDecimal totalMontant1703 = new BigDecimal("0");
    private BigDecimal totalMontantRentner = new BigDecimal("0");
    private BigDecimal totalMontantRp06 = new BigDecimal("0");

    private BigDecimal totalMontantRp07 = new BigDecimal("0");
    private BigDecimal totalMontantRp0700 = new BigDecimal("0");
    private BigDecimal totalMontantRp0702 = new BigDecimal("0");
    private BigDecimal totalMontantRp0703 = new BigDecimal("0");
    private BigDecimal totalMontantRp16 = new BigDecimal("0");
    private BigDecimal totalMontantRp17 = new BigDecimal("0");
    private BigDecimal totalMontantRp1700 = new BigDecimal("0");

    private BigDecimal totalMontantRp1702 = new BigDecimal("0");
    private BigDecimal totalMontantRp1703 = new BigDecimal("0");
    private BigDecimal totalMontantRpRentner = new BigDecimal("0");
    private Integer totalNbInsc06 = new Integer("0");

    private Integer totalNbInsc07 = new Integer("0");
    private Integer totalNbInsc0700 = new Integer("0");
    private Integer totalNbInsc0702 = new Integer("0");
    private Integer totalNbInsc0703 = new Integer("0");
    private Integer totalNbInsc16 = new Integer("0");
    private Integer totalNbInsc17 = new Integer("0");

    private Integer totalNbInsc1700 = new Integer("0");
    private Integer totalNbInsc1702 = new Integer("0");
    private Integer totalNbInsc1703 = new Integer("0");
    private Integer totalNbInscRentner = new Integer("0");
    private HSSFWorkbook wb;

    public CIStatDocument() {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet("STAT");

    }

    public CIStatDocument(String sheetTitle, String sheetTitle2, String anneeDeb, String AnneeF, BSession sessionDoc) {
        documentInfo = new JadePublishDocumentInfo();
        documentInfo.setDocumentTypeNumber(CIStatDocument.NUM_INFOROM);
        wb = new HSSFWorkbook();
        session = sessionDoc;
        sheet = wb.createSheet(sheetTitle);
        sheet = setTitleRow(wb, sheet);
        HSSFPrintSetup psa = sheet.getPrintSetup();
        psa.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        createHeader(sheet);
        sheet2 = wb.createSheet(sheetTitle2);
        sheet2 = setTitleRow2(wb, sheet2);
        HSSFPrintSetup psa2 = sheet2.getPrintSetup();
        psa2.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        createHeader(sheet2);
        debutAnnee = anneeDeb;
        finAnnee = AnneeF;

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

    public void createSheet3(BSession session) throws Exception {

        try {

            sheet3 = wb.createSheet("Stat3");
            createHeader(sheet3);
            HSSFPrintSetup psa3 = sheet3.getPrintSetup();
            psa3.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
            HSSFFont font = wb.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

            HSSFCellStyle headerCellStyle = wb.createCellStyle();
            headerCellStyle.setFont(font);
            headerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            headerCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            headerCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            headerCellStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
            headerCellStyle.setBorderTop(HSSFCellStyle.BORDER_THICK);

            HSSFRow headerRow = sheet3.createRow(0);

            HSSFCell headerCell = headerRow.createCell((short) 0);
            headerCell.setCellValue(new HSSFRichTextString(getSession().getLabel("MOTIF_ARC")));
            headerCell.setCellStyle(headerCellStyle);

            headerCell = headerRow.createCell((short) 1);
            headerCell.setCellValue(new HSSFRichTextString(getSession().getLabel("NOMBRE_ANNONCE_MOTIF")));
            headerCell.setCellStyle(headerCellStyle);

            StringBuffer theQuery = new StringBuffer();

            theQuery.append(" FROM ");
            theQuery.append(Jade.getInstance().getDefaultJdbcSchema() + "." + "HEANNOP");
            // TODO attention au table prefix je dois en tenir compte ou pas ?
            theQuery.append(" WHERE ");
            theQuery.append(" RNLENR LIKE '1101%' ");
            theQuery.append(" AND ");
            theQuery.append(" RNTSTA = 117003 ");
            theQuery.append(" AND ");
            theQuery.append(" RNDDAN BETWEEN " + new JADate(debutAnnee).toAMJ().toString() + " AND "
                    + new JADate(finAnnee).toAMJ().toString());
            theQuery.append(" GROUP BY RNMOT ");

            Map<String, String> resultMap = TISQL.queryMap(getSession(), "RNMOT", "COUNT(*) CPT", theQuery.toString());

            /**
             * Triage de la liste Nécessaire car la version actuelle de TISQL.queryMap(...) ne supporte pas la clause
             * ORDER BY
             */
            List<String> keyResultMap = new ArrayList<String>(resultMap.keySet());
            Collections.sort(keyResultMap);

            HSSFCellStyle cellStyle = wb.createCellStyle();
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            HSSFRow row;
            HSSFCell cell;
            int nombreAnnonceTotal = 0;
            for (String aKey : keyResultMap) {
                row = sheet3.createRow(sheet3.getPhysicalNumberOfRows());

                cell = row.createCell((short) 0);
                cell.setCellValue(Integer.valueOf(aKey).intValue());

                cell = row.createCell((short) 1);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(Integer.valueOf(resultMap.get(aKey)).intValue());
                nombreAnnonceTotal = nombreAnnonceTotal + Integer.valueOf(resultMap.get(aKey)).intValue();
            }
            row = sheet3.createRow(sheet3.getPhysicalNumberOfRows());

            cell = row.createCell((short) 0);
            cell.setCellValue(new HSSFRichTextString(getSession().getLabel("NOMBRE_TOTAL_ANNONCE")));

            cell = row.createCell((short) 1);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(nombreAnnonceTotal);

        } catch (Exception e) {
            throw new Exception("TODO");
        }

    }

    private void fillElements(ArrayList element, int anneeCotisation, String genre, String codeSpe) {
        int colNum = 0;
        HSSFCellStyle style = wb.createCellStyle();
        HSSFRow row = sheet2.createRow(sheet2.getPhysicalNumberOfRows());
        HSSFCell cell = row.createCell((short) colNum++);
        // cell.setCellValue(anneeCotisation);
        cell = row.createCell((short) colNum++);

        if ("6".equals(genre) || "7".equals(genre)) {
            if ("3".equals(codeSpe)) {
                cell.setCellValue(sz3);
            } else if ("0".equals(codeSpe)) {
                cell.setCellValue(sz0);
            } else if ("2".equals(codeSpe)) {
                cell.setCellValue(sz2);

            } else {
                cell.setCellValue(sz);
            }
        }
        cell = row.createCell((short) colNum++);
        cell.setCellValue(genre);
        cell = row.createCell((short) colNum++);
        if (element != null) {
            cell.setCellValue(((Integer) element.get(0)).intValue());
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new FWCurrency(String.valueOf(element.get(2))).toStringFormat());
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new FWCurrency(String.valueOf(element.get(1))).toStringFormat());

        } else {
            cell.setCellValue(0);
            cell = row.createCell((short) colNum++);
            cell.setCellValue(0);
            cell = row.createCell((short) colNum++);
            cell.setCellValue(0);
        }

    }

    private void fillEntete(ArrayList element, int anneeCotisation) {
        int colNum = 0;
        HSSFRow row = sheet2.createRow(sheet2.getPhysicalNumberOfRows());
        HSSFCellStyle style = wb.createCellStyle();
        row = sheet2.createRow(sheet2.getPhysicalNumberOfRows());
        HSSFCell cell = row.createCell((short) colNum++);
        if (anneeCotisation == 0) {
            cell.setCellValue("Total");
        } else {
            cell.setCellValue(anneeCotisation);
        }
        cell = row.createCell((short) colNum++);
        cell.setCellValue(rentner);
        cell = row.createCell((short) colNum++);
        cell = row.createCell((short) colNum++);
        cell.setCellValue(((Integer) element.get(0)).intValue());
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cell = row.createCell((short) colNum++);
        cell.setCellValue(new FWCurrency(String.valueOf(element.get(1))).toStringFormat());
        cell = row.createCell((short) colNum++);
        cell.setCellValue(new FWCurrency(String.valueOf(element.get(2))).toStringFormat());
    }

    public JadePublishDocumentInfo getDocumentInfo() {
        return documentInfo;
    }

    /*
     * return le résultatpour chaque ligne en fonction du genre et de l'extourne
     */
    private ArrayList getElements(BSession session, String anneeCotisation, String debutAnnee, String finAnnee,
            String genre, boolean extourne, String codeSpe) {
        CIEcritureRapideManager ecrMgr = new CIEcritureRapideManager();
        ecrMgr.setSession(session);
        ecrMgr.setForGenre(genre);
        if (extourne) {
            ecrMgr.setForExtourne(CIEcriture.CS_EXTOURNE_1);
        } else {
            ecrMgr.setForNotExtourne("True");
        }
        if ("pas".equals(codeSpe)) {
        } else if ("0".equals(codeSpe)) {
            ecrMgr.setInCodeSpecial("0,312004");
        } else if ("3".equals(codeSpe)) {
            ecrMgr.setForCodeSpecial(CIEcriture.CS_NONFORMATTEUR_SALARIE);
        } else if ("2".equals(codeSpe)) {
            ecrMgr.setForCodeSpecial(CIEcriture.CS_NONFORMATTEUR_INDEPENDANT);
        }
        ecrMgr.setFromDateInscription(debutAnnee);
        ecrMgr.setToDateInscription(finAnnee);
        ecrMgr.setForAnneeCotisation(anneeCotisation);
        // ecrMgr.setForCodeSpecial(CIEcriture.CS_NONFORMATTEUR_SALARIE);
        int nbInsc = 0;
        BigDecimal somme = new BigDecimal("0");
        BigDecimal sommeSansCt = new BigDecimal("0");
        try {
            nbInsc = ecrMgr.getCount();

        } catch (Exception e) {
        }
        if (0 == nbInsc) {
            return null;
        }
        try {
            somme = ecrMgr.getSum("KBMMON");
            sommeSansCt = ecrMgr.getSum(("TRUNCATE(KBMMON,0)"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer nbInsInteger = new Integer(nbInsc);
        ArrayList result = new ArrayList();
        result.add(nbInsInteger);
        result.add(somme);
        result.add(sommeSansCt);
        // Mise à jour des totaux pour chaque genre.
        if (CIEcriture.CS_CIGENRE_6.equals(genre) && !extourne) {
            totalNbInsc06 = new Integer((totalNbInsc06.intValue() + nbInsInteger.intValue()));
            totalMontant06 = totalMontant06.add(sommeSansCt);
            totalMontantRp06 = totalMontantRp06.add(somme);
        }
        if (CIEcriture.CS_CIGENRE_6.equals(genre) && extourne) {
            totalNbInsc16 = new Integer((totalNbInsc16.intValue() + nbInsInteger.intValue()));
            totalMontant16 = totalMontant16.add(sommeSansCt);
            totalMontantRp16 = totalMontantRp16.add(somme);
        }

        if (CIEcriture.CS_CIGENRE_7.equals(genre) && !extourne && "pas".equals(codeSpe)) {
            totalNbInsc07 = new Integer((totalNbInsc07.intValue() + nbInsInteger.intValue()));
            totalMontant07 = totalMontant07.add(sommeSansCt);
            totalMontantRp07 = totalMontantRp07.add(somme);
        }
        if (CIEcriture.CS_CIGENRE_7.equals(genre) && extourne && "pas".equals(codeSpe)) {
            totalNbInsc17 = new Integer((totalNbInsc17.intValue() + nbInsInteger.intValue()));
            totalMontant17 = totalMontant17.add(sommeSansCt);
            totalMontantRp17 = totalMontantRp17.add(somme);
        }
        if (CIEcriture.CS_CIGENRE_7.equals(genre) && !extourne && "0".equals(codeSpe)) {
            totalNbInsc0700 = new Integer((totalNbInsc0700.intValue() + nbInsInteger.intValue()));
            totalMontant0700 = totalMontant0700.add(sommeSansCt);
            totalMontantRp0700 = totalMontantRp0700.add(somme);
        }
        if (CIEcriture.CS_CIGENRE_7.equals(genre) && extourne && "0".equals(codeSpe)) {
            totalNbInsc1700 = new Integer((totalNbInsc1700.intValue() + nbInsInteger.intValue()));
            totalMontant1700 = totalMontant1700.add(sommeSansCt);
            totalMontantRp1700 = totalMontantRp1700.add(somme);
        }
        if (CIEcriture.CS_CIGENRE_7.equals(genre) && !extourne && "3".equals(codeSpe)) {
            totalNbInsc0703 = new Integer((totalNbInsc0703.intValue() + nbInsInteger.intValue()));
            totalMontant0703 = totalMontant0703.add(sommeSansCt);
            totalMontantRp0703 = totalMontantRp0703.add(somme);
        }
        if (CIEcriture.CS_CIGENRE_7.equals(genre) && extourne && "3".equals(codeSpe)) {
            totalNbInsc1703 = new Integer((totalNbInsc1703.intValue() + nbInsInteger.intValue()));
            totalMontant1703 = totalMontant1703.add(sommeSansCt);
            totalMontantRp1703 = totalMontantRp1703.add(somme);
        }
        if (CIEcriture.CS_CIGENRE_7.equals(genre) && !extourne && "2".equals(codeSpe)) {
            totalNbInsc0702 = new Integer((totalNbInsc0702.intValue() + nbInsInteger.intValue()));
            totalMontant0702 = totalMontant0702.add(sommeSansCt);
            totalMontantRp0702 = totalMontantRp0702.add(somme);
        }
        if (CIEcriture.CS_CIGENRE_7.equals(genre) && extourne && "2".equals(codeSpe)) {
            totalNbInsc1702 = new Integer((totalNbInsc1702.intValue() + nbInsInteger.intValue()));
            totalMontant1702 = totalMontant1702.add(sommeSansCt);
            totalMontantRp1702 = totalMontantRp1702.add(somme);
        }

        return result;

    }

    public String getOutputFile() {
        try {
            // File f = new File("tent_" + JACalendar.today().toStrAMJ() + "_" +
            // Math.round((Math.random() * 10000) % 10000) + ".xls");
            File f = File.createTempFile("stat" + JACalendar.today().toStrAMJ() + "_", ".xls");
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

    public BProcess getProcess() {
        return process;
    }

    /**
     * @return
     */
    public BSession getSession() {
        return session;
    }

    public HSSFSheet populateSheet(BSession session) {

        HSSFCellStyle style = wb.createCellStyle();
        // style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFRow row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        int colNum = 0;
        /********************** Nombre de splitting ***************/
        HSSFCell cell = row.createCell((short) colNum++);

        if (getProcess().isAborted()) {
            return sheet;
        }
        colNum = 0;
        cell = row.createCell((short) colNum++);
        if ("DE".equalsIgnoreCase(getSession().getIdLangueISO())) {
            cell.setCellValue("Anzahl Formulare 'Anmeldung für die Durchführung der Einkommenstellung");
        } else {
            cell.setCellValue("Nombre de dossier de splittings crées");
        }
        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        colNum = 0;
        cell = row.createCell((short) colNum++);
        if ("DE".equalsIgnoreCase(getSession().getIdLangueISO())) {
            cell.setCellValue("im Scheidungsfall' die vom 1.bis 31. Dezember bei Ihrer Kasse als auftraggebende AK eingegangen sind");
        } else {
            cell.setCellValue("en tant que caisse comettante");
        }
        CIDossierSplittingManager splitMgrTot = new CIDossierSplittingManager();
        splitMgrTot.setFromDateOuvertureDossier(debutAnnee);
        splitMgrTot.setUntilDateOuverture(finAnnee);
        splitMgrTot.setSession(session);
        cell = row.createCell((short) colNum++);
        cell.setCellStyle(style);
        cell.setCellValue("301");
        cell = row.createCell((short) colNum++);
        cell.setCellStyle(style);
        int nbDossier = 0;
        try {
            nbDossier = splitMgrTot.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (getProcess().isAborted()) {
            return sheet;
        }
        cell.setCellValue(nbDossier);
        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        colNum = 0;
        cell = row.createCell((short) colNum++);
        if ("DE".equalsIgnoreCase(getSession().getIdLangueISO())) {
            cell.setCellValue("davon :");
        } else {
            cell.setCellValue("dont :");
        }
        /*********************** dont les spéciaux ***********************/
        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        colNum = 0;
        cell = row.createCell((short) colNum++);
        if ("DE".equalsIgnoreCase(getSession().getIdLangueISO())) {
            cell.setCellValue("-  im RentenFall, d.h. der Antrag wurde erst bei...");
        } else {
            cell.setCellValue("- crée pour les rente");
        }
        cell = row.createCell((short) colNum++);
        cell.setCellStyle(style);
        cell.setCellValue("302");
        CIDossierSplittingManager splitRenten = new CIDossierSplittingManager();
        splitRenten.setFromDateOuvertureDossier(debutAnnee);
        splitRenten.setUntilDateOuverture(finAnnee);
        splitRenten.setForMotif(CIDossierSplitting.CS_RENTES);
        splitRenten.setSession(session);
        int nbDossierRente = 0;
        try {
            nbDossierRente = splitRenten.getCount();
        } catch (Exception e) {
        }
        if (getProcess().isAborted()) {
            return sheet;
        }
        cell = row.createCell((short) colNum++);
        cell.setCellStyle(style);
        cell.setCellValue(nbDossierRente);
        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        colNum = 0;
        cell = row.createCell((short) colNum++);
        if ("DE".equalsIgnoreCase(getSession().getIdLangueISO())) {
            cell.setCellValue("- ausserHalb des RentenFalles");
        } else {
            cell.setCellValue("- autre que les rentes");
        }
        cell = row.createCell((short) colNum++);
        cell.setCellValue("303");
        cell.setCellStyle(style);
        cell = row.createCell((short) colNum++);
        cell.setCellStyle(style);
        cell.setCellValue(nbDossier - nbDossierRente);
        /****************** Part bta ************************/
        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        colNum = 0;
        cell = row.createCell((short) colNum++);
        if ("DE".equalsIgnoreCase(getSession().getIdLangueISO())) {
            cell.setCellValue("ANZAHL GELTENDGEMACHTER BETREUUNGSGUTSCHRIFTEN :");
        } else {
            cell.setCellValue("BTA :");
        }

        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        colNum = 0;
        cell = row.createCell((short) colNum++);
        if ("DE".equalsIgnoreCase(getSession().getIdLangueISO())) {
            cell.setCellValue("Anzahl betreuender Pesonnen stand " + debutAnnee);
        } else {
            cell.setCellValue("Nombre de BTA depuis " + debutAnnee);
        }
        cell = row.createCell((short) colNum++);
        cell.setCellValue("304");
        cell.setCellStyle(style);
        CIExtendeEcritureManager ecrMgr = new CIExtendeEcritureManager();
        ecrMgr.setSession(session);
        ecrMgr.setFromDateInscription(debutAnnee);
        ecrMgr.setToDateInscription(finAnnee);
        ecrMgr.setIsBta("True");
        int nbBta = 0;
        try {
            nbBta = ecrMgr.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (getProcess().isAborted()) {
            return sheet;
        }
        cell = row.createCell((short) colNum++);
        cell.setCellValue(nbBta);
        cell.setCellStyle(style);
        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        colNum = 0;
        cell = row.createCell((short) colNum++);
        /************************** IK - Auszüge **************************/
        if ("DE".equalsIgnoreCase(getSession().getIdLangueISO())) {
            cell.setCellValue("IK - Auszüge");
        } else {
            cell.setCellValue("Extraits de CI");
        }
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        HSSFCellStyle styleTitle = wb.createCellStyle();
        styleTitle.setFont(font);
        cell.setCellStyle(styleTitle);
        colNum = 0;
        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        cell = row.createCell((short) colNum++);
        if ("DE".equalsIgnoreCase(getSession().getIdLangueISO())) {
            cell.setCellValue("Anzahl von Versicherten verlangte IK-Auszüge");
        } else {
            cell.setCellValue("Extraits de CI internes");
        }

        cell = row.createCell((short) colNum++);
        cell.setCellValue("059");
        cell.setCellStyle(style);
        cell = row.createCell((short) colNum++);
        // he92Mgr.setForMotif("98");
        // he92Mgr.setIsArchivage(false);
        // int nb98 = 0;
        // try {
        // nb98 = he92Mgr.getCount();
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // he92Mgr.setIsArchivage(true);
        // try{
        // nb98 += he92Mgr.getCount();
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // cell.setCellValue(nb98);

        /***** Il s'agit des 98 internes */
        CIRassemblementOuvertureManager raouMgr = new CIRassemblementOuvertureManager();
        raouMgr.setSession(session);
        raouMgr.setForTypeEnregistrement(CIRassemblementOuverture.CS_EXTRAIT);
        raouMgr.setFromDateOrdre(debutAnnee);
        raouMgr.setUntilDateOrdre(finAnnee);
        int nb98Interne = 0;
        try {
            nb98Interne = raouMgr.getCount();
        } catch (Exception e) {
            nb98Interne = 0;
        }
        if (getProcess().isAborted()) {
            return sheet;
        }
        cell.setCellStyle(style);
        cell.setCellValue(nb98Interne);
        /*************** valeur nb de 98 ************************/

        /*********************************** nb d'entêtes CI *******************************************/
        colNum = 0;
        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        cell = row.createCell((short) colNum++);
        if ("DE".equalsIgnoreCase(getSession().getIdLangueISO())) {
            cell.setCellValue("Anzahl IK per " + finAnnee);
        } else {
            cell.setCellValue("Nombre de CIs au RA au " + finAnnee);
        }
        cell = row.createCell((short) colNum++);
        cell = row.createCell((short) colNum++);
        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
        ciMgr.setSession(session);
        ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        int nbCI = 0;
        try {
            nbCI = ciMgr.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (getProcess().isAborted()) {
            return sheet;
        }
        cell.setCellValue(nbCI);
        colNum = 0;
        /************* nb Cis ouverts */
        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        cell = row.createCell((short) colNum++);
        if ("DE".equalsIgnoreCase(getSession().getIdLangueISO())) {
            cell.setCellValue("Anzahl geöffnete IK per " + finAnnee);
        } else {
            cell.setCellValue("Nombre de CIs ouverts au RA au " + finAnnee);
        }
        cell = row.createCell((short) colNum++);
        cell = row.createCell((short) colNum++);
        ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        ciMgr.setForEtat("1");
        nbCI = 0;
        try {
            nbCI = ciMgr.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cell.setCellValue(nbCI);

        /**
         * Stat pour gre 7 résumé
         * 
         */

        BigDecimal countNbInd = new BigDecimal("0");
        BigDecimal countNbSal = new BigDecimal("0");
        BigDecimal sommePosInd = new BigDecimal("0");
        BigDecimal sommeNegInd = new BigDecimal("0");
        BigDecimal sommePosSal = new BigDecimal("0");
        BigDecimal sommeNegSal = new BigDecimal("0");

        CIEcritureRapideManager somme = new CIEcritureRapideManager();
        somme.setSession(session);
        somme.setForGenre(CIEcriture.CS_CIGENRE_7);
        // somme.setForExtourne("0");
        somme.setForCodeSpecial(CIEcriture.CS_NONFORMATTEUR_INDEPENDANT);
        somme.setFromDateInscription(debutAnnee);
        somme.setToDateInscription(finAnnee);
        somme.setForMoisFin("12");
        try {
            countNbInd = new BigDecimal(somme.getCount());
        } catch (Exception e) {
        }
        if (getProcess().isAborted()) {
            return sheet;
        }
        somme.setForExtourne("0");
        try {
            sommePosInd = somme.getSum("KBMMON");
        } catch (Exception e) {
        }
        if (getProcess().isAborted()) {
            return sheet;
        }
        somme.setForExtourne(CIEcriture.CS_EXTOURNE_1);
        try {
            sommeNegInd = somme.getSum("KBMMON");
        } catch (Exception e) {
        }
        if (getProcess().isAborted()) {
            return sheet;
        }

        somme.setForCodeSpecial(CIEcriture.CS_NONFORMATTEUR_SALARIE);
        somme.setForExtourne("");
        try {
            countNbSal = new BigDecimal(somme.getCount());
        } catch (Exception e) {
        }
        if (getProcess().isAborted()) {
            return sheet;
        }
        somme.setForExtourne("0");
        try {
            sommePosSal = somme.getSum("KBMMON");
        } catch (Exception e) {
        }
        if (getProcess().isAborted()) {
            return sheet;
        }
        somme.setForExtourne(CIEcriture.CS_EXTOURNE_1);
        try {
            sommeNegSal = somme.getSum("KBMMON");
        } catch (Exception e) {
        }
        if (getProcess().isAborted()) {
            return sheet;
        }

        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        colNum = 0;
        cell = row.createCell((short) colNum++);
        if ("DE".equalsIgnoreCase(getSession().getIdLangueISO())) {
            cell.setCellValue("SZ 7 ind");
        } else {
            cell.setCellValue("Nombre genre 7 sal.");
        }
        // ind somme
        cell = row.createCell((short) colNum++);
        cell.setCellValue("018");
        cell.setCellStyle(style);
        cell = row.createCell((short) colNum++);
        cell.setCellValue(countNbSal.toString());
        cell.setCellStyle(style);

        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        colNum = 0;
        cell = row.createCell((short) colNum++);
        if ("DE".equalsIgnoreCase(getSession().getIdLangueISO())) {
            cell.setCellValue("SZ 7 ind");
        } else {
            cell.setCellValue("Montant genre 7 sal.");
        }
        // ind somme
        cell = row.createCell((short) colNum++);
        cell.setCellValue("045");
        cell.setCellStyle(style);
        cell = row.createCell((short) colNum++);
        cell.setCellValue((sommePosSal.subtract(sommeNegSal)).toString());
        cell.setCellStyle(style);
        HSSFFooter footer = sheet.getFooter();
        footer.setRight("Ref. : 0160CCI");
        // Ajout de la référence INFOROM
        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        colNum = 0;
        cell = row.createCell((short) colNum++);
        cell.setCellValue("Ref. : 0160CCI");

        return sheet;

    }

    public HSSFSheet populateSheet2(BSession session) {
        if ("FR".equalsIgnoreCase(getSession().getIdLangueISO())) {
            sz = "Chiffre clé";
            rentner = "Montant total des rentiers";
            sz3 = "Chiffre clé (03)";
            sz0 = "Chiffre clé (00/04)";
            sz2 = "Chiffre clé (02)";

        }
        try {
            String borneInf = "";
            String borneSup = "";
            CIEcritureRapideManager ecrMgr = new CIEcritureRapideManager();
            ecrMgr.setSession(session);
            ecrMgr.setFromDateInscription(debutAnnee);
            ecrMgr.setToDateInscription(finAnnee);
            ecrMgr.setTri("anneeCotisation");
            ecrMgr.setForGenreSixSept("True");
            ecrMgr.changeManagerSize(BManager.SIZE_NOLIMIT);
            try {
                ecrMgr.find();
            } catch (Exception e) {
                e.printStackTrace();
            }
            borneInf = ((CIEcritureRapide) ecrMgr.getFirstEntity()).getAnnee();
            borneSup = ((CIEcritureRapide) ecrMgr.getEntity(ecrMgr.size() - 1)).getAnnee();

            // String borneInf="1999";
            // String borneSup="2001";

            for (int i = Integer.parseInt(borneInf); i <= Integer.parseInt(borneSup); i++) {
                if (getProcess().isAborted()) {
                    return sheet;
                }
                ArrayList resultSix = getElements(session, String.valueOf(i), debutAnnee, finAnnee,
                        CIEcriture.CS_CIGENRE_6, false, "pas");
                ArrayList resultSixExt = getElements(session, String.valueOf(i), debutAnnee, finAnnee,
                        CIEcriture.CS_CIGENRE_6, true, "pas");
                ArrayList resultSept = getElements(session, String.valueOf(i), debutAnnee, finAnnee,
                        CIEcriture.CS_CIGENRE_7, false, "pas");
                ArrayList resultSeptExt = getElements(session, String.valueOf(i), debutAnnee, finAnnee,
                        CIEcriture.CS_CIGENRE_7, true, "pas");
                ArrayList resultSept3 = getElements(session, String.valueOf(i), debutAnnee, finAnnee,
                        CIEcriture.CS_CIGENRE_7, false, "3");
                ArrayList resultSeptExt3 = getElements(session, String.valueOf(i), debutAnnee, finAnnee,
                        CIEcriture.CS_CIGENRE_7, true, "3");
                ArrayList resultSept0 = getElements(session, String.valueOf(i), debutAnnee, finAnnee,
                        CIEcriture.CS_CIGENRE_7, false, "0");
                ArrayList resultSeptExt0 = getElements(session, String.valueOf(i), debutAnnee, finAnnee,
                        CIEcriture.CS_CIGENRE_7, true, "0");
                ArrayList resultSept2 = getElements(session, String.valueOf(i), debutAnnee, finAnnee,
                        CIEcriture.CS_CIGENRE_7, false, "2");
                ArrayList resultSeptExt2 = getElements(session, String.valueOf(i), debutAnnee, finAnnee,
                        CIEcriture.CS_CIGENRE_7, true, "2");

                if ((resultSix == null) && (resultSixExt == null) && (resultSept == null) && (resultSeptExt == null)) {
                    continue;
                }
                ArrayList totalSept = new ArrayList();
                if (null == resultSept) {
                    resultSept = new ArrayList();
                    resultSept.add(new Integer("0"));
                    resultSept.add(new BigDecimal("0"));
                    resultSept.add(new BigDecimal("0"));
                }
                if (null == resultSeptExt) {
                    resultSeptExt = new ArrayList();
                    resultSeptExt.add(new Integer("0"));
                    resultSeptExt.add(new BigDecimal("0"));
                    resultSeptExt.add(new BigDecimal("0"));
                }
                // Calcul du total des 7
                totalSept.add(new Integer(((Integer) resultSept.get(0)).intValue()
                        + ((Integer) resultSeptExt.get(0)).intValue()));
                totalSept.add(((BigDecimal) resultSept.get(2)).subtract((BigDecimal) resultSeptExt.get(2)));
                totalSept.add(((BigDecimal) resultSept.get(1)).subtract((BigDecimal) resultSeptExt.get(1)));
                // Calcul du total entête
                totalNbInscRentner = new Integer(
                        (totalNbInscRentner.intValue() + ((Integer) totalSept.get(0)).intValue()));
                totalMontantRentner = totalMontantRentner.add((BigDecimal) totalSept.get(2));
                totalMontantRpRentner = totalMontantRpRentner.add((BigDecimal) totalSept.get(1));
                fillEntete(totalSept, i);
                fillElements(resultSix, i, "6", "pas");
                fillElements(resultSixExt, i, "16", "pas");
                fillElements(resultSept, i, "7", "pas");
                fillElements(resultSeptExt, i, "17", "pas");
                fillElements(resultSept3, i, "7", "3");
                fillElements(resultSeptExt3, i, "17", "3");
                fillElements(resultSept0, i, "7", "0");
                fillElements(resultSeptExt0, i, "17", "0");
                fillElements(resultSept2, i, "7", "2");
                fillElements(resultSeptExt2, i, "17", "2");

            }
            int colNum = 0;
            ArrayList totalRentnerArr = new ArrayList();
            totalRentnerArr.add(totalNbInscRentner);
            totalRentnerArr.add(totalMontantRpRentner);
            totalRentnerArr.add(totalMontantRentner);
            fillEntete(totalRentnerArr, 0);
            ArrayList total6Arr = new ArrayList();
            total6Arr.add(totalNbInsc06);
            total6Arr.add(totalMontantRp06);
            total6Arr.add(totalMontant06);
            fillElements(total6Arr, 0, "6", "pas");
            ArrayList total16Arr = new ArrayList();
            total16Arr.add(totalNbInsc16);
            total16Arr.add(totalMontantRp16);
            total16Arr.add(totalMontant16);
            fillElements(total16Arr, 0, "16", "pas");
            ArrayList total7Arr = new ArrayList();
            total7Arr.add(totalNbInsc07);
            total7Arr.add(totalMontantRp07);
            total7Arr.add(totalMontant07);
            fillElements(total7Arr, 0, "7", "pas");
            ArrayList total17Arr = new ArrayList();
            total17Arr.add(totalNbInsc17);
            total17Arr.add(totalMontantRp17);
            total17Arr.add(totalMontant17);
            fillElements(total17Arr, 0, "17", "pas");
            ArrayList total7Arr00 = new ArrayList();
            total7Arr00.add(totalNbInsc0700);
            total7Arr00.add(totalMontantRp0700);
            total7Arr00.add(totalMontant0700);
            fillElements(total7Arr00, 0, "7", "0");
            ArrayList total17Arr00 = new ArrayList();
            total17Arr00.add(totalNbInsc1700);
            total17Arr00.add(totalMontantRp1700);
            total17Arr00.add(totalMontant1700);
            fillElements(total17Arr00, 0, "17", "0");
            ArrayList total7Arr03 = new ArrayList();
            total7Arr03.add(totalNbInsc0703);
            total7Arr03.add(totalMontantRp0703);
            total7Arr03.add(totalMontant0703);
            fillElements(total7Arr03, 0, "7", "3");
            ArrayList total17Arr03 = new ArrayList();
            total17Arr03.add(totalNbInsc1703);
            total17Arr03.add(totalMontantRp1703);
            total17Arr03.add(totalMontant1703);
            fillElements(total17Arr03, 0, "17", "3");
            // Codes 2
            ArrayList total7Arr02 = new ArrayList();
            total7Arr02.add(totalNbInsc0702);
            total7Arr02.add(totalMontantRp0702);
            total7Arr02.add(totalMontant0702);
            fillElements(total7Arr02, 0, "7", "2");
            ArrayList total17Arr02 = new ArrayList();
            total17Arr02.add(totalNbInsc1702);
            total17Arr02.add(totalMontantRp1702);
            total17Arr02.add(totalMontant1702);
            fillElements(total17Arr02, 0, "17", "2");
            // Fin de page
            HSSFCellStyle style = wb.createCellStyle();
            HSSFRow row = sheet2.createRow(sheet2.getPhysicalNumberOfRows());
            HSSFCell cell = row.createCell((short) colNum++);
            style.setBorderTop(HSSFCellStyle.BORDER_THICK);
            cell = row.createCell((short) colNum++);
            cell = row.createCell((short) colNum++);
            cell = row.createCell((short) colNum++);
            cell = row.createCell((short) colNum++);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            BigDecimal subTotal7 = totalMontant07.subtract(totalMontant17);
            cell.setCellValue(new FWCurrency(subTotal7.toString()).toStringFormat());
            // Pour faire le total * 10.1
            row = sheet2.createRow(sheet2.getPhysicalNumberOfRows());
            cell = row.createCell((short) colNum++);
            style.setBorderTop(HSSFCellStyle.BORDER_THICK);
            colNum = 0;
            cell = row.createCell((short) colNum++);
            cell = row.createCell((short) colNum++);
            cell = row.createCell((short) colNum++);
            // cell = row.createCell((short) colNum++);
            cell.setCellValue("x 10.1%");
            cell = row.createCell((short) colNum++);
            BigDecimal rate = new BigDecimal("0.101");
            cell.setCellValue(new FWCurrency(subTotal7.multiply(rate).toString()).toStringFormat());
            HSSFFooter footer = sheet2.getFooter();
            footer.setRight("Ref : 0160CCI");
            // Ajout de la référence INFOROM
            row = sheet2.createRow(sheet2.getPhysicalNumberOfRows());
            colNum = 0;
            cell = row.createCell((short) colNum++);
            cell.setCellValue("Réf. : 0160CCI");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sheet2;
    }

    public void setDocumentInfo(JadePublishDocumentInfo documentInfo) {
        this.documentInfo = documentInfo;
    }

    public void setProcess(BProcess process) {
        this.process = process;
    }

    /**
     * @param session
     */
    public void setSession(BSession session) {
        this.session = session;
    }

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
        if ("DE".equalsIgnoreCase(getSession().getIdLangueISO())) {
            for (int i = 0; i < CIStatDocument.COL_TITLES.length; i++) {
                // set cell value
                HSSFCell c = row.createCell((short) i);
                c.setCellValue(CIStatDocument.COL_TITLES[i]);
                c.setCellStyle(style);
            }
        } else {
            for (int i = 0; i < CIStatDocument.COL_TITLES_FR.length; i++) {
                // set cell value
                HSSFCell c = row.createCell((short) i);
                c.setCellValue(CIStatDocument.COL_TITLES_FR[i]);
                c.setCellStyle(style);
            }

        }
        return sheet;
    }

    private HSSFSheet setTitleRow2(HSSFWorkbook wb, HSSFSheet sheet) {
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
        if ("DE".equalsIgnoreCase(getSession().getIdLangueISO())) {

            for (int i = 0; i < CIStatDocument.COL_TITLES2.length; i++) {
                // set cell value
                HSSFCell c = row.createCell((short) i);
                c.setCellValue(CIStatDocument.COL_TITLES2[i]);
                c.setCellStyle(style);
            }
        } else {
            for (int i = 0; i < CIStatDocument.COL_TITLES2_FR.length; i++) {
                // set cell value
                HSSFCell c = row.createCell((short) i);
                c.setCellValue(CIStatDocument.COL_TITLES2_FR[i]);
                c.setCellStyle(style);
            }

        }
        return sheet;
    }

}
