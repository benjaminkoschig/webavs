/*
 * Créé le 14 avril 2010
 */
package globaz.cygnus.db.attestations;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * author fha
 */
public class RFMoyensAuxiliairesDecision extends RFAttestation {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_DECISION = "FYDDEC";
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    public static final String FIELDNAME_ID_ATTESTATION_MOYEN_AUX_DECISION = "FYIATT";
    public static final String FIELDNAME_LIBELLE_MOYEN_AUXILIAIRE_DECISION = "FYLMOA";

    public static final String TABLE_NAME = "RFMAUXD";

    private String dateDecision = "";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String idAttestationMoyenAuxDecision = "";
    private String libelleMoyenAuxiliaireDecision = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDossiers.
     */
    public RFMoyensAuxiliairesDecision() {
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
        getFrom += RFMoyensAuxiliairesDecision.TABLE_NAME;
        getFrom += " ON ";
        getFrom += RFMoyensAuxiliairesDecision.FIELDNAME_ID_ATTESTATION_MOYEN_AUX_DECISION;
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
        return RFMoyensAuxiliairesDecision.TABLE_NAME;
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
        idAttestationMoyenAuxDecision = statement
                .dbReadNumeric(RFMoyensAuxiliairesDecision.FIELDNAME_ID_ATTESTATION_MOYEN_AUX_DECISION);
        libelleMoyenAuxiliaireDecision = statement
                .dbReadString(RFMoyensAuxiliairesDecision.FIELDNAME_LIBELLE_MOYEN_AUXILIAIRE_DECISION);
        dateDecision = statement.dbReadDateAMJ(RFMoyensAuxiliairesDecision.FIELDNAME_DATE_DECISION);

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
        statement.writeKey(RFMoyensAuxiliairesDecision.FIELDNAME_ID_ATTESTATION_MOYEN_AUX_DECISION, this
                ._dbWriteNumeric(statement.getTransaction(), idAttestationMoyenAuxDecision,
                        "idAttestationMoyenAuxDecision"));

    }

    /**
     * Méthode d'écriture des champs dans la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RFMoyensAuxiliairesDecision.FIELDNAME_ID_ATTESTATION_MOYEN_AUX_DECISION, this
                ._dbWriteNumeric(statement.getTransaction(), idAttestationMoyenAuxDecision,
                        "idAttestationMoyenAuxDecision"));
        statement.writeField(RFMoyensAuxiliairesDecision.FIELDNAME_LIBELLE_MOYEN_AUXILIAIRE_DECISION, this
                ._dbWriteString(statement.getTransaction(), libelleMoyenAuxiliaireDecision,
                        "libelleMoyenAuxiliaireDecision"));
        statement.writeField(RFMoyensAuxiliairesDecision.FIELDNAME_DATE_DECISION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDecision, "dateDecision"));

    }

    public String getDateDecision() {
        return dateDecision;
    }

    public String getIdAttestationMoyenAuxDecision() {
        return idAttestationMoyenAuxDecision;
    }

    public String getLibelleMoyenAuxiliaireDecision() {
        return libelleMoyenAuxiliaireDecision;
    }

    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public void setIdAttestationMoyenAuxDecision(String idAttestationMoyenAuxDecision) {
        this.idAttestationMoyenAuxDecision = idAttestationMoyenAuxDecision;
    }

    public void setLibelleMoyenAuxiliaireDecision(String libelleMoyenAuxiliaireDecision) {
        this.libelleMoyenAuxiliaireDecision = libelleMoyenAuxiliaireDecision;
    }

}
