// créé le 24 mars 2010
package globaz.cygnus.db.conventions;

import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.globall.db.BStatement;

/**
 * author jje
 */
public class RFConventionJointAssConFouTsJointConventionAssureJointFournisseurJointMontant extends RFConvention {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_TIERS = "TITIERP";

    /**
     * Génération de la clause from pour la requête > Jointure depuis les dossiers jusque dans les tiers (Nom et AVS)
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String leftJoin = " LEFT JOIN ";
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConvention.TABLE_NAME);

        // jointure entre la table convention et la table montant convention
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFMontantsConvention.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConvention.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFConvention.FIELDNAME_ID_CONVENTION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFMontantsConvention.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFMontantsConvention.FIELDNAME_ID_CONVENTION);

        // jointure entre la table convention et la table association convention
        // fournisseur sous type
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssConventionFournisseurSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssConventionFournisseurSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_CONVENTION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConvention.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFConvention.FIELDNAME_ID_CONVENTION);

        // jointure entre la table association convention fournisseur sous type
        // et la table sous type de soin
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssConventionFournisseurSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);

        // jointure entre la table sous type de soin et la table type de soin
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSousTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);

        // jointure entre la table convention et la table convention assuré
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionAssure.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionAssure.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFConventionAssure.FIELDNAME_ID_CONVENTION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConvention.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFConvention.FIELDNAME_ID_CONVENTION);

        return fromClauseBuffer.toString();
    }

    private String csTypeBeneficiaire = "";
    private String dateDebutConventionAssure = "";
    private String dateDebutMontant = "";
    private String dateFinConventionAssure = "";
    private String dateFinMontant = "";
    private transient String fromClause = null;
    private String idAssociationConvStsFour = "";
    private String idAssure = "";
    private String idConvention = "";
    private String idConventionAssure = "";
    private String idFournisseur = "";
    private String idSousTypeSoin = "";
    private String mntAssure = "";
    private String mntMaxAvecApiFaible = "";

    private String mntMaxAvecApiGrave = "";
    private String mntMaxAvecApiMoyen = "";
    private String mntMaxDefaut = "";
    private String mntMaxSansApi = "";
    private Boolean mntPlafonne = Boolean.FALSE;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String periodicite = "";

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * Il est interdit d'effacer un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * Il est interdit de mettre un objet de ce type à jour.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RFConventionJointAssConFouTsJointConventionAssureJointFournisseurJointMontant
                            .createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        idConvention = statement.dbReadNumeric(RFConvention.FIELDNAME_ID_CONVENTION);
        idAssociationConvStsFour = statement
                .dbReadNumeric(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_CONVFOUSTS);
        idFournisseur = statement.dbReadNumeric(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_FOURNISSEUR);
        idSousTypeSoin = statement
                .dbReadNumeric(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);
        idConventionAssure = statement.dbReadNumeric(RFConventionAssure.FIELDNAME_ID_CONVENTION_ASSURE);
        idAssure = statement.dbReadNumeric(RFConventionAssure.FIELDNAME_ID_ASSURE);
        dateDebutConventionAssure = statement.dbReadDateAMJ(RFConventionAssure.FIELDNAME_DATE_DEBUT);
        dateFinConventionAssure = statement.dbReadDateAMJ(RFConventionAssure.FIELDNAME_DATE_FIN);
        dateDebutMontant = statement.dbReadDateAMJ(RFMontantsConvention.FIELDNAME_DATE_DEBUT);
        dateFinMontant = statement.dbReadDateAMJ(RFMontantsConvention.FIELDNAME_DATE_FIN);
        csTypeBeneficiaire = statement.dbReadNumeric(RFMontantsConvention.FIELDNAME_TYPE_BENEFICIAIRE);
        periodicite = statement.dbReadNumeric(RFMontantsConvention.FIELDNAME_PERIODICITE);
        mntAssure = statement.dbReadNumeric(RFConventionAssure.FIELDNAME_MONTANT_ASSURE);
        mntPlafonne = statement.dbReadBoolean(RFMontantsConvention.FIELDNAME_IS_PLAFONNE);

        mntMaxSansApi = statement.dbReadNumeric(RFMontantsConvention.FIELDNAME_MNT_MAX_SANS_API);
        mntMaxAvecApiGrave = statement.dbReadNumeric(RFMontantsConvention.FIELDNAME_MNT_MAX_AVEC_API_GRAVE);
        mntMaxAvecApiMoyen = statement.dbReadNumeric(RFMontantsConvention.FIELDNAME_MNT_MAX_AVEC_API_MOYENNE);
        mntMaxAvecApiFaible = statement.dbReadNumeric(RFMontantsConvention.FIELDNAME_MNT_MAX_AVEC_API_FAIBLE);
        mntMaxDefaut = statement.dbReadNumeric(RFMontantsConvention.FIELDNAME_MNT_MAX_DEFAUT);

    }

    public String getCsTypeBeneficiaire() {
        return csTypeBeneficiaire;
    }

    public String getDateDebutConventionAssure() {
        return dateDebutConventionAssure;
    }

    public String getDateDebutMontant() {
        return dateDebutMontant;
    }

    public String getDateFinConventionAssure() {
        return dateFinConventionAssure;
    }

    public String getDateFinMontant() {
        return dateFinMontant;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getIdAssociationConvStsFour() {
        return idAssociationConvStsFour;
    }

    public String getIdAssure() {
        return idAssure;
    }

    @Override
    public String getIdConvention() {
        return idConvention;
    }

    public String getIdConventionAssure() {
        return idConventionAssure;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public String getIdSousTypeSoin() {
        return idSousTypeSoin;
    }

    public String getMntAssure() {
        return mntAssure;
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

    public Boolean getMntPlafonne() {
        return mntPlafonne;
    }

    public String getPeriodicite() {
        return periodicite;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setCsTypeBeneficiaire(String csTypeBeneficiaire) {
        this.csTypeBeneficiaire = csTypeBeneficiaire;
    }

    public void setDateDebutConventionAssure(String dateDebutConventionAssure) {
        this.dateDebutConventionAssure = dateDebutConventionAssure;
    }

    public void setDateDebutMontant(String dateDebutMontant) {
        this.dateDebutMontant = dateDebutMontant;
    }

    public void setDateFinConventionAssure(String dateFinConventionAssure) {
        this.dateFinConventionAssure = dateFinConventionAssure;
    }

    public void setDateFinMontant(String dateFinMontant) {
        this.dateFinMontant = dateFinMontant;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setIdAssociationConvStsFour(String idAssociationConvStsFour) {
        this.idAssociationConvStsFour = idAssociationConvStsFour;
    }

    public void setIdAssure(String idAssure) {
        this.idAssure = idAssure;
    }

    @Override
    public void setIdConvention(String idConvention) {
        this.idConvention = idConvention;
    }

    public void setIdConventionAssure(String idConventionAssure) {
        this.idConventionAssure = idConventionAssure;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public void setIdSousTypeSoin(String idSousTypeSoin) {
        this.idSousTypeSoin = idSousTypeSoin;
    }

    public void setMntAssure(String mntAssure) {
        this.mntAssure = mntAssure;
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

    public void setMntPlafonne(Boolean mntPlafonne) {
        this.mntPlafonne = mntPlafonne;
    }

    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

}
