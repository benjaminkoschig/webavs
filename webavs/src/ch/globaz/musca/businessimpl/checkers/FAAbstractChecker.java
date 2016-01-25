package ch.globaz.musca.businessimpl.checkers;

import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;

public abstract class FAAbstractChecker {
    /**
     * Indique si des erreurs ont été logguées
     * 
     * @return <code>true</code> s'il y a des erreurs dans le log, <code>false</code> sinon
     */
    public static boolean hasError() {

        return (null != JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

    }
}
