/**
 *
 */
package globaz.osiris.print.list;

import globaz.globall.db.BSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * Classeur Excel contenant les statistiques OFAS du contentieux (ancien et nouveau)
 * 
 * @author SEL
 */
public class CAListStatOfas extends CAAbstractListExcel {
    private static final String NUMERO_REFERENCE_INFOROM = "0232GCA";

    private String annee = null;
    private BigDecimal ardMontant = null;
    private int ardNombre = 0;
    private BigDecimal cotiAc = null;

    private BigDecimal cotiPar = null;
    private BigDecimal cotiPers = null;

    private BigDecimal rcArdMontant = null;
    private int rcArdNombre = 0;
    private BigDecimal rcAvsMontant = null;
    private int rcAvsNombre = 0;
    private BigDecimal rpArdMontant = null;
    private int rpArdNombre = 0;
    private BigDecimal rpAvsMontant = null;
    private int rpAvsNombre = 0;

    private BigDecimal sursisMontant = null;
    private int sursisNombre = 0;

    // créé la feuille xls
    public CAListStatOfas(BSession session) {
        super(session, "ListStatOFAS", "Statistiques OFAS");
    }

    /**
     * @return the annee
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * @return the ardMontant
     */
    public BigDecimal getArdMontant() {
        return ardMontant;
    }

    /**
     * @return the ardNombre
     */
    public int getArdNombre() {
        return ardNombre;
    }

    /**
     * @return the cotiAc
     */
    public BigDecimal getCotiAc() {
        return cotiAc;
    }

    /**
     * @return the cotiPar
     */
    public BigDecimal getCotiPar() {
        return cotiPar;
    }

    /**
     * @return the cotiPers
     */
    public BigDecimal getCotiPers() {
        return cotiPers;
    }

    @Override
    public String getNumeroInforom() {
        return CAListStatOfas.NUMERO_REFERENCE_INFOROM;
    }

    /**
     * @return the rcArdMontant
     */
    public BigDecimal getRcArdMontant() {
        return rcArdMontant;
    }

    /**
     * @return the rcArdNombre
     */
    public int getRcArdNombre() {
        return rcArdNombre;
    }

    /**
     * @return the rcAvsMontant
     */
    public BigDecimal getRcAvsMontant() {
        return rcAvsMontant;
    }

    /**
     * @return the rcAvsNombre
     */
    public int getRcAvsNombre() {
        return rcAvsNombre;
    }

    /**
     * @return the rpArdMontant
     */
    public BigDecimal getRpArdMontant() {
        return rpArdMontant;
    }

    /**
     * @return the rpArdNombre
     */
    public int getRpArdNombre() {
        return rpArdNombre;
    }

    /**
     * @return the rpAvsMontant
     */
    public BigDecimal getRpAvsMontant() {
        return rpAvsMontant;
    }

    /**
     * @return the rpAvsNombre
     */
    public int getRpAvsNombre() {
        return rpAvsNombre;
    }

    /**
     * @return the sursisMontant
     */
    public BigDecimal getSursisMontant() {
        return sursisMontant;
    }

    /**
     * @return the sursisNombre
     */
    public int getSursisNombre() {
        return sursisNombre;
    }

    /**
     * Imprime les critères d'execution
     * 
     * @param dateValeur
     */
    private void initCritere(String dateValeur) {
        // Titre
        createRow();
        this.createCell(getSession().getLabel("STATOFAS_CRITERE_ANNEE") + " : ", getStyleCritereTitle());
        this.createCell(dateValeur, getStyleCritere());
    }

    /**
     * Initialise la feuille Excel selon des critères par défaut
     */
    private void initSheet(String sheetName) {
        createSheet(sheetName);
        initCritere(annee);
        initPage(true);
        createHeader();
        createFooter(CAListStatOfas.NUMERO_REFERENCE_INFOROM);
    }

    /**
     * Création des différentes feuilles Excel.
     * 
     * @return la dernière feuille Excel créée.
     */
    public HSSFSheet populateSheetListe() {
        sheetCotisation();

        sheetSursisAuPaiement();

        sheetRequisition();

        sheetArd();

        return getCurrentSheet();
    }

    /**
     * Affiche le libellé
     */
    private void printLibelle(String libelle) {
        createRow();
        createRow();
        this.createCell(libelle, getStyleCritere());
        createRow();
        createRow();
    }

    /**
     * Affiche l'entête des colonnes pour les statistiques des réquisitions
     */
    private void printTableHeader(boolean afficher) {
        this.createCell(getSession().getLabel("STATOFAS_COLUMN_LIBELLE"), getStyleListTitleLeft());
        this.createCell(getSession().getLabel("STATOFAS_COLUMN_CODE"), getStyleListTitleLeft());
        this.createCell(getSession().getLabel("STATOFAS_COLUMN_AVS"), getStyleListTitleLeft());
        if (afficher) {
            this.createCell(getSession().getLabel("STATOFAS_COLUMN_ARD"), getStyleListTitleLeft());
            this.createCell(getSession().getLabel("STATOFAS_COLUMN_TOTAL"), getStyleListTitleLeft());
        }
    }

    /**
     * @param annee
     *            the annee to set
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    /**
     * @param ardMontant
     *            the ardMontant to set
     */
    public void setArdMontant(BigDecimal ardMontant) {
        this.ardMontant = ardMontant;
    }

    /**
     * @param ardNombre
     *            the ardNombre to set
     */
    public void setArdNombre(int ardNombre) {
        this.ardNombre = ardNombre;
    }

    /**
     * @param cotiAc
     *            the cotiAc to set
     */
    public void setCotiAc(BigDecimal cotiAc) {
        this.cotiAc = cotiAc;
    }

    /**
     * @param cotiPar
     *            the cotiPar to set
     */
    public void setCotiPar(BigDecimal cotiPar) {
        this.cotiPar = cotiPar;
    }

    /**
     * @param cotiPers
     *            the cotiPers to set
     */
    public void setCotiPers(BigDecimal cotiPers) {
        this.cotiPers = cotiPers;
    }

    /**
     * @param rcArdMontant
     *            the rcArdMontant to set
     */
    public void setRcArdMontant(BigDecimal rcArdMontant) {
        this.rcArdMontant = rcArdMontant;
    }

    /**
     * @param rcArdNombre
     *            the rcArdNombre to set
     */
    public void setRcArdNombre(int rcArdNombre) {
        this.rcArdNombre = rcArdNombre;
    }

    /**
     * @param rcAvsMontant
     *            the rcAvsMontant to set
     */
    public void setRcAvsMontant(BigDecimal rcAvsMontant) {
        this.rcAvsMontant = rcAvsMontant;
    }

    /**
     * @param rcAvsNombre
     *            the rcAvsNombre to set
     */
    public void setRcAvsNombre(int rcAvsNombre) {
        this.rcAvsNombre = rcAvsNombre;
    }

    /**
     * @param rpArdMontant
     *            the rpArdMontant to set
     */
    public void setRpArdMontant(BigDecimal rpArdMontant) {
        this.rpArdMontant = rpArdMontant;
    }

    /**
     * @param rpArdNombre
     *            the rpArdNombre to set
     */
    public void setRpArdNombre(int rpArdNombre) {
        this.rpArdNombre = rpArdNombre;
    }

    /**
     * @param rpAvsMontant
     *            the rpAvsMontant to set
     */
    public void setRpAvsMontant(BigDecimal rpAvsMontant) {
        this.rpAvsMontant = rpAvsMontant;
    }

    /**
     * @param rpAvsNombre
     *            the rpAvsNombre to set
     */
    public void setRpAvsNombre(int rpAvsNombre) {
        this.rpAvsNombre = rpAvsNombre;
    }

    /**
     * @param sursisMontant
     *            the sursisMontant to set
     */
    public void setSursisMontant(BigDecimal sursisMontant) {
        this.sursisMontant = sursisMontant;
    }

    /**
     * @param sursisNombre
     *            the sursisNombre to set
     */
    public void setSursisNombre(int sursisNombre) {
        this.sursisNombre = sursisNombre;
    }

    /**
     * Feuille contenant les statistiques pour les ARD (713 et 714)
     */
    private void sheetArd() {
        int numCol = 0;
        initSheet(getSession().getLabel("STATOFAS_SHEET_ARD"));

        // définition de la taille des cell
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        numCol++;
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_5500);

        createRow();
        printLibelle(getSession().getLabel("STATOFAS_LIBELLE_ARD") + " " + getAnnee());

        this.createCell(getSession().getLabel("STATOFAS_MONTANT"), getStyleListLeft());
        this.createCell("713", getStyleListLeft());
        this.createCell(getArdMontant().doubleValue(), getStyleMontant());
        createRow();
        this.createCell(getSession().getLabel("STATOFAS_NOMBRE"), getStyleListLeft());
        this.createCell("714", getStyleListLeft());
        this.createCell(getArdNombre(), getStyleListRight());
    }

    /**
     * Feuille contenant la comptabilisation des cotisations
     */
    private void sheetCotisation() {
        int numCol = 0;
        // Titres des colonnes
        ArrayList colTitles = new ArrayList();
        colTitles.add(new ParamTitle(getSession().getLabel("STATOFAS_COMPTE")));
        colTitles.add(new ParamTitle(getSession().getLabel("STATOFAS_MIO_FRANCS")));

        // création de la page
        initSheet(getSession().getLabel("STATOFAS_SHEET_COTISATION"));

        // définition de la taille des cell
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_5500);

        printLibelle(getSession().getLabel("STATOFAS_LIBELLE_COTISATION") + " " + getAnnee());

        initTitleRow(colTitles);

        createRow();
        this.createCell(getSession().getLabel("STATOFAS_LIBELLE_COT_PERS"), getStyleListLeft());
        this.createCell(getCotiPers().divide(new BigDecimal(1000000), 0).toString(), getStyleMontant());

        createRow();
        this.createCell(getSession().getLabel("STATOFAS_LIBELLE_COT_PAR"), getStyleListLeft());
        this.createCell(getCotiPar().divide(new BigDecimal(1000000), 0).toString(), getStyleMontant());

        createRow();
        this.createCell(getSession().getLabel("STATOFAS_LIBELLE_COT_AC"), getStyleListLeft());
        this.createCell(getCotiAc().divide(new BigDecimal(1000000), 0).toString(), getStyleMontant());
    }

    /**
     * Feuille contenant les statistiques des réquisitions (57, 58, 710 et 711)
     */
    private void sheetRequisition() {
        int numCol = 0;
        initSheet(getSession().getLabel("STATOFAS_SHEET_REQUISITION"));

        // définition de la taille des cell
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        numCol++;
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_5500);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_5500);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_5500);

        printLibelle(getSession().getLabel("STATOFAS_LIBELLE_RP_AVS") + " " + getAnnee());
        printTableHeader(getRpArdMontant() != null);

        createRow();
        this.createCell(getSession().getLabel("STATOFAS_MONTANT"), getStyleListLeft());
        this.createCell("058", getStyleListLeft());
        this.createCell(getRpAvsMontant().doubleValue(), getStyleMontant());
        if (getRpArdMontant() != null) {
            this.createCell(getRpArdMontant().doubleValue(), getStyleMontant());
            this.createCell(getRpAvsMontant().doubleValue() + getRpArdMontant().doubleValue(), getStyleMontant());
        }
        createRow();
        this.createCell(getSession().getLabel("STATOFAS_NOMBRE"), getStyleListLeft());
        this.createCell("057", getStyleListLeft());
        this.createCell(getRpAvsNombre(), getStyleListRight());
        if (getRpArdMontant() != null) {
            this.createCell(getRpArdNombre(), getStyleListRight());
            this.createCell(getRpAvsNombre() + getRpArdNombre(), getStyleListRight());
        }

        createRow();

        printLibelle(getSession().getLabel("STATOFAS_LIBELLE_RCP_AVS") + " " + getAnnee());
        printTableHeader(getRcArdMontant() != null);

        createRow();
        this.createCell(getSession().getLabel("STATOFAS_MONTANT"), getStyleListLeft());
        this.createCell("710", getStyleListLeft());
        this.createCell(getRcAvsMontant().doubleValue(), getStyleMontant());
        if (getRcArdMontant() != null) {
            this.createCell(getRcArdMontant().doubleValue(), getStyleMontant());
            this.createCell(getRcAvsMontant().doubleValue() + getRcArdMontant().doubleValue(), getStyleMontant());
        }
        createRow();
        this.createCell(getSession().getLabel("STATOFAS_NOMBRE"), getStyleListLeft());
        this.createCell("711", getStyleListLeft());
        this.createCell(getRcAvsNombre(), getStyleListRight());
        if (getRcArdMontant() != null) {
            this.createCell(getRcArdNombre(), getStyleListRight());
            this.createCell(getRcAvsNombre() + getRcArdNombre(), getStyleListRight());
        }
    }

    /**
     * Feuille contenant les statistiques des sursis au paiement (55 et 56)
     */
    private void sheetSursisAuPaiement() {
        int numCol = 0;
        initSheet(getSession().getLabel("STATOFAS_SHEET_SURSIS"));

        // définition de la taille des cell
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        numCol++;
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_5500);

        createRow();
        printLibelle(getSession().getLabel("STATOFAS_LIBELLE_SURSIS") + " " + getAnnee());

        this.createCell(getSession().getLabel("STATOFAS_MONTANT"), getStyleListLeft());
        this.createCell("056", getStyleListLeft());
        this.createCell(getSursisMontant().doubleValue(), getStyleMontant());

        createRow();
        this.createCell(getSession().getLabel("STATOFAS_NOMBRE"), getStyleListLeft());
        this.createCell("055", getStyleListLeft());
        this.createCell(getSursisNombre(), getStyleListRight());
    }
}
