/*
 * Créé le 10 décembre 2010
 */
package globaz.cygnus.db.ordresversements;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author FHA
 * 
 */
public class RFOrdresVersements extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CS_ROLE = "FQTROL";
    public static final String FIELDNAME_ID_DECISION_PARENT = "FQIDDP";
    public static final String FIELDNAME_ID_DOMAINE_APPLICATION = "FQIDAP";
    public static final String FIELDNAME_ID_EXTERNE = "FQIEXT";
    public static final String FIELDNAME_ID_ORDRE_VERSEMENT = "FQIOVE";
    public static final String FIELDNAME_ID_PRESTATION = "FQIPRE";
    public static final String FIELDNAME_ID_ROLE = "FQIROL";
    public static final String FIELDNAME_ID_SECTION_DETTE = "FQISED";
    public static final String FIELDNAME_ID_SOUS_TYPE_DE_SOIN = "FQISTS";
    public static final String FIELDNAME_ID_TIERS = "FQITIE";
    public static final String FIELDNAME_ID_TIERS_ADRESSE_PAIEMENT = "FQITAP";
    public static final String FIELDNAME_ID_TYPE_DE_SOIN = "FQITSO";
    public static final String FIELDNAME_IS_COMPENSE = "FQBCOM";
    public static final String FIELDNAME_IS_FORCER_PAYEMENT = "FQBFPA";
    public static final String FIELDNAME_IS_IMPORTATION = "FQBIMP";
    public static final String FIELDNAME_MONTANT = "FQMMNT";
    public static final String FIELDNAME_MONTANT_DEPASSEMENT_QD = "FQMDQD";
    public static final String FIELDNAME_NUMERO_FACTURE = "FQINFA";
    public static final String FIELDNAME_TYPE_VERSEMENT = "FQTTYP";

    public static final String TABLE_NAME_ORVER = "RFORVER";

    public static final RFOrdresVersements loadOV(BSession session, BITransaction transaction, String idOV)
            throws Exception {
        RFOrdresVersements retValue;

        retValue = new RFOrdresVersements();
        retValue.setIdOrdreVersement(idOV);
        retValue.setSession(session);

        if (transaction == null) {
            retValue.retrieve();
        } else {
            retValue.retrieve(transaction);
        }

        return retValue;
    }

    private String csRole = "";
    private String idDecisionParent = "";
    private String idDomaineApplication = "";
    private String idExterne = "";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String idOrdreVersement = "";
    private String idPrestation = "";
    private String idRole = "";
    private String idSectionDette = "";
    private String idSousTypeSoin = "";
    private String idTiers = "";
    private String idTiersAdressePaiement = "";
    private String idTypeSoin = "";
    private Boolean isCompense = Boolean.FALSE;
    private Boolean isForcerPayement = Boolean.FALSE;
    private Boolean isImportation = Boolean.FALSE;
    private String montant = "";
    private String montantDepassementQD = "";
    private String numeroFacture = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String typeVersement = "";

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
        setIdOrdreVersement(this._incCounter(transaction, "0"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return RFOrdresVersements.TABLE_NAME_ORVER;
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

        idOrdreVersement = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_ORDRE_VERSEMENT);
        idPrestation = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_PRESTATION);
        typeVersement = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_TYPE_VERSEMENT);
        numeroFacture = statement.dbReadString(RFOrdresVersements.FIELDNAME_NUMERO_FACTURE);
        idExterne = statement.dbReadString(RFOrdresVersements.FIELDNAME_ID_EXTERNE);
        csRole = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_CS_ROLE);
        idTiersAdressePaiement = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_TIERS_ADRESSE_PAIEMENT);
        idDomaineApplication = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_DOMAINE_APPLICATION);
        montant = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_MONTANT);
        montantDepassementQD = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_MONTANT_DEPASSEMENT_QD);
        idTiers = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_TIERS);
        idRole = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_ROLE);
        isForcerPayement = statement.dbReadBoolean(RFOrdresVersements.FIELDNAME_IS_FORCER_PAYEMENT);
        idTypeSoin = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_TYPE_DE_SOIN);
        idSousTypeSoin = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);
        isImportation = statement.dbReadBoolean(RFOrdresVersements.FIELDNAME_IS_IMPORTATION);
        isCompense = statement.dbReadBoolean(RFOrdresVersements.FIELDNAME_IS_COMPENSE);
        idDecisionParent = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_DECISION_PARENT);
        idSectionDette = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_SECTION_DETTE);

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
        statement.writeKey(RFOrdresVersements.FIELDNAME_ID_ORDRE_VERSEMENT,
                this._dbWriteNumeric(statement.getTransaction(), idOrdreVersement, "idOrdreVersement"));
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

        statement.writeField(RFOrdresVersements.FIELDNAME_ID_ORDRE_VERSEMENT,
                this._dbWriteNumeric(statement.getTransaction(), idOrdreVersement, "idOrdreVersement"));
        statement.writeField(RFOrdresVersements.FIELDNAME_ID_PRESTATION,
                this._dbWriteNumeric(statement.getTransaction(), idPrestation, "idPrestation"));
        statement.writeField(RFOrdresVersements.FIELDNAME_TYPE_VERSEMENT,
                this._dbWriteNumeric(statement.getTransaction(), typeVersement, "typeVersement"));
        statement.writeField(RFOrdresVersements.FIELDNAME_NUMERO_FACTURE,
                this._dbWriteString(statement.getTransaction(), numeroFacture, "numeroFacture"));
        statement.writeField(RFOrdresVersements.FIELDNAME_ID_EXTERNE,
                this._dbWriteString(statement.getTransaction(), idExterne, "idExterne"));
        statement.writeField(RFOrdresVersements.FIELDNAME_CS_ROLE,
                this._dbWriteNumeric(statement.getTransaction(), csRole, "csRole"));
        statement.writeField(RFOrdresVersements.FIELDNAME_ID_TIERS_ADRESSE_PAIEMENT,
                this._dbWriteNumeric(statement.getTransaction(), idTiersAdressePaiement, "idTiersAdressePaiement"));
        statement.writeField(RFOrdresVersements.FIELDNAME_ID_DOMAINE_APPLICATION,
                this._dbWriteNumeric(statement.getTransaction(), idDomaineApplication, "idDomaineApplication"));
        statement.writeField(RFOrdresVersements.FIELDNAME_MONTANT,
                this._dbWriteNumeric(statement.getTransaction(), montant, "montant"));
        statement.writeField(RFOrdresVersements.FIELDNAME_MONTANT_DEPASSEMENT_QD,
                this._dbWriteNumeric(statement.getTransaction(), montantDepassementQD, "montantDepassementQD"));
        statement.writeField(RFOrdresVersements.FIELDNAME_ID_TIERS,
                this._dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(RFOrdresVersements.FIELDNAME_ID_ROLE,
                this._dbWriteNumeric(statement.getTransaction(), idRole, "idRole"));
        statement.writeField(RFOrdresVersements.FIELDNAME_ID_TYPE_DE_SOIN,
                this._dbWriteNumeric(statement.getTransaction(), idTypeSoin, "idTypeSoin"));
        statement.writeField(RFOrdresVersements.FIELDNAME_ID_SOUS_TYPE_DE_SOIN,
                this._dbWriteNumeric(statement.getTransaction(), idSousTypeSoin, "idSousTypeSoin"));
        statement.writeField(RFOrdresVersements.FIELDNAME_IS_FORCER_PAYEMENT, this._dbWriteBoolean(
                statement.getTransaction(), isForcerPayement, BConstants.DB_TYPE_BOOLEAN_CHAR, "isForcerPayement"));
        statement.writeField(RFOrdresVersements.FIELDNAME_IS_IMPORTATION, this._dbWriteBoolean(
                statement.getTransaction(), isImportation, BConstants.DB_TYPE_BOOLEAN_CHAR, "isImportation"));
        statement.writeField(RFOrdresVersements.FIELDNAME_IS_COMPENSE, this._dbWriteBoolean(statement.getTransaction(),
                isCompense, BConstants.DB_TYPE_BOOLEAN_CHAR, "isCompense"));
        statement.writeField(RFOrdresVersements.FIELDNAME_ID_DECISION_PARENT,
                this._dbWriteNumeric(statement.getTransaction(), idDecisionParent, "idDecisionParent"));

        statement.writeField(RFOrdresVersements.FIELDNAME_ID_SECTION_DETTE,
                this._dbWriteNumeric(statement.getTransaction(), idSectionDette, "idSectionDette"));

    }

    public String getCsRole() {
        return csRole;
    }

    public String getIdDecisionParent() {
        return idDecisionParent;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdOrdreVersement() {
        return idOrdreVersement;
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    public String getIdRole() {
        return idRole;
    }

    public String getIdSectionDette() {
        return idSectionDette;
    }

    public String getIdSousTypeSoin() {
        return idSousTypeSoin;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public String getIdTypeSoin() {
        return idTypeSoin;
    }

    public Boolean getIsCompense() {
        return isCompense;
    }

    public Boolean getIsForcerPayement() {
        return isForcerPayement;
    }

    public Boolean getIsImportation() {
        return isImportation;
    }

    public String getMontant() {
        return montant;
    }

    public String getMontantDepassementQD() {
        return montantDepassementQD;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public String getTypeVersement() {
        return typeVersement;
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

    public void setCsRole(String csRole) {
        this.csRole = csRole;
    }

    public void setIdDecisionParent(String idDecisionParent) {
        this.idDecisionParent = idDecisionParent;
    }

    public void setIdDomaineApplication(String idDomaineApplication) {
        this.idDomaineApplication = idDomaineApplication;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdOrdreVersement(String idOrdreVersement) {
        this.idOrdreVersement = idOrdreVersement;
    }

    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    public void setIdSectionDette(String idSectionDette) {
        this.idSectionDette = idSectionDette;
    }

    public void setIdSousTypeSoin(String idSousTypeSoin) {
        this.idSousTypeSoin = idSousTypeSoin;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public void setIdTypeSoin(String idTypeSoin) {
        this.idTypeSoin = idTypeSoin;
    }

    public void setIsCompense(Boolean isCompense) {
        this.isCompense = isCompense;
    }

    public void setIsForcerPayement(Boolean isForcerPayement) {
        this.isForcerPayement = isForcerPayement;
    }

    public void setIsImportation(Boolean isImportation) {
        this.isImportation = isImportation;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMontantDepassementQD(String montantDepassementQD) {
        this.montantDepassementQD = montantDepassementQD;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public void setTypeVersement(String typeVersement) {
        this.typeVersement = typeVersement;
    }

}
