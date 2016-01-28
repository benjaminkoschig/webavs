package globaz.naos.itext.acompte;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.itext.masse.AFAbstractSalaires_Doc;
import globaz.naos.itext.masse.AFSalaires_Param;
import globaz.naos.translation.CodeSystem;
import java.util.HashMap;
import java.util.LinkedList;

public class AFPrevisionAcompte_Doc extends AFAbstractSalaires_Doc {

    // Numéro du document
    private static final String DOC_NO = "0155CAF";
    private static final long serialVersionUID = -6176532859800657174L;

    private boolean bloquerEnvoi = false;
    AFCotisationManager cotisations = new AFCotisationManager();
    // champs a usage interne
    private String dateDebut;
    private String dateFin;
    private JADate jaDateFin;
    private AFPlanAffiliation plan;

    public AFPrevisionAcompte_Doc() throws Exception {
    }

    public AFPrevisionAcompte_Doc(BSession session) throws Exception {
        super(session);
        setFileTitle(session.getLabel(AFSalaires_Param.NOMDOC_PREVISION_ACOMPTE));
    }

    /**
     * Renseigne les champs du document avec les textes du catalogue.
     * 
     */
    @Override
    public void beforeBuildReport() throws FWIException {

        String datePeriode = "";
        datePeriode = String.valueOf(jaDateFin.getYear());

        if (Character.isUpperCase(texte(1, 2).charAt(1))) {
            datePeriode = datePeriode.toUpperCase();
        }

        try {

            this.beforeBuildReport(
                    format(loadNiveau(1), new String[] { loadPlan().getLibelleFactureNotEmpty(), datePeriode }),
                    format(loadNiveau(3),
                            new String[] { JACalendar.format(dateRetour, affiliation().getTiers().getLangueIso()) }),
                    texte(4, 1), texte(4, 2), Boolean.TRUE);
        } catch (Exception ex) {
            throw new FWIException(ex);
        }
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        setTemplateFile(AFSalaires_Param.TEMPLATE_PREVISION);

        try {
            new JADate(dateDebut);
            jaDateFin = new JADate(dateFin);
        } catch (JAException e) {
            throw new FWIException("Dates de période invalide: " + e.getMessage(), e);
        }

    }

    /**
     * Cree une ligne pour chacune des assurances de ce plan d'affiliation.
     */
    @Override
    public void createDataSource() throws Exception {
        fillDocInfo(Integer.toString(JACalendar.getYear(getDateDebut())));
        // creer la source de donnees
        LinkedList<HashMap<String, String>> lignes = new LinkedList<HashMap<String, String>>();
        LinkedList<AFCotisation> cotiList = new LinkedList<AFCotisation>();
        getDocumentInfo().setDocumentTitle(affiliation().getAffilieNumero());

        if (!loadPlan().isInactif().booleanValue()) {
            for (int idCotisation = 0; idCotisation < cotisations.size(); ++idCotisation) {
                AFCotisation cotisation = (AFCotisation) cotisations.get(idCotisation);
                HashMap<String, String> champs = new HashMap<String, String>();

                if (cotisation.getAssurance().isSurDocAcompte().booleanValue()) {
                    if (!isAleardyPresent(cotiList, cotisation)) {
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

        if (lignes.size() > 0) {
            this.setDataSource(lignes);
        }
    }

    @Override
    public void fillDocInfo(String anneeDocument) {
        getDocumentInfo().setSeparateDocument(isBloquerEnvoi());
        super.fillDocInfo(anneeDocument);
    }

    public AFCotisationManager getCotisations() {
        return cotisations;
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
        return AFPrevisionAcompte_Doc.DOC_NO;
    }

    public String getNomDoc() throws Exception {
        return getSession().getLabel(AFSalaires_Param.NOMDOC_PREVISION_ACOMPTE);
    }

    protected String getTemplate() {
        return AFSalaires_Param.TEMPLATE_PREVISION;
    }

    @Override
    protected String getTypeCatalogue() {
        return CodeSystem.PREVISION_ACOMPTE;
    }

    public boolean isBloquerEnvoi() {
        return bloquerEnvoi;
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
        jaDateFin = null;
    }

    public void setBloquerEnvoi(boolean bloquerEnvoi) {
        this.bloquerEnvoi = bloquerEnvoi;
    }

    public void setCotisations(AFCotisationManager cotisations) {
        this.cotisations = cotisations;
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
}
