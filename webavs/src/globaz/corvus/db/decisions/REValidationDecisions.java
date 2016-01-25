/*
 * Créé le 25 juil. 07
 */
package globaz.corvus.db.decisions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author SCR
 * 
 */
public class REValidationDecisions extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int ALTERNATE_KEY_ID_PRST_DUE = 1;
    public static final String FIELDNAME_ID_DECISION = "YVIDEC";
    public static final String FIELDNAME_ID_PRESTATION_DUE = "YVIPRD";
    public static final String FIELDNAME_ID_VALIDATION_DECISION = "YVIVAD";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    public static final String TABLE_NAME_VALIDATION_DECISION = "REVALDEC";
    private String idDecision = "";
    private String idPrestationDue = "";

    private String idValidationDecision = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdValidationDecision(_incCounter(transaction, "0"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_VALIDATION_DECISION;
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

        idValidationDecision = statement.dbReadNumeric(FIELDNAME_ID_VALIDATION_DECISION);
        idPrestationDue = statement.dbReadNumeric(FIELDNAME_ID_PRESTATION_DUE);
        idDecision = statement.dbReadNumeric(FIELDNAME_ID_DECISION);

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        if (alternateKey == ALTERNATE_KEY_ID_PRST_DUE) {
            statement.writeKey(FIELDNAME_ID_PRESTATION_DUE,
                    _dbWriteNumeric(statement.getTransaction(), getIdPrestationDue(), "idPRstDue"));
        } else {
            super._writeAlternateKey(statement, alternateKey);
        }

    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_VALIDATION_DECISION,
                _dbWriteNumeric(statement.getTransaction(), idValidationDecision, "idValidationDecision"));
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

        statement.writeField(FIELDNAME_ID_VALIDATION_DECISION,
                _dbWriteNumeric(statement.getTransaction(), idValidationDecision, "idValidationDecision"));
        statement.writeField(FIELDNAME_ID_PRESTATION_DUE,
                _dbWriteNumeric(statement.getTransaction(), idPrestationDue, "idPrestationDue"));
        statement.writeField(FIELDNAME_ID_DECISION,
                _dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));

    }

    /**
     * @return the idDecision
     */
    public String getIdDecision() {
        return idDecision;
    }

    /**
     * @return the idPrestationDue
     */
    public String getIdPrestationDue() {
        return idPrestationDue;
    }

    /**
     * @return the idValidationDecision
     */
    public String getIdValidationDecision() {
        return idValidationDecision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#hasCreationSpy()
     */
    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    /**
     * @param idDecision
     *            the idDecision to set
     */
    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    /**
     * @param idPrestationDue
     *            the idPrestationDue to set
     */
    public void setIdPrestationDue(String idPrestationDue) {
        this.idPrestationDue = idPrestationDue;
    }

    /**
     * @param idValidationDecision
     *            the idValidationDecision to set
     */
    public void setIdValidationDecision(String idValidationDecision) {
        this.idValidationDecision = idValidationDecision;
    }

}
