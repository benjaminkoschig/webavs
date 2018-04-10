package globaz.pavo.db.compte;

import globaz.commons.nss.NSUtil;
import globaz.framework.secure.user.FWSecureUser;
import globaz.framework.secure.user.FWSecureUserDetail;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAStringFormatter;
import globaz.globall.util.JAUtil;
import globaz.hermes.utils.HEUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.util.AFUtil;
import globaz.pavo.application.CIApplication;
import globaz.pavo.process.CIEcritureFastManager;
import globaz.pavo.translation.CodeSystem;
import globaz.pavo.util.CIAvsUtil;
import globaz.pavo.util.CIUtil;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Object repr�sentant le compte individuel. Date de cr�ation : (12.11.2002 11:58:41)
 * 
 * @author: David Girardin
 */
public class CICompteIndividuel extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static int CHAMPS_AS_CS_PAYS = 2;
    public final static int CHAMPS_AS_CS_SEXE = 1;
    // Acc�s s�curit�
    public final static String CS_ACCESS_0 = "317000";
    public final static String CS_ACCESS_5 = "317005";
    public final static String CS_ACCESS_7 = "317007";
    public final static String CS_CODE_IRRECOUVRABLE = "330001";
    public final static String CS_FEMME = "316001";

    // Sexe de l''assur�(e)
    public final static String CS_HOMME = "316000";
    public final static String CS_PAYS_TYPE = "CIPAYORI";
    public final static String CS_REGISTRE_ASSURES = "309001";
    public final static String CS_REGISTRE_GENRES_6 = "309002";
    public final static String CS_REGISTRE_GENRES_7 = "309003";
    public final static String CS_REGISTRE_HISTORIQUE = "309005";
    public final static String CS_REGISTRE_PROVISOIRE = "309004";
    public final static String CS_SEXE_TYPE = "CISEXE";
    public static int FROM_JOIN = 1;

    public static int FROM_RETRIEVE = 0;
    //
    public final static String SECURITE_LABEL = "SecureCode";
    public final static int CS_ACCESS = 317000;

    /**
     * Charge le CI du tiers donn�. Date de cr�ation : (15.11.2002 08:39:41)
     * 
     * @return le CI du tiers ou null si inexistant.
     * @param idTiers
     *            le num�ro avs du tiers.
     * @param transaction
     *            la transaction � utiliser.
     */
    public static CICompteIndividuel loadCI(String nss, BTransaction transaction) throws Exception {
        if (nss == null) {
            return null;
        }
        CICompteIndividuelManager ciManager = new CICompteIndividuelManager();
        ciManager.setSession(transaction.getSession());
        ciManager.orderByAvs(false);
        ciManager.setForNumeroAvs(nss);
        ciManager.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        ciManager.find(transaction);
        if (ciManager.size() == 0) {
            return null;
        }
        return (CICompteIndividuel) ciManager.getEntity(0);
    }

    /**
     * Lis les CI "RA" et "Provisoire" du tiers donn�.<br>
     * Si un un RA ET un provisoire existent, le RA est renvoy�.<br>
     * Envoie un email aux responsables CI si un provisoire et un au RA a �t� trouv�. Date de cr�ation : (15.11.2002
     * 08:39:41)
     * 
     * @return le RA ou provisoire trouv�, null si inexistant.
     * @param idTiers
     *            le num�ro avs du tiers.
     * @param transaction
     *            la transaction � utiliser.
     */
    public static CICompteIndividuel loadCITemporaire(String idTiers, BTransaction transaction) throws Exception {
        if (idTiers == null) {
            return null;
        }
        CICompteIndividuel result = null;
        CICompteIndividuelManager ciManager = new CICompteIndividuelManager();
        ciManager.setSession(transaction.getSession());
        ciManager.setForNumeroAvs(idTiers);
        ciManager.orderByAvs(true);
        ciManager.find(transaction);
        boolean raTrouve = false;
        // pour tous les ci trouv�s
        for (int i = 0; i < ciManager.size(); i++) {
            CICompteIndividuel compte = (CICompteIndividuel) ciManager.getEntity(i);
            if (CICompteIndividuel.CS_REGISTRE_ASSURES.equals(compte.getRegistre())) {
                // CI en "RA" est trouv�
                raTrouve = true;
                result = compte;
            }
            if (CICompteIndividuel.CS_REGISTRE_PROVISOIRE.equals(compte.getRegistre())) {
                // CI provisoire trouv�
                if (!raTrouve) {
                    result = compte;
                } else {
                    // si un RA a d�j� �t� trouv�, garder le RA comme r�f�rence
                    // et envoi d'un email
                    try {
                        CIApplication application = (CIApplication) globaz.globall.db.GlobazServer.getCurrentSystem()
                                .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO);
                        ArrayList emails = application.getEMailResponsableCI(transaction);
                        for (int j = 0; j < emails.size(); j++) {

                            JadeSmtpClient.getInstance().sendMail(
                                    (String) emails.get(j),
                                    application.getLabel("MSG_CI_EMAIL_SUJET", transaction.getSession()
                                            .getIdLangueISO()),
                                    application.getLabel("MSG_CI_EMAIL_MESSAGE", transaction.getSession()
                                            .getIdLangueISO())
                                            + " " + compte.getNssFormate(), null);
                        }
                    } catch (Exception ex) {
                        // envoi impossible
                    }
                }
            }
        }
        return result;
    }

    /** (KATSEC) */
    private String accesSecurite = new String();
    /** (KANOUV) */
    private String anneeOuverture = new String();
    // no caisse / agence
    private String caisseAgence = new String();
    private String caisseTenantCI = new String();
    // flag de modification des attributs
    private boolean changed = false;
    // ci li�s
    private ArrayList ciLies;
    /** (KABOUV) */
    private Boolean ciOuvert = new Boolean(false);
    private String codeIrrecouverable = "";
    /** (KAIIND) */
    private String compteIndividuelId = new String();
    /** (KAIINR) */
    private String compteIndividuelIdReference = new String();
    /** Interne */
    private String compteIndividuelIdReferenceSave = new String();
    /** (KADCRE) */
    private String dateCreation = new String();
    /** (KADNAI) */
    private String dateNaissance = new String();
    /** (KAICAI) */
    private String derniereCaisse = new String();
    /** (KADCLO) */
    private String derniereCloture = new String();
    /** (KAIEMP) */
    private String dernierEmployeur = new String();
    /** (KAIARC) */
    private String dernierMotifCloture = new String();
    private String dernierMotifOuverture = new String();
    private int etatEntity = CICompteIndividuel.FROM_RETRIEVE;
    /** (KABINA) */
    private Boolean inactive = new Boolean(false);
    private String infoAff = new String();
    /** (KABINV) */
    private Boolean invalide = new Boolean(false);
    private String likeInIdAffiliation = new String();
    // num�ro avs s�lectionn� pour affich� le compte.
    private String mainSelectedId = new String();
    /** (KABNNS) */
    private Boolean nnss = new Boolean(false);
    // affili�
    private String noAffilie = new String();
    /** (KALNOM) */
    private String nomPrenom = new String();
    /** (KANAVS) */
    private String numeroAvs = new String();
    /** (KANAVA) */
    private String numeroAvsAncien = new String();
    private String numeroAvsNNSS = "";
    /** (KANAVP) */
    private String numeroAvsPrecedant = new String();
    /** (KAIPAY) */
    private String paysOrigineId = new String();
    private boolean plausiNumAvs = false;
    /** (KABFUS) */
    private Boolean provenanceFusion = new Boolean(false);
    /** (KALREF) */
    private String referenceInterne = new String();
    /** (KAIREG) */
    private String registre = new String();
    /** (KATSEX) */
    private String sexe = new String();

    /** (KANSRC) */
    private String srcUpi = "";

    /** (KALUSC) */
    private String utilisateurCreation = new String();

    private String utilisateurNomComplet;

    /**
     * Commentaire relatif au constructeur CICompteIndividuel
     */
    public CICompteIndividuel() {
        super();
        String[] methods = new String[] { "getAnneeOuverture", "getCompteIndividuelId", "getDateClotureMMAA",
                "getDateCreation", "getDateNaissance", "getDernierMotifArc", "getNomPrenom",
                "getNoNomDernierEmployeur", "getNumeroAvs", "getReferenceInterne", "getRegistre", "getSexe",
                "getCiOuvert", "getIsAPI"

        };
        setMethodsToLoad(methods);
    }

    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {

    }

    /**
     * Effectue des traitements apr�s une lecture dans la BD et apr�s avoir vid� le tampon de lecture
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws java.lang.Exception {
        // cherche le nom complet de l'utilisateur qui a cr�� le CI
        CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                CIApplication.DEFAULT_APPLICATION_PAVO);
        if (!isLoadedFromManager()) {
            chargeNomComplet(transaction);
        }
        if (!JAUtil.isIntegerEmpty(derniereCaisse) && (getEtatEntity() != CICompteIndividuel.FROM_JOIN)) {
            if (CIApplication.DEFAULT_APPLICATION_PAVO.equalsIgnoreCase(getSession().getApplicationId())) {
                caisseAgence = application.getAdministration(getSession(), derniereCaisse,
                        new String[] { "getCodeAdministration" }).getCodeAdministration();
            }
        }
        if (transaction.isOpened() && (getEtatEntity() != CICompteIndividuel.FROM_JOIN)) {
            if (!JAUtil.isIntegerEmpty(getDernierEmployeur())) {
                try {
                    if (!isLoadedFromManager()) {
                        AFAffiliation aff = application.getAffilie(transaction, getDernierEmployeur(), new String[] {
                                "getAffilieNumero", "getIdTiers", "getTiers" });
                        if ((aff != null) && !JAUtil.isIntegerEmpty(aff.getTiers().getIdTiers())
                                && (aff.getTiers() != null)) {
                            infoAff = aff.getAffilieNumero();
                            infoAff += "\n" + aff.getTiers().getNom();
                            infoAff += "\n" + aff.getTiers().getLocaliteLong();
                        }
                    } else {
                        AFAffiliation aff = application.getAffilie(transaction, getDernierEmployeur(), new String[] {
                                "getAffilieNumero", "getIdTiers" });
                        if ((aff != null) && !JAUtil.isIntegerEmpty(aff.getIdTiers())) {
                            noAffilie = aff.getAffilieNumero();
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        // gpi - Sauvegarde du compteIndividuelIdReference
        compteIndividuelIdReferenceSave = compteIndividuelIdReference;

    }

    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
    }

    /**
     * Reset de l'id si entity en erreur
     */
    @Override
    protected void _alwaysAfterAdd(BTransaction transaction) throws java.lang.Exception {
        if (transaction.hasErrors()) {
            setCompteIndividuelId("");
        }
    }

    /**
     * Effectue des traitements avant un ajout dans la BD
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // num�ro avs
        if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(), "01.01.2006", JACalendar.todayJJsMMsAAAA())) {
            // apr�s 2006
            setNumeroAvsAncien("");
        } else {
            setNumeroAvsAncien(getNumeroAvs());
        }
        // fusion
        setProvenanceFusion(new Boolean(false));
        // date cr�ation
        if (JAUtil.isStringEmpty(getDateCreation())) {
            setDateCreation(JACalendar.todayJJsMMsAAAA());
        }
        // utilisateur
        if (JAUtil.isStringEmpty(getUtilisateurCreation())) {
            setUtilisateurCreation(getSession().getUserId());
        }
        if (JAUtil.isIntegerEmpty(getPaysOrigineId())) {
            setPaysOrigineId("315999");
        }
        // code s�curit�
        if (JAUtil.isIntegerEmpty(accesSecurite)) {
            setAccesSecuriteNoCheck(CICompteIndividuel.CS_ACCESS_0);
        }
        // registre
        if (JAUtil.isIntegerEmpty(getRegistre())) {
            setRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        }
        // incr�mente de +1 le num�ro
        if (JAUtil.isIntegerEmpty(compteIndividuelId)) {
            setCompteIndividuelId(this._incCounter(transaction, "0"));
        }

        if (getInvalide()) {
            setCiOuvert(false);
        }
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws java.lang.Exception {
        // suppression impossible
        if (hasUserEditCIRight(transaction)) {
            // test si le CI contient des �critures
            CIEcritureManager ecrMgr = new CIEcritureManager();
            ecrMgr.setSession(getSession());
            ecrMgr.setForCompteIndividuelId(getCompteIndividuelId());
            ecrMgr.setForNotIdTypeCompte(CIEcriture.CS_CORRECTION);
            ecrMgr.find(transaction);
            if (ecrMgr.size() != 0) {
                _addError(transaction, getSession().getLabel("MSG_CI_SUPPRESSION"));
            }
        } else {
            // suppression impossible
            _addError(transaction, getSession().getLabel("MSG_CI_SUPPRESSION"));
        }
    }

    /**
     * Effectue des traitements avant une mise � jour dans la BD
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws java.lang.Exception {
        if (!compteIndividuelIdReferenceSave.equals(compteIndividuelIdReference)) {
            if (!transaction.hasErrors()) {
                if (JAUtil.isIntegerEmpty(compteIndividuelIdReferenceSave)
                        || (getCompteIndividuelId().equals(compteIndividuelIdReferenceSave) && !JAUtil
                                .isIntegerEmpty(compteIndividuelIdReference))) {
                    // nouvelle liaison
                    // recherche du parent
                    CICompteIndividuel compte = new CICompteIndividuel();
                    compte.setSession(transaction.getSession());
                    compte.setCompteIndividuelId(compteIndividuelIdReference);
                    compte.retrieve(transaction);
                    if (!compte.isNew()) {
                        setCompteIndividuelIdReference("0"); // n�cessaire pour
                        // ne pas forcer
                        // sur un CI
                        compte.addLiaison(transaction, this, true, false);
                    }
                } else {
                    CICompteIndividuel compte;
                    // recherche de l'ancien parent
                    if (compteIndividuelIdReferenceSave.equals(compteIndividuelId)) {
                        // parent = ci
                        compte = this;
                    } else {
                        compte = new CICompteIndividuel();
                        compte.setSession(transaction.getSession());
                        compte.setCompteIndividuelId(compteIndividuelIdReferenceSave);
                        compte.retrieve(transaction);
                    }
                    if (!compte.isNew()) {
                        if (JAUtil.isIntegerEmpty(compteIndividuelIdReference)) {
                            // supprimer la liaison
                            removeLiaison(transaction, compte);
                        } else if (compteIndividuelIdReference.equals(compteIndividuelId)) {
                            // ci root -> suppression et nouvel ajout
                            removeLiaison(transaction, compte);
                            // compte.setCompteIndividuelIdReference("0");
                            this.addLiaison(transaction, compte, false);
                        }
                    }
                }
            }
        }

        if (getInvalide()) {
            setCiOuvert(false);
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CIINDIP";
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propri�t�s �choue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        compteIndividuelId = statement.dbReadNumeric("KAIIND");
        compteIndividuelIdReference = statement.dbReadNumeric("KAIINR");
        numeroAvs = statement.dbReadString("KANAVS");
        nomPrenom = statement.dbReadString("KALNOM");
        paysOrigineId = statement.dbReadNumeric("KAIPAY");
        dateNaissance = CIUtil.formatDate(statement.dbReadNumeric("KADNAI"));
        sexe = statement.dbReadNumeric("KATSEX");
        anneeOuverture = statement.dbReadNumeric("KANOUV");
        referenceInterne = statement.dbReadString("KALREF");
        accesSecurite = statement.dbReadNumeric("KATSEC");
        ciOuvert = statement.dbReadBoolean("KABOUV");
        dernierMotifCloture = statement.dbReadNumeric("KAIARC");
        dernierMotifOuverture = statement.dbReadNumeric("KAOARC");
        derniereCaisse = statement.dbReadNumeric("KAICAI");
        derniereCloture = statement.dbReadDateAMJ("KADCLO");
        provenanceFusion = statement.dbReadBoolean("KABFUS");
        dernierEmployeur = statement.dbReadNumeric("KAIEMP");
        numeroAvsAncien = statement.dbReadString("KANAVA");
        numeroAvsPrecedant = statement.dbReadString("KANAVP");
        registre = statement.dbReadNumeric("KAIREG");
        dateCreation = statement.dbReadDateAMJ("KADCRE");
        utilisateurCreation = statement.dbReadString("KALUSC");
        caisseTenantCI = statement.dbReadNumeric("KACAIT");
        codeIrrecouverable = statement.dbReadNumeric("KATIRR");
        nnss = statement.dbReadBoolean("KABNNS");
        if (nnss.booleanValue()) {
            numeroAvsNNSS = "true";
        } else {
            numeroAvsNNSS = "false";
        }
        srcUpi = statement.dbReadNumeric("KANSRC");
        inactive = statement.dbReadBoolean("KABINA");
        invalide = statement.dbReadBoolean("KABINV");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     */
    @Override
    protected void _validate(BStatement statement) {
        if (JAUtil.isStringEmpty(registre)) {
            // impossible si registre non renseign�
            _addError(statement.getTransaction(), getSession().getLabel("MSG_CI_VAL_REGISTRE"));
            return;
        }
        if (NSUtil.unFormatAVS(numeroAvs).trim().length() == 13) {
            setNumeroAvsNNSS("true");
            setNnss(new Boolean(true));
        }
        if ("true".equals(numeroAvsNNSS)) {
            setNnss(new Boolean(true));
            if (!JadeStringUtil.isBlankOrZero(numeroAvs)) {
                if (!NSUtil.nssCheckDigit(numeroAvs)) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_CI_VAL_AVS"));
                }
            }
        }
        if (!JAUtil.isStringEmpty(getNumeroAvs())) {
            if (CICompteIndividuel.CS_REGISTRE_ASSURES.equals(getRegistre())
                    || CICompteIndividuel.CS_REGISTRE_PROVISOIRE.equals(getRegistre())) {
                // test si l'assur� existe d�j� pour un ajout au RA ou
                // provisoire
                CICompteIndividuel ciExistant;
                try {
                    ciExistant = CICompteIndividuel.loadCITemporaire(getNumeroAvs(), statement.getTransaction());
                } catch (Exception ex) {
                    ciExistant = null;
                }
                if (ciExistant != null) {
                    if (CICompteIndividuel.CS_REGISTRE_ASSURES.equals(ciExistant.getRegistre())
                            || CICompteIndividuel.CS_REGISTRE_PROVISOIRE.equals(ciExistant.getRegistre())) {
                        // si pas m�me id, erreur: existe d�j�
                        if (!ciExistant.getCompteIndividuelId().equals(getCompteIndividuelId())) {
                            // erreur
                            _addError(statement.getTransaction(), getSession().getLabel("MSG_CI_EXIST") + " "
                                    + ciExistant.getCompteIndividuelId() + "/" + getCompteIndividuelId());
                        }
                    }
                }
                if (!plausiNumAvs) {
                    if ("true".equalsIgnoreCase(numeroAvsNNSS)) {
                        if (!NSUtil.nssCheckDigit(numeroAvs)) {
                            _addError(statement.getTransaction(), getSession().getLabel("MSG_CI_VAL_AVS"));
                        }

                    }
                }
            }
        }
        if (CICompteIndividuel.CS_REGISTRE_ASSURES.equals(registre)) {
            // Modif NNSS, si le nume est sur 13 pos, c'est un NNSS

            // num�ro avs
            // check du FW
            if ("false".equalsIgnoreCase(numeroAvsNNSS)) {
                _checkAVS(statement.getTransaction(), getNumeroAvs(), getSession().getLabel("MSG_CI_VAL_AVS"));
            } else if ("true".equalsIgnoreCase(numeroAvsNNSS)) {
                if (!NSUtil.nssCheckDigit(numeroAvs)) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_CI_VAL_AVS"));
                }
            }
            // date de naissance
            if (JAUtil.isStringEmpty(dateNaissance)) {
                if (!calculDateNaissance()) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_CI_VAL_NAISSANCE"));
                }
            }
            // sexe
            if (JAUtil.isIntegerEmpty(sexe)) {
                if (!calculSexe()) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_CI_VAL_SEXE"));
                }
            } else {
                _checkCode(statement.getTransaction(), CICompteIndividuel.CS_SEXE_TYPE, getSexe(), getSession()
                        .getLabel("MSG_CI_VAL_SEXE"));
            }
            // pays
            _checkCode(statement.getTransaction(), CICompteIndividuel.CS_PAYS_TYPE, getPaysOrigineId(), getSession()
                    .getLabel("MSG_CI_VAL_PAYS"));

            if (!plausiNumAvs && !"true".equals(numeroAvsNNSS)) {
                // check le pays
                if (!CIAvsUtil.checkPays(getPaysOrigineId(), getNumeroAvs())) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_CONTROLE_PAYS_NON_VALIDE"));
                }
                // param�tres ne int pour une portabilit�
                int sexeCheck = 0;
                if (CICompteIndividuel.CS_HOMME.equals(getSexe())) {
                    sexeCheck = 1;
                }
                if (CICompteIndividuel.CS_FEMME.equals(getSexe())) {
                    sexeCheck = 2;
                }
                if (JAUtil.isStringEmpty(getNomPrenom())) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_NOM_RENSEIGNE"));
                } else {
                    if (!JAUtil.isStringEmpty(getNumeroAvs()) && (getNumeroAvs().trim().length() == 11)) {
                        if (!JAUtil.isStringEmpty(getNomPrenom()) && !HEUtil.checkName(getNomPrenom())) {
                            _addError(statement.getTransaction(), getSession().getLabel("MSG_NOM_INVALIDE"));
                        } else {
                            setNomPrenom(CIAvsUtil.checkNomPrenom(getNomPrenom()));
                            // check le 1e groupe
                            if (!CIAvsUtil.checkPremierGroupe(getNomPrenom(), getNumeroAvs())) {
                                _addError(statement.getTransaction(),
                                        getSession().getLabel("MSG_PREMIER_GROUPE_NON_VALIDE"));
                            }
                        }
                    }
                }
                // check le 2e groupe
                if (!CIAvsUtil.checkDeuxiemeGroupe(getDateNaissance(), getNumeroAvs())) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_DEUXIEME_GROUPE_NON_VALIDE"));
                }
                // check le troisi�me groupe
                if (!CIAvsUtil.checkTroisiemeGroupe(getDateNaissance(), getNumeroAvs(), sexeCheck)) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_TROISIEME_GROUPE_NON_VALIDE"));
                }
                if (!CIAvsUtil.checkSexe(getNumeroAvs(), sexeCheck)) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_SEXE_NON_VALIDE"));
                }
            }
        }
        // caisse / agence
        if (!JAUtil.isStringEmpty(caisseAgence)) {
            if (caisseAgence.trim().length() <= 7) {
                // recherche de l'id tiers
                int sep = caisseAgence.indexOf('.');
                String caisse = null;
                String agence = null;
                if (sep > 0) {
                    caisse = caisseAgence.substring(0, sep);
                    agence = caisseAgence.substring(sep + 1);
                } else {
                    caisse = caisseAgence;
                }
                try {
                    if (sep > 0) {
                        CIApplication application = (CIApplication) getSession().getApplication();
                        derniereCaisse = application.getAdministration(getSession(), caisse, agence,
                                new String[] { "getIdTiersAdministration" }).getIdTiersAdministration();
                    } else {
                        derniereCaisse = getIdTierAdmin();
                    }
                } catch (Exception ex) {
                    // impossible d'appliquer le changement
                }
            }
        }

        if (getInactive() && getInvalide()) {
            _addError(statement.getTransaction(),
                    getSession().getLabel("CI_COMPTE_INDIVIDUEL_ERREUR_INACTIF_ET_INVALIDE"));
        }
    }

    /**

     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("KAIIND", this._dbWriteNumeric(statement.getTransaction(), getCompteIndividuelId(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("KAIIND",
                this._dbWriteNumeric(statement.getTransaction(), getCompteIndividuelId(), "compteIndividuelId"));
        statement.writeField("KAIINR", this._dbWriteNumeric(statement.getTransaction(),
                getCompteIndividuelIdReference(), "compteIndividuelIdReference"));
        statement.writeField("KANAVS", this._dbWriteString(statement.getTransaction(), getNumeroAvs(), "numeroAvs"));
        statement.writeField("KALNOM", this._dbWriteString(statement.getTransaction(), getNomPrenom(), "nomPrenom"));
        statement.writeField("KAIPAY",
                this._dbWriteNumeric(statement.getTransaction(), getPaysOrigineId(), "paysOrigineId"));
        statement.writeField("KADNAI", this._dbWriteNumeric(statement.getTransaction(),
                CIUtil.formatDateAMJ(getDateNaissance()), "dateNaissance"));
        statement.writeField("KATSEX", this._dbWriteNumeric(statement.getTransaction(), getSexe(), "sexe"));
        statement.writeField("KANOUV",
                this._dbWriteNumeric(statement.getTransaction(), getAnneeOuverture(), "anneeOuverture"));
        statement.writeField("KALREF",
                this._dbWriteString(statement.getTransaction(), getReferenceInterne(), "referenceInterne"));
        statement.writeField("KATSEC",
                this._dbWriteNumeric(statement.getTransaction(), getAccesSecurite(), "accesSecurite"));
        statement.writeField("KABOUV", this._dbWriteBoolean(statement.getTransaction(), isCiOuvert(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "ciOuvert"));
        statement.writeField("KAIARC", this._dbWriteNumeric(statement.getTransaction(),
                this.getDernierMotifCloture(false), "dernierMotifCloture"));
        statement.writeField("KAOARC",
                this._dbWriteNumeric(statement.getTransaction(), getDernierMotifOuverture(), "dernierMotifOuverture"));
        statement.writeField("KAICAI",
                this._dbWriteNumeric(statement.getTransaction(), getDerniereCaisse(), "derniereCaisse"));
        statement.writeField("KADCLO",
                this._dbWriteDateAMJ(statement.getTransaction(), this.getDerniereCloture(false), "derniereCloture"));
        statement.writeField("KABFUS", this._dbWriteBoolean(statement.getTransaction(), isProvenanceFusion(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "provenanceFusion"));
        statement.writeField("KAIEMP",
                this._dbWriteNumeric(statement.getTransaction(), getDernierEmployeur(), "dernierEmployeur"));
        statement.writeField("KANAVA",
                this._dbWriteString(statement.getTransaction(), getNumeroAvsAncien(), "numeroAvsAncien"));
        statement.writeField("KANAVP",
                this._dbWriteString(statement.getTransaction(), getNumeroAvsPrecedant(), "numeroAvsPrecedant"));
        statement.writeField("KAIREG", this._dbWriteNumeric(statement.getTransaction(), getRegistre(), "registre"));
        statement.writeField("KADCRE",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateCreation(), "dateCreation"));
        statement.writeField("KALUSC",
                this._dbWriteString(statement.getTransaction(), getUtilisateurCreation(), "utilisateurCreation"));
        statement.writeField("KACAIT",
                this._dbWriteNumeric(statement.getTransaction(), getCaisseTenantCI(), "caisseTenantCI"));
        statement.writeField("KABNNS",
                this._dbWriteBoolean(statement.getTransaction(), getNnss(), BConstants.DB_TYPE_BOOLEAN_CHAR, "nnss"));
        statement.writeField("KATIRR",
                this._dbWriteNumeric(statement.getTransaction(), getCodeIrrecouverable(), "irrecouvrable"));
        statement.writeField("KANSRC", this._dbWriteNumeric(statement.getTransaction(), getSrcUpi(), "srcUpi"));

        statement.writeField("KABINA", this._dbWriteBoolean(statement.getTransaction(), getInactive(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "inactive"));

        statement.writeField("KABINV", this._dbWriteBoolean(statement.getTransaction(), getInvalide(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "invalide"));
    }

    public void addLiaison(BTransaction transaction, CICompteIndividuel child, boolean updateParent) throws Exception {
        this.addLiaison(transaction, child, updateParent, true);
    }

    public void addLiaison(BTransaction transaction, CICompteIndividuel child, boolean updateParent, boolean updateChild)
            throws Exception {
        int loopCounter = 0;
        CICompteIndividuel ciParent = this;
        if (JAUtil.isIntegerEmpty(getCompteIndividuelIdReference())) {
            // ||
            // !root.getCompteIndividuelId().equals(root.getCompteIndividuelIdReference()))
            // {
            // premi�re liaison du root
            setCompteIndividuelIdReference(getCompteIndividuelId());
            if (updateParent) {
                wantCallValidate(false);
                wantCallMethodAfter(false);
                wantCallMethodBefore(false);
                this.save(transaction);
            }
        } else {
            // liaison d�j� existante, recherche du root le plus bas
            CICompteIndividuelManager ciMgr;
            do {
                if (ciParent.getCompteIndividuelId().equals(child.getCompteIndividuelId())) {
                    // d�j� li�
                    return;
                }
                ciMgr = new CICompteIndividuelManager();
                ciMgr.setSession(getSession());
                ciMgr.orderByAvs(false);
                ciMgr.setForCompteIndividuelReferenceId(ciParent.getCompteIndividuelId());
                ciMgr.setForNotCompteIndividuelId(ciParent.getCompteIndividuelId());
                ciMgr.changeManagerSize(1);
                ciMgr.wantCallMethodAfter(false);
                ciMgr.wantCallMethodBefore(false);
                ciMgr.find(transaction);
                if (!ciMgr.isEmpty()) {
                    // System.out.println("r�f inf � "+ciParent.getCompteIndividuelId()+" trouv�e");
                    CICompteIndividuel ciL = (CICompteIndividuel) ciMgr.getEntity(0);
                    ciParent = ciL;
                }
                loopCounter++;
            } while (!ciMgr.isEmpty() && (loopCounter != 10));
            // test si le child ne se trouve pas d�j� en-dessous (loop dans
            // reprise)
            loopCounter = 0;
            CICompteIndividuel ciLoop = ciParent;
            while (!ciLoop.getCompteIndividuelIdReference().equals(ciLoop.getCompteIndividuelId())
                    && (loopCounter != 10)) {
                if (ciLoop.getCompteIndividuelIdReference().equals(child.getCompteIndividuelId())) {
                    // loop dans reprise -> laisser tel quel
                    return;
                }
                CICompteIndividuel ciL = new CICompteIndividuel();
                ciL.setSession(getSession());
                ciL.setCompteIndividuelId(ciLoop.getCompteIndividuelIdReference());
                ciL.wantCallMethodAfter(false);
                ciL.wantCallMethodBefore(false);
                ciL.retrieve(transaction);
                if (!ciL.isNew()) {
                    // System.out.println("r�f sup � "+ciChild.getCompteIndividuelIdReference()+" trouv�e");
                    ciLoop = ciL;
                } else {
                    // ne devrait pas arriver
                    break;
                }
                loopCounter++;
            }
        }
        // recherche du child le plus haut
        loopCounter = 0;
        CICompteIndividuel ciChild = child;
        while (!JAUtil.isIntegerEmpty(ciChild.getCompteIndividuelIdReference())
                && !ciChild.getCompteIndividuelIdReference().equals(ciChild.getCompteIndividuelId())
                && (loopCounter != 10)) {
            CICompteIndividuel ciL = new CICompteIndividuel();
            ciL.setSession(getSession());
            ciL.setCompteIndividuelId(ciChild.getCompteIndividuelIdReference());
            ciL.wantCallMethodAfter(false);
            ciL.wantCallMethodBefore(false);
            ciL.retrieve(transaction);
            if (!ciL.isNew()) {
                // System.out.println("r�f sup � "+ciChild.getCompteIndividuelIdReference()+" trouv�e");
                ciChild = ciL;
            } else {
                // ne devrait pas arriver
                break;
            }
            loopCounter++;
        }
        if (loopCounter != 10) {
            ciChild.setCompteIndividuelIdReference(ciParent.getCompteIndividuelId());
            if (updateChild) {
                ciChild.wantCallValidate(false);
                ciChild.wantCallMethodAfter(false);
                ciChild.wantCallMethodBefore(false);
                ciChild.update(transaction);
            }
        }
    }

    /**
     * Envoie d'un CI additionnel. Toutes les �critures associ�es au ci sp�cifi� sont lues afin d'identifier celles qui
     * doivent �tre clotur�es. Ces derni�res seront li�es � la cl�ture et inclues dans un 38.
     * 
     * @param transaction
     *            la transaction � utiliser.
     * @param ci
     *            le ci concern�
     * @param ecrituresAuCI
     *            est � true si les �critures � tester sont celles au CI, false sinon.
     * @param journalInscrit
     *            le journal lors d'un inscription au CI de ce dernier ou null si non applicable. Date de cr�ation :
     *            (20.12.2002 08:19:35)
     */
    public void annonceCIAdditionnel(BTransaction transaction, ArrayList ecritures) throws Exception {
        new CIAnnonceCIAdditionnel(this).doCIAdditionnel(transaction, ecritures);
    }

    /**
     * Envoie d'un CI additionnel. Toutes les �critures associ�es au ci sp�cifi� sont lues afin d'identifier celles qui
     * doivent �tre clotur�es. Ces derni�res seront li�es � la cl�ture et inclues dans un 38.
     * 
     * @param transaction
     *            la transaction � utiliser.
     * @param ci
     *            le ci concern�
     * @param ecrituresAuCI
     *            est � true si les �critures � tester sont celles au CI, false sinon.
     * @param journalInscrit
     *            le journal lors d'un inscription au CI de ce dernier ou null si non applicable. Date de cr�ation :
     *            (20.12.2002 08:19:35)
     */
    public void annonceCIAdditionnel(BTransaction transaction, CIEcriture ecriture) throws Exception {
        new CIAnnonceCIAdditionnel(this).doCIAdditionnel(transaction, ecriture);
    }

    /**
     * Calcul la date de naissance en fonction du no avs.<br>
     * Ne fonctionne que pour une personne dont l'ann�e de naissance < 2000 Date de cr�ation : (29.04.2003 12:45:43)
     */
    public boolean calculDateNaissance() {
        if ((numeroAvs == null) || (numeroAvs.trim().length() < 8)) {
            return false;
        }
        String annee = "19" + numeroAvs.substring(3, 5);
        int multiple = Integer.parseInt(numeroAvs.substring(5, 6));
        if (multiple > 4) {
            setSexe(CICompteIndividuel.CS_FEMME);
        } else {
            setSexe(CICompteIndividuel.CS_HOMME);
        }
        multiple = ((multiple - 1) % 4) + 1;
        int mois = Integer.parseInt(numeroAvs.substring(6, 8));
        if (mois == 0) {
            // pour les cas 100, 500, mettre � 00.00.xxxx
            mois = 0;
        }
        int jj = ((mois - 1) % 31) + 1;
        int mm = ((mois - 1) / 31) + ((multiple - 1) * 3) + 1;
        if (mois == 0) {
            // pour les cas 100, 500, mettre � 00.00.xxxx
            jj = 0;
            mm = 0;
        }
        setDateNaissance(JadeStringUtil.rightJustifyInteger(String.valueOf(jj), 2) + "."
                + JadeStringUtil.rightJustifyInteger(String.valueOf(mm), 2) + "." + annee);
        return true;
    }

    /**
     * Recherche du sexe en fonction du no avs.<br>
     * Date de cr�ation : (29.04.2003 12:45:43)
     */
    public boolean calculSexe() {
        if ((numeroAvs == null) || (numeroAvs.trim().length() < 8)) {
            return false;
        }
        int digit = Character.getNumericValue(numeroAvs.charAt(5));
        if (digit < 5) {
            setSexe(CICompteIndividuel.CS_HOMME);
        } else {
            setSexe(CICompteIndividuel.CS_FEMME);
        }
        return true;
    }

    /**
     * Essaie de trouver le nom complet du cr�ateur du CI. Date de cr�ation : (29.11.2002 14:12:37)
     * 
     * @param transaction
     *            la transaction � utiliser
     */
    public void chargeNomComplet(BTransaction transaction) {
        String result = getUtilisateurCreation();
        if (!JAUtil.isStringEmpty(result)) {
            // non vide -> recherche
            FWSecureUser user = new FWSecureUser();
            user.setSession(getSession());
            user.setUser(getUtilisateurCreation());
            try {
                user.retrieve(transaction);
                if (!user.hasErrors()) {
                    result = user.getFirstname() + " " + user.getLastname();
                }
                if (JAUtil.isStringEmpty(result)) {
                    // si utilisateur trouv� mais sans nom -> id
                    result = getUtilisateurCreation();
                }
            } catch (Exception e) {
                // laisser userid
            }
        }
        utilisateurNomComplet = result;
    }

    /**
     * Teste si le ci donn�e est vide. Si oui, et s'il est du type genre 6,7 ou provisoire, ce dernier est effac�.
     * 
     * @param ci
     *            le ci � tester
     * @return true si le ci est vide et a �t� effac�
     */
    public boolean deleteIfEmpty(BTransaction transaction, boolean maj) throws Exception {
        if (transaction == null) {
            return false;
        }
        if (CICompteIndividuel.CS_REGISTRE_GENRES_6.equals(getRegistre())
                || CICompteIndividuel.CS_REGISTRE_GENRES_7.equals(getRegistre())
                || CICompteIndividuel.CS_REGISTRE_PROVISOIRE.equals(getRegistre())) {
            CIEcritureFastManager ecrMgr = new CIEcritureFastManager();
            ecrMgr.setSession(transaction.getSession());
            ecrMgr.setForCompteIndividuelId(getCompteIndividuelId());
            // ecrMgr.setForNotIdTypeCompte(CIEcriture.CS_CORRECTION);
            int ecrCINumber = ecrMgr.getCount(transaction);
            // System.out.println("  CI " + ci.getNumeroAvs() +
            // " count: "+ecrCINumber);
            if (ecrCINumber == 0) {
                // plus d'inscription, effacer le CI
                wantCallMethodAfter(false);
                wantCallMethodBefore(false);
                transaction.disableSpy();
                if (maj) {
                    // delete(transaction);
                }
                transaction.enableSpy();
                return true;
            }
        }
        return false;
    }

    public void extournerCi(String ciDestination, String anneeDebut, String anneeFin) {
        BTransaction transaction = null;
        try {
            transaction = new BTransaction(getSession());
            transaction.openTransaction();
            CICompteIndividuel ciDest = new CICompteIndividuel();
            CIEcritureManager ecritureSource = new CIEcritureManager();
            ciDest.setId(ciDestination);
            ciDest.setSession(getSession());
            try {
                ciDest.retrieve();
            } catch (Exception e) {
            }
            // modif 16.10.2006, emp�cher l'extourne sur un CI qui n'est pas au
            // RA
            if (ciDest.isNew() || !CICompteIndividuel.CS_REGISTRE_ASSURES.equals(ciDest.getRegistre())) {
                _addError(transaction, getSession().getLabel("EXTOURNE_CI_NON_RA"));
                return;
            }
            if (!CICompteIndividuel.CS_REGISTRE_ASSURES.equals(getRegistre())) {
                _addError(transaction, getSession().getLabel("EXTOURNE_CI_NON_RA"));
                return;
            }

            if ((!JAUtil.isStringEmpty(anneeDebut)) && (!JAUtil.isStringEmpty(anneeFin))) {

                if (Integer.parseInt(anneeDebut) > Integer.parseInt(anneeFin)) {
                    _addError(transaction, getSession().getLabel("DT_MOIS_DEBUT_PLUS_GRAND"));
                    return;
                }
            }
            if (!getCompteIndividuelId().equals(ciDest.getCompteIndividuelId())) {
                ecritureSource.setForCompteIndividuelId(getId());
                if (!JAUtil.isStringEmpty(anneeDebut)) {
                    ecritureSource.setFromAnnee(anneeDebut);
                }
                if (!JAUtil.isStringEmpty(anneeFin)) {
                    ecritureSource.setUntilAnnee(anneeFin);
                }
                ecritureSource.setForIdTypeCompte(CIEcriture.CS_CI);
                ecritureSource.setSession(getSession());
                try {
                    ecritureSource.find(transaction, BManager.SIZE_NOLIMIT);
                } catch (Exception e) {
                }
                if ((ecritureSource.size() > 0) && (!JAUtil.isStringEmpty(ciDestination))) {
                    for (int i = 0; i < ecritureSource.size(); i++) {
                        CIEcriture ecriture = new CIEcriture();
                        ecriture = (CIEcriture) ecritureSource.getEntity(i);
                        if (ecriture.getIdTypeCompte().equals(CIEcriture.CS_CI)
                                && !CIEcriture.CS_CIGENRE_8.equals(ecriture.getGenreEcriture())) {
                            try {
                                // ecriture.setIdTypeCompte(CIEcriture.CS_CI);
                                ecriture.extourne();
                                ecriture.setModeAjout("1");
                                ecriture.setIdTypeCompte(CIEcriture.CS_CI);
                                ecriture.setSession(getSession());
                                ecriture.setRassemblementOuvertureId("0");

                                // si il s'agit d'une �criture affili� on regarde le type (pour ccvs si type employeur
                                // ou ind�pendant pour le m�me num affili�)
                                if (!JadeStringUtil.isBlankOrZero(ecriture.getEmployeur())) {
                                    AFAffiliation affilie = new AFAffiliation();
                                    affilie.setSession(getSession());
                                    affilie.setAffiliationId(ecriture.getEmployeur());
                                    affilie.retrieve();

                                    // si affili� paritaire
                                    if (AFUtil.isAffiliationParitaire(affilie.getTypeAffiliation())) {
                                        ecriture.setForAffilieParitaire(true);
                                    }
                                    // si affili� personnel
                                    else if (AFUtil.isAffiliationPersonnelle(affilie.getTypeAffiliation())) {
                                        ecriture.setForAffiliePersonnel(true);
                                    }
                                }

                                if (isInPeriodeSplitting(getCompteIndividuelId(), ecriture.getAnnee())) {
                                    _addError(transaction, getSession().getLabel("ERREUR_EXTOURNE_SPLITTING"));
                                }
                                ecriture.add(transaction);
                                if (transaction.hasErrors()) {
                                    transaction.rollback();
                                    transaction.closeTransaction();
                                    return;
                                }
                                ecriture.extourne();
                                ecriture.setCompteIndividuelId(ciDestination);
                                ecriture.setCI(ciDest);
                                ecriture.setModeAjout("0");
                                // ecriture.setId("");
                                // ecriture.setEcritureId("");
                                ecriture.setRassemblementOuvertureId("0");
                                ecriture.setDateCiAdditionnel("0");
                                ecriture.setIdTypeCompte(CIEcriture.CS_CI);
                                ecriture.setSession(getSession());
                                if (isInPeriodeSplitting(ciDestination, ecriture.getAnnee())) {
                                    _addError(transaction, getSession().getLabel("ERREUR_EXTOURNE_SPLITTING"));
                                }
                                ecriture.add(transaction);
                                if (transaction.hasErrors()) {
                                    transaction.rollback();
                                    transaction.closeTransaction();
                                    return;
                                }
                            } catch (Exception e) {
                                e.getMessage();
                            }
                        }
                    }
                } else {
                    _addError(transaction, getSession().getLabel("MSG_EXTOURNE_COMPTE_INEXISTANT"));
                }
            } else {
                _addError(transaction, getSession().getLabel("MSG_EXTOURNE_COMPTES_IDENTIQUES"));
            }

            if (transaction.isRollbackOnly()) {
                transaction.rollback();
            } else {
                transaction.commit();
            }
        } catch (Exception e) {
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception inEx) {
                    // laisser tel quel
                }
            }
        }
    }

    public BManager find(Hashtable params) throws Exception {

        BManager manager = new CICompteIndividuelManager();
        manager.wantCallMethodAfter(false);
        manager.setSession(getSession());
        if (params != null) {
            Enumeration methods = params.keys();
            while (methods.hasMoreElements()) {
                String methodName = (String) methods.nextElement();
                Object value = params.get(methodName);

                Method m = manager.getClass().getMethod(methodName, new Class[] { value.getClass() });

                if (m != null) {
                    m.invoke(manager, new Object[] { value });
                }
            }
        }

        manager.find();
        return manager;
    }

    /**
     * Recherche de l'id du nouveau compte. Si le compte n'est pas nouveau, la m�thode n'effectue rien.
     */
    public void findAndSetId(BTransaction transaction) throws Exception {
        // incr�mente de +1 le num�ro
        if (JAUtil.isIntegerEmpty(compteIndividuelId)) {
            setCompteIndividuelId(this._incCounter(transaction, "0"));
        }
    }

    public String getAccesSecurite() {
        return accesSecurite;
    }

    /**
     * Lie le ci donn� au CI actuel. Date de cr�ation : (27.05.2003 08:26:44)
     * 
     * @param transasction
     *            la transaction � utiliser
     * @param child
     *            le ci � li�
     * @param updateParent
     *            si true, sauve les modifications apport�es au CI actuel
     * @exception java.io.Exception
     *                si un probl�me survient
     */
    public String getAnneeExtrait() throws Exception {
        CIRassemblementOuvertureManager rassemblFind = new CIRassemblementOuvertureManager();
        rassemblFind.setForCompteIndividuelId(getCompteIndividuelId());
        rassemblFind.setSession(getSession());
        rassemblFind.setOrderByDateOrdre();
        rassemblFind.setForTypeEnregistrement(CIRassemblementOuverture.CS_EXTRAIT);
        rassemblFind.find();
        if (rassemblFind.isEmpty() || JAUtil.isStringEmpty(getCompteIndividuelId())) {
            return "";
        } else {
            CIRassemblementOuverture rass = (CIRassemblementOuverture) rassemblFind.getEntity(rassemblFind.size() - 1);
            Integer retour = new Integer(JACalendar.getYear(rass.getDateOrdre()));
            return retour.toString();
        }
    }

    public String getAnneeOuverture() {
        return anneeOuverture;
    }

    public java.lang.String getCaisseTenantCI() {
        return caisseTenantCI;
    }

    public Vector getChampsAsCodeSystem(int keyChamp) {
        Vector vList = new Vector();
        // ajoute un blanc
        String[] list = new String[2];
        if (CICompteIndividuel.CHAMPS_AS_CS_PAYS == keyChamp) {
            try {
                CIApplication app = (CIApplication) getSession().getApplication();
                FWParametersSystemCodeManager manager = app.getCsPaysListe(getSession());
                vList = new Vector(manager.size());
                for (int i = 0; i < manager.size(); i++) {
                    list = new String[2];
                    FWParametersSystemCode entity = (FWParametersSystemCode) manager.getEntity(i);
                    String codePays = entity.getCurrentCodeUtilisateur().getCodeUtilisateur();
                    list[0] = entity.getIdCode();
                    list[1] = codePays + " - " + entity.getCurrentCodeUtilisateur().getLibelle();
                    vList.add(list);
                }
            } catch (Exception e) {
                // si probleme, retourne list vide.
                return new Vector();
            }
            return vList;
        } else if (CICompteIndividuel.CHAMPS_AS_CS_SEXE == keyChamp) {
            try {
                CIApplication app = (CIApplication) getSession().getApplication();
                FWParametersSystemCodeManager manager = app.getCsSexeListe(getSession());
                vList = new Vector(manager.size());
                for (int i = 0; i < manager.size(); i++) {
                    list = new String[2];
                    FWParametersSystemCode entity = (FWParametersSystemCode) manager.getEntity(i);
                    String codeSexe = entity.getCurrentCodeUtilisateur().getCodeUtilisateur();
                    String idCode = entity.getIdCode();
                    list[0] = idCode;
                    list[1] = codeSexe + " - " + entity.getCurrentCodeUtilisateur().getLibelle();
                    vList.add(list);
                }
            } catch (Exception e) {
                // si probleme, retourne list vide.
                return new Vector();
            }
            return vList;
        }
        return null;
    }

    public Object[] getCILies() {
        ArrayList ciLies = new ArrayList();
        BTransaction transaction = null;
        try {
            transaction = new BTransaction(getSession());
            transaction.openTransaction();
            if (!JAUtil.isIntegerEmpty(getCompteIndividuelIdReference())) {
                // recherche des ci en dessus
                CICompteIndividuel ciChild = this;
                int i = 0;
                // Fix jmc, �vite de tourner en boucle
                while (!ciChild.getCompteIndividuelIdReference().equals(ciChild.getCompteIndividuelId()) && (i < 30)) {
                    i++;
                    CICompteIndividuel ciL = new CICompteIndividuel();
                    ciL.setSession(getSession());
                    ciL.setCompteIndividuelId(ciChild.getCompteIndividuelIdReference());
                    ciL.retrieve(transaction);
                    if (!ciL.isNew()) {
                        ciLies.add(0,
                                new String[] { ciL.getCompteIndividuelId(), ciL.getNssFormate(), ciL.getNomPrenom(),
                                        ciL.getEtatFormate() });
                        ciChild = ciL;
                    } else {
                        break;
                    }

                }
                ciLies.add(new String[] { "", getNssFormate() });
                // recherche des ci en dessous
                CICompteIndividuel ciParent = this;
                int j = 0;
                while (!JAUtil.isIntegerEmpty(ciParent.getCompteIndividuelIdReference()) && (j < 30)) {
                    j++;
                    CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
                    ciMgr.setSession(getSession());
                    ciMgr.orderByAvs(false);
                    ciMgr.setForCompteIndividuelReferenceId(ciParent.getCompteIndividuelId());
                    ciMgr.setForNotCompteIndividuelId(ciParent.getCompteIndividuelId());
                    ciMgr.find(transaction);
                    if (!ciMgr.isEmpty()) {
                        CICompteIndividuel ciL = (CICompteIndividuel) ciMgr.getEntity(0);
                        ciLies.add(new String[] { ciL.getCompteIndividuelId(), ciL.getNssFormate(), ciL.getNomPrenom(),
                                ciL.getEtatFormate() });
                        ciParent = ciL;
                    } else {
                        break;
                    }

                }
            }
            if (transaction.isRollbackOnly()) {
                transaction.rollback();
            } else {
                transaction.commit();
            }
        } catch (Exception inEx) {
            // laisser tel quel
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception inEx) {
                    // laisser tel quel
                }
            }
        }
        return ciLies.toArray();
    }

    public Boolean getCiOuvert() {
        // pour API
        return isCiOuvert();
    }

    /**
     * Retourne la liste de log afin de l'utiliser pour {@link globaz.jsp.taglib.FWListSelectTag} Date de cr�ation :
     * (10.12.2002 08:55:41)
     * 
     * @return java.util.Vector
     */
    public Vector getClotures() {
        Vector list = new Vector();
        // ajouter tous et �critures actives
        list.add(new String[] { "active", "" });
        list.add(new String[] { "tous", getSession().getLabel("MSG_CI_ETAT_TOUS") });
        // list.add(new String[] { "active",
        // getSession().getLabel("getSession().getLabel("MSG_CI_ETAT_ACTIVES")")
        // });
        // chercher toutes les cl�tures du compte
        CIRassemblementOuvertureManager clotures = new CIRassemblementOuvertureManager();
        clotures.setSession(getSession());
        clotures.setOrderByDateCloture(true);
        clotures.setOrderByOrdre();
        clotures.setForCompteIndividuelId(getCompteIndividuelId());
        try {
            clotures.find();
            // String dateOrdreCIAdd = "";
            for (int i = 0; i < clotures.size(); i++) {
                CIRassemblementOuverture cloture = (CIRassemblementOuverture) clotures.getEntity(i);
                // ne pas inclure les cl�tures r�voqu�es
                if (cloture.isCloture() && JAUtil.isStringEmpty(cloture.getDateRevocation())) {
                    if (CIRassemblementOuverture.CS_CI_ADDITIONNEL.equals(cloture.getTypeEnregistrementWA())
                            || CIRassemblementOuverture.CS_CI_ADDITIONNEL_SUSPENS.equals(cloture
                                    .getTypeEnregistrementWA())) {
                        // if(!dateOrdreCIAdd.equals(cloture.getDateOrdre())) {
                        // prendre en compte qu'un CI additionnel par jour
                        list.add(new String[] {
                                cloture.getRassemblementOuvertureId(),
                                cloture.getDateCloture().substring(3) + " - "
                                        + getSession().getLabel("MSG_ECRITURE_CIADD") });
                        // dateOrdreCIAdd = cloture.getDateOrdre();
                        // }
                    } else {
                        list.add(new String[] {
                                cloture.getRassemblementOuvertureId(),
                                cloture.getDateCloture().substring(3) + " - " + cloture.getMotifArc() + " - "
                                        + cloture.getCaisseAgenceCommettante() });
                    }
                }
            }
        } catch (Exception ex) {
            // retourne liste vide
        }
        return list;
    }

    public String getCodeIrrecouverable() {
        return codeIrrecouverable;
    }

    public String getCompteIndividuelId() {
        return compteIndividuelId;
    }

    public String getCompteIndividuelIdReference() {
        return compteIndividuelIdReference;
    }

    /**
     * Retourne la date de cl�ture dans le format MMAA. Date de cr�ation : (23.12.2002 12:25:04)
     * 
     * @return la date de cl�ture dans le format MMAA.
     */
    public String getDateClotureMMAA() {
        return CIUtil.formatDateMMAA(this.getDerniereCloture());
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * Rretourne le type de compte en fonction du registre du ci. Date de cr�ation : (10.01.2003 14:43:00)
     * 
     * @return le type de compte en fonction du registre du ci
     */
    public String getDefaultIdTypeCompte() {
        String defaultType = "";
        if (CICompteIndividuel.CS_REGISTRE_ASSURES.equals(getRegistre())) {
            defaultType = "";
        } else if (CICompteIndividuel.CS_REGISTRE_GENRES_6.equals(getRegistre())) {
            defaultType = "";
        } else if (CICompteIndividuel.CS_REGISTRE_GENRES_7.equals(getRegistre())) {
            defaultType = "";
        } else if (CICompteIndividuel.CS_REGISTRE_PROVISOIRE.equals(getRegistre())) {
            defaultType = CIEcriture.CS_CI_SUSPENS;
        }
        return defaultType;
    }

    public CIRassemblementOuverture getDerniereBCloture(BTransaction transaction) {
        try {
            // force � regarder dans les annonces
            CIRassemblementOuvertureManager mgr = new CIRassemblementOuvertureManager();
            mgr.setSession(getSession());
            mgr.setForCompteIndividuelId(getCompteIndividuelId());
            mgr.setOrderByDateCloture(false);
            mgr.find(transaction);
            for (int i = 0; i < mgr.size(); i++) {
                CIRassemblementOuverture ras = (CIRassemblementOuverture) mgr.getEntity(i);
                if (JAUtil.isStringEmpty(ras.getDateRevocation()) && ras.isCloture()) {
                    return ras;
                }
            }
        } catch (Exception ex) {
            // laisser tel quel
        }
        return null;
    }

    public CIRassemblementOuverture getDerniereBClotureForCiAdditionnel(BTransaction transaction) {
        try {
            // force � regarder dans les annonces
            CIRassemblementOuvertureManager mgr = new CIRassemblementOuvertureManager();
            mgr.setSession(getSession());
            mgr.setForCompteIndividuelId(getCompteIndividuelId());
            mgr.setOrderByDateCloture(false);
            mgr.find(transaction);
            for (int i = 0; i < mgr.size(); i++) {
                CIRassemblementOuverture ras = (CIRassemblementOuverture) mgr.getEntity(i);
                if (JAUtil.isStringEmpty(ras.getDateRevocation()) && ras.isClotureForCiAdditionnel()) {
                    return ras;
                }
            }
        } catch (Exception ex) {
            // laisser tel quel
        }
        return null;
    }

    public String getDerniereCaisse() {

        return derniereCaisse;

    }

    public String getDerniereCaisseAgence() {
        return caisseAgence;
    }

    public String getDerniereCaisseForDetail() {
        try {
            if (JAUtil.isIntegerEmpty(compteIndividuelId)) {
                return "";
            }
            if (JAUtil.isIntegerEmpty(getDerniereCaisse())) {
                CIRassemblementOuverture clo = getDerniereBCloture(null);
                if (clo != null) {
                    setDerniereCloture(clo.getDateCloture());
                    setDernierMotifCloture(clo.getMotifArc());
                    setDerniereCaisse(clo.getCaisseCommettante());
                }
            }
            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
            return application.getAdministration(getSession(), getDerniereCaisse(),
                    new String[] { "getCodeAdministration" }).getCodeAdministration();
        } catch (Exception e) {
            return "";
        }
    }

    public String getDerniereCloture() {
        if (JAUtil.isStringEmpty(derniereCloture)) {
            // test les annonces
            String compare = this.getDerniereCloture(true);
            if (!JAUtil.isStringEmpty(compare)) {
                return compare.substring(3);
            } else {
                return "";
            }
        }
        return derniereCloture.substring(3);
    }

    public String getDerniereCloture(boolean force) {
        if (force && !JAUtil.isIntegerEmpty(getCompteIndividuelId())) {
            CIRassemblementOuverture clo = getDerniereBCloture(null);
            if (clo != null) {
                setDerniereCloture(clo.getDateCloture());
                setDernierMotifCloture(clo.getMotifArc());
                setDerniereCaisse(clo.getCaisseCommettante());
            }
        }
        return derniereCloture;
    }

    /**
     * Retourne la liste de log afin de l'utiliser pour {@link globaz.jsp.taglib.FWListSelectTag} Date de cr�ation :
     * (10.12.2002 08:55:41)
     * 
     * @return java.util.Vector
     */
    public String getDerniereClotureFormattee() {
        String result = "";
        if (!isCiOuvert().booleanValue()) {
            if (JAUtil.isStringEmpty(this.getDerniereCloture(false))) {
                return result;
            }
            result = this.getDerniereCloture(false).substring(3);
            if (!JAUtil.isIntegerEmpty(this.getDernierMotifCloture(false))) {
                result += " - " + this.getDernierMotifCloture(false);
            } else {
                String compare = this.getDernierMotifCloture(true);
                if (!JAUtil.isIntegerEmpty(compare)) {
                    result += " - " + compare;
                } else {
                    result += " - n/a";
                }
            }
            if (!JAUtil.isIntegerEmpty(getDerniereCaisse())) {
                result += " - " + getDerniereCaisseAgence();
            } else {
                result += " - n/a";
            }
        }
        return result;
    }

    /**
     * Retourne la liste de log afin de l'utiliser pour {@link globaz.jsp.taglib.FWListSelectTag} Date de cr�ation :
     * (10.12.2002 08:55:41)
     * 
     * @return java.util.Vector
     */
    public String getDerniereClotureFormatteeOuvert() {
        try {
            String result = "";
            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
            if (JAUtil.isStringEmpty(this.getDerniereCloture(true))) {
                return result;
            }
            result = this.getDerniereCloture(false).substring(3);
            if (!JAUtil.isIntegerEmpty(this.getDernierMotifCloture(false))) {
                result += " - " + this.getDernierMotifCloture(false);
            } else {
                String compare = this.getDernierMotifCloture(true);
                if (!JAUtil.isIntegerEmpty(compare)) {
                    result += " - " + compare;
                } else {
                    result += " - n/a";
                }
            }
            if (!JAUtil.isStringEmpty(getDerniereCaisseAgence())) {
                result += " - " + getDerniereCaisseAgence();
            } else if (!JAUtil.isStringEmpty(getDerniereCaisse())) {
                result += " - "
                        + application.getAdministration(getSession(), getDerniereCaisse(),
                                new String[] { "getCodeAdministration" }).getCodeAdministration();
            } else {
                result += " - n/a";
            }

            return result;
        } catch (Exception e) {
            return "";
        }
    }

    public String getDernierEmployeur() {
        return dernierEmployeur;
    }

    public String getDernieresEcritures() {
        StringBuffer buf = new StringBuffer();
        buf.append("<table>");
        try {
            if (!JAUtil.isStringEmpty(getCompteIndividuelId())) {

                CIEcritureManager ecrMgr = new CIEcritureManager();
                ecrMgr.setSession(getSession());
                ecrMgr.setForIdTypeCompteCompta("true");
                ecrMgr.setForCompteIndividuelId(getCompteIndividuelId());
                ecrMgr.setOrder("KBNANN DESC, KBNMOF DESC");
                ecrMgr.changeManagerSize(5);
                ecrMgr.find();
                for (int i = 0; i < ecrMgr.size(); i++) {
                    // mise � jour avec la derni�re �criture de l'assur�
                    CIEcriture ecr = (CIEcriture) ecrMgr.getEntity(i);
                    buf.append("<tr><td align=right>");
                    buf.append(ecr.getEmployeurPartenaireForDisplay());
                    buf.append("&nbsp;");
                    buf.append("</td><td align=right>");
                    buf.append(ecr.getGreFormat());
                    buf.append("&nbsp;");
                    buf.append("</td><td align=right>");
                    buf.append(JadeStringUtil.rightJustifyInteger(ecr.getMoisDebut(), 2));
                    buf.append("-");
                    buf.append(JadeStringUtil.rightJustifyInteger(ecr.getMoisFin(), 2));
                    buf.append(".");
                    buf.append(ecr.getAnnee());
                    buf.append("&nbsp;");
                    buf.append("</td><td align=right>");
                    buf.append(ecr.getMontantSigne());
                    buf.append("&nbsp;");
                    buf.append("</td><td align=left>");
                    buf.append(CodeSystem.getCodeUtilisateur(ecr.getCode(), getSession()));
                    if (!JAUtil.isIntegerEmpty(ecr.getRassemblementOuvertureId())) {
                        CIRassemblementOuverture rass = new CIRassemblementOuverture();
                        rass.setRassemblementOuvertureId(ecr.getRassemblementOuvertureId());
                        rass.setSession(getSession());
                        rass.retrieve();
                        buf.append("</td><td align=right>");
                        buf.append(rass.getMotifArc());
                    } else {
                        buf.append("</td><td align=right>");

                    }
                    buf.append("</td></tr>");

                }
            }
        } catch (Exception ex) {
            // laisser le buffer vide
        }
        buf.append("</table>");
        return buf.toString();
    }

    public String getDernieresEcrituresTxt(String delim) {
        if (delim == null) {
            delim = ";";
        }
        StringBuffer buf = new StringBuffer();
        try {
            if (!JAUtil.isStringEmpty(getCompteIndividuelId())) {
                CIEcritureManager ecrMgr = new CIEcritureManager();
                ecrMgr.setSession(getSession());
                ecrMgr.setForIdTypeCompteCompta("true");
                ecrMgr.setForCompteIndividuelId(getCompteIndividuelId());
                ecrMgr.setOrder("KBNANN DESC, KBNMOF DESC");
                ecrMgr.changeManagerSize(5);
                ecrMgr.find();
                for (int i = 0; i < ecrMgr.size(); i++) {
                    // mise � jour avec la derni�re �criture de l'assur�
                    CIEcriture ecr = (CIEcriture) ecrMgr.getEntity(i);
                    if (!JAUtil.isIntegerEmpty(ecr.getEmployeurPartenaireForDisplay())) {
                        buf.append(ecr.getEmployeurPartenaireForDisplay());
                    } else {
                        buf.append("?");
                    }
                    buf.append(delim);
                    buf.append(ecr.getGreFormat());
                    buf.append(delim);
                    buf.append(JadeStringUtil.rightJustifyInteger(ecr.getMoisDebut(), 2));
                    buf.append("-");
                    buf.append(JadeStringUtil.rightJustifyInteger(ecr.getMoisFin(), 2));
                    buf.append(delim);
                    buf.append(ecr.getAnnee());
                    buf.append(delim);
                    buf.append(ecr.getMontantSigne());
                    buf.append("\n");
                }
            }
        } catch (Exception ex) {
            // laisser le buffer vide
        }
        return buf.toString();
    }

    public String getDernierMotifCloture() {
        if (JAUtil.isIntegerEmpty(dernierMotifCloture)) {
            // test les annonces
            return this.getDernierMotifCloture(true);
        }
        return dernierMotifCloture;
    }

    public String getDernierMotifCloture(boolean force) {
        if (force && !JAUtil.isIntegerEmpty(getCompteIndividuelId())) {
            CIRassemblementOuverture clo = getDerniereBCloture(null);
            if (clo != null) {
                setDerniereCloture(clo.getDateCloture());
                setDernierMotifCloture(clo.getMotifArc());
                setDerniereCaisse(clo.getCaisseCommettante());
            }
        }
        return dernierMotifCloture;
    }

    public java.lang.String getDernierMotifOuverture() {
        return dernierMotifOuverture;
    }

    public int getEtatEntity() {
        return etatEntity;
    }

    public String getEtatFormate() {
        try {
            return getDateNaissance() + " - " + getSexeForNNSS() + " - " + getSession().getCode(paysOrigineId);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Retourne une list d'ID de CI li�s au CI actuel si il existe des CI li�s, sinon retourne vide
     */
    public ArrayList getIdCILies(BTransaction transaction) throws Exception {
        ArrayList ciLies = new ArrayList();
        if (!JAUtil.isIntegerEmpty(getCompteIndividuelIdReference())) {
            // recherche des ci en dessus
            CICompteIndividuel ciChild = this;
            int i = 0;
            while (!ciChild.getCompteIndividuelIdReference().equals(ciChild.getCompteIndividuelId()) && (i < 30)) {
                i++;
                CICompteIndividuel ciL = new CICompteIndividuel();
                ciL.setSession(getSession());
                ciL.setCompteIndividuelId(ciChild.getCompteIndividuelIdReference());
                ciL.retrieve(transaction);
                if (!ciL.isNew()) {
                    ciLies.add(0, ciL.getCompteIndividuelId());
                    ciChild = ciL;
                } else {
                    break;
                }
            }
            ciLies.add(getCompteIndividuelId());
            // recherche des ci en dessous
            CICompteIndividuel ciParent = this;
            int j = 0;
            while (!JAUtil.isIntegerEmpty(ciParent.getCompteIndividuelIdReference()) && (j < 30)) {
                j++;
                CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
                ciMgr.setSession(getSession());
                ciMgr.orderByAvs(false);
                ciMgr.setForCompteIndividuelReferenceId(ciParent.getCompteIndividuelId());
                ciMgr.setForNotCompteIndividuelId(ciParent.getCompteIndividuelId());
                ciMgr.find(transaction);
                if (!ciMgr.isEmpty()) {
                    CICompteIndividuel ciL = (CICompteIndividuel) ciMgr.getEntity(0);
                    ciLies.add(ciL.getCompteIndividuelId());
                    ciParent = ciL;
                } else {
                    break;
                }
            }
        }
        return ciLies;
    }

    public String getIdTierAdmin() {
        TIAdministrationManager mgr = new TIAdministrationManager();
        mgr.setForCodeAdministration(getDerniereCaisseAgence());
        mgr.setForGenreAdministration(TIAdministrationViewBean.CS_CAISSE_COMPENSATION);
        mgr.setSession(getSession());
        try {
            mgr.find();
        } catch (Exception e) {
        }
        if (mgr.size() > 0) {

            TIAdministrationViewBean ti = (TIAdministrationViewBean) mgr.getEntity(0);
            return ti.getIdTiersAdministration();
        } else {
            return "";
        }
    }

    /**
     * @return the inactive
     */
    public Boolean getInactive() {
        return inactive;
    }

    /**
     * Returns the infoAff.
     * 
     * @return String
     */
    public String getInfoAff() {
        return infoAff;
    }

    public Boolean getInvalide() {
        return invalide;
    }

    /**
     * @return
     */
    public String getLikeInIdAffiliation() {
        return likeInIdAffiliation;
    }

    public Vector getListClotures() {
        Vector list = new Vector();
        // ajouter tous et �critures actives
        list.add(new String[] { "toutesEcritures", getSession().getLabel("MSG_CI_ETAT_TOUTES_ECRITURES") });
        list.add(new String[] { "seulementClotures", getSession().getLabel("MSG_CI_ETAT_SEULEMENENT_CLOTURES") });
        list.add(new String[] { "active", getSession().getLabel("MSG_CI_ETAT_ACTIVES") });
        // chercher toutes les cl�tures du compte
        CIRassemblementOuvertureManager clotures = new CIRassemblementOuvertureManager();
        clotures.setSession(getSession());
        clotures.setOrderByDateCloture(true);
        clotures.setOrderByOrdre();
        clotures.setForCompteIndividuelId(getCompteIndividuelId());
        try {
            clotures.find();
            // String dateOrdreCIAdd = "";
            for (int i = 0; i < clotures.size(); i++) {
                CIRassemblementOuverture cloture = (CIRassemblementOuverture) clotures.getEntity(i);
                // ne pas inclure les cl�tures r�voqu�es
                if (cloture.isCloture() && JAUtil.isStringEmpty(cloture.getDateRevocation())) {
                    if (CIRassemblementOuverture.CS_CI_ADDITIONNEL.equals(cloture.getTypeEnregistrementWA())
                            || CIRassemblementOuverture.CS_CI_ADDITIONNEL_SUSPENS.equals(cloture
                                    .getTypeEnregistrementWA())) {
                        // if(!dateOrdreCIAdd.equals(cloture.getDateOrdre())) {
                        // prendre en compte qu'un CI additionnel par jour
                        list.add(new String[] {
                                cloture.getRassemblementOuvertureId(),
                                cloture.getDateCloture().substring(3) + " - "
                                        + getSession().getLabel("MSG_ECRITURE_CIADD") });
                        // dateOrdreCIAdd = cloture.getDateOrdre();
                        // }
                    } else {
                        list.add(new String[] {
                                cloture.getRassemblementOuvertureId(),
                                cloture.getDateCloture().substring(3) + " - " + cloture.getMotifArc() + " - "
                                        + cloture.getCaisseAgenceCommettante() });
                    }
                }
            }
        } catch (Exception ex) {
            // retourne liste vide
        }
        return list;
    }

    /**
     * Returns the selectedNoAvs.
     * 
     * @return String
     */
    public String getMainSelectedId() {
        return mainSelectedId;
    }

    public String getMessageNoRight() {
        return getSession().getLabel("MSG_CI_NO_AUTH");
    }

    /**
     * @return
     */
    public Boolean getNnss() {
        return nnss;
    }

    /**
     * Returns the noAffilie.
     * 
     * @return String
     */
    public String getNoAffilie() {
        return noAffilie;
    }

    public String getNomCaisse() {
        TIAdministrationManager mgr = new TIAdministrationManager();
        mgr.setForIdTiersAdministration(getDerniereCaisse());
        mgr.setForGenreAdministration(TIAdministrationViewBean.CS_CAISSE_COMPENSATION);
        mgr.setSession(getSession());
        try {
            mgr.find();
        } catch (Exception e) {
        }
        if (mgr.size() > 0) {
            TIAdministrationViewBean ti = (TIAdministrationViewBean) mgr.getEntity(0);
            return ti.getNom();
        } else {
            return "";
        }
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    /**
     * Retourne le no et le nom du dernier employeur. Date de cr�ation : (15.01.2003 09:41:26)
     * 
     * @return le no et le nom du dernier employeur
     */
    public String getNoNomDernierEmployeur() {
        if (JAUtil.isIntegerEmpty(getDernierEmployeur())) {
            return "";
        }
        try {
            // nouveau: recherche en fonction du num�ro d'affili�
            CIApplication application = (CIApplication) getSession().getApplication();
            return getDernierEmployeur()
                    + "\n"
                    + application
                            .getAffilieByNo(getSession(), getDernierEmployeur(), false, false, null, null, null, "", "")
                            .getTiers().getNom();
        } catch (Exception ex) {
            return getDernierEmployeur();
        }
    }

    public String getNssFormate() {
        return NSUtil.formatAVSNew(numeroAvs, getNnss().booleanValue());
    }

    public String getNumeroAvs() {
        return numeroAvs;
    }

    public String getNumeroAvsAncien() {
        return numeroAvsAncien;
    }

    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(numeroAvs, nnss.booleanValue());
    }

    /**
     * Pour annoncer les �criture de splitting qui tiennent sur 11 positions
     * 
     * @return
     */
    public String getNumeroAvsForSplitting() {
        if (nnss.booleanValue() && (numeroAvs.length() > 3)) {
            return "-" + NSUtil.unFormatAVS(numeroAvs).substring(3);
        }
        return numeroAvs;
    }

    /**
     * @return
     */
    public String getNumeroAvsNNSS() {
        return numeroAvsNNSS;
    }

    public String getNumeroAvsPrecedant() {
        return numeroAvsPrecedant;
    }

    public String getPaysFormate() {
        try {
            return getSession().getCode(paysOrigineId) + " - " + getSession().getCodeLibelle(paysOrigineId);
        } catch (Exception e) {
            return "";
        }
    }

    public String getPaysForNNSS() {
        try {
            return getSession().getCode(getPaysOrigineId());
        } catch (Exception e) {
            return "";
        }
    }

    public String getPaysOrigineId() {
        return paysOrigineId;
    }

    public String getReferenceInterne() {
        return referenceInterne;
    }

    public String getRegistre() {
        return registre;
    }

    public String getSexe() {
        return sexe;
    }

    public String getSexeFormate() {
        return CodeSystem.getCodeUtilisateur(getSexe(), getSession()) + " - "
                + CodeSystem.getLibelle(getSexe(), getSession());
    }

    public String getSexeForNNSS() {
        try {
            if ("fr".equals(getSession().getIdLangueISO())) {
                if (CICompteIndividuel.CS_FEMME.equals(getSexe())) {
                    return "F";
                } else {
                    return "H";
                }
            } else if ("de".equals(getSession().getIdLangueISO())) {
                if (CICompteIndividuel.CS_FEMME.equals(getSexe())) {
                    return "W";
                } else {
                    return "M";
                }
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public String getSexeLibelle() {
        try {
            return getSession().getCodeLibelle(getSexe());
        } catch (Exception e) {
            return "";
        }
    }

    public String getSrcUpi() {
        return srcUpi;
    }

    public String getUtilisateurCreation() {
        return utilisateurCreation;
    }

    /**
     * Renvoie le nom du cr�ateur du CI. Date de cr�ation : (29.11.2002 14:30:33)
     * 
     * @return le nom (complet si trouv� ou simplement l'abbr�viation) du cr�ateur.
     */
    public java.lang.String getUtilisateurNomComplet() {
        return utilisateurNomComplet;
    }

    public String getValeurAAfficherParDefaut() {
        if (isCiReader()) {
            String defaultType = "";
            if (CICompteIndividuel.CS_REGISTRE_ASSURES.equals(getRegistre())) {
                defaultType = CIEcriture.CS_CI;
            } else if (CICompteIndividuel.CS_REGISTRE_GENRES_6.equals(getRegistre())) {
                defaultType = CIEcriture.CS_CI;
            } else if (CICompteIndividuel.CS_REGISTRE_GENRES_7.equals(getRegistre())) {
                defaultType = CIEcriture.CS_CI;
            } else if (CICompteIndividuel.CS_REGISTRE_PROVISOIRE.equals(getRegistre())) {
                defaultType = CIEcriture.CS_CI_SUSPENS;
            }
            return defaultType;

        } else {
            return "";
        }
    }

    /**
     * Indique si un ou plusieurs CI sont li� � celui-ci. Date de cr�ation : (09.05.2003 10:38:31)
     * 
     * @return boolean
     */
    public boolean hasCILies() {
        return !JAUtil.isIntegerEmpty(getCompteIndividuelIdReference());
    }

    /**
     * �. Date de cr�ation : (15.11.2002 08:39:41)
     * 
     * @return le CI du tiers ou null si inexistant.
     * @param idTiers
     *            le num�ro avs du tiers.
     * @param transaction
     *            la transaction � utiliser.
     */
    public boolean hasCorrection(BTransaction transaction) throws Exception {
        CIEcritureCounter mgr = new CIEcritureCounter();
        mgr.setSession(getSession());
        mgr.setForCompteIndividuelId(getCompteIndividuelId());
        mgr.setForIdTypeCompte(CIEcriture.CS_CORRECTION);
        int count = mgr.getCount(transaction);
        if (count != 0) {
            return true;
        }
        return false;
    }

    /**
     * retourne vrai si le CI a une ou plusieurs p�riodes de splitting
     * 
     * @return
     */
    public boolean hasSplitting() {
        // Ne pas lancer la requ�te si l'id est vide
        if (!JAUtil.isIntegerEmpty(compteIndividuelId)) {
            try {
                CIPeriodeSplittingManager periodeMgr = new CIPeriodeSplittingManager();
                periodeMgr.setSession(getSession());
                periodeMgr.setForCompteIndividuelId(compteIndividuelId);
                if (periodeMgr.getCount() > 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;

            }
        } else {
            return false;
        }
    }

    /**
     * retourne la cha�ne de car � afficher dans le d�tail du CI
     * 
     * @return
     */
    public String hasSplittingToString() {
        if (!JAUtil.isIntegerEmpty(compteIndividuelId)) {
            if (hasSplitting()) {
                return getSession().getLabel("OUI");
            } else {
                return getSession().getLabel("NON");
            }
        } else {
            return "";
        }
    }

    /**
     * Charge le CI du tiers donn�. Date de cr�ation : (15.11.2002 08:39:41)
     * 
     * @return le CI du tiers ou null si inexistant.
     * @param idTiers
     *            le num�ro avs du tiers.
     * @param transaction
     *            la transaction � utiliser.
     */
    public boolean hasSuspens(BTransaction transaction) throws Exception {
        CIEcritureCounter mgr = new CIEcritureCounter();
        mgr.setSession(getSession());
        mgr.setForCompteIndividuelId(getCompteIndividuelId());
        mgr.setForIdTypeCompte(CIEcriture.CS_CI_SUSPENS);
        int count = mgr.getCount(transaction);
        if (count != 0) {
            return true;
        }
        return false;
    }

    /**
     * Teste si l'utilisateur � le droit d'�diter l'en-t�te. Test actuellement ok si niveau 5.
     */
    public boolean hasUserEditCIRight(BTransaction transaction) {
        return globaz.pavo.util.CIUtil.isSpecialist(getSession());
    }

    public boolean hasUserShowRight(BTransaction transaction) {
        return this.hasUserShowRight(transaction, getSession().getUserId());
    }

    public boolean hasUserShowRight(BTransaction transaction, String userId) {
        try {
            if (JAUtil.isStringEmpty(getAccesSecurite())) {
                // ci pas charg� correctement -> �criture cach�e
                return false;
            } else if (!JAUtil.isIntegerEmpty(getAccesSecurite())) {
                FWSecureUserDetail user = new FWSecureUserDetail();
                user.setSession(getSession());
                user.setUser(userId);
                user.setLabel(CICompteIndividuel.SECURITE_LABEL);
                user.retrieve(transaction);
                if (!user.isNew()) {
                    int accesUser = Integer.parseInt(user.getData());
                    int accesCI = Character.getNumericValue(getAccesSecurite().charAt(getAccesSecurite().length() - 1));
                    if (transaction != null) {
                        boolean accesAff = checkAffSecureCode(transaction, CS_ACCESS + accesUser);
                        if ((accesUser < accesCI) || !accesAff) {
                            // s�curit� utilisateur inf�rieure -> -> ecriture cach�e
                            return false;
                        }
                    } else {
                        if ((accesUser < accesCI)) {
                            // s�curit� utilisateur inf�rieure -> -> ecriture cach�e
                            return false;
                        }
                    }

                } else {
                    // l'utilisateur n'a pas de code acc�s -> ecriture cach�e
                    return false;
                }
                // s�curit� ok -> retourner le montant
                return true;
            } else {
                // pas de code s�curit� ou code 0 -> retourner le montant
                return true;
            }
        } catch (Exception ex) {
            // si exception -> ecriture cach�e
            return false;
        }
    }

    private boolean checkAffSecureCode(BTransaction transaction, int codeSecure) throws Exception {
        BTransaction transactionSecureCode = null;
        boolean hasRight = false;
        ResultSet resultSet = null;
        BStatement psCheckAffSecureCode = new BStatement(transaction);
        psCheckAffSecureCode.createStatement();
        resultSet = psCheckAffSecureCode.executeQuery("SELECT MATSEC as AFF_SEC from "
                + Jade.getInstance().getDefaultJdbcSchema() + ".CIECRIP ecr inner join "
                + Jade.getInstance().getDefaultJdbcSchema() + ".CIINDIP ci on ecr.KAIIND=ci.KAIIND inner join "
                + Jade.getInstance().getDefaultJdbcSchema() + ".AFAFFIP aff on ecr.KBITIE=aff.MAIAFF "
                + "WHERE ci.KAIIND=" + getCompteIndividuelId());
        Object[] ciLies = getCILies();
        while (resultSet.next()) {
            if (Integer.parseInt(resultSet.getString(1)) > codeSecure) {
                return false;
            }
        }
        if (ciLies.length > 1) {
            String[] nAVSStr = (String[]) ciLies[1];
            resultSet = psCheckAffSecureCode.executeQuery("SELECT MATSEC as AFF_SEC from "
                    + Jade.getInstance().getDefaultJdbcSchema() + ".CIECRIP ecr inner join "
                    + Jade.getInstance().getDefaultJdbcSchema() + ".CIINDIP ci on ecr.KAIIND=ci.KAIIND inner join "
                    + Jade.getInstance().getDefaultJdbcSchema() + ".AFAFFIP aff on ecr.KBITIE=aff.MAIAFF "
                    + "WHERE ci.KANAVS=" + JAUtil.quotedString(JAStringFormatter.deformatAvs(nAVSStr[1])));
        }
        while (resultSet.next()) {
            if (Integer.parseInt(resultSet.getString(1)) > codeSecure) {
                return false;
            }
        }
        return true;
    }

    public Boolean isCiOuvert() {
        return ciOuvert;
    }

    private boolean isCiReader() {
        return false;
    }

    /**
     * M�thode qui check si une inscription se trouve dans une p�riode de splitting
     * 
     * @param idCI
     * @param anneeEcriture
     * @return
     * @throws Exception
     */
    private boolean isInPeriodeSplitting(String idCI, String anneeEcriture) throws Exception {
        CIPeriodeSplittingManager splitMgr = new CIPeriodeSplittingManager();
        splitMgr.setSession(getSession());
        splitMgr.setForCompteIndividuelId(idCI);
        splitMgr.find();
        for (int i = 0; i < splitMgr.size(); i++) {
            CIPeriodeSplitting periodeSplit = (CIPeriodeSplitting) splitMgr.getEntity(i);
            if (JAUtil.isStringEmpty(periodeSplit.getDateRevocation())) {
                int anneeDebutInt = Integer.parseInt(periodeSplit.getAnneeDebut());
                int anneeFinInt = Integer.parseInt(periodeSplit.getAnneeFin());
                int anneeEcritureInt = Integer.parseInt(anneeEcriture);
                if ((anneeDebutInt <= anneeEcritureInt) && (anneeFinInt >= anneeEcritureInt)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the plausiNumAvs.
     * 
     * @return boolean
     */
    public boolean isPlausiNumAvs() {
        return plausiNumAvs;
    }

    public Boolean isProvenanceFusion() {
        return provenanceFusion;
    }

    /**
     * Lis les CI "RA" et "Provisoire" du tiers donn�.<br>
     * Si un un RA ET un provisoire existent, le RA est renvoy�.<br>
     * Envoie un email aux responsables CI si un provisoire et un au RA a �t� trouv�. Date de cr�ation : (15.11.2002
     * 08:39:41)
     * 
     * @return le RA ou provisoire trouv�, null si inexistant.
     * @param idTiers
     *            le num�ro avs du tiers.
     * @param transaction
     *            la transaction � utiliser.
     */
    public void load(String idTiers, BITransaction transactionIfc) throws Exception {
        BTransaction transaction = null;
        // boolean createTransaction = false;
        if (transactionIfc instanceof BTransaction) {
            transaction = (BTransaction) transactionIfc;
        } else {
            throw new Exception("Transaction incorrecte");
        }

        CICompteIndividuel ci = CICompteIndividuel.loadCITemporaire(idTiers, transaction);
        if (ci != null) {
            copyDataFromEntity(ci);
        }
    }

    public void removeLiaison(BTransaction transaction, CICompteIndividuel parent) throws Exception {
        // d�tacher le CI
        setCompteIndividuelIdReference("0");
        // recherche d'un �ventuel child
        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
        ciMgr.setSession(getSession());
        ciMgr.orderByAvs(false);
        ciMgr.setForCompteIndividuelReferenceId(getCompteIndividuelId());
        ciMgr.setForNotCompteIndividuelId(getCompteIndividuelId());
        ciMgr.changeManagerSize(1);
        ciMgr.wantCallMethodAfter(false);
        ciMgr.wantCallMethodBefore(false);
        ciMgr.find(transaction);
        if (!ciMgr.isEmpty()) {
            // child existe
            CICompteIndividuel child = (CICompteIndividuel) ciMgr.getEntity(0);
            if (parent.getCompteIndividuelId().equals(getCompteIndividuelId())) {
                // ci actuel root, modifier le child
                // sous child existe?
                ciMgr = new CICompteIndividuelManager();
                ciMgr.setSession(getSession());
                ciMgr.orderByAvs(false);
                ciMgr.setForCompteIndividuelReferenceId(child.getCompteIndividuelId());
                ciMgr.setForNotCompteIndividuelId(child.getCompteIndividuelId());
                ciMgr.changeManagerSize(1);
                ciMgr.wantCallMethodAfter(false);
                ciMgr.wantCallMethodBefore(false);
                ciMgr.find(transaction);
                if (!ciMgr.isEmpty()) {
                    // sous child trouv� -> child en root
                    child.setCompteIndividuelIdReference(child.getCompteIndividuelId());
                    child.wantCallValidate(false);
                    child.wantCallMethodAfter(false);
                    child.wantCallMethodBefore(false);
                    child.update(transaction);
                } else {
                    // ci seul
                    child.setCompteIndividuelIdReference("0");
                    child.wantCallValidate(false);
                    child.wantCallMethodAfter(false);
                    child.wantCallMethodBefore(false);
                    child.update(transaction);
                }
            } else {
                // lier le child au root
                // l'entitiy doit �tre maj
                wantCallValidate(false);
                wantCallMethodAfter(false);
                wantCallMethodBefore(false);
                this.update(transaction);
                wantCallValidate(true);
                wantCallMethodAfter(true);
                wantCallMethodBefore(true);
                child.setCompteIndividuelIdReference("0");
                parent.addLiaison(transaction, child, true, true);
            }
        } else {
            // aucun ancien CI
            if (parent.getCompteIndividuelId().equals(parent.getCompteIndividuelIdReference())) {
                // parent �tait root -> modification ci seul sinon, laisser tel
                // quel
                parent.setCompteIndividuelIdReference("0");
                parent.wantCallValidate(false);
                parent.wantCallMethodAfter(false);
                parent.wantCallMethodBefore(false);
                parent.update(transaction);
            }
        }
    }

    public void setAccesSecurite(String newAccesSecurite) {
        if (this.hasUserShowRight(null)) {
            accesSecurite = newAccesSecurite;
        }
    }

    public void setAccesSecuriteNoCheck(String newAccesSecurite) {
        accesSecurite = newAccesSecurite;
    }

    public void setAnneeOuverture(String newAnneeOuverture) {
        anneeOuverture = newAnneeOuverture;
    }

    public void setCaisseTenantCI(java.lang.String newCaisseTenantCI) {
        caisseTenantCI = newCaisseTenantCI;
    }

    public void setCiOuvert(Boolean newCiOuvert) {
        ciOuvert = newCiOuvert;
    }

    public void setCodeIrrecouverable(String codeIrrecouverable) {
        this.codeIrrecouverable = codeIrrecouverable;
    }

    public void setCompteIndividuelId(String newCompteIndividuelId) {
        compteIndividuelId = newCompteIndividuelId;
    }

    public void setCompteIndividuelIdReference(String newCompteIndividuelIdReference) {
        compteIndividuelIdReference = newCompteIndividuelIdReference;
    }

    public void setDateCreation(String newDateCreation) {
        dateCreation = newDateCreation;
    }

    public void setDateNaissance(String newDateNaissance) {
        dateNaissance = newDateNaissance;
    }

    public void setDerniereCaisse(String newDerniereCaisse) {

        derniereCaisse = newDerniereCaisse;

    }

    /**
     * Sets the caisseAgence.
     * 
     * @param caisseAgence
     *            The caisseAgence to set
     */
    public void setDerniereCaisseAgence(String caisseAgence) {
        this.caisseAgence = caisseAgence;
    }

    public void setDerniereCloture(String newDerniereCloture) {
        derniereCloture = newDerniereCloture;
    }

    public void setDernierEmployeur(String newDernierEmployeur) {
        dernierEmployeur = newDernierEmployeur;
    }

    public void setDernierMotifCloture(String newDernierMotifArc) {
        dernierMotifCloture = newDernierMotifArc;
    }

    public void setDernierMotifOuverture(java.lang.String newDernierMotifOuverture) {
        dernierMotifOuverture = newDernierMotifOuverture;
    }

    public void setEtatEntity(int newEtatEntity) {
        etatEntity = newEtatEntity;
    }

    /**
     * @param inactive
     *            the inactive to set
     */
    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public void setInvalide(Boolean invalide) {
        this.invalide = invalide;
    }

    /**
     * @param string
     */
    public void setLikeInIdAffiliation(String string) {
        likeInIdAffiliation = string;
    }

    /**
     * Sets the selectedNoAvs.
     * 
     * @param selectedNoAvs
     *            The selectedNoAvs to set
     */
    public void setMainSelectedId(String selectedId) {
        mainSelectedId = selectedId;
    }

    /**
     * @param boolean1
     */
    public void setNnss(Boolean boolean1) {
        nnss = boolean1;
    }

    public void setNomPrenom(String newNomPrenom) {
        nomPrenom = newNomPrenom;
    }

    public void setNumeroAvs(String newNumeroAvs) {
        numeroAvs = CIUtil.unFormatAVS(newNumeroAvs);
    }

    public void setNumeroAvsAncien(String newNumeroAvsAncien) {
        numeroAvsAncien = newNumeroAvsAncien;
    }

    /**
     * @param string
     */
    public void setNumeroAvsNNSS(String string) {
        numeroAvsNNSS = string;
    }

    public void setNumeroAvsPrecedant(String newNumeroAvsPrecedant) {
        numeroAvsPrecedant = newNumeroAvsPrecedant;
    }

    public void setPaysOrigineId(String newPaysOrigineId) {
        paysOrigineId = newPaysOrigineId;
    }

    /**
     * Sets the plausiNumAvs.
     * 
     * @param plausiNumAvs
     *            The plausiNumAvs to set
     */
    public void setPlausiNumAvs(boolean plausiNumAvs) {
        this.plausiNumAvs = plausiNumAvs;
    }

    public void setProvenanceFusion(Boolean newProvenanceFusion) {
        provenanceFusion = newProvenanceFusion;
    }

    public void setReferenceInterne(String newReferenceInterne) {
        referenceInterne = newReferenceInterne;
    }

    public void setRegistre(String newRegistre) {
        registre = newRegistre;
    }

    public void setSexe(String newSexe) {
        sexe = newSexe;
    }

    public void setSrcUpi(String srcUpi) {
        this.srcUpi = srcUpi;
    }

    public void setUtilisateurCreation(String newUtilisateurCreation) {
        utilisateurCreation = newUtilisateurCreation;
    }

    public String toMyString() {
        return "Avs: " + getNumeroAvs() + ", Nom: " + getNomPrenom() + ", Date: " + getDateNaissance();
    }
}
