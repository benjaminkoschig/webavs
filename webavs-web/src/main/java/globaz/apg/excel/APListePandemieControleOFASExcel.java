package globaz.apg.excel;


import ch.globaz.common.domaine.Date;
import globaz.apg.db.liste.APListePandemieControleModel;
import globaz.apg.db.liste.APListePandemieControleOFASModel;
import globaz.apg.process.APListePandemieControleOFAS;
import globaz.corvus.excel.REAbstractListExcel;
import globaz.globall.db.BSession;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.log.JadeLogger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import java.sql.SQLException;
import java.text.Normalizer;
import java.util.*;

public class APListePandemieControleOFASExcel extends REAbstractListExcel {
    private List<APListePandemieControleOFASModel> listSheet = new ArrayList<>();
    private static final short COLUMN_WIDTH_BIG = 12000;
    private static final short COLUMN_WIDTH_MEDIUM = 7000;
    private HSSFCellStyle styleHeader;

    private static String SHEET_LABEL = "COVID";
    //HEADER
    private static final String HEADER_COL_NUM_CAISSE = "Ausgleichskasse_Nr";
    private static final String HEADER_COL_NOM_CAISSE = "Ausgleichskasse_Name";
    private static final String HEADER_COL_1 = "AHVNr";
    private static final String HEADER_COL_2 = "NAME";
    private static final String HEADER_COL_3 = "VORNAME";
    private static final String HEADER_COL_4 = "Grund";
    private static final String HEADER_COL_5 = "Von";
    private static final String HEADER_COL_6 = "Bis";
    private static final String HEADER_COL_7 = "AnzTage";
    private static final String HEADER_COL_8 = "Tagesansatz";
    private static final String HEADER_COL_9 = "Brutto";
    private static final String HEADER_COL_10 = "Netto";
    private static final String HEADER_COL_11 = "QstAbzug";
    private static final String HEADER_COL_12 = "Gebucht_am";
    private static final String HEADER_COL_13 = "Bezahlt_am";
    private static final String HEADER_COL_14 = "Endbegünstigter";
    private static final String HEADER_COL_15 = "IBAN";
    private static final String HEADER_COL_16 = "UIDNUMMER";
    private static final String HEADER_COL_17 = "Geschäftsadresse_Anschrift";
    private static final String HEADER_COL_18 = "GeschäftsadresseAdresse";
    private static final String HEADER_COL_19 = "GeschäftsadressePLZ";
    private static final String HEADER_COL_20 = "GeschäftsadresseOrt";
    private static final String HEADER_COL_21 = "WohnsitzadresseAnschrift";
    private static final String HEADER_COL_22 = "WohnsitzadresseAdresse";
    private static final String HEADER_COL_23 = "WohnsitzadressePLZ";
    private static final String HEADER_COL_24 = "WohnsitzadresseOrt";


    public APListePandemieControleOFASExcel(BSession session) {
        super(session, "CDF Daten", session.getLabel(APListePandemieControleOFAS.LABEL_TITRE_DOCUMENT));
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            styleHeader = getWorkbook().createCellStyle();
            styleHeader.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            styleHeader.setFont(getFontBold());
        } catch (SQLException ex) {
            JadeLogger.error(APListePandemieControleOFASExcel.class, ex.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }


    public void creerDocument() {
        createControleOFAS();

    }

    private void createControleOFAS() {
        createSheet(SHEET_LABEL);
        initPage(true);
        initColumnWidthSheetListePrestation();
        createHeaderRows(false);
        if (!listSheet.isEmpty()) {
            createDataRows(listSheet);
        }
        for (int i = 0; i < 28; i++) {
            currentSheet.autoSizeColumn((short) i);
        }


    }

    private void createDataRows(List<APListePandemieControleOFASModel> listSheet) {
        Collections.sort(listSheet, new Comparator<APListePandemieControleOFASModel>() {
            @Override
            public int compare(APListePandemieControleOFASModel t1, APListePandemieControleOFASModel t2) {
                String nomT1 = Normalizer.normalize(t1.getNom(), Normalizer.Form.NFD);
                String nomT2 = Normalizer.normalize(t2.getNom(), Normalizer.Form.NFD);
                if (nomT1.compareTo(nomT2) == 0) {
                    nomT1 = Normalizer.normalize(t1.getPrenom(), Normalizer.Form.NFD);
                    nomT2 = Normalizer.normalize(t2.getPrenom(), Normalizer.Form.NFD);
                    if (nomT1.compareTo(nomT2) == 0) {
                        ch.globaz.common.domaine.Date date1;
                        ch.globaz.common.domaine.Date date2;
                        if (t1.getDe().trim().equals("00.00.0000")) {
                            date1 = new ch.globaz.common.domaine.Date("01.01." + t1.getDe().trim().substring(6, 10));
                        } else {
                            date1 = new ch.globaz.common.domaine.Date(t1.getDe().trim());
                        }
                        if (t2.getDe().trim().equals("00.00.0000")) {
                            date2 = new ch.globaz.common.domaine.Date("01.01.01" + t2.getDe().trim().substring(6, 10));
                        } else {
                            date2 = new ch.globaz.common.domaine.Date(t2.getDe().trim());
                        }
                        if (date1.compareTo(date2) == 0) {
                            if (t1.getA().trim().equals("00.00.0000")) {
                                date1 = new ch.globaz.common.domaine.Date("01.01." + t1.getA().trim().substring(6, 10));
                            } else {
                                date1 = new ch.globaz.common.domaine.Date(t1.getA().trim());
                            }
                            if (t2.getA().trim().equals("00.00.0000")) {
                                date2 = new ch.globaz.common.domaine.Date("01.01.01" + t2.getA().trim().substring(6, 10));
                            } else {
                                date2 = new Date(t2.getA().trim());
                            }
                        }
                        return date1.compareTo(date2);
                    }
                    return nomT1.compareTo(nomT2);
                }
                return nomT1.compareTo(nomT2);
            }
            });

        for(APListePandemieControleOFASModel model :listSheet){
                createRow();
                this.createCell(model.getNumCaisse());
                this.createCell(model.getNomCaisse());
                this.createCell(model.getNss());
                this.createCell(model.getNom());
                this.createCell(model.getPrenom());
                this.createCell(model.getGenreService());
                this.createCell(model.getDe());
                this.createCell(model.getA());
                this.createCell(model.getNbreJours());
                this.createCell(model.getMontantJournalier());
                this.createCell(model.getMontantBrut());
                this.createCell(model.getMontantNet());
                this.createCell(model.getDeductionSource());
                this.createCell(model.getDateComptable());
                this.createCell(model.getDatePaiement());
                this.createCell(model.getBeneficiaire());
                this.createCell(model.getIBANBenef());
                this.createCell(model.getId());
                this.createCell(model.getAdresseNomProf());
                this.createCell(model.getRueProf());
                this.createCell(model.getNpaProf());
                this.createCell(model.getLocaliteProf());
                this.createCell(model.getAdresseNomDom());
                this.createCell(model.getRueDom());
                this.createCell(model.getNpaDom());
                this.createCell(model.getLocaliteDom());
            }
        }


        private void createHeaderRows ( boolean isGarde){
            createRow();
            this.createCell(HEADER_COL_NUM_CAISSE, styleHeader);
            this.createCell(HEADER_COL_NOM_CAISSE, styleHeader);
            this.createCell(HEADER_COL_1, styleHeader);
            this.createCell(HEADER_COL_2, styleHeader);
            this.createCell(HEADER_COL_3, styleHeader);
            this.createCell(HEADER_COL_4, styleHeader);
            this.createCell(HEADER_COL_5, styleHeader);
            this.createCell(HEADER_COL_6, styleHeader);
            this.createCell(HEADER_COL_7, styleHeader);
            this.createCell(HEADER_COL_8, styleHeader);
            this.createCell(HEADER_COL_9, styleHeader);
            this.createCell(HEADER_COL_10, styleHeader);
            this.createCell(HEADER_COL_11, styleHeader);
            this.createCell(HEADER_COL_12, styleHeader);
            this.createCell(HEADER_COL_13, styleHeader);
            this.createCell(HEADER_COL_14, styleHeader);
            this.createCell(HEADER_COL_15, styleHeader);
            this.createCell(HEADER_COL_16, styleHeader);
            this.createCell(HEADER_COL_17, styleHeader);
            this.createCell(HEADER_COL_18, styleHeader);
            this.createCell(HEADER_COL_19, styleHeader);
            this.createCell(HEADER_COL_20, styleHeader);
            this.createCell(HEADER_COL_21, styleHeader);
            this.createCell(HEADER_COL_22, styleHeader);
            this.createCell(HEADER_COL_23, styleHeader);
            this.createCell(HEADER_COL_24, styleHeader);
        }

        private void initColumnWidthSheetListePrestation () {

            short numCol = 0;

            currentSheet.setColumnWidth(numCol++, APListePandemieControleOFASExcel.COLUMN_WIDTH_MEDIUM);
            currentSheet.setColumnWidth(numCol++, APListePandemieControleOFASExcel.COLUMN_WIDTH_MEDIUM);
            currentSheet.setColumnWidth(numCol++, APListePandemieControleOFASExcel.COLUMN_WIDTH_MEDIUM);
            currentSheet.setColumnWidth(numCol++, APListePandemieControleOFASExcel.COLUMN_WIDTH_MEDIUM);
            currentSheet.setColumnWidth(numCol++, APListePandemieControleOFASExcel.COLUMN_WIDTH_BIG);
            currentSheet.setColumnWidth(numCol++, APListePandemieControleOFASExcel.COLUMN_WIDTH_MEDIUM);
            currentSheet.setColumnWidth(numCol++, APListePandemieControleOFASExcel.COLUMN_WIDTH_MEDIUM);
            currentSheet.setColumnWidth(numCol++, APListePandemieControleOFASExcel.COLUMN_WIDTH_MEDIUM);
        }


        public List getListeSheet () {
            return listSheet;
        }

        public void setListeSheet (List listSheet){
            this.listSheet = listSheet;
        }

        public void setList (List < APListePandemieControleOFASModel > listSuivi) {
        }
    }
