/*
 * Créé le 14 avril 2010
 */
package globaz.cygnus.db.attestations;

import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * author fha
 */
public class RFRegimeAlimentaire extends RFAttestation {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ACCIDENT = "EZBACC";
    public static final String FIELDNAME_AUTRE = "EZLAUT";
    public static final String FIELDNAME_COMMENTAIRE = "EZLCOM";
    public static final String FIELDNAME_DATE_AIDE_DUREE_DETERMINEE_DES_LE = "EZDADF";
    public static final String FIELDNAME_DATE_AIDE_DUREE_DETERMINEE_JUSQUA = "EZDADD";

    public static final String FIELDNAME_DATE_AIDE_DUREE_INDETERMINEE_DES_LE = "EZDAIF";
    public static final String FIELDNAME_DATE_AIDE_DUREE_INDETERMINEE_REEVALUATION = "EZDADI";
    public static final String FIELDNAME_DATE_DECISION = "EZDDEC";
    public static final String FIELDNAME_DATE_DECISION_REFUS = "EZDDER";
    public static final String FIELDNAME_DATE_ENVOI_EVALUATION_CMS = "EZDDED";
    public static final String FIELDNAME_DATE_ENVOI_INFOS_MEDECIN = "EZDEIM";
    public static final String FIELDNAME_DATE_RECEPTION = "EZDRED";
    public static final String FIELDNAME_DATE_RETOUR_EVALUATION_CMS = "EZDRCD";
    public static final String FIELDNAME_DATE_RETOUR_INFOS_MEDECIN = "EZDRIM";
    public static final String FIELDNAME_ECHEANCE_REVISION = "EZDERE";
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    public static final String FIELDNAME_ID_ATTESTATION_REGIME_ALIMENTAIRE = "EZIATT";
    public static final String FIELDNAME_ID_REGIME_ALIMENTAIRE = "EZIREA";
    public static final String FIELDNAME_INVALIDITE = "EZBINV";
    public static final String FIELDNAME_IS_REGIME_ACCEPTE = "EZBRAR";
    public static final String FIELDNAME_MALADIE = "EZBMAL";
    public static final String FIELDNAME_MONTANT_MENSUEL_ACCEPTE = "EZMMEA";
    public static final String FIELDNAME_TROUBLE_AGE = "EZBAGE";
    public static final String FIELDNAME_TYPE_REGIME = "EZLTRE";

    public static final String TABLE_NAME = "RFREGAL";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Boolean aAccident = Boolean.FALSE;
    private Boolean aInvalidite = Boolean.FALSE;
    private Boolean aMaladie = Boolean.FALSE;
    private Boolean aTroubleAge = Boolean.FALSE;
    private String autre = "";
    private String commentaire = "";
    private String dateAideDureeDetermineeDesLe = "";
    private String dateAideDureeDetermineeJusqua = "";
    private String dateAideDureeIndetermineeDesLe = "";
    private String dateAideDureeIndetermineeReevaluation = "";
    private String dateDecision = "";

    private String dateDecisionRefus = "";
    private String dateEnvoiEvaluationCMS = "";
    private String dateEnvoiInfosMedecin = "";
    private String dateReception = "";
    private String dateRetourEvaluationCMS = "";
    private String dateRetourInfosMedecin = "";
    private String echeanceRevision = "";
    private String idAttestationRegimeAlimentaire = "";
    private String idRegimeAlimentaire = "";
    private Boolean isRegimeAccepte = Boolean.FALSE;
    private String montantMensuelAccepte = "";

    private String typeRegime = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFRegimeAlimentaire
     */
    public RFRegimeAlimentaire() {
        super();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return faux
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * Méthode avant l'ajout l'incrémentation de la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        String getFrom = "";

        getFrom += _getCollection();
        getFrom += RFAttestation.TABLE_NAME;
        getFrom += " INNER JOIN ";
        getFrom += _getCollection();
        getFrom += RFRegimeAlimentaire.TABLE_NAME;
        getFrom += " ON ";
        getFrom += RFRegimeAlimentaire.FIELDNAME_ID_ATTESTATION_REGIME_ALIMENTAIRE;
        getFrom += "=";
        getFrom += RFAttestation.FIELDNAME_ID_ATTESTATION;

        return getFrom;
    }

    /**
     * getter pour le nom de la table des dossiers
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFRegimeAlimentaire.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idAttestationRegimeAlimentaire = statement
                .dbReadNumeric(RFRegimeAlimentaire.FIELDNAME_ID_ATTESTATION_REGIME_ALIMENTAIRE);
        idRegimeAlimentaire = statement.dbReadNumeric(RFRegimeAlimentaire.FIELDNAME_ID_REGIME_ALIMENTAIRE);
        aTroubleAge = statement.dbReadBoolean(RFRegimeAlimentaire.FIELDNAME_TROUBLE_AGE);
        aMaladie = statement.dbReadBoolean(RFRegimeAlimentaire.FIELDNAME_MALADIE);
        aAccident = statement.dbReadBoolean(RFRegimeAlimentaire.FIELDNAME_ACCIDENT);
        aInvalidite = statement.dbReadBoolean(RFRegimeAlimentaire.FIELDNAME_INVALIDITE);

        autre = statement.dbReadString(RFRegimeAlimentaire.FIELDNAME_AUTRE);
        dateAideDureeDetermineeDesLe = statement
                .dbReadDateAMJ(RFRegimeAlimentaire.FIELDNAME_DATE_AIDE_DUREE_DETERMINEE_DES_LE);
        dateAideDureeDetermineeJusqua = statement
                .dbReadDateAMJ(RFRegimeAlimentaire.FIELDNAME_DATE_AIDE_DUREE_DETERMINEE_JUSQUA);

        dateAideDureeIndetermineeDesLe = statement
                .dbReadDateAMJ(RFRegimeAlimentaire.FIELDNAME_DATE_AIDE_DUREE_INDETERMINEE_DES_LE);
        dateAideDureeIndetermineeReevaluation = statement
                .dbReadDateAMJ(RFRegimeAlimentaire.FIELDNAME_DATE_AIDE_DUREE_INDETERMINEE_REEVALUATION);

        dateReception = statement.dbReadDateAMJ(RFRegimeAlimentaire.FIELDNAME_DATE_RECEPTION);
        dateDecision = statement.dbReadDateAMJ(RFRegimeAlimentaire.FIELDNAME_DATE_DECISION);
        dateDecisionRefus = statement.dbReadDateAMJ(RFRegimeAlimentaire.FIELDNAME_DATE_DECISION_REFUS);
        dateEnvoiInfosMedecin = statement.dbReadDateAMJ(RFRegimeAlimentaire.FIELDNAME_DATE_ENVOI_INFOS_MEDECIN);
        dateRetourInfosMedecin = statement.dbReadDateAMJ(RFRegimeAlimentaire.FIELDNAME_DATE_RETOUR_INFOS_MEDECIN);
        dateEnvoiEvaluationCMS = statement.dbReadDateAMJ(RFRegimeAlimentaire.FIELDNAME_DATE_ENVOI_EVALUATION_CMS);
        dateRetourEvaluationCMS = statement.dbReadDateAMJ(RFRegimeAlimentaire.FIELDNAME_DATE_RETOUR_EVALUATION_CMS);
        typeRegime = statement.dbReadString(RFRegimeAlimentaire.FIELDNAME_TYPE_REGIME);
        montantMensuelAccepte = statement.dbReadNumeric(RFRegimeAlimentaire.FIELDNAME_MONTANT_MENSUEL_ACCEPTE);
        echeanceRevision = statement.dbReadDateAMJ(RFRegimeAlimentaire.FIELDNAME_ECHEANCE_REVISION);
        commentaire = statement.dbReadString(RFRegimeAlimentaire.FIELDNAME_COMMENTAIRE);

        isRegimeAccepte = statement.dbReadBoolean(RFRegimeAlimentaire.FIELDNAME_IS_REGIME_ACCEPTE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des demande maintien à dom.
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFRegimeAlimentaire.FIELDNAME_ID_ATTESTATION_REGIME_ALIMENTAIRE, this._dbWriteNumeric(
                statement.getTransaction(), idAttestationRegimeAlimentaire, "idAttestationRegimeAlimentaire"));

    }

    /**
     * Méthode d'écriture des champs dans la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RFRegimeAlimentaire.FIELDNAME_ID_ATTESTATION_REGIME_ALIMENTAIRE, this._dbWriteNumeric(
                statement.getTransaction(), idAttestationRegimeAlimentaire, "idAttestationRegimeAlimentaire"));
        statement.writeField(RFRegimeAlimentaire.FIELDNAME_ID_REGIME_ALIMENTAIRE,
                this._dbWriteNumeric(statement.getTransaction(), idRegimeAlimentaire, "idRegimeAlimentaire"));
        statement.writeField(RFRegimeAlimentaire.FIELDNAME_TROUBLE_AGE, this._dbWriteBoolean(
                statement.getTransaction(), aTroubleAge, BConstants.DB_TYPE_BOOLEAN_CHAR, "aTroubleAge"));
        statement
                .writeField(RFRegimeAlimentaire.FIELDNAME_MALADIE, this._dbWriteBoolean(statement.getTransaction(),
                        aMaladie, BConstants.DB_TYPE_BOOLEAN_CHAR, "aMaladie"));
        statement.writeField(RFRegimeAlimentaire.FIELDNAME_ACCIDENT, this._dbWriteBoolean(statement.getTransaction(),
                aAccident, BConstants.DB_TYPE_BOOLEAN_CHAR, "aAccident"));
        statement.writeField(RFRegimeAlimentaire.FIELDNAME_INVALIDITE, this._dbWriteBoolean(statement.getTransaction(),
                aInvalidite, BConstants.DB_TYPE_BOOLEAN_CHAR, "aInvalidite"));
        statement.writeField(RFRegimeAlimentaire.FIELDNAME_AUTRE,
                this._dbWriteString(statement.getTransaction(), autre, "autre"));

        statement.writeField(RFRegimeAlimentaire.FIELDNAME_DATE_AIDE_DUREE_DETERMINEE_DES_LE, this._dbWriteDateAMJ(
                statement.getTransaction(), dateAideDureeDetermineeDesLe, "dateAideDureeDetermineeDesLe"));
        statement.writeField(RFRegimeAlimentaire.FIELDNAME_DATE_AIDE_DUREE_DETERMINEE_JUSQUA, this._dbWriteDateAMJ(
                statement.getTransaction(), dateAideDureeDetermineeJusqua, "dateAideDureeDetermineeJusqua"));
        statement.writeField(RFRegimeAlimentaire.FIELDNAME_DATE_AIDE_DUREE_INDETERMINEE_DES_LE, this._dbWriteDateAMJ(
                statement.getTransaction(), dateAideDureeIndetermineeDesLe, "dateAideDureeIndetermineeDesLe"));
        statement.writeField(RFRegimeAlimentaire.FIELDNAME_DATE_AIDE_DUREE_INDETERMINEE_REEVALUATION, this
                ._dbWriteDateAMJ(statement.getTransaction(), dateAideDureeIndetermineeReevaluation,
                        "dateAideDureeIndetermineeReevaluation"));

        statement.writeField(RFRegimeAlimentaire.FIELDNAME_MONTANT_MENSUEL_ACCEPTE,
                this._dbWriteNumeric(statement.getTransaction(), montantMensuelAccepte, "montantMensuelAccepte"));

        statement.writeField(RFRegimeAlimentaire.FIELDNAME_TYPE_REGIME,
                this._dbWriteString(statement.getTransaction(), typeRegime, "typeRegime"));
        statement.writeField(RFRegimeAlimentaire.FIELDNAME_COMMENTAIRE,
                this._dbWriteString(statement.getTransaction(), commentaire, "commentaire"));

        statement.writeField(RFRegimeAlimentaire.FIELDNAME_IS_REGIME_ACCEPTE, this._dbWriteBoolean(
                statement.getTransaction(), isRegimeAccepte, BConstants.DB_TYPE_BOOLEAN_CHAR, "isRegimeAccepte"));

        statement.writeField(RFRegimeAlimentaire.FIELDNAME_DATE_RECEPTION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateReception, "dateReception"));
        statement.writeField(RFRegimeAlimentaire.FIELDNAME_DATE_DECISION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDecision, "dateDecision"));
        statement.writeField(RFRegimeAlimentaire.FIELDNAME_DATE_DECISION_REFUS,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDecisionRefus, "dateDecisionRefus"));
        statement.writeField(RFRegimeAlimentaire.FIELDNAME_DATE_ENVOI_INFOS_MEDECIN,
                this._dbWriteDateAMJ(statement.getTransaction(), dateEnvoiInfosMedecin, "dateEnvoiInfosMedecin"));
        statement.writeField(RFRegimeAlimentaire.FIELDNAME_DATE_RETOUR_INFOS_MEDECIN,
                this._dbWriteDateAMJ(statement.getTransaction(), dateRetourInfosMedecin, "dateRetourInfosMedecin"));
        statement.writeField(RFRegimeAlimentaire.FIELDNAME_DATE_ENVOI_EVALUATION_CMS,
                this._dbWriteDateAMJ(statement.getTransaction(), dateEnvoiEvaluationCMS, "dateEnvoiEvaluationCMS"));
        statement.writeField(RFRegimeAlimentaire.FIELDNAME_DATE_RETOUR_EVALUATION_CMS,
                this._dbWriteDateAMJ(statement.getTransaction(), dateRetourEvaluationCMS, "dateRetourEvaluationCMS"));
        statement.writeField(RFRegimeAlimentaire.FIELDNAME_ECHEANCE_REVISION,
                this._dbWriteDateAMJ(statement.getTransaction(), echeanceRevision, "echeanceRevision"));

    }

    public Boolean getaAccident() {
        return aAccident;
    }

    public Boolean getaInvalidite() {
        return aInvalidite;
    }

    public Boolean getaMaladie() {
        return aMaladie;
    }

    public Boolean getaTroubleAge() {
        return aTroubleAge;
    }

    public String getAutre() {
        return autre;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public String getDateAideDureeDetermineeDesLe() {
        return dateAideDureeDetermineeDesLe;
    }

    public String getDateAideDureeDetermineeJusqua() {
        return dateAideDureeDetermineeJusqua;
    }

    public String getDateAideDureeIndetermineeDesLe() {
        return dateAideDureeIndetermineeDesLe;
    }

    public String getDateAideDureeIndetermineeReevaluation() {
        return dateAideDureeIndetermineeReevaluation;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public String getDateDecisionRefus() {
        return dateDecisionRefus;
    }

    public String getDateEnvoiEvaluationCMS() {
        return dateEnvoiEvaluationCMS;
    }

    public String getDateEnvoiInfosMedecin() {
        return dateEnvoiInfosMedecin;
    }

    public String getDateReception() {
        return dateReception;
    }

    public String getDateRetourEvaluationCMS() {
        return dateRetourEvaluationCMS;
    }

    public String getDateRetourInfosMedecin() {
        return dateRetourInfosMedecin;
    }

    public String getEcheanceRevision() {
        return echeanceRevision;
    }

    public String getIdAttestationRegimeAlimentaire() {
        return idAttestationRegimeAlimentaire;
    }

    public String getIdRegimeAlimentaire() {
        return idRegimeAlimentaire;
    }

    public Boolean getIsRegimeAccepte() {
        return isRegimeAccepte;
    }

    public String getMontantMensuelAccepte() {
        return montantMensuelAccepte;
    }

    public String getTypeRegime() {
        return typeRegime;
    }

    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setaAccident(Boolean aAccident) {
        this.aAccident = aAccident;
    }

    public void setaInvalidite(Boolean aInvalidite) {
        this.aInvalidite = aInvalidite;
    }

    public void setaMaladie(Boolean aMaladie) {
        this.aMaladie = aMaladie;
    }

    public void setaTroubleAge(Boolean aTroubleAge) {
        this.aTroubleAge = aTroubleAge;
    }

    public void setAutre(String autre) {
        this.autre = autre;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public void setDateAideDureeDetermineeDesLe(String dateAideDureeDetermineeDesLe) {
        this.dateAideDureeDetermineeDesLe = dateAideDureeDetermineeDesLe;
    }

    public void setDateAideDureeDetermineeJusqua(String dateAideDureeDetermineeJusqua) {
        this.dateAideDureeDetermineeJusqua = dateAideDureeDetermineeJusqua;
    }

    public void setDateAideDureeIndetermineeDesLe(String dateAideDureeIndetermineeDesLe) {
        this.dateAideDureeIndetermineeDesLe = dateAideDureeIndetermineeDesLe;
    }

    public void setDateAideDureeIndetermineeReevaluation(String dateAideDureeIndetermineeReevaluation) {
        this.dateAideDureeIndetermineeReevaluation = dateAideDureeIndetermineeReevaluation;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public void setDateDecisionRefus(String dateDecisionRefus) {
        this.dateDecisionRefus = dateDecisionRefus;
    }

    public void setDateEnvoiEvaluationCMS(String dateEnvoiEvaluationCMS) {
        this.dateEnvoiEvaluationCMS = dateEnvoiEvaluationCMS;
    }

    public void setDateEnvoiInfosMedecin(String dateEnvoiInfosMedecin) {
        this.dateEnvoiInfosMedecin = dateEnvoiInfosMedecin;
    }

    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    public void setDateRetourEvaluationCMS(String dateRetourEvaluationCMS) {
        this.dateRetourEvaluationCMS = dateRetourEvaluationCMS;
    }

    public void setDateRetourInfosMedecin(String dateRetourInfosMedecin) {
        this.dateRetourInfosMedecin = dateRetourInfosMedecin;
    }

    public void setEcheanceRevision(String echeanceRevision) {
        this.echeanceRevision = echeanceRevision;
    }

    public void setIdAttestationRegimeAlimentaire(String idAttestationRegimeAlimentaire) {
        this.idAttestationRegimeAlimentaire = idAttestationRegimeAlimentaire;
    }

    public void setIdRegimeAlimentaire(String idRegimeAlimentaire) {
        this.idRegimeAlimentaire = idRegimeAlimentaire;
    }

    public void setIsRegimeAccepte(Boolean isRegimeAccepte) {
        this.isRegimeAccepte = isRegimeAccepte;
    }

    public void setMontantMensuelAccepte(String montantMensuelAccepte) {
        this.montantMensuelAccepte = montantMensuelAccepte;
    }

    public void setTypeRegime(String typeRegime) {
        this.typeRegime = typeRegime;
    }

}
