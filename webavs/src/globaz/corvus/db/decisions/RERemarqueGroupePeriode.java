package globaz.corvus.db.decisions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

public class RERemarqueGroupePeriode extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_DATE_AU = "ZNDAU";

    public static final String FIELDNAME_DATE_DEPUIS = "ZNDDU";
    public static final String FIELDNAME_ID_DECISION = "ZNIIDE";
    public static final String FIELDNAME_ID_REM_GROUPE_PERIODE = "ZNIIRE";
    public static final String FIELDNAME_REMARQUE = "ZNLREM";
    public static final String TABLE_NAME_REMARQUE_GROUPE_PERIODE = "REREMGP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateAu = "";
    private String dateDepuis = "";
    private String idDecision = "";
    private String idRemarqueGroupePeriode = "";
    private String remarque = "";

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
        setIdRemarqueGroupePeriode(_incCounter(transaction, "0"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_REMARQUE_GROUPE_PERIODE;
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

        idRemarqueGroupePeriode = statement.dbReadNumeric(FIELDNAME_ID_REM_GROUPE_PERIODE);
        idDecision = statement.dbReadNumeric(FIELDNAME_ID_DECISION);
        dateDepuis = statement.dbReadNumeric(FIELDNAME_DATE_DEPUIS);
        dateAu = statement.dbReadNumeric(FIELDNAME_DATE_AU);
        remarque = statement.dbReadString(FIELDNAME_REMARQUE);

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
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
        statement.writeKey(FIELDNAME_ID_REM_GROUPE_PERIODE,
                _dbWriteNumeric(statement.getTransaction(), idRemarqueGroupePeriode, "idRemarqueGroupePeriode"));
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

        statement.writeField(FIELDNAME_ID_REM_GROUPE_PERIODE,
                _dbWriteNumeric(statement.getTransaction(), idRemarqueGroupePeriode, "idRemarqueGroupePeriode"));
        statement.writeField(FIELDNAME_ID_DECISION,
                _dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));
        statement.writeField(FIELDNAME_DATE_DEPUIS,
                _dbWriteNumeric(statement.getTransaction(), dateDepuis, "dateDepuis"));
        statement.writeField(FIELDNAME_DATE_AU, _dbWriteNumeric(statement.getTransaction(), dateAu, "dateAu"));
        statement.writeField(FIELDNAME_REMARQUE, _dbWriteString(statement.getTransaction(), remarque, "remarque"));

    }

    public String getDateAu() {
        return dateAu;
    }

    public String getDateDepuis() {
        return dateDepuis;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdRemarqueGroupePeriode() {
        return idRemarqueGroupePeriode;
    }

    public String getRemarque() {
        return remarque;
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

    public void setDateAu(String dateAu) {
        this.dateAu = dateAu;
    }

    public void setDateDepuis(String dateDepuis) {
        this.dateDepuis = dateDepuis;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdRemarqueGroupePeriode(String idRemarqueGroupePeriode) {
        this.idRemarqueGroupePeriode = idRemarqueGroupePeriode;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

}
