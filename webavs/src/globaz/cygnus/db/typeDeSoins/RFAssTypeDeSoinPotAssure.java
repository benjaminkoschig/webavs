/*
 * Créé le 17 décembre 2009
 */
package globaz.cygnus.db.typeDeSoins;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * author fha
 */
public class RFAssTypeDeSoinPotAssure extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_DEBUT = "FHDDEP";
    public static final String FIELDNAME_DATE_FIN = "FHDFIP";
    public static final String FIELDNAME_ID_POT_ASSURE = "FHIPOA";

    public static final String FIELDNAME_ID_SOIN_POT = "FHIASP";
    public static final String FIELDNAME_ID_SOUS_TYPE_SOIN = "FHISTS";

    public static final String TABLE_NAME = "RFASTPO";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateDebut = "";
    private String dateFin = "";
    private Boolean forcerPayement = Boolean.FALSE;
    private String idPotAssure = "";
    private String idSoinPot = "";

    private String idSousTypeSoin = "";
    private String montantPlafondAutresEnfants = "";
    private String montantPlafondCoupleDomicile = "";
    private String montantPlafondCouplesEnfants = "";

    private String montantPlafondEnfants2023 = "";
    private String montantPlafondEnfantsSepares = "";
    private String montantPlafondOrphelins = "";
    private String montantPlafondPersonneSeule = "";
    private String montantPlafondSepareDomicile = "";

    private String montantPlafondSepareHome = "";

    private String montantPourTous = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFSousTypeSoin
     */
    public RFAssTypeDeSoinPotAssure() {
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
        setIdSoinPot(_incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table des dossiers
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des sous type de soins
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idSousTypeSoin = statement.dbReadNumeric(FIELDNAME_ID_SOUS_TYPE_SOIN);
        idSoinPot = statement.dbReadNumeric(FIELDNAME_ID_SOIN_POT);
        idPotAssure = statement.dbReadNumeric(FIELDNAME_ID_POT_ASSURE);
        dateDebut = statement.dbReadDateAMJ(FIELDNAME_DATE_DEBUT);
        dateFin = statement.dbReadDateAMJ(FIELDNAME_DATE_FIN);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des sous type de soins
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_SOIN_POT, _dbWriteNumeric(statement.getTransaction(), idSoinPot, "idSoinPot"));
    }

    /**
     * Méthode d'écriture des champs dans la table des sous type de soins
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(FIELDNAME_ID_SOUS_TYPE_SOIN,
                _dbWriteNumeric(statement.getTransaction(), idSousTypeSoin, "idSousTypeSoin"));
        statement
                .writeField(FIELDNAME_ID_SOIN_POT, _dbWriteNumeric(statement.getTransaction(), idSoinPot, "idSoinPot"));
        statement.writeField(FIELDNAME_ID_POT_ASSURE,
                _dbWriteNumeric(statement.getTransaction(), idPotAssure, "idPotAssure"));
        // _dbWriteDateMonth
        statement.writeField(FIELDNAME_DATE_DEBUT, _dbWriteDateAMJ(statement.getTransaction(), dateDebut, "dateDebut"));
        statement.writeField(FIELDNAME_DATE_FIN, _dbWriteDateAMJ(statement.getTransaction(), dateFin, "dateFin"));

    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public Boolean getForcerPayement() {
        return forcerPayement;
    }

    public String getIdPotAssure() {
        return idPotAssure;
    }

    public String getIdSoinPot() {
        return idSoinPot;
    }

    public String getIdSousTypeSoin() {
        return idSousTypeSoin;
    }

    public String getMontantPlafondAutresEnfants() {
        return montantPlafondAutresEnfants;
    }

    public String getMontantPlafondCoupleDomicile() {
        return montantPlafondCoupleDomicile;
    }

    public String getMontantPlafondCouplesEnfants() {
        return montantPlafondCouplesEnfants;
    }

    public String getMontantPlafondEnfants2023() {
        return montantPlafondEnfants2023;
    }

    public String getMontantPlafondEnfantsSepares() {
        return montantPlafondEnfantsSepares;
    }

    public String getMontantPlafondOrphelins() {
        return montantPlafondOrphelins;
    }

    public String getMontantPlafondPersonneSeule() {
        return montantPlafondPersonneSeule;
    }

    public String getMontantPlafondSepareDomicile() {
        return montantPlafondSepareDomicile;
    }

    public String getMontantPlafondSepareHome() {
        return montantPlafondSepareHome;
    }

    public String getMontantPourTous() {
        return montantPourTous;
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

    public void setForcerPayement(Boolean forcerPayement) {
        this.forcerPayement = forcerPayement;
    }

    public void setIdPotAssure(String idPotAssure) {
        this.idPotAssure = idPotAssure;
    }

    public void setIdSoinPot(String idSoinPot) {
        this.idSoinPot = idSoinPot;
    }

    public void setIdSousTypeSoin(String idSousTypeSoin) {
        this.idSousTypeSoin = idSousTypeSoin;
    }

    public void setMontantPlafondAutresEnfants(String montantPlafondAutresEnfants) {
        this.montantPlafondAutresEnfants = montantPlafondAutresEnfants;
    }

    public void setMontantPlafondCoupleDomicile(String montantPlafondCoupleDomicile) {
        this.montantPlafondCoupleDomicile = montantPlafondCoupleDomicile;
    }

    public void setMontantPlafondCouplesEnfants(String montantPlafondCouplesEnfants) {
        this.montantPlafondCouplesEnfants = montantPlafondCouplesEnfants;
    }

    public void setMontantPlafondEnfants2023(String montantPlafondEnfants2023) {
        this.montantPlafondEnfants2023 = montantPlafondEnfants2023;
    }

    public void setMontantPlafondEnfantsSepares(String montantPlafondEnfantsSepares) {
        this.montantPlafondEnfantsSepares = montantPlafondEnfantsSepares;
    }

    public void setMontantPlafondOrphelins(String montantPlafondOrphelins) {
        this.montantPlafondOrphelins = montantPlafondOrphelins;
    }

    public void setMontantPlafondPersonneSeule(String montantPlafondPersonneSeule) {
        this.montantPlafondPersonneSeule = montantPlafondPersonneSeule;
    }

    public void setMontantPlafondSepareDomicile(String montantPlafondSepareDomicile) {
        this.montantPlafondSepareDomicile = montantPlafondSepareDomicile;
    }

    public void setMontantPlafondSepareHome(String montantPlafondSepareHome) {
        this.montantPlafondSepareHome = montantPlafondSepareHome;
    }

    public void setMontantPourTous(String montantPourTous) {
        this.montantPourTous = montantPourTous;
    }

}
