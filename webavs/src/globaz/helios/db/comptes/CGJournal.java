package globaz.helios.db.comptes;

import globaz.framework.util.FWLog;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BConstants;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.helios.api.ICGJournal;
import globaz.helios.application.CGApplication;
import globaz.helios.db.interfaces.CGLibelle;
import globaz.helios.db.interfaces.CGNeedExerciceComptable;
import globaz.helios.db.interfaces.ITreeListable;
import globaz.helios.process.CGJournalAnnulerProcess;
import globaz.helios.process.CGJournalComptabiliserProcess;
import globaz.helios.process.CGJournalExComptabiliserProcess;
import globaz.helios.translation.CodeSystem;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class CGJournal extends CGNeedExerciceComptable implements ITreeListable, Serializable, ICGJournal {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static int ALTERNATE_KEY_REFERENCEEXTERNE = 1;

    public final static String CS_TRI_DATE = "731002";
    public final static String CS_TRI_LIBELLE = "731003";
    // Journal tri
    public final static String CS_TRI_NUMERO = "731001";
    public final static String CS_TRI_PROPRIETAIRE = "731004";
    public final static String CS_TYPE_AUTOMATIQUE = ICGJournal.CS_TYPE_AUTOMATIQUE;
    public final static String CS_TYPE_ECRITURES_MODELES = ICGJournal.CS_TYPE_ECRITURES_MODELES;
    public final static String CS_TYPE_JOURNALIER = ICGJournal.CS_TYPE_JOURNALIER;
    // Type du journal
    public final static String CS_TYPE_MANUEL = ICGJournal.CS_TYPE_MANUEL;
    public final static String CS_TYPE_SYSTEME = ICGJournal.CS_TYPE_SYSTEME;
    public static final String FIELD_DATE = "DATE";
    public static final String FIELD_DATEVALEUR = "DATEVALEUR";
    public static final String FIELD_ESTCONFIDENTIEL = "ESTCONFIDENTIEL";
    public static final String FIELD_ESTPUBLIC = "ESTPUBLIC";
    public static final String FIELD_IDETAT = "IDETAT";
    public static final String FIELD_IDEXERCOMPTABLE = "IDEXERCOMPTABLE";

    public static final String FIELD_IDJOURNAL = "IDJOURNAL";

    public static final String FIELD_IDLIVRE = "IDLIVRE";

    public static final String FIELD_IDLOG = "IDLOG";
    public static final String FIELD_IDPERIODECOMPTABLE = "IDPERIODECOMPTABLE";
    public static final String FIELD_IDTYPEJOURNAL = "IDTYPEJOURNAL";
    public static final String FIELD_LIBELLE = "LIBELLE";
    public static final String FIELD_NUMERO = "NUMERO";
    public static final String FIELD_PROPRIETAIRE = "PROPRIETAIRE";
    public static final String FIELD_REFERENCEEXTERNE = "REFERENCEEXTERNE";
    private final static String LABEL_PREFIXE = "JOURNAL_";
    public static final String LX_REFERENCE_PREFIX = "LX";
    public static final String TABLE_NAME = "CGJOURP";
    private String date = new String();
    private String dateValeur = new String();
    private Boolean estConfidentiel = new Boolean(false);

    private Boolean estPublic = new Boolean(true);
    private String idEtat = ICGJournal.CS_ETAT_OUVERT;
    private String idJournal = new String();
    private String idLivre = new String();

    private String idLog = new String();
    private String idPeriodeComptable = "";
    private String idTypeJournal = CS_TYPE_MANUEL;
    private String libelle = new String();
    private String numero = new String();

    private String proprietaire = new String();

    private String referenceExterne = new String();

    /**
     * Commentaire relatif au constructeur CGModeleEcriture
     */
    public CGJournal() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdJournal(_incCounter(transaction, "0"));
        setNumero(_incCounter(transaction, "0", "JOUREXEC", getExerciceComptable().getIdExerciceComptable()));
        setDate(JACalendar.today().toString());
        // Propriétaire
        if (JadeStringUtil.isBlank(getProprietaire())) {
            setProprietaire(getSession().getUserId());
        }

        CGPeriodeComptable periode = new CGPeriodeComptable();
        periode.setSession(getSession());
        periode.setIdPeriodeComptable(getIdPeriodeComptable());
        periode.retrieve(transaction);

        if (periode.isNew() || periode.isEstCloture().booleanValue()) {
            _addError(transaction, getSession().getLabel("GLOBAL_PERIODE_INVALIDE"));
        }
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws java.lang.Exception {

        // Suppression
        // si l'utilisateur est le proprietaire
        // et le journal est de type manuelle
        // et le journal n'est pas comptabilisé
        boolean proprietaire = false;
        boolean comptabilise = ICGJournal.CS_ETAT_COMPTABILISE.equals(getIdEtat());
        boolean manuel = CS_TYPE_MANUEL.equals(getIdTypeJournal());

        // ou si l'utilisateur a les droits de chef comptable
        if (getSession().getUserId() != null && getSession().getUserId().equals(getProprietaire())) {
            proprietaire = true;
        }

        boolean condition = (proprietaire && manuel) && !comptabilise;

        if (condition
                || JadeAdminServiceLocatorProvider
                        .getLocator()
                        .getRoleUserService()
                        .hasRole(getSession().getUserId(),
                                getSession().getApplication().getProperty("CG_CHEF_COMPTABLE"))) {
            // Avant la suppression du journal, on supprime ses logs
            if (!getIdLog().equals("") && !getIdLog().equals("0")) {
                FWLog log = new FWLog();
                log.setSession(getSession());
                log.setIdLog(getIdLog());
                log.retrieve(transaction);
                log.delete(transaction);
            }
        } else {
            _addError(transaction, label("SUPPRESSION_NON_AUTHORISEE"));
        }
    }

    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        CGJournal oldJournal = new CGJournal();
        oldJournal.setSession(getSession());
        oldJournal.setIdJournal(getIdJournal());
        oldJournal.retrieve(transaction);

        // Le flag estPublic à été modifié.
        if (oldJournal.isEstPublic().booleanValue() != isEstPublic().booleanValue()) {

            // update de la check box saisie ouverte aux autres utilisateur
            // si l'utilisateur est le proprietaire
            // et le journal n'est pas comptabilisé
            boolean proprietaire = false;
            boolean comptabilise = ICGJournal.CS_ETAT_COMPTABILISE.equals(getIdEtat());

            // ou si l'utilisateur a les droits de chef comptable
            if (getSession().getUserId() != null && getSession().getUserId().equals(getProprietaire())) {
                proprietaire = true;
            }

            boolean condition = proprietaire && !comptabilise;

            if (!condition && !CGApplication.isUserChefComptable(getSession())) {
                _addError(transaction, getSession().getLabel("SAISIE_JRN_AUTRES_UTILISATEURS_INTERDITE"));
            }
        }

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idJournal = statement.dbReadNumeric(FIELD_IDJOURNAL);
        idExerciceComptable = statement.dbReadNumeric(FIELD_IDEXERCOMPTABLE);
        numero = statement.dbReadNumeric(FIELD_NUMERO);
        libelle = statement.dbReadString(FIELD_LIBELLE);
        idLivre = statement.dbReadNumeric(FIELD_IDLIVRE);
        date = statement.dbReadDateAMJ(FIELD_DATE);
        dateValeur = statement.dbReadDateAMJ(FIELD_DATEVALEUR);
        proprietaire = statement.dbReadString(FIELD_PROPRIETAIRE);
        estPublic = statement.dbReadBoolean(FIELD_ESTPUBLIC);
        estConfidentiel = statement.dbReadBoolean(FIELD_ESTCONFIDENTIEL);
        idEtat = statement.dbReadNumeric(FIELD_IDETAT);
        idTypeJournal = statement.dbReadNumeric(FIELD_IDTYPEJOURNAL);
        idPeriodeComptable = statement.dbReadNumeric(FIELD_IDPERIODECOMPTABLE);
        referenceExterne = statement.dbReadString(FIELD_REFERENCEEXTERNE);
        idLog = statement.dbReadNumeric(FIELD_IDLOG);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {

        // check l'exercice comptable
        super._validate(statement);

        /* libelle - obligatoire */
        if (JadeStringUtil.isBlank(getLibelle())) {
            _addError(statement.getTransaction(), label("CHAMP_LIB_ERROR"));
        }

        /* idLivre - obligatoire */
        if (getExerciceComptable().getMandat().isUtiliseLivres().booleanValue()) {
            if (JadeStringUtil.isBlank(getIdLivre())) {
                _addError(statement.getTransaction(), label("CHAMP_LIVRE_ERROR"));
            }
        }

        /* idLivre - obligatoire */
        if (JadeStringUtil.isIntegerEmpty(getIdPeriodeComptable())) {
            _addError(statement.getTransaction(), label("PERIODE_ERROR"));
        }

        _propertyMandatory(statement.getTransaction(), getIdPeriodeComptable(), label("PERIODE_ERROR"));

        // Propriétaire, oblifatoire :
        if (JadeStringUtil.isBlank(getProprietaire())) {
            _addError(statement.getTransaction(), label("PROPRIO_ERROR"));
        }

        /* dateValeur - obligatoire */
        if (JAUtil.isDateEmpty(getDateValeur())) {
            _addError(statement.getTransaction(), label("DATE_VALEUR_ERROR"));
        } else {
            try {
                BSessionUtil.checkDateGregorian(getSession(), getDateValeur());
            } catch (Exception e) {
                _addError(statement.getTransaction(), label("DATE_VALEUR_INVALID"));
            }
        }

        /*
         * dateValeur - inclue dans une pérode comptable non cloturée (et pour cet exercice comptable)
         */
        CGPeriodeComptable periode = new CGPeriodeComptable();
        periode.setSession(getSession());
        periode.setIdPeriodeComptable(getIdPeriodeComptable());
        periode.retrieve(statement.getTransaction());

        if (!periode.isDateIncluded(statement, getDateValeur())) {
            _addError(statement.getTransaction(), label("DATE_VALEUR_ERROR_2"));
        }

        /* date - obligatoire (automatique, ne devrait jamais être vide) */
        if (JAUtil.isDateEmpty(getDate())) {
            _addError(statement.getTransaction(), label("DATE_CREATION_ERROR_1"));
        } else {
            try {
                BSessionUtil.checkDateGregorian(getSession(), getDate());
            } catch (Exception e) {
                _addError(statement.getTransaction(), label("DATE_CREATION_ERROR_2"));
            }
        }
    }

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        switch (alternateKey) {
            case ALTERNATE_KEY_REFERENCEEXTERNE:
                statement.writeKey(FIELD_REFERENCEEXTERNE,
                        _dbWriteString(statement.getTransaction(), getReferenceExterne(), "referenceExterne"));
                statement.writeKey(FIELD_IDEXERCOMPTABLE,
                        _dbWriteNumeric(statement.getTransaction(), getIdExerciceComptable(), "idExerciceComptable"));
                break;
            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELD_IDJOURNAL, _dbWriteNumeric(statement.getTransaction(), getIdJournal(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(FIELD_IDJOURNAL, _dbWriteNumeric(statement.getTransaction(), getIdJournal(), "idJournal"));
        statement.writeField(FIELD_IDEXERCOMPTABLE,
                _dbWriteNumeric(statement.getTransaction(), getIdExerciceComptable(), "idExerciceComptable"));
        statement.writeField(FIELD_NUMERO, _dbWriteNumeric(statement.getTransaction(), getNumero(), "numero"));
        statement.writeField(FIELD_LIBELLE, _dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField(FIELD_IDLIVRE, _dbWriteNumeric(statement.getTransaction(), getIdLivre(), "idLivre"));
        statement.writeField(FIELD_DATE, _dbWriteDateAMJ(statement.getTransaction(), getDate(), "date"));
        statement.writeField(FIELD_DATEVALEUR,
                _dbWriteDateAMJ(statement.getTransaction(), getDateValeur(), "dateValeur"));
        statement.writeField(FIELD_PROPRIETAIRE,
                _dbWriteString(statement.getTransaction(), getProprietaire(), "proprietaire"));
        statement
                .writeField(
                        FIELD_ESTPUBLIC,
                        _dbWriteBoolean(statement.getTransaction(), isEstPublic(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                                "estPublic"));
        statement.writeField(
                FIELD_ESTCONFIDENTIEL,
                _dbWriteBoolean(statement.getTransaction(), isEstConfidentiel(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "estConfidentiel"));
        statement.writeField(FIELD_IDETAT, _dbWriteNumeric(statement.getTransaction(), getIdEtat(), "idEtat"));
        statement.writeField(FIELD_IDTYPEJOURNAL,
                _dbWriteNumeric(statement.getTransaction(), getIdTypeJournal(), "idTypeJournal"));
        statement.writeField(FIELD_IDPERIODECOMPTABLE,
                _dbWriteNumeric(statement.getTransaction(), getIdPeriodeComptable(), "idPeriodeComptable"));
        statement.writeField(FIELD_REFERENCEEXTERNE,
                _dbWriteString(statement.getTransaction(), getReferenceExterne(), "referenceExterne"));
        statement.writeField(FIELD_IDLOG, _dbWriteNumeric(statement.getTransaction(), getIdLog(), "idLog"));
    }

    /**
     * Lance le processus d'annulation d'un journal. Annulation de toutes les écritures du journal, mais pas du journal
     * lui même.
     * 
     * @param transaction
     * @throws Exception
     */
    @Override
    public void annuler(BITransaction transaction) throws Exception {
        CGJournalAnnulerProcess process = new CGJournalAnnulerProcess(getSession());
        process.setTransaction(transaction);
        process.setControleTransaction(false);
        process.setIdJournal(getIdJournal());

        if (!isNew()) {
            process.setJournal(this);
        }

        process.executeProcess();

    }

    /**
     * Lance le processus d'annulation d'un journal. Annulation de toutes les écritures du journal, mais pas du journal
     * lui même.
     * 
     * @param transaction
     * @param emailAddress
     * @throws Exception
     */
    @Override
    public void annuler(BITransaction transaction, String emailAddress) throws Exception {
        CGJournalAnnulerProcess process = new CGJournalAnnulerProcess(getSession());
        process.setTransaction(transaction);
        process.setControleTransaction(false);
        process.setIdJournal(getIdJournal());
        process.setEMailAddress(emailAddress);

        if (!isNew()) {
            process.setJournal(this);
        }

        process.executeProcess();

    }

    /**
     * Method comptabiliser. Lance le processus de comptabilisation d'un journal
     * 
     * @param transaction
     * @throws Exception
     */
    @Override
    public void comptabiliser(BITransaction transaction) throws Exception {
        CGJournalComptabiliserProcess process = new CGJournalComptabiliserProcess(getSession());
        process.setTransaction(transaction);
        process.setControleTransaction(false);
        process.setIdJournal(getIdJournal());

        if (!isNew()) {
            process.setJournal(this);
        }

        process.executeProcess();
    }

    /**
     * Method comptabiliser. Lance le processus de comptabilisation d'un journal
     * 
     * @param transaction
     * @throws Exception
     */
    @Override
    public void comptabiliser(BITransaction transaction, String emailAddress) throws Exception {
        CGJournalComptabiliserProcess process = new CGJournalComptabiliserProcess(getSession());
        process.setTransaction(transaction);
        process.setControleTransaction(false);
        process.setEMailAddress(emailAddress);
        process.setIdJournal(getIdJournal());

        if (!isNew()) {
            process.setJournal(this);
        }

        process.executeProcess();
    }

    /**
     * Method comptabiliser. Lance le processus de comptabilisation d'un journal
     * 
     * @param parent
     *            le processus parent
     * @throws Exception
     */
    public void comptabiliser(BProcess parent) throws Exception {
        CGJournalComptabiliserProcess process;
        if (parent != null) {
            process = new CGJournalComptabiliserProcess(parent);
        } else {
            process = new CGJournalComptabiliserProcess(getSession());
        }

        process.setIdJournal(getIdJournal());

        if (!isNew()) {
            process.setJournal(this);
        }

        process.executeProcess();
    }

    /**
     * Method exComptabiliser. Lance le processus d'excomptabilisation d'un journal
     * 
     * @param transaction
     * @throws Exception
     */
    @Override
    public void exComptabiliser(BITransaction transaction) throws Exception {
        CGJournalExComptabiliserProcess process = new CGJournalExComptabiliserProcess(getSession());
        process.setTransaction(transaction);
        process.setIdJournal(getIdJournal());
        process.setControleTransaction(false);

        if (!isNew()) {
            process.setJournal(this);
        }

        process.executeProcess();
    }

    @Override
    public BManager[] getChilds() throws Exception {
        BManager[] managers = new BManager[1];
        CGEnteteEcritureListViewBean manager = new CGEnteteEcritureListViewBean();
        manager.setForIdJournal(getIdJournal());

        managers[0] = manager;
        return managers;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.11.2002 14:16:56)
     */

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public String getDateValeur() {
        return dateValeur;
    }

    @Override
    public Boolean getEstConfidentiel() {
        return estConfidentiel;
    }

    @Override
    public Boolean getEstPublic() {
        return estPublic;
    }

    @Override
    public String getEtatLibelle() {
        try {
            return CodeSystem.getLibelle(getSession(), idEtat);
        } catch (Exception e) {
            e.printStackTrace();
            return CGLibelle.LIBELLE_ERROR;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.11.2002 10:14:56)
     * 
     * @return String
     */
    @Override
    public String getFullDescription() {
        return getNumero() + " - " + getLibelle();
    }

    @Override
    public String getIdEtat() {
        return idEtat;
    }

    /**
     * Getter
     */
    @Override
    public String getIdJournal() {
        return idJournal;
    }

    public String getIdLivre() {
        return idLivre;
    }

    public String getIdLog() {
        return idLog;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.12.2002 10:06:26)
     * 
     * @return String
     */
    @Override
    public String getIdPeriodeComptable() {
        return idPeriodeComptable;
    }

    @Override
    public String getIdTypeJournal() {
        return idTypeJournal;
    }

    @Override
    public String getLibelle() {
        return libelle;
    }

    @Override
    public String getNumero() {
        return numero;
    }

    @Override
    public String getProprietaire() {
        return proprietaire;
    }

    @Override
    public String getReferenceExterne() {
        return referenceExterne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.11.2002 11:03:39)
     */
    public void imprimer() {
    }

    /**
     * Le journal est-il en état ANNULE ?
     * 
     * @return
     */
    public boolean isAnnule() {
        return ((getIdEtat() != null) && (getIdEtat().equals(CS_ETAT_ANNULE)));
    }

    /**
     * Le journal est-il en état COMPTABILISE ?
     * 
     * @return
     */
    public boolean isComptabilise() {
        return ((getIdEtat() != null) && (getIdEtat().equals(CS_ETAT_COMPTABILISE)));
    }

    /**
     * Le journal est-il en état ERREUR ?
     * 
     * @return
     */
    public boolean isErreur() {
        return ((getIdEtat() != null) && (getIdEtat().equals(CS_ETAT_ERREUR)));
    }

    public Boolean isEstConfidentiel() {
        return estConfidentiel;
    }

    public Boolean isEstPublic() {
        return estPublic;
    }

    /**
     * Le journal est-il en état OUVERT ?
     * 
     * @return
     */
    public boolean isOuvert() {
        return ((getIdEtat() != null) && (getIdEtat().equals(CS_ETAT_OUVERT)));
    }

    /**
     * Le journal est-il en état PARTIEL ?
     * 
     * @return
     */
    public boolean isPartiel() {
        return ((getIdEtat() != null) && (getIdEtat().equals(CS_ETAT_PARTIEL)));
    }

    /**
     * Le journal est-il en état TRAITEMENT ?
     * 
     * @return
     */
    public boolean isTraitement() {
        return ((getIdEtat() != null) && (getIdEtat().equals(CS_ETAT_TRAITEMENT)));
    }

    private String label(String code) {
        return getSession().getLabel(LABEL_PREFIXE + code);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.03.2003 15:37:31)
     * 
     * @return globaz.framework.util.FWLog
     */
    public globaz.framework.util.FWLog retrieveLog() {
        if (getIdLog() != null && !getIdLog().equals("0") && !getIdLog().equals("")) {
            try {
                FWLog log = new FWLog();
                log.setSession(getSession());
                log.setIdLog(getIdLog());
                log.retrieve();
                return log;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public void setDate(String newDate) {
        date = newDate;
    }

    @Override
    public void setDateValeur(String newDateValeur) {
        dateValeur = newDateValeur;
    }

    @Override
    public void setEstConfidentiel(Boolean newEstConfidentiel) {
        estConfidentiel = newEstConfidentiel;
    }

    @Override
    public void setEstPublic(Boolean newEstPublic) {
        estPublic = newEstPublic;
    }

    public void setIdEtat(String newIdEtat) {
        idEtat = newIdEtat;
    }

    /**
     * Setter
     */
    @Override
    public void setIdJournal(String newIdJournal) {
        idJournal = newIdJournal;
    }

    /**
     * Method setIdJournalFrom.
     * 
     * Sette l'id d'un journal identifi par l'id de la période et de l'exercice comptable
     * 
     * @param idPeriode
     * @param idExerciceComptable
     * @throws Exception
     */
    @Override
    public void setIdJournalFrom(String idPeriode, String idExerciceComptable) throws java.lang.Exception {
        setIdJournalFrom(idPeriode, idExerciceComptable, null);
    }

    /**
     * Method setIdJournalFrom.
     * 
     * Sette l'id d'un journal identifi par l'id de la période et de l'exercice comptable
     * 
     * @param idPeriode
     * @param idExerciceComptable
     * @param transaction
     * @throws Exception
     */

    public void setIdJournalFrom(String idPeriode, String idExerciceComptable,
            globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        CGJournalManager manager = new CGJournalManager();
        manager.setSession(getSession());
        manager.setForIdExerciceComptable(idExerciceComptable);
        manager.setForIdPeriodeComptable(idPeriode);
        manager.find(transaction);
        if (manager.size() == 0) {
            throw (new Exception(label("ERROR_1")));
        } else if (manager.size() > 1) {
            throw (new Exception(label("ERROR_2")));
        } else {
            setIdJournal(((CGJournal) manager.getEntity(0)).getIdJournal());
        }
    }

    public void setIdLivre(String newIdLivre) {
        idLivre = newIdLivre;
    }

    public void setIdLog(String newIdLog) {
        idLog = newIdLog;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.12.2002 10:06:26)
     * 
     * @param newIdPeriodeComptable
     *            String
     */
    @Override
    public void setIdPeriodeComptable(String newIdPeriodeComptable) {
        idPeriodeComptable = newIdPeriodeComptable;
    }

    @Override
    public void setIdTypeJournal(String newIdTypeJournal) {
        idTypeJournal = newIdTypeJournal;
    }

    @Override
    public void setLibelle(String newLibelle) {
        libelle = newLibelle;
    }

    public void setNumero(String newNumero) {
        numero = newNumero;
    }

    public void setProprietaire(String newProprietaire) {
        proprietaire = newProprietaire;
    }

    @Override
    public void setReferenceExterne(String newReferenceExterne) {
        referenceExterne = newReferenceExterne;
    }

}
