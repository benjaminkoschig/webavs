/*
 * Créé le 14 avril 2010
 */
package globaz.cygnus.db.attestations;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * author fha
 */
public class RFMoyensAuxiliairesBon extends RFAttestation {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE = "FKDATE";
    public static final String FIELDNAME_DATE_ENTRETIEN_TEL = "FKDETE";
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    public static final String FIELDNAME_ID_ATTESTATION_MOYEN_AUX_BON = "FKIATT";
    public static final String FIELDNAME_ID_TIERS_BENEFICIAIRE = "FKITBE";
    public static final String FIELDNAME_LIBELLE_MOYEN_AUXILIAIRE = "FKLMAU";

    public static final String TABLE_NAME = "RFMAUXB";

    private String date = "";
    private String dateEntretienTel = "";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String idAttestationMoyenAuxBon = "";
    private String idTiers = "";
    private String moyenAuxiliaire = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDossiers.
     */
    public RFMoyensAuxiliairesBon() {
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
    @Override
    protected String _getFrom(BStatement statement) {

        String getFrom = "";

        getFrom += _getCollection();
        getFrom += RFAttestation.TABLE_NAME;
        getFrom += " INNER JOIN ";
        getFrom += _getCollection();
        getFrom += RFMoyensAuxiliairesBon.TABLE_NAME;
        getFrom += " ON ";
        getFrom += RFMoyensAuxiliairesBon.FIELDNAME_ID_ATTESTATION_MOYEN_AUX_BON;
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
        return RFMoyensAuxiliairesBon.TABLE_NAME;
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
        idAttestationMoyenAuxBon = statement
                .dbReadNumeric(RFMoyensAuxiliairesBon.FIELDNAME_ID_ATTESTATION_MOYEN_AUX_BON);
        moyenAuxiliaire = statement.dbReadString(RFMoyensAuxiliairesBon.FIELDNAME_LIBELLE_MOYEN_AUXILIAIRE);
        idTiers = statement.dbReadNumeric(RFMoyensAuxiliairesBon.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        date = statement.dbReadDateAMJ(RFMoyensAuxiliairesBon.FIELDNAME_DATE);
        dateEntretienTel = statement.dbReadDateAMJ(RFMoyensAuxiliairesBon.FIELDNAME_DATE_ENTRETIEN_TEL);

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
        statement.writeKey(RFMoyensAuxiliairesBon.FIELDNAME_ID_ATTESTATION_MOYEN_AUX_BON,
                this._dbWriteNumeric(statement.getTransaction(), idAttestationMoyenAuxBon, "idAttestationMoyenAuxBon"));

    }

    /**
     * Méthode d'écriture des champs dans la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RFMoyensAuxiliairesBon.FIELDNAME_ID_ATTESTATION_MOYEN_AUX_BON,
                this._dbWriteNumeric(statement.getTransaction(), idAttestationMoyenAuxBon, "idAttestationMoyenAuxBon"));
        statement.writeField(RFMoyensAuxiliairesBon.FIELDNAME_ID_TIERS_BENEFICIAIRE,
                this._dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(RFMoyensAuxiliairesBon.FIELDNAME_LIBELLE_MOYEN_AUXILIAIRE,
                this._dbWriteString(statement.getTransaction(), moyenAuxiliaire, "moyenAuxiliaire"));
        statement.writeField(RFMoyensAuxiliairesBon.FIELDNAME_DATE,
                this._dbWriteDateAMJ(statement.getTransaction(), date, "date"));
        statement.writeField(RFMoyensAuxiliairesBon.FIELDNAME_DATE_ENTRETIEN_TEL,
                this._dbWriteDateAMJ(statement.getTransaction(), dateEntretienTel, "dateEntretienTel"));

    }

    public String getDate() {
        return date;
    }

    public String getDateEntretienTel() {
        return dateEntretienTel;
    }

    public String getIdAttestationMoyenAuxBon() {
        return idAttestationMoyenAuxBon;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMoyenAuxiliaire() {
        return moyenAuxiliaire;
    }

    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDateEntretienTel(String dateEntretienTel) {
        this.dateEntretienTel = dateEntretienTel;
    }

    public void setIdAttestationMoyenAuxBon(String idAttestationMoyenAuxBon) {
        this.idAttestationMoyenAuxBon = idAttestationMoyenAuxBon;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMoyenAuxiliaire(String moyenAuxiliaire) {
        this.moyenAuxiliaire = moyenAuxiliaire;
    }
}
