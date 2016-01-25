package globaz.apg.itext;

import globaz.apg.pojo.PrestationVerseeLignePrestationPojo;
import globaz.apg.pojo.PrestationVerseeLigneRecapitulationPojo;
import globaz.apg.pojo.PrestationVerseePojo;
import globaz.apg.process.APListePrestationVerseeProcess;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.jade.common.JadeCodingUtil;
import globaz.naos.application.AFApplication;
import globaz.pyxis.api.ITIRole;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APListePrestationVersee_Doc extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final static String FIELD_DOC_DATE_PAIEMENT = "F_DATE_PAIEMENT";
    private final static String FIELD_DOC_GENRE_SERVICE = "F_GENRE_SERVICE";
    private final static String FIELD_DOC_MONTANT_BRUT = "F_MONTANT_BRUT";

    private final static String FIELD_DOC_NOM_PRENOM = "F_NOM_PRENOM";
    private final static String FIELD_DOC_NSS = "F_NSS";
    private final static String FIELD_DOC_PERIODE_DETAIL = "F_PERIODE_DETAIL";

    private final static String FIELD_DOC_RECAPITULATION_GENRE_SERVICE = "F_RECAPITULATION_GENRE_SERVICE";
    private final static String FIELD_DOC_RECAPITULATION_MONTANT_BRUT = "F_RECAPITULATION_MONTANT_BRUT";
    private final static String FIELD_DOC_RECAPITULATION_NOMBRE_CAS = "F_RECAPITULATION_NOMBRE_CAS";
    private final static String FIELD_DOC_TOTAL_HEADER_RECAP_LABEL = "F_TOTAL_HEADER_RECAP_LABEL";
    private final static String FIELD_DOC_TOTAL_HEADER_RECAP_NBR_CAS = "F_TOTAL_HEADER_RECAP_NBR_CAS";
    private final static String FIELD_DOC_TOTAL_MONTANT_BRUT = "F_TOTAL_MONTANT_BRUT";

    private final static String MODEL_NAME = "AP_LISTE_PRESTATION_VERSEE";
    public final static String NUM_INFOROM = "5043PAP";

    private final static String PARAM_DOC_DATE_PAIEMENT_LABEL = "P_DATE_PAIEMENT_LABEL";
    private final static String PARAM_DOC_DATE_TRAITEMENT = "P_DATE_TRAITEMENT";

    private final static String PARAM_DOC_DATE_TRAITEMENT_LABEL = "P_DATE_TRAITEMENT_LABEL";
    private final static String PARAM_DOC_GENRE_SERVICE_LABEL = "P_GENRE_SERVICE_LABEL";
    private final static String PARAM_DOC_MONTANT_BRUT_LABEL = "P_MONTANT_BRUT_LABEL";
    private final static String PARAM_DOC_NOM_PRENOM_LABEL = "P_NOM_PRENOM_LABEL";
    private final static String PARAM_DOC_NSS_LABEL = "P_NSS_LABEL";
    private final static String PARAM_DOC_NUMERO_AFFILIE = "P_NUMERO_AFFILIE";
    private final static String PARAM_DOC_NUMERO_AFFILIE_LABEL = "P_NUMERO_AFFILIE_LABEL";
    private final static String PARAM_DOC_PERIODE = "P_PERIODE";
    private final static String PARAM_DOC_PERIODE_DETAIL_LABEL = "P_PERIODE_DETAIL_LABEL";
    private final static String PARAM_DOC_PERIODE_LABEL = "P_PERIODE_LABEL";
    private final static String PARAM_DOC_SELECTEUR_PRESTATION = "P_SELECTEUR_PRESTATION";
    private final static String PARAM_DOC_SELECTEUR_PRESTATION_LABEL = "P_SELECTEUR_PRESTATION_LABEL";

    private final static String PARAM_DOC_TITRE = "P_TITRE";

    private PrestationVerseePojo aPrestationVerseePojo;
    private Boolean envoyerGed;
    private boolean isFirst = true;

    @Override
    public void beforeBuildReport() throws FWIException {
        // Nothing todo
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        // Nothing todo
    }

    @Override
    public void createDataSource() throws Exception {

        // set du template
        setTemplateFile(APListePrestationVersee_Doc.MODEL_NAME);

        setDocumentTitle(aPrestationVerseePojo.getNumeroAffilie());

        // remplissage du docInfo et du titre du doc
        fillDocInfo();

        // envoi des paramètres dans le template
        this.setParametres(APListePrestationVersee_Doc.PARAM_DOC_TITRE,
                getSession().getLabel(APListePrestationVerseeProcess.LABEL_TITRE));
        this.setParametres(APListePrestationVersee_Doc.PARAM_DOC_DATE_TRAITEMENT_LABEL,
                getSession().getLabel(APListePrestationVerseeProcess.LABEL_DATE_TRAITEMENT));
        this.setParametres(APListePrestationVersee_Doc.PARAM_DOC_NUMERO_AFFILIE_LABEL,
                getSession().getLabel(APListePrestationVerseeProcess.LABEL_NUMERO_AFFILIE));
        this.setParametres(APListePrestationVersee_Doc.PARAM_DOC_SELECTEUR_PRESTATION_LABEL,
                getSession().getLabel(APListePrestationVerseeProcess.LABEL_SELECTEUR_PRESTATION));
        this.setParametres(APListePrestationVersee_Doc.PARAM_DOC_PERIODE_LABEL,
                getSession().getLabel(APListePrestationVerseeProcess.LABEL_PERIODE));
        this.setParametres(APListePrestationVersee_Doc.PARAM_DOC_GENRE_SERVICE_LABEL,
                getSession().getLabel(APListePrestationVerseeProcess.LABEL_GENRE_SERVICE));
        this.setParametres(APListePrestationVersee_Doc.PARAM_DOC_NSS_LABEL,
                getSession().getLabel(APListePrestationVerseeProcess.LABEL_NSS));
        this.setParametres(APListePrestationVersee_Doc.PARAM_DOC_NOM_PRENOM_LABEL,
                getSession().getLabel(APListePrestationVerseeProcess.LABEL_NOM_PRENOM));
        this.setParametres(APListePrestationVersee_Doc.PARAM_DOC_PERIODE_DETAIL_LABEL,
                getSession().getLabel(APListePrestationVerseeProcess.LABEL_PERIODE_DETAIL));
        this.setParametres(APListePrestationVersee_Doc.PARAM_DOC_MONTANT_BRUT_LABEL,
                getSession().getLabel(APListePrestationVerseeProcess.LABEL_MONTANT_BRUT));
        this.setParametres(APListePrestationVersee_Doc.PARAM_DOC_DATE_PAIEMENT_LABEL,
                getSession().getLabel(APListePrestationVerseeProcess.LABEL_DATE_PAIEMENT));

        this.setParametres(APListePrestationVersee_Doc.PARAM_DOC_DATE_TRAITEMENT, JACalendar.todayJJsMMsAAAA());
        this.setParametres(APListePrestationVersee_Doc.PARAM_DOC_NUMERO_AFFILIE, aPrestationVerseePojo
                .getNumeroAffilie().trim() + " - " + aPrestationVerseePojo.getNomAffilie());
        this.setParametres(APListePrestationVersee_Doc.PARAM_DOC_SELECTEUR_PRESTATION,
                aPrestationVerseePojo.getSelecteurPrestationLibelle());
        this.setParametres(APListePrestationVersee_Doc.PARAM_DOC_PERIODE, aPrestationVerseePojo.getDateDebut() + " - "
                + aPrestationVerseePojo.getDateFin());

        // envoi des fields dans le template
        List<Map<String, String>> DataSource = new ArrayList<Map<String, String>>();
        Map<String, String> aDataSourceRow;

        // créer lignes prest
        for (PrestationVerseeLignePrestationPojo aLignePrestationVerseePojo : aPrestationVerseePojo
                .getListeLignePrestationVersee()) {

            aDataSourceRow = new HashMap<String, String>();

            aDataSourceRow.put(
                    APListePrestationVersee_Doc.FIELD_DOC_GENRE_SERVICE,
                    aLignePrestationVerseePojo.getGenreService() + " "
                            + aLignePrestationVerseePojo.getGenrePrestationLibelle());
            aDataSourceRow.put(APListePrestationVersee_Doc.FIELD_DOC_NSS, aLignePrestationVerseePojo.getNss());
            aDataSourceRow.put(APListePrestationVersee_Doc.FIELD_DOC_NOM_PRENOM, aLignePrestationVerseePojo.getNom()
                    + " " + aLignePrestationVerseePojo.getPrenom());
            aDataSourceRow.put(APListePrestationVersee_Doc.FIELD_DOC_PERIODE_DETAIL,
                    aLignePrestationVerseePojo.getDateDebut() + " - " + aLignePrestationVerseePojo.getDateFin());
            aDataSourceRow.put(APListePrestationVersee_Doc.FIELD_DOC_MONTANT_BRUT, new FWCurrency(
                    aLignePrestationVerseePojo.getMontantBrut()).toStringFormat());
            aDataSourceRow.put(APListePrestationVersee_Doc.FIELD_DOC_DATE_PAIEMENT,
                    aLignePrestationVerseePojo.getDatePaiement());

            DataSource.add(aDataSourceRow);

        }

        // créer 1 ligne vide
        aDataSourceRow = new HashMap<String, String>();
        aDataSourceRow.put(APListePrestationVersee_Doc.FIELD_DOC_GENRE_SERVICE, " ");
        DataSource.add(aDataSourceRow);

        // créer ligne total
        aDataSourceRow = new HashMap<String, String>();
        String totalLibelle = getSession().getLabel(APListePrestationVerseeProcess.LABEL_TOTAUX) + " "
                + aPrestationVerseePojo.getNombreCasTotal() + " "
                + getSession().getLabel(APListePrestationVerseeProcess.LABEL_CAS);
        aDataSourceRow.put(APListePrestationVersee_Doc.FIELD_DOC_TOTAL_HEADER_RECAP_LABEL, totalLibelle);
        aDataSourceRow.put(APListePrestationVersee_Doc.FIELD_DOC_TOTAL_MONTANT_BRUT, new FWCurrency(
                aPrestationVerseePojo.getMontantBrutTotal()).toStringFormat());
        DataSource.add(aDataSourceRow);

        // créer 3 lignes vides
        aDataSourceRow = new HashMap<String, String>();
        aDataSourceRow.put(APListePrestationVersee_Doc.FIELD_DOC_GENRE_SERVICE, " ");
        DataSource.add(aDataSourceRow);

        aDataSourceRow = new HashMap<String, String>();
        aDataSourceRow.put(APListePrestationVersee_Doc.FIELD_DOC_GENRE_SERVICE, " ");
        DataSource.add(aDataSourceRow);

        aDataSourceRow = new HashMap<String, String>();
        aDataSourceRow.put(APListePrestationVersee_Doc.FIELD_DOC_GENRE_SERVICE, " ");
        DataSource.add(aDataSourceRow);

        // créer ligne header récap
        aDataSourceRow = new HashMap<String, String>();
        aDataSourceRow.put(APListePrestationVersee_Doc.FIELD_DOC_TOTAL_HEADER_RECAP_LABEL,
                getSession().getLabel(APListePrestationVerseeProcess.LABEL_RECAPITULATION));
        aDataSourceRow.put(APListePrestationVersee_Doc.FIELD_DOC_TOTAL_HEADER_RECAP_NBR_CAS,
                getSession().getLabel(APListePrestationVerseeProcess.LABEL_NOMBRE_CAS));
        DataSource.add(aDataSourceRow);

        // créer lignes récap
        for (PrestationVerseeLigneRecapitulationPojo aLigneRecapitulationPrestationVerseePojo : aPrestationVerseePojo
                .getMapLigneRecapitulationPrestationVersee().values()) {

            aDataSourceRow = new HashMap<String, String>();

            String recapitulationGenreService = aLigneRecapitulationPrestationVerseePojo.getGenreService() + " "
                    + aLigneRecapitulationPrestationVerseePojo.getLibelleGenreService() + " "
                    + aLigneRecapitulationPrestationVerseePojo.getGenrePrestationLibelle();
            aDataSourceRow.put(APListePrestationVersee_Doc.FIELD_DOC_RECAPITULATION_GENRE_SERVICE,
                    recapitulationGenreService);
            aDataSourceRow.put(APListePrestationVersee_Doc.FIELD_DOC_RECAPITULATION_NOMBRE_CAS,
                    aLigneRecapitulationPrestationVerseePojo.getNombreCas());
            aDataSourceRow.put(APListePrestationVersee_Doc.FIELD_DOC_RECAPITULATION_MONTANT_BRUT, new FWCurrency(
                    aLigneRecapitulationPrestationVerseePojo.getMontantBrut()).toStringFormat());

            DataSource.add(aDataSourceRow);
        }

        this.setDataSource(DataSource);

    }

    private void fillDocInfo() throws JAException {

        getDocumentInfo().setArchiveDocument(false);
        if (getEnvoyerGed()) {
            getDocumentInfo().setArchiveDocument(true);
        }

        getDocumentInfo().setPublishDocument(false);
        getDocumentInfo().setDocumentTypeNumber(APListePrestationVersee_Doc.NUM_INFOROM);
        getDocumentInfo().setDocumentProperty("annee",
                Integer.toString(JACalendar.getYear(JACalendar.todayJJsMMsAAAA())));

        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", aPrestationVerseePojo.getNumeroAffilie());
        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(aPrestationVerseePojo.getNumeroAffilie()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    aPrestationVerseePojo.getNumeroAffilie());
        }

        try {
            TIDocumentInfoHelper.fill(getDocumentInfo(), aPrestationVerseePojo.getIdTiers(), getSession(),
                    ITIRole.CS_AFFILIE, getDocumentInfo().getDocumentProperty("numero.affilie.formatte"),
                    getDocumentInfo().getDocumentProperty("numero.affilie.non.formatte"));
        } catch (Exception e) {
            JadeCodingUtil.catchException(this, "createDataSource()", e);
        }

    }

    public PrestationVerseePojo getaPrestationVerseePojo() {
        return aPrestationVerseePojo;
    }

    public Boolean getEnvoyerGed() {
        return envoyerGed;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    public boolean next() throws FWIException {

        if (isFirst) {
            isFirst = false;
            return true;
        }

        return false;
    }

    public void setaPrestationVerseePojo(PrestationVerseePojo aPrestationVerseePojo) {
        this.aPrestationVerseePojo = aPrestationVerseePojo;
    }

    public void setEnvoyerGed(Boolean envoyerGed) {
        this.envoyerGed = envoyerGed;
    }

}
