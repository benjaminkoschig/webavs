package globaz.cygnus.db.pots;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * author fha
 * 
 * @revision JJE 25.08.2011
 */
public class RFPotsPC extends BEntity {
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_DEBUT = "FIDDEP";
    public static final String FIELDNAME_DATE_FIN = "FIDFIN";
    public static final String FIELDNAME_ID_POT_PC = "FIIPOP";

    // Personnes avec enfants à domicile
    public static final String FIELDNAME_MONTANT_PLAFOND_ADULTE_ENFANTS = "FIMAEN";

    // Couple à domicile
    public static final String FIELDNAME_MONTANT_PLAFOND_COUPLE_AVEC_ENFANTS = "FIMCAE";

    // Couple à domicile
    public static final String FIELDNAME_MONTANT_PLAFOND_COUPLE_DOMICILE = "FIMPCD";

    // Enfants - enfants à domicile
    public static final String FIELDNAME_MONTANT_PLAFOND_ENFANTS_ENFANTS = "FIMEEN";

    // Enfant séparé domicile
    public static final String FIELDNAME_MONTANT_PLAFOND_ENFANTS_SEPARES = "FIMPES";

    // Enfants - enfants pensionnaire
    public static final String FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_ENFANTS_ENFANTS = "FIMPEE";

    // Enfant séparé pensionnaire
    public static final String FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_ENFANTS_SEPARES = "FIMPEP";
    // Personne seule pensionnaire
    public static final String FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_PERSONNE_SEULE = "FIMPPP";

    // Montant pour tous pensionnaire
    public static final String FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_POUR_TOUS = "FIMPTP";

    // Couple séparé home / domicile -> home
    public static final String FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_SEPARE_DOMICILE = "FIMCDP";

    // Couple séparé les deux dans un home
    public static final String FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_SEPARE_HOME = "FIMCSP";

    // Personne seule à domicile
    public static final String FIELDNAME_MONTANT_PLAFOND_PERSONNE_SEULE = "FIMPPS";

    // Montant pour tous domicile
    public static final String FIELDNAME_MONTANT_PLAFOND_POUR_TOUS = "FIMPTS";

    // Couple séparé home / domicile -> domicile
    public static final String FIELDNAME_MONTANT_PLAFOND_SEPARE_DOMICILE = "FIMCSD";

    public static final String TABLE_NAME = "RFPOTPC";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateDebut = "";
    private String dateFin = "";
    private String idPotPC = "";

    private String montantPlafondAdulteEnfants = "";
    private String montantPlafondCoupleDomicile = "";

    private String montantPlafondCoupleEnfants = "";
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
     * Crée une nouvelle instance de la classe RFPotsPC.
     */
    public RFPotsPC() {
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
        setIdPotPC(this._incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table des types de soin
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFPotsPC.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des types de soin
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idPotPC = statement.dbReadNumeric(RFPotsPC.FIELDNAME_ID_POT_PC);

        montantPlafondCoupleDomicile = statement.dbReadNumeric(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_COUPLE_DOMICILE);
        montantPlafondPersonneSeule = statement.dbReadNumeric(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_PERSONNE_SEULE);
        montantPlafondSepareDomicile = statement.dbReadNumeric(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_SEPARE_DOMICILE);
        montantPlafondEnfantsSepares = statement.dbReadNumeric(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_ENFANTS_SEPARES);
        montantPlafondPourTous = statement.dbReadNumeric(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_POUR_TOUS);
        montantPlafondPensionnairePersonneSeule = statement
                .dbReadNumeric(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_PERSONNE_SEULE);
        montantPlafondPensionnaireSepareDomicile = statement
                .dbReadNumeric(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_SEPARE_DOMICILE);
        montantPlafondPensionnaireSepareHome = statement
                .dbReadNumeric(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_SEPARE_HOME);
        montantPlafondPensionnaireEnfantsSepares = statement
                .dbReadNumeric(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_ENFANTS_SEPARES);
        montantPlafondPensionnairePourTous = statement
                .dbReadNumeric(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_POUR_TOUS);
        montantPlafondPensionnaireEnfantsEnfants = statement
                .dbReadNumeric(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_ENFANTS_ENFANTS);
        montantPlafondEnfantsEnfants = statement.dbReadNumeric(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_ENFANTS_ENFANTS);
        montantPlafondAdulteEnfants = statement.dbReadNumeric(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_ADULTE_ENFANTS);
        montantPlafondCoupleEnfants = statement.dbReadNumeric(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_COUPLE_AVEC_ENFANTS);
        dateDebut = statement.dbReadDateAMJ(RFPotsPC.FIELDNAME_DATE_DEBUT);
        dateFin = statement.dbReadDateAMJ(RFPotsPC.FIELDNAME_DATE_FIN);

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
        statement.writeKey(RFPotsPC.FIELDNAME_ID_POT_PC,
                this._dbWriteNumeric(statement.getTransaction(), idPotPC, "idPotPC"));
    }

    /**
     * Méthode d'écriture des champs dans la table des types de soin
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RFPotsPC.FIELDNAME_ID_POT_PC,
                this._dbWriteNumeric(statement.getTransaction(), idPotPC, "idPotPC"));

        statement.writeField(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_COUPLE_DOMICILE, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondCoupleDomicile, "montantPlafondCoupleDomicile"));
        statement.writeField(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_PERSONNE_SEULE, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondPersonneSeule, "montantPlafondPersonneSeule"));
        statement.writeField(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_SEPARE_DOMICILE, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondSepareDomicile, "montantPlafondSepareDomicile"));
        statement.writeField(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_PERSONNE_SEULE, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondPensionnairePersonneSeule,
                "montantPlafondPensionnairePersonneSeule"));
        statement.writeField(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_SEPARE_DOMICILE, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondPensionnaireSepareDomicile,
                "montantPlafondPensionnaireSepareDomicile"));
        statement.writeField(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_SEPARE_HOME, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondPensionnaireSepareHome,
                "montantPlafondPensionnaireSepareHome"));
        statement.writeField(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_ENFANTS_SEPARES, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondPensionnaireEnfantsSepares,
                "montantPlafondPensionnaireEnfantsSepares"));
        statement.writeField(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_ENFANTS_ENFANTS, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondEnfantsEnfants, "montantPlafondEnfantsEnfants"));
        statement.writeField(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_ENFANTS_SEPARES, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondEnfantsSepares, "montantPlafondEnfantsSepares"));
        statement.writeField(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_ENFANTS_ENFANTS, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondPensionnaireEnfantsEnfants,
                "montantPlafondPensionnaireEnfantsEnfants"));
        statement.writeField(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_PENSIONNAIRE_POUR_TOUS, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondPensionnairePourTous, "montantPlafondPensionnairePourTous"));
        statement.writeField(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_ADULTE_ENFANTS, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondAdulteEnfants, "montantPlafondAdulteEnfants"));
        statement.writeField(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_COUPLE_AVEC_ENFANTS, this._dbWriteNumeric(
                statement.getTransaction(), montantPlafondCoupleEnfants, "montantPlafondCoupleEnfants"));
        statement.writeField(RFPotsPC.FIELDNAME_MONTANT_PLAFOND_POUR_TOUS,
                this._dbWriteNumeric(statement.getTransaction(), montantPlafondPourTous, "montantPlafondPourTous"));
        statement.writeField(RFPotsPC.FIELDNAME_DATE_DEBUT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDebut, "dateDebut"));
        statement.writeField(RFPotsPC.FIELDNAME_DATE_FIN,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFin, "dateFin"));
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getIdPotPC() {
        return idPotPC;
    }

    public String getMontantPlafondAdulteEnfants() {
        return montantPlafondAdulteEnfants;
    }

    public String getMontantPlafondCoupleDomicile() {
        return montantPlafondCoupleDomicile;
    }

    public String getMontantPlafondCoupleEnfants() {
        return montantPlafondCoupleEnfants;
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

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setIdPotPC(String idPotPC) {
        this.idPotPC = idPotPC;
    }

    public void setMontantPlafondAdulteEnfants(String montantPlafondAdulteEnfants) {
        this.montantPlafondAdulteEnfants = montantPlafondAdulteEnfants;
    }

    public void setMontantPlafondCoupleDomicile(String montantPlafondCoupleDomicile) {
        this.montantPlafondCoupleDomicile = montantPlafondCoupleDomicile;
    }

    public void setMontantPlafondCoupleEnfants(String montantPlafondCoupleEnfants) {
        this.montantPlafondCoupleEnfants = montantPlafondCoupleEnfants;
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
