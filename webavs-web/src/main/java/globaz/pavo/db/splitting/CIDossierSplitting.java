package globaz.pavo.db.splitting;

import java.util.HashMap;
import globaz.commons.nss.NSUtil;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.secure.user.FWSecureUser;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.db.inscriptions.CIRemarque;
import globaz.pavo.print.itext.CIAnalyseSplitting;
import globaz.pavo.print.itext.CISplittingAccuseReception_Doc;
import globaz.pavo.print.itext.CISplittingApercuAndLettreAccompagnementMergeProcess;
import globaz.pavo.print.itext.CISplittingInvitationExConjoint_Doc;
import globaz.pavo.util.CIUtil;
import globaz.pyxis.api.ITIPersonneAvs;

/**
 * Classe représentant un dossier de splitting. Date de création : (14.10.2002 15:43:18)
 *
 * @author: dgi
 */
public class CIDossierSplitting extends BEntity implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // Type de personne
    public final static String ASSURE = "0";
    public final static String CONJOINT = "1";

    public final static String CS_A_TRAITER = "305012";

    public final static String CS_ANNULE = "305013";

    public final static String CS_CLOTURE = "305009";

    public final static String CS_DEMANDE_AUTRE_CAISSE = "308003";
    public final static String CS_DEMANDE_CA = "305008";
    public final static String CS_DEMANDE_OUVERTURE_CI = "305002";
    // Motif du splitting
    public final static String CS_DEMANDE_PROPRE_CAISSE = "308001";
    public final static String CS_DEMANDE_REVOCATION = "305010";
    public final static String CS_ERREUR = "305014";
    public final static String CS_EXTRAIT_EN_COURS = "305003";
    public final static String CS_OUVERT = "305005";
    public final static String CS_OUVERTURE_DOSSIER = "305001";
    public final static String CS_RCI_COMPLET = "305004";
    public final static String CS_RENTES = "308002";
    public final static String CS_REVOQUE = "305011";
    public final static String CS_SAISIE_DOSSIER = "305000";
    public final static String CS_SPLITTING_COMPLET = "305007";
    public final static String CS_SPLITTING_EN_COURS = "305006";
    public final static String USER_ACTION_SUPPRIMER_SPLITTING = "pavo.splitting.dossierSplitting.supprimer";
    private String adresseAssure = new String();
    private String adresseExConjoint = new String();
    private java.lang.Boolean caRequisAssure = new Boolean(false);

    private java.lang.Boolean caRequisConjoint = new Boolean(false);
    private Boolean chkAccuseReceptionAssure = new Boolean(false);
    private Boolean chkInvitationExConjoint = new Boolean(false);
    private Boolean chkLettreAccompagnementAssure = new Boolean(false);
    private Boolean chkLettreAccompagnementExConjoint = new Boolean(false);
    private CICompteIndividuel ciAssureForNNSS = null;
    private CICompteIndividuel ciConjointForNNSS = null;
    // container pour écritures de splitting
    private CIEcrituresSplittingContainer container;
    private java.lang.String dateDivorce = new String();
    private java.lang.String dateMariage = new String();
    private java.lang.String dateOuvertureDossier = new String();
    private String duree;
    private java.lang.Boolean estTaxationDefinitive = new Boolean(false);
    private java.lang.Boolean extraitCiRequisAssure = new Boolean(false);
    private java.lang.Boolean extraitCiRequisConjoint = new Boolean(false);
    // flag pour exécution forcée du splitting
    private boolean forceExecute = false;
    private java.lang.String idArc33Assure = new String();
    private java.lang.String idArc33Conjoint = new String();
    private java.lang.String idArc65Assure = new String();
    private java.lang.String idArc65Conjoint = new String();
    private java.lang.String idArc98Assure = new String();
    private java.lang.String idArc98Conjoint = new String();
    private java.lang.String idDossierInterne = new String();
    // Attributs
    private java.lang.String idDossierSplitting = new String();
    private java.lang.String idEtat = new String();
    private java.lang.String idMotifSplitting = new String();
    // localités
    // private String tiersAssureLocalite = new String();
    // private String tiersConjointLocalite = new String();
    // en-tête aperçu mandats
    private String idPartenaire;
    private java.lang.String idRemarque = new String();
    private java.lang.String idTiersAssure = new String();
    private String idTiersAssureNNSS = "";
    private java.lang.String idTiersConjoint = new String();
    private String idTiersConjointNNSS = "";
    private Boolean isArchivage = new Boolean(false);
    private String langueAssure = new String();
    private String langueExConjoint = new String();
    private String referenceService = "";
    private String remarqueDossier = new String();
    private java.lang.String responsableDossier = new String();
    private String tiersAssureInfo = new String();
    private String tiersAssureLocalite = new String();
    // sauvegarde des info tiers
    private String tiersAssureNom = new String();
    private String tiersConjointInfo = new String();
    private String tiersConjointLocalite = new String();

    private String tiersConjointNom = new String();
    private String tiersPartenaireNomComplet = new String();

    private String titreAssure = new String();
    private String titreExConjoint = new String();

    private String typePersonne;
    // noms
    private String utilisateurNomComplet = new String(); // pas utilisé

    /**
     * Constructeur.
     */
    public CIDossierSplitting() {
        super();
        // date ouverture
        setDateOuvertureDossier(JACalendar.todayJJsMMsAAAA());
        // état
        setIdEtat(CIDossierSplitting.CS_SAISIE_DOSSIER);
        // motif par defaut
        setIdMotifSplitting(CIDossierSplitting.CS_DEMANDE_PROPRE_CAISSE);

    }

    /**
     * Effectue des traitements après une lecture dans la BD et après avoir vidé le tampon de lecture
     *
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws java.lang.Exception {
        // info tiers
        loadInfoTiers(transaction);
    }

    /**
     * Supprime tous les mandats et domiciles associés au dossier
     *
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
    }

    /**
     * Effectue des traitements après une lecture dans la BD et après avoir vidé le tampon de lecture
     *
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws java.lang.Exception {
        // info tiers
        loadInfoTiers(transaction);
    }

    /**
     * Effectue des traitements après une lecture dans la BD et après avoir vidé le tampon de lecture
     *
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterRetrieveWithResultSet(BStatement statement) throws java.lang.Exception {
        // recherche de la remarque
        if (!JadeStringUtil.isBlankOrZero(getIdRemarque())) {
            CIRemarque rem = new CIRemarque();
            rem.setIdRemarque(getIdRemarque());
            rem.setSession(getSession());
            // rem.read(statement);
            rem.retrieve(statement.getTransaction());
            setRemarqueDossier(rem.getTexte());
        }
        // cherche le nom complet du responsable du dossier (pas nécessaire pour
        // l'instant)
        // chargeNomComplet(transaction);
    }

    /**
     * Effectue des traitements après une lecture dans la BD et après avoir vidé le tampon de lecture
     *
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws java.lang.Exception {
        // info tiers
        loadInfoTiers(transaction);
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdDossierSplitting(this._incCounter(transaction, "0"));
        // id assuré
        // setIdTiersAssure(JAStringFormatter.unFormatAVS(tiersAssureInfo));
        // id conjoint
        // setIdTiersConjoint(JAStringFormatter.unFormatAVS(tiersConjointInfo));
        // ajout de la remarque si non vide
        if (getRemarqueDossier().length() != 0) {
            CIRemarque rem = new CIRemarque();
            rem.setSession(getSession());
            rem.setIdDossierSplitting(getIdDossierSplitting());
            rem.setTexte(getRemarqueDossier());
            rem.add(transaction);
            setIdRemarque(rem.getIdRemarque());
        }
        if (!NSUtil.nssCheckDigit(idTiersAssure) && (NSUtil.unFormatAVS(idTiersAssure).trim().length() == 13)) {
            _addError(transaction, getSession().getLabel("MSG_NSS_ASSURE_INVALIDE"));
        }
        if (!NSUtil.nssCheckDigit(idTiersConjoint) && (NSUtil.unFormatAVS(idTiersConjoint).trim().length() == 13)) {
            _addError(transaction, getSession().getLabel("MSG_NSS_CONJOINT_INVALIDE"));
        }

    }

    /**
     * Teste si la supression est autorisée.
     *
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        if (CIDossierSplitting.CS_SPLITTING_EN_COURS.equals(idEtat)
                || CIDossierSplitting.CS_SPLITTING_COMPLET.equals(idEtat)
                || CIDossierSplitting.CS_DEMANDE_CA.equals(idEtat) || CIDossierSplitting.CS_CLOTURE.equals(idEtat)
                || CIDossierSplitting.CS_DEMANDE_REVOCATION.equals(idEtat)) {
            // suppression impossible
            _addError(transaction, getSession().getLabel("MSG_DOSSIER_DEL_ETAT"));
            return;
        }
        if (getSession() == null) {
            _addError(transaction, getSession().getLabel("MSG_DOSSIER_DEL_USER"));
        } else {
            // role responsable ci
            String role = ((CIApplication) getSession().getApplication()).getRoleResponsableCI();
            String[] roleUser = JadeAdminServiceLocatorProvider.getLocator().getRoleService()
                    .findAllIdRoleForIdUser(getSession().getUserId());
            boolean hasRight = false;
            for (int i = 0; i < roleUser.length; i++) {
                if (role.trim().equals(roleUser[i])) {
                    hasRight = true;
                    break;
                }
            }

            if (!hasRight && !getUser(transaction).getVisa().trim().equals(getSession().getUserId().trim())) {
                // pas l'authorisation
                _addError(transaction, getSession().getLabel("MSG_DOSSIER_DEL_USER"));
            }
        }
        if (!transaction.hasErrors()) {
            // effacement mandat
            CIMandatSplittingManager mandats = new CIMandatSplittingManager();
            mandats.setForIdDossierSplitting(getIdDossierSplitting());
            mandats.setSession(getSession());
            mandats.find(transaction);
            for (int i = 0; i < mandats.size(); i++) {
                BEntity _entity = (BEntity) mandats.getEntity(i);
                _entity.delete(transaction);
            }
            // effacement domiciles
            CIDomicileSplittingManager domiciles = new CIDomicileSplittingManager();
            domiciles.setForIdDossierSplitting(getIdDossierSplitting());
            domiciles.setSession(getSession());
            domiciles.find(transaction);
            for (int i = 0; i < domiciles.size(); i++) {
                BEntity _entity = (BEntity) domiciles.getEntity(i);
                _entity.delete(transaction);
            }
            // effacement de la remarque
            if (!JAUtil.isStringEmpty(getIdRemarque())) {
                CIRemarque rem = new CIRemarque();
                rem.setSession(getSession());
                rem.setIdRemarque(getIdRemarque());
                rem.retrieve(transaction);
                if (!rem.isNew()) {
                    rem.delete(transaction);
                }
            }
        }
    }

    /**
     * Effectue des traitements avant une mise à jour dans la BD
     *
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws java.lang.Exception {
        // modification de la remarque
        CIRemarque rem = new CIRemarque();
        rem.setSession(getSession());
        if (!JAUtil.isStringEmpty(getIdRemarque())) {
            // remarque existe déjà
            rem.setIdRemarque(getIdRemarque());
            rem.retrieve(transaction);
        }
        if (getRemarqueDossier().length() != 0) {
            // si remarque donnée, sauver la remarque
            rem.setTexte(getRemarqueDossier());
            rem.setIdDossierSplitting(getIdDossierSplitting());
            rem.save(transaction);
            setIdRemarque(rem.getIdRemarque());
        } else {
            if (!rem.isNew()) {
                // sinon pas de remarque mais une ancienne existait: l'effacer.
                rem.delete(transaction);
                setIdRemarque("0");
            }
        }
    }

    /**
     * Renvoie la clause FROM
     *
     * @return la clause FROM
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + _getTableName() + " LEFT OUTER JOIN " + _getCollection() + "CIREMAP ON "
                + _getCollection() + _getTableName() + ".KDID=" + _getCollection() + "CIREMAP.KDID";
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CISPDSP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idDossierSplitting = statement.dbReadNumeric("KDID");
        idDossierInterne = statement.dbReadNumeric("KDIDIN");
        idRemarque = statement.dbReadNumeric("KIIREM");
        idTiersAssure = statement.dbReadNumeric("KDITI1");
        idTiersConjoint = statement.dbReadNumeric("KDITI2");
        dateOuvertureDossier = statement.dbReadDateAMJ("KDDATE");
        dateMariage = statement.dbReadDateAMJ("KDDMAR");
        dateDivorce = statement.dbReadDateAMJ("KDDDIV");
        responsableDossier = statement.dbReadString("KDUSER");
        extraitCiRequisAssure = statement.dbReadBoolean("KDB98A");
        extraitCiRequisConjoint = statement.dbReadBoolean("KDB98P");
        caRequisAssure = statement.dbReadBoolean("KDB33A");
        caRequisConjoint = statement.dbReadBoolean("KDB33P");
        idMotifSplitting = statement.dbReadNumeric("KDIMSP");
        estTaxationDefinitive = statement.dbReadBoolean("KDBTAX");
        idArc65Assure = statement.dbReadNumeric("KDI65A");
        idArc65Conjoint = statement.dbReadNumeric("KDI65P");
        idArc98Assure = statement.dbReadNumeric("KDI98A");
        idArc98Conjoint = statement.dbReadNumeric("KDI98P");
        idArc33Assure = statement.dbReadNumeric("KDI33A");
        idArc33Conjoint = statement.dbReadNumeric("KDI33P");
        idEtat = statement.dbReadNumeric("KDIETA");
        if (getIdTiersAssure().trim().length() == 13) {
            idTiersAssureNNSS = "true";
        } else {
            idTiersAssureNNSS = "false";
        }
        if (getIdTiersConjoint().trim().length() == 13) {
            idTiersConjointNNSS = "true";
        } else {
            idTiersConjointNNSS = "false";
        }
        referenceService = statement.dbReadString("KDREF");
        chkAccuseReceptionAssure = statement.dbReadBoolean("KDCKAA");
        chkInvitationExConjoint = statement.dbReadBoolean("KDCKIC");
        adresseAssure = statement.dbReadString("KDADRA");
        adresseExConjoint = statement.dbReadString("KDADRC");

        titreAssure = statement.dbReadNumeric("KDTITA");
        titreExConjoint = statement.dbReadNumeric("KDTITC");

        langueAssure = statement.dbReadNumeric("KDLANA");
        langueExConjoint = statement.dbReadNumeric("KDLANC");

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        // Assuré
        // if (tiersAssureInfo.trim().length() <= 14) {
        // modification de l'id
        // setIdTiersAssure(CIUtil.unFormatAVS(tiersAssureInfo));
        // }
        if (!"true".equalsIgnoreCase(idTiersAssureNNSS)) {
            _checkAVS(statement.getTransaction(), idTiersAssure, getSession().getLabel("MSG_DOSSIER_VAL_TIERS"));
        }
        // Conjoint
        // if (tiersConjointInfo.trim().length() <= 14) {
        // modification de l'id
        // setIdTiersConjoint(CIUtil.unFormatAVS(tiersConjointInfo));
        // }
        if (!"true".equalsIgnoreCase(idTiersConjointNNSS)) {
            _checkAVS(statement.getTransaction(), idTiersConjoint, getSession().getLabel("MSG_DOSSIER_VAL_CONJOINT"));
        }
        if (idTiersAssure.equals(idTiersConjoint)) {
            // assuré et conjoitn identiques
            _addError(statement.getTransaction(), getSession().getLabel("MSG_DOSSIER_VAL_AVS_IDEM"));
        }
        // Dates
        if (JAUtil.isDateEmpty(dateOuvertureDossier)) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_DOSSIER_VAL_OUVERTURE"));
        }
        if (JAUtil.isDateEmpty(dateMariage)) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_DOSSIER_VAL_MARIAGE"));
        }
        if (JAUtil.isDateEmpty(dateDivorce)) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_DOSSIER_VAL_DIVORCE"));
        }
        if (!statement.getTransaction().hasErrors()) {
            this.checkWithCI(statement.getTransaction());
        }
        if (!statement.getTransaction().hasErrors()) {
            try {
                if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dateMariage, dateDivorce)) {
                    // divorce avant mariage
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_DOSSIER_VAL_DATE"));
                }
                if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(),
                        "31.12." + String.valueOf(JACalendar.today().getYear()), dateDivorce)) {
                    // divorce plus tard que cette année
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_DOSSIER_VAL_DATE_DIV"));
                }
            } catch (Exception e) {
                _addError(statement.getTransaction(), e.getMessage());
            }
        }
        if (!statement.getTransaction().hasErrors()) {
            if (JAUtil.isStringEmpty(responsableDossier)) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_DOSSIER_VAL_RESP"));
            } else {
                // existe dans bd?
                JadeUser user = getUser(statement.getTransaction());
                // TODO A tester si user = null au cas ou i ln'existe pas
                if (user == null) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_DOSSIER_VAL_RESP"));
                }
            }
        }

        // ici on test uniquement la validité du numéro avs s'il s'agit d'un numéro avs dans le nouveau format
        // les numéros avs dans l'ancien format sont testés plus haut
        if ("true".equalsIgnoreCase(idTiersAssureNNSS) && !NSUtil.nssCheckDigit(idTiersAssure)) {
            _addError(statement.getTransaction(), getSession().getLabel("ERREUR_ASSURE_NSS_NOT_VALID"));
        }

        // ici on test uniquement la validité du numéro avs s'il s'agit d'un numéro avs dans le nouveau format
        // les numéros avs dans l'ancien format sont testés plus haut
        if ("true".equalsIgnoreCase(idTiersConjointNNSS) && !NSUtil.nssCheckDigit(idTiersConjoint)) {
            _addError(statement.getTransaction(), getSession().getLabel("ERREUR_EXCONJOINT_NSS_NOT_VALID"));
        }

        if (getChkAccuseReceptionAssure() && JadeStringUtil.isBlankOrZero(getAdresseAssure())) {
            _addError(statement.getTransaction(), getSession().getLabel("ERREUR_ASSURE_ADRESSE_EMPTY"));
        }

        if (getChkInvitationExConjoint() && JadeStringUtil.isBlankOrZero(getAdresseExConjoint())) {
            _addError(statement.getTransaction(), getSession().getLabel("ERREUR_EXCONJOINT_ADRESSE_EMPTY"));
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(_getCollection() + _getTableName() + ".KDID",
                this._dbWriteNumeric(statement.getTransaction(), getIdDossierSplitting(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("KDID",
                this._dbWriteNumeric(statement.getTransaction(), getIdDossierSplitting(), "idDossierSplitting"));
        statement.writeField("KDIDIN",
                this._dbWriteNumeric(statement.getTransaction(), getIdDossierInterne(), "idDossierInterne"));
        statement.writeField("KIIREM", this._dbWriteNumeric(statement.getTransaction(), getIdRemarque(), "idRemarque"));
        statement.writeField("KDITI1",
                this._dbWriteNumeric(statement.getTransaction(), getIdTiersAssure(), "idTiersAssure"));
        statement.writeField("KDITI2",
                this._dbWriteNumeric(statement.getTransaction(), getIdTiersConjoint(), "idTiersConjoint"));
        statement.writeField("KDDATE",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateOuvertureDossier(), "dateOuvertureDossier"));
        statement.writeField("KDDMAR",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateMariage(), "dateMariage"));
        statement.writeField("KDDDIV",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateDivorce(), "dateDivorce"));
        statement.writeField("KDUSER",
                this._dbWriteString(statement.getTransaction(), getResponsableDossier(), "responsableDossier"));
        statement.writeField("KDB98A", this._dbWriteBoolean(statement.getTransaction(), isExtraitCiRequisAssure(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "extraitCiRequisAssure"));
        statement.writeField("KDB98P", this._dbWriteBoolean(statement.getTransaction(), isExtraitCiRequisConjoint(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "extraitCiRequisConjoint"));
        statement.writeField("KDB33A", this._dbWriteBoolean(statement.getTransaction(), isCaRequisAssure(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "caRequisAssure"));
        statement.writeField("KDB33P", this._dbWriteBoolean(statement.getTransaction(), isCaRequisConjoint(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "caRequisConjoint"));
        statement.writeField("KDIMSP",
                this._dbWriteNumeric(statement.getTransaction(), getIdMotifSplitting(), "idMotifSplitting"));
        statement.writeField("KDBTAX", this._dbWriteBoolean(statement.getTransaction(), isEstTaxationDefinitive(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "estTaxationDefinitive"));
        statement.writeField("KDI65A",
                this._dbWriteNumeric(statement.getTransaction(), getIdArc65Assure(), "idArc65Assure"));
        statement.writeField("KDI65P",
                this._dbWriteNumeric(statement.getTransaction(), getIdArc65Conjoint(), "idArc65Conjoint"));
        statement.writeField("KDI98A",
                this._dbWriteNumeric(statement.getTransaction(), getIdArc98Assure(), "idArc98Assure"));
        statement.writeField("KDI98P",
                this._dbWriteNumeric(statement.getTransaction(), getIdArc98Conjoint(), "idArc98Conjoint"));
        statement.writeField("KDI33A",
                this._dbWriteNumeric(statement.getTransaction(), getIdArc33Assure(), "idArc33Assure"));
        statement.writeField("KDI33P",
                this._dbWriteNumeric(statement.getTransaction(), getIdArc33Conjoint(), "idArc33Conjoint"));
        statement.writeField("KDIETA", this._dbWriteNumeric(statement.getTransaction(), getIdEtat(), "idEtat"));
        statement.writeField("KDREF",
                this._dbWriteString(statement.getTransaction(), getReferenceService(), "referenceService"));

        statement.writeField("KDCKAA", this._dbWriteBoolean(statement.getTransaction(), getChkAccuseReceptionAssure(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "chkAccuseReceptionAssure"));
        statement.writeField("KDCKIC", this._dbWriteBoolean(statement.getTransaction(), getChkInvitationExConjoint(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "chkInvitationExConjoint"));

        statement.writeField("KDADRA",
                this._dbWriteString(statement.getTransaction(), getAdresseAssure(), "adresseAssure"));
        statement.writeField("KDADRC",
                this._dbWriteString(statement.getTransaction(), getAdresseExConjoint(), "adresseExConjoint"));
        statement.writeField("KDTITA",
                this._dbWriteNumeric(statement.getTransaction(), getTitreAssure(), "titreAssure"));
        statement.writeField("KDTITC",
                this._dbWriteNumeric(statement.getTransaction(), getTitreExConjoint(), "titreExConjoint"));
        statement.writeField("KDLANA",
                this._dbWriteNumeric(statement.getTransaction(), getLangueAssure(), "langueAssure"));
        statement.writeField("KDLANC",
                this._dbWriteNumeric(statement.getTransaction(), getLangueExConjoint(), "langueExConjoint"));
    }

    /**
     * met le statut annulé
     *
     * @throws Exception
     *
     */
    public void annuleDossierSplitting() throws Exception {
        if (CIDossierSplitting.CS_CLOTURE.equals(idEtat)) {
            getSession().addError(getSession().getLabel("ANNULATION_IMPOSSIBLE"));
        } else {
            // vérifie qu'il n'existe pas des mandats à l'état terminé pour le dossier
            boolean existMandatTermine = false;
            // pour l'assuré
            CIMandatSplittingManager mandatsAssure = new CIMandatSplittingManager();
            mandatsAssure.setForIdDossierSplitting(getIdDossierSplitting());
            mandatsAssure.setForIdTiersPartenaire(getIdTiersAssure());
            mandatsAssure.setForIdEtat(CIMandatSplitting.CS_TERMINE);
            mandatsAssure.setSession(getSession());
            if (mandatsAssure.getCount() > 0) {
                existMandatTermine = true;
            }
            // pour le conjoint
            CIMandatSplittingManager mandatsConjoint = new CIMandatSplittingManager();
            mandatsConjoint.setForIdDossierSplitting(getIdDossierSplitting());
            mandatsConjoint.setForIdTiersPartenaire(getIdTiersConjoint());
            mandatsConjoint.setForIdEtat(CIMandatSplitting.CS_TERMINE);
            mandatsConjoint.setSession(getSession());
            if (mandatsConjoint.getCount() > 0) {
                existMandatTermine = true;
            }

            // si il existe des mandats à l'état terminé on refuse l'annulation
            if (existMandatTermine) {
                getSession().addError(getSession().getLabel("ANNULATION_IMPOSSIBLE_MANDAT_TERMINE"));
            } else {
                setIdEtat(CIDossierSplitting.CS_ANNULE);
            }
        }
    }

    /**
     * Indique si l'utilisateur peut supprimer le dossier en fonction du responsable et de l'état du dossier
     *
     * @return true si autorisé supprimer le dossier
     */
    public boolean canDelete() {

        if (CIDossierSplitting.CS_SPLITTING_EN_COURS.equals(idEtat)
                || CIDossierSplitting.CS_SPLITTING_COMPLET.equals(idEtat)
                || CIDossierSplitting.CS_DEMANDE_CA.equals(idEtat) || CIDossierSplitting.CS_CLOTURE.equals(idEtat)
                || CIDossierSplitting.CS_DEMANDE_REVOCATION.equals(idEtat)) {
            // suppression impossible
            return false;
        }
        if (getSession() == null) {
            return false;
        } else if (!getSession().hasRight(CIDossierSplitting.USER_ACTION_SUPPRIMER_SPLITTING,
                FWSecureConstants.REMOVE)) {
            return false;
        }
        return true;
    }

    public boolean canDemandeCA() {
        if (!CIDossierSplitting.CS_CLOTURE.equals(getIdEtat()) && !CIDossierSplitting.CS_REVOQUE.equals(getIdEtat())
                && !CIDossierSplitting.CS_DEMANDE_CA.equals(getIdEtat())
                && !CIDossierSplitting.CS_DEMANDE_REVOCATION.equals(getIdEtat())
                && !CIDossierSplitting.CS_ANNULE.equals(getIdEtat())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Charge les informations nécessaire à l'en-tête de la recherche.<br>
     * Effectuer dans le bean car les informations de l'en-tête ne sont pas des propriétés de l'objet métier (durée,
     * assuré/conjoint) Date de création : (29.10.2002 09:22:03)
     */
    public void chargeEntete() throws Exception {
        this.retrieve();
        // durée de mariage
        setDuree(getDateMariage() + " - " + getDateDivorce());
        // définition du partenaire
        if (CIDossierSplitting.ASSURE.equals(typePersonne)) {
            setIdTiersPartenaire(getIdTiersAssure());
        } else {
            setIdTiersPartenaire(getIdTiersConjoint());
        }
        /*
         * // nom du tiers try { CIApplication application = (CIApplication) getSession().getApplication();
         * ITIPersonneAvs tiers = application.getTiersByAvs( transaction, JAUtil.formatAvs(getIdTiersPartenaire()),new
         * String[] {"getNom"}); if (tiers.getNom() != null) { setTiersPartenaireNomComplet(tiers.getNom()); } else {
         * setTiersPartenaireNomComplet(""); } } catch (Exception ex) { setTiersPartenaireNomComplet(""); }
         */
    }

    /**
     * Essaie de trouver le nom complet du créateur du CI. Date de création : (29.11.2002 14:12:37)
     *
     * @param transaction
     *            la transaction à utiliser
     */
    public void chargeNomComplet(BTransaction transaction) {
        String result = getResponsableDossier();
        if (!JAUtil.isStringEmpty(result)) {
            // non vide -> recherche
            FWSecureUser user = new FWSecureUser();
            user.setSession(getSession());
            user.setUser(getResponsableDossier());
            try {
                user.retrieve(transaction);
                if (!user.hasErrors()) {
                    result = user.getFirstname() + " " + user.getLastname();
                }
                if (JAUtil.isStringEmpty(result)) {
                    // si utilisateur trouvé mais sans nom -> id
                    result = getResponsableDossier();
                }
            } catch (Exception e) {
                // laisser userid
            }
        }
        utilisateurNomComplet = result;
    }

    /**
     * Test la date de naissance de l'assuré et du conjoit par rapport à la date de mariage et test du sexe.
     *
     * @param transaction
     *            la transaction à utiliser
     * @return true si les CI sont en ordre
     * @throws Exception
     *             dans le cas d'un problème d'exécution
     */
    private boolean checkWithCI(BTransaction transaction) throws Exception {
        return this.checkWithCI(transaction, null, null);
    }

    /**
     * Test la date de naissance de l'assuré et du conjoit par rapport à la date de mariage et test du sexe.
     *
     * @param transaction
     *            la transaction à utiliser
     * @return true si les CI sont en ordre ou si les ci sont inexistants
     * @throws Exception
     *             dans le cas d'un problème d'exécution
     */
    private boolean checkWithCI(BTransaction transaction, CICompteIndividuel ciAssure, CICompteIndividuel ciConjoint)
            throws Exception {
        // test dates et le sexe avec les CI
        if ((ciAssure == null) && !JAUtil.isStringEmpty(getIdTiersAssure())) {
            ciAssure = CICompteIndividuel.loadCI(getIdTiersAssure(), transaction);
        }
        if (ciAssure == null) {
            // ne pas faire les test
            return true;
        }
        if (BSessionUtil.compareDateFirstLower(getSession(), getDateMariage(), ciAssure.getDateNaissance())) {
            _addError(transaction, getSession().getLabel("MSG_DOSSIER_VAL_DATE_ASSURE"));
            return false;
        }
        if ((ciConjoint == null) && !JAUtil.isStringEmpty(getIdTiersConjoint())) {
            ciConjoint = CICompteIndividuel.loadCI(getIdTiersConjoint(), transaction);
        }
        if (ciConjoint == null) {
            // ne pas faire les test
            return true;
        }
        if (BSessionUtil.compareDateFirstLower(getSession(), getDateMariage(), ciConjoint.getDateNaissance())) {
            _addError(transaction, getSession().getLabel("MSG_DOSSIER_VAL_DATE_CONJOINT"));
            return false;
        }
        // modif 21.02.2007, deux assurés du même sexe peuvent avoir des revenus
        // partagés
        /*
         * if (ciAssure.getSexe().equals(ciConjoint.getSexe())) { _addError(transaction,
         * getSession().getLabel("MSG_DOSSIER_VAL_CI_SEXE")); return false; }
         */
        return true;
    };

    public void exectueSplittingBatch(BTransaction transaction) throws Exception {
        // splitter tous les mandats
        BITransaction remoteTransaction = null;
        try {
            BISession remoteSession = ((CIApplication) getSession().getApplication()).getSessionAnnonce(getSession());
            remoteTransaction = ((BSession) remoteSession).newTransaction();
            remoteTransaction.openTransaction();
            // init container
            container = new CIEcrituresSplittingContainer();
            // assuré
            boolean foundMandatsAssure = splitterMandats(getIdTiersAssure(), transaction, remoteTransaction);
            // conjoint
            boolean foundMandatConjoint = splitterMandats(getIdTiersConjoint(), transaction, remoteTransaction);
            // test des mandats ont été effectués
            if (!foundMandatsAssure) {
                // aucun mandat pour l'assuré, reste en OUVERT
                setIdEtat(CIDossierSplitting.CS_OUVERT);
                // signaler erreur
                if (!foundMandatConjoint) {
                    // aucun mandats
                    transaction.addErrors(getSession().getLabel("MSG_DOSSIER_UPDATE"));
                } else {
                    transaction.addErrors(getSession().getLabel("MSG_DOSSIER_UP_ASSURE"));
                }
                // } else if (!foundMandatConjoint) {
                // transaction.addErrors(getSession().getLabel("MSG_DOSSIER_UP_CONJOINT"));
            } else {
                if (!transaction.isRollbackOnly() && !remoteTransaction.isRollbackOnly()) {
                    // test si erreurs afin de ne pas modifier l'affichage de
                    // détail
                    setIdEtat(CIDossierSplitting.CS_SPLITTING_EN_COURS);
                    // test si l'envoi de ci additionel est nécessaire
                    // non -> déjà effectué dans lors du splitting manuel
                    // container.checkandExecCIAdditionnel(transaction);
                }
            }
            if (!transaction.isRollbackOnly() && !remoteTransaction.isRollbackOnly()) {
                updateDossier(transaction, remoteTransaction);
            } else {
                if (remoteTransaction.isRollbackOnly()) {
                    transaction.addErrors(remoteTransaction.getErrors().toString());
                }
            }
            if (transaction.isRollbackOnly()) {
                transaction.rollback();
                remoteTransaction.rollback();
            } else {
                transaction.commit();
                remoteTransaction.commit();
            }
        } finally {
            if (remoteTransaction != null) {
                remoteTransaction.closeTransaction();
            }
        }
    }

    /**
     * Exécution splitting selon mandat Date de création : (15.10.2002 11:13:36)
     */
    public void executerSplitting() throws Exception {
        // contrôler état du dossier
        if (!CIDossierSplitting.CS_OUVERT.equals(idEtat)) {
            throw new CISplittingException(getSession().getLabel("MSG_DOSSIER_SPLITTING"));
        }
        if (!forceExecute) {
            // recherche si mandats conjoint existent
            CIMandatSplittingManager maMgr = new CIMandatSplittingManager();
            maMgr.setSession(getSession());
            maMgr.setForIdTiersPartenaire(getIdTiersConjoint());
            maMgr.setForIdDossierSplitting(getIdDossierSplitting());
            maMgr.find();
            if (maMgr.isEmpty()) {
                // erreur
                forceExecute = true;
                throw new CISplittingException(getSession().getLabel("MSG_DOSSIER_UP_CONJOINT"));
            }
        }
        setIdEtat(CIDossierSplitting.CS_A_TRAITER);
        // pas nécecssaire de valider
        wantCallValidate(false);
        this.update();
        wantCallValidate(true);
    }

    public String getAdresseAssure() {
        return adresseAssure;
    }

    public String getAdresseExConjoint() {
        return adresseExConjoint;
    }

    public Boolean getChkAccuseReceptionAssure() {
        return chkAccuseReceptionAssure;
    }

    public Boolean getChkInvitationExConjoint() {
        return chkInvitationExConjoint;
    }

    public Boolean getChkLettreAccompagnementAssure() {
        return chkLettreAccompagnementAssure;
    }

    public Boolean getChkLettreAccompagnementExConjoint() {
        return chkLettreAccompagnementExConjoint;
    }

    /**
     * Returns the container.
     *
     * @return CIEcrituresSplittingContainer
     */
    public CIEcrituresSplittingContainer getContainer() {
        return container;
    }

    public java.lang.String getDateDivorce() {
        return dateDivorce;
    }

    public java.lang.String getDateMariage() {
        return dateMariage;
    }

    public String getDateNaissanceAss() {
        if (JadeStringUtil.isBlankOrZero(idTiersAssure)) {
            return "";
        }
        try {
            if (ciAssureForNNSS == null) {
                ciAssureForNNSS = loadCI(idTiersAssure);
            }
            return ciAssureForNNSS.getDateNaissance();
        } catch (Exception e) {
            return "";
        }
    }

    public String getDateNaissanceConj() {
        if (JadeStringUtil.isBlankOrZero(idTiersConjoint)) {
            return "";
        }
        try {

            if (ciConjointForNNSS == null) {
                ciConjointForNNSS = loadCI(idTiersConjoint);
            }
            return ciConjointForNNSS.getDateNaissance();
        } catch (Exception e) {
            return "";
        }
    }

    public java.lang.String getDateOuvertureDossier() {
        return dateOuvertureDossier;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.10.2002 09:18:17)
     *
     * @return java.lang.String
     */
    public java.lang.String getDuree() {
        return duree;
    }

    /**
     * Retourne l'adresse email du responsable du dossier en cours. Date de création : (26.11.2002 08:29:01)
     *
     * @param transaction
     *            la transaction à utiliser.
     * @return l'adresse email ou null si inexistante.
     */
    public String getEMailResponsable(BTransaction transaction) {
        String email = null;
        try {

            JadeUserService userService = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator()
                    .getUserService();
            JadeUser user = userService.loadForVisa((getResponsableDossier()));
            email = user.getEmail();
        } catch (Exception e) {
            return null;
        }

        return email;
    }

    public String getEtatFormateAssure() {
        try {
            if (ciAssureForNNSS == null) {
                ciAssureForNNSS = loadCI(idTiersAssure);
            }
            return ciAssureForNNSS.getEtatFormate();
        } catch (Exception e) {
            return "";
        }
    }

    public String getEtatFormateConjoint() {
        try {
            if (ciConjointForNNSS == null) {
                ciConjointForNNSS = loadCI(idTiersConjoint);
            }
            return ciConjointForNNSS.getEtatFormate();
        } catch (Exception e) {
            return "";
        }
    }

    public java.lang.String getIdArc33Assure() {
        return idArc33Assure;
    }

    public java.lang.String getIdArc33Conjoint() {
        return idArc33Conjoint;
    }

    public java.lang.String getIdArc98Assure() {
        return idArc98Assure;
    }

    public java.lang.String getIdArc98Conjoint() {
        return idArc98Conjoint;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.02.2003 16:37:32)
     *
     * @return java.lang.String
     */
    public java.lang.String getIdDossierInterne() {
        return idDossierInterne;
    }

    /**
     * Getter
     */
    public java.lang.String getIdDossierSplitting() {
        return idDossierSplitting;
    }

    public java.lang.String getIdEtat() {
        return idEtat;
    }

    public java.lang.String getIdMotifSplitting() {
        return idMotifSplitting;
    }

    public java.lang.String getIdRemarque() {
        return idRemarque;
    }

    public java.lang.String getIdTiersAssure() {
        return idTiersAssure;
    }

    /**
     * @return
     */
    public String getIdTiersAssureNNSS() {
        return idTiersAssureNNSS;
    }

    public java.lang.String getIdTiersConjoint() {
        return idTiersConjoint;
    }

    /**
     * @return
     */
    public String getIdTiersConjointNNSS() {
        return idTiersConjointNNSS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.10.2002 09:18:17)
     *
     * @return java.lang.String
     */
    public java.lang.String getIdTiersPartenaire() {
        return idPartenaire;
    }

    private String getInfoAssure() {
        try {
            if (ciAssureForNNSS == null) {
                ciAssureForNNSS = loadCI(idTiersAssure);
            }
            return ciAssureForNNSS.getNomPrenom();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @return
     */
    public Boolean getIsArchivage() {
        return isArchivage;
    }

    public String getLangueAssure() {

        if (JadeStringUtil.isBlankOrZero(langueAssure)) {
            // Francais
            return "503001";
        }

        return langueAssure;
    }

    public String getLangueExConjoint() {

        if (JadeStringUtil.isBlankOrZero(langueExConjoint)) {
            // Francais
            return "503001";
        }

        return langueExConjoint;
    }

    public String getNssWithoutPrefixeAssure() {
        if (getIdTiersAssure().trim().length() == 13) {
            return NSUtil.formatWithoutPrefixe(getIdTiersAssure(), true);
        } else {
            return NSUtil.formatWithoutPrefixe(getIdTiersAssure(), false);
        }

    }

    public String getNssWithoutPrefixeConjoint() {
        if (getIdTiersConjoint().trim().length() == 13) {
            return NSUtil.formatWithoutPrefixe(getIdTiersConjoint(), true);
        } else {
            return NSUtil.formatWithoutPrefixe(getIdTiersConjoint(), false);
        }

    }

    public String getPaysFormateAss() {
        if (JadeStringUtil.isBlankOrZero(idTiersAssure)) {
            return "";
        }
        try {
            if (ciAssureForNNSS == null) {
                ciAssureForNNSS = loadCI(idTiersAssure);
            }
            return ciAssureForNNSS.getPaysFormate();
        } catch (Exception e) {
            return "";
        }
    }

    public String getPaysFormateConj() {
        if (JadeStringUtil.isBlankOrZero(idTiersConjoint)) {
            return "";
        }
        try {
            if (ciConjointForNNSS == null) {
                ciConjointForNNSS = loadCI(idTiersConjoint);
            }
            return ciConjointForNNSS.getPaysFormate();
        } catch (Exception e) {
            return "";
        }
    }

    public String getReferenceService() {
        return referenceService;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.11.2002 08:55:57)
     *
     * @return java.lang.String
     */
    public java.lang.String getRemarqueDossier() {
        return remarqueDossier;
    }

    public java.lang.String getResponsableDossier() {
        if (JAUtil.isStringEmpty(responsableDossier)) {
            String lUser = getSession().getUserId();
            if ((lUser != null) && (lUser.length() != 0)) {
                return lUser;
            }
        }
        return responsableDossier;
    }

    public String getSexeLibelleAss() {
        if (JadeStringUtil.isBlankOrZero(idTiersAssure)) {
            return "";
        }
        try {
            if (ciAssureForNNSS == null) {
                ciAssureForNNSS = loadCI(idTiersAssure);
            }
            return ciAssureForNNSS.getSexeLibelle();
        } catch (Exception e) {
            return "";
        }
    }

    public String getSexeLibelleConj() {
        try {
            if (JadeStringUtil.isBlankOrZero(idTiersConjoint)) {
                return "";
            }
            if (ciConjointForNNSS == null) {
                ciConjointForNNSS = loadCI(idTiersConjoint);
            }
            return ciConjointForNNSS.getSexeLibelle();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 13:07:36)
     *
     * @return java.lang.String
     */
    public java.lang.String getTiersAssureInfo() {
        return tiersAssureInfo;
    }

    /**
     * Retourne la localité de l'assuré Date de création : (12.12.2002 09:09:20)
     *
     * @return la localité du tiers.
     */
    public java.lang.String getTiersAssureLocalite() {
        return tiersAssureLocalite;
    }

    /**
     * Retourne le nom de l'assuré. Date de création : (04.12.2002 12:42:14)
     *
     * @return le nom du tiers.
     */
    public java.lang.String getTiersAssureNomComplet() {
        return tiersAssureNom;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 13:07:36)
     *
     * @return java.lang.String
     */
    public java.lang.String getTiersConjointInfo() {
        return tiersConjointInfo;
    }

    /**
     * Retourne la localité du conjoint. Date de création : (12.12.2002 09:09:20)
     *
     * @return la localité du conjoint.
     */
    public java.lang.String getTiersConjointLocalite() {
        return tiersConjointLocalite;
    }

    /**
     * Retourne le nom du conjoint. Date de création : (04.12.2002 12:42:14)
     *
     * @return le nom du conjoint.
     */
    public java.lang.String getTiersConjointNomComplet() {
        return tiersConjointNom;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.12.2002 12:44:05)
     *
     * @return java.lang.String
     */
    public java.lang.String getTiersPartenaireNomComplet() {
        return tiersPartenaireNomComplet;
    }

    public String getTitreAssure() {
        return titreAssure;
    }

    public String getTitreExConjoint() {
        return titreExConjoint;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.05.2003 12:51:15)
     *
     * @return java.lang.String
     */
    public java.lang.String getTypePersonne() {
        return typePersonne;
    }

    /**
     * Retourne la classe <tt>FWSecureUser</tt> du responsable du dossier. Date de création : (03.12.2002 12:58:17)
     *
     * @return La classe correspondante
     * @param transaction
     *            la transaction à utiliser.
     */
    public JadeUser getUser(BTransaction transaction) throws Exception {
        JadeUser user = new JadeUser();
        JadeUserService service = JadeAdminServiceLocatorProvider.getLocator().getUserService();
        user = service.loadForVisa(getResponsableDossier());
        return user;
    }

    /**
     * Renvoie le nom du créateur du CI. Date de création : (29.11.2002 14:30:33)
     *
     * @return le nom (complet si trouvé ou simplement l'abbréviation) du créateur.
     */
    public java.lang.String getUtilisateurNomComplet() {
        return utilisateurNomComplet;
    }

    private void imprimerAccuseReceptionDemandeSplitting() {

        CISplittingAccuseReception_Doc accuseReceptionAssureDoc = new CISplittingAccuseReception_Doc();

        String theLangueISO = AFUtil.toLangueIso(getLangueAssure());

        String theFormulePolitesse = "";
        try {
            theFormulePolitesse = CodeSystem.getLibelleIso(getSession(), getTitreAssure(), theLangueISO);
        } catch (Exception e) {
            theFormulePolitesse = "Monsieur";
        }

        String theFormatedAVSNumber = NSUtil.formatAVSUnknown(getIdTiersAssure());

        accuseReceptionAssureDoc.setSession(getSession());
        accuseReceptionAssureDoc.setNumeroAVS(theFormatedAVSNumber);
        accuseReceptionAssureDoc.setFormulePolitesse(theFormulePolitesse);
        accuseReceptionAssureDoc.setAdresse(theFormulePolitesse + "\n" + getAdresseAssure());
        accuseReceptionAssureDoc.setLangueISO(theLangueISO);
        accuseReceptionAssureDoc.start();

    }

    /**
     * Impression de des RCI avant splitting. Date de création : (15.10.2002 11:20:18)
     */
    public void imprimerAnalyse(String email) throws Exception {
        // vérification
        if (CIDossierSplitting.CS_ANNULE.equals(idEtat) || CIDossierSplitting.CS_ERREUR.equals(idEtat)
                || CIDossierSplitting.CS_DEMANDE_OUVERTURE_CI.equals(idEtat)
                || CIDossierSplitting.CS_EXTRAIT_EN_COURS.equals(idEtat)
                || CIDossierSplitting.CS_OUVERTURE_DOSSIER.equals(idEtat)
                || CIDossierSplitting.CS_SAISIE_DOSSIER.equals(idEtat)) {
            // impossible d'imprimer à ce stade
            throw new CISplittingException(getSession().getLabel("MSG_DOSSIER_ANALYSE"));
        }
        CIAnalyseSplitting process = new CIAnalyseSplitting(getSession());

        String refUniqueAssure = getIdArc98Assure();
        String refUniqueConjoint = getIdArc98Conjoint();

        process.setControleTransaction(true);
        process.setSendCompletionMail(true);
        process.setEMailAddress(email);
        process.setRefAssure(refUniqueAssure);
        process.setRefConjoint(refUniqueConjoint);
        // process.setDossier(this);
        process.setIdDossier(getIdDossierSplitting());
        process.start();
    }

    /**
     * Impression de l'aperçu des CI. Date de création : (15.10.2002 11:20:18)
     */
    public void imprimerApercu(String email, boolean check) throws Exception {
        // vérification
        if (!CIDossierSplitting.CS_CLOTURE.equals(idEtat)) {
            // impossible d'imprimer à ce stade
            throw new CISplittingException(getSession().getLabel("MSG_DOSSIER_APERCU"));
        }

        boolean hasNoRight = hasNoRight(getSession().getUserId(), getIdTiersAssure())
                || hasNoRight(getSession().getUserId(), getIdTiersConjoint());

        String refUniqueAssure = null;
        String refUniqueConjoint = null;
        // assuré
        CIMandatSplittingManager mandats = new CIMandatSplittingManager();
        mandats.setForIdDossierSplitting(getIdDossierSplitting());
        mandats.setForIdTiersPartenaire(getIdTiersAssure());
        mandats.setSession(getSession());
        mandats.find();

        for (int i = 0; (i < mandats.size()) && (refUniqueAssure == null); i++) {
            CIMandatSplitting entity = (CIMandatSplitting) mandats.getEntity(i);
            if (entity.isMandatAutomatique()) {
                refUniqueAssure = entity.getIdArc();
            }

        }
        // conjoint
        mandats = new CIMandatSplittingManager();
        mandats.setForIdDossierSplitting(getIdDossierSplitting());
        mandats.setForIdTiersPartenaire(getIdTiersConjoint());
        mandats.setSession(getSession());
        mandats.find();

        for (int i = 0; (i < mandats.size()) && (refUniqueConjoint == null); i++) {
            CIMandatSplitting entity = (CIMandatSplitting) mandats.getEntity(i);
            if (entity.isMandatAutomatique()) {
                refUniqueConjoint = entity.getIdArc();
            }
        }

        CISplittingApercuAndLettreAccompagnementMergeProcess apercuAndLettreAccompagnementMergeProcess = new CISplittingApercuAndLettreAccompagnementMergeProcess();

        apercuAndLettreAccompagnementMergeProcess.setCache(hasNoRight);

        apercuAndLettreAccompagnementMergeProcess.setSession(getSession());
        apercuAndLettreAccompagnementMergeProcess.setEMailAddress(email);

        // set les attributs qui permettront de créer l'apercu de splitting une fois le processus soumis
        if (!JadeStringUtil.isIntegerEmpty(refUniqueAssure)) {
            apercuAndLettreAccompagnementMergeProcess.setIdAnnonceAssure(refUniqueAssure);
        }
        if (!JadeStringUtil.isIntegerEmpty(refUniqueConjoint)) {
            apercuAndLettreAccompagnementMergeProcess.setIdAnnonceExConjoint(refUniqueConjoint);
        }

        apercuAndLettreAccompagnementMergeProcess.setCheck(check);

        // set les attributs qui permettront de créer la lettre d'accompagnement pour l'assuré une fois le processus
        // soumis
        // le numéro avs et l'adresse ont été sortis de la condition
        // car ils sont utilisés dans l'aperçu du splitting indépendamment de la lettre d'accompagnement
        String theFormatedAVSNumberAssure = NSUtil.formatAVSUnknown(getIdTiersAssure());
        String theLangueISOAssure = AFUtil.toLangueIso(getLangueAssure());

        String theFormulePolitesseAssure = "";
        try {
            theFormulePolitesseAssure = CodeSystem.getLibelleIso(getSession(), getTitreAssure(), theLangueISOAssure);
        } catch (Exception e) {
            theFormulePolitesseAssure = "Monsieur";
        }
        apercuAndLettreAccompagnementMergeProcess.setNumeroAVSAssure(theFormatedAVSNumberAssure);
        apercuAndLettreAccompagnementMergeProcess
                .setAdresseAssure(theFormulePolitesseAssure + "\n" + getAdresseAssure());
        if (getChkLettreAccompagnementAssure()) {

            apercuAndLettreAccompagnementMergeProcess.setChkLettreAccompagnementAssure(new Boolean(true));
            apercuAndLettreAccompagnementMergeProcess.setFormulePolitesseAssure(theFormulePolitesseAssure);
        }
        apercuAndLettreAccompagnementMergeProcess.setLangueISOAssure(theLangueISOAssure);

        // set les attributs qui permettront de créer la lettre d'accompagnement pour l'ex-conjoint une fois le
        // processus
        // soumis
        // le numéro avs et l'adresse ont été sortis de la condition
        // car ils sont utilisés dans l'aperçu du splitting indépendamment de la lettre d'accompagnement
        String theFormatedAVSNumberExConjoint = NSUtil.formatAVSUnknown(getIdTiersConjoint());
        String theLangueISOExConjoint = AFUtil.toLangueIso(getLangueExConjoint());

        String theFormulePolitesseExConjoint = "";
        try {
            theFormulePolitesseExConjoint = CodeSystem.getLibelleIso(getSession(), getTitreExConjoint(),
                    theLangueISOExConjoint);
        } catch (Exception e) {
            theFormulePolitesseExConjoint = "Monsieur";
        }
        apercuAndLettreAccompagnementMergeProcess.setNumeroAVSExConjoint(theFormatedAVSNumberExConjoint);
        apercuAndLettreAccompagnementMergeProcess
                .setAdresseExConjoint(theFormulePolitesseExConjoint + "\n" + getAdresseExConjoint());
        if (getChkLettreAccompagnementExConjoint()) {

            apercuAndLettreAccompagnementMergeProcess.setChkLettreAccompagnementExConjoint(new Boolean(true));
            apercuAndLettreAccompagnementMergeProcess.setFormulePolitesseExConjoint(theFormulePolitesseExConjoint);
        }
        apercuAndLettreAccompagnementMergeProcess.setLangueISOExConjoint(theLangueISOExConjoint);

        apercuAndLettreAccompagnementMergeProcess.start();
    }

    public boolean hasNoRight(String userId, String numAvs) {
        return hasNoRight(userId, getSession(), numAvs);
    }

    public boolean hasNoRight(String userId, BSession session, String numAVS) {
        try {
            // non on n'a pas déjà demandé le revenu pour cet instance
            // recherche du compte individuel en relation avec l'annonce
            CICompteIndividuelManager ciRecherche = new CICompteIndividuelManager();
            // création d'une nouvelle session

            BTransaction newTrans = null;
            try {
                newTrans = new BTransaction(session);
                newTrans.openTransaction();
                // recherche du ci avec ce numéro avs
                ciRecherche.setSession(session);
                ciRecherche.setForNumeroAvs(numAVS);
                ciRecherche.find(newTrans);
                if (ciRecherche.size() != 0) {
                    // on a trouvé le ci dans PAVO !
                    CICompteIndividuel ci = (CICompteIndividuel) ciRecherche.getEntity(0);
                    // faire une nouvelle transaction
                    if (ci.hasUserShowRight(newTrans, userId)
                            && CICompteIndividuel.hasRightAffiliationSecureCode(session, newTrans, ci, userId)) {
                        // l'utilisateur a les droits pour voir le revenu
                        return false;
                    } else {
                        // l'utilisateur loggé n'a pas les droits
                        return true;
                    }
                } else {
                    // si le ci n'existe pas, on retourne le revenu
                    return false;
                }
            } catch (Exception e) {
                throw e;
            } finally {
                if (newTrans != null) {
                    newTrans.closeTransaction();
                }
            }
        } catch (Exception e) {
            setMessage(e.getMessage());
            JadeLogger.warn(this, e.getMessage());
            return true;
        }
    }

    private boolean existEcritureACacher(BSession session, String idCi) throws Exception {
        boolean existEcritureACacher = false;
        CIEcritureManager ecritureManager = new CIEcritureManager();
        ecritureManager.setCacherEcritureProtege(1);
        ecritureManager.setSession(session);
        ecritureManager.setForCompteIndividuelId(idCi);
        ecritureManager.find();
        if ((ecritureManager.size() != 0) && ecritureManager.hasEcritureProtegeParAffiliation()) {
            existEcritureACacher = true;
        }
        return existEcritureACacher;
    }

    public void imprimerApercuAndInvit() {
        if (getChkAccuseReceptionAssure()) {
            imprimerAccuseReceptionDemandeSplitting();
        }

        if (getChkInvitationExConjoint()) {
            imprimerInvitationParticipationAuSplitting();
        }
    }

    private void imprimerInvitationParticipationAuSplitting() {

        CISplittingInvitationExConjoint_Doc invitationExConjointDoc = new CISplittingInvitationExConjoint_Doc();

        String theLangueISO = AFUtil.toLangueIso(getLangueExConjoint());

        String theFormulePolitesse = "";
        try {
            theFormulePolitesse = CodeSystem.getLibelleIso(getSession(), getTitreExConjoint(), theLangueISO);
        } catch (Exception e) {
            theFormulePolitesse = "Monsieur";
        }

        String theFormatedAVSNumber = NSUtil.formatAVSUnknown(getIdTiersConjoint());
        String theInfoDemandeur = NSUtil.formatAVSUnknown(getIdTiersAssure()) + " - " + getInfoAssure();

        invitationExConjointDoc.setSession(getSession());
        invitationExConjointDoc.setNumeroAVS(theFormatedAVSNumber);
        invitationExConjointDoc.setFormulePolitesse(theFormulePolitesse);
        invitationExConjointDoc.setAdresse(theFormulePolitesse + "\n" + getAdresseExConjoint());
        invitationExConjointDoc.setLangueISO(theLangueISO);
        invitationExConjointDoc.setInfoDemandeur(theInfoDemandeur);
        invitationExConjointDoc.start();

    }

    /**
     * Teste si tous les mandats d'un tiers sont dans l'état TERMINE. Date de création : (06.11.2002 12:04:06)
     *
     * @return true si tous les mandats sont terminés.
     * @param transaction
     *            la transaction à utiliser.
     */
    private boolean isAllMandatAsState(String idTiers, String code, BTransaction transaction) throws Exception {
        CIMandatSplittingManager mandats = new CIMandatSplittingManager();
        mandats.setForIdDossierSplitting(getIdDossierSplitting());
        mandats.setForIdTiersPartenaire(idTiers);
        mandats.setSession(getSession());
        mandats.find(transaction);
        boolean terminated = true;
        for (int i = 0; (i < mandats.size()) && terminated; i++) {
            CIMandatSplitting entity = (CIMandatSplitting) mandats.getEntity(i);
            if (!code.equals(entity.getIdEtat())) {
                terminated = false;
                break;
            }
        }
        return terminated;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.02.2003 16:37:32)
     *
     * @return java.lang.Boolean
     */
    public java.lang.Boolean isCaRequisAssure() {
        return caRequisAssure;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.02.2003 16:37:32)
     *
     * @return java.lang.Boolean
     */
    public java.lang.Boolean isCaRequisConjoint() {
        return caRequisConjoint;
    }

    public java.lang.Boolean isEstTaxationDefinitive() {
        return estTaxationDefinitive;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.02.2003 16:37:32)
     *
     * @return java.lang.Boolean
     */
    public java.lang.Boolean isExtraitCiRequisAssure() {
        return extraitCiRequisAssure;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.02.2003 16:37:32)
     *
     * @return java.lang.Boolean
     */
    public java.lang.Boolean isExtraitCiRequisConjoint() {
        return extraitCiRequisConjoint;
    }

    /**
     * Returns the forceExecute.
     *
     * @return boolean
     */
    public boolean isForceExecute() {
        return forceExecute;
    }

    /**
     * Teste si l'état actuel du dossier de splitting autorise une opération sur les domiciles à l'étranger et les
     * mandats. Date de création : (28.10.2002 16:39:58)
     *
     * @return true si une modification est autorisée.
     */
    public boolean isModificationAllowedFromDossier() {
        return !(CIDossierSplitting.CS_SPLITTING_EN_COURS.equals(getIdEtat())
                || CIDossierSplitting.CS_DEMANDE_CA.equals(getIdEtat())
                || CIDossierSplitting.CS_CLOTURE.equals(getIdEtat())
                || CIDossierSplitting.CS_DEMANDE_REVOCATION.equals(getIdEtat()) // for
                // test
                // ||
                // CS_SAISIE_DOSSIER.equals(getIdEtat())
                || CIDossierSplitting.CS_REVOQUE.equals(getIdEtat()));
    }

    private CICompteIndividuel loadCI(String idTiers) throws Exception {
        if (idTiers == null) {
            return null;
        }
        CICompteIndividuelManager ciManager = new CICompteIndividuelManager();
        ciManager.setSession(getSession());
        ciManager.orderByAvs(false);
        ciManager.setForNumeroAvs(idTiers);
        ciManager.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        ciManager.find();
        if (ciManager.size() == 0) {
            return null;
        }
        return (CICompteIndividuel) ciManager.getEntity(0);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.01.2003 11:20:07)
     *
     * @param transaction
     *            globaz.globall.db.BTransaction
     */
    private void loadInfoTiers(BTransaction transaction) {
        try {
            CIApplication application = (CIApplication) getSession().getApplication();
            // chargement info assuré
            ITIPersonneAvs tiersAssure = application.getTiersByAvs(transaction,
                    NSUtil.formatAVSUnknown(getIdTiersAssure()), new String[] { "getIdTiers", "getNom" });
            if (!JAUtil.isStringEmpty(tiersAssure.getNom())) {
                tiersAssureNom = tiersAssure.getNom();
            } else {
                // pas trouvé dans tiers - > recherche des CI
                CICompteIndividuel ciAssure = CICompteIndividuel.loadCI(getIdTiersAssure(), transaction);
                if (ciAssure != null) {
                    tiersAssureNom = ciAssure.getNomPrenom();
                }

            }
            if (!isLoadedFromManager()) {
                String adresseAssu = tiersAssure.getAdressePrincipaleCourrier(CIApplication.CS_DOMAINE_ADRESSE_CI_ARC);
                if (!JAUtil.isStringEmpty(adresseAssu)) {
                    tiersAssureLocalite = adresseAssu;
                } else {
                    // pas trouvé dans tiers - > recherche des CI
                    CICompteIndividuel ciAssure = CICompteIndividuel.loadCI(getIdTiersAssure(), transaction);
                    if (ciAssure != null) {
                        tiersAssureLocalite = ciAssure.getNomPrenom();
                    }
                }
            }
            // chargement info conjoint
            ITIPersonneAvs tiersConjoint = application.getTiersByAvs(transaction,
                    NSUtil.formatAVSUnknown(getIdTiersConjoint()), new String[] { "getIdTiers", "getNom" });
            if (!JAUtil.isStringEmpty(tiersConjoint.getNom())) {
                tiersConjointNom = tiersConjoint.getNom();
            } else {
                // pas trouvé dans tiers - > recherche des CI
                CICompteIndividuel ciConjoint = CICompteIndividuel.loadCI(getIdTiersConjoint(), transaction);
                if (ciConjoint != null) {
                    tiersConjointNom = ciConjoint.getNomPrenom();
                }
            }
            if (!isLoadedFromManager()) {
                String adresseConj = tiersConjoint
                        .getAdressePrincipaleCourrier(CIApplication.CS_DOMAINE_ADRESSE_CI_ARC);
                if (!JAUtil.isStringEmpty(adresseConj)) {
                    tiersConjointLocalite = adresseConj;
                } else {
                    // pas trouvé dans tiers - > recherche des CI
                    CICompteIndividuel ciConjoint = CICompteIndividuel.loadCI(getIdTiersConjoint(), transaction);
                    if (ciConjoint != null) {
                        tiersConjointLocalite = ciConjoint.getNomPrenom();
                    }
                }
            }
        } catch (Exception ex) {
            // System.out.println("oups");
        }
    }

    /**
     * Démarrage traitement du dossier. Date de création : (15.10.2002 11:13:20)
     */
    public void ouvrir() throws Exception {
        // contrôler état du dossier
        if (isNew()) {
            // ouverture impossible si nouveau
            throw new CISplittingException(getSession().getLabel("MSG_DOSSIER_OUVRIR_NOUVEAU"));
        }
        if (!CIDossierSplitting.CS_SAISIE_DOSSIER.equals(idEtat)) {
            // ouverture impossible
            throw new CISplittingException(getSession().getLabel("MSG_DOSSIER_OUVRIR_IMPOSSIBLE"));
        }
        setIdEtat(CIDossierSplitting.CS_OUVERTURE_DOSSIER);
        // démarre la transaction
        BTransaction transaction = new BTransaction(getSession());
        try {
            transaction.openTransaction();
            updateDossier(transaction, null);
            this.retrieve(transaction);
            if (transaction.hasErrors()) {
                transaction.rollback();
            } else {
                transaction.commit();
            }
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }

        imprimerApercuAndInvit();

    }

    public String[] rechercheAnnonceRCI(String type) throws Exception {
        if (type != null) {
            setTypePersonne(type);
        }
        String[] result = null;
        BITransaction remoteTransaction = null;
        try {
            BISession remoteSession = ((CIApplication) getSession().getApplication()).getSessionAnnonce(getSession());
            remoteTransaction = ((BSession) remoteSession).newTransaction();
            remoteTransaction.openTransaction();
            IHEOutputAnnonce remoteLectureAnnonce = (IHEOutputAnnonce) remoteSession.getAPIFor(IHEOutputAnnonce.class);
            remoteLectureAnnonce.setMethodsToLoad(new String[] { "getIdAnnonce", "getRefUnique", "getInputTable" });
            if (CIDossierSplitting.ASSURE.equals(typePersonne)) {
                if (JAUtil.isIntegerEmpty(getIdArc98Assure())) {
                    // pas de RCI envoyé -> erreur
                    throw new Exception(getSession().getLabel("MSG_DOSSIER_NO_RCI_AS"));
                }
                remoteLectureAnnonce.setIdAnnonce(getIdArc98Assure());
            } else {
                if (JAUtil.isIntegerEmpty(getIdArc98Conjoint())) {
                    // pas de RCI envoyé -> erreur
                    throw new Exception(getSession().getLabel("MSG_DOSSIER_NO_RCI_CO"));
                }
                remoteLectureAnnonce.setIdAnnonce(getIdArc98Conjoint());
            }
            remoteLectureAnnonce.retrieve(remoteTransaction);
            if (!remoteLectureAnnonce.isNew()) {
                result = new String[2];
                result[0] = remoteLectureAnnonce.getIdAnnonce();
                result[1] = remoteLectureAnnonce.getRefUnique();
            }
            if (!remoteTransaction.hasErrors()) {
                remoteTransaction.commit();
            } else {
                remoteTransaction.rollback();
            }
        } catch (Exception ex) {
            if (remoteTransaction != null) {
                remoteTransaction.rollback();
            }
            throw ex;
        } finally {
            if (remoteTransaction != null) {
                remoteTransaction.closeTransaction();
            }
        }
        return result;
    }

    /**
     * Révoque les mandats d'un tiers donné. Date de création : (05.11.2002 08:16:28)
     *
     * @param idTiers
     *            le tiers dont les mandats doivent être révoqués
     * @param transaction
     *            la transaction à utiliser.
     * @exception Exception
     *                Si une erreur survient.
     */
    private void revocationMandats(String idTiers, BTransaction transaction, BITransaction remoteTransaction)
            throws Exception {
        CIMandatSplittingManager mandats = new CIMandatSplittingManager();
        mandats.setForIdDossierSplitting(getIdDossierSplitting());
        mandats.setForIdTiersPartenaire(idTiers);
        mandats.setSession(getSession());
        mandats.find(transaction);
        boolean actionCalled = false; // flag pour mandat automatiques
        for (int i = 0; i < mandats.size(); i++) {
            CIMandatSplitting entity = (CIMandatSplitting) mandats.getEntity(i);
            if (entity.isMandatAutomatique()) {
                // mandat automatique -> un seul appel de révocation
                if (!actionCalled) {
                    entity.revoquer(transaction, remoteTransaction);
                    actionCalled = true;
                }
            } else {
                // mandat manuel -> appel révocation
                entity.revoquer(transaction, remoteTransaction);
            }
        }
    }

    /**
     * Révocation du dossier. Date de création : (15.10.2002 11:12:26)
     *
     * @exception si
     *                la révocation est impossible ou si la transaction ne peut pas s'effectuer
     */
    public void revoquer() throws Exception {
        // vérification
        if (!CIDossierSplitting.CS_CLOTURE.equals(idEtat)) {
            // impossible de revoquer
            throw new CISplittingException(getSession().getLabel("MSG_DOSSIER_REVOQUER"));
        }
        BTransaction transaction = new BTransaction(getSession());
        BITransaction remoteTransaction = null;
        try {
            transaction.openTransaction();
            BISession remoteSession = ((CIApplication) getSession().getApplication()).getSessionAnnonce(getSession());
            remoteTransaction = ((BSession) remoteSession).newTransaction();
            remoteTransaction.openTransaction();
            // révoquer les mandats de splitting de l'assuré
            revocationMandats(getIdTiersAssure(), transaction, remoteTransaction);
            // révoquer les mandats de splitting du conjoint
            revocationMandats(getIdTiersConjoint(), transaction, remoteTransaction);
            // change d'état
            setIdEtat(CIDossierSplitting.CS_DEMANDE_REVOCATION);
            // mise à jour
            updateDossier(transaction, remoteTransaction);
            if (transaction.isRollbackOnly() || remoteTransaction.isRollbackOnly()) {
                transaction.rollback();
                remoteTransaction.rollback();
            } else {
                transaction.commit();
                remoteTransaction.commit();
            }
        } finally {
            try {
                if (transaction != null) {
                    transaction.closeTransaction();
                }
            } finally {
                if (remoteTransaction != null) {
                    remoteTransaction.closeTransaction();
                }
            }
        }
    }

    /**
     * Ouvrir à nouveau un dossier clôturé. Date de création : (15.10.2002 11:18:51)
     */
    public void rouvrir() throws Exception {
        // contrôler état du dossier
        if (!CIDossierSplitting.CS_CLOTURE.equals(idEtat) && !CIDossierSplitting.CS_ERREUR.equals(idEtat)) {
            throw new CISplittingException(getSession().getLabel("MSG_DOSSIER_ROUVRIR"));
        }
        // mettre à blanc
        idArc33Assure = "";
        idArc33Conjoint = "";
        setIdEtat(CIDossierSplitting.CS_OUVERT);
        this.update();
    }

    public void setAdresseAssure(String adresseAssure) {
        this.adresseAssure = adresseAssure;
    }

    public void setAdresseExConjoint(String adresseExConjoint) {
        this.adresseExConjoint = adresseExConjoint;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.02.2003 16:37:32)
     *
     * @param newCaRequisAssure
     *            java.lang.Boolean
     */
    public void setCaRequisAssure(java.lang.Boolean newCaRequisAssure) {
        caRequisAssure = newCaRequisAssure;
    }

    public void setCaRequisAssureStr(String caRequisAssureStr) {
        if ("True".equals(caRequisAssureStr)) {
            setCaRequisAssure(new Boolean(true));
        } else {
            setCaRequisAssure(new Boolean(false));
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.02.2003 16:37:32)
     *
     * @param newCaRequisConjoint
     *            java.lang.Boolean
     */
    public void setCaRequisConjoint(java.lang.Boolean newCaRequisConjoint) {
        caRequisConjoint = newCaRequisConjoint;
    }

    /*****************************************
     * On crée les setteurs pour éviter le bug FW qui initialise les boolean à false quand on passe par les écrans
     */
    public void setCaRequisConjointStr(String caRequisConjointStr) {
        if ("True".equals(caRequisConjointStr)) {
            setCaRequisConjoint(new Boolean(true));
        } else {
            setCaRequisConjoint(new Boolean(false));
        }
    }

    public void setChkAccuseReceptionAssure(Boolean chkAccuseReceptionAssure) {
        this.chkAccuseReceptionAssure = chkAccuseReceptionAssure;
    }

    public void setChkInvitationExConjoint(Boolean chkInvitationExConjoint) {
        this.chkInvitationExConjoint = chkInvitationExConjoint;
    }

    public void setChkLettreAccompagnementAssure(Boolean chkLettreAccompagnementAssure) {
        this.chkLettreAccompagnementAssure = chkLettreAccompagnementAssure;
    }

    public void setChkLettreAccompagnementExConjoint(Boolean chkLettreAccompagnementExConjoint) {
        this.chkLettreAccompagnementExConjoint = chkLettreAccompagnementExConjoint;
    }

    public void setDateDivorce(java.lang.String newDateDivorce) {
        dateDivorce = newDateDivorce;
    }

    public void setDateMariage(java.lang.String newDateMariage) {
        dateMariage = newDateMariage;
    }

    public void setDateOuvertureDossier(java.lang.String newDateOuvertureDossier) {
        dateOuvertureDossier = newDateOuvertureDossier;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.10.2002 09:18:17)
     *
     * @param newDuree
     *            java.lang.String
     */
    public void setDuree(java.lang.String newDuree) {
        duree = newDuree;
    }

    public void setEstTaxationDefinitive(java.lang.Boolean newEstTaxationDefinitive) {
        estTaxationDefinitive = newEstTaxationDefinitive;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.02.2003 16:37:32)
     *
     * @param newExtraitCiRequisAssure
     *            java.lang.Boolean
     */
    public void setExtraitCiRequisAssure(java.lang.Boolean newExtraitCiRequisAssure) {
        extraitCiRequisAssure = newExtraitCiRequisAssure;
    }

    public void setExtraitCiRequisAssureStr(String ciRequisAssureStr) {
        if ("True".equals(ciRequisAssureStr)) {
            setExtraitCiRequisAssure(new Boolean(true));
        } else {
            setExtraitCiRequisAssure(new Boolean(false));
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.02.2003 16:37:32)
     *
     * @param newExtraitCiRequisConjoint
     *            java.lang.Boolean
     */
    public void setExtraitCiRequisConjoint(java.lang.Boolean newExtraitCiRequisConjoint) {
        extraitCiRequisConjoint = newExtraitCiRequisConjoint;
    }

    public void setExtraitCiRequisConjointStr(String ciRequisConjointStr) {
        if ("True".equals(ciRequisConjointStr)) {
            setExtraitCiRequisConjoint(new Boolean(true));
        } else {
            setExtraitCiRequisConjoint(new Boolean(false));
        }
    }

    /**
     * Sets the forceExecute.
     *
     * @param forceExecute
     *            The forceExecute to set
     */
    public void setForceExecute(boolean forceExecute) {
        this.forceExecute = forceExecute;
    }

    public void setIdArc33Assure(java.lang.String newIdArc33Assure) {
        idArc33Assure = newIdArc33Assure;
    }

    public void setIdArc33Conjoint(java.lang.String newIdArc33Conjoint) {
        idArc33Conjoint = newIdArc33Conjoint;
    }

    public void setIdArc98Assure(java.lang.String newIdArc98Assure) {
        idArc98Assure = newIdArc98Assure;
    }

    public void setIdArc98Conjoint(java.lang.String newIdArc98Conjoint) {
        idArc98Conjoint = newIdArc98Conjoint;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.02.2003 16:37:32)
     *
     * @param newIdDossierInterne
     *            java.lang.String
     */
    public void setIdDossierInterne(java.lang.String newIdDossierInterne) {
        idDossierInterne = newIdDossierInterne;
    }

    /**
     * Setter
     */
    public void setIdDossierSplitting(java.lang.String newIdDossierSplitting) {
        idDossierSplitting = newIdDossierSplitting;
    }

    public void setIdEtat(java.lang.String newIdEtat) {
        idEtat = newIdEtat;
    }

    public void setIdMotifSplitting(java.lang.String newIdMotifSplitting) {
        idMotifSplitting = newIdMotifSplitting;
    }

    public void setIdRemarque(java.lang.String newIdRemarque) {
        idRemarque = newIdRemarque;
    }

    public void setIdTiersAssure(java.lang.String newIdTiersAssure) {
        idTiersAssure = CIUtil.unFormatAVS(newIdTiersAssure);
    }

    /**
     * @param string
     */
    public void setIdTiersAssureNNSS(String string) {
        idTiersAssureNNSS = string;
    }

    public void setIdTiersConjoint(java.lang.String newIdTiersConjoint) {
        idTiersConjoint = CIUtil.unFormatAVS(newIdTiersConjoint);
    }

    /**
     * @param string
     */
    public void setIdTiersConjointNNSS(String string) {
        idTiersConjointNNSS = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.10.2002 09:18:17)
     *
     * @param newIdPartenaire
     *            java.lang.String
     */
    public void setIdTiersPartenaire(java.lang.String newIdPartenaire) {
        idPartenaire = newIdPartenaire;
    }

    /**
     * @param boolean1
     */
    public void setIsArchivage(Boolean boolean1) {
        isArchivage = boolean1;
    }

    public void setLangueAssure(String langueAssure) {
        this.langueAssure = langueAssure;
    }

    public void setLangueExConjoint(String langueExConjoint) {
        this.langueExConjoint = langueExConjoint;
    }

    public void setReferenceService(String referenceService) {
        this.referenceService = referenceService;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.11.2002 08:55:57)
     *
     * @param newRemarque
     *            java.lang.String
     */
    public void setRemarqueDossier(java.lang.String newRemarque) {
        remarqueDossier = newRemarque;
    }

    public void setResponsableDossier(java.lang.String newResponsableDossier) {
        responsableDossier = newResponsableDossier;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.12.2002 12:44:05)
     *
     * @param newTiersPartenaireNomComplet
     *            java.lang.String
     */
    public void setTiersPartenaireNomComplet(java.lang.String newTiersPartenaireNomComplet) {
        tiersPartenaireNomComplet = newTiersPartenaireNomComplet;
    }

    public void setTitreAssure(String titreAssure) {
        this.titreAssure = titreAssure;
    }

    public void setTitreExConjoint(String titreExConjoint) {
        this.titreExConjoint = titreExConjoint;
    }

    /**
     * Défini si le partenaire est l'assuré ou le conjoint. Date de création : (20.01.2003 09:48:11)
     *
     * @param newTypePersonne
     *            <tt>CIDossierSplitting.ASSURE</tt> ou <tt>CIDossierSplitting.CONJOINT</tt>
     */
    public void setTypePersonne(java.lang.String newTypePersonne) {
        typePersonne = newTypePersonne;
    }

    /**
     * Exécute le splitting des mandats d'un tiers donné. Date de création : (05.11.2002 08:16:28)
     *
     * @param idTiers
     *            le tiers concerné
     * @param transaction
     *            la transaction à utiliser.
     * @return le nombre de mandats trouvés.
     * @exception Exception
     *                Si une erreur survient.
     */
    private boolean splitterMandats(String idTiers, BTransaction transaction, BITransaction remoteTransaction)
            throws Exception {
        CIMandatSplittingManager mandats = new CIMandatSplittingManager();
        mandats.setForIdDossierSplitting(getIdDossierSplitting());
        mandats.setForIdTiersPartenaire(idTiers);
        mandats.setSession(getSession());
        mandats.find(transaction);
        boolean actionCalled = false; // flag pour mandat automatiques
        boolean foundOpened = false;
        for (int i = 0; (i < mandats.size()) && !transaction.isRollbackOnly()
                && !remoteTransaction.isRollbackOnly(); i++) {
            CIMandatSplitting entity = (CIMandatSplitting) mandats.getEntity(i);
            if (CIMandatSplitting.CS_REVOQUE.equals(entity.getIdEtat())) {
                // effacer les mandats révoqués
                entity.wantCallMethodBefore(false);
                entity.delete(transaction);
            } else if (CIMandatSplitting.CS_DEMANDE_REVOCATION.equals(entity.getIdEtat())) {
                // impossible d'exécuter le splitting!
                transaction.addErrors(getSession().getLabel("MSG_DOSSIER_SPL_MANDATS"));
                return false;
            } else if (CIMandatSplitting.CS_OUVERT.equals(entity.getIdEtat())) {
                foundOpened = true;
                if (entity.isMandatAutomatique()) {
                    // mandat automatique -> un seul appel de révocation
                    if (!actionCalled) {
                        entity.splitter(transaction, remoteTransaction, this);
                        actionCalled = true;
                    }
                } else {
                    // mandat manuel -> appel révocation
                    entity.splitter(transaction, remoteTransaction, this);
                }
            } // ne rien faire si TERMINE
        }
        return foundOpened;
    }

    /**
     * Description de la classe <tt>CIDossierSplitting</tt>.<br>
     * Date de création : (03.12.2002 08:01:59)
     *
     * @return la description de la classe.
     */
    public String toMyString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Dossier de splitting no ");
        buf.append(getIdDossierSplitting());
        buf.append('\n');
        buf.append("- date d'ouverture:");
        buf.append(getDateOuvertureDossier());
        buf.append('\n');
        buf.append("- responsable:");
        buf.append(getResponsableDossier());
        buf.append('\n');
        buf.append("- assuré:");
        buf.append(getIdTiersAssure());
        buf.append('\n');
        buf.append("- conjoint:");
        buf.append(getIdTiersConjoint());
        buf.append('\n');
        buf.append("- motif:");
        buf.append(getIdMotifSplitting());
        buf.append('\n');
        buf.append("- état du dossier:");
        buf.append(getIdEtat());
        buf.append('\n');
        return buf.toString();
    }

    /**
     * Mise à jour lors de traitement journalier. Date de création : (15.10.2002 11:12:03)
     *
     * @param transaction
     *            la transaction à utiliser
     */
    public boolean updateDossier(BTransaction transaction, BITransaction remoteTransaction) throws Exception {
        // vérification
        if (CIDossierSplitting.CS_CLOTURE.equals(idEtat) || CIDossierSplitting.CS_REVOQUE.equals(idEtat)
                || CIDossierSplitting.CS_ANNULE.equals(idEtat)) {
            // aucun traitement, pas d'erreur
            return false;
        }
        boolean rCI95Done = false;
        boolean createTransaction = false;
        try {
            CIApplication application = (CIApplication) getSession().getApplication();
            BISession remoteSession = null;
            if (remoteTransaction == null) {
                remoteSession = application.getSessionAnnonce(getSession());
                remoteTransaction = ((BSession) remoteSession).newTransaction();
                remoteTransaction.openTransaction();
                createTransaction = true;
            } else {
                remoteSession = remoteTransaction.getISession();
            }
            // pas nécecssaire de valider
            wantCallValidate(false);
            // demande ouverture CI pour l'assuré et le conjoint
            if (CIDossierSplitting.CS_OUVERTURE_DOSSIER.equals(idEtat)) {
                // recherche CI pour assuré
                CICompteIndividuelManager ciManager = new CICompteIndividuelManager();
                ciManager.setSession(getSession());
                ciManager.orderByAvs(false);
                CICompteIndividuel ciToCheck = ciManager.getCIRegistreAssures(getIdTiersAssure(), transaction);
                if ((ciToCheck == null) || !ciToCheck.isCiOuvert().booleanValue()) {
                    // aucun ci ouvert --> ARC65 --> Devenu ARC 61 depuis le 01.01.19
                    HashMap attributs = new HashMap();
                    attributs.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");
                    attributs.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, "61");
                    // assuré
                    attributs.put(IHEAnnoncesViewBean.NUMERO_ASSURE, getIdTiersAssure());
                    // ref interne
                    String service = "PAVO";
                    if (!JadeStringUtil.isBlankOrZero(getReferenceService())) {
                        service = getReferenceService();
                    }
                    attributs.put(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE, service + "/" + getIdTiersAssure());
                    String idAnnonce = application.annonceARC(transaction, attributs, true);
                    if (idAnnonce != null) {
                        // changer état
                        setIdEtat(CIDossierSplitting.CS_DEMANDE_OUVERTURE_CI);
                        // mémorisé dans idArc65 qui est devenu 61 (depuis 01.01.19)
                        setIdArc65Assure(idAnnonce);
                        // sauve l'état actuel
                        // update(transaction);
                        // transaction.commit();
                    }
                }
                // recherche CI pour conjoint
                ciToCheck = ciManager.getCIRegistreAssures(getIdTiersConjoint(), transaction);
                if ((ciToCheck == null) || !ciToCheck.isCiOuvert().booleanValue()) {
                    // aucun ci ouvert --> ARC65 --> Devenu ARC 61 depuis le 01.01.19
                    HashMap attributs = new HashMap();
                    attributs.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");
                    attributs.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, "61");
                    // conjoint
                    attributs.put(IHEAnnoncesViewBean.NUMERO_ASSURE, getIdTiersConjoint());
                    // ref interne
                    String service = "PAVO";
                    if (!JadeStringUtil.isBlankOrZero(getReferenceService())) {
                        service = getReferenceService();
                    }
                    attributs.put(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE, service + "/" + getIdTiersAssure());
                    String idAnnonce = application.annonceARC(transaction, attributs, true);
                    if (idAnnonce != null) {
                        // changer état
                        setIdEtat(CIDossierSplitting.CS_DEMANDE_OUVERTURE_CI);
                        // mémorisé dans idArc65 qui est devenu 61 (depuis 01.01.19)
                        setIdArc65Conjoint(idAnnonce);
                        // sauve l'état actuel
                        // update(transaction);
                        // transaction.commit();
                    }
                }
            }
            // demande d'extrait CI
            if (CIDossierSplitting.CS_OUVERTURE_DOSSIER.equals(idEtat)
                    || CIDossierSplitting.CS_DEMANDE_OUVERTURE_CI.equals(idEtat)) {
                // test si CI est requis
                HashMap attributs = new HashMap();
                attributs.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");
                attributs.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, "98");
                if (isExtraitCiRequisAssure().booleanValue()) {
                    // ARC98
                    // assuré
                    attributs.put(IHEAnnoncesViewBean.NUMERO_ASSURE, getIdTiersAssure());
                    // ref interne
                    String service = "PAVO";
                    if (!JadeStringUtil.isBlankOrZero(getReferenceService())) {
                        service = getReferenceService();
                    }
                    attributs.put(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE,
                            service + "/" + NSUtil.unFormatAVS(getIdTiersAssure()));
                    String idAnnonce = application.annonceARC(transaction, attributs, true);
                    if (idAnnonce != null) {
                        // changer état
                        setIdEtat(CIDossierSplitting.CS_EXTRAIT_EN_COURS);
                        // mémorisé dans idArc65
                        setIdArc98Assure(idAnnonce);
                        // sauve l'état actuel
                        // update(transaction);
                        // transaction.commit();
                    }
                }
                if (isExtraitCiRequisConjoint().booleanValue()) {
                    // conjoint
                    attributs.put(IHEAnnoncesViewBean.NUMERO_ASSURE, getIdTiersConjoint());
                    // ref interne
                    String service = "PAVO";
                    if (!JadeStringUtil.isBlankOrZero(getReferenceService())) {
                        service = getReferenceService();
                    }
                    attributs.put(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE,
                            service + "/" + NSUtil.unFormatAVS(getIdTiersAssure()));
                    String idAnnonce = application.annonceARC(transaction, attributs, true);
                    if (idAnnonce != null) {
                        // changer état
                        setIdEtat(CIDossierSplitting.CS_EXTRAIT_EN_COURS);
                        // mémorisé dans idArc65
                        setIdArc98Conjoint(idAnnonce);
                        // sauve l'état actuel
                        // update(transaction);
                        // transaction.commit();
                    }
                }
            }
            // rassemblement CI
            if (CIDossierSplitting.CS_EXTRAIT_EN_COURS.equals(idEtat)) {
                // vérification extrait CI
                IHEOutputAnnonce remoteLectureAnnonce = (IHEOutputAnnonce) remoteSession
                        .getAPIFor(IHEOutputAnnonce.class);
                remoteLectureAnnonce
                        .setMethodsToLoad(new String[] { "getIdAnnonce", "getAllAnnonceRetour", "getInputTable" });
                IHEOutputAnnonce remoteLectureAnnonceConj = (IHEOutputAnnonce) remoteSession
                        .getAPIFor(IHEOutputAnnonce.class);
                remoteLectureAnnonceConj
                        .setMethodsToLoad(new String[] { "getIdAnnonce", "getAllAnnonceRetour", "getInputTable" });
                if (isExtraitCiRequisAssure().booleanValue()) {
                    remoteLectureAnnonce.setIdAnnonce(getIdArc98Assure());
                    remoteLectureAnnonce.retrieve(remoteTransaction);
                    // System.out.println("98A for "+getIdTiersAssure()+" retrieved, retour
                    // "+(remoteLectureAnnonce.getAllAnnonceRetour()?"ok":"nok"));
                }
                // test extrait assuré
                if (!isExtraitCiRequisAssure().booleanValue()
                        || remoteLectureAnnonce.getAllAnnonceRetour().booleanValue()) {
                    if (isExtraitCiRequisConjoint().booleanValue()) {
                        remoteLectureAnnonceConj.setIdAnnonce(getIdArc98Conjoint());
                        remoteLectureAnnonceConj.retrieve(remoteTransaction);
                        // System.out.println("98C for "+getIdTiersAssure()+" retrieved, retour
                        // "+(remoteLectureAnnonce.getAllAnnonceRetour()?"ok":"nok"));
                    }
                    // test extrait conjoint
                    if (remoteLectureAnnonceConj.getAllAnnonceRetour() != null) {
                        if (!isExtraitCiRequisConjoint().booleanValue()
                                || remoteLectureAnnonceConj.getAllAnnonceRetour().booleanValue()) {
                            setIdEtat(CIDossierSplitting.CS_RCI_COMPLET);
                            // System.out.println("Dossier updated");
                        }
                    } else {
                        if (!isExtraitCiRequisConjoint().booleanValue()) {
                            setIdEtat(CIDossierSplitting.CS_RCI_COMPLET);
                        }
                    }

                }
            }
            // définition des mandat de splitting
            if (CIDossierSplitting.CS_RCI_COMPLET.equals(idEtat)
                    || CIDossierSplitting.CS_OUVERTURE_DOSSIER.equals(idEtat)
                    || CIDossierSplitting.CS_DEMANDE_OUVERTURE_CI.equals(idEtat)) {
                boolean verified = true;
                // CI ouvert?
                CICompteIndividuelManager ciManager = new CICompteIndividuelManager();
                ciManager.setSession(getSession());
                ciManager.orderByAvs(false);
                CICompteIndividuel ciAssure = ciManager.getCIRegistreAssures(getIdTiersAssure(), transaction);
                if ((ciAssure == null) || !ciAssure.isCiOuvert().booleanValue()) {
                    verified = false;
                }
                // idem pour le conjoint
                CICompteIndividuel ciConjoint = ciManager.getCIRegistreAssures(getIdTiersConjoint(), transaction);
                if ((ciConjoint == null) || !ciConjoint.isCiOuvert().booleanValue()) {
                    verified = false;
                }
                if (extraitCiRequisAssure.booleanValue() || extraitCiRequisConjoint.booleanValue()) {
                    if (!CIDossierSplitting.CS_RCI_COMPLET.equals(idEtat)) {
                        verified = false;
                    }
                }
                if (verified) {
                    // test sur les données des CI ouverts
                    if (this.checkWithCI(transaction, ciAssure, ciConjoint)) {
                        // CI ok, l'utilisateur peut maintenant définir les
                        // mandats
                        setIdEtat(CIDossierSplitting.CS_OUVERT);
                    }
                }
            }
            // état splitting
            if (CIDossierSplitting.CS_SPLITTING_EN_COURS.equals(idEtat)
                    || CIDossierSplitting.CS_ERREUR.equals(idEtat)) {
                if (isAllMandatAsState(getIdTiersAssure(), CIMandatSplitting.CS_TERMINE, transaction)
                        && isAllMandatAsState(getIdTiersConjoint(), CIMandatSplitting.CS_TERMINE, transaction)) {
                    // tous les mandats sont terminés
                    // Le dossier reste en erreur
                    if (!CIDossierSplitting.CS_ERREUR.equals(idEtat)) {
                        setIdEtat(CIDossierSplitting.CS_SPLITTING_COMPLET);
                    }
                    // RCI est à imprimer
                    rCI95Done = true;
                }
            }
            // demande CA
            if (CIDossierSplitting.CS_SPLITTING_COMPLET.equals(idEtat)) {
                // ARC33
                HashMap attributs = new HashMap();
                attributs.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");
                attributs.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, "33");
                // assuré
                if (isCaRequisAssure().booleanValue()) {
                    attributs.put(IHEAnnoncesViewBean.NUMERO_ASSURE, getIdTiersAssure());
                    // ref interne
                    attributs.put(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE,
                            "PAVO/" + NSUtil.unFormatAVS(getIdTiersAssure()));
                    String user33 = "";
                    try {
                        user33 = getUser(transaction).getVisa();
                    } catch (Exception e) {
                        user33 = getSession().getUserId();
                    }
                    String idAnnonce = application.annonceARC(transaction, attributs, true, true, user33);
                    if (idAnnonce != null) {
                        // changer état
                        setIdEtat(CIDossierSplitting.CS_DEMANDE_CA);
                        // mémorisé dans idArc33
                        setIdArc33Assure(idAnnonce);
                        // sauve l'état actuel
                        // update(transaction);
                        // transaction.commit();
                    }
                }
                // conjoint
                if (isCaRequisConjoint().booleanValue()) {
                    attributs.put(IHEAnnoncesViewBean.NUMERO_ASSURE, getIdTiersConjoint());
                    // ref interne
                    attributs.put(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE,
                            "PAVO/" + NSUtil.unFormatAVS(getIdTiersAssure()));
                    String user33 = "";
                    try {
                        user33 = getUser(transaction).getVisa();
                    } catch (Exception e) {
                        user33 = getSession().getUserId();
                    }
                    String idAnnonce = application.annonceARC(transaction, attributs, true, true, user33);
                    if (idAnnonce != null) {
                        // changer état
                        setIdEtat(CIDossierSplitting.CS_DEMANDE_CA);
                        // mémorisé dans idArc33
                        setIdArc33Conjoint(idAnnonce);
                        // sauve l'état actuel
                        // update(transaction);
                        // transaction.commit();
                    }
                }
            }
            // clôture dossier
            if (CIDossierSplitting.CS_DEMANDE_CA.equals(idEtat)
                    || CIDossierSplitting.CS_SPLITTING_COMPLET.equals(idEtat)) {
                boolean finished = true;
                // ARC33 quittancés?
                IHEOutputAnnonce remoteLectureAnnonce = (IHEOutputAnnonce) remoteSession
                        .getAPIFor(IHEOutputAnnonce.class);
                remoteLectureAnnonce.setMethodsToLoad(new String[] { "getIdAnnonce", "getConfirmed", "getInputTable" });
                if (isCaRequisAssure().booleanValue()) {
                    // System.out.print("33 needed for "+getIdTiersAssure()+" (id="+getIdArc33Assure()+")");
                    remoteLectureAnnonce.setIdAnnonce(getIdArc33Assure());
                    remoteLectureAnnonce.retrieve(remoteTransaction);
                    // test CA assuré quittancé
                    if (!remoteLectureAnnonce.getConfirmed().booleanValue()) {
                        // System.out.println(" not confirmed");
                        finished = false;
                    } else {
                        // System.out.println(" confirmed");
                    }
                }
                if (isCaRequisConjoint().booleanValue()) {
                    // System.out.print("33 needed for "+getIdTiersConjoint()+" (id="+getIdArc33Conjoint()+")");
                    remoteLectureAnnonce.setIdAnnonce(getIdArc33Conjoint());
                    remoteLectureAnnonce.retrieve(remoteTransaction);
                    // test CA conjoint quittancé
                    if (!remoteLectureAnnonce.getConfirmed().booleanValue()) {
                        // System.out.println(" not confirmed");
                        finished = false;
                    } else {
                        // System.out.println(" confirmed");
                    }
                }
                if (finished) {
                    setIdEtat(CIDossierSplitting.CS_CLOTURE);
                    // email si ok
                    StringBuffer infoAssure = new StringBuffer();
                    infoAssure.append('\n');
                    infoAssure.append(getSession().getLabel("MSG_DOSSIER_EMAIL_NO_AVS"));
                    infoAssure.append(getIdTiersAssure());
                    infoAssure.append('\n');
                    infoAssure.append(getSession().getLabel("MSG_DOSSIER_EMAIL_NOM"));
                    String nomPersonne = getTiersAssureNomComplet();
                    // application.getTiersByAvs(transaction,
                    // getIdTiersAssure(), new String[] { "getNom" }).getNom();
                    if (nomPersonne == null) {
                        nomPersonne = "";
                    }
                    infoAssure.append(nomPersonne);
                    infoAssure.append('\n');
                    infoAssure.append(getSession().getLabel("MSG_DOSSIER_EMAIL_NO_AVSC"));
                    infoAssure.append(getIdTiersConjoint());
                    infoAssure.append('\n');
                    infoAssure.append(getSession().getLabel("MSG_DOSSIER_EMAIL_NOMC"));
                    nomPersonne = getTiersConjointNomComplet();
                    // application.getTiersByAvs(transaction,
                    // getIdTiersConjoint(), new String[] { "getNom"
                    // }).getNom();
                    if (nomPersonne == null) {
                        nomPersonne = "";
                    }
                    infoAssure.append(nomPersonne);
                    String message = java.text.MessageFormat.format(
                            getSession().getLabel("MSG_DOSSIER_EMAIL_MESSAGE_CL"),
                            new Object[] { infoAssure.toString() });
                    String to = getEMailResponsable(transaction);
                    if (JAUtil.isStringEmpty(to)) {
                        // adresse non trouvée -> envoie à admin
                        ((CIApplication) getSession().getApplication())
                                .sendEmailToAdmin(getSession().getLabel("MSG_DOSSIER_EMAIL_SUJET_CL"), message, this);
                    } else {
                        // adresse trouvée
                        JadeSmtpClient.getInstance().sendMail(to, getSession().getLabel("MSG_DOSSIER_EMAIL_SUJET_CL"),
                                message, null);
                        // sendEMail(getSession().getLabel("MSG_DOSSIER_EMAIL_SUJET_CL"),
                        // message);
                    }
                }
            }
            // révocation
            if (CIDossierSplitting.CS_DEMANDE_REVOCATION.equals(idEtat)) {
                if (isAllMandatAsState(getIdTiersAssure(), CIMandatSplitting.CS_REVOQUE, transaction)
                        && isAllMandatAsState(getIdTiersConjoint(), CIMandatSplitting.CS_REVOQUE, transaction)) {
                    // si tous révoqués
                    setIdEtat(CIDossierSplitting.CS_REVOQUE);
                    StringBuffer infoAssure = new StringBuffer();
                    infoAssure.append('\n');
                    infoAssure.append(getSession().getLabel("MSG_DOSSIER_EMAIL_NO_AVS"));
                    infoAssure.append(getIdTiersAssure());
                    infoAssure.append('\n');
                    infoAssure.append(getSession().getLabel("MSG_DOSSIER_EMAIL_NOM"));
                    String nomPersonne = application
                            .getTiersByAvs(transaction, getIdTiersAssure(), new String[] { "getNom" }).getNom();
                    if (nomPersonne == null) {
                        nomPersonne = "";
                    }
                    infoAssure.append(nomPersonne);
                    infoAssure.append('\n');
                    infoAssure.append(getSession().getLabel("MSG_DOSSIER_EMAIL_NO_AVSC"));
                    infoAssure.append(getIdTiersConjoint());
                    infoAssure.append('\n');
                    infoAssure.append(getSession().getLabel("MSG_DOSSIER_EMAIL_NOMC"));
                    nomPersonne = application
                            .getTiersByAvs(transaction, getIdTiersConjoint(), new String[] { "getNom" }).getNom();
                    if (nomPersonne == null) {
                        nomPersonne = "";
                    }
                    infoAssure.append(nomPersonne);
                    String message = java.text.MessageFormat.format(
                            getSession().getLabel("MSG_DOSSIER_EMAIL_MESSAGE_RE"),
                            new Object[] { infoAssure.toString() });
                    String to = getEMailResponsable(transaction);
                    if (JAUtil.isStringEmpty(to)) {
                        // adresse non trouvée -> envoie à admin
                        ((CIApplication) getSession().getApplication())
                                .sendEmailToAdmin(getSession().getLabel("MSG_DOSSIER_EMAIL_SUJET_RE"), message, this);
                    } else {
                        JadeSmtpClient.getInstance().sendMail(to, getSession().getLabel("MSG_DOSSIER_EMAIL_SUJET_RE"),
                                message, null);
                    }
                }
            }
            // sauver
            this.update(transaction);
            // if(transaction.hasErrors()) {
            // System.out.println("Transaction error:"+transaction.getErrors());
            // }
            // System.out.println("State: "+CodeSystem.getLibelle(getIdEtat(),getSession()));
            wantCallValidate(true);
            if (createTransaction) {
                if (remoteTransaction.isRollbackOnly()) {
                    remoteTransaction.rollback();
                } else {
                    remoteTransaction.commit();
                }
                // remoteTransaction.closeTransaction();
            }
        } catch (Exception ex) {
            if (createTransaction) {
                remoteTransaction.rollback();
                // remoteTransaction.closeTransaction();
            }
            throw ex;
        } finally {
            if (createTransaction) {
                remoteTransaction.closeTransaction();
            }
        }
        return rCI95Done;

    }

    public java.lang.String getIdArc65Assure() {
        return idArc65Assure;
    }

    public java.lang.String getIdArc65Conjoint() {
        return idArc65Conjoint;
    }

    public void setIdArc65Assure(java.lang.String idArc65Assure) {
        this.idArc65Assure = idArc65Assure;
    }

    public void setIdArc65Conjoint(java.lang.String idArc65Conjoint) {
        this.idArc65Conjoint = idArc65Conjoint;
    }

}
