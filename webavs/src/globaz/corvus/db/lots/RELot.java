/*
 * Créé le 25 juil. 07
 */
package globaz.corvus.db.lots;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author HPE
 * 
 */
public class RELot extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_CREATION = "YTDCRE";
    public static final String FIELDNAME_DATE_ENVOI = "YTDENV";
    public static final String FIELDNAME_DESCRIPTION = "YTLDES";
    public static final String FIELDNAME_ETAT = "YTTETA";
    public static final String FIELDNAME_ID_JOURNAL_CA = "YTIJCA";
    public static final String FIELDNAME_ID_LOT = "YTILOT";
    public static final String FIELDNAME_LOT_OWNER = "YTTOWN";

    // public static final String FIELDNAME_ID_RENTE_ACCORDEE = "YTIREA";
    public static final String FIELDNAME_TYPE_LOT = "YTTTYP";
    public static final String TABLE_NAME_LOT = "RELOTS";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csEtatLot = "";

    private String csLotOwner = "";
    // private String idRenteAccordee = "";
    private String csTypeLot = "";

    private String dateCreationLot = "";

    private String dateEnvoiLot = "";
    private String description = "";
    private String idJournalCA = "";
    private String idLot = "";

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
        setIdLot(this._incCounter(transaction, "0"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return RELot.TABLE_NAME_LOT;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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

        idLot = statement.dbReadNumeric(RELot.FIELDNAME_ID_LOT);
        csTypeLot = statement.dbReadNumeric(RELot.FIELDNAME_TYPE_LOT);
        dateCreationLot = statement.dbReadDateAMJ(RELot.FIELDNAME_DATE_CREATION);
        dateEnvoiLot = statement.dbReadDateAMJ(RELot.FIELDNAME_DATE_ENVOI);
        csEtatLot = statement.dbReadNumeric(RELot.FIELDNAME_ETAT);
        description = statement.dbReadString(RELot.FIELDNAME_DESCRIPTION);
        idJournalCA = statement.dbReadNumeric(RELot.FIELDNAME_ID_JOURNAL_CA);
        csLotOwner = statement.dbReadNumeric(RELot.FIELDNAME_LOT_OWNER);
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
        statement.writeKey(RELot.FIELDNAME_ID_LOT, this._dbWriteNumeric(statement.getTransaction(), idLot, "idLot"));
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

        statement.writeField(RELot.FIELDNAME_ID_LOT, this._dbWriteNumeric(statement.getTransaction(), idLot, "idLot"));
        statement.writeField(RELot.FIELDNAME_TYPE_LOT,
                this._dbWriteNumeric(statement.getTransaction(), csTypeLot, "csTypeLot"));
        statement.writeField(RELot.FIELDNAME_DATE_CREATION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateCreationLot, "dateCreation"));
        statement.writeField(RELot.FIELDNAME_DATE_ENVOI,
                this._dbWriteDateAMJ(statement.getTransaction(), dateEnvoiLot, "dateEnvoi"));
        statement.writeField(RELot.FIELDNAME_ETAT,
                this._dbWriteNumeric(statement.getTransaction(), csEtatLot, "csEtatLot"));
        statement.writeField(RELot.FIELDNAME_DESCRIPTION,
                this._dbWriteString(statement.getTransaction(), description, "description"));
        statement.writeField(RELot.FIELDNAME_ID_JOURNAL_CA,
                this._dbWriteNumeric(statement.getTransaction(), idJournalCA, "idJournalCA"));
        statement.writeField(RELot.FIELDNAME_LOT_OWNER,
                this._dbWriteNumeric(statement.getTransaction(), csLotOwner, "csLotOwner"));

    }

    /**
     * @return
     */
    public String getCsEtatLot() {
        return csEtatLot;
    }

    public String getCsLotOwner() {
        return csLotOwner;
    }

    /**
     * @return
     */
    public String getCsTypeLot() {
        return csTypeLot;
    }

    /**
     * @return the dateCreationLot
     */
    public String getDateCreationLot() {
        return dateCreationLot;
    }

    /**
     * @return the dateEnvoiLot
     */
    public String getDateEnvoiLot() {
        return dateEnvoiLot;
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

    // /**
    // * @return
    // */
    // public String getIdRenteAccordee() {
    // return idRenteAccordee;
    // }

    public String getIdJournalCA() {
        return idJournalCA;
    }

    /**
     * @return
     */
    public String getIdLot() {
        return idLot;
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

    // /**
    // * @param string
    // */
    // public void setIdRenteAccordee(String string) {
    // idRenteAccordee = string;
    // }

    /**
     * @param string
     */
    public void setCsEtatLot(String string) {
        csEtatLot = string;
    }

    public void setCsLotOwner(String csLotOwner) {
        this.csLotOwner = csLotOwner;
    }

    /**
     * @param string
     */
    public void setCsTypeLot(String string) {
        csTypeLot = string;
    }

    /**
     * @param dateCreationLot
     *            the dateCreationLot to set
     */
    public void setDateCreationLot(String dateCreationLot) {
        this.dateCreationLot = dateCreationLot;
    }

    /**
     * @param dateEnvoiLot
     *            the dateEnvoiLot to set
     */
    public void setDateEnvoiLot(String dateEnvoiLot) {
        this.dateEnvoiLot = dateEnvoiLot;
    }

    /**
     * @param string
     */
    public void setDescription(String string) {
        description = string;
    }

    public void setIdJournalCA(String idJournalCA) {
        this.idJournalCA = idJournalCA;
    }

    /**
     * @param string
     */
    public void setIdLot(String string) {
        idLot = string;
    }

}
