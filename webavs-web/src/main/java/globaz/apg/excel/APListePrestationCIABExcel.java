package globaz.apg.excel;

import globaz.apg.application.APApplication;
import globaz.apg.pojo.*;
import globaz.apg.process.APListePrestationCIABProcess;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.excel.REAbstractListExcel;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.phenix.listes.excel.CPListeDecisionsDefinitivesExcel;
import org.apache.poi.hssf.usermodel.*;

import java.util.List;

public class APListePrestationCIABExcel extends REAbstractListExcel {

    private static final short COLUMN_WIDTH_BIG = 12000;
    private static final short COLUMN_WIDTH_MEDIUM = 7000;

    private final static String MODEL_NAME = "AP_LISTE_COMPLEMENTS_CIAB";
    public static final String NUM_INFOROM = "5045PAP";
    private static final String PAGE_SEPARATOR = "/";

    private HSSFFont fontBold;
    private PrestationCIABPojo listePrestationCIABPojo;
    private HSSFCellStyle styleLeftBold;
    private HSSFCellStyle styleMonetaire;
    private HSSFCellStyle styleMonetaireBordureHaut;
    private HSSFCellStyle styleAlignRightBordureHaut;
    private HSSFCellStyle styleAlignRight;


    public APListePrestationCIABExcel(BSession session) {

        super(session, "APListePrestationVersee", session.getLabel(APListePrestationCIABProcess.LABEL_TITRE));

        fontBold = getWorkbook().createFont();
        fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        styleLeftBold = getWorkbook().createCellStyle();
        styleLeftBold.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        styleLeftBold.setFont(getFontBold());

        styleMonetaire = getWorkbook().createCellStyle();
        styleMonetaire.setDataFormat(REAbstractListExcel.FORMAT_MONTANT);
        styleMonetaire.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

        styleMonetaireBordureHaut = getWorkbook().createCellStyle();
        styleMonetaireBordureHaut.setDataFormat(REAbstractListExcel.FORMAT_MONTANT);
        styleMonetaireBordureHaut.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        styleMonetaireBordureHaut.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);

        styleAlignRightBordureHaut = getWorkbook().createCellStyle();
        styleAlignRightBordureHaut.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        styleAlignRightBordureHaut.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);

        styleAlignRight = getWorkbook().createCellStyle();
        styleAlignRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

    }

    private void createHeaderRows(PrestationCIABPojo aPrestationCIABPojo) {

        createRow();

        createRow();
        this.createCell(getSession().getLabel(APListePrestationCIABProcess.LABEL_TITRE), styleLeftBold);

        createRow();

        createRow();
        this.createCell(getSession().getLabel(APListePrestationCIABProcess.LABEL_DATE_TRAITEMENT), styleLeftBold);
        this.createCell(JACalendar.todayJJsMMsAAAA());

        createRow();
        this.createCell(getSession().getLabel(APListePrestationCIABProcess.LABEL_SELECTEUR_PRESTATION), styleLeftBold);
        this.createCell(aPrestationCIABPojo.getSelecteurPrestationLibelle());

        createRow();
        this.createCell(getSession().getLabel(APListePrestationCIABProcess.LABEL_PERIODE), styleLeftBold);
        this.createCell(aPrestationCIABPojo.getDateDebut() + " - " + aPrestationCIABPojo.getDateFin());

        createRow();

    }

    private void createRecapitulationPrestationCIABRows(PrestationCIABPojo aPrestationCIABPojo) {

        // header
        createRow();
        this.createCell(getSession().getLabel(APListePrestationCIABProcess.LABEL_RECAPITULATION), styleLeftBold);
        this.createCell(getSession().getLabel(APListePrestationCIABProcess.LABEL_NOMBRE_CAS), styleLeftBold);
        this.createCell(getSession().getLabel(APListePrestationCIABProcess.LABEL_MONTANT_BRUT), styleLeftBold);

        if (aPrestationCIABPojo.getMapPrestationComplementaireAssuranceCIAB().get(JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID)) != null){
            createRow();
            this.createCell(getSession().getLabel(APListePrestationCIABProcess.LABEL_COMPLEMENT_CIAB) + aPrestationCIABPojo.getMapPrestationComplementaireAssuranceCIAB().get(JadePropertiesService.getInstance()
                    .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID)).getLibelleAssuranceCompl());
            this.createCell(aPrestationCIABPojo.getMapPrestationComplementaireAssuranceCIAB().get(JadePropertiesService.getInstance()
                    .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID)).getNbCas());
            this.createCell(Double.valueOf(aPrestationCIABPojo.getMapPrestationComplementaireAssuranceCIAB().get(JadePropertiesService.getInstance()
                    .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID)).getMontantBrut()).doubleValue(), styleMonetaire);
        }

        if (aPrestationCIABPojo.getMapPrestationComplementaireAssuranceCIAB().get(JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID)) != null){
            createRow();
            this.createCell(getSession().getLabel(APListePrestationCIABProcess.LABEL_COMPLEMENT_CIAB) +aPrestationCIABPojo.getMapPrestationComplementaireAssuranceCIAB().get(JadePropertiesService.getInstance()
                    .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID)).getLibelleAssuranceCompl());
            this.createCell(aPrestationCIABPojo.getMapPrestationComplementaireAssuranceCIAB().get(JadePropertiesService.getInstance()
                    .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID)).getNbCas());
            this.createCell(Double.valueOf(aPrestationCIABPojo.getMapPrestationComplementaireAssuranceCIAB().get(JadePropertiesService.getInstance()
                    .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID)).getMontantBrut()).doubleValue(), styleMonetaire);
        }

        if (aPrestationCIABPojo.getMapPrestationComplementaireAssuranceCIAB().get(JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID)) != null){
            createRow();
            this.createCell(getSession().getLabel(APListePrestationCIABProcess.LABEL_COMPLEMENT_CIAB) +aPrestationCIABPojo.getMapPrestationComplementaireAssuranceCIAB().get(JadePropertiesService.getInstance()
                    .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID)).getLibelleAssuranceCompl());
            this.createCell(aPrestationCIABPojo.getMapPrestationComplementaireAssuranceCIAB().get(JadePropertiesService.getInstance()
                    .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID)).getNbCas());
            this.createCell(Double.valueOf(aPrestationCIABPojo.getMapPrestationComplementaireAssuranceCIAB().get(JadePropertiesService.getInstance()
                    .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID)).getMontantBrut()).doubleValue(), styleMonetaire);
        }

        if (aPrestationCIABPojo.getMapPrestationComplementaireAssuranceCIAB().get(JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID)) != null){
            createRow();
            this.createCell(getSession().getLabel(APListePrestationCIABProcess.LABEL_COMPLEMENT_CIAB) +aPrestationCIABPojo.getMapPrestationComplementaireAssuranceCIAB().get(JadePropertiesService.getInstance()
                    .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID)).getLibelleAssuranceCompl());
            this.createCell(aPrestationCIABPojo.getMapPrestationComplementaireAssuranceCIAB().get(JadePropertiesService.getInstance()
                    .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID)).getNbCas());
            this.createCell(Double.valueOf(aPrestationCIABPojo.getMapPrestationComplementaireAssuranceCIAB().get(JadePropertiesService.getInstance()
                    .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID)).getMontantBrut()).doubleValue(), styleMonetaire);
        }

        if (!aPrestationCIABPojo.getMapPrestationComplementaireAssuranceCIAB().isEmpty()) {
            createRow();
            this.createCell(getSession().getLabel(APListePrestationCIABProcess.LABEL_TOTAL_COMPLEMENT_CIAB));
            this.createCell(Integer.valueOf(aPrestationCIABPojo.getTotalNbCasComplementaire()).intValue(), styleAlignRightBordureHaut);
            this.createCell(Double.valueOf(aPrestationCIABPojo.getTotalMontantBrutComplementaire()).doubleValue(), styleMonetaireBordureHaut);
            createRow();
        }


        // detail
        for (PrestationCIABLigneRecapitulationPojo aLigneRecapitulationPrestationCIABPojo : aPrestationCIABPojo
                .getListPrestationCIAB()) {

            double totalMontantBrutService = Double.valueOf(aLigneRecapitulationPrestationCIABPojo.getTotalMontantBrutService())
                    .doubleValue();
            createRow();
            this.createCell(aLigneRecapitulationPrestationCIABPojo.getCodeService().trim() + " "
                    + aLigneRecapitulationPrestationCIABPojo.getLibelleService().trim() + " ");

            int sizeList = aLigneRecapitulationPrestationCIABPojo.getListPrestationCIABAssuranceCompl().size();

            for (PrestationCIABAssuranceComplPojo aPrestationAssuranceCompl : aLigneRecapitulationPrestationCIABPojo.getListPrestationCIABAssuranceCompl()) {
                double montantBrutService = Double.valueOf(aPrestationAssuranceCompl.getMontantBrut()).doubleValue();
                createRow();
                this.createCell("- " + aPrestationAssuranceCompl.getLibelleAssuranceCompl());
                this.createCell(Integer.valueOf(aPrestationAssuranceCompl.getNbCas()));
                this.createCell(montantBrutService, styleMonetaire);
            }

            // Si la liste est vide, nous n'affichons pas la ligne
            if (!aLigneRecapitulationPrestationCIABPojo.getListPrestationCIABAssuranceCompl().isEmpty()){
                createRow();
                this.createCell(getSession().getLabel(APListePrestationCIABProcess.LABEL_TOTAUX)+ " " + aLigneRecapitulationPrestationCIABPojo.getCodeService().trim() + " " + aLigneRecapitulationPrestationCIABPojo.getLibelleService().trim(), styleAlignRight);
                this.createCell(Integer.valueOf(aLigneRecapitulationPrestationCIABPojo.getTotalNbCasService()).intValue(), styleAlignRightBordureHaut);
                this.createCell(totalMontantBrutService, styleMonetaireBordureHaut);
                createRow();
            }
        }

        // S'il n'y a pas de jours isolés, nous n'affichons pas la ligne dans le tableau Excel
        if (aPrestationCIABPojo.getTotalJourIsole() != 0){
            createRow();
            this.createCell(getSession().getLabel(APListePrestationCIABProcess.LABEL_TOTAUX_JOURS_ISOLES));
            this.createCell(Integer.valueOf(aPrestationCIABPojo.getTotalJourIsole()).intValue());
            this.createCell(Double.valueOf(aPrestationCIABPojo.getTotalMontantBrutJourIsole()).doubleValue(), styleMonetaire);
        }
    }

    public void creerDocument() {

            // feuille 1(récapitulation)
        HSSFSheet currentSheet = createSheet(getSession().getLabel(APListePrestationCIABProcess.LABEL_RECAPITULATION));
        createHeader();
        initPage(true);
        initColumnWidthSheetListePrestation();
        createHeaderRows(listePrestationCIABPojo);
        createRecapitulationPrestationCIABRows(listePrestationCIABPojo);
        currentSheet.autoSizeColumn((short) 0);
        createFooter();
    }

    public PrestationCIABPojo getListePrestationCIABPojo() {
        return listePrestationCIABPojo;
    }

    private void initColumnWidthSheetListePrestation() {

        short numCol = 0;

        currentSheet.setColumnWidth(numCol++, APListePrestationCIABExcel.COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, APListePrestationCIABExcel.COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, APListePrestationCIABExcel.COLUMN_WIDTH_BIG);
        currentSheet.setColumnWidth(numCol++, APListePrestationCIABExcel.COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, APListePrestationCIABExcel.COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, APListePrestationCIABExcel.COLUMN_WIDTH_MEDIUM);
    }

    private void initColumnWidthSheetListePrestationRecapitulation() {

        short numCol = 0;

        currentSheet.setColumnWidth(numCol++, APListePrestationCIABExcel.COLUMN_WIDTH_BIG);
        currentSheet.setColumnWidth(numCol++, APListePrestationCIABExcel.COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, APListePrestationCIABExcel.COLUMN_WIDTH_MEDIUM);
    }

    public void setListePrestationCIABPojo(PrestationCIABPojo listePrestationCIABPojo) {
        this.listePrestationCIABPojo = listePrestationCIABPojo;
    }

    /**
     * Crée l'entête
     *
     * @return l'entête
     */
    protected HSSFHeader createHeader() {
        // en-tête
        JadePublishDocumentInfo documentInfo = new JadePublishDocumentInfo();
        HSSFHeader header = currentSheet.getHeader();
        header.setLeft(FWIImportProperties.getInstance().getProperty(documentInfo,
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        header.setRight(getDocumentTitle());
        return header;
    }

    /**
     * Crée le pied de page
     *
     *
     * @return le pied de page
     */
    protected HSSFFooter createFooter() {
        // pied de page
        HSSFFooter footer = currentSheet.getFooter();
        footer.setRight(getSession().getLabel("PAGE") + HSSFFooter.page() + PAGE_SEPARATOR + HSSFFooter.numPages());
        footer.setLeft(getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1) + " - "
                + getDateImpression() + " - " + getSession().getUserName() + " - " +HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: "
                + NUM_INFOROM);
        return footer;
    }
}
