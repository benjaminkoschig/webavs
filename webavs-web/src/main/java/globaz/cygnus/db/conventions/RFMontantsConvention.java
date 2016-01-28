// créé le 24 mars 2010
package globaz.cygnus.db.conventions;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * author fha
 */
public class RFMontantsConvention extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_DEBUT = "ETDDEB";
    public static final String FIELDNAME_DATE_FIN = "ETDFIN";
    public static final String FIELDNAME_GENRE_PC = "ETGEPC";
    public static final String FIELDNAME_ID_CONVENTION = "ETICON";
    public static final String FIELDNAME_ID_MONTANT = "ETIMON";
    public static final String FIELDNAME_IS_PLAFONNE = "ETBPLA";
    public static final String FIELDNAME_MNT_MAX_AVEC_API_FAIBLE = "ETMAAF";
    public static final String FIELDNAME_MNT_MAX_AVEC_API_GRAVE = "ETMAAG";
    public static final String FIELDNAME_MNT_MAX_AVEC_API_MOYENNE = "ETMAAM";
    public static final String FIELDNAME_MNT_MAX_DEFAUT = "ETMDEF";
    public static final String FIELDNAME_MNT_MAX_SANS_API = "ETMSAP";
    public static final String FIELDNAME_PERIODICITE = "ETTPER";
    public static final String FIELDNAME_TYPE_BENEFICIAIRE = "ETTCON";
    public static final String FIELDNAME_TYPE_PC = "ETTYPC";

    public static final String TABLE_NAME = "RFMONTA";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Génération de la clause from pour la requête > Jointure depuis les dossiers jusque dans les tiers (Nom et AVS)
     * 
     * @param schema
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFMontantsConvention.TABLE_NAME);

        return fromClauseBuffer.toString();
    }

    public static String getTableName() {
        return RFMontantsConvention.TABLE_NAME;
    }

    public static final RFMontantsConvention loadMontantConvention(BSession session, BITransaction transaction,
            String idMontant) throws Exception {
        RFMontantsConvention retValue;

        retValue = new RFMontantsConvention();
        retValue.setIdMontant(idMontant);
        retValue.setSession(session);

        if (transaction == null) {
            retValue.retrieve();
        } else {
            retValue.retrieve(transaction);
        }

        return retValue;
    }

    private String csTypeBeneficiaire = "";
    private String dateDebut = "";
    private String dateFin = "";
    private String genrePc = "";
    private String idConvention = "";
    private String idMontant = "";
    private String mntMaxAvecApiFaible = "";
    private String mntMaxAvecApiGrave = "";
    private String mntMaxAvecApiMoyen = "";
    private String mntMaxDefaut = "";
    private String mntMaxSansApi = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private String periodicite = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private Boolean plafonne = false;

    private String typePc = "";

    /**
     * Crée une nouvelle instance de la classe RFDossiers.
     */
    public RFMontantsConvention() {
        super();
    }

    /**
     * Méthode avant l'ajout l'incrémentation de la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdMontant(this._incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table des dossiers
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFMontantsConvention.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idMontant = statement.dbReadNumeric(RFMontantsConvention.FIELDNAME_ID_MONTANT);
        csTypeBeneficiaire = statement.dbReadNumeric(RFMontantsConvention.FIELDNAME_TYPE_BENEFICIAIRE);
        dateDebut = statement.dbReadDateAMJ(RFMontantsConvention.FIELDNAME_DATE_DEBUT);
        dateFin = statement.dbReadDateAMJ(RFMontantsConvention.FIELDNAME_DATE_FIN);
        periodicite = statement.dbReadNumeric(RFMontantsConvention.FIELDNAME_PERIODICITE);
        mntMaxSansApi = statement.dbReadNumeric(RFMontantsConvention.FIELDNAME_MNT_MAX_SANS_API);
        mntMaxAvecApiGrave = statement.dbReadNumeric(RFMontantsConvention.FIELDNAME_MNT_MAX_AVEC_API_GRAVE);
        mntMaxAvecApiMoyen = statement.dbReadNumeric(RFMontantsConvention.FIELDNAME_MNT_MAX_AVEC_API_MOYENNE);
        mntMaxAvecApiFaible = statement.dbReadNumeric(RFMontantsConvention.FIELDNAME_MNT_MAX_AVEC_API_FAIBLE);
        mntMaxDefaut = statement.dbReadNumeric(RFMontantsConvention.FIELDNAME_MNT_MAX_DEFAUT);
        idConvention = statement.dbReadNumeric(RFMontantsConvention.FIELDNAME_ID_CONVENTION);
        plafonne = statement.dbReadBoolean(RFMontantsConvention.FIELDNAME_IS_PLAFONNE);
        genrePc = statement.dbReadNumeric(RFMontantsConvention.FIELDNAME_GENRE_PC);
        typePc = statement.dbReadNumeric(RFMontantsConvention.FIELDNAME_TYPE_PC);

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
        statement.writeKey(RFMontantsConvention.FIELDNAME_ID_MONTANT,
                this._dbWriteNumeric(statement.getTransaction(), idMontant, "idMontant"));

    }

    /**
     * Méthode d'écriture des champs dans la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RFMontantsConvention.FIELDNAME_ID_MONTANT,
                this._dbWriteNumeric(statement.getTransaction(), idMontant, "idMontant"));
        statement.writeField(RFMontantsConvention.FIELDNAME_TYPE_BENEFICIAIRE,
                this._dbWriteNumeric(statement.getTransaction(), csTypeBeneficiaire, "csTypeBeneficiaire"));
        statement.writeField(RFMontantsConvention.FIELDNAME_DATE_DEBUT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDebut, "dateDebut"));
        statement.writeField(RFMontantsConvention.FIELDNAME_DATE_FIN,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFin, "dateFin"));
        statement.writeField(RFMontantsConvention.FIELDNAME_PERIODICITE,
                this._dbWriteNumeric(statement.getTransaction(), periodicite, "periodicite"));
        statement.writeField(RFMontantsConvention.FIELDNAME_MNT_MAX_SANS_API,
                this._dbWriteNumeric(statement.getTransaction(), mntMaxSansApi, "mntMaxSansApi"));
        statement.writeField(RFMontantsConvention.FIELDNAME_MNT_MAX_AVEC_API_GRAVE,
                this._dbWriteNumeric(statement.getTransaction(), mntMaxAvecApiGrave, "mntMaxAvecApiGrave"));
        statement.writeField(RFMontantsConvention.FIELDNAME_MNT_MAX_AVEC_API_MOYENNE,
                this._dbWriteNumeric(statement.getTransaction(), mntMaxAvecApiMoyen, "mntMaxAvecApiMoyen"));
        statement.writeField(RFMontantsConvention.FIELDNAME_MNT_MAX_AVEC_API_FAIBLE,
                this._dbWriteNumeric(statement.getTransaction(), mntMaxAvecApiFaible, "mntMaxAvecApiFaible"));
        statement.writeField(RFMontantsConvention.FIELDNAME_MNT_MAX_DEFAUT,
                this._dbWriteNumeric(statement.getTransaction(), mntMaxDefaut, "mntMaxDeaut"));
        statement.writeField(RFMontantsConvention.FIELDNAME_ID_CONVENTION,
                this._dbWriteNumeric(statement.getTransaction(), idConvention, "idConvention"));
        statement.writeField(RFMontantsConvention.FIELDNAME_IS_PLAFONNE, this._dbWriteBoolean(
                statement.getTransaction(), isPlafonne(), BConstants.DB_TYPE_BOOLEAN_CHAR, "isPlafonne"));
        statement.writeField(RFMontantsConvention.FIELDNAME_TYPE_PC,
                this._dbWriteNumeric(statement.getTransaction(), typePc, "typePc"));
        statement.writeField(RFMontantsConvention.FIELDNAME_GENRE_PC,
                this._dbWriteNumeric(statement.getTransaction(), genrePc, "genrePc"));

    }

    public String getCsTypeBeneficiaire() {
        return csTypeBeneficiaire;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getGenrePc() {
        return genrePc;
    }

    public String getIdConvention() {
        return idConvention;
    }

    public String getIdMontant() {
        return idMontant;
    }

    public String getMntMaxAvecApiFaible() {
        return mntMaxAvecApiFaible;
    }

    public String getMntMaxAvecApiGrave() {
        return mntMaxAvecApiGrave;
    }

    public String getMntMaxAvecApiMoyen() {
        return mntMaxAvecApiMoyen;
    }

    public String getMntMaxDefaut() {
        return mntMaxDefaut;
    }

    public String getMntMaxSansApi() {
        return mntMaxSansApi;
    }

    public String getPeriodicite() {
        return periodicite;
    }

    public String getTypePc() {
        return typePc;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public Boolean isPlafonne() {
        return plafonne;
    }

    public void setCsTypeBeneficiaire(String csTypeBeneficiaire) {
        this.csTypeBeneficiaire = csTypeBeneficiaire;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setGenrePc(String genrePc) {
        this.genrePc = genrePc;
    }

    public void setIdConvention(String idConvention) {
        this.idConvention = idConvention;
    }

    public void setIdMontant(String idMontant) {
        this.idMontant = idMontant;
    }

    public void setMntMaxAvecApiFaible(String mntMaxAvecApiFaible) {
        this.mntMaxAvecApiFaible = mntMaxAvecApiFaible;
    }

    public void setMntMaxAvecApiGrave(String mntMaxAvecApiGrave) {
        this.mntMaxAvecApiGrave = mntMaxAvecApiGrave;
    }

    public void setMntMaxAvecApiMoyen(String mntMaxAvecApiMoyen) {
        this.mntMaxAvecApiMoyen = mntMaxAvecApiMoyen;
    }

    public void setMntMaxDefaut(String mntMaxDefaut) {
        this.mntMaxDefaut = mntMaxDefaut;
    }

    public void setMntMaxSansApi(String mntMaxSansApi) {
        this.mntMaxSansApi = mntMaxSansApi;
    }

    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

    public void setPlafonne(Boolean plafonne) {
        this.plafonne = plafonne;
    }

    public void setTypePc(String typePc) {
        this.typePc = typePc;
    }

}
