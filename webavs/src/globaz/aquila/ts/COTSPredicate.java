package globaz.aquila.ts;

import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.poursuite.COContentieux;

/**
 * Une interface qui sert à déterminer si un traitement spécifique doit être effectué sur un contentieux et une
 * transition.
 * 
 * @author vre
 */
public interface COTSPredicate {

    /**
     * Le traitement spécifique doit-il être effectué ?
     * 
     * @param contentieux
     *            le contentieux à évaluer
     * @param transition
     *            la transition pour ce contentieux
     * @param dateExecution
     *            la date de l'exécution de la transition
     * @return vrai s'il faut effectuer un traitement spécifique pour ce contentieux et cette transition.
     * @throws Exception
     *             Si une erreur survient
     */
    boolean evaluate(COContentieux contentieux, COTransition transition, String dateExecution) throws Exception;
}
