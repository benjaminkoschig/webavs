/*
 * Créé le 8 août 07
 */
package globaz.corvus.db.taux;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author BSC
 * 
 */
public class RETaux extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_DEBUT = "ZJDDEB";
    public static final String FIELDNAME_DATE_FIN = "ZJDFIN";
    public static final String FIELDNAME_ID_TAUX = "ZJITAU";
    public static final String FIELDNAME_TAUX = "ZJMTAU";
    public static final String FIELDNAME_TYPE_TAUX = "ZJTTYP";
    public static final String TABLE_NAME_TAUX = "RETAINT";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csTypeTaux = "";
    private String dateDebut = "";
    private String dateFin = "";
    private String idTaux = "";
    private String taux = "";

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
        setIdTaux(_incCounter(transaction, "0"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_TAUX;
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

        idTaux = statement.dbReadNumeric(FIELDNAME_ID_TAUX);
        csTypeTaux = statement.dbReadNumeric(FIELDNAME_TYPE_TAUX);
        dateDebut = statement.dbReadDateAMJ(FIELDNAME_DATE_DEBUT);
        dateFin = statement.dbReadDateAMJ(FIELDNAME_DATE_FIN);
        taux = statement.dbReadNumeric(FIELDNAME_TAUX);
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
        statement.writeKey(FIELDNAME_ID_TAUX, _dbWriteNumeric(statement.getTransaction(), idTaux, "idTaux"));
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

        statement.writeField(FIELDNAME_ID_TAUX, _dbWriteNumeric(statement.getTransaction(), idTaux, "idTaux"));
        statement
                .writeField(FIELDNAME_TYPE_TAUX, _dbWriteNumeric(statement.getTransaction(), csTypeTaux, "csTypeTaux"));
        statement.writeField(FIELDNAME_DATE_DEBUT, _dbWriteDateAMJ(statement.getTransaction(), dateDebut, "dateDebut"));
        statement.writeField(FIELDNAME_DATE_FIN, _dbWriteDateAMJ(statement.getTransaction(), dateFin, "dateFin"));
        statement.writeField(FIELDNAME_TAUX, _dbWriteNumeric(statement.getTransaction(), taux, "taux"));
    }

    /**
     * @return
     */
    public String getCsTypeTaux() {
        return csTypeTaux;
    }

    /**
     * @return
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return
     */
    public String getIdTaux() {
        return idTaux;
    }

    /**
     * @return
     */
    public String getTaux() {
        return taux;
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
     * @param string
     */
    public void setCsTypeTaux(String string) {
        csTypeTaux = string;
    }

    /**
     * @param string
     */
    public void setDateDebut(String string) {
        dateDebut = string;
    }

    /**
     * @param string
     */
    public void setDateFin(String string) {
        dateFin = string;
    }

    /**
     * @param string
     */
    public void setIdTaux(String string) {
        idTaux = string;
    }

    /**
     * @param string
     */
    public void setTaux(String string) {
        taux = string;
    }

}
