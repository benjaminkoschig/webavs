package ch.globaz.prestation.businessimpl.checkers;

import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;

public abstract class PrestationCommonAbstractChecker {
    /**
     * @return <code>true</code> si le thread est en erreur et que la transaction va se rollbacker
     */
    protected static boolean threadOnError() {
        return JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR);
    }
}
