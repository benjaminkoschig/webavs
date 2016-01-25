package ch.globaz.param.businessimpl.checker;

import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;

public abstract class ParamAbstractChecker {
    /**
     * Indique si des erreurs ont �t� loggu�es
     * 
     * @return <code>true</code> s'il y a des erreurs dans le log, <code>false</code> sinon
     */
    public static boolean hasError() {

        return (null != JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

    }

}
