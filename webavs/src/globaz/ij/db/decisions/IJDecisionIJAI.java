package globaz.ij.db.decisions;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author SCR
 */
public class IJDecisionIJAI extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_BENEFICIAIRE = "XALBEN";
    public static final String FIELDNAME_CS_CANTON_TAUX_IMPOSITION = "XATCTI";
    public static final String FIELDNAME_CS_ETAT_MISE_EN_GED = "XATGED";
    public static final String FIELDNAME_CS_ETAT_SEDEX = "XATSDX";
    public static final String FIELDNAME_CS_REVISION = "XATREV";
    public static final String FIELDNAME_DATE_DECISION = "XADDEC";
    public static final String FIELDNAME_DATE_ENVOI_MISE_EN_GED = "XADGED";
    public static final String FIELDNAME_DATE_ENVOI_SEDEX = "XADSDX";
    public static final String FIELDNAME_DATE_SUR_DOC = "XADDOC";
    public static final String FIELDNAME_EMAIL_ADR = "XALEMA";
    public static final String FIELDNAME_ID_DECISION = "XAIDEC";
    public static final String FIELDNAME_ID_DOCUMENT = "XAIDOC";
    public static final String FIELDNAME_ID_DOMAINE_ADRESSE_PAIEMENT_PERSONNALISEE = "XAIDOM";
    public static final String FIELDNAME_ID_PERS_REFERENCE = "XAIPRE";
    public static final String FIELDNAME_ID_PRONONCE = "XAIPRO";
    public static final String FIELDNAME_ID_TIERS_ADR_COURRIER = "XAITAC";
    public static final String FIELDNAME_ID_TIERS_ADR_PMT = "XAITAP";
    public static final String FIELDNAME_ID_TIERS_ADRESSE_PAIEMENT_PERSONNALISEE = "XAITPP";
    public static final String FIELDNAME_ID_TIERS_PRINCIPAL = "XAITPR";
    public static final String FIELDNAME_IS_DECISION_VALIDE = "XABVAL";
    public static final String FIELDNAME_IS_SEND_TO_GEND = "XABGED";
    public static final String FIELDNAME_NO_REV_A_GARANTIR = "XANREV";
    public static final String FIELDNAME_NUMERO_AFFILIE_ADRESSE_PAIEMENT_PERSONNALISEE = "XANAFF";
    public static final String FIELDNAME_PERSONNALISATION_ADRESSE_PAIEMENT = "XALPER";
    public static final String FIELDNAME_REMARQUES = "XALREM";
    public static final String FIELDNAME_TAUX_IMPOSITION = "XANTIM";

    public static final String TABLE_NAME = "IJDECIS";

    private String beneficiaire = "";
    private String csCantonTauxImposition = "";
    private String csEtatMiseEnGed = "";
    private String csEtatSEDEX = "";
    private String csRevision = "";
    private String dateDecision = "";
    private String dateEnvoiSedex = "";
    private String dateMiseEnGed = "";
    private String dateSurDocument = "";
    private String emailAdresse = "";
    private String idDecision = "";
    private String idDocument = "";
    private String idDomaineAdressePaiementPersonnalisee = "";
    private String idPersonneReference = "";
    private String idPrononce = "";
    private String idTiersAdrCourrier = "";
    private String idTiersAdressePaiementPersonnalisee = "";
    private String idTiersAdrPmt = "";
    private String idTiersPrincipal = "";
    private Boolean isDecisionValidee = null;
    private Boolean isSendToGed = null;
    private String noRevAGarantir = "";
    private String numeroAffilieAdressePaiementPersonnalisee = "";
    private String personnalisationAdressePaiement = "";
    private String remarques = "";
    private String tauxImposition = "";

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdDecision(this._incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        return IJDecisionIJAI.TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        beneficiaire = statement.dbReadString(IJDecisionIJAI.FIELDNAME_BENEFICIAIRE);
        csCantonTauxImposition = statement.dbReadNumeric(IJDecisionIJAI.FIELDNAME_CS_CANTON_TAUX_IMPOSITION);
        csEtatMiseEnGed = statement.dbReadNumeric(IJDecisionIJAI.FIELDNAME_CS_ETAT_MISE_EN_GED);
        csEtatSEDEX = statement.dbReadNumeric(IJDecisionIJAI.FIELDNAME_CS_ETAT_SEDEX);
        csRevision = statement.dbReadNumeric(IJDecisionIJAI.FIELDNAME_CS_REVISION);
        dateDecision = statement.dbReadDateAMJ(IJDecisionIJAI.FIELDNAME_DATE_DECISION);
        dateEnvoiSedex = statement.dbReadDateAMJ(IJDecisionIJAI.FIELDNAME_DATE_ENVOI_SEDEX);
        dateMiseEnGed = statement.dbReadDateAMJ(IJDecisionIJAI.FIELDNAME_DATE_ENVOI_MISE_EN_GED);
        dateSurDocument = statement.dbReadDateAMJ(IJDecisionIJAI.FIELDNAME_DATE_SUR_DOC);
        emailAdresse = statement.dbReadString(IJDecisionIJAI.FIELDNAME_EMAIL_ADR);
        idDecision = statement.dbReadNumeric(IJDecisionIJAI.FIELDNAME_ID_DECISION);
        idDocument = statement.dbReadNumeric(IJDecisionIJAI.FIELDNAME_ID_DOCUMENT);
        idDomaineAdressePaiementPersonnalisee = statement
                .dbReadNumeric(IJDecisionIJAI.FIELDNAME_ID_DOMAINE_ADRESSE_PAIEMENT_PERSONNALISEE);
        idPersonneReference = statement.dbReadString(IJDecisionIJAI.FIELDNAME_ID_PERS_REFERENCE);
        idPrononce = statement.dbReadNumeric(IJDecisionIJAI.FIELDNAME_ID_PRONONCE);
        idTiersAdrCourrier = statement.dbReadNumeric(IJDecisionIJAI.FIELDNAME_ID_TIERS_ADR_COURRIER);
        idTiersAdrPmt = statement.dbReadNumeric(IJDecisionIJAI.FIELDNAME_ID_TIERS_ADR_PMT);
        idTiersAdressePaiementPersonnalisee = statement
                .dbReadNumeric(IJDecisionIJAI.FIELDNAME_ID_TIERS_ADRESSE_PAIEMENT_PERSONNALISEE);
        idTiersPrincipal = statement.dbReadNumeric(IJDecisionIJAI.FIELDNAME_ID_TIERS_PRINCIPAL);
        isDecisionValidee = statement.dbReadBoolean(IJDecisionIJAI.FIELDNAME_IS_DECISION_VALIDE);
        isSendToGed = statement.dbReadBoolean(IJDecisionIJAI.FIELDNAME_IS_SEND_TO_GEND);
        noRevAGarantir = statement.dbReadNumeric(IJDecisionIJAI.FIELDNAME_NO_REV_A_GARANTIR);
        numeroAffilieAdressePaiementPersonnalisee = statement
                .dbReadString(IJDecisionIJAI.FIELDNAME_NUMERO_AFFILIE_ADRESSE_PAIEMENT_PERSONNALISEE);
        personnalisationAdressePaiement = statement
                .dbReadString(IJDecisionIJAI.FIELDNAME_PERSONNALISATION_ADRESSE_PAIEMENT);
        remarques = statement.dbReadString(IJDecisionIJAI.FIELDNAME_REMARQUES);
        tauxImposition = statement.dbReadNumeric(IJDecisionIJAI.FIELDNAME_TAUX_IMPOSITION);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(IJDecisionIJAI.FIELDNAME_ID_DECISION,
                this._dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(IJDecisionIJAI.FIELDNAME_BENEFICIAIRE,
                this._dbWriteString(statement.getTransaction(), beneficiaire, "beneficiaire"));
        statement.writeField(IJDecisionIJAI.FIELDNAME_CS_CANTON_TAUX_IMPOSITION,
                this._dbWriteNumeric(statement.getTransaction(), csCantonTauxImposition, "csCantonTauxImposition"));
        statement.writeField(IJDecisionIJAI.FIELDNAME_CS_ETAT_MISE_EN_GED,
                this._dbWriteNumeric(statement.getTransaction(), csEtatMiseEnGed, "csEtatMiseEnGed"));
        statement.writeField(IJDecisionIJAI.FIELDNAME_CS_ETAT_SEDEX,
                this._dbWriteNumeric(statement.getTransaction(), csEtatSEDEX, "csEtatSEDEX"));
        statement.writeField(IJDecisionIJAI.FIELDNAME_CS_REVISION,
                this._dbWriteNumeric(statement.getTransaction(), csRevision, "csRevision"));
        statement.writeField(IJDecisionIJAI.FIELDNAME_DATE_DECISION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDecision, "dateDecision"));
        statement.writeField(IJDecisionIJAI.FIELDNAME_DATE_ENVOI_MISE_EN_GED,
                this._dbWriteDateAMJ(statement.getTransaction(), dateMiseEnGed, "dateMiseEnGed"));
        statement.writeField(IJDecisionIJAI.FIELDNAME_DATE_ENVOI_SEDEX,
                this._dbWriteDateAMJ(statement.getTransaction(), dateEnvoiSedex, "dateEnvoiSedex"));
        statement.writeField(IJDecisionIJAI.FIELDNAME_DATE_SUR_DOC,
                this._dbWriteDateAMJ(statement.getTransaction(), dateSurDocument, "dateSurDocument"));
        statement.writeField(IJDecisionIJAI.FIELDNAME_EMAIL_ADR,
                this._dbWriteString(statement.getTransaction(), emailAdresse, "emailAdresse"));
        statement.writeField(IJDecisionIJAI.FIELDNAME_ID_DECISION,
                this._dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));
        statement.writeField(IJDecisionIJAI.FIELDNAME_ID_DOCUMENT,
                this._dbWriteNumeric(statement.getTransaction(), idDocument, "idDocument"));
        statement.writeField(IJDecisionIJAI.FIELDNAME_ID_DOMAINE_ADRESSE_PAIEMENT_PERSONNALISEE, this._dbWriteNumeric(
                statement.getTransaction(), idDomaineAdressePaiementPersonnalisee,
                "idDomaineAdressePaiementPersonnalisee"));
        statement.writeField(IJDecisionIJAI.FIELDNAME_ID_PERS_REFERENCE,
                this._dbWriteString(statement.getTransaction(), idPersonneReference, "idPersonneReference"));
        statement.writeField(IJDecisionIJAI.FIELDNAME_ID_PRONONCE,
                this._dbWriteNumeric(statement.getTransaction(), idPrononce, "idPrononce"));
        statement.writeField(IJDecisionIJAI.FIELDNAME_ID_TIERS_ADR_COURRIER,
                this._dbWriteNumeric(statement.getTransaction(), idTiersAdrCourrier, "idTiersAdrCourrier"));
        statement.writeField(IJDecisionIJAI.FIELDNAME_ID_TIERS_ADR_PMT,
                this._dbWriteNumeric(statement.getTransaction(), idTiersAdrPmt, "idTiersAdrPmt"));
        statement
                .writeField(IJDecisionIJAI.FIELDNAME_ID_TIERS_ADRESSE_PAIEMENT_PERSONNALISEE, this._dbWriteNumeric(
                        statement.getTransaction(), idTiersAdressePaiementPersonnalisee,
                        "idTiersAdressePaiementPersonnalisee"));
        statement.writeField(IJDecisionIJAI.FIELDNAME_ID_TIERS_PRINCIPAL,
                this._dbWriteNumeric(statement.getTransaction(), idTiersPrincipal, "idTiersPrincipal"));
        statement.writeField(IJDecisionIJAI.FIELDNAME_NO_REV_A_GARANTIR,
                this._dbWriteNumeric(statement.getTransaction(), noRevAGarantir, "noRevAGarantir"));
        statement.writeField(IJDecisionIJAI.FIELDNAME_NUMERO_AFFILIE_ADRESSE_PAIEMENT_PERSONNALISEE, this
                ._dbWriteString(statement.getTransaction(), numeroAffilieAdressePaiementPersonnalisee,
                        "numeroAffilieAdressePaiementPersonnalisee"));
        statement.writeField(IJDecisionIJAI.FIELDNAME_PERSONNALISATION_ADRESSE_PAIEMENT,
                this._dbWriteString(statement.getTransaction(), personnalisationAdressePaiement));
        statement.writeField(IJDecisionIJAI.FIELDNAME_REMARQUES,
                this._dbWriteString(statement.getTransaction(), remarques, "remarques"));
        statement.writeField(IJDecisionIJAI.FIELDNAME_TAUX_IMPOSITION,
                this._dbWriteNumeric(statement.getTransaction(), tauxImposition, "tauxImposition"));

        if (isDecisionValidee != null) {
            statement.writeField(IJDecisionIJAI.FIELDNAME_IS_DECISION_VALIDE,
                    this._dbWriteBoolean(statement.getTransaction(), isDecisionValidee,
                            BConstants.DB_TYPE_BOOLEAN_CHAR, "isDecisionValidee"));
        }

        if (isSendToGed != null) {
            statement.writeField(IJDecisionIJAI.FIELDNAME_IS_SEND_TO_GEND, this._dbWriteBoolean(
                    statement.getTransaction(), isSendToGed, BConstants.DB_TYPE_BOOLEAN_CHAR, "isSendToGed"));
        }
    }

    public String getBeneficiaire() {
        return beneficiaire;
    }

    public String getCsCantonTauxImposition() {
        return csCantonTauxImposition;
    }

    public String getCsEtatMiseEnGed() {
        return csEtatMiseEnGed;
    }

    public String getCsEtatSEDEX() {
        return csEtatSEDEX;
    }

    public String getCsRevision() {
        return csRevision;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public String getDateEnvoiSedex() {
        return dateEnvoiSedex;
    }

    public String getDateMiseEnGed() {
        return dateMiseEnGed;
    }

    public String getDateSurDocument() {
        return dateSurDocument;
    }

    public String getEmailAdresse() {
        return emailAdresse;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public String getIdDomaineAdressePaiementPersonnalisee() {
        return idDomaineAdressePaiementPersonnalisee;
    }

    public String getIdPersonneReference() {
        return idPersonneReference;
    }

    public String getIdPrononce() {
        return idPrononce;
    }

    public String getIdTiersAdrCourrier() {
        return idTiersAdrCourrier;
    }

    public String getIdTiersAdressePaiementPersonnalisee() {
        return idTiersAdressePaiementPersonnalisee;
    }

    public String getIdTiersAdrPmt() {
        return idTiersAdrPmt;
    }

    public String getIdTiersPrincipal() {
        return idTiersPrincipal;
    }

    public Boolean getIsDecisionValidee() {
        return isDecisionValidee;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    public String getNoRevAGarantir() {
        return noRevAGarantir;
    }

    public String getNumeroAffilieAdressePaiementPersonnalisee() {
        return numeroAffilieAdressePaiementPersonnalisee;
    }

    public String getPersonnalisationAdressePaiement() {
        return personnalisationAdressePaiement;
    }

    public String getRemarques() {
        return remarques;
    }

    public String getTauxImposition() {
        return tauxImposition;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    public void setBeneficiaire(String beneiciaire) {
        beneficiaire = beneiciaire;
    }

    public void setCsCantonTauxImposition(String csCantonTauxImposition) {
        this.csCantonTauxImposition = csCantonTauxImposition;
    }

    public void setCsEtatMiseEnGed(String csEtatMiseEnGed) {
        this.csEtatMiseEnGed = csEtatMiseEnGed;
    }

    public void setCsEtatSEDEX(String csEtatSEDEX) {
        this.csEtatSEDEX = csEtatSEDEX;
    }

    public void setCsRevision(String csRevision) {
        this.csRevision = csRevision;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public void setDateEnvoiSedex(String dateEnvoiSedex) {
        this.dateEnvoiSedex = dateEnvoiSedex;
    }

    public void setDateMiseEnGed(String dateMiseEnGed) {
        this.dateMiseEnGed = dateMiseEnGed;
    }

    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
    }

    public void setEmailAdresse(String emailAdresse) {
        this.emailAdresse = emailAdresse;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public void setIdDomaineAdressePaiementPersonnalisee(String idDomaineAdressePaiementPersonnalisee) {
        this.idDomaineAdressePaiementPersonnalisee = idDomaineAdressePaiementPersonnalisee;
    }

    public void setIdPersonneReference(String idPersonneReference) {
        this.idPersonneReference = idPersonneReference;
    }

    public void setIdPrononce(String idPrononce) {
        this.idPrononce = idPrononce;
    }

    public void setIdTiersAdrCourrier(String idTiersAdrCourrier) {
        this.idTiersAdrCourrier = idTiersAdrCourrier;
    }

    public void setIdTiersAdressePaiementPersonnalisee(String idTiersAdressePaiementPersonnalisee) {
        this.idTiersAdressePaiementPersonnalisee = idTiersAdressePaiementPersonnalisee;
    }

    public void setIdTiersAdrPmt(String idTiersAdrPmt) {
        this.idTiersAdrPmt = idTiersAdrPmt;
    }

    public void setIdTiersPrincipal(String idTiersPrincipal) {
        this.idTiersPrincipal = idTiersPrincipal;
    }

    public void setIsDecisionValidee(Boolean isDecisionValidee) {
        this.isDecisionValidee = isDecisionValidee;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    public void setNoRevAGarantir(String noRevAGarantir) {
        this.noRevAGarantir = noRevAGarantir;
    }

    public void setNumeroAffilieAdressePaiementPersonnalisee(String numeroAffilieAdressePaiementPersonnalisee) {
        this.numeroAffilieAdressePaiementPersonnalisee = numeroAffilieAdressePaiementPersonnalisee;
    }

    public void setPersonnalisationAdressePaiement(String personnalisationAdressePaiement) {
        this.personnalisationAdressePaiement = personnalisationAdressePaiement;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }

    public void setTauxImposition(String tauxImposition) {
        this.tauxImposition = tauxImposition;
    }
}
