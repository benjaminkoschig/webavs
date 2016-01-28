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
public class REAnnoncesAbstractLevel3B extends REAnnoncesAbstractLevel2A {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_AGE_DEBUT_INVALIDITE_EPOUSE = "ZCNDEE";

    public static final String FIELDNAME_BTE_MOYENNE_PRISE_EN_COMPTE = "ZCMBTE";
    public static final String FIELDNAME_CODE_INFIRMITE_EPOUSE = "ZCLCOE";
    public static final String FIELDNAME_DEGRE_INVALIDITE_EPOUSE = "ZCNINE";
    public static final String FIELDNAME_ID_ANNONCE_ABS_LEV_3B = "ZCIANN";
    public static final String FIELDNAME_IS_LIMITE_REVENU = "ZCBLIM";
    public static final String FIELDNAME_IS_MINIMUM_GARANTI = "ZCBMIN";
    public static final String FIELDNAME_MNT_RENTE_ORDINAIRE_REMPLACEE = "ZCMROR";
    public static final String FIELDNAME_OFFICE_AI_COMP_EPOUSE = "ZCLAIE";
    public static final String FIELDNAME_REVENU_ANNUEL_MOYEN_SANS_BTE = "ZCNRAM";
    public static final String FIELDNAME_REVENU_PRIS_EN_COMPTE = "ZCMREV";
    public static final String FIELDNAME_SURVENANCE_EVT_ASSURE_EPOUSE = "ZCDSUE";
    public static final String TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_3B = "REAAL3B";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String ageDebutInvaliditeEpouse = "";
    private String bteMoyennePrisEnCompte = "";
    private String codeInfirmiteEpouse = "";
    private String degreInvaliditeEpouse = "";
    private String isLimiteRevenu = "";
    private String isMinimumGaranti = "";
    private String mntRenteOrdinaireRempl = "";
    private String officeAiCompEpouse = "";
    private String revenuAnnuelMoyenSansBTE = "";
    private String revenuPrisEnCompte = "";
    private String survenanceEvtAssureEpouse = "";

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
        getFrom += TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_3B;
        getFrom += " ON ";
        getFrom += REAnnonceHeader.FIELDNAME_ID_ANNONCE;
        getFrom += "=";
        getFrom += FIELDNAME_ID_ANNONCE_ABS_LEV_3B;

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
        return TABLE_NAME_ANNONCE_ABSTRACT_LEVEL_3B;
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

        revenuPrisEnCompte = statement.dbReadString(FIELDNAME_REVENU_PRIS_EN_COMPTE);
        isLimiteRevenu = statement.dbReadString(FIELDNAME_IS_LIMITE_REVENU);
        isMinimumGaranti = statement.dbReadString(FIELDNAME_IS_MINIMUM_GARANTI);
        officeAiCompEpouse = statement.dbReadString(FIELDNAME_OFFICE_AI_COMP_EPOUSE);
        degreInvaliditeEpouse = statement.dbReadString(FIELDNAME_DEGRE_INVALIDITE_EPOUSE);
        codeInfirmiteEpouse = statement.dbReadString(FIELDNAME_CODE_INFIRMITE_EPOUSE);
        survenanceEvtAssureEpouse = statement.dbReadString(FIELDNAME_SURVENANCE_EVT_ASSURE_EPOUSE);
        ageDebutInvaliditeEpouse = statement.dbReadString(FIELDNAME_AGE_DEBUT_INVALIDITE_EPOUSE);
        revenuAnnuelMoyenSansBTE = statement.dbReadString(FIELDNAME_REVENU_ANNUEL_MOYEN_SANS_BTE);
        bteMoyennePrisEnCompte = statement.dbReadString(FIELDNAME_BTE_MOYENNE_PRISE_EN_COMPTE);
        mntRenteOrdinaireRempl = statement.dbReadString(FIELDNAME_MNT_RENTE_ORDINAIRE_REMPLACEE);
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
        statement.writeKey(FIELDNAME_ID_ANNONCE_ABS_LEV_3B,
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
            statement.writeField(FIELDNAME_ID_ANNONCE_ABS_LEV_3B,
                    _dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
        }

        statement.writeField(FIELDNAME_REVENU_PRIS_EN_COMPTE,
                _dbWriteString(statement.getTransaction(), revenuPrisEnCompte, "revenuPrisEnCompte"));
        statement.writeField(FIELDNAME_IS_LIMITE_REVENU,
                _dbWriteString(statement.getTransaction(), isLimiteRevenu, "isLimiteRevenu"));
        statement.writeField(FIELDNAME_IS_MINIMUM_GARANTI,
                _dbWriteString(statement.getTransaction(), isMinimumGaranti, "isMinimumGaranti"));
        statement.writeField(FIELDNAME_OFFICE_AI_COMP_EPOUSE,
                _dbWriteString(statement.getTransaction(), officeAiCompEpouse, "officeAiCompEpouse"));
        statement.writeField(FIELDNAME_DEGRE_INVALIDITE_EPOUSE,
                _dbWriteString(statement.getTransaction(), degreInvaliditeEpouse, "degreInvaliditeEpouse"));
        statement.writeField(FIELDNAME_CODE_INFIRMITE_EPOUSE,
                _dbWriteString(statement.getTransaction(), codeInfirmiteEpouse, "codeInfirmiteEpouse"));
        statement.writeField(FIELDNAME_SURVENANCE_EVT_ASSURE_EPOUSE,
                _dbWriteString(statement.getTransaction(), survenanceEvtAssureEpouse, "survenanceEvtAssureEpouse"));
        statement.writeField(FIELDNAME_AGE_DEBUT_INVALIDITE_EPOUSE,
                _dbWriteString(statement.getTransaction(), ageDebutInvaliditeEpouse, "ageDebutInvaliditeEpouse"));
        statement.writeField(FIELDNAME_REVENU_ANNUEL_MOYEN_SANS_BTE,
                _dbWriteString(statement.getTransaction(), revenuAnnuelMoyenSansBTE, "revenuAnnuelMoyenSansBTE"));
        statement.writeField(FIELDNAME_BTE_MOYENNE_PRISE_EN_COMPTE,
                _dbWriteString(statement.getTransaction(), bteMoyennePrisEnCompte, "bteMoyennePrisEnCompte"));
        statement.writeField(FIELDNAME_MNT_RENTE_ORDINAIRE_REMPLACEE,
                _dbWriteString(statement.getTransaction(), mntRenteOrdinaireRempl, "mntRenteOrdinaireRempl"));
    }

    /**
     * @return
     */
    public String getAgeDebutInvaliditeEpouse() {
        return ageDebutInvaliditeEpouse;
    }

    /**
     * @return
     */
    public String getBteMoyennePrisEnCompte() {
        return bteMoyennePrisEnCompte;
    }

    /**
     * @return
     */
    public String getCodeInfirmiteEpouse() {
        return codeInfirmiteEpouse;
    }

    /**
     * @return
     */
    public String getDegreInvaliditeEpouse() {
        return degreInvaliditeEpouse;
    }

    /**
     * @return
     */
    public String getIsLimiteRevenu() {
        return isLimiteRevenu;
    }

    /**
     * @return
     */
    public String getIsMinimumGaranti() {
        return isMinimumGaranti;
    }

    /**
     * NE PAS UTILISER CETTEE METHODE. Utiliser REANN41.ZGMMEN --> getMensualiteRenteOrdRemp
     * 
     * @param mntRenteOrdinaireRempl
     */
    @Deprecated
    public String getMntRenteOrdinaireRempl() {
        return mntRenteOrdinaireRempl;
    }

    /**
     * @return
     */
    public String getOfficeAiCompEpouse() {
        return officeAiCompEpouse;
    }

    /**
     * @return
     */
    public String getRevenuAnnuelMoyenSansBTE() {
        return revenuAnnuelMoyenSansBTE;
    }

    /**
     * @return
     */
    public String getRevenuPrisEnCompte() {
        return revenuPrisEnCompte;
    }

    /**
     * @return
     */
    public String getSurvenanceEvtAssureEpouse() {
        return survenanceEvtAssureEpouse;
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
    public void setAgeDebutInvaliditeEpouse(String string) {
        ageDebutInvaliditeEpouse = string;
    }

    /**
     * @param string
     */
    public void setBteMoyennePrisEnCompte(String string) {
        bteMoyennePrisEnCompte = string;
    }

    /**
     * @param string
     */
    public void setCodeInfirmiteEpouse(String string) {
        codeInfirmiteEpouse = string;
    }

    /**
     * @param string
     */
    public void setDegreInvaliditeEpouse(String string) {
        degreInvaliditeEpouse = string;
    }

    /**
     * @param string
     */
    public void setIsLimiteRevenu(String string) {
        isLimiteRevenu = string;
    }

    /**
     * @param string
     */
    public void setIsMinimumGaranti(String string) {
        isMinimumGaranti = string;
    }

    /**
     * NE PAS UTILISER CETTEE METHODE. Utiliser REANN41.ZGMMEN --> getMensualiteRenteOrdRemp
     * 
     * @param mntRenteOrdinaireRempl
     */
    @Deprecated
    public void setMntRenteOrdinaireRempl(String mntRenteOrdinaireRempl) {
        this.mntRenteOrdinaireRempl = mntRenteOrdinaireRempl;
    }

    /**
     * @param string
     */
    public void setOfficeAiCompEpouse(String string) {
        officeAiCompEpouse = string;
    }

    /**
     * @param string
     */
    public void setRevenuAnnuelMoyenSansBTE(String string) {
        revenuAnnuelMoyenSansBTE = string;
    }

    /**
     * @param string
     */
    public void setRevenuPrisEnCompte(String string) {
        revenuPrisEnCompte = string;
    }

    /**
     * @param string
     */
    public void setSurvenanceEvtAssureEpouse(String string) {
        survenanceEvtAssureEpouse = string;
    }

}
