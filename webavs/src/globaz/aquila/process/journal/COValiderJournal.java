package globaz.aquila.process.journal;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.db.access.journal.COElementJournalBatch;
import globaz.aquila.db.access.journal.COElementJournalBatchManager;
import globaz.aquila.db.access.journal.COJournalBatch;
import globaz.aquila.process.journal.utils.COUtilsJournal;
import globaz.aquila.ts.COTSValidationRule;
import globaz.aquila.ts.COTSValidator;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import java.util.Iterator;
import java.util.List;

public class COValiderJournal extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String LABEL_JOURNAL_NON_ERREUR = "JOURNAL_NON_ERREUR";
    private static final String LABEL_JOURNAL_VALIDATION_ERROR = "JOURNAL_VALIDATION_ERROR";
    private static final String LABEL_JOURNAL_VALIDATION_OK = "JOURNAL_VALIDATION_OK";
    private static final String LABEL_JOURNAL_VALIDATION_WARNING = "JOURNAL_VALIDATION_WARNING";

    private String idJournal;

    /**
     * Constructor for COExecuterJournal.
     */
    public COValiderJournal() throws Exception {
        this(new BSession(ICOApplication.DEFAULT_APPLICATION_AQUILA));
    }

    /**
     * Constructor for COExecuterJournal.
     * 
     * @param parent
     */
    public COValiderJournal(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for COExecuterJournal.
     * 
     * @param session
     */
    public COValiderJournal(BSession session) throws Exception {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {

    }

    /**
     * Valide les éléments du journal. Si tout les éléments sont validés sans erreurs => état du journal OUVERT.
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            COElementJournalBatchManager manager = new COElementJournalBatchManager();
            manager.setSession(getSession());

            manager.setForIdJournal(getIdJournal());

            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            setProgressScaleValue(manager.size());

            boolean stillOnErrors = false;
            for (int i = 0; (i < manager.size()) && !isAborted(); i++) {
                COElementJournalBatch element = (COElementJournalBatch) manager.get(i);
                element.setSession(getSession());
                element.setEtat(COElementJournalBatch.OUVERT);

                doValidation(element);

                if (isAborted()) {
                    return false;
                }

                if (COElementJournalBatch.ERREUR.equals(element.getEtat())) {
                    stillOnErrors = true;
                }

                element.update(getTransaction());
                setProgressDescription(element.getContentieux().getCompteAnnexe().getIdExterneRole() + " "
                        + element.getContentieux().getSection().getIdExterne());
                incProgressCounter();
            }

            if (isAborted()) {
                return false;
            }

            if (!stillOnErrors) {
                COUtilsJournal.updateEtatJournal(getSession(), getTransaction(), getIdJournal(), COJournalBatch.OUVERT);
            } else {
                COUtilsJournal.updateEtatJournal(getSession(), getTransaction(), getIdJournal(), COJournalBatch.ERREUR);
                getMemoryLog().logMessage(getSession().getLabel(COValiderJournal.LABEL_JOURNAL_VALIDATION_WARNING),
                        FWViewBeanInterface.WARNING, COValiderJournal.class.getName());
            }
        } catch (Exception e) {
            this._addError(e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

        COUtilsJournal.isIdJournalEmpty(getSession(), getIdJournal());

        COJournalBatch journal = COUtilsJournal.getJournal(getSession(), getTransaction(), getIdJournal());
        if (journal.isAnnule() || journal.isTraite()) {
            this._addError(getSession().getLabel(COValiderJournal.LABEL_JOURNAL_NON_ERREUR));
            throw new Exception(getSession().getLabel(COValiderJournal.LABEL_JOURNAL_NON_ERREUR));
        }
    }

    /**
     * Exécute la validation du traitement spécifique pour l'élément du journal.
     * 
     * @param element
     * @throws Exception
     */
    private void doValidation(COElementJournalBatch element) throws Exception {
        COTSValidator validator = element.getTraitementSpecifique().getValidator();
        List rules = validator.validationRules();
        for (Iterator ruleIter = rules.iterator(); ruleIter.hasNext();) {
            ((COTSValidationRule) ruleIter.next()).validate(getSession(), getTransaction(), element.getContentieux(),
                    element.getTransition(), element);
        }
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors()) {
            return getSession().getLabel(COValiderJournal.LABEL_JOURNAL_VALIDATION_ERROR);
        } else {
            return getSession().getLabel(COValiderJournal.LABEL_JOURNAL_VALIDATION_OK);
        }
    }

    public String getIdJournal() {
        return idJournal;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

}
