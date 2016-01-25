/*
 * Créé le 2 août 07
 */
package globaz.corvus.db.annonces;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author HPE
 * 
 */
public class REAnnoncesAugmentationModification9Eme extends REAnnoncesAbstractLevel3B {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int ALTERNATE_KEY_ID_LIEN_ANNONCE = 1;

    public static final String FIELDNAME_CODE_TRAITEMENT = "ZGTCOD";
    public static final String FIELDNAME_DATE_LIQUIDATION = "ZGDDAL";
    public static final String FIELDNAME_ID_ANNONCE_AUG_MOD_9 = "ZGIANN";
    public static final String FIELDNAME_MENSUALITE_RENTE_ORD_REMP = "ZGMMEN";
    public static final String FIELDNAME_NOUVEAU_NO_ASSURE_AYANT_DROIT = "ZGNNA";

    public static final String TABLE_NAME_ANNONCE_AUG_MOF_9 = "REANN41";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String codeTraitement = "";
    private String dateLiquidation = "";
    private String mensualiteRenteOrdRemp = "";
    private String nouveauNoAssureAyantDroit = "";

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
        getFrom += TABLE_NAME_ANNONCE_AUG_MOF_9;
        getFrom += " ON ";
        getFrom += REAnnonceHeader.FIELDNAME_ID_ANNONCE;
        getFrom += "=";
        getFrom += FIELDNAME_ID_ANNONCE_AUG_MOD_9;

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
        return TABLE_NAME_ANNONCE_AUG_MOF_9;
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
        nouveauNoAssureAyantDroit = statement.dbReadString(FIELDNAME_NOUVEAU_NO_ASSURE_AYANT_DROIT);
        mensualiteRenteOrdRemp = statement.dbReadString(FIELDNAME_MENSUALITE_RENTE_ORD_REMP);
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

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        switch (alternateKey) {
            case ALTERNATE_KEY_ID_LIEN_ANNONCE:
                statement.writeKey(FIELDNAME_ID_LIEN_ANNONCE,
                        _dbWriteNumeric(statement.getTransaction(), getIdLienAnnonce(), "idLienAnnonce"));
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
        statement.writeKey(FIELDNAME_ID_ANNONCE_AUG_MOD_9,
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
            statement.writeField(FIELDNAME_ID_ANNONCE_AUG_MOD_9,
                    _dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
        }

        statement.writeField(FIELDNAME_NOUVEAU_NO_ASSURE_AYANT_DROIT,
                _dbWriteString(statement.getTransaction(), nouveauNoAssureAyantDroit, "nouveauNoAssureAyantDroit"));
        statement.writeField(FIELDNAME_MENSUALITE_RENTE_ORD_REMP,
                _dbWriteString(statement.getTransaction(), mensualiteRenteOrdRemp, "mensualiteRenteOrdRemp"));
        statement.writeField(FIELDNAME_DATE_LIQUIDATION,
                _dbWriteDateAMJ(statement.getTransaction(), dateLiquidation, "dateLiquidation"));
        statement.writeField(FIELDNAME_CODE_TRAITEMENT,
                _dbWriteNumeric(statement.getTransaction(), codeTraitement, "codeTraitement"));
    }

    /**
     * @return
     */
    public String getCodeTraitement() {
        return codeTraitement;
    }

    /**
     * @return
     */
    public String getDateLiquidation() {
        return dateLiquidation;
    }

    /**
     * @return
     */
    public String getMensualiteRenteOrdRemp() {
        return mensualiteRenteOrdRemp;
    }

    /**
     * @return
     */
    public String getNouveauNoAssureAyantDroit() {
        return nouveauNoAssureAyantDroit;
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

    /**
     * @param string
     */
    public void setCodeTraitement(String string) {
        codeTraitement = string;
    }

    /**
     * @param string
     */
    public void setDateLiquidation(String string) {
        dateLiquidation = string;
    }

    /**
     * @param string
     */
    public void setMensualiteRenteOrdRemp(String string) {
        mensualiteRenteOrdRemp = string;
    }

    /**
     * @param string
     */
    public void setNouveauNoAssureAyantDroit(String string) {
        nouveauNoAssureAyantDroit = string;
    }

}
