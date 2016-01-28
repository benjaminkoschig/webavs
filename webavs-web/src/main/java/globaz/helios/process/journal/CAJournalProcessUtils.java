package globaz.helios.process.journal;

import globaz.framework.util.FWLog;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.api.ICGJournal;
import globaz.helios.db.comptes.CGJournal;
import globaz.jade.client.util.JadeStringUtil;

public class CAJournalProcessUtils {

    /**
     * Remise à zéro du log des messages du journal.
     * 
     * @param session
     * @param transaction
     * @param journal
     * @return
     */
    public static boolean resetJournalLog(BSession session, BTransaction transaction, CGJournal journal) {
        try {
            if (!JadeStringUtil.isIntegerEmpty(journal.getIdLog())) {
                FWLog log = new FWLog();
                log.setSession(session);
                log.setIdLog(journal.getIdLog());

                log.retrieve(transaction);

                if (!log.hasErrors() && !log.isNew()) {
                    log.delete(transaction);
                }

                journal.setIdLog("0");
                journal.update(transaction);

                if (transaction.hasErrors()) {
                    throw (new Exception(session.getLabel("COMPTABILISER_JOURNAL_LOG_ERROR")));
                } else {
                    transaction.commit();
                }
            }
        } catch (Exception e) {
            session.addError(e.getMessage());
            try {
                transaction.rollback();
            } catch (Exception ee) {
                return false;
            }
            return false;
        }

        return true;
    }

    /**
     * Mise à jour de l'état du journal à traitement.
     * 
     * @param session
     * @param transaction
     * @param journal
     * @return
     */
    public static boolean setEtatJournalToTraitement(BSession session, BTransaction transaction, CGJournal journal) {
        try {
            journal.setIdEtat(ICGJournal.CS_ETAT_TRAITEMENT);
            journal.update(transaction);

            if (transaction.hasErrors()) {
                throw (new Exception(session.getLabel("COMPTABILISER_JOURNAL_ERROR_1")));
            } else {
                transaction.commit();
            }
        } catch (Exception e) {
            session.addError(e.getMessage());
            try {
                transaction.rollback();
            } catch (Exception ee) {
                return false;
            }
            return false;
        }

        return true;
    }

    /**
     * Mise à jour de l'état du journal.
     * 
     * @param session
     * @param transaction
     * @param journal
     * @param etat
     * @throws Exception
     */
    public static void updateEtatJournal(BSession session, BTransaction transaction, CGJournal journal, String etat)
            throws Exception {
        journal.setIdEtat(etat);
        journal.update(transaction);

        if (transaction.hasErrors()) {
            transaction.rollback();
            throw (new Exception(session.getLabel("COMPTABILISER_JOURNAL_ERR_1")));
        } else {
            transaction.commit();
        }
    }

    /**
     * Mise à jour du log du journal après traitement.
     * 
     * @param session
     * @param transaction
     * @param journal
     * @param journalLog
     * @param memoryLog
     * @return
     */
    public static boolean updateLogJournal(BSession session, BTransaction transaction, CGJournal journal,
            FWMemoryLog journalLog, FWMemoryLog memoryLog) {
        try {
            if (journalLog.hasMessages()) {
                FWLog log = journalLog.saveToFWLog(transaction);

                if (log == null || log.isNew()) {
                    throw (new Exception(session.getLabel("COMPTABILISER_JOURNAL_ERROR_6")));
                } else {
                    journal.setIdLog(log.getIdLog());
                    journal.update(transaction);
                }
            }

            if (transaction.hasErrors()) {
                throw (new Exception(session.getLabel("COMPTABILISER_JOURNAL_ERROR_6")));
            } else {
                transaction.commit();
            }
        } catch (Exception e) {
            memoryLog.logMessage(e.getMessage(), FWMessage.AVERTISSEMENT, session.getLabel("GLOBAL_JOURNAL") + " N°"
                    + journal.getNumero());
            // _addError(e.getMessage());
            try {
                transaction.rollback();
            } catch (Exception ee) {
                return false;
            }
        }

        return true;
    }
}
