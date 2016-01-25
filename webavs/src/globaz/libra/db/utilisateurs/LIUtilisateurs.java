package globaz.libra.db.utilisateurs;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * 
 * @author HPE
 * 
 */
public class LIUtilisateurs extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_GROUPE = "AFIGRO";
    public static final String FIELDNAME_ID_UTILISATEUR = "AFIUTI";
    public static final String FIELDNAME_ID_UTILISATEUR_EXTERNE = "AFIUTE";
    public static final String FIELDNAME_IS_DEFAULT = "AFBDEF";

    public static final String TABLE_NAME = "LIUTILI";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idGroupe = new String();
    private String idUtilisateur = new String();
    private String idUtilisateurExterne = new String();
    private Boolean isDefault = Boolean.FALSE;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LIUtilisateurs.
     */
    public LIUtilisateurs() {
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
        setIdUtilisateur(_incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table des utilisateurs
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des utilisateurs
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idUtilisateur = statement.dbReadNumeric(FIELDNAME_ID_UTILISATEUR);
        idUtilisateurExterne = statement.dbReadString(FIELDNAME_ID_UTILISATEUR_EXTERNE);
        idGroupe = statement.dbReadNumeric(FIELDNAME_ID_GROUPE);
        isDefault = statement.dbReadBoolean(FIELDNAME_IS_DEFAULT);

    }

    /**
     * Méthode de validation des utilisateurs
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    /**
     * Définition de la clé primaire de la table des utilisateurs
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {

        statement.writeKey(FIELDNAME_ID_UTILISATEUR,
                _dbWriteNumeric(statement.getTransaction(), idUtilisateur, "idUtilisateur"));

    }

    /**
     * Méthode d'écriture des champs dans la table des utilisateurs
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(FIELDNAME_ID_UTILISATEUR,
                _dbWriteNumeric(statement.getTransaction(), idUtilisateur, "idUtilisateur"));
        statement.writeField(FIELDNAME_ID_UTILISATEUR_EXTERNE,
                _dbWriteString(statement.getTransaction(), idUtilisateurExterne, "idUtilisateurExterne"));
        statement.writeField(FIELDNAME_ID_GROUPE, _dbWriteNumeric(statement.getTransaction(), idGroupe, "idGroupe"));
        statement.writeField(FIELDNAME_IS_DEFAULT,
                _dbWriteBoolean(statement.getTransaction(), isDefault, BConstants.DB_TYPE_BOOLEAN_CHAR, "isDefault"));

    }

    public String getIdGroupe() {
        return idGroupe;
    }

    public String getIdUtilisateur() {
        return idUtilisateur;
    }

    public String getIdUtilisateurExterne() {
        return idUtilisateurExterne;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setIdGroupe(String idGroupe) {
        this.idGroupe = idGroupe;
    }

    public void setIdUtilisateur(String idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public void setIdUtilisateurExterne(String idUtilisateurExterne) {
        this.idUtilisateurExterne = idUtilisateurExterne;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

}
