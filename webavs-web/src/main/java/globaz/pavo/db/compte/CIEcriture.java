package globaz.pavo.db.compte;

import globaz.commons.nss.NSUtil;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuelles;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuellesManager;
import globaz.framework.secure.user.FWSecureUserDetail;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JATime;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.db.inscriptions.CIJournalManager;
import globaz.pavo.db.inscriptions.CIRemarque;
import globaz.pavo.translation.CodeSystem;
import globaz.pavo.util.CIAffilie;
import globaz.pavo.util.CIUtil;
import globaz.pyxis.adresse.formater.TILocaliteLongFormater;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIHistoriqueTiers;
import globaz.pyxis.db.tiers.TIHistoriqueTiersManager;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Iterator;

/**
 * Object représentant une écriture. TODO: remplacer les SQL joins par l'API (à faire après optimisation de l'API).
 * Actuellement, fait accès au tables de PYXIS Date de création : (12.11.2002 13:05:40)
 * 
 * @author: David Girardin
 */
public class CIEcriture extends BEntity {
    /**
     * 
     */

    public static final String DATE_CODE_SPECIAL04_MANDATORY = "01.01.2016";

    private static final long serialVersionUID = 1L;
    public final static String CS_ANNEE_JEUNESSE = "306001";
    // Type de compte
    public final static String CS_CI = "303001";
    public final static String CS_CI_SUSPENS = "303002";
    public final static String CS_CI_SUSPENS_SUPPRIMES = "303003";
    // Code genre d''une inscription CI
    public final static String CS_CIGENRE_0 = "310000";
    public final static String CS_CIGENRE_1 = "310001";
    public final static String CS_CIGENRE_2 = "310002";
    public final static String CS_CIGENRE_3 = "310003";
    public final static String CS_CIGENRE_4 = "310004";
    public final static String CS_CIGENRE_5 = "310005";
    public final static String CS_CIGENRE_6 = "310006";
    public final static String CS_CIGENRE_7 = "310007";
    public final static String CS_CIGENRE_8 = "310008";
    public final static String CS_CIGENRE_9 = "310009";
    // Code amortissement
    public final static String CS_CODE_AMORTISSEMENT = "313001";
    public final static String CS_CODE_EXEMPTION = "313002";
    public final static String CS_CODE_MIS_EN_COMTE = "313005";
    public final static String CS_CODE_PROVISOIRE = "313004";
    public final static String CS_CODE_SURSIS = "313003";
    public final static String CS_COLLABORATEUR_FAMILIAL = "320003";
    public final static String CS_COLLABORATEUR_FAMILIAL_AGRICOLE = "320005";
    public final static String CS_CORRECTION = "303005";
    // Code spécial pour inscription CI
    public final static String CS_COTISATION_MINIMALE = "312001";
    // Code extourne d''une inscription CI
    public final static String CS_EXTOURNE_1 = "311001";
    public final static String CS_EXTOURNE_2 = "311002";
    public final static String CS_EXTOURNE_3 = "311003";
    public final static String CS_EXTOURNE_5 = "311005";
    public final static String CS_EXTOURNE_6 = "311006";
    public final static String CS_EXTOURNE_7 = "311007";
    public final static String CS_EXTOURNE_8 = "311008";
    public final static String CS_EXTOURNE_9 = "311009";
    public final static String CS_GENRE_6 = "303004";
    public final static String CS_GENRE_7 = "303007";
    // Branche économique
    public final static String CS_HORLOGERIE = "314033";
    public final static String CS_LACUNE_COTISATION_APPOINT = "306003";
    public final static String CS_LACUNE_COTISATION_JEUNESSE = "306002";
    public final static String CS_LICHTENSTEIN = "306006";
    // Particulier
    public final static String CS_MANDAT_NORMAL = "306000";
    public final static String CS_NONFORMATTEUR_INDEPENDANT = "312002";
    public final static String CS_NONFORMATTEUR_NONACTIF = "312004";
    public final static String CS_NONFORMATTEUR_SALARIE = "312003";
    public final static String CS_PARTAGE_CI_CLOTURES = "306005";
    public final static String CS_PARTAGE_RAM = "306004";
    public final static String CS_PERSONNEL_AGRICOLE_APPRENTI = "320004";
    // Catégorie de personnel
    public final static String CS_PERSONNEL_EXPLOITATION = "320001";
    public final static String CS_PERSONNEL_FEMININ = "320002";

    public final static String CS_TEMPORAIRE = "303006";
    public final static String CS_TEMPORAIRE_SUSPENS = "303008";
    public final static String MODE_EXTOURNE = "1";
    public final static String MODE_NORMAL = "0";
    private static String NIV_SECURITY_X = "31700";
    // code de sécurité (1-9)
    private static String SECURE_CODE = "SecureCode";
    private String affHist = "";
    private String aijApgAmi = new String();
    /** (KBNANN) */
    private String annee = new String();
    private String avsNNSS = "";
    // pour voir si le numéro AVS à changé => besoin de comptabiliser(zu
    // orddnung)
    private String avsPrecedent = new String();
    /** (KBTBRA) */
    private String brancheEconomique = new String();
    private boolean cacherMontant = false;// définit si le montant doit être
    // caché (Sécurité CI sur
    // affiliation)
    /** (KBICHO) */
    private String caisseChomage = new String();
    private String caisseTenantCI = new String();
    /** (KBTCAN) */
    private String cantonAF = new String();
    /** (KBTCAT) */
    private String categoriePersonnel = new String();
    /** (KBBIMP) */
    private Boolean certificat = new Boolean(false);
    // ci assuré
    CICompteIndividuel ci = null;
    /** (KBTCOD) */
    private String code = new String();
    /** (KBTSPE) */
    private String codeSpecial = new String();
    /** (KAIIND) */
    private String compteIndividuelId = new String();
    /** (KBDCEN) */
    private String dateAnnonceCentrale = new String();
    /** (KBDADD) */
    private String dateCiAdditionnel = new String();
    private String dateCloture = new String();
    private String dateOrdre = new String();
    // Nous indique si on viens de l'ecran qui afficher les inscriptions en
    // suspns
    private String ecranInscriptionsSuspens = "False";

    /** (KBIECR) */
    private String ecritureId = new String();

    /** (KBIERE) */
    private String ecritureReference = new String();

    /** (KBITIE) */
    private String employeur = new String();
    // Un identifiant du partenaire ou de l'affilié, suivant le genre cf.
    private String employeurPartenaire = new String();
    private String espionSaisie = new String();
    /** (KBLSUP) */
    private String espionSaisieSup = new String();
    // si le ci est au registre des assuré
    private String estRa = new String();
    /** (KBTEXT) */
    private String extourne = new String();
    private boolean forAffilieParitaire = false;
    private boolean forAffiliePersonnel = false;
    private String fromEcran = "false";
    /** (KBTGEN) */
    private String genreEcriture = new String();
    // Buffer pour le GRE avant d'être parser dans le _validate
    private String gre = new String();
    // Numéro d'affilié de l'employeur
    private String idAffilie = new String();
    /** (KCID) */
    private String idJournal = new String();
    /** (KBIDLO) */
    private String idLog = new String();
    /** (KIIREM) */
    private String idRemarque = new String();
    /** (KBTCPT) */
    private String idTypeCompte = new String();
    private String idTypeCompteAvantTentativeSplitting = "";
    private String idTypeComptePrecedant = new String();
    private boolean isFromFacturationCompta = false;
    private Boolean isNNSS = new Boolean(false);
    private Boolean isNNSSConjoint = new Boolean(false);

    private String jourDebut = "";
    private String jourFin = "";

    // journal de l'écriture
    private CIJournal journal = null;
    private String libelleAff = "";
    // Les messages
    private String messages = new String();
    private String mitgliedNumNNSS = "";
    // mode d'ajout (NORMAL/EXTOURNE)
    private String modeAjout = CIEcriture.MODE_NORMAL;
    /** (KBNMOD) */
    private String moisDebut = new String();
    /** (KBNMOF) */
    private String moisFin = new String();
    /** (KBMMON) */
    private String montant = new String();
    private String montantPrecedant = new String();
    private String motifCloture = new String();
    // Voir si on a besoin d'une nouvelle date de comptabilisation
    private boolean needNewDateCompta = true;
    // nom du créateur et modificateur de l'écriture
    private String nomCreator = "";
    private String nomModifier = "";
    private String nomPrenomAssure = new String();
    // nom complet de celui qui supprime de l'écriture
    private String nomSuppressor = "";
    // Nom du tiers
    private String nomTiers = new String();
    /** (KBLESP) */
    private boolean noSum = false;
    private String numeroavsNNSS = "";
    private CICompteIndividuel oldCI = null;
    /** (KBNBTA) */
    private String partBta = new String();
    /** (KBIPAR) */
    private String partenaireId = new String();
    // Nom, prénom du partenaire en cas de splitting
    private String partenaireNomPrenom = new String();
    // Numéro AVS du partenaire en cas de splitting
    private String partenaireNumAvs = new String();
    /** (KBTPAR) */
    private String particulier = new String();
    private boolean plausiMois = false;
    // Nom du tiers
    private String prenomTiers = new String();
    /** (KKIRAO) */
    private String rassemblementOuvertureId = new String();
    // La remarque
    private CIRemarque remarque = null;
    // flag d'exécution simple
    private boolean simpleProcess = false;
    // flag suppression depuis déclaration
    private boolean deleteFromDS = false;

    // dernier total de contrôle (champs utile pour la saisie de masse)
    private String totalControleSaisie = "";
    // flag en cours de comptabilisation
    private boolean updating = false;

    /* permet de savoir s'il g'agit d'une Ami Ai ou APG */
    /** (KBBATT) */
    private Boolean wantForDeclaration = new Boolean(true);

    // wrapper d'utilitaires
    private CIEcritureUtil wrapperUtil = null;

    // code systeme
    /**
     * Commentaire relatif au constructeur CIEcriture
     */
    public CIEcriture() {
        super();
        // écriture provisoire (saisie) par défaut
        setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
    }

    /**
     * La méthode update est toujours appelée sur un afterAdd() et se charge de mettre à jour le CI, si besoin est.
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws java.lang.Exception {
        // On met a jour le total des montants inscrits
        if (!simpleProcess) {
            if (isSumNeeded()) {
                CIJournal journalToUse = getJournal(transaction, true);
                if ((!transaction.hasErrors()) && (!journalToUse.isNew())) {
                    // On met à jour son revenu inscrit
                    journalToUse.retrieve(transaction);
                    journalToUse.updateInscription(transaction, totalControleSaisie);
                } else {
                    _addError(transaction, getSession().getLabel("MSG_JOURNAL_INSCRIT"));
                }

            }
            // test du total avec les écriture de l'année et du même employeur
            if (CIEcriture.MODE_NORMAL.equals(modeAjout)) {

            }

            if (transaction.hasErrors()) {
                setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
                idTypeComptePrecedant = CIEcriture.CS_TEMPORAIRE;
                journal = null;
            }
        }
    }

    @Override
    protected void _afterDelete(BTransaction transaction) throws java.lang.Exception {
        // remarque
        if (remarque != null) {
            // sync
            transaction.disableSpy();
            remarque.delete(transaction);
            transaction.enableSpy();
        }
        if (!simpleProcess) {
            // On met a jour le total des montants inscrits
            CIJournal journal = getJournal(transaction, true);
            if ((!transaction.hasErrors()) && (!journal.isNew())) {
                // On met à jour son revenu inscrit
                journal.updateInscription(transaction, totalControleSaisie);
            } else {
                _addError(transaction, getSession().getLabel("MSG_JOURNAL_INSCRIT"));

            }
        }

        if (ci != null) {
            ci.deleteIfEmpty(transaction, true);
        }

    }

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws java.lang.Exception {
        // noms créateur et modifieur
        if (!isLoadedFromManager()) {
            nomModifier = CIUtil.getUserNomComplet(getSpy().getUser(), transaction);
            nomCreator = CIUtil.getUserNomComplet(new BSpy(getEspionSaisie()).getUser(), transaction);
        }
        avsPrecedent = getAvs();
        // nom du suppresseur de l'écriture en suspens
        if (!JadeStringUtil.isBlank(getEspionSaisieSup())) {
            setNomSuppressor(CIUtil.getUserNomComplet(new BSpy(getEspionSaisieSup()).getUser(), transaction));
        }
    }

    @Override
    protected void _afterRetrieveWithResultSet(BStatement statement) throws java.lang.Exception {
        // lire la remarque depuis la jointure
        if (!JadeStringUtil.isIntegerEmpty(getIdRemarque())) {
            remarque = new CIRemarque();
            remarque.read(statement);
        }
        // lire le journal depuis la jointure
        if (!JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            journal = new CIJournal();
            journal.setLoadedFromManager(true);
            journal.setEtatEntity(CIJournal.FROM_JOIN);
            journal.read(statement);
        }
        // lire le ci de l'assuré
        if (!JadeStringUtil.isIntegerEmpty(getCompteIndividuelId())) {
            ci = new CICompteIndividuel();
            ci.setLoadedFromManager(true);
            ci.setEtatEntity(CICompteIndividuel.FROM_JOIN);
            ci.read(statement);
        }
    }

    @Override
    protected void _afterUpdate(BTransaction transaction) throws java.lang.Exception {
        if (!simpleProcess) {
            // On met a jour le total des montants inscrits
            CIJournal journalToUse = getJournal(transaction, true);
            if ((!transaction.hasErrors()) && (!journalToUse.isNew())) {
                // On met à jour son revenu inscrit
                if (CIEcriture.CS_GENRE_6.equals(idTypeComptePrecedant)
                        || CIEcriture.CS_GENRE_7.equals(idTypeComptePrecedant)
                        || CIEcriture.CS_CI.equals(idTypeComptePrecedant)) {
                    // pour optimiser, ne pas faire la somme de toutes les
                    // écritures
                    FWCurrency newMontant = new FWCurrency(getMontant());
                    newMontant.sub(montantPrecedant);
                    if (!JadeStringUtil.isIntegerEmpty(getExtourne())
                            && !CIEcriture.CS_EXTOURNE_2.equals(getExtourne())
                            && !CIEcriture.CS_EXTOURNE_6.equals(getExtourne())
                            && !CIEcriture.CS_EXTOURNE_8.equals(getExtourne())) {
                        newMontant.negate();
                    }
                    if (!newMontant.isZero()) {
                        journalToUse.retrieve(transaction);
                        journalToUse.updateInscriptionWith(transaction, newMontant, totalControleSaisie);
                    }
                } else {
                    journalToUse.retrieve(transaction);
                    journalToUse.updateInscription(transaction, totalControleSaisie);
                }
            } else {
                journal = null;
                // bug framework -> relecture du spy original
                transaction.rollback();
                this.retrieve(transaction);
                _addError(transaction, getSession().getLabel("MSG_JOURNAL_INSCRIT"));
            }

        }
        // l'ancien ci est peut-être maintenant vide
        if ((oldCI != null) && (oldCI.getCompteIndividuelId() != getCompteIndividuelId())) {
            oldCI.deleteIfEmpty(transaction, true);
        }
        // On recherche dans draco, s'il faut mettre à jour l'entête en cas de
        // zuordnung
        if (!needNewDateCompta) {
            DSInscriptionsIndividuellesManager dracoMgr = new DSInscriptionsIndividuellesManager();
            dracoMgr.setForIdEcrtirureCI(ecritureId);
            dracoMgr.setSession(getSession());
            dracoMgr.find(transaction);
            if (dracoMgr.size() > 0) {
                DSInscriptionsIndividuelles inscInd = (DSInscriptionsIndividuelles) dracoMgr.getFirstEntity();
                inscInd.setCompteIndividuelId(getCI(transaction, true).getCompteIndividuelId());
                inscInd.wantCallMethodBefore(false);
                inscInd.wantCallMethodAfter(false);
                inscInd.wantCallValidate(false);
                inscInd.update(transaction);
            }

        }
        setUpdating(false);
    }

    /**
     * Reset de l'id si entity en erreur
     */
    @Override
    protected void _alwaysAfterAdd(BTransaction transaction) throws java.lang.Exception {

        if (transaction.hasErrors()) {
            setEcritureId("");
            setIdAffilie("");
            setEmployeur("");

        }
        setUpdating(false);
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {

        // Incrémeter le compteur d'inscriptions
        if (JadeStringUtil.isIntegerEmpty(ecritureId)) {
            setEcritureId(this._incCounter(transaction, "0"));
        }
        // espion saisie
        if (JadeStringUtil.isBlank(getEspionSaisie())) {
            setEspionSaisie(new BSpy(getSession()).toString());
        }

        if (CIEcriture.MODE_NORMAL.equals(modeAjout)) {
            if (JadeStringUtil.isBlank(getCaisseTenantCI())) {
                setCaisseTenantCI(((CIApplication) GlobazServer.getCurrentSystem().getApplication(
                        CIApplication.DEFAULT_APPLICATION_PAVO)).getAdministrationLocale(getSession())
                        .getIdTiersAdministration());
            }

            if (!simpleProcess) {
                // Valeurs par defaut
                valeursDefaut(transaction);
            }
        }
        setUpdating(true);
        if (remarque != null) {
            remarque.add(transaction);
            setIdRemarque(remarque.getIdRemarque());
        }
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws java.lang.Exception {
        // Le type de compte est CI, GENRE_7 ou GENRE_6, seul le genre 8 est possible
        if ((CIEcriture.CS_CI.equals(getIdTypeCompte())) || (CIEcriture.CS_GENRE_6.equals(getIdTypeCompte()))
                || (CIEcriture.CS_GENRE_7.equals(getIdTypeCompte()))) {
            if (!CIEcriture.CS_CIGENRE_8.equals(getGenreEcriture())) {
                _addError(
                        transaction,
                        getSession().getLabel("MSG_ECRITURE_DEL_TYPCOM")
                                + CodeSystem.getLibelle(getIdTypeCompte(), transaction.getSession()));
            }
        } else {
            // Retrieve du journal correspondant
            CIJournal journal = getJournal(transaction, false);
            // Suppression impossible si le journal correspondant n'est pas
            // ouvert.
            if (CIJournal.CS_COMPTABILISE.equals(journal.getIdEtat())) {
                _addError(transaction, getSession().getLabel("MSG_ECRITURE_DEL_ETAT"));
            }
        }
        // PO 8303: Pas de suppression possible si l'écriture est lié dans une déclaration de salaire
        if (!isDeleteFromDS()) {
            DSInscriptionsIndividuellesManager dsMng = new DSInscriptionsIndividuellesManager();
            dsMng.setSession(getSession());
            dsMng.setForIdEcrtirureCI(getEcritureId());
            if (dsMng.getCount() > 0) {
                _addError(transaction, getSession().getLabel("MSG_CI_LIE_A_DS"));
            }
        }

    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws java.lang.Exception {
        // bz 9056 ajout d'une plausi pour qu'on ne puisse pas mettre un genre vide.
        if (JadeStringUtil.isBlankOrZero(getGenreEcriture())) {
            _addError(transaction, getSession().getLabel("MSG_ECRITURE_GRE"));
        }
        // remarque
        if (remarque != null) {
            if (JadeStringUtil.isBlank(remarque.getTexte())) {
                transaction.disableSpy();
                remarque.delete(transaction);
                transaction.enableSpy();
                setIdRemarque("0");
            } else {
                transaction.disableSpy();
                remarque.save(transaction);
                transaction.enableSpy();
                setIdRemarque(remarque.getIdRemarque());
            }
        }
        // On set la date et l'heure de suppression d'une écriture en suspens
        if (!idTypeComptePrecedant.equals(CIEcriture.CS_CI_SUSPENS_SUPPRIMES)
                && idTypeCompte.equals(CIEcriture.CS_CI_SUSPENS_SUPPRIMES)) {

            setEspionSaisieSup(new BSpy(getSession()).toString());
        }

        setUpdating(true);
    }

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return _getCollection() + _getTableName() + ".*, " + _getCollection() + "CIINDIP.*, " + _getCollection()
                + "CIJOURP.*, " + _getCollection() + "AFAFFIP.MALNAF, " + _getCollection() + "TITIERP.HTLDE1, "
                + _getCollection() + "TITIERP.HTLDE2, " + "CIINDIPPA.KANAVS as PARTENAIRE_KANAVS, "
                + "CIINDIPPA.KALNOM as PARTENAIRE_KALNOM, " + "CIINDIPPA.KABNNS as PARTENAIRE_KABNNS, "
                + _getCollection() + "CIREMAP.*, " + _getCollection() + "CIRAOUP.KKDCLO, " + _getCollection()
                + "CIRAOUP.KKDORD, " + _getCollection() + "CIRAOUP.KKTARC";
    }

    /**
     * Renvoie la clause FROM
     * 
     * @return la clause FROM
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String joinStr = new String();
        // Récupération du CI, du tiers/partenaire
        // Obligation de renommée la table CIINDIP pour requête sur le
        // partenaire
        joinStr = " inner join " + _getCollection() + "CIINDIP on " + _getCollection() + _getTableName() + ".KAIIND="
                + _getCollection() + "CIINDIP.KAIIND" + " inner join " + _getCollection() + "CIJOURP on "
                + _getCollection() + _getTableName() + ".KCID=" + _getCollection() + "CIJOURP.KCID"
                + " left outer join " + _getCollection() + "AFAFFIP on " + _getCollection() + _getTableName()
                + ".KBITIE=" + _getCollection() + "AFAFFIP.MAIAFF" + " left outer join " + _getCollection()
                + "TITIERP on " + _getCollection() + "AFAFFIP.HTITIE=" + _getCollection() + "TITIERP.HTITIE"
                + " left outer join " + _getCollection() + "CIINDIP AS CIINDIPPA on " + _getCollection()
                + _getTableName() + ".KBIPAR=CIINDIPPA.KAIIND" + " left outer join " + _getCollection() + "CIREMAP on "
                + _getCollection() + _getTableName() + ".KIIREM=" + _getCollection() + "CIREMAP.KIIREM"
                + " left outer join " + _getCollection() + "CIRAOUP on " + _getCollection() + _getTableName()
                + ".KKIRAO=" + _getCollection() + "CIRAOUP.KKIRAO";
        return _getCollection() + _getTableName() + joinStr;
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CIECRIP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        ecritureId = statement.dbReadNumeric("KBIECR");
        compteIndividuelId = statement.dbReadNumeric("KAIIND");
        rassemblementOuvertureId = statement.dbReadNumeric("KKIRAO");
        idJournal = statement.dbReadNumeric("KCID");
        idRemarque = statement.dbReadNumeric("KIIREM");
        ecritureReference = statement.dbReadNumeric("KBIERE");
        employeur = statement.dbReadNumeric("KBITIE");
        partenaireId = statement.dbReadNumeric("KBIPAR");
        extourne = statement.dbReadNumeric("KBTEXT");
        // On sauve l'extourne dans un backup pour mise à jour
        // previousExtourne = statement.dbReadNumeric("KBTEXT");
        genreEcriture = statement.dbReadNumeric("KBTGEN");
        particulier = statement.dbReadNumeric("KBTPAR");
        partBta = statement.dbReadNumeric("KBNBTA");
        codeSpecial = statement.dbReadNumeric("KBTSPE");
        moisDebut = statement.dbReadNumeric("KBNMOD");
        moisFin = statement.dbReadNumeric("KBNMOF");
        annee = statement.dbReadNumeric("KBNANN");
        montant = statement.dbReadNumeric("KBMMON", 2);
        montantPrecedant = montant;
        // On sauve le montant dans un backup pour mise à jour
        // previousMontant = statement.dbReadNumeric("KBMMON",2);
        code = statement.dbReadNumeric("KBTCOD");
        brancheEconomique = statement.dbReadNumeric("KBTBRA");
        idTypeCompte = statement.dbReadNumeric("KBTCPT");
        idTypeComptePrecedant = idTypeCompte;
        dateAnnonceCentrale = statement.dbReadDateAMJ("KBDCEN");
        caisseChomage = statement.dbReadNumeric("KBICHO");
        categoriePersonnel = statement.dbReadNumeric("KBTCAT");
        dateCiAdditionnel = statement.dbReadDateAMJ("KBDADD");
        idLog = statement.dbReadNumeric("KBIDLO");
        espionSaisie = statement.dbReadString("KBLESP");
        espionSaisieSup = statement.dbReadString("KBLSUP");
        caisseTenantCI = statement.dbReadNumeric("KBCAIT");

        // Données du tiers
        idAffilie = statement.dbReadString("MALNAF");
        nomTiers = statement.dbReadString("HTLDE1");
        prenomTiers = statement.dbReadString("HTLDE2");
        // Données du partenaire (pour le genre 8)
        partenaireNumAvs = statement.dbReadString("PARTENAIRE_KANAVS");
        partenaireNomPrenom = statement.dbReadString("PARTENAIRE_KALNOM");
        // date cloture
        dateCloture = statement.dbReadDateAMJ("KKDCLO");
        dateOrdre = statement.dbReadDateAMJ("KKDORD");
        motifCloture = statement.dbReadString("KKTARC");
        certificat = statement.dbReadBoolean("KBBIMP");
        // cantonAF = statement.dbReadNumeric("KBTCAN");
        wantForDeclaration = statement.dbReadBoolean("KBBATT");
        isNNSS = statement.dbReadBoolean("KABNNS");
        isNNSSConjoint = statement.dbReadBoolean("PARTENAIRE_KABNNS");
        if (isNNSS.booleanValue()) {
            setNumeroavsNNSS("true");
        } else {
            setNumeroavsNNSS("false");
        }
        if (isNNSSConjoint.booleanValue()) {
            setMitgliedNumNNSS("true");
        } else {
            setMitgliedNumNNSS("false");
        }
        libelleAff = statement.dbReadString("KBLIB");
        affHist = statement.dbReadString("KBIAFF");
        nomPrenomAssure = statement.dbReadString("KALNOM");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Inforom 572 - Autoriser le changement de num affilié et le genre pour les spécialistes
        boolean modifAvecCodeSpecial = false;
        if (JadeStringUtil.isEmpty(getGre()) && "true".equalsIgnoreCase(getFromEcran())) {
            modifAvecCodeSpecial = true;
            setGenreEcriture(codeUtilisateurToCodeSystemeGre(statement.getTransaction(), getGenreEcriture(), "CICODGEN"));
            if (getGenreEcriture().equalsIgnoreCase(CIEcriture.CS_CIGENRE_5) == false) {
                CIApplication app = (CIApplication) getSession().getApplication();
                AFAffiliation aff = app.getAffilieByNo(getSession(), employeurPartenaire, forAffilieParitaire,
                        forAffiliePersonnel, getMoisDebut(), getMoisFin(), getAnnee(), getJourDebut(), getJourFin());
                if ((aff == null) || (aff.isNew())) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_AFFILIE"));
                } else {
                    setEmployeur(aff.getAffiliationId());
                }
            } else {
                setEmployeur("");
            }
        }

        if ("true".equalsIgnoreCase(getFromEcran())
                && "false".equalsIgnoreCase(ecranInscriptionsSuspens)
                && !isNew()
                && ((CIEcriture.CS_CI.equals(idTypeCompte) && CIEcriture.CS_CI.equals(idTypeComptePrecedant)) || (CIEcriture.CS_GENRE_6
                        .equals(idTypeCompte) && CIEcriture.CS_GENRE_6.equals(idTypeComptePrecedant)))) {
            CIEcriture ecritureHist = new CIEcriture();
            ecritureHist.setSession(getSession());
            ecritureHist.setId(getId());
            ecritureHist.copie();
            ecritureHist.setModeAjout(CIEcriture.MODE_EXTOURNE);
            ecritureHist.setIdTypeCompte(CIEcriture.CS_CORRECTION);
            ecritureHist.simpleAdd(statement.getTransaction());
        }
        // copie du CI si modification ultérieure
        oldCI = ci;
        if (!isNew()) {
            // si c'est une zuordnung on veut compatabliser l'ecriture sans
            // changer la date de compta
            if (CIEcriture.CS_CI_SUSPENS.equals(idTypeComptePrecedant)
                    && !CIUtil.unFormatAVS(avsPrecedent).equals(CIUtil.unFormatAVS(getAvs()))) {
                idTypeComptePrecedant = "";
                idTypeCompte = CIEcriture.CS_CI;
                needNewDateCompta = false;
                setNumeroavsNNSS(getAvsNNSS());
            }
        }
        // Initialisation des valeurs numériques de moisDebut, moisFin et annee

        // SI l'ecriture est au CI => pas plausi à part les mois
        if (CIEcriture.CS_CI.equals(idTypeComptePrecedant)
                || CIEcriture.CS_CI_SUSPENS_SUPPRIMES.equals(idTypeComptePrecedant)) {
            setModeAjout(CIEcriture.MODE_EXTOURNE);
            simpleProcess = true;
            plausiMois = true;
        }
        if (CIEcriture.CS_CORRECTION.equals(idTypeComptePrecedant)) {
            setModeAjout(CIEcriture.MODE_EXTOURNE);
            simpleProcess = true;
            plausiMois = false;
        }
        if (plausiMois && !JadeStringUtil.isBlank(getGenreEcriture())) {

            int moisDebutPla = 0;
            if (!CIEcriture.CS_CIGENRE_8.equals(getGenreEcriture()) || JadeStringUtil.isIntegerEmpty(partBta)) {
                moisDebutPla = 1;
            }
            if (!JadeStringUtil.isBlank(getMoisDebut())) {
                moisDebutPla = new Integer(getMoisDebut().trim()).intValue();
            }
            int moisFinPla = 0;
            if (!CIEcriture.CS_CIGENRE_8.equals(getGenreEcriture()) || !JadeStringUtil.isIntegerEmpty(partBta)) {
                moisFinPla = 12;
            }
            if (!JadeStringUtil.isBlank(getMoisFin())) {
                moisFinPla = new Integer(getMoisFin().trim()).intValue();
            }
            // Verification du format du mois de début
            if (!(((0 <= moisDebutPla) && (moisDebutPla <= 12)) || (moisDebutPla == 66) || (moisDebutPla == 77) || (moisDebutPla == 99))) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_DATEDEB"));
            }
            // Verification du format du mois de fin
            if (!(((0 <= moisFinPla) && (moisFinPla <= 12)) || (moisFinPla == 66) || (moisFinPla == 77) || (moisFinPla == 99))) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_DATEFIN"));
            }

            // Le mois de fin doit être postérieur au mois de début
            if (moisDebutPla > moisFinPla) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_DATEDEBFIN"));
            } // Valeurs autoriséees pour l'extourne
            if (!CIEcriture.CS_CIGENRE_8.equals(getGenreEcriture()) && JadeStringUtil.isIntegerEmpty(partBta)) {
                if ((1978 < Integer.parseInt(getAnnee()))
                        && (JadeStringUtil.isIntegerEmpty(getMoisDebut())
                                || JadeStringUtil.isIntegerEmpty(getMoisFin())
                                || JadeStringUtil.isBlank(getMoisDebut()) || JadeStringUtil.isBlank(getMoisFin()))) {
                    _addError(statement.getTransaction(), getSession().getLabel("PLA_ECR_MOIS_VIDES"));
                }
            }
        }

        if (JadeStringUtil.isIntegerEmpty(ecritureId)) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_ID_VIDE"));
        }
        if (CIEcriture.MODE_NORMAL.equals(modeAjout) || modifAvecCodeSpecial) {
            // Si l'inscription CI est de type "BTA", les mois de cotisations
            // doivent être automatiquement définis à "00" - "00"
            if (!JadeStringUtil.isBlankOrZero(partBta)
                    && ((!getMoisDebut().equals("00") && !getMoisDebut().equals("0")) || (!getMoisFin().equals("00") && !getMoisFin()
                            .equals("0")))) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_DATE_BTA"));
            }
            if (!JadeStringUtil.isBlankOrZero(partBta)
                    && (!JadeStringUtil.isBlankOrZero(getCode()) || !JadeStringUtil.isBlankOrZero(getCodeSpecial()))) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_CODE_BTA"));
            }
            // fin plausi nom

            if (!JadeStringUtil.isIntegerEmpty(getCaisseChomage())) {
                setIdAffilie("");
            }

            // PARSER LE GRE
            parserGre(statement);
            // CONVERSION DES CODES UTILISATEURS
            if (getPartBta().length() == 6) {
                setPartBta(CodeSystem.getCodeUtilisateur(getPartBta(), getSession()));
            }
            setCodeSpecial(codeUtilisateurToCodeSysteme(statement.getTransaction(), getCodeSpecial(), "CICODSPE"));
            setBrancheEconomique(codeUtilisateurToCodeSysteme(statement.getTransaction(), getBrancheEconomique(),
                    "VEBRANCHEE"));
            setCode(codeUtilisateurToCodeSysteme(statement.getTransaction(), getCode(), "CICODAMO"));
            setExtourne(codeUtilisateurToCodeSysteme(statement.getTransaction(), getExtourne(), "CICODEXT"));
            setGenreEcriture(codeUtilisateurToCodeSystemeGre(statement.getTransaction(), getGenreEcriture(), "CICODGEN"));
            setParticulier(codeUtilisateurToCodeSysteme(statement.getTransaction(), getParticulier(), "CIGENSPL"));
            setPaysOrigine(codeUtilisateurToCodeSysteme(statement.getTransaction(), getPaysOrigine(), "CIPAYORI"));
            // Doit être 0 si le code est D ou S ou si partBta est renseigné
            if ((CIEcriture.CS_CODE_EXEMPTION.equals(getCode())) || (CIEcriture.CS_CODE_SURSIS.equals(getCode()))
                    || (!JadeStringUtil.isIntegerEmpty(getPartBta()))) {
                if (!JadeStringUtil.isIntegerEmpty(getMontant())) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_REVENU_NOT"));
                }
            } else if (JadeStringUtil.isIntegerEmpty(getMontant()) || (new FWCurrency(getMontant()).isNegative())) {
                if (JadeStringUtil.isIntegerEmpty(getRassemblementOuvertureId())) {
                    // effectuer le test uniquement si l'écriture n'est pas
                    // clôturée
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_REVENU"));
                }
            }
            if (!JadeStringUtil.isBlank(getAvs())) {
                if (CIEcriture.CS_CIGENRE_6.equals(getGenreEcriture()) && isCiRa()) {
                    _addError(statement.getTransaction(), getSession().getLabel("PLA_ECR_IMPOSS_GENRE_SIX"));
                }
            }
            // Initialisation des valeurs numériques de moisDebut, moisFin et
            // annee
            int annee = 0;
            if (!JadeStringUtil.isBlank(getAnnee())) {
                annee = new Integer(getAnnee().trim()).intValue();
            }
            int moisDebut = 0;
            if (!CIEcriture.CS_CIGENRE_8.equals(getGenreEcriture())) {
                moisDebut = 1;
            }
            if (!JadeStringUtil.isBlank(getMoisDebut())) {
                moisDebut = new Integer(getMoisDebut().trim()).intValue();
            }
            int moisFin = 0;
            if (!CIEcriture.CS_CIGENRE_8.equals(getGenreEcriture())) {
                moisFin = 12;
            }
            if (!JadeStringUtil.isBlank(getMoisFin())) {
                moisFin = new Integer(getMoisFin().trim()).intValue();
            }
            // Verification du format du mois de début
            if (!(((0 <= moisDebut) && (moisDebut <= 12)) || (moisDebut == 66) || (moisDebut == 77) || (moisDebut == 99))) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_DATEDEB"));
            }
            /***
             * Initialisation des jours de début et de fin pour plausi
             */

            // Verification du format du mois de fin
            if (!(((0 <= moisFin) && (moisFin <= 12)) || (moisFin == 66) || (moisFin == 77) || (moisFin == 99))) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_DATEFIN"));
            }

            // Le mois de fin doit être postérieur au mois de début
            if (moisDebut > moisFin) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_DATEDEBFIN"));
            } // Valeurs autoriséees pour l'extourne

            if ((annee < 1969) || (annee > 1975)) {
                if (!JadeStringUtil.isIntegerEmpty(getExtourne()) && !CIEcriture.CS_EXTOURNE_1.equals(getExtourne())
                        && !CIEcriture.CS_EXTOURNE_8.equals(getExtourne())
                        && !CIEcriture.CS_EXTOURNE_9.equals(getExtourne())) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_EXTOURNE"));
                }
            }
            // Annee supérieure à 1948
            if (annee < 1948) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_ANNEE"));
            }
            // Annee > année actuelle
            //
            if (JACalendar.today().getYear() < annee) {
                // BTC: changement pour cot. Pers.
                // si le journal est de type COTISATION PERSONNELLES, ne pas
                // ajourter d'erreurs dans le buffer
                CIJournal journal = getJournal(statement.getTransaction(), true);
                if (!journal.getIdTypeInscription().equalsIgnoreCase(CIJournal.CS_COTISATIONS_PERSONNELLES)) {
                    // pour la facultative, on autorise annee +1
                    if (journal.getIdTypeInscription().equalsIgnoreCase(CIJournal.CS_ASSURANCE_FACULTATIVE)) {
                        if (JACalendar.today().getYear() < (annee - 1)) {
                            _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_ANNEE_TODAY"));
                        }
                    } else {
                        _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_ANNEE_TODAY"));
                    }
                }
            }
            // le chiffre clé particulier est nul sauf si GENRE 8 (0 à 5)
            if (CIEcriture.CS_CIGENRE_8.equals(getGenreEcriture())) {
                if (!(JadeStringUtil.isIntegerEmpty(getParticulier())
                        || CIEcriture.CS_MANDAT_NORMAL.equals(getParticulier())
                        || CIEcriture.CS_ANNEE_JEUNESSE.equals(getParticulier())
                        || CIEcriture.CS_LACUNE_COTISATION_JEUNESSE.equals(getParticulier())
                        || CIEcriture.CS_LACUNE_COTISATION_APPOINT.equals(getParticulier())
                        || CIEcriture.CS_PARTAGE_RAM.equals(getParticulier()) || CIEcriture.CS_PARTAGE_CI_CLOTURES
                            .equals(getParticulier()))) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_PARTIC"));
                }
            } else if (!(JadeStringUtil.isIntegerEmpty(getParticulier()) || CIEcriture.CS_MANDAT_NORMAL
                    .equals(getParticulier()))) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_PARTIC_NOT"));
            } // Si genre cotisation est 0 pas de part Bta

            if (CIEcriture.CS_CIGENRE_4.equals(getGenreEcriture())) {
                if (!JadeStringUtil.isIntegerEmpty(getCodeSpecial())
                        && !(CIEcriture.CS_COTISATION_MINIMALE.equals(getCodeSpecial()))) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_CODSPE4"));
                }
            }
            // Code irrécouvrable
            // Valeurs permises: blanc, A, D, S, P
            // BTC: PERMETTRE AUSSI LES MISES EN COMPTES
            if (!((JadeStringUtil.isBlank(getCode())) || (CIEcriture.CS_CODE_AMORTISSEMENT.equals(getCode()))
                    || (CIEcriture.CS_CODE_EXEMPTION.equals(getCode()))
                    || (CIEcriture.CS_CODE_SURSIS.equals(getCode()))
                    || (CIEcriture.CS_CODE_PROVISOIRE.equals(getCode())) || (CIEcriture.CS_CODE_MIS_EN_COMTE
                        .equals(getCode())))) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_CODE_VAL"));
            }

            // Pour une écriture de genre 4,0 et 7 seul D est autorisé
            if (!CIEcriture.CS_CIGENRE_4.equals(getGenreEcriture())
                    && !CIEcriture.CS_CIGENRE_7.equals(getGenreEcriture())
                    && !CIEcriture.CS_CIGENRE_0.equals(getGenreEcriture())
                    && CIEcriture.CS_CODE_EXEMPTION.equals(getCode())) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_CODE_D"));
            }
            // Pour une écriture de genre 5 ou 8, le code irrecouvrable doit
            // être vide
            if ((CIEcriture.CS_CIGENRE_5.equals(getGenreEcriture()) || CIEcriture.CS_CIGENRE_8
                    .equals(getGenreEcriture()))
                    && (!JadeStringUtil.isBlank(getCode()) && !CIEcriture.CS_CODE_PROVISOIRE.equals(getCode()))) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_CODE_NOT"));
            }

            AFAffiliation aff = null;
            if (!simpleProcess) {
                // Modif 4.12.2006

                if (!CIUtil.isCIGenreZeroPossible(getSession())) {
                    if (CIEcriture.CS_CIGENRE_0.equals(getGenreEcriture())) {
                        _addError(statement.getTransaction(), getSession().getLabel("GENRE_0_IMPOSSIBLE"));
                    }
                }
                // pour les abschreibung on empêche les codes 1,5,6,8
                if (CIEcriture.CS_CODE_AMORTISSEMENT.equals(getCode())) {
                    if (CIEcriture.CS_CIGENRE_1.equals(getGenreEcriture())
                            || CIEcriture.CS_CIGENRE_5.equals(getGenreEcriture())
                            || CIEcriture.CS_CIGENRE_6.equals(getGenreEcriture())
                            || CIEcriture.CS_CIGENRE_8.equals(getGenreEcriture())) {
                        _addError(statement.getTransaction(), getSession().getLabel("CODE_AMORT_MAUVAIS"));
                    }
                }
                // Période de cotisation 00-00 impossible après 1978
                if (!CIEcriture.CS_CIGENRE_8.equals(getGenreEcriture()) && JadeStringUtil.isIntegerEmpty(partBta)) {
                    if ((1978 < Integer.parseInt(getAnnee()))
                            && (JadeStringUtil.isIntegerEmpty(getMoisDebut())
                                    || JadeStringUtil.isIntegerEmpty(getMoisFin())
                                    || JadeStringUtil.isBlank(getMoisDebut()) || JadeStringUtil.isBlank(getMoisFin()))) {
                        _addError(statement.getTransaction(), getSession().getLabel("PLA_ECR_MOIS_VIDES"));
                    }
                }
                // Numéro d'AVS obligatoire sauf pour genre 6
                // if(!CS_CIGENRE_6.equals(getGenreEcriture())) {
                if (JadeStringUtil.isBlank(getAvs())) {
                    if (JadeStringUtil.isBlank(getNomPrenom())) {
                        _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_AVS"));
                    }
                }
                // }
                // Employeur/Conjoint
                /******* MISE A JOUR DE VALEURS */
                // Si au genre 8, l'ID entré est un ID de partenaire
                // Sinon, c'est l'ID d'affilié de l'employeur
                if (CIEcriture.CS_CIGENRE_8.equals(getGenreEcriture())
                        && JadeStringUtil.isIntegerEmpty(getPartenaireId())) {
                    if (CIUtil.unFormatAVS(getEmployeurPartenaire()).equals(getAvs())) {
                        _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_CONJ_IDEN"));
                    } else {
                        setPartenaireNumAvs(CIUtil.unFormatAVS(getEmployeurPartenaire()));
                    }
                } else if (!JadeStringUtil.isBlank(getEmployeurPartenaire())) {
                    setIdAffilie(getEmployeurPartenaire());
                }
                // Récupération du journal
                if ("6".equals(getAijApgAmi()) || "7".equals(getAijApgAmi()) || "5".equals(getAijApgAmi()) || "8".equals(getAijApgAmi())) {
                    CIJournal journal = getJournalEcrituresSpeciales(statement.getTransaction());
                    setIdJournal(journal.getIdJournal());
                } else {
                    getJournal(statement.getTransaction(), false);
                }
                // Le journal a été trouvé
                if (!statement.getTransaction().hasErrors()) {
                    // Genre ecriture = 1/6/7 si le type d'inscriptions est
                    // DECLARATION SALAIRE, ...
                    if (!journal.hasErrors()) {
                        if ((CIJournal.CS_DECLARATION_SALAIRES.equals(journal.getIdTypeInscription()))
                                || (CIJournal.CS_DECLARATION_COMPLEMENTAIRE.equals(journal.getIdTypeInscription()))
                                || (CIJournal.CS_CONTROLE_EMPLOYEUR.equals(journal.getIdTypeInscription()))
                                || (CIJournal.CS_ASSURANCE_MILITAIRE.equals(journal.getIdTypeInscription()))
                                || (CIJournal.CS_ASSURANCE_CHOMAGE.equals(journal.getIdTypeInscription()))
                                || (CIJournal.CS_APG.equals(journal.getIdTypeInscription()))
                                || (CIJournal.CS_PANDEMIE.equals(journal.getIdTypeInscription()))
                                || (CIJournal.CS_IJAI.equals(journal.getIdTypeInscription()))) {
                            if (!CIEcriture.CS_CIGENRE_1.equals(getGenreEcriture())
                                    && !CIEcriture.CS_CIGENRE_6.equals(getGenreEcriture())
                                    && !CIEcriture.CS_CIGENRE_7.equals(getGenreEcriture())) {
                                _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_GENRE"));
                            }
                        } else // 8 si SPLITTING
                        if (CIJournal.CS_SPLITTING.equals(journal.getIdTypeInscription())
                                && (!CIEcriture.CS_CIGENRE_8.equals(getGenreEcriture()))) {
                            _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_GENRE"));
                        } else // 0 si ASSURANCE FACULTATIVE ou si partBta
                               // existe
                        if (CIJournal.CS_ASSURANCE_FACULTATIVE.equals(journal.getIdTypeInscription())
                                || (!JadeStringUtil.isIntegerEmpty(getPartBta()))) {
                            if (!CIEcriture.CS_CIGENRE_0.equals(getGenreEcriture())
                                    && !CIEcriture.CS_CIGENRE_7.equals(getGenreEcriture())) {
                                _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_GENRE"));
                            }
                        }
                    }
                    // Caisse chomage
                    // Obligatoire si le type du journal est ASSURANCE_CHOMAGE
                    if (CIJournal.CS_ASSURANCE_CHOMAGE.equals(journal.getIdTypeInscription())
                            || (journal.getIdTypeInscription().equals(CIJournal.CS_INSCRIPTIONS_JOURNALIERES) && !JadeStringUtil
                                    .isIntegerEmpty(getCaisseChomage()))) {
                        _propertyMandatory(statement.getTransaction(), getCaisseChomage(),
                                getSession().getLabel("MSG_ECRITURE_CAISCHO"));
                        setIdAffilie("");
                        // Le numéro de la caisse comporte 3 chiffres ou plus
                        if (getCaisseChomage().length() < 3) {
                            _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_CAISCHO_FORMAT"));
                        }
                    } else if ((!CIJournal.CS_CORRECTIF.equals(journal.getIdTypeInscription()))
                            && (!JadeStringUtil.isIntegerEmpty(getCaisseChomage()))) {
                        if ((!journal.getIdTypeInscription().equals(CIJournal.CS_INSCRIPTIONS_JOURNALIERES))
                                && (!JadeStringUtil.isIntegerEmpty(getCaisseChomage()))) {
                            _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_CAICHO_NOT"));
                        }
                    }

                    if ((CIJournal.CS_APG.equals(journal.getIdTypeInscription()))
                            || (CIJournal.CS_PANDEMIE.equals(journal.getIdTypeInscription()))
                            || (CIJournal.CS_IJAI.equals(journal.getIdTypeInscription()))
                            || (CIJournal.CS_ASSURANCE_MILITAIRE.equals(journal.getIdTypeInscription()))
                            || (CIJournal.CS_ASSURANCE_CHOMAGE.equals(journal.getIdTypeInscription()))
                            || (!JadeStringUtil.isIntegerEmpty(getPartBta()))
                            || (CIEcriture.CS_CIGENRE_5.equals(getGenreEcriture()))) {
                        if (!JadeStringUtil.isIntegerEmpty(getEmployeur())) {
                            _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_EMPCONJ_NOT"));
                        }
                    } else if (CIEcriture.CS_CIGENRE_8.equals(getGenreEcriture())) {
                        // En genre splitting le partenaire doit être renseigné
                        if (JadeStringUtil.isIntegerEmpty(getPartenaireNumAvs())
                                && JadeStringUtil.isIntegerEmpty(getPartenaireId())) {
                            _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_CONJ"));
                        } else {
                            // Vérifier que le partenaire est bien au registre
                            // des assures
                            CICompteIndividuelManager ciPartMng = new CICompteIndividuelManager();
                            ciPartMng.setSession(getSession());
                            ciPartMng.orderByAvs(false);
                            ciPartMng.setForNumeroAvs(getPartenaireNumAvs());
                            ciPartMng.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
                            ciPartMng.find(statement.getTransaction());
                            if (ciPartMng.getSize() == 0) {
                                _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_PARTSPL"));
                            } else {
                                /**************************************************************/
                                // ATTENTION: ON STOCKE L'ID DU PARTENAIRE ET
                                // SON NOM, PRENOM
                                setPartenaireId(((CICompteIndividuel) ciPartMng.getEntity(0)).getCompteIndividuelId());
                                setPartenaireNomPrenom(((CICompteIndividuel) ciPartMng.getEntity(0)).getNomPrenom());
                            }
                        }
                    } else if ((JadeStringUtil.isBlank(getIdAffilie()))
                            && (JadeStringUtil.isIntegerEmpty(getEmployeur()))
                            && (JadeStringUtil.isIntegerEmpty(getCaisseChomage()))) {
                        // Aucun numéro d'affilié n'a été spécifié par
                        // l'utilisateur et il n'y a pas d'employeur par défaut
                        // (valable pour le add uniquement)
                        _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_EMP"));
                    } else if (!JadeStringUtil.isBlank(getIdAffilie())) {
                        // A appeler seulement en mode ajout
                        // On vérifie qu'il existe un affilié
                        CIApplication app = (CIApplication) getSession().getApplication();

                        aff = app.getAffilieByNo(getSession(), getIdAffilie(), forAffilieParitaire,
                                forAffiliePersonnel, getMoisDebut(), getMoisFin(), getAnnee(), getJourDebut(),
                                getJourFin());
                        if ((aff == null) || (aff.isNew())) {
                            _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_AFFILIE"));
                            return;
                        } else {
                            /****************************************************************************/
                            // ATTENTION: ON STOCKE L'ID DU TIERS, SON NOM,
                            // PRENOM ou SA RAISON SOCIALE
                            setNomTiers(aff.getTiers().getDesignation1());
                            setPrenomTiers(aff.getTiers().getDesignation2());
                            setEmployeur(aff.getAffiliationId());
                        }
                    } else if (!JadeStringUtil.isIntegerEmpty(getEmployeur())) {
                        // On récupère les infos de l'employeur (on ne passe que
                        // dans le cas d'utilisation de la valeur par défaut)
                        // On vérifie qu 'il existe un affilié
                        CIApplication app = (CIApplication) getSession().getApplication();

                        aff = app.getAffilie(statement.getTransaction(), getEmployeur(), null);

                        if ((aff == null) || (aff.isNew())) {
                            _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_AFFILIE"));
                        } else {
                            /****************************************************************************/
                            // ATTENTION: ON STOCKE L'ID DU TIERS, SON NOM,
                            // PRENOM ou SA RAISON SOCIALE
                            setIdAffilie(aff.getAffilieNumero());
                            setNomTiers(aff.getTiers().getDesignation1());
                            setPrenomTiers(aff.getTiers().getDesignation2());
                        }
                    }

                    // si l'affilié existe, faire des tests dessus
                    if ((aff != null) && (!aff.isNew())) {

                        // plus nécessaire, le test n'est fait qu'en mode ajout
                        // Branche économique: obligatoire pour genre 1, 2, 3,
                        // 4, 6, 7, 9 avant 1969
                        // Sinon doit être nul
                        if ((annee < 1969)
                                && ((CIEcriture.CS_CIGENRE_1.equals(getGenreEcriture()))
                                        || (CIEcriture.CS_CIGENRE_2.equals(getGenreEcriture()))
                                        || (CIEcriture.CS_CIGENRE_3.equals(getGenreEcriture()))
                                        || (CIEcriture.CS_CIGENRE_4.equals(getGenreEcriture()))
                                        || (CIEcriture.CS_CIGENRE_6.equals(getGenreEcriture()))
                                        || (CIEcriture.CS_CIGENRE_7.equals(getGenreEcriture())) || (CIEcriture.CS_CIGENRE_9
                                            .equals(getGenreEcriture())))) {
                            if (JadeStringUtil.isIntegerEmpty(getBrancheEconomique())) {
                                // essai depuis affilié
                                setBrancheEconomique(aff.getBrancheEconomique());
                                if (JadeStringUtil.isIntegerEmpty(getBrancheEconomique())) {
                                    _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_BRECO"));
                                }
                            }
                        }

                    }
                }
                if (JadeStringUtil.isIntegerEmpty(getMoisDebut())
                        && !CIEcriture.CS_CIGENRE_8.equals(getGenreEcriture()) && (annee >= 1979)
                        && JadeStringUtil.isIntegerEmpty(getPartBta())
                        && JadeStringUtil.isIntegerEmpty(getBrancheEconomique())) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_DATEDEB_NOT"));
                }
                if (JadeStringUtil.isIntegerEmpty(getMoisFin()) && !CIEcriture.CS_CIGENRE_8.equals(getGenreEcriture())
                        && (annee >= 1979) && JadeStringUtil.isIntegerEmpty(getPartBta())
                        && JadeStringUtil.isIntegerEmpty(getBrancheEconomique())) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_DATEFIN_NOT"));
                }

                if (!this.rechercheCI(statement.getTransaction())) {
                    if (statement.getTransaction().hasErrors() && !needNewDateCompta) {
                        idTypeCompte = CIEcriture.CS_CI_SUSPENS;
                        idTypeComptePrecedant = CIEcriture.CS_CI_SUSPENS;
                        return;
                    }
                    return;
                }
                // Age
                // La personne doit avoir 16/18 par rapport à l'année de
                // cotisation sauf pour le genre splitting
                if (!hasAge() && !CIEcriture.CS_CIGENRE_8.equals(getGenreEcriture())) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_AGE"));
                }
                // si mois 99, contrôle les autres écritures
                if ((moisDebut == 99) || (moisFin == 99)) {
                    if (!getWrapperUtil().rechercheEcritureSemblable(statement.getTransaction())) {
                        // écriture pour la même année et le même employeur non
                        // trouvée
                        _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_99"));
                    }
                }
                if ((!JadeStringUtil.isIntegerEmpty(getIdAffilie()))
                        && (!JadeStringUtil.isIntegerEmpty(getCaisseChomage()))) {
                    _addError(statement.getTransaction(),
                            "L'affilié ne doit pas être renseigné s'il d'agit d'une déclataration de chômage");
                    setIdAffilie("");
                }

                // montant
                double mnt = Double.parseDouble(getMontant());
                if ((mnt != 0) && (mnt < 1)) {
                    // montant inférieur à 1 fr
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_MONINF"));
                }

                int anneeInt;
                anneeInt = new Integer(getAnnee().trim()).intValue();

                if (anneeInt < 1979) {
                    if ((this.moisDebut.equals("00") && JadeStringUtil.isBlank(this.moisFin))
                            || (this.moisDebut.equals("00") && !this.moisFin.equals("00"))
                            || (!this.moisDebut.equals("00") && JadeStringUtil.isBlank(this.moisFin))
                            || (JadeStringUtil.isBlank(this.moisDebut) && !this.moisFin.equals("00"))) {
                        _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_PERIODE_FAUSSE"));
                    }

                }
                if (!"0".equals(getEmployeur())) {
                    // test du total avec les écriture de l'année et du même
                    // employeur

                    // s'il existe une ecriture provisoire pour le même
                    // employeur et la même année
                    if (!JadeStringUtil.isBlank(getEmployeur())) {
                        if (existeEcritureProvisoireSemblable(statement.getTransaction())
                                && !CIEcriture.CS_CODE_PROVISOIRE.equals(getCode())) {
                            _addError(statement.getTransaction(),
                                    getSession().getLabel("MSG_ECRITURE_PROVISOIRE_EXISTANTE"));
                        }
                        if (CIJournal.CS_DECLARATION_SALAIRES.equals(getJournal(statement.getTransaction(), true)
                                .getIdTypeInscription())
                                || CIJournal.CS_DECLARATION_COMPLEMENTAIRE.equals(getJournal(
                                        statement.getTransaction(), true).getIdTypeInscription())
                                || CIJournal.CS_CONTROLE_EMPLOYEUR.equals(getJournal(statement.getTransaction(), true)
                                        .getIdTypeInscription())) {
                            if (existeEcritureSemblableMontan(statement.getTransaction())) {
                                _addError(statement.getTransaction(),
                                        getSession().getLabel("MSG_ECRITURE_SEMBLABLE_EXISTANTE"));
                            }
                        }
                    }
                }
                if (!JadeStringUtil.isIntegerEmpty(employeur)) {
                    // Pour les genre 1 et 7 code 3, on regarde par rapport à
                    // l'affiliation, pour le reste le total de l'année
                    if (CIEcriture.CS_CIGENRE_1.equals(getGenreEcriture())
                            || (CIEcriture.CS_CIGENRE_7.equals(getGenreEcriture()) && CIEcriture.CS_NONFORMATTEUR_SALARIE
                                    .equals(getCodeSpecial()))) {

                        if ((!JadeStringUtil.isIntegerEmpty(getExtourne()))
                                && (CIEcriture.CS_EXTOURNE_1.equals(getExtourne())
                                        || CIEcriture.CS_EXTOURNE_8.equals(getExtourne()) || CIEcriture.CS_EXTOURNE_9
                                            .equals(getExtourne()))) {
                            if (!getWrapperUtil().rechercheEcritureEmp(statement.getTransaction())) {
                                // montant total pour cet employeur et cette
                                // année négatif!
                                _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_SUMEMP"));
                            }
                        }
                    } else {
                        if ((!JadeStringUtil.isIntegerEmpty(getExtourne()))
                                && (CIEcriture.CS_EXTOURNE_1.equals(getExtourne())
                                        || CIEcriture.CS_EXTOURNE_8.equals(getExtourne()) || CIEcriture.CS_EXTOURNE_9
                                            .equals(getExtourne()))) {
                            if (!getWrapperUtil().rechercheEcritureEmpTotAnnee(statement.getTransaction())) {
                                // montant total pour cet employeur et cette
                                // année négatif!
                                _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_SUMEMP"));
                            }
                        }
                    }
                } else if (CIJournal.CS_APG.equals(journal.getIdTypeInscription())
                        || CIJournal.CS_PANDEMIE.equals(journal.getIdTypeInscription())
                        || CIJournal.CS_ASSURANCE_MILITAIRE.equals(journal.getIdTypeInscription())
                        || CIJournal.CS_IJAI.equals(journal.getIdTypeInscription())) {
                    if ((!JadeStringUtil.isIntegerEmpty(getExtourne()))
                            && (CIEcriture.CS_EXTOURNE_1.equals(getExtourne())
                                    || CIEcriture.CS_EXTOURNE_8.equals(getExtourne()) || CIEcriture.CS_EXTOURNE_9
                                        .equals(getExtourne()))) {
                        if (!getWrapperUtil().rechercheEcritureEmpApg(statement.getTransaction(),
                                journal.getIdTypeInscription(), false, false)) {
                            // montant total pour cet employeur et cette année
                            // négatif!
                            _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_SUMEMP"));
                        }
                    }
                }
                // test du total avec les écritures de la même année pour les
                // écritures de type caisse chomage
                else if (!JadeStringUtil.isIntegerEmpty(caisseChomage)) {
                    if ((!JadeStringUtil.isIntegerEmpty(getExtourne()))
                            && (CIEcriture.CS_EXTOURNE_1.equals(getExtourne())
                                    || CIEcriture.CS_EXTOURNE_8.equals(getExtourne()) || CIEcriture.CS_EXTOURNE_9
                                        .equals(getExtourne()))) {
                        if (!getWrapperUtil().rechercheEcritureEmpApg(statement.getTransaction(),
                                journal.getIdTypeInscription(), true, false)) {
                            // montant total pour cet employeur et cette année
                            // négatif!
                            _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_SUMEMP"));
                        }
                    }

                }
            } // simpleProcess
            if (!statement.getTransaction().hasErrors()) {
                if (CIUtil.isRetraite(getAvs(), annee, avsNNSS, getSession())) {
                    // retraité pour cette écriture
                    if (!CIEcriture.CS_CIGENRE_7.equals(getGenreEcriture())) {
                        if (JadeStringUtil.isBlankOrZero(partBta)) {
                            // doit être genre 7
                            setGenreEcriture(CIEcriture.CS_CIGENRE_7);
                        }
                    }
                } else {
                    // Modif version 5.3, accepter les gre 7 si NNSS et CI
                    // inconnu

                    // si pas retraite, genre 7 seuelement si clôture 71 ou
                    // 81
                    if (CIEcriture.CS_CIGENRE_7.equals(getGenreEcriture()) && !isGenreSeptPossible(annee, getAvs())) {
                        // pas possible d'être un genre 7
                        if ((!"true".equals(avsNNSS) && (NSUtil.unFormatAVS(getAvs()).trim().length() != 13))
                                || (CICompteIndividuel.CS_REGISTRE_ASSURES.equals(getCI(statement.getTransaction(),
                                        false).getRegistre()))) {
                            if (!getAvs().startsWith("000000")) {
                                _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_GENRE7"));
                            }
                        }
                    } else {
                        if (!CIEcriture.CS_CIGENRE_8.equals(getGenreEcriture())) {
                            /*
                             * if(!JAUtil.isStringEmpty(getAvs())){ setGenreSeptSiCloture(); }
                             */
                        }
                    }

                }

                // code spécial
                if (CIEcriture.CS_CIGENRE_7.equals(getGenreEcriture()) && (annee >= 1997)) {
                    if (JadeStringUtil.isIntegerEmpty(getCodeSpecial())) {
                        if (aff != null) {
                            determineCodeSpecial(aff);
                        } else {
                            CIJournal journal = getJournal(statement.getTransaction(), true);
                            if (journal != null) {
                                if (CIJournal.CS_APG.equals(journal.getIdTypeInscription())
                                        || CIJournal.CS_PANDEMIE.equals(journal.getIdTypeInscription())
                                        || CIJournal.CS_IJAI.equals(journal.getIdTypeInscription())
                                        || !JadeStringUtil.isBlank(caisseChomage)) {
                                    setCodeSpecial(CIEcriture.CS_NONFORMATTEUR_SALARIE);
                                }
                            }
                        }
                    }
                } else if (!CIEcriture.CS_CIGENRE_4.equals(getGenreEcriture())
                        && !CIEcriture.CS_CIGENRE_0.equals(getGenreEcriture())
                        && !JadeStringUtil.isIntegerEmpty(getCodeSpecial())) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_CODSPE7_NOT"));
                }
            }
        } // mode extourne
          // mise à jour de l'état

        checkInscriptionAuCIEstPossible(statement.getTransaction());

        updateInscription(statement.getTransaction());

        forceCodeSpecial04IfNeeded();

    }

    private void forceCodeSpecial04IfNeeded() {

        try {

            if (CIEcriture.CS_CIGENRE_7.equalsIgnoreCase(getGenreEcriture())
                    && !JadeStringUtil.isBlankOrZero(getEmployeur())) {

                String dateInscription = new BSpy(getEspionSaisie()).getDate();

                AFAffiliation affiliation = new AFAffiliation();
                affiliation.setSession(getSession());
                affiliation.setAffiliationId(getEmployeur());
                affiliation.retrieve();

                if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), DATE_CODE_SPECIAL04_MANDATORY,
                        dateInscription)
                        && globaz.naos.translation.CodeSystem.TYPE_AFFILI_NON_ACTIF.equalsIgnoreCase(affiliation
                                .getTypeAffiliation())) {
                    setCodeSpecial(CIEcriture.CS_NONFORMATTEUR_NONACTIF);

                } else if (globaz.naos.translation.CodeSystem.TYPE_AFFILI_NON_ACTIF.equalsIgnoreCase(affiliation
                        .getTypeAffiliation())) {
                    setCodeSpecial("");

                } else {
                    determineCodeSpecial(affiliation);
                }

            }

        } catch (Exception e) {
            // Nothing to do, le code spécial n'est pas forcé
        }

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(_getCollection() + _getTableName() + ".KBIECR",
                this._dbWriteNumeric(statement.getTransaction(), getEcritureId(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("KBIECR", this._dbWriteNumeric(statement.getTransaction(), getEcritureId(), "ecritureId"));
        statement.writeField("KAIIND",
                this._dbWriteNumeric(statement.getTransaction(), getCompteIndividuelId(), "compteIndividuelId"));
        statement.writeField("KKIRAO", this._dbWriteNumeric(statement.getTransaction(), getRassemblementOuvertureId(),
                "rassemblementOuvertureId"));
        statement.writeField("KCID", this._dbWriteNumeric(statement.getTransaction(), getIdJournal(), "idJournal"));
        statement.writeField("KIIREM", this._dbWriteNumeric(statement.getTransaction(), getIdRemarque(), "idRemarque"));
        statement.writeField("KBIERE",
                this._dbWriteNumeric(statement.getTransaction(), getEcritureReference(), "ecritureReference"));
        statement.writeField("KBITIE", this._dbWriteNumeric(statement.getTransaction(), getEmployeur(), "employeur"));
        statement.writeField("KBIPAR",
                this._dbWriteNumeric(statement.getTransaction(), getPartenaireId(), "partenaireId"));
        statement.writeField("KBTEXT", this._dbWriteNumeric(statement.getTransaction(), getExtourne(), "extourne"));
        statement.writeField("KBTGEN",
                this._dbWriteNumeric(statement.getTransaction(), getGenreEcriture(), "genreEcriture"));
        statement.writeField("KBTPAR",
                this._dbWriteNumeric(statement.getTransaction(), getParticulier(), "particulier"));
        statement.writeField("KBNBTA", this._dbWriteNumeric(statement.getTransaction(), getPartBta(), "partBta"));
        statement.writeField("KBTSPE",
                this._dbWriteNumeric(statement.getTransaction(), getCodeSpecial(), "codeSpecial"));
        statement.writeField("KBNMOD", this._dbWriteNumeric(statement.getTransaction(), getMoisDebut(), "moisDebut"));
        statement.writeField("KBNMOF", this._dbWriteNumeric(statement.getTransaction(), getMoisFin(), "moisFin"));
        statement.writeField("KBNANN", this._dbWriteNumeric(statement.getTransaction(), getAnnee(), "annee"));
        statement.writeField("KBMMON", this._dbWriteNumeric(statement.getTransaction(), getMontant(), "montant"));
        statement.writeField("KBTCOD", this._dbWriteNumeric(statement.getTransaction(), getCode(), "code"));
        statement.writeField("KBTBRA",
                this._dbWriteNumeric(statement.getTransaction(), getBrancheEconomique(), "brancheEconomique"));
        statement.writeField("KBTCPT",
                this._dbWriteNumeric(statement.getTransaction(), getIdTypeCompte(), "idTypeCompte"));
        statement.writeField("KBDCEN",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateAnnonceCentrale(), "dateAnnonceCentrale"));
        statement.writeField("KBICHO",
                this._dbWriteNumeric(statement.getTransaction(), getCaisseChomage(), "caisseChomage"));
        statement.writeField("KBTCAT",
                this._dbWriteNumeric(statement.getTransaction(), getCategoriePersonnel(), "categoriePersonnel"));
        statement.writeField("KBDADD",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateCiAdditionnel(), "dateCiAdditionnel"));
        statement.writeField("KBIDLO", this._dbWriteNumeric(statement.getTransaction(), getIdLog(), "idLog"));
        statement.writeField("KBLESP",
                this._dbWriteString(statement.getTransaction(), getEspionSaisie(), "espionSaisie"));
        statement.writeField("KBLSUP",
                this._dbWriteString(statement.getTransaction(), getEspionSaisieSup(), "espionSaisieSup"));
        statement.writeField("KBCAIT",
                this._dbWriteNumeric(statement.getTransaction(), getCaisseTenantCI(), "caisseTenantCI"));
        statement.writeField("KBBIMP", this._dbWriteBoolean(statement.getTransaction(), isCertificat(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "certificat"));
        statement.writeField("KBBATT", this._dbWriteBoolean(statement.getTransaction(), getWantForDeclaration(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "declarationSuivante"));
        statement.writeField("KBLIB", this._dbWriteString(statement.getTransaction(), getLibelleAff(), "libelle"));
        statement.writeField("KBIAFF", this._dbWriteString(statement.getTransaction(), getAffHist(), "histAff"));
    }

    /**
     * Retourne l'écriture si celle-ci est à clôturer. Sinon <tt>null</tt>. Date de création : (23.12.2002 10:31:37)
     * 
     * @return l'écriture si celle-ci est à clôturer. Si la date de clôture se situe entre le mois de début et le mois
     *         de fin de l'écriture, cette dernière est éclatée et la parie à clôturer est retournée. Sinon
     *         <tt>null</tt>.
     * @param dateCloture
     *            la date de clôture
     * @param transaction
     *            la transaction à utiliser.
     * @exception si
     *                une erreur survient.
     */
    public CIEcriture aCloturer(BTransaction transaction, JADate dateCloture, boolean needUpdate) throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(getRassemblementOuvertureId())) {
            // écriture déjà clôturée
            return null;
        }
        String dateClotureAMJ = dateCloture.toStrAMJ();
        int codeCloture = Integer.parseInt(dateClotureAMJ.substring(0, dateClotureAMJ.length() - 2));
        int codeEcriture;
        if (isMoisSpeciaux()) {
            codeEcriture = Integer.parseInt(getAnnee() + "01");
        } else {
            codeEcriture = Integer.parseInt(getAnnee() + JadeStringUtil.rightJustifyInteger(getMoisDebut(), 2));
        }
        if (codeEcriture <= codeCloture) {
            // a cloturer
            int moisFinInt = 0;
            try {
                if (isMoisSpeciaux()) {
                    moisFinInt = 12;
                } else {
                    moisFinInt = Integer.parseInt(getMoisFin());
                }
            } catch (Exception ex) {
                // laisser à 0
            }
            int moisClotureInt = dateCloture.getMonth();
            if ((codeEcriture == codeCloture) || (moisFinInt > moisClotureInt)) {
                // eclatement nécessaire
                // return eclater(transaction, dateCloture);
                /*
                 * if(CS_TEMPORAIRE.equals(idTypeCompte)){ setIdTypeCompte(CS_TEMPORAIRE_SUSPENS); }
                 * if(CS_CI.equals(idTypeCompte)){ setIdTypeCompte(CS_CI_SUSPENS); }
                 */
                // setDateCiAdditionnel("0");
                // setRassemblementOuvertureId(getCI(transaction,false).getDerniereBCloture(transaction).getRassemblementOuvertureId());
                // return null;
                // _addError(transaction,getSession().getLabel("LABEL_MSG_IK_PERIODE_A_CHEVAL_FR"));
                // return null;
            }
            return this;
        }
        return null;
    }

    /**
     * Indique si l'écriture ou un partie de l'écriture est à clôturer.
     * 
     * @return true si lécriture est à clôturer.
     * @param dateCloture
     *            la date de clôture
     * @exception si
     *                une erreur survient.
     */
    public boolean aCloturer(JADate dateCloture) throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(getRassemblementOuvertureId())) {
            // écriture déjà clôturée
            return false;
        }
        String dateClotureAMJ = dateCloture.toStrAMJ();
        int codeCloture = Integer.parseInt(dateClotureAMJ.substring(0, dateClotureAMJ.length() - 2));
        int codeEcriture;
        if (isMoisSpeciaux()) {
            codeEcriture = Integer.parseInt(getAnnee() + "01");
        } else {
            codeEcriture = Integer.parseInt(getAnnee() + JadeStringUtil.rightJustifyInteger(getMoisDebut(), 2));
        }
        int codeEcritureFin;
        if (isMoisSpeciaux()) {
            codeEcritureFin = Integer.parseInt(getAnnee() + "12");
        } else {
            codeEcritureFin = Integer.parseInt(getAnnee() + JadeStringUtil.rightJustifyInteger(getMoisFin(), 2));
        }
        if ((codeEcriture <= codeCloture) && (codeEcritureFin <= codeCloture)) {
            return true;
        }
        return false;
    }

    /**
     * Indique si l'écriture ou un partie de l'écriture est après la clôture.
     * 
     * @return true si lécriture est à clôturer.
     * @param dateCloture
     *            la date de clôture
     * @exception si
     *                une erreur survient.
     */
    public boolean apresCloture(JADate dateCloture) throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(getRassemblementOuvertureId())) {
            // écriture déjà clôturée
            return false;
        }
        String dateClotureAMJ = dateCloture.toStrAMJ();
        int codeCloture = Integer.parseInt(dateClotureAMJ.substring(0, dateClotureAMJ.length() - 2));
        int codeEcriture;
        if (isMoisSpeciaux()) {
            codeEcriture = Integer.parseInt(getAnnee() + "01");
        } else {
            codeEcriture = Integer.parseInt(getAnnee() + JadeStringUtil.rightJustifyInteger(getMoisFin(), 2));
        }
        if (codeEcriture > codeCloture) {
            return true;
        }
        return false;
    }

    /**
     * Méthode permettant de masquer le montant dans le viewbean (ecriture_de.jsp) si nécessaire
     * 
     * @param modeExtourne
     */
    public void cacherMontantSiProtege(boolean modeExtourne) {
        BTransaction transactionEcriture = null;

        try {
            String userLevelSecurity = "";
            // recherche du complément "secureCode" du user (niveau de sécutité)
            BSession session = getSession();
            FWSecureUserDetail user = new FWSecureUserDetail();
            user.setSession(session);
            user.setUser(session.getUserId());
            user.setLabel(CIEcriture.SECURE_CODE);
            user.retrieve();
            if (!JadeStringUtil.isEmpty(user.getData())) {
                userLevelSecurity = user.getData();
            } else {
                // si le complément n'existe pas on lui met un niveau à 0 (aucun
                // droit)
                userLevelSecurity = "0";
            }

            // si ecriture de genre 18
            if (getGenreEcriture().equals(CIEcriture.CS_CIGENRE_8)
                    && (!getExtourne().equals("0") && !getExtourne().equals(""))) {
                transactionEcriture = new BTransaction(getSession());
                transactionEcriture.openTransaction();
                BPreparedStatement psChercheEcritureACacher = new BPreparedStatement(transactionEcriture);

                // attention en mode extourne (option -> extourner
                // l'inscription) si l'écriture est de genre 18 c'est que
                // l'écriture que l'on extourne est en genre 8 => on vérifie
                // donc les droit sur une écriture 8
                if (!modeExtourne) {
                    psChercheEcritureACacher.prepareStatement(CIEcritureManager.getSqlChercheEcritureACacher("KAIIND"));
                } else {
                    psChercheEcritureACacher.prepareStatement(CIEcritureManager
                            .getSqlChercheEcritureGenre8ACacher("KAIIND"));
                }

                psChercheEcritureACacher.clearParameters();
                psChercheEcritureACacher.setBigDecimal(1, new BigDecimal(getCompteIndividuelId()));
                psChercheEcritureACacher
                        .setBigDecimal(2, new BigDecimal(CIEcriture.NIV_SECURITY_X + userLevelSecurity));
                ResultSet result = psChercheEcritureACacher.executeQuery();
                if (result.next()) {
                    setCacherMontant(true);
                }
            }
            // si écriture de genre 8
            else if (getGenreEcriture().equals(CIEcriture.CS_CIGENRE_8)
                    && (getExtourne().equals("0") || getExtourne().equals(""))) {
                transactionEcriture = new BTransaction(getSession());
                transactionEcriture.openTransaction();
                BPreparedStatement psChercheEcritureGenre8ACacher = new BPreparedStatement(transactionEcriture);

                // attention en mode extourne si l'écriture est de genre 8 c'est
                // que l'écriture que l'on extourne est en genre 18 => on
                // vérifie donc les droit sur une écriture 18
                if (!modeExtourne) {
                    psChercheEcritureGenre8ACacher.prepareStatement(CIEcritureManager
                            .getSqlChercheEcritureGenre8ACacher("KAIIND"));
                } else {
                    psChercheEcritureGenre8ACacher.prepareStatement(CIEcritureManager
                            .getSqlChercheEcritureACacher("KAIIND"));
                }

                psChercheEcritureGenre8ACacher.clearParameters();
                psChercheEcritureGenre8ACacher.setBigDecimal(1, new BigDecimal(getCompteIndividuelId()));
                psChercheEcritureGenre8ACacher.setBigDecimal(2, new BigDecimal(CIEcriture.NIV_SECURITY_X
                        + userLevelSecurity));
                ResultSet resultGenre8 = psChercheEcritureGenre8ACacher.executeQuery();
                if (resultGenre8.next()) {
                    setCacherMontant(true);
                }
            } else {
                AFAffiliation affiliation;
                try {
                    // récupération de l'affilié
                    affiliation = getAffiliationEcriture(this);
                    // on vérifie le niveau de sécurité
                    if (!affiliation.hasRightAccesSecurity()) {
                        // on cache le montant
                        setCacherMontant(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (transactionEcriture != null) {
                try {
                    transactionEcriture.closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean canEclateIncription() {
        if (CIEcriture.CS_TEMPORAIRE.equals(getIdTypeCompte())
                || CIEcriture.CS_TEMPORAIRE_SUSPENS.equals(getIdTypeCompte())
                || CIEcriture.CS_CORRECTION.equals(getIdTypeCompte())
                || CIEcriture.CS_CI_SUSPENS.equals(getIdTypeCompte())
                || CIEcriture.CS_CI_SUSPENS_SUPPRIMES.equals(getIdTypeCompte())) {
            getSession().addError(getSession().getLabel("PLA_FONCTIONNALITE_IMP"));
            return false;
        }
        return true;
    }

    private void checkInscriptionAuCIEstPossible(BTransaction transaction) throws Exception {

        CICompteIndividuel theCI = new CICompteIndividuel();
        theCI.setSession(getSession());
        theCI.setCompteIndividuelId(getCompteIndividuelId());
        theCI.retrieve();

        if (theCI.getInvalide()) {
            _addError(transaction, FWMessageFormat.format(
                    getSession().getLabel("CI_ECRITURE_ERREUR_AJOUT_IMPOSSIBLE_CI_INVALIDE"),
                    NSUtil.formatAVSUnknown(theCI.getNumeroAvs())));
        }

        if (theCI.getInactive()) {
            CIEcritureManager ecritureManager = new CIEcritureManager();
            ecritureManager.setSession(getSession());
            ecritureManager.setForCompteIndividuelId(theCI.getCompteIndividuelId());
            ecritureManager.setForAnnee(getAnnee());

            if (ecritureManager.getCount() <= 0) {
                _addError(transaction, FWMessageFormat.format(
                        getSession().getLabel(
                                "CI_ECRITURE_ERREUR_AJOUT_IMPOSSIBLE_CI_INACTIF_ET_PAS_DEJA_INSCRIPTION_FOR_ANNEE"),
                        NSUtil.formatAVSUnknown(theCI.getNumeroAvs()), getAnnee()));
            }

        }
    }

    /**
     * Renvoie le code système asssocié à un code utilisateur et un groupe.
     * 
     * @param Un
     *            object BTransaction.
     * @param Le
     *            code utilisateur.
     * @param Le
     *            groupe.
     * @return Le code système asssocié à un code utilisateur et un groupe
     */
    public String codeUtilisateurToCodeSysteme(BTransaction transaction, String code, String groupe) throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(code.trim())) {
            try {
                int valeurCode = new Integer(code).intValue();
                // En dessous de 300000, c'est une code utilisateur (pour PAVO
                // en tout cas)
                if (valeurCode < 300000) {
                    throw new Exception();
                }
                // code est déjà un code système
                return code;
            } catch (Exception e) {
                // C'est un code utilisateur. Il faut obtenir le code système
                FWParametersSystemCodeManager systemCodeMng = new FWParametersSystemCodeManager();
                systemCodeMng.setSession(getSession());
                systemCodeMng.setForIdGroupe(groupe);
                systemCodeMng.setForCodeUtilisateur(code);
                systemCodeMng.find(transaction);
                if (!systemCodeMng.hasErrors()) {
                    if (systemCodeMng.getSize() > 0) {
                        return ((FWParametersSystemCode) systemCodeMng.getEntity(0)).getIdCode();
                    } else {
                        // Pas de code système pour le code utilisateur
                        return "";
                    }
                } else {
                    _addError(transaction, getSession().getLabel("MSG_ECRITURE_USER_CODE"));
                    return "";
                }
            }
        } else {
            return "";
        }
    }

    /**
     * Renvoie le code système asssocié à un code utilisateur et un groupe. Pour l'ecriture
     * 
     * @param Un
     *            object BTransaction.
     * @param Le
     *            code utilisateur.
     * @param Le
     *            groupe.
     * @return Le code système asssocié à un code utilisateur et un groupe
     */
    public String codeUtilisateurToCodeSystemeGre(BTransaction transaction, String code, String groupe)
            throws Exception {
        if (!JadeStringUtil.isBlank(code.trim())) {
            try {
                int valeurCode = new Integer(code).intValue();
                // En dessous de 300000, c'est une code utilisateur (pour PAVO
                // en tout cas)
                if (valeurCode < 300000) {
                    throw new Exception();
                }
                // code est déjà un code système
                return code;
            } catch (Exception e) {
                // C'est un code utilisateur. Il faut obtenir le code système
                FWParametersSystemCodeManager systemCodeMng = new FWParametersSystemCodeManager();
                systemCodeMng.setSession(getSession());
                systemCodeMng.setForIdGroupe(groupe);
                systemCodeMng.setForCodeUtilisateur(code);
                systemCodeMng.find(transaction);
                if (!systemCodeMng.hasErrors()) {
                    if (systemCodeMng.getSize() > 0) {
                        return ((FWParametersSystemCode) systemCodeMng.getEntity(0)).getIdCode();
                    } else {
                        // Pas de code système pour le code utilisateur
                        return "";
                    }
                } else {
                    _addError(transaction, getSession().getLabel("MSG_ECRITURE_USER_CODE"));
                    return "";
                }
            }
        } else {
            return "";
        }
    }

    /**
     * Comptabilise l'inscription. Date de création : (12.11.2002 13:52:43)
     */
    public void comptabiliser() throws Exception {
        BTransaction transaction = null;
        try {

            transaction = new BTransaction(getSession());
            transaction.openTransaction();

            this.comptabiliser(transaction, true, true);
            CIJournal journal = getJournal(transaction, false);
            if (CIJournal.CS_OUVERT.equals(journal.getIdEtat())) {
                // mis à jour état journal
                journal.retrieve();
                journal.setIdEtat(CIJournal.CS_PARTIEL);
                journal.update(transaction);
            }
            if (transaction.isRollbackOnly()) {
                transaction.rollback();
            } else {
                transaction.commit();
            }
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }

    }

    /**
     * Comptabilise l'inscription. Date de création : (12.11.2002 13:52:43)
     * 
     * @param testCIAdditionnel
     *            indique si un éventuel CI additionel doit être envoyé.
     */
    public void comptabiliser(BITransaction transactionIfc, boolean testCIAdditionnel, boolean needUpdate)
            throws Exception {
        this.comptabiliser(transactionIfc, null, testCIAdditionnel, needUpdate);
    }

    /**
     * Comptabilise l'inscription. Date de création : (12.11.2002 13:52:43)
     * 
     * @param testCIAdditionnel
     *            indique si un éventuel CI additionel doit être envoyé.
     */
    public void comptabiliser(BITransaction transactionIfc, String dateCompta, boolean testCIAdditionnel,
            boolean testSplitting) throws Exception {
        BTransaction transaction = null;
        boolean createTransaction = false;
        if (testSplitting) {
            idTypeCompteAvantTentativeSplitting = idTypeComptePrecedant;
        }
        if (transactionIfc instanceof BTransaction) {
            transaction = (BTransaction) transactionIfc;
        } else {
            throw new Exception("Transaction incorrecte");
        }
        try {
            // gestion de la transaction pour l'utilisation de l'API
            if (transaction.getConnection() == null) {
                transaction.openTransaction();
                createTransaction = true;
            }
            // Si la cotisation est GENRE 6, l'idTypeCompte devient GENRE_6
            String registre = getCI(transaction, false).getRegistre();
            if (CICompteIndividuel.CS_REGISTRE_GENRES_6.equals(registre)) {
                setIdTypeCompte(CIEcriture.CS_GENRE_6);
            } else if (CICompteIndividuel.CS_REGISTRE_GENRES_7.equals(registre)) {
                setIdTypeCompte(CIEcriture.CS_GENRE_7);
            } else {
                // Sinon le type de compte devient CI ou suspens
                if (CIEcriture.CS_TEMPORAIRE_SUSPENS.equals(idTypeComptePrecedant)) {
                    setIdTypeCompte(CIEcriture.CS_CI_SUSPENS);
                }
                if (CIEcriture.CS_TEMPORAIRE.equals(idTypeComptePrecedant)) {
                    setIdTypeCompte(CIEcriture.CS_CI);
                }
            }
            // mise à jour ancien type compte pour éviter plusieurs
            // comptabilisations
            idTypeComptePrecedant = idTypeCompte;

            // mise à jour de la date de l'inscription
            if (JAUtil.isDateEmpty(dateCompta)) {
                dateCompta = JACalendar.today().toStrAMJ();
            }
            String time = new JATime(JACalendar.now()).toString();
            BSpy spy = new BSpy(dateCompta + time + getSession().getUserId());
            // On ne le fait pas lors s'une Zuordnung
            if (needNewDateCompta && !isFromFacturationCompta) {
                setEspionSaisie(spy.getFullData());

            }

            // Effectuer la mise à jour si nécessaire
            if (!updating) {
                try {
                    wantCallMethodBefore(false);
                    wantCallMethodAfter(false);
                    wantCallValidate(false);
                    this.save(transaction);
                    wantCallMethodBefore(true);
                    wantCallMethodAfter(true);
                    wantCallValidate(true);
                } catch (Exception e) {
                    _addError(transaction, getSession().getLabel("MSG_ECRITURE_COMPT_UPDATE") + getEcritureId() + "/"
                            + getCompteIndividuelId());
                }
            }
            // ci additionnel si nécessaire
            if (testCIAdditionnel) {
                getCI(transaction, false).annonceCIAdditionnel(transaction, this);
            }

            // test les période de splitting si demandé et si non clôturé
            if (testSplitting) {
                getWrapperUtil().checkPeriode(transaction);
                // si erreur lors du splitting
                if (transaction.hasErrors()) {
                    idTypeComptePrecedant = idTypeCompteAvantTentativeSplitting;
                }

            }

            // test si réouverture (écriture éventuellement éclatée à ce stade)
            // attention, ne pas prendre en compte les écritures en suspens qui
            // n'ont pas de CI au RA
            if (CIEcriture.CS_CI_SUSPENS.equals(idTypeCompte)
                    && CICompteIndividuel.CS_REGISTRE_ASSURES.equals(getCI(transaction, false).getRegistre())) {
                getWrapperUtil().reouvreCI(transaction);
            }
            if (createTransaction) {
                if (transaction.isRollbackOnly()) {
                    transaction.rollback();
                } else {
                    transaction.commit();
                }
            }

        } catch (Exception ex) {
            if (createTransaction) {
                transaction.rollback();
            }
            throw ex;
        } finally {
            if (createTransaction) {
                transaction.closeTransaction();
            }
        }
    }

    /**
     * Affiche une copie de cette écriture. Date de création : (14.11.2002 09:53:16)
     */
    public void copie() throws Exception {

        CIEcriture ecr = new CIEcriture();
        ecr.setSession(getSession());
        ecr.setId(getId());
        ecr.retrieve();
        if (CIEcriture.CS_TEMPORAIRE.equals(ecr.getIdTypeCompte())
                || CIEcriture.CS_TEMPORAIRE_SUSPENS.equals(ecr.getIdTypeCompte())
                || CIEcriture.CS_CI_SUSPENS.equals(ecr.getIdTypeCompte())
                || CIEcriture.CS_CI_SUSPENS_SUPPRIMES.equals(ecr.getIdTypeCompte())
                || CIEcriture.CS_CORRECTION.equals(ecr.getIdTypeCompte())) {
            this.retrieve();
            getSession().addError(getSession().getLabel("PLA_FONCTIONNALITE_IMP"));
            return;
        }
        setId("");
        setEcritureId("");
        setCI(ecr.getCI(null, false));
        setCompteIndividuelId(ecr.getCompteIndividuelId());
        if (CIJournal.CS_ASSURANCE_MILITAIRE.equals(ecr.getJournal(null, false).getIdTypeInscription())) {
            setAijApgAmi("6");

        }
        if (CIJournal.CS_APG.equals(ecr.getJournal(null, false).getIdTypeInscription())) {
            setAijApgAmi("7");
        }
        if (CIJournal.CS_PANDEMIE.equals(ecr.getJournal(null, false).getIdTypeInscription())) {
            setAijApgAmi("5");
        }
        if (CIJournal.CS_IJAI.equals(ecr.getJournal(null, false).getIdTypeInscription())) {
            setAijApgAmi("8");

        }
        if (!JadeStringUtil.isBlank(ecr.getCaisseChomage()) && !"0".equals(ecr.getCaisseChomage())) {
            setAijApgAmi("3");
        }
        if (!"0".equals(ecr.getPartenaireId())) {
            setAijApgAmi("2");
        }
        if (!"0".equals(ecr.getPartBta())) {
            setAijApgAmi("4");
        }
        if (!"6".equals(getAijApgAmi()) && !"7".equals(getAijApgAmi()) && !"5".equals(getAijApgAmi()) && !"8".equals(getAijApgAmi())) {
            journal = getJournalduJour(null);
        } else {
            journal = getJournalEcrituresSpeciales(null);
        }
        setIdJournal(journal.getIdJournal());
        setEmployeur(ecr.getEmployeur());
        setIdAffilie(ecr.getIdAffilie());
        setNomTiers(ecr.getNomTiers());
        setPrenomTiers(ecr.getPrenomTiers());
        if (!JadeStringUtil.isIntegerEmpty(ecr.getPartenaireId())) {
            setPartenaireId(ecr.getPartenaireId());
            setPartenaireNumAvs(ecr.getPartenaireNumAvs());
            setPartenaireNomPrenom(ecr.getPartenaireNomPrenom());
            setMitgliedNumNNSS(ecr.getMitgliedNumNNSS());
        }
        setCaisseChomage(ecr.getCaisseChomage());
        setExtourne(ecr.getExtourne());
        setGenreEcriture(ecr.getGenreEcriture());
        setParticulier(ecr.getParticulier());
        setMoisDebut(ecr.getMoisDebut());
        setMoisFin(ecr.getMoisFin());
        setJourDebut(ecr.getJourDebut());
        setJourFin(ecr.getJourFin());
        setAnnee(ecr.getAnnee());
        setMontant(ecr.getMontant());
        setCode(ecr.getCode());
        setPartBta(ecr.getPartBta());
        setCodeSpecial(ecr.getCodeSpecial());
        setBrancheEconomique(ecr.getBrancheEconomique());
        setCategoriePersonnel(ecr.getCategoriePersonnel());
        setRemarque(ecr.getRemarque());
        setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
        idTypeComptePrecedant = "";
    }

    public void copie(boolean wantCreateJournal) throws Exception {
        CIEcriture ecr = new CIEcriture();
        ecr.setSession(getSession());
        ecr.setId(getId());
        ecr.retrieve();
        if (CIEcriture.CS_CI_SUSPENS_SUPPRIMES.equals(ecr.getIdTypeCompte())
                || CIEcriture.CS_CORRECTION.equals(ecr.getIdTypeCompte())) {
            this.retrieve();
            getSession().addError(getSession().getLabel("PLA_FONCTIONNALITE_IMP"));
            return;
        }
        setId("");
        setEcritureId("");
        setCI(ecr.getCI(null, false));
        setCompteIndividuelId(ecr.getCompteIndividuelId());
        setIdJournal(journal.getIdJournal());
        setEmployeur(ecr.getEmployeur());
        setIdAffilie(ecr.getIdAffilie());
        setNomTiers(ecr.getNomTiers());
        setPrenomTiers(ecr.getPrenomTiers());
        if (!JadeStringUtil.isIntegerEmpty(ecr.getPartenaireId())) {
            setPartenaireId(ecr.getPartenaireId());
            setPartenaireNumAvs(ecr.getPartenaireNumAvs());
            setPartenaireNomPrenom(ecr.getPartenaireNomPrenom());
        }
        setCaisseChomage(ecr.getCaisseChomage());
        setExtourne(ecr.getExtourne());
        setGenreEcriture(ecr.getGenreEcriture());
        setParticulier(ecr.getParticulier());
        setMoisDebut(ecr.getMoisDebut());
        setMoisFin(ecr.getMoisFin());
        setJourDebut(ecr.getJourDebut());
        setJourFin(ecr.getJourFin());
        setAnnee(ecr.getAnnee());
        setMontant(ecr.getMontant());
        setCode(ecr.getCode());
        setPartBta(ecr.getPartBta());
        setCodeSpecial(ecr.getCodeSpecial());
        setBrancheEconomique(ecr.getBrancheEconomique());
        setCategoriePersonnel(ecr.getCategoriePersonnel());
        setRemarque(ecr.getRemarque());
        setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
        idTypeComptePrecedant = "";
    }

    public void determineCodeSpecial(AFAffiliation aff) {
        // Att: les variables statiques du type d'affiliation n'existent pas
        // a mettre à jour
        if (aff != null) {
            String genreAff = aff.getTypeAffiliation();
            if (globaz.naos.translation.CodeSystem.TYPE_AFFILI_INDEP.equals(genreAff)
                    || globaz.naos.translation.CodeSystem.TYPE_AFFILI_TSE.equals(genreAff)) {
                // indépendant
                setCodeSpecial(CIEcriture.CS_NONFORMATTEUR_INDEPENDANT);
            } else if (globaz.naos.translation.CodeSystem.TYPE_AFFILI_EMPLOY.equals(genreAff)
                    || globaz.naos.translation.CodeSystem.TYPE_AFFILI_LTN.equals(genreAff)) {
                // employeur + BZ 8587 ajout du type LTN lors de la comparaison
                setCodeSpecial(CIEcriture.CS_NONFORMATTEUR_SALARIE);
            } else if ("804005".equals(genreAff)) {
                // employeur/indépendant
                // test du no avs
                if (CIUtil.unFormatAVS(getAvs()).equals(CIUtil.unFormatAVS(aff.getTiers().getNumAvsActuel()))) {
                    // indépendant
                    setCodeSpecial(CIEcriture.CS_NONFORMATTEUR_INDEPENDANT);
                } else {
                    setCodeSpecial(CIEcriture.CS_NONFORMATTEUR_SALARIE);
                }
            }
        }
    }

    /**
     * Eclate l'écriture en cas de clôture pendant la période celle-ci.
     * 
     * @param transaction
     *            la transaction à utiliser
     * @param dateCloture
     *            la date de clôture de l'annonce 22 (<tt>MMAA</tt>)
     * @return la permier partie de l'écriture éclatée, la seconde étant l'instance actuelle.
     * @exception si
     *                une erreur survient Date de création : (19.12.2002 13:45:24)
     */
    public CIEcriture eclater(BTransaction transaction, JADate dateCloture) throws Exception {
        if (isMoisSpeciaux()) {
            // pas d'éclatement si 66,77 ou 99
            return this;
        }
        int anneeInt = Integer.parseInt(annee);
        if (dateCloture.getYear() != anneeInt) {
            return this;
        }
        int moisFinInt = Integer.parseInt(moisFin);
        if (dateCloture.getMonth() >= moisFinInt) {
            return this;
        }
        int moisDebutInt = Integer.parseInt(moisDebut);
        if ((moisDebutInt > 12) || (moisDebutInt == 0)) {
            throw new Exception();
        }
        int moisTotal = (Integer.parseInt(moisFin) - moisDebutInt) + 1;
        int moisCloture = (dateCloture.getMonth() - moisDebutInt) + 1;
        long montantDebut = (long) Math.ceil((Double.parseDouble(montant) / moisTotal) * moisCloture);
        double montantFin = Double.parseDouble(montant) - montantDebut;
        CIEcriture copieFin = new CIEcriture();
        copieFin.setSession(getSession());
        copyDataToEntity(copieFin);
        copieFin.setEcritureId("");
        copieFin.setEcritureReference(getEcritureId());
        copieFin.setIdTypeCompte(CIEcriture.CS_CORRECTION);

        copieFin.simpleAdd(transaction);
        CIEcriture ecritureDebut = new CIEcriture();
        ecritureDebut.setSession(getSession());
        copyDataToEntity(ecritureDebut);
        ecritureDebut.setEcritureId("");
        ecritureDebut.setMontant(String.valueOf(montantDebut));
        ecritureDebut.setMoisFin(String.valueOf(dateCloture.getMonth()));
        if (CIEcriture.CS_CI_SUSPENS.equals(ecritureDebut.getIdTypeCompte())) {
            ecritureDebut.setIdTypeCompte(CIEcriture.CS_CI);
        }

        ecritureDebut.simpleAdd(transaction);

        setMontant(String.valueOf(montantFin));
        setMoisDebut(String.valueOf(dateCloture.getMonth() + 1));
        if (!isUpdating()) {
            this.update(transaction);
        }
        return ecritureDebut;
    }

    public void eclater(String dateCloture, String debutmois1, String debutmois2, String moisFin1, String moisFin2,
            String montant1, String montant2) throws Exception {
        BTransaction transaction = null;
        try {
            transaction = new BTransaction(getSession());
            transaction.openTransaction();

            if (!JadeStringUtil.isBlank(dateCloture)) {
                if (7 == dateCloture.length()) {
                    if ('.' != dateCloture.charAt(2)) {
                        _addError(transaction, getSession().getLabel("ECL_DATE_CLOTURE_VIDE"));
                    }
                } else if (6 == dateCloture.length()) {
                    if ('.' != dateCloture.charAt(1)) {
                        _addError(transaction, getSession().getLabel("ECL_DATE_CLOTURE_VIDE"));
                    }
                }

            } else {
                _addError(transaction, getSession().getLabel("ECL_DATE_CLOTURE_VIDE"));
            }
            if (CIEcriture.CS_TEMPORAIRE.equals(getIdTypeCompte())
                    || CIEcriture.CS_TEMPORAIRE_SUSPENS.equals(getIdTypeCompte())
                    || CIEcriture.CS_CI_SUSPENS_SUPPRIMES.equals(getIdTypeCompte())) {
                _addError(transaction, "error");
                return;
            }
            String moisCloture = dateCloture.substring(0, dateCloture.indexOf("."));

            if (Integer.parseInt(moisFin1) > Integer.parseInt(moisCloture)) {
                _addError(transaction, getSession().getLabel("ECL_MOIS_FIN_GRAND"));
            }
            if (Integer.parseInt(debutmois2) <= Integer.parseInt(moisCloture)) {
                _addError(transaction, getSession().getLabel("ECL_MOIS_DEBUT_PETIT"));
            }
            FWCurrency montantAComparer = new FWCurrency(montant1, 2);
            // montantAComparer.setScale(2);
            montantAComparer.add(new FWCurrency(montant2, 2));

            if (!montantAComparer.equals(new FWCurrency(getMontant()))) {
                _addError(transaction, getSession().getLabel("ECL_MONTANT_PAS_EGAL"));

            }

            // On historise l'ecriture de base
            CIEcriture copieFin = new CIEcriture();
            copieFin.setSession(getSession());
            copyDataToEntity(copieFin);
            copieFin.setEcritureId("");
            copieFin.setEcritureReference(getEcritureId());
            copieFin.setIdTypeCompte(CIEcriture.CS_CORRECTION);
            copieFin.simpleAdd(transaction);
            // On Update l'ecriture avant CI, On conserve l'id pour l'ecriture
            // avant cloture
            setMontant(montant1);
            setMoisDebut(debutmois1);
            setMoisFin(moisFin1);
            this.update(transaction);
            this.copie();
            setIdTypeCompte(CIEcriture.CS_CI);
            setMontant(montant2);
            setMoisFin(moisFin2);
            setMoisDebut(debutmois2);
            this.add(transaction);
            if (transaction.isRollbackOnly()) {
                transaction.rollback();
            } else {
                transaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private boolean existeEcritureProvisoireSemblable(BTransaction transaction) {
        CIEcritureManager mgr = new CIEcritureManager();
        mgr.setSession(getSession());
        mgr.setForCompteIndividuelId(getCompteIndividuelId());
        mgr.setForEmployeur(getEmployeur());
        mgr.setForCode(CIEcriture.CS_CODE_PROVISOIRE);
        mgr.setForAnnee(getAnnee());
        mgr.setForNotId(getEcritureId());
        mgr.setForEmployeur(getEmployeur());
        try {
            mgr.find(transaction);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (mgr.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean existeEcritureSemblableMontan(BTransaction transaction) {
        // Modif 1-5-7-1 => transaction en erreur, mgr avec une entité vide et
        // outofbondsException
        if (transaction.hasErrors()) {
            return false;
        }
        CIEcritureManager mgr = new CIEcritureManager();
        mgr.setSession(getSession());
        mgr.setForGenre(getGenreEcriture());
        mgr.setForMoisDebut(getMoisDebut());
        mgr.setForMoisFin(getMoisFin());
        if (JadeStringUtil.isBlank(getExtourne())) {
            mgr.setForNotExtourne(CIEcriture.CS_EXTOURNE_1);
        } else {
            mgr.setForExtourne(getExtourne());
        }
        mgr.setForCompteIndividuelId(getCompteIndividuelId());
        mgr.setForEmployeur(getEmployeur());
        mgr.setForNotId(getEcritureId());
        mgr.setForAnnee(getAnnee());
        // Modif 1.13 jmc, la catégorie de personnel doit aussi être différente
        mgr.setForCategoriePersonnel(categoriePersonnel);
        try {
            mgr.find(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mgr.size() == 0) {
            return false;
        } else {
            for (Iterator<?> iter = mgr.iterator(); iter.hasNext();) {
                CIEcriture ecriture = (CIEcriture) iter.next();

                if (getMontant().substring(0, getMontant().length() - 3).equals(
                        ecriture.getMontant().substring(0, ecriture.getMontant().length() - 3))) {
                    return true;
                }
            }
            return false;
        }

    }

    /**
     * Ajoute une extourne de cette écriture au ci. Date de création : (14.11.2002 09:53:16)
     */
    public void extourne() throws Exception {
        CIEcriture ecr = new CIEcriture();
        ecr.setSession(getSession());
        ecr.setId(getId());
        ecr.retrieve();
        if (CIEcriture.CS_TEMPORAIRE.equals(ecr.getIdTypeCompte())
                || CIEcriture.CS_TEMPORAIRE_SUSPENS.equals(ecr.getIdTypeCompte())
                || CIEcriture.CS_CI_SUSPENS.equals(ecr.getIdTypeCompte())
                || CIEcriture.CS_CI_SUSPENS_SUPPRIMES.equals(ecr.getIdTypeCompte())
                || CIEcriture.CS_CORRECTION.equals(ecr.getIdTypeCompte())) {
            this.retrieve();
            getSession().addError(getSession().getLabel("PLA_FONCTIONNALITE_IMP"));
            return;
        }
        this.copie();
        if (JadeStringUtil.isIntegerEmpty(getExtourne()) || CIEcriture.CS_EXTOURNE_2.equals(getExtourne())
                || CIEcriture.CS_EXTOURNE_6.equals(getExtourne()) || CIEcriture.CS_EXTOURNE_8.equals(getExtourne())) {
            setExtourne(CIEcriture.CS_EXTOURNE_1);
        } else {
            setExtourne("");
        }
    }

    /**
     * Permet d'extourner sans créer de new journal
     * 
     * @param wantCreateJournal
     * @throws Exception
     */
    public void extourne(boolean wantCreateJournal) throws Exception {
        CIEcriture ecr = new CIEcriture();
        ecr.setSession(getSession());
        ecr.setId(getId());
        ecr.retrieve();
        if (CIEcriture.CS_CI_SUSPENS_SUPPRIMES.equals(ecr.getIdTypeCompte())
                || CIEcriture.CS_CORRECTION.equals(ecr.getIdTypeCompte())) {
            this.retrieve();
            getSession().addError(getSession().getLabel("PLA_FONCTIONNALITE_IMP"));
            return;
        }
        this.copie(false);

        if (JadeStringUtil.isIntegerEmpty(getExtourne()) || CIEcriture.CS_EXTOURNE_2.equals(getExtourne())
                || CIEcriture.CS_EXTOURNE_6.equals(getExtourne()) || CIEcriture.CS_EXTOURNE_8.equals(getExtourne())) {
            setExtourne(CIEcriture.CS_EXTOURNE_1);
        } else {
            setExtourne("");
        }

    }

    public String getAffHist() {
        return affHist;
    }

    /**
     * @param ecriture
     * @return l'affiliation de l'écriture (employeur ou partenaire si de genre 8)
     * @throws Exception
     */
    private AFAffiliation getAffiliationEcriture(CIEcriture ecriture) throws Exception {
        AFAffiliation affiliation = new AFAffiliation();
        affiliation.setSession(getSession());
        if (ecriture.getGenreEcriture().equals(CIEcriture.CS_CIGENRE_8)) {
            affiliation.setAffiliationId(ecriture.getPartenaireId());
        } else {
            affiliation.setAffiliationId(ecriture.getEmployeur());
        }
        affiliation.retrieve();

        return affiliation;
    }

    /**
     * Returns the aijApgAmi.
     * 
     * @return String
     */
    public String getAijApgAmi() {
        return aijApgAmi;
    }

    public String getAnnee() {
        return annee;
    }

    public java.lang.String getAvs() {
        return getCI(null, false).getNumeroAvs();
    }

    /**
     * @return
     */
    public String getAvsNNSS() {
        return avsNNSS;
    }

    public String getAvsNNSSDetailEcriture() {
        return getCI(null, true).getNumeroAvsNNSS();
    }

    public String getBrancheEconomique() {
        return brancheEconomique;
    }

    public boolean getCacherMontant() {
        return cacherMontant;
    }

    public String getCaisseChomage() {
        return caisseChomage;
    }

    /**
     * Retourne le numéro d'affilié pour la caisse de chômage. Date de création : (18.12.2002 16:32:32)
     * 
     * @return java.lang.String
     * @param caisse
     *            le numéro de la caisse
     */
    public String getCaisseChomageFormattee() {
        return "999999" + JadeStringUtil.rightJustifyInteger(getCaisseChomage(), 5);
    }

    /*
     * Retourne une chaîne vide si la valuer du numéro de caisse est 0.
     */
    public String getCaisseChomageStr() {
        if ("0".equals(caisseChomage)) {
            return "";
        } else {
            return caisseChomage;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.04.2003 13:01:08)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCaisseTenantCI() {
        return caisseTenantCI;
    }

    /**
     * @return
     */
    public String getCantonAF() {
        return cantonAF;
    }

    public String getCategoriePersonnel() {
        return categoriePersonnel;
    }

    /**
     * Retourne le ci de l'assuré
     * 
     * @param transaction
     *            la transaction à utiliser
     * @param synchronise
     *            vrai pour forcer un retrieve si pas déjà effectué
     * @return le journal correspondant
     * @exception si
     *                une erreur survient
     */
    public CICompteIndividuel getCI(BTransaction transaction, boolean synchronise) {
        if ((ci == null) || (synchronise && (ci.getEtatEntity() == CICompteIndividuel.FROM_JOIN))) {
            ci = new CICompteIndividuel();
            ci.setSession(getSession());
            if (!JadeStringUtil.isIntegerEmpty(getCompteIndividuelId())) {
                ci.setCompteIndividuelId(getCompteIndividuelId());
                try {
                    ci.retrieve(transaction);
                } catch (Exception ex) {
                    // laisser tel quel
                }
            }
        }
        return ci;
    }

    public String getCode() {
        return code;
    }

    public String getCodeSpecial() {
        return codeSpecial;
    }

    public String getCompteIndividuelId() {
        return compteIndividuelId;
    }

    public String getDateAnnonceCentrale() {
        return dateAnnonceCentrale;
    }

    public String getDateCiAdditionnel() {
        return dateCiAdditionnel;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.04.2003 08:48:28)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateCloture() {
        if (!JAUtil.isDateEmpty(dateCloture)) {
            return dateCloture.substring(3);
        }
        return dateCloture;
    }

    public java.lang.String getDateDeNaissance() {
        return getCI(null, false).getDateNaissance();
    }

    /**
     * Retourne la date d'inscription en fonction le l'espion saisie. Date de création : (10.01.2003 11:55:57)
     * 
     * @return java.lang.String
     */
    public String getDateInscription() {
        String result = "";
        BSpy spy = new BSpy(getEspionSaisie());
        if (!JAUtil.isDateEmpty(spy.getDate())) {
            result = spy.getDate();
        }
        if (!"0".equals(getRassemblementOuvertureId())) {
            if ((result != null) && (result.trim().length() > 0)) {
                result += "/" + getDateCloture() + " (" + motifCloture + ")";
            } else {
                result = getDateCloture() + " (" + motifCloture + ")";
            }
        }
        return result;
    }

    public String getDateNaissance() {
        return getCI(null, true).getDateNaissance();

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.04.2003 08:48:02)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateOrdre() {
        return dateOrdre;
    }

    public String getDateTransmission() {
        if (JAUtil.isDateEmpty(dateCiAdditionnel)) {
            return dateOrdre;
        }
        return dateCiAdditionnel;
    }

    public String getDefautltNNSSDisplay() {
        try {
            return GlobazSystem.getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).getProperty(
                    "nsstag.defaultdisplay.newnss");
        } catch (Exception e) {
            return "false";

        }
    }

    public java.lang.String getDernieresEcritures() {
        if (!isNew()) {
            return getCI(null, false).getDernieresEcritures();
        } else {
            return "";
        }
    }

    public java.lang.String getDernieresEcrituresTxt(String delim) {
        if (!isNew()) {
            return getCI(null, false).getDernieresEcrituresTxt(delim);
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getEcranInscriptionsSuspens() {
        return ecranInscriptionsSuspens;
    }

    public String getEcritureId() {
        return ecritureId;
    }

    public String getEcritureReference() {
        return ecritureReference;
    }

    public String getEmployeur() {
        return employeur;
    }

    public String getEmployeurPartenaire() {
        return employeurPartenaire;
    }

    /**
     * Retourne une référence sur l'employeur ou le numéro AVS du partenaire selon le genre de l'écriture.
     * 
     * @return une référence sur l'employeur ou le numéro AVS du partenaire selon le genre de l'écriture.
     */
    public String getEmployeurPartenaireForDisplay() {
        if (CIEcriture.CS_CIGENRE_8.equals(getGenreEcriture())) {
            if (!JadeStringUtil.isIntegerEmpty(getPartenaireNumAvs())) {
                return NSUtil.formatAVSUnknown(getPartenaireNumAvs());
            }
        } else {
            return this.getNoNomEmployeur().substring(0, this.getNoNomEmployeur().indexOf(" "));
        }
        return "";
    }

    public String getEspionSaisie() {
        return espionSaisie;
    }

    /**
     * @return
     */
    public String getEspionSaisieSup() {
        return espionSaisieSup;
    }

    /**
     * Returns the estRa.
     * 
     * @return String
     */
    public String getEstRa() {
        return estRa;
    }

    public String getEtatFormate() {
        return getCI(null, true).getEtatFormate();
    }

    public String getExtourne() {
        return extourne;
    }

    public CICompteIndividuel getForcedCi(BTransaction transaction) {
        CICompteIndividuelManager mgr = new CICompteIndividuelManager();
        mgr.setForNumeroAvs(ci.getNumeroAvs());
        mgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        mgr.setSession(getSession());
        try {
            mgr.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mgr.size() > 0) {
            return (CICompteIndividuel) mgr.getFirstEntity();
        } else {
            return null;
        }
    }

    public String getForcedNoNomEmployeur() {
        if ("".equals(employeurPartenaire) && JadeStringUtil.isIntegerEmpty(employeur)) {
            return "";
        }
        AFAffiliationManager afMgr = new AFAffiliationManager();
        if (!JadeStringUtil.isIntegerEmpty(employeur)) {
            afMgr.setForAffiliationId(employeur);
        } else {
            afMgr.setForAffilieNumero(employeurPartenaire);
        }
        afMgr.setSession(getSession());
        try {
            afMgr.find();
            if (afMgr.size() == 0) {
                return "";
            } else {
                TITiersViewBean ti = new TITiersViewBean();
                ti.setSession(getSession());
                ti.setIdTiers(((AFAffiliation) afMgr.getFirstEntity()).getIdTiers());
                ti.retrieve();
                return ti.getPrenomNom();
            }
        } catch (Exception e) {

        }
        return "";
    }

    /**
     * @return
     */
    public String getFromEcran() {
        return fromEcran;
    }

    public String getGenreEcriture() {
        return genreEcriture;
    }

    public String getGre() {
        return gre;
    }

    /**
     * Retourne le genre formaté sans le code particulier de splitting
     * 
     * @return
     */
    public String getGreForExtrait() {
        String greCourant = "";
        if (JadeStringUtil.isBlankOrZero(getExtourne())) {
            greCourant = "0";
        } else {
            greCourant += CodeSystem.getCodeUtilisateur(getExtourne(), getSession());
        }
        greCourant += CodeSystem.getCodeUtilisateur(getGenreEcriture(), getSession());
        return greCourant;
    }

    /**
     * Retourne le GRE formatté sur deux chiffres si le code particulier est à mandat normal.
     * 
     * @return Le GRE formatté sur deux chiffres si le code particulier est à mandat normal.
     */
    public String getGreFormat() {
        String greCourant = new String();
        // L'extourne n'a pas de code utilisateur 0...
        if (JadeStringUtil.isIntegerEmpty(getExtourne())) {
            greCourant = "0";
        } else {
            greCourant += CodeSystem.getCodeUtilisateur(getExtourne(), getSession());
        }
        greCourant += CodeSystem.getCodeUtilisateur(getGenreEcriture(), getSession());
        if (!CIEcriture.CS_MANDAT_NORMAL.equals(getParticulier())) {
            greCourant += CodeSystem.getCodeUtilisateur(getParticulier(), getSession());
        }
        return greCourant;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdLog() {
        return idLog;
    }

    public String getIdRemarque() {
        return idRemarque;
    }

    public String getIdTiers() {
        try {
            AFAffiliation affForTiers = new AFAffiliation();
            affForTiers.setSession(getSession());
            affForTiers.setAffiliationId(employeur);
            affForTiers.retrieve();
            if (!affForTiers.isNew()) {
                return affForTiers.getIdTiers();
            }
            return "";

        } catch (Exception e) {
            return "";
        }
    }

    public String getIdTypeCompte() {
        return idTypeCompte;
    }

    /*
     * Renvoie le nom de l'affilié
     */
    public String getInfoAffilie() {
        String infoAffilie = getNomTiers();
        if (!JadeStringUtil.isBlank(getPrenomTiers())) {
            infoAffilie += " " + getPrenomTiers();
        }
        return infoAffilie;
    }

    /**
     * Retourne les infos sur l'employeur (nom, prénom ou raison sociale) ou les nom, prénom du partenaire en cas de
     * genre 8
     * 
     * @return Les infos sur l'employeur (nom, prénom ou raison sociale) ou les nom, prénom du partenaire en cas de
     *         genre 8
     */
    public String getInfoEmployeurPartenaire() {
        if (CIEcriture.CS_CIGENRE_8.equals(getGenreEcriture())) {
            return getPartenaireNomPrenom();
        } else {
            return getInfoAffilie();
        }
    }

    /**
     * Retourne le journal correspondant
     * 
     * @param transaction
     *            la transaction à utiliser
     * @param synchronize
     *            vrai pour forcer un retrieve si pas déjà effectué
     * @return le journal correspondant
     * @exception si
     *                une erreur survient
     */
    public CIJournal getJournal(BTransaction transaction, boolean synchronize) {
        if ((journal == null) || (synchronize && (journal.getEtatEntity() == CIJournal.FROM_JOIN))) {
            try {
                // si écriture unique, créer/rechercher journal du jour
                if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
                    journal = getJournalduJour(transaction);
                    setIdJournal(journal.getIdJournal());
                } else {
                    journal = new CIJournal();
                    journal.setSession(getSession());
                    journal.setIdJournal(getIdJournal());
                    journal.retrieve(transaction);
                }
            } catch (Exception ex) { // laisser tel quel }
            }
        }
        return journal;
    }

    /**
     * Retourne ou créer le journal du jour pour une inscription unique journalière. Date de création : (28.02.2003
     * 07:49:18)
     * 
     * @param transaction
     *            la transaction à utiliser.
     * @return le journal du jour.
     */
    private CIJournal getJournalduJour(BTransaction transaction) throws Exception {
        // recherche si déjà existant
        CIJournalManager mgr = new CIJournalManager();
        mgr.setSession(getSession());
        mgr.setForIdTypeInscription(CIJournal.CS_INSCRIPTIONS_JOURNALIERES);
        mgr.setForDate(JACalendar.todayJJsMMsAAAA());
        mgr.find(transaction);
        if (mgr.size() != 0) {
            return (CIJournal) mgr.getEntity(0);
        }
        // journal non trouvé -> en créer un
        CIJournal jour = new CIJournal();
        jour.setSession(getSession());
        jour.setIdTypeInscription(CIJournal.CS_INSCRIPTIONS_JOURNALIERES);
        jour.setIdTypeCompte(CIJournal.CS_CI);
        jour.setLibelle(getSession().getLabel("MSG_JOURNAL_LIBELLE_JOURN") + " " + JACalendar.todayJJsMMsAAAA());
        jour.setDateInscription(JACalendar.todayJJsMMsAAAA());
        jour.setProprietaire("ssii");
        jour.add(transaction);
        return jour;

    }

    private CIJournal getJournalEcrituresSpeciales(BTransaction transaction) throws Exception {
        if ("6".equals(getAijApgAmi())) {
            // recherche si déjà existant
            CIJournalManager mgr = new CIJournalManager();
            mgr.setSession(getSession());
            mgr.setForIdTypeInscription(CIJournal.CS_ASSURANCE_MILITAIRE);
            mgr.setForDate(JACalendar.todayJJsMMsAAAA());
            mgr.find(transaction);
            if (mgr.size() != 0) {
                setJournal((CIJournal) mgr.getEntity(0));
                return (CIJournal) mgr.getEntity(0);
            }
            // journal non trouvé -> en créer un
            CIJournal jour = new CIJournal();
            jour.setSession(getSession());
            jour.setIdTypeInscription(CIJournal.CS_ASSURANCE_MILITAIRE);
            jour.setIdTypeCompte(CIJournal.CS_CI);
            jour.setLibelle(getSession().getLabel("MSG_JOURNAL_AMI_JOURNALIER") + " " + JACalendar.todayJJsMMsAAAA());
            jour.setProprietaire("ssii");
            jour.setDateInscription(JACalendar.todayJJsMMsAAAA());
            jour.add(transaction);
            setJournal(jour);
            return jour;
        }
        if ("7".equals(getAijApgAmi())) {
            // recherche si déjà existant
            CIJournalManager mgr = new CIJournalManager();
            mgr.setSession(getSession());
            mgr.setForIdTypeInscription(CIJournal.CS_APG);
            mgr.setForDate(JACalendar.todayJJsMMsAAAA());
            mgr.find(transaction);
            if (mgr.size() != 0) {
                setJournal((CIJournal) mgr.getEntity(0));
                return (CIJournal) mgr.getEntity(0);
            }
            // journal non trouvé -> en créer un
            CIJournal jour = new CIJournal();
            jour.setSession(getSession());
            jour.setIdTypeInscription(CIJournal.CS_APG);
            jour.setIdTypeCompte(CIJournal.CS_CI);
            jour.setLibelle(getSession().getLabel("MSG_JOURNAL_APG_JOURNALIER") + " " + JACalendar.todayJJsMMsAAAA());
            jour.setProprietaire("ssii");
            jour.setDateInscription(JACalendar.todayJJsMMsAAAA());
            jour.add(transaction);
            setJournal(jour);
            return jour;
        }
        if ("5".equals(getAijApgAmi())) {
            // recherche si déjà existant
            CIJournalManager mgr = new CIJournalManager();
            mgr.setSession(getSession());
            mgr.setForIdTypeInscription(CIJournal.CS_PANDEMIE);
            mgr.setForDate(JACalendar.todayJJsMMsAAAA());
            mgr.find(transaction);
            if (mgr.size() != 0) {
                setJournal((CIJournal) mgr.getEntity(0));
                return (CIJournal) mgr.getEntity(0);
            }
            // journal non trouvé -> en créer un
            CIJournal jour = new CIJournal();
            jour.setSession(getSession());
            jour.setIdTypeInscription(CIJournal.CS_PANDEMIE);
            jour.setIdTypeCompte(CIJournal.CS_CI);
            jour.setLibelle(getSession().getLabel("MSG_JOURNAL_PANDEMIE_JOURNALIER") + " " + JACalendar.todayJJsMMsAAAA());
            jour.setProprietaire("ssii");
            jour.setDateInscription(JACalendar.todayJJsMMsAAAA());
            jour.add(transaction);
            setJournal(jour);
            return jour;
        }
        if ("8".equals(getAijApgAmi())) {
            // recherche si déjà existant
            CIJournalManager mgr = new CIJournalManager();
            mgr.setSession(getSession());
            mgr.setForIdTypeInscription(CIJournal.CS_IJAI);
            mgr.setForDate(JACalendar.todayJJsMMsAAAA());
            mgr.find(transaction);
            if (mgr.size() != 0) {
                setJournal((CIJournal) mgr.getEntity(0));
                return (CIJournal) mgr.getEntity(0);
            }
            // journal non trouvé -> en créer un
            CIJournal jour = new CIJournal();
            jour.setSession(getSession());
            jour.setIdTypeInscription(CIJournal.CS_IJAI);
            jour.setIdTypeCompte(CIJournal.CS_CI);
            jour.setLibelle(getSession().getLabel("MSG_JOURNAL_AIJ_JOURNALIER") + " " + JACalendar.todayJJsMMsAAAA());
            jour.setProprietaire("ssii");
            jour.setDateInscription(JACalendar.todayJJsMMsAAAA());
            jour.add(transaction);
            setJournal(jour);
            return jour;
        }

        return new CIJournal();
    }

    public String getJournalIdEtat() {
        CIJournal journal = getJournal(null, false);
        return journal.getIdEtat();
    }

    public String getLibelleAff() {
        return libelleAff;
    }

    public java.lang.String getMessages() {
        return messages;
    }

    public String getMitgliedNumNNSS() {
        return mitgliedNumNNSS;
    }

    /**
     * Returns the modeAjout.
     * 
     * @return String
     */
    public String getModeAjout() {
        return modeAjout;
    }

    public String getMoisDebut() {
        return moisDebut;
    }

    public String getMoisDebutPad() {
        return (getMoisDebut().length() == 1 ? "0" : "") + getMoisDebut();
    }

    public String getMoisDebutWithoutZero() {
        if (!JadeStringUtil.isIntegerEmpty(moisDebut)) {
            return moisDebut;
        } else {
            return "";
        }
    }

    public String getMoisFin() {
        return moisFin;
    }

    public String getMoisFinPad() {
        return (getMoisFin().length() == 1 ? "0" : "") + getMoisFin();
    }

    public String getMoisFinWithoutZero() {
        if (!JadeStringUtil.isIntegerEmpty(moisFin)) {
            return moisFin;
        } else {
            return "";
        }
    }

    public String getMontant() {
        return montant;
    }

    public String getMontantFormat() {
        if (hasShowRight(null) && !cacherMontant) {
            return new FWCurrency(getMontant()).toStringFormat();
        } else {
            return "(" + getSession().getLabel("MSG_ECRITURE_CACHE") + ")";
        }
    }

    /**
     * Retourne le montant signé de l'inscription.<br>
     * Le montant est positif si la propriété <tt>extourne</tt> est 0, 6 ou 8, et négative dans les autres cas. . Date
     * de création : (12.11.2002 13:46:16)
     * 
     * @return le montant signé.
     */
    public String getMontantSigne() {
        if (JadeStringUtil.isIntegerEmpty(getExtourne()) || CIEcriture.CS_EXTOURNE_2.equals(getExtourne())
                || CIEcriture.CS_EXTOURNE_6.equals(getExtourne()) || CIEcriture.CS_EXTOURNE_8.equals(getExtourne())) {
            return getMontantFormat();
        }
        return "-" + getMontantFormat();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.11.2002 09:21:41)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNomPrenom() {
        return getCI(null, false).getNomPrenom();
    }

    /**
     * @return
     */
    public String getNomPrenomAssure() {
        return nomPrenomAssure;
    }

    /**
     * @return
     */
    public String getNomSuppressor() {
        return nomSuppressor;
    }

    public String getNomTiers() {
        return nomTiers;
    }

    /**
     * Retourne le nom complet du nom du créateur de l'écriture. Date de création : (09.01.2003 15:31:09)
     * 
     * @return le nom complet du nom du créateur de l'écriture
     */
    public String getNomUserCreation() {
        return nomCreator;
    }

    /**
     * Retourne le nom complet du nom du créateur de l'écriture. Date de création : (09.01.2003 15:31:09)
     * 
     * @return le nom complet du nom du créateur de l'écriture
     */
    public String getNomUserModification() {
        return nomModifier;
    }

    /**
     * Génère le numéro et le nom de l'employeur concernant cette écriture. Date de création : (04.12.2002 08:36:39)
     * 
     * @return le no et le nom de l'employeur.
     */
    public String getNoNomEmployeur() {
        return this.getNoNomEmployeur(false);
    }

    public String getNoNomEmployeur(boolean wantLocalite) {
        // if (!isNew()) {
        // Modif CCVS, si les champs ne sont pas vide, on les remplace par cela
        if (!JadeStringUtil.isBlankOrZero(libelleAff) && !JadeStringUtil.isBlank(affHist)) {
            return affHist + " " + libelleAff;
        }
        if (!JadeStringUtil.isBlankOrZero(libelleAff) && JadeStringUtil.isBlank(affHist)) {
            return libelleAff;
        }
        if (!JadeStringUtil.isBlank(affHist) && JadeStringUtil.isBlankOrZero(libelleAff)) {
            return affHist;
        }

        if (!JadeStringUtil.isIntegerEmpty(getEmployeur())) {
            // recherche Tiers et Affiliation
            String nomHist = "";
            String prenomHist = "";
            String localite = "";
            String localiteHist = "";
            try {
                String dateSaisie = "31.12." + annee;
                CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                        CIApplication.DEFAULT_APPLICATION_PAVO);
                AFAffiliation aff = application.getAffilie(getSession(), getEmployeur(), null);
                TIHistoriqueTiersManager histMgr = new TIHistoriqueTiersManager();
                histMgr.setSession((BSession) application.getSessionTiers(getSession()));
                histMgr.setForIdTiers(aff.getIdTiers());
                histMgr.setForChamp(TITiers.FIELD_DESIGNATION1);
                histMgr.setForDateDebutLowerOrEqualTo(dateSaisie);
                histMgr.changeManagerSize(BManager.SIZE_NOLIMIT);
                if (!JadeStringUtil.isBlankOrZero(aff.getIdTiers())) {
                    histMgr.find();
                }
                if (histMgr.size() > 0) {
                    nomHist = ((TIHistoriqueTiers) histMgr.getEntity(histMgr.size() - 1)).getValeur();
                } else {
                    histMgr.setForDateDebutLowerOrEqualTo("");
                    if (!JadeStringUtil.isBlankOrZero(aff.getIdTiers())) {
                        histMgr.find();
                    }
                    if (histMgr.size() > 0) {
                        nomHist = ((TIHistoriqueTiers) histMgr.getFirstEntity()).getValeur();
                    }
                }
                histMgr.setForDateDebutLowerOrEqualTo(dateSaisie);
                histMgr.setForChamp(TITiers.FIELD_DESIGNATION2);
                if (!JadeStringUtil.isBlankOrZero(aff.getIdTiers())) {
                    histMgr.find();
                }
                if (histMgr.size() > 0) {
                    prenomHist = ((TIHistoriqueTiers) histMgr.getEntity(histMgr.size() - 1)).getValeur();
                } else {
                    histMgr.setForDateDebutLowerOrEqualTo("");
                    if (!JadeStringUtil.isBlankOrZero(aff.getIdTiers())) {
                        histMgr.find();
                    }
                    if (histMgr.size() > 0) {
                        prenomHist = ((TIHistoriqueTiers) histMgr.getFirstEntity()).getValeur();
                    }
                }

                if (wantLocalite) {
                    localiteHist = ","
                            + aff.getTiers().getAdresseAsString(null, IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                                    IConstantes.CS_APPLICATION_DEFAUT, dateSaisie, new TILocaliteLongFormater());
                }
                if ("".equals(prenomHist) && !JadeStringUtil.isBlankOrZero(nomHist)) {
                    return getIdAffilie() + " " + nomHist + localiteHist;
                } else if (!JadeStringUtil.isBlankOrZero(nomHist)) {
                    return getIdAffilie() + " " + nomHist + " " + prenomHist.trim() + localiteHist;
                }
            } catch (Exception e) {

            }
            if (wantLocalite) {
                try {
                    CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                            CIApplication.DEFAULT_APPLICATION_PAVO);
                    AFAffiliation aff = application.getAffilie(getSession(), getEmployeur(), null);
                    localite = ", " + aff.getTiers().getLocaliteLong();
                } catch (Exception ex) {
                    // reste vide
                }
            }

            if ("".equals(getPrenomTiers())) {
                return getIdAffilie() + " " + getNomTiers() + localite;
            } else {
                return getIdAffilie() + " " + getNomTiers() + " " + getPrenomTiers().trim() + localite;
            }

        }

        if (!JadeStringUtil.isIntegerEmpty(getPartenaireId())) {
            // recherche du nom du partenaire
            if (!JadeStringUtil.isBlank(getPartenaireNumAvs())) {
                return NSUtil.formatAVSUnknown(getPartenaireNumAvs()) + " " + getPartenaireNomPrenom();
            } else {
                try {
                    CICompteIndividuel ci = new CICompteIndividuel();
                    ci.setSession(getSession());
                    ci.setCompteIndividuelId(getPartenaireId());
                    if (isLoadedFromManager()) {
                        ci.setLoadedFromManager(true);
                    }
                    ci.retrieve();
                    if (!ci.isNew()) {
                        return NSUtil.formatAVSUnknown(ci.getNumeroAvs()) + " " + ci.getNomPrenom();
                    }
                } catch (Exception e) {
                    // erreur de db, on continue avec les autres test
                }
            }
        }
        if (!JadeStringUtil.isIntegerEmpty(getCaisseChomage())) {
            // caisse de chômage
            return JAUtil.formatAvs(getCaisseChomageFormattee()) + " " + getSession().getLabel("MSG_ECRITURE_NOM_AC");
        }
        if (!JadeStringUtil.isIntegerEmpty(getPartBta())) {
            // BTA
            return getSession().getLabel("MSG_ECRITURE_NOM_BTA");
        }
        // aucune info, recherche du journal
        CIJournal journal = getJournal(null, false);
        if (CIJournal.CS_APG.equals(journal.getIdTypeInscription())) {
            // APG
            return getSession().getLabel("MSG_ECRITURE_NOM_APG");
        }
        if (CIJournal.CS_PANDEMIE.equals(journal.getIdTypeInscription())) {
            // APG
            return getSession().getLabel("MSG_ECRITURE_NOM_PANDEMIE");
        }
        if (CIJournal.CS_IJAI.equals(journal.getIdTypeInscription())) {
            // AI
            return getSession().getLabel("MSG_ECRITURE_NOM_AI");
        }
        if (CIJournal.CS_ASSURANCE_MILITAIRE.equals(journal.getIdTypeInscription())) {
            // Militaire
            return getSession().getLabel("MSG_ECRITURE_NOM_MIL");
        }
        // }
        if (!JadeStringUtil.isBlank(employeurPartenaire)) {
            // après erreur d'ajout, reprendre l'affilié introduit
            return employeurPartenaire + " ";
        }
        return " ";
    }

    public String getNoNomEmployeurBis() {
        // if (!isNew()) {
        // Modif CCVS, si les champs ne sont pas vide, on les remplace par cela
        if (!JadeStringUtil.isBlankOrZero(libelleAff) && !JadeStringUtil.isBlank(affHist)) {
            return affHist;
        }
        if (!JadeStringUtil.isIntegerEmpty(getEmployeur())) {
            return getIdAffilie();
        }

        if (!JadeStringUtil.isIntegerEmpty(getPartenaireId())) {
            // recherche du nom du partenaire
            if (!JadeStringUtil.isBlank(getPartenaireNumAvs())) {
                return NSUtil.formatAVSUnknown(getPartenaireNumAvs()) + " " + getPartenaireNomPrenom();
            } else {
                try {
                    CICompteIndividuel ci = new CICompteIndividuel();
                    ci.setSession(getSession());
                    ci.setCompteIndividuelId(getPartenaireId());
                    if (isLoadedFromManager()) {
                        ci.setLoadedFromManager(true);
                    }
                    ci.retrieve();
                    if (!ci.isNew()) {
                        return NSUtil.formatAVSUnknown(ci.getNumeroAvs()) + " " + ci.getNomPrenom();
                    }
                } catch (Exception e) {
                    // erreur de db, on continue avec les autres test
                }
            }
        }
        if (!JadeStringUtil.isIntegerEmpty(getCaisseChomage())) {
            // caisse de chômage
            return JAUtil.formatAvs(getCaisseChomageFormattee());
        }
        // aucune info, recherche du journal
        CIJournal journal = getJournal(null, false);
        if (CIJournal.CS_APG.equals(journal.getIdTypeInscription())) {
            // APG
            return "77777777777";
        }
        if (CIJournal.CS_PANDEMIE.equals(journal.getIdTypeInscription())) {
            // APG
            return "55555555555";
        }
        if (CIJournal.CS_IJAI.equals(journal.getIdTypeInscription())) {
            // AI
            return "88888888888";
        }
        if (CIJournal.CS_ASSURANCE_MILITAIRE.equals(journal.getIdTypeInscription())) {
            // Militaire
            return "66666666666";
        }

        return " ";
    }

    public String getNssFormate() {
        return getCI(null, true).getNssFormate();
    }

    public String getNumeroAvsConjointWithoutPrefixe() {
        if (!JadeStringUtil.isBlank(employeurPartenaire)) {
            if ("true".equalsIgnoreCase(getMitgliedNumNNSS())) {
                return NSUtil.formatWithoutPrefixe(employeurPartenaire, true);
            } else if ("false".equalsIgnoreCase(getMitgliedNumNNSS())) {
                return NSUtil.formatWithoutPrefixe(employeurPartenaire, false);
            }
        } else {
            if ("true".equalsIgnoreCase(getMitgliedNumNNSS())) {
                return NSUtil.formatWithoutPrefixe(partenaireNumAvs, true);
            } else if ("false".equalsIgnoreCase(getMitgliedNumNNSS())) {
                return NSUtil.formatWithoutPrefixe(partenaireNumAvs, false);
            }
            return partenaireNumAvs;

        }
        return partenaireNumAvs;
    }

    /**
     * @return
     */
    public String getNumeroavsNNSS() {
        return numeroavsNNSS;
    }

    public String getPartBta() {
        return partBta;
    }

    public String getPartBtaPad() {
        return (getMoisDebut().length() == 1 ? "0" : "") + getPartBta();
    }

    public String getPartenaireId() {
        return partenaireId;
    }

    public String getPartenaireNomPrenom() {
        return partenaireNomPrenom;
    }

    public String getPartenaireNumAvs() {
        return partenaireNumAvs;
    }

    public String getPartialNss() {
        return getCI(null, true).getNumeroAvsFormateSansPrefixe();
    }

    public String getParticulier() {
        return particulier;
    }

    public String getPaysCode() {
        return getCI(null, true).getPaysForNNSS();
    }

    public String getPaysForNNSS() {
        return getCI(null, true).getPaysFormate();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.11.2002 09:21:41)
     * 
     * @return java.lang.String
     */
    public java.lang.String getPaysOrigine() {
        return getCI(null, false).getPaysOrigineId();
    }

    public String getPrenomTiers() {
        return prenomTiers;
    }

    public String getRassemblementOuvertureId() {
        return rassemblementOuvertureId;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.11.2002 09:21:41)
     * 
     * @return java.lang.String
     */
    public java.lang.String getRemarque() {
        if (remarque != null) {
            return remarque.getTexte();
        }
        return "";
    }

    public String getRevenu() {
        return montant;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.11.2002 09:21:41)
     * 
     * @return java.lang.String
     */
    public java.lang.String getSexe() {
        return getCI(null, false).getSexe();
    }

    public String getSexeCode() {
        return getCI(null, true).getSexeForNNSS();
    }

    public String getSexeForNNSS() {
        return getCI(null, true).getSexeLibelle();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.03.2003 15:01:06)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTotalControleSaisie() {
        return totalControleSaisie;
    }

    /**
     * @return
     */
    public Boolean getWantForDeclaration() {
        return wantForDeclaration;
    }

    /**
     * Returns the wrapperUtil.
     * 
     * @return CIEcritureUtil
     */
    public CIEcritureUtil getWrapperUtil() {
        if (wrapperUtil == null) {
            wrapperUtil = new CIEcritureUtil(this);
        }
        return wrapperUtil;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.06.2003 13:55:30)
     * 
     * @return boolean
     */
    public boolean hasAge() {
        try {
            String naissance = getCI(null, false).getDateNaissance();
            int anneeNaissance;
            if ((JadeStringUtil.isBlank(naissance) || (naissance.length() < 10))
                    && !getCI(null, false).getNnss().booleanValue()) {
                String avs = getCI(null, false).getNumeroAvs();
                if (JadeStringUtil.isBlank(avs) || (avs.length() < 6)) {
                    return true;
                }
                anneeNaissance = Integer.parseInt("19" + getCI(null, false).getNumeroAvs().substring(3, 5));
            } else if (getCI(null, false).getNnss().booleanValue() && JadeStringUtil.isBlankOrZero(naissance)) {
                return true;
            } else {
                anneeNaissance = Integer.parseInt(naissance.substring(6));
            }
            int anneeEcriture = Integer.parseInt(getAnnee());
            int ageMin = 18;
            if (anneeEcriture < 1957) {
                ageMin = 16;
            }
            if (anneeEcriture < (anneeNaissance + ageMin)) {
                // trop jeune
                return false;
            }
        } catch (Exception ex) {
            return true;
        }
        return true;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.07.2003 10:27:41)
     * 
     * @return boolean
     */
    public boolean hasShowRight(BTransaction transaction) {
        CICompteIndividuel ci = getCI(transaction, false);
        if (ci == null) {
            return true;
        } else {
            return ci.hasUserShowRight(transaction);
        }
    }

    /**
     * Initialize l'object écriture lors d'une inscription unique. Date de création : (15.04.2003 12:54:51)
     * 
     * @param ci
     *            le ci concerné
     */
    public void initEcriture(CICompteIndividuel ciInsc) {
        ci = ciInsc;
        setSession(ci.getSession());
        setCompteIndividuelId(ci.getCompteIndividuelId());
    }

    /**
     * @return
     */
    public Boolean isCertificat() {
        return certificat;

    }

    public boolean isCiRa() {
        CICompteIndividuelManager mgr = new CICompteIndividuelManager();
        mgr.setSession(getSession());
        mgr.setForNumeroAvs(ci.getNumeroAvs());
        mgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        try {
            if (mgr.getCount() > 0) {
                return true;

            } else {
                return false;
            }
        } catch (Exception e) {
            // TODO Bloc catch auto-généré
            e.printStackTrace();
        }
        return false;
    }

    public boolean isDeleteFromDS() {
        return deleteFromDS;
    }

    public boolean isForAffilieParitaire() {
        return forAffilieParitaire;
    }

    public boolean isForAffiliePersonnel() {
        return forAffiliePersonnel;
    }

    public boolean isFromFacturationCompta() {
        return isFromFacturationCompta;
    }

    /**
     * Indique si l'écriture peut être du genre 7 en fonction de la dernière clôture (pour non rentier). Date de
     * création : (16.04.2003 08:50:37)
     * 
     * @return boolean
     * @param anneeEcriture
     *            java.lang.String
     * @param noAvs
     *            java.lang.String
     */
    private boolean isGenreSeptPossible(int ecriture, String noAvs) throws Exception {
        String clo = getCI(null, false).getDerniereCloture(true);
        if (JadeStringUtil.isBlank(clo)) {
            // pas possible si pas clôture
            return false;
        }
        String mot = getCI(null, false).getDernierMotifCloture();
        if (!"71".equals(mot) && !"81".equals(mot)) {
            // clôturé mais pas pour AVS -> pas possible
            return false;
        }
        String moisFinTest = JadeStringUtil.isIntegerEmpty(getMoisFin()) ? "12" : getMoisFin();
        if (isMoisSpeciaux()) {
            moisFinTest = "01";
        }
        if (!BSessionUtil.compareDateFirstLower(getSession(), clo,
                "01." + JadeStringUtil.rightJustifyInteger(moisFinTest, 2) + "." + getAnnee())) {
            // écriture avant la clôture -> pas genre 7
            return false;
        }
        return true;
    }

    /**
     * Test si le mois de fin et de début sont égal à 66,77 ou 99
     * 
     * @return true si les mois de début et de fin sont spéciaux (66,77,99)
     */
    public boolean isMoisSpeciaux() {
        return "66".equals(getMoisFin()) || "77".equals(getMoisFin()) || "99".equals(getMoisFin());
    }

    public boolean isPeriodeDeCotisationACheval(BTransaction transaction, JADate dateCloture) {
        String dateClotureAMJ = dateCloture.toStrAMJ();
        int codeCloture = Integer.parseInt(dateClotureAMJ.substring(0, dateClotureAMJ.length() - 2));
        int codeEcriture;
        if (isMoisSpeciaux()) {
            codeEcriture = Integer.parseInt(getAnnee() + "01");
        } else {
            codeEcriture = Integer.parseInt(getAnnee() + JadeStringUtil.rightJustifyInteger(getMoisDebut(), 2));

        }
        if ((codeEcriture <= codeCloture) && (Integer.parseInt(getAnnee()) == dateCloture.getYear())) {
            // a cloturer
            int moisFinInt = 0;
            try {
                if (isMoisSpeciaux()) {
                    moisFinInt = 12;
                } else {
                    moisFinInt = Integer.parseInt(getMoisFin());
                }
            } catch (Exception ex) {
                // laisser à 0
            }
            int moisClotureInt = dateCloture.getMonth();
            if ((codeEcriture == codeCloture) || (moisFinInt > moisClotureInt)) {
                if (moisFinInt == Integer.parseInt(getMoisDebut())) {
                    return false;
                } else {
                    return true;
                }

            }

        }
        return false;

    }

    /**
     * Returns the ecritureUpdate.
     * 
     * @return boolean
     */
    public boolean isSumNeeded() {
        return !noSum;
    }

    /**
     * Returns the updating.
     * 
     * @return boolean
     */
    public boolean isUpdating() {
        return updating;
    }

    protected void parserGre(BStatement statement) {
        if (!JadeStringUtil.isBlank(getGre().trim())) {
            String greCourant = getGre();
            if (greCourant.length() == 2) {
                greCourant = greCourant + "0";
            }
            // Si GRE de bonne taille...
            if (greCourant.length() == 3) {
                // Récupération des codes utilisateurs
                // pour l'extourne:
                setExtourne(greCourant.substring(0, 1));
                // pour le genre d'écriture
                setGenreEcriture(greCourant.substring(1, 2));
                // pour le code particulier:
                setParticulier(greCourant.substring(2, 3));
            } else {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_ECRITURE_GRE"));
            }
        }
    }

    /**
     * Recherche ou créé le CI correspondant. Date de création : (15.04.2003 15:20:01)
     * 
     * @param transaction
     *            globaz.globall.db.BTransaction
     */
    public boolean rechercheCI(BTransaction transaction) throws Exception {
        return this.rechercheCI(transaction, null, true, true);
    }

    /**
     * Recherche ou créé le CI correspondant. Date de création : (15.04.2003 15:20:01)
     * 
     * @param transaction
     *            globaz.globall.db.BTransaction
     */
    public boolean rechercheCI(BTransaction transaction, String id) throws Exception {
        return this.rechercheCI(transaction, id, true, true);
    }

    /**
     * Recherche ou créé le CI correspondant. Date de création : (15.04.2003 15:20:01)
     * 
     * @param transaction
     *            globaz.globall.db.BTransaction
     */
    public boolean rechercheCI(BTransaction transaction, String id, boolean wantValidate, boolean wantCreate)
            throws Exception {
        if (transaction.hasErrors()) {
            return false;
        }

        CICompteIndividuel ciTemp;
        CICompteIndividuelManager ciMng = new CICompteIndividuelManager();
        ciMng.setSession(getSession());
        ciMng.orderByAvs(false);
        ciMng.setForNumeroAvs(getAvs());
        if (CIEcriture.CS_CIGENRE_6.equals(getGenreEcriture())) {
            ciMng.setForNomPrenom(getNomPrenom());
            ciMng.setForRegistre(CICompteIndividuel.CS_REGISTRE_GENRES_6);
            ciMng.find(transaction);
        } else {
            ciMng.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            if (!JadeStringUtil.isBlank(getAvs())) {
                ciMng.find(transaction);
            }
        }

        if (!transaction.hasErrors() && (ciMng.getSize() > 0)) {
            // Si il existe un CI on s'accroche à celui-là
            // pour un ci au ra, tester si l'écriture n'est pas après une
            // clôture et que le ci est fermé
            ci = (CICompteIndividuel) ciMng.getEntity(0);
            setCompteIndividuelId(ci.getCompteIndividuelId());
            return true;
        }
        if (CIEcriture.CS_CIGENRE_6.equals(getGenreEcriture())) {
            // création d'un genre 6
            ciTemp = new CICompteIndividuel();
            if (!JadeStringUtil.isBlank(id)) {
                ciTemp.setCompteIndividuelId(id);
            }
            ciTemp.wantCallValidate(wantValidate);
            ciTemp.setSession(getSession());
            ciTemp.setNomPrenom(getNomPrenom());
            ciTemp.setSexe(getSexe());
            ciTemp.setPaysOrigineId(getPaysOrigine());
            ciTemp.setDateNaissance(getDateDeNaissance());
            ciTemp.setNumeroAvs(getAvs());
            if ("true".equalsIgnoreCase(getAvsNNSS())) {
                ciTemp.setNnss(new Boolean(true));
            }
            ciTemp.setRegistre(CICompteIndividuel.CS_REGISTRE_GENRES_6);
            ciTemp.setCiOuvert(new Boolean(true));
            if (wantCreate) {
                ciTemp.add(transaction);
            }
            if (!transaction.hasErrors()) {
                ci = ciTemp;
                setCompteIndividuelId(ci.getCompteIndividuelId());
            } else {
                _addError(transaction,
                        getSession().getLabel("MSG_ECRITURE_ADD_CI_NEW") + ci.getMsgType() + " : " + ci.getMessage());
                return false;
            }
        } else if (CIEcriture.CS_CIGENRE_7.equals(getGenreEcriture()) && (Integer.parseInt(getAnnee()) < 1997)) {
            // existe un genre 7 pour cet assuré
            ciMng.setForNomPrenom(getNomPrenom());
            ciMng.setForRegistre(CICompteIndividuel.CS_REGISTRE_GENRES_7);
            ciMng.find(transaction);
            if (!transaction.hasErrors() && (ciMng.getSize() > 0)) {
                // Si il existe, on s'accroche à celui-là
                ci = (CICompteIndividuel) ciMng.getEntity(0);
                setCompteIndividuelId(ci.getCompteIndividuelId());
            } else {
                // création d'un genre 7
                ciTemp = new CICompteIndividuel();
                if (!JadeStringUtil.isBlank(id)) {
                    ciTemp.setCompteIndividuelId(id);
                }
                ciTemp.wantCallValidate(wantValidate);
                ciTemp.setSession(getSession());
                ciTemp.setNomPrenom(getNomPrenom());
                ciTemp.setSexe(getSexe());
                ciTemp.setPaysOrigineId(getPaysOrigine());
                ciTemp.setDateNaissance(getDateDeNaissance());
                ciTemp.setNumeroAvs(getAvs());
                ciTemp.setRegistre(CICompteIndividuel.CS_REGISTRE_GENRES_7);
                if ("true".equalsIgnoreCase(getAvsNNSS())) {
                    ciTemp.setNnss(new Boolean(true));
                }
                ciTemp.setCiOuvert(new Boolean(true));
                if (wantCreate) {
                    ciTemp.add(transaction);
                }
                if (!transaction.hasErrors()) {
                    ci = ciTemp;
                    setCompteIndividuelId(ci.getCompteIndividuelId());
                } else {
                    _addError(transaction, getSession().getLabel("MSG_ECRITURE_ADD_CI_NEW") + ci.getMsgType() + " : "
                            + ci.getMessage());
                    return false;
                }
            }
        } else {
            // sinon, recherche d'un éventuel CI provisoire
            ciMng.setForRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
            if (JadeStringUtil.isBlank(getAvs())) {
                ciMng.setForNomPrenom(getNomPrenom());
            }
            if (!JadeStringUtil.isBlank(getAvs()) || !JadeStringUtil.isBlank(getNomPrenom())) {
                ciMng.find(transaction);
            }
            if (!transaction.hasErrors() && (ciMng.getSize() > 0)) {
                // Si il existe un CI au registre provisoire correspondant au
                // numéro d'AVS on s'accroche à celui-là
                ci = (CICompteIndividuel) ciMng.getEntity(0);
                setCompteIndividuelId(ci.getCompteIndividuelId());
            } else {
                // création d'un ci provisoire
                ciTemp = new CICompteIndividuel();
                if (!JadeStringUtil.isBlank(id)) {
                    ciTemp.setCompteIndividuelId(id);
                }
                ciTemp.wantCallValidate(wantValidate);
                ciTemp.setSession(getSession());
                ciTemp.setNumeroAvs(getAvs());
                ciTemp.setNomPrenom(getNomPrenom());
                ciTemp.setRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
                if ("true".equalsIgnoreCase(getAvsNNSS())) {
                    ciTemp.setNnss(new Boolean(true));
                }
                ciTemp.setCiOuvert(new Boolean(true));
                if (wantCreate) {
                    ciTemp.add(transaction);
                }
                if (!transaction.hasErrors()) {
                    ci = ciTemp;
                    setCompteIndividuelId(ci.getCompteIndividuelId());
                } else {
                    _addError(transaction, getSession().getLabel("MSG_ECRITURE_ADD_CI_NEW") + ci.getMsgType() + " : "
                            + ci.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

    public void setAffHist(String affHist) {
        this.affHist = affHist;
    }

    /**
     * Sets the aijApgAmi.
     * 
     * @param aijApgAmi
     *            The aijApgAmi to set
     */
    public void setAijApgAmi(String aijApgAmi) {
        this.aijApgAmi = aijApgAmi;
    }

    public void setAncienTypeCompteTemp(String typeCompte) {
        idTypeComptePrecedant = typeCompte;

    }

    public void setAnnee(String newAnnee) {
        annee = newAnnee.trim();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 12:00:34)
     * 
     * @param newAVS
     *            java.lang.String
     */
    public void setAvs(java.lang.String newAvs) {
        getCI(null, false).setNumeroAvs(newAvs);
    }

    /**
     * @param string
     */
    public void setAvsNNSS(String string) {
        avsNNSS = string;
    }

    public void setBrancheEconomique(String newBrancheEconomique) {
        brancheEconomique = newBrancheEconomique;
    }

    public void setCacherMontant(boolean cacherMontant) {
        this.cacherMontant = cacherMontant;
    }

    public void setCaisseChomage(String newCaisseChomage) {
        caisseChomage = newCaisseChomage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.04.2003 13:01:08)
     * 
     * @param newCaisseTenantCI
     *            java.lang.String
     */
    public void setCaisseTenantCI(java.lang.String newCaisseTenantCI) {
        caisseTenantCI = newCaisseTenantCI;
    }

    /**
     * @param string
     */
    public void setCantonAF(String string) {
        cantonAF = string;
    }

    public void setCategoriePersonnel(String newCategoriePersonnel) {
        categoriePersonnel = newCategoriePersonnel;
    }

    /**
     * @param b
     */
    public void setCertificat(Boolean b) {
        certificat = b;
    }

    /**
     * Pour reprise. Date de création : (10.03.2003 09:50:13)
     * 
     * @param journal
     *            globaz.pavo.db.inscriptions.CIJournal
     */
    public void setCI(CICompteIndividuel newCi) {
        ci = newCi;
    }

    public void setCode(String newCode) {
        code = newCode;
    }

    public void setCodeSpecial(String newCodeSpecial) {
        codeSpecial = newCodeSpecial;
    }

    public void setCompteIndividuelId(String newCompteIndividuelId) {
        compteIndividuelId = newCompteIndividuelId;
    }

    public void setDateAnnonceCentrale(String newDateAnnonceCentrale) {
        dateAnnonceCentrale = newDateAnnonceCentrale;
    }

    public void setDateCiAdditionnel(String newDateCiAdditionnel) {
        dateCiAdditionnel = newDateCiAdditionnel;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.04.2003 08:48:28)
     * 
     * @param newDateCloture
     *            java.lang.String
     */
    public void setDateCloture(java.lang.String newDateCloture) {
        dateCloture = newDateCloture;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.11.2002 09:21:41)
     * 
     * @param newDateDeNaissance
     *            java.lang.String
     */
    public void setDateDeNaissance(java.lang.String newDateDeNaissance) {
        getCI(null, false).setDateNaissance(newDateDeNaissance);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.04.2003 08:48:02)
     * 
     * @param newDateOrdre
     *            java.lang.String
     */
    public void setDateOrdre(java.lang.String newDateOrdre) {
        dateOrdre = newDateOrdre;
    }

    public void setDeleteFromDS(boolean deleteFromDS) {
        this.deleteFromDS = deleteFromDS;
    }

    /**
     * @param string
     */
    public void setEcranInscriptionsSuspens(String string) {
        ecranInscriptionsSuspens = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    public void setEcritureId(String newEcritureId) {
        ecritureId = newEcritureId;
    }

    public void setEcritureReference(String newEcritureReference) {
        ecritureReference = newEcritureReference;
    }

    public void setEmployeur(String newEmployeur) {
        employeur = newEmployeur;
    }

    public void setEmployeurPartenaire(String newEmployeurPartenaire) {
        employeurPartenaire = newEmployeurPartenaire.trim();
    }

    public void setEspionSaisie(String newEspionSaisie) {
        espionSaisie = newEspionSaisie;
    }

    /**
     * @param string
     */
    public void setEspionSaisieSup(String string) {
        espionSaisieSup = string;
    }

    /**
     * Sets the estRa.
     * 
     * @param estRa
     *            The estRa to set
     */
    public void setEstRa(String estRa) {
        this.estRa = estRa;
    }

    public void setExtourne(String newExtourne) {
        extourne = newExtourne;
    }

    /**
     * Permet de filtrer sur les affiliés paritaires lors de la recherche de l'affilié (lors du _validate). false par
     * défaut
     * 
     * @param forAffilieParitaire
     */
    public void setForAffilieParitaire(boolean forAffilieParitaire) {
        this.forAffilieParitaire = forAffilieParitaire;
    }

    /**
     * Permet de filtrer sur les affiliés personnels lors de la recherche de l'affilié (lors du _validate). false par
     * défaut
     * 
     * @param forAffiliePersonnel
     */
    public void setForAffiliePersonnel(boolean forAffiliePersonnel) {
        this.forAffiliePersonnel = forAffiliePersonnel;
    }

    /**
     * @param string
     */
    public void setFromEcran(String string) {
        fromEcran = string;
    }

    public void setFromFacturationCompta(boolean isFromFacturationCompta) {
        this.isFromFacturationCompta = isFromFacturationCompta;
    }

    public void setGenreEcriture(String newGenreEcriture) {
        genreEcriture = newGenreEcriture;
    }

    public void setGenreSeptSiCloture() {
        try {
            /*
             * ***************Si modif, mettre à jour aussi ciutil pour datenTrager
             */
            String clo = "";
            CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
            ciMgr.setSession(getSession());
            ciMgr.setForNumeroAvs(getAvs());
            ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            ciMgr.find();
            if (ciMgr.size() != 0) {

                CICompteIndividuel ciClo = new CICompteIndividuel();
                ciClo = (CICompteIndividuel) ciMgr.getFirstEntity();
                clo = ciClo.getDerniereCloture(false);

                if (JadeStringUtil.isBlank(clo)) {
                    // pas possible si pas clôture
                    return;
                }
                // String mot = getCI(null, false).getDernierMotifCloture();
                String mot = ciClo.getDernierMotifCloture(false);
                if (!"71".equals(mot) && !"81".equals(mot)) {
                    // clôturé mais pas pour AVS -> pas possible
                    return;
                }
                String moisFinTest = JadeStringUtil.isIntegerEmpty(getMoisFin()) ? "12" : getMoisFin();
                if (isMoisSpeciaux()) {
                    moisFinTest = "01";
                }
                try {
                    if (!BSessionUtil.compareDateFirstLower(getSession(), clo,
                            "01." + JadeStringUtil.rightJustifyInteger(moisFinTest, 2) + "." + getAnnee())) {
                        // écriture avant la clôture -> pas genre 7
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setGenreEcriture(CIEcriture.CS_CIGENRE_7);
            }

        } catch (Exception e) {
            return;
        }
    }

    public void setGre(String newGre) {
        gre = newGre;
    }

    public void setIdAffilie(String newIdAffilie) {
        idAffilie = newIdAffilie;
    }

    public void setIdJournal(String newIdJournal) {
        idJournal = newIdJournal;
    }

    public void setIdLog(String newIdLog) {
        idLog = newIdLog;
    }

    public void setIdRemarque(String newIdRemarque) {
        idRemarque = newIdRemarque;
    }

    public void setIdTypeCompte(String newIdTypeCompte) {
        idTypeCompte = newIdTypeCompte;
    }

    /**
     * Pour reprise. Date de création : (10.03.2003 09:50:13)
     * 
     * @param journal
     *            globaz.pavo.db.inscriptions.CIJournal
     */
    public void setJournal(CIJournal newJournal) {
        journal = newJournal;
        newJournal.incCounter();
    }

    public void setLibelleAff(String libelleAff) {
        this.libelleAff = libelleAff;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.11.2002 09:21:41)
     * 
     * @param newMessages
     *            java.lang.String
     */
    public void setMessages(java.lang.String newMessages) {
        messages = newMessages;
    }

    public void setMitgliedNumNNSS(String mitgliedNumNNSS) {
        this.mitgliedNumNNSS = mitgliedNumNNSS;
    }

    /**
     * Sets the modeAjout.
     * 
     * @param modeAjout
     *            The modeAjout to set
     */
    public void setModeAjout(String modeAjout) {
        this.modeAjout = modeAjout;
    }

    public void setMoisDebut(String newMoisDebut) {
        moisDebut = newMoisDebut.trim();
    }

    public void setMoisFin(String newMoisFin) {
        moisFin = newMoisFin.trim();
    }

    public void setMontant(String newMontant) {
        // On stocke toujours un chiffre positif.
        // C'est l'extourne que décide si c'est un chiffre à enlever ou pas
        if ((newMontant != null) && newMontant.startsWith("(")) {
            // montant chaché, ne pas mettre à jour
            return;
        }
        FWCurrency val = new FWCurrency(newMontant);
        val.abs();
        montant = val.toString();
    }

    /**
     * Modifie les centimes du montant. Date de création : (04.12.2002 11:12:45)
     * 
     * @param cts
     *            les centimes du montant
     */
    public void setMontantCts(String cts) {
        montant = montant.substring(0, montant.indexOf('.') + 1) + cts;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.11.2002 09:21:41)
     * 
     * @param newNomPrenom
     *            java.lang.String
     */
    public void setNomPrenom(java.lang.String newNomPrenom) {
        getCI(null, false).setNomPrenom(newNomPrenom);
    }

    /**
     * @param string
     */
    public void setNomPrenomAssure(String string) {
        nomPrenomAssure = string;
    }

    /**
     * @param string
     */
    public void setNomSuppressor(String string) {
        nomSuppressor = string;
    }

    public void setNomTiers(String newNomTiers) {
        nomTiers = newNomTiers;
    }

    /**
     * Sets the ecritureUpdate.
     * 
     * @param ecritureUpdate
     *            The ecritureUpdate to set
     */
    public void setNoSumNeeded(boolean sumNeeded) {
        noSum = sumNeeded;
    }

    /**
     * @param string
     */
    public void setNumeroavsNNSS(String string) {
        numeroavsNNSS = string;
    }

    public void setPartBta(String newPartBta) {
        partBta = newPartBta;
    }

    public void setPartenaireId(String newPartenaireId) {
        partenaireId = newPartenaireId;
    }

    public void setPartenaireNomPrenom(String newPartenaireNomPrenom) {
        partenaireNomPrenom = newPartenaireNomPrenom;
    }

    public void setPartenaireNumAvs(String newPartenaireNumAvs) {
        partenaireNumAvs = newPartenaireNumAvs;
    }

    public void setParticulier(String newParticulier) {
        particulier = newParticulier;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.11.2002 09:21:41)
     * 
     * @param newPaysOrigine
     *            java.lang.String
     */
    public void setPaysOrigine(java.lang.String newPaysOrigine) {
        getCI(null, false).setPaysOrigineId(newPaysOrigine);
    }

    public void setPrenomTiers(String newPrenomTiers) {
        prenomTiers = newPrenomTiers;
    }

    public void setRassemblementOuvertureId(String newRassemblementOuvertureId) {
        rassemblementOuvertureId = newRassemblementOuvertureId;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.11.2002 09:21:41)
     * 
     * @param newRemarque
     *            java.lang.String
     */
    public void setRemarque(java.lang.String newRemarque) {
        if (remarque == null) {
            if (!JadeStringUtil.isBlank(newRemarque)) {
                remarque = new CIRemarque();
                remarque.setSession(getSession());
                remarque.setTexte(newRemarque);
            }
        } else {
            remarque.setTexte(newRemarque);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.11.2002 09:21:41)
     * 
     * @param newSexe
     *            java.lang.String
     */
    public void setSexe(java.lang.String newSexe) {
        getCI(null, false).setSexe(newSexe);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.03.2003 15:01:06)
     * 
     * @param newTotalControleSaisie
     *            java.lang.String
     */
    public void setTotalControleSaisie(java.lang.String newTotalControleSaisie) {
        totalControleSaisie = JANumberFormatter.deQuote(newTotalControleSaisie);
    }

    /**
     * Sets the updating.
     * 
     * @param updating
     *            The updating to set
     */
    public void setUpdating(boolean updating) {
        this.updating = updating;
    }

    /**
     * @param boolean1
     */
    public void setWantForDeclaration(Boolean boolean1) {
        wantForDeclaration = boolean1;
    }

    /**
     * Ajoute une écriture sans tester les liens avec le CI et sans mettre à jour le journal.<br>
     * Cette méthode est utile lors d'un journal de splitting par exemple ou ces tests ne sont pas nécessaire. Date de
     * création : (28.01.2003 13:55:40)
     * 
     * @param transaction
     *            la transaction à utiliser
     * @exception si
     *                une erreur survient
     */
    public void simpleAdd(BTransaction transaction) throws Exception {
        simpleProcess = true;
        this.add(transaction);
    }

    /**
     * Supprime une écriture sans mettre à jour le journal.<br>
     * Cette méthode est utile lors d'un journal de splitting par exemple ou ces tests ne sont pas nécessaire. Date de
     * création : (28.01.2003 13:55:40)
     * 
     * @param transaction
     *            la transaction à utiliser
     * @exception si
     *                une erreur survient
     */
    public void simpleDelete(BTransaction transaction) throws Exception {
        simpleProcess = true;
        this.delete(transaction);
    }

    /**
     * Mise à jour de l'écriture sans mettre à jour le journal.<br>
     * Cette méthode est utile dans le traitement des CIs Date de création : (28.01.2003 13:55:40)
     * 
     * @param transaction
     *            la transaction à utiliser
     * @exception si
     *                une erreur survient
     */
    public void simpleUpdate(BTransaction transaction) throws Exception {
        simpleProcess = true;
        this.update(transaction);
    }

    private void updateInscription(BTransaction transaction) throws Exception {
        // s'il s'agit d'une zuordnung et qu'il y a des erreurs => pas de
        // comptabilisation
        if (transaction.hasErrors() && !needNewDateCompta) {
            idTypeCompte = CIEcriture.CS_CI_SUSPENS;
            idTypeComptePrecedant = CIEcriture.CS_CI_SUSPENS;
            return;
        }
        if (transaction.hasErrors()
                && (JadeStringUtil.isIntegerEmpty(idTypeComptePrecedant) || CIEcriture.CS_TEMPORAIRE
                        .equals(idTypeComptePrecedant))) {
            // erreur dans la transaction à la suite d'une création
            setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
            return;
        }
        if (CIEcriture.CS_CORRECTION.equals(idTypeCompte)) {
            return;
        }
        // si registre provisoire
        if (CICompteIndividuel.CS_REGISTRE_PROVISOIRE.equals(getCI(transaction, false).getRegistre())) {
            if (CIEcriture.CS_CI.equals(idTypeCompte)) {
                setIdTypeCompte(CIEcriture.CS_CI_SUSPENS);
            } else {
                if (CIEcriture.CS_TEMPORAIRE.equals(idTypeComptePrecedant)
                        || JadeStringUtil.isIntegerEmpty(idTypeComptePrecedant)) {
                    setIdTypeCompte(CIEcriture.CS_TEMPORAIRE_SUSPENS);
                }
            }
        } else {
            String clo = getCI(transaction, false).getDerniereCloture(true);
            boolean apresCloture = false;
            if (!getCI(transaction, false).isCiOuvert().booleanValue() && !JadeStringUtil.isBlank(clo)) {
                // Si l'ecriture est à cheval, on sort de la méthode

                if (isPeriodeDeCotisationACheval(transaction, new JADate(clo))) {
                    _addError(transaction, getSession().getLabel("MSG_IK_PERIODE_A_CHEVAL"));
                    return;
                }
                // une clôture existe
                if (apresCloture(new JADate(clo))) {
                    // écriture après clôture -> mettre en suspens
                    if (CIEcriture.CS_CI.equals(idTypeCompte)) {
                        setIdTypeCompte(CIEcriture.CS_CI_SUSPENS);
                    } else {
                        if (CIEcriture.CS_TEMPORAIRE.equals(idTypeComptePrecedant)
                                || JadeStringUtil.isIntegerEmpty(idTypeComptePrecedant)) {
                            setIdTypeCompte(CIEcriture.CS_TEMPORAIRE_SUSPENS);
                        }
                    }
                    apresCloture = true;
                }
            } else if (!JadeStringUtil.isBlank(clo)) {
                if (isPeriodeDeCotisationACheval(transaction, new JADate(clo))) {
                    _addError(transaction, getSession().getLabel("MSG_IK_PERIODE_A_CHEVAL"));
                    return;
                }

            }
            if (!apresCloture) {
                // le ci n'est pas provisoire l'inscription n'est pas après une
                // clôture
                if (CIEcriture.CS_CI_SUSPENS.equals(idTypeCompte)) {
                    setIdTypeCompte(CIEcriture.CS_CI);
                }
                if (CIEcriture.CS_TEMPORAIRE_SUSPENS.equals(idTypeCompte)) {
                    setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
                }
            }
        }
        if (CIEcriture.CS_CI.equals(idTypeCompte) || CIEcriture.CS_CI_SUSPENS.equals(idTypeCompte)) {
            // comptabiliser si pas déjà fait
            if (JadeStringUtil.isIntegerEmpty(idTypeComptePrecedant)
                    || CIEcriture.CS_TEMPORAIRE.equals(idTypeComptePrecedant)
                    || CIEcriture.CS_TEMPORAIRE_SUSPENS.equals(idTypeComptePrecedant)) {
                if (simpleProcess) {
                    this.comptabiliser(transaction, true, false);
                } else {
                    this.comptabiliser(transaction, true, true);
                }
                // On récupère le journal synchronisé
                CIJournal journal = getJournal(transaction, true);
                journal.retrieve(transaction);
                journal.setIdEtat(CIJournal.CS_PARTIEL);
                if (!simpleProcess) {
                    journal.update(transaction);
                }
            }
        }

    }

    public void updTypeCommptePrecedant(String newType) {
        idTypeComptePrecedant = newType;
    }

    /**
     * Valeurs par défaut pour une Ecriture. Date de création : (14.11.2002 09:53:16)
     * 
     * @param transaction
     *            globaz.globall.db.BTransaction
     */
    public void valeursDefaut(BTransaction transaction) throws Exception {
        CIJournal journal = getJournal(transaction, false);
        if (!journal.isNew()) {
            // Mois début = 1 si type DECLARATION_SALAIRES ou
            // DECLARATION_COMPLEMENTAIRE
            if (JadeStringUtil.isBlank(getMoisDebut())
                    && (!CIJournal.CS_SPLITTING.equals(journal.getIdTypeInscription()) && !CIJournal.CS_INSCRIPTIONS_JOURNALIERES
                            .equals(journal.getIdTypeInscription()))) {
                setMoisDebut("1");
            }
            // Mois fin = 12 si type DECLARATION_SALAIRES ou
            // DECLARATION_COMPLEMENTAIRE
            if (JadeStringUtil.isBlank(getMoisFin())
                    && (!CIJournal.CS_SPLITTING.equals(journal.getIdTypeInscription()) && !CIJournal.CS_INSCRIPTIONS_JOURNALIERES
                            .equals(journal.getIdTypeInscription()))) {
                setMoisFin("12");
                setWantForDeclaration(new Boolean(false));

            } else if (wantForDeclaration.booleanValue()) {
                setWantForDeclaration(new Boolean(true));
            }
            // Genre
            // =1 si type DECLARATION SALAIRES ou DECLARATION COMPLEMENTAIRE,
            // APG, IJAI, ASSURANCE CHOMAGE, ASSURANCE MILITAIRE ou CONTROLE
            // EMPLOYEUR
            if (CIJournal.CS_DECLARATION_SALAIRES.equals(journal.getIdTypeInscription())
                    || CIJournal.CS_DECLARATION_COMPLEMENTAIRE.equals(journal.getIdTypeInscription())
                    || CIJournal.CS_APG.equals(journal.getIdTypeInscription())
                    || CIJournal.CS_PANDEMIE.equals(journal.getIdTypeInscription())
                    || CIJournal.CS_IJAI.equals(journal.getIdTypeInscription())
                    || CIJournal.CS_ASSURANCE_CHOMAGE.equals(journal.getIdTypeInscription())
                    || CIJournal.CS_ASSURANCE_MILITAIRE.equals(journal.getIdTypeInscription())
                    || CIJournal.CS_CORRECTIF.equals(journal.getIdTypeInscription())
                    || CIJournal.CS_CONTROLE_EMPLOYEUR.equals(journal.getIdTypeInscription())) {
                setGenreEcriture(CIEcriture.CS_CIGENRE_1);
            } else // =8 si type SPLITTING
            if (CIJournal.CS_SPLITTING.equals(journal.getIdTypeInscription())) {
                setGenreEcriture(CIEcriture.CS_CIGENRE_8);
            } else // =0 si type ASSURANCE FACULTATIVE
            if (CIJournal.CS_ASSURANCE_FACULTATIVE.equals(journal.getIdTypeInscription())) {
                setGenreEcriture(CIEcriture.CS_CIGENRE_0);
            }
            // Extourne, Si 0, c'est qu'il ny en a pas
            if ("0".equals(getExtourne())) {
                setExtourne("");
            }
            // Particulier = 0
            if (JadeStringUtil.isBlank(getParticulier())) {
                setParticulier(CIEcriture.CS_MANDAT_NORMAL);
            }
            // Année = année de cotisation du journal si ce champs existe
            if (JadeStringUtil.isBlank(getAnnee()) && (!JadeStringUtil.isBlank(journal.getAnneeCotisation()))) {
                setAnnee(journal.getAnneeCotisation());
            }
            // remplir le chiffre clé en fonction de l'affiliation si ce dernier
            // est omis
            if ((JadeStringUtil.isBlank(getGre()) && JadeStringUtil.isBlank(getGenreEcriture()))
                    && (CIJournal.CS_DECLARATION_SALAIRES.equals(journal.getIdTypeInscription())
                            || CIJournal.CS_DECLARATION_COMPLEMENTAIRE.equals(journal.getIdTypeInscription())
                            || CIJournal.CS_CORRECTIF.equals(journal.getIdTypeInscription())
                            || CIJournal.CS_CONTROLE_EMPLOYEUR.equals(journal.getIdTypeInscription())
                            || CIJournal.CS_COTISATIONS_PERSONNELLES.equals(journal.getIdTypeInscription())
                            || CIJournal.CS_DECISION_COT_PERS.equals(journal.getIdTypeInscription()) || CIJournal.CS_INSCRIPTIONS_JOURNALIERES
                                .equals(journal.getIdTypeInscription()))) {
                CIApplication app = (CIApplication) getSession().getApplication();
                AFAffiliation aff = null;
                try {

                    aff = app.getAffilieByNo(getSession(), employeurPartenaire, forAffilieParitaire,
                            forAffiliePersonnel, getMoisDebut(), getMoisFin(), getAnnee(), "", "");
                    if (aff != null) {
                        if (CIAffilie.CS_NON_ACTIF.equals(aff.getTypeAffiliation())
                                || CIAffilie.CS_NA_SELON_ART_1.equals(aff.getTypeAffiliation())) {
                            setGre("04");
                        }
                        if (CIAffilie.CS_EMPLOYEUR.equals(aff.getTypeAffiliation())) {
                            setGre("01");
                        }
                        if (CIAffilie.CS_INDEPENDANT.equals(aff.getTypeAffiliation())) {
                            setGre("03");
                        }
                        if (CIAffilie.CS_TSE.equals(aff.getTypeAffiliation())) {
                            setGre("02");
                        }
                        if (CIAffilie.CS_INDEPENDANT_EMPLOYEUR.equals(aff.getTypeAffiliation())) {

                            TITiersViewBean ti = new TITiersViewBean();
                            ti.setIdTiers(aff.getIdTiers());
                            ti.setSession(getSession());
                            ti.retrieve();
                            if (CIUtil.unFormatAVS(ti.getNumAvsActuel()).equals(CIUtil.unFormatAVS(getAvs()))) {
                                setGre("03");
                            } else {
                                setGre("01");
                            }
                        }
                        if (CIAffilie.CS_ASSURANCE_FAC.equals(aff.getTypeAffiliation())) {
                            setGre("00");
                        }
                    }
                } catch (Exception e) {

                }

            }
            // Employeur = ID d'affiliation du journal si ce champs existe
            if (JadeStringUtil.isBlank(getEmployeur()) && (!JadeStringUtil.isBlank(journal.getIdAffiliation()))) {
                setEmployeur(journal.getIdAffiliation());
                AFAffiliationManager affmgr = new AFAffiliationManager();
                AFAffiliation aff = new AFAffiliation();
                affmgr.setForAffiliationId(journal.getIdAffiliation());
                affmgr.setSession(getSession());
                try {
                    affmgr.find();
                } catch (Exception e) {
                }
                if (affmgr.size() == 1) {
                    aff = (AFAffiliation) affmgr.getEntity(0);
                    setBrancheEconomique(aff.getBrancheEconomique());
                }

            }
            // Part BTA = vide
            if (JadeStringUtil.isBlank(getPartBta())) {
                setPartBta("");
            }
            // Branche économique = vide
            if (JadeStringUtil.isBlank(getBrancheEconomique())) {
                setBrancheEconomique("");
            }
            // Caisse chômage = vide
            if (JadeStringUtil.isBlank(getCaisseChomage())) {
                setCaisseChomage("");
            }
            // Code irrécouvrable = vide
            if (JadeStringUtil.isBlank(getCode())) {
                setCode("");
            }
            // Type de compte = le type de compte du journal si celui-ci existe
            if (JadeStringUtil.isIntegerEmpty(getIdTypeCompte())
                    && (!JadeStringUtil.isIntegerEmpty(journal.getIdTypeCompte()))) {
                setIdTypeCompte(journal.getIdTypeCompte());
            }
        }
    }

    public String getJourDebut() {
        return jourDebut;
    }

    public void setJourDebut(String jourDebut) {
        this.jourDebut = jourDebut;
    }

    public String getJourFin() {
        return jourFin;
    }

    public void setJourFin(String jourFin) {
        this.jourFin = jourFin;
    }

}
