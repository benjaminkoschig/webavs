package globaz.corvus.db.annonces;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

public class REAnnoncesDiminution9Eme extends REAnnoncesAbstractLevel2B {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_CODE_TRAITEMENT = "ZWNCTR";

    public static final String FIELDNAME_DATE_LIQUIDATION = "ZWDLIQ";
    public static final String FIELDNAME_ID_ANNONCE_DIMINUTION_9_EME = "ZWIANN";
    public static final String FIELDNAME_MENSUALITE_RENTE_ORDINAIRE = "ZWMMRO";
    public static final String TABLE_NAME_ANNONCE_DIMINUTION_9_EME = "REANN42";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String codeTraitement = "";
    private String dateLiquidation = "";
    private String mensualiteRenteOrdinaire = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * initialise la valeur de Id annonce header
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
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        String getFrom = "";

        getFrom += super._getFrom(statement);

        getFrom += " INNER JOIN ";
        getFrom += _getCollection();
        getFrom += TABLE_NAME_ANNONCE_DIMINUTION_9_EME;
        getFrom += " ON ";
        getFrom += REAnnonceHeader.FIELDNAME_ID_ANNONCE;
        getFrom += "=";
        getFrom += FIELDNAME_ID_ANNONCE_DIMINUTION_9_EME;

        return getFrom;
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
        return TABLE_NAME_ANNONCE_DIMINUTION_9_EME;
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
        super._readProperties(statement);

        mensualiteRenteOrdinaire = statement.dbReadString(FIELDNAME_MENSUALITE_RENTE_ORDINAIRE);
        dateLiquidation = statement.dbReadDateAMJ(FIELDNAME_DATE_LIQUIDATION);
        codeTraitement = statement.dbReadNumeric(FIELDNAME_CODE_TRAITEMENT);

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
        statement.writeKey(FIELDNAME_ID_ANNONCE_DIMINUTION_9_EME,
                _dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
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

        if (_getAction() == ACTION_COPY) {
            super._writeProperties(statement);
        } else {
            statement.writeField(FIELDNAME_ID_ANNONCE_DIMINUTION_9_EME,
                    _dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
        }

        statement.writeField(FIELDNAME_MENSUALITE_RENTE_ORDINAIRE,
                _dbWriteString(statement.getTransaction(), mensualiteRenteOrdinaire, "mensualiteRenteOrdinaire"));
        statement.writeField(FIELDNAME_DATE_LIQUIDATION,
                _dbWriteDateAMJ(statement.getTransaction(), dateLiquidation, "dateLiquidation"));
        statement.writeField(FIELDNAME_CODE_TRAITEMENT,
                _dbWriteNumeric(statement.getTransaction(), codeTraitement, "codeTraitement"));
    }

    public String getCodeTraitement() {
        return codeTraitement;
    }

    public String getDateLiquidation() {
        return dateLiquidation;
    }

    public String getMensualiteRenteOrdinaire() {
        return mensualiteRenteOrdinaire;
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
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setCodeTraitement(String codeTraitement) {
        this.codeTraitement = codeTraitement;
    }

    public void setDateLiquidation(String dateLiquidation) {
        this.dateLiquidation = dateLiquidation;
    }

    public void setMensualiteRenteOrdinaire(String mensualiteRenteOrdinaire) {
        this.mensualiteRenteOrdinaire = mensualiteRenteOrdinaire;
    }

}
