/*
 * Créé le 2 mars 06
 */
package globaz.ij.db.prononces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.basesindemnisation.IJFormulaireIndemnisation;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * @author hpe
 * 
 *         BEntity qui reprend tous les champs nécessaires pour l'établissement de la liste des formulaires dans l'état
 *         envoyé (mais non reçus en retour)
 */
public class IJListeFormulairesNonRecus extends BEntity implements Comparable<IJListeFormulairesNonRecus> {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** DOCUMENT ME! */
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    /** DOCUMENT ME! */
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    /** DOCUMENT ME! */
    public static final String TABLE_AVS = "TIPAVSP";
    /** DOCUMENT ME! */
    public static final String TABLE_TIERS = "TITIERP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Sélection des champs à reprendre dans la BdD
     * 
     * @param schema
     *            --> _getCollection()
     * 
     * @return tous les champs nécessaires
     */
    public static final String createFields(String schema) {
        return IJPrononce.FIELDNAME_ID_PRONONCE + " , " + PRDemande.FIELDNAME_IDTIERS + " , "
                + IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION + " , "
                + IJFormulaireIndemnisation.FIELDNAME_DATEENVOI + " , "
                + IJFormulaireIndemnisation.FIELDNAME_NOMBRERAPPEL + " , " + IJAgentExecution.FIELDNAME_ID_TIERS
                + " , " + FIELDNAME_NUM_AVS;
    }

    /**
     * Création de la clause From
     * 
     * @param schema
     *            --> _getCollection()
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String leftJoin = " LEFT JOIN ";
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);

        // jointure entre tables demande et prononce
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononce.TABLE_NAME_PRONONCE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDDEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononce.TABLE_NAME_PRONONCE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononce.FIELDNAME_ID_DEMANDE);

        // jointure entre tables prononce et baseIndemnisation
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJBaseIndemnisation.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononce.TABLE_NAME_PRONONCE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononce.FIELDNAME_ID_PRONONCE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJBaseIndemnisation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJBaseIndemnisation.FIELDNAME_IDPRONONCE);

        // jointure entre tables baseIndemnisation et formulaires
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJFormulaireIndemnisation.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJBaseIndemnisation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJFormulaireIndemnisation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJFormulaireIndemnisation.FIELDNAME_IDINDEMNISATION);

        // jointure entre tables formulaires et agent d'exécution
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJAgentExecution.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJFormulaireIndemnisation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJFormulaireIndemnisation.FIELDNAME_IDINSTITUTIONRESPONSABLE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJAgentExecution.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJAgentExecution.FIELDNAME_ID_AGENT_EXECUTION);

        // jointure entre table des demandes et table des tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre table des demandes et table des numeros AVS
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        return fromClauseBuffer.toString();

    }

    private String dateEnvoiFormulaire = "";
    private String fields = null;
    private String fromClause = null;
    private String idBaseIndemnisation = "";
    private String idPrononce = "";
    private String idTiers = "";
    private String idTiersAgentExecution = "";
    private String nbreRappelFormulaire = "";
    private String noAVSTiers = "";
    private String nomAgentExecution = "";
    private String communePolitique = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getCommunePolitique() {
        return communePolitique;
    }

    public void setCommunePolitique(String communePolitique) {
        this.communePolitique = communePolitique;
    }

    private String nomTiers = "";

    private String prenomTiers = "";

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), idTiers);
        noAVSTiers = tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        nomTiers = tiers.getProperty(PRTiersWrapper.PROPERTY_NOM);
        prenomTiers = tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM);

        PRTiersWrapper tiersAgent = PRTiersHelper.getTiersParId(getSession(), idTiersAgentExecution);

        if (tiersAgent == null) {
            tiersAgent = PRTiersHelper.getAdministrationParId(getISession(), idTiersAgentExecution);
        }
        if (tiersAgent == null) {
            nomAgentExecution = "Not Found for : idTiersAG = " + idTiersAgentExecution;
        } else {
            nomAgentExecution = tiersAgent.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + tiersAgent.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
        }

        super._afterRetrieve(transaction);
    }

    @Override
    protected String _getFrom(BStatement statement) {
        if (fields == null) {
            fields = createFields(_getCollection());
        }
        return fromClause;
    }

    @Override
    protected String _getTableName() {
        return PRDemande.TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idPrononce = statement.dbReadNumeric(IJPrononce.FIELDNAME_ID_PRONONCE);
        idTiers = statement.dbReadNumeric(PRDemande.FIELDNAME_IDTIERS);
        idBaseIndemnisation = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION);
        dateEnvoiFormulaire = statement.dbReadDateAMJ(IJFormulaireIndemnisation.FIELDNAME_DATEENVOI);
        nbreRappelFormulaire = statement.dbReadNumeric(IJFormulaireIndemnisation.FIELDNAME_NOMBRERAPPEL);
        idTiersAgentExecution = statement.dbReadNumeric(IJAgentExecution.FIELDNAME_ID_TIERS);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /**
     * @return la date d'envoi du formulaire
     */
    public String getDateEnvoiFormulaire() {
        return dateEnvoiFormulaire;
    }

    /**
     * @return les champs nécessaire si fields = null
     */
    public String getFields() {
        if (fields == null) {
            fields = createFields(_getCollection());
        }
        return fields;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getIdBaseIndemnisation() {
        return idBaseIndemnisation;
    }

    public String getIdPrononce() {
        return idPrononce;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersAgentExecution() {
        return idTiersAgentExecution;
    }

    public String getNbreRappelFormulaire() {
        return nbreRappelFormulaire;
    }

    public String getNoAVSTiers() {
        return noAVSTiers;
    }

    public String getNomAgentExecution() {
        return nomAgentExecution;
    }

    public String getNomTiers() {
        return nomTiers;
    }

    public String getPrenomTiers() {
        return prenomTiers;
    }

    public void setDateEnvoiFormulaire(String string) {
        dateEnvoiFormulaire = string;
    }

    public void setFields(String string) {
        fields = string;
    }

    public void setFromClause(String string) {
        fromClause = string;
    }

    public void setIdBaseIndemnisation(String string) {
        idBaseIndemnisation = string;
    }

    public void setIdPrononce(String string) {
        idPrononce = string;
    }

    public void setIdTiers(String string) {
        idTiers = string;
    }

    public void setIdTiersAgentExecution(String string) {
        idTiersAgentExecution = string;
    }

    public void setNbreRappelFormulaire(String string) {
        nbreRappelFormulaire = string;
    }

    public void setNoAVSTiers(String string) {
        noAVSTiers = string;
    }

    public void setNomAgentExecution(String string) {
        nomAgentExecution = string;
    }

    public void setNomTiers(String string) {
        nomTiers = string;
    }

    public void setPrenomTiers(String string) {
        prenomTiers = string;
    }

    @Override
    public int compareTo(IJListeFormulairesNonRecus o) {
        return getCommunePolitique().compareToIgnoreCase(o.getCommunePolitique());
    }

}
