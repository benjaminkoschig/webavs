// créé le 24 mars 2010
package globaz.cygnus.db.conventions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * author fha
 */
public class RFAssConventionFournisseurSousTypeDeSoin extends BEntity {

    // ~ Static fields/initializers
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_ADRESSE_PAIEMENT = "EUIFAP";
    public static final String FIELDNAME_ID_CONVENTION = "EUICON";
    public static final String FIELDNAME_ID_CONVFOUSTS = "EUICFS";
    public static final String FIELDNAME_ID_FOURNISSEUR = "EUIFOU";
    public static final String FIELDNAME_ID_SOUS_TYPE_DE_SOIN = "EUISTS";

    public static final String TABLE_NAME = "RFCOFOS";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Génération de la clause from pour la requête > Jointure depuis les dossiers jusque dans les tiers (Nom et AVS)
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionAssure.TABLE_NAME);

        return fromClauseBuffer.toString();
    }

    private String idAdressePaiement = "";
    private String idConvention = "";
    private String idConvFouSts = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private String idFournisseur = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String idSousTypeDeSoin = "";

    /**
     * Crée une nouvelle instance de la classe RFAssConventionFournisseurSousTypeDeSoin.
     */
    public RFAssConventionFournisseurSousTypeDeSoin() {
        super();
    }

    @Override
    protected String _getTableName() {
        return RFAssConventionFournisseurSousTypeDeSoin.TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idConvFouSts = statement.dbReadNumeric(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_CONVFOUSTS);
        idConvention = statement.dbReadNumeric(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_CONVENTION);
        idFournisseur = statement.dbReadNumeric(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_FOURNISSEUR);
        idSousTypeDeSoin = statement
                .dbReadNumeric(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);
        idAdressePaiement = statement
                .dbReadNumeric(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_ADRESSE_PAIEMENT);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_CONVFOUSTS,
                this._dbWriteNumeric(statement.getTransaction(), idConvFouSts, "idConvFouSts"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_CONVFOUSTS,
                this._dbWriteNumeric(statement.getTransaction(), idConvFouSts, "idConvFouSts"));
        statement.writeField(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_CONVENTION,
                this._dbWriteNumeric(statement.getTransaction(), idConvention, "idConvention"));
        statement.writeField(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_FOURNISSEUR,
                this._dbWriteNumeric(statement.getTransaction(), idFournisseur, "idFournisseur"));
        statement.writeField(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_DE_SOIN,
                this._dbWriteNumeric(statement.getTransaction(), idSousTypeDeSoin, "idSousTypeDeSoin"));
        statement.writeField(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_ADRESSE_PAIEMENT,
                this._dbWriteNumeric(statement.getTransaction(), idAdressePaiement, "idAdressePaiement"));
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public String getIdConvention() {
        return idConvention;
    }

    public String getIdConvFouSts() {
        return idConvFouSts;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public String getIdSousTypeDeSoin() {
        return idSousTypeDeSoin;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public void setIdConvention(String idConvention) {
        this.idConvention = idConvention;
    }

    public void setIdConvFouSts(String idConvFouSts) {
        this.idConvFouSts = idConvFouSts;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public void setIdSousTypeDeSoin(String idSousTypeDeSoin) {
        this.idSousTypeDeSoin = idSousTypeDeSoin;
    }

}
