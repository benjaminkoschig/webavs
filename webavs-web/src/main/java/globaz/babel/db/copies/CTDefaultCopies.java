/*
 * Créé le 2 nov. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.db.copies;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 * 
 * 
 */
public class CTDefaultCopies extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Le CS du type de document (standard/meta document) */
    public static final String FIELDNAME_CS_TYPE_DOCUMENT = "CGTTYD";

    /** Le CS du type d'intervenant recevant une copie par defaut */
    public static final String FIELDNAME_CS_TYPE_INTERVENANT = "CGTINT";
    /** L'id de la copie par default */
    public static final String FIELDNAME_DEFAULT_COPIE_ID = "CGIDEC";
    /** L'id du document */
    public static final String FIELDNAME_ID_DOCUMENT = "CGIDOC";
    /** Table des copies par default */
    public static final String TABLE_DEFAULT_COPIES = "CTDEFCOP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_DEFAULT_COPIES);

        return fromClauseBuffer.toString();
    }

    private String csTypeDocument = "";
    private String csTypeIntervenant = "";
    private transient String fromClause = null;
    private String idDefaultCopie = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String idDocument = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdDefaultCopie(_incCounter(transaction, "0"));
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = createFromClause(_getCollection());
        }

        return fromClause;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_DEFAULT_COPIES;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDefaultCopie = statement.dbReadNumeric(FIELDNAME_DEFAULT_COPIE_ID);
        idDocument = statement.dbReadNumeric(FIELDNAME_ID_DOCUMENT);
        csTypeDocument = statement.dbReadNumeric(FIELDNAME_CS_TYPE_DOCUMENT);
        csTypeIntervenant = statement.dbReadNumeric(FIELDNAME_CS_TYPE_INTERVENANT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // nope
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_DEFAULT_COPIE_ID,
                _dbWriteNumeric(statement.getTransaction(), idDefaultCopie, "idDefaultCopie"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_DEFAULT_COPIE_ID,
                _dbWriteNumeric(statement.getTransaction(), idDefaultCopie, "idDefaultCopie"));
        statement.writeField(FIELDNAME_ID_DOCUMENT,
                _dbWriteNumeric(statement.getTransaction(), idDocument, "idDocument"));
        statement.writeField(FIELDNAME_CS_TYPE_DOCUMENT,
                _dbWriteNumeric(statement.getTransaction(), csTypeDocument, "csTypeDocument"));
        statement.writeField(FIELDNAME_CS_TYPE_INTERVENANT,
                _dbWriteNumeric(statement.getTransaction(), csTypeIntervenant, "csTypeIntervenant"));
    }

    /**
     * @return
     */
    public String getCsTypeDocument() {
        return csTypeDocument;
    }

    /**
     * @return
     */
    public String getCsTypeIntervenant() {
        return csTypeIntervenant;
    }

    /**
     * @return
     */
    public String getFromClause() {
        return fromClause;
    }

    /**
     * @return
     */
    public String getIdDefaultCopie() {
        return idDefaultCopie;
    }

    /**
     * @return
     */
    public String getIdDocument() {
        return idDocument;
    }

    /**
     * @param string
     */
    public void setCsTypeDocument(String string) {
        csTypeDocument = string;
    }

    /**
     * @param string
     */
    public void setCsTypeIntervenant(String string) {
        csTypeIntervenant = string;
    }

    /**
     * @param string
     */
    public void setFromClause(String string) {
        fromClause = string;
    }

    /**
     * @param string
     */
    public void setIdDefaultCopie(String string) {
        idDefaultCopie = string;
    }

    /**
     * @param string
     */
    public void setIdDocument(String string) {
        idDocument = string;
    }

}