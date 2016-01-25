/*
 * Créé le 24.07.2009
 */
package globaz.libra.db.dossiers;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * 
 * @author HPE
 * 
 */
public class LIDossiers extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int ALTERNATE_KEY_ID_EXTERNE = 1;
    public static final String FIELDNAME_CS_ETAT = "AATETA";
    public static final String FIELDNAME_ID_DOMAINE = "AAIDOM";
    public static final String FIELDNAME_ID_DOSSIER = "AAIDOS";
    public static final String FIELDNAME_ID_EXTERNE = "AAIEXT";
    public static final String FIELDNAME_ID_GESTIONNAIRE = "AAIGES";
    public static final String FIELDNAME_ID_GROUPE = "AAIGRO";
    public static final String FIELDNAME_ID_TIERS = "AAITIE";

    public static final String FIELDNAME_IS_URGENT = "AABURG";

    public static final String TABLE_NAME = "LIDOSSI";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csEtat = new String();
    private String idDomaine = new String();
    private String idDossier = new String();
    private String idExterne = new String();
    private String idGestionnaire = new String();
    private String idGroupe = new String();
    private String idTiers = new String();
    private Boolean isUrgent = Boolean.FALSE;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LIDossiers.
     */
    public LIDossiers() {
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
        setIdDossier(_incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table des échéances
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

        idDossier = statement.dbReadNumeric(FIELDNAME_ID_DOSSIER);
        idTiers = statement.dbReadNumeric(FIELDNAME_ID_TIERS);
        csEtat = statement.dbReadNumeric(FIELDNAME_CS_ETAT);
        idGestionnaire = statement.dbReadNumeric(FIELDNAME_ID_GESTIONNAIRE);
        idGroupe = statement.dbReadNumeric(FIELDNAME_ID_GROUPE);
        idDomaine = statement.dbReadNumeric(FIELDNAME_ID_DOMAINE);
        isUrgent = statement.dbReadBoolean(FIELDNAME_IS_URGENT);
        idExterne = statement.dbReadNumeric(FIELDNAME_ID_EXTERNE);

    }

    /**
     * Méthode de validation des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeAlternateKey(globaz.globall.db.BStatement , int)
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        if (alternateKey == ALTERNATE_KEY_ID_EXTERNE) {
            statement.writeKey(FIELDNAME_ID_EXTERNE,
                    _dbWriteNumeric(statement.getTransaction(), getIdExterne(), "idExterne"));
        } else {
            super._writeAlternateKey(statement, alternateKey);
        }
    }

    /**
     * Définition de la clé primaire de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {

        statement.writeKey(FIELDNAME_ID_DOSSIER, _dbWriteNumeric(statement.getTransaction(), idDossier, "idDossier"));

    }

    /**
     * Méthode d'écriture des champs dans la table des échéances
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(FIELDNAME_ID_DOSSIER, _dbWriteNumeric(statement.getTransaction(), idDossier, "idDossier"));
        statement.writeField(FIELDNAME_ID_TIERS, _dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(FIELDNAME_CS_ETAT, _dbWriteNumeric(statement.getTransaction(), csEtat, "csEtat"));
        statement.writeField(FIELDNAME_ID_GESTIONNAIRE,
                _dbWriteNumeric(statement.getTransaction(), idGestionnaire, "idGestionnaire"));
        statement.writeField(FIELDNAME_ID_GROUPE, _dbWriteNumeric(statement.getTransaction(), idGroupe, "idGroupe"));
        statement.writeField(FIELDNAME_ID_DOMAINE, _dbWriteNumeric(statement.getTransaction(), idDomaine, "idDomaine"));
        statement.writeField(FIELDNAME_IS_URGENT,
                _dbWriteBoolean(statement.getTransaction(), isUrgent, BConstants.DB_TYPE_BOOLEAN_CHAR, "isUrgent"));
        statement.writeField(FIELDNAME_ID_EXTERNE, _dbWriteNumeric(statement.getTransaction(), idExterne, "idExterne"));

    }

    public String getCsEtat() {
        return csEtat;
    }

    public String getIdDomaine() {
        return idDomaine;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdGroupe() {
        return idGroupe;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public Boolean getIsUrgent() {
        return isUrgent;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setIdDomaine(String idDomaine) {
        this.idDomaine = idDomaine;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdGroupe(String idGroupe) {
        this.idGroupe = idGroupe;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsUrgent(Boolean isUrgent) {
        this.isUrgent = isUrgent;
    }

}