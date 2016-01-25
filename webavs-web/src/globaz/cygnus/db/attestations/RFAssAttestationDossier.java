/*
 * Créé le 14 avril 2010
 */
package globaz.cygnus.db.attestations;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * author fha
 */
public class RFAssAttestationDossier extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_DEBUT = "EXDDEB";
    public static final String FIELDNAME_DATE_FIN = "EXDFIN";
    public static final String FIELDNAME_ID_ASSOC_ATTESTATION_DOSSIER = "EXIASS";
    public static final String FIELDNAME_ID_ATTESTATION = "EXIATT";
    public static final String FIELDNAME_ID_DOSSIER = "EXIDOS";
    public static final String FIELDNAME_ID_SOUS_TYPE_SOIN = "EXISOS";

    public static final String TABLE_NAME = "RFADASS";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateDebut = "";
    private String dateFin = "";
    private String idAttestation = "";
    private String idAttestationDossier = "";
    private String idDossier = "";
    private String idSousTypeSoin = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDossiers.
     */
    public RFAssAttestationDossier() {
        super();
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
        setIdAttestationDossier(this._incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table des dossiers
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFAssAttestationDossier.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAttestationDossier = statement.dbReadNumeric(RFAssAttestationDossier.FIELDNAME_ID_ASSOC_ATTESTATION_DOSSIER);
        dateDebut = statement.dbReadDateAMJ(RFAssAttestationDossier.FIELDNAME_DATE_DEBUT);
        dateFin = statement.dbReadDateAMJ(RFAssAttestationDossier.FIELDNAME_DATE_FIN);
        idDossier = statement.dbReadNumeric(RFAssAttestationDossier.FIELDNAME_ID_DOSSIER);
        idAttestation = statement.dbReadNumeric(RFAssAttestationDossier.FIELDNAME_ID_ATTESTATION);
        idSousTypeSoin = statement.dbReadNumeric(RFAssAttestationDossier.FIELDNAME_ID_SOUS_TYPE_SOIN);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFAssAttestationDossier.FIELDNAME_ID_ASSOC_ATTESTATION_DOSSIER,
                this._dbWriteNumeric(statement.getTransaction(), idAttestationDossier, "idAttestationDossier"));

    }

    /**
     * Méthode d'écriture des champs dans la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RFAssAttestationDossier.FIELDNAME_ID_ASSOC_ATTESTATION_DOSSIER,
                this._dbWriteNumeric(statement.getTransaction(), idAttestationDossier, "idAttestationDossier"));
        statement.writeField(RFAssAttestationDossier.FIELDNAME_DATE_DEBUT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDebut, "dateDebut"));
        statement.writeField(RFAssAttestationDossier.FIELDNAME_DATE_FIN,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFin, "dateFin"));
        statement.writeField(RFAssAttestationDossier.FIELDNAME_ID_DOSSIER,
                this._dbWriteNumeric(statement.getTransaction(), idDossier, "idDossier"));
        statement.writeField(RFAssAttestationDossier.FIELDNAME_ID_ATTESTATION,
                this._dbWriteNumeric(statement.getTransaction(), idAttestation, "idAttestation"));
        statement.writeField(RFAssAttestationDossier.FIELDNAME_ID_SOUS_TYPE_SOIN,
                this._dbWriteNumeric(statement.getTransaction(), idSousTypeSoin, "idSousTypeSoin"));
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getIdAttestation() {
        return idAttestation;
    }

    public String getIdAttestationDossier() {
        return idAttestationDossier;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdSousTypeSoin() {
        return idSousTypeSoin;
    }

    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setIdAttestation(String idAttestation) {
        this.idAttestation = idAttestation;
    }

    public void setIdAttestationDossier(String idAttestationDossier) {
        this.idAttestationDossier = idAttestationDossier;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdSousTypeSoin(String idSousTypeSoin) {
        this.idSousTypeSoin = idSousTypeSoin;
    }

}
