package globaz.aquila.ts;

import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.journal.COElementJournalBatch;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;

/**
 * L'interface d'une régle de validation à évaluer lors d'un traitement spécifique sur un contentieux et une transition.
 * 
 * @author vre
 */
public interface COTSValidationRule {

    /**
     * Evalue une règle de validation sur le contentieux lors de la transition donnée et renseigne l'élément de journal
     * si nécessaire.
     * 
     * @param BSession
     * @param BTransaction
     * @param contentieux
     *            le contentieux pour lequel évaluer la règle
     * @param transition
     *            la transition de ce contentieux
     * @param elementJournalBatch
     *            l'élément de journal à modifier si nécessaire
     */
    void validate(BSession session, BTransaction transaction, COContentieux contentieux, COTransition transition,
            COElementJournalBatch elementJournalBatch);
}
