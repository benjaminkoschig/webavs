package globaz.aquila.ts;

import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.journal.COElementJournalBatch;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import java.util.List;

/**
 * Validation du traitement spécifique pour un contentieux et une transition.<br/>
 * Test ces critères (Exemple : Pour FER la validation va testé la longueur et caractères contenu dans la nom d'un
 * tiers)<br/>
 * Si ces critères sont remplis un élément de journal sera créé pour ce contentieux et cette transition.
 * 
 * @author DDA
 */
public interface COTSValidator {

    /**
     * Valide un traitement spécifique sur un contentieux et une transition.
     * 
     * @param BSession
     * @param BTransaction
     * @param contentieux
     *            le contentieux sur lequel effectuer le traitement spécifique
     * @param transition
     *            la transition pour le contentieux
     * @return un élément de journal qui décrit le résultat du traitement, jamais nul, peut-être sans erreurs.
     */
    COElementJournalBatch validate(BSession session, BTransaction transaction, COContentieux contentieux,
            COTransition transition);

    /**
     * Retourne une vue non modifiable des règles de validation qui sont lancées par ce validateur.
     * 
     * @return une liste non modifiable, jamais nulle, peut-être vide
     */
    List validationRules();
}
