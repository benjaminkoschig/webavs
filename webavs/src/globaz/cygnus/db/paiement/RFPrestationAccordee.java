/*
 * Créé le 10 décembre 2010
 */
package globaz.cygnus.db.paiement;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author FHA
 * @revision JJE 19.08.2011
 */
public class RFPrestationAccordee extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CS_SOURCE = "FRSRFM";
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    public static final String FIELDNAME_CS_TYPE_RFA = "FRTTRF";
    public static final String FIELDNAME_DATE_AUGMENTATION = "FRDAUG";
    public static final String FIELDNAME_DATE_DIMINUTION = "FRDDIM";
    public static final String FIELDNAME_DATE_DIMINUTION_INITIALE = "FRDDII";
    public static final String FIELDNAME_DATE_FIN_DROIT_INITIALE = "FRDFDI";
    public static final String FIELDNAME_ID_ADRESSE_PAIEMENT = "FRIADP";
    public static final String FIELDNAME_ID_DECISION = "FRIDEC";
    public static final String FIELDNAME_ID_GESTIONNAIRE_PREPARER_DECISION = "FRIGPD";
    public static final String FIELDNAME_ID_LOT = "FRILOT";
    public static final String FIELDNAME_ID_PARENT_ADAPTATION = "FRIPAA";
    public static final String FIELDNAME_ID_RFM_ACCORDEE = "FRIRFA";
    public static final String FIELDNAME_IS_ADAPTATION = "FRBIAD";
    public static final String FIELDNAME_IS_SUPPRIME = "FRBISU";
    public static final String TABLE_NAME_RFM_ACCORDEE = "RFACCOR";

    private String cs_source = "";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String csTypeRFA = "";
    private String dateAugmentation = "";
    private String dateDiminution = "";
    private String dateDiminutionInitiale = "";
    private String dateFinDroitInitiale = "";
    private String idAdressePaiement = "";
    private String idDecision = "";
    private String idGestionnairePreparerDecision = "";
    private String idLot = "";
    private String idParentAdaptation = "";
    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    private String idRFMAccordee = "";
    private Boolean isAdaptation = Boolean.FALSE;
    private Boolean isSupprime = Boolean.FALSE;

    /**
     * Crée une nouvelle instance de la classe RFPrestationAccordee
     */
    public RFPrestationAccordee() {
        super();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return faux
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

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
        // this.setIdRFMAccordee(this._incCounter(transaction, "0"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return RFPrestationAccordee.TABLE_NAME_RFM_ACCORDEE;
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

        idRFMAccordee = statement.dbReadNumeric(RFPrestationAccordee.FIELDNAME_ID_RFM_ACCORDEE);
        isSupprime = statement.dbReadBoolean(RFPrestationAccordee.FIELDNAME_IS_SUPPRIME);
        csTypeRFA = statement.dbReadNumeric(RFPrestationAccordee.FIELDNAME_CS_TYPE_RFA);
        idDecision = statement.dbReadNumeric(RFPrestationAccordee.FIELDNAME_ID_DECISION);
        idLot = statement.dbReadNumeric(RFPrestationAccordee.FIELDNAME_ID_LOT);
        idGestionnairePreparerDecision = statement
                .dbReadString(RFPrestationAccordee.FIELDNAME_ID_GESTIONNAIRE_PREPARER_DECISION);
        dateFinDroitInitiale = statement.dbReadDateAMJ(RFPrestationAccordee.FIELDNAME_DATE_FIN_DROIT_INITIALE);
        idAdressePaiement = statement.dbReadNumeric(RFPrestationAccordee.FIELDNAME_ID_ADRESSE_PAIEMENT);
        cs_source = statement.dbReadNumeric(RFPrestationAccordee.FIELDNAME_CS_SOURCE);
        dateAugmentation = statement.dbReadDateAMJ(RFPrestationAccordee.FIELDNAME_DATE_AUGMENTATION);
        dateDiminution = statement.dbReadDateAMJ(RFPrestationAccordee.FIELDNAME_DATE_DIMINUTION);
        idParentAdaptation = statement.dbReadNumeric(RFPrestationAccordee.FIELDNAME_ID_PARENT_ADAPTATION);
        isAdaptation = statement.dbReadBoolean(RFPrestationAccordee.FIELDNAME_IS_ADAPTATION);
        dateDiminutionInitiale = statement.dbReadDateAMJ(RFPrestationAccordee.FIELDNAME_DATE_DIMINUTION_INITIALE);

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
        statement.writeKey(RFPrestationAccordee.FIELDNAME_ID_RFM_ACCORDEE,
                this._dbWriteNumeric(statement.getTransaction(), idRFMAccordee, "idRFMAccordee"));
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

        statement.writeField(RFPrestationAccordee.FIELDNAME_ID_RFM_ACCORDEE,
                this._dbWriteNumeric(statement.getTransaction(), idRFMAccordee, "idRFMAccordee"));
        statement.writeField(RFPrestationAccordee.FIELDNAME_IS_SUPPRIME, this._dbWriteBoolean(
                statement.getTransaction(), isSupprime, BConstants.DB_TYPE_BOOLEAN_CHAR, "isSupprime"));
        statement.writeField(RFPrestationAccordee.FIELDNAME_CS_TYPE_RFA,
                this._dbWriteNumeric(statement.getTransaction(), csTypeRFA, "csTypeRFA"));
        statement.writeField(RFPrestationAccordee.FIELDNAME_ID_DECISION,
                this._dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));
        statement.writeField(RFPrestationAccordee.FIELDNAME_ID_LOT,
                this._dbWriteNumeric(statement.getTransaction(), idLot, "idLot"));
        statement.writeField(RFPrestationAccordee.FIELDNAME_ID_GESTIONNAIRE_PREPARER_DECISION, this._dbWriteString(
                statement.getTransaction(), idGestionnairePreparerDecision, "idGestionnairePreparerDecision"));
        statement.writeField(RFPrestationAccordee.FIELDNAME_DATE_FIN_DROIT_INITIALE,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFinDroitInitiale, "dateFinDroitInitiale"));
        statement.writeField(RFPrestationAccordee.FIELDNAME_ID_ADRESSE_PAIEMENT,
                this._dbWriteNumeric(statement.getTransaction(), idAdressePaiement, "idAdressePaiement"));
        statement.writeField(RFPrestationAccordee.FIELDNAME_CS_SOURCE,
                this._dbWriteNumeric(statement.getTransaction(), cs_source, "cs_source"));
        statement.writeField(RFPrestationAccordee.FIELDNAME_DATE_AUGMENTATION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateAugmentation, "dateAugmentation"));
        statement.writeField(RFPrestationAccordee.FIELDNAME_DATE_DIMINUTION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDiminution, "dateDiminution"));
        statement.writeField(RFPrestationAccordee.FIELDNAME_ID_PARENT_ADAPTATION,
                this._dbWriteNumeric(statement.getTransaction(), idParentAdaptation, "idParentAdaptation"));
        statement.writeField(RFPrestationAccordee.FIELDNAME_IS_ADAPTATION, this._dbWriteBoolean(
                statement.getTransaction(), isAdaptation, BConstants.DB_TYPE_BOOLEAN_CHAR, "isAdaptation"));
        statement.writeField(RFPrestationAccordee.FIELDNAME_DATE_DIMINUTION_INITIALE,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDiminutionInitiale, "dateDiminutionInitiale"));

    }

    public String getCs_source() {
        return cs_source;
    }

    public String getCsTypeRFA() {
        return csTypeRFA;
    }

    public String getDateAugmentation() {
        return dateAugmentation;
    }

    public String getDateDiminution() {
        return dateDiminution;
    }

    public String getDateDiminutionInitiale() {
        return dateDiminutionInitiale;
    }

    public String getDateFinDroitInitiale() {
        return dateFinDroitInitiale;
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdGestionnairePreparerDecision() {
        return idGestionnairePreparerDecision;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getIdParentAdaptation() {
        return idParentAdaptation;
    }

    public String getIdRFMAccordee() {
        return idRFMAccordee;
    }

    public Boolean getIsAdaptation() {
        return isAdaptation;
    }

    public Boolean getIsSupprime() {
        return isSupprime;
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

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setCs_source(String cs_source) {
        this.cs_source = cs_source;
    }

    public void setCsTypeRFA(String csTypeRFA) {
        this.csTypeRFA = csTypeRFA;
    }

    public void setDateAugmentation(String dateAugmentation) {
        this.dateAugmentation = dateAugmentation;
    }

    public void setDateDiminution(String dateDiminution) {
        this.dateDiminution = dateDiminution;
    }

    public void setDateDiminutionInitiale(String dateDiminutionInitiale) {
        this.dateDiminutionInitiale = dateDiminutionInitiale;
    }

    public void setDateFinDroitInitiale(String dateFinDroitInitiale) {
        this.dateFinDroitInitiale = dateFinDroitInitiale;
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdGestionnairePreparerDecision(String idGestionnairePreparerDecision) {
        this.idGestionnairePreparerDecision = idGestionnairePreparerDecision;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdParentAdaptation(String idParentAdaptation) {
        this.idParentAdaptation = idParentAdaptation;
    }

    public void setIdRFMAccordee(String idRFMAccordee) {
        this.idRFMAccordee = idRFMAccordee;
    }

    public void setIsAdaptation(Boolean isAdaptation) {
        this.isAdaptation = isAdaptation;
    }

    public void setIsSupprime(Boolean isSupprime) {
        this.isSupprime = isSupprime;
    }

}
