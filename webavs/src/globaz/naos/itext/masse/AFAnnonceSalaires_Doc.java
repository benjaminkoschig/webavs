package globaz.naos.itext.masse;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.process.LEGenererEnvoi;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.naos.application.AFApplication;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.db.releve.AFApercuReleve;
import globaz.naos.db.releve.AFApercuReleveManager;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TIRole;
import java.util.HashMap;
import java.util.LinkedList;

public class AFAnnonceSalaires_Doc extends AFAbstractSalaires_Doc {

    private static final String DOC_NO = "0004CAF";
    private static final long serialVersionUID = -6176532859800657174L;
    private boolean bloquerEnvoi = false;
    private String dateDebut;
    private String dateFin;
    private String datePeriodeSuivi = "";

    private boolean decompteAnnuel = false;

    private boolean ignoreDocument = false;
    private boolean impressionMasse = false;
    private JADate jaDateDebut;

    private JADate jaDateFin;
    private AFPlanAffiliation plan;

    public AFAnnonceSalaires_Doc() throws Exception {
    }

    public AFAnnonceSalaires_Doc(BSession session) throws Exception {
        super(session);
        setFileTitle(session.getLabel(AFSalaires_Param.NOMDOC_ANNONCE_SALAIRES));
    }

    /**
     * Renseigne les champs du document avec les textes du catalogue.
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     * 
     * @see globaz.framework.printing.itext.FWIDocumentManager#beforeBuildReport()
     */
    @Override
    public void beforeBuildReport() throws FWIException {

        if (!ignoreDocument) {
            // dates de la période.
            // reprendre la date proposée en fonction de la périodicité de
            // l'affiliation sauf pour les annuelles si traitement de masse ->
            // déduire en fonction de la dernière période facturée
            if (decompteAnnuel && impressionMasse) {
                jaDateDebut = new JADate(1, 1, jaDateDebut.getYear());
                // Recherche des relevé saisi
                try {
                    AFApercuReleveManager mgr = new AFApercuReleveManager();
                    mgr.setSession(getSession());
                    mgr.setForAffilieNumero(affiliation().getAffilieNumero());
                    mgr.setForIdTiers(affiliation().getTiers().getIdTiers());
                    mgr.setForPlanAffiliationId(loadPlan().getPlanAffiliationId());
                    mgr.setFromDateDebut(jaDateDebut.toString());
                    mgr.find();
                    if (mgr.size() > 0) {
                        // si un ou plusieurs relevés existent, on prend le
                        // dernier en date pour calculer la date de début
                        AFApercuReleve rel = (AFApercuReleve) mgr.getFirstEntity();
                        JACalendar cal = new JACalendarGregorian();
                        jaDateDebut = cal.addDays(new JADate(rel.getDateFin()), 1);
                    }
                } catch (Exception ex) {
                    // on laisse la date au 1.1
                }

            }
            String datePeriode = "";
            datePeriodeSuivi = "";

            if (jaDateDebut.getMonth() != jaDateFin.getMonth()) {
                datePeriode += JACalendar.getMonthName(jaDateDebut.getMonth(), getIsoLangueDestinataire());
                datePeriode += " " + getSession().getLabel("NAOS_ENTREDATE") + " ";
                datePeriodeSuivi += jaDateDebut.getMonth() + "-";
            }
            datePeriode += JACalendar.getMonthName(jaDateFin.getMonth(), getIsoLangueDestinataire());
            datePeriode += " ";
            datePeriode += String.valueOf(jaDateFin.getYear());
            datePeriodeSuivi += jaDateFin.getMonth() + "." + jaDateFin.getYear();

            if (Character.isUpperCase(texte(1, 2).charAt(1))) {
                datePeriode = datePeriode.toUpperCase();
            }

            try {

                this.beforeBuildReport(
                        format(loadNiveau(1), new String[] { loadPlan().getLibelleFactureNotEmpty(), datePeriode }),
                        format(loadNiveau(3),
                                new String[] { JACalendar.format(dateRetour, affiliation().getTiers().getLangueIso()) }),
                        texte(4, 1), texte(4, 2), Boolean.TRUE);

                // démarrage du suivi
                if (isDejaJournalise()) {
                    getMemoryLog().logMessage("suivi déjà présent", "", FWMessage.INFORMATION);
                } else {
                    genererControle();
                }

            } catch (Exception ex) {
                throw new FWIException(ex);
            }
        }
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        super.beforeExecuteReport();

        try {
            jaDateDebut = new JADate(dateDebut);
            jaDateFin = new JADate(dateFin);
        } catch (JAException e) {
            throw new FWIException("Dates de période invalide: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean beforePrintDocument() {
        return ((size() > 0) && !isAborted() && !ignoreDocument);
    }

    /**
     * Cree une ligne pour chacune des assurances de ce plan d'affiliation.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.framework.printing.itext.FWIDocumentManager#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        fillDocInfo("");
        // creer la source de donnees
        LinkedList<HashMap<String, String>> lignes = new LinkedList<HashMap<String, String>>();
        LinkedList<AFCotisation> cotiList = new LinkedList<AFCotisation>();

        if (!loadPlan().isInactif().booleanValue()) {
            // charger les cotisations pour le plan d'affiliation courant
            AFCotisationManager cotisations = new AFCotisationManager();

            cotisations.setForPlanAffiliationId(planAffiliationId);
            cotisations.setForAffiliationId(affiliationId);
            cotisations.setNotForTypeAssurance(CodeSystem.TYPE_ASS_FFPP);
            cotisations.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
            // cotisations.setForDate(dateEnvoi);
            cotisations.setForPeriode(dateDebut, dateFin);
            cotisations.setForNotMotifFin(CodeSystem.MOTIF_FIN_EXCEPTION);
            cotisations.setSession(getSession());
            // inclure les annuelles sans mois de facturation si impression
            // manuelle
            if (!impressionMasse) {
                cotisations.setWithAnnuelZero(true);
            }
            cotisations.setOrder("IDEXTERNE");
            cotisations.find();

            for (int idCotisation = 0; idCotisation < cotisations.size(); ++idCotisation) {
                AFCotisation cotisation = (AFCotisation) cotisations.get(idCotisation);
                if (impressionMasse && !decompteAnnuel
                        && CodeSystem.PERIODICITE_ANNUELLE.equals(cotisation.getPeriodicite())) {
                    // flag pour le recacul de la date de début
                    decompteAnnuel = true;
                }
                if (cotisation.getAssurance().isSurDocAcompte().booleanValue()) {
                    // test si pas déjà présent
                    if (!isAleardyPresent(cotiList, cotisation)) {
                        HashMap<String, String> champs = new HashMap<String, String>();
                        champs.put(
                                AFSalaires_Param.F_VALEUR_COL1,
                                format(texte(2, 3),
                                        new String[] { cotisation.getAssurance().getAssuranceLibelleCourt(
                                                affiliation().getTiers().getLangueIso()) }));
                        champs.put(AFSalaires_Param.F_VALEUR_COL2, "_________________");
                        lignes.add(champs);
                        cotiList.add(cotisation);
                    }
                }
            }
        }

        // texte pour FFPP
        String texteFFPP = null;
        try {
            texteFFPP = texte(2, 4);
        } catch (Exception ex) {
            // si le paragraphe n'existe pas, on ignore
        }
        if (!JadeStringUtil.isEmpty(texteFFPP) && (jaDateFin.getMonth() == 12)) {
            // ajouter la ligne pour la lamat genève
            lignes.add(new HashMap<String, String>()); // ajouter une ligne vide

            HashMap<String, String> champs = new HashMap<String, String>();

            champs.put(
                    AFSalaires_Param.F_VALEUR_COL1,
                    format(texteFFPP,
                            new String[] { JACalendar.getMonthName(jaDateFin.getMonth(), getIsoLangueDestinataire())
                                    .toUpperCase() }));
            champs.put(AFSalaires_Param.F_VALEUR_COL2, "_________________");
            lignes.add(champs);

            lignes.add(new HashMap<String, String>()); // ajouter une ligne vide
        }
        if (lignes.size() == 0) {
            ignoreDocument = true;
        } else {
            this.setDataSource(lignes);
        }

    }

    @Override
    public void fillDocInfo(String anneeDocument) {
        getDocumentInfo().setSeparateDocument(isBloquerEnvoi());
        super.fillDocInfo(anneeDocument);
    }

    public void genererControle() throws Exception {
        // prépare les données pour l'envoi

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(ILEConstantes.CS_PARAM_GEN_ID_AFFILIATION, affiliation().getAffiliationId());
        params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS, affiliation().getTiers().getIdTiers());
        params.put(ILEConstantes.CS_PARAM_GEN_NUMERO, affiliation().getAffilieNumero());
        // params.put(ILEConstantes.CS_PARAM_GEN_ROLE,
        // CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(getSession().getApplication()));
        params.put(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
        params.put(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE, getSession().getApplicationId());
        params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE, affiliation().getTiers().getIdTiers());

        params.put(ILEConstantes.CS_PARAM_GEN_PERIODE, datePeriodeSuivi);
        params.put(ILEConstantes.CS_PARAM_GEN_PLAN, loadPlan().getLibelleFactureNotEmpty());

        // execute le process de génération
        LEGenererEnvoi gen = new LEGenererEnvoi();
        gen.setSession(getSession());
        gen.setParamEnvoiList(params);
        gen.setSendCompletionMail(false);
        gen.setGenerateEtapeSuivante(new Boolean(true));
        gen.setCsDocument(ILEConstantes.CS_DEBUT_SUIVI_RELEVES);
        gen.setDateRappel(dateRetour);
        gen.executeProcess();
    }

    /**
     * getter pour l'attribut date debut.
     * 
     * @return la valeur courante de l'attribut date debut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * getter pour l'attribut date fin.
     * 
     * @return la valeur courante de l'attribut date fin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * Retourne le numéro de document
     */
    @Override
    public String getNoDocument() {
        return AFAnnonceSalaires_Doc.DOC_NO;
    }

    public String getNomDoc() throws Exception {
        return getSession().getLabel(AFSalaires_Param.NOMDOC_ANNONCE_SALAIRES);
    }

    protected String getTemplate() {
        return AFSalaires_Param.TEMPLATE_SALAIRES;
    }

    @Override
    protected String getTypeCatalogue() {
        return CodeSystem.TYPE_CAT_ANNONCE;
    }

    public boolean isBloquerEnvoi() {
        return bloquerEnvoi;
    }

    private boolean isDejaJournalise() throws Exception {
        // On sette les critères qui font que l'envoi est unique
        LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();

        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                AFApplication.DEFAULT_APPLICATION_NAOS);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, affiliation().getAffilieNumero());
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, datePeriodeSuivi);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PLAN, loadPlan().getLibelleFactureNotEmpty());

        LUJournalListViewBean viewBean = new LUJournalListViewBean();

        viewBean.setSession(getSession());
        viewBean.setProvenance(provenanceCriteres);
        viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_CATEGORIE_GROUPE);
        viewBean.setForValeurCodeSysteme(ILEConstantes.CS_CATEGORIE_SUIVI_RELEVES);
        viewBean.find(getTransaction());
        // Si le viewBean retourne un enregistrement c'est que l'envoi a déjà
        // été journalisé donc on retourne true
        if (viewBean.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isImpressionMasse() {
        return impressionMasse;
    }

    private AFPlanAffiliation loadPlan() throws FWIException {
        if (plan == null) {
            plan = new AFPlanAffiliation();
            plan.setPlanAffiliationId(planAffiliationId);
            plan.setSession(getSession());

            try {
                plan.retrieve();
            } catch (Exception e) {
                throw new FWIException("Impossible de charger le plan de l'affiliation: " + e.getMessage(), e);
            }
        }

        return plan;
    }

    @Override
    public boolean next() throws FWIException {

        boolean retValue = hasNext;

        if (hasNext) {
            hasNext = false;
        }

        return retValue;
    }

    /** Prépare un nouveau document pour un nouvel affilié et un nouveau plan. */
    public void resetFull() {
        super.reset();
        resetPlan();
    }

    /** Prépare un nouveau document pour le même affilié et un autre plan. */
    public void resetPlan() {
        hasNext = true;
        plan = null;
        jaDateDebut = null;
        jaDateFin = null;
    }

    public void setBloquerEnvoi(boolean bloquerEnvoi) {
        this.bloquerEnvoi = bloquerEnvoi;
    }

    /**
     * setter pour l'attribut date debut.
     * 
     * @param dateDebut
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * setter pour l'attribut date fin.
     * 
     * @param dateFin
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setImpressionMasse(boolean impressionMasse) {
        this.impressionMasse = impressionMasse;
    }

}
