package globaz.aquila.ts;

import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.journal.COElementJournalBatch;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import java.util.List;

/**
 * Validation du traitement sp�cifique pour un contentieux et une transition.<br/>
 * Test ces crit�res (Exemple : Pour FER la validation va test� la longueur et caract�res contenu dans la nom d'un
 * tiers)<br/>
 * Si ces crit�res sont remplis un �l�ment de journal sera cr�� pour ce contentieux et cette transition.
 * 
 * @author DDA
 */
public interface COTSValidator {

    /**
     * Valide un traitement sp�cifique sur un contentieux et une transition.
     * 
     * @param BSession
     * @param BTransaction
     * @param contentieux
     *            le contentieux sur lequel effectuer le traitement sp�cifique
     * @param transition
     *            la transition pour le contentieux
     * @return un �l�ment de journal qui d�crit le r�sultat du traitement, jamais nul, peut-�tre sans erreurs.
     */
    COElementJournalBatch validate(BSession session, BTransaction transaction, COContentieux contentieux,
            COTransition transition);

    /**
     * Retourne une vue non modifiable des r�gles de validation qui sont lanc�es par ce validateur.
     * 
     * @return une liste non modifiable, jamais nulle, peut-�tre vide
     */
    List validationRules();
}
