package globaz.cygnus.db.documents;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * author fha
 */
public class RFDocuments extends BEntity {
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_DOCUMENT = "FDIDOC";
    public static final String FIELDNAME_TYPE_DOCUMENT = "FDITDO";

    public static final String TABLE_NAME = "RFDOCUM";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idDocument = "";
    private String typeDocument = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFTypeDeSoin.
     */
    public RFDocuments() {
        super();
    }

    /**
     * Méthode avant l'ajout, incrémentant la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdDocument(this._incCounter(transaction, "0"));
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour le nom de la table des types de soin
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFDocuments.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des types de soin
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDocument = statement.dbReadNumeric(RFDocuments.FIELDNAME_ID_DOCUMENT);
        typeDocument = statement.dbReadString(RFDocuments.FIELDNAME_TYPE_DOCUMENT);

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
        statement.writeKey(RFDocuments.FIELDNAME_ID_DOCUMENT,
                this._dbWriteNumeric(statement.getTransaction(), idDocument, "idDocument"));
    }

    /**
     * Méthode d'écriture des champs dans la table des types de soin
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RFDocuments.FIELDNAME_ID_DOCUMENT,
                this._dbWriteNumeric(statement.getTransaction(), idDocument, "idDocument"));
        statement.writeField(RFDocuments.FIELDNAME_TYPE_DOCUMENT,
                this._dbWriteNumeric(statement.getTransaction(), typeDocument, "typeDocument"));

    }

    public String getIdDocument() {
        return idDocument;
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

}
