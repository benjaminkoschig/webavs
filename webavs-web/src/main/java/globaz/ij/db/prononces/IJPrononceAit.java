package globaz.ij.db.prononces;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.prestation.clone.factory.IPRCloneable;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class IJPrononceAit extends IJPrononce implements IPRCloneable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    public static final String FIELDNAME_ID_PRONONCE_AIT = "WPIAIT";

    /**
     */
    public static final String FIELDNAME_MONTANT = "WPMONT";

    /**
     */
    public static final String TABLE_NAME_PRONONCE_AIT = "IJAIT";

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
        StringBuffer fromClause = new StringBuffer();

        fromClause.append(schema);
        fromClause.append(TABLE_NAME_PRONONCE_AIT);

        // jointure avec la table des prononces
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(TABLE_NAME_PRONONCE);
        fromClause.append(" ON ");
        fromClause.append(FIELDNAME_ID_PRONONCE_AIT);
        fromClause.append("=");
        fromClause.append(FIELDNAME_ID_PRONONCE);

        return fromClause.toString();
    }

    private String idPrononceAit = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String montant = "";

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
        setCsTypeIJ(IIJPrononce.CS_ALLOC_INIT_TRAVAIL);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return createFromClause(_getCollection());
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_PRONONCE_AIT;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idPrononce = statement.dbReadNumeric(FIELDNAME_ID_PRONONCE_AIT);
        montant = statement.dbReadNumeric(FIELDNAME_MONTANT);
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_PRONONCE_AIT, _dbWriteNumeric(statement.getTransaction(), getIdPrononce()));
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        if (_getAction() == ACTION_COPY) {
            super._writeProperties(statement);
        } else {
            statement.writeField(FIELDNAME_ID_PRONONCE_AIT,
                    _dbWriteNumeric(statement.getTransaction(), getIdPrononce(), "idPrononce"));
        }

        statement.writeField(FIELDNAME_MONTANT, _dbWriteNumeric(statement.getTransaction(), montant, "montant"));
    }

    /**
     * DOCUMENT ME!
     * 
     * @param action
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public IPRCloneable duplicate(int action) throws Exception {
        IJPrononceAit clone = new IJPrononceAit();

        duplicatePrononce(clone, action);

        clone.setMontant(getMontant());

        return clone;
    }

    public String getIdPrononceAit() {
        return idPrononceAit;
    }

    public String getMontant() {
        return montant;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setIdPrononceAit(String idPrononceAit) {
        this.idPrononceAit = idPrononceAit;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }
}
