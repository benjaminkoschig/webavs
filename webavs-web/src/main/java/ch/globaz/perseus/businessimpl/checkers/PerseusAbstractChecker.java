package ch.globaz.perseus.businessimpl.checkers;

import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;

/**
 * Classe parente de l'ensemble des checker de l'application de gestion des prestations complémentaires familiales
 * 
 * @author vyj
 */
public abstract class PerseusAbstractChecker {
    /**
     * @return <code>true</code> si le thread est en erreur et que la transaction va se rollbacker
     */
    protected static boolean threadOnError() {
        return JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR);
    }

}
