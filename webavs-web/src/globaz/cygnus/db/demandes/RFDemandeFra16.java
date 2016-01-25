/*
 * Créé le 07 février 2012
 */
package globaz.cygnus.db.demandes;

import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author mbo
 */
public class RFDemandeFra16 extends RFDemande {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    public static final String FIELDNAME_CS_TYPE_VHC = "GBCSTP";
    public static final String FIELDNAME_ID_DEMANDE_FRA_16 = "GBIDEM";
    public static final String FIELDNAME_IS_TVA = "GBITVA";
    public static final String FIELDNAME_NB_KILOMETRES = "GBNBKM";
    public static final String FIELDNAME_PRIX_KILOMETRE = "GBPRKM";

    public static final String TABLE_NAME = "RFDFT16";

    private String csTypeVhc = "";
    private boolean hasCreationSpy = false;
    private boolean hasSpy = false;
    private String idDemandeFra16 = "";
    private boolean isTva = Boolean.FALSE;
    private String nombreKilometres = null;
    private String prixKilometre = null;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDemandeFrq17Fra18
     */
    public RFDemandeFra16() {
        super();
    }

    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * Méthode avant l'ajout l'incrémentation de la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // super._beforeAdd(transaction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        String getFrom = "";

        getFrom += _getCollection();
        getFrom += RFDemande.TABLE_NAME;
        getFrom += " INNER JOIN ";
        getFrom += _getCollection();
        getFrom += RFDemandeFra16.TABLE_NAME;
        getFrom += " ON ";
        getFrom += RFDemandeFra16.FIELDNAME_ID_DEMANDE_FRA_16;
        getFrom += "=";
        getFrom += RFDemande.FIELDNAME_ID_DEMANDE;

        return getFrom;
    }

    /**
     * getter pour le nom de la table des demandes frqp et fra
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFDemandeFra16.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des demandes frqp et fra
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idDemandeFra16 = statement.dbReadNumeric(RFDemandeFra16.FIELDNAME_ID_DEMANDE_FRA_16);
        csTypeVhc = statement.dbReadNumeric(RFDemandeFra16.FIELDNAME_CS_TYPE_VHC);
        nombreKilometres = statement.dbReadNumeric(RFDemandeFra16.FIELDNAME_NB_KILOMETRES);
        prixKilometre = statement.dbReadNumeric(RFDemandeFra16.FIELDNAME_PRIX_KILOMETRE);
        isTva = statement.dbReadBoolean(RFDemandeFra16.FIELDNAME_IS_TVA);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des demandes frqp et fra
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFDemandeFra16.FIELDNAME_ID_DEMANDE_FRA_16,
                this._dbWriteNumeric(statement.getTransaction(), idDemandeFra16, "idDemandeFra16"));
    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Méthode d'écriture des champs dans la table des demandes frqp et fra
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // super._writeProperties(statement);
        statement.writeField(RFDemandeFra16.FIELDNAME_ID_DEMANDE_FRA_16,
                this._dbWriteNumeric(statement.getTransaction(), idDemandeFra16, "idDemandeFra16"));
        statement.writeField(RFDemandeFra16.FIELDNAME_CS_TYPE_VHC,
                this._dbWriteNumeric(statement.getTransaction(), csTypeVhc, "csTypeVhc"));
        statement.writeField(RFDemandeFra16.FIELDNAME_NB_KILOMETRES,
                this._dbWriteNumeric(statement.getTransaction(), nombreKilometres, "nombreKilometres"));
        statement.writeField(RFDemandeFra16.FIELDNAME_PRIX_KILOMETRE,
                this._dbWriteNumeric(statement.getTransaction(), prixKilometre, "prixKilometre"));
        statement.writeField(RFDemandeFra16.FIELDNAME_IS_TVA,
                this._dbWriteBoolean(statement.getTransaction(), isTva, BConstants.DB_TYPE_BOOLEAN_CHAR, "isTva"));

    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getCsTypeVhc() {
        return csTypeVhc;
    }

    public String getIdDemandeFra16() {
        return idDemandeFra16;
    }

    public final boolean getIsTva() {
        return isTva;
    }

    public String getNombreKilometres() {
        return nombreKilometres;
    }

    public String getPrixKilometre() {
        return prixKilometre;
    }

    @Override
    public boolean hasCreationSpy() {
        return hasCreationSpy;
    }

    @Override
    public boolean hasSpy() {
        return hasSpy;
    }

    public void setCsTypeVhc(String csTypeVhc) {
        this.csTypeVhc = csTypeVhc;
    }

    public void setHasCreationSpy(boolean hasCreationSpy) {
        this.hasCreationSpy = hasCreationSpy;
    }

    public void setHasSpy(boolean hasSpy) {
        this.hasSpy = hasSpy;
    }

    public void setIdDemandeFra16(String idDemandeFra16) {
        this.idDemandeFra16 = idDemandeFra16;
    }

    public void setNombreKilometres(String nombreKilometres) {
        this.nombreKilometres = nombreKilometres;
    }

    public void setPrixKilometre(String prixKilometre) {
        this.prixKilometre = prixKilometre;
    }

    public final void setTva(boolean isTva) {
        this.isTva = isTva;
    }

}