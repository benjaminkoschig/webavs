package globaz.osiris.db.comptes;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWLog;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIJournal;
import globaz.osiris.api.APIOperation;
import globaz.osiris.external.IntJournalCG;
import globaz.osiris.print.itext.list.CAIListJournalEcritures_Doc;
import globaz.osiris.process.journal.CAAnnulerJournal;
import globaz.osiris.process.journal.CAUtilsJournal;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Insérez la description du type ici. Date de création : (12.12.2001 09:25:51)
 * 
 * @author: Administrator
 */
public class CAJournal extends BEntity implements Serializable, APIJournal {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String ANNULE = "202005";

    public final static String COMPTABILISE = "202002";
    public final static String ERREUR = "202004";
    public static final String FIELD_ACCEPTERERREURS = "ACCEPTERERREURS";
    public static final String FIELD_DATE = "DATE";
    public static final String FIELD_DATEVALEURCG = "DATEVALEURCG";
    public static final String FIELD_ESTCONFIDENTIEL = "ESTCONFIDENTIEL";
    public static final String FIELD_ESTPUBLIC = "ESTPUBLIC";
    public static final String FIELD_ESTVISIMM = "ESTVISIMM";
    public static final String FIELD_ETAT = "ETAT";
    public static final String FIELD_IDJOURNAL = "IDJOURNAL";
    public static final String FIELD_IDLOG = "IDLOG";
    public static final String FIELD_IDPOSJOU = "IDPOSJOU";
    public static final String FIELD_LIBELLE = "LIBELLE";
    public static final String FIELD_NOJOURNALCG = "NOJOURNALCG";

    public static final String FIELD_PROPRIETAIRE = "PROPRIETAIRE";
    public static final String FIELD_TYPEJOURNAL = "TYPEJOURNAL";
    public final static String OUVERT = "202001";
    public final static String PARTIEL = "202003";
    public static final String TABLE_CAJOURP = "CAJOURP";
    public final static String TRAITEMENT = "202006";

    public final static String TYPE_FACTURATION = "203009";
    public final static String TYPE_AUTOMATIQUE = "203001";
    public final static String TYPE_BULLETIN_NEUTRE = "203008";
    public final static String TYPE_CONTENTIEUX = "203004";
    public final static String TYPE_JOURNALIER = "203006";
    public final static String TYPE_JOURNALIER_CONTENTIEUX = "203007";
    public final static String TYPE_MANUEL = "203002";
    public final static String TYPE_SYSTEM = "203005";
    public final static String TYPE_TEMPORAIRE = "203003";

    /**
     * Cette méthode retourne un journal journalier par user de type journalier
     * 
     * @param session
     * @param transaction
     * @return CAJournal
     * @throws Exception
     */
    public static CAJournal fetchJournalJournalier(BSession session, BITransaction transaction) throws Exception {
        return CAJournal.fetchJournalJournalier(session, transaction, CAJournal.TYPE_JOURNALIER);
    }

    /**
     * Cette méthode retourne un journal journalier par user à choix (journalier, journalier contentieux, etc.) Si le
     * type de journal passé en paramètre n'existe pas on prend par défaut le type journalier
     * 
     * @param session
     * @param transaction
     * @param typeJournal
     * @return CAJournal
     * @throws Exception
     */
    public static CAJournal fetchJournalJournalier(BSession session, BITransaction transaction, String typeJournal)
            throws Exception {
        if (!typeJournal.equalsIgnoreCase(CAJournal.TYPE_JOURNALIER_CONTENTIEUX)) {
            typeJournal = CAJournal.TYPE_JOURNALIER;
        }
        CAJournal journalier = null;
        // Récupérer le journal de type "écritures journalières" en état
        // "Ouvert" pour la date du jour
        CAJournalManager mgr = new CAJournalManager();
        mgr.setSession(session);
        mgr.setForTypeJournal(typeJournal);
        mgr.setForProprietaire(session.getUserName());
        mgr.setForEtat(CAJournal.OUVERT);
        mgr.setForDateValeurCG(JACalendar.today().toStr("."));
        mgr.find(transaction);
        // Si trouvé, on renvoie le journal
        if (!mgr.isEmpty()) {
            journalier = (CAJournal) mgr.getEntity(0);
            // Sinon, ouvrir un nouveau journal
        } else {
            journalier = new CAJournal();
            journalier.setSession(session);
            journalier.setDateValeurCG(JACalendar.today().toStr("."));
            journalier.setEstConfidentiel(Boolean.FALSE);
            journalier.setEstPublic(Boolean.TRUE);
            journalier.setEstVisibleImmediatement(Boolean.TRUE);
            if (typeJournal.equalsIgnoreCase(CAJournal.TYPE_JOURNALIER)) {
                journalier.setLibelle(session.getLabel("ECRITURE_JOURNALIERE"));
            } else if (typeJournal.equalsIgnoreCase(CAJournal.TYPE_JOURNALIER_CONTENTIEUX)) {
                journalier.setLibelle(session.getLabel("ECRITURE_JOURNALIERE_CONTENTIEUX"));
            }
            journalier.setTypeJournal(typeJournal);
            journalier.add(transaction);
        }
        // Retourner le journal
        return journalier;
    }

    private FWLog _log = null;
    private FWMemoryLog _memLog = null;
    private Boolean accepterErreurs = new Boolean(false);
    private FWParametersSystemCode csEtat = null;
    private FWParametersSystemCodeManager csEtats = null;
    private FWParametersSystemCode csTypeJournal = null;
    private FWParametersSystemCodeManager csTypeJournals = null;
    private String date = new String();
    private String dateValeurCG = new String();
    private Boolean estConfidentiel = new Boolean(false);
    private Boolean estPublic = new Boolean(false);
    private Boolean estVisibleImmediatement = new Boolean(false);
    private String etat = new String();
    private String idJournal = new String();
    private String idLog = new String();
    private String idPosteJournalisation = new String();
    private String libelle = new String();
    private String noJournalCG = new String();
    private String proprietaire = new String();
    private String typeJournal = new String();
    private CAUtilsJournal utils = new CAUtilsJournal();
    // code systeme

    private FWParametersUserCode ucEtat = null;

    private FWParametersUserCode ucTypeJournal = null;

    /**
     * Commentaire relatif au constructeur CAJournal
     */
    public CAJournal() {
        super();
    }

    @Override
    protected void _afterDelete(BTransaction transaction) throws java.lang.Exception {
        // Supprimer le log s'il existe
        if (getLog() != null) {
            getLog().delete(transaction);
            if (transaction.hasErrors()) {
                _addError(transaction, getSession().getLabel("7000") + " #" + getIdLog());
            } else {
                setIdLog("0");
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        if (getEtat().equals(CAJournal.ERREUR) && JadeStringUtil.isIntegerEmpty(getNoJournalCG())) {
            CAOperationManager manager = new CAOperationManager();
            manager.setSession(getSession());

            manager.setForEtat(APIOperation.ETAT_ERREUR);
            manager.setForIdJournal(getIdJournal());

            if (manager.getCount() == 0) {
                setEtat(CAJournal.OUVERT);
                this.update();
            }
        }
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incrémente le prochain numéro
        setIdJournal(this._incCounter(transaction, idJournal));

        // propriétaire = user si pas mentionné
        if (JadeStringUtil.isBlank(getProprietaire())) {
            setProprietaire(getSession().getUserName());
        }

        // Date du jour si date pas mentionnée
        if (JAUtil.isDateEmpty(getDate())) {
            setDate(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
        }

        // Type de journal = manuel par défaut
        if (JadeStringUtil.isIntegerEmpty(getTypeJournal())) {
            setTypeJournal(CAJournal.TYPE_MANUEL);
        }

        // Etat du journal en fonction du type
        if (JadeStringUtil.isBlank(getEtat())) {
            if (getTypeJournal().equals(CAJournal.TYPE_MANUEL) || getTypeJournal().equals(CAJournal.TYPE_JOURNALIER)
                    || CAJournal.TYPE_CONTENTIEUX.equals(getTypeJournal())
                    || CAJournal.TYPE_JOURNALIER_CONTENTIEUX.equals(getTypeJournal())) {
                setEtat(CAJournal.OUVERT);
            } else {
                setEtat(CAJournal.TRAITEMENT);
            }
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.01.2002 09:35:55)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws java.lang.Exception {

        // vérification de l'état du journal
        if (!getEtat().equals(CAJournal.ANNULE)) {
            _addError(transaction, getSession().getLabel("4001"));
        }

        // vérification que le propriétaire = user ou le journal doit-être
        // public
        if (!getProprietaire().equalsIgnoreCase(getSession().getUserName()) && !getEstPublic().booleanValue()) {
            _addError(null, getSession().getLabel("4002"));
        }

        // vérification que le journal ne contienne pas d'opérations
        if (hasOperations()) {
            _addError(transaction, getSession().getLabel("4003"));
        }

    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws java.lang.Exception {

        // Sauvegarder le log mémoire s'il existe et s'il y a des messages
        if (_memLog != null) {
            // Supprimer le log existant
            if (getLog() != null) {
                getLog().delete(transaction);
                if (transaction.hasErrors()) {
                    _addError(transaction, getSession().getLabel("7000") + " #" + getIdLog());
                } else {
                    setIdLog("0");
                }
                // En cas d'erreur, on signale que la sauvegarde du log a échoué
                if (transaction.hasErrors()) {
                    _addError(transaction, getSession().getLabel("5011"));
                }
            }
            // Vérifier s'il y a des messages
            if (_memLog.hasMessages()) {
                // Demander la sauvegarde
                _log = _memLog.saveToFWLog(transaction);

                // En cas d'erreur, on signale que la sauvegarde du log a échoué
                if (transaction.hasErrors()) {
                    _addError(transaction, getSession().getLabel("5011"));
                    _log = null;
                }

                // Si la sauvegarde a réussi
                if (_log != null) {
                    setIdLog(_log.getIdLog());
                }
            }
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return CAJournal.TABLE_CAJOURP;
    }

    /**
     * Retourne le total des écritures contenues dans le journal. <br/>
     * Utiliser dans l'écran de détail et par AF.
     * 
     * @return
     */
    @Override
    public String _getTotalEcritures() {
        if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            return "";
        }

        try {
            CAEcritureManager manager = new CAEcritureManager();
            manager.setSession(getSession());

            manager.setForIdJournal(getIdJournal());

            ArrayList idTypeOperationLike = new ArrayList();
            idTypeOperationLike.add(APIOperation.CAECRITURE);
            manager.setForIdTypeOperationLikeIn(idTypeOperationLike);

            ArrayList etatNotIn = new ArrayList();
            etatNotIn.add(APIOperation.ETAT_INACTIF);
            manager.setForEtatNotIn(etatNotIn);

            FWCurrency totalEcritures = new FWCurrency(manager.getSum(CAOperation.FIELD_MONTANT).doubleValue());

            return totalEcritures.toStringFormat();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Retourne le total des ordres de recouvrement contenus dans le journal. <br/>
     * Utiliser dans l'écran de détail et par AF.
     * 
     * @return
     */
    public String _getTotalOrdreRecouvrement() {
        if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            return "";
        }

        try {
            CAEcritureManager manager = new CAEcritureManager();
            manager.setSession(getSession());

            manager.setForIdJournal(getIdJournal());

            ArrayList idTypeOperationLike = new ArrayList();
            idTypeOperationLike.add(APIOperation.CAOPERATIONORDRERECOUVREMENT);
            manager.setForIdTypeOperationLikeIn(idTypeOperationLike);

            ArrayList etatNotIn = new ArrayList();
            etatNotIn.add(APIOperation.ETAT_INACTIF);
            manager.setForEtatNotIn(etatNotIn);

            FWCurrency totalEcritures = new FWCurrency(manager.getSum(CAOperation.FIELD_MONTANT).doubleValue());

            return totalEcritures.toStringFormat();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Retourne le total des ordres de versements contenus dans le journal. <br/>
     * Utiliser dans l'écran de détail et par AF.
     * 
     * @return
     */
    public String _getTotalOrdreVersement() {
        if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            return "";
        }

        try {
            CAEcritureManager manager = new CAEcritureManager();
            manager.setSession(getSession());

            manager.setForIdJournal(getIdJournal());

            ArrayList idTypeOperationLike = new ArrayList();
            idTypeOperationLike.add(APIOperation.CAOPERATIONORDREVERSEMENT);
            idTypeOperationLike.add(APIOperation.CAOPERATIONORDREVERSEMENTAVANCE);
            manager.setForIdTypeOperationLikeIn(idTypeOperationLike);

            ArrayList etatNotIn = new ArrayList();
            etatNotIn.add(APIOperation.ETAT_INACTIF);
            manager.setForEtatNotIn(etatNotIn);

            FWCurrency totalEcritures = new FWCurrency(manager.getSum(CAOperation.FIELD_MONTANT).doubleValue());

            return totalEcritures.toStringFormat();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idJournal = statement.dbReadNumeric(CAJournal.FIELD_IDJOURNAL);
        idPosteJournalisation = statement.dbReadNumeric(CAJournal.FIELD_IDPOSJOU);
        libelle = statement.dbReadString(CAJournal.FIELD_LIBELLE);
        date = statement.dbReadDateAMJ(CAJournal.FIELD_DATE);
        etat = statement.dbReadNumeric(CAJournal.FIELD_ETAT);
        estPublic = statement.dbReadBoolean(CAJournal.FIELD_ESTPUBLIC);
        estVisibleImmediatement = statement.dbReadBoolean(CAJournal.FIELD_ESTVISIMM);
        noJournalCG = statement.dbReadNumeric(CAJournal.FIELD_NOJOURNALCG);
        dateValeurCG = statement.dbReadDateAMJ(CAJournal.FIELD_DATEVALEURCG);
        accepterErreurs = statement.dbReadBoolean(CAJournal.FIELD_ACCEPTERERREURS);
        proprietaire = statement.dbReadString(CAJournal.FIELD_PROPRIETAIRE);
        estConfidentiel = statement.dbReadBoolean(CAJournal.FIELD_ESTCONFIDENTIEL);
        idLog = statement.dbReadNumeric(CAJournal.FIELD_IDLOG);
        typeJournal = statement.dbReadNumeric(CAJournal.FIELD_TYPEJOURNAL);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.03.2002 10:04:09)
     * 
     * @param context
     *            BProcess
     */
    private void _supprimerComptesAnnexes(BProcess context) throws Exception {

        // Initialiser
        String _lastIdCompteAnnexe = "";

        // Lecture des sections
        CACompteAnnexeManager cMgr = new CACompteAnnexeManager();
        cMgr.setSession(getSession());
        cMgr.setOrderBy(CACompteAnnexeManager.ORDER_IDCOMPTEANNEXE);

        // Récupérer les opérations associées au journal
        cMgr.setForIdJournal(getIdJournal());

        // Boucle de lecture
        while (true) {

            cMgr.clear();
            cMgr.find(context.getTransaction());

            // Sortir s'il n'y a aucune opération trouvée
            if (cMgr.size() == 0) {
                break;
            }

            // Récupérer les sections
            for (int i = 0; i < cMgr.size(); i++) {

                // Récupérer les sections
                CACompteAnnexe _cpt = (CACompteAnnexe) cMgr.getEntity(i);

                // Sauver dernier id
                _lastIdCompteAnnexe = _cpt.getIdCompteAnnexe();

                // Vérifier la condition de sortie
                if (context.isAborted()) {
                    return;
                }

                // Vérifier s'il y a des opérations
                _cpt.delete(context.getTransaction());
                if (_cpt.hasErrors()) {
                    _addError(null, getSession().getLabel("5041") + " " + _cpt.getIdCompteAnnexe());
                    return;
                }
            }

            cMgr.setAfterIdCompteAnnexe(_lastIdCompteAnnexe);

        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.03.2002 10:04:09)
     * 
     * @param context
     *            BProcess
     */
    private void _supprimerSections(BProcess context) throws Exception {

        // Initialiser
        String _lastIdSection = "";

        // Lecture des sections
        CASectionManager sMgr = new CASectionManager();
        sMgr.setSession(getSession());
        sMgr.setOrderBy(CASectionManager.ORDER_IDSECTION);

        // Récupérer les opérations associées au journal
        sMgr.setForIdJournal(getIdJournal());

        // Boucle de lecture
        while (true) {

            sMgr.clear();
            sMgr.find(context.getTransaction());

            // Sortir s'il n'y a aucune opération trouvée
            if (sMgr.size() == 0) {
                break;
            }

            // Récupérer les sections
            for (int i = 0; i < sMgr.size(); i++) {

                // Récupérer les sections
                CASection _sec = (CASection) sMgr.getEntity(i);

                // Sauver dernier id
                _lastIdSection = _sec.getIdSection();

                // Vérifier la condition de sortie
                if (context.isAborted()) {
                    return;
                }

                // Supprimer la section
                _sec.delete(context.getTransaction());
                if (_sec.hasErrors()) {
                    _addError(null, getSession().getLabel("5040") + " " + _sec.getIdSection());
                    return;
                }
            }

            // Positionner sur la prochaine section
            sMgr.setAfterIdSection(_lastIdSection);

        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
        _propertyMandatory(statement.getTransaction(), getIdJournal(), getSession().getLabel("7023"));
        _propertyMandatory(statement.getTransaction(), getLibelle(), getSession().getLabel("7014"));
        _propertyMandatory(statement.getTransaction(), getDate(), getSession().getLabel("7007"));
        _propertyMandatory(statement.getTransaction(), getDateValeurCG(), getSession().getLabel("5327"));
        _propertyMandatory(statement.getTransaction(), getProprietaire(), getSession().getLabel("7016"));
        _checkDate(statement.getTransaction(), getDate(), getSession().getLabel("7005"));
        _checkDate(statement.getTransaction(), getDateValeurCG(), getSession().getLabel("7003"));
        _propertyMandatory(statement.getTransaction(), getEtat(), getSession().getLabel("7022"));
        _propertyMandatory(statement.getTransaction(), getTypeJournal(), getSession().getLabel("7018"));

        // Vérifier le code système état
        if (getCsEtats().getCodeSysteme(getEtat()) == null) {
            _addError(statement.getTransaction(), getSession().getLabel("7021"));
        }

        // Vérifier le code système type de journal
        if (getCsTypeJournals().getCodeSysteme(getTypeJournal()) == null) {
            _addError(statement.getTransaction(), getSession().getLabel("7019"));
        }
        // bug 6474
        // Vérification que la période comptable est ouverte pour le traitement
        if (utils.isInterfaceCgActive(getSession())
                && !utils.isPeriodeComptableOuverte(getSession(), statement.getTransaction(), getDateValeurCG())) {
            getMemoryLog().logMessage(statement.getTransaction().getErrors().toString(), FWMessage.FATAL,
                    this.getClass().getName());
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(CAJournal.FIELD_IDJOURNAL,
                this._dbWriteNumeric(statement.getTransaction(), getIdJournal(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(CAJournal.FIELD_IDJOURNAL,
                this._dbWriteNumeric(statement.getTransaction(), getIdJournal(), "idJournal"));
        statement.writeField(CAJournal.FIELD_IDPOSJOU,
                this._dbWriteNumeric(statement.getTransaction(), getIdPosteJournalisation(), "idPosJou"));
        statement.writeField(CAJournal.FIELD_LIBELLE,
                this._dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField(CAJournal.FIELD_DATE, this._dbWriteDateAMJ(statement.getTransaction(), getDate(), "date"));
        statement.writeField(CAJournal.FIELD_ETAT, this._dbWriteNumeric(statement.getTransaction(), getEtat(), "etat"));
        statement.writeField(CAJournal.FIELD_ESTPUBLIC, this._dbWriteBoolean(statement.getTransaction(),
                getEstPublic(), BConstants.DB_TYPE_BOOLEAN_CHAR, "estPublic"));
        statement.writeField(CAJournal.FIELD_ESTVISIMM, this._dbWriteBoolean(statement.getTransaction(),
                getEstVisibleImmediatement(), BConstants.DB_TYPE_BOOLEAN_CHAR, "estVisImm"));
        statement.writeField(CAJournal.FIELD_NOJOURNALCG,
                this._dbWriteNumeric(statement.getTransaction(), getNoJournalCG(), "noJournalCG"));
        statement.writeField(CAJournal.FIELD_DATEVALEURCG,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateValeurCG(), "dateValeurCG"));
        statement.writeField(CAJournal.FIELD_ACCEPTERERREURS, this._dbWriteBoolean(statement.getTransaction(),
                accepterErreurs, BConstants.DB_TYPE_BOOLEAN_CHAR, "accepterErreurs"));
        statement.writeField(CAJournal.FIELD_PROPRIETAIRE,
                this._dbWriteString(statement.getTransaction(), getProprietaire(), "proprietaire"));
        statement.writeField(CAJournal.FIELD_ESTCONFIDENTIEL, this._dbWriteBoolean(statement.getTransaction(),
                getEstConfidentiel(), BConstants.DB_TYPE_BOOLEAN_CHAR, "estConfidentiel"));
        statement.writeField(CAJournal.FIELD_IDLOG,
                this._dbWriteNumeric(statement.getTransaction(), getIdLog(), "idLog"));
        statement.writeField(CAJournal.FIELD_TYPEJOURNAL,
                this._dbWriteNumeric(statement.getTransaction(), getTypeJournal(), "typeJournal"));
    }

    /**
     * Insérer une opération dans le journal Date de création : (18.01.2002 12:48:18)
     * 
     * @param oper
     *            globaz.osiris.db.comptes.CAOperation
     * @return FWMessage null s'il n'y a pas d'erreur, sinon un message d'erreur
     */
    public FWMessage addOperation(CAOperation oper) {
        return null;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2002 16:37:11)
     * 
     * @return FWParametersSystemCode
     */
    public FWParametersSystemCode getCsEtat() {

        if (csEtat == null) {
            // liste pas encore chargee, on la charge
            csEtat = new FWParametersSystemCode();
            csEtat.getCode(getEtat());
        }
        return csEtat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2002 16:38:30)
     * 
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsEtats() {
        // liste déjà chargée ?
        if (csEtats == null) {
            // liste pas encore chargée, on la charge
            csEtats = new FWParametersSystemCodeManager();
            csEtats.setSession(getSession());
            csEtats.getListeCodesSup("OSIETAJOU", getSession().getIdLangue());
        }
        return csEtats;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2002 17:07:40)
     * 
     * @return FWParametersSystemCode
     */
    public FWParametersSystemCode getCsTypeJournal() {

        if (csTypeJournal == null) {
            // liste pas encore chargee, on la charge
            csTypeJournal = new FWParametersSystemCode();
            csTypeJournal.getCode(getTypeJournal());
        }
        return csTypeJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2002 17:08:25)
     * 
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsTypeJournals() {
        // liste déjà chargée ?
        if (csTypeJournals == null) {
            // liste pas encore chargée, on la charge
            csTypeJournals = new FWParametersSystemCodeManager();
            csTypeJournals.setSession(getSession());
            csTypeJournals.getListeCodesSup("OSITYPJOU", getSession().getIdLangue());
        }
        return csTypeJournals;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public String getDateValeurCG() {
        return dateValeurCG;
    }

    public Boolean getEstConfidentiel() {
        return estConfidentiel;
    }

    @Override
    public Boolean getEstPublic() {
        return estPublic;
    }

    @Override
    public Boolean getEstVisibleImmediatement() {
        return estVisibleImmediatement;
    }

    @Override
    public String getEtat() {
        return etat;
    }

    /**
     * Getter
     */
    @Override
    public String getIdJournal() {
        return idJournal;
    }

    @Override
    public String getIdLog() {
        return idLog;
    }

    public String getIdPosteJournalisation() {
        return idPosteJournalisation;
    }

    @Override
    public String getLibelle() {
        return libelle;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2002 16:53:13)
     * 
     * @return globaz.osiris.db.utils.FWLog
     */
    public FWLog getLog() {

        // Si le log n'existe pas, retourner null
        if (JadeStringUtil.isIntegerEmpty(getIdLog())) {
            return null;
        }

        // Si log pas déjà chargé
        if (_log == null) {
            // Instancier un nouveau LOG
            _log = new FWLog();
            _log.setSession(getSession());

            // Récupérer le log en question
            _log.setIdLog(getIdLog());
            try {
                _log.retrieve();
                if (_log.isNew()) {
                    _log = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _log = null;
            }
        }

        return _log;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.01.2002 17:48:22)
     * 
     * @return globaz.osiris.db.utils.FWMemoryLog
     */
    public FWMemoryLog getMemoryLog() {
        if (_memLog == null) {
            _memLog = new FWMemoryLog();
            _memLog.setSession(getSession());
        }
        return _memLog;
    }

    public String getNoJournalCG() {
        return noJournalCG;
    }

    public String getNumeroJournalComptaGen() {
        CAUtilsJournal utils = new CAUtilsJournal();
        if (utils.isInterfaceCgActive(getSession())) {
            // Récupérer le journal
            IntJournalCG journalCg = utils.getJournalCG(getSession(), null, getNoJournalCG());
            if (journalCg != null) {
                return journalCg.getNumero();
            } else {
                return IntJournalCG.NUMERO_NO_JOURNAL;
            }
        } else {
            return IntJournalCG.NUMERO_NO_JOURNAL;
        }
    }

    @Override
    public String getProprietaire() {
        return proprietaire;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2002 17:05:25)
     * 
     * @return String
     */
    @Override
    public String getTypeJournal() {
        return typeJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2002 16:36:04)
     * 
     * @return FWParametersUserCode
     */
    public FWParametersUserCode getUcEtat() {

        if (ucEtat == null) {
            // liste pas encore chargee, on la charge
            ucEtat = new FWParametersUserCode();
            ucEtat.setSession(getSession());
            // Récupérer le code système dans la langue de l'utilisateur
            if (!JadeStringUtil.isIntegerEmpty(getEtat())) {
                ucEtat.setIdCodeSysteme(getEtat());
                ucEtat.setIdLangue(getSession().getIdLangue());
                try {
                    ucEtat.retrieve();
                    if (ucEtat.isNew()) {
                        _addError(null, getSession().getLabel("7324"));
                    }
                } catch (Exception e) {
                    _addError(null, getSession().getLabel("7324"));
                }
            }
        }

        return ucEtat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2002 16:33:39)
     * 
     * @return FWParametersUserCode
     */
    public FWParametersUserCode getUcTypeJournal() {

        if (ucTypeJournal == null) {
            // liste pas encore chargee, on la charge
            ucTypeJournal = new FWParametersUserCode();
            ucTypeJournal.setSession(getSession());

            // Récupérer le code système dans la langue de l'utilisateur
            ucTypeJournal.setIdCodeSysteme(getTypeJournal());
            ucTypeJournal.setIdLangue(getSession().getIdLangue());
            try {
                ucTypeJournal.retrieve();
                if (ucTypeJournal.isNew()) {
                    _addError(null, getSession().getLabel("7324"));
                }
            } catch (Exception e) {
                _addError(null, getSession().getLabel("7324"));
            }
        }

        return ucTypeJournal;
    }

    /**
     * Retourne vrai si le journal contient des opérations Date de création : (06.05.2002 08:15:35)
     * 
     * @return boolean vrai si le journal contient des opérations
     */
    public boolean hasOperations() {

        // Charger un manager
        CAOperationManager mgr = new CAOperationManager();
        mgr.setSession(getSession());
        mgr.setForIdJournal(getIdJournal());

        // Compter le nombre de correspondances
        try {
            if (mgr.getCount() != 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            _addError(null, e.getMessage());
            return false;
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2002 13:25:07)
     */
    public CAIListJournalEcritures_Doc imprimerJournal(BProcess context) {
        // Initialiser
        CAIListJournalEcritures_Doc proc = null;
        try {
            // Instancier un process d'impression sans parent
            if (context == null) {
                proc = new CAIListJournalEcritures_Doc();
                proc.setControleTransaction(true);
                proc.setEMailAddress(getSession().getUserEMail());
                // Instancier un process avec parent
            } else {
                proc = new CAIListJournalEcritures_Doc(context);
            }

            // Paramètres communs + démarrage
            proc.setIdJournal(getIdJournal());
            proc.executeProcess();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }
        // retourner le process
        return proc;
    }

    /**
     * Le journal est-il en état ANNULE ?
     * 
     * @return
     */
    public boolean isAnnule() {
        return (getEtat() != null) && getEtat().equals(CAJournal.ANNULE);
    }

    /**
     * Le journal est-il en état COMPTABILISE ?
     * 
     * @return
     */
    public boolean isComptabilise() {
        return (getEtat() != null) && getEtat().equals(CAJournal.COMPTABILISE);
    }

    /**
     * Le journal est-il en état ERREUR ?
     * 
     * @return
     */
    public boolean isErreur() {
        return (getEtat() != null) && getEtat().equals(CAJournal.ERREUR);
    }

    /**
     * Le journal est-il en état OUVERT ?
     * 
     * @return
     */
    public boolean isOuvert() {
        return (getEtat() != null) && getEtat().equals(CAJournal.OUVERT);
    }

    /**
     * Le journal est-il en état ANNULE ?
     * 
     * @return
     */
    public boolean isPartiel() {
        return (getEtat() != null) && getEtat().equals(CAJournal.PARTIEL);
    }

    /**
     * Le journal est-il en état TRAITEMENT ?
     * 
     * @return
     */
    public boolean isTraitement() {
        return (getEtat() != null) && getEtat().equals(CAJournal.TRAITEMENT);
    }

    /**
     * Retourne vrai si les propriétés du journal peuvent être modifiées Date de création : (15.01.2002 11:03:06)
     * 
     * @return boolean
     */
    public boolean isUpdatable() {
        String _etat = getEtat();
        return _etat.equalsIgnoreCase(CAJournal.OUVERT) || _etat.equalsIgnoreCase(CAJournal.ERREUR)
                || _etat.equalsIgnoreCase(CAJournal.PARTIEL) || JadeStringUtil.isBlank(_etat);
    }

    /**
     * Supprimer une opération du journal Date de création : (18.01.2002 12:48:18)
     * 
     * @param oper
     *            globaz.osiris.db.comptes.CAOperation l'opération à supprimer
     * @return FWMessage null s'il n'y a pas d'erreur, sinon un message d'erreur
     */
    public FWMessage removeOperation(CAOperation oper) {
        return null;
    }

    public void setDate(String newDate) {
        if (isUpdatable()) {
            date = newDate;
        } else {
            _addError(null, getSession().getLabel("7008"));
        }

    }

    public void setDateValeurCG(String newDateValeurCG) {
        if (isUpdatable()) {
            dateValeurCG = newDateValeurCG;
        } else {
            _addError(null, getSession().getLabel("7006"));
        }

    }

    public void setEstConfidentiel(Boolean newEstConfidentiel) {
        if (isUpdatable()) {
            estConfidentiel = newEstConfidentiel;
        } else {
            _addError(null, getSession().getLabel("7010"));
        }

    }

    public void setEstPublic(Boolean newEstPublic) {
        if (isUpdatable()) {
            estPublic = newEstPublic;
        } else {
            _addError(null, getSession().getLabel("7011"));
        }

    }

    public void setEstVisibleImmediatement(Boolean newEstVisibleImmediatement) {
        if (isUpdatable()) {
            estVisibleImmediatement = newEstVisibleImmediatement;
        } else {
            _addError(null, getSession().getLabel("7012"));
        }

    }

    public void setEtat(String newEtat) {
        etat = newEtat;
        csEtat = null;
        ucEtat = null;

    }

    /**
     * Setter
     */
    @Override
    public void setIdJournal(String newIdJournal) {
        if (isUpdatable()) {
            idJournal = newIdJournal;
        } else {
            _addError(null, getSession().getLabel("7024"));
        }

    }

    public void setIdLog(String newIdLog) {
        idLog = newIdLog;
        _log = null;
    }

    public void setIdPosteJournalisation(String newIdPosteJournalisation) {
        idPosteJournalisation = newIdPosteJournalisation;
    }

    public void setLibelle(String newLibelle) {
        if (isUpdatable()) {
            libelle = newLibelle;
        } else {
            _addError(null, getSession().getLabel("7015"));
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2002 13:58:30)
     * 
     * @param newMemoryLog
     *            globaz.framework.util.FWMemoryLog
     */
    public void setMemoryLog(FWMemoryLog newMemoryLog) {
        _memLog = newMemoryLog;
    }

    public void setNoJournalCG(String newNoJournalCG) {
        noJournalCG = newNoJournalCG;
    }

    public void setProprietaire(String newProprietaire) {
        if (isUpdatable()) {
            proprietaire = newProprietaire;
        } else {
            _addError(null, getSession().getLabel("7017"));
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2002 17:05:25)
     * 
     * @param newTypeJournal
     *            String
     */
    public void setTypeJournal(String newTypeJournal) {
        if (isUpdatable()) {
            typeJournal = newTypeJournal;
            csTypeJournal = null;
        } else {
            _addError(null, getSession().getLabel("7020"));
        }
    }

    /**
     * Suppression d'un journal Date de création : (25.02.2002 12:58:23)
     * 
     * @param context
     *            BProcess le context d'exécution
     */
    public void supprimer(BProcess context) {

        // Initialiser
        String _lastIdOperation = "0";

        // Sous contrôle d'exception
        try {

            // Si le journal n'est pas annulé, on effectue une tentative
            if (!getEtat().equals(CAJournal.ANNULE)) {
                new CAAnnulerJournal().annuler(context, this);
                if (context.getTransaction().hasErrors()) {
                    return;
                }
            }

            // Instancier un nouveau manager
            CAOperationManager mgr = new CAOperationManager();
            mgr.setSession(getSession());

            // Récupérer les opérations associées au journal
            mgr.setOrderBy(CAOperationManager.ORDER_IDOPERATION);
            mgr.setForIdJournal(getIdJournal());

            // Compter le nombre d'opération
            context.setState(getSession().getLabel("6112"));
            context.setProgressScaleValue(mgr.getCount(context.getTransaction()));

            // Boucle de lecture
            while (true) {

                // Récupérer une série d'opérations
                try {
                    mgr.clear();
                    mgr.find(context.getTransaction());
                } catch (Exception e) {
                    _addError(context.getTransaction(), e.getMessage());
                    return;
                }

                // Sortir s'il n'y a aucune opération trouvée
                if (mgr.size() == 0) {
                    break;
                }

                // Récupérer les opérations
                for (int i = 0; i < mgr.size(); i++) {

                    // Récupérer une opération et la convertir dans le type
                    // d'opération
                    CAOperation _operX = (CAOperation) mgr.getEntity(i);
                    CAOperation _oper = _operX.getOperationFromType(context.getTransaction());

                    // Si l'opération n'a pas été convertie
                    if (_oper == null) {
                        _addError(context.getTransaction(), getSession().getLabel("5013") + " " + _operX.toString());
                        return;
                    } else {

                        // Vérifier la condition de sortie
                        if (context.isAborted()) {
                            return;
                        }

                        // Supprimer l'opération
                        try {
                            _oper.delete(context.getTransaction());
                            if (context.getTransaction().hasErrors()) {
                                _addError(context.getTransaction(),
                                        getSession().getLabel("5028") + " " + _oper.toString());
                                return;
                            }
                        } catch (Exception e) {
                            _addError(context.getTransaction(), e.getMessage());
                            return;
                        }
                    }

                    // Progression
                    context.incProgressCounter();

                }

                // Positionnement sur id suivante
                mgr.setAfterIdOperation(_lastIdOperation);
            }

            // Supprimer les sections
            _supprimerSections(context);
            _supprimerComptesAnnexes(context);

        } catch (Exception e) {
            _addError(context.getTransaction(), e.getMessage());
            return;
        }

    }

    /**
     * @see globaz.globall.db.BEntity#toString()
     */
    public String toMyString() {
        try {
            return "[" + getIdJournal() + "] " + getDateValeurCG() + " " + getLibelle();
        } catch (Exception e) {
            return super.toString();
        }
    }
}
