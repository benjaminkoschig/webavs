package globaz.libra.db.domaines;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * 
 * @author HPE
 * 
 */
public class LIDomaines extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CS_DOMAINE = "ADTDOM";
    public static final String FIELDNAME_ID_DOMAINE = "ADIDOM";
    public static final String FIELDNAME_NOM_APPLICATION = "ADLNOA";

    public static final String TABLE_NAME = "LIDOMAI";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csDomaine = new String();
    private String idDomaine = new String();
    private String nomApplication = new String();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LIDomaines.
     */
    public LIDomaines() {
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
        setIdDomaine(_incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table des domaines
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des domaines
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idDomaine = statement.dbReadNumeric(FIELDNAME_ID_DOMAINE);
        nomApplication = statement.dbReadString(FIELDNAME_NOM_APPLICATION);
        csDomaine = statement.dbReadNumeric(FIELDNAME_CS_DOMAINE);

    }

    /**
     * Méthode de validation des domaines
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    /**
     * Définition de la clé primaire de la table des domaines
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {

        statement.writeKey(FIELDNAME_ID_DOMAINE, _dbWriteNumeric(statement.getTransaction(), idDomaine, "idDomaine"));

    }

    /**
     * Méthode d'écriture des champs dans la table des domaines
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(FIELDNAME_ID_DOMAINE, _dbWriteNumeric(statement.getTransaction(), idDomaine, "idDomaine"));
        statement.writeField(FIELDNAME_NOM_APPLICATION,
                _dbWriteString(statement.getTransaction(), nomApplication, "nomApplication"));
        statement.writeField(FIELDNAME_CS_DOMAINE, _dbWriteNumeric(statement.getTransaction(), csDomaine, "csDomaine"));
    }

    public String getCsDomaine() {
        return csDomaine;
    }

    public String getIdDomaine() {
        return idDomaine;
    }

    public String getNomApplication() {
        return nomApplication;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setCsDomaine(String csDomaine) {
        this.csDomaine = csDomaine;
    }

    public void setIdDomaine(String idDomaine) {
        this.idDomaine = idDomaine;
    }

    public void setNomApplication(String nomApplication) {
        this.nomApplication = nomApplication;
    }

}
