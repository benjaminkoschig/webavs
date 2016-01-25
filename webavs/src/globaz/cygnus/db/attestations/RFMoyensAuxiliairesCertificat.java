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
public class RFMoyensAuxiliairesCertificat extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_CERTIFICAT = "FXDCER";
    public static final String FIELDNAME_ID_ATTESTATION_MOYEN_AUX_CERTIFICAT = "FXIATT";

    public static final String TABLE_NAME = "RFMAUXC";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String dateCertificat = "";
    private String idAttestationMoyenAuxCertificat = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDossiers.
     */
    public RFMoyensAuxiliairesCertificat() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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
    // @Override
    // protected String _getFrom(BStatement statement) {
    //
    // String getFrom = "";
    //
    // getFrom += this._getCollection();
    // getFrom += RFAttestation.TABLE_NAME;
    // getFrom += " INNER JOIN ";
    // getFrom += this._getCollection();
    // getFrom += RFMoyensAuxiliairesCertificat.TABLE_NAME;
    // getFrom += " ON ";
    // getFrom += RFMoyensAuxiliairesCertificat.FIELDNAME_ID_ATTESTATION_MOYEN_AUX_CERTIFICAT;
    // getFrom += "=";
    // getFrom += RFAttestation.FIELDNAME_ID_ATTESTATION;
    //
    // return getFrom;
    // }

    /**
     * getter pour le nom de la table des dossiers
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFMoyensAuxiliairesCertificat.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        // super._readProperties(statement);
        idAttestationMoyenAuxCertificat = statement
                .dbReadNumeric(RFMoyensAuxiliairesCertificat.FIELDNAME_ID_ATTESTATION_MOYEN_AUX_CERTIFICAT);
        dateCertificat = statement.dbReadDateAMJ(RFMoyensAuxiliairesCertificat.FIELDNAME_DATE_CERTIFICAT);

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
        statement.writeKey(RFMoyensAuxiliairesCertificat.FIELDNAME_ID_ATTESTATION_MOYEN_AUX_CERTIFICAT, this
                ._dbWriteNumeric(statement.getTransaction(), idAttestationMoyenAuxCertificat,
                        "idAttestationMoyenAuxCertificat"));

    }

    /**
     * Méthode d'écriture des champs dans la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RFMoyensAuxiliairesCertificat.FIELDNAME_ID_ATTESTATION_MOYEN_AUX_CERTIFICAT, this
                ._dbWriteNumeric(statement.getTransaction(), idAttestationMoyenAuxCertificat,
                        "idAttestationMoyenAuxCertificat"));
        statement.writeField(RFMoyensAuxiliairesCertificat.FIELDNAME_DATE_CERTIFICAT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateCertificat, "idTiers"));

    }

    public String getDateCertificat() {
        return dateCertificat;
    }

    public String getIdAttestationMoyenAuxCertificat() {
        return idAttestationMoyenAuxCertificat;
    }

    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setDateCertificat(String dateCertificat) {
        this.dateCertificat = dateCertificat;
    }

    public void setIdAttestationMoyenAuxCertificat(String idAttestationMoyenAuxCertificat) {
        this.idAttestationMoyenAuxCertificat = idAttestationMoyenAuxCertificat;
    }
}
