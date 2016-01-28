package globaz.aquila.ts;

import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.journal.COElementJournalBatch;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;

/**
 * L'interface d'une r�gle de validation � �valuer lors d'un traitement sp�cifique sur un contentieux et une transition.
 * 
 * @author vre
 */
public interface COTSValidationRule {

    /**
     * Evalue une r�gle de validation sur le contentieux lors de la transition donn�e et renseigne l'�l�ment de journal
     * si n�cessaire.
     * 
     * @param BSession
     * @param BTransaction
     * @param contentieux
     *            le contentieux pour lequel �valuer la r�gle
     * @param transition
     *            la transition de ce contentieux
     * @param elementJournalBatch
     *            l'�l�ment de journal � modifier si n�cessaire
     */
    void validate(BSession session, BTransaction transaction, COContentieux contentieux, COTransition transition,
            COElementJournalBatch elementJournalBatch);
}
