/*
 * Créé le 24 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.pavo.db.compte;

import globaz.commons.nss.NSUtil;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.hermes.service.HEAttestationCAService;
import globaz.hermes.utils.HEUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pavo.application.CIApplication;
import globaz.pavo.service.dto.CIDataRemiseCA;
import globaz.pavo.util.CIUtil;

/**
 * Entity permettant de gérer des exceptions dans les déclarations de salaire
 * 
 * @author sda
 */
public class CIExceptions extends BEntity {

    public final static String ANNONCE_APP = "annonce";
    public final static String APPLICATIONID = "HERMES";
    public final static String DEFAULT_APPLICATION_PAVO = "PAVO";
    public final static String KEY_SESSION_HERMES = "sessionHermes";
    private static final long serialVersionUID = -34941896683865231L;
    /** (MALNAF) */
    private String affilie = "";
    private CICompteIndividuel compte = null;
    /** (DAENG) */
    private String dateEngagement = "";
    private String dateEnregistrement = "";
    /** (DALIC) */
    private String dateLicenciement = "";
    private String dateNaissance = "";
    /** (HTLDE1) */
    private String descriptionAffilieDebut = "";
    /** (HTLDE2) */
    private String descriptionAffilieFin = "";
    /** (MAIAFF) */
    private String idAffiliation = "";
    /** (KAIIND) */
    private String idCompteIndividuel = "";
    /** (IDEXCP) */
    private String idExceptions = "";
    private String isJsp = "false";

    /** KBNNS */
    private Boolean isNNSS = new Boolean(false);

    private String langueCorrespondance = "";
    private String nomJsp = "";

    /** (KALNOM) */
    private String nomPrenom = "";

    /** (KANAVS) */
    private String numeroAvs = "";
    private String numeroAvsNNSS = "";
    private String pays = "";
    private String sexe = "";
    private String wantCheckDoublon = "false";

    // Inforom 573
    private String warningEmployeurSansPersoOrAccountZero = "";

    public CIExceptions() {
        super();

    }

    /**
     * Cette méthode retourne une string paramétrée avec la valeur de warningEmployeurSansPersoOrAccountZero,
     * Ce getter est fait ainsi pour contourner un problème du framework et ne pas réafficher des messages
     * d'avertissements à l'utilisateur à tout moment
     * 
     * @return
     */
    public String getWarningEmployeurSansPersoOrAccountZero() {
        String warningToReturn = warningEmployeurSansPersoOrAccountZero;
        warningEmployeurSansPersoOrAccountZero = "";
        return warningToReturn;

    }

    public void setWarningEmployeurSansPersoOrAccountZero(String warningEmployeurSansPersoOrAccountZero) {
        this.warningEmployeurSansPersoOrAccountZero = warningEmployeurSansPersoOrAccountZero;
    }

    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        super._afterAdd(transaction);
        if (JadeStringUtil.equals(getIsJsp(), "true", true)) {
            imprimerAttestationCA();
        }
        // inforom 573 contrôler si c'est un employeur sans personnelle ou avec acompte AVS à zéro
        if (!HEUtil.checkAffSansPersoOrAccountAVSZero(getAffilie(), null, getSession())) {
            setWarningEmployeurSansPersoOrAccountZero(getSession().getLabel("DT_DETAIL_COL1") + " : "
                    + NSUtil.formatAVSUnknown(getNumeroAvs()) + " : "
                    + getSession().getLabel("PAVO_EMPLOYEUR_SANS_PERSONNEL"));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // Incrémente le compteur pour l'idExceptions
        if (JadeStringUtil.isIntegerEmpty(idExceptions)) {
            setIdExceptions(this._incCounter(transaction, "0"));
        }
        if (JadeStringUtil.isBlankOrZero(getDateEngagement())) {
            dateEngagement = "00.00." + JACalendar.getYear(JACalendar.todayJJsMMsAAAA());
        } else if (dateEngagement.trim().length() == 4) {
            dateEngagement = "00.00." + dateEngagement.trim();

        }
        // checker les doublons PO 8594
        if ("true".equals(wantCheckDoublon) && hasDoubles()) {
            _addError(transaction, FWMessageFormat.format(getSession().getLabel("EXCEPTION_AFFILIE_DOUBLON"),
                    numeroAvs, affilie, dateEngagement));
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String joinStr = new String();

        joinStr = " inner join " + _getCollection() + "AFAFFIP on " + _getCollection() + _getTableName() + ".MAIAFF="
                + _getCollection() + "AFAFFIP.MAIAFF inner join " + _getCollection() + "CIINDIP ON " + _getCollection()
                + _getTableName() + ".KAIIND=" + _getCollection() + "CIINDIP.KAIIND left outer join "
                + _getCollection() + "TITIERP on " + _getCollection() + "AFAFFIP.HTITIE=" + _getCollection()
                + "TITIERP.HTITIE";

        return _getCollection() + _getTableName() + joinStr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "CIEXCP";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idExceptions = statement.dbReadNumeric("IDEXCP");
        idCompteIndividuel = statement.dbReadNumeric("KAIIND");
        idAffiliation = statement.dbReadNumeric("MAIAFF");
        dateEngagement = statement.dbReadDateAMJ("DAENG");
        dateLicenciement = statement.dbReadDateAMJ("DALIC");
        affilie = statement.dbReadString("MALNAF");
        numeroAvs = statement.dbReadString("KANAVS");
        nomPrenom = statement.dbReadString("KALNOM");
        dateNaissance = statement.dbReadDateAMJ("KADNAI");
        sexe = statement.dbReadNumeric("KATSEX");
        pays = statement.dbReadNumeric("KAIPAY");
        descriptionAffilieDebut = statement.dbReadString("HTLDE1");
        descriptionAffilieFin = statement.dbReadString("HTLDE2");
        isNNSS = statement.dbReadBoolean("KABNNS");
        if (isNNSS.booleanValue()) {
            numeroAvsNNSS = "true";
        } else {
            numeroAvsNNSS = "false";
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        if ("true".equalsIgnoreCase(numeroAvsNNSS)) {
            isNNSS = new Boolean(true);
        } else {
            isNNSS = new Boolean(false);
        }
        // On teste que le numéro Avs ne soit pas vide
        if (JadeStringUtil.isBlank(getNumeroAvs())) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_COMPTE_INDIVIDUEL_VIDE"));
        }
        // On teste que le numéro d'affilié ne soit pas vide
        if (JadeStringUtil.isBlank(getAffilie())) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_AFFILIE_VIDE"));
        }
        // On oblige la saisie d'une date de début dans les exceptions
        if (JadeStringUtil.isBlank(getDateEngagement())) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_DATE_ENGAGEMENT_VIDE"));
        }
        // On stocke le numéro AVS sans les points pour éviter une erreur de
        // formatage lors du déclenchement d'une plausibilité
        setNumeroAvs(CIUtil.unFormatAVS(getNumeroAvs()));
        // On teste que la date d'engagement soit valide
        if ((JadeStringUtil.isBlank(getDateEngagement()) && !JadeStringUtil.isBlank(getDateLicenciement()))
                || (BSessionUtil.compareDateFirstGreater(getSession(), dateEngagement, dateLicenciement) && !JadeStringUtil
                        .isBlank(dateLicenciement))) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_DATE_LICENCIEMENT_NON_VALIDE"));
        }
        // Set l'id de l'affilie dans le champ et teste que l'affilié
        // sélectionné corresponde bien à la bonne affiliation
        CIApplication mgrAffilie;

        mgrAffilie = new CIApplication();
        AFAffiliation numeroAffiliation = new AFAffiliation();
        // On prend la bonne affiliation en fonction de la date d'engagement
        numeroAffiliation = mgrAffilie.getAffilieByNo(getSession(), affilie, true, false,
                String.valueOf(JACalendar.getMonth(dateEngagement)),
                String.valueOf(JACalendar.getMonth(dateEngagement)),
                String.valueOf(JACalendar.getYear(dateEngagement)), String.valueOf(JACalendar.getDay(dateEngagement)),
                String.valueOf(JACalendar.getDay(dateEngagement)));
        // Ensuite on compare l'affiliation sélectionnée avec la date de
        // licenciement
        if ((numeroAffiliation == null)
                || (BSessionUtil
                        .compareDateFirstGreater(getSession(), dateLicenciement, numeroAffiliation.getDateFin())
                        && (dateLicenciement.length() != 0) && (numeroAffiliation.getDateFin().length() != 0))) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_AFFILIE_NON_VALIDE"));
        } else {

            // on set l'id de l'affilié dans le champs
            setIdAffiliation(numeroAffiliation.getAffiliationId());

        }
        // Set l'id du compte individuel dans le champ et teste que l'assuré
        // sélectionné soit correct
        CICompteIndividuelManager compteManager = new CICompteIndividuelManager();
        compteManager.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        compteManager.setSession(getSession());
        compteManager.setForNumeroAvs(CIUtil.unFormatAVS(numeroAvs));
        compteManager.find();
        // Si compteManager est vide c'est que l'assuré entré n'est pas valide
        if (compteManager.size() == 0) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_ASSURE_NON_VALIDE"));
        }
        compte = new CICompteIndividuel();
        compte = (CICompteIndividuel) compteManager.getFirstEntity();
        // On set l'id de l'assuré dans le champ
        setIdCompteIndividuel(compte.getCompteIndividuelId());

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("IDEXCP", this._dbWriteNumeric(statement.getTransaction(), getIdExceptions(), ""));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("IDEXCP",
                this._dbWriteNumeric(statement.getTransaction(), getIdExceptions(), "idExceptions"));
        statement.writeField("KAIIND",
                this._dbWriteNumeric(statement.getTransaction(), getIdCompteIndividuel(), "idCompteIndividuel"));
        statement.writeField("MAIAFF",
                this._dbWriteNumeric(statement.getTransaction(), getIdAffiliation(), "idAffiliation"));
        statement.writeField("DAENG",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateEngagement(), "dateEngagement"));
        statement.writeField("DALIC",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateLicenciement(), "dateLicenciement"));
    }

    /**
     * Retourne le numéro de l'affilie
     * 
     * @return affilie
     */
    public String getAffilie() {
        return affilie;
    }

    /**
     * Retourne le numéro de l'affilié ainsi que la description complète
     * 
     * @return affilie + descriptionAffilieDebut + descriptionAffilieFin
     */
    public String getAffilieDesignation() {
        return affilie + " " + descriptionAffilieDebut + " " + descriptionAffilieFin;
    }

    public BIApplication getApplication(String application, BSession session) throws Exception {
        return GlobazSystem.getApplication(CIExceptions.APPLICATIONID);
    }

    /**
     * Retourne le numéro Avs ainsi que le nom et le prénom de l'assuré
     * 
     * @return numeroAvs + nomPrenom
     */
    public String getAvsNomPrenom() {
        return NSUtil.formatAVSNew(numeroAvs, isNNSS.booleanValue()) + " " + nomPrenom;
    }

    public CICompteIndividuel getCompte() {
        return compte;
    }

    /**
     * Renvoie la date d'engagement
     * 
     * @return dateEngagement
     */
    public String getDateEngagement() {
        return dateEngagement;
    }

    public String getDateEnregistrement() {
        return dateEnregistrement;
    }

    /**
     * Renvoie la date de licenciement
     * 
     * @return dateLicenciement
     */
    public String getDateLicenciement() {
        return dateLicenciement;
    }

    /**
     * @return
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * Renvoie le début de la description de l'affilié
     * 
     * @return descriptionAffilieDebut
     */
    public String getDescriptionAffilieDebut() {
        return descriptionAffilieDebut;
    }

    /**
     * Renvoie la fin de la description de l'affilié
     * 
     * @return descriptionAffilieFin
     */
    public String getDescriptionAffilieFin() {
        return descriptionAffilieFin;
    }

    /**
     * Renvoie l'id de l'afiflié
     * 
     * @return idAffiliation;
     */
    public String getIdAffiliation() {
        return idAffiliation;
    }

    /**
     * Renvoie l'id du compte individuel
     * 
     * @return idCompteIndividuel
     */
    public String getIdCompteIndividuel() {
        return idCompteIndividuel;
    }

    /**
     * Renvoie l'id de la table exceptions
     * 
     * @return
     */
    public String getIdExceptions() {
        return idExceptions;
    }

    /**
     * Retourne la description complète de l'affilié
     * 
     * @return descriptionAffilieDebut et descriptionAffilieFin
     */
    public String getInfoAffilie() {
        return descriptionAffilieDebut + " " + descriptionAffilieFin;
    }

    /**
     * Retourne le nom et le prénom de l'assuré
     * 
     * @return nomPrenom
     */
    public String getInfoAssure() {
        return nomPrenom;
    }

    public String getIsJsp() {
        return isJsp;
    }

    /**
     * @return
     */
    public Boolean getIsNNSS() {
        return isNNSS;
    }

    public String getLangueCorrespondance() {
        return langueCorrespondance;
    }

    public String getNomJsp() {
        return nomJsp;
    }

    /**
     * Retourne le nom et le prénom de l'assuré
     * 
     * @return nomPrenom
     */
    public String getNomPrenom() {
        return nomPrenom;
    }

    /**
     * Retourne le numéro Avs
     * 
     * @return numeroAvs
     */
    public String getNumeroAvs() {
        return numeroAvs;
    }

    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(numeroAvs, "true".equalsIgnoreCase(numeroAvsNNSS));
    }

    /**
     * @return
     */
    public String getNumeroAvsNNSS() {
        return numeroAvsNNSS;
    }

    /**
     * @return
     */
    public String getPays() {
        return pays;
    }

    public String getPaysCode() {
        return getSession().getCode(getPays());
    }

    public String getPaysFormate() {
        return getSession().getCode(getPays()) + " - " + getSession().getCodeLibelle(getPays());
    }

    public BISession getSessionAnnonce(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute(CIExceptions.KEY_SESSION_HERMES);
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = getApplication(CIExceptions.ANNONCE_APP, local).newSession(local);
            local.setAttribute(CIExceptions.KEY_SESSION_HERMES, remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    /**
     * @return
     */
    public String getSexe() {
        return sexe;
    }

    public String getSexeForNNSS() {
        if ("fr".equals(getSession().getIdLangueISO())) {
            if (CICompteIndividuel.CS_FEMME.equals(getSexe())) {
                return "F";
            } else {
                return "H";
            }
        } else if ("de".equals(getSession().getIdLangueISO())) {
            if (CICompteIndividuel.CS_FEMME.equals(getSexe())) {
                return "F";
            } else {
                return "H";
            }
        }
        return "";

    }

    public String getSexeLibelle() {
        return getSession().getCodeLibelle(getSexe());
    }

    /**
     * Cette méthode permet de s'assurer qu'une exception n'a pas de doublons
     * 
     * @return boolean hasDoubles : true s'il y a un ou plusieurs doublons, false sinon
     * @throws Exception
     */
    private boolean hasDoubles() throws Exception {
        boolean hasdouble = false;
        // Contrôle des doublons dans les exceptions
        CIExceptionsManager ciExceptionManager = new CIExceptionsManager();
        // doit être renseigné dans tous les cas selon _validate
        ciExceptionManager.setForNumeroAvs(getNumeroAvs());
        // doit être renseigné dans tous les cas selon _validate
        ciExceptionManager.setForNumeroAffilie(getAffilie());
        // si la date d'enregistrement n'est pas settée, une date est settée automatiquement.
        ciExceptionManager.setForDateEngagement(getDateEngagement());
        // la date de licenciement peut être vide
        if (!JadeStringUtil.isBlankOrZero(getDateLicenciement())) {
            ciExceptionManager.setForDateLicenciement(getDateLicenciement());
        }
        ciExceptionManager.setSession(getSession());
        ciExceptionManager.find();

        if (ciExceptionManager.size() >= 1) {
            hasdouble = true;
        }
        return hasdouble;
    }

    /**
     * Impression de l'attestation CA
     * 
     * @throws Exception
     */
    private void imprimerAttestationCA() throws Exception {

        CIDataRemiseCA dataRemiseCA = new CIDataRemiseCA();
        dataRemiseCA.peupler(this);

        BSession sessionHermes = (BSession) getSessionAnnonce(getSession());
        String userEmail = getSession().getUserEMail();

        HEAttestationCAService.soumettreGenerationAttestationRemiseCA(dataRemiseCA, sessionHermes, userEmail);

    }

    /**
     * Sette le numéro de l'affilié
     * 
     * @param string
     */
    public void setAffilie(String string) {
        affilie = string;
    }

    /**
     * Sette la date d'engagement
     * 
     * @param string
     */
    public void setDateEngagement(String string) {
        dateEngagement = string;
    }

    public void setDateEnregistrement(String dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    /**
     * Sette la date de licenciement
     * 
     * @param string
     */
    public void setDateLicenciement(String string) {
        dateLicenciement = string;
    }

    /**
     * @param string
     */
    public void setDateNaissance(String string) {
        dateNaissance = string;
    }

    /**
     * Sette le début de la description de l'affilié
     * 
     * @param string
     */
    public void setDescriptionAffilieDebut(String string) {
        descriptionAffilieDebut = string;
    }

    /**
     * Sette la fin de la description de l'affilié
     * 
     * @param string
     */
    public void setDescriptionAffilieFin(String string) {
        descriptionAffilieFin = string;
    }

    /**
     * Sette l'id de l'affilié
     * 
     * @param string
     */
    public void setIdAffiliation(String string) {
        idAffiliation = string;
    }

    /**
     * Sette l'id du compte individuel
     * 
     * @param string
     */
    public void setIdCompteIndividuel(String string) {
        idCompteIndividuel = string;
    }

    /**
     * Sette l'id de la table exceptions
     * 
     * @param string
     */
    public void setIdExceptions(String string) {
        idExceptions = string;
    }

    public void setIsJsp(String isJsp) {
        this.isJsp = isJsp;
    }

    /**
     * @param boolean1
     */
    public void setIsNNSS(Boolean boolean1) {
        isNNSS = boolean1;
    }

    public void setLangueCorrespondance(String langueCorrespondance) {
        this.langueCorrespondance = langueCorrespondance;
    }

    public void setNomJsp(String nomJsp) {
        this.nomJsp = nomJsp;
    }

    /**
     * Sette le nom et le prénom de l'assuré
     * 
     * @param string
     */
    public void setNomPrenom(String string) {
        nomPrenom = string;
    }

    /**
     * Sette le numéro Avs
     * 
     * @param string
     */
    public void setNumeroAvs(String string) {
        numeroAvs = string;
    }

    /**
     * @param string
     */
    public void setNumeroAvsNNSS(String string) {
        numeroAvsNNSS = string;
    }

    /**
     * @param string
     */
    public void setPays(String string) {
        pays = string;
    }

    /**
     * @param string
     */
    public void setSexe(String string) {
        sexe = string;
    }

    public String getWantCheckDoublon() {
        return wantCheckDoublon;
    }

    public void setWantCheckDoublon(String wantCheckDoublon) {
        this.wantCheckDoublon = wantCheckDoublon;
    }

}
