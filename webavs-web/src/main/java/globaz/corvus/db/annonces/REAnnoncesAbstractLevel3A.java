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
public class REAnnoncesAbstractLevel3A extends REAnnoncesAbstractLevel2A {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_CODE_REVENU_SPLITTE = "YZTCOD";

    public static final String FIELDNAME_DATE_DEBUT_ANTICIPATION = "YZDDEB";
    public static final String FIELDNAME_ID_ANNONCE_ABS_LEV_3A = "YZIANN";
    public static final String FIELDNAME_IS_SURVIVANT = "YZBSUR";
    public static final String FIELDNAME_NOMBRE_ANNEE_ANTICIPATION = "YZNANT";
    public static final String FIELDNAME_NOMBRE_ANNEE_BONIF_TRANSITOIRE = "YZNBON";
    public static final String FIELDNAME_NOMBRE_ANNEE_BTA = "YZNBTA";
    public static final String FIELDNAME_REDUCTION_ANTICIPATION = "YZNRED";
    public static final String TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_3A = "REAAL3A";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String codeRevenuSplitte = "";
    private String dateDebutAnticipation = "";
    private String isSurvivant = "";
    private String nbreAnneeAnticipation = "";
    private String nbreAnneeBonifTrans = "";
    private String nbreAnneeBTA = "";
    private String reductionAnticipation = "";

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
        getFrom += TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_3A;
        getFrom += " ON ";
        getFrom += REAnnonceHeader.FIELDNAME_ID_ANNONCE;
        getFrom += "=";
        getFrom += FIELDNAME_ID_ANNONCE_ABS_LEV_3A;

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
        return TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_3A;
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

        codeRevenuSplitte = statement.dbReadString(FIELDNAME_CODE_REVENU_SPLITTE);
        nbreAnneeBTA = statement.dbReadString(FIELDNAME_NOMBRE_ANNEE_BTA);
        nbreAnneeBonifTrans = statement.dbReadString(FIELDNAME_NOMBRE_ANNEE_BONIF_TRANSITOIRE);
        nbreAnneeAnticipation = statement.dbReadString(FIELDNAME_NOMBRE_ANNEE_ANTICIPATION);
        reductionAnticipation = statement.dbReadString(FIELDNAME_REDUCTION_ANTICIPATION);
        dateDebutAnticipation = statement.dbReadString(FIELDNAME_DATE_DEBUT_ANTICIPATION);
        isSurvivant = statement.dbReadString(FIELDNAME_IS_SURVIVANT);
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
        statement.writeKey(FIELDNAME_ID_ANNONCE_ABS_LEV_3A,
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
            statement.writeField(FIELDNAME_ID_ANNONCE_ABS_LEV_3A,
                    _dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
        }

        statement.writeField(FIELDNAME_CODE_REVENU_SPLITTE,
                _dbWriteString(statement.getTransaction(), codeRevenuSplitte, "codeRevenuSplitte"));
        statement.writeField(FIELDNAME_NOMBRE_ANNEE_BTA,
                _dbWriteString(statement.getTransaction(), nbreAnneeBTA, "nbreAnneeBTA"));
        statement.writeField(FIELDNAME_NOMBRE_ANNEE_BONIF_TRANSITOIRE,
                _dbWriteString(statement.getTransaction(), nbreAnneeBonifTrans, "nbreAnneeBonifTrans"));
        statement.writeField(FIELDNAME_NOMBRE_ANNEE_ANTICIPATION,
                _dbWriteString(statement.getTransaction(), nbreAnneeAnticipation, "nbreAnneeAnticipation"));
        statement.writeField(FIELDNAME_REDUCTION_ANTICIPATION,
                _dbWriteString(statement.getTransaction(), reductionAnticipation, "reductionAnticipation"));
        statement.writeField(FIELDNAME_DATE_DEBUT_ANTICIPATION,
                _dbWriteString(statement.getTransaction(), dateDebutAnticipation, "dateDebutAnticipation"));
        statement.writeField(FIELDNAME_IS_SURVIVANT,
                _dbWriteString(statement.getTransaction(), isSurvivant, "isSurvivant"));
    }

    /**
     * @return
     */
    public String getCodeRevenuSplitte() {
        return codeRevenuSplitte;
    }

    /**
     * @return
     */
    public String getDateDebutAnticipation() {
        return dateDebutAnticipation;
    }

    /**
     * @return
     */
    public String getIsSurvivant() {
        return isSurvivant;
    }

    /**
     * @return
     */
    public String getNbreAnneeAnticipation() {
        return nbreAnneeAnticipation;
    }

    /**
     * @return
     */
    public String getNbreAnneeBonifTrans() {
        return nbreAnneeBonifTrans;
    }

    /**
     * @return
     */
    public String getNbreAnneeBTA() {
        return nbreAnneeBTA;
    }

    /**
     * @return
     */
    public String getReductionAnticipation() {
        return reductionAnticipation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#hasCreationSpy()
     */
    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return false;
    }

    /**
     * @param string
     */
    public void setCodeRevenuSplitte(String string) {
        codeRevenuSplitte = string;
    }

    /**
     * @param string
     */
    public void setDateDebutAnticipation(String string) {
        dateDebutAnticipation = string;
    }

    /**
     * @param string
     */
    public void setIsSurvivant(String string) {
        isSurvivant = string;
    }

    /**
     * @param string
     */
    public void setNbreAnneeAnticipation(String string) {
        nbreAnneeAnticipation = string;
    }

    /**
     * @param string
     */
    public void setNbreAnneeBonifTrans(String string) {
        nbreAnneeBonifTrans = string;
    }

    /**
     * @param string
     */
    public void setNbreAnneeBTA(String string) {
        nbreAnneeBTA = string;
    }

    /**
     * @param string
     */
    public void setReductionAnticipation(String string) {
        reductionAnticipation = string;
    }

}
