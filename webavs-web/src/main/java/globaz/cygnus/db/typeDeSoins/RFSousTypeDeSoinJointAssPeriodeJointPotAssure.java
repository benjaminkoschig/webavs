/*
 * Créé le 11 janvier 2009
 */
package globaz.cygnus.db.typeDeSoins;

import globaz.globall.db.BStatement;

/**
 * @author jje
 * 
 *         Edité par fha le 13/04/2010 : ajout du codeSousTypeSoin pour la gestion des conventions (ajout couple
 *         fournisseur-type de soin)
 */
public class RFSousTypeDeSoinJointAssPeriodeJointPotAssure extends RFSousTypeDeSoin {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTypeDeSoin.TABLE_NAME);

        // jointure entre la table des types de soin et la table des sous-types
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSousTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);

        // jointure entre la table des sous-types de soin et la table de liaison
        // sts/pot
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssTypeDeSoinPotAssure.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssTypeDeSoinPotAssure.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssTypeDeSoinPotAssure.FIELDNAME_ID_SOUS_TYPE_SOIN);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);

        // jointure entre la table de liaison sts/pot et la table potAssure
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPotAssure.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPotAssure.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPotAssure.FIELDNAME_ID_POT_ASSURE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssTypeDeSoinPotAssure.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssTypeDeSoinPotAssure.FIELDNAME_ID_POT_ASSURE);

        return fromClauseBuffer.toString();
    }

    private String dateDebut = "";
    private String dateFin = "";

    // private Boolean forcerPayement = Boolean.FALSE;
    private String idPotAssure = "";

    private String idSoinPot = "";
    private String montantPlafondAdulteEnfants = "";
    // private Boolean imputerGrandeQd = Boolean.TRUE;
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

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    public RFSousTypeDeSoinJointAssPeriodeJointPotAssure() {
        super();
    }

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return RFSousTypeDeSoin.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des sous types de soin
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        super._readProperties(statement);

        idPotAssure = statement.dbReadNumeric(RFAssTypeDeSoinPotAssure.FIELDNAME_ID_POT_ASSURE);
        idSoinPot = statement.dbReadNumeric(RFAssTypeDeSoinPotAssure.FIELDNAME_ID_SOIN_POT);

        dateDebut = statement.dbReadDateAMJ(RFAssTypeDeSoinPotAssure.FIELDNAME_DATE_DEBUT);
        dateFin = statement.dbReadDateAMJ(RFAssTypeDeSoinPotAssure.FIELDNAME_DATE_FIN);

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

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getIdPotAssure() {
        return idPotAssure;
    }

    public String getIdSoinPot() {
        return idSoinPot;
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

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setIdPotAssure(String idPotAssure) {
        this.idPotAssure = idPotAssure;
    }

    public void setIdSoinPot(String idSoinPot) {
        this.idSoinPot = idSoinPot;
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
