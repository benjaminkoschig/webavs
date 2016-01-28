package globaz.cygnus.db.typeDeSoins;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * author fha
 */
public class RFPotAssure extends BEntity {
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // public static final String FIELDNAME_FORCER_PAYEMENT = "FGBFOR";
    public static final String FIELDNAME_ID_POT_ASSURE = "FGIPOA";
    public static final String FIELDNAME_MONTANT_PLAFOND_ADULTE_ENFANTS = "FGMAEN";
    public static final String FIELDNAME_MONTANT_PLAFOND_COUPLE_AVEC_ENFANT = "FGMPCE";
    // public static final String FIELDNAME_IMPUTER_GRANDE_QD = "FGBIGQ";
    public static final String FIELDNAME_MONTANT_PLAFOND_COUPLE_DOMICILE = "FGMPCD";
    public static final String FIELDNAME_MONTANT_PLAFOND_ENFANTS_ENFANTS = "FGMPEE";
    public static final String FIELDNAME_MONTANT_PLAFOND_ENFANTS_SEPARES = "FGMPES";
    public static final String FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_ENFANTS_ENFANTS = "FGMPPE";
    public static final String FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_ENFANTS_SEPARES = "FGMPEP";
    public static final String FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_PERSONNE_SEULE = "FGMPPP";
    public static final String FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_POUR_TOUS = "FGMPTP";
    public static final String FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_SEPARE_DOMICILE = "FGMCDP";
    public static final String FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_SEPARE_HOME = "FGMCSP";
    public static final String FIELDNAME_MONTANT_PLAFOND_PERSONNE_SEULE = "FGMPPS";
    public static final String FIELDNAME_MONTANT_PLAFOND_POUR_TOUS = "FGMPTS";
    public static final String FIELDNAME_MONTANT_PLAFOND_SEPARE_DOMICILE = "FGMCSD";
    public static final String TABLE_NAME = "RFPOTAS";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    public static final RFPotAssure loadPotAssure(BSession session, BITransaction transaction, String idPotAssure)
            throws Exception {
        RFPotAssure retValue;

        retValue = new RFPotAssure();
        retValue.setIdPotAssure(idPotAssure);
        retValue.setSession(session);

        if (transaction == null) {
            retValue.retrieve();
        } else {
            retValue.retrieve(transaction);
        }

        return retValue;
    }

    // private Boolean forcerPayement = Boolean.FALSE;
    private String idPotAssure = "";
    private String montantPlafondAdulteEnfants = "";
    // private Boolean imputerGrandeQd = Boolean.FALSE;
    private String montantPlafondCoupleDomicile = "";
    private String montantPlafondCoupleEnfant = "";
    private String montantPlafondEnfantsEnfants = "";
    private String montantPlafondEnfantsSepares = "";
    private String montantPlafondPensionnaireEnfantsEnfants = "";
    private String montantPlafondPensionnaireEnfantsSepares = "";
    private String montantPlafondPensionnairePersonneSeule = "";
    private String montantPlafondPensionnairePourTous = "";
    private String montantPlafondPensionnaireSepareDomicile = "";
    private String montantPlafondPensionnaireSepareHome = "";
    private String montantPlafondPersonneSeule = "";
    private String montantPlafondPourTous = "";
    private String montantPlafondSepareDomicile = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFTypeDeSoin.
     */
    public RFPotAssure() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Méthode avant l'ajout, incrémentant la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdPotAssure(this._incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table des types de soin
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFPotAssure.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des types de soin
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idPotAssure = statement.dbReadNumeric(RFPotAssure.FIELDNAME_ID_POT_ASSURE);

        montantPlafondPensionnairePourTous = statement
                .dbReadNumeric(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_POUR_TOUS);
        montantPlafondPourTous = statement.dbReadNumeric(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_POUR_TOUS);
        montantPlafondPersonneSeule = statement.dbReadNumeric(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_PERSONNE_SEULE);
        montantPlafondPensionnairePersonneSeule = statement
                .dbReadNumeric(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_PERSONNE_SEULE);
        montantPlafondPensionnaireSepareHome = statement
                .dbReadNumeric(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_SEPARE_HOME);
        montantPlafondSepareDomicile = statement.dbReadNumeric(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_SEPARE_DOMICILE);
        montantPlafondPensionnaireSepareDomicile = statement
                .dbReadNumeric(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_SEPARE_DOMICILE);
        montantPlafondCoupleDomicile = statement.dbReadNumeric(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_COUPLE_DOMICILE);
        montantPlafondEnfantsSepares = statement.dbReadNumeric(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_ENFANTS_SEPARES);
        montantPlafondPensionnaireEnfantsSepares = statement
                .dbReadNumeric(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_ENFANTS_SEPARES);
        montantPlafondAdulteEnfants = statement.dbReadNumeric(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_ADULTE_ENFANTS);
        montantPlafondCoupleEnfant = statement.dbReadNumeric(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_COUPLE_AVEC_ENFANT);

        montantPlafondEnfantsEnfants = statement.dbReadNumeric(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_ENFANTS_ENFANTS);
        montantPlafondPensionnaireEnfantsEnfants = statement
                .dbReadNumeric(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_ENFANTS_ENFANTS);

        // this.forcerPayement = statement.dbReadBoolean(RFPotAssure.FIELDNAME_FORCER_PAYEMENT);
        // this.imputerGrandeQd = statement.dbReadBoolean(RFPotAssure.FIELDNAME_IMPUTER_GRANDE_QD);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des types de soin
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFPotAssure.FIELDNAME_ID_POT_ASSURE,
                this._dbWriteNumeric(statement.getTransaction(), idPotAssure, "idPotAssure"));
    }

    /**
     * Méthode d'écriture des champs dans la table des types de soin
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(RFPotAssure.FIELDNAME_ID_POT_ASSURE,
                this._dbWriteNumeric(statement.getTransaction(), idPotAssure, "idPotAssure"));

        statement.writeField(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_POUR_TOUS,
                this._dbWriteNumeric(statement.getTransaction(), montantPlafondPourTous, "montantPlafondPourTous"));
        statement.writeField(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_POUR_TOUS, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondPensionnairePourTous, "montantPlafondPensionnairePourTous"));

        statement.writeField(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_PERSONNE_SEULE, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondPersonneSeule, "montantPlafondPersonneSeule"));
        statement.writeField(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_PERSONNE_SEULE, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondPensionnairePersonneSeule,
                "montantPlafondPensionnairePersonneSeule"));

        statement.writeField(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_SEPARE_HOME, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondPensionnaireSepareHome,
                "montantPlafondPensionnaireSepareHome"));

        statement.writeField(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_SEPARE_DOMICILE, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondSepareDomicile, "montantPlafondSepareDomicile"));
        statement.writeField(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_SEPARE_DOMICILE, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondPensionnaireSepareDomicile,
                "montantPlafondPensionnaireSepareDomicile"));

        statement.writeField(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_COUPLE_DOMICILE, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondCoupleDomicile, "montantPlafondCoupleDomicile"));

        statement.writeField(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_ENFANTS_SEPARES, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondEnfantsSepares, "montantPlafondEnfantsSepares"));
        statement.writeField(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_ENFANTS_SEPARES, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondPensionnaireEnfantsSepares,
                "montantPlafondPensionnaireEnfantsSepares"));

        statement.writeField(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_ADULTE_ENFANTS, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondAdulteEnfants, "montantPlafondAdulteEnfants"));

        statement.writeField(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_COUPLE_AVEC_ENFANT, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondCoupleEnfant, "montantPlafondCoupleEnfant"));

        statement.writeField(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_ENFANTS_ENFANTS, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondEnfantsEnfants, "montantPlafondEnfantsEnfants"));
        statement.writeField(RFPotAssure.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_ENFANTS_ENFANTS, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondPensionnaireEnfantsEnfants,
                "montantPlafondPensionnaireEnfantsEnfants"));

        // statement.writeField(RFPotAssure.FIELDNAME_FORCER_PAYEMENT, this._dbWriteBoolean(statement.getTransaction(),
        // this.forcerPayement, BConstants.DB_TYPE_BOOLEAN_CHAR, "forcerPayement"));
        //
        // statement.writeField(RFPotAssure.FIELDNAME_IMPUTER_GRANDE_QD,
        // this._dbWriteBoolean(statement.getTransaction(),
        // this.imputerGrandeQd, BConstants.DB_TYPE_BOOLEAN_CHAR, "imputerGrandeQd"));
    }

    public String getIdPotAssure() {
        return idPotAssure;
    }

    public String getMontantPlafondAdulteEnfants() {
        return montantPlafondAdulteEnfants;
    }

    public String getMontantPlafondCoupleDomicile() {
        return montantPlafondCoupleDomicile;
    }

    public String getMontantPlafondCoupleEnfant() {
        return montantPlafondCoupleEnfant;
    }

    public String getMontantPlafondEnfantsEnfants() {
        return montantPlafondEnfantsEnfants;
    }

    public String getMontantPlafondEnfantsSepares() {
        return montantPlafondEnfantsSepares;
    }

    public String getMontantPlafondPensionnaireEnfantsEnfants() {
        return montantPlafondPensionnaireEnfantsEnfants;
    }

    public String getMontantPlafondPensionnaireEnfantsSepares() {
        return montantPlafondPensionnaireEnfantsSepares;
    }

    public String getMontantPlafondPensionnairePersonneSeule() {
        return montantPlafondPensionnairePersonneSeule;
    }

    public String getMontantPlafondPensionnairePourTous() {
        return montantPlafondPensionnairePourTous;
    }

    public String getMontantPlafondPensionnaireSepareDomicile() {
        return montantPlafondPensionnaireSepareDomicile;
    }

    public String getMontantPlafondPensionnaireSepareHome() {
        return montantPlafondPensionnaireSepareHome;
    }

    public String getMontantPlafondPersonneSeule() {
        return montantPlafondPersonneSeule;
    }

    public String getMontantPlafondPourTous() {
        return montantPlafondPourTous;
    }

    public String getMontantPlafondSepareDomicile() {
        return montantPlafondSepareDomicile;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setIdPotAssure(String idPotAssure) {
        this.idPotAssure = idPotAssure;
    }

    public void setMontantPlafondAdulteEnfants(String montantPlafondAdulteEnfants) {
        this.montantPlafondAdulteEnfants = montantPlafondAdulteEnfants;
    }

    public void setMontantPlafondCoupleDomicile(String montantPlafondCoupleDomicile) {
        this.montantPlafondCoupleDomicile = montantPlafondCoupleDomicile;
    }

    public void setMontantPlafondCoupleEnfant(String montantPlafondCoupleEnfant) {
        this.montantPlafondCoupleEnfant = montantPlafondCoupleEnfant;
    }

    public void setMontantPlafondEnfantsEnfants(String montantPlafondEnfantsEnfants) {
        this.montantPlafondEnfantsEnfants = montantPlafondEnfantsEnfants;
    }

    public void setMontantPlafondEnfantsSepares(String montantPlafondEnfantsSepares) {
        this.montantPlafondEnfantsSepares = montantPlafondEnfantsSepares;
    }

    public void setMontantPlafondPensionnaireEnfantsEnfants(String montantPlafondPensionnaireEnfantsEnfants) {
        this.montantPlafondPensionnaireEnfantsEnfants = montantPlafondPensionnaireEnfantsEnfants;
    }

    public void setMontantPlafondPensionnaireEnfantsSepares(String montantPlafondPensionnaireEnfantsSepares) {
        this.montantPlafondPensionnaireEnfantsSepares = montantPlafondPensionnaireEnfantsSepares;
    }

    public void setMontantPlafondPensionnairePersonneSeule(String montantPlafondPensionnairePersonneSeule) {
        this.montantPlafondPensionnairePersonneSeule = montantPlafondPensionnairePersonneSeule;
    }

    public void setMontantPlafondPensionnairePourTous(String montantPlafondPensionnairePourTous) {
        this.montantPlafondPensionnairePourTous = montantPlafondPensionnairePourTous;
    }

    public void setMontantPlafondPensionnaireSepareDomicile(String montantPlafondPensionnaireSepareDomicile) {
        this.montantPlafondPensionnaireSepareDomicile = montantPlafondPensionnaireSepareDomicile;
    }

    public void setMontantPlafondPensionnaireSepareHome(String montantPlafondPensionnaireSepareHome) {
        this.montantPlafondPensionnaireSepareHome = montantPlafondPensionnaireSepareHome;
    }

    public void setMontantPlafondPersonneSeule(String montantPlafondPersonneSeule) {
        this.montantPlafondPersonneSeule = montantPlafondPersonneSeule;
    }

    public void setMontantPlafondPourTous(String montantPlafondPourTous) {
        this.montantPlafondPourTous = montantPlafondPourTous;
    }

    public void setMontantPlafondSepareDomicile(String montantPlafondSepareDomicile) {
        this.montantPlafondSepareDomicile = montantPlafondSepareDomicile;
    }

}
