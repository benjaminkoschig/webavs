package ch.globaz.al.businessimpl.checker;

import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;

/**
 * Classe mère de tous les checkers
 * 
 * @author jts
 */
public abstract class ALAbstractChecker {

    /**
     * Indique si des erreurs ont été logguées
     * 
     * @return <code>true</code> s'il y a des erreurs dans le log, <code>false</code> sinon
     */
    public static boolean hasError() {

        return (null != JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

    }
}
