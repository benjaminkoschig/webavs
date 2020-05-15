package globaz.apg.excel;

import ch.globaz.common.domaine.Date;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.vulpecula.util.I18NUtil;
import globaz.apg.db.liste.APListePandemieControleModel;
import globaz.apg.db.liste.APListePandemieControleV2Model;
import globaz.apg.db.liste.APListePandemieSuiviDossierModel;
import globaz.apg.process.APListePandemieControleProcess;
import globaz.corvus.excel.REAbstractListExcel;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.log.JadeLogger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import java.sql.SQLException;
import java.text.Normalizer;
import java.util.*;

public class APListePandemieControleExcel extends REAbstractListExcel {
    private List listSheet = new ArrayList<>();
    private Map<String, List> mapListes = new HashMap<>();
    private static final short COLUMN_WIDTH_BIG = 12000;
    private static final short COLUMN_WIDTH_MEDIUM = 7000;
    private HSSFCellStyle styleHeader;
    Map<String, String> listCSGenreService;
    Map<String, String> listCSEtat;

    private static String KEY_DOUBLONS = "";
    private static String KEY_MONTANT_SUP = "";
    private static String KEY_MULTI_SITU = "";
    private static String KEY_DOUBLE_DEMANDE = "";
    private static String KEY_GARDE_IND_SUP_30 = "";
    private static String KEY_JOURS_CARENCES = "";
    //HEADER
    private static final String HEADER_COL_1 = "DOC_MODEL_NSS";
    private static final String HEADER_COL_2 = "DOC_MODEL_NOM";
    private static final String HEADER_COL_3 = "DOC_MODEL_DATE_DE_DEBUT";
    private static final String HEADER_COL_4 = "DOC_MODEL_DATE_DE_FIN";
    private static final String HEADER_COL_5 = "DOC_MODEL_GENRE_DE_SERVICE";
    private static final String HEADER_COL_6 = "DOC_MODEL_ETAT";
    private static final String HEADER_COL_7 = "DOC_MODEL_ID_DEMANDE";
    private static final String HEADER_COL_8 = "DOC_MODEL_ID_DROIT";
    private static final String HEADER_COL_9 = "DOC_MODEL_ID_TIERS";
    private static final String HEADER_COL_NBRE_JOURS = "DOC_MODEL_NBRE_JOURS";
    //HEADER V2
    private static final String HEADER_COL_V2_1 = "DOC_LISTE_MODEL_V2_NSS";
    private static final String HEADER_COL_V2_2 = "DOC_LISTE_MODEL_V2_NOM";
    private static final String HEADER_COL_V2_3 = "DOC_LISTE_MODEL_V2_DATE_DE_DEBUT_PRESTATION";
    private static final String HEADER_COL_V2_4 = "DOC_LISTE_MODEL_V2_DATE_DE_FIN_PRESTATION";
    private static final String HEADER_COL_V2_5 = "DOC_LISTE_MODEL_V2_MONTANT_JOURNALIER";
    private static final String HEADER_COL_V2_6 = "DOC_LISTE_MODEL_V2_NBRE_JOURS";
    private static final String HEADER_COL_V2_7 = "DOC_LISTE_MODEL_V2_MONTANT_BRUTE_REPARTIION";
    private static final String HEADER_COL_V2_8 = "DOC_LISTE_MODEL_V2_BENEFICIAIRE_PAIEMENT";
    private static final String HEADER_COL_V2_9 = "DOC_LISTE_MODEL_V2_GENRE_DE_SERVICE";
    private static final String HEADER_COL_V2_10 = "DOC_LISTE_MODEL_V2_ETAT";
    private static final String HEADER_COL_V2_11 = "DOC_LISTE_MODEL_V2_DATE_DE_DEBUT_DROIT";
    private static final String HEADER_COL_V2_12 = "DOC_LISTE_MODEL_V2_DATE_DE_FIN_DROIT";
    private static final String HEADER_COL_V2_13 = "DOC_LISTE_MODEL_V2_PAIEMENT_ID_DEMANDE";
    private static final String HEADER_COL_V2_14 = "DOC_LISTE_MODEL_V2_PAIEMENT_ID_DROIT";
    private static final String HEADER_COL_V2_15 = "DOC_LISTE_MODEL_V2_PAIEMENT_ID_TIERS";
    private static final String HEADER_COL_V2_DATE_DEBUT_1ST_PERIODE = "DOC_LISTE_MODEL_V2_DATE_DEBUT_1ST_PERIODE";
    private static final String HEADER_COL_V2_DATE_FIN_1ST_PERIODE = "DOC_LISTE_MODEL_V2_DATE_FIN_1ST_PERIODE";


    public APListePandemieControleExcel(BSession session) {
        super(session, "APListePandemieControleExcel", session.getLabel(APListePandemieControleProcess.LABEL_TITRE_DOCUMENT));
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            styleHeader = getWorkbook().createCellStyle();
            styleHeader.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            styleHeader.setFont(getFontBold());
            listCSGenreService = getCSListeWithCodeLibelle("APGENSERVI");
            listCSEtat = getCSListeWithLibelle("APETADROIT");
            KEY_DOUBLONS = getSession().getLabel(APListePandemieControleProcess.LABEL_TITRE_SHEET_DOCUMENT);
            KEY_MONTANT_SUP = getSession().getLabel(APListePandemieControleProcess.LABEL_TITRE_SHEET_5_DOCUMENT);
            KEY_MULTI_SITU = getSession().getLabel(APListePandemieControleProcess.LABEL_TITRE_SHEET_6_DOCUMENT);
            KEY_DOUBLE_DEMANDE = getSession().getLabel(APListePandemieControleProcess.LABEL_TITRE_SHEET_7_DOCUMENT);
            KEY_GARDE_IND_SUP_30 = getSession().getLabel(APListePandemieControleProcess.LABEL_TITRE_SHEET_8_DOCUMENT);
            KEY_JOURS_CARENCES = getSession().getLabel(APListePandemieControleProcess.LABEL_TITRE_SHEET_9_DOCUMENT);
        } catch (SQLException ex) {
            JadeLogger.error(APListePandemieControleExcel.class, ex.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }

    private Map<String, String> getCSListeWithCodeLibelle(String nomFamille) {
        Map<String, String> resultTrie = new HashMap<String, String>();
        try {
            List<JadeCodeSysteme> codes = JadeBusinessServiceLocator.getCodeSystemeService()
                    .getFamilleCodeSysteme(nomFamille);
            for (JadeCodeSysteme code : codes) {
                Langues langue = I18NUtil.getLangues();
                String codeValue = code.getCodeUtilisateur(langue);
                String libelleValue = code.getTraduction(langue);
                resultTrie.put(code.getIdCodeSysteme(), codeValue + " " + libelleValue);
            }
        } catch (Exception ex) {
            JadeLogger.error(APListePandemieControleExcel.class, ex.getMessage());
        }
        return resultTrie;
    }

    private Map<String, String> getCSListeWithLibelle(String nomFamille) {
        Map<String, String> resultTrie = new HashMap<String, String>();
        try {
            List<JadeCodeSysteme> codes = JadeBusinessServiceLocator.getCodeSystemeService()
                    .getFamilleCodeSysteme(nomFamille);
            for (JadeCodeSysteme code : codes) {
                Langues langue = I18NUtil.getLangues();
                String libelleValue = code.getTraduction(langue);
                resultTrie.put(code.getIdCodeSysteme(), libelleValue);
            }
        } catch (Exception ex) {
            JadeLogger.error(APListePandemieControleExcel.class, ex.getMessage());
        }
        return resultTrie;
    }

    public void creerDocument() {
        createSheetPaiementDouble();
        createSheetRule();
        createSheetMontantSup();
        createSheetMultiSituationProf();
        createSheetDoubleDemande();
        createSheetGardeIndSup30();
        createSheetJoursCarences();

    }

    private void createSheetJoursCarences() {
        listSheet = (List<APListePandemieControleV2Model>) mapListes.get(KEY_JOURS_CARENCES);
        createSheet(KEY_JOURS_CARENCES);
        initPage(true);
        initColumnWidthSheetListePrestation(false);
        createHeaderRowV2JoursCarences();
        if (!listSheet.isEmpty()) {
            createDataRowsV2JoursCarences(listSheet);
        }
        for (int i = 0; i < 12; i++) {
            currentSheet.autoSizeColumn((short) i);
        }
    }

    private void createSheetGardeIndSup30() {
        listSheet = (List<APListePandemieControleModel>) mapListes.get(KEY_GARDE_IND_SUP_30);
        createSheet(KEY_GARDE_IND_SUP_30);
        initPage(true);
        initColumnWidthSheetListePrestation(true);
        createHeaderRows(true);
        if (!listSheet.isEmpty()) {
            createDataRows(listSheet, true);
        }
        for (int i = 0; i < 10; i++) {
            currentSheet.autoSizeColumn((short) i);
        }


    }


    private void createSheetPaiementDouble() {
        listSheet = (List<APListePandemieControleModel>) mapListes.get(KEY_DOUBLONS);
        createSheet(KEY_DOUBLONS);
        initPage(true);
        initColumnWidthSheetListePrestation(false);
        createHeaderRows(false);
        if (!listSheet.isEmpty()) {
            createDataRows(listSheet, false);
        }
        for (int i = 0; i < 9; i++) {
            currentSheet.autoSizeColumn((short) i);
        }

    }

    private void createSheetRule() {
        String key = "Rule";
        for (int i = 0; i < 3; i++) {
            listSheet = (List<APListePandemieControleModel>) mapListes.get(key + " " + (65 + i));
            initPage(true);
            createSheet(key + " " + (65 + i));
            initColumnWidthSheetListePrestation(false);
            createHeaderRows(false);
            if (!listSheet.isEmpty()) {
                createDataRows(listSheet, false);
            }
            for (int j = 0; j < 9; j++) {
                currentSheet.autoSizeColumn((short) j);
            }
        }

    }

    private void createSheetMontantSup() {
        listSheet = (List<APListePandemieControleModel>) mapListes.get(KEY_MONTANT_SUP);
        createSheet(KEY_MONTANT_SUP);
        initPage(true);
        initColumnWidthSheetListePrestation(false);
        createHeaderRows(false);
        if (!listSheet.isEmpty()) {
            createDataRows(listSheet, false);
        }
        for (int i = 0; i < 9; i++) {
            currentSheet.autoSizeColumn((short) i);
        }
    }

    private void createSheetMultiSituationProf() {
        listSheet = (List<APListePandemieControleV2Model>) mapListes.get(KEY_MULTI_SITU);
        createSheet(KEY_MULTI_SITU);
        initPage(true);
        initColumnWidthSheetListePrestation(false);
        createHeaderRowV2(false);
        if (!listSheet.isEmpty()) {
            createDataRowsV2(listSheet, false);
        }
        for (int i = 0; i < 15; i++) {
            currentSheet.autoSizeColumn((short) i);
        }
    }

    private void createSheetDoubleDemande() {
        listSheet = (List<APListePandemieControleModel>) mapListes.get(KEY_DOUBLE_DEMANDE);
        createSheet(KEY_DOUBLE_DEMANDE);
        initPage(true);
        initColumnWidthSheetListePrestation(false);
        createHeaderRows(false);
        if (!listSheet.isEmpty()) {
            createDataRows(listSheet, false);
        }
        for (int i = 0; i < 9; i++) {
            currentSheet.autoSizeColumn((short) i);
        }


    }

    private void createDataRows(List<APListePandemieControleModel> listSheet, boolean isGarde) {
        Collections.sort(listSheet, new Comparator<APListePandemieControleModel>() {
            @Override
            public int compare(APListePandemieControleModel t1, APListePandemieControleModel t2) {
                String nomT1 = Normalizer.normalize(t1.getNom(), Normalizer.Form.NFD);
                String nomT2 = Normalizer.normalize(t2.getNom(), Normalizer.Form.NFD);
                if (nomT1.compareTo(nomT2) == 0) {
                    Date date1;
                    Date date2;
                    if (t1.getDateDebut().trim().equals("00.00.0000")) {
                        date1 = new Date("01.01." + t1.getDateDebut().trim().substring(6, 10));
                    } else {
                        date1 = new Date(t1.getDateDebut().trim());
                    }
                    if (t2.getDateDebut().trim().equals("00.00.0000")) {
                        date2 = new Date("01.01.01" + t2.getDateDebut().trim().substring(6, 10));
                    } else {
                        date2 = new Date(t2.getDateDebut().trim());
                    }
                    if (date1.compareTo(date2) == 0) {
                        if (t1.getDateFin().trim().equals("00.00.0000")) {
                            date1 = new Date("01.01." + t1.getDateDebut().trim().substring(6, 10));
                        } else {
                            date1 = new Date(t1.getDateFin().trim());
                        }
                        if (t2.getDateFin().trim().equals("00.00.0000")) {
                            date2 = new Date("01.01.01" + t2.getDateDebut().trim().substring(6, 10));
                        } else {
                            date2 = new Date(t2.getDateFin().trim());
                        }
                    }
                    return date1.compareTo(date2);
                }
                return nomT1.compareTo(nomT2);
            }
        });
        for (APListePandemieControleModel model : listSheet) {
            createRow();
            this.createCell(model.getNss());
            this.createCell(model.getNom());
            this.createCell(model.getDateDebut());
            this.createCell(model.getDateFin());
            if (isGarde) {
                this.createCell(model.getNbreJours());
            }
            this.createCell(listCSGenreService.get(model.getGenreService()));
            this.createCell(listCSEtat.get(model.getEtat()));
            this.createCell(model.getIdDroit());
            this.createCell(model.getIdDemande());
            this.createCell(model.getIdTiers());
        }
    }

    private void createDataRowsV2(List<APListePandemieControleV2Model> listSheet, boolean isDoubleDemande) {
        Collections.sort(listSheet, new Comparator<APListePandemieControleV2Model>() {
            @Override
            public int compare(APListePandemieControleV2Model t1, APListePandemieControleV2Model t2) {
                String nomT1 = Normalizer.normalize(t1.getNom(), Normalizer.Form.NFD);
                String nomT2 = Normalizer.normalize(t2.getNom(), Normalizer.Form.NFD);
                if (nomT1.compareTo(nomT2) == 0) {
                    Double nb1 = Double.valueOf(t1.getNss().trim().replaceAll("\\.", ""));
                    Double nb2 = Double.valueOf(t2.getNss().trim().replaceAll("\\.", ""));
                    if (nb1.compareTo(nb2) == 0) {
                        nb1 = Double.valueOf(t1.getDateDebutPrestation().trim().replaceAll("\\.", ""));
                        nb2 = Double.valueOf(t2.getDateDebutPrestation().trim().replaceAll("\\.", ""));
                        if (nb1.compareTo(nb2) == 0) {
                            nb1 = Double.valueOf(t1.getDateFinPrestation().trim().replaceAll("\\.", ""));
                            nb2 = Double.valueOf(t2.getDateFinPrestation().trim().replaceAll("\\.", ""));
                        }
                    }
                    return nb1.compareTo(nb2);
                }
                return nomT1.compareTo(nomT2);
            }
        });
        for (APListePandemieControleV2Model model : listSheet) {
            createRow();
            this.createCell(model.getNss());
            this.createCell(model.getNom());
            this.createCell(model.getDateDebutPrestation());
            this.createCell(model.getDateFinPrestation());
            if (!isDoubleDemande) {
                this.createCell(model.getMntJournalier());
                this.createCell(model.getNbreJours());
                this.createCell(model.getMntPrestation());
                this.createCell(model.getMntBrutRepartition());
                this.createCell(model.getBenefPaiement());
            }
            this.createCell(listCSGenreService.get(model.getGenreService()));
            this.createCell(listCSEtat.get(model.getEtat()));
            this.createCell(model.getDateDebutDroit());
            this.createCell(model.getIdDroit());
            this.createCell(model.getIdDemande());
            this.createCell(model.getIdTiers());

        }
    }

    private void createDataRowsV2JoursCarences(List<APListePandemieControleV2Model> listSheet) {
        Collections.sort(listSheet, new Comparator<APListePandemieControleV2Model>() {
            @Override
            public int compare(APListePandemieControleV2Model t1, APListePandemieControleV2Model t2) {
                String nomT1 = Normalizer.normalize(t1.getNom(), Normalizer.Form.NFD);
                String nomT2 = Normalizer.normalize(t2.getNom(), Normalizer.Form.NFD);
                if (nomT1.compareTo(nomT2) == 0) {
                    Date date1;
                    Date date2;
                    if (t1.getDateDebutPrestation().trim().equals("00.00.0000")) {
                        date1 = new Date("01.01." + t1.getDateDebutPrestation().trim().substring(6, 10));
                    } else {
                        date1 = new Date(t1.getDateDebutPrestation().trim());
                    }
                    if (t2.getDateDebutPrestation().trim().equals("00.00.0000")) {
                        date2 = new Date("01.01." + t1.getDateDebutPrestation().trim().substring(6, 10));
                    } else {
                        date2 = new Date(t2.getDateDebutPrestation().trim());
                    }
                    if (date1.compareTo(date2) == 0) {
                        if (t1.getDateFinPrestation().trim().equals("00.00.0000")) {
                            date1 = new Date("01.01." + t1.getDateDebutPrestation().trim().substring(6, 10));
                        } else {
                            date1 = new Date(t1.getDateFinPrestation().trim());
                        }
                        if (t2.getDateFinPrestation().trim().equals("00.00.0000")) {
                            date2 = new Date("01.01." + t1.getDateDebutPrestation().trim().substring(6, 10));
                        } else {
                            date2 = new Date(t2.getDateFinPrestation().trim());
                        }
                    }
                    return date1.compareTo(date2);
                }
                return nomT1.compareTo(nomT2);

            }
        });
        for (APListePandemieControleV2Model model : listSheet) {
            createRow();
            this.createCell(model.getNss());
            this.createCell(model.getNom());
            this.createCell(model.getDateDebut1stPeriode());
            this.createCell(model.getDateFin1stPeriode());
            this.createCell(model.getDateDebutPrestation());
            this.createCell(model.getDateFinPrestation());
            this.createCell(model.getNbreJours());
            this.createCell(listCSGenreService.get(model.getGenreService()));
            this.createCell(listCSEtat.get(model.getEtat()));
            this.createCell(model.getIdDemande());
            this.createCell(model.getIdDroit());
            this.createCell(model.getIdTiers());
        }
    }

    private void createHeaderRows(boolean isGarde) {
        createRow();
        this.createCell(getSession().getLabel(HEADER_COL_1), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_2), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_3), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_4), styleHeader);
        if (isGarde) {
            this.createCell(getSession().getLabel(HEADER_COL_NBRE_JOURS), styleHeader);
        }
        this.createCell(getSession().getLabel(HEADER_COL_5), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_6), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_7), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_8), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_9), styleHeader);
    }

    private void createHeaderRowV2(boolean isDoubleDemande) {
        createRow();
        this.createCell(getSession().getLabel(HEADER_COL_V2_1), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_V2_2), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_V2_3), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_V2_4), styleHeader);
        if (!isDoubleDemande) {
            this.createCell(getSession().getLabel(HEADER_COL_V2_5), styleHeader);
            this.createCell(getSession().getLabel(HEADER_COL_V2_6), styleHeader);
            this.createCell(getSession().getLabel(HEADER_COL_V2_7), styleHeader);
            this.createCell(getSession().getLabel(HEADER_COL_V2_8), styleHeader);
        }
        this.createCell(getSession().getLabel(HEADER_COL_V2_9), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_V2_10), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_V2_11), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_V2_12), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_V2_13), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_V2_14), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_V2_15), styleHeader);
    }

    private void createHeaderRowV2JoursCarences() {
        createRow();
        this.createCell(getSession().getLabel(HEADER_COL_V2_1), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_V2_2), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_V2_DATE_DEBUT_1ST_PERIODE), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_V2_DATE_FIN_1ST_PERIODE), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_V2_3), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_V2_4), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_V2_6), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_V2_9), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_V2_10), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_V2_13), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_V2_14), styleHeader);
        this.createCell(getSession().getLabel(HEADER_COL_V2_15), styleHeader);
    }

    private void initColumnWidthSheetListePrestation(boolean isRule) {

        short numCol = 0;

        currentSheet.setColumnWidth(numCol++, APListePandemieControleExcel.COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, APListePandemieControleExcel.COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, APListePandemieControleExcel.COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, APListePandemieControleExcel.COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, APListePandemieControleExcel.COLUMN_WIDTH_BIG);
        currentSheet.setColumnWidth(numCol++, APListePandemieControleExcel.COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, APListePandemieControleExcel.COLUMN_WIDTH_MEDIUM);
        currentSheet.setColumnWidth(numCol++, APListePandemieControleExcel.COLUMN_WIDTH_MEDIUM);
    }


    public Map<String, List> getMapListes() {
        return mapListes;
    }

    public void setMapListes(Map<String, List> mapListes) {
        this.mapListes = mapListes;
    }

    public void setList(List<APListePandemieSuiviDossierModel> listSuivi) {
    }
}
