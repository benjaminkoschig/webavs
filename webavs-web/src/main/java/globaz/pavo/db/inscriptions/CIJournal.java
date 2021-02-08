package globaz.pavo.db.inscriptions;

import globaz.framework.secure.user.FWSecureUser;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureCounter;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.db.compte.CIEcrituresSomme;
import globaz.pavo.process.CIEffaceJournal;
import globaz.pavo.util.CIUtil;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;

/**
 * Journal d'inscriptions.
 * 
 * @author: ema
 */
public class CIJournal extends BEntity {

    private static final long serialVersionUID = 6799080018665175470L;
    public final static String CS_APG = "301006";
    public final static String CS_PANDEMIE = "301015";
    public final static String CS_ASSURANCE_CHOMAGE = "301009";
    public final static String CS_ASSURANCE_FACULTATIVE = "301012";
    public final static String CS_ASSURANCE_MILITAIRE = "301010";
    public final static String CS_BTA = "301014";
    // Type de compte
    public final static String CS_CI = "303001";
    public final static String CS_COMPTABILISE = "302002";
    public final static String CS_CONTROLE_EMPLOYEUR = "301003";
    public final static String CS_CORRECTIF = "301011";
    public final static String CS_COTISATIONS_PERSONNELLES = "301004";
    public final static String CS_DECISION_COT_PERS = "301005";
    public final static String CS_DECLARATION_COMPLEMENTAIRE = "301002";
    // Type d'inscription
    public final static String CS_DECLARATION_SALAIRES = "301001";
    public final static String CS_IJAI = "301007";
    public final static String CS_INSCRIPTIONS_JOURNALIERES = "301013";
    // Etat du journal
    public final static String CS_OUVERT = "302001";
    public final static String CS_PARTIEL = "302003";
    public final static String CS_PROVISOIRE = "303006";
    public final static String CS_SPLITTING = "301008";
    public static int FROM_JOIN = 1;
    // syncro BD
    public static int FROM_RETRIEVE = 0;

    // code systeme
    private String affError = "";

    /** (KCANCO) */
    private String anneeCotisation = new String();
    /** (KCNCOR) */
    private String correctionSpeciale = new String();
    // compteur d'écritures interne (reprise)
    private int counterEcriture = 0;
    /** (KCDATE) */
    private String date = new String();

    /** (KCDINS) */
    private String dateInscription = new String();
    /** (KCDREC) */
    private String dateReception = new String();

    /** (KCBVER) */
    private String estVerrouille = new String();
    private int etatEntity = CIJournal.FROM_RETRIEVE;
    /** (KCIAFF) */
    private String idAffiliation = new String();
    /** (KCIETA) */
    private String idEtat = new String();
    /** Fichier CIJournal */
    /** (KCID) */
    private String idJournal = new String();
    /** (KIIREM) */
    private String idRemarque = new String();
    /** (KCITCP) */
    private String idTypeCompte = new String();
    /** (KCITIN) */
    private String idTypeInscription = new String();
    private boolean isFromFacturationCompta = false;
    /** (KCLIB) */
    private String libelle = new String();
    // Le memory log (pour la comptabilisation)
    private FWMemoryLog memoryLog = null;
    /** (KCMCOR) */
    private String motifCorrection = new String();
    // Le nom du tiers
    private String nomTiers = new String();
    // Le numéro d'affilié du tiers
    private String numeroAffilie = new String();
    // Le prénom du tiers
    private String prenomTiers = new String();
    /** (KCUSER) */
    private String proprietaire = new String();
    /** (KCREFE) */
    private String referenceExterne = new String();
    // Référence externe pour les journeaux de facturation
    private String refExterneFacturation = new String();
    // Le texte de remarque
    private String remTexte = new String();
    /** (KCMTOT) */
    private String totalControle = new String();
    /** (KCMTIN) */
    private String totalInscrit = new String();

    public static String getTotalFactures() {
        return "";
    }

    /**
     * Commentaire relatif au constructeur CIJournal
     */
    public CIJournal() {
        super();
        setIdTypeCompte(CIJournal.CS_PROVISOIRE);
    }

    @Override
    protected void _afterAdd(BTransaction transaction) throws java.lang.Exception {
    }

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws java.lang.Exception {
        // todo à enlever lorsque texte à 255 bytes
        if (!JadeStringUtil.isIntegerEmpty(getIdRemarque())) {
            CIRemarque rem = new CIRemarque();
            rem.setSession(getSession());
            rem.setIdRemarque(getIdRemarque());
            rem.retrieve(transaction);
            setRemTexte(rem.getTexte());
        }
    }

    @Override
    protected void _afterRetrieveWithResultSet(BStatement statement) throws java.lang.Exception {
    }

    @Override
    protected void _afterUpdate(BTransaction transaction) throws java.lang.Exception {
    }

    /**
     * Reset de l'id si entity en erreur
     */
    @Override
    protected void _alwaysAfterAdd(BTransaction transaction) throws java.lang.Exception {
        if (transaction.hasErrors()) {
            setIdJournal("");
            affError = "";
        }
    }

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // Valeurs par défaut
        // Le journal est ouvert
        if (JadeStringUtil.isIntegerEmpty(getIdEtat())) {
            setIdEtat(CIJournal.CS_OUVERT);
        }
        // Date de création
        setDate(JACalendar.todayJJsMMsAAAA());
        // Le type de compte est PROVISOIRE sauf si inscriptions SPLTTING ou
        // CORRECTIF (dans ce cas: CI)
        if (JadeStringUtil.isIntegerEmpty(getIdTypeCompte())) {
            if ((CIJournal.CS_SPLITTING.equals(getIdTypeInscription()))
                    || (CIJournal.CS_CORRECTIF.equals(getIdTypeInscription()))) {
                setIdTypeCompte(CIJournal.CS_CI);
            } else {
                setIdTypeCompte(CIJournal.CS_PROVISOIRE);
            }
        }
        if (CIJournal.CS_COTISATIONS_PERSONNELLES.equals(getIdTypeInscription())) {
            _propertyMandatory(transaction, getAnneeCotisation(), getSession().getLabel("MSG_JOURNAL_ADDUP_ANCO"));
        }

        if (CIJournal.CS_COTISATIONS_PERSONNELLES.equals(getIdTypeInscription())
                && !JadeStringUtil.isBlank(anneeCotisation)) {
            CIJournalManager journalMng = new CIJournalManager();
            journalMng.setSession(getSession());
            journalMng.setForIdTypeInscription(CIJournal.CS_COTISATIONS_PERSONNELLES);
            journalMng.setForAnneeCotisation(getAnneeCotisation());
            if (journalMng.getCount(transaction) > 0) {
                _addError(transaction, getSession().getLabel("MSG_SECOND_JOURNAL_COT_PERS"));
            }
        }
        if (CIJournal.CS_DECLARATION_SALAIRES.equals(getIdTypeInscription())
                || CIJournal.CS_DECLARATION_COMPLEMENTAIRE.equals(getIdTypeInscription())
                || CIJournal.CS_CONTROLE_EMPLOYEUR.equals(getIdTypeInscription())) {
            // L'année de cotisation est obligatoire en cas de
            // DECLARATION_SALAIRES ou de DECLARATION_COMPLEMENTAIRE
            _propertyMandatory(transaction, getAnneeCotisation(), getSession().getLabel("MSG_JOURNAL_ADDUP_ANCO"));
            if (Integer.parseInt(getAnneeCotisation()) > java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)) {
                _addError(transaction, getSession().getLabel("MSG_ANN_SUP_ANNEE_ACUTEL"));
            }
            if (CIJournal.CS_DECLARATION_SALAIRES.equals(getIdTypeInscription())) {
                CIJournalManager journalMng = new CIJournalManager();
                journalMng.setSession(getSession());
                journalMng.setForIdTypeInscription(CIJournal.CS_DECLARATION_SALAIRES);
                journalMng.setForAnneeCotisation(getAnneeCotisation());
                journalMng.setForIdAffiliationReel(getIdAffiliation());
                journalMng.find(transaction);
                if (!transaction.hasErrors()) {
                    // Plusieurs journaux CS_DECLARATION_SALAIRES sur la même
                    // année
                    if (journalMng.getSize() > 0) {
                        setIdTypeInscription(CIJournal.CS_DECLARATION_COMPLEMENTAIRE);
                    }

                }
            }
        }
        // validation des entrées. On ne peut pas utiliser _validate()
        validateInputs(transaction);
        // Créer une nouvelle remarque
        if (!JadeStringUtil.isBlank(getRemTexte())) {
            CIRemarque remarque = new CIRemarque();
            remarque.setSession(getSession());
            remarque.setTexte(getRemTexte());
            remarque.add(transaction);
            if (!transaction.hasErrors()) {
                setIdRemarque(remarque.getIdRemarque());
            } else {
                _addError(transaction, getSession().getLabel("MSG_JOURNAL_REM"));
            }
        }
        // incrémente de +1 le numéro si pas d'erreur dans le bean
        if (!transaction.hasErrors() && JadeStringUtil.isIntegerEmpty(idJournal)) {
            setIdJournal(this._incCounter(transaction, "0"));
        }
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws java.lang.Exception {
        // Le journal doit etre ouvert
        if (!CIJournal.CS_OUVERT.equals(getIdEtat())) {
            _addError(transaction, getSession().getLabel("MSG_JOURNAL_UPDATE"));
            return;
        }
        // propriétaire
        String lUser = getSession().getUserId();
        if ((lUser != null) && (lUser.length() != 0)) {
            if (!proprietaire.equals(lUser) && !CIUtil.isSpecialist(transaction.getSession())) {
                _addError(transaction, getSession().getLabel("MSG_JOURNAL_PROPRIO"));
                return;
            }
        }

        // Le type de compte des ecritures du journal doir etre different de CI
        CIEcritureManager ecrMng = new CIEcritureManager();
        ecrMng.setSession(getSession());
        ecrMng.setForIdTypeCompteList(new String[] { CIEcriture.CS_CI, CIEcriture.CS_GENRE_6, CIEcriture.CS_GENRE_7,
                CIEcriture.CS_CI_SUSPENS });
        ecrMng.setForIdJournal(getIdJournal());
        int ecrCINumber = ecrMng.getCount(transaction);
        if (!transaction.hasErrors()) {
            if (ecrCINumber > 0) {
                _addError(transaction, getSession().getLabel("MSG_JOURNAL_DELETE_TYPECOMPTE"));
                // Pas la peine d'effacer les ecritures
                return;
            }
        } else {
            _addError(transaction, getSession().getLabel("MSG_JOURNAL_RETRI_ECR"));
            return;
        }

        try {
            if (!transaction.hasErrors()) {

                CIEffaceJournal process = new CIEffaceJournal(getSession());
                process.setIdJournal(getIdJournal());
                process.setSendCompletionMail(false);
                process.executeProcess();

            } else {
                _addError(transaction, getSession().getLabel("MSG_JOURNAL_RETRI_ECR"));
                return;
            }
        } catch (Exception e) {
            _addError(transaction, getSession().getLabel("MSG_SUPP_JOURNAL_ECHEC"));
            return;
        }
        // effacer la remarque
        if (!JadeStringUtil.isIntegerEmpty(getIdRemarque())) {
            CIRemarque rem = new CIRemarque();
            rem.setSession(getSession());
            rem.setIdRemarque(getIdRemarque());
            rem.retrieve(transaction);
            rem.delete(transaction);
        }
    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws java.lang.Exception {
        // validation des entrées. On ne peut pas utiliser _validate()
        validateInputs(transaction);
        // La remarque
        CIRemarque remarque = new CIRemarque();
        // Mettre à jour la remarque, si elle existe
        if (!JadeStringUtil.isIntegerEmpty(getIdRemarque())) {
            remarque.setSession(getSession());
            remarque.setIdRemarque(getIdRemarque());
            remarque.retrieve(transaction);
            if (!transaction.hasErrors() && (!remarque.isNew())) {
                // Si le texte est non nul on met à jour, sinon on efface
                if (!JadeStringUtil.isBlank(getRemTexte())) {
                    remarque.setTexte(getRemTexte());
                    remarque.update(transaction);
                } else {
                    remarque.delete(transaction);
                    setIdRemarque("");
                }
                if (transaction.hasErrors()) {
                    _addError(transaction, getSession().getLabel("MSG_JOURNAL_REM"));
                }
            } else {
                _addError(transaction, getSession().getLabel("MSG_JOURNAL_REM"));
            }
        } else { // Sinon la créer
            if (!JadeStringUtil.isBlank(getRemTexte())) {
                remarque.setSession(getSession());
                remarque.setTexte(getRemTexte());
                remarque.add(transaction);
                if (transaction.hasErrors()) {
                    _addError(transaction, getSession().getLabel("MSG_JOURNAL_REM"));
                } else {
                    setIdRemarque(remarque.getIdRemarque());
                }
            }
        }
    }

    @Override
    protected String _getFields(BStatement statement) {
        return _getCollection() + _getTableName() + ".*,  " + _getCollection() + "AFAFFIP.MALNAF, " + _getCollection()
                + "TITIERP.HTLDE1, " + _getCollection() + "TITIERP.HTLDE2 ";
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
        joinStr = // " left outer join "+_getCollection()
        // +"CIECRIP on "+_getCollection() +_getTableName()
        // +".KCID="+_getCollection() +"CIECRIP.KCID)" +
        // " LEFT OUTER JOIN "+_getCollection()+ "CIREMAP ON "+
        // _getCollection()+ _getTableName()+ ".KCID="+ _getCollection()+
        // "CIREMAP.KCID)" +
        " left outer join " + _getCollection() + "AFAFFIP on " + _getCollection() + _getTableName() + ".KCIAFF="
                + _getCollection() + "AFAFFIP.MAIAFF" + " left outer join " + _getCollection() + "TITIERP on "
                + _getCollection() + "AFAFFIP.HTITIE=" + _getCollection() + "TITIERP.HTITIE";
        // " left outer join "+_getCollection() +"TIPAVSP on "+_getCollection()
        // +_getTableName() +".KCIAFF="+_getCollection() +"TIPAVSP.HTITIE" +
        // " left outer join "+_getCollection() +"TITIERP on "+_getCollection()
        // +_getTableName() +".KCIAFF="+_getCollection() +"TITIERP.HTITIE";
        return _getCollection() + _getTableName() + joinStr;
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CIJOURP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idJournal = statement.dbReadNumeric("KCID");
        idRemarque = statement.dbReadNumeric("KIIREM");
        date = statement.dbReadDateAMJ("KCDATE");
        libelle = statement.dbReadString("KCLIB");
        idTypeInscription = statement.dbReadNumeric("KCITIN");
        totalControle = statement.dbReadNumeric("KCMTOT", 2);
        totalInscrit = statement.dbReadNumeric("KCMTIN", 2);
        correctionSpeciale = statement.dbReadNumeric("KCNCOR", 2);
        motifCorrection = statement.dbReadString("KCMCOR");
        anneeCotisation = statement.dbReadNumeric("KCANCO");
        idAffiliation = statement.dbReadNumeric("KCIAFF");
        dateReception = statement.dbReadDateAMJ("KCDREC");
        dateInscription = statement.dbReadDateAMJ("KCDINS");
        referenceExterne = statement.dbReadString("KCREFE");
        proprietaire = statement.dbReadString("KCUSER");
        estVerrouille = statement.dbReadString("KCBVER");
        idTypeCompte = statement.dbReadNumeric("KCITCP");
        idEtat = statement.dbReadNumeric("KCIETA");
        refExterneFacturation = statement.dbReadString("KCREFEX");
        // écritures trouvées
        // ecrituresFound = statement.dbReadNumeric("FOUND");
        // Lecture des propriétés issues du tiers
        nomTiers = statement.dbReadString("HTLDE1");
        prenomTiers = statement.dbReadString("HTLDE2");
        numeroAffilie = statement.dbReadString("MALNAF");
    }

    @Override
    protected void _validate(BStatement statement) throws java.lang.Exception {
        if (!JadeStringUtil.isBlank(affError)) {
            _addError(statement.getTransaction(), affError);
        }
        // validateInputs(statement.getTransaction());
        if (CIJournal.CS_DECLARATION_SALAIRES.equals(getIdTypeInscription())) {
            CIJournalManager journalMng = new CIJournalManager();
            journalMng.setSession(getSession());
            journalMng.setForIdTypeInscription(CIJournal.CS_DECLARATION_SALAIRES);
            journalMng.setForAnneeCotisation(getAnneeCotisation());
            journalMng.setForIdAffiliationReel(getIdAffiliation());
            journalMng.find(statement.getTransaction());
            if ((journalMng.getSize() == 1)
                    && (!((CIJournal) journalMng.getEntity(0)).getIdJournal().equals(getIdJournal()))) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_JOURNAL_DECLAR_SAL"));
            }

        }
        if (CIJournal.CS_COTISATIONS_PERSONNELLES.equals(getIdTypeInscription())
                && !JadeStringUtil.isBlank(anneeCotisation)) {
            CIJournalManager journalMng = new CIJournalManager();
            journalMng.setSession(getSession());
            journalMng.setForIdTypeInscription(CIJournal.CS_COTISATIONS_PERSONNELLES);
            journalMng.setForAnneeCotisation(getAnneeCotisation());
            journalMng.find(statement.getTransaction());
            if ((journalMng.getSize() == 1)
                    && (!((CIJournal) journalMng.getEntity(0)).getIdJournal().equals(getIdJournal()))) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_SECOND_JOURNAL_COT_PERS"));
            }
        }

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(_getCollection() + _getTableName() + ".KCID",
                this._dbWriteNumeric(statement.getTransaction(), getIdJournal(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("KCID", this._dbWriteNumeric(statement.getTransaction(), getIdJournal(), "idJournal"));
        statement.writeField("KIIREM", this._dbWriteNumeric(statement.getTransaction(), getIdRemarque(), "idRemarque"));
        statement.writeField("KCDATE", this._dbWriteDateAMJ(statement.getTransaction(), getDate(), "date"));
        statement.writeField("KCLIB", this._dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField("KCITIN",
                this._dbWriteNumeric(statement.getTransaction(), getIdTypeInscription(), "idTypeInscription"));
        statement.writeField("KCMTOT",
                this._dbWriteNumeric(statement.getTransaction(), getTotalControle(), "totalControle"));
        statement.writeField("KCMTIN",
                this._dbWriteNumeric(statement.getTransaction(), getTotalInscrit(), "totalInscrit"));
        statement.writeField("KCNCOR",
                this._dbWriteNumeric(statement.getTransaction(), getCorrectionSpeciale(), "correctionSpeciale"));
        statement.writeField("KCMCOR",
                this._dbWriteString(statement.getTransaction(), getMotifCorrection(), "motifCorrection"));
        statement.writeField("KCANCO",
                this._dbWriteNumeric(statement.getTransaction(), getAnneeCotisation(), "anneeCotisation"));
        statement.writeField("KCIAFF",
                this._dbWriteNumeric(statement.getTransaction(), getIdAffiliation(), "idAffiliation"));
        statement.writeField("KCDREC",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateReception(), "dateReception"));
        statement.writeField("KCDINS",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateInscription(), "dateInscription"));
        statement.writeField("KCREFE",
                this._dbWriteString(statement.getTransaction(), getReferenceExterne(), "referenceExterne"));
        statement.writeField("KCUSER",
                this._dbWriteString(statement.getTransaction(), getProprietaire(), "proprietaire"));
        statement.writeField("KCBVER",
                this._dbWriteString(statement.getTransaction(), getEstVerrouille(), "estVerrouille"));
        statement.writeField("KCITCP",
                this._dbWriteNumeric(statement.getTransaction(), getIdTypeCompte(), "idTypeCompte"));
        statement.writeField("KCIETA", this._dbWriteNumeric(statement.getTransaction(), getIdEtat(), "idEtat"));
        statement.writeField("KCREFEX",
                this._dbWriteString(statement.getTransaction(), getRefExterneFacturation(), "refExterneFacturation"));
    }

    /**
     * Test si l'affilié donné existe dans la période de l'écriture
     */
    private boolean checkPerdiodeAff(String debutAff, String finAff) throws Exception {
        if (JadeStringUtil.isBlank(getAnneeCotisation()) || "0".equals(getAnneeCotisation())) {
            return true;
        }
        boolean result = BSessionUtil.compareDateFirstLowerOrEqual(getSession(), debutAff, "31.12."
                + getAnneeCotisation());
        if (JAUtil.isDateEmpty(finAff)) {
            return result;
        }
        return result
                & BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), finAff, "01.01." + getAnneeCotisation());
    }

    /**
     * Uniquement pour reprise. Date de création : (27.03.2003 16:16:08)
     * 
     * @param transaction
     *            globaz.globall.db.BTransaction
     */
    public void closeJournalReprise(BTransaction transaction) throws Exception {
        CIEcrituresSomme somme = new CIEcrituresSomme();
        somme.setSession(getSession());
        somme.setForIdJournal(getIdJournal());
        BigDecimal result = somme.getSum("KBMMON", transaction);
        if (result != null) {
            setTotalInscrit(result.toString());
            setIdEtat(CIJournal.CS_COMPTABILISE);
            wantCallMethodBefore(false);
            this.update(transaction);
            wantCallMethodBefore(true);
        }
    }

    /**
     * Comptabilisation des écritures d'un journal Date de création : (27.11.2002 11:13:36)
     * 
     * @param Un
     *            numéro AVS de départ
     * @param Un
     *            numéro AVS de fin
     * @param Une
     *            BTransaction
     */
    public StringBuffer comptabiliser(String fromAvs, String toAvs, BITransaction transactionIfc, BProcess process)
            throws Exception {
        // Comptabilisationrefusée si etat sur ETAT_COMPTABILISER
        if (getIdEtat() == CIJournal.CS_COMPTABILISE) {
            throw new Exception(getSession().getLabel("MSG_JOURNAL_COMPT_ETAT"));
        }
        BTransaction transaction = null;
        StringBuffer errorBuffer = new StringBuffer();
        boolean createTransaction = false;
        if (transactionIfc instanceof BTransaction) {
            transaction = (BTransaction) transactionIfc;
        } else {
            throw new Exception("Transaction incorrecte");
        }
        try {
            if (transaction.getConnection() == null) {
                transaction.openTransaction();
                createTransaction = true;
            }
            // totalControle et totalInscrit doivent être égaux
            /*
             * Modif 4.13, seuls DS, comléments, contr empl et correctifs doivent avoir un total de contrôle
             */
            if (CIJournal.CS_DECLARATION_SALAIRES.equals(getIdTypeInscription())
                    || CIJournal.CS_DECLARATION_COMPLEMENTAIRE.equals(getIdTypeInscription())
                    || CIJournal.CS_CONTROLE_EMPLOYEUR.equals(getIdTypeInscription())
                    || CIJournal.CS_CORRECTIF.equals(getIdTypeInscription())) {
                if (!getTotalControle().equals(getTotalInscrit())) {
                    _addError(transaction, getSession().getLabel("MSG_JOURNAL_COMPT_TOTAUX") + getIdJournal());
                    return errorBuffer;
                }
            }
            // Selection des inscriptions pour lesquelles l'idTypeCompte est
            // PROVISOIRE
            BStatement statement = null;
            CIEcritureManager ecritureMng = new CIEcritureManager();
            ecritureMng.setSession(getSession());
            ecritureMng.setForIdJournal(getIdJournal());
            ecritureMng.setForIdTypeCompteList(new String[] { CIEcriture.CS_TEMPORAIRE,
                    CIEcriture.CS_TEMPORAIRE_SUSPENS });
            ecritureMng.orderByAnnee();
            int sizeProcess = ecritureMng.getCount(transaction);
            process.setProgressScaleValue(sizeProcess);
            ecritureMng.changeManagerSize(BManager.SIZE_NOLIMIT);
            // ecritureMng.find(transaction);
            int ind = 0;

            statement = ecritureMng.cursorOpen(transaction);
            // On formate les bornes du numéro AVS sur la même longueur
            int maxAvsBound = Integer.MAX_VALUE;
            double fromAvsVal = 0;
            double toAvsVal = Double.MAX_VALUE;
            if ((!JadeStringUtil.isBlank(fromAvs)) || (!JadeStringUtil.isBlank(toAvs))) {
                maxAvsBound = Math.max(fromAvs.length(), toAvs.length());
            }
            // Conversion vers un entier pour la comparaison
            if (!JadeStringUtil.isBlank(fromAvs)) {
                fromAvsVal = new Double(fromAvs).doubleValue();
            }
            if (!JadeStringUtil.isBlank(toAvs)) {
                toAvsVal = new Double(toAvs).doubleValue();
            }
            double avsCourantVal = 0;
            // HashMap cis = new HashMap();
            if (!transaction.hasErrors()) {
                // Comptabilisation de l'ecriture
                String dateCompta;
                if (JadeStringUtil.isBlank(getDateInscription())) {
                    dateCompta = JACalendar.today().toStrAMJ();
                } else {
                    dateCompta = new JADate(getDateInscription()).toStrAMJ();
                }
                CIEcriture ecritureCourante;
                // for (int i = 0; i < ecritureMng.getSize(); i++) {
                while ((ecritureCourante = (CIEcriture) ecritureMng.cursorReadNext(statement)) != null) {
                    // ecritureCourante = (CIEcriture) ecritureMng.getEntity(i);
                    // Entre certains numéros AVS
                    // D'abord formater le numéro AVS sur la même longueur que
                    // les bornes
                    ind++;
                    process.setProgressCounter(ind);
                    ecritureCourante.setNoSumNeeded(true);
                    String avsCourant = ecritureCourante.getAvs();
                    if (avsCourant.length() > maxAvsBound) {
                        // Le numéro est trop long-> sous-chaîne
                        avsCourant = avsCourant.substring(0, maxAvsBound);
                    }
                    if (JadeStringUtil.isBlank(avsCourant)) {
                        avsCourantVal = 0;
                    } else {
                        avsCourantVal = new Double(avsCourant).doubleValue();
                    }
                    if ((avsCourantVal >= fromAvsVal) && (avsCourantVal <= toAvsVal)) {
                        transaction.disableSpy();
                        ecritureCourante.setFromFacturationCompta(isFromFacturationCompta);
                        ecritureCourante.comptabiliser(transaction, dateCompta, true, true);
                        transaction.enableSpy();
                        // ajout du no de ci dans la liste
                        if (!transaction.hasErrors()) {
                            transaction.commit();
                        } else {
                            // continuer avec les autre ecritures
                            errorBuffer.append(ecritureCourante.getAvs() + " " + transaction.getErrors());
                            transaction.clearErrorBuffer();
                        }
                    }
                    if (process.isAborted()) {
                        errorBuffer.append(getSession().getLabel("MSG_DERNIER_NUMERO_TRAITE") + " "
                                + ecritureCourante.getAvs());
                        return errorBuffer;

                    }
                }

            }
            if (!transaction.hasErrors()) {
                // mettre à jour l'état
                this.retrieve(transaction);
                // Existe t'il encore des ecritures TEMPORAIRE ou en SUSPENS?
                inscrire(transaction);
                wantCallMethodBefore(false);
                this.updateInscription(transaction);
                this.update(transaction);
                wantCallMethodBefore(true);
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
        return errorBuffer;
    }

    public String getAnneeCotisation() {
        return anneeCotisation;
    }

    /**
     * Method getBrancheEconomique.
     * 
     * @return String
     */
    public String getBrancheEconomique() {
        if (!JadeStringUtil.isBlank(getNumeroAffilie())) {
            AFAffiliationManager affMgr = new AFAffiliationManager();
            try {
                affMgr.setSession(getSession());
                affMgr.setForAffilieNumero(getNumeroAffilie());
                affMgr.find();
                if (!affMgr.isEmpty()) {
                    return ((AFAffiliation) affMgr.getFirstEntity()).getBrancheEconomique();
                }
            } catch (Exception ex) {
                // retour vide
            }
        }
        return "";
    }

    public String getCorrectionSpeciale() {
        return correctionSpeciale;
    }

    /**
     * Retourne le montant de cotisation sous un format à apostrophe
     * 
     * @return java.lang.String
     */
    public String getCorrectionSpecialeFormat() {
        return new FWCurrency(getCorrectionSpeciale()).toStringFormat();
    }

    public int getCounterEcriture() {
        return counterEcriture;
    }

    public String getDate() {
        return date;
    }

    public String getDateFormatee() {
        if (CIJournal.CS_OUVERT.equals(getIdEtat())) {
            return getDate();
        } else {
            return dateInscription;
        }
    }

    public String getDateInscription() {
        return dateInscription;
    }

    public String getDateReception() {
        return dateReception;
    }

    /**
     * Retourne la description du journal: ID journal, ID affilié ou libellé
     */
    public String getDescription() {
        // String description = new String();
        // La description contient: l'ID,
        // description = getIdJournal();
        // le nom de l'affilié et sa ville, si renseigné
        if (!JadeStringUtil.isIntegerEmpty(getIdAffiliation())) {
            // Connection à l'affilié
            // pour des questions de performances, on utilise le join
            return getNumeroAffilie() + " " + getInfoAffilie();
        }
        // Sinon le libellé
        return getLibelle();
    }

    public String getEcrituresPresentes() throws Exception {
        if (isNew()) {
            return "False";
        }
        CIEcritureCounter mgr = new CIEcritureCounter();
        mgr.setSession(getSession());
        mgr.setForIdJournal(getIdJournal());
        // int count = mgr.getCount(statement.getTransaction());
        mgr.wantCallMethodAfter(false);
        mgr.changeManagerSize(1);
        mgr.find();
        if (mgr.size() > 0) {
            // if (JAUtil.isStringEmpty(ecrituresFound)) {
            return "True";
        } else {
            return "False";
        }
    }

    public String getEstVerrouille() {
        return estVerrouille;
    }

    public int getEtatEntity() {
        return etatEntity;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdEtat() {
        return idEtat;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdRemarque() {
        return idRemarque;
    }

    public String getIdTypeCompte() {
        return idTypeCompte;
    }

    public String getIdTypeInscription() {
        return idTypeInscription;
    }

    /**
     * Renvoie le nom (et son prénom) de l'affilié
     * 
     * @return java.lang.String
     */
    public String getInfoAffilie() {
        String infoAffilie = getNomTiers();
        if (!JadeStringUtil.isBlank(infoAffilie)) {
            infoAffilie += " " + getPrenomTiers();
        } else {
            if (!"".equals(getNumeroAffilie())) {
                AFAffiliationManager afMgr = new AFAffiliationManager();
                afMgr.setForAffilieNumero(getNumeroAffilie());
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
            }
        }
        return infoAffilie;
    }

    public String getLibelle() {
        return libelle;
    }

    public FWMemoryLog getMemoryLog() {
        return memoryLog;
    }

    public String getMotifCorrection() {
        return motifCorrection;
    }

    public String getNomTiers() {
        return nomTiers;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public String getPrenomTiers() {
        return prenomTiers;
    }

    public String getProprietaire() {
        if (isNew()) {
            String lUser = getSession().getUserId();
            if ((lUser != null) && (lUser.length() != 0)) {
                proprietaire = lUser;
            }
        }
        return proprietaire;
    }

    /**
     * Returns the proprietaireNomComplet.
     * 
     * @return String
     */
    public String getProprietaireNomComplet() {
        String result = getProprietaire();
        if (!JadeStringUtil.isBlank(result)) {
            // non vide -> recherche
            FWSecureUser user = new FWSecureUser();
            user.setSession(getSession());
            user.setUser(getProprietaire());
            try {
                user.retrieve();
                if (!user.hasErrors()) {
                    result = user.getFirstname() + " " + user.getLastname();
                }
                if (JadeStringUtil.isBlank(result)) {
                    // si utilisateur trouvé mais sans nom -> id
                    result = getProprietaire();
                }
            } catch (Exception e) {
                // laisser userid
            }
        }
        return result;
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    /**
     * Returns the refExterneFacturation.
     * 
     * @return String
     */
    public String getRefExterneFacturation() {
        return refExterneFacturation;
    }

    public java.lang.String getRemTexte() {
        return remTexte;
    }

    public String getSoldeJournal() {
        FWCurrency solde = new FWCurrency(getTotalControle());
        solde.sub(new FWCurrency(getTotalInscrit()));
        return solde.toStringFormat();
    }

    public String getTotalControle() {
        return totalControle;
    }

    public String getTotalControleFormat() {
        return new FWCurrency(getTotalControle()).toStringFormat();
    }

    public String getTotalInscrit() {
        return totalInscrit;
    }

    /**
     * Retourne le total des revenus inscrits moins la correction
     */
    public String getTotalInscritCorrigeFormat() {
        FWCurrency totalInscritCorrige = new FWCurrency(getTotalInscrit());
        totalInscritCorrige.sub(new FWCurrency(getCorrectionSpeciale()));
        return totalInscritCorrige.toStringFormat();
    }

    public String getTotalInscritFormat() {
        return new FWCurrency(getTotalInscrit()).toStringFormat();
    }

    public boolean hasEcrtiures() {
        CIEcritureManager mgr = new CIEcritureManager();
        mgr.setForIdJournal(getIdJournal());
        mgr.setSession(getSession());
        try {
            mgr.find();
        } catch (Exception e) {
            return false;
        }
        if (mgr.size() == 0) {
            return false;
        } else {
            return true;
        }

    }

    public void incCounter() {
        counterEcriture++;
    }

    public void inscrire(BITransaction transaction) {
        CIEcritureManager ecritureMngBis = new CIEcritureManager();
        ecritureMngBis.setSession(getSession());
        ecritureMngBis.setForIdJournal(getIdJournal());
        ecritureMngBis.setForIdTypeCompteList(new String[] { CIEcriture.CS_CI_SUSPENS,
                CIEcriture.CS_TEMPORAIRE_SUSPENS, CIEcriture.CS_TEMPORAIRE });
        try {
            if (ecritureMngBis.getCount(transaction) > 0) {
                setIdEtat(CIJournal.CS_PARTIEL);
            } else {
                setIdEtat(CIJournal.CS_COMPTABILISE);
            }
            if (JadeStringUtil.isBlank(getDateInscription())) {
                setDateInscription(JACalendar.todayJJsMMsAAAA());
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

    }

    public boolean isFromFacturationCompta() {
        return isFromFacturationCompta;
    }

    /**
     * Révoque le journal (splitting). Date de création : (16.05.2003 08:35:39)
     * 
     * @param transaction
     *            la transaction à utiliser
     * @exception Exception
     *                si une erreur survient
     */
    public void revoquerJournalSplitting(BTransaction transaction) throws Exception {
        // efface les écritures associées sans test
        // todo: effacer les liens égalements (CILRAEP)
        CIEcritureManager ecrMng = new CIEcritureManager();
        ecrMng.setSession(getSession());
        if (JadeStringUtil.isBlank(getIdJournal())) {
            return;
        }
        ecrMng.setForIdJournal(getIdJournal());
        ecrMng.find(transaction, BManager.SIZE_NOLIMIT);
        for (int i = 0; i < ecrMng.size(); i++) {
            CIEcriture ecr = (CIEcriture) ecrMng.getEntity(i);
            ecr.wantCallMethodBefore(false);
            ecr.simpleDelete(transaction);
        }
        // effacer la remarque
        if (!JadeStringUtil.isIntegerEmpty(getIdRemarque())) {
            CIRemarque rem = new CIRemarque();
            rem.setSession(getSession());
            rem.setIdRemarque(getIdRemarque());
            rem.delete(transaction);
        }
        // efface le journal sans test
        wantCallMethodBefore(false);
        this.delete(transaction);
    }

    public void setAnneeCotisation(String newAnneeCotisation) {
        anneeCotisation = newAnneeCotisation;
    }

    public void setCorrectionSpeciale(String newCorrectionSpeciale) {
        correctionSpeciale = new FWCurrency(newCorrectionSpeciale).toString();
    }

    public void setDate(String newDate) {
        date = newDate;
    }

    public void setDateInscription(String newDateInscription) {
        dateInscription = newDateInscription;
    }

    public void setDateReception(String newDateReception) {
        dateReception = newDateReception;
    }

    public void setEstVerrouille(String newEstVerrouille) {
        estVerrouille = newEstVerrouille;
    }

    public void setEtatEntity(int newEtatEntity) {
        etatEntity = newEtatEntity;
    }

    public void setFromFacturationCompta(boolean isFromFacturationCompta) {
        this.isFromFacturationCompta = isFromFacturationCompta;
    }

    /**
     * set l'id de l'affilié en fonction du numéro d'affilié passé en paramètre (affilié paritaire seulement)
     * 
     * @param affilieNumero
     */
    public void setIdAffiliation(String affilieNumero) {
        this.setIdAffiliation(affilieNumero, true, false);
    }

    /**
     * set l'id de l'affilié en fonction du numéro d'affilié passé en paramètre et du type d'affiliation (par ou pers)
     * 
     * @param newIdAffiliation
     * @param forTypeAffPar
     *            indique si on filtre les affiliés sur le type paritaire
     * @param forTypeAffPers
     *            indique si on filtre les affiliés sur le type personnel
     */
    public void setIdAffiliation(String newIdAffiliation, boolean forTypeAffPar, boolean forTypeAffPers) {
        if (!JadeStringUtil.isBlank(newIdAffiliation)) {
            // converti l'id affilié en id tiers
            try {
                CIApplication app = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                        CIApplication.DEFAULT_APPLICATION_PAVO);

                AFAffiliation aff = app.getAffilieByNo(getSession(), newIdAffiliation, forTypeAffPar, forTypeAffPers,
                        null, null, getAnneeCotisation(), "", "");

                if (aff != null) {
                    idAffiliation = aff.getAffiliationId();
                    // test période avec affiliation

                    if (!checkPerdiodeAff(aff.getDateDebut(), aff.getDateFin())) {
                        affError = getSession().getLabel("MSG_ECRITURE_AFF_PERIODE");
                        // idAffiliation="";
                    } else {
                    }

                    return;
                } else {
                    affError = getSession().getLabel("MSG_ECRITURE_AFF_PERIODE");
                }
            } catch (Exception ex) {
                // prendre le paramètre comme id
            }
        }
        idAffiliation = newIdAffiliation;
    }

    public void setIdEtat(String newIdEtat) {
        idEtat = newIdEtat;
    }

    public void setIdJournal(String newIdJournal) {
        idJournal = newIdJournal;
    }

    public void setIdRemarque(String newIdRemarque) {
        idRemarque = newIdRemarque;
    }

    public void setIdTypeCompte(String newIdTypeCompte) {
        idTypeCompte = newIdTypeCompte;
    }

    public void setIdTypeInscription(String newIdTypeInscription) {
        idTypeInscription = newIdTypeInscription;
    }

    public void setLibelle(String newLibelle) {
        libelle = newLibelle;
    }

    public void setMemoryLog(FWMemoryLog newMemoryLog) {
        memoryLog = newMemoryLog;
    }

    public void setMotifCorrection(String newMotifCorrection) {
        motifCorrection = newMotifCorrection;
    }

    public void setNomTiers(String newNomTiers) {
        nomTiers = newNomTiers;
    }

    public void setNumeroAffilie(String newNumeroAffilie) {
        numeroAffilie = newNumeroAffilie;
    }

    public void setPrenomTiers(String newPrenomTiers) {
        prenomTiers = newPrenomTiers;
    }

    public void setProprietaire(String newProprietaire) {
        proprietaire = newProprietaire;
    }

    public void setReferenceExterne(String newReferenceExterne) {
        referenceExterne = newReferenceExterne;
    }

    /**
     * Sets the refExterneFacturation.
     * 
     * @param refExterneFacturation
     *            The refExterneFacturation to set
     */
    public void setRefExterneFacturation(String refExterneFacturation) {
        this.refExterneFacturation = refExterneFacturation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 10:09:42)
     * 
     * @param newRemTexte
     *            java.lang.String
     */
    public void setRemTexte(java.lang.String newRemTexte) {
        remTexte = newRemTexte;
    }

    public void setTotalControle(String newTotalControle) {
        totalControle = new FWCurrency(newTotalControle).toString();
    }

    public void setTotalInscrit(String newTotalInscrit) {
        totalInscrit = new FWCurrency(newTotalInscrit).toString();
    }

    /**
     * Met à jour les total des revenus inscrits
     * 
     * @param Un
     *            objet BTransaction
     * @param Un
     *            object FWCurrency contenant la valeur à ajouter au total des revenus inscrits
     * @return le nouveau total des revenus.
     */
    public void updateInscription(BTransaction transaction) throws Exception {
        CIEcrituresSomme somme = new CIEcrituresSomme();
        somme.setSession(getSession());
        somme.setForIdJournal(getIdJournal());
        BigDecimal result = somme.getSum("KBMMON", transaction);
        if (result != null) {
            setTotalInscrit(result.toString());
            this.update(transaction);
            // return result.toString();
        } else {
            // return getTotalInscrit();
        }
    }

    /**
     * Met à jour les total des revenus inscrits
     * 
     * @param Un
     *            objet BTransaction
     * @param Un
     *            object FWCurrency contenant la valeur à ajouter au total des revenus inscrits
     * @return le nouveau total des revenus.
     */
    public void updateInscription(BTransaction transaction, String totalControleSaisie) throws Exception {
        CIEcrituresSomme somme = new CIEcrituresSomme();
        somme.setSession(getSession());
        somme.setForIdJournal(getIdJournal());
        BigDecimal result = somme.getSum("KBMMON", transaction);
        if (result == null) {
            result = new BigDecimal("0.00");
        }
        setTotalInscrit(result.toString());
        if (!JadeStringUtil.isIntegerEmpty(totalControleSaisie)) {
            setTotalControle(totalControleSaisie);
        }
        this.update(transaction);
        // return result.toString();
    }

    /**
     * Met à jour les total des revenus inscrits
     * 
     * @param Un
     *            objet BTransaction
     * @param Un
     *            object FWCurrency contenant la valeur à ajouter au total des revenus inscrits
     * @return le nouveau total des revenus.
     */
    public void updateInscriptionWith(BTransaction transaction, FWCurrency newMontant, String totalControleSaisie)
            throws Exception {
        FWCurrency total = new FWCurrency(getTotalInscrit());
        total.add(newMontant);
        setTotalInscrit(total.toStringFormat());
        if (!JadeStringUtil.isIntegerEmpty(totalControleSaisie)) {
            setTotalControle(totalControleSaisie);
        }
        this.update(transaction);
        // return result.toString();
    }

    /**
     * Validation des données entrées en ajout/mise à jour d'un journal.
     * 
     * @param Un
     *            objet BTransaction
     */
    public void validateInputs(BTransaction transaction) throws java.lang.Exception {
        // Le type de journal est obligatoire
        _propertyMandatory(transaction, getIdTypeInscription(), getSession().getLabel("MSG_JOURNAL_ADDUP_TYPE"));

        // Affilié obligatoire pour DECLARATION_SALAIRES ou de
        // DECLARATION_COMPLEMENTAIRE ou CONTROLE_EMPLOYEUR
        if (CIJournal.CS_DECLARATION_SALAIRES.equals(getIdTypeInscription())
                || CIJournal.CS_DECLARATION_COMPLEMENTAIRE.equals(getIdTypeInscription())
                || CIJournal.CS_CONTROLE_EMPLOYEUR.equals(getIdTypeInscription())
                || CIJournal.CS_CORRECTIF.equals(getIdTypeInscription())) {
            _propertyMandatory(transaction, getIdAffiliation(), getSession().getLabel("MSG_JOURNAL_ADDUP_AFF"));
        } else if (!(CIJournal.CS_CORRECTIF.equals(getIdTypeInscription()))
                && !(CIJournal.CS_COTISATIONS_PERSONNELLES.equals(getIdTypeInscription()))) {
            if (!JadeStringUtil.isIntegerEmpty(getIdAffiliation())) {
                _addError(transaction, getSession().getLabel("MSG_JOURNAL_ADDUP_NOAFF"));
            }
        }
        // ************ AJOUTER LE CODE DE VERIFICATION VELA
        // Année de cotisation minimale: 1948
        if ((!JadeStringUtil.isIntegerEmpty(getAnneeCotisation()))
                && (new Integer(getAnneeCotisation()).intValue() < 1948)) {
            _addError(transaction, getSession().getLabel("MSG_JOURNAL_ADDUP_ANCOTOLOW"));
        }
        // Type de compte obligatoire
        _propertyMandatory(transaction, getIdTypeCompte(), getSession().getLabel("MSG_JOURNAL_ADDUP_TYPCOM"));

        // La correction spéciale n'est valide que si un total de revenus pour
        // contrôle est spécifié
        if ((!JadeStringUtil.isIntegerEmpty(getCorrectionSpeciale()))
                && (JadeStringUtil.isIntegerEmpty(getTotalControle()))) {
            _addError(transaction, getSession().getLabel("MSG_JOURNAL_ADDUP_CORREC"));
        }
        // Motif obligatoire si correction spéciale
        if (!JadeStringUtil.isIntegerEmpty(getCorrectionSpeciale())) {
            if (JadeStringUtil.isBlank(getMotifCorrection())) {
                _addError(transaction, getSession().getLabel("MSG_JOURNAL_ADDUP_MOTIF"));
            }
        } else {
            if (!JadeStringUtil.isBlank(getMotifCorrection())) {
                _addError(transaction, getSession().getLabel("MSG_JOURNAL_ADDUP_NOMOTIF"));
            }
        }
    }
}
