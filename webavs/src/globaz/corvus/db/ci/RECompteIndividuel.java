/*
 * Créé le 15 fevr. 07
 */
package globaz.corvus.db.ci;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 */
/**
 * @author SCR
 * 
 */
public class RECompteIndividuel extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int ALTERNATE_KEY_ID_TIERS = 1;
    public static final String FIELDNAME_ID_CI = "YNICI";
    public static final String FIELDNAME_ID_TIERS = "YNITIE";

    public static final String TABLE_NAME_CI = "RECI";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idCi = "";
    private String idTiers = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * initialise la valeur de Id période api
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdCi(_incCounter(transaction, "0"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_CI;
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
        idCi = statement.dbReadNumeric(FIELDNAME_ID_CI);
        idTiers = statement.dbReadNumeric(FIELDNAME_ID_TIERS);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        switch (alternateKey) {
            case ALTERNATE_KEY_ID_TIERS:
                statement.writeKey(FIELDNAME_ID_TIERS,
                        _dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers"));
                break;
            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
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
        statement.writeKey(FIELDNAME_ID_CI, _dbWriteNumeric(statement.getTransaction(), idCi, "idCi"));
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

        statement.writeField(FIELDNAME_ID_CI, _dbWriteNumeric(statement.getTransaction(), idCi, "idCi"));
        statement.writeField(FIELDNAME_ID_TIERS, _dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
    }

    /**
     * @return
     */
    public String getIdCi() {
        return idCi;
    }

    /**
     * @return
     */
    public String getIdTiers() {
        return idTiers;
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
     * @return
     */
    public void setIdCi(String idCi) {
        this.idCi = idCi;
    }

    /**
     * @return
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

}
