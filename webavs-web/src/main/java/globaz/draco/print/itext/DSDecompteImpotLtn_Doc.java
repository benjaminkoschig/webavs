package globaz.draco.print.itext;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.commons.nss.NSUtil;
import globaz.draco.application.DSApplication;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuelles;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.hercule.service.CEDocumentItextService;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import globaz.naos.db.assurance.AFCalculAssurance;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.db.adressecourrier.TILocaliteManager;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIHistoriqueAvsManager;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.CommonExcelmlContainer;
import globaz.webavs.common.CommonExcelmlUtils;
import globaz.webavs.common.ICommonConstantes;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author BJO <h1>description</h1> Permet d'imprimer la liste pour les autorités fiscales pour un canton comprenant
 *         tous les salariés dont l'employeur s'est acquitté de ses cotisations. Calculer la commission de 10% du
 *         montant fiscal et le déduire.
 */
public class DSDecompteImpotLtn_Doc extends FWIDocumentManager {
    private static final long serialVersionUID = -7273212247090676365L;
    public static final String DS_ATTESTATION = "125005";
    public static final String DS_DOMAINE = "124001";
    private static final String MODEL_NAME = "DRACO_DECOMPTE_IMPOTS_PRELEVES_LTN";
    public static final String NUMERO_REFERENCE_INFOROM = "0199CDS";
    private static final String XLS_DOC_NAME = "ListeDecompteImpotsPrelevesLtn";
    private static final String XLS_DOC_NAME_EMPLOYEUR = "ListeDecompteImpotsPrelevesLtnEmployeur";

    private String annee;
    private AFAssurance assuranceImpotCantonal;

    private AFAssurance assuranceImpotFederal;
    private String canton;
    private ICTDocument catalogue;
    private BigDecimal commission;
    private List<Map<String, String>> dataSource = new ArrayList<Map<String, String>>();

    private String dateDebut;
    private String dateFin;

    private String dateNaissanceSalarie = "";
    private String dateValeur = JACalendar.todayJJsMMsAAAA();
    private String emailAddress;
    private BigDecimal impotCantonal;
    private BigDecimal impotFederal;
    private int indiceInscriptionIndividuelle = 0;
    private DSInscriptionsIndividuelles inscriptionInd;
    private boolean isFirst = true;
    private String langueIsoTiers;// langue du tier
    private ArrayList listInscriptionIndividuelle;

    private String localite = "";
    private String nomPrenomSalarie;
    private String npa = "";
    private String nssSalarie = "";
    private String pActiviteJ1;
    private String pActiviteJ2;
    private String pActiviteM1;
    private String pActiviteM2;
    private BigDecimal pourcentCommission;
    // information sur les salariés
    private String rue = "";
    private BigDecimal salaireBrutAvsSalarie;

    private boolean simulation = false;
    private AFTauxAssurance tauxCantonal;
    private AFTauxAssurance tauxFederal;
    private BigDecimal totalImpotEncaisse;
    private BigDecimal totalImpotSource;

    private BigDecimal totalSalaireBrut;
    private BigDecimal totalVerser;
    private String typeImpression = "pdf";

    private CommonExcelmlContainer xlsContainer = new CommonExcelmlContainer();

    public boolean isWantInfoEmplyoeur() {
        return wantInfoEmplyoeur;
    }

    public void setWantInfoEmplyoeur(boolean wantInfoEmplyoeur) {
        this.wantInfoEmplyoeur = wantInfoEmplyoeur;
    }

    private boolean wantInfoEmplyoeur = false;
    private String idDeclarationSalaireCourante = "";
    private String numeroAffilieEmployeur = "";
    private String raisonSocialeEmployeur = "";
    private String npaEmployeur = "";
    private String localiteEmployeur="";

    public DSDecompteImpotLtn_Doc() throws Exception {
        this(new BSession(DSApplication.DEFAULT_APPLICATION_DRACO));
    }

    /**
     * @param session
     * @param rootApplication
     * @param fileName
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public DSDecompteImpotLtn_Doc(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

    public DSDecompteImpotLtn_Doc(globaz.globall.db.BSession session) throws Exception {
        super(session, DSApplication.DEFAULT_APPLICATION_ROOT, "ImpressionDeclaration");
    }

    /**
     * Envoi les paramètre au header du documents
     * 
     * @param bean
     * @throws Exception
     */
    private void _setHeader(CaisseHeaderReportBean bean) throws Exception {
    }

    protected void abort(String message, String type) {
        getMemoryLog().logMessage(message, type, this.getClass().getName());
        this.abort();
    }

    public void addParametersMap() {
        Map param = getImporter().getParametre();
        Iterator<String> ite = param.entrySet().iterator();

        while (ite.hasNext()) {
            Object key = ite.next();

            if (key instanceof String) {
                xlsContainer.put((String) key, (String) param.get(key));
            }
        }
    }

    private boolean isTypeImpressionExcel(){
        return "xls".equalsIgnoreCase(getTypeImpression());
    }

    @Override
    public void afterBuildReport() {
        if ("xls".equals(getTypeImpression())) {
            try {
                printXlsDocument();
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
    }

    @Override
    public void afterExecuteReport() {
    }

    protected void afterTable() throws Exception {
        super.setParametres(DSDecompteImpotLtn_Param.P_TOTAUX_LABEL, getSession().getLabel("TOTAUX"));
        super.setParametres(DSDecompteImpotLtn_Param.P_COMMISSION_LABEL, getSession().getLabel("COMMISSION") + " "
                + pourcentCommission.toString() + "%" + " " + getSession().getLabel("EN_FAVEUR_CAISSE"));
        super.setParametres(DSDecompteImpotLtn_Param.P_TOTAL_VERSER_LABEL, getSession().getLabel("TOTAL_VERSER"));

        super.setParametres(DSDecompteImpotLtn_Param.P_SALAIRE_BRUT_TOTAL, new FWCurrency(getTotalSalaireBrut()
                .toString()).toStringFormat());
        super.setParametres(DSDecompteImpotLtn_Param.P_IMPOT_ENCAISSE_TOTAL, new FWCurrency(getTotalImpotEncaisse()
                .toString()).toStringFormat());

        // calcul de la commission
        commission = getTotalImpotEncaisse().multiply(pourcentCommission).divide(new BigDecimal(100), 1);
        commission = JANumberFormatter.formatBigD(commission);// arrondie le
        // montant au
        // plus proche
        super.setParametres(DSDecompteImpotLtn_Param.P_COMMISSION,
                "-" + new FWCurrency(getCommission().toString()).toStringFormat());

        // calcul du total à verser à l'autorité
        totalVerser = getTotalImpotEncaisse().subtract(getCommission());
        super.setParametres(DSDecompteImpotLtn_Param.P_TOTAL_VERSER,
                new FWCurrency(getTotalVerser().toString()).toStringFormat());
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        try {
            setDocumentTitle(getSession().getLabel("DECOMPTE_LTN"));
            super.setTemplateFile(DSDecompteImpotLtn_Doc.MODEL_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        try {
            // pour sélectionner le label en fonction de la langue
            // getSession().getApplication().getLabel(id, isoLanguage);

            // langue de la session
            langueIsoTiers = getSession().getIdLangueISO();
            dateDebut = "01.01." + annee;
            dateFin = "31.12." + annee;

            totalSalaireBrut = new BigDecimal(0);
            totalImpotEncaisse = new BigDecimal(0);
            pourcentCommission = new BigDecimal(10);

            setFileTitle(getSession().getLabel("DECOMPTE_IMPOTS_LTN_TITRE_MAIL") + " " + getCanton());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean beforePrintDocument() {
        if ("xls".equals(getTypeImpression())) {
            return false;
        }

        return super.beforePrintDocument();
    }

    protected void beforeTable() throws Exception {
        super.setParametres(DSDecompteImpotLtn_Param.P_TITRE, getSession().getLabel("DECOMPTE_IMPOTS_LTN_TITRE"));
        super.setParametres(DSDecompteImpotLtn_Param.P_PERIODE_DECLARATION,
                getSession().getLabel("ENTETE_DECLARATIONPERIODE") + " 01.01." + getAnnee() + " - " + "31.12."
                        + getAnnee());
        super.setParametres(DSDecompteImpotLtn_Param.P_CANTON, getSession().getLabel("AUTORITE_FISCALE") + " : "
                + getCanton());
        super.setParametres(DSDecompteImpotLtn_Param.P_DATE, getSession().getLabel("ENVOI_DATE") + " : "
                + getDateValeur());
        super.setParametres(DSDecompteImpotLtn_Param.P_PAGE, getSession().getLabel("PAGE"));
    }

    protected ICTDocument catalogue() throws FWIException {
        if (catalogue == null) {
            try {
                // Recherche le catalogue
                ICTDocument helper = (ICTDocument) getSession().getAPIFor(ICTDocument.class);

                helper.setCsDomaine(DSDecompteImpotLtn_Doc.DS_DOMAINE); // domaine
                // avs
                helper.setCsTypeDocument(DSDecompteImpotLtn_Doc.DS_ATTESTATION); // pour
                // le
                // type
                // de
                // catalogue
                helper.setCodeIsoLangue(langueIsoTiers); // dans la langue de
                // l'affilié
                helper.setActif(Boolean.TRUE); // actif
                helper.setDefault(Boolean.TRUE); // et par défaut

                // charger le catalogue de texte
                ICTDocument[] candidats = helper.load();

                if ((candidats != null) && (candidats.length > 0)) {
                    catalogue = candidats[0];
                }
            } catch (Exception e) {
                catalogue = null;
            }
        }

        if (catalogue == null) {
            this.abort("impossible de trouver le catalogue de texte", FWMessage.ERREUR);
            throw new FWIException("impossible de trouver le catalogue de texte");
        }

        return catalogue;
    }

    protected void columnTableHeader() {
        super.setParametres(DSDecompteImpotLtn_Param.P_EMPLOYES_COL_LABEL, getSession().getLabel("EMPLOYES"));
        super.setParametres(DSDecompteImpotLtn_Param.P_ADRESSE_COL_LABEL, getSession().getLabel("ADRESSE"));
        super.setParametres(DSDecompteImpotLtn_Param.P_PERIODE_ACTIVITE_COL_LABEL,
                getSession().getLabel("ENTETE_PERIODETRAVAIL"));
        super.setParametres(DSDecompteImpotLtn_Param.P_SALAIRE_BRUT_COL_LABEL, getSession().getLabel("SALAIRE_BRUT"));
        super.setParametres(DSDecompteImpotLtn_Param.P_IMPOT_ENCAISSE_COL_LABEL, getSession()
                .getLabel("IMPOT_ENCAISSE"));
        super.setParametres(DSDecompteImpotLtn_Param.P_NSS_COL_LABEL, getSession().getLabel("ENTETE_AVS"));
        super.setParametres(DSDecompteImpotLtn_Param.P_DATE_NAISSANCE_COL_LABEL, getSession()
                .getLabel("DATE_NAISSANCE"));
        super.setParametres(DSDecompteImpotLtn_Param.P_NOM_PRENOM_COL_LABEL, getSession().getLabel("ENTETE_NOMPRENOM"));
        super.setParametres(DSDecompteImpotLtn_Param.P_RUE_COL_LABEL, getSession().getLabel("RUE_ET_NUMERO"));
        super.setParametres(DSDecompteImpotLtn_Param.P_NPA_COL_LABEL, getSession().getLabel("NPA"));
        super.setParametres(DSDecompteImpotLtn_Param.P_LIEU_COL_LABEL, getSession().getLabel("LIEU"));
        super.setParametres(DSDecompteImpotLtn_Param.P_DEBUT_COL_LABEL, getSession().getLabel("ENTETE_DEBUT"));
        super.setParametres(DSDecompteImpotLtn_Param.P_FIN_COL_LABEL, getSession().getLabel("ENTETE_FIN"));
        super.setParametres(DSDecompteImpotLtn_Param.P_JOUR1_COL_LABEL, getSession().getLabel("ENVOI_JOUR"));
        super.setParametres(DSDecompteImpotLtn_Param.P_MOIS1_COL_LABEL, getSession().getLabel("ENVOI_MOIS"));
        super.setParametres(DSDecompteImpotLtn_Param.P_JOUR2_COL_LABEL, getSession().getLabel("ENVOI_JOUR"));
        super.setParametres(DSDecompteImpotLtn_Param.P_MOIS2_COL_LABEL, getSession().getLabel("ENVOI_MOIS"));
    }

    @Override
    public void createDataSource() throws Exception {
        // insertion du numéro inforom
        getDocumentInfo().setDocumentTypeNumber(DSDecompteImpotLtn_Doc.NUMERO_REFERENCE_INFOROM);
        getDocumentInfo().setDocumentProperty("annee", getAnnee());
        if (simulation) {
            getDocumentInfo().setDocumentProperty("simulation", "true");
        }

        // insertion du header de la caisse
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), langueIsoTiers);
        setTemplateFile(DSDecompteImpotLtn_Doc.MODEL_NAME);
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        _setHeader(headerBean);
        caisseReportHelper.addHeaderParameters(this, headerBean);
        getImporter().getParametre().put(
                ICaisseReportHelper.PARAM_SUBREPORT_HEADER,
                ((ACaisseReportHelper) caisseReportHelper).getDefaultModelPath() + "/"
                        + getTemplateProperty(getDocumentInfo(), "header.filename.declaration"));

        // insertion des informations générales
        beforeTable();

        // insertion des titres des colonnes
        columnTableHeader();

        // insertion des lignes de données
        List<Map<String, String>> listDataSource = getListLignesSalaries();
        if (!listDataSource.isEmpty()) {
            dataSource.addAll(listDataSource);
            this.setDataSource(dataSource);
        }

        // insertion des totaux
        afterTable();
    }

    private void enteteXlsEmployeur(){
        xlsContainer.put(DSDecompteImpotLtn_Param.P_COL_EMPLOYEUR_LABEL, getSession().getLabel("EMPLOYEUR"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_NUM_AFF_LABEL, getSession().getLabel("ENTETE_NUMEROAFFILIE"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_RAIS_SOC_LABEL, getSession().getLabel("DRACO_LISTE_RAISON_SOCIALE"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_NPA_AFF_LABEL, getSession().getLabel("NPA"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_LIEU_AFF_LABEL, getSession().getLabel("LIEU"));
    }

    private void enteteXls() {

        xlsContainer.put("NUMERO_INFOROM", DSDecompteImpotLtn_Doc.NUMERO_REFERENCE_INFOROM);
        xlsContainer.put(DSDecompteImpotLtn_Param.P_TITRE, getSession().getLabel("DECOMPTE_IMPOTS_LTN_TITRE"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_PERIODE_DECLARATION,
                getSession().getLabel("ENTETE_DECLARATIONPERIODE") + " 01.01." + getAnnee() + " - " + "31.12."
                        + getAnnee());
        xlsContainer.put(DSDecompteImpotLtn_Param.P_CANTON, getSession().getLabel("AUTORITE_FISCALE") + " : "
                + getCanton());
        xlsContainer.put(DSDecompteImpotLtn_Param.P_DATE, getSession().getLabel("ENVOI_DATE"));
        xlsContainer.put("P_DATE_VALUE", getDateValeur());

        xlsContainer.put(DSDecompteImpotLtn_Param.P_EMPLOYES_COL_LABEL, getSession().getLabel("EMPLOYES"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_ADRESSE_COL_LABEL, getSession().getLabel("ADRESSE"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_PERIODE_ACTIVITE_COL_LABEL,
                getSession().getLabel("ENTETE_PERIODETRAVAIL"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_SALAIRE_BRUT_COL_LABEL, getSession().getLabel("SALAIRE_BRUT"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_IMPOT_ENCAISSE_COL_LABEL, getSession().getLabel("IMPOT_ENCAISSE"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_NSS_COL_LABEL, getSession().getLabel("ENTETE_AVS"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_DATE_NAISSANCE_COL_LABEL, getSession().getLabel("DATE_NAISSANCE"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_NOM_PRENOM_COL_LABEL, getSession().getLabel("ENTETE_NOMPRENOM"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_RUE_COL_LABEL, getSession().getLabel("RUE_ET_NUMERO"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_NPA_COL_LABEL, getSession().getLabel("NPA"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_LIEU_COL_LABEL, getSession().getLabel("LIEU"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_DEBUT_COL_LABEL, getSession().getLabel("ENTETE_DEBUT"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_FIN_COL_LABEL, getSession().getLabel("ENTETE_FIN"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_JOUR1_COL_LABEL, getSession().getLabel("ENVOI_JOUR"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_MOIS1_COL_LABEL, getSession().getLabel("ENVOI_MOIS"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_JOUR2_COL_LABEL, getSession().getLabel("ENVOI_JOUR"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_MOIS2_COL_LABEL, getSession().getLabel("ENVOI_MOIS"));

        xlsContainer.put(DSDecompteImpotLtn_Param.P_TOTAUX_LABEL, getSession().getLabel("TOTAUX"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_COMMISSION_LABEL, getSession().getLabel("COMMISSION") + " "
                + pourcentCommission.toString() + "%" + " " + getSession().getLabel("EN_FAVEUR_CAISSE"));
        xlsContainer.put(DSDecompteImpotLtn_Param.P_TOTAL_VERSER_LABEL, getSession().getLabel("TOTAL_VERSER"));

        xlsContainer.put(DSDecompteImpotLtn_Param.P_COMMISSION, "-" + getCommission().toString());
        xlsContainer.put(DSDecompteImpotLtn_Param.P_TOTAL_VERSER, getTotalVerser().toString());
    }

    /**
     * remplace dans message {n} par args[n].
     * 
     * @param message
     *            le message dans lequel se trouve les groupes à remplacer
     * @param args
     *            les valeurs de remplacement (les nulls sont permis, ils seront remplacés par "")
     * @return le message formatté
     * @see MessageFormat
     */
    protected String formatMessage(StringBuffer message, Object[] args) {
        return CEDocumentItextService.formatMessage(message.toString(), (String[]) args);
    }

    public String getAnnee() {
        return annee;
    }

    public String getCanton() {
        return canton;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public String getDateNaissanceSalarie() {
        return dateNaissanceSalarie;
    }

    public String getDateValeur() {
        return dateValeur;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public ArrayList getListInscriptionIndividuelle() {
        return listInscriptionIndividuelle;
    }


    private void fillFieldsEmployeurs(Map<String, String> fields){
        fields.put(DSDecompteImpotLtn_Param.F_NUM_AFF,numeroAffilieEmployeur);
        fields.put(DSDecompteImpotLtn_Param.F_RAIS_SOC,raisonSocialeEmployeur);
        fields.put(DSDecompteImpotLtn_Param.F_NPA_AFF,npaEmployeur);
        fields.put(DSDecompteImpotLtn_Param.F_LIEU_AFF,localiteEmployeur);
    }

    private List<Map<String, String>> getListLignesSalaries() {
        List<Map<String, String>> lignes = new ArrayList<Map<String, String>>();
        for (int i = 0; i < listInscriptionIndividuelle.size(); i++) {
            Map<String, String> fields = new HashMap<String, String>();

            // récupération sur le salarié en cours et insertion des valeur dans
            // les champs
            recupInfoSalarie();

            if(isWantInfoEmplyoeur() && isTypeImpressionExcel()){
                fillFieldsEmployeurs(fields);
            }

            fields.put(DSDecompteImpotLtn_Param.F_NSS, getNssSalarie());
            fields.put(DSDecompteImpotLtn_Param.F_DATE_NAISSANCE, getDateNaissanceSalarie());
            fields.put(DSDecompteImpotLtn_Param.F_NOM, getNomPrenomSalarie());
            fields.put(DSDecompteImpotLtn_Param.F_RUE, getRue());
            fields.put(DSDecompteImpotLtn_Param.F_NPA, getNpa());
            fields.put(DSDecompteImpotLtn_Param.F_LIEU, getLocalite());
            fields.put(DSDecompteImpotLtn_Param.F_JOUR1, getPActiviteJ1());
            fields.put(DSDecompteImpotLtn_Param.F_MOIS1, getPActiviteM1());
            fields.put(DSDecompteImpotLtn_Param.F_JOUR2, getPActiviteJ2());
            fields.put(DSDecompteImpotLtn_Param.F_MOIS2, getPActiviteM2());
            fields.put(DSDecompteImpotLtn_Param.F_SALAIRE_BRUT,
                    new FWCurrency(getSalaireBrutAvsSalarie().toString()).toStringFormat());
            fields.put(DSDecompteImpotLtn_Param.F_IMPOT_ENCAISSE,
                    new FWCurrency(getTotalImpotSource().toString()).toStringFormat());
            fields.put(DSDecompteImpotLtn_Param.F_SALAIRE_BRUT_SS_FMT, getSalaireBrutAvsSalarie().toString());
            fields.put(DSDecompteImpotLtn_Param.F_IMPOT_ENCAISSE_SS_FMT, getTotalImpotSource().toString());

            lignes.add(fields);
        }
        return lignes;
    }

    public String getLocalite() {
        return localite;
    }

    public String getNomPrenomSalarie() {
        return nomPrenomSalarie;
    }

    public String getNpa() {
        return npa;
    }

    public String getNssSalarie() {
        return nssSalarie;
    }

    public String getPActiviteJ1() {
        return pActiviteJ1;
    }

    public String getPActiviteJ2() {
        return pActiviteJ2;
    }

    public String getPActiviteM1() {
        return pActiviteM1;
    }

    public String getPActiviteM2() {
        return pActiviteM2;
    }

    public String getRue() {
        return rue;
    }

    public BigDecimal getSalaireBrutAvsSalarie() {
        return salaireBrutAvsSalarie;
    }

    public boolean getSimulation() {
        return simulation;
    }

    /**
     * Récupère le texte du catalogue en fonction du niveau et de la position, et remplace les {n} par les textes passés
     * dans le tableau d'objet "args"
     * 
     * @param niveau
     * @param position
     * @param args
     * @return
     * @throws FWIException
     */
    protected String getTexte(int niveau, int position, Object[] args) throws FWIException {
        String texte;
        try {
            if (args != null) {
                texte = CEDocumentItextService.formatMessage(catalogue().getTextes(niveau).getTexte(position)
                        .getDescription(), (String[]) args);
            } else {
                texte = catalogue().getTextes(niveau).getTexte(position).getDescription();
            }
            return texte;
        } catch (FWIException e) {
            throw e;
        } catch (Exception e) {
            return "";
        }
    }

    public BigDecimal getTotalImpotEncaisse() {
        return totalImpotEncaisse;
    }

    public BigDecimal getTotalImpotSource() {
        return totalImpotSource;
    }

    public BigDecimal getTotalSalaireBrut() {
        return totalSalaireBrut;
    }

    public BigDecimal getTotalVerser() {
        return totalVerser;
    }

    public String getTypeImpression() {
        return typeImpression;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    public boolean next() throws FWIException {
        // si First est à true on va au suivant sinon on s'arrête
        if (isFirst) {
            isFirst = false;
            return true;
        }
        return false;
    }


    private void fillExcelEmployeurContent(Map<String, String> fields){
        this.remplirColumn(DSDecompteImpotLtn_Param.F_NUM_AFF,fields.get(DSDecompteImpotLtn_Param.F_NUM_AFF),"");
        this.remplirColumn(DSDecompteImpotLtn_Param.F_RAIS_SOC,fields.get(DSDecompteImpotLtn_Param.F_RAIS_SOC),"");
        this.remplirColumn(DSDecompteImpotLtn_Param.F_NPA_AFF,fields.get(DSDecompteImpotLtn_Param.F_NPA_AFF),"");
        this.remplirColumn( DSDecompteImpotLtn_Param.F_LIEU_AFF,fields.get(DSDecompteImpotLtn_Param.F_LIEU_AFF),"");
    }
    /**
     * Préparation des données pour le document excel
     */
    private void prepareDataForXLSDoc() {

        BigDecimal totalSalaire = new BigDecimal(0);
        BigDecimal totalImpots = new BigDecimal(0);

        //Ajoute les entêtes de l'employeur si désirées
        if(isWantInfoEmplyoeur()){
            enteteXlsEmployeur();
        }

        // Entetes du fichier excel
        enteteXls();

        // liste
        int size = dataSource.size();
        for (int i = 0; i < size; i++) {

            Map<String, String> bean = dataSource.get(i);

            if(isWantInfoEmplyoeur()){
                fillExcelEmployeurContent(bean);
            }

            this.remplirColumn(DSDecompteImpotLtn_Param.F_NSS, bean.get(DSDecompteImpotLtn_Param.F_NSS), "");
            this.remplirColumn(DSDecompteImpotLtn_Param.F_DATE_NAISSANCE,
                    bean.get(DSDecompteImpotLtn_Param.F_DATE_NAISSANCE), "");
            this.remplirColumn(DSDecompteImpotLtn_Param.F_NOM, bean.get(DSDecompteImpotLtn_Param.F_NOM), "");
            this.remplirColumn(DSDecompteImpotLtn_Param.F_RUE, bean.get(DSDecompteImpotLtn_Param.F_RUE), "");
            this.remplirColumn(DSDecompteImpotLtn_Param.F_NPA, bean.get(DSDecompteImpotLtn_Param.F_NPA), "");
            this.remplirColumn(DSDecompteImpotLtn_Param.F_LIEU, bean.get(DSDecompteImpotLtn_Param.F_LIEU), "");
            this.remplirColumn(DSDecompteImpotLtn_Param.F_JOUR1, bean.get(DSDecompteImpotLtn_Param.F_JOUR1), "");
            this.remplirColumn(DSDecompteImpotLtn_Param.F_MOIS1, bean.get(DSDecompteImpotLtn_Param.F_MOIS1), "");
            this.remplirColumn(DSDecompteImpotLtn_Param.F_JOUR2, bean.get(DSDecompteImpotLtn_Param.F_JOUR2), "");
            this.remplirColumn(DSDecompteImpotLtn_Param.F_MOIS2, bean.get(DSDecompteImpotLtn_Param.F_MOIS2), "");
            this.remplirColumn(DSDecompteImpotLtn_Param.F_SALAIRE_BRUT,
                    bean.get(DSDecompteImpotLtn_Param.F_SALAIRE_BRUT_SS_FMT), "");
            this.remplirColumn(DSDecompteImpotLtn_Param.F_IMPOT_ENCAISSE,
                    bean.get(DSDecompteImpotLtn_Param.F_IMPOT_ENCAISSE_SS_FMT), "");

            if (!JadeStringUtil.isEmpty(bean.get(DSDecompteImpotLtn_Param.F_SALAIRE_BRUT_SS_FMT))) {
                totalSalaire = totalSalaire
                        .add(new BigDecimal(bean.get(DSDecompteImpotLtn_Param.F_SALAIRE_BRUT_SS_FMT)));
            }
            if (!JadeStringUtil.isEmpty(bean.get(DSDecompteImpotLtn_Param.F_IMPOT_ENCAISSE_SS_FMT))) {
                totalImpots = totalImpots
                        .add(new BigDecimal(bean.get(DSDecompteImpotLtn_Param.F_IMPOT_ENCAISSE_SS_FMT)));
            }
        }

        this.remplirColumn("P_SALAIRE_BRUT_TOTAL", totalSalaire.toString(), "0.00");
        this.remplirColumn("P_IMPOT_ENCAISSE_TOTAL", totalImpots.toString(), "0.00");
    }

    public void printXlsDocument() throws Exception {

        try {

            String xmlModelPath = Jade.getInstance().getExternalModelDir() + DSApplication.DEFAULT_APPLICATION_ROOT
                    + "/model/excelml/" + DSDecompteImpotLtn_Doc.XLS_DOC_NAME + "Modele.xml";

            if(isWantInfoEmplyoeur()){
                xmlModelPath = Jade.getInstance().getExternalModelDir() + DSApplication.DEFAULT_APPLICATION_ROOT
                        + "/model/excelml/" + DSDecompteImpotLtn_Doc.XLS_DOC_NAME_EMPLOYEUR + "Modele.xml";
            }

            String xlsDocPath = Jade.getInstance().getPersistenceDir()
                    + JadeFilenameUtil.addOrReplaceFilenameSuffixUID(DSDecompteImpotLtn_Doc.XLS_DOC_NAME + ".xml");

            if(isWantInfoEmplyoeur()){
                xlsDocPath = Jade.getInstance().getPersistenceDir()
                        + JadeFilenameUtil.addOrReplaceFilenameSuffixUID(DSDecompteImpotLtn_Doc.XLS_DOC_NAME_EMPLOYEUR + ".xml");
            }

            prepareDataForXLSDoc();

            xlsDocPath = CommonExcelmlUtils.createDocumentExcel(xmlModelPath, xlsDocPath, xlsContainer);

            JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
            docInfoExcel.setApplicationDomain(DSApplication.DEFAULT_APPLICATION_DRACO);
            docInfoExcel.setDocumentTitle(DSDecompteImpotLtn_Doc.XLS_DOC_NAME);
            if(isWantInfoEmplyoeur()){
                docInfoExcel.setDocumentTitle(DSDecompteImpotLtn_Doc.XLS_DOC_NAME_EMPLOYEUR);
            }
            docInfoExcel.setPublishDocument(true);
            docInfoExcel.setArchiveDocument(false);
            docInfoExcel.setDocumentTypeNumber(DSDecompteImpotLtn_Doc.NUMERO_REFERENCE_INFOROM);
            this.registerAttachedDocument(docInfoExcel, xlsDocPath);
        } catch (Exception e) {
            throw new Exception("Error generating excel file", e);
        }
    }

    private void recuperationInformationEmployeur(String idDeclarationSalaire) throws Exception {

        numeroAffilieEmployeur = "";
        raisonSocialeEmployeur = "";
        npaEmployeur = "";
        localiteEmployeur="";

        DSDeclarationViewBean declaration = new DSDeclarationViewBean();
        declaration.setSession(getSession());
        declaration.setIdDeclaration(idDeclarationSalaire);
        declaration.retrieve();

        AFAffiliation affiliation = new AFAffiliation();
        affiliation.setSession(getSession());
        affiliation.setAffiliationId(declaration.getAffiliationId());
        affiliation.retrieve();

        TITiersViewBean tiers = new TITiersViewBean();
        tiers.setSession(getSession());
        tiers.setIdTiers(affiliation.getIdTiers());
        tiers.retrieve();

        //On récupère l'adresse de domicile, pas besoin d'héritage
        TIAdresseDataSource adresseDataSource = tiers.getAdresseAsDataSource(
                IConstantes.CS_AVOIR_ADRESSE_DOMICILE, IConstantes.CS_APPLICATION_DEFAUT,
                JACalendar.todayJJsMMsAAAA(), false);

        numeroAffilieEmployeur = affiliation.getAffilieNumero();
        raisonSocialeEmployeur = affiliation.getRaisonSociale();

        if(adresseDataSource != null){
            npaEmployeur = adresseDataSource.localiteNpa;
            localiteEmployeur = adresseDataSource.localiteNom;
        }

    }

    /**
     * Récupère les informations sur le salarié en cours : nss,adresse,salaire avs brut,date de naissance
     */
    private void recupInfoSalarie() {
        try {
            inscriptionInd = (DSInscriptionsIndividuelles) listInscriptionIndividuelle
                    .get(indiceInscriptionIndividuelle);

            //récupération des informations de l'employeur uniquement si on change de déclaration de salaires
            if(isWantInfoEmplyoeur()  && isTypeImpressionExcel() && !idDeclarationSalaireCourante.equalsIgnoreCase(inscriptionInd.getIdDeclaration())){
                recuperationInformationEmployeur(inscriptionInd.getIdDeclaration());
            }

            idDeclarationSalaireCourante = inscriptionInd.getIdDeclaration();

            // récupération du nss
            nssSalarie = inscriptionInd.getNumeroAvs().toString();
            nssSalarie = NSUtil.formatAVSUnknown(nssSalarie);
            // salaire avs brut
            // salaireBrutAvsSalarie = new
            // FWCurrency(inscriptionInd.getMontant());
            if (inscriptionInd.getGenreEcriture().equals("11")) {
                salaireBrutAvsSalarie = new BigDecimal("-" + inscriptionInd.getMontant());
            } else {
                salaireBrutAvsSalarie = new BigDecimal(inscriptionInd.getMontant());
            }
            // salaireBrutAvsSalarie = new BigDecimal(inscriptionInd.getMontant());

            pActiviteJ1 = inscriptionInd.getJourDebut();
            pActiviteM1 = inscriptionInd.getMoisDebut();
            pActiviteJ2 = inscriptionInd.getJourFin();
            pActiviteM2 = inscriptionInd.getMoisFin();

            TIPersonneAvsManager personneAvsManager = new TIPersonneAvsManager();
            personneAvsManager.setSession(getSession());
            personneAvsManager.setForNumAvsActuel(nssSalarie);
            personneAvsManager.find();
            TITiersViewBean tiers = (TITiersViewBean) personneAvsManager.getFirstEntity();
            if (tiers == null) {
                System.out.println("tiers non trouvé -> recherche dans l'historique : " + nssSalarie);
                TIHistoriqueAvsManager historiqueAVSManager = new TIHistoriqueAvsManager();
                historiqueAVSManager.setSession(getSession());
                historiqueAVSManager.setForNumAvs(nssSalarie);
                historiqueAVSManager.find();
                if (historiqueAVSManager.size() > 0) {
                    TIHistoriqueAvs historiqueAvs = (TIHistoriqueAvs) historiqueAVSManager.getFirstEntity();
                    tiers = new TITiersViewBean();
                    tiers.setSession(getSession());
                    tiers.setIdTiers(historiqueAvs.getIdTiers());
                    tiers.retrieve();
                }
            }
            nomPrenomSalarie = tiers.getNomPrenom();
            dateNaissanceSalarie = tiers.getDateNaissance();
            rue = tiers.getRue();
            localite = tiers.getLocalite();

            TIAvoirAdresse avoirAdresse = TITiers.findAvoirAdresse(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    IConstantes.CS_APPLICATION_DEFAUT, JACalendar.todayJJsMMsAAAA(), tiers.getIdTiers(), getSession());
            if (avoirAdresse == null) {
                System.out.println("avoir adresse null : " + nssSalarie);
                getMemoryLog().logMessage(getSession().getLabel("ERROR_GETTING_ADRESSE") + nssSalarie,
                        FWMessage.ERREUR, this.getClass().toString());
                this.abort();
            }
            TILocaliteManager localiteManager = new TILocaliteManager();
            localiteManager.setSession(getSession());
            // BZ 8482 - Inclusion des localités inactives
            localiteManager.setInclureInactif(true);
            localiteManager.setForIdLocalite(avoirAdresse.getIdLocalite());
            localiteManager.find();
            TILocalite localitetiers = (TILocalite) localiteManager.getFirstEntity();

            if (localitetiers != null) {
                npa = localitetiers.getNumPostal();
            }

            // calcul des taux de l'impot à la source cantonal en fonction du
            // canton du salarié
            if (!JadeStringUtil.isBlankOrZero(tiers.getIdCantonDomicile())) {
                AFAssuranceManager assuranceManager = new AFAssuranceManager();
                assuranceManager.setSession(getSession());
                assuranceManager.setForTypeAssurance(CodeSystem.TYPE_ASS_IMPOT_SOURCE);
                assuranceManager.setForCanton(tiers.getIdCantonDomicile());
                assuranceManager.find();

                if (assuranceManager.size() >= 1) {
                    assuranceImpotCantonal = (AFAssurance) assuranceManager.getFirstEntity();
                    tauxCantonal = assuranceImpotCantonal.getTaux(dateFin);
                } else {
                    throw new Exception(getSession().getLabel("AUCUNE_ASSURANCE_LTN_POUR_TIERS"));
                }
            }
            if (tauxCantonal == null) {
                throw new Exception(getSession().getLabel("AUCUN_TAUX_POUR_CANTON"));
            }

            impotCantonal = new BigDecimal(
                    AFCalculAssurance.calculResultatAssurance(dateDebut, dateFin, tauxCantonal, new Double(
                            JANumberFormatter.deQuote(salaireBrutAvsSalarie.toString())).doubleValue(), getSession()));
            impotCantonal = JANumberFormatter.formatBigD(impotCantonal);// arrondie
            // le
            // montant
            // au
            // plus
            // proche

            // calcul de l'impot fédéral
            AFAssuranceManager assuranceManager = new AFAssuranceManager();
            assuranceManager.setSession(getSession());
            assuranceManager.setForTypeAssurance(CodeSystem.TYPE_ASS_IMPOT_SOURCE);
            assuranceManager.setForCanton("0");// canton = 0 pour le fédéral
            assuranceManager.find();
            if (assuranceManager.size() >= 1) {
                assuranceImpotFederal = (AFAssurance) assuranceManager.getFirstEntity();
                tauxFederal = assuranceImpotFederal.getTaux(dateFin);
            } else {
                throw new Exception(getSession().getLabel("AUCUNE_ASSURANCE_LTN_POUR_TIERS"));
            }
            if (tauxFederal == null) {
                throw new Exception("Aucun taux pour l'impot fédéral");
            }

            impotFederal = new BigDecimal(
                    AFCalculAssurance.calculResultatAssurance(dateDebut, dateFin, tauxFederal, new Double(
                            JANumberFormatter.deQuote(salaireBrutAvsSalarie.toString())).doubleValue(), getSession()));
            impotFederal = JANumberFormatter.formatBigD(impotFederal);// arrondie
            // le
            // montant
            // au
            // plus
            // proche

            // calcul du total de l'impot
            totalImpotSource = new BigDecimal(0);
            totalImpotSource = totalImpotSource.add(impotCantonal);
            totalImpotSource = totalImpotSource.add(impotFederal);
            totalImpotSource = JANumberFormatter.formatBigD(totalImpotSource);// arrondie
            // le
            // montant
            // au
            // plus
            // proche

            totalSalaireBrut = totalSalaireBrut.add(salaireBrutAvsSalarie);
            totalImpotEncaisse = totalImpotEncaisse.add(totalImpotSource);

            indiceInscriptionIndividuelle++;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remplirColumn(String column, BigDecimal value, String defaultValue) {
        if (value != null) {
            xlsContainer.put(column, value.toString());
        } else {
            xlsContainer.put(column, defaultValue);
        }
    }

    public void remplirColumn(String column, String value, String defaultValue) {
        if (!JadeStringUtil.isEmpty(value)) {
            xlsContainer.put(column, value);
        } else {
            xlsContainer.put(column, defaultValue);
        }
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public void setDateNaissanceSalarie(String dateNaissanceSalarie) {
        this.dateNaissanceSalarie = dateNaissanceSalarie;
    }

    public void setDateValeur(String dateValeur) {
        this.dateValeur = dateValeur;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setListInscriptionIndividuelle(ArrayList listInscriptionIndividuelle) {
        this.listInscriptionIndividuelle = listInscriptionIndividuelle;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public void setNomPrenomSalarie(String nomPrenomSalarie) {
        this.nomPrenomSalarie = nomPrenomSalarie;
    }

    public void setNpa(String npa) {
        this.npa = npa;
    }

    public void setNssSalarie(String nssSalarie) {
        this.nssSalarie = nssSalarie;
    }

    public void setPActiviteJ1(String pActiviteJ1) {
        this.pActiviteJ1 = pActiviteJ1;
    }

    public void setPActiviteJ2(String pActiviteJ2) {
        this.pActiviteJ2 = pActiviteJ2;
    }

    public void setPActiviteM1(String pActiviteM1) {
        this.pActiviteM1 = pActiviteM1;
    }

    public void setPActiviteM2(String pActiviteM2) {
        this.pActiviteM2 = pActiviteM2;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public void setSalaireBrutAvsSalarie(BigDecimal salaireBrutAvsSalarie) {
        this.salaireBrutAvsSalarie = salaireBrutAvsSalarie;
    }

    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }

    public void setTotalImpotEncaisse(BigDecimal totalImpotEncaisse) {
        this.totalImpotEncaisse = totalImpotEncaisse;
    }

    public void setTotalImpotSource(BigDecimal totalImpotSource) {
        this.totalImpotSource = totalImpotSource;
    }

    public void setTotalSalaireBrut(BigDecimal totalSalaireBrut) {
        this.totalSalaireBrut = totalSalaireBrut;
    }

    public void setTotalVerser(BigDecimal totalVerser) {
        this.totalVerser = totalVerser;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }
}
