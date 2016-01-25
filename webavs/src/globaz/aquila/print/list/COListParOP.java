/*
 * Cr�� le 8 f�vr. 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.aquila.print.list;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.application.COApplication;
import globaz.aquila.service.COServiceLocator;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.external.IntAdresseCourrier;
import globaz.osiris.external.IntTiers;
import globaz.pyxis.api.osiris.TITiersOSI;
import globaz.pyxis.db.tiers.TIAdministrationAdresse;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * <H1>Description</H1>
 * <p>
 * Un document qui regroupe les �tapes du contentieux par office de poursuite.
 * </p>
 * <p>
 * La gestion se fait en deux parties, la premi�re consiste en l'ajout des �taptes ex�cut�es dans le process du
 * contentieux gr�ce � la m�thode {@link #insertRow(CASection, String, String, String) insertRow}.
 * </p>
 * <p>
 * La cr�ation du document en lui-m�me se fait dans un deuxi�me temps, lors de l'ex�cution de celui-ci, dans la m�thode
 * {@link #_bindDataTable() bindDataTable}.
 * </p>
 * 
 * @author vre
 */
public class COListParOP extends FWIAbstractManagerDocumentList {

    // ~ Inner Classes
    // --------------------------------------------------------------------------------------------------
    // TotauxParOffice
    private class SummaryContentieux {

        IntTiers offPours;
        FWCurrency totalFraisOffice = new FWCurrency();
        FWCurrency totalMontantOffice = new FWCurrency();
        // ~ Instance fields
        // --------------------------------------------------------------------------------------------
        int totalParOffice = 0;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Cr�e une nouvelle instance de la classe SummaryContentieux.
         */
        public SummaryContentieux() {
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * getter pour l'attribut id off pours.
         * 
         * @return la valeur courante de l'attribut id off pours
         */
        public String getIdOffPours() {
            return (offPours != null) ? offPours.getIdTiers() : "0";
        }
    }

    private static final String FILE_NAME = "ListPoursuiteParOffice";
    // ~ Static fields/initializers
    protected static final String FORMAT_MONTANT = "#,##0.00";
    private static final int MAX_LENGTH_EXCEL_SHEET_NAME = 30;
    public static final String NUMERO_REFERENCE_INFOROM = "0025GCO";

    private static final long serialVersionUID = -995542187720935706L;
    private String[] col_titles;
    private String currIdOffPours = "";
    private String date = new String();
    private String dateReference = "";
    // Le format doit �tre global s'il y a un grand nombre de donn�es
    private HSSFDataFormat format;
    // ~ Instance fields
    private String lastDebiteur = "";
    private ArrayList listOffices = new ArrayList();

    private ArrayList listTotauxParOffice = new ArrayList();
    private boolean modePrevisionnel = false;
    private HSSFPrintSetup ps;
    private String selectionTriListeCA = "";
    private String selectionTriListeSection = "";
    private HSSFCellStyle styleMontant;
    private HSSFCellStyle styleTexte;
    private String titreDoc;
    private FWCurrency totalFrais = new FWCurrency();
    // Donn�es pour la feuille Excel
    private int totalGeneral = 0;
    private FWCurrency totalMontant = new FWCurrency();

    // Feuille Excel
    private HSSFWorkbook wb;

    // ~ Constructors
    /**
     * Cr�e une nouvelle instance de la classe COListParOP.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public COListParOP() throws Exception {
        super(new BSession(ICOApplication.DEFAULT_APPLICATION_AQUILA), COApplication.APPLICATION_AQUILA_ROOT, "globaz",
                "Liste par OP", null, ICOApplication.DEFAULT_APPLICATION_AQUILA);
        _setDocumentTitle(getSession().getLabel("AQUILA_LISTE_OP"));

        // Titre des colonnes
        col_titles = new String[5];
        col_titles[0] = getSession().getLabel("CONT_POU_DEBITEUR");
        col_titles[1] = getSession().getLabel("CONT_POU_FACTURE");
        col_titles[2] = getSession().getLabel("CONT_POU_MONTANT");
        col_titles[3] = getSession().getLabel("CONT_POU_AVANCE_FRAIS");
        // col_titles[4] = "Etape"; //getSession().getLabel("CONT_POU_ETAPE");

        // Classeur Excel et styles
        wb = new HSSFWorkbook();
        format = wb.createDataFormat();
        styleTexte = wb.createCellStyle();
        styleMontant = wb.createCellStyle();

        _createDocumentInfo();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe COListParOP.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public COListParOP(BProcess parent) throws Exception {
        this();
        setParent(parent);
    }

    /**
     * @param row
     * @param colNum
     * @param montant
     * @param style
     */
    protected void addExcelCell(HSSFRow row, short colNum, double montant, HSSFCellStyle style) {
        HSSFCell cell;
        if (row != null) {
            cell = row.createCell(colNum);
            if (style != null) {
                cell.setCellStyle(style);
            }
            cell.setCellValue(montant);
        }
    }

    /**
     * @param row
     * @param colNum
     * @param texte
     * @param style
     */
    protected void addExcelCell(HSSFRow row, short colNum, String texte, HSSFCellStyle style) {
        HSSFCell cell;

        if (row != null) {
            cell = row.createCell(colNum);
            if (style != null) {
                cell.setCellStyle(style);
            }
            cell.setCellValue(texte);
        }
    }

    /**
     * @param sheet
     * @param libelle
     * @param facture
     * @param montant
     * @param frais
     */
    protected void addExcelRowData(HSSFSheet sheet, String libelle, String facture, double montant, double frais) {
        HSSFRow row;

        styleTexte = setStyleAlignLeftWrap();
        styleMontant = setStyleMontant();

        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        int colNum = 0;

        this.addExcelCell(row, (short) colNum++, libelle, styleTexte);
        this.addExcelCell(row, (short) colNum++, facture, styleTexte);
        this.addExcelCell(row, (short) colNum++, montant, styleMontant);
        this.addExcelCell(row, (short) colNum++, frais, styleMontant);
    }

    /**
     * @param sheet
     */
    protected void addExcelRowTitle(HSSFSheet sheet, String totalLibelle, String totalFacture, double totalMontant,
            double totalFrais) {
        HSSFRow row;

        // D�finition de la police
        HSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold

        // D�finition des styles
        HSSFCellStyle styleTotalGenText = wb.createCellStyle();
        styleTotalGenText.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        styleTotalGenText.setWrapText(true);
        styleTotalGenText.setFont(font);

        HSSFCellStyle styleTotalGenMontant = wb.createCellStyle();
        styleTotalGenMontant.setDataFormat(format.getFormat(COListParOP.FORMAT_MONTANT));
        styleTotalGenMontant.setFont(font);

        row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        int colNum = 0;

        this.addExcelCell(row, (short) colNum++, totalLibelle, styleTotalGenText);
        this.addExcelCell(row, (short) colNum++, totalFacture, styleTotalGenText);
        this.addExcelCell(row, (short) colNum++, totalMontant, styleTotalGenMontant);
        this.addExcelCell(row, (short) colNum++, totalFrais, styleTotalGenMontant);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList #addRow(globaz.globall.db.BEntity)
     */
    @Override
    protected void addRow(BEntity entity) throws FWIException {
        // Fait rien
    }

    /**
     * @param section
     * @return le code de l'administration d'OP
     */
    private String getCodeAdministration(CASection section) {
        // on r�cup�re le tiers par la section
        IntTiers tiers = section.getCompteAnnexe().getTiers();

        if (tiers == null) {
            return "";
        } else {
            TITiersOSI osiTiers = new TITiersOSI();
            TIAdministrationAdresse code = osiTiers.getTIAdministrationAdresse(tiers, section);

            if (!JadeStringUtil.isBlankOrZero(code.getCantonAdministration())) {

                return code.getCodeAdministration();
            }

            return "";
        }
    }

    /**
     * getter pour l'attribut date.
     * 
     * @return la valeur courante de l'attribut date
     */
    public String getDate() {
        return date;
    }

    /**
     * getter pour l'attribut date reference.
     * 
     * @return la valeur courante de l'attribut date reference
     */
    public String getDateReference() {
        return dateReference;
    }

    /**
     * Retourne le libell� de l'office des poursuites Cr�� le : 12 sept. 06
     * 
     * @param section
     * @return le libell� de l'Office des poursuites
     * @throws Exception
     */
    private String getLibelleOfficeDesPoursuites(CASection section, boolean titre) throws COListParOPException {
        String res = "";

        SummaryContentieux sc = getSummaryContentieux(section);
        IntTiers op = sc.offPours;

        // ajouter l'en-tete de l'OP si on commence le doc ou si on traite un
        // autre OP que le summary pr�c�dent
        if (!currIdOffPours.equals(sc.getIdOffPours())) {
            if ((op != null) && !JadeStringUtil.isBlank(op.getNom())) {
                res = op.getNom();
            } else {
                res = getSession().getLabel("CONT_POU_OFF_NON_DET");
            }
        }
        return res;
    }

    /**
     * Retourne le num�ro de la commune OFS<br>
     * Cr�� le : 12 sept. 06
     * 
     * @param section
     * @return le num�ro de la commune OFS
     */
    private String getNumOfficeDesPoursuite(CASection section) {
        IntTiers tiers = section.getCompteAnnexe().getTiers();

        if (tiers == null) {
            return null;
        } else {
            if (tiers.getAdresseCourrier(IntAdresseCourrier.POURSUITE) == null) {
                return null;
            } else {
                return tiers.getAdresseCourrier(IntAdresseCourrier.POURSUITE).getNumCommuneOfs();
            }
        }
    }

    /**
     * G�n�re le fichier Excel Cr�� le : 17 ao�t 06
     * 
     * @return le chemin absolu du fichier
     */
    public String getOutputFile() {
        try {
            File f = File.createTempFile(COListParOP.FILE_NAME + JACalendar.today().toStrAMJ() + "_", ".xls");
            f.deleteOnExit();
            FileOutputStream out = new FileOutputStream(f);
            wb.write(out);
            out.close();
            return f.getAbsolutePath();
        } catch (Exception e) {
            JadeCodingUtil.catchException(this, "doAction", e);
            return "";
        }
    }

    /**
     * getter pour l'attribut selection tri liste CA.
     * 
     * @return la valeur courante de l'attribut selection tri liste CA
     */
    public String getSelectionTriListeCA() {
        return selectionTriListeCA;
    }

    /**
     * getter pour l'attribut selection tri liste section.
     * 
     * @return la valeur courante de l'attribut selection tri liste section
     */
    public String getSelectionTriListeSection() {
        return selectionTriListeSection;
    }

    /**
     * Test si la feuille � d�j� �t� cr��e pour cette office
     * 
     * @author: sel Cr�� le : 12 sept. 06
     * @param section
     * @param idOffPours
     * @return Sheet
     * @throws Exception
     */
    private HSSFSheet getSheet(CASection section, String idOffPours) throws COListParOPException {
        HSSFSheet sheet = null;
        HSSFRow row;

        try {
            // Test si la feuille � d�j� �t� cr��e pour cette office
            if (listOffices.contains(currIdOffPours)) {
                // On utilise la feuille existante
                sheet = wb.getSheetAt(listOffices.indexOf(currIdOffPours));
            } else {
                // On cr��e une nouvelle feuille correspondante
                listTotauxParOffice.add(new SummaryContentieux());

                String libelleOffice = getCodeAdministration(section);
                // si on n'a pas de code d'administration, on cr�er une feuille avec un nom par d�faut
                if (JadeStringUtil.isBlankOrZero(libelleOffice)) {
                    sheet = wb.createSheet();
                } else {
                    if (libelleOffice.length() > COListParOP.MAX_LENGTH_EXCEL_SHEET_NAME) {
                        libelleOffice = libelleOffice.substring(0, COListParOP.MAX_LENGTH_EXCEL_SHEET_NAME);
                    }
                    sheet = wb.createSheet(libelleOffice);
                }
                initSheet(sheet);

                // Ajoute � la liste
                listOffices.add(currIdOffPours);

                row = sheet.createRow(sheet.getPhysicalNumberOfRows());
                this.addExcelCell(row, (short) 0, getLibelleOfficeDesPoursuites(section, true), null);
                // Ajoute 2 lignes vide
                sheet.createRow(sheet.getPhysicalNumberOfRows());
                sheet.createRow(sheet.getPhysicalNumberOfRows());

                // Cr�ation de la ligne avec les ent�tes de colonnes
                setTitleRow(wb, sheet);
            }
        } catch (Exception e) {
            throw new COListParOPException("Error getSheet : " + e.toString());
        }
        return sheet;
    }

    /**
     * @param section
     * @return
     * @throws Exception
     */
    private SummaryContentieux getSummaryContentieux(CASection section) throws COListParOPException {
        // ajouter cette �tape dans la liste en encapsulant les donn�es dans une
        // instance de SummaryContentieux
        SummaryContentieux sc = new SummaryContentieux();
        try {
            CACompteAnnexe compte = (CACompteAnnexe) section.getCompteAnnexe();
            sc.offPours = COServiceLocator.getTiersService().getOfficePoursuite(getSession(), compte.getTiers(),
                    compte.getIdExterneRole());
        } catch (Exception e) {
            throw new COListParOPException("Error getSummaryContentieux : " + e.toString());
        }

        return sc;
    }

    /** Initialisation des colonnes et des groupes. */
    @Override
    protected void initializeTable() {
    }

    /**
     * Initialise une feuille Excel
     * 
     * @author: sel Cr�� le : 17 ao�t 06
     * @param sheet
     */
    protected void initSheet(HSSFSheet sheet) {
        // Titre de la liste
        titreDoc = getSession().getLabel("CONT_POU_LISTE");
        setHeader(sheet);
        setFooter(sheet);
        // Mise en page en landscape
        setPrintSetUp(sheet, true);

        // Largeur des colonnes
        sheet.setColumnWidth((short) 0, (short) 13000); // D�biteur
        sheet.setColumnWidth((short) 1, (short) 11500); // Facture
        sheet.setColumnWidth((short) 2, (short) 3000); // Montant
        sheet.setColumnWidth((short) 3, (short) 3000); // Avance de frais
    }

    /**
     * Ajoute une ligne au document Cr�� le : 27.06.2002 Modifi� le : 23 ao�t 06
     * 
     * @param section
     *            globaz.osiris.db.comptes.CASection
     * @param date
     *            String
     * @param montant
     *            String
     * @param frais
     *            String
     * @throws Exception
     */
    public void insertRow(CASection section, String date, String montant, String frais) throws COListParOPException {
        // Compte annexe
        CACompteAnnexe _compte = (CACompteAnnexe) section.getCompteAnnexe();
        String debiteur = _compte.getIdExterneRole() + " " + _compte.getDescription() + ", "
                + _compte.getTiers().getLieu();

        String idOffPours = "";
        if (getNumOfficeDesPoursuite(section) != null) {
            idOffPours = getLibelleOfficeDesPoursuites(section, false);
        }

        // Section
        String facture = section.getIdExterne() + " " + section.getDescription();
        // Frais
        FWCurrency totalFrais = new FWCurrency(frais);
        // Montant
        FWCurrency totalMontant = new FWCurrency(montant);
        // Etape
        // String sEtape = etape.getEtape().getDescription();

        // Mise � jour des totaux g�n�raux
        this.totalFrais.add(totalFrais);
        this.totalMontant.add(totalMontant);

        // Mise � jour de l'Id courant
        currIdOffPours = idOffPours;

        // D�biteur
        String libelleDebiteur = "";
        if (!lastDebiteur.equalsIgnoreCase(debiteur)) {
            libelleDebiteur = debiteur;
        }
        addExcelRowData(getSheet(section, idOffPours), libelleDebiteur, facture, totalMontant.doubleValue(),
                totalFrais.doubleValue());

        // Mise � jour des totaux par office
        ((SummaryContentieux) listTotauxParOffice.get(listOffices.indexOf(currIdOffPours))).totalParOffice++;
        ((SummaryContentieux) listTotauxParOffice.get(listOffices.indexOf(currIdOffPours))).totalFraisOffice
                .add(totalFrais);
        ((SummaryContentieux) listTotauxParOffice.get(listOffices.indexOf(currIdOffPours))).totalMontantOffice
                .add(totalMontant);
        totalGeneral++;
        lastDebiteur = debiteur;
    }

    /**
     * getter pour l'attribut mode previsionnel.
     * 
     * @return la valeur courante de l'attribut mode previsionnel
     */
    public boolean isModePrevisionnel() {
        return modePrevisionnel;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return null;
    }

    /**
     * setter pour l'attribut date.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDate(String string) {
        date = string;
    }

    /**
     * setter pour l'attribut date reference.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateReference(String string) {
        dateReference = string;
    }

    /**
     * G�n�re les totaux.
     * 
     * @author: sel Cr�� le : 23 ao�t 06
     */
    public void setFinalizeSheet() {
        HSSFSheet sheet;

        for (int i = 0; i < listOffices.size(); i++) {
            sheet = wb.getSheetAt(i);
            SummaryContentieux totOffice = (SummaryContentieux) listTotauxParOffice.get(i);
            // Totale derni�re feuille
            // Champs de total par Office
            addExcelRowTitle(sheet, "* " + getSession().getLabel("CONT_POU_TOT_OFF"),
                    String.valueOf(totOffice.totalParOffice), totOffice.totalMontantOffice.doubleValue(),
                    totOffice.totalFraisOffice.doubleValue());
            // Initialisation des totaux par office
            totOffice = null;
            // Champs de total
            addExcelRowTitle(sheet, "* " + getSession().getLabel("CONT_POU_TOT_GEN"), String.valueOf(totalGeneral),
                    totalMontant.doubleValue(), totalFrais.doubleValue());
        }
    }

    /**
     * Cr�� le : 23 ao�t 06
     * 
     * @param sheet
     * @return HSSFFooter
     */
    private HSSFFooter setFooter(HSSFSheet sheet) {
        HSSFFooter footer = sheet.getFooter();
        footer.setRight(getSession().getLabel("PAGE") + HSSFFooter.page() + "/" + HSSFFooter.numPages());
        footer.setLeft(getSession().getLabel("CACPAGE") + " : " + COListParOP.NUMERO_REFERENCE_INFOROM + " - "
                + this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.') + 1) + " - "
                + JACalendar.todayJJsMMsAAAA() + " - " + getSession().getUserName());
        return footer;
    }

    /**
     * Cr�� le : 17 ao�t 06
     * 
     * @param sheet
     * @return HSSFHeader
     */
    private HSSFHeader setHeader(HSSFSheet sheet) {
        HSSFHeader header = sheet.getHeader();
        header.setLeft(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        header.setRight(titreDoc);
        return header;
    }

    /**
     * setter pour l'attribut mode previsionnel.
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setModePrevisionnel(boolean b) {
        modePrevisionnel = b;
    }

    /**
     * Cr�� le : 17 ao�t 06
     * 
     * @param sheet
     * @param landscapeTrue
     * @return HSSFPrintSetup
     */
    private HSSFPrintSetup setPrintSetUp(HSSFSheet sheet, boolean landscapeTrue) {
        ps = sheet.getPrintSetup();
        ps.setLandscape(landscapeTrue);

        // format
        ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // marges en-t�te/pied de page
        ps.setHeaderMargin(0);
        ps.setFooterMargin(0);

        return ps;
    }

    /**
     * setter pour l'attribut selection tri liste CA.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setSelectionTriListeCA(String string) {
        selectionTriListeCA = string;
    }

    /**
     * setter pour l'attribut selection tri liste section.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setSelectionTriListeSection(String string) {
        selectionTriListeSection = string;
    }

    /**
     * alignement � gauche avec WrapText
     * 
     * @return
     */
    private HSSFCellStyle setStyleAlignLeftWrap() {
        styleTexte.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        styleTexte.setWrapText(true);
        return styleTexte;
    }

    /**
     * alignement � droite et affichage au format #,##0.00
     * 
     * @return
     */
    private HSSFCellStyle setStyleMontant() {
        styleMontant.setDataFormat(format.getFormat(COListParOP.FORMAT_MONTANT));
        styleMontant.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        styleMontant.setDataFormat(format.getFormat(COListParOP.FORMAT_MONTANT));
        return styleMontant;
    }

    /**
     * Method setTitleRow. Permet de d�finir l'ent�te des colonnes.
     * 
     * @param wb
     * @param sheet
     * @return HSSFSheet
     */
    private HSSFSheet setTitleRow(HSSFWorkbook wb, HSSFSheet sheet) {
        HSSFRow row = null;
        // D�finition de la police
        HSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold

        // D�finition du style align� au centre
        HSSFCellStyle styleLeft = wb.createCellStyle();
        HSSFCellStyle styleRight = wb.createCellStyle();
        styleRight.setWrapText(true); // La largeur de la cellule s'adapte au
        // contenu
        styleLeft.setFont(font);
        styleRight.setFont(font);
        styleRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

        // create Title Row
        row = sheet.createRow(sheet.getPhysicalNumberOfRows());

        this.addExcelCell(row, (short) 0, col_titles[0], styleLeft);
        this.addExcelCell(row, (short) 1, col_titles[1], styleLeft);

        this.addExcelCell(row, (short) 2, col_titles[2], styleRight);
        this.addExcelCell(row, (short) 3, col_titles[3], styleRight);

        return sheet;
    }

}
