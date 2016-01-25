package globaz.osiris.process.journal;

import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationManager;
import java.util.ArrayList;

/**
 * @author dda
 */
public class CAProcessExtournerJournal extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String LABEL_5157 = "5157";
    private static final String LABEL_5507 = "5507";
    private static final String LABEL_EXOURNER_JOURNAL_NOT_OK = "EXOURNER_JOURNAL_NOT_OK";
    private static final String LABEL_EXOURNER_JOURNAL_OK = "EXOURNER_JOURNAL_OK";
    private static final String LABEL_EXTOURNE_JOURNAL_IDENTIFIANT = "EXTOURNE_JOURNAL_IDENTIFIANT";
    private static final String LABEL_JOURNAL_NON_COMPTABILISE = "JOURNAL_NON_COMPTABILISE";

    private String idJournal = new String();

    /**
	 * 
	 *
	 */
    public CAProcessExtournerJournal() {
        super();
    }

    /**
     * @param parent
     */
    public CAProcessExtournerJournal(BProcess parent) {
        super(parent);
    }

    /**
     * Nettoyage après erreur ou exécution
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {

        // Sous contrôle d'exceptions
        try {
            // Vérifier l'identifiant de l'ordre groupé
            if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
                getMemoryLog().logMessage(CAProcessExtournerJournal.LABEL_5507, null, FWMessage.FATAL,
                        this.getClass().getName());
                return false;
            }

            CAJournal journal = getSourceJournal();

            if (isAborted()) {
                return false;
            }

            CAJournal destinationJournal = addDestinationJournal(journal);

            if (isAborted()) {
                return false;
            }

            iterateForExtourne(destinationJournal);
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return false;
        }

        return !isOnError();
    }

    /**
     * Créer le journal de destination.
     * 
     * @param journal
     * @return
     * @throws Exception
     */
    private CAJournal addDestinationJournal(CAJournal journal) throws Exception {
        CAJournal destinationJournal = new CAJournal();
        destinationJournal.setSession(getSession());

        String newLibelle = getSession().getLabel(CAProcessExtournerJournal.LABEL_EXTOURNE_JOURNAL_IDENTIFIANT) + " "
                + journal.getLibelle();
        if (newLibelle.length() > 40) {
            newLibelle = getSession().getLabel(CAProcessExtournerJournal.LABEL_EXTOURNE_JOURNAL_IDENTIFIANT);
        }

        destinationJournal.setLibelle(newLibelle);
        destinationJournal.setDateValeurCG(journal.getDateValeurCG());
        destinationJournal.setDate(JACalendar.todayJJsMMsAAAA());

        destinationJournal.add(getTransaction());
        return destinationJournal;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors()) {
            return FWMessageFormat.format(getSession()
                    .getLabel(CAProcessExtournerJournal.LABEL_EXOURNER_JOURNAL_NOT_OK), getIdJournal());
        } else {
            return FWMessageFormat.format(getSession().getLabel(CAProcessExtournerJournal.LABEL_EXOURNER_JOURNAL_OK),
                    getIdJournal());
        }
    }

    /**
     * Retourne l'id du journal à extourner
     * 
     * @return
     */
    public String getIdJournal() {
        return idJournal;
    }

    /**
     * Retrouve le journal à extourner.
     * 
     * @return
     * @throws Exception
     */
    private CAJournal getSourceJournal() throws Exception {
        CAJournal journal = new CAJournal();
        journal.setSession(getSession());
        journal.setIdJournal(getIdJournal());

        journal.retrieve();

        if (journal.isNew()) {
            throw new Exception(getSession().getLabel(CAProcessExtournerJournal.LABEL_5157));
        }

        if (!journal.getEtat().equals(CAJournal.COMPTABILISE)) {
            throw new Exception(getSession().getLabel(CAProcessExtournerJournal.LABEL_JOURNAL_NON_COMPTABILISE));
        }
        return journal;
    }

    /**
     * Liste les opérations du journal (source), cast des opérations et exécute l'extourne.
     * 
     * @param destinationJournal
     * @throws Exception
     */
    private void iterateForExtourne(CAJournal destinationJournal) throws Exception {
        CAOperationManager manager = new CAOperationManager();
        manager.setSession(getSession());
        manager.setForIdJournal(getIdJournal());

        ArrayList etatNotIn = new ArrayList();
        etatNotIn.add(APIOperation.ETAT_INACTIF);
        manager.setForEtatNotIn(etatNotIn);

        BStatement statement = manager.cursorOpen(getTransaction());
        CAOperation operation = null;

        setProgressScaleValue(manager.getCount());
        while (((operation = (CAOperation) manager.cursorReadNext(statement)) != null) && !isAborted()) {
            setProgressDescription(operation.getIdOperation());
            CAOperation oper = operation.getOperationFromType(getTransaction());
            String newLibelle = getSession().getLabel(CAProcessExtournerJournal.LABEL_EXTOURNE_JOURNAL_IDENTIFIANT)
                    + " " + operation.getLibelle();

            if (newLibelle.length() > 40) {
                newLibelle = getSession().getLabel(CAProcessExtournerJournal.LABEL_EXTOURNE_JOURNAL_IDENTIFIANT);
            }

            oper.extourner(getTransaction(), destinationJournal, newLibelle);
            incProgressCounter();
        }
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Set l'id du journal à extourner
     * 
     * @param newIdJournal
     *            String
     */
    public void setIdJournal(String newIdJournal) {
        idJournal = newIdJournal;
    }
}
