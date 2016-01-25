/*
 * Créé le 17 décembre 2009
 */
package globaz.cygnus.db.typeDeSoins;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author jje
 */
public class RFTypeDeSoin extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CODE = "EFNCOD";
    public static final String FIELDNAME_ID_TYPE_SOIN = "EFITSO";

    public static final String TABLE_NAME = "RFTSOIN";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String code = "";
    private String idTypeSoin = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFTypeDeSoin.
     */
    public RFTypeDeSoin() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Méthode avant l'ajout, incrémentant la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdTypeSoin(_incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table des types de soin
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des types de soin
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idTypeSoin = statement.dbReadNumeric(FIELDNAME_ID_TYPE_SOIN);
        code = statement.dbReadString(FIELDNAME_CODE);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des types de soin
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_TYPE_SOIN,
                _dbWriteNumeric(statement.getTransaction(), idTypeSoin, "idTypeSoin"));
    }

    /**
     * Méthode d'écriture des champs dans la table des types de soin
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ID_TYPE_SOIN,
                _dbWriteNumeric(statement.getTransaction(), idTypeSoin, "idTypeSoin"));
        statement.writeField(FIELDNAME_CODE, _dbWriteString(statement.getTransaction(), code, "code"));

    }

    public String getCode() {
        return code;
    }

    public String getIdTypeSoin() {
        return idTypeSoin;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setIdTypeSoin(String idTypeSoin) {
        this.idTypeSoin = idTypeSoin;
    }

}
