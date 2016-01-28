/*
 * Créé le 25 novembre 2009
 */
package globaz.cygnus.db.decisions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author fha
 */
public class RFAssDossierDecision extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_DECISION = "ECIDEC";
    public static final String FIELDNAME_ID_DECISION_DOSSIER = "ECIDOD";
    public static final String FIELDNAME_ID_DOSSIER = "ECIDOS";

    public static final String TABLE_NAME = "RFADODE";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idDecision = "";
    private String idDecisionDossier = "";
    private String idDossier = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDossiers.
     */
    public RFAssDossierDecision() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Méthode avant l'ajout l'incrémentation de la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdDecisionDossier(_incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table des dossiers
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDecisionDossier = statement.dbReadNumeric(FIELDNAME_ID_DECISION_DOSSIER);
        idDecision = statement.dbReadNumeric(FIELDNAME_ID_DECISION);
        idDossier = statement.dbReadNumeric(FIELDNAME_ID_DOSSIER);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_DECISION_DOSSIER,
                _dbWriteNumeric(statement.getTransaction(), idDecisionDossier, "idDecisionDossier"));

    }

    /**
     * Méthode d'écriture des champs dans la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ID_DECISION_DOSSIER,
                _dbWriteNumeric(statement.getTransaction(), idDecisionDossier, "idDecisionDossier"));
        statement.writeField(FIELDNAME_ID_DECISION,
                _dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));
        statement.writeField(FIELDNAME_ID_DOSSIER, _dbWriteNumeric(statement.getTransaction(), idDossier, "idDossier"));
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDecisionDossier() {
        return idDecisionDossier;
    }

    public String getIdDossier() {
        return idDossier;
    }

    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDecisionDossier(String idDecisionDossier) {
        this.idDecisionDossier = idDecisionDossier;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

}
