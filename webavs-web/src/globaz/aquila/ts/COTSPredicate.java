package globaz.aquila.ts;

import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.poursuite.COContentieux;

/**
 * Une interface qui sert � d�terminer si un traitement sp�cifique doit �tre effectu� sur un contentieux et une
 * transition.
 * 
 * @author vre
 */
public interface COTSPredicate {

    /**
     * Le traitement sp�cifique doit-il �tre effectu� ?
     * 
     * @param contentieux
     *            le contentieux � �valuer
     * @param transition
     *            la transition pour ce contentieux
     * @param dateExecution
     *            la date de l'ex�cution de la transition
     * @return vrai s'il faut effectuer un traitement sp�cifique pour ce contentieux et cette transition.
     * @throws Exception
     *             Si une erreur survient
     */
    boolean evaluate(COContentieux contentieux, COTransition transition, String dateExecution) throws Exception;
}
