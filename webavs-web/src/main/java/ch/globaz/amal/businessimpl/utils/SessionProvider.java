package ch.globaz.amal.businessimpl.utils;

import globaz.globall.db.BSession;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadContext;

public class SessionProvider {
    private final static String OBJ_BSESSION = "bsession";

    /**
     * Recherche une session dans le context
     * 
     * @return
     */
    public static BSession findSession() {
        JadeThreadContext ctx = JadeThread.currentContext();
        if (ctx == null) {
            return null;
        }
        Object session = ctx.getTemporaryObject(SessionProvider.OBJ_BSESSION);
        if (session == null) {
            return null;
        }
        if (session instanceof BSession) {
            return (BSession) session;
        } else {
            return null;
        }
    }
}
