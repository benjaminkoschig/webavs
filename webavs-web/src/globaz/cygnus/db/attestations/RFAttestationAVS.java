/*
 * Créé le 11 novembre 2011
 */
package globaz.cygnus.db.attestations;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * author fha
 */
public class RFAttestationAVS extends RFAttestation {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_GENRE_TRAVAUX = "GALGTR";
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    public static final String FIELDNAME_ID_ATTESTATION_AVS = "GAIATT";
    public static final String FIELDNAME_ID_TIERS_BENEFICIAIRE = "GAITIE";
    public static final String FIELDNAME_TAUX_HORAIRE = "GAMTHO";

    public static final String TABLE_NAME = "RFATAVS";

    private String genreTravaux = "";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String idAttestationAVS = "";
    private String idTiers = "";
    private String tauxHoraire = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDossiers.
     */
    public RFAttestationAVS() {
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
        getFrom += RFAttestationAVS.TABLE_NAME;
        getFrom += " ON ";
        getFrom += RFAttestationAVS.FIELDNAME_ID_ATTESTATION_AVS;
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
        return RFAttestationAVS.TABLE_NAME;
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
        idAttestationAVS = statement.dbReadNumeric(RFAttestationAVS.FIELDNAME_ID_ATTESTATION_AVS);
        genreTravaux = statement.dbReadString(RFAttestationAVS.FIELDNAME_GENRE_TRAVAUX);
        idTiers = statement.dbReadNumeric(RFAttestationAVS.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        tauxHoraire = statement.dbReadNumeric(RFAttestationAVS.FIELDNAME_TAUX_HORAIRE);

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
        statement.writeKey(RFAttestationAVS.FIELDNAME_ID_ATTESTATION_AVS,
                this._dbWriteNumeric(statement.getTransaction(), idAttestationAVS, "idAttestationAVS"));

    }

    /**
     * Méthode d'écriture des champs dans la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RFAttestationAVS.FIELDNAME_ID_ATTESTATION_AVS,
                this._dbWriteNumeric(statement.getTransaction(), idAttestationAVS, "idAttestationAVS"));
        statement.writeField(RFAttestationAVS.FIELDNAME_ID_TIERS_BENEFICIAIRE,
                this._dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(RFAttestationAVS.FIELDNAME_GENRE_TRAVAUX,
                this._dbWriteString(statement.getTransaction(), genreTravaux, "genreTravaux"));
        statement.writeField(RFAttestationAVS.FIELDNAME_TAUX_HORAIRE,
                this._dbWriteNumeric(statement.getTransaction(), tauxHoraire, "tauxHoraire"));

    }

    public String getGenreTravaux() {
        return genreTravaux;
    }

    public String getIdAttestationAVS() {
        return idAttestationAVS;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getTauxHoraire() {
        return tauxHoraire;
    }

    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setGenreTravaux(String genreTravaux) {
        this.genreTravaux = genreTravaux;
    }

    public void setIdAttestationAVS(String idAttestationAVS) {
        this.idAttestationAVS = idAttestationAVS;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setTauxHoraire(String tauxHoraire) {
        this.tauxHoraire = tauxHoraire;
    }

}
