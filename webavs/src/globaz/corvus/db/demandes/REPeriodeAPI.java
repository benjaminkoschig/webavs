/*
 * Créé le 3 janv. 07
 */
package globaz.corvus.db.demandes;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.clone.factory.IPRCloneable;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class REPeriodeAPI extends BEntity implements IPRCloneable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_DATE_DEBUT_INVALIDITE = "YHDDEB";

    public static final String FIELDNAME_DATE_FIN_INVALIDITE = "YHDFIN";
    public static final String FIELDNAME_DEGRE_IMPOTENCE = "YHTDIM";
    public static final String FIELDNAME_ID_DEMANDE_RENTE = "YHIDEM";
    // Nom des champs de la table
    public static final String FIELDNAME_ID_PERIODE_API = "YHIPAP";
    public static final String FIELDNAME_IS_ASSISTANCE_PRATIQUE = "YHBASP";
    public static final String FIELDNAME_IS_RESIDENCE_HOME = "YHBREH";
    public static final String FIELDNAME_CS_GENRE_DROIT_API = "YHTGDR";
    public static final String FIELDNAME_TYPE_PRESTATION_HISTORIQUE = "YHTPHI";
    public static final String FIELDNAME_TYPE_PRESTATION = "YHTPRS";

    // Nom de la table
    public static final String TABLE_NAME = "REPEAPI";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csDegreImpotence = "";
    private String dateDebutInvalidite = "";
    private String dateFinInvalidite = "";
    private String idDemandeRente = "";
    private String idPeriodeAPI = "";
    private Boolean isAssistancePratique = Boolean.FALSE;
    private Boolean isResidenceHome = Boolean.FALSE;
    private String csGenreDroitApi = "";
    private String typePrestationHistorique = "";
    private String typePrestation = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getTypePrestationHistorique() {
        return typePrestationHistorique;
    }

    public String getTypePrestation() {
        return typePrestation;
    }

    public void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }

    public void setTypePrestationHistorique(String typePrestationHistorique) {
        this.typePrestationHistorique = typePrestationHistorique;
    }

    public String getCsGenreDroitApi() {
        return csGenreDroitApi;
    }

    public void setCsGenreDroitApi(String csGenreDroitApi) {
        this.csGenreDroitApi = csGenreDroitApi;
    }

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
        setIdPeriodeAPI(_incCounter(transaction, "0"));

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
        return TABLE_NAME;
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

        idPeriodeAPI = statement.dbReadNumeric(FIELDNAME_ID_PERIODE_API);
        dateDebutInvalidite = statement.dbReadDateAMJ(FIELDNAME_DATE_DEBUT_INVALIDITE);
        dateFinInvalidite = statement.dbReadDateAMJ(FIELDNAME_DATE_FIN_INVALIDITE);
        csDegreImpotence = statement.dbReadNumeric(FIELDNAME_DEGRE_IMPOTENCE);
        idDemandeRente = statement.dbReadNumeric(FIELDNAME_ID_DEMANDE_RENTE);
        isAssistancePratique = statement.dbReadBoolean(FIELDNAME_IS_ASSISTANCE_PRATIQUE);
        isResidenceHome = statement.dbReadBoolean(FIELDNAME_IS_RESIDENCE_HOME);
        csGenreDroitApi = statement.dbReadNumeric(FIELDNAME_CS_GENRE_DROIT_API);
        typePrestationHistorique = statement.dbReadString(FIELDNAME_TYPE_PRESTATION_HISTORIQUE);
        typePrestation = statement.dbReadString(FIELDNAME_TYPE_PRESTATION);
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
        statement.writeKey(FIELDNAME_ID_PERIODE_API,
                _dbWriteNumeric(statement.getTransaction(), idPeriodeAPI, "idPeriodeAPI"));
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
        statement.writeField(FIELDNAME_ID_PERIODE_API,
                _dbWriteNumeric(statement.getTransaction(), idPeriodeAPI, "idPeriodeAPI"));
        statement.writeField(FIELDNAME_DATE_DEBUT_INVALIDITE,
                _dbWriteDateAMJ(statement.getTransaction(), dateDebutInvalidite, "dateDebutInvalidite"));
        statement.writeField(FIELDNAME_DATE_FIN_INVALIDITE,
                _dbWriteDateAMJ(statement.getTransaction(), dateFinInvalidite, "dateFinInvalidite"));
        statement.writeField(FIELDNAME_DEGRE_IMPOTENCE,
                _dbWriteNumeric(statement.getTransaction(), csDegreImpotence, "degreImpotence"));
        statement.writeField(FIELDNAME_ID_DEMANDE_RENTE,
                _dbWriteNumeric(statement.getTransaction(), idDemandeRente, "idDemandeRente"));
        statement.writeField(
                FIELDNAME_IS_ASSISTANCE_PRATIQUE,
                _dbWriteBoolean(statement.getTransaction(), isAssistancePratique, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "isAssistancePratique"));
        statement.writeField(
                FIELDNAME_IS_RESIDENCE_HOME,
                _dbWriteBoolean(statement.getTransaction(), isResidenceHome, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "isResidenceHome"));
        statement.writeField(FIELDNAME_CS_GENRE_DROIT_API,
                _dbWriteNumeric(statement.getTransaction(), csGenreDroitApi, "csGenreDroitApi"));
        statement.writeField(FIELDNAME_TYPE_PRESTATION_HISTORIQUE,
                this._dbWriteString(statement.getTransaction(), typePrestationHistorique, "typePrestationHistorique"));
        statement.writeField(FIELDNAME_TYPE_PRESTATION,
                this._dbWriteString(statement.getTransaction(), typePrestation, "typePrestation"));
    }

    @Override
    public IPRCloneable duplicate(int action) throws Exception {
        REPeriodeAPI clone = new REPeriodeAPI();
        clone.setDateDebutInvalidite(getDateDebutInvalidite());
        clone.setDateFinInvalidite(getDateFinInvalidite());
        clone.setCsDegreImpotence(getCsDegreImpotence());
        clone.setIdDemandeRente(getIdDemandeRente());
        clone.setIsAssistancePratique(getIsAssistancePratique());
        clone.setIsResidenceHome(getIsResidenceHome());
        clone.setCsGenreDroitApi(getCsGenreDroitApi());
        clone.setTypePrestation(getTypePrestation());
        clone.setTypePrestationHistorique(getTypePrestationHistorique());

        clone.wantCallValidate(false);
        return clone;
    }

    /**
     * @return the csDegreImpotence
     */
    public String getCsDegreImpotence() {
        return csDegreImpotence;
    }

    /**
     * getter pour l'attribut dateDebutInvalidite
     * 
     * @return la valeur courante de l'attribut dateDebutInvalidite
     */
    public String getDateDebutInvalidite() {
        return dateDebutInvalidite;
    }

    /**
     * getter pour l'attribut dateFinInvalidite
     * 
     * @return la valeur courante de l'attribut dateFinInvalidite
     */
    public String getDateFinInvalidite() {
        return dateFinInvalidite;
    }

    /**
     * getter pour l'attribut idDemandeRente
     * 
     * @return la valeur courante de l'attribut idDemandeRente
     */
    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    /**
     * getter pour l'attribut idPeriodeAPI
     * 
     * @return la valeur courante de l'attribut idPeriodeAPI
     */
    public String getIdPeriodeAPI() {
        return idPeriodeAPI;
    }

    /**
     * getter pour l'attribut isAssistancePratique
     * 
     * @return la valeur courante de l'attribut isAssistancePratique
     */
    public Boolean getIsAssistancePratique() {
        return isAssistancePratique;
    }

    /**
     * getter pour l'attribut isResidenceHome
     * 
     * @return la valeur courante de l'attribut isResidenceHome
     */
    public Boolean getIsResidenceHome() {
        return isResidenceHome;
    }

    @Override
    public String getUniquePrimaryKey() {
        return getIdPeriodeAPI();
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
     * @param csDegreImpotence
     *            the csDegreImpotence to set
     */
    public void setCsDegreImpotence(String csDegreImpotence) {
        this.csDegreImpotence = csDegreImpotence;
    }

    /**
     * setter pour l'attribut dateDebutInvalidite.
     * 
     * @param dateDebutInvalidite
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutInvalidite(String string) {
        dateDebutInvalidite = string;
    }

    /**
     * setter pour l'attribut dateFinInvalidite.
     * 
     * @param dateFinInvalidite
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFinInvalidite(String string) {
        dateFinInvalidite = string;
    }

    /**
     * setter pour l'attribut idDemandeRente.
     * 
     * @param idDemandeRente
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDemandeRente(String string) {
        idDemandeRente = string;
    }

    /**
     * setter pour l'attribut idPeriodeAPI.
     * 
     * @param idPeriodeAPI
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPeriodeAPI(String string) {
        idPeriodeAPI = string;
    }

    /**
     * setter pour l'attribut isAssistancePratique.
     * 
     * @param isAssistancePratique
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsAssistancePratique(Boolean string) {
        isAssistancePratique = string;
    }

    /**
     * setter pour l'attribut isResidenceHome.
     * 
     * @param isResidenceHome
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsResidenceHome(Boolean string) {
        isResidenceHome = string;
    }

    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdPeriodeAPI(pk);

    }

}
