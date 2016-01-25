package globaz.apg.excel;

import globaz.apg.pojo.PrestationVerseeLignePrestationPojo;
import globaz.apg.pojo.PrestationVerseeLigneRecapitulationPojo;
import globaz.apg.pojo.PrestationVerseePojo;
import globaz.apg.process.APListePrestationVerseeProcess;
import globaz.corvus.excel.REAbstractListExcel;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;

public class APListePrestationVerseeExcel extends REAbstractListExcel {

    private static final short COLUMN_WIDTH_BIG = 12000;
    private static final short COLUMN_WIDTH_MEDIUM = 7000;

    private HSSFFont fontBold;
    private List<PrestationVerseePojo> listePrestationVerseePojo;
    private HSSFCellStyle styleLeftBold;
    private HSSFCellStyle styleMonetaire;

    public APListePrestationVerseeExcel(BSession session) {

        super(session, "APListePrestationVersee", session.getLabel(APListePrestationVerseeProcess.LABEL_TITRE));

        fontBold = getWorkbook().createFont();
        fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        styleLeftBold = getWorkbook().createCellStyle();
        styleLeftBold.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        styleLeftBold.setFont(getFontBold());

        styleMonetaire = getWorkbook().createCellStyle();
        styleMonetaire.setDataFormat(REAbstractListExcel.FORMAT_MONTANT);
        styleMonetaire.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

    }

    private void createHeaderRows(PrestationVerseePojo aPrestationVerseePojo) {

        createRow();

        createRow();
        this.createCell(getSession().getLabel(APListePrestationVerseeProcess.LABEL_TITRE), styleLeftBold);

        createRow();

        createRow();
        this.createCell(getSession().getLabel(APListePrestationVerseeProcess.LABEL_DATE_TRAITEMENT), styleLeftBold);
        this.createCell(JACalendar.todayJJsMMsAAAA());

        createRow();
        this.createCell(getSession().getLabel(APListePrestationVerseeProcess.LABEL_NUMERO_AFFILIE), styleLeftBold);
        this.createCell(aPrestationVerseePojo.getNumeroAffilie().trim() + " - " + aPrestationVerseePojo.getNomAffilie());

        createRow();
        this.createCell(getSession().getLabel(APListePrestationVerseeProcess.LABEL_SELECTEUR_PRESTATION), styleLeftBold);
        this.createCell(aPrestationVerseePojo.getSelecteurPrestationLibelle());

        createRow();
        this.createCell(getSession().getLabel(APListePrestationVerseeProcess.LABEL_PERIODE), styleLeftBold);
        this.createCell(aPrestationVerseePojo.getDateDebut() + " - " + aPrestationVerseePojo.getDateFin());

        createRow();

    }

    private void createPrestationVerseeRows(PrestationVerseePojo aPrestationVerseePojo) {

        // header
        createRow();
        this.createCell(getSession().getLabel(APListePrestationVerseeProcess.LABEL_GENRE_SERVICE), styleLeftBold);
        this.createCell(getSession().getLabel(APListePrestationVerseeProcess.LABEL_NSS), styleLeftBold);
        this.createCell(getSession().getLabel(APListePrestationVerseeProcess.LABEL_NOM_PRENOM), styleLeftBold);
        this.createCell(getSession().getLabel(APListePrestationVerseeProcess.LABEL_PERIODE), styleLeftBold);
        this.createCell(getSession().getLabel(APListePrestationVerseeProcess.LABEL_MONTANT_BRUT), styleLeftBold);
        this.createCell(getSession().getLabel(APListePrestationVerseeProcess.LABEL_DATE_PAIEMENT), styleLeftBold);

        // detail
        for (PrestationVerseeLignePrestationPojo aLignePrestationVerseePojo : aPrestationVerseePojo
                .getListeLignePrestationVersee()) {

            double montantBrut = Double.valueOf(aLignePrestationVerseePojo.getMontantBrut()).doubleValue();
            createRow();
            this.createCell(aLignePrestationVerseePojo.getGenreService() + " "
                    + aLignePrestationVerseePojo.getGenrePrestationLibelle());
            this.createCell(aLignePrestationVerseePojo.getNss());
            this.createCell(aLignePrestationVerseePojo.getNom() + " " + aLignePrestationVerseePojo.getPrenom());
            this.createCell(aLignePrestationVerseePojo.getDateDebut() + " - " + aLignePrestationVerseePojo.getDateFin());
            this.createCell(montantBrut, styleMonetaire);
            this.createCell(aLignePrestationVerseePojo.getDatePaiement());
        }

    }

    private void createRecapitulationPrestationVerseeRows(PrestationVerseePojo aPrestationVerseePojo) {

        // header
        createRow();
        this.createCell(getSession().getLabel(APListePrestationVerseeProcess.LABEL_RECAPITULATION), styleLeftBold);
        this.createCell(getSession().getLabel(APListePrestationVerseeProcess.LABEL_NOMBRE_CAS), styleLeftBold);
        this.createCell(getSession().getLabel(APListePrestationVerseeProcess.LABEL_MONTANT_BRUT), styleLeftBold);

        // detail
        for (PrestationVerseeLigneRecapitulationPojo aLigneRecapitulationPrestationVerseePojo : aPrestationVerseePojo
                .getMapLigneRecapitulationPrestationVersee().values()) {

            double montantBrut = Double.valueOf(aLigneRecapitulationPrestationVerseePojo.getMontantBrut())
                    .doubleValue();
            createRow();
            this.createCell(aLigneRecapitulationPrestationVerseePojo.getGenreService() + " "
                    + aLigneRecapitulationPrestationVerseePojo.getLibelleGenreService() + " "
                    + aLigneRecapitulationPrestationVerseePojo.getGenrePrestationLibelle());
            this.createCell(Integer.valueOf(aLigneRecapitulationPrestationVerseePojo.getNombreCas()).intValue());
            this.createCell(montantBrut, styleMonetaire);
        }

    }

    public void creerDocument() {

        for (PrestationVerseePojo aPrestationVerseePojo : listePrestationVerseePojo) {

            // feuille 1(liste prestations)
            createSheet(aPrestationVerseePojo.getNumeroAffilie());
            initPage(true);
            initColumnWidthSheetListePrestation();
            createHeaderRows(aPrestationVerseePojo);
            createPrestationVerseeRows(aPrestationVerseePojo);

            // feuille 2(récapitulation)
            createSheet(aPrestationVerseePojo.getNumeroAffilie() + "_"
                    + getSession().getLabel(APListePrestationVerseeProcess.LABEL_RECAPITULATION));
            initPage(true);
            initColumnWidthSheetListePrestationRecapitulation();
            createHeaderRows(aPrestationVerseePojo);
            createRecapitulationPrestationVerseeRows(aPrestationVerseePojo);

        }

    }

    public List<PrestationVerseePojo> getListePrestationVerseePojo() {
        return listePrestationVerseePojo;
    }

    private void initColumnWidthSheetListePrestation() {

        short numCol = 0;

        currentSheet.setColumnWidth(numCol++, APListePrestationVerseeExcel.COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, APListePrestationVerseeExcel.COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, APListePrestationVerseeExcel.COLUMN_WIDTH_BIG);
        currentSheet.setColumnWidth(numCol++, APListePrestationVerseeExcel.COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, APListePrestationVerseeExcel.COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, APListePrestationVerseeExcel.COLUMN_WIDTH_MEDIUM);
    }

    private void initColumnWidthSheetListePrestationRecapitulation() {

        short numCol = 0;

        currentSheet.setColumnWidth(numCol++, APListePrestationVerseeExcel.COLUMN_WIDTH_BIG);
        currentSheet.setColumnWidth(numCol++, APListePrestationVerseeExcel.COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, APListePrestationVerseeExcel.COLUMN_WIDTH_MEDIUM);
    }

    public void setListePrestationVerseePojo(List<PrestationVerseePojo> listePrestationVerseePojo) {
        this.listePrestationVerseePojo = listePrestationVerseePojo;
    }

}
