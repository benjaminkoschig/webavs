/*
 * Créé le 4 mars 2011
 */
package globaz.cygnus.db.adaptationsJournalieres;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * author jje
 */
public class RFAdaptationJournaliere extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CS_ETAT = "FUTETA";
    public static final String FIELDNAME_DATE_DE_TRAITEMENT = "FUDTRA";
    public static final String FIELDNAME_DATE_DEBUT = "FUDDEB";
    public static final String FIELDNAME_DATE_FIN = "FUDFIN";
    public static final String FIELDNAME_ID_ADAPTATION_JOUNRALIERE = "FUIADJ";
    public static final String FIELDNAME_ID_DECISION_PC = "FUIDEC";
    public static final String FIELDNAME_ID_GESTIONNAIRE = "FUIGES";
    public static final String FIELDNAME_ID_TIERS_BENEFICIAIRE = "FUITIB";
    public static final String FIELDNAME_NSS_TIERS_BENEFICIAIRE = "FUINSS";
    public static final String FIELDNAME_NUMERO_DECISION_PC = "FUINDE";
    public static final String FIELDNAME_TYPE_DECISION_PC = "FUTDEC";

    public static final String TABLE_NAME = "RFADJOU";

    private String dateDeDebut = "";
    private String dateDeFin = "";
    private String dateDeTraitement = "";
    private String etat = "";
    private String idAdaptationJournaliere = "";
    private String idDecisionPc = "";
    private String idGestionnaire = "";
    private String idTiersBeneficiaire = "";
    private String nss = "";
    private String numeroDecisionPc = "";
    private String typeDeDecisionPc = "";

    /**
     * Crée une nouvelle instance de la classe RFAdaptationJournaliere.
     */
    public RFAdaptationJournaliere() {
        super();
    }

    /**
     * Méthode avant l'ajout l'incrémentation de la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdAdaptationJournaliere(this._incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table des adaptations journalières
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFAdaptationJournaliere.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des adaptations journalières
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idAdaptationJournaliere = statement.dbReadNumeric(RFAdaptationJournaliere.FIELDNAME_ID_ADAPTATION_JOUNRALIERE);
        dateDeDebut = statement.dbReadDateAMJ(RFAdaptationJournaliere.FIELDNAME_DATE_DEBUT);
        dateDeFin = statement.dbReadDateAMJ(RFAdaptationJournaliere.FIELDNAME_DATE_FIN);
        dateDeTraitement = statement.dbReadDateAMJ(RFAdaptationJournaliere.FIELDNAME_DATE_DE_TRAITEMENT);
        idTiersBeneficiaire = statement.dbReadNumeric(RFAdaptationJournaliere.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        etat = statement.dbReadNumeric(RFAdaptationJournaliere.FIELDNAME_CS_ETAT);
        idDecisionPc = statement.dbReadNumeric(RFAdaptationJournaliere.FIELDNAME_ID_DECISION_PC);
        numeroDecisionPc = statement.dbReadString(RFAdaptationJournaliere.FIELDNAME_NUMERO_DECISION_PC);
        typeDeDecisionPc = statement.dbReadNumeric(RFAdaptationJournaliere.FIELDNAME_TYPE_DECISION_PC);
        idGestionnaire = statement.dbReadString(RFAdaptationJournaliere.FIELDNAME_ID_GESTIONNAIRE);
        nss = statement.dbReadString(RFAdaptationJournaliere.FIELDNAME_NSS_TIERS_BENEFICIAIRE);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des adaptations journalières
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFAdaptationJournaliere.FIELDNAME_ID_ADAPTATION_JOUNRALIERE,
                this._dbWriteNumeric(statement.getTransaction(), idAdaptationJournaliere, "idAdaptationJournaliere"));

    }

    /**
     * Méthode d'écriture des champs dans la table des adaptations journalières
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(RFAdaptationJournaliere.FIELDNAME_ID_ADAPTATION_JOUNRALIERE,
                this._dbWriteNumeric(statement.getTransaction(), idAdaptationJournaliere, "idAdaptationJournaliere"));
        statement.writeField(RFAdaptationJournaliere.FIELDNAME_DATE_DEBUT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDeDebut, "dateDeDebut"));
        statement.writeField(RFAdaptationJournaliere.FIELDNAME_DATE_FIN,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDeFin, "dateDeFin"));
        statement.writeField(RFAdaptationJournaliere.FIELDNAME_DATE_DE_TRAITEMENT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDeTraitement, "dateDeTraitement"));
        statement.writeField(RFAdaptationJournaliere.FIELDNAME_ID_TIERS_BENEFICIAIRE,
                this._dbWriteNumeric(statement.getTransaction(), idTiersBeneficiaire, "idTiersBeneficiaire"));
        statement.writeField(RFAdaptationJournaliere.FIELDNAME_ID_DECISION_PC,
                this._dbWriteNumeric(statement.getTransaction(), idDecisionPc, "idDecisionPc"));
        statement.writeField(RFAdaptationJournaliere.FIELDNAME_NUMERO_DECISION_PC,
                this._dbWriteString(statement.getTransaction(), numeroDecisionPc, "numeroDecisionPc"));
        statement.writeField(RFAdaptationJournaliere.FIELDNAME_TYPE_DECISION_PC,
                this._dbWriteNumeric(statement.getTransaction(), typeDeDecisionPc, "typeDeDecisionPc"));
        statement.writeField(RFAdaptationJournaliere.FIELDNAME_ID_GESTIONNAIRE,
                this._dbWriteString(statement.getTransaction(), idGestionnaire, "idGestionnaire"));
        statement.writeField(RFAdaptationJournaliere.FIELDNAME_CS_ETAT,
                this._dbWriteNumeric(statement.getTransaction(), etat, "etat"));
        statement.writeField(RFAdaptationJournaliere.FIELDNAME_NSS_TIERS_BENEFICIAIRE,
                this._dbWriteString(statement.getTransaction(), nss, "nss"));

    }

    public String getDateDeDebut() {
        return dateDeDebut;
    }

    public String getDateDeFin() {
        return dateDeFin;
    }

    public String getDateDeTraitement() {
        return dateDeTraitement;
    }

    public String getEtat() {
        return etat;
    }

    public String getIdAdaptationJournaliere() {
        return idAdaptationJournaliere;
    }

    public String getIdDecisionPc() {
        return idDecisionPc;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public String getNss() {
        return nss;
    }

    public String getNumeroDecisionPc() {
        return numeroDecisionPc;
    }

    public String getTypeDeDecisionPc() {
        return typeDeDecisionPc;
    }

    public void setDateDeDebut(String dateDeDebut) {
        this.dateDeDebut = dateDeDebut;
    }

    public void setDateDeFin(String dateDeFin) {
        this.dateDeFin = dateDeFin;
    }

    public void setDateDeTraitement(String dateDeTraitement) {
        this.dateDeTraitement = dateDeTraitement;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setIdAdaptationJournaliere(String idAdaptationJournaliere) {
        this.idAdaptationJournaliere = idAdaptationJournaliere;
    }

    public void setIdDecisionPc(String idDecisionPc) {
        this.idDecisionPc = idDecisionPc;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setNumeroDecisionPc(String numeroDecisionPc) {
        this.numeroDecisionPc = numeroDecisionPc;
    }

    public void setTypeDeDecisionPc(String typeDeDecisionPc) {
        this.typeDeDecisionPc = typeDeDecisionPc;
    }

}
