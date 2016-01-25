/*
 * Créé le 13 avril 2010
 */
package globaz.cygnus.db.conventions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * author fha
 */
public class RFConventionAssure extends BEntity {

    // ~ Static fields/initializers
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_DEBUT = "EWDDEB";
    public static final String FIELDNAME_DATE_FIN = "EWDFIN";
    public static final String FIELDNAME_ID_ASSURE = "EWIASS";
    public static final String FIELDNAME_ID_CONVENTION = "EWILCO";
    public static final String FIELDNAME_ID_CONVENTION_ASSURE = "EWICON";
    public static final String FIELDNAME_ID_DOSSIER = "EWIDOS";
    public static final String FIELDNAME_MONTANT_ASSURE = "EWMASS";

    public static final String TABLE_NAME = "RFCONAS";

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

    private String dateDebut = "";
    private String dateFin = "";
    private String idAssure = "";
    private String idConvention = "";
    private String idConventionAssure = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private String montantAssure = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFAssConventionFournisseurSousTypeDeSoin.
     */
    public RFConventionAssure() {
        super();
    }

    @Override
    protected String _getTableName() {
        return RFConventionAssure.TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idConventionAssure = statement.dbReadNumeric(RFConventionAssure.FIELDNAME_ID_CONVENTION_ASSURE);
        dateDebut = statement.dbReadDateAMJ(RFConventionAssure.FIELDNAME_DATE_DEBUT);
        dateFin = statement.dbReadDateAMJ(RFConventionAssure.FIELDNAME_DATE_FIN);
        idAssure = statement.dbReadNumeric(RFConventionAssure.FIELDNAME_ID_ASSURE);
        idConvention = statement.dbReadNumeric(RFConventionAssure.FIELDNAME_ID_CONVENTION);
        montantAssure = statement.dbReadNumeric(RFConventionAssure.FIELDNAME_MONTANT_ASSURE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFConventionAssure.FIELDNAME_ID_CONVENTION_ASSURE,
                this._dbWriteNumeric(statement.getTransaction(), idConventionAssure, "idConventionAssure"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RFConventionAssure.FIELDNAME_ID_CONVENTION_ASSURE,
                this._dbWriteNumeric(statement.getTransaction(), idConventionAssure, "idConventionAssure"));
        statement.writeField(RFConventionAssure.FIELDNAME_DATE_DEBUT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDebut, "dateDebut"));
        statement.writeField(RFConventionAssure.FIELDNAME_DATE_FIN,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFin, "dateFin"));
        statement.writeField(RFConventionAssure.FIELDNAME_ID_ASSURE,
                this._dbWriteNumeric(statement.getTransaction(), idAssure, "idAssure"));
        statement.writeField(RFConventionAssure.FIELDNAME_ID_CONVENTION,
                this._dbWriteNumeric(statement.getTransaction(), idConvention, "idConvention"));
        statement.writeField(RFConventionAssure.FIELDNAME_MONTANT_ASSURE,
                this._dbWriteNumeric(statement.getTransaction(), montantAssure, "montantAssure"));
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getIdAssure() {
        return idAssure;
    }

    public String getIdConvention() {
        return idConvention;
    }

    public String getIdConventionAssure() {
        return idConventionAssure;
    }

    public String getMontantAssure() {
        return montantAssure;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setIdAssure(String idAssure) {
        this.idAssure = idAssure;
    }

    public void setIdConvention(String idConvention) {
        this.idConvention = idConvention;
    }

    public void setIdConventionAssure(String idConventionAssure) {
        this.idConventionAssure = idConventionAssure;
    }

    public void setMontantAssure(String montantAssure) {
        this.montantAssure = montantAssure;
    }

}
