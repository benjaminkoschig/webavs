/*
 * Créé le 07 nov. 07
 */
package globaz.corvus.db.recap;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.tools.PRDateFormater;

/**
 * <H1>Description</H1>
 * 
 * @deprecated utiliser RERecapInfo
 * @author scr
 */
@Deprecated
public class REInfoRecapOld extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CODE_RECAP = "ZQNCOD";
    public static final String FIELDNAME_DATE_PMT = "ZQDDAT";
    public static final String FIELDNAME_ID_INFO_RECAP = "ZQIIFR";
    public static final String FIELDNAME_ID_TIERS = "ZQITIE";
    public static final String FIELDNAME_MONTANT = "ZQMMON";
    public static final String TABLE_NAME_INFO_RECAP = "REINFREC";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String codeRecap = "";

    // Format : mm.aaaa
    private String datePmt = "";
    private String idInfoRecap = "";
    private String idTiers = "";
    private String montant = "";

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
        setIdInfoRecap(_incCounter(transaction, "0"));
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
        return TABLE_NAME_INFO_RECAP;
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

        idInfoRecap = statement.dbReadNumeric(FIELDNAME_ID_INFO_RECAP);
        idTiers = statement.dbReadNumeric(FIELDNAME_ID_TIERS);
        datePmt = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement.dbReadNumeric(FIELDNAME_DATE_PMT));
        montant = statement.dbReadNumeric(FIELDNAME_MONTANT);
        codeRecap = statement.dbReadNumeric(FIELDNAME_CODE_RECAP);
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

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_INFO_RECAP,
                _dbWriteNumeric(statement.getTransaction(), idInfoRecap, "idInfoRecap"));
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
        statement.writeField(FIELDNAME_ID_INFO_RECAP,
                _dbWriteNumeric(statement.getTransaction(), idInfoRecap, "idInfoRecap"));
        statement.writeField(FIELDNAME_ID_TIERS, _dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(FIELDNAME_DATE_PMT, _dbWriteNumeric(statement.getTransaction(), datePmt, "datePmt"));
        statement.writeField(FIELDNAME_CODE_RECAP, _dbWriteNumeric(statement.getTransaction(), codeRecap, "codeRecap"));
        statement.writeField(FIELDNAME_MONTANT, _dbWriteNumeric(statement.getTransaction(), montant, "montant"));

    }

    public String getCodeRecap() {
        return codeRecap;
    }

    public String getDatePmt() {
        return datePmt;
    }

    public String getIdInfoRecap() {
        return idInfoRecap;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMontant() {
        return montant;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    public void setCodeRecap(String codeRecap) {
        this.codeRecap = codeRecap;
    }

    public void setDatePmt(String datePmt) {
        this.datePmt = datePmt;
    }

    public void setIdInfoRecap(String idInfoRecap) {
        this.idInfoRecap = idInfoRecap;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

}
