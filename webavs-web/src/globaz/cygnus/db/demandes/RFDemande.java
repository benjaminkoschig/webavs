package globaz.cygnus.db.demandes;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSpy;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.db.demandes.PRDemande;

/**
 * @author jje
 */
public class RFDemande extends BEntity {

    public Boolean getIsRetro() {
        return isRetro;
    }

    public void setIsRetro(Boolean isRetro) {
        this.isRetro = isRetro;
    }

    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CS_ETAT = "EDTETA";
    public static final String FIELDNAME_CS_SOURCE = "EDTSRC";
    public static final String FIELDNAME_CS_STATUT = "EDTSTA";
    public static final String FIELDNAME_DATE_DEBUT_TRAITEMENT = "EDDDTR";
    public static final String FIELDNAME_DATE_FACTURE = "EDDFAC";
    public static final String FIELDNAME_DATE_FIN_TRAITEMENT = "EDDFTR";
    public static final String FIELDNAME_DATE_IMPUTATION = "EDDIMP";
    public static final String FIELDNAME_DATE_RECEPTION = "EDDREC";
    public static final String FIELDNAME_ID_ADRESSE_PAIEMENT = "EDIAPA";
    public static final String FIELDNAME_ID_DECISION = "EDIDEC";
    public static final String FIELDNAME_ID_DEMANDE = "EDIDEM";
    public static final String FIELDNAME_ID_DEMANDE_PARENT = "EDIDPA";
    public static final String FIELDNAME_ID_DOSSIER = "EDIDOS";
    public static final String FIELDNAME_ID_EXECUTION_PROCESS = "EDIEPR";
    public static final String FIELDNAME_ID_FOURNISSEUR = "EDIFOU";
    public static final String FIELDNAME_ID_GESTIONNAIRE = "EDIGES";
    public static final String FIELDNAME_ID_QD_ASSURE = "EDIQDA";
    public static final String FIELDNAME_ID_QD_PRINCIPALE = "EDIIQD";
    public static final String FIELDNAME_ID_SOUS_TYPE_DE_SOIN = "EDISTS";
    public static final String FIELDNAME_IS_CONTRAT_TRAVAIL = "EDBCTR";
    public static final String FIELDNAME_IS_FORCER_IMPUTATION_SUR_QD = "EDBFIM";
    public static final String FIELDNAME_IS_FORCER_PAIEMENT = "EDBFPA";
    public static final String FIELDNAME_IS_PP = "EDBPPR";
    public static final String FIELDNAME_IS_TEXTE_REDIRECTION = "EDBTRE";
    public static final String FIELDNAME_MONTANT_A_PAYER = "EDMPAY";
    public static final String FIELDNAME_MONTANT_FACTURE = "EDMFAC";
    public static final String FIELDNAME_MONTANT_MENSUEL = "EDMMEN";
    public static final String FIELDNAME_NUMERO_FACTURE = "EDNFAC";
    public static final String FIELDNAME_REMARQUE_FOURNISSEUR = "EDREFF";
    public static final String FIELDNAME_IS_RETRO = "EDBRET";
    public static final String TABLE_NAME = "RFDEMAN";

    private BSpy creationSpy = null;
    private String csEtat = "";
    private String csSource = "";
    private String csStatut = "";
    private String dateDebutTraitement = "";
    private String dateFacture = "";
    private String dateFinTraitement = "";
    private String dateImputation = "";
    private String datePaiement = "";
    private String dateReception = "";
    private String idAdressePaiement = "";
    private String idDecision = "";
    private String idDemande = "";
    private String idDemandeParent = "";
    private String idDossier = "";
    private String idExecutionProcess = "";
    private String idFournisseur = "";
    private String idGestionnaire = "";
    private String idQdAssure = "";
    private String idQdPrincipale = "";
    private String idSousTypeDeSoin = "";
    private Boolean isRetro = Boolean.FALSE;
    private Boolean isContratDeTravail = Boolean.FALSE;
    private Boolean isForcerImputationSurQd = Boolean.FALSE;
    private Boolean isForcerPaiement = Boolean.FALSE;
    private Boolean isPP = Boolean.FALSE;
    private Boolean isTexteRedirection = Boolean.FALSE;
    private String montantAPayer = "";
    private String montantFacture = "";
    private String montantMensuel = "";
    private String numeroFacture = "";
    private transient PRDemande prDemande;
    private String remarqueFournisseur = "";

    private BSpy spy = null;

    /**
     * Crée une nouvelle instance de la classe RFDemande.
     */
    public RFDemande() {
        super();
    }

    /**
     * Méthode avant l'ajout l'incrémentation de la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdDemande(this._incCounter(transaction, idDemande, RFDemande.TABLE_NAME));
    }

    /**
     * getter pour le nom de la table des demandes
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFDemande.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des demandes
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idDemande = statement.dbReadNumeric(RFDemande.FIELDNAME_ID_DEMANDE);
        dateReception = statement.dbReadDateAMJ(RFDemande.FIELDNAME_DATE_RECEPTION);
        dateFacture = statement.dbReadDateAMJ(RFDemande.FIELDNAME_DATE_FACTURE);
        dateDebutTraitement = statement.dbReadDateAMJ(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
        dateFinTraitement = statement.dbReadDateAMJ(RFDemande.FIELDNAME_DATE_FIN_TRAITEMENT);
        dateImputation = statement.dbReadDateAMJ(RFDemande.FIELDNAME_DATE_IMPUTATION);
        numeroFacture = statement.dbReadString(RFDemande.FIELDNAME_NUMERO_FACTURE);
        montantFacture = statement.dbReadNumeric(RFDemande.FIELDNAME_MONTANT_FACTURE, 2);
        montantAPayer = statement.dbReadNumeric(RFDemande.FIELDNAME_MONTANT_A_PAYER, 2);
        isForcerPaiement = statement.dbReadBoolean(RFDemande.FIELDNAME_IS_FORCER_PAIEMENT);
        csEtat = statement.dbReadNumeric(RFDemande.FIELDNAME_CS_ETAT);
        csSource = statement.dbReadNumeric(RFDemande.FIELDNAME_CS_SOURCE);
        csStatut = statement.dbReadNumeric(RFDemande.FIELDNAME_CS_STATUT);
        idFournisseur = statement.dbReadNumeric(RFDemande.FIELDNAME_ID_FOURNISSEUR);
        idGestionnaire = statement.dbReadString(RFDemande.FIELDNAME_ID_GESTIONNAIRE);
        idDossier = statement.dbReadNumeric(RFDemande.FIELDNAME_ID_DOSSIER);
        idAdressePaiement = statement.dbReadNumeric(RFDemande.FIELDNAME_ID_ADRESSE_PAIEMENT);
        idSousTypeDeSoin = statement.dbReadNumeric(RFDemande.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);
        idQdPrincipale = statement.dbReadNumeric(RFDemande.FIELDNAME_ID_QD_PRINCIPALE);
        idQdAssure = statement.dbReadNumeric(RFDemande.FIELDNAME_ID_QD_ASSURE);
        idDecision = statement.dbReadNumeric(RFDemande.FIELDNAME_ID_DECISION);
        idDemandeParent = statement.dbReadNumeric(RFDemande.FIELDNAME_ID_DEMANDE_PARENT);
        isContratDeTravail = statement.dbReadBoolean(RFDemande.FIELDNAME_IS_CONTRAT_TRAVAIL);
        isPP = statement.dbReadBoolean(RFDemande.FIELDNAME_IS_PP);
        isTexteRedirection = statement.dbReadBoolean(RFDemande.FIELDNAME_IS_TEXTE_REDIRECTION);
        montantMensuel = statement.dbReadNumeric(RFDemande.FIELDNAME_MONTANT_MENSUEL, 2);
        isForcerImputationSurQd = statement.dbReadBoolean(RFDemande.FIELDNAME_IS_FORCER_IMPUTATION_SUR_QD);
        remarqueFournisseur = statement.dbReadString(RFDemande.FIELDNAME_REMARQUE_FOURNISSEUR);
        idExecutionProcess = statement.dbReadNumeric(RFDemande.FIELDNAME_ID_EXECUTION_PROCESS);
        isRetro = statement.dbReadBoolean(RFDemande.FIELDNAME_IS_RETRO);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * Définition de la clé primaire de la table des demandes
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFDemande.FIELDNAME_ID_DEMANDE,
                this._dbWriteNumeric(statement.getTransaction(), idDemande, "idDemande"));
    }

    /**
     * Méthode d'écriture des champs dans la table des demandes
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(RFDemande.FIELDNAME_ID_DEMANDE,
                this._dbWriteNumeric(statement.getTransaction(), idDemande, "idDemande"));
        statement.writeField(RFDemande.FIELDNAME_DATE_RECEPTION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateReception, "dateReception"));
        statement.writeField(RFDemande.FIELDNAME_DATE_FACTURE,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFacture, "dateFacture"));
        statement.writeField(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDebutTraitement, "dateDebutTraitement"));
        statement.writeField(RFDemande.FIELDNAME_DATE_FIN_TRAITEMENT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFinTraitement, "dateFinTraitement"));
        statement.writeField(RFDemande.FIELDNAME_DATE_IMPUTATION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateImputation, "dateImputation"));
        statement.writeField(RFDemande.FIELDNAME_NUMERO_FACTURE,
                this._dbWriteString(statement.getTransaction(), numeroFacture, "numeroFacture"));
        statement.writeField(RFDemande.FIELDNAME_MONTANT_FACTURE,
                this._dbWriteNumeric(statement.getTransaction(), montantFacture, "montantFacture"));
        statement.writeField(RFDemande.FIELDNAME_MONTANT_A_PAYER,
                this._dbWriteNumeric(statement.getTransaction(), montantAPayer, "montantAPayer"));
        statement.writeField(RFDemande.FIELDNAME_IS_FORCER_PAIEMENT, this._dbWriteBoolean(statement.getTransaction(),
                isForcerPaiement, BConstants.DB_TYPE_BOOLEAN_CHAR, "isForcerPaiement"));
        statement.writeField(RFDemande.FIELDNAME_CS_ETAT,
                this._dbWriteNumeric(statement.getTransaction(), csEtat, "csEtat"));
        statement.writeField(RFDemande.FIELDNAME_CS_SOURCE,
                this._dbWriteNumeric(statement.getTransaction(), csSource, "csSource"));
        statement.writeField(RFDemande.FIELDNAME_CS_STATUT,
                this._dbWriteNumeric(statement.getTransaction(), csStatut, "csStatut"));
        statement.writeField(RFDemande.FIELDNAME_ID_FOURNISSEUR,
                this._dbWriteNumeric(statement.getTransaction(), idFournisseur, "idFournisseur"));
        statement.writeField(RFDemande.FIELDNAME_ID_GESTIONNAIRE,
                this._dbWriteString(statement.getTransaction(), idGestionnaire, "idGestionnaire"));
        statement.writeField(RFDemande.FIELDNAME_ID_DOSSIER,
                this._dbWriteNumeric(statement.getTransaction(), idDossier, "idDossier"));
        statement.writeField(RFDemande.FIELDNAME_ID_ADRESSE_PAIEMENT,
                this._dbWriteNumeric(statement.getTransaction(), idAdressePaiement, "idAdressePaiement"));
        statement.writeField(RFDemande.FIELDNAME_ID_SOUS_TYPE_DE_SOIN,
                this._dbWriteNumeric(statement.getTransaction(), idSousTypeDeSoin, "idSousTypeDeSoin"));
        statement.writeField(RFDemande.FIELDNAME_ID_QD_ASSURE,
                this._dbWriteNumeric(statement.getTransaction(), idQdAssure, "idQdAssure"));
        statement.writeField(RFDemande.FIELDNAME_ID_QD_PRINCIPALE,
                this._dbWriteNumeric(statement.getTransaction(), idQdPrincipale, "idQdPrincipale"));
        statement.writeField(RFDemande.FIELDNAME_ID_DECISION,
                this._dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));
        statement.writeField(RFDemande.FIELDNAME_ID_DEMANDE_PARENT,
                this._dbWriteNumeric(statement.getTransaction(), idDemandeParent, "idDemandeParent"));
        statement.writeField(RFDemande.FIELDNAME_IS_CONTRAT_TRAVAIL, this._dbWriteBoolean(statement.getTransaction(),
                isContratDeTravail, BConstants.DB_TYPE_BOOLEAN_CHAR, "isContratDeTravail"));
        statement.writeField(RFDemande.FIELDNAME_IS_PP,
                this._dbWriteBoolean(statement.getTransaction(), isPP, BConstants.DB_TYPE_BOOLEAN_CHAR, "isPP"));
        statement.writeField(RFDemande.FIELDNAME_IS_TEXTE_REDIRECTION, this._dbWriteBoolean(statement.getTransaction(),
                isTexteRedirection, BConstants.DB_TYPE_BOOLEAN_CHAR, "isTexteRedirection"));
        statement.writeField(RFDemande.FIELDNAME_MONTANT_MENSUEL,
                this._dbWriteNumeric(statement.getTransaction(), montantMensuel, "montantMensuel"));
        statement.writeField(RFDemande.FIELDNAME_IS_FORCER_IMPUTATION_SUR_QD, this._dbWriteBoolean(
                statement.getTransaction(), isForcerImputationSurQd, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "isForcerImputationSurQd"));
        statement.writeField(RFDemande.FIELDNAME_REMARQUE_FOURNISSEUR,
                this._dbWriteString(statement.getTransaction(), remarqueFournisseur, "remarqueFournisseur"));
        statement.writeField(RFDemande.FIELDNAME_ID_EXECUTION_PROCESS,
                this._dbWriteNumeric(statement.getTransaction(), idExecutionProcess, "idExecutionProcess"));
        statement.writeField(RFDemande.FIELDNAME_IS_RETRO,
                this._dbWriteBoolean(statement.getTransaction(), isRetro, BConstants.DB_TYPE_BOOLEAN_CHAR));

    }

    @Override
    public BSpy getCreationSpy() {
        return super.getCreationSpy();
    }

    public String getCsEtat() {
        return csEtat;
    }

    public String getCsSource() {
        return csSource;
    }

    public String getCsStatut() {
        return csStatut;
    }

    public String getDateDebutTraitement() {
        return dateDebutTraitement;
    }

    public String getDateFacture() {
        return dateFacture;
    }

    public String getDateFinTraitement() {
        return dateFinTraitement;
    }

    public String getDateImputation() {
        return dateImputation;
    }

    public String getDatePaiement() {
        return datePaiement;
    }

    public String getDateReception() {
        return dateReception;
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdDemandeParent() {
        return idDemandeParent;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdExecutionProcess() {
        return idExecutionProcess;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdQdAssure() {
        return idQdAssure;
    }

    public String getIdQdPrincipale() {
        return idQdPrincipale;
    }

    public String getIdSousTypeDeSoin() {
        return idSousTypeDeSoin;
    }

    public Boolean getIsContratDeTravail() {
        return isContratDeTravail;
    }

    public Boolean getIsForcerImputationSurQd() {
        return isForcerImputationSurQd;
    }

    public Boolean getIsForcerPaiement() {
        return isForcerPaiement;
    }

    public Boolean getIsPP() {
        return isPP;
    }

    public String getMontantAPayer() {
        return montantAPayer;
    }

    public String getMontantFacture() {
        return montantFacture;
    }

    public String getMontantMensuel() {
        return montantMensuel;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public PRDemande getPrDemande() {
        return prDemande;
    }

    public String getRemarqueFournisseur() {
        return remarqueFournisseur;
    }

    @Override
    public BSpy getSpy() {
        return super.getSpy();
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setCreationSpy(BSpy creationSpy) {
        this.creationSpy = creationSpy;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setCsSource(String csSource) {
        this.csSource = csSource;
    }

    public void setCsStatut(String csStatut) {
        this.csStatut = csStatut;
    }

    public void setDateDebutTraitement(String dateDebutTraitement) {
        this.dateDebutTraitement = dateDebutTraitement;
    }

    public void setDateFacture(String dateFacture) {
        this.dateFacture = dateFacture;
    }

    public void setDateFinTraitement(String dateFinTraitement) {
        this.dateFinTraitement = dateFinTraitement;
    }

    public void setDateImputation(String dateImputation) {
        this.dateImputation = dateImputation;
    }

    public void setDatePaiement(String datePaiement) {
        this.datePaiement = datePaiement;
    }

    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdDemandeParent(String idDemandeParent) {
        this.idDemandeParent = idDemandeParent;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdExecutionProcess(String idExecutionProcess) {
        this.idExecutionProcess = idExecutionProcess;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdQdAssure(String idQdAssure) {
        this.idQdAssure = idQdAssure;
    }

    public void setIdQdPrincipale(String idQdPrincipale) {
        this.idQdPrincipale = idQdPrincipale;
    }

    public void setIdSousTypeDeSoin(String idSousTypeDeSoin) {
        this.idSousTypeDeSoin = idSousTypeDeSoin;
    }

    public void setIsContratDeTravail(Boolean isContratDeTravail) {
        this.isContratDeTravail = isContratDeTravail;
    }

    public void setIsForcerImputationSurQd(Boolean isForcerImputationSurQd) {
        this.isForcerImputationSurQd = isForcerImputationSurQd;
    }

    public void setIsForcerPaiement(Boolean isForcerPaiement) {
        this.isForcerPaiement = isForcerPaiement;
    }

    public void setIsPP(Boolean isPP) {
        this.isPP = isPP;
    }

    public void setMontantAPayer(String montantAPayer) {
        this.montantAPayer = montantAPayer;
    }

    public void setMontantFacture(String montantFacture) {
        this.montantFacture = montantFacture;
    }

    public void setMontantMensuel(String montantMensuel) {
        this.montantMensuel = montantMensuel;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public void setPrDemande(PRDemande prDemande) {
        this.prDemande = prDemande;
    }

    public void setRemarqueFournisseur(String remarqueFournisseur) {
        this.remarqueFournisseur = remarqueFournisseur;
    }

    public void setSpy(BSpy spy) {
        this.spy = spy;
    }

    public Boolean getIsTexteRedirection() {
        return isTexteRedirection;
    }

    public void setIsTexteRedirection(Boolean isTexteRedirection) {
        this.isTexteRedirection = isTexteRedirection;
    }

}
