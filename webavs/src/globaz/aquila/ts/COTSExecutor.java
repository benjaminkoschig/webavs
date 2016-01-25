package globaz.aquila.ts;

import globaz.aquila.db.access.journal.COElementJournalBatch;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;

/**
 * Exécution du traitement spécifique (Exemple : FER création des informations pour fichier RDP).
 * 
 * @author DDA
 */
public interface COTSExecutor {

    public String execute(BSession session, BTransaction transaction, COElementJournalBatch element) throws Exception;

}
