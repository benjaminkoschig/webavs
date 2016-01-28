package ch.globaz.corvus.businessimpl.checkers;

import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;

public class CorvusAbstractChecker {
    /**
     * @return <code>true</code> si le thread est en erreur et que la transaction va se rollbacker
     */
    protected static boolean threadOnError() {
        return JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR);
    }

}
