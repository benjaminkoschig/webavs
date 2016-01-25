package globaz.cygnus.db.documents;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * author fha
 */
public class RFTypeDocuments extends BEntity {
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_ID_TYPE_DOCUMENT = "FEITDO";

    public static final String TABLE_NAME = "RFTYDOC";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idTypeDocument = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFTypeDeSoin.
     */
    public RFTypeDocuments() {
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
        setIdTypeDocument(_incCounter(transaction, "0"));
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
        idTypeDocument = statement.dbReadString(FIELDNAME_ID_TYPE_DOCUMENT);

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
        statement.writeKey(FIELDNAME_ID_TYPE_DOCUMENT,
                _dbWriteNumeric(statement.getTransaction(), idTypeDocument, "idTypeDocument"));
    }

    /**
     * Méthode d'écriture des champs dans la table des types de soin
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ID_TYPE_DOCUMENT,
                _dbWriteNumeric(statement.getTransaction(), idTypeDocument, "idTypeDocument"));

    }

    public String getIdTypeDocument() {
        return idTypeDocument;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setIdTypeDocument(String idTypeDocument) {
        this.idTypeDocument = idTypeDocument;
    }

}
