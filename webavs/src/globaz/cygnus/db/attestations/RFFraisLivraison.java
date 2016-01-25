/*
 * Créé le 14 avril 2010
 */
package globaz.cygnus.db.attestations;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * author fha
 */
public class RFFraisLivraison extends RFAttestation {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ATTESTATION_LIVRAISON = "FJDALI";
    public static final String FIELDNAME_CENTRE_LOCATION = "FJLCLO";
    public static final String FIELDNAME_DATE = "FJDATE";
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    public static final String FIELDNAME_ID_ATTESTATION_FRAIS_LIVRAISON = "FJIATT";
    public static final String FIELDNAME_OFFICE_PC = "FJLOPC";
    public static final String FIELDNAME_REMARQUE = "FJLREM";

    public static final String TABLE_NAME = "RFFRLIV";

    private String attestationLivraison = "";
    private String centreLocation = "";
    private String date = "";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String idAttestationFraisLivraison = "";
    private String officePC = "";
    private String remarque = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDossiers.
     */
    public RFFraisLivraison() {
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

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /**
     * Méthode avant l'ajout l'incrémentation de la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        String getFrom = "";

        getFrom += _getCollection();
        getFrom += RFAttestation.TABLE_NAME;
        getFrom += " INNER JOIN ";
        getFrom += _getCollection();
        getFrom += RFFraisLivraison.TABLE_NAME;
        getFrom += " ON ";
        getFrom += RFFraisLivraison.FIELDNAME_ID_ATTESTATION_FRAIS_LIVRAISON;
        getFrom += "=";
        getFrom += RFAttestation.FIELDNAME_ID_ATTESTATION;

        return getFrom;
    }

    /**
     * getter pour le nom de la table des dossiers
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFFraisLivraison.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idAttestationFraisLivraison = statement
                .dbReadNumeric(RFFraisLivraison.FIELDNAME_ID_ATTESTATION_FRAIS_LIVRAISON);
        centreLocation = statement.dbReadString(RFFraisLivraison.FIELDNAME_CENTRE_LOCATION);
        date = statement.dbReadDateAMJ(RFFraisLivraison.FIELDNAME_DATE);
        officePC = statement.dbReadString(RFFraisLivraison.FIELDNAME_OFFICE_PC);
        remarque = statement.dbReadString(RFFraisLivraison.FIELDNAME_REMARQUE);
        attestationLivraison = statement.dbReadDateAMJ(RFFraisLivraison.FIELDNAME_ATTESTATION_LIVRAISON);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des demande maintien à dom.
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFFraisLivraison.FIELDNAME_ID_ATTESTATION_FRAIS_LIVRAISON, this._dbWriteNumeric(
                statement.getTransaction(), idAttestationFraisLivraison, "idAttestationFraisLivraison"));

    }

    /**
     * Méthode d'écriture des champs dans la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RFFraisLivraison.FIELDNAME_ID_ATTESTATION_FRAIS_LIVRAISON, this._dbWriteNumeric(
                statement.getTransaction(), idAttestationFraisLivraison, "idAttestationFraisLivraison"));
        statement.writeField(RFFraisLivraison.FIELDNAME_CENTRE_LOCATION,
                this._dbWriteString(statement.getTransaction(), centreLocation, "centreLocation"));
        statement.writeField(RFFraisLivraison.FIELDNAME_DATE,
                this._dbWriteDateAMJ(statement.getTransaction(), date, "date"));
        statement.writeField(RFFraisLivraison.FIELDNAME_OFFICE_PC,
                this._dbWriteString(statement.getTransaction(), officePC, "officePC"));
        statement.writeField(RFFraisLivraison.FIELDNAME_REMARQUE,
                this._dbWriteString(statement.getTransaction(), remarque, "remarque"));
        statement.writeField(RFFraisLivraison.FIELDNAME_ATTESTATION_LIVRAISON,
                this._dbWriteDateAMJ(statement.getTransaction(), attestationLivraison, "attestationLivraison"));
    }

    public String getAttestationLivraison() {
        return attestationLivraison;
    }

    public String getCentreLocation() {
        return centreLocation;
    }

    public String getDate() {
        return date;
    }

    public String getIdAttestationFraisLivraison() {
        return idAttestationFraisLivraison;
    }

    public String getOfficePC() {
        return officePC;
    }

    public String getRemarque() {
        return remarque;
    }

    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setAttestationLivraison(String attestationLivraison) {
        this.attestationLivraison = attestationLivraison;
    }

    public void setCentreLocation(String centreLocation) {
        this.centreLocation = centreLocation;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIdAttestationFraisLivraison(String idAttestationFraisLivraison) {
        this.idAttestationFraisLivraison = idAttestationFraisLivraison;
    }

    public void setOfficePC(String officePC) {
        this.officePC = officePC;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

}
