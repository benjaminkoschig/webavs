package globaz.naos.itext.masse;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.translation.CodeSystem;
import java.util.HashMap;
import java.util.LinkedList;

public class AFConfirmationSalaires_Doc extends AFAbstractSalaires_Doc {

    public static final String DOC_NO = "0003CAF";
    private static final long serialVersionUID = -2055750408035068368L;
    private boolean bloquerEnvoi = false;
    private AFCotisationManager cotisations = new AFCotisationManager();
    private String dateDebut;

    private String dateFin;
    private AFPlanAffiliation plan;

    public AFConfirmationSalaires_Doc() throws Exception {
    }

    public AFConfirmationSalaires_Doc(BSession session) throws Exception {
        super(session);
        setFileTitle(session.getLabel(AFSalaires_Param.NOMDOC_CONFIRMATION_SALAIRES));
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        // creer l'entete du doc
        // ---------------------------------------------------
        this.beforeBuildReport(loadNiveau(1), loadNiveau(3), null, loadNiveau(4), Boolean.FALSE);
        try {
            super.setDocumentTitle(affiliation().getAffilieNumero());
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    @Override
    public void createDataSource() throws Exception {
        fillDocInfo("");

        LinkedList lignes = new LinkedList();
        if (!loadPlan().isInactif().booleanValue()) {
            for (int idCotisation = 0; idCotisation < cotisations.size(); ++idCotisation) {
                AFCotisation cotisation = (AFCotisation) cotisations.get(idCotisation);

                if (JadeStringUtil.isEmpty((cotisation.getMassePeriodicite()))) {
                    continue;
                }

                if (cotisation.getAssurance().isSurDocAcompte().booleanValue()) {
                    HashMap champs = new HashMap();

                    champs.put(
                            AFSalaires_Param.F_VALEUR_COL1,
                            format(texte(2, 3),
                                    new String[] { cotisation.getAssurance().getAssuranceLibelleCourt(
                                            affiliation().getTiers().getLangueIso())
                                            + (CodeSystem.MOTIF_FIN_EXCEPTION.equals(cotisation.getMotifFin()) ? (" ("
                                                    + cotisation.getDateDebut() + "-" + cotisation.getDateFin() + ")")
                                                    : "") }));
                    champs.put(AFSalaires_Param.F_VALEUR_COL2,
                            format(texte(2, 4), new String[] { cotisation.getMassePeriodicite() }));
                    lignes.add(champs);
                }
            }
        }

        if (!lignes.isEmpty()) {
            this.setDataSource(lignes);
        }
    }

    @Override
    public void fillDocInfo(String anneeDocument) {
        getDocumentInfo().setSeparateDocument(isBloquerEnvoi());

        super.fillDocInfo(anneeDocument);
    }

    /**
     * getter pour l'attribut affiliation id.
     * 
     * @return la valeur courante de l'attribut affiliation id
     */
    @Override
    public String getAffiliationId() {
        return affiliationId;
    }

    public AFCotisationManager getCotisations() {
        return cotisations;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * getter pour l'attribut date envoi.
     * 
     * @return la valeur courante de l'attribut date envoi
     */
    @Override
    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public String getDateFin() {
        return dateFin;
    }

    /**
     * Retourne le numéro de document
     */
    @Override
    public String getNoDocument() {
        return AFConfirmationSalaires_Doc.DOC_NO;
    }

    public String getNomDoc() throws Exception {
        return getSession().getLabel(AFSalaires_Param.NOMDOC_CONFIRMATION_SALAIRES);
    }

    @Override
    protected String getTypeCatalogue() {
        return CodeSystem.TYPE_CAT_CONFIRMATION;
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

    /**
     * @return DOCUMENT ME!
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    public boolean next() throws FWIException {
        boolean retValue = hasNext;

        if (hasNext) {
            hasNext = false;
        }

        return retValue;
    }

    /**
     * setter pour l'attribut affiliation id.
     * 
     * @param affiliationId
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setAffiliationId(String affiliationId) {
        this.affiliationId = affiliationId;
    }

    public void setBloquerEnvoi(boolean bloquerEnvoi) {
        this.bloquerEnvoi = bloquerEnvoi;
    }

    public void setCotisations(AFCotisationManager cotisations) {
        this.cotisations = cotisations;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * setter pour l'attribut date envoi.
     * 
     * @param dateEnvoi
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }
}
