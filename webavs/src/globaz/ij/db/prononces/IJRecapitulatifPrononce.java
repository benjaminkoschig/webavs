/*
 * Créé le 14 sept. 05
 */
package globaz.ij.db.prononces;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAVector;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJRecapitulatifPrononce extends IJPrononce {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected static String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononce.TABLE_NAME_PRONONCE);

        // jointure entre les tables des grandes ij, petite ij, AIT, Alloc.
        // Assist. et la table des prononces
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJGrandeIJ.TABLE_NAME_GRANDE_IJ);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononce.TABLE_NAME_PRONONCE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononce.FIELDNAME_ID_PRONONCE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJGrandeIJ.TABLE_NAME_GRANDE_IJ);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJGrandeIJ.FIELDNAME_ID_PRONONCE_GRANDE_IJ);

        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPetiteIJ.TABLE_NAME_PETITE_IJ);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononce.TABLE_NAME_PRONONCE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononce.FIELDNAME_ID_PRONONCE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPetiteIJ.TABLE_NAME_PETITE_IJ);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPetiteIJ.FIELDNAME_ID_PRONONCE_PETITE_IJ);

        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononceAit.TABLE_NAME_PRONONCE_AIT);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononce.TABLE_NAME_PRONONCE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononce.FIELDNAME_ID_PRONONCE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononceAit.TABLE_NAME_PRONONCE_AIT);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononceAit.FIELDNAME_ID_PRONONCE_AIT);

        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononceAllocAssistance.TABLE_NAME_PRONONCE_ALLOC_ASSISTANCE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononce.TABLE_NAME_PRONONCE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononce.FIELDNAME_ID_PRONONCE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononceAllocAssistance.TABLE_NAME_PRONONCE_ALLOC_ASSISTANCE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononceAllocAssistance.FIELDNAME_ID_PRONONCE_ALLOC_ASSISTANCE);

        // jointure avec les revenus
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJRevenu.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononce.TABLE_NAME_PRONONCE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononce.FIELDNAME_ID_REVENU_READAPTATION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJRevenu.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJRevenu.FIELDNAME_ID_REVENU);

        return fromClauseBuffer.toString();
    }

    private IJMesureJointAgentExecution[] agentsExecution = null;
    private Boolean alocationExploitation = Boolean.FALSE;
    private String anneeNiveauManqueAGagner = "";
    private String anneeNiveauReadaptation = "";
    private String csEtatCivil = "";
    private String csGenreReadaptation = "";
    private String csModeCalcul = "";
    private String csPeriodiciteManqueAGagner = "";
    private String csPeriodiciteRevenuReadaptation = "";
    private JAVector employeurs = null;
    private String fromClause = null;
    private String heuresSemaineManqueAGagner = "";
    private String heuresSemainesReadaptation = "";
    private String idRevenuPetiteIJ = "";
    private String incapacite3emeRevision = "";
    private String montantAit = "";
    private String montantIndemniteAssistance = "";
    private String montantManqueAGagner = "";
    private String montantRevenuReadaptation = "";
    private String montantTotal = "";

    private String noAVS = "";
    private String nom = "";
    private String nombreEnfantsACharge = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String prenom = "";

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        super._afterRetrieve(transaction);

        // agents execution
        IJMesureJointAgentExecutionManager mesureJointAgentExecutionManager = new IJMesureJointAgentExecutionManager();
        mesureJointAgentExecutionManager.setSession(getSession());
        mesureJointAgentExecutionManager.setForIdPrononce(getIdPrononce());
        mesureJointAgentExecutionManager.find(transaction);

        agentsExecution = new IJMesureJointAgentExecution[mesureJointAgentExecutionManager.size()];

        for (int i = 0; i < mesureJointAgentExecutionManager.size(); i++) {
            agentsExecution[i] = (IJMesureJointAgentExecution) mesureJointAgentExecutionManager.getEntity(i);
        }

        // requerant
        PRTiersWrapper tiers = loadDemande(transaction).loadTiers();
        nom = tiers.getProperty(PRTiersWrapper.PROPERTY_NOM);
        prenom = tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
        noAVS = tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        csEtatCivil = tiers.getProperty(PRTiersWrapper.PROPERTY_PERSONNE_AVS_ETAT_CIVIL);

        // TODO nombre enfants a charge

        // manque a gagner
        if (!JadeStringUtil.isIntegerEmpty(idRevenuPetiteIJ)) {
            IJRevenu revenu = new IJRevenu();
            revenu.setSession(getSession());
            revenu.setIdRevenu(idRevenuPetiteIJ);
            revenu.retrieve(transaction);

            montantManqueAGagner = revenu.getRevenu();
            csPeriodiciteManqueAGagner = revenu.getCsPeriodiciteRevenu();
            heuresSemaineManqueAGagner = revenu.getNbHeuresSemaine();
            anneeNiveauManqueAGagner = revenu.getAnnee();
        }

        // employeurs
        if (IIJPrononce.CS_GRANDE_IJ.equals(getCsTypeIJ()) || IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(getCsTypeIJ())) {
            IJSitProJointEmployeurManager sitProJointEmployeurManager = new IJSitProJointEmployeurManager();
            sitProJointEmployeurManager.setSession(getSession());
            sitProJointEmployeurManager.setForIdGrandeIJ(getIdPrononce());
            sitProJointEmployeurManager.find(transaction);

            employeurs = sitProJointEmployeurManager.getContainer();
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return IJPrononce.TABLE_NAME_PRONONCE;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        montantRevenuReadaptation = statement.dbReadNumeric(IJRevenu.FIELDNAME_REVENU, 2);
        csPeriodiciteRevenuReadaptation = statement.dbReadNumeric(IJRevenu.FIELDNAME_CS_PERIODICITE_REVENU);
        heuresSemainesReadaptation = statement.dbReadNumeric(IJRevenu.FIELDNAME_NB_HEURES_SEMAINE);
        anneeNiveauReadaptation = statement.dbReadNumeric(IJRevenu.FIELDNAME_ANNEE);
        montantIndemniteAssistance = statement.dbReadNumeric(IJGrandeIJ.FIELDNAME_MONTANT_INDEMNITE_ASSISTANCE);
        incapacite3emeRevision = statement.dbReadNumeric(IJGrandeIJ.FIELDNAME_POURCENT_DEGRE_INCAPACITE_TRAVAIL);
        alocationExploitation = statement.dbReadBoolean(IJGrandeIJ.FIELDNAME_INDEMNITE_EXPLOITATION);
        csModeCalcul = statement.dbReadNumeric(IJPetiteIJ.FIELDNAME_CS_SITUATION_ASSURE);
        idRevenuPetiteIJ = statement.dbReadNumeric(IJPetiteIJ.FIELDNAME_ID_DERNIER_REVENU_OU_MANQUE_A_GAGNER);

        montantAit = statement.dbReadNumeric(IJPrononceAit.FIELDNAME_MONTANT);
        montantTotal = statement.dbReadNumeric(IJPrononceAllocAssistance.FIELDNAME_MONTANT_TOTAL);
        csGenreReadaptation = statement.dbReadNumeric(IJPrononce.FIELDNAME_CS_GENRE);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // lecture seule
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // lecture seule
    }

    /**
     * getter pour l'attribut agents execution
     * 
     * @return la valeur courante de l'attribut agents execution
     */
    public IJMesureJointAgentExecution[] getAgentsExecution() {
        return agentsExecution;
    }

    /**
     * getter pour l'attribut alocation exploitation
     * 
     * @return la valeur courante de l'attribut alocation exploitation
     */
    public Boolean getAlocationExploitation() {
        return alocationExploitation;
    }

    /**
     * getter pour l'attribut annee niveau manque AGagner
     * 
     * @return la valeur courante de l'attribut annee niveau manque AGagner
     */
    public String getAnneeNiveauManqueAGagner() {
        return anneeNiveauManqueAGagner;
    }

    /**
     * getter pour l'attribut annee niveau readaptation
     * 
     * @return la valeur courante de l'attribut annee niveau readaptation
     */
    public String getAnneeNiveauReadaptation() {
        return anneeNiveauReadaptation;
    }

    /**
     * getter pour l'attribut cs etat civil
     * 
     * @return la valeur courante de l'attribut cs etat civil
     */
    public String getCsEtatCivil() {
        return csEtatCivil;
    }

    public String getCsGenreReadaptation() {
        return csGenreReadaptation;
    }

    /**
     * getter pour l'attribut cs mode calcul
     * 
     * @return la valeur courante de l'attribut cs mode calcul
     */
    public String getCsModeCalcul() {
        return csModeCalcul;
    }

    /**
     * getter pour l'attribut periodicite manque AGagner
     * 
     * @return la valeur courante de l'attribut periodicite manque AGagner
     */
    public String getCsPeriodiciteManqueAGagner() {
        return csPeriodiciteManqueAGagner;
    }

    /**
     * getter pour l'attribut cs periodicite revenu readaptation
     * 
     * @return la valeur courante de l'attribut cs periodicite revenu readaptation
     */
    public String getCsPeriodiciteRevenuReadaptation() {
        return csPeriodiciteRevenuReadaptation;
    }

    /**
     * getter pour l'attribut employeurs
     * 
     * @return la valeur courante de l'attribut employeurs
     */
    public JAVector getEmployeurs() {
        return employeurs;
    }

    /**
     * getter pour l'attribut heures semaine manque AGagner
     * 
     * @return la valeur courante de l'attribut heures semaine manque AGagner
     */
    public String getHeuresSemaineManqueAGagner() {
        return heuresSemaineManqueAGagner;
    }

    /**
     * getter pour l'attribut heures semaines readaptation
     * 
     * @return la valeur courante de l'attribut heures semaines readaptation
     */
    public String getHeuresSemainesReadaptation() {
        return heuresSemainesReadaptation;
    }

    /**
     * getter pour l'attribut id revenu petite IJ
     * 
     * @return la valeur courante de l'attribut id revenu petite IJ
     */
    public String getIdRevenuPetiteIJ() {
        return idRevenuPetiteIJ;
    }

    /**
     * getter pour l'attribut incapacite3eme revision
     * 
     * @return la valeur courante de l'attribut incapacite3eme revision
     */
    public String getIncapacite3emeRevision() {
        return incapacite3emeRevision;
    }

    public String getMontantAit() {
        return montantAit;
    }

    /**
     * getter pour l'attribut montant indemnite assistance
     * 
     * @return la valeur courante de l'attribut montant indemnite assistance
     */
    public String getMontantIndemniteAssistance() {
        return montantIndemniteAssistance;
    }

    /**
     * getter pour l'attribut montant manque AGagner
     * 
     * @return la valeur courante de l'attribut montant manque AGagner
     */
    public String getMontantManqueAGagner() {
        return montantManqueAGagner;
    }

    /**
     * getter pour l'attribut montant revenu readaptation
     * 
     * @return la valeur courante de l'attribut montant revenu readaptation
     */
    public String getMontantRevenuReadaptation() {
        return montantRevenuReadaptation;
    }

    public String getMontantTotal() {
        return montantTotal;
    }

    /**
     * getter pour l'attribut no AVS
     * 
     * @return la valeur courante de l'attribut no AVS
     */
    public String getNoAVS() {
        return noAVS;
    }

    /**
     * getter pour l'attribut nom
     * 
     * @return la valeur courante de l'attribut nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * getter pour l'attribut nombre enfants ACharge
     * 
     * @return la valeur courante de l'attribut nombre enfants ACharge
     */
    public String getNombreEnfantsACharge() {
        return nombreEnfantsACharge;
    }

    /**
     * getter pour l'attribut prenom
     * 
     * @return la valeur courante de l'attribut prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * setter pour l'attribut alocation exploitation
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setAlocationExploitation(Boolean boolean1) {
        alocationExploitation = boolean1;
    }

    /**
     * setter pour l'attribut annee niveau manque AGagner
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setAnneeNiveauManqueAGagner(String string) {
        anneeNiveauManqueAGagner = string;
    }

    /**
     * setter pour l'attribut annee niveau readaptation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setAnneeNiveauReadaptation(String string) {
        anneeNiveauReadaptation = string;
    }

    /**
     * setter pour l'attribut cs etat civil
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsEtatCivil(String string) {
        csEtatCivil = string;
    }

    public void setCsGenreReadaptation(String csGenreReadaptation) {
        this.csGenreReadaptation = csGenreReadaptation;
    }

    /**
     * setter pour l'attribut cs mode calcul
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsModeCalcul(String string) {
        csModeCalcul = string;
    }

    /**
     * setter pour l'attribut periodicite manque AGagner
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsPeriodiciteManqueAGagner(String string) {
        csPeriodiciteManqueAGagner = string;
    }

    /**
     * setter pour l'attribut cs periodicite revenu readaptation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsPeriodiciteRevenuReadaptation(String string) {
        csPeriodiciteRevenuReadaptation = string;
    }

    /**
     * setter pour l'attribut employeurs
     * 
     * @param vector
     *            une nouvelle valeur pour cet attribut
     */
    public void setEmployeurs(JAVector vector) {
        employeurs = vector;
    }

    /**
     * setter pour l'attribut heures semaine manque AGagner
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setHeuresSemaineManqueAGagner(String string) {
        heuresSemaineManqueAGagner = string;
    }

    /**
     * setter pour l'attribut heures semaines readaptation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setHeuresSemainesReadaptation(String string) {
        heuresSemainesReadaptation = string;
    }

    /**
     * setter pour l'attribut id revenu petite IJ
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdRevenuPetiteIJ(String string) {
        idRevenuPetiteIJ = string;
    }

    /**
     * setter pour l'attribut incapacite3eme revision
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIncapacite3emeRevision(String string) {
        incapacite3emeRevision = string;
    }

    public void setMontantAit(String montantAit) {
        this.montantAit = montantAit;
    }

    /**
     * setter pour l'attribut montant indemnite assistance
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantIndemniteAssistance(String string) {
        montantIndemniteAssistance = string;
    }

    /**
     * setter pour l'attribut montant manque AGagner
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantManqueAGagner(String string) {
        montantManqueAGagner = string;
    }

    /**
     * setter pour l'attribut montant revenu readaptation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantRevenuReadaptation(String string) {
        montantRevenuReadaptation = string;
    }

    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }

    /**
     * setter pour l'attribut no AVS
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoAVS(String string) {
        noAVS = string;
    }

    /**
     * setter pour l'attribut nom
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNom(String string) {
        nom = string;
    }

    /**
     * setter pour l'attribut nombre enfants ACharge
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNombreEnfantsACharge(String string) {
        nombreEnfantsACharge = string;
    }

    /**
     * setter pour l'attribut prenom
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setPrenom(String string) {
        prenom = string;
    }
}
