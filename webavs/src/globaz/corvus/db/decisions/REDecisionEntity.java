/*
 * Créé le 25 juil. 07
 */
package globaz.corvus.db.decisions;

import globaz.corvus.api.lots.IRELot;
import globaz.corvus.db.prestations.REPrestationJointLot;
import globaz.corvus.db.prestations.REPrestationJointLotManager;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.prestations.REPrestationsManager;
import globaz.corvus.exceptions.REBusinessException;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;

/**
 * @author SCR
 */
public class REDecisionEntity extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int ALTERNATE_KEY_UID = 1;

    public static final String FIELDNAME_COPIE_UID = "YWLCUI";
    public static final String FIELDNAME_DATE_DEBUT_RETRO = "YWDDER";
    public static final String FIELDNAME_DATE_DECISION = "YWDDEC";
    public static final String FIELDNAME_DATE_FIN_RETRO = "YWDFIR";
    public static final String FIELDNAME_DATE_PREPARATION = "YWDPRE";
    public static final String FIELDNAME_DATE_VALIDATION = "YWDVAL";
    public static final String FIELDNAME_DECISION_DEPUIS = "YWDDED";
    public static final String FIELDNAME_EMAIL_ADRESSE = "YWLEMA";
    public static final String FIELDNAME_ETAT = "YWTETA";
    public static final String FIELDNAME_GENRE_DECISION = "YWTTYD";
    public static final String FIELDNAME_ID_DECISION = "YWIDEC";
    public static final String FIELDNAME_ID_DEMANDE_RENTE = "YWIDEM";
    public static final String FIELDNAME_ID_PRESTATION = "YWIPRS";
    public static final String FIELDNAME_ID_TIER_ADR_COURRIER = "YWITAC";
    public static final String FIELDNAME_ID_TIERS_BENEF_PRINCIPAL = "YWITBE";
    public static final String FIELDNAME_IS_AVEC_BONNE_FOI = "YWBFOI";
    public static final String FIELDNAME_IS_INTERET_MORATOIRE = "YWBIIM";
    public static final String FIELDNAME_IS_REM_ANNULE_DECISION = "YWBRAD";
    public static final String FIELDNAME_IS_REM_RED_PLAFOND = "YWBRRP";
    public static final String FIELDNAME_IS_REM_REMARIAGE_RENTE_SURVIVANT = "YWBRRS";
    public static final String FIELDNAME_IS_REM_RENTE_AVEC_DEBUT_DROIT_5ANS_AVANT_DEPOT_DEMANDE = "YWBDDA";
    public static final String FIELDNAME_IS_REM_RENTE_AVEC_MONTANT_MINIMUM_MAJORE_INVALIDITE = "YWBMMI";
    public static final String FIELDNAME_IS_REM_RENTE_ENFANT = "YWBRRE";
    public static final String FIELDNAME_IS_REM_RENTE_REDUITE_POUR_SURASSURANCEE = "YWBRSU";
    public static final String FIELDNAME_IS_REM_RENTE_VEUF_LIMITEE = "YWBRVF";
    public static final String FIELDNAME_IS_REM_RENTE_VEUVE_LIMITEE = "YWBRVE";
    public static final String FIELDNAME_IS_REM_SUPP_VEUF = "YWBRSV";
    public static final String FIELDNAME_IS_REMARQUE_INCARCERATION = "YWBRIN";
    public static final String FIELDNAME_IS_SANS_BONNE_FOI = "YWBFFO";
    public static final String FIELDNAME_IS_TEXTE_OBLIG_PAYER_COTI = "YWBOBC";
    public static final String FIELDNAME_PREPARE_PAR = "YWIPRE";
    public static final String FIELDNAME_REMARQUE_DECISION = "YWLREM";
    public static final String FIELDNAME_TRAITER_PAR = "YWITRA";
    public static final String FIELDNAME_TYPE_DECISION = "YWTTYP";
    public static final String FIELDNAME_UID = "YWLUID";
    public static final String FIELDNAME_VALIDE_PAR = "YWIVAP";

    public static final String TABLE_NAME_DECISIONS = "REDECIS";

    private String adresseEMail = "";
    private String csEtat = "";
    private String csGenreDecision = "";
    private String csTypeDecision = "";
    private String cuid = "";
    private String dateDebutRetro = "";
    private String dateDecision = "";
    private String dateFinRetro = "";
    private String datePreparation = "";
    private String dateValidation = "";
    private String decisionDepuis = "";
    private String idDecision = "";
    private String idDemandeRente = "";
    private String idTiersAdrCourrier = "";
    private String idTiersBeneficiairePrincipal = "";
    private Boolean isAvecBonneFoi = Boolean.FALSE;
    private Boolean isObliPayerCoti = Boolean.FALSE;
    private Boolean isRemAnnDeci = Boolean.FALSE;
    private Boolean isRemarqueIncarceration = Boolean.FALSE;
    /**
     * Remarque de la décision : Remariage d'une rente de survivant
     */
    private Boolean isRemarqueRemariageRenteDeSurvivant = Boolean.FALSE;
    /**
     * Remarque de la décision : début de droit fixé à 5 ans avant la date de dépôt de la demande
     */
    private Boolean isRemarqueRenteAvecDebutDroit5AnsAvantDepotDemande = Boolean.FALSE;
    /**
     * Remarque dans la décision : Rente avec montant minimum majoré pour invalidité précoce
     */
    private Boolean isRemarqueRenteAvecMontantMinimumMajoreInvalidite = Boolean.FALSE;
    /**
     * Remarque de la décision : Rente de VEUF (m) limitée
     */
    private Boolean isRemarqueRenteDeVeufLimitee = Boolean.FALSE;
    /**
     * Remarque de la décision : Rente de VEUVE (f) limitée
     */
    private Boolean isRemarqueRenteDeVeuveLimitee = Boolean.FALSE;
    /**
     * Remarque de la décision : Rente pour enfants
     */
    private Boolean isRemarqueRentePourEnfant = Boolean.FALSE;
    /**
     * Remarque dans la décision : Rente réduite pour surassurance
     */
    private Boolean isRemarqueRenteReduitePourSurassurance = Boolean.FALSE;
    private Boolean isRemInteretMoratoires = Boolean.FALSE;
    private Boolean isRemRedPlaf = Boolean.FALSE;
    private Boolean isRemSuppVeuf = Boolean.FALSE;
    private Boolean isSansBonneFoi = Boolean.FALSE;
    private String preparePar = "";
    private String remarqueDecision = "";
    private String traitePar = "";
    private String uid = "";
    private String validePar = "";

    public REDecisionEntity() {
        super();

        wantCallMethodBefore(true);
    }

    /**
     * @param transaction
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdDecision(this._incCounter(transaction, "0"));
    }

    @Override
    protected void _beforeRetrieve(BTransaction transaction) throws Exception {
        setId(idDecision);
        super._beforeRetrieve(transaction);
    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {

        String proprieteEnBase = getSession().getApplication().getProperty(
                "ignorer.verification.avant.modification.decision");
        if (JadeStringUtil.isBlankOrZero(proprieteEnBase) || !"true".equals(proprieteEnBase.toLowerCase())) {

            // recherche de la prestation (REPREST) de cette décision ainsi que le lot lié
            REPrestationJointLotManager prestationManager = new REPrestationJointLotManager();
            prestationManager.setSession(getSession());
            prestationManager.setForIdDecision(idDecision);
            prestationManager.find();

            if (prestationManager.size() > 0) {
                for (int i = 0; i < prestationManager.size(); i++) {
                    REPrestationJointLot prestation = (REPrestationJointLot) prestationManager.get(i);

                    if (IRELot.CS_ETAT_LOT_VALIDE.equals(prestation.getCsEtatLot())
                            || IRELot.CS_ETAT_LOT_EN_TRAITEMENT.equals(prestation.getCsEtatLot())) {
                        throw new REBusinessException(getSession().getLabel("ERREUR_MODIFICATION_DECISION_INTERDITE"));
                    }
                }
            }
        }

        super._beforeUpdate(transaction);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return REDecisionEntity.TABLE_NAME_DECISIONS;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idDecision = statement.dbReadNumeric(REDecisionEntity.FIELDNAME_ID_DECISION);
        csTypeDecision = statement.dbReadNumeric(REDecisionEntity.FIELDNAME_TYPE_DECISION);
        dateDecision = statement.dbReadDateAMJ(REDecisionEntity.FIELDNAME_DATE_DECISION);
        csEtat = statement.dbReadNumeric(REDecisionEntity.FIELDNAME_ETAT);
        preparePar = statement.dbReadString(REDecisionEntity.FIELDNAME_PREPARE_PAR);
        datePreparation = statement.dbReadDateAMJ(REDecisionEntity.FIELDNAME_DATE_PREPARATION);
        dateValidation = statement.dbReadDateAMJ(REDecisionEntity.FIELDNAME_DATE_VALIDATION);
        validePar = statement.dbReadString(REDecisionEntity.FIELDNAME_VALIDE_PAR);
        uid = statement.dbReadString(REDecisionEntity.FIELDNAME_UID);
        cuid = statement.dbReadString(REDecisionEntity.FIELDNAME_COPIE_UID);
        idDemandeRente = statement.dbReadNumeric(REDecisionEntity.FIELDNAME_ID_DEMANDE_RENTE);
        decisionDepuis = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REDecisionEntity.FIELDNAME_DECISION_DEPUIS));
        adresseEMail = statement.dbReadString(REDecisionEntity.FIELDNAME_EMAIL_ADRESSE);
        dateDebutRetro = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REDecisionEntity.FIELDNAME_DATE_DEBUT_RETRO));
        dateFinRetro = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REDecisionEntity.FIELDNAME_DATE_FIN_RETRO));
        isAvecBonneFoi = statement.dbReadBoolean(REDecisionEntity.FIELDNAME_IS_AVEC_BONNE_FOI);
        isSansBonneFoi = statement.dbReadBoolean(REDecisionEntity.FIELDNAME_IS_SANS_BONNE_FOI);
        isRemarqueIncarceration = statement.dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REMARQUE_INCARCERATION);
        isRemarqueRenteDeVeufLimitee = statement.dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REM_RENTE_VEUF_LIMITEE);
        isRemarqueRenteDeVeuveLimitee = statement.dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REM_RENTE_VEUVE_LIMITEE);
        isRemarqueRemariageRenteDeSurvivant = statement
                .dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REM_REMARIAGE_RENTE_SURVIVANT);
        isRemarqueRentePourEnfant = statement.dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REM_RENTE_ENFANT);
        isRemarqueRenteAvecDebutDroit5AnsAvantDepotDemande = statement
                .dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REM_RENTE_AVEC_DEBUT_DROIT_5ANS_AVANT_DEPOT_DEMANDE);
        isRemarqueRenteAvecMontantMinimumMajoreInvalidite = statement
                .dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REM_RENTE_AVEC_MONTANT_MINIMUM_MAJORE_INVALIDITE);
        isRemarqueRenteReduitePourSurassurance = statement
                .dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REM_RENTE_REDUITE_POUR_SURASSURANCEE);
        isRemInteretMoratoires = statement.dbReadBoolean(REDecisionEntity.FIELDNAME_IS_INTERET_MORATOIRE);
        isObliPayerCoti = statement.dbReadBoolean(REDecisionEntity.FIELDNAME_IS_TEXTE_OBLIG_PAYER_COTI);
        traitePar = statement.dbReadString(REDecisionEntity.FIELDNAME_TRAITER_PAR);
        idTiersBeneficiairePrincipal = statement.dbReadNumeric(REDecisionEntity.FIELDNAME_ID_TIERS_BENEF_PRINCIPAL);
        csGenreDecision = statement.dbReadNumeric(REDecisionEntity.FIELDNAME_GENRE_DECISION);
        isRemSuppVeuf = statement.dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REM_SUPP_VEUF);
        isRemRedPlaf = statement.dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REM_RED_PLAFOND);
        isRemAnnDeci = statement.dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REM_ANNULE_DECISION);
        remarqueDecision = statement.dbReadString(REDecisionEntity.FIELDNAME_REMARQUE_DECISION);
        idTiersAdrCourrier = statement.dbReadNumeric(REDecisionEntity.FIELDNAME_ID_TIER_ADR_COURRIER);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        switch (alternateKey) {
            case ALTERNATE_KEY_UID:
                statement.writeKey(REDecisionEntity.FIELDNAME_UID,
                        this._dbWriteString(statement.getTransaction(), getUid(), "uuid"));

                break;
            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(REDecisionEntity.FIELDNAME_ID_DECISION,
                this._dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(REDecisionEntity.FIELDNAME_ID_DECISION,
                this._dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));
        statement.writeField(REDecisionEntity.FIELDNAME_TYPE_DECISION,
                this._dbWriteNumeric(statement.getTransaction(), csTypeDecision, "csTypeDecision"));
        statement.writeField(REDecisionEntity.FIELDNAME_DATE_DECISION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDecision, "dateDecision"));
        statement.writeField(REDecisionEntity.FIELDNAME_ETAT,
                this._dbWriteNumeric(statement.getTransaction(), csEtat, "csEtat"));
        statement.writeField(REDecisionEntity.FIELDNAME_PREPARE_PAR,
                this._dbWriteString(statement.getTransaction(), preparePar, "preparePar"));
        statement.writeField(REDecisionEntity.FIELDNAME_DATE_PREPARATION,
                this._dbWriteDateAMJ(statement.getTransaction(), datePreparation, "datePreparation"));
        statement.writeField(REDecisionEntity.FIELDNAME_DATE_VALIDATION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateValidation, "dateValidation"));
        statement.writeField(REDecisionEntity.FIELDNAME_VALIDE_PAR,
                this._dbWriteString(statement.getTransaction(), validePar, "validePar"));
        statement.writeField(REDecisionEntity.FIELDNAME_UID,
                this._dbWriteString(statement.getTransaction(), uid, "uid"));
        statement.writeField(REDecisionEntity.FIELDNAME_COPIE_UID,
                this._dbWriteString(statement.getTransaction(), cuid, "cuid"));
        statement.writeField(REDecisionEntity.FIELDNAME_ID_DEMANDE_RENTE,
                this._dbWriteNumeric(statement.getTransaction(), idDemandeRente, "idDemandeRente"));
        statement.writeField(
                REDecisionEntity.FIELDNAME_DECISION_DEPUIS,
                this._dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(decisionDepuis), "decisionDepuis"));
        statement.writeField(REDecisionEntity.FIELDNAME_EMAIL_ADRESSE,
                this._dbWriteString(statement.getTransaction(), adresseEMail, "adresseEMail"));
        statement.writeField(
                REDecisionEntity.FIELDNAME_DATE_DEBUT_RETRO,
                this._dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateDebutRetro), "dateDebutRetro"));
        statement.writeField(
                REDecisionEntity.FIELDNAME_DATE_FIN_RETRO,
                this._dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateFinRetro), "dateFinRetro"));
        statement.writeField(REDecisionEntity.FIELDNAME_IS_AVEC_BONNE_FOI, this._dbWriteBoolean(
                statement.getTransaction(), isAvecBonneFoi, BConstants.DB_TYPE_BOOLEAN_CHAR, "isAvecBonneFoi"));
        statement.writeField(REDecisionEntity.FIELDNAME_IS_SANS_BONNE_FOI, this._dbWriteBoolean(
                statement.getTransaction(), isSansBonneFoi, BConstants.DB_TYPE_BOOLEAN_CHAR, "isSansBonneFoi"));
        statement.writeField(REDecisionEntity.FIELDNAME_IS_TEXTE_OBLIG_PAYER_COTI, this._dbWriteBoolean(
                statement.getTransaction(), isObliPayerCoti, BConstants.DB_TYPE_BOOLEAN_CHAR, "isObliPayerCoti"));
        statement.writeField(REDecisionEntity.FIELDNAME_TRAITER_PAR,
                this._dbWriteString(statement.getTransaction(), traitePar, "traitePar"));
        statement.writeField(REDecisionEntity.FIELDNAME_ID_TIERS_BENEF_PRINCIPAL, this._dbWriteNumeric(
                statement.getTransaction(), idTiersBeneficiairePrincipal, "idTiersBeneficiairePrincipal"));
        statement.writeField(REDecisionEntity.FIELDNAME_GENRE_DECISION,
                this._dbWriteNumeric(statement.getTransaction(), csGenreDecision, "csGenreDecision"));
        statement.writeField(REDecisionEntity.FIELDNAME_IS_REM_SUPP_VEUF, this._dbWriteBoolean(
                statement.getTransaction(), isRemSuppVeuf, BConstants.DB_TYPE_BOOLEAN_CHAR, "isRemSuppVeuf"));
        statement.writeField(REDecisionEntity.FIELDNAME_IS_REM_RED_PLAFOND, this._dbWriteBoolean(
                statement.getTransaction(), isRemRedPlaf, BConstants.DB_TYPE_BOOLEAN_CHAR, "isRemRedPlaf"));
        statement.writeField(REDecisionEntity.FIELDNAME_IS_REM_ANNULE_DECISION, this._dbWriteBoolean(
                statement.getTransaction(), isRemAnnDeci, BConstants.DB_TYPE_BOOLEAN_CHAR, "isRemAnnDeci"));
        statement.writeField(REDecisionEntity.FIELDNAME_REMARQUE_DECISION,
                this._dbWriteString(statement.getTransaction(), remarqueDecision, "remarqueDecision"));
        statement.writeField(REDecisionEntity.FIELDNAME_ID_TIER_ADR_COURRIER,
                this._dbWriteNumeric(statement.getTransaction(), idTiersAdrCourrier, "idTiersAdrCourrier"));
        statement.writeField(REDecisionEntity.FIELDNAME_IS_INTERET_MORATOIRE, this._dbWriteBoolean(
                statement.getTransaction(), isRemInteretMoratoires, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "isRemInteretMoratoires"));
        statement.writeField(REDecisionEntity.FIELDNAME_IS_REM_RENTE_VEUF_LIMITEE, this._dbWriteBoolean(
                statement.getTransaction(), isRemarqueRenteDeVeufLimitee, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "isRemarqueRenteDeVeufLimitee"));
        statement.writeField(REDecisionEntity.FIELDNAME_IS_REM_RENTE_VEUVE_LIMITEE, this._dbWriteBoolean(
                statement.getTransaction(), isRemarqueRenteDeVeuveLimitee, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "isRemarqueRenteDeVeuveLimitee"));
        statement.writeField(REDecisionEntity.FIELDNAME_IS_REM_REMARIAGE_RENTE_SURVIVANT, this._dbWriteBoolean(
                statement.getTransaction(), isRemarqueRemariageRenteDeSurvivant, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "isRemarqueRemariageRenteDeSurvivant"));
        statement.writeField(REDecisionEntity.FIELDNAME_IS_REM_RENTE_ENFANT, this._dbWriteBoolean(
                statement.getTransaction(), isRemarqueRentePourEnfant, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "isRemarqueRentePourEnfant"));
        statement.writeField(REDecisionEntity.FIELDNAME_IS_REM_RENTE_AVEC_DEBUT_DROIT_5ANS_AVANT_DEPOT_DEMANDE, this
                ._dbWriteBoolean(statement.getTransaction(), isRemarqueRenteAvecDebutDroit5AnsAvantDepotDemande,
                        BConstants.DB_TYPE_BOOLEAN_CHAR, "isRemarqueRenteAvecDebutDroit5AnsAvantDepotDemande"));
        statement.writeField(REDecisionEntity.FIELDNAME_IS_REM_RENTE_AVEC_MONTANT_MINIMUM_MAJORE_INVALIDITE, this
                ._dbWriteBoolean(statement.getTransaction(), isRemarqueRenteAvecMontantMinimumMajoreInvalidite,
                        BConstants.DB_TYPE_BOOLEAN_CHAR, "isRemarqueRenteAvecMontantMinimumMajoreInvalidite"));
        statement.writeField(REDecisionEntity.FIELDNAME_IS_REM_RENTE_REDUITE_POUR_SURASSURANCEE, this._dbWriteBoolean(
                statement.getTransaction(), isRemarqueRenteReduitePourSurassurance, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "isRemarqueRenteReduitePourSurassurance"));
        statement.writeField(REDecisionEntity.FIELDNAME_IS_REMARQUE_INCARCERATION, this._dbWriteBoolean(
                statement.getTransaction(), isRemarqueIncarceration, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "isRemarqueIncarceration"));

    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getAdresseEMail() {
        return adresseEMail;
    }

    /**
     * @return the csEtat
     */
    public String getCsEtat() {
        return csEtat;
    }

    public String getCsGenreDecision() {
        return csGenreDecision;
    }

    /**
     * @return the csTypeDecision
     */
    public String getCsTypeDecision() {
        return csTypeDecision;
    }

    /**
     * @return the cuid
     */
    public String getCuid() {
        return cuid;
    }

    public String getDateDebutRetro() {
        return dateDebutRetro;
    }

    /**
     * @return the dateDecision
     */
    public String getDateDecision() {
        return dateDecision;
    }

    public String getDateFinRetro() {
        return dateFinRetro;
    }

    /**
     * @return the datePreparation
     */
    public String getDatePreparation() {
        return datePreparation;
    }

    /**
     * @return the dateValidation
     */
    public String getDateValidation() {
        return dateValidation;
    }

    /**
     * @return the decisionDu
     */
    public String getDecisionDepuis() {
        return decisionDepuis;
    }

    /**
     * @return the idDecision
     */
    public String getIdDecision() {
        return idDecision;
    }

    /**
     * @return the idDemandeRente
     */
    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public String getIdTiersAdrCourrier() {
        return idTiersAdrCourrier;
    }

    public String getIdTiersBeneficiairePrincipal() {
        return idTiersBeneficiairePrincipal;
    }

    public Boolean getIsAvecBonneFoi() {
        return isAvecBonneFoi;
    }

    public Boolean getIsObliPayerCoti() {
        return isObliPayerCoti;
    }

    public Boolean getIsRemAnnDeci() {
        return isRemAnnDeci;
    }

    public Boolean getIsRemarqueIncarceration() {
        return isRemarqueIncarceration;
    }

    /**
     * @return the isRemarqueRemariageRenteDeSurvivant
     */
    public final Boolean getIsRemarqueRemariageRenteDeSurvivant() {
        return isRemarqueRemariageRenteDeSurvivant;
    }

    public final Boolean getIsRemarqueRenteAvecDebutDroit5AnsAvantDepotDemande() {
        return isRemarqueRenteAvecDebutDroit5AnsAvantDepotDemande;
    }

    public final Boolean getIsRemarqueRenteAvecMontantMinimumMajoreInvalidite() {
        return isRemarqueRenteAvecMontantMinimumMajoreInvalidite;
    }

    /**
     * @return the isRemarqueRenteDeVeufLimitee
     */
    public final Boolean getIsRemarqueRenteDeVeufLimitee() {
        return isRemarqueRenteDeVeufLimitee;
    }

    /**
     * @return the isRemarqueRenteDeVeuveLimitee
     */
    public final Boolean getIsRemarqueRenteDeVeuveLimitee() {
        return isRemarqueRenteDeVeuveLimitee;
    }

    /**
     * @return the isRemarqueRentePourEnfant
     */
    public final Boolean getIsRemarqueRentePourEnfant() {
        return isRemarqueRentePourEnfant;
    }

    public final Boolean getIsRemarqueRenteReduitePourSurassurance() {
        return isRemarqueRenteReduitePourSurassurance;
    }

    /**
     * Inforom 500 Renseigne si le document de décision généré doit contenir une remarque concernant les intérêts
     * moratoires
     * 
     * @return Si le document de décision généré doit contenir une remarque concernant les intérêts moratoires
     */
    public Boolean getIsRemInteretMoratoires() {
        return isRemInteretMoratoires;
    }

    public Boolean getIsRemRedPlaf() {
        return isRemRedPlaf;
    }

    public Boolean getIsRemSuppVeuf() {
        return isRemSuppVeuf;
    }

    public Boolean getIsSansBonneFoi() {
        return isSansBonneFoi;
    }

    /**
     * @return the preparePar
     */
    public String getPreparePar() {
        return preparePar;
    }

    public REPrestations getPrestation(BTransaction transaction) throws Exception {

        REPrestationsManager mgr = new REPrestationsManager();
        mgr.setSession(getSession());
        mgr.setForIdDecision(getIdDecision());
        mgr.find(transaction, 2);

        if (mgr.size() == 0) {
            throw new Exception("Aucune prestation trouvée pour cette décision. idDecision = " + getIdDecision());
        }

        if (mgr.size() > 1) {
            throw new Exception("Plusieurs prestations trouvées pour cette décision. idDecision = " + getIdDecision());
        }

        return (REPrestations) mgr.getEntity(0);
    }

    public String getRemarqueDecision() {
        return remarqueDecision;
    }

    public String getTraitePar() {
        return traitePar;
    }

    /**
     * @return the uuid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @return the validePar
     */
    public String getValidePar() {
        return validePar;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#hasCreationSpy()
     */
    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    public void setAdresseEMail(String adresseEMail) {
        this.adresseEMail = adresseEMail;
    }

    /**
     * @param csEtat
     *            the csEtat to set
     */
    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setCsGenreDecision(String csGenreDecision) {
        this.csGenreDecision = csGenreDecision;
    }

    /**
     * @param csTypeDecision
     *            the csTypeDecision to set
     */
    public void setCsTypeDecision(String csTypeDecision) {
        this.csTypeDecision = csTypeDecision;
    }

    /**
     * @param uuid
     *            the cuid to set
     */
    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    public void setDateDebutRetro(String dateDebutRetro) {
        this.dateDebutRetro = dateDebutRetro;
    }

    /**
     * @param dateDecision
     *            the dateDecision to set
     */
    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public void setDateFinRetro(String dateFinRetro) {
        this.dateFinRetro = dateFinRetro;
    }

    /**
     * @param datePreparation
     *            the datePreparation to set
     */
    public void setDatePreparation(String datePreparation) {
        this.datePreparation = datePreparation;
    }

    /**
     * @param dateValidation
     *            the dateValidation to set
     */
    public void setDateValidation(String dateValidation) {
        this.dateValidation = dateValidation;
    }

    /**
     * @param decisionDu
     *            the decisionDu to set
     */
    public void setDecisionDepuis(String decisionDepuis) {
        this.decisionDepuis = decisionDepuis;
    }

    /**
     * @param idDecision
     *            the idDecision to set
     */
    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    /**
     * @param idDemandeRente
     *            the idDemandeRente to set
     */
    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIdTiersAdrCourrier(String idTiersAdrCourrier) {
        this.idTiersAdrCourrier = idTiersAdrCourrier;
    }

    public void setIdTiersBeneficiairePrincipal(String idTiersBeneficiairePrincipal) {
        this.idTiersBeneficiairePrincipal = idTiersBeneficiairePrincipal;
    }

    public void setIsAvecBonneFoi(Boolean isAvecBonneFoi) {
        this.isAvecBonneFoi = isAvecBonneFoi;
    }

    public void setIsObliPayerCoti(Boolean isObliPayerCoti) {
        this.isObliPayerCoti = isObliPayerCoti;
    }

    public void setIsRemAnnDeci(Boolean isRemAnnDeci) {
        this.isRemAnnDeci = isRemAnnDeci;
    }

    public void setIsRemarqueIncarceration(Boolean isRemarqueIncarceration) {
        this.isRemarqueIncarceration = isRemarqueIncarceration;
    }

    /**
     * @param isRemarqueRemariageRenteDeSurvivant
     *            the isRemarqueRemariageRenteDeSurvivant to set
     */
    public final void setIsRemarqueRemariageRenteDeSurvivant(Boolean isRemarqueRemariageRenteDeSurvivant) {
        this.isRemarqueRemariageRenteDeSurvivant = isRemarqueRemariageRenteDeSurvivant;
    }

    public final void setIsRemarqueRenteAvecDebutDroit5AnsAvantDepotDemande(
            Boolean isRemarqueRenteAvecDebutDroit5AnsAvantDepotDemande) {
        this.isRemarqueRenteAvecDebutDroit5AnsAvantDepotDemande = isRemarqueRenteAvecDebutDroit5AnsAvantDepotDemande;
    }

    public final void setIsRemarqueRenteAvecMontantMinimumMajoreInvalidite(
            Boolean isRemarqueRenteAvecMontantMinimumMajoreInvalidite) {
        this.isRemarqueRenteAvecMontantMinimumMajoreInvalidite = isRemarqueRenteAvecMontantMinimumMajoreInvalidite;
    }

    /**
     * @param isRemarqueRenteDeVeufLimitee
     *            the isRemarqueRenteDeVeufLimitee to set
     */
    public final void setIsRemarqueRenteDeVeufLimitee(Boolean isRemarqueRenteDeVeufLimitee) {
        this.isRemarqueRenteDeVeufLimitee = isRemarqueRenteDeVeufLimitee;
    }

    /**
     * @param isRemarqueRenteDeVeuveLimitee
     *            the isRemarqueRenteDeVeuveLimitee to set
     */
    public final void setIsRemarqueRenteDeVeuveLimitee(Boolean isRemarqueRenteDeVeuveLimitee) {
        this.isRemarqueRenteDeVeuveLimitee = isRemarqueRenteDeVeuveLimitee;
    }

    /**
     * @param isRemarqueRentePourEnfant
     *            the isRemarqueRentePourEnfant to set
     */
    public final void setIsRemarqueRentePourEnfant(Boolean isRemarqueRentePourEnfant) {
        this.isRemarqueRentePourEnfant = isRemarqueRentePourEnfant;
    }

    public final void setIsRemarqueRenteReduitePourSurassurance(Boolean isRemarqueRenteReduitePourSurassurance) {
        this.isRemarqueRenteReduitePourSurassurance = isRemarqueRenteReduitePourSurassurance;
    }

    /**
     * Inforom 500 Définit si le document de décision généré doit contenir une remarque concernant les intérêts
     * moratoires
     * 
     * @param isRemInteretMoratoires
     */
    public void setIsRemInteretMoratoires(Boolean isRemInteretMoratoires) {
        this.isRemInteretMoratoires = isRemInteretMoratoires;
    }

    public void setIsRemRedPlaf(Boolean isRemRedPlaf) {
        this.isRemRedPlaf = isRemRedPlaf;
    }

    public void setIsRemSuppVeuf(Boolean isRemSuppVeuf) {
        this.isRemSuppVeuf = isRemSuppVeuf;
    }

    public void setIsSansBonneFoi(Boolean isSansBonneFoi) {
        this.isSansBonneFoi = isSansBonneFoi;
    }

    /**
     * @param preparePar
     *            the preparePar to set
     */
    public void setPreparePar(String preparePar) {
        this.preparePar = preparePar;
    }

    public void setRemarqueDecision(String remarqueDecision) {
        this.remarqueDecision = remarqueDecision;
    }

    public void setTraitePar(String traitePar) {
        this.traitePar = traitePar;
    }

    /**
     * @param uuid
     *            the uuid to set
     */
    public void setUid(String uuid) {
        uid = uuid;
    }

    /**
     * @param validePar
     *            the validePar to set
     */
    public void setValidePar(String validePar) {
        this.validePar = validePar;
    }

}
