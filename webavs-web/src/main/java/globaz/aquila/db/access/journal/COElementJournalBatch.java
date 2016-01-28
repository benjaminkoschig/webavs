package globaz.aquila.db.access.journal;

import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COContentieuxFactory;
import globaz.aquila.db.access.traitspec.COTraitementSpecifique;
import globaz.framework.util.FWLog;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.utils.CARemarque;
import java.util.Enumeration;

/**
 * Elément d'un journal du contentieux Aquila.
 * 
 * @author vre
 */
public class COElementJournalBatch extends BEntity {

    public final static String ERREUR = "5210003";
    public static final String FNAME_CS_ETAT = "OPTETA";
    public static final String FNAME_CS_TYPE_CONTENTIEUX = "OPTTCO";

    public static final String FNAME_ID_CONTENTIEUX = "OPICON";
    public static final String FNAME_ID_ELEMENT_JOURNAL = "OPIEJR";

    public static final String FNAME_ID_JOURNAL = "OPIJRN";

    public static final String FNAME_ID_LOG = "OPILOG";

    public static final String FNAME_ID_REMARQUE = "OPIREM";
    public static final String FNAME_ID_TRAITEMENTSPECIFIQUE = "OPITRS";
    public static final String FNAME_ID_TRANSITION = "OPITRA";
    private static final String ID_BLANK = "0";
    public final static String INACTIF = "521004";
    private static final String LABEL_CONTENTIEUX_NON_RENSEIGNE = "CONTENTIEUX_NON_RENSEIGNE";
    public static final String LABEL_JOURNAL_NI_OUVERT_NI_ERREUR = "JOURNAL_NI_OUVERT_NI_ERREUR";
    public static final String LABEL_JOURNAL_NON_RENSEIGNE = "JOURNAL_NON_RENSEIGNE";
    private static final String LABEL_TRAITEMENT_SPECIFIQUE_NON_RENSEIGNE = "TRAITEMENT_SPECIFIQUE_NON_RENSEIGNE";

    private static final String LABEL_TRANSITION_NON_RENSEIGNE = "TRANSITION_NON_RENSEIGNE";
    public final static String OUVERT = "5210001";
    private static final long serialVersionUID = 8035342325500271209L;
    public static final String TABLE_NAME = "COELJRP";

    public final static String TRAITE = "5210002";

    private COContentieux contentieux;
    private String csTypeContentieux;
    private String etat;
    private String idContentieux;
    private String idElementJournal = new String();
    private String idJournal;
    private String idLog;
    private String idRemarque;
    private String idTraitementSpecifique;

    private String idTransition;

    private FWMemoryLog log;
    private String texteRemarque;
    private COTraitementSpecifique traitementSpecifique;

    private COTransition transition;

    /**
     * Constructeur no-args.
     */
    public COElementJournalBatch() {
    }

    /**
     * Crée un nouvel élément de journal pour le contentieux et la transition donnée.
     * 
     * @param contentieux
     *            le contentieux
     * @param transition
     *            la transition
     */
    public COElementJournalBatch(COContentieux contentieux, COTransition transition) {
        idContentieux = contentieux.getIdContentieux();
        csTypeContentieux = contentieux.getLibSequence();
        idTransition = transition.getIdTransition();
    }

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        setTexteRemarque(getRemarque().getTexte());
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        idElementJournal = this._incCounter(transaction, idElementJournal);

        if (JadeStringUtil.isIntegerEmpty(getEtat())) {
            setEtat(COElementJournalBatch.OUVERT);
        }

        saveLogs(transaction);
        saveRemarque(transaction);

    }

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        COJournalBatch journal = new COJournalBatch();
        journal.setSession(getSession());

        journal.setIdJournal(getIdJournal());

        journal.retrieve(transaction);

        if (journal.hasErrors() || journal.isNew()) {
            _addError(transaction, getSession().getLabel(COElementJournalBatch.LABEL_JOURNAL_NON_RENSEIGNE));
            throw new Exception(getSession().getLabel(COElementJournalBatch.LABEL_JOURNAL_NON_RENSEIGNE));
        }

        if (!journal.isOuvert() && !journal.isErreur()) {
            _addError(transaction, getSession().getLabel(COElementJournalBatch.LABEL_JOURNAL_NI_OUVERT_NI_ERREUR));
            throw new Exception(COElementJournalBatch.LABEL_JOURNAL_NI_OUVERT_NI_ERREUR);
        }

        deleteLogs(transaction);
        deleteRemarque(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        deleteLogs(transaction);
        saveLogs(transaction);
        updateRemarque(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return COElementJournalBatch.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idElementJournal = statement.dbReadNumeric(COElementJournalBatch.FNAME_ID_ELEMENT_JOURNAL);
        idJournal = statement.dbReadNumeric(COElementJournalBatch.FNAME_ID_JOURNAL);
        etat = statement.dbReadNumeric(COElementJournalBatch.FNAME_CS_ETAT);
        idContentieux = statement.dbReadNumeric(COElementJournalBatch.FNAME_ID_CONTENTIEUX);
        csTypeContentieux = statement.dbReadNumeric(COElementJournalBatch.FNAME_CS_TYPE_CONTENTIEUX);
        idTransition = statement.dbReadNumeric(COElementJournalBatch.FNAME_ID_TRANSITION);
        idTraitementSpecifique = statement.dbReadNumeric(COElementJournalBatch.FNAME_ID_TRAITEMENTSPECIFIQUE);
        idLog = statement.dbReadNumeric(COElementJournalBatch.FNAME_ID_LOG);
        idRemarque = statement.dbReadNumeric(COElementJournalBatch.FNAME_ID_REMARQUE);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            throw new Exception(getSession().getLabel(COElementJournalBatch.LABEL_JOURNAL_NON_RENSEIGNE));
        }

        if (JadeStringUtil.isIntegerEmpty(getIdContentieux())) {
            throw new Exception(getSession().getLabel(COElementJournalBatch.LABEL_CONTENTIEUX_NON_RENSEIGNE));
        }

        if (JadeStringUtil.isIntegerEmpty(getIdTransition())) {
            throw new Exception(getSession().getLabel(COElementJournalBatch.LABEL_TRANSITION_NON_RENSEIGNE));
        }

        if (JadeStringUtil.isIntegerEmpty(getIdTraitementSpecifique())) {
            throw new Exception(getSession().getLabel(COElementJournalBatch.LABEL_TRAITEMENT_SPECIFIQUE_NON_RENSEIGNE));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(COElementJournalBatch.FNAME_ID_ELEMENT_JOURNAL,
                this._dbWriteNumeric(statement.getTransaction(), idElementJournal));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(COElementJournalBatch.FNAME_ID_ELEMENT_JOURNAL,
                this._dbWriteNumeric(statement.getTransaction(), idElementJournal));
        statement.writeField(COElementJournalBatch.FNAME_ID_JOURNAL,
                this._dbWriteNumeric(statement.getTransaction(), idJournal, "idJournal"));
        statement.writeField(COElementJournalBatch.FNAME_CS_ETAT,
                this._dbWriteNumeric(statement.getTransaction(), etat, "csEtat"));
        statement.writeField(COElementJournalBatch.FNAME_ID_CONTENTIEUX,
                this._dbWriteNumeric(statement.getTransaction(), idContentieux, "idContentieux"));
        statement.writeField(COElementJournalBatch.FNAME_CS_TYPE_CONTENTIEUX,
                this._dbWriteNumeric(statement.getTransaction(), csTypeContentieux, "csTypeContentieux"));
        statement.writeField(COElementJournalBatch.FNAME_ID_TRANSITION,
                this._dbWriteNumeric(statement.getTransaction(), idTransition, "idTransition"));
        statement.writeField(COElementJournalBatch.FNAME_ID_TRAITEMENTSPECIFIQUE,
                this._dbWriteNumeric(statement.getTransaction(), idTraitementSpecifique, "idTraitementSpecifique"));
        statement.writeField(COElementJournalBatch.FNAME_ID_LOG,
                this._dbWriteNumeric(statement.getTransaction(), idLog, "idLogFW"));
        statement.writeField(COElementJournalBatch.FNAME_ID_REMARQUE,
                this._dbWriteNumeric(statement.getTransaction(), idRemarque, "idRemarque"));
    }

    /**
     * Set l'état en erreur de l'élément et ajoute les messages dans le log.
     * 
     * @param session
     * @param transaction
     * @param className
     * @param message
     */
    public void addErrors(BSession session, BTransaction transaction, String className, String message) {
        setEtat(COElementJournalBatch.ERREUR);

        getLog(session).logMessage(message, FWMessage.ERREUR, className);
    }

    /**
     * Supprime les logs (FWMessage) liés à l'élément (Si logs il y a).
     * 
     * @param transaction
     * @throws Exception
     */
    private void deleteLogs(BTransaction transaction) throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(getIdLog())) {
            FWLog oldLog = new FWLog();
            oldLog.setSession(getSession());
            oldLog.setIdLog(getIdLog());

            oldLog.retrieve(transaction);

            if (!oldLog.isNew() || !oldLog.hasErrors()) {
                oldLog.delete(transaction);
            }

            setIdLog(COElementJournalBatch.ID_BLANK);
        }
    }

    /**
     * Suppression de la remarque.
     * 
     * @param transaction
     * @throws Exception
     */
    private void deleteRemarque(BTransaction transaction) throws Exception {
        CARemarque remarque = getRemarque();
        if (!remarque.isNew()) {
            remarque.delete(transaction);
            setIdRemarque(COElementJournalBatch.ID_BLANK);
        }
    }

    public COContentieux getContentieux() {
        if (contentieux == null) {
            try {
                if (!JadeStringUtil.isIntegerEmpty(getIdContentieux())) {
                    contentieux = COContentieuxFactory.loadContentieux(getSession(), getIdContentieux());

                    if (contentieux.hasErrors() || contentieux.isNew()) {
                        contentieux = null;
                    }
                } else {
                    contentieux = null;
                }
            } catch (Exception e) {
                contentieux = null;
            }
        }

        return contentieux;
    }

    public String getCsTypeContentieux() {
        return csTypeContentieux;
    }

    /**
     * Pour écran.
     * 
     * @return Les erreurs liés à l'éléments.
     */
    public String getErrorMessages() {
        if (isErreur() && !JadeStringUtil.isIntegerEmpty(getIdLog())) {
            FWLog log = new FWLog();
            log.setSession(getSession());

            log.setIdLog(getIdLog());
            try {
                log.retrieve();
                if (log.isNew()) {
                    return "";
                }

                StringBuffer sb = new StringBuffer();
                Enumeration enume = log.getMessagesToEnumeration();
                while (enume.hasMoreElements()) {
                    FWMessage msg = (FWMessage) enume.nextElement();
                    sb.append(msg.getCsTypeMessage().getLibelle() + ": ");
                    sb.append(msg.getMessageText());
                    sb.append("\n");
                }

                return sb.toString();
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public String getEtat() {
        return etat;
    }

    /**
     * Pour écran.
     * 
     * @return Le libellé de l'état de l'élément.
     */
    public String getEtatLibelle() {
        FWParametersUserCode ucEtat = new FWParametersUserCode();
        ucEtat.setSession(getSession());

        if (!JadeStringUtil.isIntegerEmpty(getEtat())) {
            ucEtat.setIdCodeSysteme(getEtat());
            ucEtat.setIdLangue(getSession().getIdLangue());

            try {
                ucEtat.retrieve();
                if (ucEtat.isNew() || ucEtat.hasErrors()) {
                    return "";
                }
            } catch (Exception e) {
                return "";
            }
        }

        return ucEtat.getLibelle();
    }

    public String getIdContentieux() {
        return idContentieux;
    }

    public String getIdElementJournal() {
        return idElementJournal;
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

    public String getIdTraitementSpecifique() {
        return idTraitementSpecifique;
    }

    public String getIdTransition() {
        return idTransition;
    }

    /**
     * Si log = null => new log.
     * 
     * @param session
     * @return Charge le log attaché.
     */
    private FWMemoryLog getLog(BSession session) {
        if (log == null) {
            log = new FWMemoryLog();
            log.setSession(session);
        }

        return log;
    }

    /**
     * Return la remarque liée à l'élément.
     * 
     * @return
     */
    public CARemarque getRemarque() {
        CARemarque remarque = new CARemarque();
        remarque.setSession(getSession());

        remarque.setIdRemarque(getIdRemarque());

        try {
            remarque.retrieve();
        } catch (Exception e) {
            _addError(null, e.getMessage());
        }

        return remarque;
    }

    public String getTexteRemarque() {
        return texteRemarque;
    }

    public COTraitementSpecifique getTraitementSpecifique() {
        if (traitementSpecifique == null) {
            traitementSpecifique = new COTraitementSpecifique();
            traitementSpecifique.setSession(getSession());

            traitementSpecifique.setIdTraitementSpecifique(getIdTraitementSpecifique());

            try {
                traitementSpecifique.retrieve();

                if (traitementSpecifique.hasErrors() || traitementSpecifique.isNew()) {
                    traitementSpecifique = null;
                }
            } catch (Exception e) {
                traitementSpecifique = null;
            }
        }

        return traitementSpecifique;
    }

    public COTransition getTransition() {
        if (transition == null) {
            transition = new COTransition();
            transition.setSession(getSession());

            transition.setIdTransition(getIdTransition());

            try {
                transition.retrieve();

                if (transition.hasErrors() || transition.isNew()) {
                    transition = null;
                }
            } catch (Exception e) {
                transition = null;
            }
        }

        return transition;
    }

    /**
     * @see ERREUR
     * @return True état ERREUR
     */
    public boolean isErreur() {
        return COElementJournalBatch.ERREUR.equals(getEtat());
    }

    /**
     * @see INACTIF
     * @return True état INACTIF
     */
    public boolean isInactif() {
        return COElementJournalBatch.INACTIF.equals(getEtat());
    }

    /**
     * @see OUVERT
     * @return True état OUVERT
     */
    public boolean isOuvert() {
        return COElementJournalBatch.OUVERT.equals(getEtat());
    }

    /**
     * @see TRAITE
     * @return True état TRAITE
     */
    public boolean isTraite() {
        return COElementJournalBatch.TRAITE.equals(getEtat());
    }

    /**
     * Ajoute les logs et met à jour l'id log.
     * 
     * @param transaction
     */
    private void saveLogs(BTransaction transaction) {
        if (log != null) {
            FWLog elementLog = log.saveToFWLog(transaction);
            setIdLog(elementLog.getIdLog());
        }
    }

    /**
     * Ajoute une remarque.
     * 
     * @param transaction
     * @throws Exception
     */
    private void saveRemarque(BTransaction transaction) throws Exception {
        if (!JadeStringUtil.isBlank(getTexteRemarque())) {
            CARemarque remarque = new CARemarque();
            remarque.setSession(getSession());
            remarque.setTexte(getTexteRemarque());
            remarque.add(transaction);

            setIdRemarque(remarque.getIdRemarque());
        }
    }

    public void setCsTypeContentieux(String csTypeContentieux) {
        this.csTypeContentieux = csTypeContentieux;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setIdContentieux(String idContentieux) {
        this.idContentieux = idContentieux;
    }

    public void setIdElementJournal(String idElementJournal) {
        this.idElementJournal = idElementJournal;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setIdLog(String idLog) {
        this.idLog = idLog;
    }

    public void setIdRemarque(String idRemarque) {
        this.idRemarque = idRemarque;
    }

    public void setIdTraitementSpecifique(String idTraitementSpecifique) {
        this.idTraitementSpecifique = idTraitementSpecifique;
    }

    public void setIdTransition(String idTransition) {
        this.idTransition = idTransition;
    }

    public void setTexteRemarque(String texteRemarque) {
        this.texteRemarque = texteRemarque;
    }

    /**
     * Mise à jour ou suppresion de la remarque.
     * 
     * @param transaction
     * @throws Exception
     */
    private void updateRemarque(BTransaction transaction) throws Exception {
        if (!JadeStringUtil.isBlank(getTexteRemarque())) {
            if (JadeStringUtil.isIntegerEmpty(getIdRemarque())) {
                saveRemarque(transaction);
            } else {
                CARemarque remarque = getRemarque();
                if (!remarque.isNew()) {
                    remarque.setTexte(getTexteRemarque());
                    remarque.update(transaction);

                    setIdRemarque(remarque.getIdRemarque());
                }
            }
        } else if (!JadeStringUtil.isIntegerEmpty(getIdRemarque())) {
            deleteRemarque(transaction);
        }
    }
}
