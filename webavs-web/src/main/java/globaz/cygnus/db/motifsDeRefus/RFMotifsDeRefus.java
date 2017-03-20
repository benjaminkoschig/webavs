/*
 * Créé le 26 janvier 2010
 */
package globaz.cygnus.db.motifsDeRefus;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author jje
 */
public class RFMotifsDeRefus extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DESCRIPTION_DE = "EHLMDE";
    public static final String FIELDNAME_DESCRIPTION_FR = "EHLMFR";
    public static final String FIELDNAME_DESCRIPTION_IT = "EHLMIT";

    public static final String FIELDNAME_DESCRIPTION_LONGUE_DE = "EHLDMD";
    public static final String FIELDNAME_DESCRIPTION_LONGUE_FR = "EHLDMF";
    public static final String FIELDNAME_DESCRIPTION_LONGUE_IT = "EHLDMI";

    public static final String FIELDNAME_HAS_MONTANT = "EHBMNT";
    public static final String FIELDNAME_ID_MOTIF_REFUS = "EHIMOR";
    public static final String FIELDNAME_ID_MOTIF_REFUS_SYSTEME = "EHIMRS";

    public static final String FIELDNAME_IDS_SOIN = "EHISOI";
    public static final String FIELDNAME_IS_MOTIF_SYSTEME = "EHBMSY";

    public static final String TABLE_NAME = "RFMOTRE";

    /**
     * Génération de la clause from pour la requête > Jointure depuis les dossiers, demandes jusqu'au tiers
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFMotifsDeRefus.TABLE_NAME);

        return fromClauseBuffer.toString();
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String descriptionDE = "";
    private String descriptionFR = "";
    private String descriptionIT = "";

    private String descriptionLongueDE = "";
    private String descriptionLongueFR = "";
    private String descriptionLongueIT = "";

    private transient String fromClause = null;
    private Boolean hasMontant = Boolean.FALSE;
    private String idMotifRefus = "";

    private String idMotifRefusSysteme = "";
    private String idsSoin = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private Boolean isMotifRefusSysteme = Boolean.FALSE;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFMotifsDeRefus
     */
    public RFMotifsDeRefus() {
        super();
    }

    /**
     * Méthode avant l'ajout l'incrémentation de la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdMotifRefus(this._incCounter(transaction, "0"));
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFMotifsDeRefus.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * getter pour le nom de la table des motifs de refus
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFMotifsDeRefus.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des motifs de refus
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idMotifRefus = statement.dbReadNumeric(RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS);
        idsSoin = statement.dbReadString(RFMotifsDeRefus.FIELDNAME_IDS_SOIN);
        hasMontant = statement.dbReadBoolean(RFMotifsDeRefus.FIELDNAME_HAS_MONTANT);

        descriptionFR = statement.dbReadString(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_FR);
        descriptionIT = statement.dbReadString(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_IT);
        descriptionDE = statement.dbReadString(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_DE);

        descriptionLongueFR = statement.dbReadString(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_LONGUE_FR);
        descriptionLongueIT = statement.dbReadString(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_LONGUE_IT);
        descriptionLongueDE = statement.dbReadString(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_LONGUE_DE);

        isMotifRefusSysteme = statement.dbReadBoolean(RFMotifsDeRefus.FIELDNAME_IS_MOTIF_SYSTEME);
        idMotifRefusSysteme = statement.dbReadString(RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS_SYSTEME);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des motifs de refus
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS,
                this._dbWriteNumeric(statement.getTransaction(), idMotifRefus, "idMotifRefus"));
    }

    /**
     * Méthode d'écriture des champs dans la table des motifs de refus
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS,
                this._dbWriteNumeric(statement.getTransaction(), idMotifRefus, "idMotifRefus"));
        statement.writeField(RFMotifsDeRefus.FIELDNAME_IDS_SOIN,
                this._dbWriteString(statement.getTransaction(), idsSoin, "idsSoin"));
        statement.writeField(RFMotifsDeRefus.FIELDNAME_HAS_MONTANT, this._dbWriteBoolean(statement.getTransaction(),
                hasMontant, BConstants.DB_TYPE_BOOLEAN_CHAR, "hasMontant"));

        statement.writeField(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_FR,
                this._dbWriteString(statement.getTransaction(), descriptionFR, "descriptionFR"));
        statement.writeField(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_IT,
                this._dbWriteString(statement.getTransaction(), descriptionIT, "descriptionIT"));
        statement.writeField(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_DE,
                this._dbWriteString(statement.getTransaction(), descriptionDE, "descriptionDE"));

        statement.writeField(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_LONGUE_FR,
                this._dbWriteString(statement.getTransaction(), descriptionLongueFR, "descriptionLongueFR"));
        statement.writeField(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_LONGUE_IT,
                this._dbWriteString(statement.getTransaction(), descriptionLongueIT, "descriptionLongueIT"));
        statement.writeField(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_LONGUE_DE,
                this._dbWriteString(statement.getTransaction(), descriptionLongueDE, "descriptionLongueDE"));

        statement.writeField(RFMotifsDeRefus.FIELDNAME_IS_MOTIF_SYSTEME,
                this._dbWriteBoolean(statement.getTransaction(), isMotifRefusSysteme, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "isMotifRefusSysteme"));
        statement.writeField(RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS_SYSTEME,
                this._dbWriteString(statement.getTransaction(), idMotifRefusSysteme, "idMotifRefusSysteme"));

    }

    public String getDescriptionDE() {
        return descriptionDE;
    }

    public String getDescriptionFR() {
        return descriptionFR;
    }

    public String getDescriptionIT() {
        return descriptionIT;
    }

    public String getDescriptionLongueDE() {
        return descriptionLongueDE;
    }

    public String getDescriptionLongueFR() {
        return descriptionLongueFR;
    }

    public String getDescriptionLongueIT() {
        return descriptionLongueIT;
    }

    public String getFromClause() {
        return fromClause;
    }

    public Boolean getHasMontant() {
        return hasMontant;
    }

    public String getIdMotifRefus() {
        return idMotifRefus;
    }

    public String getIdMotifRefusSysteme() {
        return idMotifRefusSysteme;
    }

    public String getIdsSoin() {
        return idsSoin;
    }

    public Boolean getIsMotifRefusSysteme() {
        return isMotifRefusSysteme;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setDescriptionDE(String descriptionDE) {
        this.descriptionDE = descriptionDE;
    }

    public void setDescriptionFR(String descriptionFR) {
        this.descriptionFR = descriptionFR;
    }

    public void setDescriptionIT(String descriptionIT) {
        this.descriptionIT = descriptionIT;
    }

    public void setDescriptionLongueDE(String descriptionLongueDE) {
        this.descriptionLongueDE = descriptionLongueDE;
    }

    public void setDescriptionLongueFR(String descriptionLongueFR) {
        this.descriptionLongueFR = descriptionLongueFR;
    }

    public void setDescriptionLongueIT(String descriptionLongueIT) {
        this.descriptionLongueIT = descriptionLongueIT;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setHasMontant(Boolean hasMontant) {
        this.hasMontant = hasMontant;
    }

    public void setIdMotifRefus(String idMotifRefus) {
        this.idMotifRefus = idMotifRefus;
    }

    public void setIdMotifRefusSysteme(String idMotifRefusSysteme) {
        this.idMotifRefusSysteme = idMotifRefusSysteme;
    }

    public void setIdsSoin(String idsSoin) {
        this.idsSoin = idsSoin;
    }

    public void setIsMotifRefusSysteme(Boolean isMotifRefusSysteme) {
        this.isMotifRefusSysteme = isMotifRefusSysteme;
    }

}
