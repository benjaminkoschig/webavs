package globaz.aquila.process.journal.utils;

import globaz.aquila.db.access.journal.COElementJournalBatch;
import globaz.aquila.db.access.journal.COJournalBatch;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

public class COUtilsJournal {

    /**
     * Return le journal du contentieux aquila pour l'idjournal passée en paramètre.
     * 
     * @param session
     * @param transaction
     * @param idJournal
     * @return le journal du contentieux aquila pour l'idjournal passée en paramètre.
     * @throws Exception
     */
    public static COJournalBatch getJournal(BSession session, BTransaction transaction, String idJournal)
            throws Exception {
        COJournalBatch journal = new COJournalBatch();
        journal.setSession(session);

        journal.setIdJournal(idJournal);

        journal.retrieve(transaction);

        if (journal.hasErrors() || journal.isNew()) {
            throw new Exception(COElementJournalBatch.LABEL_JOURNAL_NON_RENSEIGNE);
        }

        return journal;
    }

    /**
     * L'id du journal est-elle renseignée ?
     * 
     * @param session
     * @param idJournal
     * @throws Exception
     */
    public static void isIdJournalEmpty(BSession session, String idJournal) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(idJournal)) {
            session.addError(session.getLabel(COElementJournalBatch.LABEL_JOURNAL_NON_RENSEIGNE));
            throw new Exception(session.getLabel(COElementJournalBatch.LABEL_JOURNAL_NON_RENSEIGNE));
        }
    }

    /**
     * Mise à jour de l'état du journal.
     * 
     * @param session
     * @param transaction
     * @param idJournal
     * @param etat
     * @throws Exception
     */
    public static void updateEtatJournal(BSession session, BTransaction transaction, String idJournal, String etat)
            throws Exception {
        COJournalBatch journal = COUtilsJournal.getJournal(session, transaction, idJournal);

        journal.setEtat(etat);

        journal.update(transaction);
    }

}
