package globaz.lynx.utils;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.lynx.db.journal.LXJournal;
import globaz.lynx.db.journal.LXJournalManager;

public class LXJournalUtil {

    /**
     * Retrouve le journal de type JOURNALIER pour l'utilisateur. Si aucune résolu, en créé un nouveau d'abord.
     * 
     * @param session
     * @param transaction
     * @param typeJournal
     * @return
     * @throws Exception
     */
    public static LXJournal fetchJournalJournalier(BSession session, BITransaction transaction, String idSociete)
            throws Exception {
        LXJournalManager manager = new LXJournalManager();
        manager.setSession(session);
        manager.setForCsTypeJournal(LXJournal.CS_TYPE_JOURNALIER);
        manager.setForProprietaire(session.getUserName());
        manager.setForCsEtat(LXJournal.CS_ETAT_OUVERT);
        manager.setForDateValeurCG(JACalendar.today().toStr("."));
        manager.setForIdSociete(idSociete);
        manager.find(transaction);

        if (!manager.isEmpty() && !manager.hasErrors()) {
            return (LXJournal) manager.getFirstEntity();
        } else {
            LXJournal journalier = new LXJournal();
            journalier.setSession(session);

            journalier.setDateCreation(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
            journalier.setDateValeurCG(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
            journalier.setCsEtat(LXJournal.CS_ETAT_OUVERT);
            journalier.setIdSociete(idSociete);

            journalier.setCsTypeJournal(LXJournal.CS_TYPE_JOURNALIER);

            journalier.setLibelle(session.getLabel("OPERATIONS_JOURNALIERES"));

            journalier.add(transaction);

            if (journalier.hasErrors()) {
                throw new Exception(journalier.getErrors().toString());
            }

            if (journalier.isNew()) {
                throw new Exception(session.getLabel("JOURNAL_JOURNALIER_ERROR"));
            }

            return journalier;
        }
    }

    /**
     * Return le libellé d'un journal. Utilisé pour les écrans.
     * 
     * @param session
     * @param idJournal
     * @return
     */
    public static String getEtatJournal(BSession session, String idJournal) {
        LXJournal journal = new LXJournal();
        journal.setSession(session);

        journal.setIdJournal(idJournal);

        try {
            journal.retrieve();
        } catch (Exception e) {
            return "";
        }

        if (journal.hasErrors() || journal.isNew()) {
            return "";
        }

        return journal.getCsEtat();
    }

    /**
     * Return le libellé d'un journal. Utilisé pour les écrans.
     * 
     * @param session
     * @param idJournal
     * @return
     */
    public static LXJournal getJournal(BSession session, BTransaction transaction, String idJournal) {
        LXJournal journal = new LXJournal();
        journal.setSession(session);

        journal.setIdJournal(idJournal);

        try {
            journal.retrieve(transaction);
        } catch (Exception e) {
            return null;
        }

        if (journal.hasErrors() || journal.isNew()) {
            return null;
        }

        return journal;
    }

    /**
     * Return le libellé d'un journal. Utilisé pour les écrans.
     * 
     * @param session
     * @param idJournal
     * @return
     */
    public static String getLibelleJournal(BSession session, String idJournal) {
        LXJournal journal = new LXJournal();
        journal.setSession(session);

        journal.setIdJournal(idJournal);

        try {
            journal.retrieve();
        } catch (Exception e) {
            return "";
        }

        if (journal.hasErrors() || journal.isNew()) {
            return "";
        }

        return journal.getIdJournal() + " - " + journal.getLibelle();
    }

    /**
     * Return le libellé d'un journal. Utilisé pour les écrans.
     * 
     * @param session
     * @param idJournal
     * @return
     */
    public static boolean isOuvert(BSession session, String idJournal) {
        LXJournal journal = new LXJournal();
        journal.setSession(session);

        journal.setIdJournal(idJournal);

        try {
            journal.retrieve();
        } catch (Exception e) {
            return false;
        }

        if (journal.hasErrors() || journal.isNew()) {
            return false;
        }

        return LXJournal.CS_ETAT_OUVERT.equals(journal.getCsEtat());
    }

    /**
     * Constructeur
     */
    protected LXJournalUtil() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }

}
