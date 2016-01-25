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
public class REAnnoncesAugmentationModification10Eme extends REAnnoncesAbstractLevel3A {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int ALTERNATE_KEY_ID_LIEN_ANNONCE = 1;

    public static final String FIELDNAME_ID_ANNONCE_AUG_MOD_10 = "ZEIANN";
    public static final String FIELDNAME_NOUVEAU_NO_ASSURE_AYANT_DROIT = "ZENNNA";

    public static final String TABLE_NAME_ANNONCE_AUG_MOF_10 = "REANN44";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

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
        getFrom += TABLE_NAME_ANNONCE_AUG_MOF_10;
        getFrom += " ON ";
        getFrom += REAnnonceHeader.FIELDNAME_ID_ANNONCE;
        getFrom += "=";
        getFrom += FIELDNAME_ID_ANNONCE_AUG_MOD_10;

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
        return TABLE_NAME_ANNONCE_AUG_MOF_10;
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
        statement.writeKey(FIELDNAME_ID_ANNONCE_AUG_MOD_10,
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
            statement.writeField(FIELDNAME_ID_ANNONCE_AUG_MOD_10,
                    _dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
        }

        statement.writeField(FIELDNAME_NOUVEAU_NO_ASSURE_AYANT_DROIT,
                _dbWriteString(statement.getTransaction(), nouveauNoAssureAyantDroit, "nouveauNoAssureAyantDroit"));
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
    public void setNouveauNoAssureAyantDroit(String string) {
        nouveauNoAssureAyantDroit = string;
    }

}
